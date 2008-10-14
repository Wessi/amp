<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ page import="org.dgfoundation.amp.ar.AmpARFilter"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
  <!-- Dependencies --> 
        <script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/container_core-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/connection-min.js"/>"></script>
        
        <!-- Source File -->
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/container-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/tabview-min.js"/>"></script> 

        <!-- Core + Skin CSS -->
        <digi:ref href="css/menu.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/container.css" type="text/css" rel="stylesheet" />

        <!-- Stylesheet of AMP -->
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />



</style>
<style>
.link{
	text-decoration: none;
	font-size: 8pt; font-family: Tahoma;
}

.reportname{
	background:#bbe0e3; 
	width: 100px;
	margin-top: 20px;
	margin-left: 5px;
}
</style>
<c:set var="showCurrSettings">
	<digi:trn key="rep:showCurrSettings">Show current settings</digi:trn> 
</c:set>
<c:set var="hideCurrSettings">
	<digi:trn key="rep:hideCurrSettings">Hide current settings</digi:trn> 
</c:set>
<script language="JavaScript">
function toggleSettings(){
	var currentDisplaySettings = document.getElementById('currentDisplaySettings');
	var displaySettingsButton = document.getElementById('displaySettingsButton');
	if(currentDisplaySettings.style.display == "block"){
		currentDisplaySettings.style.display = "none";
		displaySettingsButton.innerHTML = "${showCurrSettings} &gt;&gt;";
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = "${hideCurrSettings} &lt;&lt;";
	}
}
	
</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/util.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>


<div id="mySorter" style="display: none">
	<jsp:include page="/repository/aim/view/ar/levelSorterPicker.jsp" />
        <!--
		<a href='#' onclick='hideSorter();return false'>
			<b>
				<digi:trn key="rep:pop:Close">Close</digi:trn>
			</b>
		</a>
		 -->
	</div>
<%
Integer counter = (Integer)session.getAttribute("progressValue");
counter++;
session.setAttribute("progressValue", counter);
%>

<div id="myFilterWrapper" style="display: none;" >
	<div id="myFilter" style="display: none;" >
			<jsp:include page="/aim/reportsFilterPicker.do" />
	</div>
	<div id="myRange" style="display: none">
	          <jsp:include page="/repository/aim/view/ar/RangePicker.jsp" />
	</div>
	<div id="customFormat" style="display: none">
	          <jsp:include page="/repository/aim/view/ar/customFormatPicker.jsp" />
	</div>
</div>

<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>

<jsp:include page="/repository/aim/view/saveReports/dynamicSaveReportsAndFilters.jsp" />
<%
counter++;
session.setAttribute("progressValue", counter);
%>

<logic:notEqual name="widget" scope="request" value="true">
<logic:notEqual name="viewFormat" scope="request" value="print">
<jsp:include page="/repository/aim/view/ar/toolBar.jsp" />

<c:set var="rowIdx" value="<%=new Integer(0)%>" scope="request"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page" />


<logic:notEqual name="widget" scope="request" value="true">
	<div class="reportname" style="background: #222E5D">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
  			<tr>
    			<td align="left">
    				<img src="images/tableftcorner.gif"/>
    			</td>
    			<td align="center" nowrap="nowrap" style="background:#222E5D; font-family: Arial;color:white;font-weight: bold;">
    				<bean:write scope="session" name="reportMeta" property="name" />
    			</td>
    			<td align="right">
    				<img src="images/tabrightcorner.gif" />
    			</td>
  			</tr>
		</table>
	</div>
</logic:notEqual>

