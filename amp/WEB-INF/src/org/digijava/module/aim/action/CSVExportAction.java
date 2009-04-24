/**
 * CSVExportAction.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *
 */
package org.digijava.module.aim.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * CSV Export is actually using the XLS export API. The difference is only at
 * the output processing where XLS is simply converted to CSV
 *
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 7, 2006
 *
 */
public class CSVExportAction
    extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws java.lang.
      Exception {

    GroupReportData rd = ARUtil.generateReport(mapping, form, request,
                                               response);

    rd.setCurrentView(GenericViews.XLS);

    HttpSession session = request.getSession();
    
    AmpARFilter arf=(AmpARFilter) session.getAttribute(ArConstants.REPORTS_FILTER);
	
	if (session.getAttribute("currentMember")!=null || arf.isPublicView()){
	    AmpReports r = (AmpReports) session.getAttribute("reportMeta");
	
	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-Disposition",
	                       "inline; filename=data.csv ");
	
	    HSSFWorkbook wb = new HSSFWorkbook();
	
	    String sheetName = rd.getName();
	    if (sheetName.length() > 31)
	      sheetName = sheetName.substring(0, 31);
	
	    HSSFSheet sheet = wb.createSheet(getCleanName(sheetName));
	
	    IntWrapper rowId = new IntWrapper();
	    IntWrapper colId = new IntWrapper();
	
	    HSSFRow row = sheet.createRow(rowId.intValue());
	
	    GroupReportDataXLS grdx = new GroupReportDataXLS(wb, sheet, row, rowId,
	        colId, null, rd);
	
	    grdx.setMetadata(r);
	
	    String sortBy = (String) session.getAttribute("sortBy");
	    if (sortBy != null)
	      rd.setSorterColumn(sortBy);
	
	    //show title+desc
	    rowId.inc();
	    colId.reset();
	    row = sheet.createRow(rowId.shortValue());
	    HSSFCell cell = row.createCell(colId.shortValue());
	
	    Site site = RequestUtils.getSite(request);
	    Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
	
	    Long siteId=site.getId();
	    String locale=navigationLanguage.getCode();	
	    
	    String translatedNotes = "";
	    if (FeaturesUtil.getGlobalSettingValue("Amounts in Thousands").equalsIgnoreCase("true")){
	    	translatedNotes = TranslatorWorker.translateText("Amounts are in thousands (000)", locale, siteId);
	    }
		if ("".equalsIgnoreCase(translatedNotes)) {
		    translatedNotes = AmpReports.getNote(session);
		}
	    
		
	    cell.setCellValue(translatedNotes + "\n");
	    
	    grdx.makeColSpan(rd.getTotalDepth(),false);
	    rowId.inc();
	    colId.reset();
	
	    row = sheet.createRow(rowId.shortValue());
	    cell = row.createCell(colId.shortValue());
	    
	  	
		String translatedReportName="Report Name:";
		String translatedReportDescription="Description:";
		try{	
			translatedReportName=TranslatorWorker.translateText("Report Name:",locale,siteId);
			translatedReportDescription=TranslatorWorker.translateText("Description:",locale,siteId);
		}catch (WorkerException e){;}
	
	    cell.setCellValue(translatedReportName+": " + r.getName());
	
	    grdx.makeColSpan(rd.getTotalDepth(),false);
	    rowId.inc();
	    colId.reset();
	
	    row = sheet.createRow(rowId.shortValue());
	    cell = row.createCell(colId.shortValue());
	    cell.setCellValue(translatedReportDescription+": " + r.getReportDescription());
	
	    grdx.makeColSpan(rd.getTotalDepth(),false);
	    rowId.inc();
	    colId.reset();
	
	    grdx.generate();
	    
	    // we now iterate the rows and create the csv
	
	    StringBuffer sb = new StringBuffer();
	    Iterator i = sheet.rowIterator();
	    while (i.hasNext()) {
	      HSSFRow crow = (HSSFRow) i.next();
	      for (short ii = crow.getFirstCellNum(); ii <= crow.getLastCellNum(); ii++) {
	        HSSFCell ccell = crow.getCell(ii);
	        String s = "";
	        if (ccell != null) {
	          if (ccell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
	            s = Double.toString(ccell.getNumericCellValue());
	          else
	            s = ccell.getStringCellValue();
	          sb.append("\"").append(s).append("\"");
	
	          if (ii < crow.getLastCellNum())
	            sb.append(",");
	        }
	      }
	      sb.append("\n");
	
	    }
	    PrintWriter out = new PrintWriter(new OutputStreamWriter(response.
	        getOutputStream(), "UnicodeLittle"), true);
	
	    out.println(sb.toString());
	
	    out.close();
	}else{
		
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		
		Long siteId=site.getId();
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

  
  private String getCleanName(String name){
	  name=name.replaceAll("/", "_");
	  name=name.replace("\\\\", "_");
	  name=name.replaceAll("\\*", "_");
	  name=name.replaceAll("\\?", "_");
	  name=name.replaceAll("\\[", "_");
	  name=name.replaceAll("\\]", "_");
	  return name;
  }
}
