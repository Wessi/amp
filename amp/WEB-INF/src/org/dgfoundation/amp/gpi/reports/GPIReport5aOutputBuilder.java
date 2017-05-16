package org.dgfoundation.amp.gpi.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.algo.BooleanWrapper;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;

/**
 * A utility class to transform a GeneratedReport to GPI Report 6
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport5aOutputBuilder extends GPIReportOutputBuilder {

	public static final String TOTAL_ACTUAL_DISBURSEMENTS = "Total Actual Disbursements";
	public static final String CONCESSIONAL = "Concessional";
	public static final String DISBURSEMENTS_OTHER_PROVIDERS = "Disbursements through other providers";
	public static final String REMARK = "Remark";

	public static final String ACTIVITY_BUDGET_ON = "On Budget";
	public static final String YES_VALUE = "yes";

	public GPIReport5aOutputBuilder() {
		addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_YEAR));
		addColumn(new GPIReportOutputColumn(TOTAL_ACTUAL_DISBURSEMENTS));
		addColumn(new GPIReportOutputColumn(CONCESSIONAL));
		addColumn(new GPIReportOutputColumn(DISBURSEMENTS_OTHER_PROVIDERS));
		addColumn(new GPIReportOutputColumn(MeasureConstants.ACTUAL_DISBURSEMENTS));
		addColumn(new GPIReportOutputColumn(MeasureConstants.PLANNED_DISBURSEMENTS));
		addColumn(new GPIReportOutputColumn(MeasureConstants.DISBURSED_AS_SCHEDULED));
		addColumn(new GPIReportOutputColumn(MeasureConstants.OVER_DISBURSED));
		addColumn(new GPIReportOutputColumn(REMARK));
	}

	public final static Set<String> YEAR_LEVEL_HIERARCHIES = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS,
					MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.DISBURSED_AS_SCHEDULED,
					MeasureConstants.OVER_DISBURSED, TOTAL_ACTUAL_DISBURSEMENTS)));

	public final static Set<String> ON_BUDGET_MEASURES = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS,
					MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.DISBURSED_AS_SCHEDULED,
					MeasureConstants.OVER_DISBURSED, DISBURSEMENTS_OTHER_PROVIDERS)));

	public final static Map<String, ReportOutputColumn> headersMap = new HashMap<>();

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

		GPIReportOutputColumn donorColumn = null;
		addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY));
		addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP));
		for (ReportOutputColumn roc : generatedReport.leafHeaders) {
			if (ColumnConstants.DONOR_AGENCY.equals(roc.originalColumnName)) {
				donorColumn = new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY);
			} else if (ColumnConstants.DONOR_GROUP.equals(roc.originalColumnName)) {
				donorColumn = new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP);
				isDonorAgency = false;
			}
			headersMap.putIfAbsent(roc.originalColumnName, roc);
		}

		if (donorColumn != null) {
			headers.add(donorColumn);
		}

		headers.add(getColumns().get(TOTAL_ACTUAL_DISBURSEMENTS));
		headers.add(getColumns().get(CONCESSIONAL));
		headers.add(getColumns().get(MeasureConstants.ACTUAL_DISBURSEMENTS));
		headers.add(getColumns().get(MeasureConstants.PLANNED_DISBURSEMENTS));
		headers.add(new GPIReportOutputColumn(DISBURSEMENTS_OTHER_PROVIDERS));
		headers.add(getColumns().get(MeasureConstants.DISBURSED_AS_SCHEDULED));
		headers.add(getColumns().get(MeasureConstants.OVER_DISBURSED));
		headers.add(new GPIReportOutputColumn(REMARK));

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
		List<ReportArea> mockList = generatedReport.reportContents.getChildren();
		for (ReportArea reportArea : mockList) {
			Map<GPIReportOutputColumn, String> columns = new HashMap<>();
			Map<String, Map<String, ReportCell>> years = new HashMap<>();

			for (ReportOutputColumn roc : generatedReport.leafHeaders) {
				ReportCell rc = reportArea.getContents().get(roc);
				rc = rc != null ? rc : TextCell.EMPTY;
				if ((roc.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS))
						&& !roc.parentColumn.originalColumnName.equals(NiReportsEngine.TOTALS_COLUMN_NAME)) {
					if (years.get(roc.parentColumn.columnName) == null) {
						years.put(roc.parentColumn.columnName, getEmptyGPIRow(generatedReport.spec));
					}
					years.get(roc.parentColumn.columnName).put(TOTAL_ACTUAL_DISBURSEMENTS, rc);
				} else if (roc.originalColumnName.equals(ColumnConstants.DONOR_AGENCY)
						|| roc.originalColumnName.equals(ColumnConstants.DONOR_GROUP)) {
					columns.put(new GPIReportOutputColumn(roc), rc.displayedValue);
					columns.put(new GPIReportOutputColumn(REMARK),
							getRemarkEndpointURL(generatedReport.spec, reportArea.getOwner().id));
				}
			}

			Set<Integer> concessional = new HashSet<>();
			for (ReportArea budgetArea : reportArea.getChildren()) {
				ReportCell rc = budgetArea.getContents().get(headersMap.get(ColumnConstants.ON_OFF_TREASURY_BUDGET));
				if (String.valueOf(rc.value).equals(ACTIVITY_BUDGET_ON)) {
					concessional.add(1);
				} else if (String.valueOf(rc.value).equals("Off Budget")) {
					concessional.add(0);
				}
			}

			columns.put(new GPIReportOutputColumn(CONCESSIONAL), StringUtils.join(concessional.toArray(), ","));

			Optional<ReportArea> onBudgetAreaElement = reportArea.getChildren().stream()
					.filter(budgetArea -> isOnBudget(budgetArea)).findAny();

			if (onBudgetAreaElement.isPresent()) {
				for (ReportOutputColumn roc : generatedReport.leafHeaders) {
					ReportCell rc = onBudgetAreaElement.get().getContents().get(roc);
					rc = rc != null ? rc : TextCell.EMPTY;
					if ((ON_BUDGET_MEASURES.contains(roc.originalColumnName))
							&& !roc.parentColumn.originalColumnName.equals(NiReportsEngine.TOTALS_COLUMN_NAME)) {
						if (years.get(roc.parentColumn.originalColumnName) == null) {
							years.put(roc.parentColumn.originalColumnName, new HashMap<>());
						}

						if (rc != TextCell.EMPTY) {
							years.get(roc.parentColumn.originalColumnName).put(roc.originalColumnName, rc);
						}
					}
				}

				Optional<ReportArea> hasExecArea = onBudgetAreaElement.get().getChildren().stream()
						.filter(execArea -> hasExecutingAgency(execArea)).findAny();

				if (hasExecArea.isPresent()) {
					for (ReportOutputColumn roc : generatedReport.leafHeaders) {
						ReportCell rc = hasExecArea.get().getContents().get(roc);
						rc = rc != null ? rc : TextCell.EMPTY;
						if ((MeasureConstants.ACTUAL_DISBURSEMENTS.equals(roc.originalColumnName))
								&& !roc.parentColumn.originalColumnName.equals(NiReportsEngine.TOTALS_COLUMN_NAME)) {
							if (years.get(roc.parentColumn.originalColumnName) == null) {
								years.put(roc.parentColumn.originalColumnName, new HashMap<>());
							}
							years.get(roc.parentColumn.originalColumnName).put(DISBURSEMENTS_OTHER_PROVIDERS, rc);
						}
					}
				}
			}

			years.forEach((k, v) -> {
				Map<GPIReportOutputColumn, String> row = new HashMap<>();
				row.put(yearColumn, k);
				final BooleanWrapper isRowEmpty = new BooleanWrapper(true);
				v.forEach((x, y) -> {
					if (MeasureConstants.DISBURSED_AS_SCHEDULED.equals(x)
							|| MeasureConstants.OVER_DISBURSED.equals(x)) {
						BigDecimal percentage = new BigDecimal(((AmountCell) y).extractValue());
						row.put(getColumns().get(x), percentage.setScale(0, RoundingMode.UP) + "%");
					} else {
						row.put(getColumns().get(x), y.displayedValue);
					}
					if (YEAR_LEVEL_HIERARCHIES.contains(x)) {
						if (y instanceof AmountCell && (((AmountCell) y).extractValue() != 0)) {
							isRowEmpty.set(false);
						}

						if (y instanceof TextCell && StringUtils.isNotBlank(y.value.toString())) {
							isRowEmpty.set(false);
						}
					}
				});

				if (!isRowEmpty.value) {
					row.putAll(columns);
					contents.add(row);
				}
			});
		}

		contents.sort(getByYearDonorComparator(getYearColumn(), getDonorColumn()));

		return contents;
	}

	/**
	 * Generate the remark endpoint url
	 * 
	 * @param spec
	 * @param id
	 * @return
	 */
	private String getRemarkEndpointURL(ReportSpecification spec, long id) {
		String remarkEndpoint = "/gpi/report/remarks?donorId=%s&donorType=%s&from=%s&to=%s";

		String donorType = isDonorAgency ? GPIReportConstants.HIERARCHY_DONOR_AGENCY
				: GPIReportConstants.HIERARCHY_DONOR_GROUP;

		FilterRule dateRule = GPIReportUtils.getDateFilterRule(spec);

		return String.format(remarkEndpoint, id, donorType, dateRule.min, dateRule.max);
	}

	private Map<String, ReportCell> getEmptyGPIRow(ReportSpecification spec) {

		DecimalFormat decimalFormatter = ReportsUtil.getDecimalFormatOrDefault(spec);
		AmountCell emptyCell = new AmountCell(BigDecimal.ZERO, decimalFormatter.format(BigDecimal.ZERO));
		Map<String, ReportCell> initMap = new HashMap<>();

		ON_BUDGET_MEASURES.forEach(m -> initMap.put(m, emptyCell));

		return initMap;
	}

	/**
	 * build the contents of the report
	 * 
	 * @param generatedReport
	 * @return
	 */
	@Override
	protected Map<GPIReportOutputColumn, String> getReportSummary(GeneratedReport generatedReport) {

		Map<GPIReportOutputColumn, String> columns = new HashMap<>();

		List<ReportArea> onBudgetAreas = new ArrayList<>();

		if (generatedReport.reportContents.getChildren() != null) {
			onBudgetAreas = generatedReport.reportContents.getChildren().stream().filter(r -> r.getChildren() != null)
					.flatMap(r -> r.getChildren().stream()).collect(Collectors.toList()).stream()
					.filter(budgetArea -> isOnBudget(budgetArea))
					.collect(Collectors.toList());
		}

		// get the sum of actual disbursements for on-budget projects
		BigDecimal actDisbSum = onBudgetAreas.stream()
				.flatMap(budgetArea -> budgetArea.getContents().entrySet().stream())
				.filter(entry -> isTotalMeasureColumn(MeasureConstants.ACTUAL_DISBURSEMENTS, entry.getKey()))
				.map(entry -> entry.getValue()).filter(rc -> rc != null)
				.map(rc -> new BigDecimal(((AmountCell) rc).extractValue()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// get the sum of planned disbursements for on-budget projects
		BigDecimal planDisbSum = onBudgetAreas.stream()
				.flatMap(budgetArea -> budgetArea.getContents().entrySet().stream())
				.filter(entry -> isTotalMeasureColumn(MeasureConstants.PLANNED_DISBURSEMENTS, entry.getKey()))
				.map(entry -> entry.getValue()).filter(rc -> rc != null)
				.map(rc -> new BigDecimal(((AmountCell) rc).extractValue()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		columns.put(new GPIReportOutputColumn(MeasureConstants.DISBURSED_AS_SCHEDULED),
				formatAmount(generatedReport, calculateDisbursedAsScheduled(actDisbSum, planDisbSum)) + "%");
		columns.put(new GPIReportOutputColumn(MeasureConstants.OVER_DISBURSED),
				formatAmount(generatedReport, calculateOverDisbursed(actDisbSum, planDisbSum)) + "%");

		return columns;
	}

	private boolean isOnBudget(ReportArea budgetArea) {
		boolean match = budgetArea.getContents().entrySet().stream()
				.anyMatch(e -> e.getKey().originalColumnName.equals(ColumnConstants.ON_OFF_TREASURY_BUDGET)
						&& (String.valueOf(e.getValue().value)).equals(ACTIVITY_BUDGET_ON));

		return match;
	}

	private boolean hasExecutingAgency(ReportArea execArea) {
		boolean match = execArea.getContents().entrySet().stream()
				.anyMatch(e -> e.getKey().originalColumnName.equals(ColumnConstants.HAS_EXECUTING_AGENCY)
						&& (String.valueOf(e.getValue().value)).equals(YES_VALUE));

		return match;
	}

	private boolean isTotalMeasureColumn(String columnName, ReportOutputColumn roc) {
		return columnName.equals(roc.originalColumnName)
				&& NiReportsEngine.TOTALS_COLUMN_NAME.equals(roc.parentColumn.originalColumnName);
	}

	/**
	 * Calculate % disbursed as scheduled (Actual Disbursements/Planned
	 * Disbursements * 100)
	 * 
	 * @param actual
	 * @param planned
	 * @return
	 */
	public BigDecimal calculateDisbursedAsScheduled(BigDecimal actual, BigDecimal planned) {
		BigDecimal result = BigDecimal.ZERO;
		if (actual == null || planned == null || (!actual.equals(BigDecimal.ZERO) && planned.equals(BigDecimal.ZERO))) {
			result = BigDecimal.ZERO;
		}

		if (actual.compareTo(planned) >= 0) {
			result = BigDecimal.ONE;
		} else {
			result = actual.divide(planned, NiFormula.DIVISION_MC);
		}

		return result.multiply(new BigDecimal(100)).setScale(0, RoundingMode.UP);
	}

	/**
	 * Calculate % over disbursed ((Actual Disbursements - Planned
	 * Disbursements)/Actual Disbursements * 100)
	 * 
	 * @param actual
	 * @param planned
	 * @return
	 */
	public BigDecimal calculateOverDisbursed(BigDecimal actual, BigDecimal planned) {
		if (actual == null || planned == null || actual.equals(BigDecimal.ZERO) || planned.equals(BigDecimal.ZERO)
				|| actual.compareTo(planned) < 0) {
			return BigDecimal.ZERO;
		}

		return actual.subtract(planned).divide(actual, NiFormula.DIVISION_MC).multiply(new BigDecimal(100)).setScale(0,
				RoundingMode.UP);
	}

}
