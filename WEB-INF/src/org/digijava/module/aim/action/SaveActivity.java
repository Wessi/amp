/*
 * SaveActivity.java
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jcr.Node;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Session;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.error.AMPActivityError;
import org.dgfoundation.amp.error.AMPError;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityComponente;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.EUActivityContribution;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DesktopUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;

/**
 * SaveActivity class creates a 'AmpActivity' object and populate the fields
 * with the values entered by the user and passes this object to the persister
 * class.
 *
 */
public class SaveActivity extends Action {

	private static Logger logger = Logger.getLogger(SaveActivity.class);

	private ServletContext ampContext = null;

	
	
	
	
	private void processPreStep(EditActivityForm eaForm, AmpActivity activity, TeamMember tm, Boolean[] createdAsDraft) throws Exception, AMPError{
		// if the activity is being added from a users workspace,
		// associate the
		// activity with the team of the current member.
		if (tm != null && (eaForm.isEditAct() == false)) {
			AmpTeam team = TeamUtil.getAmpTeam(tm.getTeamId());
			activity.setTeam(team);
		} else {
			activity.setTeam(null);
		}

		
		if (activity.getCategories() == null) {
			activity.setCategories( new HashSet() );
		}

		if(!eaForm.isEditAct()){
			activity.setCreatedAsDraft(eaForm.getDraft());
			createdAsDraft[0]=eaForm.getDraft();
	}
		else{
			if(eaForm.getWasDraft()&&!eaForm.getDraft()){
				activity.setCreatedAsDraft(false);
				createdAsDraft[0]=false;
			}
			else{
				createdAsDraft[0]=true;
			}
		}
	}
	
	
	
	/**
	 * this method is also called from processStep1
	 * it is necessary in the saveRecovery if processStep1 fails 
	 * 
	 * these are required informations for an activity to be able to be created as draft
	 */ 
	private void processActivityMustHaveInfo(EditActivityForm eaForm, AmpActivity activity) throws Exception, AMPError{
		activity.setName(eaForm.getIdentification().getTitle());
	}
	
	private void processStep1(boolean check, EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request) throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			if (eaForm.getIdentification().getTitle() != null) {
				if (eaForm.getIdentification().getTitle().trim().length() == 0) {
					errors.add("title", new ActionError(
					"error.aim.addActivity.titleMissing"));
				} else if (eaForm.getIdentification().getTitle().length() > 255) {
					errors.add("title", new ActionError(
					"error.aim.addActivity.titleTooLong"));
				}
			}
			
			Long statId=eaForm.getPlanning().getStatusId();
			
			if(isStatusEnabled()){
				if(eaForm.getDraft()==null || !eaForm.getDraft().booleanValue()){
					if (statId != null && statId.equals(new Long(0))) {
						errors.add("status", new ActionError(
						"error.aim.addActivity.statusMissing"));
					}
				}
			}
			
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		activity.setAmpId(eaForm.getAmpId());
		processActivityMustHaveInfo(eaForm, activity);
		activity.setBudget(Boolean.parseBoolean(eaForm.getIdentification().getBudgetCheckbox()));
		
		if (eaForm.getIdentification().getDescription() == null
				|| eaForm.getIdentification().getDescription().trim().length() == 0) {
			activity.setDescription(new String(" "));
		} else {
			activity.setDescription(eaForm.getIdentification().getDescription());
		}
		
		if (eaForm.getIdentification().getLessonsLearned() == null
				|| eaForm.getIdentification().getLessonsLearned().trim().length() == 0) {
			activity.setLessonsLearned(new String(" "));
		} else {
			activity.setLessonsLearned(eaForm.getIdentification().getLessonsLearned());
		}

		if (eaForm.getIdentification().getProjectImpact() == null
				|| eaForm.getIdentification().getProjectImpact().trim().length() == 0) {
			activity.setProjectImpact(new String(" "));
		} else {
			activity.setProjectImpact(eaForm.getIdentification().getProjectImpact());
		}

		if (eaForm.getIdentification().getActivitySummary() == null
				|| eaForm.getIdentification().getActivitySummary().trim().length() == 0) {
			activity.setActivitySummary(new String(" "));
		} else {
			activity.setActivitySummary(eaForm.getIdentification().getActivitySummary());
		}

		if (eaForm.getIdentification().getContractingArrangements() == null
				|| eaForm.getIdentification().getContractingArrangements().trim().length() == 0) {
			activity.setContractingArrangements(new String(" "));
		} else {
			activity.setContractingArrangements(eaForm.getIdentification().getContractingArrangements());
		}

		if (eaForm.getIdentification().getCondSeq() == null
				|| eaForm.getIdentification().getCondSeq().trim().length() == 0) {
			activity.setCondSeq(new String(" "));
		} else {
			activity.setCondSeq(eaForm.getIdentification().getCondSeq());
		}

		if (eaForm.getIdentification().getLinkedActivities() == null
				|| eaForm.getIdentification().getLinkedActivities().trim().length() == 0) {
			activity.setLinkedActivities(new String(" "));
		} else {
			activity.setLinkedActivities(eaForm.getIdentification().getLinkedActivities());
		}

		if (eaForm.getIdentification().getConditionality() == null
				|| eaForm.getIdentification().getConditionality().trim().length() == 0) {
			activity.setConditionality(new String(" "));
		} else {
			activity.setConditionality(eaForm.getIdentification().getConditionality());
		}

		if (eaForm.getIdentification().getProjectManagement() == null
				|| eaForm.getIdentification().getProjectManagement().trim().length() == 0) {
			activity.setProjectManagement(new String(" "));
		} else {
			activity.setProjectManagement(eaForm.getIdentification().getProjectManagement());
		}

		if (eaForm.getIdentification().getPurpose() == null
				|| eaForm.getIdentification().getPurpose().trim().length() == 0) {
			activity.setPurpose(new String(" "));
		} else {
			activity.setPurpose(eaForm.getIdentification().getPurpose());
		}
		if (eaForm.getIdentification().getResults() == null
				|| eaForm.getIdentification().getResults().trim().length() == 0) {
			activity.setResults(new String(" "));
		} else {
			activity.setResults(eaForm.getIdentification().getResults());
		}
		if (eaForm.getIdentification().getObjectives() == null
				|| eaForm.getIdentification().getObjectives().trim().length() == 0) {
			activity.setObjective(new String(" "));
		} else {
			activity.setObjective(eaForm.getIdentification().getObjectives());
		}

		if (eaForm.getPlanning().getStatusReason() == null 
				|| eaForm.getPlanning().getStatusReason().trim().length() != 0) {
			activity.setStatusReason(" ");
		} else {
			activity.setStatusReason(eaForm.getPlanning().getStatusReason().trim());
		}

		
		
		if (eaForm.getContractDetails() == null
				|| eaForm.getContractDetails().trim().length() == 0) {
			activity.setContractDetails(new String(" "));
		} else {
			activity.setContractDetails(eaForm.getContractDetails());
		}
		
		
		/*
		 * tanzania adds
		 */
		if (eaForm.getFY() == null
				|| eaForm.getFY().trim().length() == 0)
			activity.setFY(new String(" "));
		else
			activity.setFY(eaForm.getFY());

		if (eaForm.getVote() == null
				|| eaForm.getVote().trim().length() == 0)
			activity.setVote(new String(" "));
		else
			activity.setVote(eaForm.getVote());
		
		if (eaForm.getSubVote() == null
				|| eaForm.getSubVote().trim().length() == 0)
			activity.setSubVote(new String(" "));
		else
			activity.setSubVote(eaForm.getSubVote());
		
		if (eaForm.getSubProgram() == null
				|| eaForm.getSubProgram().trim().length() == 0)
			activity.setSubProgram(new String(" "));
		else
			activity.setSubProgram(eaForm.getSubProgram());
		
		if (eaForm.getProjectCode() == null
				|| eaForm.getProjectCode().trim().length() == 0)
			activity.setProjectCode(new String(" "));
		else
			activity.setProjectCode(eaForm.getProjectCode());
		
		activity.setGovernmentApprovalProcedures(eaForm.getIdentification().getGovernmentApprovalProcedures());

		if (eaForm.getIdentification().getGovAgreementNumber() == null
				|| eaForm.getIdentification().getGovAgreementNumber().trim().length() == 0)
			activity.setGovAgreementNumber(new String(" "));
		else
			activity.setGovAgreementNumber(eaForm.getIdentification().getGovAgreementNumber());
		
		if (eaForm.getIdentification().getCrisNumber() == null
				|| eaForm.getIdentification().getCrisNumber().trim().length() == 0)
			activity.setCrisNumber(new String(" "));
		else
			activity.setCrisNumber(eaForm.getIdentification().getCrisNumber());

		activity.setJointCriteria(eaForm.getIdentification().getJointCriteria());

		activity.setHumanitarianAid(eaForm.getIdentification().getHumanitarianAid());

		if (eaForm.getConditions() == null
				|| eaForm.getConditions().trim().length() == 0) {
			activity.setCondition(" ");
		} else {
			activity.setCondition(eaForm.getConditions());
		}

		try {
			activity.setLineMinRank(Integer.valueOf(eaForm.getPlanning().getLineMinRank()));
			if (activity.getLineMinRank().intValue() < 1 || activity.getLineMinRank().intValue() > 5) {
				logger.debug("Line Ministry Rank is out of permisible range (1 to 5)");
				activity.setLineMinRank(null);
			}
		}
		catch (NumberFormatException nex) {
			logger.debug("Line Ministry Rank is not a number : " + nex);
			activity.setLineMinRank(null);
		}
		try {
			activity.setPlanMinRank(Integer.valueOf(eaForm.getPlanning().getPlanMinRank()));
			if (activity.getPlanMinRank().intValue() < 1 || activity.getPlanMinRank().intValue() > 5) {
				logger.debug("Plan Ministry Rank is out of permisible range (1 to 5)");
				activity.setPlanMinRank(null);
			}
		}
		catch (NumberFormatException nex) {
			logger.debug("Plan Ministry Rank is not a number : " + nex);
			activity.setPlanMinRank(null);
		}

		activity.setProposedApprovalDate(DateConversion.getDate(eaForm
				.getPlanning().getOriginalAppDate()));
		activity.setActualApprovalDate(DateConversion.getDate(eaForm
				.getPlanning().getRevisedAppDate()));
		activity.setProposedStartDate(DateConversion.getDate(eaForm
				.getPlanning().getOriginalStartDate()));
		activity.setActualStartDate(DateConversion.getDate(eaForm
				.getPlanning().getRevisedStartDate()));
		activity.setActualCompletionDate(DateConversion.getDate(eaForm
				.getPlanning().getCurrentCompDate()));
		activity.setOriginalCompDate(DateConversion.getDate(eaForm
				.getPlanning().getProposedCompDate()));
		activity.setContractingDate(DateConversion.getDate(eaForm
				.getPlanning().getContractingDate()));
		activity.setDisbursmentsDate(DateConversion.getDate(eaForm
				.getPlanning().getDisbursementsDate()));
        activity.setProposedCompletionDate(DateConversion.getDate(eaForm
        		.getPlanning().getProposedCompDate()));

        
		AmpActivityClosingDates closeDate = null;

		if (activity.getClosingDates() == null) {
			activity.setClosingDates(new HashSet());
		}

