<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<link rel="stylesheet" href="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.css"/>" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/relatedLinks.js"/>"></script>
<digi:context name="digiContext" property="context" />

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<!-- script for tree-like view (drilldown reports) -->

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>

<link rel="stylesheet"
	href="<digi:file src="module/aim/css/newamp.css"/>" />
<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<!-- script for tree-like view (drilldown reports) -->
<script language="JavaScript" type="text/javascript"src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.js"/>"></script>

<div id="myTabs">
<div class="DHTMLSuite_aTab">
 				Click on one of the buttons below, to open a drilldown tab:<br/>
			<logic:iterate name="myReports" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports"> 
			<logic:equal name="report" property="drilldownTab" value="true">
                <input type="button" value="<bean:write name="report" property="name"/>" onclick="tabViewObj.createNewTab('myTabs','<bean:write name="report" property="name"/>','','/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true',true);return false"/>&nbsp;
			</logic:equal>
			</logic:iterate>
</div>
</div>


<script type="text/javascript">
var tabViewObj = new DHTMLSuite.tabView();
tabViewObj.setParentId('myTabs');
tabViewObj.setIndexActiveTab(0);
tabViewObj.setTabTitles(Array('My Tabs'));
tabViewObj.init();
</script>
