<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="org.digijava.module.categorymanager.util.CategoryConstants"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<%@page import="org.digijava.module.contentrepository.util.DocumentManagerRights"%>

<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<digi:errors />


<digi:instance property="crDocumentManagerForm" />
<bean:define id="myForm" name="crDocumentManagerForm" toScope="page"
	type="org.digijava.module.contentrepository.form.DocumentManagerForm" />

<bean:define id="isTeamLeader" name="myForm" property="teamLeader" />
<bean:define id="meTeamMember" name="myForm" property="teamMember" />

<bean:define id="tMembers" name="myForm" property="teamMembers" />
<bean:define id="selectedType" name="myForm" property="type" />

<%@include file="documentManagerJsHelper.jsp" %>
<digi:ref href="css_2/desktop_yui_tabs.css" type="text/css" rel="stylesheet" /> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css"> 

<style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}

input.file {
	width: 300px;
	margin: 0;
}

input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity:0 ;
	filter:alpha(opacity: 0);
	width: 300px;
	opacity: 0;
	z-index: 2;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}

div.fakefile input {
	margin-bottom: 5px;
	margin-left: 0;
	width: 217px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: 300px;
	padding: 3px;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}
-->
</style>
<style>
.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important;
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}
</style>

<style>
.yui-skin-sam a.yui-pg-page{
margin-left: 2px;
padding-right: 7px;
font-size: 11px;
border-right: 1px solid rgb(208, 208, 208);
}

.yui-skin-sam .yui-pg-pages{
border: 0px;
padding-left: 0px;
}
.yui-pg-current-page {
    background-color: #FFFFFF;
    color: rgb(208, 208, 208);
    padding: 0px;
}
.current-page {
    background-color: #FF6000;
    color: #FFFFFF;
    padding: 2px;
    font-weight: bold;
}
</style>

<script language="javascript">
 var uploadDoc="<digi:trn jsFriendly='true'>Upload doc</digi:trn>";
 var addWebLink="<digi:trn jsFriendly='true'>Add Web Link</digi:trn>";
 var createFromTemplate="<digi:trn jsFriendly='true'>Create From Template</digi:trn>";
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
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
<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp.table");
	var privateListObj	= null;
	var teamListObj	= null;
	var breadcrumbName='Resources';
	var breadCrumbObj = null;
		
	function loadTab() {
		for (var i=0; i<4; i++) {
			var tab		= repositoryTabView.getTab(i);
			if ( tab.get("active") && !repositoryTabView.activatedLists[i] ) {				
				repositoryTabView.dynLists[i].sendRequest();
				repositoryTabView.activatedLists[i]		= true;
			}
		}
	}
	
	function afterPageLoad(e) {
		//set breadcrumb text
		var breadCrumbObj=$('div.breadcrump_cont').text(breadcrumbName);
		
		privateListObj			= new DynamicList(document.getElementById("my_markup"), "privateListObj","privateFilterDivId", ${meTeamMember.teamId}, '${meTeamMember.email}');
		privateListObj.filterInfoDivId	= "privateFilterInfo";
		//privateListObj.sendRequest();
		teamListObj				= new DynamicList(document.getElementById("team_markup"), "teamListObj","teamFilterDivId", ${meTeamMember.teamId}, null);
		teamListObj.filterInfoDivId	= "teamFilterInfo";
		//teamListObj.sendRequest();
		sharedListObj				= new SharedDynamicList(document.getElementById("shared_markup"), "sharedListObj","sharedFilterDivId");
		sharedListObj.filterInfoDivId	= "sharedFilterInfo";
		//sharedListObj.sendRequest();

		
		
		publicListObj			= new PublicDynamicList(document.getElementById("public_markup"), "publicListObj",null);
		//publicListObj.sendRequest();
		repositoryTabView				= new YAHOO.widget.TabView("demo");
		repositoryTabView.addListener("activeTabChange", loadTab);
		
		repositoryTabView.dynLists	= new Array();
		repositoryTabView.dynLists.push( privateListObj );
		repositoryTabView.dynLists.push( teamListObj );
		repositoryTabView.dynLists.push( sharedListObj );
		repositoryTabView.dynLists.push( publicListObj );

		repositoryTabView.activatedLists	= new Array();
		for (var i=0; i<4; i++)
			repositoryTabView.activatedLists.push(false);
		
		initFileUploads();
		
		loadTab();
		
		fPanel	= new FilterAsYouTypePanel("labelButtonId", getLabelFilterCallbackObj(privateListObj), "mainLabels");
		fAddPanel	= new FilterAsYouTypePanel("labelButtonId", labelCallbackObj, "addLabelPanel");
		fPanel.initLabelArray(false);
		fAddPanel.initLabelArray(false);
		
		teamFPanel	= new FilterAsYouTypePanel("teamLabelButtonId", getLabelFilterCallbackObj(teamListObj), "teamMainLabels");
		teamFPanel.initLabelArray(false);

		sharedFPanel	= new FilterAsYouTypePanel("sharedLabelButtonId", getLabelFilterCallbackObj(sharedListObj), "sharedMainLabels");
		sharedFPanel.initLabelArray(false);
		
	
		templateFPanel	= new FilterAsYouTypePanel("templateLabelButtonId", getTemplateLabelsCb("docFromTemplateForm", "templateFilterInfoDiv"), "templateMainLabels");
		templateFPanel.initLabelArray(false);
	}
	YAHOO.util.Event.on(window, "load", afterPageLoad); 
