package org.dgfoundation.amp.gpi.reports;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.common.util.DateTimeUtil;

public class GPIReportUtils {

	/**
	 * 
	 * @param indicatorCode
	 * @param formParams
	 * @return generatedRerport {@link GeneratedReport}
	 */
	public static GeneratedReport getGeneratedReportForIndicator(String indicatorCode, JsonBean formParams) {

		switch (indicatorCode) {
			case GPIReportConstants.REPORT_5a:
				return getGeneratedReportForIndicator5a(formParams);
			case GPIReportConstants.REPORT_5b:
				return getGeneratedReportForIndicator5b(formParams);
			case GPIReportConstants.REPORT_6:
				return getGeneratedReportForIndicator6(formParams);
			case GPIReportConstants.REPORT_9b:
				return getGeneratedReportForIndicator9b(formParams);
			default:
				throw new RuntimeException("Wrong indicator code:" + indicatorCode);
		}
	}

	/**
	 * Create the template for the GPI report 5a
	 * 
	 * @param formParams
	 * @return generatedReport 
	 */
	public static GeneratedReport getGeneratedReportForIndicator5a(JsonBean formParams) {

		ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_5a,
				ArConstants.DONOR_TYPE);

		if (isDonorAgency(formParams)) {
			spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
			spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
		} else {
			spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
			spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_GROUP));
		}
		
		spec.addColumn(new ReportColumn(ColumnConstants.ON_OFF_TREASURY_BUDGET));
		spec.addColumn(new ReportColumn(ColumnConstants.HAS_EXECUTING_AGENCY));
		
		spec.getHierarchies().add(new ReportColumn(ColumnConstants.ON_OFF_TREASURY_BUDGET));
		spec.getHierarchies().add(new ReportColumn(ColumnConstants.HAS_EXECUTING_AGENCY));

		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
		spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
		spec.addMeasure(new ReportMeasure(MeasureConstants.DISBURSED_AS_SCHEDULED));
		spec.addMeasure(new ReportMeasure(MeasureConstants.OVER_DISBURSED));
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
		spec.setSummaryReport(true);

		applyAppovalStatusFilter(formParams, spec);
		applySettings(formParams, spec);
		clearYearRangeSettings(spec);

		GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

		return generatedReport;
	}

	/**
	 * Create the template for the GPI report 5b
	 * 
	 * @param formParams
	 * @return generatedReport 
	 */
	public static GeneratedReport getGeneratedReportForIndicator5b(JsonBean formParams) {

		ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_5b,
				ArConstants.DONOR_TYPE);

		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
		spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_GROUP));
		
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
		spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));

		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
		spec.setSummaryReport(true);

		applyAppovalStatusFilter(formParams, spec);
		applySettings(formParams, spec);
		clearYearRangeSettings(spec);

		for (String mtefColumn : getMTEFColumnsForIndicator5b(spec)) {
			spec.addColumn(new ReportColumn(mtefColumn));
		}

		GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

		return generatedReport;
	}

	/**
	 * Create the template for the GPI report 6
	 * 
	 * @param formParams
	 * @return generatedReport 
	 */
	public static GeneratedReport getGeneratedReportForIndicator6(JsonBean formParams) {

		ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_6, ArConstants.DONOR_TYPE);

		if (isDonorAgency(formParams)) {
			spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
			spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
		} else {
			spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
			spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_GROUP));
		}
		
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
		spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
		spec.setSummaryReport(true);

		applyAppovalStatusFilter(formParams, spec);
		applySettings(formParams, spec);
		clearYearRangeSettings(spec);
		
		GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

		return generatedReport;
	}

	/**
	 * Create the template for the GPI report 9b.
	 * 
	 * @param formParams
	 * @return generatedReport 
	 */
	public static GeneratedReport getGeneratedReportForIndicator9b(JsonBean formParams) {

		ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_9b, ArConstants.GPI_TYPE);

		if (isDonorAgency(formParams)) {
			spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
			spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
		} else {
			spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
			spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_GROUP));
		}
		
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
		spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES));
		spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES));
		spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_AUDITING_PROCEDURES));
		spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES));
		spec.setSummaryReport(true);

		applyAppovalStatusFilter(formParams, spec);
		applySettings(formParams, spec);
		clearYearRangeSettings(spec);

		GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);

		return generatedReport;
	}

	public static String getHierarchyColumn(JsonBean formParams) {
		if (formParams.get(GPIReportConstants.HIERARCHY_PARAMETER) != null) {
			return (String) formParams.get(GPIReportConstants.HIERARCHY_PARAMETER);
		}

		return GPIReportConstants.HIERARCHY_DONOR_AGENCY;
	}
	
	public static boolean isDonorAgency(JsonBean formParams) {
		String donorHierarchy = getHierarchyColumn(formParams);
		
		return !GPIReportConstants.HIERARCHY_DONOR_GROUP.equals(donorHierarchy);
	}
	
	/**
	 * @param formParams
	 * @param spec
	 */
	public static void applyAppovalStatusFilter(JsonBean formParams, ReportSpecificationImpl spec) {
		if (formParams != null) {
			Map<String, Object> filters = (Map<String, Object>) formParams.get(EPConstants.FILTERS);
			AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);

			if (filterRules == null) {
				filterRules = new AmpReportFilters();
			}

			ReportElement elem = new ReportElement(new ReportColumn(ColumnConstants.APPROVAL_STATUS));

			// Validated Activities - 4
			FilterRule filterRule = new FilterRule(Arrays.asList("4"), true);
			filterRules.addFilterRule(elem, filterRule);

			spec.setFilters(filterRules);
		}
	}
	
	/**
	 * Apply settings on report specifications
	 * 
	 * @param formParams
	 * @param spec
	 */
	public static void applySettings(JsonBean formParams, ReportSpecificationImpl spec) {
		spec.setSettings(MondrianReportUtils.getCurrentUserDefaultSettings());
		spec.getSettings().getCurrencyFormat().setMinimumFractionDigits(0);
		SettingsUtils.applySettings(spec, formParams, true);
	}

	/**
	 * @param spec
	 */
	public static void clearYearRangeSettings(ReportSpecificationImpl spec) {
		ReportSettingsImpl reportSettings = (ReportSettingsImpl) spec.getSettings();
		reportSettings.setYearRangeFilter(null);
	}

	public static List<String> getMTEFColumnsForIndicator5b(ReportSpecification spec) {
		
		FilterRule dateFilterRule = getDateFilterRule(spec);
		if (dateFilterRule == null || StringUtils.isBlank(dateFilterRule.min)) {
			throw new RuntimeException("No year selected. Please specify the date filter");
		}
		
		Date fromJulianNumberToDate = DateTimeUtil.fromJulianNumberToDate(dateFilterRule.min);
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromJulianNumberToDate);
		int year = cal.get(Calendar.YEAR);

		List<String> mtefColumns = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			mtefColumns.add("MTEF " + (year + i));
		}

		return mtefColumns;
	}

	public static FilterRule getDateFilterRule(ReportSpecification spec) {
		Optional<Entry<ReportElement, FilterRule>> dateRuleEntry = spec.getFilters()
				.getFilterRules().entrySet().stream()
				.filter(entry -> entry.getKey().type.equals(ElementType.DATE))
				.filter(entry -> entry.getKey().entity == null)
				.findAny();
		
		FilterRule dateRule = dateRuleEntry.isPresent() ? dateRuleEntry.get().getValue() : null;
		
		return dateRule;
	}
}
