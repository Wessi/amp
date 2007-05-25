<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<script langauage="JavaScript">
	function onDelete() {
		var flag = confirm("Are You Sure?");
		return flag;
	}

function onCancel() {
			<digi:context name="cancelComponents" property="context/module/moduleinstance/updateComponents.do?event=cancel" />
			document.aimUpdateComponentsForm.action = "<%= cancelComponents%>";
			document.aimUpdateComponentsForm.target = "_self";
			document.aimUpdateComponentsForm.submit();
	}


	function updateComponents(id) {
			  
			  
			 if(isEmpty(document.aimUpdateComponentsForm.compTitle.value)==true)
			 {
						alert("please enter a Component Title:");
			 }	
			 else if(isEmpty(document.aimUpdateComponentsForm.compCode.value)==true)
			 {
						alert("please enter a Component code:");
			 }	
			 else
			 {
			<digi:context name="updateComponents" property="context/module/moduleinstance/updateComponents.do?event=newComp" />
			document.aimUpdateComponentsForm.action = "<%= updateComponents%>&componentId="+id;
			document.aimUpdateComponentsForm.target = "_self";
			document.aimUpdateComponentsForm.submit();
			 }
	
	}
	
</script>

<digi:instance property="aimUpdateComponentsForm" />
<digi:form action="/updateComponents.do" method="post">

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

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
							<digi:trn key="aim:clickToViewComponentManager">Click here to view Component Manager</digi:trn>
						</bean:define>
						<digi:link href="/getComponents.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:componentManager">
						Component Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:addComponent">Add Component</digi:trn>	
					</td>
					<!-- End navigation -->
				</tr>
				<tr>

					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:addNewComponent">
						Add A New Component
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
<digi:errors/>					<table width="100%" cellspacing=1 cellSpacing=1>
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
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>	
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#dddddb height="20" 			align="center" colspan="5"><B>
																<digi:trn key="aim:addComponent">Add Component</digi:trn>	
															<!-- end header -->
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">	
																<table width="100%" border=0	 bgColor=#f4f4f2>
																	<tr>
																		
																		<td width="30%" align="right"><font color=red>*</font>
																		<digi:trn key="aim:compTitle">Component Title</digi:trn>	
																		</td>
																	    <td width="30%" >
																	          <html:textarea property="compTitle" cols="40" rows="2" />
																	    </td>
																	</tr>
																	<tr>
																		<td width="30%" align="right"><font color=red>*</font>
																	        <digi:trn key="aim:compCode">Component Code</digi:trn>
																		</td>
																	    <td width="30%">
																           <html:text property="compCode" size="10" />
																		</td>
																	</tr>
																		<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:compType">Component Type</digi:trn>	
																		</td>
																		<td width="30%">
																           <html:text property="compType" size="10" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:compDesc">Component Description</digi:trn>	
																		</td>
																		<td width="30%">
																           <html:textarea property="compDes" cols="40" rows="3" />
																		</td>
																	</tr>
	
															</tr>	
															<td width=30% align = right>
																			<font color=red>* Mandatory fields</font>
																		</td>
														  <tr>
															<td colspan="2" width="60%">
			<table width="100%" cellspacing="5">
				<tr>
					<td width="50%" align="right">

							<input  type="button" name="addBtn" value="Save" onclick="updateComponents('<bean:write name="aimUpdateComponentsForm" property="id" />')"/>
					</td>
					<td width="50%" align="left">
						<%--<html:reset value="Cancel" styleClass="dr-menu" 	onclick="javascript:history.go(-1)"/>--%>
						<html:reset value="Cancel" styleClass="dr-menu" 	onclick="onCancel()"/>
							<%--<input  type="button" name="addBtn" value="Cancel" onclick="onCancel()"/>--%>
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
</digi:form>














