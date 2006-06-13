package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpWoreda;
import org.digijava.module.aim.dbentity.AmpZone;
import org.digijava.module.aim.helper.AmpLocations;
import org.digijava.module.aim.helper.Location;

public class LocationUtil {

	private static Logger logger = Logger.getLogger(LocationUtil.class);

	public static Collection searchForLocation(String keyword, int implevel) {
		Session session = null;
		Collection col = new ArrayList();
		
		logger.info("INSIDE Search Location DBUTIL..... ");
	
		try {
			
			session = PersistenceManager.getSession();
			int tempIncr = 0;
			if (implevel == 2) {
				logger.info("searching regions........");
				String qryStr = "select country.countryId,country.countryName,region.ampRegionId,region.name "
						+ "from "
						+ Country.class.getName()
						+ " country , "
						+ AmpRegion.class.getName()
						+ " "
						+ "region where region.name like '%"
						+ keyword
						+ "%' and " + "country.iso = region.country";
				
				Query qry = session.createQuery(qryStr);
				Iterator itr = qry.list().iterator();
				
				while (itr.hasNext()) {
					Object obj[] = (Object[]) itr.next();
					Long cId = (Long) obj[0];
					String cName = (String) obj[1];
					Long rId = (Long) obj[2];
					String rName = (String) obj[3];
					
					Location loc = new Location();
					loc.setCountry(cName);
					loc.setCountryId(cId);
					loc.setRegion(rName);
					loc.setRegionId(rId);
					loc.setLocId(new Long(System.currentTimeMillis() + (tempIncr++)));
					col.add(loc);
				}
			} else if (implevel == 3) {
				logger.info("searching zones........");
				
/*				String qryStr = "select country.countryId,country.countryName,region.ampRegionId,region.name "
					+ "from "
					+ Country.class.getName()
					+ " country , "
					+ AmpRegion.class.getName()
					+ " "
					+ "region where region.name like '%"
					+ keyword
					+ "%' and " + "country.iso = region.country";
*/
				String qryStr = "select country.countryId,country.countryName,region.ampRegionId, region.name, zone.ampZoneId, zone.name "
					+ "from "
					+ Country.class.getName()
					+ " country, "
					+ AmpRegion.class.getName()
					+ " region, "
					+ AmpZone.class.getName()
					+ " zone "
					+ "where zone.name like '%"
					+  keyword
					+ "%' and "
					+ "region.ampRegionId = zone.region and "
					+ "country.iso = zone.country";
				
				Query qry = session.createQuery(qryStr);
				Iterator itr = qry.list().iterator();
				
				tempIncr = 0;
				while (itr.hasNext()) {
					Object obj[] = (Object[]) itr.next();
					Long cId = (Long) obj[0];
					String cName = (String) obj[1];
					Long rId = (Long) obj[2];
					String rName = (String) obj[3];
					Long zId = (Long) obj[4];
					String zName = (String) obj[5];
					
					Location loc = new Location();
					loc.setLocId(new Long(System.currentTimeMillis()+(tempIncr++)));
					logger.info("Loc id set as " + loc.getLocId());
					loc.setCountry(cName);
					loc.setCountryId(cId);
					loc.setRegion(rName);
					loc.setRegionId(rId);
					loc.setZone(zName);
					loc.setZoneId(zId);
					col.add(loc);
				}
				
			} else if (implevel == 4) {
				logger.info("searching Woredas........");
				
				String qryStr = "select country.countryId,country.countryName,region.ampRegionId, region.name, zone.ampZoneId, zone.name, woreda.ampWoredaId, woreda.name "
					+ "from "
					+ Country.class.getName()
					+ " country, "
					+ AmpRegion.class.getName()
					+ " region, "
					+ AmpZone.class.getName()
					+ " zone, "
					+ AmpWoreda.class.getName()
					+ " woreda, "
					+ " where woreda.name like '%"
					+  keyword
					+ "%' and "
					+ "region.ampRegionId = zone.region and "
					+ "woreda.zone = zone.ampZoneId and "
					+ "country.iso = zone.country";
				

				Query qry = session.createQuery(qryStr);
				Iterator itr = qry.list().iterator();
				
				tempIncr = 0;
				while (itr.hasNext()) {
					Object obj[] = (Object[]) itr.next();
					Long cId = (Long) obj[0];
					String cName = (String) obj[1];
					Long rId = (Long) obj[2];
					String rName = (String) obj[3];
					Long zId = (Long) obj[4];
					String zName = (String) obj[5];
					Long wId = (Long) obj[6];
					String wName = (String) obj[7];
					
					Location loc = new Location();
					loc.setLocId(new Long(System.currentTimeMillis() + (tempIncr++)));
					loc.setCountry(cName);
					loc.setCountryId(cId);
					loc.setRegion(rName);
					loc.setRegionId(rId);
					loc.setZone(zName);
					loc.setZoneId(zId);
					loc.setWoreda(wName);
					loc.setWoredaId(wId);
					col.add(loc);
				}
				
			} else {
				
				logger.info("Imp level is not selected for search....");
				/*String queryString = "select loc from "
						+ AmpLocation.class.getName() + " loc "
						+ "where country like '%" + keyword + "%'";
				Query qry = session.createQuery(queryString);*/
			}
		} catch (Exception ex) {
			logger.debug("Unable to search " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}

		return col;
	}
	//End Search Location.
	
	public static AmpLocation getAmpLocation(Long countryId, Long regionId,
			Long zoneId, Long woredaId) {

		Session session = null;
		AmpLocation loc = null;
		boolean flag = false;

		try {
			session = PersistenceManager.getSession();

			String queryString = "select l from " + AmpLocation.class.getName()
					+ " l";
			if (countryId != null && (!(countryId.equals(new Long(-1))))) {
				if (!flag) {
					queryString += " where";
				}
				queryString += " country_id = " + countryId;
				flag = true;
			}

			if (regionId != null && (!(regionId.equals(new Long(-1))))) {
				if (!flag) {
					queryString += " where";
				} else {
					queryString += " and";
				}
				queryString += " region_id = " + regionId;
				flag = true;
			}

			if (zoneId != null && (!(zoneId.equals(new Long(-1))))) {
				if (!flag) {
					queryString += " where";
				} else {
					queryString += " and";
				}
				queryString += " zone_id = " + zoneId;
			}

			if (woredaId != null && (!(woredaId.equals(new Long(-1))))) {
				if (!flag) {
					queryString += " where";
				} else {
					queryString += " and";
				}
				queryString += " woreda_id = " + woredaId;
			}

			System.out.println("query is " + queryString);
			Query qry = session.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext())
				loc = (AmpLocation) itr.next();

		} catch (Exception e) {
			logger.error("Uanble to get location :" + e);
		} finally {

			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed " + ex);
			}
		}
		return loc;
	}

	
	public static List getAmpLocations(Long id) {
		AmpLocation ampLocation = null;
		ArrayList ampLocations = new ArrayList();
		Session session = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getSession();

			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a " + "where (a.ampActivityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			iter = qry.list().iterator();
			AmpActivity ampActivity = null;
			while (iter.hasNext()) {
				ampActivity = (AmpActivity) iter.next();
			}
			// end
			logger.debug("Activity: " + ampActivity.getAmpActivityId());
			iter = ampActivity.getLocations().iterator();
			while (iter.hasNext()) {
				ampLocation = (AmpLocation) iter.next();
				AmpLocations location = new AmpLocations();
				location.setCountry(ampLocation.getCountry());
				logger.debug("Country: " + location.getCountry());
				if (ampLocation.getAmpRegion() != null)
					location.setRegion(ampLocation.getAmpRegion().getName());
				logger.debug("Region: " + location.getRegion());
				if (ampLocation.getAmpZone() != null)
					location.setZone(ampLocation.getAmpZone().getName());
				logger.debug("Zone: " + location.getZone());
				if (ampLocation.getAmpWoreda() != null)
					location.setWoreda(ampLocation.getAmpWoreda().getName());
				logger.debug("Woreda: " + location.getWoreda());
				ampLocations.add(location);
			}
		} catch (Exception ex) {
			logger.error("Unable to get amp locations :" + ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return ampLocations;
	}

	public static Collection getAllLocations(Long id) {
		AmpLocation ampLocation = null;
		ArrayList ampLocations = new ArrayList();
		Session session = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getSession();

			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
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

			iter = ampActivity.getLocations().iterator();
			while (iter.hasNext()) {
				ampLocation = (AmpLocation) iter.next();
				ampLocations.add(ampLocation);
			}
		} catch (Exception ex) {
			logger.error("Unable to get amp locations :" + ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return ampLocations;
	}

	public static AmpLocation getAmpLocation(Long id) {
		AmpLocation ampLocation = null;
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			// modified by Priyajith
			// Desc: removed the usage of session.load and
			// used the select query
			// start
			String queryString = "select l from " + AmpLocation.class.getName()
					+ " l " + "where (l.ampLocationId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				ampLocation = (AmpLocation) itr.next();
			}
			// end
		} catch (Exception ex) {
			logger.debug("Unable to get amp locations " + ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return ampLocation;
	}

	public static AmpRegion getAmpRegion(Long id) {
		Session session = null;
		AmpRegion region = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from " + AmpRegion.class.getName()
					+ " r " + "where (r.ampRegionId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				region = (AmpRegion) itr.next();
			}

		} catch (Exception e) {
			logger.debug("Exception from getAmpRegion()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return region;
	}

	public static AmpZone getAmpZone(Long id) {
		Session session = null;
		AmpZone zone = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select z from " + AmpZone.class.getName()
					+ " z " + "where (z.ampZoneId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				zone = (AmpZone) itr.next();
			}

		} catch (Exception e) {
			logger.debug("Exception from getAmpZone()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return zone;
	}

	public static AmpWoreda getAmpWoreda(Long id) {
		Session session = null;
		AmpWoreda woreda = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select w from " + AmpWoreda.class.getName()
					+ " w " + "where (w.ampWoredaId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				woreda = (AmpWoreda) itr.next();
			}

		} catch (Exception e) {
			logger.debug("Exception from getAmpZone()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return woreda;
	}

	public static Collection getAllRegionsUnderCountry(String iso) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select reg from " + AmpRegion.class.getName()
					+ " reg " + "where country_id = '" + iso
					+ "'  order by reg.name";

			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllRegionsUnderCountry()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllZonesUnderRegion(Long id) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select zon from " + AmpZone.class.getName()
					+ " zon " + "where region_id = " + id
					+ " order by zon.name";

			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllZonesUnderRegion()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllWoredasUnderZone(Long id) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select wor from " + AmpWoreda.class.getName()
					+ " wor " + "where zone_id = " + id + " order by wor.name";

			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllWoredasUnderZone()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static ArrayList getAmpLocations() {
		Session session = null;
		Query q = null;
		AmpRegion ampRegion = null;
		ArrayList region = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select l from " + AmpRegion.class.getName()
					+ " l order by l.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampRegion = (AmpRegion) iter.next();
				region.add(ampRegion);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp location names  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return region;
	}

}