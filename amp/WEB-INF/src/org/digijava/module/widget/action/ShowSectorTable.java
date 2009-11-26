package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.widget.dbentity.AmpSectorOrder;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;
import org.digijava.module.widget.dbentity.AmpSectorTableYear;
import org.digijava.module.widget.form.ShowSectorTableForm;
import org.digijava.module.widget.helper.SectorTableHelper;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.SectorTableWidgetUtil;

public class ShowSectorTable extends Action {

    private static Logger logger = Logger.getLogger(ShowSectorTable.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ShowSectorTableForm tableForm = (ShowSectorTableForm) form;

        // translate headers
        Long siteId = RequestUtils.getSiteDomain(request).getSite().getId();
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        String headingUSMil = TranslatorWorker.translateText("US$ millions", langCode, siteId);
        String headingFY = TranslatorWorker.translateText("FY", langCode, siteId);
        String headingPercent = TranslatorWorker.translateText("Of", langCode, siteId);
        String headingOther = TranslatorWorker.translateText("Other", langCode, siteId);
        String headingTotal = TranslatorWorker.translateText("Total", langCode, siteId);

        List<String> yearsHeader = new ArrayList<String>();
        List<SectorTableHelper> sectorsInfo = new ArrayList<SectorTableHelper>();
        ArrayList<String> totalValues = new ArrayList<String>();
        ArrayList<String> otherValues = new ArrayList<String>();
        String fiscalCalendarId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
        AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(Long.parseLong(fiscalCalendarId));
        Long id = tableForm.getWidgetId();
        AmpSectorTableWidget secTableWidget = SectorTableWidgetUtil.getAmpSectorTableWidget(id);
        boolean donorColumnAdded = false;
        if (secTableWidget.getDonorYear() != null) {
            donorColumnAdded = true;
            Collection<AmpOrganisation> donors = DbUtil.getAmpOrganisations(false);
            tableForm.setDonors(new ArrayList(donors));
        }
        tableForm.setDonorColumnAdded(donorColumnAdded);
        List<AmpSectorTableYear> sectorTableYears = new ArrayList(secTableWidget.getYears());
        List<AmpSectorOrder> sectorOrders = new ArrayList(secTableWidget.getSectorsColumns());
        Iterator<AmpSectorOrder> sectorOrderIter = sectorOrders.iterator();
        SectorTableHelper sectorTableRowOther = new SectorTableHelper();
        sectorTableRowOther.setSectorName(headingOther);
        sectorTableRowOther.setSectorId(SectorTableHelper.OTHER_ROW_SECTOR_ID);

        SectorTableHelper sectorTableRowEmpty = new SectorTableHelper();
        sectorTableRowEmpty.setEmptyRow(true);
        sectorTableRowEmpty.setSectorId(SectorTableHelper.EMPTY_ROW_SECTOR_ID);

        SectorTableHelper sectorTableRowTotal = new SectorTableHelper();
        sectorTableRowTotal.setSectorName(headingTotal);
        sectorTableRowTotal.setApplyStyle(true);
        sectorTableRowTotal.setSectorId(SectorTableHelper.TOTAL_ROW_SECTOR_ID);

        //here we will put whole amounts for each year
        List<Long> wholeAmounts = new ArrayList<Long>();

        /*here we will use this variable to store sums of amounts for each year,
        i.e. the suming amounts for all selected  sectors for each year*/

        List<Long> otherAmounts = new ArrayList<Long>();

         /* here we will use this variable to store sums of percents for each year,
          * i.e. the suming pecents for all selected  sectors for each year
          * we are using such approach because of round values problem
          * (round may give us 101% instead of 100 ;) )
          */
        List<Long> otherPercents = new ArrayList<Long>();

        boolean isTotalsCalcualted = false;
        while (sectorOrderIter.hasNext()) {
            AmpSectorOrder sectorOrder = sectorOrderIter.next();
            
            //creating row  for each sector
            SectorTableHelper sectorTableRow = new SectorTableHelper();
            AmpSector sector = sectorOrder.getSector();
            Long sectorId = sector.getAmpSectorId();
            sectorTableRow.setSectorId(sectorId);
            sectorTableRow.setSectorName(sector.getName());
            ArrayList<String> cells = new ArrayList<String>();
            Iterator<AmpSectorTableYear> totalYearIter = sectorTableYears.iterator();

            /*
             * we will use it to get sum for selected year.
             * e.g. if we have two sector A and B and two years 2008,2009
             * we will put 2008, 2009 years value for the A to the otherAmounts list
             * respectivly with index 0,1;
             * for sector B we will add its 2008 value to A's 2008 values using index =0
             * and add its 2009 value to A's 2009 values using index =1
             */
            int index = 0;
            while (totalYearIter.hasNext()) {
                AmpSectorTableYear sectorTableYear = totalYearIter.next();
                Long year = sectorTableYear.getYear();
                Date startDate = ChartWidgetUtil.getStartOfYear(year.intValue(), calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
                //we need data including the last day of toYear,this is till the first day of toYear+1
                int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
                Date endDate = new Date(ChartWidgetUtil.getStartOfYear(year.intValue() + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum()).getTime() - MILLISECONDS_IN_DAY);
                Long amount = SectorTableWidgetUtil.calculateFunding(null, new Long[]{sectorId}, startDate, endDate);
                Long wholeAmount = null;
                //we are calculating whole amounts only once for each year
                if (wholeAmounts.size() <= index) {
                    wholeAmount = SectorTableWidgetUtil.calculateFunding(null, null, startDate, endDate);
                    wholeAmounts.add(wholeAmount);
                } else {
                    wholeAmount = wholeAmounts.get(index);
                }
                Long allExceptOthersAmount = null;

                // summing sector amounts for each year separately
                if (otherAmounts.size() <= index) {
                    allExceptOthersAmount =amount;
                    otherAmounts.add(allExceptOthersAmount);
                } else {
                    allExceptOthersAmount = otherAmounts.get(index)+amount;
                    otherAmounts.remove(index);
                    otherAmounts.add(index,allExceptOthersAmount);
                }
                
                String heading = "";
                if (sectorTableYear.getType().equals(AmpSectorTableYear.TOTAL_TYPE_YEAR)) {
                    if (calendar.getIsFiscal()) {
                        heading = headingFY + " " + year + "/" + (year + 1) + " (" + headingUSMil + ")";
                    } else {
                        heading = year + " (" + headingUSMil + ")";
                    }
                    // adding total values
                    if (!isTotalsCalcualted) {
                        totalValues.add(wholeAmount + "");
                    }
                    if(otherValues.size()<=index){
                        otherValues.add((wholeAmount.longValue()-allExceptOthersAmount.longValue())+"");
                    }
                    else{
                        otherValues.remove(index);
                        otherValues.add(index,(wholeAmount.longValue()-allExceptOthersAmount.longValue())+"");
                    }

                    cells.add(amount + "");
                    if (otherPercents.size() <= index) {
                        otherPercents.add(0l);
                    }
                } else {
                    if (calendar.getIsFiscal()) {
                        heading = headingFY + " % " + headingPercent + " " + year + "/" + (year + 1);
                    } else {
                        heading = " % " + headingPercent + " " + year;
                    }
                    long percent = 0;
                    long allExcludeOthersPercent=0;
                    if (wholeAmount != 0) {
                        percent = Math.round(1.0 * amount / wholeAmount * 100);
                    }
                    if (otherPercents.size() <= index) {
                        allExcludeOthersPercent=percent;
                        otherPercents.add(allExcludeOthersPercent);
                    } else {
                        allExcludeOthersPercent = otherPercents.get(index).longValue() + percent;
                        otherPercents.remove(index);
                        otherPercents.add(index, allExcludeOthersPercent);

                    }
                    long wholePercent = 0;
                    if (wholeAmount != 0) {
                        wholePercent = 100;
                    }
                    if(otherValues.size()<=index){
                        otherValues.add((wholePercent-allExcludeOthersPercent)+"%");
                    }
                    else{
                        otherValues.remove(index);
                        otherValues.add(index,(wholePercent-allExcludeOthersPercent)+"%");
                    }
                    if (!isTotalsCalcualted) {
                        totalValues.add(wholePercent + "%");
                    }
                    cells.add(percent + "%");
                }
                if (!isTotalsCalcualted) {
                    yearsHeader.add(heading);
                }
                index++;
            }
            isTotalsCalcualted = true;
            // adding year cells values to row
            sectorTableRow.setValues(cells);
            sectorsInfo.add(sectorTableRow);
        }
        tableForm.setYears(yearsHeader);
        sectorTableRowTotal.setValues(totalValues);
        sectorTableRowOther.setValues(otherValues);
        sectorsInfo.add(sectorTableRowOther);
        sectorsInfo.add(sectorTableRowEmpty);
        sectorsInfo.add(sectorTableRowTotal);
        tableForm.setSectorsInfo(sectorsInfo);

        return mapping.findForward("forward");
    }
}


