package org.digijava.module.aim.form;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancialFilters;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class MainProjectDetailsForm extends ValidatorForm
{
	private String ampId;
	private String name ;
	private String description ;
	private String objectives;
	private String channelOverviewTabColor ;
	private String financialProgressTabColor ;
	private String physicalProgressTabColor ;
	private String documentsTabColor ;
	private long ampActivityId ;
	private int tabIndex ;
	private int flag;
	private long ampFundingId ;
	private String currency ;
	private String perspective ;
	private String perpsectiveName;
	private long fiscalCalId ;
	private int fromYear ;
	private int toYear ;
	private Collection years ;
	private Collection currencies;
	private Collection perspectives;
	private boolean calendarPresent;
	private boolean currencyPresent;
	private boolean perspectivePresent;
	private boolean yearRangePresent;
	private boolean goButtonPresent;
	private boolean sessionExpired;
	private String type;
	
	/**
	 * @return
	 */
	public long getAmpActivityId() {
		return ampActivityId;
	}

	/**
	 * @param l
	 */
	public void setAmpActivityId(long l) {
		ampActivityId = l;
	}

	/**
	 * @return
	 */
	public int getTabIndex() {
		return tabIndex;
	}

	/**
	 * @param i
	 */
	public void setTabIndex(int i) {
		tabIndex = i;
	}
	
	public String getName() 
	{
		return name;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description ;
	}

	public void setName(String name) 
	{
		this.name = name ;
	}
	/**
	 * @return
	 */
	public String getChannelOverviewTabColor() {
		return channelOverviewTabColor;
	}

	/**
	 * @return
	 */
	public String getDocumentsTabColor() {
		return documentsTabColor;
	}

	/**
	 * @return
	 */
	public String getFinancialProgressTabColor() {
		return financialProgressTabColor;
	}

	/**
	 * @return
	 */
	public String getPhysicalProgressTabColor() {
		return physicalProgressTabColor;
	}

	/**
	 * @param string
	 */
	public void setChannelOverviewTabColor(String string) {
		channelOverviewTabColor = string;
	}

	/**
	 * @param string
	 */
	public void setDocumentsTabColor(String string) {
		documentsTabColor = string;
	}

	/**
	 * @param string
	 */
	public void setFinancialProgressTabColor(String string) {
		financialProgressTabColor = string;
	}

	/**
	 * @param string
	 */
	public void setPhysicalProgressTabColor(String string) {
		physicalProgressTabColor = string;
	}
	/**
	 * @return
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param i
	 */
	public void setFlag(int i) {
		flag = i;
	}

	/**
	 * @return
	 */
	public long getAmpFundingId() {
		return ampFundingId;
	}

	/**
	 * @param l
	 */
	public void setAmpFundingId(long l) {
		ampFundingId = l;
	}

	/**
	 * @return
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @return
	 */
	public long getFiscalCalId() {
		return fiscalCalId;
	}

	/**
	 * @return
	 */
	public int getFromYear() {
		return fromYear;
	}

	/**
	 * @return
	 */
	public String getPerspective() {
		return perspective;
	}

	/**
	 * @return
	 */
	public int getToYear() {
		return toYear;
	}

	/**
	 * @return
	 */
	public Collection getYears() {
		return years;
	}

	/**
	 * @param string
	 */
	public void setCurrency(String string) {
		currency = string;
	}

	/**
	 * @param l
	 */
	public void setFiscalCalId(long l) {
		fiscalCalId = l;
	}

	/**
	 * @param i
	 */
	public void setFromYear(int i) {
		fromYear = i;
	}

	/**
	 * @param string
	 */
	public void setPerspective(String string) {
		perspective = string;
	}

	/**
	 * @param i
	 */
	public void setToYear(int i) {
		toYear = i;
	}

	/**
	 * @param collection
	 */
	public void setYears(Collection collection) {
		years = collection;
	}

	/**
	 * @return
	 */
	public Collection getCurrencies() {
		return currencies;
	}

	/**
	 * @param collection
	 */
	public void setCurrencies(Collection collection) {
		currencies = collection;
	}
	
	public ActionErrors validate(ActionMapping actionMapping,
								  HttpServletRequest httpServletRequest) {
	   ActionErrors errors = super.validate(actionMapping, httpServletRequest);
	   HttpSession session = httpServletRequest.getSession();
	   TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
	   if ( teamMember != null )	{
			  FinancialFilters ff = CommonWorker.getFilters(teamMember.getTeamId(),"FP");
			  setCalendarPresent(ff.isCalendarPresent());
			  setCurrencyPresent(ff.isCurrencyPresent());
			  setPerspectivePresent(ff.isPerspectivePresent());
			  setYearRangePresent(ff.isYearRangePresent());
			  setGoButtonPresent(ff.isGoButtonPresent());
			  FilterParams fp = new FilterParams();
			  ApplicationSettings apps = null;
			  if ( teamMember != null )	{
				  apps = teamMember.getAppSettings();
			  }

			  if ( getCurrency() != null )
				  fp.setCurrencyCode(getCurrency());
			  else	{
				  Currency curr = CurrencyUtil.getCurrency(apps.getCurrencyId());
				  if (curr != null) {
				  		fp.setCurrencyCode(curr.getCurrencyCode());
				  }
			  }

			  if ( getFiscalCalId() != 0 )
				  fp.setFiscalCalId(new Long( getFiscalCalId() ));
			  else	{
				  fp.setFiscalCalId(apps.getFisCalId());
			  }

			  if ( getPerspective() != null )
				  fp.setPerspective(getPerspective());
			  else	{
				  String perspective = CommonWorker.getPerspective(apps.getPerspective());
				  fp.setPerspective(perspective);
			  }

			  if ( getFromYear()==0 || getToYear()==0 )	{
				int year = new GregorianCalendar().get(Calendar.YEAR);
				fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
				fp.setToYear(year+Constants.TO_YEAR_RANGE);
			  }
			  else	{
				  fp.setToYear(getToYear());
				  fp.setFromYear(getFromYear());
			  }
			  setYears(YearUtil.getYears());
			  setCurrencies(CurrencyUtil.getAllCurrencies(1));		
	   }
	   return errors.isEmpty() ? null : errors;
	 }
	/**
	 * @return
	 */
	public boolean isCalendarPresent() {
		return calendarPresent;
	}

	/**
	 * @return
	 */
	public boolean isCurrencyPresent() {
		return currencyPresent;
	}

	/**
	 * @return
	 */
	public boolean isPerspectivePresent() {
		return perspectivePresent;
	}

	/**
	 * @return
	 */
	public boolean isYearRangePresent() {
		return yearRangePresent;
	}

	/**
	 * @param b
	 */
	public void setCalendarPresent(boolean b) {
		calendarPresent = b;
	}

	/**
	 * @param b
	 */
	public void setCurrencyPresent(boolean b) {
		currencyPresent = b;
	}

	/**
	 * @param b
	 */
	public void setPerspectivePresent(boolean b) {
		perspectivePresent = b;
	}

	/**
	 * @param b
	 */
	public void setYearRangePresent(boolean b) {
		yearRangePresent = b;
	}

	/**
	 * @return
	 */
	public boolean isGoButtonPresent() {
		return goButtonPresent;
	}

	/**
	 * @param b
	 */
	public void setGoButtonPresent(boolean b) {
		goButtonPresent = b;
	}

	/**
	 * @return
	 */
	public boolean isSessionExpired() {
		return sessionExpired;
	}

	/**
	 * @param b
	 */
	public void setSessionExpired(boolean b) {
		sessionExpired = b;
	}

	/**
	 * @return Returns the ampId.
	 */
	public String getAmpId() {
		return ampId;
	}
	/**
	 * @param ampId The ampId to set.
	 */
	public void setAmpId(String ampId) {
		this.ampId = ampId;
	}
    /**
     * @return Returns the objectives.
     */
    public String getObjectives() {
        return objectives;
    }
    /**
     * @param objectives The objectives to set.
     */
    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

	/**
	 * @return Returns the perspectives.
	 */
	public Collection getPerspectives() {
		return perspectives;
	}

	/**
	 * @param perspectives The perspectives to set.
	 */
	public void setPerspectives(Collection perspectives) {
		this.perspectives = perspectives;
	}

	/**
	 * @return Returns the perpsectiveName.
	 */
	public String getPerpsectiveName() {
		return perpsectiveName;
	}

	/**
	 * @param perpsectiveName The perpsectiveName to set.
	 */
	public void setPerpsectiveName(String perpsectiveName) {
		this.perpsectiveName = perpsectiveName;
	}
}
