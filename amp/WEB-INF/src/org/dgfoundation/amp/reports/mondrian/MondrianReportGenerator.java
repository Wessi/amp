/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.mondrian.MondrianETL;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.FilterRule.FilterType;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.newreports.TextCell;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.queries.MDXGenerator;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXConfig;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXElement;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXFilter;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXMeasure;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXTuple;
import org.digijava.kernel.ampapi.mondrian.util.AmpMondrianSchemaProcessor;
import org.digijava.kernel.ampapi.mondrian.util.Connection;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.mondrian.util.MondrianMapping;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.digijava.kernel.ampapi.saiku.SaikuGeneratedReport;
import org.digijava.kernel.ampapi.saiku.SaikuReportArea;
import org.digijava.kernel.ampapi.saiku.SaikuReportSorter;
import org.digijava.kernel.ampapi.saiku.util.CellDataSetToAmpHierachies;
import org.digijava.kernel.ampapi.saiku.util.CellDataSetToGeneratedReport;
import org.digijava.kernel.ampapi.saiku.util.SaikuPrintUtils;
import org.digijava.kernel.ampapi.saiku.util.SaikuUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.calendar.util.CalendarUtil;
import org.olap4j.Axis;
import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.OlapException;
import org.olap4j.Position;
import org.olap4j.metadata.Member;
import org.olap4j.query.SortOrder;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.olap.util.OlapResultSetUtil;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

/**
 * Generates a report via Mondrian
 * @author Nadejda Mandrescu
 *
 */
public class MondrianReportGenerator implements ReportExecutor {
	protected static final Logger logger = Logger.getLogger(MondrianReportGenerator.class);
	
	//TODO: set to false
	//e.g. skips to throw exceptions until schema def is complete and all mappings are configured based on it
	private static final boolean IS_DEV = true;
	private final Class<? extends ReportAreaImpl> reportAreaType;
	private final boolean printMode;
	
	private MDXGenerator generator = null;
	
	private List<ReportOutputColumn> leafHeaders = null; //leaf report columns list
	
	private final ReportEnvironment environment;
	
	/**
	 * Mondrian Report Generator
	 * @param reportAreaType - report area type to be used for output generation.
	 * @param printMode - if set to true, then Olap4J CellSet will be printed to the standard output
	 */
	public MondrianReportGenerator(Class<? extends ReportAreaImpl> reportAreaType, ReportEnvironment environment, boolean printMode) {
		this.reportAreaType = reportAreaType;
		this.environment = environment;
		this.printMode = printMode; 
	}
	
	/**
	 * Mondrian Report Generator
	 * @param reportAreaType - report area type to be used for output generation.
	 */
	public MondrianReportGenerator(Class<? extends ReportAreaImpl> reportAreaType, ReportEnvironment environment) {
		this (reportAreaType, environment, false);
	}
	
	@Override
	public GeneratedReport executeReport(ReportSpecification spec) throws AMPException {
		try {
			stats = new ReportGenerationStats();
			CellDataSet cellDataSet = generateReportAsSaikuCellDataSet(spec);
			long postprocStart = System.currentTimeMillis();
			
			logger.info("[" + spec.getReportName() + "]" +  "Converting CellDataSet to GeneratedReport...");
			GeneratedReport report = toGeneratedReport(spec, cellDataSet, cellDataSet.runtime);
			logger.info("[" + spec.getReportName() + "]" +  "CellDataSet converted to GeneratedReport.");
			logger.info("[" + spec.getReportName() + "]" +  "Sorting report...");
			if (SaikuReportArea.class.isAssignableFrom(reportAreaType)) {
				report = new SaikuGeneratedReport(
						spec, report.generationTime, report.requestingUser,
						(SaikuReportArea)report.reportContents, cellDataSet, report.rootHeaders, report.leafHeaders, environment);
				SaikuReportSorter.sort(report, environment);
				if (printMode)
					SaikuPrintUtils.print(cellDataSet, spec.getReportName() + "_POST_SORT");
			} else 
				MondrianReportSorter.sort(report, environment);
			logger.info("[" + spec.getReportName() + "]" +  "Report sorted.");
			stats.postproc_time = System.currentTimeMillis() - postprocStart;
			stats.total_time += stats.postproc_time;
			return report;
		}
		catch(Exception e) {
			stats.crashed = true;
			throw e;
		}
		finally {
			writeStats();
			tearDown();
		}
	}
	
