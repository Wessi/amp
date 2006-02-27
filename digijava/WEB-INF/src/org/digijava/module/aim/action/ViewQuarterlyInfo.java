package org.digijava.module.aim.action;

import java.util.Calendar;
import java.util.Collection;
import java.util.ArrayList;
import java.util.GregorianCalendar;

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
import org.digijava.module.aim.util.DbUtil;

public class ViewQuarterlyInfo extends TilesAction	{
	private static Logger logger = Logger.getLogger(ViewQuarterlyInfo.class);
	
	public ActionForward execute(ComponentContext context,
								 ActionMapping mapping, 
								 ActionForm form,
								 HttpServletRequest request, 
								 HttpServletResponse response) 
								 throws java.lang.Exception 	{
		HttpSession session = request.getSession();
		TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");						 	
		QuarterlyInfoForm formBean = (QuarterlyInfoForm)form;
		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		}
		else	{
			formBean.setSessionExpired(false);
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
	
			if ( fp.getCurrencyCode() == null )	{
				Currency curr = DbUtil.getCurrency(apps.getCurrencyId());
				fp.setCurrencyCode(curr.getCurrencyCode());
			}
	
			if ( fp.getFiscalCalId() == null )	{
				fp.setFiscalCalId(apps.getFisCalId());
			}
	
			if ( fp.getPerspective() == null )	{
				String perspective = CommonWorker.getPerspective(apps.getPerspective());
				fp.setPerspective(perspective);
			}
	
			if ( fp.getFromYear()==0 || fp.getToYear()==0 )	{
				int year = new GregorianCalendar().get(Calendar.YEAR);
				fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
				fp.setToYear(year+Constants.TO_YEAR_RANGE);
			}
			formBean.setPerspective(fp.getPerspective());
			formBean.setCurrency(fp.getCurrencyCode());
			formBean.setFiscalCalId(fp.getFiscalCalId().longValue());
			formBean.setFromYear(fp.getFromYear());
			formBean.setToYear(fp.getToYear());
			session.setAttribute("filterParams",fp);	
			
			formBean.setYears(YearUtil.getYears());
			formBean.setCurrencies(DbUtil.getAmpCurrency());
			formBean.setFiscalYears(new ArrayList());
			formBean.setFiscalYears(DbUtil.getAllFisCalenders());
			
			TabColors tc = PresentationUtil.setTabColors(fp.getTransactionType());
			formBean.setCommitmentTabColor(tc.getCommitmentTabColor());
			formBean.setDisbursementTabColor(tc.getDisbursementTabColor());
			formBean.setExpenditureTabColor(tc.getExpenditureTabColor());
			
			if ( formBean.getPerspective().equals("DI") ) 	{
				Collection c = QuarterlyDiscrepancyWorker.getDiscrepancy(fp);
				formBean.setDiscrepancies(c);
			}
			else	{
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
