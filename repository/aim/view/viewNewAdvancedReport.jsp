<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>


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

		<jsp:include page="/repository/aim/view/ar/levelSorterPicker.jsp" />
		<br/>
		<a href='#' onclick='closeMessage();return false'><b><digi:trn key="rep:pop:Close">Close</digi:trn></b></a>
</div>
 -->
<!--
<div style='position:relative;display:none;' id="filterPicker-<bean:write name="reportMeta" property="ampReportId"/>">
		<jsp:include page="/aim/reportsFilterPicker.do" />
		<br/>
		<a href='#' onclick='closeMessage();return false'><b><digi:trn key="rep:pop:Close">Close</digi:trn></b></a>

</div>
 -->



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
		<td><digi:trn key="rep:pop:Description">Description:</digi:trn><i><bean:write scope="session" name="reportMeta"
			property="reportDescription" /></i></td>
	</tr>

</logic:notEqual>


	<tr>
		<td>
		<div id="menucontainer">
			<logic:notEmpty name="reportMeta" property="hierarchies">
				<a style="cursor:pointer"
					onClick="showSorter(); ">
				<u><digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn></u> </a>&nbsp;
			</logic:notEmpty>
			<a style="cursor:pointer"
				onClick="showFilter(); ">
			<u><digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn></u> </a>
		</div>
		</td>
	</tr>
	<tr>
			<td align="right">
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
		</tr>
<logic:notEmpty name="reportMeta" property="hierarchies">
		<tr>
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
	<tr> <td>&nbsp;</td></tr>
	</logic:notEmpty>

	<tr>
	<td width="500">
	<digi:trn key="rep:pop:SelectedFilters">Currently Selected Filters:</digi:trn>
		<logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" scope="session">
		<bean:define id="listable" name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" toScope="request"/>
		<bean:define id="listableStyle" value="list" toScope="request"/>
		<bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
			<jsp:include page="${listable.jspFile}"/>
		</logic:present>
	</td>
	</tr>
	<tr>



	<logic:notEqual name="report" property="totalUniqueRows" value="0">
		<tr>
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
				&nbsp;
			</td>
		</tr>
		<logic:notEmpty name="totalPages" scope="request">
			<tr>
			<td>
				<digi:trn key="aim:pages">Pages :</digi:trn>&nbsp;
				<bean:define id="totalPages" name="totalPages" type="java.lang.Integer" scope="request" toScope="page"/>
				<c:forEach var="i" begin="0" end="${totalPages - 1}">
					<c:if  test="${i eq pageNumber}">
						<font color="#FF0000"><c:out value="${i + 1}" /></font>
					</c:if>
					<c:if  test="${i ne pageNumber}">
							<a  style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~pageNumber=<c:out value="${i}" />');">
								<c:out value="${i + 1}" />
							</a>
					</c:if>
					<c:if  test="${i < totalPages - 1}">
						|&nbsp
					</c:if>
				</c:forEach>
			</td>
			</tr>
		</logic:notEmpty>

	</logic:notEqual>
	<logic:equal name="report" property="totalUniqueRows" value="0">
		<tr>
			<td><b>
			<digi:trn key="rep:pop:filteredreport">The specified filtered report does not hold any data. Either
			pick a different filter criteria or use another report.	</digi:trn>
			</b></td>
		</tr>
	</logic:equal>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>

</table>
</div>

