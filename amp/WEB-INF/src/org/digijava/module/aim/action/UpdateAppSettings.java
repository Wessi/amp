package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.UpdateAppSettingsForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.mathcs.backport.java.util.Arrays;

public class UpdateAppSettings extends Action {

	private static Logger logger = Logger.getLogger(UpdateAppSettings.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws java.lang.Exception {

		UpdateAppSettingsForm uForm = (UpdateAppSettingsForm) form;
		
		String shareResAction=request.getParameter("shareResAction");
		
		this.populatePossibleValsAddTR(uForm);
		
		this.populatePublishResourcesPossibleVals(uForm);
		
		this.populateShareResAmongWorkspacesPossibleVals(uForm);
		
		logger.debug("In updtate app settings");
		HttpSession session = request.getSession();

		if (session.getAttribute("currentMember") == null) {
			return mapping.findForward("index");
		}

		TeamMember tm = (TeamMember) session.getAttribute("currentMember");

		if (request.getParameter("updated") != null
				&& request.getParameter("updated").equals("true")) {
			uForm.setUpdated(true);
		} else {
			uForm.setUpdated(false);
		}

		if (request.getParameter("errors") != null	&& request.getParameter("errors").equals("true")) {
			uForm.setErrors(true);
		} else {
			uForm.setErrors(false);
		}
		
		if (uForm.getType() == null || uForm.getType().trim().equals("") || (shareResAction!=null && shareResAction.equalsIgnoreCase("getOptions"))) {
			String path = mapping.getPath();
			logger.debug("path = " + path);
			AmpApplicationSettings ampAppSettings = null;
			boolean loadValues = false;
			if (uForm.getType() == null || uForm.getType().trim().equals("")) {
				loadValues = true;
			}
			if (path != null
					&& (path.trim().equals("/aim/defaultSettings") || path
							.trim().equals("/defaultSettings"))) {
				if (tm.getTeamHead() == false) {
					return mapping.findForward("viewMyDesktop");
				}
				uForm.setType("default");
				uForm.setTeamName(tm.getTeamName());
				ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
			} else if (path != null
					&& (path.trim().equals("/aim/customizeSettings") || path
							.trim().equals("/customizeSettings"))) {
				uForm.setType("userSpecific");
				uForm.setMemberName(tm.getMemberName());
				ampAppSettings = DbUtil.getMemberAppSettings(tm.getMemberId());
			}
			// added by mouhamad for burkina on 21/02/08
			session = request.getSession();
			String name = "- " + ampAppSettings.getCurrency().getCurrencyName();
			session.setAttribute(ArConstants.SELECTED_CURRENCY, name);

			// AMP-3168 Currency conversion in team workspace setup
			session.setAttribute("reportCurrencyCode", ampAppSettings.getCurrency().getCurrencyCode());
			AmpARFilter filter = (AmpARFilter) session.getAttribute(ArConstants.REPORTS_FILTER);
			if (filter != null) {
				filter.setCurrency(ampAppSettings.getCurrency());
				session.setAttribute(ArConstants.REPORTS_FILTER, filter);
			}
			if (ampAppSettings != null && loadValues) {
				uForm.setAppSettingsId(ampAppSettings.getAmpAppSettingsId());
				uForm.setDefRecsPerPage(ampAppSettings.getDefaultRecordsPerPage());

				Integer reportsPerPage = ampAppSettings.getDefaultReportsPerPage();
				if (reportsPerPage == null) {
					reportsPerPage = 0;
				}
				Integer reportStartYear = ampAppSettings.getReportStartYear();
				if (reportStartYear == null) {
					reportStartYear = 0;
				}
				Integer reportEndYear = ampAppSettings.getReportEndYear();
				if (reportEndYear == null) {
					reportEndYear = 0;
				}
				if(shareResAction==null){
					uForm.setAllowAddTeamRes( ampAppSettings.getAllowAddTeamRes() );
					this.populateShareResAmongWorkspacesPossibleVals(uForm);
				}
				uForm.setAllowShareAccrossWRK(ampAppSettings.getAllowShareTeamRes());
				uForm.setAllowPublishingResources(ampAppSettings.getAllowPublishingResources());
				
				uForm.setDefReportsPerPage(reportsPerPage);
				uForm.setReportStartYear(reportStartYear);
				uForm.setReportEndYear(reportEndYear);
				uForm.setLanguage(ampAppSettings.getLanguage());
				uForm.setValidation(ampAppSettings.getValidation());
				uForm.setCurrencyId(ampAppSettings.getCurrency().getAmpCurrencyId());
                if(ampAppSettings.getFiscalCalendar()!=null){
				uForm.setFisCalendarId(ampAppSettings.getFiscalCalendar().getAmpFiscalCalId());
                }

				if (ampAppSettings.getDefaultTeamReport() != null){
					uForm.setDefaultReportForTeamId(ampAppSettings.getDefaultTeamReport().getAmpReportId());
				}else{
					uForm.setDefaultReportForTeamId(new Long(0));
				}
					
			}
			
			
			//get team members
			Long currentTeamId=tm.getTeamId();
			List<TeamMember> members =TeamMemberUtil.getAllMembersExcludingTL(currentTeamId);
			uForm.setTeamMembers(members);
			if(uForm.getAllowPublishingResources()!=null && uForm.getAllowPublishingResources().equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_SPECIFIC_USERS)){
				//Long currentTeamId=tm.getTeamId();
				//List<TeamMember> members =TeamMemberUtil.getAllMembersExcludingTL(currentTeamId);
				//uForm.setTeamMembers(members);
				if(members!=null){
					List<Long> selMembersIds= null; 
					for (TeamMember teamMember : members) {
						if(teamMember.getPublishDocuments()!=null && teamMember.getPublishDocuments()){
							if(selMembersIds==null){
								selMembersIds=new ArrayList<Long>();
							}
							selMembersIds.add(teamMember.getMemberId());
						}
					}
					if(selMembersIds!=null && selMembersIds.size()>0){
						uForm.setSelTeamMembers(selMembersIds.toArray(new Long[selMembersIds.size()] ));
					}
				}
			}
			
			/* Select only the reports that are shown as tabs */
			List<AmpReports> reports = TeamUtil.getAllTeamReports(tm.getTeamId(), null,null, null, true, tm.getMemberId(),null);
			if (reports != null) {
				Iterator iterator = reports.iterator();
				while (iterator.hasNext()) {
					AmpReports ampreport = (AmpReports) iterator.next();
					if (ampreport.getDrilldownTab() == null
							|| !ampreport.getDrilldownTab().booleanValue()) {
						iterator.remove();
					}
				}
			}
			
			Collections.sort(reports, 
					new Comparator<AmpReports> () {
						public int compare(AmpReports o1, AmpReports o2) {
							return o1.getName().compareTo( o2.getName() );
						}
					}
				);
			
			uForm.setReports(reports);
			uForm.setCurrencies(CurrencyUtil
					.getAllCurrencies(CurrencyUtil.ALL_ACTIVE));
			uForm.setFisCalendars(DbUtil.getAllFisCalenders());

			// set Navigation languages
			Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));

