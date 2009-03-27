package org.digijava.module.gis.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.gis.dbentity.GisMapSegment;
import org.digijava.module.gis.util.ColorRGB;
import org.digijava.module.gis.util.CoordinateRect;
import org.digijava.module.gis.util.FundingData;
import org.digijava.module.gis.util.GisUtil;
import org.digijava.module.gis.util.HilightData;
import org.digijava.module.gis.util.SegmentData;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiColumn;
import org.digijava.module.widget.table.WiRow;
import org.digijava.module.widget.table.WiTable;
import org.digijava.module.widget.table.filteredColumn.WiColumnDropDownFilter;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfWriter;

public class PDFExportAction extends Action implements PdfPageEvent {
	protected static Logger logger = Logger.getLogger(PDFExportAction.class);
	private HttpSession session = null;
	private String locale = null;
	private Site site = null;
	private Locale navigationLanguage = null;

	private PdfPTable contenTable;
	private HttpServletResponse response;

	private Long[] tableId = null;
	private Long[] columnId = null;
	private Long[] itemId = null;
	private String siteId;

	public PDFExportAction(HttpSession session, String locale, Site site,
			HttpServletResponse response) {
		this.session = session;
		this.locale = locale;
		this.site = site;
		this.response = response;
	}

	public PDFExportAction() {
		super();
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		// for translation purposes
		this.site = RequestUtils.getSite(request);
		this.navigationLanguage = RequestUtils.getNavigationLanguage(request);

		this.siteId = site.getSiteId();
		this.locale = navigationLanguage.getCode();

		HttpSession session = request.getSession();

		Rectangle page = PageSize.A4;
		Document document = new Document();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);

		//This sets up default values for options in the dashboard
		Long selectedDonor = null;
		Integer selectedFromYear = null;
		Integer selectedTotYear = null;
		Boolean showLabels = true;
		Boolean showLegends = true;
		String selectedDonorName = "";

		//Breakdown by sector parameters

		if (request.getParameter("selectedDonor") != null&& !request.getParameter("selectedDonor").equals("-1")) {
			selectedDonor = Long.parseLong(request.getParameter("selectedDonor"));
		}
		if (request.getParameter("selectedDonorName") != null	&& !request.getParameter("selectedDonorName").equals("-1")) {
			selectedDonorName = request.getParameter("selectedDonorName");
		}
		if (request.getParameter("selectedFromYear") != null&& !request.getParameter("selectedFromYear").equals("-1")) {
			selectedFromYear = Integer.parseInt(request.getParameter("selectedFromYear"));
		}
		if(request.getParameter("selectedToYear") != null && !request.getParameter("selectedToYear").equals("-1")){
			selectedTotYear = Integer.parseInt(request.getParameter("selectedToYear"));
		}
		if (request.getParameter("showLabels") != null && !request.getParameter("showLabels").equals("-1")) {
			showLabels = Boolean.parseBoolean(request.getParameter("showLabels"));
		}
		if (request.getParameter("showLegends") != null	&& !request.getParameter("showLegends").equals("-1")) {
			showLegends = Boolean.parseBoolean(request.getParameter("showLegends"));
		}

		ByteArrayOutputStream outChartByteArray = getChartImage(request,selectedDonor, selectedFromYear,selectedTotYear, showLegends, showLabels);
		Image imgChart = Image.getInstance(outChartByteArray.toByteArray());

		//GIS Map parameters
		Long secId = null;
		Long indId = null;
		if (request.getParameter("sectorId") != null&& !request.getParameter("sectorId").equals("-1")) {
			secId = Long.parseLong(request.getParameter("sectorId"));
		}
		if (request.getParameter("indicatorId") != null	&& !request.getParameter("indicatorId").equals("-1")) {
			indId = Long.parseLong(request.getParameter("indicatorId"));
		}

		//Widgets parameters (selected filters)
		if (request.getParameter("tableId") != null
				&& !request.getParameter("tableId").equals("-1")) {
			String tableStr = request.getParameter("tableId");
			String tableIdStr[] = tableStr.split(",");
			this.tableId = new Long[tableIdStr.length];

			int tableIdIndex = 0;
			for(String str : tableIdStr)
			{
				this.tableId[tableIdIndex] = Long.parseLong(str);
				tableIdIndex++;
			}
		}
		if (request.getParameter("columnId") != null&& !request.getParameter("columnId").equals("-1")) {

			String columnStr = request.getParameter("columnId");
			String columnIdStr[] = columnStr.split(",");
			this.columnId = new Long[columnIdStr.length];

			int columnIdIndex = 0;
			for(String str : columnIdStr)
			{
				this.columnId[columnIdIndex] = Long.parseLong(str);
				columnIdIndex++;
			}

		}
		if (request.getParameter("itemId") != null&& !request.getParameter("itemId").equals("-1")) {
			String itemStr = request.getParameter("itemId");
			String itemIdStr[] = itemStr.split(",");
			this.itemId = new Long[itemIdStr.length];

			int itemIdIndex = 0;
			for(String str : itemIdStr)
			{
				this.itemId[itemIdIndex] = Long.parseLong(str);
				itemIdIndex++;
			}
		}


		//Check for sector and indicator
		ByteArrayOutputStream outMapByteArray;
		String indicatorName = "None";
		String sectorName = "None";

		String mapCode = request.getParameter("mapCode");

