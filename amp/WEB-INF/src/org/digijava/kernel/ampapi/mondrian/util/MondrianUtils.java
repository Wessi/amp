/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mondrian.olap.MondrianException;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.module.common.util.DateTimeUtil;
import org.olap4j.CellSet;
import org.olap4j.OlapException;
import org.olap4j.layout.RectangularCellSetFormatter;
import org.olap4j.mdx.parser.MdxParseException;
import org.olap4j.metadata.Datatype;

/**
 * Mondrian utility class 
 * @author Nadejda Mandrescu
 *
 */
public class MondrianUtils {
	protected static final Logger logger = Logger.getLogger(MondrianUtils.class);
	
	public static String PRINT_PATH = null; 
	
	/**
	 * Gets full OlapException error details
	 * @param e OlapException
	 * @return String with full OlapException error details
	 */
	public static String getOlapExceptionMessage(OlapException e) {
		return "OlapException [" + e.getErrorCode() + "]: ctx=" + e.getContext()+ ", region=" + e.getRegion() + ", msg=" + e.getMessage() 
				+ ", SQLException: " + ErrorReportingPlugin.getSQLExceptionMessage(e.getNextException(), 1)
				+ getExceptionDetails((Exception)e.getCause(), 2);
	}
	
	public static String getMdxParseException(MdxParseException e) {
		return "MdxParseException at region=" + e.getRegion() + ", msg=" + e.getMessage()
				+ getExceptionDetails((Exception)e.getCause(), 1);
	}
	
	public static String getMondrianException(MondrianException e) {
		return "MondrianException: " + e.getMessage() + ". " 
				+ getExceptionDetails((MondrianException)e.getCause(), 1);
	}
	
	private static String getExceptionDetails(Exception e, int depth) {
		if (e==null || depth==0) return "";
		return e.getClass() + ": " + e.getMessage() + ". " + getExceptionDetails((Exception)e.getCause(), depth-1);
	}
	
	/**
	 * Identifies if it is an Olap specific exception and reconstructs the error message details
	 * @param e - an exception
	 * @return error message
	 */
	public static String toString(Exception e) {
		if (e instanceof MdxParseException)
			return getMdxParseException((MdxParseException)e);
		if (e instanceof OlapException)
			return getOlapExceptionMessage((OlapException)e);
		if (e instanceof MondrianException)
			return getMondrianException((MondrianException)e);
		if (e instanceof RuntimeException) {
			if (e.getCause() instanceof MdxParseException) {
				return getMdxParseException((MdxParseException)e.getCause());
			}
		}
		return e.getMessage();
	}
	
	/* candidate for removal
	public static MDXAttribute getDuplicate(MDXAttribute mdxAttr) {
		MDXLevel newLevel = new MDXLevel(mdxAttr);
		String hierarchy = MondrianMapping.getDuplicateHierarchy(newLevel.getHierarchy());   
		newLevel.setHierarchy(hierarchy);
		return newLevel;
	}
	*/
	
	/**
	 * Prints formated cellSet to standard system console or file if {@link PRINT_PATH} is configured
	 * @param cellSet
	 * @param reportName
	 */
	public static void print(CellSet cellSet, String reportName) {
		RectangularCellSetFormatter formatter = new RectangularCellSetFormatter(false);
		
		PrintWriter writer = getOutput(reportName);
		
		formatter.format(cellSet, writer);
		writer.flush();
		writer.close();
	}
	
	public static PrintWriter getOutput(String reportName) {
		PrintWriter writer = null;
		if (PRINT_PATH != null){
			String fileName = PRINT_PATH + (PRINT_PATH.endsWith(File.separator) ? "" : File.separator) + reportName + ".txt";
			try {
				File file = new File(fileName);
				file.getParentFile().mkdirs();
				file.createNewFile();
				writer = new PrintWriter(file);	
			} catch (IOException e) {
				logger.error("Writing to standard output, because cannot write to specified file path \"" + fileName + "\": " + e.getMessage());
			}
		}
		if (writer == null)
			writer = new PrintWriter(System.out);
		
		return writer;
	}
	
