package org.dgfoundation.amp.gpi.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.jdbc.Work;

/**
 * A utility class to transform a GeneratedReport to GPI Report 1 Output 1
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport1Output1Builder extends GPIReportOutputBuilder {

	public static final String EXTENT_OF_USE_OF_COUNTY_RESULT = "Result";
	public static final String EXTENT_OF_USE_OF_GOV_SOURCES = "M&E";
	
	public Map<Long, String> orgToGroupName = new HashMap<>();

	public GPIReport1Output1Builder() {
		addColumn(new GPIReportOutputColumn(ColumnConstants.PROJECT_TITLE));
		addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q1, MeasureConstants.ACTUAL_COMMITMENTS));
		addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q2, ColumnConstants.ACTUAL_APPROVAL_DATE));
		addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q3, ColumnConstants.FINANCING_INSTRUMENT));
		addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q4, ColumnConstants.IMPLEMENTING_AGENCY));
		addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q5, ColumnConstants.PRIMARY_SECTOR));
		addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q6));
		addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q6_DESCRIPTION));
		addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q7));
		addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q8));
		addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q9));
		addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q10));
		addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q10_DESCRIPTION));
		addColumn(new GPIReportOutputColumn(EXTENT_OF_USE_OF_COUNTY_RESULT));
		addColumn(new GPIReportOutputColumn(EXTENT_OF_USE_OF_GOV_SOURCES));
		addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_REMARK));
		addColumn(new GPIReportOutputColumn(ColumnConstants.ACTIVITY_ID));
		addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_ID));
		
		orgToGroupName = fetchOrgToGroupName();
	}

	/**
	 * build the headers of the report
	 * 
	 * @param generatedReport
	 * @return
	 */
	@Override
	protected List<GPIReportOutputColumn> buildHeaders(GeneratedReport generatedReport) {
		List<GPIReportOutputColumn> headers = new ArrayList<>();

		headers.add(getColumns().get(ColumnConstants.PROJECT_TITLE));
		headers.add(getColumns().get(GPIReportConstants.GPI_1_Q1));
		headers.add(getColumns().get(GPIReportConstants.GPI_1_Q2));
		headers.add(getColumns().get(GPIReportConstants.GPI_1_Q3));
		headers.add(getColumns().get(GPIReportConstants.GPI_1_Q4));
		headers.add(getColumns().get(GPIReportConstants.GPI_1_Q5));
		headers.add(getColumns().get(ColumnConstants.GPI_1_Q6));
		headers.add(getColumns().get(ColumnConstants.GPI_1_Q6_DESCRIPTION));
		headers.add(getColumns().get(ColumnConstants.GPI_1_Q7));
		headers.add(getColumns().get(ColumnConstants.GPI_1_Q8));
		headers.add(getColumns().get(ColumnConstants.GPI_1_Q9));
		headers.add(getColumns().get(ColumnConstants.GPI_1_Q10));
		headers.add(getColumns().get(ColumnConstants.GPI_1_Q10_DESCRIPTION));
		headers.add(getColumns().get(EXTENT_OF_USE_OF_COUNTY_RESULT));
		headers.add(getColumns().get(EXTENT_OF_USE_OF_GOV_SOURCES));

		return headers;
	}
	
	private Map<Long, String> fetchOrgToGroupName() {
		String query = "SELECT o.amp_org_id as orgId, gr.org_grp_name AS groupName "
				+ "FROM amp_organisation o "
				+ "JOIN amp_org_group gr ON o.org_grp_id = gr.amp_org_grp_id";

		Map<Long, String> orgToGroupName = new HashMap<>();
		PersistenceManager.getSession().doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				try (RsInfo rs = SQLUtils.rawRunQuery(connection, query, null)) {
					while (rs.rs.next()) {

						Long orgId = rs.rs.getLong("orgId");
						String orgGroup = rs.rs.getString("groupName");

						orgToGroupName.put(orgId, orgGroup);
					}
				}
			}
		});

		return orgToGroupName;
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
		
		List<GPIOutput1Item> gpiItems = getFilteredGPIItems(generatedReport);
		gpiItems.forEach(gpiItem -> contents.add(generateRow(gpiItem)));

		GPIReportOutputColumn approvalColumn = getColumns().get(ColumnConstants.ACTUAL_APPROVAL_DATE);
		Comparator<Map<GPIReportOutputColumn, String>> byApprovalDate = (Map<GPIReportOutputColumn, String> o1,
				Map<GPIReportOutputColumn, String> o2) -> o2.get(approvalColumn) == null ? -1 :
					o2.get(approvalColumn).compareTo(o1.get(approvalColumn));

		contents.sort(byApprovalDate);

		return contents;
	}

	/**
	 * @param generatedReport
	 * @return
	 */
	private List<GPIOutput1Item> getFilteredGPIItems(GeneratedReport generatedReport) {
		GpiReport1Output1Visitor visitor = new GpiReport1Output1Visitor();
		generatedReport.reportContents.accept(visitor);

		FilterRule donorAgencyRule = GPIReportUtils.getFilterRule(originalFormParams, ColumnConstants.DONOR_AGENCY);
		Set<String> filteredDonors = donorAgencyRule == null ? new HashSet<>()
				: donorAgencyRule.values.stream().collect(Collectors.toSet());
		
		List<GPIOutput1Item> gpiItems = visitor.getGpiItems().stream()
				.filter(g -> filteredDonors.isEmpty() || filteredDonors.contains(String.valueOf(g.getDonorId())))
				.collect(Collectors.toList());
		return gpiItems;
	}
	
	/**
	 * build the contents of the report
	 * 
	 * @param generatedReport
	 * @return
	 */
	@Override
	protected Map<GPIReportOutputColumn, String> getReportSummary(GeneratedReport generatedReport) {
		List<GPIOutput1Item> gpiItems = getFilteredGPIItems(generatedReport);
		
		return generateSummary(gpiItems);
	}
	
	/**
	 * @param gpiElement
	 * @return
	 */
	private Map<GPIReportOutputColumn, String> generateRow(GPIOutput1Item gpiElement) {
		
		Map<GPIReportOutputColumn, String> row = new HashMap<>();
		
		row.put(getColumns().get(ColumnConstants.PROJECT_TITLE), gpiElement.getProjectTitle());
		row.put(getColumns().get(GPIReportConstants.GPI_1_Q1), gpiElement.getApprovalDateAsString());
		row.put(getColumns().get(GPIReportConstants.GPI_1_Q2), gpiElement.getActCommitments().toString());
		row.put(getColumns().get(GPIReportConstants.GPI_1_Q3), 
				String.join("###", gpiElement.getFinancingInstruments().values()));
		row.put(getColumns().get(GPIReportConstants.GPI_1_Q4), 
				String.join("###", getImplAgencyWithGroupName(gpiElement.getImplementingAgencies())));
		row.put(getColumns().get(GPIReportConstants.GPI_1_Q5), 
				String.join("###", gpiElement.getPrimarySectors().values()));
		row.put(getColumns().get(ColumnConstants.GPI_1_Q6), gpiElement.getQ6() ? "Yes" : "No");
		row.put(getColumns().get(ColumnConstants.GPI_1_Q6_DESCRIPTION), gpiElement.getQ6Description());
		row.put(getColumns().get(ColumnConstants.GPI_1_Q7), gpiElement.getQ7().toString());
		row.put(getColumns().get(ColumnConstants.GPI_1_Q8), gpiElement.getQ8().toString());
		row.put(getColumns().get(ColumnConstants.GPI_1_Q9), gpiElement.getQ9().toString());
		row.put(getColumns().get(ColumnConstants.GPI_1_Q10), gpiElement.getQ10() ? "Yes" : "No");
		row.put(getColumns().get(ColumnConstants.GPI_1_Q10_DESCRIPTION), gpiElement.getQ10Description());
		row.put(getColumns().get(EXTENT_OF_USE_OF_COUNTY_RESULT), 
				getPercentage(gpiElement.getQ8(), gpiElement.getQ7()) + "%");
		row.put(getColumns().get(EXTENT_OF_USE_OF_GOV_SOURCES), 
				getPercentage(gpiElement.getQ9(), gpiElement.getQ7()) + "%");
		row.put(getColumns().get(ColumnConstants.ACTIVITY_ID), String.valueOf(gpiElement.getActivityId()));
		row.put(getColumns().get(ColumnConstants.DONOR_ID), String.valueOf(gpiElement.getDonorId()));

		return row;
	}

	/**
	 * @param gpiElement
	 * @return
	 */
	private Map<GPIReportOutputColumn, String> generateSummary(List<GPIOutput1Item> gpiElements) {

		BigDecimal cnt = BigDecimal.valueOf(gpiElements.size());
		Map<GPIReportOutputColumn, String> row = new HashMap<>();

		BigDecimal q6Cnt = getCountOfFilteredElements(gpiElements, GPIOutput1Item::getQ6);
		BigDecimal q7Sum = getSumOfFields(gpiElements, GPIOutput1Item::getQ7);
		BigDecimal q8Sum = getSumOfFields(gpiElements, GPIOutput1Item::getQ8);
		BigDecimal q9Sum = getSumOfFields(gpiElements, GPIOutput1Item::getQ9);
		BigDecimal q10Cnt = getCountOfFilteredElements(gpiElements, GPIOutput1Item::getQ10);

		row.put(getColumns().get(GPIReportConstants.GPI_1_Q1), getPercentage(q6Cnt, cnt) + "%");
		row.put(getColumns().get(GPIReportConstants.GPI_1_Q2), getPercentage(q8Sum, q7Sum) + "%");
		row.put(getColumns().get(GPIReportConstants.GPI_1_Q3), getPercentage(q9Sum, q7Sum) + "%");
		row.put(getColumns().get(GPIReportConstants.GPI_1_Q4), getPercentage(q10Cnt, cnt) + "%");
		
		row.put(getColumns().get(GPIReportConstants.COLUMN_REMARK), getRemarkEndpointURL(gpiElements));

		return row;
	}

	/**
	 * @param cnt
	 * @param q6Cnt
	 * @return
	 */
	private BigDecimal getPercentage(BigDecimal a, BigDecimal b) {
		return a.divide(b, NiFormula.DIVISION_MC).scaleByPowerOfTen(2).setScale(0, RoundingMode.HALF_UP);
	}

	/**
	 * @param items
	 * @return
	 */
	private BigDecimal getCountOfFilteredElements(List<GPIOutput1Item> items, Predicate<GPIOutput1Item> p) {
		return BigDecimal.valueOf(items.stream().filter(p).count());
	}

	/**
	 * @param items
	 * @return
	 */
	private BigDecimal getSumOfFields(List<GPIOutput1Item> items, Function<GPIOutput1Item, BigDecimal> func) {
		return items.stream().map(func).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	private List<String> getImplAgencyWithGroupName(Map<Long, String> implementingAgencies) {
		List<String> implAgencyGroup = new ArrayList<>();
		
		implementingAgencies.entrySet().forEach(e -> {
			implAgencyGroup.add(String.format("%s - %s", e.getValue(), orgToGroupName.get(e.getKey())));
		});
		
		return implAgencyGroup;
	}
	
	/**
	 * Generate the remark endpoint url
	 * 
	 * @param spec
	 * @param id
	 * @return
	 */
	private String getRemarkEndpointURL(List<GPIOutput1Item> gpiElements) {
		String remarkEndpoint = GPIReportConstants.GPI_REMARK_ENDPOINT + "?donorId=%s&donorType=%s&from=%s&to=%s";

		String donorType = GPIReportConstants.HIERARCHY_DONOR_AGENCY;

		List<String> donorIds = gpiElements.stream().map(gpiItem -> Long.toString(gpiItem.getDonorId()))
				.collect(Collectors.toList());
		String ids = String.join(",", donorIds);

		FilterRule approvalDateRule = GPIReportUtils.getFilterRule(originalFormParams, 
				ColumnConstants.ACTUAL_APPROVAL_DATE);
		
		String min = approvalDateRule == null ? "0" : approvalDateRule.min != null ? approvalDateRule.min : "0";
		String max = approvalDateRule == null ? "0" : approvalDateRule.max != null ? approvalDateRule.max : "0";

		return String.format(remarkEndpoint, ids, donorType, min, max);
	}
}