		if (!(eaForm.isEditAct())) {
			closeDate = new AmpActivityClosingDates();
			closeDate.setAmpActivityId(activity);
			closeDate.setClosingDate(DateConversion.getDate(eaForm
					.getPlanning().getProposedCompDate()));
			closeDate.setComments(" ");
			closeDate.setType(Constants.REVISED);
			activity.getClosingDates().add(closeDate);
		}

		if (eaForm.getActivityCloseDates() != null && eaForm.getActivityCloseDates().size()>0) {
			Iterator itr = eaForm.getActivityCloseDates().iterator();
			while (itr.hasNext()) {
				String date = (String) itr.next();
				closeDate = new AmpActivityClosingDates();
				closeDate.setAmpActivityId(activity);
				closeDate.setClosingDate(DateConversion.getDate(date));
				closeDate.setType(Constants.REVISED);
				closeDate.setComments(" ");
				activity.getClosingDates().add(closeDate);
			}
		}

		if (eaForm.getPlanning().getCurrentCompDate() != null
				&& eaForm.getPlanning().getCurrentCompDate().trim().length() > 0) {
			closeDate = new AmpActivityClosingDates();
			closeDate.setAmpActivityId(activity);
			closeDate.setClosingDate(DateConversion.getDate(eaForm
					.getPlanning().getCurrentCompDate()));
			closeDate.setComments(" ");
			closeDate.setType(Constants.CURRENT);

			Collection temp = activity.getClosingDates();
			if (!(temp.contains(closeDate))) {
				activity.getClosingDates().add(closeDate);
			}
		}


		// set activity internal ids
		Set internalIds = new HashSet();
		if (eaForm.getIdentification().getSelectedOrganizations() != null) {
			OrgProjectId orgProjId[] = eaForm
					.getIdentification().getSelectedOrganizations();
			for (int i = 0; i < orgProjId.length; i++) {
				AmpActivityInternalId actInternalId = new AmpActivityInternalId();
				if(orgProjId[i] != null){
					actInternalId.setAmpActivity(activity);
					actInternalId.setOrganisation(DbUtil
							.getOrganisation(orgProjId[i].getOrganisation().getAmpOrgId()));
					actInternalId
							.setInternalId(orgProjId[i].getProjectId());
					internalIds.add(actInternalId);
				}
			}
		}
		//activity.getInternalIds().clear();
		activity.setInternalIds(internalIds);

		/* Saving categories to AmpActivity */
		CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getAccessionInstrument(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getAcChapter(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getPlanning().getStatusId(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getLocation().getLevelId(), activity.getCategories() );
		CategoryManagerUtil.addCategoryToSet(eaForm.getGbsSbs(), activity.getCategories() );
        CategoryManagerUtil.addCategoryToSet(eaForm.getLocation().getImplemLocationLevel(), activity.getCategories() );
        CategoryManagerUtil.addCategoryToSet(eaForm.getActivityLevel(), activity.getCategories());
        CategoryManagerUtil.addCategoryToSet(eaForm.getIdentification().getProjectCategory(), activity.getCategories());
		/* END - Saving categories to AmpActivity */
		
        
		
		
		activity.setComments(" ");

		
        activity.setDraft(eaForm.getDraft());

		
	}
	
	
	private void processStep1_5(boolean check, EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request) throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		List formRefDocs=eaForm.getReferenceDocs();
    	Set<AmpActivityReferenceDoc> resultRefDocs=new HashSet<AmpActivityReferenceDoc>();
    	if(formRefDocs!=null && !formRefDocs.isEmpty())
		for (Iterator refIter = formRefDocs.iterator(); refIter.hasNext();) {
			ReferenceDoc refDoc = (ReferenceDoc) refIter.next();
			if(ArrayUtils.contains(eaForm.getAllReferenceDocNameIds(), refDoc.getCategoryValueId())){
				AmpActivityReferenceDoc dbRefDoc=null;//categoryRefDocMap.get(refDoc.getCategoryValueId());
				if (refDoc.getChecked() == true){
					dbRefDoc=new AmpActivityReferenceDoc();
					dbRefDoc.setCreated(new Date());
					AmpCategoryValue catVal=CategoryManagerUtil.getAmpCategoryValueFromDb(refDoc.getCategoryValueId());
					dbRefDoc.setCategoryValue(catVal);
					dbRefDoc.setActivity(activity);
				//}
				dbRefDoc.setActivity(activity);
				dbRefDoc.setComment(refDoc.getComment());
				dbRefDoc.setLastEdited(new Date());
				resultRefDocs.add(dbRefDoc);

			}else{
				dbRefDoc=null;
				resultRefDocs.add(dbRefDoc);
				}
			}
		}
		activity.setReferenceDocs(resultRefDocs);

	}
	
	
	private void processStep2(boolean check,EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request, HttpSession session) throws Exception, AMPError{
		
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		eaForm.getSectors().setPrimarySectorVisible(FeaturesUtil.isVisibleSectors("Primary", ampContext)?"true":"false");
		eaForm.getSectors().setSecondarySectorVisible(FeaturesUtil.isVisibleSectors("Secondary", ampContext)?"true":"false");
		session.setAttribute("Primary Sector", eaForm.getSectors().getPrimarySectorVisible());
		session.setAttribute("Secondary Sector", eaForm.getSectors().getSecondarySectorVisible());
		
		if (check){
			//Do the checks here
			if(eaForm.getDraft()==null || !eaForm.getDraft().booleanValue()){
				if(isSectorEnabled()){
					if (eaForm.getSectors().getActivitySectors() == null || eaForm.getSectors().getActivitySectors().size() < 1) {
						errors.add("sector", new ActionError("error.aim.addActivity.sectorEmpty"));
					}
					else{
						boolean secPer = false;
						int percent = 0, primaryPrc=0, secondaryPrc=0;
						boolean primary=false;
						boolean hasPrimarySectorsAdded=false, hasSecondarySectorsAdded=false;
						
						Iterator<ActivitySector> secPerItr = eaForm.getSectors().getActivitySectors().iterator();
						while (secPerItr.hasNext()) {
							ActivitySector actSect = (ActivitySector) secPerItr.next();
							AmpClassificationConfiguration config=SectorUtil.getClassificationConfigById(actSect.getConfigId());
							if(config.isPrimary()){
								primary=true;
							}
							if("Primary".equals(config.getName())) 
								hasPrimarySectorsAdded=true;
							if("Secondary".equals(config.getName())) 
								hasSecondarySectorsAdded=true;
							
							if (null == actSect.getSectorPercentage() || "".equals(actSect.getSectorPercentage())) {
								errors.add("sectorPercentageEmpty", new ActionError("error.aim.addActivity.sectorPercentageEmpty"));
							}
							// sector percentage is not a number
							else {
								try {
									if("Primary".equals(config.getName())) primaryPrc+=actSect.getSectorPercentage().intValue();
									if("Secondary".equals(config.getName())) secondaryPrc+=actSect.getSectorPercentage().intValue();
									percent += actSect.getSectorPercentage().intValue();
								} catch (NumberFormatException nex) {
									errors.add("sectorPercentageNonNumeric",
											new ActionError("error.aim.addActivity.sectorPercentageNonNumeric"));
								}
							}
						}
						
						if(primaryPrc!=100 && primaryPrc >0)
							errors.add("primarySectorPercentageSumWrong", new ActionError("error.aim.addActivity.primarySectorPercentageSumWrong"));
						
						if(secondaryPrc!=100 && secondaryPrc >0)
							errors.add("secondarySectorPercentageSumWrong", new ActionError("error.aim.addActivity.secondarySectorPercentageSumWrong"));
						
						
						// no primary sectors added
						if (isPrimarySectorEnabled() && !hasPrimarySectorsAdded)
							errors.add("noPrimarySectorsAdded",
									new ActionError("error.aim.addActivity.noPrimarySectorsAdded"));
						
						if (isSecondarySectorEnabled() && !hasSecondarySectorsAdded)
							errors.add("noSecondarySectorsAdded",
									new ActionError("error.aim.addActivity.noSecondarySectorsAdded"));
					}
				}
				
				if(eaForm.getLocation().getSelectedLocs() != null && eaForm.getLocation().getSelectedLocs().size()>0){
					Iterator<Location> itr = eaForm.getLocation().getSelectedLocs().iterator();
					Double totalPercentage = 0d;
					while (itr.hasNext()) {
						Location loc = itr.next();
						Double percentage=FormatHelper.parseDouble(loc.getPercent());
						if(percentage != null)
							totalPercentage += percentage;
					}
					
					//Checks if it's 100%
					if (totalPercentage != 0 && totalPercentage != 100 && FeaturesUtil.isVisibleField("Regional Percentage", ampContext))
						errors.add("locationPercentageSumWrong",
								new ActionError("error.aim.addActivity.locationPercentageSumWrong"));
				}
				
				if (eaForm.getPrograms().getNationalPlanObjectivePrograms() != null
						&& eaForm.getPrograms().getNationalPlanObjectivePrograms().size() > 0) {
					Iterator<AmpActivityProgram> npoIt = eaForm.getPrograms().getNationalPlanObjectivePrograms().iterator();
					Double totalPercentage = 0d;
					while (npoIt.hasNext()) {
						AmpActivityProgram activityProgram = npoIt.next();
						totalPercentage += activityProgram.getProgramPercentage();
					}
					if (totalPercentage != 100)
						errors.add("nationalPlanProgramsPercentageSumWrong",
								new ActionError("error.aim.addActivity.nationalPlanProgramsPercentageSumWrong"));
				}
				
				if (eaForm.getPrograms().getPrimaryPrograms()!= null
						&& eaForm.getPrograms().getPrimaryPrograms().size() > 0) {
					Iterator<AmpActivityProgram> ppIt = eaForm.getPrograms().getPrimaryPrograms().iterator();
					Double totalPercentage = 0d;
					while (ppIt.hasNext()) {
						AmpActivityProgram activityProgram = ppIt.next();
						totalPercentage += activityProgram.getProgramPercentage();
					}
					if (totalPercentage != 100)
						errors.add("primaryProgramsPercentageSumWrong",
								new ActionError("error.aim.addActivity.primaryProgramsPercentageSumWrong"));
				}
				
				if (eaForm.getPrograms().getSecondaryPrograms()!= null
						&& eaForm.getPrograms().getSecondaryPrograms().size() > 0) {
					Iterator<AmpActivityProgram> spIt = eaForm.getPrograms().getSecondaryPrograms().iterator();
					Double totalPercentage = 0d;
					while (spIt.hasNext()) {
						AmpActivityProgram activityProgram = spIt.next();
						totalPercentage += activityProgram.getProgramPercentage();
					}
					if (totalPercentage != 100)
						errors.add("secondaryProgramsPercentageSumWrong",
								new ActionError("error.aim.addActivity.secondaryProgramsPercentageSumWrong"));
				}
			}
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}

		//Do the initializations and all the information transfer between beans here
		if (eaForm.getCrossIssues().getEqualOpportunity() == null
				|| eaForm.getCrossIssues().getEqualOpportunity().trim().length() == 0) {
			activity.setEqualOpportunity(new String(" "));
		} else {
			activity.setEqualOpportunity(eaForm.getCrossIssues().getEqualOpportunity());
		}

		if (eaForm.getCrossIssues().getEnvironment() == null
				|| eaForm.getCrossIssues().getEnvironment().trim().length() == 0) {
			activity.setEnvironment(new String(" "));
		} else {
			activity.setEnvironment(eaForm.getCrossIssues().getEnvironment());
		}

		if (eaForm.getCrossIssues().getMinorities() == null
				|| eaForm.getCrossIssues().getMinorities().trim().length() == 0) {
			activity.setMinorities(new String(" "));
		} else {
			activity.setMinorities(eaForm.getCrossIssues().getMinorities());
		}

		// set the sectors
		if (eaForm.getSectors().getActivitySectors() != null && eaForm.getSectors().getActivitySectors().size()>0) {
			Set sectors = new HashSet();
			if (eaForm.getSectors().getActivitySectors() != null) {
				Iterator itr = eaForm.getSectors().getActivitySectors().iterator();
				while (itr.hasNext()) {
					ActivitySector actSect = (ActivitySector) itr.next();
					Long sectorId = null;
					if (actSect.getSubsectorLevel2Id() != null
							&& (!actSect.getSubsectorLevel2Id().equals(new Long(-1)))) {
						sectorId = actSect.getSubsectorLevel2Id();
					} else if (actSect.getSubsectorLevel1Id() != null
							&& (!actSect.getSubsectorLevel1Id().equals(new Long(-1)))) {
						sectorId = actSect.getSubsectorLevel1Id();
					} else {
						sectorId = actSect.getSectorId();
					}
					AmpActivitySector amps = new AmpActivitySector();
					amps.setActivityId(activity);
					if (sectorId != null && (!sectorId.equals(new Long(-1))))
						amps.setSectorId(SectorUtil.getAmpSector(sectorId));
					amps.setSectorPercentage(actSect.getSectorPercentage());
                                                amps.setClassificationConfig(SectorUtil.getClassificationConfigById(actSect.getConfigId()));
					sectors.add(amps);
				}
			}
			activity.setSectors(sectors);
		}

		if (eaForm.getComponents().getActivityComponentes() != null) {
			Set componentes = new HashSet();
			if (eaForm.getComponents().getActivityComponentes() != null && eaForm.getComponents().getActivityComponentes().size()>0) {
				Iterator itr = eaForm.getComponents().getActivityComponentes().iterator();
				while (itr.hasNext()) {
					ActivitySector actSect = (ActivitySector) itr.next();
					Long sectorId = null;
					if (actSect.getSubsectorLevel2Id() != null
							&& (!actSect.getSubsectorLevel2Id().equals(new Long(-1)))) {
						sectorId = actSect.getSubsectorLevel2Id();
					} else if (actSect.getSubsectorLevel1Id() != null
							&& (!actSect.getSubsectorLevel1Id().equals(new Long(-1)))) {
						sectorId = actSect.getSubsectorLevel1Id();
					} else {
						sectorId = actSect.getSectorId();
					}
					AmpActivityComponente ampc = new AmpActivityComponente();
					ampc.setActivity(activity);
					if (sectorId != null && (!sectorId.equals(new Long(-1))))
						ampc.setSector(SectorUtil.getAmpSector(sectorId));
					ampc.setPercentage(new Float(actSect.getSectorPercentage()));
					componentes.add(ampc);
				}
			}
			activity.setComponentes(componentes);
		}

		if (eaForm.getProgram() != null
				&& (!eaForm.getProgram().equals(new Long(-1)))) {
			AmpTheme theme = ProgramUtil.getThemeObject(eaForm.getProgram());
			if (theme != null) {
				activity.setThemeId(theme);
			}
		}
		if (eaForm.getProgramDescription() != null
				&& eaForm.getProgramDescription().trim().length() != 0) {
			activity.setProgramDescription(eaForm
					.getProgramDescription());
		} else {
			activity.setProgramDescription(" ");
		}

		// set locations
		if (eaForm.getLocation().getSelectedLocs() != null && eaForm.getLocation().getSelectedLocs().size()>0) {
			Set<AmpActivityLocation> locations = new HashSet<AmpActivityLocation>();
			Iterator<Location> itr = eaForm.getLocation().getSelectedLocs().iterator();
			while (itr.hasNext()) {
				Location loc = itr.next();
                                        String countryIso=loc.getNewCountryId();
				AmpLocation ampLoc = LocationUtil.getAmpLocation(countryIso, loc.getRegionId(), loc.getZoneId(), loc.getWoredaId());

				if (ampLoc == null) {
					ampLoc = new AmpLocation();
					ampLoc.setCountry(loc.getCountry());
                                                if(countryIso!=null){
					ampLoc.setDgCountry(DbUtil.getDgCountry(loc.getNewCountryId()));
                                                }
					ampLoc.setRegion(loc.getRegion());
					ampLoc.setAmpRegion(LocationUtil.getAmpRegion(loc.getRegionId()));
					ampLoc.setAmpZone(LocationUtil.getAmpZone(loc.getZoneId()));
					ampLoc.setAmpWoreda(LocationUtil.getAmpWoreda(loc.getWoredaId()));
					ampLoc.setDescription(new String(" "));
					DbUtil.add(ampLoc);
				}

				//AMP-2250
				AmpActivityLocation actLoc=new AmpActivityLocation();
				actLoc.setActivity(activity);//activity);
				actLoc.getActivity().setAmpActivityId(eaForm.getActivityId());
				actLoc.setLocation(ampLoc);
				Double percent=FormatHelper.parseDouble(loc.getPercent());
                                        if(percent==null){
				percent=new Double(0);
                                        }
                                        actLoc.setLocationPercentage(percent.floatValue());
				locations.add(actLoc);
				//locations.add(ampLoc);
				//AMP-2250
			}
			activity.setLocations(locations);
		}
		if(eaForm.getLocation().getSelectedLocs() != null && eaForm.getLocation().getSelectedLocs().size() == 1){
			Iterator<Location> itr = eaForm.getLocation().getSelectedLocs().iterator();
			while (itr.hasNext()) {
				Location loc = itr.next();
				if(loc.getRegion().equals("") && eaForm.getRegionalFundings()!=null){
					eaForm.getRegionalFundings().clear();
		 	      }
			 }
		}else if (eaForm.getLocation().getSelectedLocs()==null || eaForm.getLocation().getSelectedLocs().size() == 0){
			if (eaForm.getRegionalFundings()!=null) eaForm.getRegionalFundings().clear();
		}


		Set programs = new HashSet();
		List activityNPO = eaForm.getPrograms().getNationalPlanObjectivePrograms();
		List activityPP = eaForm.getPrograms().getPrimaryPrograms();
		List activitySP = eaForm.getPrograms().getSecondaryPrograms();
		if (activityNPO != null) {
		        programs.addAll(activityNPO);
		}
		if (activityPP != null) {
		        programs.addAll(activityPP);
		}
		if (activitySP != null) {
		        programs.addAll(activitySP);
		}
		activity.setActPrograms(programs);

		
		
		
		
	}

