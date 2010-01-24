/*
 * UpdateWorkspace.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.UpdateWorkspaceForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

public class UpdateWorkspace extends Action {

	private static Logger logger = Logger.getLogger(UpdateWorkspace.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

            boolean permitted = false;
            HttpSession session = request.getSession();
            if(session.getAttribute("ampAdmin") != null) {
                String key = (String) session.getAttribute("ampAdmin");
                if(key.equalsIgnoreCase("yes")) {
                    permitted = true;
                } else {
                    if(session.getAttribute("teamLeadFlag") != null) {
                        key = (String) session.getAttribute("teamLeadFlag");
                        if(key.equalsIgnoreCase("true")) {
                            permitted = true;
                        }
                    }
                }
            }
            if(!permitted) {
                return mapping.findForward("index");
            }
            if(session.getAttribute("ampWorkspaces") != null) {
                session.removeAttribute("ampWorkspaces");
            }

            UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;

            String event = request.getParameter("event");
            String dest = request.getParameter("dest");
            String tId1 = request.getParameter("tId");
            	
            ////System.out.println("Am primit parametrul " + tId1 +" *****************************8");
            logger.info("event : " + event + " dest : " + dest);
            ActionErrors errors = new ActionErrors();
            if(uwForm.getWorkspaceType()!=null &&  "Team".compareTo(uwForm.getWorkspaceType()) ==0 )
            {
            	if( (uwForm.getComputation()!=null && uwForm.getComputation()==true)  && (uwForm.getOrganizations()==null || uwForm.getOrganizations().size()==0)){
	            	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.updateWorkspace.noChildOrg"));
	            	saveErrors(request, errors);   
	            	return mapping.getInputForward();
	            }
            	uwForm.setChildWorkspaces(new ArrayList());
            	if(uwForm.getComputation()==null || uwForm.getComputation()==false) 
            		{
            			uwForm.setAddActivity(true);
            			uwForm.setOrganizations(null);
            		}
			}
            if(uwForm.getWorkspaceType()!=null && "Management".compareTo(uwForm.getWorkspaceType()) == 0 )
           	{
           		uwForm.setOrganizations(new ArrayList());
           		uwForm.setComputation(null);
           		uwForm.setAddActivity(null);
           	}
            
            AmpTeam newTeam = null;
            newTeam = null;
            if(uwForm.getTeamName() != null) {
                newTeam = new AmpTeam();
                newTeam.setName(uwForm.getTeamName());
                newTeam.setTeamCategory(uwForm.getCategory());
                newTeam.setAccessType(uwForm.getWorkspaceType());
                newTeam.setAddActivity(uwForm.getAddActivity());
                newTeam.setComputation(uwForm.getComputation());

                // newTeam.setType(typeCategoryValue);
                if(uwForm.getOrganizations()!=null) 
                	{
                	TreeSet s=new TreeSet();
                	s.addAll(uwForm.getOrganizations());
                	newTeam.setOrganizations(s);
                	}
                if(null == uwForm.getRelatedTeam()
                   || "-1".equals(uwForm.getRelatedTeam().toString()
                                  .trim()))
                    newTeam.setRelatedTeamId(null);
                else
                    newTeam.setRelatedTeamId(TeamUtil.getAmpTeam(uwForm
                        .getRelatedTeam()));
                if(uwForm.getDescription() != null
                   && uwForm.getDescription().trim().length() > 0) {
                    newTeam.setDescription(uwForm.getDescription());
                } else {
                    newTeam.setDescription(" ");
                }
            }

            if(event != null && event.trim().equalsIgnoreCase("reset")) {
        		uwForm.setPopupReset(false);
                uwForm.setTeamName("");
                uwForm.setCategory("");
                uwForm.setTypeId(new Long(0));
                uwForm.setDescription("");
                uwForm.setWorkspaceType("");
                uwForm.setRelatedTeamFlag("no");
                uwForm.setRelatedTeamName("");
                uwForm.setAddActivity(null);
                uwForm.setComputation(null);
                if (uwForm.getChildWorkspaces() != null)
                	uwForm.getChildWorkspaces().clear();
                return mapping.findForward("admin");
            }
            else
            if(event != null && event.trim().equalsIgnoreCase("add")) {
                uwForm.setActionEvent("add");
                if(newTeam != null) {
                	if(uwForm.getChildWorkspaces()==null && uwForm.getWorkspaceType().compareTo("Management")==0)
                    {
                 	   errors
                        .add(
                            ActionErrors.GLOBAL_ERROR,
                            new ActionError(
                                "error.aim.updateWorkspace.noManagementChildSelected"));
                    saveErrors(request, errors);
                    logger
                        .debug(
                        "error.aim.updateWorkspace.noManagementChildSelected !!!!!");
                    return mapping.getInputForward();
                    }

                    boolean teamExist = TeamUtil.createTeam(newTeam, uwForm
                        .getChildWorkspaces());
                    if(teamExist) {
                        errors
                            .add(
                                ActionErrors.GLOBAL_ERROR,
                                new ActionError(
                                    "error.aim.updateWorkspace.teamNameAlreadyExist"));
                        saveErrors(request, errors);
                        logger
                            .debug(
                            "Team name already exist. Error message saved to request");
                        return mapping.getInputForward();
                    }
                }
            } else if(event != null && event.trim().equalsIgnoreCase("edit")) {
                uwForm.setActionEvent("edit");
                if(newTeam != null) {
                	if(uwForm.getChildWorkspaces().size()==0 && uwForm.getWorkspaceType().compareTo("Management")==0)
                    {
                 		errors
                        .add(
                            ActionErrors.GLOBAL_ERROR,
                            new ActionError(
                                "error.aim.updateWorkspace.noManagementChildSelected"));
                    saveErrors(request, errors);
                    logger
                        .debug(
                        "error.aim.updateWorkspace.noManagementChildSelected !!!!!");
                    return mapping.getInputForward();
                    }
                	if (tId1==null)	newTeam.setAmpTeamId(uwForm.getTeamId());
                	else newTeam.setAmpTeamId(new Long(Long.parseLong(tId1)));
                    if(newTeam.getAccessType().equalsIgnoreCase("Team"))// && (uwForm.getChildWorkspaces() != null && uwForm.getChildWorkspaces().size() > 0)) {
                    {	uwForm.setChildWorkspaces(new ArrayList());
                        //errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.updateWorkspace.childTeamsExistForTeam"));
                        //saveErrors(request, errors);
                      //  return mapping.getInputForward();
                    }
                   
                    
                    if(uwForm.getOrganizations()!=null) 
                	{
                	TreeSet s=new TreeSet();
                	s.addAll(uwForm.getOrganizations());
                	newTeam.setOrganizations(s);
                	}
        
                    boolean teamExist = TeamUtil.updateTeam(newTeam, uwForm
                        .getChildWorkspaces());
                    if(teamExist) {
                        errors
                            .add(
                                ActionErrors.GLOBAL_ERROR,
                                new ActionError(
                                    "error.aim.updateWorkspace.teamNameAlreadyExist"));
                        saveErrors(request, errors);
                        logger
                            .debug(
                            "Team name already exist. Error message saved to request");
                        return mapping.getInputForward();
                    } else {
                        uwForm.setUpdateFlag(true);
                    }
                    TeamMember tm = (TeamMember) session
                        .getAttribute("currentMember");
                    if(tm != null) {
                        if(tm.getTeamId() != null) {
                            session.removeAttribute("currentMember");
                            tm.setTeamName(newTeam.getName());
                            session.setAttribute("currentMember", tm);
                            PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, tm);
                        }
                    }return mapping.findForward("forward");
                }
            } else if(event != null && event.trim().equalsIgnoreCase("delete")) {
                String tId = request.getParameter("tId");
                Long teamId = new Long(Long.parseLong(tId));
                boolean memExist = TeamUtil.membersExist(teamId);
                boolean actExist = TeamUtil.teamHasActivities(teamId);

                if(memExist) {
                	errors = new ActionErrors();
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                        "error.aim.membersExistForTeam"));
                    saveErrors(request, errors);
                
                  return mapping.findForward("forward");
                } 
                if(actExist) {
                	errors = new ActionErrors();
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                        "error.aim.activitiesExistForTeam"));
                    saveErrors(request, errors);
                
                  return mapping.findForward("forward");
                } 
                TeamUtil.removeTeam(teamId);
            }
            uwForm.setReset(true);
            uwForm.reset(mapping, request);

            return mapping.findForward("forward");
        }
}
