<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript">
<!--

function addChildWorkspaces() {
		if (document.aimUpdateWorkspaceForm.workspaceType.value != "Team") { 
			openNewWindow(650, 380);
			<digi:context name="addChild" property="context/module/moduleinstance/addChildWorkspaces.do" />
			document.aimUpdateWorkspaceForm.action = "<%=addChild%>?dest=teamLead";
			document.aimUpdateWorkspaceForm.target = popupPointer.name;
			document.aimUpdateWorkspaceForm.submit();
		} else {
			alert("Workspace type must be 'Management' to add teams");
			return false;
		}
}

function removeChildWorkspace(id) {
	<digi:context name="update" property="context/module/moduleinstance/removeChildWorkspace.do" />
	document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=teamLead&tId="+id;
	document.aimUpdateWorkspaceForm.target = "_self";
	document.aimUpdateWorkspaceForm.submit();
}

function update(action) {
	var id = document.aimUpdateWorkspaceForm.teamId.value;
	<digi:context name="update" property="context/module/moduleinstance/updateWorkspaceForTeam.do" />
	document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=teamLead&event="+action+"&tId="+id;
	document.aimUpdateWorkspaceForm.target = "_self";
	document.aimUpdateWorkspaceForm.submit();
}

-->
</script>

<digi:instance property="aimUpdateWorkspaceForm" />
<digi:form action="/updateWorkspaceForTeam.do" method="post">

<html:hidden property="teamId" />
<html:hidden property="actionEvent" />
<html:hidden property="id" />
<html:hidden property="mainAction" />

<table cellSpacing=0 cellPadding=0 vAlign="top" align="left" width="100%">
<tr><td width="100%">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td>
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
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:teamWorkspaceSetup">
						Team Workspace Setup
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
							<digi:trn key="aim:teamWorkspaceSetup">
								Team Workspace Setup 
							</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%" 
						valign="top" align="left">
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
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%">	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width=117>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=100>
															<digi:trn key="aim:teamSettings">
															Team Settings
															</digi:trn>
														</td>
														<td background="module/aim/images/corner-r.gif" height="17" width=17>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border=0 cellPadding=3 cellSpacing=3 class=box-border-nopadding width="100%">
													<tr bgColor=#dddddb>
														<td bgColor=#dddddb colspan="2" align="center"><b>
															<digi:trn key="aim:teamGeneralSettings">
															General Settings	
															</digi:trn></b>
														</td>
													</tr>
													<logic:equal name="aimUpdateWorkspaceForm" property="updateFlag" value="true">
													<tr>
														<td colspan="2" align="center">
															<font color="blue"><b>
															<digi:trn key="aim:updateToAMPComplete">
																Update to AMP Complete
															</digi:trn></b></font>
														</td>
													</tr>
													</logic:equal>
													<tr>
														<td colspan="2" align="center">
															<digi:errors/>
														</td>
													</tr>													
													<tr bgcolor="#f4f4f2">
														<td align="right" width="30%">
															<digi:trn key="aim:teamName">
															Team Name
															</digi:trn>
														</td>
														<td align="left">
															<html:text property="teamName" size="50" styleClass="inp-text" />
														</td>
													</tr>
													<tr bgcolor="#f4f4f2">
														<td align="right" width="30%">
															<digi:trn key="aim:teamDescription">
															Team Description
															</digi:trn>
														</td>
														<td align="left">
															<html:textarea property="description" rows="3" cols="50" styleClass="inp-text"/>
														</td>
													</tr>							
													<tr bgcolor="#f4f4f2">
														<td align="right" width="30%">
															<digi:trn key="aim:teamCategory">Team Category</digi:trn>			
														</td>
														<td align="left">
															<html:select property="type" styleClass="inp-text">
																<html:option value="">-- Select Category --</html:option>
																<html:option value="Bilateral">Bilateral</html:option>
																<html:option value="Multilateral">Multilateral</html:option>
															</html:select>
														</td>
													</tr>
													<tr bgcolor="#f4f4f2">
														<td align="right">
															<digi:trn key="aim:workspaceType">Workspace Type</digi:trn>		
														</td>
														<td align="left">
															<html:select property="workspaceType" styleClass="inp-text">
																<html:option value="">-- Select Type --</html:option>
																<html:option value="Management">Management</html:option>
																<html:option value="Team">Team</html:option>
															</html:select>
														</td>
													</tr>													
													<tr bgcolor="#f4f4f2">
														<td align="right" width="150" bgcolor="#f4f4f2">
															<digi:trn key="aim:childWorkspaces">Child Workspaces</digi:trn>		
														</td>
														<td align="left" bgcolor="#f4f4f2">
															<input type="button" value="Add" class="buton" onclick="addChildWorkspaces()">
														</td>																
													</tr>
													<c:if test="${!empty aimUpdateWorkspaceForm.childWorkspaces}">
													<tr>
														<td colspan="2" align="center" bgcolor="#f4f4f2">
															<table width="98%" cellPadding=2 cellSpacing=0 valign="top" align="center" 
															class="box-border-nopadding">
															<c:forEach var="workspaces" items="${aimUpdateWorkspaceForm.childWorkspaces}">
																<tr>
																	<td align="left">&nbsp;
																		<c:out value="${workspaces.name}"/>
																	</td>
																	<td align="right" width="10">
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
																		<a href="javascript:removeChildWorkspace(<c:out value="${workspaces.ampTeamId}"/>)">
																	 	<digi:img src="module/cms/images/deleteIcon.gif" 
																		border="0" alt="Remove this child workspace"/></a>&nbsp;
																		</c:if>
																	</td>																	
																</tr>															
															</c:forEach>
															</table>
														</td>
													</tr>
													</c:if>	
													<c:if test="${empty aimUpdateWorkspaceForm.childWorkspaces}">
													<tr>
														<td colspan="2" align="center" bgcolor="#f4f4f2">
															<table width="98%" cellPadding=2 cellSpacing=0 valign="top" align="center" 
															class="box-border-nopadding">
															<tr>
																<td align="left">
																	<digi:trn key="aim:noChildTeams">No child teams</digi:trn>
																</td>
															</tr>
															</table>
														</td>
													</tr>
													</c:if>													
													<tr><td>&nbsp;</td></tr>
													<tr>
														<td colspan="2" align="center">
															<html:submit styleClass="dr-menu" value=" Update " onclick="update('edit')"/>
														</td>
													</tr>							
												</table>
											</td>
										</tr>
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
