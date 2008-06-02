<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance id="tform" property="gisTableWidgetTeaserForm"/>

<c:if test="${ empty tform.table}">
	<table style="border : 1px solid silver">
		<tr>
			<td>
				No Data assigned.
			</td>
		</tr>
	</table>
</c:if>

<c:if test="${ ! empty tform.table}">
	<table 
		<c:if test="${tform.table.width != null}">
			width="${tform.table.width}" 
		</c:if>
		<c:if test="${tform.table.cssClass != null}">
			class="${tform.table.cssClass}" 
		</c:if>
		border="1">
		<c:forEach var="theader" items="${tform.table.headerRows}" varStatus="hstat">
			<tr>
				<c:forEach var="cell" items="${theader.cells}">
					<td>
						<strong>${cell.value}</strong>
					</td>
				</c:forEach>
			</tr>
		</c:forEach>
	</table>
</c:if>