	void writeStats() {
		if (stats != null) {
			PersistenceManager.getSession().createSQLQuery(
					String.format("INSERT INTO amp_reports_runtime_log (lock_wait_time, mdx_time, total_time, mdx_query, width, height, postproc_time, crashed) VALUES (%d, %d, %d, %s, %d, %d, %d, %s)",
							stats.lock_wait_time, stats.mdx_time, stats.total_time, SQLUtils.stringifyObject(stats.mdx_query),
							stats.width, 
							stats.height,
							stats.postproc_time,
							stats.crashed
							)).executeUpdate();
		}
	}
	
	ReportGenerationStats stats;
	
	/**
	 * Generates a report as a Saiku {@link CellDataSet}, without any translation into our Reports API structures
	 * also sets {@link #stats} to a newly-created instance
	 * @param spec - {@link ReportSpecification}
	 * @return {@link CellDataSet}
	 * @throws AMPException
	 */
	private CellDataSet generateReportAsSaikuCellDataSet(final ReportSpecification spec) throws AMPException {
		init(spec);
		AmpMondrianSchemaProcessor.registerReport(spec, environment);
		CellDataSet cellDataSet;
		ValueWrapper<Boolean> forcedOut = new ValueWrapper<Boolean>(false);
		stats.lock_wait_time = MondrianETL.FULL_ETL_LOCK.readLockWithTimeout(7000, forcedOut);

		try {
			long startTime = System.currentTimeMillis();
			//while (System.currentTimeMillis() < startTime + 15000) {};
			//try {Thread.sleep(60000);}catch(Exception e){}
			CellSet cellSet = null;
			String mdxQuery = getMDXQuery(spec);
			stats.mdx_query = mdxQuery;

			if (printMode) System.out.println("[" + spec.getReportName() + "] MDX query: " + mdxQuery);
		
			try {
				cellSet = generator.runQuery(mdxQuery);
			} catch (Exception e) {
				tearDown();
				stats.crashed = true;
				throw new AMPException("Cannot generate Mondrian Report '" + spec.getReportName() +"' : " 
						+ e.getMessage() == null ? e.getClass().getName() : e.getMessage());
			}
		
			stats.mdx_time = System.currentTimeMillis() - startTime;
			if (printMode)
				System.out.println("[" + spec.getReportName() + "] MDX query run time: " + stats.mdx_time);
			else
				logger.info("[" + spec.getReportName() + "] MDX query run time: " + stats.mdx_time);
		
			cellDataSet = postProcess(spec, cellSet);
			stats.total_time = System.currentTimeMillis() - startTime;
		
			cellDataSet.setRuntime((int) stats.total_time);
			logger.info("CellDataSet for '" + spec.getReportName() + "' report generated within: " + stats.total_time + "ms");
					
			stats.width = cellDataSet == null ? 0 : cellDataSet.getWidth();
			stats.height = cellDataSet == null ? 0 : cellDataSet.getHeight();
			
			if (printMode) {
				if (cellSet != null)
					MondrianUtils.print(cellSet, spec.getReportName());
				if (cellDataSet != null)
					SaikuPrintUtils.print(cellDataSet, spec.getReportName() + "_POST");
			}
		}
		finally {
			MondrianETL.FULL_ETL_LOCK.unlockIfStillUsed(forcedOut);
		}
		return cellDataSet;
	}
	
