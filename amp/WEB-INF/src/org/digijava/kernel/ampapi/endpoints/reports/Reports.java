package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JSONResult;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.ReportMetadata;
import org.digijava.kernel.ampapi.saiku.SaikuGeneratedReport;
import org.digijava.kernel.ampapi.saiku.SaikuReportArea;
import org.digijava.kernel.ampapi.saiku.util.SaikuUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.action.ReportsFilterPicker;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.olap.query2.ThinHierarchy;
import org.saiku.olap.query2.ThinQuery;
import org.saiku.service.util.export.excel.ExcelBuilderOptions;
import org.saiku.service.util.export.excel.ExcelWorksheetBuilder;
import org.saiku.web.export.PdfReport;
import org.saiku.web.rest.objects.resultset.QueryResult;
import org.saiku.web.rest.util.RestUtil;

/***
 * 
 * @author
 * 
 */

@Path("data")
public class Reports {
	
	private static final String DEFAULT_CATALOG_NAME = "AMP";
	private static final String DEFAULT_CUBE_NAME = "Donor Funding";
	private static final String DEFAULT_UNIQUE_NAME = "[amp].[AMP].[AMP].[Donor Funding]";
	private static final String DEFAULT_QUERY_NAME = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
	private static final String DEFAULT_CONNECTION_NAME = "amp";
	private static final String DEFAULT_SCHEMA_NAME = "AMP";

	protected static final Logger logger = Logger.getLogger(Reports.class);

	@Context
	private HttpServletRequest httpRequest;

	@GET
	@Path("/report/{report_id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final JSONResult getReport(@PathParam("report_id") Long reportId) {
		AmpReports ampReport = DbUtil.getAmpReport(reportId);

		// TODO: for now we do not translate other types of reports than Donor
		// Type reports (hide icons for non-donor-type reports?)
		ReportSpecificationImpl spec = null;
		try {
			spec = AmpReportsToReportSpecification.convert(ampReport);
		} catch (AMPException e1) {
			JSONResult result = new JSONResult();
			result.setErrorMessage(e1.getMessage());
			return result;
		}

		JSONResult result = new JSONResult();
		ReportMetadata metadata = new ReportMetadata();
		metadata.setReportSpec(spec);
		metadata.setSettings(EndpointUtils.getReportSettings(spec));
		metadata.setCatalog(DEFAULT_CATALOG_NAME);
		metadata.setCube(DEFAULT_CUBE_NAME);
		metadata.setUniqueName(DEFAULT_UNIQUE_NAME);
		metadata.setQueryName(DEFAULT_QUERY_NAME);
		metadata.setName(ampReport.getName());
		metadata.setConnection(DEFAULT_CONNECTION_NAME);
		metadata.setSchema(DEFAULT_SCHEMA_NAME);

		result.setReportMetadata(metadata);
		
		//Translate column names.
		/*Set<ReportColumn> translatedColumns = new LinkedHashSet<ReportColumn>();  
		Iterator<ReportColumn> iCols = metadata.getReportSpec().getColumns().iterator();
		while(iCols.hasNext()) {
			ReportColumn auxCol = iCols.next();
			String translatedName = TranslatorWorker.translateText(auxCol.getEntityName());
			auxCol = new ReportColumn(translatedName, auxCol.getEntityType());
			translatedColumns.add(auxCol);			
		}
		metadata.getReportSpec().setColumns(translatedColumns);
		
		List<ReportMeasure> translatedMeasures = new ArrayList<ReportMeasure>(); 
		Iterator<ReportMeasure> iMs = metadata.getReportSpec().getMeasures().iterator();
		while(iMs.hasNext()) {
			ReportMeasure auxMeasure = iMs.next();
			String translatedName = TranslatorWorker.translateText(auxMeasure.getMeasureName());
			auxMeasure = new ReportMeasure(translatedName, auxMeasure.getEntityType());
			translatedMeasures.add(auxMeasure);
		}
		metadata.getReportSpec().setMeasures(translatedMeasures);*/
		return result;
	}
	
