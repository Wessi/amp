<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div id="mySorter" style="display: none">
	<jsp:include page="/repository/aim/view/ar/levelSorterPicker.jsp" />
        <!--
		<a href='#' onclick='hideSorter();return false'>
			<b>
				<digi:trn key="rep:pop:Close">Close</digi:trn>
			</b>
		</a>
		 -->
</div>

<div id="myFilter" style="display: none">
		<jsp:include page="/aim/reportsFilterPicker.do" />
        <!--
		<a href='#' onclick='hideFilter();return false'>
			<b>
				<digi:trn key="rep:pop:Close">Close</digi:trn>
			</b>
		</a>
		-->
</div>

<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>

<!--
<div style='position:relative;display:none;' id="sorterPicker-<bean:write name="reportMeta" property="ampReportId"/>">

		<MM:BeginLock translatorClass="MM_SSI" type="ssi" orig="%3Cjsp:include page=%22/repository/aim/view/ar/levelSorterPicker.jsp%22 /%3E" fileRef="/repository/aim/view/ar/levelSorterPicker.jsp"><MM:EndLock>
		<br/>
		<a href='#' onclick='closeMessage();return false'><b><digi:trn key="rep:pop:Close">Close</digi:trn></b></a>
</div>
 -->
<!--
<div style='position:relative;display:none;' id="filterPicker-<bean:write name="reportMeta" property="ampReportId"/>">
		<MM:BeginLock translatorClass="MM_SSI" type="ssi" orig="%3Cjsp:include page=%22/aim/reportsFilterPicker.do%22 /%3E" fileRef="/aim/reportsFilterPicker.do"><MM:EndLock>
		<br/>
		<a href='#' onclick='closeMessage();return false'><b><digi:trn key="rep:pop:Close">Close</digi:trn></b></a>

</div>
 -->


<c:set var="rowIdx" value="<%=new Integer(0)%>" scope="request"/>


<div align="center">
<table  cellSpacing="0" cellPadding="0" width="99%" border="0" align="center">

	<logic:notEqual name="widget" scope="request" value="true">
	<logic:notEqual name="viewFormat" scope="request" value="print">
		<!-- attach filters -->
		<tr>
			<td><jsp:include page="/repository/aim/view/ar/toolBar.jsp" /></td>
		</tr>
		<tr>
		<!-- filters -->
		</tr>
	</logic:notEqual>
	</logic:notEqual>
	<tr>
		<td><bean:define id="reportMeta" name="reportMeta"
			type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
			toScope="page" />
		</td>
	</tr>

	<logic:equal name="viewFormat" scope="request" value="print">
		<script language="JavaScript">
	function load()
	{
		window.print();
	}

	function unload() {}
</script>
	</logic:equal>

<logic:notEqual name="widget" scope="request" value="true">

	<tr>
		<td align="left"><font size="+1"><digi:trn key="rep:pop:ReportName">Report Name:</digi:trn><b><bean:write
			scope="session" name="reportMeta" property="name" /></b></font></td>
	</tr>
	<tr>
		<td><b><digi:trn key="rep:pop:ReportDescription">Report Description:</digi:trn></b><i><bean:write scope="session" name="reportMeta"
			property="reportDescription" /></i></td>
	</tr>
	<tr bgcolor="#EEEEEE">
			<td>
			<table width="100%">
			<tr>
				<td width="50%">
				<font size="-5" face="arial" color="red">
					<span  STYLE="font-style:  italic">

					<c:set var="AllAmount">
					<%=org.digijava.module.aim.dbentity.AmpReports.getNote(session)%>
					</c:set>
					<digi:trn key="rep:pop:AllAmount"><%=org.digijava.module.aim.dbentity.AmpReports.getNote(session)%></digi:trn>
					<logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY %>">
					<bean:define id="selCurrency" name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY %>" />
					<digi:trn key="<%="aim:currency:" + ((String)selCurrency).toLowerCase().replaceAll(" ", "") %>">
						<%=selCurrency %>
					</digi:trn>
					</logic:present>
					</span>
				</font>
				</td>
				<td align="right">
					<logic:notEmpty name="reportMeta" property="hierarchies">
						<input type="button" onClick="showSorter(); " value="<digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn>">&nbsp;
					</logic:notEmpty>
					<input type="button" onClick="showFilter(); " value="<digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>">	&nbsp;			
				</td>
				</tr>
			</table>
			</td>
		</tr>
</logic:notEqual>
	
