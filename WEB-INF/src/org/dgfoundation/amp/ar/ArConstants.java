/**
 * ArConstants.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 15, 2006
 *
 */
public final class ArConstants {
	public final static MetaInfo []prefixes=new MetaInfo[] {
		//PLEASE KEEP THE SAME ORDER IN prefixes AND suffixes !!
		
		new MetaInfo(GenericViews.HTML,"/repository/aim/view/ar/html/"),
		new MetaInfo(GenericViews.XLS,"org.dgfoundation.amp.ar.view.xls."),
		new MetaInfo(GenericViews.PDF,"org.dgfoundation.amp.ar.view.pdf."),
		new MetaInfo(GenericViews.PRINT,"/repository/aim/view/ar/print/"),
		new MetaInfo(GenericViews.TREE,"/repository/aim/view/ar/tree/"),
		new MetaInfo(GenericViews.FOLDABLE,"/repository/aim/view/ar/foldable/"),		
		};

	public final static MetaInfo []suffixes=new MetaInfo[] {
		new MetaInfo(GenericViews.HTML,".jsp"),
		new MetaInfo(GenericViews.XLS,"XLS"),
		new MetaInfo(GenericViews.PDF,"PDF"),
		new MetaInfo(GenericViews.PRINT,".jsp"),
		new MetaInfo(GenericViews.TREE,".jsp"),
		new MetaInfo(GenericViews.FOLDABLE,".jsp"),		
		};

	//metainfo categs:
	public final static String ADJUSTMENT_TYPE="Adjustment Type";
	public final static String TRANSACTION_TYPE="Transaction Type";
	public final static String TRANSACTION_DATE="Transaction Date";
	
	public final static String FUNDING_TYPE="Funding Type";
	public final static String TERMS_OF_ASSISTANCE="Type Of Assistance";
	public final static String FINANCING_INSTRUMENT="Financing Instrument";	
	public final static String YEAR="Year";
	public final static String FISCAL_Y="FISCAL_Y";
	public final static String FISCAL_M="FISCAL_M";
	public final static String QUARTER="Quarter";
	public final static String MONTH="Month";
	public final static String PROPOSED_COST="Proposed Cost";
	public final static String SOURCE_FUNDING="Source Funding";
	
	public final static String PERSPECTIVE="Perspective";

	
	public final static String DONOR="Donor Agency";
	public final static String DONOR_GROUP="Donor Group";
	public final static String DONOR_TYPE_COL="Donor Type";
	
	/**
	 * @deprecated use COLUMN_COUNTRY if it's related to the column
	 */
	public final static String COUNTRY="Country";
	/**
	 * @deprecated use COLUMN_REGION if it's related to the column
	 */
	public final static String REGION="Region";
	/**
	 * @deprecated use COLUMN_DISTRICT if it's related to the column
	 */
	public final static String DISTRICT="District";
	/**
	 * @deprecated use COLUMN_ZONE if it's related to the column
	 */
	public final static String ZONE="Zone";
	
	public final static String COMPONENT="Component Type";

	public final static String UNALLOCATED="Unallocated";
	
	//report type
	public final static int DONOR_TYPE=1;
	public final static int COMPONENT_TYPE=2;
	public final static int REGIONAL_TYPE=3;
	public final static int CONTRIBUTION_TYPE=4;
	public final static int PLEDGES_TYPE=5;
	
	//metainfo values:
	public final static String COMMITMENT="Commitments";
	public final static String DISBURSEMENT="Disbursements";
	public final static String EXPENDITURE="Expenditures";
	public final static String PLEDGES_COMMITMENT="Pledges Commitments";
	public final static String PLEDGES_DISBURSEMENT="Pledges Disbursements";
	public final static String PLEDGE="Pledge";
	//public final static String PLEDGES_TOTAL_PLEDGED="Total Pledged";
	
    
	//Computed Field Constants
    //
    public final static String MAX_ACTUAL_COMMITMENT="MAX_ACTUAL_COMMITMENT";
    public final static String MAX_ACTUAL_DISBURSEMENT="MAX_ACTUAL_DISBURSEMENT";
    
    public final static String MAX_PLANNED_COMMITMENT="MAX_PLANNED_COMMITMENT";
    public final static String MAX_PLANNED_DISBURSEMENT="MAX_PLANNED_DISBURSEMENT";
    
    public final static String MIN_ACTUAL_COMMITMENT="MIN_ACTUAL_COMMITMENT";
    public final static String MIN_ACTUAL_DISBURSEMENT="MIN_ACTUAL_DISBURSEMENT";
    
