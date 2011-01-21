<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@page import="org.digijava.module.contentrepository.util.DocumentManagerUtil" %>

<c:if test="${empty firstOrganisationPopupInclusion}">
	<c:set var="firstOrganisationPopupInclusion" value="true" scope="request"/>
	<script type="text/javascript">
	function showDocuments(objId) {
			var sessionName	= "<%= org.digijava.module.aim.dbentity.AmpOrganisationDocument.SESSION_NAME %>";
			var url			= "/aim/insertDocumentsInSession.do?objId="+objId+"&sessionName="+sessionName;
			var popupName	= 'my_popup';
			window.open(url, popupName, 'width=900, height=300');
			document.forms[0].action=url;
			document.forms[0].target=popupName;
			document.forms[0].submit();
	}
	</script>
</c:if>

<logic:notEmpty name="currentOrg" scope="request">
<bean:define id="org" name="currentOrg"
	type="org.digijava.module.aim.helper.RelOrganization" scope="request"
	toScope="page" />
<div style='position:relative;display:none;' id='org-<bean:write name="org" property="orgCode"/>'> 
<ul>
<c:set var="ampOrg" scope="page" value="${org.ampOrganisation}" />
<c:set var="onClickText" value="" scope="page" />
<c:set var="uniqueId" value="" scope="page" />
<c:if test="${!empty ampOrg}" >
	<c:set var="uniqueId" value="${ampOrg.uniqueDocumentId}" />
</c:if>
	<% 
	String uuid				= (String)pageContext.findAttribute("uniqueId");
	pageContext.setAttribute("uniqueWebLink", DocumentManagerUtil.getWebLinkByUuid(uuid, request) );
	%>
<c:if test="${ (!empty ampOrg) && (!empty uniqueId) }">
	<c:if test="${empty uniqueWebLink}" >
		<c:set var="onClickText" scope="page">
		window.location='/contentrepository/downloadFile.do?uuid=${ampOrg.uniqueDocumentId}'
		</c:set>
	</c:if>
	<c:if test="${!empty uniqueWebLink}" >
		<c:set var="onClickText" scope="page">
		window.open('${uniqueWebLink}');
		</c:set>
	</c:if>
</c:if>
<c:if test="${(!empty ampOrg) && (empty ampOrg.uniqueDocumentId) && (!empty ampOrg.documents)}">
	<c:set var="onClickText" scope="page">
	showDocuments( ${ampOrg.ampOrgId} )
	</c:set>
</c:if>

<c:if test="${!empty org.orgName}">
	<li> Organization Name: <bean:write name="org" property="orgName"/></li>
</c:if>
<c:if test="${!empty org.acronym}">
	<li>Organization Acronym: <bean:write name="org" property="acronym"/></li>
</c:if>
<c:if test="${!empty org.orgTypeId}">
	<c:if test="${!empty org.orgTypeId.orgType}">
		<li>Organization Type: <bean:write name="org" property="orgTypeId.orgType"/></li>
	</c:if>
</c:if>
<c:if test="${!empty org.orgGrpId}">
	<c:if test="${!empty org.orgGrpId.orgGrpName}">
		<li>Organization Group: <bean:write name="org" property="orgGrpId.orgGrpName"/></li>
	</c:if>
</c:if>
<c:if test="${!empty org.orgCode}">
<li>Organization Code: <bean:write name="org" property="orgCode"/></li>
</c:if>

</ul>
</div>
<ul>
<li>
	<div align="left" width="2" 
	style="display: inline"
	onMouseOver="stm(['<bean:write name="org" property="orgCode"/> Details',document.getElementById('org-<bean:write name="org" property="orgCode"/>').innerHTML],Style[0])" 
	onMouseOut="htm()">
	[ <u><bean:write name="org" property="orgName"/>
		<logic:notEmpty name="org" property="additionalInformation" >
			(${org.additionalInformation})
		</logic:notEmpty></u>]
	</div>
	<c:if test="${onClickText!=''}">
	   -&gt; <a style="cursor:pointer; text-decoration:underline; color: blue" onclick="${onClickText}" >
	   <digi:trn key="aim:organisation:moreInfo">more info</digi:trn>
	   </a>
    </c:if>
</li>
</ul>
</logic:notEmpty>