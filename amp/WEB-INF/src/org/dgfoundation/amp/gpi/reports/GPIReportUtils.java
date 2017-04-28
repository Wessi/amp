package org.dgfoundation.amp.gpi.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

public class GPIReportUtils {
	
	public static GeneratedReport getGeneratedReportForIndicator(String indicatorCode, JsonBean formParams, boolean isSummary) {
		switch(indicatorCode) {
		case GPIReportConstants.REPORT_5b:
			return  getGeneratedReportForIndicator5b(formParams, isSummary);
		case GPIReportConstants.REPORT_9b:
			return  getGeneratedReportForIndicator9b(formParams, isSummary);
		default:
			return null;
		}
	}
	
	/**
	 * create the template for the gpi report 5b. It can be refactored to another class
	 * 
	 * @param formParams
	 * @return
	 */
	public static GeneratedReport getGeneratedReportForIndicator5b(JsonBean formParams, boolean isSummary) {
		
		ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_5b, ArConstants.GPI_TYPE);
		
		if (!isSummary)  {
			String hierarchyColumn = getHierarchyColumn(formParams);
			if (hierarchyColumn.equals(GPIReportConstants.HIERARCHY_DONOR_GROUP)) {
				spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
				spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_GROUP));
			} else {
				spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
				spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
			}
			spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
		} 
		
		for (String mtefColumn : getMTEFColumnsForIndicator5b()) {
			spec.addColumn(new ReportColumn(mtefColumn));
		}
		
		spec.setSummaryReport(true);
		
 		if(formParams != null){
 			Map<String, Object> filters= (Map<String, Object>) formParams.get(EPConstants.FILTERS);
 			AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
 			
 			if(filterRules == null) {
 				filterRules = new AmpReportFilters();
 			} else if (!isSummary) {
 				filterRules.getFilterRules().clear();
 			}
 			
 			ReportElement elem = new ReportElement(new ReportColumn(ColumnConstants.APPROVAL_STATUS));
			
 			// Validated Activities - 4
 			FilterRule filterRule = new FilterRule(Arrays.asList("4"), true);
			filterRules.addFilterRule(elem, filterRule);
 			
			spec.setFilters(filterRules);
 		}
 		
 		SettingsUtils.applySettings(spec, formParams, true);
		
 		GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);
 		
 		return generatedReport;
	}
	
	/**
	 * create the template for the gpi report 9b. It can be refactored to another class
	 * 
	 * @param formParams
	 * @return
	 */
	public static GeneratedReport getGeneratedReportForIndicator9b(JsonBean formParams, boolean isSummary) {
		
		ReportSpecificationImpl spec = new ReportSpecificationImpl(GPIReportConstants.REPORT_9b, ArConstants.GPI_TYPE);
		
		if (!isSummary)  {
			String hierarchyColumn = getHierarchyColumn(formParams);
			if (hierarchyColumn.equals(GPIReportConstants.HIERARCHY_DONOR_GROUP)) {
				spec.addColumn(new ReportColumn(ColumnConstants.DONOR_GROUP));
				spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_GROUP));
			} else {
				spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
				spec.getHierarchies().add(new ReportColumn(ColumnConstants.DONOR_AGENCY));
			}
			spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
		}
		
		spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES));
		spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES));
		spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_AUDITING_PROCEDURES));
		spec.addMeasure(new ReportMeasure(MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES));
		spec.setSummaryReport(true);
		
 		if(formParams != null){
 			Map<String, Object> filters= (Map<String, Object>) formParams.get(EPConstants.FILTERS);
 			AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
 			
 			if(filterRules == null) {
 				filterRules = new AmpReportFilters();
 			} else if (!isSummary) {
 				filterRules.getFilterRules().clear();
 			}
 			
 			ReportElement elem = new ReportElement(new ReportColumn(ColumnConstants.APPROVAL_STATUS));
			
 			// Validated Activities - 4
 			FilterRule filterRule = new FilterRule(Arrays.asList("4"), true);
			filterRules.addFilterRule(elem, filterRule);
 			
			spec.setFilters(filterRules);
 		}
 		
 		SettingsUtils.applySettings(spec, formParams, true);
		
 		GeneratedReport generatedReport = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);
 		
 		return generatedReport;
	}
	
	private static String getHierarchyColumn(JsonBean formParams) {
		if (formParams.get(GPIReportConstants.HIERARCHY_PARAMETER) != null) {
			return (String) formParams.get(GPIReportConstants.HIERARCHY_PARAMETER);
		}
		
		return "";
	}
	
	public static Boolean getSummaryInfo(JsonBean formParams) {
		if (formParams.get(GPIReportConstants.SUMMARY_PARAMETER) != null) {
			return (Boolean) formParams.get(GPIReportConstants.SUMMARY_PARAMETER);
		}
		
		return false;
	}

	public static List<String> getMTEFColumnsForIndicator5b() {
		List<String> mtefColumns = new ArrayList<>();
		
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = 1; i <= 3; i++) {
			mtefColumns.add("MTEF " + (currentYear + i));
		}
		
		return mtefColumns;
	}
}