		if(secId != null && indId != null){
			AmpIndicator indicator = IndicatorUtil.getIndicator(indId);
			indicatorName = indicator.getName();
			sectorName = SectorUtil.getAmpSector(secId).getName();
            if (mapCode != null && mapCode.trim().length() > 0) {
    			outMapByteArray = getMapImageSectorIndicator(mapCode, secId, indId);
            } else {
    			outMapByteArray = getMapImageSectorIndicator("TZA", secId, indId);
            }

		}
		else
		{
            if (mapCode != null && mapCode.trim().length() > 0) {
    			outMapByteArray = getMapImage(mapCode);
            } else {
    			outMapByteArray = getMapImage("TZA");
            }
		}

		// Get the Map Image
		// GIS, this
		// should
		// come
		// through
		// parameter
		Image imgMap = Image.getInstance(outMapByteArray.toByteArray());

		// put the Chart and map on a table
		float[] widths1 = { 2f, 1f };
		PdfPTable imagesTable = new PdfPTable(widths1);
		imagesTable.setWidthPercentage(100);
		imagesTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		imagesTable.addCell(getImageMap(imgMap, sectorName, indicatorName));
		imagesTable.addCell(getImageChart(imgChart, selectedDonorName, selectedFromYear));
//		imagesTable.addCell(" ");

		// First batch of widgets
		float[] layoutWidths = { 2f, 1f };
		PdfPTable layoutTable1 = new PdfPTable(layoutWidths);
		layoutTable1.setExtendLastRow(false);
		layoutTable1.setWidthPercentage(100);
		layoutTable1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		PdfPTable resourcesBox = getResourcesBox();
		PdfPTable mdgsBox = getMDGSBox();

		layoutTable1.addCell(mdgsBox);
		layoutTable1.addCell(resourcesBox);

		layoutTable1.addCell(" ");
		layoutTable1.addCell(" ");

		PdfPTable aeProcessIndicatorBox = getAEPIBox();
		PdfPCell tempCell = new PdfPCell(aeProcessIndicatorBox);
		tempCell.setColspan(2);
		layoutTable1.addCell(tempCell);

		layoutTable1.addCell(" ");
		layoutTable1.addCell(" ");

		PdfPTable IOBox = getIntermediateOutputBox();
		tempCell = new PdfPCell(IOBox);
		tempCell.setColspan(2);
		tempCell.setBorder(Rectangle.NO_BORDER);
		tempCell.setPaddingBottom(10);
		layoutTable1.addCell(tempCell);


		PdfPTable totalResourcesBox = getTotalResourcesBox();
		tempCell = new PdfPCell(totalResourcesBox);
		tempCell.setColspan(2);
		tempCell.setBorder(Rectangle.NO_BORDER);
		layoutTable1.addCell(tempCell);

		layoutTable1.addCell(" ");
		layoutTable1.addCell(" ");

		PdfPTable EAResourcesBox = getEAResourcesBox();
		tempCell = new PdfPCell(EAResourcesBox);
		tempCell.setColspan(2);
		tempCell.setBorder(Rectangle.NO_BORDER);
		tempCell.setPaddingBottom(10);
		layoutTable1.addCell(tempCell);

		document.open();
		String countryName = "";
        String ISO = null;
        Iterator itr1 = FeaturesUtil.getDefaultCountryISO().iterator();
        while (itr1.hasNext()) {
          AmpGlobalSettings ampG = (AmpGlobalSettings) itr1.next();
          ISO = ampG.getGlobalSettingsValue();
        }

        if(ISO != null && !ISO.equals("")){
            Country cntry = DbUtil.getDgCountry(ISO);
            countryName = cntry.getCountryName();
        }
        else
        {
        	countryName = "";
        }
        //Translation for Result Matrix
        //Paragraph title = new Paragraph(TranslatorWorker.translate("gis:resultsmatrix", locale, siteId) + countryName, new Font(Font.HELVETICA, 24, Font.BOLD));
        Paragraph title = new Paragraph(TranslatorWorker.translateText("Results Matrix:", locale, siteId) + countryName, new Font(Font.HELVETICA, 24, Font.BOLD));
        
        String generatedOnTranslation = TranslatorWorker.translateText("gis:generatedon", locale, siteId);
        if(generatedOnTranslation == null || generatedOnTranslation.equals(""))
        	generatedOnTranslation = "Generated on: ";

		Paragraph updateDate = new Paragraph(generatedOnTranslation
				+ FormatHelper.formatDate(new Date(System.currentTimeMillis()))
				+ "\n\n", new Font(Font.HELVETICA, 6, Font.BOLDITALIC));

		document.add(title);
		document.add(updateDate);
		document.add(imagesTable);
		document.add(layoutTable1);

