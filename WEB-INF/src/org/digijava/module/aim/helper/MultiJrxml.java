package org.digijava.module.aim.helper;

import java.io.*;

public class MultiJrxml
{
	public void createJrxml(int cnt, String filePath, int height)
	throws IOException
	{
		try
		{
			//System.out.println( cnt +"DYNAMIC Multi-JRXML..");
                    FileOutputStream out,out2; // declare a file output object
                    PrintStream p,p2; // declare a print stream object

					out = new FileOutputStream(filePath);
					out2 = new FileOutputStream(filePath,true);
					p = new PrintStream(out);
					p.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
					p2 = new PrintStream(out2);

         	System.out.println("creating now- dynamic trend...");

			int n=cnt;//cnt;
			int center=0;//(12-n)*30;
			int x=(0+center),x1=0,y=0,y1=0,x2=0,y2=0;
			int textkey=11,linekey=21,c=0;
			String ctextkey="";


			p2.println("<!-- Created with iReport - A designer for JasperReports -->");
			p2.println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
			p2.println("<jasperReport");
			p2.println("name='MultilateralDonorPdf'");
			p2.println("columnCount='1'");
			p2.println("printOrder='Vertical'");
			p2.println("orientation='Landscape'");
			p2.println("pageWidth='842'");
			p2.println("pageHeight='595'");
			p2.println("columnWidth='535'");
			p2.println("columnSpacing='0'");
			p2.println("leftMargin='20'");
			p2.println("rightMargin='20'");
			p2.println("topMargin='20'");
			p2.println("bottomMargin='20'");
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

//						DYNAMIC CCCCCCCCC
						String dc;
						int colCnt = 5+ (n*4) + 5 + 3;
						System.out.println(" Cnt = " + colCnt);
//						 gets the no of fields = 4 constants fields + YearCnt*3 + yearCnt
						for(int k=1; k<=colCnt; k++)
						{
							System.out.println("k="+k);
							dc="c"+k;
							p2.println("<field name='"+dc+"' class='java.lang.String'/>");
						}

			p2.println("<group  name='Data' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
			p2.println("<groupExpression><![CDATA[$F{c4}]]></groupExpression>");
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
			p2.println("<band height='41'  isSplitAllowed='true' >");
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='43'");
			p2.println("y='0'");
			p2.println("width='700'");
			p2.println("height='20'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='staticText-23'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='14' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Annual Report By Type of Asssistance]]></text>");
			p2.println("</staticText>");
			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='20'");
			p2.println("width='500'");
			p2.println("height='20'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField'");
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
			p2.println("<band height='80'  isSplitAllowed='true' >");
			
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='30'");
			p2.println("width='150'");
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
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[                Donor]]></text>");
			p2.println("</staticText>");

			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='0'");
			p2.println("width='700'");
			p2.println("height='15'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField'");
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
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='15'");
			p2.println("width='700'");
			p2.println("height='15'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField'");
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

			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='60'");
			p2.println("width='"+(790-(4-n)*85)+"'");
			p2.println("height='20'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='textField'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"   \"+$F{c4}]]></textFieldExpression>");
			p2.println("</textField>");

						x=150;y=0;
						c=6;
						for(int j=0;j<n;j++)
						{
							ctextkey="c"+c;

								p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
								p2.println("mode='Opaque'");
								p2.println("x='"+x+"'");
								p2.println("y='30'");
								p2.println("width='85'");
								p2.println("height='30'");
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
								p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
								p2.println("</textElement>");
								p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Year: \"+$F{"+ctextkey+"}]]></textFieldExpression>");
								p2.println("</textField>");

								p2.println("<line direction='TopDown'>");
								p2.println("<reportElement");
								p2.println("mode='Transparent'");
								p2.println("x='"+x+"'");
								p2.println("y='30'");
								p2.println("width='0'");
								p2.println("height='30'");
								p2.println("forecolor='#666666'");
								p2.println("backcolor='#999999'");
								p2.println("key='line-1'");
								p2.println("stretchType='NoStretch'");
								p2.println("positionType='FixRelativeToTop'");
								p2.println("isPrintRepeatedValues='true'");
								p2.println("isRemoveLineWhenBlank='false'");
								p2.println("isPrintInFirstWholeBand='false'");
								p2.println("isPrintWhenDetailOverflows='false'/>");
								p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
								p2.println("</line>");


							x+=85;
							c+=5;

						}// for2

			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='" + (490-(4-n)*85) +"'");
			p2.println("y='30'");
			p2.println("width='300'");
			p2.println("height='14'");
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
			p2.println("<text><![CDATA[Total  ]]></text>");
			p2.println("</staticText>");


/*			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='31'");
			p2.println("width='"+(790-(4-n)*85)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#666666'");
			p2.println("backcolor='#999999'");
			p2.println("key='line-38'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
			p2.println("</line>");
			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='79'");
			p2.println("width='"+(790-(4-n)*85)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#666666'");
			p2.println("backcolor='#999999'");
			p2.println("key='line-39'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
			p2.println("</line>");
*/

			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='61'");
			p2.println("width='"+(790-(4-n)*85)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#666666'");
			p2.println("backcolor='#999999'");
			p2.println("key='line-44'");
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
			p2.println("x='"+ (490-(4-n)*85) +"'");
			p2.println("y='44'");
			p2.println("width='60'");
			p2.println("height='16'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-101'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Commitment]]></text>");
			p2.println("</staticText>");

								x=150+(n*85);
								p2.println("<line direction='TopDown'>");
								p2.println("<reportElement");
								p2.println("mode='Transparent'");
								p2.println("x='"+x+"'");
								p2.println("y='31'");
								p2.println("width='0'");
								p2.println("height='30'");
								p2.println("forecolor='#666666'");
								p2.println("backcolor='#999999'");
								p2.println("key='line-1'");
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
			p2.println("x='"+  (550-(4-n)*85) +"'");
			p2.println("y='44'");
			p2.println("width='60'");
			p2.println("height='16'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-102'");
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
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='"+ (610-(4-n)*85) +"'");
			p2.println("y='44'");
			p2.println("width='60'");
			p2.println("height='16'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-103'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Disbursment]]></text>");
			p2.println("</staticText>");
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='"+  (670-(4-n)*85) +"'");
			p2.println("y='44'");
			p2.println("width='60'");
			p2.println("height='16'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-104'");
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
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='"+ (730-(4-n)*85) +"'");
			p2.println("y='44'");
			p2.println("width='60'");
			p2.println("height='16'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-105'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Undisbursed]]></text>");
			p2.println("</staticText>");

			x=150+(n*85)+60;
			for(int i=0;i<4;i++)
			{

								p2.println("<line direction='TopDown'>");
								p2.println("<reportElement");
								p2.println("mode='Transparent'");
								p2.println("x='"+x+"'");
								p2.println("y='45'");
								p2.println("width='0'");
								p2.println("height='16'");
								p2.println("forecolor='#666666'");
								p2.println("backcolor='#999999'");
								p2.println("key='line-11'");
								p2.println("stretchType='NoStretch'");
								p2.println("positionType='FixRelativeToTop'");
								p2.println("isPrintRepeatedValues='true'");
								p2.println("isRemoveLineWhenBlank='false'");
								p2.println("isPrintInFirstWholeBand='false'");
								p2.println("isPrintWhenDetailOverflows='false'/>");
								p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
								p2.println("</line>");

			x+=60;
			}
			x=150+(n*85);
								p2.println("<line direction='TopDown'>");
								p2.println("<reportElement");
								p2.println("mode='Opaque'");
								p2.println("x='"+x+"'");
								p2.println("y='45'");
								p2.println("width='300'");
								p2.println("height='0'");
								p2.println("forecolor='#666666'");
								p2.println("backcolor='#999999'");
								p2.println("key='line-111'");
								p2.println("stretchType='NoStretch'");
								p2.println("positionType='FixRelativeToTop'");
								p2.println("isPrintRepeatedValues='true'");
								p2.println("isRemoveLineWhenBlank='false'");
								p2.println("isPrintInFirstWholeBand='false'");
								p2.println("isPrintWhenDetailOverflows='false'/>");
								p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
								p2.println("</line>");

			p2.println("<rectangle radius='0'>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='30'");
			p2.println("width='"+(791-(4-n)*85)+"'");
			p2.println("height='50'");
			p2.println("forecolor='#666666'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='rectangle-1'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToBottom'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid'	/>");
			p2.println("</rectangle>");


			p2.println("</band>");


			p2.println("</pageHeader>");
			p2.println("<columnHeader>");
			p2.println("<band height='0'  isSplitAllowed='true' >");
			p2.println("</band>");
			p2.println("</columnHeader>");
			p2.println("<detail>");

			y=0;
			p2.println("<band height='"+height+"'  isSplitAllowed='true' >");
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='90'");
			p2.println("y='"+y+"'");
			p2.println("width='60'");
			p2.println("height='"+height/4+"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-7'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Commitment]]></text>");
			p2.println("</staticText>");
			y+=height/4;
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='90'");
			p2.println("y='"+y+"'");
			p2.println("width='60'");
			p2.println("height='"+height/4+"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-100'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Planned Disb.]]></text>");
			p2.println("</staticText>");
			y+=height/4;
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='90'");
			p2.println("y='"+y+"'");
			p2.println("width='60'");
			p2.println("height='"+height/4+"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-8'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Disbursment]]></text>");
			p2.println("</staticText>");
			y+=height/4;
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='90'");
			p2.println("y='"+y+"'");
			p2.println("width='60'");
			p2.println("height='"+height/4+"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-9'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Expenditure]]></text>");
			p2.println("</staticText>");
			
			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='0'");
			p2.println("width='90'");
			p2.println("height='"+height+"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-10'");
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

						x=150;y=30;
						x1=150;
						c=7;
						for(int j=0;j<n;j++)
						{
							y=0;
							for(int k=0;k<4;k++)
							{
								ctextkey="c"+c;

								p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
								p2.println("mode='Transparent'");
								p2.println("x='"+x+"'");
								p2.println("y='"+y+"'");
								p2.println("width='85'");
								p2.println("height='"+height/4+"'");
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
								p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
								p2.println("</textElement>");
								p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
								p2.println("</textField>");
							
							y+=height/4;
							c++;

							}

							x+=85;
							c++;

							p2.println("<line direction='TopDown'>");
							p2.println("<reportElement");
							p2.println("mode='Transparent'");
							p2.println("x='"+x1+"'");
							p2.println("y='0'");
							p2.println("width='0'");
							p2.println("height='"+height+"'");
							p2.println("forecolor='#666666'");
							p2.println("backcolor='#999999'");
							p2.println("key='line-41'");
							p2.println("stretchType='NoStretch'");
							p2.println("positionType='FixRelativeToTop'");
							p2.println("isPrintRepeatedValues='true'");
							p2.println("isRemoveLineWhenBlank='false'");
							p2.println("isPrintInFirstWholeBand='false'");
							p2.println("isPrintWhenDetailOverflows='false'/>");
							p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
							p2.println("</line>");

							x1+=85;

						}// for1

/*			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='62'");
			p2.println("width='"+(790-(4-n)*85)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#666666'");
			p2.println("backcolor='#999999'");
			p2.println("key='line-40'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
			p2.println("</line>");
*/

						x=(490-(4-n)*85);
						x1=150+(n*85);
						y=0;
						c=(5+n*5+1);
						for(int j=0;j<5;j++)
						{
							ctextkey="c"+c;

							p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
							p2.println("mode='Transparent'");
							p2.println("x='"+x+"'");
							p2.println("y='0'");
							p2.println("width='60'");
							p2.println("height='"+height+"'");
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

							x+=60;
							c++;

							p2.println("<line direction='TopDown'>");
							p2.println("<reportElement");
							p2.println("mode='Transparent'");
							p2.println("x='"+x1+"'");
							p2.println("y='0'");
							p2.println("width='0'");
							p2.println("height='"+height+"'");
							p2.println("forecolor='#666666'");
							p2.println("backcolor='#999999'");
							p2.println("key='line-41'");
							p2.println("stretchType='NoStretch'");
							p2.println("positionType='FixRelativeToTop'");
							p2.println("isPrintRepeatedValues='true'");
							p2.println("isRemoveLineWhenBlank='false'");
							p2.println("isPrintInFirstWholeBand='false'");
							p2.println("isPrintWhenDetailOverflows='false'/>");
							p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
							p2.println("</line>");

							x1+=60;

						}// for2

			p2.println("<rectangle radius='0'>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='0'");
			p2.println("width='"+(791-(4-n)*85)+"'");
			p2.println("height='"+height+"'");
			p2.println("forecolor='#666666'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='rectangle-2'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToBottom'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid'	/>");
			p2.println("</rectangle>");

			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='90'");
			p2.println("y='0'");
			p2.println("width='0'");
			p2.println("height='"+height+"'");
			p2.println("forecolor='#666666'");
			p2.println("backcolor='#999999'");
			p2.println("key='line-38'");
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
			p2.println("x='568'");
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
			p2.println("x='753'");
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
						//System.err.println("File error");
					}
				}//CreateJrxml
}