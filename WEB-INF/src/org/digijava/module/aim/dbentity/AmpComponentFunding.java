/*
 * AmpComponentFunding.java
 * Created : 30-Aug-2005
 */

package org.digijava.module.aim.dbentity;

import java.util.Date;

public class AmpComponentFunding{
	
	private Long ampComponentFundingId;
	private AmpActivity activity;
	private Integer transactionType;
	private Integer adjustmentType;
	private Date transactionDate;
	private Date reportingDate;
	private Double transactionAmount;
	private AmpOrganisation reportingOrganization;
	private AmpCurrency currency;
	private String expenditureCategory;
	private AmpComponent component;
	private AmpPerspective perspective;
	/**
	 * @return Returns the activity.
	 */
	public AmpActivity getActivity() {
		return activity;
	}
	/**
	 * @param activity The activity to set.
	 */
	public void setActivity(AmpActivity activity) {
		this.activity = activity;
	}
	/**
	 * @return Returns the adjustmentType.
	 */
	public Integer getAdjustmentType() {
		return adjustmentType;
	}
	/**
	 * @param adjustmentType The adjustmentType to set.
	 */
	public void setAdjustmentType(Integer adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	/**
	 * @return Returns the ampComponentFundingId.
	 */
	public Long getAmpComponentFundingId() {
		return ampComponentFundingId;
	}
	/**
	 * @param ampComponentFundingId The ampComponentFundingId to set.
	 */
	public void setAmpComponentFundingId(Long ampComponentFundingId) {
		this.ampComponentFundingId = ampComponentFundingId;
	}
	/**
	 * @return Returns the component.
	 */
	public AmpComponent getComponent() {
		return component;
	}
	/**
	 * @param component The component to set.
	 */
	public void setComponent(AmpComponent component) {
		this.component = component;
	}
	/**
	 * @return Returns the currency.
	 */
	public AmpCurrency getCurrency() {
		return currency;
	}
	/**
	 * @param currency The currency to set.
	 */
	public void setCurrency(AmpCurrency currency) {
		this.currency = currency;
	}
	/**
	 * @return Returns the expenditureCategory.
	 */
	public String getExpenditureCategory() {
		return expenditureCategory;
	}
	/**
	 * @param expenditureCategory The expenditureCategory to set.
	 */
	public void setExpenditureCategory(String expenditureCategory) {
		this.expenditureCategory = expenditureCategory;
	}
	/**
	 * @return Returns the perspective.
	 */
	public AmpPerspective getPerspective() {
		return perspective;
	}
	/**
	 * @param perspective The perspective to set.
	 */
	public void setPerspective(AmpPerspective perspective) {
		this.perspective = perspective;
	}
	/**
	 * @return Returns the reportingDate.
	 */
	public Date getReportingDate() {
		return reportingDate;
	}
	/**
	 * @param reportingDate The reportingDate to set.
	 */
	public void setReportingDate(Date reportingDate) {
		this.reportingDate = reportingDate;
	}
	/**
	 * @return Returns the reportingOrganization.
	 */
	public AmpOrganisation getReportingOrganization() {
		return reportingOrganization;
	}
	/**
	 * @param reportingOrganization The reportingOrganization to set.
	 */
	public void setReportingOrganization(AmpOrganisation reportingOrganization) {
		this.reportingOrganization = reportingOrganization;
	}
	/**
	 * @return Returns the transactionAmount.
	 */
	public Double getTransactionAmount() {
		return transactionAmount;
	}
	/**
	 * @param transactionAmount The transactionAmount to set.
	 */
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	/**
	 * @return Returns the transactionDate.
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}
	/**
	 * @param transactionDate The transactionDate to set.
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	/**
	 * @return Returns the transactionType.
	 */
	public Integer getTransactionType() {
		return transactionType;
	}
	/**
	 * @param transactionType The transactionType to set.
	 */
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public int hashCode() {
		if (this.ampComponentFundingId == null)
			return 0;
		return ampComponentFundingId.intValue();
	}
	
	public boolean equals(Object obj) {
		if (obj == null) throw new NullPointerException();
		if (!(obj instanceof AmpComponentFunding)) throw new ClassCastException();
		if(this.ampComponentFundingId==null) return false;
		
		AmpComponentFunding comp = (AmpComponentFunding) obj;
		return this.ampComponentFundingId.equals(comp.ampComponentFundingId);
		
	}
	
}
