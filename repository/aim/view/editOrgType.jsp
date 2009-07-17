<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

	function msg() {
          var msg="<digi:trn key="aim:deleteOrganizationType">Are you sure about deleting this Organization Type ?</digi:trn>"
		if (confirm(msg)) {
			document.aimAddOrgTypeForm.action.value = "delete";
			document.aimAddOrgTypeForm.submit();
		}
		else
			return false;
	}

	function move() {
		<digi:context name="selectLoc" property="context/module/moduleinstance/orgTypeManager.do" />
		url = "<%= selectLoc %>";
		document.location.href = url;
	}

	function check() {
		var str = document.aimAddOrgTypeForm.orgType.value;
		var code = trim(document.aimAddOrgTypeForm.orgTypeCode.value);
		str = trim(str);

		if (str == null || str.length == 0) {
			alert("Please enter name for this Type");
			document.aimAddOrgTypeForm.orgType.focus();
			return false;
		}
		else if (code == null || code.length == 0) {
			alert("Please enter code for this Type");
			document.aimAddOrgTypeForm.orgTypeCode.focus();
			return false;
		}
		else {
			document.aimAddOrgTypeForm.orgType.value = str;
			document.aimAddOrgTypeForm.submit();
		}
	}

	function trim ( inputStringTrim ) {
		fixedTrim = "";
		lastCh = " ";
		for (x=0; x < inputStringTrim.length; x++) {
			ch = inputStringTrim.charAt(x);
			if ((ch != " ") || (lastCh != " ")) { fixedTrim += ch; }
				lastCh = ch;
		}
		if (fixedTrim.charAt(fixedTrim.length - 1) == " ") {
			fixedTrim = fixedTrim.substring(0, fixedTrim.length - 1); }
		return fixedTrim
	}

</script>

<digi:instance property="aimAddOrgTypeForm" />
<digi:context name="digiContext" property="context"/>

<digi:form action="/editOrgType.do" method="post">
<html:hidden property="action" />
<html:hidden property="ampOrgTypeId" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=757>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>

						<digi:link href="/admin.do" styleClass="comment">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/orgTypeManager.do" styleClass="comment">
						<digi:trn key="aim:orgTypeManager">
						Oraganization Type Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimAddOrgTypeForm" property="action" value="create" >
							<digi:trn key="aim:addOrgType">Add Organization Type</digi:trn>
						</logic:equal>
						<logic:equal name="aimAddOrgTypeForm" property="action" value="edit" >
							<digi:trn key="aim:editOrgType">Edit Organization Type</digi:trn>
						</logic:equal>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:orgTypeManager">
						Oraganization Type Manager
						</digi:trn>
						</span>
						<br>
						<digi:errors/>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr>
						<td noWrap width=571 vAlign="top">
							<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">
										&nbsp;
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
														<tr bgColor=#dddddb>
															<td bgColor=#dddddb height="20" align="center" colspan="5">
																<logic:equal name="aimAddOrgTypeForm" property="action" value="create" >
																	<b><digi:trn key="aim:addOrgType">Add Organization Type</digi:trn></b>
																</logic:equal>
																<logic:equal name="aimAddOrgTypeForm" property="action" value="edit" >
																	<b><digi:trn key="aim:editOrgType">Edit Organization Type</digi:trn></b>
																</logic:equal>
															</td>
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">
																<table width="100%" cellPadding=3 cellSpacing=3 border=0 bgColor=#f4f4f2>
																	<tr>
																		<td width="30%" align="right">
																		<digi:trn key="aim:orgTypeName">Name</digi:trn>	</td>
																	    <td width="30%" >
																	          <html:text property="orgType" size="35" />
																	    </td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:orgTypeCode">Type
                                                                            Code</digi:trn>
																		</td>
																		<td width="30%">
																           <html:text property="orgTypeCode" size="15" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:orgTypeIsGovernmental">Is Governmental</digi:trn>
																		</td>
																		<td width="30%">
																           <html:checkbox property="orgTypeIsGovernmental"/>
																		</td>
																	</tr>
																	<tr>
																		<td colspan="2" width="60%"  align="center">
																			<table width="100%" cellspacing="5">
																				<tr>
																					<td width="45%" align="right">
																						<c:set var="translation">
																							<digi:trn key="aim:btnSave">Save</digi:trn>
																						</c:set>
																						<input type="button" value="${translation}" class="dr-menu" onclick="check()">
																					</td>
																					<td width="8%" align="left">
																						<c:set var="translation">
																							<digi:trn key="aim:btnReset">Reset</digi:trn>
																						</c:set>
																						<input type="reset" value="${translation}" class="dr-menu">
																					</td>
																					<td width="45%" align="left">
																						<c:set var="translation">
																							<digi:trn key="aim:btnCancel">Cancel</digi:trn>
																						</c:set>
																						<input type="button" value="${translation}" class="dr-menu" onclick="move()">
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<logic:equal name="aimAddOrgTypeForm" property="deleteFlag" value="delete" >
																		<tr>
																			<td colspan="2" width="60%"  align="center">
																				<c:set var="translation">
																					<digi:trn key="aim:btnDeleteThisType">Delete this Type</digi:trn>
																				</c:set>
																				<input type="button" value="${translation}" class="dr-menu" onclick="msg()">
																			</td>
																		</tr>
																	</logic:equal>
																	<logic:equal name="aimAddOrgTypeForm" property="deleteFlag" value="orgReferences" >
																		<tr>
																			<td colspan="2" width="60%"  align="center">
																				<b><digi:trn key="aim:cannotDeleteOrgTypeMsg"><font color="#FF0000">
																						Can not delete this Type as some organization/group references it!</font>
																					</digi:trn>
																				</b>
																			</td>
																		</tr>
																	</logic:equal>
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
						<!-- <td noWrap width=100% vAlign="top"> -->
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</td>
	</tr>
</table>
</digi:form>