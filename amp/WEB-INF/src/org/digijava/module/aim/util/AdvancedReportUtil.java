/**
 * AdvancedReportUtil.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.aim.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.dbentity.AmpTeamReports;

/**
 * AdvancedReportUtil.java
 * TODO description here
 * @author mihai
 * @package org.digijava.module.aim.util
 * @since 11.09.2007
 */
public final class AdvancedReportUtil {
    
    private static Logger logger = Logger.getLogger(AdvancedReportUtil.class);
    
	public static void saveReport(AmpReports ampReports,Long ampTeamId,Long ampMemberId,boolean teamLead)
	{
		Session session = null;
		Transaction tx = null;
		String queryString=null;
		Query query=null;
		Iterator iter=null;
		Set pageFilters = new HashSet();
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			//session.save(ampReports);
			session.saveOrUpdate(ampReports);
			//ampReports.setDescription("/viewAdvancedReport.do?view=reset&ampReportId="+ampReports.getAmpReportId());
			//session.update(ampReports);
			
			if (ampReports.getMembers()==null){
				ampReports.setMembers(new HashSet());
			}
				
			
			AmpTeam ampTeam = (AmpTeam) session.get(AmpTeam.class, ampTeamId);
			
			
			if(teamLead == true&&(ampReports.getOwnerId()==null||(ampReports.getOwnerId().getAmpTeam().getTeamLead()!=null && 
					ampReports.getOwnerId().getAmpTeamMemId().equals(ampReports.getOwnerId().getAmpTeam().getTeamLead().getAmpTeamMemId())))
					)
			{
				//logger.info(teamMember.getMemberName() + " is Team Leader ");

				//Check if this assignment was already done.
				AmpTeamReports teamReports = TeamUtil.getAmpTeamReport(ampTeam.getAmpTeamId(), ampReports.getAmpReportId());
				if(teamReports == null)
				{
					AmpTeamReports ampTeamReports = new AmpTeamReports();
					
					ampTeamReports.setTeamView(true);				
					ampTeamReports.setTeam(ampTeam);
					ampTeamReports.setReport(ampReports);
					session.save(ampTeamReports);
				}
			}
//			else
//			{
				//logger.info(teamMember.getMemberName() + " is Team Memeber ");
				//Long lg = teamMember.getMemberId();
				AmpTeamMember ampTeamMember =null;
				if(ampReports.getOwnerId()!=null){
					ampTeamMember=(AmpTeamMember) session.get(AmpTeamMember.class, ampReports.getOwnerId().getAmpTeamMemId());	
				}else {
					ampTeamMember = (AmpTeamMember) session.get(AmpTeamMember.class, ampMemberId);	
				}					
				Set reportSet = ampTeamMember.getReports();
				
				//reportSet.add(ampReports);  // Not needed because it is set from ampReports object
				
				
				ampReports.getMembers().add(ampTeamMember);
				session.saveOrUpdate(ampTeamMember);
				//session.save(ampTeamMember);
//			}

			queryString = "select filters from " + AmpFilters.class.getName() + " filters ";
			//logger.info( " Filter Query...:: " + queryString);
			query = session.createQuery(queryString);
			if(query!=null)
			{
				iter = query.list().iterator();
				while(iter.hasNext())
				{
					AmpFilters filt = (AmpFilters) iter.next();
					if(filt.getFilterName().compareTo("Region") != 0 && 
						filt.getFilterName().compareTo("Start Date/Close Date") !=0	&& 
						filt.getFilterName().compareTo("Planned/Actual") != 0 )  
					{
						//logger.info("Insertd : " + filt.getFilterName());
						pageFilters.add(filt);
					}
				}
			}

			AmpPages ampPages = new AmpPages();
			ampPages.setFilters(pageFilters);
			ampPages.setPageName(ampReports.getName());
			//logger.info(" Page Name  : " + ampPages.getPageName());
				
			String pageCode = "" + ampReports.getName().trim().charAt(0);
			for(int j=0; j <ampReports.getName().length(); j++)
			{
				if(ampReports.getName().charAt(j) == ' ')
						pageCode = pageCode + ampReports.getName().charAt(j+1);
			}
			ampPages.setPageCode(pageCode);
			ampPages.setAmpTeamId(ampTeamId);
			session.save(ampPages);
			
			
			pageFilters = ampPages.getFilters();
			Iterator itr = pageFilters.iterator();
			while (itr.hasNext()) {
				AmpFilters filt = (AmpFilters) itr.next();
				AmpTeamPageFilters tpf = new AmpTeamPageFilters();
				tpf.setFilter(filt);
				tpf.setTeam(ampTeam);
				tpf.setPage(ampPages);
				session.save(tpf);
			
			}
			
			ampReports.setAmpPage(ampPages);
			
			queryString = "select t from " + AmpTeam.class.getName() + " t " +
					"where t.accessType = 'Management'";
			query = session.createQuery(queryString);
			itr = query.list().iterator();
			while (itr.hasNext()) {
				AmpTeam t = (AmpTeam) itr.next();
				pageFilters = ampPages.getFilters();
				Iterator itr1 = pageFilters.iterator();
				while (itr1.hasNext()) {
					AmpFilters filt = (AmpFilters) itr1.next();
					AmpTeamPageFilters tpf = new AmpTeamPageFilters();
					tpf.setFilter(filt);
					tpf.setTeam(t);
					tpf.setPage(ampPages);
					session.save(tpf);
			
				}
			}
			
			tx.commit(); 

		}
		catch (Exception ex) {
			logger.error("Exception from saveReport()  " + ex.getMessage()); 
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
					logger.debug("Transaction Rollbacked");
				}
				catch (HibernateException e) {
					logger.error("Rollback failed :" + e);
				}
			}			
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception e) {
					logger.error("Release session faliled :" + e);
				}
			}
		}
	}

    
    
	public static Collection getColumnList()
	{
		Session session = null;
		String sqlQuery = "";
		boolean flag =false;
		Iterator iter = null;
		Collection coll = new ArrayList();
		Query query = null;
		AmpColumns ampColumns = new AmpColumns();
		try
		{
			session = PersistenceManager.getSession();
			sqlQuery = "select c from "+ AmpColumns.class.getName() + " c order by columnName asc";
			query = session.createQuery(sqlQuery);
			if (query != null) 
			{
				iter = query.list().iterator();
				while (iter.hasNext()) 
				{
					ampColumns = (AmpColumns) iter.next();
					coll.add(ampColumns);
				}
				flag = true;
			}
			return coll;
		}
		catch(Exception e)
		{
			logger.error(e);
			//System.out.println(" Error in getColumnList()  :  " + e);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (HibernateException e) {
				logger.error(e);
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return coll;
			
	}

	public static Collection getMeasureList()
	{
		Session session = null;
		String sqlQuery = "";
		boolean flag =false;
		Iterator iter = null;
		Collection coll = new ArrayList();
		Query query = null;
		AmpMeasures ampMeasures = new AmpMeasures();
		try
		{
			session = PersistenceManager.getSession();
			sqlQuery = "select c from "+ AmpMeasures.class.getName() + " c";
			query = session.createQuery(sqlQuery);
			if (query != null) 
			{
				iter = query.list().iterator();
				while (iter.hasNext()) 
				{
					ampMeasures = (AmpMeasures) iter.next();
					coll.add(ampMeasures);
				}
				flag = true;
			}
			return coll;
		}
		catch(Exception e)
		{
			logger.error(e);
			//System.out.println(" Error in getMeasureList()  :  " + e);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (HibernateException e) {
				logger.error(e);
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return coll;
	}

	@Deprecated
	public static AmpTeamMember checkDuplicateReportName(String reportTitle){
		AmpTeamMember teamMember=null;
		Session session = null;
		Query query = null;
		Iterator iter=null;
		String queryString;
		try {
			session = PersistenceManager.getSession();
			queryString = "select report from " + AmpReports.class.getName() + " report ";
			//logger.info( " Query :" + queryString);
			query = session.createQuery(queryString);
	//		iter = query.list().iterator();
			
			if(query!=null)
			{
				iter = query.list().iterator();
				//logger.info("............Query is not null............");
				while(iter.hasNext())
				{
					AmpReports r = (AmpReports) iter.next();
					if( reportTitle.trim().equals(r.getName()) )
					{
						teamMember=r.getOwnerId();
						break;
					}
						
				}
			}

		} catch (Exception ex) {
			logger.error("Unable to get checkDupilcateReportName()", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return teamMember;
	}
        
        public static boolean checkDuplicateReportName(String reportTitle, Long ownerId, Long dbReportId) throws Exception{
		boolean exist=false;
		Session session = null;
		Query query = null;
		Iterator iter=null;
		String queryString;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "select report.ownerId from " + AmpReports.class.getName() 
                                + " report where report.name=:name and report.ownerId=:ownerId ";
                        if(dbReportId!=null){
                            queryString+=" and report.ampReportId!=:dbReportId";
                        }
			query = session.createQuery(queryString);
                        query.setLong("ownerId", ownerId);
                        query.setString("name", reportTitle.trim());
                         if(dbReportId!=null){
                            query.setLong("dbReportId", dbReportId);
                        }
                       if(query.list()!=null&&query.list().size()>0){
                           exist=true;
                       }			

		} catch (Exception ex) {
			logger.error("Unable to get checkDupilcateReportName()", ex);
                        throw ex;
		} 
		
		return exist;
	}
        
	/**
	 * compares AmpReports with primary key
	 * @author dare
	 *
	 */
	public static class AmpReportIdComparator implements Comparator<AmpReports>{

		public int compare(AmpReports r1, AmpReports r2) {			
			return r1.getAmpReportId().compareTo(r2.getAmpReportId());
		}
		
	}

	/*
	 * this is to delete a report completely by a team lead
	 */
	public static boolean deleteReportsCompletely(Long qid) {
	    Session session = null;
	    Transaction tx = null;
	    String queryString = null;
	    Query qry = null;
	    try {
	        session = PersistenceManager.getRequestDBSession();
	        tx = session.beginTransaction();
	        AmpReports ampReports = null;
	        // loading the 3 tables from where the deletion has to be done
	        try {
	            logger.info(" this is the utils's qid " + qid);
	            ampReports = (AmpReports) session.load(AmpReports.class, qid);
	            AmpTeamReports ampTeamReports = null;
	            queryString = "select tr from " + AmpTeamReports.class.getName() + " tr " +
	                "where tr.report=:qid ";
	            qry = session.createQuery(queryString);
	            qry.setParameter("qid", qid, Hibernate.LONG);
	            Iterator itr = qry.list().iterator();
	            Collection col = new ArrayList();
	            while (itr.hasNext()) {
	                ampTeamReports = (AmpTeamReports) itr.next();
	                session.delete(ampTeamReports);
	            }
	
	            if(ampReports.getAmpPage()!=null){
	            	AmpPages ampPage = ampReports.getAmpPage();
	            	session.delete(ampPage);
	            }
	            
	            
	            session.delete(ampReports);
	            tx.commit();
	            return true;
	        } catch (net.sf.hibernate.ObjectNotFoundException onfe) {
	            logger.error("Exception from deleteQuestion() :", onfe);
	            if (tx != null) {
	                try {
	                    tx.rollback();
	                } catch (Exception trbf) {
	                    logger.error("Transaction roll back failed ", trbf);
	                }
	            }
	        }
	    } catch (Exception e) {
	        logger.error("Exception from deleteQuestion() :", e);
	        if (tx != null) {
	            try {
	                tx.rollback();
	            } catch (Exception trbf) {
	                logger.error("Transaction roll back failed ", e);
	            }
	        }
	    }
	    return false;
	}



	public static boolean deleteReportsForOwner(Long qid) {
	    Session session = null;
	    Transaction tx = null;
	    String queryString = null;
	    Query qry = null;
	    Collection col = null;
	    try {
	        session = PersistenceManager.getRequestDBSession();
	        try {
	            queryString = "select rep from " + AmpReports.class.getName() + " rep " +
	                "where rep.ownerId=:oId ";
	            qry = session.createQuery(queryString);
	            qry.setParameter("oId", qid, Hibernate.LONG);
	            Iterator itr = qry.list().iterator();
	            col = new ArrayList();
	            while (itr.hasNext()) {
	                AmpReports rep = (AmpReports) itr.next();
	                session.delete(rep);
	            }
	            return true;
	        } catch (net.sf.hibernate.ObjectNotFoundException onfe) {
	            logger.error("Exception from deleteQuestion() :", onfe);
	        }
	    } catch (Exception e) {
	        logger.error("Exception from deleteQuestion() :", e);
	    }
	    return false;
	}
}
