package org.digijava.module.contentrepository.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.contentrepository.dbentity.CrDocumentsToOrganisations;
import org.digijava.module.contentrepository.dbentity.filter.DocumentFilter;
import org.hibernate.Query;
import org.hibernate.Session;

public class DocumentFilterDAO {
	private static Logger logger	= Logger.getLogger(DocumentFilterDAO.class);
	public void saveObject(DocumentFilter obj) {
		Session hbSession;
		try{
			hbSession	= PersistenceManager.getRequestDBSession();
			hbSession.saveOrUpdate(obj);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public List<DocumentFilter> getAll() {
		Session hbSession;
		try{
			hbSession	= PersistenceManager.getRequestDBSession();
			String queryString 	= "select df from "	+ DocumentFilter.class.getName() + " df ";
			Query query			= hbSession.createQuery(queryString);
			List<DocumentFilter> resultList	=  query.list();
			return resultList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public DocumentFilter getDocumentFilter(Long id) {
		Session hbSession;
		try{
			hbSession			= PersistenceManager.getRequestDBSession();
			DocumentFilter	df 	= (DocumentFilter)hbSession.load(DocumentFilter.class, id);
			
			return df;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
