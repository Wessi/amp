/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportFiltersImpl;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.ReportUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Test Reports generation via Reports API provided by {@link org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator MondrianReportGenerator}
 * @author Nadejda Mandrescu
 */
public class MondrianReportsTests extends AmpTestCase {
	
	private MondrianReportsTests(String name) {
		super(name);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(MondrianReportsTests.class.getName());
		suite.addTest(new MondrianReportsTests("testNoTotals"));
		suite.addTest(new MondrianReportsTests("testTotals"));
		suite.addTest(new MondrianReportsTests("testColumnSortingNoTotals"));
		suite.addTest(new MondrianReportsTests("testColumnMeasureSortingTotals"));
		suite.addTest(new MondrianReportsTests("testSortingByTuplesTotals"));
		suite.addTest(new MondrianReportsTests("testMultipleDateFilters"));
		suite.addTest(new MondrianReportsTests("testAmpReportToReportSpecification"));
		suite.addTest(new MondrianReportsTests("testHeavyQuery"));
		
		return suite;
	}
	
	public void testNoTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testNoTotals", false);
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		generateAndValidate(spec, false);
	}
	
	public void testTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testTotals", true);
		generateAndValidate(spec, true);
	}
	
	public void testColumnSortingNoTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testColumnSortingNoTotals", false);
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL), true)); //ascending
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ALL), false)); //descending
		generateAndValidate(spec, true);
	}
	
	public void testColumnMeasureSortingTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testColumnMeasureSortingTotals", true);
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL), true)); //ascending
		spec.addSorter(new SortingInfo(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL), false)); //descending
		generateAndValidate(spec, true);
	}

	public void testSortingByTuplesTotals() {
		ReportSpecificationImpl spec = getDefaultSpec("testSortingByTuplesTotals", true);
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_QUARTERLY);
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL), true)); //ascending
		spec.addSorter(new SortingInfo("2013", "Q2", new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL), false)); //descending
		generateAndValidate(spec, true);
	}
	
	public void testMultipleDateFilters() {
		ReportSpecificationImpl spec = getDefaultSpec("testMultipleDateFilters", true);
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_QUARTERLY);
		ReportFiltersImpl filters = new ReportFiltersImpl();
		filters.addFilterRule(new ReportElement(ElementType.DATE), new FilterRule("2010-01-01", "2011-09-15", true, true, false));
		filters.addFilterRule(new ReportElement(ElementType.YEAR), new FilterRule(Arrays.asList("2013", "2014"), true, false));
		spec.setFilters(filters);
		generateAndValidate(spec, true);
	}

	private ReportSpecificationImpl getDefaultSpec(String name, boolean doTotals) {
		ReportSpecificationImpl spec = new ReportSpecificationImpl(name);
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.setCalculateColumnTotals(doTotals);
		spec.setCalculateRowTotals(doTotals);
		return spec;
	}
	public void testAmpReportToReportSpecification1() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		ServletRequestAttributes attr = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attr);
		HttpServletRequest test = TLSUtils.getRequest();
		System.out.println(test);
	}
	
	public void testAmpReportToReportSpecification() {
		//AmpReports report = (AmpReports) PersistenceManager.getSession().get(AmpReports.class, 1018L);//id is from Moldova DB, TODO: update for tests db 
		AmpReports report = ReportTestingUtils.loadReportByName("NadiaMondrianTest");

		org.apache.struts.mock.MockHttpServletRequest mockRequest = new org.apache.struts.mock.MockHttpServletRequest(new org.apache.struts.mock.MockHttpSession());
		mockRequest.setAttribute("ampReportId", report.getId().toString());
		TLSUtils.populate(mockRequest);
		ReportContextData.createWithId(report.getId().toString(), true);

		ReportSpecificationImpl spec = ReportUtils.toReportSpecification(report);
		
		generateAndValidate(spec, true);
	}
	
	public void testHeavyQuery() {
		long start = System.currentTimeMillis();
		ReportSpecificationImpl spec = new ReportSpecificationImpl("testHeavyQuery");
		spec.addColumn(new ReportColumn(ColumnConstants.REGION, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
		generateAndValidate(spec, true);
		long end = System.currentTimeMillis();
		System.out.println("Full time (with printing) = " + (end-start) + "(ms)");
	}
	
	private void generateAndValidate(ReportSpecification spec, boolean print) {
		String err = null;
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, print);
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
			System.out.println("Generation duration = " + report.generationTime + "(ms)");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			err = e.getMessage();
		}
		//basic tests, todo more
		assertNull(err);
		assertNotNull(report);
		assertNotNull(report.reportContents);
	}
}