	@GET
	@Path("/report/{report_id}/result")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final GeneratedReport getReportResult(@PathParam("report_id") Long reportId) {
		// TODO: for now we do not translate other types of reports than 
		// Donor Type reports (hide icons for non-donor-type reports?)
		ReportSpecificationImpl spec = ReportsUtil.getReport(reportId);
		return EndpointUtils.runReport(spec);
	}
	
	@POST
	@Path("/report/custom/paginate")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	/**
	 * Generates a custom report.  
	 * 
	 * @param formParams {@link ReportsUtil#getReportResultByPage form parameters}
	 * @return JsonBean result for the requested page and pagination information
	 * @see ReportsUtil#getReportResultByPage
	 */
	public final JsonBean getCustomReport(JsonBean formParams) {
		List<String> errors = ReportsUtil.validateReportConfig(formParams, true);
		if (errors.size() > 0) {
			JsonBean result = new JsonBean();
			result.set(EPConstants.ERROR, errors);
			return result;
		}
		// we need reportId only to store the report result in cache
		Long reportId = (long) formParams.getString(EPConstants.REPORT_NAME).hashCode();
		formParams.set(EPConstants.IS_CUSTOM, true);
		return getReportResultByPage(formParams, reportId);
	}
	
	/**
	 * Retrieves report data for the specified reportId and a given page number
	 *  
	 * @param reportId    report Id
	 * @param formParams  {@link ReportsUtil#getReportResultByPage form parameters}
	 * @return JsonBean result for the requested page and pagination information
	 * @see ReportsUtil#getReportResultByPage
	 */
	@POST
	@Path("/report/{report_id}/paginate")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final JsonBean getReportResultByPage(JsonBean formParams,
			@PathParam("report_id") Long reportId) {
		return ReportsUtil.getReportResultByPage(reportId, formParams);
	}
	
	@POST
	@Path("/report/{report_id}/result/jqGrid")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	/**
	 * Provides paginated result for tabs.  
	 * @see {@link getReportResultByPage} for more details
	 */
	public final JsonBean getReportResultForTabGrid(JsonBean formParams, 
			@PathParam("report_id") Long reportId) {
		
		List<String> extraColumns = new ArrayList<String>();
		extraColumns.add(ColumnConstants.ACTIVITY_ID);
		extraColumns.add(ColumnConstants.APPROVAL_STATUS);
		extraColumns.add(ColumnConstants.DRAFT);
		extraColumns.add(ColumnConstants.TEAM_ID);
		formParams.set(EPConstants.ADD_COLUMNS, extraColumns);
		
		// Convert jqgrid sorting params into ReportUtils sorting params.
		if (formParams.getString("sidx") != null) {
			List<Map<String, Object>> sorting = new ArrayList<Map<String, Object>>();
			String[] auxColumns = formParams.get("sidx").toString().split(",");
			for (int i = 0; i < auxColumns.length; i++) {
				if (!auxColumns[i].trim().equals("")) {
					Map<String, Object> sort = new HashMap<String, Object>();
					Boolean asc = true;
					if (auxColumns[i].contains(" asc") || formParams.getString("sord").equals("asc")) {
						asc = true;
						auxColumns[i] = auxColumns[i].replace(" asc", "");
					} else if (auxColumns[i].contains(" desc") || formParams.getString("sord").equals("desc")) {
						asc = false;
						auxColumns[i] = auxColumns[i].replace(" desc", "");
					}
					List<String> listOfColumns = new ArrayList<String>();
					listOfColumns.add(auxColumns[i].trim());
					sort.put("columns", listOfColumns);
					sort.put("asc", asc);
					
					// TODO: Testing what happens if we use only the last column
					// coming from jqgrid (specially on hierarchical reports).
					if (i == auxColumns.length - 1) {
						sorting.add(sort);
					}
				}
			}
			formParams.set("sorting", sorting);
		}
		
		// AMP-18516: Fix "page" parameter when is entered manually by user.
		if (formParams.get("page") instanceof String) {
			formParams.set("page", Integer.valueOf(formParams.get("page").toString()));
		}
		
		return getReportResultByPage(formParams, reportId);
	}
	
