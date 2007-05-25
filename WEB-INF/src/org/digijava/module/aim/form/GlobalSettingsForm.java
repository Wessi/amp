package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.*;
public class GlobalSettingsForm extends ActionForm {
	Collection gsfCol = null;
	Collection countryNameCol = null;
	String countryName = null;
	Long gsfId;
	String gsfName = null;
	String gsfValue = null;
	
	Long globalId;
	String globalSettingsName = null;
	
	
	Map possibleValues 					= new HashMap(); // The values are of type org.digijava.module.aim.helper.KeyValue
	Map possibleValuesDictionary 		= new HashMap(); // The values are of also of type HashMap (key-value)
	
	String newSettingName;
	String newTableName;
	
	public String getNewSettingName() {
		return newSettingName;
	}
	public Long getGsfId() {
		return gsfId;
	}
	public void setGsfId(Long gsfId) {
		this.gsfId = gsfId;
	}
	public void setNewSettingName(String newSettingName) {
		this.newSettingName = newSettingName;
	}
	public String getNewTableName() {
		return newTableName;
	}
	public void setNewTableName(String newTableName) {
		this.newTableName = newTableName;
	}
	public Long getGlobalId() {
		return globalId; 
	}
	public void setGlobalId(Long gsfId) {
		this.globalId = gsfId;
	}
	public String getGlobalSettingsName() {
		return globalSettingsName;
	}
	public void setGlobalSettingsName(String gsfName) {
		this.globalSettingsName = gsfName;
	}
	public String getGsfValue() {
		return gsfValue;
	}
	public void setGsfValue(String gsfValue) {
		this.gsfValue = gsfValue;
	}
	public Collection getGsfCol() {
		return gsfCol;
	}
	public void setGsfCol(Collection gsfCol) {
		this.gsfCol = gsfCol;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Collection getCountryNameCol() {
		return countryNameCol;
	}
	public void setCountryNameCol(Collection countryNameCol) {
		this.countryNameCol = countryNameCol;
	}

	public void setPossibleValues(String key, Collection values) {
		possibleValues.put(key, values);
	}
	public Collection getPossibleValues(String key) {
		return (Collection) possibleValues.get(key);
	}
	
	public void setPossibleValuesDictionary(String key, Map dictionary) {
		possibleValuesDictionary.put(key, dictionary);
	}
	public Map getPossibleValuesDictionary(String key) {
		return (Map) possibleValuesDictionary.get(key);
	}
	

	
}