    public final static String MIN_PLANNED_COMMITMENT="MIN_PLANNED_COMMITMENT";
    public final static String MIN_PLANNED_DISBURSEMENT="MIN_PLANNED_DISBURSEMENT";
    public final static String COUNT_PROJECTS="COUNT_PROJECTS";
    
    //funding variable  names
    public final static String ACTUAL_COMMITMENT="ACTUAL_COMMITMENT";
    public final static String ACTUAL_DISBURSEMENT="ACTUAL_DISBURSEMENT";
    public final static String ACTUAL_EXPENDITURET="ACTUAL_EXPENDITURET";
    public final static String PLANNED_COMMITMENT="PLANNED_COMMITMENT";
    public final static String PLANNED_DISBURSEMENT="PLANNED_DISBURSEMENT";
    public final static String PLANNED_EXPENDITURE="PLANNED_EXPENDITURE";
    
    
    public final static String TOTAL_ACTUAL_COMMITMENT="TOTAL_ACTUAL_COMMITMENT";
    public final static String TOTAL_ACTUAL_DISBURSEMENT="TOTAL_ACTUAL_DISBURSEMENT";

    public final static String TOTAL_PLANNED_COMMITMENT="TOTAL_PLANNED_COMMITMENT";
    public final static String TOTAL_PLANNED_DISBURSEMENT="TOTAL_PLANNED_DISBURSEMENT";
    
    public final static String ACTUAL_COMMITMENT_FILTERED="ACTUAL_COMMITMENT_FILTERED";
    public final static String ACTUAL_DISBURSEMENT_FILTERED="ACTUAL_DISBURSEMENT_FILTERED";
    public final static String PLANNED_COMMITMENT_FILTERED="PLANNED_COMMITMENT_FILTERED";
    public final static String PLANNED_DISBURSEMENT_FILTERED="PLANNED_DISBURSEMENT_FILTERED";
  
    
    public final static String ACTUAL_COMMITMENT_COUNT="ACTUAL_COMMITMENT_COUNT";
    public final static String ACTUAL_DISBURSEMENT_COUNT="ACTUAL_DISBURSEMENT_COUNT";
    
    public final static String PLANNED_COMMITMENT_COUNT="PLANNED_COMMITMENT_COUNT";
    public final static String PLANNED_DISBURSEMENT_COUNT="PLANNED_DISBURSEMENT_COUNT";
    
    public final static String PLEDGED_TOTAL="PLEDGE_TOTAL";
    public final static String TOTAL_PLEDGE_ACTIVITY_ACTUAL_COMMITMENT = "TOTAL_PLEDGE_ACTIVITY_ACTUAL_COMMITMENT";
    
    //dates variable names
    public final static String ACTIVITY_CLOSE_DATE_VALUE = "ACTIVITY_CLOSE_DATE_VALUE";
    public final static String ACTUAL_START_DATE_VALUE = "ACTUAL_START_DATE_VALUE";
    public final static String ACTUAL_APPROVAL_DATE_VALUE = "ACTUAL_APPROVAL_DATE_VALUE";
    public final static String ACTIVITY_APPROVAL_DATE_VALUE = "ACTIVITY_APPROVAL_DATE_VALUE";
    public final static String PROPOSED_START_DATE_VALUE = "PROPOSED_START_DATE_VALUE";
    public final static String ACTUAL_COMPLETION_DATE_VALUE = "ACTUAL_COMPLETION_DATE_VALUE";
    public final static String PROPOSED_COMPLETION_DATE_VALUE = "PROPOSED_COMPLETION_DATE_VALUE";
    public final static String CURRENT_DATE_VALUE="CURRENT_DATE_VALUE";
    //end computed filds 
    
	public final static String PLANNED="Planned";
	public final static String ACTUAL="Actual";
	public final static String PIPELINE="Pipeline";
	
	//created columns
	public final static String COLUMN_TOTAL="Total Costs";
	public final static String COLUMN_CONTRIBUTION_TOTAL="Total Contributions";
	
	public final static String COLUMN_RAW_DATA="RAW DATA";
	public final static String COLUMN_FUNDING="Funding";
	public final static String COLUMN_PROPOSED_COST="Proposed Project Cost";
	public final static String COSTING_GRAND_TOTAL="Grand Total";
	
	public final static String COLUMN_ANY_SECTOR="Sector";
	public final static String COLUMN_SUB_SECTOR="Sub-Sector";
	
