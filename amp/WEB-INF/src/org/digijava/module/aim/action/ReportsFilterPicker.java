/**
 * 
 */
package org.digijava.module.aim.action;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.filters.GroupingElement;
import org.digijava.module.aim.util.filters.HierarchyListableImplementation;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Session;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author mihai
 * 
 */
public class ReportsFilterPicker extends MultiAction {
	private static Logger logger = Logger.getLogger(ReportsFilterPicker.class);
	
	final String KEY_RISK_PREFIX = "aim:risk:";
	int curYear=new GregorianCalendar().get(Calendar.YEAR);
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		String sourceIsReportWizard			= request.getParameter("sourceIsReportWizard");
		if ("true".equals(sourceIsReportWizard) ) {
			filterForm.setSourceIsReportWizard(true);
			if ( request.getParameter("doreset") != null ) {
				filterForm.setIsnewreport(true);
				reset(form, request, mapping);
				filterForm.setIsnewreport(false);
			}
		}
		else
			filterForm.setSourceIsReportWizard(false);
		
		ServletContext ampContext = getServlet().getServletContext();
		Site site = RequestUtils.getSite(request);
		Long siteId = site.getId();
		String locale = RequestUtils.getNavigationLanguage(request).getCode();
		
		String ampReportId 	= request.getParameter("ampReportId");
		if ( "".equals(ampReportId) )
			ampReportId		= null;

		if (ampReportId != null && filterForm.getAmpReportId() != null) {
			if (!filterForm.getAmpReportId().toString().equalsIgnoreCase(ampReportId)) {
				filterForm.setIsnewreport(true);
				reset(form, request, mapping);
				filterForm.setAmpReportId(new Long(ampReportId));
				filterForm.setIsnewreport(false);
			}
		} 
		else if( ampReportId != null ){
			filterForm.setIsnewreport(true);
			filterForm.setAmpReportId(new Long(ampReportId));
			filterForm.setIsnewreport(false);
		}

		if (("true").equalsIgnoreCase(filterForm.getResetFormat())) {
			resetFormat(form, request, mapping);
		}

		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession.getAttribute(Constants.CURRENT_MEMBER);
		
		AmpARFilter existingFilter		= (AmpARFilter)request.getSession().getAttribute(ReportWizardAction.EXISTING_SESSION_FILTER);
		if ( existingFilter != null ) { 
			FilterUtil.populateForm(filterForm, existingFilter);
			request.getSession().setAttribute(ReportWizardAction.EXISTING_SESSION_FILTER, null);
		}

		Long ampTeamId = null;
		if (teamMember != null)
			ampTeamId = teamMember.getTeamId();
		if (teamMember != null)
			filterForm.setTeamAccessType(teamMember.getTeamAccessType());

		// create filter dropdowns
		Collection currency = CurrencyUtil.getAmpCurrency();
	     //Only currencies havening exchanges rates AMP-2620
	      Collection<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
	      filterForm.setCurrencies(validcurrencies);
	      for (Iterator iter = currency.iterator(); iter.hasNext();) {
			AmpCurrency element = (AmpCurrency) iter.next();
			 if( CurrencyUtil.isRate(element.getCurrencyCode())== true)
					{
				 filterForm.getCurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
					}
			}
	      
		Collection allFisCalenders = DbUtil.getAllFisCalenders();

		/**
		 * For filterPicker ver2
		 */
		List<AmpSector> ampSectors = SectorUtil.getAmpSectorsAndSubSectorsHierarchy(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME);

		List<AmpSector> secondaryAmpSectors = SectorUtil.getAmpSectorsAndSubSectorsHierarchy(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME);
		
		HierarchyListableImplementation rootAmpSectors	= new HierarchyListableImplementation();
		rootAmpSectors.setLabel("Primary Sectors");
		rootAmpSectors.setUniqueId(0 + "");
		rootAmpSectors.setChildren(ampSectors);
		GroupingElement<HierarchyListableImplementation> sectorsElement		= 
			new GroupingElement<HierarchyListableImplementation>("Primary Sectors", "filter_sectors_div", rootAmpSectors, "selectedSectors");
		
		HierarchyListableImplementation rootSecondaryAmpSectors	= new HierarchyListableImplementation();
		rootSecondaryAmpSectors.setLabel("Secondary Sectors");
		rootSecondaryAmpSectors.setUniqueId("0");
		rootSecondaryAmpSectors.setChildren(secondaryAmpSectors);
		GroupingElement<HierarchyListableImplementation> secondarySectorsElement		= 
			new GroupingElement<HierarchyListableImplementation>("Secondary Sectors", "filter_secondary_sectors_div", 
					rootSecondaryAmpSectors, "selectedSecondarySectors");
		
		filterForm.setSectorElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
		filterForm.getSectorElements().add(sectorsElement);
		filterForm.getSectorElements().add(secondarySectorsElement);
		
