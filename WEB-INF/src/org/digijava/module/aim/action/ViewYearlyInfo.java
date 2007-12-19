package org.digijava.module.aim.action;

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
import org.digijava.module.aim.form.YearlyInfoForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancialFilters;
import org.digijava.module.aim.helper.PresentationUtil;
import org.digijava.module.aim.helper.QuarterlyInfoWorker;
import org.digijava.module.aim.helper.TabColors;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.TotalsQuarterly;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.helper.YearlyDiscrepancyWorker;
import org.digijava.module.aim.helper.YearlyInfoWorker;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class ViewYearlyInfo extends TilesAction {
	private static Logger logger = Logger.getLogger(ViewYearlyInfo.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		TeamMember teamMember = (TeamMember) session
				.getAttribute("currentMember");
		YearlyInfoForm formBean = (YearlyInfoForm) form;
		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		} else {
			formBean.setSessionExpired(false);
			FinancialFilters ff = CommonWorker.getFilters(teamMember
					.getTeamId(), "FP");
			formBean.setCalendarPresent(ff.isCalendarPresent());
			formBean.setCurrencyPresent(ff.isCurrencyPresent());
			formBean.setPerspectivePresent(ff.isPerspectivePresent());
			formBean.setYearRangePresent(ff.isYearRangePresent());
			formBean.setGoButtonPresent(ff.isGoButtonPresent());
			FilterParams fp = (FilterParams) session
					.getAttribute("filterParams");
			fp.setTransactionType(formBean.getTransactionType());
			ApplicationSettings apps = null;
			if (teamMember != null) {
				apps = teamMember.getAppSettings();
			}

			if (fp.getCurrencyCode() == null) {
				Currency curr = CurrencyUtil.getCurrency(apps.getCurrencyId());
				fp.setCurrencyCode(curr.getCurrencyCode());
			}

			if (fp.getFiscalCalId() == null) {
				fp.setFiscalCalId(apps.getFisCalId());
			}

			if (fp.getPerspective() == null) {
				String perspective = CommonWorker.getPerspective(apps
						.getPerspective());
				fp.setPerspective(perspective);
			}

			if (fp.getFromYear() == 0 || fp.getToYear() == 0) {
				int year = new GregorianCalendar().get(Calendar.YEAR);
				fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
				fp.setToYear(year+Constants.TO_YEAR_RANGE);
			}
			formBean.setPerspective(fp.getPerspective());
			formBean.setPerpsectiveName(DbUtil.getPerspective(fp.getPerspective()).getName());
			formBean.setCurrency(fp.getCurrencyCode());
			formBean.setFiscalCalId(fp.getFiscalCalId().longValue());
			formBean.setFromYear(fp.getFromYear());
			formBean.setToYear(fp.getToYear());
			session.setAttribute("filterParams", fp);
			if (fp.getPerspective().equals(Constants.DISCREPANCY)) {
				Collection discrepancies = YearlyDiscrepancyWorker
						.getYearlyDiscrepancy(fp);
				formBean.setDiscrepancies(discrepancies);
			} else {
				Collection yearlyInfo = YearlyInfoWorker.getYearlyInfo(fp);
				if (yearlyInfo.size() != 0) {
					formBean.setYearlyInfo(yearlyInfo);
					TotalsQuarterly tq = QuarterlyInfoWorker
							.getTotalsQuarterly(fp.getAmpFundingId(),
									Constants.MOFED,fp.getCurrencyCode());
					formBean.setTotalCommitted(tq.getTotalCommitted());
					formBean.setTotalDisbursed(tq.getTotalDisbursed());
                                        formBean.setTotalDisbOrdered(tq.getTotalDisbOrdered());
					formBean.setTotalUnExpended(tq.getTotalUnExpended());
					formBean.setTotalRemaining(tq.getTotalRemaining());
					formBean.setCurrCode(tq.getCurrencyCode());

					String strTotalPlanned = YearlyInfoWorker.getTotalYearly(
							yearlyInfo, Constants.PLANNED);
					formBean.setTotalPlanned(strTotalPlanned);
					String strTotalActual = YearlyInfoWorker.getTotalYearly(
							yearlyInfo, Constants.ACTUAL);
					formBean.setTotalActual(strTotalActual);
				}
			}

			TabColors tc = PresentationUtil.setTabColors(fp
					.getTransactionType());
			formBean.setCommitmentTabColor(tc.getCommitmentTabColor());
			formBean.setDisbursementTabColor(tc.getDisbursementTabColor());
			formBean.setExpenditureTabColor(tc.getExpenditureTabColor());
			formBean.setYears(YearUtil.getYears());
			formBean.setCurrencies(CurrencyUtil.getAmpCurrency());
			logger.info("Loading perspectives") ;
			formBean.setPerspectives(DbUtil.getAmpPerspective());
			logger.info("loaded " + formBean.getPerspectives().size() + " entries");
			formBean.setFiscalYears(new ArrayList());
			formBean.setFiscalYears(DbUtil.getAllFisCalenders());
		}
		return null;
	}
}