		document.close();
		response.setContentType("application/pdf");
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		return null;
	}

	private PdfPTable getIntermediateOutputBox() throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = {1f,3f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);

		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText("Output Indicators", locale, siteId)+"\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.setBorder(Rectangle.NO_BORDER);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));

		float[] layoutExAidResourcesWidths = { 1f, 1f };
		PdfPTable layoutExAidResources = new PdfPTable(
				layoutExAidResourcesWidths);
		layoutExAidResources.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutExAidResources.setWidthPercentage(100);

		PdfPTable table1ExAidResources = getWidgetTable("table_place4");
		layoutExAidResources.addCell(table1ExAidResources);

		layoutExAidResources.addCell(" ");
		layoutExAidResources.addCell(new Paragraph(TranslatorWorker.translateText("Source: Official government sources", locale, siteId), new Font(Font.HELVETICA, 6)));

		layoutExAidResources.addCell(" ");

		layoutCell.addElement(layoutExAidResources);
		generalBox.addCell(layoutCell);


		return generalBox;
	}


	private PdfPTable getTotalResourcesBox() throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = {1f,3f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText("Total resources", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.setBorder(Rectangle.NO_BORDER);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));

		float[] layoutTotalResourcesWidths = {1f };
		PdfPTable layoutTotalResources = new PdfPTable(
				layoutTotalResourcesWidths);
		layoutTotalResources.setWidthPercentage(100);
		layoutTotalResources.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		PdfPTable tableTotalResources1 = getWidgetTable("table_place6");
		layoutTotalResources.addCell(tableTotalResources1);
