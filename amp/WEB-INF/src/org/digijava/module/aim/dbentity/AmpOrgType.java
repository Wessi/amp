
package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.util.Identifiable;

public class AmpOrgType implements Serializable,Comparable,Identifiable{
	
	private Long ampOrgTypeId;
	private String orgType;
	private String orgTypeCode;
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
}
