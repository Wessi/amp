/*
 * Created on 1/03/2006
 * 
 * @author akashs
 * 
 */
package org.digijava.module.aim.dbentity;

import java.util.Set;

public class AmpAhsurvey {

	private Long ampAHSurveyId;
	
	//private AmpFunding ampFundingId;
	//private Integer surveyYear;
	
	private AmpActivity ampActivityId;
	private AmpOrganisation ampDonorOrgId;
	
	private Set responses;
	
	/**
	 * @return Returns the ampAHSurveyId.
	 */
	public Long getAmpAHSurveyId() {
		return ampAHSurveyId;
	}
	/**
	 * @param ampAHSurveyId The ampAHSurveyId to set.
	 */
	public void setAmpAHSurveyId(Long ampAHSurveyId) {
		this.ampAHSurveyId = ampAHSurveyId;
	}
	/**
	 * @return Returns the responses.
	 */
	public Set getResponses() {
		return responses;
	}
	/**
	 * @param responses The responses to set.
	 */
	public void setResponses(Set responses) {
		this.responses = responses;
	}
	/**
	 * @return Returns the ampActivityId.
	 */
	public AmpActivity getAmpActivityId() {
		return ampActivityId;
	}
	/**
	 * @param ampActivityId The ampActivityId to set.
	 */
	public void setAmpActivityId(AmpActivity ampActivityId) {
		this.ampActivityId = ampActivityId;
	}
	/**
	 * @return Returns the ampDonorOrgId.
	 */
	public AmpOrganisation getAmpDonorOrgId() {
		return ampDonorOrgId;
	}
	/**
	 * @param ampDonorOrgId The ampDonorOrgId to set.
	 */
	public void setAmpDonorOrgId(AmpOrganisation ampDonorOrgId) {
		this.ampDonorOrgId = ampDonorOrgId;
	}
}
