
package org.digijava.module.calendar.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.digijava.module.calendar.dbentity.Calendar.TBD;
import org.digijava.module.calendar.form.CalendarItemForm;

/**
 * Class with static methods used for Data convertion and form population 
 */
public class CalendarUtil {
	
	/**
	 * Converts java.util.Calendar to a String value considering TBD value.
	 * @param calendar
	 * @param tbd
	 * @return formated String
	 * @see #formatDate(Date, TBD)
	 */
	public static String formatDate(Calendar calendar, TBD tbd) {
		return formatDate(calendar.getTime(), tbd);
	}
	
	/**
	 * Converts java.util.Date to String taking into considration 
	 * the specifics of our event (Calendar) ie TBD and allDayEvent values
	 * @param date
	 * @param tbd
	 * @return formated String (eventually with "TBD") 
	 * @see org.digijava.module.calendar.dbentity.Calendar
	 * @see #formatDate(Calendar, TBD)
	 */
	public static String formatDate(Date date, TBD tbd) {
		StringBuffer buffer = new StringBuffer();
		if (tbd == TBD.YEAR) buffer.append("TBD");
		else if (tbd == TBD.MONTH) buffer.append("TBD ").append(new SimpleDateFormat("yyyy").format(date));
		else if (tbd == TBD.DAY) buffer.append("TBD ").append(new SimpleDateFormat("MMM yyyy").format(date));
		else if (tbd == TBD.HOUR) buffer.append("TBD ").append(new SimpleDateFormat("dd MMM yyyy").format(date));
		else if (tbd == TBD.MINUTE) buffer.append("TBD ").append(new SimpleDateFormat("HH, dd MMM yyyy").format(date));
		else {
			if (isAllDayEvent(date)) buffer.append(new SimpleDateFormat("dd MMM yyyy").format(date));
			else buffer.append(new SimpleDateFormat("HH:mm, dd MMM yyyy").format(date));
		}
			
		return buffer.toString();
	}
	
	
	/**
	 * This method populates CalendarItemForm with start date and end date 
	 * @param event
	 * @param form
	 * @see #populateDate(Date, CalendarItemForm.DateForm, TBD)
	 * @see CalendarItemForm
	 * @see CalendarItemForm.DateForm
	 */
	public static void populateDates(org.digijava.module.calendar.dbentity.Calendar event, CalendarItemForm form) {
		populateDate(event.getStartDate(),form.getStart(), event.getStartTBD());
		populateDate(event.getEndDate(), form.getEnd(), event.getEndTBD());
	}
	
	/**
	 * Populates an instance of CalendarItemForm.DateForm (start or end for CalendarItemForm)
	 * with year, month, date (day), hour, minute, allDayEvent values 
	 * @param date
	 * @param form
	 * @param tbd
	 */
	public static void populateDate(Date date, CalendarItemForm.DateForm form, TBD tbd) {
        Calendar calendar = Calendar.getInstance();
        
        calendar.setTime(date);
        if (tbd != TBD.DAY) form.setDate(Integer.toString(calendar.get(Calendar.DATE)));
        if (tbd != TBD.MONTH) form.setMonth(Integer.toString(calendar.get(Calendar.MONTH) + 1));
        if (tbd != TBD.YEAR) form.setYear(Integer.toString(calendar.get(Calendar.YEAR)));
        
        
        if (isAllDayEvent(calendar)) {
            form.setAllDayEvent(true);
        } else {
            form.setAllDayEvent(false);
            form.setHour(String.valueOf(calendar.get(Calendar.HOUR)));
            form.setMin(String.valueOf(calendar.get(Calendar.MINUTE)));
        }
		
	}
	
	
	
	/**
	 * Calculates whenever our date instance is considered all day event
	 * @param date
	 * @return true is date instance (for our logic) is all day event
	 * 		   false otherwise
	 * @see #isAllDayEvent(Calendar)
	 */
	
	public static boolean isAllDayEvent(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return isAllDayEvent(calendar);
	}
	
	
	
	/**
	 * We consider our calendar instance to be all day event if second == 0 (min) 
	 * @param calendar
	 * @return true is all day event (for our logic)
	 * 		   false otherwise
	 * @see #isAllDayEvent(Date)
	 */	
	public static boolean isAllDayEvent(Calendar calendar) {
		return calendar.get(Calendar.SECOND) == calendar.getActualMinimum(Calendar.SECOND);
	}
}
