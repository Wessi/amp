// Generated by delombok at Mon Mar 24 00:10:06 EET 2014
package org.digijava.module.aim.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.FundingInformationItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingDetailComparator;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;


public class FundingCalculationsHelper {
    private static Logger logger = Logger.getLogger(FundingCalculationsHelper.class);
    List<FundingDetail> fundDetailList = new ArrayList<FundingDetail>();
    DecimalWraper totPlanDisb = new DecimalWraper();
    DecimalWraper totPlannedComm = new DecimalWraper();
    DecimalWraper totPlannedExp = new DecimalWraper();
    DecimalWraper totPlannedDisbOrder = new DecimalWraper();
    DecimalWraper totPlannedReleaseOfFunds = new DecimalWraper();
    DecimalWraper totPlannedEDD = new DecimalWraper();
    DecimalWraper totPlannedArrears = new DecimalWraper();
    DecimalWraper totActualComm = new DecimalWraper();
    DecimalWraper totActualDisb = new DecimalWraper();
    DecimalWraper totActualExp = new DecimalWraper();
    DecimalWraper totBilaterlaSscExp = new DecimalWraper();
    DecimalWraper totTriangularSscExp = new DecimalWraper();    
    DecimalWraper totActualDisbOrder = new DecimalWraper();
    DecimalWraper totActualReleaseOfFunds = new DecimalWraper();
    DecimalWraper totActualEDD = new DecimalWraper();
    DecimalWraper totActualArrears = new DecimalWraper();
    DecimalWraper totPipelineDisb = new DecimalWraper();
    DecimalWraper totOfficialDevelopmentAidDisb = new DecimalWraper();
    DecimalWraper totBilateralSscDisb = new DecimalWraper();
    DecimalWraper totTriangularSscDisb = new DecimalWraper();   
    DecimalWraper totPipelineComm = new DecimalWraper();
    DecimalWraper totPipelineExp = new DecimalWraper();
    DecimalWraper totOfficialDevelopmentAidExp = new DecimalWraper();
    DecimalWraper totTriangularSccExp = new DecimalWraper();
    DecimalWraper totBilateralSScExp = new DecimalWraper(); 
    DecimalWraper totPipelineDisbOrder = new DecimalWraper();
    DecimalWraper totPipelineReleaseOfFunds = new DecimalWraper();
    DecimalWraper totPipelineEDD = new DecimalWraper();
    DecimalWraper totPipelineArrears = new DecimalWraper();
    DecimalWraper totBilateralSscArrears = new DecimalWraper();
    DecimalWraper totBilateralSscDisbOrder = new DecimalWraper();
    DecimalWraper totBilateralSscComm = new DecimalWraper();
    DecimalWraper totOfficialDevelopmentAidComm = new DecimalWraper();  
    DecimalWraper totTriangularSscArrears = new DecimalWraper();
    DecimalWraper totTriangularSscComm = new DecimalWraper();
    DecimalWraper totTriangularSscDisbOrder = new DecimalWraper();
    DecimalWraper totOfficialDevAidArrears = new DecimalWraper();
    DecimalWraper totOdaSscComm = new DecimalWraper();
    DecimalWraper totBilateralSccEDD = new DecimalWraper();
    
    @SuppressWarnings("serial")
    private static Map<Integer, String> transactionTypeLabelMap = new HashMap<Integer, String>(){{
        put(Constants.COMMITMENT, "Commitment");
        put(Constants.DISBURSEMENT, "Disbursement");
        put(Constants.EXPENDITURE, "Expenditure");
        put(Constants.RELEASE_OF_FUNDS, "Release of Funds");
        put(Constants.ESTIMATED_DONOR_DISBURSEMENT, "Estimated Disbursement");
        put(Constants.DISBURSEMENT_ORDER, "Disbursement Order");
        put(Constants.MTEFPROJECTION, "MTEF Projection");
        put(Constants.ARREARS, "Arrears");
    }};
    
    public static String getTransactionTypeLabel (int type) {
        return transactionTypeLabelMap.get(type);
    }
    
    private static String combineKeys(int transType, HardCodedCategoryValue adjKey) {
        return String.format("%s %s", getTransactionTypeLabel(transType), adjKey.getValueKey());
    }
    
