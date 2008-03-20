/*
 * ProgramUtil.java
 */

package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.kernel.util.collections.HierarchyMember;
import org.digijava.kernel.util.collections.HierarchyMemberFactory;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicatorValue;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AllMEIndicators;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AllThemes;
import org.digijava.module.aim.helper.AmpIndSectors;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.EditProgram;
import org.digijava.module.aim.helper.IndicatorsBean;
import org.digijava.module.aim.helper.TreeItem;
import org.digijava.module.translation.util.DbUtil;


public class ProgramUtil {

		private static Logger logger = Logger.getLogger(ProgramUtil.class);
		public static final int YAERS_LIST_START = 2000;
                public static final String NATIONAL_PLAN_OBJECTIVE =
                    "National Plan Objective";
                public static final String PRIMARY_PROGRAM = "Primary Program";
                public static final String SECONDARY_PROGRAM = "Secondary Program";
                public static final int NATIONAL_PLAN_OBJECTIVE_KEY = 1;
                public static final int PRIMARY_PROGRAM_KEY = 2;
                public static final int SECONDARY_PROGRAM_KEY = 3;




		public static Collection getAllIndicatorsFromPrograms(Collection programs) throws AimException{
			try {
				Collection result= null;
				if (programs!= null && programs.size() >0){
					result = new ArrayList();
					long t = 1;
					for (Iterator iter = programs.iterator(); iter.hasNext();) {
						AmpTheme OldProg = (AmpTheme) iter.next();
						AmpTheme prog= getThemeObject(OldProg.getAmpThemeId());
						Set indicators = prog.getIndicators();
						if (indicators!=null && indicators.size()>0){
							for (Iterator indicIter = indicators.iterator(); indicIter.hasNext();) {
								AmpThemeIndicators indicator = (AmpThemeIndicators) indicIter.next();
								ActivityIndicator aiBean = new ActivityIndicator();
								aiBean.setIndicatorId(indicator.getAmpThemeIndId());

								aiBean.setIndicatorName(indicator.getName());
								aiBean.setIndicatorCode(indicator.getCode());
								aiBean.setIndicatorValId(new Long(t*1));
								aiBean.setActivityId(new Long(t*1));
								aiBean.setDefaultInd(false);
								++t;
								result.add(aiBean);
							}
						}
					}
				}
				return result;

			} catch (Exception e) {
				logger.error(e);
				throw new AimException("Cannot get programs for activity.",e);
			}

		}



		public static AmpTheme getTheme(String name) {
			Session session = null;
			AmpTheme theme = null;

			try {
				session = PersistenceManager.getSession();
				String qryStr = "select theme from " + AmpTheme.class.getName()
						+ " theme where (theme.name=:name)";
				Query qry = session.createQuery(qryStr);
				qry.setParameter("name", name, Hibernate.STRING);
				Iterator itr = qry.list().iterator();
				if (itr.hasNext()) {
					theme = (AmpTheme) itr.next();
				}
			} catch (Exception e) {
				logger.error("Exception from getTheme()");
				logger.error(e.getMessage());
			} finally {
				if (session != null) {
					try {
						PersistenceManager.releaseSession(session);
					} catch (Exception rsf) {
						logger.error("Release session failed");
					}
				}
			}
			return theme;
		}

		/**
         * Returns AmpThemeIndicators.
         * @param keyword String -
         * @return Coll AmpThemeIndicators
         */
	
		@Deprecated
		public static Collection getThemeindicators(String keyword) {
			Session session = null;
			Query qry = null;
			Collection ThemeIndicators = new ArrayList();
			keyword=keyword.toLowerCase();

			try {
				  session = PersistenceManager.getRequestDBSession();
				String queryString = "select t from " + AmpThemeIndicators.class.getName()
						+ " t where lower(name) like '%"+keyword+"%'";
				qry = session.createQuery(queryString);
				ThemeIndicators = qry.list();
			} catch (Exception e) {
				logger.error("Unable to get all themes : "+e);
				logger.debug("Exceptiion " + e);
			} 
			return ThemeIndicators;
		}
		
		/**
		 * Load theme by ID.
		 * @param ampThemeId
		 * @return
		 * @throws DgException
		 */
        public static AmpTheme getThemeById(Long ampThemeId) throws DgException {
			Session session = PersistenceManager.getRequestDBSession();
			AmpTheme theme = null;
	
			try {
				theme = (AmpTheme) session.load(AmpTheme.class, ampThemeId);
			} catch (ObjectNotFoundException e) {
				logger.debug("AmpTheme with id " + ampThemeId + " not found");
			} catch (Exception e) {
				throw new DgException("Cannot load AmpTheme with id " + ampThemeId,
						e);
			}
			return theme;
		}

        /**
         * Retrieves top level themes.
         * @return
         * @throws DgException
         */
		@SuppressWarnings("unchecked")
		public static Collection<AmpTheme> getParentThemes() throws DgException{
			Session session = PersistenceManager.getRequestDBSession();
			Query qry = null;
			Collection<AmpTheme> themes = new ArrayList<AmpTheme>();

			try {
				String queryString = "select t from " + AmpTheme.class.getName()
						+ " t where t.parentThemeId is null";
				qry = session.createQuery(queryString);
				themes = qry.list();
			} catch (Exception e) {
				throw new DgException("Cannot search parent themes",e);
			}
			return themes;
		}

		/**
		 * Retrieves all themes from db.
		 * @return
		 * @throws DgException
		 */
		@SuppressWarnings("unchecked")
		public static List<AmpTheme> getAllThemes() throws DgException{
			Session session = null;
			Query qry = null;
			List<AmpTheme> themes = new ArrayList<AmpTheme>();

			try {
				session = PersistenceManager.getRequestDBSession();
				String queryString = " from " + AmpTheme.class.getName()
						+ " t where t.parentThemeId is null";
				qry = session.createQuery(queryString);
				themes = qry.list();
			} catch (Exception e) {
				throw new DgException("Cannot retrive all themes from db",e);
			}
			return themes;
		}

        /**
         * Returns All AmpThemes including sub Themes if parameter is true.
         * @param includeSubThemes boolean false - only top level Themes, true - all themes
         * @return List AmpTheme
         */
        @SuppressWarnings("unchecked")
		public static List<AmpTheme> getAllThemes(boolean includeSubThemes) throws DgException{
            Session session = null;
            Query qry = null;
            List<AmpTheme> themes = new ArrayList<AmpTheme>();

            try {
                session = PersistenceManager.getRequestDBSession();
                String queryString = "select t from " + AmpTheme.class.getName()+ " t ";
                if (!includeSubThemes) {
                    queryString += "where t.parentThemeId is null ";
                }
                qry = session.createQuery(queryString);
                themes = qry.list();
            }
            catch (Exception e) {
            	throw new DgException("Cannot load themes hierarchy",e);
            }
            return themes;
        }

//        public static Collection searchForindicators(String keyword,String sectorname) {
//    		Session session = null;
//    		Collection col = null;
//
//    		try {
//    			session = PersistenceManager.getRequestDBSession();
//    			String queryString = "select t from "
//    					+ AmpThemeIndicators.class.getName() + " t "
//    					+ "where t.ampThemeIndId in (select t1.themeIndicatorId from " +
//    					AmpIndicatorSector.class.getName() + " t1 join t1.sectorId si "+
//    					"where si.name = '"+sectorname+"')" +
//    					"and t.name like '%" + keyword + "%'";
//    			Query qry = session.createQuery(queryString);
//    			col = qry.list();
//    		} catch (Exception ex) {
//    			logger.debug("Unable to search " + ex);
//    			}
//    		return col;
//    	}


        public static Collection searchForindicator(String sectorname) {
    		Session session = null;
    		Collection col = null;

    		try {
    			session = PersistenceManager.getRequestDBSession();
    			String queryString = "select t from "
    					+ AmpThemeIndicators.class.getName() + " t "
    					+ "where t.ampThemeIndId in (select t1.themeIndicatorId from " +
    					AmpIndicatorSector.class.getName() + " t1 join t1.ampSectorId si "+
    					  "where upper(si.name) like '"+sectorname.toUpperCase()+"')";
    			Query qry = session.createQuery(queryString);
    			col = qry.list();
    		} catch (Exception ex) {
    			logger.debug("Unable to search " + ex);
    			}
    		return col;
    	}

        @Deprecated
        public static ArrayList getAmpThemeIndicators() {
    		Session session = null;
    		Query q = null;
    		AmpThemeIndicators ampThemeIndicators = null;
    		ArrayList themeIndicators = new ArrayList();
    		String queryString = null;
    		Iterator iter = null;

    		try {
    			session = PersistenceManager.getRequestDBSession();
    			queryString = " select t from " + AmpThemeIndicators.class.getName()
    					+ " t order by t.name";
    			q = session.createQuery(queryString);
    			iter = q.list().iterator();

    			while (iter.hasNext()) {
    				ampThemeIndicators = (AmpThemeIndicators) iter.next();
    				themeIndicators.add(ampThemeIndicators);
    			}

    		} catch (Exception ex) {
    			logger.error("Unable to get Amp indicators names  from database "
    					+ ex.getMessage());
    			}
    		return themeIndicators;
    	}

