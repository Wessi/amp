<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>

<tr><td colspan='<bean:write name="columnReport" property="totalDepth"/>'>
<i><bean:write name="columnReport" property="name"/></i>
</td></tr>

<% int rowIdx=2; %>

<!-- generate report headings -->
<%int maxDepth=columnReport.getMaxColumnDepth(); %>
<%for(int curDepth=0;curDepth<=columnReport.getMaxColumnDepth();curDepth++,rowIdx++) {%>
<tr>
<logic:iterate name="columnReport" property="items" id="column" scope="page" type="org.dgfoundation.amp.ar.Column">
	<% column.setCurrentDepth(curDepth);%>
	<% int rowsp=column.getCurrentRowSpan(); %>
	<logic:iterate name="column" property="subColumnList" id="subColumn" scope="page">
	<td align="center" class=clsTableTitleCol rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
		<b><bean:write name="subColumn" property="name"/></b>
	</td>
	</logic:iterate>
</logic:iterate>
</tr>
<% } %>

<!-- generate report data -->

<logic:iterate name="columnReport" property="ownerIds" id="ownerId" scope="page">
<%rowIdx++; %>
<tr onmouseover="setPointer(this, <%=rowIdx%>, 'over', '#FFFFFF', '#CCFFCC', '#FECC99');" onmouseout="setPointer(this, <%=rowIdx%>, 'out', '#FFFFFF', '#CCFFCC', '#FFCC99');" onmousedown="setPointer(this, <%=rowIdx%>, 'click', '#FFFFFF', '#CCFFCC', '#FFCC99');">
	<logic:iterate name="columnReport" property="items" id="column" scope="page">
		<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="page" toScope="request"/>
		<jsp:include page="<%=viewable.getViewerPath()%>"/>	
	</logic:iterate>
</tr>
</logic:iterate>


<!-- generate total row -->
<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.ColumnReportData" scope="page" toScope="request"/>
<jsp:include page="TrailCells.jsp"/>
