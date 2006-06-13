package org.digijava.module.aim.action ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.form.QuarterlyInfoForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancialFilters;
import org.digijava.module.aim.helper.PresentationUtil;
import org.digijava.module.aim.helper.QuarterlyDiscrepancyWorker;
import org.digijava.module.aim.helper.QuarterlyInfoWorker;
import org.digijava.module.aim.helper.TabColors;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.TotalsQuarterly;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class QuarterlyInfoFilter extends TilesAction	{
	private static Logger logger = Logger.getLogger(QuarterlyInfoFilter.class);
	
	public ActionForward execute(ComponentContext context,
								 ActionMapping mapping, 
								 ActionForm form,
								 HttpServletRequest request, 
								 HttpServletResponse response) 
								 throws IOException,ServletException {
								 	
		QuarterlyInfoForm formBean = (QuarterlyInfoForm)form;
		HttpSession session = request.getSession();
		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		}
		else	{
			formBean.setSessionExpired(false);
			TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
			
			FinancialFilters ff = CommonWorker.getFilters(teamMember.getTeamId(),"FP");
			formBean.setCalendarPresent(ff.isCalendarPresent());
			formBean.setCurrencyPresent(ff.isCurrencyPresent());
			formBean.setPerspectivePresent(ff.isPerspectivePresent());
			formBean.setYearRangePresent(ff.isYearRangePresent());
			formBean.setGoButtonPresent(ff.isGoButtonPresent());
			FilterParams fp = (FilterParams)session.getAttribute("filterParams");
			fp.setTransactionType(formBean.getTransactionType());
			
			ApplicationSettings apps = null;
			if ( teamMember != null )	{
				apps = teamMember.getAppSettings();
			}
	
			if ( formBean.getCurrency() != null )
				fp.setCurrencyCode(formBean.getCurrency());
			else	{
				Currency curr = CurrencyUtil.getCurrency(apps.getCurrencyId());
				fp.setCurrencyCode(curr.getCurrencyCode());
			}
	
			if ( formBean.getFiscalCalId() != 0 )
				fp.setFiscalCalId(new Long( formBean.getFiscalCalId() ));
			else	{
				fp.setFiscalCalId(apps.getFisCalId());
			}
	
			logger.debug("Perspective = " + formBean.getPerspective());
			if ( formBean.getPerspective() != null ) {
				fp.setPerspective(formBean.getPerspective());
				//logger.debug("If - Setting perspective as " + formBean.getPerspective());
			}
			else	{
				String perspective = CommonWorker.getPerspective(apps.getPerspective());
				fp.setPerspective(perspective);
				//logger.debug("Else - Setting perspective as " + perspective);
			}
	
			if ( formBean.getFromYear()==0 || formBean.getToYear()==0 )	{
				int year = new GregorianCalendar().get(Calendar.YEAR);
				fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
				fp.setToYear(year+Constants.TO_YEAR_RANGE);
			}
			else	{
				fp.setToYear(formBean.getToYear());
				fp.setFromYear(formBean.getFromYear());
			}
			session.setAttribute("filterParams",fp);
			formBean.setPerpsectiveName(DbUtil.getPerspective(fp.getPerspective()).getName());			
			formBean.setYears(YearUtil.getYears());
			formBean.setCurrencies(CurrencyUtil.getAmpCurrency());
			formBean.setPerspectives(DbUtil.getAmpPerspective());
			formBean.setFiscalYears(new ArrayList());
			formBean.setFiscalYears(DbUtil.getAllFisCalenders());
			
			TabColors tc = PresentationUtil.setTabColors(fp.getTransactionType());
			formBean.setCommitmentTabColor(tc.getCommitmentTabColor());
			formBean.setDisbursementTabColor(tc.getDisbursementTabColor());
			formBean.setExpenditureTabColor(tc.getExpenditureTabColor());
			
			logger.debug("formBean.getPerspective() = " + formBean.getPerspective());
			if ( fp.getPerspective().equals("DI") ) {
				Collection c = QuarterlyDiscrepancyWorker.getDiscrepancy(fp);
				formBean.setDiscrepancies(c);
			}
			else {
				Collection c = QuarterlyInfoWorker.getQuarterlyInfo(fp);
				if ( c.size() != 0 )	{
					formBean.setQuarterlyInfo(c);
					TotalsQuarterly tq = QuarterlyInfoWorker.getTotalsQuarterly(fp.getAmpFundingId(),fp.getPerspective(),fp.getCurrencyCode());
					formBean.setTotalCommitted(tq.getTotalCommitted());
					formBean.setTotalDisbursed(tq.getTotalDisbursed());														  	
					formBean.setTotalUnExpended(tq.getTotalUnExpended());																							
					formBean.setTotalRemaining(tq.getTotalRemaining());												
				}
			}
		}
		return null;
	}
}