        /**
         * Returns list of years for drop-down list.
         * @return
         */
        public static Collection<LabelValueBean> getYearsBeanList(){
            return getYearsBeanList(YAERS_LIST_START);
        }

        public static Collection<LabelValueBean> getYearsBeanList(int from){
            Collection<LabelValueBean> result=new ArrayList<LabelValueBean>();
            int start=from;
            Calendar now=Calendar.getInstance();
            int end=now.get(Calendar.YEAR);
            for (int i = start; i <= end; i++) {
                result.add(new LabelValueBean(String.valueOf(i),String.valueOf(i)));
            }
            return result;
        }

        @Deprecated
	    public static Collection getAllThemeIndicators() throws AimException
	    {
	    	Collection colThInd = new ArrayList();
	    	Collection colTh = null;
	    	colTh = getAllPrograms();
	    	Iterator itrColTh = colTh.iterator();
	    	while(itrColTh.hasNext())
	    	{
	    		AmpTheme ampTh1 = (AmpTheme) itrColTh.next();
	    		AllThemes tempAllThemes = new AllThemes();
				tempAllThemes.setProgramId(ampTh1.getAmpThemeId());
				tempAllThemes.setProgramName(ampTh1.getName());
	    		Collection allInds = new ArrayList();
	    		Iterator itr = getThemeIndicators(ampTh1.getAmpThemeId()).iterator();
	    		while(itr.hasNext())
	    		{
	    			AmpPrgIndicator ampPrg = (AmpPrgIndicator) itr.next();
	    			AllPrgIndicators prgInd = new AllPrgIndicators();
					prgInd.setIndicatorId(ampPrg.getIndicatorId());
					prgInd.setName(ampPrg.getName());
					prgInd.setCode(ampPrg.getCode());
					prgInd.setType(ampPrg.getType());
					prgInd.setCreationDate(ampPrg.getCreationDate());
					prgInd.setCategory(ampPrg.getCategory());
					prgInd.setNpIndicator(ampPrg.isNpIndicator());
					prgInd.setSector(ampPrg.getIndSectores());
					allInds.add(prgInd);

	    		}
	    		tempAllThemes.setAllPrgIndicators(allInds);
	    		colThInd.add(tempAllThemes);
	    	}
	    	return colThInd;
	    }

        // New function added for the program Indicator Manager by pcsingh

        @Deprecated
        public static Collection getAllThemesIndicators() throws AimException
	    {
	    	Collection colThInd = new ArrayList();
	    	Collection colTh = null;
	    	colTh = getAllMainPrograms();
	    	Iterator itrColTh = colTh.iterator();
	    	while(itrColTh.hasNext())
	    	{
	    		AmpTheme ampTh1 = (AmpTheme) itrColTh.next();
	    		AllThemes tempAllThemes = new AllThemes();
				tempAllThemes.setProgramId(ampTh1.getAmpThemeId());
				tempAllThemes.setProgramName(ampTh1.getName());
	    		Collection allInds = new ArrayList();
	    		Iterator itr = getThemeIndicators(ampTh1.getAmpThemeId()).iterator();
	    		while(itr.hasNext())
	    		{
	    			AmpPrgIndicator ampPrg = (AmpPrgIndicator) itr.next();
	    			AllPrgIndicators prgInd = new AllPrgIndicators();
					prgInd.setIndicatorId(ampPrg.getIndicatorId());
					prgInd.setName(ampPrg.getName());
					prgInd.setCode(ampPrg.getCode());
					prgInd.setType(ampPrg.getType());
					prgInd.setCreationDate(ampPrg.getCreationDate());
					prgInd.setCategory(ampPrg.getCategory());
					prgInd.setNpIndicator(ampPrg.isNpIndicator());
					allInds.add(prgInd);
	    		}
	    		tempAllThemes.setAllPrgIndicators(allInds);
	    		colThInd.add(tempAllThemes);
	    	}
	    	return colThInd;
	    }

        // New function added for the program Indicator Manager by pcsingh
	    public static Collection getAllMainPrograms() throws AimException
        {
        	Session session = null;
        	Query qry = null;
        	Collection colPrg = null;
        	try
        	{
        		session = PersistenceManager.getRequestDBSession();
    			String queryString = " from "
    								+ AmpTheme.class.getName() + " th";
    			qry = session.createQuery(queryString);
    			colPrg = qry.list();
        	}
        	catch(Exception ex)
        	{
        		logger.error("Unable to get all the Themes");
    			logger.debug("Exception " + ex);
    			throw new AimException(ex);
        	}
        	Collection mainProgram = new ArrayList();;
        	Iterator itr = colPrg.iterator();
        	while(itr.hasNext()) {
        		AmpTheme tmpTheme = (AmpTheme)itr.next();
        		if(tmpTheme.getParentThemeId()==null || tmpTheme.getParentThemeId().getAmpThemeId().intValue()==0) {
        			mainProgram.add(tmpTheme);
        		}
        	}
        	return mainProgram;
        }


        public static Collection getAllPrograms() throws AimException
        {
        	Session session = null;
        	Query qry = null;
        	Collection colPrg = null;
        	try
        	{
        		session = PersistenceManager.getRequestDBSession();
    			String queryString = " from "
    								+ AmpTheme.class.getName() + " th";
    			qry = session.createQuery(queryString);
    			colPrg = qry.list();
        	}
        	catch(Exception ex)
        	{
        		logger.error("Unable to get all the Themes");
    			logger.debug("Exception " + ex);
    			throw new AimException(ex);
        	}
        	return colPrg;
        }

        @Deprecated
        public static Collection getAllProgramIndicators()
        {
        	Session session = null;
        	Query qry = null;
        	Collection colInd = null;
        	try
        	{
        		session = PersistenceManager.getRequestDBSession();
        		String queryString = " from "
        							+ AmpThemeIndicators.class.getName() + " thInd";
        		qry = session.createQuery(queryString);
        		colInd = qry.list();
        	}
        	catch(Exception ex)
        	{
        		logger.error("Unable to get all the Indicators of Themes");
    			logger.debug("Exception " + ex);
        	}
        	return colInd;
        }

	    public static ArrayList getThemesByIds(ArrayList ampThemeIds) {
	        Session session = null;
	        Query qry = null;

	        try {
	            session = PersistenceManager.getRequestDBSession();
	            String qryStr = "select t from " + AmpTheme.class.getName()
	                + " t where t.ampThemeId in (:ids)";
	            qry = session.createQuery(qryStr);
	            qry.setParameterList("ids", ampThemeIds);
	            return (ArrayList) qry.list();
	        } catch(Exception e) {
	            logger.error("Unable to get all themes" + e);
	            e.printStackTrace(System.out);
	        }
	        return null;
		}

		public static AmpTheme getThemeObject(Long ampThemeId) {
			Session session = null;
	        AmpTheme ampTheme = new AmpTheme();
	        try {
	        	session = PersistenceManager.getRequestDBSession();
	            ampTheme = (AmpTheme) session.load(AmpTheme.class, ampThemeId);
	        }
	        catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	        return ampTheme;
		}


		public static String getHierarchyName(AmpTheme prog){
			String result="";
			List<AmpTheme> progs=new ArrayList<AmpTheme>();
			AmpTheme curProg = prog;
			while (curProg.getParentThemeId()!=null) {
				curProg = curProg.getParentThemeId();
				progs.add(curProg);
			}


			Collections.reverse(progs);

			for (ListIterator<AmpTheme> iterator = progs.listIterator(); iterator.hasNext();) {
				AmpTheme p = (AmpTheme) iterator.next();
				result += p.getName() + " > ";
			}

			result += prog.getName();
			return result;
		}



	/* Commemted by pcsingh
	 * due to some doubt abt assignment of indicator from one theme to other
	 * doubt are like what should be default values, same indicator can be
	 * assign to mant theme or not etc.
	 * Note: same functions are written twice for future use

		public static void assignThemeInd(Long indId, Long themeId){
			Session session = null;
			Transaction tx = null;
			AmpThemeIndicators ampThInd = null;
			AmpTheme ampTh = getThemeObject(themeId);
			try{
				session = PersistenceManager.getRequestDBSession();
				ampThInd = (AmpThemeIndicators)session.load(AmpThemeIndicators.class, indId);
				logger.info("Inside assignIndacators:::\n"+
						"ampThemeIndicators.getName"+ampThInd.getName()+"\n\n\n");
				Iterator getTheme = ampThInd.getThemes().iterator();
				while(getTheme.hasNext()){
					AmpTheme tmpAmpTheme = (AmpTheme)getTheme.next();
					logger.info("Getting thems related to given indicator::::"+
							"tmpAmpTheme.getName="+tmpAmpTheme.getName());
				}
				logger.info("\n\n\n");

				logger.info("Current theme name:::="+ampTh.getName());
				Iterator itr = ampTh.getIndicators().iterator();
				while(itr.hasNext()){
					AmpThemeIndicators tmpAmpThemeInd = (AmpThemeIndicators)itr.next();
					logger.info("\nGetting all Indicators related to theme::::\n"
							+"tmpAmpThemeIndicators.getName="+tmpAmpThemeInd.getName());
				}
				logger.info("\n\n\n");

			}catch(Exception ex){
				logger.error("Exception from assignThemeIndicator  "+ex.getMessage());
				ex.printStackTrace(System.out);
			}
		}
*/
	
