/**
 * AmpARFilter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.lucene.store.Directory;
import org.dgfoundation.amp.PropertyListable;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.annotations.reports.IgnorePersistence;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.logic.AmpARFilterHelper;
import org.digijava.module.aim.logic.Logic;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.mondrian.query.MoConstants;

import edu.emory.mathcs.backport.java.util.Collections;


/**
 * Filtering bean. Holds info about filtering parameters and creates the
 * filtering query
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundatiTeam member rights follow
 *         the activity roles as we have defined them.on.org
 * @since Aug 5, 2006
 * 
 */

public class AmpARFilter extends PropertyListable {
	
	public final static String SDF_OUT_FORMAT_STRING = "yyyy-MM-dd";
	public final static String SDF_IN_FORMAT_STRING = "dd/MM/yyyy";
	
	public final static Set<String> activityStatus = Collections.unmodifiableSet(new HashSet<String>() {{
														this.add(Constants.APPROVED_STATUS);
														this.add(Constants.EDITED_STATUS);
														this.add(Constants.STARTED_APPROVED_STATUS);
														}});
	/**
	 * Date string formatted for SQL queries
	 * field not static because SimpleDateFormat is not thread-safe
	 */
	private final SimpleDateFormat sdfOut = new SimpleDateFormat(SDF_OUT_FORMAT_STRING);
	
	/**
	 * Date string formatted for database serialization
	 * field not static because SimpleDateFormat is not thread-safe
	 */
	private final SimpleDateFormat sdfIn = new SimpleDateFormat(SDF_IN_FORMAT_STRING);
	
	protected static Logger logger = Logger.getLogger(AmpARFilter.class);
	private Long id;
	private boolean justSearch=false;
	private boolean workspaceonly=false;

	private Long ampReportId;
	private Set statuses = null;
	private Set workspaces = null;
	// private Set donors=null; //not used anymore
	@PropertyListableIgnore
	private Set sectors = null;
	@PropertyListableIgnore
	private Set sectorsAndAncestors	= null;
	private Set selectedSectors = null;
	
	private Long teamMemberId;
	
	private String CRISNumber;
	private String budgetNumber;
	private Boolean showArchived    = false;
	
	@PropertyListableIgnore
	private Integer computedYear;
	
	@PropertyListableIgnore
	private Integer actualAppYear;
	
	@PropertyListableIgnore
	private ArrayList<FilterParam> indexedParams=null;
	
	/**
	 * whether this filter should queryAppend a WorkspaceFilter query in generateFilterQuery. Ignored when building a workspace filter (always ON)
	 */
	private boolean needsTeamFilter;
	
	@PropertyListableIgnore
	private Set secondarySectors = null;
	private Set selectedSecondarySectors = null;
	@PropertyListableIgnore
	private Set secondarySectorsAndAncestors = null;
    @PropertyListableIgnore
	private Set tertiarySectors = null;
	private Set selectedTertiarySectors = null;
    @PropertyListableIgnore
    private Set tertiarySectorsAndAncestors = null;
    
    @PropertyListableIgnore
	private Set tagSectors = null;
	private Set selectedTagSectors = null;
    @PropertyListableIgnore
    private Set tagSectorsAndAncestors = null;


	@PropertyListableIgnore
	private Set relatedSecondaryProgs;

	@PropertyListableIgnore
	private List nationalPlanningObjectives;
	private Set selectedNatPlanObj;
	@PropertyListableIgnore
	private Set relatedNatPlanObjs;

	@PropertyListableIgnore
	private String teamAccessType;
	
	@PropertyListableIgnore
	private List primaryPrograms;
	private Set selectedPrimaryPrograms;
	@PropertyListableIgnore
	private Set relatedPrimaryProgs;

	@PropertyListableIgnore
	private List secondaryPrograms;
	private Set selectedSecondaryPrograms;
	
	private String multiDonor = null;

	public String getMultiDonor() {
		return multiDonor;
	}

	public void setMultiDonor(String multiDonor) {
		this.multiDonor = multiDonor;
	}

	@PropertyListableIgnore
	public List getNationalPlanningObjectives() {
		return nationalPlanningObjectives;
	}

	public void setNationalPlanningObjectives(List nationalPlanningObjectives) {
		this.nationalPlanningObjectives = nationalPlanningObjectives;
	}


	@PropertyListableIgnore
	public Set getRelatedNatPlanObjs() {
		return relatedNatPlanObjs;
	}

	public void setRelatedNatPlanObjs(Set relatedNatPlanObjs) {
		this.relatedNatPlanObjs = relatedNatPlanObjs;
	}
	@PropertyListableIgnore
	public Set getRelatedSecondaryProgs() {
		return relatedSecondaryProgs;
	}

	public void setRelatedSecondaryProgs(Set relatedSecondaryProgs) {
		this.relatedSecondaryProgs = relatedSecondaryProgs;
	}

	@PropertyListableIgnore
	public Set getRelatedPrimaryProgs() {
		return relatedPrimaryProgs;
	}

	public void setRelatedPrimaryProgs(Set relatedPrimaryProgs) {
		this.relatedPrimaryProgs = relatedPrimaryProgs;
	}

	@PropertyListableIgnore
	public List getPrimaryPrograms() {
		return primaryPrograms;
	}

	public void setPrimaryPrograms(List primaryPrograms) {
		this.primaryPrograms = primaryPrograms;
	}

	@PropertyListableIgnore
	public List getSecondaryPrograms() {
		return secondaryPrograms;
	}

	public void setSecondaryPrograms(List secondaryPrograms) {
		this.secondaryPrograms = secondaryPrograms;
	}

	public Set getSelectedNatPlanObj() {
		return selectedNatPlanObj;
	}

	public void setSelectedNatPlanObj(Set selectedNatPlanObj) {
		this.selectedNatPlanObj = selectedNatPlanObj;
	}

	public Set getSelectedPrimaryPrograms() {
		return selectedPrimaryPrograms;
	}

	public void setSelectedPrimaryPrograms(Set selectedPrimaryPrograms) {
		this.selectedPrimaryPrograms = selectedPrimaryPrograms;
	}

	public Set getSelectedSecondaryPrograms() {
		return selectedSecondaryPrograms;
	}

	public void setSelectedSecondaryPrograms(Set selectedSecondaryPrograms) {
		this.selectedSecondaryPrograms = selectedSecondaryPrograms;
	}

	private Set regions = null;
	private Set risks = null;
	private Set donorTypes = null;
	private Set<AmpOrgGroup> donorGroups = null;
	private Set<AmpOrgGroup> contractingAgencyGroups = null;
	
	private Set responsibleorg = null;
	private Set<AmpOrganisation> executingAgency;
	private Set<AmpOrganisation> contractingAgency;
	private Set<AmpOrganisation> implementingAgency;
	private Set<AmpOrganisation> beneficiaryAgency;
	private Set<AmpOrganisation> donnorgAgency;

	private Set financingInstruments = null;
	private Set<AmpCategoryValue> projectCategory = null;

	
	private Set<AmpCategoryValue> typeOfAssistance = null;
	private Set<AmpCategoryValue> modeOfPayment = null;
	// private Long ampModalityId=null;

	private AmpCurrency currency = null;

	/* NOT USED
	private Set ampTeamsforpledges = null;
	*/
	
	private AmpFiscalCalendar calendarType = null;
	private boolean widget = false;
	private boolean publicView = false;
	private Set<AmpCategoryValue> budget = null;
	private Collection<Integer> lineMinRank;
	private Collection<Integer> planMinRank;
	
	/**
	 * the date is stored in the {@link #sdfIn} hardcoded format
	 */
	private String fromDate;
	
	/**
	 * the date is stored in the {@link #sdfIn} hardcoded format
	 */
	private String toDate;
	
	private String fromActivityStartDate; // view: v_actual_start_date, column name: Actual Start Date
	private String toActivityStartDate;
	
	private String fromActivityActualCompletionDate; // view: v_actual_completion_date, column name: Current Completion Date
	private String toActivityActualCompletionDate;  // view: v_actual_completion_date, column name: Current Completion Date
	
	private String fromActivityFinalContractingDate; // view: v_contracting_date, column name: Final Date for Contracting
	private String toActivityFinalContractingDate;  // view: v_contracting_date, column name: Final Date for Contracting
	
	private Integer fromMonth;
	private Integer yearFrom;
	private Integer toMonth;
	private Integer yearTo;
	private Collection<AmpCategoryValueLocations> locationSelected = null;
	@PropertyListableIgnore
	private Collection<AmpCategoryValueLocations> relatedLocations;
	private Collection<AmpCategoryValueLocations> pledgesLocations;
	private Boolean unallocatedLocation = null;
	//private AmpCategoryValueLocations regionSelected = null;
	private Collection<String> approvalStatusSelected=null;
	private boolean approved = false;
	private boolean draft = false;

	private Integer renderStartYear = null; // the range of dates columns that
											// has to be render, years not in
											// range will be computables for
											// totals but wont be rederisables
	private Integer renderEndYear = null;

	private DecimalFormat currentFormat = null;
	
	public final static int AMOUNT_OPTION_IN_UNITS = 0; // not sure
	public final static int AMOUNT_OPTION_IN_THOUSANDS = 1;
	public final static int AMOUNT_OPTION_IN_MILLIONS = 2;
	
	private Integer amountinthousand;
	
