package org.digijava.module.visualization.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

public class DbUtil {
	private static Logger logger = Logger.getLogger(DbUtil.class);
	
	public static List<AmpOrganisation> getDonorOrganisationByGroupId(
			Long orgGroupId, boolean publicView) {
        Session session = null;
        Query q = null;
        List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
        StringBuilder queryString = new StringBuilder("select distinct org from " + AmpOrgRole.class.getName() + " orgRole inner join orgRole.role role inner join orgRole.organisation org ");
        if (publicView) {
            queryString.append(" inner join orgRole.activity act  inner join act.team tm ");
        }
        queryString.append(" where  role.roleCode='DN' ");
         if (orgGroupId != null&&orgGroupId !=-1) {
            queryString.append(" and org.orgGrpId=:orgGroupId ");
        }
        if (publicView) {
            queryString.append(" and act.draft=false and act.approvalStatus ='approved' and tm.parentTeamId is not null ");
        }

        queryString.append("order by org.name asc");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());
            if (orgGroupId != null&&orgGroupId !=-1) {
                q.setLong("orgGroupId", orgGroupId);
            }
            organizations = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get Amp organization names from database ", ex);
        }
        return organizations;
    }
	
	public static Collection<AmpOrganisation> getAllOrganizations() {
		Session session = null;
		Collection<AmpOrganisation> col = new ArrayList<AmpOrganisation>();

		try {
			session = PersistenceManager.getSession();
			
			String queryString = " SELECT aorg FROM " + AmpOrganisation.class.getName() + " aorg ";
			
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get Organizations, " + ex);
		} 
		return col;
	}
	
	public static Collection<AmpSector> getAllSectors(){
		Session session = null;
        Query qry = null;
        Collection<AmpSector> col = new ArrayList<AmpSector>();
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "SELECT DISTINCT s.* FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
					+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id AND s.parent_sector_id is null "
					+ "AND c.name='Primary'";
             qry = session.createSQLQuery(queryString).addEntity(AmpSector.class);
             col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting sectors : " + ex);
		} 
        return col;
	}
	
	public static List<AmpSector> getSubSectors(Long id){
		Session session = null;
        Query qry = null;
        List<AmpSector> col = new ArrayList<AmpSector>();
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "SELECT s.* FROM amp_sector s "
					+ "WHERE s.parent_sector_id =:id " ;
             qry = session.createSQLQuery(queryString).addEntity(AmpSector.class);
             qry.setParameter("id", id, Hibernate.LONG);
             col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting sub-sectors : " + ex);
		} 
        return col;
	}
	
	/**
     * Returns pledge amount in selected currency
     * for selected organization and year
     * @param orgID
     * @param year
     * @param currCode
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    @SuppressWarnings("unchecked")
    public static DecimalWraper getPledgesFunding(Long[] orgIds, Long[] orgGroupIds,
            Date startDate, Date endDate,
            String currCode) throws DgException {
    	DecimalWraper totalPlannedPldges = new DecimalWraper();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Integer startYear = Integer.valueOf(sdf.format(startDate));
        Integer endYear = Integer.valueOf(sdf.format(endDate));
        String years = "";
        for (int i = startYear; i <= endYear; i++) {
			if(!years.equals(""))
				years = years + ", ";
			years = years + "'" + i + "'";
		}
        
        String oql = "select fd ";
        oql += " from ";
        oql += FundingPledgesDetails.class.getName()
                + " fd inner join fd.pledgeid plg ";
        oql += " inner join  plg.organization org  ";
        oql += " where  fd.fundingYear in (" + years + ")";
        if (orgIds == null || orgIds.length==0 || orgIds[0] == -1) {
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
                oql += " and  org.orgGrpId.ampOrgGrpId in (" + DashboardUtil.getInStatement(orgGroupIds) + ") ";
            }
        } else {
            oql += " and org.ampOrgId in (" + DashboardUtil.getInStatement(orgIds) + ") ";
        }
        Session session = PersistenceManager.getRequestDBSession();
        List<FundingPledgesDetails> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            //query.setDate("startDate", startDate);
            //query.setDate("endDate", endDate);
            //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
            //    query.setLong("orgGroupId", orgGroupId);
            //}
            fundingDets = query.list();
            Iterator<FundingPledgesDetails> fundDetIter = fundingDets.iterator();
            while (fundDetIter.hasNext()) {
                FundingPledgesDetails pledge = fundDetIter.next();
                //converting amounts
                java.sql.Date dt = new java.sql.Date(pledge.getFunding_date().getTime());
                double frmExRt = Util.getExchange(pledge.getCurrency().getCurrencyCode(), dt);
                double toExRt = Util.getExchange(currCode, dt);
                DecimalWraper amt = CurrencyWorker.convertWrapper(pledge.getAmount(), frmExRt, toExRt, dt);
                totalPlannedPldges.setValue(totalPlannedPldges.getValue().add(amt.getValue()));
            }



        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return totalPlannedPldges;
    }
    @SuppressWarnings("unchecked")
    public static List<AmpFundingDetail> getUnallocatedFunding(DashboardFilter filter)
            throws DgException {
        Long[] orgIds = filter.getOrgIds();
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        int transactionType = filter.getTransactionType();
        TeamMember tm = filter.getTeamMember();
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
        Session session = PersistenceManager.getRequestDBSession();
        String oql = "select fd ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";
        oql += " left join act.locations actloc  ";

        oql += "  where  "
                + "   fd.adjustmentType = 1 ";
        oql += " inner join act.ampActivityGroup actGroup ";
        if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            oql += " and fd.transactionType =:transactionType  ";
        } else {
            oql += " and (fd.transactionType=1 or fd.transactionType=0) "; // the option comm&disb is selected
        }
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<:endDate)   ";
        if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
                oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
            }
        } else {
            oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
        }
     
        if(filter.getFromPublicView() != null && filter.getFromPublicView() == true){
            oql += DashboardUtil.getTeamQueryManagement();
        }
        else
        {
            oql += DashboardUtil.getTeamQuery(tm);
        }
        
        oql += " and actloc is NULL ";

        if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
			oql += ActivityUtil.getApprovedActivityQueryString("act");
		}
        
        oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

        Query query = session.createQuery(oql);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
        //    query.setLong("orgGroupId", orgGroupId);
        //}
        if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            query.setLong("transactionType", transactionType);
        }
        List<AmpFundingDetail> fundingDets = query.list();
        return fundingDets;

    }
    
    
    public static List<AmpCategoryValueLocations> getRegions(DashboardFilter filter) throws DgException {
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
	            oql += " inner join act.ampActivityGroup actGroup ";
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
	            oql += "  and parcv.value = 'Region'";// get only regions
	            oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

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
	            throw new DgException("Cannot load regions from db", e);
	        }
	        return locations;
		}
     }
    
    public static List<AmpSector> getSectors(DashboardFilter filter) throws DgException {
    	List<AmpSector> sectors = new ArrayList<AmpSector>();
        if (filter.getSelSectorIds()!=null && filter.getSelSectorIds().length > 0 && filter.getSelSectorIds()[0] != -1) {
			if (filter.getSelSectorIds().length == 1) {
				List<AmpSector> sector = getSubSectors(filter.getSelSectorIds()[0]);
				sectors.addAll(sector);
				return sectors;
			} else {
				for (int i = 0; i < filter.getSelSectorIds().length; i++) {
					AmpSector sector = SectorUtil.getAmpSector(filter.getSelSectorIds()[i]);
					sectors.add(sector);
				}
				return sectors;
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
	        Long[] locationIds = filter.getSelLocationIds();
	        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
	        /*
	         * We are selecting sectors which are funded
	         * In selected year by the selected organization
	         *
	         */
	        try {
	            String oql = "select distinct sec  from ";
	            oql += AmpFundingDetail.class.getName()
	                    + " as fd inner join fd.ampFundingId f ";
	            oql += "   inner join f.ampActivityId act ";
	            oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
	            oql += " inner join act.ampActivityGroup actGroup ";
	            if (locationCondition) {
	                oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
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
	            if (locationCondition) {
	                oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
	            }
	
	            if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
					oql += ActivityUtil.getApprovedActivityQueryString("act");
				}
	            oql += "  and sec.ampSecSchemeId in (select clscfg.classification.id from " 
	            	+ AmpClassificationConfiguration.class.getName() + " clscfg where clscfg.name = 'Primary') " 
	            	+ " and sec.parentSectorId is null";// get only primary sectors
	            
	            oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

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
	
	            sectors = query.list();
	        }
	        catch (Exception e) {
	            logger.error(e);
	            throw new DgException("Cannot load sectors from db", e);
	        }
	        return sectors;
    	}
     }
    
    public static List<AmpActivityVersion> getActivities(DashboardFilter filter) throws DgException {
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        List<AmpActivityVersion> activities = null;
        Long[] orgIds= filter.getOrgIds();
        
        int transactionType = filter.getTransactionType();
        TeamMember teamMember = filter.getTeamMember();
        // apply calendar filter
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        
        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
        Long[] locationIds = filter.getSelLocationIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        Long[] sectorIds = filter.getSelSectorIds();
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
        /*
         * We are selecting sectors which are funded
         * In selected year by the selected organization
         *
         */
        try {
            String oql = "select distinct act from ";
            oql += AmpFundingDetail.class.getName()
                    + " as fd inner join fd.ampFundingId f ";
            oql += "   inner join f.ampActivityId act ";
            oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
            oql += " inner join act.ampActivityGroup actGroup ";
            if (locationCondition) {
                oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
            }
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
            if (locationCondition) {
                oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
            }
            if (sectorCondition) {
                oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
            }

            if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
				oql += ActivityUtil.getApprovedActivityQueryString("act");
			}
            
            oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

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
            
            activities = query.list();
        }
        catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load activities from db", e);
        }
        return activities;

     }
    
    public static List<AmpOrganisation> getDonors(DashboardFilter filter) throws DgException {
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        List<AmpOrganisation> donors = new ArrayList<AmpOrganisation>();
        Long[] orgIds= filter.getOrgIds();
        if (orgGroupIds!=null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
			if (orgGroupIds.length == 1) {
				List<AmpOrganisation> donorsByGrp = org.digijava.module.aim.util.DbUtil.getOrganisationByGroupId(orgGroupIds[0]);
				donors.addAll(donorsByGrp);
				return donors;
			} else {
				for (int i = 0; i < filter.getSelLocationIds().length; i++) {
					List<AmpOrganisation> donorsByGrp = org.digijava.module.aim.util.DbUtil.getOrganisationByGroupId(orgGroupIds[i]);
					donors.addAll(donorsByGrp);
				}
				return donors;
			}
		} else {
	        int transactionType = filter.getTransactionType();
	        TeamMember teamMember = filter.getTeamMember();
	        // apply calendar filter
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
	        
	        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
	        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
	        Long[] locationIds = filter.getSelLocationIds();
	        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
	        Long[] sectorIds = filter.getSelSectorIds();
	        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
	        /*
	         * We are selecting sectors which are funded
	         * In selected year by the selected organization
	         *
	         */
	        try {
	            String oql = "select distinct donor from ";
	            oql += AmpFundingDetail.class.getName()
	                    + " as fd inner join fd.ampFundingId f ";
	            oql += "   inner join f.ampActivityId act ";
	            oql += "   inner join f.ampDonorOrgId donor ";
	            oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
	            oql += " inner join act.ampActivityGroup actGroup ";
	            if (locationCondition) {
	                oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
	            }
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
	            if (locationCondition) {
	                oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
	            }
	            if (sectorCondition) {
	                oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
	            }
	
	            if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
					oql += ActivityUtil.getApprovedActivityQueryString("act");
				}
	           
	            oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

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
	            
	            donors = query.list();
	        }
	        catch (Exception e) {
	            logger.error(e);
	            throw new DgException("Cannot load activities from db", e);
	        }
	        return donors;
		}
     }
    
    /**
     * Returns funding amount
     * @param orgID
     * @param year
     * @param assistanceTypeId
     * @param currCode
     * @param transactionType
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    @SuppressWarnings("unchecked")
    public static DecimalWraper getFunding(DashboardFilter filter, Date startDate,
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
        oql += " inner join act.ampActivityGroup actGroup ";
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

        oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

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
    public static AmpOrganisation getOrganisation(Long id) {
        Session session = null;
        AmpOrganisation org = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrganisation.class.getName() + " o "
                + "where (o.ampOrgId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                org = (AmpOrganisation) itr.next();
            }

        } catch (Exception ex) {
            logger.error("Unable to get organisation from database", ex);
        }
        logger.debug("Getting organisation successfully ");
        return org;
    }
    
    public static AmpOrgGroup getOrgGroup(Long id) {
        Session session = null;
        AmpOrgGroup orgGrp = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrgGroup.class.getName() + " o "
                + "where (o.ampOrgGrpId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                orgGrp = (AmpOrgGroup) itr.next();
            }

        } catch (Exception ex) {
            logger.error("Unable to get organisation group from database", ex);
        }
        logger.debug("Getting organisation successfully ");
        return orgGrp;
    }

}
