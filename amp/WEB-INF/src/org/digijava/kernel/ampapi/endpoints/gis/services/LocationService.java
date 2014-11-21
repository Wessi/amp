package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.map.ObjectWriter;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.helpers.geojson.objects.ClusteredPoints;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.jdbc.Work;

import com.tonbeller.wcf.utils.ObjectFactory.ObjectHolder;

/**
 * 
 * @author Diego Dimunzio
 *
 */

public class LocationService {
	protected static Logger logger = Logger.getLogger(LocationService.class);

	/**
	 * Get totals (actual commitments/ actual disbursements) by administrative level
	 * @param admlevel
	 * @param type
	 * @return
	 */
	public JsonBean getTotals(String admlevel, String type, JsonBean filter) {
		String err = null;
		JsonBean retlist = new JsonBean();
		String admLevelId=null;
		switch (admlevel) {
		case "adm0":
			admlevel = ColumnConstants.COUNTRY; 
			admLevelId = CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY
					.getIdInDatabase().toString();
			break;
		case "adm1":
			admlevel = ColumnConstants.REGION;
			admLevelId = CategoryConstants.IMPLEMENTATION_LOCATION_REGION
					.getIdInDatabase().toString();
			break;
		case "adm2":
			admlevel = ColumnConstants.ZONE; 
			admLevelId = CategoryConstants.IMPLEMENTATION_LOCATION_ZONE
					.getIdInDatabase().toString();
			break;
		case "adm3":
			admlevel = ColumnConstants.DISTRICT; 
			admLevelId = CategoryConstants.IMPLEMENTATION_LOCATION_DISTRICT
					.getIdInDatabase().toString();
			break;
		default:
			admlevel = ColumnConstants.REGION; 
			admLevelId = CategoryConstants.IMPLEMENTATION_LOCATION_REGION
					.getIdInDatabase().toString();
			break;
		}
		
		switch (type) {
		case "ac":
			type = MeasureConstants.ACTUAL_COMMITMENTS; 
			break;
		case "ad":
			type = MeasureConstants.ACTUAL_DISBURSEMENTS; 
			break;
		default:
			type = MeasureConstants.ACTUAL_COMMITMENTS;
			break;
		}
		String numberformat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
 		ReportSpecificationImpl spec = new ReportSpecificationImpl("LocationsTotals");
		spec.addColumn(new ReportColumn(admlevel, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.GEOCODE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(type, ReportEntityType.ENTITY_TYPE_ALL));
		spec.getHierarchies().addAll(spec.getColumns());
		MondrianReportFilters filterRules=new MondrianReportFilters(); 
		
		if(filter!=null){
			Object columnFilters=filter.get("columnFilters");
			if(columnFilters!=null){
				filterRules = FilterUtils.getApiColumnFilter((LinkedHashMap<String, Object>)filter.get("columnFilters"));	
			}
 		}
		AmpCategoryValueLocations country = DynLocationManagerUtil.getDefaultCountry();

		// code below disabled because filter-by-value not supported anymore; also this column will be redefined because of AMP-18736
//		if(admlevel.equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey())){
//			filterRules.addFilterRule(MondrianReportUtils.getColumn(ColumnConstants.COUNTRY, ReportEntityType.ENTITY_TYPE_ACTIVITY), 
//					new FilterRule(country.getName(), true, false));
//		}
		
		filterRules.addFilterRule(MondrianReportUtils.getColumn(ColumnConstants.IMPLEMENTATION_LEVEL, ReportEntityType.ENTITY_TYPE_ACTIVITY), 
				new FilterRule(admLevelId, true));
		spec.setFilters(filterRules);
		
		EndpointUtils.applySettings(spec, filter);
		
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils.getRequest()),true);
		GeneratedReport report = null;
		
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			err = e.getMessage();
		}
		
		String currcode = FilterUtils.getSettingbyName((LinkedHashMap<Integer, Object>) filter.get(EPConstants.SETTINGS),SettingsConstants.CURRENCY_ID);
		retlist.set("currency", currcode);

		retlist.set("numberformat", numberformat);
		List<JsonBean> values = new ArrayList<JsonBean>();
		for (Iterator iterator = report.reportContents.getChildren().iterator(); iterator.hasNext();) {
			JsonBean item = new JsonBean();
			ReportAreaImpl reportArea =  (ReportAreaImpl) iterator.next();
			LinkedHashMap<ReportOutputColumn, ReportCell> content = (LinkedHashMap<ReportOutputColumn, ReportCell>) reportArea.getContents();
			org.dgfoundation.amp.newreports.TextCell reportcolumn = (org.dgfoundation.amp.newreports.TextCell) content.values().toArray()[1];
			item.set("admID",reportcolumn.value);
			ReportCell reportcell = (ReportCell) content.values().toArray()[2];
			item.set("amount",reportcell.value);
			values.add(item);
		}
		retlist.set("values", values);
		return retlist;
	}
	/**
	 * Build an excel file export by structure
	 * @return
	 */
	public static HSSFWorkbook generateExcelExportByStructure(LinkedHashMap<String, Object> filters){
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(TranslatorWorker.translateText("Time Stamp"));//1
		columnNames.add(TranslatorWorker.translateText("Activity Id"));//2
		columnNames.add(TranslatorWorker.translateText("Project Title"));//3
		columnNames.add(TranslatorWorker.translateText("Description"));//4
//		columnNames.add(TranslatorWorker.translateText("Approval Date"));
		columnNames.add(TranslatorWorker.translateText("Project Site"));//5
		columnNames.add(TranslatorWorker.translateText("Latitude"));//6
		columnNames.add(TranslatorWorker.translateText("Longitude"));//7
//		columnNames.add(TranslatorWorker.translateText("Project Site"));//8
		columnNames.add(TranslatorWorker.translateText("Sectors"));//9
		columnNames.add(TranslatorWorker.translateText("Donors"));//10
		columnNames.add(TranslatorWorker.translateText("Total Project Commitments"));//11 
		columnNames.add(TranslatorWorker.translateText("Total Project Disbursements"));//12
		
		
		List<Activity> report=getMapExportByStructure(filters);
		java.util.Date date = new java.util.Date();
		
		int i=1;
		List<List>rowValues=new ArrayList<>();

		for (Activity a : report) {
			List<String>coloumnsValues=new ArrayList<String>();
			coloumnsValues.add(date.toString());			//1
			coloumnsValues.add(a.getAmpId());//2
			coloumnsValues.add(a.getName());//3
			coloumnsValues.add(a.getDescription());//4
			coloumnsValues.add(a.getStructureName());//5
			coloumnsValues.add(a.getLatitude());//6
			coloumnsValues.add(a.getLongitude());//7
//			coloumnsValues.add(a.getImplementationLevel());//8
			coloumnsValues.add(a.getPrimarySector());//9
			coloumnsValues.add(a.getDonorAgency());//10
			coloumnsValues.add(a.getTotalCommitments());//11
			coloumnsValues.add(a.getTotalDisbursments());//12
			rowValues.add(coloumnsValues);
		}
		
		
		return generateExcelExport(columnNames,rowValues,"MapExport-structure.xls");
	}
	/**
	 * Return a excel workbook 
	 * @param columnNames arraylist of columnames 
	 * @param rowValues arraylist of row values, should be in the same order as column names
	 * @param fileName file for the excel file
	 * @return
	 */
	
	
	public static HSSFWorkbook generateExcelExport(List<String> columnNames,List<List>rowValues,String fileName){
		HSSFWorkbook wb = new HSSFWorkbook();


		HSSFSheet sheet = wb.createSheet(fileName);

		HSSFRow row = sheet.createRow((short) (0));
		HSSFRichTextString str = null;
		HSSFFont titlefont = wb.createFont();

		titlefont.setFontHeightInPoints((short) 10);
		titlefont.setBoldweight(titlefont.BOLDWEIGHT_BOLD);
		
		HSSFFont font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 8);
		font.setBoldweight(font.BOLDWEIGHT_NORMAL);
		HSSFCellStyle style = wb.createCellStyle();
		HSSFCellStyle tstyle = wb.createCellStyle();
		tstyle.setFont(titlefont);
		tstyle.setAlignment(style.ALIGN_CENTER);
		
		int x = 0;
		for (String columnName:columnNames) {
			HSSFCell cell = row.createCell( x);
			str = new HSSFRichTextString(columnName);
			cell.setCellValue(str);
			cell.setCellStyle(tstyle);
			x++;
		}
		int i=1;
		for (List<String> list : rowValues) {
			int j=0;
			row = sheet.createRow((short) (i));
			for (String colValue : list) {
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(new HSSFRichTextString(colValue));
				cell.setCellStyle(style);
				j++;
			}
			i++;
		}
		return wb;
	}
	/**
	 * Returs a list of activities for the mapexport. once we have the chance to as a report row its value
	 * by report column name we can avoid all the iff and directly export the report to excel
	 * also we can remove the query once the amp_structure table is in the mondrian schem
	 * @return
	 */
	public static List<Activity> getMapExportByLocation(final Map<String,Activity>geocodeInfo,LinkedHashMap<String, Object> filters) {
		List<Activity> activities = new ArrayList<Activity>();
		final List<String> geoCodesId = new ArrayList<String>();
		ReportSpecificationImpl spec = new ReportSpecificationImpl("MapExport");
		Set<ReportColumn> hierarchies = new LinkedHashSet<ReportColumn>();
		ReportColumn ampId = new ReportColumn(ColumnConstants.AMP_ID,
				ReportEntityType.ENTITY_TYPE_ALL);

		ReportColumn geoid = MondrianReportUtils.getColumn(
				ColumnConstants.GEOCODE, ReportEntityType.ENTITY_TYPE_ACTIVITY);

		ReportColumn impLevel = MondrianReportUtils.getColumn(
				ColumnConstants.IMPLEMENTATION_LEVEL,
				ReportEntityType.ENTITY_TYPE_ACTIVITY);
		spec.addColumn(geoid);

		spec.addColumn(ampId);
		spec.addColumn(impLevel);
		spec.addColumn(geoid);
		hierarchies.add(ampId);
		hierarchies.add(impLevel);
		hierarchies.add(geoid);

		spec.setHierarchies(hierarchies);

		getCommonSpecForExport(spec);

		MondrianReportGenerator generator = new MondrianReportGenerator(
				ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils
						.getRequest()), false);
		GeneratedReport report = null;
		
		
		applyFilters((LinkedHashMap<String, Object>)filters.get("otherFilters"),(LinkedHashMap<String, Object>)filters.get("columnFilters"), spec);
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
		if (report != null && report.reportContents != null
				&& report.reportContents.getChildren() != null) {
			for (ReportArea reportArea : report.reportContents.getChildren()) {
				getActivitiesById(reportArea, activities, geoCodesId);
			}
			// Go and fetch location specific information
			if(geoCodesId.size()==0){
				return activities;
			}
			PersistenceManager.getSession().doWork(new Work() {
				public void execute(Connection conn) throws SQLException {

					String query = "select geo_code,location_name,gs_lat,gs_long from "
							+ " amp_category_value_location   "
							+ "where  geo_code in ("
							+ org.dgfoundation.amp.Util.toCSString(geoCodesId)
							+ ")";
					ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
					while (rs.next()) {
						Activity a = new Activity();
						String geoCode = rs.getString("geo_code");
						a.setLocationName(rs.getString("location_name"));
						a.setLatitude(rs.getString("gs_lat"));
						a.setLongitude(rs.getString("gs_long"));
						a.setGeoCode(geoCode);
						geocodeInfo.put(geoCode, a);
					}
				}
			});
			Collections.sort(activities, new Comparator<Activity>() {
				@Override
				public int compare(Activity a, Activity b) {
					return a.getAmpId().compareTo(b.getAmpId());
				}
			});
		}
		return activities;
	}
	private static void applyFilters(LinkedHashMap<String, Object> otherFilter,LinkedHashMap<String, Object>columnFilters,
			ReportSpecificationImpl spec) {
		List<String> activitIds=null;
		if (otherFilter != null) {
			activitIds = FilterUtils.applyKeywordSearch( otherFilter);
		}
		
 		MondrianReportFilters filterRules = FilterUtils.getFilterRules(columnFilters,
				otherFilter, activitIds);
		if(filterRules!=null){
			spec.setFilters(filterRules);
		}
	}
	
	private static void getActivitiesById(ReportArea reportArea,
			List<Activity> activities,List<String>geoCodesId) {
		if (reportArea.getChildren() == null) {
			Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
			Set<ReportOutputColumn> col = row.keySet();
			Activity a = new Activity();
			for (ReportOutputColumn reportOutputColumn : col) {
				String columnValue = row.get(reportOutputColumn).displayedValue
						.toString();
				if (reportOutputColumn.originalColumnName
						.equals(ColumnConstants.GEOCODE)) {
					if(columnValue==null || columnValue.equals("Undefined")|| columnValue.equals("") || columnValue.equals("GeoId: Undefined")){
						return;
					}
					geoCodesId.add(columnValue);
					a.setGeoCode(columnValue);
				}
				else{
					if (reportOutputColumn.originalColumnName
							.equals(ColumnConstants.IMPLEMENTATION_LEVEL)) {
						a.setImplementationLevel(columnValue);
					}else{
						getActivityIdForReports( a, row,reportOutputColumn);
					}
				}
			}
			activities.add(a);
		} else {
			for (ReportArea innerreportArea : reportArea.getChildren()) {
				getActivitiesById(innerreportArea,activities,geoCodesId);
			}
		}
	}
	/**
	 * Returs a list of activities for the mapexport. once we have the chance to as a report row its value
	 * by report column name we can avoid all the iff and directly export the report to excel
	 * also we can remove the query once the amp_structure table is in the mondrian schem
	 * @return
	 */
	public static List<Activity> getMapExportByStructure(LinkedHashMap<String, Object> filters) {
		final List<Activity> mapExportBean = new ArrayList<Activity>();

		ReportSpecificationImpl spec = new ReportSpecificationImpl("MapExport");
		//since amp_id will be added as a hiearchy onthe other report
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.AMP_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		getCommonSpecForExport(spec);

		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils.getRequest()),false);
		GeneratedReport report = null;
		applyFilters((LinkedHashMap<String, Object>)filters.get("otherFilters"),(LinkedHashMap<String, Object>)filters.get("columnFilters"), spec);
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
		final Map<Long,Activity>activities=new LinkedHashMap<Long,Activity>();

		if (report != null && report.reportContents != null
				&& report.reportContents.getChildren() != null) {
			for (ReportArea reportArea : report.reportContents.getChildren()) {
				Long activityId = 0L;
				Activity activity = new Activity();
				Map<ReportOutputColumn, ReportCell> row = reportArea
						.getContents();
				Set<ReportOutputColumn> col = row.keySet();
				for (ReportOutputColumn reportOutputColumn : col) {
					if (reportOutputColumn.originalColumnName
							.equals(ColumnConstants.ACTIVITY_ID)) {
						activityId = Long
								.parseLong(row.get(reportOutputColumn).value
										.toString());
						activity.setId(activityId);
					} else {
						getActivityIdForReports(activity, row,
								reportOutputColumn);
					}
				}
				activities.put(activityId, activity);
			}
			if(activities.size()==0){
				return mapExportBean;
			}
		//once we have all activities we go and fetch structures associated to those activities
		//since its not yet implemented on reports we fetch them separately 
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				
				
				String query="select ast.amp_activity_id,s.title, "+
						" s.latitude, "+
						" s.longitude "+
						"  from amp_activity_structures ast , amp_structure s  " +
						" where ast.amp_structure_id=s.amp_structure_id "+
						" and ast.amp_activity_id in("+ org.dgfoundation.amp.Util.toCSString(activities.keySet()) +")";
	    		ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
	    		while (rs.next()){
	    			Activity a= activities.get(rs.getLong("amp_activity_id"));
	    			Activity newActivity=new Activity();
	    			newActivity.setId(a.getId());
	    			newActivity.setAmpId(a.getAmpId());
	    			newActivity.setName(a.getName());
	    			newActivity.setTotalCommitments(a.getTotalCommitments());
	    			newActivity.setTotalDisbursments(a.getTotalDisbursments());
	    			newActivity.setDonorAgency(a.getDonorAgency());
	    			newActivity.setImplementationLevel(a.getImplementationLevel());
	    			newActivity.setPrimarySector(a.getPrimarySector());

	    			newActivity.setDescription(a.getDescription());
	    			
	    			newActivity.setStructureName(rs.getString("title"));
	    			newActivity.setLatitude(rs.getString("latitude"));
	    			newActivity.setLongitude(rs.getString("longitude"));
	    			
	    			mapExportBean.add(newActivity);
	    			
	    		}
				
			}
			});
		
		}
		return mapExportBean;
	}
	private static void  getActivityIdForReports(
			Activity activity, Map<ReportOutputColumn, ReportCell> row,
			ReportOutputColumn reportOutputColumn) {

			String columnValue=row.get(reportOutputColumn).displayedValue.toString();
			if(reportOutputColumn.originalColumnName.equals(ColumnConstants.AMP_ID)){
				activity.setAmpId(columnValue);
			}else{
				if(reportOutputColumn.originalColumnName.equals(ColumnConstants.PROJECT_TITLE)){
					activity.setName(columnValue);
				}else{
					if(reportOutputColumn.originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS)){
						activity.setTotalCommitments(columnValue);
					}
						else{
							if(reportOutputColumn.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)){
								activity.setTotalDisbursments(columnValue);
							}	else{ 
								if(reportOutputColumn.originalColumnName.equals(ColumnConstants.DONOR_AGENCY)){
									activity.setDonorAgency(columnValue);
								}else{
									if(reportOutputColumn.originalColumnName.equals(ColumnConstants.PRIMARY_SECTOR)){
										activity.setPrimarySector(columnValue);
									}
									else{
										if(reportOutputColumn.originalColumnName.equals(ColumnConstants.PROJECT_DESCRIPTION)){
											activity.setDescription(columnValue);
										}
									}
								}
							}
					}
				}
			}

	}
	public static void getCommonSpecForExport(ReportSpecificationImpl spec) {
		boolean doTotals=true;


		//hierarchies
//		Set<ReportColumn> hierarchies=new LinkedHashSet<ReportColumn>();
//		hierarchies.add(c);
//		spec.setHierarchies(hierarchies);
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.DONOR_AGENCY, ReportEntityType.ENTITY_TYPE_ACTIVITY));
//		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_DESCRIPTION, ReportEntityType.ENTITY_TYPE_ACTIVITY));

		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));

		spec.setCalculateColumnTotals(doTotals);
		spec.setCalculateRowTotals(doTotals);
	}
	public static HSSFWorkbook generateExcelExportByLocation(LinkedHashMap<String, Object> filters) {

		
		
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(TranslatorWorker.translateText("Time Stamp"));//0
		columnNames.add(TranslatorWorker.translateText("Activity Id"));//1
		columnNames.add(TranslatorWorker.translateText("Project Title"));//2
		columnNames.add(TranslatorWorker.translateText("Description"));//3
		columnNames.add(TranslatorWorker.translateText("Type"));//4
		columnNames.add(TranslatorWorker.translateText("Location Name"));//5
		columnNames.add(TranslatorWorker.translateText("Latitude"));//6
		columnNames.add(TranslatorWorker.translateText("Longitude"));//7
		columnNames.add(TranslatorWorker.translateText("GeoId"));//8
		columnNames.add(TranslatorWorker.translateText("Sectors"));//9
		columnNames.add(TranslatorWorker.translateText("Donors"));//10
		columnNames.add(TranslatorWorker.translateText("Total Project Commitments"));//11
		columnNames.add(TranslatorWorker.translateText("Total Project Disbursements"));//12

		final Map<String,Activity>geocodeInfo=new LinkedHashMap<String,Activity>();
		
		List<Activity> report=getMapExportByLocation(geocodeInfo,filters);
		java.util.Date date = new java.util.Date();
		
		int i=1;
		List<List>rowValues=new ArrayList<>();

		for (Activity a : report) {
			List<String>coloumnsValues=new ArrayList<String>();
			coloumnsValues.add(date.toString());//0
			coloumnsValues.add(a.getAmpId());//1
			coloumnsValues.add(a.getName());//2
			coloumnsValues.add(a.getDescription());//3
			coloumnsValues.add(a.getImplementationLevel());//4
			coloumnsValues.add(geocodeInfo.get(a.getGeoCode()).getLocationName());//5
			coloumnsValues.add(geocodeInfo.get(a.getGeoCode()).getLatitude());//6
			coloumnsValues.add(geocodeInfo.get(a.getGeoCode()).getLongitude());//7
			coloumnsValues.add(a.getGeoCode());//8

			coloumnsValues.add(a.getPrimarySector());//9
			coloumnsValues.add(a.getDonorAgency());//10
			coloumnsValues.add(a.getTotalCommitments());//11
			coloumnsValues.add(a.getTotalDisbursments());//12	
			rowValues.add(coloumnsValues);
		}
		
		
		return generateExcelExport(columnNames,rowValues,"map-export-administrative-Locations.xls");
	}
	
	public static List<ClusteredPoints> getClusteredPoints(JsonBean config) throws AmpApiException {
		String adminLevel = "";
		final List<ClusteredPoints> l = new ArrayList<ClusteredPoints>();

		if (config != null) {
			Object otherFilter = config.get("otherFilters");
			if (otherFilter != null
					&& ((Map<String, Object>) otherFilter).get("adminLevel") != null) {
				adminLevel = ((Map<String, Object>) otherFilter).get(
						"adminLevel").toString();
			}
		}
		
		final String usedAdminLevel = adminLevel;
		//fetch activities filtered by mondrian
		List<Long>activitiesId = getActivitiesForFiltering(config);
		final Double countryLatitude=FeaturesUtil.getGlobalSettingDouble(GlobalSettingsConstants.COUNTRY_LATITUDE);
		final Double countryLongitude=FeaturesUtil.getGlobalSettingDouble(GlobalSettingsConstants.COUNTRY_LONGITUDE);
		final ValueWrapper<String> qry = new ValueWrapper<String>(null);
		if(adminLevel.equals("Country")){
					qry.value = " SELECT al.amp_activity_id, acvl.id root_location_id,acvl.location_name root_location_description,acvl.gs_lat, acvl.gs_long "+  
					" FROM amp_activity_location al   "+
					" join amp_location loc on al.amp_location_id = loc.amp_location_id  "+
					" join amp_category_value_location acvl on loc.location_id = acvl.id  "+
					" join amp_category_value amcv on acvl.parent_category_value =amcv.id "+  
					" where amcv.category_value ='Country' "+
					" and al.amp_activity_id in(" + Util.toCSStringForIN(activitiesId) + " ) " +
					" and location_name=(select country_name "
					+ " from DG_COUNTRIES where iso='"+ FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY) +"')";

			
		}else{
		qry.value = " WITH RECURSIVE rt_amp_category_value_location(id, parent_id, gs_lat, gs_long, acvl_parent_category_value, level, root_location_id,root_location_description) AS ( "
				+ " select acvl.id, acvl.parent_location, acvl.gs_lat, acvl.gs_long, acvl.parent_category_value, 1, acvl.id,acvl.location_name  "
				+ " from amp_category_value_location acvl  "
				+ " join amp_category_value amcv on acvl.parent_category_value =amcv.id  "
				+ " where amcv.category_value ='"
				+ adminLevel
				+ "'  "
				+ " and acvl.gs_lat is not null and acvl.gs_long is not null  "
				+ " UNION ALL  "
				+ " SELECT acvl.id, acvl.parent_location, rt.gs_lat, rt.gs_long, acvl.parent_category_value, rt.LEVEL + 1, rt.root_location_id, rt.root_location_description  "
				+ " FROM rt_amp_category_value_location rt, amp_category_value_location acvl  "
				+ " WHERE acvl.parent_location =rt.id  "
				+ " )  "
				+ " SELECT al.amp_activity_id, acvl.root_location_id, acvl.root_location_description, acvl.gs_lat, acvl.gs_long  "
				+ " FROM amp_activity_location al  "
				+ " join amp_location loc on al.amp_location_id = loc.amp_location_id  "
				+ " join rt_amp_category_value_location acvl on loc.location_id = acvl.id  "
				+ " where al.amp_activity_id in(" + Util.toCSStringForIN(activitiesId) + " ) "
				+ " order by acvl.root_location_id,al.amp_activity_id";
		}

		try {
			PersistenceManager.getSession().doWork(new Work() {

				@Override
				public void execute(Connection connection) throws SQLException {
					try(java.sql.ResultSet rs = SQLUtils.rawRunQuery(connection, qry.value, null)) {
						ClusteredPoints cp = null;
						Long rootLocationId = 0L;
						while (rs.next()) {
							if (!rootLocationId.equals(rs.getLong("root_location_id"))) {
								if (cp != null) {
									l.add(cp);
								}
								rootLocationId = rs.getLong("root_location_id");
								cp = new ClusteredPoints();
								cp.setAdmin(rs.getString("root_location_description"));
								if (usedAdminLevel.equals("Country")){
									cp.setLat(countryLatitude.toString());
									cp.setLon(countryLongitude.toString());							
								}else{
									cp.setLat(rs.getString("gs_lat"));
									cp.setLon(rs.getString("gs_long"));
								}
							}
							cp.getActivityids().add(rs.getLong("amp_activity_id"));
						}
						if (cp != null) {
							l.add(cp);
						}
					}
				}});
		}
		catch(HibernateException e){
			throw new RuntimeException(e);
		}
	
		return l;
	}

	private static List<Long> getActivitiesForFiltering(JsonBean config)
			throws AmpApiException {
		List<Long> activitiesId = new ArrayList<Long>();
		boolean doTotals = true;
		ReportSpecificationImpl spec = new ReportSpecificationImpl(
				"ActivityIds");

		spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID,
				ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS,
				ReportEntityType.ENTITY_TYPE_ALL));
		spec.setCalculateColumnTotals(doTotals);
		spec.setCalculateRowTotals(doTotals);
		MondrianReportFilters filterRules = FilterUtils.getFilters(config);

		if (filterRules != null) {
			spec.setFilters(filterRules);
		}
		MondrianReportGenerator generator = new MondrianReportGenerator(
				ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils
						.getRequest()), false);
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
		} catch (AMPException e) {
			logger.error("Cannot execute report", e);
			throw new AmpApiException(e);
		}
		List<ReportArea> ll = null;
		ll = report.reportContents.getChildren();
		if (ll != null) {
			for (ReportArea reportArea : ll) {
				Map<ReportOutputColumn, ReportCell> row = reportArea
						.getContents();
				Set<ReportOutputColumn> col = row.keySet();
				for (ReportOutputColumn reportOutputColumn : col) {
					if (reportOutputColumn.originalColumnName
							.equals(ColumnConstants.ACTIVITY_ID)) {
						activitiesId.add(new Long(
								row.get(reportOutputColumn).value.toString()));
					}
				}
			}

		}
		return activitiesId;
	}
	@SuppressWarnings("unchecked")
	public static List<AmpStructure> getStructures(JsonBean config) throws AmpApiException{
		List<AmpStructure> al = null;
		List<Long> activitiesId=getActivitiesForFiltering( config);
		String queryString = "select s from " + AmpStructure.class.getName() + " s inner join s.activities a where"
					+ " a.ampActivityId in (" + Util.toCSStringForIN(activitiesId) + " )";
			Query q = PersistenceManager.getSession().createQuery(queryString);
			
			al = q.list();

		
		return al;

	}	
}
