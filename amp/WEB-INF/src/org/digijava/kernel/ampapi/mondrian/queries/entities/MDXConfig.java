/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.olap4j.query.SortOrder;

/**
 * MDX query configuration, that stores information to query like Measures, Attributes, Filters ...
 * @author Nadejda Mandrescu
 *
 */
public class MDXConfig {
	private String cubeName = MoConstants.DEFAULT_CUBE_NAME;
	private String mdxName = MoConstants.DEFAULT_QUERY_NAME; 
	private List<MDXMeasure> columnMeasures = new ArrayList<MDXMeasure>();
	private List<MDXAttribute> columnAttributes = new ArrayList<MDXAttribute>(); 
	private List<MDXAttribute> rowAttributes = new ArrayList<MDXAttribute>();
	private Map<MDXElement, MDXFilter> axisFilters = new HashMap<MDXElement, MDXFilter>();
	private Map<MDXAttribute, MDXFilter> dataFilters = new HashMap<MDXAttribute, MDXFilter>();
	private List<MDXAttribute> singleValueFilters = new ArrayList<MDXAttribute>();
	private LinkedHashMap<MDXElement, SortOrder> sortingOrder = new LinkedHashMap<MDXElement, SortOrder>();
	private boolean allowEmptyData = false;
	private boolean doRowTotals = false;
	private boolean doColumnsTotals = false;
	/**
	 * @return the cubeName
	 */
	public String getCubeName() {
		return cubeName;
	}
	/**
	 * (Optional) Configure cube name. <br>
	 * If not set, {@link MoConstants#DEFAULT_CUBE_NAME} is used 
	 * @param cubeName the cubeName to set
	 */
	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}
	/**
	 * Label for the current MDXConfig for logging/debugging purpose
	 * @return the mdxName - label
	 */
	public String getMdxName() {
		return mdxName;
	}
	/**
	 * (Optional) Label for the current MDXConfig for logging/debugging purpose <br>
	 * If not set, {@link MoConstants#DEFAULT_QUERY_NAME} is used
	 * @param mdxName - label for the current request <br>
	 * E.g. for reports you can use Report Name
	 */
	public void setMdxName(String mdxName) {
		this.mdxName = mdxName;
	}

	/**
	 * @return the columnMeasures
	 */
	public List<MDXMeasure> getColumnMeasures() {
		return columnMeasures;
	}
	/**
	 * List of measures to be displayed on columns
	 * @param columnMeasures the columnMeasures to set
	 */
	public void setColumnMeasures(List<MDXMeasure> columnMeasures) {
		this.columnMeasures = columnMeasures;
	}
	/**
	 * Adds a new Measure to the column list
	 * @param columnMeasure
	 */
	public void addColumnMeasure(MDXMeasure columnMeasure) {
		this.columnMeasures.add(columnMeasure);
	}
	/**
	 * @return the columnAttributes
	 */
	public List<MDXAttribute> getColumnAttributes() {
		return columnAttributes;
	}
	/**
	 * List of attributes to be displayed on columns, as a grouping for measures <br>
	 * Ex. Year column attribute; To have 'Donor Type' list on rows, use {@link #setRowAttributes(List)}
	 * @param columnAttributes the columnAttributes to set
	 */
	public void setColumnAttributes(List<MDXAttribute> columnAttributes) {
		this.columnAttributes = columnAttributes;
	}
	/**
	 * Adds a new Attribute to the column list <br>
	 * Ex. Year column attribute; To have 'Donor Type' list on rows, use {@link #addRowAttribute(MDXAttribute)}
	 * @param columnAttribute - the attribute to add
	 */
	public void addColumnAttribute(MDXAttribute columnAttribute) {
		this.columnAttributes.add(columnAttribute);
	}
	/**
	 * @return the rows
	 */
	public List<MDXAttribute> getRowAttributes() {
		return rowAttributes;
	}
	/**
	 * List of attributes to be displayed on rows, to drill down measures <br>
	 * E.g. Donor Type
	 * @param rows the rows to set
	 */
	public void setRowAttributes(List<MDXAttribute> rowAttributes) {
		this.rowAttributes = rowAttributes;
	}
	/**
	 * Adds an attribute to be displayed on rows, to drill down measures <br>
	 * E.g. Donor Type
	 * @param rowAttribute
	 */
	public void addRowAttribute(MDXAttribute rowAttribute) {
		this.rowAttributes.add(rowAttribute);
	}
	/**
	 * @return the axis filters on displayed values <br>
	 * <b>Note</b>: measures are not filtered by these filters<br>
	 * Use {@link #getDataFilters()} to filter data
	 */
	public Map<MDXElement, MDXFilter> getAxisFilters() {
		return axisFilters;
	}
	/**
	 * Configures axis filters for displayed columns/rows <br>
	 * <b>Note</b>: measures are not filtered by these filters.
	 * Use {@link #getDataFilters()} to filter data <br>
	 * It is similar to 'Date Settings' option from reports
	 * @param filters the filters to set
	 */
	public void setAxisFilters(Map<MDXElement, MDXFilter> axisFilters) {
		this.axisFilters = axisFilters;
	}
	/**
	 * Adds an axis filter for displayed columns/rows<br>
	 * <b>Note</b>: measures are not filtered by these filters.
	 * Use {@link #getDataFilters()} to filter data <br>
	 * It is similar to 'Date Settings' option from reports
	 * @param mdxElement - MDXElement to filter by 
	 * @param filter - the filter to apply
	 */
	public void addAxisFilter(MDXElement mdxElement, MDXFilter filter) {
		this.axisFilters.put(mdxElement, filter);
	}
	/**
	 * @return the dataFilters
	 */
	public Map<MDXAttribute, MDXFilter> getDataFilters() {
		return dataFilters;
	}
	/**
	 * Configures filters for measures data, it affects totals (aka filters from Reports).<br>
	 * Use {@link #setLevelFilters(List)} for single value filters (e.g. 2014 year only)
	 * @param dataFilters the dataFilters to set
	 */
	public void setDataFilters(Map<MDXAttribute, MDXFilter> dataFilters) {
		this.dataFilters = dataFilters;
	}
	/**
	 * Configures filters for measures data, it affects totals (aka filters from Reports).<br>
	 * Use {@link #addLevelFilters(MDXLevel)} for single value filters (e.g. 2014 year only)
	 * @param dataFilters the dataFilters to set
	 */
	public void addDataFilter(MDXAttribute mdxElement, MDXFilter dataFilter) {
		this.dataFilters.put(mdxElement, dataFilter);
	}
	/**
	 * @return the singleValueFilters
	 */
	public List<MDXAttribute> getLevelFilters() {
		return singleValueFilters;
	}
	/**
	 * Single value filter, e.g. to display only results for 2014
	 * @param singleValueFilters the singleValueFilters to set
	 */
	public void setSingleValueFilters(List<MDXAttribute> singleValueFilters) {
		this.singleValueFilters = singleValueFilters;
	}
	/**
	 * Adds a single value filter, e.g. to display only results for 2014
	 * @param singleValueFilters
	 */
	public void addSingleValueFilters(MDXAttribute singleValueFilter) {
		this.singleValueFilters.add(singleValueFilter);
	}
	/**
	 * @return the allowEmptyData
	 */
	public boolean isAllowEmptyData() {
		return allowEmptyData;
	}
	/**
	 * @return the map of MDXElement by sorting preference order and sorting rule
	 * @see SortOrder
	 */
	public LinkedHashMap<MDXElement, SortOrder> getSortingOrder() {
		return sortingOrder;
	}
	/**
	 * Configures sorting rules over ordered list of MDXElement
	 * @param sortingOrder - ordered list of MDXElement as their sorting order and sorting mechanism: <br>
	 * SortOrder.ASC - ascending and keeping the hierarchies <br>
	 * SortOrder.BASC - ascending and <b>breaking</b> hierarchies <br>
	 * SortOrder.DESC - descending and keeping hierarchies <br>
	 * SortOrder.BDESC - descending and breaking hierarchies
	 * @see SortOrder
	 */
	public void setSortingOrder(LinkedHashMap<MDXElement, SortOrder> sortingOrder) {
		this.sortingOrder = sortingOrder;
	}
	/**
	 * Configures if empty data should be returned as an output
	 * @param allowEmptyData - true if empty data should be returned. 
	 * E.g. set to 'true' for Line/Tabular Charts where 0 values should be displayed. 
	 * Set to 'false' to not pollute a report with empty lines, or a PieChart with no data slices 
	 */
	public void setAllowEmptyData(boolean allowEmptyData) {
		this.allowEmptyData = allowEmptyData;
	}
	/**
	 * @return the doRowTotals
	 */
	public boolean isDoRowTotals() {
		return doRowTotals;
	}
	/**
	 * Configures where totals per rows should be counted or not
	 * @param doRowTotals the doRowTotals to set
	 */
	public void setDoRowTotals(boolean doRowTotals) {
		this.doRowTotals = doRowTotals;
	}
	/**
	 * @return the doColumnsTotals
	 */
	public boolean isDoColumnsTotals() {
		return doColumnsTotals;
	}
	/**
	 * @param doColumnsTotals the doColumnsTotals to set
	 */
	public void setDoColumnsTotals(boolean doColumnsTotals) {
		this.doColumnsTotals = doColumnsTotals;
	}
	
}
