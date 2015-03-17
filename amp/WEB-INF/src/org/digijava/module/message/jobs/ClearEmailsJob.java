package org.digijava.module.message.jobs;

import java.util.List;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.message.dbentity.AmpEmail;
import org.digijava.module.message.util.AmpMessageUtil;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * This Job is used to delete emails  from AmpEmails table, 
 * that were delivered to all recipients   
 * @author Dare
 *
 */
public class ClearEmailsJob implements StatefulJob{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
		} catch (DgException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			List <AmpEmail> emailsForRemoval=AmpMessageUtil.loadSentEmails(session);
			if(emailsForRemoval!=null && emailsForRemoval.size()>0){
				for (AmpEmail ampEmail : emailsForRemoval) {
					session.delete(ampEmail);
				}
			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		finally {
			PersistenceManager.cleanupSession(session);
		}
		
	}

}
