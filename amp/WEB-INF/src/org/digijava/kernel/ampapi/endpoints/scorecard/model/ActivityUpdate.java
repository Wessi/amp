package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import java.util.Date;


/**
 * 
 * @author Emanuel Perez
 * 
 */
public class ActivityUpdate {

	private Long donorId;
	private Long activityId;
	private Date modifyDate;
	private String userName;
	private Long auditLoggerId;
	private String workspaceName;
	private Date loggedDate;
	
	public Long getDonorId() {
		return donorId;
	}
	public void setDonorId(Long donorId) {
		this.donorId = donorId;
	}
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Long getAuditLoggerId() {
		return auditLoggerId;
	}
	public void setAuditLoggerId(Long auditLoggerId) {
		this.auditLoggerId = auditLoggerId;
	}
	public String getWorkspaceName() {
		return workspaceName;
	}
	public void setWorkspaceName(String workspaceName) {
		this.workspaceName = workspaceName;
	}
	public Date getLoggedDate() {
		return loggedDate;
	}
	public void setLoggedDate(Date loggedDate) {
		this.loggedDate = loggedDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
