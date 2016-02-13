/**
 * ViewNewAdvancedReport.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportLog;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 15, 2006
 * 
 */
public class ViewNewAdvancedReport extends Action {
	private static Logger logger = Logger.getLogger(ViewNewAdvancedReport.class) ;
	
	public ViewNewAdvancedReport() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
			{
		AdvancedReportForm arf=(AdvancedReportForm) form;
		HttpSession httpSession = request.getSession();

		
		String loadStatus=request.getParameter("loadstatus");
		Integer progressValue = (httpSession.getAttribute("progressValue") != null) ? (Integer)httpSession.getAttribute("progressValue") :null;
		if(progressValue == null)
			progressValue = 0;

		if(loadStatus != null){
			ServletOutputStream os = response.getOutputStream();
			os.println(progressValue + ","+httpSession.getAttribute("progressTotalRows"));
			os.flush();
			return null;
		}

		progressValue = 0;

		String widget=request.getParameter("widget");
		request.setAttribute("widget",widget);

		String debug=request.getParameter("debug");
		request.setAttribute("debug",debug);

		
		TeamMember tm = (TeamMember) request.getSession().getAttribute("currentMember");				
		AmpApplicationSettings ampAppSettings = null;				
		if(tm!=null)				
		ampAppSettings = DbUtil.getMemberAppSettings(tm.getMemberId());
		if(ampAppSettings==null)
			if(tm!=null)
			ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
		
		if (ampAppSettings != null)
			request.setAttribute("recordsPerPage", ampAppSettings
					.getDefaultRecordsPerPage());
		else
			request.setAttribute("recordsPerPage", new Integer(25));
		
		
		//check currency code:
		if(httpSession.getAttribute("reportCurrencyCode")==null) httpSession.setAttribute("reportCurrencyCode",Constants.DEFAULT_CURRENCY);
		
		if(httpSession.getAttribute("reportSorters")==null) httpSession.setAttribute("reportSorters",new HashMap());
		Map sorters=(Map) httpSession.getAttribute("reportSorters");
		String ampReportId = request.getParameter("ampReportId");
		
		GroupReportData rd=(GroupReportData) httpSession.getAttribute("report");
		AmpReports ar=(AmpReports) httpSession.getAttribute("reportMeta");
		Session session = PersistenceManager.getSession();
		String sortBy=request.getParameter("sortBy");
		String applySorter = request.getParameter("applySorter");
		if(ampReportId==null) 
			ampReportId=ar.getAmpReportId().toString();
		Long reportId=new Long(ampReportId);
		request.setAttribute("ampReportId",ampReportId);
		
		String startRow=request.getParameter("startRow");
		String endRow=request.getParameter("endRow");
		String cachedStr=request.getParameter("cached");
		
		
		boolean cached=false;
		if(cachedStr!=null) cached=Boolean.parseBoolean(cachedStr);
		
		AmpARFilter filter = (AmpARFilter) httpSession.getAttribute(ArConstants.REPORTS_FILTER);
		if(filter==null || !reportId.equals(filter.getAmpReportId())) {
			if(filter != null && filter.isPublicView())
			{
				//This is to avoid resetting the publicView status to allow the right redirection on Public Views
				filter=new AmpARFilter();
				httpSession.setAttribute(ArConstants.REPORTS_FILTER,filter);
				filter.readRequestData(request);
				filter.setPublicView(true);
			}
			else
			{
				filter=new AmpARFilter();
				httpSession.setAttribute(ArConstants.REPORTS_FILTER,filter);
				filter.readRequestData(request);
			}
		}

		if (tm !=null && (Constants.ACCESS_TYPE_MNGMT.equalsIgnoreCase(tm.getTeamAccessType()) ||
				"Donor".equalsIgnoreCase(tm.getTeamType()))){
			filter.setApproved(true);
			filter.setDraft(true);
		}else{
			filter.setApproved(false);
			filter.setDraft(false);
		}
		if (tm ==null)
		{
			filter.setApproved(true);
			filter.setDraft(true);
		}
		httpSession.setAttribute("progressValue", ++progressValue); 
		httpSession.setAttribute("progressTotalRows", request.getAttribute("recordsPerPage"));
		
		if( (!cached && (applySorter == null && sortBy == null || ar==null)) || 
			(ampReportId != null && ar != null && !ampReportId.equals(ar.getAmpReportId().toString()) )) 
		{
			
			progressValue = progressValue + 20;// 20 is the weight of this process on the progress bar
			httpSession.setAttribute("progressValue", progressValue); 

			rd=ARUtil.generateReport(mapping,form,request,response);
			progressValue = progressValue + 10;// 20 is the weight of this process on the progress bar
			httpSession.setAttribute("progressValue", progressValue); 
	
			ar = (AmpReports) session.get(AmpReports.class, new Long(ampReportId));
			//This is for public views to avoid nullPointerException due to there is no logged user.
			if(tm != null){
				saveOrUpdateReportLog(tm, ar);
			}
			
			progressValue = progressValue + 3;// 3 is the weight of this process on the progress bar
			httpSession.setAttribute("progressValue", progressValue); 
		}
		
		
		//test if the request was for hierarchy sorting purposes:
		if(applySorter!=null) {
			if(request.getParameter("levelPicked")!=null && request.getParameter("levelSorter")!=null)
				sorters.put(request.getParameter("levelPicked"),new MetaInfo(request.getParameter("levelSorter"),request.getParameter("levelSortOrder")));
			else 	
				sorters.put(arf.getLevelPicked(),new MetaInfo(arf.getLevelSorter(),arf.getLevelSortOrder()));
			
			rd.importLevelSorters(sorters,ar.getHierarchies().size());
			rd.applyLevelSorter();
						
		}
		
		// test if the request was for column sorting purposes:
		if (sortBy != null) {
			httpSession.setAttribute("sortBy", sortBy);
			rd.setSorterColumn(sortBy);
			if (applySorter == null && ar.getHierarchies() != null
					&& !ar.getHierarchies().isEmpty()) {
				List<AmountCell> trailCells = rd.getTrailCells();
				for (AmountCell cell : trailCells) {
					if (sortBy.equals(cell.getColumn().getName())) {
						Set<AmpReportHierarchy> hierarchies = ar.getHierarchies();
						for (AmpReportHierarchy hierarchy : hierarchies) {
							sorters.put(hierarchy.getLevelId(), new MetaInfo(
									cell.getColumn().getAbsoluteColumnName(),
									rd.getSortAscending() ? "ascending": "descending"));
						}
						rd.importLevelSorters(sorters, ar.getHierarchies().size());
						rd.applyLevelSorter();
						break;
					}
				}
			}
		}
		
		if (rd==null) return mapping.findForward("index");
		rd.setGlobalHeadingsDisplayed(new Boolean(false));
		
		String viewFormat=request.getParameter("viewFormat");
		if(viewFormat==null) viewFormat=GenericViews.HTML;
		request.setAttribute("viewFormat",viewFormat);
	
		if(startRow==null && endRow==null) {
		    startRow="0";
		    Integer rpp = (Integer)request.getAttribute("recordsPerPage");
		    rpp = rpp - 1; //Zero indexed array 
		    endRow=rpp.toString();
		}
		
//		apply pagination if exists
		if(startRow!=null) rd.setStartRow(Integer.parseInt(startRow));
		if(endRow!=null) rd.setEndRow(Integer.parseInt(endRow));
		rd.setCurrentRowNumber(0);
		
	
		request.setAttribute("extraTitle",ar.getName());
		rd.setCurrentView(viewFormat);
		httpSession.setAttribute("report",rd);
		httpSession.setAttribute("reportMeta",ar);
		
		PersistenceManager.releaseSession(session);

		progressValue = progressValue + 10;// 20 is the weight of this process on the progress bar
		httpSession.setAttribute("progressValue", progressValue);
		
		httpSession.setAttribute("progressTotalRows", endRow);
		
		httpSession.setAttribute("progressValue", progressValue);


		
		return mapping.findForward("forward");
	}


	private void saveOrUpdateReportLog(TeamMember tm, AmpReports ar) {
		AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
		AmpReportLog reportlog = DbUtil.getAmpReportLog(ar.getAmpReportId(), ampTeamMember.getAmpTeamMemId());
		if(reportlog!=null){
			reportlog.setLastView(new Date());
			DbUtil.update(reportlog);
		}else{
			reportlog = new AmpReportLog();
			reportlog.setReport(ar);
			reportlog.setMember(ampTeamMember);
			reportlog.setLastView(new Date());
			DbUtil.add(reportlog);				
		}
	}
	
}