	private void init(ReportSpecification spec) {
		if (!Connection.IS_TESTING)
			if(MondrianETL.runETL(false).cacheInvalidated) {
				MondrianReportUtils.flushCache();
			}
		
		MondrianReportUtils.configureDefaults(spec);
		addDummyHierarchy(spec);
	}
	
	/**
	 * Generates MDX Query string that can be passed to Saiku or any other MDX processor
	 * @param spec - {@link ReportSpecification}
	 * @return mdx string
	 * @throws AMPException
	 */
	public String getMDXQuery(ReportSpecification spec) throws AMPException {
		MDXConfig config = toMDXConfig(spec);
		try {
			generator = new MDXGenerator();
			return generator.getAdvancedOlapQuery(config);
		} catch (AmpApiException e) {
			tearDown();
			throw new RuntimeException("Cannot generate Mondrian Report: ", e);
		} 
	}
	
	/**
	 * Releases the resources
	 */
	public void tearDown() {
		if (generator != null) 
			generator.tearDown();
	}
	
	/**
	 *  Adds a dummy hierarchy by internal id (which is entity id) to group by non-hierarchical columns,
	 *  but only if there are non-hierarchical columns
	 */
	private void addDummyHierarchy(ReportSpecification spec) {
		//if we have more columns than hierarchies, then add the dummy hierarchy to group non-hierarchical columns by it
		if (spec.getHierarchies().size() < spec.getColumns().size()) {
			ReportColumn internalId = MondrianReportUtils.getColumn(ColumnConstants.INTERNAL_USE_ID, ReportEntityType.ENTITY_TYPE_ALL);
			Set<ReportColumn> newColumns = new LinkedHashSet<ReportColumn>(spec.getColumns().size() + 1);
			int pos = spec.getHierarchies().size();
			for(Iterator<ReportColumn> iter = spec.getColumns().iterator(); iter.hasNext(); pos--) {
				//check if we skiped all hierarchies 
				if (pos == 0)
					newColumns.add(internalId);
				else 
					newColumns.add(iter.next());
			}
			spec.getColumns().clear();
			spec.getColumns().addAll(newColumns);
			spec.getHierarchies().add(internalId);
		}
	}
	
	private MDXConfig toMDXConfig(ReportSpecification spec) throws AMPException {
		MDXConfig config = new MDXConfig();
		config.setCubeName(MoConstants.DEFAULT_CUBE_NAME);
		config.setMdxName(spec.getReportName());
		boolean doHierarchiesTotals = false;//we are moving totals calculation out of MDX. spec.getHierarchies() != null && spec.getHierarchies().size() > 0;
		//totals to be done post generation, because in MDX it take too long
		config.setDoColumnsTotals(false);//we are moving totals calculation out of MDX. columns totals in MDX are equivalent to what we perceive as row totals in standard report
		config.setDoRowTotals(false); //we are moving totals calculation out of MDX. row totals in MDX are equivalent to what we perceive as column totals, e.g. this is for Total Actual Commitments
		config.setColumnsHierarchiesTotals(0); //we are moving subtotals out of MDX.
		config.setRowsHierarchiesTotals(0); //we are moving subtotals out of MDX.
		//add requested columns
		if (!spec.isSummaryReport())
			for (ReportColumn col:spec.getColumns()) {
				MDXAttribute elem = (MDXAttribute)MondrianMapping.toMDXElement(col);
				if (elem == null) 
					reportError("No mapping found for column name = " + (col==null ? null : col.getColumnName()) + ", entity type = " + (col == null ? null : col.getEntityType()));
				else 
					config.addRowAttribute(elem);
			}
		//add requested measures
		for (ReportMeasure measure: spec.getMeasures()) {
			MDXMeasure elem = (MDXMeasure)MondrianMapping.toMDXElement(measure);
			if (elem == null) 
				reportError("No mapping found for column name = " + (measure==null ? null : measure.getMeasureName()) + ", entity type = " + (measure == null ? null : measure.getEntityName()));
			else
				config.addColumnMeasure(elem);
		}
		//add grouping columns for measure
		config.getColumnAttributes().addAll(MondrianMapping.getDateElements(spec.getGroupingCriteria(), spec.getSettings().getCalendar()));
		//add sorting
		configureSortingRules(config, spec, doHierarchiesTotals);
		
		//add filters
		addFilters(spec.getFilters(), config);
		
		//add settings
		addSettings(spec.getSettings(), config);
		
		config.setAllowEmptyColumnsData(spec.isDisplayEmptyFundingColumns());
		config.setAllowEmptyRowsData(spec.isDisplayEmptyFundingRows());
		
		return config;
	}
	
