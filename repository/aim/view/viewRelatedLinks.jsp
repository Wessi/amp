<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>
<style>

.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}

</style>
<script language="javascript">
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}
</script>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>

<digi:instance property="aimRelatedLinksForm" />
<digi:form action="/relatedLinksList.do" method="post">
<digi:context name="digiContext" property="context" />
<logic:notPresent parameter="subtab"> 
	<bean:define id="subtabId" value="0"/>
</logic:notPresent>
<logic:present parameter="subtab">
	<bean:parameter id="subtabId" name="subtab" />
</logic:present>

<table width="100%" cellSpacing=0 cellPadding=0 valign="top" align="left">
<tr><td width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780>
	<tr>
		<td  width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:portfolio">Portfolio</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here to view Team Workspace Setup</digi:trn>
						</c:set>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;
						<c:choose>
							<c:when test="${subtabId == 0 }">
								<digi:trn key="aim:relatedDocumentsList">
								Related Documents List
								</digi:trn>
							</c:when>
							<c:otherwise>
								<digi:trn key="aim:relatedLinksList">
								Related Links List
								</digi:trn>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="4" scope="request"/>
									<c:set var="selectedSubTab" value="<%=request.getParameter("subtab") == null ? "0": request.getParameter("subtab") %>" scope="request"/>
									<jsp:include page="teamSetupMenu.jsp" flush="true" />
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
                                    <div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
	                                    <div align="center">
											<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="98%" border=0>
										<tr><td>
											<digi:errors />
										</td></tr>
										<tr>
											<td bgColor=#ffffff valign="top">

											<table width="100%" cellPadding=0 cellSpacing=4 vAlign="top" align="left">
											<tr><td bgColor=#ffffff valign="top">
												<c:if test="${subtabId == 0 }">
													<table border=0 cellPadding=4 cellSpacing=0 width="100%" id="dataTable">
														<tr>
															<td  bgColor=#999999 valign="center" align="center" width="30%" style="color:black;">
																<b>
																<digi:trn key="aim:doctitle">
																Title
																</digi:trn>
																</b>
															</td>
															<td  bgColor=#999999 valign="center" align="center" width="30%" style="color:black;">
																<b>
																<digi:trn key="fm:documentfilename">
																Filename
																</digi:trn>
																</b>
															</td>
															<td  bgColor=#999999 align="center" width="40%" style="color:black;">
																<b>
																<digi:trn key="aim:activityName">
																	Activity
																</digi:trn>
																</b>
															</td>
														</tr>
														<logic:notEmpty name="aimRelatedLinksForm" property="relatedLinks">
														<logic:iterate name="aimRelatedLinksForm" property="relatedLinks" id="relatedLink" indexId="idx"
														type="org.digijava.module.aim.helper.Documents">
	
	
														<c:if test="${relatedLink.isFile == true}">
														<tr>
															<td width="30%" valign="top">
	
																<input type="checkbox" name="deleteLinks" value='<bean:write name="idx"/>'>
	                                                                                                                        <jsp:useBean id="docParams" type="java.util.Map" class="java.util.HashMap"/>
	
																<c:set target="${docParams}" property="docId">
																	<c:out value="${relatedLink.docId}"/>
																</c:set>
																<c:set target="${docParams}" property="actId">
																	<c:out value="${relatedLink.activityId}"/>
																</c:set>
																<c:set target="${docParams}" property="pageId" value="1"/>
																<c:set var="translation">
																	<digi:trn key="aim:clickToViewDocumentDetails">Click here to view Document Details</digi:trn>
																</c:set>
																<digi:link href="/getDocumentDetails.do" name="docParams" title="${translation}" >
																<bean:write name="relatedLink" property="title" /></digi:link>
                                                             </td>
                                                             <td width="30%">
																<c:if test="${relatedLink.isFile == true}">
																<logic:notEmpty name="relatedLink" property="fileName">
																<bean:define name="relatedLink" property="fileName" id="fileName"/>
