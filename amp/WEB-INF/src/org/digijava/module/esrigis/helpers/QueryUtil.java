package org.digijava.module.esrigis.helpers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.taglibs.standard.tag.rt.fmt.FormatNumberTag;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.visualization.util.Constants;
import org.springframework.beans.BeanWrapperImpl;

import fi.joensuu.joyds1.calendar.EthiopicCalendar;
import fi.joensuu.joyds1.calendar.NepaliCalendar;

public class QueryUtil {
	 public static final BigDecimal ONEHUNDERT = new BigDecimal(100);
	 
	public static Date getStartDate(Long fiscalCalendarId, int year) {
		Date startDate = null;
		if (fiscalCalendarId != null && fiscalCalendarId != -1) {
			AmpFiscalCalendar calendar = FiscalCalendarUtil
					.getAmpFiscalCalendar(fiscalCalendarId);
			if (calendar.getBaseCal().equalsIgnoreCase("GREG-CAL")) {
				startDate = getStartOfYear(year,
						calendar.getStartMonthNum() - 1,
						calendar.getStartDayNum());
			} else {
				startDate = getGregorianCalendarDate(calendar, year, true);
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.YEAR, year);
			startDate = cal.getTime();
		}
		return startDate;
	}

	public static Date getStartOfYear(int year, int month, int day) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, month, day, 0, 0, 0);
		return cal.getTime();
	}

	public static Date getGregorianCalendarDate(
			AmpFiscalCalendar fiscalCalendar, int year, boolean startDate) {
		Date date;
		fi.joensuu.joyds1.calendar.Calendar calendar = getCalendar(
				fiscalCalendar, startDate, year);
		Calendar gregorianCal = calendar.toJavaUtilGregorianCalendar();
		date = gregorianCal.getTime();
		return date;
	}

	public static Date getEndDate(Long fiscalCalendarId, int year) {
		Date endDate = null;
		if (fiscalCalendarId != null && fiscalCalendarId != -1) {
			AmpFiscalCalendar calendar = FiscalCalendarUtil
					.getAmpFiscalCalendar(fiscalCalendarId);
			if (calendar.getBaseCal().equalsIgnoreCase("GREG-CAL")) {
				// we need data including the last day of toYear,this is till
				// the first day of toYear+1
				int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
				endDate = new Date(getStartOfYear(year + 1,
						calendar.getStartMonthNum() - 1,
						calendar.getStartDayNum()).getTime()
						- MILLISECONDS_IN_DAY);
			} else {
				endDate = getGregorianCalendarDate(calendar, year, false);
			}

		} else {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.YEAR, year + 1);
			endDate = cal.getTime();
		}
		return endDate;
	}

	public static fi.joensuu.joyds1.calendar.Calendar getCalendar(
			AmpFiscalCalendar fiscalCalendar, boolean startDate, int year) {
		fi.joensuu.joyds1.calendar.Calendar calendar = null;
		String calendarType = fiscalCalendar.getBaseCal();
		if (calendarType.equals("ETH-CAL")) {
			calendar = new EthiopicCalendar();
		} else {
			if (calendarType.equals("NEP-CAL")) {
				calendar = new NepaliCalendar();
			}
		}
		if (startDate) {
			calendar.set(year, fiscalCalendar.getStartMonthNum(),
					fiscalCalendar.getStartDayNum());
		} else {
			calendar.set(year + 1, fiscalCalendar.getStartMonthNum(),
					fiscalCalendar.getStartDayNum());
			calendar.addDays(-1);
		}
		return calendar;
	}

	public static String getOrganizationQuery(boolean orgGroupView, Long[] selectedOrganizations, Long[] selectedOrgGroups) {
		String qry = "";
		if (orgGroupView) {
			qry = " and  f.ampDonorOrgId.orgGrpId.ampOrgGrpId in ("
					+ getInStatement(selectedOrgGroups) + ") ";
		} else {
			qry = " and f.ampDonorOrgId in ("
					+ getInStatement(selectedOrganizations) + ") ";
		}
		return qry;
	}
	
	public static String getOrganizationTypeQuery(boolean orgTypeView, Long[] selectedOrganizations, Long[] selectedtypes) {
		String qry = "";
		if (orgTypeView) {
			qry = " and  f.ampDonorOrgId.orgGrpId.orgType in ("+ getInStatement(selectedtypes) + ") ";
		} else {
			qry = " and f.ampDonorOrgId in ("+ getInStatement(selectedOrganizations) + ") ";
		}
		return qry;
	}

	
	public static String getInStatement(Long ids[]) {
		String oql = "";
		for (int i = 0; i < ids.length; i++) {
			oql += "" + ids[i];
			if (i < ids.length - 1) {
				oql += ",";
			}
		}
		return oql;
	}

	public static String getTeamQueryManagement() {
		String qr = "";
		qr += " and act.draft=false and act.approvalStatus ='approved' ";
		qr += " and act.team is not null and act.team in (select at.ampTeamId from "
				+ AmpTeam.class.getName()
				+ " at where parentTeamId is not null)";

		return qr;
	}
	
	  public static void getTeams(AmpTeam team, List<AmpTeam> teams) {
	        teams.add(team);
	        Collection<AmpTeam> childrenTeams =  TeamUtil.getAllChildrenWorkspaces(team.getAmpTeamId());
	        if (childrenTeams != null) {
	            for (AmpTeam tm : childrenTeams) {
	                getTeams(tm, teams);
	            }
	        }
	    }


	public static String getTeamQuery(TeamMember teamMember) {
		String qr = "";
		if (teamMember != null) {
			AmpTeam team = TeamUtil.getAmpTeam(teamMember.getTeamId());
			List<AmpTeam> teams = new ArrayList<AmpTeam>();
			getTeams(team, teams);
			String relatedOrgs = "";
			String teamIds = "";
			if (teamMember.getTeamAccessType().equals("Management")) {
				qr += " and act.draft=false and act.approvalStatus ='approved' ";
			}
			qr += " and (";
			for (AmpTeam tm : teams) {
				if (tm.getComputation() != null && tm.getComputation()) {
					relatedOrgs += getComputationOrgsQry(tm);
				}
				teamIds += tm.getAmpTeamId() + ",";

			}
			if (teamIds.length() > 1) {
				teamIds = teamIds.substring(0, teamIds.length() - 1);
				qr += " act.team.ampTeamId in ( " + teamIds + ")";

			}
			if (relatedOrgs.length() > 1) {
				relatedOrgs = relatedOrgs
						.substring(0, relatedOrgs.length() - 1);
				qr += " or f.ampDonorOrgId in(" + relatedOrgs + ")";
			}
			qr += ")";

		} else {
			qr += "  and act.team is not null ";
		}
		return qr;
	}
	
    public static String getComputationOrgsQry(AmpTeam team) {
        String orgIds = "";
        if (team.getComputation() != null && team.getComputation()) {
            Set<AmpOrganisation> orgs = team.getOrganizations();
            Iterator<AmpOrganisation> orgIter = orgs.iterator();
            while (orgIter.hasNext()) {
                AmpOrganisation org = orgIter.next();
                orgIds += org.getAmpOrgId() + ",";
            }

        }
        return orgIds;
    }    
    
    public static MapFilter getNewFilter(){
    	MapFilter filter = new MapFilter();
    	
    	List<AmpOrgGroup> orgGroups = new ArrayList(DbUtil.getAllOrgGroups());
		filter.setOrgGroups(orgGroups);
		List<AmpOrganisation> orgs = null;

		if (filter.getOrgGroupId() == null || filter.getOrgGroupId() == -1) {
			filter.setOrgGroupId(-1l);
		}
		if (filter.getSelLocationIds() == null) {
			Long[] locationIds={-1l};
			filter.setSelLocationIds(locationIds);
		}
		
		orgs = DbUtil.getDonorOrganisationByGroupId(filter.getOrgGroupId(), false); 
		filter.setOrganizations(orgs);
		
		List<AmpOrgType> orgtypes = new ArrayList<AmpOrgType>(DbUtil.getAllOrgTypes());
		filter.setOrganizationstype(orgtypes);
		
		List<AmpSector> sectors = new ArrayList(org.digijava.module.visualization.util.DbUtil.getAllSectors());
		filter.setSectors(sectors);

		if (filter.getYear() == null) {
			Long year = null;
			try {
				year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
			} catch (NumberFormatException ex) {
				year = new Long(Calendar.getInstance().get(Calendar.YEAR));
			}
			filter.setYear(year);
		}
		filter.setYears(new ArrayList<BeanWrapperImpl>());
		long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
		long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
		long maxYear = yearFrom + countYear;
		if (maxYear < filter.getYear()) {
			maxYear = filter.getYear();
		}
		for (long i = yearFrom; i <= maxYear; i++) {
			filter.getYears().add(new BeanWrapperImpl(new Long(i)));
		}

		Collection calendars = DbUtil.getAllFisCalenders();
		if (calendars != null) {
			filter.setFiscalCalendars(new ArrayList(calendars));
		}
		String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (value != null) {
			Long fisCalId = Long.parseLong(value);
			filter.setFiscalCalendarId(fisCalId);
		}
		
		if (filter.getLargestProjectNumber() == null) {
			filter.setLargestProjectNumber(10);
		}
		if (filter.getDivideThousands() == null) {
			filter.setDivideThousands(false);
		}
		if (filter.getDivideThousandsDecimalPlaces() == null) {
			filter.setDivideThousandsDecimalPlaces(0);
		}
		if (filter.getRegions() == null) {
			try {
				filter.setRegions(new ArrayList<AmpCategoryValueLocations>(DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Long[] regionId = filter.getRegionIds();
		List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

		if (regionId != null && regionId.length!=0 && regionId[0] != -1) {
			AmpCategoryValueLocations region;
			try {
				region = LocationUtil.getAmpCategoryValueLocationById(regionId[0]);
				if (region.getChildLocations() != null) {
					zones.addAll(region.getChildLocations());
				}
			} catch (DgException e) {
				e.printStackTrace();
			}

		}
		filter.setZones(zones);
		Collection currency = CurrencyUtil.getAmpCurrency();
        List<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
        filter.setCurrencies(validcurrencies);
        //Only currencies which have exchanges rates
        for (Iterator iter = currency.iterator(); iter.hasNext();) {
            AmpCurrency element = (AmpCurrency) iter.next();
            try {
				if (CurrencyUtil.isRate(element.getCurrencyCode()) == true) {
					filter.getCurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
				}
			} catch (AimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        filter.setIsinitialized(true);
		return filter;
    	
    }
    
    public static String getPercentage(BigDecimal base, BigDecimal pct){
        return FormatHelper.formatNumber(base.multiply(pct).divide(ONEHUNDERT).doubleValue()); 
    }

}
