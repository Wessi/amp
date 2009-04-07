/*
 * GetRegionalFundings.java
 * Created : 06-Oct-2005
 */
package org.digijava.module.aim.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.form.RegionalFundingForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.RegionalFundingsHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * Action class for getting the regional funding details of an activity
 * 
 * @author Priyajith
 */
public class GetRegionalFundings extends TilesAction {

	private static Logger logger = Logger.getLogger(GetRegionalFundings.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		HttpSession sesion = request.getSession();
		TeamMember tm = (TeamMember) sesion.getAttribute("currentMember");
		request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
		try {
			RegionalFundingForm rfForm = (RegionalFundingForm) form;

			String currCode = "";
			long calCode = -1;

			currCode = CurrencyUtil.getAmpcurrency(
					tm.getAppSettings().getCurrencyId()).getCurrencyCode();
			calCode = tm.getAppSettings().getFisCalId().longValue();

			rfForm.setGoButton(false);
			rfForm.setCurrFilter(false);
			rfForm.setCalFilter(false);

			Collection filters = DbUtil.getFilters(tm.getTeamId(), DbUtil
					.getPageId(Constants.REGIONAL_FUNDING_PAGE_CODE));
			Iterator itr = filters.iterator();
			while (itr.hasNext()) {
				AmpFilters filter = (AmpFilters) itr.next();
				if (filter.getFilterName().equalsIgnoreCase(
						Constants.CALENDAR_FILTER)) {
					if (rfForm.getCalFilterValue() != -1) {
						calCode = rfForm.getCalFilterValue();
					}
					rfForm.setCalFilter(true);
					rfForm.setGoButton(true);
				} else if (filter.getFilterName().equalsIgnoreCase(
						Constants.CURRENCY_FILTER)) {
					if (rfForm.getCurrFilterValue() != null
							&& rfForm.getCurrFilterValue().trim().length() > 0) {
						currCode = rfForm.getCurrFilterValue().trim();
					}
					rfForm.setCurrFilter(true);
					rfForm.setGoButton(true);
				}
			}
			rfForm.setCalFilterValue(calCode);
			rfForm.setCurrFilterValue(currCode);

			if (rfForm.isCalFilter()) {
				if (rfForm.getFiscalCalendars() == null
						|| rfForm.getFiscalCalendars().size() == 0) {
					rfForm.setFiscalCalendars(DbUtil.getAllFisCalenders());
				}
			}
			if (rfForm.isCurrFilter()) {
				if (rfForm.getCurrencies() == null
						|| rfForm.getCurrencies().size() == 0) {
					rfForm.setCurrencies(CurrencyUtil.getAmpCurrency());
				}
			}

			if (rfForm.getRegionId() != null
					&& rfForm.getRegionId().longValue() > 0) {
				Collection regFunds = ActivityUtil.getRegionalFundings(
						new Long(rfForm.getAmpActivityId()), rfForm
								.getRegionId());
				ArrayList temp = RegionalFundingsHelper.getRegionalFundings(
						regFunds, currCode, calCode);
				rfForm.setRegionalFundings(temp);
				return null;
			}
			if (rfForm.getAmpActivityId() > 0) {
				Collection regFunds = ActivityUtil
						.getRegionalFundings(new Long(rfForm.getAmpActivityId()));
				ArrayList temp = RegionalFundingsHelper.getRegionalFundings(
						regFunds, currCode, calCode);

				BigDecimal totComm = new BigDecimal(0);
				BigDecimal totDisb = new BigDecimal(0);
				BigDecimal totUnDisb = new BigDecimal(0);
				BigDecimal totExp = new BigDecimal(0);
				BigDecimal totUnExp = new BigDecimal(0);

				for (int i = 0; i < temp.size(); i++) {
					RegionalFunding regFund = (RegionalFunding) temp.get(i);
					regFund.setTotUnDisbursed(regFund.getTotCommitments().subtract( regFund.getTotDisbursements()));
					regFund.setTotUnExpended(regFund.getTotDisbursements().subtract( regFund.getTotExpenditures()) );
					temp.set(i, regFund);
					totComm =totComm.add( regFund.getTotCommitments());
					totDisb =totDisb.add(regFund.getTotDisbursements());
					totUnDisb =totUnDisb.add(regFund.getTotUnDisbursed());
					totExp =totExp.add(regFund.getTotExpenditures());
					totUnExp =totUnExp.add( regFund.getTotUnExpended());
				}

				rfForm.setTotCommitments(totComm);
				rfForm.setTotDisbursements(totDisb);
				rfForm.setTotExpenditures(totExp);
				rfForm.setTotUnDisbursed(totUnDisb);
				rfForm.setTotUnExpended(totUnExp);
				rfForm.setRegionalFundings(temp);
			}
			return null;

		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
			e.printStackTrace(System.out);
		}

		return null;
	}
}