</script>
<script type="text/javascript">
	var W3CDOM = (document.createElement && document.getElementsByTagName);

	function initFileUploads() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));

		var fakeFileUpload2 = document.createElement('div');
		fakeFileUpload2.className = 'fakefile2';


		var button = document.createElement('input');
		button.type = 'button';
		button.className='buton';

		button.value = '<digi:trn key="aim:browse">Browse...</digi:trn>';
		fakeFileUpload2.appendChild(button);

		fakeFileUpload.appendChild(fakeFileUpload2);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];

 			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}
	}

	function validateAddDocumentLocal(){
		var ret = false;
		if(validateAddDocument() == true){
			hidePanel(0);
			//document.forms['crDocumentManagerForm'].docTitle.value = escape(document.forms['crDocumentManagerForm'].docTitle.value);
			//document.forms['crDocumentManagerForm'].docDescription.value = escape(document.forms['crDocumentManagerForm'].docDescription.value);
			//document.forms['crDocumentManagerForm'].style.visibility='hidden';
			//document.getElementById('msgLoading').style.visibility='visible';
			//alert('asd');
			ret = true;
		}
		return ret;
	}
	
	function getLabelFilterCallbackObj( listObj ) {
		var labelFilterCallbackObj	= {
					click: function(e, label) {
						this.listObj.addRemoveLabel(label);
						this.listObj.sendRequest();
					},
					applyClick: function(e, labelArray){
						this.listObj.emptyLabels();
						for (var i=0; i<labelArray.length; i++) {
							this.listObj.addRemoveLabel(labelArray[i]);
						}
						if (labelArray.length == 0) {
							this.listObj.emptyLabels();
						}
						this.listObj.sendRequest();
					},
					listObj: listObj
				}
		
		return labelFilterCallbackObj;
	}
	
	var labelCallbackObj	= {
			click: function(e, label) {
				var postStr	= "action=add&docUUID="+this.docUUID+"&labelUUIDs="+label.uuid;
				this.sendLabelRequest(postStr);
			},
			applyClick: function (e, labelArray){
				var postStr	= "action=add&applyClick=true&docUUID="+this.docUUID;
				for (var i=0; i<labelArray.length; i++) {
					postStr	+= "&labelUUIDs="+labelArray[i].uuid;
				}
				this.sendLabelRequest(postStr);
			},
			remove: function(dUUID, lUUID) {
				var postStr	= "action=remove&docUUID="+dUUID+"&labelUUIDs="+lUUID;
				this.sendLabelRequest(postStr);
			},
			sendLabelRequest: function (postStr) {
				YAHOO.util.Connect.asyncRequest('POST', '/contentrepository/label.do?', this, postStr);
			},
			failure: function () {
				alert("We are sorry but your request cannot be processed at this time");
			},
			success: function () {
				this.dynamicList.sendRequest();
			},
			docUUID: "",
			dynamicList: null
	}

	var menuPanelForUser	= new ActionsMenu("actionsButtonId","actionsMenu");
	var menuPanelForTeam	= new ActionsMenu("actionsButtonIdTeam","actionsMenu", true);

