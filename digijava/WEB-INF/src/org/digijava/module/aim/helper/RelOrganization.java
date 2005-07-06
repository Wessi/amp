/*
 * RelOrganization.java
 */

package org.digijava.module.aim.helper;

public class RelOrganization {
	private String orgName;
	private String role;
	
	public RelOrganization() {}
	
	/**
	 * @param orgName
	 * @param role
	 */
	public RelOrganization(String orgName, String role) {
		this.orgName = orgName;
		this.role = role;
	}
	/**
	 * @return Returns the orgName.
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName The orgName to set.
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return Returns the role.
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role The role to set.
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) throw new NullPointerException();
		if (!(obj instanceof RelOrganization)) throw new NullPointerException();
		
		RelOrganization relOrg = (RelOrganization) obj;
		return (relOrg.getOrgName().equals(orgName) && relOrg.getRole().equals(role));
	}
}