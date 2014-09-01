/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.FileItem;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureImg;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Util class used to manipulate an activity
 * @author aartimon@dginternational.org 
 * @since Jun 17, 2011
 */
public class ActivityUtil {
	private static final Logger logger = Logger.getLogger(ActivityUtil.class);

	/**
	 * types for {@link org.digijava.module.aim.dbentity.AmpActivityFields#activityType} 
	 */
    public static final Long ACTIVITY_TYPE_PROJECT = 0L;
    public static final Long ACTIVITY_TYPE_SSC = 1L;
	
	/**
	 * Method used to save an Activity/ActivityVersion depending
	 * on activation of versioning option
	 * @param am
	 */
	public static void saveActivity(AmpActivityModel am, boolean draft,boolean rejected){
		Session session = AmpActivityModel.getHibernateSession();
		AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
		if (!wicketSession.getLocale().getLanguage().equals(TLSUtils.getLangCode())){
			logger.error("WRONG LANGUAGE: TLSUtils(" + TLSUtils.getLangCode() + ") vs Wicket(" + wicketSession.getLocale().getLanguage() + ")");
		}
		
		AmpActivityVersion oldA = am.getObject();

		boolean newActivity = oldA.getAmpActivityId() == null;
		try 
		{
			AmpTeamMember ampCurrentMember = wicketSession.getAmpCurrentMember();
			AmpActivityVersion a = saveActivityNewVersion(am.getObject(), am.getTranslationHashMap().values(), 
					ampCurrentMember, draft, session, rejected, true);
			am.setObject(a);
		} catch (Exception exception) {
			logger.error("Error saving activity:", exception); // Log the exception			
			throw new RuntimeException("Can't save activity:", exception);

		} finally {
			ActivityGatekeeper.unlockActivity(String.valueOf(am.getId()), am.getEditingKey());
			AmpActivityModel.endConversation();
			try {
				ServletContext sc = wicketSession.getHttpSession().getServletContext();
				Site site = wicketSession.getSite();
				Locale locale = wicketSession.getLocale();
				LuceneUtil.addUpdateActivity(sc.getRealPath("/"), !newActivity, site, locale, am.getObject(), oldA);
			} catch (Exception e) {
				logger.error("error while trying to update lucene logs:", e);
			}		
		}
	}
	
