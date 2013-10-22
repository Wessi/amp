package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.dgfoundation.amp.ar.FilterParam;

/**
 * fetches a view which contains internationalized String columns; feeds to the consumer the query result (plus translated values)
 * @author Dolghier Constantin
 *
 */
public class I18nDatabaseViewFetcher extends DatabaseViewFetcher{
	
	/**
	 * Map<ClassName, ColumnCache>
	 */
	public final Map<PropertyDescription, ColumnValuesCacher> cachers;
	public final String locale;
	public final I18nViewDescription viewDesc;
	
	/**
	 * the respective number of the translated columns in the view. 1-based column numbering
	 */
	private Map<String, Integer> colNameToColNumber;
	
	/**
	 * the respective name of the translated columns in the view. 1-based column numbering
	 */
	private Map<Integer, String> colNumberToColName;
	
	public I18nDatabaseViewFetcher(String viewName, String condition, String locale, Map<PropertyDescription, ColumnValuesCacher> cachers, Connection connection, String... columnNames)
	{
		super(viewName, condition, connection, columnNames);
		this.locale = locale;
		this.cachers = cachers;
		this.viewDesc = InternationalizedViewsRepository.i18Models.get(this.viewName);
		if (this.viewDesc == null)
			throw new RuntimeException("asked to i18n an unconfigured view: " + this.viewName);

	}
	
	/**
	 * multi-step translation:
	 * 0. fetches the raw data from the database (does not fetch the translated-text-columns, as those are worthless anyway)
	 * 1. collect all the ids of all the translateable columns (distinct set of ids for each column); build a "usedIds" set for each column
	 * 2. consult the cache of each of the columns and remove from "usedIds" the already-fetched entries (by, for example, a different view which uses the same cache)
	 * 3. run (2 or 3) SELECT's for each of the columns to fetch the respective translations into the caches
	 * 4. return a proxying/delegating ResultSet which is a passthrough for all the columns except the translated ones. The translated columns are seeked in O(1) in the caches
	 */
	@Override
	public ResultSet fetchRows(ArrayList<FilterParam> params) throws SQLException
	{
		String queryColumnsPart = buildColumnsPart();
		String query = "SELECT " + queryColumnsPart + " FROM " + this.viewName + " " + this.condition;
		ResultSet rawResults = rawRunQuery(connection, query, params);
				
		// scan all ids of all i18n'ed columns, fetch all results into cache IF NOT ALREADY EXISTING, return a delegator which reads the cache
		Map<Integer, Set<Long>> usedIds = new HashMap<Integer, Set<Long>>(); // Map<columnNumber, Set<TranslatedElementId>> - everything which the user requested
		Map<Integer, String> indexColumnNames = new HashMap<Integer, String>();
		for(int colNr:this.colNumberToColName.keySet())
		{
			I18nViewColumnDescription colDesc = viewDesc.getColumnDescription(colNumberToColName.get(colNr));
			if (!colDesc.isCalculated())
			{
				usedIds.put(colNr, new HashSet<Long>());
				indexColumnNames.put(colNr, colDesc.indexColumnName);
			}
		}
		
		// FIRST STEP: scan ids of entities to translate
		// first iteration -> scan all the columns' indices
		while (rawResults.next())
		{
			for(int colNr:this.colNumberToColName.keySet())
				if (usedIds.containsKey(colNr)) // no usedIds kept for calculated columns
				{
					Long id = rawResults.getLong(indexColumnNames.get(colNr));
					if (id != null)
						usedIds.get(colNr).add(id);
				}
		}		
		rawResults.beforeFirst(); // reset for the wrapper
		
		// SECOND STEP: subtract the ids of translatable entities which are already in the cacher
		for(int colNr:this.colNumberToColName.keySet())
			if (usedIds.containsKey(colNr))
			{
				String columnName = this.colNumberToColName.get(colNr);
				PropertyDescription propDescr = viewDesc.getColumnDescription(columnName).prop;
				if (!cachers.containsKey(propDescr))
					cachers.put(propDescr, new ColumnValuesCacher(propDescr));

				Set<Long> existingIdsSet = usedIds.get(colNr);
				Set<Long> alreadyExistingTranslations = cachers.get(propDescr).values.keySet();
				for(Long existingId:alreadyExistingTranslations)
					existingIdsSet.remove(existingId);
			}
		
		
		// THIRD STEP: fetch the translations
		for(int colNr:this.colNumberToColName.keySet())
		{
			String columnName = this.colNumberToColName.get(colNr);
			PropertyDescription prop = viewDesc.getColumnDescription(columnName).prop;
			if (!prop.isCalculated())
			{
				//String className =prop.className;
			
				Set<Long> ids = usedIds.get(colNr);
				Map<Long, String> values = prop.generateValues(connection, ids, locale);
				cachers.get(prop).importValues(values);
			}
		}
		return new TranslatingResultSet(rawResults); // STEP 4: return delegating ResultSet
	}
	
