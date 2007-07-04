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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

/**
 * Filtering bean. Holds info about filtering parameters and creates the filtering query
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 5, 2006
 *
 */
public class AmpARFilter implements Filter {
	
	protected static Logger logger = Logger.getLogger(AmpARFilter.class);
	private Long id;
	private Long ampReportId;
	private Set statuses=null;
	private Set donors=null;
	private Set sectors=null;
	private Set regions=null;
	private Set risks=null;
	private Long ampModalityId=null;
	private AmpCurrency currency=null;
	private Set ampTeams=null;
	private AmpFiscalCalendar calendarType=null;
	private AmpPerspective perspective;
	private boolean widget=false;
	private boolean publicView=false;
	private Boolean budget=null;
	private Integer lineMinRank;
	private Integer planMinRank;
	private Integer fromYear;
	private Integer toYear;	
	
	private String pageSize; // to be used for exporting reports
	
	private String text;
	
	private static final String initialFilterQuery="SELECT distinct(amp_activity_id) FROM amp_activity WHERE 1";
	private String generatedFilterQuery;
	private int initialQueryLength=initialFilterQuery.length();
	
	private void queryAppend(String filter) {
		//generatedFilterQuery+= (initialQueryLength==generatedFilterQuery.length()?"":" AND ") + " amp_activity_id IN ("+filter+")";
		generatedFilterQuery+= " AND amp_activity_id IN ("+filter+")";
	}
	
	public void readRequestData(HttpServletRequest request) {
		this.generatedFilterQuery=initialFilterQuery;
		String perspective=null;
		TeamMember tm = (TeamMember) request.getSession()
		.getAttribute(Constants.CURRENT_MEMBER);
		
		if(tm!=null) {perspective = tm.getAppSettings().getPerspective();
		if (perspective.equals("Donor"))
			perspective = "DN";
		if (perspective.equals("MOFED"))
			perspective = "MA";}
		else perspective="MA";

		this.setPerspective(DbUtil.getPerspective(perspective));

		String widget=(String) request.getAttribute("widget");
		if(widget!=null) this.setWidget(new Boolean(widget).booleanValue());
		
		String ampReportId = request.getParameter("ampReportId");
		if(ampReportId==null) {
			AmpReports ar=(AmpReports) request.getSession().getAttribute("reportMeta");
			ampReportId=ar.getAmpReportId().toString();
		}			
			
		this.setAmpReportId(new Long(ampReportId));
		
	}
	

	public AmpARFilter() {
		super();	
		this.generatedFilterQuery=initialFilterQuery;
	}
	
	public void generateFilterQuery() {
		String BUDGET_FILTER="SELECT amp_activity_id FROM amp_activity WHERE budget="+(budget!=null?budget.toString():"null")+(budget!=null && budget.booleanValue()==false?" OR budget is null":"");
		String TEAM_FILTER="SELECT amp_activity_id FROM amp_activity WHERE amp_team_id IN ("+Util.toCSString(ampTeams,true)+") OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("+Util.toCSString(ampTeams,true)+") )";
		String STATUS_FILTER="SELECT amp_activity_id FROM v_status WHERE amp_status_id IN ("+Util.toCSString(statuses,true)+")";
		String ORG_FILTER = "SELECT amp_activity_id FROM v_donors WHERE amp_donor_org_id IN ("+Util.toCSString(donors,true)+")";
		String SECTOR_FILTER="SELECT amp_activity_id FROM v_sectors WHERE amp_sector_id IN ("+Util.toCSString(sectors,true)+")";
		String REGION_FILTER="SELECT amp_activity_id FROM v_regions WHERE name IN ("+Util.toCSString(regions,true)+")";
		String FINANCING_INSTR_FILTER="SELECT amp_activity_id FROM v_financing_instrument WHERE amp_modality_id='"+ampModalityId+"'";
		String LINE_MIN_RANK_FILTER="SELECT amp_activity_id FROM amp_activity WHERE line_min_rank="+lineMinRank;
		String PLAN_MIN_RANK_FILTER="SELECT amp_activity_id FROM amp_activity WHERE plan_min_rank="+planMinRank;
		
		String TEXT_FILTER = "SELECT a.amp_activity_id FROM amp_activity a WHERE a.name LIKE '%"+text+"%'" +
				" UNION SELECT b.amp_activity_id FROM amp_activity b, dg_editor e where b.description = e.editor_key AND e.body LIKE '%"+text+"%'"+
				" UNION SELECT b.amp_activity_id FROM amp_activity b, dg_editor e where b.objectives = e.editor_key AND e.body LIKE '%"+text+"%'"+
				" UNION SELECT b.amp_activity_id FROM amp_activity b, dg_editor e where b.purpose = e.editor_key AND e.body LIKE '%"+text+"%'"+
				" UNION SELECT b.amp_activity_id FROM amp_activity b, dg_editor e where b.results = e.editor_key AND e.body LIKE '%"+text+"%'";
				
	
		String RISK_FILTER="SELECT v.activity_id from AMP_ME_INDICATOR_VALUE v, AMP_INDICATOR_RISK_RATINGS r where v.risk=r.amp_ind_risk_ratings_id and r.amp_ind_risk_ratings_id in ("+Util.toCSString(risks,true)+")";
		
		
		if(budget!=null) queryAppend(BUDGET_FILTER);
		if(ampTeams!=null && ampTeams.size()>0) queryAppend(TEAM_FILTER);
		if(statuses!=null && statuses.size()>0) queryAppend(STATUS_FILTER);
		if(donors!=null && donors.size()>0) queryAppend(ORG_FILTER);
		if(sectors!=null && sectors.size()!=0) queryAppend(SECTOR_FILTER);
		if(regions!=null && regions.size()>0) queryAppend(REGION_FILTER);
		if(ampModalityId!=null && ampModalityId.intValue()!=0) queryAppend(FINANCING_INSTR_FILTER);
		if(risks!=null && risks.size()>0) queryAppend(RISK_FILTER);
		if(lineMinRank!=null) queryAppend(LINE_MIN_RANK_FILTER);
		if(planMinRank!=null) queryAppend(PLAN_MIN_RANK_FILTER);
		if(text!=null) queryAppend(TEXT_FILTER);
		
	}
	
	
	/**
	 * @return Returns the ampCurrencyCode.
	 */
	public AmpCurrency getCurrency() {
		return currency;
	}


