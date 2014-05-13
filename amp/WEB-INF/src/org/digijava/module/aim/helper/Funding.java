// Generated by delombok at Mon Mar 24 00:10:06 EET 2014
package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.FundingInformationItem;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author jose
 *
 * funding digest for an AmpFunding instance
 */
public class Funding implements Serializable {
	private static Logger logger = Logger.getLogger(Funding.class);
	private long fundingId;
	//private AmpTermsAssist ampTermsAssist;
	private AmpCategoryValue typeOfAssistance;
	private AmpCategoryValue financingInstrument;
	private AmpCategoryValue fundingStatus;
	private AmpCategoryValue modeOfPayment;
	private String orgFundingId;
	private String sourceRole;
	private String signatureDate;
	//private AmpModality modality;
	private List<FundingDetail> fundingDetails; // Collection of Funding Details
	
//	private Collection<MTEFProjection> mtefProjections;
	private String currentFunding;
	private String propStartDate;
	private String propCloseDate;
	private String actStartDate;
	private String actCloseDate;
	private String reportingDate;
	private String conditions;
	private String donorObjective;
	private List<FundingInformationItem> ampRawFunding;
	private String subtotalActualCommitments;
	private String subtotalPlannedCommitments;
	private String subtotalPipelineCommitments;
	private String subtotalOfficialDevelopmentAidCommitments;
	private String subtotalBilateralSscCommitments;
	private String subtotalTriangularSscCommitments;
	private String subtotalPlannedDisbursements;
	private String subtotalPipelineDisbursements;
	private String subtotalDisbursements;
	private String subtotalPlannedExpenditures;
	private String subtotalPipelineExpenditures;
	private String subtotalExpenditures;
	private String subtotalActualDisbursementsOrders;
	private String subtotalPlannedDisbursementsOrders;
	private String subtotalPipelineDisbursementsOrders;
	private String subtotalActualRoF;
	private String subtotalPlannedRoF;
	private String subtotalPipelineRoF;
	private String subtotalRoF;
	private String subtotalActualEDD;
	private String subtotalPlannedEDD;
	private String subtotalPipelineEDD;
	private String subtotalEDD;
	private String subtotalMTEFs;
	private String undisbursementbalance;
	
	/**
	 * misnomer: agreementTitle. To be renamed in the future!
	 */
	private String title;
	
	/**
	 * misnomer: agreementCode. To be renamed in the future!
	 */
	private String code;
	private String fundingClassificationDate;
	private Long groupVersionedFunding;
	private Float capitalSpendingPercentage;	
	
	public boolean equals(Object e) {
		if (e instanceof Funding) {
			Funding tmp = (Funding)e;
			return fundingId == tmp.fundingId;
		}
		throw new ClassCastException("cannot compare a " + this.getClass().getName() + " instance with a " + e.getClass().getName() + " instance");
	}
	
	public void populateAmpRawFunding(AmpFunding fundingSource) {
		ArrayList<FundingInformationItem> funding = new ArrayList<FundingInformationItem>();
		if (fundingSource.getFundingDetails() != null) funding.addAll(fundingSource.getFundingDetails());
		if (fundingSource.getMtefProjections() != null) funding.addAll(fundingSource.getMtefProjections());
		this.ampRawFunding = funding;
	}
	
	public void cleanAmpRawFunding() {
		this.ampRawFunding = null;
	}
	
	public Collection<FundingDetail> filterFundings(int transactionType, String adjustmentType) {
		if (fundingDetails != null) {
			List<FundingDetail> retDetails = new ArrayList<FundingDetail>();
			for (FundingDetail detail : fundingDetails) {
				if (detail.getTransactionType() == transactionType && detail.getAdjustmentTypeName().getValue().equals(adjustmentType)) retDetails.add(detail);
			}
			return retDetails;
		}
		return null;
	}
	
	protected Collection<FundingDetail> filterFundings(int transactionType) {
		if (fundingDetails != null) {
			List<FundingDetail> retDetails = new ArrayList<FundingDetail>();
			for (FundingDetail detail : fundingDetails) {
				if (detail.getTransactionType() == transactionType) retDetails.add(detail);
			}
			return retDetails;
		}
		return null;
	}
	
