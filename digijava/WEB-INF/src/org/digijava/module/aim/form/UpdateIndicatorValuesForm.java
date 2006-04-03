/*
 * UpdateIndicatorValuesForm.java
 * Created : 21-Mar-2006
 */
package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class UpdateIndicatorValuesForm extends ActionForm {
	
	private Long indicatorId;
	private Long indicatorValId;
	private Long activityId;
	private float baseVal;
	private float targetVal;
	private float revTargetVal;
	private String baseValDate;
	private String targetValDate;
	private String revTargetValDate;
	
	private Collection indicators;
	private Long expIndicatorId;
	
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
	 * @return Returns the baseVal.
	 */
	public float getBaseVal() {
		return baseVal;
	}
	/**
	 * @param baseVal The baseVal to set.
	 */
	public void setBaseVal(float baseVal) {
		this.baseVal = baseVal;
	}
	/**
	 * @return Returns the baseValDate.
	 */
	public String getBaseValDate() {
		return baseValDate;
	}
	/**
	 * @param baseValDate The baseValDate to set.
	 */
	public void setBaseValDate(String baseValDate) {
		this.baseValDate = baseValDate;
	}
	/**
	 * @return Returns the expIndicatorId.
	 */
	public Long getExpIndicatorId() {
		return expIndicatorId;
	}
	/**
	 * @param expIndicatorId The expIndicatorId to set.
	 */
	public void setExpIndicatorId(Long expIndicatorId) {
		this.expIndicatorId = expIndicatorId;
	}
	/**
	 * @return Returns the indicatorId.
	 */
	public Long getIndicatorId() {
		return indicatorId;
	}
	/**
	 * @param indicatorId The indicatorId to set.
	 */
	public void setIndicatorId(Long indicatorId) {
		this.indicatorId = indicatorId;
	}
	/**
	 * @return Returns the indicators.
	 */
	public Collection getIndicators() {
		return indicators;
	}
	/**
	 * @param indicators The indicators to set.
	 */
	public void setIndicators(Collection indicators) {
		this.indicators = indicators;
	}
	/**
	 * @return Returns the indicatorValId.
	 */
	public Long getIndicatorValId() {
		return indicatorValId;
	}
	/**
	 * @param indicatorValId The indicatorValId to set.
	 */
	public void setIndicatorValId(Long indicatorValId) {
		this.indicatorValId = indicatorValId;
	}
	/**
	 * @return Returns the revTargetVal.
	 */
	public float getRevTargetVal() {
		return revTargetVal;
	}
	/**
	 * @param revTargetVal The revTargetVal to set.
	 */
	public void setRevTargetVal(float revTargetVal) {
		this.revTargetVal = revTargetVal;
	}
	/**
	 * @return Returns the revTargetValDate.
	 */
	public String getRevTargetValDate() {
		return revTargetValDate;
	}
	/**
	 * @param revTargetValDate The revTargetValDate to set.
	 */
	public void setRevTargetValDate(String revTargetValDate) {
		this.revTargetValDate = revTargetValDate;
	}
	/**
	 * @return Returns the targetVal.
	 */
	public float getTargetVal() {
		return targetVal;
	}
	/**
	 * @param targetVal The targetVal to set.
	 */
	public void setTargetVal(float targetVal) {
		this.targetVal = targetVal;
	}
	/**
	 * @return Returns the targetValDate.
	 */
	public String getTargetValDate() {
		return targetValDate;
	}
	/**
	 * @param targetValDate The targetValDate to set.
	 */
	public void setTargetValDate(String targetValDate) {
		this.targetValDate = targetValDate;
	}
	
}

