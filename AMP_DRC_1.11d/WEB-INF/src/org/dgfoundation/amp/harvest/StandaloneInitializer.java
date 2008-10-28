/**
 * StandaloneInitializer.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.harvest;

import java.sql.SQLException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 8, 2006
 *
 */
public class StandaloneInitializer {

public static SessionFactory initialize() {
	

	ResourceStreamHandlerFactory.installIfNeeded();
	try {
		DigiConfigManager
				.initialize("/home/dan/workspace/amp/repository");
		PersistenceManager.initialize(false);
	} catch (DgException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		SessionFactory sessionFactory = PersistenceManager.getSessionFactory();
		return sessionFactory;		
			

}	
}