		AmpActivityProgramSettings natPlanSetting 	= ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
		AmpTheme nationalPlanningProg 				= ProgramUtil.getAmpThemesAndSubThemesHierarchy(natPlanSetting.getDefaultHierarchy());
		AmpActivityProgramSettings primaryPrgSetting = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
		AmpTheme primaryProg 						= ProgramUtil.getAmpThemesAndSubThemesHierarchy(primaryPrgSetting.getDefaultHierarchy());
		AmpActivityProgramSettings secondaryPrg 	= ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM);
		AmpTheme secondaryProg	 					= ProgramUtil.getAmpThemesAndSubThemesHierarchy(secondaryPrg.getDefaultHierarchy());
		GroupingElement<AmpTheme> natPlanProgElement		= 
			new GroupingElement<AmpTheme>("National Planning Objective", "filter_nat_plan_obj_div", 
					nationalPlanningProg, "selectedNatPlanObj");
		GroupingElement<AmpTheme> primaryProgElement		= 
			new GroupingElement<AmpTheme>("Primary Program", "filter_primary_prog_div", 
					primaryProg, "selectedPrimaryPrograms");
		GroupingElement<AmpTheme> secondaryProgElement		= 
			new GroupingElement<AmpTheme>("Secondary Program", "filter_secondary_prog_div", 
					secondaryProg, "selectedSecondaryPrograms");
		
		filterForm.setProgramElements(new ArrayList<GroupingElement<AmpTheme>>());
		if(nationalPlanningProg!=null)
			filterForm.getProgramElements().add(natPlanProgElement);
		if(primaryProg!=null)
			filterForm.getProgramElements().add(primaryProgElement);
		if(secondaryProg!=null)
			filterForm.getProgramElements().add(secondaryProgElement);

		Collection donorTypes = DbUtil.getAllOrgTypesOfPortfolio();
		Collection<AmpOrgGroup> donorGroups = ARUtil.filterDonorGroups(DbUtil.getAllOrgGroupsOfPortfolio());
		
		filterForm.setDonorElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
		
		HierarchyListableImplementation rootOrgType	= new HierarchyListableImplementation();
		rootOrgType.setLabel("All Donor Types");
		rootOrgType.setUniqueId("0");
		rootOrgType.setChildren( donorTypes );
		GroupingElement<HierarchyListableImplementation> donorTypeElement	=
				new GroupingElement<HierarchyListableImplementation>("Donor Types", "filter_donor_types_div", 
						rootOrgType, "selectedDonorTypes");
		filterForm.getDonorElements().add(donorTypeElement);
		
		HierarchyListableImplementation rootOrgGroup	= new HierarchyListableImplementation();
		rootOrgGroup.setLabel("All Donor Groups");
		rootOrgGroup.setUniqueId("0");
		rootOrgGroup.setChildren( donorGroups );
		GroupingElement<HierarchyListableImplementation> donorGroupElement	=
				new GroupingElement<HierarchyListableImplementation>("Donor Groups", "filter_donor_groups_div", 
						rootOrgGroup, "selectedDonorGroups");
		filterForm.getDonorElements().add(donorGroupElement);
		
		Collection<AmpOrganisation> donors = ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_DONOR);
		HierarchyListableImplementation rootDonors	= new HierarchyListableImplementation();
		rootDonors.setLabel("All Donors");
		rootDonors.setUniqueId("0");
		rootDonors.setChildren( donors );
		GroupingElement<HierarchyListableImplementation> donorsElement	=
				new GroupingElement<HierarchyListableImplementation>("Donor Agencies", "filter_donor_agencies_div", 
						rootDonors, "selectedDonnorAgency");
		filterForm.getDonorElements().add(donorsElement);
		
		
		
		filterForm.setRelatedAgenciesElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
		
		if (FeaturesUtil.isVisibleFeature("Executing Agency", ampContext) ) {
			Collection<AmpOrganisation> execAgencies			= (ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_EXECUTING_AGENCY));
			HierarchyListableImplementation rootExecAgencies	= new HierarchyListableImplementation();
			rootExecAgencies.setLabel("All Executing Agencies");
			rootExecAgencies.setUniqueId("0");
			rootExecAgencies.setChildren( execAgencies );
			GroupingElement<HierarchyListableImplementation> execAgenciesElement	=
					new GroupingElement<HierarchyListableImplementation>("Executing Agencies", "filter_executing_agencies_div", 
							rootExecAgencies, "selectedExecutingAgency");
			filterForm.getRelatedAgenciesElements().add(execAgenciesElement);
		}
		if (FeaturesUtil.isVisibleFeature("Implementing Agency", ampContext) ) {
			Collection<AmpOrganisation> implemAgencies			= (ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_IMPLEMENTING_AGENCY));
			HierarchyListableImplementation rootImplemAgencies	= new HierarchyListableImplementation();
			rootImplemAgencies.setLabel("All Implementing Agencies");
			rootImplemAgencies.setUniqueId("0");
			rootImplemAgencies.setChildren( implemAgencies );
			GroupingElement<HierarchyListableImplementation> execAgenciesElement	=
					new GroupingElement<HierarchyListableImplementation>("Implementing Agencies", "filter_implementing_agencies_div", 
							rootImplemAgencies, "selectedImplementingAgency");
			filterForm.getRelatedAgenciesElements().add(execAgenciesElement);
			
		}
		if (FeaturesUtil.isVisibleFeature("Responsible Organization", ampContext) ) {
			Collection<AmpOrganisation> respAgencies			= (ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_RESPONSIBLE_ORG));
			HierarchyListableImplementation rootRespAgencies	= new HierarchyListableImplementation();
			rootRespAgencies.setLabel("All Responsible Agencies");
			rootRespAgencies.setUniqueId("0");
			rootRespAgencies.setChildren( respAgencies );
			GroupingElement<HierarchyListableImplementation> respAgenciesElement	=
					new GroupingElement<HierarchyListableImplementation>("Responsible Agencies", "filter_responsible_agencies_div", 
							rootRespAgencies, "selectedresponsibleorg");
			filterForm.getRelatedAgenciesElements().add(respAgenciesElement);
			
		}
		if (FeaturesUtil.isVisibleFeature("Beneficiary Agency", ampContext) ) {
			Collection<AmpOrganisation> benAgencies			= (ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_BENEFICIARY_AGENCY));
			HierarchyListableImplementation rootBenAgencies	= new HierarchyListableImplementation();
			rootBenAgencies.setLabel("All Beneficiary Agencies");
			rootBenAgencies.setUniqueId("0");
			rootBenAgencies.setChildren( benAgencies );
			GroupingElement<HierarchyListableImplementation> benAgenciesElement	=
					new GroupingElement<HierarchyListableImplementation>("Beneficiary Agencies", "filter_beneficiary_agencies_div", 
							rootBenAgencies, "selectedBeneficiaryAgency");
			filterForm.getRelatedAgenciesElements().add(benAgenciesElement);
			
		}
		
		
		filterForm.setFinancingLocationElements( new ArrayList<GroupingElement<HierarchyListableImplementation>>() );
		
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<AmpCategoryValue> finInstrValues	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY, true, request);	
			HierarchyListableImplementation rootFinancingInstrument	= new HierarchyListableImplementation();
			rootFinancingInstrument.setLabel("All Financing Instrument Values");
			rootFinancingInstrument.setUniqueId("0");
			rootFinancingInstrument.setChildren( finInstrValues );
			GroupingElement<HierarchyListableImplementation> finInstrElement	=
					new GroupingElement<HierarchyListableImplementation>("Financing Instrument", "filter_financing_instr_div", 
							rootFinancingInstrument, "selectedFinancingInstruments");
			filterForm.getFinancingLocationElements().add(finInstrElement);
		}
		
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<AmpCategoryValue> typeOfAssistValues	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY, true, request);	
			HierarchyListableImplementation rootTypeOfAssistance	= new HierarchyListableImplementation();
			rootTypeOfAssistance.setLabel("All Type of Assistance Values");
			rootTypeOfAssistance.setUniqueId("0");
			rootTypeOfAssistance.setChildren( typeOfAssistValues );
			GroupingElement<HierarchyListableImplementation> typeOfAssistElement	=
					new GroupingElement<HierarchyListableImplementation>("Type of Assistance", "filter_type_of_assistance_div", 
							rootTypeOfAssistance, "selectedTypeOfAssistance");
			filterForm.getFinancingLocationElements().add(typeOfAssistElement);
		}
		
		if (FeaturesUtil.isVisibleField("Project Category", ampContext)) { 
			Collection<AmpCategoryValue> projCategoryValues	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PROJECT_CATEGORY_KEY, true, request);	
			HierarchyListableImplementation rootProjCategory	= new HierarchyListableImplementation();
			rootProjCategory.setLabel("All Project Category Values");
			rootProjCategory.setUniqueId("0");
			rootProjCategory.setChildren( projCategoryValues );
			GroupingElement<HierarchyListableImplementation> projCategoryElement	=
					new GroupingElement<HierarchyListableImplementation>("Project Category", "filter_project_category_div", 
							rootProjCategory, "selectedProjectCategory");
			filterForm.getFinancingLocationElements().add(projCategoryElement);
		}
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<HierarchyListableImplementation> children	= 
				new ArrayList<HierarchyListableImplementation>();
			HierarchyListableImplementation rootDisbursementOrders	= new HierarchyListableImplementation();
			rootDisbursementOrders.setLabel("All");
			rootDisbursementOrders.setUniqueId("-1");
			rootDisbursementOrders.setChildren( children );
			HierarchyListableImplementation notRejectedDO	= new HierarchyListableImplementation();
			notRejectedDO.setLabel("Not Rejected");
			notRejectedDO.setUniqueId("0");
			children.add(notRejectedDO);
			HierarchyListableImplementation rejectedDO	= new HierarchyListableImplementation();
			rejectedDO.setLabel("Rejected");
			rejectedDO.setUniqueId("1");
			children.add(rejectedDO);
			GroupingElement<HierarchyListableImplementation> disbOrdersElement	=
					new GroupingElement<HierarchyListableImplementation>("Disbursement Orders", "filter_disb_orders_div", 
							rootDisbursementOrders, "disbursementOrders");
			filterForm.getFinancingLocationElements().add(disbOrdersElement);
		}
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<HierarchyListableImplementation> children	= 
				new ArrayList<HierarchyListableImplementation>();
			HierarchyListableImplementation rootOnOffBudget	= new HierarchyListableImplementation();
			rootOnOffBudget.setLabel("All");
			rootOnOffBudget.setUniqueId("-1");
			rootOnOffBudget.setChildren( children );
			HierarchyListableImplementation onBudgetDO	= new HierarchyListableImplementation();
			onBudgetDO.setLabel("On Budget");
			onBudgetDO.setUniqueId("1");
			children.add(onBudgetDO);
			HierarchyListableImplementation offBudgetDO	= new HierarchyListableImplementation();
			offBudgetDO.setLabel("Off Budget");
			offBudgetDO.setUniqueId("0");
			children.add(offBudgetDO);
			GroupingElement<HierarchyListableImplementation> disbOrdersElement	=
					new GroupingElement<HierarchyListableImplementation>("On Budget", "filter_on_budget_div", 
							rootOnOffBudget, "selectedBudgets");
			filterForm.getFinancingLocationElements().add(disbOrdersElement);
		}
		if (true) { 
			Collection<AmpCategoryValueLocations> regions = DynLocationManagerUtil.getRegionsOfDefCountryHierarchy();	
			HierarchyListableImplementation rootRegions	= new HierarchyListableImplementation();
			rootRegions.setLabel("All Regions");
			rootRegions.setUniqueId("0");
			rootRegions.setChildren( regions );
			GroupingElement<HierarchyListableImplementation> regionsElement	=
					new GroupingElement<HierarchyListableImplementation>("Regions", "filter_regions_div", 
							rootRegions, "regionSelected");
			filterForm.getFinancingLocationElements().add(regionsElement);
		}
		
		
		
		filterForm.setOtherCriteriaElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>() );
		
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<AmpCategoryValue> activityStatusValues	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY, true, request);	
			HierarchyListableImplementation rootActivityStatus	= new HierarchyListableImplementation();
			rootActivityStatus.setLabel("All");
			rootActivityStatus.setUniqueId("0");
			rootActivityStatus.setChildren( activityStatusValues );
			GroupingElement<HierarchyListableImplementation> activityStatusElement	=
					new GroupingElement<HierarchyListableImplementation>("Status", "filter_activity_status_div", 
							rootActivityStatus, "selectedStatuses");
			filterForm.getOtherCriteriaElements().add(activityStatusElement);
		}
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<HierarchyListableImplementation> children	= 
				new ArrayList<HierarchyListableImplementation>();
			HierarchyListableImplementation rootApprovalStatus	= new HierarchyListableImplementation();
			rootApprovalStatus.setLabel("All");
			rootApprovalStatus.setUniqueId("-1");
			rootApprovalStatus.setChildren( children );
			HierarchyListableImplementation newDraftDO	= new HierarchyListableImplementation();
			newDraftDO.setLabel( TranslatorWorker.translateText("New Draft", locale, siteId) );
			newDraftDO.setUniqueId("1");
			children.add(newDraftDO);
			HierarchyListableImplementation newUnvalidatedDO	= new HierarchyListableImplementation();
			newUnvalidatedDO.setLabel( TranslatorWorker.translateText("New Unvalidated", locale, siteId) );
			newUnvalidatedDO.setUniqueId("2");
			children.add(newUnvalidatedDO);
			HierarchyListableImplementation validatedActDO	= new HierarchyListableImplementation();
			validatedActDO.setLabel( TranslatorWorker.translateText("Validated Activities", locale, siteId) );
			validatedActDO.setUniqueId("4");
			children.add(validatedActDO);
			HierarchyListableImplementation existingDraftDO	= new HierarchyListableImplementation();
			existingDraftDO.setLabel( TranslatorWorker.translateText("Existing Draft", locale, siteId) );
			existingDraftDO.setUniqueId("3");
			children.add(existingDraftDO);
			HierarchyListableImplementation existingUnvalidatedDO	= new HierarchyListableImplementation();
			existingUnvalidatedDO.setLabel( TranslatorWorker.translateText("Existing Unvalidated", locale, siteId) );
			existingUnvalidatedDO.setUniqueId("0");
			children.add(existingUnvalidatedDO);
			GroupingElement<HierarchyListableImplementation> approvalStatusElement	=
					new GroupingElement<HierarchyListableImplementation>("Approval Status", "filter_approval_status_div", 
							rootApprovalStatus, "approvalStatusSelected");
			filterForm.getOtherCriteriaElements().add(approvalStatusElement);
		}
		if ( FeaturesUtil.isVisibleField("Risk", ampContext) ) {
			Collection<AmpCategoryValue> riskValues	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.INDICATOR_RISK_TYPE_KEY, true, request);	
			HierarchyListableImplementation rootRisk	= new HierarchyListableImplementation();
			rootRisk.setLabel("All Risks");
			rootRisk.setUniqueId("0");
			rootRisk.setChildren( riskValues );
			GroupingElement<HierarchyListableImplementation> riskElement	=
					new GroupingElement<HierarchyListableImplementation>("Risk", "filter_risk_div", 
							rootRisk, "selectedRisks");
			filterForm.getOtherCriteriaElements().add(riskElement);
		}
		if ( FeaturesUtil.isVisibleField("Line Ministry Rank", ampContext)) {
			Collection<HierarchyListableImplementation> children	= 
				new ArrayList<HierarchyListableImplementation>();
			HierarchyListableImplementation rootLineMinRank	= new HierarchyListableImplementation();
			rootLineMinRank.setLabel("All");
			rootLineMinRank.setUniqueId("-1");
			rootLineMinRank.setChildren( children );
			for (int i=1; i<6 ; i++) {
				HierarchyListableImplementation lineMinDO	= new HierarchyListableImplementation();
				lineMinDO.setLabel( i + "" );
				lineMinDO.setUniqueId( i + "");
				children.add(lineMinDO);
			}
			GroupingElement<HierarchyListableImplementation> lineMinRankElement	=
					new GroupingElement<HierarchyListableImplementation>("Line Ministry Rank", "filter_line_min_rank_div", 
							rootLineMinRank, "lineMinRanks");
			filterForm.getOtherCriteriaElements().add(lineMinRankElement);
		}
		if ( FeaturesUtil.isVisibleField("Ministry of Planning Rank", ampContext)) {
			Collection<HierarchyListableImplementation> children	= 
				new ArrayList<HierarchyListableImplementation>();
			HierarchyListableImplementation rootplanMinRank	= new HierarchyListableImplementation();
			rootplanMinRank.setLabel("All");
			rootplanMinRank.setUniqueId("-1");
			rootplanMinRank.setChildren( children );
			for (int i=1; i<6 ; i++) {
				HierarchyListableImplementation planMinDO	= new HierarchyListableImplementation();
				planMinDO.setLabel( i + "" );
				planMinDO.setUniqueId( i + "");
				children.add(planMinDO);
			}
			GroupingElement<HierarchyListableImplementation> planMinRankElement	=
					new GroupingElement<HierarchyListableImplementation>("Planning Ministry Rank", "filter_plan_min_rank_div", 
							rootplanMinRank, "planMinRanks");
			filterForm.getOtherCriteriaElements().add(planMinRankElement);
		}
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<HierarchyListableImplementation> children	= 
				new ArrayList<HierarchyListableImplementation>();
			HierarchyListableImplementation rootArchivedStatus	= new HierarchyListableImplementation();
			rootArchivedStatus.setLabel("All");
			rootArchivedStatus.setUniqueId("0");
			rootArchivedStatus.setChildren( children );
			HierarchyListableImplementation unarchivedDO	= new HierarchyListableImplementation();
			unarchivedDO.setLabel( TranslatorWorker.translateText("Non-archived Activities", locale, siteId) );
			unarchivedDO.setUniqueId("1");
			children.add(unarchivedDO);
			HierarchyListableImplementation archivedDO	= new HierarchyListableImplementation();
			archivedDO.setLabel( TranslatorWorker.translateText("Archived Activities", locale, siteId) );
			archivedDO.setUniqueId("2");
			children.add(archivedDO);
			GroupingElement<HierarchyListableImplementation> archivedElement	=
					new GroupingElement<HierarchyListableImplementation>("Archived", "filter_archived_div", 
							rootArchivedStatus, "selectedArchivedStatus");
			filterForm.getOtherCriteriaElements().add(archivedElement);
		}
		
