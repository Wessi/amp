<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>

<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="org.digijava.module.contentrepository.action.SelectDocumentDM"%>
<%@ page import="org.digijava.module.aim.helper.ActivityDocumentsConstants"%>

<%@include file="addDocumentPanel.jsp" %> 

<%@include file="documentManagerJsHelper.jsp" %>

<%@include file="documentManagerDivHelper.jsp" %>

<logic:notEmpty scope="session" name="<%= org.digijava.module.contentrepository.action.SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>">
	<logic:notEmpty name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" scope="session"
			property="<%=ActivityDocumentsConstants.RELATED_DOCUMENTS %>">
	<bean:define name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" 
			property="<%=ActivityDocumentsConstants.RELATED_DOCUMENTS %>"  id="relDocs" scope="session" toScope="page"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" scope="session"
			property="<%=ActivityDocumentsConstants.TEMPORARY_DOCUMENTS %>">
	<bean:define name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" 
			property="<%=ActivityDocumentsConstants.TEMPORARY_DOCUMENTS %>"  id="tempDocs" scope="session" toScope="page"/>
	</logic:notEmpty>

	<c:if test="${ (!empty relDocs) || (!empty tempDocs) }" >

		<div id="selDocumentsDiv"></div>
		<logic:notEmpty name="showRemoveButton" >
			<logic:equal name="showRemoveButton" value="true">
		&nbsp;&nbsp;&nbsp;&nbsp;
			<html:button  styleClass="buton" property="submitButton" onclick="removeSelectedDocuments()">
				<digi:trn key="btn:remove">Remove</digi:trn>
			</html:button>
			</logic:equal>
		</logic:notEmpty>
		<br /> <br />
		<logic:empty scope="request" name="dmWindowTitle"><bean:define toScope="request" id="dmWindowTitle" value=" " /></logic:empty>
			<script type="text/javascript">
				var rights			= null;
				<logic:notEmpty scope="request" name="crRights">
				rights			= {
						showVersionsRights	: ${showVersionsRights},
						versioningRights	: ${versioningRights},
						makePublicRights	: ${makePublicRights},
						deleteRights		: ${deleteRights}
				};
				</logic:notEmpty>
				windowController	= newWindow('<bean:write name="dmWindowTitle"/>',false,'selDocumentsDiv');
				windowController.populateWithSelDocs('<%= org.digijava.module.aim.helper.ActivityDocumentsConstants.RELATED_DOCUMENTS%>', rights);
				
			</script>
	</c:if>
</logic:notEmpty>