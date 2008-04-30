/*
 * AddAmpActivity.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.Constants;

/**
 * Used to capture the activity details to the form bean of type org.digijava.module.aim.form.EditActivityForm
 *
 * Add Activity is an eight step process with a preview at the last. The same action is used for all the eight
 * steps + preview. A form bean variable identified by the name 'step' is used for this purpose. When the user
 * clicks the next button in the jsp page, the value of the step is incremented by 1. Thus based on the value of
 * this variable the action forwards it to the eight steps and the preview.
 *
 * @author Priyajith
 */
public class AddAmpActivity extends Action {

  //private static Logger logger = Logger.getLogger(AddAmpActivity.class);

  private ServletContext ampContext = null;

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws java.lang.
      Exception {

    HttpSession session = request.getSession();
    	ampContext = getServlet().getServletContext();


    TeamMember teamMember = new TeamMember();

    // Get the current member who has logged in from the session
    teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);

    //PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);

    // if user is not logged in, forward him to the home page
    //if (session.getAttribute("currentMember") == null)
    //return mapping.findForward("index");

    //return mapping.findForward("publicPreview");

    EditActivityForm eaForm = (EditActivityForm) form; 
    session.setAttribute("selectedSectorsForActivity", eaForm.getActivitySectors());
    
   
    
    if (teamMember != null && teamMember.getTeamType()
       .equalsIgnoreCase("GOVERNMENT")) {
       eaForm.setGovFlag(true);
    } else {
       eaForm.setGovFlag(false);
   }

      if (eaForm.getSteps() == null) {
          List steps = ActivityUtil.getSteps(eaForm.isGovFlag());
          eaForm.setSteps(steps);
      }
    if(eaForm.getClassificationConfigs()==null){
        eaForm.setClassificationConfigs(SectorUtil.getAllClassificationConfigs());
    }

    //set the level, if available
    String levelTxt=request.getParameter("activityLevelId");
    if(levelTxt!=null) eaForm.setActivityLevel(Long.parseLong(levelTxt));

     //set the contracts, if available
     //eaForm.getCurrCode()
    if(eaForm.getActivityId()!=null&&(eaForm.getContracts()==null||eaForm.getContracts().size()==0)){
           List contracts=ActivityUtil.getIPAContracts(eaForm.getActivityId(),eaForm.getCurrCode());
           eaForm.setContracts(contracts);
     }

     // load all the active currencies
      eaForm.setCurrencies(CurrencyUtil.getAmpCurrency());

    //Only currencies havening exchanges rates AMP-2620
      ArrayList<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
      eaForm.setValidcurrencies(validcurrencies);
      if(eaForm.getCurrencies()!=null && eaForm.getCurrencies().size()>0){
    	  for (Iterator iter = eaForm.getCurrencies().iterator(); iter.hasNext();) {
    			AmpCurrency element = (AmpCurrency) iter.next();
    			 if( CurrencyUtil.isRate(element.getCurrencyCode())== true)
    					{
    				 	eaForm.getValidcurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
    					}
    			}
      }




    /*Clear eventually dirty information found in session related to DM*/
		if ( request.getParameter("action") != null && request.getParameter("action").equals("create") ){
                        SelectDocumentDM.clearContentRepositoryHashMap(request);
                        eaForm.setActPrograms(null);
                        if (ProgramUtil.getAmpActivityProgramSettingsList() != null) {
                                eaForm.setNationalSetting(ProgramUtil.
                                                          getAmpActivityProgramSettings(
                                                              ProgramUtil.
                                                              NATIONAL_PLAN_OBJECTIVE));
                                eaForm.setPrimarySetting(ProgramUtil.
                                                         getAmpActivityProgramSettings(
                                                             ProgramUtil.PRIMARY_PROGRAM));
                                eaForm.setSecondarySetting(ProgramUtil.
                                                           getAmpActivityProgramSettings(
                                                               ProgramUtil.SECONDARY_PROGRAM));
               }

                }


	//===============Sectors START===========================

		// set Global Settings Multi-Sector Selecting
		/*String multiSectorSelect = FeaturesUtil
				.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBALSETTINGS_MULTISECTORSELECT);
		eaForm.setMultiSectorSelecting(multiSectorSelect);*/

