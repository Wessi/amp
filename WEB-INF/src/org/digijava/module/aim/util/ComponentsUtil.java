package org.digijava.module.aim.util;
/*
 * @author Govind G Dalwani
 */
import java.util.*;

import org.apache.log4j.*;
import org.digijava.kernel.persistence.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.AmpMEIndicatorList;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.FundingDetail;

import net.sf.hibernate.*;

public class ComponentsUtil{

	private static Logger logger = Logger.getLogger(ComponentsUtil.class);

	public static Collection<AmpComponent> getAmpComponents()
	{
		logger.debug(" starting to get all the components....");
		Collection col = null;
		String queryString=null;
		Session session = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select distinct co from "+AmpComponent.class.getName()+" co ";
			qry = session.createQuery(queryString);
			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get Components  from database " + ex.getMessage());
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
	public static Collection getComponentForEditing(Long id)
	{
		Collection col = null;
		String queryString=null;
		Session session = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select co from "+AmpComponent.class.getName()+" co where co.ampComponentId=:id";
			qry = session.createQuery(queryString);
			qry.setParameter("id",id,Hibernate.LONG);

			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get Component for editing from database " + ex.getMessage());
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
	 * update component details
	 */
	public static void updateComponents(AmpComponent ampComp) {
		DbUtil.update(ampComp);
	}

	/*
	 * add a new Component
	 */
	public static void addNewComponent(AmpComponent ampComp){
			DbUtil.add(ampComp);

	}

	/*
	 * delete a Component
	 */
	public static void deleteComponent(Long compId)
	{

		Session session = null;
		Transaction tx = null;
		try
		{
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			
			// Now delete AmpComponent
			AmpComponent ampComp = (AmpComponent) session.load(
					AmpComponent.class,compId);

			/*
			 * Delete AmpComponentFunding items
			 * This operation should be done on casade :(
			 */
			String queryString = "select co from "
					+ AmpComponentFunding.class.getName()
					+ " co where amp_component_id='" + compId + "'";
			Query qry = session.createQuery(queryString);

			Iterator componentFundingIterator = qry.list().iterator();
			
			while(componentFundingIterator.hasNext()){
				session.delete(componentFundingIterator.next());
			}
			

			session.delete(ampComp);
			tx.commit();
		}
		catch (Exception e)
		{
			logger.error("Exception from deleteComponent() : " + e);
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
	 * To get the Component Fundings from the ampComponentFundings Table
	 * parameter passed is the component id
	 */
	public static Collection getComponentFunding(Long id)
	{
		Collection col = null;
		String queryString=null;
		Session session = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select co from " +AmpComponentFunding.class.getName()+" co where co.component=:id";
			qry = session.createQuery(queryString);
			qry.setParameter("id",id,Hibernate.LONG);

			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get Component for editing from database " + ex.getMessage());
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
	 * To get the physical progress for a component from the physical progress table...
	 * parameter passed is the amp component id
	 */
	public static Collection getComponentPhysicalProgress(Long id)
	{
		Collection col = null;
		String queryString=null;
		Session session = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select co from "+AmpPhysicalPerformance.class.getName()+" co where co.component=:id";
			qry = session.createQuery(queryString);
			qry.setParameter("id",id,Hibernate.LONG);
			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get Component for editing from database " + ex.getMessage());
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
	public static Collection getComponent(String title)
	{
		logger.info(" in here ");
		Collection col = null;
		String queryString=null;
		Session session = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select co from "+AmpComponent.class.getName()+" co where co.title=:title";
			qry = session.createQuery(queryString);
			qry.setParameter("title",title,Hibernate.STRING);

			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get Component for editing from database " + ex.getMessage());
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
		logger.info(" returning the collection");
		return col;
	}
	/*
	 * this is to check whether a component with the same name already exists in the AMP Components Table.returns true if present and false if not present
	 */
	public static boolean checkComponentNameExists(String title)
	{
		logger.info(" in the checking for components existence through title ");
		boolean flag = false;
		Collection col = null;
		String queryString=null;
		Session session = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select co from "+AmpComponent.class.getName()+" co where co.title=:title";
			qry = session.createQuery(queryString);
			qry.setParameter("title",title,Hibernate.STRING);

			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get Component for editing from database " + ex.getMessage());
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
		logger.info(" returning the collection");
		if(col.isEmpty())
		{
		return false;
		}
		else return true;
	}
	/*
	 * this is to check whether a component with the same code already exists in the AMP Components Table.returns true if present and false if not present
	 */
	public static boolean checkComponentCodeExists(String code)
	{
		logger.info(" in the checking for component existence through code ");
		Collection col = null;
		String queryString=null;
		Session session = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select co from "+AmpComponent.class.getName()+" co where co.code=:code";
			qry = session.createQuery(queryString);
			qry.setParameter("code",code,Hibernate.STRING);

			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get Component for editing from database " + ex.getMessage());
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
		logger.info(" returning the collection");
		if(col.isEmpty())
		{
		return false;
		}
		else return true;
	}
	
	public static Collection getAllComponentIndicators() 
	{
		Session session = null;
		Query qry = null;
		Collection col = new ArrayList();

		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select i from " + AmpComponentsIndicators.class.getName() + " i";
			qry = session.createQuery(queryString);
			col = qry.list();
		} 
		catch (Exception e) 
		{
			logger.error("Unable to get component indicators");
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
	
	public static void saveComponentIndicator(AmpComponentsIndicators newIndicator) 
	{
		Session session = null;
		Transaction tx = null;
		
		try 
		{
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(newIndicator);
			tx.commit();							
		} 
		catch (Exception e) 
		{
			logger.error("Exception from saveComponentIndicator() :" + e.getMessage());
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
	
	public static Collection getComponentIndicator(Long id){
		Session session = null;
		Query query = null;
		Collection ampCoInd = null;
		try{
			session = PersistenceManager.getSession();
			String queryString = "select ami from "+ AmpComponentsIndicators.class.getName()+ " ami where (ami.ampCompIndId=:id)";
			query = session.createQuery(queryString);
			query.setParameter("id",id,Hibernate.LONG);
			ampCoInd =query.list();
		}catch(Exception ex){
			logger.error("Unable to retrieve Indicator/s for a give Component");
			logger.debug("Exception:="+ex);
		}
		finally{
			try{
				if(session!=null){
					PersistenceManager.releaseSession(session);
				}
			}catch(Exception ex){
				logger.debug("releaseSession() Failed:",ex);
			}
		}
		return ampCoInd;
	}
	
	public static void delComponentIndicator(Long indId)
	{

		Session session = null;
		Transaction tx = null;
		try
		{
			session = PersistenceManager.getSession();
			AmpComponentsIndicators ampCompInd = (AmpComponentsIndicators) session.load(
					AmpComponentsIndicators.class,indId);
			tx = session.beginTransaction();
			session.delete(ampCompInd);
			tx.commit();
		}
		catch (Exception ex){
			logger.error("Exception from delComponentIndicators() :" + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null){
				try{
					tx.rollback();
				}catch (Exception trbf){
					logger.error("Transaction roll back failed ");
					trbf.printStackTrace(System.out);
				}
			}
		}
		finally{
			if (session != null){
				try{
					PersistenceManager.releaseSession(session);
				}catch (Exception rsf){
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
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
					+ AmpComponentsIndicators.class.getName() + " ami " 
					+ "where ( name=:name"
					+ " or code=:code) and " +
							"(ami.ampCompIndId !=:id)" ;
				qry = session.createQuery(queryString);
				qry.setParameter("id", id, Hibernate.LONG);
				qry.setParameter("code", code.trim(), Hibernate.STRING);
				qry.setParameter("name", name.trim(), Hibernate.STRING);
			} else {
				queryString = "select count(*) from "
					+ AmpComponentsIndicators.class.getName() + " ami " 
					+ "where ( name=:name"
					+ " or code=:code)" ;
				qry = session.createQuery(queryString);
				qry.setParameter("code", code.trim(), Hibernate.STRING);
				qry.setParameter("name", name.trim(), Hibernate.STRING);
												
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
			catch (Exception ex) {
				logger.debug("releaseSession() FAILED", ex);
			}
		}
		return duplicatesExist;
	}

	public static void calculateFinanceByYearInfo(Components<FundingDetail> tempComp, Collection<AmpComponentFunding> fundingComponentActivity) {
		SortedMap<Integer, Map<String, Double>> fbyi = new TreeMap<Integer,Map<String,Double>>();
		Iterator<AmpComponentFunding> fundingDetailIterator = fundingComponentActivity.iterator();
		while(fundingDetailIterator.hasNext()){
			AmpComponentFunding fundingDetail = fundingDetailIterator.next();
			Calendar cal = Calendar.getInstance();
			cal.setTime(fundingDetail.getTransactionDate());
			int year = cal.get(Calendar.YEAR);
			if(!fbyi.containsKey(year)){
				fbyi.put(year, new HashMap<String,Double>());
			}
			Map<String, Double> yearInfo = fbyi.get(year);
			double montoProgramado = yearInfo.get("MontoProgramado") != null ? yearInfo.get("MontoProgramado") : 0;
			double montoReprogramado = yearInfo.get("MontoReprogramado") != null ? yearInfo.get("MontoReprogramado") : 0;
			double montoEjecutado = yearInfo.get("MontoEjecutado") != null ? yearInfo.get("MontoEjecutado") : 0;
			double exchangeRate = 1;
			if(fundingDetail.getExchangeRate() != null && fundingDetail.getExchangeRate() != 0 ) {
				exchangeRate = fundingDetail.getExchangeRate();
			}
			if(fundingDetail.getTransactionType()== 0 && fundingDetail.getAdjustmentType() == 0){
				montoProgramado +=fundingDetail.getTransactionAmount() / exchangeRate ;
			}else if(fundingDetail.getTransactionType()== 0 && fundingDetail.getAdjustmentType() == 1){
				montoReprogramado +=fundingDetail.getTransactionAmount() / exchangeRate ;
			}else if(fundingDetail.getTransactionType()== 2 && fundingDetail.getAdjustmentType() == 1){
				montoEjecutado +=fundingDetail.getTransactionAmount() / exchangeRate ;
			}
			yearInfo.put("MontoProgramado", montoProgramado);
			yearInfo.put("MontoReprogramado", montoReprogramado);
			yearInfo.put("MontoEjecutado", montoEjecutado);			
		}
		tempComp.setFinanceByYearInfo(fbyi);		
	}
}