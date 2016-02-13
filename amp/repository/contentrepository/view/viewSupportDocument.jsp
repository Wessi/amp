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

<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />

<%@include file="addDocumentPanel.jsp" %>

<digi:errors />

<digi:instance property="crDocumentManagerForm" />
<bean:define id="myForm" name="crDocumentManagerForm" toScope="page"
	type="org.digijava.module.contentrepository.form.DocumentManagerForm" />

<bean:define id="isTeamLeader" name="myForm" property="teamLeader" />
<bean:define id="meTeamMember" name="myForm" property="teamMember" />

<bean:define id="tMembers" name="myForm" property="teamMembers" />
<bean:define id="selectedType" name="myForm" property="type" />

<%@include file="documentManagerJsHelper.jsp" %>


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
	padding: 0;
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

<script language="javascript">
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}
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

</script>

<table border=0 bgColor=#ffffff cellPadding=0 cellSpacing=0 width="85%"
	class="box-border-nopadding">
	<tr>
		<td valign="bottom" class="crumb" >
			&nbsp;&nbsp;&nbsp;
			<logic:present name="ampAdmin" scope="session">
				<logic:equal name="ampAdmin" value="no">
					<c:set var="translation1">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
					</c:set>
					<digi:link href="/../aim/viewMyDesktop.do" styleClass="comment" title="${translation1}" >
		            	<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
		            </digi:link>
		    	</logic:equal>
		    </logic:present>
		    <logic:present name="ampAdmin" scope="session">
				<logic:equal name="ampAdmin" value="yes">
		            <c:set var="translation2">
		                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
		            </c:set>
		            <digi:link href="/../admin.do" styleClass="comment" title="${translation2}" >
		                <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
		            </digi:link>   
           	</logic:equal>
            </logic:present>         
            &nbsp;&gt;&nbsp;
            <digi:trn key="aim:supportdocuments">Support Documents</digi:trn>
			<br />
		</td>
	</tr>
	<tr>
		<td align="left" vAlign="top">
				
			<span class="subtitle-blue"> &nbsp;&nbsp; 
				<digi:trn key="aim:supportdocuments">Support Documents</digi:trn>
			</span> 
			<br />
			<table border="0" cellPadding=5 cellSpacing=0 width="95%"
			style="position: relative; left: 10px">
			<tr><td>
			<div id="demo" class="yui-navset">			
				<ul class="yui-nav">
			        <feature:display name="Support Documents" module="HELP">
			        	<li id="tab1" class="selected">
			        		<a href="#sup_docs">
				        		<div>dsdsds
				        			<digi:trn key="aim:supportdocuments">Support Documents</digi:trn>
				        		</div>
				        	</a>
				        </li>
			        </feature:display>
			    </ul>            
			    <div class="yui-content" style="background-color: #EEEEEE;">
				    <feature:display name="Support Documents" module="HELP">
				        <div id="sup_docs" style="border-color: #27415f;border-left: thin solid #27415f; border-right: thin solid #27415f; border-bottom: thin solid #27415f;">
				        <div>
				        <table width="500" border="0" cellpadding="3" cellspacing="0" style="padding-left:30px;">
				        <tr>
				        <td>
				        	<logic:present name="ampAdmin" scope="session">
								<logic:equal name="ampAdmin" value="yes">
					        	<button type="button" class="dr-menu buton" onClick="setType('support');configPanel(0,'','','', false); showMyPanel(0, 'addDocumentDiv'); ">
							  		<digi:trn key="contentrepository:addResource">
							 	    	       Add Resource ...    				
							  		</digi:trn>            
						    	</button>
					    		</logic:equal>
					    	</logic:present>
					    </td>
					    </tr>	
					    </table>				    	
				        </div>
							  <table border="0" cellPadding=1 cellSpacing=0 width="95%"
								style="position: relative; left: 20px">
								<tr style="display: block;" id="myDocumentstr">
									<td colspan="3">
										<br />
										<div id="support_markup" align="left" class="all_markup">
										<bean:define name="crDocumentManagerForm" property="supportDocuments" id="documentDataCollection" type="java.util.Collection" toScope="request" />
										<jsp:include page="documentTable.jsp" flush="true" />
										</div>
										<br />
									</td>
								</tr>
							</table>
						</div>
					</feature:display>					
				</div>	
			</div>		
			<div id="addDocumentDiv" style="display: none">
				<div align="center">
				<div id="addDocumentErrorHolderDiv" style="font-size:11px; color: red"></div>
				<digi:form action="/documentManager.do?fwd=support" method="post" enctype="multipart/form-data" >
					<input type="hidden" name="type" id="typeId"/>
					<input type="hidden" name="uuid" id="nodeUUID"/>
					<table cellpadding="3" cellspacing="3" border="0">
						<tr>
							<td> 
								<digi:trn key="contentrepository:addEdit:typeDocument">Document</digi:trn>
								<input name="webResource" type="radio" value="false" onclick="selectResourceType()" />
							</td>
							<td> 
								<digi:trn key="contentrepository:addEdit:typeUrl">URL</digi:trn>
								<input name="webResource" type="radio" value="true" onclick="selectResourceType()"/>
							</td>
						</tr>
						<tr>
						<td><strong><digi:trn key="contentrepository:addEdit:Title">Title:</digi:trn></strong><font color="red">*</font></td>
						<td><html:text property="docTitle" size="30" /></td>
						</tr>
						<tr>
						<td><strong><digi:trn key="contentrepository:addEdit:Description">Description:</digi:trn></strong></td>
						<td><html:textarea property="docDescription" cols="28"/></td>
						</tr>
						<tr>
						<td><strong><digi:trn key="contentrepository:addEdit:Notes">Notes:</digi:trn></strong></td>
						<td><html:textarea property="docNotes" cols="28" /></td>
						</tr>
						<tr>
						<td><strong><digi:trn key="aim:typeOfTheDocument">Type:</digi:trn></strong></td>
						<td>
							<c:set var="translation">
								<digi:trn key="contentrepository:doctype:firstline">Please select a type from below</digi:trn>
							</c:set>
							<category:showoptions  firstLine="${translation}" name="crDocumentManagerForm" property="docType"  keyName="<%= CategoryConstants.DOCUMENT_TYPE_KEY %>" styleClass="inp-text" />
						</td>
						</tr>
						<tr id="tr_path">
						<td><strong><digi:trn key="contentrepository:addEdit:Path">Path:</digi:trn><font color="red">*</font></strong></td>
						<td>
			                             <!-- <html:file property="fileData"></html:file> -->
			                             <div class="fileinputs"> 
							
						<input id="fileData" name="fileData" type="file" class="file buton">
			                        </div></td>
						</tr>
						<tr style="display: none" id="tr_url">
						<td><strong><digi:trn key="contentrepository:addEdit:Url">URL:</digi:trn><font color="red">*</font></strong></td>
						<td><html:text property="webLink" size="32"></html:text></td>
						</tr>
						<tr>
							<td align="right">
								<html:submit styleClass="dr-menu buton" style="padding-bottom: 2px; padding-top: 2px;" onclick="return validateAddDocumentLocal()"><digi:trn key="contentrepository:addEdit:Submit">Submit</digi:trn></html:submit>&nbsp;
							</td>
							<td align="left">
								&nbsp;
								<button class="dr-menu buton" type="button" style="padding-bottom: 1px; padding-top: 1px;"  
								onClick="hidePanel(0)">
									<digi:trn key="contentrepository:addEdit:Cancel">Cancel</digi:trn>
								</button>
							</td>
						</tr>
					</table>
				</digi:form>
				</div>			        
		    </div>		
		</td></tr></table>
        <br />
      </td>
	</tr>
</table>
<br/>
	

<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp.table");

	function afterPageLoad(e) {
		YAHOO.amp.table.mytable	= YAHOO.amp.table.enhanceMarkup("support_markup");
		
		initFileUploads();
	}
	YAHOO.util.Event.on(window, "load", afterPageLoad); 
</script>