package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.junit.Test;

public class BasicMondrianReportTests extends MondrianReportsTestCase {
	
	public BasicMondrianReportTests() {
		super("basic mondrian tests");
	}
	
	@Test
	public void testProjectTitleLanguages() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "7 181 333", "Actual Disbursements", "1 665 111")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Actual Commitments", "0", "Actual Disbursements", "660 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SSC Project 1", "Actual Commitments", "111 333", "Actual Disbursements", "555 111"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000"));
		
		runMondrianTestCase(
				this.buildSpecification("testcase EN", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_TOTALS_ONLY),						
						"en", 
						Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"),
						correctReport); 
				
		ReportAreaForTests correctReportRu = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "7 181 333", "Actual Disbursements", "1 665 111")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Вода Eth", "Actual Commitments", "0", "Actual Disbursements", "660 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Проект КЮЮ 1", "Actual Commitments", "111 333", "Actual Disbursements", "555 111"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "обещание 2", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000"));
		
		runMondrianTestCase(
				buildSpecification("testcase RU", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_TOTALS_ONLY),
				"ru", 
				Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"),
				correctReportRu); 

		//System.out.println(describeReportOutputInCode(rep));
		//System.out.println(rep.toString());
	}
	
	@Test
	public void test_AMP_18497() {
		// for running manually: open http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/32 on the AMP 2.10 testcases database
		
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Group", "", "Actual Commitments", "999 888", "Actual Disbursements", "1 301 323")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_2", "Donor Group", "American", "Actual Commitments", "999 888", "Actual Disbursements", "453 213"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Donor Group", "American, National", "Actual Commitments", "0", "Actual Disbursements", "188 110"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Donor Group", "American, Default Group, European, International, National", "Actual Commitments", "0", "Actual Disbursements", "660 000"));

		runMondrianTestCase(
				buildSpecification("AMP-18497", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_GROUP), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_TOTALS_ONLY),
				"en",
				Arrays.asList("Eth Water", "Test MTEF directed", "TAC_activity_2"),
				cor);
	}
	
	@Test
	public void test_AMP_18499() {
		// for running manually: open http://localhost:8080/aim/viewNewAdvancedReport.do~view=reset~widget=false~resetSettings=true~ampReportId=73 OR http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/73
		ReportAreaForTests cor = new ReportAreaForTests()
	    	.withContents("Project Title", "Report Totals", "Actual Commitments", "666 777")
	    	.withChildren(
	    			new ReportAreaForTests().withContents("Project Title", "ptc activity 1", "Actual Commitments", "666 777"),
	    			new ReportAreaForTests().withContents("Project Title", "Proposed Project Cost 1 - USD", "Actual Commitments", "0"),
	    			new ReportAreaForTests().withContents("Project Title", "Project with documents", "Actual Commitments", "0")
	    	);
		
		runMondrianTestCase(
				buildSpecification("AMP-18499", Arrays.asList(ColumnConstants.PROJECT_TITLE), Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), null, GroupingCriteria.GROUPING_TOTALS_ONLY),
				"en",
				Arrays.asList("Proposed Project Cost 1 - USD", "Project with documents", "ptc activity 1"),
				cor);
	}
	
	@Test
	public void test_AMP_18504() {
		// for running manually: http://localhost:8080/aim/viewNewAdvancedReport.do~view=reset~widget=false~resetSettings=true~ampReportId=24 or http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/24
		
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Agency", "", 
	    		"[2009] Actual Commitments", "100 000", "[2009] Actual Disbursements", "0", 
	    		"[2010] Actual Commitments", "0", "[2010] Actual Disbursements", "60 000", 
	    		"[2012] Actual Commitments", "25 000", "[2012] Actual Disbursements", "12 000", 
	    		"[Total Measures] Actual Commitments", "125 000", "[Total Measures] Actual Disbursements", "72 000")
	    .withChildren(
	      new ReportAreaForTests()
	      		// line below incorrect - to be overwritten manually
	          .withContents("Project Title", "date-filters-activity", "Donor Agency", "Ministry of Finance", 
	        		  "[2009] Actual Commitments", "100 000", "[2009] Actual Disbursements", "0",
	        		  "[2010] Actual Commitments", "0", "[2010] Actual Disbursements", "60 000",
	        		  "[2012] Actual Commitments", "25 000", "[2012] Actual Disbursements", "12 000", 
	        		  "[Total Measures] Actual Commitments", "125 000", "[Total Measures] Actual Disbursements", "72 000"),

          new ReportAreaForTests()
	      	// line below incorrect - to be overwritten manually
	        .withContents("Project Title", "pledged 2", "Donor Agency", "USAID", "[2009] Actual Commitments", "0", "[2010] Actual Disbursements", "0", "[2012] Actual Commitments", "0", "[2012] Actual Disbursements", "0", "[2013] Actual Commitments", "2 670 000", "[2014] Actual Commitments", "4 400 000", "[2014] Actual Disbursements", "450 000", "[Total Measures] Actual Commitments", "7 070 000", "[Total Measures] Actual Disbursements", "450 000"));
		
		runMondrianTestCase(
				buildSpecification("AMP-18504",
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY),
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
						null, GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "pledged 2"),
				cor);
	}
	
	@Test
	public void test_AMP_18509() {
		// http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/16
		// test is now testing just that the thing is not crashing or outputting malformed output 
		ReportAreaForTests cor = new ReportAreaForTests()
	    	.withContents("Project Title", "Report Totals", 
	    			"Region", "", 
	    			"AMP ID", "", 
	    			"2009-Q1-Actual Commitments", "100 000", 
	    			"2010-Q2-Actual Disbursements", "60 000", 
	    			"2012-Q3-Actual Commitments", "25 000", 
	    			"2012-Q4-Actual Disbursements", "12 000", 
	    			"2013-Q4-Actual Commitments", "2 670 000", 
	    			"2014-Q2-Actual Commitments", "4 400 000", 
	    			"2014-Q2-Actual Disbursements", "450 000", 
	    			"Total Measures-Actual Commitments", "7 195 000", 
	    			"Total Measures-Actual Disbursements", "522 000")
	    	.withChildren(
	    			new ReportAreaForTests()
	    				.withContents("Project Title", "date-filters-activity", 
	    						"Region", "Undefined",  // wrong - to be changed in the future to ""
	    						"AMP ID", "872113null", 
	    						"2009-Q1-Actual Commitments", "100 000", 
	    						"2010-Q2-Actual Disbursements", "60 000", 
	    						"2012-Q3-Actual Commitments", "25 000", 
	    						"2012-Q4-Actual Disbursements", "12 000", 
	    						"2013-Q4-Actual Commitments", "0", 
	    						"2014-Q2-Actual Commitments", "0", 
	    						"2014-Q2-Actual Disbursements", "0", 
	    						"Total Measures-Actual Commitments", "125 000", 
	    						"Total Measures-Actual Disbursements", "72 000"),
	    			new ReportAreaForTests()
	    				.withContents("Project Title", "pledged 2", 
	    						"Region", "Cahul County", 
	    						"AMP ID", "87211347", 
	    						"2009-Q1-Actual Commitments", "0", 
	    						"2010-Q2-Actual Disbursements", "0", 
	    						"2012-Q3-Actual Commitments", "0", 
	    						"2012-Q4-Actual Disbursements", "0", 
	    						"2013-Q4-Actual Commitments", "2 670 000", 
	    						"2014-Q2-Actual Commitments", "4 400 000", 
	    						"2014-Q2-Actual Disbursements", "450 000", 
	    						"Total Measures-Actual Commitments", "7 070 000", 
	    						"Total Measures-Actual Disbursements", "450 000"));
		
		runMondrianTestCase(
				buildSpecification("AMP-18509", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.AMP_ID),
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.ACTUAL_EXPENDITURES),
						null,
						GroupingCriteria.GROUPING_QUARTERLY),
				"en",
				Arrays.asList("date-filters-activity", "pledged 2"),
				cor);
	}
	
	@Test
	public void test_AMP_18530_no_hier() {
		// report with "Region" as a column, an activity without locations + one with locations
		ReportAreaForTests cor = null;
		runMondrianTestCase(
				buildSpecification("AMP-18530-no-hier",						
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
						null,
						GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);

	}
	
	@Test
	public void test_AMP_18530_hier() {
		// report with "Region" as a column, an activity without locations + one with locations
		ReportAreaForTests cor = null;
		runMondrianTestCase(
				buildSpecification("AMP-18530-hier", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						Arrays.asList(ColumnConstants.REGION), 
						GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);
	}
}
