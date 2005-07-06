<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript">
<!--

function validate() {
	if (trim(document.aimAddThemeForm.themeName.value).length == 0) {
		alert("Program name not entered");
		document.aimAddThemeForm.themeName.focus;
		return false;
	} else {
		return true;
	}
}

function cancel() {
	<digi:context name="cancel" property="context/module/moduleinstance/themeManager.do" />
	document.aimAddThemeForm.action = "<%= cancel %>";
	document.aimAddThemeForm.target = "_self"
	document.aimAddThemeForm.submit();	
}

-->
</script>


<digi:instance property="aimAddThemeForm" />
<digi:form action="/addTheme.do" method="post">

<table width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left">
<tr><td>
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
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
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewThemeManager">Click here to view Theme Manager</digi:trn>
						</bean:define>
						<digi:link href="/themeManager.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:programManager">	
						Program Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:addProgram">
						Add Program
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:programManager">	
						Program Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=0 cellSpacing=0>
					<tr>
						<td noWrap width=600 vAlign="top">
							<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">
										&nbsp;
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="97%" border=0>	
											<tr>
												<td>
													<digi:errors/>
												</td>
											</tr>
											<tr>
												<td bgColor=#ffffff>
													<table border=0 cellPadding=0 cellSpacing=0 width="100%" bgcolor="#dddddd">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#dddddb height="20"	align="center" colspan="5"><B>
																<digi:trn key="aim:addProgram">
																Add Program
															</digi:trn>
															<!-- end header -->
														</tr>
														<!-- Page Logic -->
														<tr bgcolor="#dddddd">
															<td width="100%">	
																<table width="100%" border=0 vAlign="top" align="left"
																cellPadding=4 cellSpacing=1 bgcolor="#dddddd">
																	<tr>
																		<td align="right" bgcolor="#f4f4f2">
																		<FONT color=red>*</FONT>
																		<digi:trn key="aim:programName">Program Name</digi:trn>	</td>
																	    <td bgcolor="#f4f4f2">
																	          <html:text property="themeName" size="50" styleClass="inp-text"/>
																	    </td>
																	</tr>
																	<tr>
																		<td align="right" bgcolor="#f4f4f2">
																	        <digi:trn key="aim:programCode">Program Code</digi:trn>	
																		</td>
																	    <td bgcolor="#f4f4f2">
																           <html:text property="themeCode" size="10" styleClass="inp-text"/>
																		</td>
																	</tr>
																	<tr>
																		<td align="right" bgcolor="#f4f4f2">
																	        <digi:trn key="aim:description">Description</digi:trn>	
																		</td>
																		<td bgcolor="#f4f4f2">
																           <html:textarea property="description" rows="3" cols="65" styleClass="inp-text"/>
																		</td>
																	</tr>
																	<tr>
																		<td align="right" bgcolor="#f4f4f2">
																	        <digi:trn key="aim:themeType">Type
																			</digi:trn>		
																		</td>
																		<td bgcolor="#f4f4f2">
																	          <html:text property="type" size="20"styleClass="inp-text"/>
																		</td>
																	</tr>
																	<tr>
																		<td align="left" bgcolor="#f4f4f2" colspan="2">
																			<digi:trn key="aim:allMarkedRequiredField">
																			All fields marked with an <FONT color=red><B><BIG>*</BIG>
																			</B></FONT> are required.</digi:trn>
																		</td>
																	</tr>																	
																	<tr>
																		<td colspan="2" align="center" bgcolor="#ffffff" width="100%">
																			<table cellspacing="5" border=0>
																				<tr>
																					<td align="right" width="50">
																						<html:submit styleClass="dr-menu" value="Save" 
																						onclick="return validate()"/>
																					</td>
																					<td align="center" width="50">
																						<html:reset value="Reset" styleClass="dr-menu"/>
																					</td>
																					<td align="left" width="50">
																						<input type="button" value="Cancel" class="dr-menu" onclick="cancel()">
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</table>	
															</td>
														</tr>
													<!-- end page logic -->
													</table>
												</td>
											</tr>
											
										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#f4f4f2>
										&nbsp;
									</td>
								</tr>
							</table>
						</td>
						<td noWrap width=100% vAlign="top">
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
