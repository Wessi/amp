<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="JavaScript">
	function addActivity() {
		window.location.href="/aim/addActivity.do~pageId=1~reset=true~action=create";	
	}
	
	function teamWorkspaceSetup(a) {
		window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";	
	}
	
	var tabName	= "Tab-By Project";
	
	<logic:notEmpty name="defaultTeamReport" scope="session">
			tabName	= 'Tab-<bean:write name="defaultTeamReport" scope="session" property="name"/>';
	</logic:notEmpty>
	
</script>

	
<digi:context name="digiContext" property="context" />


<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>

<ul id="MyTabs" class="shadeTabs">

	<logic:iterate name="myReports" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports"> 
				<logic:equal name="report" property="drilldownTab" value="true">
				
				<li><a id='Tab-<bean:write name="report" property="name"/>' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea"><bean:write name="report" property="name"/></a></li>
				</logic:equal>
	</logic:iterate>

<!-- 
<logic:empty name="myReports" scope="session">
	<logic:notEmpty name="defaultTeamReport" scope="session">
		<li><a id='Tab-<bean:write name="defaultTeamReport" scope="session" property="name"/>' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="defaultTeamReport" scope="session" property="ampReportId"/>~widget=true" rel="ajaxcontentarea"><bean:write name="defaultTeamReport" scope="session" property="name"/></a></li>
	</logic:notEmpty>
</logic:empty>
 -->
</ul>
<div id="ajaxcontentarea" class="contentstyle">
<digi:trn key="aim:clickOnATab">
<p/>
Click on one of the tabs to display activities. You can create more tabs by using the Advanced Reports Manager.
</digi:trn>
</div>

<script type="text/javascript">
//Start Ajax tabs script for UL with id="maintab" Separate multiple ids each with a comma.
startajaxtabs("MyTabs");
reloadTab("MyTabs",tabName);
</script>

<div id="debug"></div>
