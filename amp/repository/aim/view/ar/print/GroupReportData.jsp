<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="groupReport" name="viewable" type="org.dgfoundation.amp.ar.GroupReportData" scope="request" toScope="page"/>
<logic:present name="groupReport" property="parent">
	<tr>
		<td colspan="3" style="font-size: 13pt; color:#000000;padding-bottom: 5px;padding-top: 5px;" >
			<b><bean:write name="groupReport" property="name"/></u></b>
		</td>
	</tr>
</logic:present>


<logic:iterate name="groupReport"  property="items" id="item" scope="page">
	
		<bean:define id="viewable" name="item" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<jsp:include flush="true"  page="<%=viewable.getViewerPath()%>"/>
		
</logic:iterate>

<!-- generate total row -->
<logic:present name="groupReport" property="parent">
	<tr>
	<td></td>
		<td colspan="2">
			<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.GroupReportData" scope="page" toScope="request"/>
			<jsp:include flush="true"  page="TrailCells.jsp"/>	
		</td>
	</tr>
		
		
</logic:present>
