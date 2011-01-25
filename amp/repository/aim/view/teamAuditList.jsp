<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="javascript">
function showUser(email){
	if (email != ""){
		<digi:context name="information" property="context/module/moduleinstance/userProfile.do" />
        var param = "~edit=true~email="+email;
		previewWorkspaceframe("${information}",param);
	}
	else{
		var trasnlation = '<digi:trn key="aim:userblankmail" jsFriendly="true">The user does not have a valid email address</digi:trn>'
		alert (trasnlation);
	}
}


</script>
<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>
<digi:instance property="aimTeamAuditListForm" />
<digi:form action="/teamAuditList.do" method="post">
<table width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left">
	<tr>
		<td width="100%" vAlign="top" align="left">
			<jsp:include page="teamPagesHeader.jsp" flush="true" />
		</td>
	</tr>
	<tr>
	<td width="100%" vAlign="top" align="left">
	<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>
		<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33>
						<div class="breadcrump_cont">
							<span class="sec_name">
								<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
							</span>
							
							<span class="breadcrump_sep">|</span>
							<digi:link href="/viewMyDesktop.do" title="${translation}" styleClass="l_sm">
								<digi:trn key="aim:portfolio">Portfolio</digi:trn>
							</digi:link>
							<span class="breadcrump_sep"><b>»</b></span>
							<c:set var="translation">
								<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
							</c:set>
							<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="l_sm" title="${translation}">
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></digi:link>
							<span class="breadcrump_sep"><b>»</b></span>
							<span class="bread_sel"><digi:trn key="aim:AuditTrialList">Audit Trial List</digi:trn></span>
						</div>						
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="7" scope="request"/>
									<c:set var="selectedSubTab" value="0" scope="request"/>
										
									<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
										<tr>
											<td valign=top>
												<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">	
										
									<jsp:include page="teamSetupMenu.jsp" flush="true" />
										

							<table class="inside" width=100% cellpadding="0" cellspacing="0">
								<tr>
							    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
							    	<b class="ins_title">
							    		<c:if test="${aimTeamAuditListForm.sortBy!='nameasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=nameasc" styleClass="l_sm">
													<digi:trn key="aim:name">Name</digi:trn>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='nameasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=namedesc" styleClass="l_sm">
													<digi:trn key="aim:name">Name</digi:trn>
												</digi:link>
							  			</c:if>
							  		</b>
							  	</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='typeasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=typeasc" styleClass="l_sm">
													<digi:trn key="aim:objectType">Object Type</digi:trn>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='typeasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=typedesc" styleClass="l_sm">
													<digi:trn key="aim:objectType">Object Type</digi:trn>
												</digi:link>
											</c:if>
										</b>
							    </td>
							    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
							    	<b class="ins_title">
								    	<c:if test="${aimTeamAuditListForm.sortBy!='teamasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=teamasc" styleClass="l_sm">
													<digi:trn key="aim:teamName">Team Name</digi:trn>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='teamasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=teamdesc" styleClass="l_sm">
													<digi:trn key="aim:teamName">Team Name</digi:trn>
												</digi:link>
											</c:if>
										</b>
									</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='authorasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=authorasc" styleClass="l_sm">
													<digi:trn key="aim:authorName">Author Name</digi:trn>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='authorasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=authordesc" styleClass="l_sm">
													<digi:trn key="aim:authorName">Author Name</digi:trn>
												</digi:link>
											</c:if>
										</b>
									</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='creationdateasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=creationdateasc" styleClass="l_sm">
													<digi:trn key="aim:creationDateLogger">Creation Date</digi:trn>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='creationdateasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=creationdatedesc" styleClass="l_sm">
													<digi:trn key="aim:creationDateLogger">Creation Date</digi:trn>
												</digi:link>
								  		</c:if>
							  		</b>
							  	</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='editorasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=editorasc" styleClass="l_sm">
													<digi:trn key="aim:editorName">Editor Name</digi:trn>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='editorasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=editordesc" styleClass="l_sm">
													<digi:trn key="aim:editorName">Editor Name</digi:trn>
												</digi:link>
											</c:if>
										</b>
									</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='changedateasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=changedateasc" styleClass="l_sm">
													<digi:trn key="aim:changeDate">Change Date</digi:trn>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='changedateasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=changedatedesc" styleClass="l_sm">
													<digi:trn key="aim:changeDate">Change Date</digi:trn>
												</digi:link>
											</c:if>
										</b>
									</td>
									<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
										<b class="ins_title">
											<c:if test="${aimTeamAuditListForm.sortBy!='actionasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=actionasc" styleClass="l_sm">
													<digi:trn key="aim:action">Action</digi:trn>
												</digi:link>
											</c:if>
											<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='actionasc'}">
												<digi:link style="color:black" href="/teamAuditList.do?sortBy=actiondesc" styleClass="l_sm">
													<digi:trn key="aim:action">Action</digi:trn>
												</digi:link>
											</c:if>
										</b>
									</td>
								</tr>
								
								
								<logic:iterate name="aimTeamAuditListForm" property="logs" id="log" type="org.digijava.module.aim.dbentity.AmpAuditLogger">
									<tr>
										<td width="280" class=inside title="${log.objectName}">
											<c:if test="${fn:length(log.objectName) > 30}" >
												<c:out value="${fn:substring(log.objectName, 0, 30)}" />...
											</c:if>
											<c:if test="${fn:length(log.objectName) < 30}" >
												<bean:write name="log" property="objectName"/>
											</c:if>
										</td>
										<td class=inside width="150" title="${log.objectTypeTrimmed}">
											<bean:write name="log" property="objectTypeTrimmed"/>									
										</td>
										<td class=inside width="100" title="${log.teamName}">
											<c:if test="${fn:length(log.objectName) > 10}" >
												<c:out value="${fn:substring(log.teamName, 0, 10)}" />...
											</c:if>
											<c:if test="${fn:length(log.teamName) < 10}" >
												<bean:write name="log" property="teamName"/>
											</c:if>									
										</td>
										<td class=inside width="150">
											<a href="javascript:showUser('<bean:write name="log" property="authorEmail"/>')" style="text-decoration: none" title="${log.authorName}" class="l_sm">
												<c:if test="${fn:length(log.objectName) > 8}" >
													<c:out value="${fn:substring(log.authorName, 0, 8)}" />...
												</c:if>
												<c:if test="${fn:length(log.objectName) < 8}" >
													<bean:write name="log" property="authorName"/>
												</c:if>
											</a>								  
										</td>
										<td width="100" class=inside title="${log.loggedDate}">
											<bean:write name="log" property="sloggeddate"/>
										</td>
										
										<td width="100" class=inside title="${log.editorName}">
											<bean:write name="log" property="editorName" />								  
										</td>
										<td width="150" class=inside title="${log.modifyDate}">
											<bean:write name="log" property="smodifydate"/>								  
										</td>
										<td width="100" class=inside>
											<logic:equal value="delete" property="action" name="log">
												<digi:trn key="admin:delete">Delete</digi:trn>
											</logic:equal>
											<logic:equal value="add" property="action" name="log">
												<digi:trn key="admin:add">Add</digi:trn>
											</logic:equal>
											<logic:equal value="update" property="action" name="log">
												<digi:trn key="admin:update">Update</digi:trn>
											</logic:equal>								  
										</td>
									</tr>
								</logic:iterate>
							</table>
							
							<!-- Pagination -->
