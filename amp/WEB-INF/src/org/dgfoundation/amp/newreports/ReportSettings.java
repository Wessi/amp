/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.text.DecimalFormat;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

/**
 * Stores Report Settings like currency to display, currency format, calendar type, year ranges to display and other. 
 * Any display filters (like year ranges) will affect only the columns, but not the totals. To filter the totals, please use {@link ReportFilters}
 * @author Nadejda Mandrescu
 */
public interface ReportSettings extends ReportFilters {
	
	/** returns currency code to be used for amounts display */ 
	public String getCurrencyCode();
	
	/** returns a DecimalFormat for currency display format */ 
	public DecimalFormat getCurrencyFormat();
	
	/** returns the Calendar to be used for grouping by dates, filtering by dates, etc */ 
	public AmpFiscalCalendar getCalendar();
}