	@GET
	@Path("/tabs")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final List<JSONTab> getTabs() {

		TeamMember tm = (TeamMember) httpRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
		AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
		if (ampTeamMember != null) {
			return getDefaultTabs(ampTeamMember);
		} else {
			return getPublicTabs();
		}
	}

	private List<JSONTab> getDefaultTabs(AmpTeamMember ampTeamMember) {
		List<JSONTab> tabs = new ArrayList<JSONTab>();

		// Look for the Default Tab and add it visible to the list
		AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(ampTeamMember.getAmpTeam().getAmpTeamId());
		AmpReports defaultTeamReport = ampAppSettings.getDefaultTeamReport();
		if (defaultTeamReport != null) {
			tabs.add(ReportsUtil.convert(defaultTeamReport, true));
		}

		// Get the visible tabs of the currently logged user
		if (ampTeamMember.getDesktopTabSelections() != null && ampTeamMember.getDesktopTabSelections().size() > 0) {
			TreeSet<AmpDesktopTabSelection> sortedSelection = new TreeSet<AmpDesktopTabSelection>(AmpDesktopTabSelection.tabOrderComparator);
			sortedSelection.addAll(ampTeamMember.getDesktopTabSelections());
			Iterator<AmpDesktopTabSelection> iter = sortedSelection.iterator();

			while (iter.hasNext()) {
				AmpReports report = iter.next().getReport();
				JSONTab tab = new JSONTab(report.getAmpReportId(), TranslatorWorker.translateText(report.getName()), true);
				tabs.add(tab);
			}
		}

		// Get the rest of the tabs that aren't visible on first instance
		List<AmpReports> userActiveTabs = TeamUtil.getAllTeamReports(ampTeamMember.getAmpTeam().getAmpTeamId(), true, null, null, true,
				ampTeamMember.getAmpTeamMemId(), null, null);
		Iterator<AmpReports> iter = userActiveTabs.iterator();

		while (iter.hasNext()) {
			AmpReports report = iter.next();
			JSONTab tab = new JSONTab(report.getAmpReportId(), TranslatorWorker.translateText(report.getName()), false);
			boolean found = false;
			Iterator<JSONTab> iTabs = tabs.iterator();
			while (iTabs.hasNext() && !found) {
				JSONTab auxTab = iTabs.next();
				if (auxTab.getId() == tab.getId()) {
					found = true;
				}
			}
			if (!found) {
				tabs.add(tab);
			}
		}
		// tabs.addAll(userActiveTabs);
		return tabs;
	}

	private List<JSONTab> getPublicTabs() {
		List<JSONTab> tabs = new ArrayList<JSONTab>();
		return tabs;
	}
	
	@POST
	@Path("/saikureport/{report_id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final QueryResult getSaikuReport(JsonBean queryObject, @PathParam("report_id") Long reportId) {
		QueryResult result;
		try {
			result = RestUtil.convert(getSaikuCellDataSet(queryObject, reportId));
			result.setQuery(new ThinQuery());
		} catch (Exception e) {
			String error = ExceptionUtils.getRootCauseMessage(e);
			return new QueryResult(error);
		}
		return result;
	}	

