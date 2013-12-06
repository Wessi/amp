package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.SQLException;
import java.util.*;

/**
 * these are indexed in the map by their hashCode() / equals() functions, so be sure to make those meaningful
 * @author Dolghier Constantin
 *
 */
public interface PropertyDescription 
{
	/**
	 * false - this is a regular translated column. true - this is a row-by-row calculated column, it shouldn't be cached and can't be "generateValues()"'ed
	 * @return
	 */
	public boolean isCalculated();
	
	/**
	 * will only be called for non-calculated
	 * @param connection
	 * @param ids
	 * @param locale
	 * @return
	 * @throws SQLException
	 */
	public Map<Long, String> generateValues(java.sql.Connection connection, Collection<Long> ids, String locale) throws SQLException;
	
	/**
	 * will only be called for calculated
	 * @param currentLine
	 * @return
	 * @throws SQLException
	 */
	public String getValueFor(java.sql.ResultSet currentLine) throws SQLException;
	
	/**
	 * nice, human readable, description, for debugging
	 * @return
	 */
	public String getNiceDescription();
}
