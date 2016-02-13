package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.form.FiscalCalendarForm;
import javax.servlet.http.*;
import java.util.*;

public class FiscalCalendarManager extends Action {

		  private static Logger logger = Logger.getLogger(FiscalCalendarManager.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {

					 HttpSession session = request.getSession();
					 if (session.getAttribute("ampAdmin") == null) {
								return mapping.findForward("index");
					 } else {
								String str = (String)session.getAttribute("ampAdmin");
								if (str.equals("no")) {
										  return mapping.findForward("index");
								}
					 }					 

					 final int NUM_RECORDS = 10;

					 Collection fisCal = new ArrayList();
					 FiscalCalendarForm fcForm = (FiscalCalendarForm) form;
					 int page = 0;
					 
					 logger.debug("In fiscal calendar manager action");
					 
					 if (request.getParameter("page") == null) {
								page = 1;
					 } else {
								/*
								 * check whether the page is a valid integer
								 */
								page = Integer.parseInt(request.getParameter("page"));
					 }

					 Collection ampFisCal = (Collection)session.getAttribute("ampFisCal");
					 if (ampFisCal == null) {
								ampFisCal = DbUtil.getAllFisCalenders();
								session.setAttribute("ampFisCal",ampFisCal);
					 }
					 
					 int numPages = ampFisCal.size() / NUM_RECORDS;
					 numPages += (ampFisCal.size() % NUM_RECORDS != 0) ? 1 : 0;

					 /*
					  * check whether the numPages is less than the page . if yes return error.
					  */
					 
					 int stIndex = ((page - 1) * NUM_RECORDS) + 1;
					 int edIndex = page * NUM_RECORDS;
					 if (edIndex > ampFisCal.size()) {
								edIndex = ampFisCal.size();
					 }

					 Vector vect = new Vector();
					 vect.addAll(ampFisCal);
					 
					 final String[] MONTHS = { "January","February","March","April","May","June","July",
					 						   "August","September","October","November","December"};
					 Vector month = new Vector();
					 Calendar c = Calendar.getInstance();
					 int mon;
					 
					 for (int i = (stIndex-1); i < edIndex; i++) {
								fisCal.add(vect.get(i));
								mon = ((AmpFiscalCalendar)vect.get(i)).getStartMonthNum().intValue();
								c.set(Calendar.MONTH, mon - 1);
								month.add(MONTHS[c.get(Calendar.MONTH)]);
					 }
					 
					 Collection pages = null;
					 
					 if (numPages > 1) {
								pages = new ArrayList();
								for (int i = 0;i < numPages;i ++) {
										  Integer pageNum = new Integer(i+1);
										  pages.add(pageNum);
								}
					 }
					 
					 fcForm.setFiscalCal(fisCal);
					 fcForm.setPages(pages);
					 fcForm.setMonth(month);
					
					 logger.debug("FiscalCalendar manager returning");
					 return mapping.findForward("forward");
		  }
}