package org.digijava.module.aim.helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.ListIterator;

import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;

public class ParisIndicator5aSubJrxml extends ParisIndicatorJrxml {
    public synchronized void createSubJrxml(String filePath, String reportName, List data) throws IOException, WorkerException {

        FileOutputStream out = new FileOutputStream(filePath);
        PrintStream p = new PrintStream(out);

        p.println("<?xml version=\"1.0\" encoding=\"UTF-8\"  ?>");
        p.println("<!-- Created with iReport - A designer for JasperReports -->");
        p.println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
        p.println("<jasperReport");
        p.println("		 name='" + reportName + "_sub'");
        p.println("		 columnCount='1'");
        p.println("		 printOrder='Vertical'");
        p.println("		 orientation='Portrait'");
        p.println("		 pageWidth='309'");
        p.println("		 pageHeight='802'");
        p.println("		 columnWidth='309'");
        p.println("		 columnSpacing='0'");
        p.println("		 leftMargin='0'");
        p.println("		 rightMargin='0'");
        p.println("		 topMargin='0'");
        p.println("		 bottomMargin='0'");
        p.println("		 whenNoDataType='AllSectionsNoDetail'");
        p.println("		 isTitleNewPage='false'");
        p.println("		 isSummaryNewPage='false'>");
        p.println("	<property name='ireport.scriptlethandling' value='0' />");
        p.println("	<property name='ireport.encoding' value='UTF-8' />");
        p.println("	<import value='java.util.*' />");
        p.println("	<import value='net.sf.jasperreports.engine.*' />");
        p.println("	<import value='net.sf.jasperreports.engine.data.*' />");
        p.println("		<background>");
        p.println("			<band height='0'  isSplitAllowed='true' >");
        p.println("			</band>");
        p.println("		</background>");
        p.println("		<title>");
        p.println("			<band height='0'  isSplitAllowed='true' >");
        p.println("			</band>");
        p.println("		</title>");
        p.println("		<pageHeader>");
        p.println("			<band height='0'  isSplitAllowed='true' >");
        p.println("			</band>");
        p.println("		</pageHeader>");
        p.println("		<columnHeader>");
        p.println("			<band height='" + (data.size() * 16 + 40) + "' isSplitAllowed='true' >");
        p.println("				<rectangle radius='0' >");
        p.println("					<reportElement");
        p.println("						mode='Opaque'");
        p.println("						x='0'");
        p.println("						y='0'");
        p.println("						width='307'");
        p.println("						height='40'");
        p.println("						forecolor='#000000'");
        p.println("						backcolor='#CCCCCC'");
        p.println("						key='element-22'/>");
        p.println("					<graphicElement stretchType='NoStretch' pen='Thin'/>");
        p.println("				</rectangle>");
        p.println("				<line direction='TopDown'>");
        p.println("					<reportElement");
        p.println("						x='0'");
        p.println("						y='39'");
        p.println("						width='309'");
        p.println("						height='1'");
        p.println("						forecolor='#808080'");
        p.println("						key='line'");
        p.println("						positionType='FixRelativeToBottom'/>");
        p.println("					<graphicElement stretchType='NoStretch' pen='Thin'/>");
        p.println("				</line>");

        int i = 6;
        int top = 40;
        for (ListIterator dnIter = data.listIterator(); dnIter.hasNext(); ) {
            String[] dn = (String[]) dnIter.next();
            int startLeft = 131;
            int offsetLeft = 59;
            if (dnIter.nextIndex() == 1) {
                p.println("				<staticText>");
                p.println("					<reportElement");
                p.println("						mode='Opaque'");
                p.println("						x='0'");
                p.println("						y='0'");
                p.println("						width='131'");
                p.println("						height='40'");
                p.println("						forecolor='#000000'");
                p.println("						backcolor='#CCCCCC'");
                p.println("						key='staticText-1'");
                p.println("						stretchType='NoStretch'");
                p.println("						positionType='FixRelativeToTop'");
                p.println("						isPrintRepeatedValues='true'");
                p.println("						isRemoveLineWhenBlank='false'");
                p.println("						isPrintInFirstWholeBand='false'");
                p.println("						isPrintWhenDetailOverflows='false'/>");
                p.println("					<box topBorder='1Point' topBorderColor='#000000' leftBorder='1Point' leftBorderColor='#000000' rightBorder='1Point' rightBorderColor='#000000' bottomBorder='1Point' bottomBorderColor='#000000'/>");
                p.println("					<textElement textAlignment='Center'>");
                p.println("						<font isBold='true'/>");
                p.println("					</textElement>");
                p.println("				<text><![CDATA[" + dn[0] + "]]></text>");
                p.println("				</staticText>");
                p.println("				<staticText>");
                p.println("					<reportElement");
                p.println("						mode='Opaque'");
                p.println("						x='131'");
                p.println("						y='0'");
                p.println("						width='" + (((String[]) data.get(0)).length - 1) * offsetLeft + "'");
                p.println("						height='26'");
                p.println("						forecolor='#000000'");
                p.println("						backcolor='#CCCCCC'");
                p.println("						key='staticText-2'");
                p.println("						stretchType='NoStretch'");
                p.println("						positionType='FixRelativeToTop'");
                p.println("						isPrintRepeatedValues='true'");
                p.println("						isRemoveLineWhenBlank='false'");
                p.println("						isPrintInFirstWholeBand='false'");
                p.println("						isPrintWhenDetailOverflows='false'/>");

                p.println("					<box topBorder='1Point' topBorderColor='#000000' leftBorder='1Point' leftBorderColor='#000000' rightBorder='1Point' rightBorderColor='#000000' bottomBorder='1Point' bottomBorderColor='#000000'/>");
                p.println("					<textElement textAlignment='Center'>");
                p.println("						<font isBold='true'/>");
                p.println("					</textElement>");
                p.println("				<text><![CDATA["+ TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("Percent of donors that use all three partner's PFM procedures", this.getLangCode(), this.getSite().getId())) + "]]></text>");
                p.println("				</staticText>");

                for (int j = 0; j < ((String[]) data.get(0)).length - 1; j++) {
                	p.println("				<staticText>");
                    p.println("					<reportElement");
                    p.println("						mode='Opaque'");
                    p.println("						x='" + (startLeft + (offsetLeft * j)) + "'");
                    p.println("						y='26'");
                    p.println("						width='" + offsetLeft + "'");
                    p.println("						height='14'");
                    p.println("						forecolor='#000000'");
                    p.println("						key='staticText-3'");
                    p.println("						stretchType='NoStretch'");
                    p.println("						positionType='FixRelativeToTop'");
                    p.println("						isPrintRepeatedValues='true'");
                    p.println("						isRemoveLineWhenBlank='false'");
                    p.println("						isPrintInFirstWholeBand='false'");
                    p.println("						isPrintWhenDetailOverflows='false'/>");
                    p.println("					<box topBorder='1Point' topBorderColor='#000000' leftBorder='1Point' leftBorderColor='#000000' rightBorder='1Point' rightBorderColor='#000000' bottomBorder='1Point' bottomBorderColor='#000000'/>");
                    p.println("					<textElement textAlignment='Center'>");
                    p.println("						<font isBold='true'/>");
                    p.println("					</textElement>");
                    p.println("				<text><![CDATA[" + dn[j + 1] + "]]></text>");
                    p.println("				</staticText>");
				}
            } else if (dnIter.nextIndex() > 1) {
            	for (int k = 0; k < ((String[]) data.get(0)).length; k++) {
            		p.println("				<staticText>");
                    p.println("					<reportElement");
                    if(k == 0) {
                    	p.println("						x='0'");
                    } else {
                    	p.println("						x='" + (131 + (offsetLeft * (k-1))) + "'");
                    }
                    p.println("						y='" + top + "'");
                    if(k == 0) {
                    	p.println("						width='131'");
                    } else {
                    	p.println("						width='" + offsetLeft + "'");
                    }
                    p.println("						height='16'");
                    p.println("						key='staticText-" + k + "'/>");
                    p.println("					<box topBorder='1Point' topBorderColor='#000000' leftBorder='1Point' leftBorderColor='#000000' rightBorder='1Point' rightBorderColor='#000000' bottomBorder='1Point' bottomBorderColor='#000000'/>");
                    p.println("					<textElement textAlignment='Center'>");
                    p.println("						<font isBold='true'/>");
                    p.println("					</textElement>");
                    p.println("				<text><![CDATA[ " + dn[k] + "]]></text>");
                    p.println("				</staticText>");
				}
            	top += 16;
            }
        }
        p.println("			</band>");
        p.println("		</columnHeader>");
        p.println("		<detail>");
        p.println("			<band height='1'  isSplitAllowed='true' >");
        p.println("			</band>");
        p.println("		</detail>");
        p.println("		<columnFooter>");
        p.println("			<band height='0'  isSplitAllowed='true' >");
        p.println("			</band>");
        p.println("		</columnFooter>");
        p.println("		<pageFooter>");
        p.println("			<band height='0'  isSplitAllowed='true' >");
        p.println("			</band>");
        p.println("		</pageFooter>");
        p.println("		<summary>");
        p.println("			<band height='0'  isSplitAllowed='true' >");
        p.println("			</band>");
        p.println("		</summary>");
        p.println("</jasperReport>");

        p.close();
        out.close();
    }

    public ParisIndicator5aSubJrxml() {
    }
}