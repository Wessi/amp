/*
 * AmpTeam.java @Author Priyajith C Created: 13-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.util.Identifiable;
public class AmpTeam  implements Serializable, Comparable, Identifiable {
	
	private Long ampTeamId;

	private String name;

	private String description;

	private AmpTeamMember teamLead; // Denotes the Team Leader

	//private String type; 			// Whether Bilateral or Multilateral
	
	private AmpCategoryValue type;  // Replaces the old "type" attribute (Bilateral, Multilateral) 

	private AmpTeam parentTeamId;
	
	private String accessType;  	// Management or Team
	
	// added for donor-access
	private String teamCategory;	// Donor or Mofed team
	private AmpTeam relatedTeamId;	// a donor team referring a mofed team
	private Set activityList;		// activities assigned to donor team
	private NpdSettings npdSettings;

	public NpdSettings getNpdSettings() {
		return npdSettings;
	}

	public void setNpdSettings(NpdSettings npdSettings) {
		this.npdSettings = npdSettings;
	}

	/**
	 * @return ampTeamId
	 */
	public Long getAmpTeamId() {
		return ampTeamId;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return teamLeadId
	 */
	public AmpTeamMember getTeamLead() {
		return teamLead;
	}

	/**
	 * @param ampTeamId
	 */
	public void setAmpTeamId(Long ampTeamId) {
		this.ampTeamId = ampTeamId;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param teamLeadId
	 */
	public void setTeamLead(AmpTeamMember teamLead) {
		this.teamLead = teamLead;
	}

//	public String getType() {
//		return type;
//	}

//	public void setType(String type) {
//		this.type = type;
//	}

	/**
	 * @return Returns the parentTeamId.
	 */
	public AmpTeam getParentTeamId() {
		return parentTeamId;
	}

	/**
	 * @param parentTeamId
	 *            The parentTeamId to set.
	 */
	public void setParentTeamId(AmpTeam parentTeamId) {
		this.parentTeamId = parentTeamId;
	}

	/**
	 * @return Returns the accessType.
	 */
	public String getAccessType() {
		return accessType;
	}

	/**
	 * @param accessType
	 *            The accessType to set.
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) throw new NullPointerException();
		if (!(obj instanceof AmpTeam)) throw new ClassCastException();
		
		AmpTeam temp = (AmpTeam) obj;
		return (this.ampTeamId.equals(temp.getAmpTeamId()));
	}
	/**
	 * @return Returns the activityList.
	 */
	public Set getActivityList() {
		return activityList;
	}
	/**
	 * @param activityList The activityList to set.
	 */
	public void setActivityList(Set activityList) {
		this.activityList = activityList;
	}
	/**
	 * @return Returns the relatedTeam.
	 */
	public AmpTeam getRelatedTeamId() {
		return relatedTeamId;
	}
	/**
	 * @param relatedTeam The relatedTeam to set.
	 */
	public void setRelatedTeamId(AmpTeam relatedTeam) {
		this.relatedTeamId = relatedTeam;
	}
	/**
	 * @return Returns the teamCategory.
	 */
	public String getTeamCategory() {
		return teamCategory;
	}
	/**
	 * @param teamCategory The teamCategory to set.
	 */
	public void setTeamCategory(String teamCategory) {
		this.teamCategory = teamCategory;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		return ampTeamId.compareTo(((AmpTeam)o).getAmpTeamId());
	}
	
	public String toString() {
		return name;
	}

	public Object getIdentifier() {
		return this.getAmpTeamId();
	}

	public AmpCategoryValue getType() {
		return type;
	}

	public void setType(AmpCategoryValue type) {
		this.type = type;
	}
}