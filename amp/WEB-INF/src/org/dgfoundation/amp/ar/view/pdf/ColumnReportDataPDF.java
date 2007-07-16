/**
 * ColumnReportDataPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import java.util.ArrayList;
import java.util.Iterator;

import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;

import com.lowagie.text.Element;
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
public class ColumnReportDataPDF extends PDFExporter {

	/**
	 * @param parent
	 */
	public ColumnReportDataPDF(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param table
	 * @param item
	 * @param ownerId
	 */
	public ColumnReportDataPDF(PdfPTable table, Viewable item, Long ownerId) {
		super(table, item, ownerId);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		ColumnReportData columnReport = (ColumnReportData) item;

		Font titleFont = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.BOLD);
		//title
		if (columnReport.getParent() != null) {
			
			//introducing the translaton issues
			ReportData parent=(ReportData)columnReport.getParent();
			
			while (parent.getReportMetadata()==null)
			{
				parent=parent.getParent();
			}
			//when we get to the top of the hierarchy we have access to AmpReports
			
			//requirements for translation purposes
			TranslatorWorker translator=TranslatorWorker.getInstance();
			String siteId=parent.getReportMetadata().getSiteId();
			String locale=parent.getReportMetadata().getLocale();
			String prefix="rep:pop:";
			String translatedName=null;
			try{
				translatedName=TranslatorWorker.translate(prefix+columnReport.getName(),locale,siteId);
			}catch (WorkerException e)
				{System.out.println(e);}
			
			PdfPCell pdfc; 
			if(translatedName.compareTo("")==0)
				pdfc= new PdfPCell(new Paragraph(columnReport.getName(),titleFont));
			else 
				pdfc=new PdfPCell(new Paragraph(translatedName,titleFont));
			pdfc.setColspan(columnReport.getTotalDepth());
			table.addCell(pdfc);
		}
		
		
		// headings

		Font font = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD);

		if(columnReport.getGlobalHeadingsDisplayed().booleanValue()==false)  {
			PDFExporter.headingCells=new ArrayList();
			columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
		for (int curDepth = 0; curDepth <= columnReport.getMaxColumnDepth(); curDepth++) {
			Iterator i = columnReport.getItems().iterator();
			while (i.hasNext()) {
				
				Column col = (Column) i.next();
				col.setCurrentDepth(curDepth);
				int rowsp = col.getCurrentRowSpan();
				Iterator ii = col.getSubColumnList().iterator();
				
				if(ii.hasNext())
				while (ii.hasNext()) {
					Column element2 = (Column) ii.next();
					element2.setMaxNameDisplayLength(16);
					PdfPCell pdfc = new PdfPCell(new Paragraph(element2
							.getName(metadata.getHideActivities()),font));
					pdfc.setVerticalAlignment(Element.ALIGN_CENTER);
					pdfc.setColspan(element2.getWidth());
					table.addCell(pdfc);
					headingCells.add(pdfc);
				} else {
					PdfPCell pdfc = new PdfPCell(new Paragraph(""));
					pdfc.setColspan(col.getWidth());
					table.addCell(pdfc);
					headingCells.add(pdfc);
				}
			}
		}
		}

		// add data

		if (metadata.getHideActivities() == null
				|| metadata.getHideActivities().booleanValue() == false) {
			Iterator i = columnReport.getOwnerIds().iterator();
		while (i.hasNext()) {
			Long element = (Long) i.next();
			this.setOwnerId(element);
			Iterator ii = columnReport.getItems().iterator();
			while (ii.hasNext()) {
				Viewable velement = (Viewable) ii.next();
				velement.invokeExporter(this);
			}
		}
		}
		
		//add trail cells
		TrailCellsPDF trails=new TrailCellsPDF(this,columnReport);
		trails.generate();


	}

}