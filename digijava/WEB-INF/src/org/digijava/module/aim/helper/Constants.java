package org.digijava.module.aim.helper ;

public class Constants
{
	public static final int COMMITMENT = 0 ;
	public static final int DISBURSEMENT = 1 ;
	public static final int EXPENDITURE = 2 ;
	public static final int PLANNED = 0 ;
	public static final int ACTUAL = 1 ;
	public static final int FROM_YEAR = 1983 ;
	public static final int TO_YEAR = 2005 ;
	public static final String DONOR = "DN" ;
	public static final String MOFED = "MA" ;
	public static final String DEF_DNR_PERSPECTIVE = "DONOR" ;
	public static final String DEF_MFD_PERSPECTIVE = "MOFED" ;
	public static final String DISCREPANCY = "DI";
	public static final Long MOFED_ORG_ID= new Long(12);
	public static final String DEFAULT_CURRENCY = "USD" ;
	public static final Long ETH_FY = new Long(1) ;
	public static final Long ETH_CAL = new Long(5) ;
	public static final Long GREGORIAN = new Long(4) ;
	public static final double USD = 1.0 ;
	public static final String ACTIVE_TAB_COLOR = "white" ;
	public static final String INACTIVE_TAB_COLOR = "aqua" ;
	public static final String ACTIVE_MAIN_TAB_CLASS = "sub-nav-selected" ;
	public static final String INACTIVE_MAIN_TAB_CLASS = "sub-nav" ;
	public static final String ACTIVE_SUB_TAB_CLASS = "sub-nav2-selected" ;
	public static final String INACTIVE_SUB_TAB_CLASS = "sub-nav2" ;
	public static final String REPORTING_AGENCY = "RA" ;
	public static final String FUNDING_AGENCY = "DN" ;
	public static final String IMPLEMENTING_AGENCY = "IA" ;
	public static final String EXECUTING_AGENCY = "EA";
	public static final String CONTRACTOR = "CT";
	public static final String RELATED_INSTITUTIONS = "RL" ;
	public static final int NUM_RECORDS = 10 ;
	public static final String LOAN = "L" ;
	public static final String GRANT = "G" ;
	public static final Long DESKTOP = new Long(1) ;
	public static final Long CALENDAR = new Long(1) ;
	public static final Long CURRENCY = new Long(2) ;
	public static final Long DONORS = new Long(3) ;
	public static final Long REGION = new Long(4) ;
	public static final Long SECTOR = new Long(5) ;
	public static final Long STATUS = new Long(6) ;
	public static final Long YEAR_RANGE = new Long(7) ;
	public static final Long PERSPECTIVE = new Long(8) ;
	public static final Long FINANCING_INSTRUMENT = new Long(9) ;
	public static final Long ACTUAL_PLANNED = new Long(10) ;
	public static final Long STARTDATE_CLOSEDATE = new Long(11) ;
// Report Pages Constants Set
	public static final Long QUARTER_PIPELINE = new Long(9);
	public static final Long QUARTERLYBYSECTOR = new Long(12);
	public static final Long QUARTERLYBYPROJECT = new Long(11);
	public static final Long QUARTERLYMULTIDONOR = new Long(10);
	public static final Long TREND = new Long(8);
	public static final Long PIPELINE = new Long(7);
	public static final Long DATERANGE = new Long(6);
	public static final Long SECTORBYPROJECT = new Long(5);
	public static final Long MULTILATERALDONOR = new Long(3);
	public static final Long PROJECTBYDONOR = new Long(4);
	public static final Long PHYSICALCOMPONENT = new Long(13);
//	public static final Long YEAR_RANGE = new Long(1) ;
	public static final String DIRECT_BUDGET_SUPPORT = "1" ;
	public static final String PROGRAM_SUPPORT = "6" ;
	public static final String PROJECT_SUPPORT = "3" ;
	public static final String OTHER_AID = "4" ;
	public static final Long STATUS_PLANNED = new Long(5) ;
	
	public static final Integer ORIGINAL = new Integer(0);
	public static final Integer REVISED = new Integer(1);	
	public static final Integer CURRENT = new Integer(2);
	
	public static final String COUNTRY_ISO = "et";
	public static final String COUNTRY = "Ethiopia";
	
	public static final String CURRENCY_RATE_DEAFULT_START_DATE = "25/04/2005";
	public static final String CURRENCY_RATE_DEAFULT_END_DATE = "01/05/2005";
	
	public static final int FROM_YEAR_RANGE = 22;
	public static final int TO_YEAR_RANGE = 5;

	public static final Long STATUS_NAME = new Long(1) ;	
	public static final Long DONOR_NAME = new Long(2) ;
	public static final Long ACTUAL_START_DATE = new Long(3) ;
	public static final Long ACTIVITY_NAME = new Long(4) ;
	public static final Long TERM_ASSIST_NAME = new Long(5) ;
	public static final Long LEVEL_NAME = new Long(6) ;
	public static final Long ACTUAL_COMPLETION_DATE = new Long(7) ;
	public static final Long SECTOR_NAME = new Long(8) ;
	public static final Long REGION_NAME = new Long(9) ;
	public static final Long FUNDING_INSTRUMENT = new Long(10) ;
	public static final Long OBJECTIVE = new Long(11) ;
	public static final Long AMP_ID = new Long(12) ;
	public static final Long CONTACT_NAME = new Long(13) ;
	public static final Long DESCRIPTION = new Long(14) ;
	public static final Long TOTAL_COMMITMENT = new Long(15) ;
	public static final Long TOTAL_DISBURSEMENT = new Long(16) ;
	//humbly added by Mihai
	public static final Long COMPONENT_NAME = new Long(17);
	
	
	public static final String REGIONAL_FUNDING_PAGE_CODE = "RFS";
	public static final String CALENDAR_FILTER = "Calendar";
	public static final String CURRENCY_FILTER = "Currency";

	public static final String ANNUAL = "A" ;
	public static final String QUARTERLY = "Q" ;
	
	public static final String APPROVED_STATUS = "approved";
	
	
	
	//humbly added by Mihai
	public static final Long DONOR_FUNDING = new Long(1);
	public static final Long COMPONENT_FUNDING = new Long(2);
	public static final Long REGION_FUNDING = new Long(3);
	
	public static final String SESSION_LIST	= "sessionList";
	public static final String EDIT_ACT_LIST = "editActivityList";
	public static final String USER_ACT_LIST = "userActivityList";
	public static final String TS_ACT_LIST = "timestampActivityList";
	
	public static final long MAX_TIME_LIMIT = 3600000;
	
	public static final String ME_IND_VAL_BASE_ID = "base";
	public static final String ME_IND_VAL_ACTUAL_ID = "actual";
	public static final String ME_IND_VAL_TARGET_ID = "target";
	
	public static final String ACTIVITY_PERFORMANCE_CHART_TITLE = "Activity - Performance";
	public static final String ACTIVITY_RISK_CHART_TITLE = "Activity - Risk";
	public static final String PORTFOLIO_PERFORMANCE_CHART_TITLE = "Portfolio - Performance";
	public static final String PORTFOLIO_RISK_CHART_TITLE = "Portfolio - Risk";	
	
	
	public static final int CHART_HEIGHT = 350;
	public static final int CHART_WIDTH = 400;
	
	public static final int PORTFOLIO_CHART_HEIGHT = 400;
	public static final int PORFOLIO_CHART_WIDTH = 730;	
	
	public static final String ACCESS_TYPE_TEAM = "Team";
	public static final String ACCESS_TYPE_MNGMT = "Management";
}	
