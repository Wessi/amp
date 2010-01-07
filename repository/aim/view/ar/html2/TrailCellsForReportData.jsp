<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.digijava.kernel.translator.TranslatorWorker"%>
<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>

<!-- generate total row -->
	<c:if test="${reportData.levelDepth == 1}">
		<td nowrap="nowrap" style="border-bottom:#E2E2E2 1px solid;border-right:#E2E2E2 1px solid;" height="18px" >
		<span style="font-family: Arial;color:black; font-weight: bold;font-size: 12px;margin-left: 2px">
				<digi:trn key="rep:popup:reporttotals">Report Totals:</digi:trn>
			</span>
	</c:if>
	
	
	<c:if test="${reportData.levelDepth == 2}">
		<td rowspan="${reportData.rowSpan}" nowrap="nowrap" class="firstLevel hierarchyCell" height="15px" title='<bean:write name="reportData" property="repName"/>' >
			<span style="font-family: Arial;font-weight: bold;font-size: 10px;padding-left: 5px;padding-right: 3px;">
				<%if(reportData.getRepName().length()<40){ %>
					<% if (!("".equals(reportData.getRepName()))){ %>
					<!-- *************************************************** 
							WARNING:
								Do not add Translations here!
						 ***************************************************--> 
		 				${reportData.repName}
					<% 
					} 
				}else{%>
					<%=reportData.getRepName().substring(0,39)%>...
				<%} %>	
			</span>
		</c:if>
	
	<c:if test="${reportData.levelDepth == 3}">
		<td rowspan="${reportData.rowSpan}" nowrap="nowrap" class="secondLevel hierarchyCell" height="13px" >
			<span style="font-family: Arial;font-size: 9px;font-weight: bold;padding-left: 15px;padding-right: 3px">
			<% if (!("".equals(reportData.getRepName()))){ %>
			<!-- *************************************************** 
					WARNING:
						Do not add Translations here!
				 ***************************************************--> 
				${reportData.repName}
			<% } %>
			</span>
		</c:if>
	
	<c:if test="${reportData.levelDepth == 4}">
		<td nowrap="nowrap" class="thirdLevel hierarchyCell" 
				height="13px" rowspan="${reportData.rowSpan}" >
		<span style="font-family: Arial;font-size: 9px;font-weight: bold;padding-left: 25px;padding-right: 3px">
			<% if (!("".equals(reportData.getRepName()))){ %>
			<!-- *************************************************** 
					WARNING:
						Do not add Translations here!
				 ***************************************************--> 
		 		${reportData.repName}
			<% } %>
		</span>
	</c:if>
	</td>
	



