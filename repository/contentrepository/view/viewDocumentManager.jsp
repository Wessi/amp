<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="org.digijava.module.aim.helper.CategoryConstants"%>

<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />

<%@include file="addDocumentPanel.jsp" %>

<digi:errors />

<digi:instance property="crDocumentManagerForm" />
<bean:define id="myForm" name="crDocumentManagerForm" toScope="page"
	type="org.digijava.module.contentrepository.form.DocumentManagerForm" />

<bean:define id="isTeamLeader" name="myForm" property="teamLeader" />
<bean:define id="meTeamMember" name="myForm" property="teamMember" />

<bean:define id="tMembers" name="myForm" property="teamMembers" />
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

	

</script>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="75%"
	class="box-border-nopadding">
	<tr>
		<td width=14>&nbsp;</td>
		<td valign="bottom" class="crumb" >
			&nbsp;&nbsp;&nbsp;
			<c:set var="translation">
					<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
			</c:set>
			<digi:link href="/../aim/viewMyDesktop.do" styleClass="comment" title="${translation}" >
               	<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
               </digi:link> &gt; <digi:trn key="contentrepository:contentRepository">Content Repository</digi:trn>
			<br />
			<br />		</td>
	</tr>
	<tr>
		<td width=14>&nbsp;</td>

		<td align=left vAlign=top>
				
			<span class="subtitle-blue"> &nbsp;&nbsp; 
				<digi:trn key="contentrepository:contentRepository">
							Content Repository
				</digi:trn> 
			</span> 
			<br />
			<table border="0" cellPadding=5 cellSpacing=0 width="95%"
			style="position: relative; left: 20px">
			<tr><td>
			<div id="demo" class="yui-navset">
			    <ul class="yui-nav">
			        <li id="tab1" class="selected"><a href="#my_res"><div><digi:trn key="rep:res:dhtmlTab:myresources">My Resources</digi:trn></div></a></li>
			        <li id="tab2"><a href="#team_res"><div><digi:trn key="rep:res:dhtmlTab:teamResources">Team Resources</digi:trn></div></a></li>
			        <li id="tab3"><a href="#public_res"><div><digi:trn key="rep:res:dhtmlTab:publicResources">Public Resources</digi:trn></div></a></li>
			        <li id="tab4"><a href="#team_mem_res"><div><digi:trn key="rep:res:dhtmlTab:otherResources">Other Resources</digi:trn></div></a></li>
			    </ul>            
			    <div class="yui-content" style="background-color: #EEEEEE;">
			        <div id="my_res" style="border-color: #27415f;border-left: thin solid #27415f; border-right: thin solid #27415f; border-bottom: thin solid #27415f;">
			        <div>
			        <table width="500" border="0" cellpadding="3" cellspacing="0" style="padding-left:30px;">
			        <tr>
			        <td>
			        	<button type="button" class="dr-menu" onClick="setType('private');configPanel(0,'','','', false); showMyPanel(0, 'addDocumentDiv'); ">
					  		<digi:trn key="contentrepository:addResource">
					 	    	       Add Resource ...    				
					  		</digi:trn>            
				    	</button>
				    <tr>
				    <td>	
				    </table>				    	
			        </div>
			        <jsp:include page="iconReferences.jsp"/>
						  <table border="0" cellPadding=1 cellSpacing=0 width="95%"
							style="position: relative; left: 20px">
							<tr style="display: table-row" id="myDocumentstr">
								<td colspan="3">
									<logic:notEmpty name="crDocumentManagerForm" property="myPersonalDocuments">
									<br />
									<div id="my_markup" align="center" class="all_markup">
									<bean:define name="crDocumentManagerForm" property="myPersonalDocuments" id="documentDataCollection" type="java.util.Collection" toScope="request" />
									<jsp:include page="documentTable.jsp" flush="true" />
									</div>
									<br />
									</logic:notEmpty>
								</td>
							</tr>
						</table>
					</div>

			        <div id="team_res" style="border-color: #27415f;border-left: thin solid #27415f; border-right: thin solid #27415f; border-bottom: thin solid #27415f;">

			        <div>
			        <table width="500" border="0" cellpadding="3" cellspacing="0" style="padding-left:30px;">
			        	<tr>
			        	<td>
						<c:if test="${isTeamLeader}">
							<button class="dr-menu" type="button" onClick="setType('team'); configPanel(0,'','','', false);showMyPanel(0, 'addDocumentDiv');">						
                            	<digi:trn key="contentrepository:addResource">
		 	    	       				Add Resource ...    							
	 	    	       			</digi:trn>            
							</button>
						</c:if>
						</td>
						</tr>
					</table>	
			        </div>
			        <jsp:include page="iconReferences.jsp"/>		        
					<table border="0" cellPadding=1 cellSpacing=0 width="100%"
						style="position: relative; left: 20px" >
						<tr style="display: table-row" id="teamDocumentstr">
							<td colspan="3">
								<logic:notEmpty name="crDocumentManagerForm" property="myTeamDocuments">
								<br />
								<div id="team_markup" align="center" class="all_markup">
								<bean:define name="crDocumentManagerForm" property="myTeamDocuments" id="documentDataCollection" type="java.util.Collection" toScope="request" />
								<jsp:include page="documentTable.jsp" flush="true" />
								</div>
								<br />
								</logic:notEmpty>
							</td>
						</tr>
					</table>
			        </div>
			        <div id="public_res" style="border-color: #27415f;border-left: thin solid #27415f; border-right: thin solid #27415f; border-bottom: thin solid #27415f;">
			        <div>
			        <jsp:include page="iconReferences.jsp"/>
			        </div>
				    <br/>
					<div id="publicDocumentsDiv">
					</div>			        
			        </div>
			        
			        <div id="team_mem_res" style="border-color: #27415f;border-left: thin solid #27415f; border-right: thin solid #27415f; border-bottom: thin solid #27415f;">
			        <div>
			        <jsp:include page="iconReferences.jsp"/>
			        </div>
				    <br/>
					<div id="otherDocumentsDiv">
					</div>
			    </div>			    
			</div>			
			<div id="addDocumentDiv" style="display: none">
				<div align="center">
				<div id="addDocumentErrorHolderDiv" style="font-size:11px; color: red"></div>
				<digi:form action="/documentManager.do" method="post" enctype="multipart/form-data" >
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
							<category:showoptions firstLine="${translation}" name="crDocumentManagerForm" property="docType"  keyName="<%= CategoryConstants.DOCUMENT_TYPE_KEY %>" styleClass="inp-text" />
						</td>
						</tr>
						<tr id="tr_path">
						<td><strong><digi:trn key="contentrepository:addEdit:Path">Path:</digi:trn><font color="red">*</font></strong></td>
						<td>
			                             <!-- <html:file property="fileData"></html:file> -->
			                             <div class="fileinputs"> 
							
						<input id="fileData" name="fileData" type="file" class="file">
			                        </div></td>
						</tr>
						<tr style="display: none" id="tr_url">
						<td><strong><digi:trn key="contentrepository:addEdit:Url">URL:</digi:trn><font color="red">*</font></strong></td>
						<td><html:text property="webLink" size="32"></html:text></td>
						</tr>
						<tr>
							<td align="right">
								<html:submit styleClass="dr-menu" onclick="return validateAddDocument()"><digi:trn key="contentrepository:addEdit:Submit">Submit</digi:trn></html:submit>&nbsp;
							</td>
							<td align="left">
								&nbsp;<button class="dr-menu" style="font-size: x-small" type="button" onClick="hidePanel(0)"><digi:trn key="contentrepository:addEdit:Cancel">Cancel</digi:trn></button>
							</td>
						</tr>
					</table>
				</digi:form>
				</div>			        
		    </div>		
		</td></tr></table>
		<%-- END -- Table for "My Documents" --%>
        <br />
      </td>
	</tr>
</table>
<br/>

	
<%@include file="documentManagerDivHelper.jsp" %>

<script type="text/javascript">
YAHOO.namespace("YAHOO.amp.table");
YAHOO.amp.table.mytable	= YAHOO.amp.table.enhanceMarkup("team_markup");
YAHOO.amp.table.teamtable	= YAHOO.amp.table.enhanceMarkup("my_markup");
</script>
<script type="text/javascript">
	initFileUploads();
</script>
<script type="text/javascript">
	function afterPageLoad(e) {
		windowController	= newWindow( '<digi:trn key="rep:res:dhtmltab:publicresources">Public Resources</digi:trn>', false, 'publicDocumentsDiv');
		windowController.populateWithPublicDocs();
		//This is to avoid a problem in French with a string with the character ' in the middle.
		var msgaux = '<digi:trn key="rep:res:dhtmltab:teammemberresources">Team Member Resources</digi:trn>';
		windowController	= newWindow( msgaux, true, 'otherDocumentsDiv');
		windowController.populateWithPublicDocs();
	}
	YAHOO.util.Event.on(window, "load", afterPageLoad); 
</script>	