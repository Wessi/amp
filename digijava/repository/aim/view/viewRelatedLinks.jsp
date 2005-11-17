<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>			

<digi:instance property="aimRelatedLinksForm" />
<digi:form action="/relatedLinksList.do" method="post">
<digi:context name="digiContext" property="context" />

<table width="100%" cellSpacing=0 cellPadding=0 valign="top" align="left">
<tr><td width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</bean:define>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
							<digi:trn key="aim:portfolio">Portfolio</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here to view Team Workspace Setup</digi:trn>
						</bean:define>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="<%=translation%>" >
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;						
						<digi:trn key="aim:relatedLinksList">
						Related Links List
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>Team Workspace Setup</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#3754a1>
								<td vAlign="top" width="100%">
									<jsp:include page="teamSetupMenu.jsp" flush="true" />
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td>&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>	
										<tr><td>
											<digi:errors />
										</td></tr>
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width=167>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=150>
															<digi:trn key="aim:relatedLinksList">
																Related Links List
															</digi:trn>
														</td>
														<td background="module/aim/images/corner-r.gif" height="17" width=17>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										
										<tr>
											<td bgColor=#ffffff class="box-border" valign="top">

											<table width="100%" cellPadding=0 cellSpacing=4 vAlign="top" align="left">
											<tr><td bgColor=#ffffff valign="top">

												<table border=0 cellPadding=4 cellSpacing=1 class="box-border-nopadding" width="100%">
													<tr bgColor=#dddddb>
														<td valign="center" align="center" width="50%">
															<b>
															<digi:trn key="aim:documents">
															Documents
															</digi:trn>
															</b>
														</td>
														<td bgColor=#dddddb align="center">
															<b>
															<digi:trn key="aim:activityName">
																Activity
															</digi:trn>
															</b>
														</td>
													</tr>

													<%--
													<logic:empty name="aimRelatedLinksForm" property="relatedLinks">
													<tr bgColor=#f4f4f2>
														<td colspan=2>
															<digi:trn key="aim:noDocsPresent">
															No documents present
															</digi:trn>
														</td>
													</tr>	
													</logic:empty>
													--%>
							
													<logic:notEmpty name="aimRelatedLinksForm" property="relatedLinks">
													<logic:iterate name="aimRelatedLinksForm" property="relatedLinks" id="relatedLink" 
													type="org.digijava.module.aim.helper.Documents">

													<c:if test="${relatedLink.isFile == true}">
													<tr>
														<td bgColor=#f4f4f2 width="50%">	
															<jsp:useBean id="docParams" type="java.util.Map" class="java.util.HashMap"/>			
															<c:set target="${docParams}" property="docId">	
																<c:out value="${relatedLink.docId}"/>
															</c:set>											
															<c:set target="${docParams}" property="actId">
																<c:out value="${relatedLink.activityId}"/>
															</c:set>																							
															<c:set target="${docParams}" property="pageId" value="1"/>				
															<bean:define id="translation">
																<digi:trn key="aim:clickToViewDocumentDetails">Click here to view Document Details</digi:trn>
															</bean:define>
															<digi:link href="/getDocumentDetails.do" name="docParams" title="<%=translation%>" >
															<bean:write name="relatedLink" property="title" /></digi:link>
															&nbsp; :
															<c:if test="${relatedLink.isFile == true}">
															<logic:notEmpty name="relatedLink" property="fileName">
															<bean:define name="relatedLink" property="fileName" id="fileName"/>
													    	<%
																int index2;
																String extension = null;
																index2 = ((String)fileName).lastIndexOf(".");	
																if( index2 >= 0 ) {
																   extension = "module/cms/images/extensions/" + 
																	((String)fileName).substring(
																				index2 + 1,((String)fileName).length()) + ".gif";
																}
														    %>															
													    	<digi:img skipBody="true" src="<%=extension%>" border="0" 
															 align="absmiddle"/>																 
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
														<td bgColor=#f4f4f2>
																<bean:write name="relatedLink" property="activityName" />
														</td>
													</tr>
													</c:if>
													
													</logic:iterate>
													</logic:notEmpty>
												</table>
											
											</td></tr>
											<tr><td bgColor=#ffffff valign="top">

												<table border=0 cellPadding=4 cellSpacing=1 class=box-border-nopadding width="100%">
													<tr bgColor=#dddddb>
														<td valign="center" align="center" width="50%">
															<b>
															<digi:trn key="aim:links">
															Links
															</digi:trn>
															</b>
														</td>
														<td bgColor=#dddddb align="center">
															<b>
															<digi:trn key="aim:activityName">
																Activity
															</digi:trn>
															</b>
														</td>
													</tr>

													<%--
													<logic:empty name="aimRelatedLinksForm" property="relatedLinks">
													<tr bgColor=#f4f4f2>
														<td colspan=2>
															<digi:trn key="aim:noLinksPresent">
															No links present
															</digi:trn>
														</td>
													</tr>	
													</logic:empty>
													--%>
							
													<logic:notEmpty name="aimRelatedLinksForm" property="relatedLinks">
													<logic:iterate name="aimRelatedLinksForm" property="relatedLinks" id="relatedLink" 
													type="org.digijava.module.aim.helper.Documents">

													<c:if test="${relatedLink.isFile == false}">
													<tr>
														<td bgColor=#f4f4f2 width="50%">	
															<c:set target="${docParams}" property="docId">	
																<c:out value="${relatedLink.docId}"/>
															</c:set>											
															<c:set target="${docParams}" property="actId">
																<c:out value="${relatedLink.activityId}"/>
															</c:set>																							
															<c:set target="${docParams}" property="pageId" value="1"/>				
															<bean:define id="translation">
																<digi:trn key="aim:clickToViewDocumentDetails">Click here to view Document Details</digi:trn>
															</bean:define>
															<digi:link href="/getDocumentDetails.do" name="docParams" title="<%=translation%>" >
															<bean:write name="relatedLink" property="title" /></digi:link>
															&nbsp; :
															<c:if test="${relatedLink.isFile == true}">
															<bean:define name="relatedLink" property="fileName" id="fileName"/>
													    	<%
																int index2;
																String extension = null;
																index2 = ((String)fileName).lastIndexOf(".");	
																if( index2 >= 0 ) {
																   extension = "module/cms/images/extensions/" + 
																	((String)fileName).substring(
																				index2 + 1,((String)fileName).length()) + ".gif";
																}
														    %>
														    	<digi:img skipBody="true" src="<%=extension%>" border="0" 
																 align="absmiddle"/>	
																<a href="<%=digiContext%>/cms/downloadFile.do?itemId=<bean:write name="relatedLink" property="docId" />">
																<i><bean:write name="relatedLink" property="fileName" /></i></a>
															</c:if>
															<c:if test="${relatedLink.isFile == false}">
																<digi:img src="module/aim/images/web-page.gif"/>
																<a target="_blank" href="<bean:write name="relatedLink" property="url" />">
																<i><bean:write name="relatedLink" property="url" /></i></a>
															</c:if>
														</td>
														<td bgColor=#f4f4f2>
																<bean:write name="relatedLink" property="activityName" />
														</td>
													</tr>
													</c:if>
													</logic:iterate>
													</logic:notEmpty>
												</table>											
											
											</td></tr>											
											</table>

											</td>
										</tr>										

										<%--
										<logic:notEmpty name="aimRelatedLinksForm" property="pages">
											<tr>
												<td>
													<digi:trn key="aim:relLinksPages">
														Pages
													</digi:trn> :
													<logic:iterate name="aimRelatedLinksForm" property="pages" id="pages" 
													type="java.lang.Integer">
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page">
															<%=pages%>
														</c:set>
														<c:set target="${urlParams1}" property="reset" value="false" />
																			    
														<c:if test="${aimRelatedLinksForm.currentPage == pages}">
															<%=pages%>
														</c:if>
														<c:if test="${aimRelatedLinksForm.currentPage != pages}">
															<bean:define id="translation">
																<digi:trn key="aim:clickToViewPages">Click here to view Pages</digi:trn>
															</bean:define>
															<digi:link href="/relatedLinksList.do" name="urlParams1" title="<%=translation%>" >
																<%=pages%>
															</digi:link>
														</c:if>

														|&nbsp; 
													</logic:iterate>
												</td>
											</tr>
										</logic:notEmpty>

										--%>
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>
								&nbsp;
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
