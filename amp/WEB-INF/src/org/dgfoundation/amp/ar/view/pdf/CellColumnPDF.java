/**
 * CellColumnPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.Cell;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 *
 */
public class CellColumnPDF extends PDFExporter {

	public CellColumnPDF(Exporter parent,Viewable item) {
		super(parent,item);
	}
	
	/**
	 * @param table
	 * @param item
	 */
	public CellColumnPDF(PdfPTable table, Viewable item,Long ownerId) {
		super(table, item,ownerId);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.view.pdf.PDFExporter#generate()
	 */
	public void generate() {
		CellColumn col=(CellColumn) item;
		 Cell c=col.getByOwner(ownerId);
		 if(c!=null)
		 c.invokeExporter(this);
		 else table.addCell(new PdfPCell(new Paragraph(" ",new Font(Font.FontFamily.COURIER, 10))));
	}

}
