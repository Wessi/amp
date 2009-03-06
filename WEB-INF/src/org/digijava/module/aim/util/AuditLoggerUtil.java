/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005
 */

package org.digijava.module.aim.util;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
/**
 * ActivityUtil is the persister class for all activity related
 * entities
 *
 * @author Priyajith
 */
public class AuditLoggerUtil {

	private static Logger logger = Logger.getLogger(AuditLoggerUtil.class);

	public static void logObject(HttpSession hsession,HttpServletRequest request,
			LoggerIdentifiable o, String action) {

		Session session = null;
		Transaction tx = null;
		TeamMember tm = (TeamMember) hsession.getAttribute(Constants.CURRENT_MEMBER);
		String objId;
		objId = o.getIdentifier().toString();
		String objType = (String) o.getObjectType();
		String browser=request.getHeader("user-agent");
		try {
			session = PersistenceManager.getSession();

			tx = session.beginTransaction();
			AmpAuditLogger aal = new AmpAuditLogger();
			long time = System.currentTimeMillis();
			Timestamp ts = new Timestamp(time);
			if ("update".compareTo(action) == 0) {
				Collection<AmpAuditLogger> col = getAudits(session, objId, objType);
				if (col != null && col.size() == 1) {
					AmpAuditLogger existentLoggerObj = (AmpAuditLogger) col.iterator().next();
					aal.setAuthorEmail(existentLoggerObj.getAuthorEmail());
					aal.setAuthorName(existentLoggerObj.getAuthorName());
					aal.setLoggedDate(existentLoggerObj.getLoggedDate());
				}
			} else {
				aal.setAuthorName(tm.getMemberName());
				aal.setAuthorEmail(tm.getEmail());
				aal.setLoggedDate(ts);
			}


			aal.setEditorEmail(tm.getEmail());
			aal.setEditorName(tm.getMemberName());
			aal.setAction(action);
			aal.setModifyDate(ts);
			aal.setBrowser(browser);
			aal.setIp(request.getRemoteAddr());
			aal.setObjectId((String) o.getIdentifier().toString());
			aal.setObjectType((String) o.getObjectType());
			aal.setTeamName(tm.getTeamName());
			aal.setObjectName(o.getObjectName());
			
			session.save(aal);
			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : ", ex);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :", rsf);
				}
			}
		}

		return;
	}

	private static Collection<AmpAuditLogger> getAudits(Session session,
			String objId, String objType) {
		String qryStr;
		Query qry;
		try {
			String addAction = "add";
			qryStr = "select f from "
				+ AmpAuditLogger.class.getName()
				+ " f where f.objectType=:objectType and f.action=:actionObj and f.objectId=:objectId ";
			qry = session.createQuery(qryStr);
			qry.setParameter("objectType", objType.toString(),
					Hibernate.STRING);
			qry.setParameter("actionObj", addAction.toString(),
					Hibernate.STRING);
			qry.setParameter("objectId", objId.toString(),
					Hibernate.STRING);
			return qry.list();
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
		return null;
	}

	public static void logActivityUpdate(HttpSession hsession,HttpServletRequest request, AmpActivity activity, List<String> details){
		Session session = null;
		Transaction tx = null;
		TeamMember tm = (TeamMember) hsession.getAttribute(Constants.CURRENT_MEMBER);
		String objId;
		objId = activity.getIdentifier().toString();
		String objType = (String) activity.getObjectType();
		String browser=request.getHeader("user-agent");
		try {
			session = PersistenceManager.getSession();

			tx = session.beginTransaction();			
			long time = System.currentTimeMillis();
			Timestamp ts = new Timestamp(time);			
			AmpAuditLogger existentLoggerObj = null;
				
			Collection<AmpAuditLogger> col = getAudits(session, objId, objType);
			if (col != null && col.size() == 1) {
				existentLoggerObj = (AmpAuditLogger) col
						.iterator().next();
			}

			Iterator<String> it = details.iterator();
			while(it.hasNext()){
				AmpAuditLogger aal = new AmpAuditLogger();
				if(existentLoggerObj!=null){
					aal.setAuthorEmail(existentLoggerObj.getAuthorEmail());
					aal.setAuthorName(existentLoggerObj.getAuthorName());
					aal.setLoggedDate(existentLoggerObj.getLoggedDate());
				} else{
					aal.setAuthorName(tm.getMemberName());
					aal.setAuthorEmail(tm.getEmail());
					aal.setLoggedDate(ts);
				}
				String message = it.next();
				aal.setEditorEmail(tm.getEmail());
				aal.setEditorName(tm.getMemberName());
				aal.setAction("update");
				aal.setModifyDate(ts);
				aal.setBrowser(browser);
				aal.setIp(request.getRemoteAddr());
				aal.setObjectId((String) activity.getIdentifier().toString());
				aal.setObjectType((String) activity.getObjectType());
				aal.setTeamName(tm.getTeamName());
				aal.setObjectName(activity.getObjectName());
				aal.setDetail(message);
				session.save(aal);				
			}

			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : ", ex);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :", rsf);
				}
			}
		}			
	}
	
	/**
	 * @author dan
	 */
	public static Collection getLogObjects() {
		Session session = null;
		Collection<AmpAuditLogger> col = new ArrayList<AmpAuditLogger>();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + AmpAuditLogger.class.getName() + " f";
			qry = session.createQuery(qryStr);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}
	/**
	 * 
	 * @return
	 */
	public static Collection getTeamLogObjects(String teamname) {
		Session session = null;
		Collection<AmpAuditLogger> col = new ArrayList<AmpAuditLogger>();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " 
				+ AmpAuditLogger.class.getName() 
				+ " f where f.teamName like '" + teamname + "'";
			qry = session.createQuery(qryStr);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}

	/**
	 * @author dan
	 */
	public static Object loadObject(String idObj, String className) {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		Long id=new Long(Long.parseLong(idObj));
		Object o=null;
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + className + " f where f.id="+id;
			if(className.contains("AmpReports")) {
				qryStr = "select f from " + className + " f where f.ampReportId="+id;
			}
			qry = session.createQuery(qryStr);
			col = qry.list();
			o=col.iterator().next();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return o;
	}

	public static List<AmpAuditLogger> getActivityLogObjects(String activityId) {
		Session session = null;
		List<AmpAuditLogger> col = new ArrayList<AmpAuditLogger>();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			
			qryStr = "select f from "
					+ AmpAuditLogger.class.getName()
					+ " f where f.objectType=:objectType and f.objectId=:objectId order by f.modifyDate desc";
			qry = session.createQuery(qryStr);
			qry.setParameter("objectType", AmpActivity.class.getCanonicalName(), Hibernate.STRING);
			qry.setParameter("objectId", activityId, Hibernate.STRING);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}

	public static List<String> generateLogs(AmpActivity activity,
			Long activityId) {
		List<String> auditTrail = new ArrayList<String>();
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivity oldActivity = (AmpActivity) session.load(
					AmpActivity.class, activityId);
			if (oldActivity.getName() != null
					&& !oldActivity.getName().equals(activity.getName())) {
				auditTrail.add("Name changed");
			}
			if(auditTrail.isEmpty()){
				auditTrail.add("Updated");
			}
		} catch (HibernateException e) {
			logger.error("HibernateException", e);
		} catch (SQLException e) {
			logger.error("SQLException", e);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed.", rsf);
				}
			}
		}
		return auditTrail;

	} 
	
	private static String getDateRange(int interval) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, -interval);
		//Logs don't take in account global setting format
		SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd");
        return fd.format(cal.getTime());
    }
	/**
	 * @author Diego Dimunzio
	 * @param interval
	 * @return
	 */
	public static Collection getLogByPeriod(int interval) {
		Session session = null;
		Collection<AmpAuditLogger> col = new ArrayList<AmpAuditLogger>();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + 
				AmpAuditLogger.class.getName() 
				+ " f where f.loggedDate < '" + getDateRange(interval) + "'";
			qry = session.createQuery(qryStr);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}
	
	
	/**
	 * @author Diego Dimunzio
	 * Delete all records whose date is less than the interval
	 * @param interval
	*/
	public static void DeleteLogsByPeriod(String interval){
		Session session = null;
		String qryStr = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from "
				+ AmpAuditLogger.class.getName()
				+ " f where f.loggedDate < '" + getDateRange(Integer.parseInt(interval)) + "'";
			tx = session.beginTransaction();
			session.delete(qryStr);
			tx.commit();
		
		} catch (HibernateException e) {
			logger.error("HibernateException", e);
		} catch (SQLException e) {
			logger.error("SQLException", e);
	}
}
	/**
     * This class is used for sorting by name.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerNameComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerNameComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getObjectName()==null || o2.getObjectName()==null)?0:collator.compare(o1.getObjectName().toLowerCase(), o2.getObjectName().toLowerCase());
            return result;
        }
    }
	
    /**
     * This class is used for sorting by Object type.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerTypeComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerTypeComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerTypeComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getObjectTypeTrimmed()==null || o2.getObjectName()==null)?0:collator.compare(o1.getObjectTypeTrimmed().toLowerCase(), o2.getObjectTypeTrimmed().toLowerCase());
            return result;
        }
    }
    
    /**
     * This class is used for sorting by Team Name.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerTeamComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerTeamComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerTeamComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getTeamName()==null || o2.getTeamName()==null)?0:collator.compare(o1.getTeamName().toLowerCase(), o2.getTeamName().toLowerCase());
            return result;
        }
    }
    
    /**
     * This class is used for sorting by Author Name.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerAuthorComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerAuthorComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerAuthorComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getAuthorName()==null || o2.getAuthorName()==null)?0:collator.compare(o1.getAuthorName().toLowerCase(), o2.getAuthorName().toLowerCase());
            return result;
        }
    }
    
    /**
     * This class is used for sorting by  Creation Date.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerCreationDateComparator implements Comparator<AmpAuditLogger> {
    	public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            int result = (o1.getLoggedDate()==null || o2.getLoggedDate()==null)?0:o1.getLoggedDate().compareTo(o2.getLoggedDate());
            return result;
        }
    }
    
    /**
     * This class is used for sorting by  Change Date.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerChangeDateComparator implements Comparator<AmpAuditLogger> {
    	   public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            int result = (o1.getModifyDate()==null || o2.getModifyDate()==null)?0:o1.getModifyDate().compareTo(o2.getModifyDate());
            return result;
        }
    }
    
    /**
     * This class is used for sorting by Editor Name.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerEditorNameComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerEditorNameComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerEditorNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getEditorName()==null || o2.getEditorName()==null)?0:collator.compare(o1.getEditorName(), o2.getEditorName());
            return result;
        }
    }
    /**
     * This class is used for sorting by Action.
     * @author Diego Dimunzio
     *
     */
    public static class HelperAuditloggerActionComparator implements Comparator<AmpAuditLogger> {
    	Locale locale;
        Collator collator;

        public HelperAuditloggerActionComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAuditloggerActionComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpAuditLogger o1, AmpAuditLogger o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getAction()==null || o2.getAction()==null)?0:collator.compare(o1.getAction(), o2.getAction());
            return result;
        }
    }
} 
