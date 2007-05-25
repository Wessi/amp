/**
 * 
 */
package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.EUActivity;

/**
 * @author mihai
 * 
 */
public final class EUActivityUtil {
	private static Logger logger = Logger.getLogger(EUActivityUtil.class);

	public static Collection getEUActivities(Long actId) {
		Session session = null;
		Collection euActivities = new ArrayList();

		try {
			session = PersistenceManager.getSession() ;
			String queryString = "select eu from " + EUActivity.class.getName() +
 			 " eu " +  "where (eu.activity.ampActivityId=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				EUActivity act = (EUActivity) itr.next();
				euActivities.add(act);
			}
		} catch(Exception ex) {
			logger.error("Unable to get EUActivities for activityid="+actId +" "+ ex);
			ex.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return euActivities;
	}
	
	public static Double getTotalCostConverted(Collection euActivities,Long desktopCurrencyId) {
		double ret=0;
		Iterator i = euActivities.iterator();
		while (i.hasNext()) {
			EUActivity element = (EUActivity) i.next();
			element.setDesktopCurrencyId(desktopCurrencyId);
			ret+=element.getTotalCostConverted();
		}
		return new Double(ret);		
	}
	
	public static Double getTotalContributionsConverted(Collection euActivities,Long desktopCurrencyId) {
		double ret=0;
		Iterator i = euActivities.iterator();
		while (i.hasNext()) {
			EUActivity element = (EUActivity) i.next();
			element.setDesktopCurrencyId(desktopCurrencyId);
			ret+=element.getTotalContributionsConverted();
		}
		return new Double(ret);		
	}
	
}