		// Add sectors
		if (request.getParameter("addSector") != null) {

			Object searchedsector = session.getAttribute("add");

			if (searchedsector != null && searchedsector.equals("true")) {
				Collection selectedSecto = (Collection) session
						.getAttribute("sectorSelected");
				Collection<ActivitySector> prevSelSectors = eaForm
						.getActivitySectors();

				if (selectedSecto != null) {
					Iterator<ActivitySector> itre = selectedSecto.iterator();
					while (itre.hasNext()) {
						ActivitySector selectedSector = (ActivitySector) itre
								.next();

						boolean addSector = true;
						if (prevSelSectors != null) {
							Iterator<ActivitySector> itr = prevSelSectors
									.iterator();
							while (itr.hasNext()) {
								ActivitySector asec = (ActivitySector) itr
										.next();

								if (asec.getSectorName().equals(selectedSector.getSectorName())) {
									if (selectedSector.getSubsectorLevel1Name() == null) {
										addSector = false;
										break;
									}
									if (asec.getSubsectorLevel1Name() != null) {
										if (asec.getSubsectorLevel1Name().equals(selectedSector.getSubsectorLevel1Name())) {
											if (selectedSector.getSubsectorLevel2Name() == null) {
												addSector = false;
												break;
											}
											if (asec.getSubsectorLevel2Name() != null) {
												if (asec.getSubsectorLevel2Name().equals(selectedSector.getSubsectorLevel2Name())) {
													addSector = false;
													break;
												}
											} else {
												addSector = true;
												break;
											}
										}
									} else {
										addSector = true;
										break;
									}
								}
							}
						}

						if (addSector) {
							// if an activity already has one or more
							// sectors,than after adding new one
							// the percentages must equal blanks and user should
							// fill them
                            if (prevSelSectors != null) {
                                Iterator iter = prevSelSectors.iterator();
                                boolean firstSecForConfig = true;
                                while (iter.hasNext()) {
                                    ActivitySector actSect = (ActivitySector) iter
                                        .next();
                                    if (actSect.getConfigId().equals(selectedSector.getConfigId())) {
                                        firstSecForConfig = false;
                                        break;
                                    }

                                }
                                if (firstSecForConfig) {
                                    selectedSector.setSectorPercentage(100f);
                                }
                                prevSelSectors.add(selectedSector);
                            } else {
                                selectedSector.setSectorPercentage(new Float(
                                    100));
                                prevSelSectors = new ArrayList<ActivitySector> ();
                                prevSelSectors.add(selectedSector);
                            }
						}

						eaForm.setActivitySectors(prevSelSectors);
					}

				}
				session.removeAttribute("sectorSelected");
				session.removeAttribute("add");
                                session.setAttribute("selectedSectorsForActivity", eaForm.getActivitySectors());
				return mapping.findForward("addActivityStep2");

			} else {
				ActivitySector selectedSector = (ActivitySector) session
						.getAttribute("sectorSelected");
				Collection<ActivitySector> prevSelSectors = eaForm
						.getActivitySectors();

				boolean addSector = true;
				if (prevSelSectors != null) {
					Iterator<ActivitySector> itr = prevSelSectors.iterator();
					while (itr.hasNext()) {
						ActivitySector asec = (ActivitySector) itr.next();
						if (asec.getSectorName().equals(
								selectedSector.getSectorName())) {
							if (selectedSector.getSubsectorLevel1Name() == null) {
								addSector = false;
								break;
							}
							if (asec.getSubsectorLevel1Name() != null) {
								if (asec
										.getSubsectorLevel1Name()
										.equals(
												selectedSector
														.getSubsectorLevel1Name())) {
									if (selectedSector.getSubsectorLevel2Name() == null) {
										addSector = false;
										break;
									}
									if (asec.getSubsectorLevel2Name() != null) {
										if (asec
												.getSubsectorLevel2Name()
												.equals(
														selectedSector
																.getSubsectorLevel2Name())) {
											addSector = false;
											break;
										}
									} else {
										addSector = false;
										break;
									}
								}
							} else {
								addSector = false;
								break;
							}
						}
					}
				}

                if (addSector) {
                    // if an activity already has one or more sectors,than after
                    // adding new one
                    // the percentages must equal blanks and user should fill
                    // them
                    if (prevSelSectors != null) {
                        Iterator iter = prevSelSectors.iterator();
                        boolean firstSecForConfig = true;
                        while (iter.hasNext()) {
                            ActivitySector actSect = (ActivitySector) iter
                                .next();
                            if (actSect.getConfigId().equals(selectedSector.getConfigId())) {
                                firstSecForConfig = false;
                                break;
                            }

                        }
                        if (firstSecForConfig) {
                            selectedSector.setSectorPercentage(100f);
                        }
                        prevSelSectors.add(selectedSector);
                    } else {
                        selectedSector.setSectorPercentage(new Float(
                            100));
                        prevSelSectors = new ArrayList<ActivitySector> ();
                        prevSelSectors.add(selectedSector);
                    }
                }
				eaForm.setActivitySectors(prevSelSectors);
				session.removeAttribute("sectorSelected");
                                session.setAttribute("selectedSectorsForActivity", eaForm.getActivitySectors());
				return mapping.findForward("addActivityStep2");

			}

		}

    // Remove sectors
    else
    if (request.getParameter("remSectors") != null) {
      Long selSectors[] = eaForm.getSelActivitySectors();
      String configId=request.getParameter("configId");
      Collection<ActivitySector> prevSelSectors = eaForm.getActivitySectors();
      session.setAttribute("removedSector", eaForm.getSelActivitySectors());
      Collection newSectors = new ArrayList();

      Iterator<ActivitySector> itr = prevSelSectors.iterator();

      boolean flag = false;

      while (itr.hasNext()) {
        ActivitySector asec = (ActivitySector) itr.next();
        flag = false;
        for (int i = 0; i < selSectors.length; i++) {

          if (asec.getSubsectorLevel1Id() == -1 && asec.getSectorId().equals(selSectors[i])&&asec.getConfigId().equals(Long.parseLong(configId))) {
            flag = true;
            break;
          }
          if (asec.getSubsectorLevel1Id() != -1 && asec.getSubsectorLevel2Id() == -1 && asec.getSubsectorLevel1Id().equals(selSectors[i])&&asec.getConfigId().equals(Long.parseLong(configId))) {
              flag = true;
              break;
            }
          if (asec.getSubsectorLevel1Id() != -1 && asec.getSubsectorLevel2Id() != -1 && asec.getSubsectorLevel2Id().equals(selSectors[i])&&asec.getConfigId().equals(Long.parseLong(configId))) {
              flag = true;
              break;
            }
        }
        if (!flag) {
          newSectors.add(asec);
        }
      }

      eaForm.setActivitySectors(newSectors);
      session.setAttribute("selectedSectorsForActivity", eaForm.getActivitySectors());
      return mapping.findForward("addActivityStep2");
    }
    //

    //we use this pointer to simplify adding items in the selectors:
    session.setAttribute("eaf", eaForm);
    String logframepr = (String) session.getAttribute("logframepr"); //logframepreview
    if (logframepr == null || logframepr == "")
      logframepr = "";
    if (logframepr.compareTo("true") == 0) {
      eaForm.setStep("9");
      eaForm.setPageId(1);
    }
    //===============Sectors END=============================


    //===============Componentes START=======================


    // Add componente
    if (request.getParameter("addComponente") != null) {
      ActivitySector selectedComponente = (ActivitySector) session.getAttribute("addComponente");
      if(selectedComponente==null) selectedComponente=new ActivitySector();
      session.removeAttribute("componenteSelected");

      Collection<ActivitySector> prevSelComponentes = eaForm.getActivityComponentes();



      boolean addComponente = true;
      if (prevSelComponentes != null) {
    	  Iterator<ActivitySector> itr = prevSelComponentes.iterator();
    	  while (itr.hasNext()) {
    		  ActivitySector asec =  itr.next();
	          if (asec.getSectorName().equals(selectedComponente.getSectorName())){
	        	  if (selectedComponente.getSubsectorLevel1Name() == null) {
	        		  addComponente = false;
						break;
	        	  }
	        	  if(asec.getSubsectorLevel1Name() != null ) {
						if(asec.getSubsectorLevel1Name().equals(selectedComponente.getSubsectorLevel1Name())){
							if(selectedComponente.getSubsectorLevel2Name() == null){
								addComponente = false;
							      break;
							}
							if(asec.getSubsectorLevel2Name() != null){
								if(asec.getSubsectorLevel2Name().equals(selectedComponente.getSubsectorLevel2Name())){
									addComponente = false;
							        break;
							 	}
							}else{
								addComponente = false;
						        break;
							}
						}
		          }else{
		        	  addComponente = false;
						break;
		          }
	          }
    	  }
      }
      if (addComponente) {
			if (prevSelComponentes != null) {
				if (prevSelComponentes.isEmpty())
					selectedComponente.setSectorPercentage(new Float(100));
				prevSelComponentes.add(selectedComponente);
			} else {
				selectedComponente.setSectorPercentage(new Float(100));
				prevSelComponentes = new ArrayList<ActivitySector>();
				prevSelComponentes.add(selectedComponente);
			}
      }

      eaForm.setActivityComponentes(prevSelComponentes);
      return mapping.findForward("addActivityStep2");
    }

