/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.DateColumns;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

/**
 * Reports utility methods
 * @author Nadejda Mandrescu
 */
public class MondrianReportUtils {
	protected static final Logger logger = Logger.getLogger(MondrianReportUtils.class);
	
	public static ReportEntityType getColumnEntityType(String columnName, ReportEntityType currentEntityType) {
		//if (Mappings.ALL_ENTITIES_COLUMNS.contains(columnName))
			return ReportEntityType.ENTITY_TYPE_ALL;
		//return currentEntityType;
	}
	
	public static ReportEntityType getMeasuresEntityType(String measureName, ReportEntityType currentEntityType) {
		//if (Mappings.ALL_ENTITIES_MEASURES.contains(measureName))
			return ReportEntityType.ENTITY_TYPE_ALL;
		//return currentEntityType;
	}
	
	public static ReportEntityType getReportEntityType(AmpReports report) throws AMPException {
		switch (report.getType().intValue()) {
		case ArConstants.DONOR_TYPE:
			//the only supported type for now
			return ReportEntityType.ENTITY_TYPE_ALL;
		default: 
			throw new AMPException("Not supported report translation for report type: " + report.getType());
		}
	}
	
	public static ReportColumn getColumn(String columnName, ReportEntityType currentEntityType) {
		return new ReportColumn(columnName, getColumnEntityType(columnName, currentEntityType));
	}
	
	public static ReportMeasure getMeasure(String measureName, ReportEntityType currentEntityType) {
		return new ReportMeasure(measureName, getMeasuresEntityType(measureName, currentEntityType));
	}
	
	public static ReportAreaImpl getNewReportArea(Class<? extends ReportAreaImpl> reportAreaType) throws AMPException {
		ReportAreaImpl reportArea = null;
		try {
			reportArea = reportAreaType.newInstance();
		} catch(Exception e) {
			throw new AMPException("Cannot instantiate " + reportAreaType.getName() + ". " + e.getMessage());
		}
		return reportArea;
	}
	
	/**
	 * flushes Mondrian Cache
	 */
	public static void flushCache() {
		new mondrian.rolap.CacheControlImpl(null).flushSchemaCache();
//		try {
//			RolapConnection rolapConn = olapConnection.unwrap(mondrian.rolap.RolapConnection.class);
//			rolapConn.getCacheControl(null).flushSchema(rolapConn.getSchema());
//		}
//		catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
	}
	
	/**
	 * @return default configuration for the current user settings
	 */
	public static MondrianReportSettings getCurrentUserDefaultSettings() {
		MondrianReportSettings settings = new MondrianReportSettings();
		settings.setCurrencyFormat(FormatHelper.getDefaultFormat());
		AmpApplicationSettings ampAppSettings = AmpARFilter.getEffectiveSettings();
		if (ampAppSettings == null) {
			settings.setCurrencyCode(CurrencyUtil.getDefaultCurrency().getCurrencyCode());
			settings.setCalendar(DbUtil.getAmpFiscalCalendar(DbUtil.getBaseFiscalCalendar()));
		} else { 
			settings.setCurrencyCode(ampAppSettings.getCurrency().getCurrencyCode());
			settings.setCalendar(ampAppSettings.getFiscalCalendar());
		}
		return settings;
	}
	
	/**
	 * Configures default & mandatory behavior
	 * @param spec - report specification
	 */
	public static void configureDefaults(ReportSpecification spec) {
		if (spec instanceof ReportSpecificationImpl) {
			ReportSpecificationImpl s = (ReportSpecificationImpl)spec;
			if (s.getSettings() == null) {
				s.setSettings(getCurrentUserDefaultSettings());
			}
			if (GroupingCriteria.GROUPING_TOTALS_ONLY.equals(s.getGroupingCriteria()))
				s.setCalculateColumnTotals(false);
		}
	}
	
	/**
	 * Retrieves column index for the specified column from the given ReportSpecification
	 * @param col
	 * @param spec
	 * @return
	 */
	public static int getColumnId(ReportColumn col, ReportSpecification spec) {
		if (spec == null || spec.getColumns() == null) return -1;
		int colId = 0;
		for (Iterator<ReportColumn> iter = spec.getColumns().iterator(); iter.hasNext(); colId++)
			if (iter.next().equals(col))
				break;
		return colId == spec.getColumns().size() ? -1 : colId; 
	}
	
	/**
	 * Filters out null dates. 
	 * The current solution is add an explicit upper limit to filter by if no filter is already configured for the date. 
	 * @param spec
	 */
	public static void filterOutNullDates(ReportSpecificationImpl spec) {
		MondrianReportFilters filters = spec.getFilters() == null ? new MondrianReportFilters() : (MondrianReportFilters)spec.getFilters();
		Set<ReportColumn> existingFilters = new HashSet<ReportColumn>();
		existingFilters.addAll(filters.getDateFilterRules().keySet());
		for(ReportColumn column : spec.getColumns()) {
			if (DateColumns.ACTIVITY_DATES.contains(column.getColumnName()) && !existingFilters.contains(column)) {
				try {
					filters.addDateRangeFilterRule(column, null, new Date(MoConstants.UNDEFINED_KEY -1));
				} catch (AmpApiException e) {
					logger.error(e);
				}
				existingFilters.add(column);
			}	
		}
		spec.setFilters(filters);
	}
}
