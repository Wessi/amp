<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>


<script language="javascript">
	function resetFields() {
		var keyword = document.getElementsByName("keyword")[0];
		var queryType = document.getElementsByName("queryType")[0];
		keyword.value = "";
		queryType.value = -1;
		document.getElementById("resultTable").innerHTML = "";
		keyword.focus();
	}

	function popup(mylink, windowname) {
		if (!window.focus)
			return true;
		var href;
		if (typeof (mylink) == 'string')
			href = mylink;
		else
			href = mylink.href;
		window
				.open(
						href,
						windowname,
						'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
		return false;
	}
	function checkKeyWord(){
		var keyword = document.getElementsByName("keyword");
		if (keyword) {
			keyword = keyword[0];
			if (keyword.value.length < 3) {
				alert("<digi:trn keyWords="AMP Search">Please enter a search of more than 2 characters.</digi:trn>");
				return false;
			}

		}
		return true;
		
	}

</script>



<!-- MAIN CONTENT PART START -->
<!-- BREADCRUMP START -->
<div class="breadcrump">
<div class="centering">
<div class="breadcrump_cont">
<span class="sec_name"><digi:trn>AMP Search</digi:trn></span><span class="breadcrump_sep">|</span><a class="l_sm"><digi:trn>Tools</digi:trn></a><span class="breadcrump_sep"><b>»</b></span><span class="bread_sel"><digi:trn>AMP Search</digi:trn></span></div>
</div>
</div>
<!-- BREADCRUMP END -->
<table cellspacing="0" cellpadding="0" border="0" align="center"
	width="1000">
	<tr>
		<td valign="top">
		<fieldset><digi:form action="/search.do" onsubmit="return checkKeyWord()">
			<c:if test="${!empty searchform.keyword}">
				<div class="search_results_header">Search results for "<span
					class="green_text">${searchform.keyword}</span>"</div>
			</c:if>

			<div class="help_search txt_sm_b" style="padding-right: 150px"><digi:trn>Keyword</digi:trn>: <html:text
				property="keyword" styleClass="inputx insidex" size="25" /><digi:trn>Type</digi:trn>:
			<html:select property="queryType" styleClass="inputx insidex">
				<html:option value="-1">
					<digi:trn>ALL</digi:trn>
				</html:option>
				<html:option value="0">
					<digi:trn>Activities</digi:trn>
				</html:option>
				<html:option value="1">
					<digi:trn>Reports</digi:trn>
				</html:option>
				<html:option value="2">
					<digi:trn>Tabs</digi:trn>
				</html:option>
				<html:option value="3">
					<digi:trn>Resources</digi:trn>
				</html:option>
			</html:select> <html:submit styleClass="buttonx_sm">
				<digi:trn>Search</digi:trn>
			</html:submit></div>
		</digi:form>
		<table width="100%">
			<tbody>
				<tr>
					<td width="50%" valign="top"><c:choose>
						<c:when test="${empty resultList}">
							<c:if test="${param.reset != 'true'}">
    <div class="txt_sm_b"><digi:trn>Your search return no results. Please try another keyword.</digi:trn></div>
							</c:if>
						</c:when>
						<c:otherwise>
							<c:set var="search_results_block_class">
								<c:choose>
									<c:when test="${searchform.queryType==-1}">
										search_results_block
									</c:when>
									<c:otherwise>
									search_results_block_last
								</c:otherwise>
								</c:choose>

							</c:set>
							<c:set var="resultFound">
							<digi:trn>Results found in</digi:trn>
							</c:set>
							<div class="search_results">
							<c:if test="${searchform.queryType==-1||searchform.queryType==0}">
								<div class="${search_results_block_class}"><span
									class="button_green">${fn:length(resultActivities)}</span>
								 ${resultFound} <span class="button_green"><digi:trn>Activities</digi:trn></span>
								<ul>
									<c:forEach items="${resultActivities}" var="activity">
										<li><digi:link module="search"
											href="/search.do?ampActivityId=${activity.ampActivityId}">${activity.objectName}</digi:link>
									</c:forEach>
								</ul>
								</div>
								</c:if>
								<c:if test="${searchform.queryType==-1||searchform.queryType==2}">
							<div class="${search_results_block_class}"><span
								class="button_green">${fn:length(resultTabs)}</span> ${resultFound} <span class="button_green"><digi:trn>Tabs</digi:trn></span>
							<ul>
								<c:forEach items="${resultTabs}" var="tab">
									<li><a
										title="<digi:trn>Click here to view the tab</digi:trn>"
										href="/search/search.do?ampReportId=${tab.ampReportId}">${tab.objectName}</a>
								</c:forEach>
							</ul>
							</div>
							</c:if>
							<c:if test="${searchform.queryType==-1||searchform.queryType==1}">
							<div class="${search_results_block_class}"><span
								class="button_green">${fn:length(resultReports)}</span> ${resultFound} <span class="button_green"><digi:trn>Reports</digi:trn></span> 
							<ul>
								<c:forEach items="${resultReports}" var="report">
									<li><a
										title="<digi:trn>Click here to view the tab</digi:trn>"
										href="/search/search.do?ampReportId=${report.ampReportId}">${report.objectName}</a>
								</c:forEach>
							</ul>
							</div>
							</c:if>
							<c:if test="${searchform.queryType==-1||searchform.queryType==3}">
							<div class="search_results_block_last"><span
								class="button_green">${fn:length(resultResources)}</span>
							${resultFound} <span class="button_green"><digi:trn>Resources</digi:trn></span>
							<ul>
								<c:forEach items="${resultResources}" var="resource">
									<li><c:choose>
										<c:when test="${empty resource.webLink}">
											<a 
												onclick="window.location='/contentrepository/downloadFile.do?uuid=${resource.uuid}'"
												title="<digi:trn>Click here to download file</digi:trn>">
											${resource.name}</a>
										</c:when>
										<c:otherwise>
											<a href="${resource.webLink}"
												title="Click here to follow link" target="_blank">
											${resource.webLink}</a>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</ul>
							</div>
							</c:if>
							</div>
						</c:otherwise>
					</c:choose></td>
				</tr>
			</tbody>
		</table>
		</fieldset>
		</td>
	</tr>
</table>

<!-- MAIN CONTENT PART END -->

