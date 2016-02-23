package org.digijava.module.visualization.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.dbentity.AmpDashboardGraph;
import org.digijava.module.visualization.dbentity.AmpGraph;
import org.digijava.module.visualization.form.DashboardForm;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.visualization.helper.EntityRelatedListHelper;
import org.digijava.module.visualization.util.Constants;
import org.digijava.module.visualization.util.DashboardUtil;
import org.springframework.beans.BeanWrapperImpl;


public class LaunchDashboard extends Action {
	private static Logger logger = Logger.getLogger(LaunchDashboard.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		HttpSession session = request.getSession();
		request.setAttribute("compatibility_shim", "chrome=1; IE=8");
		VisualizationForm dForm = (VisualizationForm) form;
		AmpDashboard dashboard = dForm.getDashboard();
        String dashId = request.getParameter("id") != null ? (String) request.getParameter("id") : null;
        String graphsList = request.getParameter("graphs") != null ? (String) request.getParameter("graphs") : null;
        List<AmpGraph> list = new ArrayList<AmpGraph>();
		if(graphsList!=null){
			String[] graphsListSplit = graphsList.split(",");
			for (int i = 0; i < graphsListSplit.length; i++) {
				list.add(org.digijava.module.visualization.util.DbUtil.getDashboardGraphById(Long.valueOf(graphsListSplit[i])).getGraph());
			}
			dForm.setGraphList(list);
		}
		if (dashId!=null){
			Long graphId = request.getParameter("graphId") != null ? Long.parseLong(request.getParameter("graphId")) : null;
			
			Long dId = Long.parseLong(dashId);
			dashboard = org.digijava.module.visualization.util.DbUtil.getDashboardById(dId);
			DashboardFilter filter = new DashboardFilter();
			if (dashboard.getBaseType()==4)//for Deal based dashboard set MTEF projections as Transaction type by default
	        	filter.setTransactionType(org.digijava.module.aim.helper.Constants.MTEFPROJECTION);




			DashboardUtil.initializeFilter(filter, request, dashboard);
			TeamMember tm = (TeamMember) session.getAttribute("currentMember");
			if (tm==null) {
				filter.setFromPublicView(true);
	        }
			dForm.setDashboard(dashboard);
			dForm.setFilter(filter);
			dForm.getFilter().setAgencyType(dashboard.getPivot());
			dForm.getFilter().setAgencyTypeDefault(dashboard.getPivot());
			dForm.getFilter().setAgencyTypeFilter(dashboard.getPivot());
			dForm.getFilter().setAgencyTypeQuickFilter(dashboard.getPivot());
			List<AmpDashboardGraph> listDG = org.digijava.module.visualization.util.DbUtil.getDashboardGraphByDashboard(dId);
			list = new ArrayList<AmpGraph>();
			//If a dashboard is executed from the menu, it will show all graphs in the dashboard by default
			for (Iterator iterator = listDG.iterator(); iterator.hasNext();) {
				AmpDashboardGraph ampDashboardGraph = (AmpDashboardGraph) iterator.next();

				AmpGraph graph = ampDashboardGraph.getGraph();
				if (graphId != null){//Only a graph must be shown
					if (graphId.equals(graph.getId())){
						list.add(graph);
						break;
					}
				}else{
						list.add(graph);
					}
				}
				dForm.setGraphList(list);
				//If a dashboard is executed from the menu, it will show all rank lists available by default
				if (dashboard.getBaseType()==4){//except for deal dashboard, in that case only show secondary program ranks which is where is peacebuilding 
					dForm.getFilter().setShowSecondaryProgramsRanking(true);
					dForm.getFilter().setShowProgramsRanking(false);
					dForm.getFilter().setShowNPORanking(false);
					dForm.getFilter().setShowProjectsRanking(false);
					dForm.getFilter().setShowSectorsRanking(false);
					dForm.getFilter().setShowRegionsRanking(false);
					dForm.getFilter().setShowOrganizationsRanking(false);
				} else {
					dForm.getFilter().setShowNPORanking(true);
					dForm.getFilter().setShowProgramsRanking(true);
					dForm.getFilter().setShowProjectsRanking(true);
					dForm.getFilter().setShowSectorsRanking(true);
					dForm.getFilter().setShowRegionsRanking(true);
					dForm.getFilter().setShowOrganizationsRanking(true);
				}
			}
	        dForm.getFilter().setDashboardType(dashboard.getBaseType());
	        
	        String sliderLabels = "";
			for (Long i = dForm.getFilter().getStartYear(); i <= dForm.getFilter().getEndYear(); i++) {
				Long fiscalCalendarId = dForm.getFilter().getFiscalCalendarId();
				Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
				Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	            String headingFY = TranslatorWorker.translateText("FY");
				String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
				sliderLabels = sliderLabels + yearName + ",";
			}
			dForm.getFilter().setFlashSliderLabels(sliderLabels);
	        switch (dashboard.getBaseType()) {
			case 0:
				dForm.setDashboardTitle(dashboard.getName());
				dForm.setDashboardSubTitle("");
				break;
	
			case 1:
				String orgGroupIds = request.getParameter("orgGroupIds") != null ? (String) request.getParameter("orgGroupIds") : null;
				if (orgGroupIds!=null && orgGroupIds.length()>0 && !orgGroupIds.equals("-1")){
					String grpId = orgGroupIds.contains(",") ? orgGroupIds.split(",")[0] : orgGroupIds;
					AmpOrgGroup grp = org.digijava.module.visualization.util.DbUtil.getOrgGroup(Long.valueOf(grpId));
					dForm.setDashboardTitle(grp.getOrgGrpName());
				} else {
					dForm.setDashboardTitle(TranslatorWorker.translateText("All Organization Groups"));
				}
				String orgIds = request.getParameter("orgIds") != null ? (String) request.getParameter("orgIds") : null;
				if (orgIds!=null && orgIds.length()>0){
					if (!orgIds.contains(",")){
						String id = orgIds.split(",")[0];
						AmpOrganisation orga = org.digijava.module.visualization.util.DbUtil.getOrganisation(Long.valueOf(id));
						dForm.setDashboardSubTitle(orga.getName());
					} else {
			        	dForm.setDashboardSubTitle(TranslatorWorker.translateText("Multiple Organizations"));
					}
				} else {
		        	dForm.setDashboardSubTitle(TranslatorWorker.translateText("All Organizations"));
				}
				break;
	
			case 2:
				String regionIds = request.getParameter("regionIds") != null ? (String) request.getParameter("regionIds") : null;
				if (regionIds!=null && regionIds.length()>0 && !regionIds.equals("-1")){
					String regId = regionIds.contains(",") ? regionIds.split(",")[0] : regionIds;
					AmpCategoryValueLocations reg = org.digijava.module.visualization.util.DbUtil.getLocationById(Long.valueOf(regId));
					dForm.setDashboardTitle(reg.getName());
				} else {
					dForm.setDashboardTitle(TranslatorWorker.translateText("All Regions"));
				}
				String zoneIds = request.getParameter("zoneIds") != null ? (String) request.getParameter("zoneIds") : null;
				if (zoneIds!=null && zoneIds.length()>0){
					if (!zoneIds.contains(",")){
						String id = zoneIds.split(",")[0];
						AmpCategoryValueLocations zone = org.digijava.module.visualization.util.DbUtil.getLocationById(Long.valueOf(id));
						dForm.setDashboardSubTitle(zone.getName());
					} else {
						dForm.setDashboardSubTitle(TranslatorWorker.translateText("Multiple zones"));
					}
				} else {
					dForm.setDashboardSubTitle(TranslatorWorker.translateText("All zones"));
				}
				break;
	
			case 3:
				String sectorIds = request.getParameter("sectorIds") != null ? (String) request.getParameter("sectorIds") : null;
				if (sectorIds!=null && sectorIds.length()>0 && !sectorIds.equals("-1")){
					String secId = sectorIds.contains(",") ? sectorIds.split(",")[0] : sectorIds;
					AmpSector sec = SectorUtil.getAmpSector(Long.valueOf(secId));
					dForm.setDashboardTitle(sec.getName());
				} else {
					dForm.setDashboardTitle(TranslatorWorker.translateText("All Sectors"));
				}
				String subSectorIds = request.getParameter("subSectorIds") != null ? (String) request.getParameter("subSectorIds") : null;
				if (subSectorIds!=null && subSectorIds.length()>0){
					if (!subSectorIds.contains(",")){
						String id = subSectorIds.split(",")[0];
						AmpSector sec = SectorUtil.getAmpSector(Long.valueOf(id));
						dForm.setDashboardSubTitle(sec.getName());
					} else {
						dForm.setDashboardSubTitle(TranslatorWorker.translateText("Multiple sub sectors"));
					}
				} else {
					dForm.setDashboardSubTitle(TranslatorWorker.translateText("All sub sectors"));
				}
				break;
			case 4:
				dForm.setDashboardTitle(dashboard.getName());
				dForm.setDashboardSubTitle("");
				break;
	
			default:
				break;
			}

        if (request.getParameter("fromMap") != null && request.getParameter("fromMap").trim().equalsIgnoreCase("true")) {
            dForm.getFilter().setRegionId(Long.parseLong(request.getParameter("filter.regionId")));
        }
			
			dForm.getFilter().setFromGenerator(true);
			//Set a reduced list of Organization Groups considering the role in the list of activities
			dForm.getFilter().setOrgGroups(org.digijava.module.visualization.util.DbUtil.getOrganisationGroupsByRole(false, dForm.getFilter()));
			if (request.getParameter("graphId") != null){
				return mapping.findForward("showGraph");
			}
			return mapping.findForward("forward");
		}
	//}
}