    // Remove componentes
    else
    if (request.getParameter("remComponentes") != null) {
      Long selComponentes[] = eaForm.getSelActivityComponentes();
      Collection<ActivitySector> prevSelComponentes = eaForm.getActivityComponentes();
      Collection newComponentes = new ArrayList();

      Iterator<ActivitySector> itr = prevSelComponentes.iterator();

      boolean flag = false;

      while (itr.hasNext()) {
        ActivitySector asec =  itr.next();
        flag = false;
        for (int i = 0; i < selComponentes.length; i++) {

          if (asec.getSubsectorLevel1Id() == -1 && asec.getSectorId().equals(selComponentes[i])) {
            flag = true;
            break;
          }
          if (asec.getSubsectorLevel1Id() != -1 && asec.getSubsectorLevel2Id() == -1 && asec.getSubsectorLevel1Id().equals(selComponentes[i])) {
              flag = true;
              break;
            }
          if (asec.getSubsectorLevel1Id() != -1 && asec.getSubsectorLevel2Id() != -1 && asec.getSubsectorLevel2Id().equals(selComponentes[i])) {
              flag = true;
              break;
            }
        }
        if (!flag) {
        	newComponentes.add(asec);
        }
      }

      eaForm.setActivitySectors(newComponentes);
      return mapping.findForward("addActivityStep2");
    }

    //===============Componentes END=========================







    //eaForm.setAllComps(ActivityUtil.getAllComponentNames());
    ProposedProjCost propProjCost = null;
    if (eaForm.getProProjCost() != null) {
      propProjCost = eaForm.getProProjCost();
      if (propProjCost.getCurrencyCode() == null &&
          propProjCost.getFunAmount() == null &&
          propProjCost.getFunDate() == null) {
        eaForm.setProProjCost(null);
      }
    }

    try {

      if (!eaForm.isEditAct() || eaForm.isReset()) {
        eaForm.reset(mapping, request);
      }

      /*
       * The action 'AddAmpActivity' is used by 'Add Activity', 'View Channel Overview', and
       * 'show activity details' page. In the case if 'view channel overview', and
       * 'show activity details', we have to directly forward to the preview page.
       * A form bean variable called pageId is used for this purpose. All the requests
       * coming from pages other than 'Add activity' page will have a pageId value
       * which is greater than 1.
       */
      if (eaForm.getPageId() > 1)
        eaForm.setStep("9");

      // added by Akash
      // desc: clearing comment properties
      // start
      String action = request.getParameter("action");
      if (action != null && action.trim().length() != 0) {
        if ("create".equals(action)) {
          eaForm.getCommentsCol().clear();
          eaForm.setCommentFlag(false);
          eaForm.setProProjCost(null);
          eaForm.setActPrograms(null);
        }
      }
      // end

      Collection themes = new ArrayList();
      themes = ProgramUtil.getAllThemes();
      eaForm.setProgramCollection(themes);

      // added by Akash
      // desc: setting WorkingTeamLeadFlag & approval status in form bean
      // start
      boolean teamLeadFlag = false;
      boolean workingTeamFlag = true;
      if (teamMember != null) {
        Long ampTeamId = teamMember.getTeamId();
        teamLeadFlag = teamMember.getTeamHead();
        workingTeamFlag = TeamUtil.checkForParentTeam(ampTeamId);
      }

      if (teamLeadFlag && workingTeamFlag)
        eaForm.setWorkingTeamLeadFlag("yes");
      else
        eaForm.setWorkingTeamLeadFlag("no");
        eaForm.setTeamLead(teamLeadFlag);


      boolean activityApprovalStatusProcess=false;

      if (!eaForm.isEditAct() || logframepr.compareTo("true") == 0 || request.getParameter("logframe") != null) {
       if (teamMember != null)
        if ("true".compareTo((String) session.getAttribute("teamLeadFlag"))==0)
            eaForm.setApprovalStatus(org.digijava.module.aim.helper.Constants.APPROVED_STATUS);
          else
            {
        	  synchronized (ampContext) {
	        	  //ampContext=this.getServlet().getServletContext();
	        	  AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
	        	 // AmpModulesVisibility moduleToTest=FeaturesUtil.getModuleVisibility("Activity Approval Process");
	        	  AmpModulesVisibility moduleToTest=ampTreeVisibility.getModuleByNameFromRoot("Activity Approval Process");
	        	  if(moduleToTest!=null)
	          	  	activityApprovalStatusProcess= moduleToTest.isVisibleTemplateObj(ampTreeVisibility.getRoot());
	        	  if(activityApprovalStatusProcess==true ) eaForm.setApprovalStatus(org.digijava.module.aim.helper.Constants.STARTED_STATUS);
	        	  	else eaForm.setApprovalStatus(org.digijava.module.aim.helper.Constants.APPROVED_STATUS);
	        	  }
            }
      }
      else {
        String sessId = session.getId();
        ArrayList sessList = (ArrayList) ampContext.getAttribute(
            org.digijava.module.aim.helper.Constants.SESSION_LIST);
        if (sessList.contains(sessId) == false) {
          ActionErrors errors = new ActionErrors();
          errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
              "error.aim.activityAlreadyOpenedForEdit"));
          saveErrors(request, errors);

          String url = "/aim/viewChannelOverview.do?ampActivityId="
              + eaForm.getActivityId() + "&tabIndex=0";
          RequestDispatcher rd = getServlet().getServletContext()
              .getRequestDispatcher(url);
          rd.forward(request, response);
        }

