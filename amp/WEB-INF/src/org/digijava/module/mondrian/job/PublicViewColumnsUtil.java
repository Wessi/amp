package org.digijava.module.mondrian.job;

import java.util.*;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.hibernate.Session;

/**
 * Utilities regarding cached_ variants of reports' extractor tables / views
 * @author Dolghier Constantin
 *
 */
public class PublicViewColumnsUtil 
{
	protected static Logger logger = Logger.getLogger(PublicViewColumnsUtil.class);
	
	/**
	 * only change this to false when you have REALLY good reasons for this and ONLY WHILE TESTING OTHER STUFF <br />
	 * NEVER RUN ANY AMP 2.7+ PRODUCTION INSTANCE WITH THIS SET TO false !!! 
	 * Злой маньяк Constantin is taking care that this rule is enforced!
	 */
	public final static boolean CRASH_ON_INVALID_COLUMNS = true;
	
	public static String getPublicViewTable(String extractorView)
	{
		return ArConstants.VIEW_PUBLIC_PREFIX + extractorView;
	}
	
	/**
	 * returns Map<String extractorViewName, Boolean does_cached_view_exist>
	 * @param session
	 * @return
	 */
	protected static Map<String, CachedTableState> getExtractorColumns(java.sql.Connection conn)
	{
		try
		{
			Map<String, CachedTableState> result = new TreeMap<String, CachedTableState>();
			List<String> views = SQLUtils.<String>fetchAsList(conn, "SELECT DISTINCT(extractorview) FROM amp_columns WHERE extractorview IS NOT NULL ORDER BY extractorview", 1);
			for(String viewName:views)
			{
				String cachedViewName = getPublicViewTable(viewName);
				CachedTableState publicViewState = compareTableStructures(viewName, cachedViewName);
				result.put(viewName, publicViewState);
			}
			return result;
		}
		catch(Exception e)
		{
			throw new RuntimeException("error getting extractor columns", e);
		}
	}
	
	protected static CachedTableState compareTableStructures(String viewName, String cachedViewName)
	{
		if (!SQLUtils.tableExists(viewName))
			return CachedTableState.ORIGINAL_TABLE_MISSING;
		if (!SQLUtils.tableExists(cachedViewName))
			return CachedTableState.CACHED_TABLE_MISSING;
		List<String> originalCols = new ArrayList<String>(SQLUtils.getTableColumns(viewName));
		List<String> cacheCols = new ArrayList<String>(SQLUtils.getTableColumns(cachedViewName));
		if (originalCols.size() != cacheCols.size())
			return CachedTableState.CACHED_TABLE_DIFFERENT_STRUCTURE;
		for(int i = 0; i < originalCols.size(); i ++)
		{
			String a = originalCols.get(i);
			String b = cacheCols.get(i);
			if (!a.equals(b))
				return CachedTableState.CACHED_TABLE_DIFFERENT_STRUCTURE;
		}
		return CachedTableState.CACHED_TABLE_OK;
	}
	
	/**
	 * strategy of this function:
	 * <ul>
	 * 	<li>non-existing tables will always be created and populated</li>
	 *  <li>existing_and_correct tables will dropped, created and updated if updateData = true</li>
	 *   <li>existing but faulty tables will be dropped and recreated if updateData = true </li>
	 * </ul>
	 * @param conn
	 * @param updateSchemaIfDifferent
	 * @param updateData
	 */
	public static void maintainPublicViewCaches(java.sql.Connection conn, boolean updateData)
	{
		logger.info(String.format("doing maintenance on public view caches, updateData = %b", updateData));
		Map<String, CachedTableState> viewsStates = getExtractorColumns(conn);
		for(String viewName:viewsStates.keySet())
		{
			CachedTableState viewState = viewsStates.get(viewName);
			if (viewState != CachedTableState.CACHED_TABLE_OK)
				logger.info(String.format("the view %s's cache has the schema state %s", viewName, viewState));
			try
			{
				doColumnMaintenance(conn, viewName, viewState, updateData);
			}
			catch(Exception e)
			{
				logger.error("error while doing maintenance on the view!", e);
			}
		}
	}
	
