package org.digijava.module.esrigis.helpers;

/**
 * @author Diego Dimunzio
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.visualization.util.DashboardUtil; //TODO: Check this functions and use a common
import org.hibernate.Query;
import org.hibernate.Session;

public class DbHelper {
	private static Logger logger = Logger.getLogger(DbHelper.class);

	public static List<AmpActivityVersion> getActivities(MapFilter filter)throws DgException {
		Long[] orgGroupIds = filter.getSelOrgGroupIds();
		List<AmpActivityVersion> activities = null;
		Long[] orgIds = filter.getOrgIds();

		int transactionType = filter.getTransactionType();
		TeamMember teamMember = filter.getTeamMember();
		// apply calendar filter
		Long fiscalCalendarId = filter.getFiscalCalendarId();

		Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue() - filter.getYearsInRange());
		Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
		Long[] locationIds = filter.getSelLocationIds();
		boolean locationCondition = locationIds != null
				&& locationIds.length > 0 && !locationIds[0].equals(-1l);
		Long[] sectorIds = filter.getSelSectorIds();
		boolean sectorCondition = sectorIds != null && sectorIds.length > 0
				&& !sectorIds[0].equals(-1l);
		/*
		 * We are selecting sectors which are funded In selected year by the
		 * selected organization
		 */
		try {
			String oql = "select distinct act from ";
			oql += AmpFundingDetail.class.getName()
					+ " as fd inner join fd.ampFundingId f ";
			oql += " inner join f.ampActivityId act ";
			oql += " inner join act.ampActivityGroup actGroup ";
			oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
			if (locationCondition) {
				oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
			}
			if (sectorCondition) {
				oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
			}
			oql += "  where fd.adjustmentType = 1";
			if (filter.getTransactionType() < 2) {
				oql += " and fd.transactionType =:transactionType  ";
			} else {
				oql += " and (fd.transactionType =0 or  fd.transactionType =1) ";
			}
			if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
				if (orgGroupIds != null && orgGroupIds.length > 0
						&& orgGroupIds[0] != -1) {
					oql += QueryUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
				}
			} else {
				oql += QueryUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
			}
			oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";

			if (filter.getFromPublicView() != null
					&& filter.getFromPublicView() == true) {
				oql += QueryUtil.getTeamQueryManagement();
			} else {
				oql += QueryUtil.getTeamQuery(teamMember);
			}
			if (locationCondition) {
				oql += " and loc.id in (" + QueryUtil.getInStatement(locationIds) + ") ";
			}
			if (sectorCondition) {
				oql += " and sec.id in (" + QueryUtil.getInStatement(sectorIds) + ") ";
			}

			if (filter.getShowOnlyApprovedActivities() != null
					&& filter.getShowOnlyApprovedActivities()) {
				oql += ActivityUtil.getApprovedActivityQueryString("act");
			}
			
			//Additional clause to get the last version
			oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";
			

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

    public static List<AmpCategoryValueLocations> getLocations(MapFilter filter, String implementationLevel) throws DgException {
    	List<AmpCategoryValueLocations> locations = new ArrayList<AmpCategoryValueLocations>();
    	if (filter.getSelLocationIds()!=null && filter.getSelLocationIds().length > 0 && filter.getSelLocationIds()[0] != -1) {
			if (filter.getSelLocationIds().length == 1) {
				AmpCategoryValueLocations loc = LocationUtil.getAmpCategoryValueLocationById(filter.getSelLocationIds()[0]);
				locations.addAll(loc.getChildLocations());
				return locations;
			} else {
				for (int i = 0; i < filter.getSelLocationIds().length; i++) {
					AmpCategoryValueLocations loc = LocationUtil.getAmpCategoryValueLocationById(filter.getSelLocationIds()[i]);
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
	        
	        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
	        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
	        Long[] sectorIds = filter.getSelSectorIds();
	        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
	        /*
	         * We are selecting regions which are funded
	         * In selected year by the selected organization
	         *
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
	            oql += "  where fd.adjustmentType = 1";
	            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                oql += " and fd.transactionType =:transactionType  ";
	            } else {
	                oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
	            }
	            if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
	                if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
	                    oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
	                }
	            } else {
	                oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
	            }
	            oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
	            
	            if(filter.getFromPublicView() != null && filter.getFromPublicView() == true){
	                oql += DashboardUtil.getTeamQueryManagement();
	            }
	            else
	            {
	                oql += DashboardUtil.getTeamQuery(teamMember);
	            }
	            if (sectorCondition) {
	                oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
	            }
	
	            if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
					oql += ActivityUtil.getApprovedActivityQueryString("act");
				}
	            oql += "  and parcv.value = :implementationLevel";
	            
	            oql+=" order by loc.parentCategoryValue";
	            Session session = PersistenceManager.getRequestDBSession();
	            Query query = session.createQuery(oql);
	            query.setDate("startDate", startDate);
	            query.setDate("endDate", endDate);
	            query.setDate("endDate", endDate);
	            query.setString("implementationLevel", implementationLevel);
	            //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
	            //    query.setLong("orgGroupId", orgGroupId);
	            //}
	            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                query.setLong("transactionType", transactionType);
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

    public static List<AmpCategoryValueLocations> getZones(MapFilter filter) throws DgException {
    	List<AmpCategoryValueLocations> locations = new ArrayList<AmpCategoryValueLocations>();
    	if (filter.getSelLocationIds()!=null && filter.getSelLocationIds().length > 0 && filter.getSelLocationIds()[0] != -1) {
			if (filter.getSelLocationIds().length == 1) {
				AmpCategoryValueLocations loc = LocationUtil.getAmpCategoryValueLocationById(filter.getSelLocationIds()[0]);
				locations.addAll(loc.getChildLocations());
				return locations;
			} else {
				for (int i = 0; i < filter.getSelLocationIds().length; i++) {
					AmpCategoryValueLocations loc = LocationUtil.getAmpCategoryValueLocationById(filter.getSelLocationIds()[i]);
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
	        
	        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
	        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
	        Long[] sectorIds = filter.getSelSectorIds();
	        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
	        /*
	         * We are selecting regions which are funded
	         * In selected year by the selected organization
	         *
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
	            oql += "  where fd.adjustmentType = 1";
	            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                oql += " and fd.transactionType =:transactionType  ";
	            } else {
	                oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
	            }
	            if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
	                if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
	                    oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
	                }
	            } else {
	                oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
	            }
	            oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
	            
	            if(filter.getFromPublicView() != null && filter.getFromPublicView() == true){
	                oql += DashboardUtil.getTeamQueryManagement();
	            }
	            else
	            {
	                oql += DashboardUtil.getTeamQuery(teamMember);
	            }
	            if (sectorCondition) {
	                oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
	            }
	
	            if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
					oql += ActivityUtil.getApprovedActivityQueryString("act");
				}
	            oql += "  and parcv.value = 'Zone'";// get only regions
	            
	            oql+=" order by loc.parentCategoryValue";
	            Session session = PersistenceManager.getRequestDBSession();
	            Query query = session.createQuery(oql);
	            query.setDate("startDate", startDate);
	            query.setDate("endDate", endDate);
	            //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
	            //    query.setLong("orgGroupId", orgGroupId);
	            //}
	            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                query.setLong("transactionType", transactionType);
	            }
	            locations = query.list();
	        }
	        catch (Exception e) {
	            logger.error(e);
	            throw new DgException("Cannot load zones from db", e);
	        }
	        return locations;
		}
     }

    @SuppressWarnings("unchecked")
    //TODO: Remove this and make a common function for Visualization/GIS
    public static DecimalWraper getFunding(MapFilter filter, Date startDate,
            Date endDate, Long assistanceTypeId,
            Long financingInstrumentId,
            int transactionType,int adjustmentType) throws DgException {
        DecimalWraper total = null;
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
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";
        if (locationCondition) {
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }

        if (sectorCondition) {
            oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
        }

        oql += " where  fd.transactionType =:transactionType  and  fd.adjustmentType =:adjustmentType ";
        if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
                oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
            }
        } else {
            oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
        }
        if (locationCondition) {
            oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
        }

        if (sectorCondition) {
            oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
        }

        if (filter.getActivityId()!=null) {
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
            oql += DashboardUtil.getTeamQueryManagement();
        }
        else
        {
            oql += DashboardUtil.getTeamQuery(tm);
        }

        Session session = PersistenceManager.getRequestDBSession();
        List<AmpFundingDetail> fundingDets = null;
        try {
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
            query.setLong("transactionType", transactionType);
            query.setLong("adjustmentType",adjustmentType);
            
            if (filter.getActivityId()!=null) {
                query.setLong("activityId", filter.getActivityId());
            }
            fundingDets = query.list();
            /*the objects returned by query  and   selected currency
            are passed doCalculations  method*/
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
            cal.doCalculations(fundingDets, currCode);
            /*Depending on what is selected in the filter
            we should return either actual commitments
            or actual Disbursement or  */
            switch (transactionType) {
                case Constants.EXPENDITURE:
                    if (Constants.PLANNED == adjustmentType) {
                        total = cal.getTotPlannedExp();
                    } else {
                        total = cal.getTotActualExp();
                    }
                    break;
                case Constants.DISBURSEMENT:
                    if (Constants.ACTUAL == adjustmentType) {
                        total = cal.getTotActualDisb();
                    } else {
                        total = cal.getTotPlanDisb();
                    }
                    break;
                default:
                    if (Constants.ACTUAL == adjustmentType) {
                        total = cal.getTotActualComm();
                    } else {
                        total = cal.getTotPlannedComm();
                    }
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }


        return total;
	}

}
