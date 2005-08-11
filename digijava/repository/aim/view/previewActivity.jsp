<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimEditActivityForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

<!-- 
function backClicked() {
	document.aimEditActivityForm.step.value = "7";
	<digi:context name="backStep" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= backStep %>";
	document.aimEditActivityForm.target = "_self";		
	document.aimEditActivityForm.submit();
}

function disable() {
	document.aimEditActivityForm.submitButton.disabled = true;
	document.aimEditActivityForm.backButton.disabled = true;
	document.aimEditActivityForm.submit();
	return true;
}
-->

</script>

<digi:form action="/saveActivity.do" method="post">
<html:hidden property="step" />
<html:hidden property="editAct" />

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td width="100%" vAlign="top" align="left">

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="780" vAlign="top" align="left" border=0>
	<tr>
		<td class=r-dotted-lg width="10" align="left" vAlign="top">&nbsp;</td>
		<td class=r-dotted-lg align=left vAlign=top>
			<table width="100%" cellSpacing="3" cellPadding="1" vAlign="top" align="left" border=0>
					<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
								<c:if test="${aimEditActivityForm.pageId == 0}">
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
									</bean:define>
									<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>">
										<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.pageId == 1}">								
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
									</bean:define>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;								
								</c:if>	

								<c:if test="${aimEditActivityForm.pageId == 2}">
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
									</bean:define>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;	
									
									<jsp:useBean id="urlParam" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParam}" property="ampActivityId">
										<c:out value="${aimEditActivityForm.activityId}"/>
									</c:set>
									<c:set target="${urlParam}" property="tabIndex" value="0"/>
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewProjectDetails">Click here to view Project Details</digi:trn>
									</bean:define>
									<digi:link href="/viewChannelOverview.do" styleClass="comment" name="urlParam" title="<%=translation%>" >
										<digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;	
								</c:if>									
								
								<c:if test="${aimEditActivityForm.pageId != 2}">
								
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewAddActivityStep1">Click here to goto Add Activity Step 1</digi:trn>
								</bean:define>
								<digi:link href="/addActivity.do?step=1&edit=true" styleClass="comment" title="<%=translation%>" >
								
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivityStep1">
										Edit Activity - Step 1
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn key="aim:addActivityStep1">
										Add Activity - Step 1
									</digi:trn>
								</c:if>																
								</digi:link>&nbsp;&gt;&nbsp;						
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep2">Click here to goto Add Activity Step 2</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment" title="<%=translation%>" >						
									<digi:trn key="aim:addActivityStep2">
									Step 2
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep3">Click here to goto Add Activity Step 3</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=3&edit=true" styleClass="comment" title="<%=translation%>" >						
									<digi:trn key="aim:addActivityStep3">
									Step 3
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;							
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep4">Click here to goto Add Activity Step 4</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=4&edit=true" styleClass="comment" title="<%=translation%>" >						
									<digi:trn key="aim:addActivityStep4">
									Step 4
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep5">Click here to goto Add Activity Step 5</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=5&edit=true" styleClass="comment" title="<%=translation%>" >												
									<digi:trn key="aim:addActivityStep5">
									Step 5
									</digi:trn>						
									</digi:link>&nbsp;&gt;&nbsp;	
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep6">Click here to goto Add Activity Step 6</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=6&edit=true" styleClass="comment" title="<%=translation%>" >						
									<digi:trn key="aim:addActivityStep6">
									Step 6
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep7">Click here to goto Add Activity Step 7</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=7&edit=true" styleClass="comment" title="<%=translation%>" >
									<digi:trn key="aim:addActivityStep7">
									Step 7
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								<digi:trn key="aim:previewActivity">
								Preview Activity
								</digi:trn>							
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="0" cellPadding="0" vAlign="bottom">
						<tr>
							<td width="50%" align="left"> 
								<c:if test="${aimEditActivityForm.pageId != 2}">
								<table width="100%" cellSpacing="1" cellPadding="1" vAlign="bottom">
									<tr>
										<td height=16 vAlign="bottom" width="100%"><span class=subtitle-blue>
											<c:if test="${aimEditActivityForm.editAct == false}">
												<digi:trn key="aim:addNewActivity">
													Add New Activity
												</digi:trn>
											</c:if>			
											<c:if test="${aimEditActivityForm.editAct == true}">
												<digi:trn key="aim:editActivity">
													Edit Activity
												</digi:trn>
											</c:if></span>
										</td>									
									</tr>
								</table>
								</c:if>
							</td>
							<td width="50%" align="right"> 
								<table cellSpacing="1" cellPadding="1" vAlign="bottom" border=0>
									<tr>
										<td height=16 vAlign=bottom align="right">
											<digi:img src="module/aim/images/print_icon.gif"/>
										</td>
										<td height=16 vAlign=center align="left" width="50">
											<digi:link href="/showPrinterFriendlyPage.do?edit=true" target="_blank">
												<digi:trn key="aim:print">
													Print
												</digi:trn>
											</digi:link>&nbsp;
										</td>									
									</tr>
								</table>							
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td width="100%" vAlign="top">	
						<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left" bgcolor="#006699">
							<tr><td vAlign="center" width="100%" align ="center" 
							class="textalb" height="20"><%--
								<digi:trn key="aim:addEditActivityConfirmation">
								Add/Edit Activity Confirmation</digi:trn>--%>
								<digi:trn key="aim:previewActivity">
									Preview Activity
								</digi:trn>
							</td></tr>
							<tr><td width="100%" bgcolor="#f4f4f2">
							<table width="100%" cellSpacing="2" cellPadding="2" vAlign="top" align="left" bgcolor="#f4f4f2">
							<tr><td align="center" vAlign="top" bgcolor="#ffffff">
								<table width="98%" cellSpacing=1 cellpadding=3 bgcolor="#006699">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:ampId">
											AMP ID</digi:trn>
										</td>
										<td class="v-name" bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.ampId}"/>
										</td>
									</tr>								
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:projectTitle">
											Project title</digi:trn>
										</td>
										<td class="v-name" bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.title}"/>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:objectives">
											Objectives</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<bean:define id="objKey">
												<c:out value="${aimEditActivityForm.objectives}"/>
											</bean:define>
											<digi:edit key="<%=objKey%>"></digi:edit>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:description">
											Description</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<bean:define id="descKey">
												<c:out value="${aimEditActivityForm.description}"/>
											</bean:define>
											<digi:edit key="<%=descKey%>"></digi:edit>
										</td>
									</tr>		
									<%--
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:conditions">
											Conditions</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.conditions}"/>
										</td>
									</tr>									
									--%>
								</table>
							</td></tr>
							<tr><td bgcolor="#f4f4f2">
								&nbsp;
							</td></tr>
							<tr><td align="center" vAlign="top" bgcolor="#f4f4f2">
								<table width="98%" cellSpacing=1 cellpadding=3 bgcolor="#006699">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:orgsAndProjectIds">
											Organizations and Project IDs
											</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.selectedOrganizations}">
												<table cellSpacing=2 cellPadding=2 border=0>
													<c:forEach var="selectedOrganizations" items="${aimEditActivityForm.selectedOrganizations}" >
														<tr><td>	
														<c:out value="${selectedOrganizations.name}"/> :
														<c:out value="${selectedOrganizations.projectId}"/>				
														</td></tr>
													</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:planning">
											Planning</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<table width="100%" cellSpacing=2 cellPadding=1>
												<tr>
													<td width="32%"><digi:trn key="aim:originalApprovalDate">
													Original Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.originalAppDate}"/>
													</td>
												</tr>
												<tr>
													<td width="32%"><digi:trn key="aim:revisedApprovalDate">Revised Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.revisedAppDate}"/>
													</td>
												</tr>
												<tr>
													<td width="32%"><digi:trn key="aim:originalStartDate">Original Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.originalStartDate}"/>
													</td>
												</tr>
												<tr>
													<td width="32%"><digi:trn key="aim:revisedStartDate">Revised Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.revisedStartDate}"/>
													</td>
												</tr>												
												<tr>
													<td width="32%"><digi:trn key="aim:currentCompletionDate">
													Current Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.currentCompDate}"/>
													</td>
												</tr>																								
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td colspan="3"><digi:trn key="aim:status">Status</digi:trn> :
														<c:if test="${!empty aimEditActivityForm.status}">
															<bean:define name="aimEditActivityForm" id="statusId" property="status" />
															<c:if test="${statusId != -1}">
																<c:forEach var="statusCol" items="${aimEditActivityForm.statusCollection}" >
																	<c:if test="${statusCol.ampStatusId == statusId}">
																		<c:out value="${statusCol.name}"/>
																	</c:if>
																</c:forEach>
															</c:if>
														</c:if>
													</td>
												</tr>																								
												<tr>
													<td colspan="3"><c:out value="${aimEditActivityForm.statusReason}"/></td>
												</tr>
											</table>
										</td>
									</tr>		
									<%--
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:financingInstrument">Financing Instrument</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.modalityCollection}">
												<c:forEach var="tempModality" items="${aimEditActivityForm.modalityCollection}">
													<c:if test="${tempModality.ampModalityId == aimEditActivityForm.modality}">
														<c:out value="${tempModality.name}"/>
													</c:if>
												</c:forEach>
											</c:if>
										</td>
									</tr>		
									--%>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:level">
											Level</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.levelCollection}">
												<c:forEach var="tempLevel" items="${aimEditActivityForm.levelCollection}">
													<c:if test="${tempLevel.ampLevelId == aimEditActivityForm.level}">												
														<c:out value="${tempLevel.name}"/>
													</c:if>
												</c:forEach>
											</c:if>										
										</td>
									</tr>																											
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:locations">
											Locations</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.selectedLocs}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="locations" items="${aimEditActivityForm.selectedLocs}">
													<tr><td>
													<c:if test="${!empty locations.country}">
														[<c:out value="${locations.country}"/>]
													</c:if>
													<c:if test="${!empty locations.region}">
														[<c:out value="${locations.region}"/>]
													</c:if>
													<c:if test="${!empty locations.zone}">
														[<c:out value="${locations.zone}"/>]
													</c:if>
													<c:if test="${!empty locations.woreda}">
														[<c:out value="${locations.woreda}"/>]
													</c:if>
													</td></tr>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>		
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:sectors">
											Sectors</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.activitySectors}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="sectors" items="${aimEditActivityForm.activitySectors}">
													<tr><td>
													<c:if test="${!empty sectors.sectorName}">
														[<c:out value="${sectors.sectorName}"/>]
													</c:if>
													<c:if test="${!empty sectors.subsectorLevel1Name}">
														[<c:out value="${sectors.subsectorLevel1Name}"/>]
													</c:if>
													<c:if test="${!empty sectors.subsectorLevel2Name}">
														[<c:out value="${sectors.subsectorLevel2Name}"/>]
													</c:if>
													</td></tr>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:program">Program</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.programCollection}">
												<c:forEach var="tempPgm" items="${aimEditActivityForm.programCollection}">
													<c:if test="${tempPgm.ampThemeId == aimEditActivityForm.program}">
														<c:out value="${tempPgm.name}"/>
													</c:if>
												</c:forEach>
											</c:if>
										</td>
									</tr>											
									
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:fundings">
											Fundings</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.fundingOrganizations}">
												<table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#006699">
												<c:forEach var="fundOrgs" items="${aimEditActivityForm.fundingOrganizations}">
													<tr><td bgcolor="#ffffff">
														<table width="100%" cellSpacing="1" cellPadding="1">
															<tr><td bgcolor="#ffffff"><b>
																<c:out value="${fundOrgs.orgName}"/></b>
															</td></tr>
															<c:if test="${!empty fundOrgs.fundings}">
																<c:forEach var="fund" items="${fundOrgs.fundings}">
																	<tr><td bgcolor="#ffffff">
																		<table width="100%" cellSpacing="1" cellPadding="1" class="box-border">
																			<tr>
																				<td width="31%">
																					<digi:trn key="aim:fundingOrgId">
																					Funding Organization Id</digi:trn>
																				</td>
																				<td width="1">:</td>
																				<td align="left">
																					<c:out value="${fund.orgFundingId}"/>
																				</td>
																			</tr>																		
																			<tr>
																				<td width="31%">
																					<digi:trn key="aim:typeOfAssistance">
																					Type of Assistance</digi:trn>
																				</td>
																				<td width="1">:</td>
																				<td align="left">
																					<c:out value="${fund.ampTermsAssist.termsAssistName}"/>
																				</td>
																			</tr>
																			<tr>
																				<td width="31%">
																					<digi:trn key="aim:financingInstrument">
																					Financing Instrument</digi:trn>
																				</td>
																				<td width="1">:</td>
																				<td align="left">
																					<c:if test="${!empty aimEditActivityForm.modalityCollection}">
																						<c:forEach var="tempModality" 
																						items="${aimEditActivityForm.modalityCollection}">
																						<c:if test="${tempModality.ampModalityId == fund.modality.ampModalityId}">
																							<c:out value="${tempModality.name}"/>
																						</c:if>
																						</c:forEach>
																					</c:if>
																				</td>
																			</tr>																			
																			<%--
																			<tr>
																				<td width="31%">
																					<digi:trn key="aim:signatureDate">
																					Signature Date</digi:trn>
																				</td>
																				<td width="1">:</td>
																				<td align="left">
																					<c:out value="${fund.signatureDate}"/>
																				</td>
																			</tr>	
																			--%>
																			<c:if test="${!empty fund.fundingDetails}">
																			<tr><td colspan="3">
																			<table width="100%" cellSpacing=1 cellPadding=1 bgcolor="#dddddd">
																				<tr><td valign="top" width="100" bgcolor="#ffffff">
																					<digi:trn key="aim:commitments">
																					Commitments</digi:trn>
																				</td>
																				<td bgcolor="#ffffff">
																					<c:forEach var="fundDet" items="${fund.fundingDetails}">
																					<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																						<c:if test="${fundDet.transactionType == 0}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fundDet.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fundDet.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fundDet.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fundDet.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fundDet.perspectiveName}"/>
																							</td>
																						</tr>
																						</c:if>																						
																					</table>
																				   </c:forEach>
																				</td></tr>
																			</table>
																			</td></tr>
																			<tr><td colspan="3">
																			<table width="100%" cellSpacing=1 cellPadding=1 bgcolor="#dddddd">
																				<tr><td valign="top" width="100" bgcolor="#ffffff">
																					<digi:trn key="aim:disbursements">
																					Disbursements</digi:trn>
																				</td>
																				<td bgcolor="#ffffff">
																					<c:forEach var="fundDet" items="${fund.fundingDetails}">
																					<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																						<c:if test="${fundDet.transactionType == 1}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fundDet.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fundDet.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fundDet.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fundDet.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fundDet.perspectiveName}"/>
																							</td>
																						</tr>
																						</c:if>																						
																					</table>
																				   </c:forEach>
																				</td></tr>
																			</table>
																			</td></tr>	
																			<tr><td colspan="3">
																			<table width="100%" cellSpacing=1 cellPadding=1 bgcolor="#dddddd">
																				<tr><td valign="top" width="100" bgcolor="#ffffff">
																					<digi:trn key="expenditures">
																					Expenditures</digi:trn>
																				</td>
																				<td bgcolor="#ffffff">
																					<c:forEach var="fundDet" items="${fund.fundingDetails}">
																					<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																						<c:if test="${fundDet.transactionType == 2}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fundDet.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fundDet.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fundDet.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fundDet.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fundDet.perspectiveName}"/>
																							</td>
																						</tr>
																						</c:if>																						
																					</table>
																				   </c:forEach>
																				</td></tr>
																			</table>
																			</td></tr>																			
																			</c:if>
																		</table>
																	</td></tr>
																</c:forEach>
															</c:if>
														</table>
													</td></tr>
												</c:forEach>
												<tr><td bgcolor="#ffffff">
													<FONT color=blue>*
													<digi:trn key="aim:theAmountEnteredAreInThousands">	
													The amount entered are in thousands (000)</digi:trn></FONT>
												</td></tr>
												</table>
											</c:if>
										</td>
									</tr>										
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:components">
											Components</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.selectedComponents}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="comp" items="${aimEditActivityForm.selectedComponents}">
													<tr><td><b>
													<c:out value="${comp.title}"/></b> -
													<c:out value="${comp.amount}"/> -
													<c:out value="${comp.reportingDate}"/>
													</td></tr>
													<tr><td>
													<i>
													<digi:trn key="aim:description">Description</digi:trn> :</i>
													<c:out value="${comp.description}"/>
													</td></tr>													
													<c:if test="${!empty comp.phyProgress}">
														<c:forEach var="phyProg" items="${comp.phyProgress}">
															<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																<b>
																<c:out value="${phyProg.title}"/></b> -
																<c:out value="${phyProg.reportingDate}"/>
															</td></tr>	
															<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<i>
																<digi:trn key="aim:description">Description</digi:trn> :</i>
																<c:out value="${phyProg.description}"/>
															</td></tr>															
														</c:forEach>
													</c:if>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>	
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:relatedDocuments">
											Related Documents</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.documentList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												<c:forEach var="docs" items="${aimEditActivityForm.documentList}">
													<tr><td>
					   								<bean:define id="fileName" name="docs" 
														property="fileName" />
													    <%
														int index2;
														String extension = null;
														index2 = ((String)fileName).lastIndexOf(".");	
														if( index2 >= 0 ) {
														   extension = "module/cms/images/extensions/" + 
															((String)fileName).substring(
															index2 + 1,((String)fileName).length()) + ".gif";
														}
													    %>
														 
													 <table width="100%" cellPadding=0 cellSpacing=1>
													 	<tr>
															<td width="2">
																<digi:img skipBody="true" src="<%=extension%>" border="0" align="absmiddle"/>
															</td>
															<td vAlign="center" align="left">
																<c:out value="${docs.title}"/> -
																<c:out value="${docs.fileName}"/>
															</td>
														</tr>
													 </table>
													</td></tr>
												</c:forEach>
												</table>
											</c:if>
											<c:if test="${!empty aimEditActivityForm.linksList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												<c:forEach var="links" items="${aimEditActivityForm.linksList}">
													<tr><td>
														<table width="100%" cellPadding=0 cellSpacing=1>
															<tr>
																<td width="2">
																	<digi:img src="module/aim/images/web-page.gif"/>
																</td>
																<td align="left" vAlign="center">
																	<c:out value="${links.title}"/> -
																	<a href="<c:out value="${links.url}"/>">
																	<c:out value="${links.url}"/></a>
																</td>
															</tr>
														</table>
													</td></tr>
												</c:forEach>
												</table>
											</c:if>											
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:executingAgencies">
											Executing Agencies</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.executingAgencies}">
												<table width="100%" cellpadding="2" cellspacing="2" valign="top" align="left">
													<c:forEach var="exAgency" items="${aimEditActivityForm.executingAgencies}"> 
														<tr><td>
															<c:out value="${exAgency.name}"/>
														</td></tr>												
													</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:implementingAgencies">
											Implementing Agencies</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.impAgencies}">
												<table width="100%" cellpadding="2" cellspacing="2" valign="top" align="left">
													<c:forEach var="impAgency" items="${aimEditActivityForm.impAgencies}"> 
														<tr><td>
															<c:out value="${impAgency.name}"/>
														</td></tr>												
													</c:forEach>
												</table>
											</c:if>										
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:contractors">
											Contractors</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.contractors}"/>
										</td>
									</tr>									
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:donorFundingContactInformation">
											Donor funding contact information</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.dnrCntFirstName}"/>
											<c:out value="${aimEditActivityForm.dnrCntLastName}"/> -
											<c:out value="${aimEditActivityForm.dnrCntEmail}"/>
										</td>
									</tr>	
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:mofedContactInformation">
											MOFED contact information</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.mfdCntFirstName}"/>
											<c:out value="${aimEditActivityForm.mfdCntLastName}"/> -
											<c:out value="${aimEditActivityForm.mfdCntEmail}"/>
										</td>
									</tr>										
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:activityCreatedBy">
											Activity created by</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.actAthFirstName}"/>
											<c:out value="${aimEditActivityForm.actAthLastName}"/> -
											<c:out value="${aimEditActivityForm.actAthEmail}"/>
										</td>
									</tr>																			
									<logic:notEmpty name="aimEditActivityForm" property="createdDate">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#ffffff">
											<digi:trn key="aim:activityCreatedOn">
											Activity created on</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.createdDate}"/>
										</td>
									</tr>									
									</logic:notEmpty>
								</table>
							</td></tr>							
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>
							<c:if test="${aimEditActivityForm.pageId != 2}">	
							<tr><td bgColor=#f4f4f2 align="center">
								<table cellPadding=3>
									<tr>
										<td>
											<input type="button" value=" << Back" class="dr-menu" onclick="backClicked()"
											name="backButton">
											
										</td>
										<td>
											<input type="button" value="Save Activity" class="dr-menu" onclick="disable()"
											name="submitButton">
										</td>
									</tr>
								</table>
							</td></tr>
							</c:if>
							</table>
							</td></tr>							
						</table>
						</td></tr>
					</table>
				</td></tr>
				<tr><td>
					&nbsp;
				</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>