	private void configureSortingRules(MDXConfig config, ReportSpecification spec, boolean doHierarchiesTotals) throws AMPException {
		if (spec.getSorters() == null || spec.getSorters().size() == 0 ) return;
		boolean nonBreakingSort = spec.isCalculateRowTotals() || doHierarchiesTotals;
		
		for (SortingInfo sortInfo : spec.getSorters()) {
			MDXTuple tuple = new MDXTuple();
			if (!sortInfo.isTotals) {//totals sorting will be done during post-processing
				for (Entry<ReportElement, FilterRule> entry : sortInfo.sortByTuple.entrySet()) {
					if (ElementType.ENTITY.equals(entry.getKey().type))
						tuple.add(applySingleFilter(MondrianMapping.toMDXElement(entry.getKey().entity), entry.getValue()));
					else { 
						MDXAttribute mdxAttr = MondrianMapping.getElementByType(entry.getKey().type);
						if (mdxAttr == null) {
							String err = "No mapping found for Element type = " + entry.getKey().type;
							logger.error(err);
							throw new AMPException(err);
						}
						tuple.add(applySingleFilter(mdxAttr, entry.getValue()));
					}	
				}
				config.getSortingOrder().put(tuple, sortInfo.ascending ? 
													(nonBreakingSort ? SortOrder.ASC : SortOrder.BASC) : 
													(nonBreakingSort ? SortOrder.DESC : SortOrder.BDESC));
			}
		}
	}
	
	private MDXElement applySingleFilter(MDXElement elem, FilterRule filter) throws AMPException {
		if (filter == null) return elem;
		if (!FilterType.SINGLE_VALUE.equals(filter.filterType))
			throw new AMPException("Sorting filter must be a single value filter");
		if (elem instanceof MDXMeasure)
			throw new AMPException("Single value sorting filter cannot be applied over measures");
		((MDXAttribute)elem).setValue(filter.value);
		return elem;
	}
	
	private void addFilters(ReportFilters reportFilter, MDXConfig config) throws AmpApiException {
		if (reportFilter == null) return;
		for(Entry<ReportElement, List<FilterRule>> entry : reportFilter.getFilterRules().entrySet()) {
			ReportElement elem = entry.getKey();
			MDXAttribute mdxElem = null;
			
			switch (elem.type) {
			case ENTITY : 
				mdxElem = (MDXAttribute)MondrianMapping.toMDXElement(elem.entity);
			break;
			default: mdxElem = MondrianMapping.getElementByType(elem.type);
			break; 
			}
			if (mdxElem == null) {
				reportError("Mapping not defined for report element = " + elem);
				if (IS_DEV) continue;
			}
			
			for (FilterRule filter : entry.getValue()) {
				MDXFilter mdxFilter = null;
				
				switch(filter.filterType) {
				case RANGE: mdxFilter = new MDXFilter(filter.min, filter.minInclusive, filter.max, filter.maxInclusive, filter.isIdFilter); 
				break;
				case SINGLE_VALUE: mdxFilter = new MDXFilter(filter.value, filter.valuesInclusive, filter.isIdFilter);
				break;
				case VALUES: mdxFilter = new MDXFilter(filter.values, filter.valuesInclusive, filter.isIdFilter);
				break;
				}
				
				config.addDataFilter(mdxElem, mdxFilter);
			}
		}
	}
	