//		PdfPTable tableTotalResources2 = getWidgetTable("table_place6");
//		layoutTotalResources.addCell(tableTotalResources2);

		layoutTotalResources.addCell(new Paragraph(TranslatorWorker.translateText("Source: Ministry of Finance", locale, siteId), new Font(Font.HELVETICA, 6)));

		layoutCell.addElement(layoutTotalResources);
		generalBox.addCell(layoutCell);


		return generalBox;
	}

	private PdfPTable getAEPIBox() throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = {1f,3f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);

		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText("Aid Effectiveness Process Indicators", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setPadding(0);
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(0);
		lineCell.setBorder(Rectangle.NO_BORDER);
		layoutCell.setBackgroundColor(new Color(206,226,251));

		float[] layoutAEIndicatorsWidths = { 1f, 1f };
		PdfPTable layoutAEIndicators = new PdfPTable(layoutAEIndicatorsWidths);
		layoutAEIndicators.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutAEIndicators.setWidthPercentage(100);

		PdfPTable table1AEIndicators = getWidgetTable("table_place1");
		PdfPTable table2AEIndicators = getWidgetTable("table_place2");

		layoutAEIndicators.addCell(table1AEIndicators);
		layoutAEIndicators.addCell(table2AEIndicators);
		layoutAEIndicators.addCell(new Paragraph(TranslatorWorker.translateText("Source: 2006 Paris Declaration Survey", locale, siteId) , new Font(Font.HELVETICA, 6)));
		layoutAEIndicators.addCell(" ");



		layoutCell.addElement(layoutAEIndicators);


		generalBox.addCell(layoutCell);


		return generalBox;
	}

	private PdfPTable getEAResourcesBox() throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = {1f,3f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);

		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText("External Aid Resources", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.setBorder(Rectangle.NO_BORDER);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));

		float[] layoutExAidResourcesWidths = { 1f, 1f };
		PdfPTable layoutExAidResources = new PdfPTable(layoutExAidResourcesWidths);
		layoutExAidResources.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutExAidResources.setWidthPercentage(100);

		PdfPTable table1ExAidResources = getWidgetTable("table_place5");
		layoutExAidResources.addCell(table1ExAidResources);

		layoutExAidResources.addCell(" ");
		//widget:sourceAMPdatabase
		layoutExAidResources.addCell(new Paragraph(TranslatorWorker.translateText("Source: AMP database", locale, siteId), new Font(Font.HELVETICA, 6)));

		layoutExAidResources.addCell(" ");

		layoutCell.addElement(layoutExAidResources);
		generalBox.addCell(layoutCell);


		return generalBox;
	}

	private PdfPTable getImageChart(Image imgChart, String selectedDonorName, Integer selectedYear) throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = {2f,1f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		//gis:breakdownbysector

		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText("Breakdown by sector", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));
		layoutCell.addElement(imgChart);

		PdfPCell textCell = new PdfPCell();
		textCell.setPadding(2);
		textCell.setBackgroundColor(new Color(206,226,251));
		//widget:piechart:allAmountsin000USD
		String selectedDonorTranslation = TranslatorWorker.translateText("Selected donor", locale, siteId);
		if(selectedDonorTranslation == null || selectedDonorTranslation.equals(""))
			selectedDonorTranslation = "Selected donor";
		String selectedYearTranslation = TranslatorWorker.translateText("Selected year", locale, siteId);
		if(selectedYearTranslation == null || selectedYearTranslation.equals(""))
			selectedYearTranslation = "Selected year";

		textCell.addElement(new Paragraph(TranslatorWorker.translateText("All amounts in 000s of USD", locale, siteId) + "\n" + selectedDonorTranslation + ": " + selectedDonorName + "\n" + selectedYearTranslation + ": " + selectedYear + "\n\n", new Font(Font.HELVETICA, 6)));

		generalBox.addCell(textCell);
		PdfPCell text2Cell = new PdfPCell();
		text2Cell.setPadding(2);
		text2Cell.setBackgroundColor(new Color(206,226,251));
		//widget:SourceAmpdatabase


		text2Cell.addElement(new Paragraph(TranslatorWorker.translateText("Source: AMP database", locale, siteId), new Font(Font.HELVETICA, 6)));

		generalBox.addCell(layoutCell);
		generalBox.addCell(text2Cell);


		return generalBox;
	}

	private PdfPTable getImageMap(Image imgMap, String sectorName, String indicatorName) throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = {1f,2f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		//gis:regionalview

		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText("Regional View", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));
		layoutCell.addElement(imgMap);
		generalBox.addCell(layoutCell);

		PdfPCell textCell = new PdfPCell();
		textCell.setPadding(2);
		textCell.setBackgroundColor(new Color(206,226,251));
		//gis:minmax:message
		//TODO TRN: no record for this key.
		String selectedSectorTranslation = TranslatorWorker.translateText("gis:selectedSector", locale, siteId);

		if(selectedSectorTranslation == null || selectedSectorTranslation.equals(""))
			selectedSectorTranslation = "Selected sector";
		//TODO TRN: no record for this key
		String selectedIndicatorTranslation = TranslatorWorker.translateText("gis:selectedIndicator", locale, siteId);
		if(selectedIndicatorTranslation == null || selectedIndicatorTranslation.equals(""))
			selectedIndicatorTranslation = "Selected Indicator";

		String defaultMinMaxMessage="Regions with the lowest (MIN) values for the selected indicator are shaded dark green. "
				+"Regions with the highest (MAX) value are shaded light green. " 
				+"For some indicators (such as mortality rates), having the MAX value indicates the lowest performance";
		
		textCell.addElement(new Paragraph(TranslatorWorker.translateText(defaultMinMaxMessage, locale, siteId) + "\n" + selectedSectorTranslation + ": " + sectorName + "\n" + selectedIndicatorTranslation + ": " + indicatorName + "\n\n"+TranslatorWorker.translateText("Data Source: Dev Info", locale, siteId), new Font(Font.HELVETICA, 6)));
		PdfPCell legendCell = new PdfPCell();
		legendCell.setPadding(0);
		legendCell.setBorder(Rectangle.NO_BORDER);
		try {
			Image image = Image.getInstance(this.getServlet().getServletContext().getRealPath("/repository/gis/view/images/fundingLegend.png"));
//			image.scaleAbsoluteWidth(320f);
			image.setAlignment(Image.ALIGN_RIGHT);
//			image.scaleAbsoluteHeight(20f);
			legendCell.addElement(image);
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		generalBox.addCell(legendCell);
		generalBox.addCell(textCell);



		return generalBox;
	}

	private PdfPTable getMDGSBox() throws WorkerException {

		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = {1f,2f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		//gis:millenniumdevelopmentgoals

		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText("Millennium Development Goals", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));

		float[] chartsWidths = { 1f, 1f, 1f };
		PdfPTable layoutCharts = new PdfPTable(chartsWidths);
		layoutCharts.getDefaultCell().setPadding(1);
		layoutCharts.setExtendLastRow(false);
		layoutCharts.setWidthPercentage(100f);


		try {
			ByteArrayOutputStream chart_place1 = getWidgetImage("chart_place1");
			if (chart_place1 != null)
				layoutCharts.addCell(Image.getInstance(chart_place1.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place2 = getWidgetImage("chart_place2");
			if (chart_place2 != null)
				layoutCharts.addCell(Image.getInstance(chart_place2.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place3 = getWidgetImage("chart_place3");
			if (chart_place3 != null)
				layoutCharts.addCell(Image.getInstance(chart_place3.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place4 = getWidgetImage("chart_place4");
			if (chart_place4 != null)
				layoutCharts.addCell(Image.getInstance(chart_place4.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place5 = getWidgetImage("chart_place5");
			if (chart_place5 != null)
				layoutCharts.addCell(Image.getInstance(chart_place5.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place6 = getWidgetImage("chart_place6");
			if (chart_place6 != null)
				layoutCharts.addCell(Image.getInstance(chart_place6.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PdfPCell legendCell = new PdfPCell();
		legendCell.setPadding(0);
		legendCell.setBorder(Rectangle.NO_BORDER);
		try {
			Image image = Image.getInstance(this.getServlet().getServletContext().getRealPath("/TEMPLATE/ampTemplate/images/legend1.jpg"));
			image.scaleAbsoluteWidth(50f);
			image.setAlignment(Image.ALIGN_RIGHT);
//			image.scaleAbsoluteHeight(20f);
			legendCell.addElement(image);
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//
		PdfPCell legendTextCell = new PdfPCell();
		legendTextCell.setPadding(0);
		legendTextCell.setBorder(Rectangle.NO_BORDER);
		//widget:SourceOfficialgovernmentsources

		legendTextCell.addElement(new Paragraph(TranslatorWorker.translateText("Source: Official government sources", locale, siteId), new Font(Font.HELVETICA, 6)));

		PdfPTable legendTable = new PdfPTable(1);
		legendTable.setWidthPercentage(100f);
		legendTable.addCell(legendCell);
		legendTable.addCell(legendTextCell);
		PdfPCell legendCellLayout = new PdfPCell();
		legendCellLayout.setColspan(3);
		legendCellLayout.addElement(legendTable);
		layoutCharts.addCell(legendCellLayout);
		layoutCell.addElement(layoutCharts);

		generalBox.addCell(layoutCell);

		return generalBox;
	}

	private PdfPTable getResourcesBox() throws WorkerException {

		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = {2f,1f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		//gis:resourcesatglance

		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText("Resources at a glance", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);

		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));

		float[] resourcesAtAGlanceWidths = { 2f, 1f };
		PdfPTable layoutResourcesAtAGlance = new PdfPTable(
				resourcesAtAGlanceWidths);
		layoutResourcesAtAGlance.setWidthPercentage(100f);
		layoutResourcesAtAGlance.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutResourcesAtAGlance.getDefaultCell().setPadding(0);
		layoutResourcesAtAGlance.setExtendLastRow(false);

		PdfPTable layoutResourcesAtAGlanceTable1 = getWidgetTable("atGlanceTable_Place1");
		PdfPTable layoutResourcesAtAGlanceTable2 = getWidgetTable("atGalnceTable_Place2");
		PdfPTable layoutResourcesAtAGlanceTable3 = getWidgetTable("atGlanceTable_Place3");

		PdfPTable layoutResourcesAtAGlanceTable1and2 = new PdfPTable(1);
		layoutResourcesAtAGlanceTable1and2.setExtendLastRow(false);
		layoutResourcesAtAGlanceTable1and2.getDefaultCell().setBorder(
				PdfPCell.NO_BORDER);
		layoutResourcesAtAGlanceTable1and2
				.addCell(layoutResourcesAtAGlanceTable1);
		layoutResourcesAtAGlanceTable1and2
				.addCell(layoutResourcesAtAGlanceTable2);

		layoutResourcesAtAGlance.addCell(layoutResourcesAtAGlanceTable1and2);

		PdfPTable layoutResourcesAtAGlanceTable3alone = new PdfPTable(1);
		layoutResourcesAtAGlanceTable3.setExtendLastRow(false);
		layoutResourcesAtAGlanceTable3alone.getDefaultCell().setBorder(
				PdfPCell.NO_BORDER);
		layoutResourcesAtAGlanceTable3alone
				.addCell(layoutResourcesAtAGlanceTable3);

		layoutResourcesAtAGlance.addCell(layoutResourcesAtAGlanceTable3alone);
		layoutCell.addElement(layoutResourcesAtAGlance);
		//widget:SourceOECD

		layoutCell.addElement(new Paragraph(TranslatorWorker.translateText("Source: OECD", locale, siteId), new Font(Font.HELVETICA, 6)));

		generalBox.addCell(layoutCell);

		//Make the innerLayout
		return generalBox;
	}

	private PdfPTable getWidgetTable(String codePlace) {
		try {

			String code = codePlace;

			AmpDaWidgetPlace place = WidgetUtil.getPlace(code);
			AmpWidget widget = place.getAssignedWidget();
			if (widget == null)
				return null;

			WiTable table = new WiTable.TableBuilder(widget.getId()).build();
			WiColumnDropDownFilter filter = null;
			int currentTableIdIndex = matchesId(table.getId());
			if(currentTableIdIndex > -1)
			{
				table = new WiTable.TableBuilder(this.tableId[currentTableIdIndex]).build();
				if (itemId != null && columnId != null && columnId[currentTableIdIndex]!=null && itemId[currentTableIdIndex]!=null && columnId[currentTableIdIndex].longValue()>0 && itemId[currentTableIdIndex].longValue()>0){
					filter = (WiColumnDropDownFilter) table.getColumnById(columnId[currentTableIdIndex]);
					//TODO this is not correct, check why columnId and itemId are not null when table is normal table.
					if (filter!=null){
						filter.setActiveItemId(itemId[currentTableIdIndex]);
					}
				}
			}
			List<WiColumn> columns = table.getColumns();
			List<WiRow> rows = table.getDataRows();

			PdfPTable pdfTable = new PdfPTable(columns.size());
			pdfTable.setExtendLastRow(false);
			Font fontHeader = new Font();
			fontHeader.setSize(5);
			fontHeader.setStyle(Font.BOLD);
			fontHeader.setColor(new Color(255, 255, 255));

			Font fontCell = new Font();
			fontCell.setSize(4);

			for (WiColumn column : columns) {
				String columnName = "";
				if(filter != null && column.getId() == filter.getId())
					column = filter;

				if (column instanceof WiColumnDropDownFilter && filter != null)
					columnName = filter.getProvider().getItem(filter.getActiveItemId()).getName();
				else
					columnName = column.getName();

				PdfPCell cell = new PdfPCell(new Phrase(columnName,
						fontHeader));
				cell.setBackgroundColor(new Color(34, 46, 93));

				pdfTable.addCell(cell);
			}

			int counter = 0;
			for (WiRow row : rows) {
				List<WiCell> cells = row.getCells();
				Color cellColor;
				if (counter % 2 == 0)
					cellColor = new Color(255, 255, 255);
				else
					cellColor = new Color(219, 229, 241);
				counter++;

				for (WiCell cell : cells) {
					PdfPCell cellPdf = new PdfPCell(new Phrase(cell.getValue(),
							fontCell));
					cellPdf.setBackgroundColor(cellColor);

					pdfTable.addCell(cellPdf);
				}

			}
			return pdfTable;
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	private int matchesId(Long ptableId)
	{
		for(int a=0;a < this.tableId.length; a++)
		{
			if(this.tableId[a].equals(ptableId))
				return a;
		}
		return -1;
	}

	private ByteArrayOutputStream getMapImageSectorIndicator(String mapCode, Long secId, Long indId) throws Exception {
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		GisMap map = null;

		if (mapCode != null && mapCode.trim().length() > 0) {
			map = GisUtil.getMap(mapCode);
		}

        //Get segments with funding for dashed paint map
        List secFundings = org.digijava.module.gis.util.DbUtil.getSectorFoundings(secId);

        Iterator it = secFundings.iterator();

        Object [] fundingList = getFundingsByLocations(secFundings);
        Map fundingLocationMap = (Map) fundingList[0];

        List segmentDataDasheList = new ArrayList();

        Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();
        while (locFoundingMapIt.hasNext()) {
            String key = (String) locFoundingMapIt.next();
            org.digijava.module.gis.util.SegmentData segmentData = new SegmentData();
            segmentData.setSegmentCode(key);
            segmentData.setSegmentValue("100");
            segmentDataDasheList.add(segmentData);
        }

        List hilightDashData = prepareDashSegments(segmentDataDasheList,
                new ColorRGB(0, 0, 0), map);

        //Need to pass year and subgroup ID in the future to get correct results.
        List inds = org.digijava.module.gis.util.DbUtil.getIndicatorValuesForSectorIndicator(secId, indId, new Long(-1));

        List segmentDataList = new ArrayList();
        Iterator indsIt = inds.iterator();
        Double min = null;
        Double max = null;

        Set regSet = new HashSet();

        while (indsIt.hasNext()) {
            Object[] indData = (Object[]) indsIt.next();

            String segmentCode = (String) indData[1];
            Double indValue = (Double) indData[0];

            if (isRegion(map,segmentCode)) {

            SegmentData indHilightData = new SegmentData();
            indHilightData.setSegmentCode(segmentCode);
            indHilightData.setSegmentValue(indValue.toString());

            if (min == null) {
                min = indValue;
                max = indValue;
            }

            if (indValue < min) {
                min = indValue;
            }

            if (indValue > max) {
                max = indValue;
            }

    //                        regSet.add(segmentCode);
            segmentDataList.add(indHilightData);
           }


        }

        if (min == null) {
            min = new Double(0);
            max = new Double(0);
        }

        List hilightData = prepareHilightSegments(segmentDataList, map, min, max);

        int canvasWidth = 700;
		int canvasHeight = 700;

        BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
                                                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = graph.createGraphics();

        g2d.setBackground(new Color(0, 0, 100, 255));

        g2d.clearRect(0, 0, canvasWidth, canvasHeight);
		GisUtil gisUtil = new GisUtil();
		CoordinateRect rect = gisUtil.getMapRect(map);
        gisUtil.addDataToImage(g2d,
                               map.getSegments(),
                               hilightData,
                               hilightDashData,
                               canvasWidth, canvasHeight,
                               rect.getLeft(), rect.getRight(),
                               rect.getTop(), rect.getBottom(), true, false);

        gisUtil.addCaptionsToImage(g2d,
                                   map.getSegments(),
                                   canvasWidth, canvasHeight,
                                   rect.getLeft(), rect.getRight(),
                                   rect.getTop(), rect.getBottom());

        g2d.dispose();

        RenderedImage ri = graph;

		try {
			ImageIO.write(ri, "png", outByteStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		graph.flush();

		return outByteStream;
	}
	private ByteArrayOutputStream getMapImage(String mapCode) {
		GisUtil gisUtil = new GisUtil();
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		GisMap map = null;

		if (mapCode != null && mapCode.trim().length() > 0) {
			map = GisUtil.getMap(mapCode);
		}

		int canvasWidth = 700;
		int canvasHeight = 700;

		CoordinateRect rect = gisUtil.getMapRect(map);

		BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = graph.createGraphics();

		g2d.setBackground(new Color(0, 0, 100, 255));

		g2d.clearRect(0, 0, canvasWidth, canvasHeight);

		gisUtil.addDataToImage(g2d, map.getSegments(), -1, canvasWidth,
				canvasHeight, rect.getLeft(), rect.getRight(), rect.getTop(),
				rect.getBottom(), true, false);

		gisUtil.addCaptionsToImage(g2d, map.getSegments(), canvasWidth,
				canvasHeight, rect.getLeft(), rect.getRight(), rect.getTop(),
				rect.getBottom());

		g2d.dispose();

		RenderedImage ri = graph;

		try {
			ImageIO.write(ri, "png", outByteStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		graph.flush();

		return outByteStream;
	}

	public void onChapter(PdfWriter arg0, Document arg1, float arg2,
			Paragraph arg3) {
		// TODO Auto-generated method stub

	}

	public void onChapterEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub

	}

	public void onCloseDocument(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub

	}

	public void onEndPage(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub

	}

	public void onGenericTag(PdfWriter arg0, Document arg1, Rectangle arg2,
			String arg3) {
		// TODO Auto-generated method stub

	}

	public void onOpenDocument(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub

	}

	public void onParagraph(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub

	}

	public void onParagraphEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub

	}

	public void onSection(PdfWriter arg0, Document arg1, float arg2, int arg3,
			Paragraph arg4) {
		// TODO Auto-generated method stub

	}

	public void onSectionEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub

	}

	public void onStartPage(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub

	}

	public ByteArrayOutputStream getChartImage(HttpServletRequest request,Long donorId, Integer fromYear,Integer toYear, Boolean showLegends, Boolean showLabels)
			throws Exception {
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		Long[] donorIDs = null;
		if (donorId != null) {
			donorIDs = new Long[1];
			donorIDs[0] = donorId;
		}
		GregorianCalendar cal = new GregorianCalendar();
		if (fromYear == null){
			fromYear = new Integer(cal.get(Calendar.YEAR));
		}
		if(toYear==null){
			toYear = new Integer(cal.get(Calendar.YEAR));
		}

		ChartOption opt = new ChartOption();
		opt.setShowTitle(true);
		opt.setShowLegend(showLegends);
		opt.setShowLabels(showLabels);
		opt.setHeight(new Integer(660));
		opt.setWidth(new Integer(420));
		String sitId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
		opt.setSiteId(sitId);
		String langCode = RequestUtils.getNavigationLanguage(request).getCode();
		opt.setLangCode(langCode);

		// generate chart
		JFreeChart chart = ChartWidgetUtil.getSectorByDonorChart(donorIDs,fromYear,toYear, opt);
		ChartRenderingInfo info = new ChartRenderingInfo();

		// write image in response

		ChartUtilities.writeChartAsJPEG(outByteStream, chart, opt.getWidth()
				.intValue(), opt.getHeight().intValue(), info);

		return outByteStream;
	}

	public ByteArrayOutputStream getWidgetImage(String codePlace)
			throws Exception {
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		String code = codePlace;

		AmpDaWidgetPlace place = WidgetUtil.getPlace(code);
		AmpWidget widget = place.getAssignedWidget();
		//AmpWidgetIndicatorChart cWidget = ChartWidgetUtil.getIndicatorChartWidget(place.getId());
		AmpWidgetIndicatorChart cWidget = ChartWidgetUtil.getIndicatorChartWidget(widget.getId());
		if (widget == null)
			return null;

		ChartOption opt = new ChartOption();

		opt.setShowTitle(true);
		if (widget != null) {
			opt.setTitle(widget.getName());
		}
		opt.setShowLegend(false);
		opt.setShowLabels(true);
		opt.setHeight(new Integer(160));
		opt.setWidth(new Integer(220));
		IndicatorSector indicatorCon = cWidget.getIndicator();

		if (indicatorCon != null) {
			// generate chart
			JFreeChart chart = ChartWidgetUtil.getIndicatorChart(indicatorCon,
					opt);
			ChartRenderingInfo info = new ChartRenderingInfo();
			// write image in response
			ChartUtilities.writeChartAsJPEG(outByteStream, chart, opt
					.getWidth().intValue(), opt.getHeight().intValue(), info);
		}
		return outByteStream;
	}

	class RoundRectangle implements PdfPCellEvent {
		public void cellLayout(PdfPCell cell, Rectangle rect,
				PdfContentByte[] canvas) {
			PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
			cb.setColorStroke(new Color(255, 255, 255));
			cb.setColorFill(new Color(34, 46, 93));
			roundRectangleUpper(cb, rect.getLeft(), rect.getBottom(), rect
					.getWidth()-2, rect.getHeight(), 4);
			cb.fill();
		}

		public void roundRectangleUpper(PdfContentByte cb, float x, float y,
				float w, float h, float r) {
			if (w < 0) {
				x += w;
				w = -w;
			}
			if (h < 0) {
				y += h;
				h = -h;
			}
			if (r < 0)
				r = -r;
			float b = 0.4477f;
			cb.moveTo(x + w, y);
			cb.lineTo(x + w, y + h - r);
			cb.curveTo(x + w, y + h - r * b, x + w - r * b, y + h, x + w - r, y
					+ h);
			cb.lineTo(x + r, y + h);
			cb.curveTo(x + r * b, y + h, x, y + h - r * b, x, y + h - r);
			cb.lineTo(x, y);
		}
	}

    private Object[] getFundingsByLocations (List activityList) throws Exception {

        Map locationFundingMap = new HashMap();
        FundingData totalFundingForSector = new FundingData();
        //Calculate total funding
        Iterator<Object[]> actIt = activityList.iterator();
        while (actIt.hasNext()) {
            Object[] actData = actIt.next();
            AmpActivity activity = (AmpActivity) actData[0];
            Float percentsForSectorSelected = (Float)actData[1];
            FundingData totalFunding = getActivityTotalFundingInUSD (activity);

            totalFundingForSector.setCommitment(totalFundingForSector.getCommitment() + totalFunding.getCommitment().floatValue()*percentsForSectorSelected.floatValue()/100f);
            totalFundingForSector.setDisbursement(totalFundingForSector.getDisbursement() + totalFunding.getDisbursement().floatValue()*percentsForSectorSelected.floatValue()/100f);
            totalFundingForSector.setExpenditure(totalFundingForSector.getExpenditure() + totalFunding.getExpenditure().floatValue()*percentsForSectorSelected.floatValue()/100f);


            FundingData fundingForSector = new FundingData();
            fundingForSector.setCommitment(new Double(totalFunding.getCommitment().floatValue()*percentsForSectorSelected.floatValue()/100f));
            fundingForSector.setDisbursement(new Double(totalFunding.getDisbursement().floatValue()*percentsForSectorSelected.floatValue()/100f));
            fundingForSector.setExpenditure(new Double(totalFunding.getExpenditure().floatValue()*percentsForSectorSelected.floatValue()/100f));

            Set locations = activity.getLocations();
            Iterator <AmpActivityLocation> locIt = locations.iterator();


            while (locIt.hasNext()) {
                AmpActivityLocation loc = locIt.next();
                if (loc.getLocation().getAmpRegion() != null && loc.getLocationPercentage().floatValue() > 0.0f) {
                    String regCode = loc.getLocation().getAmpRegion().getName();
                    if (locationFundingMap.containsKey(regCode)) {
                        FundingData existingVal = (FundingData)locationFundingMap.get(regCode);

                        FundingData newVal = new FundingData();
                        newVal.setCommitment(new Double(existingVal.getCommitment() + fundingForSector.getCommitment().floatValue() * loc.getLocationPercentage().floatValue() / 100f));
                        newVal.setDisbursement(new Double(existingVal.getDisbursement() + fundingForSector.getDisbursement().floatValue() * loc.getLocationPercentage().floatValue() / 100f));
                        newVal.setExpenditure(new Double(existingVal.getExpenditure() + fundingForSector.getExpenditure().floatValue() * loc.getLocationPercentage().floatValue() / 100f));

                        locationFundingMap.put(regCode, newVal);
                    } else {
                        FundingData newVal = new FundingData();
                        newVal.setCommitment(new Double(fundingForSector.getCommitment().floatValue() * loc.getLocationPercentage().floatValue() / 100f));
                        newVal.setDisbursement(new Double(fundingForSector.getDisbursement().floatValue() * loc.getLocationPercentage().floatValue() / 100f));
                        newVal.setExpenditure(new Double(fundingForSector.getExpenditure().floatValue() * loc.getLocationPercentage().floatValue() / 100f));

                        locationFundingMap.put(regCode, newVal);
                    }
                }
            }

        //    Set activiactivity.getFunding();
        }
        Object[] retVal = new Object[2];
        retVal[0] = locationFundingMap;
        retVal[1] = totalFundingForSector;
        return retVal;
    }
    private List prepareDashSegments(List segmentData, ColorRGB dashColor, GisMap map) {
        List retVal = new ArrayList();
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            for (int idx = (int) 0; idx < segmentData.size(); idx++) {
                SegmentData sd = (SegmentData) segmentData.get(idx);
                if (sd.getSegmentCode().equalsIgnoreCase(segment.getSegmentCode())) {
                    HilightData hData = new HilightData();
                    hData.setSegmentId((int) segment.getSegmentId());
                    hData.setColor(dashColor);
                    retVal.add(hData);
                }
            }
        }
        return retVal;
    }

    private boolean isRegion (GisMap map, String regCode) {
        boolean retVal = false;
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            if (segment.getSegmentCode().equalsIgnoreCase(regCode)) {
                retVal = true;
                break;
            }
        }

        return retVal;
    }


    private List prepareHilightSegments(List segmentData, GisMap map, Double min, Double max) {

        float delta = max.floatValue() - min.floatValue();
        float coeff = 205/delta;

        List retVal = new ArrayList();
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            for (int idx = (int) 0; idx < segmentData.size(); idx++) {
                SegmentData sd = (SegmentData) segmentData.get(idx);
                if (sd.getSegmentCode().equalsIgnoreCase(segment.getSegmentCode())) {
                    HilightData hData = new HilightData();
                    hData.setSegmentId((int) segment.getSegmentId());
                    float green = (Float.parseFloat(sd.getSegmentValue()) - min.floatValue()) * coeff;
                    hData.setColor(new ColorRGB((int) 0,(int) (green + 50f), 0));
                    retVal.add(hData);
                }
            }
        }
        return retVal;
    }

    private FundingData getActivityTotalFundingInUSD(AmpActivity activity) {
        FundingData retVal = null;
        Set fundSet = activity.getFunding();
        Iterator <AmpFunding> fundIt = fundSet.iterator();

        Double commitment = new Double (0);
        Double disbursement = new Double (0);
        Double expenditure = new Double (0);

        try {
            while (fundIt.hasNext()) {
                AmpFunding fund = fundIt.next();
                Set fundDetaiuls = fund.getFundingDetails();
                Iterator<AmpFundingDetail> fundDetIt = fundDetaiuls.iterator();
                while (fundDetIt.hasNext()) {
                    AmpFundingDetail fundDet = fundDetIt.next();
                    Double exchangeRate = null;
                    /*
                    try {

                        exchangeRate = CurrencyUtil.getLatestExchangeRate(
                                fundDet.getAmpCurrencyId().getCurrencyCode());

                    } catch (AimException ex) {
                        //Add exception reporting
                    }
                    */
                    exchangeRate = fundDet.getFixedExchangeRate();


                    /*
                    switch (fundDet.getTransactionType().intValue()) {
                    case Constants.COMMITMENT:
                        commitment += fundDet.getTransactionAmount() /
                                exchangeRate;
                        break;

                    case Constants.DISBURSEMENT:
                        disbursement += fundDet.getTransactionAmount() /
                                exchangeRate;
                        break;

                    case Constants.EXPENDITURE:
                        expenditure += fundDet.getTransactionAmount() /
                                exchangeRate;
                        break;
                    }
                    */

                   if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
                       commitment += fundDet.getTransactionAmount() /exchangeRate;
                   } else if (fundDet.getTransactionType().intValue() ==Constants.DISBURSEMENT) {
                       disbursement += fundDet.getTransactionAmount() /exchangeRate;
                   } else if (fundDet.getTransactionType().intValue() ==Constants.EXPENDITURE) {
                       expenditure += fundDet.getTransactionAmount() /exchangeRate;
                   }

                }
            }
        } catch (Exception ex1) {
            String ggg="gadfg";
            //Add exception reporting
        }

        retVal = new FundingData(commitment, disbursement, expenditure);

        return retVal;
    }

}
