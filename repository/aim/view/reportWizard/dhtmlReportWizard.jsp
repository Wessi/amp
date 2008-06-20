<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>">.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>">.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/element/element-beta.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>">.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/animation-min.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dom-min.js'/>">.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/tab/tabview.js'/>" >.</script>
	<%-- <script type="text/javascript" src=".<digi:file src='module/aim/scripts/logger/logger-min.js'/>">.</script> --%>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/ajaxconnection/connection-min.js'/>" > .</script>
	
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop.js'/>" >.</script>
	
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/myDragAndDropObjects.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportManager.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/fundingGroups.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/saving.js'/>" >.</script>
	
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/border_tabs.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
	
	<digi:instance property="aimReportWizardForm" />
	<bean:define name="aimReportWizardForm" id="myForm" type="org.digijava.module.aim.form.reportwizard.ReportWizardForm" toScope="request"/>
	
	<c:set var="failureMessage">
		<digi:trn key="aim:reportwizard:connectionProblems">Apparently there are some connection problems. Please try again in a few moments.</digi:trn>
	</c:set>
	<c:set var="savingMessage">
		<digi:trn key="aim:reportwizard:savingReport">Saving report</digi:trn>
	</c:set>

	<script type="text/javascript">
		YAHOO.namespace("YAHOO.amp.reportwizard");
		YAHOO.amp.reportwizard.numOfSteps	= 4;
		
		YAHOO.amp.reportwizard.tabLabels	= new Array("reportdetails_tab_label", "columns_tab_label", 
											"hierarchies_tab_label", "measures_tab_label");
		selectedCols						= new Array();
		selectedHiers						= new Array();
		selectedMeas						= new Array();
		
		
		function initializeDragAndDrop() {
			YAHOO.amp.reportwizard.tabView 		= new YAHOO.widget.TabView('wizard_container');
			YAHOO.amp.reportwizard.tabView.addListener("contentReady", continueInitialization);
		}
		function continueInitialization(){
			treeObj = new DHTMLSuite.JSDragDropTree();
			treeObj.setTreeId('dhtmlgoodies_tree');
			treeObj.init();
			treeObj.showHideNode(false,'dhtmlgoodies_tree');
// 			if (YAHOO.widget.Logger) {
// 				var reader = new YAHOO.widget.LogReader( "logDiv", 
// 							{ newestOnTop: true, height: "400px" } );
// 			}
			if ( ${myForm.desktopTab} )
				repManager		= new TabReportManager();
			else
				repManager		= new NormalReportManager();
			
			var saveBtns		= document.getElementsByName("save");	
			for (var i=0; i<saveBtns.length; i++  ) {
				repManager.addStyleToButton(saveBtns[i]);
			}
			for (var i=0; i<YAHOO.amp.reportwizard.numOfSteps; i++) {
				repManager.addStyleToButton("step"+ i +"_prev_button");
				repManager.addStyleToButton("step"+ i +"_next_button");
			}
			
			var numOfRows			= Math.round(YAHOO.util.Dom.getDocumentHeight() / 33.7);
			//alert( YAHOO.util.Dom.getDocumentHeight() );
			//alert( YAHOO.util.Dom.getViewportHeight() / YAHOO.util.Dom.getViewportWidth() );
			document.getElementsByName("reportDescription")[0].rows 	= numOfRows;

			columnsDragAndDropObject	= new ColumnsDragAndDropObject('source_col_div');
			columnsDragAndDropObject.createDragAndDropItems();
			new YAHOO.util.DDTarget('source_measures_ul');
			new YAHOO.util.DDTarget('dest_measures_ul');
			new YAHOO.util.DDTarget('source_hierarchies_ul');
			new YAHOO.util.DDTarget('dest_hierarchies_ul');
			measuresDragAndDropObject	= new MyDragAndDropObject('source_measures_ul');
			measuresDragAndDropObject.createDragAndDropItems();
			
			//createDragAndDropItems('source_ul');
			//createDragAndDropItems('dest_col_ul');
			//new YAHOO.util.DDTarget('dest_li_1');
			//new YAHOO.util.DD('logDiv');
			for (var i=1; i<YAHOO.amp.reportwizard.numOfSteps; i++) {
				tab		= YAHOO.amp.reportwizard.tabView.getTab(i);
				tab.set("disabled", true);
			}
			tab2	= YAHOO.amp.reportwizard.tabView.getTab(2);
			tab2.addListener("beforeActiveChange", generateHierarchies);
			
			ColumnsDragAndDropObject.selectObjsByDbId ("source_col_div", "dest_col_ul", selectedCols);
			generateHierarchies();
			MyDragAndDropObject.selectObjsByDbId ("source_hierarchies_ul", "dest_hierarchies_ul", selectedHiers);
			MyDragAndDropObject.selectObjsByDbId ("source_measures_ul", "dest_measures_ul", selectedMeas);
			
			saveReportEngine			= new SaveReportEngine("${savingMessage}","${failureMessage}");
			
			var dg			= document.getElementById("DHTMLSuite_treeNode1");
			var cn			= dg.childNodes;
			
			for (var i=0; i<cn.length; i++) {
				if ( cn[i].nodeName.toLowerCase()=="input" || cn[i].nodeName.toLowerCase()=="img" ||
					cn[i].nodeName.toLowerCase()=="a" )
					cn[i].style.display		= "none";
			}

			repManager.checkSteps();
		}
		YAHOO.util.Event.addListener(window, "load", initializeDragAndDrop) ;
	</script>


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="85%">
	<tr>

		<td valign="bottom">
			&nbsp;&nbsp;&nbsp;
			<c:set var="translation">
					<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
			</c:set>
			<digi:link href="/../aim/viewMyDesktop.do" styleClass="comment" title="${translation}" >
               	<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
               </digi:link> &gt; <digi:trn key="aim:reportwizard:reportgenerator">Report Generator</digi:trn>
			<br />
			<br />
		</td>
	</tr>
	<tr>
		<td align="left" vAlign="top">
		<digi:form action="/reportWizard.do" method="post" >
		<span id="formChild" style="display:none;">&nbsp;</span>
		<script type="text/javascript">
			<c:forEach items="${aimReportWizardForm.selectedColumns}" var="dbId">
				selectedCols.push('${dbId}');
			</c:forEach>	
			<c:forEach items="${aimReportWizardForm.selectedHierarchies}" var="dbId">
				selectedHiers.push('${dbId}');
			</c:forEach>	
			<c:forEach items="${aimReportWizardForm.selectedMeasures}" var="dbId">
				selectedMeas.push('${dbId}');
			</c:forEach>	
		</script>
		
		
		<html:hidden name="aimReportWizardForm" property="desktopTab"/>
		<html:hidden name="aimReportWizardForm" property="originalTitle"/>
		<div style="color: red; text-align: center; visibility: hidden" id="savingReportDiv">
		</div>
		<br />
		<div id="wizard_container" class="yui-navset">
		<ul class="yui-nav">
			<li id="reportdetails_tab_label" class="selected"><a href="#type_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:reportDetails">1. Report Details</digi:trn></div></a> </li>
			<li id="columns_tab_label" class="disabled"><a href="#columns_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:columns">2. Columns</digi:trn></div></a> </li>
			<li id="hierachies_tab_label" class="disabled"><a href="#hierarchies_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:hierarchies">3. Hierarchies</digi:trn></div></a> </li>
			<li id="measures_tab_label" class="disabled"><a href="#measures_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:measures">4. Measures</digi:trn></div></a> </li>
		</ul>
			<div class="yui-content" style="background-color: #EEEEEE">
				<div id="type_step_div" class="yui-tab-content" style="padding: 0px 0px 1px 0px;" >
					<div class="subtabs">
						
						<button id="step0_prev_button" type="button" class="toolbar-dis" disabled="disabled"
							onclick="repManager.previousStep();">
							<img src="/TEMPLATE/ampTemplate/images/prev_dis.png" class="toolbar" />
							<digi:trn key="btn:previous">Previous</digi:trn>
						</button>
						<button id="step0_next_button" type="button" class="toolbar-dis" 
							 onclick="repManager.nextStep()" disabled="disabled">
							<img src="/TEMPLATE/ampTemplate/images/next_dis.png" class="toolbar" /> 
							<digi:trn key="btn:next">Next</digi:trn>
						</button>
						<button type="button" class="toolbar-dis" disabled="disabled" name="save" 
																					onclick="saveReportEngine.decideToShowTitlePanel()" >
							<img height="16" src="/TEMPLATE/ampTemplate/images/save_dis.png" class="toolbar"/>
							Save
						</button>
						<button type="button" class="toolbar-dis" onclick="saveReportEngine.showTitlePanel()" disabled="disabled" name="save">
							<img height="16" src="/TEMPLATE/ampTemplate/images/save_as_dis.png" class="toolbar"/>
							Save As..
						</button>
					</div>
					<div style="height: 355px;">
					<br />
					<table cellpadding="15px" width="100%" align="center" >
						<tr>
							<td width="46%" style="vertical-align: top;">
								<span class="list_header">
									<digi:trn key="rep:wizard:fundingGrouping"> Funding Grouping </digi:trn>
								</span>
								<div align="center" id="reportGroupDiv" style="border: 1px solid gray; background-color: white; 
											vertical-align: bottom; padding-top: 5%; padding-left: 2%; padding-bottom: 5%; padding-right: 2%;">
								<table>
								<feature:display name="Donor Report" module="Reports">
                                             <tr>
                                               <td>
                                                 <html:radio property="reportType" value="donor"  onclick="repManager.checkSteps()">
                                                   <digi:trn key="aim:donorReport">
                                                   Donor Report (Donor Funding)
                                                   </digi:trn>
                                                 </html:radio>
                                               </td>
                                             </tr>
                                             </feature:display>
                                             <feature:display name="Regional Report" module="Reports">										
                                             <tr>
                                               <td>
                                                 <html:radio property="reportType" value="regional"  onclick="repManager.checkSteps()">
                                                   <digi:trn key="aim:regionalReport">
                                                   Regional Report (Regional Funding)
                                                   </digi:trn>
                                                 </html:radio>
                                               </td>
                                             </tr>
                                             </feature:display>
                                             <feature:display name="Component Report" module="Reports">
                                             <tr>
                                               <td>
                                 	                <html:radio property="reportType" value="component"  onclick="repManager.checkSteps()">
                                                   <digi:trn key="aim:componentReport">
                                                   Component Report (Component Funding)
                                                   </digi:trn>
                                                 </html:radio>
                                               </td>
                                             </tr>
                                             </feature:display>
                                               <feature:display module="Reports" name="Contribution Report">
                                                 <tr>
                                                   <td>
                                                     <html:radio property="reportType" value="contribution"  onclick="repManager.checkSteps()">
                                                       <digi:trn key="aim:contributionReport">
                                                       Contribution Report (Activity Contributions)
                                                       </digi:trn>
                                                     </html:radio>
                                                   </td>
                                                 </tr>
                                               </feature:display>
                                     </table>
									<br />
								</div>
							</td>
							<td width="47%" rowspan="2">
								<span class="list_header">
									<digi:trn key="aim:reportBuilder:ReporDescription">Report Description</digi:trn>
								</span>
								<br/>
								<html:textarea property="reportDescription" styleClass="inp-text" style="border: 1px solid gray;width: 100%; height: auto;" />
							</td>
						</tr>
						<tr>
							<td colspan="1">
								<span class="list_header">
									<digi:trn key="aim:reportBuilder:TotalsGrouping">Totals Grouping</digi:trn>
								</span>
								<div align="center" id="totalsGroupingDiv" style="border: 1px solid gray; background-color: white; vertical-align: bottom; width: 100%">
									<br />
									<html:checkbox property="hideActivities" value="true">
										<digi:trn key="aim:summaryReport">
											Summary Report
										</digi:trn>
									</html:checkbox>
									<br /><br />
									<html:radio property="reportPeriod" value="A">
										<digi:trn key="aim:AnnualReport">
											Annual Report
										</digi:trn>
									</html:radio>								
									<html:radio property="reportPeriod" value="Q">
										<digi:trn key="aim:QuarterlyReport">
											Quarterly Report
										</digi:trn>
									</html:radio>
									<html:radio property="reportPeriod" value="M">
										<digi:trn key="aim:MonthlyReport">
											Monthly Report
										</digi:trn>
									</html:radio>
									<br /><br />
								</div>
							</td>
						</tr>
					</table>
					</div>
				</div>
				<div id="columns_step_div"  class="yui-tab-content" align="center" style="padding: 0px 0px 1px 0px; display: none;">
					<div class="subtabs">
						<button id="step1_prev_button" type="button" class="toolbar"
							onclick="repManager.previousStep();">
							<img src="/TEMPLATE/ampTemplate/images/prev.png" class="toolbar" />
							<digi:trn key="btn:previous">Previous</digi:trn>
						</button>
						<button id="step1_next_button" type="button" class="toolbar-dis" 
							onclick="repManager.nextStep()" disabled="disabled">
							<img height="16" src="/TEMPLATE/ampTemplate/images/next_dis.png" class="toolbar" /> 
							<digi:trn key="btn:next">Next</digi:trn>
						</button>
						<button type="button" class="toolbar-dis" disabled="disabled" name="save" 
												onclick="saveReportEngine.decideToShowTitlePanel()" >
							<img height="16" src="/TEMPLATE/ampTemplate/images/save_dis.png" class="toolbar"/>
							Save
						</button>
						<button type="button" class="toolbar-dis" onclick="saveReportEngine.showTitlePanel()" disabled="disabled" name="save">
							<img src="/TEMPLATE/ampTemplate/images/save_as_dis.png" class="toolbar"/>
							Save As..
						</button>
						
					</div>
					<div style="height: 355px;">
					<table cellpadding="5px" style="vertical-align: middle" width="100%">
						<tr>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:availableColumns">Available Columns</digi:trn>
							</span>
							<div id="source_col_div" class="draglist">
								<jsp:include page="setColumns.jsp" />
							</div>
						</td>
						<td valign="middle" align="center">
							<button class="buton arrow" type="button" onClick="ColumnsDragAndDropObject.selectObjs('source_col_div', 'dest_col_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
							</button>
							<br/> <br />
							<button class="buton arrow" type="button" onClick="ColumnsDragAndDropObject.deselectObjs('dest_col_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_left.gif"/>
							</button>
						</td>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:selectedColumns">Selected Columns</digi:trn>
							</span>
							<ul id="dest_col_ul" class="draglist">
							
							</ul>
						</td>
						</tr>
						<tr>
							<td align="center">
								<span id="columnsMust">
								<font color="red">* 
									<digi:trn key="rep:wizard:hint:mustselectcolumn">
										Must select at least one column
									</digi:trn>
								</font>
								</span>
							</td>
							<td>&nbsp;</td>
							<td align="center" style="visibility: hidden;">
								<span id="columnsLimit" >
								<font color="red">* 
									<digi:trn key="rep:wizard:hint:limit3columns">
										You cannot select more than 3 columns in a desktop tab
									</digi:trn>
								</font>
								</span>
							</td>
						</tr>
					</table>
					</div>
				</div>
				<div id="hierarchies_step_div"  class="yui-tab-content"  style="padding: 0px 0px 1px 0px; display: none;">
					<div class="subtabs">
						<button id="step2_prev_button" type="button" class="toolbar"
							onclick="repManager.previousStep();">
							<img src="/TEMPLATE/ampTemplate/images/prev.png" class="toolbar" /> 
							<digi:trn key="btn:previous">Previous</digi:trn>
						</button>
						<button id="step2_next_button" type="button" class="toolbar-dis" 
							onclick="repManager.nextStep()" disabled="disabled">
							<img height="16" src="/TEMPLATE/ampTemplate/images/next_dis.png" class="toolbar" />
							<digi:trn key="btn:next">Next</digi:trn>
						</button>
						<button type="button" class="toolbar-dis" disabled="disabled" name="save" 
										onclick="saveReportEngine.decideToShowTitlePanel()" >
							<img height="16" src="/TEMPLATE/ampTemplate/images/save_dis.png" class="toolbar" />
							Save
						</button>
						<button type="button" class="toolbar-dis" onclick="saveReportEngine.showTitlePanel()" disabled="disabled" name="save">
							<img src="/TEMPLATE/ampTemplate/images/save_as_dis.png" class="toolbar"/>
							Save As..
						</button>
						
					</div>
					<div style="height: 355px;">
					<table cellpadding="5px" style="vertical-align: middle" width="100%" >
						<tr>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:availableHierarchies">Available Hierarchies</digi:trn>
							</span>
							<ul id="source_hierarchies_ul" class="draglist">
							</ul>
						</td>
						<td valign="middle" align="center">
							<button class="buton arrow" type="button" onClick="MyDragAndDropObject.selectObjs('source_hierarchies_ul', 'dest_hierarchies_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
							</button>
							<br/> <br />
							<button class="buton arrow" type="button" onClick="MyDragAndDropObject.deselectObjs('dest_hierarchies_ul', 'source_hierarchies_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_left.gif"/>
							</button>
						</td>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:selectedHierarchies">Selected Hierarchies</digi:trn>
							</span>
							<ol id="dest_hierarchies_ul" class="draglist">
							</ol>					
						</tr>
						<tr>
							<td colspan="3">
								<span id="hierarchiesMust">
								<font color="red">* 
									<digi:trn key="rep:wizard:hint:notmorehierarchies">
										You cannot select more than 3 hierarchies
									</digi:trn>
								</font>
								</span>
							</td>
						</tr>
					</table>
					</div>
				</div>
				<div id="measures_step_div" class="yui-tab-content" style="padding: 0px 0px 1px 0px; display: none;" >
					<div class="subtabs">
						
						<button id="step3_prev_button" type="button" class="toolbar"
							onclick="repManager.previousStep();">
							<img src="/TEMPLATE/ampTemplate/images/prev.png" class="toolbar" />
							<digi:trn key="btn:previous">Previous</digi:trn>
						</button>
						<button id="step3_next_button" type="button" class="toolbar-dis" 
							onclick="repManager.nextStep()" disabled="disabled">
							<img height="16" src="/TEMPLATE/ampTemplate/images/next_dis.png" class="toolbar" />
							<digi:trn key="btn:next">Next</digi:trn>
						</button>
						<button type="button" class="toolbar-dis" onclick="saveReportEngine.decideToShowTitlePanel()" 
								disabled="disabled" name="save">
							<img height="16" src="/TEMPLATE/ampTemplate/images/save_dis.png" class="toolbar"/>
							Save
						</button>
						<button type="button" class="toolbar-dis" onclick="saveReportEngine.showTitlePanel()" disabled="disabled" name="save">
							<img src="/TEMPLATE/ampTemplate/images/save_as_dis.png" class="toolbar"/>
							Save As..
						</button>
					</div>
					<div style="height: 355px;">
					<table cellpadding="5px" style="vertical-align: middle" width="100%">
						<tr>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:availableMeasures">Available Measures</digi:trn>
							</span>
							<ul id="source_measures_ul" class="draglist">
								<jsp:include page="setMeasures.jsp" />
							</ul>
						</td>
						<td valign="middle"  align="center">
							<button class="buton arrow" type="button" onClick="MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
							</button>
							<br/> <br />
							<button class="buton arrow" type="button" onClick="MyDragAndDropObject.deselectObjs('dest_measures_ul', 'source_measures_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_left.gif"/>
							</button>
						</td>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:selectedMeasures">Selected Measures</digi:trn>
							</span>
							<ul id="dest_measures_ul" class="draglist">
							</ul>					
						</tr>
						<tr>
							<td>
								<span id="measuresMust">
								<font color="red">* 
									<digi:trn key="rep:wizard:hint:mustselectmeasure">
										Must select at least one measure
									</digi:trn>
								</font>
								</span>
							</td>
							<td>&nbsp;</td>
							<td>
								<span id="measuresLimit" style="visibility: hidden">
								<font color="red">* 
									<digi:trn key="rep:wizard:hint:limit2measures">
										You cannot select more than 2 measures in a desktop tab
									</digi:trn>
								</font>
								</span>
							</td>
						</tr>
					
					</table>
					</div>
				</div>
			</div>
		</div>
		<%-- <div id="logDiv" style="border: medium solid red; width: 20%;">
		</div> --%>
			
		<div id="titlePanel" style="display: none">
			<div class="hd" style="font-size: 8pt">
				<digi:trn key="rep:wizard:enterTitle">Please enter a title for this report: </digi:trn>
			</div>
			<div class="bd" id="titlePanelBody">
			<html:text onkeyup="repManager.checkSteps()" property="reportTitle" styleClass="inp-text" 
					style="border: 1px solid gray; width: 100%; font-size: 8pt; font-weight: bolder;" />
			</div>
			<div class="ft" align="right">
				<button id="last_save_button" type="button" class="buton repbuton" 
					style="color: lightgray" onclick="repManager.nextStep()" disabled="disabled">
						<digi:trn key="btn:saveReport">Save Report</digi:trn>
				</button>
				&nbsp;&nbsp;&nbsp;
			</div
		</div>
		</digi:form>
	</td>
	</tr>
</table>
	