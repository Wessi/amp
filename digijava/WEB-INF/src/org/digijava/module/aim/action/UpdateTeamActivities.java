package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.UpdateDB;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class UpdateTeamActivities extends Action {

	private static Logger logger = Logger.getLogger(UpdateTeamActivities.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In update team activities");
		
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

		TeamActivitiesForm taForm = (TeamActivitiesForm) form;

		Long id = null;
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");

		int numRecords = 0;
		int page = 0;

		if (session.getAttribute("currentMember") != null) {
			id = tm.getTeamId();
			numRecords = tm.getAppSettings().getDefRecsPerPage();
		}

		if (taForm.getRemoveActivity() != null) {
			/* remove all selected activities */
		    
	        if (taForm.getSelActivities() != null) {
	        	if (tm.getTeamType().equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) {
	        		TeamUtil.removeActivitiesFromDonor(tm.getTeamId(),taForm.getSelActivities());
	        	} else {
	        		TeamUtil.removeActivitiesFromTeam(taForm.getSelActivities());	
	        	}
	            
	        }
	 		    
			Long selActivities[] = taForm.getSelActivities();
			taForm.setSelActivities(null);

			if (session.getAttribute("unassignedActivityList") != null) {
				session.removeAttribute("unassignedActivityList");
			}
			
			if (session.getAttribute("ampProjects") != null) {
				logger.info("removing ampProjects from session..");
				session.removeAttribute("ampProjects");
			}
			if (session.getAttribute("teamActivityList") != null) {
				session.removeAttribute("teamActivityList");
			}
			taForm.setRemoveActivity(null);
			taForm.setSelActivities(null);			

			return mapping.findForward("forward");
		} else if (taForm.getAssignActivity() != null) {
			/* add the selected activities to the team list */
			logger.info("in assign activity");
			Long selActivities[] = taForm.getSelActivities();

			if (selActivities != null) {
				for (int i = 0; i < selActivities.length; i++) {
					if (selActivities[i] != null) {
						Long actId = selActivities[i];
						AmpActivity activity = ActivityUtil.getProjectChannelOverview(actId);
						AmpTeam ampTeam = TeamUtil.getAmpTeam(taForm.getTeamId());
						activity.setTeam(ampTeam);

						logger.info("updating " + activity.getName());
						DbUtil.update(activity);
						UpdateDB.updateReportCache(actId);
					}
				}
			} else {
				taForm.setAssignActivity(null);
				taForm.setSelActivities(null);
				return mapping.findForward("forward");				
			}
			taForm.setSelActivities(null);
			if (session.getAttribute("unassignedActivityList") != null) {
				session.removeAttribute("unassignedActivityList");
			}
			if (session.getAttribute("ampProjects") != null) {
				session.removeAttribute("ampProjects");
			}
			if (session.getAttribute("teamActivityList") != null) {
				session.removeAttribute("teamActivityList");
			}
			taForm.setAssignActivity(null);
			return mapping.findForward("forward");
		} else {
			/* show all unassigned activities */

			if (request.getParameter("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(request.getParameter("page"));
			}

			AmpTeam ampTeam = TeamUtil.getAmpTeam(id);

			Collection col = null;
			if (session.getAttribute("unassignedActivityList") == null) {
				col = TeamUtil.getAllUnassignedActivities();
				List temp = (List) col;
				Collections.sort(temp);
				col = (Collection) temp;
				session.setAttribute("unassignedActivityList", col);
			}
			
			Collection actList = (Collection) session
					.getAttribute("unassignedActivityList");

			
			Comparator acronymComp = new Comparator() {
				public int compare(Object o1, Object o2) {
					Activity r1 = (Activity) o1;
					Activity r2 = (Activity) o2;
			        return r1.getDonors().trim().toLowerCase().compareTo(r2.getDonors().trim().toLowerCase());
				}
			};
			Comparator racronymComp = new Comparator() {
				public int compare(Object o1, Object o2) {
					Activity r1 = (Activity) o1;
					Activity r2 = (Activity) o2;
					return -(r1.getDonors().trim().toLowerCase().compareTo(r2.getDonors().trim().toLowerCase()));
				}
			};
			
			List temp = (List)actList;
			String sort = (taForm.getSort() == null) ? null : taForm.getSort().trim();
			String sortOrder = (taForm.getSortOrder() == null) ? null : taForm.getSortOrder().trim();
			
			if ( sort == null || "".equals(sort) || sortOrder == null || "".equals(sortOrder)) {
				Collections.sort(temp);
				taForm.setSort("activity");
				taForm.setSortOrder("asc");
			}
			else {
				if ("activity".equals(sort)) {
					if ("asc".equals(sortOrder))
						Collections.sort(temp);
					else
						Collections.sort(temp,Collections.reverseOrder());
				}
				else if ("donor".equals(sort)) {
					if ("asc".equals(sortOrder))
						Collections.sort(temp, acronymComp);
					else
						Collections.sort(temp, racronymComp);
				}
			}			
			
			int stIndex = ((page - 1) * numRecords) + 1;
			int edIndex = page * numRecords;
			if (edIndex > temp.size()) {
				edIndex = temp.size();
			}

			Vector vect = new Vector();
			vect.addAll(temp);

			col = new ArrayList();
			for (int i = (stIndex - 1); i < edIndex; i++) {
				col.add(vect.get(i));
			}

			int numPages = temp.size() / numRecords;
			numPages += (temp.size() % numRecords != 0) ? 1 : 0;

			Collection pages = null;

			if (numPages > 1) {
				pages = new ArrayList();
				for (int i = 0; i < numPages; i++) {
					Integer pageNum = new Integer(i + 1);
					pages.add(pageNum);
				}
			}
			taForm.setPages(pages);
			taForm.setActivities(col);
			taForm.setTeamId(id);
			taForm.setTeamName(ampTeam.getName());
			taForm.setCurrentPage(new Integer(page));
			taForm.setSelActivities(null);
			session.setAttribute("pageno", new Integer(page));
			return mapping.findForward("showAddActivity");
		}
	}

	public boolean canDelete(Long actId) {
		logger.debug("In can delete");
		Iterator itr = TeamMemberUtil.getAllMembersUsingActivity(actId).iterator();
		if (itr.hasNext()) {
			logger.debug("return false");
			return false;
		} else {
			logger.debug("return true");
			return true;
		}

	}
}