<logic:notEmpty name="reportMeta" property="hierarchies">
		<tr bgcolor="#EEEEEE">
			<td>
				<logic:notEmpty name="report" property="levelSorters">
				<logic:iterate name="report" property="levelSorters" id="sorter" indexId="levelId">
				<logic:present name="sorter">
					<digi:trn key="rep:pop:Level">Level</digi:trn> <bean:write name="levelId"/> <digi:trn key="rep:pop:sortedBy">sorted by</digi:trn> <bean:write name="sorter"/><br/>
				</logic:present>
				</logic:iterate>
				</logic:notEmpty>
			</td>
		</tr>
	</logic:notEmpty>
	<tr><td height="5">&nbsp;</td></tr>
	<tr bgcolor="#EEEEEE">
	<td width="500">
	<b><digi:trn key="rep:pop:SelectedFiltersCurrentCriterias">The following is the current filter criteria apllied to the report:</digi:trn></b><br><br>
		<logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" scope="session">
		<bean:define id="listable" name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" toScope="request"/>
		<bean:define id="listableStyle" value="list" toScope="request"/>
		<bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
			<jsp:include page="${listable.jspFile}"/>
		</logic:present>
	</td>
	</tr>
	<tr bgcolor="#EEEEEE">



	<logic:notEqual name="report" property="totalUniqueRows" value="0">
		<tr bgcolor="#EEEEEE">
			<td><!-- begin big report table -->
			<c:set var="pageNumber" value="<%=new Integer(0)%>" scope="request"/>
			<c:set var="paginar" value="<%=new Boolean(true)%>" scope="request"/>
			<c:if test="${not empty param.pageNumber }">
				<c:set var="pageNumber" value="<%=Integer.valueOf(request.getParameter("pageNumber"))%>" scope="request"/>
			</c:if>

			<table id='reportTable'  cellSpacing="0" cellPadding="1" width="100%" class="reportsBorderTable">
				<bean:define id="viewable" name="report"
					type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
				<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
			</table>

			<!-- end of big report table --></td>
		</tr>
		<tr>
			<td>
				<logic:equal name="viewFormat" value="print">
				<u><digi:trn key="rep:print:lastupdate">Last Update :</digi:trn></u>
				&nbsp;
				<c:if test="${reportMeta.updatedDate != null}">
					<bean:write scope="session" name="reportMeta" property="updatedDate"/>
				</c:if>
				&nbsp;
				<u><digi:trn key="rep:print:user">User :</digi:trn></u>
				<c:if test="${reportMeta.user != null}">
					<bean:write scope="session" name="reportMeta" property="user"/>
				</c:if>
				</logic:equal>
			<BR>
			</td>
		</tr>
		
			<tr>
			 <td>
				<logic:notEqual name="viewFormat" value="print">
				<digi:trn key="aim:pages">Pages :
				
				</digi:trn>&nbsp;
				<c:forEach var="i" begin="1" end="${report.visibleRows}" step="${recordsPerPage}">
					<logic:equal name="viewFormat" value="html">
							<a  style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=html~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage}"/>';">
					</logic:equal>
					<logic:equal name="viewFormat" value="foldable">
						<a  style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage}"/>');">	
					</logic:equal>
							<c:choose>							
								<c:when  test="${i eq report.startRow}">
									<font color="#FF0000"><fmt:formatNumber value="${(i-1)/recordsPerPage + 1}" maxFractionDigits="0"/></font>
								</c:when>
								<c:otherwise>
									<fmt:formatNumber value="${(i-1)/recordsPerPage + 1}" maxFractionDigits="0"/>
								</c:otherwise>								
							</c:choose>
							</a>
				|&nbsp
				</c:forEach>
				</logic:notEqual>
			</td>
			</tr>
			

	</logic:notEqual>
	<tr><td height="15">&nbsp;</td></tr>
	<logic:equal name="report" property="totalUniqueRows" value="0">
		<tr>
			<td>
			<digi:trn key="rep:pop:filteredreportnodata">The report has no data to display using the current filter criteria. Kindly select different filter criteria by clicking the button labelled "Change Filters" at the top of this page, or choose to view another report.</digi:trn>
			</td>
		</tr>
	</logic:equal>
	<tr>
		<td>&nbsp;
			
		</td>
	</tr>
	<tr>
		<td>&nbsp;
			
		</td>
	</tr>
	<tr>
		<td>&nbsp;
			
		</td>
	</tr>
	<tr>
		<td>&nbsp;
			
		</td>
	</tr>

</table>
</div>