<!--
														    	<%
																	int index2;
																	String extension = "";
																	index2 = ((String)fileName).lastIndexOf(".");
																	if( index2 >= 0 ) {
																	   extension = "module/cms/images/extensions/" +
																		((String)fileName).substring(
																					index2 + 1,((String)fileName).length()) + ".gif";
																	}
															    %>
														    	<digi:img skipBody="true" src="<%=extension%>" border="0"
																 align="absmiddle"/>-->
																</logic:notEmpty>
																<a href="<%=digiContext%>/cms/downloadFile.do?itemId=<bean:write name="relatedLink" property="docId" />">
																	<i><bean:write name="relatedLink" property="fileName" /></i></a>
																</c:if>
																<c:if test="${relatedLink.isFile == false}">
																	<digi:img src="module/aim/images/web-page.gif"/>
																	<a target="_blank" href="<bean:write name="relatedLink" property="url" />">
																	<i><bean:write name="relatedLink" property="url" /></i></a>
																</c:if>
															</td>
															<td width="40%">
																	<bean:write name="relatedLink" property="activityName" />
															</td>
														</tr>
														</c:if>
	
														</logic:iterate>
														</logic:notEmpty>
													</table>
												</c:if>
											</td></tr>
											<tr><td bgColor=#ffffff valign="top">
												<c:if test="${subtabId == 1 }">
													<table border=0 cellPadding=4 cellSpacing=0 width="100%" id="dataTable" >
														<tr>
															<td bgColor=#999999 valign="center" align="center" width="30%" style="color:black">
																<b>
																<digi:trn key="aim:doctitle">
																Title
																</digi:trn>
																</b>
															</td>
															<td bgColor=#999999 valign="center" align="center" width="30%" style="color:black">
																<b>
																<digi:trn key="aim:links">
																Links
																</digi:trn>
																</b>
															</td>
															<td bgColor=#999999 align="center" width="30%" style="color:black">
																<b>
																<digi:trn key="aim:activityName">
																	Activity
																</digi:trn>
																</b>
															</td>
														</tr>
														<logic:notEmpty name="aimRelatedLinksForm" property="relatedLinks">
														<logic:iterate name="aimRelatedLinksForm" property="relatedLinks" id="relatedLink" indexId="idx"
														type="org.digijava.module.aim.helper.Documents">
	
														<c:if test="${relatedLink.isFile == false}">
														<tr>
															<td>
	
																<input type="checkbox" name="deleteLinks" value='<bean:write name="idx"/>'>
	                                                                                                                        <jsp:useBean id="docPars" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${docPars}" property="docId">
																	<c:out value="${relatedLink.docId}"/>
																</c:set>
																<c:set target="${docPars}" property="actId">
																	<c:out value="${relatedLink.activityId}"/>
																</c:set>
																<c:set target="${docPars}" property="pageId" value="1"/>
																<c:set var="translation">
																	<digi:trn key="aim:clickToViewDocumentDetails">Click here to view Document Details</digi:trn>
																</c:set>
																<digi:link href="/getDocumentDetails.do" name="docPars" title="${translation}" >
																<bean:write name="relatedLink" property="title" /></digi:link>
															</td>
															<td>
																<c:if test="${relatedLink.isFile == false}">
																	<a target="_blank" href="<bean:write name="relatedLink" property="url" />">
																	<i><bean:write name="relatedLink" property="url" /></i></a>
																</c:if>
															</td>
															<td>
																	<bean:write name="relatedLink" property="activityName" />
															</td>
														</tr>
														</c:if>
														</logic:iterate>
														</logic:notEmpty>
													</table>
												</c:if>

											</td></tr>

												<tr>

													<td>&nbsp;

														

													</td>

												</tr>
												<tr>

													<td align="center">

														<html:submit styleClass="buton" property="removeFields"><digi:trn key="aim:addEditActivityDeleteSelected">Delete Selected</digi:trn></html:submit>

													</td>

												</tr>
											</table>

											</td>
										</tr>

									</table>
	                                    </div>
                                    </div>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>&nbsp;
								
							</td></tr>
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



<script language="javascript">
setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", false);
</script>

