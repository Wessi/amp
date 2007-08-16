<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="groupReport" name="viewable" type="org.dgfoundation.amp.ar.GroupReportData" scope="request" toScope="page"/>

<logic:present name="groupReport" property="parent">
<tr><td colspan='<bean:write name="groupReport" property="totalDepth"/>'>
<b><bean:write name="groupReport" property="name"/></b>
</td></tr>
</logic:present>
<tr id='<bean:write name="groupReport" property="absoluteReportName"/>'><td>

<!-- generate report headings -->
<bean:define id="viewable" name="groupReport" property="firstColumnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="../reportHeadings.jsp"/>

<logic:iterate name="groupReport"  property="items" id="item" scope="page">
	<bean:define id="viewable" name="item" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
	<jsp:include page="<%=viewable.getViewerPath()%>"/>	
</logic:iterate>
</td></tr>

<!-- generate total row -->
<logic:present name="groupReport" property="parent">
<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.GroupReportData" scope="page" toScope="request"/>
<bean:define id="grandTotal" toScope="request" value="yes" property="java.lang.String" />
<jsp:include page="TrailCells.jsp"/>
<tr><td colspan='<bean:write name="groupReport" property="totalDepth"/>'>
&nbsp;
</td></tr>
</logic:present>