	private void processStep3(boolean check, EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request) throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
        if(eaForm.getFunding().getProProjCost()==null){
            activity.setFunAmount(null);
            activity.setFunDate(null);
            activity.setCurrencyCode(null);
        }else{
            activity.setFunAmount(eaForm.getFunding().getProProjCost().getFunAmountAsDouble());
            //check for null on bolivia
            if(eaForm.getFunding().getProProjCost().getFunDate()!=null){
        	activity.setFunDate(FormatHelper.parseDate(eaForm.getFunding().getProProjCost().getFunDate()).getTime());
            }
            activity.setCurrencyCode(eaForm.getFunding().getProProjCost().getCurrencyCode());
        }
        
		// set organizations role
		Set orgRole = new HashSet();
		if (eaForm.getFunding().getFundingOrganizations() != null && eaForm.getFunding().getFundingOrganizations().size()>0) { // funding

														// organizations
			AmpRole role = DbUtil.getAmpRole(Constants.FUNDING_AGENCY);
			Iterator itr = eaForm.getFunding().getFundingOrganizations().iterator();
			while (itr.hasNext()) {
				FundingOrganization fOrg = (FundingOrganization) itr
						.next();
				AmpOrganisation ampOrg = DbUtil.getOrganisation(fOrg
						.getAmpOrgId());
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(ampOrg);
				orgRole.add(ampOrgRole);
			}
		}

        
		//set funding and funding details
		Set fundings = new HashSet();
		if (eaForm.getFunding().getFundingOrganizations() != null && eaForm.getFunding().getFundingOrganizations().size()>0) {
			Iterator itr1 = eaForm.getFunding().getFundingOrganizations().iterator();
			while (itr1.hasNext()) {
				FundingOrganization fOrg = (FundingOrganization) itr1.next();
				//add fundings
				if (fOrg.getFundings() != null && fOrg.getFundings().size()>0) {
					Iterator itr2 = fOrg.getFundings().iterator();
					while (itr2.hasNext()) {
						Funding fund = (Funding) itr2.next();
						AmpFunding ampFunding = new AmpFunding();
						ampFunding.setActive(fOrg.getFundingActive());
						if("unchecked".equals(fOrg.getDelegatedCooperationString()) || fOrg.getDelegatedCooperation()==null)
							ampFunding.setDelegatedCooperation(false);
						else
							ampFunding.setDelegatedCooperation(true);
						if("unchecked".equals(fOrg.getDelegatedPartnerString()) || fOrg.getDelegatedPartner()==null)
							ampFunding.setDelegatedPartner(false);
						else
							ampFunding.setDelegatedPartner(true);

						ampFunding.setAmpDonorOrgId(DbUtil.getOrganisation(fOrg.getAmpOrgId()));
						ampFunding.setFinancingId(fund.getOrgFundingId());
						/*
						 * if (fund.getSignatureDate() != null)
						 * ampFunding.setSignatureDate(DateConversion
						 * .getDate(fund.getSignatureDate()));
						 */
						//ampFunding.setModalityId(fund.getModality());
						ampFunding.setFinancingInstrument(fund.getFinancingInstrument());
						if (fund.getConditions() != null
								&& fund.getConditions().trim().length() != 0) {
							ampFunding.setConditions(fund.getConditions());
						} else {
							ampFunding.setConditions(new String(" "));
						}
						ampFunding.setComments(new String(" "));
						/*ampFunding.setAmpTermsAssistId(fund.getAmpTermsAssist());*/
						ampFunding.setTypeOfAssistance( fund.getTypeOfAssistance() );
						ampFunding.setAmpActivityId(activity);

						// add funding details for each funding
						Set fundDeatils = new HashSet();
						if (fund.getFundingDetails() != null) {
							Iterator itr3 = fund.getFundingDetails().iterator();
							while (itr3.hasNext()) {
								FundingDetail fundDet = (FundingDetail) itr3.next();
								AmpFundingDetail ampFundDet = new AmpFundingDetail();
								ampFundDet.setTransactionType(new Integer(fundDet.getTransactionType()));
								// ampFundDet.setPerspectiveId(DbUtil.getPerspective(Constants.MOFED));
								ampFundDet.setAdjustmentType(new Integer(fundDet.getAdjustmentType()));
								ampFundDet.setTransactionDate(DateConversion.getDate(fundDet
														.getTransactionDate()));
								boolean useFixedRate = false;
								if (fundDet.getTransactionType() == Constants.COMMITMENT) {
									if (fundDet.isUseFixedRate()
											&& fundDet.getFixedExchangeRate().doubleValue() > 0
											&& fundDet.getFixedExchangeRate().doubleValue() != 1) {
										useFixedRate = true;
									}
								}

								if (!useFixedRate) {
									Double transAmt = new Double(
											FormatHelper.parseDouble(fundDet.getTransactionAmount()));
									ampFundDet.setTransactionAmount(transAmt);
									ampFundDet.setAmpCurrencyId(CurrencyUtil.getCurrencyByCode(fundDet.getCurrencyCode()));
									ampFundDet.setFixedExchangeRate(null);
								} else {
									// Use the fixed exchange rate
									double transAmt = FormatHelper.parseDouble(fundDet.getTransactionAmount());
									Date trDate = DateConversion.getDate(fundDet.getTransactionDate());
									// double frmExRt =
									// CurrencyUtil.getExchangeRate(fundDet.getCurrencyCode(),1,trDate);
									// double amt =
									// CurrencyWorker.convert1(transAmt,
									// frmExRt,1);
									// amt *=
									// fundDet.getFixedExchangeRate();
									ampFundDet.setTransactionAmount(new Double(transAmt));
									ampFundDet.setFixedExchangeRate(fundDet.getFixedExchangeRate());
									ampFundDet.setAmpCurrencyId(CurrencyUtil.getCurrencyByCode(fundDet
															.getCurrencyCode()));
								}
								ampFundDet.setAmpFundingId(ampFunding);
								if (fundDet.getTransactionType() == Constants.EXPENDITURE) {
									ampFundDet.setExpCategory(fundDet.getClassification());
								}
								ampFundDet.setDisbOrderId(fundDet.getDisbOrderId());
								ampFundDet.setContract(fundDet.getContract());
								fundDeatils.add(ampFundDet);
							}
						}
						ampFunding.setFundingDetails(fundDeatils);
						this.saveMTEFProjections(fund, ampFunding);
						fundings.add(ampFunding);
					}
				}
				//AMP-2947 Funding amount should not be mandatory when adding a Funding Agency
				else{
					Funding fund = new Funding();
					AmpFunding ampFunding = new AmpFunding();
					ampFunding.setActive(fOrg.getFundingActive());
					if("unchecked".equals(fOrg.getDelegatedCooperationString()) || fOrg.getDelegatedCooperation()==null)
						ampFunding.setDelegatedCooperation(false);
					else
						ampFunding.setDelegatedCooperation(true);
					if("unchecked".equals(fOrg.getDelegatedPartnerString()) || fOrg.getDelegatedPartner()==null)
						ampFunding.setDelegatedPartner(false);
					else
						ampFunding.setDelegatedPartner(true);

					ampFunding.setAmpDonorOrgId(DbUtil.getOrganisation(fOrg.getAmpOrgId()));
					ampFunding.setFinancingId(fund.getOrgFundingId());

					ampFunding.setFinancingInstrument(fund.getFinancingInstrument());
					if (fund.getConditions() != null
							&& fund.getConditions().trim().length() != 0) {
						ampFunding.setConditions(fund.getConditions());
					} else {
						ampFunding.setConditions(new String(" "));
					}
					ampFunding.setComments(new String(" "));
					ampFunding.setTypeOfAssistance( fund.getTypeOfAssistance() );
					ampFunding.setAmpActivityId(activity);
					this.saveMTEFProjections(fund, ampFunding);
					fundings.add(ampFunding);
				}
			}
		}
		activity.setFunding(fundings);