	private void addSettings(ReportSettings reportSettings, MDXConfig config) {
		if (reportSettings == null) return;
		/* unfortunately the formatting works a bit differently in Mondrian, e.g.:
		 * 1) original value '19,795,441,979.544' => MDX pattern '# ##0,##' => displayed value '1979544 1,979,544'
		 * 2) original value '162,330' => MDX pattern '#,##0.##' => displayed value '162,330.' (with dot at the end)
		 * so we'll do this as well manually during post process...  
		if (reportSettings.getCurrencyFormat() != null)
			config.setAmountsFormat(reportSettings.getCurrencyFormat().toPattern());
		*/
	}
	
	private CellDataSet postProcess(ReportSpecification spec, CellSet cellSet) throws AMPException {
		CellSetAxis rowAxis = cellSet.getAxes().get(Axis.ROWS.axisOrdinal());
		CellSetAxis columnAxis = cellSet.getAxes().get(Axis.COLUMNS.axisOrdinal());
		
		if (rowAxis.getPositionCount() > 0 && columnAxis.getPositionCount() > 0 ) {
			leafHeaders = getOrderedLeafColumnsList(spec, rowAxis, columnAxis);
		} else 
			leafHeaders = null;
		
		logger.info("[" + spec.getReportName() + "]" +  "Starting conversion from Olap4J CellSet to Saiku CellDataSet via Saiku method...");
		CellDataSet cellDataSet = OlapResultSetUtil.cellSet2Matrix(cellSet); // we can also pass a formater to cellSet2Matrix(cellSet, formatter)
		logger.info("[" + spec.getReportName() + "]" +  "Conversion from Olap4J CellSet to Saiku CellDataSet ended.");
		
		if (spec.isCalculateColumnTotals() || spec.isCalculateRowTotals()
				//enable totals for non-hierarhical columns
				|| spec.getHierarchies().size() < spec.getColumns().size()) {
			try {
				logger.info("[" + spec.getReportName() + "]" +  "Starting totals calculation over the Saiku CellDataSet via Saiku method...");
				SaikuUtils.doTotals(spec, cellDataSet, cellSet);
				logger.info("[" + spec.getReportName() + "]" +  "Totals over the Saiku CellDataSet ended.");
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new AMPException(e.getMessage());
			}
		}
		
		applyFilterSetting(spec, cellDataSet);
		
		CellDataSetToAmpHierachies.concatenateNonHierarchicalColumns(spec, cellDataSet, leafHeaders);
		
		//clear totals if were enabled for non-hierarchical merges
		if (!spec.isCalculateColumnTotals())
			cellDataSet.setColTotalsLists(null);
		if (!spec.isCalculateRowTotals())
			cellDataSet.setRowTotalsLists(null);
		
		return cellDataSet;
	}
	
	private void applyFilterSetting(ReportSpecification spec, CellDataSet cellDataSet) throws AMPException {
		if (spec.getSettings() == null || spec.getSettings().getFilterRules() == null) return;
		for (Entry<ReportElement, List<FilterRule>> pair : spec.getSettings().getFilterRules().entrySet()) {
			switch(pair.getKey().type) {
			case YEAR: 
				if (!GroupingCriteria.GROUPING_TOTALS_ONLY.equals(spec.getGroupingCriteria()))
					applyYearRangeSetting(spec, pair.getValue(), cellDataSet);
				break;
			default: throw new AMPException("Not supported: settings behavior over " + pair.getKey().type);
			}
		}
	}
	