	/**
	 * saves a new version of an activity
	 * returns newActivity
	 */
	public static AmpActivityVersion saveActivityNewVersion(AmpActivityVersion a, Collection<AmpContentTranslation> translations, 
			AmpTeamMember ampCurrentMember, boolean draft, Session session, boolean rejected, boolean isActivityForm) throws Exception
	{
		//saveFundingOrganizationRole(a);
        AmpActivityVersion oldA = a;
		boolean newActivity = false;
		
		if (a.getAmpActivityId() == null){
			a.setActivityCreator(ampCurrentMember);
			a.setCreatedBy(ampCurrentMember);
			a.setTeam(ampCurrentMember.getAmpTeam());
			newActivity = true;
		}
		
		if (a.getDraft() == null)
			a.setDraft(false);
		boolean draftChange = draft != a.getDraft();
		a.setDraft(draft);

		a.setDeleted(false);

        if (ContentTranslationUtil.multilingualIsEnabled())
            ContentTranslationUtil.cloneTranslations(a, translations);

		//is versioning activated?
        boolean createNewVersion = (draft == draftChange) && ActivityVersionUtil.isVersioningEnabled();
		if (createNewVersion){
			try {
				AmpActivityGroup tmpGroup = a.getAmpActivityGroup();
				
				a = ActivityVersionUtil.cloneActivity(a, ampCurrentMember);
				//keeping session.clear() only for acitivity form as it was before
				if (isActivityForm)
					session.clear();
				if (tmpGroup == null){
					//we need to create a group for this activity
					tmpGroup = new AmpActivityGroup();
					tmpGroup.setAmpActivityLastVersion(a);
					
					session.save(tmpGroup);
				}
				
				a.setAmpActivityGroup(tmpGroup);
				a.setMember(new HashSet());
				a.setAmpActivityId(null);
				if (oldA.getAmpActivityId() != null)
					session.evict(oldA);
			} catch (CloneNotSupportedException e) {
				logger.error("Can't clone current Activity: ", e);
			}
		}
		
		if (a.getAmpActivityGroup() == null){
			//we need to create a group for this activity
			AmpActivityGroup tmpGroup = new AmpActivityGroup();
			tmpGroup.setAmpActivityLastVersion(a);
			a.setAmpActivityGroup(tmpGroup);
			session.save(tmpGroup);
		}
		
		setCreationTimeOnStructureImages(a);

		AmpActivityGroup group = a.getAmpActivityGroup();
		if (group == null){
			throw new RuntimeException("Non-existent group should have been added by now!");
		}
		
		if (!newActivity){
			//existing activity
			//previousVersion for current activity
			group.setAmpActivityLastVersion(a);
			session.update(group);
		}
		a.setAmpActivityGroup(group);
		Date updatedDate = Calendar.getInstance().getTime();
		if (a.getCreatedDate() == null)
			a.setCreatedDate(updatedDate);
		a.setUpdatedDate(updatedDate);
		a.setModifiedDate(updatedDate);
		a.setModifiedBy(ampCurrentMember);
		
		if (isActivityForm) {
			setActivityStatus(ampCurrentMember, draft, a, oldA, newActivity,rejected);
			
			saveContacts(a, session,(draft != draftChange));
			saveIndicators(a, session);

			saveResources(a); 
			saveEditors(session, createNewVersion); 
			saveComments(a, session,draft); 
			saveAgreements(session);
		}
		
		updateComponentFunding(a, session);
		saveAnnualProjectBudgets(a, session);
	
        if (createNewVersion){
            //a.setAmpActivityId(null); //hibernate will save as a new version
            session.save(a);
        }
        else{
            session.saveOrUpdate(a);
            //session.update(a);
        }

        if (newActivity){
            //translations need cloning again or the update on the activity will fail
            ContentTranslationUtil.cloneTranslations(a);
            a.setAmpId(org.digijava.module.aim.util.ActivityUtil.generateAmpId(ampCurrentMember.getUser(), a.getAmpActivityId(), session));
            session.update(a);
        }
        return a;
	}

	
	private static void setCreationTimeOnStructureImages(AmpActivityVersion activity){
		if (activity.getStructures() != null){
			for(AmpStructure str :  activity.getStructures()){
				if (str.getImages() != null){
					for(AmpStructureImg img : str.getImages()){
						img.setStructure(str);
						img.setCreationTime(System.currentTimeMillis());
					}
				}
			}
		}
	}

