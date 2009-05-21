<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil,org.digijava.module.aim.helper.Constants" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<digi:context name="digiContext" property="context" />

<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>

<logic:iterate name="publicReports" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports" indexId="position"> 
	<logic:equal name="report" property="publicReport" value="true">
		<logic:equal name="report" property="drilldownTab" value="true">
			<logic:equal name="position" value="0">
				<bean:define id="firstReportName" name="report" property="name" toScope="Page"/>
			</logic:equal>
		</logic:equal>
	</logic:equal>
</logic:iterate>

<logic:present name="firstReportName">
	<logic:notPresent name="currentMember">
	<%
	String onOff=FeaturesUtil.getGlobalSettingValue(Constants.GLOBAL_BUDGET_FILTER);
	if("On Budget".equals(onOff)) {
	%>
	<digi:trn key="amp:showOnBudget">
	Showing all On Budget activities...
	</digi:trn>
	<% 
	} 
	else if("Off Budget".equals(onOff)) {
	%>
	<digi:trn key="amp:showOffBudget">
	Showing all Off Budget activities...
	</digi:trn>
	<% 
	} else { 
	%>	
	<digi:trn key="amp:showallBudget">
		Showing all public activities...
	</digi:trn>
	<% 
	} 
	%>
	</logic:notPresent>
</logic:present>
<div id="content"  class="yui-skin-sam" style="padding-left:10px;width:98%;min-width:680px;"> 
<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;font-size:10px;">

<ul id="PublicTabs" class="yui-nav">
<logic:iterate name="publicReports" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports" indexId="position"> 
			<logic:equal name="report" property="publicReport" value="true">
				<logic:equal name="report" property="drilldownTab" value="true">
					<li><a id='Tab-<bean:write name="report" property="name"/>' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea"><div><bean:write name="report" property="name"/></div></a></li>
				</logic:equal>
			</logic:equal>
</logic:iterate>
</ul>
</div>

<logic:notPresent name="firstReportName">
<digi:trn key="aim:noPublicTabs">
No Public Tabs
</digi:trn>
</logic:notPresent>

<div id="ajaxcontentarea" class="contentstyle" style="border:1px solid black;min-height:410px;_height:410px;padding-left:5px;padding-top:5px;">
</div>
</div>	
<logic:present name="firstReportName">
	<script type="text/javascript">
	//Start Ajax tabs script for UL with id="maintab" Separate multiple ids each with a comma.
	
	startajaxtabs("PublicTabs");
	reloadTab('PublicTabs','Tab-<bean:write name="firstReportName"/>');
	</script>
</logic:present>


