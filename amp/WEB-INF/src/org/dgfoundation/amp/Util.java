package org.dgfoundation.amp; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.swarmcache.ObjectCache;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.workers.CategAmountColWorker;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.EthiopianCalendar;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.editor.exception.EditorException;
import org.springframework.beans.BeanWrapperImpl;

public final class Util {

	protected static Logger logger = Logger.getLogger(Util.class);

	/**
	 * Returns a set with objects from the "source" collection, which were identified by "selected" id
	 * @param source
	 * @param selected an object whose toString property returns the id of the selected object
	 * @return the set
	 * @see Util#getSelectedObjects method
	 */
	public static Identifiable getSelectedObject(Collection source, Object selected) {
		Set ret=getSelectedObjects(source, new Object[] {selected});
		if(ret.size()==0) return null;
		return (Identifiable) ret.iterator().next();
	}
	
	/**
	 * Returns a Collection of the same type as the source, holding 
	 * the elements of the original collection inside WrapDynaBean items 
	 * @param source the source collection
	 * @return the target collection - a list
	 * @see BeanWrapperImpl
	 */
	public static List<BeanWrapperImpl> createBeanWrapperItemsCollection(Collection source) {
		List<BeanWrapperImpl> dest=new ArrayList<BeanWrapperImpl>();
		Iterator i=source.iterator();
		while (i.hasNext()) {
			Object element = (Object) i.next();
			BeanWrapperImpl bwi=new BeanWrapperImpl(element);

			dest.add(bwi);
		}
		return dest;

	}
		
	
		
	
	/**
	 * loads using Hibernate the object of the specified type, identified by the serializable object
	 * @param type the class type of the specified object
	 * @param selected the  serializable id of the object to be loaded
	 * @return the fetched object
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public static Object getSelectedObject(Class type, Object selected) throws HibernateException, SQLException {
		if(selected==null || "-1".equals(selected.toString())) return null;
		Set ret=getSelectedObjects(type, new Object[] {selected});
		if(ret.size()==0) return null;
		return ret.iterator().next();
	}
	
	/**
	 * loads using Hibernate the objects of the specified type, identified by the serializable objects in the array
	 * @param type the class type of the specified objects
	 * @param selected the array with serializable ids of objects to be loaded
	 * @return the set of fetched objects
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public static Set getSelectedObjects(Class type, Object[] selected) throws HibernateException, SQLException {
		if(selected==null) return null;
		HashSet set=new HashSet();		
		Session session = PersistenceManager.getSession();
		for (int i = 0; i < selected.length; i++) {
			set.add(session.load(type,new Long(selected[i].toString())));
		}
		return set;
	}
	
	/**
	 * Returns a set with objects from the "source" collection, which were identified by Ids present in the "selected" array
	 * @param source a Collection of Identifiable objects
	 * @param selected an array of objects whose toString property returns the Id of the selected object
	 * @return a set with objects
	 * @see Identifiable
	 */
	public static Set getSelectedObjects(Collection source, Object[] selected) {
		Set ret = new HashSet();
		if(selected==null) return ret;
		Iterator i = source.iterator();
		while (i.hasNext()) {
			Identifiable element = (Identifiable) i.next();
			for (int k = 0; k < selected.length; k++)
				if (element.getIdentifier().equals(
						new Long(selected[k].toString())))
					ret.add(element);
		}

		return ret;

	}
	
	public static String getEditorBody(Site site,String key,  Locale navLang)
			throws EditorException {
		String editorBody = null;
		
		Session session=null;
		try {
			session = PersistenceManager.getSession();

			Query q = session
					.createQuery("select e.body,e.language from "
							+ (org.digijava.module.editor.dbentity.Editor.class)
									.getName()
							+ " e "
							+ " where (e.siteId=:siteId) and (e.editorKey=:editorKey)");
	          q.setParameter("siteId", site.getSiteId(), Hibernate.STRING);
	          q.setParameter("editorKey", key, Hibernate.STRING);
	          List result = q.list();
	          if(result.size()==0) return "";
	          Iterator i=result.iterator();
	          Object[] res=null;
	          while (i.hasNext()) {
				 res= (Object[]) i.next();
				if(res[1].equals(navLang.getCode())) editorBody=(String) res[0];				
			}
	         if(editorBody==null) editorBody=(String) res[0];
	          

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();			
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}

		return editorBody;
	}
	
	
    

	/**
	 * Return a list of AmpOrganisationS that match the specified role
	 * 
	 * @param orgRoles
	 *            the AmpOrgRole collection with org-role mappings
	 * @param roleCode
	 *            the role code
	 * @return the list of AmpOrgs with that role
	 * @author mihai 06.05.2007
	 */
	public static Collection getOrgsByRole(Collection orgRoles, String roleCode) {
		ArrayList ret = new ArrayList();
		Iterator i = orgRoles.iterator();
		while (i.hasNext()) {
			AmpOrgRole element = (AmpOrgRole) i.next();
			if (element.getRole().getRoleCode().equals(roleCode))
				ret.add(element.getOrganisation());
		}
		return ret;
	}

