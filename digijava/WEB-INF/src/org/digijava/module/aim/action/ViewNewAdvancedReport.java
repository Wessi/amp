/**
 * ViewNewAdvancedReport.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;


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
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.GroupReportData;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 15, 2006
 *
 */
public class ViewNewAdvancedReport extends Action {

	
	private static Logger logger = Logger.getLogger(ViewNewAdvancedReport.class) ;
	
	/**
	 * 
	 */
	public ViewNewAdvancedReport() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
			{

				GroupReportData rd=ARUtil.generateReport(mapping,form,request,response);
				HttpSession hs = request.getSession();
				String ampReportId = request.getParameter("ampReportId");
				
				if (rd==null) return mapping.findForward("index");
		
				Session session = PersistenceManager.getSession();
				
				String viewFormat=request.getParameter("viewFormat");
				if(viewFormat==null) viewFormat=GenericViews.HTML;

				request.setAttribute("viewFormat",viewFormat);
				
				AmpReports reportMeta = (AmpReports) session.get(AmpReports.class, new Long(ampReportId));

				request.setAttribute("extraTitle",reportMeta.getName());
				
				rd.setCurrentView(viewFormat);
				hs.setAttribute("report",rd);
				hs.setAttribute("reportMeta",reportMeta);
				
				session.close();
				
				return mapping.findForward("forward");
			}
	
}
