<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script langauage="JavaScript">
	function saveClicked() {
			  <digi:context name="preview" property="context/module/moduleinstance/GlobalSettings.do?action=save" />
	document.aimGlobalSettingsForm.action = "<%= preview %>";
	document.aimGlobalSettingsForm.target = "_self";	
	document.aimGlobalSettingsForm.submit();
	
	}
</script>

<digi:instance property="aimGlobalSettingsForm" />

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
						<digi:trn key="aim:globalSettingsManager">
						Global Settings Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>Global Settings Manager</span>
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

									<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left>
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn key="aim:systemSettings">
												System Settings
											</digi:trn>
											<!-- end table title -->
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left bgcolor="#d7eafd">
	<tr>
	<td bgcolor="#fefefe">
	Setting Name
	</td>												
	<td bgcolor="#fefefe">
	Current Value
	</td>												
	<td bgcolor="#fefefe">
	New Value
	</td>												

	
												<logic:notEmpty name="aimGlobalSettingsForm" property="gsfCol">
													<logic:iterate name="aimGlobalSettingsForm" property="gsfCol" id="globalSett"
																	type="org.digijava.module.aim.dbentity.AmpGlobalSettings	">
															<tr>
																<td bgcolor="#ffffff">
																			<bean:write name="globalSett" property="globalSettingsName"/>
																</td>
																<td bgcolor="#ffffff">
																			<bean:write name="globalSett" property="globalSettingsValue"/>
																</td>
																			
																<td bgcolor="#f4f4f2" >
<digi:form action="/GlobalSettings.do" method="post" >												
<html:hidden property="globalId" name="globalSett"/>
<html:hidden property="globalSettingsName" name="globalSett"/>
																<logic:equal name="globalSett" property="globalSettingsName" value="Default Country">
																	<html:select property="gsfValue" styleClass="inp-text">
																		<html:optionsCollection name="aimGlobalSettingsForm"
																			property="countryNameCol" value="iso"
																			label="countryName" />
																	</html:select>
																</logic:equal>		
																<logic:equal name="globalSett" property="globalSettingsName" value="Public View">
																	<html:select property="gsfValue" styleClass="inp-text">
																		<html:option value="On">On</html:option>
																		<html:option value="Off">Off</html:option>																		
																	</html:select>
																</logic:equal>		
																<logic:equal name="globalSett" property="globalSettingsName" value="Public View Budget Filter">
																	<html:select property="gsfValue" styleClass="inp-text">
																		<html:option value="On Budget">On Budget</html:option>
																		<html:option value="Off Budget">Off Budget</html:option>																		
																		<html:option value="All">All Activities</html:option>																																				
																	</html:select>
																</logic:equal>		
																
																<html:submit property="save">																														
																Save
																</html:submit>
</digi:form>																
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
				</tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

