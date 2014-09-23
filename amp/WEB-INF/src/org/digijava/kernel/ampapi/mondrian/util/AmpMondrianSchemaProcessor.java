package org.digijava.kernel.ampapi.mondrian.util;

import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Set;

import mondrian.olap.Util.PropertyList;
import mondrian.spi.DynamicSchemaProcessor;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.mondrian.MondrianDBUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpMondrianSchemaProcessor implements DynamicSchemaProcessor {
	
	private static ThreadLocal<ReportSpecification> currentReport = new ThreadLocal<>();
	private static ThreadLocal<ReportEnvironment> currentEnvironment = new ThreadLocal<>();
	
	protected static final Logger logger = Logger.getLogger(AmpMondrianSchemaProcessor.class);

	@Override
	public String processSchema(String schemaURL, PropertyList connectInfo) throws Exception {
		//String rootDir = AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH;
//		String url2 = schemaURL.replaceAll("res:\\.\\.", rootDir + File.separator + "WEB-INF");
//		String url = schemaURL.replaceAll("res:\\.\\.", "WEB-INF");
		String contents = null;
		try(InputStreamReader isr = new InputStreamReader(this.getClass().getResourceAsStream("AMP.xml"), "utf-8")) { 
		//try(InputStreamReader isr = new InputStreamReader(new FileInputStream(url2), "utf-8")) {
			try(Scanner scanner = new Scanner(isr)) {
				contents = scanner.useDelimiter("\\Z").next();
			}}
		if (contents == null)
			throw new RuntimeException("could not read schema");
		return processContents(contents);
	};
	
	public String processContents(String contents) {
		if (currentReport.get() == null || currentEnvironment.get() == null) {
			logger.error("currentReport || currentEnvironment == null -> not processing schema!");
			return contents;
		}
		contents = contents.replaceAll("@@actual@@", Long.toString(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase()));
		contents = contents.replaceAll("@@planned@@", Long.toString(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getIdInDatabase()));
		contents = contents.replaceAll("@@currency@@", Long.toString(getReportCurrency().getAmpCurrencyId()));
		contents = contents.replaceAll("@@filteredActivities@@", getAllowedActivitiesIds());
		String locale = getReportLocale();
		contents = contents.replaceAll("@@locale@@", locale);
		int pos = contents.indexOf("@@");
		if (pos >= 0)
			throw new RuntimeException("your schema contains unrecognized tag: " + contents.substring(pos, contents.indexOf("@@", pos + 2) + 2));
		return contents;
	}
	
	protected String getReportLocale() {
		return "_" + currentEnvironment.get().locale;
	}
	
	protected AmpCurrency getReportCurrency() {
		AmpCurrency res = CurrencyUtil.getCurrencyByCode(currentReport.get().getSettings().getCurrencyCode());
		return res;
	}
	
	protected String getAllowedActivitiesIds() {
		TeamMember tm = currentEnvironment.get().viewer;
		Set<Long> allowedActivities = ActivityUtil.getAllAmpActivityIds(WorkspaceFilter.generateWorkspaceFilterQuery(tm));
		if (allowedActivities.size() > 0) {
			String dateFilters = MondrianDBUtils.generateDateColumnsFilterQuery(
					allowedActivities,
					((MondrianReportFilters)currentReport.get().getFilters()).getDateFilterRules());
			if (dateFilters != null) {
				allowedActivities = ActivityUtil.getAllAmpActivityIds(dateFilters);
			}
		}
		return Util.toCSStringForIN(allowedActivities);
	}
	
	/**
	 * this should be called before each and every report run using Mondrian
	 * @param spec
	 */
	public static void registerReport(ReportSpecification spec, ReportEnvironment environment) {
		currentReport.set(spec);
		currentEnvironment.set(environment);
	}
}
