 /*
 * EditActivity.java
 * Created: Feb 10, 2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.GroupReportData;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRegionalObservation;
import org.digijava.module.aim.dbentity.AmpRegionalObservationActor;
import org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.ActivityContactInfo;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivityDocumentsUtil;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AmpContactsWorker;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.CustomField;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancingBreakdown;
import org.digijava.module.aim.helper.FinancingBreakdownWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil.HelperLocationAncestorLocationNamesAsc;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.helper.BudgetDbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.gateperm.core.GatePermConst;
import org.hibernate.Session;


/**
 * Loads the activity details of the activity specified in the form bean
 * variable 'activityId' to the EditActivityForm bean instance
 *
 * @author Priyajith
 */
public class EditActivity
    extends Action {

  private ServletContext ampContext = null;

  private static Logger logger = Logger.getLogger(EditActivity.class);

  @SuppressWarnings("unchecked")
public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {


	HttpSession session = request.getSession();

    TeamMember tm = (TeamMember) session.getAttribute("currentMember");
    
    //added in tanzania
    AmpTeam currentTeam = null;
    if(tm != null)
    	currentTeam=TeamUtil.getAmpTeam(tm.getTeamId());
    boolean isPreview=mapping.getPath().trim().endsWith("viewActivityPreview");
    String langCode = RequestUtils.getNavigationLanguage(request).getCode();
    
    

    AmpActivityVersion activity = null;
    String computeTotals = FeaturesUtil.getGlobalSettingValue(Constants.
        GLOBALSETTINGS_COMPUTE_TOTALS);

    boolean debug = (request.getParameter("debug")!=null)?true:false;

    //if("true".compareTo(request.getParameter("public"))!=0)
    //return mapping.findForward("forward");

    ActionMessages errors = new ActionMessages();

    ampContext = getServlet().getServletContext();

    // if user is not logged in, forward him to the home page
    if (session.getAttribute("currentMember") == null &&
        request.getParameter("edit") != null)
      if ("true".compareTo(request.getParameter("public")) != 0)
        return mapping.findForward("index");

    boolean isPublicView = (request.getParameter("public")==null)?false:request.getParameter("public").equals("true");
    EditActivityForm eaForm = (EditActivityForm) form; // form bean instance
    Long activityId = null;
    activityId = eaForm.getActivityId();
    Long actIdParam = null;
    if(request.getParameter("ampActivityId")!=null) actIdParam = new Long(request.getParameter("ampActivityId"));
    if(actIdParam != null && actIdParam !=0L ) 
    	activityId=actIdParam;
    
    String resetMessages = request.getParameter("resetMessages");
    if(resetMessages != null && resetMessages.equals("true")) {
    	if(eaForm.getMessages() != null) {
    		eaForm.getMessages().clear();
        }
    }
    
    // set Globam Settings Multi-Sector Selecting
   /* String multiSectorSelect = FeaturesUtil.getGlobalSettingValue(Constants.
    		GLOBALSETTINGS_MULTISECTORSELECT);
    eaForm.setMultiSectorSelecting(multiSectorSelect);
    */
    //
    String errorMsgKey = "";

    //gateperm checks are non mandatory, this means that an user still has permissions to edit an activity
    //those permissions can come from someplace else
    boolean gatePermEditAllowed = false;
    Session hsession = null;
    
    try {
    	
    hsession=PersistenceManager.getSession();
    
    if (activityId != null) {
        activity = (AmpActivityVersion) hsession.load(AmpActivityVersion.class, activityId);
        eaForm.getIdentification().setWasDraft(activity.isCreatedAsDraft());
        if(activity!=null)
        {
            boolean hasTeamLead = true;
            if (currentTeam != null) {
                AmpTeamMember teamHead = TeamMemberUtil.getTeamHead(currentTeam.getAmpTeamId());
                if (teamHead == null) {
                    hasTeamLead = false;
                }
            }
            eaForm.setWarningMessges(new ArrayList<String>());
            if (activity.getDraft() != null && activity.getDraft()) {
                eaForm.getWarningMessges().add("This is a draft activity");
            } else {
                if (Constants.ACTIVITY_NEEDS_APPROVAL_STATUS.contains(activity.getApprovalStatus())) {
                    if (hasTeamLead) {
                        eaForm.getWarningMessges().add("The activity is awaiting approval.");
                    } else {
                        eaForm.getWarningMessges().add("This activity cannot be validated because there is no Workspace Manager.");
                    }
                }
            }
        	Map scope=new HashMap();
        	scope.put(GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
        	gatePermEditAllowed = activity.canDo(GatePermConst.Actions.EDIT, scope);
        }
    }


    //old permission checking - this will be replaced by a global gateperm stuff
    // Checking whether the user have write access to the activity

    if (!gatePermEditAllowed) {

//			if (errorMsgKey.trim().length() > 0) {
//				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
//						errorMsgKey));
//				saveErrors(request, errors);
//
//				errorMsgKey = "error.aim.editActivity.userPartOfManagementTeam";
//				String url = "/aim/viewChannelOverview.do?ampActivityId="
//						+ activityId + "&tabIndex=0";
//				RequestDispatcher rd = getServlet().getServletContext()
//						.getRequestDispatcher(url);
//				rd.forward(request, response);
//				return null;
//
//			}
			
			//TODO this for tanzania. think we should have plugable rules cos all cantries have different rules.
			if (!isPreview && activity!=null && activity.getTeam()!=null && currentTeam!=null){
				AmpTeam activityTeam=activity.getTeam();
				//if user is member of same team to which activity belongs then it can be edited
				if (currentTeam.getComputation() != null && currentTeam.getComputation()){
					if (!currentTeam.getAmpTeamId().equals(activityTeam.getAmpTeamId())){
						errorMsgKey="error.aim.editActivity.noWritePermissionForUser";
					}
				}
			}

			if (errorMsgKey.trim().length() > 0 && !isPreview) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						errorMsgKey));
				saveErrors(request, errors);
				String url = "/aim/viewChannelOverview.do?ampActivityId="
						+ activityId + "&tabIndex=0";
				RequestDispatcher rd = getServlet().getServletContext()
						.getRequestDispatcher(url);
				rd.forward(request, response);
				return null;
			}
		}
    Collection euActs = EUActivityUtil.getEUActivities(activityId);
	request.setAttribute("costs", euActs);
	
    // load all themes
    Collection themes = new ArrayList(ProgramUtil.getAllThemes());
    eaForm.getPrograms().setProgramCollection(themes);

    try {

      // Checking whether the activity is already opened for editing
      // by some other user
      eaForm.setReset(true);
      eaForm.getLocation().setLocationReset(true);
      eaForm.getPhisycalProgress().setPhyProgReset(true);
      eaForm.getDocuments().setDocReset(true);
      eaForm.getComponents().setComponentReset(true);
      eaForm.reset(mapping, request);

      eaForm.setActivityId(activityId);
      HashMap activityMap = (HashMap) ampContext
          .getAttribute(Constants.EDIT_ACT_LIST);

      boolean canEdit = true;
      /*
       * modified by Govind
       */
      String step = request.getParameter("step");

      eaForm.setActivityId(activityId);
      eaForm.setReset(false);

      ProposedProjCost propProjCost = null;
      if (eaForm.getFunding().getProProjCost() != null) {
        propProjCost = new ProposedProjCost();
        propProjCost = eaForm.getFunding().getProProjCost();
        if (propProjCost.getCurrencyCode() == null &&
            propProjCost.getFunAmount() == null &&
            propProjCost.getFunDate() == null) {
          eaForm.getFunding().setProProjCost(null);
        }
      }
      List nationalPlanObjectivePrograms=new ArrayList();
      List primaryPrograms=new ArrayList();
      List secondaryPrograms=new ArrayList();
      eaForm.getPrograms().setNationalPlanObjectivePrograms(nationalPlanObjectivePrograms);
      eaForm.getPrograms().setPrimaryPrograms(primaryPrograms);
      eaForm.getPrograms().setSecondaryPrograms(secondaryPrograms);

      if (tm != null && tm.getAppSettings() != null && tm.getAppSettings()
          .getCurrencyId() != null) {
              String currCode="";
              AmpCurrency curr=CurrencyUtil.
                  getAmpcurrency(
                      tm.getAppSettings()
                      .getCurrencyId());
              if(curr!=null){
                      currCode = curr.getCurrencyCode();
              }
              eaForm.setCurrCode(currCode);
              if(eaForm.getFundingCurrCode()==null){
              eaForm.setFundingCurrCode(currCode);
              }
          if (eaForm.getRegFundingPageCurrCode() == null) {
              eaForm.setRegFundingPageCurrCode(currCode);
          }
      }

      /*List prLst = new ArrayList();
      if (eaForm.getActPrograms() == null) {
        eaForm.setActPrograms(prLst);
      }
      else {
        prLst = eaForm.getActPrograms();
        prLst.clear();
        eaForm.setActPrograms(prLst);
      }*/

   
       if(eaForm.getSteps()==null){
            List steps = ActivityUtil.getSteps();
            eaForm.setSteps(steps);
       }
        

      // checking its the activity is already opened for editing...
      if (activityMap != null && activityMap.containsValue(activityId)) {
        //logger.info("activity is in activityMap " + activityId);
        // The activity is already opened for editing
        synchronized (ampContext) {
          HashMap tsaMap = (HashMap) ampContext
              .getAttribute(Constants.TS_ACT_LIST);
          if (tsaMap != null) {
            Long timeStamp = (Long) tsaMap.get(activityId);
            if (timeStamp != null) {

              if ( (System.currentTimeMillis() - timeStamp
                    .longValue()) > Constants.MAX_TIME_LIMIT) {
                // time limit has execeeded. invalidate the activity references
                tsaMap.remove(activityId);
                HashMap userActList = (HashMap) ampContext
                    .getAttribute(Constants.USER_ACT_LIST);
                Iterator itr = userActList.keySet().iterator();
                while (itr.hasNext()) {
                  Long userId = (Long) itr.next();
                  Long actId = (Long) userActList.get(userId);
                  if (actId.longValue() == activityId
                      .longValue()) {
                    userActList.remove(userId);
                    break;
                  }
                }
                itr = activityMap.keySet().iterator();
                String sessId = null;
                while (itr.hasNext()) {
                  sessId = (String) itr.next();
                  Long actId = (Long) activityMap.get(sessId);
                  if (actId.longValue() ==
                      activityId.longValue()) {
                    activityMap.remove(sessId);
                    break;
                  }
                }
                ArrayList sessList = (ArrayList) ampContext
                    .getAttribute(Constants.SESSION_LIST);
                sessList.remove(sessId);
                Collections.sort(sessList);

                ampContext.setAttribute(Constants.EDIT_ACT_LIST,
                                        activityMap);
                ampContext.setAttribute(Constants.USER_ACT_LIST,
                                        userActList);
                ampContext.setAttribute(Constants.SESSION_LIST,
                                        sessList);
                ampContext.setAttribute(Constants.TS_ACT_LIST,
                                        tsaMap);

              }
              else
                canEdit = false;
            }
            else
              canEdit = false;
          }
          else
            canEdit = false;
        }
      }

      //logger.info("CanEdit = " + canEdit);
      //AMP-3461 When an activity is open by a user, an other user should be able to preview it
      if (!canEdit && !isPreview) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
            "error.aim.activityAlreadyOpenedForEdit"));
        saveErrors(request, errors);

        String url = "/aim/viewChannelOverview.do?ampActivityId="
            + activityId + "&tabIndex=0";
        RequestDispatcher rd = getServlet().getServletContext()
            .getRequestDispatcher(url);
        rd.forward(request, response);
        return null;
      }
      else {
        // logger.info("Path = " + mapping.getPath());
        if (!mapping.getPath().trim().endsWith("viewActivityPreview")) {
          // Edit the activity
          //logger.info("mapping does not end with viewActivityPreview.do");
          String sessId = session.getId();
          synchronized (ampContext) {
            ArrayList sessList = (ArrayList) ampContext.
                getAttribute(Constants.SESSION_LIST);
            HashMap userActList = (HashMap) ampContext.getAttribute(
                Constants.USER_ACT_LIST);

            HashMap tsaList = (HashMap) ampContext.getAttribute(
                Constants.TS_ACT_LIST);

            if (sessList == null) {
              sessList = new ArrayList();
            }
            if (userActList == null) {
              userActList = new HashMap();
            }
            if (activityMap == null) {
              activityMap = new HashMap();
            }
            if (tsaList == null) {
              tsaList = new HashMap();
            }

            sessList.add(sessId);
            Collections.sort(sessList);
            activityMap.put(sessId, activityId);
            if (tm != null)
              userActList.put(tm.getMemberId(), activityId);
            tsaList.put(activityId,
                        new Long(System.currentTimeMillis()));

            ampContext.setAttribute(Constants.SESSION_LIST,
                                    sessList);
            ampContext.setAttribute(Constants.EDIT_ACT_LIST,
                                    activityMap);
            ampContext.setAttribute(Constants.USER_ACT_LIST,
                                    userActList);
            ampContext.setAttribute(Constants.TS_ACT_LIST, tsaList);
          }
          eaForm.setEditAct(true);
        }
        else {
            String showOnlyAct=request.getParameter("showOnlyAct"); // to show activity preview without next, previous links
            boolean withoutNextPrevLink=Boolean.parseBoolean(showOnlyAct);
        	   if (session.getAttribute("report") != null && !withoutNextPrevLink) {
                GroupReportData r = (GroupReportData) session.getAttribute("report");
                TreeSet l = (TreeSet) r.getOwnerIds();
                Iterator i = l.iterator();
                Long prev = null, next = null;
                while (i.hasNext()) {
                    Long e = (Long) i.next();
                    if (e.compareTo(activityId) == 0) {
                        break;
                    } else {
                        prev = e;
                    }
                }
                if (i.hasNext()) {
                    next = (Long) i.next();
                }
                session.setAttribute("previousActivity", prev);
                session.setAttribute("nextActivity", next);
                request.setAttribute(Constants.ONLY_PREVIEW, "true");
                logger.info("mapping does end with viewActivityPreview.do");
            } else {
                session.removeAttribute("previousActivity");
                session.removeAttribute("nextActivity");
            }
        }
      }


      logger.debug("step [before IF] : " + eaForm.getStep());

      if (step != null) {
    	  eaForm.setStep(step);
      }	else{
    	  eaForm.setStep("1");
      }

      eaForm.setReset(false);

      if (activityId != null) {
    	  /* Clearing Tanzania Adds */
    	  eaForm.getIdentification().setVote(null);
    	  eaForm.getIdentification().setSubVote(null);
    	  eaForm.getIdentification().setFY(null);
    	  eaForm.getIdentification().setSelectedFYs(null);
    	  eaForm.getIdentification().setSubProgram(null);
    	  eaForm.getIdentification().setProjectCode(null);
    	  eaForm.getIdentification().setGbsSbs(null);
    	  eaForm.getIdentification().setGovernmentApprovalProcedures(null);
    	  eaForm.getIdentification().setJointCriteria(null);
    	  /* END - Clearing Tanzania Adds */

    	  eaForm.getIdentification().setCrisNumber(null);
        /* Insert Categories */
        AmpCategoryValue ampCategoryValue = CategoryManagerUtil.
            getAmpCategoryValueFromList(CategoryConstants.ACCHAPTER_NAME,activity.getCategories());
        
        if (ampCategoryValue != null)
        	eaForm.getIdentification().setAcChapter(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.PROCUREMENT_SYSTEM_NAME, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setProcurementSystem(new Long(ampCategoryValue.getId()));

    	ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.REPORTING_SYSTEM_NAME, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setReportingSystem(new Long(ampCategoryValue.getId()));

    	ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.AUDIT_SYSTEM_NAME, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setAuditSystem(new Long(ampCategoryValue.getId()));

    	ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.INSTITUTIONS_NAME, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setInstitutions(new Long(ampCategoryValue.getId()));
        
    	ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.ACCESSION_INSTRUMENT_NAME, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setAccessionInstrument(new Long(ampCategoryValue.getId()));
    
        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            CategoryConstants.ACTIVITY_STATUS_KEY, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getIdentification().setStatusId(new Long(ampCategoryValue.getId()));
        
        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.PROJECT_IMPLEMENTING_UNIT_KEY, activity.getCategories());
        if (ampCategoryValue != null){
        	eaForm.getIdentification().setProjectImplUnitId(new Long(ampCategoryValue.getId()));
        }

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            CategoryConstants.IMPLEMENTATION_LEVEL_KEY, activity.getCategories());
        if (ampCategoryValue != null)
          eaForm.getLocation().setLevelId(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryConstants.ACTIVITY_LEVEL_KEY, activity.getCategories());
            if (ampCategoryValue != null)
              eaForm.getIdentification().setActivityLevel(new Long(ampCategoryValue.getId()));


        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryConstants.FINANCIAL_INSTRUMENT_KEY, activity.getCategories());
            if (ampCategoryValue != null)
              eaForm.getIdentification().setGbsSbs(new Long(ampCategoryValue.getId()));

        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                CategoryConstants.IMPLEMENTATION_LOCATION_KEY, activity.getCategories());
            if (ampCategoryValue != null)
              eaForm.getLocation().setImplemLocationLevel(new Long(ampCategoryValue.getId()));
            
        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                    CategoryConstants.PROJECT_CATEGORY_KEY, activity.getCategories());
            if (ampCategoryValue != null)
                  eaForm.getIdentification().setProjectCategory(new Long(ampCategoryValue.getId()));
            
        ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                    CategoryConstants.ACTIVITY_BUDGET_KEY, activity.getCategories());
            if (ampCategoryValue != null)
                  eaForm.getIdentification().setBudgetCV(new Long(ampCategoryValue.getId()));
            else
            	 eaForm.getIdentification().setBudgetCV(0L);

            

        /* End - Insert Categories */ 

        /* Injecting documents into session */
        SelectDocumentDM.clearContentRepositoryHashMap(request);
        
        //Added because of a problem with the save as draft and redirect.
        try {
        	//this does not work, throws java.lang.IllegalStateException: No modifications are allowed to a locked ParameterMap
        	//request.getParameterMap().put("viewAllRights", "true");
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		
        if (activity.getActivityDocuments() != null && activity.getActivityDocuments().size() > 0 )
        		ActivityDocumentsUtil.injectActivityDocuments(request, activity.getActivityDocuments());
        
        eaForm.getDocuments().setCrDocuments( DocumentManagerUtil.createDocumentDataCollectionFromSession(request) );
        /* END - Injecting documents into session */

        DocumentManagerUtil.logoutJcrSessions(request.getSession());
        /* Clearing session information about comments */
        String action = request.getParameter("action");
        if (action != null && action.trim().length() != 0) {
          if ("edit".equals(action)) {
        	if (eaForm.getComments().getCommentsCol() != null)
        			eaForm.getComments().getCommentsCol().clear();
        	else
        		eaForm.getComments().setCommentsCol(new ArrayList<AmpComments>());
            eaForm.getComments().setCommentFlag(false);
            /**
             * The commentColInSession session attribute is a map of lists.
             * Each list contains the AmpComments for a specific field. So the keys are the fields' ids.
             * It isn't really needed anymore except for compatibility with previewLogframe which
             * still uses this map.
             */
            HashMap<Long, List<AmpComments>> commentColInSession	= (HashMap)request.getSession().getAttribute("commentColInSession");
            if ( commentColInSession != null ) {
            	commentColInSession.clear();
            }
            else {
            	commentColInSession		= new HashMap<Long, List<AmpComments>>();
            	request.getSession().setAttribute("commentColInSession", commentColInSession );
            }
            AmpComments.populateWithComments( eaForm.getComments().getCommentsCol(), commentColInSession,
            					activityId);
          }
        }
        /* END - Clearing session information about comments */

        // load the activity details
        String actApprovalStatus = DbUtil.getActivityApprovalStatus(activityId);
       // HttpSession session = request.getSession();
        
        //eaForm.setApprovalStatus(actApprovalStatus);
        if (tm != null && tm.getTeamId()!=null && activity.getTeam() != null && activity.getTeam().getAmpTeamId() != null) {
					if (("true".compareTo((String) session
							.getAttribute("teamLeadFlag")) == 0 || tm
							.isApprover())
							&& tm.getTeamId().equals(
									activity.getTeam().getAmpTeamId()) ){
              AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMemberCached(tm.getMemberId());
			  eaForm.getIdentification().setApprovedBy(teamMember);
			  eaForm.getIdentification().setApprovalDate(new Date());
			  //eaForm.getIdentification().setApprovalStatus(Constants.APPROVED_STATUS);
			  eaForm.getIdentification().setApprovalStatus(actApprovalStatus);
			  }

            else{
              //eaForm.setApprovalStatus(Constants.STARTED_STATUS);//actApprovalStatus);
            	eaForm.getIdentification().setApprovalStatus(Constants.EDITED_STATUS);
            }
        }

        if (activity != null) {
          // set title,description and objective

          ProposedProjCost pg = new ProposedProjCost();
          if (activity.getFunAmount() != null)
            pg.setFunAmountAsDouble(activity.getFunAmount());
          pg.setCurrencyCode(activity.getCurrencyCode());
          pg.setFunDate(FormatHelper.formatDate(activity.getFunDate()));
          eaForm.getFunding().setProProjCost(pg);

          //load programs by type
          if(ProgramUtil.getAmpActivityProgramSettingsList()!=null){
                       List activityNPO=ActivityUtil.getActivityProgramsByProgramType(activityId,ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
                       List activityPP=ActivityUtil.getActivityProgramsByProgramType(activityId,ProgramUtil.PRIMARY_PROGRAM);
                       List activitySP=ActivityUtil.getActivityProgramsByProgramType(activityId,ProgramUtil.SECONDARY_PROGRAM);
                       eaForm.getPrograms().setNationalPlanObjectivePrograms(activityNPO);
                       eaForm.getPrograms().setPrimaryPrograms(activityPP);
                       eaForm.getPrograms().setSecondaryPrograms(activitySP);
                       eaForm.getPrograms().setNationalSetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE));
                       eaForm.getPrograms().setPrimarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM));
                       eaForm.getPrograms().setSecondarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM));
            }


         /* try {
            List actPrgs = new ArrayList();
            Set prgSet = activity.getActivityPrograms();
            if (prgSet != null) {
              Iterator prgItr = prgSet.iterator();
              while (prgItr.hasNext()) {
                AmpTheme prg = (AmpTheme) prgItr.next();
                String newName = ProgramUtil.getHierarchyName(prg);
                prg.setProgramviewname(newName);
                actPrgs.add(prg);
                 }
            }

            eaForm.setActPrograms(actPrgs);
          }

          catch (Exception ex) {
            ex.printStackTrace();
          }
          */
          eaForm.getIdentification().setTitle(activity.getName().trim());
          eaForm.getCosting().setCosts(new ArrayList(activity.getCosts()));
          eaForm.getIdentification().setTeam(activity.getTeam());
          eaForm.getIdentification().setCreatedBy(activity.getActivityCreator());
          eaForm.getIdentification().setUpdatedBy(activity.getUpdatedBy());
         // eaForm.getIdentification().setBudget(activity.getBudget());
          AmpCategoryValue budgetOff =  CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ACTIVITY_BUDGET_OFF);	
          eaForm.getIdentification().setBudgetCVOff(
        		  (budgetOff==null)?0:budgetOff.getId()
        		  );
          AmpCategoryValue budgetOn =  CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ACTIVITY_BUDGET_ON);	
          eaForm.getIdentification().setBudgetCVOn(
        		  (budgetOn==null)?1:budgetOn.getId()
        		  );
          
          eaForm.getIdentification().setHumanitarianAid(activity.getHumanitarianAid());
          eaForm.getIdentification().setGovAgreementNumber(activity.getGovAgreementNumber());
          
          if(activity.getBudgetCodeProjectID()!=null)
        	  eaForm.getIdentification().setBudgetCodeProjectID(activity.getBudgetCodeProjectID().trim());
          
          eaForm.getIdentification().setBudgetCodes(ActivityUtil.getBudgetCodes());
          
        //Budget classification
      	eaForm.getIdentification().setSelectedbudgedsector(ActivityUtil.getBudgetSector(eaForm.getActivityId()));
      	eaForm.getIdentification().setSelectedorg(ActivityUtil.getBudgetOrganization(eaForm.getActivityId()));
      	eaForm.getIdentification().setSelecteddepartment(ActivityUtil.getBudgetDepartment(eaForm.getActivityId()));
      	eaForm.getIdentification().setSelectedprogram(ActivityUtil.getBudgetProgram(eaForm.getActivityId()));
      	
      	
      	eaForm.getIdentification().setBudgetsectors(BudgetDbUtil.getBudgetSectors());
      	eaForm.getIdentification().setBudgetprograms(BudgetDbUtil.getBudgetPrograms());
      	if (eaForm.getIdentification().getSelectedbudgedsector()!=null){
      		eaForm.getIdentification().setBudgetorgs(
      			new ArrayList<AmpOrganisation>(BudgetDbUtil.getOrganizationsBySector(eaForm.getIdentification().getSelectedbudgedsector())));
      	}else{
      		eaForm.getIdentification().setBudgetorgs(new ArrayList<AmpOrganisation>());
      	}
      	if (eaForm.getIdentification().getSelectedorg()!=null){
      		eaForm.getIdentification().setBudgetdepartments(
      			new ArrayList<AmpDepartments>(BudgetDbUtil.getDepartmentsbyOrg(eaForm.getIdentification().getSelectedorg())));
      	}else{
      		eaForm.getIdentification().setBudgetdepartments(new ArrayList<AmpDepartments>());
      	}
          
          /*
           * Tanzania adds
           */
          if (activity.getFY() != null) {
        	  String fy =activity.getFY().trim();
        	  eaForm.getIdentification().setFY(fy);
        	  String[] years = fy.split(",");
        	  eaForm.getIdentification().setSelectedFYs(years);
        	  
          }
            
          
          if (activity.getVote() != null)
            eaForm.getIdentification().setVote(activity.getVote().trim());
          if (activity.getSubVote() != null)
            eaForm.getIdentification().setSubVote(activity.getSubVote().trim());
          if (activity.getSubProgram() != null)
            eaForm.getIdentification().setSubProgram(activity.getSubProgram().trim());
          if (activity.getProjectCode() != null)
            eaForm.getIdentification().setProjectCode(activity.getProjectCode().trim());

          if (activity.getGbsSbs() != null)
            eaForm.getIdentification().setGbsSbs(activity.getGbsSbs());

          if (activity.isGovernmentApprovalProcedures() != null)
            eaForm.getIdentification().setGovernmentApprovalProcedures(activity.isGovernmentApprovalProcedures());
          else
            eaForm.getIdentification().setGovernmentApprovalProcedures(null);

          if (activity.isJointCriteria() != null)
            eaForm.getIdentification().setJointCriteria(activity.isJointCriteria());
          else
             eaForm.getIdentification().setJointCriteria(null);
          
          if (activity.isHumanitarianAid() != null)
        	  eaForm.getIdentification().setHumanitarianAid(activity.isHumanitarianAid());
          else
        	  activity.setHumanitarianAid(new Boolean(false));


          if (activity.getCrisNumber() != null)
              eaForm.getIdentification().setCrisNumber(activity.getCrisNumber().trim());

          
          if (activity.getDescription() != null)
            eaForm.getIdentification().setDescription(activity.getDescription().trim());

          if (activity.getEqualOpportunity() != null)
              eaForm.getCrossIssues().setEqualOpportunity(activity.getEqualOpportunity().trim());
          
          if (activity.getEnvironment() != null)
              eaForm.getCrossIssues().setEnvironment(activity.getEnvironment().trim());
         
          if (activity.getMinorities() != null)
              eaForm.getCrossIssues().setMinorities(activity.getMinorities().trim());


          if (activity.getLessonsLearned()!=null)
        	  eaForm.getIdentification().setLessonsLearned(activity.getLessonsLearned().trim());
          
      	eaForm.getIdentification().setProjectImpact(activity.getProjectImpact());
      	
      	if(activity.getChapter()!=null) {
      		eaForm.getIdentification().setChapterCode(activity.getChapter().getCode());
      		eaForm.getIdentification().setChapterYear(activity.getChapter().getYear());
      		eaForm.getIdentification().setChapterForPreview(activity.getChapter());
      		logger.info("GETTING CHAPTER AND IMPUTATIONS : "+activity.getChapter().getCode());
      		logger.info("GETTING CHAPTER AND IMPUTATIONS : "+activity.getChapter().getImputations().size());
      		//
//      		Iterator it = activity.getChapter().getImputations().iterator();
//      		while (it.hasNext()) {
//      			
//      		}
      	}
        
    	eaForm.getIdentification().setActivitySummary(activity.getActivitySummary());

  
    	eaForm.getIdentification().setContractingArrangements(activity.getContractingArrangements());

  
    	eaForm.getIdentification().setCondSeq(activity.getCondSeq());

  
    	eaForm.getIdentification().setLinkedActivities(activity.getLinkedActivities());

  
    	eaForm.getIdentification().setConditionality(activity.getConditionality());

  
    	eaForm.getIdentification().setProjectManagement(activity.getProjectManagement());
  
  
    	eaForm.getContracts().setContractDetails(activity.getContractDetails());
  	

  		eaForm.getIdentification().setConvenioNumcont(activity.getConvenioNumcont());
  		eaForm.getIdentification().setClasiNPD(activity.getClasiNPD());
        
  		if (activity.getProjectComments() != null)
            eaForm.getIdentification().setProjectComments(activity.getProjectComments().trim());

  		if (activity.getObjective() != null)
            eaForm.getIdentification().setObjectives(activity.getObjective().trim());
          
          if (activity.getPurpose() != null)
            eaForm.getIdentification().setPurpose(activity.getPurpose().trim());
          
          if (activity.getResults() != null)
            eaForm.getIdentification().setResults(activity.getResults());
          
          if (activity.getDocumentSpace() == null ||
              activity.getDocumentSpace().trim().length() == 0) {
            if (tm != null && DocumentUtil.isDMEnabled()) {
              eaForm.getDocuments().setDocumentSpace("aim-document-space-" +
                                      tm.getMemberId() +
                                      "-" + System.currentTimeMillis());
              Site currentSite = RequestUtils.getSite(request);
              DocumentUtil.createDocumentSpace(currentSite,
                                               eaForm.getDocuments().getDocumentSpace());
            }
          }
          else {
            eaForm.getDocuments().setDocumentSpace(activity.getDocumentSpace().
                                    trim());
          }
          eaForm.getIdentification().setAmpId(activity.getAmpId());
          Editor reason=org.digijava.module.editor.util.DbUtil.getEditor(activity.getStatusReason(),langCode);
          if(reason!=null){
            eaForm.getIdentification().setStatusReason(reason.getBody());
          }

          if (null != activity.getLineMinRank())
            eaForm.getPlanning().setLineMinRank(activity.getLineMinRank().
                                  toString());
          else
            eaForm.getPlanning().setLineMinRank("-1");
          if (null != activity.getPlanMinRank())
            eaForm.getPlanning().setPlanMinRank(activity.getPlanMinRank().
                                  toString());
          else
            eaForm.getPlanning().setPlanMinRank("-1");
          
          eaForm.getPlanning().setActRankCollection(new ArrayList());
          for(int i = 1; i < 6; i++) {
            eaForm.getPlanning().getActRankCollection().add(new Integer(i));
          }

          eaForm.getIdentification().setCreatedDate(DateConversion
                                .ConvertDateToString(activity.
              getCreatedDate()));
          eaForm.getIdentification().setUpdatedDate(DateConversion
                                .ConvertDateToString(activity.
              getUpdatedDate()));
          eaForm.getPlanning().setOriginalAppDate(DateConversion
                                    .ConvertDateToString(activity
              .getProposedApprovalDate()));
          eaForm.getPlanning().setRevisedAppDate(DateConversion
                                   .ConvertDateToString(activity
              .getActualApprovalDate()));
          eaForm.getPlanning().setOriginalStartDate(DateConversion
                                      .ConvertDateToString(activity
              .getProposedStartDate()));
          eaForm.getPlanning().setRevisedStartDate(DateConversion
                                   .ConvertDateToString(activity
              .getActualStartDate()));
          eaForm.getPlanning().setContractingDate(DateConversion
                                    .ConvertDateToString(activity.
              getContractingDate()));
          eaForm.getPlanning().setDisbursementsDate(DateConversion
                                      .ConvertDateToString(activity.
              getDisbursmentsDate()));
          eaForm.getPlanning().setProposedCompDate(DateConversion
                                     .ConvertDateToString(activity.
              getOriginalCompDate()));

          eaForm.getPlanning().setCurrentCompDate(DateConversion
                                    .ConvertDateToString(activity
              .getActualCompletionDate()));

          /*eaForm.getPlanning().setProposedCompDate(DateConversion.ConvertDateToString(
              activity.getProposedCompletionDate()));*/

          // loading organizations and thier project ids.
          Set orgProjIdsSet = activity.getInternalIds();
          if (orgProjIdsSet != null) {
            Iterator projIdItr = orgProjIdsSet.iterator();
            Collection temp = new ArrayList();
            while (projIdItr.hasNext()) {
              AmpActivityInternalId actIntId = (
                  AmpActivityInternalId) projIdItr
                  .next();
              OrgProjectId projId = new OrgProjectId();
              projId.setId(actIntId.getId());
              projId.setOrganisation(actIntId.getOrganisation());
              projId.setProjectId(actIntId.getInternalId());
              temp.add(projId);
            }
            if (temp != null && temp.size() > 0) {
              OrgProjectId orgProjectIds[] = new OrgProjectId[
                  temp
                  .size()];
              Object arr[] = temp.toArray();
              for (int i = 0; i < arr.length; i++) {
                orgProjectIds[i] = (OrgProjectId) arr[i];
              }
              eaForm.getIdentification().setSelectedOrganizations(orgProjectIds);
            }
          }

          // loading the locations
          int impLevel = 0;

          Collection ampLocs = activity.getLocations();

          if (ampLocs != null && ampLocs.size() > 0) {
           List<Location> locs = new ArrayList<Location>();

            Iterator locIter = ampLocs.iterator();
            boolean maxLevel = false;
            
            String cIso                                                         = FeaturesUtil.getDefaultCountryIso();
            AmpCategoryValueLocations defCountry    = DynLocationManagerUtil.getLocationByIso(cIso, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
            AmpCategoryValue implLevel                              = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            		CategoryConstants.IMPLEMENTATION_LEVEL_KEY, activity.getCategories());
            AmpCategoryValue implLocValue                   = CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            		CategoryConstants.IMPLEMENTATION_LOCATION_KEY, activity.getCategories());
            boolean setFullPercForDefaultCountry    = false;
            if ( !"true".equals( FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALLOW_PERCENTAGES_FOR_ALL_COUNTRIES ) ) &&
            		implLevel!=null && implLocValue!=null &&
            		CategoryManagerUtil.equalsCategoryValue(implLevel, CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL) &&
            		CategoryManagerUtil.equalsCategoryValue(implLocValue, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY)
            ) {
            	setFullPercForDefaultCountry            = true;
            }

            while (locIter.hasNext()) {
            	AmpActivityLocation actLoc = (AmpActivityLocation) locIter.next();	//AMP-2250
            	if (actLoc == null)
            		continue;
            	AmpLocation loc=actLoc.getLocation();								//AMP-2250
//              if (!maxLevel) {
//                if (loc.getAmpWoreda() != null) {
//                  impLevel = 3;
//                  maxLevel = true;
//                }
//                else if (loc.getAmpZone() != null
//                         && impLevel < 2) {
//                  impLevel = 2;
//                }
//                else if (loc.getAmpRegion() != null
//                         && impLevel < 1) {
//                  impLevel = 1;
//                }
//              }

              if (loc != null) {
                Location location = new Location();
                location.setLocId(loc.getAmpLocationId());
                location.setLat(loc.getLocation().getGsLat());
                location.setLon(loc.getLocation().getGsLong());
                
                logger.info(" this is the settings Value" + cIso);
                Country cntry = DbUtil.getDgCountry(cIso);
                location.setCountryId(cntry.getCountryId());
                location.setCountry(cntry.getCountryName());
                location.setNewCountryId(cntry.getIso());
                
                location.setAmpCVLocation( loc.getLocation() );
                if ( loc.getLocation() != null ){
	                location.setAncestorLocationNames( DynLocationManagerUtil.getParents( loc.getLocation()) );
					location.setLocationName(loc.getLocation().getName());
					location.setLocId( loc.getLocation().getId() );
                }
                AmpCategoryValueLocations ampCVRegion	= 
        			DynLocationManagerUtil.getAncestorByLayer(loc.getLocation(), CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
        		
        		if ( ampCVRegion != null ) {
//                if (loc.getAmpRegion() != null) {
//                  location.setRegion(loc.getAmpRegion()
//                                     .getName());
//                  location.setRegionId(loc.getAmpRegion()
//                                       .getAmpRegionId());
                  if (eaForm.getFunding().getFundingRegions() == null) {
                    eaForm.getFunding()
                        .setFundingRegions(new ArrayList());
                  }
                  if (!eaForm.getFunding().getFundingRegions().contains(ampCVRegion) ) {
                    eaForm.getFunding().getFundingRegions().add( ampCVRegion );
                  }
                }
//                if (loc.getAmpZone() != null) {
//                  location
//                      .setZone(loc.getAmpZone().getName());
//                  location.setZoneId(loc.getAmpZone()
//                                     .getAmpZoneId());
//                }
//                if (loc.getAmpWoreda() != null) {
//                  location.setWoreda(loc.getAmpWoreda()
//                                     .getName());
//                  location.setWoredaId(loc.getAmpWoreda()
//                                       .getAmpWoredaId());
//                }

                if(actLoc.getLocationPercentage()!=null){
                	String strPercentage	= FormatHelper.formatNumberNotRounded((double)actLoc.getLocationPercentage() );
                	//TODO Check the right why to show numbers in percentages, here it calls formatNumberNotRounded but so the format
                	//depends on global settings which is not correct
                	
                	location.setPercent( strPercentage.replace(",", ".") );
                }
                
                if ( setFullPercForDefaultCountry && actLoc.getLocationPercentage() == 0.0 &&
                		CategoryManagerUtil.equalsCategoryValue(loc.getLocation().getParentCategoryValue(),
                				CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY) &&
                				loc.getLocation().getId() != defCountry.getId() )
                {
                	location.setPercentageBlocked(true);
                }

                locs.add(location);
              }
            }
        
              Collections.sort(locs, new HelperLocationAncestorLocationNamesAsc(langCode));
              eaForm.getLocation().setSelectedLocs(locs);
          }

       
          eaForm.getDocuments().setAllReferenceDocNameIds(null);
          eaForm.getDocuments().setReferenceDocs(null);

          eaForm=setSectorsToForm(eaForm, activity);

          if (activity.getThemeId() != null) {
            eaForm.getPrograms().setProgram(activity.getThemeId().getAmpThemeId());
          }
          if (activity.getProgramDescription() != null)
        	  eaForm.getPrograms().setProgramDescription(activity
                                       .getProgramDescription().trim());

          
          
          FundingCalculationsHelper calculations=new FundingCalculationsHelper();    
          String toCurrCode=null;
          if (tm != null)
              toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
          calculations.setDebug(debug);

                    ArrayList fundingOrgs = new ArrayList();
                    Iterator fundItr = activity.getFunding().iterator();
                    while(fundItr.hasNext()) {
                        AmpFunding ampFunding = (AmpFunding) fundItr.next();
                        AmpOrganisation org = ampFunding.getAmpDonorOrgId();
                        if(org == null || org.getAmpOrgId()==null) continue;
                        FundingOrganization fundOrg = new FundingOrganization();
                        fundOrg.setAmpOrgId(org.getAmpOrgId());
                        fundOrg.setOrgName(org.getName());
                        fundOrg.setFundingorgid(org.getFundingorgid());
                        fundOrg.setFundingActive(ampFunding.getActive());
                        fundOrg.setDelegatedCooperation(ampFunding.getDelegatedCooperation());
                        fundOrg.setDelegatedPartner(ampFunding.getDelegatedPartner());

                        if ( fundOrg.getDelegatedCooperation()!=null && fundOrg.getDelegatedCooperation() ) {
                        	fundOrg.setDelegatedCooperationString("checked");
                        }
                        else
                        	fundOrg.setDelegatedCooperationString("unchecked");

                        if ( fundOrg.getDelegatedPartner()!=null && fundOrg.getDelegatedPartner() ) {
                        	fundOrg.setDelegatedPartnerString("checked");
                        }
                        else
                        	fundOrg.setDelegatedPartnerString("unchecked");

                        int index = fundingOrgs.indexOf(fundOrg);
                        //logger.info("Getting the index as " + index
                        //	+ " for fundorg " + fundOrg.getOrgName());
                        if(index > -1) {
                            fundOrg = (FundingOrganization) fundingOrgs
                                .get(index);
                        }


			            Funding fund = new Funding();
			            //fund.setAmpTermsAssist(ampFunding.getAmpTermsAssistId());
			            fund.setTypeOfAssistance(ampFunding.getTypeOfAssistance());
			            fund.setFinancingInstrument(ampFunding.getFinancingInstrument());
			            fund.setFundingStatus(ampFunding.getFundingStatus());
			            fund.setModeOfPayment(ampFunding.getModeOfPayment());
			            
			            fund.setActStartDate(DateConversion.ConvertDateToString(ampFunding.getActualStartDate()));
			            fund.setActCloseDate(DateConversion.ConvertDateToString(ampFunding.getActualCompletionDate()));
			            
			            fund.setFundingId(ampFunding.getAmpFundingId().
			                              longValue());
			            fund.setGroupVersionedFunding(ampFunding.getGroupVersionedFunding());
			            fund.setOrgFundingId(ampFunding.getFinancingId());
			            fund.setConditions(ampFunding.getConditions());
			            fund.setDonorObjective(ampFunding.getDonorObjective());

			            /* Get MTEF Projections */
			            ArrayList<MTEFProjection> MTEFProjections	= new ArrayList<MTEFProjection>();
			            if (ampFunding.getMtefProjections() != null) {
			            	Iterator<AmpFundingMTEFProjection> iterMtef	= ampFunding.getMtefProjections().iterator();
			            	while ( iterMtef.hasNext() ) {
				            	AmpFundingMTEFProjection ampProjection		= iterMtef.next();
				            	MTEFProjection	projection					= new MTEFProjection();

				            	projection.setAmount( FormatHelper.formatNumber(ampProjection.getAmount()) + "" );
				            	if ( ampProjection.getProjected() != null )
				            		projection.setProjected( ampProjection.getProjected().getId() );
				            	else
				            		logger.error("Projection with date " + ampProjection.getProjectionDate() + " has no type (neither projection nor pipeline) !!!!");
				            	projection.setCurrencyCode( ampProjection.getAmpCurrency().getCurrencyCode() );
				            	projection.setCurrencyName( ampProjection.getAmpCurrency().getCurrencyName() );
				            	projection.setProjectionDate( DateConversion.ConvertDateToString(ampProjection.getProjectionDate()) );
				            	//projection.setIndex();
				            	projection.setAmpFunding( ampProjection.getAmpFunding() );
				            	MTEFProjections.add(projection);
			            	}

			            }
			            Collections.sort(MTEFProjections);
			            fund.setMtefProjections(MTEFProjections);
			            /* END - Get MTEF Projections */

			            Collection fundDetails = ampFunding.getFundingDetails();

			            if (fundDetails != null && fundDetails.size() > 0) {
			            //  Iterator fundDetItr = fundDetails.iterator();
			             // long indexId = System.currentTimeMillis();
			        
			            calculations.doCalculations(fundDetails, toCurrCode);
                                        
			            List<FundingDetail> fundDetail = calculations.getFundDetailList();
			            if(isPreview){
                        Iterator fundingIterator = fundDetail.iterator();
                         while(fundingIterator.hasNext())
                         {
                         	FundingDetail currentFundingDetail = (FundingDetail)fundingIterator.next();
                         	
                         	currentFundingDetail.getContract();
                         	
                         	if(currentFundingDetail.getFixedExchangeRate() == null)
                         	{
                            	Double currencyAppliedAmount;
                            	String currencyCode;
                            	if (tm != null) {
                            		currencyCode							= CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId() ).getCurrencyCode();
                            	}
                            	else { 
                            		currencyCode							= Constants.DEFAULT_CURRENCY;
                            	}
                            	currencyAppliedAmount			= getAmountInDefaultCurrency(currentFundingDetail, currencyCode );
                            	
                            	String currentAmount = FormatHelper.formatNumber(currencyAppliedAmount);
                            	currentFundingDetail.setTransactionAmount(currentAmount);
                            	currentFundingDetail.setCurrencyCode( currencyCode );
                         	}
                         	else
                         	{
                         		String currencyCode;
                            	if (tm != null) {
                            		currencyCode							= CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId() ).getCurrencyCode();
                            	}
                            	else { 
                            		currencyCode							= Constants.DEFAULT_CURRENCY;
                            	}
                         		Double fixedExchangeRate = FormatHelper.parseDouble( currentFundingDetail.getFixedExchangeRate() );
                         		Double currencyAppliedAmount = CurrencyWorker.convert1(FormatHelper.parseDouble(currentFundingDetail.getTransactionAmount()),fixedExchangeRate,1);
                            	String currentAmount = FormatHelper.formatNumber(currencyAppliedAmount);
                            	currentFundingDetail.setTransactionAmount(currentAmount);
                            	currentFundingDetail.setCurrencyCode( currencyCode );
                         	}
                         }
		            }
			            
			              if (fundDetail != null)
			                Collections.sort(fundDetail,
			                                 FundingValidator.dateComp);
			              fund.setFundingDetails(fundDetail);
			              fund.setAmpFundingDetails(fundDetails);
			              eaForm.getFunding().setFundingDetails(fundDetail);
			              // funding.add(fund);
			            }
			            if (fundOrg.getFundings() == null)
			              fundOrg.setFundings(new ArrayList());
			            fundOrg.getFundings().add(fund);

			            if (index > -1) {
			              fundingOrgs.set(index, fundOrg);
			              //logger.info("!!!! Setting the fund org obj to the index :"	+ index);
			            }
			            else {
			              fundingOrgs.add(fundOrg);
			              //logger.info("???? Adding new fund org object");
			            }
          }
                 
          //Added for the calculation of the subtotal per Organization          
          Iterator<FundingOrganization> iterFundOrg = fundingOrgs.iterator();
          while (iterFundOrg.hasNext())
          {
        	  FundingOrganization currFundingOrganization = iterFundOrg.next();
        	  Iterator<Funding> iterFunding = currFundingOrganization.getFundings().iterator();
        	  while(iterFunding.hasNext()){
        		  Funding currFunding = iterFunding.next();
                  FundingCalculationsHelper calculationsSubtotal=new FundingCalculationsHelper();  
                  if(currFunding.getAmpFundingDetails()!=null){
	                  try{
		                  calculationsSubtotal.doCalculations(currFunding.getAmpFundingDetails(), toCurrCode);
		        		  currFunding.setSubtotalPlannedCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotPlannedComm().doubleValue()));
		        		  currFunding.setSubtotalActualCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotActualComm().doubleValue()));
		        		  currFunding.setSubtotalPipelineCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotPipelineComm().doubleValue()));
		        		  currFunding.setSubtotalPlannedDisbursements(FormatHelper.formatNumber(calculationsSubtotal.getTotPlanDisb().doubleValue()));
		        		  currFunding.setSubtotalDisbursements(FormatHelper.formatNumber(calculationsSubtotal.getTotActualDisb().doubleValue()));
		        		  currFunding.setSubtotalPlannedExpenditures(FormatHelper.formatNumber(calculationsSubtotal.getTotPlannedExp().doubleValue()));
		        		  currFunding.setSubtotalExpenditures(FormatHelper.formatNumber(calculationsSubtotal.getTotActualExp().doubleValue()));
		        		  currFunding.setSubtotalActualDisbursementsOrders(FormatHelper.formatNumber(calculationsSubtotal.getTotActualDisbOrder().doubleValue()));
		        		  currFunding.setUnDisbursementBalance(FormatHelper.formatNumber(calculationsSubtotal.getUnDisbursementsBalance().doubleValue()));
		        		  currFunding.setAmpFundingDetails(null);
		        		  //TODO:aca se setearia el resto
	                  }
	                  catch(Exception ex){
	                	  ex.printStackTrace();
	                  }
                  }
        	  }
          }

                    
          //logger.info("size = " + fundingOrgs);
          Collections.sort(fundingOrgs);
          eaForm.getFunding().setFundingOrganizations(fundingOrgs);
          //get the total depend of the 
         
          if(debug){
        	  eaForm.getFunding().setTotalCommitments(calculations.getTotalCommitments().getCalculations());
        	  eaForm.getFunding().setTotalCommitmentsDouble(calculations.getTotalCommitments()
        			  .getValue().doubleValue());
        	  
        	  eaForm.getFunding().setTotalDisbursements(calculations.getTotActualDisb().getCalculations());
        	  eaForm.getFunding().setTotalPlannedDisbursements(calculations.getTotPlanDisb().getCalculations());
        	  eaForm.getFunding().setTotalExpenditures(calculations.getTotPlannedExp().getCalculations());  
        	  eaForm.getFunding().setTotalPlannedCommitments(calculations.getTotPlannedComm().getCalculations());
        	  eaForm.getFunding().setTotalPipelineCommitments(calculations.getTotPipelineComm().getCalculations());
        	  eaForm.getFunding().setTotalPlannedExpenditures(calculations.getTotPlannedExp().getCalculations());
        	  eaForm.getFunding().setTotalActualDisbursementsOrders(calculations.getTotActualDisbOrder().getCalculations());
        	  eaForm.getFunding().setTotalPlannedDisbursementsOrders(calculations.getTotPlannedDisbOrder().getCalculations());
        	  eaForm.getFunding().setUnDisbursementsBalance(calculations.getUnDisbursementsBalance().getCalculations());
          }
          else{
              	//actual
      		  eaForm.getFunding().setTotalCommitments(calculations.getTotalCommitments().toString());
        	  eaForm.getFunding().setTotalDisbursements(calculations.getTotActualDisb().toString());
        	  eaForm.getFunding().setTotalExpenditures(calculations.getTotActualExp().toString());
        	  eaForm.getFunding().setTotalActualDisbursementsOrders(calculations.getTotActualDisbOrder().toString());
        	  //planned
        	  eaForm.getFunding().setTotalPlannedDisbursements(calculations.getTotPlanDisb().toString());
        	  eaForm.getFunding().setTotalPlannedCommitments(calculations.getTotPlannedComm().toString());
        	  eaForm.getFunding().setTotalPlannedExpenditures(calculations.getTotPlannedExp().toString());
        	  eaForm.getFunding().setTotalPlannedDisbursementsOrders(calculations.getTotPlannedDisbOrder().toString());
        	  eaForm.getFunding().setUnDisbursementsBalance(calculations.getUnDisbursementsBalance().toString());
              //pipeline
              eaForm.getFunding().setTotalPipelineCommitments(calculations.getTotPipelineComm().toString());
          }
          ArrayList regFunds = new ArrayList(); 
          Iterator rItr = activity.getRegionalFundings().iterator();

          eaForm.getFunding().setRegionTotalDisb(0);
          while (rItr.hasNext()) {
            AmpRegionalFunding ampRegFund = (AmpRegionalFunding)
                rItr
                .next();

            double disb = 0;
            if (ampRegFund.getAdjustmentType().intValue() == 1 &&
                ampRegFund.getTransactionType().intValue() == 1)
              disb = ampRegFund.getTransactionAmount().
                  doubleValue();
            //if(!ampCompFund.getCurrency().getCurrencyCode().equals("USD")) {
            //double toRate=1;

            //	disb/=ARUtil.getExchange(ampCompFund.getCurrency().getCurrencyCode(),new java.sql.Date(ampCompFund.getTransactionDate().getTime()));
            //}
            eaForm.getFunding().setRegionTotalDisb(eaForm.getFunding().getRegionTotalDisb() +
                                      disb);

            FundingDetail fd = new FundingDetail();
            fd.setAdjustmentType(ampRegFund.getAdjustmentType()
                                 .intValue());
            if (fd.getAdjustmentType() == 1) {
              fd.setAdjustmentTypeName("Actual");
            }
            else if (fd.getAdjustmentType() == 0) {
              fd.setAdjustmentTypeName("Planned");
            } else if (fd.getAdjustmentType() == 2) {
              fd.setAdjustmentTypeName("Pipeline");
            }
            fd.setCurrencyCode(ampRegFund.getCurrency()
                               .getCurrencyCode());
            fd.setCurrencyName(ampRegFund.getCurrency()
                               .getCurrencyName());
            fd.setTransactionAmount(FormatHelper.formatNumber(
                                        ampRegFund
                                        .getTransactionAmount().doubleValue()));
            fd.setTransactionDate(DateConversion
                                  .ConvertDateToString(ampRegFund
                .getTransactionDate()));
            fd.setTransactionType(ampRegFund.getTransactionType()
                                  .intValue());

            RegionalFunding regFund = new RegionalFunding();
            regFund.setRegionId( ampRegFund.getRegionLocation().getId() );
            regFund.setRegionName( ampRegFund.getRegionLocation().getName());

            if (regFunds.contains(regFund) == false) {
              regFunds.add(regFund);
            }

            int index = regFunds.indexOf(regFund);
            regFund = (RegionalFunding) regFunds.get(index);
            if (fd.getTransactionType() == 0) { // commitments
              if (regFund.getCommitments() == null) {
                regFund.setCommitments(new ArrayList());
              }
              regFund.getCommitments().add(fd);
            }
            else if (fd.getTransactionType() == 1) { // disbursements
              if (regFund.getDisbursements() == null) {
                regFund.setDisbursements(new ArrayList());
              }
              regFund.getDisbursements().add(fd);
            }
            else if (fd.getTransactionType() == 2) { // expenditures
              if (regFund.getExpenditures() == null) {
                regFund.setExpenditures(new ArrayList());
              }
              regFund.getExpenditures().add(fd);
            }
            regFunds.set(index, regFund);
          }

          // Sort the funding details based on Transaction date.
          Iterator itr1 = regFunds.iterator();
          int index = 0;
          while (itr1.hasNext()) {
            RegionalFunding regFund = (RegionalFunding) itr1.next();
            List list = null;
            if (regFund.getCommitments() != null) {
              list = new ArrayList(regFund.getCommitments());
              Collections.sort(list, FundingValidator.dateComp);
            }
            regFund.setCommitments(list);
            list = null;
            if (regFund.getDisbursements() != null) {
              list = new ArrayList(regFund.getDisbursements());
              Collections.sort(list, FundingValidator.dateComp);
            }
            regFund.setDisbursements(list);
            list = null;
            if (regFund.getExpenditures() != null) {
              list = new ArrayList(regFund.getExpenditures());
              Collections.sort(list, FundingValidator.dateComp);
            }
            regFund.setExpenditures(list);
            regFunds.set(index++, regFund);
          }

          eaForm.getFunding().setRegionalFundings(regFunds);

          eaForm.getComponents().setSelectedComponents(null);
          eaForm.getComponents().setCompTotalDisb(0);

          if (activity.getComponents() != null && activity.getComponents().size() > 0) {
            getComponents(activity, eaForm, toCurrCode);
          }

          Collection memLinks = null;
          if (tm != null)
            memLinks = TeamMemberUtil.getMemberLinks(tm.getMemberId());
          Collection actDocs = activity.getDocuments();
          if (tm != null && actDocs != null && actDocs.size() > 0) {
            Collection docsList = new ArrayList();
            Collection linksList = new ArrayList();

            Iterator docItr = actDocs.iterator();
            while (docItr.hasNext()) {
              RelatedLinks rl = new RelatedLinks();

              CMSContentItem cmsItem = (CMSContentItem) docItr
                  .next();
              rl.setRelLink(cmsItem);
              if (tm != null)
                rl.setMember(TeamMemberUtil.getAmpTeamMember(tm
                    .getMemberId()));
              Iterator tmpItr = memLinks.iterator();
              while (tmpItr.hasNext()) {
                Documents doc = (Documents) tmpItr.next();
                if ( cmsItem.getDocType() != null)
                		doc.setDocType(cmsItem.getDocType().getValue());
                if (doc.getDocId().longValue() == cmsItem
                    .getId()) {
                  rl.setShowInHomePage(true);
                  break;
                }
              }

              if (cmsItem.getIsFile()) {
                docsList.add(rl);
              }
              else {
                linksList.add(rl);
              }
            }
            eaForm.getDocuments().setDocuments(DbUtil.getKnowledgeDocuments(eaForm.getActivityId()));
            eaForm.getDocuments().setDocumentList(docsList);
            eaForm.getDocuments().setLinksList(linksList);
          }
          Site currentSite = RequestUtils.getSite(request);
          eaForm.getDocuments().setManagedDocumentList(DocumentUtil.getDocumentsForActivity(currentSite, activity));
          eaForm.getAgencies().setExecutingAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setImpAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setBenAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setConAgencies(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setReportingOrgs(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setSectGroups(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setRegGroups(new ArrayList<AmpOrganisation>());
          eaForm.getAgencies().setRespOrganisations(new ArrayList<AmpOrganisation>());
          
          eaForm.getAgencies().setExecutingOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setImpOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setBenOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setConOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setRepOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setSectOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setRegOrgToInfo(new HashMap<String, String>());
          eaForm.getAgencies().setRespOrgToInfo(new HashMap<String, String>());
          
          Set relOrgs = activity.getOrgrole();
          if (relOrgs != null) {
            Iterator relOrgsItr = relOrgs.iterator();
            AmpOrgRole orgRole = null;
            AmpOrganisation organisation = null;
            while (relOrgsItr.hasNext()) {
              orgRole = (AmpOrgRole) relOrgsItr.next();
              organisation = DbUtil.getOrganisation(orgRole.getOrganisation().getAmpOrgId());
              //
              if (orgRole.getRole().getRoleCode().equals(
                      Constants.RESPONSIBLE_ORGANISATION)
                      && (!eaForm.getAgencies().getRespOrganisations().contains(organisation))) {
                	  eaForm.getAgencies().getRespOrganisations().add(organisation);
                	  if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
                		  eaForm.getAgencies().getRespOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
                 }          
              if (orgRole.getRole().getRoleCode().equals(
                  Constants.EXECUTING_AGENCY)
                  && (!eaForm.getAgencies().getExecutingAgencies().contains(organisation))) {
            	  eaForm.getAgencies().getExecutingAgencies().add(organisation);
            	  if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
            		  eaForm.getAgencies().getExecutingOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
             }
              else if (orgRole.getRole().getRoleCode().equals(
                  Constants.IMPLEMENTING_AGENCY)
                       && (!eaForm.getAgencies().getImpAgencies().contains(
                           organisation))) {
                eaForm.getAgencies().getImpAgencies().add(
                    organisation);
                if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
                	eaForm.getAgencies().getImpOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
              }
              else if (orgRole.getRole().getRoleCode().equals(
                  Constants.BENEFICIARY_AGENCY)
                       && (!eaForm.getAgencies().getBenAgencies().contains(
                           organisation))) {
                eaForm.getAgencies().getBenAgencies().add(
                    organisation);
                if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
          		  eaForm.getAgencies().getBenOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
              }
              else if (orgRole.getRole().getRoleCode().equals(
                  Constants.CONTRACTING_AGENCY)
                       && (!eaForm.getAgencies().getConAgencies().contains(
                           organisation))) {
                eaForm.getAgencies().getConAgencies().add(
                    organisation);
                if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
          		  eaForm.getAgencies().getConOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
              }
              else if (orgRole.getRole().getRoleCode().equals(
                  Constants.REPORTING_AGENCY)
                       && (!eaForm.getAgencies().getReportingOrgs().contains(
                           organisation))) {
                eaForm.getAgencies().getReportingOrgs().add(
                    organisation);
                if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
          		  eaForm.getAgencies().getRepOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
              } else if (orgRole.getRole().getRoleCode().equals(
                      Constants.SECTOR_GROUP)
                      && (!eaForm.getAgencies().getSectGroups().contains(
                          organisation))) {
               eaForm.getAgencies().getSectGroups().add(
                   organisation);
               if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
         		  eaForm.getAgencies().getSectOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
             } else if (orgRole.getRole().getRoleCode().equals(
                     Constants.REGIONAL_GROUP)
                     && (!eaForm.getAgencies().getRegGroups().contains(
                         organisation))) {
              eaForm.getAgencies().getRegGroups().add(
                  organisation);
              if ( orgRole.getAdditionalInfo() != null && orgRole.getAdditionalInfo().length() > 0 )
        		  eaForm.getAgencies().getRegOrgToInfo().put(organisation.getAmpOrgId().toString(), orgRole.getAdditionalInfo() );
            }

            }
          }

          if (activity.getIssues() != null
              && activity.getIssues().size() > 0) {
            ArrayList issueList = new ArrayList();
            Iterator iItr = activity.getIssues().iterator();
            while (iItr.hasNext()) {
              AmpIssues ampIssue = (AmpIssues) iItr.next();
              Issues issue = new Issues();
              issue.setId(ampIssue.getAmpIssueId());
              issue.setName(ampIssue.getName());
              issue.setIssueDate(FormatHelper.formatDate(ampIssue.getIssueDate()));
              ArrayList measureList = new ArrayList();
              if (ampIssue.getMeasures() != null
                  && ampIssue.getMeasures().size() > 0) {
                Iterator mItr = ampIssue.getMeasures()
                    .iterator();
                while (mItr.hasNext()) {
                  AmpMeasure ampMeasure = (AmpMeasure) mItr
                      .next();
                  Measures measure = new Measures();
                  measure.setId(ampMeasure.getAmpMeasureId());
                  measure.setName(ampMeasure.getName());
                  ArrayList actorList = new ArrayList();
                  if (ampMeasure.getActors() != null
                      && ampMeasure.getActors().size() > 0) {
                    Iterator aItr = ampMeasure.getActors()
                        .iterator();
                    while (aItr.hasNext()) {
                      AmpActor actor = (AmpActor) aItr
                          .next();
                      actorList.add(actor);
                    }
                  }
                  measure.setActors(actorList);
                  measureList.add(measure);
                }
              }
              issue.setMeasures(measureList);
              issueList.add(issue);
            }
            eaForm.getIssues().setIssues(issueList);
          }
          else {
            eaForm.getIssues().setIssues(null);
          }

        // Regional Observations step.
		if (activity.getRegionalObservations() != null) {
			ArrayList issueList = new ArrayList();
			Iterator iItr = activity.getRegionalObservations().iterator();
			while (iItr.hasNext()) {
				AmpRegionalObservation ampRegionalObservation = (AmpRegionalObservation) iItr.next();
				Issues issue = new Issues();
				issue.setId(ampRegionalObservation.getAmpRegionalObservationId());
				issue.setName(ampRegionalObservation.getName());
				issue.setIssueDate(FormatHelper.formatDate(ampRegionalObservation.getObservationDate()));
				ArrayList measureList = new ArrayList();
				if (ampRegionalObservation.getRegionalObservationMeasures() != null) {
					Iterator mItr = ampRegionalObservation.getRegionalObservationMeasures().iterator();
					while (mItr.hasNext()) {
						AmpRegionalObservationMeasure ampMeasure = (AmpRegionalObservationMeasure) mItr.next();
						Measures measure = new Measures();
						measure.setId(ampMeasure.getAmpRegionalObservationMeasureId());
						measure.setName(ampMeasure.getName());
						ArrayList actorList = new ArrayList();
						if (ampMeasure.getActors() != null) {
							Iterator aItr = ampMeasure.getActors().iterator();
							while (aItr.hasNext()) {
								AmpRegionalObservationActor actor = (AmpRegionalObservationActor) aItr.next();
								AmpActor auxAmpActor = new AmpActor();
								auxAmpActor.setAmpActorId(actor.getAmpRegionalObservationActorId());
								auxAmpActor.setName(actor.getName());
								actorList.add(auxAmpActor);
							}
						}
						measure.setActors(actorList);
						measureList.add(measure);
					}
				}
				issue.setMeasures(measureList);
				issueList.add(issue);
			}
			eaForm.getObservations().setIssues(issueList);
		} else {
			eaForm.getObservations().setIssues(null);
		}
          
          
          // loading the contact person details and condition
          /*
          eaForm.getContactInfo().setDnrCntFirstName(activity.getContFirstName());
          eaForm.getContactInfo().setDnrCntLastName(activity.getContLastName());
          eaForm.getContactInfo().setDnrCntEmail(activity.getEmail());
          eaForm.getContactInfo().setDnrCntTitle(activity.getDnrCntTitle());
          eaForm.getContactInfo().setDnrCntOrganization(activity.getDnrCntOrganization());
          eaForm.getContactInfo().setDnrCntPhoneNumber(activity.getDnrCntPhoneNumber());
          eaForm.getContactInfo().setDnrCntFaxNumber(activity.getDnrCntFaxNumber());

          eaForm.getContactInfo().setMfdCntFirstName(activity.getMofedCntFirstName());
          eaForm.getContactInfo().setMfdCntLastName(activity.getMofedCntLastName());
          eaForm.getContactInfo().setMfdCntEmail(activity.getMofedCntEmail());
          eaForm.getContactInfo().setMfdCntTitle(activity.getMfdCntTitle());
          eaForm.getContactInfo().setMfdCntOrganization(activity.getMfdCntOrganization());
          eaForm.getContactInfo().setMfdCntPhoneNumber(activity.getMfdCntPhoneNumber());
          eaForm.getContactInfo().setMfdCntFaxNumber(activity.getMfdCntFaxNumber());
          
          eaForm.getContactInfo().setPrjCoFirstName(activity.getPrjCoFirstName());
          eaForm.getContactInfo().setPrjCoLastName(activity.getPrjCoLastName());
          eaForm.getContactInfo().setPrjCoEmail(activity.getPrjCoEmail());
          eaForm.getContactInfo().setPrjCoTitle(activity.getPrjCoTitle());
          eaForm.getContactInfo().setPrjCoOrganization(activity.getPrjCoOrganization());
          eaForm.getContactInfo().setPrjCoPhoneNumber(activity.getPrjCoPhoneNumber());
          eaForm.getContactInfo().setPrjCoFaxNumber(activity.getPrjCoFaxNumber());
          
          eaForm.getContactInfo().setSecMiCntFirstName(activity.getSecMiCntFirstName());
          eaForm.getContactInfo().setSecMiCntLastName(activity.getSecMiCntLastName());
          eaForm.getContactInfo().setSecMiCntEmail(activity.getSecMiCntEmail());
          eaForm.getContactInfo().setSecMiCntTitle(activity.getSecMiCntTitle());
          eaForm.getContactInfo().setSecMiCntOrganization(activity.getSecMiCntOrganization());
          eaForm.getContactInfo().setSecMiCntPhoneNumber(activity.getSecMiCntPhoneNumber());
          eaForm.getContactInfo().setSecMiCntFaxNumber(activity.getSecMiCntFaxNumber());
		  */
          
          
          ActivityContactInfo contactInfo=eaForm.getContactInformation();
          //Reset contact info
          contactInfo.setDonorContacts(new ArrayList<AmpActivityContact>());
          contactInfo.setImplExecutingAgencyContacts(new ArrayList<AmpActivityContact>());
          contactInfo.setMofedContacts(new ArrayList<AmpActivityContact>());
          contactInfo.setProjCoordinatorContacts(new ArrayList<AmpActivityContact>());
          contactInfo.setSectorMinistryContacts(new ArrayList<AmpActivityContact>());
          
          
          
	      List<AmpActivityContact> activityContacts=null;
	      try {
	    	  activityContacts=ContactInfoUtil.getActivityContacts(activity.getAmpActivityId());
	    	  if(activityContacts!=null && activityContacts.size()>0){
	    		  for (AmpActivityContact ampActCont : activityContacts) {
					AmpContact contact= ampActCont.getContact();
					contact.setTemporaryId(contact.getId().toString());
				}
	    	  }
		} catch (Exception e) {
			e.printStackTrace();
		}
	      contactInfo.setActivityContacts(activityContacts);
	      if(activityContacts!=null && activityContacts.size()>0){
	    	  for (AmpActivityContact ampActContact : activityContacts) {
	    		//donor contact
				if(ampActContact.getContactType().equals(Constants.DONOR_CONTACT)){
					if(contactInfo.getDonorContacts()==null){
						contactInfo.setDonorContacts(new ArrayList<AmpActivityContact>());
					}
					if(ampActContact.getPrimaryContact()!=null && ampActContact.getPrimaryContact()){
                        contactInfo.setPrimaryDonorContId(ampActContact.getContact().getTemporaryId());
                        /*
						contactInfo.setPrimaryDonorContId(ampActContact.getContact().getTemporaryId());
						/*if(contactInfo.getPrimaryDonorContIds()==null){
							contactInfo.setPrimaryDonorContIds(new String[1]);
						}
						contactInfo.getPrimaryDonorContIds()[0]=ampActContact.getContact().getTemporaryId();*/
						
					}					
					contactInfo.getDonorContacts().add(ampActContact);
				}
				//mofed contact
				else if(ampActContact.getContactType().equals(Constants.MOFED_CONTACT)){
					if(contactInfo.getMofedContacts()==null){
						contactInfo.setMofedContacts(new ArrayList<AmpActivityContact>());
					}
					if(ampActContact.getPrimaryContact()!=null && ampActContact.getPrimaryContact()){
                        contactInfo.setPrimaryMofedContId(ampActContact.getContact().getTemporaryId());
                        /*
						contactInfo.setPrimaryMofedContId(ampActContact.getContact().getTemporaryId());
						/*if(contactInfo.getPrimaryMofedContIds()==null){
							contactInfo.setPrimaryMofedContIds(new String[1]);
						}
						contactInfo.getPrimaryMofedContIds()[0]=ampActContact.getContact().getTemporaryId();*/
						
					}
					contactInfo.getMofedContacts().add(ampActContact);
				}
				//project coordinator contact
				else if(ampActContact.getContactType().equals(Constants.PROJECT_COORDINATOR_CONTACT)){
					if(contactInfo.getProjCoordinatorContacts()==null){
						contactInfo.setProjCoordinatorContacts(new ArrayList<AmpActivityContact>());
					}

					if(ampActContact.getPrimaryContact()!=null && ampActContact.getPrimaryContact()){
                        contactInfo.setPrimaryProjCoordContId (ampActContact.getContact().getTemporaryId());
                        /*
						contactInfo.setPrimaryProjCoordContId (ampActContact.getContact().getTemporaryId());
						/*if(contactInfo.getPrimaryProjCoordContIds()==null){
							contactInfo.setPrimaryProjCoordContIds(new String[1]);
						}
						contactInfo.getPrimaryProjCoordContIds()[0]=ampActContact.getContact().getTemporaryId();*/
						

					}
					contactInfo.getProjCoordinatorContacts().add(ampActContact);
				}
				//sector ministry contact
				else if(ampActContact.getContactType().equals(Constants.SECTOR_MINISTRY_CONTACT)){
					if(contactInfo.getSectorMinistryContacts()==null){
						contactInfo.setSectorMinistryContacts(new ArrayList<AmpActivityContact>());
					}
					if(ampActContact.getPrimaryContact()!=null && ampActContact.getPrimaryContact()){
                        contactInfo.setPrimarySecMinContId (ampActContact.getContact().getTemporaryId());
                        /*
						contactInfo.setPrimarySecMinContId (ampActContact.getContact().getTemporaryId());
						/*if(contactInfo.getPrimarySecMinContIds()==null){
							contactInfo.setPrimarySecMinContIds(new String[1]);
						}
						contactInfo.getPrimarySecMinContIds()[0]=ampActContact.getContact().getTemporaryId();*/
						
					}
					contactInfo.getSectorMinistryContacts().add(ampActContact);
				}
				//implementing/executing agency
				else if(ampActContact.getContactType().equals(Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT)){
					if(contactInfo.getImplExecutingAgencyContacts()==null){
						contactInfo.setImplExecutingAgencyContacts(new ArrayList<AmpActivityContact>());
					}
					if(ampActContact.getPrimaryContact()!=null && ampActContact.getPrimaryContact()){
                        contactInfo.setPrimaryImplExecutingContId (ampActContact.getContact().getTemporaryId());
                        /*
						contactInfo.setPrimaryImplExecutingContId (ampActContact.getContact().getTemporaryId());
						/*if(contactInfo.getPrimaryImplExecutingContIds()==null){
							contactInfo.setPrimaryImplExecutingContIds(new String[1]);
						}
						contactInfo.getPrimaryImplExecutingContIds()[0]=ampActContact.getContact().getTemporaryId();*/
						
					}
					contactInfo.getImplExecutingAgencyContacts().add(ampActContact);
				}
			}
	    	  
	      }
	      
	      if(activityContacts!=null){
	    	  AmpContactsWorker.copyContactsToSubLists(activityContacts, eaForm);
	      }          
          
          
// The if block below doesn't seem to make any sense. I don't see any reason to set the 
// activity creator when editing the activity. 
// If someone still need to re-enable this piece of code at least change the condition 
// eaForm.getIsPreview!=1 => isPreview
//          
//          if (eaForm.getIsPreview() != 1 && !isPublicView) {
//            AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(tm.
//                getMemberId());
//            activity.setActivityCreator(teamMember);
//          }
          if (activity.getCondition() != null)
        	  eaForm.getIdentification().setConditions(activity.getCondition().trim());

          if (activity.getActivityCreator() != null) {
            User usr = activity.getActivityCreator().getUser();
            if (usr != null) {
              eaForm.getIdentification().setActAthFirstName(usr.getFirstNames());
              eaForm.getIdentification().setActAthLastName(usr.getLastName());
              eaForm.getIdentification().setActAthEmail(usr.getEmail());
              eaForm.getIdentification().setActAthAgencySource(usr.getOrganizationName());
            }
          }
        }
        
        if(eaForm.getCustomFields()!=null){
	        Iterator<CustomField<?>> itcf = eaForm.getCustomFields().iterator();
	        while(itcf.hasNext()){
	        	CustomField cf = itcf.next();
	        	try{
	        		Object value = PropertyUtils.getSimpleProperty(activity, cf.getAmpActivityPropertyName());
	        		cf.setValue(value);
	        	}catch(Exception e){
	        		logger.error("Error getting property [" + cf.getAmpActivityPropertyName() + "] from bean ", e);
	        	}
	        }
        }
      }
      //Collection statusCol = null;
      // load the status from the database
//            if(eaForm.getStatusCollection() == null) { // TO BE DELETED
//                statusCol = DbUtil.getAmpStatus();
//                eaForm.setStatusCollection(statusCol);
//            } else {
//                statusCol = eaForm.getStatusCollection();
//            }
      // Initailly setting the implementation level as "country"
//      if (eaForm.getLocation().getImplemLocationLevel() == null)
//        eaForm.getLocation().setImplemLocationLevel(
//            CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.
//            IMPLEMENTATION_LOCATION_KEY, new Long(0)).getId()
//            );

      //Collection modalColl = null;
      // load the modalities from the database
      /* if(eaForm.getModalityCollection() == null) { //No need to load modalitiees. Using category manager.
           modalColl = DbUtil.getAmpModality();
           eaForm.setModalityCollection(modalColl);
       } else {
           modalColl = eaForm.getModalityCollection();
       }*/

      // Initally set the modality as "Project Support"
      Collection financingInstrValues = CategoryManagerUtil.
          getAmpCategoryValueCollectionByKey(CategoryConstants.
                                             FINANCING_INSTRUMENT_KEY, null, request);
      if (financingInstrValues != null && financingInstrValues.size() > 0) {
        Iterator itr = financingInstrValues.iterator();
        while (itr.hasNext()) {
          AmpCategoryValue financingInstr = (AmpCategoryValue) itr.next();
          if(financingInstr!=null)
        	  if(financingInstr.getValue()!=null)
        		  if (financingInstr.getValue().equalsIgnoreCase("Project Support")) {
        			  eaForm.getFunding().setModality(financingInstr.getId());
        			  break;
        		  }
        }
      }
      //Collection levelCol = null;
      // Loading the levels from the database
      /*if(eaForm.getLevelCollection() == null) {
          levelCol = DbUtil.getAmpLevels();
          eaForm.setLevelCollection(levelCol);
                   } else {
          levelCol = eaForm.getLevelCollection();
                   }
       */

      // load all the active currencies
      eaForm.setCurrencies(CurrencyUtil.getAmpCurrency());
      
      //Only currencies havening exchanges rates AMP-2620
      ArrayList<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
      eaForm.getFunding().setValidcurrencies(validcurrencies);
      for (Iterator iter = eaForm.getCurrencies().iterator(); iter.hasNext();) {
		AmpCurrency element = (AmpCurrency) iter.next();
		 if( CurrencyUtil.isRate(element.getCurrencyCode())== true)
				{
			 		if(element!=null && element.getCurrencyCode()!=null)
			 			eaForm.getFunding().getValidcurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
				}
		}


      //load the possible projection values
      eaForm.getFunding().setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false, request));

    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
    if (request.getParameter("logframepr") != null)
      if (request.getParameter("logframepr").compareTo("true") == 0) {
        session.setAttribute("logframepr", "true");
        return mapping.findForward("forwardToPreview");
      }

    Collection ampFundingsAux = DbUtil.getAmpFunding(activityId);
    FilterParams fp = (FilterParams) session.getAttribute("filterParams");
    TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
    if (fp == null) {
      fp = new FilterParams();
      int year = new GregorianCalendar().get(Calendar.YEAR);
		fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
		fp.setToYear(year+Constants.TO_YEAR_RANGE);
    }

    ApplicationSettings apps = null;
    if (teamMember != null) {
      apps = teamMember.getAppSettings();
    }
    
    if (apps != null) {
        Currency curr = CurrencyUtil.getCurrency(apps.getCurrencyId());
			if (curr != null) {
				fp.setCurrencyCode(curr.getCurrencyCode());
			}

			if (fp.getFiscalCalId() == null) {
				if (apps.getFisCalId() != null) {
					fp.setFiscalCalId(apps.getFisCalId());
				} else {
					fp.setFiscalCalId(FeaturesUtil
							.getGlobalSettingValueLong("Default Calendar"));
				}
			}

      Collection<FinancingBreakdown> fb = FinancingBreakdownWorker.getFinancingBreakdownList(
          activityId, ampFundingsAux, fp,debug);
      eaForm.getFunding().setFinancingBreakdown(fb);
      String overallTotalCommitted = "";
      String overallTotalDisbursed = "";
      String overallTotalUnDisbursed = "";
      String overallTotalExpenditure = "";
      String overallTotalUnExpended = "";
      String overallTotalDisburOrder = "";
      
      overallTotalCommitted = FinancingBreakdownWorker.getOverallTotal(
          fb, Constants.COMMITMENT,Constants.ACTUAL,debug);
      overallTotalDisbursed = FinancingBreakdownWorker.getOverallTotal(
          fb, Constants.DISBURSEMENT,Constants.ACTUAL,debug);
      overallTotalDisburOrder=FinancingBreakdownWorker.getOverallTotal(
          fb, Constants.DISBURSEMENT_ORDER,Constants.ACTUAL,debug);
      if(!debug){
      overallTotalUnDisbursed = FormatHelper.getDifference(
          overallTotalCommitted, overallTotalDisbursed);
      }
      else{
    	  overallTotalUnDisbursed =overallTotalCommitted +"-" +overallTotalDisbursed; 
      }
      overallTotalExpenditure = FinancingBreakdownWorker.getOverallTotal(
          fb, Constants.EXPENDITURE,Constants.ACTUAL,debug);
      if(!debug){
      overallTotalUnExpended = FormatHelper.getDifference(
          overallTotalDisbursed, overallTotalExpenditure);
      }
      else{
    	  overallTotalExpenditure = overallTotalDisbursed+ "-" + overallTotalExpenditure;
      }
      
      eaForm.getFunding().setTotalCommitted(overallTotalCommitted);
      eaForm.getFunding().setTotalDisbursed(overallTotalDisbursed);
      eaForm.getFunding().setTotalExpended(overallTotalExpenditure);
      eaForm.getFunding().setTotalUnDisbursed(overallTotalUnDisbursed);
      eaForm.getFunding().setTotalUnExpended(overallTotalUnExpended);
      eaForm.getFunding().setTotalDisbOrder(overallTotalDisburOrder);
    }
    
    String actApprovalStatus = DbUtil.getActivityApprovalStatus(activityId);
	Long ampTeamId = teamMember.getTeamId();
	boolean teamLeadFlag    = teamMember.getTeamHead() || teamMember.isApprover();
	boolean workingTeamFlag = TeamUtil.checkForParentTeam(ampTeamId);
	//commented under AMP-10788