	private static void setActivityStatus(AmpTeamMember ampCurrentMember, boolean draft, AmpActivityFields a, AmpActivityVersion oldA, boolean newActivity,boolean rejected) {
		Long teamMemberTeamId=ampCurrentMember.getAmpTeam().getAmpTeamId();
		String validation=org.digijava.module.aim.util.DbUtil.getValidationFromTeamAppSettings(teamMemberTeamId);
		
		//setting activity status....
		AmpTeamMemberRoles role = ampCurrentMember.getAmpMemberRole();
		boolean teamLeadFlag =  role.getTeamHead() || role.isApprover();
		Boolean crossTeamValidation = ampCurrentMember.getAmpTeam().getCrossteamvalidation();
		Boolean isSameWorkspace = ampCurrentMember.getAmpTeam().getAmpTeamId()==a.getTeam().getAmpTeamId();
		
		// Check if validation is ON in GS and APP Settings 
		if ("On".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION))
				&& !"validationOff".equalsIgnoreCase(validation)) {
			if (teamLeadFlag) {
				if (draft) {
					if (rejected) {
						a.setApprovalStatus(Constants.REJECTED_STATUS);
					} else {
						a.setApprovalStatus(Constants.STARTED_APPROVED_STATUS);
					}
				} else {
					// If activity belongs to the same workspace where TL/AP is
					// logged set it validated
					if (isSameWorkspace) {
						a.setApprovalStatus(Constants.APPROVED_STATUS);
						a.setApprovedBy(ampCurrentMember);
						a.setApprovalDate(Calendar.getInstance().getTime());
					} else {
						/*
						 * If activity doesn't belongs to the same workspace
						 * where TL/AP is logged but cross team validation is on
						 * set it validated
						 */
						if (crossTeamValidation) {
							a.setApprovalStatus(Constants.APPROVED_STATUS);
							a.setApprovedBy(ampCurrentMember);
							a.setApprovalDate(Calendar.getInstance().getTime());
						} else {
							a.setApprovalStatus(Constants.STARTED_STATUS);
						}
					}
				}
			} else {
				if ("newOnly".equals(validation)) {
					if (newActivity) {
						// all the new activities will have the started status
						a.setApprovalStatus(Constants.STARTED_STATUS);
					} else {
						// if we edit an existing not validated status it will
						// keep the old status - started
						if (oldA.getApprovalStatus() != null
								&& Constants.STARTED_STATUS.compareTo(oldA.getApprovalStatus()) == 0)
							a.setApprovalStatus(Constants.STARTED_STATUS);
						// if we edit an existing activity that is validated or
						// startedvalidated or edited
						else
							a.setApprovalStatus(Constants.APPROVED_STATUS);
					}
				} else {
					if ("allEdits".equals(validation)) {
						if (newActivity) {
							a.setApprovalStatus(Constants.STARTED_STATUS);
						} else {
							if (oldA.getApprovalStatus() != null
									&& Constants.STARTED_STATUS.compareTo(oldA.getApprovalStatus()) == 0)
								a.setApprovalStatus(Constants.STARTED_STATUS);
							else
								a.setApprovalStatus(Constants.EDITED_STATUS);
						}
					}
				}

			}

		} else {
			// Validation is OF in GS activity approved
			if (newActivity) {
				a.setApprovalStatus(Constants.STARTED_APPROVED_STATUS);
			} else {
				a.setApprovalStatus(Constants.APPROVED_STATUS);
			}
			a.setApprovedBy(ampCurrentMember);
			a.setApprovalDate(Calendar.getInstance().getTime());
		}
	}

	/**
	 * Method used to load the last version of an object
	 * @param am
     * @param id
     * @return activity loaded
	 */
	public static AmpActivityVersion load(AmpActivityModel am, Long id) {
		if (id == null){
			return new AmpActivityVersion();
		}
			
		Session session = am.getHibernateSession();//am.getSession();
		
		
		//am.setTransaction(session.beginTransaction());
		
		/**
		 * try to use optimistic locking
		 */
		AmpActivityVersion act = (AmpActivityVersion) session.get(AmpActivityVersion.class, id);

		//check the activity group for the last version of an activity
		AmpActivityGroup group = act.getAmpActivityGroup();
		if (group == null){ //Activity created previous to the versioning system?
			//we need to create a group for this activity
			group = new AmpActivityGroup();
			group.setAmpActivityLastVersion(act);
			
			session.save(group);
		}

		if (act.getDraft() == null)
			act.setDraft(false);
		act.setAmpActivityGroup(group);
		
		if (act.getComponentFundings() != null)
			act.getComponentFundings().size();
		if (act.getComponentProgress() != null)
			act.getComponentProgress().size();
		if (act.getCosts() != null)
			act.getCosts().size();
		if (act.getMember() != null)
			act.getMember().size();
		if (act.getContracts() != null)
			act.getContracts().size();
		if (act.getIndicators() != null)
			act.getIndicators().size();
		
		
		
		return act;
	}


	private static void updateComponentFunding(AmpActivityVersion a,
			Session session) {
		if (a.getComponentFundings() == null || a.getComponents() == null)
			return;
		Iterator<AmpComponentFunding> it1 = a.getComponentFundings().iterator();
		while (it1.hasNext()) {
			AmpComponentFunding cf = (AmpComponentFunding) it1
					.next();
			Iterator<AmpComponent> it2 = a.getComponents().iterator();
			while (it2.hasNext()) {
				AmpComponent comp = (AmpComponent) it2.next();
				if (comp.getTitle().compareTo(cf.getComponent().getTitle()) == 0){
					cf.setComponent(comp);
					break;
				}
			}
			session.saveOrUpdate(cf);
		}
	}

	private static void saveComments(AmpActivityVersion a, Session session, boolean draft) {
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		
		
		HashSet<AmpComments> newComm = s.getMetaData(OnePagerConst.COMMENTS_ITEMS);
		HashSet<AmpComments> delComm = s.getMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS);
		
		if (delComm != null){
			Iterator<AmpComments> di = delComm.iterator();
			while (di.hasNext()) {
				AmpComments tComm = (AmpComments) di.next();
				session.delete(tComm);
			}
		}

		if (newComm != null){
			Iterator<AmpComments> ni = newComm.iterator();
			while (ni.hasNext()) {
				AmpComments tComm = (AmpComments) ni.next();
				if (ActivityVersionUtil.isVersioningEnabled() && !draft){
					try {
						tComm = (AmpComments) tComm.prepareMerge(a);
					} catch (CloneNotSupportedException e) {
						logger.error("can't clone: ", e);
					}
				}
					
				if (tComm.getMemberId() == null)
					tComm.setMemberId(((AmpAuthWebSession)org.apache.wicket.Session.get()).getAmpCurrentMember());
				if (tComm.getAmpActivityId() == null)
					tComm.setAmpActivityId(a);
				session.saveOrUpdate(tComm);
			}
		}
	}

	private static void saveEditors(Session session, boolean createNewVersion) {
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		EditorStore editorStore = s.getMetaData(OnePagerConst.EDITOR_ITEMS);
		HashMap<String, HashMap<String, String>> editors = editorStore.getValues();
		
		AmpAuthWebSession wicketSession = ((AmpAuthWebSession)org.apache.wicket.Session.get());
		
		//String currentLanguage = TLSUtils.getLangCode();
		if (editors == null || editors.keySet() == null)
			return;
		Iterator<String> it = editors.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String oldKey = editorStore.getOldKey().get(key);
			HashMap<String, String> values = editors.get(key);
            Set<String> locales = values.keySet();

            for (String locale: locales){
                String value = values.get(locale);

                if (value == null || value.trim().length() == 0)
                    continue; //we don't save empty editors

                try {
                    boolean editorFound = false;
                    List<Editor> edList = DbUtil.getEditorList(oldKey, wicketSession.getSite());
                    Iterator<Editor> it2 = edList.iterator();
                    while (it2.hasNext()) {
                        Editor editor = (Editor) it2.next();
                        if (editor.getLanguage().equals(locale)){
                            //editor.setBody(value);
                            editorFound = true;

                            //create a new editor and copy the old values
                            Editor toSaveEditor = new Editor();
                            toSaveEditor.setBody(value);
                            toSaveEditor.setSiteId(editor.getSiteId());
                            toSaveEditor.setLanguage(editor.getLanguage());
                            toSaveEditor.setEditorKey(key);
                            session.save(toSaveEditor);

                            if (!createNewVersion){
                                //we need to delete the old editor since this is not a new activity version
                                session.delete(editor);
                            }

                            break;
                        }
                    }

                    if (!editorFound){
                        //add new editor
                        Editor editor = new Editor();
                        editor.setBody(value);
                        editor.setSite(wicketSession.getSite());
                        editor.setLanguage(locale);
                        editor.setEditorKey(key);
                        session.saveOrUpdate(editor);
                    }

                } catch (EditorException e) {
                    logger.error("Can't get editor:", e);
                }
            }


		}
	}

	private static void saveAgreements(Session session) {

		AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();
		HashSet<AmpAgreement> agreements = s.getMetaData(OnePagerConst.AGREEMENT_ITEMS);
		// AmpFundiong
		if (agreements == null)
			return;
		Iterator<AmpAgreement> it = agreements.iterator();
		while (it.hasNext()) {
			AmpAgreement agg = (AmpAgreement) it.next();
			if (agg.getId() == null || agg.getId().equals(-1L)) {
				agg.setId(null);
				session.save(agg);
			} else {
				session.merge(agg);
			}
		}
		session.flush();
	}

	private static void saveResources(AmpActivityVersion a) {
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		
		HttpServletRequest req = SessionUtil.getCurrentServletRequest();
		
		if (a.getActivityDocuments() == null)
			a.setActivityDocuments(new HashSet<AmpActivityDocument>());

		HashSet<TemporaryDocument> newResources = s.getMetaData(OnePagerConst.RESOURCES_NEW_ITEMS);
		HashSet<AmpActivityDocument> deletedResources = s.getMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS);
        HashSet<TemporaryDocument> existingTitles = s.getMetaData(OnePagerConst.RESOURCES_EXISTING_ITEM_TITLES);

        /*
         * update titles
         */
        if (existingTitles != null && !existingTitles.isEmpty()) {
            for (TemporaryDocument d : existingTitles) {
                Node node = DocumentManagerUtil.getWriteNode(d.getExistingDocument().getUuid(), req);
                if (node != null) {
                    NodeWrapper nw = new NodeWrapper(node);
                    if (!nw.getTitle().equals(d.getTitle())) {
                        if (d.getWebLink() != null && d.getWebLink().trim().length() > 0 &&
                                (d.getFileName() == null || d.getFileName().trim().length()==0)) {
                            d.setFileName(d.getWebLink());
                        }

                        if (!deletedResources.contains(d.getExistingDocument())) {
                            String contentType = nw.getContentType();
                            String fileName = nw.getName();
                            Bytes fileSize = null;
                            InputStream fileData = null;
                            try {
                                Property data = nw.getNode().getProperty(CrConstants.PROPERTY_DATA);
                                fileData = data.getStream();
                                
                                Property size = nw.getNode().getProperty(CrConstants.PROPERTY_FILE_SIZE);
                                fileSize = Bytes.bytes(size.getLong());
                                DocumentManagerUtil.logoutJcrSessions(req);
                            } catch (RepositoryException e) {
                                logger.error("Error while getting data stream from JCR:", e);
                            }


                            class FileItemEx implements FileItem{
                                private String contentType;
                                private String fileName;
                                private Bytes fileSize;
                                private InputStream fileData;


                                public FileItemEx(String fileNameIn, String contentTypeIn, InputStream fileDataIn, Bytes fileSizeIn) {
                                    fileName = fileNameIn;
                                    contentType = contentTypeIn;
                                    fileData = fileDataIn;
                                    fileSize = fileSizeIn;

                                }



                                @Override
                                public InputStream getInputStream() throws IOException {
                                    return fileData;  //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public String getContentType() {
                                    return contentType;  //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public String getName() {
                                    return fileName;  //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public boolean isInMemory() {
                                    return false;  //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public long getSize() {
                                    return fileSize.bytes();  //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public byte[] get() {
                                    return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public String getString(String s) throws UnsupportedEncodingException {
                                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public String getString() {
                                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public void write(File file) {
                                    //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public void delete() {
                                    //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public String getFieldName() {
                                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public void setFieldName(String s) {
                                    //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public boolean isFormField() {
                                    return false;  //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public void setFormField(boolean b) {
                                    //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public OutputStream getOutputStream() throws IOException {
                                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                                }
                            }

                            FileUpload file = new FileUpload(new FileItemEx(fileName, contentType, fileData, fileSize));
                            d.setFile(file);
                            newResources.add(d);
                            deletedResources.add(d.getExistingDocument());
                        }
                    }
                }
            }
        }


		/*
		 * remove old resources
		 */
		if (deletedResources != null){
			Iterator<AmpActivityDocument> it = deletedResources.iterator();
			while (it.hasNext()) {
				AmpActivityDocument tmpDoc = (AmpActivityDocument) it
				.next();
				Iterator<AmpActivityDocument> it2 = a.getActivityDocuments().iterator();
				while (it2.hasNext()) {
					AmpActivityDocument existDoc = (AmpActivityDocument) it2
							.next();
					if (existDoc.getUuid().compareTo(tmpDoc.getUuid()) == 0){
						it2.remove();
						break;
					}
				}
			}
		}
		
		/*
		 * Add new resources
		 */
		if (newResources != null){
			Iterator<TemporaryDocument> it2 = newResources.iterator();
			while (it2.hasNext()) {
				TemporaryDocument temp = (TemporaryDocument) it2
				.next();
				TemporaryDocumentData tdd = new TemporaryDocumentData(); 
				tdd.setTitle(temp.getTitle());
				tdd.setName(temp.getFileName());
				tdd.setDescription(temp.getDescription());
				tdd.setNotes(temp.getNote());
				if(temp.getType()!=null)
					tdd.setCmDocTypeId(temp.getType().getId());
				tdd.setDate(temp.getDate());
				tdd.setYearofPublication(temp.getYear());
				
				if (temp.getWebLink() == null || temp.getWebLink().length() == 0){
                    if (temp.getFile() != null){

                        tdd.setFileSize(temp.getFile().getSize());

                        final FileUpload file = temp.getFile();
                        /**
                         * For Document Manager compatibility purposes
                         */
                        final FormFile formFile = new FormFile() {

                            @Override
                            public void setFileSize(int arg0) {
                            }

                            @Override
                            public void setFileName(String arg0) {
                            }

                            @Override
                            public void setContentType(String arg0) {
                            }

                            @Override
                            public InputStream getInputStream() throws FileNotFoundException,
                                    IOException {
                                return file.getInputStream();
                            }

                            @Override
                            public int getFileSize() {
                                return (int) file.getSize();
                            }

                            @Override
                            public String getFileName() {
                                return file.getClientFileName();
                            }

                            @Override
                            public byte[] getFileData() throws FileNotFoundException, IOException {
                                return file.getBytes();
                            }

                            @Override
                            public String getContentType() {
                                return file.getContentType();
                            }

                            @Override
                            public void destroy() {
                            }
                        };
                        tdd.setFormFile(formFile);
                    }
				}
				
				tdd.setWebLink(temp.getWebLink());
				
				ActionMessages messages = new ActionMessages();
				NodeWrapper node = tdd.saveToRepository(SessionUtil.getCurrentServletRequest(), messages);
				
				AmpActivityDocument aad = new AmpActivityDocument();
				aad.setAmpActivity(a);
				aad.setDocumentType(ActivityDocumentsConstants.RELATED_DOCUMENTS);
				aad.setUuid(node.getUuid());
				a.getActivityDocuments().add(aad);
			}
		}
	}

	private static void saveIndicators(AmpActivityVersion a, Session session) throws Exception {
		if (a.getAmpActivityId() != null){
			//cleanup old indicators
			Set<IndicatorActivity> old = IndicatorUtil.getAllIndicatorsForActivity(a.getAmpActivityId());
			
			if (old != null){
				Iterator<IndicatorActivity> it = old.iterator();
				while (it.hasNext()) {
					IndicatorActivity oldInd = (IndicatorActivity) it
					.next();
					
					boolean found=false;
					if (a.getIndicators() == null)
						continue;
					Iterator<IndicatorActivity> it2 = a.getIndicators().iterator();
					while (it2.hasNext()) {
						IndicatorActivity newind = (IndicatorActivity) it2
								.next();
						if ((newind.getId() != null) && (newind.getId().compareTo(oldInd.getId()) == 0)){
							found=true;
							break;
						}
					}
					if (!found){
						Object tmp = session.load(IndicatorActivity.class, oldInd.getId());
						session.delete(tmp);
					}
				}
			}
			
		}
		
		Set<IndicatorActivity> inds = a.getIndicators();
		if (inds != null){
			Iterator<IndicatorActivity> it = inds.iterator();
			while (it.hasNext()) {
				IndicatorActivity ind = (IndicatorActivity) it
						.next();
				ind.setActivity(a);
				session.saveOrUpdate(ind);
			}
		}
	}

    public static void saveContacts(AmpActivityVersion a, Session session,boolean checkForContactsRemoval) throws Exception {
        Set<AmpActivityContact> activityContacts=a.getActivityContacts();
        // if activity contains contact,which is not in contact list, we should remove it
        Long oldActivityId = a.getAmpActivityId();
        if(oldActivityId != null){
            if(checkForContactsRemoval || !ActivityVersionUtil.isVersioningEnabled()){
                //List<AmpActivityContact> activityDbContacts=ContactInfoUtil.getActivityContacts(oldActivityId);
                List<Long> activityDbContactsIds=ContactInfoUtil.getActivityContactIds(oldActivityId);
                if(activityDbContactsIds!=null && activityDbContactsIds.size()>0){
                    for (Long actContactId : activityDbContactsIds) {
                        int count = 0;
                        if (activityContacts != null) {
                            for (AmpActivityContact activityContact : activityContacts) {
                                if (activityContact.getId() != null && activityContact.getId().equals(actContactId)) {
                                    count++;
                                    break;
                                }
                            }
                        }
                        if (count == 0) { //if activity contains contact,which is not in contact list, we should remove it
                            Query qry = session.createQuery("delete from " + AmpActivityContact.class.getName() + " a where a.id=" + actContactId);
                            qry.executeUpdate();
                        }
                    }
                }

            }
        }

        //add or edit activity contact and amp contact
        if(activityContacts != null && activityContacts.size() > 0) {
            for (AmpActivityContact activityContact : activityContacts) {

                //we have to check if the contact is new, first we have to save it

                // save the contact first
                if (activityContact.getContact().getId() == null) {
                    session.saveOrUpdate(activityContact.getContact());
                }

                // then the reference
                if (activityContact.getId() == null) {
                    session.saveOrUpdate(activityContact);
                }
                
                //session.merge(activityContact.getContact());
            }
        }



    }
    
    
	private static void saveAnnualProjectBudgets(AmpActivityVersion a,
			Session session) throws Exception {
		if (a.getAmpActivityId() != null) {
			Iterator<AmpAnnualProjectBudget> it = a.getAnnualProjectBudgets()
					.iterator();
			while (it.hasNext()) {
				AmpAnnualProjectBudget annualBudget = it.next();
				annualBudget.setActivity(a);
				session.saveOrUpdate(annualBudget);
			}

		}
	}



}
