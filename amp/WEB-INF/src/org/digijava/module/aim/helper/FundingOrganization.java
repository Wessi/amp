package org.digijava.module.aim.helper;

import java.util.Collection;

/**
 * @author jose
 */
public class FundingOrganization implements Comparable{
	
	private Long ampOrgId;
	private String orgName ;
	//Collection of Funding objects
	private Collection fundings;
	private String currentOrganization;
	Boolean fundingActive;
	String fundingActiveString;
	Boolean delegatedCooperation;
	String delegatedCooperationString;
	Boolean delegatedPartner;
	String delegatedPartnerString;
	
	
	
	public Long getAmpOrgId() {
		return ampOrgId;
	}
	
	public void setAmpOrgId(Long ampOrgId) {
		this.ampOrgId = ampOrgId;
	}
	
	public String getOrgName() {
		return orgName;
	}
	
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public Collection getFundings() {
		return fundings;
	}
	
	public void setFundings(Collection fundings) {
		this.fundings = fundings;
	}
    public String getCurrentOrganization() {
        return currentOrganization;
    }
    public void setCurrentOrganization(String currentOrganization) {
        this.currentOrganization = currentOrganization;
    }
    
    public boolean equals(Object e) {
    	if (e instanceof FundingOrganization) {
    		FundingOrganization forg = (FundingOrganization) e;
    		return ampOrgId.longValue() == forg.getAmpOrgId().longValue();
    	}
    	throw new ClassCastException();
    }

	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		FundingOrganization comparedOrganization = (FundingOrganization)arg0;
		return this.orgName.compareTo(comparedOrganization.orgName);
		//return -1;
	}

	public Boolean getDelegatedCooperation() {
		return delegatedCooperation;
	}

	public void setDelegatedCooperation(Boolean delegatedCooperation) {
		this.delegatedCooperation = delegatedCooperation;
	}

	public Boolean getDelegatedPartner() {
		return delegatedPartner;
	}

	public void setDelegatedPartner(Boolean delegatedPartner) {
		this.delegatedPartner = delegatedPartner;
	}

	public Boolean getFundingActive() {
		return fundingActive;
	}

	public void setFundingActive(Boolean fundingActive) {
		this.fundingActive = fundingActive;
	}

	public String getDelegatedCooperationString() {
		return delegatedCooperationString;
	}

	public void setDelegatedCooperationString(String delegatedCooperationString) {
		this.delegatedCooperationString = delegatedCooperationString;
	}

	public String getDelegatedPartnerString() {
		return delegatedPartnerString;
	}

	public void setDelegatedPartnerString(String delegatedPartnerString) {
		this.delegatedPartnerString = delegatedPartnerString;
	}

	public String getFundingActiveString() {
		return fundingActiveString;
	}

	public void setFundingActiveString(String fundingActiveString) {
		this.fundingActiveString = fundingActiveString;
	}

	
}
