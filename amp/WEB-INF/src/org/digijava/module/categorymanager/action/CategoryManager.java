/**
 * 
 */
package org.digijava.module.categorymanager.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.dbentity.AmpLinkedCategoriesState;
import org.digijava.module.categorymanager.form.CategoryManagerForm;
import org.digijava.module.categorymanager.util.CategoryLabelsUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.PossibleValue;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author Alex Gartner
 *
 */
public class CategoryManager extends Action {
	
	private static Logger logger	= Logger.getLogger(CategoryManager.class);
	ActionMessages	errors;

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		errors						= new ActionMessages();
		CategoryManagerForm myForm	= (CategoryManagerForm) form;
		HttpSession session 		= request.getSession();
		
		/**
		 * Test if user is administrator
		 */
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
					return mapping.findForward("index");
				}
			}
		/**
		 * If the user wants to create a new category
		 */
		if (request.getParameter("new") != null) {
			myForm.setNumOfPossibleValues(new Integer(0));
			myForm.setCategoryName(null);
			myForm.setDescription(null);
			myForm.setKeyName(null);
			myForm.setIsMultiselect(false);
			myForm.setIsOrdered(false);
			List<PossibleValue> possibleVals = new ArrayList();
			for (int i = 0; i < 3; i++) {
				possibleVals.add(new PossibleValue());
			}
			myForm.setEditedCategoryId(null);
			myForm.setPossibleVals(possibleVals);
			myForm.setAdvancedMode(true);
			return mapping.findForward("createOrEditCategory");
		}
		/**
		 * If the user wants add a new value to existing category we need to show the appropriate text boxes 
		 * and prepare the ActionForm
		 */
		if (request.getParameter("addValue") != null) {
			int position 	= myForm.getPossibleVals().size();
			if ( request.getParameter("position") != null )
				position	= Integer.parseInt(request.getParameter("position"))-1;
			if(myForm.getPossibleVals()==null){
				myForm.setPossibleVals(new ArrayList<PossibleValue>());
			}
			for (int i=0; i<myForm.getNumOfAdditionalFields(); i++) {
				PossibleValue value=new PossibleValue();
				value.setValue("");
				value.setId(null);
				value.setDisable(false);
				CategoryLabelsUtil.addUsedCategoriesToPossibleValue(myForm, value);
				myForm.getPossibleVals().add(position, value);
			}
			return mapping.findForward("createOrEditCategory");
		}
		/**
		 * If the user wants to edit an existing category
		 */
		if (request.getParameter("edit") != null) {
			Collection categoryCollection		= this.loadCategories(new Long(request.getParameter("edit")) );
			AmpCategoryClass ampCategoryClass	= (AmpCategoryClass)(categoryCollection.toArray())[0];
			boolean advancedMode				= false;
			if ( request.getParameter("advancedMode") != null)
				advancedMode					= true;
			this.populateForm(ampCategoryClass, myForm, advancedMode, request);
			return mapping.findForward("createOrEditCategory");
		}
		if (request.getParameter("delete") != null ) {
			this.deleteCategory(new Long( request.getParameter("delete") ));
		}
                else{
		/**
		 * Adding a new category to the database
		 */
		if (myForm.getSubmitPressed() != null && myForm.getSubmitPressed().booleanValue()) {
			myForm.setSubmitPressed(false);
			boolean saved	= this.saveCategoryToDatabase(myForm, errors);
			if (!saved) {
				/*try{
					AmpCategoryClass ampCategoryClass	= CategoryManagerUtil.loadAmpCategoryClass( myForm.getEditedCategoryId() );
					this.populateForm(ampCategoryClass, myForm);
				}
				catch (Exception e) {
					// TODO: handle exception
					logger.info(e.getMessage());
					e.printStackTrace();
				}*/
				this.saveErrors(request, errors);
				return mapping.findForward("createOrEditCategory");
			}
		}
                }
		/**
		 * loading existing categories
		 */
		myForm.setCategories( this.loadCategories(null) );
		/* Ordering the values alphabetically if necessary */
		if (myForm.getCategories() != null) {
			myForm.setAllCategoryValues( new HashSet<AmpCategoryValue>() );
			Iterator iterator	= myForm.getCategories().iterator();
			while(iterator.hasNext()) {
				AmpCategoryClass ampCategoryClass	= (AmpCategoryClass) iterator.next();
				myForm.getAllCategoryValues().addAll( ampCategoryClass.getPossibleValues() );
				myForm.getAllCategoryValues().remove(null);
				
				if ( ampCategoryClass.getIsOrdered() && ampCategoryClass.getPossibleValues() != null ) {
					TreeSet treeSet	= new TreeSet( new CategoryManagerUtil.CategoryComparator(request) );
					treeSet.addAll( ampCategoryClass.getPossibleValues() );
					ampCategoryClass.setPossibleValues( new ArrayList(treeSet) );
				}
			}
		}
                
		/* END- Ordering the values alphabetically if necessary */
		String errorString		= CategoryManagerUtil.checkImplementationLocationCategory();
		if ( errorString != null ) {
			ActionMessage error	= (ActionMessage) new ActionMessage("error.aim.categoryManager.implLocProblem", errorString);
			errors.add("title", error);
		}
		this.saveErrors(request, errors);
		return mapping.findForward("forward");
	}
	/**
	 * 
	 * @param ampCategoryClass
	 * @param myForm
	 */
	private void populateForm(AmpCategoryClass ampCategoryClass, CategoryManagerForm myForm, boolean advancedMode, HttpServletRequest request) {
		if (ampCategoryClass != null) {
			myForm.setCategoryName( ampCategoryClass.getName() );
			myForm.setDescription( ampCategoryClass.getDescription() );
			myForm.setNumOfPossibleValues( new Integer(ampCategoryClass.getPossibleValues().size()) );
			myForm.setEditedCategoryId( ampCategoryClass.getId() );	
			myForm.setIsMultiselect( ampCategoryClass.isMultiselect() );
			myForm.setIsOrdered( ampCategoryClass.isOrdered() );
			myForm.setKeyName( ampCategoryClass.getKeyName() );
			
			myForm.setAdvancedMode(advancedMode);
			
			
			ArrayList<AmpCategoryClass> availableCategories	= new ArrayList<AmpCategoryClass>( myForm.getCategories() );
			availableCategories.remove( ampCategoryClass );
			myForm.setAvailableCategories( createKVList(availableCategories, request) );
			myForm.setUsedCategories(null);
			
			
			Iterator iterator					= ampCategoryClass.getPossibleValues().iterator();
			//String[] possibleValues				= new String [ampCategoryClass.getPossibleValues().size()];
			List<PossibleValue> possibleVals	= new ArrayList<PossibleValue>();
			
			while (iterator.hasNext()) {
				AmpCategoryValue ampCategoryValue	= (AmpCategoryValue) iterator.next();
				if (ampCategoryValue!=null){
					//possibleValues[k++]					= ampCategoryValue.getValue();
					PossibleValue value					= new PossibleValue();
				
					value.setValue(ampCategoryValue.getValue());
					value.setId(ampCategoryValue.getId());
					value.setDisable(false);
					possibleVals.add(value);
				}
			}
			
			//myForm.setPossibleValues( possibleValues );
			myForm.setPossibleVals(possibleVals);
			
			CategoryLabelsUtil.populateFormWithLabels(ampCategoryClass, myForm);
		}
		else{
			if ( myForm.getPossibleValues() != null && myForm.getPossibleValues().length > 0 ) {
					int count	= 0;
					for (int i=0; i< myForm.getPossibleValues().length; i++) {
						if ( myForm.getPossibleValues()[i].length() > 0  )
									count++;
					}
					myForm.setNumOfPossibleValues( new Integer(count) );
			}
			else
				myForm.setNumOfPossibleValues( new Integer(0) );
		}
		
	}
	
	
	/**
	 * @param categoryId set to null if you want to load all categories
	 * @return all the categories from the database or just one if categoryId is not null
	 */
	private Collection loadCategories(Long categoryId) {
		logger.info("Getting category with id " + categoryId);
		
		Session dbSession			= null;
		Collection returnCollection	= null;
		try {
			dbSession			= PersistenceManager.getSession();
			String queryString;
			Query qry;
			if ( categoryId != null ){
				queryString = "select c from "
					+ AmpCategoryClass.class.getName()
					+ " c where c.id=:id";
				qry			= dbSession.createQuery(queryString);
				qry.setParameter("id", categoryId, Hibernate.LONG);
			}
			else {
				queryString = "select c from "
					+ AmpCategoryClass.class.getName()
					+ " c ";
				qry 		= dbSession.createQuery(queryString);
			}
			
			returnCollection	= qry.list();
			
			Iterator<AmpCategoryClass> iter	= returnCollection.iterator();
			while ( iter.hasNext() ) {
				iter.next().getUsedCategories().size();
			}
			
		} catch (Exception ex) {
			logger.error("Unable to get Categories: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return returnCollection;
	}
	private void deleteCategory (Long categoryId) {
		Session dbSession			= null;
		Transaction transaction		= null;
		try{
			dbSession		= PersistenceManager.getSession();
//beginTransaction();
			
			dbSession.createQuery("delete from " + AmpLinkedCategoriesState.class.getName() +" s where s.mainCategory.id=:categoryId")
			.setLong("categoryId", categoryId).executeUpdate();
			//transaction.commit();
			
			String queryString 	= "select c from "	+ AmpCategoryClass.class.getName()+ " c where c.id=:id";
			Query qry			= dbSession.createQuery(queryString);
			qry.setParameter("id", categoryId, Hibernate.LONG);
			dbSession.delete( qry.uniqueResult() );			
			
		} 
		catch (Exception ex) {
			ActionMessage error	= (ActionMessage) new ActionMessage("error.aim.categoryManager.cannotDeleteCategory");
			errors.add("title", error);
			logger.error("Unable to delete Category: " + ex.getMessage());
			
			try {
				transaction.rollback();
			} catch (HibernateException e) {
				logger.error("Failed to rollback transaction when deleting category with id " + categoryId + ":"+ ex.getMessage());
				e.printStackTrace();
			}
		} 
		finally {
			try {
				PersistenceManager.releaseSession(dbSession);
				
			} 
			catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
	}
	/**
	 * 
	 * @param myForm
	 * @param errors
	 * @throws Exception
	 */


		
	
	private boolean saveCategoryToDatabase(CategoryManagerForm myForm, ActionMessages errors) throws Exception {
		
		Session dbSession					= null;	
		Transaction tx						= null;
		String undeletableCategoryValues	= null; 
		
		boolean retValue					= true;
		
		
//beginTransaction();
		
		
		try {
			/* Testing if entered category key is not already used */
			if ( myForm.getEditedCategoryId() == null && 
					CategoryManagerUtil.isCategoryKeyInUse( myForm.getKeyName() )  ) 
			{
				ActionMessage duplicateKeyError	= new ActionMessage("error.aim.categoryManager.duplicateKey");
				errors.add("title",duplicateKeyError);
				return false;
			}
			
			dbSession						= PersistenceManager.getSession();
//beginTransaction();
			AmpCategoryClass dbCategory		= new AmpCategoryClass();
			dbCategory.setPossibleValues( new Vector() );
			if (myForm.getEditedCategoryId() != null) {
				String queryString	= "select c from " + AmpCategoryClass.class.getName() + " c where c.id=:id";
				Query query			= dbSession.createQuery(queryString);
				query.setParameter("id", myForm.getEditedCategoryId(), Hibernate.LONG);
				Collection col		= query.list();
				if (col !=null && col.size() > 0)
					dbCategory			= (AmpCategoryClass)col.toArray()[0];
			}

			dbCategory.setName( myForm.getCategoryName() );
			dbCategory.setDescription( myForm.getDescription() );
			dbCategory.setIsMultiselect( myForm.getIsMultiselect() );
			dbCategory.setIsOrdered( myForm.getIsOrdered() );
			dbCategory.setKeyName( myForm.getKeyName() );
			
			//dbCategory.getPossibleValues().clear();
			
			//String[] possibleValues		= myForm.getPossibleValues();
			List <PossibleValue> possibleVals=myForm.getPossibleVals();
			
			/**
			 * Eliminate empty values from the new values
			 */
			Iterator<PossibleValue> iter			= possibleVals.iterator();
			while ( iter.hasNext() ) {
				if ( iter.next().getValue().equals("") )
					iter.remove();
			}
			/**
			 * Add new values
			 */
			for ( int i=0; i<possibleVals.size(); i++ ) {
				PossibleValue pVal		= possibleVals.get(i);
				if ( !pVal.isDisable() && 
						(pVal.getId() == null || pVal.getId() == 0L) ) {
					AmpCategoryValue newVal			= new AmpCategoryValue();
					newVal.setValue( pVal.getValue() );
					newVal.setAmpCategoryClass( dbCategory );
					dbCategory.getPossibleValues().add(i, newVal);
				}
			}
			/**
			 * Save modifications to existing values only if we are in advanced mode
			 */
			if ( myForm.isAdvancedMode() )
				for ( int i=0; i<possibleVals.size(); i++ ) {
					dbCategory.getPossibleValues().get(i).setValue( possibleVals.get(i).getValue() );
				}
			/**
			 * Remove deleted values from database
			 */
			iter						= possibleVals.iterator();
			while ( iter.hasNext() ) {
				PossibleValue pVal		= iter.next();
				if ( pVal.isDisable() ) {
					if ( pVal.getId() == null ) {
						throw new Exception ("Received id paramter is null");
					}
					Iterator<AmpCategoryValue> iterCV	= dbCategory.getPossibleValues().iterator();
					while ( iterCV.hasNext() ) {
						AmpCategoryValue ampCategoryValue	= iterCV.next();
						if ( pVal.getId().equals(ampCategoryValue.getId()) ) {
							iterCV.remove();
							try{
//session.flush();
								if ( CategoryManagerUtil.verifyDeletionProtectionForCategoryValue( dbCategory.getKeyName(), 
																		ampCategoryValue.getValue()) )
										throw new Exception("This value is in CategoryConstants.java and used by the system");
							}
							catch (Exception e) {
								if (undeletableCategoryValues ==  null) 
									undeletableCategoryValues = new String() + ampCategoryValue.getValue();
								else
									undeletableCategoryValues += ", " + ampCategoryValue.getValue(); 
							}
						}
					}
				}
			}
			
			this.reindexAmpCategoryValueList( dbCategory.getPossibleValues() );
			
			CategoryLabelsUtil.populateCategoryWithLabels(myForm, dbCategory);
			
			String dupValue		= CategoryManager.checkDuplicateValues(dbCategory.getPossibleValues()); 
			
			if ( dupValue != null ) {
				if (tx != null)
					tx.rollback();
				ActionMessage error	= new ActionMessage("error.aim.categoryManager.duplicateValue", dupValue);
				errors.add("title",error);
				retValue			= false;
			}
			else {
			
				if (undeletableCategoryValues != null) {
					if (tx != null)
						tx.rollback();
					ActionMessage error2	= new ActionMessage("error.aim.categoryManager.cannotDeleteValues", undeletableCategoryValues);
					errors.add("title",error2);
					retValue			= false;
				}
				
				else {
					if (myForm.getEditedCategoryId() == null) {
						dbSession.saveOrUpdate( dbCategory );
					}
					else{
//session.flush();
						//label category states updates
						String queryString	= "delete from " + AmpLinkedCategoriesState.class.getName() + " s where s.mainCategory=:dbCategory";
						dbSession.createQuery(queryString).setEntity("dbCategory", dbCategory).executeUpdate();
						List<AmpCategoryClass> usedCategories = dbCategory.getUsedCategories();
						for (AmpCategoryClass ampCategoryClass : usedCategories) {
							AmpLinkedCategoriesState newState = new AmpLinkedCategoriesState ();
							newState.setMainCategory(dbCategory);
							newState.setLinkedCategory(ampCategoryClass);
							newState.setSingleChoice(ampCategoryClass.getUsedByCategorySingleSelect());
							dbSession.saveOrUpdate( newState );
						}						

					}
					
					//tx.commit();
				}
			}
		} catch (Exception ex) {
			logger.error("Unable to save or update the AmpCategoryClass: " + ex);
			ActionMessage error1	= new ActionMessage("error.aim.categoryManager.cannotSaveOrUpdate");
			errors.add("title",error1);
			if (tx != null)
					tx.rollback();
			retValue			= false;
			
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return retValue;
	}
	
	private void reindexAmpCategoryValueList( List<AmpCategoryValue> values ) {
		for (int i=0; i<values.size(); i++) 
			values.get(i).setIndex(i);
	}
	
	public static String checkDuplicateValues( List<AmpCategoryValue>  values) {
		HashSet<String> set					= new HashSet<String>( values.size() );
		Iterator<AmpCategoryValue> iter		= values.iterator();
		
		while ( iter.hasNext() ) {
			String value					= iter.next().getValue(); 
			if ( !set.add( value ) )
				return value;
		}
		return null;
	}
	
	public static Set<KeyValue> createKVList(Collection<AmpCategoryClass> categories, HttpServletRequest request) {
		TreeSet<KeyValue> kvCategories	= new TreeSet<KeyValue>( KeyValue.valueComparator );
		Iterator<AmpCategoryClass> iter		= categories.iterator();
		while (iter.hasNext()) {
			AmpCategoryClass cat 	= (AmpCategoryClass) iter.next();
			String translatedName	= CategoryManagerUtil.translate( 
							CategoryManagerUtil.getTranslationKeyForCategoryName(cat.getKeyName()), request, cat.getName()   );
			
			KeyValue kv				= new KeyValue(cat.getId().toString(), translatedName);
			kvCategories.add(kv);
		}
		return kvCategories;
	}
	
}
