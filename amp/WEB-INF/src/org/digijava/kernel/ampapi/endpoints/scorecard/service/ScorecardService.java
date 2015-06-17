package org.digijava.kernel.ampapi.endpoints.scorecard.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ActivityUpdate;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ColoredCell;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ColoredCell.Colors;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.ampapi.endpoints.util.CalendarUtil;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpScorecardNoUpdateOrganisation;
import org.digijava.module.aim.dbentity.AmpScorecardSettings;
import org.digijava.module.aim.dbentity.AmpScorecardSettingsCategoryValue;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import clover.org.apache.commons.lang.StringUtils;
import clover.org.apache.log4j.Logger;

/**
 * Service class for Scorecard generation
 * 
 * @author Emanuel Perez
 * 
 */
public class ScorecardService {

	private static final int DEFAULT_START_YEAR = 2010;
	private static final Logger LOGGER = Logger.getLogger(ScorecardService.class);
	private AmpScorecardSettings settings;
	private AmpFiscalCalendar fiscalCalendar;
	private Double DEFAULT_THRESHOLD = 70d;

	public ScorecardService() {
		getAmpScorecardSettings();
		Long gsCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
		this.fiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(gsCalendarId);

	}

	private void getAmpScorecardSettings() {
		Collection<AmpScorecardSettings> settingsList = DbUtil.getAll(AmpScorecardSettings.class);
		if (settingsList != null && !settingsList.isEmpty()) {
			settings = settingsList.iterator().next();
		} else {
			// set default values
			settings = new AmpScorecardSettings();
			settings.setPercentageThreshold(DEFAULT_THRESHOLD);
			settings.setValidationPeriod(false);
			settings.setClosedStatuses(new HashSet<AmpScorecardSettingsCategoryValue> ());
		}
	}

	public List<ActivityUpdate> getDonorActivityUpdates() {

		return getDonorActivityUpdates(null);
	}

	@SuppressWarnings("unchecked")
	public List<AmpScorecardNoUpdateOrganisation> getAllNoUpdateDonors() {
		int startYear = getDefaultStartYear();
		int endYear = getDefaultEndYear();
		Date startDate = CalendarUtil.getStartDate(fiscalCalendar.getAmpFiscalCalId(), startYear);
		Date endDate = CalendarUtil.getEndDate(fiscalCalendar.getAmpFiscalCalId(), endYear);
		Session session = PersistenceManager.getRequestDBSession();
		String queryString = "from " + AmpScorecardNoUpdateOrganisation.class.getName()
				+ " nuo where nuo.modifyDate >= :startDate and nuo.modifyDate <= :endDate";
		Query query = session.createQuery(queryString);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.list();
	}

