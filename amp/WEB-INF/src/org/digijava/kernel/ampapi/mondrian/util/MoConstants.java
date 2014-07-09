package org.digijava.kernel.ampapi.mondrian.util;
/**
 * 
 * @author Diego Dimunzio
 * @since March 12 2009 - AMP 1.13
 */
public final class MoConstants {

	//AMP Cubes
	public static String CONNECTION_DS = "jdbc:mondrian:Datasource=java:comp/env/ampDS";
	public static final String SCHEMA_PATH = "WEB-INF/queries/AMP.xml".replace("/", System.getProperty("file.separator"));
	public static final String FUNDING_CUBE_NAME = "Donor Funding";
	public static final String DEFAULT_CUBE_NAME = FUNDING_CUBE_NAME;
	
	//Query
	public static final String QUERY_NAME_KEY = "QUERY_NAME_KEY";
	public static final String DEFAULT_QUERY_NAME = "Anonymous";
	public static final String COLUMNS = "COLUMNS";
	public static final String ROWS = "ROWS";
	public static final String FILTER = "FILTER";
	public static final String MEASURE = "Measure";
	public static final String MEASURES = "Measures";
	public static final String MEMBERS = "Members";
	public static final String CURRENT_MEMBER = "CurrentMember";
	public static final String FUNC_CROSS_JOIN = "CrossJoin";
	public static final String FUNC_UNION = "Union";
	public static final String FUNC_ORDER = "Order";
	public static final String FUNC_FILTER = "Filter";
	
	//Activity
	public static String AMP_ACTIVITY_TABLE = "(\\bamp_activity\\b)";
	public static String CACHED_ACTIVITY_TABLE = "cached_amp_activity";
	//Dimensions
	public static String PRIMARY_SECTOR = "Primary Sector";
	public static String SECONDARY_SECTOR = "Secondary Sector";
	public static String ACTIVITY = "Activity";
	public static String DONOR_DATES = "Donor Dates";
	public static String REGIONS = "Regions";
	public static String PRIMARY_PROGRAMS = "Primary Programs";
	public static String SECONDARY_PROGRAMS = "Secondary Program";
	public static String STATUS = "Status";
	public static String DONOR = "Donor";
	public static String DONOR_TYPES = "Donor Types";
	public static String DONOR_GROUP = "Donor Group";
	public static String FINANCING_INTRUMENT= "Financing Instrument";
	public static  String TERMS_OF_ASSISTANCE= "Terms of Assistance";
	public static  String NATIONAL_PROGRAM= "National Program";
	public static String SECTORS= "Sectors";
	public static String SUB_SECTORS = "Primary Sector Sub-Sectors";
	public static String SUB_SUB_SECTORS = "Primary Sector Sub-Sub-Sectors";
	public static String SEC_SUB_SECTORS = "Secondary Sector Sub-Sectors";
	public static String SEC_SUB_SUB_SECTORS = "Secondary Sector Sub-Sub-Sectors";
	public static String CURRENCY = "currency";
	public static String YEAR = "Year";
	//Attributes
	public static final String ATTR_ACTIVITY_NAME = "Activity Title";
	public static final String ATTR_ACTIVITY_AMP_ID = "AMP ID";
	public static final String ATTR_STATUS_NAME = "Status";
	public static final String ATTR_PRIMARY_SECTOR_NAME = "Primary Sector";
	public static final String ATTR_SECONDARY_SECTOR_NAME = "Secondary Sector";
	public static final String ATTR_DONOR_TYPES_NAME = "DonorType";
	public static final String ATTR_DONOR_GROUP_NAME = "DonorGroup";
	
