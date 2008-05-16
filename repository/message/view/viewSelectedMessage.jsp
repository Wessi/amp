<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>

<digi:instance property="messageForm" />
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script langauage="JavaScript">
//	function edit(id) {	
//		window.opener.location.href='${contextPath}/message/messageActions.do?actionType=fillTypesAndLevels&editingMessage=true&msgStateId='+id;
//		window.close();	
//	}
	
//	function deleteAlert (id) {
//		messageForm.action="${contextPath}/message/messageActions.do?editingMessage=false&actionType=removeSelectedMessage&msgStateId="+id;
// 		messageForm.target = "_self";
//  		messageForm.submit();
//  		window.opener.location.reload();
//  		window.close();  					
//	}
	
	function closeWindow() {
		window.opener.location.reload();
		window.close();	
	}

</script>

<digi:form action="/messageActions.do">
			<table width="100%" cellspacing="0" cellpadding="0" border="0" bgcolor="#ffffff" align="center" valign="top">
				<tr>
					<td class="r-dotted-lg" width="10"/>
					<td class="r-dotted-lg" valign="top" align="left">
						<table width="98%" cellspacing="3" cellpadding="1" align="left" valign="top">
						<td/>
						<td>
							<table width="100%" cellspacing="5" cellpadding="3" border="0" valign="top">
								<tr>
									<td width="75%" valign="top">
										<table width="100%" cellspacing="0" cellpadding="0" border="0">
											<tr>
												<td width="100%">
													<table width="100%" cellspacing="0" cellpadding="0" border="0">
														<tr>
															<td width="13" height="20" background="module/aim/images/left-side.gif"/>
															<td class="textalb" valign="center" height="20" bgcolor="#006699" align="center">
																${messageForm.messageName}
															</td>
															<td width="13" height="20" background="module/aim/images/right-side.gif"/>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td width="100%" bgcolor="#f4f4f2">
													<table width="100%" cellspacing="1" cellpadding="3" bgcolor="#006699" align="left" valign="top">
														<tr>
															<td valign="top" bgcolor="#f4f4f2" align="center">
																<table width="95%" border="0" bgcolor="#f4f4f2">
																	<tr>
																		<td/>
																	</tr>
																	<tr>
																		<td>
																			<table width="100%" cellspacing="1" cellpadding="5" bgcolor="#eeeeee">
																				<tr><td></td></tr>
																				<tr>
																					<td colspan="3" align="right">priority</td>
																					<td align="left" bgcolor="#ffffff">
																						<logic:equal name="messageForm" property="priorityLevel" value="-1">None</logic:equal>
																						<logic:equal name="messageForm" property="priorityLevel" value="1">low</logic:equal>
																						<logic:equal name="messageForm" property="priorityLevel" value="2">medium</logic:equal>
																						<logic:equal name="messageForm" property="priorityLevel" value="3">Critical</logic:equal>
																						
																					</td>
																					<td></td>
																				</tr>
																				<tr><td></td></tr>
																				<tr>
																					<td align="right"><b><digi:trn key="message:from">From</digi:trn></b></td>
																					<td align="left" bgcolor="#ffffff" >${messageForm.sender}</td>
																					<td colspan="3"></td>
																				</tr>
																				<tr><td></td></tr>		
																				<tr>
																					<td align="right"><b><digi:trn key="message:received">Received</digi:trn></b></td>
																					<td align="left" bgcolor="#ffffff">${messageForm.creationDate}</td>
																				</tr>
																				<tr><td></td></tr>	
																				<tr>																					
																					<td align="center" colspan="4"><textarea rows="3" cols="45" readonly="readonly">${messageForm.description}</textarea></td>																					
																				</tr>
																				<c:if test="${not empty messageForm.forwardedMsg}">
																			    	<tr>
																			    		<td></td>																			    		
																			    		<td >
																			    			<table width="85%" align="center" border="0" style="border:1px solid; border-color: #484846;">
																								<tr><td align="center" nowrap="nowrap"><font color="red">**********forwarded Message:*********</font></td></tr>
																								<tr>
																									<td width="10%"><digi:trn key="message:from">From</digi:trn></td>
																									<td>&nbsp;${messageForm.forwardedMsg.from}</td>
																								</tr>
																								<tr>
																									<td><digi:trn key="message:receiver">Received</digi:trn></td>
																									<td>&nbsp;${messageForm.forwardedMsg.creationDate}</td>
																								</tr>
																								<tr>
																									<td><digi:trn key="message:subject">Subject</digi:trn> </td>
																									<td>${messageForm.forwardedMsg.name}																						
																									</td>
																								</tr>
																								<tr>
																									<td>To:</td>
																									<td>
																										<c:forEach var="receiver" items="${messageForm.forwardedMsg.receivers}"> ${receiver} ,&nbsp;</c:forEach>
																									</td>
																								</tr>
																								<tr>
																									<td><digi:trn key="message:msgDetails">Message Details</digi:trn> </td>
																									<td>&nbsp;${messageForm.forwardedMsg.description}</td>
																								</tr>
																							</table>
																			    		</td>																        	
													                            	</tr>
													                            </c:if>																																							
																				<tr>
																					<td colspan="4" align="center">
																						<!-- <c:if test="${messageForm.className=='m'}">
																							<c:set var="trnEdittBtn">
																								<digi:trn key="aim:btn:edit">edit</digi:trn>
																							</c:set> 
																							<input type="button" value="${trnEdittBtn }" onclick="edit(${messageForm.msgStateId});" />
																							<c:set var="trnDeletetBtn"><digi:trn key="aim:btn:delete">delete</digi:trn>	</c:set> 
																						<input type="button" value="${trnDeletetBtn }" onclick="deleteAlert(${messageForm.msgStateId});" />
																						</c:if>  --> 																						
																						<c:set var="trnCloseBtn"><digi:trn key="aim:btn:close">Close</digi:trn>	</c:set>
																						<input type="button" value="${trnCloseBtn }" onclick="closeWindow()" />
																					</td>
																				</tr>
																				
																			</table>
																		</td>
																	</tr>
																	<tr>
																		<td/>
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
						</td>
						<td/>
						</table>
					</td>
					<td width="10"/>
				</tr>
			</table>
</digi:form>