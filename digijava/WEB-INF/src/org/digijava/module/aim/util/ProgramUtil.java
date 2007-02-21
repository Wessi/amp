/*
 * ProgramUtil.java
 */

package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.kernel.util.collections.HierarchyMember;
import org.digijava.kernel.util.collections.HierarchyMemberFactory;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicatorValue;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AllThemes;
import org.digijava.module.aim.helper.EditProgram;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.TreeItem;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpProgramType;

public class ProgramUtil {

		private static Logger logger = Logger.getLogger(ProgramUtil.class);
	
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

		public static Collection getParentThemes() {
			Session session = null;
			Query qry = null;
			Collection themes = new ArrayList();
	
			try {
				session = PersistenceManager.getSession();
				String queryString = "select t from " + AmpTheme.class.getName()
						+ " t where t.parentThemeId is null";
				qry = session.createQuery(queryString);
				themes = qry.list();
			} catch (Exception e) {
				logger.error("Unable to get all themes : "+e);
				logger.debug("Exceptiion " + e);
			} finally {
				try {
					if (session != null) {
						PersistenceManager.releaseSession(session);
					}
				} catch (Exception ex) {
					logger.error("releaseSession() failed");
				}
			}
			return themes;
		}

		public static List getAllThemes() {
			Session session = null;
			Query qry = null;
			List themes = new ArrayList();
	
			try {
				session = PersistenceManager.getRequestDBSession();
				String queryString = " from " + AmpTheme.class.getName()
						+ " t where t.parentThemeId is null";
				qry = session.createQuery(queryString);
				themes = qry.list();
			} catch (Exception e) {
				logger.error("Unable to get all themes");
				logger.debug("Exceptiion " + e);
			} 
			return themes;
		}

        /**
         * Returns All AmpThemes including sub Themes if parametre is true.
         * @param includeSubThemes boolean false - only top level Themes, true - all themes
         * @return List AmpTheme
         */
        public static List getAllThemes(boolean includeSubThemes) {
            Session session = null;
            Query qry = null;
            List themes = new ArrayList();

            try {
                session = PersistenceManager.getRequestDBSession();
                String queryString = "select t from " + AmpTheme.class.getName()
                        + " t ";
                if (!includeSubThemes) {
                    queryString += "where t.parentThemeId is null ";
                }
                qry = session.createQuery(queryString);
                themes = qry.list();
            }
            catch (Exception e) {
                logger.error("Unable to get all themes");
                logger.debug("Exceptiion " + e);
            }
            return themes;
        }
        
        public static Collection getYearsBeanList(){
            Collection result=new ArrayList();
            int start=2000;
            Calendar now=Calendar.getInstance();
            int end=now.get(Calendar.YEAR);
            for (int i = start; i <= end; i++) {
                result.add(new LabelValueBean(String.valueOf(i),String.valueOf(i)));
            }
            return result;
        }
    
	    public static Collection getAllThemeIndicators()
	    {
	    	Collection colThInd = new ArrayList();
	    	Collection colTh = null;
	    	Collection colInd = null;
	    	colTh = getAllPrograms();
	    	colInd = getAllProgramIndicators();
	    	Iterator itrColTh = colTh.iterator();
	    	while(itrColTh.hasNext())
	    	{
	    		AmpTheme ampTh1 = (AmpTheme) itrColTh.next();
	    		AllThemes tempAllThemes = new AllThemes();
				tempAllThemes.setProgramId(ampTh1.getAmpThemeId());
				tempAllThemes.setProgramName(ampTh1.getName());
	    		Iterator itrColInd = colInd.iterator();
	    		Collection allInds = new ArrayList();
	    		while(itrColInd.hasNext())
	    		{
	    			AmpThemeIndicators ampThInd = (AmpThemeIndicators) itrColInd.next();
	    			Set thIndset = ampThInd.getThemes();
	    			if(thIndset.isEmpty()){
	    				continue;
	    			}
	    			else
	    			{
	    				Iterator thIndItr = thIndset.iterator();
	    				while(thIndItr.hasNext())
	    				{
	    					AmpTheme ampTh2 = (AmpTheme) thIndItr.next();
	    					if(ampTh1.getAmpThemeId().equals(ampTh2.getAmpThemeId()))
	    					{
	    						AllPrgIndicators prgInd = new AllPrgIndicators();
	    						prgInd.setIndicatorId(ampThInd.getAmpThemeIndId());
	    						prgInd.setName(ampThInd.getName());
	    						prgInd.setCode(ampThInd.getCode());
	    						prgInd.setType(ampThInd.getType());
	    						prgInd.setCreationDate(DateConversion.ConvertDateToString(ampThInd.getCreationDate()));
	    						prgInd.setCategory(ampThInd.getCategory());
	    						prgInd.setNpIndicator(ampThInd.isNpIndicator());
	    						allInds.add(prgInd);
	    					}
	    				}
	    			}
	    		}
	    		tempAllThemes.setAllPrgIndicators(allInds);
	    		colThInd.add(tempAllThemes);
	    	}
	    	return colThInd;
	    }

