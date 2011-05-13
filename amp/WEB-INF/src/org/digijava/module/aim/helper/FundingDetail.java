package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.budget.helper.BudgetDbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.PledgeForm;
/**
 * @author jose
 *
 */
public class FundingDetail implements Serializable, Comparable
{
	private Long fundingId;
	private long indexId;
	private boolean checked;
	private int transactionType;
	private int adjustmentType;
	private String adjustmentTypeName;
	private String adjustmentTypeNameTrimmed;
	private String transactionDate;
	private String transactionAmount;
	private Long reportingOrganizationId;
	private String reportingOrganizationName;
	private String currencyCode;
	private String currencyName;
	private int index;
	private String classification;
	private boolean useFixedRate;
	private String fixedExchangeRate;
	private Long ampComponentFundingId;
	

	private Long fundDetId;
    private String disbOrderId;
    private IPAContract contract;
    private Boolean disbursementOrderRejected;        
    private Long pledge;
    private AmpCategoryValue pledgename;
       
	
    
    
	public AmpCategoryValue getPledgename() {
		if (this.pledge!=null && !this.pledge.equals(0L)){
			FundingPledges pledge = PledgesEntityHelper.getPledgesById(this.pledge);
			return pledge.getTitle();
		}
		return pledgename;
	}

	public void setPledgename(AmpCategoryValue pledgename) {
		this.pledgename = pledgename;
	}
    
    public Long getPledge() {
		if (pledge!=null){
			return pledge;
		}else{
			return 0L;
		}
	}

	public void setPledge(Long pledge) {
		this.pledge = pledge;
	}

		public IPAContract getContract() {
            return contract;
        }

        public void setContract(IPAContract contract) {
            this.contract = contract;
        }

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
	public String getFixedExchangeRate() {
		return fixedExchangeRate;
	}

	/**
	 * @param fixedExchangeRate The fixedExchangeRate to set.
	 */
	public void setFixedExchangeRate(String fixedExchangeRate) {
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
	    if(useFixedRate==false){
		setFixedExchangeRate(null);
	    }
		this.useFixedRate = useFixedRate;
	}

	public Long getAmpComponentFundingId() {
		return ampComponentFundingId;
	}


        public Long getFundDetId() {
                return fundDetId;
        }

        public String getDisbOrderId() {
                return disbOrderId;
        }

        public void setAmpComponentFundingId(Long ampComponentFundingId) {
		this.ampComponentFundingId = ampComponentFundingId;
	}

        public void setFundDetId(Long fundDetId) {
                this.fundDetId = fundDetId;
        }

        public void setDisbOrderId(String disbOrderId) {
                this.disbOrderId = disbOrderId;
        }
        
        
        public String getFormattedRate(){
	 String returnValue=null;
	 if (getFixedExchangeRate()!=null){
		 DecimalFormat decFor=new DecimalFormat();
		 BigDecimal fixedExchangeRate=new BigDecimal(getFixedExchangeRate().replace(",", "."));
		 returnValue=decFor.format(fixedExchangeRate);
	 }
	 return returnValue;
 }

		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			FundingDetail obj=(FundingDetail) o;
			return this.getFundingId().compareTo(obj.getFundingId());
		}

		public Long getFundingId() {
			return fundingId;
		}

		public void setFundingId(Long fundingId) {
			this.fundingId = fundingId;
		}

		public void setDisbursementOrderRejected(
				Boolean disbursementOrderRejected) {
			this.disbursementOrderRejected = disbursementOrderRejected;
		}

		public Boolean getDisbursementOrderRejected() {
			return disbursementOrderRejected;
		}
      
        
}
