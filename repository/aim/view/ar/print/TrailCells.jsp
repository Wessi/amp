<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<bean:define id="reportData" name="viewable"  type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page" />

<table border="0" width="100%" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" style="margin-top: 15px;margin-bottom: 15px">
	<!-- generate total row -->
	<tr>
		<td style="text-align: left;text-transform: uppercase;border-bottom: 1px solid;padding: 2px;font-size: 8pt;" colspan="2">
			<b>
			<digi:trn key="rep:popup:totalsFor">TOTALS FOR</digi:trn> 
			<digi:trn key="rep:popu:${reportData.columnIdTrn}">${reportData.columnId}</digi:trn>
			<% if (!("".equals(reportData.getRepName()))){ %>
				: <digi:trn key="rep:pop:${reportData.repNameTrn}">${reportData.repName}</digi:trn>
			<% } %>
			</b>
		</td>
		<logic:iterate name="reportData" property="trailCells" id="cell" 	scope="page">
			<bean:define id="viewable" name="cell" 	type="org.dgfoundation.amp.ar.cell.AmountCell" scope="page"  toScope="request" />
			<logic:notEqual name="viewable" property="amount" value="0">
				<tr>
					<td width="50%"  style="text-align: left;text-transform: uppercase;border-bottom: 1px dotted;padding: 2px;font-size: 8pt;">
						<bean:define id="col" name="viewable" property="column" 	type="org.dgfoundation.amp.ar.Column" scope="request" 	toScope="request" />
						<logic:iterate id="name" name="col"  property="absoluteColumnNameAsList">
							<c:set var="key">
								aim:reportbuilder:${name}
							</c:set>
							<digi:trn key="${key}">
								<c:out value="${name}"></c:out>
							</digi:trn>
						</logic:iterate>
					 </td>
					<td width="50%" style="text-align: right;text-transform: uppercase;border-bottom: 1px dotted;padding: 2px;font-size: 8pt;padding-left: 10px"><jsp:include page="<%=viewable.getViewerPath()%>" /></td>
				</tr>
			</logic:notEqual>
		</logic:iterate>
	</tr>
</table>