<table width="100%" cellpadding="3" cellspacing="1" rules="rows" frame="box" style="margin-left: 5px;border-color:#999999;">
	<logic:notEmpty property="reportDescription" name="reportMeta">
	<tr style="border-top:#222E5D 3px solid;">
		<td style="padding-left: 5px;padding-left: 5px;">
			<digi:trn key="rep:pop:Description">Description:</digi:trn><br>
				<span style="font-weight: bold;font-size: 13px;margin-left: 5px;margin-top: 3px; font-family: Arial">
					<bean:write scope="session" name="reportMeta" property="reportDescription" />
				</span>
		</td>
	</tr>
	</logic:notEmpty>
	<logic:empty property="reportDescription" name="reportMeta">
		<tr style="border-top:#222E5D 3px solid;">
	</logic:empty>
	<logic:notEmpty property="reportDescription" name="reportMeta">
	<tr>
	</logic:notEmpty> 
		<td align="left" height="20px" style="padding-left: 5px;padding-left: 5px;">
			<span  style="color: red;font-family: Arial;font-size: 10px;">
				<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
					<c:set var="AllAmount">
						<%=org.digijava.module.aim.dbentity.AmpReports.getNote(session)%>
					</c:set>
					<digi:trn key="rep:pop:AllAmount">
						<%=org.digijava.module.aim.dbentity.AmpReports.getNote(session)%>
					</digi:trn>
				</gs:test>			
				<logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY%>">
					<bean:define id="selCurrency" name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY %>" />
					<digi:trn key="<%="aim:currency:" + ((String)selCurrency).toLowerCase().replaceAll(" ", "") %>"><%=selCurrency %></digi:trn>
				</logic:present>
			</span>
		</td>
	</tr>