	private static void doColumnMaintenance(java.sql.Connection conn, String viewName, CachedTableState viewState, boolean updateData)
	{
		switch(viewState)
		{
			case ORIGINAL_TABLE_MISSING:
			{
				logger.error(String.format("a view referenced in amp_columns [%s] is nonexistant in the database. THIS IS A SERIOUS ERROR!", viewName));
				if (CRASH_ON_INVALID_COLUMNS)
				{
					logger.fatal("Crashing AMP on purpose. DO NOT IGNORE THIS MESSAGE OR DISABLE THE CHECK - FIX THE DATABASE. Offending amp_columns-referenced view is " + viewName);
					throw new Error("a column references non-existant view " + viewName + ". Fix your database!");
				}
				return;
			}
			case CACHED_TABLE_MISSING:
			{
				String cachedView = getPublicViewTable(viewName);
				createTableCache(conn, viewName, cachedView);					
			}
			break;
			
			case CACHED_TABLE_DIFFERENT_STRUCTURE:
			{
//				if (!updateData)
//				{
//					logger.info("\t->not allowed to update table schema, skipping");
//					return;
//				}
				String cachedView = getPublicViewTable(viewName);
				createTableCache(conn, viewName, cachedView);					
			}
			break;
			
			case CACHED_TABLE_OK:
			{
				if (!updateData)
				{
					//logger.info("\t->not allowed to refresh table, skipping");
					return;
				}
				String cachedView = getPublicViewTable(viewName);
				createTableCache(conn, viewName, cachedView); // recreating table from scratch anyway, because God-knows-what we have there (maybe it is the first script execution and the awful state pre-AMP 2.7.1 is still there
			}			
		}

	}
	
	/**
	 * drops a table and replaces it with DDL, data and indices of a view/table
	 * @param conn
	 * @param viewName
	 * @param cacheName
	 */
	protected static void createTableCache(java.sql.Connection conn, String viewName, String cacheName)
	{
		logger.info(String.format("\t->creating a cache named %s for view %s...", cacheName, viewName));
		cacheName = cacheName.toLowerCase();
		if (cacheName.equals("cached_amp_activity_group") && SQLUtils.tableExists(cacheName))
		{
			SQLUtils.executeQuery(conn, String.format("TRUNCATE %s", cacheName));
			SQLUtils.executeQuery(conn, String.format("INSERT INTO %s SELECT * FROM %s WHERE amp_activity_last_version_id IS NOT NULL", cacheName, viewName));
		}
		else
		{
			SQLUtils.executeQuery(conn, String.format("DROP TABLE IF EXISTS %s", cacheName));
			SQLUtils.executeQuery(conn, String.format("CREATE TABLE %s AS SELECT * FROM %s;", cacheName, viewName));
		}
		Collection<String> columns = SQLUtils.getTableColumns(viewName);
		for(String columnName:columns)
			if (looksLikeIndexableColumn(viewName, columnName))
			{
				logger.debug(String.format("\t\t...creating an index for column %s of cached table %s", columnName, cacheName));
				SQLUtils.executeQuery(conn, String.format("CREATE INDEX ON %s(%s)", cacheName, columnName));
			}
	}
	
	/**
	 * decides whether a particular column of a particular view should have an index on top of it.<br />
	 * current implementation is a quick-and-dirty-and-good-enough heuristics which ignores the viewName. For cases when the heuristics fails, hardcoded cases can be added
	 * @param viewName
	 * @param columnName
	 * @return
	 */
	protected static boolean looksLikeIndexableColumn(String viewName, String columnName)
	{
		columnName = columnName.toLowerCase();
		return columnName.endsWith("id") || columnName.startsWith("id") || columnName.endsWith("_type") || columnName.endsWith("_code") || columnName.endsWith("_name");
	}
	
}
