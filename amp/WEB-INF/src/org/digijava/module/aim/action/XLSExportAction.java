/**
 * XLSExportAction.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.util.IOUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.view.xls.GroupReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public class XLSExportAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
	    HttpSession session = request.getSession();
	    AmpARFilter arf= (AmpARFilter) session.getAttribute(ArConstants.REPORTS_FILTER);
	    TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		if(tm == null){
			//Prepare filter for Public View export
			if(arf==null) arf=new AmpARFilter();		
			arf.setPublicView(true);
			session.setAttribute(ArConstants.REPORTS_FILTER,arf);
			if ("true".equals(request.getParameter("resetFilter"))){
				request.setAttribute(ArConstants.INITIALIZE_FILTER_FROM_DB, "true");
			}
		}

		GroupReportData rd = ARUtil.generateReport(mapping, form, request,
				response);

		ARUtil.cleanReportOfHtmlCodes(rd);
		
		rd.setCurrentView(GenericViews.XLS);
		
		if (session.getAttribute("currentMember")!=null ||  rd.getReportMetadata().getPublicReport()){
	     response.setContentType("application/msexcel");
	     	response.setHeader("Content-Disposition","attachment; filename="+ rd.getName().replace(" ","_") +".xls");
	        AdvancedReportForm reportForm = (AdvancedReportForm) form;
	        //
			AmpReports r=(AmpReports) session.getAttribute("reportMeta");
		
//			for translation purposes
			Site site = RequestUtils.getSite(request);
			Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
					
			String siteId=site.getId().toString();
			String locale=navigationLanguage.getCode();	
			
		int numberOfColumns = rd.getTotalDepth();
		
		String sortBy=(String) session.getAttribute("sortBy");
		if(sortBy!=null){
			rd.setSorterColumn(sortBy); 
			rd.setSortAscending( (Boolean)session.getAttribute(ArConstants.SORT_ASCENDING) );
		}
			
		Map sorters=(Map) session.getAttribute("reportSorters");
		//XLSExporter.resetStyles();
	        
		
		HSSFWorkbook wb = new HSSFWorkbook();
		
		if (numberOfColumns > 250)
		{
			String sheetName = "ERROR Message";
			HSSFSheet sheet = wb.createSheet(sheetName);
			
			
			String[] errMsgs = {TranslatorWorker.translateText("The report has too many columns, please redo the report or export in an another format.", locale, siteId), 
								TranslatorWorker.translateText("Maximum supported number of columns: ", locale, siteId) + 250,
								TranslatorWorker.translateText("You demanded columns: ", locale, siteId) + numberOfColumns};
			
			for(int i = 0; i < errMsgs.length; i++)
			{
				HSSFRow row = sheet.createRow(i);
				HSSFCell cell = row.createCell(0);
				cell.setCellValue(errMsgs[i]);
			}
			
			sheet.autoSizeColumn(0);

			wb.write(response.getOutputStream());
			return null;
		}
		
		String sheetName=rd.getName();
		if(sheetName.length()>31) sheetName=sheetName.substring(0,31);
		sheetName = sheetName.replace('/', '_');
		sheetName = sheetName.replace('*', '_');
		sheetName = sheetName.replace('?', '_');
		sheetName = sheetName.replace(']', '_');
		sheetName = sheetName.replace('[', '_');
		sheetName = sheetName.replace('\\', '_');
		
		HSSFSheet sheet = wb.createSheet(sheetName);
		
		
		IntWrapper rowId=new IntWrapper();
		IntWrapper colId=new IntWrapper();
		
		HSSFRow row = sheet.createRow(rowId.intValue());
		//HSSFPatriarch patriarch = sheet.createDrawingPatriarch();	    
	    
		HSSFFooter footer = sheet.getFooter();
		footer.setRight( "Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages() );
			 

		if ( sorters != null && sorters.size() > 0 ) {
			rd.importLevelSorters(sorters,r.getHierarchies().size());
			rd.applyLevelSorter();
		}
		
		String plainReportParam = request.getParameter("plainReport");
		Boolean isPlainReport = plainReportParam != null ? Boolean.valueOf(plainReportParam): false;
		
		String publicPortalModeParam = request.getParameter("publicPortalMode");
		Boolean isPublicPortalMode = publicPortalModeParam != null ? Boolean.valueOf(publicPortalModeParam): false;

		GroupReportDataXLS grdx	= ARUtil.instatiateGroupReportDataXLS(request.getSession(), wb, sheet, row, rowId,
		        colId, null, rd, isPlainReport);
		grdx.setMetadata(r);
		grdx.setFilter(arf);
		colId.reset();
		if (isPublicPortalMode){
			rowId.reset();
			grdx.setMachineFriendlyColName(true);
		}else{
		//show title+desc+logo+statement
			grdx.makeColSpan(numberOfColumns, false);	
			rowId.inc();
			
			row = sheet.createRow(rowId.shortValue());
			grdx.createHeaderLogoAndStatement(request, reportForm, getServlet().getServletContext().getRealPath("/"));
			grdx.createHeaderNameAndDescription( request );
		}
		
		grdx.generate();
		//AMP-14423
		setColumnsWidth(sheet, numberOfColumns);

		/*
		 * 
		 * Commented until Apache POI fixes the following bug: 
		 *      https://issues.apache.org/bugzilla/show_bug.cgi?id=49188
		 *  
		 * Beware issue happens on large db's when generating a report with 
		 * a lot of info and columns (8000 rows, 50 columns).
		 * 
		 *  
		try{
			sheet.autoSizeColumn((short)0);
		}
		catch (ClassCastException e) {
			throw e;
		}
		*/
		rowId.inc();
		colId.reset();
		row=sheet.createRow(rowId.shortValue());
		HSSFCell cell=row.createCell(colId.shortValue());
		if(reportForm!=null && reportForm.getLogoOptions() !=null)
			if (reportForm.getLogoOptions().equals("0")) {//disabled
				// do nothing 
			} else if (reportForm.getLogoOptions().equals("1")) {//enabled																		 	                	                
				if (reportForm.getLogoPositionOptions().equals("0")) {//header
					// see startPage
				} else if (reportForm.getLogoPositionOptions().equals("1")) {//footer
					String path = getServlet().getServletContext().getRealPath("/");
					InputStream is = new FileInputStream(path + "/TEMPLATE/ampTemplate/images/AMPLogo.png");
					byte[] bytes = IOUtils.toByteArray(is);
				    int idImg = wb.addPicture(bytes,  HSSFWorkbook.PICTURE_TYPE_PNG);
				    
				    // ajout de l'image sur l'ancre ( lig, col )  
				    HSSFClientAnchor ancreImg = new HSSFClientAnchor();
				    ancreImg.setCol1(colId.shortValue());
				    ancreImg.setRow1(rowId.shortValue());
				    HSSFPicture Img = sheet.createDrawingPatriarch().createPicture( ancreImg,  idImg );			 
				    // redim de l'image
				    Img.resize();
				}				
			}
		if(reportForm!=null && reportForm.getStatementOptions() != null)
			if (reportForm.getStatementOptions().equals("0")) {//disabled
				// do nothing 
			} else 
				if (reportForm.getStatementOptions().equals("1")) {//enabled										
					if ((reportForm.getLogoOptions().equals("1")) && (reportForm.getLogoPositionOptions().equals("1"))) { 
						// creation d'une nouvelle cellule pour le statement	
						grdx.makeColSpan(rd.getTotalDepth(),false);	
						rowId.inc();
						colId.reset();
						row=sheet.createRow(rowId.shortValue());
						cell=row.createCell(colId.shortValue());						
					}
					String stmt = "";
					try {
						//TODO TRN: key is all right but if possible replace with default text. or delete this todo tag
						stmt = TranslatorWorker.translateText("This Report was created by AMP", locale,siteId);
					} catch (WorkerException e) {
					    e.printStackTrace();}
					stmt += " " + FeaturesUtil.getCurrentCountryName();
					if (reportForm.getDateOptions().equals("0")) {//disabled
						// no date
					} else if (reportForm.getDateOptions().equals("1")) {//enable		
						stmt += " " + DateFormat.getDateInstance(DateFormat.FULL, new java.util.Locale(locale)).format(new Date());
					}				 	                	                
					if (reportForm.getStatementPositionOptions().equals("0")) {//header		
						//
					} else if (reportForm.getStatementPositionOptions().equals("1")) {//footer
						cell.setCellValue(stmt);  
					}				
				}
		
	    wb.write(response.getOutputStream());
	    
		}else{
			Site site = RequestUtils.getSite(request);
			Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
			
			String siteId=site.getSiteId();
			String locale=navigationLanguage.getCode();
			
			session.setAttribute("sessionExpired", true);
			response.setContentType("text/html");
    		OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
    		PrintWriter out = new PrintWriter(outputStream, true);
    		String url = FeaturesUtil.getGlobalSettingValue("Site Domain");
    		String alert = TranslatorWorker.translateText("Your session has expired. Please log in again.",locale,siteId);
    		String script = "<script>opener.close();" 
    			+ "alert('"+ alert +"');" 
    			+ "window.location=('"+ url +"');"
    			+ "</script>";
    		out.println(script);
			out.close();	
			outputStream.close();
			return null;
		}

		return null;
	}
	
	private void setColumnsWidth(HSSFSheet sheet, int numberOfColumns){
		int maxWidth = 55;
		for(int i = 0; i <= numberOfColumns; i++){
			sheet.autoSizeColumn(i);
			if(sheet.getColumnWidth(i) > maxWidth * 256){
				sheet.setColumnWidth(i, maxWidth * 256);
			}
		}

	}
}
