package org.digijava.module.aim.helper.fiscalcalendar;

import java.util.Date;

import org.joda.time.DateTime;

public interface ICalendarWorker  {

	/**
	 * The date in the given calendar
	 * 
	 * @return
	 * @throws Exception
	 */
	public DateTime getCalendarDate();
	
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
	public ComparableMonth getMonth() throws Exception;

	/**
	 * The the time, and apply the current fiscal calendar configuration
	 * 
	 * @param time
	 */
	public void setTime(Date time);

	
	public Integer getYearDiff(ICalendarWorker worker) throws Exception;
	
	public String getFiscalYear() throws Exception;
   
	public ComparableMonth getFiscalMonth() throws Exception;
}
