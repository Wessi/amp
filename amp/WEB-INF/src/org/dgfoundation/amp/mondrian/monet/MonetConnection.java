package org.dgfoundation.amp.mondrian.monet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.Constants;

/**
 * this class wraps a connection to MonetDB
 * @author simple
 *
 */
public class MonetConnection implements AutoCloseable {
	
	public final Connection conn;
	private static DataSource dataSource = getMonetDataSource();
	
	private MonetConnection() throws SQLException {
		//this.conn = dataSource.getConnection();
		this.conn = getDirectConnection();
		//this.conn = DriverManager.getConnection("jdbc:monetdb://localhost/amp_moldova_210", "monetdb", "monetdb");
		this.conn.setAutoCommit(false);
	}
	
	/**
	 * fix for AMP-18357 and AMP-18352: MonetDB + Tomcat-DBCP = ( X )
	 * @return
	 */
	private static Connection getDirectConnection() throws SQLException {
		org.apache.tomcat.jdbc.pool.DataSource src = (org.apache.tomcat.jdbc.pool.DataSource) dataSource;
		return DriverManager.getConnection(src.getUrl(), src.getUsername(), src.getUsername());
	}
	
	public static MonetConnection getConnection() {
		try {
			return new MonetConnection();
		}
		catch(SQLException e) {throw new RuntimeException(e);}
	}
	
	@Override public void finalize() {
		close();
	}
	
	@Override public void close(){
		PersistenceManager.closeQuietly(conn);
	}
	
	/**
	 * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
	 * @param tableName - the table / view whose columns to fetch
	 * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
	 * @return
	 * @throws SQLException 
	 */
	public LinkedHashSet<String> getTableColumns(final String tableName, boolean crashOnDuplicates){
		return new LinkedHashSet<String>(getTableColumnsWithTypes(tableName, crashOnDuplicates).keySet());
	}
	
	/**
	 * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
	 * @param tableName - the table / view whose columns to fetch
	 * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
	 * @return Map<ColumnName, data_type>
	 */
	public LinkedHashMap<String, String> getTableColumnsWithTypes(final String tableName, boolean crashOnDuplicates){
		String query = String.format("SELECT c.name, c.type FROM sys.columns c WHERE c.table_id = (SELECT t.id FROM sys.tables t WHERE t.name='%s') ORDER BY c.number", tableName.toLowerCase());
		return SQLUtils.getTableColumnsWithTypes(this.conn, tableName, query, crashOnDuplicates);
	}
	
	public Set<String> getTablesWithNameMatching(String begin) {
		return SQLUtils.getTablesWithNameMatching(this.conn, "SELECT t.name FROM sys.tables t WHERE NOT t.system", begin);
	}
		
	/**
	 * equivalent to calling {@link #getTableColumns(String, false)}
	 * @param tableName
	 * @return
	 */
	public LinkedHashSet<String> getTableColumns(final String tableName) {
		return getTableColumns(tableName, false);
	}
	
	public boolean tableExists(String tableName) {
		return !getTableColumns(tableName).isEmpty();
	}
	
	public void flush() {
		try {
			SQLUtils.flush(conn);
		}
		catch(Exception e) {}
	}
	
	public boolean dropTable(String tableName) {
		try {
			flush();
			if (tableExists(tableName)) {
				SQLUtils.executeQuery(this.conn, "DROP TABLE " + tableName);
				flush();
			}
		} catch (Exception e) {return false;}		
		return true;
	}

	/**
	 * pumps the result of running a query on a database in the enclosed Monet DB
	 * @param srcConn
	 * @param srcQuery
	 * @param destTableName
	 * @throws SQLException
	 */
	public void createTableFromQuery(java.sql.Connection srcConn, String srcQuery, String destTableName) throws SQLException {
		ResultSet rs = SQLUtils.rawRunQuery(srcConn, srcQuery, null);
		if (tableExists(destTableName)) {
			dropTable(destTableName);
		};
		/*else */{
			// create table based on the results structure
			DatabaseTableDescription tableDescription = DatabaseTableDescription.describeResultSet(destTableName, getMapper(), rs);
			tableDescription.create(this.conn, false);
		}
			
		copyEntries(destTableName, rs);
	}
	
