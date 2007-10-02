package org.digijava.module.aim.helper;

import java.io.Serializable;
/**
 * @author jose
 *  
 */
public class FundingDetail implements Serializable
{
	private long indexId;
	private boolean checked;
	private int transactionType;
	private int adjustmentType;
	private String adjustmentTypeName;
	private String transactionDate;
	private String transactionAmount;
	private String perspectiveCode;
	private String perspectiveName;
	private String perspectiveNameTrimmed;
	private Long reportingOrganizationId;
	private String reportingOrganizationName;
	private String currencyCode;
	private String currencyName;
	private int index;
	private String classification;
	private boolean useFixedRate;
	private Double fixedExchangeRate;
	private Long ampComponentFundingId;
	
	/*
	private Long regionId;
	private String regionName;
	*/
	
	public FundingDetail() {}

	public FundingDetail(long id) {
		this.indexId = id;
	}

	
	public int getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(int adjustmentType) {
		this.adjustmentType = adjustmentType;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

/*	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
*/
	public String getPerspectiveCode() {
		return perspectiveCode;
	}

	public void setPerspectiveCode(String perspectiveCode) {
		this.perspectiveCode = perspectiveCode;
	}

	public String getPerspectiveName() {
		return perspectiveName;
	}

	public void setPerspectiveName(String perspectiveName) {
		this.perspectiveName = perspectiveName;
	}

	public String getReportingOrganizationName() {
		return reportingOrganizationName;
	}

	public void setReportingOrganizationName(String reportingOrganizationName) {
		this.reportingOrganizationName = reportingOrganizationName;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public Long getReportingOrganizationId() {
		return reportingOrganizationId;
	}

	public void setReportingOrganizationId(Long reportingOrganizationId) {
		this.reportingOrganizationId = reportingOrganizationId;
	}

	public String getAdjustmentTypeName() {
		return adjustmentTypeName;
	}
	
	public String getAdjustmentTypeNameTrimmed(){
		return adjustmentTypeName.replaceAll(" ","");
	}

	public void setAdjustmentTypeName(String adjustmentTypeName) {
		this.adjustmentTypeName = adjustmentTypeName;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return Returns the indexId.
	 */
	public long getIndexId() {
		return indexId;
	}
	/**
	 * @param indexId The indexId to set.
	 */
	public void setIndexId(long indexId) {
		this.indexId = indexId;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) throw new NullPointerException();
		if (!(obj instanceof FundingDetail)) throw new ClassCastException();
		
		FundingDetail fd = (FundingDetail) obj;
		return (this.indexId == fd.indexId);
	}
	/**
	 * @return Returns the classification.
	 */
	public String getClassification() {
		return classification;
	}
	/**
	 * @param classification The classification to set.
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * @return Returns the fixedExchangeRate.
	 */
	public Double getFixedExchangeRate() {
		return fixedExchangeRate;
	}

	/**
	 * @param fixedExchangeRate The fixedExchangeRate to set.
	 */
	public void setFixedExchangeRate(Double fixedExchangeRate) {
		this.fixedExchangeRate = fixedExchangeRate;
	}

	/**
	 * @return Returns the useFixedRate.
	 */
	public boolean isUseFixedRate() {
		return useFixedRate;
	}

	/**
	 * @param useFixedRate The useFixedRate to set.
	 */
	public void setUseFixedRate(boolean useFixedRate) {
		this.useFixedRate = useFixedRate;
	}

	public String getPerspectiveNameTrimmed() {
		return perspectiveName.replaceAll(" ","");
	}

	public void setPerspectiveNameTrimmed(String perspectiveNameTrimmed) {
		this.perspectiveNameTrimmed = perspectiveNameTrimmed;
	}

	public Long getAmpComponentFundingId() {
		return ampComponentFundingId;
	}

	public void setAmpComponentFundingId(Long ampComponentFundingId) {
		this.ampComponentFundingId = ampComponentFundingId;
	}
} 