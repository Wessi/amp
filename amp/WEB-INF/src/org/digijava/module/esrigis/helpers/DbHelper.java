package org.digijava.module.esrigis.helpers;

/**
 * @author Diego Dimunzio
 */

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.*;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.visualization.util.DbUtil;
import org.hibernate.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.*;

public class DbHelper {
	private static Logger logger = Logger.getLogger(DbHelper.class);

	public static void delete(AmpMapConfig map) {
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			// beginTransaction();
			sess.delete(map);
			// tx.commit();
		} catch (Exception e) {
			if (e instanceof JDBCException)
				throw (JDBCException) e;
			logger.error("Exception " + e.toString());
			try {
				tx.rollback();
			} catch (HibernateException ex) {
				logger.error("rollback() failed");
				logger.error(ex.toString());
			}
		}
	}

	public static AmpMapConfig getMap(Long id) {
		Session session = null;
		AmpMapConfig map = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			map = (AmpMapConfig) session.load(AmpMapConfig.class, id);
		} catch (Exception e) {
			logger.error("Unable to get object of class "
					+ AmpMapConfig.class.getName() + " width id=" + id
					+ ". Error was:" + e);
		}
		return map;
	}

	public static void save(AmpMapConfig map) {
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			session.saveOrUpdate(map);
		} catch (Exception e) {
			logger.error("Unable to save item", e);
		}

	}

	public static List<AmpActivityVersion> getActivities(MapFilter filter,HttpServletRequest request)
			throws DgException {
		Long[] orgGroupIds = filter.getSelOrgGroupIds();
		List<AmpActivityVersion> activities = null;
		Long[] orgIds = filter.getOrgIds();
		Long[] implOrgIds = filter.getImplOrgIds();
		Long[] implOrgGroupIds = filter.getImplOrgGroupIds();

		Long[] orgtypeids = filter.getOrgtypeIds();
		int transactionType = filter.getTransactionType();
		TeamMember teamMember = filter.getTeamMember();
		// apply calendar filter
		Long fiscalCalendarId = filter.getFiscalCalendarId();
		Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter
				.getStartYear().intValue());
		Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter
				.getEndYear().intValue());
		Long[] locationIds = filter.getSelLocationIds();
		boolean locationCondition = locationIds != null
				&& locationIds.length > 0 && !locationIds[0].equals(-1l);
		Long[] zonesids = filter.getZoneIds();
		//boolean zonescondition = zonesids != null && zonesids.length > 1;
		Long[] sectorIds = filter.getSelSectorIds();
		boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
		boolean structureTypeCondition = filter.getSelStructureTypes() != null && !QueryUtil.inArray(-1l,filter.getSelStructureTypes() );
		Boolean isfilteredworkspace = TeamUtil.getAmpTeam(teamMember.getTeamId()).getComputation()!=null && TeamUtil.getAmpTeam(teamMember.getTeamId()).getComputation() && TeamUtil.getAmpTeam(teamMember.getTeamId()).getFilterDataSet()!=null
		&& TeamUtil.getAmpTeam(teamMember.getTeamId()).getFilterDataSet().size()>0;
		
		AmpCategoryValue budgetOn = null;
		AmpCategoryValue budgetOff = null;
		try {
			budgetOn = CategoryManagerUtil
					.getAmpCategoryValueFromDB(CategoryConstants.ACTIVITY_BUDGET_ON);
			budgetOff = CategoryManagerUtil
					.getAmpCategoryValueFromDB(CategoryConstants.ACTIVITY_BUDGET_OFF);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*
		 * We are selecting sectors which are funded In selected year by the
		 * selected organization
		 */
		try {
			String oql = "select distinct act from ";
			oql += AmpFundingDetail.class.getName()
					+ " as fd inner join fd.ampFundingId f ";
			oql += " inner join f.ampActivityId act ";

			if (filter.getFromPublicView() != null
					&& filter.getFromPublicView())
				oql += " inner join act.ampActivityGroupCached actGroup ";
			else
				oql += " inner join act.ampActivityGroup actGroup ";

			if (locationCondition) {
				oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
			}

			if (sectorCondition) {
				oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
			}
			
				oql += " inner join act.orgrole role  ";
			
			if (structureTypeCondition) {
				oql += " inner join act.structures str  ";
			}
			// Status
			if (filter.getSelprojectstatus() != null) {
				oql += " join  act.categories as categories ";
			}

			oql += "  where fd.adjustmentType = "
					+ CategoryManagerUtil.getAmpCategoryValueFromDB(
							CategoryConstants.ADJUSTMENT_TYPE_ACTUAL).getId();
			if (filter.getTransactionType() < 2) {
				oql += " and fd.transactionType =:transactionType  ";
			} else {
				oql += " and (fd.transactionType =0 or  fd.transactionType =1) ";
			}
			if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
				if (orgGroupIds != null && orgGroupIds.length > 0
						&& orgGroupIds[0] != -1) {
					oql += QueryUtil.getOrganizationQuery(true, orgIds,
							orgGroupIds);
				}
			} else {
				oql += QueryUtil.getOrganizationQuery(false, orgIds,
						orgGroupIds);
			}

			if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
				if (orgtypeids != null && orgtypeids.length > 0
						&& orgtypeids[0] != -1) {
					oql += QueryUtil.getOrganizationTypeQuery(true, orgIds,
							orgtypeids);
				}
			} else if (orgtypeids != null) {
				oql += QueryUtil.getOrganizationTypeQuery(true, orgIds,
						orgtypeids);
				;
			}

			oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)";

			if (filter.getFromPublicView() != null
					&& filter.getFromPublicView() == true) {
				oql += QueryUtil.getTeamQueryManagement();
			} else {
				if (!isfilteredworkspace){
					oql += QueryUtil.getTeamQuery(teamMember);
				}
			}

			// locations filter

			if (locationCondition) {
				locationIds = getAllDescendantsLocation(locationIds,
						DbUtil.getAmpLocations());
				filter.setSelLocationIds(locationIds);
				if (zonesids.length > 0) {
					zonesids = getAllDescendantsLocation(zonesids,
							DbUtil.getAmpLocations());
					Long[] both = (Long[]) ArrayUtils.addAll(locationIds,
							zonesids);
					filter.setSelLocationIds(both);
					oql += " and loc.id in ("
							+ QueryUtil.getInStatement(locationIds) + ","
							+ QueryUtil.getInStatement(zonesids) + ") ";
				} else {
					oql += " and loc.id in ("
							+ QueryUtil.getInStatement(locationIds) + ") ";
				}
			}

			if (sectorCondition) {
				oql += " and sec.id in (" + QueryUtil.getInStatement(sectorIds)
						+ ") ";
			}

			// Organization Type
			if (filter.getSelorganizationsTypes() != null) {
				oql += " and role.organisation.orgGrpId.orgType in ("
						+ QueryUtil.getInStatement(filter
								.getSelorganizationsTypes()) + ")";
			}

			// Implementing Agency
			if (implOrgIds == null || implOrgIds.length == 0
					|| implOrgIds[0] == -1) {
				if (implOrgGroupIds != null && implOrgGroupIds.length > 0
						&& implOrgGroupIds[0] != -1) {
					oql += QueryUtil.getOrganizationQuery(true, implOrgIds,
							implOrgGroupIds, Constants.IMPLEMENTING_AGENCY);
				}
			} else {
				oql += QueryUtil.getOrganizationQuery(false, implOrgIds,
						implOrgGroupIds, Constants.IMPLEMENTING_AGENCY);
			}

			// Project Status
			if (filter.getSelprojectstatus() != null) {
				oql += " and categories.id in ("
						+ QueryUtil
								.getInStatement(filter.getSelprojectstatus())
						+ ") ";
			}
			// On/Off budget
			if (filter.getOnBudget() != null) {
				if (filter.getOnBudget() == 1) {
					oql += " and categories.id in (" + budgetOn.getId() + ") ";
				} else if (filter.getOnBudget() == 2) {
					oql += " and categories.id in (" + budgetOff.getId() + ") ";
				}
			}
			// Type of assistance
			if (filter.getSeltypeofassistence() != null) {
				oql += " and f.typeOfAssistance in ("
						+ QueryUtil.getInStatement(filter
								.getSeltypeofassistence()) + ") ";
			}
			// Financing instrument

			if (filter.getSelfinancingInstruments() != null) {
				oql += " and f.financingInstrument in ("
						+ QueryUtil.getInStatement(filter
								.getSelfinancingInstruments()) + ") ";
			}
			// Structure Types
			if (structureTypeCondition) {
				oql += " and str.type.typeId in ("
						+ QueryUtil.getInStatement(filter
								.getSelStructureTypes()) + ") ";
			}

			oql += ActivityUtil.getApprovedActivityQueryString("act");
			// Show only activities that are not draft
			oql += ActivityUtil.getNonDraftActivityQueryString("act");

			// Additional clause to get the last version
			if (ActivityVersionUtil.isVersioningEnabled()) {
				oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";
				oql += " and (act.deleted = false or act.deleted is null)";
			}

			if (filter.isModeexport()) {
				String inactivities = "";
				ArrayList<BigInteger> filteractivities = getInActivities(filter
						.getReportfilterquery());
				for (Iterator iterator = filteractivities.iterator(); iterator
						.hasNext();) {
					BigInteger id = (BigInteger) iterator.next();
					if (inactivities == "") {
						inactivities += id.toString();
					} else {
						inactivities += "," + id.toString();
					}
				}
				oql += " and act.ampActivityId in(" + inactivities + ")";
			}
			
			if (isfilteredworkspace){
				String inactivities= "";
				AmpARFilter teamFilter = (AmpARFilter) request.getSession().getAttribute(ArConstants.TEAM_FILTER);
				ArrayList<BigInteger> filteractivities = getInActivities(teamFilter.getGeneratedFilterQuery());
				for (Iterator iterator = filteractivities.iterator(); iterator.hasNext();) {
					BigInteger id = (BigInteger) iterator.next();
					if (inactivities ==""){
						inactivities += id.toString();
					}else{
						inactivities +="," + id.toString();
					}
				}
				oql += " and act.ampActivityId in("+ inactivities +")";
			}
			
			Session session = PersistenceManager.getRequestDBSession();
			Query query = session.createQuery(oql);
			query.setDate("startDate", startDate);
			query.setDate("endDate", endDate);
			if (filter.getTransactionType() < 2) { // the option comm&disb is
				query.setLong("transactionType", transactionType);
			}
			
			activities = query.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load activities from db", e);
		}
		return activities;

	}

	public static ArrayList<BigInteger> getInActivities(String query)
			throws Exception {
		Session session = PersistenceManager.getRequestDBSession();
		ArrayList<BigInteger> result = (ArrayList<BigInteger>) session
				.createSQLQuery(query).list();
		return result;
	}

	public static List<AmpCategoryValueLocations> getLocations(
			MapFilter filter, String implementationLevel,HttpServletRequest request) throws DgException {
		List<AmpCategoryValueLocations> locations = new ArrayList<AmpCategoryValueLocations>();
		if (filter.getSelLocationIds() != null
				&& filter.getSelLocationIds().length > 0
				&& filter.getSelLocationIds()[0] != -1) {
			if (filter.getSelLocationIds().length == 1) {
				AmpCategoryValueLocations loc = LocationUtil
						.getAmpCategoryValueLocationById(filter
								.getSelLocationIds()[0]);
				locations.addAll(loc.getChildLocations());
				return locations;
			} else {
				for (int i = 0; i < filter.getSelLocationIds().length; i++) {
					AmpCategoryValueLocations loc = LocationUtil
							.getAmpCategoryValueLocationById(filter
									.getSelLocationIds()[i]);
					locations.add(loc);
				}
				return locations;
			}
		} else {
			
	        Long[] orgGroupIds = filter.getSelOrgGroupIds();
	        Long[] orgIds = filter.getOrgIds();
	        
	        int transactionType = filter.getTransactionType();
	        TeamMember teamMember = filter.getTeamMember();
	        // apply calendar filter
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
	        
	        Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
	        Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
	        Long[] sectorIds = filter.getSelSectorIds();
	        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
	        Boolean isfilteredworkspace =   TeamUtil.getAmpTeam(teamMember.getTeamId()).getComputation()!=null  && TeamUtil.getAmpTeam(teamMember.getTeamId()).getComputation() && TeamUtil.getAmpTeam(teamMember.getTeamId()).getFilterDataSet()!=null
			&& TeamUtil.getAmpTeam(teamMember.getTeamId()).getFilterDataSet().size()>0;
	        /*
	         * We are selecting regions which are funded
	         * In selected year by the selected organization
	         *
	         */
	        try {
	            String oql = "select distinct loc  from ";
	            oql += AmpFundingDetail.class.getName()
	                    + " as fd inner join fd.ampFundingId f ";
	            oql += " inner join f.ampActivityId act ";
	            
	            if(filter.getFromPublicView() !=null&& filter.getFromPublicView())
	            	oql += " inner join act.ampActivityGroupCached actGroup ";
	            else
	            	oql += " inner join act.ampActivityGroup actGroup ";
	            
	            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
	            oql += " inner join loc.parentCategoryValue parcv ";
	            oql += " inner join act.orgrole role  ";
	            
	            if (sectorCondition) {
	                oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
	            }
	            
	            oql += "  where fd.adjustmentType ="+CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL).getId();
	            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                oql += " and fd.transactionType =:transactionType  ";
	            } else {
	                oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
	            }
	            if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
	                if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
	                    oql += QueryUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
	                }
	            } else {
	                oql += QueryUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
	            }
	            oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
	            
	            if(filter.getFromPublicView() != null && filter.getFromPublicView() == true){
	                oql += QueryUtil.getTeamQueryManagement();
	            }else{
	            	if (!isfilteredworkspace){
						oql += QueryUtil.getTeamQuery(teamMember);
					}
	            }
	            if (sectorCondition) {
	                oql += " and sec.id in ("+QueryUtil.getInStatement(sectorIds)+") ";
	            }
	
	            if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
					oql += ActivityUtil.getApprovedActivityQueryString("act");
				}
	            
	            if (ActivityVersionUtil.isVersioningEnabled()){
	    			oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";	
	    			oql += " and (act.deleted = false or act.deleted is null)";
	    		}
	            
	            if ("zone".equalsIgnoreCase(implementationLevel)){
	            	oql += "  and (parcv.value =:implementationLevel or parcv.value =:district or parcv.value =:communal)";
	            }else{
	            	oql += "  and (parcv.value =:implementationLevel or parcv.value =:zone or parcv.value =:district or parcv.value =:communal)";
	            }
	            oql+=" order by loc.parentCategoryValue";
	            Session session = PersistenceManager.getRequestDBSession();
	            Query query = session.createQuery(oql);
	            query.setDate("startDate", startDate);
	            query.setDate("endDate", endDate);
	            
	            if ("zone".equalsIgnoreCase(implementationLevel)){
	            	query.setString("implementationLevel", implementationLevel);
	            	query.setString("district", "District");
	            	query.setString("communal", "Communal section");
	            }else{
	            	query.setString("implementationLevel", implementationLevel);
	            	query.setString("zone", "Zone");
	            	query.setString("district", "District");
	            	query.setString("communal", "Communal section");
	            }
	           
	            if (filter.isModeexport()){
					String inactivities= "";
					ArrayList<BigInteger> filteractivities = getInActivities(filter.getReportfilterquery());
					for (Iterator iterator = filteractivities.iterator(); iterator.hasNext();) {
						BigInteger id = (BigInteger) iterator.next();
						if (inactivities ==""){
							inactivities += id.toString();
						}else{
							inactivities +="," + id.toString();
						}
					}
					oql += " and act.ampActivityId in("+ inactivities +")";
				}
	            
	            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                query.setLong("transactionType", transactionType);
	            }
	            
	            if (isfilteredworkspace){
					String inactivities= "";
					AmpARFilter teamFilter = (AmpARFilter) request.getSession().getAttribute(ArConstants.TEAM_FILTER);
					ArrayList<BigInteger> filteractivities = getInActivities(teamFilter.getGeneratedFilterQuery());
					for (Iterator iterator = filteractivities.iterator(); iterator.hasNext();) {
						BigInteger id = (BigInteger) iterator.next();
						if (inactivities ==""){
							inactivities += id.toString();
						}else{
							inactivities +="," + id.toString();
						}
					}
					oql += " and act.ampActivityId in("+ inactivities +")";
				}
	            locations = query.list();
	        }
	        catch (Exception e) {
	            logger.error(e);
	            throw new DgException("Cannot load regions from db", e);
	        }
	        return locations;
		}
	}

	public static List<AmpCategoryValueLocations> getZones(MapFilter filter)
			throws DgException {
		List<AmpCategoryValueLocations> locations = new ArrayList<AmpCategoryValueLocations>();
		if (filter.getSelLocationIds() != null
				&& filter.getSelLocationIds().length > 0
				&& filter.getSelLocationIds()[0] != -1) {
			if (filter.getSelLocationIds().length == 1) {
				AmpCategoryValueLocations loc = LocationUtil
						.getAmpCategoryValueLocationById(filter
								.getSelLocationIds()[0]);
				locations.addAll(loc.getChildLocations());
				return locations;
			} else {
				for (int i = 0; i < filter.getSelLocationIds().length; i++) {
					AmpCategoryValueLocations loc = LocationUtil
							.getAmpCategoryValueLocationById(filter
									.getSelLocationIds()[i]);
					locations.add(loc);
				}
				return locations;
			}
		} else {

			Long[] orgGroupIds = filter.getSelOrgGroupIds();
			Long[] orgIds = filter.getOrgIds();

			int transactionType = filter.getTransactionType();
			TeamMember teamMember = filter.getTeamMember();
			// apply calendar filter
			Long fiscalCalendarId = filter.getFiscalCalendarId();

			Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter
					.getStartYear().intValue());
			Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter
					.getEndYear().intValue());
			Long[] sectorIds = filter.getSelSectorIds();
			boolean sectorCondition = sectorIds != null && sectorIds.length > 0
					&& !sectorIds[0].equals(-1l);
			/*
			 * We are selecting regions which are funded In selected year by the
			 * selected organization
			 */
			try {
				String oql = "select distinct loc  from ";
				oql += AmpFundingDetail.class.getName()
						+ " as fd inner join fd.ampFundingId f ";
				oql += "   inner join f.ampActivityId act ";
				oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
				oql += " inner join loc.parentCategoryValue parcv ";
				if (sectorCondition) {
					oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
				}
				oql += "  where fd.adjustmentType ="
						+ CategoryManagerUtil.getAmpCategoryValueFromDB(
								CategoryConstants.ADJUSTMENT_TYPE_ACTUAL)
								.getId();
				if (filter.getTransactionType() < 2) { // the option comm&disb
														// is not selected
					oql += " and fd.transactionType =:transactionType  ";
				} else {
					oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the
																						// option
																						// comm&disb
																						// is
																						// selected
				}
				if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
					if (orgGroupIds != null && orgGroupIds.length > 0
							&& orgGroupIds[0] != -1) {
						oql += QueryUtil.getOrganizationQuery(true, orgIds,
								orgGroupIds);
					}
				} else {
					oql += QueryUtil.getOrganizationQuery(false, orgIds,
							orgGroupIds);
				}
				oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";

				if (filter.getFromPublicView() != null
						&& filter.getFromPublicView() == true) {
					oql += QueryUtil.getTeamQueryManagement();
				} else {
					oql += QueryUtil.getTeamQuery(teamMember);
				}
				if (sectorCondition) {
					oql += " and sec.id in ("
							+ QueryUtil.getInStatement(sectorIds) + ") ";
				}

				if (filter.getShowOnlyApprovedActivities() != null
						&& filter.getShowOnlyApprovedActivities()) {
					oql += ActivityUtil.getApprovedActivityQueryString("act");
				}
				oql += "  and parcv.value = 'Zone'";// get only regions

				oql += " order by loc.parentCategoryValue";
				Session session = PersistenceManager.getRequestDBSession();
				Query query = session.createQuery(oql);
				query.setDate("startDate", startDate);
				query.setDate("endDate", endDate);
				// if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1)
				// && orgGroupId != -1) {
				// query.setLong("orgGroupId", orgGroupId);
				// }
				if (filter.getTransactionType() < 2) { // the option comm&disb
														// is not selected
					query.setLong("transactionType", transactionType);
				}
				locations = query.list();
			} catch (Exception e) {
				logger.error(e);
				throw new DgException("Cannot load zones from db", e);
			}
			return locations;
		}
	}

    @SuppressWarnings("unchecked")
    public static ArrayList<DecimalWraper> getFunding(MapFilter filter, Date startDate,Date endDate,HttpServletRequest request,Long assistanceTypeId,Long financingInstrumentId,
            Long adjustmentType) throws Exception {
    	ArrayList<DecimalWraper> totals = new ArrayList<DecimalWraper>();
        String oql = "";
        String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} 
        
        Long[] orgIds = filter.getOrgIds();
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        
        TeamMember tm = filter.getTeamMember();
        Long[] locationIds = filter.getSelLocationIds();
        Long[] sectorIds = filter.getSelSectorIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
        Boolean isfilteredworkspace =   TeamUtil.getAmpTeam(tm.getTeamId()).getComputation()!=null  && TeamUtil.getAmpTeam(tm.getTeamId()).getComputation() && TeamUtil.getAmpTeam(tm.getTeamId()).getFilterDataSet()!=null
		&& TeamUtil.getAmpTeam(tm.getTeamId()).getFilterDataSet().size()>0;
		
        if (locationCondition && sectorCondition) {
        	oql = " select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actloc.locationPercentage,actsec.sectorPercentage,fd.fixedExchangeRate) ";
        } else if (locationCondition)  {
        	oql = " select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actloc.locationPercentage,fd.fixedExchangeRate) ";
        } else if (sectorCondition)  {
        	oql = " select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actsec.sectorPercentage,fd.fixedExchangeRate) ";
        } else {
            oql = "select fd ";
        }
        oql += " from ";
        oql += AmpFundingDetail.class.getName()  + " as fd inner join fd.ampFundingId f ";
        oql += " inner join f.ampActivityId act ";
        oql += " inner join act.orgrole role  ";
		
        if(filter.getFromPublicView() !=null&& filter.getFromPublicView())
        	oql += " inner join act.ampActivityGroupCached actGroup ";
        else
        	oql += " inner join act.ampActivityGroup actGroup ";
        
        if (locationCondition) {
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }

        if (sectorCondition) {
            oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
        }
        
        if (filter.getFromPublicView() != null && filter.getFromPublicView())
			oql += " inner join act.ampActivityGroupCached actGroup ";
		else
			oql += " inner join act.ampActivityGroup actGroup ";

		if (locationCondition) {
			oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
		}

		if (sectorCondition) {
			oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
		}

		oql += " where fd.adjustmentType =:adjustmentType ";
		if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
			if (orgGroupIds != null && orgGroupIds.length > 0
					&& orgGroupIds[0] != -1) {
				oql += QueryUtil
						.getOrganizationQuery(true, orgIds, orgGroupIds);
			}
		} else {
			oql += QueryUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
		}
		if (locationCondition) {
			oql += " and loc.id in (" + QueryUtil.getInStatement(locationIds)+ ") ";
		}

		if (sectorCondition) {
			oql += " and sec.id in (" + QueryUtil.getInStatement(sectorIds)+ ") ";
		}

		if (filter.getActivityId() != null) {
			oql += " and act.ampActivityId =:activityId ";
		}

		oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
		if (assistanceTypeId != null) {
			oql += "  and f.typeOfAssistance=:assistanceTypeId ";
		}
		if (financingInstrumentId != null) {
			oql += "   and f.financingInstrument=:financingInstrumentId  ";
		}

		if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
			oql += ActivityUtil.getApprovedActivityQueryString("act");
		}
        if(filter.getFromPublicView() != filter.getFromPublicView()){
            oql += QueryUtil.getTeamQueryManagement();
        }else{
        	if(!isfilteredworkspace){
        		oql += QueryUtil.getTeamQuery(tm);
        	}
        }
        
        if (ActivityVersionUtil.isVersioningEnabled()){
			oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";	
			oql += " and (act.deleted = false or act.deleted is null)";
		}
        
        if (isfilteredworkspace){
			String inactivities= "";
			AmpARFilter teamFilter = (AmpARFilter) request.getSession().getAttribute(ArConstants.TEAM_FILTER);
			ArrayList<BigInteger> filteractivities = getInActivities(teamFilter.getGeneratedFilterQuery());
			for (Iterator iterator = filteractivities.iterator(); iterator.hasNext();) {
				BigInteger id = (BigInteger) iterator.next();
				if (inactivities ==""){
					inactivities += id.toString();
				}else{
					inactivities +="," + id.toString();
				}
			}
			oql += " and act.ampActivityId in("+ inactivities +")";
		}
        
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpFundingDetail> fundingDets = null;
        Query query = session.createQuery(oql);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        //if ((orgIds == null || orgIds.length == 0 || orgIds[0] == -1) && orgGroupId != -1) {
        //    query.setLong("orgGroupId", orgGroupId);
        //}
        if (assistanceTypeId != null) {
            query.setLong("assistanceTypeId", assistanceTypeId);
        }
        if (financingInstrumentId != null) {
            query.setLong("financingInstrumentId", financingInstrumentId);
        }
        query.setLong("adjustmentType",adjustmentType);

        if (filter.getActivityId()!=null) {
            query.setLong("activityId", filter.getActivityId());
        }

        fundingDets = query.list();

        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        cal.doCalculations(fundingDets, currCode);

        if (CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ADJUSTMENT_TYPE_PLANNED).getId().compareTo(adjustmentType)==0) {
            totals.add(cal.getTotPlannedExp());
        } else {
            totals.add(cal.getTotActualExp());
        }
        if (CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL).getId().compareTo(adjustmentType)==0) {
            totals.add(cal.getTotActualDisb());
        } else {
            totals.add(cal.getTotPlanDisb());
        }
        if (CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL).getId().compareTo(adjustmentType)==0) {
            totals.add(cal.getTotActualComm());
        } else {
            totals.add(cal.getTotPlannedComm());
        }

        return totals;
	}

	public static List<AmpOrganisation> getDonorOrganisationByGroupId(
			Long orgGroupId, boolean publicView) {
		Session session = null;
		Query q = null;
		List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
		StringBuilder queryString = new StringBuilder(
				"select distinct org from "
						+ AmpOrgRole.class.getName()
						+ " orgRole inner join orgRole.role role inner join orgRole.organisation org ");
		if (publicView) {
			queryString
					.append(" inner join orgRole.activity act  inner join act.team tm ");
		}
		queryString.append(" where  role.roleCode='DN' ");
		if (orgGroupId != null && orgGroupId != -1) {
			queryString.append(" and org.orgGrpId=:orgGroupId ");
		}
		if (publicView) {
			queryString
					.append(" and act.draft=false and act.approvalStatus ='approved' and tm.parentTeamId is not null ");
		}

		queryString.append("order by org.name asc");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			if (orgGroupId != null && orgGroupId != -1) {
				q.setLong("orgGroupId", orgGroupId);
			}
			organizations = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Amp organization names from database ",
					ex);
		}
		return organizations;
	}

	public static List<AmpOrganisation> getDonorOrganisationByType(
			Long orgTypeId, boolean publicView) {
		Session session = null;
		Query q = null;
		List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
		StringBuilder queryString = new StringBuilder(
				"select distinct org from "
						+ AmpOrgRole.class.getName()
						+ " orgRole inner join orgRole.role role inner join orgRole.organisation org ");
		if (publicView) {
			queryString
					.append(" inner join orgRole.activity act  inner join act.team tm ");
		}
		queryString.append(" where  role.roleCode='DN' ");
		if (orgTypeId != null && orgTypeId != -1) {
			queryString.append(" and org.orgGrpId.orgType=:orgtypeId ");
		}
		if (publicView) {
			queryString
					.append(" and act.draft=false and act.approvalStatus ='approved' and tm.parentTeamId is not null ");
		}

		queryString.append("order by org.name asc");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			if (orgTypeId != null && orgTypeId != -1) {
				q.setLong("orgtypeId", orgTypeId);
			}
			organizations = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Amp organization names from database ",
					ex);
		}
		return organizations;
	}

	public static Collection<AmpStructureType> getAllStructureTypes() {
		Session session = null;
		Query q = null;
		List<AmpStructureType> sts = new ArrayList<AmpStructureType>();
		StringBuilder queryString = new StringBuilder(
				"select structure_type from "
						+ AmpStructureType.class.getName() + " structure_type ");
		queryString.append("order by structure_type.name asc");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			sts = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Amp Structure Types from database ", ex);
		}
		return sts;
	}

	public static AmpStructureType getStructureTypesByName(String name) {
		Session session = null;
		Query q = null;
		AmpStructureType stt = null;
		ArrayList result;
		StringBuilder queryString = new StringBuilder("select st from "
				+ AmpStructureType.class.getName() + " st ");
		queryString.append("where st.name=:name");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			q.setString("name", name.trim());
			result = (ArrayList) q.list();
			if (result.size() > 0) {
				stt = (AmpStructureType) result.get(0);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp Structure Type from database "
					+ name + " ", ex);
		}
		return stt;
	}

	public static Set<AmpActivity> getActivityByAmpId(String id) {
		Session session = null;
		Query q = null;
		Set<AmpActivity> activities = null;
		StringBuilder queryString = new StringBuilder("select a from "
				+ AmpActivity.class.getName() + " a ");
		queryString.append("where a.ampId=:id");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			q.setString("id", id);
			activities = new HashSet<AmpActivity>(q.list());

		} catch (Exception ex) {
			logger.error("Unable to get Amp Structure Type from database ", ex);
		}
		return activities;
	}

	public static AmpActivityVersion getActivityById(Long id) {
		Session session = null;
		AmpActivityVersion activity = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			activity = (AmpActivityVersion) session.load(
					AmpActivityVersion.class, id);

		} catch (Exception ex) {
			logger.error("Unable to get Amp Structure Type from database ", ex);
		}
		return activity;
	}

	public static List<AmpMapConfig> getMaps() {
		Session session = null;
		Query q = null;
		List<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
		StringBuilder queryString = new StringBuilder("select a from "
				+ AmpMapConfig.class.getName() + " a");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			maps = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get maps from database ", ex);
		}
		return maps;
	}

	public static AmpStructureType getStructureType(Long structureTypeId) {
		Session session = null;
		AmpStructureType ampStructureType = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			ampStructureType = (AmpStructureType) session.load(
					AmpStructureType.class, structureTypeId);
		} catch (Exception e) {
			logger.error("Unable to get object of class "
					+ AmpStructureType.class.getName() + " width id="
					+ structureTypeId + ". Error was:" + e);
		}
		return ampStructureType;
	}
	public static void deleteStructureType(AmpStructureType structureType) throws AdminException {
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
            Query q = sess.createQuery("select st from " + AmpStructure.class.getName() + " st where st.type.typeId=:typeId "  );
            q.setLong("typeId", structureType.getTypeId());
            if (!q.list().isEmpty()){
            	throw new AdminException("The Structure Type is being referenced, it can not be deleted.");	
            }	
			
//beginTransaction();
			sess.delete(structureType);
			//tx.commit();
		}
		catch (Exception e) {
			if (e instanceof AdminException){
				throw (AdminException)e;
			}
			if (e instanceof JDBCException)
				throw (JDBCException) e;
			logger.error("Exception " + e.toString());
			try {
				tx.rollback();
			} catch (HibernateException ex) {
				logger.error("rollback() failed");
				logger.error(ex.toString());
			}
		}

	}

	public static void saveStructure(AmpStructure structure) {
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			session.save(structure);

		} catch (Exception e) {
			logger.error("Unable to save structure type", e);
		}

	}

	public static void saveStructureType(AmpStructureType structureType) {
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			session.save(structureType);

		} catch (Exception e) {
			logger.error("Unable to save structure type", e);
		}

	}

	public static void saveMapConfig(AmpMapConfig map) {
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			session.save(map);
		} catch (Exception e) {
			logger.error("Unable to save structure type", e);
		}

	}

	private static Long[] getAllDescendants(Long[] sectorIds,
			ArrayList<AmpSector> allSectorList) {
		// Go through the list to determine the children
		List<Long> tempSectorIds = new ArrayList<Long>();
		for (AmpSector as : allSectorList) {
			for (Long i : sectorIds) {
				if (!tempSectorIds.contains(i))
					tempSectorIds.add(i);
				if (as.getParentSectorId() != null
						&& as.getParentSectorId().getAmpSectorId().equals(i)) {
					tempSectorIds.add(as.getAmpSectorId());
				} else if (as.getParentSectorId() != null
						&& as.getParentSectorId().getParentSectorId() != null
						&& as.getParentSectorId().getParentSectorId()
								.getAmpSectorId().equals(i)) {
					tempSectorIds.add(as.getAmpSectorId());
				}
			}
		}
		return (Long[]) tempSectorIds.toArray(new Long[0]);
	}

	public static Long[] getAllDescendantsLocation(Long[] locationIds,
			ArrayList<AmpCategoryValueLocations> allLocationsList) {
		List<Long> tempLocationsIds = new ArrayList<Long>();
		for (AmpCategoryValueLocations as : allLocationsList) {
			for (Long i : locationIds) {
				if (!tempLocationsIds.contains(i))
					tempLocationsIds.add(i);
				if (as.getParentLocation() != null
						&& as.getParentLocation().getId().equals(i)) {
					tempLocationsIds.add(as.getId());
				} else if (as.getParentLocation() != null
						&& as.getParentLocation().getParentLocation() != null
						&& as.getParentLocation().getParentLocation().getId()
								.equals(i)) {
					tempLocationsIds.add(as.getId());
				} else if (as.getParentLocation() != null
						&& as.getParentLocation().getParentLocation() != null
						&& as.getParentLocation().getParentLocation()
								.getParentLocation() != null
						&& as.getParentLocation().getParentLocation()
								.getParentLocation().getId().equals(i)) {
					tempLocationsIds.add(as.getId());
				}
			}
		}
		return (Long[]) tempLocationsIds.toArray(new Long[0]);
	}

	public static AmpMapConfig getMapByType(Integer mapType) {
		Session session = null;
		Query q = null;
		AmpMapConfig map = new AmpMapConfig();
		StringBuilder queryString = new StringBuilder("select a from "
				+ AmpMapConfig.class.getName() + " a where mapType = :mapType ");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			q.setInteger("mapType", mapType);
			List<AmpMapConfig> maps = q.list();
			if (maps.size() > 0)
				map = maps.get(0);
			else
				map = null;
		} catch (Exception ex) {
			logger.error("Unable to get individual map from database, of type: " + mapType, ex);
		}
		return map;
	}

	public static List<AmpMapConfig> getMapsBySubType(Integer mapSubType) {
		Session session = null;
		Query q = null;
		
		List<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
		StringBuilder queryString = new StringBuilder("select a from "
				+ AmpMapConfig.class.getName() + " a where mapSubType = :mapSubType ");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			q.setInteger("mapSubType", mapSubType);
			maps = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get individual map from database, of type: " + mapSubType, ex);
		}
		return maps;		
	}

}
