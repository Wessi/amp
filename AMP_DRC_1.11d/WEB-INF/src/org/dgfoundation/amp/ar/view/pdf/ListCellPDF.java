/**
 * ListCellPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ListCell;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 *
 */
public class ListCellPDF extends PDFExporter {

	public ListCellPDF(Exporter parent,Viewable item) {
		super(parent,item);
	}
	
	/**
	 * @param table
	 * @param item
	 */
	public ListCellPDF(PdfPTable table, Viewable item,Long ownerId) {
		super(table, item,ownerId);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.view.pdf.PDFExporter#generate()
	 */
	public void generate() {
		ListCell lc=(ListCell) item;
		List items=(List) lc.getValue();
		String res="";
		
		Iterator i=items.iterator();		
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			res+=element.toString();
			if(i.hasNext()) res+=", ";
		}
		PdfPCell pdfc = new PdfPCell(new Paragraph(res,new Font(Font.COURIER, 10)));
		table.addCell(pdfc);
	}

}