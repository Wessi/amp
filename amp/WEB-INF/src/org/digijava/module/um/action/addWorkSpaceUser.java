package org.digijava.module.um.action;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.form.AddUserForm;

public class addWorkSpaceUser extends Action{
	private static Logger logger = Logger.getLogger(addWorkSpaceUser.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {
		
		AddUserForm upMemForm = (AddUserForm) form;
		ActionErrors errors = new ActionErrors();
		logger.debug("In add members");
		
		String actionFlag = request.getParameter("actionFlag");
		logger.debug("actionFlag: " + actionFlag);

		if ("deleteWS".equals(actionFlag)) {
            logger.debug("In delete team member");
            Long selMembers[] = new Long[1];
            Long id = upMemForm.getTeamMemberId();
            selMembers[0] = id;
            Site site = RequestUtils.getSite(request);
            TeamMemberUtil.removeTeamMembers(selMembers);
            Collection asWS = upMemForm.getAssignedWorkspaces();
            for(java.util.Iterator it = asWS.iterator(); it.hasNext(); ){
            	AmpTeamMember newMember = (AmpTeamMember)it.next();
            	if(newMember.getAmpTeamMemId().compareTo(id)==0){
            		asWS.remove(newMember);
            		break;
            	}
            }
		}
		else{
			AmpTeam ampTeam = TeamUtil.getAmpTeam(upMemForm.getTeamId());
			User user = org.digijava.module.aim.util.DbUtil.getUser(upMemForm.getEmail());

			/* check if the user have entered an invalid user id */
			if (user == null) {
				upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.aim.addTeamMember.invalidUser"));
				saveErrors(request, errors);
	
				return mapping.findForward("forward");	
			}
	
			/* if user havent specified the role for the new member */
			if (upMemForm.getRole() == null) {
				upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.aim.addTeamMember.roleNotSelected"));
				saveErrors(request, errors);
				return mapping.findForward("forward");			
			}
			
			/* check if user have selected role as Team Lead when a team lead 
			 * already exist for the team */
			if (TeamMemberUtil.getTeamHead(ampTeam.getAmpTeamId()) != null &&
					TeamMemberUtil.getTeamHead(ampTeam.getAmpTeamId()).getAmpMemberRole().getAmpTeamMemRoleId().equals(upMemForm.getRole())) {
				upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
				errors.add(ActionErrors.GLOBAL_ERROR,new ActionError(
						"error.aim.addTeamMember.teamLeadAlreadyExist"));
				saveErrors(request, errors);
				return mapping.findForward("forward");			
			}
			
			/* check if user is already part of the selected team */
			if (TeamUtil.isMemberExisting(upMemForm.getTeamId(),upMemForm.getEmail())) {
				upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
				errors.add(ActionErrors.GLOBAL_ERROR,new ActionError(
						"error.aim.addTeamMember.teamMemberAlreadyExist"));
				saveErrors(request, errors);
				logger.debug("Member is already existing");
				return mapping.findForward("forward");			
			}
			/*check if user is not admin; as admin he can't be part of a workspace*/
			if (upMemForm.getEmail().equalsIgnoreCase("admin@amp.org")) {
				upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
				errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("error.aim.addTeamMember.teamMemberIsAdmin"));
				saveErrors(request, errors);
				logger.debug("Member is already existing");
				return mapping.findForward("forward");
			}
			
			AmpTeamMemberRoles role = TeamMemberUtil.getAmpTeamMemberRole(upMemForm.getRole());
			if (role != null) {
				AmpTeamMember newMember = new AmpTeamMember();
				newMember.setUser(user);
				newMember.setAmpTeam(ampTeam);
				newMember.setAmpMemberRole(role);
				// add the default permissions
				newMember.setReadPermission(role.getReadPermission());
				newMember.setWritePermission(role.getWritePermission());
				newMember.setDeletePermission(role.getDeletePermission());
				// add the default application settings for the user  
				AmpApplicationSettings ampAppSettings = org.digijava.module.aim.util.DbUtil
						.getTeamAppSettings(ampTeam.getAmpTeamId());
				AmpApplicationSettings newAppSettings = new AmpApplicationSettings();
				//newAppSettings.setTeam(ampAppSettings.getTeam());
				newAppSettings.setTeam(newMember.getAmpTeam());
				newAppSettings.setMember(newMember);
				newAppSettings.setDefaultRecordsPerPage(ampAppSettings
						.getDefaultRecordsPerPage());
				newAppSettings.setCurrency(ampAppSettings.getCurrency());
				newAppSettings.setFiscalCalendar(ampAppSettings
						.getFiscalCalendar());
				newAppSettings.setLanguage(ampAppSettings.getLanguage());
				newAppSettings.setUseDefault(new Boolean(true));
				Site site = RequestUtils.getSite(request);
				try{
					TeamUtil.addTeamMember(newMember,newAppSettings,site);
					addWorkSpace(newMember, upMemForm);
				}catch (Exception e){
						e.printStackTrace();
						logger.error("error when trying to add a new member: " + newMember.getUser().getEmail() + " from team: "
								+ newMember.getAmpTeam().getName());
				}
			}
		}
		return mapping.findForward("forward");
	}

	private void addWorkSpace(AmpTeamMember newMember, AddUserForm upMemForm) {
		Collection assignedWS = upMemForm.getAssignedWorkspaces();
		if(assignedWS==null){
			ArrayList assWS = new ArrayList();
			upMemForm.setAssignedWorskpaces(assWS);
		}
		upMemForm.getAssignedWorkspaces().add(newMember);
		upMemForm.setTeamId(-1L);
		upMemForm.setRole(-1L);
	}	
}