	/**
	 * DEPRECATED, TO BE REMOVED IN NEXT BRANCH
	 * @deprecated
	 */
	private Boolean amountinmillion;
	
	private String decimalseparator;
	private String groupingseparator;
	private Integer groupingsize;
	private Boolean customusegroupings;
	private Integer maximumFractionDigits;
	
	
	/**
	 * @return the maximumFractionDigits
	 */
	public Integer getMaximumFractionDigits() {
		return maximumFractionDigits;
	}

	/**
	 * @param maximumFractionDigits the maximumFractionDigits to set
	 */
	public void setMaximumFractionDigits(Integer maximumFractionDigits) {
		this.maximumFractionDigits = maximumFractionDigits;
	}

	public String getDecimalseparator() {
		return decimalseparator;
	}

	public void setDecimalseparator(String decimalseparator) {
		this.decimalseparator = decimalseparator;
	}

	public String getGroupingseparator() {
		return groupingseparator;
	}

	public void setGroupingseparator(String groupingseparator) {
		this.groupingseparator = groupingseparator;
	}
	
	/**
	 * DO NOT USE - TO BE PHYSICALLY REMOVED IN 2.4
	 * @deprecated
	 * @return
	 */
	public final Boolean getAmountinmillion() {
		return amountinmillion;
	}

	/**
	 * DO NOT USE - TO BE PHYSICALLY REMOVED IN 2.4
	 * @deprecated
	 * @return
	 */
	public final void setAmountinmillion(Boolean amountinmillion) {
		this.amountinmillion = amountinmillion;
	}

	private Boolean governmentApprovalProcedures;
	private Boolean jointCriteria;
	private String accessType=null;
	
	
	private String pageSize; // to be used for exporting reports

	private String text;
	private String indexText;
	private String searchMode;
	private static final String initialPledgeFilterQuery = "SELECT distinct(id) FROM amp_funding_pledges WHERE 1=1 ";
	private static final String initialFilterQuery = "SELECT distinct(amp_activity_id) FROM amp_activity WHERE 1=1 ";
	private String generatedFilterQuery;
	private int initialQueryLength = initialFilterQuery.length();
	
	private String sortBy;
	private Boolean sortByAsc						= true;
	private Collection<String> hierarchySorters		= new ArrayList<String>();
	
	private Set<AmpCategoryValue> projectImplementingUnits = null; 
	
	private boolean budgetExport		= false;
	
	private void queryAppend(String filter) {
		generatedFilterQuery += " AND amp_activity_id IN (" + filter + ")";
	}
	
	private void PledgequeryAppend(String filter) {
		generatedFilterQuery += " AND id IN (" + filter + ")";
	}
	
	/**
	 * TODO-Constantin: non-trivially-slow function called at least 3 times per report render
	 * @param request
	 */
	public void readRequestData(HttpServletRequest request) {
		
		this.generatedFilterQuery = initialFilterQuery;
		TeamMember tm = (TeamMember) request.getSession().getAttribute(
				Constants.CURRENT_MEMBER);
		
		String ampReportId = null ;
		//Check if the reportid is not nut for public mondrian reports
		if (request.getParameter("ampReportId")!=null && !request.getParameter("ampReportId").equals("")){
			ampReportId = request.getParameter("ampReportId");
			AmpReports ampReport=DbUtil.getAmpReport(Long.parseLong(ampReportId));
			if (ampReport != null)
			{
				this.budgetExport	= ampReport.getBudgetExporter()==null ? false:ampReport.getBudgetExporter();
				
				Site site = RequestUtils.getSite(request);
				Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
				String siteId = site.getId().toString();
				String locale = navigationLanguage.getCode();
				ampReport.setSiteId(siteId);
				ampReport.setLocale(locale);
				if (ampReport.getType() == ArConstants.PLEDGES_TYPE){
						this.generatedFilterQuery = initialPledgeFilterQuery;
				}
			}
		}
		if (ampReportId == null) {
			AmpReports ar = (AmpReports) request.getSession().getAttribute(
			"reportMeta");
			if (ar!=null){
				ampReportId = ar.getAmpReportId().toString();
			}
			if (ar !=null && ar.getType() == ArConstants.PLEDGES_TYPE){
				this.generatedFilterQuery = initialPledgeFilterQuery;
			}
		}
		
		AmpApplicationSettings tempSettings = null;
		
		if (tm == null || tm.getTeamId() == null ) {
			tm	= null;
		}
				
		if (tm != null) {
			this.setNeedsTeamFilter(false);
			this.setAccessType(tm.getTeamAccessType());
			teamMemberId = tm.getMemberId();

			tempSettings = DbUtil.getMemberAppSettings(tm.getMemberId());

			if (tempSettings == null)
				if (tm != null)
					tempSettings = DbUtil.getTeamAppSettings(tm.getTeamId());

			if (this.getCurrency() == null)
				this.setCurrency(tempSettings.getCurrency());

		}
		else {
			this.setNeedsTeamFilter(true);
			//Check if the reportid is not nut for public mondrian reports
			if (ampReportId != null){
				AmpReports ampReport=DbUtil.getAmpReport(Long.parseLong(ampReportId));
				if (ampReport != null && ampReport.getOwnerId() != null)
					teamMemberId = ampReport.getOwnerId().getAmpTeamMemId();
			}
		}
		
		 if (calendarType==null){
			if (tempSettings!=null){
				calendarType=tempSettings.getFiscalCalendar();
			}
		}
			
		if (tempSettings!=null){
			calendarType=tempSettings.getFiscalCalendar();
		}
		String gvalue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (calendarType==null){
			if (gvalue!=null){
				Long fiscalCalId=Long.parseLong(gvalue);
				calendarType=DbUtil.getFiscalCalendar(fiscalCalId);
			}
		}
		
		
		Long defaultCalendarId=null;
		
		if (tempSettings!=null){
			if (tempSettings.getFiscalCalendar()!=null){
				defaultCalendarId=tempSettings.getFiscalCalendar().getAmpFiscalCalId();
			}else{
				defaultCalendarId=Long.parseLong(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR));	
			}
		}	
		///Set the range depending of workspase setup / global setting and selected calendar
		ICalendarWorker worker = null;
		Date checkDate = null;
		//If the years filter data has just be taken from the filter data from the db, it must not be overridden
		Boolean overrideYears = (request.getParameter("view") != null && "reset".compareTo(request.getParameter("view")) == 0) 
				&& (request.getAttribute(ArConstants.INITIALIZE_FILTER_FROM_DB) == null || !"true".equals(request.getAttribute(ArConstants.INITIALIZE_FILTER_FROM_DB)));

