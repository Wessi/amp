/*
 * AmpTeam.java @Author Priyajith C Created: 13-Aug-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
public class AmpTeam  implements Serializable, Comparable, Identifiable, Versionable {
	
	private Long ampTeamId;

	private String name;
	
	private Boolean addActivity;
	private Boolean computation;

	private String description;

	//private String type; 			// Whether Bilateral or Multilateral
	
	private AmpCategoryValue type;  // Replaces the old "type" attribute (Bilateral, Multilateral)
	

	private AmpTeam parentTeamId;
	
	private Collection childrenWorkspaces;
	
	private String accessType;  	// Management or Team
	
	// added for donor-access
	private String teamCategory;	// Donor or Mofed team
	private AmpTeam relatedTeamId;	// a donor team referring a mofed team
	private Set<AmpActivity> activityList;		// activities assigned to donor team
	
	private Set organizations;		// activities assigned to donor team
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
	public Set<AmpActivity> getActivityList() {
		return activityList;
	}
	/**
	 * @param activityList The activityList to set.
	 */
	public void setActivityList(Set<AmpActivity> activityList) {
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

	public Set getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Set organizations) {
		this.organizations = organizations;
	}

	public Boolean getAddActivity() {
		return addActivity;
	}

	public void setAddActivity(Boolean addActivity) {
		this.addActivity = addActivity;
	}

	public Boolean getComputation() {
		return computation;
	}

	public void setComputation(Boolean computation) {
		this.computation = computation;
	}

	public Collection getChildrenWorkspaces() {
		return childrenWorkspaces;
	}

	public void setChildrenWorkspaces(Collection childrenWorkspaces) {
		this.childrenWorkspaces = childrenWorkspaces;
	}

	@Override
	public boolean equalsForVersioning(Object obj) {
		return this.equals(obj);
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Output getOutput() {
		return new Output(null, new String[] { this.name }, new Object[] { "" });
	}
}