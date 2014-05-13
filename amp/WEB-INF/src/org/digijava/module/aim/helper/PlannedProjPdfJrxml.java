package org.digijava.module.aim.helper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.module.aim.util.FeaturesUtil;

public class PlannedProjPdfJrxml
{
	public void createJrxml(String filePath ,int cnt, int h)
	throws IOException
       {		
		  try
			{
			FileOutputStream out2; // declare a file output object
			PrintStream p2; // declare a print stream object
			File fopen = new File(filePath);	

			out2 = new FileOutputStream(fopen);
			p2 = new PrintStream(out2);
			p2.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");

int n=cnt;//cnt;
int center=0;//(12-n)*30;
int x=(0+center),x1=0,y=0,y1=0,xl=0,yl=0;
int textkey=11,linekey=21,c=0;
String ctextkey;
int pagesize=(60*n)+120;
			int height,ht;

			if(h>100)
				height=h;
			else
				height=100;

p2.println("<!-- Created with iReport - A designer for JasperReports -->");
p2.println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
p2.println("<jasperReport");
p2.println("name='PlannedProjectPdf'");
p2.println("columnCount='1'");
p2.println("printOrder='Vertical'");
p2.println("orientation='Landscape'");
p2.println("pageWidth='1190'");
p2.println("pageHeight='842'");
p2.println("columnWidth='842'");
p2.println("columnSpacing='0'");
p2.println("leftMargin='7'");
p2.println("rightMargin='7'");
p2.println("topMargin='7'");
p2.println("bottomMargin='7'");
p2.println("whenNoDataType='NoPages'");
p2.println("isTitleNewPage='false'");
p2.println("isSummaryNewPage='false'>");
p2.println("<property name='ireport.scriptlethandling' value='2' />");

//DYNAMIC CCCCCCCCC
String dc;
for(int k=1;k<=(15+(1+16*n)+5);k++)
{
	dc="c"+k;
	p2.println("<field name='"+dc+"' class='java.lang.String'/>");
	
}

p2.println("<group  name='Data' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
p2.println("<groupExpression><![CDATA[$F{c1}]]></groupExpression>");
p2.println("<groupHeader>");
p2.println("</groupHeader>");
p2.println("<groupFooter>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</groupFooter>");
p2.println("</group>");
p2.println("<background>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</background>");
p2.println("<title>");
p2.println("<band height='42'  isSplitAllowed='true' >");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='70'");
p2.println("y='0'");
p2.println("width='869'");
p2.println("height='20'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='staticText-1'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='14' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Quarterly Team Project Detail Report]]></text>");
p2.println("</staticText>");
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='20'");
p2.println("width='500'");
p2.println("height='20'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-17'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Workspace : \"+$F{c1}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("</band>");
p2.println("</title>");

p2.println("<pageHeader>");
p2.println("<band height='71'  isSplitAllowed='true' >");

p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='1'");
p2.println("width='800'");
p2.println("height='14'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-18'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Filters: \"+$F{c2}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='15'");
p2.println("width='800'");
p2.println("height='15'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-19'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"             \"+$F{c3}]]></textFieldExpression>");
p2.println("</textField>");


p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='30'");
p2.println("width='70'");
p2.println("height='40'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-2'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Donor]]></text>");
p2.println("</staticText>");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='70'");
p2.println("y='30'");
p2.println("width='80'");
p2.println("height='40'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-3'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Project Title]]></text>");
p2.println("</staticText>");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='150'");
p2.println("y='30'");
p2.println("width='60'");
p2.println("height='40'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-5'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Type of Assist.");
p2.println(" / ");
p2.println("Inst. of Fund.]]></text>");
p2.println("</staticText>");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='210'");
p2.println("y='30'");
p2.println("width='60'");
p2.println("height='40'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-6'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Sector]]></text>");
p2.println("</staticText>");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='270'");
p2.println("y='30'");
p2.println("width='50'");
p2.println("height='40'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-7'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Status ");
p2.println(" / ");
p2.println("Impl. Level]]></text>");

p2.println("</staticText>");


p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='320'");
p2.println("y='30'");
p2.println("width='80'");
p2.println("height='40'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-8'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Location]]></text>");
p2.println("</staticText>");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='400'");
p2.println("y='30'");
p2.println("width='60'");
p2.println("height='40'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-9'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Start Date");
p2.println("Close Date");
p2.println("Comm. Date]]></text>");
p2.println("</staticText>");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='460'");
p2.println("y='30'");
p2.println("width='60'");
p2.println("height='40'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-10'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Total Commitment]]></text>");
p2.println("</staticText>");

	x=540;y=0;
	c=16;
		for(int j=0;j<n;j++)
		{
		ctextkey="c"+c;

			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='"+x+"'");
			p2.println("y='30'");
			p2.println("width='180'");
			p2.println("height='20'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='textField-52'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Year:\"+$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");

		x += 180;
		c ++;
		}//for1

p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='520'");
p2.println("y='30'");
p2.println("width='20'");
p2.println("height='40'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-99'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[--]]></text>");
p2.println("</staticText>");

		x=540;y=0;
//		c=17;
		for(int j=0;j<n;j++)
		{
			ctextkey="c"+c;
			
				p2.println("<staticText>");
				p2.println("<reportElement");
				p2.println("mode='Opaque'");
				p2.println("x='"+x+"'");
				p2.println("y='50'");
				p2.println("width='60'");
				p2.println("height='20'");
				p2.println("forecolor='#000000'");
				p2.println("backcolor='#CCCCCC'");
				p2.println("key='staticText-08'");
				p2.println("stretchType='NoStretch'");
				p2.println("positionType='FixRelativeToTop'");
				p2.println("isPrintRepeatedValues='true'");
				p2.println("isRemoveLineWhenBlank='false'");
				p2.println("isPrintInFirstWholeBand='false'");
				p2.println("isPrintWhenDetailOverflows='false'/>");
				p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
				p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
				p2.println("</textElement>");
				p2.println("<text><![CDATA[Planned Disb.]]></text>");
				p2.println("</staticText>");

			x += 60;

				p2.println("<staticText>");
				p2.println("<reportElement");
				p2.println("mode='Opaque'");
				p2.println("x='"+x+"'");
				p2.println("y='50'");
				p2.println("width='60'");
				p2.println("height='20'");
				p2.println("forecolor='#000000'");
				p2.println("backcolor='#CCCCCC'");
				p2.println("key='staticText-08'");
				p2.println("stretchType='NoStretch'");
				p2.println("positionType='FixRelativeToTop'");
				p2.println("isPrintRepeatedValues='true'");
				p2.println("isRemoveLineWhenBlank='false'");
				p2.println("isPrintInFirstWholeBand='false'");
				p2.println("isPrintWhenDetailOverflows='false'/>");
				p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
				p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
				p2.println("</textElement>");
				p2.println("<text><![CDATA[Disbursments]]></text>");
				p2.println("</staticText>");

			x += 60;

				p2.println("<staticText>");
				p2.println("<reportElement");
				p2.println("mode='Opaque'");
				p2.println("x='"+x+"'");
				p2.println("y='50'");
				p2.println("width='60'");
				p2.println("height='20'");
				p2.println("forecolor='#000000'");
				p2.println("backcolor='#CCCCCC'");
				p2.println("key='staticText-08'");
				p2.println("stretchType='NoStretch'");
				p2.println("positionType='FixRelativeToTop'");
				p2.println("isPrintRepeatedValues='true'");
				p2.println("isRemoveLineWhenBlank='false'");
				p2.println("isPrintInFirstWholeBand='false'");
				p2.println("isPrintWhenDetailOverflows='false'/>");
				p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
				p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
				p2.println("</textElement>");
				p2.println("<text><![CDATA[Expenditure]]></text>");
				p2.println("</staticText>");

			x += 60;
			c += 5;

		}//for2

p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='"+(1080-((3-n)*180))+"'");
p2.println("y='30'");
p2.println("width='95'");
p2.println("height='40'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-117'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Total  ]]></text>");
p2.println("</staticText>");

p2.println("</band>");


p2.println("</pageHeader>");

p2.println("<columnHeader>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</columnHeader>");
p2.println("<detail>");
p2.println("<band height='100'  isSplitAllowed='true' >");
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='460'");
p2.println("y='3'");
p2.println("width='60'");
p2.println("height='95'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-20'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c15}]]></textFieldExpression>");
p2.println("</textField>");
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='400'");
p2.println("y='3'");
p2.println("width='60'");
p2.println("height='32'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-21'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c12}]]></textFieldExpression>");
p2.println("</textField>");
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='400'");
p2.println("y='35'");
p2.println("width='60'");
p2.println("height='31'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-22'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c13}]]></textFieldExpression>");
p2.println("</textField>");
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='400'");
p2.println("y='66'");
p2.println("width='60'");
p2.println("height='32'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-23'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c14}]]></textFieldExpression>");
p2.println("</textField>");
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='320'");
p2.println("y='3'");
p2.println("width='80'");
p2.println("height='95'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-24'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c11}]]></textFieldExpression>");
p2.println("</textField>");


p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='270'");
p2.println("y='3'");
p2.println("width='50'");
p2.println("height='40'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-30'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c6}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='270'");
p2.println("y='43'");
p2.println("width='50'");
p2.println("height='55'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-25'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c10}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='210'");
p2.println("y='3'");
p2.println("width='60'");
p2.println("height='95'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-26'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c9}]]></textFieldExpression>");
p2.println("</textField>");
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='150'");
p2.println("y='3'");
p2.println("width='60'");
p2.println("height='47'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-27'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c7}]]></textFieldExpression>");
p2.println("</textField>");
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='150'");
p2.println("y='51'");
p2.println("width='60'");
p2.println("height='46'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-28'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c8}]]></textFieldExpression>");
p2.println("</textField>");
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='70'");
p2.println("y='3'");
p2.println("width='80'");
p2.println("height='95'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-29'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c5}]]></textFieldExpression>");
p2.println("</textField>");
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='0'");
p2.println("y='3'");
p2.println("width='70'");
p2.println("height='95'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-31'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c4}]]></textFieldExpression>");
p2.println("</textField>");

		x=540;y=3;
		c=15+n+1;
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<4;j++) 
			{
				for(int k=0;k<3;k++) 
				{
					ctextkey="c"+c;
					
						p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
						p2.println("mode='Transparent'");
						p2.println("x='"+x+"'");
						p2.println("y='"+y+"'");
						p2.println("width='60'");
						p2.println("height='24'");
						p2.println("forecolor='#000000'");
						p2.println("backcolor='#FFFFFF'");
						p2.println("key='textField-30'");
						p2.println("stretchType='NoStretch'");
						p2.println("positionType='Float'");
						p2.println("isPrintRepeatedValues='true'");
						p2.println("isRemoveLineWhenBlank='false'");
						p2.println("isPrintInFirstWholeBand='false'");
						p2.println("isPrintWhenDetailOverflows='false'/>");
						p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
						p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
						p2.println("</textElement>");
						p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
						p2.println("</textField>");
					
					x += 60;
					c ++;
				}//k
				y += 24;
				x = 540+180*i;
			}//j
			y = 3;
			x = x + 180;
		}//i for3

p2.println("<line direction='TopDown'>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='2'");
p2.println("width='"+(1175-((3-n)*180))+"'");
p2.println("height='0'");
p2.println("forecolor='#666666'");
p2.println("backcolor='#999999'");
p2.println("key='line-45'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
p2.println("</line>");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='520'");
p2.println("y='3'");
p2.println("width='20'");
p2.println("height='95'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-37'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Q1");
p2.println("");
p2.println("");
p2.println("Q2");
p2.println("");
p2.println("");
p2.println("Q3");
p2.println("");
p2.println("");
p2.println("Q4]]></text>");
p2.println("</staticText>");

		x=(1115-((3-n)*180));y=3;
		c=15+(13*n)+1;
		for(int j=0;j<3;j++)
		{
				ctextkey="c"+c;

					p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
					p2.println("mode='Transparent'");
					p2.println("x='"+x+"'");
					p2.println("y='"+y+"'");
					p2.println("width='60'");
					p2.println("height='32'");
					p2.println("forecolor='#000000'");
					p2.println("backcolor='#FFFFFF'");
					p2.println("key='textField-55'");
					p2.println("stretchType='NoStretch'");
					p2.println("positionType='Float'");
					p2.println("isPrintRepeatedValues='true'");
					p2.println("isRemoveLineWhenBlank='false'");
					p2.println("isPrintInFirstWholeBand='false'");
					p2.println("isPrintWhenDetailOverflows='false'/>");
					p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
					p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
					p2.println("</textElement>");
					p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
					p2.println("</textField>");
			
				y += 32;
				c += 1;

		}//for4

p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='"+(1080-((3-n)*180))+"'");
p2.println("y='3'");
p2.println("width='35'");
p2.println("height='95'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-119'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
//p2.println("<text><![CDATA[P. Disb.");
p2.println("<text><![CDATA[");
p2.println("");
p2.println("P. Disb");
p2.println("");
p2.println("");
p2.println("");
p2.println("Disb.");
p2.println("");
p2.println("");
p2.println("");
p2.println("Exp.]]></text>");
p2.println("</staticText>");
p2.println("<line direction='TopDown'>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='99'");
p2.println("width='"+(1175-((3-n)*180))+"'");
p2.println("height='0'");
p2.println("forecolor='#666666'");
p2.println("backcolor='#999999'");
p2.println("key='line-46'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
p2.println("</line>");
p2.println("</band>");
p2.println("</detail>");
p2.println("<columnFooter>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</columnFooter>");
p2.println("<pageFooter>");
p2.println("<band height='22'  isSplitAllowed='true' >");
p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='2'");
p2.println("y='4'");
p2.println("width='300'");
p2.println("height='16'");
p2.println("forecolor='#3333FF'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-4'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Times-Roman' pdfFontName='Times-Roman' size='12' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='CP1252' isStrikeThrough='false' />");
p2.println("</textElement>");

int amountsUnitCode = Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));
switch(amountsUnitCode)
{
case AmpARFilter.AMOUNT_OPTION_IN_UNITS:
	p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"  \"]]></textFieldExpression>");
	break;
	
case AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS:
	p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\" * All the amounts are in thousands (000) \"]]></textFieldExpression>");
	break;

case AmpARFilter.AMOUNT_OPTION_IN_MILLIONS:
	p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\" * All the amounts are in millions (000 000) \"]]></textFieldExpression>");
	break;
}


p2.println("</textField>");

p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='955'");
p2.println("y='4'");
p2.println("width='174'");
p2.println("height='14'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-5'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Right' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Helvetica' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='CP1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Page \" + $V{PAGE_NUMBER} + \" of \"]]></textFieldExpression>");
p2.println("</textField>");

p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Report' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='1137'");
p2.println("y='4'");
p2.println("width='36'");
p2.println("height='14'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-6'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Helvetica' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='CP1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"\"+$V{PAGE_NUMBER}]]></textFieldExpression>");
p2.println("</textField>");
p2.println("</band>");
p2.println("</pageFooter>");


p2.println("<summary>");
p2.println("<band height='1'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</summary>");
p2.println("</jasperReport>");


		p2.close();
		}
		catch (Exception e)
		{
			//System.err.println("File error");
		}
	}//CreateJrxml
}
