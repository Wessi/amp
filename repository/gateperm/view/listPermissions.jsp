<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>

<script type="text/javascript">
<!--
function confirmDeletion(message)
{
	var msg= message;
	var temp = confirm(msg);
	return(temp);
}
//-->
</script>

<c:set var="message">
<digi:trn key="gateperm:permDeleteConfirm">Do you really want to delete this Permission object?</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>
<table id="permissionsList" align="center">
<thead>
<tr><td>Name</td><td>Description</td><td>Permission Type</td><td>Contents</td><td>Linked With</td><td>Change Permission</td></tr>
</thead>
<logic:iterate id="perm" name="allPermissions" scope="request">
<tr>
<td><bean:write name="perm" property="name"/></td>
<td><bean:write name="perm" property="description"/></td>
<td><bean:write name="perm" property="class.simpleName"/></td>
<td>
<logic:equal name="perm" property="class.simpleName" value="GatePermission">
Actions:
<bean:write name="perm" property="actions"/>
<br/>
Gate Init:
<bean:write name="perm" property="gateTypeName"/>
<br/>
Gate Parameters:	
<bean:write name="perm" property="gateParameters"/>
</logic:equal>
<logic:equal name="perm" property="class.simpleName" value="CompositePermission">
Permissions
<bean:write name="perm" property="permissions"/>
</logic:equal>
</td>
<td>
<bean:write name="perm" property="compositeLinkedPermissions"/>
</td>

<%--
<td> 
<bean:define id="listable" name="perm" scope="page" toScope="request"/>
<jsp:include page="${listable.jspFile}"/>
</td>
 --%>
<td>
<digi:link href="/managePerm.do?edit" paramId="permissionId" paramName="perm" paramProperty="id" title="EDIT">
<digi:img src="module/gateperm/images/edit.gif" border="0" />
</digi:link>
<digi:link href="/managePerm.do?delete" paramId="permissionId" onclick="return confirmDeletion('${msg}')" paramName="perm" paramProperty="id" title="DELETE">
<digi:img src="module/gateperm/images/delete.gif" border="0" />
</digi:link>
</td>
</tr>
</logic:iterate>
</table>

<div align="right">
<digi:link href="/managePerm.do?new" title="NEW">
<digi:img src="module/gateperm/images/add.gif" border="0" />&nbsp;Add New
</digi:link>
</div>


<script type="text/javascript">
<!--
var tableWidgetObj = new DHTMLSuite.tableWidget();
tableWidgetObj.setTableId('permissionsList');
tableWidgetObj.setTableWidth('80%');
tableWidgetObj.setTableHeight(250	);
//tableWidgetObjsetNoCssLayout();
tableWidgetObj.setColumnSort(Array('S','S','S'));
tableWidgetObj.init();
tableWidgetObj.sortTableByColumn(0);	// Initially sort the table by the first column
//-->
</script>