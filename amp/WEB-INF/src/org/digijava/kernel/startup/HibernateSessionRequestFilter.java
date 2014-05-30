/**
 * 
 */
package org.digijava.kernel.startup;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;

/**
 * @author mihai
 * {@link http://community.jboss.org/wiki/OpenSessionInView}
 */
public class HibernateSessionRequestFilter implements Filter {
	private static Logger log = Logger.getLogger(HibernateSessionRequestFilter.class);
	/**
	 * 
	 */
	public HibernateSessionRequestFilter() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {

            log.debug("Starting a database transaction");
            
            Transaction transaction = PersistenceManager.getCurrentSession().getTransaction();

            if(transaction.isActive()) 
            	transaction.commit();
            transaction=PersistenceManager.getCurrentSession().beginTransaction();
            
 
            // Call the next filter (continue request processing)
            chain.doFilter(request, response);
 
            // Commit and cleanup
            log.debug("Committing the database transaction");
            Transaction tx	= PersistenceManager.getCurrentSession().getTransaction();
			if (tx!=null && tx.isActive())
				try {
					tx.commit();
				} catch (RuntimeException e) {
					tx.rollback();
					log.error("Transaction has been rolled back after exception during commit "
							+ e);
				}

            PersistenceManager.removeClosedSessionsFromMap();
            //PersistenceManager.checkClosedOrLongSessionsFromTraceMap();
            if ( request instanceof HttpServletRequest )
            	DocumentManagerUtil.closeJCRSessions((HttpServletRequest)request);
 
        } catch (StaleObjectStateException staleEx) {
            log.error("This interceptor does not implement optimistic concurrency control!");
            log.error("Your application will not work until you add compensation actions!");
            // Rollback, close everything, possibly compensate for any permanent changes
            // during the conversation, and finally restart business conversation. Maybe
            // give the user of the application a chance to merge some of his work with
            // fresh data... what you do here depends on your applications design.
            throw staleEx;
        } catch (Throwable ex) {
            // Rollback only
            ex.printStackTrace();
            PersistenceManager.rollbackCurrentSessionTx();
 
            // Let others handle it... maybe another interceptor for exceptions?
            throw new ServletException(ex);
        }

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
