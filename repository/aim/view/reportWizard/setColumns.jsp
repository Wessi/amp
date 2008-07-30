<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

	<script type="text/javascript">
		var idOfFolderTrees = ['dhtmlgoodies_tree'];
	</script>
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/folder-tree-static.css" />" />
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/context-menu.css" />" />


<digi:instance property="aimReportWizardForm" />
<bean:define name="aimReportWizardForm" id="myForm" type="org.digijava.module.aim.form.reportwizard.ReportWizardForm"/>
<digi:form action="/advancedReportManager.do" method="post">
																
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>

                                            
																			<p id="columnTree">
																				<c:if test="${!empty aimReportWizardForm.ampTreeColumns}">
																							<bean:define name="aimReportWizardForm" property="ampTreeColumns" id="ampTreeColumns" type="java.util.Map"  toScope="page"/>
																							<logic:iterate name="ampTreeColumns" id="ampTreeColumn" type="java.util.Map.Entry" >
																								<bean:define id="columnCollection" name="ampTreeColumn" property="value" type="java.util.ArrayList" scope="page"/>
																								<logic:iterate name="columnCollection" id="ampColumnFromTree" type="org.digijava.module.aim.dbentity.AmpColumns">
																									<script type="text/javascript" >
																										insertColInfo('${ampColumnFromTree.columnId}', '${ampColumnFromTree.columnName}');
																									</script>
																								</logic:iterate>
																							</logic:iterate>
																					<!--
																						Because the donor report and the contribution report are now different (the donor has also the indicator columnns)
																						we have to create different c:if for each report
																						 -->
																						 <font size="3">
																							<ul id="dhtmlgoodies_tree" class="DHTMLSuite_tree">
																							<bean:define name="aimReportWizardForm" property="ampTreeColumns" id="ampTreeColumns" type="java.util.Map"  toScope="page"/>
																							<li noDrag="true">
																							<input type=checkbox id="root"
																											name="root"
																											value="root"
																											onclick="checkUncheckAll3();"

																								/>
																							<a id="1" style="font-size: 12px;color:#0e69b3;text-decoration:none"><digi:trn key="aim:report:AMP" >AMP</digi:trn></a>
																								<ul class="nodragul">
																							<logic:iterate name="ampTreeColumns" id="ampTreeColumn" type="java.util.Map.Entry" >
																								<bean:define id="themeColumn" name="ampTreeColumn" property="key" type="java.lang.String" scope="page"/>
																								<bean:define id="columnCollection" name="ampTreeColumn" property="value" type="java.util.ArrayList" scope="page"/>
																								<div id="limodule:<bean:write name="themeColumn"/>" noDrag="true">
																								<li id="limodule:<bean:write name="themeColumn"/>" noDrag="true">
																								<a id="module:<bean:write name="themeColumn"/>" style="font-size: 12px;color:#0e69b3;text-decoration:none">
																									<digi:trn key="aim:report:${themeColumn}"><bean:write name="themeColumn"/></digi:trn>
																								</a>
																								<ul>
																								<logic:iterate name="columnCollection" id="ampColumnFromTree" type="org.digijava.module.aim.dbentity.AmpColumns">
																									<li class="dhtmlgoodies_sheet.gif" noDrag="true" style="white-space:nowrap;">
																										
																										<input type=checkbox id="fieldVis:<bean:write name="ampColumnFromTree" property="columnId"/>"
																											name="selectedColumns"
																											value="<bean:write name="ampColumnFromTree" property="columnId"/>"
																										/>
																										<a id="field:<bean:write name="ampColumnFromTree" property="columnId"/>" style="font-size: 12px;color:#0e69b3;text-decoration:none">
																										</a>
																										<span style="font-size: 12px;color:#0e69b3;text-decoration:none">
																											<digi:trn key="aim:report:${ampColumnFromTree.columnName}"><bean:write name="ampColumnFromTree" property="columnName"/></digi:trn>
																											<logic:equal name="ampColumnFromTree" property="columnName" value="Cumulative Commitment">
																												<img src= "../ampTemplate/images/help.gif" border="0" title="<digi:trn key="aim:report:CumulativeCommitmentToolip">Sum of all ACTUAL COMMITMENTS independent of filters</digi:trn>">
																											</logic:equal>
																											<logic:equal name="ampColumnFromTree" property="columnName" value="Cumulative Disbursement">
																												<img src= "../ampTemplate/images/help.gif" border="0" title="<digi:trn key="aim:report:CumulativeDisbursementToolip">Sum of all ACTUAL DISBURSEMENTS independent of filters</digi:trn>">
																											</logic:equal>
																											<logic:equal name="ampColumnFromTree" property="columnName" value="Undisbursed Cumulative Balance">
																												<img src= "../ampTemplate/images/help.gif" border="0" title="<digi:trn key="aim:report:UndisbursedCumulativeBalanceToolip">Cumulative Commitment - Cumulative Disbursement (independent of filters)</digi:trn>">
																											</logic:equal>
																										</span>
																									
																									</li>
																								</logic:iterate>
																								</ul>

																								</li></div>
																							</logic:iterate>
																							</ul>
																							</li></ul>
																						</font>



																				</c:if>
																			</p>



</digi:form>



