package org.dgfoundation.amp.gpi.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.algo.BooleanWrapper;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * A utility class to transform a GeneratedReport to GPI Report 9b
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport9bOutputBuilder extends GPIReportOutputBuilder {

	public GPIReport9bOutputBuilder() {
		addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_YEAR));
		addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY));
		addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP));
		addColumn(new GPIReportOutputColumn(MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES,
				GPIReportConstants.REPORT_9_TOOLTIP.get(MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES)));
		addColumn(new GPIReportOutputColumn(MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES,
				GPIReportConstants.REPORT_9_TOOLTIP.get(MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES)));
		addColumn(new GPIReportOutputColumn(MeasureConstants.NATIONAL_AUDITING_PROCEDURES,
				GPIReportConstants.REPORT_9_TOOLTIP.get(MeasureConstants.NATIONAL_AUDITING_PROCEDURES)));
		addColumn(new GPIReportOutputColumn(MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES,
				GPIReportConstants.REPORT_9_TOOLTIP.get(MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES)));
	}

	public final static Set<String> YEAR_LEVEL_HIERARCHIES = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList(MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES,
					MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES,
					MeasureConstants.NATIONAL_AUDITING_PROCEDURES,
					MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES)));

	/**
	 * build the headers of the report
	 * 
	 * @param generatedReport
	 * @return
	 */
	@Override
	protected List<GPIReportOutputColumn> buildHeaders(GeneratedReport generatedReport) {
		List<GPIReportOutputColumn> headers = new ArrayList<>();
		headers.add(getColumns().get(GPIReportConstants.COLUMN_YEAR));

		String donorColumnName = isDonorAgency ? ColumnConstants.DONOR_AGENCY : ColumnConstants.DONOR_GROUP;
		headers.add(getColumns().get(donorColumnName));

		headers.add(getColumns().get(MeasureConstants.NATIONAL_BUDGET_EXECUTION_PROCEDURES));
		headers.add(getColumns().get(MeasureConstants.NATIONAL_FINANCIAL_REPORTING_PROCEDURES));
		headers.add(getColumns().get(MeasureConstants.NATIONAL_AUDITING_PROCEDURES));
		headers.add(getColumns().get(MeasureConstants.NATIONAL_PROCUREMENT_EXECUTION_PROCEDURES));

		return headers;
	}

	/**
	 * build the contents of the report
	 * 
	 * @param generatedReport
	 * @return
	 */
	@Override
	protected List<Map<GPIReportOutputColumn, String>> getReportContents(GeneratedReport generatedReport) {
		List<Map<GPIReportOutputColumn, String>> contents = new ArrayList<>();
		GPIReportOutputColumn yearColumn = getColumns().get(GPIReportConstants.COLUMN_YEAR);

		if (generatedReport.reportContents.getChildren() != null) {
			for (ReportArea reportArea : generatedReport.reportContents.getChildren()) {
				Map<GPIReportOutputColumn, String> columns = new HashMap<>();
				Map<String, Map<String, ReportCell>> years = new HashMap<>();
				for (ReportOutputColumn roc : generatedReport.leafHeaders) {
					ReportCell rc = reportArea.getContents().get(roc);
					rc = rc != null ? rc : TextCell.EMPTY;
					if (isMeasureColumn(roc)) {
						if (years.get(roc.parentColumn.columnName) == null) {
							years.put(roc.parentColumn.columnName, new HashMap<>());
						}
						years.get(roc.parentColumn.columnName).put(roc.columnName, rc);
					} else if (roc.parentColumn == null) {
						columns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
					}
				}

				years.forEach((k, v) -> {
					Map<GPIReportOutputColumn, String> row = new HashMap<>();
					row.put(yearColumn, k);
					final BooleanWrapper isRowEmpty = new BooleanWrapper(true);
					v.forEach((x, y) -> {
						row.put(getColumns().get(x), y.displayedValue);
						if (YEAR_LEVEL_HIERARCHIES.contains(x) && (((AmountCell) y).extractValue() != 0)) {
							isRowEmpty.set(false);
						}
					});

					if (!isRowEmpty.value) {
						row.putAll(columns);
						contents.add(row);
					}
				});
			}
		}

		Comparator<Map<GPIReportOutputColumn, String>> byYear = (Map<GPIReportOutputColumn, String> o1,
				Map<GPIReportOutputColumn, String> o2) -> o2.get(yearColumn).compareTo(o1.get(yearColumn));

		contents.sort(byYear);

		return contents;
	}

	/**
	 * build the contents of the report
	 * 
	 * @param generatedReport
	 * @return
	 */
	@Override
	protected Map<GPIReportOutputColumn, String> getReportSummary(GeneratedReport generatedReport) {

		Map<GPIReportOutputColumn, String> summaryColumns = new HashMap<>();
		for (ReportOutputColumn roc : generatedReport.leafHeaders) {
			ReportCell rc = generatedReport.reportContents.getContents().get(roc);
			rc = rc != null ? rc : TextCell.EMPTY;
			if (isTotalMeasureColumn(roc)) {
				summaryColumns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
			}
		}

		return summaryColumns;
	}

	/**
	 * @param roc
	 * @return
	 */
	private boolean isMeasureColumn(ReportOutputColumn roc) {
		return YEAR_LEVEL_HIERARCHIES.contains(roc.columnName)
				&& !NiReportsEngine.TOTALS_COLUMN_NAME.equals(roc.parentColumn.columnName);
	}

	/**
	 * @param roc
	 * @return
	 */
	private boolean isTotalMeasureColumn(ReportOutputColumn roc) {
		return YEAR_LEVEL_HIERARCHIES.contains(roc.originalColumnName)
				&& NiReportsEngine.TOTALS_COLUMN_NAME.equals(roc.parentColumn.originalColumnName);
	}
}
