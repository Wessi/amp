<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.dgfoundation.amp.ar.ArConstants"%>
<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>

<c:set var="categoryYear"><%=ArConstants.YEAR%></c:set>
<c:set var="categoryQuarter"><%=ArConstants.QUARTER%></c:set>

<%int rowIdx = 2;%>

<!-- generate report headings -->
<logic:equal name="columnReport" property="globalHeadingsDisplayed" value="false">
 <thead class="fixedHeader"> 
  <%int maxDepth = columnReport.getMaxColumnDepth();
  	columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
  %>
  <%for (int curDepth = 0; curDepth <= columnReport.getMaxColumnDepth(); curDepth++, rowIdx++) {%>
  <tr title="Report Headings">
    <logic:iterate name="columnReport" property="items" id="column" scope="page" type="org.dgfoundation.amp.ar.Column">
      <%
      	column.setCurrentDepth(curDepth);
      	int rowsp = column.getCurrentRowSpan();
      %>
		
		<logic:iterate name="column" property="subColumnList" id="subColumn" scope="page" type="org.dgfoundation.amp.ar.Column">
        <c:set var="reportHeading">
          <%=subColumn.getName(reportMeta.getHideActivities())%>
        </c:set>
        
        <logic:equal name="column" property="columnDepth" value="1">
        	<td style="border-bottom:#E2E2E2 1px solid;background-color:#EAEAEA;padding-left: 2px; padding-right: 2px " height="20px" nowrap="nowrap" align="center" class="clsTableTitleColHtml" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>          
	        	<logic:notEqual name="widget" scope="request" value="true">
	            	<html:link  style="font-family: Arial;font-size: 11px;text-decoration: none;color: black;cursor:pointer;" page="/viewNewAdvancedReport.do" paramName="column" paramProperty="name" paramId="sortBy">
	              		<digi:trn key="aim:reportBuilder:${reportHeading}"><c:out value="${reportHeading}"/></digi:trn>
                        <c:if test="${reportHeading == 'Undisbursed Balance'}">
                                <img src= "../ampTemplate/images/help.gif" border="0" title="<digi:trn key="aim:report:UndisbursedBalanceToolip">Cumulative Commitment - Cumulative Disbursement (independent of filters)</digi:trn>">
                        </c:if>
                        <c:if test="${reportHeading == 'Cumulative Commitment'}">
                            <img src= "../ampTemplate/images/help.gif" border="0" title="<digi:trn key="aim:report:CumulativeCommitmentToolip">Sum of all ACTUAL COMMITMENTS independent of filters</digi:trn>">
                        </c:if>
                        <c:if test="${reportHeading == 'Cumulative Disbursement'}">
                            <img src= "../ampTemplate/images/help.gif" border="0" title="<digi:trn key="aim:report:CumulativeDisbursementToolip">Sum of all ACTUAL DISBURSEMENTS independent of filters</digi:trn>">
                        </c:if>
                        <c:if test="${reportHeading == 'Undisbursed Cumulative Balance'}">
                            <img src= "../ampTemplate/images/help.gif" border="0" title="<digi:trn key="aim:report:UndisbursedCumulativeBalanceToolip">Cumulative Commitment - Cumulative Disbursement (independent of filters)</digi:trn>">
                        </c:if>
	              	</html:link>
	            </logic:notEqual>
            
	            <c:if test="${column.name == columnReport.sorterColumn}">
	            	<logic:equal name="columnReport" property="sortAscending" value="false">
	                	<img src= "../ampTemplate/images/down.gif" align="absmiddle" border="0"/>
	              	</logic:equal>
	              	<logic:equal name="columnReport" property="sortAscending" value="true">
	                	<img src= "../ampTemplate/images/up.gif" align="absmiddle" border="0"/>
	              	</logic:equal>
	            </c:if>
	         </td>
          </logic:equal>
          
        <logic:notEqual name="column" property="columnDepth" value="1">
        	<c:choose>
        		<c:when test="${subColumn.width!=1 || subColumn.contentCategory==categoryYear}">
        		<%
        			if(subColumn.getName().length()<5){%>
        				<td style="background-color:#EAEAEA; margin-left: 2px; margin-right: 2px;" class="clsTableTitleColHtml" height="20px" nowrap="nowrap" align="center" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
							<digi:trn key="aim:reportBuilder:${reportHeading}"><c:out value="${reportHeading}"/></digi:trn> 
					<%}else{%>
						<td class="clsTableTitleColHtml" style="background-color:#EAEAEA;text-decoration: none;border-right: #FFFFFF 1px solid;border-bottom: #FFFFFF 1px solid" height="15px" nowrap="nowrap" align="center" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
							<digi:trn key="aim:reportBuilder:${reportHeading}"><c:out value="${reportHeading}"/></digi:trn>	
          		<%}%>
          		</c:when>
          		<c:otherwise>
	      			<td style="background-color:#EAEAEA;border-right: #FFFFFF 1px solid;border-bottom: #FFFFFF 1px solid; font:9px Arial;" valign="bottom" height="15px" nowrap="nowrap" align="center" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
	      				<html:link style="font-family: Arial;font-size: 11px;text-decoration: none;color: black ;cursor:pointer;" page="/viewNewAdvancedReport.do" paramName="subColumn" paramProperty="name" paramId="sortBy">
	        				<digi:trn key="aim:reportBuilder:${reportHeading}">
	            				<c:out value="${reportHeading}"/>
	            			</digi:trn>
						</html:link>
		  			  
	            
					<c:if test="${subColumn.name == columnReport.sorterColumn}">
	        			<logic:equal name="columnReport" property="sortAscending" value="false">
	                		<img src= "../ampTemplate/images/down.gif" align="absmiddle" border="0"/>
	            		</logic:equal>
	            		<logic:equal name="columnReport" property="sortAscending" value="true">
	                		<img src= "../ampTemplate/images/up.gif" align="absmiddle" border="0"/>
	            		</logic:equal>
	       			</c:if>
	     		</c:otherwise>
	     		</c:choose>
	     	</td>	            
        	</logic:notEqual>
    	
	</logic:iterate>
   </logic:iterate>
  </tr>
  <%} %>
    </thead>
 
	<tbody>
  
  </logic:equal>