	public static String getDatatypeName(Datatype type) {
		switch(type) {
		case INTEGER:
		case UNSIGNED_INTEGER : return "Integer";
		//to see if other needs conversion
		default: return type.name();
		}
	}
	
	/**
	 * Builds a filter between [from .. to] or [from .. infinite) or (-infinite .. to] year ranges.
	 * @param from - the year to start from or null
	 * @param to - the year to end with or null
	 * @throws AmpApiException if range is invalid
	 */
	public static FilterRule getYearsRangeFilter(Integer from, Integer to) throws AmpApiException {
		return getDatesRangeFilterRule(ElementType.YEAR, from, to, false);
	}
	
	/**
	 * Builds a filter for specific  [from .. to] quarters, with no year limits
	 * @param from - from quarter limit
	 * @param to - to quarter limit
	 * @throws AmpApiException if range is invalid
	 */
	public static FilterRule getQuarterRangeFilterRule(Integer from, Integer to) throws AmpApiException {
		return getDatesRangeFilterRule(ElementType.QUARTER, from, to, false);
	}
	
	/**
	 * Builds a filter for specific months in all years. Month numbers between [1..12] 
	 * @param from - first month number of the range
	 * @param to - last month number of the range 
	 * @throws AmpApiException if range is invalid
	 */
	public static FilterRule getMonthRangeFilterRule(Integer from, Integer to) throws AmpApiException {
		return getDatesRangeFilterRule(ElementType.MONTH, from, to, true);
	}
	
	/**
	 * Builds a date range filter [from .. to] or [from .. infinite ) or (infinite .. to]
	 * @param from - the date to start from or null
	 * @param to - the date to end with or null
	 * @throws AmpApiException if range is invalid
	 */
	public static FilterRule getDateRangeFilterRule(Date from, Date to) throws AmpApiException {
		return getDatesRangeFilterRule(ElementType.DATE,  DateTimeUtil.toJulianDayNumber(from), DateTimeUtil.toJulianDayNumber(to), false);
	}
	
	private static FilterRule getDatesRangeFilterRule(ElementType elemType, Integer from, Integer to, boolean bothLimits) throws AmpApiException {
		validate (elemType, from);
		validate (elemType, to);
		if (from == null && to == null)
			throw new AmpApiException(elemType + ": at least 'from' or 'to' range limit must be specified. Do not use the range filter if no filter is needed.");
		if (from != null && to != null && from > to)
			throw new AmpApiException("The lower limit 'from' must be smaller or equal to the upper limit 'to'. Failed request for from = " + from + ", to = " + to);
		if (to == null)
			to = MoConstants.UNDEFINED_KEY - 1; //to skip undefined dates
		return new FilterRule(toStringOrNull(from), toStringOrNull(to), true, true, true);
	}
	
	/**
	 * Builds a filter for list of years 
	 * @param years - years to filter by
	 * @param valuesToInclude - true if this years to be kept, false if this years must be excluded
	 * @throws AmpApiException if list is invalid
	 */
	public static FilterRule getYearsFilterRule(List<Integer> years, boolean valuesToInclude) throws AmpApiException {
		return getDatesListFilterRule(ElementType.YEAR, years, valuesToInclude);
	}
	
	/**
	 * Builds a filter for a list of quarters 
	 * @param quarters - the list of quarters [1..4]
	 * @param valuesToInclude - configures if this is a list of quarters to be kept (true) or to be excluded (false)
	 * @throws AmpApiException if list is invalid
	 */
	public static FilterRule getQuarterFilterRule(List<Integer> quarters, boolean valuesToInclude) throws AmpApiException {
		return getDatesListFilterRule(ElementType.QUARTER, quarters, valuesToInclude);
	}
	
	/**
	 * Builds a filter for specific months list in all years
	 * @param months - month numbers between [1..12]
	 * @param valuesToInclude - true if this months must be kept, false if they must be excluded
	 * @throws AmpApiException if list is invalid
	 */
	public static FilterRule getMonthsFilterRule(List<Integer> months, boolean valuesToInclude) throws AmpApiException {
		return getDatesListFilterRule(ElementType.MONTH, months, valuesToInclude);
	}
	
