<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">

	<!-- 

	function editDocument(docId,actId) {
		document.aimRelatedLinksForm.activityId.value=actId;
		document.aimRelatedLinksForm.docId.value=docId;
		<digi:context name="editDoc" property="context/module/moduleinstance/updateDocumentDetails.do" />
		document.aimRelatedLinksForm.action = "<%= editDoc %>";	
	 	document.aimRelatedLinksForm.target = "_self";
	   	document.aimRelatedLinksForm.submit();
	}

	function deleteDocument(docId,actId) {
		document.aimRelatedLinksForm.activityId.value=actId;
		document.aimRelatedLinksForm.docId.value=docId;
		<digi:context name="deleteDoc" property="context/module/moduleinstance/deleteDocument.do" />
		document.aimRelatedLinksForm.action = "<%= deleteDoc %>";	
	 	document.aimRelatedLinksForm.target = "_self";
	   	document.aimRelatedLinksForm.submit();
	}

	-->

</script>


<digi:instance property="aimRelatedLinksForm" />
<digi:context name="digiContext" property="context" />
<digi:form action="/getDocumentDetails.do" method="post">

<html:hidden property="activityId" />
<html:hidden property="docId" />
<html:hidden property="pageId" />
<html:hidden property="valuesSet" value="false"/>

<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#ffffff"
class="box-border-nopadding">
	<tr>
		<td bgcolor="#dddddd" align="center">
			<b><digi:trn key="aim:documentDetails">Document Details</digi:trn></b>
		</td>
	</td>
	<tr>
		<td bgcolor="#ffffff" vAlign="top" align="center">
			<table width="100%%" cellSpacing="1" cellPadding="5" vAlign="top" align="center">
				<tr>
					<td width="20%" align="right" bgcolor="#f4f4f4">
						<b>
						<digi:trn key="aim:activityName">Activity</digi:trn></b>
					</td>
					<td width="80%" align="left" bgcolor="#f4f4f4">
						<c:out value="${aimRelatedLinksForm.document.activityName}" />
					</td>
				</tr>
				<tr>
					<td width="20%" align="right" bgcolor="#f4f4f4">
						<b>
						<digi:trn key="aim:docTitle">Title</digi:trn></b>
					</td>
					<td width="80%" align="left" bgcolor="#f4f4f4">
						<c:out value="${aimRelatedLinksForm.document.title}" />
					</td>
				</tr>				
				<tr>
					<td width="20%" align="right" bgcolor="#f4f4f4">
						<b>
						<digi:trn key="aim:docDescription">Description</digi:trn></b>
					</td>
					<td width="80%" align="left" bgcolor="#f4f4f4">
						<c:out value="${aimRelatedLinksForm.document.docDescription}" />
					</td>
				</tr>								
				<tr>
					<td width="20%" align="right" bgcolor="#f4f4f4">
						<b>
						<digi:trn key="aim:docType">Type</digi:trn></b>
					</td>
					<td width="80%" align="left" bgcolor="#f4f4f4">
						<c:if test="${aimRelatedLinksForm.document.isFile == true}">
							<digi:trn key="aim:docIsFile">File</digi:trn>
						</c:if>
						<c:if test="${aimRelatedLinksForm.document.isFile == false}">
							<digi:trn key="aim:docIsURL">URL</digi:trn>
						</c:if>
					</td>
				</tr>	
				<tr>
					<td width="20%" align="right" bgcolor="#f4f4f4">
						<b>
						<c:if test="${aimRelatedLinksForm.document.isFile == true}">
							<digi:trn key="aim:fileName">File name</digi:trn>
						</c:if>
						<c:if test="${aimRelatedLinksForm.document.isFile == false}">
							<digi:trn key="aim:url">URL</digi:trn>
						</c:if></b>
					</td>
					<td width="80%" align="left" bgcolor="#f4f4f4">
						<c:if test="${aimRelatedLinksForm.document.isFile == true}">
							<a href="<%=digiContext%>/cms/downloadFile.do?itemId=<c:out value="${aimRelatedLinksForm.document.docId}"/>">
								<c:out value="${aimRelatedLinksForm.document.fileName}" />
								<i><bean:write name="relatedLink" property="fileName" /></i></a>
						</c:if>
						<c:if test="${aimRelatedLinksForm.document.isFile == false}">
							<a href="<c:out value="${aimRelatedLinksForm.document.url}" />" target="_blank">
							<c:out value="${aimRelatedLinksForm.document.url}" /></a>
						</c:if>
					</td>
				</tr>				
			</table>
		</td>
	</tr>
	<c:if test="${aimRelatedLinksForm.pageId == 1}">
	<tr>
		<td vAlign="top" align="center">
			<table cellSpacing="3" cellPadding="3">
				<tr>
				
					<td><input type="button" value="    Edit    " class="dr-menu"
					onclick="editDocument(<c:out value="${aimRelatedLinksForm.document.docId}" />,<c:out value="${aimRelatedLinksForm.document.activityId}" />)">
					</td>
					<td><input type="button" value="Remove" class="dr-menu"
					onclick="deleteDocument(<c:out value="${aimRelatedLinksForm.document.docId}" />,<c:out value="${aimRelatedLinksForm.document.activityId}" />)">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</c:if>		
</table>
</digi:form>