	public void copyTableFromPostgres(java.sql.Connection srcConn, String tableName) throws SQLException {
		createTableFromPostgresQuery(tableName, srcConn, "select * from " + tableName);
	}
	
	/**
	 * makes a snapshot of a query, which is copied to MonetDB
	 * @param tableName
	 * @param columnsToIndex columns on which to create indices
	 * @throws SQLException
	 */
	protected void createTableFromPostgresQuery(String tableName, java.sql.Connection srcConn, String tableCreationQuery) throws SQLException {
		dropTable(tableName);
		createTableFromQuery(srcConn, tableCreationQuery, tableName);
	}
	
	/**
	 * copies entries contained in a RS to the Monet DB
	 * @param destTableName
	 * @param rs
	 */
	public void copyEntries(String destTableName, ResultSet rs) throws SQLException {
		List<List<Object>> rows = new ArrayList<>();
		int nrColumns = rs.getMetaData().getColumnCount();
		while (rs.next()) {
			List<Object> line = new ArrayList<>();
			for(int i = 1; i <= nrColumns; i++)
				line.add(rs.getObject(i));
			rows.add(line);
		}
		if (rows.isEmpty())
			return; // nothing to do
		Collection<String> colNames = new ArrayList<String>(this.getTableColumns(destTableName)).subList(0, nrColumns);
		SQLUtils.insert(this.conn, destTableName, null, null, colNames, rows);
	}
	
	public void copyTableStructureFromPostgres(Connection srcConn, String srcTable, String destTable) throws SQLException {
		DatabaseTableDescription tableDescription = DatabaseTableDescription.describeResultSet(destTable, getMapper(), SQLUtils.rawRunQuery(srcConn, "select * from " + srcTable, null));
		dropTable(tableDescription.tableName);
		tableDescription.create(this.conn, false);
	}
	
	public void executeQuery(String query){
		SQLUtils.executeQuery(conn, query);
	}
	
	private static DataSource getMonetDataSource() {
		try {
			Context initialContext = new InitialContext();
			DataSource res = (javax.sql.DataSource) initialContext.lookup(Constants.MONETDB_JNDI_ALIAS);
			if (res == null)
				throw new Error("could not find Monet data source!");
			return res;
		}
		catch(Exception e) {
			throw new Error(e);
		}
	}
	
	public static DbColumnTypesMapper getMapper() {
		return new DbColumnTypesMapper() {
			
			@Override
			public String mapSqlTypeToName(int rsType, int maxWidth) {
				switch(rsType) {
				case java.sql.Types.TINYINT:
				case java.sql.Types.SMALLINT:
				case java.sql.Types.INTEGER:
				case java.sql.Types.BIGINT:
					return "integer";
					
				case java.sql.Types.FLOAT:
				case java.sql.Types.REAL:
				case java.sql.Types.DOUBLE:
				case java.sql.Types.NUMERIC:
				case java.sql.Types.DECIMAL:
					return "double";
					
				case java.sql.Types.CHAR:
				case java.sql.Types.VARCHAR:
					if (maxWidth <= 255)
						return "varchar(255)";
					
				// intentional fall-through
				case java.sql.Types.LONGVARCHAR:
					return "text";
					
				case java.sql.Types.DATE:
				case java.sql.Types.TIME:
				case java.sql.Types.TIMESTAMP:
				case 2013: //java.sql.Types.TIME_WITH_TIMEZONE:
				case 2014: //java.sql.Types.TIMESTAMP_WITH_TIMEZONE:
					return "date";
				
				case java.sql.Types.BIT:
					return "boolean";
					
				default:
					throw new RuntimeException("don't know how to map this column type to MonetDB: " + rsType);
			}
			}
		};
	}
}
