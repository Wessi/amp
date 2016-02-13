/*
* AMP GLOBAL SETTINGS 
*/
package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpGlobalSettings implements Serializable {
	private Long globalId;
	private String globalSettingsName;
	private String globalSettingsValue;
	/**
	 * @return Returns the globalSettingsName.
	 */
	public String getGlobalSettingsName() {
		return globalSettingsName;
	}
	/**
	 * @param globalSettingsName The globalSettingsName to set.
	 */
	public void setGlobalSettingsName(String globalSettingsName) {
		this.globalSettingsName = globalSettingsName;
	}
	/**
	 * @return Returns the globalSettingsValue.
	 */
	public String getGlobalSettingsValue() {
		return globalSettingsValue;
	}
	/**
	 * @param globalSettingsValue The globalSettingsValue to set.
	 */
	public void setGlobalSettingsValue(String globalSettingsValue) {
		this.globalSettingsValue = globalSettingsValue;
	}
	/**
	 * @return Returns the globalId.
	 */
	public Long getGlobalId() {
		return globalId;
	}
	/**
	 * @param globalId The globalId to set.
	 */
	public void setGlobalId(Long globalId) {
		this.globalId = globalId;
	}
		
}