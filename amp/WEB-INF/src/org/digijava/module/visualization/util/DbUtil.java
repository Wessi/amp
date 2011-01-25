package org.digijava.module.visualization.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.visualization.form.VisualizationForm;
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
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			
			String queryString = " SELECT aorg FROM " + AmpOrganisation.class.getName() + " aorg ";
			
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get Organizations, " + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}

	public static Collection<AmpActivity> getAllActivities() {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			
			String queryString = " SELECT act FROM " + AmpActivityVersion.class.getName() + " act ";
			
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get Activities, " + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}

	public static Collection<AmpFundingDetail> getAllFundingsDetails() {
		Session session = null;
		Collection col = new ArrayList();
		try {
			session = PersistenceManager.getSession();
			//String queryString = "select * from AMP_FUNDING_DETAIL ";
			String queryString = " SELECT afd FROM " + AmpFundingDetail.class.getName() + " afd ";
			
			//Query qry = session.createSQLQuery(queryString).addEntity(AmpFundingDetail.class);
			Query qry = session.createQuery(queryString);
			col = qry.list();
			
		} catch (Exception ex) {
			logger.error("Unable to get Fundings Details, " + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}

	public static Collection<AmpFunding> getAllFundings() {
		Session session = null;
		Collection col = new ArrayList();
		try {
			session = PersistenceManager.getSession();
			
			String queryString = " SELECT af FROM " + AmpFunding.class.getName() + " af ";
			
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get Fundings, " + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}

	public static Collection<AmpCategoryValueLocations> getAllRegions() {
		Session session = null;
		Collection col = new ArrayList();
		try {
			session = PersistenceManager.getSession();
			
			String queryString = "SELECT * FROM amp_category_value_location acvl " +
						"INNER JOIN amp_category_value acv ON (acvl.parent_category_value = acv.id)" +
						"WHERE acv.category_value = 'Region'";
			Query qry = session.createSQLQuery(queryString).addEntity(AmpCategoryValueLocations.class);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get regions, " + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}

	public static int getProjectsAmount(){
		Session session = null;
        Query qry = null;
        int count=0;
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "select count(*) from " + AmpActivity.class.getName() + " act ";
             qry = session.createQuery(queryString);
             count= ((Integer) qry.iterate().next()).intValue();
		} catch (Exception ex) {
			logger.error("Exception while getting activities amount:" + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return count;
		
	}
	
	public static int getSectorsAmount(){
		Session session = null;
        Query qry = null;
        int count=0;
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "select count(*) from " + AmpSector.class.getName() + " sec where sec.parentSectorId is null";
             qry = session.createQuery(queryString);
             count=((Integer)qry.uniqueResult()).intValue();
		} catch (Exception ex) {
			logger.error("Exception while getting sectors amount:" + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return count;
	}
	
	public static Collection<AmpSector> getAllSectors(){
		Session session = null;
        Collection col = new ArrayList();
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "select sec from " + AmpSector.class.getName() + " sec where sec.parentSectorId is null";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting sectors :" + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}
	
	public static int getRegionsAmount(){
		Session session = null;
        Query qry = null;
        int count=0;
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "SELECT COUNT(*) FROM amp_category_value_location acvl " +
					"INNER JOIN amp_category_value acv ON (acvl.parent_category_value = acv.id)" +
					"WHERE acv.category_value = 'Region'";
             qry = session.createSQLQuery(queryString);
             count=((BigInteger)qry.uniqueResult()).intValue();
             
		} catch (Exception ex) {
			logger.error("Exception while getting regions amount:" + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return count;
	}
	
	public static Collection<AmpFundingDetail>  getFundingDetailsByRegion(Long id){
		Session session = null;
		Collection col = new ArrayList();
        try {	
        	session = PersistenceManager.getRequestDBSession();
        	String queryString = "SELECT " +
					"afd.`amp_fund_detail_id`,afd.`fiscal_year`,afd.`fiscal_quarter`,afd.`transaction_type`," +
					"afd.`adjustment_type`,afd.`transaction_date`,afd.`transaction_date2`,afd.`reporting_date`," +
					"(afd.`transaction_amount`*aal.`location_percentage`/100) as transaction_amount," +
					"afd.`language`,afd.`version`,afd.`cal_type`,afd.`org_role_code`,afd.`exp_category`," +
					"afd.`fixed_exchange_rate`,afd.`disb_order_id`,afd.`disbursement_order_rejected`,afd.`reporting_org_id`," +
					"afd.`amp_currency_id`,afd.`amp_funding_id`,afd.`ipa_contract_id`,afd.`fixed_base_currency_id`,afd.`pledge_id` " +
					"FROM `amp_funding_detail` afd " +
					"INNER JOIN `amp_funding` af ON (af.`amp_funding_id`=afd.`amp_funding_id`) " +
					"INNER JOIN `amp_activity` act ON (af.`amp_activity_id`=act.`amp_activity_id`) " +
					"INNER JOIN `amp_activity_location` aal ON (act.`amp_activity_id`=aal.`amp_activity_id`) " +
					"INNER JOIN `amp_location` al ON (al.`amp_location_id`= aal.`amp_location_id`) " +
					"WHERE al.`region_location_id` =:id";
					 
					 
        	Query qry = session.createSQLQuery(queryString).addEntity(AmpFundingDetail.class);
        	qry.setParameter("id", id, Hibernate.LONG);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting funding details by regions:" + ex);
			ex.printStackTrace();
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}
	
	public static Collection<AmpFundingDetail>  getFundingDetailsBySector(Long id){
		Session session = null;
		Collection col = new ArrayList();
        try {	
        	session = PersistenceManager.getRequestDBSession();
        	String queryString = "SELECT " +
					"afd.`amp_fund_detail_id`,afd.`fiscal_year`,afd.`fiscal_quarter`,afd.`transaction_type`," +
					"afd.`adjustment_type`,afd.`transaction_date`,afd.`transaction_date2`,afd.`reporting_date`," +
					"(afd.`transaction_amount`*aas.`sector_percentage`/100) as transaction_amount," +
					"afd.`language`,afd.`version`,afd.`cal_type`,afd.`org_role_code`,afd.`exp_category`," +
					"afd.`fixed_exchange_rate`,afd.`disb_order_id`,afd.`disbursement_order_rejected`,afd.`reporting_org_id`," +
					"afd.`amp_currency_id`,afd.`amp_funding_id`,afd.`ipa_contract_id`,afd.`fixed_base_currency_id`,afd.`pledge_id` " +
					"FROM `amp_funding_detail` afd " +
					"INNER JOIN `amp_funding` af ON (af.`amp_funding_id`=afd.`amp_funding_id`) " +
					"INNER JOIN `amp_activity` act ON (af.`amp_activity_id`=act.`amp_activity_id`) " +
					"INNER JOIN `amp_activity_sector` aas ON (act.`amp_activity_id`=aas.`amp_activity_id`) " +
					"INNER JOIN `amp_sector` asec ON (asec.`amp_sector_id`= aas.`amp_sector_id`) " +
					"WHERE (asec.`amp_sector_id` =:id " +
					"OR asec.`parent_sector_id` =:id " +
					"OR asec.`parent_sector_id` IN (SELECT asec1.`amp_sector_id` FROM  `amp_sector` asec1 WHERE asec1.`parent_sector_id` =:id)) ";
					 
        	Query qry = session.createSQLQuery(queryString).addEntity(AmpFundingDetail.class);
        	qry.setParameter("id", id, Hibernate.LONG);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting funding details by sector:" + ex);
			ex.printStackTrace();
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}
	
	public static Collection<AmpActivity> getActivitiesUsingFilter(VisualizationForm form) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			
			//String queryString = " SELECT act FROM " + AmpActivityVersion.class.getName() + " act ";
			String queryString = "SELECT distinct * FROM amp_activity WHERE 1=1 ";
			
			if (form.getFilter().getOrganizationsSelected()!=null && form.getFilter().getOrganizationsSelected().size() > 0) {
				queryString += "AND amp_activity_id IN (SELECT v.amp_activity_id FROM v_donors v  " +
						"WHERE v.amp_donor_org_id IN ("	+ Util.toCSString(form.getFilter().getOrganizationsSelected()) + "))";
			}
			
			if (form.getFilter().getSectorsSelected()!=null && form.getFilter().getSectorsSelected().size() > 0) {
				queryString += "AND amp_activity_id IN (SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
					+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
					+ "AND c.name='Primary' AND (aas.amp_sector_id IN (" + Util.toCSString(form.getFilter().getSectorsSelected()) + ") " 
					+ "OR aas.amp_sector_id IN (SELECT s2.amp_sector_id FROM amp_sector s2 WHERE s2.parent_sector_id IN (" + Util.toCSString(form.getFilter().getSectorsSelected()) + ")) " 
					+ "OR aas.amp_sector_id IN (SELECT s3.amp_sector_id FROM amp_sector s3 WHERE s3.parent_sector_id IN (SELECT s4.amp_sector_id FROM amp_sector s4 WHERE s4.parent_sector_id IN (" + Util.toCSString(form.getFilter().getSectorsSelected()) + ")))))";
			}
			
			if (form.getFilter().getLocationsSelected()!=null && form.getFilter().getLocationsSelected().size() > 0) {
				queryString += "AND amp_activity_id IN (SELECT aal.amp_activity_id FROM amp_activity_location aal, amp_location al " +
				"WHERE ( aal.amp_location_id=al.amp_location_id AND " +
				"al.location_id IN ("+ Util.toCSString(form.getFilter().getLocationsSelected()) + ") ))";
			}
			
			Query qry = session.createSQLQuery(queryString).addEntity(AmpActivity.class);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get Activities with filter, " + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}
	
	public static Collection<AmpSector> getSectorsUsedInActivities(String actList){
		Session session = null;
        Query qry = null;
        Collection col = new ArrayList();
        if (actList!=null && actList.length()>0) {
			try {	
	        	session = PersistenceManager.getSession();
				String queryString = "SELECT DISTINCT s.* FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
						+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id AND s.parent_sector_id is null "
						+ "AND c.name='Primary' AND aas.amp_activity_id in (" + actList + ")";
	             qry = session.createSQLQuery(queryString).addEntity(AmpSector.class);
	             col = qry.list();
			} catch (Exception ex) {
				logger.error("Exception while getting sectors : " + ex);
			} finally {
				try {
					if (session != null)
						PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.error("releaseSession() failed");
				}
			}
        }
		return col;
	}
	
	public static Collection<AmpSector> getSectors(){
		Session session = null;
        Query qry = null;
        Collection col = new ArrayList();
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "SELECT DISTINCT s.* FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
					+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id AND s.parent_sector_id is null "
					+ "AND c.name='Primary'";
             qry = session.createSQLQuery(queryString).addEntity(AmpSector.class);
             col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting sectors : " + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
        return col;
	}
	
	public static List<AmpSector> getSubSectors(Long id){
		Session session = null;
        Query qry = null;
        List col = new ArrayList();
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "SELECT s.* FROM amp_sector s "
					+ "WHERE s.parent_sector_id =:id " ;
             qry = session.createSQLQuery(queryString).addEntity(AmpSector.class);
             qry.setParameter("id", id, Hibernate.LONG);
             col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting sub-sectors : " + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
        return col;
	}
	
	public static Collection<AmpLocation> getRegionsUsedInActivities(String actList){
		Session session = null;
        Query qry = null;
        Collection col = new ArrayList();
        if (actList!=null && actList.length()>0) {
	        try {	
	        	session = PersistenceManager.getSession();
	        	String queryString = "SELECT DISTINCT loc.* FROM amp_activity_location aal, amp_location loc, amp_category_value_location acvl, amp_category_value acv "
	        		+ " WHERE aal.amp_location_id = loc.amp_location_id AND loc.location_id = acvl.id AND acvl.parent_category_value = acv.id AND acv.category_value = 'Region' "
	        		+ " AND aal.amp_activity_id in (" + actList + ")";
				
	             qry = session.createSQLQuery(queryString).addEntity(AmpLocation.class);
	             col = qry.list();
	             
			} catch (Exception ex) {
				logger.error("Exception while getting regions :" + ex);
			} finally {
				try {
					if (session != null)
						PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.error("releaseSession() failed");
				}
			}
        }
		return col;
	}
	
	public static Collection<AmpLocation> getLocationsUsedInActivities(String actList){
		Session session = null;
        Query qry = null;
        Collection col = new ArrayList();
        if (actList!=null && actList.length()>0) {
	        try {	
	        	session = PersistenceManager.getSession();
	        	String queryString = "SELECT DISTINCT loc.* FROM amp_activity_location aal, amp_location loc, amp_category_value_location acvl, amp_category_value acv "
	        		+ " WHERE aal.amp_location_id = loc.amp_location_id AND loc.location_id = acvl.id AND acvl.parent_category_value = acv.id "
	        		+ " AND aal.amp_activity_id in (" + actList + ")";
				
	             qry = session.createSQLQuery(queryString).addEntity(AmpLocation.class);
	             col = qry.list();
	             
			} catch (Exception ex) {
				logger.error("Exception while getting regions :" + ex);
			} finally {
				try {
					if (session != null)
						PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.error("releaseSession() failed");
				}
			}
        }
		return col;
	}
		
}
