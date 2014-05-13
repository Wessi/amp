<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<html>
			<head>
				<style>
		.table_text {
			font-family: sans-serif !important;
			color: #484846;
			padding: 5px 10px;
			font-size: 10px;
			font-weight: normal;
			border-collapse: collapse;
			border-style: none;
			font-family: sans-serif;

			line-height: 18px;
			border-spacing: 0;
		}
		
		TH {
			background-attachment: scroll;
			background-clip: border-box;
			background-color: #B5B5B5;
			background-image: url("http://www.budgettransparency.gov.tl/images/button_icons/overlay.png");
			background-origin: padding-box;
			background-repeat: repeat-x;
			border-collapse: collapse;
			border-color: #333333;
			border-style: none;
			border-width: 0;
			clear: none;
			color: #333333;
			display: table-cell;
			font-family: sans-serif;
			font-size: 13px;
			font-weight: bold;
			height: 13px;
			line-height: 13px;
			margin: 0;
			outline: 0 none #333333;
			padding: 3px 8px 3px 10px;
			text-align: center;
			vertical-align: middle;
			white-space: nowrap;
    
		}
		
		A {
			color: #05528B;
			font-size: 10px;
			text-decoration: underline;
		}
		A:link {
			color: #05528B;
			font-size: 10px;
			text-decoration: underline;
		}
		
		
		
		A:hover {
			color: #0E69B3;
			text-decoration: none;
		}
		</style>
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"></script>
		
			</head>
			<body leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
				<table border="0" class="table_text" width="100%" id="mainTable">
					<tr>
						<th>Donor Agency</th>
						<th colspan="2" align="center">2011</th>

						<th colspan="3">All Years</th>

					</tr>
					<tr>
						<th> </th>
						<th>Committed<font color="red"> *</font></th>
						<th>Disbursed<font color="red"> *</font></th>

						<th>Committed</th>
						<th>Disbursed</th>
						<th width="50" nowrap="nowrap">Disb. Rate<font color="red"> **</font></th>

					</tr>
					<!--
					<xsl:for-each select="report/row">
						<xsl:if test="position()>7">
							<tr>
								<xsl:for-each select="column">
								
									<xsl:if test="position()=1 or position()=2 or position()=3 or position()=10 or position()=11">
											<xsl:if test="position()=1">
												<td nowrap="true">
												<xsl:text disable-output-escaping="yes">&amp;</xsl:text>nbsp;
												<a href="/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=2447" target="_new">
													<xsl:value-of select="text()"/>
												</a>
												</td>
											</xsl:if>
											<xsl:if test="position()>1">
												<td nowrap="true" align="right">
												<xsl:value-of select="text()"/>
												</td>
											</xsl:if>
										
									</xsl:if>
								</xsl:for-each>

									<td nowrap="true" align="right">
										<xsl:if test="number(column[18]/text())>0">
											<xsl:if test="number(column[19]/text())>0">
												<xsl:variable name="actCom" select="number(column[18]/text())"/>
												<xsl:variable name="actDis" select="number(column[19]/text())"/>
												<xsl:variable name="percentage" select="$actDis div $actCom * 100"/>
		
												<xsl:value-of select="round($percentage * 100) div 100"/> %
												
											</xsl:if>
										</xsl:if>
									</td>

							</tr>
						</xsl:if>
					</xsl:for-each>
-->
				</table>
				
				<script type="text/javascript" language="javascript">
					function toNumber (str) {
						var numberStr = str;
						var retVal;
						if (numberStr != null) {
						numberStr = numberStr.replace(",","");
						var comaIdx = numberStr.indexOf(",");
						while (comaIdx >-1) {
							numberStr = numberStr.replace(",","");
							comaIdx = numberStr.indexOf(",");
						}
						retVal = Number(numberStr);
						} else {
							retVal=0;
						}
						return retVal;
					}
					

					function sortByTotalComComparator (a, b) {
						return toNumber(b[3]) - toNumber(a[3]); 
					}
					
					
				
					var rows = new Array();
					<xsl:for-each select="report/row">
						var cols = new Array();
						<xsl:if test="position()>7">
							<xsl:for-each select="column">
								<xsl:if test="position()=1 or position()=2 or position()=3 or position()=10 or position()=11">
									<xsl:if test="position()=1">
										cols.push("<xsl:value-of select="text()"/>");
									</xsl:if>
									<xsl:if test="position()>1">
										cols.push("<xsl:value-of select="text()"/>");
									</xsl:if>
								</xsl:if>
							</xsl:for-each>
							rows.push(cols);
						</xsl:if>
					</xsl:for-each>	
					
					rows.sort(sortByTotalComComparator);
						
					for (rowIdx = 0; rowIdx <xsl:text disable-output-escaping="yes">&lt;</xsl:text> rows.length; rowIdx++)	{
						var cols = rows[rowIdx];
						var rowDomObject = $("<xsl:text disable-output-escaping="yes">&lt;</xsl:text>TR<xsl:text disable-output-escaping="yes">&gt;</xsl:text>");
						for (colIdx = 0; colIdx <xsl:text disable-output-escaping="yes">&lt;</xsl:text> cols.length; colIdx++)	{
							var colDomObject = null;
							if (colIdx > 0) {
								colDomObject = $("<xsl:text disable-output-escaping="yes">&lt;</xsl:text>TD align='right'<xsl:text disable-output-escaping="yes">&gt;</xsl:text>");
								colDomObject.html(cols[colIdx]);
							} else {
								colDomObject = $("<xsl:text disable-output-escaping="yes">&lt;</xsl:text>TD align='left' style='padding-left:10px'<xsl:text disable-output-escaping="yes">&gt;</xsl:text>");
								var reportLink = $("<xsl:text disable-output-escaping="yes">&lt;</xsl:text>A<xsl:text disable-output-escaping="yes">&gt;</xsl:text>"); 
								reportLink.attr("href", "/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=2447");
								reportLink.attr("target", "_new");
								reportLink.html(cols[colIdx]);
								colDomObject.append(reportLink);
								//colDomObject.html(cols[colIdx]);
							}
							rowDomObject.append(colDomObject);
						}
						
						var percentColDomObject = $("<xsl:text disable-output-escaping="yes">&lt;</xsl:text>TD align='right'<xsl:text disable-output-escaping="yes">&gt;</xsl:text>");
						var totalCom = toNumber(cols[3]);
						var totalDisb = toNumber(cols[4])
						if (totalCom<xsl:text disable-output-escaping="yes">&gt;</xsl:text>0 <xsl:text disable-output-escaping="yes">&amp;</xsl:text><xsl:text disable-output-escaping="yes">&amp;</xsl:text> totalDisb<xsl:text disable-output-escaping="yes">&gt;</xsl:text>0) {
							percentColDomObject.html(Math.round((totalDisb/totalCom*100)*100)/100 + " %");
						} else {
							percentColDomObject.html("0 %");
						}
						rowDomObject.append(percentColDomObject);
						//console.log(rowDomObject);
						$("#mainTable").append(rowDomObject);
					}	
				</script>
				
				
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