//		List<AmpTheme> nationalPlanningObjectives;
//		AmpActivityProgramSettings natPlanSetting = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
//		if (natPlanSetting!=null && natPlanSetting.getDefaultHierarchy() != null) {
//			nationalPlanningObjectives = ProgramUtil.getAmpThemesAndSubThemes(natPlanSetting.getDefaultHierarchy());
//		} else {
//			nationalPlanningObjectives = ProgramUtil.getAllSubThemesFor(ProgramUtil.getAllThemes(false));
//		}

//		List<AmpTheme> primaryPrograms;
//		AmpActivityProgramSettings primaryPrgSetting = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
//		if (primaryPrgSetting!=null && primaryPrgSetting.getDefaultHierarchy() != null) {
//			primaryPrograms = ProgramUtil.getAmpThemesAndSubThemes(primaryPrgSetting.getDefaultHierarchy());
//		} else {
//			primaryPrograms = ProgramUtil.getAllSubThemesFor(ProgramUtil.getAllThemes(false));
//
//		}

//		List<AmpTheme> secondaryPrograms;
//		AmpActivityProgramSettings secondaryPrg = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM);
//		if (secondaryPrg!=null && secondaryPrg.getDefaultHierarchy() != null) {
//			secondaryPrograms = ProgramUtil.getAmpThemesAndSubThemes(secondaryPrg.getDefaultHierarchy());
//		} else {
//			secondaryPrograms = ProgramUtil.getAllSubThemesFor(ProgramUtil.getAllThemes(false));
//		}

		/**
		 * This has been moved in SectorUtil.getAmpSectorsAndSubSectors();
		 * 
		 * Long
		 * primaryConfigClassId=SectorUtil.getPrimaryConfigClassificationId();
		 * ampSectors =
		 * (List)SectorUtil.getAllParentSectors(primaryConfigClassId);
		 */

		// ampSectors =
		// SectorUtil.getAllSectorsFromScheme(FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_SECTOR_SCHEME));
		/*
		 * TreeSet<AmpSector> alphaOrderedSectors = new TreeSet<AmpSector>(
		 * new Comparator<AmpSector>() {
		 * 
		 * public int compare(AmpSector as1, AmpSector as2) { if ( as1.getName() !=
		 * null && as2.getName() != null ) return
		 * as1.getName().compareToIgnoreCase(as2.getName() );
		 * 
		 * return -1; } } ); Iterator<AmpSector> sectIter = (Iterator<AmpSector>)ampSectors.iterator();
		 * while ( sectIter.hasNext() ) { alphaOrderedSectors.add(
		 * sectIter.next() ); }
		 */

		AmpApplicationSettings tempSettings = getAppSetting(request);
		if (tempSettings != null) {
			filterForm.setDefaultCurrency(tempSettings.getCurrency().getAmpCurrencyId());

			if (filterForm.getCurrency() == null) {
				filterForm.setCurrency(tempSettings.getCurrency().getAmpCurrencyId());
			}
			if (tempSettings.getFiscalCalendar()!=null && filterForm.getCalendar() == null) {
				filterForm.setCalendar(tempSettings.getFiscalCalendar().getAmpFiscalCalId());
			}
		} else {
			filterForm.setDefaultCurrency(CurrencyUtil.getCurrencyByCode(Constants.DEFAULT_CURRENCY).getAmpCurrencyId());
			if (filterForm.getCalendar() == null) {
				String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
				if (value != null) {
					filterForm.setCalendar(Long.parseLong(value));
				}
			}
		}

		// create the pageSizes Collection for the dropdown
		Collection pageSizes = new ArrayList();

		
		Collection<AmpCategoryValue> risks=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.INDICATOR_RISK_TYPE_KEY);


		
		// Collection regions=LocationUtil.getAllVRegions();
		//filterForm.setCurrencies(currency);
		filterForm.setCalendars(allFisCalenders);
		// filterForm.setDonors(donors);
		filterForm.setRisks(risks);
		
		//filterForm.setNationalPlanningObjectives(nationalPlanningObjectives);
		//filterForm.setPrimaryPrograms(primaryPrograms);
		//filterForm.setSecondaryPrograms(secondaryPrograms);
		

		filterForm.setFromYears(new ArrayList<BeanWrapperImpl>());
		filterForm.setToYears(new ArrayList<BeanWrapperImpl>());

		filterForm.setFromMonths(new ArrayList<BeanWrapperImpl>());
		filterForm.setToMonths(new ArrayList<BeanWrapperImpl>());

		filterForm.setCountYears(new ArrayList<BeanWrapperImpl>());
		filterForm.setComputedYearsRange(new ArrayList<BeanWrapperImpl>());
		filterForm.setPageSizes(pageSizes);
		//filterForm.setRegionSelectedCollection(regions);
		filterForm.setApprovalStatusSelectedCollection(new ArrayList());
		//filterForm.setDonorTypes(donorTypes);
		//filterForm.setDonorGroups(donorGroups);

