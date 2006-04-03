<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>

<script type="text/javascript">
	function confirmDelete() {
		var flag = confirm('Delete this indicator value?');
		return flag;			  
	}
</script>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>

<digi:instance property="aimUpdateIndicatorValuesForm" />

<digi:form action="/saveMEIndicatorValues.do">

<html:hidden property="indicatorId" />
<html:hidden property="indicatorValId" />
<input type="hidden" name="event" value="save">

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
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here view Team Workspace Setup</digi:trn>
						</bean:define>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="<%=translation%>" >
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;						
						<digi:trn key="aim:activityList">Activity List</digi:trn>
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
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="98%">	
										<tr><td>
											<digi:errors />
										</td></tr>
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width=237>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=220>
															<digi:trn key="aim:monitoringAndEvaluation">&nbsp;
																Monitoring & Evaluation	
															</digi:trn>
														</td>
														<td background="module/aim/images/corner-r.gif" height="17" width=17>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border valign="top">
												<table border=0 cellPadding=0 cellSpacing=1 class=box-border-nopadding width="100%">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"
															bgcolor="#ffffff">
																<tr><td valign="center" align="center" bgcolor="#dddddd" height="20">
																	<b><digi:trn key="aim:indicators">Indicators</digi:trn></b>
																</td></tr>
															</table>
														</td>
													</tr>
													<logic:empty name="aimUpdateIndicatorValuesForm" property="indicators">
													<tr>
														<td align="center">
															<table width="100%" cellSpacing=2 cellPadding=3 vAlign="top" align="center"
															bgcolor="#f4f4f2">
																<tr><td bgcolor="#f4f4f2" align="center">
																	<digi:trn key="aim:noIndicatorsPresent">
																		No indicators present
																	</digi:trn>
																</td></tr>
															</table>														
														</td>
													</tr>	
													</logic:empty>
							
													<logic:notEmpty name="aimUpdateIndicatorValuesForm" property="indicators">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellSpacing=1 cellPadding=4 vAlign="top" align="left" bgcolor="#f4f4f2">
															<logic:iterate name="aimUpdateIndicatorValuesForm" property="indicators" id="indicator" 
															type="org.digijava.module.aim.helper.ActivityIndicator">
																<tr bgcolor="#f4f4f2">
																<td width="12" valign="center">
																	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																	<c:if test="${aimUpdateIndicatorValuesForm.expIndicatorId==indicator.indicatorId}">
																		<digi:link href="/collapseIndicator.do">
																		<img src= "../ampTemplate/images/arrow_down.gif" border=0>
																		</digi:link>
																	</c:if>
																	<c:if test="${aimUpdateIndicatorValuesForm.expIndicatorId!=indicator.indicatorId}">
																		<c:set target="${urlParams}" property="indValId">
																			<bean:write name="indicator" property="indicatorValId" />
																		</c:set>
																		<digi:link href="/expandIndicator.do" name="urlParams">
																			<img src= "../ampTemplate/images/arrow_right.gif" border=0>
																		</digi:link>																
																	</c:if>																	
																</td>
																<td valign="top">
																	<b><bean:write name="indicator" property="indicatorName" /> - 
																	<bean:write name="indicator" property="indicatorCode" /></b>
																</td>
																<td valign="top" width="12">
																	<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlParams1}" property="event" value="delete" />
																	<c:set target="${urlParams1}" property="indicatorValId">
																		<bean:write name="indicator" property="indicatorValId" />
																	</c:set>
																	<digi:link href="/deleteIndicatorValue.do" name="urlParams1" 
																	onclick="return confirmDelete()">
																		<img src= "../ampTemplate/images/trash_12.gif" border=0>
																	</digi:link>
																</td></tr>																
																<c:if test="${aimUpdateIndicatorValuesForm.expIndicatorId==indicator.indicatorId}">
																<tr>
																	<td width="12">&nbsp;</td>
																	<td>
																		<table cellspacing="0" cellpadding="3" valign="top" align="left"
																		border=0>
																			<tr>
																				<td>Base Value</td>
																				<td><input type="text" name="baseVal" 
																				value="<bean:write name="indicator" property="baseVal" />"
																				class="inp-text" size="10"></td>
																				<td>Date</td>
																				<td><input type="text" name="baseValDate" 
																				value="<bean:write name="indicator" property="baseValDate" />"
																				class="inp-text" size="10" readonly="true"
																				id="baseValDate"></td>
																				<td align="left" vAlign="center">
																					<a href="javascript:calendar('baseValDate')">
																						<img src="../ampTemplate/images/show-calendar.gif" 
																						alt="Click to View Calendar" border=0>
																					</a>
																				</td>
																			</tr>
																			<tr>
																				<td>Target Value</td>
																				<td><input type="text" name="targetVal" 
																				value="<bean:write name="indicator" property="targetVal" />"
																				class="inp-text" size="10"></td>
																				<td>Date</td>
																				<td><input type="text" name="targetValDate" 
																				value="<bean:write name="indicator" property="targetValDate" />"
																				class="inp-text" size="10" readonly="true"
																				id="targetValDate"></td>
																				<td align="left" vAlign="center">
																					<a href="javascript:calendar('targetValDate')">
																						<img src="../ampTemplate/images/show-calendar.gif" 
																						alt="Click to View Calendar" border=0>
																					</a>
																				</td>
																			</tr>
																			<tr>
																				<td>Revised target Value</td>
																				<td><input type="text" name="revTargetVal" 
																				value="<bean:write name="indicator" property="revTargetVal" />"
																				class="inp-text" size="10"></td>
																				<td>Date</td>
																				<td><input type="text" name="revTargetValDate" 
																				value="<bean:write name="indicator" property="revTargetValDate" />"
																				class="inp-text" size="10" readonly="true"
																				id="revTargetValDate"></td>
																				<td align="left" vAlign="center">
																					<a href="javascript:calendar('revTargetValDate')">
																						<img src="../ampTemplate/images/show-calendar.gif" 
																						alt="Click to View Calendar" border=0>
																					</a>
																				</td>																				
																			</tr>			
																			<tr>
																				<td colspan="3"><input type="submit" value="Save Values" class="buton"></td>
																			</tr>
																		</table>
																	</td>
																	<td width="12">&nbsp;</td>
																</tr>
																</c:if>
															</logic:iterate>
															</table>
														</td>
													</tr>													
													</logic:notEmpty>
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
