package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.*;
import java.io.Serializable;



public class IndicatorForm extends ActionForm implements Serializable
{
	private Long indId = null;
	private Long activityId;
	private String indicatorName = null;
	private String indicatorDesc = null;
	private String indicatorCode = null;
	private String searchKey = null;
	private boolean defaultFlag = false;
	private Long selectedIndicator = null;
	private Collection searchReturn = null;
	private Collection indicators = null;
	private Collection nondefaultindicators = null;	
	private Collection indicatorValues = null;
	private String sameIndicatorName = null;
	private String sameIndicatorCode = null;
	private boolean errorFlag;
	private String event;
	private Long selectedIndicators[];
	private Long selIndicators[];
	private String searchkey = null;
	private String addswitch = null;
	private boolean noSearchResult = false;
	
	public String getSameIndicatorCode() {
		return sameIndicatorCode;
	}

	public void setSameIndicatorCode(String sameIndicatorCode) {
		this.sameIndicatorCode = sameIndicatorCode;
	}

	public String getSameIndicatorName() {
		return sameIndicatorName;
	}

	public void setSameIndicatorName(String sameIndicatorName) {
		this.sameIndicatorName = sameIndicatorName;
	}

	public Collection getIndicators() {
		return indicators;
	}

	public void setIndicators(Collection indicators) {
		this.indicators = indicators;
	}

	public boolean getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(boolean defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public String getIndicatorCode() {
		return indicatorCode;
	}

	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}

	public String getIndicatorDesc() {
		return indicatorDesc;
	}

	public void setIndicatorDesc(String indicatorDesc) {
		this.indicatorDesc = indicatorDesc;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public Collection getIndicatorValues() {
		return indicatorValues;
	}

	public void setIndicatorValues(Collection indicatorValues) {
		this.indicatorValues = indicatorValues;
	}

	public Long getSelectedIndicator() {
		return selectedIndicator;
	}

	public void setSelectedIndicator(Long selectedIndicator) {
		this.selectedIndicator = selectedIndicator;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public Collection getSearchReturn() {
		return searchReturn;
	}

	public void setSearchReturn(Collection searchReturn) {
		this.searchReturn = searchReturn;
	}

	public Long getIndId() {
		return indId;
	}

	public void setIndId(Long indId) {
		this.indId = indId;
	}

	public boolean isErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return Returns the nondefaultindicators.
	 */
	public Collection getNondefaultindicators() {
		return nondefaultindicators;
	}

	/**
	 * @param nondefaultindicators The nondefaultindicators to set.
	 */
	public void setNondefaultindicators(Collection nondefaultindicators) {
		this.nondefaultindicators = nondefaultindicators;
	}

	/**
	 * @return Returns the searchkey.
	 */
	public String getSearchkey() {
		return searchkey;
	}

	/**
	 * @param searchkey The searchkey to set.
	 */
	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}

	/**
	 * @return Returns the selectedIndicators.
	 */
	public Long[] getSelectedIndicators() {
		return selectedIndicators;
	}

	/**
	 * @param selectedIndicators The selectedIndicators to set.
	 */
	public void setSelectedIndicators(Long[] selectedIndicators) {
		this.selectedIndicators = selectedIndicators;
	}

	/**
	 * @return Returns the activityId.
	 */
	public Long getActivityId() {
		return activityId;
	}

	/**
	 * @param activityId The activityId to set.
	 */
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	/**
	 * @return Returns the addswitch.
	 */
	public String getAddswitch() {
		return addswitch;
	}

	/**
	 * @param addswitch The addswitch to set.
	 */
	public void setAddswitch(String addswitch) {
		this.addswitch = addswitch;
	}

	/**
	 * @return Returns the selIndicators.
	 */
	public Long[] getSelIndicators() {
		return selIndicators;
	}

	/**
	 * @param selIndicators The selIndicators to set.
	 */
	public void setSelIndicators(Long[] selIndicators) {
		this.selIndicators = selIndicators;
	}

	/**
	 * @return Returns the noSearchResult.
	 */
	public boolean getNoSearchResult() {
		return noSearchResult;
	}

	/**
	 * @param noSearchResult The noSearchResult to set.
	 */
	public void setNoSearchResult(boolean noSearchResult) {
		this.noSearchResult = noSearchResult;
	}
}
