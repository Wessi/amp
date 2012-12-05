package org.digijava.module.visualization.action;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.util.DbUtil;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Font;

public class ExportToPDF extends Action {

    private static Logger logger = Logger.getLogger(ExportToPDF.class);
    public static final Color TITLECOLOR = new Color(34, 46, 93);
    public static final Color BORDERCOLOR = new Color(255, 255, 255);
    public static final Color CELLCOLOR = new Color(219, 229, 241);
    public static final Font PLAINFONT = new Font(Font.TIMES_ROMAN, 10);
    public static final Font HEADERFONT = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
    public static final Font TITLEFONT = new Font(Font.TIMES_ROMAN, 24, Font.BOLD);
    public static final Font SUBTITLEFONT = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    public static final Font HEADERFONTWHITE = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.WHITE);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/download");
        response.setHeader("content-disposition", "attachment;filename=dashboard.pdf");
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        VisualizationForm vForm = (VisualizationForm) form;
        String fundingOpt = request.getParameter("fundingOpt");
        String aidPredicOpt = request.getParameter("aidPredicOpt");
        String aidPredicQuarterOpt = request.getParameter("aidPredicQuarterOpt");
        String budgetBreakdownOpt = request.getParameter("budgetBreakdownOpt");
        String aidTypeOpt = request.getParameter("aidTypeOpt");
        String financingInstOpt = request.getParameter("financingInstOpt");
        String organizationOpt = request.getParameter("organizationOpt");
        String sectorOpt = request.getParameter("sectorOpt");
        String regionOpt = request.getParameter("regionOpt");
        String NPOOpt = request.getParameter("NPOOpt");
        String programOpt = request.getParameter("programOpt");
        String summaryOpt = request.getParameter("summaryOpt");
        String ODAGrowthOpt = request.getParameter("ODAGrowthOpt");
        try {
        	String pageTrn = TranslatorWorker.translateText("Page", langCode, siteId);
        	String filtersTrn = TranslatorWorker.translateText("Filters", langCode, siteId);
			String filtersAllTrn = TranslatorWorker.translateText("All", langCode, siteId);
			String filtersAmountsInTrn = "";
			if(vForm.getFilter().getShowAmountsInThousands() != null && vForm.getFilter().getShowAmountsInThousands())
				filtersAmountsInTrn = TranslatorWorker.translateText("All amounts in thousands", langCode, siteId);
			else
				filtersAmountsInTrn = TranslatorWorker.translateText("All amounts in millions", langCode, siteId);
			String filtersCurrencyTypeTrn = TranslatorWorker.translateText("Currency Type", langCode, siteId);
			String filtersStartYearTrn = TranslatorWorker.translateText("Start Year", langCode, siteId);
			String filtersEndYearTrn = TranslatorWorker.translateText("End Year", langCode, siteId);
			String filtersOrgGroupTrn = TranslatorWorker.translateText("Organization Groups", langCode, siteId);
			String filtersOrganizationsTrn = TranslatorWorker.translateText("Organizations", langCode, siteId);
			String filtersSectorsTrn = TranslatorWorker.translateText("Sectors", langCode, siteId);
			String filtersSubSectorsTrn = TranslatorWorker.translateText("Sub-Sectors", langCode, siteId);
			String filtersRegionsTrn = TranslatorWorker.translateText("Regions", langCode, siteId);
			String filtersZonesTrn = TranslatorWorker.translateText("Zones", langCode, siteId);
			String filtersLocationsTrn = TranslatorWorker.translateText("Locations", langCode, siteId);
        	String fundingTrn = TranslatorWorker.translateText("Funding", langCode, siteId);
            String ODAGrowthTrn = TranslatorWorker.translateText("ODA Growth", langCode, siteId);
            String topPrjTrn = TranslatorWorker.translateText("Top Projects", langCode, siteId);
            String topOrganizationTrn = TranslatorWorker.translateText("Top Organizations", langCode, siteId);
            String topRegionTrn = TranslatorWorker.translateText("Top Regions", langCode, siteId);
            String projectTrn = TranslatorWorker.translateText("Project", langCode, siteId);
            String sectorTrn = TranslatorWorker.translateText("Sector", langCode, siteId);
            String organizationTrn = TranslatorWorker.translateText("Organization", langCode, siteId);
            String regionTrn = TranslatorWorker.translateText("Region", langCode, siteId);
            String NPOTrn = TranslatorWorker.translateText("NPO", langCode, siteId);
	        String programTrn = TranslatorWorker.translateText("Program", langCode, siteId);
	        String aidPredTrn = TranslatorWorker.translateText("Aid Predictability", langCode, siteId);
	        String aidPredQuarterTrn = TranslatorWorker.translateText("Aid Predictability Quarterly", langCode, siteId);
            String aidTypeTrn = TranslatorWorker.translateText("Aid Type", langCode, siteId);
            String budgetBreakdownTrn = TranslatorWorker.translateText("Budget Breakdown", langCode, siteId);
            String finInstTrn = TranslatorWorker.translateText("Financing Instrument", langCode, siteId);
            String sectorProfTrn = TranslatorWorker.translateText("Sector Profile", langCode, siteId);
            String regionProfTrn = TranslatorWorker.translateText("Region Profile", langCode, siteId);
            String NPOProfTrn = TranslatorWorker.translateText("NPO Profile", langCode, siteId);
	        String programProfTrn = TranslatorWorker.translateText("Program Profile", langCode, siteId);
	        String organizationProfTrn = TranslatorWorker.translateText("Organization Profile", langCode, siteId);
            String plannedTrn = TranslatorWorker.translateText("Planned", langCode, siteId);
            String actualTrn = TranslatorWorker.translateText("Actual", langCode, siteId);
            String yearTrn = TranslatorWorker.translateText("Year", langCode, siteId);
            String dashboardTrn = TranslatorWorker.translateText("Dashboard", langCode, siteId);
            String summaryTrn = TranslatorWorker.translateText("Summary", langCode, siteId);
            String totalCommsTrn = TranslatorWorker.translateText("Total Commitments", langCode, siteId);
            String totalDisbsTrn = TranslatorWorker.translateText("Total Disbursements", langCode, siteId);
            String numberPrjTrn = TranslatorWorker.translateText("Number of Projects", langCode, siteId);
            String numberSecTrn = TranslatorWorker.translateText("Number of Sectors", langCode, siteId);
            String numberDonTrn = TranslatorWorker.translateText("Number of Organizations", langCode, siteId);
            String numberRegTrn = TranslatorWorker.translateText("Number of Regions", langCode, siteId);
            String avgPrjZSizeTrn = TranslatorWorker.translateText("Average Project Size", langCode, siteId);
            String currName = vForm.getFilter().getCurrencyCode();
            String fundTypeTrn = "";
            switch (vForm.getFilter().getTransactionType()) {
				case Constants.COMMITMENT:
					fundTypeTrn = TranslatorWorker.translateText("Commitments", langCode, siteId);
					break;
				case Constants.DISBURSEMENT:
					fundTypeTrn = TranslatorWorker.translateText("Disbursements", langCode, siteId);
					break;
				case Constants.EXPENDITURE:
					fundTypeTrn = TranslatorWorker.translateText("Expenditures", langCode, siteId);
					break;
				default:
					fundTypeTrn = TranslatorWorker.translateText("Values", langCode, siteId);
				break;
			}
            String dashboardTypeTrn = "";
            switch (vForm.getFilter().getDashboardType()) {
	            case org.digijava.module.visualization.util.Constants.DashboardType.DONOR:
	            	dashboardTypeTrn = TranslatorWorker.translateText("Organization", langCode, siteId);
					break;
				case org.digijava.module.visualization.util.Constants.DashboardType.SECTOR:
					dashboardTypeTrn = TranslatorWorker.translateText("Sector", langCode, siteId);
					break;
				case org.digijava.module.visualization.util.Constants.DashboardType.REGION:
					dashboardTypeTrn = TranslatorWorker.translateText("Region", langCode, siteId);
					break;
			
			}
        
        	PdfWriter pdfWriter =PdfWriter.getInstance(doc, baos);
            HttpSession session = request.getSession();
            String footerText = pageTrn + " - ";
            doc.setPageCount(1);
            HeaderFooter footer = new HeaderFooter(new Phrase(footerText), true);
            footer.setBorder(0);
            doc.setFooter(footer);
            doc.open();
            Paragraph pageTitle;
            if(vForm.getDashboard()!=null){
            	pageTitle = new Paragraph(vForm.getDashboard().getName().toUpperCase(), TITLEFONT);
                	
            } else {
            	pageTitle = new Paragraph(dashboardTypeTrn.toUpperCase() + " " + dashboardTrn.toUpperCase(), TITLEFONT);
            }
            pageTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(pageTitle);
            doc.add(new Paragraph(" "));
            
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
            	String itemList = filtersOrgGroupTrn + ": ";
                Long[] orgGroupIds = vForm.getFilter().getSelOrgGroupIds();
                if (orgGroupIds != null && orgGroupIds.length != 0 && orgGroupIds[0]!=-1) {
    				for (int i = 0; i < orgGroupIds.length; i++) {
    					itemList = itemList + DbUtil.getOrgGroup(orgGroupIds[i]).getOrgGrpName() + "; ";
    				}
    			} else {
    				itemList = itemList + filtersAllTrn;
    			}
            	Paragraph pageSubTitle = new Paragraph(itemList, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                itemList = filtersOrganizationsTrn + ": ";
                Long[] orgIds = vForm.getFilter().getSelOrgIds();
                if (orgIds != null && orgIds.length != 0 && orgIds[0]!=-1) {
    				for (int i = 0; i < orgIds.length; i++) {
    					itemList = itemList + DbUtil.getOrganisation(orgIds[i]).getName() + "; ";
    				}
    			} else {
    				itemList = itemList + filtersAllTrn;
    			}
                pageSubTitle = new Paragraph(itemList, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                doc.add(new Paragraph(" "));
            }
     
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
            	String itemList = filtersSectorsTrn + ": ";
            	String itemList2 = filtersSubSectorsTrn + ": ";
            	Long[] sectorIds = vForm.getFilter().getSelSectorIds();
            	boolean hasSub = false;
                if (sectorIds != null && sectorIds.length != 0 && sectorIds[0]!=-1) {
    				for (int i = 0; i < sectorIds.length; i++) {
    					if (SectorUtil.getAmpSector(sectorIds[i]).getParent()==null){
    						itemList = itemList + SectorUtil.getAmpSector(sectorIds[i]).getName() + "; ";
    					} else {
    						hasSub = true;
    						itemList2 = itemList2 + SectorUtil.getAmpSector(sectorIds[i]).getName() + "; ";
    					}
    				}
    			} else {
    				itemList = itemList + filtersAllTrn;
    			}
                if (!hasSub) {
                	itemList2 = itemList2 + filtersAllTrn;
				}
            	Paragraph pageSubTitle = new Paragraph(itemList, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                pageSubTitle = new Paragraph(itemList2, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                doc.add(new Paragraph(" "));
            }
     
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
            	String itemList = filtersRegionsTrn + ": ";
            	String itemList2 = filtersZonesTrn + ": ";
            	Long[] locationIds = vForm.getFilter().getSelLocationIds();
            	boolean hasSub = false;
                if (locationIds != null && locationIds.length != 0 && locationIds[0]!=-1) {
    				for (int i = 0; i < locationIds.length; i++) {
    					if (LocationUtil.getAmpCategoryValueLocationById(locationIds[i]).getParentLocation().getParentLocation()==null){
    						itemList = itemList + LocationUtil.getAmpCategoryValueLocationById(locationIds[i]).getName() + "; ";
    					} else {
    						hasSub = true;
    						itemList2 = itemList2 + LocationUtil.getAmpCategoryValueLocationById(locationIds[i]).getName() + "; ";
    					}
    				}
    			} else {
    				itemList = itemList + filtersAllTrn;
    			}
                if (!hasSub) {
                	itemList2 = itemList2 + filtersAllTrn;
				}
            	
                Paragraph pageSubTitle = new Paragraph(itemList, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                pageSubTitle = new Paragraph(itemList2, SUBTITLEFONT);
            	pageSubTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(pageSubTitle);
                
                doc.add(new Paragraph(" "));
            }
     
            PdfPCell cell = null;
            List list = null;
            int colspan = 0;
            Image img = null;
            String[] singleRow = null;
            
            //Filters.
            PdfPTable filtersTbl = null;
            filtersTbl = new PdfPTable(1);
            filtersTbl.setWidthPercentage(100);
            PdfPCell filterTitleCell = new PdfPCell(new Paragraph(filtersTrn, HEADERFONT));
            filterTitleCell.setColspan(1);
            filtersTbl.addCell(filterTitleCell);
            
            
            cell = new PdfPCell(new Paragraph(filtersAmountsInTrn));
            filtersTbl.addCell(cell);
            cell = new PdfPCell(new Paragraph(filtersCurrencyTypeTrn + ": " + vForm.getFilter().getCurrencyCode()));
            filtersTbl.addCell(cell);
            cell = new PdfPCell(new Paragraph(filtersStartYearTrn + ": " + vForm.getFilter().getStartYear()));
            filtersTbl.addCell(cell);
            cell = new PdfPCell(new Paragraph(filtersEndYearTrn + ": " + vForm.getFilter().getEndYear()));
            filtersTbl.addCell(cell);
            String itemList = "";
            Long[] orgGroupIds = vForm.getFilter().getSelOrgGroupIds();
            if (orgGroupIds != null && orgGroupIds.length != 0 && orgGroupIds[0]!=-1) {
				for (int i = 0; i < orgGroupIds.length; i++) {
					itemList = itemList + DbUtil.getOrgGroup(orgGroupIds[i]).getOrgGrpName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            cell = new PdfPCell(new Paragraph(filtersOrgGroupTrn + ": " + itemList));
            filtersTbl.addCell(cell);
            itemList = "";
            Long[] orgIds = vForm.getFilter().getSelOrgIds();
            if (orgIds != null && orgIds.length != 0 && orgIds[0]!=-1) {
				for (int i = 0; i < orgIds.length; i++) {
					itemList = itemList + DbUtil.getOrganisation(orgIds[i]).getName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            cell = new PdfPCell(new Paragraph(filtersOrganizationsTrn + ": " + itemList));
            filtersTbl.addCell(cell);
            itemList = "";
            Long[] sectorIds = vForm.getFilter().getSelSectorIds();
            if (sectorIds != null && sectorIds.length != 0 && sectorIds[0]!=-1) {
				for (int i = 0; i < sectorIds.length; i++) {
					itemList = itemList + SectorUtil.getAmpSector(sectorIds[i]).getName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            cell = new PdfPCell(new Paragraph(filtersSectorsTrn + ": " + itemList));
            filtersTbl.addCell(cell);
            itemList = "";
            Long[] locationIds = vForm.getFilter().getSelLocationIds();
            if (locationIds != null && locationIds.length != 0 && locationIds[0]!=-1) {
				for (int i = 0; i < locationIds.length; i++) {
					itemList = itemList + LocationUtil.getAmpCategoryValueLocationById(locationIds[i]).getName() + "; ";
				}
			} else {
				itemList = filtersAllTrn;
			}
            cell = new PdfPCell(new Paragraph(filtersLocationsTrn + ": " + itemList));
            filtersTbl.addCell(cell);
            
		    doc.add(filtersTbl);
            doc.add(new Paragraph(" "));
            
          //Summary table.
            if (summaryOpt.equals("1")) {
				PdfPTable summaryTbl = null;
	            summaryTbl = new PdfPTable(7);
	            summaryTbl.setWidthPercentage(100);
	            PdfPCell sumamaryTitleCell = new PdfPCell(new Paragraph(summaryTrn + " (" + currName + ")", HEADERFONT));
	            sumamaryTitleCell.setColspan(7);
	            summaryTbl.addCell(sumamaryTitleCell);
	            cell = new PdfPCell(new Paragraph(totalCommsTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(totalDisbsTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(numberPrjTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            summaryTbl.addCell(cell);
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new PdfPCell(new Paragraph(numberDonTrn, HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            summaryTbl.addCell(cell);
				//}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
		            cell = new PdfPCell(new Paragraph(numberRegTrn, HEADERFONT));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            summaryTbl.addCell(cell);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
		            cell = new PdfPCell(new Paragraph(numberSecTrn, HEADERFONT));
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            summaryTbl.addCell(cell);
	            //}
	            cell = new PdfPCell(new Paragraph(avgPrjZSizeTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            summaryTbl.addCell(cell);
	            
	            cell = new PdfPCell(new Paragraph(getFormattedNumber(vForm.getSummaryInformation().getTotalCommitments()), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(getFormattedNumber(vForm.getSummaryInformation().getTotalDisbursements())));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfProjects().toString()));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            summaryTbl.addCell(cell);
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.DONOR) {
	            	cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfOrganizations().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
				//}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
	            	cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfRegions().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
	            //}
	            //if (vForm.getFilter().getDashboardType()!=org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
	            	cell = new PdfPCell(new Paragraph(vForm.getSummaryInformation().getNumberOfSectors().toString()));
	            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                summaryTbl.addCell(cell);
	            //}
	            cell = new PdfPCell(new Paragraph(getFormattedNumber(vForm.getSummaryInformation().getAverageProjectSize())));
	            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                summaryTbl.addCell(cell);
                doc.add(summaryTbl);
	            doc.add(new Paragraph(" "));
            }
           
            Paragraph subTitle = null;
          //Top projects table.
            if (vForm.getFilter().getShowProjectsRanking()==null || vForm.getFilter().getShowProjectsRanking()){
            	 subTitle = new Paragraph(topPrjTrn + " (" + currName + ")", SUBTITLEFONT);
                 subTitle.setAlignment(Element.ALIGN_LEFT);
                 doc.add(subTitle);
                 doc.add(new Paragraph(" "));
                 PdfPTable topPrjTbl = null;
	            topPrjTbl = new PdfPTable(2);
	            topPrjTbl.setWidthPercentage(100);
	            //PdfPCell topPrjTitleCell = new PdfPCell(new Paragraph(topPrjTrn + " (" + currName + ")", HEADERFONT));
	            //topPrjTitleCell.setColspan(2);
	            //topPrjTbl.addCell(topPrjTitleCell);
	            cell = new PdfPCell(new Paragraph(projectTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            topPrjTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(fundTypeTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            topPrjTbl.addCell(cell);
	            Map<AmpActivityVersion, BigDecimal> topProjects = vForm.getRanksInformation().getTopProjects();
	            if (topProjects!=null){
		            list = new LinkedList(topProjects.entrySet());
				    for (Iterator it = list.iterator(); it.hasNext();) {
				        Map.Entry entry = (Map.Entry)it.next();
				        cell = new PdfPCell(new Paragraph(entry.getKey().toString()));
				        topPrjTbl.addCell(cell);
		            	cell = new PdfPCell(new Paragraph(getFormattedNumber((BigDecimal)entry.getValue())));
		            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		            	topPrjTbl.addCell(cell);
				    }
				    doc.add(topPrjTbl);
		            doc.add(new Paragraph(" "));
	            }
        	}
          //Sector Profile Table.
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.SECTOR) {
            	doc.newPage();
            	subTitle = new Paragraph(sectorProfTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
                PdfPTable sectorProfTbl = null;
	            String[] sectorProfRows = vForm.getExportData().getSectorTableData().split("<");
	            colspan = sectorProfRows[1].split(">").length; 
	            sectorProfTbl = new PdfPTable(colspan);
	            sectorProfTbl.setWidthPercentage(100);
	            //PdfPCell sectorProfTitleCell = new PdfPCell(new Paragraph(sectorProfTrn + " (" + currName + ")", HEADERFONT));
	            //sectorProfTitleCell.setColspan(colspan);
	            //sectorProfTbl.addCell(sectorProfTitleCell);
	            cell = new PdfPCell(new Paragraph(sectorTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            sectorProfTbl.addCell(cell);
	            singleRow = sectorProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                sectorProfTbl.addCell(cell);
				}
	            for (int i = 2; i < sectorProfRows.length; i++) {
	            	singleRow = sectorProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	                	cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	sectorProfTbl.addCell(cell);
	    			}
				}
	            doc.add(sectorProfTbl);
	            doc.add(new Paragraph(" "));
                PdfPTable sectorGraph = new PdfPTable(1);
	            sectorGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getSectorGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getSectorGraph(),null);
	            sectorGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(sectorProfTrn, HEADERFONT));
	            //sectorGraph.addCell(cell);
	            doc.add(sectorGraph);
	            doc.add(new Paragraph(" "));
            }
            
          //Region Profile Table.
            if (vForm.getFilter().getDashboardType()==org.digijava.module.visualization.util.Constants.DashboardType.REGION) {
            	doc.newPage();
            	subTitle = new Paragraph(regionProfTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
                PdfPTable regionProfTbl = null;
	            String[] regionProfRows = vForm.getExportData().getRegionTableData().split("<");
	            colspan = regionProfRows[1].split(">").length; 
	            regionProfTbl = new PdfPTable(colspan);
	            regionProfTbl.setWidthPercentage(100);
	            //PdfPCell regionProfTitleCell = new PdfPCell(new Paragraph(regionProfTrn + " (" + currName + ")", HEADERFONT));
	            //regionProfTitleCell.setColspan(colspan);
	            //regionProfTbl.addCell(regionProfTitleCell);
	            cell = new PdfPCell(new Paragraph(regionTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            regionProfTbl.addCell(cell);
	            singleRow = regionProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	regionProfTbl.addCell(cell);
				}
	            for (int i = 2; i < regionProfRows.length; i++) {
	            	singleRow = regionProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	                	cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	regionProfTbl.addCell(cell);
	    			}
				}
	            doc.add(regionProfTbl);
	            doc.add(new Paragraph(" "));
	            PdfPTable regionGraph = new PdfPTable(1);
	            regionGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getRegionGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getRegionGraph(),null);
	            regionGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(regionProfTrn, HEADERFONT));
	            //regionGraph.addCell(cell);
	            doc.add(regionGraph);
	            doc.add(new Paragraph(" "));
            }
            
          //Funding Table.
	        if (!fundingOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(fundingTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (fundingOpt.equals("1") || fundingOpt.equals("3")){
	            PdfPTable fundingTbl = null;
	            String[] fundingRows = vForm.getExportData().getFundingTableData().split("<");
	            colspan = (fundingRows[1].split(">").length + 1)/2; 
	            fundingTbl = new PdfPTable(colspan);
	            fundingTbl.setWidthPercentage(100);
	            //PdfPCell fundingTitleCell = new PdfPCell(new Paragraph(fundingTrn + " (" + currName + ")", HEADERFONT));
	            //fundingTitleCell.setColspan(colspan);
	            //fundingTbl.addCell(fundingTitleCell);
	            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            fundingTbl.addCell(cell);
	            singleRow = fundingRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	fundingTbl.addCell(cell);
				}
	            for (int i = 1; i < fundingRows.length; i++) {
	            	singleRow = fundingRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	if(j > 0) {
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	fundingTbl.addCell(cell);
	    			}
				}
	            doc.add(fundingTbl);
	            doc.add(new Paragraph(" "));
            }
            if (fundingOpt.equals("2") || fundingOpt.equals("3")){
	            PdfPTable fundingGraph = new PdfPTable(1);
	            fundingGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getFundingGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getFundingGraph(),null);
	            fundingGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(fundingTrn, HEADERFONT));
	            //fundingGraph.addCell(cell);
	            doc.add(fundingGraph);
	            doc.add(new Paragraph(" "));
            }
	        
          //ODA Growth 
        	if (!ODAGrowthOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(ODAGrowthTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
        	if (ODAGrowthOpt.equals("1") || ODAGrowthOpt.equals("3")){
	            PdfPTable ODAGrowthTbl = null;
	            String[] ODAGrowthRows = vForm.getExportData().getODAGrowthTableData().split("<");
	            colspan = (ODAGrowthRows[1].split(">").length); 
	            ODAGrowthTbl = new PdfPTable(colspan);
	            ODAGrowthTbl.setWidthPercentage(100);
	            singleRow = ODAGrowthRows[1].split(">");
	            for (int i = 0; i < singleRow.length; i++) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	ODAGrowthTbl.addCell(cell);
				}
	            for (int i = 2; i < ODAGrowthRows.length; i++) {
	            	singleRow = ODAGrowthRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(singleRow[j]));
//    	                	cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	ODAGrowthTbl.addCell(cell);
	    			}
				}
	            doc.add(ODAGrowthTbl);
	            doc.add(new Paragraph(" "));
            }
        	if (ODAGrowthOpt.equals("2") || ODAGrowthOpt.equals("3")) {
            	PdfPTable ODAGraph = new PdfPTable(1);
	            ODAGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getODAGrowthGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getFundingGraph(),null);
	            ODAGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(odaGrowthTrn, HEADERFONT));
	            //ODAGraph.addCell(cell);
	            doc.add(ODAGraph);
	            doc.add(new Paragraph(" "));
            }
          
          //Aid Predictability Table.
			if (!aidPredicOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(aidPredTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (aidPredicOpt.equals("1") || aidPredicOpt.equals("3")){
	            PdfPTable aidPredTbl = null;
	            String[] aidPredRows = vForm.getExportData().getAidPredicTableData().split("<");
	            colspan = (aidPredRows[1].split(">").length + 1)/2; 
	            aidPredTbl = new PdfPTable(colspan);
	            aidPredTbl.setWidthPercentage(100);
	            //PdfPCell aidPredTitleCell = new PdfPCell(new Paragraph(aidPredTrn + " (" + currName + ")", HEADERFONT));
	            //aidPredTitleCell.setColspan(colspan);
	            //aidPredTbl.addCell(aidPredTitleCell);
	            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(plannedTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(actualTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredTbl.addCell(cell);
	            for (int i = 1; i < aidPredRows.length; i++) {
	            	singleRow = aidPredRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	aidPredTbl.addCell(cell);
	    			}
				}
	            doc.add(aidPredTbl);
	            doc.add(new Paragraph(" "));
            }
            if (aidPredicOpt.equals("2") || aidPredicOpt.equals("3")){
	            PdfPTable aidPredGraph = new PdfPTable(1);
	            aidPredGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getAidPredictabilityGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getAidPredictabilityGraph(),null);
	            aidPredGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(aidPredTrn, HEADERFONT));
	            //aidPredGraph.addCell(cell);
	            doc.add(aidPredGraph);
	            doc.add(new Paragraph(" "));
            }
		    
		  //Aid Predictability Table.
			if (!aidPredicQuarterOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(aidPredQuarterTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (aidPredicQuarterOpt.equals("1") || aidPredicQuarterOpt.equals("3")){
	            PdfPTable aidPredQuarterTbl = null;
	            String[] aidPredQuarterRows = vForm.getExportData().getAidPredicQuarterTableData().split("<");
	            colspan = (aidPredQuarterRows[1].split(">").length + 1)/2; 
	            aidPredQuarterTbl = new PdfPTable(colspan);
	            aidPredQuarterTbl.setWidthPercentage(100);
	            //PdfPCell aidPredTitleCell = new PdfPCell(new Paragraph(aidPredTrn + " (" + currName + ")", HEADERFONT));
	            //aidPredTitleCell.setColspan(colspan);
	            //aidPredTbl.addCell(aidPredTitleCell);
	            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredQuarterTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(plannedTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredQuarterTbl.addCell(cell);
	            cell = new PdfPCell(new Paragraph(actualTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidPredQuarterTbl.addCell(cell);
	            for (int i = 1; i < aidPredQuarterRows.length; i++) {
	            	singleRow = aidPredQuarterRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	aidPredQuarterTbl.addCell(cell);
	    			}
				}
	            doc.add(aidPredQuarterTbl);
	            doc.add(new Paragraph(" "));
            }
            if (aidPredicQuarterOpt.equals("2") || aidPredicQuarterOpt.equals("3")){
	            PdfPTable aidPredQuarterGraph = new PdfPTable(1);
	            aidPredQuarterGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getAidPredictabilityQuarterGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getAidPredictabilityGraph(),null);
	            aidPredQuarterGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(aidPredTrn, HEADERFONT));
	            //aidPredGraph.addCell(cell);
	            doc.add(aidPredQuarterGraph);
	            doc.add(new Paragraph(" "));
            }
		    
		    
          //Budget breakdown Table.
			if (!budgetBreakdownOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(budgetBreakdownTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (budgetBreakdownOpt.equals("1") || budgetBreakdownOpt.equals("3")){
	            PdfPTable budgetBreakdownTbl = null;
	            String[] budgetBreakdownRows = vForm.getExportData().getBudgetTableData().split("<");
	            colspan = (budgetBreakdownRows[1].split(">").length + 1)/2; 
	            budgetBreakdownTbl = new PdfPTable(colspan);
	            budgetBreakdownTbl.setWidthPercentage(100);
	            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            budgetBreakdownTbl.addCell(cell);
	            singleRow = budgetBreakdownRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	budgetBreakdownTbl.addCell(cell);
				}
	            for (int i = 1; i < budgetBreakdownRows.length; i++) {
	            	singleRow = budgetBreakdownRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	budgetBreakdownTbl.addCell(cell);
	    			}
				}
	            doc.add(budgetBreakdownTbl);
	            doc.add(new Paragraph(" "));
            }
            if (budgetBreakdownOpt.equals("2") || budgetBreakdownOpt.equals("3")){
	            PdfPTable budgetBreakdownGraph = new PdfPTable(1);
	            budgetBreakdownGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getBudgetGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            budgetBreakdownGraph.addCell(img);
	            doc.add(budgetBreakdownGraph);
	            doc.add(new Paragraph(" "));
            }
            
          
          //Aid Type Table.
			if (!aidTypeOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(aidTypeTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (aidTypeOpt.equals("1") || aidTypeOpt.equals("3")){
	            PdfPTable aidTypeTbl = null;
	            String[] aidTypeRows = vForm.getExportData().getAidTypeTableData().split("<");
	            colspan = (aidTypeRows[1].split(">").length + 1)/2; 
	            aidTypeTbl = new PdfPTable(colspan);
	            aidTypeTbl.setWidthPercentage(100);
	            //PdfPCell aidTypeTitleCell = new PdfPCell(new Paragraph(aidTypeTrn + " (" + currName + ")", HEADERFONT));
	            //aidTypeTitleCell.setColspan(colspan);
	            //aidTypeTbl.addCell(aidTypeTitleCell);
	            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            aidTypeTbl.addCell(cell);
	            singleRow = aidTypeRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	aidTypeTbl.addCell(cell);
				}
	            for (int i = 1; i < aidTypeRows.length; i++) {
	            	singleRow = aidTypeRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	aidTypeTbl.addCell(cell);
	    			}
				}
	            doc.add(aidTypeTbl);
	            doc.add(new Paragraph(" "));
            }
            if (aidTypeOpt.equals("2") || aidTypeOpt.equals("3")){
	            PdfPTable aidTypeGraph = new PdfPTable(1);
	            aidTypeGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getAidTypeGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getAidTypeGraph(),null);
	            aidTypeGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(aidTypeTrn, HEADERFONT));
	            //aidTypeGraph.addCell(cell);
	            doc.add(aidTypeGraph);
	            doc.add(new Paragraph(" "));
            }
            
          //Financing Instrument Table.
		    if (!financingInstOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(finInstTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (financingInstOpt.equals("1") || financingInstOpt.equals("3")){
                PdfPTable finInstTbl = null;
	            String[] finInstRows = vForm.getExportData().getFinancingInstTableData().split("<");
	            colspan = (finInstRows[1].split(">").length + 1)/2; 
	            finInstTbl = new PdfPTable(colspan);
	            finInstTbl.setWidthPercentage(100);
	            //PdfPCell finInstTitleCell = new PdfPCell(new Paragraph(finInstTrn + " (" + currName + ")", HEADERFONT));
	            //finInstTitleCell.setColspan(colspan);
	            //finInstTbl.addCell(finInstTitleCell);
	            cell = new PdfPCell(new Paragraph(yearTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            finInstTbl.addCell(cell);
	            singleRow = finInstRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i=i+2) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	finInstTbl.addCell(cell);
				}
	            for (int i = 1; i < finInstRows.length; i++) {
	            	singleRow = finInstRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j=j+2) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	finInstTbl.addCell(cell);
	    			}
				}
	            doc.add(finInstTbl);
	            doc.add(new Paragraph(" "));
            }
            if (financingInstOpt.equals("2") || financingInstOpt.equals("3")){
                PdfPTable finInstGraph = new PdfPTable(1);
	            finInstGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getFinancingInstGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getFinancingInstGraph(),null);
	            finInstGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(finInstTrn, HEADERFONT));
	            //finInstGraph.addCell(cell);
	            doc.add(finInstGraph);
	            doc.add(new Paragraph(" "));
            }
            
          //Sector Profile Table.
            if (!sectorOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(sectorProfTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (sectorOpt.equals("1") || sectorOpt.equals("3")){
                PdfPTable sectorProfTbl = null;
	            String[] sectorProfRows = vForm.getExportData().getSectorTableData().split("<");
	            colspan = sectorProfRows[1].split(">").length; 
	            sectorProfTbl = new PdfPTable(colspan);
	            sectorProfTbl.setWidthPercentage(100);
	            //PdfPCell sectorProfTitleCell = new PdfPCell(new Paragraph(sectorProfTrn + " (" + currName + ")", HEADERFONT));
	            //sectorProfTitleCell.setColspan(colspan);
	            //sectorProfTbl.addCell(sectorProfTitleCell);
	            cell = new PdfPCell(new Paragraph(sectorTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            sectorProfTbl.addCell(cell);
	            singleRow = sectorProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                sectorProfTbl.addCell(cell);
				}
	            for (int i = 2; i < sectorProfRows.length; i++) {
	            	singleRow = sectorProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	sectorProfTbl.addCell(cell);
	    			}
				}
	            doc.add(sectorProfTbl);
	            doc.add(new Paragraph(" "));
            }
            if (sectorOpt.equals("2") || sectorOpt.equals("3")){
                PdfPTable sectorGraph = new PdfPTable(1);
	            sectorGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getSectorGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getSectorGraph(),null);
	            sectorGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(sectorProfTrn, HEADERFONT));
	            //sectorGraph.addCell(cell);
	            doc.add(sectorGraph);
	            doc.add(new Paragraph(" "));
            }
		    
          //Region Profile Table.
            if (!regionOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(regionProfTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (regionOpt.equals("1") || regionOpt.equals("3")){
                PdfPTable regionProfTbl = null;
	            String[] regionProfRows = vForm.getExportData().getRegionTableData().split("<");
	            colspan = regionProfRows[1].split(">").length; 
	            regionProfTbl = new PdfPTable(colspan);
	            regionProfTbl.setWidthPercentage(100);
	            //PdfPCell regionProfTitleCell = new PdfPCell(new Paragraph(regionProfTrn + " (" + currName + ")", HEADERFONT));
	            //regionProfTitleCell.setColspan(colspan);
	            //regionProfTbl.addCell(regionProfTitleCell);
	            cell = new PdfPCell(new Paragraph(regionTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            regionProfTbl.addCell(cell);
	            singleRow = regionProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	regionProfTbl.addCell(cell);
				}
	            
	            for (int i = 2; i < regionProfRows.length; i++) {
	            	singleRow = regionProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	regionProfTbl.addCell(cell);
	    			}
				}
	            doc.add(regionProfTbl);
	            doc.add(new Paragraph(" "));
            }
            if (regionOpt.equals("2") || regionOpt.equals("3")){
	            PdfPTable regionGraph = new PdfPTable(1);
	            regionGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getRegionGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getRegionGraph(),null);
	            regionGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(regionProfTrn, HEADERFONT));
	            //regionGraph.addCell(cell);
	            doc.add(regionGraph);
	            doc.add(new Paragraph(" "));
            }
            
          //Organization Profile Table.
            if (!organizationOpt.equals("0")){
            	doc.newPage();
            	subTitle = new Paragraph(organizationProfTrn + " (" + currName + ")", SUBTITLEFONT);
                subTitle.setAlignment(Element.ALIGN_LEFT);
                doc.add(subTitle);
                doc.add(new Paragraph(" "));
            }
            if (organizationOpt.equals("1") || organizationOpt.equals("3")){
                PdfPTable organizationProfTbl = null;
	            String[] organizationProfRows = vForm.getExportData().getOrganizationTableData().split("<");
	            colspan = organizationProfRows[1].split(">").length; 
	            organizationProfTbl = new PdfPTable(colspan);
	            organizationProfTbl.setWidthPercentage(100);
	            //PdfPCell organizationProfTitleCell = new PdfPCell(new Paragraph(organizationProfTrn + " (" + currName + ")", HEADERFONT));
	            //organizationProfTitleCell.setColspan(colspan);
	            //organizationProfTbl.addCell(organizationProfTitleCell);
	            cell = new PdfPCell(new Paragraph(organizationTrn, HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            organizationProfTbl.addCell(cell);
	            singleRow = organizationProfRows[1].split(">");
	            for (int i = 1; i < singleRow.length; i++) {
	            	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	organizationProfTbl.addCell(cell);
				}
	            for (int i = 2; i < organizationProfRows.length; i++) {
	            	singleRow = organizationProfRows[i].split(">");
	            	for (int j = 0; j < singleRow.length; j++) {
	                	if(j > 0) { //Skip first and last column
		                	BigDecimal bd = new BigDecimal(singleRow[j]);
	                		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
	                	}
	                	else
	                		cell = new PdfPCell(new Paragraph(singleRow[j]));
	                	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	                	organizationProfTbl.addCell(cell);
	    			}
				}
	            doc.add(organizationProfTbl);
	            doc.add(new Paragraph(" "));
            }
            if (organizationOpt.equals("2") || organizationOpt.equals("3")){
	            PdfPTable organizationGraph = new PdfPTable(1);
	            organizationGraph.setWidthPercentage(100);
	            ByteArrayOutputStream ba = new ByteArrayOutputStream();
	            ImageIO.write(vForm.getExportData().getOrganizationGraph(), "png", ba);
	            img = Image.getInstance(ba.toByteArray());
	            //img = Image.getInstance(vForm.getExportData().getRegionGraph(),null);
	            organizationGraph.addCell(img);
	            //cell = new PdfPCell(new Paragraph(regionProfTrn, HEADERFONT));
	            //organizationGraph.addCell(cell);
	            doc.add(organizationGraph);
	            doc.add(new Paragraph(" "));
            }
	        
	        //NPO Profile Table.
			if (!NPOOpt.equals("0")){
				doc.newPage();
				subTitle = new Paragraph(NPOProfTrn + " (" + currName + ")", SUBTITLEFONT);
			    subTitle.setAlignment(Element.ALIGN_LEFT);
			    doc.add(subTitle);
			    doc.add(new Paragraph(" "));
			}
			if (NPOOpt.equals("1") || NPOOpt.equals("3")){
			    PdfPTable NPOProfTbl = null;
			    String[] NPOProfRows = vForm.getExportData().getNPOTableData().split("<");
			    colspan = NPOProfRows[1].split(">").length; 
			    NPOProfTbl = new PdfPTable(colspan);
			    NPOProfTbl.setWidthPercentage(100);
			    cell = new PdfPCell(new Paragraph(NPOTrn, HEADERFONT));
			    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    NPOProfTbl.addCell(cell);
			    singleRow = NPOProfRows[1].split(">");
			    for (int i = 1; i < singleRow.length; i++) {
			    	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
			    	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    	NPOProfTbl.addCell(cell);
				}
			    for (int i = 2; i < NPOProfRows.length; i++) {
			    	singleRow = NPOProfRows[i].split(">");
			    	for (int j = 0; j < singleRow.length; j++) {
			        	if(j > 0) { //Skip first and last column
			            	BigDecimal bd = new BigDecimal(singleRow[j]);
			        		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
			        	}
			        	else
			        		cell = new PdfPCell(new Paragraph(singleRow[j]));
			        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        	NPOProfTbl.addCell(cell);
					}
				}
			    doc.add(NPOProfTbl);
			    doc.add(new Paragraph(" "));
			}
			if (NPOOpt.equals("2") || NPOOpt.equals("3")){
			    PdfPTable NPOGraph = new PdfPTable(1);
			    NPOGraph.setWidthPercentage(100);
			    ByteArrayOutputStream ba = new ByteArrayOutputStream();
			    ImageIO.write(vForm.getExportData().getNPOGraph(), "png", ba);
			    img = Image.getInstance(ba.toByteArray());
			    NPOGraph.addCell(img);
			    doc.add(NPOGraph);
			    doc.add(new Paragraph(" "));
			}
	        
			//Program Profile Table.
			if (!programOpt.equals("0")){
				doc.newPage();
				subTitle = new Paragraph(programProfTrn + " (" + currName + ")", SUBTITLEFONT);
			    subTitle.setAlignment(Element.ALIGN_LEFT);
			    doc.add(subTitle);
			    doc.add(new Paragraph(" "));
			}
			if (programOpt.equals("1") || programOpt.equals("3")){
			    PdfPTable programProfTbl = null;
			    String[] programProfRows = vForm.getExportData().getProgramTableData().split("<");
			    colspan = programProfRows[1].split(">").length; 
			    programProfTbl = new PdfPTable(colspan);
			    programProfTbl.setWidthPercentage(100);
			    cell = new PdfPCell(new Paragraph(programTrn, HEADERFONT));
			    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    programProfTbl.addCell(cell);
			    singleRow = programProfRows[1].split(">");
			    for (int i = 1; i < singleRow.length; i++) {
			    	cell = new PdfPCell(new Paragraph(singleRow[i], HEADERFONT));
			    	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    	programProfTbl.addCell(cell);
				}
			    for (int i = 2; i < programProfRows.length; i++) {
			    	singleRow = programProfRows[i].split(">");
			    	for (int j = 0; j < singleRow.length; j++) {
			        	if(j > 0) { //Skip first and last column
			            	BigDecimal bd = new BigDecimal(singleRow[j]);
			        		cell = new PdfPCell(new Paragraph(getFormattedNumber(bd)));
			        	}
			        	else
			        		cell = new PdfPCell(new Paragraph(singleRow[j]));
			        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        	programProfTbl.addCell(cell);
					}
				}
			    doc.add(programProfTbl);
			    doc.add(new Paragraph(" "));
			}
			if (programOpt.equals("2") || programOpt.equals("3")){
			    PdfPTable programGraph = new PdfPTable(1);
			    programGraph.setWidthPercentage(100);
			    ByteArrayOutputStream ba = new ByteArrayOutputStream();
			    ImageIO.write(vForm.getExportData().getProgramGraph(), "png", ba);
			    img = Image.getInstance(ba.toByteArray());
			    programGraph.addCell(img);
			    doc.add(programGraph);
			    doc.add(new Paragraph(" "));
			}
			
            //close document
            doc.close();
            response.setContentLength(baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
        } catch (Exception ex) {
            logger.error("error", ex);
        }

        return null;
    }
    
    public String getFormattedNumber(BigDecimal number){
    	 return FormatHelper.formatNumberNotRounded(number.doubleValue());
    }
}