</script>

<!-- BREADCRUMP START -->
<div class="breadcrump">
<div class="centering">
<div class="breadcrump_cont">
<span class="sec_name"><digi:trn>Resources</digi:trn></span></div>
</div>
</div>
<!-- BREADCRUMP END --> 

<!-- POPUPS START-->
<div id="menuContainerDiv"></div>
<div id="addDocumentDiv" class="dialog">
				<div align="center">
				<div id="addDocumentErrorHolderDiv" style="font-size:11px; color: red"></div>
				<digi:form action="/documentManager.do" method="post" enctype="multipart/form-data" >
					<input type="hidden" name="type" id="typeId"/>
					<input type="hidden" name="uuid" id="nodeUUID"/>					
					<table cellpadding="3" cellspacing="3" border="0">						
						<tr>
							<td>
								<div class="t_sm"><b><digi:trn>Title</digi:trn>:</b><font color="red">*</font></div>
							</td>
							<td><html:text property="docTitle" size="5" styleClass="inputx" style="width:300px;" /></td>
						</tr>
						<tr>							
							<td>
								<div class="t_sm"><b><digi:trn>Description</digi:trn>:</b></div>
							</td>
							<td><html:textarea property="docDescription" cols="" rows="" style="width:300px; height:100px;" styleClass="inputx"/></td>
						</tr>
						<tr>
							<td>
								<div class="t_sm"><b><digi:trn>Notes</digi:trn>:</b></div>
							</td>
							<td><html:textarea property="docNotes" cols="" rows="" style="width:300px; height:100px;" styleClass="inputx"/></td>
						</tr>
						<tr>
		                    <td>
		                    	<div class="t_sm"><b><digi:trn>Year Of Publication</digi:trn>:</b></div>
		                    </td>
		                    <td>		
		                        <html:select property="yearOfPublication" styleClass="dropdwn_sm">
		                        	<html:option value="-1"><digi:trn>select...</digi:trn></html:option>
		                        	<c:forEach var="year" items="${crDocumentManagerForm.years}">
		                        		<html:option value="${year}">${year}</html:option>
		                        	</c:forEach>
		                        </html:select>
		                    </td>
		                </tr>
						
						<tr>
							<td>
								<div class="t_sm"><b><digi:trn>Type</digi:trn>:</b></div>
							</td>
							<td>
								<c:set var="translation">
									<digi:trn>Please select a type from below</digi:trn>
								</c:set>
								<category:showoptions  firstLine="${translation}" name="crDocumentManagerForm" property="docType"  keyName="<%= CategoryConstants.DOCUMENT_TYPE_KEY %>" styleClass="dropdwn_sm" />
							</td>
						</tr>						
						<tr id="tr_path">
							<td>
								<div class="t_sm"><b><digi:trn>Path</digi:trn>:</b><font color="red">*</font></div>
							</td>
							<td>
				                <div class="fileinputs"> 
									<input id="fileData" name="fileData" type="file" class="file buton">
			                	</div>
			               </td>
						</tr>
						<tr style="display: none" id="tr_url">
							<td>
								<div class="t_sm"><b><digi:trn>URL</digi:trn>:</b><font color="red">*</font></div>
							</td>
							<td><html:text property="webLink" size="32"></html:text></td>
						</tr>
						<tr><td colspan="2"><hr/></td> </tr>						
						<tr>
							<td align="right">
								<html:submit styleClass="buttonx" style="padding-bottom: 2px; padding-top: 2px;" onclick="return validateAddDocumentLocal()"><digi:trn>Submit</digi:trn></html:submit>&nbsp;
							</td>
							<td align="left">&nbsp;
								<button class="buttonx" type="button" style="padding-bottom: 1px; padding-top: 1px;"  onClick="hidePanel(0)">
									<digi:trn>Cancel</digi:trn>
								</button>
							</td>
						</tr>
					</table>
				</digi:form>
				</div>
		    </div>
<!-- POPUPS END-->

