
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script langauage="JavaScript">
	function onDelete() {
		var flag = confirm("Delete this Scheme ?");
		return flag;
	}
</script>

<digi:instance property="aimAddSectorForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
<html:hidden property="event" value="view"/>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:sectorManager">
						Sector Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>Sector Manager</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<html:errors />
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">

									<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn key="aim:schemes">
												Schemes
											</digi:trn>
											<!-- end table title -->
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#d7eafd">
													<logic:empty name="aimAddSectorForm" property="formSectorSchemes">
													<tr bgcolor="#ffffff">
														<td colspan="5" align="center"><b>
															<digi:trn key="aim:noSchemes">
															No Schemes present
															</digi:trn>
														</b></td>
													</tr>
													</logic:empty>
													<logic:notEmpty name="aimAddSectorForm" property="formSectorSchemes">
													<logic:iterate name="aimAddSectorForm" property="formSectorSchemes" id="sectorScheme"
																	type="org.digijava.module.aim.dbentity.AmpSectorScheme	">
													<tr>
														<td bgcolor="#ffffff">
															<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams2}" property="ampSecSchemeId">
															<bean:write name="sectorScheme" property="ampSecSchemeId" />
															</c:set>
															<c:set target="${urlParams2}" property="event" value="edit" />
															<c:set target="${urlParams2}" property="dest" value="admin" />
															<bean:define id="translation">
																<digi:trn key="aim:clickToViewSchemes">Click here to view Schemes</digi:trn>
															</bean:define>
															<digi:link href="/updateSectorSchemes.do" name="urlParams2" title="<%=translation%>" >
															<bean:write name="sectorScheme" property="secSchemeName"/></digi:link>
														</td>

														<td bgcolor="#ffffff" width="40" align="center">
															<bean:define id="translation">
																<digi:trn key="aim:clickToEditScheme">Click here to Edit Scheme</digi:trn>
															</bean:define>
															[ <digi:link href="/updateSectorSchemes.do" name="urlParams2" title="<%=translation%>" >
															Edit	
															</digi:link> 
															]
														</td>

														<%--<logic:equal name="aimAddSectorForm" property="deleteSchemeFlag" value="true">--%>
														<td bgcolor="#ffffff" width="55" align="center">
															<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams4}" property="ampSecSchemeId">
																<bean:write name="sectorScheme" property="ampSecSchemeId" />
															</c:set>
															<c:set target="${urlParams4}" property="event" value="deleteScheme"/>
															<bean:define id="translation">
																<digi:trn key="aim:clickToDeleteScheme">Click here to Delete Scheme</digi:trn>
															</bean:define>
															[ <digi:link href="/updateSectorSchemes.do" name="urlParams4"
																title="<%=translation%>" onclick="return onDelete()">
																Delete
															</digi:link>
															] 
														

														<%--<logic:equal name="aimAddSectorForm" property="deleteSchemeFlag" value="false">
															<td colspan="2" align="center">
																<b><digi:trn key="aim:cannotDeleteSectorMsg2">
																	Cannot Delete the sector since activity exist
																</digi:trn></b>
															</td>

														</logic:equal>--%>
													</tr>
													</logic:iterate>



													</logic:notEmpty>
													<!-- end page logic -->
											</table>
										</td></tr>
									</table>

								</td>
							</tr>
						</table>
					</td>

					<td noWrap width=100% vAlign="top">
						<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>
							<tr>
								<td>
									<!-- Other Links -->
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
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToAddScheme">Click here to Add a Scheme</digi:trn>
												</bean:define>
												<digi:link href="/updateSectorSchemes.do?dest=admin&event=addscheme" title="<%=translation%>" >
												<digi:trn key="aim:addScheme">
												Add Scheme
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
										<!-- end of other links -->
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

