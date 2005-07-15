<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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
			document.aimEditActivityForm.docWebResource.focus();
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
			document.aimEditActivityForm.docWebResource.focus();
		}
		return flag;
	}
	
	function validateResource()
	{
		var titleFlag = isEmpty(document.aimEditActivityForm.docTitle.value);
		var urlFlag = isEmpty(document.aimEditActivityForm.docWebResource.value);
		if(titleFlag == true && urlFlag == true)
		{
			alert("Please enter Title and URL");
			document.aimEditActivityForm.docTitle.focus();
			return false;
		}
		else
		{
			if(titleFlag == true)
			{
				alert(" Please enter Title");
				document.aimEditActivityForm.docTitle.focus();
				return false;
			}
			if(urlFlag == true)
			{
				alert(" Please enter URL ");
				document.aimEditActivityForm.docWebResource.focus();
				return false;
			}
			else
			{
				flag = validateUrl(document.aimEditActivityForm.docWebResource.value);
				return flag;
			}			
		}
		return true;
	}
	

	function validateDocument()
	{
		var titleFlag = isEmpty(document.aimEditActivityForm.docTitle.value);
		var fileFlag = isEmpty(document.aimEditActivityForm.docFile.value);
		if(titleFlag == true && fileFlag == true)
		{
			alert("Please enter Title and select a File");
			document.aimEditActivityForm.docTitle.focus();
			return false;
			
		}
		else
		{
			if(titleFlag == true)
			{
				alert(" Please enter title");
				document.aimEditActivityForm.docTitle.focus();
				return false;
			}
			if(fileFlag == true)
			{
				alert(" Please select File ");
				document.aimEditActivityForm.docFile.focus();
				return false;
			}
		}
		return true;
	}
	
	function addDocument() 
	{
		var resourceFlag, docFlag;
		if(document.aimEditActivityForm.docFileOrLink.value == "file")
		{
			docFlag = validateDocument();
			
		}
		else
			resourceFlag = validateResource();		
		if(docFlag == true || resourceFlag == true)
		{
			<digi:context name="addDoc" property="context/module/moduleinstance/documentSelected.do" />
			document.aimEditActivityForm.action = "<%= addDoc %>";	
		 	document.aimEditActivityForm.target = window.opener.name;	
		    document.aimEditActivityForm.submit();
			window.opener.document.aimEditActivityForm.currUrl.value="";
			window.close();			  
		}
	}

	function load() {
		document.aimEditActivityForm.docTitle.focus();
	}

	function unload() {
		window.opener.document.aimEditActivityForm.currUrl.value="";
	}
	function closeWindow() {
		window.opener.document.aimEditActivityForm.currUrl.value="";
		window.close();			  
	}

-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/documentSelected.do" method="post" enctype="multipart/form-data" onsubmit="return false;">
<html:hidden property="docFileOrLink" />
<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<logic:equal name="aimEditActivityForm" property="docFileOrLink" value="file">
									<digi:trn key="aim:selectDocument">
									Select document</digi:trn>
								</logic:equal>
								<logic:equal name="aimEditActivityForm" property="docFileOrLink" value="link">
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
									<logic:equal name="aimEditActivityForm" property="docFileOrLink" value="file">
									<tr>
										<td>
										<FONT color=red>*</FONT>
											<a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">										  <digi:trn key="aim:file">File</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">
											<html:file name="aimEditActivityForm" property="docFile" size="50"/>
											</a>
										</td>
									</tr>
									</logic:equal>
									<logic:equal name="aimEditActivityForm" property="docFileOrLink" value="link">
									<tr>
										<td>
										<FONT color=red>*</FONT>
										<a title="<digi:trn key="aim:WebSource">Web links related to the project</digi:trn>">
										<digi:trn key="aim:webResource">Web resource</digi:trn></a>
										</td>
										<td>
											<a title="<digi:trn key="aim:WebSource">Web links related to the project</digi:trn>">
											<html:text property="docWebResource" />
											</a>
										</td>
									</tr>
									</logic:equal>									
									<tr>
										<td align="center" colspan=2>
											<table cellPadding=5>
												<tr>
													<td>
														<input type="button" value="Add" class="dr-menu" onclick="addDocument()">
													</td>
													<td>
														<input type="reset" value="Clear" class="dr-menu">													
													</td>
													<td>
														<input type="button" value="Close" class="dr-menu"
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