        synchronized (ampContext) {
          HashMap tsList = (HashMap) ampContext.getAttribute(org.digijava.
              module.aim.helper.Constants.TS_ACT_LIST);
          if (tsList != null) {
            tsList.put(eaForm.getActivityId(),
                       new Long(System.currentTimeMillis()));
          }
          ampContext.setAttribute(org.digijava.module.aim.helper.Constants.
                                  TS_ACT_LIST, tsList);
        }
      }
      // end

      if (eaForm.getStep().equals("1")) { // show the step 1 page.

        if (eaForm.getContext() == null) {
          SiteDomain currentDomain = RequestUtils.getSiteDomain(request);

          String url = SiteUtils.getSiteURL(currentDomain, request.getScheme(),
                                            request.getServerPort(),
                                            request.getContextPath());
          eaForm.setContext(url);
        }

        /*
         * AMP uses the editor module of the DiGi java framework to store the description and
         * objectives in the html form. The editor module requires an entry in the DG_EDITOR table
         * for the fields which needs to be shown in html format. So a key is generated for both the
         * description and objective fields. The logic for generating the key for description is to
         * append teamMember id and the current time to the string "aim-desc". The logic for generating
         * key for objective is to append the team member id and the current time to the string "aim-obj".
         * Initially the contents for both the description and objectives are set as a blank string
         */
        // Creating a new entry in the DG_EDITOR table for description with the initial value for description as " "
        if (eaForm.getDescription() == null ||
            eaForm.getDescription().trim().length() == 0) {
          eaForm.setDescription("aim-desc-" + teamMember.getMemberId() + "-" +
                                System.currentTimeMillis());
          User user = RequestUtils.getUser(request);
          String currentLang = RequestUtils.getNavigationLanguage(request).
              getCode();
          String refUrl = RequestUtils.getSourceURL(request);
          String key = eaForm.getDescription();
          Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
              currentLang,
              refUrl,
              key,
              key,
              " ",
              null,
              request);
          ed.setLastModDate(new Date());
          ed.setGroupName(Constants.GROUP_OTHER);
          org.digijava.module.editor.util.DbUtil.saveEditor(ed);
        }

        //---
        if (eaForm.getPurpose() == null ||
            eaForm.getPurpose().trim().length() == 0) {
          eaForm.setPurpose("aim-purp-" + teamMember.getMemberId() + "-" +
                            System.currentTimeMillis());
          User user = RequestUtils.getUser(request);
          String currentLang = RequestUtils.getNavigationLanguage(request).
              getCode();
          String refUrl = RequestUtils.getSourceURL(request);
          String key = eaForm.getPurpose();
          Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
              currentLang,
              refUrl,
              key,
              key,
              " ",
              null,
              request);
          ed.setLastModDate(new Date());
          ed.setGroupName(Constants.GROUP_OTHER);
          org.digijava.module.editor.util.DbUtil.saveEditor(ed);
        }

//      ---
        if (eaForm.getLessonsLearned() == null ||
            eaForm.getLessonsLearned().trim().length() == 0) {
          eaForm.setLessonsLearned("aim-less-" + teamMember.getMemberId() + "-" +
                            System.currentTimeMillis());
          User user = RequestUtils.getUser(request);
          String currentLang = RequestUtils.getNavigationLanguage(request).
              getCode();
          String refUrl = RequestUtils.getSourceURL(request);
          String key = eaForm.getLessonsLearned();
          Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
              currentLang,
              refUrl,
              key,
              key,
              " ",
              null,
              request);
          ed.setLastModDate(new Date());
          ed.setGroupName(Constants.GROUP_OTHER);
          org.digijava.module.editor.util.DbUtil.saveEditor(ed);
        }
        eaForm.setProjectImpact(Util.initLargeTextProperty("aim-projimp-",eaForm.getProjectImpact(), request));
        eaForm.setActivitySummary(Util.initLargeTextProperty("aim-actsum-",eaForm.getActivitySummary(), request));
        eaForm.setContractingArrangements(Util.initLargeTextProperty("aim-contrarr-",eaForm.getContractingArrangements(), request));
        eaForm.setCondSeq(Util.initLargeTextProperty("aim-condseq-",eaForm.getCondSeq(), request));
        eaForm.setLinkedActivities(Util.initLargeTextProperty("aim-linkedact-",eaForm.getLinkedActivities(), request));
        eaForm.setConditionality(Util.initLargeTextProperty("aim-conditional-",eaForm.getConditionality(), request));
        eaForm.setProjectManagement(Util.initLargeTextProperty("aim-projmanag-",eaForm.getProjectManagement(), request));
        eaForm.setContractDetails(Util.initLargeTextProperty("aim-contrdetail-",eaForm.getContractDetails(), request));


        if (eaForm.getResults() == null ||
            eaForm.getResults().trim().length() == 0) {
          eaForm.setResults("aim-results-" + teamMember.getMemberId() + "-" +
                            System.currentTimeMillis());
          User user = RequestUtils.getUser(request);
          String currentLang = RequestUtils.getNavigationLanguage(request).
              getCode();
          String refUrl = RequestUtils.getSourceURL(request);
          String key = eaForm.getResults();
          Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
              currentLang,
              refUrl,
              key,
              key,
              " ",
              null,
              request);
          ed.setLastModDate(new Date());
          ed.setGroupName(Constants.GROUP_OTHER);
          org.digijava.module.editor.util.DbUtil.saveEditor(ed);
        }
        //---

        //---
        if (eaForm.getPurpose() == null ||
            eaForm.getPurpose().trim().length() == 0) {
          eaForm.setPurpose("aim-purp-" + teamMember.getMemberId() + "-" +
                            System.currentTimeMillis());
          User user = RequestUtils.getUser(request);
          String currentLang = RequestUtils.getNavigationLanguage(request).
              getCode();
          String refUrl = RequestUtils.getSourceURL(request);
          String key = eaForm.getPurpose();
          Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
              currentLang,
              refUrl,
              key,
              key,
              " ",
              null,
              request);
          ed.setLastModDate(new Date());
          ed.setGroupName(Constants.GROUP_OTHER);
          org.digijava.module.editor.util.DbUtil.saveEditor(ed);
        }

        if (eaForm.getResults() == null ||
            eaForm.getResults().trim().length() == 0) {
          eaForm.setResults("aim-results-" + teamMember.getMemberId() + "-" +
                            System.currentTimeMillis());
          User user = RequestUtils.getUser(request);
          String currentLang = RequestUtils.getNavigationLanguage(request).
              getCode();
          String refUrl = RequestUtils.getSourceURL(request);
          String key = eaForm.getResults();
          Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
              currentLang,
              refUrl,
              key,
              key,
              " ",
              null,
              request);
          ed.setLastModDate(new Date());
          ed.setGroupName(Constants.GROUP_OTHER);
          org.digijava.module.editor.util.DbUtil.saveEditor(ed);
        }
        //---

        // Creating a new entry in the DG_EDITOR table for objective with the initial value for objective as " "
        if (eaForm.getObjectives() == null ||
            eaForm.getObjectives().trim().length() == 0) {
          eaForm.setObjectives("aim-obj-" + teamMember.getMemberId() + "-" +
                               System.currentTimeMillis());
          User user = RequestUtils.getUser(request);
          String currentLang = RequestUtils.getNavigationLanguage(request).
              getCode();
          String refUrl = RequestUtils.getSourceURL(request);
          String key = eaForm.getObjectives();
          Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
              currentLang,
              refUrl,
              key,
              key,
              " ",
              null,
              request);
          ed.setLastModDate(new Date());
          ed.setGroupName(Constants.GROUP_OTHER);
          org.digijava.module.editor.util.DbUtil.saveEditor(ed);
        }

        // Exactly as description/objectives, AMP is using DigiJava's document
        // management module to store documents (if enabled). Before storing
        // Documents, we need to create space there.
        // Later, we will give to space user-friendly name
        if (DocumentUtil.isDMEnabled()) {
          if (eaForm.getDocumentSpace() == null ||
              eaForm.getDocumentSpace().trim().length() == 0) {
            eaForm.setDocumentSpace("aim-document-space-" +
                                    teamMember.getMemberId() +
                                    "-" + System.currentTimeMillis());
            Site currentSite = RequestUtils.getSite(request);
            DocumentUtil.createDocumentSpace(currentSite,
                                             eaForm.getDocumentSpace());
          }
        }
        eaForm.setReset(false);

        // loading Activity Rank collection
        if (null == eaForm.getActRankCollection()) {
          eaForm.setActRankCollection(new ArrayList());
          for (int i = 1; i < 6; i++)
            eaForm.getActRankCollection().add(new Integer(i));
        }

        if (eaForm.getCosts() != null && eaForm.getCosts().size() != 0) {
          double grandCost = 0;
          double grandContribution = 0;
          Long currencyId = teamMember.getAppSettings().getCurrencyId();
          Iterator i = eaForm.getCosts().iterator();
          while (i.hasNext()) {
            EUActivity element = (EUActivity) i.next();
            element.setDesktopCurrencyId(currencyId);
            grandCost += element.getTotalCostConverted();
            grandContribution += element.getTotalContributionsConverted();
          }
          eaForm.setOverallCost(new Double(grandCost));
          eaForm.setOverallContribution(new Double(grandContribution));

        }