	private void applyYearRangeSetting(ReportSpecification spec, List<FilterRule> filters, CellDataSet cellDataSet) throws AMPException {
		if (leafHeaders == null || leafHeaders.size() == 0) return;

		//detect the level where years are displayed
		int level = 0; //this is measures level
		switch(spec.getGroupingCriteria()) {
		case GROUPING_YEARLY : level = 1; break;
		case GROUPING_QUARTERLY : level = 2; break; //i.e. level 0 is for measures, level 1 is for quarters and level 2 is for years
		case GROUPING_MONTHLY : level = 3; break;
		case GROUPING_TOTALS_ONLY : return;  
		}

		List<Integer[]> yearRanges = new ArrayList<Integer[]>();
		Set<Integer> yearSet = new TreeSet<Integer>();
		getYearSettings(filters, yearRanges, yearSet);
		
		Iterator<ReportOutputColumn> iter = leafHeaders.iterator();
		int pos = spec.getColumns().size(); //initial position in the list
		//skip report columns unrelated to years
		while(pos > 0 && iter.hasNext()) {
			iter.next();
			pos--;
		}
		
		//detect the columns that are not in the years ranges or years set
		SortedSet<Integer> leafColumnsNumberToRemove = new TreeSet<Integer>();
		pos = spec.getColumns().size(); //re-init the starting position
		int end = leafHeaders.size() - spec.getMeasures().size();
		while(iter.hasNext() && pos < end) {
			ReportOutputColumn column = iter.next();
			//move to year level header
			int currentLevel = level;
			while(currentLevel > 0 && column!= null) {
				column = column.parentColumn;
				currentLevel--; 
			}
			
			if (column != null) { //must not be null actually
				int year = CalendarUtil.parseYear(spec.getSettings().getCalendar(), column.columnName); 
				boolean isAllowed = yearSet.contains(year); //first check if it is in the set
				if (!isAllowed)
					for (Integer[] range : yearRanges)
						if (range[0] <= year && year <= range[1]) { //check if the year is within any [min, max] allowed range from the list of configured ranges
							isAllowed = true;
							break;
						}
				
				if (!isAllowed) {
					leafColumnsNumberToRemove.add(pos);
					iter.remove();//remove also the leaf header
				}
			}
			pos ++;
		}
		
		cellDataSet.setCellSetHeaders(removeYearsToHideCells(cellDataSet.getCellSetHeaders(), leafColumnsNumberToRemove));
		cellDataSet.setCellSetBody(removeYearsToHideCells(cellDataSet.getCellSetBody(), leafColumnsNumberToRemove));
		removeYearsToHideCells(cellDataSet, leafColumnsNumberToRemove);
	}
	
	private AbstractBaseCell[][] removeYearsToHideCells(AbstractBaseCell[][] cellSetHeaders, SortedSet<Integer> leafColumnsNumberToRemove) {
		if (cellSetHeaders.length == 0) return cellSetHeaders; //not the case, but.. 
		AbstractBaseCell[][] newCellSetHeaders = new AbstractBaseCell[cellSetHeaders.length][cellSetHeaders[0].length - leafColumnsNumberToRemove.size()];
		for (int i = 0; i < cellSetHeaders.length; i++) {
			int newJ = 0;
			for (int j = 0; j< cellSetHeaders[i].length; j++) {
				if (!leafColumnsNumberToRemove.contains(j)) {
					newCellSetHeaders[i][newJ++] = cellSetHeaders[i][j];  
				}
			}	
		}
		return newCellSetHeaders;
	}
	
	private void removeYearsToHideCells(CellDataSet cellDataSet, SortedSet<Integer> leafColumnsNumberToRemove) {
		//navigate through the totals list and remember the totals only for the columns we display 
		for(List<TotalNode> totalLists : cellDataSet.getRowTotalsLists()) {
			for(TotalNode totalNode : totalLists) {
				if (totalNode.getTotalGroups() != null && totalNode.getTotalGroups().length > 0) {
					int offset = cellDataSet.getLeftOffset();
					for (int i = 0; i < totalNode.getTotalGroups().length; i++) {
						int newJ  = 0; 
						TotalAggregator[] newLine = new TotalAggregator[totalNode.getTotalGroups()[i].length - leafColumnsNumberToRemove.size()];
						for (int j = 0; j< totalNode.getTotalGroups()[i].length; j++) {
							if ( !leafColumnsNumberToRemove.contains(j+offset) ) {
								newLine[newJ ++ ] = totalNode.getTotalGroups()[i][j];
							} 
						}
						totalNode.getTotalGroups()[i] = newLine; 
					}
				}
			}
		}
	}
	
