package org.digijava.module.gis.util;

import java.util.Iterator;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.gis.dbentity.GisMap;
import java.util.List;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author George Kvizhinadze
 * @version 1.0
 */
public class DbUtil {
    private static Logger logger = Logger.getLogger(DbUtil.class);

    public DbUtil() {
    }

    public static void saveMap(GisMap map) throws DgException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.save(map);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            logger.debug("Unable to save the map", ex);
            throw new DgException(
                    "Unable to save the map", ex);
        } finally {
            if (sess != null) {
                try {
                    PersistenceManager.releaseSession(sess);
                } catch (Exception ex1) {
                    logger.warn("releaseSession() failed", ex1);
                }
            }
        }
    }

    public static GisMap getMap(long id) {
        Session session = null;
        GisMap gisMap = null;
        try {
            session = PersistenceManager.getSession();
            gisMap = (GisMap) session.load(
                    GisMap.class,
                    new Long(id));
        } catch (Exception ex) {
            String gg = null;
        } finally {
            try {
                PersistenceManager.releaseSession(session);
            } catch (Exception ex2) {
            }
        }
        return gisMap;
    }

    public static GisMap getMapByCode(String code) {
        Session session = null;
        GisMap map = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from rs in class " +
                                          GisMap.class.getName() +
                                          " where (rs.mapCode=:mapCode)");
            q.setParameter("mapCode", code, Hibernate.STRING);
            Iterator it = q.iterate();
            if (it.hasNext()) {
                map = (GisMap) it.next();
            } else {
                logger.debug("Unable to get map from DB");
            }
        } catch (Exception ex) {
            logger.debug("Unable to get map from DB", ex);
        }
        return map;

    }

    //To be moved to aid module later
    public static List getUsedSectors() {
        List retVal = null;
        Session session = null;
        GisMap map = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("select sec.sectorId, count(*) from sec in class " +
                                          AmpActivitySector.class.getName() +
                                          " group by sec.sectorId");
            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get map from DB", ex);
        }
        return retVal;
    }


    public static List getSectorFoundings(Long sectorId) {
        List retVal = null;
        Session session = null;
        GisMap map = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("select sec.activityId, sec.sectorPercentage from sec in class " +
                                          AmpActivitySector.class.getName() +
                                          " where sec.sectorId=:sectorId");
            q.setParameter("sectorId", sectorId, Hibernate.LONG);
            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get map from DB", ex);
        }
        return retVal;
    }


    public static Double getActivityFoundings(Long activityId) {
        Double retVal = null;
        Session session = null;
        GisMap map = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            /*
            Query q = session.createQuery("select sec.activityId, sec.sectorPercentage from sec in class " +
                                          AmpActivitySector.class.getName() +
                                          " where sec.sectorId=:sectorId");
            */
           Query q = session.createQuery("select sum(fd.transactionAmount) from fd in class " +
                                          AmpFundingDetail.class.getName() +
                                          " where fd.ampFundingId.ampActivityId.ampActivityId=:activityId");

            q.setParameter("activityId", activityId, Hibernate.LONG);
            List tmpLst = q.list();
            if (!tmpLst.isEmpty())
            retVal = (Double)tmpLst.get(0);
        } catch (Exception ex) {
            logger.debug("Unable to get map from DB", ex);
        }
        return retVal;
    }

    public static Double getTotalActivityFoundings(String ActIdWhereclause) {
            Double retVal = null;
            Session session = null;
            GisMap map = null;
            try {
                session = PersistenceManager.getRequestDBSession();
                /*
                Query q = session.createQuery("select sec.activityId, sec.sectorPercentage from sec in class " +
                                              AmpActivitySector.class.getName() +
                                              " where sec.sectorId=:sectorId");
                */
               Query q = session.createQuery("select sum(fd.transactionAmount) from fd in class " +
                                              AmpFundingDetail.class.getName() +
                                              " where fd.ampFundingId.ampActivityId.ampActivityId in ("+
                                              ActIdWhereclause + ")");

                //q.setParameter("activityId", activityId, Hibernate.LONG);
                List tmpLst = q.list();
                if (!tmpLst.isEmpty())
                retVal = (Double)tmpLst.get(0);
            } catch (Exception ex) {
                logger.debug("Unable to get map from DB", ex);
            }
            return retVal;
    }


}
