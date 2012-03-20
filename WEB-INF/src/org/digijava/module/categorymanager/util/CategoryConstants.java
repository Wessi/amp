package org.digijava.module.categorymanager.util;


public class CategoryConstants {
	public static final String ACCHAPTER_NAME	= "A.C. Chapter";
	public static final String ACCHAPTER_KEY	= "acchapter";
	public static final String PROCUREMENT_SYSTEM_NAME		= "Procurement System";
	public static final String PROCUREMENT_SYSTEM_KEY		= "procurement_system";
	public static final String REPORTING_SYSTEM_NAME		= "Reporting System";
	public static final String REPORTING_SYSTEM_KEY			= "reporting_system";
	public static final String AUDIT_SYSTEM_NAME			= "Audit System";
	public static final String AUDIT_SYSTEM_KEY				= "audit_system";
	public static final String INSTITUTIONS_NAME			= "Institutions";
	public static final String INSTITUTIONS_KEY				= "institutions";
	public static final String ACCESSION_INSTRUMENT_NAME	= "Accession Instrument";
	public static final String ACCESSION_INSTRUMENT_KEY		= "accessioninstr";
	public static final String DOCUMENT_TYPE_NAME		= "Document type";
	public static final String DOCUMENT_TYPE_KEY		= "docType"; 
	public static final String LOGFRAME_NAME			= "Logframe";
	public static final String LOGFRAME_KEY			= "logframe";
	
	public static final String ACTIVITY_STATUS_NAME		= "Activity Status";
	public static final String ACTIVITY_STATUS_KEY			= "activity_status";
	
	public static final String PROJECT_IMPLEMENTING_UNIT_NAME = "Project Implementing Unit";
    public static final String PROJECT_IMPLEMENTING_UNIT_KEY = "project_impl_unit";
	
	public static final String IMPLEMENTATION_LEVEL_NAME			= "Implementation Level";
	public static final String IMPLEMENTATION_LEVEL_KEY			= "implementation_level";
	
	public static final String IMPLEMENTATION_LOCATION_NAME			= "Implementation Location";
	public static final String IMPLEMENTATION_LOCATION_KEY				= "implementation_location";
	
	public static final String REFERENCE_DOCS_KEY = "reference_docs";
	
	public static final String TEAM_TYPE_NAME		= "Team Type";
	public static final String TEAM_TYPE_KEY		= "team_type";
	
	public static final String TYPE_OF_ASSISTENCE_NAME		= "Type of Assistence";
	public static final String TYPE_OF_ASSISTENCE_KEY		= "type_of_assistence";
	
	public static final String PLEDGES_TYPES_NAME		= "Pledges Types";
	public static final String PLEDGES_TYPES_KEY		= "pledges_types";
	
	public static final String PLEDGES_NAMES_NAME		= "Pledges Names";
	public static final String PLEDGES_NAMES_KEY		= "pledges_names";
	
	public static final String FINANCING_INSTRUMENT_NAME		= "Financing Instrument";
	public static final String FINANCING_INSTRUMENT_KEY		= "financing_instrument";
	
	public static final String PROGRAM_TYPE_NAME				= "Program Type";
	public static final String PROGRAM_TYPE_KEY				= "program_type";
	
	public static final String DOCUMENT_LANGUAGE_NAME				= "Document Languages";
	public static final String DOCUMENT_LANGUAGE_KEY				= "document_languages";
	
	public static final String FINANCIAL_INSTRUMENT_NAME			="Financial Instrument";
	public static final String FINANCIAL_INSTRUMENT_KEY			="financial_instrument";
	
	public static final String MTEF_PROJECTION_NAME				="MTEF Projection";
	public static final String MTEF_PROJECTION_KEY					="mtef_projection";
	
	public static final String FUNDING_STATUS_NAME				="Funding Status";
	public static final String FUNDING_STATUS_KEY					="funding_status";
	
	public static final String MODE_OF_PAYMENT_NAME				="Mode of Payment";
	public static final String MODE_OF_PAYMENT_KEY					="mode_of_payment";
	
	public static final String ACTIVITY_LEVEL_NAME					="Activity Level";
	public static final String ACTIVITY_LEVEL_KEY					="activity_level";

	public static final String PROJECT_CATEGORY_NAME		= "Project Category";
	public static final String PROJECT_CATEGORY_KEY		= "project_category";
	
	public static final String DATA_EXCHANGE_NAME		= "Data Echange Category";
	public static final String DATA_EXCHANGE_KEY		= "data_exchange";
	
	public static final String ACTIVITY_BUDGET_NAME			= "Activity Budget";
	public static final String ACTIVITY_BUDGET_KEY			= "activity_budget";
	
	//--- IPA Contracting Step 13
	
	//Activity Category
	public static final String IPA_ACTIVITY_CATEGORY_NAME="IPA Activity Category";
	public static final String IPA_ACTIVITY_CATEGORY_KEY="ipa_act_cat";
	//IPA Status
	public static final String IPA_STATUS_NAME="IPA Status";
	public static final String IPA_STATUS_KEY="ipa_cat_stat";
	//Contracting Type
	public static final String IPA_TYPE_NAME="IPA Type";
	public static final String IPA_TYPE_KEY="ipa_type";
	//Activity Type
	public static final String IPA_ACTIVITY_TYPE_NAME="IPA Activity Type";
	public static final String IPA_ACTIVITY_TYPE_KEY="ipa_activity_type";
	//---

