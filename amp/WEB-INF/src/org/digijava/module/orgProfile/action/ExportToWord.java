package org.digijava.module.orgProfile.action;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;

import java.awt.Font;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.helper.ParisIndicatorHelper;
import org.digijava.module.orgProfile.helper.Project;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.helper.WidgetVisitor;
import org.digijava.module.widget.helper.WidgetVisitorAdapter;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.digijava.module.aim.util.DbUtil;
import com.lowagie.text.*;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.table.RtfCell;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

public class ExportToWord extends Action {

    private static Logger logger = Logger.getLogger(ExportToWord.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/msword");
        response.setHeader("content-disposition", "inline;filename=orgProfile.doc");
        Long siteId=RequestUtils.getSiteDomain(request).getSite().getId();
        String langCode= RequestUtils.getNavigationLanguage(request).getCode();
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            RtfWriter2.getInstance(doc, baos);
            List<AmpDaWidgetPlace> orgPlaces = WidgetUtil.getAllOrgProfilePlaces();
            Iterator<AmpDaWidgetPlace> placeIter = orgPlaces.iterator();
            doc.open();
            com.lowagie.text.Font pageTitleFont = com.lowagie.text.FontFactory.getFont("Arial", 24, com.lowagie.text.Font.BOLD);
            Paragraph pageTitle=new Paragraph(TranslatorWorker.translateText("Org. Profile",langCode,siteId),pageTitleFont);
            pageTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(pageTitle);
            while (placeIter.hasNext()) {
                AmpDaWidgetPlace place = placeIter.next();
                AmpWidget wd = place.getAssignedWidget();
                if (wd != null) {
                    final ArrayList rendertype = new ArrayList();
                    WidgetVisitor adapter = new WidgetVisitorAdapter() {

                        @Override
                        public void visit(AmpWidgetOrgProfile orgProfile) {
                            rendertype.add(orgProfile.getType());

                        }
                    };
                    wd.accept(adapter);
                    if (rendertype.size() > 0) {
                        Long type = (Long) rendertype.get(0);
                        HttpSession session = request.getSession();
                        FilterHelper filter = (FilterHelper) session.getAttribute("orgProfileFilter");
                        ChartOption opt = new ChartOption();
                        opt.setWidth(500);
                        opt.setHeight(350);
                        opt.setSiteId(siteId);
                        opt.setLangCode(langCode);
                        JFreeChart chart = null;
                        Table orgSummaryTbl = null;
                        Table orgContactsTbl = null;
                        Table largetsProjectsTbl = null;
                        Table parisDecTbl = null;
                        Table typeOfAidTbl =null;
                        Table odaProfileTbl =null;
                        ChartRenderingInfo info = new ChartRenderingInfo();
                        AmpOrganisation organization = filter.getOrganization();
                        String transTypeName="";
                        String currName=filter.getCurrName();
                        String amountInThousands = "";
                        String typeOfAid=TranslatorWorker.translateText("TYPE OF AID",langCode,siteId);
                        String odaProfile=TranslatorWorker.translateText("ODA Profile",langCode,siteId);
                        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
                            amountInThousands =","+TranslatorWorker.translateText("Amounts in thousands",langCode,siteId)+" ";
                        }
                        switch (filter.getTransactionType()) {
                            case org.digijava.module.aim.helper.Constants.COMMITMENT:
                                transTypeName = TranslatorWorker.translateText("Commitment",langCode,siteId);
                                break;
                            case org.digijava.module.aim.helper.Constants.DISBURSEMENT:
                                transTypeName = TranslatorWorker.translateText("Disbursement",langCode,siteId);
                                break;
                        }
                        switch (type.intValue()) {
                            case WidgetUtil.ORG_PROFILE_TYPE_OF_AID:
                                typeOfAidTbl=new Table(6);
                                RtfCell typeofAidTitleCell = new RtfCell(new Paragraph(typeOfAid+"("+transTypeName+"|"+currName+amountInThousands+")",OrgProfileUtil.HEADERFONT));
                                typeofAidTitleCell.setColspan(6);
                                typeOfAidTbl.addCell(typeofAidTitleCell);
                                typeofAidTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                RtfCell tpAidTitleCell = new RtfCell(new Paragraph(typeOfAid,OrgProfileUtil.HEADERFONT));
                                tpAidTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                typeOfAidTbl.addCell(tpAidTitleCell);
                                OrgProfileUtil.getDataTable(typeOfAidTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_TYPE_OF_AID);
                                break;
                            case WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB:
                                chart = ChartWidgetUtil.getPledgesCommDisbChart(opt, filter);
                                break;
                            case WidgetUtil.ORG_PROFILE_ODA_PROFILE:
                                odaProfileTbl=new Table(6);
                                RtfCell odaProfileTitleCell = new RtfCell(new Paragraph(odaProfile+"("+transTypeName+"|"+currName+amountInThousands+")",OrgProfileUtil.HEADERFONT));
                                odaProfileTitleCell.setColspan(6);
                                odaProfileTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                odaProfileTbl.addCell(odaProfileTitleCell);
                                RtfCell odaProfTitleCell = new RtfCell(new Paragraph(odaProfile,OrgProfileUtil.HEADERFONT));
                                odaProfTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                odaProfileTbl.addCell(odaProfTitleCell);
                                OrgProfileUtil.getDataTable(odaProfileTbl, filter, siteId, langCode, WidgetUtil.ORG_PROFILE_ODA_PROFILE);
                                break;
                            case WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN:
                                chart = ChartWidgetUtil.getSectorByDonorChart(opt, filter);
                                break;
                            case WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN:
                                chart = ChartWidgetUtil.getRegionByDonorChart(opt, filter);
                                break;
                            case WidgetUtil.ORG_PROFILE_SUMMARY:


                                //create summary table

                                orgSummaryTbl = new Table(2);
                                RtfCell summaryTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Organization Profile",langCode,siteId), OrgProfileUtil.HEADERFONT));
                                summaryTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                summaryTitleCell.setColspan(2);
                                summaryTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                orgSummaryTbl.addCell(summaryTitleCell);

                                RtfCell grTitleCell = new RtfCell();
                                grTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Group",langCode,siteId)+":", OrgProfileUtil.PLAINFONT));
                                grTitleCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                orgSummaryTbl.addCell(grTitleCell);

                                RtfCell grCell = new RtfCell();
                                grCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                grCell.addElement(new Paragraph(filter.getOrgGroup().getOrgGrpName(), OrgProfileUtil.PLAINFONT));
                                orgSummaryTbl.addCell(grCell);

                                RtfCell grTypeTitleCell = new RtfCell();
                                grTypeTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Type",langCode,siteId)+":", OrgProfileUtil.PLAINFONT));
                                orgSummaryTbl.addCell(grTypeTitleCell);

                                RtfCell grTypeCell = new RtfCell();
                                AmpOrgType orgGroupType = filter.getOrgGroup().getOrgType();
                                String orgGroupTpName = "";
                                if (orgGroupType != null) {
                                    orgGroupTpName = orgGroupType.getOrgType();
                                }
                                grTypeCell.addElement(new Paragraph(orgGroupTpName, OrgProfileUtil.PLAINFONT));
                                orgSummaryTbl.addCell(grTypeCell);

                                RtfCell orgTitleCell = new RtfCell();
                                orgTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Organization Name",langCode,siteId)+":", OrgProfileUtil.PLAINFONT));
                                orgTitleCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                orgSummaryTbl.addCell(orgTitleCell);

                                RtfCell orgCell = new RtfCell();
                                orgCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                orgCell.addElement(new Paragraph(organization.getName(), OrgProfileUtil.PLAINFONT));
                                orgSummaryTbl.addCell(orgCell);

                                RtfCell orgAcrTitleCell = new RtfCell();
                                orgAcrTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Organization Acronym",langCode,siteId)+":", OrgProfileUtil.PLAINFONT));
                                orgSummaryTbl.addCell(orgAcrTitleCell);

                                RtfCell orgAcrCell = new RtfCell();
                                orgAcrCell.addElement(new Paragraph(organization.getAcronym(), OrgProfileUtil.PLAINFONT));
                                orgSummaryTbl.addCell(orgAcrCell);


                                RtfCell orgDnGrpTitleCell = new RtfCell();
                                orgDnGrpTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Donor Group",langCode,siteId)+":", OrgProfileUtil.PLAINFONT));
                                orgDnGrpTitleCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                orgSummaryTbl.addCell(orgDnGrpTitleCell);

                                RtfCell orgDnGrpCell = new RtfCell();
                                AmpOrgGroup grp = organization.getOrgGrpId();
                                String grpName = "";
                                if (grp != null) {
                                    grpName = grp.getOrgGrpName();
                                }
                                orgDnGrpCell.addElement(new Paragraph(grpName, OrgProfileUtil.PLAINFONT));
                                orgDnGrpCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                orgSummaryTbl.addCell(orgDnGrpCell);

                                RtfCell orgWbLinkTitleCell = new RtfCell();
                                orgWbLinkTitleCell.addElement(new Paragraph(TranslatorWorker.translateText("Web Link",langCode,siteId)+":", OrgProfileUtil.PLAINFONT));
                                orgSummaryTbl.addCell(orgWbLinkTitleCell);

                                RtfCell orgWbLinkCell = new RtfCell();
                                orgWbLinkCell.addElement(new Paragraph(organization.getOrgUrl(), OrgProfileUtil.PLAINFONT));
                                orgSummaryTbl.addCell(orgWbLinkCell);

                               //create contacts table
                                orgContactsTbl=new Table(6);


                                RtfCell contactHeaderCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Contact Information",langCode,siteId),OrgProfileUtil.HEADERFONT));
                                contactHeaderCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                contactHeaderCell.setColspan(6);
                                contactHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                orgContactsTbl.addCell(contactHeaderCell);

                                RtfCell contactLastNameCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Last Name",langCode,siteId),OrgProfileUtil.HEADERFONT));
                                contactLastNameCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                contactLastNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                orgContactsTbl.addCell(contactLastNameCell);

                                RtfCell contactNameCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("First name",langCode,siteId),OrgProfileUtil.HEADERFONT));
                                contactNameCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                contactNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                orgContactsTbl.addCell(contactNameCell);

                                RtfCell contactEmailCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Email",langCode,siteId),OrgProfileUtil.HEADERFONT));
                                contactEmailCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                contactEmailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                orgContactsTbl.addCell(contactEmailCell);

                                RtfCell contactPhoneCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Telephone",langCode,siteId),OrgProfileUtil.HEADERFONT));
                                contactPhoneCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                contactPhoneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                orgContactsTbl.addCell(contactPhoneCell);

                                RtfCell contactFaxCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Fax",langCode,siteId),OrgProfileUtil.HEADERFONT));
                                contactFaxCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                contactFaxCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                orgContactsTbl.addCell(contactFaxCell);

                                RtfCell contactTitleCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("Title",langCode,siteId),OrgProfileUtil.HEADERFONT));
                                contactTitleCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                contactTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                orgContactsTbl.addCell(contactTitleCell);
                                int count=0;
                                if(organization.getContacts()!=null){
                                List<AmpContact> contacts=new ArrayList(organization.getContacts());
                                Iterator<AmpContact> contactsIter=contacts.iterator();
                                while(contactsIter.hasNext()){
                                    AmpContact contact=contactsIter.next();
                                    RtfCell name = new RtfCell(new Paragraph(contact.getLastname(),OrgProfileUtil.PLAINFONT));
                                    RtfCell lastName = new RtfCell(new Paragraph(contact.getName(),OrgProfileUtil.PLAINFONT));
//                                    RtfCell email = new RtfCell(new Paragraph(contact.getEmail(),OrgProfileUtil.PLAINFONT));
//                                    RtfCell phone = new RtfCell(new Paragraph(contact.getPhone(),OrgProfileUtil.PLAINFONT));
//                                    RtfCell fax = new RtfCell(new Paragraph(contact.getFax(),OrgProfileUtil.PLAINFONT));
                                    String contacTitle = "";
                                    if (contact.getTitle() != null) {
                                        contacTitle = contact.getTitle().getValue();
                                    }
                                    RtfCell title = new RtfCell(new Paragraph(contacTitle,OrgProfileUtil.PLAINFONT));
                                    if(count%2==0){
                                        title.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
//                                        fax.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
//                                        phone.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
//                                        email.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        lastName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        name.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    }
                                    orgContactsTbl.addCell(lastName);
                                    orgContactsTbl.addCell(name);
//                                    orgContactsTbl.addCell(email);
//                                    orgContactsTbl.addCell(phone);
//                                    orgContactsTbl.addCell(fax);
                                    orgContactsTbl.addCell(title);
                                    count++;

                                }


                                }

                                //create largest projects table
                                largetsProjectsTbl = new Table(3);
                                RtfCell largestProjectsTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Five (5) Largest projects",langCode,siteId)+" (" + (filter.getYear() - 1) + ")", OrgProfileUtil.HEADERFONT));
                                largestProjectsTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                largestProjectsTitle.setColspan(3);
                                largestProjectsTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                largetsProjectsTbl.addCell(largestProjectsTitle);

                                RtfCell largestProjectsProjecttitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Project title",langCode,siteId), OrgProfileUtil.HEADERFONT));
                                largestProjectsProjecttitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                largestProjectsProjecttitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                largetsProjectsTbl.addCell(largestProjectsProjecttitle);

                                RtfCell largestProjectsCommitmentTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Commitment",langCode,siteId), OrgProfileUtil.HEADERFONT));
                                largestProjectsCommitmentTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                largestProjectsCommitmentTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                largetsProjectsTbl.addCell(largestProjectsCommitmentTitle);

                                RtfCell largestProjectsSectorTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("Sector",langCode,siteId), OrgProfileUtil.HEADERFONT));
                                largestProjectsSectorTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                largestProjectsSectorTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                largetsProjectsTbl.addCell(largestProjectsSectorTitle);
                                List<Project> projects = OrgProfileUtil.getOrganisationLargestProjects(filter);
                                Iterator<Project> projectIter = projects.iterator();
                                count = 0;
                                while (projectIter.hasNext()) {
                                    Project project = projectIter.next();
                                    RtfCell title = new RtfCell(new Paragraph(project.getTitle(), OrgProfileUtil.PLAINFONT));
                                    RtfCell amount = new RtfCell(new Paragraph(project.getAmount(), OrgProfileUtil.PLAINFONT));
                                    RtfCell sectorsCell = new RtfCell(new Paragraph(project.getSectorNames(),OrgProfileUtil.PLAINFONT));
                                    if (count % 2 == 0) {
                                        title.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        amount.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        sectorsCell.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    }

                                    largetsProjectsTbl.addCell(title);

                                    largetsProjectsTbl.addCell(amount);
                                    largetsProjectsTbl.addCell(sectorsCell);
                                    count++;

                                }
                                break;

                            case WidgetUtil.ORG_PROFILE_PARIS_DECLARATION:
                                // creating Paris declaration table

                                // creating heading 
                                float widths[] = new float[]{10f, 40f, 10f, 10f, 10f, 10f, 10f};

                                parisDecTbl = new Table(widths.length);
                                parisDecTbl.setWidths(widths);


                                RtfCell parisDecTitle = new RtfCell(new Paragraph(TranslatorWorker.translateText("PARIS DECLARATION INDICATORS - DONORS",langCode,siteId), OrgProfileUtil.HEADERFONT));
                                parisDecTitle.setColspan(2);
                                parisDecTitle.setRowspan(2);
                                parisDecTitle.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                RtfCell allDonorsCell = new RtfCell(new Paragraph(TranslatorWorker.translateText("All Donors ",langCode,siteId), OrgProfileUtil.HEADERFONT));
                                allDonorsCell.setColspan(3);
                                allDonorsCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                RtfCell selectedOrgCell = new RtfCell(new Paragraph(organization.getName(), OrgProfileUtil.HEADERFONT));
                                selectedOrgCell.setColspan(2);
                                selectedOrgCell.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                RtfCell baseline = new RtfCell(new Paragraph(2005 + TranslatorWorker.translateText(" Baseline ",langCode,siteId), OrgProfileUtil.HEADERFONT));
                                RtfCell value = new RtfCell(new Paragraph(filter.getYear() - 1 + TranslatorWorker.translateText(" Value ",langCode,siteId), OrgProfileUtil.HEADERFONT));
                                RtfCell target = new RtfCell(new Paragraph(2010 +TranslatorWorker.translateText(" Target ",langCode,siteId), OrgProfileUtil.HEADERFONT));
                                baseline.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                value.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                target.setBackgroundColor(OrgProfileUtil.TITLECOLOR);
                                baseline.setBorderColor(OrgProfileUtil.BORDERCOLOR);
                                value.setBorderColor(OrgProfileUtil.BORDERCOLOR);
                                target.setBorderColor(OrgProfileUtil.BORDERCOLOR);

                                //adding headers
                                parisDecTbl.addCell(parisDecTitle);
                                parisDecTbl.addCell(allDonorsCell);
                                parisDecTbl.addCell(selectedOrgCell);
                                parisDecTbl.addCell(baseline);
                                parisDecTbl.addCell(value);
                                parisDecTbl.addCell(target);
                                parisDecTbl.addCell(baseline);
                                parisDecTbl.addCell(value);
                              
                                //end of creating heading

                                // creating content
                                

                                Collection<AmpAhsurveyIndicator> indicators = DbUtil.getAllAhSurveyIndicators();

                                Iterator<AmpAhsurveyIndicator> iter = indicators.iterator();
                                count = 0;
                                while (iter.hasNext()) {
                                    AmpAhsurveyIndicator piIndicator = iter.next();
                                    if(piIndicator.getIndicatorCode().equals("10b")||piIndicator.getIndicatorCode().equals("8")){
                                        continue;
                                    }
                                    ParisIndicatorHelper piHelper = new ParisIndicatorHelper(piIndicator, filter,true);
                                    RtfCell indicatorCode = new RtfCell(new Paragraph(piIndicator.getIndicatorCode(),OrgProfileUtil.PLAINFONT));
                                    RtfCell indicatorName = new RtfCell(new Paragraph(TranslatorWorker.translateText(piIndicator.getName(),langCode,siteId),OrgProfileUtil.PLAINFONT));
                                    String sufix = "";
                                    if (!piIndicator.getIndicatorCode().equals("6")) {
                                        sufix = "%";
                                    }

                                    RtfCell indicatorAllBaseline = new RtfCell(new Paragraph(piHelper.getAllDonorBaseLineValue() + sufix,OrgProfileUtil.PLAINFONT));
                                    RtfCell indicatorAllCurrentValue = new RtfCell(new Paragraph(piHelper.getAllCurrentValue() + sufix,OrgProfileUtil.PLAINFONT));
                                    RtfCell indicatorAllTargetValue = new RtfCell(new Paragraph(piHelper.getAllTargetValue() + sufix,OrgProfileUtil.PLAINFONT));
                                    RtfCell indicatorOrgBaseline = new RtfCell(new Paragraph(piHelper.getOrgBaseLineValue() + sufix,OrgProfileUtil.PLAINFONT));
                                    RtfCell indicatorOrgCurrentValue = new RtfCell(new Paragraph(piHelper.getOrgValue() + sufix,OrgProfileUtil.PLAINFONT));
                                    if (count % 2 == 1) {
                                        indicatorAllBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorAllCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorAllTargetValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorOrgBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorOrgCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorCode.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        indicatorName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                    }
                                    count++;
                                    parisDecTbl.addCell(indicatorCode);
                                    parisDecTbl.addCell(indicatorName);
                                    indicatorAllBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    parisDecTbl.addCell(indicatorAllBaseline);
                                    indicatorAllCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    parisDecTbl.addCell(indicatorAllCurrentValue);
                                    indicatorAllTargetValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    parisDecTbl.addCell(indicatorAllTargetValue);
                                    indicatorOrgBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    parisDecTbl.addCell(indicatorOrgBaseline);
                                    indicatorOrgCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    parisDecTbl.addCell(indicatorOrgCurrentValue);
                                 

                                    /* we should add indicator 5aii and indicator 5bii,
                                    these indicators don't exist in db so we add them manually*/

                                   if (piIndicator.getIndicatorCode().equals("5a")) {
                                        AmpAhsurveyIndicator ind5aii = new AmpAhsurveyIndicator();
                                        ind5aii.setAmpIndicatorId(piIndicator.getAmpIndicatorId());
                                        ind5aii.setIndicatorCode("5aii");
                                        RtfCell indicator5aCode = new RtfCell(new Paragraph("5aii",OrgProfileUtil.PLAINFONT));
                                        RtfCell indicator5aName = new RtfCell(new Paragraph(TranslatorWorker.translateText("Number of donors using country PFM",langCode,siteId),OrgProfileUtil.PLAINFONT));

                                        ParisIndicatorHelper piInd5aHelper = new ParisIndicatorHelper(ind5aii, filter,true);
                                        RtfCell indicator5aAllBaseline = new RtfCell(new Paragraph(piInd5aHelper.getAllDonorBaseLineValue() + " ", OrgProfileUtil.PLAINFONT));
                                        RtfCell indicatorAll5aCurrentValue = new RtfCell(new Paragraph(piInd5aHelper.getAllCurrentValue() + " ", OrgProfileUtil.PLAINFONT));
                                        RtfCell indicatorAll5aTargetValue = new RtfCell(new Paragraph(piInd5aHelper.getAllTargetValue() + " ", OrgProfileUtil.PLAINFONT));
                                        RtfCell indicatorOrg5aBaseline = new RtfCell(new Paragraph(piInd5aHelper.getOrgBaseLineValue() + " ", OrgProfileUtil.PLAINFONT));
                                        RtfCell indicatorOrg5aCurrentValue = new RtfCell(new Paragraph(piInd5aHelper.getOrgValue() + " ", OrgProfileUtil.PLAINFONT));

                                        if (count % 2 == 1) {
                                            indicator5aAllBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorAll5aCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorAll5aTargetValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorOrg5aBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicatorOrg5aCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5aCode.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5aName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        }
                                        count++;
                                        parisDecTbl.addCell(indicator5aCode);
                                        parisDecTbl.addCell(indicator5aName);
                                        indicator5aAllBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicator5aAllBaseline);
                                        indicatorAll5aCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicatorAll5aCurrentValue);
                                        indicatorAll5aTargetValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicatorAll5aTargetValue);
                                        indicatorOrg5aBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicatorOrg5aBaseline);
                                        indicatorOrg5aCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicatorOrg5aCurrentValue);

                                    }
                                    if (piIndicator.getIndicatorCode().equals("5b")) {
                                        AmpAhsurveyIndicator ind5bii = new AmpAhsurveyIndicator();
                                        ind5bii.setIndicatorCode("5bii");
                                        ind5bii.setAmpIndicatorId(piIndicator.getAmpIndicatorId());

                                        RtfCell indicator5bCode = new RtfCell(new Paragraph("5bii", OrgProfileUtil.PLAINFONT));
                                        RtfCell indicator5bName = new RtfCell(new Paragraph(TranslatorWorker.translateText("Number of donors using country procurement system",langCode,siteId),OrgProfileUtil.PLAINFONT));

                                        ParisIndicatorHelper piInd5bHelper = new ParisIndicatorHelper(ind5bii, filter,true);
                                        RtfCell indicator5bAllBaseline = new RtfCell(new Paragraph(piInd5bHelper.getAllDonorBaseLineValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        RtfCell indicator5bAllCurrentValue = new RtfCell(new Paragraph(piInd5bHelper.getAllCurrentValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        RtfCell indicator5bAllTargetValue = new RtfCell(new Paragraph(piInd5bHelper.getAllTargetValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        RtfCell indicator5bOrgBaseline = new RtfCell(new Paragraph(piInd5bHelper.getOrgBaseLineValue() + sufix, OrgProfileUtil.PLAINFONT));
                                        RtfCell indicator5bOrgCurrentValue = new RtfCell(new Paragraph(piInd5bHelper.getOrgValue() + sufix, OrgProfileUtil.PLAINFONT));

                                        if (count % 2 == 1) {
                                            indicator5bAllBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bAllCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bAllTargetValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bOrgBaseline.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bOrgCurrentValue.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bCode.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                            indicator5bName.setBackgroundColor(OrgProfileUtil.CELLCOLOR);
                                        }
                                        count++;
                                        parisDecTbl.addCell(indicator5bCode);
                                        parisDecTbl.addCell(indicator5bName);
                                        indicator5bAllBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicator5bAllBaseline);
                                        indicator5bAllCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicator5bAllCurrentValue);
                                        indicator5bAllTargetValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicator5bAllTargetValue);
                                        indicator5bOrgBaseline.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicator5bOrgBaseline);
                                        indicator5bOrgCurrentValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        parisDecTbl.addCell(indicator5bOrgCurrentValue);

                                    }




                                }


                                //--end of creating content






                                break;



                        }
                        if (chart != null) {
                            Plot plot = chart.getPlot();
                            plot.setNoDataMessage("No Data Available");
                            Font font = new Font(null, 0, 24);
                            plot.setNoDataMessageFont(font);

                            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                            // write image in response
                            ChartUtilities.writeChartAsPNG(
                                    outByteStream,
                                    chart,
                                    opt.getWidth().intValue(),
                                    opt.getHeight().intValue(),
                                    info);
                            Image img = Image.getInstance(outByteStream.toByteArray());
                            img.setAlignment(Image.ALIGN_CENTER);
                            doc.add(img);
                        }
                        else{
                            if( orgSummaryTbl !=null){
                                doc.add(orgSummaryTbl);
                                doc.add(orgContactsTbl);
                                doc.add(largetsProjectsTbl);
                            }
                            else{
                                if(parisDecTbl!=null){
                                    doc.add(parisDecTbl);
                                }
                                else{
                                    if(typeOfAidTbl!=null){
                                        doc.add(typeOfAidTbl);
                                    }
                                    else{
                                        if(odaProfileTbl!=null){
                                             doc.add(odaProfileTbl);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
          
            
            doc.close();
            response.setContentLength(baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