	/**
	 * SIDE-EFFECT: overwrites {@link #colNameToColNumber} and {@link #colNumberToColName} 
	 * @return
	 */
	protected String buildColumnsPart()
	{
		colNameToColNumber = new HashMap<String, Integer>();
		colNumberToColName = new HashMap<Integer, String>();
		
		List<String> columnStrings = new ArrayList<String>();
		
		int number = 0;
		for(String columnName:this.columnNames)
		{
			number ++;
			I18nViewColumnDescription colDesc = viewDesc.getColumnDescription(columnName);
			if (colDesc == null)
				columnStrings.add(columnName);
			else
			{
				columnStrings.add(String.format("'' as %s", columnName)); // do not fetch this column from database - save time, memory and bandwidth
			
				colNameToColNumber.put(columnName, number);
				colNumberToColName.put(number, columnName);
			}
		}
		return generateCSV(columnStrings);
	}
	
	/**
	 * a ResultSet which delegates everything to an underlying ResultSet, except getObject() and getString() -> for those it does translations (if it is in a translated view column)
	 * @author Dolghier Constantin
	 *
	 */
	class TranslatingResultSet extends DelegResultSet
	{
		public TranslatingResultSet(ResultSet rs)
		{
			super(rs);
		}
		
		@Override
		public Object getObject(int nr) throws SQLException
		{
			if (colNumberToColName.containsKey(nr))
				return getTranslation(colNumberToColName.get(nr));
			return super.getObject(nr);
		}

		@Override 
		public Object getObject(String columnLabel) throws SQLException
		{
			if (colNameToColNumber.containsKey(columnLabel))
				return getTranslation(columnLabel);
			return super.getObject(columnLabel);
		}

		@Override
		public String getString(int nr) throws SQLException
		{
			if (colNumberToColName.containsKey(nr))
				return getTranslation(colNumberToColName.get(nr));
			return super.getString(nr);
		}
		
		@Override
		public String getString(String columnLabel) throws SQLException
		{
			if (colNameToColNumber.containsKey(columnLabel))
				return getTranslation(columnLabel);
			return super.getString(columnLabel);
		}
		
		/**
		 * does the effective translation of an entry
		 * @param columnLabel
		 * @return
		 * @throws SQLException
		 */
		protected String getTranslation(String columnLabel) throws SQLException
		{
			I18nViewColumnDescription colDesc = viewDesc.getColumnDescription(columnLabel);
			if (colDesc.isCalculated())
			{
				return colDesc.prop.getValueFor(this);
			}
			
			Long idToTranslate = super.getDelegate().getLong(colDesc.indexColumnName);
			
			if ((idToTranslate == null) || (idToTranslate.longValue() <= 0))
				return super.getString(columnLabel); // this particular entry should not be translated
			
			
			if (!cachers.get(colDesc.prop).values.containsKey(idToTranslate))
				throw new RuntimeException("error getting item with id " + idToTranslate + " from view " + viewName + ", column " + columnLabel);
			
			return cachers.get(colDesc.prop).values.get(idToTranslate);
		}
		
		@Override
		public String toString()
		{
			return "Translating ResultSet delegating to " + super.toString();
		}
		
	}
}