	public final CellDataSet getSaikuCellDataSet(JsonBean queryObject, Long reportId) throws Exception {

		//TODO: Move this to util classes, check with Tabs to see how it's done there for uniformity
		MondrianReportFilters filterRules = null;
		LinkedHashMap<String, Object> queryModel = (LinkedHashMap<String, Object>) queryObject.get("queryModel");
		if(queryModel.containsKey("filtersApplied") && (Boolean)queryModel.get("filtersApplied")) {
			LinkedHashMap<String, Object> filters = (LinkedHashMap<String, Object>) queryModel.get("filters");
			filterRules = FilterUtils.getFilterRules((LinkedHashMap<String, Object>)filters.get("columnFilters"), (LinkedHashMap<String, Object>)filters.get("otherFilters"), null);
		}
		
		AmpReports ampReport = DbUtil.getAmpReport(reportId);

		MondrianReportGenerator generator = new MondrianReportGenerator(SaikuReportArea.class, ReportEnvironment.buildFor(httpRequest));
		SaikuGeneratedReport report = null;
		try {
			ReportSpecificationImpl spec = AmpReportsToReportSpecification.convert(ampReport);
			if(filterRules != null) spec.setFilters(filterRules);
			if(queryModel.containsKey("settingsApplied") && (Boolean)queryModel.get("settingsApplied")) {
				EndpointUtils.applySettings(spec, extractSettings(queryModel));
			}
			report = (SaikuGeneratedReport) generator.executeReport(spec);
			System.out.println("[" + spec.getReportName() + "] total report generation duration = " + report.generationTime + "(ms)");
		} catch (Exception e) {
			logger.error("Cannot execute report (" + ampReport + ")", e);
			String error = ExceptionUtils.getRootCauseMessage(e);
		}
		
		//Adjust Width. Hackish correction until we can correctly determine why and how the totals are being miscalculated for columns
		//This is also corrected in the client side (file SaikuTableRenderer, function sanitizeRows) 
		if (report.cellDataSet.getCellSetHeaders().length > 0 )
			report.cellDataSet.setWidth(report.cellDataSet.getCellSetHeaders()[0].length);
		return report.cellDataSet;
	}
	@POST
	@Path("/saikureport/export/xls/{report_id}")
	@Produces({"application/vnd.ms-excel" })
	public final Response exportXlsSaikuReport(String query, @PathParam("report_id") Long reportId) {
		return exportSaikuReport(query, reportId, "xls");
		
	}
	@POST
	@Path("/saikureport/export/csv/{report_id}")
	@Produces({"text/csv"})
	public final Response exportCsvSaikuReport(String query, @PathParam("report_id") Long reportId) {
		return exportSaikuReport(query, reportId, "csv");
	}

	@POST
	@Path("/saikureport/export/pdf/{report_id}")
	@Produces({"application/pdf"})
	public final Response exportPdfSaikuReport(String query, @PathParam("report_id") Long reportId) {
		return exportSaikuReport(query, reportId, "pdf");
	}

	public final Response exportSaikuReport(String query, Long reportId, String type) {
        CellDataSet result;
		try {
			String decodedQuery = java.net.URLDecoder.decode(query);
			decodedQuery = decodedQuery.replace("query=", "");
			JsonBean queryObject = JsonBean.getJsonBeanFromString(decodedQuery);
			result = getSaikuCellDataSet(queryObject, reportId);

			byte[] doc = null;
			String filename = "";
			
			switch(type) {
				case "xls": 
					ExcelBuilderOptions options = new ExcelBuilderOptions();
					options.repeatValues = true;
			        ExcelWorksheetBuilder worksheetBuilder = new ExcelWorksheetBuilder(result, new ArrayList<ThinHierarchy>(), options);
			        doc = worksheetBuilder.build();
					filename = "export.xls";
					break;
				case "csv":
					doc = SaikuUtils.getCsv(result, ",", "\"");
					filename = "export.csv";
					break;
				case "pdf":
		            PdfReport pdf = new PdfReport();
		    		QueryResult qr = RestUtil.convert(result);
		            doc  = pdf.pdf(qr, null);
					filename = "export.pdf";
					break;
			}
			
			if(doc != null) {
				return Response.ok(doc, MediaType.APPLICATION_OCTET_STREAM).header(
		                "content-disposition",
		                "attachment; filename = " + filename).header(
		                "content-length",doc.length).build();
			}
			else
			{
				throw new Exception("Empty response while exporting.");
			}
			
		} catch (Exception e) {
			String error = ExceptionUtils.getRootCauseMessage(e);
			logger.error(e.getMessage());
			e.printStackTrace();
			return Response.serverError().build();
		}
        
	}