        public static Collection getAllPrograms()
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
        	}
        	return colPrg;
        }
        
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
	        finally	{
				try {
					if (session != null)
						PersistenceManager.releaseSession(session);
				}
				catch (Exception ex) {
					logger.error("releaseSession() failed");
				}
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
					themeInd.add(tempPrgInd);
				}
			}
			catch(Exception ex) {
				logger.error("Exception from getThemeIndicators()  " + ex.getMessage());
				ex.printStackTrace(System.out);
			}
			finally {
				if (session != null) {
					try {
						PersistenceManager.releaseSession(session);
					} catch (Exception rsf) {
						logger.error("Release session failed");
					}
				}
			}
			return themeInd;
		}
	
		public static Collection getThemeIndicatorValues(Long themeIndicatorId)
		{
			Session session = null;
			Collection col = new ArrayList();
			try
			{
				session = PersistenceManager.getRequestDBSession();
				String queryString = "select thIndValId from "
									+ AmpThemeIndicatorValue.class.getName()
									+ " thIndValId where (thIndValId.themeIndicatorId=:themeIndicatorId)";
				Query qry = session.createQuery(queryString);
				qry.setParameter("themeIndicatorId",themeIndicatorId,Hibernate.LONG);
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
				session = PersistenceManager.getSession();
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
			finally {
				try
				{
					if(session != null)
						PersistenceManager.releaseSession(session);
				}
				catch(Exception e2)
				{
					logger.error("releaseSession() failed : "+e2);
				}
			}
			return allSubThemes;
		}

		public static Collection getSubThemes(Long parentThemeId)
		{
			Session session = null;
			Query qry = null;
			Collection subThemes = new ArrayList();
			try
			{
				session = PersistenceManager.getRequestDBSession();
			    String queryString = "from " + AmpTheme.class.getName() +
			        " subT where subT.parentThemeId=:parentThemeId";
			    qry = session.createQuery(queryString);
			    qry.setParameter("parentThemeId",parentThemeId);
			    subThemes = qry.list();
			}
			catch(Exception e1)
			{
				logger.error("Unable to get all the sub-themes");
				logger.debug("Exception : "+e1);
			}
			return subThemes;
		}
		
		public static void saveEditThemeIndicators(AllPrgIndicators allPrgInd, Long ampThemeId)
		{
			Session session = null;
			try
			{
				session = PersistenceManager.getSession();
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
		
		public static AmpThemeIndicators saveEditPrgInd(AllPrgIndicators allPrgInd, AmpTheme tempAmpTheme)
		{
			Session session = null;
			Transaction tx = null;
			AmpThemeIndicators ampThemeInd = null;
			try
			{
				session = PersistenceManager.getSession();
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
			return ampThemeInd;
		}
		
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
		
		public static void saveThemeIndicators(AmpPrgIndicator tempPrgInd, Long ampThemeId)
		{
			Session session = null;
			Transaction tx = null;
			try
			{
				session = PersistenceManager.getSession();
				AmpTheme tempAmpTheme = null;
				tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,ampThemeId);
				AmpThemeIndicators ampThemeInd = new AmpThemeIndicators();
				ampThemeInd.setName(tempPrgInd.getName());
				ampThemeInd.setCode(tempPrgInd.getCode());
				ampThemeInd.setType(tempPrgInd.getType());
				ampThemeInd.setCreationDate(DateConversion.getDate(tempPrgInd.getCreationDate()));
				ampThemeInd.setCategory(tempPrgInd.getCategory());
				ampThemeInd.setNpIndicator(tempPrgInd.isNpIndicator());
				ampThemeInd.setDescription(tempPrgInd.getDescription());
				Set ampThemeSet = new HashSet();
				ampThemeSet.add(tempAmpTheme);
				ampThemeInd.setThemes(ampThemeSet);
				tx = session.beginTransaction();
				session.save(ampThemeInd);
				tempAmpTheme.getIndicators().add(ampThemeInd);
				session.saveOrUpdate(tempAmpTheme);
				if(tempPrgInd.getPrgIndicatorValues()!=null) {
				Iterator indItr = tempPrgInd.getPrgIndicatorValues().iterator();
				while(indItr.hasNext())
				{
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
			catch(Exception ex)
			{
				logger.error("Exception from saveThemeIndicators() : " + ex.getMessage());
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
	
		public static void deleteTheme(Long themeId)
		{
			ArrayList colTheme = (ArrayList)getRelatedThemes(themeId);
			int colSize = colTheme.size();
			for(int i=colSize-1; i>=0; i--)
			{
				AmpTheme ampTh = (AmpTheme) colTheme.get(i);
				Set tempIndicators = ampTh.getIndicators();
				Iterator tempInd = tempIndicators.iterator();
				while(tempInd.hasNext())
				{
					AmpThemeIndicators themeInd = (AmpThemeIndicators) tempInd.next();
					deletePrgIndicator(themeInd.getAmpThemeIndId());
				}
				deleteonebyone(ampTh.getAmpThemeId());
			}
		}
	
		public static void deleteonebyone(Long thID)
		{
			Session sess = null;
			Transaction tx = null;
			try
			{
				sess = PersistenceManager.getRequestDBSession();
				tx = sess.beginTransaction();
				AmpTheme tempTheme = (AmpTheme) sess.load(AmpTheme.class,thID);
				sess.delete(tempTheme);
				tx.commit();
			}
			catch(Exception e1)
			{
				logger.error("Unable to delete the themes");
				logger.debug("Exception : "+e1);
			}
		}
		
		public static AllPrgIndicators getThemeIndicator(Long indId)
		{
			Session session = null;
			AllPrgIndicators tempPrgInd = new AllPrgIndicators();
	
			try
			{
				session = PersistenceManager.getSession();
				AmpThemeIndicators tempInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,indId);
				tempPrgInd.setIndicatorId(tempInd.getAmpThemeIndId());
				tempPrgInd.setName(tempInd.getName());
				tempPrgInd.setCode(tempInd.getCode());
				tempPrgInd.setType(tempInd.getType());
				tempPrgInd.setDescription(tempInd.getDescription());
				tempPrgInd.setCreationDate(DateConversion.ConvertDateToString(tempInd.getCreationDate()));
				tempPrgInd.setCategory(tempInd.getCategory());
				tempPrgInd.setNpIndicator(tempInd.isNpIndicator());
				session.flush();
			}
			catch(Exception e)
			{
				logger.error("Unable to get the specified Indicator");
				logger.debug("Exception : "+e);
			}
			finally
			{
				try
				{
					if(session != null)
					{
						PersistenceManager.releaseSession(session);
					}
				}
				catch(Exception ex)
				{
					logger.error("releaseSession() failed");
				}
			}
			return tempPrgInd;
		}
	
		public static AllPrgIndicators getThemeIndValues(Long indId)
		{
			AllPrgIndicators programInd = getThemeIndicator(indId);
			programInd.setThemeIndValues(getThemeIndicatorValues(indId));
			return programInd;
		}
		
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
	
		public static void deletePrgIndicator(Long indId)
		{
			Session session = null;
			Transaction tx = null;
			try
			{
				deletePrgIndicatorValue(indId);
				session = PersistenceManager.getRequestDBSession();
				tx = session.beginTransaction();
				AmpThemeIndicators tempThemeInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,indId);
				session.delete(tempThemeInd);
				tx.commit();
			}
			catch(Exception e1)
			{
				logger.error("The theme indicators were not deleted");
				logger.debug("Exception : "+e1);
			}
		}
		
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
			catch(Exception e1)
			{
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
				tempAmpTheme.setType(editPrg.getType());
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
	
		static Collection tempPrg = new ArrayList();
		public static Collection getRelatedThemes(Long id)
		{
			AmpTheme ampThemetemp = new AmpTheme();
			ampThemetemp = getThemeObject(id);
			Collection themeCol = getSubThemes(id);
			tempPrg.add(ampThemetemp);
			if(!themeCol.isEmpty())
			{
				Iterator itr = themeCol.iterator();
				AmpTheme tempTheme = new AmpTheme();
				while(itr.hasNext())
				{
					tempTheme = (AmpTheme) itr.next();
					getRelatedThemes(tempTheme.getAmpThemeId());
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
        
        /*
         * Added by Govind
         */
        
        public static Collection getProgramTypes() {
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
        /*
         * to save New Program Types
         */
        public static void saveNewProgramType(AmpProgramType prg) {
        	DbUtil.add(prg);
    		
    	}
        /*
         * update a Program  
         */
        public static void updateProgramType(AmpProgramType prg) {
        	DbUtil.update(prg);
    		
    	}
        /*
         * to get the Program Type for Editing
         */
        public static Collection getProgramTypeForEdititng(Long Id) {
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
    	}
        /*
         * 
         */
        public static void deleteProgramType(AmpProgramType prg) {
        	DbUtil.delete(prg);
    		
    	}
 }