<!-- MAIN CONTENT PART START -->
<table border="0" cellspacing="0" cellpadding="0" width="1000" align="center">	
	<tbody>
		<tr>
		<td align=left vAlign=top>
			<div style="width:1000px;" class="yui-skin-sam" id="content"> 
				<div id="demo" class="yui-navset">			
				<ul class="yui-nav">
			        <feature:display name="My Resources" module="Resources">
			        	<c:if  test="${selectedType=='private' || selectedType=='version'}">
			        		<li id="tab1" class="selected"><a href="#my_res"><div><digi:trn>My Resources</digi:trn></div></a></li>
			        	</c:if>
			        	<c:if  test="${selectedType!='private' && selectedType!='version'}">
			        		<li id="tab1"><a href="#my_res"><div> <digi:trn>My Resources</digi:trn></div></a></li>
			        	</c:if>
			        </feature:display>
			        <feature:display name="Team Resources" module="Resources">
			        	<c:if  test="${selectedType=='team'}">
			        		<li id="tab2" class="selected"><a href="#team_res"><div class="tab_link"><digi:trn>Team Resources</digi:trn></div></a></li>
			        	</c:if>
			        	<c:if  test="${selectedType!='team'}">
			        		<li id="tab2"><a href="#team_res"><div class="tab_link"><digi:trn>Team Resources</digi:trn></div></a></li>
			        	</c:if>
					</feature:display>
					<feature:display name="Shared Resources" module="Resources">
						<c:if  test="${selectedType=='shared'}">
			        		<li id="tab3" class="selected"><a href="#shared_res"><div class="tab_link"><digi:trn>Shared Resources</digi:trn></div></a></li>
			        	</c:if>
			        	<c:if  test="${selectedType!='shared'}">
			        		<li id="tab3"><a href="#shared_res"><div class="tab_link"><digi:trn>Shared Resources</digi:trn></div></a></li>
			        	</c:if>
					</feature:display>					
					
					<feature:display name="Public Resources" module="Resources">
			        	<li id="tab4"><a href="#public_res"><div class="tab_link"><digi:trn>Public Resources</digi:trn></div></a></li>
			       </feature:display>
			    </ul> 
			     
			    <div class="yui-content" style="border-color: #d0d0d0">
			    	<feature:display name="My Resources" module="Resources">
			      		<div id="my_res" class="resource_popin" style="border: none;">				        	       
							<table border="0" cellPadding="1" cellSpacing="0" width="100%"style="position: relative; left: 0px" >
								<tr>
						        	<td>
							        	<button id="actionsButtonId" type="button" onclick="menuPanelForUser.toggleUserView();" class="buttonx"><digi:trn>Add Resource</digi:trn>
							        		<img  src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif"> 
							        	</button>
								    	<button id="filterButtonId" class="buttonx" type="button" onclick="privateListObj.getFilterPanel('filterButtonId','privateFilterDivId').show();">
								    		<digi:trn>Filters</digi:trn>
								    	</button>
								    	<button id="labelButtonId" class="buttonx" type="button" onclick="fPanel.toggleView();">
								    		<digi:trn>Labels</digi:trn>
								    	</button>
								    </td>								    
								</tr>
								<tr><td><hr style="width: 97%;margin-left: 0px; margin-right: 15px;height:2px;"/></td></tr>
								<tr>
									<td>
											<div style="width: 80%; float: left" class="t_sm" id="privateFilterInfo"></div>
											<div class="show_legend" align="right" style="width: 15%; float: left">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td style="font-size: 11px;font-family: Arial,sans-serif">
									          				<jsp:include page="legendForResources.jsp"/>
									          			</td>
									          		</tr>
									          	</table>
											</div>
											<br />
											<div id="my_markup" align="left" style="clear: both;" >
											</div>
									</td>
								</tr>
							</table>
							<bean:define id="filterDivId" value="privateFilterDivId" toScope="request" />
							<jsp:include page="filters/filters.jsp"/>	        
				        </div>
					</feature:display>
					
					
					<feature:display name="Team Resources" module="Resources">
						<div id="team_res" class="resource_popin"  style="border: none;">				        	       
							<table border="0" cellPadding="1" cellSpacing="0" width="100%"style="position: relative; left: 0px" >
								<tr>
						        	<td>
									<%if (DocumentManagerRights.hasAddResourceToTeamResourcesRights(request) ) { %>
										<button id="actionsButtonIdTeam" type="button" onclick="menuPanelForTeam.toggleTeamView();" class="buttonx"><digi:trn>Add Resource</digi:trn>
											<img  src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
										</button>
										<!-- 
											<button class="dr-menu buton" type="button" onClick="setType('team'); configPanel(0,'','','', false);showMyPanel(0, 'addDocumentDiv');">						
			                            		<digi:trn>Add Resource ...</digi:trn>            
											</button>
										 -->
										
									<%}%>
										<button id="teamFilterButtonId" class="buttonx" type="button" onclick="teamListObj.getFilterPanel('teamFilterButtonId','teamFilterDivId').show();">
								    		<digi:trn>Filters</digi:trn>
								    	</button>
								    	<button id="teamLabelButtonId" class="buttonx" type="button" onclick="teamFPanel.toggleView();">
								    		<digi:trn>Labels</digi:trn>
								    	</button>
									</td>
								</tr>						
								<tr><td><hr style="width: 97%;margin-left: 0px; margin-right: 15px;height:2px;"/></td></tr>	
								<tr>
									<td>									
											<div style="width: 80%; float: left" id="teamFilterInfo" class="t_sm"></div>
											<div class="show_legend" align="right" style="width: 15%; float: left">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td style="font-size: 11px;font-family: Arial,sans-serif">
									          				<jsp:include page="legendForResources.jsp"/>
									          			</td>
									          		</tr>
									          	</table>
											</div>
											<br />
											<div id="team_markup" align="left"  class="all_markup">
											</div>
									</td>
								</tr>
							</table>	
							<bean:define id="filterDivId" value="teamFilterDivId" toScope="request" />
							<jsp:include page="filters/filters.jsp"/>
				        </div>
					</feature:display>					
					
					<!-- Shared Resources Start  -->
					<feature:display name="Shared Resources" module="Resources">
						<div id="shared_res"  class="resource_popin" style="border: none;">				        	       
							<table border="0" cellPadding="1" cellSpacing="0" width="100%"style="position: relative; left: 0px" >
								<tr>
									<td>										
										<button id="sharedFilterButtonId" class="buttonx" type="button" onclick="sharedListObj.getFilterPanel('sharedFilterButtonId','sharedFilterDivId').show();">
								    		<digi:trn>Filters</digi:trn>
								    	</button>
								    	<button id="sharedLabelButtonId" class="buttonx" type="button" onclick="sharedFPanel.toggleView();">
								    		<digi:trn>Labels</digi:trn>
								    	</button>
									</td>
								</tr>						
								<tr><td><hr style="width: 97%;margin-left: 0px; margin-right: 15px;height:2px;"/></td></tr>	
								<tr>
									<td>										
										<div style="width: 80%; float: left" id="sharedFilterInfo" class="t_sm"></div>
										<br />
										<div id="shared_markup" align="left" class="all_markup">
												
										</div>
										<br />
									</td>
								</tr>
							</table>
							<bean:define id="filterDivId" value="sharedFilterDivId" toScope="request" />
							<jsp:include page="filters/filters.jsp"/>		        
				        </div>
					</feature:display>
					
					<!-- Shared Resources end  -->
					
					<!-- Public resources -->
					<feature:display name="Public Resources" module="Resources">
				        <div id="public_res"  class="resource_popin" style="border: none;">				        	       
							<table border="0" cellPadding=1 cellSpacing=0 width="100%" style="position: relative; left: 0px" >
								<tr>
									<td>										
										<div id="public_markup" align="left" class="all_markup">
										
										</div>
										<br />
									</td>
								</tr>
							</table>	        
				        </div>
			        </feature:display>
					<!--End public Resources-->
				</div>
			</div>
			</div>
		
		
						
		<%-- END -- Table for "My Documents" --%>
        <br />
      </td>
	</tr>
	</tbody>
</table>
<!-- MAIN CONTENT PART END -->
<br/>

<c:set var="publicResourcesWindowName">
	<digi:trn>Public Resources</digi:trn>
</c:set>
<c:set var="teammemberResourcesWindowName">
	<digi:trn>Team Member Resources</digi:trn>
</c:set>
<c:set var="sharedResourcesWindowName">
	<digi:trn>Shared Resources</digi:trn>
</c:set>
	
<%@include file="documentManagerDivHelper.jsp" %>