	/**
	 * Returns comma separated view string representation of the collection
	 * items
	 * 
	 * @param col
	 *            the collectoin
	 * @return the comma separated string
	 * @author mihai 06.05.2007
	 */
	public static String toCSString(Collection col, boolean identifiable) {
		String ret = "";
		if (col == null || col.size() == 0)
			return ret;
		Iterator i = col.iterator();
		while (i.hasNext()) {
			Object element = (Object) i.next();
			if (element == null)
				continue;			
			Object item=element;
			if(identifiable) item=((Identifiable)element).getIdentifier();
			
			if (item instanceof String)
				ret += "'" + (String) item + "'"; else
			if (item instanceof PropertyListable)
				ret += ((PropertyListable)item).getBeanName();
			
			else	ret += item.toString();
			if (i.hasNext())
				ret += ",";
		}
		return ret;
	}
	


	/**
	 * QUICK access to exchange rates. Gets the exchange rate for the given
	 * currency at the specified date. The exchange rate is fetched directy from
	 * the DB using a stored function. Afterwards it is stored inside the digi
	 * cache for later usage so we do not need to access the DB again. All the
	 * logic involving exchange rate calculation is done at the DB level,
	 * therefore the result is returned very quickly.
	 * 
	 * @param currency
	 *            the currency code
	 * @param currencyDate
	 *            the currency date
	 * @return the exchange rate
	 * @author mihai 06.05.2007
	 * @see CategAmountColWorker
	 * @see AmountCell
	 */
	public static double getExchange(String currency, java.sql.Date currencyDate) {
		Connection conn = null;
		double ret = 1;

		// we try the digi cache:
		ObjectCache ratesCache = DigiCacheManager.getInstance().getCache(
				"EXCHANGE_RATES_CACHE");

		Double cacheRet = (Double) ratesCache.get(new String(currency
				+ currencyDate));
		if (cacheRet != null)
			return cacheRet.doubleValue();
		Session sess = null;
		try {
			sess = PersistenceManager.getSession();
			conn = sess.connection();
		} catch (HibernateException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}

		String query = "SELECT getExchange(?,?)";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, currency);
			ps.setDate(2, currencyDate);

			ResultSet rs = ps.executeQuery();

			if (rs.next())
				ret = rs.getDouble(1);

			rs.close();

		} catch (SQLException e) {
			logger.error("Unable to get exchange rate for currencty "
					+ currency + " for the date " + currencyDate);
			logger.error(e);
			e.printStackTrace();
		}

		try {
			sess.close();
		} catch (HibernateException e) {
			logger.error(e);
			e.printStackTrace();
		}

		logger.info("rate for " + currency + " to USD on " + currencyDate
				+ " is " + ret);
		if (ret != 1)
			ratesCache
					.put(new String(currency + currencyDate), new Double(ret));

		return ret;

	}
	/**
	 * As the name implies only the years are checked by this function. 
	 * 
	 * @param toBeCheckedDate 
	 * @param startDate
	 * @param endDate
	 * @param calendarTypeId
	 * @author Alex Gartner - needed for MTEF Projections filter from Financial Progress
	 * @return If filterStartYear <= toBeCheckedYear && toBeCheckedYear < filterEndYear then it returns true. Otherwise false.
	 */
	public static boolean checkYearFilter(Date toBeCheckedDate, Date startDate, Date endDate, Long calendarTypeId ) {
		EthiopianCalendar ec			= new EthiopianCalendar();
		GregorianCalendar currentTime	= new GregorianCalendar();
		currentTime.setTime(toBeCheckedDate);
		
		ec								= ec.getEthiopianDate( currentTime );
		
		Integer year					= currentTime.get(Calendar.YEAR);
		
		if(calendarTypeId.equals(Constants.ETH_FY))
		{
			year	= new Integer(ec.ethFiscalYear);
			
			//quarter=new String("Q"+ec.ethFiscalQrt);
		}
		if(calendarTypeId.equals(Constants.ETH_CAL))
		{
			year	=	new Integer(ec.ethYear);
		}
		
		GregorianCalendar startCalendar	= new GregorianCalendar();
		startCalendar.setTime(startDate);
		
		GregorianCalendar endCalendar	= new GregorianCalendar();
		endCalendar.setTime(endDate);
		
		Integer startYear				= startCalendar.get(Calendar.YEAR);
		Integer endYear					= endCalendar.get(Calendar.YEAR);
		
		if ( startYear <= year && year < endYear )
				return true;
		
		return false;
	}

}