	/**
	 * @param ampCurrencyCode The ampCurrencyCode to set.
	 */
	public void setCurrency(AmpCurrency ampCurrencyCode) {
		this.currency = ampCurrencyCode;
	}


	/**
	 * @return Returns the ampModalityId.
	 */
	public Long getAmpModalityId() {
		return ampModalityId;
	}


	/**
	 * @param ampModalityId The ampModalityId to set.
	 */
	public void setAmpModalityId(Long ampModalityId) {
		this.ampModalityId = ampModalityId;
	}

 

	/**
	 * @return Returns the sectors.
	 */
	public Set getSectors() {
		return sectors;
	}

	/**
	 * @param sectors The sectors to set.
	 */
	public void setSectors(Set sectors) {
		this.sectors = sectors;
	}


	/**
	 * @return Returns the generatedFilterQuery.
	 */
	public String getGeneratedFilterQuery() {
		return generatedFilterQuery;
	}

	/**
	 * @return Returns the initialQueryLength.
	 */
	public int getInitialQueryLength() {
		return initialQueryLength;
	}

	/**
	 * @return Returns the ampTeams.
	 */
	public Set getAmpTeams() {
		return ampTeams;
	}

	/**
	 * @param ampTeams The ampTeams to set.
	 */
	public void setAmpTeams(Set ampTeams) {
		this.ampTeams = ampTeams;
	}



	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
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
	 * @param calendarType The calendarType to set.
	 */
	public void setCalendarType(AmpFiscalCalendar calendarType) {
		this.calendarType = calendarType;
	}

	/**
	 * @return Returns the donors.
	 */
	public Set getDonors() {
		return donors;
	}

	/**
	 * @param donors The donors to set.
	 */
	public void setDonors(Set donors) {
		this.donors = donors;
	}

	/**
	 * @return Returns the regions.
	 */
	public Set getRegions() {
		return regions;
	}

	/**
	 * @param regions The regions to set.
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
	 * @param statuses The statuses to set.
	 */
	public void setStatuses(Set statuses) {
		this.statuses = statuses;
	}

	/**
	 * @return Returns the perspectiveCode.
	 */
	public AmpPerspective getPerspective() {
		return perspective;
	}

	/**
	 * @param perspectiveCode The perspectiveCode to set.
	 */
	public void setPerspective(AmpPerspective perspective) {
		this.perspective = perspective;
	}

	public boolean isPublicView() {
		return publicView;
	}

	public void setPublicView(boolean publicView) {
		this.publicView = publicView;
	}

	public boolean isWidget() {
		return widget;
	}

	public void setWidget(boolean widget) {
		this.widget = widget;
	}

	public Boolean getBudget() {
		return budget;
	}

	public void setBudget(Boolean budget) {
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
	 * provides a way to display this bean in HTML. Properties are automatically shown along with their values. CollectionS are unfolded and
	 * excluded properties (internally used) are not shown.
	 * @see AmpARFilter.IGNORED_PROPERTIES
	 */
	public String toString() {
		StringBuffer ret=new StringBuffer();
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(AmpARFilter.class);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			Method m=propertyDescriptors[i].getReadMethod();
			Object object = m.invoke(this,new Object[]{});
			if(object==null || IGNORED_PROPERTIES.contains(propertyDescriptors[i].getName())) continue;			
			ret.append("<b>").append(propertyDescriptors[i].getName()).append(": ").append("</b>");
			if(object instanceof Collection) 
				ret.append(Util.toCSString((Collection) object,false));
			
			else ret.append(object);
			if(i<propertyDescriptors.length) ret.append("; ");
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

	private static final String IGNORED_PROPERTIES="class#generatedFilterQuery#initialQueryLength#widget#publicView#ampReportId";

	public Integer getLineMinRank() {
		return lineMinRank;
	}

	public void setLineMinRank(Integer lineMinRank) {
		this.lineMinRank = lineMinRank;
	}

	public Integer getPlanMinRank() {
		return planMinRank;
	}

	public void setPlanMinRank(Integer planMinRank) {
		this.planMinRank = planMinRank;
	}

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
		this.text = text;
	}

	public Integer getFromYear() {
		return fromYear;
	}

	public void setFromYear(Integer fromYear) {
		this.fromYear = fromYear;
	}

	public Integer getToYear() {
		return toYear;
	}

	public void setToYear(Integer toYear) {
		this.toYear = toYear;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	} 
}
