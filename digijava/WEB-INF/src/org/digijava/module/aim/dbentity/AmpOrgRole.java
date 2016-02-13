package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;


public class AmpOrgRole implements Serializable
{
    private Long ampOrgRoleId;
    private AmpActivity activity;
	private AmpOrganisation organisation;
	private AmpRole role;
	
    /**
     * @return Returns the activity.
     */
    public AmpActivity getActivity() {
        return activity;
    }
    /**
     * @param activity The activity to set.
     */
    public void setActivity(AmpActivity activity) {
        this.activity = activity;
    }
    /**
     * @return Returns the ampOrgRoleId.
     */
    public Long getAmpOrgRoleId() {
        return ampOrgRoleId;
    }
    /**
     * @param ampOrgRoleId The ampOrgRoleId to set.
     */
    public void setAmpOrgRoleId(Long ampOrgRoleId) {
        this.ampOrgRoleId = ampOrgRoleId;
    }
    /**
     * @return Returns the organisation.
     */
    public AmpOrganisation getOrganisation() {
        return organisation;
    }
    /**
     * @param organisation The organisation to set.
     */
    public void setOrganisation(AmpOrganisation organisation) {
        this.organisation = organisation;
    }
    /**
     * @return Returns the role.
     */
    public AmpRole getRole() {
        return role;
    }
    /**
     * @param role The role to set.
     */
    public void setRole(AmpRole role) {
        this.role = role;
    }
    
	public boolean equals(Object obj) {
		if (obj == null)
			throw new NullPointerException();
		
		if (!(obj instanceof AmpOrgRole))
			throw new ClassCastException();
		
		AmpOrgRole orgRole = (AmpOrgRole) obj;
		return (orgRole.getActivity().getAmpActivityId().equals(activity.getAmpActivityId()) &&
		        orgRole.getOrganisation().getAmpOrgId().equals(organisation.getAmpOrgId()) && 
		        orgRole.getRole().getAmpRoleId().equals(role.getAmpRoleId()));
	}
}	