	private void getYearSettings(List<FilterRule> filters, List<Integer[]> yearRanges, Set<Integer> yearSet) {
		//build the list of ranges and selective set of years
		for(FilterRule rule : filters) {
			switch(rule.filterType) {
			case RANGE :
				Integer min = rule.min == null ? Integer.MIN_VALUE : Integer.parseInt(rule.min);
				Integer max = rule.max == null ? Integer.MAX_VALUE : Integer.parseInt(rule.max);
				yearRanges.add(new Integer[]{min, max});
				break;
			case SINGLE_VALUE : 
				yearSet.add(Integer.parseInt(rule.value)); 
				break;
			case VALUES : 
				for (String value : rule.values) {
					yearSet.add(Integer.parseInt(value));
				}
				break;
			}
		} 
	}
	
	private GeneratedReport toGeneratedReport(ReportSpecification spec, CellDataSet cellDataSet, int duration) throws AMPException {
		long start = System.currentTimeMillis();
		CellDataSetToGeneratedReport translator = new CellDataSetToGeneratedReport(spec, cellDataSet, leafHeaders);
		ReportAreaImpl root = translator.transformTo(reportAreaType);
		GeneratedReport genRep = new GeneratedReport(spec, duration + (int)(System.currentTimeMillis() - start), null, root, getRootHeaders(leafHeaders), leafHeaders); 
		return genRep;
	}
	
	@Deprecated
	private GeneratedReport toGeneratedReport(ReportSpecification spec, CellSet cellSet, int duration) throws AMPException {
		CellSetAxis rowAxis = cellSet.getAxes().get(Axis.ROWS.axisOrdinal());
		CellSetAxis columnAxis = cellSet.getAxes().get(Axis.COLUMNS.axisOrdinal());
		ReportAreaImpl root = MondrianReportUtils.getNewReportArea(reportAreaType);
		
		if (rowAxis.getPositionCount() > 0 && columnAxis.getPositionCount() > 0 ) {
			/* Build Report Areas */
			// stack of current group of children
			Deque<List<ReportArea>> stack = new ArrayDeque<List<ReportArea>>();
			int maxStackSize = 1 + (spec.isCalculateRowTotals() ? rowAxis.getAxisMetaData().getHierarchies().size()  : 0); 
			refillStack(stack, maxStackSize); //prepare the stack
			
			int cellOrdinal = 0; //initial position of row data from the cellSet
			boolean wasAreaEnd = false; 
			
			for (Position rowPos : rowAxis.getPositions()) {
				int columnPos = 0;
				boolean areaEnd = false; 
				ReportAreaImpl reportArea = MondrianReportUtils.getNewReportArea(reportAreaType);
				Map<ReportOutputColumn, ReportCell> contents = new LinkedHashMap<ReportOutputColumn, ReportCell>();
				
				for (Member member : rowPos.getMembers()) {
					TextCell text = new TextCell(member.getName());
					contents.put(leafHeaders.get(columnPos++), text);
					areaEnd = areaEnd || member.isAll();
				}
				
				for (Position colPos : columnAxis.getPositions()) {
					Cell cell = cellSet.getCell(cellOrdinal++);
					if (cell.getValue() instanceof OlapException) {
						logger.error("Unexpected cell error: " + MondrianUtils.getOlapExceptionMessage((OlapException)cell.getValue()));
					} else {
						if (cell.getValue() != null) {
							AmountCell amount = new AmountCell(new BigDecimal(cell.getValue().toString()), null);
							contents.put(leafHeaders.get(columnPos++), amount);
						} else 
							columnPos++; //skip column
					} 
				}
				
				reportArea.setContents(contents);
				
				if (areaEnd) {
					reportArea.setChildren(stack.pop());
				} else if (wasAreaEnd) { 
					//was an area end but now is simple content => refill with new children lists to the stack 
					refillStack(stack, maxStackSize);
				}
				stack.peek().add(reportArea);
				wasAreaEnd = areaEnd;
			}
			root.setChildren(stack.pop());
		}
			
		//we should have requesting user already configure in spec - spec must have all required data
		GeneratedReport genRep = new GeneratedReport(spec, duration, null, root, getRootHeaders(leafHeaders), leafHeaders); 
		return genRep;
	}
	
