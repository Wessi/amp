/**
 * 
 */
package org.digijava.module.mondrian.job;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.autopatcher.core.AutopatcherService;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;

/**
 * @author mihai
 *
 */
public class RefreshMondrianCacheJob implements StatefulJob {
	private static Logger logger = Logger.getLogger(RefreshMondrianCacheJob.class);
	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		ServletContext ctx = null;
		Connection connection = null;
		Session session = null;
		try {
			ctx = (ServletContext) arg0.getScheduler().getContext().get(Constants.AMP_SERVLET_CONTEXT);
		
		String patchFile = ctx.getRealPath("/repository/mondrian/sql/refresh_mondrian_cache.sql");
		
		LineNumberReader bis = new LineNumberReader(new FileReader(patchFile));
		StringBuffer sb = new StringBuffer();
		String s = bis.readLine();
		while (s != null) {
			sb.append(s);
			s = bis.readLine();
		}
		
		session = PersistenceManager.getSession();
		connection = session.connection();
		
		connection.setAutoCommit(false);

		StringTokenizer stok = new StringTokenizer(sb.toString(),";");
			
			Statement st = connection.createStatement();
			
			while (stok.hasMoreTokens()) {
				String sqlCommand = stok.nextToken();
				if (sqlCommand.trim().equals(""))
					continue;
				st.addBatch(sqlCommand);
			}
			st.executeBatch();
			connection.commit();
		
		}
		catch (SchedulerException e) {
			logger.error(e);
			e.printStackTrace();
			return;
		} catch (FileNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
			return;
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
			return;
		
	} finally {
		try {
			connection.setAutoCommit(true);
			PersistenceManager.releaseSession(session);
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
			return;			
		}
	}
		logger.info("Refresh Mondrian Cache Job Successful!");
	}
	

}
