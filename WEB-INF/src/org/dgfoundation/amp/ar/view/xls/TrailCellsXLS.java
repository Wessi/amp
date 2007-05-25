/**
 * TrailCellsXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.Cell;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public class TrailCellsXLS extends XLSExporter {

	/**
	 * @param parent
	 * @param item
	 */
	public TrailCellsXLS(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param sheet
	 * @param row
	 * @param rowId
	 * @param colId
	 * @param ownerId
	 * @param item
	 */
	public TrailCellsXLS(HSSFWorkbook wb ,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
			IntWrapper colId, Long ownerId, Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		// generate totals:
		ReportData grd = (ReportData) item;

		row=sheet.createRow(rowId.shortValue());
		
		if (grd.getParent() != null) {
			HSSFCell cell = this.getCell(this.getHighlightedStyle(false));
			cell.setCellValue("TOTALS FOR " + grd.getName());
			
			makeColSpan(grd.getSourceColsCount().intValue());
			//colId.inc();
			Iterator i = grd.getTrailCells().iterator();
			while (i.hasNext()) {
				Cell element = (Cell) i.next();
				element.invokeExporter(this);
			}

		}
		colId.reset();

	}

}