	private JsonBean extractSettings(LinkedHashMap<String, Object> queryModel) {
		JsonBean config = new JsonBean();
		config.set("settings", queryModel.get("settings"));
		return config;
	}

	@GET
	@Path("/report/{report_id}/settings/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final Object getSettings(@PathParam("report_id") Long reportId) {
		AmpARFilter arFilter = FilterUtil.buildFilter(null, reportId);
		ReportsFilterPickerForm oldFilterForm = new ReportsFilterPickerForm();
		FilterUtil.populateForm(oldFilterForm, arFilter, reportId);
		ReportsFilterPicker.fillSettingsFormDropdowns(oldFilterForm);
		Map<String, Object> settings = new HashMap<String, Object>();
		settings.put("decimalSeparators", oldFilterForm.getAlldecimalSymbols());
		settings.put("decimalSymbols", oldFilterForm.getAlldecimalSymbols());
		settings.put("groupSeparators", oldFilterForm.getAllgroupingseparators());
		settings.put("amountInThousands", oldFilterForm.getAmountinthousands());
		settings.put("selectedDecimalPlaces", oldFilterForm.getCustomDecimalPlaces());
		settings.put("selectedDecimalSeparator", oldFilterForm.getCustomDecimalSymbol());
		settings.put("selectedGroupSeparator", oldFilterForm.getCustomGroupCharacter());
		settings.put("selectedUseGroupingSeparator", oldFilterForm.getCustomUseGrouping());
		// settings.put("calendars", oldFilterForm.getCalendars());
		// settings.put("currencies", oldFilterForm.getCurrencies());
		return settings;
	}
	
	@GET
	@Path("/report/columns")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final Map<String, String> getAllowedColumns() {
		Map<String, String> columnToDisplayName = new HashMap<String, String>();
		Set<String> configurableColumns = MondrianReportUtils.getConfigurableColumns();
		for (String originalColumnName : configurableColumns) {
			columnToDisplayName.put(originalColumnName, TranslatorWorker.translateText(originalColumnName));
		}
		
		return columnToDisplayName;
	}
	
	@GET
	@Path("/report/measures")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final Map<String, String> getAllowedMeasures() {
		Map<String, String> measuresToDisplayName = new HashMap<String, String>();
		Set<String> configurableMeasures = MondrianReportUtils.getConfigurableMeasures();
		for (String originalMeasureName : configurableMeasures) {
			measuresToDisplayName.put(originalMeasureName, TranslatorWorker.translateText(originalMeasureName));
		}
		
		return measuresToDisplayName;
	}
	
	
	/*@POST
	@Path("/report/saveTab/{report_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public final void saveTab(JsonBean formParams, @PathParam("report_id") Long reportId) {
		// Open AmpReport.
		AmpReports oldReport = DbUtil.getAmpReport(reportId);

		// Convert json object back to the new format: MondrianReportFilters.
		if (formParams.get("filters") != null) {
			JsonBean filters = new JsonBean();
			LinkedHashMap<String, Object> requestFilters = (LinkedHashMap<String, Object>) formParams.get("filters");
			MondrianReportFilters mondrianReportFilters = null;
			if (requestFilters != null) {
				filters.any().putAll(requestFilters);
				mondrianReportFilters = FilterUtils.getFilters(filters);
				
				// Transform back to legacy ARFilters.
				MondrianReportFiltersConverter converter = new MondrianReportFiltersConverter(mondrianReportFilters);
				AmpARFilter ampARFilters = converter.buildFilters();
			}
		}
	}*/
	
	@POST
	@Path("/report/export-to-map/{report_id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	/**
	 * Exports the report to Map
	 * 
	 * @param config current report configuration (settings, filters)
	 * @param reportId report id
	 * @return Api state configuration id 
	 */
	public String exportToMap(JsonBean config, @PathParam("report_id") Long reportId) {
		return ReportsUtil.exportToMap(config, reportId);
	}
}