    @SuppressWarnings("serial")
    Map<String, Supplier<DecimalWraper>> wrapperNames = new HashMap<String, Supplier<DecimalWraper>>(){{
        put(combineKeys(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL), () -> totActualComm);
        put(combineKeys(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED), () -> totPlannedComm);
        put(combineKeys(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE), () -> totPipelineComm);
        put(combineKeys(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL), () -> totActualDisb);
        put(combineKeys(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED), () -> totPlanDisb);
        put(combineKeys(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE), () -> totPipelineDisb);
        put(combineKeys(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_OFFICIAL_DEV_AID), () -> totOfficialDevelopmentAidDisb );
        put(combineKeys(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC), () -> totBilateralSscDisb );
        put(combineKeys(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_TRIANGULAR_SSC), () -> totTriangularSscDisb );
        put(combineKeys(Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL), () -> totActualEDD);
        put(combineKeys(Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED), () -> totPlannedEDD);
        put(combineKeys(Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE), () -> totPipelineEDD);
        put(combineKeys(Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC), () -> totBilateralSccEDD);
        put(combineKeys(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL), () -> totActualExp);
        put(combineKeys(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_PLANNED), () -> totPlannedExp);
        put(combineKeys(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE), () -> totPipelineExp);
        put(combineKeys(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_OFFICIAL_DEV_AID), () -> totOfficialDevelopmentAidExp );
        put(combineKeys(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_TRIANGULAR_SSC), () -> totTriangularSccExp );
        put(combineKeys(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC), () -> totBilaterlaSscExp );
        put(combineKeys(Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL), () -> totActualReleaseOfFunds);
        put(combineKeys(Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_PLANNED), () -> totPlannedReleaseOfFunds);
        put(combineKeys(Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE), () -> totPipelineReleaseOfFunds);
        put(combineKeys(Constants.ARREARS, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL), () -> totActualArrears);
        put(combineKeys(Constants.ARREARS, CategoryConstants.ADJUSTMENT_TYPE_PLANNED), () -> totPlannedArrears);
        put(combineKeys(Constants.ARREARS, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE), () -> totPipelineArrears);
        put(combineKeys(Constants.ARREARS, CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC), () -> totBilateralSscArrears);
        put(combineKeys(Constants.ARREARS, CategoryConstants.ADJUSTMENT_TYPE_TRIANGULAR_SSC), () -> totTriangularSscArrears);
        put(combineKeys(Constants.ARREARS, CategoryConstants.ADJUSTMENT_TYPE_OFFICIAL_DEV_AID), () -> totOfficialDevAidArrears);
        put(combineKeys(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_ODA_SSC), () -> totOdaSscComm);
        put(combineKeys(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC), () -> totBilateralSscComm);
        put(combineKeys(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_OFFICIAL_DEV_AID), () -> totOfficialDevelopmentAidComm );
        put(combineKeys(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_TRIANGULAR_SSC), () -> totTriangularSscComm);
        put(combineKeys(Constants.DISBURSEMENT_ORDER, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL), () -> totActualDisbOrder);
        put(combineKeys(Constants.DISBURSEMENT_ORDER, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE), () -> totPipelineDisbOrder);
        put(combineKeys(Constants.DISBURSEMENT_ORDER, CategoryConstants.ADJUSTMENT_TYPE_PLANNED), () -> totPlannedDisbOrder);
        put(combineKeys(Constants.DISBURSEMENT_ORDER, CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC), () -> totBilateralSscDisbOrder);
        put(combineKeys(Constants.DISBURSEMENT_ORDER, CategoryConstants.ADJUSTMENT_TYPE_TRIANGULAR_SSC), () -> totTriangularSscDisbOrder);
    }};
    
    public DecimalWraper getTotalByKey(String adjKey, String transTypeKey) {
        Supplier<DecimalWraper> supplier = wrapperNames.get(String.format("%s %s", transTypeKey, adjKey));
        if(supplier != null) {
            return supplier.get();
        } else {
            logger.error("Unsupported transaction + adjustment combination: " +
                    String.format("transType: %s, adjType: %s", transTypeKey, adjKey));
        }
        return new DecimalWraper();
    }
    
    /**
     * DO NOT CALCULATE SSC STUFF HERE!
     */
    DecimalWraper totalCommitments = new DecimalWraper();
    DecimalWraper unDisbursementsBalance = new DecimalWraper();
    DecimalWraper totalMtef = new DecimalWraper();
    DecimalWraper totalMtefPipeline = new DecimalWraper();
    DecimalWraper totalMtefProjection = new DecimalWraper();

