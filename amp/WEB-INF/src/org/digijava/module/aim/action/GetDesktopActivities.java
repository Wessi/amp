/*
 * GetDesktopActivities.java
 * Created: 29-May-2006
 */
package org.digijava.module.aim.action;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.module.aim.form.DesktopForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.aim.util.IndicatorUtil;

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
		
		/*
		 * Do not uncomment ---- FIX for bug AMP-2030
		 * if(session.getAttribute(Constants.DEFAULT_TEAM_REPORT)==null){
                  session.setAttribute("filterCurrentReport",null);
                }
        */

		session.setAttribute(Constants.TEAM_ID,tm.getTeamId());
		String risk=(String) request.getParameter("risk");
		AmpARFilter arf = (AmpARFilter) session.getAttribute(ArConstants.REPORTS_FILTER);
		if(risk!=null)
		{
			if(arf!=null)
				if(arf.getRisks()!=null)
				{
					arf.getRisks().clear();
					AmpCategoryValue airr=FeaturesUtil.getFilter(risk);
					arf.getRisks().add(airr);
					session.setAttribute(ArConstants.REPORTS_FILTER, arf);
				}
				else{
					arf.setRisks(new HashSet());
					AmpCategoryValue airr=FeaturesUtil.getFilter(risk);
					arf.getRisks().add(airr);
					session.setAttribute(ArConstants.REPORTS_FILTER, arf);
				}
			else{
				arf=new AmpARFilter();
				arf.setRisks(new HashSet());
				AmpCategoryValue airr=FeaturesUtil.getFilter(risk);
				arf.getRisks().add(airr);
				session.setAttribute(ArConstants.REPORTS_FILTER, arf);
			}
		}

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

		
		/* Do not uncomment this too. This load is not useful, just uses 100% CPU and high memory and db load.

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
		*/
		
		return mapping.findForward("forward");
	}
}