		if (renderStartYear == null || overrideYears) {
			// Check if there is value on workspace setting
			if (tempSettings != null
					&& tempSettings.getReportStartYear() != null
					&& tempSettings.getReportStartYear().intValue() != 0) {
				this.setRenderStartYear(tempSettings.getReportStartYear());
			} else { // if not check if the value exist on
				// global setting
				 gvalue = FeaturesUtil
						.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
				if (gvalue != null && !"".equalsIgnoreCase(gvalue)
						&& Integer.parseInt(gvalue) > 0) {
					renderStartYear = Integer.parseInt(gvalue);
				}

			}
			
			if (renderStartYear!=null && renderStartYear>0 && calendarType != null && calendarType.getAmpFiscalCalId().equals(defaultCalendarId) ){
				worker = calendarType.getworker();
				try {
					checkDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/" + renderStartYear);
					worker.setTime(checkDate);
					renderStartYear=worker.getYear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		renderStartYear=(renderStartYear==null)?-1:renderStartYear;
		
		if (renderEndYear == null || overrideYears) {
			// Check if there is value on workspace setting
			if (tempSettings != null && tempSettings.getReportEndYear() != null
					&& tempSettings.getReportEndYear().intValue() != 0) {
				this.setRenderEndYear(tempSettings.getReportEndYear());
			} else {
				 gvalue=null;
				 gvalue = FeaturesUtil
						.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
				if (gvalue != null && !"".equalsIgnoreCase(gvalue)
						&& Integer.parseInt(gvalue) > 0) {
					renderEndYear = Integer.parseInt(gvalue);
				}
			}
			
			 if (renderEndYear!=null && renderEndYear>0 && calendarType != null && calendarType.getAmpFiscalCalId().equals(defaultCalendarId) ){
				worker = calendarType.getworker();
				try {
					checkDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/" + renderEndYear);
					worker.setTime(checkDate);
					renderEndYear=worker.getYear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		renderEndYear=(renderEndYear==null)?-1:renderEndYear;
		

		if (currentFormat == null) {
			currentFormat = FormatHelper.getDefaultFormat();
			FormatHelper.tlocal.set(null);
		} else {
			FormatHelper.tlocal.set(currentFormat);
		}

		String widget = (String) request.getAttribute("widget");
		if (widget != null)
			this.setWidget(new Boolean(widget).booleanValue());

		try {
			if (ampReportId != null)
				this.setAmpReportId(new Long(ampReportId));
		}
		catch (NumberFormatException e) {
			logger.info("NumberFormatException:" + e.getMessage());
			e.printStackTrace();
		}

	}

	public AmpARFilter() {
		super();
		this.generatedFilterQuery = initialFilterQuery;
	}
	
	private String createDateCriteria(String to, String from, String sqlColumn) {
		String dateCriteria	 	= "";
		try {
			if ( (to != null && to.length() > 0)  ) {
				dateCriteria	= sqlColumn + " <= '" + sdfOut.format(sdfIn.parse(to)) + "'";
			}
			if ( (from != null && from.length() > 0)  ) {
				if ( dateCriteria.length() > 0 ) {
					dateCriteria += " AND ";
				}
				else
					dateCriteria	= "";
				
				dateCriteria	+= sqlColumn + " >= '" + sdfOut.format(sdfIn.parse(from)) + "'";
			}
		}
		catch (ParseException pe) {
			pe.printStackTrace();
		}
		
		return dateCriteria;
	}

	public void generateFilterQuery(HttpServletRequest request, boolean workspaceFilter) {
		if (request.getSession().getAttribute(ArConstants.PLEDGES_REPORT) != null && 
				request.getSession().getAttribute(ArConstants.PLEDGES_REPORT).toString().equalsIgnoreCase("true")){
			indexedParams=new ArrayList<FilterParam>();
			
			
			String WORKSPACE_ONLY="";
			if (this.workspaceonly && "Management".equals(this.getAccessType())){
					WORKSPACE_ONLY = "SELECT v.pledge_id FROM v_pledges_projects v WHERE v.approval_status IN ("+Util.toCSString(activityStatus)+")";
					PledgequeryAppend(WORKSPACE_ONLY);
			}
			
			String DONNOR_AGENCY_FILTER = " SELECT v.pledge_id FROM v_pledges_donor v  WHERE v.amp_donor_org_id IN ("
				+ Util.toCSString(donnorgAgency) + ")";
			
			String DONOR_TYPE_FILTER	= "SELECT v.id FROM v_pledges_donor_type v WHERE org_type_id IN ("
				+ Util.toCSString(donorTypes) + ")";

			String DONOR_GROUP_FILTER = "SELECT v.pledge_id FROM v_pledges_donor_group v WHERE amp_org_grp_id IN ("
					+ Util.toCSString(donorGroups) + ")";

			String FINANCING_INSTR_FILTER = "SELECT v.pledge_id FROM v_pledges_aid_modality v WHERE amp_modality_id IN ("
				+ Util.toCSString(financingInstruments) + ")";
			
			String TYPE_OF_ASSISTANCE_FILTER = "SELECT v.pledge_id FROM v_pledges_type_of_assistance v WHERE terms_assist_code IN ("
				+ Util.toCSString(typeOfAssistance) + ")";
			
			String SECTOR_FILTER = "SELECT pledge_id FROM v_pledges_sectors ps, amp_sector s"
				+ " WHERE ps.amp_sector_id=s.amp_sector_id"
				+ " AND ps.amp_sector_id in ("+ Util.toCSString(sectors) + ")";
			
			String REGION_SELECTED_FILTER = "";
			if (locationSelected!=null) {
				Set<AmpCategoryValueLocations> allSelectedLocations = new HashSet<AmpCategoryValueLocations>();
				allSelectedLocations.addAll(locationSelected);
				
				DynLocationManagerUtil.populateWithDescendants(allSelectedLocations, locationSelected);
				this.pledgesLocations = new ArrayList<AmpCategoryValueLocations>();
				this.pledgesLocations.addAll(allSelectedLocations);
				DynLocationManagerUtil.populateWithAscendants(this.pledgesLocations, locationSelected);
				
				String allSelectedLocationString = Util.toCSString(allSelectedLocations);
				String subSelect = "SELECT aal.pledge_id FROM amp_funding_pledges_location aal, amp_location al " +
						"WHERE ( aal.location_id=al.location_id AND " +
						"al.location_id IN (" + allSelectedLocationString + ") )";
				
				if (REGION_SELECTED_FILTER.equals("")) {
					REGION_SELECTED_FILTER	= subSelect;
				} else {
					REGION_SELECTED_FILTER += " OR amp_activity_id IN (" + subSelect + ")"; 
				}			
			}
			
			if (donnorgAgency != null && donnorgAgency.size() > 0){
				PledgequeryAppend(DONNOR_AGENCY_FILTER);
			}
			
			if (donorGroups != null && donorGroups.size() > 0)
				PledgequeryAppend(DONOR_GROUP_FILTER);
			
			if (donorTypes != null && donorTypes.size() > 0)
				PledgequeryAppend(DONOR_TYPE_FILTER);
			
			if (financingInstruments != null && financingInstruments.size() > 0){
				PledgequeryAppend(FINANCING_INSTR_FILTER);
			}
			if (typeOfAssistance != null && typeOfAssistance.size() > 0){
				PledgequeryAppend(TYPE_OF_ASSISTANCE_FILTER);
			}
			if (sectors != null && sectors.size() > 0){
				PledgequeryAppend(SECTOR_FILTER);
			}
			if (!REGION_SELECTED_FILTER.equals("")) {
				PledgequeryAppend(REGION_SELECTED_FILTER);
			}
		
			return;
		}
		
		indexedParams=new ArrayList<FilterParam>();
		
		String BUDGET_FILTER = "SELECT amp_activity_id FROM v_on_off_budget WHERE budget_id IN ("
			+ Util.toCSString(budget) + ")";			

		String STATUS_FILTER = "SELECT amp_activity_id FROM v_status WHERE amp_status_id IN ("
				+ Util.toCSString(statuses) + ")";

		String WORKSPACE_FILTER = "select amp_activity_id from amp_activity where amp_team_id IN ("
			+ Util.toCSString(workspaces) + ")";

		// String ORG_FILTER = "SELECT amp_activity_id FROM v_donor_groups WHERE
		// amp_org_grp_id IN ("+Util.toCSString(donors,true)+")";
		// String PARENT_SECTOR_FILTER="SELECT amp_activity_id FROM v_sectors
		// WHERE amp_sector_id IN ("+Util.toCSString(sectors,true)+")";
		// String SUB_SECTOR_FILTER="SELECT amp_activity_id FROM v_sub_sectors
		// WHERE amp_sector_id IN ("+Util.toCSString(sectors,true)+")";
		// String SECTOR_FILTER="(("+PARENT_SECTOR_FILTER+") UNION
		// ("+SUB_SECTOR_FILTER+"))";

		String SECTOR_FILTER = "SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
				+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
				+ "AND c.name='Primary' AND aas.amp_sector_id in ("
				+ Util.toCSString(sectors) + ")";

		String NATIONAL_PLAN_FILTER = "SELECT aap.amp_activity_id FROM amp_activity_program aap inner join  amp_theme p on aap.amp_program_id=p.amp_theme_id "
				+ "inner join  AMP_PROGRAM_SETTINGS ps on ps.amp_program_settings_id=aap.program_setting where ps.name='National Plan Objective' AND "
				+ " aap.amp_program_id in ("
				+ Util.toCSString(nationalPlanningObjectives) + ")";

		String PRIMARY_PROGRAM_FILTER = "SELECT aap.amp_activity_id FROM amp_activity_program aap inner join  amp_theme p on aap.amp_program_id=p.amp_theme_id "
				+ "inner join  AMP_PROGRAM_SETTINGS ps on ps.amp_program_settings_id=aap.program_setting where ps.name='Primary Program' AND "
				+ " aap.amp_program_id in ("
				+ Util.toCSString(primaryPrograms) + ")";

		String SECONDARY_PROGRAM_FILTER = "SELECT aap.amp_activity_id FROM amp_activity_program aap inner join  amp_theme p on aap.amp_program_id=p.amp_theme_id "
				+ "inner join  AMP_PROGRAM_SETTINGS ps on ps.amp_program_settings_id=aap.program_setting where ps.name='Secondary Program' AND "
				+ " aap.amp_program_id in ("
				+ Util.toCSString(secondaryPrograms) + ")";

		// String SECONDARY_PARENT_SECTOR_FILTER=
		// "SELECT amp_activity_id FROM v_secondary_sectors WHERE amp_sector_id
		// IN ("+Util.toCSString(secondarySectors,true)+")";
		// String SECONDARY_SUB_SECTOR_FILTER=
		// "SELECT amp_activity_id FROM v_secondary_sub_sectors WHERE
		// amp_sector_id IN ("+Util.toCSString(secondarySectors,true)+")";
		// String SECONDARY_SECTOR_FILTER="(("+SECONDARY_PARENT_SECTOR_FILTER+")
		// UNION ("+SECONDARY_SUB_SECTOR_FILTER+"))";
		String SECONDARY_SECTOR_FILTER = "SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
				+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
				+ "AND c.name='Secondary' AND aas.amp_sector_id in ("
				+ Util.toCSString(secondarySectors) + ")";

       String TERTIARY_SECTOR_FILTER = "SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
				+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
				+ "AND c.name='Tertiary' AND aas.amp_sector_id in ("
				+ Util.toCSString(tertiarySectors) + ")";
       
       String TAG_SECTOR_FILTER = "SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
				+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
				+ "AND c.name='Tag' AND aas.amp_sector_id in ("
				+ Util.toCSString(tagSectors) + ")";


		String REGION_FILTER = "SELECT amp_activity_id FROM v_regions WHERE name IN ("
				+ Util.toCSString(regions) + ")";
		String FINANCING_INSTR_FILTER = "SELECT amp_activity_id FROM v_financing_instrument WHERE amp_modality_id IN ("
				+ Util.toCSString(financingInstruments) + ")";
		String LINE_MIN_RANK_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE line_min_rank IN ("
	 	 		+ Util.toCSString(lineMinRank) + ")";
	 	String PLAN_MIN_RANK_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE plan_min_rank IN ("
	 	 		+ Util.toCSString( planMinRank ) + ")";
		//String REGION_SELECTED_FILTER = "SELECT amp_activity_id FROM v_regions WHERE region_id ="
		//		+ (regionSelected==null?null:regionSelected.getIdentifier());
	 	
	 	String MULTI_DONOR		= "SELECT amp_activity_id FROM v_multi_donor WHERE value = '" + multiDonor + "'";
	 	
	 	String REGION_SELECTED_FILTER = "";
 		if (unallocatedLocation != null) {
			if (unallocatedLocation == true) {
				REGION_SELECTED_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE amp_activity_id NOT IN(SELECT amp_activity_id FROM amp_activity_location)";
			}
		}
		
		String ACTUAL_APPROVAL_YEAR_FILTER = "";
		if (actualAppYear!=null && actualAppYear!=-1) {
			ACTUAL_APPROVAL_YEAR_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE EXTRACT (YEAR FROM actual_approval_date) = " + actualAppYear + " ";
		}
		
		if (locationSelected!=null) {
			Set<AmpCategoryValueLocations> allSelectedLocations = new HashSet<AmpCategoryValueLocations>();
			allSelectedLocations.addAll(locationSelected);
			
			DynLocationManagerUtil.populateWithDescendants(allSelectedLocations, locationSelected);
			this.relatedLocations						= new ArrayList<AmpCategoryValueLocations>();
			this.relatedLocations.addAll(allSelectedLocations);
			DynLocationManagerUtil.populateWithAscendants(this.relatedLocations, locationSelected);
			
			String allSelectedLocationString			= Util.toCSString(allSelectedLocations);
			String subSelect			= "SELECT aal.amp_activity_id FROM amp_activity_location aal, amp_location al " +
					"WHERE ( aal.amp_location_id=al.amp_location_id AND " +
					"al.location_id IN (" + allSelectedLocationString + ") )";
			
			if (REGION_SELECTED_FILTER.equals("")) {
				REGION_SELECTED_FILTER	= subSelect;
			} else {
				REGION_SELECTED_FILTER += " OR amp_activity_id IN (" + subSelect + ")"; 
			}			
		}
		
		StringBuffer actStatusValue = new StringBuffer("");
		if(approvalStatusSelected!=null){
			for(String valOption:approvalStatusSelected){
				switch (Integer.parseInt(valOption)) {
				case -1:
					actStatusValue.append("1=1 ");					
					break;
				case 0://Existing Un-validated - This will show all the activities that have been approved at least once and have since been edited and not validated.
					actStatusValue.append(" (approval_status='edited' and draft <> true)");break;
				case 1://New Draft - This will show all the activities that have never been approved and are saved as drafts.
					actStatusValue.append(" (approval_status in ('started', 'startedapproved') and draft is true) ");break;
				case 2://New Un-validated - This will show all activities that are new and have never been approved by the workspace manager.
					actStatusValue.append(" (approval_status='started' and draft <> true)");break;
				case 3://existing draft. This is because when you filter by Existing Unvalidated you get draft activites that were edited and saved as draft
					actStatusValue.append(" ( (approval_status='edited' or approval_status='approved') and draft is true ) ");break;
				case 4://Validated Activities 
					actStatusValue.append("(approval_status='approved' and draft<>true)");break;
				default:actStatusValue.append("1=1 ");	break;
				}
				actStatusValue.append(" OR ");
			}
		    int posi = actStatusValue.lastIndexOf("OR");
		    if(posi>0)
		    	actStatusValue.delete(posi, posi+2);
		}    
		String ACTIVITY_STATUS="select amp_activity_id from amp_activity where "+actStatusValue.toString();
		String APPROVED_FILTER = "";
			if("Management".equals(this.getAccessType()))
				APPROVED_FILTER="SELECT amp_activity_id FROM amp_activity WHERE approval_status IN ("
					+ Util.toCSString(activityStatus) + ")";
			else APPROVED_FILTER="SELECT amp_activity_id FROM amp_activity WHERE approval_status IN ('"
				+ Constants.APPROVED_STATUS + "','"+ Constants.STARTED_APPROVED_STATUS +"')";
		
		String DRAFT_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE (draft is null) OR (draft is false )";
		String TYPE_OF_ASSISTANCE_FILTER = "SELECT amp_activity_id FROM v_terms_assist WHERE terms_assist_code IN ("
			+ Util.toCSString(typeOfAssistance) + ")";

		String MODE_OF_PAYMENT_FILTER = "SELECT amp_activity_id FROM v_mode_of_payment WHERE mode_of_payment_code IN ("
			+ Util.toCSString(modeOfPayment) + ")";

		String PROJECT_CATEGORY_FILTER = "SELECT amp_activity_id FROM v_project_category WHERE amp_category_id IN ("
			+ Util.toCSString(projectCategory) + ")";

		String PROJECT_IMPL_UNIT_FILTER = "SELECT amp_activity_id FROM v_project_impl_unit WHERE proj_impl_unit_id IN ("
			+ Util.toCSString(projectImplementingUnits) + ")";

//		String DONOR_TYPE_FILTER = "SELECT aa.amp_activity_id "
//				+ "FROM amp_activity aa, amp_org_role aor, amp_role rol, amp_org_type typ, amp_organisation og  "
//				+ "WHERE aa.amp_activity_id = aor.activity AND aor.role = rol.amp_role_id AND rol.role_code='DN' "
//				+ "AND typ.amp_org_type_id =  og.org_type_id AND og.amp_org_id = aor.organisation "
//				+ "AND typ.amp_org_type_id IN ("
//				+ Util.toCSString(donorTypes) + ")";
		
		String DONOR_TYPE_FILTER	= "SELECT amp_activity_id FROM v_donor_type WHERE org_type_id IN ("
			+ Util.toCSString(donorTypes) + ")";

		String DONOR_GROUP_FILTER = "SELECT amp_activity_id FROM v_donor_groups WHERE amp_org_grp_id IN ("
				+ Util.toCSString(donorGroups) + ")";

		String CONTRACTING_AGENCY_GROUP_FILTER = "SELECT amp_activity_id FROM v_contracting_agency_groups WHERE amp_org_grp_id IN ("
				+ Util.toCSString(contractingAgencyGroups) + ")";
				
		String EXECUTING_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_executing_agency v  "
				+ "WHERE v.amp_org_id IN ("
				+ Util.toCSString(executingAgency) + ")";
		
		String CONTRACTING_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_contracting_agency v  "
				+ "WHERE v.amp_org_id IN ("
				+ Util.toCSString(contractingAgency) + ")";

		
		String BENEFICIARY_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_beneficiary_agency v  "
				+ "WHERE v.amp_org_id IN ("
				+ Util.toCSString(beneficiaryAgency) + ")";
		
		String IMPLEMENTING_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_implementing_agency v  "
				+ "WHERE v.amp_org_id IN ("
				+ Util.toCSString(implementingAgency) + ")";
		
		String RESPONSIBLE_ORGANIZATION_FILTER = " SELECT v.amp_activity_id FROM v_responsible_organisation v  WHERE v.amp_org_id IN ("
			+ Util.toCSString(responsibleorg) + ")";

		String DONNOR_AGENCY_FILTER = " SELECT v.amp_activity_id FROM v_donors v  WHERE v.amp_donor_org_id IN ("
			+ Util.toCSString(donnorgAgency) + ")";
		String ARCHIVED_FILTER          = "";
		if (this.showArchived != null)
			ARCHIVED_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE "
					+ ((this.showArchived) ? "archived=true"
							: "(archived=false or archived is null)");

		boolean dateFilterHidesProjects = "true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DATE_FILTER_HIDES_PROJECTS));

		if(dateFilterHidesProjects && fromDate!=null && fromDate.length()>0) {
			String FROM_DATE_FILTER=null;
			try {
				FROM_DATE_FILTER = " SELECT distinct(f.amp_activity_id) FROM amp_funding_detail fd, amp_funding f WHERE f.amp_funding_id=fd.amp_funding_id AND fd.transaction_date>='"
					+ sdfOut.format(sdfIn.parse(fromDate)) + "'";

			} catch (ParseException e) {
				logger.error(e);
				e.printStackTrace();
			}
			
			queryAppend(FROM_DATE_FILTER);
		}
		if(dateFilterHidesProjects && toDate!=null && toDate.length()>0) {
			String TO_DATE_FILTER=null;
			try {
				TO_DATE_FILTER = " SELECT distinct(f.amp_activity_id) FROM amp_funding_detail fd, amp_funding f WHERE f.amp_funding_id=fd.amp_funding_id AND fd.transaction_date<='"
					+  sdfOut.format(sdfIn.parse(toDate)) + "'";
			} catch (ParseException e) {
				logger.error(e);
				e.printStackTrace();
			}		
			queryAppend(TO_DATE_FILTER);
		}
		
		String ACTIVITY_START_DATE_FILTER	 	= this.createDateCriteria(toActivityStartDate, fromActivityStartDate, "asd.actual_start_date");
		if ( ACTIVITY_START_DATE_FILTER.length() > 0 ) {
			ACTIVITY_START_DATE_FILTER = "SELECT asd.amp_activity_id from v_actual_start_date asd WHERE " + ACTIVITY_START_DATE_FILTER;
			queryAppend(ACTIVITY_START_DATE_FILTER);
		}
		
		String ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER	 	= this.createDateCriteria(toActivityActualCompletionDate, fromActivityActualCompletionDate, "acd.actual_completion_date");
		if ( ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER.length() > 0 ) {
			ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER = "SELECT acd.amp_activity_id from v_actual_completion_date acd WHERE " + ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER;
			queryAppend(ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER);
		}
		
		String ACTIVITY_FINAL_CONTRACTING_DATE_FILTER	 	= this.createDateCriteria(toActivityFinalContractingDate, fromActivityFinalContractingDate, "ctrd.contracting_date");
		if ( ACTIVITY_FINAL_CONTRACTING_DATE_FILTER.length() > 0 ) {
			ACTIVITY_FINAL_CONTRACTING_DATE_FILTER = "SELECT ctrd.amp_activity_id from v_contracting_date ctrd WHERE " + ACTIVITY_FINAL_CONTRACTING_DATE_FILTER;
			queryAppend(ACTIVITY_FINAL_CONTRACTING_DATE_FILTER);
		}
		
		/*
		 * if(fromYear!=null) { AmpARFilterHelper filterHelper =
		 * Logic.getInstance().getAmpARFilterHelper(); String
		 * FROM_FUNDING_YEAR_FILTER =
		 * filterHelper.createFromYearQuery(fromYear);
		 * queryAppend(FROM_FUNDING_YEAR_FILTER); }
		 * 
		 * if(toYear!=null) { AmpARFilterHelper filterHelper =
		 * Logic.getInstance().getAmpARFilterHelper(); String
		 * TO_FUNDING_YEAR_FILTER = filterHelper.createToYearQuery(toYear);
		 * queryAppend(TO_FUNDING_YEAR_FILTER); }
		 * 
		 * if (fromMonth!=null) { AmpARFilterHelper filterHelper =
		 * Logic.getInstance().getAmpARFilterHelper(); String FROM_MONTH_FILTER =
		 * filterHelper.createFromMonthQuery(fromMonth);
		 * queryAppend(FROM_MONTH_FILTER); }
		 * 
		 * if (toMonth!=null) { AmpARFilterHelper filterHelper =
		 * Logic.getInstance().getAmpARFilterHelper(); String TO_MONTH_FILTER =
		 * filterHelper.createToMonthQuery(toMonth);
		 * queryAppend(TO_MONTH_FILTER); }
		 */

		if (fromMonth == null) {
			if (yearFrom != null) {
				AmpARFilterHelper filterHelper = Logic.getInstance()
						.getAmpARFilterHelper();
				String FROM_FUNDING_YEAR_FILTER = filterHelper
						.createFromYearQuery(yearFrom);
				queryAppend(FROM_FUNDING_YEAR_FILTER);
			}
		} else {
			AmpARFilterHelper filterHelper = Logic.getInstance()
					.getAmpARFilterHelper();
			String FROM_FUNDING_YEARMONTH_FILTER = filterHelper
					.createFromMonthQuery(fromMonth, yearFrom);
			queryAppend(FROM_FUNDING_YEARMONTH_FILTER);
		}

		if (toMonth == null) {
			if (yearTo != null) {
				AmpARFilterHelper filterHelper = Logic.getInstance()
						.getAmpARFilterHelper();
				String TO_FUNDING_YEAR_FILTER = filterHelper
						.createToYearQuery(yearTo);
				queryAppend(TO_FUNDING_YEAR_FILTER);
			}
		} else {
			AmpARFilterHelper filterHelper = Logic.getInstance()
					.getAmpARFilterHelper();
			String TO_FUNDING_YEARMONTH_FILTER = filterHelper
					.createToMonthQuery(toMonth, yearTo);
			queryAppend(TO_FUNDING_YEARMONTH_FILTER);
		}

		
		/*
		if (fromDate != null) 
			if (fromDate.trim().length() > 0){
				String FROM_FUNDING_DATE_FILTER = "SELECT DISTINCT(f.amp_activity_id) FROM amp_funding f, amp_funding_detail fd "
					+ "WHERE f.amp_funding_id=fd.amp_funding_id AND DATEDIFF(fd.transaction_date,?) >= 0";
				queryAppend(FROM_FUNDING_DATE_FILTER);
				//add to the params list that will be used on the prepared statment
				indexedParams.add(new FilterParam(new java.sql.Date(FormatHelper.parseDate2(this.getFromDate()).getTime()),java.sql.Types.DATE));
			}
		if (toDate != null)
			if (toDate.trim().length() > 0){
				String TO_FUNDING_DATE_FILTER = "SELECT DISTINCT(f.amp_activity_id) FROM amp_funding f, amp_funding_detail fd "
					+ "WHERE f.amp_funding_id=fd.amp_funding_id AND DATEDIFF(?, fd.transaction_date) >= 0";
				queryAppend(TO_FUNDING_DATE_FILTER);
				//add to the params list that will be used on the prepared statment
				indexedParams.add(new FilterParam(new java.sql.Date(FormatHelper.parseDate2(this.getToDate()).getTime()),java.sql.Types.DATE));
				
			}
			*/
		
		/*
		 * if (fromYear==null) fromYear = 0;
		 * 
		 * if (fromMonth==null) fromMonth = 1;
		 * 
		 * if (toYear==null) toYear = 9999;
		 * 
		 * if (toMonth==null) toMonth = 12;
		 * 
		 * AmpARFilterHelper filterHelper =
		 * Logic.getInstance().getAmpARFilterHelper(); String MONTH_YEAR_FILTER =
		 * filterHelper.createMonthYearQuery(fromMonth, fromYear, toMonth,
		 * toYear); queryAppend(MONTH_YEAR_FILTER);
		 */
		if (text != null) {
			if ("".equals(text.trim()) == false) {
				String TEXT_FILTER = "SELECT a.amp_activity_id from amp_activity a WHERE a.amp_id="
						+ text;
				queryAppend(TEXT_FILTER);
			}
		}

		if (indexText != null)
			if ("".equals(indexText.trim()) == false) {
				String LUCENE_ID_LIST = "";
				HttpSession session = request.getSession();
				ServletContext ampContext = session.getServletContext();
				Directory idx = (Directory) ampContext
						.getAttribute(Constants.LUCENE_INDEX);
				if(request.getParameter("searchMode") != null)
					searchMode = request.getParameter("searchMode");
				Hits hits = LuceneUtil.search(ampContext.getRealPath("/") + LuceneUtil.ACTVITY_INDEX_DIRECTORY, "all", indexText, searchMode);
				logger.info("New lucene search !");
				if(hits!=null)
				for (int i = 0; i < hits.length(); i++) {
					Document doc;
					try {
						doc = hits.doc(i);
						if (LUCENE_ID_LIST == "")
							LUCENE_ID_LIST = doc.get("id");
						else
							LUCENE_ID_LIST = LUCENE_ID_LIST + ","
									+ doc.get("id");
						// AmpActivity act =
						// ActivityUtil.getAmpActivity(Long.parseLong(doc.get("id")));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				logger.info("Lucene ID List:" + LUCENE_ID_LIST);
				if (LUCENE_ID_LIST.length() < 1) {
					logger.info("Not found!");
					LUCENE_ID_LIST = "-1";
				}
				queryAppend(LUCENE_ID_LIST);
			}

		String RISK_FILTER = "SELECT v.activity_id from AMP_ME_INDICATOR_VALUE v, AMP_INDICATOR_RISK_RATINGS r where v.risk=r.amp_ind_risk_ratings_id and r.amp_ind_risk_ratings_id in ("
				+ Util.toCSString(risks) + ")";

		if (budget != null)
			queryAppend(BUDGET_FILTER);

		if (needsTeamFilter || workspaceFilter)
		{
			String TEAM_FILTER = WorkspaceFilter.getWorkspaceFilterQuery(this.getTeamMemberId(), this.getAccessType(), this.isDraft());
			queryAppend(TEAM_FILTER);
		}

		if (!workspaceFilter)
		{
			// not workspace, e.g. normal report/tab filter
			// Merge Filter with the Workspace Filter
			AmpARFilter teamFilter = (AmpARFilter) request.getSession().getAttribute(ArConstants.TEAM_FILTER);

			if (!this.budgetExport && (teamFilter != null))
			{
				queryAppend(teamFilter.getGeneratedFilterQuery());
			}
		}
		if (statuses != null && statuses.size() > 0)
			queryAppend(STATUS_FILTER);
		if (workspaces != null && workspaces.size() > 0)
			queryAppend(WORKSPACE_FILTER);
		// if(donors!=null && donors.size()>0) queryAppend(ORG_FILTER);
		if (sectors != null && sectors.size() != 0) {
			queryAppend(SECTOR_FILTER);
		}
		if (secondarySectors != null && secondarySectors.size() != 0) {
			queryAppend(SECONDARY_SECTOR_FILTER);
		}
        if (tertiarySectors != null && !tertiarySectors.isEmpty()) {
			queryAppend(TERTIARY_SECTOR_FILTER);
		}
        if (tagSectors != null && tagSectors.size() > 0 ) {
        	queryAppend(TAG_SECTOR_FILTER);
        }
		if (nationalPlanningObjectives != null
				&& nationalPlanningObjectives.size() != 0) {
			queryAppend(NATIONAL_PLAN_FILTER);
		}
		if (primaryPrograms != null && primaryPrograms.size() != 0) {
			queryAppend(PRIMARY_PROGRAM_FILTER);
		}
		if (secondaryPrograms != null && secondaryPrograms.size() != 0) {
			queryAppend(SECONDARY_PROGRAM_FILTER);
		}
		if (regions != null && regions.size() > 0)
			queryAppend(REGION_FILTER);
		if (financingInstruments != null && financingInstruments.size() > 0)
			queryAppend(FINANCING_INSTR_FILTER);
		if (risks != null && risks.size() > 0)
			queryAppend(RISK_FILTER);
		if ((lineMinRank != null) && (lineMinRank.size() > 0))
			queryAppend(LINE_MIN_RANK_FILTER);
		if ((planMinRank != null)&&(planMinRank.size() > 0))
			queryAppend(PLAN_MIN_RANK_FILTER);
		//if (regionSelected != null)
		//	queryAppend(REGION_SELECTED_FILTER);
		if (!REGION_SELECTED_FILTER.equals("")) {
			queryAppend(REGION_SELECTED_FILTER);
		}
		if(approvalStatusSelected!=null)
			queryAppend(ACTIVITY_STATUS);
		if (approved == true)
			queryAppend(APPROVED_FILTER);
		if (draft == true)
			queryAppend(DRAFT_FILTER);
		if (typeOfAssistance != null && typeOfAssistance.size() > 0)
			queryAppend(TYPE_OF_ASSISTANCE_FILTER);
		if (modeOfPayment != null && modeOfPayment.size() > 0)
			queryAppend(MODE_OF_PAYMENT_FILTER);
		if (projectCategory != null && projectCategory.size() > 0)
			queryAppend(PROJECT_CATEGORY_FILTER);
		
		if(projectImplementingUnits!=null && projectImplementingUnits.size() > 0){
			queryAppend(PROJECT_IMPL_UNIT_FILTER);
		}
		
		if (donorGroups != null && donorGroups.size() > 0)
			queryAppend(DONOR_GROUP_FILTER);
			
		if ((contractingAgencyGroups != null) && (contractingAgencyGroups.size() > 0))
			queryAppend(CONTRACTING_AGENCY_GROUP_FILTER);
			
		if (donorTypes != null && donorTypes.size() > 0)
			queryAppend(DONOR_TYPE_FILTER);

		if (executingAgency != null && executingAgency.size() > 0)
			queryAppend(EXECUTING_AGENCY_FILTER);

		if (contractingAgency != null && contractingAgency.size() > 0)
			queryAppend(CONTRACTING_AGENCY_FILTER);

		if (beneficiaryAgency != null && beneficiaryAgency.size() > 0)
			queryAppend(BENEFICIARY_AGENCY_FILTER);
		
		if (implementingAgency != null && implementingAgency.size() > 0)
			queryAppend(IMPLEMENTING_AGENCY_FILTER);

		if (donnorgAgency != null && donnorgAgency.size() > 0)
			queryAppend(DONNOR_AGENCY_FILTER);
		
		if (responsibleorg!=null && responsibleorg.size() >0){
			queryAppend(RESPONSIBLE_ORGANIZATION_FILTER);
		}
		
		if (actualAppYear!=null && actualAppYear!=-1) {
			queryAppend(ACTUAL_APPROVAL_YEAR_FILTER);
		}
		
		if (governmentApprovalProcedures != null) {
			String GOVERNMENT_APPROVAL_FILTER = "SELECT a.amp_activity_id from amp_activity a where governmentApprovalProcedures="
					+ ((governmentApprovalProcedures)?"true":"false");
			queryAppend(GOVERNMENT_APPROVAL_FILTER);
		}
		if (jointCriteria != null) {
			String JOINT_CRITERIA_FILTER = "SELECT a.amp_activity_id from amp_activity a where jointCriteria="
					+ ((jointCriteria)?"true":"false");;
			queryAppend(JOINT_CRITERIA_FILTER);
		}
		if (showArchived != null) {
			queryAppend(ARCHIVED_FILTER);
		}
		if ( multiDonor != null ) {
			queryAppend( MULTI_DONOR );
		}

		if ( this.isPublicView() && ! this.budgetExport  ){
			generatedFilterQuery = getOffLineQuery(generatedFilterQuery);
		}

		DbUtil.countActivitiesByQuery(this.generatedFilterQuery,indexedParams);
		logger.info(this.generatedFilterQuery);		
	}

	/**
	 * @return Returns the ampCurrencyCode.
	 */
	public AmpCurrency getCurrency() {
		return currency;
	}

	/**
	 * @param ampCurrencyCode
	 *            The ampCurrencyCode to set.
	 */
	public void setCurrency(AmpCurrency ampCurrencyCode) {
		this.currency = ampCurrencyCode;
	}

	public Set getFinancingInstruments() {
		return financingInstruments;
	}

	public void setFinancingInstruments(Set financingInstruments) {
		this.financingInstruments = financingInstruments;
	}

	public void setProjectCategory(Set<AmpCategoryValue> projectCategory) {
		this.projectCategory = projectCategory;
	}

	public Set<AmpCategoryValue> getProjectCategory() {
		return projectCategory;
	}

	/**
	 * @return Returns the sectors.
	 */
	@PropertyListableIgnore
	public Set getSectors() {
		return sectors;
	}

	/**
	 * @param sectors
	 *            The sectors to set.
	 */
	public void setSectors(Set sectors )  {
		this.sectors = sectors;
	}
	

	/**
	 * @return the sectorsAndAncestors
	 */
	@PropertyListableIgnore
	public Set getSectorsAndAncestors() {
		return sectorsAndAncestors;
	}

	/**
	 * @param sectorsAndAncestors the sectorsAndAncestors to set
	 */
	public void setSectorsAndAncestors(Set sectorsAndAncestors) {
		this.sectorsAndAncestors = sectorsAndAncestors;
	}

	/**
	 * @return Returns the generatedFilterQuery.
	 */
	@PropertyListableIgnore
	public String getGeneratedFilterQuery() {
		return generatedFilterQuery;
	}

	/**
	 * @return Returns the initialQueryLength.
	 */
	@PropertyListableIgnore
	public int getInitialQueryLength() {
		return initialQueryLength;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the calendarType.
	 */
	public AmpFiscalCalendar getCalendarType() {
		return calendarType;
	}

	/**
	 * @param calendarType
	 *            The calendarType to set.
	 */
	public void setCalendarType(AmpFiscalCalendar calendarType) {
		this.calendarType = calendarType;
	}

	/**
	 * @return Returns the donors.
	 */
	// public Set getDonors() {
	// return donors;
	// }
	/**
	 * @param donors
	 *            The donors to set.
	 */
	// public void setDonors(Set donors) {
	// this.donors = donors;
	// }
	/**
	 * @return Returns the regions.
	 */
	public Set getRegions() {
		return regions;
	}

	/**
	 * @param regions
	 *            The regions to set.
	 */
	public void setRegions(Set regions) {
		this.regions = regions;
	}

	/**
	 * @return Returns the statuses.
	 */
	public Set getStatuses() {
		return statuses;
	}

	/**
	 * @param statuses
	 *            The statuses to set.
	 */
	public void setStatuses(Set statuses) {
		this.statuses = statuses;
	}

	/**
	 * @return Returns the workspaces.
	 */
	public Set getWorkspaces() {
		return workspaces;
	}

	/**
	 * @param workspaces
	 *            The workspaces to set.
	 */
	public void setWorkspaces(Set workspaces) {
		this.workspaces = workspaces;
	}

	@PropertyListableIgnore
	public boolean isPublicView() {
		return publicView;
	}

	public void setPublicView(boolean publicView) {
		this.publicView = publicView;
	}

	@PropertyListableIgnore
	public boolean isWidget() {
		return widget;
	}

	public void setWidget(boolean widget) {
		this.widget = widget;
	}

	public Set getBudget() {
		return budget;
	}

	public void setBudget(Set budget) {
		this.budget = budget;
	}

	public Set getRisks() {
		return risks;
	}

	public void setRisks(Set risks) {
		this.risks = risks;
	}

	/**
	 * @return Returns the approvalStatus.
	 */

	/**
	 * provides a way to display this bean in HTML. Properties are automatically
	 * shown along with their values. CollectionS are unfolded and excluded
	 * properties (internally used) are not shown.
	 * 
	 * @see AmpARFilter.IGNORED_PROPERTIES
	 */
	public String toString() {
		StringBuffer ret = new StringBuffer();
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(AmpARFilter.class);
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				Method m = propertyDescriptors[i].getReadMethod();
				Object object = m.invoke(this, new Object[] {});
				if (object == null
						|| IGNORED_PROPERTIES.contains(propertyDescriptors[i]
								.getName()))
					continue;
				ret.append("<b>").append(propertyDescriptors[i].getName())
						.append(": ").append("</b>");
				if (object instanceof Collection)
					ret.append(Util.toCSString((Collection) object));

				else
					ret.append(object);
				if (i < propertyDescriptors.length)
					ret.append("; ");
			}
		} catch (IntrospectionException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return ret.toString();

	}

	private static final String IGNORED_PROPERTIES = "class#generatedFilterQuery#initialQueryLength#widget#publicView#ampReportId";

	public Collection<Integer> getLineMinRank() {
		return lineMinRank;
	}

	public void setLineMinRank(Collection<Integer> lineMinRank) {
		this.lineMinRank = lineMinRank;
	}

	public Collection<Integer> getPlanMinRank() {
		return planMinRank;
	}

	public void setPlanMinRank(Collection<Integer> planMinRank) {
		this.planMinRank = planMinRank;
	}

	@PropertyListableIgnore
	public Long getAmpReportId() {
		return ampReportId;
	}

	public void setAmpReportId(Long ampReportId) {
		this.ampReportId = ampReportId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text.trim().length() == 0)
			this.text = null;
		this.text = text;

	}

	public void setFromMonth(Integer fromMonth) {
		this.fromMonth = fromMonth;
	}

	public void setToMonth(Integer toMonth) {
		this.toMonth = toMonth;
	}

	public Integer getFromMonth() {
		return fromMonth;
	}

	public Integer getToMonth() {
		return toMonth;
	}

	public Integer getYearFrom() {
		return yearFrom;
	}

	public void setYearFrom(Integer fromYear) {
		this.yearFrom = fromYear;
	}

	public Integer getYearTo() {
		return yearTo;
	}

	public void setYearTo(Integer toYear) {
		this.yearTo = toYear;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public Boolean getGovernmentApprovalProcedures() {
		return governmentApprovalProcedures;
	}

	public void setGovernmentApprovalProcedures(
			Boolean governmentApprovalProcedures) {
		this.governmentApprovalProcedures = governmentApprovalProcedures;
	}

	public Boolean getJointCriteria() {
		return jointCriteria;
	}

	public void setJointCriteria(Boolean jointCriteria) {
		this.jointCriteria = jointCriteria;
	}

	@Override
	public String getBeanName() {
		return null;
	}

//	/**
//	 * @return the regionSelected
//	 */
//	public AmpCategoryValueLocations getRegionSelected() {
//		return regionSelected;
//	}
//
//	/**
//	 * @param regionSelected the regionSelected to set
//	 */
//	public void setRegionSelected(AmpCategoryValueLocations regionSelected) {
//		this.regionSelected = regionSelected;
//	}

	public String getIndexText() {
		return indexText;
	}

	@PropertyListableIgnore
	public boolean isApproved() {
		return approved;
	}

	public void setIndexText(String indexText) {
		this.indexText = indexText;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	@IgnorePersistence
	public boolean isDraft() {
		return draft;
	}
	/**
	 * TODO draft parameter needs to be renamed to hideDraft 
	 * @param draft
	 */
	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	public Set getDonorTypes() {
		return donorTypes;
	}

	public void setDonorTypes(Set donorTypes) {
		this.donorTypes = donorTypes;
	}

	public Set<AmpOrgGroup> getDonorGroups() {
		return donorGroups;
	}

	public void setDonorGroups(Set<AmpOrgGroup> donorGroups) {
		this.donorGroups = donorGroups;
	}

	public void setContractingAgencyGroups(Set<AmpOrgGroup> contractingAgencyGroups) {
		this.contractingAgencyGroups = contractingAgencyGroups;
	}
	
	public Set<AmpOrgGroup> getContractingAgencyGroups(){
		return this.contractingAgencyGroups;
	}
	
	public Set<AmpOrganisation> getBeneficiaryAgency() {
		return beneficiaryAgency;
	}

	public void setBeneficiaryAgency(Set<AmpOrganisation> beneficiaryAgency) {
		this.beneficiaryAgency = beneficiaryAgency;
	}

	public Set<AmpOrganisation> getExecutingAgency() {
		return executingAgency;
	}

	public void setExecutingAgency(Set<AmpOrganisation> executingAgency) {
		this.executingAgency = executingAgency;
	}

	public Set<AmpOrganisation> getContractingAgency() {
		return contractingAgency;
	}

	public void setContractingAgency(Set<AmpOrganisation> contractingAgency) {
		this.contractingAgency = contractingAgency;
	}

	public Set<AmpOrganisation> getImplementingAgency() {
		return implementingAgency;
	}

	public void setImplementingAgency(Set<AmpOrganisation> implementingAgency) {
		this.implementingAgency = implementingAgency;
	}

	public Set getSelectedSectors() {
		return selectedSectors;
	}

	public void setSelectedSectors(Set selectedSectors) {
		this.selectedSectors = selectedSectors;
	}

	@PropertyListableIgnore
	public Set getSecondarySectors() {
		return secondarySectors;
	}

	public void setSecondarySectors(Set secondarySectors) {
		this.secondarySectors = secondarySectors;
	}
	
	/**
	 * @return the secondarySectorsAndAncestors
	 */
	@PropertyListableIgnore
	public Set getSecondarySectorsAndAncestors() {
		return secondarySectorsAndAncestors;
	}

	/**
	 * @param secondarySectorsAndAncestors the secondarySectorsAndAncestors to set
	 */
	public void setSecondarySectorsAndAncestors(Set secondarySectorsAndAncestors) {
		this.secondarySectorsAndAncestors = secondarySectorsAndAncestors;
	}

	public Set getSelectedSecondarySectors() {
		return selectedSecondarySectors;
	}

	public void setSelectedSecondarySectors(Set selectedSecondarySectors) {
		this.selectedSecondarySectors = selectedSecondarySectors;
	}

       public Set getSelectedTertiarySectors() {
        return selectedTertiarySectors;
    }

    public void setSelectedTertiarySectors(Set selectedTertiarySectors) {
        this.selectedTertiarySectors = selectedTertiarySectors;
    }
    
    @PropertyListableIgnore
    public Set getTertiarySectors() {
        return tertiarySectors;
    }

    public void setTertiarySectors(Set tertiarySectors) {
        this.tertiarySectors = tertiarySectors;
    }
    @PropertyListableIgnore
    public Set getTertiarySectorsAndAncestors() {
        return tertiarySectorsAndAncestors;
    }

    public void setTertiarySectorsAndAncestors(Set tertiarySectorsAndAncestors) {
        this.tertiarySectorsAndAncestors = tertiarySectorsAndAncestors;
    }

	public Set<AmpCategoryValue> getTypeOfAssistance() {
		return typeOfAssistance;
	}

	public void setTypeOfAssistance(Set<AmpCategoryValue> typeOfAssistance) {
		this.typeOfAssistance = typeOfAssistance;
	}

	public Set<AmpCategoryValue> getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(Set<AmpCategoryValue> modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public Integer getRenderStartYear() {
		return renderStartYear;
	}

	public void setRenderStartYear(Integer renderStartYear) {
		this.renderStartYear = renderStartYear;
	}

	public Integer getRenderEndYear() {
		return renderEndYear;
	}

	public void setRenderEndYear(Integer renderEndYear) {
		this.renderEndYear = renderEndYear;
	}

	@PropertyListableIgnore
	public DecimalFormat getCurrentFormat() {
		return currentFormat;
	}

	public void setCurrentFormat(DecimalFormat currentFormat) {
		this.currentFormat = currentFormat;
		if ( this.currentFormat != null ) {
			FormatHelper.tlocal.set(currentFormat);
		}
	}

	/**
	 * returns the date in the {@link #sdfIn} format
	 */
	public String getFromDate() {
		return fromDate;
	}

	/**
	 * returns the fromDate as a Date object
	 * @return
	 */
	public Date buildFromDateAsDate()
	{
		if (fromDate == null || (fromDate.length() == 0))
			return null;
		try
		{
			return sdfIn.parse(fromDate);
		}
		catch(ParseException e)
		{
			logger.error("invalid date trickled into AmpARFilter::fromDate!", e); // SHOULD NOT HAPPEN!
			return null;
		}
	}
	
	/**
	 * returns the toDate as a Date object
	 * @return
	 */
	public Date buildToDateAsDate()
	{
		if (toDate == null || (toDate.length() == 0))
			return null;
		try
		{
			return sdfIn.parse(toDate);
		}
		catch(ParseException e)
		{
			logger.error("invalid date trickled into AmpARFilter::toDate!", e); // SHOULD NOT HAPPEN!
			return null;
		}
	}
	
	/**
	 * sets the date in the {@link #sdfIn} format. Will ignore call if fed incorrect data
	 */
	public void setFromDate(String fromDate) {
		if (!FormatHelper.isValidDateString(fromDate, sdfIn))
		{
			logger.error("tried to push invalidly-formatted date into AmpARFilter: " + fromDate, new RuntimeException());
			return;
		}
		this.fromDate = fromDate;
	}

	/**
	 * returns the date in the {@link #sdfIn} format
	 */
	public String getToDate() {
		return toDate;
	}

	/**
	 * sets the date in the {@link #sdfIn} format. Will ignore call if fed incorrect data
	 */
	public void setToDate(String toDate) {
		if (!FormatHelper.isValidDateString(fromDate, sdfIn))
		{
			logger.error("tried to push invalidly-formatted date into AmpARFilter: " + fromDate, new RuntimeException());
			return;
		}
		this.toDate = toDate;
	}
		

	/**
	 * @return the fromActivityStartDate
	 */
	public String getFromActivityStartDate() {
		return fromActivityStartDate;
	}

	/**
	 * @param fromActivityStartDate the fromActivityStartDate to set
	 */
	public void setFromActivityStartDate(String fromActivityStartDate) {
		this.fromActivityStartDate = fromActivityStartDate;
	}

	/**
	 * @return the toActivityStartDate
	 */
	public String getToActivityStartDate() {
		return toActivityStartDate;
	}

	/**
	 * @param toActivityStartDate the toActivityStartDate to set
	 */
	public void setToActivityStartDate(String toActivityStartDate) {
		this.toActivityStartDate = toActivityStartDate;
	}
	
	

	/**
	 * @return the fromActivityActualCompletionDate
	 */
	public String getFromActivityActualCompletionDate() {
		return fromActivityActualCompletionDate;
	}

	/**
	 * @param fromActivityActualCompletionDate the fromActivityActualCompletionDate to set
	 */
	public void setFromActivityActualCompletionDate(
			String fromActivityActualCompletionDate) {
		this.fromActivityActualCompletionDate = fromActivityActualCompletionDate;
	}

	/**
	 * @return the toActivityActualCompletionDate
	 */
	public String getToActivityActualCompletionDate() {
		return toActivityActualCompletionDate;
	}

	/**
	 * @param toActivityActualCompletionDate the toActivityActualCompletionDate to set
	 */
	public void setToActivityActualCompletionDate(
			String toActivityActualCompletionDate) {
		this.toActivityActualCompletionDate = toActivityActualCompletionDate;
	}
	

	/**
	 * @return the fromActivityFinalContractingDate
	 */
	public String getFromActivityFinalContractingDate() {
		return fromActivityFinalContractingDate;
	}

	/**
	 * @param fromActivityFinalContractingDate the fromActivityFinalContractingDate to set
	 */
	public void setFromActivityFinalContractingDate(
			String fromActivityFinalContractingDate) {
		this.fromActivityFinalContractingDate = fromActivityFinalContractingDate;
	}

	/**
	 * @return the toActivityFinalContractingDate
	 */
	public String getToActivityFinalContractingDate() {
		return toActivityFinalContractingDate;
	}

	/**
	 * @param toActivityFinalContractingDate the toActivityFinalContractingDate to set
	 */
	public void setToActivityFinalContractingDate(
			String toActivityFinalContractingDate) {
		this.toActivityFinalContractingDate = toActivityFinalContractingDate;
	}

	public Collection<String> getApprovalStatusSelected() {
		return approvalStatusSelected;
	}

	public void setApprovalStatusSelected(Collection<String> approvalStatusSelected) {
		this.approvalStatusSelected = approvalStatusSelected;
	}

	public Set<AmpOrganisation> getDonnorgAgency() {
		return donnorgAgency;
	}

	public void setDonnorgAgency(Set<AmpOrganisation> donnorgAgency) {
		this.donnorgAgency = donnorgAgency;
	}
	@IgnorePersistence
	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getTeamAccessType() {
		return teamAccessType;
	}

	public void setTeamAccessType(String teamAccessType) {
		this.teamAccessType = teamAccessType;
	}
	
	public boolean isWorkspaceonly() {
		return workspaceonly;
	}

	public void setWorkspaceonly(boolean workspaceonly) {
		this.workspaceonly = workspaceonly;
	}
	
	public boolean isJustSearch() {
		return justSearch;
	}

	public void setJustSearch(boolean justSearch) {
		this.justSearch = justSearch;
	}
	@PropertyListableIgnore
	public ArrayList<FilterParam> getIndexedParams() {
		return indexedParams;
	}

	public Set getResponsibleorg() {
		return responsibleorg;
	}

	public void setResponsibleorg(Set responsibleorg) {
		this.responsibleorg = responsibleorg;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public Boolean getSortByAsc() {
		return sortByAsc;
	}

	public void setSortByAsc(Boolean sortByAsc) {
		this.sortByAsc = sortByAsc;
	}

	public Collection<String> getHierarchySorters() {
		return hierarchySorters;
	}

	public void setHierarchySorters(Collection<String> hierarchySorters) {
		this.hierarchySorters = hierarchySorters;
	}

	public String getCRISNumber() {
		return CRISNumber;
	}

	public void setCRISNumber(String number) {
		CRISNumber = number;
	}

	public String getBudgetNumber() {
		return budgetNumber;
	}

	public void setBudgetNumber(String budgetNumber) {
		this.budgetNumber = budgetNumber;
	}

	public Integer getComputedYear() {
		return computedYear;
	}

	public void setComputedYear(Integer computedYear) {
		this.computedYear = computedYear;
	}

	public Integer getActualAppYear() {
		return actualAppYear;
	}

	public void setActualAppYear(Integer actualAppYear) {
		this.actualAppYear = actualAppYear;
	}

	/**
	 * @return the locationSelected
	 */
	public Collection<AmpCategoryValueLocations> getLocationSelected() {
		return locationSelected;
	}

	/**
	 * @param locationSelected the locationSelected to set
	 */
	public void setLocationSelected(
			Collection<AmpCategoryValueLocations> locationSelected) {
		this.locationSelected = locationSelected;
	}

	/**
	 * @return the unallocatedLocation
	 */
	public Boolean getUnallocatedLocation() {
		return unallocatedLocation;
	}

	/**
	 * @param unallocatedLocation the unallocatedLocation to set
	 */
	public void setUnallocatedLocation(Boolean unallocatedLocation) {
		this.unallocatedLocation = unallocatedLocation;
	}
	
	public Integer getAmountinthousand() {
		return amountinthousand;
	}

	public void setAmountinthousand(Integer amountinthousand) {
		this.amountinthousand = amountinthousand;
	}

	/**
	 * @return the relatedLocations
	 */
	@PropertyListableIgnore
	public Collection<AmpCategoryValueLocations> getRelatedLocations() {
		return relatedLocations;
	}

	/**
	 * @param relatedLocations the relatedLocations to set
	 */
	public void setRelatedLocations(
			Collection<AmpCategoryValueLocations> relatedLocations) {
		this.relatedLocations = relatedLocations;
	}
	
	public Collection<AmpCategoryValueLocations> getPledgesLocations() {
		return pledgesLocations;
	}

	public void setPledgesLocations(
			Collection<AmpCategoryValueLocations> pledgesLocations) {
		this.pledgesLocations = pledgesLocations;
	}

	public Set<AmpCategoryValue> getProjectImplementingUnits() {
		return projectImplementingUnits;
	}
	
	
	
	public void setProjectImplementingUnits(
			Set<AmpCategoryValue> projectImplementingUnits) {
		this.projectImplementingUnits = projectImplementingUnits;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

	public String getSearchMode() {
		return searchMode;
	}

	/**
	 * @return the showArchived
	 */
	public Boolean getShowArchived() {
		return showArchived;
	}

	/**
	 * @param showArchived
	 *            the showArchived to set
	 */
	public void setShowArchived(Boolean showArchived) {
		this.showArchived = showArchived;
	}
	
	@PropertyListableIgnore
	public String getFilterConditionOnly() {
		String genFilter = this.getGeneratedFilterQuery();
		int pos = genFilter.indexOf(initialFilterQuery);
		genFilter = genFilter.substring(pos + initialFilterQuery.length());
		pos = genFilter.indexOf("AND");
		genFilter = genFilter.substring(pos + 3);
		return genFilter;
	}
	
	@PropertyListableIgnore
	public String getOffLineQuery(String query) {
		String result = query;
		Pattern p = Pattern.compile(MoConstants.AMP_ACTIVITY_TABLE);
		Matcher m = p.matcher(result);
		result = m.replaceAll(MoConstants.CACHED_ACTIVITY_TABLE);
		return result;
	}

	/**
	 * @return the groupingsize
	 */
	public Integer getGroupingsize() {
		return groupingsize;
	}

	/**
	 * @param groupingsize the groupingsize to set
	 */
	public void setGroupingsize(Integer groupingsize) {
		this.groupingsize = groupingsize;
	}

	/**
	 * @return the customusegroupings
	 */
	public Boolean getCustomusegroupings() {
		return customusegroupings;
	}

	/**
	 * @param customusegroupings the customusegroupings to set
	 */
	public void setCustomusegroupings(Boolean customusegroupings) {
		this.customusegroupings = customusegroupings;
	}
	@PropertyListableIgnore
	public Set getTagSectors() {
		return tagSectors;
	}

	public void setTagSectors(Set tagSectors) {
		this.tagSectors = tagSectors;
	}

	public Set getSelectedTagSectors() {
		return selectedTagSectors;
	}

	public void setSelectedTagSectors(Set selectedTagSectors) {
		this.selectedTagSectors = selectedTagSectors;
	}
	@PropertyListableIgnore
	public Set getTagSectorsAndAncestors() {
		return tagSectorsAndAncestors;
	}

	public void setTagSectorsAndAncestors(Set tagSectorsAndAncestors) {
		this.tagSectorsAndAncestors = tagSectorsAndAncestors;
	}

	/*
	 * FIELD NOT USED
	public Set getAmpTeamsforpledges() {
		return ampTeamsforpledges;
	}

	public void setAmpTeamsforpledges(Set ampTeamsforpledges) {
		this.ampTeamsforpledges = ampTeamsforpledges;
	}
	 */
	
	/**
	 * effective team member - used for generating the TeamFilter
	 * equals currently logged-in user or, if missing, the AmpReport owner
	 * @return
	 */
	public Long getTeamMemberId()
	{
		return this.teamMemberId;
	}

	public void setTeamMemberId(Long teamMemberId)
	{
		this.teamMemberId =teamMemberId; 
	}
	
	public boolean getNeedsTeamFilter()
	{
		return needsTeamFilter;
	}
	
	private void setNeedsTeamFilter(boolean needs)
	{
		this.needsTeamFilter = needs;
	}
}