</logic:notEqual>
</logic:notEqual>
	<logic:equal name="viewFormat" scope="request" value="print">
	<script language="JavaScript">
		function load()
		{
			window.print();
		}
		function unload() {}
	</script>
	</logic:equal>
	<logic:notEqual name="widget" scope="request" value="true">
	<logic:notEqual name="viewFormat" scope="request" value="print">
	<tr>
		<td>
		<div style="margin-left:5px;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
	        <span style="cursor:pointer;font-style: italic;float:right;" onClick="toggleSettings();" id="displaySettingsButton">${showCurrSettings} &lt;&lt;</span>
            <span style="cursor:pointer;float:left;">
            <logic:notEmpty name="reportMeta" property="hierarchies">
                <a class="settingsLink" onClick="showSorter();">
                <digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn>
                </a> | 
            </logic:notEmpty> 
                <a class="settingsLink" onClick="showFilter(); " >
                <digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
                </a>
                <feature:display name="Save Filters from Desktop" module="Report Generator">
	          	 	|
	          	 	<a class="settingsLink" onClick="initSaveReportEngine();saveReportEngine.showPanel(); " >
	                	${saveFilters}
	                </a>
                </feature:display>
           	  <logic:notEqual name="viewFormat" value="foldable">
           	  	|
				<a  id="frezzlink" class="settingsLinkDisable">
               		<script language="javascript">
						document.write((scrolling)?msg2:msg1);
					</script>
                </a>
                      	
                      	
           	|<a class="settingsLink" onClick="showRange(); " >
               		 <digi:trn key="rep:pop:ChangeRange">Change Range</digi:trn>
                 </a>
              </logic:notEqual>
                
                |<a  class="settingsLink" onClick="showFormat(); " >
                <digi:trn key="rep:pop:ChangeFormat">Change Format</digi:trn>
                </a>
           
            </span>
             &nbsp;<br>
             <div style="display:block;background-color:#FFFFCC;padding:2px 2px 2px 2px;" id="currentDisplaySettings" >
             <table cellpadding="0" cellspacing="0" border="0" width="80%">
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
			<strong>
			<digi:trn key="rep:pop:SelectedFilters">Selected Filters:</digi:trn></strong>
                <logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" scope="session">
                <bean:define id="listable" name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" toScope="request"/>
                <bean:define id="listableStyle" value="settingsList" toScope="request"/>
                <bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
                    <jsp:include page="${listable.jspFile}" flush="true"/>
                </logic:present>
             </td>
             </tr>
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
				<strong><digi:trn key="rep:pop:SelectedRange">Selected Range:</digi:trn></strong>
				<%
                	AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");
                %>
                <c:set var="all" scope="page">
                	<digi:trn key="rep:pop:SelectedRangeAll">All:</digi:trn>
                </c:set>
                
                <digi:trn key="rep:pop:SelectedRangeStartYear">Start Year:</digi:trn> <%=(arf.getRenderStartYear() > 0)?arf.getRenderStartYear():pageContext.getAttribute("all")%> |
                <digi:trn key="rep:pop:SelectedRangeEndYear">End Year:</digi:trn> <%=(arf.getRenderEndYear() > 0)?arf.getRenderEndYear():pageContext.getAttribute("all")%> |
             </td>
             </tr>
             <tr>
           </table>
           </div>
    	</div>
	</td>
	</tr>
	</logic:notEqual>
	</logic:notEqual>
 
	<logic:equal name="widget" scope="request" value="true">
	<table width="100%"> 
		<tr>
		<td style="padding-left:-2px;">
		<div style="width:99%;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
	        <span style="cursor:pointer;font-style: italic;float:right;" onClick="toggleSettings();" id="displaySettingsButton">${showCurrSettings} &gt;&gt;</span>
            <span style="cursor:pointer;float:left;">
            <logic:notEmpty name="reportMeta" property="hierarchies">
                <a class="settingsLink" onClick="showSorter();">
                <digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn>
                </a> | 
            </logic:notEmpty> 
                <a class="settingsLink" onClick="showFilter(); " >
                <digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
                </a>
                <feature:display name="Save Filters from Desktop" module="Report Generator">
	                |
	          	 	<a class="settingsLink" onClick="initSaveReportEngine();saveReportEngine.showPanel(); " >
	                	${saveFilters}
	                </a>
           		</feature:display>
           	  <logic:notEqual name="viewFormat" value="foldable">
           	  |
           	  	<a  id="frezzlink" class="settingsLinkDisable">
               		<script language="javascript">
						document.write((scrolling)?msg2:msg1);
					</script>
                </a>
           	  
            	|<a class="settingsLink" onClick="showRange(); " >
               		 <digi:trn key="rep:pop:ChangeRange">Change Range</digi:trn>
                 </a>
              </logic:notEqual>
                
                |<a  class="settingsLink" onClick="showFormat(); " >
                <digi:trn key="rep:pop:ChangeFormat">Change Format</digi:trn>
                </a>
           
            </span>
             &nbsp;<br>
             <div style="display:none;background-color:#FFFFCC;padding:2px 2px 2px 2px;" id="currentDisplaySettings" >
             <table cellpadding="0" cellspacing="0" border="0" width="80%">
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
			<strong>
			<digi:trn key="rep:pop:SelectedFilters">Selected Filters:</digi:trn></strong>
                <logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" scope="session">
                <bean:define id="listable" name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" toScope="request"/>
                <bean:define id="listableStyle" value="settingsList" toScope="request"/>
                <bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
                    <jsp:include page="${listable.jspFile}" flush="true"/>
                </logic:present>
             </td>
             </tr>
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
				<strong><digi:trn key="rep:pop:SelectedRange">Selected Range:</digi:trn></strong>
				<%
                	AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");
                %>
                    <c:set var="all" scope="page">
                	<digi:trn key="rep:pop:SelectedRangeAll">All:</digi:trn>
                </c:set>
                
            	<digi:trn key="rep:pop:SelectedRangeStartYear">Start Year:</digi:trn> <%=(arf.getRenderStartYear() > 0)?arf.getRenderStartYear():pageContext.getAttribute("all")%> |
                <digi:trn key="rep:pop:SelectedRangeEndYear">End Year:</digi:trn> <%=(arf.getRenderEndYear() > 0)?arf.getRenderEndYear():pageContext.getAttribute("all")%> |
              </td>
             </tr>
             <tr>
           </table>
           </div>
    	</div>
	</td>
	</tr>
	<tr>
		<td>
		<table width="100%">
			<tr>
			
			<td align="right">
			<span style="color: red;font-family: Arial;padding-right: 5px">
				<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
				<c:set var="AllAmount">
					<%=org.digijava.module.aim.dbentity.AmpReports.getNote(session)%>
				</c:set>
				<digi:trn key="rep:pop:AllAmount">
					<%=org.digijava.module.aim.dbentity.AmpReports.getNote(session)%>
				</digi:trn>
				</gs:test>
				<logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY%>">
					<bean:define id="selCurrency" name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY %>" />
					<digi:trn key="<%="aim:currency:" + ((String)selCurrency).toLowerCase().replaceAll(" ", "") %>"><%=selCurrency %></digi:trn>
				</logic:present>
			</span>
			</td>
			</tr>
		</table>
		</td>
	</tr>
		<tr>
		<td style="padding-left: 5px;padding-right: 5px" align="left">
		<table width="100%">
			<tr>
			<td>
            <logic:notEqual name="viewFormat" value="print">
                <logic:equal name="viewFormat" value="foldable">
					<c:if test="${report.startRow != 0}">
                    	<!-- Go to FIRST PAGE -->
                    	<a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=0~endRow=<c:out value="${recordsPerPage-1}"/>');">	
                    	&lt;&lt;
                    	</a>
                    	|
                    	<a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow-recordsPerPage}"/>~endRow=<c:out value="${report.startRow-1}"/>');">	
    		            	<digi:trn key="aim:previous">Previous</digi:trn>
                    	</a>
                    	|
                    </c:if>
                </logic:equal>
				<c:set var="lastPage">
                	0
                </c:set>
                <c:forEach var="i" begin="0" end="${report.visibleRows-1}" step="${recordsPerPage}">
                    <logic:equal name="viewFormat" value="html">
                        <a style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=html~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+(recordsPerPage-1)}"/>';">
                    </logic:equal>
                    <logic:equal name="viewFormat" value="foldable">
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage-1}"/>');">	
                    </logic:equal>
                    <c:choose>							
                        <c:when  test="${i eq report.startRow}">
                            <font color="#FF0000"><fmt:formatNumber value="${(i)/recordsPerPage + 1}" maxFractionDigits="0"/></font>
                        </c:when>
                        <c:otherwise>
                            <fmt:formatNumber value="${(i/recordsPerPage) + 1}" maxFractionDigits="0"/>
                        </c:otherwise>								
                    </c:choose>
                    </a>
                    |
                	<c:set var="lastPage">
                    	${lastPage+1}
                    </c:set>
				</c:forEach>

                <logic:equal name="viewFormat" value="foldable">
					<c:if  test="${(report.startRow+recordsPerPage+1) < report.visibleRows}">
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow+recordsPerPage}"/>~endRow=<c:out value="${report.startRow+(recordsPerPage*2)-1}"/>');">	
                            <digi:trn key="aim:next">Next</digi:trn>
                        </a>
                        |
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${((lastPage-1)*recordsPerPage)}"/>~endRow=<c:out value="${(lastPage*recordsPerPage)}"/>');">	
                        &gt;&gt;
                        </a>
                    </c:if>
                </logic:equal>
				</logic:notEqual>
            </td>
            <td align="right">
            <jsp:include page="legendPopup.jsp" />
            </td>
            </tr>
            </table>
			</td>
		</tr>
	</logic:equal>
	
	<logic:notEmpty name="reportMeta" property="hierarchies">
		<logic:notEmpty name="report" property="levelSorters">
			<tr>
				<td align="left">
				<logic:iterate name="report" property="levelSorters" id="sorter" indexId="levelId">
					<span style="font-style: italic;font-size: 9px;font-family: Arial;margin-left: 3px; margin-top: 3px;margin-left: 3px">
					<logic:present name="sorter">
						<digi:trn key="rep:pop:Level">Level</digi:trn> 
							<bean:write name="levelId"/> 
							<digi:trn key="rep:pop:sortedBy">sorted by</digi:trn> 
							<bean:write name="sorter"/>
						<br>		
					</logic:present>
				</span>
				</logic:iterate>
				</td>
			</tr>
				</logic:notEmpty>
	</logic:notEmpty>
	
	<logic:notEqual name="report" property="totalUniqueRows" value="0">
	<tr>
		<td  style="padding-left: 5px;padding-left: 5px;">
		<table style="width: 100%">
		<tr>
		<td>
			<!-- begin big report table -->
			<c:set var="pageNumber" value="<%=new Integer(0)%>" scope="request"/>
			<c:set var="paginar" value="<%=new Boolean(true)%>" scope="request"/>
			<c:if test="${not empty param.pageNumber }">
				<c:set var="pageNumber" value="<%=Integer.valueOf(request.getParameter("pageNumber"))%>" scope="request"/>
			</c:if>
		<logic:equal name="viewFormat" value="print">
			<table id='reportTable'  cellSpacing="0" width="780" style="overflow:hidden">
				<bean:define id="viewable" name="report" type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
				<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
			</table>
		</logic:equal>
		<logic:notEqual name="viewFormat" value="print">
	<table id='reportTable'  cellSpacing="0" cellPadding="1" width="100%" class="reportsBorderTable">
				
			<bean:define id="viewable" name="report" type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
				<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" flush="true"/>
			
	</tr>
		
			</tbody>
	</table>

		



	</logic:notEqual>
		</td>
		</tr>
		</table>
		<!-- end of big report table -->
		</td>
		</tr>
		<logic:equal name="viewFormat" value="print">
		<tr>
			<td>
				<u><digi:trn key="rep:print:lastupdate">Last Update :</digi:trn></u>
				&nbsp;
				<c:if test="${reportMeta.updatedDate != null}">
					<bean:write scope="session" name="reportMeta" property="updatedDate"/>
				</c:if>
				&nbsp;
				<u><digi:trn key="rep:print:user">User :</digi:trn></u>
				<c:if test="${reportMeta.user != null}">
					<bean:write scope="session" name="reportMeta" property="user"/>
				</c:if>
				<BR>
			</td>
		</tr>
		</logic:equal>
		<tr>
			 <td style="padding-left: 5px;padding-right: 5px">
			 <table width="100%">
			 <tr>
			 <td>
            <logic:notEqual name="viewFormat" value="print">
                <logic:equal name="viewFormat" value="foldable">
					<c:if test="${report.startRow != 0}">
                    	<!-- Go to FIRST PAGE -->
                    	<a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=0~endRow=<c:out value="${recordsPerPage-1}"/>');">	
                    	&lt;&lt;
                    	</a>
                    	|
                    	<a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow-recordsPerPage}"/>~endRow=<c:out value="${report.startRow-1}"/>');">	
    		            	<digi:trn key="aim:previous">Previous</digi:trn>
                    	</a>
                    	|
                    </c:if>
                </logic:equal>
				<c:set var="lastPage">
                	0
                </c:set>
                <c:forEach var="i" begin="0" end="${report.visibleRows-1}" step="${recordsPerPage}">
                    <logic:equal name="viewFormat" value="html">
                        <a style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=html~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+(recordsPerPage-1)}"/>';">
                    </logic:equal>
                    <logic:equal name="viewFormat" value="foldable">
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage-1}"/>');">	
                    </logic:equal>
                    <c:choose>							
                        <c:when  test="${i eq report.startRow}">
                            <font color="#FF0000"><fmt:formatNumber value="${(i)/recordsPerPage + 1}" maxFractionDigits="0"/></font>
                        </c:when>
                        <c:otherwise>
                            <fmt:formatNumber value="${(i/recordsPerPage) + 1}" maxFractionDigits="0"/>
                        </c:otherwise>								
                    </c:choose>
                    </a>
                    |
                	<c:set var="lastPage">
                    	${lastPage+1}
                    </c:set>
				</c:forEach>

                <logic:equal name="viewFormat" value="foldable">
					<c:if  test="${(report.startRow+recordsPerPage+1) < report.visibleRows}">
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow+recordsPerPage}"/>~endRow=<c:out value="${report.startRow+(recordsPerPage*2)-1}"/>');">	
                            <digi:trn key="aim:next">Next</digi:trn>
                        </a>
                        |
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${((lastPage-1)*recordsPerPage)}"/>~endRow=<c:out value="${(lastPage*recordsPerPage)}"/>');">	
                        &gt;&gt;
                        </a>
                    </c:if>
                </logic:equal>
				</logic:notEqual>
            </td>
            <td align="right">
            <jsp:include page="legendPopup.jsp" />
            </td>
            </tr>
            <tr>
            	<td align="left">
            	<%
                	AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");
                %>
				<c:set var="accessTypeLocal"><%=arf.getAccessType().toString() %></c:set>
				<c:if test="${accessTypeLocal == 'Management'}">
                <digi:trn key="aim:noNewActivitiesManagement">Number of activities that do not show up in the list (these include draft and new activities that are pending approvals)</digi:trn>:
                &nbsp;<%= arf.getActivitiesRejectedByFilter().toString() %>
                </c:if>&nbsp;
			</td>
            </tr>
            </table>
			</td>
			</tr>
		</logic:notEqual>
	<logic:equal name="report" property="totalUniqueRows" value="0">
		<tr>
			<td style="font-family: Arial;font-style: italic;font-size: 10x"> 
				<digi:trn key="rep:pop:filteredreport">The specified filtered report does not hold any data. Either
					pick a different filter criteria or use another report.	
				</digi:trn>
			</td>
		</tr>
	</logic:equal>


</table>


<%
	session.setAttribute(" ", null);
	session.setAttribute("progressValue", -1);
%>

