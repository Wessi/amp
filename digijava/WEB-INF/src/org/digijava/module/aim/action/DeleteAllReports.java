package org.digijava.module.aim.action;

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
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;


public class DeleteAllReports extends Action {

	  private static Logger logger = Logger.getLogger(DeleteAllReports.class);

	  public ActionForward execute(ActionMapping mapping,
							ActionForm form,
							HttpServletRequest request,
							HttpServletResponse response) throws java.lang.Exception {

		  	ActionErrors myErrors	= new ActionErrors();
		  	HttpSession session = request.getSession();
		  	//logger.info("In delete reports11111111");
		  	boolean hasDeletePrivilege = false;
		  	TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		  	
		  	if ( tm == null ) {
		  		myErrors.add("title", new ActionError("error.aim.cannotdeleteReports.cannotReportDeleted"));
				saveErrors(request,myErrors);
		  		logger.error("No 'currentMember' property in session");
		  		return mapping.findForward("forward");
		  	}
		  	
		  	if ( ! (hasDeletePrivilege=tm.getDelete()) ) {
		  		myErrors.add("title", new ActionError("error.aim.cannotdeleteReports.noPrivilegeToDelete"));
				saveErrors(request,myErrors);
		  		logger.error("Not allowed to delete report");
		  		return mapping.findForward("forward");
		  	}
		  	
//				if (session.getAttribute("teamLeadFlag") == null) {
//					logger.info("not a team Lead!");
//					//return mapping.findForward("forward");
//				} else {
//					String str = (String)session.getAttribute("teamLeadFlag");
//					logger.info("teamFlag ..... "+session.getAttribute("teamLeadFlag")+ " strrrr "+str);
//					if (str.equals("true")) {
//						logger.info("yes a team Lead!");
//						 hasDeletePrivilege	= true;
//						//return mapping.findForward("forward");
//					}
//					else
//					{
//						logger.info("NOPE!! team Lead!");
//					}
//				}
				 if(hasDeletePrivilege)
				 {
//					 ReportsForm repForm = (ReportsForm) form;
//					 logger.info("In delete reports");
//					 logger.info(repForm.getReportId());
					 String a = request.getParameter("rid");
					 logger.info(" this is rid...."+request.getParameter("rid")+" thisisa...."+a);
					 Long id = new Long(a);
					 if ( id != null) {
						 ReportsForm ampReport = new ReportsForm();
						
						 ampReport.setReportId(id);
						 logger.info(" this is setReportid "+ampReport.getReportId());
				
								 DbUtil.deleteReportsCompletely(id);
								
								 ActionErrors errors = new ActionErrors();
									errors.add("title", new ActionError(
											"error.aim.deleteReports.reportDeleted"));
									saveErrors(request,errors);
						
								logger.debug("Report deleted");				
					 }
				 }
				 return mapping.findForward("forward");
				
	  }
}

