/**
 * ReportHeadingsXLS.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * @author mihai
 * @since 22.06.2007
 *
 */
public class ReportHeadingsXLS extends XLSExporter {

	/**
	 * @param parent
	 * @param item
	 */
	public ReportHeadingsXLS(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param wb
	 * @param sheet
	 * @param row
	 * @param rowId
	 * @param colId
	 * @param ownerId
	 * @param item
	 */
	public ReportHeadingsXLS(XSSFWorkbook wb, XSSFSheet sheet, XSSFRow row,
			IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		ColumnReportData columnReport = (ColumnReportData) item;
//		requirements for translation purposes
		String siteId=this.getMetadata().getSiteId();
		String locale=this.getMetadata().getLocale();
		boolean fundingReached = false;
		
		// column headings:
		if(columnReport.getGlobalHeadingsDisplayed().booleanValue()==false) {

		rowId.inc();
		colId.reset();
		
		columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
		for (int curDepth = 0; curDepth <= columnReport.getMaxColumnDepth(); curDepth++) {
			row = sheet.createRow(rowId.shortValue());
			
			XSSFCell cell1 =  this.getCell(row,this.getRegularStyle());
			cell1.setCellValue("");
			colId.inc();			
			Iterator i = columnReport.getItems().iterator();
			//int cellCount = 0;
			while (i.hasNext()) {
				Column col = (Column) i.next();
				col.setCurrentDepth(curDepth);
				int rowsp = col.getCurrentRowSpan();
				Iterator ii = col.getSubColumnList().iterator();
				if (ii.hasNext()){
					while (ii.hasNext()) {
						//cellCount++;
						Column element2 = (Column) ii.next();
						
						int medium = 0;
						int count = 0;
						Iterator ei = element2.getItems().iterator();
						while (ei.hasNext()) {
							Object o = (Object) ei.next();
							int length = o.toString().length();
							if (length > 10){ //we're only interested in cells that exceed our minimum cell length
								medium += length;
								count++;
							}
						}
						//medium += element2.getName().length();
						if (count > 0){
							medium = medium/count;
							medium += 5;	
						}
						
						if (!"-".equalsIgnoreCase(element2.getName(metadata.getHideActivities()))){
						
						XSSFCell cell =  this.getCell(row,this.getHighlightedStyle());
						XSSFCellStyle style = null;
						try{	
							style = cell.getCellStyle();
						}
						catch (ClassCastException ex) {
							
							throw ex;
						}
						style.setWrapText(true);
						cell.setCellStyle(style);
						String cellValue=element2.getName(metadata.getHideActivities());
						//if (rowId.value == 8){
						if (rowId.value < 10 && cellValue.length() > 0){
							//here we set the cell width
							//if (sheet.getColumnWidth((short)colId.value)<cellValue.length()*256){
								short val;
								if ((short)(medium*256) < 2560)
									val = 2560; //at least 10 chars
								else
									val = (short)(medium*256);
								/*
								if (rowId.value == 6 && !fundingReached){
									if (cellValue == "Funding")
										fundingReached = true;
									else
										val *=3;
								}
								*/
								if (colId.value == 0)
									sheet.setColumnWidth((short)colId.value, (short)10240);
								else
									sheet.setColumnWidth((short)colId.value, val);
							//}

						}
						//this value should be translated
						String translatedCellValue=new String();
						//String prefix="aim:reportBuilder:";
						
						try{
							translatedCellValue=TranslatorWorker.translateText(cellValue,locale,siteId);
						}catch (WorkerException e)
						{
							e.printStackTrace();
						}
						
						
						if(translatedCellValue.compareTo("")==0)
							cell.setCellValue(cellValue);
						else 
							cell.setCellValue(translatedCellValue);

						if(rowsp>1){
							makeRowSpan(rowsp-1,true);
							cell.setCellStyle(getSpanStyle(true));
						}
						
						if (element2.getWidth() > 1){
							makeColSpan(element2.getWidth(),true);
							cell.setCellStyle(getSpanStyle(true));
						}else{
							colId.inc();
						}
					}
					}		
				}
				else {
					//add padding cells before creating a colspan here. Making a colspan before these cells are created 
					//will make the borders look ugly
					if (!"-".equalsIgnoreCase(col.getName(metadata.getHideActivities()))){
						if(col.getWidth()>1) {
							for(int k=0;k<col.getWidth();k++) {
								XSSFCell cell = row.getCell(colId.intValue()+k);
								if(cell==null) cell=row.createCell(colId.intValue()+k);
								XSSFCellStyle cellstyle = wb.createCellStyle();
								cellstyle.cloneStyleFrom(this.getHighlightedStyle());
								cell.setCellStyle(cellstyle);
							}
						}

					 if(col.getWidth()==1) makeColSpan(col.getWidth(),true);
					}
				}
			}
			rowId.inc();
			colId.reset();
		}
		}

	}

}
