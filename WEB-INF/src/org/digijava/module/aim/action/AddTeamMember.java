/*
 * AddTeamMember.java
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

/**
 * Adds a member to a team
 * 
 * @author priyajith
 */
public class AddTeamMember extends Action {

	private static Logger logger = Logger.getLogger(AddTeamMember.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		TeamMemberForm upMemForm = (TeamMemberForm) form;
		ActionErrors errors = new ActionErrors();
		logger.debug("In add members");
		
		AmpTeam ampTeam = TeamUtil.getAmpTeam(upMemForm.getTeamId());
		User user = DbUtil.getUser(upMemForm.getEmail());

		/* check if the user have entered an invalid user id */
		if (user == null) {
			upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"error.aim.addTeamMember.invalidUser"));
			saveErrors(request, errors);
			if (upMemForm.getFromPage() == 1) {
				return mapping.findForward("showAddFromAdmin");	
			} else {
				return mapping.findForward("showAddFromTeam");	
			}
		}

		/* if user havent specified the role for the new member */
		if (upMemForm.getRole() == null) {
			upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"error.aim.addTeamMember.roleNotSelected"));
			saveErrors(request, errors);
			if (upMemForm.getFromPage() == 1) {
				return mapping.findForward("showAddFromAdmin");	
			} else {
				return mapping.findForward("showAddFromTeam");	
			}			
		}
		
		/* check if user have selected role as Team Lead when a team lead 
		 * already exist for the team */
		if (ampTeam.getTeamLead() != null &&
				ampTeam.getTeamLead().getAmpMemberRole().getAmpTeamMemRoleId().equals(upMemForm.getRole())) {
			upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError(
					"error.aim.addTeamMember.teamLeadAlreadyExist"));
			saveErrors(request, errors);
			if (upMemForm.getFromPage() == 1) {
				return mapping.findForward("showAddFromAdmin");	
			} else {
				return mapping.findForward("showAddFromTeam");	
			}						
		}
		
		/* check if user is already part of the selected team */
		if (TeamUtil.isMemberExisting(upMemForm.getTeamId(),upMemForm.getEmail())) {
			upMemForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError(
					"error.aim.addTeamMember.teamMemberAlreadyExist"));
			saveErrors(request, errors);
			logger.debug("Member is already existing");
			if (upMemForm.getFromPage() == 1) {
				logger.debug("Forwarding to showAddFromAdmin");
				return mapping.findForward("showAddFromAdmin");	
			} else {
				logger.debug("Forwarding to showAddFromTeam");
				return mapping.findForward("showAddFromTeam");	
			}				
		}		
		
		AmpTeamMemberRoles role = TeamMemberUtil.getAmpTeamMemberRole(upMemForm.getRole());
		if (role != null) {
			AmpTeamMember newMember = new AmpTeamMember();
			newMember.setUser(user);
			newMember.setAmpTeam(ampTeam);
			newMember.setAmpMemberRole(role);
			if (upMemForm.getPermissions().equals("default")) {
				newMember.setReadPermission(role.getReadPermission());
				newMember.setWritePermission(role.getWritePermission());
				newMember.setDeletePermission(role.getDeletePermission());
			} else if (upMemForm.getPermissions().equals("userSpecific")) {
				if (upMemForm.getReadPerms() != null
						&& upMemForm.getReadPerms().equals("on")) {
					newMember.setReadPermission(new Boolean(true));
				} else {
					newMember.setReadPermission(new Boolean(false));
				}
				if (upMemForm.getWritePerms() != null
						&& upMemForm.getWritePerms().equals("on")) {
					newMember.setWritePermission(new Boolean(true));
				} else {
					newMember.setWritePermission(new Boolean(false));
				}
				if (upMemForm.getDeletePerms() != null
						&& upMemForm.getDeletePerms().equals("on")) {
					newMember.setDeletePermission(new Boolean(true));
				} else {
					newMember.setDeletePermission(new Boolean(false));
				}
			}		
			
			
			// add the default application settings for the user  
			AmpApplicationSettings ampAppSettings = DbUtil
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
			newAppSettings.setDefaultPerspective(ampAppSettings
					.getDefaultPerspective());
			newAppSettings.setUseDefault(new Boolean(true));
			Site site = RequestUtils.getSite(request);
			try{
				TeamUtil.addTeamMember(newMember,newAppSettings,site);
			}catch (Exception e){
					e.printStackTrace();
					logger.error("error when trying to add a new member: " + newMember.getUser().getEmail() + " from team: "
							+ newMember.getAmpTeam().getName());
				}
			
			upMemForm.setEmail(null);
			upMemForm.setRole(null);
			upMemForm.setTeamName(null);
			upMemForm.setAmpRoles(null);
			upMemForm.setPermissions(null);
		}
		if (upMemForm.getFromPage() == 1) {
			return mapping.findForward("addedFromAdmin");
		} else {
			return mapping.findForward("addedFromTeam");	
		}
		
	}
}