		@Deprecated
		public static void assignThemeInd(Long indId, Long themeId)
		{

//			logger.info("\n\nInside program Util\n"+indId+"      "+themeId);
//			Session session = null;
//			Transaction tx = null;
//			AmpIndicator ampThInd = null;
//			AmpTheme ampTh = getThemeObject(themeId);
//			Set tmpTest = ampTh.getIndicators();
//			try
//			{
//				session = PersistenceManager.getRequestDBSession();
//				ampThInd = (AmpIndicator) session.load(AmpIndicator.class, indId);
//				logger.info(ampThInd.getName());
//				Set tempTh = ampThInd.getThemes();
//				Iterator itr = tempTh.iterator();
//				while(itr.hasNext()){
//					AmpTheme tmp = (AmpTheme)itr.next();
//					logger.info("Themes related to indicator\n\n"+tmp.getName());
//				}
//				tempTh.clear();
//				logger.info(tempTh.isEmpty());
//				tempTh.add(ampTh);
//				itr = tempTh.iterator();
//				while(itr.hasNext()){
//					AmpTheme tmp = (AmpTheme)itr.next();
//					logger.info("Now Themes related to indicator\n\n"+tmp.getName());
//				}
//				logger.info(tempTh.isEmpty());
				//ampThInd.setThemes(ampThInd);
//				ampThInd.setThemes(tempTh);

//				tmpTest.add(ampThInd);
//				ampTh.setIndicators(tmpTest);
//				tx = session.beginTransaction();
//				session.saveOrUpdate(ampThInd);
//				tx.commit();
//			}
//			catch(Exception ex) {
//				logger.error("Exception from getThemeIndicators()  " + ex.getMessage());
//				ex.printStackTrace(System.out);
//			}
		}
 //Comment over pcsing
		
		public static Collection getSectorIndicator(Long themeIndicatorId)
		{
			Session session = null;
			Collection col = new ArrayList();
			try
			{
				session = PersistenceManager.getRequestDBSession();
				String queryString = "select SectInd from "
									+ AmpIndicatorSector.class.getName()
									+ " SectInd where (SectInd.themeIndicatorId=:themeIndicatorId)";
				Query qry = session.createQuery(queryString);
				qry.setParameter("themeIndicatorId",themeIndicatorId,Hibernate.LONG);
				Iterator indItr = qry.list().iterator();
				while(indItr.hasNext())
				{
					AmpIndicatorSector IndSector = (AmpIndicatorSector) indItr.next();
					AmpIndSectors Indsectors = new AmpIndSectors();
					Indsectors.setAmpIndicatorSectorId(IndSector.getAmpIndicatorSectorId());
					Indsectors.setSectorId(IndSector.getSectorId());
					//Indsectors.setThemeIndicatorId(IndSector.getThemeIndicatorId());
					col.add(Indsectors);

				}
			}
			catch(Exception e1) {
				logger.error("Error in retrieving the values for indicator with ID : "+themeIndicatorId);
				logger.debug("Exception : "+e1);
                e1.printStackTrace();
			}
			return col;
		}