		if(eaForm.getFunding().getProProjCost()==null){
            activity.setFunAmount(null);
            activity.setFunDate(null);
            activity.setCurrencyCode(null);
        }else{
            activity.setFunAmount(eaForm.getFunding().getProProjCost().getFunAmountAsDouble());
            //check null for bolivia
            if (eaForm.getFunding().getProProjCost().getFunDate()!=null){
                activity.setFunDate(FormatHelper.parseDate(eaForm.getFunding().getProProjCost().getFunDate()).getTime());
            }
            activity.setCurrencyCode(eaForm.getFunding().getProProjCost().getCurrencyCode());
        }
		
		
		
		
	}
	
	
	private void processStep4(boolean check, EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request) throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		// set Regional fundings
		Set regFundings = new HashSet();
		if (eaForm.getRegionalFundings() != null
				&& eaForm.getRegionalFundings().size() > 0) {
			Iterator itr1 = eaForm.getRegionalFundings().iterator();
			while (itr1.hasNext()) {
				RegionalFunding regFund = (RegionalFunding) itr1.next();
				if (regFund.getCommitments() != null
						&& regFund.getCommitments().size() > 0) {
					Iterator itr2 = regFund.getCommitments().iterator();
					while (itr2.hasNext()) {
						AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
						ampRegFund.setActivity(activity);
						ampRegFund.setTransactionType(new Integer(
								Constants.COMMITMENT));
						FundingDetail fd = (FundingDetail) itr2.next();
						Iterator tmpItr=null;
						if(eaForm.getCurrencies()!=null){
							tmpItr = eaForm.getCurrencies()
							.iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr
								.next();
								if (curr.getCurrencyCode().equals(
										fd.getCurrencyCode())) {
									ampRegFund.setCurrency(curr);
									break;
								}
							}
						}

						boolean regionFlag=false;

						if(eaForm.getFundingRegions()!=null && eaForm.getFundingRegions().size()>0){
							tmpItr = eaForm.getFundingRegions().iterator();
							while (tmpItr.hasNext()) {
								AmpRegion reg = (AmpRegion) tmpItr.next();
								if (reg.getAmpRegionId().equals(
										regFund.getRegionId())) {
									ampRegFund.setRegion(reg);
									regionFlag=true;
									break;
								}
							}
						}
						ampRegFund.setTransactionAmount(new Double(
								FormatHelper.parseDouble(fd
										.getTransactionAmount())));
						ampRegFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampRegFund.setAdjustmentType(new Integer(fd
								.getAdjustmentType()));
						if(regionFlag){
							regFundings.add(ampRegFund);
						}
					}
				}

				if (regFund.getDisbursements() != null
						&& regFund.getDisbursements().size() > 0) {
					Iterator itr2 = regFund.getDisbursements()
					.iterator();
					while (itr2.hasNext()) {
						AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
						ampRegFund.setActivity(activity);
						ampRegFund.setTransactionType(new Integer(
								Constants.DISBURSEMENT));
						FundingDetail fd = (FundingDetail) itr2.next();
						Iterator tmpItr=null;
						if(eaForm.getCurrencies()!=null){
							tmpItr = eaForm.getCurrencies()
							.iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr
								.next();
								if (curr.getCurrencyCode().equals(
										fd.getCurrencyCode())) {
									ampRegFund.setCurrency(curr);
									break;
								}
							}
						}
						boolean regionFlag=false;
						if(eaForm.getFundingRegions()!=null && eaForm.getFundingRegions().size()>0){
							tmpItr = eaForm.getFundingRegions().iterator();
							while (tmpItr.hasNext()) {
								AmpRegion reg = (AmpRegion) tmpItr.next();
								if (reg.getAmpRegionId().equals(
										regFund.getRegionId())) {
									ampRegFund.setRegion(reg);
									regionFlag=true;
									break;
								}
							}
						}

						ampRegFund.setTransactionAmount(new Double(
								FormatHelper.parseDouble(fd
										.getTransactionAmount())));
						ampRegFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampRegFund.setAdjustmentType(new Integer(fd
								.getAdjustmentType()));
						if(regionFlag){
							regFundings.add(ampRegFund);
						}
					}
				}

				if (regFund.getExpenditures() != null
						&& regFund.getExpenditures().size() > 0) {

					Iterator itr2 = regFund.getExpenditures()
					.iterator();
					while (itr2.hasNext()) {
						AmpRegionalFunding ampRegFund = new AmpRegionalFunding();
						ampRegFund.setActivity(activity);
						ampRegFund.setTransactionType(new Integer(
								Constants.EXPENDITURE));
						FundingDetail fd = (FundingDetail) itr2.next();
						Iterator tmpItr=null;
						if(eaForm.getCurrencies()!=null){
							tmpItr = eaForm.getCurrencies()
							.iterator();
							while (tmpItr.hasNext()) {
								AmpCurrency curr = (AmpCurrency) tmpItr
								.next();
								if (curr.getCurrencyCode().equals(
										fd.getCurrencyCode())) {
									ampRegFund.setCurrency(curr);
									break;
								}
							}
						}
						boolean regionFlag=false;
						if( eaForm.getFundingRegions()!=null && eaForm.getFundingRegions().size()>0){
							tmpItr = eaForm.getFundingRegions().iterator();
							while (tmpItr.hasNext()) {
								AmpRegion reg = (AmpRegion) tmpItr.next();
								if (reg.getAmpRegionId().equals(
										regFund.getRegionId())) {
									ampRegFund.setRegion(reg);
									regionFlag=true;
									break;
								}
							}
						}

						ampRegFund.setTransactionAmount(new Double(
								FormatHelper.parseDouble(fd
										.getTransactionAmount())));
						ampRegFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampRegFund.setAdjustmentType(new Integer(fd
								.getAdjustmentType()));
						if(regionFlag){
							regFundings.add(ampRegFund);
						}
					}

				}
			}
		}
		activity.setRegionalFundings(regFundings);


	
	}
	

	private void processStep5(boolean check, EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request, Set<Components<AmpComponentFunding>> tempComp) throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here

		//set components
		proccessComponents(tempComp, eaForm, activity);

		if (eaForm.getIssues() != null && eaForm.getIssues().size() > 0) {
			Set issueSet = new HashSet();
			for (int i = 0; i < eaForm.getIssues().size(); i++) {
				Issues issue = (Issues) eaForm.getIssues().get(i);
				AmpIssues ampIssue = new AmpIssues();
				ampIssue.setActivity(activity);
				ampIssue.setName(issue.getName());
				if (issue.getIssueDate()!=null){
					ampIssue.setIssueDate(FormatHelper.parseDate(issue.getIssueDate()).getTime());
				}
				Set measureSet = new HashSet();
				if (issue.getMeasures() != null
						&& issue.getMeasures().size() > 0) {
					for (int j = 0; j < issue.getMeasures().size(); j++) {
						Measures measure = (Measures) issue
								.getMeasures().get(j);
						AmpMeasure ampMeasure = new AmpMeasure();
						ampMeasure.setIssue(ampIssue);
						ampMeasure.setName(measure.getName());
						Set actorSet = new HashSet();
						if (measure.getActors() != null
								&& measure.getActors().size() > 0) {
							for (int k = 0; k < measure.getActors()
									.size(); k++) {
								AmpActor actor = (AmpActor) measure
										.getActors().get(k);
								actor.setAmpActorId(null);
								actor.setMeasure(ampMeasure);
								actorSet.add(actor);
							}
						}
						ampMeasure.setActors(actorSet);
						measureSet.add(ampMeasure);
					}
				}
				ampIssue.setMeasures(measureSet);
				issueSet.add(ampIssue);
			}
			activity.setIssues(issueSet);
		}

	}

    

	private void processStep6(boolean check, EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request, Collection relatedLinks) throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		if(eaForm.getDocumentSpace() == null
	            || eaForm.getDocumentSpace().trim().length() == 0) {
			activity.setDocumentSpace(new String(" "));
		} else {
			activity.setDocumentSpace(eaForm.getDocumentSpace());
		}
		
		//relatedLinks = new ArrayList();
		relatedLinks.clear();
		if (eaForm.getDocumentList() != null && eaForm.getDocumentList() .size()>0) {
			Iterator itr = eaForm.getDocumentList().iterator();
			while (itr.hasNext()) {
				RelatedLinks rl = (RelatedLinks) itr.next();
				relatedLinks.add(rl);
			}
		}
		if (eaForm.getLinksList() != null && eaForm.getLinksList().size()>0) {
			Iterator itr = eaForm.getLinksList().iterator();
			while (itr.hasNext()) {
				RelatedLinks rl = (RelatedLinks) itr.next();
				relatedLinks.add(rl);
			}
		}
		
		/* Saving related documents into AmpActivity */
        HashSet<String>UUIDs				= new HashSet<String>();
        Collection<DocumentData> tempDocs	= TemporaryDocumentData.retrieveTemporaryDocDataList(request);
        Iterator<DocumentData> docIter			= tempDocs.iterator();
        while ( docIter.hasNext() ) {
        	TemporaryDocumentData tempDoc	= (TemporaryDocumentData) docIter.next();
        	NodeWrapper nodeWrapper			= tempDoc.saveToRepository(request, errors);
        	if ( nodeWrapper != null )
        			UUIDs.add( nodeWrapper.getUuid() );
        }
        if(SelectDocumentDM.getSelectedDocsSet(request, ActivityDocumentsConstants.RELATED_DOCUMENTS, false)!=null) {
        	UUIDs.addAll(SelectDocumentDM.getSelectedDocsSet(request, ActivityDocumentsConstants.RELATED_DOCUMENTS, false));
        }
		if (UUIDs != null && UUIDs.size() >0 ) {
			if ( activity.getActivityDocuments() == null )
					activity.setActivityDocuments(new HashSet<AmpActivityDocument>() );
			else
					activity.getActivityDocuments().clear();
			Iterator<String> iter	= UUIDs.iterator();

			while ( iter.hasNext() ) {
				String uuid					= iter.next();
				AmpActivityDocument doc		= new AmpActivityDocument();
				doc.setUuid(uuid);
				doc.setDocumentType( ActivityDocumentsConstants.RELATED_DOCUMENTS );
				activity.getActivityDocuments().add(doc);
			}
		}
		SelectDocumentDM.clearContentRepositoryHashMap(request);
		/* END -Saving related documents into AmpActivity */


	}

    
	private void processStep7(boolean check, EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request) throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