	public Collection<FundingDetail> getMtefDetails() {
		return filterFundings(Constants.MTEFPROJECTION);
	}
	
	public Collection<FundingDetail> getPlannedCommitmentsDetails() {
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey());
	}
	
	public Collection<FundingDetail> getActualCommitmentsDetails() {
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	}
	
	public Collection<FundingDetail> getPipelineCommitmentsDetails() {
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey());
	}
	
	public Collection<FundingDetail> getOfficialDevelopmentAidCommitmentsDetails() {
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_ODA_SSC.getValueKey());
	}
	
	public Collection<FundingDetail> getBilateralSscCommitmentsDetails() {
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC.getValueKey());
	}
	
	public Collection<FundingDetail> getTriangularSscCommitmentsDetails() {
		return filterFundings(Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_TRIANGULAR_SSC.getValueKey());
	}
	
	public Collection<FundingDetail> getPlannedDisbursementDetails() {
		return filterFundings(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey());
	}
	
	public Collection<FundingDetail> getActualDisbursementDetails() {
		return filterFundings(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	}
	
	public Collection<FundingDetail> getPipelineDisbursementDetails() {
		return filterFundings(Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey());
	}
	
	public Collection<FundingDetail> getPlannedExpendituresDetails() {
		return filterFundings(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey());
	}
	
	public Collection<FundingDetail> getActualExpendituresDetails() {
		return filterFundings(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	}
	
	public Collection<FundingDetail> getPipelineExpendituresDetails() {
		return filterFundings(Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey());
	}
	
	public Collection<FundingDetail> getPlannedRoFDetails() {
		return filterFundings(Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey());
	}
	
	public Collection<FundingDetail> getActualRoFDetails() {
		return filterFundings(Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	}
	
	public Collection<FundingDetail> getPipelineRoFDetails() {
		return filterFundings(Constants.RELEASE_OF_FUNDS, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey());
	}
	
	public Collection<FundingDetail> getPlannedEDDDetails() {
		return filterFundings(Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey());
	}
	
	public Collection<FundingDetail> getActualEDDDetails() {
		return filterFundings(Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	}
	
	public Collection<FundingDetail> getPipelineEDDDetails() {
		return filterFundings(Constants.ESTIMATED_DONOR_DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey());
	}
	
	public Collection<FundingDetail> getCommitmentsDetails() {
		return filterFundings(Constants.COMMITMENT);
	}
	
	public Collection<FundingDetail> getDisbursementsDetails() {
		return filterFundings(Constants.DISBURSEMENT);
	}
	
	public Collection<FundingDetail> getDisbursementOrdersDetails() {
		return filterFundings(Constants.DISBURSEMENT_ORDER);
	}
	
	public Collection<FundingDetail> getExpendituresDetails() {
		return filterFundings(Constants.EXPENDITURE);
	}
	
	public Collection<FundingDetail> getRoFDetails() {
		return filterFundings(Constants.RELEASE_OF_FUNDS);
	}
	
	public Collection<FundingDetail> getEDDDetails() {
		return filterFundings(Constants.ESTIMATED_DONOR_DISBURSEMENT);
	}
	
	/**
	 * returns a funding item built and with all its' currency Codes overwritten to a single one
	 * WARNING, BUG! CurrencyName is not overwritten
	 * WARNING 2, BUG 2 - MTEF projections do not have their currency updated anyway - only the totals are
	 * @param ampFunding
	 * @param activityTotalCalculations
	 * @param toCurrCode
	 * @param isPreview
	 * @param tm
	 * @return
	 */
	public Funding(AmpFunding ampFunding, FundingCalculationsHelper activityTotalCalculations, String toCurrCode, boolean changeToWorkspaceCurrency, TeamMember tm) {
		//Funding funding = new Funding();
		//fund.setAmpTermsAssist(ampFunding.getAmpTermsAssistId());
		this.setTypeOfAssistance(ampFunding.getTypeOfAssistance());
		this.setFinancingInstrument(ampFunding.getFinancingInstrument());
		this.setFundingStatus(ampFunding.getFundingStatus());
		this.setModeOfPayment(ampFunding.getModeOfPayment());
		this.setActStartDate(DateConversion.ConvertDateToString(ampFunding.getActualStartDate()));
		this.setActCloseDate(DateConversion.ConvertDateToString(ampFunding.getActualCompletionDate()));
		this.setFundingId(ampFunding.getAmpFundingId().longValue());
		this.setGroupVersionedFunding(ampFunding.getGroupVersionedFunding());
		this.setOrgFundingId(ampFunding.getFinancingId());
		if (ampFunding.getSourceRole() != null) this.setSourceRole(ampFunding.getSourceRole().getName());
		this.setConditions(ampFunding.getConditions());
		this.setDonorObjective(ampFunding.getDonorObjective());
		this.setCapitalSpendingPercentage(ampFunding.getCapitalSpendingPercentage());
		this.setFundingClassificationDate(DateConversion.ConvertDateToString(ampFunding.getFundingClassificationDate()));
		if (ampFunding.getAgreement() != null) {
			this.setTitle(ampFunding.getAgreement().getTitle());
			this.setCode(ampFunding.getAgreement().getCode());
		} else {
			this.setCode("");
			this.setTitle("");
		}
		//Collection<AmpFundingDetail> fundDetails = ampFunding.getFundingDetails();
		String currencyCode;
		if (tm != null) {
			currencyCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
		} else {
			currencyCode = Constants.DEFAULT_CURRENCY;
		}
		if (true) // we might also have MTEFs, so no reason to do the "if". Plus, anyway, this will be a NOP if there are no fundings inside
		 {
			//  Iterator fundDetItr = fundDetails.iterator();
			// long indexId = System.currentTimeMillis();
			activityTotalCalculations.doCalculations(ampFunding, toCurrCode);
			List<FundingDetail> fundDetail = activityTotalCalculations.getFundDetailList();
			if (changeToWorkspaceCurrency) {
				Iterator<FundingDetail> fundingIterator = fundDetail.iterator();
				while (fundingIterator.hasNext()) {
					FundingDetail currentFundingDetail = fundingIterator.next();
					currentFundingDetail.getContract();
					Double currencyAppliedAmount;
					if (currentFundingDetail.getFixedExchangeRate() == null) {
						currencyAppliedAmount = getAmountInCurrency(currentFundingDetail, currencyCode);
					} else {
						Double fixedExchangeRate = FormatHelper.parseDouble(currentFundingDetail.getFixedExchangeRate());
						currencyAppliedAmount = CurrencyWorker.convert1(FormatHelper.parseDouble(currentFundingDetail.getTransactionAmount()), fixedExchangeRate, 1);
					}
					String currentAmount = FormatHelper.formatNumber(currencyAppliedAmount);
					currentFundingDetail.setTransactionAmount(currentAmount);
					currentFundingDetail.setCurrencyCode(currencyCode);
				}
			}
			if (fundDetail != null) Collections.sort(fundDetail, FundingValidator.dateComp);
			this.setFundingDetails(fundDetail);
			this.populateAmpRawFunding(ampFunding);
			// funding.add(fund);
		}
	}
	
	private double getAmountInCurrency(FundingDetail fundDet, String toCurrCode) {
		java.sql.Date dt = new java.sql.Date(DateConversion.getDate(fundDet.getTransactionDate()).getTime());
		double frmExRt = Util.getExchange(fundDet.getCurrencyCode(), dt);
		// String toCurrCode = CurrencyUtil.getAmpcurrency( appSet.getCurrencyId() ).getCurrencyCode();
		double toExRt = Util.getExchange(toCurrCode, dt);
		double amt = CurrencyWorker.convert1(FormatHelper.parseDouble(fundDet.getTransactionAmount()), frmExRt, toExRt);
		return amt;
	}
	
	@java.lang.SuppressWarnings("all")
	public long getFundingId() {
		return this.fundingId;
	}
	
	@java.lang.SuppressWarnings("all")
	public AmpCategoryValue getTypeOfAssistance() {
		return this.typeOfAssistance;
	}
	
	@java.lang.SuppressWarnings("all")
	public AmpCategoryValue getFinancingInstrument() {
		return this.financingInstrument;
	}
	
	@java.lang.SuppressWarnings("all")
	public AmpCategoryValue getFundingStatus() {
		return this.fundingStatus;
	}
	
	@java.lang.SuppressWarnings("all")
	public AmpCategoryValue getModeOfPayment() {
		return this.modeOfPayment;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getOrgFundingId() {
		return this.orgFundingId;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSourceRole() {
		return this.sourceRole;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSignatureDate() {
		return this.signatureDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public List<FundingDetail> getFundingDetails() {
		return this.fundingDetails;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getCurrentFunding() {
		return this.currentFunding;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getPropStartDate() {
		return this.propStartDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getPropCloseDate() {
		return this.propCloseDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getActStartDate() {
		return this.actStartDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getActCloseDate() {
		return this.actCloseDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getReportingDate() {
		return this.reportingDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getConditions() {
		return this.conditions;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getDonorObjective() {
		return this.donorObjective;
	}
	
	@java.lang.SuppressWarnings("all")
	public List<FundingInformationItem> getAmpRawFunding() {
		return this.ampRawFunding;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalActualCommitments() {
		return this.subtotalActualCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPlannedCommitments() {
		return this.subtotalPlannedCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPipelineCommitments() {
		return this.subtotalPipelineCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalOfficialDevelopmentAidCommitments() {
		return this.subtotalOfficialDevelopmentAidCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalBilateralSscCommitments() {
		return this.subtotalBilateralSscCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalTriangularSscCommitments() {
		return this.subtotalTriangularSscCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPlannedDisbursements() {
		return this.subtotalPlannedDisbursements;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPipelineDisbursements() {
		return this.subtotalPipelineDisbursements;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalDisbursements() {
		return this.subtotalDisbursements;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPlannedExpenditures() {
		return this.subtotalPlannedExpenditures;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPipelineExpenditures() {
		return this.subtotalPipelineExpenditures;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalExpenditures() {
		return this.subtotalExpenditures;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalActualDisbursementsOrders() {
		return this.subtotalActualDisbursementsOrders;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPlannedDisbursementsOrders() {
		return this.subtotalPlannedDisbursementsOrders;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPipelineDisbursementsOrders() {
		return this.subtotalPipelineDisbursementsOrders;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalActualRoF() {
		return this.subtotalActualRoF;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPlannedRoF() {
		return this.subtotalPlannedRoF;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPipelineRoF() {
		return this.subtotalPipelineRoF;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalRoF() {
		return this.subtotalRoF;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalActualEDD() {
		return this.subtotalActualEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPlannedEDD() {
		return this.subtotalPlannedEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalPipelineEDD() {
		return this.subtotalPipelineEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalEDD() {
		return this.subtotalEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getSubtotalMTEFs() {
		return this.subtotalMTEFs;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getUndisbursementbalance() {
		return this.undisbursementbalance;
	}
	
	/**
	 * misnomer: agreementTitle. To be renamed in the future!
	 */
	@java.lang.SuppressWarnings("all")
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * misnomer: agreementCode. To be renamed in the future!
	 */
	@java.lang.SuppressWarnings("all")
	public String getCode() {
		return this.code;
	}
	
	@java.lang.SuppressWarnings("all")
	public String getFundingClassificationDate() {
		return this.fundingClassificationDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public Long getGroupVersionedFunding() {
		return this.groupVersionedFunding;
	}
	
	@java.lang.SuppressWarnings("all")
	public Float getCapitalSpendingPercentage() {
		return this.capitalSpendingPercentage;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setFundingId(final long fundingId) {
		this.fundingId = fundingId;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTypeOfAssistance(final AmpCategoryValue typeOfAssistance) {
		this.typeOfAssistance = typeOfAssistance;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setFinancingInstrument(final AmpCategoryValue financingInstrument) {
		this.financingInstrument = financingInstrument;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setFundingStatus(final AmpCategoryValue fundingStatus) {
		this.fundingStatus = fundingStatus;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setModeOfPayment(final AmpCategoryValue modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setOrgFundingId(final String orgFundingId) {
		this.orgFundingId = orgFundingId;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSourceRole(final String sourceRole) {
		this.sourceRole = sourceRole;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSignatureDate(final String signatureDate) {
		this.signatureDate = signatureDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setFundingDetails(final List<FundingDetail> fundingDetails) {
		this.fundingDetails = fundingDetails;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setCurrentFunding(final String currentFunding) {
		this.currentFunding = currentFunding;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setPropStartDate(final String propStartDate) {
		this.propStartDate = propStartDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setPropCloseDate(final String propCloseDate) {
		this.propCloseDate = propCloseDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setActStartDate(final String actStartDate) {
		this.actStartDate = actStartDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setActCloseDate(final String actCloseDate) {
		this.actCloseDate = actCloseDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setReportingDate(final String reportingDate) {
		this.reportingDate = reportingDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setConditions(final String conditions) {
		this.conditions = conditions;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setDonorObjective(final String donorObjective) {
		this.donorObjective = donorObjective;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setAmpRawFunding(final List<FundingInformationItem> ampRawFunding) {
		this.ampRawFunding = ampRawFunding;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalActualCommitments(final String subtotalActualCommitments) {
		this.subtotalActualCommitments = subtotalActualCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPlannedCommitments(final String subtotalPlannedCommitments) {
		this.subtotalPlannedCommitments = subtotalPlannedCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPipelineCommitments(final String subtotalPipelineCommitments) {
		this.subtotalPipelineCommitments = subtotalPipelineCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalOfficialDevelopmentAidCommitments(final String subtotalOfficialDevelopmentAidCommitments) {
		this.subtotalOfficialDevelopmentAidCommitments = subtotalOfficialDevelopmentAidCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalBilateralSscCommitments(final String subtotalBilateralSscCommitments) {
		this.subtotalBilateralSscCommitments = subtotalBilateralSscCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalTriangularSscCommitments(final String subtotalTriangularSscCommitments) {
		this.subtotalTriangularSscCommitments = subtotalTriangularSscCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPlannedDisbursements(final String subtotalPlannedDisbursements) {
		this.subtotalPlannedDisbursements = subtotalPlannedDisbursements;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPipelineDisbursements(final String subtotalPipelineDisbursements) {
		this.subtotalPipelineDisbursements = subtotalPipelineDisbursements;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalDisbursements(final String subtotalDisbursements) {
		this.subtotalDisbursements = subtotalDisbursements;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPlannedExpenditures(final String subtotalPlannedExpenditures) {
		this.subtotalPlannedExpenditures = subtotalPlannedExpenditures;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPipelineExpenditures(final String subtotalPipelineExpenditures) {
		this.subtotalPipelineExpenditures = subtotalPipelineExpenditures;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalExpenditures(final String subtotalExpenditures) {
		this.subtotalExpenditures = subtotalExpenditures;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalActualDisbursementsOrders(final String subtotalActualDisbursementsOrders) {
		this.subtotalActualDisbursementsOrders = subtotalActualDisbursementsOrders;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPlannedDisbursementsOrders(final String subtotalPlannedDisbursementsOrders) {
		this.subtotalPlannedDisbursementsOrders = subtotalPlannedDisbursementsOrders;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPipelineDisbursementsOrders(final String subtotalPipelineDisbursementsOrders) {
		this.subtotalPipelineDisbursementsOrders = subtotalPipelineDisbursementsOrders;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalActualRoF(final String subtotalActualRoF) {
		this.subtotalActualRoF = subtotalActualRoF;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPlannedRoF(final String subtotalPlannedRoF) {
		this.subtotalPlannedRoF = subtotalPlannedRoF;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPipelineRoF(final String subtotalPipelineRoF) {
		this.subtotalPipelineRoF = subtotalPipelineRoF;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalRoF(final String subtotalRoF) {
		this.subtotalRoF = subtotalRoF;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalActualEDD(final String subtotalActualEDD) {
		this.subtotalActualEDD = subtotalActualEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPlannedEDD(final String subtotalPlannedEDD) {
		this.subtotalPlannedEDD = subtotalPlannedEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalPipelineEDD(final String subtotalPipelineEDD) {
		this.subtotalPipelineEDD = subtotalPipelineEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalEDD(final String subtotalEDD) {
		this.subtotalEDD = subtotalEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setSubtotalMTEFs(final String subtotalMTEFs) {
		this.subtotalMTEFs = subtotalMTEFs;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setUndisbursementbalance(final String undisbursementbalance) {
		this.undisbursementbalance = undisbursementbalance;
	}
	
	/**
	 * misnomer: agreementTitle. To be renamed in the future!
	 */
	@java.lang.SuppressWarnings("all")
	public void setTitle(final String title) {
		this.title = title;
	}
	
	/**
	 * misnomer: agreementCode. To be renamed in the future!
	 */
	@java.lang.SuppressWarnings("all")
	public void setCode(final String code) {
		this.code = code;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setFundingClassificationDate(final String fundingClassificationDate) {
		this.fundingClassificationDate = fundingClassificationDate;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setGroupVersionedFunding(final Long groupVersionedFunding) {
		this.groupVersionedFunding = groupVersionedFunding;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setCapitalSpendingPercentage(final Float capitalSpendingPercentage) {
		this.capitalSpendingPercentage = capitalSpendingPercentage;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "Funding(fundingId=" + this.getFundingId() + ", typeOfAssistance=" + this.getTypeOfAssistance() + ", financingInstrument=" + this.getFinancingInstrument() + ", fundingStatus=" + this.getFundingStatus() + ", modeOfPayment=" + this.getModeOfPayment() + ", orgFundingId=" + this.getOrgFundingId() + ", sourceRole=" + this.getSourceRole() + ", signatureDate=" + this.getSignatureDate() + ", fundingDetails=" + this.getFundingDetails() + ", currentFunding=" + this.getCurrentFunding() + ", propStartDate=" + this.getPropStartDate() + ", propCloseDate=" + this.getPropCloseDate() + ", actStartDate=" + this.getActStartDate() + ", actCloseDate=" + this.getActCloseDate() + ", reportingDate=" + this.getReportingDate() + ", conditions=" + this.getConditions() + ", donorObjective=" + this.getDonorObjective() + ", ampRawFunding=" + this.getAmpRawFunding() + ", subtotalActualCommitments=" + this.getSubtotalActualCommitments() + ", subtotalPlannedCommitments=" + this.getSubtotalPlannedCommitments() + ", subtotalPipelineCommitments=" + this.getSubtotalPipelineCommitments() + ", subtotalOfficialDevelopmentAidCommitments=" + this.getSubtotalOfficialDevelopmentAidCommitments() + ", subtotalBilateralSscCommitments=" + this.getSubtotalBilateralSscCommitments() + ", subtotalTriangularSscCommitments=" + this.getSubtotalTriangularSscCommitments() + ", subtotalPlannedDisbursements=" + this.getSubtotalPlannedDisbursements() + ", subtotalPipelineDisbursements=" + this.getSubtotalPipelineDisbursements() + ", subtotalDisbursements=" + this.getSubtotalDisbursements() + ", subtotalPlannedExpenditures=" + this.getSubtotalPlannedExpenditures() + ", subtotalPipelineExpenditures=" + this.getSubtotalPipelineExpenditures() + ", subtotalExpenditures=" + this.getSubtotalExpenditures() + ", subtotalActualDisbursementsOrders=" + this.getSubtotalActualDisbursementsOrders() + ", subtotalPlannedDisbursementsOrders=" + this.getSubtotalPlannedDisbursementsOrders() + ", subtotalPipelineDisbursementsOrders=" + this.getSubtotalPipelineDisbursementsOrders() + ", subtotalActualRoF=" + this.getSubtotalActualRoF() + ", subtotalPlannedRoF=" + this.getSubtotalPlannedRoF() + ", subtotalPipelineRoF=" + this.getSubtotalPipelineRoF() + ", subtotalRoF=" + this.getSubtotalRoF() + ", subtotalActualEDD=" + this.getSubtotalActualEDD() + ", subtotalPlannedEDD=" + this.getSubtotalPlannedEDD() + ", subtotalPipelineEDD=" + this.getSubtotalPipelineEDD() + ", subtotalEDD=" + this.getSubtotalEDD() + ", subtotalMTEFs=" + this.getSubtotalMTEFs() + ", undisbursementbalance=" + this.getUndisbursementbalance() + ", title=" + this.getTitle() + ", code=" + this.getCode() + ", fundingClassificationDate=" + this.getFundingClassificationDate() + ", groupVersionedFunding=" + this.getGroupVersionedFunding() + ", capitalSpendingPercentage=" + this.getCapitalSpendingPercentage() + ")";
	}
}