			HashMap translations = new HashMap();
			Iterator iterator = TrnUtil.getLanguages(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
			while (iterator.hasNext()) {
				TrnLocale item = (TrnLocale) iterator.next();
				translations.put(item.getCode(), item);
			}
			// sort languages
			List sortedLanguages = new ArrayList();
			iterator = languages.iterator();
			while (iterator.hasNext()) {
				Locale item = (Locale) iterator.next();
				sortedLanguages.add(translations.get(item.getCode()));
			}
			Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);
			uForm.setLanguages(sortedLanguages);

			logger.info("TYpe =" + uForm.getType());
			if (uForm.getType().equalsIgnoreCase("userSpecific")) {
				logger.info("mapping to showUserSettings");
				return mapping.findForward("showUserSettings");
			} else {
				return mapping.findForward("showDefaultSettings");
			}
		} else {
			logger.debug("In saving");
			SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
			
			String context = SiteUtils.getSiteURL(currentDomain, request
					.getScheme(), request.getServerPort(), request
					.getContextPath());
			if(uForm.getDefRecsPerPage() == null || uForm.getDefRecsPerPage() < 2)
				{
					ActionMessages errors = new ActionMessages();
	        		errors.add("title", new ActionMessage("error.aim.addActivity.wrongNrActsPerPage", TranslatorWorker.translateText("Please enter the title",request)));
	        		if (errors.size() > 0)
	        			saveErrors(request, errors);
	        		
					if (uForm.getType().equals("default")) {
						uForm.setType(null);
						String url = context + "/translation/switchLanguage.do?code="
								+ uForm.getLanguage() + "&rfr=" + context
								+ "/aim/defaultSettings.do~errors=true~updated="
								+ uForm.getUpdated();
						response.sendRedirect(url);
						logger.debug("redirecting " + url + " ....");
						uForm.setErrors(true);
						return null;
					} else if (uForm.getType().equals("userSpecific")) {
						uForm.setType(null);
						String url = context + "/translation/switchLanguage.do?code="
								+ uForm.getLanguage() + "&rfr=" + context
								+ "/aim/customizeSettings.do~errors=true~updated="
								+ uForm.getUpdated();
						response.sendRedirect(url);
						logger.debug("redirecting " + url + " ....");
						uForm.setErrors(false);
						return null;
					 }
				}
			AmpApplicationSettings ampAppSettings = null;
			if ("save".equals(uForm.getSave())) {
				ampAppSettings = new AmpApplicationSettings();
				ampAppSettings.setAmpAppSettingsId(uForm.getAppSettingsId());
				ampAppSettings.setDefaultRecordsPerPage(new Integer(uForm.getDefRecsPerPage()));
				ampAppSettings.setReportStartYear((new Integer(uForm.getReportStartYear())));
				ampAppSettings.setReportEndYear((new Integer(uForm.getReportEndYear())));
				ampAppSettings.setDefaultReportsPerPage(uForm.getDefReportsPerPage());
				ampAppSettings.setCurrency(CurrencyUtil.getAmpcurrency(uForm.getCurrencyId()));
				ampAppSettings.setFiscalCalendar(DbUtil.getAmpFiscalCalendar(uForm.getFisCalendarId()));
				ampAppSettings.setLanguage(uForm.getLanguage());
				ampAppSettings.setValidation(uForm.getValidation());
				ampAppSettings.setTeam(TeamUtil.getAmpTeam(tm.getTeamId()));
				ampAppSettings.setAllowAddTeamRes( uForm.getAllowAddTeamRes() );
				ampAppSettings.setAllowShareTeamRes(uForm.getAllowShareAccrossWRK());
				ampAppSettings.setAllowPublishingResources(uForm.getAllowPublishingResources());
				//
				AmpReports ampReport = DbUtil.getAmpReports(uForm.getDefaultReportForTeamId());
				//
				ampAppSettings.setDefaultTeamReport(ampReport);
				//
				session.setAttribute(Constants.DEFAULT_TEAM_REPORT, ampAppSettings.getDefaultTeamReport());
				session.setAttribute("filterCurrentReport", ampAppSettings.getDefaultTeamReport());
				// this.updateAllTeamMembersDefaultReport( tm.getTeamId(), ampReport);
				// added by mouhamad for burkina on 21/02/08
				String name = "- "+ ampAppSettings.getCurrency().getCurrencyName();
				session.setAttribute(ArConstants.SELECTED_CURRENCY, name);
				// end
				if (uForm.getType().equals("userSpecific")) {
					ampAppSettings.setMember(TeamMemberUtil.getAmpTeamMember(tm.getMemberId()));
					ampAppSettings.setUseDefault(new Boolean(false));
				} else {
					/* change all members settings whose has 'useDefault' set */
					Iterator itr = TeamMemberUtil.getAllTeamMembers(tm.getTeamId()).iterator();
					logger.debug("before while");
					while (itr.hasNext()) {
						TeamMember member = (TeamMember) itr.next();
						AmpApplicationSettings memSettings = DbUtil.getMemberAppSettings(member.getMemberId());
						if (memSettings != null) {
							if (memSettings.getUseDefault().booleanValue() == true) {
								AmpTeamMember ampMember = TeamMemberUtil.getAmpTeamMember(member.getMemberId());
								restoreApplicationSettings(memSettings, ampAppSettings, ampMember);
							}
						}
					}
				}
				try {
					DbUtil.update(ampAppSettings);
					//update team members
					if(uForm.getResetTeamMembers()!=null && uForm.getResetTeamMembers()){
						uForm.setSelTeamMembers(null);
					}
					if(uForm.getAllowPublishingResources().equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_ONLY_TL)){//allowed only to WM
						//remove rights to other tms
						TeamMemberUtil.removeTeamMembersResourcePublishingRights(tm.getTeamId(),null);
						//give permissions to TL
						AmpTeamMember teamHead= TeamMemberUtil.getTeamHead(tm.getTeamId());
						if(teamHead.getPublishDocPermission()==null){
							teamHead.setPublishDocPermission(true);
							DbUtil.saveOrUpdateObject(teamHead);
						}
					}else if (uForm.getAllowPublishingResources().equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_SPECIFIC_USERS)){//allowed specific users
						if(uForm.getSelTeamMembers()!=null && uForm.getSelTeamMembers().length!=0){							
							List<Long> selTMs=Arrays.asList(uForm.getSelTeamMembers());
							//remove rights to other tms
							TeamMemberUtil.removeTeamMembersResourcePublishingRights(tm.getTeamId(),selTMs);
							//grant publishing rights to selected tms
							TeamMemberUtil.grantMembersResourcePublishingRights(tm.getTeamId(), selTMs);
						}else{
							TeamMemberUtil.removeTeamMembersResourcePublishingRights(tm.getTeamId(),null); //in case every user was unselected
						}
					}else if (uForm.getAllowPublishingResources().equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_TM)){//allowed to all users
						//grant publishing rights to everyone
						TeamMemberUtil.grantMembersResourcePublishingRights(tm.getTeamId(), null);
					}					
					
					uForm.setUpdated(true);
				} catch (Exception e) {
					uForm.setUpdated(false);
				}
			} else if (uForm.getRestore() != null) {
				ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
				AmpApplicationSettings memSettings = DbUtil.getMemberAppSettings(tm.getMemberId());
				AmpTeamMember member = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
				try {
					restoreApplicationSettings(memSettings, ampAppSettings, member);
					uForm.setUpdated(true);
				} catch (Exception e) {
					uForm.setUpdated(false);
				}
			}
			AmpApplicationSettings tempSettings = DbUtil.getMemberAppSettings(tm.getMemberId());
			if(tempSettings!=null){
				ApplicationSettings applicationSettings = getReloadedAppSettings(tempSettings);
				tm.setAppSettings(applicationSettings);
			}			
			if (session.getAttribute(Constants.CURRENT_MEMBER) != null) {
				session.removeAttribute(Constants.CURRENT_MEMBER);
				session.setAttribute(Constants.CURRENT_MEMBER, tm);
			}
			session.setAttribute(Constants.DESKTOP_SETTINGS_CHANGED, new Boolean(true));
			//
			uForm.setUpdateFlag(false);
			//
			//SiteDomain 
			currentDomain = RequestUtils.getSiteDomain(request);

			//String 
			context = SiteUtils.getSiteURL(currentDomain, request
					.getScheme(), request.getServerPort(), request
					.getContextPath());
			if (uForm.getType().equals("default")) {
				uForm.setType(null);
				String url = context + "/translation/switchLanguage.do?code="
						+ ampAppSettings.getLanguage() + "&rfr=" + context
						+ "/aim/defaultSettings.do~updated="
						+ uForm.getUpdated();
				response.sendRedirect(url);
				logger.debug("redirecting " + url + " ....");
				// return mapping.findForward("default");
				return null;
			} else if (uForm.getType().equals("userSpecific")) {
				uForm.setType(null);
				String url = context + "/translation/switchLanguage.do?code="
						+ ampAppSettings.getLanguage() + "&rfr=" + context
						+ "/aim/customizeSettings.do~updated="
						+ uForm.getUpdated();
				response.sendRedirect(url);
				logger.debug("redirecting " + url + " ....");
				// return mapping.findForward("userSpecific");
				return null;
			} else {
				return mapping.findForward("index");
			}
		}
	}

	/* restoreApplicationSettings( oldSettings , newSettings , member) */
	public void restoreApplicationSettings(AmpApplicationSettings oldSettings,
			AmpApplicationSettings newSettings, AmpTeamMember ampMember) {

		logger.debug("In restoreApplicationSettings() ");

		/* set all values except id from oldSettings to newSettings */
		oldSettings.setDefaultRecordsPerPage(newSettings.getDefaultRecordsPerPage());
		oldSettings.setDefaultReportsPerPage(newSettings.getDefaultReportsPerPage());
		oldSettings.setCurrency(newSettings.getCurrency());
		oldSettings.setFiscalCalendar(newSettings.getFiscalCalendar());
		oldSettings.setLanguage(newSettings.getLanguage());
		oldSettings.setValidation(newSettings.getValidation());
		oldSettings.setTeam(newSettings.getTeam());
		oldSettings.setMember(ampMember);
		oldSettings.setReportStartYear(newSettings.getReportStartYear());
		oldSettings.setReportEndYear(newSettings.getReportEndYear());

		oldSettings.setUseDefault(new Boolean(true));
		oldSettings.setDefaultTeamReport(newSettings.getDefaultTeamReport());
		DbUtil.update(oldSettings);
		logger.debug("restoreApplicationSettings() returning");
	}

	public ApplicationSettings getReloadedAppSettings(AmpApplicationSettings ampAppSettings) {
		ApplicationSettings appSettings = new ApplicationSettings();
		appSettings.setAppSettingsId(ampAppSettings.getAmpAppSettingsId());
		appSettings.setDefRecsPerPage(ampAppSettings.getDefaultRecordsPerPage());
		appSettings.setReportStartYear(ampAppSettings.getReportStartYear());
		appSettings.setReportEndYear(ampAppSettings.getReportEndYear());

		appSettings.setDefReportsPerPage(ampAppSettings.getDefaultReportsPerPage());
		appSettings.setCurrencyId(ampAppSettings.getCurrency().getAmpCurrencyId());
		appSettings.setFisCalId(ampAppSettings.getFiscalCalendar().getAmpFiscalCalId());
		appSettings.setLanguage(ampAppSettings.getLanguage());
		appSettings.setValidation(ampAppSettings.getValidation());
		appSettings.setDefaultAmpReport(ampAppSettings.getDefaultTeamReport());
		return appSettings;
	}

	private void updateAllTeamMembersDefaultReport(Long teamId,AmpReports ampReport) {
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			Transaction tx = session.beginTransaction();
			String queryString = "SELECT a FROM "
					+ AmpApplicationSettings.class.getName() + " a WHERE  "
					+ "a.team=:teamId";
			Query query = session.createQuery(queryString);
			query.setParameter("teamId", teamId, Hibernate.LONG);
			Collection reports = query.list();
			Iterator iterator = reports.iterator();

			while (iterator.hasNext()) {
				AmpApplicationSettings setting = (AmpApplicationSettings) iterator.next();
				setting.setDefaultTeamReport(ampReport);
			}

			tx.commit();
			session.flush();

		} catch (Exception ex) {
			logger.error("Unable to get fundingDetails :" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}

	}
	
	private void populatePossibleValsAddTR (UpdateAppSettingsForm uForm) {
		if (uForm.getPossibleValsAddTR() == null || uForm.getPossibleValsAddTR().size() == 0 ) {
			KeyValue elem1	= new KeyValue(CrConstants.TEAM_RESOURCES_ADD_ONLY_WORKSP_MANAGER.toString(), "Managed by Workspace Manager");
			KeyValue elem2	= new KeyValue(CrConstants.TEAM_RESOURCES_ADD_ALLOWED_WORKSP_MEMBER.toString(), "Workspace Members Allowed to Add New Resources");
			KeyValue elem3	= new KeyValue(CrConstants.TEAM_RESOURCES_VERSIONING_ALLOWED_WORKSP_MEMBER.toString(), "Workspace Members Allowed to Add New Resources and Versions");
			
			uForm.setPossibleValsAddTR(new ArrayList<KeyValue>() );
			uForm.getPossibleValsAddTR().add(elem1);
			uForm.getPossibleValsAddTR().add(elem2);
			uForm.getPossibleValsAddTR().add(elem3);
		}
	}
	
	private void populateShareResAmongWorkspacesPossibleVals (UpdateAppSettingsForm uForm) {
		Integer selectedTeamResourceRight=uForm.getAllowAddTeamRes();
		uForm.setShareResAmongWorkspacesPossibleVals(new ArrayList<KeyValue>() );
			
		KeyValue elem1	= new KeyValue(CrConstants.SHARE_AMONG_WRKSPACES_ALLOWED_WM.toString(), "Managed by Workspace Manager");
		uForm.getShareResAmongWorkspacesPossibleVals().add(elem1);
			
		if(selectedTeamResourceRight!=null && selectedTeamResourceRight.equals(CrConstants.TEAM_RESOURCES_VERSIONING_ALLOWED_WORKSP_MEMBER)){
			KeyValue elem2	= new KeyValue(CrConstants.SHARE_AMONG_WRKSPACES_ALLOWED_TM.toString(), "Workspace Members Allowed to Share Resources Across Workspaces");
			uForm.getShareResAmongWorkspacesPossibleVals().add(elem2);
		}
	}
	
	private void populatePublishResourcesPossibleVals(UpdateAppSettingsForm uForm){
		if(uForm.getPublishResourcesPossibleVals()==null || uForm.getPublishResourcesPossibleVals().size() ==0){
			KeyValue elem1	= new KeyValue(CrConstants.PUBLISHING_RESOURCES_ALLOWED_ONLY_TL.toString(), "Managed by Workspace Manager");
			KeyValue elem2	= new KeyValue(CrConstants.PUBLISHING_RESOURCES_ALLOWED_TM.toString(), "All Team Members are allowed to publish documents");
			KeyValue elem3	= new KeyValue(CrConstants.PUBLISHING_RESOURCES_ALLOWED_SPECIFIC_USERS.toString(), "Only selected members are allowed to publish documents");
			
			uForm.setPublishResourcesPossibleVals(new ArrayList<KeyValue>() );
			uForm.getPublishResourcesPossibleVals().add(elem1);
			uForm.getPublishResourcesPossibleVals().add(elem2);
			uForm.getPublishResourcesPossibleVals().add(elem3);
		}
	}
}
