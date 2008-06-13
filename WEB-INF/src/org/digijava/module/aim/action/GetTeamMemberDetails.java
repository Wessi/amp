
/*
 * GetTeamMemberDetails.java
 */

package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

/**
 * Get the team member details passing the id of the team member
 * whose details are required
 * 
 * @author priyajith
 */

public class GetTeamMemberDetails extends Action {

	private static Logger logger = Logger.getLogger(GetTeamMemberDetails.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In GetTeamMemberDetails");
		
		HttpSession session = request.getSession();
		
		TeamMemberForm upForm = (TeamMemberForm) form;
		if (request.getParameter("id") != null
				&& request.getParameter("action") != null) {

			Long id = new Long(Long.parseLong(request.getParameter("id")));
			AmpTeamMember ampMember = TeamMemberUtil.getAmpTeamMember(id);
			Long teamId = ampMember.getAmpTeam().getAmpTeamId();

			Collection<TeamMember> col = TeamMemberUtil.getAllTeamMembers(teamId);
			for(TeamMember member : col){
				if(member.getTeamHead()){
					upForm.setHeadId(member.getMemberId());
					break;
				}
			}
			upForm.setWokspaceManId(TeamMemberUtil.getAmpTeamHeadRole().getAmpTeamMemRoleId());
			
			User user = ampMember.getUser();
			upForm.setName(user.getName());
			upForm.setTeamMemberId(ampMember.getAmpTeamMemId());
			upForm.setTeamId(ampMember.getAmpTeam().getAmpTeamId());
			upForm.setTeamName(ampMember.getAmpTeam().getName());
			AmpTeamMemberRoles ampRole = TeamMemberUtil.getAmpTeamMemberRole(ampMember
					.getAmpMemberRole().getAmpTeamMemRoleId());

			upForm.setRole(ampRole.getAmpTeamMemRoleId());
			upForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
			upForm.setUserId(user.getId());
			if (ampMember.getReadPermission().booleanValue() == true) {
				upForm.setReadPerms("on");
			}
			if (ampMember.getWritePermission().booleanValue() == true) {
				upForm.setWritePerms("on");
			}
			if (ampMember.getDeletePermission().booleanValue() == true) {
				upForm.setDeletePerms("on");
			}

			String action = request.getParameter("action");
			if (action.trim().equals("edit")) {
				upForm.setAction("edit");
			} else if (action.trim().equals("delete")) {
				upForm.setAction("delete");
			} else {
				upForm.setAction("");
			}
		}
		return mapping.findForward("showUpdate");
	}
}

