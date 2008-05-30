package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.form.YearlyComparisonsForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.helper.YearlyComparisonsWorker;
import org.digijava.module.aim.helper.YearlyDiscrepancyAllWorker;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class YearlyComparisonsFilter extends TilesAction	{
	private static Logger logger = Logger.getLogger(YearlyComparisonsFilter.class);

	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
								 throws java.lang.Exception 	{

		YearlyComparisonsForm formBean = (YearlyComparisonsForm) form;
		HttpSession session = request.getSession();
		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		}
		else	{
			formBean.setSessionExpired(false);
			TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
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
			formBean.setYears(YearUtil.getYears());
			formBean.setCurrencies(CurrencyUtil.getAmpCurrency());
			formBean.setFiscalYears(new ArrayList());
			formBean.setFiscalYears(DbUtil.getAllFisCalenders());
			if ( fp.getPerspective().equals("DI") )	{
				Collection c  = YearlyDiscrepancyAllWorker.getDiscrepancy(fp);
				formBean.setYearlyDiscrepanciesAll(c);
			}
			else	{
				Collection c = YearlyComparisonsWorker.getYearlyComparisons(fp) ;
				if ( c.size() != 0 )
					formBean.setYearlyComparisons(c);
			}
		}
		return null;
	}
}
