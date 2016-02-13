/**
 * GroupReportDataXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import java.awt.Color;
import java.util.Iterator;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.Viewable;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 29, 2006
 *
 */
public class GroupReportDataPDF extends PDFExporter {

    	private static Color alternateColorA=new Color(185,219,243);
    	private static Color alternateColorB=new Color(0,156,205);
    	private static Color lastedUsedColor=null;
    	/**
	 * @param parent
	 */
	public GroupReportDataPDF(Exporter parent,Viewable item) {
		super(parent,item);
	}

	/**
	 * @param table
	 * @param item
	 * @param ownerId
	 */
	public GroupReportDataPDF(PdfPTable table, Viewable item, Long ownerId) {
		super(table, item, ownerId);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
	  	GroupReportData grd=(GroupReportData) item;
		Font titleFont;
		 titleFont = new Font(Font.COURIER, 13, Font.BOLD);
				
		if((grd.getParent()!=null)&&(!grd.getName().equalsIgnoreCase(grd.getParent().getName()))) {
		    	PdfPCell pdfc = new PdfPCell(new Paragraph(grd.getName(),titleFont));
			pdfc.setColspan(grd.getTotalDepth());
			pdfc.setPaddingTop(2);
			pdfc.setPaddingBottom(2);
			if (lastedUsedColor!=alternateColorA){
			    pdfc.setBackgroundColor(alternateColorA);
			    lastedUsedColor=alternateColorA;
			}else{
			    pdfc.setBackgroundColor(alternateColorB);    
			}	
			table.addCell(pdfc);
		}
		Iterator i=grd.getItems().iterator();
		while (i.hasNext()) {
		    	Viewable element = (Viewable) i.next();
			element.invokeExporter(this);			
		
		}

		//add trail cells
		TrailCellsPDF trails=new TrailCellsPDF(this,grd);
		trails.generate();

		//add an empty row
		PdfPCell pdfc2 = new PdfPCell(new Paragraph(" "));
		pdfc2.setColspan(grd.getTotalDepth());
		table.addCell(pdfc2);
	}

	
}