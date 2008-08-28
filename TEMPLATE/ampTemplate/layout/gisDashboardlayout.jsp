<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%
String countryName = "";
String ISO = null;
java.util.Iterator itr1 = org.digijava.module.aim.util.FeaturesUtil.getDefaultCountryISO().iterator();
while (itr1.hasNext()) {
	org.digijava.module.aim.dbentity.AmpGlobalSettings ampG = (org.digijava.module.aim.dbentity.AmpGlobalSettings) itr1.next();
	ISO = ampG.getGlobalSettingsValue();
}

if(ISO != null && !ISO.equals("")){
	org.digijava.kernel.dbentity.Country cntry = org.digijava.module.aim.util.DbUtil.getDgCountry(ISO);
	countryName = " " + cntry.getCountryName();
}
else {
	countryName = "";
}
%>

<HTML>
	<digi:base />
	<digi:context name="digiContext" property="context"/>
	<HEAD>
		<TITLE><digi:trn key="gis:resultsmatrix">Results Matrix: </digi:trn><%=countryName%></TITLE>
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">		
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
     	<META HTTP-EQUIV="EXPIRES" CONTENT="0">		
		<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	    <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
		<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
        <style>
		.tableElement TD {
			border:1px solid white;
		}
		.tableEven {
			background-color:#dbe5f1;
			font-size:8pt;
			padding:2px;
		}
		
		.tableOdd {
			background-color:#FFFFFF;
			font-size:8pt;!important
			padding:2px;
		}
		.tableHeader {
			background-color:#222e5d;
			color:white;
			padding:2px;
		}		 
		.Hovered {
			background-color:#a5bcf2;
		}
		SELECT {
			Font-size:10px;
			font-family:Verdana,Arial,Helvetica,sans-serif;
		}
		
		</style>
	</HEAD>
	
    <BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
		<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=0 valign="top" align="left">
			<TBODY>
			<TR>
				<TD width="100%" vAlign="top" align="left">
<script language="javascript">
  function exportPDF() {
	var selectedDonor = document.getElementsByName("selectedDonor")[0].value;
	var selectedYear = document.getElementsByName("selectedYear")[0].value;
	var showLabels = document.getElementsByName("showLabels")[0].checked;
	var showLegends = document.getElementsByName("showLegends")[0].checked;
	
  
	openURLinWindow("/gis/pdfExport.do?selectedDonor=" + selectedDonor + "&selectedYear=" + selectedYear + "&showLabels=" + showLabels + "&showLegends=" + showLegends , 780, 500);
  }

