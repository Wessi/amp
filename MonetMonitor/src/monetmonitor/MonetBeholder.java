package monetmonitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;



/**
 * 
 * @author acartaleanu
 * class for the purpose of connecting to the Monet server via JDBC
 *
 */
public class MonetBeholder{

	
	
	public MonetBeholder() {
		try {
			Class.forName("nl.cwi.monetdb.jdbc.MonetDriver");
		} catch(Exception exc) {
			
		}

	}
	void runSelect(Connection conn, String query) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		try(ResultSet rs = ps.executeQuery()) {
			int nrColumns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				List<Object> res = new ArrayList<>();
				for(int i = 0; i < nrColumns; i++)
					res.add(rs.getObject(i + 1));
//				if (res.size() > 100)
//					System.out.println("should not happen");
			}
			rs.close();
			ps.close();
			conn.close();
			
		}
	}
	
	void work() throws SQLException {
//		String query = "select count(*) from (select distinct \"amp_org_id\" from \"mondrian_organizations_en\") as \"init\"";
		String query = "select tables.name from tables";

		
		String url = "jdbc:monetdb://localhost/"+ Constants.getDbName();
		runSelect(DriverManager.getConnection(url, "monetdb", "monetdb"), query);
//		DriverManager.getConnection(url).close();
	}
	
	/**
	 * 
	 * @return true if server is responding, false if it isn't
	 * @throws ClassNotFoundException 
	 */	
	public boolean checkMonetServerRunning() {
		try {
			work();
			return true;
		} catch (java.sql.SQLException exc) {
			exc.printStackTrace();
		}
		return false;
	}
}
