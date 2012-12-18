/*
 * AmpTeam.java
 * Created: 03-Sep-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpSession;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.ar.dbentity.FilterDataSetInterface;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;

public class AmpReports implements Comparable, LoggerIdentifiable, Serializable, FilterDataSetInterface {

	private Long ampReportId;

	private Long id; // for logging

	//private AmpARFilter defaultFilter;
	
	private transient ReportGenerator reportGenerator;

	private String name;
	
	private String nametrimed;

	// private String description;
	private String reportDescription;

	private String options;

	private Boolean hideActivities;

	private Boolean drilldownTab;

	private Boolean publicReport;

	private Long type;

	// private AmpReportsOptions ampReportsOptions;
	private String description;

	private Set members;

	private Set<AmpReportColumn> columns;

	private List<AmpReportColumn> orderedColumns;

	private Set<AmpReportHierarchy> hierarchies;

	private Set<AmpReportMeasures> measures;

	private Set reportMeasures;

	private AmpTeamMember ownerId; // the member that created the report

	private Date updatedDate; // last date when the report was modified

	private String nameTrn;

	/*
	 *  to be set in order to get information for translation purposes in pdf and excel reports
	 *  not serialized
	 */
	private Long siteId;

	private String locale;

	//private AmpPages ampPage;

	private AmpCategoryValue activityLevel;

	private String user;
	
	private Set<AmpDesktopTabSelection> desktopTabSelections;
	
	private Set logs;
	
	private Set<AmpFilterData> filterDataSet;
	
	private Boolean allowEmptyFundingColumns;
	
	private Boolean budgetExporter = false;
	private Date publishedDate;
	
	private AmpCategoryValue reportCategory;

	// public static final String NOTE="NOTE: All shown funding items are in USD
	// currency. All calendaristic date cells are shown using DD/MM/YYYY format.
	// All amounts are in thousands.";
	// private static SimpleDateFormat dateFormat = new
	// SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);

	

	public Set getLogs() {
		return logs;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public void setLogs(Set logs) {
		this.logs = logs;
	}

	public String getFormatedUpdatedDate() {
		String result = null;
		if (this.updatedDate != null) {
			result = DateTimeUtil.formatDate(this.updatedDate);
		}
		return result;
	}

	public AmpTeamMember getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(AmpTeamMember owner) {
		this.ownerId = owner;
	}

	public static String getNote(HttpSession session) {
		int amountsUnitCode = Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));

		switch(amountsUnitCode)
		{
			case AmpARFilter.AMOUNT_OPTION_IN_UNITS:
				return "";

			case AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS:
				return "Amounts are in thousands (000)";

			case AmpARFilter.AMOUNT_OPTION_IN_MILLIONS:
				return "Amounts are in millions (000 000)";
				
			default:
				new RuntimeException("(Uberbug)").printStackTrace();				
				return "(BUG 3221321)";				
		}
	}

	public Set<AmpReportMeasures> getMeasures() {
		return measures;
	}

	public void setMeasures(Set<AmpReportMeasures> measures) {
		this.measures = measures;
	}

	public Long getAmpReportId() {
		return ampReportId;
	}

	public String getName() {
		return name;
	}

	/*
	 * public String getDescription() { return description; }
	 */

	public String getReportDescription() {
		return reportDescription;
	}

	public String getOptions() {
		return options;
	}

	/*
	 * public AmpReportsOptions getAmpReportsOptions() { return
	 * ampReportsOptions; }
	 */

	public Set getMembers() {
		return members;
	}

	public void setAmpReportId(Long ampReportId) {
		this.ampReportId = ampReportId;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * public void setDescription(String description) { this.description =
	 * description; }
	 */

	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	/*
	 * public void setAmpReportsOptions(AmpReportsOptions ampReportsOptions) {
	 * this.ampReportsOptions = ampReportsOptions; }
	 */

	public void setMembers(Set members) {
		this.members = members;
	}

	public int compareTo(Object o) {
		if (!(o instanceof AmpReports))
			throw new ClassCastException();

		AmpReports rep = (AmpReports) o;
		return (this.name.trim().toLowerCase().compareTo(rep.name.trim()
				.toLowerCase()));

	}



	public Set<AmpReportColumn> getShowAblesColumns(){
		Set<AmpReportColumn> finalColumns=new HashSet<AmpReportColumn>();
		if (hideActivities){
			Long order=1l;
			for (AmpReportColumn element : columns) {
				boolean add=ARUtil.hasHierarchy(this.getHierarchies(), element.getColumn().getColumnName()) || ARUtil.hasHeaderValue(element.getColumn());
				if (add){
					element.setOrderId(order);
					finalColumns.add(element);
					order ++;
				}
			}
		}else{
			finalColumns	= new TreeSet<AmpReportColumn>();
			finalColumns.addAll(columns);
		}	
		return finalColumns;
	}
	
	/**
	 * @return the columns
	 */
	public Set<AmpReportColumn> getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(Set<AmpReportColumn> columns) {
		this.columns = columns;
	}

	/**
	 * @return the hierarchies
	 */
	public Set<AmpReportHierarchy> getHierarchies() {
		return hierarchies;
	}

	/**
	 * @param hierarchies the hierarchies to set
	 */
	public void setHierarchies(Set<AmpReportHierarchy> hierarchies) {
		this.hierarchies = hierarchies;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	/**
	 * @return Returns the orderedColumns.
	 */
	public List<AmpReportColumn> getOrderedColumns() {
		return orderedColumns;
	}

	/**
	 * @param orderedColumns
	 *            The orderedColumns to set.
	 */
	public void setOrderedColumns(List<AmpReportColumn> orderedColumns) {
		this.orderedColumns = orderedColumns;
	}

	/**
	 * @return Returns the defaultFilter.
	 */
//	public AmpARFilter getDefaultFilter() {
//		return defaultFilter;
//	}

	/**
	 * @param defaultFilter
	 *            The defaultFilter to set.
	 */
//	public void setDefaultFilter(AmpARFilter defaultFilter) {
//		this.defaultFilter = defaultFilter;
//	}

	/**
	 * @return Returns the hideActivities.
	 */
	public Boolean isHideActivities() {
		return hideActivities;
	}

	/**
	 * @param hideActivities
	 *            The hideActivities to set.
	 */
	public void setHideActivities(Boolean hideActivities) {
		this.hideActivities = hideActivities;
	}

	/**
	 * @return Returns the hideActivities.
	 */
	public Boolean getHideActivities() {
		return hideActivities;
	}

	public Boolean getDrilldownTab() {
		return drilldownTab;
	}

	public void setDrilldownTab(Boolean drilldownTab) {
		this.drilldownTab = drilldownTab;
	}

	public Boolean getPublicReport() {
		return publicReport;
	}

	public void setPublicReport(Boolean publicReport) {
		this.publicReport = publicReport;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Object getObjectType() {
		// TODO Auto-generated method stub
		return this.getClass().getName();
	}

	public Object getIdentifier() {
		// TODO Auto-generated method stub
		return this.getAmpReportId().toString();
	}

	public Long getId() {
		return this.getAmpReportId();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObjectName() {
		// TODO Auto-generated method stub
		return this.getName();
	}

	public String getNameTrn() {
		return this.name.toLowerCase().replaceAll(" ", "");
	}

	public void setNameTrn(String nameTrn) {
		this.nameTrn = nameTrn;
	}

	public Set getReportMeasures() {
		return reportMeasures;
	}

	public void setReportMeasures(Set reportMeasures) {
		this.reportMeasures = reportMeasures;
	}

	public AmpCategoryValue getActivityLevel() {
		return activityLevel;
	}

	public void setActivityLevel(AmpCategoryValue activityLevel) {
		this.activityLevel = activityLevel;
	}

	
//	public void setAmpPage(AmpPages ampPage) {
//		this.ampPage = ampPage;
//	}
//
//	public AmpPages getAmpPage() {
//		return ampPage;
//	}
	

	/**
	 * @return the budgetExporter
	 */
	public Boolean getBudgetExporter() {
		return budgetExporter;
	}

	/**
	 * @param budgetExporter the budgetExporter to set
	 */
	public void setBudgetExporter(Boolean budgetExporter) {
		this.budgetExporter = budgetExporter;
	}

	public Set<AmpDesktopTabSelection> getDesktopTabSelections() {
		return desktopTabSelections;
	}

	public void setDesktopTabSelections(
			Set<AmpDesktopTabSelection> desktopTabSelections) {
		this.desktopTabSelections = desktopTabSelections;
	}
	

	@Override
	public AmpFilterData newAmpFilterData(FilterDataSetInterface filterRelObj,
			String propertyName, String propertyClassName,
			String elementClassName, String value) {
		return new AmpFilterData(filterRelObj, propertyName, propertyClassName, elementClassName, value);
	}
	@Override
	public Set<AmpFilterData> getFilterDataSet() {
		return filterDataSet;
	}
	@Override
	public void setFilterDataSet(Set filterDataSet) {
		this.filterDataSet = filterDataSet;
	}

	public String getUser() {
		if (ownerId!= null) {
			user = ownerId.getUser().getFirstNames();
			return user;
		}
		return "";
	}

	public int getMeasureOrder(String measurName) {
		Set<AmpReportMeasures> locMeasures = this.getMeasures();
		for (AmpReportMeasures m : locMeasures) {
			if(m.getMeasure().getMeasureName().equals(measurName)){
				return m.getOrder();
			}	
		}
		return 0;
	}
	
	  public static Comparator UpdatedDateComparator = new Comparator() {
		    public int compare(Object report, Object anotherReport) {
		      Date firstDate = ((AmpReports)report).getUpdatedDate();
		      Date secondDate = ((AmpReports)anotherReport).getUpdatedDate();
		      return firstDate.compareTo(secondDate);
		    }
		  };
	
	  public static Comparator lexicographicComparator = new Comparator() {
		    public int compare(Object report, Object anotherReport) {
		      return
		      	((AmpReports)report).getName().compareToIgnoreCase( ((AmpReports)anotherReport).getName() );
		    }
		  };



	public String getNametrimed() {
		return this.name.replace("'","\\'");
	}

	public void setNametrimed(String nametrimed) {
		this.nametrimed = nametrimed;
	}

	public Boolean getAllowEmptyFundingColumns() {
		return allowEmptyFundingColumns;
	}

	public void setAllowEmptyFundingColumns(Boolean allowEmptyFundingColumns) {
		this.allowEmptyFundingColumns = allowEmptyFundingColumns;
	}
	
	public int getNumOfHierarchies() {
			int ret	= 0;
			if ( this.hierarchies != null )
				ret	= this.hierarchies.size();
			return ret;
	}
	
	public String getHierarchiesPath() {
		String ret	= "";
		try{
			if ( this.getHierarchies() != null ) {
				for (AmpReportHierarchy arh: this.getHierarchies() ) {
					String colName			= arh.getColumn().getColumnName();
					String translColName	= TranslatorWorker.translateText(colName, this.locale, this.siteId);
					ret						+= " / " + translColName;
				}
				ret		+= " /";
				if ( ret.length() > 0 )
					ret		= ret.substring(1);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
         @Override
        public String getObjectFilteredName() {
		return DbUtil.filter(getObjectName());
	}

		public AmpCategoryValue getReportCategory() {
			return reportCategory;
		}

		public void setReportCategory(AmpCategoryValue reportCategory) {
			this.reportCategory = reportCategory;
		}
		
	public ReportGenerator getReportGenerator()
	{
		return this.reportGenerator;
	}
	
	public void setReportGenerator(ReportGenerator generator)
	{
		this.reportGenerator = generator;
	}
}