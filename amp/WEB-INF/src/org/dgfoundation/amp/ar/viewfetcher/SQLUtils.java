package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.FilterParam;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.engine.spi.TypedValue;

public class SQLUtils {
	
	public final static String SQL_UTILS_NULL = "###NULL###";
	
	/**
	 * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
	 * @param tableName - the table / view whose columns to fetch
	 * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
	 * @return
	 * @throws SQLException 
	 */
	public static LinkedHashSet<String> getTableColumns(final String tableName, boolean crashOnDuplicates){
		return new LinkedHashSet<String>(getTableColumnsWithTypes(tableName, crashOnDuplicates).keySet());
	}
	
	/**
	 * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
	 * @param tableName - the table / view whose columns to fetch
	 * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
	 * @return Map<ColumnName, data_type>
	 */
	public static LinkedHashMap<String, String> getTableColumnsWithTypes(final String tableName, boolean crashOnDuplicates){
		LinkedHashMap<String, String> res = new LinkedHashMap<>();
		String query = String.format("SELECT c.column_name, c.data_type FROM information_schema.columns As c WHERE table_schema='public' AND table_name = '%s' ORDER BY c.ordinal_position", tableName.toLowerCase());
		try (Connection jdbcConnection = PersistenceManager.getJdbcConnection()) {
			try(ResultSet rs = rawRunQuery(jdbcConnection, query, null)) {
				while (rs.next()) {
					String columnName = rs.getString(1);
					String columnType = rs.getString(2);
					
					if (crashOnDuplicates && res.containsKey(columnName))
						throw new RuntimeException("not allowed to have duplicate column names in table " + tableName);
					res.put(columnName, columnType);
				}
			}
		}
		catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
		return res;
	}
	
	/**
	 * returns the rowcount in a table
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public static long countRows(Connection conn, String tableName) {
		return fetchLongs(conn, "SELECT COUNT(*) FROM " +tableName).get(0);
	}
	
	/**
	 * equivalent to calling {@link #getTableColumns(String, false)}
	 * @param tableName
	 * @return
	 */
	public static LinkedHashSet<String> getTableColumns(final String tableName)
	{
		return getTableColumns(tableName, false);
	}
	
	public static boolean tableExists(String tableName)
	{
		return !getTableColumns(tableName).isEmpty();
	}
	
