<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>


<script language="JavaScript">
<!-- 
	
	function checkDot(str, cnt)
	{
		var count = 0, index=0, flag;
		for (i = 0;  i < str.length;  i++)
		{
			if(str.charAt(i) == ".")
				count = count + 1;
			if(count == 2)
			{
				index = i + 1;
				break;
			}
				
		}
		var diff = str.length - index;
		if(count >= cnt && diff > 1)
			flag =  true;
		else
		{
			alert("URL format invalid ");
			document.crDocumentManagerForm.webLink.focus();
			flag =  false;
		}
		return flag;
	}
	
	function validateUrl(str)
	{
		str = trim(str);
		var flag;
		var temp="";
		if(str.substr(0,3) == "www")
			flag = checkDot(str, 2);
		else if(str.substr(0,7) == "http://")
		{
			temp = str.substring(7,10);
			if(temp == "www")
				flag = checkDot(str, 2);
			else
				flag = checkDot(str, 1);	
		}
		else
		{
			flag = false;
			alert("URl format invalid ");
			document.crDocumentManagerForm.webLink.focus();
		}
		return flag;
	}
	
	function validateResource()
	{
		var titleFlag = isEmpty(document.crDocumentManagerForm.docTitle.value);
		var urlFlag = isEmpty(document.crDocumentManagerForm.webLink.value);
		if(titleFlag == true && urlFlag == true)
		{
			alert("Please enter Title and URL");
			document.crDocumentManagerForm.docTitle.focus();
			return false;
		}
		else
		{
			if(titleFlag == true)
			{
				alert(" Please enter Title");
				document.crDocumentManagerForm.docTitle.focus();
				return false;
			}
			if(urlFlag == true)
			{
				alert(" Please enter URL ");
				document.crDocumentManagerForm.webLink.focus();
				return false;
			}
			else
			{
				flag = validateUrl(document.crDocumentManagerForm.webLink.value);
				return flag;
			}			
		}
		return true;
	}
	

	function validateDocument()
	{
		var titleFlag = isEmpty(document.crDocumentManagerForm.docTitle.value);
		var fileFlag = isEmpty(document.crDocumentManagerForm.fileData.value);
		if(titleFlag == true && fileFlag == true)
		{
			alert("Please enter Title and select a File");
			document.crDocumentManagerForm.docTitle.focus();
			return false;
			
		}
		else
		{
			if(titleFlag == true)
			{
				alert(" Please enter title");
				document.crDocumentManagerForm.docTitle.focus();
				return false;
			}
			if(fileFlag == true)
			{
				alert(" Please select File ");
				document.crDocumentManagerForm.fileData.focus();
				return false;
			}
		}
		return true;
	}
	
	function addDocument() 
	{
		var resourceFlag, docFlag;
		if(document.crDocumentManagerForm.webResource.value == "false")
		{
			docFlag = validateDocument();
			
		}
		else
			resourceFlag = validateResource();		
		if(docFlag == true || resourceFlag == true)
		{

			document.crDocumentManagerForm.action = "/contentrepository/addTemporaryDocument.do";	
		    document.crDocumentManagerForm.submit();
		}
	}

	function load() {
		document.crDocumentManagerForm.docTitle.focus();
	}

	function unload() {
	}
	function closeWindow() {
		window.close();			  
	}

-->
</script>

