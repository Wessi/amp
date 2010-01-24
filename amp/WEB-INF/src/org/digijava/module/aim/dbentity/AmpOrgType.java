
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Collection;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.DonorTypeDimension;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.Identifiable;

public class AmpOrgType implements Serializable,Comparable,Identifiable, ARDimensionable, HierarchyListable

{
	
	private Long ampOrgTypeId;
	private String orgType;
	private String orgTypeCode;
    @Deprecated
	private Boolean orgTypeIsGovernmental;

    // NGO, Governmental etc.
    private String classification;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
	//private Set organizations;
	
	/**
	 * @return Returns the ampOrgTypeId.
	 */
	public Long getAmpOrgTypeId() {
		return ampOrgTypeId;
	}
	/**
	 * @param ampOrgTypeId The ampOrgTypeId to set.
	 */
	public void setAmpOrgTypeId(Long ampOrgTypeId) {
		this.ampOrgTypeId = ampOrgTypeId;
	}
	/**
	 * @return Returns the orgType.
	 */
	public String getOrgType() {
		return orgType;
	}
	/**
	 * @param orgType The orgType to set.
	 */
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	/**
	 * @return Returns the orgTypeCode.
	 */
	public String getOrgTypeCode() {
		return orgTypeCode;
	}
	/**
	 * @param orgTypeCode The orgTypeCode to set.
	 */
	public void setOrgTypeCode(String orgTypeCode) {
		this.orgTypeCode = orgTypeCode;
	}
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		if(this.orgType == null) return -1;
		if(((AmpOrgType)arg0).getOrgType() == null) return 1;
		return this.orgType.compareTo(((AmpOrgType)arg0).getOrgType());  
		//return 0;
	}

	public String toString() {
		if (orgType != null) {
			return orgType;
		}
		return "";
	}
	public Object getIdentifier() {
		return ampOrgTypeId;
	}
	public Class getDimensionClass() {
	    return DonorTypeDimension.class;
	}
	public Boolean getOrgTypeIsGovernmental() {
		return orgTypeIsGovernmental;
	}
	public void setOrgTypeIsGovernmental(Boolean orgTypeIsGovernmental) {
		this.orgTypeIsGovernmental = orgTypeIsGovernmental;
	}

	@Override
	public Collection<AmpOrgType> getChildren() {
		return null;
	}

	@Override
	public int getCountDescendants() {
		return 1;
	}

	@Override
	public String getLabel() {
		return this.orgType;
	}

	@Override
	public String getUniqueId() {
		// TODO Auto-generated method stub
		return this.ampOrgTypeId + "";
	}
}
