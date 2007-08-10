package org.digijava.module.aim.helper;

import java.io.*;
import java.lang.Math;

public class ParisIndicator7Jrxml
{
	public void createJrxml(String filePath, int cols, int rows,String type)
	throws IOException
	{
		try
		{
			 FileOutputStream out,out2; // declare a file output object
     PrintStream p,p2; // declare a print stream object

				out = new FileOutputStream(filePath);
				out2 = new FileOutputStream(filePath,true);
				p = new PrintStream(out);
				p.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
				//p.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
				p2 = new PrintStream(out2);
				////System.out.println("creating now- dynamic trend...");
				int a = (cols/4) * 270 + 80;
				int b = (cols/4);
				b= (b*240)+80;
				
				p2.println("<!-- Created with iReport - A designer for JasperReports -->");
				p2.println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
				p2.println("<jasperReport");
				p2.println("		 name='indicator7pdf'");
				p2.println("		 columnCount='1'");
				p2.println("		 printOrder='Vertical'");
				p2.println("		 orientation='Portrait'");
				p2.println("		 pageWidth='"+a+"'");
				p2.println("		 pageHeight='842'");
				p2.println("		 columnWidth='535'");
				p2.println("		 columnSpacing='0'");
				p2.println("		 leftMargin='30'");
				p2.println("		 rightMargin='30'");
				p2.println("		 topMargin='20'");
				p2.println("		 bottomMargin='20'");
				p2.println("		 whenNoDataType='NoPages'");
				p2.println("		 isTitleNewPage='false'");
				p2.println("		 isSummaryNewPage='false'>");
				p2.println("	<property name='ireport.scriptlethandling' value='2' />");
				p2.println("<parameter name='qu' isForPrompting='false' class='java.lang.String'>");
				p2.println("<defaultValueExpression ><![CDATA[\"select * from my_table\"]]></defaultValueExpression>");
				p2.println("</parameter>");
				p2.println("<parameter name='nam' isForPrompting='false' class='java.lang.String'>");
				p2.println("<defaultValueExpression ><![CDATA[\"zzzz\"]]></defaultValueExpression>");
				p2.println("</parameter>");
				p2.println("<queryString><![CDATA[$P!{qu}]]></queryString>");

//							DYNAMIC CCCCCCCCC
							String dc;
							int colCnt =13;
							////System.out.println(" Cnt = " + colCnt);
//							 gets the no of fields = 4 constants fields + YearCnt*3 + yearCnt
							for(int k=1; k<=cols; k++)
							{
								////System.out.println("k="+k);
								dc="m"+k;
								p2.println("<field name='"+dc+"' class='java.lang.String'/>");
							}

				p2.println("<group  name='Data' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
				p2.println("<groupExpression><![CDATA[$F{m1}]]></groupExpression>");
				p2.println("<groupHeader>");

				p2.println("</groupHeader>");
				p2.println("<groupFooter>");
				p2.println("<band height='0'  isSplitAllowed='true' >");
				p2.println("</band>");
				p2.println("</groupFooter>");
				p2.println("</group>");
				
				
				
				
					p2.println("		<background>");
					p2.println("			<band height='0'  isSplitAllowed='true' >");
					p2.println("			</band>");
					p2.println("		</background>");
					p2.println("		<title>");
					p2.println("			<band height='22'  isSplitAllowed='true' >");
					p2.println("				<staticText>");
					p2.println("					<reportElement");
					p2.println("						mode='Opaque'");
					p2.println("						x='141'");
					p2.println("						y='0'");
					p2.println("						width='300'");
					p2.println("						height='22'");
					p2.println("						forecolor='#000000'");
					p2.println("						backcolor='#FFFFFF'");
					p2.println("						key='staticText-1'");
					p2.println("						stretchType='NoStretch'");
					p2.println("						positionType='FixRelativeToTop'");
					p2.println("						isPrintRepeatedValues='true'");
					p2.println("						isRemoveLineWhenBlank='false'");
					p2.println("						isPrintInFirstWholeBand='false'");
					p2.println("						isPrintWhenDetailOverflows='false'/>");
					p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
					p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='18' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
					p2.println("					</textElement>");
					p2.println("				<text><![CDATA[PARIS REPORT 7]]></text>");
					p2.println("				</staticText>");
					p2.println("			</band>");
					p2.println("		</title>");
					p2.println("		<pageHeader>");
					p2.println("			<band height='49'  isSplitAllowed='true' >");
					p2.println("			</band>");
					p2.println("		</pageHeader>");
					p2.println("		<columnHeader>");
					p2.println("			<band height='125'  isSplitAllowed='true' >");
					
					p2.println("				<staticText>");
					p2.println("					<reportElement");
					p2.println("						mode='Opaque'");
					p2.println("						x='0'");
					p2.println("						y='0'");
					p2.println("						width='80'");
					p2.println("						height='125'");
					p2.println("						forecolor='#000000'");
					p2.println("						backcolor='#CCCCCC'");
					p2.println("						key='staticText-2'");
					p2.println("						stretchType='NoStretch'");
					p2.println("						positionType='FixRelativeToTop'");
					p2.println("						isPrintRepeatedValues='true'");
					p2.println("						isRemoveLineWhenBlank='false'");
					p2.println("						isPrintInFirstWholeBand='false'");
					p2.println("						isPrintWhenDetailOverflows='false'/>");
					p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
					p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
					p2.println("					</textElement>");
					p2.println("				<text><![CDATA[Donors]]></text>");
					p2.println("				</staticText>");
					
		
					String ctextkey;
					int x=80;
					int count = 0;					
					for(int j=2;j<=cols;j+=4)
					{
						
						count++;
						ctextkey="m"+j;						
						p2.println("				<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='"+x+"'");
						p2.println("						y='0'");
						p2.println("						width='240'");
						p2.println("						height='25'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='textField-3'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
						p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
						p2.println("					</textElement>");
						p2.println("				<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
						p2.println("				</textField>");
							x=x+240;											
					}
					
					
					//in here
					x = 80;
					for(int j =0;j<count;j++)
					{
					p2.println("				<staticText>");
					p2.println("					<reportElement");
					p2.println("						mode='Opaque'");
					p2.println("						x='"+x+"'");
					p2.println("						y='25'");
					p2.println("						width='80'");
					p2.println("						height='100'");
					p2.println("						forecolor='#000000'");
					p2.println("						backcolor='#CCCCCC'");
					p2.println("						key='staticText-7'");
					p2.println("						stretchType='NoStretch'");
					p2.println("						positionType='FixRelativeToTop'");
					p2.println("						isPrintRepeatedValues='true'");
					p2.println("						isRemoveLineWhenBlank='false'");
					p2.println("						isPrintInFirstWholeBand='false'");
					p2.println("						isPrintWhenDetailOverflows='false'/>");
					p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
					p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
					p2.println("					</textElement>");
					p2.println("				<text><![CDATA[ Aid flows to the government sector scheduled for fiscal year]]></text>");
					p2.println("				</staticText>");
					if(type.equals("pdf"))
					{
						p2.println("				<line direction='TopDown'>");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='"+x+"'");
						p2.println("						y='25'");
						p2.println("						width='0'");
						p2.println("						height='100'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='line-11'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
						p2.println("				</line>");
					}
					x=x+80;
					
					p2.println("				<staticText>");
					p2.println("					<reportElement");
					p2.println("						mode='Opaque'");
					p2.println("						x='"+x+"'");
					p2.println("						y='25'");
					p2.println("						width='80'");
					p2.println("						height='100'");
					p2.println("						forecolor='#000000'");
					p2.println("						backcolor='#CCCCCC'");
					p2.println("						key='staticText-8'");
					p2.println("						stretchType='NoStretch'");
					p2.println("						positionType='FixRelativeToTop'");
					p2.println("						isPrintRepeatedValues='true'");
					p2.println("						isRemoveLineWhenBlank='false'");
					p2.println("						isPrintInFirstWholeBand='false'");
					p2.println("						isPrintWhenDetailOverflows='false'/>");
					p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
					p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
					p2.println("					</textElement>");
					p2.println("				<text><![CDATA[ Total Aid flows disbursed to the government sector]]></text>");
					p2.println("				</staticText>");
					if(type.equals("pdf"))
					{
						p2.println("				<line direction='TopDown'>");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='"+x+"'");
						p2.println("						y='25'");
						p2.println("						width='0'");
						p2.println("						height='100'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='line-11'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
						p2.println("				</line>");
					}
					x=x+80;
					
					
					p2.println("				<staticText>");
					p2.println("					<reportElement");
					p2.println("						mode='Opaque'");
					p2.println("						x='"+x+"'");
					p2.println("						y='25'");
					p2.println("						width='80'");
					p2.println("						height='100'");
					p2.println("						forecolor='#000000'");
					p2.println("						backcolor='#CCCCCC'");
					p2.println("						key='staticText-9'");
					p2.println("						stretchType='NoStretch'");
					p2.println("						positionType='FixRelativeToTop'");
					p2.println("						isPrintRepeatedValues='true'");
					p2.println("						isRemoveLineWhenBlank='false'");
					p2.println("						isPrintInFirstWholeBand='false'");
					p2.println("						isPrintWhenDetailOverflows='false'/>");
					p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
					p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
					p2.println("					</textElement>");
					p2.println("				<text><![CDATA[ Proportion of aid to the government sector disbursed within the fiscal year it was scheduled]]></text>");
					p2.println("				</staticText>");
					if(type.equals("pdf"))
					{
						p2.println("				<line direction='TopDown'>");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='"+x+"'");
						p2.println("						y='25'");
						p2.println("						width='0'");
						p2.println("						height='100'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='line-11'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
						p2.println("				</line>");
					}
					x=x+80;
					}
					// till here
					if(type.equals("pdf"))
					{
						x=80;
						for(int j=2;j<=cols;j+=4)
						{
							p2.println("				<line direction='TopDown'>");
							p2.println("					<reportElement");
							p2.println("						mode='Opaque'");
							p2.println("						x='"+x+"'");
							p2.println("						y='0'");
							p2.println("						width='0'");
							p2.println("						height='125'");
							p2.println("						forecolor='#000000'");
							p2.println("						backcolor='#FFFFFF'");
							p2.println("						key='line-11'");
							p2.println("						stretchType='NoStretch'");
							p2.println("						positionType='FixRelativeToTop'");
							p2.println("						isPrintRepeatedValues='true'");
							p2.println("						isRemoveLineWhenBlank='false'");
							p2.println("						isPrintInFirstWholeBand='false'");
							p2.println("						isPrintWhenDetailOverflows='false'/>");
							p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
							p2.println("				</line>");
							x= x+240;
						}					
						int c =(cols/4)*240;
						p2.println("				<line direction='TopDown'>");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='80'");
						p2.println("						y='25'");
						p2.println("						width='"+c+"'");
						p2.println("						height='0'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='line-11'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
						p2.println("				</line>");
						p2.println("				<line direction='TopDown'>");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='0'");
						p2.println("						y='0'");
						p2.println("						width='0'");
						p2.println("						height='125'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='line-11'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
						p2.println("				</line>");
						p2.println("				<line direction='TopDown'>");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='"+b+"'");
						p2.println("						y='0'");
						p2.println("						width='0'");
						p2.println("						height='125'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='line-11'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
						p2.println("				</line>");
						p2.println("				<line direction='TopDown'>");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='0'");
						p2.println("						y='0'");
						p2.println("						width='"+b+"'");
						p2.println("						height='0'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='line-29'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
						p2.println("				</line>");
						p2.println("				<line direction='TopDown'>");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='0'");
						p2.println("						y='124'");
						p2.println("						width='"+b+"'");
						p2.println("						height='0'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='line-29'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
						p2.println("				</line>");
					}
					p2.println("			</band>");					
					p2.println("		</columnHeader>");
					p2.println("		<detail>");
					p2.println("			<band height='30'  isSplitAllowed='true' >");
					p2.println("				<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >");
					p2.println("					<reportElement");
					p2.println("						mode='Opaque'");
					p2.println("						x='0'");
					p2.println("						y='0'");
					p2.println("						width='80'");
					p2.println("						height='30'");
					p2.println("						forecolor='#000000'");
					p2.println("						backcolor='#FFFFFF'");
					p2.println("						key='textField-2'");
					p2.println("						stretchType='NoStretch'");
					p2.println("						positionType='FixRelativeToTop'");
					p2.println("						isPrintRepeatedValues='true'");
					p2.println("						isRemoveLineWhenBlank='false'");
					p2.println("						isPrintInFirstWholeBand='false'");
					p2.println("						isPrintWhenDetailOverflows='false'/>");
					p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
					p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
					p2.println("					</textElement>");
					p2.println("				<textFieldExpression   class='java.lang.String'><![CDATA[$F{m1}]]></textFieldExpression>");
					p2.println("				</textField>");
					x=80;
					boolean flag = true;
					for(int j=3;j<=cols;j++)
					{
						flag = true;
						ctextkey="m"+j;
						for(int i=0;i<=cols;i++)
						{
							if((4*i)+2 ==j)
							{
								////System.out.println( " in equals");
								flag = false;
								break;
							}
						}
						////System.out.println(" this is  j "+ j + " ctextkey "+ ctextkey);
						if(flag)
						{	
							p2.println("				<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >");
							p2.println("					<reportElement");
							p2.println("						mode='Opaque'");
							p2.println("						x='"+x+"'");
							p2.println("						y='0'");
							p2.println("						width='80'");
							p2.println("						height='30'");
							p2.println("						forecolor='#000000'");
							p2.println("						backcolor='#FFFFFF'");
							p2.println("						key='textField-20'");
							p2.println("						stretchType='NoStretch'");
							p2.println("						positionType='FixRelativeToTop'");
							p2.println("						isPrintRepeatedValues='true'");
							p2.println("						isRemoveLineWhenBlank='false'");
							p2.println("						isPrintInFirstWholeBand='false'");
							p2.println("						isPrintWhenDetailOverflows='false'/>");
							p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
							p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
							p2.println("					</textElement>");
							p2.println("				<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
							p2.println("				</textField>");
							if(type.equals("pdf"))
							{
								p2.println("				<line direction='TopDown'>");
								p2.println("					<reportElement");
								p2.println("						mode='Opaque'");
								p2.println("						x='"+x+"'");
								p2.println("						y='0'");
								p2.println("						width='0'");
								p2.println("						height='30'");
								p2.println("						forecolor='#000000'");
								p2.println("						backcolor='#FFFFFF'");
								p2.println("						key='line-11'");
								p2.println("						stretchType='NoStretch'");
								p2.println("						positionType='FixRelativeToTop'");
								p2.println("						isPrintRepeatedValues='true'");
								p2.println("						isRemoveLineWhenBlank='false'");
								p2.println("						isPrintInFirstWholeBand='false'");
								p2.println("						isPrintWhenDetailOverflows='false'/>");
								p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
								p2.println("				</line>");
							}
							x=x+80;
						}
					
					}
					if(type.equals("pdf"))
					{
						p2.println("				<line direction='TopDown'>");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='0'");
						p2.println("						y='0'");
						p2.println("						width='0'");
						p2.println("						height='30'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='line-11'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
						p2.println("				</line>");
						p2.println("				<line direction='TopDown'>");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='"+b+"'");
						p2.println("						y='0'");
						p2.println("						width='0'");
						p2.println("						height='30'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='line-11'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
						p2.println("				</line>");
						p2.println("				<line direction='TopDown'>");
						p2.println("					<reportElement");
						p2.println("						mode='Opaque'");
						p2.println("						x='0'");
						p2.println("						y='29'");
						p2.println("						width='"+b+"'");
						p2.println("						height='0'");
						p2.println("						forecolor='#000000'");
						p2.println("						backcolor='#FFFFFF'");
						p2.println("						key='line-11'");
						p2.println("						stretchType='NoStretch'");
						p2.println("						positionType='FixRelativeToTop'");
						p2.println("						isPrintRepeatedValues='true'");
						p2.println("						isRemoveLineWhenBlank='false'");
						p2.println("						isPrintInFirstWholeBand='false'");
						p2.println("						isPrintWhenDetailOverflows='false'/>");
						p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
						p2.println("				</line>");
					}
					p2.println("			</band>");
					p2.println("		</detail>");
					p2.println("		<columnFooter>");
					p2.println("			<band height='0'  isSplitAllowed='true' >");
					p2.println("			</band>");
					p2.println("		</columnFooter>");
					p2.println("<pageFooter>");
					p2.println("<band height='22'  isSplitAllowed='true' >");
					p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
					p2.println("mode='Transparent'");
					p2.println("x='2'");
					p2.println("y='4'");
					p2.println("width='250'");
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
					p2.println("x='464'");
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
					p2.println("x='641'");
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
					p2.println("		<summary>");
					p2.println("			<band height='0'  isSplitAllowed='true' >");
					p2.println("			</band>");
					p2.println("		</summary>");
					p2.println("</jasperReport>");
					



				
		////System.out.println("hyup");			
		}
		
		catch(Exception e)
		{
			
		}
	}
}