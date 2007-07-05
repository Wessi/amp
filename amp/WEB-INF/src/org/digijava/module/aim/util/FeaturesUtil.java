package org.digijava.module.aim.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeature;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpSiteFlag;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.dbentity.FeatureTemplates;
import org.digijava.module.aim.helper.Flag;

public class FeaturesUtil {

	private static Logger logger = Logger.getLogger(FeaturesUtil.class);

	private static Collection globalSettingsCache=null;
	
	private ServletContext ampContext = null;
	
	public static void logGlobalSettingsCache() {
		String log		= "";
		Iterator iter	= globalSettingsCache.iterator();
		while( iter.hasNext() ) {
			AmpGlobalSettings ampGlobalSetting	= (AmpGlobalSettings) iter.next();
			log		= log + ampGlobalSetting.getGlobalSettingsName() + ":" + ampGlobalSetting.getGlobalSettingsValue() + ";";
		}
		logger.info ("GlobalSettingsCache is -> " + log);
	}
	
	public static synchronized Collection getGlobalSettingsCache() {
		return globalSettingsCache;
	}
	
	public static synchronized void setGlobalSettingsCache(Collection globalSettings) {
		globalSettingsCache=globalSettings;
	}
	
	public static boolean isDefault(Long templateId)
	{
		String s=FeaturesUtil.getGlobalSettingValue("Feature Template");
		if(s!=null)
			if(templateId.compareTo(new Long(Long.parseLong(s)))==0) return true;
		return false;
	}
	
	public static Collection getAMPFeatures() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + AmpFeature.class.getName() + " f";
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
 * @author dan
 *
 * @return
 */
	public static Collection getAMPTemplates() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + FeatureTemplates.class.getName() + " f";
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
	 * Used to get the features which are currently active for AMP
	 * @return The collection of org.digijava.module.aim.dbentity.AmpFeature objects
	 */
	public static Collection getActiveFeatures() {
		FeatureTemplates template=getTemplate(getGlobalSettingValue("Feature Template"));
		return getTemplateFeatures(template.getTemplateId());
	/*	Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + AmpFeature.class.getName() + " f" +
					" where f.active = true";
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
		*/
		
	}

	public static AmpFeature toggleFeature(Integer featureId) {
		Session session = null;
		Transaction tx = null;
		AmpFeature feature = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			feature = (AmpFeature) session.load(AmpFeature.class,
					featureId);
			feature.setActive(!feature.isActive());
			session.update(feature);
			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Transaction rollback failed :" + rbf.getMessage());
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return feature;
	}

	/**
	 * @author dan
	 */
	public static boolean existTemplate(String templateName) {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + FeatureTemplates.class.getName() + " f" +
					" where f.featureTemplateName = '"+templateName+"'";
			qry = session.createQuery(qryStr);
			col = qry.list();
			if(col==null) return false;
			if(col.size()==0) return false;
			if(col.size()>0) return true;
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
		return true;
	}

