package org.digijava.module.visualization.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.visualization.form.DashboardForm;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.visualization.helper.EntityRelatedListHelper;
import org.digijava.module.visualization.util.Constants;
import org.digijava.module.visualization.util.DashboardUtil;
import org.digijava.module.aim.util.DbUtil;
import org.springframework.beans.BeanWrapperImpl;


public class DashboardLauncher extends Action {
	private static Logger logger = Logger.getLogger(DashboardLauncher.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm dForm = (VisualizationForm) form;
		DashboardFilter filter = new DashboardFilter();
		initializeFilter(filter, request);
		dForm.setFilter(filter);
		HttpSession session = request.getSession();
	    TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		if (tm == null) {
			filter.setFromPublicView(true);
		}
		dForm.setDashboardList(org.digijava.module.visualization.util.DbUtil.getAllDashboards());
		return mapping.findForward("forward");

	}

	private void initializeFilter(DashboardFilter filter, HttpServletRequest request) {
		filter.setDashboardType(Constants.DashboardType.DONOR);
		
		filter.setShowOrganizationsRanking(false);
		filter.setShowRegionsRanking(false);
		filter.setShowSectorsRanking(false);
		filter.setShowProjectsRanking(false);
		String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
		String locale = RequestUtils.getNavigationLanguage(request).getCode();
		String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (value != null) {
			Long fisCalId = Long.parseLong(value);
			filter.setFiscalCalendarId(fisCalId);
		}
		List<AmpOrgGroup> orgGroups = new ArrayList<AmpOrgGroup>(DbUtil.getAllOrgGroups());
		filter.setOrgGroups(orgGroups);
		List<EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>> orgGroupsWithOrgsList = new ArrayList<EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>>();
		for(AmpOrgGroup orgGroup:orgGroups){
			List<AmpOrganisation> organizations=DbUtil.getOrganisationByGroupId(orgGroup.getAmpOrgGrpId());
			orgGroupsWithOrgsList.add(new EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>(orgGroup,organizations));
		}
		filter.setOrgGroupWithOrgsList(orgGroupsWithOrgsList);
		List<AmpOrganisation> orgs = null;

		if (filter.getOrgGroupId() == null
				|| filter.getOrgGroupId() == -1) {

			filter.setOrgGroupId(-1l);// -1 option denotes
												// "All Groups", which is the
												// default choice.
		}
		if(filter.getOrgGroupId()!=-1){

		orgs = DbUtil.getDonorOrganisationByGroupId(
				filter.getOrgGroupId(), filter.getFromPublicView());
		}
		filter.setOrganizations(orgs);
		try {
		if(filter.getSelSectorConfigId()==null){
				filter.setSelSectorConfigId(SectorUtil.getPrimaryConfigClassification().getId());
		}
		filter.setSectorConfigs(SectorUtil.getAllClassificationConfigs());
		filter.setConfigWithSectorAndSubSectors(new ArrayList<EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>>());
		List<AmpSector> sectors = org.digijava.module.visualization.util.DbUtil
					.getParentSectorsFromConfig(filter.getSelSectorConfigId());
		filter.setSectors(sectors);
		for(AmpClassificationConfiguration config: filter.getSectorConfigs()){
			List<AmpSector> currentConfigSectors = org.digijava.module.visualization.util.DbUtil.getParentSectorsFromConfig(config.getId());
			List<EntityRelatedListHelper<AmpSector,AmpSector>> sectorsWithSubSectors = new ArrayList<EntityRelatedListHelper<AmpSector,AmpSector>>();
			for(AmpSector sector:currentConfigSectors){;
				List<AmpSector> sectorList=new ArrayList<AmpSector>(sector.getSectors());
				sectorsWithSubSectors.add(new EntityRelatedListHelper<AmpSector,AmpSector>(sector,sectorList));
			}
			filter.getConfigWithSectorAndSubSectors().add(new EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>(config,sectorsWithSubSectors));
			}
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (filter.getStartYear() == null) {
			Long year = null;
			try {
				year = Long.parseLong(FeaturesUtil
						.getGlobalSettingValue("Current Fiscal Year"));
			} catch (NumberFormatException ex) {
				year = new Long(Calendar.getInstance().get(Calendar.YEAR));
			}
			filter.setDefaultStartYear(year-3);
			filter.setStartYear(year-3);
			filter.setStartYearQuickFilter(year-3);
			filter.setStartYearFilter(year-3);
			filter.setEndYear(year);
			filter.setDefaultEndYear(year);
			filter.setEndYearQuickFilter(year);
			filter.setEndYearFilter(year);
			filter.setYearToCompare(year-1);
		}
		filter.setYears(new TreeMap<String, Integer>());
		int yearFrom = Integer.parseInt(FeaturesUtil
						.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
		int countYear = Integer.parseInt(FeaturesUtil
						.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
		long maxYear = yearFrom + countYear;
		if (maxYear < filter.getStartYear()) {
			maxYear = filter.getStartYear();
		}
		for (int i = yearFrom; i <= maxYear; i++) {
			Long fiscalCalendarId = filter.getFiscalCalendarId();
			Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
			Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
			String headingFY;
			try {
				headingFY = TranslatorWorker.translateText("FY", locale, siteId);
			} catch (WorkerException e) {
				headingFY = "FY";
			}
			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
			filter.getYears().put(yearName,i);
		}

		Collection calendars = DbUtil.getAllFisCalenders();
		if (calendars != null) {
			filter.setFiscalCalendars(new ArrayList(calendars));
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
		if (filter.getShowAmountsInThousands() == null) {
			filter.setShowAmountsInThousands(false);
		}
		//Initialize formatting information
		if(filter.getDecimalSeparator() == null || filter.getGroupSeparator() == null ){
			filter.setDecimalSeparator(FormatHelper.getDecimalSymbol());
			filter.setGroupSeparator(FormatHelper.getGroupSymbol());
		}
		
		if (filter.getRegions() == null) {
			try {
				filter.setRegions(new ArrayList<AmpCategoryValueLocations>(
						DynLocationManagerUtil.
						getRegionsOfDefCountryHierarchy()));
				List<EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>> regionWithZones = new ArrayList<EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>>();
				for(AmpCategoryValueLocations region:filter.getRegions()){
					List<AmpCategoryValueLocations> zones=new ArrayList<AmpCategoryValueLocations>(region.getChildLocations());
					regionWithZones.add(new EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>(region,zones));
				}
				filter.setRegionWithZones(regionWithZones);
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		filter.setZones(zones);
		Collection currency = CurrencyUtil.getActiveAmpCurrencyByName();
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
        HttpSession httpSession = request.getSession();
        TeamMember teamMember = (TeamMember) httpSession.getAttribute("currentMember");
		AmpApplicationSettings tempSettings = null;
		if (teamMember != null) {
			tempSettings = DbUtil.getMemberAppSettings(teamMember.getMemberId());
			if (tempSettings!=null && tempSettings.getCurrency()!=null){
				filter.setCurrencyId(tempSettings.getCurrency().getAmpCurrencyId());
				filter.setCurrencyIdQuickFilter(tempSettings.getCurrency().getAmpCurrencyId());
			}
		}
	}

}
