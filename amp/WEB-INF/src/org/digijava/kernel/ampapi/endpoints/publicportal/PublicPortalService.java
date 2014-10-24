package org.digijava.kernel.ampapi.endpoints.publicportal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import clover.com.google.common.base.Strings;


/**
 * Public Portal Service
 * @author Nadejda Mandrescu
 *
 */
public class PublicPortalService {
	protected static final Logger logger = Logger.getLogger(PublicPortalService.class);
	
	private static final int DEFAULT_PERIOD = 12; //months
	
	/**
	 * Retrieves top 'count' projects based on fixed requirements. <br>
	 * NOTE: the requirement is fixed at the moment, however we may need to provide some flexibility
	 * => JsonBean config can be used for that
	 * @param config - report config
	 * @param count - the maximum number of results to retrieve  
	 * @return JsonBean object with results
	 * Requirement:
	 * Top 20 projects (by commitment size) during the selected time period. 
	 * The totals should be displayed in the currency selected by the admin 
	 * and fields to display are 
	 * Start Date, 
	 * Donor(s), 
	 * Primary Sector(s), 
	 * Project Title, 
	 * Cumulative Commitments, 
	 * Cumulative Disbursements.
	 */
	public static JsonBean getTopProjects(JsonBean config, Integer count, Integer months) {
		JsonBean result = new JsonBean();
		List<JsonBean> content = new ArrayList<JsonBean>();
		result.set("topprojects", content);
		
		ReportSpecificationImpl spec = new ReportSpecificationImpl("PublicPortal_GetTopProjects");
		spec.addColumn(new ReportColumn(ColumnConstants.ACTUAL_START_DATE));
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
		
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
		
		spec.addSorter(new SortingInfo(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS), false, true));
		
		spec.setFilters(getPeriodFilter(months));
		/*TODO: tbd if we need to filter out null dates from results
		MondrianReportUtils.filterOutNullDates(spec);
		*/
		
		GeneratedReport report = EndpointUtils.runReport(spec);
		if (report == null) {
			
		} else {
			if (report.reportContents != null && report.reportContents.getChildren() != null) {
				//provide header titles
				Map<String, String> headersToId = new HashMap<String, String>(report.leafHeaders.size());
				Map<String, String> headers = new LinkedHashMap<String, String>(report.leafHeaders.size());
				Iterator<ReportColumn> colIter = spec.getColumns().iterator();
				for (ReportOutputColumn leafHeader : report.leafHeaders) {
					final String columnName = colIter.hasNext() ? colIter.next().getColumnName() : leafHeader.originalColumnName;
					final String id = StringUtils.replace(StringUtils.lowerCase(columnName), " ", "-");
					headers.put(id, leafHeader.columnName);
					headersToId.put(leafHeader.columnName, id);
				}
				result.set("headers", headers);
				
				//provide the top projects data
				count = Math.min(count, report.reportContents.getChildren().size());
				result.set("count", count);
				
				Iterator<ReportArea> iter = report.reportContents.getChildren().iterator();
				while (count > 0) {
					ReportArea data = iter.next();
					JsonBean jsonData = new JsonBean();
					for (Entry<ReportOutputColumn, ReportCell> cell : data.getContents().entrySet()) {
						jsonData.set(headersToId.get(cell.getKey().columnName), cell.getValue().displayedValue);
					}
					content.add(jsonData);
					count --;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * @return period = number of months, the public portal data must be filtered by 
	 */
	public static final int getPublicPortalPeriodInMonths() {
		String months = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PUBLIC_VIEW_LAST_PERIOD);
		int period = StringUtils.isNumeric(months) ? Integer.valueOf(months) : DEFAULT_PERIOD;
		return period;	
	}
	
	public static final ReportFilters getPeriodFilter(Integer months) {
		if (months == null)
			months = getPublicPortalPeriodInMonths();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -months);
		MondrianReportFilters filters = new MondrianReportFilters();
		try {
			filters.addDateRangeFilterRule(cal.getTime(), null);
		} catch (AmpApiException e) {
			logger.error(e);
		}
		return filters;
	}
	
	public static final ReportFilters getDefaultPublicFilter() {
		return getPeriodFilter(getPublicPortalPeriodInMonths());
	}
}