//	if (workingTeamFlag) {
//		eaForm.setButtonText("edit");	// In case of regular working teams
//		if (!(activity.getDraft()!=null && activity.getDraft()) && ( actApprovalStatus != null && Constants.ACTIVITY_NEEDS_APPROVAL_STATUS.contains(actApprovalStatus.toLowerCase())))
//	 	{
//	 		//burkina
//	 		// if an user save an activity he could edit it even it is not approved by team leader
//	 		//if(workingTeamFlag && !teamLeadFlag && teamMember.getMemberId().equals(activity.getCreatedBy().getAmpTeamMemId()))
//	 		if (workingTeamFlag && teamLeadFlag && teamMember.getTeamId().equals(activity.getTeam().getAmpTeamId())) {
//	 			eaForm.setButtonText("validate");
//	 		}/*else {
//	 			formBean.setButtonText("approvalAwaited");
//	 		}*/		 		
//	 	}
//	} else {
//		eaForm.setButtonText("none");	// In case of management-workspace
//	}
	
	String globalProjectsValidation		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION);
	boolean isManagement = false;
	if("Management".toLowerCase().compareTo(teamMember.getTeamAccessType().toLowerCase()) == 0) {
		eaForm.setButtonText("none");
	}
	else{ //not a management team
		//there is another simple way to write these "if"s, but it is more clear like this
		eaForm.setButtonText("edit");
		if(activity!=null && activity.getDraft()!=null && !activity.getDraft())
			if("Off".toLowerCase().compareTo(globalProjectsValidation.toLowerCase())==0){
				//global validation off
				eaForm.setButtonText("edit");
			}
			else{
				//global validation is on
				
//				if("alloff".compareTo(apps.getValidation().toLowerCase())==0)
//					eaForm.setButtonText("edit");
				
				//only the team leader of the team that owns the activity has rights to validate it
				//if activity is already approved it will display the edit value
				if("alledits".compareTo(apps.getValidation().toLowerCase())==0 )
					if(teamLeadFlag &&
					   teamMember.getTeamId().equals(activity.getTeam().getAmpTeamId()) &&
					   (Constants.STARTED_STATUS.compareTo(activity.getApprovalStatus().toLowerCase())==0 || 
					    Constants.EDITED_STATUS.compareTo(activity.getApprovalStatus().toLowerCase())==0)
					   ) 
					eaForm.setButtonText("validate");
					//else eaForm.setButtonText("edit");
				
				//only the team leader of the team that owns the activity has rights to validate it
				//it will display the validate label only if it is just started and was not approved not even once
				if("newonly".compareTo(apps.getValidation().toLowerCase())==0)
					if(teamLeadFlag && 
							Constants.STARTED_STATUS.compareTo(activity.getApprovalStatus().toLowerCase())==0					
							 && teamMember.getTeamId().equals(activity.getTeam().getAmpTeamId())
					) eaForm.setButtonText("validate");
					//else eaForm.setButtonText("edit");
			}
	}
	
	
	
   
    } finally {
    	org.digijava.kernel.persistence.PersistenceManager.releaseSession(hsession); 	
    }
    
    String debugFM=request.getParameter("debugFM");
    if(debugFM!=null && "true".compareTo(debugFM)==0)
    	return mapping.findForward("forwardDebugFM");
    return mapping.findForward("forward");
  }

  private EditActivityForm setSectorsToForm(EditActivityForm form, AmpActivityVersion activity) {
		Collection sectors = activity.getSectors();

		if (sectors != null && sectors.size() > 0) {
			List<ActivitySector> activitySectors = new ArrayList<ActivitySector>();
			Iterator sectItr = sectors.iterator();
			while (sectItr.hasNext()) {
				AmpActivitySector ampActSect = (AmpActivitySector) sectItr.next();
				if (ampActSect != null) {
					AmpSector sec = ampActSect.getSectorId();
					if (sec != null) {
						AmpSector parent = null;
						AmpSector subsectorLevel1 = null;
						AmpSector subsectorLevel2 = null;
						if (sec.getParentSectorId() != null) {
							if (sec.getParentSectorId().getParentSectorId() != null) {
								subsectorLevel2 = sec;
								subsectorLevel1 = sec.getParentSectorId();
								parent = sec.getParentSectorId().getParentSectorId();
							} else {
								subsectorLevel1 = sec;
								parent = sec.getParentSectorId();
							}
						} else {
							parent = sec;
						}
						ActivitySector actSect = new ActivitySector();
                                                actSect.setConfigId(ampActSect.getClassificationConfig().getId());
						if (parent != null) {
							actSect.setId(parent.getAmpSectorId());
							String view = FeaturesUtil.getGlobalSettingValue("Allow Multiple Sectors");
							if (view != null)
								if (view.equalsIgnoreCase("On")) {
									actSect.setCount(1);
								} else {
									actSect.setCount(2);
								}

							actSect.setSectorId(parent.getAmpSectorId());
							actSect.setSectorName(parent.getName());
							if (subsectorLevel1 != null) {
								actSect.setSubsectorLevel1Id(subsectorLevel1.getAmpSectorId());
								actSect.setSubsectorLevel1Name(subsectorLevel1.getName());
								if (subsectorLevel2 != null) {
									actSect.setSubsectorLevel2Id(subsectorLevel2.getAmpSectorId());
									actSect.setSubsectorLevel2Name(subsectorLevel2.getName());
								}
							}
							actSect.setSectorPercentage(ampActSect.getSectorPercentage());
                                                        actSect.setSectorScheme(parent.getAmpSecSchemeId().getSecSchemeName());
                                                        
						}
                                               
						activitySectors.add(actSect);
					}
				}
			}
			Collections.sort(activitySectors);
			form.getSectors().setActivitySectors(activitySectors);
		}
		return form;
	}

  private Collection getSectosHelper(Collection sectors){
	  return null;
  }


