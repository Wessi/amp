package org.digijava.module.categorymanager.util;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.exception.NoCategoryClassException;
import org.digijava.module.calendar.entity.AmpEventType;
import org.digijava.module.categorymanager.action.CategoryManager;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

public class CategoryManagerUtil {
	private static Logger logger = Logger.getLogger(CategoryManagerUtil.class);

	/**
	 * Looks up the AmpCategoryValue with id = categoryValueId. If not null it adds it to the Set someSet.
	 * @param categoryValueId
	 * @param someSet
	 */
	public static void addCategoryToSet (Long categoryValueId, Set someSet) {
		if(categoryValueId != null) {
			if( someSet == null )
				logger.error("The set received as argument 2 in null. Cannot insert AmpCategoryValue ");
			AmpCategoryValue ampCategoryValue	= CategoryManagerUtil.getAmpCategoryValueFromDb( categoryValueId );
			if (ampCategoryValue != null)
					someSet.add(ampCategoryValue);
		}
	}

	/**
	 * Used for translating the drop-downs with category values. The actual translation should be done in the CategoryManager page
	 * in "Translator View". This function just extracts the translation from the database
	 * @param ampCategoryValue
	 * @param request
	 * @return The translated category value or ,if any error appears, the empty string
	 */
	public static String translateAmpCategoryValue(AmpCategoryValue ampCategoryValue, HttpServletRequest request) {
		//translation key is generated from the text hashcode
		String key=TranslatorWorker.generateTrnKey(ampCategoryValue.getValue());
		return translate(key, request, ampCategoryValue.getValue() );
		//return translate(CategoryManagerUtil.getTranslationKeyForCategoryValue(ampCategoryValue), request, ampCategoryValue.getValue() );
	}
	public static String translate(String key, HttpServletRequest request, String defaultValue) {
		Session session	= null;
		String ret		= "";
		String	lang	= RequestUtils.getNavigationLanguage(request).getCode();
		Long	siteId	= RequestUtils.getSite(request).getId();
		try{
			session			= PersistenceManager.getSession();
			String qryStr	= "select m from "
						+ Message.class.getName() + " m "
						+ "where (m.locale=:langIso and m.key=:translationKey and m.siteId=:thisSiteId)";

			Query qry		= session.createQuery(qryStr);
			qry.setParameter("langIso", lang, Hibernate.STRING);
			qry.setParameter("translationKey", key.toLowerCase() ,Hibernate.STRING);
			qry.setParameter("thisSiteId", siteId+"", Hibernate.STRING);

			Message m		= (Message)qry.uniqueResult();
			if ( m == null ) {
				logger.debug("No translation found for key '"+ key +"' for lang " + lang + ".");
				return defaultValue;
			}
			ret				= m.getMessage();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		if ( ret == null || ret.equals("") )
				ret	= defaultValue;
		return ret;
	}
	
	public static AmpCategoryValue getAmpCategoryValueFromCollectionById( Long ampCategoryValueId, Collection<AmpCategoryValue> col ) {
		if ( ampCategoryValueId == null || col == null) {
			logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
			return null;
		}
		Iterator<AmpCategoryValue> iterator	= col.iterator();
		
		while ( iterator.hasNext() ) {
			AmpCategoryValue val		= iterator.next();
			if ( ampCategoryValueId.equals(val.getId()) )
				return val;
		}
		return null;
	}
	
	/**
	 *
	 * @param categoryId
	 * @param values - a set of AmpCategoryValuews
	 * @return The AmpCategoryValue object witch belongs to the AmpCategoryClass with id=categoryId
	 * or null if the object is not in the set.
	 */
	public static AmpCategoryValue getAmpCategoryValueFromList(Long categoryId, Collection values) {
		if ( categoryId == null || values == null) {
			logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
			return null;
		}
		Iterator iterator	= values.iterator();
		while( iterator.hasNext() ) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
			if ( ampCategoryValue.getAmpCategoryClass().getId().longValue() == categoryId.longValue() ) {
				return ampCategoryValue;
			}
		}
		return null;
	}
	/**
	 *
	 * @param categoryName
	 * @param values
	 * @return The AmpCategoryValue object witch belongs to the AmpCategoryClass with name=categoryName
	 * or null if the object is not in the set.
	 */
	public static AmpCategoryValue getAmpCategoryValueFromList(String categoryName, Set values) {
		if ( categoryName == null || values == null) {
			logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
			return null;
		}
		Iterator iterator	= values.iterator();
		while( iterator.hasNext() ) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
			if ( ampCategoryValue.getAmpCategoryClass().getName().equals(categoryName) ) {
				return ampCategoryValue;
			}
		}
		return null;
	}
	
	
	public static List<AmpEventType>  getAmpEventColors() throws NoCategoryClassException{
List<AmpEventType> eventTypeList = new ArrayList<AmpEventType>(); 
        
        AmpCategoryClass categoryClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.EVENT_TYPE_KEY);   
        Iterator<AmpCategoryValue> categoryClassIter = categoryClass.getPossibleValues().iterator();
         while(categoryClassIter.hasNext()){
        	AmpEventType eventType = new AmpEventType();
        	AmpCategoryValue item = (AmpCategoryValue) categoryClassIter.next();
        	 eventType.setName(item.getValue());
        	 eventType.setId(item.getId());
        	   Iterator<AmpCategoryValue> usedValues = item.getUsedValues().iterator();
        	    while (usedValues.hasNext()){
        		 AmpCategoryValue categoryValueItem = (AmpCategoryValue) usedValues.next();
        		 eventType.setColor(categoryValueItem.getValue());
        	 }
        	  eventTypeList.add(eventType);
        }
		return eventTypeList;
		
	}
	
	/**
	 *
	 * @param categoryKey
	 * @param values
	 * @return The AmpCategoryValue object witch belongs to the AmpCategoryClass with key=categoryKey
	 * or null if the object is not in the set.
	 */
	public static AmpCategoryValue getAmpCategoryValueFromListByKey(String categoryKey, Set values) {
		if ( categoryKey == null || values == null) {
			logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
			return null;
		}
		Iterator iterator	= values.iterator();
		while( iterator.hasNext() ) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
			if ( ampCategoryValue.getAmpCategoryClass().getKeyName().equals(categoryKey) ) {
				return ampCategoryValue;
			}
		}
		return null;
	}
	
	public static Collection getAmpCategoryValuesFromListByKey(String categoryKey, Set values) {
		Collection<AmpCategoryValue> ret=new ArrayList<AmpCategoryValue>();
		if ( categoryKey == null || values == null) {
			logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
			return null;
		}
		Iterator iterator	= values.iterator();
		while( iterator.hasNext() ) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
			if ( ampCategoryValue.getAmpCategoryClass().getKeyName().equals(categoryKey) ) {
				ret.add(ampCategoryValue);
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * @param categoryKey The key of the category
	 * @param categoryIndex The index of the value within the category
	 * @return
	 */
	public static AmpCategoryValue getAmpCategoryValueFromDb(String categoryKey, Long categoryIndex) {
		Session dbSession			= null;
		Collection returnCollection	= null;
		try {
		
			dbSession= PersistenceManager.getRequestDBSession();
			String queryString;
			Query qry;

			queryString = "select v from "
				+ AmpCategoryValue.class.getName()
				+ " v join v.ampCategoryClass as c where c.keyName=:key AND v.index=:index";
			qry			= dbSession.createQuery(queryString);
			qry.setParameter("key", categoryKey, Hibernate.STRING);
			qry.setParameter("index", categoryIndex, Hibernate.LONG);
			returnCollection	= qry.list();

		} catch (Exception ex) {
			logger.error("Unable to get AmpCategoryValue: " + ex.getMessage());
			ex.printStackTrace();
		} 
//		finally {
//			try {
//				PersistenceManager.releaseSession(dbSession);
//			} catch (Exception ex2) {
//				logger.error("releaseSession() failed :" + ex2);
//			}
	//}
	
		if (returnCollection != null) {
			Iterator it=returnCollection.iterator();
			if(it.hasNext())
			{
				AmpCategoryValue x=(AmpCategoryValue)it.next();
				return x;
			}
		}
		return null;
	}
	
	public static AmpCategoryValue getAmpCategoryValueFromDb(Long valueId) {
		return getAmpCategoryValueFromDb( valueId, false );
	}
	
	/**
	 *
	 * @param valueId
	 * @param initializeTaggedValues AmpCategoryValue.usedByValues property is lazyly initialized. Setting the initializeTaggedValues as true will initialize the 
	 * property before closing the hibernate session
	 * @return Extracts the AmpCategoryValue with id=valueId from the database. Return null if not found.
	 */
	public static AmpCategoryValue getAmpCategoryValueFromDb(Long valueId, boolean initializeTaggedValues) {
		AmpCategoryValue retVal			= null;
		if(valueId!=null)
		{
			Session dbSession			= null;
			try {
				dbSession				= PersistenceManager.getSession();
				retVal					= (AmpCategoryValue) dbSession.get(AmpCategoryValue.class, valueId);
				
				if ( retVal != null && initializeTaggedValues )
					retVal.getUsedByValues().size();

			} catch (Exception ex) {
				logger.error("Unable to get AmpCategoryValue: ", ex);
			} finally {
				try {
					PersistenceManager.releaseSession(dbSession);
				} catch (Exception ex2) {
					logger.error("releaseSession() failed :" + ex2);
				}
			}
			return retVal;
		}
		else
			logger.debug("[getAmpCategoryValueFromDb] valueId is null");
		return null;
	}
	
	public static AmpCategoryValue getAmpCategoryValueFromDB(HardCodedCategoryValue hcValue) throws Exception {
		Collection<AmpCategoryValue> c 	= CategoryManagerUtil.getAmpCategoryValueCollectionByKey(hcValue.getCategoryKey());
		if (c != null) {
			Iterator<AmpCategoryValue> tempItr = c.iterator();
			while (tempItr.hasNext()) {
				AmpCategoryValue categoryValue = (AmpCategoryValue) tempItr.next();
				if (categoryValue.getValue().equalsIgnoreCase( hcValue.getValueKey() )) {
					return categoryValue;
				}
			}
		}
		
		throw new Exception ("HardCodedCategoryValue not found in the database");
	}
	
	
	/**
	 * 
	 * @param tagId   the id of the tag (that is the id of the AmpCategoryValue object used as tag)
	 * @param categoryKey   if not null, only the AmpCategoryValue objects belonging to the category with key=categoryKey will be returned in the set
	 * @return set of AmpCategoryValue objects tagged with tag that has id=tagId
	 */
	public static Set<AmpCategoryValue> getTaggedCategoryValues(Long tagId, String categoryKey) {
		
		AmpCategoryValue tag	= getAmpCategoryValueFromDb(tagId, true);
		if ( tag != null && tag.getUsedByValues() != null ) {
			return orderCategoryValues(tag.getUsedByValues(), categoryKey);
		}
		return null;
	}
	/**
	 * 
	 * @param tag   the AmpCategoryValue object used as tag
	 * @param categoryKey   if not null, only the AmpCategoryValue objects belonging to the category with key=categoryKey will be returned in the set
	 * @return set of AmpCategoryValue objects tagged with the specified tag
	 */
	public static Set<AmpCategoryValue> getTaggedCategoryValues(AmpCategoryValue tag, String categoryKey) {
		try {
			if ( tag != null && tag.getUsedByValues() != null ) {
				return orderCategoryValues(tag.getUsedByValues(), categoryKey);
			}
			return null;
		}
		catch (Exception e) {
			logger.info( e.getMessage() );
			logger.info( "Trying to reload the AmpCategoryValue object in order to initialize lazy property" );
			return getTaggedCategoryValues(tag.getId(), categoryKey);
		}
	}
	/**
	 * 
	 * @param unorderedSet
	 * @param categoryKey
	 * @return a set containing filtered and ordered category values
	 */
	private static Set<AmpCategoryValue> orderCategoryValues(Set<AmpCategoryValue> unorderedSet, String categoryKey) {
		TreeSet<AmpCategoryValue> returnSet	= new TreeSet<AmpCategoryValue>(
				new Comparator<AmpCategoryValue>() {
					public int compare(AmpCategoryValue o1, AmpCategoryValue o2) {
						if ( o1.getAmpCategoryClass().getId().equals( o2.getAmpCategoryClass().getId()) ) {
							return o1.getIndex() - o2.getIndex();
						}
						else
							return o1.getAmpCategoryClass().getKeyName().compareTo( o2.getAmpCategoryClass().getKeyName() );					
					}
				}
		);
		if ( unorderedSet != null)  {		
			if ( categoryKey != null ) {
				Iterator<AmpCategoryValue> iter		= unorderedSet.iterator();
				while ( iter.hasNext() ) {
					AmpCategoryValue item				= iter.next();
					if ( item.getAmpCategoryClass().getKeyName().equals(categoryKey) )
						returnSet.add(item);
				}
			}
			else {
				returnSet.addAll( unorderedSet );
			}
			return returnSet;
		}
		return null;
			
	}
	
	
	/**
	 *
	 * @param categoryId
	 * @return AmpCategoryClass object with id=categoryId from the database
	 */
	public static AmpCategoryClass loadAmpCategoryClass(Long categoryId) throws NoCategoryClassException {
		Session dbSession			= null;
		Collection returnCollection	= null;
		try {
			dbSession			= PersistenceManager.getSession();
			String queryString;
			Query qry;

			queryString = "select c from "
				+ AmpCategoryClass.class.getName()
				+ " c where c.id=:id";
			qry			= dbSession.createQuery(queryString);
			qry.setParameter("id", categoryId, Hibernate.LONG);



			returnCollection	= qry.list();

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
		if((returnCollection!=null)&&(!returnCollection.isEmpty())){
            return (AmpCategoryClass)returnCollection.toArray()[0];
        }else{
        	throw new NoCategoryClassException("No AmpCategoryClass found with id '" + categoryId + "'");
        	//logger.error( "No AmpCategoryClass found with id '" + categoryId + "'" );
            
        }
	}
	/**
	 *
	 * @param categoryId
	 * @return AmpCategoryClass object with name=categoryName from the database
	 * @deprecated use loadAmpCategoryClassByKey instead
	 */
	@Deprecated
	public static AmpCategoryClass loadAmpCategoryClass(String name) throws NoCategoryClassException {
		Session dbSession			= null;
		Collection returnCollection	= null;
		try {
			dbSession			= PersistenceManager.getSession();
			String queryString;
			Query qry;

			queryString = "select c from "
				+ AmpCategoryClass.class.getName()
				+ " c where c.name=:name";
			qry			= dbSession.createQuery(queryString);
			qry.setParameter("name", name, Hibernate.STRING);



			returnCollection	= qry.list();

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
        if((returnCollection!=null)&&(!returnCollection.isEmpty())){
            return (AmpCategoryClass)returnCollection.toArray()[0];
        }else{
        	throw new NoCategoryClassException("No AmpCategoryClass found with name '" + name + "'");
        }

	}
	/**
	 *
	 * @param categoryName
	 * @param ordered overrides the property in AmpCategoryClass and decides whether the values should be ordered alphabetically.
	 * If null the ordered property of the AmpCategoryClass object is checked.
	 * @param request TODO
	 * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with name=categoryName
	 * @throws Exception in case request is null and the values need to be ordered alphabetically
	 */
	@Deprecated
	public static Collection<AmpCategoryValue> getAmpCategoryValueCollection(String categoryName, Boolean ordered, HttpServletRequest request) throws Exception {
		boolean shouldOrderAlphabetically;
		
		AmpCategoryClass ampCategoryClass = null; 
		try {
			ampCategoryClass = CategoryManagerUtil.loadAmpCategoryClass(categoryName);
		} catch (NoCategoryClassException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ampCategoryClass == null)
			return null;
		if (ordered == null) {
			shouldOrderAlphabetically	= ampCategoryClass.getIsOrdered();
		}
		else
			shouldOrderAlphabetically	= ordered.booleanValue();

		List<AmpCategoryValue> ampCategoryValues			= ampCategoryClass.getPossibleValues();

		if ( !shouldOrderAlphabetically )
				return ampCategoryValues;
		
		if ( shouldOrderAlphabetically && request == null )
			throw new Exception("Cannot return an ordered value of AmpCategoryValue objects if request parameter is null");

		TreeSet<AmpCategoryValue> treeSet	= new TreeSet<AmpCategoryValue>( new CategoryManagerUtil.CategoryComparator(request) );
		treeSet.addAll(ampCategoryValues);


		return treeSet;
	}
	/**
	 * This is a wrapper function for getAmpCategoryValueCollectionByKey(String categoryKey, Boolean ordered, HttpServletRequest). 
	 * The function is called with ordered = false, request = nu=ll.
	 * @param categoryKey
	 * @return 
	 */
	public static Collection<AmpCategoryValue> getAmpCategoryValueCollectionByKey(String categoryKey){
		try {
			return getAmpCategoryValueCollectionByKey(categoryKey, false, null);
		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * It should actually never get here
			 */
			return null;
		}
	}
	/**
	 *
	 * @param categoryKey
	 * @param ordered overrides the property in AmpCategoryClass and decides whether the values should be ordered alphabetically.
	 * If null the ordered property of the AmpCategoryClass object is checked.
	 * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with key=categoryKey
	 * @throws Exception in case request is null and the values need to be ordered alphabetically
	 */
	public static Collection<AmpCategoryValue> getAmpCategoryValueCollectionByKey(String categoryKey, Boolean ordered, HttpServletRequest request) throws Exception {
		boolean shouldOrderAlphabetically;
		AmpCategoryClass ampCategoryClass = null;
		try {
			ampCategoryClass = CategoryManagerUtil.loadAmpCategoryClassByKey(categoryKey);
		} catch (NoCategoryClassException e) {
			e.printStackTrace();
		}
		if (ampCategoryClass == null)
			return null;
		if (ordered == null) {
			shouldOrderAlphabetically	= ampCategoryClass.getIsOrdered();
		}
		else
			shouldOrderAlphabetically	= ordered.booleanValue();

		List<AmpCategoryValue> ampCategoryValues			= ampCategoryClass.getPossibleValues();

		if ( !shouldOrderAlphabetically )
				return ampCategoryValues;
		
		if ( shouldOrderAlphabetically && request == null )
			throw new Exception("Cannot return an ordered value of AmpCategoryValue objects if request parameter is null");

		TreeSet<AmpCategoryValue> treeSet	= new TreeSet<AmpCategoryValue>( new CategoryManagerUtil.CategoryComparator(request) );
		treeSet.addAll(ampCategoryValues);


		return treeSet;
	}
	/**
	 *
	 * @param categoryId
	 * @param ordered must be true if the AmpCategoryValues should be ordered alphabetically.
	 * If false the ordered property of the AmpCategoryClass object is checked.
	 * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with id=categoryId
	 * @throws Exception in case request is null and the values need to be ordered alphabetically
	 */
	public static Collection getAmpCategoryValueCollection(Long categoryId, Boolean ordered, HttpServletRequest request) throws Exception {
		boolean shouldOrderAlphabetically;
		
		AmpCategoryClass ampCategoryClass;
		try {
			ampCategoryClass	= CategoryManagerUtil.loadAmpCategoryClass(categoryId);
		}
		catch(Exception E) {
			E.printStackTrace();
			return null;
		}
		if (ampCategoryClass == null)
			return null;
		if (ordered == null) {
			shouldOrderAlphabetically	= ampCategoryClass.getIsOrdered();
		}
		else
			shouldOrderAlphabetically	= ordered.booleanValue();
	
		List ampCategoryValues		= ampCategoryClass.getPossibleValues();
	
		if ( !shouldOrderAlphabetically )
				return ampCategoryValues;
		
		if ( shouldOrderAlphabetically && request == null )
			throw new Exception("Cannot return an ordered value of AmpCategoryValue objects if request parameter is null");
	
		TreeSet treeSet	= new TreeSet( new CategoryManagerUtil.CategoryComparator(request) );
		treeSet.addAll(ampCategoryValues);
		return treeSet;
		
	}
	/**
	 * returns a string containing only ascii characters
	 * @param input The string that needs to be filtered
	 * @return
	 */
	public static String asciiStringFilter (String input) {
		byte [] bytearray		= input.getBytes(); 
		
		CharsetDecoder decoder	= Charset.forName("US-ASCII").newDecoder();
		decoder.replaceWith("_");
		decoder.onMalformedInput(CodingErrorAction.REPLACE);
		decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
		
		try {
			CharBuffer buffer 		= decoder.decode( ByteBuffer.wrap(bytearray) );
			return buffer.toString();
		} catch (CharacterCodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getTranslationKeyForCategoryName(String classKeyName) {
			String translationKey		= "cm:category_" + classKeyName + "_name";
			return translationKey;
		
	}
	public static String getTranslationKeyForCategoryDescription(String classKeyName) {
		String translationKey		= "cm:category_" + classKeyName + "_description";
		return translationKey;
	
	}
	/**
	 *
	 * @param ampCategoryValue
	 * @return the translation key for the ampCategoryValue
	 */
	public static String getTranslationKeyForCategoryValue(AmpCategoryValue ampCategoryValue) {
		return getTranslationKeyForCategoryValue(ampCategoryValue.getValue(), ampCategoryValue.getAmpCategoryClass().getKeyName() );
	}
	
	/**
	 *
	 * @param ampCategoryValue
	 * @return the translation key for the ampCategoryValue
	 */
	public static String getTranslationKeyForCategoryValue(String value, String classKeyName) {
		String filteredValue			= asciiStringFilter( value );
		String translationKey			= "cm:category_" + classKeyName +
										"_" + filteredValue;
		return translationKey.toLowerCase();
	}
	
	/**
	 *
	 * @param key The key of the AmpCategoryClass object. (A key can be attributed when creating a new category)
	 * @return The AmpCategoryClass object with the specified key. If not found returns null.
	 */
	public static AmpCategoryClass loadAmpCategoryClassByKey(String key) throws NoCategoryClassException
	{
		Session dbSession			= null;
		Collection col=new ArrayList();
		try {
			dbSession						= PersistenceManager.getSession();
			//AmpCategoryClass dbCategory		= new AmpCategoryClass();
				String queryString	= "select c from " + AmpCategoryClass.class.getName() + " c where c.keyName=:key";
				Query query			= dbSession.createQuery(queryString);
				query.setParameter("key", key, Hibernate.STRING);
				 col		= query.list();

		} catch (Exception ex) {
			logger.error("Error retrieving AmpCategoryClass with key '" + key + "': " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}

		if(!col.isEmpty())
		{
			Iterator it=col.iterator();

			AmpCategoryClass x=(AmpCategoryClass) it.next();
			return x;
		}
		else{
			throw new NoCategoryClassException("No AmpCategoryClass found with key '" + key + "'");
		}
	}

	public static String getStringValueOfAmpCategoryValue(AmpCategoryValue ampCategoryValue) {
		if (ampCategoryValue != null) {
			return ampCategoryValue.getValue();
		}
		return "";
	}
	
	public static Collection<String> getStringValueOfAmpCategoryValues(Collection<AmpCategoryValue> values) {
		Collection<String> ret=new ArrayList<String>();
		Iterator i=values.iterator();
		while (i.hasNext()) {
			AmpCategoryValue elem = (AmpCategoryValue) i.next();
			ret.add(elem.getValue());
		}
		return ret;
	}
	
	public static void addValueToCategory(String categoryKey, String value) throws Exception {
		Session dbSession			= null;
		String error				= null;
		
		try {
			dbSession			= PersistenceManager.getSession();
			String queryString;
			Query qry;

			queryString = "select c from "
				+ AmpCategoryClass.class.getName()
				+ " c where c.keyName=:categoryKey";
			qry			= dbSession.createQuery(queryString);
			qry.setString("categoryKey", categoryKey );
			
			List<AmpCategoryClass> resultList		= qry.list();
			
			if ( resultList == null || resultList.size() != 1 )
				error	= "There was a problem loading the specified AmpCategoryClass with key:" + categoryKey;
			else {
				AmpCategoryClass ampCategoryClass	= resultList.get(0);
				
				if ( ampCategoryClass.getPossibleValues() == null )
					ampCategoryClass.setPossibleValues( new ArrayList<AmpCategoryValue>() );
				
				AmpCategoryValue ampCategoryValue 	= new AmpCategoryValue();
				ampCategoryValue.setValue(value);
				ampCategoryValue.setAmpCategoryClass(ampCategoryClass);
				ampCategoryValue.setIndex( ampCategoryClass.getPossibleValues().size() );
				
				List<AmpCategoryValue> tempList		= new ArrayList<AmpCategoryValue>();
				tempList.addAll(ampCategoryClass.getPossibleValues());
				tempList.add( ampCategoryValue );
				
				if ( CategoryManager.checkDuplicateValues(tempList) == null ) {
					ampCategoryClass.getPossibleValues().add( ampCategoryValue );
				}
				else
					error							= "The value already exists";
				dbSession.flush();
			}

		} catch (Exception ex) {
			logger.error("Unable to get Categories: " + ex.getMessage());
			error	= "A Hibernate exception occured. See Stacktrace above.";
			ex.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		
		if ( error != null )
			throw new Exception( error );
	}
	
	/**
	 * Verify that a category value has been inserted in CategoryConstants as a HardCodedcategoryValue
	 * @return true if the category value key has been inserted in the CategoryConstants class.
	 */
	public static boolean verifyDeletionProtectionForCategoryValue (String categoryKey, String valueKey ) {
		Field[] fields	= CategoryConstants.class.getDeclaredFields();
		for (int i=0; i< fields.length; i++  ) {
			Class fieldClass	= fields[i].getType();
			if ( fieldClass.equals( HardCodedCategoryValue.class ) ) {
				HardCodedCategoryValue proprietyValue;
				try {
					proprietyValue = (HardCodedCategoryValue)fields[i].get(null);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if ( proprietyValue.getCategoryKey().equals(categoryKey) 
						&& proprietyValue.getValueKey().equals(valueKey) ) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean equalsCategoryValue(AmpCategoryValue value, HardCodedCategoryValue hcValue){
			if ( value != null && value.getAmpCategoryClass().getKeyName().equals( hcValue.getCategoryKey() ) && value.getValue().equals( hcValue.getValueKey() ) )
				return true;
			else 
				return false;
	}
	
	
	public static boolean isCategoryKeyInUse(String key) {
		try {
			CategoryManagerUtil.loadAmpCategoryClassByKey(key);
			return true;
		} catch (NoCategoryClassException e) {
			// TODO Auto-generated catch block
			logger.info("Category key '" + key + "' was not found in database.");
			return false;
		}
	}
	
	public static String checkImplementationLocationCategory ()  {
			String errorString			= "The following values were not found: ";
			String separator			= "";
			
			AmpCategoryValue country;
			try {
				country = getAmpCategoryValueFromDB(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
			} catch (Exception e) {
				country = null;
			}
			AmpCategoryValue region;
			try {
				region 	= getAmpCategoryValueFromDB(CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
			} catch (Exception e) {
				region	= null;
			}
			AmpCategoryValue zone;
			try {
				zone = getAmpCategoryValueFromDB(CategoryConstants.IMPLEMENTATION_LOCATION_ZONE);
			} catch (Exception e) {
				zone = null;
			}
			AmpCategoryValue district;
			try {
				district = getAmpCategoryValueFromDB(CategoryConstants.IMPLEMENTATION_LOCATION_DISTRICT);
			} catch (Exception e) {
				district = null;
			}
			
			if ( country == null ) {
				errorString 			+= "Country";
				separator				= ", ";
			}
			if ( region == null ){
				errorString				+= separator + "Region";
				separator				= ", ";
			}
			if ( zone == null ){
				errorString				+= separator + "Zone";
				separator				= ", ";
			}
			if ( district == null ){
				errorString				+= separator + "District";
				separator				= ", ";
			}
			if ( separator != "" ) {
				return errorString;
			}
			else { // checking order
				if ( country.getIndex() >= region.getIndex() )
					return "Country must be before Region";
				if ( region.getIndex() >= zone.getIndex() )
					return "Region must be before Zone";
				if ( zone.getIndex() >= district.getIndex() )
					return "Zone must be before District";
			}
			return null;
	}
	
	/**
	 *
	 * @author Alex Gartner
	 * A comparator for AmpCategoryValue objects which is needed when ordering them alphabetically
	 */
	public static class CategoryComparator implements Comparator<AmpCategoryValue> {
		private HttpServletRequest request;
		public CategoryComparator(HttpServletRequest request) {
			this.request	= request;
		}
		
		public int compare(AmpCategoryValue catValue1, AmpCategoryValue catValue2) {
			if ( request == null )
				return catValue1.getValue().compareTo( catValue2.getValue() );
			
			String transValue1		= CategoryManagerUtil.translateAmpCategoryValue(catValue1, request);
			String transValue2		= CategoryManagerUtil.translateAmpCategoryValue(catValue2, request);
			
			return transValue1.compareTo( transValue2 );

		}
		public boolean equals(AmpCategoryValue value1, AmpCategoryValue value2) {
			if ( this.compare(value1, value2) == 0 )
					return true;
			return false;
		}
	}

}

