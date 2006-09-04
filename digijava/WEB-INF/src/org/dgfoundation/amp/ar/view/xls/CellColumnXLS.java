/**
 * CellColumnXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.Cell;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 *
 */
public class CellColumnXLS extends XLSExporter {

	/**
	 * @param parent
	 * @param item
	 */
	public CellColumnXLS(Exporter parent, Viewable item) {
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
	public CellColumnXLS(HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
			IntWrapper colId, Long ownerId, Viewable item) {
		super(sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		CellColumn col=(CellColumn) item;
		 Cell c=col.getByOwner(ownerId);		 
		 if(c!=null)
		 c.invokeExporter(this);
		 else {
			 HSSFCell cell=row.createCell(colId.shortValue());
			 cell.setCellValue(" ");
			 colId.inc();
		 }
	}

}