	public static String ALL_PRIMARY_SECTOR = "All Primary Sectors";
	public static String ALL_SECONDARY_SECTOR = "All Secondary Sectors";
	public static String ALL_ACTIVITIES = "All Activities";
	public static String ALL_PERIODS = "All Periods";
	public static String ALL_REGIONS = "All Regions";
	public static String ALL_PROGRAMS = "All Programs";
	public static String ALL_STATUS = "All Status";
	public static String ALL_DONOR = "All Donors";
	public static String ALL_DONOR_TYPES = "All Donors Types";
	public static String ALL_DONOR_GROUP = "All Donors Group";
	public static String ALL_FINANCING_INTRUMENT= "All Financing Instruments";
	public static String ALL_TERMS_OF_ASSISTANCE= "All Terms of Assistance";
	public static String ALL_SUB_SECTORS = "All Sub-Sectors";
	public static String ALL_SUB_SUB_SECTORS = "All Sub-Sub-Sectors";
	public static String ALL_CURRENCIES = "All Currencies";
	
	//Measures
	public static String RAW_ACTUAL_COMMITMENTS = "Raw Actual Commitments";
	public static String RAW_ACTUAL_DISBURSEMENTS = "Raw Actual Disbursements";
	public static String RAW_ACTUAL_EXPENDITURES = "Raw Actual Expenditures";
	public static String RAW_PLANNED_COMMITMENTS = "Raw Planned Commitments";
	public static String RAW_PLANNED_DISBURSEMENTS = "Raw Planned Disbursements";
	public static String RAW_PLANNED_EXPENDITURES = "Raw Planned Expenditures";
	public static String ACTUAL_COMMITMENTS = "Actual Commitments";
	public static String ACTUAL_DISBURSEMENTS = "Actual Disbursements";
	public static String ACTUAL_EXPENDITURES = "Actual Expenditures";
	public static String PLANNED_COMMITMENTS = "Planned Commitments";
	public static String PLANNED_DISBURSEMENTS = "Planned Disbursements";
	public static String PLANNED_EXPENDITURES = "Planned Expenditures";
	public static String ACTIVITY_COUNT = "Activity Count";
	
	
	//Pledges Constant
	public static String PLEDGES_MEASURE = "Pledges Total";
	public static String PLEDGE_TITTLE = "Tilte";
	public static String PLEDGE_ALL_TITTLE = "All Titles";
	public static String PLEDGE_TYPE_OF_ASSINETANCE = "Type of Assistance";
	public static String PLEDGE_ALL_TYPE_OF_ASSINETANCE = "All Type of Assistance";
	public static String PLEDGE_AID_MODALITY = "Aid Modality";
	public static String PLEDGE_ALL_AID_MODALITY = "All Aid Modality";
	public static String PLEDGE_PLEDGES_DATES = "Pledges Dates";
	public static String PLEDGE_ALL_PLEDGES_DATES = "All Pledges Dates";
	public static String PLEDGE_ALL_PLEDGES_TYPES = "All Pledges Types";
	public static String PLEDGE_PLEDGES_TYPES = "Pledges Types";
	public static String PLEDGE_PLEDGES_CONTACT_NAME = "Contact Name";
	public static String PLEDGE_PLEDGES_CONTACT_EMAIL = "Contact Email";
	public static String PLEDGE_PLEDGES_COMMITMENTS = "Pledges Actual Commitments";
	public static String PLEDGE_PLEDGES_DISBURSEMENTS = "Pledges Actual Disbursements";
	public static String PLEDGE_PLEDGES_COMMITMENTS_GAP = "Commitment Gap";
	
	//Months
	public static String MONTH_JANUARY = "january";
	public static String MONTH_FEBRUARY = "february";
	public static String MONTH_MARCH = "march";
	public static String MONTH_APRIL = "april";
	public static String MONTH_MAY = "may";
	public static String MONTH_JUNE = "june";
	public static String MONTH_JULY = "july";
	public static String MONTH_AGOUST = "august";
	public static String MONTH_SEPTEMBER = "september";
	public static String MONTH_OCTOBER = "october";
	public static String MONTH_NOVEMBER = "november";
	public static String MONTH_DECEMBER = "december";

}