//		filterForm.setExecutingAgency(ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_EXECUTING_AGENCY));
//		//filterForm.setDonnorAgency((ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_DONOR)));
//		filterForm.setBeneficiaryAgency(ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_BENEFICIARY_AGENCY));
//		filterForm.setImplementingAgency(ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_IMPLEMENTING_AGENCY));
//		filterForm.setResponsibleorg(ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_RESPONSIBLE_ORG));
		
		String calValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (filterForm.getCalendar() == null && calValue != null) {
			filterForm.setCalendar(Long.parseLong(calValue));
		}
		// loading Activity Rank collection
//		if (null == filterForm.getActRankCollection()) {
//			filterForm.setActRankCollection(new ArrayList());
//			for (int i = 1; i < 6; i++)
//				filterForm.getActRankCollection().add(new BeanWrapperImpl(new Integer(i)));
//		}

		Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.YEAR_RANGE_START));
		Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
	
		if (filterForm.getCountYear() == null) {
			filterForm.setCountYear(countYear);
		}

		if (filterForm.getCountYearFrom() == null) {
			filterForm.setCountYearFrom(yearFrom);
		}
		
		
		
		for (long i = curYear-10; i < curYear; i ++) {
			filterForm.getComputedYearsRange().add(new BeanWrapperImpl(new Long(i)));
		}
		
		for (long i = 10; i <= 100; i += 10) {
			filterForm.getCountYears().add(new BeanWrapperImpl(new Long(i)));
		}

		for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
			filterForm.getFromYears().add(new BeanWrapperImpl(new Long(i)));
			filterForm.getToYears().add(new BeanWrapperImpl(new Long(i)));
		}

		if (filterForm.getFromYear() == null) {
			// Long
			// fromYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE));
			filterForm.setFromYear(-1l);
		}

		if (filterForm.getToYear() == null) {
			// Long
			// toYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE));
			filterForm.setToYear(-1l);
		}

		for (int i = 1; i <= 12; i++) {
			filterForm.getFromMonths().add(new BeanWrapperImpl(new Integer(i)));
			filterForm.getToMonths().add(new BeanWrapperImpl(new Integer(i)));
		}

		if (filterForm.getFromMonth() == null)
			filterForm.setFromMonth(-1);

		if (filterForm.getToMonth() == null)
			filterForm.setToMonth(-1);

		/*--------------------------*/
		Integer rStart = getDefaultStartYear(request);
		
		Integer rEnd = getDefaultEndYear(request);
		
		
		 if (filterForm.getCalendar() != null) {
			rStart = getYearOnCalendar(filterForm.getCalendar(), rStart,tempSettings);
			rEnd = getYearOnCalendar(filterForm.getCalendar(), rEnd,tempSettings);
		}

		if (filterForm.getRenderStartYear() == null || (request.getParameter("view") != null && "reset".compareTo(request.getParameter("view")) == 0)) {
			filterForm.setRenderStartYear(rStart);
			tempSettings = null;
		}

		if (filterForm.getRenderEndYear() == null || (request.getParameter("view") != null && "reset".compareTo(request.getParameter("view")) == 0)) {
			filterForm.setRenderEndYear(rEnd);
			tempSettings = null;
		}
		
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A0")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A1")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A2")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A3")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A4")));

		if (ampReportId != null) {

			AmpReports rep = (AmpReports) DbUtil.getAmpReports(new Long(ampReportId));

			httpSession.setAttribute("filterCurrentReport", rep);
		}

		if (filterForm.getCustomDecimalSymbol() == null) {
			filterForm.setCustomDecimalSymbol(String.valueOf((FormatHelper.getDecimalFormat().getDecimalFormatSymbols().getDecimalSeparator())));
			filterForm.setCustomDecimalPlaces(FormatHelper.getDecimalFormat().getMaximumFractionDigits());
			filterForm.setCustomGroupCharacter(String.valueOf(FormatHelper.getDecimalFormat().getDecimalFormatSymbols().getGroupingSeparator()));
			filterForm.setCustomUseGrouping(FormatHelper.getDecimalFormat().isGroupingUsed());
			filterForm.setCustomGroupSize(FormatHelper.getDecimalFormat().getGroupingSize());
		}

		return modeSelect(mapping, form, request, response);

	}

	public ActionForward modeReset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		request.setAttribute("reset", "reset");
		// filterForm.setSelectedDonors(null);
		filterForm.setSelectedRisks(null);
		filterForm.setSelectedSectors(null);
		filterForm.setSelectedStatuses(null);
		filterForm.setSelectedSecondaryPrograms(null);
		filterForm.setSelectedPrimaryPrograms(null);
		filterForm.setSelectedNatPlanObj(null);
		filterForm.setSelectedArchivedStatus(new Object[]{"1"});
		HttpSession httpSession = request.getSession();
		
		AmpApplicationSettings tempSettings = getAppSetting(request);
		if (tempSettings != null) {
			filterForm.setCurrency(tempSettings.getCurrency().getAmpCurrencyId());
			String name = "- " + tempSettings.getCurrency().getCurrencyName();
			httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
			filterForm.setCalendar(tempSettings.getFiscalCalendar().getAmpFiscalCalId());
		}

		// Long
		// fromYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE));
		filterForm.setFromYear(-1l);

		// Long
		// toYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE));

		if (tempSettings != null) {
			filterForm.setRenderStartYear(tempSettings.getReportStartYear());
			filterForm.setRenderEndYear(tempSettings.getReportEndYear());
		} else {
			filterForm.setRenderStartYear(-1);
			filterForm.setRenderEndYear(-1);
		}
		filterForm.setToYear(-1l);
		filterForm.setFromMonth(-1);
		filterForm.setToMonth(-1);
		filterForm.setFromDate(null);
		filterForm.setToDate(null);

		filterForm.setLineMinRanks(null);
		filterForm.setPlanMinRanks(null);
		filterForm.setText(null);
		filterForm.setPageSize(null);
		filterForm.setGovernmentApprovalProcedures(null);
		filterForm.setJointCriteria(null);
		filterForm.setJustSearch(null);
		filterForm.setRegionSelected(null);
		filterForm.setApprovalStatusSelected(null);
		// filterForm.setRegions(null);
		//filterForm.setDonorGroups(null);
		//filterForm.setDonorTypes(null);
		//filterForm.setExecutingAgency(null);
		//filterForm.setBeneficiaryAgency(null);
		//filterForm.setImplementingAgency(null);
		//filterForm.setDonnorAgency(null);
		
		filterForm.setDonorElements(null);
		filterForm.setRelatedAgenciesElements(null);
		
		if (tempSettings != null) {
			filterForm.setRenderStartYear(tempSettings.getReportStartYear());
			filterForm.setRenderEndYear(tempSettings.getReportEndYear());
		} else {
			filterForm.setRenderStartYear(-1);
			filterForm.setRenderEndYear(-1);
		}

		return modeApply(mapping, form, request, response);
	}

	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getParameter("apply") != null && request.getAttribute("apply") == null)
			return modeApply(mapping, form, request, response);
		if (request.getParameter("reset") != null && request.getAttribute("reset") == null)
			return modeReset(mapping, form, request, response);

		HttpSession httpSession = request.getSession();

		if (httpSession.getAttribute(ArConstants.SELECTED_CURRENCY) == null) {
		
			AmpApplicationSettings tempSettings=getAppSetting(request);
			if (tempSettings != null) {
				String name = "- " + tempSettings.getCurrency().getCurrencyName();
				httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
			}
		}
		return mapping.findForward("forward");
	}

	/**
	 * generate a session based AmpARFilter object based on the form selections
	 * 
	 * @param mapping
	 * @param form
	 * @param requestTeamMember
	 *            teamMember = (TeamMember) httpSession
	 *            .getAttribute(Constants.CURRENT_MEMBER);
	 *            AmpApplicationSettings tempSettings =
	 *            DbUtil.getMemberAppSettings(teamMember.getMemberId());
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modeApply(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		HttpSession httpSession = request.getSession();

		Session session = PersistenceManager.getSession();
				
		request.setAttribute("apply", "apply");
		AmpARFilter arf;
		if ( filterForm.getSourceIsReportWizard() != null && filterForm.getSourceIsReportWizard() ) {
			arf	= new AmpARFilter();
		}
		else {
			arf 	= (AmpARFilter) httpSession.getAttribute(ArConstants.REPORTS_FILTER);
			if (arf == null)
				arf = new AmpARFilter();
		}
		arf.readRequestData(request);

		// for each sector we have also to add the subsectors

		Set selectedSectors = Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedSectors());
		Set selectedSecondarySectors = Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedSecondarySectors());

		if (selectedSectors != null && selectedSectors.size() > 0) {
			arf.setSelectedSectors(new HashSet());
			arf.getSelectedSectors().addAll(selectedSectors);

			arf.setSectors(SectorUtil.getSectorDescendents(selectedSectors));
		} else {
			arf.setSectors(null);
			arf.setSelectedSectors(null);
		}
		
		if (filterForm.getSelectedHistory() != null && filterForm.getSelectedHistory().length > 0) {
			ArrayList<String> histories = new ArrayList();
			for (int i = 0; i < filterForm.getSelectedHistory().length; i++) {
				histories.add(filterForm.getSelectedHistory()[i].toString());
			}
			arf.setHistory(histories);
		}

		if (selectedSecondarySectors != null && selectedSecondarySectors.size() > 0) {
			arf.setSelectedSecondarySectors(new HashSet());
			arf.getSelectedSecondarySectors().addAll(selectedSecondarySectors);

			arf.setSecondarySectors(SectorUtil.getSectorDescendents(selectedSecondarySectors));
		} else {
			arf.setSecondarySectors(null);
			arf.setSelectedSecondarySectors(null);
		}

		Set selectedNatPlanObj = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedNatPlanObj());
		Set selectedPrimaryPrograms = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedPrimaryPrograms());
		Set selectedSecondaryPrograms = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedSecondaryPrograms());

		if (selectedNatPlanObj != null && selectedNatPlanObj.size() > 0) {
			arf.setSelectedNatPlanObj(new HashSet());
			arf.getSelectedNatPlanObj().addAll(selectedNatPlanObj);
			arf.setNationalPlanningObjectives( new ArrayList(selectedNatPlanObj) );
			
			arf.setRelatedNatPlanObjs(new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(selectedNatPlanObj, arf.getNationalPlanningObjectives(), arf.getRelatedNatPlanObjs() );
			
		} else {
			arf.setSelectedNatPlanObj(null);
			arf.setNationalPlanningObjectives(null);
			arf.setRelatedNatPlanObjs(null);
		}

		if (selectedPrimaryPrograms != null && selectedPrimaryPrograms.size() > 0) {
			arf.setSelectedPrimaryPrograms(new HashSet());
			arf.getSelectedPrimaryPrograms().addAll(selectedPrimaryPrograms);
			arf.setPrimaryPrograms(new ArrayList(selectedPrimaryPrograms));
			
			arf.setRelatedPrimaryProgs(new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(selectedPrimaryPrograms, arf.getPrimaryPrograms(), arf.getRelatedPrimaryProgs() );
			
		} else {
			arf.setPrimaryPrograms(null);
			arf.setSelectedPrimaryPrograms(null);
			arf.setRelatedPrimaryProgs(null);
		}

		if (selectedSecondaryPrograms != null && selectedSecondaryPrograms.size() > 0) {
			arf.setSelectedSecondaryPrograms(new HashSet());
			arf.getSelectedSecondaryPrograms().addAll(selectedSecondaryPrograms);
			arf.setSecondaryPrograms(new ArrayList(selectedSecondaryPrograms));
			
			arf.setRelatedSecondaryProgs( new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(selectedSecondaryPrograms, arf.getSecondaryPrograms(), arf.getRelatedSecondaryProgs() );
			
		} else {
			arf.setSecondaryPrograms(null);
			arf.setSelectedSecondaryPrograms(null);
			arf.setRelatedSecondaryProgs(null);

		}
		AmpApplicationSettings tempSettings = getAppSetting(request);
		AmpFiscalCalendar selcal = (AmpFiscalCalendar) Util.getSelectedObject(AmpFiscalCalendar.class, filterForm.getCalendar());
		if (!selcal.equals(arf.getCalendarType())) {
			arf.setCalendarType(selcal);
			if (filterForm.getRenderEndYear().intValue() == arf.getRenderEndYear().intValue() && filterForm.getRenderStartYear().intValue() == arf.getRenderStartYear().intValue()) {
				Integer defaultStart = getDefaultStartYear(request);
				Integer defaultEnd = getDefaultEndYear(request);
				if (filterForm.getCalendar() != null) {
					if (filterForm.getRenderStartYear() > 0)
					filterForm.setRenderStartYear(getYearOnCalendar(filterForm.getCalendar(), defaultStart,tempSettings));
				
					if (filterForm.getRenderEndYear()> 0)
					filterForm.setRenderEndYear(getYearOnCalendar(filterForm.getCalendar(), defaultEnd,tempSettings));

				}

			}
		}
		arf.setText(filterForm.getText());

		if (filterForm.getIndexString() != null) {
			arf.setIndexText(filterForm.getIndexString());
		}

		arf.setYearFrom(filterForm.getFromYear() == null || filterForm.getFromYear().longValue() == -1 ? null : new Integer(filterForm.getFromYear().intValue()));
		arf.setYearTo(filterForm.getToYear() == null || filterForm.getToYear().longValue() == -1 ? null : new Integer(filterForm.getToYear().intValue()));
		arf.setFromMonth(filterForm.getFromMonth() == null || filterForm.getFromMonth().intValue() == -1 ? null : new Integer(filterForm.getFromMonth().intValue()));
		arf.setToMonth(filterForm.getToMonth() == null || filterForm.getToMonth().intValue() == -1 ? null : new Integer(filterForm.getToMonth().intValue()));
		arf.setFromDate(filterForm.getFromDate() == null ? null : new String(filterForm.getFromDate()));
		arf.setToDate(filterForm.getToDate() == null ? null : new String(filterForm.getToDate()));

		if (filterForm.getComputedYear()!=-1){
			arf.setComputedYear(filterForm.getComputedYear());
		}else{
			arf.setComputedYear(curYear);
		}
		// arf.setDonors(Util.getSelectedObjects(AmpOrgGroup.class,filterForm.getSelectedDonors()));
		
		Long cur = filterForm.getCurrency();
		if (cur == null) {
			cur = filterForm.getDefaultCurrency();
		}
		
		
		AmpCurrency currency = (AmpCurrency) Util.getSelectedObject(AmpCurrency.class, cur);
		arf.setCurrency(currency);
		String name = "- " + currency.getCurrencyName();
		httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
		Integer all = new Integer(-1);
		
		if ( filterForm.getLineMinRanks() != null && filterForm.getLineMinRanks().length > 0 ) {
			ArrayList<Integer> ranks	= new ArrayList<Integer>();
			for (int i=0; i< filterForm.getLineMinRanks().length;  i++) {
				Integer val	= Integer.parseInt((String)filterForm.getLineMinRanks()[i]);
				if ( val == 0 ) {
					ranks	= null;
					break;
				}
				else
					ranks.add(val);
			}
			arf.setLineMinRank(ranks);
		}
		if ( filterForm.getPlanMinRanks() != null && filterForm.getPlanMinRanks().length > 0 ) {
			ArrayList<Integer> ranks	= new ArrayList<Integer>();
			for (int i=0; i< filterForm.getPlanMinRanks().length;  i++) {
				Integer val	= Integer.parseInt((String)filterForm.getPlanMinRanks()[i]);
				if ( val == 0 ) {
					ranks	= null;
					break;
				}
				else
					ranks.add(val);
			}
			arf.setPlanMinRank(ranks);
		}
		
//		if (!all.equals(filterForm.getRegionSelected()))
//			arf.setRegionSelected(filterForm.getRegionSelected() == null || filterForm.getRegionSelected() == -1 ? 
//					null : DynLocationManagerUtil.getLocation(filterForm.getRegionSelected(),false) );
		
		Set selectedRegions = null;
		if (filterForm.getRegionSelected() != null){
			if (!filterForm.getRegionSelected()[0].toString().equals("-1")) {
				selectedRegions = Util.getSelectedObjects(AmpCategoryValueLocations.class, filterForm.getRegionSelected());
			}
		}
		
		if (selectedRegions != null && selectedRegions.size() > 0) {
			arf.setLocationSelected(new HashSet());
			arf.getLocationSelected().addAll(selectedRegions);
		} else {
			arf.setLocationSelected(null);
			arf.setRelatedLocations(null);
		}
		
		if (!all.equals(filterForm.getApprovalStatusSelected())){
			if(filterForm.getApprovalStatusSelected() != null){
				ArrayList<String> appvals = new ArrayList<String>();
				for (int i = 0; i < filterForm.getApprovalStatusSelected().length; i++) {
					String id = String.valueOf("" + filterForm.getApprovalStatusSelected()[i]);
					appvals.add(id);
				}
			    arf.setApprovalStatusSelected(appvals);
			}
			else{
				arf.setApprovalStatusSelected(null);
			}
		}
		else 
			arf.setApprovalStatusSelected(null);
		
		if (filterForm.getSelectedStatuses() != null && filterForm.getSelectedStatuses().length > 0)
			arf.setStatuses(new HashSet());
		else
			arf.setStatuses(null);

		for (int i = 0; filterForm.getSelectedStatuses() != null && i < filterForm.getSelectedStatuses().length; i++) {
			Long statusId						= Long.parseLong( filterForm.getSelectedStatuses()[i].toString() );
			AmpCategoryValue value 	= (AmpCategoryValue) session.load(AmpCategoryValue.class, statusId);
			arf.getStatuses().add(value);
		}
		if (filterForm.getSelectedProjectCategory() != null && filterForm.getSelectedProjectCategory().length > 0)
			arf.setProjectCategory(new HashSet());
		else
			arf.setProjectCategory(null);
		
		for (int i = 0; filterForm.getSelectedProjectCategory() != null && i < filterForm.getSelectedProjectCategory().length; i++) {
			Long id = Long.parseLong(filterForm.getSelectedProjectCategory()[i] + "");
			AmpCategoryValue value = (AmpCategoryValue) CategoryManagerUtil.getAmpCategoryValueFromDb(id);
			arf.getProjectCategory().add(value);
		}

		if (filterForm.getSelectedFinancingInstruments() != null && filterForm.getSelectedFinancingInstruments().length > 0)
			arf.setFinancingInstruments(new HashSet());
		else
			arf.setFinancingInstruments(null);
		
		for (int i = 0; filterForm.getSelectedFinancingInstruments() != null && i < filterForm.getSelectedFinancingInstruments().length; i++) {
			Long id = Long.parseLong(filterForm.getSelectedFinancingInstruments()[i] + "");
			AmpCategoryValue value = (AmpCategoryValue) CategoryManagerUtil.getAmpCategoryValueFromDb(id);
			arf.getFinancingInstruments().add(value);
		}

		if (filterForm.getSelectedTypeOfAssistance() != null && filterForm.getSelectedTypeOfAssistance().length > 0) {
			arf.setTypeOfAssistance(new HashSet<AmpCategoryValue>());
			for (int i = 0; i < filterForm.getSelectedTypeOfAssistance().length; i++) {
				Long id = filterForm.getSelectedTypeOfAssistance()[i];
				AmpCategoryValue value = CategoryManagerUtil.getAmpCategoryValueFromDb(id);
				if (value != null)
					arf.getTypeOfAssistance().add(value);
			}
		} else
			arf.setTypeOfAssistance(null);

		if (filterForm.getPageSize() != null) {
			arf.setPageSize(filterForm.getPageSize()); // set page size in the
			// ARF filter
		}

		arf.setRisks(Util.getSelectedObjects(AmpCategoryValue.class, filterForm.getSelectedRisks()));

		arf.setGovernmentApprovalProcedures(filterForm.getGovernmentApprovalProcedures());
		arf.setUnallocatedLocation(filterForm.getUnallocatedLocation());
		arf.setJointCriteria(filterForm.getJointCriteria());

		if (filterForm.getSelectedDonorTypes() != null && filterForm.getSelectedDonorTypes().length > 0) {
			arf.setDonorTypes( new HashSet<AmpOrgType>() );
			arf.setSelectedDonorTypes( new HashSet<AmpOrgType>() );
			for (int i = 0; i < filterForm.getSelectedDonorTypes().length; i++) {
				Long id = Long.parseLong("" + filterForm.getSelectedDonorTypes()[i]);
				AmpOrgType type = DbUtil.getAmpOrgType(id);
				if (type != null) {
					arf.getDonorTypes().add(type);
					arf.getSelectedDonorTypes().add(type);
				}
			}
		} else{
			arf.setDonorTypes(null);
			arf.setSelectedDonorTypes(null);
		}

		if (filterForm.getSelectedDonorGroups() != null && filterForm.getSelectedDonorGroups().length > 0) {
			arf.setDonorGroups(new HashSet<AmpOrgGroup>());
			arf.setSelectedDonorGroups(new HashSet<AmpOrgGroup>() );
			for (int i = 0; i < filterForm.getSelectedDonorGroups().length; i++) {
				Long id = Long.parseLong("" + filterForm.getSelectedDonorGroups()[i]);
				AmpOrgGroup grp = DbUtil.getAmpOrgGroup(id);
				if (grp != null){
					arf.getDonorGroups().add(grp);
					arf.getSelectedDonorGroups().add(grp);
				}
			}
		} else {
			arf.setDonorGroups(null);
			arf.setSelectedDonorGroups(null);
		}
		if (filterForm.getSelectedBudgets() != null) {
			int selectedValue;
			if ( filterForm.getSelectedBudgets().length == 1 ) {
				selectedValue	= Integer.parseInt( (String)filterForm.getSelectedBudgets()[0] );
			}
			else 
				selectedValue	= -1;
			switch (selectedValue) {
			case -1:
				arf.setBudget(null);
				break;
			case 1:
				arf.setBudget(true);
				break;
			case 0:
				arf.setBudget(false);
				break;

			}
		}
		else 
			arf.setBudget(null);
		
		arf.setJustSearch(filterForm.getJustSearch());

		arf.setRenderStartYear((filterForm.getRenderStartYear() != -1) ? filterForm.getRenderStartYear() : 0);
		arf.setRenderEndYear((filterForm.getRenderEndYear() != -1) ? filterForm.getRenderEndYear() : 0);

		DecimalFormat custom = new DecimalFormat();
		DecimalFormatSymbols ds = new DecimalFormatSymbols();
		ds.setDecimalSeparator((!"CUSTOM".equalsIgnoreCase(filterForm.getCustomDecimalSymbol()) ? filterForm.getCustomDecimalSymbol().charAt(0) : filterForm.getCustomDecimalSymbolTxt().charAt(0)));
		ds
				.setGroupingSeparator((!"CUSTOM".equalsIgnoreCase(filterForm.getCustomGroupCharacter()) ? filterForm.getCustomGroupCharacter().charAt(0) : filterForm.getCustomGroupCharacterTxt()
						.charAt(0)));
		custom.setMaximumFractionDigits((filterForm.getCustomDecimalPlaces() != -1) ? filterForm.getCustomDecimalPlaces() : 99);
		custom.setGroupingUsed(filterForm.getCustomUseGrouping());
		custom.setGroupingSize(filterForm.getCustomGroupSize());
		custom.setDecimalFormatSymbols(ds);
		arf.setAmountinthousand(filterForm.getAmountinthousands());
		arf.setCurrentFormat(custom);

		arf.setBeneficiaryAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedBeneficiaryAgency(), AmpOrganisation.class));
		arf.setResponsibleorg(ReportsUtil.processSelectedFilters(filterForm.getSelectedresponsibleorg(), AmpOrganisation.class));
		
		arf.setImplementingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedImplementingAgency(), AmpOrganisation.class));
		arf.setExecutingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedExecutingAgency(), AmpOrganisation.class));
		arf.setDonnorgAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedDonnorAgency(), AmpOrganisation.class));
		if ( arf.getDonnorgAgency() != null ) {
			for (AmpOrganisation tempOrg: arf.getDonnorgAgency() ) {
				if ( arf.getDonorGroups() == null )
					arf.setDonorGroups(new HashSet<AmpOrgGroup>() );
				if ( arf.getDonorTypes() == null )
					arf.setDonorTypes( new HashSet<AmpOrgType>() );
				
				arf.getDonorGroups().add( tempOrg.getOrgGrpId() );
				arf.getDonorTypes().add( tempOrg.getOrgGrpId().getOrgType() );
			}
		}
		
		arf.setProjectCategory(ReportsUtil.processSelectedFilters(filterForm.getSelectedProjectCategory(), AmpCategoryValue.class));
		
		if ( filterForm.getSelectedArchivedStatus() == null || filterForm.getSelectedArchivedStatus().length != 1 ) {
			arf.setShowArchived(null);
		}
		else {
			String selection 	= (String) filterForm.getSelectedArchivedStatus()[0];
			if ("1".equals(selection) )
				arf.setShowArchived(false);
			else
				arf.setShowArchived(true);
		} 
			
		
		if(filterForm.getDisbursementOrders()!=null){
			if (filterForm.getDisbursementOrders().length == 1) {
				Integer disbOrder	= Integer.parseInt( (String)filterForm.getDisbursementOrders()[0] );
				if(disbOrder == 0){
					arf.setDisbursementOrderRejected(false);
				}else if(disbOrder == 1){
					arf.setDisbursementOrderRejected(true);
				}else{
					arf.setDisbursementOrderRejected(null);
				}
			}
			if (filterForm.getDisbursementOrders().length > 1) {
				arf.setDisbursementOrderRejected(null);
			}
		}
			

		if ( filterForm.getSourceIsReportWizard() != null && filterForm.getSourceIsReportWizard() ) {
			httpSession.setAttribute(ReportWizardAction.SESSION_FILTER, arf);
			return mapping.findForward("reportWizard");
		}
			
		httpSession.setAttribute(ArConstants.REPORTS_FILTER, arf);
		if (arf.isPublicView())
			return mapping.findForward("publicView");
		return mapping.findForward(arf.isWidget() ? "mydesktop" : "reportView");
	}

	public void reset(ActionForm form, HttpServletRequest request, ActionMapping mapping) {
		request.setAttribute("apply", null);
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		filterForm.setSelectedRisks(null);
		filterForm.setSelectedSectors(null);
		filterForm.setSelectedStatuses(null);
		filterForm.setSelectedNatPlanObj(null);
		filterForm.setJustSearch(null);
		filterForm.setSelectedPrimaryPrograms(null);
		filterForm.setSelectedSecondarySectors(null);
		filterForm.setSelectedArchivedStatus(new Object[]{"1"});
		HttpSession httpSession = request.getSession();
		AmpApplicationSettings tempSettings=getAppSetting(request);
		if (tempSettings != null) {
			filterForm.setCurrency(tempSettings.getCurrency().getAmpCurrencyId());
			String name = "- " + tempSettings.getCurrency().getCurrencyName();
			if (httpSession.getAttribute(ArConstants.SELECTED_CURRENCY) == null)
				httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
			if(tempSettings.getFiscalCalendar() != null && tempSettings.getFiscalCalendar().getAmpFiscalCalId() != null) {
				filterForm.setCalendar(tempSettings.getFiscalCalendar().getAmpFiscalCalId());
			}
		}

		// Long
		// fromYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE));
		filterForm.setFromYear(-1l);
		filterForm.setComputedYear(-1);
		// Long
		// toYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE));
		filterForm.setToYear(-1l);
		filterForm.setFromMonth(-1);
		filterForm.setToMonth(-1);
		filterForm.setFromDate(null);
		filterForm.setToDate(null);

		filterForm.setLineMinRanks(null);
		filterForm.setPlanMinRanks(null);
		filterForm.setText(null);
		filterForm.setPageSize(null);
		filterForm.setGovernmentApprovalProcedures(null);
		filterForm.setJointCriteria(null);
		filterForm.setRegionSelected(null);
		filterForm.setApprovalStatusSelected(null);
		//filterForm.setDonorGroups(null);
		//filterForm.setDonorTypes(null);
		//filterForm.setExecutingAgency(null);
		//filterForm.setBeneficiaryAgency(null);
//		filterForm.setDonnorAgency(null);
		//filterForm.setImplementingAgency(null);
		//filterForm.reset(mapping, request);
		
		filterForm.setDonorElements(null);
		filterForm.setRelatedAgenciesElements(null);
		
	}

	public void resetFormat(ActionForm form, HttpServletRequest request, ActionMapping mapping) {
		HttpSession httpSession = request.getSession();
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		AmpARFilter arf = (AmpARFilter) httpSession.getAttribute(ArConstants.REPORTS_FILTER);
		arf.setCurrentFormat(null);
		filterForm.setCustomDecimalSymbol(null);
		filterForm.setCustomDecimalPlaces(null);
		filterForm.setCustomGroupCharacter(null);
		filterForm.setCustomUseGrouping(null);
		filterForm.setCustomGroupSize(null);
		filterForm.setResetFormat(null);
		filterForm.setAmountinthousands(null);
	}

	private Integer getDefaultStartYear(HttpServletRequest request) {
		AmpApplicationSettings tempSettings = getAppSetting(request);
		Integer rStart = -1;
		if (tempSettings != null && tempSettings.getReportStartYear() != null && tempSettings.getReportStartYear().intValue() != 0) {
			rStart = tempSettings.getReportStartYear();
		} else {
			String gvalue = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
			if (gvalue != null && !"".equalsIgnoreCase(gvalue) && Integer.parseInt(gvalue) > 0) {
				rStart = Integer.parseInt(gvalue);
			}
		}

		return rStart;
	}

	private Integer getDefaultEndYear(HttpServletRequest request) {
		AmpApplicationSettings tempSettings = getAppSetting(request);
		Integer rEnd = -1;
		if (tempSettings != null && tempSettings.getReportEndYear() != null && tempSettings.getReportEndYear().intValue() != 0) {
			rEnd = tempSettings.getReportEndYear();
		} else {
			String gvalue = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
			if (gvalue != null && !"".equalsIgnoreCase(gvalue) && Integer.parseInt(gvalue) > 0) {
				rEnd = Integer.parseInt(gvalue);
			}
		}
		return rEnd;
	}

	
	private Integer getYearOnCalendar(AmpFiscalCalendar calendar, Integer pyear, AmpFiscalCalendar defCalendar) {
		if (pyear==null) return 0;
		
		Integer year=null;
		try {
			Date testDate=new SimpleDateFormat("dd/MM/yyyy").parse("11/09/"+pyear);
			ICalendarWorker work1=defCalendar.getworker();
			work1.setTime(testDate);
			ICalendarWorker work2=calendar.getworker();
			work2.setTime(testDate);
			int diff=work2.getYearDiff(work1);
			pyear=pyear+diff;
			return pyear;
		} catch (Exception e) {
			logger.error("Can't get year on calendar",e);
		}
		return year;
	
	}
	private Integer getYearOnCalendar(Long calendarId, Integer pyear,AmpApplicationSettings 	tempSettings ) {
		if (pyear==null) return 0;
		AmpFiscalCalendar	cal = FiscalCalendarUtil.getAmpFiscalCalendar(calendarId);
	
		AmpFiscalCalendar defauCalendar=null;
		if  (tempSettings!=null)
			defauCalendar=tempSettings.getFiscalCalendar();
		
		if (defauCalendar==null){
			String calValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
			defauCalendar=FiscalCalendarUtil.getAmpFiscalCalendar(Long.parseLong(calValue));
		}
		
		if(defauCalendar==null) return 0;
		
		Integer year=getYearOnCalendar(cal, pyear,defauCalendar);
		cal=null;
		return year;
	}
	
	public static AmpApplicationSettings getAppSetting(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession.getAttribute(Constants.CURRENT_MEMBER);
		AmpApplicationSettings tempSettings = null;
		if (teamMember != null) {
			tempSettings = DbUtil.getMemberAppSettings(teamMember.getMemberId());
		}
		return tempSettings;
	}

	
}