function checkTables(){
	var tables = document.getElementsByTagName("table");
	var matchingTables = new Array();
	var currentTable;
	for(var idx=0;idx < tables.length; idx++){
		if(tables[idx].id.search("tableWidget") > -1){
			applyStyle(tables[idx]);
		}
	}
}    
function applyStyle(table){
	table.className += " tableElement";
	setStripsTable(table.id, "tableEven", "tableOdd");
	setHoveredTable(table.id, true);
/*	table.parentNode.onpropertychange = function() {
		alert("hola");
	}*/
}
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	if(tableElement)
	{
		tableElement.setAttribute("border","0");
		tableElement.setAttribute("cellPadding","2");
		tableElement.setAttribute("cellSpacing","2");
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}
}
function setHoveredTable(tableId, hasHeaders) {
	var tableElement = document.getElementById(tableId);
	if(tableElement){
	    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		var i = 0;
		if(hasHeaders){
			rows[0].className += " tableHeader";
			i = 1;
			
		}
	
		for(i, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}

</script>
<jsp:include page="/TEMPLATE/ampTemplate/layout/gisReportToolbar.jsp" />


					<TABLE bgColor=#ffffff cellPadding=0 cellSpacing=0 width="99%" vAlign="top" align="left" border=0>
						<TR>
							<TD  width="10">&nbsp;</td>
							<TD align=center vAlign=top>
								<TABLE width="100%" cellPadding="5" cellSpacing="0" vAlign="top" align="left" border="0">
									<TR>
										<TD vAlign="top" align="left" width="60%" >
											<digi:insert attribute="body" />
										</TD>
										<td valign="top">
											<digi:insert attribute="pieChart"/>											
										</td>										
									</TR>
									<tr>
										<td valign="top">
                                        <div id="content" class="yui-skin-sam" style="width:100%;">
                                          <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                            <ul class="yui-nav">
                                              <li class="selected">
                                                <a href="#">
                                                <div>
                                                  <digi:trn key="gis:millenniumdevelopmentgoals">Millennium Development Goals</digi:trn>
                                                </div>
                                                </a>
                                              </li>
                                            </ul>
                                            <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">                                        
											<table cellpadding="5">
												<tr>
													<td>
														<digi:insert attribute="widget1">
															<digi:put name="widget-teaser-param">chart_place1</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget2">
															<digi:put name="widget-teaser-param">chart_place2</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget3">
															<digi:put name="widget-teaser-param">chart_place3</digi:put>
														</digi:insert>
													
													</td>
												</tr>
												<tr>
													<td>
														<digi:insert attribute="widget4">
															<digi:put name="widget-teaser-param">chart_place4</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget5">
															<digi:put name="widget-teaser-param">chart_place5</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget6">
															<digi:put name="widget-teaser-param">chart_place6</digi:put>
														</digi:insert>
													</td>
												</tr>
											</table>
                                            </div>
                                          </div>
                                        </div> 
										</td>
										<td valign="top">
                                        <div id="content" class="yui-skin-sam" style="width:100%;">
                                          <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                            <ul class="yui-nav">
                                              <li class="selected">
                                                <a href="#">
                                                <div>
                                                  <digi:trn key="gis:resourcesatglance">Resources at a glance</digi:trn>
                                                </div>
                                                </a>
                                              </li>
                                            </ul>
                                            <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">                                        
												<table cellpadding="5">
												<tr valign="top">
													<td>
														<digi:insert attribute="widget7">
															<digi:put name="widget-teaser-param">atGlanceTable_Place1</digi:put>
														</digi:insert>
													</td>
													<td rowspan="2">
														<digi:insert attribute="widget8">
															<digi:put name="widget-teaser-param">atGlanceTable_Place3</digi:put>
														</digi:insert>
													</td>
												</tr>
												<tr>
													<td>
														<digi:insert attribute="widget9">
															<digi:put name="widget-teaser-param">atGalnceTable_Place2</digi:put>
														</digi:insert>
													</td>
												</tr>
											</table>

                                            </div>
                                          </div>
                                        </div> 
										</td>
									</tr>
									<tr>
										<td colspan="2">
                                        <div id="content" class="yui-skin-sam" style="width:900px;">
                                          <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                            <ul class="yui-nav">
                                              <li class="selected">
                                                <a href="#">
                                                <div>
                                                  <digi:trn key="gis:aideffectivenessindicators">Aid Effectiveness Process Indicators</digi:trn>
                                                </div>
                                                </a>
                                              </li>
                                            </ul>
                                            <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                        
                                            <table>
                                                <tr>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget10">
                                                            <digi:put name="widget-teaser-param">table_place1</digi:put>
                                                        </digi:insert>
                                                    </td>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget11">
                                                            <digi:put name="widget-teaser-param">table_place2</digi:put>
                                                        </digi:insert>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td valign="top">
                                                    <br><br>
                                                        <digi:insert attribute="widget12">
                                                            <digi:put name="widget-teaser-param">table_place3</digi:put>
                                                        </digi:insert>
                                                    </td>
                                                    <td>
                                                    </td>
                                                </tr>
                                            </table>
                                            
                                            </div>
                                            </div>
                                            </div>
                                        </td>
                                    </tr>
									<tr>
										<td colspan="2" valign="top">
                                        <div id="content" class="yui-skin-sam" style="width:600px;">
                                          <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                            <ul class="yui-nav">
                                              <li class="selected">
                                                <a href="#">
                                                <div>
                                                  <digi:trn key="gis:intermediateoutputindicators">Intermediate Output Indicators</digi:trn>
                                                </div>
                                                </a>
                                              </li>
                                            </ul>
                                            <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                            <table>
                                                <tr>
                                                    <td>
                                                    <digi:insert attribute="widget13">
                                                        <digi:put name="widget-teaser-param">table_place4</digi:put>
                                                    </digi:insert>
                                                    </td>
                                                </tr>
                                            </table>
                                            </div>
                                            </div>
                                            </div>
    									</td>
									</tr>
									<tr>
										<td colspan="2">
                                        <div id="content" class="yui-skin-sam" style="width:900px;">
                                          <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                            <ul class="yui-nav">
                                              <li class="selected">
                                                <a href="#">
                                                <div>
                                                  <digi:trn key="gis:resources">Resources</digi:trn>
                                                </div>
                                                </a>
                                              </li>
                                            </ul>
                                            <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                            <table>
                                                <tr>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget14">
                                                            <digi:put name="widget-teaser-param">table_place5</digi:put>
                                                        </digi:insert>
                                                    </td>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget15">
                                                            <digi:put name="widget-teaser-param">table_place6</digi:put>
                                                        </digi:insert>
                                                    </td>
                                                </tr>
                                            </table>
                                            </div>
                                            </div>
                                            </div>
    									</td>
									</tr>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</BODY>
</HTML>

