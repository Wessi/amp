<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.dgfoundation.amp.ar.cell.Cell" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@page import="org.dgfoundation.amp.ar.CellColumn"%>
<bean:define id="cellColumn" name="viewable" type="org.dgfoundation.amp.ar.CellColumn" scope="request" toScope="page"/>
<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>
<bean:define id="isGroup" name="isGroup" type="java.lang.String" scope="request" toScope="page"/>

<% if (isGroup.equalsIgnoreCase("false")){%>
<td width="5%">&nbsp</td>
<%} %>
<td height="18" w nowrap="nowrap" style="font-size:8pt; font-weight: bold;border-bottom:1px solid;text-transform: uppercase">
<% Cell c=cellColumn.getByOwner(ownerId);%>
	<b>			
		<c:set var="key">
			aim:reportBuilder:<%=cellColumn.getName()%>
		</c:set>
	 	<digi:trn key="${key}">
	 		<%=cellColumn.getName()%>
		 </digi:trn>
	</b>
</td>
<td height="18"  style="font-size:8pt;border-bottom:1px solid;padding-left: 3px;" width="100%">
<% if(c!=null) {
	request.setAttribute("cell",c);
%> 
	<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.cell.Cell" scope="request" toScope="request"/>
	<jsp:include page="<%=viewable.getViewerPath()%>"/>	 
<% } 
	else { %> 
	&nbsp; 
<% } %>
</td>