	/**
	 * @author dan
	 */
	public static FeatureTemplates getTemplate(String templateId) {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + FeatureTemplates.class.getName() + " f" +
					" where f.templateId = '"+templateId+"'";
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
		if(!col.isEmpty())
		{
			Iterator it=col.iterator();
		
			FeatureTemplates x=(FeatureTemplates) it.next();
			return x;
		}
		else return null;
	}

	
	/**
	 * @author dan
	 */
	public static boolean deleteTemplate(Long id) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx=session.beginTransaction();
			FeatureTemplates ft=new FeatureTemplates();
			ft=(FeatureTemplates)session.load(FeatureTemplates.class,id);
			session.delete(ft);
			tx.commit();
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
		return true;
	}


	/**
	 * @author dan
	 */
	public static Collection getTemplateFeatures(Long id) {
		Session session = null;
		FeatureTemplates ft=new FeatureTemplates();
		try {
			session = PersistenceManager.getSession();
			ft=(FeatureTemplates)session.load(FeatureTemplates.class,id);
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
		return ft.getFeatures();
	}
	
	/**
	 * @author dan
	 */
	public static String getTemplateName(Long id) {
		Session session = null;
		FeatureTemplates ft=new FeatureTemplates();
		try {
			session = PersistenceManager.getSession();
			ft=(FeatureTemplates)session.load(FeatureTemplates.class,id);
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
		return ft.getFeatureTemplateName();
	}
	
	/**
	 * 
	 * @author dan
	 *
	 * @return
	 */
	public static void insertTemplateFeatures(Collection features, String template) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			FeatureTemplates ampTemplate=new FeatureTemplates();
			ampTemplate.setFeatureTemplateName(template);
			ampTemplate.setFeatures(new HashSet());
			for(Iterator it=features.iterator();it.hasNext();)
				{	
					AmpFeature ampFeature=(AmpFeature)it.next();
					ampTemplate.getFeatures().add(ampFeature);
				}
			session.save(ampTemplate);
			tx.commit();
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

		return ;
	}

	/**
	 * 
	 * @author dan
	 *
	 * @return
	 */
	public static void updateTemplateFeatures(Collection features, Long templateId, String templateName) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			FeatureTemplates ampTemplate=new FeatureTemplates();
			ampTemplate=(FeatureTemplates)session.load(FeatureTemplates.class,templateId);
			ampTemplate.setFeatureTemplateName(templateName);
		    System.out.println(ampTemplate.getFeatureTemplateName());
			ampTemplate.setFeatures(new HashSet());
			//ampTemplate.getFeatures().addAll(features);
			for(Iterator it=features.iterator();it.hasNext();)
				{	
					AmpFeature ampFeature=(AmpFeature)it.next();
					//boolean found=false;
					//for(Iterator jt=ampTemplate.getFeatures().iterator();jt.hasNext();)
					//{
						//AmpFeature ampFeature2=(AmpFeature)jt.next();
						//if(ampFeature.getName().compareTo(ampFeature2.getName())==0) {found=true;break;}
					//}
					//if(!found) 
					ampTemplate.getFeatures().add(ampFeature);
				}
			session.saveOrUpdate(ampTemplate);
			tx.commit();
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

		return ;
	}

	
	
	public static Collection getAllCountries() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select c.countryId,c.countryName from " + Country.class.getName() + " c order by c.countryName";
			qry = session.createQuery(qryStr);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				Object obj[] = (Object[]) itr.next();
				Long cId = (Long) obj[0];
				String cName = (String) obj[1];
				org.digijava.module.aim.helper.Country
					ctry = new org.digijava.module.aim.helper.Country();
				ctry.setId(cId);
				ctry.setName(cName);
				col.add(ctry);
			}

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

	public static Collection getAllCountryFlags() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		String params = "";

		try {
			session = PersistenceManager.getSession();
			qryStr = "select f.countryId,f.defaultFlag from " + AmpSiteFlag.class.getName() + " f";
			qry = session.createQuery(qryStr);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				Object obj[] = (Object[]) itr.next();
				Long cId = (Long) obj[0];
				Boolean defFlag = (Boolean) obj[1];
				Flag f = new Flag();
				if (params != null && params.trim().length() > 0) {
					params += ",";
				}
				params += cId.longValue();

				f.setCntryId(cId);
				f.setDefaultFlag(defFlag.booleanValue());
				col.add(f);
			}


			if (params != null && params.trim().length() > 0) {
				qryStr = "select c.countryId,c.countryName from " + Country.class.getName() + " c" +
					" where c.countryId in (" + params + ")";

				qry = session.createQuery(qryStr);
				itr = qry.list().iterator();
				while (itr.hasNext()) {
					Object obj[] = (Object[]) itr.next();
					Long cId = (Long) obj[0];
					String cName = (String) obj[1];
					long temp = cId.longValue();

					Iterator itr1 = col.iterator();
					while (itr1.hasNext()) {
						Flag f = (Flag) itr1.next();
						if (f.getCntryId().longValue() == temp) {
							f.setCntryName(cName);
						}
					}

				}
			}
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

	public static AmpSiteFlag getAmpSiteFlag(Long id) {
		Session session = null;
		AmpSiteFlag flag = null;

		try {
			session = PersistenceManager.getSession();
			flag = (AmpSiteFlag) session.get(AmpSiteFlag.class,id);
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

		return flag;
	}

	public static byte[] getFlag(Long id) {
		Session session = null;
		byte flag[] = null;

		try {
			session = PersistenceManager.getSession();
			AmpSiteFlag tmp = (AmpSiteFlag) session.get(AmpSiteFlag.class,id);
			if (tmp != null) {
				flag = tmp.getFlag();
			}

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

		return flag;
	}

	public static byte[] getDefaultFlag() {
		Session session = null;
		byte flag[] = null;
		String qryStr = null;
		Query qry = null;

		try {
			qryStr = "select f from " + AmpSiteFlag.class.getName() + " f " +
					"where f.defaultFlag=true";
			session = PersistenceManager.getSession();
			qry = session.createQuery(qryStr);
			AmpSiteFlag sf = null;
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				sf = (AmpSiteFlag) itr.next();
			}
			if (sf != null) {
				flag = sf.getFlag();
			}

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

		return flag;
	}

	public static boolean defaultFlagExist() {
		Session session = null;
		boolean exist = false;
		String qryStr = null;
		Query qry = null;

		try {
			qryStr = "select count(*) from " + AmpSiteFlag.class.getName() + " f " +
					"where f.defaultFlag=true";
			session = PersistenceManager.getSession();
			qry = session.createQuery(qryStr);
			Iterator itr = qry.list().iterator();
			Integer num = null;
			if (itr.hasNext()) {
				num = (Integer) itr.next();
			}
			if (num.intValue() > 0) {
				exist = true;
			}

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

		return exist;
	}

	public static void setDefaultFlag(Long id) {
		Session session = null;
		Transaction tx = null;
		String qryStr = null;
		Query qry = null;
		try {
			session = PersistenceManager.getSession();
			qryStr = "select s from " + AmpSiteFlag.class.getName() + " s " +
					"where s.defaultFlag=true";
			qry = session.createQuery(qryStr);
			Iterator itr = qry.list().iterator();
			AmpSiteFlag defFlag = null;
			if (itr.hasNext()) {
				defFlag = (AmpSiteFlag) itr.next();
			}
			AmpSiteFlag newDefFlag = (AmpSiteFlag) session.load(AmpSiteFlag.class,id);
			tx = session.beginTransaction();
			newDefFlag.setDefaultFlag(true);
			session.update(newDefFlag);
			if (defFlag != null) {
				defFlag.setDefaultFlag(false);
				session.update(defFlag);
			}
			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
	}
	
	
	public static String getGlobalSettingValue(String globalSettingName) {
		Collection settings=null;
		settings=getGlobalSettingsCache();
		if(settings==null) {
			settings=getGlobalSettings();
			setGlobalSettingsCache(settings);
		}
		
		Iterator i=settings.iterator();
		while (i.hasNext()) {
			AmpGlobalSettings element = (AmpGlobalSettings) i.next();
			if(element.getGlobalSettingsName().equals(globalSettingName)) return element.getGlobalSettingsValue();
		}
		return null;
	}
	/**
	 * 
	 * @author dan
	 * made for visibility module
	 */
	public static Long getGlobalSettingValueLong(String globalSettingName) {
		return new Long(Long.parseLong(getGlobalSettingValue(globalSettingName)));
	}
	
	/*
	 * edited by Govind G Dalwani
	 */
	/*
	 * to get all the Global settings
	 */
	public static Collection getGlobalSettings()
	{
		Collection coll = null;
		Session session = null;
		Transaction tx = null;
		String qryStr = null;
		Query qry = null;
		try{
				session = PersistenceManager.getSession();
				qryStr = "select gs from " + AmpGlobalSettings.class.getName() + " gs " ;
				qry = session.createQuery(qryStr);
				coll=qry.list();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return coll;
	}
	/*
	 * to get the country names
	 */
	public static Collection getCountryNames()
	{
		Collection col = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		try{
			session = PersistenceManager.getSession();
			qryStr = "select cn from " + Country.class.getName() + " cn order by cn.countryName" ;
			qry = session.createQuery(qryStr);
			col=qry.list();

			}
			catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null) {
					try {
						tx.rollback();
					} catch (Exception rbf) {
						logger.error("Rollback failed !");
					}
				}
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
	/*
	 * to get the country ISO that is set as a default value...
	 */
	public static Collection getDefaultCountryISO()
	{
		Collection col = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		try{
			session = PersistenceManager.getSession();
			qryStr = "select gs from " + AmpGlobalSettings.class.getName() + " gs where gs.globalSettingsName = 'Default Country' " ;
			qry = session.createQuery(qryStr);
			col=qry.list();

			}
			catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null) {
					try {
						tx.rollback();
					} catch (Exception rbf) {
						logger.error("Rollback failed !");
					}
				}
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
	/*
	 * to get the country name from the Iso got
	 */
	public static Collection getDefaultCountry(String ISO)
	{
		Collection col = null;
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Transaction tx = null;
		String a ="in the get country...";
		logger.info(a);
		try{
			session = PersistenceManager.getSession();
			qryStr = "select cn from " + Country.class.getName() + " cn where cn.iso = '"+ ISO +"'" ;
			qry = session.createQuery(qryStr);
			col=qry.list();

			}
			catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
				if (tx != null) {
					try {
						tx.rollback();
					} catch (Exception rbf) {
						logger.error("Rollback failed !");
					}
				}
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
	 * @author dan
	 *
	 * @return
	 */
		public static Collection getAMPTemplatesVisibility() {
			Session session = null;
			Collection col = new ArrayList();
			String qryStr = null;
			Query qry = null;

			try {
				session = PersistenceManager.getSession();
				qryStr = "select f from " + AmpTemplatesVisibility.class.getName() + " f order by f.name asc";
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
		 * @author dan
		 *
		 * @return
		 */
			public static Collection getAMPModulesVisibility() {
				Session session = null;
				Collection col = new ArrayList();
				String qryStr = null;
				Query qry = null;

				try {
					session = PersistenceManager.getSession();
					qryStr = "select f from " + AmpModulesVisibility.class.getName() + " f order by f.name asc";
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
			public static boolean existTemplateVisibility(String templateName) {
				Session session = null;
				Collection col = new ArrayList();
				String qryStr = null;
				Query qry = null;

				try {
					session = PersistenceManager.getSession();
					qryStr = "select f from " + AmpTemplatesVisibility.class.getName() + " f" +
							" where f.name = '"+templateName+"'";
					qry = session.createQuery(qryStr);
					col = qry.list();
					if(col==null) return false;
					if(col.size()==0) return false;
					if(col.size()>0) return true;
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
				return true;
			}
			/**
			 * 
			 * @author dan
			 *
			 * @return
			 */
			public static Collection getAMPModules() {
				Session session = null;
				Collection col = new ArrayList();
				String qryStr = null;
				Query qry = null;

				try {
					session = PersistenceManager.getSession();
					qryStr = "select f from " + AmpModulesVisibility.class.getName() + " f order by f.name asc";
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
			 * @author dan
			 *
			 * @return
			 */
			public static void insertTemplate(String templateName, Session session) {
				Transaction tx = null;

				try {
					session = PersistenceManager.getSession();
					tx = session.beginTransaction();
					AmpTemplatesVisibility ampTemplate=new AmpTemplatesVisibility();
					ampTemplate.setName(templateName);
					session.save(ampTemplate);
					tx.commit();
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

				return ;
			}

			/**
			 * @author dan
			 */
			public static Collection getTemplateModules(Long id) {
				Session session = null;
				AmpTemplatesVisibility ft=new AmpTemplatesVisibility();
				try {
					session = PersistenceManager.getSession();
					ft=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,id);
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
				return ft.getItems();
			}
			
			/**
			 * @author dan
			 */
			public static String getTemplateNameVisibility(Long id) {
				Session session = null;
				AmpTemplatesVisibility ft=new AmpTemplatesVisibility();
				try {
					session = PersistenceManager.getSession();
					ft=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,id);
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
				return ft.getName();
			}

			/**
			 * @author dan
			 * @param session 
			 * @throws HibernateException 
			 */
			public static AmpTemplatesVisibility getTemplateVisibility(Long id, Session session) throws HibernateException {
				AmpTemplatesVisibility ft=new AmpTemplatesVisibility();
					ft=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,id);
					
					List list = session.createQuery("from "+AmpModulesVisibility.class.getName()).list();
					ft.setAllItems(new TreeSet(list));
					
				return ft;
			}
			
			
			/**
			 * 
			 * @author dan
			 *
			 * @return
			 */
			public static void updateModulesTemplate(Collection modules, Long templateId, String templateName) {
				Session session = null;
				Transaction tx = null;

				try {
					session = PersistenceManager.getRequestDBSession();
					AmpTemplatesVisibility ampTemplate;
					tx = session.beginTransaction();
					ampTemplate=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,templateId);
					ampTemplate.setItems(null);
					session.saveOrUpdate(ampTemplate);
					tx.commit(); 
				} catch (Exception ex) {
					logger.error("Exception :::: " + ex.getMessage());
				} finally {
					if (session != null) {
						try {
							PersistenceManager.releaseSession(session);
						} catch (Exception rsf) {
							logger.error("Release session failed :" + rsf.getMessage());
						}
					}
				}

				try {
					session = PersistenceManager.getRequestDBSession();
					AmpTemplatesVisibility ampTemplate;
					tx = session.beginTransaction();
					ampTemplate=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,templateId);
					ampTemplate.setName(templateName);
					//ampTemplate.setVisible("true");
					for(Iterator it=modules.iterator();it.hasNext();)
						{	
							AmpModulesVisibility ampModule=(AmpModulesVisibility)it.next();
							ampTemplate.getItems().add(ampModule);
						}
					session.saveOrUpdate(ampTemplate);
					tx.commit(); 
				} catch (Exception ex) {
					logger.error("Exception ;;;; " + ex.getMessage());
				} finally {
					if (session != null) {
						try {
							PersistenceManager.releaseSession(session);
						} catch (Exception rsf) {
							logger.error("Release session failed :" + rsf.getMessage());
						}
					}
				}
			
				return ;
			}

			/**
			 * @author dan
			 */
			public static boolean deleteTemplateVisibility(Long id, Session session) {
				Transaction tx = null;

				try {
					//tx=session.beginTransaction();
					AmpTemplatesVisibility ft=new AmpTemplatesVisibility();
					ft=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,id);
					session.delete(ft);
					session.flush();
					//tx.commit();
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
				return true;
			}
			
			/**
			 * @author dan
			 */
			public static AmpModulesVisibility getModuleVisibility(String moduleName) {

				Session session = null;
				Query q = null;
				Collection c = null;
				AmpModulesVisibility id = null;
				Iterator iter = null;

				try {
					session = PersistenceManager.getRequestDBSession();
					String queryString = new String();
					queryString = "select a from " + AmpModulesVisibility.class.getName()
							+ " a where (a.moduleName=:moduleName) ";
					q = session.createQuery(queryString);
					q.setParameter("moduleName", moduleName, Hibernate.STRING);
					c = q.list();
					if (c.size() != 0) {
						iter = c.iterator();
						if (iter.hasNext()) {
							id = (AmpModulesVisibility) iter.next();
						}
					} else {
						if (logger.isDebugEnabled())
							logger.debug("No page with corresponding name");
					}
				} catch (Exception ex) {
					if (logger.isDebugEnabled())
						logger.error("Unable to get page id  from database", ex);
				}finally {
					if (session != null) {
						try {
							PersistenceManager.releaseSession(session);
						} catch (Exception rsf) {
							logger.error("Release session failed :" + rsf.getMessage());
						}
					}
					}
				if (logger.isDebugEnabled())
					logger.debug("getPageId() returning page id:" + id);
				return id;
			}

			
			/**
			 * @author dan
			 */
			public static Collection getModuleFeatures(Long id) {
				Session session = null;
				AmpModulesVisibility ft=new AmpModulesVisibility();
				try {
					session = PersistenceManager.getSession();
					ft=(AmpModulesVisibility)session.load(AmpModulesVisibility.class,id);
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
				return ft.getItems();
			}

			/**
			 * @author dan
			 */
			public static String getModuleNameVisibility(Long id) {
				Session session = null;
				AmpModulesVisibility ft=new AmpModulesVisibility();
				try {
					session = PersistenceManager.getSession();
					ft=(AmpModulesVisibility)session.load(AmpModulesVisibility.class,id);
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
				return ft.getName();
			}
			/**
			 * @author dan
			 */
			public static Collection getFeaturesOfModules(Long id) {
				Session session = null;
				AmpModulesVisibility ft=new AmpModulesVisibility();
				try {
					session = PersistenceManager.getSession();
					ft=(AmpModulesVisibility)session.load(AmpModulesVisibility.class,id);
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
				return ft.getItems();
			}

			/**
			 * 
			 * @author dan
			 *
			 * @return
			 */
			public static void updateFeaturesModule(Collection modules, Long templateId, String templateName) {
				Session session = null;
				Transaction tx = null;

				try {
					session = PersistenceManager.getRequestDBSession();
					AmpModulesVisibility ampModule;
					tx = session.beginTransaction();
					ampModule=(AmpModulesVisibility)session.load(AmpModulesVisibility.class,templateId);
					//ampModule.setItems(null);
					boolean found=false;
					for(Iterator it=ampModule.getItems().iterator();it.hasNext();)
					{
						AmpFeaturesVisibility fDb=(AmpFeaturesVisibility) it.next();
						found=false;
						for(Iterator jt=modules.iterator();jt.hasNext();)
						{
							AmpFeaturesVisibility fRqst=(AmpFeaturesVisibility) jt.next();
							if(fRqst.getId().compareTo(fDb.getId())==0) {found=true;break;}
						}
						//if(found) fDb.setVisible("true");
						//else fDb.setVisible("false");
					}
					session.saveOrUpdate(ampModule);
					tx.commit(); 
				} catch (Exception ex) {
					logger.error("Exception :::: " + ex.getMessage());
				} finally {
					if (session != null) {
						try {
							PersistenceManager.releaseSession(session);
						} catch (Exception rsf) {
							logger.error("Release session failed :" + rsf.getMessage());
						}
					}
				}

			
				return ;
			}

			/**
			 * 
			 * @author dan
			 *
			 * @return
			 */
			public static void updateAmpTreeVisibility(Collection modules, Collection features, Collection fields, Long templateId) {
				Session session = null;
				Transaction tx = null;
				AmpTemplatesVisibility ampTemplate=new AmpTemplatesVisibility();
				try {
					session = PersistenceManager.getSession();
					tx=session.beginTransaction();
					ampTemplate=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,templateId);
					ampTemplate.getItems().retainAll(modules);
					ampTemplate.getItems().addAll(modules);
					ampTemplate.getFeatures().retainAll(features);
					ampTemplate.getFeatures().addAll(features);
					//ampTemplate.getFields().retainAll(fields);
					//ampTemplate.getFields().addAll(fields);
					tx.commit(); 
				} catch (Exception ex) {
					logger.error("Exception : " + ex.getMessage());
					ex.printStackTrace();
				} finally {
					if (session != null) {
						try {
							PersistenceManager.releaseSession(session);
						} catch (Exception rsf) {
							logger.error("Release session failed :" + rsf.getMessage());
						}
					}
				}
				return ;
			}

			/**
			 * 
			 * @author dan
			 * @param session 
			 *
			 * @return
			 * @throws HibernateException 
			 */
			public static void updateAmpModulesTreeVisibility(Collection modules, Long templateId, Session session) throws HibernateException {
				Transaction tx = null;
				AmpTemplatesVisibility ampTemplate=new AmpTemplatesVisibility();
					tx=session.beginTransaction();
					ampTemplate=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,templateId);
					ampTemplate.getItems().retainAll(modules);
					ampTemplate.getItems().addAll(modules);
					tx.commit(); 
				return ;
			}
			
			/**
			 * 
			 * @author dan
			 * @param session 
			 *
			 * @return
			 * @throws HibernateException 
			 */
			public static void updateAmpTemplateNameTreeVisibility(String templateName, Long templateId, Session session) throws HibernateException {
				Transaction tx = null;
				AmpTemplatesVisibility ampTemplate=new AmpTemplatesVisibility();
					tx=session.beginTransaction();
					ampTemplate=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,templateId);
					ampTemplate.setName(templateName);
					tx.commit(); 
				return ;
			}
			
			/**
			 * 
			 * @author dan
			 *
			 * @return
			 * @throws SQLException 
			 * @throws HibernateException 
			 */
			public static void updateAmpFeaturesTreeVisibility(Collection features, Long templateId,Session session) throws HibernateException, SQLException {
				Transaction tx = null;
				AmpTemplatesVisibility ampTemplate=new AmpTemplatesVisibility();
					tx=session.beginTransaction();
					ampTemplate=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,templateId);

					ampTemplate.getFeatures().retainAll(features);
					ampTemplate.getFeatures().addAll(features);
					tx.commit(); 
				return ;
			}
			/**
			 * 
			 * @author dan
			 * @param session 
			 *
			 * @return
			 * @throws HibernateException 
			 */
			public static void updateAmpFieldsTreeVisibility(Collection fields, Long templateId, Session session) throws HibernateException {
				Transaction tx = null;
				AmpTemplatesVisibility ampTemplate=new AmpTemplatesVisibility();
					tx=session.beginTransaction();
					ampTemplate=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,templateId);
					ampTemplate.getFields().retainAll(fields);
					ampTemplate.getFields().addAll(fields);
					tx.commit(); 
				return ;
			}

			
			/**
			 * @author dan
			 */
			public static void insertFieldWithFeatureVisibility(Long templateId, Long featureId, String fieldName) {
				Session session = null;
				AmpFeaturesVisibility feature=new AmpFeaturesVisibility();
				AmpFieldsVisibility field=new AmpFieldsVisibility();
				AmpTemplatesVisibility template=null;
				Transaction tx;
				try {
					session = PersistenceManager.getSession();
					tx=session.beginTransaction();
					feature=(AmpFeaturesVisibility)session.load(AmpFeaturesVisibility.class,featureId);
					field.setParent(feature);
					field.setName(fieldName);					
					session.save(field);
					tx.commit();
					tx=session.beginTransaction();
					template=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,templateId);
					template.getFields().add(field);
					tx.commit();
					//session.saveOrUpdate(template);
					//tx.commit();
					
				} catch (Exception ex) {
					logger.error("Exception : " + ex.getMessage());
					ex.printStackTrace();
				} finally {
					if (session != null) {
						try {
							PersistenceManager.releaseSession(session);
						} catch (Exception rsf) {
							logger.error("Release session failed :" + rsf.getMessage());
						}
					}
				}
				return ;
			}

			/**
			 * @author dan
			 */
			public static void insertFeatureWithModuleVisibility(Long templateId, Long moduleId, String featureName) {
				Session session = null;
				AmpModulesVisibility module=new AmpModulesVisibility();
				AmpFeaturesVisibility feature=new AmpFeaturesVisibility();
				AmpTemplatesVisibility template=null;
				Transaction tx;
				try {
					session = PersistenceManager.getSession();
					tx=session.beginTransaction();
					module=(AmpModulesVisibility)session.load(AmpModulesVisibility.class,moduleId);
					feature.setParent(module);
					feature.setName(featureName);					
					session.save(feature);
					tx.commit();
					tx=session.beginTransaction();
					template=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,templateId);
					template.getFeatures().add(feature);
					tx.commit();
					//session.saveOrUpdate(template);
					//tx.commit();
					
				} catch (Exception ex) {
					logger.error("Exception : " + ex.getMessage());
					ex.printStackTrace();
				} finally {
					if (session != null) {
						try {
							PersistenceManager.releaseSession(session);
						} catch (Exception rsf) {
							logger.error("Release session failed :" + rsf.getMessage());
						}
					}
				}
				return ;
			}


			/**
			 * @author dan
			 */
			public static void insertModuleVisibility(Long templateId, String moduleName) {
				Session session = null;
				AmpModulesVisibility module=new AmpModulesVisibility();
				AmpTemplatesVisibility template=null;
				Transaction tx;
				try {
					session = PersistenceManager.getSession();
					tx=session.beginTransaction();
					module.setName(moduleName);
					session.save(module);
					tx.commit();
					tx=session.beginTransaction();
					template=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,templateId);
					template.getItems().add(module);
					tx.commit();
					//session.saveOrUpdate(template);
					//tx.commit();
					
				} catch (Exception ex) {
					logger.error("Exception : " + ex.getMessage());
					ex.printStackTrace();
				} finally {
					if (session != null) {
						try {
							PersistenceManager.releaseSession(session);
						} catch (Exception rsf) {
							logger.error("Release session failed :" + rsf.getMessage());
						}
					}
				}
				return ;
			}

			
			/**
			 * @author dan
			 */
			public static void updateFieldWithFeatureVisibility(Long featureId, String fieldName) {
				Session session = null;
				AmpFeaturesVisibility feature=new AmpFeaturesVisibility();
				AmpFieldsVisibility field=new AmpFieldsVisibility();
				Query qry;
				Collection col=new ArrayList();
				String qryStr;
				Transaction tx;
				try {
					session = PersistenceManager.getSession();
					tx=session.beginTransaction();
					feature=(AmpFeaturesVisibility)session.load(AmpFeaturesVisibility.class,featureId);
					qryStr = "select f from " + AmpFieldsVisibility.class.getName() + " f"
					+" where f.name = '"+fieldName+"'";;
					qry = session.createQuery(qryStr);
					col = qry.list();
					field=(AmpFieldsVisibility) col.iterator().next();
					feature.getItems().add(field);
					field.setParent(feature);
					session.saveOrUpdate(field);
					tx.commit();
					
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
				return ;
			}

			/**
			 * @author dan
			 */
			public static void updateFeatureWithModuleVisibility(Long moduleId, String featureName) {
				Session session = null;
				AmpModulesVisibility module=new AmpModulesVisibility();
				AmpFeaturesVisibility feature=new AmpFeaturesVisibility();
				Query qry;
				Collection col=new ArrayList();
				String qryStr;
				Transaction tx;
				try {
					
					session = PersistenceManager.getSession();
					tx=session.beginTransaction();
					module=(AmpModulesVisibility)session.load(AmpModulesVisibility.class,moduleId);
					qryStr = "select f from " + AmpFeaturesVisibility.class.getName() + " f"
					+" where f.name = '"+featureName+"'";
					qry = session.createQuery(qryStr);
					col = qry.list();
					feature=(AmpFeaturesVisibility) col.iterator().next();
					module.getItems().add(feature);
					feature.setParent(module);
					session.saveOrUpdate(feature);
					tx.commit();
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
				return ;
			}

			
			
			/**
			 * @author dan
			 */
			public static AmpTemplatesVisibility getTemplateById(Long id) {
				Session session = null;
				AmpTemplatesVisibility ft=new AmpTemplatesVisibility();
				try {
					session = PersistenceManager.getSession();
					ft=(AmpTemplatesVisibility)session.load(AmpTemplatesVisibility.class,id);
					List list = session.createQuery("from "+AmpModulesVisibility.class.getName()).list();
					ft.setAllItems(new TreeSet(list));
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
				return ft;
			}

			
}