//			Collection statusCol = null; TO BE DELETED
//			// load the status from the database
//			if(eaForm.getStatusCollection() == null) {
//				statusCol= DbUtil.getAmpStatus();
//				eaForm.setStatusCollection(statusCol);
//			}
//			else {
//				statusCol = eaForm.getStatusCollection();
//			}
        // Initially setting the implementation level as "country"
        /*
         * Removed As asked in AMP-2889

        if (eaForm.getImplemLocationLevel() == null)
          eaForm.setImplemLocationLevel(
              CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.
              IMPLEMENTATION_LOCATION_KEY, new Long(0)).getId()
              );
		*/
      	//get all possible refdoc names from categories
      	Collection<AmpCategoryValue> catValues=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.REFERENCE_DOCS_KEY,false);

    	if (catValues!=null && eaForm.getReferenceDocs()==null){
        	List<ReferenceDoc> refDocs=new ArrayList<ReferenceDoc>();
    		Collection<AmpActivityReferenceDoc> activityRefDocs=null;
    		Map<Long, AmpActivityReferenceDoc> categoryRefDocMap=null;

    		if (eaForm.getActivityId()!=null){
        		//get list of ref docs for activity
    			activityRefDocs=ActivityUtil.getReferenceDocumentsFor(eaForm.getActivityId());
            	//create map where keys are category value ids.
    			categoryRefDocMap = AmpCollectionUtils.createMap(
    					activityRefDocs,
    					new ActivityUtil.CategoryIdRefDocMapBuilder());
    		}

        	//create arrays, number of elements as much as category values
        	Long[] refdocIds=new Long[catValues.size()];
        	String[] refdocComments=new String[catValues.size()];

        	int c=0;
        	int selectedIds=0;
        	for(AmpCategoryValue catVal: catValues){
        		AmpActivityReferenceDoc refDoc=(categoryRefDocMap==null)?null:categoryRefDocMap.get(catVal.getId());
        		ReferenceDoc doc=new ReferenceDoc();
        		doc.setCategoryValueId(catVal.getId());
        		doc.setCategoryValue(catVal.getValue());
        		if (refDoc==null){
        			refdocComments[c]="";
        			doc.setComment("");
        			doc.setChecked(false);
        		}else{
        			refdocIds[selectedIds++]=refDoc.getCategoryValue().getId();
        			refdocComments[c]=refDoc.getComment();
        			doc.setComment(refDoc.getComment());
        			doc.setRefDocId(refDoc.getId());
        			doc.setChecked(true);
        		}
        		refDocs.add(doc);
        		c++;
        	}

        	//set selected ids
        	eaForm.setAllReferenceDocNameIds(refdocIds);
        	//set all comments, some are empty
//        	eaForm.setRefDocComments(refdocComments);

        	eaForm.setReferenceDocs(refDocs);

    	}







        // load the modalities from the database
        /*if (eaForm.getModalityCollection() == null) { // no longer necessary since they are in Category Manager
         modalColl = DbUtil.getAmpModality();
         eaForm.setModalityCollection(modalColl);
            } else {
         modalColl = eaForm.getModalityCollection();
            }*/

        // Initally set the modality as "Project Support"
        Collection financingInstrValues = CategoryManagerUtil.
            getAmpCategoryValueCollectionByKey(CategoryConstants.
                                               FINANCING_INSTRUMENT_KEY, null);
        Iterator iter = financingInstrValues.iterator();
        while (iter.hasNext()) {
          AmpCategoryValue financingInstrVal = (AmpCategoryValue) iter.next();
          if(financingInstrVal!=null)
        	  if ("Project Support".equalsIgnoreCase(financingInstrVal.getValue())) {
        		  eaForm.setModality(financingInstrVal.getId());
        	  }
        }
        /*if (modalColl != null && eaForm.getModality() == null) {
         Iterator itr = modalColl.iterator();
         while (itr.hasNext()) {
          AmpModality mod = (AmpModality) itr.next();
          if (mod.getName().equalsIgnoreCase("Project Support")) {
           eaForm.setModality(mod.getAmpModalityId());
           break;
          }
         }
            }*/
        Collection levelCol = null;
        // Loading the levels from the database
        /*			if (eaForm.getLevelCollection() == null) { //not necessary anymore. They are in Category Manager.
            levelCol = DbUtil.getAmpLevels();
            eaForm.setLevelCollection(levelCol);
           } else {
            levelCol = eaForm.getLevelCollection();
           }*/

        // load all themes

        //eaForm.setProgramCollection(ProgramUtil.getAllThemes());

        // load all the active currencies
        eaForm.setCurrencies(CurrencyUtil.getAmpCurrency());

        eaForm.setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false));

        // load all the perspectives
        eaForm.setPerspectives(DbUtil.getAmpPerspective());

        eaForm.setFundingRegionId(new Long( -1));
        return mapping.findForward("addActivityStep1");
      }
      else if (eaForm.getStep().equals("1.1")) { // shows the edit page of the editor module
        eaForm.setStep("1");
        // When the contents are saved the editor module redirects to the url specified in the 'referrer' parameter
        String url = "/editor/showEditText.do?id=" + eaForm.getEditKey() +"&lang="+RequestUtils.
                        getNavigationLanguage(request).
                        getCode()+
            "&referrer=" + eaForm.getContext() +
            "/aim/addActivity.do?edit=true";
        response.sendRedirect(eaForm.getContext() + url);
      }
      else if (eaForm.getStep().equals("1_5")) { // show the 'Refernces' step page.
          return mapping.findForward("addActivityStep1_5");
      }
      else if (eaForm.getStep().equals("2.2")) { // shows the edit page of the editor module
          eaForm.setStep("2");
          // When the contents are saved the editor module redirects to the url specified in the 'referrer' parameter
          String url = "/editor/showEditText.do?id=" + eaForm.getEditKey() +"&lang="+RequestUtils.
                        getNavigationLanguage(request).
                        getCode()+
              "&referrer=" + eaForm.getContext() +
              "/aim/addActivity.do?edit=true";
          response.sendRedirect(eaForm.getContext() + url);
        }
      else if (eaForm.getStep().equals("2")) { // show the step 2 page.
    	  if (eaForm.getContext() == null) {
              SiteDomain currentDomain = RequestUtils.getSiteDomain(request);

              String url = SiteUtils.getSiteURL(currentDomain, request.getScheme(),
                                                request.getServerPort(),
                                                request.getContextPath());
              eaForm.setContext(url);
            }

    	  if (eaForm.getEqualOpportunity() == null ||
    	            eaForm.getEqualOpportunity().trim().length() == 0) {
    	          eaForm.setEqualOpportunity("aim-eo-" + teamMember.getMemberId() + "-" +
    	                               System.currentTimeMillis());
    	          setEditorKey(eaForm.getEqualOpportunity(), request);
    	        }
    	  if (eaForm.getEnvironment() == null ||
  	            eaForm.getEnvironment().trim().length() == 0) {
  	          eaForm.setEnvironment("aim-env-" + teamMember.getMemberId() + "-" +
  	                               System.currentTimeMillis());
  	          setEditorKey(eaForm.getEnvironment(), request);
  	        }
    	  if (eaForm.getMinorities() == null ||
  	            eaForm.getMinorities().trim().length() == 0) {
  	          eaForm.setMinorities("aim-min-" + teamMember.getMemberId() + "-" +
  	                               System.currentTimeMillis());
  	          setEditorKey(eaForm.getMinorities(), request);
  	        }

        return mapping.findForward("addActivityStep2");
      }
      else if (eaForm.getStep().equals("3")) { // show the step 3 page.
        return mapping.findForward("addActivityStep3");
      }
      else if (eaForm.getStep().equals("4")) { // show the step 4 page.
        return mapping.findForward("addActivityStep4");
      }
      else if (eaForm.getStep().equals("5")) { // show the step 5 page.
        return mapping.findForward("addActivityStep5");
      }
      else if (eaForm.getStep().equals("6")) { // show the step 6 page.
        return mapping.findForward("addActivityStep6");
      }
      else if (eaForm.getStep().equals("7")) { // show the step 7 page.
        return mapping.findForward("addActivityStep7");
      }
      else if (eaForm.getStep().equals("8")) { // show the step 7 page.
        return mapping.findForward("addActivityStep8");
      }
      else if (eaForm.getStep().equals("11")) { // show the step 11 page.
        return mapping.findForward("addActivityStep11");
      }
      else if (eaForm.getStep().equals("12")) { // show the step 12 page.
	        return mapping.findForward("addActivityStep12");
	      }
       else if (eaForm.getStep().equals("13")) { // show the step 13 page.
          return mapping.findForward("addActivityStep13");
      }
      else if (eaForm.getStep().equals("9")) { // show the preview page.

 //       if (eaForm.getAmpId() == null ) { // if AMP-ID is not generated, generate the AMP-ID
          /*
           * The logic for geerating the AMP-ID is as follows:
           * 1. get default global country code
           * 2. Get the maximum of the ampActivityId + 1, MAX_NUM
           * 3. merge them
           */
//          String ampId =
//              FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.
//                                                 Constants.
//                                                 GLOBAL_DEFAULT_COUNTRY).
//              toUpperCase();
          /*if (eaForm.getFundingOrganizations() != null) {
           if (eaForm.getFundingOrganizations().size() == 1) {
            Iterator itr = eaForm.getFundingOrganizations().iterator();
            if (itr.hasNext()) {
             FundingOrganization fOrg = (FundingOrganization) itr
               .next();
           ampId += "-" + DbUtil.getOrganisation(fOrg.getAmpOrgId()).getOrgCode();
            }
           }
               }*/

//          long maxId = ActivityUtil.getActivityMaxId();
//          maxId++;
//          ampId += "/" + maxId;
//          eaForm.setAmpId(ampId);
//        	User user= RequestUtils.getUser(request);
//        	if(eaForm.getActivityId()!=null){
//        		eaForm.setAmpId(ActivityUtil.generateAmpId(user,eaForm.getActivityId()));
//        	}else {
//        		eaForm.setAmpId(ActivityUtil.generateAmpId(user,ActivityUtil.getActivityMaxId()+1));
//        	}
//
//        }

        /*
         * If the mode is 'Add', set the Activity Creator as the current logged in user
         */
        if (eaForm.getIsPreview() != 1) {
          if (teamMember != null && (!eaForm.isEditAct()) &&
              (eaForm.getActAthEmail() == null ||
               eaForm.getActAthEmail().trim().length() == 0)) {
            User usr = DbUtil.getUser(teamMember.getEmail());
            if (usr != null) {
              eaForm.setActAthFirstName(usr.getFirstNames());
              eaForm.setActAthLastName(usr.getLastName());
              eaForm.setActAthEmail(usr.getEmail());
              eaForm.setActAthAgencySource(usr.getOrganizationName());

            }
          }

        }
        else {
          AmpActivity activity = ActivityUtil.getAmpActivity(eaForm.
              getActivityId());
          if("edit".equals(action)) {
          	//check if we have edit permissin for this activity
          	Long ampActivityId=Long.parseLong(request.getParameter("ampActivityId"));

          }



    	if (activity.getActivityCreator() != null) {
            eaForm.setActAthFirstName(activity.getActivityCreator().getUser().
                                      getFirstNames());
            eaForm.setActAthLastName(activity.getActivityCreator().getUser().
                                     getLastName());
            eaForm.setActAthEmail(activity.getActivityCreator().getUser().
                                  getEmail());
            eaForm.setActAthAgencySource(activity.getActivityCreator().getUser().
                                         getOrganizationName());
          }
          eaForm.setIsPreview(0);
        }



Collection<AmpCategoryValue> catValues=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.REFERENCE_DOCS_KEY,false);

    	if (catValues!=null && eaForm.getReferenceDocs()==null){
        	List<ReferenceDoc> refDocs=new ArrayList<ReferenceDoc>();
    		Collection<AmpActivityReferenceDoc> activityRefDocs=null;
    		Map<Long, AmpActivityReferenceDoc> categoryRefDocMap=null;

    		if (eaForm.getActivityId()!=null){
        		//get list of ref docs for activity
    			activityRefDocs=ActivityUtil.getReferenceDocumentsFor(eaForm.getActivityId());
            	//create map where keys are category value ids.
    			categoryRefDocMap = AmpCollectionUtils.createMap(
    					activityRefDocs,
    					new ActivityUtil.CategoryIdRefDocMapBuilder());
    		}

        	//create arrays, number of elements as much as category values
        	Long[] refdocIds=new Long[catValues.size()];
        	String[] refdocComments=new String[catValues.size()];

        	int c=0;
        	int selectedIds=0;
        	for(AmpCategoryValue catVal: catValues){
        		AmpActivityReferenceDoc refDoc=(categoryRefDocMap==null)?null:categoryRefDocMap.get(catVal.getId());
        		ReferenceDoc doc=new ReferenceDoc();
        		doc.setCategoryValueId(catVal.getId());
        		doc.setCategoryValue(catVal.getValue());
        		if (refDoc==null){
        			refdocComments[c]="";
        			doc.setComment("");
        			doc.setChecked(false);
        		}else{
        			refdocIds[selectedIds++]=refDoc.getCategoryValue().getId();
        			refdocComments[c]=refDoc.getComment();
        			doc.setComment(refDoc.getComment());
        			doc.setRefDocId(refDoc.getId());
        			doc.setChecked(true);
        		}
        		refDocs.add(doc);
        		c++;
        	}

        	//set selected ids
        	eaForm.setAllReferenceDocNameIds(refdocIds);
        	//set all comments, some are empty
//        	eaForm.setRefDocComments(refdocComments);

        	eaForm.setReferenceDocs(refDocs);

    	}



        Collection euActs = EUActivityUtil.getEUActivities(eaForm.getActivityId());
        // EUActivities = same as Costs
        request.setAttribute("costs", euActs);
        request.setAttribute("actId", eaForm.getActivityId());
        int risk = MEIndicatorsUtil.getOverallRisk(eaForm.getActivityId());
        String riskName = MEIndicatorsUtil.getRiskRatingName(risk);
        String rskColor = MEIndicatorsUtil.getRiskColor(risk);
        request.setAttribute("overallRisk", riskName);
        request.setAttribute("riskColor", rskColor);

        Long prev = new Long( -1);
        Long next = new Long( -1);
        if (eaForm.getActivityId() != null) {
          ReportData rep = (ReportData) session.getAttribute("report");
          if (rep != null) {
            Collection ids = (Collection) rep.getOwnerIds();
            Iterator it = ids.iterator();

            while (it.hasNext()) {
              Long el = (Long) it.next();
              if (el.compareTo(eaForm.getActivityId()) == 0) {
                if (it.hasNext())
                  next = new Long( ( (Long) it.next()).longValue());
                break;
              }
              prev = new Long(el.longValue());
            }
          }
          request.setAttribute("nextId", next);
          request.setAttribute("prevId", prev);
        }

