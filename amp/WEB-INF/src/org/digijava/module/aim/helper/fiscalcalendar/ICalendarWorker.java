package org.digijava.module.aim.helper.fiscalcalendar;

import java.util.Date;

public interface ICalendarWorker {

	public Date getDate() throws Exception;

	/**
	 * Get the the YEAR according to the current fiscal calendar configuration
	 * 
	 * @return Integer
	 * @throws Exception
	 */
	public Integer getYear() throws Exception;

	/**
	 * Get the QUARTER according to the current fiscal calendar configuration
	 * 
	 * @return Integer
	 * @throws Exception
	 */
	public Integer getQuarter() throws Exception;

	/**
	 * Get the MONTH according to the current fiscal calendar configuration
	 * 
	 * @return Comparable
	 * @throws Exception
	 */
	public Comparable getMonth() throws Exception;

	/**
	 * The the time, and apply the current fiscal calendar configuration
	 * 
	 * @param time
	 */
	public void setTime(Date time);

	
	public Integer getYearDiff(ICalendarWorker worker) throws Exception;
	
}