<div class="paging" style="font-size:11px;">
<c:if test="${aimTeamAuditListForm.currentPage > 1}">
	<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
	<c:set target="${urlParamsFirst}" property="page" value="1"/>
	<c:set target="${urlParamsFirst}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
	<c:set var="translation">
		<digi:trn key="aim:firstpage">First Page</digi:trn>
	</c:set>
	<digi:link href="/teamAuditList.do" name="urlParamsFirst" title="${translation}" styleClass="l_sm">
		&lt;&lt;
	</digi:link>
	<jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
	<c:set target="${urlParamsPrevious}" property="page" value="${aimTeamAuditListForm.currentPage -1}"/>
	<c:set target="${urlParamsPrevious}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
	<c:set var="translation">
		<digi:trn key="aim:previouspage">Previous Page</digi:trn>
	</c:set>|
	<digi:link href="/teamAuditList.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" styleClass="l_sm">
		<digi:trn key="aim:previous">Previous</digi:trn>
	</digi:link>|
</c:if>


<c:set var="length" value="${aimTeamAuditListForm.pagesToShow}"></c:set>
<c:set var="start" value="${aimTeamAuditListForm.offset}"/>
<logic:iterate name="aimTeamAuditListForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="${length}">	
	<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
	<c:set target="${urlParams1}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
	<c:set target="${urlParams1}" property="page"><%=pages%></c:set>
	<c:if test="${aimTeamAuditListForm.currentPage == pages && aimTeamAuditListForm.pagesSize > 1}">
		<b class="paging_sel"><%=pages%></b>
		|						
	</c:if>
	<c:if test="${aimTeamAuditListForm.currentPage != pages && aimTeamAuditListForm.pagesSize > 1}">
		<c:set var="translation">
			<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
		</c:set>
		<digi:link href="/teamAuditList.do" name="urlParams1" title="${translation}" styleClass="l_sm">
		<%=pages%>							
		</digi:link>
		|
	</c:if>
