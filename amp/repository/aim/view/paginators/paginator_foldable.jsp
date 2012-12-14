<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<div class="paging">
	<c:if test="${(report.visibleRows / recordsPerPage > 1) && (recordsPerPage ne max_value)}">
		<c:if test="${report.startRow != 0}">
			<!-- Go to FIRST PAGE -->
			<c:choose>
				<c:when test="${param.queryEngine!='true' }">
					<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=0~endRow=<c:out value="${recordsPerPage-1}"/>');">	
		           		&lt;&lt;
		           	</a>
		       		&nbsp;|&nbsp;
					<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow-recordsPerPage}"/>~endRow=<c:out value="${report.startRow-1}"/>');">	
						<digi:trn key="aim:previous">Previous</digi:trn>
					</a>
					&nbsp;|&nbsp;
				</c:when>
				<c:otherwise>
					<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=0~endRow=${recordsPerPage-1}~queryEngine=true');">	
						&lt;&lt;
					</a>
					&nbsp;|&nbsp;
					<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${report.startRow-recordsPerPage}~endRow=${report.startRow-1}~queryEngine=true');">	
						<digi:trn key="aim:previous">Previous</digi:trn>
					</a>
					&nbsp;|&nbsp;
				</c:otherwise>
			</c:choose>
		</c:if>
		<c:forEach var="i" begin="${startPageRow}" end="${endPageRow}" step="${recordsPerPage}">
			<c:choose>
				<c:when test="${param.queryEngine!='true' }">
		           	<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage-1}"/>');">
				</c:when>
				<c:otherwise>
					<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${i}~endRow=${i+recordsPerPage-1}~queryEngine=true');">	
				</c:otherwise>
			</c:choose>	
			<b ${i == report.startRow ? 'class="paging_sel"' : ''}>
				<fmt:formatNumber value="${(i)/recordsPerPage + 1}" maxFractionDigits="0"/>
			</b>										
			</a>  
			&nbsp;|&nbsp;
		</c:forEach>

		<c:if test="${(report.startRow+recordsPerPage+1) <= report.visibleRows}">
			<c:choose>
				<c:when test="${param.queryEngine!='true' }">
					<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow+recordsPerPage}"/>~endRow=<c:out value="${report.startRow+(recordsPerPage*2)-1}"/>');">	
						<digi:trn key="aim:next">Next</digi:trn>
					</a>
					&nbsp;|&nbsp;
					<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${((lastPage-1)*recordsPerPage)}"/>~endRow=<c:out value="${(lastPage*recordsPerPage)}"/>');">	
						&gt;&gt;
					</a>
				</c:when>
				<c:otherwise>
					<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${report.startRow+recordsPerPage}~endRow=${report.startRow+(recordsPerPage*2)-1}~queryEngine=true');">	
						<digi:trn key="aim:next">Next</digi:trn>
					</a>
					&nbsp;|&nbsp;
					<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${((lastPage-1)*recordsPerPage)}~endRow=${(lastPage*recordsPerPage)}~queryEngine=true');">	
				       	&gt;&gt;
					</a>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:if>
</div>