<style type="text/css">
<!--
div.fileinputs {
	height:25px;
	position:relative;
	width:450px;
}
input.file {
	margin:0px;
	size:10px;
	width:400px;
}
input.file.hidden {
	opacity:0;
	position:relative;
	text-align:right;
	width:370px;
	z-index:2;
}
input.button {
	background-color:#ECF3FD;
	border-color:#FFFFFF rgb(0, 51, 153) rgb(0, 51, 153) rgb(255, 255, 255);
	border-style:solid;
	border-width:1px;
	color:#000000;
	font-family:Verdana,Arial,Helvetica,sans-serif;
	font-size:11px;
	font-weight:bold;
	left:400px;
	position:absolute;
	text-decoration:none;
	top:0px;
	width:120px;
}
div.fakefile {
	left:0px;
	line-height:90%;
	margin:0pt;
	padding:0pt;
	position:absolute;
	top:0px;
	width:300px;
	z-index:1;
}
div.fakefile input {
	margin-bottom:0px;
	margin-left:0px;
	width:300px;
}
div.fakefile2 {
	left:300px;
	line-height:90%;
	margin:0pt;
	padding:0pt;
	position:absolute;
	top:0px;
	width:100px;
	z-index:1;
}
div.fakefile2 input {
	width:80px;
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



<digi:instance property="crDocumentManagerForm" />
<digi:form action="/addTemporaryDocument.do" method="post" enctype="multipart/form-data" onsubmit="return false;">
<html:errors/>
<html:hidden property="webResource" />
<html:hidden property="pageCloseFlag" />
<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<logic:equal name="crDocumentManagerForm" property="webResource" value="false">
									<digi:trn key="aim:selectDocument">
									Select document</digi:trn>
								</logic:equal>
								<logic:equal name="crDocumentManagerForm" property="webResource" value="true">
									<digi:trn key="aim:selectWebResource">
									Select web resource</digi:trn>
								</logic:equal>								
								
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td>
											<FONT color=red>*</FONT>
											<a title="<digi:trn key="aim:TitlefortheDocument">Title of the document to be attached</digi:trn>">
											<digi:trn key="aim:title">Title</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:TitlefortheDocument">Title of the document to be attached</digi:trn>">
											<html:text property="docTitle"  styleClass="inp-text" size="50"/>
											</a>
										</td>
									</tr>								
									<tr>
										<td>
											<a title="<digi:trn key="aim:DescoftheDocument">Description of the contents and intent of the document</digi:trn>">
											<digi:trn key="aim:description">Description</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:DescoftheDocument">Description of the contents and intent of the document</digi:trn>">
											<html:textarea property="docDescription" rows="4" cols="50" styleClass="inp-text"/>
											</a>
										</td>
									</tr>
									<field:display name="Document Comment" feature="Related Documents">
									<tr>
										<td>
											<a title="<digi:trn key="cr:NotesForDocument">Notes regarding the document</digi:trn>">
											<digi:trn key="cr:comments">Notes</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="cr:NotesForDocument">Notes regarding the document</digi:trn>">
											<html:textarea property="docNotes" rows="3" cols="50" styleClass="inp-text"/>
											</a>
										</td>
									</tr>
									</field:display>
									
									<field:display name="Document Type" feature="Related Documents">
										<tr>
											<td>
												<a title="<digi:trn key="aim:typeOfTheDocumentDescription">Select type of document</digi:trn>">
													<digi:trn key="aim:typeOfTheDocument">Document type</digi:trn>
												</a>
											</td>
											<td>
												<c:set var="translation">
													<digi:trn key="aim:addActivityDocTypeFirstLine">Please select from below</digi:trn>
												</c:set>
												<a title="<digi:trn key="aim:typeOfTheDocumentDescription">Select type of document</digi:trn>">
													<category:showoptions firstLine="${translation}" name="crDocumentManagerForm" property="docType" categoryName="<%=org.digijava.module.aim.helper.CategoryConstants.DOCUMENT_TYPE_NAME %>" styleClass="inp-text"/>
												</a>
											</td>
										</tr>
									</field:display>
									
									<field:display name="Document Language" feature="Related Documents">
									<tr>
										<td>
											<a title="<digi:trn key="aim:languageOfTheDocumentDescription">Select document language</digi:trn>">
											<digi:trn key="aim:langOfTheDocument">Document Language</digi:trn>
											</a>
										</td>
										<td>
											<c:set var="translation">
													<digi:trn key="aim:addActivityDocLanguageFirstLine">Please select from below</digi:trn>
											</c:set>
											<a title="<digi:trn key="aim:languageOfTheDocumentDescription">Select document language</digi:trn>">
											<category:showoptions firstLine="${translation}" name="crDocumentManagerForm" property="docLang" keyName="<%=org.digijava.module.aim.helper.CategoryConstants.DOCUMENT_LANGUAGE_KEY %>" styleClass="inp-text"/>
											</a>
										</td>
									</tr>
									</field:display>
									
									<logic:equal name="crDocumentManagerForm" property="webResource" value="false">
									<tr>
										<td>
										<FONT color=red>*</FONT>
											<a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">										  <digi:trn key="aim:file">File</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">
												<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
													<input id="fileData" name="fileData" type="file" class="file"/>
												</div>
											</a>
										</td>
									</tr>
									</logic:equal>
									<logic:equal name="crDocumentManagerForm" property="webResource" value="true">
									<tr>
										<td>
										<FONT color=red>*</FONT>
										<a title="<digi:trn key="aim:WebSource">Web links related to the project</digi:trn>">
										<digi:trn key="aim:webResource">Web resource</digi:trn></a>
										</td>
										<td>
											<a title="<digi:trn key="aim:WebSource">Web links related to the project</digi:trn>">
											<html:text property="webLink" />
											</a>
										</td>
									</tr>
									</logic:equal>
									
									
																	
									<tr>
										<td align="center" colspan=2>
											<table cellPadding=5>
												<tr>
													<td>
														<input type="button" value="<digi:trn key="aim:DmfAdd">Add</digi:trn>" class="dr-menu" onclick="addDocument()">
													</td>
													<td>
														<input type="reset" value="<digi:trn key="aim:DmfClear">Clear</digi:trn>" class="dr-menu">													
													</td>
													<td>
														<input type="button" value="<digi:trn key="aim:DmfClose">Close</digi:trn>" class="dr-menu"
														onclick="closeWindow()">
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>					
					</table>
				</td>
			</tr>
		</table>
	</td></tr>
</table>
</digi:form>

<script type="text/javascript">
	initFileUploads();
	if ( document.crDocumentManagerForm.pageCloseFlag.value == "true" ) {
			window.opener.location.replace(window.opener.location.href); 
			window.close();
		}
</script>
