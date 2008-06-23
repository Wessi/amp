package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpIndicatorSector;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Sector;

/**
 * Utility class for persisting all Sector with Scheme related entities
 * @author Govind G Dalwani
 */


public class SectorUtil {

	private static Logger logger = Logger.getLogger(SectorUtil.class);

	public static Collection searchForSector(String keyword) {
		Session session = null;
		Collection col = new ArrayList();

		logger.info("INSIDE Search Sector DBUTIL..... ");

		try {

			session = PersistenceManager.getSession();
			int tempIncr = 0;
				logger.info("searching SECTORS...............");
				/*String qryStr = "select country.countryId,country.countryName,region.ampRegionId,region.name "
						+ "from "
						+ Country.class.getName()
						+ " country , "
						+ AmpRegion.class.getName()
						+ " "
						+ "region where region.name like '%"
						+ keyword
						+ "%' and " + "country.iso = region.country"; */

				String qryStr = "select sector from " + AmpSector.class.getName()
					+ " sector , "
					+ AmpSectorScheme.class.getName()
					+ " sscheme"
					+ " where lower(sector.name) like '%"
					+ keyword.toLowerCase()
					+ "%' and " + "sector.ampSecSchemeId = sscheme.ampSecSchemeId";

				Query qry = session.createQuery(qryStr);
				//qry.setParameter("orgType", orgType, Hibernate.LONG) ;
				//col =qry.list();
				Iterator itr = qry.list().iterator();

				ActivitySector sectr;
				tempIncr = 0;

				while (itr.hasNext()) {
					AmpSector as=(AmpSector) itr.next();
					sectr = new ActivitySector();
			   		sectr.setSectorScheme(as.getAmpSecSchemeId().getSecSchemeName());
					if(as.getParentSectorId() != null){
						
						sectr.setSectorName(as.getParentSectorId().getName());
						sectr.setSectorId(as.getParentSectorId().getAmpSectorId());
						sectr.setSubsectorLevel1Id(as.getAmpSectorId());
						sectr.setSubsectorLevel1Name(as.getName());
						
						if(as.getParentSectorId().getParentSectorId() != null){
						
							sectr.setSectorName(as.getParentSectorId().getParentSectorId().getName());
							sectr.setSectorId(as.getParentSectorId().getAmpSectorId());
							sectr.setSubsectorLevel1Id(as.getParentSectorId().getAmpSectorId());
							sectr.setSubsectorLevel1Name(as.getParentSectorId().getName());
							sectr.setSubsectorLevel2Id(as.getAmpSectorId());
							sectr.setSubsectorLevel2Name(as.getName());
							
						}
					}else{
						
						sectr.setSectorName(as.getName());
				   		sectr.setSectorId(as.getAmpSectorId());
						
						
					}
					col.add(sectr);
				}

		} catch (Exception ex) {
			logger.debug("Unable to search sectors" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return col;
	}//End Search Sector.

	public static List getAmpSectors(Long id) {
		ArrayList ampSectors = new ArrayList();
		AmpSector ampSector = null;
		Session session = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();

			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a " + "where (a.ampActivityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			AmpActivity ampActivity = null;
			while (itr.hasNext()) {
				ampActivity = (AmpActivity) itr.next();
			}
			// end

			iter = ampActivity.getSectors().iterator();
			while (iter.hasNext()) {
				//ampSector = (AmpSector) iter.next();
				ampSector = ((AmpActivitySector) iter.next()).getSectorId();
				ampSectors.add(ampSector);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp sectors from database :" + ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("Executed successfully " + ampSectors.size());
		return ampSectors;
	}

	public static Collection getSectorActivities(Long sectorId) {

		Session sess = null;
		Collection col = null;

		try {
			sess = PersistenceManager.getSession();
			AmpSector sector = (AmpSector) sess.load(AmpSector.class, sectorId);

			Iterator itr = sector.getAidlist().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}
		} catch (Exception e) {
			logger.debug("Exception from getSectorActivities()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;

	}

	public static Collection getAllSectorSchemes() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select ss from "
					+ AmpSectorScheme.class.getName() + " ss "
					+ "order by ss.secSchemeName";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get all sector schemes, " + ex);
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

	public static Collection<AmpSector> getAllParentSectors(Long secSchemeId) {
		Session session = null;
		Collection<AmpSector> col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s " + "where amp_sec_scheme_id = " + secSchemeId
					+ " and parent_sector_id is null " + "order by s.name";
			Query qry = session.createQuery(queryString);
			col = qry.list();

		} catch (Exception e) {
			logger.error("Cannot get parent sectors, " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return col;
	}
	
	public static Collection getAllParentSectors() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s "	+ " where parent_sector_id is null " + "order by s.name";
			Query qry = session.createQuery(queryString);
			col = qry.list();

		} catch (Exception e) {
			logger.error("Cannot get parent sectors, " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return col;
	}
	
	
	
	public static List getAllSectorsFromScheme(Long secSchemeId) {
		Session session = null;
		List col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s " + "where amp_sec_scheme_id = " + secSchemeId;
			Query qry = session.createQuery(queryString);
			col = qry.list();

		} catch (Exception e) {
			logger.error("Cannot get parent sectors, " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return col;
	}

	public static Collection getAllSectors() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select sc from " + AmpSector.class.getName()
					+ " sc order by sc.name";
			Query qry = session.createQuery(queryString);
			col = qry.list();

		} catch (Exception e) {
			logger.error("Cannot get sectors, " + e);
		} 
		return col;
	}
	
	public static Collection<AmpSector> getAllChildSectors(Long parSecId) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s " + "where parent_sector_id = " + parSecId
					+ " order by s.name";
			Query qry = session.createQuery(queryString);
			col = qry.list();

		} catch (Exception e) {
			logger.error("Cannot get child sectors, " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return col;
	}

	public static void updateSectorOrganisation(Long sectorId,
			AmpOrganisation organisation) {
		AmpSector sector = getAmpSector(sectorId);
		sector.setAmpOrgId(organisation);
		DbUtil.update(sector);
	}

	public static void updateSubSectors(AmpSector sector,
			AmpOrganisation organisation) {

		int index = 0;
		ArrayList sectorList = new ArrayList();
		Iterator itr = null;

		try {

			itr = getSubSectors(sector.getAmpSectorId()).iterator();
			while (itr.hasNext()) {
				Sector subSec = (Sector) itr.next();
				sectorList.add(subSec);
			}
			while (index < sectorList.size()) {
				Sector sec = (Sector) sectorList.get(index++);
				itr = getSubSectors(sec.getSectorId()).iterator();

				while (itr.hasNext()) {
					Sector subSec = (Sector) itr.next();
					sectorList.add(subSec);
				}
			}

			for (int i = 0; i < sectorList.size(); i++) {
				Sector sec = (Sector) sectorList.get(i);
				updateSectorOrganisation(sec.getSectorId(), organisation);
			}
		} catch (Exception e) {
			logger.debug("Exception from updateSubSectors()");
			logger.debug(e.toString());
		}
	}

	// Retreives all sub-sectors within the sector with id 'parentSecId'
	public static Collection getSubSectors(Long parentSecId) {

		Session session = null;
		Query qry = null;
		Collection col = new ArrayList();
		Iterator itr = null;
		AmpSector ampSector = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();

			if (parentSecId.intValue() == 0) {
				queryString = "select s from " + AmpSector.class.getName()
						+ " s where s.parentSectorId is null order by s.name";

				qry = session.createQuery(queryString);
			} else {
				queryString = "select s from " + AmpSector.class.getName()
						+ " s where (s.parentSectorId=:parentSectorId) "
						+ "order by s.name";

				qry = session.createQuery(queryString);
				qry.setParameter("parentSectorId", parentSecId, Hibernate.LONG);
			}
			itr = qry.list().iterator();

			while (itr.hasNext()) {
				ampSector = (AmpSector) itr.next();

				Sector sec = new Sector();
				if (ampSector.getAmpOrgId() != null) {
					sec.setOrgId(ampSector.getAmpOrgId().getAmpOrgId());
					sec.setOrgName(ampSector.getAmpOrgId().getName());
				}
				sec.setSectorId(ampSector.getAmpSectorId());
				sec.setSectorName(ampSector.getName());
				col.add(sec);
			}

		} catch (Exception ex) {
			logger.error("Unable to get subsectors");
			ex.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return col;
	}

	/*
	 * Retreives the sector details for the sector with the id 'sectorId'
	 */
	public static Sector getSector(Long sectorId) {

		Session session = null;
		Query qry = null;
		Iterator itr = null;
		AmpSector ampSector = null;
		Sector sec = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select s from " + AmpSector.class.getName()
					+ " s where (s.ampSectorId=:ampSectorId)";

			qry = session.createQuery(queryString);
			qry.setParameter("ampSectorId", sectorId, Hibernate.LONG);
			itr = qry.list().iterator();

			if (itr.hasNext()) {
				ampSector = (AmpSector) itr.next();
				sec = new Sector(ampSector.getAmpSectorId(), ampSector
						.getName(), ampSector.getAmpOrgId().getAmpOrgId(),
						DbUtil.getOrganisation(ampSector.getAmpOrgId().getAmpOrgId())
								.getName());

			}

		} catch (Exception ex) {
			logger.error("Unable to get sector info");
			logger.debug("Exceptiion " + ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return sec;
	}

	public static AmpIndicatorSector getIndIcatorSector(Long indicatorId){
		
		Session session = null;
		Query qry = null;
		AmpIndicatorSector indSectorId = null;
		Iterator itr = null;
		
		
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select s from " + AmpIndicatorSector.class.getName()
			+ " s where (s.themeIndicatorId=:themeIndicatorId)";
			
			qry = session.createQuery(queryString);
			qry.setParameter("themeIndicatorId", indicatorId, Hibernate.LONG);
			itr = qry.list().iterator();

			if (itr.hasNext()) {
				indSectorId = (AmpIndicatorSector) itr.next();
			}
			
		} catch (Exception e) {
			logger.error("Unable to get sector");
			logger.debug("Exceptiion " + e);
		}
		
		return indSectorId;
	}
	
	public static boolean getIndIcatorSector(Long indicatorId,Long sectorId){
		
		Session session = null;
		Query qry = null;
		List <AmpIndicatorSector> indSectors = null;
		boolean exist=false;
		
		
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select s from " + AmpIndicatorSector.class.getName()
			+ " s where (s.themeIndicatorId=:themeIndicatorId) and s.sectorId=:sectorId";
			
			qry = session.createQuery(queryString);
			qry.setParameter("themeIndicatorId", indicatorId, Hibernate.LONG);
			qry.setLong("sectorId", sectorId);
			indSectors = qry.list();
			if(indSectors!=null&&indSectors.size()>0){
				exist=true;
			}

			
		} catch (Exception e) {
			logger.error("Unable to get sector");
			logger.debug("Exceptiion " + e);
		}
		
		return exist;
	}
	
	public static AmpSector getAmpSector(Long id) {

		Session session = null;
		Query qry = null;
		Iterator itr = null;
		AmpSector ampSector = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select s from " + AmpSector.class.getName()
					+ " s where (s.ampSectorId=:ampSectorId)";

			qry = session.createQuery(queryString);
			qry.setParameter("ampSectorId", id, Hibernate.LONG);
			itr = qry.list().iterator();

			if (itr.hasNext()) {
				ampSector = (AmpSector) itr.next();
			}

		} catch (Exception ex) {
			logger.error("Unable to get amp_sector info");
			logger.debug("Exceptiion " + ex);
		}

		return ampSector;

	}

	public static AmpSectorScheme getAmpSectorScheme(Long id) {
		Session session = null;
		Query qry = null;
		Iterator itr = null;
		AmpSectorScheme ampSectorScheme = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select s from " + AmpSectorScheme.class.getName()
					+ " s where (s.ampSecSchemeId=:ampSectorSchemeId)";

			qry = session.createQuery(queryString);
			qry.setParameter("ampSectorSchemeId", id, Hibernate.LONG);
			itr = qry.list().iterator();

			if (itr.hasNext()) {
				ampSectorScheme = (AmpSectorScheme) itr.next();
			}

		} catch (Exception ex) {
			logger.error("Unable to get amp_sector_scheme info");
			logger.debug("Exception " + ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return ampSectorScheme;

	}

	/*
	 * update sector details
	 */
	public static void updateSector(AmpSector sector) {

		if (sector.getParentSectorId() == null
				&& organisationChanged(sector.getAmpSectorId(), sector
						.getAmpOrgId()) == true) {

			updateSubSectors(sector, sector.getAmpOrgId());
		}
		DbUtil.update(sector);
	}


	public static ArrayList getAmpSectors() {
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		ArrayList sector = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select Sector from "
					+ AmpSector.class.getName()
					+ " Sector where Sector.parentSectorId is null order by Sector.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampSector = (AmpSector) iter.next();
				sector.add(ampSector);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Sector names  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return sector;
	}

	public static ArrayList getAmpSubSectors() {
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		ArrayList subsector = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select Sector from "
					+ AmpSector.class.getName()
					+ " Sector where Sector.parentSectorId is not null order by Sector.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();
			while (iter.hasNext()) {
				ampSector = (AmpSector) iter.next();
				subsector.add(ampSector);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp sector names  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return subsector;
	}

	public static ArrayList getAmpSubSectors(Long ampSectorId) {
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		ArrayList subsector = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select Sector from "
					+ AmpSector.class.getName()
					+ " Sector where Sector.parentSectorId is not null and Sector.parentSectorId.ampSectorId=:ampSectorId";
			q = session.createQuery(queryString);
			q.setParameter("ampSectorId", ampSectorId, Hibernate.LONG);
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampSector = (AmpSector) iter.next();
				subsector.add(ampSector);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp sub sectors  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return subsector;
	}

	public static AmpSector getAmpParentSector(Long ampSectorId) {
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select Sector from " + AmpSector.class.getName()
					+ " Sector where Sector.ampSectorId=:ampSectorId";
			q = session.createQuery(queryString);
			q.setParameter("ampSectorId", ampSectorId, Hibernate.LONG);
			iter = q.list().iterator();

			ampSector = (AmpSector) iter.next();
			while (ampSector.getParentSectorId() != null)
				ampSector = ampSector.getParentSectorId();
			//	ampSectorId=ampSector.getAmpSectorId();
			//	logger.debug("Sector Id: " + ampSectorId);

		} catch (Exception ex) {
			logger.error("Unable to get Amp sub sectors  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return ampSector;
	}

	public static ArrayList getDonorSectors(Long ampSecSchemeId,
			Long ampActivityId) {
		logger.debug("In getDonorSectors");
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		ArrayList sector = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			//queryString = " select Sector from " + AmpSector.class.getName()
			// + " Sector where Sector.parentSectorId is null order by
			// Sector.name";

			queryString = "select sector from "
					+ AmpSector.class.getName()
					+ " sector, "
					+ AmpActivity.class.getName()
					+ " act where (sector.ampSecSchemeId = :ampSecSchemeId) and (act.ampActivityId = :ampActivityId) and sector.parentSectorId is null";
			//queryString = "select sector from " + AmpSector.class.getName() +
			// " sector, " + AmpActivity.class.getName() + " act where
			// (sector.ampSecSchemeId = :ampSecSchemeId) and (act.ampActivityId
			// = :ampActivityId)";
			q = session.createQuery(queryString);
			q.setParameter("ampSecSchemeId", ampSecSchemeId, Hibernate.LONG);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);

			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampSector = (AmpSector) iter.next();
				sector.add(ampSector);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Sector names  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return sector;
	}

	public static Collection searchSectorCode(String key) {
		Session sess = null;
		Collection col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s where s.sectorCode like '%" + key + "%'";
			qry = sess.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}

		} catch (Exception e) {
			logger.debug("Exception from searchSectorCode()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;

	}

	public static Collection searchSectorName(String key) {
		Session sess = null;
		Collection col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s where s.name like '%" + key + "%'";
			qry = sess.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}

		} catch (Exception e) {
			logger.debug("Exception from searchSectorName()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static boolean organisationChanged(Long sectorId,
			AmpOrganisation organisation) {
		logger.debug("in organisationChanged()");
		Session sess = null;
		boolean flag = false;

		try {
			sess = PersistenceManager.getSession();
			String qryString = "select s from " + AmpSector.class.getName()
					+ " s where (s.ampSectorId=:ampSectorId)";

			Query qry = sess.createQuery(qryString);
			qry.setParameter("ampSectorId", sectorId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();

			if (itr.hasNext()) {
				AmpSector ampSector = (AmpSector) itr.next();
				logger.debug(ampSector.getAmpOrgId().getName() + " !- "
						+ organisation.getName());
				if (ampSector.getAmpOrgId().getAmpOrgId() != organisation
						.getAmpOrgId()) {
					flag = true;
				}
			}
		} catch (Exception e) {
			logger.debug("Exception thrown from fn organisationChanged()");
			logger.debug(e.toString());
		} finally {
			if (sess != null) {
				try {
					PersistenceManager.releaseSession(sess);
				} catch (Exception ex) {
					logger.debug("releaseSession() 2 failed");
					logger.debug(ex.toString());
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * 
	 * @return List of sectors and sub-sectors ordered by sectors alphabetically
	 *  and then by sub-sectors alphabetically (Ex. A, a1, a2, a3, B, b1, b2, etc...). 
	 *  The names of the sectors are a embelished (upper case, or added some spaces) 
	 *  for better presentation. 
	 *  DO NOT save this objects back to the database  !!!! 
	 */
	public static List<AmpSector> getAmpSectorsAndSubSectors( String configurationName ) {
		List<AmpSector> ret 	= new ArrayList<AmpSector>();
		Long id					= null;
		try {
			Collection<AmpClassificationConfiguration> configs	= 
					 SectorUtil.getAllClassificationConfigs();
			Iterator<AmpClassificationConfiguration> confIter	= configs.iterator();
			while ( confIter.hasNext() ) {
				AmpClassificationConfiguration conf		= confIter.next();
				if ( configurationName.equals( conf.getName() ) ) {
					if ( conf.getClassification()!= null )
						id	= conf.getClassification().getAmpSecSchemeId();
				}
			}
			if ( id != null ) {
				Collection<AmpSector> dbReturnSet = SectorUtil.getAllParentSectors(id);
				Iterator<AmpSector> iter = dbReturnSet.iterator();
				while (iter.hasNext()) {
					AmpSector ampSector = iter.next();
					ampSector.setName( ampSector.getName().toUpperCase() );
					ret.add(ampSector);
					Collection<AmpSector> dbChildReturnSet = 
							SectorUtil.getAllChildSectors( ampSector.getAmpSectorId() );
					Iterator<AmpSector> iterSub = dbChildReturnSet.iterator();
					while (iterSub.hasNext()) {
						AmpSector ampSubSector = (AmpSector) iterSub.next();
						String temp = " -- " + ampSubSector.getName();				
						ampSubSector.setName(temp);
						ret.add(ampSubSector);
					}
				}
			}
		
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}


// Govind's Starts from here!!
	/*
	 * this is to get the sector schemes from the ampSectorScheme table
	 */
	public static Collection getSectorSchemes(){
		String queryString = null;
		Session session = null;
		Collection col = null;
		Query qry = null;

		try
		{
			session = PersistenceManager.getSession();
			queryString ="select pi from "+AmpSectorScheme.class.getName()+" pi ";
			qry = session.createQuery(queryString);
			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get report names  from database " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2)
			{
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}

/*
 * this is to get the level one sectors from the AmpSector table
 */
	public static Collection getSectorLevel1(Integer schemeId)
	{
		String queryString = null;
		Session session = null;
		Collection col = null;
		Query qry = null;

		try
		{
			session = PersistenceManager.getSession();
			queryString ="select pi from "+AmpSector.class.getName()+" pi where pi.ampSecSchemeId=:schemeId and pi.parentSectorId IS null";
			qry = session.createQuery(queryString);
			qry.setParameter("schemeId",schemeId,Hibernate.INTEGER);
			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get report names  from database " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2)
			{
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}
/*
 * get scheme to be edited
 */
	public static Collection getEditScheme(Integer schemeId)
	{
		String queryString = null;
		Session session = null;
		Collection col = null;
		Query qry = null;

		try
		{
			session = PersistenceManager.getSession();
			queryString ="select pi from "+AmpSectorScheme.class.getName()+" pi where pi.ampSecSchemeId=:schemeId";
			qry = session.createQuery(queryString);
			qry.setParameter("schemeId",schemeId,Hibernate.INTEGER);
			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get report names  from database " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2)
			{
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}
        
        
    /**
     * Returns All  Configurations of Classifications
     * 
     * @return All Configurations
     * @throws DgException If exception occurred
     */
    public static List<AmpClassificationConfiguration> getAllClassificationConfigs() throws DgException {
        String queryString = null;
        Session session = null;
        List<AmpClassificationConfiguration> configs = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select cls from " + AmpClassificationConfiguration.class.getName() + " cls ";
            qry = session.createQuery(queryString);
            configs = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get report names  from database " + ex.getMessage());
            throw new DgException(ex);

        }

        return configs;
    }

    /**
     * Returns Classification Configuration by Configuration Id
     * 
     * @param configId Configuration Id
     * @return Classification Configuration using  Configuration Id
     * @throws DgException If exception occurred
     */
    public static AmpClassificationConfiguration getClassificationConfigById(Long configId) throws DgException {

        Session session = null;
        AmpClassificationConfiguration config = null;


        try {
            session = PersistenceManager.getRequestDBSession();
            config = (AmpClassificationConfiguration) session.load(AmpClassificationConfiguration.class, configId);
        } catch (Exception ex) {
            logger.error("Unable to get configs from database " + ex.getMessage());
            throw new DgException(ex);

        }

        return config;
    }

    /**
     * Returns true if specified classification is selected as default classification 
     * in the configuration
     * otherwise returns false.
     * 
     * @param classificationId  Id of classification
     * @return true If specified classification is selected as default classification 
     * in the configuration
     * @throws DgException If exception occurred
     */
    public static boolean isClassificationUsed(Long classificationId) throws DgException {

        Session session = null;
        boolean used = false;
        String queryString = null;
        List configs = null;
        Query qry = null;


        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select cls from " + AmpClassificationConfiguration.class.getName() + " cls where cls.classification=:classificationId ";
            qry = session.createQuery(queryString);
            qry.setLong("classificationId", classificationId);
            configs = qry.list();
            if (configs != null && configs.size() > 0) {
                used = true;
            }
        } catch (Exception ex) {
            logger.error("Unable to get configs from database " + ex.getMessage());
            throw new DgException(ex);

        }

        return used;
    }

    /**
     * adds or update classification configuration 
     * 
     * 
     * @param configId  Id of configuration
     * @param configName Name of configuration
     * @param multiSector  
     * @param classification Default classification
     * @throws DgException If exception occurred
     */
    public static void saveClassificationConfig(Long configId, String configName, boolean multiSector, AmpSectorScheme classification) throws DgException {

        Session session = null;
        AmpClassificationConfiguration config = null;
        Transaction tx = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            if (configId != null && !configId.equals(0l)) {
                //Load existed configuration for update procedure.
                config = (AmpClassificationConfiguration) session.load(AmpClassificationConfiguration.class, configId);
            } else {
                // Create new configuration
                config = new AmpClassificationConfiguration();

            }
            config.setName(configName);
            config.setMultisector(multiSector);
            config.setClassification(classification);
            tx = session.beginTransaction();
            session.saveOrUpdate(config);
            tx.commit();



        } catch (Exception ex) {
            logger.error("Unable to save config to database " + ex.getMessage());
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception rbf) {
                    logger.error("Rollback failed");
                }
            }
            throw new DgException(ex);

        }

    }
    
    /**
     * gets id of classification which is selected in primary configuration
     * 
     *
     * @return Id of classification  
     * @throws DgException If exception occurred
     */
    public static Long getPrimaryConfigClassificationId() throws DgException {

        Session session = null;
        Long id = null;
        Transaction tx = null;
        String queryString = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select cls.ampSecSchemeId from "
                    + AmpClassificationConfiguration.class.getName() +
                    " config inner join config.classification cls "+
                    " where config.primary=true ";
            qry = session.createQuery(queryString);
            //There must be only one primary configuration in database
            id=(Long)qry.uniqueResult();

        } catch (Exception ex) {
            logger.error("Unable to save config to database " + ex.getMessage());
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception rbf) {
                    logger.error("Rollback failed");
                }
            }
            throw new DgException(ex);

        }
        return id;

    }
    /*
 * this is to delete a scheme
 */
	public static void deleteScheme(Long schemeId)
	{
		logger.info(" deleting the scheme");
		Session session = null;
		Transaction tx = null;
		try
		{
			session = PersistenceManager.getSession();
			AmpSectorScheme scheme = (AmpSectorScheme) session.load(
					AmpSectorScheme.class,schemeId);
			tx = session.beginTransaction();
			session.delete(scheme);
			tx.commit();
		}
		catch (Exception e)
		{
			logger.error("Exception from deleteQuestion() :" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null)
			{
				try
				{
					tx.rollback();
				}
				catch (Exception trbf)
				{
					logger.error("Transaction roll back failed ");
					e.printStackTrace(System.out);
				}
			}
		}
		finally
		{
			if (session != null)
			{
				try
				{
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf)
				{
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
	}

	/*
	 * this is to get the level one sectors from the AmpSector table
	 */
		public static AmpSectorScheme getParentSchemeId(Long id)
		{
			String queryString = null;
			Session session = null;
			Collection col = null;
			Query qry = null;
			AmpSectorScheme schemeId=null;

			try
			{
				session = PersistenceManager.getSession();

				AmpSector sec = (AmpSector) session.load(AmpSector.class,
						id);
				 schemeId = sec.getAmpSecSchemeId();

			}
			catch(Exception ex)
			{
				logger.error("Unable to get report names  from database " + ex.getMessage());
				ex.printStackTrace(System.out);
			}
			finally
			{
				try
				{
					PersistenceManager.releaseSession(session);
				}
				catch (Exception ex2)
				{
					logger.error("releaseSession() failed ");
				}
			}
			return schemeId;
		}

                public static void deleteSector(Long sectorId)
                                {
                                        logger.info(" deleting the Sector");
                                        Session session = null;
                                        Transaction tx = null;
                                        try
                                        {
                                                session = PersistenceManager.getSession();
                                                AmpSector sector = (AmpSector) session.load(
                                                                AmpSector.class,sectorId);
                                                tx = session.beginTransaction();
                                                sector.setAidlist(null);
                                                session.delete(sector);
                                                tx.commit();
                                        }
                                        catch (Exception e)
                                        {
                                                logger.error("Exception from deleteQuestion() :" + e.getMessage());
                                                e.printStackTrace(System.out);
                                                if (tx != null)
                                                {
                                                        try
                                                        {
                                                                tx.rollback();
                                                        }
                                                        catch (Exception trbf)
                                                        {
                                                                logger.error("Transaction roll back failed ");
                                                                e.printStackTrace(System.out);
                                                        }
                                                }
                                        }
                                        finally
                                        {
                                                if (session != null)
                                                {
                                                        try
                                                        {
                                                                PersistenceManager.releaseSession(session);
                                                        }
                                                        catch (Exception rsf)
                                                        {
                                                                logger.error("Failed to release session :" + rsf.getMessage());
                                                        }
                                                }
                                      }
		}

         
                
                public static void deleteIndSector(Long sectorid,Long indid){
	        	
        	    logger.info(" deleting the indsectors");
	   			Session session = null;
	   			Transaction tx = null;
	   			AmpThemeIndicators ampThemeInd=null;
        	   
        	   try {
        		   session = PersistenceManager.getRequestDBSession();
        		   tx = session.beginTransaction();
        		   ampThemeInd=(AmpThemeIndicators)session.load(AmpThemeIndicators.class,indid);
        		   Iterator itr=ampThemeInd.getSectors().iterator();
        		   while(itr.hasNext()){
        			   AmpIndicatorSector ind=(AmpIndicatorSector)itr.next();
        			   if(ind.getSectorId().getAmpSectorId().equals(sectorid)){
        				   itr.remove();
        				   session.delete(ind);
        				     
        			   }
        		   }
        		   session.update(ampThemeInd);
        		   tx.commit();
        		   session.flush();
        		   
				
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("Exception from deleteIndSectors:" + e.getMessage());
				e.printStackTrace(System.out);
			}
        	   
           }
                

    public static Set<AmpSector> getSectorDescendents( Collection<AmpSector> parentSectors ) {
    	Set<AmpSector> generatedSectors	= new HashSet<AmpSector>();
    	
    	generatedSectors.addAll( parentSectors );
    	Iterator sectorIterator=parentSectors.iterator();
		
		while(sectorIterator.hasNext())
		{//process each sector and get all its children
			AmpSector currentSector=(AmpSector)sectorIterator.next();
			if(currentSector!=null)
			{
				Collection childSectors=SectorUtil.getAllChildSectors(currentSector.getAmpSectorId());
				generatedSectors.addAll(childSectors); //add the children sectors to the filter
				
				//add the grand children
				Iterator childSectorsIterator=childSectors.iterator();
				while(childSectorsIterator.hasNext())
				{
					AmpSector currentChild=(AmpSector)childSectorsIterator.next();
					Collection grandChildrenSectors=SectorUtil.getAllChildSectors(currentChild.getAmpSectorId());
					generatedSectors.addAll(grandChildrenSectors);
				}
				
			}
		
		}
		
		return generatedSectors;
    }
                
	/*
	 * this is to delete a sector

		public static void deleteSector(Long schemeId)
		{
			logger.info(" deleting the scheme");
			Session session = null;
			Transaction tx = null;
			try
			{
				session = PersistenceManager.getSession();
				AmpSector scheme = (AmpSector) session.load(
						AmpSectorScheme.class,schemeId);
				tx = session.beginTransaction();
				session.delete(scheme);
				tx.commit();
			}
			catch (Exception e)
			{
				logger.error("Exception from deleteQuestion() :" + e.getMessage());
				e.printStackTrace(System.out);
				if (tx != null)
				{
					try
					{
						tx.rollback();
					}
					catch (Exception trbf)
					{
						logger.error("Transaction roll back failed ");
						e.printStackTrace(System.out);
					}
				}
			}
			finally
			{
				if (session != null)
				{
					try
					{
						PersistenceManager.releaseSession(session);
					}
					catch (Exception rsf)
					{
						logger.error("Failed to release session :" + rsf.getMessage());
					}
				}
			}
		}
                */
}
