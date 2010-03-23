package org.digijava.module.fundingpledges.dbentity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.fundingpledges.form.PledgeForm;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class PledgesEntityHelper {
	private static Logger logger = Logger.getLogger(PledgesEntityHelper.class);
	public static ArrayList<FundingPledges> getPledges(){
		 Session session = null;
	        Query q = null;
	        FundingPledges pledge = new FundingPledges();
	        ArrayList<FundingPledges> AllPledges = new ArrayList<FundingPledges>();
	        List list = null;
	        try {
	            session = PersistenceManager.getRequestDBSession();
	            String queryString = new String();
	            queryString = " select a from " + FundingPledges.class.getName() + " a ";
	            q = session.createQuery(queryString);
	            Iterator iter = q.list().iterator();
	            while (iter.hasNext()) {
	            	pledge = (FundingPledges) iter.next();
	            	AllPledges.add(pledge);
	            }

	        } catch (Exception ex) {
	        	logger.debug("Projects : Unable to get Pledges names from database" + ex.getMessage());
	        }
	        return AllPledges;
	}
	
	public static FundingPledges getPledgesById(Long id){
		Session session = null;
		Query qry = null;
		FundingPledges pledge = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select p from " + FundingPledges.class.getName()
					+ " p where (p.id=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				pledge = (FundingPledges) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge");
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
		return pledge;
	}
	
	public static ArrayList<FundingPledgesDetails> getPledgesDetails(Long pledgeid){
		Session session = null;
		Query qry = null;
		ArrayList<FundingPledgesDetails> fundingpledgesdetails =  new ArrayList<FundingPledgesDetails>();
		FundingPledgesDetails fd = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select d from " + FundingPledgesDetails.class.getName()
					+ " d where (d.pledgeid=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", pledgeid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				fd = (FundingPledgesDetails) itr.next();
				fundingpledgesdetails.add(fd);
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge details");
			logger.debug("Exception " + e);
		}
		return fundingpledgesdetails;
	}
	
	public static ArrayList<FundingPledgesLocation> getPledgesLocations(Long pledgeid){
		Session session = null;
		Query qry = null;
		ArrayList<FundingPledgesLocation> fundingpledgeloc = new ArrayList<FundingPledgesLocation>();
		FundingPledgesLocation pl = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select l from " + FundingPledgesLocation.class.getName()
					+ " l where (l.pledgeid=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", pledgeid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				pl = (FundingPledgesLocation) itr.next();
				fundingpledgeloc.add(pl);
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge locations");
			logger.debug("Exception " + e);
		} 
		return fundingpledgeloc;
	}
	public static ArrayList<FundingPledgesSector> getPledgesSectors(Long pledgeid){
		Session session = null;
		Query qry = null;
		ArrayList<FundingPledgesSector> fundingPledgesSector = new ArrayList<FundingPledgesSector>();
		FundingPledgesSector fs = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select s from " + FundingPledgesSector.class.getName()
					+ " s where (s.pledgeid=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", pledgeid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				fs = (FundingPledgesSector) itr.next();
				fundingPledgesSector.add(fs);
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge sectors");
			logger.debug("Exception " + e);
		} 
		return fundingPledgesSector;
	}
	public static void savePledge(FundingPledges pledge, Set<FundingPledgesSector> sectors,PledgeForm plf) throws DgException {
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.save(pledge);
			
			for (Iterator iterator = sectors.iterator(); iterator.hasNext();) {
				FundingPledgesSector fundingPledgesSector = (FundingPledgesSector) iterator
						.next();
				fundingPledgesSector.setPledgeid(pledge);
				session.save(fundingPledgesSector);
			}
			if(plf.getFundingPledgesDetails()!=null && plf.getFundingPledgesDetails().size()>0){
				for (Iterator iterator = plf.getFundingPledgesDetails().iterator(); iterator.hasNext();) {
					FundingPledgesDetails FundingPledgesDetails = (FundingPledgesDetails) iterator.next();
					FundingPledgesDetails.setPledgeid(pledge);
					session.save(FundingPledgesDetails);
				}
			}
			
			if(plf.getSelectedLocs()!=null && plf.getSelectedLocs().size()>0){
				for (Iterator iterator = plf.getSelectedLocs().iterator(); iterator.hasNext();) {
					FundingPledgesLocation FundingPledgesloc = (FundingPledgesLocation) iterator.next();
					FundingPledgesloc.setPledgeid(pledge);
					session.save(FundingPledgesloc);
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error saving pledge",e);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception ex) {
					throw new DgException("Cannot rallback save pledge action",ex);
				}
				throw new DgException("Cannot save Pledge!",e);
			}
		}
	}
	
	public static void updatePledge(FundingPledges pledge, Set<FundingPledgesSector> sectors,PledgeForm plf) throws DgException {
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.update(pledge);
			Collection<FundingPledgesSector> fpsl = PledgesEntityHelper.getPledgesSectors(pledge.getId());
			Collection<FundingPledgesLocation> fpll = PledgesEntityHelper.getPledgesLocations(pledge.getId());
			Collection<FundingPledgesDetails> fpdl = PledgesEntityHelper.getPledgesDetails(pledge.getId());
			
			if(sectors!=null && sectors.size()>0){
				for (Iterator iterator = sectors.iterator(); iterator.hasNext();) {
					FundingPledgesSector fundingPledgesSector = (FundingPledgesSector) iterator.next();
					if (fpsl.contains(fundingPledgesSector)) {
						fundingPledgesSector.setPledgeid(pledge);
						session.update(fundingPledgesSector);
					} else {
						fundingPledgesSector.setPledgeid(pledge);
						session.save(fundingPledgesSector);
					}
				}
			} else {
				for (Iterator iterator = fpsl.iterator(); iterator.hasNext();) {
					FundingPledgesSector fundingPledgesSector = (FundingPledgesSector) iterator.next();
					session.delete(fundingPledgesSector);
				}
			}
			fpsl = PledgesEntityHelper.getPledgesSectors(pledge.getId());
			if(fpsl!=null && fpsl.size()>0){
				for (Iterator iterator = fpsl.iterator(); iterator.hasNext();) {
					FundingPledgesSector fundingPledgesSector = (FundingPledgesSector) iterator.next();
					if (!sectors.contains(fundingPledgesSector)) {
						session.delete(fundingPledgesSector);
					}
				}
			}
			
			if(plf.getFundingPledgesDetails()!=null && plf.getFundingPledgesDetails().size()>0){
				for (Iterator iterator = plf.getFundingPledgesDetails().iterator(); iterator.hasNext();) {
					FundingPledgesDetails fundingPledgesDetails = (FundingPledgesDetails) iterator.next();
					if (fpdl.contains(fundingPledgesDetails)) {
						fundingPledgesDetails.setPledgeid(pledge);
						session.update(fundingPledgesDetails);
					} else {
						fundingPledgesDetails.setPledgeid(pledge);
						session.save(fundingPledgesDetails);
					}
				}
			} else {
				for (Iterator iterator = fpdl.iterator(); iterator.hasNext();) {
					FundingPledgesDetails fundingPledgesDetails = (FundingPledgesDetails) iterator.next();
					session.delete(fundingPledgesDetails);
				}
			}
			fpdl = PledgesEntityHelper.getPledgesDetails(pledge.getId());
			if(fpdl!=null && fpdl.size()>0){
				for (Iterator iterator = fpdl.iterator(); iterator.hasNext();) {
					FundingPledgesDetails fundingPledgesDetails = (FundingPledgesDetails) iterator.next();
					if (!plf.getFundingPledgesDetails().contains(fundingPledgesDetails)) {
						session.delete(fundingPledgesDetails);
					}
				}
			}
			if(plf.getSelectedLocs()!=null && plf.getSelectedLocs().size()>0){
				for (Iterator iterator = plf.getSelectedLocs().iterator(); iterator.hasNext();) {
					FundingPledgesLocation fundingPledgesloc = (FundingPledgesLocation) iterator.next();
					if (fpll.contains(fundingPledgesloc)) {
						fundingPledgesloc.setPledgeid(pledge);
						session.update(fundingPledgesloc);
					} else {
						fundingPledgesloc.setPledgeid(pledge);
						session.save(fundingPledgesloc);
					}
					
				}
			} else {
				for (Iterator iterator = fpll.iterator(); iterator.hasNext();) {
					FundingPledgesLocation fundingPledgesloc = (FundingPledgesLocation) iterator.next();
					session.delete(fundingPledgesloc);
				}
			}
			fpll = PledgesEntityHelper.getPledgesLocations(pledge.getId());
			if(fpll!=null && fpll.size()>0){
				for (Iterator iterator = fpll.iterator(); iterator.hasNext();) {
					FundingPledgesLocation fundingPledgesloc = (FundingPledgesLocation) iterator.next();
					if (!plf.getSelectedLocs().contains(fundingPledgesloc)) {
						session.delete(fundingPledgesloc);
					}
				}
			}
			
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error saving pledge",e);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception ex) {
					throw new DgException("Cannot rallback save pledge action",ex);
				}
				throw new DgException("Cannot save Pledge!",e);
			}
		}
	}
	
	public static AmpOrganisation getOrganizationById(Long id) {
		Session session = null;
		AmpOrganisation ampOrg = null;
		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getRequestDBSession();
			ampOrg = (AmpOrganisation) session.load(AmpOrganisation.class, id);
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					;//PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return ampOrg;
	}
}
