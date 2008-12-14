/**
 * 
 */
package org.dgfoundation.amp;

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.ampharvester.api.ExportManager;
import org.digijava.module.ampharvester.api.ImportManager;
import org.digijava.module.ampharvester.jaxb10.Activities;
import org.digijava.module.ampharvester.jaxb10.ActivityType;
import org.digijava.module.ampharvester.jaxb10.ObjectFactory;
import org.digijava.module.ampharvester.jaxb10.impl.FreeTextTypeImpl;
import org.digijava.module.ampharvester.util.XmlTransformerHelper;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

/**
 * @author mihai
 * This is a standalone initializer for digi/hibernate, that can be use to patch/inspect/query
 * the amp database without jboss/tomcat running.
 */
public class StandaloneAMPStartup {
	private static Logger logger = Logger.getLogger(StandaloneAMPStartup.class);
	
	
	  public static AmpActivity loadActivity(Long id, Session session) throws DgException {
		AmpActivity result = null;

		try {
			session.flush();
			result = (AmpActivity) session.get(AmpActivity.class, id);
			session.evict(result);
			result = (AmpActivity) session.get(AmpActivity.class, id);
		} catch (ObjectNotFoundException e) {
			logger.debug("AmpActivity with id=" + id + " not found");
		} catch (Exception e) {
			throw new DgException("Canno load AmpActivity with id" + id, e);
		}
		return result;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceStreamHandlerFactory.installIfNeeded();
		try {
			
			StandaloneJndiAMPInitializer.initAMPUnifiedJndiAlias();
			
			DigiConfigManager.initialize("repository");
			PersistenceManager.initialize(false);
			
			
			
			//BELOW THIS LINE, HIBERNATE IS AVAILABLE, ADD YOUR SCRIPT INVOCATION HERE			
			
			
			
			try {
				//EXAMPLE OF A WORKING HIBERNATE SESSION OBJECT:
				Session session = PersistenceManager.getSession();
				AmpActivity activity = loadActivity(new Long(9),session);				
				ActivityType xmlActivity = ExportManager.getXmlActivity(activity, session);
				
				
				ObjectFactory objFactory = new ObjectFactory();
				Activities aList=objFactory.createActivities();
				aList.getActivity().add(xmlActivity);
				
				xmlActivity.setAmpId("200K ");
				xmlActivity.getTitle().setValue("Generated Activity ");
				byte[] toByte = XmlTransformerHelper.marshalToByte(aList);
				
				String title=xmlActivity.getTitle().getValue();
				ImportManager im=new ImportManager(toByte);
				for(long i=15699;i<200000;i++){ 
				im.startImportHttp(new String("#"+i), activity.getTeam());
				if((i % 100)==0) logger.info("Completed #"+i);
				}
				PersistenceManager.releaseSession(session);
				
							
				
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
			} catch (JAXBException e) {
			    // TODO Auto-generated catch block
			    logger.error(e);
			    throw new RuntimeException( "JAXBException Exception encountered", e);
			}
			
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}

	}

}