	/**
	 * Returns the list of all ActivityUpdates that occurred on the system
	 * 
	 * @param allowedStatuses
	 * @return List<ActivityUpdate> , list with all ActivityUpdates
	 */
	public List<ActivityUpdate> getDonorActivityUpdates(final List<String> allowedStatuses) {
		final List<ActivityUpdate> activityUpdateList = new ArrayList<ActivityUpdate>();
		int startYear = getDefaultStartYear();
		int endYear = getDefaultEndYear();
		String gsCalendarId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		Date startDate = CalendarUtil.getStartDate(Long.valueOf(gsCalendarId), startYear);
		Date endDate = CalendarUtil.getEndDate(Long.valueOf(gsCalendarId), endYear);
		String pattern = "yyyy-MM-dd";
		final String formattedStartDate = new SimpleDateFormat(pattern).format(startDate);
		final String formattedEndDate = new SimpleDateFormat(pattern).format(endDate);
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String query = "SELECT distinct (l.id),r.organisation ,l.teamname, "
						+ " l.authorname,l.loggeddate,a.amp_id,l.modifydate "
						+ "FROM amp_audit_logger l, amp_activity_version a,amp_org_role r "
						+ "WHERE objecttype = 'org.digijava.module.aim.dbentity.AmpActivityVersion' "
						+ "AND a.amp_activity_id = l.objectid:: integer " + "AND r.activity=a.amp_activity_id "
						+ "AND    (EXISTS " + "        ( " + "        SELECT af.amp_donor_org_id "
						+ "       FROM   amp_funding af " + "      WHERE  r.organisation = af.amp_donor_org_id "
						+ "     AND    (( af.source_role_id IS NULL) "
						+ "     OR     af.source_role_id =( SELECT amp_role_id         FROM   amp_role "
						+ "  WHERE  role_code='DN')))) " + " AND modifydate>='" + formattedStartDate
						+ "' AND modifydate <= '" + formattedEndDate + "' ";

				if (allowedStatuses != null && !allowedStatuses.isEmpty()) {
					String status = StringUtils.join(allowedStatuses, ",");
					query += " AND approval_status IN (" + status + ") ";
				}
				query += "ORDER BY r.organisation,l.modifydate ";

				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) {
						ActivityUpdate activityUpdate = new ActivityUpdate();
						activityUpdate.setActivityId(rs.getString("amp_id"));
						activityUpdate.setAuditLoggerId(rs.getLong("id"));
						activityUpdate.setDonorId(rs.getLong("organisation"));
						activityUpdate.setWorkspaceName(rs.getString("teamname"));
						activityUpdate.setUserName(rs.getString("authorname"));
						activityUpdate.setLoggedDate(rs.getDate("loggeddate"));
						activityUpdate.setModifyDate(rs.getDate("modifydate"));
						activityUpdateList.add(activityUpdate);
					}
				}

			}
		});

		return activityUpdateList;
	}
	
	public List<ScorecardNoUpdateDonor> getAllDonors(final boolean filterNoUpdates, final boolean noUpdates) {
		
		final List<ScorecardNoUpdateDonor> donorsList = new ArrayList<ScorecardNoUpdateDonor>();
		
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				Map<Long, String> organisationsNames = QueryUtil.getTranslatedName(conn,"amp_organisation","amp_org_id","name");
				
				String query = "SELECT distinct(o.amp_org_id ),o.name  FROM   amp_organisation o,amp_org_role r   "
						+ "WHERE o.amp_org_id = r.organisation  AND (EXISTS (SELECT af.amp_donor_org_id "
						+ "FROM  amp_funding af WHERE  o.amp_org_id = af.amp_donor_org_id "
						+ "AND ((af.source_role_id IS NULL) OR af.source_role_id = (SELECT amp_role_id FROM amp_role WHERE role_code = 'DN')))) "
						+ "AND ( o.deleted IS NULL OR o.deleted = false ) ";
				
				if (filterNoUpdates) {
					query += "AND o.amp_org_id ";
					if (noUpdates) {
						query += "NOT ";
					}
					query += "IN (SELECT amp_donor_id from no_update_organisation)";
				}
				
				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) {
						ScorecardNoUpdateDonor donor = new ScorecardNoUpdateDonor();
						donor.setAmpDonorId(rs.getLong("amp_org_id"));
						if (ContentTranslationUtil.multilingualIsEnabled()) {
							donor.setName(organisationsNames.get(rs.getLong("amp_org_id")));
						} else {
							donor.setName(rs.getString("name"));
						}
						donorsList.add(donor);
					}
				}
			}
		});
			
		return donorsList;
	}
	

	/**
	 * Returns the list of all Activity Statuses from category values table
	 * (amp_category_value)
	 * 
	 * @return Collection<AmpCategoryValue>, the list with of all activity
	 *         statuses
	 */
	public static Collection<AmpCategoryValue> getAllCategoryValues() {
		Collection<AmpCategoryValue> responses = new ArrayList<AmpCategoryValue>();

		Session session = PersistenceManager.getRequestDBSession();
		String queryString = "FROM " + AmpCategoryValue.class.getName() + " acv "
        		+ "WHERE acv.ampCategoryClass.keyName = 'activity_status'";
        Query query = session.createQuery(queryString);
        responses = query.list();
        
        for (AmpCategoryValue acv : responses) {
        	acv.setValue(TranslatorWorker.translateText(acv.getValue()));
        }

		return responses;
	}

	/**
	 * Gets the default start year for the donor scorecard. It tries to get it
	 * from Global Settings, if it is not defined it uses a default value of
	 * 2010.
	 * 
	 * @return the default start year for generating the donor scorecard
	 */
	private int getDefaultStartYear() {
		String defaultStartYear = FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
		int startYear = DEFAULT_START_YEAR;
		if (defaultStartYear != null && !"".equalsIgnoreCase(defaultStartYear)
				&& Integer.parseInt(defaultStartYear) > 0) {
			startYear = Integer.parseInt(defaultStartYear);
		}
		return startYear;
	}

	/**
	 * Gets the default end year for the donor scorecard. It tries to get it
	 * from Global Settings, if it is not defined it uses the current year.
	 * 
	 * @return the default end year for generating the donor scorecard
	 */
	private int getDefaultEndYear() {
		String defaultEndYear = FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
		int endYear = Calendar.getInstance().get(Calendar.YEAR);
		if (defaultEndYear != null && !"".equalsIgnoreCase(defaultEndYear) && Integer.parseInt(defaultEndYear) > 0) {
			endYear = Integer.parseInt(defaultEndYear);
		}
		return endYear;
	}

	/**
	 * Returns a list of all the donors
	 * 
	 * @return List <String>
	 */
	public List<AmpOrganisation> getDonors() {
		List<AmpOrganisation> donors = new ArrayList<AmpOrganisation>();
		List<JsonBean> donorJson = QueryUtil.getDonors();
		for (JsonBean bean : donorJson) {
			AmpOrganisation donor = new AmpOrganisation();
			donor.setName(bean.getString("name"));
			donor.setAmpOrgId(Long.valueOf(bean.getString("id")));
			donor.setAcronym(bean.getString("acronym"));
			donors.add(donor);
		}
		return donors;
	}

	/**
	 * Returns a list of all quarters that will span the donor scorecard. The
	 * start and end of the period is defined through Global Settings:
	 * START_YEAR_DEFAULT_VALUE and END_YEAR_DEFAULT_VALUE
	 * 
	 * @return List<Quarter>, the list of the quarters that will represent the
	 *         headers of the donor scorecard file.
	 */
	public List<Quarter> getQuarters() {
		final List<Quarter> quarters = new ArrayList<Quarter>();
		int startYear = getDefaultStartYear();
		int endYear = getDefaultEndYear();
		String gsCalendarId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		Date startDate = CalendarUtil.getStartDate(Long.valueOf(gsCalendarId), startYear);
		Date endDate = CalendarUtil.getEndDate(Long.valueOf(gsCalendarId), endYear);
		try {
			ICalendarWorker worker = fiscalCalendar.getworker();
			Date currentDate = new Date(startDate.getTime());
			int index = 1;
			while (currentDate.compareTo(endDate) < 1) {
				for (int i = 1; i <= 4; i++) {
					worker.setTime(currentDate);
					Quarter quarter = new Quarter(fiscalCalendar, i, worker.getYear());
					quarters.add(quarter);
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDate);
					cal.add(Calendar.MONTH, 3 * index);
					index++;
					currentDate.setTime(cal.getTimeInMillis());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Couldn't generate quarters ", e);
		}

		return quarters;
	}
	
	/**
	 * Returns the last past quarter
	 * 
	 * @return Quarter, the last past quarter
	 */
	public Quarter getPastQuarter() {
		List<Quarter> quarters = getQuarters();
		Quarter quarter = null;
		
		int i = 1;
		if (quarters.size() > 0) {
			while ( i < quarters.size() && quarters.get(i).getQuarterStartDate().before(new Date())) {
				i++;
			};
			
			quarter = quarters.get(i-1);
		} 
		
		return quarter;
	}

	/**
	 * Generates the Map<Long, Map<String, ColoredCell>> with the data of the
	 * ColoredCells that will be used to create the donor scorecard
	 * 
	 * @param activityUpdates
	 *            the List of all ActivityUpdates that took place during the
	 *            period for which the scorecard will be generated.
	 * @return a Map<Long, Map<String, ColoredCell>> with all ColoredCells
	 *         filled with the appropiate colors. For each quarter and donor
	 *         there is a ColoredCell.
	 */
	public Map<Long, Map<String, ColoredCell>> getOrderedScorecardCells(List<ActivityUpdate> activityUpdates) {
		Map<Long, Map<String, ColoredCell>> data = initializeScorecardCellData();
		data = countActivityUpdates(activityUpdates, data);
		data = processCells(data);
		return data;
	}

	/**
	 * Sets the fill color for every ColoredCell on Map<Long, Map<String,
	 * ColoredCell>> based on the rules: - Green cell: If a donor has updated
	 * more projects that the number defined by doing: Number of Updated
	 * Projects by donor on a quarter > = Threshold (determined from
	 * AmoScorecardSettings) X Total live projects of a donor on a given quarter
	 * -Yellow cell: if a donor updates projects during the grace period, the
	 * update occurred is counted double. It is counted on the quarter to which
	 * the grace period belongs; and it is also counted on the current quarter.
	 * If the updates performed on the grace period make the number of updates
	 * to reach or surpass the threshold for that quarter, then the previous
	 * quarter is marked with yellow.
	 * 
	 * @param data
	 *            Map<Long, Map<String, ColoredCell>>
	 * @return the Map<Long, Map<String, ColoredCell>> with the ColoredCell
	 *         filled with colors.
	 */
	private Map<Long, Map<String, ColoredCell>> processCells(final Map<Long, Map<String, ColoredCell>> data) {
		Set<AmpScorecardSettingsCategoryValue> statuses = settings.getClosedStatuses();
		String closedStatuses = "";
		for (AmpScorecardSettingsCategoryValue status : statuses) {
			closedStatuses += "" + status.getAmpCategoryValueStatus().getId() + ",";
		}
		if (!closedStatuses.equals("")) {
			closedStatuses = closedStatuses.substring(0, closedStatuses.length() - 1);
		}
		final String status = closedStatuses;
		List<Quarter> quarters = getQuarters();

		for (final Quarter quarter : quarters) {
			PersistenceManager.getSession().doWork(new Work() {
				public void execute(Connection conn) throws SQLException {
					Double threshold = settings.getPercentageThreshold();
					if (threshold == null) {
						threshold = DEFAULT_THRESHOLD;
					}
					String quarterEndDate = new SimpleDateFormat("yyyy-MM-dd").format(quarter.getQuarterEndDate());
					//Get total (not completed nor deleted) activities  by donor at the end of a given quarter
					Object [] activityIds = getLatestActivityIdsByDate(quarterEndDate);
					String query = "select count (distinct (a.amp_id)) as total_activities,r.organisation as donor_id "
							+ "from amp_activity_version a, amp_org_role r,amp_organisation o,amp_activities_categoryvalues c,amp_category_value v "+
							" WHERE  r.activity=a.amp_activity_id  AND a.amp_activity_id = c.amp_activity_id "+
							"AND c.amp_categoryvalue_id = v.id "+
							"AND v.amp_category_class_id = (select id from amp_category_class where keyname='activity_status') ";
					if (!status.equals("")) {
						query += "AND c.amp_categoryvalue_id not in (" + status + " ) ";
					}
					query += "AND o.amp_org_id = r.organisation " + "AND ( o.deleted IS NULL OR o.deleted = false ) "
							+ "AND    (EXISTS  (SELECT af.amp_donor_org_id " + " FROM   amp_funding af "
							+ " WHERE  r.organisation = af.amp_donor_org_id "
							+ " AND    (( af.source_role_id IS NULL) "
							+ " OR     af.source_role_id =( SELECT amp_role_id         FROM   amp_role "
							+ " WHERE  role_code='DN')))) " + " and date_created <= '" + quarterEndDate + "' "
							+ " AND a.deleted is false ";
					if (activityIds.length > 0) {
						query += "AND a.amp_activity_id in (" + StringUtils.join(activityIds, ",") + ") ";
					}

					query += " group by r.organisation; ";

					try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
						ResultSet rs = rsi.rs;
						while (rs.next()) {
							Integer totalActivities = rs.getInt("total_activities");
							Long donorId = rs.getLong("donor_id");
							ColoredCell cell = data.get(donorId).get(quarter.toString());
							Integer totalUpdatedActivities = cell.getUpdatedActivites().size();
							Integer totalUpdatedActivitiesOnGracePeriod = cell.getUpdatedActivitiesOnGracePeriod()
									.size();
							//we don't process process cells for no update donors
							if (cell.getColor().equals(Colors.GRAY)) {
								continue;
							}
							if (totalUpdatedActivities >= (totalActivities * (threshold / 100))) {
								cell.setColor(Colors.GREEN);
							} else if ((totalUpdatedActivities + totalUpdatedActivitiesOnGracePeriod) >= (totalActivities * (threshold / 100))) {
								cell.setColor(Colors.YELLOW);
							}
						}
					}

				}
			});
		}
		return data;
	}
	
	/**
	 * Gets the latest activity ids for all existing activity that were updated before the end of a quarter.
	 *  
	 * We can have an activity that is 'on going' on Quarter 3 and 'completed' on quarter 4. In order to 
	 * validate quarter 3 correctly we need to know which is the latest version of the activity that was updated
	 * before the quarter end and we should also omit updates and versions that happened after the end of the quarter.
	 *   
	 * @param endPeriodDate, the end date of a quarter
	 * @return Object [] with the ids of the latest versions of an activity for the quarter
	 */
	public Object [] getLatestActivityIdsByDate (final String endPeriodDate) {
		final List <Long> activityIds = new ArrayList <Long> ();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String query = "select max(amp_activity_id) as amp_activity_id,amp_id from amp_activity_version "+
								"where deleted is false and date_updated <= '"+endPeriodDate+"' "+
								"group by amp_id";
				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) {
						activityIds.add(rs.getLong("amp_activity_id"));
					}
				}
				
			}});
		return activityIds.toArray();
		
	}

	/**
	 * Count the number of activities that were updated on a given quarter for a
	 * donor.
	 * 
	 * @param activityUpdates
	 *            the list of activity of all activity updates
	 * @param data
	 *            the structure holding the ColoredCells for each donor and
	 *            quarter.
	 * @return the Map<Long, Map<String, ColoredCell>> with ColoredCells
	 *         containing the amount of activity updates.
	 */
	private Map<Long, Map<String, ColoredCell>> countActivityUpdates(List<ActivityUpdate> activityUpdates,
			Map<Long, Map<String, ColoredCell>> data) {
		ICalendarWorker worker = fiscalCalendar.getworker();
		for (ActivityUpdate activityUpdate : activityUpdates) {
			Long donorId = activityUpdate.getDonorId();
			worker.setTime(activityUpdate.getModifyDate());
			Quarter quarter = new Quarter(fiscalCalendar, activityUpdate.getModifyDate());
			ColoredCell cell = data.get(donorId).get(quarter.toString());
			LOGGER.info("Quarter" + quarter);
			if (isUpdateOnGracePeriod(activityUpdate.getModifyDate())) {
				Quarter previousQuarter = quarter.getPreviousQuarter();
				ColoredCell previousQuarterCell = data.get(donorId).get(previousQuarter.toString());
				if (!isFirstQuarterOfPeriod(previousQuarterCell)) {
					previousQuarterCell.getUpdatedActivitiesOnGracePeriod().add(activityUpdate.getActivityId());
				}
			}
			cell.getUpdatedActivites().add(activityUpdate.getActivityId());
		}
		return data;
	}

	private boolean isFirstQuarterOfPeriod(ColoredCell previousQuarterCell) {
		return (previousQuarterCell == null);
	}

	/**
	 * Checks if a given date is on the Grace period of a quarter. If
	 * AmpScorecardSettings property's validationPeriod is set to false, then
	 * Grace period is disabled and the result is always false. Otherwise it
	 * checks if the current date is between the current quarter start date and
	 * the (current Quarter start date + number of weeks of the validation
	 * period) if that is the case, the result is true
	 * 
	 * @param updateDate
	 *            , the date to check whether it is in the grave period or not.
	 * @return true if it is in the grace period (validation period), false
	 *         otherwise
	 */
	private boolean isUpdateOnGracePeriod(Date updateDate) {
		boolean isOnGracePeriod = false;
		if (settings != null && settings.getValidationPeriod() != null && settings.getValidationPeriod()) {
			Integer weekNumber = settings.getValidationTime();
			try {
				Quarter quarter = new Quarter(fiscalCalendar, updateDate);
				Date quarterStartDate = quarter.getQuarterStartDate();
				Calendar calendarGracePeriod = Calendar.getInstance();
				calendarGracePeriod.setTime(quarterStartDate);
				calendarGracePeriod.add(Calendar.WEEK_OF_YEAR, weekNumber);
				if (quarterStartDate.compareTo(updateDate) <= 0
						&& updateDate.compareTo(calendarGracePeriod.getTime()) <= 0) {
					isOnGracePeriod = true;
				}

			} catch (Exception e) {
				LOGGER.warn("Couldn't get quarter for date " + updateDate);
			}
		}
		return isOnGracePeriod;
	}

	/**
	 * Populates the Map<Long, Map<String, ColoredCell>> with all the donors and
	 * quarters that will be included on the donor scorecard. It also creates
	 * the ColoredCells for every donor/quarter pair and fill it with
	 * Colors.Gray if it is in the NoUpdateDonor list for the given quarter
	 * 
	 * @return the initialized Map<Long, Map<String, ColoredCell>> with the
	 *         populated quarters and donors
	 */
	private Map<Long, Map<String, ColoredCell>> initializeScorecardCellData() {
		Map<Long, Map<String, ColoredCell>> data = new HashMap<Long, Map<String, ColoredCell>>();
		List<Quarter> quarters = getQuarters();
		List<JsonBean> donors = QueryUtil.getDonors();
		for (JsonBean donor : donors) {
			Long donorId = (Long) donor.get("id");
			Map<String, ColoredCell> quarterCellMap = new HashMap<String, ColoredCell>();
			for (Quarter quarter : quarters) {

				ColoredCell cell = new ColoredCell();
				cell.setDonorId(donorId);
				cell.setQuarter(quarter);
				quarterCellMap.put(quarter.toString(), cell);
			}
			data.put(donorId, quarterCellMap);

		}
		data = markNoUpdateDonorCells(data);
		return data;
	}

	/**
	 * Sets the ColoredCells' fill color to gray for the donors and quarters
	 * that have explicitly declared that they don't have projects updates for
	 * the given quarter
	 * 
	 * @param data
	 *            Map<Long, Map<String, ColoredCell>>, containing the
	 *            ColoredCells for every donor and quarter
	 * @return Map<Long, Map<String, ColoredCell>> the ColoredCells for each
	 *         donor and quarter filled with gray when it has been declared that
	 *         the donor/quarter don't have a project update
	 */
	private Map<Long, Map<String, ColoredCell>> markNoUpdateDonorCells(Map<Long, Map<String, ColoredCell>> data) {
		Collection<AmpScorecardNoUpdateOrganisation> noUpdateDonors = this.getAllNoUpdateDonors();

		for (AmpScorecardNoUpdateOrganisation noUpdateDonor : noUpdateDonors) {
			Quarter quarter = new Quarter(fiscalCalendar, noUpdateDonor.getModifyDate());
			ColoredCell noUpdateCell = data.get(noUpdateDonor.getAmpDonorId()).get(quarter.toString());
			noUpdateCell.setColor(Colors.GRAY);
		}
		return data;
	}
	
	/**
	 * Gets the count of active organisations for the past quarter
	 * 
	 * @return int the count of active organisations for the past quarter 
	 */
	public int getPastQuarterOrganizationsCount() {
		
		final IntWrapper orgCount = new IntWrapper();
		
		Quarter lastQuarter = getPastQuarter();
		Date startDate = lastQuarter == null ? new Date() : lastQuarter.getQuarterStartDate();
		String pattern = "yyyy-MM-dd";
		final String formattedStartDate = new SimpleDateFormat(pattern).format(startDate);
		final String formattedEndDate = new SimpleDateFormat(pattern).format(new Date());
		
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String query = "SELECT count(DISTINCT(r.organisation)) "
						+ "		FROM   amp_org_role r, amp_activity_version v, amp_organisation o "
						+ "WHERE  r.activity= v.amp_activity_id	AND  r.organisation=o.amp_org_id "
						+ "AND (EXISTS (SELECT af.amp_donor_org_id FROM amp_funding af "
						+ "WHERE  organisation = af.amp_donor_org_id AND  (( af.source_role_id IS NULL) "
						+ "OR af.source_role_id = (SELECT amp_role_id FROM amp_role WHERE  role_code='DN'))) OR r.role=1) "
						+ "AND (o.deleted IS NULL OR o.deleted = FALSE) "
						+ "AND  v.date_updated <= '"+ formattedEndDate + "'"
						+ "AND  v.date_updated >= '"+ formattedStartDate + "'";
				
				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) {
						orgCount.inc(rs.getInt("count"));
					}
				}  catch (Exception e) {
					LOGGER.error("Exception while getting org types amount:" + e.getMessage());
				}
			}
		});
		
		return orgCount.intValue();
	}
	
	/**
	 * Gets the count of users logged into the System in the past quarter
	 * 
	 * @return int the count of logged users in the past quarter
	 */
	public int getPastQuarterUsersCount() {
		String queryString = "select count(distinct o.authorName) from " + AmpAuditLogger.class.getName()
							+ " o where o.action = 'login'";
		
		return getPastQuarterObjectsCount(queryString, "loggedDate");
	}
	
	/**
	 * Gets the count of projects with action in the past quarter
	 * 
	 * @return int the count of projects with action in the past quarter
	 */
	public int getPastQuarterProjectsCount() {
		String queryString = "select count(distinct o.objectName) from " + AmpAuditLogger.class.getName()
							+ " o where o.objecttype = 'org.digijava.module.aim.dbentity.AmpActivityVersion'";
		
		return getPastQuarterObjectsCount(queryString, "modifyDate");
	}
	
	private int getPastQuarterObjectsCount(String queryString, String paramDate) {
		Quarter lastQuarter = getPastQuarter();
		Date startDate = lastQuarter == null ? new Date() : lastQuarter.getQuarterStartDate();
		
		String pattern = "yyyy-MM-dd";
		final String formattedStartDate = new SimpleDateFormat(pattern).format(startDate);
		final String formattedEndDate = new SimpleDateFormat(pattern).format(new Date());
		
		Session session = null;
		Query qry = null;
		int count = 0;

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString += " AND o." + paramDate + " >= '" + formattedStartDate + "' "
						  + "AND o." + paramDate + " <= '" + formattedEndDate + "'";
			
			qry = session.createQuery(queryString);
			count = ((Integer) qry.uniqueResult()).intValue();
		} catch (Exception e) {
			LOGGER.error("Exception while getting past quarter projects count:" + e.getMessage());
		}
		
		return count;
	}
}
