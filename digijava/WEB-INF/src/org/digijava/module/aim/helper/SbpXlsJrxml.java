
package org.digijava.module.aim.helper;
import java.io.*;

public class SbpXlsJrxml
{
	 public void createJrxml(int cnt, String filePath)
		throws IOException
       {		
		  try
			{
			System.out.println("DYNAMIC Multi-JRXML..Sector by project");

//			File fopen = new File("TrendAnalysisPdf_new.jrxml");
			FileOutputStream out2; // declare a file output object
			PrintStream p2; // declare a print stream object
			File fopen = new File(filePath);	

			out2 = new FileOutputStream(fopen);
			p2 = new PrintStream(out2);
			p2.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			System.out.println("creating now- dynamic trend...");

int n=cnt;//cnt;
int totalIndex = n;
int center=0;//(12-n)*30;
int x=(0+center),x1=0,y=0,y1=0,xl=0,yl=0;
int textkey=11,linekey=21,c=0;
String ctextkey;
int pagesize=(60*n)+120;

p2.println("<!-- Created with iReport - A designer for JasperReports -->");
p2.println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
p2.println("<jasperReport");
p2.println("name='SectorByProjectXls'");

p2.println("columnCount='1'");
p2.println("printOrder='Vertical'");
p2.println("orientation='Landscape'");
p2.println("pageWidth='1082'");
p2.println("pageHeight='595'");
p2.println("columnWidth='535'");
p2.println("columnSpacing='0'");
p2.println("leftMargin='15'");
p2.println("rightMargin='15'");
p2.println("topMargin='21'");
p2.println("bottomMargin='21'");
p2.println("whenNoDataType='NoPages'");
p2.println("isTitleNewPage='false'");
p2.println("isSummaryNewPage='false'>");
p2.println("<property name='ireport.scriptlethandling' value='2' />");
p2.println("<parameter name='qu' isForPrompting='false' class='java.lang.String'>");
p2.println("<defaultValueExpression ><![CDATA[\"select * from my_table\"]]></defaultValueExpression>");
p2.println("</parameter>");
p2.println("<parameter name='nam' isForPrompting='false' class='java.lang.String'>");
p2.println("<defaultValueExpression ><![CDATA[\"zzzz\"]]></defaultValueExpression>");
p2.println("</parameter>");
p2.println("<queryString><![CDATA[$P!{qu}]]></queryString>");

//DYNAMIC CCCCCCCCC
String dc;
int colcnt = 6+ (n*3) + n;

for(int k=1;k<=((4*n)+6+4+4);k++)
{
	dc="c"+k;
	p2.println("<field name='"+dc+"' class='java.lang.String'/>");
	
}

p2.println("<group  name='GroupBySector' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
p2.println("<groupExpression><![CDATA[$F{c4}]]></groupExpression>");
p2.println("<groupHeader>");
p2.println("<band height='20'  isSplitAllowed='true' >");

p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='1'");
//p2.println("width='"+((240*n)+82)+"'");
p2.println("width='1040'");
p2.println("height='18'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#999999'");
p2.println("key='textField'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\" Sector: \"+ $F{c4}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("</band>");
p2.println("</groupHeader>");
p2.println("<groupFooter>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</groupFooter>");
p2.println("</group>");



p2.println("<group  name='GroupByDonor' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
p2.println("<groupExpression><![CDATA[$F{c5}]]></groupExpression>");
p2.println("<groupHeader>");
p2.println("<band height='20'  isSplitAllowed='true' >");

p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='1'");
//p2.println("width='"+(440+(n-1)*180)+"'");
p2.println("width='1040'");
p2.println("height='18'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='textField-10'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='Float'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"\"+$F{c5}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("</band>");
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
p2.println("<band height='100'  isSplitAllowed='true' >");

p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='0'");
p2.println("width='691'");
p2.println("height='20'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='staticText-4'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='14' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[ Annual Report By Sector ]]></text>");
p2.println("</staticText>");

p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='20'");
p2.println("width='600'");
p2.println("height='20'");
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
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Workspace :   \"+$F{c1}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='40'");
p2.println("width='800'");
p2.println("height='14'");
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
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Filters:   \"+$F{c2}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='54'");
p2.println("width='1040'");
p2.println("height='16'");
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
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"              \"+$F{c3}]]></textFieldExpression>");
p2.println("</textField>");


// xls non repeating header....

p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='70'");
p2.println("width='82'");
p2.println("height='30'");
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
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Donor / Project]]></text>");
p2.println("</staticText>");


x=82;y=0;
x1=82;y1=0;
c=7;
/*
  for1
loop for generating dynamic fields for Header Band part 
(Title+WorkSpace+Filters+)
and may be years also.
*/

for(int j=0;j<n;j++) 
{
ctextkey="c"+c;

p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='"+x+"'");
p2.println("y='70'");
p2.println("width='240'");
p2.println("height='15'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='textField-11'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Year: \"+$F{"+ctextkey+"}]]></textFieldExpression>");
p2.println("</textField>");

c +=5;
x += 240;

p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='"+x1+"'");
p2.println("y='85'");
p2.println("width='240'");
p2.println("height='15'");
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
p2.println("<text><![CDATA[Commit.      Planned Disb.       Disb           Exp.]]></text>");
p2.println("</staticText>");

x1 += 240;

}
//-------------

p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='"+x+"'");
p2.println("y='70'");
p2.println("width='240'");
p2.println("height='15'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='textField-11'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[ Total ]]></text>");
p2.println("</staticText>");


p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='"+x1+"'");
p2.println("y='85'");
p2.println("width='240'");
p2.println("height='15'");
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
p2.println("<text><![CDATA[Commit.       Planned Disb.        Disb          Exp.]]></text>");
p2.println("</staticText>");

//---------


p2.println("</band>");
p2.println("</title>");
p2.println("<pageHeader>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</pageHeader>");
p2.println("<columnHeader>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</columnHeader>");
p2.println("<detail>");
p2.println("<band height='46'  isSplitAllowed='true' >");

c=8;
x =82;y=0;
//for2
System.out.println("n"+n);
for(int j=0;j<n;j++)
{
ctextkey="c"+c;
System.out.println("cc2"+c);

p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='"+x+"'");
p2.println("y='0'");
p2.println("width='60'");
p2.println("height='45'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-11'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
p2.println("</textField>");

x +=60;
c++;
ctextkey="c"+c;
System.out.println("cc3"+c);


p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='"+x+"'");
p2.println("y='0'");
p2.println("width='60'");
p2.println("height='45'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-11'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
p2.println("</textField>");

x +=60;
c++;
ctextkey="c"+c;
System.out.println("cc4"+c);
p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='"+x+"'");
p2.println("y='0'");
p2.println("width='60'");
p2.println("height='45'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-27'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
p2.println("</textField>");

x +=60;
c++;
ctextkey="c"+c;
System.out.println("cc5"+c);
p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='"+x+"'");
p2.println("y='0'");
p2.println("width='60'");
p2.println("height='45'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-24'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
p2.println("</textField>");

x += 60;
c += 2;
//System.out.println("ccccccccccc  "+c);
}

c=6+(n*4)+1+3;
//x =622;
y=0;
//for3
for(int j=0;j<3+1;j++)
{
ctextkey="c"+c;
System.out.println("cc"+c);
p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='"+x+"'");
p2.println("y='0'");
p2.println("width='60'");
p2.println("height='45'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-11'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
p2.println("</textField>");

x+=60;
c++;
}

p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='0'");
p2.println("y='0'");
p2.println("width='82'");
p2.println("height='45'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-11'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c6}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("<line direction='TopDown'>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='46'");
p2.println("width='"+(262+((n-1)*270))+"'");
p2.println("height='0'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='line-5'");
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
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\" * All the amounts are in thousands (000) \"]]></textFieldExpression>");
p2.println("</textField>");
p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='595'");
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
p2.println("x='774'");
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
p2.println("<band height='26'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</summary>");
p2.println("</jasperReport>");

		p2.close();
		}
			catch (Exception e)
			{
				System.err.println("File error" + e);
			}
	}//CreateJrxml
}