	/**
	 * Builds a date list filter 
	 * @param dates - the dates to filter by 
	 * @param valuesToInclude - true if this dates must be kept, false if they must be excluded
	 * @throws AmpApiException if list is invalid
	 */
	public static FilterRule getDatesFilterRule(List<Date> dates, boolean valuesToInclude) throws AmpApiException {
		List<Integer> julianDateNumbers = new ArrayList<Integer>(dates.size());
		for (Date date : dates) {
			julianDateNumbers.add(DateTimeUtil.toJulianDayNumber(date));
		}
		return getDatesListFilterRule(ElementType.DATE, julianDateNumbers, valuesToInclude);
	}
	
	private static FilterRule getDatesListFilterRule(ElementType elemType, List<Integer> values, boolean valuesToInclude) throws AmpApiException {
		List<String> strValues = new ArrayList<String>(values.size());
		for (Integer value : values) {
			validate(elemType, value);
			strValues.add(value == null ? null : value.toString());
		}
		return new FilterRule(strValues, valuesToInclude, true);
	}
	
	/**
	 * Builds a single year filter 
	 * @param year
	 * @param valueToInclude
	 * @throws AmpApiException
	 */
	public static FilterRule getSingleYearFilterRule(Integer year, boolean valueToInclude) throws AmpApiException {
		return getSingleDateFilterRule(ElementType.YEAR, year, valueToInclude);
	}
	
	/**
	 * Builds a single quarter filter, no years filter
	 * @param quarter
	 * @param valueToInclude
	 * @throws AmpApiException
	 */
	public static FilterRule getSingleQuarterFilterRule(Integer quarter, boolean valueToInclude) throws AmpApiException {
		return getSingleDateFilterRule(ElementType.QUARTER, quarter, valueToInclude);
	}
	
	/**
	 * Builds a single month filter, no years filter
	 * @param month
	 * @param valueToInclude
	 * @throws AmpApiException
	 */
	public static FilterRule getSingleMonthFilterRule(Integer month, boolean valueToInclude) throws AmpApiException {
		return getSingleDateFilterRule(ElementType.MONTH, month, valueToInclude);
	}
	
	/**
	 * Builds a single date filter
	 * @param month
	 * @param valueToInclude
	 * @throws AmpApiException
	 */
	public static FilterRule getSingleDateFilterRule(Date date, boolean valueToInclude) throws AmpApiException {
		return getSingleDateFilterRule(ElementType.MONTH, DateTimeUtil.toJulianDayNumber(date), valueToInclude);
	}
	
	private static FilterRule getSingleDateFilterRule(ElementType elemType, Integer value, boolean valueToInclude) throws AmpApiException {
		validate (elemType, value);
		if (value == null)
			throw new AmpApiException("Single value filter must have a value specified. value = " + value);
		return new FilterRule(value.toString(), valueToInclude, true);
	}
	
	private static void validate(ElementType elemType, Integer value) throws AmpApiException {
		Integer lowerLimit = null;
		Integer upperLimit = null;
		boolean mustBeNotNull = false;
		switch (elemType) {
		case YEAR : 
		case DATE : lowerLimit = 0; upperLimit  = Integer.MAX_VALUE; break;
		case QUARTER : lowerLimit = 1; upperLimit  = 4; mustBeNotNull = true; break;
		case MONTH : lowerLimit = 1; upperLimit  = 12; mustBeNotNull = true; break;
		default: break;
		}
		if (mustBeNotNull && value == null || lowerLimit != null && value != null && (value < lowerLimit || value > upperLimit ))
			throw new AmpApiException(elemType + " range limits must be within [" + lowerLimit + ", " + upperLimit + "]. Value not in the range = " + value);
	}
	
	private static String toStringOrNull(Object o) {
		return o == null ? null : o.toString();
	}
}
