package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.Collection;

import org.digijava.module.aim.dbentity.AmpModality;
import org.digijava.module.aim.dbentity.AmpTermsAssist;

/**
 * @author jose
 *
 */
public class Funding implements Serializable 
{
    private int fundingId;
	private AmpTermsAssist ampTermsAssist;
	private String orgFundingId;
	private String signatureDate;
	private AmpModality modality;
	private Collection fundingDetails;	// Collection of Funding Details
   	private String currentFunding;
   	private String propStartDate;
   	private String propCloseDate;
   	private String actStartDate;
   	private String actCloseDate;
   	private String reportingDate;
   	private String conditions;
	
	public AmpTermsAssist getAmpTermsAssist() {
		return ampTermsAssist;
	}
	
	public void setAmpTermsAssist(AmpTermsAssist ampTermsAssist) {
		this.ampTermsAssist = ampTermsAssist;
	}
	
	public String getOrgFundingId() {
		return orgFundingId;
	}
	
	public void setOrgFundingId(String orgFundingId) {
		this.orgFundingId = orgFundingId;
	}
	
	public String getSignatureDate() {
		return signatureDate;
	}
	
	public void setSignatureDate(String signatureDate) {
		this.signatureDate = signatureDate;
	}
    public Collection getFundingDetails() {
        return fundingDetails;
    }
    public void setFundingDetails(Collection fundingDetails) {
        this.fundingDetails = fundingDetails;
    }
    public int getFundingId() {
        return fundingId;
    }
    public void setFundingId(int fundingId) {
        this.fundingId = fundingId;
    }
 
    public String getCurrentFunding() {
        return currentFunding;
    }
    
    public void setCurrentFunding(String currentFunding) {
        this.currentFunding = currentFunding;
    }
	/**
	 * @return Returns the actCloseDate.
	 */
	public String getActCloseDate() {
		return actCloseDate;
	}
	/**
	 * @param actCloseDate The actCloseDate to set.
	 */
	public void setActCloseDate(String actCloseDate) {
		this.actCloseDate = actCloseDate;
	}
	/**
	 * @return Returns the actStartDate.
	 */
	public String getActStartDate() {
		return actStartDate;
	}
	/**
	 * @param actStartDate The actStartDate to set.
	 */
	public void setActStartDate(String actStartDate) {
		this.actStartDate = actStartDate;
	}
	/**
	 * @return Returns the propCloseDate.
	 */
	public String getPropCloseDate() {
		return propCloseDate;
	}
	/**
	 * @param propCloseDate The propCloseDate to set.
	 */
	public void setPropCloseDate(String propCloseDate) {
		this.propCloseDate = propCloseDate;
	}
	/**
	 * @return Returns the propStartDate.
	 */
	public String getPropStartDate() {
		return propStartDate;
	}
	/**
	 * @param propStartDate The propStartDate to set.
	 */
	public void setPropStartDate(String propStartDate) {
		this.propStartDate = propStartDate;
	}
	/**
	 * @return Returns the conditions.
	 */
	public String getConditions() {
		return conditions;
	}
	/**
	 * @param conditions The conditions to set.
	 */
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	/**
	 * @return Returns the reportingDate.
	 */
	public String getReportingDate() {
		return reportingDate;
	}
	/**
	 * @param reportingDate The reportingDate to set.
	 */
	public void setReportingDate(String reportingDate) {
		this.reportingDate = reportingDate;
	}
	/**
	 * @return Returns the modality.
	 */
	public AmpModality getModality() {
		return modality;
	}
	/**
	 * @param modality The modality to set.
	 */
	public void setModality(AmpModality modality) {
		this.modality = modality;
	}
}
