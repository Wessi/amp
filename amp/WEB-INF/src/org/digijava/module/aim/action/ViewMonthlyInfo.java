package org.digijava.module.aim.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.form.MonthlyInfoForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancialFilters;
import org.digijava.module.aim.helper.MonthlyComparison;
import org.digijava.module.aim.helper.MonthlyInfo;
import org.digijava.module.aim.helper.MonthlyInfoWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.util.CurrencyUtil;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 *
 * @author 
 */
public class ViewMonthlyInfo extends TilesAction {

    private static Logger logger = Logger.getLogger(ViewYearlyInfo.class);

    public ActionForward execute(ComponentContext context,
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();
        TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
        MonthlyInfoForm monthlyForm = (MonthlyInfoForm) form;
        ActionErrors errors = new ActionErrors();

        FinancialFilters ff = CommonWorker.getFilters(teamMember.getTeamId(), "FP");
        monthlyForm.setCalendarPresent(ff.isCalendarPresent());
        monthlyForm.setCurrencyPresent(ff.isCurrencyPresent());
        monthlyForm.setYearRangePresent(ff.isYearRangePresent());
        monthlyForm.setGoButtonPresent(ff.isGoButtonPresent());
        FilterParams fp = (FilterParams) session.getAttribute("filterParams");
        fp.setTransactionType(monthlyForm.getTransactionType());
        ApplicationSettings apps = null;
        if (teamMember != null) {
            apps = teamMember.getAppSettings();
        }

        if (monthlyForm.getCurrency() != null) {
            fp.setCurrencyCode(monthlyForm.getCurrency());
        } else {
            if (fp.getCurrencyCode() == null) {
                Currency curr = CurrencyUtil.getCurrency(apps.getCurrencyId());
                fp.setCurrencyCode(curr.getCurrencyCode());
            }

        }

        if (monthlyForm.getFromYear() == 0 || monthlyForm.getToYear() == 0) {
            int year = new GregorianCalendar().get(Calendar.YEAR);
            fp.setFromYear(year - Constants.FROM_YEAR_RANGE);
            fp.setToYear(year + Constants.TO_YEAR_RANGE);
        } else {
            if(fp.getToYear()==0&&fp.getFromYear()==0){
            fp.setToYear(monthlyForm.getToYear());
            fp.setFromYear(monthlyForm.getFromYear());
            }
        }
        session.setAttribute("filterParams", fp);

        monthlyForm.setCurrency(fp.getCurrencyCode());
        monthlyForm.setFiscalCalId(fp.getFiscalCalId().longValue());
        monthlyForm.setFromYear(fp.getFromYear());
        monthlyForm.setToYear(fp.getToYear());
        monthlyForm.setYears(YearUtil.getYears());
        monthlyForm.setCurrencies(CurrencyUtil.getAmpCurrency());
            List monthlyInfos = new ArrayList();
            try {
                if (mapping.getInput().equals("/aim/viewMonthlyComparisons")) {
                  BigDecimal totalActualComm = new BigDecimal(0);
                  BigDecimal totalDisbOrders = new BigDecimal(0);
                  BigDecimal totalPlannedDisb=new BigDecimal(0);
                  BigDecimal totalActualDisb=new BigDecimal(0);
                  BigDecimal totalActualExp=new BigDecimal(0);
                  BigDecimal totalPlannedExp=new BigDecimal(0);
                  
                    monthlyInfos = MonthlyInfoWorker.getMonthlyComparisons(fp);
                    if(monthlyInfos!=null){
                       Iterator iterInfo = monthlyInfos.iterator();
                        while (iterInfo.hasNext()) {
                            MonthlyComparison comparison=(MonthlyComparison)iterInfo.next();
                            totalActualComm=totalActualComm.add(comparison.getActualCommitment());
                            totalActualDisb=totalActualComm.add(comparison.getActualDisbursement());
                            totalActualExp=totalActualExp.add(comparison.getActualExpenditure());
                            totalDisbOrders=totalDisbOrders.add(comparison.getDisbOrders());
                            totalPlannedDisb=totalPlannedDisb.add(comparison.getPlannedDisbursement());
                            totalPlannedExp=totalPlannedExp.add(comparison.getPlannedExpenditure());
                                
                        }
                       monthlyForm.setTotalActualCommitment(totalActualComm);
                       monthlyForm.setTotalActualDisbursement(totalActualDisb);
                       monthlyForm.setTotalActualExpenditure(totalActualExp);
                       monthlyForm.setTotalPlannedDisbursement(totalPlannedDisb);
                       monthlyForm.setTotalPlannedExpenditure(totalPlannedExp);
                       monthlyForm.setTotalDisbOrder(totalDisbOrders);
                       Collections.sort(monthlyInfos);
                    }

                } else {
                    monthlyInfos = MonthlyInfoWorker.getMonthlyData(fp);
                    BigDecimal totalActual = new BigDecimal(0); 
                    BigDecimal totalPlanned = new BigDecimal(0);
                    if (monthlyInfos != null) {
                        Iterator iterInfo = monthlyInfos.iterator();
                        while (iterInfo.hasNext()) {
                            MonthlyInfo info = (MonthlyInfo) iterInfo.next();

                            totalActual = totalActual.add(info.getActualAmount());
                            totalPlanned = totalActual.add(info.getPlannedAmount());
                    }
                        monthlyForm.setTotalActual(totalActual);
                        monthlyForm.setTotalPlanned(totalPlanned);
                }
            }
                
            } catch (DgException ex) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.monthlyview.unableLoadFundingDetails"));
                saveErrors(request, errors);

            }
            monthlyForm.setMonthlyInfoList(monthlyInfos);

        




        return null;

    }
    }