	// activity indicator risk
	public static final String EVENT_TYPE_NAME = "Event Type";
	public static final String EVENT_TYPE_KEY = "event_type";
	public static final String EVENT_COLOR_NAME = "Event Color";
	public static final String EVENT_COLOR_KEY = "event_color";

	public static final long NONE_TYPE		= 0;
	public static final long COUNTRY_TYPE	= 1;
	public static final long REGION_TYPE	= 2;
	
	public static final HardCodedCategoryValue TYPE_OF_ASSITANCE_GRANT	= 
		new CategoryConstants.HardCodedCategoryValue("type_of_assistence","Grant", true);
	
	public static final HardCodedCategoryValue LOGFRAME_OBJECTIVE	= 
		new CategoryConstants.HardCodedCategoryValue("logframe","Objective", true);
	public static final HardCodedCategoryValue LOGFRAME_PURPOSE	= 
		new CategoryConstants.HardCodedCategoryValue("logframe","Purpose", true);
	public static final HardCodedCategoryValue LOGFRAME_RESULTS	= 
		new CategoryConstants.HardCodedCategoryValue("logframe","Results", true);
	
	public static final HardCodedCategoryValue TEAM_TYPE_MULTILATERAL	= 
		new CategoryConstants.HardCodedCategoryValue("team_type","Multilateral", false);
	public static final HardCodedCategoryValue TEAM_TYPE_BILATERAL	= 
		new CategoryConstants.HardCodedCategoryValue("team_type","Bilateral", false);
	
	public static final HardCodedCategoryValue MTEF_PROJECTION_PROJECTION	= 
		new CategoryConstants.HardCodedCategoryValue("mtef_projection","projection", true);
	public static final HardCodedCategoryValue MTEF_PROJECTION_PIPELINE	= 
		new CategoryConstants.HardCodedCategoryValue("mtef_projection","pipeline", false);
	
	public static final HardCodedCategoryValue ACTIVITY_STATUS_PROPOSED	= 
		new CategoryConstants.HardCodedCategoryValue("activity_status","proposed", false);
	
	public static final HardCodedCategoryValue FIN_INSTR_BUDGET_SUPPORT	= 
		new CategoryConstants.HardCodedCategoryValue("financing_instrument","Budget Support", false);
	
	public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_COUNTRY	= 
		new CategoryConstants.HardCodedCategoryValue("implementation_location", "Country", true);
	public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_REGION	= 
		new CategoryConstants.HardCodedCategoryValue("implementation_location", "Region", true);
	public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_ZONE	= 
		new CategoryConstants.HardCodedCategoryValue("implementation_location", "Zone", true);
	public static final HardCodedCategoryValue IMPLEMENTATION_LOCATION_DISTRICT	= 
		new CategoryConstants.HardCodedCategoryValue("implementation_location", "District", true);
	
	public static final HardCodedCategoryValue IMPLEMENTATION_LEVEL_INTERNATIONAL = 
		 new CategoryConstants.HardCodedCategoryValue("implementation_level", "International", true);
	public static final HardCodedCategoryValue IMPLEMENTATION_LEVEL_NATIONAL = 
		 new CategoryConstants.HardCodedCategoryValue("implementation_level", "National", true);
	public static final HardCodedCategoryValue IMPLEMENTATION_LEVEL_REGIONAL = 
		 new CategoryConstants.HardCodedCategoryValue("implementation_level", "Regional", true);
	 
	 public static final HardCodedCategoryValue ACTIVITY_BUDGET_ON = 
		 new CategoryConstants.HardCodedCategoryValue("activity_budget", "On Budget", true);
	 public static final HardCodedCategoryValue ACTIVITY_BUDGET_OFF = 
		 new CategoryConstants.HardCodedCategoryValue("activity_budget", "Off Budget", true);
	 
	//Org. Manager : Staff Information type
	 public static final String ORGANIZATION_STAFF_INFO_NAME="Staff Information Type";
	 public static final String ORGANIZATION_STAFF_INFO_KEY="staff_information_type";
	 
	// organization budget Information type
	public static final String ORGANIZATION_BUDGET_INFO_NAME = "NGO Budget Type";
	public static final String ORGANIZATION_BUDGET_INFO_KEY = "ngo_budget_type";
	
	public static final String CONTACT_TITLE_NAME = "Contact Title";
	public static final String CONTACT_TITLE_KEY = "contact_title";

    public static final String CONTACT_PHONE_TYPE_NAME = "Phone Type";
    public static final String CONTACT_PHONE_TYPE_KEY = "contact_phone_type";


	public static final String RESOURCE_TYPE_COUNTRY_ANALYTIC_REPORT_KEY = "Country Analytic Report (Paris Indicator)";

	public static final String WORKSPACE_GROUP_KEY	= "workspace_group";

	
	public static class HardCodedCategoryValue {
		private String valueKey;
		private String categoryKey;
		private boolean protectOnDelete;
		public String getValueKey() {
			return valueKey;
		}
		public String getCategoryKey() {
			return categoryKey;
		}
				
		public boolean isProtectOnDelete() {
			return protectOnDelete;
		}
		private HardCodedCategoryValue(String categoryKey, String valueKey, boolean protectOnDelete) {
			this.categoryKey		= categoryKey;
			this.valueKey			= valueKey;
			this.protectOnDelete	= protectOnDelete;
		}
	}
}

