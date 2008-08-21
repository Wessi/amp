<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>


<%int rowIdx = 2;%>

<!-- generate report headings -->
<logic:equal name="columnReport" property="globalHeadingsDisplayed" value="false">
  <%int maxDepth = columnReport.getMaxColumnDepth();
  columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
  %>
  <%for (int curDepth = 0; curDepth <= columnReport
  .getMaxColumnDepth(); curDepth++, rowIdx++) {%>
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
        <td align="center" style="background-color:#999999;color:black;" class=clsTableTitleCol rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
            <logic:equal name="column" property="columnDepth" value="1">          
	            <logic:equal name="widget" scope="request" value="true">				
	              <a style="color:black;cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~sortBy=<bean:write name="subColumn" property="name"/>');">
		              <c:set var="portfTitle">
		                <%=subColumn.getName(reportMeta.getHideActivities())%>
		              </c:set>
		              <digi:trn key="aim:protfilio:${portfTitle}"><%=subColumn.getName(reportMeta.getHideActivities())%></digi:trn>
				  </a>
	            </logic:equal>
            
	            <logic:notEqual name="widget" scope="request" value="true">
	              <html:link style="color:black;cursor:pointer" page="/viewNewAdvancedReport.do" paramName="subColumn" paramProperty="name" paramId="sortBy">
	              <digi:trn key="aim:reportBuilder:${reportHeading}"><c:out value="${reportHeading}"/></digi:trn>
	              </html:link>
	            </logic:notEqual>
            
	            <c:if test="${subColumn.name == columnReport.sorterColumn}">
	              <logic:equal name="columnReport" property="sortAscending" value="false">
	                <img src= "../ampTemplate/images/down.gif" align="absmiddle" border="0"/>
	              </logic:equal>
	              <logic:equal name="columnReport" property="sortAscending" value="true">
	                <img src= "../ampTemplate/images/up.gif" align="absmiddle" border="0"/>
	              </logic:equal>
	            </c:if>
            
          </logic:equal>
            
          <logic:notEqual name="column" property="columnDepth" value="1">
			<logic:notEqual name="subColumn" property="width" value="1">
				<digi:trn key="aim:reportBuilder:${reportHeading}"><c:out value="${reportHeading}"/></digi:trn>
          	</logic:notEqual>
          	<logic:equal name="subColumn" property="width" value="1"> 
	            <logic:equal name="widget" scope="request" value="true">				
	              <a style="color:black;cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~sortBy=<bean:write name="subColumn" property="name"/>');">
		              <c:set var="portfTitle">
		                <%=subColumn.getName(reportMeta.getHideActivities())%>
		              </c:set>
		              <digi:trn key="aim:reportBuilder:${reportHeading}"><c:out value="${reportHeading}"/></digi:trn>
				  </a>
	            </logic:equal>
            
	            <logic:notEqual name="widget" scope="request" value="true">
	              <html:link style="color:black;cursor:pointer" page="/viewNewAdvancedReport.do" paramName="subColumn" paramProperty="name" paramId="sortBy">
	              	<digi:trn key="aim:reportBuilder:${reportHeading}"><c:out value="${reportHeading}"/></digi:trn>
	              </html:link>
	            </logic:notEqual>  
	            
				<c:if test="${subColumn.name == columnReport.sorterColumn}">
	              <logic:equal name="columnReport" property="sortAscending" value="false">
	                <img src= "../ampTemplate/images/down.gif" align="absmiddle" border="0"/>
	              </logic:equal>
	              <logic:equal name="columnReport" property="sortAscending" value="true">
	                <img src= "../ampTemplate/images/up.gif" align="absmiddle" border="0"/>
	              </logic:equal>
	            </c:if>
	                    		
          	</logic:equal>            
          </logic:notEqual>
          
        </td>

      </logic:iterate>
      
    </logic:iterate>
  </tr>

  <%}
  %>
  </logic:equal>
