/*
 *  AmpTeamMember.java
 *  @Author Priyajith C
 *  Created: 13-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import org.digijava.kernel.user.User;
import java.util.*;
import java.io.Serializable;

public class AmpTeamMember implements Serializable {
	
	private Long ampTeamMemId;
	private User user;
	private AmpTeam ampTeam;
	private AmpTeamMemberRoles ampMemberRole;
	private Boolean readPermission;     /* whether the team member has read permission on the team pages */
	private Boolean writePermission;    /* whether the team member has write permission on the team pages */
	private Boolean deletePermission;   /* whether the team member has delete permission on the team pages */
	private Set activities;
	private Set reports;

	public void setReports(Set reports) {
		this.reports = reports;
	}

	public Set getReports() {
			  return this.reports;
	}
	
	/**
	 * @return ampTeam
	 */
	public AmpTeam getAmpTeam() {
		return ampTeam;
	}

	/**
	 * @return ampMemberRole
	 */
	public AmpTeamMemberRoles getAmpMemberRole() {
		return ampMemberRole;
	}

	/**
	 * @return ampTeamMemId
	 */
	public Long getAmpTeamMemId() {
		return ampTeamMemId;
	}

	/**
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param ampTeam
	 */
	public void setAmpTeam(AmpTeam ampTeam) {
		this.ampTeam = ampTeam;
	}

	/**
	 * @param ampMemberRole
	 */
	public void setAmpMemberRole(AmpTeamMemberRoles ampMemberRole) {
		this.ampMemberRole = ampMemberRole;
	}

	/**
	 * @param ampTeamMemId
	 */
	public void setAmpTeamMemId(Long ampTeamMemId) {
		this.ampTeamMemId = ampTeamMemId;
	}

	/**
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getReadPermission() {
		return readPermission;
	}

	public void setReadPermission(Boolean readPermission) {
		this.readPermission = readPermission;
	}

	public Boolean getWritePermission() {
		return writePermission;
	}

	public void setWritePermission(Boolean writePermission) {
		this.writePermission = writePermission;
	}
	
	public Boolean getDeletePermission() {
		return deletePermission;
	}

	public void setDeletePermission(Boolean deletePermission) {
		this.deletePermission = deletePermission;
	}		

	public Set getActivities() {
		return activities;
	}

	public void setActivities(Set activities) {
		this.activities = activities;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) return false;
		
		if (!(obj instanceof AmpTeamMember)) {
			throw new ClassCastException();
		}
		
		AmpTeamMember temp = (AmpTeamMember) obj;
		return temp.getAmpTeamMemId().equals(ampTeamMemId);
	}

}