//			if(eaForm.getStatusCollection() == null) { //TO BE DELETED
//				eaForm.setStatusCollection(DbUtil.getAmpStatus());
//			}

        /*if (eaForm.getModalityCollection() == null) { //No longer needed. It is in category Manager.
         eaForm.setModalityCollection(DbUtil.getAmpModality());
            }
         */
        if (eaForm.getLevelCollection() == null) {
          eaForm.setLevelCollection(DbUtil.getAmpLevels());
        }

//      patch for comments that were not saved yet
        boolean currentlyEditing	= false;
        if( request.getParameter("currentlyEditing")!=null && request.getParameter("currentlyEditing").equals("true")) {
        	currentlyEditing		= true;
        }

        HashMap unsavedComments = (HashMap) session.getAttribute("commentColInSession");
        Set keySet = null;
        if (unsavedComments != null)
        	keySet = unsavedComments.keySet();


        if (teamMember == null)
          return mapping.findForward("publicPreview");
        else {
          ArrayList<AmpComments> colAux	= null;
          Collection ampFields 			= DbUtil.getAmpFields();
          HashMap allComments 			= new HashMap();

          for (Iterator itAux = ampFields.iterator(); itAux.hasNext(); ) {
            AmpField field = (AmpField) itAux.next();
            if ( currentlyEditing && keySet!=null && keySet.contains(field.getAmpFieldId()) ) {
            	colAux							= new ArrayList<AmpComments>();
            	Collection<AmpComments> toAdd 	= (Collection) unsavedComments.get(field.getAmpFieldId());
            	colAux.addAll( toAdd );
            }
            else {
            	colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(),
                                                  eaForm.getActivityId());
            }
            // patch for comments that were not saved yet
            /*if (keySet != null && keySet.contains(field.getAmpFieldId())){
            	Collection toAdd = (Collection) unsavedComments.get(field.getAmpFieldId());
            	Iterator i = toAdd.iterator();
            	while (i.hasNext()) {
					AmpComments e1 = (AmpComments) i.next();
					boolean found = false;
					Iterator j = colAux.iterator();
					while (j.hasNext()) {
						AmpComments e2 = (AmpComments) j.next();
						if (e1.getAmpCommentId() != null && e1.getAmpCommentId().equals(e2.getAmpCommentId())){
							found = true;
							break;
						}
					}
					if (found){
						j.remove();
					}
					colAux.add(e1);
				}
            }*/
            //
            allComments.put(field.getFieldName(), colAux);
          }

          eaForm.setAllComments(allComments);
          //eaForm.setCommentsCol(colAux);

          if (request.getParameter("logframe") != null || logframepr.compareTo("true") == 0) {
//            eaForm.setIndicatorsME(IndicatorUtil.getActivityIndicatorsList(eaForm.getActivityId()));
            eaForm.setIndicatorsME(IndicatorUtil.getActivityIndicatorHelperBeans(eaForm.getActivityId()));
            if (!eaForm.isEditAct()) {
              eaForm.setIndicatorId(null);
              eaForm.setIndicatorValId(null);
              eaForm.setExpIndicatorId(null);
              eaForm.setBaseVal(null);
              eaForm.setBaseValDate(null);
              eaForm.setTargetVal(null);
              eaForm.setTargetValDate(null);
              eaForm.setRevTargetVal(null);
              eaForm.setRevTargetValDate(null);
              eaForm.setIndicatorPriorValues(null);
              eaForm.setCurrentVal(null);
              eaForm.setCurrentValDate(null);
              eaForm.setIndicatorRisk(null);

            }

            //get the levels of risks

            Long defaultCurrency=teamMember.getAppSettings().getCurrencyId();
	        double allCosts=0;
	        if(eaForm.getCosts() != null)
	        	for(Iterator it=eaForm.getCosts().iterator();it.hasNext();)
	        	{
	        		EUActivity euAct=(EUActivity) it.next();
	        		euAct.setDesktopCurrencyId(defaultCurrency);
	        		allCosts+=euAct.getTotalCostConverted();
	        	}
            eaForm.setAllCosts(new Double(allCosts));
            if ((eaForm.getIndicatorsME() != null) && (!eaForm.getIndicatorsME().isEmpty()))
              eaForm.setRiskCollection(MEIndicatorsUtil.getAllIndicatorRisks());
            return mapping.findForward("previewLogframe");
          }
          /* Setting documents for preview */
          Collection rlList	= eaForm.getDocumentList();
          eaForm.setDocuments( new ArrayList<Documents>() );
          if (rlList != null ) {
        	  Iterator iter 		= rlList.iterator();
        	  if(iter.hasNext())
        	  {
        		  RelatedLinks rl		= (RelatedLinks) iter.next();
        		  CMSContentItem item	= rl.getRelLink();
        		  if ( item != null ) {
        			  eaForm.getDocuments().add( createHelperDocument(item, null, null) );
        		  }
        	  }
          }
          eaForm.setCrDocuments( DocumentManagerUtil.createDocumentDataCollectionFromSession(request) );

          /* END - Setting documents for preview */
          return mapping.findForward("preview");
        }
      }
      else if (eaForm.getStep().equals("10")) { // show step 9 - M&E page

//          eaForm.setIndicatorsME(IndicatorUtil.getActivityIndicatorsList(eaForm.getActivityId()));
    	  if (eaForm.getActivityId()!=null && eaForm.getActivityId()!=0){
              eaForm.setIndicatorsME(IndicatorUtil.getActivityIndicatorHelperBeans(eaForm.getActivityId()));
    	  }else{
    		  eaForm.setIndicatorsME(null);
    	  }

          for(Iterator itr = IndicatorUtil.getAllDefaultIndicators(eaForm.getActivityId()).iterator(); itr.hasNext();){
          	ActivityIndicator actInd = (ActivityIndicator) itr.next();
          	actInd.setActivityId(eaForm.getActivityId());
             eaForm.getIndicatorsME().add(actInd);
          }
          if (!eaForm.isEditAct()) {
            eaForm.setIndicatorId(null);
            eaForm.setIndicatorValId(null);
            eaForm.setExpIndicatorId(null);
            eaForm.setBaseVal(null);
            eaForm.setBaseValDate(null);
            eaForm.setTargetVal(null);
            eaForm.setTargetValDate(null);
            eaForm.setRevTargetVal(null);
            eaForm.setRevTargetValDate(null);
            eaForm.setIndicatorPriorValues(null);
            eaForm.setCurrentVal(null);
            eaForm.setCurrentValDate(null);
            eaForm.setIndicatorRisk(null);
          }

          //get the levels of risks
          if (eaForm.getIndicatorsME()!=null && !eaForm.getIndicatorsME().isEmpty())
            eaForm.setRiskCollection(MEIndicatorsUtil.getAllIndicatorRisks());

          return mapping.findForward("addActivityStep10");
      }
      else {
        return mapping.findForward("adminHome");
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
      return null;
    }
    return null;
  }

  public static Documents createHelperDocument (CMSContentItem cmsItem, Long activityId, String activityName) {
		Documents document = new Documents();
      document.setActivityId( activityId );
      document.setActivityName( activityName );
      document.setDocId(new Long(cmsItem.getId()));
      document.setTitle(cmsItem.getTitle());
      document.setIsFile(cmsItem.getIsFile());
      document.setFileName(cmsItem.getFileName());
      document.setUrl(cmsItem.getUrl());
      document.setDocDescription(cmsItem.getDescription());
      document.setDate(cmsItem.getDate());
      if (cmsItem.getDocType() != null)
      	document.setDocType(cmsItem.getDocType().getValue());

      if (cmsItem.getDocLanguage() != null)
      	document.setDocLanguage( cmsItem.getDocLanguage().getValue() );
      document.setDocComment( cmsItem.getDocComment() );

      return document;
	}

  public void setEditorKey(String s, HttpServletRequest request)
  {
	  User user = RequestUtils.getUser(request);
      String currentLang = RequestUtils.getNavigationLanguage(request).
          getCode();
      String refUrl = RequestUtils.getSourceURL(request);
      String key = s;
      Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
          currentLang,
          refUrl,
          key,
          key,
          " ",
          null,
          request);
      ed.setLastModDate(new Date());
      ed.setGroupName(Constants.GROUP_OTHER);
      try {
		org.digijava.module.editor.util.DbUtil.saveEditor(ed);
	} catch (EditorException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
}
  }
}

class ProgramComparator
    implements Comparator {
  public int compare(Object o1, Object o2) {
    AmpTheme i1 = (AmpTheme) o1;
    AmpTheme i2 = (AmpTheme) o2;

    Long sk1 = i1.getAmpThemeId();
    Long sk2 = i2.getAmpThemeId();

    return sk1.compareTo(sk2);
  }
}

class HierarchicalDefinition
    implements HierarchyDefinition {
  public Object getObjectIdentity(Object object) {
    AmpTheme i = (AmpTheme) object;
    return i.getAmpThemeId();

  }

  public Object getParentIdentity(Object object) {
    AmpTheme i = (AmpTheme) object;
    if (i.getParentThemeId() == null) {
      return null;
    }
    else {
      return i.getParentThemeId().getAmpThemeId();
    }
  }
}