    DecimalWraper totalPledged = new DecimalWraper();
    
    boolean debug;
    
    /**
     * extracts all the donor funding + MTEF funding from a source and adds them into a single source; then calculates the totals <br />
     * also resets the internal {@link #getFundDetailList()}
     * @param fundingSource
     * @param userCurrencyCode
     */
    public void doCalculations(AmpFunding fundingSource, String userCurrencyCode) {
        ArrayList<AmpFundingDetail> fundingDetails = new ArrayList<AmpFundingDetail>();
        fundingDetails.addAll(fundingSource.getFundingDetails());
        Collections.sort(fundingDetails, FundingDetailComparator.getFundingDetailComparator());
        ArrayList<FundingInformationItem> funding = new ArrayList<FundingInformationItem>();
        if (fundingSource.getFundingDetails() != null) {
            funding.addAll(fundingDetails);
        }
        if (fundingSource.getMtefProjections() != null) {
            funding.addAll(fundingSource.getMtefProjections());
        }
        boolean updateTotals = fundingSource.isCountedInTotals();
        doCalculations(funding, userCurrencyCode, updateTotals);
    }
    
    /**
     * also resets the internal {@link #getFundDetailList()}
     * @param details
     * @param userCurrencyCode
     * @param updateTotals - if false, then only fundDetailList will be built, without updating the totals
     */
    public void doCalculations(Collection<? extends FundingInformationItem> details, String userCurrencyCode, boolean updateTotals) {
        fundDetailList = new ArrayList<FundingDetail>();
        int indexId = 0;
        AmpCategoryValue actualAdjustmentType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB();
        if (actualAdjustmentType == null) {
            throw new RuntimeException("ACTUAL adjustment type not found in the database");
        }
        String decimalSeparatorStr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);
        char decimalSeparatorChar = decimalSeparatorStr == null ? '.' : (decimalSeparatorStr.isEmpty() ? '.' : decimalSeparatorStr.charAt(0));
        for (FundingInformationItem fundDet:details)
        {
            AmpCategoryValue adjType = actualAdjustmentType;
            if (fundDet.getAdjustmentType() != null)
                adjType = fundDet.getAdjustmentType();
            FundingDetail fundingDetail = new FundingDetail();
            fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());
            fundingDetail.setFundDetId(fundDet.getDbId());
            
            if (fundDet.getFixedExchangeRate() != null && fundDet.getFixedExchangeRate().doubleValue() != 1) {
                // We cannot use FormatHelper.formatNumber as this might roundup our number (and this would be very wrong)
                fundingDetail.setFixedExchangeRate((fundDet.getFixedExchangeRate().toString()).replace('.', decimalSeparatorChar));
                fundingDetail.setUseFixedRate(true);
            }
            fundingDetail.setIndexId(indexId++);
            fundingDetail.setAdjustmentTypeName(fundDet.getAdjustmentType());
            fundingDetail.setExpenditureClass(fundDet.getExpenditureClass());
            if(fundDet.getTransactionType().equals(Constants.MTEFPROJECTION)){
                fundingDetail.setProjectionTypeName(((AmpFundingMTEFProjection)fundDet).getProjected());    
            }
            
            fundingDetail.setContract(fundDet.getContract());
            java.sql.Date dt = new java.sql.Date(fundDet.getTransactionDate().getTime());
            
