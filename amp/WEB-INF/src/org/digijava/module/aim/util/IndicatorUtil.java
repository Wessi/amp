package org.digijava.module.aim.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.dbentity.IndicatorConnection;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.DateConversion;

/**
 * Indicator utilities.
 * Methods for manipulating {@link AmpIndicator}, {@link AmpIndicatorValue},
 * {@link IndicatorConnection} and all its subclasses.
 * Also indicator relation management methods can be found here. 
 *
 */
public class IndicatorUtil {

	private static Logger logger = Logger.getLogger(IndicatorUtil.class);
	
	
	/**
	 * Loads Indicator from db.
	 * NULL if nothing found with specified ID.
	 * @param id primary key values of AmpIndicator
	 * @return {@link AmpIndicator} object loaded from db.
	 * @throws DgException
	 */
	public static AmpIndicator getIndicator(Long id) throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		AmpIndicator indicator=null;
		try {
			indicator = (AmpIndicator)session.load(AmpIndicator.class, id);
		} catch (ObjectNotFoundException e) {
			logger.debug("indicator with "+id+"not found");
		} catch (Exception e) {
			throw new DgException("Cannot load indicator",e);
		}
		return indicator;
	}
	
	/**
	 * Saves {@link AmpIndicator} or updates in db.
	 * @param indicator object.
	 * @throws DgException if anything goes wrong rollback is attempted.
	 */
	public static void saveIndicator(AmpIndicator indicator) throws DgException {
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.saveOrUpdate(indicator);
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error saving indicator",e);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception ex) {
					throw new DgException("Cannot rallback save Indicator actioin",ex);
				}
				throw new DgException("Cannot save Indicator!",e);
			}
		}
	}
	
	/**
	 * Removes {@link AmpIndicator} from db.
	 * Also Indicator values will be deleted because of AmpIndicator.hbm.xml mappings.
	 * @param id primary key of indicator
	 * @throws DgException
	 */
	public static void deleteIndicator(Long id) throws DgException{
		deleteIndicator(getIndicator(id));
	}

	/**
	 * Removes {@link AmpIndicator} from db.
	 * Also Indicator values will be deleted because of AmpIndicator.hbm.xml mappings.
	 * @param indicator AmpIndicator object
	 * @throws DgException
	 */
	public static void deleteIndicator(AmpIndicator indicator) throws DgException{
		unAssigneIndicatorFromAll(indicator);
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			//indicator.getSectors().clear();
			//session.update(indicator);
			session.delete(indicator);
			tx.commit();
		} catch (HibernateException e) {
			logger.error("caannot remove Indicator!",e);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					throw new DgException("Cannot rollback delete AmpIndicator action");
				}
			}
			throw new DgException("Cannot delete AmpIndicator");
		}
		
	}

	/**
	 * Recursively all indicators of the theme object.
	 * If childrenToo parameter is true then all indicators assigned to all sub themes at all levels of the specified theme are also add to the results.
	 * If this parameter is  false then only specified theme indicators are returned.
	 * @param prog
	 * @param childrenToo
	 * @return
	 * @throws DgException
	 */
	public static Set<IndicatorTheme> getIndicators(AmpTheme prog, boolean childrenToo) throws DgException{
		Set<IndicatorTheme> indicators = new TreeSet<IndicatorTheme>();
		if (prog.getIndicators() != null) {
			indicators.addAll(prog.getIndicators());
		}
		if (childrenToo) {
			Collection<AmpTheme> children = ProgramUtil.getSubThemes(prog.getAmpThemeId());
			if (children != null) {
				for (AmpTheme child : children) {
					Set<IndicatorTheme> subIndicators = getIndicators(child, childrenToo);
					indicators.addAll(subIndicators);
				}
			}
		}
		return indicators;
	}


	/**
	 * Returns all connections objects for indicator.
	 * connection objects are for both {@link AmpTheme} and {@link AmpActivity}
	 * @param indicator
	 * @return
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<IndicatorConnection> getAllConnectionsOfIndicator(AmpIndicator indicator) throws DgException{
		List<IndicatorConnection> result=null;
		Long indicatorId=indicator.getIndicatorId();
		Session session=PersistenceManager.getRequestDBSession();
		String oql="from "+IndicatorConnection.class.getName()+" c where c.indicator.indicatorId=:indId";
		try {
			Query query=session.createQuery(oql);
			query.setLong("indId", indicatorId);
			result=query.list();
		} catch (HibernateException e) {
			throw new DgException("Cannot search connections for indicators",e);
		}
		return result;
	}
	
	/**
	 * Drops all connections of indicator to activities, themes and sectors.
	 * Removing connection beans will cascade remove on indicator values too.
	 * @param indicator
	 * @throws DgException
	 */
	public static void unAssigneIndicatorFromAll(AmpIndicator indicator)throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			
			//Indicator values for themes.
			if (indicator.getValuesTheme()!=null){
				for (IndicatorConnection indicatorValues : getAllConnectionsOfIndicator(indicator)) {
					try {
						session.delete(indicatorValues);
					} catch (HibernateException e) {
						e.printStackTrace();
					}
				}
				indicator.getValuesTheme().clear();
			}
			
			//save indicator changes.
			session.save(indicator);
			
			//try to commit
			if (tx!=null) tx.commit();
		} catch (HibernateException e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					throw new DgException("Cannot rollback unassigne inndicator from all entities",e);
				}
			}
			throw new DgException("Cannot unassigne inndicator from all entities",e);
		}
	}
	
	/**
	 * Loads IndicatorTheme connection bean.
	 * NULL if not found.
	 * @param connId
	 * @return bean that represents connection between AmpTheme and Indicator - {@link IndicatorTheme}
	 * @throws DgException
	 */
	public static IndicatorTheme getConnectionToTheme(Long connId) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		IndicatorTheme conn=null;
		try {
			conn=(IndicatorTheme)session.load(IndicatorTheme.class, connId);
		} catch (ObjectNotFoundException e) {
			logger.debug("Cannot fined IndicatorTheme with Id="+connId);
		} catch (Exception ex){
			throw new DgException("Cannot load connection to theme (IndicatorTheme) with id "+connId,ex);
		}
		return conn;
	}
	
	/**
	 * Loads IdnicatorActivity connection bean by ID.
	 * If nothing found NULL is returned.
	 * @param connId
	 * @return connection bean or NULL
	 * @throws DgException
	 */
	public static IndicatorActivity getConnectionToActivity(Long connId) throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		IndicatorActivity result=null;
		try {
			result= (IndicatorActivity)session.load(IndicatorActivity.class, connId);
		} catch (ObjectNotFoundException e) {
			logger.debug("Activity-Indicator conenction with ID="+connId+" not found",e);
		} catch (Exception ex){
			throw new DgException("cannot load indicatorActivity with id="+connId,ex);
		}
		return result;
	}
	
	/**
	 * Removes connection bean dropping connection between indicator and program or activity.
	 * Indicator itself is unaffected but values for this connections will be deleted with connection bean.
	 * @param conenctionId id of the connection bean.
	 * @throws DgException
	 */
	public static void removeConnection(Long connectionId)throws DgException{
		removeConnection(getConnectionToTheme(connectionId));
	}
	
	/**
	 * Removes connection bean dropping connection between indicator and program or activity.
	 * Indicator itself is unaffected but values for this connections will be deleted with connection bean.
	 * @param conn
	 * @throws DgException
	 */
	public static void removeConnection(IndicatorConnection conn)throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.delete(conn);
			tx.commit();
		} catch (HibernateException e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					throw new DgException("Cannot rollback indicator connection deletion operation!",e1);
				}
				throw new DgException("Cannot delete indicator connection bean ",e);
			}
		}
	}
	
	/**
	 * Loads all indicators from DB.
	 * @return list of {@link AmpIndicator} objects
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<AmpIndicator> getAllIndicators() throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		List result=null;
		try {
			result=session.createQuery("from "+AmpIndicator.class.getName()).list();
		} catch (HibernateException e) {
			throw new DgException("Cannot load all indicators!",e);
		}
		return result;
	}

	/**
	 * Assigns Indicator to program (AmpTheme).
	 * @see #assignIndicatorToTheme(AmpTheme, AmpIndicator)
	 * @param themeId
	 * @param indicatorId
	 * @throws DgException
	 */
	
	
	
	public static void assignIndicatorToTheme(Long themeId, Long indicatorId) throws DgException{
		AmpTheme theme=ProgramUtil.getThemeById(themeId);
		AmpIndicator indicator=IndicatorUtil.getIndicator(indicatorId);
		assignIndicatorToTheme(theme, indicator);
	}
	
	
	
	
	/**
	 * Assigns indicator to program (theme).
	 * Actually this creates new {@link IndicatorTheme} object in db 
	 * which is subclass of {@link IndicatorConnection} db bean.
	 * @param theme
	 * @param indicator
	 * @throws DgException
	 */
	public static IndicatorTheme assignIndicatorToTheme(AmpTheme theme, AmpIndicator indicator) throws DgException{
		IndicatorTheme connection=new IndicatorTheme();
		connection.setIndicator(indicator);
		connection.setTheme(theme);
		Session sessioin=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=sessioin.beginTransaction();
			sessioin.save(connection);
			tx.commit();
			sessioin.flush();
			return connection;
		} catch (HibernateException e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					throw new DgException("Cannot rollback create indicator-theme connection action",e1);
				}
			}
			throw new DgException("Cannot create indicator-theme connection ",e);
		}
	}

	public static IndicatorActivity assignIndicatorToActivity(AmpActivity activity, AmpIndicator indicator) throws DgException{
		IndicatorActivity connection=new IndicatorActivity();
		connection.setIndicator(indicator);
		connection.setActivity(activity);
		// TODO replace all lines below awith: return saveConnectionToActivity(connection);
		Session sessioin=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=sessioin.beginTransaction();
			sessioin.save(connection);
			tx.commit();
			sessioin.flush();
			return connection;
		} catch (HibernateException e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					throw new DgException("Cannot rollback create indicator-activity connection action",e1);
				}
			}
			throw new DgException("Cannot create indicator-activity connection ",e);
		}
	}

	/**
	 * 
	 * @param connection
	 * @return
	 * @throws DgException
	 */
	public static IndicatorActivity saveConnectionToActivity(IndicatorActivity connection) throws DgException{
		Session sessioin=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=sessioin.beginTransaction();
			sessioin.saveOrUpdate(connection);
			tx.commit();
			return connection;
		} catch (HibernateException e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					throw new DgException("Cannot rollback create indicator-activity connection action",e1);
				}
			}
			throw new DgException("Cannot create indicator-activity connection ",e);
		}
	}
	
	/**
	 * Return all indicators (Not connection beans) assigned to specified Theme 
	 * @param themeId primary key of the theme who's indicators should be returned.
	 * @return set of {@link AmpIndicator} beans.
	 * @throws DgException
	 */
	public static Set<AmpIndicator> getThemeIndicators(Long themeId) throws DgException{
		//TODO no need in his method. or retrive ampTheme and get list of connections from it.
		Session session=PersistenceManager.getRequestDBSession();
		Set<AmpIndicator> result=null;
		String oql="from "+IndicatorTheme.class.getName()+" ti where ti.theme.ampThemeId=:themeId";
		try {
			Query query=session.createQuery(oql);
			query.setLong("themeId", themeId);
			List resultList=query.list();
			if (resultList!=null && resultList.size()>0){
				result=new HashSet<AmpIndicator>();
				for (Iterator resIter = resultList.iterator(); resIter.hasNext();) {
					IndicatorTheme themeConnection = (IndicatorTheme) resIter.next();
					result.add(themeConnection.getIndicator());
				}
			}
		} catch (HibernateException e) {
			throw new DgException("cannot load Theme("+themeId+") indicators!",e);
		}
		return result;
	}
	
	public static Set<IndicatorTheme> getIndicatorThemeConnections(Long themeId) throws DgException{
		AmpTheme theme=ProgramUtil.getThemeById(themeId);
		return getIndicatorThemeConnections(theme);
	}

	public static Set<IndicatorTheme> getIndicatorThemeConnections(AmpTheme theme){
		return theme.getIndicators();
	}
	
	/**
	 * Update {@link IndicatorTheme} connection bean.
	 * To create new in db, use {@link #assignIndicatorToTheme(AmpTheme, AmpIndicator)}
	 * @param connection
	 * @throws DgException
	 */
	public static void updateThemeConnection(IndicatorTheme connection) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.update(connection);
			tx.commit();
		} catch (HibernateException e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					throw new DgException("Cannot rollback update operation of IndicatorTheme bean",e);
				}
			}
			throw new DgException("Cannot update IndicatorTheme bean",e);
		}
	}
	
	/**
	 * Tries to find connection bean between activity and indicator.
	 * If not found NULL is returned.
	 * @param activity
	 * @param indicator
	 * @return
	 * @throws DgException
	 */
	public static IndicatorActivity findActivityIndicatorConnection(AmpActivity activity,AmpIndicator indicator) throws DgException{
		IndicatorActivity result=null;
		//these two line may throw null pointer exception, but don't fix here, fix caller of this method to not path null here!
		Long activityId=activity.getAmpActivityId();
		Long indicatorId=indicator.getIndicatorId();
		Session session=PersistenceManager.getRequestDBSession();
		String oql="from "+IndicatorActivity.class.getName()+" conn ";
		oql+=" where conn.activity.ampActivityId=:actId and conn.indicator.indicatorId=:indicId";
		try {
			Query query=session.createQuery(oql);
			query.setLong("actId", activityId);
			query.setLong("indicId", indicatorId);
			result=(IndicatorActivity)query.uniqueResult();
		} catch (ObjectNotFoundException e) {
			logger.debug("Cannot find conenction for activity("+activityId+") and indicator("+indicatorId+")!");
		} catch (HibernateException e) {
			throw new DgException("Error searching conenction for activity("+activityId+") and indicator("+indicatorId+")!",e);
		}
		return result;
	}
	
	/**
	 * Loads all indicators for activity.
	 * NULL if there are no Indicators
	 * TODO correct this method
	 * @param activityId
	 * @return
	 * @throws DgException
	 */
	public static Set<AmpIndicator> getActivityIndicators(Long activityId) throws DgException{
		Set<AmpIndicator> result=null;
		Session sesison=PersistenceManager.getRequestDBSession();
		String oql="select indi from "+AmpIndicator.class.getName()+" indi ";
		oql+="where indi.valuesActivity.activity.ampActivityId =:actId ";
		oql+="order by indi.name";
		try {
			Query query=sesison.createQuery(oql);
			query.setLong("actId", activityId);
			List resultList=query.list();
			if (result!=null && result.size()>0){
				result=new HashSet<AmpIndicator>();
				for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
					AmpIndicator indicator = (AmpIndicator) iterator.next();
					result.add(indicator);
				}
			}
		} catch (HibernateException e) {
			throw new DgException("Cannot load indicators for Activity with id "+activityId,e);
		}
		return result;
	}

	/**
	 * Returns Indicator helper beans for activity.
	 * Used on edit activity action. please remove this when Edit activity is refactored.
	 * @param activityId
	 * @return set of indicator helper beans.
	 * @throws DgException
	 */
	public static Set<ActivityIndicator> getActivityIndicatorHelperBeans(Long activityId) throws DgException{
		AmpActivity activity=ActivityUtil.loadActivity(activityId);
		return getActivityIndicatorHelperBeans(activity);
	}
	
	
	/**
	 * Returns set of indicator helper beans for activity.
	 * @param activity
	 * @return
	 * @throws DgException
	 */
	public static Set<ActivityIndicator> getActivityIndicatorHelperBeans(AmpActivity activity) throws DgException{
		Set<ActivityIndicator> result=null;
		if (activity==null) return null;
		Set<IndicatorActivity> indicators =activity.getIndicators();
		if (indicators!=null && indicators.size()>0){
			result=new HashSet<ActivityIndicator>();
			for (IndicatorActivity connection : indicators) {
				ActivityIndicator helper=new ActivityIndicator();
				AmpIndicator indicator=connection.getIndicator();
				helper.setActivityId(activity.getAmpActivityId());
				helper.setConnectionId(connection.getId());
				helper.setIndicatorId(indicator.getIndicatorId());
				helper.setIndicatorCode(indicator.getCode());
				helper.setIndicatorName(indicator.getName());
				helper.setDefaultInd(indicator.isDefaultInd());
				helper.setIndicatorsCategory(indicator.getIndicatorsCategory());
				helper.setIndicatorValId(indicator.getIndicatorId());
				if (indicator.getRisk()!=null){
					helper.setRisk(indicator.getRisk().getAmpIndRiskRatingsId());
				}
				
				Set<AmpIndicatorValue> values=connection.getValues();
				if (values!=null && values.size()>0){
					for (AmpIndicatorValue value : values) {
						//target
						if (AmpIndicatorValue.ACTUAL==value.getValueType()){
							helper.setActualVal(value.getValue().floatValue());
							helper.setActualValDate(DateConversion.ConvertDateToString(value.getValueDate()));
							helper.setCurrentValDate(DateConversion.ConvertDateToString(value.getValueDate()));
							helper.setActualValComments(value.getComment());
						}
						if (AmpIndicatorValue.BASE==value.getValueType()){
							helper.setBaseVal(value.getValue().floatValue());
							helper.setBaseValDate(DateConversion.ConvertDateToString(value.getValueDate()));
							helper.setBaseValComments(value.getComment());
						}
						if (AmpIndicatorValue.TARGET==value.getValueType()){
							helper.setTargetVal(value.getValue().floatValue());
							helper.setTargetValDate(DateConversion.ConvertDateToString(value.getValueDate()));
							helper.setTargetValComments(value.getComment());
						}
						if (AmpIndicatorValue.REVISED==value.getValueType()){
							helper.setRevisedTargetVal(value.getValue().floatValue());
							helper.setRevisedTargetValDate(DateConversion.ConvertDateToString(value.getValueDate()));
							helper.setRevisedTargetValComments(value.getComment());
						}
					}
				}
				
				
				result.add(helper);
			}
		}
		
		return result;
	}
	
	/**
	 * Compares {@link AmpIndicator} objects by 'name' property
	 * @author Irakli Kobiashvili
	 *
	 */
	public static class IndicatorNameComparator implements Comparator<AmpIndicator> {

		public int compare(AmpIndicator indic0, AmpIndicator indic1) {
			return indic0.getName().compareTo(indic1.getName());
		}
		
	}
	
	/**
	 * Compares {@link IndicatorTheme} objects by its indicators name property.
	 * This is possible because IndicatorTheme should always have reference to one {@link AmpIndicator}
	 * @author Irakli Kobiashvili
	 *
	 */
	public static class IndThemeIndciatorNameComparator implements Comparator<IndicatorTheme>{

		public int compare(IndicatorTheme ind0, IndicatorTheme ind1) {
			return ind0.getIndicator().getName().compareTo(ind1.getIndicator().getName());
		}
		
	}
	
	
	//TODO INDIC - Old Methods below this. Above are added by Irakli.
	//most code below is deprecated or removed because of ugly names and ugly implementation!

	@Deprecated
	public static void saveIndicators(AmpPrgIndicator tempPrgInd) {

		Session session = null;
		Transaction tx = null;
		AmpActivity act = null;
		AmpIndicator tempind = new AmpIndicator();
		try {
			session = PersistenceManager.getRequestDBSession();
			if (tempPrgInd.getIndicatorId() != null) {
				tempind = (AmpIndicator) session.load(AmpIndicator.class,
						tempPrgInd.getIndicatorId());
			}
			tempind.setName(tempPrgInd.getName());
			tempind.setCode(tempPrgInd.getCode());
			tempind.setType(tempPrgInd.getType());
			tempind.setDescription(tempPrgInd.getDescription());
			tempind.setCreationDate(DateConversion.getDate(tempPrgInd
					.getCreationDate()));
			tempind.setDefaultInd(tempPrgInd.isDefaultInd());

			Collection sect = tempPrgInd.getIndSectores();
			if (sect != null && sect.size() > 0) {
				Iterator<ActivitySector> sectIter = sect.iterator();
				while (sectIter.hasNext()) {
					ActivitySector sector = sectIter.next();
					AmpSector tempAmpSector = null;
					tempAmpSector = (AmpSector) session.load(AmpSector.class,
							sector.getSectorId());
					Set ampThemeSet = new HashSet();
					if (tempAmpSector.getIndicators() != null) {
						tempAmpSector.getIndicators().add(tempind);
					} else {
						Set indcators = new HashSet();
						indcators.add(tempind);
						tempAmpSector.setIndicators(indcators);
					}
					ampThemeSet.add(tempAmpSector);
					tempind.setSectors(ampThemeSet);
				}
			}
			Collection selActivity = tempPrgInd.getSelectedActivity();
			if (selActivity != null && selActivity.size() > 0 ) {
				Iterator<LabelValueBean> selAct = selActivity.iterator();
				while(selAct.hasNext()){
					LabelValueBean 	selActivitys = selAct.next(); 
				AmpActivity tmpAmpActivity = null;
				tmpAmpActivity = (AmpActivity) session.load(AmpActivity.class,
						new Long(selActivitys.getValue()));
				Set activity = new HashSet();
				if (tmpAmpActivity.getIndicators() != null) {
					//TODO INDIC tmpAmpActivity.getIndicators().add(tempind);
				} else {

					Set indcators = new HashSet();
					indcators.add(tempind);
					tmpAmpActivity.setIndicators(indcators);
				}
				activity.add(tmpAmpActivity);
				//TODO INDIC tempind.setActivity(activity);
			}
		}
			
			
			if(tempPrgInd.isPrjStatus() 
					&& tempPrgInd.getSelectedActivity() != null
					&& tempPrgInd.getSelectedActivity().size() > 0){
				
				if (selActivity != null && selActivity.size() > 0 ){
					
					Iterator<LabelValueBean> selAct = selActivity.iterator();
				
					while(selAct.hasNext()){
						LabelValueBean 	selActivitys = selAct.next(); 
					AmpActivity tmpAmpActivity = null;
					tmpAmpActivity = (AmpActivity) session.load(AmpActivity.class,
							new Long(selActivitys.getValue()));
					tmpAmpActivity.getIndicators().remove(tempind);
					//TODO INDIC tempind.getActivity().remove(tmpAmpActivity);
			   }
			}
		}
			tx = session.beginTransaction();
			session.saveOrUpdate(tempind);
			tx.commit();

		} catch (Exception ex) {
			logger.error("Unable to get non-default indicators");
			logger.debug("Exception : " + ex);
		}
	}

	@Deprecated
	public static ArrayList getAmpIndicator() {
		Session session = null;
		Query q = null;
		AmpIndicator ampIndicator = null;
		ArrayList Indicator = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select t from " + AmpIndicator.class.getName()
					+ " t order by t.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampIndicator = (AmpIndicator) iter.next();
				Indicator.add(ampIndicator);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp indicators names  from database "
					+ ex.getMessage());
		}
		return Indicator;
	}

	@Deprecated
	public static AllPrgIndicators getAmpIndicator(Long indId) {
		Session session = null;
		AllPrgIndicators tempPrgInd = new AllPrgIndicators();

		try {
			session = PersistenceManager.getRequestDBSession();
			AmpIndicator tempInd = (AmpIndicator) session.load(
					AmpIndicator.class, indId);

			tempPrgInd.setIndicatorId(tempInd.getIndicatorId());

			tempPrgInd.setName(tempInd.getName());
			tempPrgInd.setCode(tempInd.getCode());
			tempPrgInd.setType(tempInd.getType());
			tempPrgInd.setDescription(tempInd.getDescription());
			tempPrgInd.setCreationDate(DateConversion
					.ConvertDateToString(tempInd.getCreationDate()));
			tempPrgInd.setCategory(tempInd.getCategory());
			tempPrgInd.setSector(tempInd.getSectors());
			//TODO INDIC tempPrgInd.setActivity(tempInd.getActivity());
			
//			tempPrgInd.setActualVal(tempInd.getActualVal());
//			tempPrgInd.setActualValComments(tempInd.getActualValComments());
//			tempPrgInd.setActualValDate(tempInd.getActualValDate());
//			tempPrgInd.setBaseVal(tempInd.getBaseVal());
//			tempPrgInd.setBaseValComments(tempInd.getBaseValComments());
//			tempPrgInd.setBaseValDate(tempInd.getBaseValDate());
//			tempPrgInd.setTargetVal(tempInd.getTargetVal());
//			tempPrgInd.setTargetValComments(tempInd.getTargetValComments());
//			tempPrgInd.setTargetValDate(tempInd.getTargetValDate());
//			tempPrgInd.setRevisedTargetVal(tempInd.getRevisedTargetVal());
//			tempPrgInd.setRevisedTargetValComments(tempInd.getRevisedTargetValComments());
//			tempPrgInd.setRevisedTargetValDate(tempInd.getRevisedTargetValDate());
			tempPrgInd.setRisk(tempInd.getRisk());
			tempPrgInd.setIndicatorsCategory(tempInd.getIndicatorsCategory());
			if(tempInd.isDefaultInd()){
				tempPrgInd.setType("global");
			}
			
			session.flush();
		} catch (Exception e) {
			logger.error("Unable to get the specified Indicator");
			logger.debug("Exception : " + e);
		}
		return tempPrgInd;
	}
	
	@Deprecated
	public static Object getAmpMEIndicatorValue(Long indId, Long actId){
		Session session = null;
		Query q = null;
		String queryString = null;
		Object val=null;
		try {
		if(actId!=null){
		session = PersistenceManager.getRequestDBSession();
		queryString = " select val from "
				+ AmpMEIndicatorValue.class.getName()
				+ " val where val.indicator=:indId and val.activityId=:actId";
		q = session.createQuery(queryString);
		q.setLong("indId", indId);
		q.setLong("actId", actId);
		val=q.uniqueResult();
		}
		}
		catch (Exception e) {
			logger.error("Unable to get the specified Indicator");
			logger.debug("Exception : " + e);
		}
		return val;
		
	}

	@Deprecated
	public static AllPrgIndicators getAmpIndicator(Long indId, Long actId) {
		Session session = null;
		AllPrgIndicators tempPrgInd = new AllPrgIndicators();
		AmpIndicator tempInd=null;

		try {
			Object val=getAmpMEIndicatorValue(indId, actId);
			if(val!=null){
			AmpMEIndicatorValue value = (AmpMEIndicatorValue) val;
			tempInd = value.getIndicator();
			tempPrgInd.setActualVal(value.getActualVal());
			tempPrgInd.setActualValComments(value.getActualValComments());
			tempPrgInd.setActualValDate(value.getActualValDate());
			tempPrgInd.setBaseVal(value.getBaseVal());
			tempPrgInd.setBaseValComments(value.getBaseValComments());
			tempPrgInd.setBaseValDate(value.getBaseValDate());
			tempPrgInd.setTargetVal(value.getTargetVal());
			tempPrgInd.setTargetValComments(value.getTargetValComments());
			tempPrgInd.setTargetValDate(value.getTargetValDate());
			tempPrgInd.setRevisedTargetVal(value.getRevisedTargetVal());
			tempPrgInd.setRevisedTargetValComments(value
					.getRevisedTargetValComments());
			tempPrgInd.setRevisedTargetValDate(value.getRevisedTargetValDate());
			}
			else{
				session = PersistenceManager.getRequestDBSession();
				tempInd=(AmpIndicator) session.load(
						AmpIndicator.class, indId);
			}
			tempPrgInd.setIndicatorId(tempInd.getIndicatorId());
			tempPrgInd.setName(tempInd.getName());
			tempPrgInd.setCode(tempInd.getCode());
			tempPrgInd.setRisk(tempInd.getRisk());
			tempPrgInd.setIndicatorsCategory(tempInd.getIndicatorsCategory());
			
		} catch (Exception e) {
			logger.error("Unable to get the specified Indicator");
			logger.debug("Exception : " + e);
		}
		return tempPrgInd;
	}

	@Deprecated
	public static Collection getAllIndicators(Long ampThemeId) {
		Session session = null;
		AmpTheme tempAmpTheme = null;
		Collection themeInd = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			tempAmpTheme = (AmpTheme) session.load(AmpTheme.class, ampThemeId);
			Set themeIndSet = tempAmpTheme.getIndicators();
			Iterator itrIndSet = themeIndSet.iterator();
			while (itrIndSet.hasNext()) {
				AmpIndicator tempThemeInd = (AmpIndicator) itrIndSet.next();
				AmpPrgIndicator tempPrgInd = new AmpPrgIndicator();
				Long ampThemeIndId = tempThemeInd.getIndicatorId();
				tempPrgInd.setIndicatorId(ampThemeIndId);
				tempPrgInd.setName(tempThemeInd.getName());
				tempPrgInd.setCode(tempThemeInd.getCode());
				tempPrgInd.setCreationDate(DateConversion
						.ConvertDateToString(tempThemeInd.getCreationDate()));
				tempPrgInd.setPrgIndicatorValues(ProgramUtil
						.getThemeIndicatorValues(ampThemeIndId));
				themeInd.add(tempPrgInd);
			}
		} catch (Exception ex) {
			logger.error("Exception from getThemeIndicators()  "
					+ ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return themeInd;
	}

	@Deprecated
	public static void deleteIndicatorOLD(Long indId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
			AmpIndicator tempindInd = (AmpIndicator) session.load(
					AmpIndicator.class, indId);

			Collection sect = tempindInd.getSectors();
			if (sect != null && sect.size() > 0) {
				Iterator<AmpSector> sectIter = sect.iterator();
				while (sectIter.hasNext()) {
					AmpSector sector = sectIter.next();
					AmpSector tempAmpSector = null;
					tempAmpSector = (AmpSector) session.load(AmpSector.class,
							sector.getAmpSectorId());
					Set ampThemeSet = new HashSet();
					tempAmpSector.getIndicators().remove(tempindInd);
					tempindInd.getSectors().remove(tempAmpSector);
				}
			}

			//TODO INDIC
//			Collection activity = tempindInd.getActivity();
//			if (activity != null && activity.size() > 0) {
//				Iterator<AmpActivity> actItre = activity.iterator();
//				while (actItre.hasNext()) {
//					AmpActivity activit = actItre.next();
//					AmpActivity tempActivity = null;
//					tempActivity = (AmpActivity) session.load(
//							AmpActivity.class, activit.getAmpActivityId());
//					tempActivity.getIndicators().remove(tempindInd);
//					tempindInd.getActivity().remove(tempActivity);
//				}
//			}

			session.delete(tempindInd);
			tx.commit();
		} catch (Exception e) {
			logger.error("Unable to delete the indicator");
			logger.debug("Exception : " + e);
		}
	}

	public static Collection getAllNonDefaultIndicators() {
		Session session = null;
		Collection col = null;
		Query qry = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select nondefInd from "
					+ AmpIndicator.class.getName()
					+ " nondefInd where nondefInd.defaultInd = false";
			qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get non-default indicators");
			logger.debug("Exception : " + e);
			e.printStackTrace();
		}
		return col;
	}

	public static Collection getAllDefaultIndicators(Long ampActivityId) {
		Session session = null;
		Collection<ActivityIndicator> coll = new ArrayList();
		Query qry = null;
		AmpIndicator ampIndicator = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select nondefInd from "
					+ AmpIndicator.class.getName()
					+ " nondefInd where nondefInd.defaultInd = true";
			qry = session.createQuery(queryString);
			iter = qry.list().iterator();

			while (iter.hasNext()) {
				ampIndicator = (AmpIndicator) iter.next();
				ActivityIndicator actInd = new ActivityIndicator();
				actInd.setIndicatorId(ampIndicator.getIndicatorId());
				Object val=getAmpMEIndicatorValue(ampIndicator.getIndicatorId(), ampActivityId);
				if(val!=null){
					AmpMEIndicatorValue value = (AmpMEIndicatorValue) val;
					actInd.setIndicatorValId(value.getAmpMeIndValId());
					
					
					//================
					// AMP-2828 by mouhamad
			        String dateFormat = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBALSETTINGS_DATEFORMAT);
			        dateFormat = dateFormat.replace("m", "M");
			          
			        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
						
					actInd.setActualVal(value.getActualVal());
					actInd.setActualValComments(value.getActualValComments());
					actInd.setActualValDate(formatter.format(value.getActualValDate()));
					

					actInd.setCurrentVal(value.getActualVal());
					actInd.setCurrentValComments(value.getActualValComments());
					actInd.setCurrentValDate(formatter.format(value.getActualValDate()));
					
					actInd.setBaseVal(value.getBaseVal());
					actInd.setBaseValComments(value.getBaseValComments());
					actInd.setBaseValDate(formatter.format(value.getBaseValDate()));
					actInd.setTargetVal(value.getTargetVal());
					actInd.setTargetValComments(value.getTargetValComments());
					actInd.setTargetValDate(formatter.format(value.getTargetValDate()));
					actInd.setRevisedTargetVal(value.getRevisedTargetVal());
					actInd.setRevisedTargetValComments(value
							.getRevisedTargetValComments());
					actInd.setRevisedTargetValDate(formatter.format(value.getRevisedTargetValDate()));
					if(value.getRisk()!=null){
					actInd.setRisk(value.getRisk().getAmpIndRiskRatingsId());
					}
					actInd.setIndicatorsCategory(value.getIndicatorsCategory());
						
					//	===================
							
					
				}
				actInd.setIndicatorCode(ampIndicator.getCode());
				actInd.setIndicatorName(ampIndicator.getName());
				//actInd.setIndicatorValId(ampIndicator.getIndicatorId() + 1);
				coll.add(actInd);
			}

		} catch (Exception e) {
			logger.error("Unable to get default indicators");
			logger.debug("Exception : " + e);
			e.printStackTrace();
		}
		return coll;
	}
	
	public static Collection getAllDefaultIndicators()
	{
		Session session = null;
		Collection col = new ArrayList();
		Query qry = null;
		try
		{
			session = PersistenceManager.getRequestDBSession();
	
			String queryString = "select defInd from "
				+ AmpIndicator.class.getName()
				+ " defInd where defInd.defaultInd = true";
			
			qry = session.createQuery(queryString);
			col = qry.list();
		}
		catch(Exception e)
		{
			logger.error("Unable to get default indicators");
			logger.debug("Exception : " + e);
		}
		return col;
	}

	@Deprecated
	public static Collection getActivityIndicatorsList(Long ampActivityId) {

		Session session = null;
		Query qry = null;
		AmpActivity tempAmpactivity = null;
		Collection coll = new ArrayList();
		try {
			if(ampActivityId!=null&&ampActivityId>0){
			session = PersistenceManager.getRequestDBSession();
			tempAmpactivity = (AmpActivity) session.load(AmpActivity.class,
					ampActivityId);
			Set activityIndiSet = tempAmpactivity.getIndicators();
			Iterator itrIndSet = activityIndiSet.iterator();
			while (itrIndSet.hasNext()) {
				AmpIndicator tempThemeInd = (AmpIndicator) itrIndSet.next();
				ActivityIndicator actInd = new ActivityIndicator();
				Long ampThemeIndId = tempThemeInd.getIndicatorId();
				actInd.setIndicatorId(ampThemeIndId);
				actInd.setIndicatorCode(tempThemeInd.getCode());
				actInd.setIndicatorName(tempThemeInd.getName());
				Object val=getAmpMEIndicatorValue(ampThemeIndId,ampActivityId);
				if(val!=null){
				AmpMEIndicatorValue value = (AmpMEIndicatorValue) val;
				actInd.setIndicatorValId(value.getAmpMeIndValId());
				
				
				//================
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					
				actInd.setActualVal(value.getActualVal());
				actInd.setActualValComments(value.getActualValComments());
				actInd.setActualValDate(formatter.format(value.getActualValDate()));
				
				actInd.setCurrentVal(value.getActualVal());
				actInd.setCurrentValComments(value.getActualValComments());
				actInd.setCurrentValDate(formatter.format(value.getActualValDate()));
				
				
				actInd.setBaseVal(value.getBaseVal());
				actInd.setBaseValComments(value.getBaseValComments());
				actInd.setBaseValDate(formatter.format(value.getBaseValDate()));
				actInd.setTargetVal(value.getTargetVal());
				actInd.setTargetValComments(value.getTargetValComments());
				actInd.setTargetValDate(formatter.format(value.getTargetValDate()));
				actInd.setRevisedTargetVal(value.getRevisedTargetVal());
				actInd.setRevisedTargetValComments(value
						.getRevisedTargetValComments());
				actInd.setRevisedTargetValDate(formatter.format(value.getRevisedTargetValDate()));
				if(value.getRisk()!=null){
				actInd.setRisk(value.getRisk().getAmpIndRiskRatingsId());
				}
				actInd.setIndicatorsCategory(value.getIndicatorsCategory());
					
				//	===================
						
				
				
				
				
				
				}
				actInd.setActivityId(ampActivityId);
				coll.add(actInd);
			}
			}
		}

		catch (Exception ex) {
			logger.error("Exception from getThemeIndicators()  "
					+ ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return coll;

	}

	@Deprecated
	public static String getIndicatorName(Long id) {
		Session session = null;
		AmpIndicator Ind = null;
		String indName = null;
		String indCode = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			Ind = (AmpIndicator) session.load(AmpIndicator.class, id);
			indName = Ind.getName();

		} catch (Exception ex1) {
			logger.error("Unable to get AmpIndicator object : " + ex1);
		}
		return indName;
	}

	@Deprecated
	public static String getIndicatorCode(Long id) {
		Session session = null;
		AmpIndicator Ind = null;
		String indCode = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			Ind = (AmpIndicator) session.load(AmpIndicator.class, id);
			indCode = Ind.getCode();
		} catch (Exception ex1) {
			logger.error("Unable to get AmpIndicator object : " + ex1);
		}
		return indCode;
	}
	
	
	  public static ArrayList searchForindicator(String sectorname) {
  		Session session = null;
  		
  		Iterator iter =null;
  		AmpIndicator ampIndicator = null;
  		AmpSector ampsector =null;
		ArrayList Indicator = new ArrayList();
  		try {
  			session = PersistenceManager.getRequestDBSession();
  		    String queryString = "select sec from "
                + AmpSector.class.getName() + " sec "
                + "where sec.name=:name";
                Query qry = session.createQuery(queryString);
                qry.setString("name", sectorname);
   	
  		      iter = qry.list().iterator();

			while (iter.hasNext()) {
				ampsector = (AmpSector) iter.next();
				for(Iterator itr = ampsector.getIndicators().iterator();itr.hasNext();){
					AmpIndicator sect=(AmpIndicator)itr.next();
					Indicator.add(sect);
				}
				
			}
   			   
  		} catch (Exception ex) {
  			logger.debug("Unable to search " + ex);
  			}
  		return Indicator;
  	}
	  
	  /**
	   * 
	   * @param keyWord
	   * @param sectorName
	   * @return List of Idicators
	   * @author dare
	   */
	  public static ArrayList searchIndicators(String keyWord,String sectorName){
			Session session = null;
			Query qry = null;
			List retValue=new ArrayList();
			try{
				session=PersistenceManager.getRequestDBSession();
				String queryString=null;			
				
				if (keyWord!=null && keyWord.length()>0){
					if(sectorName!=null && sectorName.length()>0 && !sectorName.equals("-1")){
						queryString="select ind from "+ AmpIndicator.class.getName() +" ind inner join ind.sectors sec where ind.name like '%" + keyWord + "%'"
						+" and sec.name='"+sectorName+"'";
						qry = session.createQuery(queryString);
						retValue=qry.list();
					}else {
						 queryString="select ind from "+ AmpIndicator.class.getName() +" ind where ind.name like '%" + keyWord + "%'";
						 qry = session.createQuery(queryString);
						 retValue=qry.list();
					} 
				}else if (sectorName!=null && sectorName.length()>0 && !sectorName.equals("-1")){
					retValue=searchForindicator(sectorName);
				}			
			}catch (Exception ex) {
				logger.debug("Unable to search " + ex);
				ex.printStackTrace();
			}
			return (ArrayList)retValue;
		}
	  
	  public static void deleteIndsector(Long sectorid,Long indid){
		
		 logger.info(" deleting the indsectors");
			Session session = null;
			Transaction tx = null;
			AmpIndicator ampInd=null;
 	   
 	   try {
 		   session = PersistenceManager.getRequestDBSession();
 		   tx = session.beginTransaction();
 		  AmpIndicator tempindInd = (AmpIndicator) session.load(AmpIndicator.class, indid);
 		  
 		 Collection sect = tempindInd.getSectors();
			if (sect != null && sect.size() > 0) {
				Iterator<AmpSector> sectIter = sect.iterator();
				while (sectIter.hasNext()) {
					AmpSector sector = sectIter.next();
					
					if(sector!=null && sector.getAmpSectorId().equals(sectorid)){
					
						AmpSector tempAmpSector = null;
						tempAmpSector = (AmpSector) session.load(AmpSector.class,sector.getAmpSectorId());
						Set ampThemeSet = new HashSet();
						tempAmpSector.getIndicators().remove(tempindInd);
						tempindInd.getSectors().remove(tempAmpSector);
					}
	 		  }
		 }
			session.update(tempindInd);
	 		tx.commit();
	 		session.flush();
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception from deleteIndSectors:" + e.getMessage());
			e.printStackTrace(System.out);
		}
	}
	
	  @Deprecated
	public static void deleteIndtheme(Long indid){
		
		 logger.info(" deleting the indthemes");
			Session session = null;
			Transaction tx = null;
			AmpIndicator ampInd=null;
	   
	   try {
		   session = PersistenceManager.getRequestDBSession();
		   tx = session.beginTransaction();
		  AmpIndicator tempindInd = (AmpIndicator) session.load(AmpIndicator.class, indid);

		  //TODO INDIC
//		 Collection theme = tempindInd.getThemes();
//			if (theme != null && theme.size() > 0) {
//				Iterator<AmpTheme> themeItr = theme.iterator();
//				while (themeItr.hasNext()) {
//					AmpTheme themeInd = themeItr.next();
//					
//					if(themeInd!=null){
//					
//						AmpTheme tempAmpTheme = null;
//						tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,themeInd.getAmpThemeId());
//						Set ampThemeSet = new HashSet();
//						tempAmpTheme.getIndicators().remove(tempindInd);
//						tempindInd.getThemes().remove(tempAmpTheme);
//					}
//	 		  }
//		 }
			session.update(tempindInd);
	 		tx.commit();
	 		session.flush();
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception from deleteIndSectors:" + e.getMessage());
			e.printStackTrace(System.out);
		}
	}
	
	@Deprecated
	 public static AmpIndicator getIndicatorById(Long indId){
         Session session = null;
         AmpIndicator tempInd = new AmpIndicator();

         try{
             session = PersistenceManager.getRequestDBSession();
             tempInd = (AmpIndicator) session.load(AmpIndicator.class,indId);
             session.flush();
         }
         catch(Exception e){
             logger.error("Unable to get the specified Indicator");
             logger.debug("Exception : "+e);
         }
         return tempInd;
		}
	 
	 @Deprecated
	 public static void saveEditPrgIndValues(Collection<AmpPrgIndicatorValue> prgIndValues,AmpIndicator ampInd)
		{
			Session session = null;
			Transaction tx = null;
			try
			{
				session = PersistenceManager.getRequestDBSession();
				Iterator indValItr = prgIndValues.iterator();
				while(indValItr.hasNext())
				{
					AmpThemeIndicatorValue ampThIndVal = null;
					AmpPrgIndicatorValue ampPrgIndVal = (AmpPrgIndicatorValue) indValItr.next();
					if(ampPrgIndVal.getIndicatorValueId() == null){
						ampThIndVal = new AmpThemeIndicatorValue();
					}else{
												ampThIndVal = (AmpThemeIndicatorValue) session.load(AmpThemeIndicatorValue.class,ampPrgIndVal.getIndicatorValueId());
					}
					ampThIndVal.setValueAmount(ampPrgIndVal.getValAmount());
					ampThIndVal.setCreationDate(DateConversion.getDate(ampPrgIndVal.getCreationDate()));
					ampThIndVal.setValueType(ampPrgIndVal.getValueType());
					ampThIndVal.setIndicatorId(ampInd);
					tx = session.beginTransaction();
					session.saveOrUpdate(ampThIndVal);
					tx.commit();
					}
				}
			
			catch(Exception ex)
			{
				
		             logger.error("Unable to get the specified Indicator");
		             logger.debug("Exception : "+ex);
			}
			
		}
	 
	  
}