/**
 * @param activity
 * @param eaForm
 * @param componets
 */
	private void getComponents(AmpActivityVersion activity, EditActivityForm eaForm, String toCurrCode) {

		Collection componets = activity.getComponents();
		List<Components<FundingDetail>> selectedComponents = new ArrayList<Components<FundingDetail>>();
		Iterator compItr = componets.iterator();
		while (compItr.hasNext()) {
			AmpComponent temp = (AmpComponent) compItr.next();
			Components<FundingDetail> tempComp = new Components<FundingDetail>();
			tempComp.setTitle(temp.getTitle());
			tempComp.setComponentId(temp.getAmpComponentId());
			tempComp.setType_Id((temp.getType() != null) ? temp.getType().getType_id() : null);
		
			if (temp.getDescription() == null) {
				tempComp.setDescription(" ");
			} else {
				tempComp.setDescription(temp.getDescription().trim());
			}
			tempComp.setCode(temp.getCode());
			tempComp.setUrl(temp.getUrl());
			tempComp.setCommitments(new ArrayList<FundingDetail>());
			tempComp.setDisbursements(new ArrayList<FundingDetail>());
			tempComp.setExpenditures(new ArrayList<FundingDetail>());

			Collection<AmpComponentFunding> fundingComponentActivity = ActivityUtil.getFundingComponentActivity(tempComp.getComponentId(), activity.getAmpActivityId());
			Iterator cItr = fundingComponentActivity.iterator();
		
			while (cItr.hasNext()) {
				AmpComponentFunding ampCompFund = (AmpComponentFunding) cItr.next();

				double disb = 0;
				
				if (ampCompFund.getAdjustmentType().intValue() == 1 && ampCompFund.getTransactionType().intValue() == 1) 
				disb = ampCompFund.getTransactionAmount().doubleValue();

				eaForm.getComponents().setCompTotalDisb(eaForm.getComponents().getCompTotalDisb() + disb);
				
				FundingDetail fd = new FundingDetail();
				
				fd.setAdjustmentType(ampCompFund.getAdjustmentType().intValue());
				
				if (fd.getAdjustmentType() == 1) {
					fd.setAdjustmentTypeName("Actual");
				} else if (fd.getAdjustmentType() == 0) {
					fd.setAdjustmentTypeName("Planned");
                } else if (fd.getAdjustmentType() == 2) {
                    fd.setAdjustmentTypeName("Pipeline");
                }
		
				fd.setAmpComponentFundingId(ampCompFund.getAmpComponentFundingId());
				
				//convert to  default currency 
				
				java.sql.Date dt = new java.sql.Date(ampCompFund.getTransactionDate().getTime());

				double frmExRt = ampCompFund.getExchangeRate() != null ? ampCompFund.getExchangeRate() : Util.getExchange(ampCompFund.getCurrency().getCurrencyCode(), dt);
				double toExRt = Util.getExchange(toCurrCode, dt);
				DecimalWraper amt = CurrencyWorker.convertWrapper(ampCompFund.getTransactionAmount(), frmExRt, toExRt, dt);

				
				fd.setCurrencyCode(toCurrCode);
				fd.setTransactionAmount(FormatHelper.formatNumber( amt.getValue()));
				
				
				fd.setCurrencyName(ampCompFund.getCurrency().getCurrencyName());
				fd.setTransactionDate(DateConversion.ConvertDateToString(ampCompFund.getTransactionDate()));
				
				fd.setTransactionType(ampCompFund.getTransactionType().intValue());
				
				if (fd.getTransactionType() == 0) {
				
					tempComp.getCommitments().add(fd);
				} else if (fd.getTransactionType() == 1) {
					tempComp.getDisbursements().add(fd);
					
				} else if (fd.getTransactionType() == 2) {
					tempComp.getExpenditures().add(fd);
				}
				
				
			}

			ComponentsUtil.calculateFinanceByYearInfo(tempComp, fundingComponentActivity);

			Collection<AmpPhysicalPerformance> phyProgress = ActivityUtil.getPhysicalProgressComponentActivity(tempComp.getComponentId(), activity.getAmpActivityId());

			if (phyProgress != null && phyProgress.size() > 0) {
				Collection physicalProgress = new ArrayList();
				Iterator phyProgItr = phyProgress.iterator();
				while (phyProgItr.hasNext()) {
					AmpPhysicalPerformance phyPerf = (AmpPhysicalPerformance) phyProgItr.next();
					PhysicalProgress phyProg = new PhysicalProgress();
					phyProg.setPid(phyPerf.getAmpPpId());
					phyProg.setDescription(phyPerf.getDescription());
					phyProg.setReportingDate(DateConversion.ConvertDateToString(phyPerf.getReportingDate()));
					phyProg.setTitle(phyPerf.getTitle());
					physicalProgress.add(phyProg);
				}
				tempComp.setPhyProgress(physicalProgress);
			}

			selectedComponents.add(tempComp);
		}

		// Sort the funding details based on Transaction date.
		Iterator compIterator = selectedComponents.iterator();
		int index = 0;
		while (compIterator.hasNext()) {
			Components components = (Components) compIterator.next();
			List list = null;
			if (components.getCommitments() != null) {
				list = new ArrayList(components.getCommitments());
				Collections.sort(list, FundingValidator.dateComp);
			}
			components.setCommitments(list);
			list = null;
			if (components.getDisbursements() != null) {
				list = new ArrayList(components.getDisbursements());
				Collections.sort(list, FundingValidator.dateComp);
			}
			components.setDisbursements(list);
			list = null;
			if (components.getExpenditures() != null) {
				list = new ArrayList(components.getExpenditures());
				Collections.sort(list, FundingValidator.dateComp);
			}
			components.setExpenditures(list);
			selectedComponents.set(index++, components);
		}

		eaForm.getComponents().setSelectedComponents(selectedComponents);
	}

	private double getAmountInDefaultCurrency(FundingDetail fundDet, String toCurrCode ) {
		
		java.sql.Date dt = new java.sql.Date(DateConversion.getDate(fundDet.getTransactionDate()).getTime());
		double frmExRt = Util.getExchange(fundDet.getCurrencyCode(),dt);
		//String toCurrCode = CurrencyUtil.getAmpcurrency( appSet.getCurrencyId() ).getCurrencyCode();
		double toExRt = Util.getExchange(toCurrCode, dt);
	
		double amt = CurrencyWorker.convert1(FormatHelper.parseDouble(fundDet.getTransactionAmount()),frmExRt,toExRt);
		
		return amt;
		
	}

}
