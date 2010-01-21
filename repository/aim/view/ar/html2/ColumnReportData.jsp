<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>

<c:set var="trailCellsFile">TrailCellsForReportData.jsp</c:set>
<c:if test="${columnReport.levelDepth == 1}">
	<c:set var="trailCellsFile">TrailCells.jsp</c:set>
</c:if>

<c:set var="markerColor" target="page">lightgreen</c:set>
<c:set var="skippedClass" target="page">hierarchyClass</c:set>
<c:set var="baseId" target="page">report_row_</c:set>

<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<!-- CRD-before trailCells!! -->
	<c:choose>
		<c:when test="${skipRowTag=='true'}">
			<!-- skipping row tag -->
			<jsp:include page="${trailCellsFile}"/>
			<c:set var="skipRowTag" scope="request">false</c:set>
		</c:when>
		<c:otherwise>
		<!-- printing row tag -->
			<tr onmousedown="getRowSelectorInstance(this,'${skippedClass}', '${baseId}', '${markerColor}').toggleRow()" 
				onMouseover="getRowSelectorInstance(this,'${skippedClass}', '${baseId}', '${markerColor}').markRow(false)" 
				onMouseout="getRowSelectorInstance(this,'${skippedClass}', '${baseId}', '${markerColor}').unmarkRow(false)">
				<jsp:include page="${trailCellsFile}"/>
		</c:otherwise>
	</c:choose>
<!-- CRD-after trailCells!! -->
<%int rowIdx = 2;%>

<!-- generate report data -->


<c:set var="skipRowTag">true</c:set>
<c:if test="${columnReport.levelDepth == 1}">
	<c:set var="skipRowTag">false</c:set>
</c:if>

<logic:notEqual name="reportMeta" property="hideActivities" value="true">	
<logic:iterate name="columnReport" property="ownerIds" id="ownerId" scope="page">
<logic:equal name="columnReport" property="canDisplayRow" value="true">
<%
	rowIdx++;	
	String width="0";
%>

<% 
	if(columnReport.getLevelDepth()<=2){
		request.setAttribute("pading","20px");
	}
	if (columnReport.getLevelDepth()==3){
		request.setAttribute("pading","30px");
	}
	if (columnReport.getLevelDepth()==4){
		request.setAttribute("pading","35px");
	}
%>
<!-- SSSS -${columnReport.currentRowNumber}- SSSS -->
<c:if test="${skipRowTag!='true'}">

	<%if (rowIdx%2==0){ %>
		<tr class="oddActivity" height="16px" 
			onmousedown="getRowSelectorInstance(this,'${skippedClass}', '${baseId}', '${markerColor}').toggleRow()" 
			onMouseover="getRowSelectorInstance(this,'${skippedClass}', '${baseId}', '${markerColor}').markRow(false)" 
			onMouseout="getRowSelectorInstance(this,'${skippedClass}', '${baseId}', '${markerColor}').unmarkRow(false)">
	<%}else{%>
		<tr height="16px" 
			onmousedown="getRowSelectorInstance(this,'${skippedClass}', '${baseId}', '${markerColor}').toggleRow()" 
			onMouseover="getRowSelectorInstance(this,'${skippedClass}', '${baseId}', '${markerColor}').markRow(false)" 
			onMouseout="getRowSelectorInstance(this,'${skippedClass}', '${baseId}', '${markerColor}').unmarkRow(false)">
	<%}%>
</c:if>

		<c:if test="${addFakeColumn}">
			<td></td>
		</c:if>
	<logic:iterate name="columnReport" property="items" id="column" scope="page">
		<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="page" toScope="request"/>
		<jsp:include page="<%=viewable.getViewerPath()%>"/>
	</logic:iterate>
<c:if test="${skipRowTag!='true'}">	
	</tr>
</c:if>
<c:set var="skipRowTag">false</c:set>
</logic:equal>
</logic:iterate>
</logic:notEqual>

<c:if test="${columnReport.levelDepth > 1}">	
	<%-- Here we include the totals for this ColumnReportData --%>
	<tr class="${columnReport.htmlClassName}">
		<td class="hierarchyCell">&nbsp;</td>
		<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<jsp:include page="TrailCells.jsp" />
	</tr>
</c:if>
