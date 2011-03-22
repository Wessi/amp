package org.digijava.module.gis.form;

import java.util.List;

import org.apache.struts.action.ActionForm;


/**
 * Form for GIS Dashboard actions.
 * @author George Kvizhinadze
 *
 */
public class GisRegReportForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String regCode;
    private String regName;
    private int mapLevel;
    private long sectorId;
    private String sectorIdStr;
    private long startDate;
    private long endDate;

    private String startYear;
    private String endYear;

    private String actualCommitmentsStr;
    private String actualDisbursementsStr;
    private String actualExpendituresStr;

    private Long primarySectorSchemeId;

    private String selSectorName;

    private List activityLocationFundingList;
    private String selectedCurrency;

    public String getSectorIdStr() {
        return sectorIdStr;
    }

    public void setSectorIdStr(String sectorIdStr) {
        this.sectorIdStr = sectorIdStr;
    }

    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(String selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }
    public String getActualCommitmentsStr() {
        return actualCommitmentsStr;
    }

    public String getActualDisbursementsStr() {
        return actualDisbursementsStr;
    }

    public String getActualExpendituresStr() {
        return actualExpendituresStr;
    }

    public long getEndDate() {
        return endDate;
    }

    public int getMapLevel() {
        return mapLevel;
    }

    public long getSectorId() {
        return sectorId;
    }

    public long getStartDate() {
        return startDate;
    }

    public String getRegCode() {
        return regCode;
    }

    public String getStartYear() {
        return startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public String getRegName() {
        return regName;
    }

    public String getSelSectorName() {
        return selSectorName;
    }

    public List getActivityLocationFundingList() {
        return activityLocationFundingList;
    }

    public Long getPrimarySectorSchemeId() {
        return primarySectorSchemeId;
    }

    public void setActualCommitmentsStr(String actualCommitmentsStr) {
        this.actualCommitmentsStr = actualCommitmentsStr;
    }

    public void setActualDisbursementsStr(String actualDisbursementsStr) {
        this.actualDisbursementsStr = actualDisbursementsStr;
    }

    public void setActualExpendituresStr(String actualExpendituresStr) {
        this.actualExpendituresStr = actualExpendituresStr;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public void setMapLevel(int mapLevel) {
        this.mapLevel = mapLevel;
    }

    public void setSectorId(long sectorId) {
        this.sectorId = sectorId;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public void setSelSectorName(String selSectorName) {
        this.selSectorName = selSectorName;
    }

    public void setActivityLocationFundingList(List activityLocationFundingList) {
        this.activityLocationFundingList = activityLocationFundingList;
    }

    public void setPrimarySectorSchemeId(Long primarySectorSchemeId) {
        this.primarySectorSchemeId = primarySectorSchemeId;
    }


}