            Double fixedExchangeRate = fundDet.getFixedExchangeRate();
            DecimalWraper amt = new DecimalWraper();
            amt.setValue(BigDecimal.valueOf(fundDet.getTransactionAmount()).multiply(BigDecimal.valueOf
                    (AmpCurrencyConvertor.getInstance().getExchangeRate(fundDet.getAmpCurrencyId().getCurrencyCode(),
                            userCurrencyCode, fixedExchangeRate, dt.toLocalDate()))));
            if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
                fundingDetail.setClassification(fundDet.getExpCategory());
            }
            fundingDetail.setCurrencyCode(userCurrencyCode);
            AmpCurrency curr = CurrencyUtil.getAmpcurrency(userCurrencyCode);
            if(curr != null) {
                fundingDetail.setCurrencyName(curr.getCountryName());
            }
            fundingDetail.setTransactionAmount(CurrencyWorker.convert(FeaturesUtil.applyThousandsForVisibility(amt
                    .doubleValue()).doubleValue(), 1, 1));
            fundingDetail.setTransactionDate(DateConversion.convertDateToLocalizedString(fundDet.getTransactionDate()));
            fundingDetail.setFiscalYear(DateConversion.convertDateToFiscalYearString(fundDet.getTransactionDate()));
            fundingDetail.setCapitalPercent(fundDet.getCapitalSpendingPercentage());
            fundingDetail.setReportingDate(fundDet.getReportingDate());
            fundingDetail.setRecipientOrganisation(fundDet.getRecipientOrg());
            fundingDetail.setRecipientOrganisationRole(fundDet.getRecipientRole());
            fundingDetail.setTransactionType(fundDet.getTransactionType().intValue());
            fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());
            if (fundDet.getPledgeid() != null) {
                fundingDetail.setPledge(fundDet.getPledgeid().getId());
                fundingDetail.setAttachedPledgeName(fundDet.getPledgeid().getEffectiveName());
            }
            // TOTALS
            if (updateTotals) addToTotals(adjType, fundDet, amt);
            fundDetailList.add(fundingDetail);
            fundingDetail.setDisasterResponse(fundDet.getDisasterResponse());
        }
        totalCommitments = Logic.getInstance().getTotalDonorFundingCalculator().getTotalCommtiments(totPlannedComm, totActualComm, totPipelineComm);
        unDisbursementsBalance = Logic.getInstance().getTotalDonorFundingCalculator().getunDisbursementsBalance(totalCommitments, totActualDisb);
        
    }
    
    protected void addToTotals(AmpCategoryValue adjType, FundingInformationItem fundDet, DecimalWraper amt) {
        /**
         * no adjustment type for MTEF transactions or PLEDGED amounts, so this "if" is outside the PLANNED / ACTUAL / PIPELINE branching if's
         */
        if (fundDet.getTransactionType() == Constants.MTEFPROJECTION) {
            totalMtef.add(amt);
            String projectionType = ((AmpFundingMTEFProjection)fundDet).getProjected().getValue();
            if ("pipeline".equals(projectionType)) {
                totalMtefPipeline.add(amt);
            } else if ("projection".equals(projectionType)) {
                totalMtefProjection.add(amt);
            }
            return;
        }
        if (fundDet.getTransactionType() == Constants.PLEDGE){
            totalPledged.add(amt);
        } else {
            DecimalWraper wrp = getTotalByKey(adjType.getLabel(), getTransactionTypeLabel(fundDet.getTransactionType()));
            if (wrp != null) {
                wrp.add(amt);
            }
        }
    }
    

    public FundingCalculationsHelper() {
    }
    

    public List<FundingDetail> getFundDetailList() {
        return this.fundDetailList;
    }
    

    public DecimalWraper getTotPlanDisb() {
        return this.totPlanDisb;
    }
    

    public DecimalWraper getTotPlannedComm() {
        return this.totPlannedComm;
    }
    

    public DecimalWraper getTotPlannedExp() {
        return this.totPlannedExp;
    }
    

    public DecimalWraper getTotPlannedDisbOrder() {
        return this.totPlannedDisbOrder;
    }
    

    public DecimalWraper getTotPlannedReleaseOfFunds() {
        return this.totPlannedReleaseOfFunds;
    }
    

    public DecimalWraper getTotPlannedEDD() {
        return this.totPlannedEDD;
    }
    

    public DecimalWraper getTotActualComm() {
        return this.totActualComm;
    }
    

    public DecimalWraper getTotActualDisb() {
        return this.totActualDisb;
    }
    

    public DecimalWraper getTotActualExp() {
        return this.totActualExp;
    }
    

    public DecimalWraper getTotActualDisbOrder() {
        return this.totActualDisbOrder;
    }
    

    public DecimalWraper getTotActualReleaseOfFunds() {
        return this.totActualReleaseOfFunds;
    }
    

    public DecimalWraper getTotActualEDD() {
        return this.totActualEDD;
    }
    

    public DecimalWraper getTotPipelineDisb() {
        return this.totPipelineDisb;
    }
    

    public DecimalWraper getTotPipelineComm() {
        return this.totPipelineComm;
    }
    

    public DecimalWraper getTotPipelineExp() {
        return this.totPipelineExp;
    }
    

    public DecimalWraper getTotPipelineDisbOrder() {
        return this.totPipelineDisbOrder;
    }
    

    public DecimalWraper getTotPipelineReleaseOfFunds() {
        return this.totPipelineReleaseOfFunds;
    }
    

    public DecimalWraper getTotPipelineEDD() {
        return this.totPipelineEDD;
    }
    

    public DecimalWraper getTotOdaSscComm() {
        return this.totOdaSscComm;
    }
    

    public DecimalWraper getTotBilateralSscComm() {
        return this.totBilateralSscComm;
    }
    

    public DecimalWraper getTotTriangularSscComm() {
        return this.totTriangularSscComm;
    }
    
    /**
     * DO NOT CALCULATE SSC STUFF HERE!
     */

    public DecimalWraper getTotalCommitments() {
        return this.totalCommitments;
    }
    

    public DecimalWraper getUnDisbursementsBalance() {
        return this.unDisbursementsBalance;
    }
    

    public DecimalWraper getTotalMtef() {
        return this.totalMtef;
    }

    public DecimalWraper getTotalMtefPipeline() {
        return this.totalMtefPipeline;
    }

    public DecimalWraper getTotalMtefProjection() {
        return this.totalMtefProjection;
    }


    public DecimalWraper getTotalPledged(){
        return this.totalPledged;
    }

    public DecimalWraper getTotBilateralSscArrears() {
        return totBilateralSscArrears;
    }

    public DecimalWraper getTotTriangularSscArrears() {
        return totTriangularSscArrears;
    }

    public DecimalWraper getTotBilateralSscDisbOrder() {
        return totBilateralSscDisbOrder;
    }

    public DecimalWraper getTotTriangularSscDisbOrder() {
        return totTriangularSscDisbOrder;
    }

    public DecimalWraper getTotOfficialDevAidArrears() {
        return totOfficialDevAidArrears;
    }

    public DecimalWraper getTotBilaterlaSscExp() {
        return totBilaterlaSscExp;
    }

    public DecimalWraper getTotTriangularSscExp() {
        return totTriangularSscExp;
    }

    public DecimalWraper getTotOfficialDevelopmentAidDisb() {
        return totOfficialDevelopmentAidDisb;
    }

    public DecimalWraper getTotBilateralSscDisb() {
        return totBilateralSscDisb;
    }

    public DecimalWraper getTotTriangularSscDisb() {
        return totTriangularSscDisb;
    }

    public DecimalWraper getTotOfficialDevelopmentAidExp() {
        return totOfficialDevelopmentAidExp;
    }

    public DecimalWraper getTotTriangularSccExp() {
        return totTriangularSccExp;
    }

    public DecimalWraper getTotBilateralSScExp() {
        return totBilateralSScExp;
    }

    public DecimalWraper getTotOfficialDevelopmentAidComm() {
        return totOfficialDevelopmentAidComm;
    }

    public DecimalWraper getTotBilateralSccEDD() {
        return totBilateralSccEDD;
    }

    public boolean isDebug() {
        return this.debug;
    }
    

    public void setFundDetailList(final List<FundingDetail> fundDetailList) {
        this.fundDetailList = fundDetailList;
    }
    

    public void setTotPlanDisb(final DecimalWraper totPlanDisb) {
        this.totPlanDisb = totPlanDisb;
    }
    

    public void setTotPlannedComm(final DecimalWraper totPlannedComm) {
        this.totPlannedComm = totPlannedComm;
    }
    

    public void setTotPlannedExp(final DecimalWraper totPlannedExp) {
        this.totPlannedExp = totPlannedExp;
    }
    

    public void setTotPlannedDisbOrder(final DecimalWraper totPlannedDisbOrder) {
        this.totPlannedDisbOrder = totPlannedDisbOrder;
    }
    

    public void setTotPlannedReleaseOfFunds(final DecimalWraper totPlannedReleaseOfFunds) {
        this.totPlannedReleaseOfFunds = totPlannedReleaseOfFunds;
    }
    

    public void setTotPlannedEDD(final DecimalWraper totPlannedEDD) {
        this.totPlannedEDD = totPlannedEDD;
    }
    

    public void setTotActualComm(final DecimalWraper totActualComm) {
        this.totActualComm = totActualComm;
    }
    

    public void setTotActualDisb(final DecimalWraper totActualDisb) {
        this.totActualDisb = totActualDisb;
    }
    

    public void setTotActualExp(final DecimalWraper totActualExp) {
        this.totActualExp = totActualExp;
    }
    

    public void setTotActualDisbOrder(final DecimalWraper totActualDisbOrder) {
        this.totActualDisbOrder = totActualDisbOrder;
    }
    

    public void setTotActualReleaseOfFunds(final DecimalWraper totActualReleaseOfFunds) {
        this.totActualReleaseOfFunds = totActualReleaseOfFunds;
    }
    

    public void setTotActualEDD(final DecimalWraper totActualEDD) {
        this.totActualEDD = totActualEDD;
    }
    

    public void setTotPipelineDisb(final DecimalWraper totPipelineDisb) {
        this.totPipelineDisb = totPipelineDisb;
    }
    

    public void setTotPipelineComm(final DecimalWraper totPipelineComm) {
        this.totPipelineComm = totPipelineComm;
    }
    

    public void setTotPipelineExp(final DecimalWraper totPipelineExp) {
        this.totPipelineExp = totPipelineExp;
    }
    

    public void setTotPipelineDisbOrder(final DecimalWraper totPipelineDisbOrder) {
        this.totPipelineDisbOrder = totPipelineDisbOrder;
    }
    

    public void setTotPipelineReleaseOfFunds(final DecimalWraper totPipelineReleaseOfFunds) {
        this.totPipelineReleaseOfFunds = totPipelineReleaseOfFunds;
    }
    

    public void setTotPipelineEDD(final DecimalWraper totPipelineEDD) {
        this.totPipelineEDD = totPipelineEDD;
    }
    

    public void setTotOdaSscComm(final DecimalWraper totOdaSscComm) {
        this.totOdaSscComm = totOdaSscComm;
    }
    

    public void setTotBilateralSscComm(final DecimalWraper totBilateralSscComm) {
        this.totBilateralSscComm = totBilateralSscComm;
    }
    

    public void setTotTriangularSscComm(final DecimalWraper totTriangularSscComm) {
        this.totTriangularSscComm = totTriangularSscComm;
    }

    public DecimalWraper getTotActualArrears() {
        return totActualArrears;
    }

    public void setTotActualArrears(DecimalWraper totActualArrears) {
        this.totActualArrears = totActualArrears;
    }   
    
    /**
     * DO NOT CALCULATE SSC STUFF HERE!
     */

    public void setTotalCommitments(final DecimalWraper totalCommitments) {
        this.totalCommitments = totalCommitments;
    }
    

    public void setUnDisbursementsBalance(final DecimalWraper unDisbursementsBalance) {
        this.unDisbursementsBalance = unDisbursementsBalance;
    }
    

    public void setTotalMtef(final DecimalWraper totalMtef) {
        this.totalMtef = totalMtef;
    }
    

    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

    public DecimalWraper getTotPlannedArrears() {
        return totPlannedArrears;
    }

    public void setTotPlannedArrears(DecimalWraper totPlannedArrears) {
        this.totPlannedArrears = totPlannedArrears;
    }
    
    public DecimalWraper getTotPipelineArrears() {
        return totPipelineArrears;
    }

    public void setTotPipelineArrears(DecimalWraper totPipelineArrears) {
        this.totPipelineArrears = totPipelineArrears;
    }

    public void setTotBilateralSscDisbOrder(DecimalWraper totBilateralSscDisbOrder) {
        this.totBilateralSscDisbOrder = totBilateralSscDisbOrder;
    }

    public void setTotTriangularSscDisbOrder(DecimalWraper totTriangularSscDisbOrder) {
        this.totTriangularSscDisbOrder = totTriangularSscDisbOrder;
    }

    public void setTotOfficialDevAidArrears(DecimalWraper totOfficialDevAidArrears) {
        this.totOfficialDevAidArrears = totOfficialDevAidArrears;
    }

    public boolean canEqual(final java.lang.Object other) {
        return other instanceof FundingCalculationsHelper;
    }

    public void setTotBilateralSscArrears(DecimalWraper totBilateralSscArrears) {
        this.totBilateralSscArrears = totBilateralSscArrears;
    }

    public void setTotTriangularSscArrears(DecimalWraper totTriangularSscArrears) {
        this.totTriangularSscArrears = totTriangularSscArrears;
    }


}