	public final static String COLUMN_COUNTRY="Country";
	public final static String COLUMN_REGION="Region";
	public final static String COLUMN_ZONE="Zone";
	public final static String COLUMN_DISTRICT="District";
	
	public final static String COLUMN_SECTOR_GROUP="Sector Group";
	
	public final static String COLUMN_ANY_NATPROG					= "National Planning Objectives";
	public final static String COLUMN_ANY_PRIMARYPROG			= "Primary Program";
	public final static String COLUMN_ANY_SECONDARYPROG	= "Secondary Program";
	
	//additional measures
	public final static String UNDISBURSED_BALANCE="Undisbursed Balance";
	public final static String UNCOMMITTED_BALANCE="Uncommitted Balance";
	public final static String TOTAL_COMMITMENTS="Total Commitments";
	public final static String TOTAL_PERCENTAGE_OF_TOTAL_DISBURSEMENTS="Percentage Of Total Disbursements";
	public final static String EXECUTION_RATE = "Execution Rate";
	
//maldives only:
//	public final static String SECTOR_PERCENTAGE="Sector Percentage";
	
	public final static String PERCENTAGE="Percentage";
	
	//draft in title
	public final static String DRAFT="DRAFT";
	public final static String STATUS="STATUS";
	//bolivia:
//	public final static String LOCATION_PERCENTAGE="Location Percentage";
//	public final static String COMPONENTE_PERCENTAGE="Componente Percentage";
	
//	public final static String EXECUTING_AGENCY_PERCENTAGE="Eexecuting Agency Percentage";
	
	
	//burkina
//	public final static String PROGRAM_PERCENTAGE="Program Percentage";
	
	
	
//	public final static String NPO_PERCENTAGE="National Planning Objectives Percentage";
	//hierarchysorter
	public final static String HIERARCHY_SORTER_TITLE="Title";
	
	public static final String VIEW_PROPOSED_COST="v_proposed_cost";	
	public static final String VIEW_COST="v_costs";	
	public static final String VIEW_DONOR_FUNDING="v_donor_funding";
	public static final String VIEW_COMPONENT_FUNDING="v_component_funding";
	public static final String VIEW_REGIONAL_FUNDING="v_regional_funding";
	public static final String VIEW_CONTRIBUTION_FUNDING="v_contribution_funding";
	public static final String VIEW_PLEDGES_FUNDING="v_pledges_funding_st";
	
	
	//Columns order names
	public static final String PLEDGES_COLUMNS="Pledges Columns";
	public static final String PLEDGES_CONTACTS_1 ="Pledge Contact 1";
	public static final String PLEDGES_CONTACTS_2 ="Pledge Contact 2";
	
	//reportsFilter
	
	public final static String REPORTS_FILTER="ReportsFilter";

	public final static String COMPUTE_ON_YEAR="ComputeOnYear";

	//the currency in use
	public final static String SELECTED_CURRENCY="SelectedCurrency";

	public static final String DISBURSEMENT_ORDERS = "Disbursement Orders";
	
	public static final String INITIALIZE_FILTER_FROM_DB	= "Initialize filter from db";

	public static final String COLUMN_PROJECT_TITLE						 		= "Project Title";
	public static final String COLUMN_CUMULATIVE_COMMITMENT			= "Cumulative Commitment";
	public static final String COLUMN_CUMULATIVE_DISBURSEMENT		= "Cumulative Disbursement";
	public static final String COLUMN_UNDISB_CUMULATIVE_BALANCE		= "Undisbursed Cumulative Balance";
	public static final String COLUMN_UNCOMM_CUMULATIVE_BALANCE	= "Uncommitted Cumulative Balance";

	public static final String SUM_OFF_RESULTS = "SUM_OFF_RESULTS";

	public static final String TOTAL_PRIOR_ACTUAL_DISBURSEMENT = "TOTAL_PRIOR_ACTUAL_DISBURSEMENT";
	public static final String TOTAL_ACTUAL_DISBURSEMENT_LAST_CLOSED_MONTH = "TOTAL_ACTUAL_DISBURSEMENT_LAST_CLOSED_MONTH";
	
	public final static String CUMULATED_DISBURSEMENT_SELECTED_YEAR= "CUMULATED_DISBURSEMENT_SELECTED_YEAR";
	public final static String TOTAL_PLANNED_DISBURSEMENT_SELECTED_YEAR= "TOTAL_PLANNED_DISBURSEMENT_SELECTED_YEAR";
	
	public final static String EXCHANGE_RATES_CACHE="EXCHANGE_RATES_CACHE";
	
		
}