	private List<ReportOutputColumn> getOrderedLeafColumnsList(ReportSpecification spec, CellSetAxis rowAxis, CellSetAxis columnAxis) {
		//<fully qualified column name, ReportOutputColumn instance>, where fully qualified means with all parents name: /root/root-child/root-grandchild/...
		Map<String, ReportOutputColumn> reportColumnsByFullName = new LinkedHashMap<String,ReportOutputColumn>();
		List<ReportOutputColumn> reportColumns = new ArrayList<ReportOutputColumn>(); //leaf report columns list

		//build the list of available columns
		for (Member textColumn : rowAxis.getPositions().get(0).getMembers()) {
			ReportOutputColumn reportColumn = new ReportOutputColumn(textColumn.getLevel().getCaption(), null, textColumn.getLevel().getName());
			reportColumns.add(reportColumn);
		}
		//int measuresLeafPos = columnAxis.getAxisMetaData().getHierarchies().size();
		for (Position position : columnAxis.getPositions()) {
			String fullColumnName = "";
			for (Member measureColumn : position.getMembers()) {
				ReportOutputColumn parent = reportColumnsByFullName.get(fullColumnName);
				fullColumnName += "/" +  measureColumn.getName();
				ReportOutputColumn reportColumn = reportColumnsByFullName.get(fullColumnName);
				if (reportColumn == null) {
					reportColumn = new ReportOutputColumn(measureColumn.getCaption(), parent, measureColumn.getName());
					reportColumnsByFullName.put(fullColumnName, reportColumn);
				}
				if (measureColumn.getDepth() == 0) { //lowest depth ==0 => this is leaf column
					reportColumns.add(reportColumn);
				}
			}
		}
		//add measures total columns
		if (spec.isCalculateColumnTotals() && !GroupingCriteria.GROUPING_TOTALS_ONLY.equals(spec.getGroupingCriteria())) {
			ReportOutputColumn totalMeasuresColumn = ReportOutputColumn.buildTranslated(MoConstants.TOTAL_MEASURES, environment.locale, null);
			for (ReportMeasure measure : spec.getMeasures())
				reportColumns.add(ReportOutputColumn.buildTranslated(measure.getMeasureName(), environment.locale, totalMeasuresColumn));
		}
		return reportColumns;
	}
	
	private void refillStack(Deque<List<ReportArea>> stack, int maxSize) {
		for (int i = stack.size(); i < maxSize; i++) {
			stack.push(new ArrayList<ReportArea>()); 
		}
	}
	
	private List<ReportOutputColumn> getRootHeaders(List<ReportOutputColumn> leafHeaders) {
		if (leafHeaders ==null || leafHeaders.size() == 0) return null;
		Set<ReportOutputColumn> rootHeaders = new LinkedHashSet<ReportOutputColumn>();
		for (ReportOutputColumn leaf : leafHeaders) {
			while(leaf.parentColumn != null) {
				leaf = leaf.parentColumn;
			}
			rootHeaders.add(leaf);
		}
		return Arrays.asList(rootHeaders.toArray(new ReportOutputColumn[1]));
	}
	
	private void reportError(String error) throws AmpApiException {
		if (IS_DEV)
			logger.error(error);
		else
			throw new AmpApiException(error);
	}
}

	