</logic:iterate>
<c:if test="${aimTeamAuditListForm.currentPage != aimTeamAuditListForm.pagesSize && aimTeamAuditListForm.pagesSize != 0}">
	<jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap" />
	<c:set target="${urlParamsNext}" property="page" value="${aimTeamAuditListForm.currentPage+1}"/>
	<c:set target="${urlParamsNext}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
	<c:set var="translation"> 
	<digi:trn key="aim:nextpage">Next Page</digi:trn>
	</c:set>
	<digi:link  href="/teamAuditList.do" style="text-decoration=none" name="urlParamsNext" title="${translation}" styleClass="l_sm">
		<digi:trn key="aim:next">Next</digi:trn>
	</digi:link>
	<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap" />|
	
	<c:if test="${aimTeamAuditListForm.pagesSize > aimTeamAuditListForm.pagesToShow}">
		<c:set target="${urlParamsLast}" property="page" value="${aimTeamAuditListForm.pagesSize}" />
		<c:set target="${urlParamsLast}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
	</c:if>
	<c:if test="${aimTeamAuditListForm.pagesSize < aimTeamAuditListForm.pagesToShow}">
		<c:set target="${urlParamsLast}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
		<c:set target="${urlParamsLast}" property="page" value="${aimTeamAuditListForm.pagesSize}" />
		<c:set var="translation">
			<digi:trn>Last Page</digi:trn>
		</c:set>
		<digi:link href="/teamAuditList.do" style="text-decoration=none" name="urlParamsLast" title="${translation}" styleClass="l_sm">
			&gt;&gt;
		</digi:link>
	</c:if> 
</c:if>
<c:out value="${aimTeamAuditListForm.currentPage}"/>&nbsp; 
<digi:trn key="aim:of">
	of
</digi:trn>&nbsp;

<c:out value="${aimTeamAuditListForm.pagesSize}"/>

</div>
					<!-- end of Pagination -->
							


