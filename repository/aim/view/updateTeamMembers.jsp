<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="aimTeamMemberForm" />
<digi:form action="/updateTeamMember.do" method="post">

<html:hidden property="teamId" />
<html:hidden property="teamMemberId" />
<html:hidden property="action" />
<html:hidden property="userId" />
<html:hidden property="name" />
<html:errors/>
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
						</bean:define>
						<digi:link href="/workspaceManager.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;

						<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams}" property="teamId">
						<bean:write name="aimTeamMemberForm" property="teamId" />
						</c:set>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewTeamMembers">Click here to view Team Members</digi:trn>
						</bean:define>
						<digi:link href="/teamMembers.do" name="urlParams" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:members">
						Members
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimTeamMemberForm" property="action" value="edit">
							<digi:trn key="aim:editTeamMembers">Edit Members</digi:trn>	
						</logic:equal>
						<logic:equal name="aimTeamMemberForm" property="action" value="delete">
							<digi:trn key="aim:deleteTeamMembers">Delete Members</digi:trn>	
						</logic:equal>						
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<bean:write name="aimTeamMemberForm" property="teamName" />
					</span></td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td vAlign="top" width="100%">
									&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="95%" border=0>	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width="100%">
													<tr bgColor=#f4f4f2 height="20">
														<td bgColor=#c9c9c7 class=box-title width=100 align="center">
																<logic:equal name="aimTeamMemberForm" property="action" value="edit">
																	<digi:trn key="aim:editTeamMembers">Edit Members</digi:trn>	
																</logic:equal>
																<logic:equal name="aimTeamMemberForm" property="action" value="delete">
																	<digi:trn key="aim:deleteTeamMembers">Delete Members</digi:trn>	
																</logic:equal>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border=0 cellPadding=5 cellSpacing=1 class=box-border width="100%">
	<tr>
		<td align="right" width="50%">
			<digi:trn key="aim:memberName">Name &nbsp;&nbsp; &nbsp;</digi:trn>
		</td>
		<td align="left" width="50%">
			<bean:write name="aimTeamMemberForm" property="name" />
		</td>
	</tr>
	<tr>
		<td align="right" width="50%">
			<digi:trn key="aim:memberRole">Role&nbsp;&nbsp; &nbsp;&nbsp;</digi:trn>	
		</td>
		<td align="left" width="50%">
			<html:select property="role">
				<html:option value="">------ Select role ------</html:option>
				<html:optionsCollection name="aimTeamMemberForm" property="ampRoles" value="ampTeamMemRoleId" label="role" />
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			<logic:equal name="aimTeamMemberForm" property="action" value="edit">
				<table cellspacing="5">
					<tr>
						<td>
							<html:checkbox property="readPerms">
								<digi:trn key="aim:readPerms">&nbsp;&nbsp; Read &nbsp;&nbsp;
								</digi:trn>
							</html:checkbox>					
						</td>
					</tr>
					<tr>
						<td>
							<html:checkbox property="writePerms">
								<digi:trn key="aim:writePerms">&nbsp;&nbsp; Add / Update &nbsp;&nbsp;
								</digi:trn>
							</html:checkbox>					
						</td>
					</tr>
					<tr>
						<td>
							<html:checkbox property="deletePerms">
								<digi:trn key="aim:deletePerms">&nbsp;&nbsp; Delete &nbsp;&nbsp;
								</digi:trn>
							</html:checkbox>					
						</td>
					</tr>
				</table>
			</logic:equal>
			<logic:equal name="aimTeamMemberForm" property="action" value="delete">
				<table cellspacing="5">
					<tr>
						<td>
							<html:checkbox property="readPerms">
								<digi:trn key="aim:readPerms">&nbsp;&nbsp; Read &nbsp;&nbsp;
								</digi:trn>
							</html:checkbox>					
						</td>
					</tr>
					<tr>
						<td>
							<html:checkbox property="writePerms">
								<digi:trn key="aim:writePerms">&nbsp;&nbsp; Add / Update &nbsp;&nbsp;
								</digi:trn>
							</html:checkbox>					
						</td>
					</tr>
					<tr>
						<td>
							<html:checkbox property="deletePerms">
								<digi:trn key="aim:deletePerms">&nbsp;&nbsp; Delete &nbsp;&nbsp;
								</digi:trn>
							</html:checkbox>					
						</td>
					</tr>
				</table>
			</logic:equal>
		</td>
	</tr>
	<tr>
		<td colspan="2" width="60%">
			<logic:equal name="aimTeamMemberForm" property="action" value="delete">
			<table width="100%" cellspacing="5">
				<tr>
					<td width="50%" align="right">
						<html:submit value="Delete" styleClass="dr-menu"/>
					</td>
					<td width="50%" align="left">
						<html:reset value="Cancel" onclick="javascript:history.go(-1)" styleClass="dr-menu" />
					</td>
				</tr>
			</table>
			</logic:equal>
			<logic:equal name="aimTeamMemberForm" property="action" value="edit">
			<table width="100%" cellspacing="5">
				<tr>
					<td width="50%" align="right">
						<html:submit value="Save" styleClass="dr-menu" />
					</td>
					<td width="50%" align="left">
						<html:reset value="Cancel" onclick="javascript:history.go(-1)" styleClass="dr-menu" />
					</td>
				</tr>
			</table>
			</logic:equal>
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
					<td noWrap width=100% vAlign="top">
						<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>	
							<tr>
								<td>
									<table cellPadding=0 cellSpacing=0 width=100>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
								
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
												</bean:define>
												<digi:link href="/workspaceManager.do" title="<%=translation%>" >
												<digi:trn key="aim:workspaceManager">
													Workspace Manager
												</digi:trn>
												</digi:link>
											</td>
										</tr>

										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToAddTeams">Click here to Add Teams</digi:trn>
												</bean:define>
												<digi:link href="/updateWorkspace.do" title="<%=translation%>" >
												<digi:trn key="aim:addTeam">
												Add Teams
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewRoles">Click here to view Roles</digi:trn>
												</bean:define>
												<digi:link href="/roles.do" title="<%=translation%>" >
												<digi:trn key="aim:roles">
												Roles
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToAddRoles">Click here to Add Roles</digi:trn>
												</bean:define>
												<digi:link href="/updateRole.do" title="<%=translation%>" >
												<digi:trn key="aim:addRole">
												Add Roles
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</bean:define>
												<digi:link href="/admin.do" title="<%=translation%>" >
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
												</digi:link>
											</td>
										</tr>
									</table>

								</td>
							</tr>
						</table>
					</td></tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>