//		if (eaForm.getContractors() == null
//				|| eaForm.getContractors().trim().length() == 0) {
//			activity.setContractors(" ");
//		} else {
//			activity.setContractors(eaForm.getContractors());
//		}
		Set orgRole = new HashSet();
		
		if (eaForm.getAgencies().getExecutingAgencies() != null && eaForm.getAgencies().getExecutingAgencies().size()>0) { // executing
			// agencies
			AmpRole role = DbUtil.getAmpRole(Constants.EXECUTING_AGENCY);
			Iterator itr = eaForm.getAgencies().getExecutingAgencies().iterator();
			while (itr.hasNext()) {
				AmpOrganisation tmp= (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole=new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(tmp);
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getImpAgencies() != null && eaForm.getAgencies().getImpAgencies().size()>0) { // implementing agencies
			AmpRole role = DbUtil.getAmpRole(Constants.IMPLEMENTING_AGENCY);
			Iterator itr = eaForm.getAgencies().getImpAgencies().iterator();
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getBenAgencies() != null && eaForm.getAgencies().getBenAgencies().size()>0) { // beneficiary agencies
			AmpRole role = DbUtil.getAmpRole(Constants.BENEFICIARY_AGENCY);
			Iterator itr = eaForm.getAgencies().getBenAgencies().iterator();
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getConAgencies() != null && eaForm.getAgencies().getConAgencies().size()>0) { // contracting agencies
			AmpRole role = DbUtil.getAmpRole(Constants.CONTRACTING_AGENCY);
			Iterator itr = eaForm.getAgencies().getConAgencies().iterator();
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getRegGroups() != null && eaForm.getAgencies().getRegGroups().size()>0) { // regional groups
			AmpRole role = DbUtil.getAmpRole(Constants.REGIONAL_GROUP);
			Iterator itr = eaForm.getAgencies().getRegGroups().iterator();
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getSectGroups() != null && eaForm.getAgencies().getSectGroups().size()>0) { // sector groups
			AmpRole role = DbUtil.getAmpRole(Constants.SECTOR_GROUP);
			Iterator itr = eaForm.getAgencies().getSectGroups().iterator();
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getReportingOrgs() != null && eaForm.getAgencies().getReportingOrgs().size()>0) { // Reporting
			// Organization
			AmpRole role = DbUtil.getAmpRole(Constants.REPORTING_AGENCY);
			Iterator itr = eaForm.getAgencies().getReportingOrgs().iterator();
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
		}
		if (eaForm.getAgencies().getRespOrganisations() != null && eaForm.getAgencies().getRespOrganisations().size()>0) { // Responsible Organisation
			AmpRole role = DbUtil.getAmpRole(Constants.RESPONSIBLE_ORGANISATION);
			Iterator itr = eaForm.getAgencies().getRespOrganisations().iterator();
			while (itr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) itr.next();
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(activity);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
		}
		activity.setOrgrole(orgRole);
		
		
	}

	private void processStep8(boolean check, EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request) throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here

		activity.setContFirstName(eaForm.getContactInfo().getDnrCntFirstName());
		activity.setContLastName(eaForm.getContactInfo().getDnrCntLastName());
		activity.setEmail(eaForm.getContactInfo().getDnrCntEmail());
		activity.setDnrCntTitle(eaForm.getContactInfo().getDnrCntTitle());
		activity.setDnrCntOrganization(eaForm.getContactInfo().getDnrCntOrganization());
		activity.setDnrCntFaxNumber(eaForm.getContactInfo().getDnrCntFaxNumber());
		activity.setDnrCntPhoneNumber(eaForm.getContactInfo().getDnrCntPhoneNumber());


		activity.setMofedCntFirstName(eaForm.getContactInfo().getMfdCntFirstName());
		activity.setMofedCntLastName(eaForm.getContactInfo().getMfdCntLastName());
		activity.setMofedCntEmail(eaForm.getContactInfo().getMfdCntEmail());
		activity.setMfdCntTitle(eaForm.getContactInfo().getMfdCntTitle());
		activity.setMfdCntOrganization(eaForm.getContactInfo().getMfdCntOrganization());
		activity.setMfdCntFaxNumber(eaForm.getContactInfo().getMfdCntFaxNumber());
		activity.setMfdCntPhoneNumber(eaForm.getContactInfo().getMfdCntPhoneNumber());

		activity.setPrjCoFirstName(eaForm.getContactInfo().getPrjCoFirstName());
		activity.setPrjCoLastName(eaForm.getContactInfo().getPrjCoLastName());
		activity.setPrjCoEmail(eaForm.getContactInfo().getPrjCoEmail());
		activity.setPrjCoTitle(eaForm.getContactInfo().getPrjCoTitle());
		activity.setPrjCoOrganization(eaForm.getContactInfo().getPrjCoOrganization());
		activity.setPrjCoPhoneNumber(eaForm.getContactInfo().getPrjCoPhoneNumber());
		activity.setPrjCoFaxNumber(eaForm.getContactInfo().getPrjCoFaxNumber());

		activity.setSecMiCntFirstName(eaForm.getContactInfo().getSecMiCntFirstName());
		activity.setSecMiCntLastName(eaForm.getContactInfo().getSecMiCntLastName());
		activity.setSecMiCntEmail(eaForm.getContactInfo().getSecMiCntEmail());
		activity.setSecMiCntTitle(eaForm.getContactInfo().getSecMiCntTitle());
		activity.setSecMiCntOrganization(eaForm.getContactInfo().getSecMiCntOrganization());
		activity.setSecMiCntPhoneNumber(eaForm.getContactInfo().getSecMiCntPhoneNumber());
		activity.setSecMiCntFaxNumber(eaForm.getContactInfo().getSecMiCntFaxNumber());

	}

	
	private void processStep9(boolean check, EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request) throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
        if(eaForm.getSurvey().getAhsurvey()!=null) 
        	DbUtil.updateSurvey(eaForm.getSurvey().getAhsurvey());
        if(eaForm.getSurvey().getAmpSurveyId()!=null) 
        	DbUtil.saveSurveyResponses(eaForm.getSurvey().getAmpSurveyId(), eaForm.getSurvey().getIndicators());

	}

	private void processStep11(boolean check, EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request) throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		if (check){
			//Do the checks here
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		if(eaForm.getCosts()!=null && eaForm.getCosts().size()>0) {
			Set costs=new HashSet();
			Iterator i=eaForm.getCosts().iterator();
			while (i.hasNext()) {
				EUActivity element = (EUActivity) i.next();
				element.setActivity(activity);
				element.setId(null);
				if(element.getContributions()!=null){
					Iterator ii=element.getContributions().iterator();
					while (ii.hasNext()) {
						EUActivityContribution element2 = (EUActivityContribution) ii.next();
						element2.setId(null);
					}
				}
				costs.add(element);
			}
			activity.setCosts(costs);
		}

	}
	
	
	
	private void processPostStep(EditActivityForm eaForm, AmpActivity activity, TeamMember tm){
		
		if (eaForm.isEditAct()) {
		
			//AmpActivity act = ActivityUtil.getActivityByName(eaForm.getTitle());
			// Setting approval status of activity
			activity.setApprovalStatus(eaForm.getApprovalStatus());
			activity.setApprovalDate(eaForm.getApprovalDate());
			activity.setApprovedBy(eaForm.getApprovedBy());

			//AMP-3464
			//if an approved activity is edited and the appsettings is set to newOnly then the activity
			//doesn't need to be approved again!
			AmpActivity aAct = ActivityUtil.getAmpActivity(eaForm.getActivityId());

			activity.setApprovalStatus(aAct.getApprovalStatus());
			if(tm.getTeamId().equals(aAct.getTeam().getAmpTeamId())){
				if(eaForm.getDraft()==true){
					if(tm.getTeamHead()){
						activity.setApprovalStatus(Constants.APPROVED_STATUS);
					}else{
						activity.setApprovalStatus(aAct.getApprovalStatus());
					}
				}
				if("newOnly".equals(DbUtil.getTeamAppSettingsMemberNotNull(aAct.getTeam().getAmpTeamId()).getValidation()))
				{
					if(Constants.APPROVED_STATUS.equals(activity.getApprovalStatus()) || Constants.EDITED_STATUS.equals(activity.getApprovalStatus()) )
						activity.setApprovalStatus(Constants.APPROVED_STATUS);
				}

				if("allEdits".equals(DbUtil.getTeamAppSettingsMemberNotNull(aAct.getTeam().getAmpTeamId()).getValidation())){
					if(!tm.getTeamHead()){
						if(Constants.APPROVED_STATUS.equals(aAct.getApprovalStatus())) activity.setApprovalStatus(Constants.EDITED_STATUS);
						else activity.setApprovalStatus(aAct.getApprovalStatus());
					}
				}
				if(tm.getTeamHead()) activity.setApprovalStatus(Constants.APPROVED_STATUS);
			}
			else{
				if("newOnly".equals(DbUtil.getTeamAppSettingsMemberNotNull(aAct.getTeam().getAmpTeamId()).getValidation()))
				{
					if(Constants.APPROVED_STATUS.equals(activity.getApprovalStatus()) || Constants.EDITED_STATUS.equals(activity.getApprovalStatus()) )
						activity.setApprovalStatus(Constants.APPROVED_STATUS);
				}

				if("allEdits".equals(DbUtil.getTeamAppSettingsMemberNotNull(aAct.getTeam().getAmpTeamId()).getValidation())){
					if(Constants.APPROVED_STATUS.equals(aAct.getApprovalStatus())) activity.setApprovalStatus(Constants.EDITED_STATUS);
					else activity.setApprovalStatus(aAct.getApprovalStatus());
				}
			}
			activity.setActivityCreator(eaForm.getCreatedBy());
		}
		else{
			AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(tm
					.getMemberId());
			activity.setActivityCreator(teamMember);
			Calendar cal = Calendar.getInstance();
			activity.setCreatedDate(cal.getTime());
			// Setting approval status of activity
			activity.setApprovalStatus(eaForm.getApprovalStatus());

		}

	}
	
	
	
	/**
	 * This is the structure that a processStep should have .. it's only to be used as a starting point in treating a new step
	 * parameters:
	 *   - check - set to true when data validation should be made, if data is invalid user is redirected to that step to correct it
	 *
	 * 
	 * DO NOT MODIFY
	 * 
	 * @author Arty
	 */
	private void processStepBulk(boolean check, EditActivityForm eaForm, AmpActivity activity, ActionErrors errors, HttpServletRequest request) throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		if (check){
			//Do the checks here			
			
			end:
			if (errors.size() > 0){
				//we have all the errors for this step saved and we must throw the amp error
				saveErrors(request, errors);
				throw err;
			}
		}
		
		//Do the initializations and all the information transfer between beans here
		
	}

	private void symErr() throws Exception, AMPError{
		AMPError err;
		err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_WARNING, false);
		
		throw err;
	}
	
	private void processStepX(int stepNumber, boolean check, EditActivityForm eaForm, AmpActivity activity, 
			ActionErrors errors, HttpServletRequest request, HttpSession session, Set<Components<AmpComponentFunding>> tempComp,
			Collection relatedLinks, String stepText[]) throws Exception, AMPError{
		
		
		/**
		 * 
		 * Step Number shouldn't be necessary the same as processStepX 
		 * But it will be nice to keep the step's in a order
		 * 
		 */
		switch (stepNumber) {
		/**
		 * Step Text should be set before runing the handler so forwards can be properlly set
		 * in case of any exception
		 */
		case 0:
			stepText[stepNumber] = "1";
			processStep1(check, eaForm, activity, errors, request);
			break;
		case 1:
			stepText[stepNumber] = "1_5";
			processStep1_5(check, eaForm, activity, errors, request);
			break;
		case 2:
			stepText[stepNumber] = "2";
			processStep2(check, eaForm, activity, errors, request, session);
			break;
		case 3:
			stepText[stepNumber] = "3";
			processStep3(check, eaForm, activity, errors, request);
			break;
		case 4:
			stepText[stepNumber] = "4";
			processStep4(check, eaForm, activity, errors, request);
			break;
		case 5:
			stepText[stepNumber] = "5";
			processStep5(check, eaForm, activity, errors, request, tempComp);
			break;
		case 6:
			stepText[stepNumber] = "6";
			processStep6(check, eaForm, activity, errors, request, relatedLinks);
			break;
		case 7:
			stepText[stepNumber] = "7";
			processStep7(check, eaForm, activity, errors, request);
			break;
		case 8:
			stepText[stepNumber] = "8";
			processStep8(check, eaForm, activity, errors, request);
			break;
		case 9:
			stepText[stepNumber] = "9";
			processStep9(check, eaForm, activity, errors, request);
			break;
		case 10:
			stepText[stepNumber] = "11";
			processStep11(check, eaForm, activity, errors, request);
			break;
			
		default:
			break;
		}
		
	}
	
	private void cleanup(EditActivityForm eaForm, HttpSession session, HttpServletRequest request, ActionMapping mapping, Long actId, TeamMember tm){
		
		eaForm.setFundDonor(null);
		eaForm.setStep("1");
		eaForm.setReset(true);
		eaForm.setDocReset(true);
		eaForm.setLocationReset(true);
		eaForm.setOrgPopupReset(true);
		eaForm.setOrgSelReset(true);
		eaForm.setComponentReset(true);
//		eaForm.setSectorReset(true);
		eaForm.setPhyProgReset(true);
		// Clearing comment properties
		eaForm.getCommentsCol().clear();
		eaForm.setCommentFlag(false);
		// Clearing approval process properties
		eaForm.setWorkingTeamLeadFlag("no");
		eaForm.setFundingRegions(null);
		eaForm.setRegionalFundings(null);
		eaForm.getPlanning().setLineMinRank(null);
		eaForm.getPlanning().setPlanMinRank(null);
                    eaForm.setTeamLead(false);

		/* Clearing categories */
		eaForm.getIdentification().setAccessionInstrument(new Long(0));
		eaForm.setAcChapter(new Long(0));
		eaForm.getPlanning().setStatusId(new Long(0));
		eaForm.setGbsSbs(new Long(0));
        eaForm.setDraft(null);
        eaForm.getIdentification().setProjectCategory(new Long(0));
		/* END - Clearing categories */

		eaForm.setPageId(-1);
		//UpdateDB.updateReportCache(activity.getAmpActivityId());
		eaForm.reset(mapping, request);

		if (session.getAttribute(Constants.AMP_PROJECTS) != null) {

			Collection col = (Collection) session.getAttribute(
					Constants.AMP_PROJECTS);
			AmpProject project = new AmpProject();
			project.setAmpActivityId(actId);
			col.remove(project);

			Collection actIds = new ArrayList();
			actIds.add(actId);
			Collection dirtyActivities = DesktopUtil.getAmpProjects(actIds);
			if(dirtyActivities!=null && dirtyActivities.size()>0){
				Iterator pItr = dirtyActivities.iterator();
				if (pItr.hasNext()) {
					AmpProject proj = (AmpProject) pItr.next();
					col.add(proj);
				}
			}

			session.setAttribute(Constants.AMP_PROJECTS,col);
			session.setAttribute(Constants.DIRTY_ACTIVITY_LIST,dirtyActivities);
			session.setAttribute(Constants.DESKTOP_SETTINGS_CHANGED,new Boolean(true));
		}

		if (tm.getTeamHead()) {
			if (session.getAttribute(Constants.MY_TASKS) != null) {
				session.removeAttribute(Constants.MY_TASKS);
			}
		}
		
	}
	
	private Long switchSave(RecoverySaveParameters rsp)
			throws Exception{
		
		Long actId;
		if (rsp.getEaForm().isEditAct()) {
			rsp.setOldActivityId(rsp.getEaForm().getActivityId());
			rsp.setEdit(true);

			/*actId = ActivityUtil.saveActivity(activity, eaForm.getActivityId(),
					true, eaForm.getCommentsCol(), eaForm
					.isSerializeFlag(), field, relatedLinks, tm
					.getMemberId(), eaForm.getIndicatorsME(),tempComp,eaForm.getContracts(), alwaysRollback);
			*/
			actId = ActivityUtil.saveActivity(rsp);
		}
		else{
			rsp.setOldActivityId(null);
			rsp.setEdit(false);
			/*actId = ActivityUtil.saveActivity(activity, null, false, 
					eaForm.getCommentsCol(), eaForm.isSerializeFlag(),
					field, relatedLinks,tm.getMemberId() , 
					eaForm.getIndicatorsME(), tempComp, eaForm.getContracts(), 
					alwaysRollback);
			*/
			actId = ActivityUtil.saveActivity(rsp);
		}
		return actId;
	}
	
	public Long recoverySave(RecoverySaveParameters rsp) throws AMPError{
		
		AMPError err;
		Long actId;
		
		AmpActivity recoveryActivity=null;
		boolean recoveryMode = false;
		logger.debug("Attempting normal save!");
		try {
			rsp.setAlwaysRollback(false);
			actId = switchSave(rsp);
			logger.debug("Succeeded!");
			return actId;
		} catch (Exception e) {
			//TODO: Record error that initially caused save problems
			recoveryMode = true;
		}
		
		if (recoveryMode){
			logger.debug("<<<<RECOVERY MODE>>>>");
			rsp.setDidRecover(true);
			int currentStep = 0;
			int badSteps = 0;
			
			//we try to add each step to the amp activity and after adding each step we try to save
			//if the save fails we exclude the step, and we rebuild the AmpActivity without that step
			boolean rebuild = false;
			logger.debug("Building savable activity!");
			while (currentStep < rsp.getNoOfSteps() || rebuild){
				recoveryActivity = new AmpActivity();
				rsp.setActivity(recoveryActivity);
				thisStep:
				{
					//We force this step 
					try {
						processActivityMustHaveInfo(rsp.getEaForm(), recoveryActivity);
						processPreStep(rsp.getEaForm(), recoveryActivity, rsp.getTm(),new Boolean[]{ rsp.isCreatedAsDraft()});

						//we set the activity as draft
						//this must be set after the method processPreStep so it overrides the value
						recoveryActivity.setCreatedAsDraft(true);
						
					} catch (Exception e) {
						//these steps are essential
						logger.error("Essential Step Failure!");
						err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_ERROR, false);
						throw err;
					}				
					//We rebuild AmpActivity including all steps lower than currentStep
					logger.debug("Adding previous steps!");
					for (int i = 0; i < currentStep; i++)
						if (!rsp.getStepFailure()[i])
							try {
								processStepX(i, false, rsp.getEaForm(), recoveryActivity, rsp.getErrors(), rsp.getRequest(), rsp.getSession(), rsp.getTempComp(), rsp.getRelatedLinks(), rsp.getStepText());
							} catch (Exception e) {
								// All steps till here should be validated, but let's take precaution
								rsp.getStepFailure()[i] = true;
								//TODO: Log a small error, that here I should never be
								
								//We invalidated this step so let's take it all over again
								badSteps++;
								if (badSteps >= rsp.getNoOfSteps()){
									logger.error("WARNING: FOREVER LOOP SPOTTED!");
									//precaution so we don't end in a forever loop
									err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_ERROR, false);
									throw err;
								}
								break thisStep;
							}
					//no more problems with the steps ... we reset the counter
					badSteps = 0;
					logger.debug("Activity built!");
					if (rebuild){ //if we needed just to rebuild the activity then exit
						//adding final touches to activity
						processPostStep(rsp.getEaForm(), recoveryActivity, rsp.getTm());
						recoveryActivity.setDraft(true);
						
						rebuild = false;
						break thisStep;
					}
					logger.debug("Adding step:" + String.valueOf(currentStep));
					//we got to the step that we're trying to add 
					try{
						processStepX(currentStep, false, rsp.getEaForm(), recoveryActivity, rsp.getErrors(), rsp.getRequest(), rsp.getSession(), rsp.getTempComp(), rsp.getRelatedLinks(), rsp.getStepText());
					} catch (Exception e) {
						logger.debug("    FAILED!");
						//mark the step as failed 
						rsp.getStepFailure()[currentStep] = true;
						//no need to try saving ... because we didn't improve anything
						currentStep++;
						break thisStep;
					}
					
					//adding final touches to activity
					processPostStep(rsp.getEaForm(), recoveryActivity, rsp.getTm());
					recoveryActivity.setDraft(true);
					
					logger.debug("Attempting partial save!");
					//now let's try saving with the currently built AmpActivity
					try {
						rsp.setAlwaysRollback(true);
						actId = switchSave(rsp);						
					} catch (Exception e) {
						logger.debug("     FAILED!");
						rsp.getStepFailure()[currentStep] = true;
					}
					if (currentStep == rsp.getNoOfSteps() - 1) //if the last added Step has failed then rebuild the activity
						rebuild = true;
					currentStep++;
				}
			}
			
		}
		logger.debug("PARTIAL SAVE!");
		try {
			if (recoveryActivity != null){
				rsp.setAlwaysRollback(false);
				actId = switchSave(rsp);
			}
			else{
				throw new AMPActivityError(Constants.AMP_ERROR_LEVEL_ERROR, false);
			}
			logger.debug("SUCCESS!");
			return actId;
		} catch (Exception e) {
			logger.debug("FAILED!!!!!!!!!!!!");
			err = new AMPActivityError(Constants.AMP_ERROR_LEVEL_ERROR, false);
			throw err;
		}
	}
	
	//refractoring Save activity
	//Arty - AMP-4012
	//
	// errors will be treated per step, so user will be redirected to the first step where he has errors 
	// and he will be warned about ALL the errors that he has to correct .. thus improving efficiency for the user
	//
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Long actId = null;
		HttpSession session = request.getSession();
		Set<Components<AmpComponentFunding>> tempComp = new HashSet<Components<AmpComponentFunding>>();
		ampContext = getServlet().getServletContext();
		ActionErrors errors = new ActionErrors();
		EditActivityForm eaForm = (EditActivityForm) form;
		AmpActivity activity = new AmpActivity();
		Collection relatedLinks = new ArrayList();
		
		/**
		 * Forward the user if:
		 * if user has not logged in
		 * 
		 */
		if (session.getAttribute("currentMember") == null || eaForm.getPageId() < 0 || eaForm.getPageId() > 1) {
			return mapping.findForward("index");
		}

		//Some session cleanup
		session.removeAttribute("report");
		session.removeAttribute("reportMeta");
		session.removeAttribute("forStep9");
		session.removeAttribute("commentColInSession");

		TeamMember tm = null;
		if (session.getAttribute("currentMember") != null)
			tm = (TeamMember) session.getAttribute("currentMember");

		Boolean[] createdAsDraft={false};
		//any processing that needs to be done to the activity before the actual steps
		
		processPreStep(eaForm, activity, tm, createdAsDraft);

		String toDelete = request.getParameter("delete");
		if (toDelete == null || (!toDelete.trim().equalsIgnoreCase("true"))) {
			if (eaForm.isEditAct() == false) {
				AmpActivity act = ActivityUtil.getActivityByName(eaForm.getIdentification().getTitle());
				if (act != null) {
					eaForm.setActivityId(act.getAmpActivityId());
					logger.debug("Activity with the name "
							+ eaForm.getIdentification().getTitle() + " already exist.");
					return mapping.findForward("activityExist");
				}
			}
		} else if (toDelete.trim().equals("true")) {
			eaForm.setEditAct(true);
		} else {
			logger.debug("No duplicate found");
		}

		
		boolean titleFlag = false;
		boolean statusFlag = false;

		
		
		//The number of processStep methods we have
		final int noOfSteps = 11;
		String stepText[] = new String[noOfSteps];
		Boolean stepFailure[] = new Boolean[noOfSteps];
		for (int i = 0; i < noOfSteps; i++){
			stepFailure[i] = false;
			stepText[i] = new String();
		}
		eaForm.setStepText(stepText);
		eaForm.setStepFailure(stepFailure);

		
		logger.debug("No of steps:" + String.valueOf(eaForm.getSteps().size()));
		
		int stepNumber = 0;
		while (stepNumber < noOfSteps){
			try {
				processStepX(stepNumber, true, eaForm, activity, errors, request, session, tempComp, relatedLinks, stepText);
			} catch (AMPError error) {
				if (!error.isContinuable()){
					if (error.getLevel() == Constants.AMP_ERROR_LEVEL_WARNING){
						//in this case user messed up, the ActionError was set-up in the method
						//we just redirect him to the page where he needs to correct the input
						logger.debug(">>> Warning -> redirect to step:" + stepText[stepNumber]);
						eaForm.setStep(stepText[stepNumber]);
						request.setAttribute("step", stepText[stepNumber]);
						String forwardText = "addActivityStep" + stepText[stepNumber];
						forwardText = "addActivityStepXNR";
						return mapping.findForward(forwardText);
					}
					else{
						//TODO: redirect to custom error page
						logger.error(">>> Error on step:" + stepText[stepNumber]);
					}
				}
				else{
					logger.error(">>> Error that is not continuable on step:" + stepText[stepNumber]);
				}
			} catch (Exception e) {
				logger.error(">>> Unknown error on step:" + stepText[stepNumber]);
			}
			stepNumber++;
		}

				
		//TAG HERE YOU SHOULD BE

		Long field = null;
		if (eaForm.getField() != null)
			field = eaForm.getField().getAmpFieldId();

		//this fields are used to determine receivers of approvals(Messaging System)
		String oldActivityApprovalStatus="";
		String editedActivityApprovalStatus="";

		
		/*
		 * Do all remaining processing for the activity before saving
		 * Both cases (when editing and when creating a new activity) are treated inside the method 
		 */
	    processPostStep(eaForm, activity, tm);
	    RecoverySaveParameters rsp;
	    if (eaForm.isEditAct()) {
			List<String> auditTrail = AuditLoggerUtil.generateLogs(
					activity, eaForm.getActivityId());
			//*** Preparing parameters for the recovery save
			rsp = new RecoverySaveParameters();
			rsp.setNoOfSteps(noOfSteps);
			rsp.setStepText(stepText);
			rsp.setStepFailure(stepFailure);
			rsp.setEaForm(eaForm);
			rsp.setTm(tm);
			
			rsp.setActivity(activity);
			rsp.setCreatedAsDraft(createdAsDraft[0]);
			rsp.setErrors(errors);
			rsp.setRequest(request);
			rsp.setSession(session);
			rsp.setField(field);
			
			rsp.setRelatedLinks(relatedLinks);
			rsp.setTempComp(tempComp);
			rsp.setDidRecover(false);
			//***
			
			// update an existing activity
			actId = recoverySave(rsp);
			activity = rsp.getActivity();
			
			/*actId = ActivityUtil.saveActivity(activity, eaForm.getActivityId(),
					true, eaForm.getCommentsCol(), eaForm
					.isSerializeFlag(), field, relatedLinks, tm
					.getMemberId(), eaForm.getIndicatorsME(),tempComp,eaForm.getContracts());
			*/
			
			//update lucene index
			LuceneUtil.addUpdateActivity(request, true, actId);
			//for logging the activity
			AuditLoggerUtil.logActivityUpdate(session, request,
					activity, auditTrail);

			// remove the activity details from the edit activity list
			if (toDelete == null
					|| (!toDelete.trim().equalsIgnoreCase("true"))) {
				String sessId = session.getId();
				synchronized (ampContext) {
					HashMap activityMap = (HashMap) ampContext
					.getAttribute(Constants.EDIT_ACT_LIST);
					activityMap.remove(sessId);
					ArrayList sessList = (ArrayList) ampContext
					.getAttribute(Constants.SESSION_LIST);
					sessList.remove(sessId);
					Collections.sort(sessList);
					ampContext.setAttribute(Constants.EDIT_ACT_LIST,
							activityMap);
					ampContext.setAttribute(Constants.SESSION_LIST,
							sessList);

					HashMap tsList = (HashMap) ampContext
					.getAttribute(Constants.TS_ACT_LIST);
					if (tsList != null) {
						tsList.remove(eaForm.getActivityId());
					}
					ampContext.setAttribute(Constants.TS_ACT_LIST,
							tsList);
					HashMap uList = (HashMap) ampContext
					.getAttribute(Constants.USER_ACT_LIST);
					if (uList != null) {
						uList.remove(tm.getMemberId());
					}
					ampContext.setAttribute(Constants.USER_ACT_LIST,
							uList);

				}
			}
		} else {
			// create a new activity
			

			//*** Preparing parameters for the recovery save
			rsp = new RecoverySaveParameters();
			rsp.setNoOfSteps(noOfSteps);
			rsp.setStepText(stepText);
			rsp.setStepFailure(stepFailure);
			rsp.setEaForm(eaForm);
			rsp.setTm(tm);
			
			rsp.setActivity(activity);
			rsp.setCreatedAsDraft(createdAsDraft[0]);
			rsp.setErrors(errors);
			rsp.setRequest(request);
			rsp.setSession(session);
			rsp.setField(field);
			
			rsp.setRelatedLinks(relatedLinks);
			rsp.setTempComp(tempComp);
			rsp.setDidRecover(false);
			//***

			actId = recoverySave(rsp);			
			activity = rsp.getActivity();
			
			/*actId = ActivityUtil.saveActivity(activity, null, false, eaForm.getCommentsCol(), eaForm.isSerializeFlag(),
					field, relatedLinks,tm.getMemberId() , eaForm.getIndicatorsME(), tempComp, eaForm.getContracts());
			*/
			//update lucene index
			LuceneUtil.addUpdateActivity(request, false, actId);
			//for logging the activity
			AuditLoggerUtil.logObject(session, request, activity, "add");
		}

		//If we're adding an activity, create system/admin message
		if(!createdAsDraft[0]) {
			ActivitySaveTrigger ast=new ActivitySaveTrigger(activity);
		}

		boolean needApproval=false;
		boolean approved=false;
		//this field is used to define if "activity approved" approval has to be created
		boolean needNewAppForApproved=true;

		AmpActivity myActivity=ActivityUtil.loadActivity(actId);
		editedActivityApprovalStatus=myActivity.getApprovalStatus();

		if(eaForm.isEditAct()){
			if(tm.getTeamHead()){
				/**
				 * we have two cases: team leader approves activity or edits already approved one. if so(second situation), then no messages should be
				 * created. If activity that team leader edited was not approved,this means that team leader now approved it and we need to send message to
				 * creator/updater of activity to let him know his activity was approved.
				 */
				if(oldActivityApprovalStatus.equals(org.digijava.module.aim.helper.Constants.APPROVED_STATUS)){
					needNewAppForApproved=false;
				}
				needApproval = false;
				approved=true;
			}else if("newOnly".equals(tm.getAppSettings().getValidation())){
				needNewAppForApproved=false;
				approved=true;
				needApproval = false;
			}else{
				needApproval = true;
				approved=false;
			}
		}else{
			if(tm.getTeamHead()){
				needNewAppForApproved=false;
				needApproval=false;
				approved=true;
			}else{
				approved=false;
				needApproval=true;
			}
		}

		/**
		 * I am doing this,because activity field holds old value of updatedBy and myActivity field holds new one.
		 * If team leader approved activity,then myActivity has updatedBy=teamLeader and activity has previous updater if he/she exists.
		 * So If updater exists message  should be sent to him, not team leader.
		 * But if someone(not team leader) edited activity, then message should be sent to him.
		 */
		if(tm.getTeamHead()){
			myActivity=activity;
			myActivity.setUpdatedBy(eaForm.getUpdatedBy());
		}

		//if workspace has no manager, then there is no need to approve any activity.
		if(TeamMemberUtil.getTeamHead(tm.getTeamId())!=null){
			//check whether Activity is approved or needs Approval
			if(approved && needNewAppForApproved&&!myActivity.getDraft()){
				new ApprovedActivityTrigger(myActivity);
			}
			if(needApproval&&!myActivity.getDraft()){
				new NotApprovedActivityTrigger(myActivity);
			}
		}

		if(DocumentUtil.isDMEnabled()) {
			Site currentSite = RequestUtils.getSite(request);
			Node spaceNode = DocumentUtil.getDocumentSpace(currentSite,
					activity.getDocumentSpace());
			DocumentUtil.renameNode(spaceNode, activity.getName());
		}

		//We need to getPageId before the cleanup so it doesn't get reset
		int temp = eaForm.getPageId();

		/**
		 *  Perform session & form cleanup
		 */
		cleanup(eaForm, session, request, mapping, actId, tm);

		//OLD!!!
		//boolean surveyFlag = false;

		if (temp == 0)
			return mapping.findForward("adminHome");
		else if (temp == 1) {
			//OLD surveyFlag was always false
			/*if (surveyFlag) { // forwarding to edit survey action for
				// saving survey responses
				logger.debug("forwarding to edit survey action...");
				return mapping.findForward("saveSurvey");
			} else {*/
				if (rsp.isDidRecover())
					return mapping.findForward("saveErrors");
				else
					return mapping.findForward("viewMyDesktop");
			//}
		} else {
			logger.info("returning null....");
			return null;
		}

	
	}


	private boolean isSectorEnabled() {
 	    ServletContext ampContext = getServlet().getServletContext();
	    AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
		if(currentTemplate!=null)
			if(currentTemplate.getFeatures()!=null)
				for(Iterator it=currentTemplate.getFeatures().iterator();it.hasNext();)
				{
					AmpFeaturesVisibility feature=(AmpFeaturesVisibility) it.next();
					if(feature.getName().compareTo("Sectors")==0)
					{
						return true;
					}

				}
		return false;
	}

	private boolean isStatusEnabled() {
 	   ServletContext ampContext = getServlet().getServletContext();

	   AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");

		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
		if(currentTemplate!=null)
			if(currentTemplate.getFields()!=null)
				for(Iterator it=currentTemplate.getFields().iterator();it.hasNext();)
				{
					AmpFieldsVisibility field=(AmpFieldsVisibility) it.next();
					//System.out.println(field.getName());
					if(field.getName().equals("Status"))
					{
						return true;
					}

				}
		return false;
	}

	/**
	 * @param tempComp
	 * @param eaForm
	 * @param activity
	 */
	private void proccessComponents(Collection<Components<AmpComponentFunding>> tempComps, EditActivityForm eaForm, AmpActivity activity) {
		activity.setComponents(new HashSet());
		if (eaForm.getSelectedComponents() != null) {
			Iterator<Components<FundingDetail>> itr = eaForm.getSelectedComponents().iterator();
			while (itr.hasNext()) {
				Components<FundingDetail> comp = itr.next();
				Components<AmpComponentFunding> tempComp = new Components<AmpComponentFunding>();

				AmpComponent ampComp = null;
				Collection col = ComponentsUtil.getComponent(comp.getTitle());
				Iterator it = col.iterator();
				if(it.hasNext()){
					ampComp = (AmpComponent)it.next();
					activity.getComponents().add(ampComp);
				}

				if (comp.getCommitments() != null
						&& comp.getCommitments().size() > 0) {
					HashSet<AmpComponentFunding> temp = new HashSet<AmpComponentFunding>();
					Iterator<FundingDetail> commitmentsIter = comp.getCommitments().iterator();
					while (commitmentsIter.hasNext()) {
						FundingDetail fd = commitmentsIter.next();
						AmpComponentFunding ampCompFund = new AmpComponentFunding();
						ampCompFund.setActivity(activity);
						ampCompFund.setTransactionType(new Integer(
								Constants.COMMITMENT));
						Iterator tmpItr = eaForm.getCurrencies()
								.iterator();
						while (tmpItr.hasNext()) {
							AmpCurrency curr = (AmpCurrency) tmpItr
									.next();
							if (curr.getCurrencyCode().equals(
									fd.getCurrencyCode())) {
								ampCompFund.setCurrency(curr);
								break;
							}
						}

						ampCompFund.setAmpComponentFundingId(fd.getAmpComponentFundingId());
						ampCompFund.setReportingOrganization(null);
							ampCompFund.setTransactionAmount(FormatHelper.parseDouble(fd.getTransactionAmount()));

						ampCompFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampCompFund.setAdjustmentType(new Integer(fd
								.getAdjustmentType()));
						ampCompFund.setComponent(ampComp);
						temp.add(ampCompFund);
					}
					tempComp.setCommitments(temp);
				}


				if (comp.getDisbursements() != null
						&& comp.getDisbursements().size() > 0) {
					HashSet<AmpComponentFunding> temp = new HashSet<AmpComponentFunding>();
					Iterator itr2 = comp.getDisbursements().iterator();
					while (itr2.hasNext()) {
						AmpComponentFunding ampCompFund = new AmpComponentFunding();
						ampCompFund.setActivity(activity);
						ampCompFund.setTransactionType(new Integer(
								Constants.DISBURSEMENT));
						FundingDetail fd = (FundingDetail) itr2.next();
						Iterator tmpItr = eaForm.getCurrencies()
								.iterator();
						while (tmpItr.hasNext()) {
							AmpCurrency curr = (AmpCurrency) tmpItr
									.next();
							if (curr.getCurrencyCode().equals(
									fd.getCurrencyCode())) {
								ampCompFund.setCurrency(curr);
								break;
							}
						}

						ampCompFund.setAmpComponentFundingId(fd.getAmpComponentFundingId());

							ampCompFund.setTransactionAmount(new Double(
									FormatHelper.parseDouble(fd
											.getTransactionAmount())));

						ampCompFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampCompFund.setAdjustmentType(new Integer(fd
								.getAdjustmentType()));
						ampCompFund.setComponent(ampComp);
						temp.add(ampCompFund);
					}
					tempComp.setDisbursements(temp);
				}

				if (comp.getExpenditures() != null
						&& comp.getExpenditures().size() > 0) {
					HashSet<AmpComponentFunding> temp = new HashSet<AmpComponentFunding>();
					Iterator itr2 = comp.getExpenditures().iterator();
					while (itr2.hasNext()) {
						AmpComponentFunding ampCompFund = new AmpComponentFunding();
						ampCompFund.setActivity(activity);
						ampCompFund.setTransactionType(new Integer(
								Constants.EXPENDITURE));
						FundingDetail fd = (FundingDetail) itr2.next();
						Iterator tmpItr = eaForm.getCurrencies()
								.iterator();
						while (tmpItr.hasNext()) {
							AmpCurrency curr = (AmpCurrency) tmpItr
									.next();
							if (curr.getCurrencyCode().equals(
									fd.getCurrencyCode())) {
								ampCompFund.setCurrency(curr);
								break;
							}
						}

						ampCompFund.setAmpComponentFundingId(fd.getAmpComponentFundingId());
						ampCompFund.setTransactionAmount(new Double(FormatHelper.parseDouble(fd.getTransactionAmount())));

						ampCompFund.setTransactionDate(DateConversion
								.getDate(fd.getTransactionDate()));
						ampCompFund.setAdjustmentType(new Integer(fd
								.getAdjustmentType()));
						ampCompFund.setComponent(ampComp);
						temp.add(ampCompFund);
					}
					tempComp.setExpenditures(temp);
				}

				// set physical progress

				if (comp.getPhyProgress() != null) {
					Set phyProgess = new HashSet();
					Iterator itr1 = comp.getPhyProgress().iterator();
					while (itr1.hasNext()) {
						PhysicalProgress phyProg = (PhysicalProgress) itr1
								.next();
						AmpPhysicalPerformance ampPhyPerf = new AmpPhysicalPerformance();
						if (phyProg.getDescription() == null
								|| phyProg.getDescription().trim()
										.length() == 0) {
							ampPhyPerf.setDescription(new String(" "));
						} else {
							ampPhyPerf.setDescription(phyProg
									.getDescription());
						}
                                                if(!phyProg.isNewProgress()){
                                                  ampPhyPerf.setAmpPpId(phyProg.
                                                      getPid());
                                                }
						ampPhyPerf.setReportingDate(DateConversion
								.getDate(phyProg.getReportingDate()));
						ampPhyPerf.setTitle(phyProg.getTitle());
						ampPhyPerf.setAmpActivityId(activity);
						ampPhyPerf.setComponent(ampComp);
						ampPhyPerf.setComments(" ");
						phyProgess.add(ampPhyPerf);
					}
					tempComp.setPhyProgress(phyProgess);
				}
                                tempComp.setComponentId(comp.getComponentId());
                                tempComp.setType_Id(comp.getType_Id());

                                tempComps.add(tempComp);
			}
		}
	}

	private void saveMTEFProjections (Funding fund, AmpFunding ampFunding) {
		if ( ampFunding.getMtefProjections() != null ) {
			ampFunding.getMtefProjections().clear();
		}
		else
			ampFunding.setMtefProjections(new HashSet<AmpFundingMTEFProjection> ());
		if(fund.getMtefProjections()!=null)
		{
			Iterator mtefItr=fund.getMtefProjections().iterator();
			while (mtefItr.hasNext())
			{
				MTEFProjection mtef=(MTEFProjection)mtefItr.next();
				AmpFundingMTEFProjection ampmtef=new AmpFundingMTEFProjection();
				ampmtef.setAmount(FormatHelper.parseDouble(mtef.getAmount()));

				ampmtef.setAmpFunding(ampFunding);
				ampmtef.setAmpCurrency(CurrencyUtil.getCurrencyByCode(mtef.getCurrencyCode()));
				ampmtef.setProjected( CategoryManagerUtil.getAmpCategoryValueFromDb(mtef.getProjected()) );
				ampmtef.setProjectionDate( DateConversion.getDate(mtef.getProjectionDate()) );

				ampFunding.getMtefProjections().add(ampmtef);
			}
		}
	}
	  private boolean isPrimarySectorEnabled() {
	 	    ServletContext ampContext = getServlet().getServletContext();
		    AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
			AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
			if(currentTemplate!=null)
				if(currentTemplate.getFeatures()!=null)
					for(Iterator it=currentTemplate.getFields().iterator();it.hasNext();)
					{
						AmpFieldsVisibility field=(AmpFieldsVisibility) it.next();
						if(field.getName().compareTo("Primary Sector")==0)
						{
							return true;
						}

					}
			return false;
	  }
	  private boolean isSecondarySectorEnabled() {
	 	    ServletContext ampContext = getServlet().getServletContext();
		    AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
			AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
			if(currentTemplate!=null)
				if(currentTemplate.getFeatures()!=null)
					for(Iterator it=currentTemplate.getFields().iterator();it.hasNext();)
					{
						AmpFieldsVisibility field=(AmpFieldsVisibility) it.next();
						if(field.getName().compareTo("Secondary Sector")==0)
						{
							return true;
						}

					}
			return false;
	  }

}