<%--
<table width="98%" cellPadding=0 cellSpacing=0 bgColor=#ffffff>
						<tr>
						<td colspan="2" valign="top">
						<div>
						<div align="center">
						<table width="100%" height="100%" border="0" align=center cellPadding=0 cellSpacing=0  id="dataTable">
							<tr>
								<td width="280" height="22" align="center" valign="center" bgcolor="#999999"
									style="color: black"><c:if 
									test="${aimTeamAuditListForm.sortBy!='nameasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=nameasc">
										<b><digi:trn key="aim:name">Name</digi:trn></b>									
									</digi:link>
								</c:if> <c:if
									test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='nameasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=namedesc">
										<b><digi:trn key="aim:name">Name</digi:trn></b>									
									</digi:link>
							  </c:if></td>
								<td valign="center" align="center" bgcolor="#999999" style="color: black" width="150">
								<c:if test="${aimTeamAuditListForm.sortBy!='typeasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=typeasc">
										<b><digi:trn key="aim:objectType">Object Type</digi:trn></b>									
									</digi:link>
								</c:if>
								<c:if test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='typeasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=typedesc">
										<b><digi:trn key="aim:objectType">Object Type</digi:trn></b>									
									</digi:link>
								</c:if>
								</td>
								<td valign="center" align="center" bgcolor="#999999"
									style="color: black"><c:if
									test="${aimTeamAuditListForm.sortBy!='teamasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=teamasc">
										<b><digi:trn key="aim:teamName">Team Name</digi:trn></b>									
									</digi:link>
								</c:if> <c:if
									test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='teamasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=teamdesc">
										<b><digi:trn key="aim:teamName">Team Name</digi:trn></b>									
									</digi:link>
								</c:if></td>
								<td align="center" valign="center" bgcolor="#999999"
									style="color: black"><c:if
									test="${aimTeamAuditListForm.sortBy!='authorasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=authorasc">
										<b><digi:trn key="aim:authorName">Author Name</digi:trn></b>									
									</digi:link>
								</c:if> <c:if
									test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='authorasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=authordesc">
										<b><digi:trn key="aim:authorName">Author Name</digi:trn></b>									
									</digi:link>
								</c:if></td>
								<td width="100" align="center" valign="center" bgcolor="#999999"
									style="color: black"><c:if
									test="${aimTeamAuditListForm.sortBy!='creationdateasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=creationdateasc">
										<b><digi:trn key="aim:creationDateLogger">Creation Date</digi:trn></b>									
									</digi:link>
								</c:if> <c:if
									test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='creationdateasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=creationdatedesc">
										<b><digi:trn key="aim:creationDateLogger">Creation Date</digi:trn></b>									
									</digi:link>
							  </c:if></td>
								<td width="208" align="center" valign="center" bgcolor="#999999"
									style="color: black"><c:if
									test="${aimTeamAuditListForm.sortBy!='editorasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=editorasc">
										<b><digi:trn key="aim:editorName">Editor Name</digi:trn></b>									
									</digi:link>
								</c:if> <c:if
									test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='editorasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=editordesc">
										<b><digi:trn key="aim:editorName">Editor Name</digi:trn></b>									
									</digi:link>
								</c:if></td>
								<td align="center" valign="center" bgcolor="#999999"
									style="color: black"><c:if
									test="${aimTeamAuditListForm.sortBy!='changedateasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=changedateasc">
										<b><digi:trn key="aim:changeDate">Change Date</digi:trn></b>									
									</digi:link>
								</c:if> <c:if
									test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='changedateasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=changedatedesc">
										<b><digi:trn key="aim:changeDate">Change Date</digi:trn></b>									
									</digi:link>
								</c:if></td>
								<td width="129" align="center" valign="center" bgcolor="#999999"style="color: black">
								<c:if test="${aimTeamAuditListForm.sortBy!='actionasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=actionasc">
										<b><digi:trn key="aim:action">Action</digi:trn></b>									
									</digi:link>
								</c:if> <c:if
									test="${not empty aimTeamAuditListForm.sortBy && aimTeamAuditListForm.sortBy=='actionasc'}">
									<digi:link style="color:black" href="/teamAuditList.do?sortBy=actiondesc">
										<b><digi:trn key="aim:action">Action</digi:trn></b>									
									</digi:link>
								</c:if></td>
							</tr>
							<logic:iterate name="aimTeamAuditListForm" property="logs"
								id="log" type="org.digijava.module.aim.dbentity.AmpAuditLogger">
								<tr>
								<td width="280" height="18" align="center" title="${log.objectName}">
								<c:if test="${fn:length(log.objectName) > 30}" >
									<c:out value="${fn:substring(log.objectName, 0, 30)}" />...
								</c:if>
								<c:if test="${fn:length(log.objectName) < 30}" >
									<bean:write name="log" property="objectName"/>
								</c:if>
								</td>
								<td align="center" width="150" title="${log.objectTypeTrimmed}">
									<bean:write name="log" property="objectTypeTrimmed"/>									
								</td>
								<td align="center" width="100" title="${log.teamName}">
									<c:if test="${fn:length(log.objectName) > 10}" >
										<c:out value="${fn:substring(log.teamName, 0, 10)}" />...
									</c:if>
									<c:if test="${fn:length(log.teamName) < 10}" >
										<bean:write name="log" property="teamName"/>
									</c:if>									
								</td>
								<td align="center" width="150">
									<a href="javascript:showUser('<bean:write name="log" property="authorEmail"/>')" style="text-decoration: none" title="${log.authorName}">
									<c:if test="${fn:length(log.objectName) > 8}" >
										<c:out value="${fn:substring(log.authorName, 0, 8)}" />...
									</c:if>
									<c:if test="${fn:length(log.objectName) < 8}" >
										 <bean:write name="log" property="authorName"/>
									</c:if>
									</a>								  
								</td>
								<td width="100" align="center" title="${log.loggedDate}">
									<bean:write name="log" property="sloggeddate"/>
								</td>
								
								<td width="100" align="center" title="${log.editorName}">
									<bean:write name="log" property="editorName" />								  
								</td>
								<td width="150" align="center" title="${log.modifyDate}">
									  <bean:write name="log" property="smodifydate"/>								  
								 </td>
									<td width="100" align="center">
										<logic:equal value="delete" property="action" name="log">
											<digi:trn key="admin:delete">Delete</digi:trn>
										</logic:equal> <logic:equal value="add" property="action" name="log">
											<digi:trn key="admin:add">Add</digi:trn>
										</logic:equal> <logic:equal value="update" property="action" name="log">
											<digi:trn key="admin:update">Update</digi:trn>
										</logic:equal>								  
									</td>
								</tr>
							</logic:iterate>
						</table>           	
						</div>
                        </div>                                 
						</td>
						</tr>
						<tr>
						<td align="left" valign="middle">
						<div style="cursor: pointer; font-family: Arial; text-align: left; text-decoration: none;">
						<br>
						<c:if test="${aimTeamAuditListForm.currentPage > 1}">
							<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
							<c:set target="${urlParamsFirst}" property="page" value="1"/>
							<c:set target="${urlParamsFirst}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
							<c:set var="translation">
								<digi:trn key="aim:firstpage">First Page</digi:trn>
							</c:set>
							<digi:link href="/teamAuditList.do" style="text-decoration=none" name="urlParamsFirst" title="${translation}"  >
								<span style="font-size: 8pt; font-family: Tahoma;">&lt;&lt;</span>							
							</digi:link>
							<jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
							<c:set target="${urlParamsPrevious}" property="page" value="${aimTeamAuditListForm.currentPage -1}"/>
							<c:set target="${urlParamsPrevious}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
							<c:set var="translation">
								<digi:trn key="aim:previouspage">Previous Page</digi:trn>
							</c:set>|
							<digi:link href="/teamAuditList.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >
								<digi:trn key="aim:previous">
									<span style="font-size: 8pt; font-family: Tahoma;">Previous</span>
								</digi:trn>&nbsp;	
							</digi:link>|
						</c:if>
					<c:set var="length" value="${aimTeamAuditListForm.pagesToShow}"></c:set>
					<c:set var="start" value="${aimTeamAuditListForm.offset}"/>
					<logic:iterate name="aimTeamAuditListForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="${length}">	
						<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams1}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
						<c:set target="${urlParams1}" property="page"><%=pages%></c:set>
						<c:if test="${aimTeamAuditListForm.currentPage == pages && aimTeamAuditListForm.pagesSize > 1}">
							<font color="#FF0000"><%=pages%></font>
							|						
						</c:if>
						<c:if test="${aimTeamAuditListForm.currentPage != pages && aimTeamAuditListForm.pagesSize > 1}">
							<c:set var="translation">
								<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
							</c:set>
							<digi:link href="/teamAuditList.do" name="urlParams1" title="${translation}" >
								<%=pages%>							
							</digi:link>
							|
						</c:if>
						</logic:iterate>
						<c:if test="${aimTeamAuditListForm.currentPage != aimTeamAuditListForm.pagesSize && aimTeamAuditListForm.pagesSize != 0}">
							<jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap" />
							<c:set target="${urlParamsNext}" property="page" value="${aimTeamAuditListForm.currentPage+1}"/>
							<c:set target="${urlParamsNext}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
							<c:set var="translation"> 
								<digi:trn key="aim:nextpage">Next Page</digi:trn>
							</c:set>
							<digi:link  href="/teamAuditList.do" style="text-decoration=none" name="urlParamsNext" title="${translation}">
								<digi:trn key="aim:next"><span style="font-size: 8pt; font-family: Tahoma;">Next</span></digi:trn>
							</digi:link>
							<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap" />|
							
						<c:if test="${aimTeamAuditListForm.pagesSize > aimTeamAuditListForm.pagesToShow}">
							<c:set target="${urlParamsLast}" property="page" value="${aimTeamAuditListForm.pagesSize}" />
							<c:set target="${urlParamsLast}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
						</c:if>
						<c:if test="${aimTeamAuditListForm.pagesSize < aimTeamAuditListForm.pagesToShow}">
							<c:set target="${urlParamsLast}" property="sortBy" value="${aimTeamAuditListForm.sortBy}" />
							<c:set target="${urlParamsLast}" property="page" value="${aimTeamAuditListForm.pagesSize}" />
							<c:set var="translation">
                                    <digi:trn>Last Page</digi:trn>
							</c:set>
							<digi:link href="/teamAuditList.do" style="text-decoration=none" name="urlParamsLast" title="${translation}">
								<span style="font-size: 8pt; font-family: Tahoma;">&gt;&gt;</span>
							</digi:link>
						</c:if> 
					</c:if>
					<c:out value="${aimTeamAuditListForm.currentPage}"/>&nbsp; 
					<digi:trn key="aim:of">
						<span style="font-size: 8pt; font-family: Tahoma;">of</span>
					</digi:trn>&nbsp;
					<span style="font-size: 8pt; font-family: Tahoma;">
						<c:out value="${aimTeamAuditListForm.pagesSize}"/>
					</span>					
						<a style="float: right; cursor: pointer;" onclick="window.scrollTo(0,0); return false">
							<digi:trn key="aim:backtotop">Back to Top</digi:trn>
					</span>
						<span style="font-size: 10pt; font-family: Tahoma;">↑</span>
						</a>					
					</td>
					</div>
				</tr>
				</table>
				</td>
			</tr>
--%>
										
										
										</div>
										</div>											
												
											</td>
										</tr>
									</table>	
										
										
								</td>
							</tr>
		</table>
	</td>
</tr>
</table>
</td></tr>
</table>

</digi:form>
