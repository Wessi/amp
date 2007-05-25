package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

/**
 * This form is used for asynchronous JS request action on server side.
 * @see org.digijava.module.aim.action.GetActivities
 * @author Irakli Kobiashvili
 *
 */
public class ActivitiesForm extends ActionForm {

	private Long programId;
	private String statusId;
	private String startYear;
	private String endYear;
	private Long donorId;
	
	public Long getDonorId() {
		return donorId;
	}
	public void setDonorId(Long donorId) {
		this.donorId = donorId;
	}
	public String getEndYear() {
		return endYear;
	}
	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}
	public Long getProgramId() {
		return programId;
	}
	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	public String getStartYear() {
		return startYear;
	}
	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	
}