		@Deprecated
		public static Collection getThemeIndicators(Long ampThemeId)
		{
			Session session = null;
			AmpTheme tempAmpTheme = null;
			Collection themeInd = new ArrayList();

			try
			{
				session = PersistenceManager.getRequestDBSession();
				tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,ampThemeId);
				Set themeIndSet = tempAmpTheme.getIndicators();
				Iterator itrIndSet = themeIndSet.iterator();
				while(itrIndSet.hasNext())
				{
					AmpThemeIndicators tempThemeInd = (AmpThemeIndicators) itrIndSet.next();
					AmpPrgIndicator tempPrgInd = new AmpPrgIndicator();
					Long ampThemeIndId = tempThemeInd.getAmpThemeIndId();
					tempPrgInd.setIndicatorId(ampThemeIndId);
					tempPrgInd.setName(tempThemeInd.getName());
					tempPrgInd.setCode(tempThemeInd.getCode());
                    tempPrgInd.setCreationDate(DateConversion.ConvertDateToString(tempThemeInd.getCreationDate()));
					tempPrgInd.setPrgIndicatorValues(getThemeIndicatorValues(ampThemeIndId));
					tempPrgInd.setIndSectores(getSectorIndicator(ampThemeIndId));
					themeInd.add(tempPrgInd);
				}
			}
			catch(Exception ex) {
				logger.error("Exception from getThemeIndicators()  " + ex.getMessage());
				ex.printStackTrace(System.out);
			}
			return themeInd;
		}
                
      public static Collection getProgramIndicators(Long programId)
			throws DgException {
		Set indicators = new HashSet();
		ArrayList programs = (ArrayList) getRelatedThemes(programId);
		if (programs != null) {
			Iterator<AmpTheme> iterProgram = programs.iterator();
			while (iterProgram.hasNext()) {
				AmpTheme program = iterProgram.next();
				Set inds = IndicatorUtil.getIndicatorThemeConnections(program);
				if (inds != null) {
					indicators.addAll(inds);
				}
			}
		}
		return indicators;
	}
                
		@Deprecated
		public static Collection getThemeIndicatorValues(Long themeIndicatorId)
		{
			Session session = null;
			Collection col = new ArrayList();
			try
			{
				session = PersistenceManager.getRequestDBSession();
				String queryString = "select thIndValId from "
									+ AmpThemeIndicatorValue.class.getName()
									+ " thIndValId where (thIndValId.indicatorId=:indicatorId)";
				Query qry = session.createQuery(queryString);
				qry.setParameter("indicatorId",themeIndicatorId,Hibernate.LONG);
				Iterator indItr = qry.list().iterator();
				while(indItr.hasNext())
				{
					AmpThemeIndicatorValue tempThIndVal = (AmpThemeIndicatorValue) indItr.next();
					AmpPrgIndicatorValue tempPrgIndVal = new AmpPrgIndicatorValue();
					tempPrgIndVal.setIndicatorValueId(tempThIndVal.getAmpThemeIndValId());
					tempPrgIndVal.setValueType(tempThIndVal.getValueType());
					tempPrgIndVal.setValAmount(tempThIndVal.getValueAmount());
					tempPrgIndVal.setCreationDate(DateConversion.ConvertDateToString(tempThIndVal.getCreationDate()));
					col.add(tempPrgIndVal);
				}
			}
			catch(Exception e1) {
				logger.error("Error in retrieving the values for indicator with ID : "+themeIndicatorId);
				logger.debug("Exception : "+e1);
			}
			return col;
		}

		@Deprecated
        public static Collection getThemeIndicatorValuesDB(Long themeIndicatorId)
        {
                Session session = null;
                Collection col = null;
                try
                {
                        session = PersistenceManager.getRequestDBSession();
                        String queryString = "select thIndValId from "
                                                                + AmpThemeIndicatorValue.class.getName()
                                                                + " thIndValId where (thIndValId.themeIndicatorId=:themeIndicatorId)";
                        Query qry = session.createQuery(queryString);
                        qry.setParameter("themeIndicatorId",themeIndicatorId,Hibernate.LONG);
                        col = qry.list();
                }
                catch(Exception e1) {
                        logger.error("Error in retrieving the values for indicator with ID : "+themeIndicatorId);
                        logger.debug("Exception : "+e1);
                }
                return col;
        }

		//WWWTTTFFFF!!!!????!!!   
		public static Collection getAllSubThemes(Long parentThemeId)
		{
			Session session = null;
			Query qry = null;
			Collection tempCol1 = new ArrayList();
			Collection tempCol2 = new ArrayList();
			Collection tempCol3 = new ArrayList();
			Collection tempCol4 = new ArrayList();
			Collection tempCol5 = new ArrayList();
			Collection tempCol6 = new ArrayList();
			Collection tempCol7 = new ArrayList();
			Collection tempCol8 = new ArrayList();
			Collection allSubThemes = new ArrayList();
			try
			{
				session = PersistenceManager.getRequestDBSession();
				// level 1 starts
				String queryString1 = "select subT from " +AmpTheme.class.getName()
									+ " subT where (subT.parentThemeId=:parentThemeId)";
				qry = session.createQuery(queryString1);
				qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
				tempCol1 = qry.list();
				if(!tempCol1.isEmpty())
				{
					Iterator tempItrCol1 = tempCol1.iterator();
					while(tempItrCol1.hasNext())
					{
						AmpTheme ampTheme1 = (AmpTheme) tempItrCol1.next();
						parentThemeId = ampTheme1.getAmpThemeId();
						allSubThemes.add(ampTheme1);
						//	level 2 starts
						String queryString2 = "select subT from " +AmpTheme.class.getName()
											+ " subT where (subT.parentThemeId=:parentThemeId)";
						qry = session.createQuery(queryString2);
						qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
						tempCol2 = qry.list();
						if(!tempCol2.isEmpty())
						{
							Iterator tempItrCol2 = tempCol2.iterator();
							while(tempItrCol2.hasNext())
							{
								AmpTheme ampTheme2 = (AmpTheme) tempItrCol2.next();
								parentThemeId = ampTheme2.getAmpThemeId();
								allSubThemes.add(ampTheme2);
								//	level 3 starts
								String queryString3 = "select subT from " +AmpTheme.class.getName()
													+ " subT where (subT.parentThemeId=:parentThemeId)";
								qry = session.createQuery(queryString3);
								qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
								tempCol3 = qry.list();
								if(!tempCol3.isEmpty())
								{
									Iterator tempItrCol3 = tempCol3.iterator();
									while(tempItrCol3.hasNext())
									{
										AmpTheme ampTheme3 = (AmpTheme) tempItrCol3.next();
										parentThemeId = ampTheme3.getAmpThemeId();
										allSubThemes.add(ampTheme3);
										//	level 4 starts
										String queryString4 = "select subT from " +AmpTheme.class.getName()
															+ " subT where (subT.parentThemeId=:parentThemeId)";
										qry = session.createQuery(queryString4);
										qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
										tempCol4 = qry.list();
										if(!tempCol4.isEmpty())
										{
											Iterator tempItrCol4 = tempCol4.iterator();
											while(tempItrCol4.hasNext())
											{
												AmpTheme ampTheme4 = (AmpTheme) tempItrCol4.next();
												parentThemeId = ampTheme4.getAmpThemeId();
												allSubThemes.add(ampTheme4);
												//	level 5 starts
												String queryString5 = "select subT from " +AmpTheme.class.getName()
																	+ " subT where (subT.parentThemeId=:parentThemeId)";
												qry = session.createQuery(queryString5);
												qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
												tempCol5 = qry.list();
												if(!tempCol5.isEmpty())
												{
													Iterator tempItrCol5 = tempCol5.iterator();
													while(tempItrCol5.hasNext())
													{
														AmpTheme ampTheme5 = (AmpTheme) tempItrCol5.next();
														parentThemeId = ampTheme5.getAmpThemeId();
														allSubThemes.add(ampTheme5);
														//	level 6 starts
														String queryString6 = "select subT from " +AmpTheme.class.getName()
																			+ " subT where (subT.parentThemeId=:parentThemeId)";
														qry = session.createQuery(queryString6);
														qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
														tempCol6 = qry.list();
														if(!tempCol6.isEmpty())
														{
															Iterator tempItrCol6 = tempCol6.iterator();
															while(tempItrCol6.hasNext())
															{
																AmpTheme ampTheme6 = (AmpTheme) tempItrCol6.next();
																parentThemeId = ampTheme6.getAmpThemeId();
																allSubThemes.add(ampTheme6);
																//	level 7 starts
																String queryString7 = "select subT from " +AmpTheme.class.getName()
																					+ " subT where (subT.parentThemeId=:parentThemeId)";
																qry = session.createQuery(queryString7);
																qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
																tempCol7 = qry.list();
																if(!tempCol7.isEmpty())
																{
																	Iterator tempItrCol7 = tempCol7.iterator();
																	while(tempItrCol7.hasNext())
																	{
																		AmpTheme ampTheme7 = (AmpTheme) tempItrCol7.next();
																		parentThemeId = ampTheme7.getAmpThemeId();
																		allSubThemes.add(ampTheme7);
																		//	level 8 starts
																		String queryString8 = "select subT from " +AmpTheme.class.getName()
																							+ " subT where (subT.parentThemeId=:parentThemeId)";
																		qry = session.createQuery(queryString8);
																		qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
																		tempCol8 = qry.list();
																		if(!tempCol8.isEmpty())
																		{
																			Iterator tempItrCol8 = tempCol8.iterator();
																			while(tempItrCol8.hasNext())
																			{
																				AmpTheme ampTheme8 = (AmpTheme) tempItrCol8.next();
																				allSubThemes.add(ampTheme8);
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			catch(Exception e1) {
				logger.error("Unable to get all the Sub-Themes");
				logger.debug("Exception : "+e1);
			}
			return allSubThemes;
		}

		/**
		 * Returns all sub themes of specified one.
		 * @param parentThemeId
		 * @return
		 */
		public static Collection<AmpTheme> getSubThemes(Long parentThemeId) throws DgException
		{
			Session session = session = PersistenceManager.getRequestDBSession();
			Query qry = null;
			Collection<AmpTheme> subThemes = null;
			try
			{
			    String queryString = "from " + AmpTheme.class.getName() +
			        " subT where subT.parentThemeId=:parentThemeId";
			    qry = session.createQuery(queryString);
			    qry.setParameter("parentThemeId",parentThemeId);
			    subThemes = qry.list();
			}
			catch(Exception e1)
			{
				throw new DgException("Cannot seacr sub themes for theme with id="+parentThemeId,e1);
			}
			return subThemes;
		}

		/**
		 * Return all subchildren in the tree
		 * Recursively iterates on all child programs till the end of the branch.
		 * @param parentThemeId db ID of the parent program
		 * @return collection of AmpTheme beans
		 * @throws AimException if enything goes wrong
		 */
		public static Collection getAllSubThemesFor(Long parentThemeId) throws AimException
		{
			Collection subThemes = new ArrayList();
			try
			{
				Collection progs = getSubThemes(parentThemeId);
				if (progs != null && progs.size()>0){
					subThemes.addAll(progs);
					for (Iterator iter = progs.iterator(); iter.hasNext();) {
						AmpTheme child = (AmpTheme) iter.next();
						Collection col = getAllSubThemes(child.getAmpThemeId());
						subThemes.addAll(col);
					}
				}
			}
			catch(Exception e1)
			{
				logger.error("Unable to get all the sub-themes");
				logger.debug("Exception : "+e1);
				throw new AimException("Cannot get sub themes for "+parentThemeId,e1);
			}
			return subThemes;
		}

		/**
		 * @deprecated use {@link IndicatorUtil} methods. 
		 * @param allPrgInd
		 * @param ampThemeId
		 */
		@Deprecated
		public static void saveEditThemeIndicators(AllPrgIndicators allPrgInd, Long ampThemeId)
		{
			Session session = null;
			try
			{
				session = PersistenceManager.getRequestDBSession();
				AmpTheme tempAmpTheme = null;
				tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,ampThemeId);
				AmpThemeIndicators ampThemeInd = saveEditPrgInd(allPrgInd, tempAmpTheme);
				session.flush();
				saveEditPrgIndValues(allPrgInd.getThemeIndValues(),ampThemeInd);
			}
			catch(Exception ex)
			{
				logger.error("Exception from saveEditThemeIndicators() : " + ex.getMessage());
				ex.printStackTrace(System.out);
			}
		}

		/**
		 * @deprecated use {@link IndicatorUtil} methods.
		 * @param allPrgInd
		 * @param tempAmpTheme
		 * @return
		 */
		@Deprecated
		public static AmpThemeIndicators saveEditPrgInd(AllPrgIndicators allPrgInd, AmpTheme tempAmpTheme)
		{
			Session session = null;
			Transaction tx = null;
			AmpThemeIndicators ampThemeInd = null;
			try
			{
				session = PersistenceManager.getRequestDBSession();
				ampThemeInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,allPrgInd.getIndicatorId());
				ampThemeInd.setName(allPrgInd.getName());
				ampThemeInd.setCode(allPrgInd.getCode());
				ampThemeInd.setType(allPrgInd.getType());
				ampThemeInd.setDescription(allPrgInd.getDescription());
				ampThemeInd.setCreationDate(DateConversion.getDate(allPrgInd.getCreationDate()));
				ampThemeInd.setCategory(allPrgInd.getCategory());
				ampThemeInd.setNpIndicator(allPrgInd.isNpIndicator());
				Set ampThemeSet = new HashSet();
				ampThemeSet.add(tempAmpTheme);
				tx = session.beginTransaction();
				session.saveOrUpdate(ampThemeInd);
				tx.commit();
			}
			catch(Exception ex)
			{
				logger.error("Exception from saveEditPrgInd() : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null)
				{
					try
					{
						tx.rollback();
					}
					catch (Exception trbf)
					{
						logger.error("Transaction roll back failed : "+trbf.getMessage());
						trbf.printStackTrace(System.out);
					}
				}
			}
			return ampThemeInd;
		}

		/**
		 * We do not have program indicators, but have universal {@link AmpIndicator}.
		 * Use {@link IndicatorUtil} class to work with indicators.
		 * @param prgIndValues
		 * @param ampThemeInd
		 */
		@Deprecated
		public static void saveEditPrgIndValues(Collection prgIndValues, AmpThemeIndicators ampThemeInd)
		{
			Session session = null;
			Transaction tx = null;
			try
			{
				session = PersistenceManager.getSession();
				Iterator indValItr = prgIndValues.iterator();
				while(indValItr.hasNext())
				{
					AmpThemeIndicatorValue ampThIndVal = null;
					AmpPrgIndicatorValue ampPrgIndVal = (AmpPrgIndicatorValue) indValItr.next();
					if(ampPrgIndVal.getIndicatorValueId() == null)
						ampThIndVal = new AmpThemeIndicatorValue();
					else
						ampThIndVal = (AmpThemeIndicatorValue) session.load(AmpThemeIndicatorValue.class,ampPrgIndVal.getIndicatorValueId());
					ampThIndVal.setValueAmount(ampPrgIndVal.getValAmount());
					ampThIndVal.setCreationDate(DateConversion.getDate(ampPrgIndVal.getCreationDate()));
					ampThIndVal.setValueType(ampPrgIndVal.getValueType());
					ampThIndVal.setThemeIndicatorId(ampThemeInd);
					tx = session.beginTransaction();
					session.saveOrUpdate(ampThIndVal);
					tx.commit();
				}
			}
			catch(Exception ex)
			{
				logger.error("Exception from saveEditPrgIndValues() : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null)
				{
					try
					{
						tx.rollback();
					}
					catch (Exception trbf)
					{
						logger.error("Transaction roll back failed : "+trbf.getMessage());
						trbf.printStackTrace(System.out);
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
		
		/**
		 * This was saving theme indicator but deprecated now.
		 * @deprecated there are no Theme Indicators any more. use {@link IndicatorUtil} methods.
		 * @param tempPrgInd
		 * @param ampThemeId
		 */
		@Deprecated
		public static void saveThemeIndicators(AmpPrgIndicator tempPrgInd,Long ampThemeId)
		{
			Session session = null;
			Transaction tx = null;
			try
			{
				session = PersistenceManager.getRequestDBSession();
				AmpTheme tempAmpTheme = null;
				tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,ampThemeId);
				AmpThemeIndicators ampThemeInd=null;
				if(tempPrgInd.getIndicatorId()!=null){
					ampThemeInd=(AmpThemeIndicators)session.load(AmpThemeIndicators.class,tempPrgInd.getIndicatorId());
				}else{
				ampThemeInd = new AmpThemeIndicators();
				}
				ampThemeInd.setName(tempPrgInd.getName());
				ampThemeInd.setCode(tempPrgInd.getCode());
				ampThemeInd.setType(tempPrgInd.getType());
				ampThemeInd.setCreationDate(DateConversion.getDate(tempPrgInd.getCreationDate()));
				ampThemeInd.setCategory(tempPrgInd.getCategory());
				ampThemeInd.setNpIndicator(tempPrgInd.isNpIndicator());
				ampThemeInd.setDescription(tempPrgInd.getDescription());
				if(ampThemeInd.getSectors()==null){
				   ampThemeInd.setSectors(new HashSet());
				}
				
				    Long sectorIds[]= tempPrgInd.getSector();
	               	Set sectors = new HashSet();
	               Collection sect=tempPrgInd.getIndSectores();
	               if(sect!=null&&sect.size()>0){
	            	 Iterator <ActivitySector> sectIter=sect.iterator();
	            	  while(sectIter.hasNext()){
	            		  ActivitySector sector=sectIter.next();
	            		  if(tempPrgInd.getIndicatorId()==null||!SectorUtil.getIndIcatorSector(tempPrgInd.getIndicatorId(), sector.getSectorId())){
	            		  AmpIndicatorSector amps = new AmpIndicatorSector();
	           			   amps.setThemeIndicatorId(ampThemeInd);
	           			   amps.setSectorId(SectorUtil.getAmpSector(sector.getSectorId()));
	           			   ampThemeInd.getSectors().add(amps);
	            		  }
	            	   }
	               }
	          
				Set ampThemeSet = new HashSet();
				ampThemeSet.add(tempAmpTheme);
				ampThemeInd.setThemes(ampThemeSet);
				tx = session.beginTransaction();
				Iterator itr = ampThemeInd.getThemes().iterator();
				while(itr.hasNext()){
				AmpTheme theme = (AmpTheme)itr.next();
					itr.remove();
				}
				session.saveOrUpdate(ampThemeInd);
				//tempAmpTheme.getIndicators().add(ampThemeInd);
				//session.saveOrUpdate(tempAmpTheme);

                if(tempPrgInd.getPrgIndicatorValues()!=null && tempPrgInd.getPrgIndicatorValues().size()!=0){
                    Iterator indItr = tempPrgInd.getPrgIndicatorValues().iterator();
                    while(indItr.hasNext()) {
                        AmpPrgIndicatorValue prgIndValue = (AmpPrgIndicatorValue) indItr.next();
                        AmpThemeIndicatorValue indValue = new AmpThemeIndicatorValue();
                        indValue.setValueType(prgIndValue.getValueType());
                        indValue.setValueAmount(prgIndValue.getValAmount());
                        indValue.setCreationDate(DateConversion.getDate(prgIndValue.getCreationDate()));
                        indValue.setThemeIndicatorId(ampThemeInd);
                        session.save(indValue);
                    }
                }
				tx.commit();
			}
			catch(Exception ex){
				logger.error("Exception from saveThemeIndicators() : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null){
					try{
						tx.rollback();
					}catch (Exception trbf){
						logger.error("Transaction roll back failed : "+trbf.getMessage());
						trbf.printStackTrace(System.out);
					}
				}
			}
		}

		public static void deleteTheme(Long themeId) throws AimException, DgException
		{
			ArrayList colTheme = (ArrayList)getRelatedThemes(themeId);
			int colSize = colTheme.size();
			for(int i=colSize-1; i>=0; i--)
			{
				AmpTheme ampTh = (AmpTheme) colTheme.get(i);
				/*Set tempIndicators = ampTh.getIndicators();
				Iterator tempInd = tempIndicators.iterator();
				while(tempInd.hasNext())
				{
					AmpThemeIndicators themeInd = (AmpThemeIndicators) tempInd.next();
					//deletePrgIndicator(themeInd.getAmpThemeIndId());
				}*/
				Session sess = null;
				Transaction tx = null;
				try {
					sess = PersistenceManager.getRequestDBSession();
					tx = sess.beginTransaction();
					AmpTheme tempTheme = (AmpTheme) sess.load(AmpTheme.class,ampTh.getAmpThemeId());
					sess.delete(tempTheme);
					tx.commit();
				} catch (HibernateException e) {
					logger.error(e);
					throw new AimException("Cannot delete theme with id "+themeId,e);
				} catch (DgException e) {
					logger.error(e);
				}
			}
		}

		public static AllPrgIndicators getThemeIndicator(Long indId)
		{
			Session session = null;
			AllPrgIndicators tempPrgInd = new AllPrgIndicators();

			try{
				session = PersistenceManager.getRequestDBSession();
				AmpThemeIndicators tempInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,indId);
				tempPrgInd.setIndicatorId(tempInd.getAmpThemeIndId());

				tempPrgInd.setName(tempInd.getName());
				tempPrgInd.setCode(tempInd.getCode());
				tempPrgInd.setType(tempInd.getType());
				tempPrgInd.setDescription(tempInd.getDescription());
				tempPrgInd.setCreationDate(DateConversion.ConvertDateToString(tempInd.getCreationDate()));
				tempPrgInd.setCategory(tempInd.getCategory());
				tempPrgInd.setNpIndicator(tempInd.isNpIndicator());
                tempPrgInd.setThemes(tempInd.getThemes());
                tempPrgInd.setSector(getSectorIndicator(indId)); 
                session.flush();
			}
			catch(Exception e){
				logger.error("Unable to get the specified Indicator");
				logger.debug("Exception : "+e);
			}
			return tempPrgInd;
		}

		@Deprecated
        public static AmpThemeIndicators getThemeIndicatorById(Long indId){
            Session session = null;
            AmpThemeIndicators tempInd = new AmpThemeIndicators();

            try{
                session = PersistenceManager.getRequestDBSession();
                tempInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,indId);
                session.flush();
            }
            catch(Exception e){
                logger.error("Unable to get the specified Indicator");
                logger.debug("Exception : "+e);
            }
            return tempInd;
		}

		@Deprecated
		public static AllPrgIndicators getThemeIndValues(Long indId)
		{
			AllPrgIndicators programInd = getThemeIndicator(indId);
			programInd.setThemeIndValues(getThemeIndicatorValues(indId));
			return programInd;
		}

		/**
		 * @deprecated use {@link IndicatorUtil} mthods.
		 * @param allPrgInd
		 */
		@Deprecated
		public static void saveIndicator(AllPrgIndicators allPrgInd)
		{
			Session session = null;
			Transaction tx = null;
			try
			{
				session = PersistenceManager.getSession();
				AmpThemeIndicators tempThemeInd = null;
				tempThemeInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,allPrgInd.getIndicatorId());
				tempThemeInd.setName(allPrgInd.getName());
				tempThemeInd.setCode(allPrgInd.getCode());
				tempThemeInd.setType(allPrgInd.getType());
				tempThemeInd.setCategory(allPrgInd.getCategory());
				tempThemeInd.setNpIndicator(allPrgInd.isNpIndicator());
				tx = session.beginTransaction();
				session.saveOrUpdate(tempThemeInd);
				tx.commit();
			}
			catch(Exception ex)
			{
				logger.error("Exception from saveIndicator() : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null)
				{
					try
					{
						tx.rollback();
					}
					catch (Exception trbf)
					{
						logger.error("Transaction roll back failed : "+trbf.getMessage());
						trbf.printStackTrace(System.out);
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

		/**
		 * @deprecated use {@link IndicatorUtil}
		 * @param indId
		 */
		@Deprecated
		public static void deleteProgramIndicator(Long indId){
			Session session = null;
			Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
			AmpIndicator tempThemeInd = (AmpIndicator) session.load(AmpIndicator.class,indId);
//			tempThemeInd.getThemes().remove(tempThemeInd);
			session.delete(tempThemeInd);
			tx.commit();
		} catch (Exception e) {
			logger.error("Unable to delete the themes");
			logger.debug("Exception : "+e);
		}
	}
		
		/**
		 * Deletes indicator with its values
		 * @param indId db ID of the indicator
		 * @throws AimException if any error with DB
		 * @deprecated
		 */
		@Deprecated
		public static void deletePrgIndicator(Long indId) throws AimException
		{
//			Session session = null;
//			Transaction tx = null;
//			try
//			{
//				//deletePrgIndicatorValue(indId);
//				session = PersistenceManager.getRequestDBSession();
//				tx = session.beginTransaction();
//				AmpIndicator tempThemeInd = (AmpIndicator) session.load(AmpIndicator.class,indId);
//                Iterator itr=tempThemeInd.getThemes().iterator();
//                while(itr.hasNext()){
//                    AmpTheme tm=(AmpTheme)itr.next();
//                    Iterator indItr=tm.getIndicators().iterator();
//                    while(indItr.hasNext()){
//                        AmpIndicator tmInd=(AmpIndicator)indItr.next();
//                        if(tmInd.getIndicatorId().equals(indId)){
//                            indItr.remove();
//                            //please read http://www.hibernate.org/hib_docs/reference/en/html/example-parentchild.html
//                            session.delete(tmInd);
//                            tempThemeInd.getThemes().remove(tm);
//                            
//                        }
//                    }
//                    session.update(tempThemeInd);
//                }
//				tx.commit();
//				session.flush();
//			}
//			catch(Exception e1)
//			{
//				logger.error("The theme indicators were not deleted");
//				logger.debug("Exception : "+e1);
//				throw new AimException(e1);
//			}
		}

		@Deprecated
		public static void deletePrgIndicatorValue(Long themeIndicatorId)
		{
			Session session = null;
			Query qry = null;
			Transaction tx = null;
			Collection indValues = new ArrayList();
			try
			{
				session = PersistenceManager.getRequestDBSession();
				String queryString = "from " + AmpThemeIndicatorValue.class.getName() +
			       " indVal where indVal.themeIndicatorId=:themeIndicatorId";
				qry = session.createQuery(queryString);
				qry.setParameter("themeIndicatorId",themeIndicatorId);
				indValues = qry.list();
				tx = session.beginTransaction();
				Iterator indValuesItr = indValues.iterator();
				while(indValuesItr.hasNext())
				{
					AmpThemeIndicatorValue ampThIndValue = (AmpThemeIndicatorValue) indValuesItr.next();
					session.delete(ampThIndValue);
				}
				tx.commit();
				session.flush();
			}
			catch(Exception e1){
				logger.error("The theme indicator values were not deleted");
				logger.debug("Exception : "+e1);
			}
		}

		@Deprecated
        public static void deletePrgIndicatorValueById(Long themeIndicatorId,Long themeIndicatorValueId)
        {
            Session session = null;
            Query qry = null;
            Transaction tx = null;
            Collection indValues = new ArrayList();
            try
            {
                session = PersistenceManager.getRequestDBSession();
                String queryString = "from " + AmpThemeIndicatorValue.class.getName() +
                   " indVal where indVal.indicatorId=:indicatorId";
                qry = session.createQuery(queryString);
                qry.setParameter("indicatorId",themeIndicatorId);
                indValues = qry.list();
                tx = session.beginTransaction();
                Iterator indValuesItr = indValues.iterator();
                while(indValuesItr.hasNext()){
                    AmpThemeIndicatorValue ampThIndValue = (AmpThemeIndicatorValue) indValuesItr.next();
                    if(ampThIndValue.getAmpThemeIndValId().equals(themeIndicatorValueId)){
                        session.delete(ampThIndValue);
                    }
                }
                tx.commit();
                session.flush();
            }
            catch(Exception e1){
                logger.error("The theme indicator values were not deleted");
                logger.debug("Exception : "+e1);
            }
		}

		public static void updateTheme(EditProgram editPrg)
		{
			Session session = null;
			Transaction tx = null;
			try
			{
				session = PersistenceManager.getSession();
				AmpTheme tempAmpTheme = null;
				tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,editPrg.getAmpThemeId());
				tempAmpTheme.setName(editPrg.getName());
				tempAmpTheme.setThemeCode(editPrg.getThemeCode());
				tempAmpTheme.setDescription(editPrg.getDescription());
				tempAmpTheme.setTypeCategoryValue( editPrg.getTypeCategVal() );

				tempAmpTheme.setLeadAgency( editPrg.getLeadAgency() );
				tempAmpTheme.setTargetGroups( editPrg.getTargetGroups() );
				tempAmpTheme.setBackground( editPrg.getBackground() );
				tempAmpTheme.setObjectives( editPrg.getObjectives() );
				tempAmpTheme.setOutputs( editPrg.getOutputs() );
				tempAmpTheme.setBeneficiaries( editPrg.getBeneficiaries() );
				tempAmpTheme.setEnvironmentConsiderations( editPrg.getEnvironmentConsiderations() );

				tx = session.beginTransaction();
				session.update(tempAmpTheme);
				tx.commit();
			}
			catch(Exception ex)
			{
				logger.error("Exception from saveIndicator() : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null)
				{
					try
					{
						tx.rollback();
					}
					catch (Exception trbf)
					{
						logger.error("Transaction roll back failed : "+trbf.getMessage());
						trbf.printStackTrace(System.out);
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

		public static Collection getRelatedThemes(Long id) throws DgException
		{
			AmpTheme ampThemetemp = new AmpTheme();
			ampThemetemp = getThemeObject(id);
			Collection<AmpTheme> themeCol = getSubThemes(id);
			Collection tempPrg = new ArrayList();
			tempPrg.add(ampThemetemp);
			if(!themeCol.isEmpty())
			{
				Iterator itr = themeCol.iterator();
				AmpTheme tempTheme = new AmpTheme();
				while(itr.hasNext())
				{
					tempTheme = (AmpTheme) itr.next();
					tempPrg.addAll(getRelatedThemes(tempTheme.getAmpThemeId()));
				}
			}
			return tempPrg;
		}


        public static String getThemesHierarchyXML(Collection allAmpThemes) {
            String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
            result += "<progTree>\n";
            if (allAmpThemes != null && allAmpThemes.size() > 0) {

                //make hieararchy of programs wrapped into TreeItem
                Collection themeTree = CollectionUtils.getHierarchy(allAmpThemes,
                        new ProgramHierarchyDefinition(), new XMLtreeItemFactory());

                //get XML from each top level item. They will handle subitems.
                for (Iterator treeItemIter = themeTree.iterator(); treeItemIter.hasNext(); ) {
                    TreeItem item = (TreeItem) treeItemIter.next();
                    result += item.getXml();
                }
            }
            result += "</progTree>\n";
            return result;
        }

        public static AmpActivityProgramSettings getAmpActivityProgramSettings(String name) throws DgException {
            Session session = null;
            AmpActivityProgramSettings programSettings=null;

            try {
                    session = PersistenceManager.getRequestDBSession();
                    String queryString = "select ap from "
                                    + AmpActivityProgramSettings.class.getName()+ " ap "
                                    + "where ap.name=:name";
                    Query qry = session.createQuery(queryString);
                    qry.setString("name", name);
                    programSettings=(AmpActivityProgramSettings)qry.uniqueResult();


            } catch (Exception ex) {
                    logger.debug("Unable to search " + ex);
                    throw new DgException(ex);
                    }
            return programSettings;
    }


      public static List getAmpActivityProgramSettingsList() throws DgException {
            Session session = null;
            List programSettings=null;

            try {
                    session = PersistenceManager.getRequestDBSession();
                    String queryString = "select ap from "
                                    + AmpActivityProgramSettings.class.getName()+ " ap ";

                    Query qry = session.createQuery(queryString);
                    programSettings=qry.list();
                    if( programSettings==null|| programSettings.size()==0){
               programSettings=createDefaultAmpActivityProgramSettingsList();
               }


            } catch (Exception ex) {
                    logger.debug("Unable to search " + ex);
                    throw new DgException(ex);
                    }
            return programSettings;
    }


    public static List createDefaultAmpActivityProgramSettingsList() throws DgException {
        Session session = null;
        List programSettings=null;
        Transaction tx = null;


        try {
                session = PersistenceManager.getRequestDBSession();
                tx = session.beginTransaction();
                AmpActivityProgramSettings settingNPO=new AmpActivityProgramSettings("National Plan Objective");
                AmpActivityProgramSettings settingPP=new AmpActivityProgramSettings("Primary Program");
                AmpActivityProgramSettings settingSP=new AmpActivityProgramSettings("Secondary Program");
                session.save(settingNPO);
                session.save(settingPP);
                session.save(settingSP);
                tx.commit();
                programSettings=new ArrayList();
                programSettings.add(settingNPO);
                programSettings.add(settingPP);
                programSettings.add(settingSP);


             } catch (Exception ex) {
              logger.error("Unable to create Default program Settings List  " + ex);
                        if (tx != null)
                          {
                                  try
                                  {
                                          tx.rollback();
                                  }
                                  catch (Exception extx)
                                  {
                                          logger.error("Transaction roll back failed : "+extx.getMessage());

                                  }
                          }


                    throw new DgException(ex);
                    }

        return programSettings;
}


    public static String printHierarchyNames(AmpTheme child) {
            String names = "";
            AmpTheme parent = child.getParentThemeId();
            if (parent == null) {
                    return names;
            }
            else {
                    names = printHierarchyNames(parent);
                    names += "[" + parent.getName() + "] ";
            }

            return names;

    }

    //save new settings
    public static void saveAmpActivityProgramSettings(List settings) throws
        DgException {
            Session session = null;
            Transaction tx = null;

            try {
                    session = PersistenceManager.getRequestDBSession();
                    if (settings != null) {
                            Iterator settingsIter = settings.iterator();
                            tx = session.beginTransaction();
                            while (settingsIter.hasNext()) {
                                    AmpActivityProgramSettings setting = (
                                        AmpActivityProgramSettings)
                                        settingsIter.next();
                                    AmpActivityProgramSettings oldSetting = (
                                        AmpActivityProgramSettings) session.
                                        get(AmpActivityProgramSettings.class,
                                            setting.getAmpProgramSettingsId());
                                    oldSetting.setAllowMultiple(setting.
                                        isAllowMultiple());
                                    oldSetting.setDefaultHierarchy(setting.
                                        getDefaultHierarchy());
                                    session.update(oldSetting);

                            }
                            tx.commit();

                    }

            }
            catch (Exception ex) {
                    logger.error("Unable to save program Setting  " + ex);
                    if (tx != null) {
                            try {
                                    tx.rollback();
                            }
                            catch (Exception extx) {
                                    logger.error(
                                        "Transaction roll back failed : " +
                                        extx.getMessage());

                            }
                    }

                    throw new DgException(ex);
            }

    }


    
	 public static String renderLevel(Collection themes,int level,HttpServletRequest request) {
		 //CategoryManagerUtil cat = new CategoryManagerUtil();
		 if (themes == null || themes.size() == 0)
			return "<center><b>No Programs</b></<center>";
		 //Site site = RequestUtils.getSite(request);
		 String retVal;
		retVal = "<table width=\"100%\" cellPadding=\"0\" cellSpacing=\"1\" valign=\"top\" align=\"left\" bgcolor=\"#ffffff\" border=\"0\" style=\"border-collapse: collapse;\">\n";
		Iterator iter = themes.iterator();
		while (iter.hasNext()) {
			TreeItem item = (TreeItem) iter.next();
			AmpTheme theme = (AmpTheme) item.getMember();
			retVal += "<tr><td>&nbsp;</td><td width=\"100%\">\n";

			
			// visible div start
			retVal += "<div>";// id=\"div_theme_"+theme.getAmpThemeId()+"\"";
			retVal += " <table width=\"100%\"  border=\"1\" style=\"border-collapse: collapse;border-color: #ffffff\">";
			if (level == 1){
			retVal += "<tr bgcolor=\"#ffcccc\" width=\"100%\">";
			}else{
			retVal += "<tr bgcolor=\"#f4f4f2\">";
			}
			retVal += "   <td width=\"1%\" >";
			retVal += "     <img id=\"img_" + theme.getAmpThemeId()+ "\" onclick=\"expandProgram(" + theme.getAmpThemeId()+ ")\" src=\"../ampTemplate/images/arrow_right.gif\"/>\n";
			retVal += "     <img id=\"imgh_"+ theme.getAmpThemeId()+ "\" onclick=\"collapseProgram("+ theme.getAmpThemeId()+ ")\" src=\"../ampTemplate/images/arrow_down.gif\"  style=\"display : none;\"/>\n";
			retVal += "   </td>";
			if (level>1){
				retVal += "   <td width=\"1%\">";
				retVal += "     <img src=\"../ampTemplate/images/link_out_bot.gif\"/>\n";
				retVal += "   </td>";
				retVal += "   <td width=\"1%\">";
				retVal += "     <img src=\""+getLevelImage(level)+"\" />\n";
				retVal += "   </td>";
			}
			retVal += "   <td  class=\"progName\">";
			retVal += "    <a href=\"javascript:editProgram("+ theme.getAmpThemeId()+ ")\">"+getTrn("aim:admin:themeTree:theme_name",((AmpTheme) item.getMember()).getEncodeName(), request)+"</a>\n";
			retVal += "   </td>";
			retVal += "   <td class=\"progCode\"  width=\"45%\" nowrap=\"nowrap\">("+ ((AmpTheme) item.getMember()).getThemeCode() + ")</td>";
			retVal += "   <td nowrap=\"nowrap\" width=\"10%\">";
			retVal += "     <a href=\"javascript:addSubProgram('5','"+theme.getAmpThemeId() +"','"+level+"','"+theme.getEncodeName()+"')\">"+getTrn("aim:admin:themeTree:add_sub_prog", "Add Sub Program", request)+"</a> |\n";
			retVal += "   </td>";
			retVal += "   <td nowrap=\"nowrap\" width=\"10%\">";
			retVal += "     <a href=\"javascript:assignIndicators('"+theme.getAmpThemeId() +"')\">"+getTrn("aim:admin:themeTree:manage_indicators", "Manage Indicators", request)+"</a>\n";
			retVal += "   </td>";
			retVal += "   <td width=\"12\">";
			retVal += "     <a href=\"/aim/themeManager.do~event=delete~themeId="+theme.getAmpThemeId()+"\" onclick=\"return deleteProgram()\"><img src=\"../ampTemplate/images/trash_12.gif\" border=\"0\"></a>";
			retVal += "   </td>";
			retVal += " </tr></table>";
			retVal += "</div>\n";
	
			// hidden div start
			retVal += "<div id=\"div_theme_" + theme.getAmpThemeId()+ "\" style=\"display : none;\">\n";
			if (item.getChildren() != null || item.getChildren().size() > 0) {
				retVal += renderLevel(item.getChildren(), level+1,request);
			}
			retVal += "</div>\n";

			retVal += "</td></tr>\n";
		}
		retVal += "</table>\n";
		return retVal;
	}

	 public static String getTrn(String key, String defResult, HttpServletRequest request){
		 //CategoryManagerUtil cat = new CategoryManagerUtil();
		 //return CategoryManagerUtil.translate(key, request, defResult);
		String	lang	= RequestUtils.getNavigationLanguage(request).getCode();
		Long	siteId	= RequestUtils.getSite(request).getId();
		
		Message m = null;
		
		try {
			m = DbUtil.getMessage(key.toLowerCase(), lang, siteId);
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (m == null)
		 {
			 return defResult;
		 }
		 else
		 {
			 return m.getMessage();
		 }
		 
	 }
	 
    public static String getLevelImage(int level){
    	switch (level) {
		case 0:
			return "../ampTemplate/images/arrow_right.gif";
		case 1:
			return "../ampTemplate/images/arrow_right.gif";
		case 2:
			return "../ampTemplate/images/square1.gif";
		case 3:
			return "../ampTemplate/images/square2.gif";
		case 4:
			return "../ampTemplate/images/square3.gif";
		case 5:
			return "../ampTemplate/images/square4.gif";
		case 6:
			return "../ampTemplate/images/square5.gif";
		case 7:
			return "../ampTemplate/images/square6.gif";
		case 8:
			return "../ampTemplate/images/square7.gif";
		}
    	return "../ampTemplate/images/arrow_right.gif";
    }

    public static Collection<AmpActivity> checkActivitiesUsingProgram( Long programId ) {
    	ArrayList<AmpActivity> activities	= new ArrayList<AmpActivity>();
    	 Session sess = null;
         Collection col = null;
         try {
             ArrayList programs = (ArrayList) getRelatedThemes(programId);
             if (programs != null) {
                 Iterator<AmpTheme> iterProgram = programs.iterator();
                 while (iterProgram.hasNext()) {
                     AmpTheme program = iterProgram.next();
                     sess = PersistenceManager.getRequestDBSession();
                     AmpTheme themeToBeDeleted = (AmpTheme) sess.load(AmpTheme.class, program.getAmpThemeId());
                     if (themeToBeDeleted.getActivities() != null) {
                         activities.addAll(themeToBeDeleted.getActivities());
                     }
                     if (themeToBeDeleted.getActivityId() != null) {
                         activities.add(themeToBeDeleted.getActivityId());
                     }

                     String queryString = "select a from " + AmpActivityProgram.class.getName() + " a where (a.program=:program) ";
                     Query qry = sess.createQuery(queryString);
                     qry.setLong("program", programId);
                     Collection result = qry.list();
                     if (result != null) {
                         Iterator iterator = result.iterator();
                         while (iterator.hasNext()) {
                             AmpActivityProgram actProgram = (AmpActivityProgram) iterator.next();
                             if (actProgram != null && actProgram.getActivity() != null) {
                                 activities.add(actProgram.getActivity());
                             }
                         }
                     }
                 }
             }

             return activities;
         }
         catch (Exception e) {
			// TODO: handle exception
        	 e.printStackTrace();
        	 return null;
		}
    	
    }
    public static String getNameOfProgramSettingsUsed(Long programId) { 
    	Collection programSettings					= getProgramSetttingsUsed(programId);
    	
    	Iterator iter	= programSettings.iterator();
    	String result	= "";
    	while ( iter.hasNext() ) {
    		AmpActivityProgramSettings aaps			= (AmpActivityProgramSettings) iter.next();
    		if ( aaps.getName() != null )
    			result	+= "'" + aaps.getName() + "'" + ", ";
    	}
    	if ( result.length() > 0 ) 
    		return result.substring(0, result.length()-2);
    	else
    		return null;
    }
    public static Collection getProgramSetttingsUsed(Long programId) {
    	Session sess 						= null;
        try {
        	sess = PersistenceManager.getRequestDBSession();
        	String qryString 		= "select a from " + AmpActivityProgramSettings.class.getName() + " a where (a.defaultHierarchy=:program) ";
        	Query qry 			= sess.createQuery(qryString);
            qry.setLong("program", programId);
            Collection result	= qry.list();
            return result;
        }
        catch (Exception e) {
			// TODO: handle exception
        	 e.printStackTrace();
        	 return null;
		}
	}
    
        /**
         * Hierarchy member factory.
         * Used to create XML enabled members.
         */
        public static class XMLtreeItemFactory implements HierarchyMemberFactory{
            public HierarchyMember createHierarchyMember(){
                TreeItem item=new TreeItem();
                item.setChildren(new ArrayList());
                return item;
            }
        }

        public static class ProgramHierarchyDefinition implements
                HierarchyDefinition {
            public Object getObjectIdentity(Object object) {
                AmpTheme i = (AmpTheme) object;
                return i.getAmpThemeId();

            }

            public Object getParentIdentity(Object object) {
                AmpTheme i = (AmpTheme) object;
                if (i.getParentThemeId() == null) {
                    return null;
                }
                else {
                    return i.getParentThemeId().getAmpThemeId();
                }
            }
        }

        public static class HierarchicalProgramComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                AmpTheme i1 = (AmpTheme) o1;
                AmpTheme i2 = (AmpTheme) o2;

                Long sk1 = i1.getAmpThemeId();
                Long sk2 = i2.getAmpThemeId();

                return sk1.compareTo(sk2);
            }

        }

        /**
         * Added by Govind
         *
         * @deprecated Use Category Manager functions instead
         */

        /*public static Collection getProgramTypes() {
    		Session session = null;
    		Collection col = null;
    		try {
    			session = PersistenceManager.getSession();
    			String qryStr = "select name from " + AmpProgramType.class.getName()
    					+ " name ";
    			Query qry = session.createQuery(qryStr);
    			col=qry.list();
    		} catch (Exception e) {
    			logger.error("Exception from getTheme()");
    			logger.error(e.getMessage());
    		} finally {
    			if (session != null) {
    				try {
    					PersistenceManager.releaseSession(session);
    				} catch (Exception rsf) {
    					logger.error("Release session failed");
    				}
    			}
    		}
    		return col;
    	}
*/
        /**
         * to save New Program Types
         * AmpProgramType no longer used. Use Category Manager instead
         */
        /*public static void saveNewProgramType(AmpProgramType prg) {
        	DbUtil.add(prg);

    	}*/
        /**
         * update a Program
         * AmpProgramType no longer used. Use Category Manager instead
         */
        /*public static void updateProgramType(AmpProgramType prg) {
        	DbUtil.update(prg);

    	}*/
        /**
         * to get the Program Type for Editing
         * AmpProgramType no longer used. Use Category Manager instead
         */
        /*public static Collection getProgramTypeForEdititng(Long Id) {
    		Session session = null;
    		Collection col = null;
    		try {
    			session = PersistenceManager.getSession();
    			String qryStr = "select name from " + AmpProgramType.class.getName()
    					+ " name where name.ampProgramId=:Id ";

    			Query qry = session.createQuery(qryStr);
				qry.setParameter("Id",Id);
    			col=qry.list();
    		} catch (Exception e) {
    			logger.error("Exception from getTheme()");
    			logger.error(e.getMessage());
    		} finally {
    			if (session != null) {
    				try {
    					PersistenceManager.releaseSession(session);
    				} catch (Exception rsf) {
    					logger.error("Release session failed");
    				}
    			}
    		}
    		return col;
    	}*/
        /**
         * @deprecated AmpProgramType no longer used. Use Category Manager instead
         */
        /*public static void deleteProgramType(AmpProgramType prg) {
        	try {
				DbUtil.delete(prg);
			} catch (JDBCException e) {
				logger.error(e);
			}

    	}*/
    	 /**
         * Used to sort indicators by name
         */
        public static class IndicatorNameComparator implements Comparator {

			public int compare(Object obj1, Object obj2) {
				AmpThemeIndicators indic1 = (AmpThemeIndicators) obj1;
				AmpThemeIndicators indic2 = (AmpThemeIndicators) obj2;
				return indic1.getName().compareTo(indic2.getName());
			}

        }

        public static class HelperAmpThemeNameComparator implements Comparator {
            public int compare(Object obj1, Object obj2) {
                AmpTheme theme1 = (AmpTheme) obj1;
                AmpTheme theme2 = (AmpTheme) obj2;
                return theme1.getName().compareTo(theme2.getName());
            }
        }

        public static class HelperAmpIndicatorNameComparator implements Comparator {

            public int compare(Object obj1, Object obj2) {
                AmpPrgIndicator indic1 = (AmpPrgIndicator) obj1;
                AmpPrgIndicator indic2 = (AmpPrgIndicator) obj2;
                return indic1.getName().compareTo(indic2.getName());
            }

        }

        public static class HelperAllPrgIndicatorNameComparator implements Comparator {

            public int compare(Object obj1, Object obj2) {
                AllPrgIndicators indic1 = (AllPrgIndicators) obj1;
                AllPrgIndicators indic2 = (AllPrgIndicators) obj2;
                return indic1.getName().compareTo(indic2.getName());
            }
        }

        public static class HelperAllMEIndicatorNameComparator implements Comparator {
           public int compare(Object obj1, Object obj2) {
               AllMEIndicators  indic1 = (AllMEIndicators) obj1;
               AllMEIndicators indic2 = (AllMEIndicators) obj2;
               return indic1.getName().compareTo(indic2.getName());
           }
        }
        public static class HelperAllIndicatorBeanNameComparator
            implements Comparator {
            public int compare(Object obj1, Object obj2) {
                IndicatorsBean indic1 = (IndicatorsBean) obj1;
                IndicatorsBean indic2 = (IndicatorsBean) obj2;
                return indic1.getName().toLowerCase().compareTo(indic2.getName().toLowerCase());
            }
        }

        public static class HelperAllIndicatorBeanSectorComparator
            implements Comparator {
            public int compare(Object obj1, Object obj2) {
                IndicatorsBean indic1 = (IndicatorsBean) obj1;
                IndicatorsBean indic2 = (IndicatorsBean) obj2;
                return indic1.getSectorName().compareTo(indic2.getSectorName());
            	}

        }

        public static class HelperAllIndicatorBeanTypeComparator
            implements Comparator {
            public int compare(Object obj1, Object obj2) {
                IndicatorsBean indic1 = (IndicatorsBean) obj1;
                IndicatorsBean indic2 = (IndicatorsBean) obj2;
                return indic1.getType().compareTo(indic2.getType());

            }
        }

        public static class ThemeIdComparator implements Comparator<AmpTheme>{

			public int compare(AmpTheme thm1, AmpTheme thm2) {
				return thm1.getAmpThemeId().compareTo(thm2.getAmpThemeId());
			}

        }

 }
