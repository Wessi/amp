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
import org.digijava.module.aim.form.FinancingBreakdownForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancialFilters;
import org.digijava.module.aim.helper.FinancingBreakdownWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.util.DbUtil;

public class FinancingBreakdownFilter extends TilesAction	{
	private static Logger logger = Logger.getLogger(FinancingBreakdownFilter.class);
	
	public ActionForward execute(ComponentContext context,
								 ActionMapping mapping, 
								 ActionForm form,
								 HttpServletRequest request, 
								 HttpServletResponse response) 
								 throws java.lang.Exception 	{
							 	
		String overallTotalCommitted = null ;
		String overallTotalDisbursed = null ;
		String overallTotalExpenditure = null ;
		FinancingBreakdownForm formBean = (FinancingBreakdownForm) form ;
		HttpSession session = request.getSession();
		TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
		FinancialFilters ff = CommonWorker.getFilters(teamMember.getTeamId(),"FP");
		formBean.setCalendarPresent(ff.isCalendarPresent());
		formBean.setCurrencyPresent(ff.isCurrencyPresent());
		formBean.setPerspectivePresent(ff.isPerspectivePresent());
		formBean.setYearRangePresent(ff.isYearRangePresent());
		formBean.setGoButtonPresent(ff.isGoButtonPresent());
		
		FilterParams fp = (FilterParams)session.getAttribute("filterParams");
		ApplicationSettings apps = null;
		if ( teamMember != null )	{
			apps = teamMember.getAppSettings();
		}

		if ( formBean.getCurrency() != null )
			fp.setCurrencyCode(formBean.getCurrency());
		else	{
			Currency curr = DbUtil.getCurrency(apps.getCurrencyId());
			fp.setCurrencyCode(curr.getCurrencyCode());
		}

		if ( formBean.getFiscalCalId() != 0 )
			fp.setFiscalCalId(new Long( formBean.getFiscalCalId() ));
		else	{
			fp.setFiscalCalId(apps.getFisCalId());
		}

		if ( formBean.getPerspective() != null )
			fp.setPerspective(formBean.getPerspective());
		else	{
			String perspective = CommonWorker.getPerspective(apps.getPerspective());
			fp.setPerspective(perspective);
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
		Long id = new Long(formBean.getAmpActivityId());
		Collection ampFundings = DbUtil.getAmpFunding(id);
		Collection fb = FinancingBreakdownWorker.getFinancingBreakdownList(id,ampFundings,fp);
		formBean.setFinancingBreakdown(fb);
		formBean.setYears(YearUtil.getYears());
		formBean.setFiscalYears(new ArrayList());
			formBean.setFiscalYears(DbUtil.getAllFisCalenders());
		formBean.setCurrencies(DbUtil.getAmpCurrency());
		overallTotalCommitted = FinancingBreakdownWorker.getOverallTotal(fb,0);
		formBean.setTotalCommitted(overallTotalCommitted);
		overallTotalDisbursed = FinancingBreakdownWorker.getOverallTotal(fb,1);
		formBean.setTotalDisbursed(overallTotalDisbursed);
		overallTotalExpenditure = FinancingBreakdownWorker.getOverallTotal(fb,2);
		formBean.setTotalExpended(overallTotalExpenditure);
		
		return  null;
	}
}	
		
		
		
		
		
		
		
		
		
		
		
		
		
	




