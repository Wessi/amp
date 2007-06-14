package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;
 
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
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Workspace;
import org.digijava.module.aim.util.TeamUtil;

public class GetWorkspace extends Action {

	private static Logger logger = Logger.getLogger(GetWorkspace.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In GetWorkspace");
		
		boolean permitted = false;
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") != null) {
			String key = (String) session.getAttribute("ampAdmin");
			if (key.equalsIgnoreCase("yes")) {
				permitted = true;
			} else {
				if (session.getAttribute("teamLeadFlag") != null) {
					key = (String) session.getAttribute("teamLeadFlag");
					if (key.equalsIgnoreCase("true")) {
						permitted = true;	
					}
				}
			}
		}
		if (!permitted) {
			return mapping.findForward("index");
		}
		
		UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;
		uwForm.setUpdateFlag(false);
		
		String dest = request.getParameter("dest");
		String id	= request.getParameter("tId");
		logger.debug("dest = " + dest);
		logger.debug("id = " + id);
		Long teamId = new Long(0);
		try {
			if (id != null && id.trim().length() > 0) {
				teamId = new Long(Long.parseLong(id));	
				if (teamId.longValue() == -1) {
					TeamMember tm = (TeamMember) session.getAttribute("currentMember");
					teamId = tm.getTeamId();
				}
				if (uwForm.isAddFlag())
					uwForm.setAddFlag(false);
			} else {
				// Clearing formbean properties before going to add a team
				if (!uwForm.isAddFlag()) {
					uwForm.setReset(true);
					uwForm.reset(mapping, request);
					uwForm.setAddFlag(true);
				}
				uwForm.setActionEvent("add");
				return mapping.findForward("showAddWorkspace");				
			}
		} catch (NumberFormatException nfe) {
			// incorrect id.
		}

		Workspace workspace = TeamUtil.getWorkspace(teamId);
		if (workspace != null) {
			Long typeId		= new Long(0);
			if ( workspace.getType() != null ) {
				typeId	= workspace.getType().getId();
			}
			
			uwForm.setTeamId(new Long(workspace.getId()));
			uwForm.setTeamName(workspace.getName());
			uwForm.setCategory(workspace.getTeamCategory());
			uwForm.setTypeId(typeId);
			uwForm.setWorkspaceType(workspace.getWorkspaceType());
			uwForm.setDescription(workspace.getDescription());
			
			uwForm.setRelatedTeam(workspace.getRelatedTeam());
			if (null == uwForm.getRelatedTeam()) {
				if ("DONOR".equalsIgnoreCase(uwForm.getCategory()) && "Team".equalsIgnoreCase(uwForm.getWorkspaceType())) {
					//Collection col = TeamUtil.getAllRelatedTeamsByType(uwForm.getType());
					Collection col = TeamUtil.getAllRelatedTeams();
					if (col.size() < 1)
						uwForm.setRelatedTeamFlag("nil");
					else {
						//String uwtype = uwForm.getType();
						Iterator itr = col.iterator();
						while (itr.hasNext()) {
							AmpTeam team 		= (AmpTeam) itr.next();
							String typeValue	= null;
							if ( team.getType() != null ) {
								typeValue		= team.getType().getValue();
							}
							
						/*if (uwtype.equalsIgnoreCase(team.getType())) {
								if ("Bilateral".equalsIgnoreCase(uwtype))
									uwForm.getRelatedTeamBilatColl().add(team);
								else if ("Multilateral".equalsIgnoreCase(uwtype))
									uwForm.getRelatedTeamMutilatColl().add(team);
							} */
							if ( Constants.TEAM_TYPE_BILATERAL.equals( typeValue ) )
								uwForm.getRelatedTeamBilatColl().add(team);
							else if ( Constants.TEAM_TYPE_MULTILATERAL.equals( typeValue ) )
								uwForm.getRelatedTeamMutilatColl().add(team);
						}
						uwForm.setRelatedTeamBilatCollSize(new Integer(uwForm.getRelatedTeamBilatColl().size()));
						uwForm.setRelatedTeamFlag("set");
					}
				}
			}
			else {
				uwForm.setRelatedTeamFlag("noedit");
				uwForm.setRelatedTeamName(TeamUtil.getAmpTeam(uwForm.getRelatedTeam()).getName());
			}
			logger.debug("uwForm.getRelatedTeamFlag() : " + uwForm.getRelatedTeamFlag());
			
			uwForm.setChildWorkspaces(workspace.getChildWorkspaces());
			uwForm.setActionEvent("edit");
		}			

		logger.debug("Dest value = " + dest);
		return mapping.findForward(dest);
	}
	
}
