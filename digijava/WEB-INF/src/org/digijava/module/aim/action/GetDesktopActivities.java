/*
 * GetDesktopActivities.java
 * Created: 29-May-2006
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.DesktopForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DesktopUtil;

/**
 * Loads all desktop activities for the user
 * @author Priyajith
 *
 */
public class GetDesktopActivities extends Action {
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		DesktopForm dForm = (DesktopForm) form;
		
		session.setAttribute(Constants.TEAM_ID,tm.getTeamId());
		
		if (Constants.ACCESS_TYPE_MNGMT.equalsIgnoreCase(tm.getTeamAccessType()) ||
				"Donor".equalsIgnoreCase(tm.getTeamType())) {
			dForm.setShowAddActivityLink(false);
		} else {
			dForm.setShowAddActivityLink(true);
		}
		dForm.setTeamId(tm.getTeamId());
		dForm.setTeamHead(tm.getTeamHead());

		
		// load activities
		/*
		 * The activities of the user is stored in a session scoped varible 'ampProjects' declared in the class 
		 * org.digijava.module.aim.helper.Constants. 
		 */
		Collection col = (Collection) session.getAttribute(Constants.AMP_PROJECTS);
		//if (col == null) {
			col = DesktopUtil.getDesktopActivities(tm.getTeamId(),tm.getMemberId(),
					tm.getTeamHead());
			session.setAttribute(Constants.AMP_PROJECTS,col);
		//}
		dForm.setTotalCalculated(false);
		dForm.setSearchKey(null);
		Collection col1 = (Collection) session.getAttribute(Constants.AMP_PROJECTS);
		dForm.setActivities(new ArrayList(col1));		
		
		return mapping.findForward("forward");
	}
}