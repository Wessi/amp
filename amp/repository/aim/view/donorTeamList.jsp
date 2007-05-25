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

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<digi:instance property="aimDonorTeamsForm" />

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
						</digi:link>
						&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here view Team Workspace Setup</digi:trn>
						</bean:define>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="<%=translation%>" >
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;						
						<digi:trn key="aim:donorActivityList">Donor Activity List</digi:trn>
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
												<table border="0" cellPadding=0 cellSpacing=0 width=167>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=150>
															<digi:trn key="aim:donorActivityList">Donor Activity List</digi:trn>
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
														<td bgColor=#dddddb colspan="2" align="center">
															<b><digi:trn key="aim:donorTeam">Donor Team</digi:trn></b>
														</td>
														<td bgColor=#dddddb colspan="2" align="center">
															<b><digi:trn key="aim:donorTeamLeader">Team Leader</digi:trn></b>
														</td>														
													</tr>
													<c:if test="${aimDonorTeamsForm.donorTeams != null}">
													<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
														<c:forEach var="donorTeams" items="${aimDonorTeamsForm.donorTeams}">	
															<tr bgColor=#f4f4f2>
																<td colspan="2" align="left">
																	<c:set target="${urlParams}" property="dnrTeamId">
																		<c:out value="${donorTeams.teamId}" />
																	</c:set>
																	<c:set target="${urlParams}" property="teamId">
																		<c:out value="${aimDonorTeamsForm.teamId}" />
																	</c:set>
																	<c:set target="${urlParams}" property="type" value="A"/>
																	<digi:link href="/getDonorActivityList.do" name="urlParams">
																	<c:out value="${donorTeams.teamName}" /></digi:link>
																</td>
																<td colspan="2" align="left">
																	<c:out value="${donorTeams.teamMemberName}" />
																</td>
															</tr>
														</c:forEach>
													</c:if>
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