	public static void executeQuery(Connection conn, String query)
	{
		try
		{
			Statement statement = conn.createStatement();
			statement.execute(query);
			statement.close();
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * runs a query, optimizing for throughput
	 * @param connection
	 * @param query
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet rawRunQuery(Connection connection, String query, ArrayList<FilterParam> params) throws SQLException
	{
		//logger.info("Running raw SQL query: " + query);
		
		PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		if (params != null)
		{
			//logger.debug("the parameters are:");
			for (int i = 0; i < params.size(); i++) 
			{
				ps.setObject(i + 1, params.get(i).getValue(), params.get(i).getSqlType());
				//logger.debug(String.format("\tvalue: %s, SQL type: %d", params.get(i).getValue(), params.get(i).getSqlType()));
			}
		}
		
		ResultSet rs = ps.executeQuery();
		rs.setFetchSize(500);
		
		return rs;
	}
	
	/**
	 * fetches an ArrayList of longs
	 * @param connection
	 * @param query
	 * @return
	 */
	public static List<Long> fetchLongs(Connection connection, String query) {
		List<Long> res = new ArrayList<>();
		try(ResultSet rs = rawRunQuery(connection, query, null)) {
			while (rs.next()) {
				res.add(rs.getLong(1));
			}
		}
		catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
		return res;
	}
	
	/**
	 * runs a query and returns a list of the nth elements in each of the rows
	 * @param connection
	 * @param query
	 * @param n
	 * @return
	 */
	public static <T> List<T> fetchAsList(Connection connection, String query, int n)
	{
		ResultSet rs = null;
		try {
			rs = rawRunQuery(connection, query, null);
			return fetchAsList(rs, n, " with query " + query);
		}
		catch(SQLException e) {
			throw new RuntimeException("Error fetching list of values with query " + query, e);
		}
//		finally
//		{
//			PersistenceManager.closeQuietly(rs);
//		}
	}
	
	public static <T> List<T> fetchAsList(ResultSet rs, int n, String errMsgAdd)
	{
		try
		{
			ArrayList<T> result = new ArrayList<T>();
			
			while (rs.next())
			{
				T elem = (T) rs.getObject(n);
				result.add(elem);
			}
			return result;
		}
		catch(SQLException e)
		{
			throw new RuntimeException("Error fetching list of values" + errMsgAdd, e);
		}
	}	
	
	/**
	 * generates a raw comma-separated-values
	 * @param values
	 * @return
	 */
	public static String generateCSV(java.util.Collection<?> values)
	{
		StringBuilder result = new StringBuilder();
		java.util.Iterator<?> it = values.iterator();
		while (it.hasNext())
		{
			result.append(it.next().toString());
			if (it.hasNext())
				result.append(", ");
		}
		return result.toString();
	}
	
	public static Criterion getUnaccentILikeExpression(final String propertyName, final String value, final String locale, final MatchMode matchMode) {
		return new Criterion(){
			private static final long serialVersionUID = -8979378752879206485L;

			@Override
			public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
				Dialect dialect = criteriaQuery.getFactory().getDialect();
				String[] columns = criteriaQuery.findColumns(propertyName, criteria);
				String entityName = criteriaQuery.getEntityName(criteria);
              
				String []ids=criteriaQuery.getIdentifierColumns(criteria);
				if (columns.length!=1)
					throw new HibernateException("ilike may only be used with single-column properties");
				if (ids.length!=1)
					throw new HibernateException("We do not support multiple identifiers just yet!");

				if ( dialect instanceof PostgreSQLDialect ) {
					//AMP-15628 - the replace of "this_." with "" inside the ids and columns was removed
					String ret=" "+ids[0]+" = any(contentmatch('"+entityName+"','"+columns[0]+"','"+locale+"', ?)) OR ";
					ret+=" unaccent(" + columns[0] + ") ilike " +  "unaccent(?)";
					return ret;
				} else {
					throw new HibernateException("We do not handle non-postgresql databases yet, sorry!");
				}
			}
		

			@Override
			public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException 
			{
				return new TypedValue[] { criteriaQuery.getTypedValue( criteria, propertyName, matchMode.toMatchString(value).toLowerCase() ) ,
					criteriaQuery.getTypedValue( criteria, propertyName, matchMode.toMatchString(value).toLowerCase() )};
			}
			};
		}
		
		    
		//ao.* from amp_organisation ao -> "ao.amp_org_id, ao.column2, getOrgName(....), ...."
		/**
		 * 
		 * @param tableName
		 * @param tableAlias
		 * @param renames Map<ColumnName, String to Replace with>
		 * @return
		 */
		public static String rewriteQuery(String tableName, String tableAlias, Map<String, String> renames)
		{
			LinkedHashSet<String> columns = SQLUtils.getTableColumns(tableName);
			ArrayList<String> outputs = new ArrayList<String>();
			for(String column:columns)
			{
				if (renames.containsKey(column))
					outputs.add(renames.get(column) + " AS " + column);
				else
					outputs.add(tableAlias + "." + column);
			}
   		
			return Util.collectionAsString(outputs);
		}
		
		/**
		 * runs an INSERT query with all the bells and whistles and escaping
		 * @param conn
		 * @param tableName
		 * @param idColumnName
		 * @param seqName
		 * @param coordsList - each entry corresponds to a row to insert
		 */
		public static void insert(Connection conn, String tableName, String idColumnName, String seqName, List<Map<String, Object>> coordsList) {
			if ((idColumnName == null) ^ (seqName == null))
				throw new RuntimeException("idColumnName should be both null or both non-null");
			
			int rowsPerInsert = 300;
			int nrSegments = coordsList.size() / rowsPerInsert + 1;			
			for(int i = 0; i < nrSegments; i++) {
				int segmentStart = i * rowsPerInsert; // inclusive
				int segmentEnd = Math.min(coordsList.size(), (i + 1) * rowsPerInsert); // exclusive
				if (segmentStart >= segmentEnd)
					break; 
				String query = buildMultiRowInsert(tableName, idColumnName, seqName, coordsList.subList(segmentStart, segmentEnd));
				//System.out.println("executing mondrian dimension table insert " + query);
				SQLUtils.executeQuery(conn, query.toString());
			}
		}
		
		/**
		 * builds a statement of the form INSERT INTO $tableName$ (col1, col2, col3) VALUES (val11, val12, val13), (val21, val22, val23);
		 * @param tableName
		 * @param idColumnName
		 * @param seqName
		 * @param coordsList
		 * @return
		 */
		public static String buildMultiRowInsert(String tableName, String idColumnName, String seqName, List<Map<String, Object>> coordsList) {
			if ((idColumnName == null) ^ (seqName == null))
				throw new RuntimeException("idColumnName and seqName should be either both null or both nonnull");
			
			if (coordsList.isEmpty())
				return null; // nothing to do
			
			List<String> keys = new ArrayList<String>(coordsList.get(0).keySet());
			StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
			boolean needComma = false;
			for(String key:keys) {
				if (needComma)
					query.append(",");
				query.append(key);
				needComma = true;
			}
			
			if (idColumnName != null) {
				if (!keys.isEmpty())
					query.append(",");
				query.append(idColumnName);
			}
			
			query.append(") VALUES");
			boolean firstRow = true;
			for(Map<String, Object> coords:coordsList) {
				query.append(firstRow ? " " : ",");
				query.append(buildCoordsLine(coords, keys, idColumnName, seqName));
				firstRow = false;
			}
			query.append(";");
			
			return query.toString();
		}
		
		/**
		 * builds a line of type (colValue, colValue, colValue)
		 * @param coords
		 * @param keys
		 * @param idColumnName
		 * @param seqName
		 * @return
		 */
		public static String buildCoordsLine(Map<String, Object> coords, List<String> keys, String idColumnName, String seqName) {
			boolean needComma = false;
			StringBuilder query = new StringBuilder("(");
			for(String key:keys) {
				if (needComma)
					query.append(",");
				query.append(stringifyObject(coords.get(key)));
				needComma = true;
			}
			
			if (idColumnName != null) {
				if (!keys.isEmpty())
					query.append(",");
				query.append(String.format("nextval('%s')", seqName));
			}
			query.append(")");
			return query.toString();
		}
		
		/**
		 * returns a ready-to-be-included-into-SQL-query representation of a var
		 * @param obj
		 * @return
		 */
		public static String stringifyObject(Object obj) {
			if (obj instanceof Number)
				return obj.toString();
			else if (obj instanceof String)
			{
				if (obj.toString().equals(SQL_UTILS_NULL))
					return "NULL";
				//$t$blablabla$t$ - dollar-quoting
				//return "'" + obj.toString() + "'";
				String dollarQuote = "$dAaD41$";
				return dollarQuote + obj.toString() + dollarQuote;
			}
			else if (obj == null)
				return "NULL";
			else
			{
				return "'" + obj.toString() + "'";
			}
		}
}
