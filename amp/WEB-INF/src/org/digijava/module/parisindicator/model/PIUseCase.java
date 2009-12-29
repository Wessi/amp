package org.digijava.module.parisindicator.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.parisindicator.form.PIForm;
import org.digijava.module.parisindicator.helper.PIAbstractReport;
import org.digijava.module.parisindicator.helper.PIReport10a;
import org.digijava.module.parisindicator.helper.PIReport10b;
import org.digijava.module.parisindicator.helper.PIReport3;
import org.digijava.module.parisindicator.helper.PIReport4;
import org.digijava.module.parisindicator.helper.PIReport5a;
import org.digijava.module.parisindicator.helper.PIReport5b;
import org.digijava.module.parisindicator.helper.PIReport6;
import org.digijava.module.parisindicator.helper.PIReport7;
import org.digijava.module.parisindicator.helper.PIReport9;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;
import org.digijava.module.parisindicator.util.PIConstants;
import org.digijava.module.parisindicator.util.PIUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class PIUseCase {

	private static Logger logger = Logger.getLogger(PIUseCase.class);

	/*
	 * Receives the form and populates the collections used in the filters.
	 * TODO: Replace the form variable for all collections to decouple the
	 * Action from the UseCase, so this method can be used from elsewhere.
	 */
	public PIForm setupFiltersData(PIForm form, HttpServletRequest request) {
		if (form.getStatuses() == null || form.getStatuses().isEmpty()) {
			form.setStatuses(DbUtil.getAmpStatusFromCM(request));
		}
		if (form.getDonors() == null || form.getDonors().isEmpty()) {
			form.setDonors(DbUtil.getAllDonorOrgs());
		}
		if (form.getSectors() == null || form.getSectors().isEmpty()) {
			form.setSectors(SectorUtil.getAmpSectors());
		}
		if (form.getDonorGroups() == null || form.getDonorGroups().isEmpty()) {
			form.setDonorGroups(DbUtil.getAllOrgGroups());
		}
		if (form.getCalendars() == null || form.getCalendars().isEmpty()) {
			form.setCalendars(DbUtil.getAllFisCalenders());
		}
		if (form.getCurrencyTypes() == null || form.getCurrencyTypes().isEmpty()) {
			form.setCurrencyTypes(CurrencyUtil.getAllCurrencies(CurrencyUtil.ALL_ACTIVE));
		}
		if (form.getFinancingInstruments() == null || form.getFinancingInstruments().isEmpty()) {
			form.setFinancingInstruments(DbUtil.getAllFinancingInstruments());
		}
		return form;
	}

	/*
	 * Resets all filters to their base values, some values must be null as
	 * default and other needs always a value like the calendar, currency, etc.
	 */
	public void resetFilterSelections(PIForm form, ApplicationSettings appSettings) {

		if (appSettings.getFisCalId() != null) {
			form.setSelectedCalendar(DbUtil.getAmpFiscalCalendar(appSettings.getFisCalId()).getAmpFiscalCalId()
					.toString());
		} else {
			form.setSelectedCalendar(DbUtil.getAmpFiscalCalendar(DbUtil.getBaseFiscalCalendar()).getAmpFiscalCalId()
					.toString());
		}
		form.setSelectedCurrency(CurrencyUtil.getAmpcurrency(appSettings.getCurrencyId()).getCurrencyCode());
		form.setSelectedEndYear(Calendar.getInstance().get(Calendar.YEAR));
		form.setSelectedStartYear(Calendar.getInstance().get(Calendar.YEAR) - 2);
		form.setStartYears(new int[10]);
		form.setEndYears(new int[10]);
		int auxYear = form.getSelectedEndYear() - 5;
		for (int i = 0; i < 10; i++) {
			form.getStartYears()[i] = auxYear + i;
			form.getEndYears()[i] = auxYear + i;
		}

		form.setSelectedDonors(null);
		form.setSelectedDonorGroups(null);
		form.setSelectedSectors(null);
		form.setSelectedStatuses(null);
		form.setSelectedFinancingIstruments(null);
	}

	/*
	 * Gets the collection of PI reports from the DB and filters any report if
	 * necessary.
	 */
	public Collection<AmpAhsurveyIndicator> setupAvailablePIReports() {
		Collection<AmpAhsurveyIndicator> list = new ArrayList<AmpAhsurveyIndicator>();
		list = DbUtil.getAllAhSurveyIndicators();
		Iterator<AmpAhsurveyIndicator> iter = list.iterator();
		while (iter.hasNext()) {
			AmpAhsurveyIndicator aux = iter.next();
			if (checkReportName(aux.getIndicatorCode()) == null) {
				iter.remove();
			}
		}
		return list;
	}

	/*
	 * Checks the report name with the available PI reports.
	 */
	public String checkReportName(String name) {
		String ret = null;
		if (name == null) {
		} else if (name.equals(PIConstants.PARIS_INDICATOR_REPORT_3)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_4)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_5a)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_5b)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_6)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_7)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_9)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_10a)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_10b)) {
			ret = name;
		}
		return ret;
	}

	/*
	 * Returns an indicator object from the list of available reports given the
	 * report code.
	 */
	public AmpAhsurveyIndicator getPIReport(String code) {
		AmpAhsurveyIndicator ret = null;
		Collection<AmpAhsurveyIndicator> list = this.setupAvailablePIReports();
		Iterator<AmpAhsurveyIndicator> iter = list.iterator();
		while (iter.hasNext()) {
			AmpAhsurveyIndicator aux = iter.next();
			if (aux.getIndicatorCode().equals(code)) {
				ret = aux;
			}
		}
		return ret;
	}

	/*
	 * Executes part of the common logic for all reports and then creates the
	 * concrete report.
	 */
	public PIAbstractReport createReport(PIForm form) throws Exception {
		// Create the report.
		PIAbstractReport report = null;
		if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_3)) {
			report = new PIReport3();
		} else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_4)) {
			report = new PIReport4();
		} else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_5a)) {
			report = new PIReport5a();
		} else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_5b)) {
			report = new PIReport5b();
		} else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_6)) {
			report = new PIReport6();
		} else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_7)) {
			report = new PIReport7();
		} else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_9)) {
			report = new PIReport9();
		} else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10a)) {
			report = new PIReport10a();
		} else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10b)) {
			report = new PIReport10b();
		}

		// Get the common info from surveys and apply some filters.
		Collection<PIReportAbstractRow> preMainReportRows = null;
		AmpFiscalCalendar auxCalendar = DbUtil.getAmpFiscalCalendar(new Long(form.getSelectedCalendar()));
		AmpCurrency auxCurrency = CurrencyUtil.getAmpcurrency(form.getSelectedCurrency());
		Collection<AmpOrganisation> auxDonors = PIUtils.getDonorsCollection(form.getSelectedDonors());
		Collection<AmpOrgGroup> auxDonorGroups = PIUtils.getDonorGroups(form.getSelectedDonorGroups());
		Collection<AmpSector> auxSectors = PIUtils.getSectors(form.getSelectedSectors());
		Collection<AmpCategoryValue> auxStatuses = PIUtils.getStatuses(form.getSelectedStatuses());
		Collection<AmpCategoryValue> auxFinancingInstruments = PIUtils.getFinancingInstruments(form
				.getSelectedFinancingIstruments());
		if (!report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10a)
				&& !report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10b)) {
			Collection<AmpAhsurvey> commonData = getCommonSurveyData(auxDonors, auxDonorGroups);

			// Execute the logic for generating each report.
			preMainReportRows = report.generateReport(commonData, form.getSelectedStartYear(), form
					.getSelectedEndYear(), auxCalendar, auxCurrency, auxSectors, auxStatuses, auxFinancingInstruments);
		} else {
			if (report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10a)) {
				Collection<AmpOrganisation> commonData10a = getCommonSurveyDataForPI10a(auxDonors, auxDonorGroups);

				// Execute the logic for generating each report.
				preMainReportRows = report.generateReport10a(commonData10a, form.getSelectedStartYear(), form
						.getSelectedEndYear(), auxCalendar, auxCurrency, auxSectors, auxStatuses,
						auxFinancingInstruments);
			}
			if (report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10b)) {
				Collection<AmpOrganisation> commonData10b = getCommonSurveyDataForPI10b(auxDonors, auxDonorGroups);

				// Execute the logic for generating each report.
				preMainReportRows = report.generateReport10b(commonData10b, form.getSelectedStartYear(), form
						.getSelectedEndYear(), auxCalendar, auxCurrency, auxSectors, auxStatuses,
						auxFinancingInstruments);
			}
		}

		// Postprocess the report if needed.
		Collection<PIReportAbstractRow> postMainReportRows = report.reportPostProcess(preMainReportRows, form
				.getSelectedStartYear(), form.getSelectedEndYear());

		if (report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_5a)) {
			PIReport5a auxReport = new PIReport5a();
			int[][] miniTable = auxReport.createMiniTable(postMainReportRows, form.getSelectedStartYear(), form
					.getSelectedEndYear());
			report.setMiniTable(miniTable);
		} else if (report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_5b)) {
			PIReport5b auxReport = new PIReport5b();
			int[][] miniTable = auxReport.createMiniTable(postMainReportRows, form.getSelectedStartYear(), form
					.getSelectedEndYear());
			report.setMiniTable(miniTable);
		}

		report.setReportRows(postMainReportRows);

		return report;
	}

	/*
	 * Returns a collection of AmpAhSurvey objects filtered by donor and donor
	 * group.
	 */
	public Collection<AmpAhsurvey> getCommonSurveyData(Collection<AmpOrganisation> filterDonors,
			Collection<AmpOrgGroup> filterDonorGroups) {

		Collection<AmpAhsurvey> commonData = null;
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			// Set the query to return AmpAhSurvey objects.
			Criteria criteria = session.createCriteria(AmpAhsurvey.class);
			criteria.createAlias("pointOfDeliveryDonor", "podd1");
			criteria.createAlias("pointOfDeliveryDonor.orgTypeId", "podd2");
			// Set the filter for Multilateral and Bilateral PoDDs.
			criteria.add(Restrictions.in("podd2.orgTypeCode", new String[] { PIConstants.ORG_GRP_MULTILATERAL,
					PIConstants.ORG_GRP_BILATERAL }));
			// If needed, filter for organizations.
			if (filterDonors != null) {
				criteria.add(Restrictions.in("pointOfDeliveryDonor", filterDonors));
			}
			// If needed, filter for organization groups.
			if (filterDonorGroups != null) {
				criteria.add(Restrictions.in("podd1.orgGrpId", filterDonorGroups));
			}
			commonData = criteria.list();
		} catch (Exception e) {
			logger.error(e);
		}
		return commonData;
	}

	public Collection<AmpOrganisation> getCommonSurveyDataForPI10a(Collection<AmpOrganisation> filterDonors,
			Collection<AmpOrgGroup> filterDonorGroups) {

		Collection<AmpOrganisation> commonData = new ArrayList<AmpOrganisation>();
		Session session = null;
		try {
			// TODO: change this code to use restrictions like the
			// getCommonSurveyData() method.
			session = PersistenceManager.getRequestDBSession();
			List<AmpCalendar> calendars = session.createCriteria(AmpCalendar.class).list();
			Iterator<AmpCalendar> iterCalendar = calendars.iterator();
			while (iterCalendar.hasNext()) {
				AmpCalendar auxCalendar = iterCalendar.next();
				Set<AmpOrganisation> auxOrganisations = auxCalendar.getOrganisations();
				if (auxOrganisations != null) {
					Iterator<AmpOrganisation> iterOrganisations = auxOrganisations.iterator();
					while (iterOrganisations.hasNext()) {
						AmpOrganisation auxOrganisation = iterOrganisations.next();
						AmpOrgGroup auxOrgGrp = auxOrganisation.getOrgGrpId();
						// Filter for Multilateral and Bilateral PoDDs.
						if (auxOrgGrp.getOrgType().equals(PIConstants.ORG_GRP_BILATERAL)
								|| auxOrgGrp.getOrgType().equals(PIConstants.ORG_GRP_MULTILATERAL)) {
							boolean add = true;
							// If needed, filter for organizations.
							if (filterDonors != null) {
								if (!PIUtils.containOrganisations(filterDonors, auxOrganisation)) {
									add = false;
								}
							}
							// If needed, filter for organization groups.
							if (filterDonorGroups != null) {
								if (!PIUtils.containOrgGrps(filterDonorGroups, auxOrgGrp)) {
									add = false;
								}
							}
							if (add) {
								commonData.add(auxOrganisation);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return commonData;
	}

	public Collection<AmpOrganisation> getCommonSurveyDataForPI10b(Collection<AmpOrganisation> filterDonors,
			Collection<AmpOrgGroup> filterDonorGroups) {

		/*Collection<AmpOrganisation> commonData = new ArrayList<AmpOrganisation>();
		Session session = null;
		try {
			// TODO: change this code to use restrictions like the
			// getCommonSurveyData() method.
			session = PersistenceManager.getRequestDBSession();
			List<AmpActivityDocument> documents = session.createCriteria(AmpActivityDocument.class).list();
			Iterator<AmpActivityDocument> iterDocs = documents.iterator();
			while (iterDocs.hasNext()) {
				AmpActivityDocument auxDocs = iterDocs.next();
				Set<AmpOrganisation> auxOrganisations = auxDocs.getOrganisations();
				if (auxOrganisations != null) {
					Iterator<AmpOrganisation> iterOrganisations = auxOrganisations.iterator();
					while (iterOrganisations.hasNext()) {
						AmpOrganisation auxOrganisation = iterOrganisations.next();
						AmpOrgGroup auxOrgGrp = auxOrganisation.getOrgGrpId();
						// Filter for Multilateral and Bilateral PoDDs.
						if (auxOrgGrp.getOrgType().equals(PIConstants.ORG_GRP_BILATERAL)
								|| auxOrgGrp.getOrgType().equals(PIConstants.ORG_GRP_MULTILATERAL)) {
							boolean add = true;
							// If needed, filter for organizations.
							if (filterDonors != null) {
								if (!PIUtils.containOrganisations(filterDonors, auxOrganisation)) {
									add = false;
								}
							}
							// If needed, filter for organization groups.
							if (filterDonorGroups != null) {
								if (!PIUtils.containOrgGrps(filterDonorGroups, auxOrgGrp)) {
									add = false;
								}
							}
							if (add) {
								commonData.add(auxOrganisation);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return commonData;*/
		//TODO: Implement this method when 6012 is fixed.
		return null;
	}
}