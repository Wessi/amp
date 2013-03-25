package org.digijava.module.message.jobs;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.message.dbentity.AmpEmail;
import org.digijava.module.message.dbentity.AmpEmailReceiver;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.util.AmpMessageUtil;
import org.digijava.module.um.util.AmpUserUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class SendReminderEmailJob implements StatefulJob{
	private static Logger logger = Logger.getLogger(SendReminderEmailJob.class);
	
	private static final String MAIL_SENDER="ampsite@dg.org";
	private static final String MAIL_SUBJECT="Reminder to enter to amp site";
	private static final String MAIL_BODY="You have a period of innactivity on ampsite, dont forget to use it";
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Collection<AmpGlobalSettings> col = FeaturesUtil.getGlobalSettings();
		String name;
		String value;
		int period = 0;
		int quequeSize = 0;
		for (Iterator iterator = col.iterator(); iterator.hasNext();) {
			AmpGlobalSettings ampGls = (AmpGlobalSettings) iterator.next();
			name = ampGls.getGlobalSettingsName();
			value = ampGls.getGlobalSettingsValue();
			if (name.equalsIgnoreCase(GlobalSettingsConstants.REMINDER_TIME)) {
				period = Integer.parseInt(value);
			} 
		}
		Session hibernateSession = null;

		try {
			hibernateSession = PersistenceManager.openNewSession();
			hibernateSession.setFlushMode(FlushMode.MANUAL);
			hibernateSession.beginTransaction();
			
			Date compareDate = AmpDateUtils.getDateBeforeDays(new Date(),
					period);
			List<String> reminderUsers = AmpUserUtil.getUserReminder(compareDate);
			boolean asHtml = true;
	        boolean log = true;
	        boolean rtl = false;
	        String from;
	        String subject;
	        String text;
			if(reminderUsers!=null && reminderUsers.size()>0){
				
				logger.info("Enter DG Reminder Email manager");
				for(int i=0;i<reminderUsers.size();i++){
					InternetAddress[] emailAddrs =  new InternetAddress[]{new InternetAddress(reminderUsers.get(i))};
					User user = UserUtils.getUserByEmail(reminderUsers.get(i));
					from = TranslatorWorker.translateText(MAIL_SENDER, user.getRegisterLanguage().getCode(), "3");
					text = TranslatorWorker.translateText(MAIL_SUBJECT, user.getRegisterLanguage().getCode(), "3");
					subject = TranslatorWorker.translateText(MAIL_BODY, user.getRegisterLanguage().getCode(), "3");
					DgEmailManager.sendMail(emailAddrs, from, null, null, subject, text, "UTF8", asHtml, log, rtl);
					AuditLoggerUtil.logSentReminderEmails(hibernateSession,user);
				}
		        
				logger.info("Sent reminder emails");
				
			}
			hibernateSession.getTransaction().commit();
			hibernateSession.flush();
			hibernateSession.setFlushMode(FlushMode.AUTO);
		} catch (Throwable t) {
			try {
				logger.error("Error while flushing session:", t);
				if (hibernateSession.getTransaction().isActive()) {
					logger.info("Trying to rollback database transaction after exception");
					hibernateSession.getTransaction().rollback();
				} else
					logger.error("Can't rollback transaction because transaction not active");
			} catch (Throwable rbEx) {
				logger.error("Could not rollback transaction after exception!",
						rbEx);
			}
		} finally {
			hibernateSession.close();
		}
		
	}

}
