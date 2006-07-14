package org.digijava.module.aim.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;

import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMECurrValHistory;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.helper.AmpMEIndicatorList;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.PriorCurrentValues;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.MEIndicatorRisk;
import org.digijava.module.aim.helper.MEIndicatorValue;

public class MEIndicatorsUtil 
{
	private static Logger logger = Logger.getLogger(MEIndicatorsUtil.class);
	public static Collection getAllIndicators() 
	{
		Session session = null;
		Query qry = null;
		Collection indicators = new ArrayList();
		Collection col;
		AmpMEIndicatorList indList = null;

		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select i from " + AmpMEIndicators.class.getName() + " i";
			qry = session.createQuery(queryString);
			col = qry.list();
			Iterator itr = col.iterator();
			while(itr.hasNext())
			{
				indList = new AmpMEIndicatorList();
				AmpMEIndicators meindicators = (AmpMEIndicators) itr.next();
				indList.setAmpMEIndId(meindicators.getAmpMEIndId());
				indList.setName(meindicators.getName());
				indList.setCode(meindicators.getCode());
				indList.setDescription(meindicators.getDescription());
				indList.setDefaultInd(meindicators.isDefaultInd());
				indList.setIndicatorValues(null);
				indicators.add(indList);
			}
		} catch (Exception e) {
			logger.error("Unable to get indicators");
			logger.debug("Exception : " + e);
		} finally {
			try 
			{
				if (session != null) 
				{
					PersistenceManager.releaseSession(session);
				}
			} 
			catch (Exception ex) 
			{
				logger.debug("releaseSession() FAILED", ex);
			}
		}
		return indicators;
	}
	
	public static Collection getAllDefaultIndicators()
	{
		Session session = null;
		Collection col = new ArrayList();
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select defInd from "
								+ AmpMEIndicators.class.getName()
								+ " defInd where defInd.defaultInd = 1";
			qry = session.createQuery(queryString);
			col = qry.list();
		}	
		catch(Exception e)
		{
			logger.error("Unable to get non-default indicators");
			logger.debug("Exception : " + e);
		}
		finally
		{
			try 
			{
				if (session != null) 
				{
					PersistenceManager.releaseSession(session);
				}
			} 
			catch (Exception ex) 
			{
				logger.debug("releaseSession() FAILED", ex);
			}
		}
		return col;
	}
	
	public static Collection getAllNonDefaultIndicators()
	{
		Session session = null;
		Collection col = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select nondefInd from "
								+ AmpMEIndicators.class.getName()
								+ " nondefInd where nondefInd.defaultInd = 0";
			qry = session.createQuery(queryString);
			col = qry.list();
		}	
		catch(Exception e)
		{
			logger.error("Unable to get non-default indicators");
			logger.debug("Exception : " + e);
		}
		finally
		{
			try 
			{
				if (session != null) 
				{
					PersistenceManager.releaseSession(session);
				}
			} 
			catch (Exception ex) 
			{
				logger.debug("releaseSession() FAILED", ex);
			}
		}
		return col;
	}
	
	public static Collection getMeIndValIds(Long meIndicatorId)
	{
		Session session = null;
		Collection col = null;
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select ampMeIndValId from "
				+ AmpMEIndicatorValue.class.getName() 
				+ " ampMeIndValId where (ampMeIndValId.meIndicatorId=:meIndicatorId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("meIndicatorId",meIndicatorId,Hibernate.LONG);
			col = qry.list();
		}
		catch(Exception exp)
		{
			logger.debug("UNABLE to find meIndicatorValueIds for given meIndicatorId.", exp);			
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception e)
			{
				logger.debug("releaseSession() FAILED", e);
			}
		}
		return col;
	}

	public static Collection getMeCurrValIds(Long meIndValue)
	{
		Session session = null;
		Collection col = null;
		
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select ampMECurrValHistoryId from "
								+AmpMECurrValHistory.class.getName()
								+" ampMECurrValHistoryId where (ampMECurrValHistoryId.meIndValue=:meIndValue)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("meIndValue",meIndValue,Hibernate.LONG);
			col = qry.list();
		}
		catch(Exception e1)
		{
			logger.debug("UNABLE to find meIndicatorCurrValIds for given meIndValId", e1);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception e2)
			{
				logger.debug("releaseSession() FAILED", e2);
			}
		}
		return col;
	}
	
	public static AmpMEIndicators findIndicatorId(String name,String code)
	{
		Session session = null;
		Query qry = null;
		Collection col = null;
		AmpMEIndicators meind = null;
		
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select ind from "
								+ AmpMEIndicators.class.getName() + " ind " 
								+ "where (name like '" + name + "' && code like '" + code + "')";
			qry = session.createQuery(queryString);
			col = qry.list();
			Iterator itr = col.iterator();
			while(itr.hasNext())
				meind = (AmpMEIndicators) itr.next();
		}
		catch(Exception e)
		{
			logger.debug("UNABLE to find Indicators with the name,code.", e);
		}
		finally
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			} 
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() FAILED", ex2);
			}
		}
		return meind;
	}
	
	public static boolean checkDuplicateNameCode(String name,String code,Long id) {
		Session session = null;
		Query qry = null;
		boolean duplicatesExist = false;
		String queryString = null;
		
		try
		{
			session = PersistenceManager.getSession();
			if (id != null && id.longValue() > 0) {
				queryString = "select count(*) from "
					+ AmpMEIndicators.class.getName() + " meind " 
					+ "where (name like '" + name + "'"
					+ " or code like '" + code + "') and " +
							"(meind.ampMEIndId !=:id)" ;
				qry = session.createQuery(queryString);
				qry.setParameter("id",id,Hibernate.LONG);
			} else {
				queryString = "select count(*) from " 
					+ AmpMEIndicators.class.getName() + " meind " 
					+ "where name like '" + name + "'"
					+ " or code like '" + code + "'" ;
				qry = session.createQuery(queryString);								
			}
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				Integer cnt = (Integer) itr.next();
				if (cnt.intValue() > 0)
					duplicatesExist = true;
			}
		}
		catch (Exception ex) {
			logger.error("UNABLE to find Indicators with duplicate name.", ex);
			ex.printStackTrace(System.out);
		} 
		finally {
			try {
				PersistenceManager.releaseSession(session);
			} 
			catch (Exception ex2) {
				logger.debug("releaseSession() FAILED", ex2);
			}
		}
		return duplicatesExist;
	}

	public static Collection getActivityList()
	{
		Session session = null;
		Collection col = null;

		try
		{
			session = PersistenceManager.getSession();
	
			String queryString = "select ampActivityId from "
								+ AmpActivity.class.getName() + " ampActivityId";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.debug("UNABLE to find activity ids from AmpActivity.");
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception exp)
			{
				logger.debug("releaseSession() FAILED", exp);
			}
		}
		return col;
	}
	
	public static Collection searchForIndicators(String keyword) 
	{
		Session session = null;
		Collection col = null;
		Query qry = null;
		
		try 
		{
			session = PersistenceManager.getSession();
			
			String queryString = "select ind from "
					+ AmpMEIndicators.class.getName() + " ind "
					+ "where (ind.name like '%" + keyword + "%' && ind.defaultInd=false)";
			
			qry = session.createQuery(queryString);
			col = qry.list();
		} 
		catch (Exception ex)
		{
			logger.debug("Unable to SEARCH for Indicators", ex);
		} 
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return col;
	}
	
	public static Collection getActivityIndicatorsList(Long actId) 
	{
		Session session = null;
		Collection col = new ArrayList();
		try 
		{
			session = PersistenceManager.getSession();
			String qryStr = "select indVal from " + AmpMEIndicatorValue.class.getName() + "" +
					" indVal where (indVal.activityId=:actId)" ;
			Query qry = session.createQuery(qryStr);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) 
			{
				AmpMEIndicatorValue meIndValue = (AmpMEIndicatorValue) itr.next();
				ActivityIndicator actInd = new ActivityIndicator();
				actInd.setIndicatorId(meIndValue.getMeIndicatorId().getAmpMEIndId());
				col.add(actInd);
			}
		} 
		catch (Exception e) 
		{
			logger.error("Exception from getActivityIndicatorsList() :" + e.getMessage());
			e.printStackTrace(System.out);
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
		return col;
	}

	public static Collection getActivityIndicators(Long actId) 
	{
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			if (actId != null) {
				qryStr = "select indVal from " + AmpMEIndicatorValue.class.getName() + " " +
						"indVal where (indVal.activityId=:actId)" ;
		
				qry = session.createQuery(qryStr);
				qry.setParameter("actId",actId,Hibernate.LONG);
				Iterator itr = qry.list().iterator();
				while (itr.hasNext()) {
					AmpMEIndicatorValue meIndValue = (AmpMEIndicatorValue) itr.next();
					ActivityIndicator actInd = new ActivityIndicator();
					actInd.setIndicatorName(meIndValue.getMeIndicatorId().getName());
					actInd.setIndicatorCode(meIndValue.getMeIndicatorId().getCode());
					actInd.setIndicatorId(meIndValue.getMeIndicatorId()
							.getAmpMEIndId());
					actInd.setIndicatorValId(meIndValue.getAmpMeIndValId());
					
					actInd.setBaseVal(meIndValue.getBaseVal());
					if (meIndValue.getBaseValDate() != null) {
						actInd.setBaseValDate(
								DateConversion.ConvertDateToString(
										meIndValue.getBaseValDate()));
					}

					actInd.setActualVal(meIndValue.getActualVal());
					if (meIndValue.getActualValDate() != null) {
						actInd.setActualValDate(DateConversion
								.ConvertDateToString(meIndValue
										.getActualValDate()));
					}

					actInd.setTargetVal(meIndValue.getTargetVal());
					if (meIndValue.getTargetValDate() != null) {
						actInd.setTargetValDate(DateConversion.ConvertDateToString(
								meIndValue.getTargetValDate()));						
					}					
					
					actInd.setRevisedTargetVal(meIndValue.getRevisedTargetVal());
					if (meIndValue.getRevisedTargetValDate() != null) {
						actInd.setRevisedTargetValDate(DateConversion.ConvertDateToString(
								meIndValue.getRevisedTargetValDate()));						
					}
					
					if (meIndValue.getRisk() != null)
						actInd.setRisk(meIndValue.getRisk()
								.getAmpIndRiskRatingsId());

					actInd.setComments(meIndValue.getComments());
					actInd.setDefaultInd(meIndValue.getMeIndicatorId()
							.isDefaultInd());
					
					actInd.setPriorValues(getPriorIndicatorValues(meIndValue.getAmpMeIndValId(),true));
					
					col.add(actInd);
				}				
			}
			// load all global indicators which doesnt have an 
			// entry in 'amp_me_indicator_value' table
			
			qryStr = "select meInd from " + AmpMEIndicators.class.getName() + " meInd " +
					"where meInd.defaultInd = true";
			qry = session.createQuery(qryStr);
			Iterator itr = qry.list().iterator();
			long t = System.currentTimeMillis();
			while (itr.hasNext()) {
				AmpMEIndicators meInd = (AmpMEIndicators) itr.next();
				ActivityIndicator actInd = new ActivityIndicator();
				actInd.setIndicatorId(meInd.getAmpMEIndId());
				if (!col.contains(actInd)) {
					actInd.setIndicatorName(meInd.getName());
					actInd.setIndicatorCode(meInd.getCode());
					actInd.setIndicatorValId(new Long(t*-1));
					actInd.setActivityId(actId);
					actInd.setDefaultInd(meInd.isDefaultInd());
					++t;
					col.add(actInd);
				}
			}
			
		} catch (Exception e) {
			logger.error("Exception from getActivityIndicators() :" + e.getMessage());
			e.printStackTrace(System.out);
		} finally { 
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
		return col;
	}
	
	public static Collection getIndicatorsForActivity(Long actId) 
	{
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		DecimalFormat df = new DecimalFormat("###.##%");
		
		try {
			session = PersistenceManager.getSession();
			if (actId != null) {
				qryStr = "select indVal from " + AmpMEIndicatorValue.class.getName() + " " +
						"indVal where (indVal.activityId=:actId)" ;
		
				qry = session.createQuery(qryStr);
				qry.setParameter("actId",actId,Hibernate.LONG);
				Iterator itr = qry.list().iterator();
				while (itr.hasNext()) {
					AmpMEIndicatorValue meIndValue = (AmpMEIndicatorValue) itr.next();
					ActivityIndicator actInd = new ActivityIndicator();
					actInd.setIndicatorName(meIndValue.getMeIndicatorId().getName());
					actInd.setIndicatorCode(meIndValue.getMeIndicatorId().getCode());
			
					actInd.setBaseVal(meIndValue.getBaseVal());
					if (meIndValue.getBaseValDate() != null) {
						actInd.setBaseValDate(
								DateConversion.ConvertDateToString(
										meIndValue.getBaseValDate()));
					}
					actInd.setIndicatorId(meIndValue.getMeIndicatorId()
							.getAmpMEIndId());
					actInd.setIndicatorValId(meIndValue.getAmpMeIndValId());
					
					actInd.setActualVal(meIndValue.getActualVal());
					if (meIndValue.getActualValDate() != null) {
						actInd.setActualValDate(DateConversion
								.ConvertDateToString(meIndValue
										.getActualValDate()));
					}

					actInd.setTargetVal(meIndValue.getTargetVal());
					if (meIndValue.getTargetValDate() != null) {
						actInd.setTargetValDate(DateConversion.ConvertDateToString(
								meIndValue.getTargetValDate()));						
					}					
					
					actInd.setRevisedTargetVal(meIndValue.getRevisedTargetVal());
					if (meIndValue.getRevisedTargetValDate() != null) {
						actInd.setRevisedTargetValDate(DateConversion.ConvertDateToString(
								meIndValue.getRevisedTargetValDate()));						
					}
					
					actInd.setPriorValues(getPriorIndicatorValues(meIndValue.getAmpMeIndValId(),false));
					actInd.setComments(meIndValue.getComments());
					actInd.setDefaultInd(meIndValue.getMeIndicatorId()
							.isDefaultInd());
					
					if (meIndValue.getRisk() != null) {
						if (meIndValue.getRisk() != null)
							actInd.setRiskName(meIndValue.getRisk()
									.getRatingName());
					}					
					
					float progress = 0;
					float baseVal = meIndValue.getBaseVal();
					float targetVal = meIndValue.getRevisedTargetVal();
					float actVal = meIndValue.getActualVal(); 
					
					if (targetVal == baseVal) {
						progress = 100;
					} else {
						progress = (actVal - baseVal)/(targetVal - baseVal);
						progress = (progress < 0) ? 0 : progress;
					}
					
					actInd.setProgress(df.format(progress));
					col.add(actInd);
				}				
			}
		} catch (Exception e) {
			logger.error("Exception from getActivityIndicators() :" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
		return col;
	}	
	
	public static void saveMEIndicatorValues(ActivityIndicator actInd, int chk) 
	{
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			AmpMEIndicatorValue meIndVal = null;
			
			if (actInd.getIndicatorValId() != null && 
					actInd.getIndicatorValId().longValue() > 0) {
				meIndVal = (AmpMEIndicatorValue) session.load(
						AmpMEIndicatorValue.class,actInd.getIndicatorValId());
			} else {
 				meIndVal = new AmpMEIndicatorValue();
				AmpActivity act = (AmpActivity) session.load(AmpActivity.class,
						actInd.getActivityId());
				meIndVal.setActivityId(act);
				AmpMEIndicators meInd = (AmpMEIndicators) session.load(
						AmpMEIndicators.class,actInd.getIndicatorId());
				meIndVal.setMeIndicatorId(meInd);

			}
			meIndVal.setBaseVal(actInd.getBaseVal());
			meIndVal.setBaseValDate(DateConversion.getDate(actInd.getBaseValDate()));
			
			meIndVal.setTargetVal(actInd.getTargetVal());
			meIndVal.setTargetValDate(DateConversion.getDate(actInd.getTargetValDate()));
			
			meIndVal.setRevisedTargetVal(actInd.getRevisedTargetVal());
			meIndVal.setRevisedTargetValDate(DateConversion.getDate(actInd.getRevisedTargetValDate()));

			meIndVal.setActualVal(actInd.getActualVal());
			meIndVal.setActualValDate(DateConversion.getDate(actInd.getActualValDate()));
			
			
			if(chk == 1) {
				AmpIndicatorRiskRatings indRisk = new AmpIndicatorRiskRatings();
				indRisk.setAmpIndRiskRatingsId(actInd.getRisk());
				meIndVal.setRisk(indRisk);
				meIndVal.setComments(actInd.getComments());
			}
			tx = session.beginTransaction();
			session.saveOrUpdate(meIndVal);
			tx.commit();
		} catch (Exception e) {
			logger.error("Exception from saveMEIndicatorValues() :" + e.getMessage());
			e.printStackTrace(System.out);		
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception trbf) {
					logger.error("Transaction roll back failed ");
					e.printStackTrace(System.out);
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}			
		}
	}
	
	public static void deleteMEIndicatorValues(Long indValId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			AmpMEIndicatorValue meIndVal = (AmpMEIndicatorValue) session.load(
					AmpMEIndicatorValue.class,indValId);
			
			String qryStr = "select meCh from " + AmpMECurrValHistory.class.getName() + "" +
					" meCh where (meCh.meIndValue=:indVal)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("indVal",indValId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			tx = session.beginTransaction();
			while (itr.hasNext()) {
				AmpMECurrValHistory currValHist = (AmpMECurrValHistory) itr.next();
				session.delete(currValHist);
			}
			session.delete(meIndVal);
			tx.commit();
			
		} catch (Exception e) {
			logger.error("Exception from saveMEIndicatorValues() :" + e.getMessage());
			e.printStackTrace(System.out);		
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception trbf) {
					logger.error("Transaction roll back failed ");
					e.printStackTrace(System.out);
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}			
		}
	}	
	
	public static Collection getPortfolioMEIndicatorValues(Collection actIds,
			Long indId) {
		
		Session session = null;
		Collection col = new ArrayList();
	
		try {
			session = PersistenceManager.getSession();
			String qryStr = null;
			Iterator itr = null;
			
			if (actIds != null && actIds.size() > 0) {
				if (actIds.size() == 1) {
					itr = actIds.iterator();
					Long actId = (Long) itr.next();
					if (indId.longValue() > 0) {
						qryStr = "select mi.amp_me_indicator_id,iv.base_val,iv.actual_val,iv.revised_target_val," +
								"mi.name from amp_me_indicator_value iv inner join " +
								"amp_me_indicators mi on (iv.me_indicator_id=mi.amp_me_indicator_id)" +
								" where mi.default_ind = 1  and iv.activity_id=" + actId + " and " +
								"iv.me_indicator_id=" + indId + " order by iv.activity_id,mi.name";
					} else {
						qryStr = "select mi.amp_me_indicator_id,iv.base_val,iv.actual_val,iv.revised_target_val,mi.name " +
								"from amp_me_indicator_value iv inner join amp_me_indicators mi " +
								"on (iv.me_indicator_id=mi.amp_me_indicator_id) where mi.default_ind = 1 " +
								"and iv.activity_id=" + actId + " order by iv.activity_id,mi.name";
					}
				} else {
					itr = actIds.iterator();
					String params = "";
					while (itr.hasNext()) {
						Long actId = (Long) itr.next();
						if (params.length() > 0) params += ",";
						params += actId;
					}
					if (indId.longValue() > 0) {
						qryStr = "select mi.amp_me_indicator_id,iv.base_val,iv.actual_val,iv.revised_target_val,a.name from " +
								"amp_me_indicator_value iv inner join amp_me_indicators mi on " +
								"(iv.me_indicator_id=mi.amp_me_indicator_id) inner join amp_activity a on " +
								"(a.amp_activity_id=iv.activity_id) where mi.default_ind = 1  and " +
								"iv.activity_id in (" + params + ") and iv.me_indicator_id=" + indId + "" +
										" order by a.name";
					} else {
						qryStr = "select mi.amp_me_indicator_id,sum(iv.base_val),sum(iv.actual_val),sum(iv.revised_target_val)," +
								"mi.name from amp_me_indicator_value iv inner join amp_me_indicators mi on" +
								" (iv.me_indicator_id=mi.amp_me_indicator_id) where mi.default_ind = 1 and " +
								"iv.activity_id in ( " + params + ") group by iv.me_indicator_id " +
										"order by iv.activity_id";
					}					
				}
				
				Connection con = session.connection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(qryStr);
				while (rs.next()) {
					long id = rs.getLong(1);
					double baseVal = rs.getDouble(2);
					double actVal = rs.getDouble(3);
					double tarVal = rs.getDouble(4);
					String key = rs.getString(5);
					
					double totIndVal = baseVal + tarVal + actVal;
					
					MEIndicatorValue baseIndVal = new MEIndicatorValue();
					baseIndVal.setIndId(new Long(id));
					baseIndVal.setIndicatorName(key);
					baseIndVal.setType(Constants.ME_IND_VAL_BASE_ID);
					if (totIndVal > 0) { 
						baseIndVal.setValue(baseVal * (100 / totIndVal));
					} else { 
						baseIndVal.setValue(0);
					}
					
					MEIndicatorValue actIndVal = new MEIndicatorValue();
					actIndVal.setIndId(new Long(id));
					actIndVal.setIndicatorName(key);
					actIndVal.setType(Constants.ME_IND_VAL_ACTUAL_ID);
					if (totIndVal > 0) {
						actIndVal.setValue(actVal * (100 / totIndVal));	
					} else {
						actIndVal.setValue(0);
					}
					
					MEIndicatorValue targetIndVal = new MEIndicatorValue();
					targetIndVal.setIndId(new Long(id));
					targetIndVal.setIndicatorName(key);
					targetIndVal.setType(Constants.ME_IND_VAL_TARGET_ID);
					if (totIndVal > 0) { 
						targetIndVal.setValue(tarVal * (100 / totIndVal));
					} else { 
						targetIndVal.setValue(0);
					}
					
					
					col.add(baseIndVal);
					col.add(actIndVal);
					col.add(targetIndVal);
				}
			}
		} catch (Exception e) {
			logger.error("Exception from getPortfolioMEIndicatorValues() :" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
		return col;		
	}
	
	public static Collection getMEIndicatorValues(Long actId) {
		Session session = null;
		Collection col = new ArrayList();
	
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select indVal from " + AmpMEIndicatorValue.class.getName() + "" +
					" indVal where (indVal.activityId=:actId)" ;
			Query qry = session.createQuery(qryStr);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				
				AmpMEIndicatorValue meIndValue = (AmpMEIndicatorValue) itr.next();
				AmpMEIndicators meInd = meIndValue.getMeIndicatorId();
				
				double totIndVal = meIndValue.getBaseVal() + meIndValue.getRevisedTargetVal() + meIndValue.getActualVal();
				
				MEIndicatorValue baseIndVal = new MEIndicatorValue();
				baseIndVal.setIndId(meInd.getAmpMEIndId());
				baseIndVal.setIndicatorName(meInd.getName());
				baseIndVal.setType(Constants.ME_IND_VAL_BASE_ID);
				if (totIndVal > 0) { 
					baseIndVal.setValue(meIndValue.getBaseVal() * (100 / totIndVal));
				} else { 
					baseIndVal.setValue(0);
				}
				
				MEIndicatorValue actIndVal = new MEIndicatorValue();
				actIndVal.setIndId(meInd.getAmpMEIndId());
				actIndVal.setIndicatorName(meInd.getName());
				actIndVal.setType(Constants.ME_IND_VAL_ACTUAL_ID);
				if (totIndVal > 0) {
					actIndVal.setValue(meIndValue.getActualVal() * (100 / totIndVal));	
				} else {
					actIndVal.setValue(0);
				}
				
				MEIndicatorValue targetIndVal = new MEIndicatorValue();
				targetIndVal.setIndId(meInd.getAmpMEIndId());
				targetIndVal.setIndicatorName(meInd.getName());
				targetIndVal.setType(Constants.ME_IND_VAL_TARGET_ID);
				if (totIndVal > 0) { 
					targetIndVal.setValue(meIndValue.getRevisedTargetVal() * (100 / totIndVal));
				} else { 
					targetIndVal.setValue(0);
				}
				
				col.add(baseIndVal);
				col.add(actIndVal);
				col.add(targetIndVal);
				
			}
			
		} catch (Exception e) {
			logger.error("Exception from getMEIndicatorValues() :" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
		return col;		
	}
	
	public static Collection getPortfolioMEIndicatorRisks(Collection actIds) {
		Session session = null;
		ArrayList col = new ArrayList();
		String qryStr = null;
		Iterator itr = null;
		Query qry = null;
		Map riskMap = new HashMap();
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select r from " + AmpIndicatorRiskRatings.class.getName() + " r";
			qry = session.createQuery(qryStr);
			itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpIndicatorRiskRatings r = (AmpIndicatorRiskRatings) itr.next();
				riskMap.put(new Integer(r.getRatingValue()),r.getRatingName());
			}
			
			if (actIds != null && actIds.size() > 0) {
				itr = actIds.iterator();
				String name;
				while (itr.hasNext()) {
					Long id = (Long) itr.next();
					int value = getOverallRisk(id);
					Integer key = new Integer(value);
					
					if (riskMap.containsKey(key)) {
						name = (String) riskMap.get(new Integer(value));
						MEIndicatorRisk meRisk = new MEIndicatorRisk();
						meRisk.setRisk(name);
						int index = -1;
						if (col != null && col.size() > 0) {
							index = col.indexOf(meRisk);
						}
						 
						if (index >= 0) {
							meRisk = (MEIndicatorRisk) col.get(index);
							meRisk.setRiskCount(meRisk.getRiskCount()+1);
						} else {
							meRisk.setRiskCount(1);
							meRisk.setRiskRating((byte) value);
							col.add(meRisk);											
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception from getPortfolioMEIndicatorRisk() :" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
		return col;				
	}
	
	public static Collection getAllIndicatorRisks()
	{
		Session session = null;
		Query qry = null;
		Collection col = null;
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select r from " + AmpIndicatorRiskRatings.class.getName() + " r";
			qry = session.createQuery(queryString);
			col = qry.list();
		} 
		catch (Exception e) 
		{
			logger.error("Unable to get the risk ratings");
			logger.debug("Exception : " + e);
		} 
		finally 
		{
			try 
			{
				if (session != null) 
				{
					PersistenceManager.releaseSession(session);
				}
			} 
			catch (Exception ex) 
			{
				logger.debug("releaseSession() FAILED", ex);
			}
		}
		return col;
	}

	public static Collection getMEIndicatorRisks(Long actId) {
		Session session = null;
		Collection col = new ArrayList();
	
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select count(*), indVal.risk from " + AmpMEIndicatorValue.class.getName() + "" +
					" indVal where (indVal.activityId=:actId)" +
							" group by indVal.risk";

			Query qry = session.createQuery(qryStr);

			qry.setParameter("actId",actId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				Integer riskCount = (Integer) obj[0];
				AmpIndicatorRiskRatings riskRating = (AmpIndicatorRiskRatings) obj[1];
				MEIndicatorRisk meRisk = new MEIndicatorRisk();
				meRisk.setRisk(riskRating.getRatingName());
				meRisk.setRiskCount(riskCount.intValue());
				meRisk.setRiskRating((byte) riskRating.getRatingValue());
				col.add(meRisk);
			}
			
		} catch (Exception e) {
			logger.error("Exception from getMEIndicatorRisks() :" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
		return col;				
	}	
	
	public static void updateMEIndicator(AmpMEIndicators newIndicator) {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(newIndicator);
			tx.commit();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed.");
				}
			}
		}
 	}
	
	public static void saveMEIndicator(AmpMEIndicators newIndicator,Long actId,boolean defaultIndicator) {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = PersistenceManager.getSession();

			tx = session.beginTransaction();
			session.save(newIndicator);
			
			AmpActivity act = null;
			if (actId != null && actId.longValue() > 0) {
				act = (AmpActivity) session.load(AmpActivity.class,actId);	
				AmpMEIndicatorValue ampMEIndValnew = new AmpMEIndicatorValue();
				ampMEIndValnew.setActivityId(act);
				ampMEIndValnew.setMeIndicatorId(newIndicator);
				ampMEIndValnew.setBaseVal(0);
				ampMEIndValnew.setTargetVal(0);
				ampMEIndValnew.setActualVal(0);
				ampMEIndValnew.setRevisedTargetVal(0);				
				ampMEIndValnew.setBaseValDate(null);
				ampMEIndValnew.setTargetValDate(null);
				ampMEIndValnew.setActualValDate(null);
				ampMEIndValnew.setRevisedTargetValDate(null);
				ampMEIndValnew.setRisk(null);
				ampMEIndValnew.setComments(null);
				session.save(ampMEIndValnew);
			}
			tx.commit();							
		} catch (Exception e) {
			logger.error("Exception from saveMEIndicator() :" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
	}	
	
	public static Collection getPriorIndicatorValues(Long tempId,boolean considerLastValue)
	{
		Session session = null;
		Collection col = new ArrayList();
		
		try
		{
			session = PersistenceManager.getSession();
			PriorCurrentValues priorCurrVal = null;
			if (considerLastValue) {
				AmpMEIndicatorValue meVal = (AmpMEIndicatorValue) session.load(AmpMEIndicatorValue.class,tempId);
				if (meVal.getActualValDate() != null) {
					priorCurrVal = new PriorCurrentValues();
					priorCurrVal.setCurrValue(meVal.getActualVal());
					priorCurrVal.setCurrValDate(DateConversion.ConvertDateToString(meVal.getActualValDate()));
					col.add(priorCurrVal);									
				}
			}
			
			String qryStr = "select pivalues from " + AmpMECurrValHistory.class.getName() + "" +
							" pivalues where (pivalues.meIndValue=:tempId)" ;
			Query qry = session.createQuery(qryStr);
			qry.setParameter("tempId",tempId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while(itr.hasNext())
			{
				AmpMECurrValHistory meCurrValHis = new AmpMECurrValHistory();
				priorCurrVal = new PriorCurrentValues();
				meCurrValHis = (AmpMECurrValHistory) itr.next();
				priorCurrVal.setCurrHistoryId(meCurrValHis.getAmpMECurrValHistoryId());
				priorCurrVal.setCurrValue(meCurrValHis.getCurrValue());
				priorCurrVal.setCurrValDate(DateConversion.ConvertDateToString(meCurrValHis.getCurrValueDate()));
				col.add(priorCurrVal);
			}
		}
		catch(Exception e)
		{
			logger.error("Unable to get the prior values of the indicator");
			logger.debug("Exception : " + e);
		}
		finally
		{
			try 
			{
				if (session != null) 
				{
					PersistenceManager.releaseSession(session);
				}
			} 
			catch (Exception ex) 
			{
				logger.debug("releaseSession() FAILED", ex);
			}
		}
		return col;
	}
	
	public static AmpMEIndicatorValue getMEIndicatorValue(Long id) {
		Session session = null;
		AmpMEIndicatorValue indVal = null;
		
		try {
			session = PersistenceManager.getSession();
			indVal = (AmpMEIndicatorValue) session.load(AmpMEIndicatorValue.class,id);
		}
		catch(Exception e) {
			logger.error("Unable to get AmpMEIndicatorValue object");
		}
		finally {
			try  {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} 
			catch (Exception ex)  {
				logger.error("releaseSession() FAILED", ex);
			}
		}
		return indVal;		
	}
	
	public static String getRiskColor(int overallRisk) {
		String fntColor = "";
		int r = 0,g = 0,b = 0;
		switch (overallRisk) {
		case Constants.HIGHLY_SATISFACTORY:
			r = Constants.HIGHLY_SATISFACTORY_CLR.getRed();
			g = Constants.HIGHLY_SATISFACTORY_CLR.getGreen();
			b = Constants.HIGHLY_SATISFACTORY_CLR.getBlue();
			break;
		case Constants.VERY_SATISFACTORY:
			r = Constants.VERY_SATISFACTORY_CLR.getRed();
			g = Constants.VERY_SATISFACTORY_CLR.getGreen();
			b = Constants.VERY_SATISFACTORY_CLR.getBlue();				
			break;
		case Constants.SATISFACTORY:
			r = Constants.SATISFACTORY_CLR.getRed();
			g = Constants.SATISFACTORY_CLR.getGreen();
			b = Constants.SATISFACTORY_CLR.getBlue();				
			break;
		case Constants.UNSATISFACTORY:
			r = Constants.UNSATISFACTORY_CLR.getRed();
			g = Constants.UNSATISFACTORY_CLR.getGreen();
			b = Constants.UNSATISFACTORY_CLR.getBlue();				
			break;
		case Constants.VERY_UNSATISFACTORY:
			r = Constants.VERY_UNSATISFACTORY_CLR.getRed();
			g = Constants.VERY_UNSATISFACTORY_CLR.getGreen();
			b = Constants.VERY_UNSATISFACTORY_CLR.getBlue();				
			break;
		case Constants.HIGHLY_UNSATISFACTORY:
			r = Constants.HIGHLY_UNSATISFACTORY_CLR.getRed();
			g = Constants.HIGHLY_UNSATISFACTORY_CLR.getGreen();
			b = Constants.HIGHLY_UNSATISFACTORY_CLR.getBlue();				
		}
		
		String hexR = Integer.toHexString(r);
		String hexG = Integer.toHexString(g);
		String hexB = Integer.toHexString(b);
		if (hexR.equals("0"))
			fntColor += "00";
		else 
			fntColor += hexR;
		
		if (hexG.equals("0"))
			fntColor += "00";
		else 
			fntColor += hexG;
		
		if (hexB.equals("0"))
			fntColor += "00";
		else 
			fntColor += hexB;
		
		return fntColor;
	}
	
	public static int getOverallPortfolioRisk(Collection actIds) {
		int risk = 0;
		try {
		Collection col = getPortfolioMEIndicatorRisks(actIds); 
		Iterator itr = col.iterator();
		float temp = 0;
		while (itr.hasNext()) {
			MEIndicatorRisk meRisk = (MEIndicatorRisk) itr.next();
			temp += meRisk.getRiskRating() * meRisk.getRiskCount();
		}
		
		if (col.size() > 0) {
			temp /= (float) col.size();
			temp = Math.round(temp);
			if (temp < 0) 
				risk = (int) Math.floor(temp);
			else if(temp > 0)
				risk = (int) Math.ceil(temp);
			else 
				risk = -1;
		}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return risk;
	}
	
	public static int getOverallRisk(Long actId) {
		int risk = 0;
		try {
		Collection col = getMEIndicatorRisks(actId); 
		Iterator itr = col.iterator();
		float temp = 0;
		while (itr.hasNext()) {
			MEIndicatorRisk meRisk = (MEIndicatorRisk) itr.next();
			temp += meRisk.getRiskRating();
		}
		if (col.size() > 0) {
			temp /= (float) col.size();
			temp = Math.round(temp);
			if (temp < 0) 
				risk = (int) Math.floor(temp);
			else if(temp > 0)
				risk = (int) Math.ceil(temp);
			else
				risk = -1;
		}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return risk;
	}
	
	public static int getRiskRatingValue(String name) {
		Session session = null;
		int riskRating = 0;
		
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select r.ratingValue from " + AmpIndicatorRiskRatings.class.getName() + "" +
					" r where (r.ratingName=:name)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("name",name,Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				Integer temp = (Integer) itr.next();
				riskRating = temp.intValue();
			}
		}
		catch(Exception e) {
			logger.error("Unable to get risk ratibg value");
			e.printStackTrace(System.out);
		}
		finally {
			try  {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} 
			catch (Exception ex) {
				logger.error("releaseSession() FAILED", ex);
			}
		}		
		return riskRating;
	}

	public static String getRiskRatingName(int risk) {
		Session session = null;
		String riskName = "";
		
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select r.ratingName from " + AmpIndicatorRiskRatings.class.getName() + "" +
					" r where (r.ratingValue=:risk)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("risk",new Integer(risk),Hibernate.INTEGER);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				riskName = (String) itr.next();
			}
		}
		catch(Exception e) {
			logger.error("Unable to get risk ratibg value");
			e.printStackTrace(System.out);
		}
		finally {
			try  {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} 
			catch (Exception ex) {
				logger.error("releaseSession() FAILED", ex);
			}
		}		
		return riskName;
	}	
	
}
