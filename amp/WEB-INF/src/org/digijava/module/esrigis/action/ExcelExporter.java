package org.digijava.module.esrigis.action;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;

public class ExcelExporter extends Action{
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)throws java.lang.Exception {
			String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
	        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
	        
			String structures = request.getParameter("structures");
			JSONObject jsonobject = (JSONObject) new JSONTokener(structures).nextValue();
			JSONArray starray =  jsonobject.getJSONArray("Structures");
			try {
				HSSFWorkbook wb = new HSSFWorkbook();
				String titletrn = TranslatorWorker.translateText("Structures List", langCode, siteId);
		        HSSFSheet sheet = wb.createSheet(titletrn);
		        HSSFRow row = sheet.createRow(0);
		        HSSFCell cell = row.createCell(0);
		        HSSFRichTextString columtitle = new HSSFRichTextString(TranslatorWorker.translateText("Name", langCode, siteId));
		        cell.setCellValue(columtitle);
	            cell.setCellStyle(getRowHeadingSt(wb));
	            
	            cell = row.createCell(1);
	            columtitle = new HSSFRichTextString(TranslatorWorker.translateText("Type", langCode, siteId));
		        cell.setCellValue(columtitle);
	            cell.setCellStyle(getRowHeadingSt(wb));
	            
	            cell = row.createCell(2);
	            columtitle = new HSSFRichTextString(TranslatorWorker.translateText("Activity Name", langCode, siteId));
		        cell.setCellValue(columtitle);
	            cell.setCellStyle(getRowHeadingSt(wb));
	            
	            Integer i = new Integer(1);
		        for (Iterator iterator = starray.iterator(); iterator.hasNext();) {
		        	JSONObject json = (JSONObject) iterator.next();
		        	row = sheet.createRow(i);
		        	
		        	cell = row.createCell(0);
		        	columtitle = new HSSFRichTextString(json.get("Structure Name").toString());
		        	cell.setCellValue(columtitle);
		            cell.setCellStyle(getCellSt(wb));
		            
		            cell = row.createCell(1);
		            columtitle = new HSSFRichTextString(json.get("Structure Type").toString());
		        	cell.setCellValue(columtitle);
		            cell.setCellStyle(getCellSt(wb));
		            
		            cell = row.createCell(2);
		            columtitle = new HSSFRichTextString(getactivityname(json.get("Activity").toString()));
		        	cell.setCellValue(columtitle);
		            cell.setCellStyle(getCellSt(wb));
		            i++;
		        }
		        sheet.autoSizeColumn(0); 
		        sheet.autoSizeColumn(1);
		        sheet.autoSizeColumn(2);
		        sheet.autoSizeColumn(3); 
		        
		        response.setContentType("application/vnd.ms-excel");
		        response.setHeader("Content-disposition", "inline; filename=Structures.xls");
		        wb.write(response.getOutputStream());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	}
	
	private HSSFCellStyle getRowHeadingSt(HSSFWorkbook wb) {
		HSSFFont frowheading = wb.createFont();
		frowheading.setFontName("Arial Unicode MS");
		frowheading.setFontHeightInPoints((short) 8);
		frowheading.setBoldweight(frowheading.BOLDWEIGHT_BOLD);
		HSSFCellStyle rowheading = wb.createCellStyle();
		rowheading.setFont(frowheading);
		rowheading.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		rowheading.setBorderRight(HSSFCellStyle.BORDER_THIN);
		rowheading.setBorderTop(HSSFCellStyle.BORDER_THIN);
		rowheading.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		rowheading.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		return rowheading;
	}

	private HSSFCellStyle getCellSt(HSSFWorkbook wb) {
		HSSFFont fdataitem = wb.createFont();
		fdataitem.setFontName("Arial Unicode MS");
		fdataitem.setFontHeightInPoints((short) 8);
		fdataitem.setBoldweight(fdataitem.BOLDWEIGHT_NORMAL);
		HSSFCellStyle dataitem = wb.createCellStyle();
		dataitem.setFont(fdataitem);
		dataitem.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderRight(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataitem.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataitem.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
		return dataitem;
	}
	private String getactivityname(String url){
		String name = url.substring(url.indexOf(">")+1, url.lastIndexOf("<"));
		return name;
	}
}
