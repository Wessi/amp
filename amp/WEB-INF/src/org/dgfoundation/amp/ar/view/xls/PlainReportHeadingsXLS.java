/**
 * PlainReportHeadingsXLS.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;

/**
 * @author mmoras
 *
 */
public class PlainReportHeadingsXLS extends ReportHeadingsXLS {

	/**
	 * @param parent
	 * @param item
	 */
	public PlainReportHeadingsXLS(Exporter parent, Viewable item) {
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
	public PlainReportHeadingsXLS(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row,
			IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createHierarchyHeaderCell (int curDepth) {
		ColumnReportData columnReport = (ColumnReportData) item;
		Integer rowSpan = columnReport.getMaxColumnDepth();
			
			if ( this.getMetadata().getHierarchies() != null &&
					this.getMetadata().getHierarchies().size() > 0) {
				boolean first = true;
				for (AmpReportHierarchy arh: this.getMetadata().getHierarchies() ) {
					String colName			= arh.getColumn().getColumnName();
					
					String translColName	= getColumnDisplayName(colName);
					
					if ( translColName != null && translColName.trim().length() > 0 ) {
						if(first){
							first = false;
						}else{
							colId.inc();
						}
						if (curDepth == 0) {
							HSSFCell cell1 =  this.getCell(row,this.getHighlightedStyle());
							cell1.setCellValue( translColName.trim() );
							makeRowSpan(rowSpan, true);
						}else{
							HSSFCell cell1 =  this.getCell(row,this.getHighlightedStyle());
							cell1.setCellValue("");
						}
					}					
				}
			}

	}
	
	@Override
	protected void createHeadingBorders (int curDepth) {		
		ColumnReportData columnReport = (ColumnReportData) item;
		if (curDepth == 0) {
			int maxRowSpan		= 1;
			Boolean summaryReport		= this.getMetadata().getHideActivities();
			if (  summaryReport == null  || !summaryReport ) {
				Column tempCol			= (Column)columnReport.getItems().get(0);
				maxRowSpan				= (tempCol.getRowSpan()>maxRowSpan)?tempCol.getRowSpan():maxRowSpan;
                                    if(maxRowSpan>1) maxRowSpan-=1;
			}
			else {
				Iterator<Column> colIter	= columnReport.getItems().iterator();
				while ( colIter.hasNext() ) {
					Column tempCol	= colIter.next();
					maxRowSpan		= (tempCol.getCurrentRowSpan()>maxRowSpan)?tempCol.getCurrentRowSpan():maxRowSpan;
				}
				if ( maxRowSpan > 3 ) maxRowSpan = 3;
                                    
			}
			makeRowSpan(maxRowSpan,true);
			sheet.setColumnWidth((short)colId.value, (short)5120);
		}
	}

}
