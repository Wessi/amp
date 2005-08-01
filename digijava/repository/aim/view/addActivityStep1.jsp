
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
<!--

function checkSelOrgs() {
	if (document.aimEditActivityForm.selOrgs.checked != null) { // only one org. added
		if (document.aimEditActivityForm.selOrgs.checked == false) {
			alert("Please choose an organization to remove");
			return false;
		}
	} else { // many org. present
		var length = document.aimEditActivityForm.selOrgs.length;	  
		var flag = 0;
		for (i = 0;i < length;i ++) {
			if (document.aimEditActivityForm.selOrgs[i].checked == true) {
				flag = 1;
				break;
			}
		}

		if (flag == 0) {
			alert("Please choose an organization to remove");
			return false;					  
		}
	}
	return true;
}	

function selectOrganisation() {
	if (document.aimEditActivityForm.currUrl.value == "") { 		  
		openNewWindow(600, 400);
		<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true" />
		document.aimEditActivityForm.currUrl.value = "<%=selectOrganization%>"		  
		document.aimEditActivityForm.action = "<%= selectOrganization %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	} else {
		popupPointer.focus();
	}
} 	

function commentWin() {
	if (document.aimEditActivityForm.currUrl1.value == "") { 		  
		openNewWindow(600, 400);
		<digi:context name="comment" property="context/module/moduleinstance/viewComment.do" />
		url = "<%=comment %>?comment=current_completion_date";
		document.aimEditActivityForm.currUrl1.value = "<%=comment %>";		  
		document.aimEditActivityForm.action = url;
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	} else {
		popupPointer.focus();
	}
}

function removeSelOrganisations() {
	var flag = checkSelOrgs();
	if (flag == false) return false;
	<digi:context name="remOrgs" property="context/module/moduleinstance/removeSelOrganisations.do" />
	document.aimEditActivityForm.action = "<%= remOrgs %>";
	document.aimEditActivityForm.target = "_self"
	document.aimEditActivityForm.submit();
	return true;
}

function resetAll()
{
	<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do" />
	document.aimEditActivityForm.action = "<%= resetAll %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	return true;
}

function validateForm() {
	if (trim(document.aimEditActivityForm.title.value) == "") {
		alert("Please enter title");
		document.aimEditActivityForm.title.focus();
		return false;
	}

	if (document.aimEditActivityForm.status.value == "-1") {
		alert("Please select status");
		document.aimEditActivityForm.status.focus();
		return false;
	}
	gotoStep(2);
	return true;
}

function reviseCloseDate() {
	openNewWindow(600, 150);
	<digi:context name="rev" property="context/module/moduleinstance/reviseCompDate.do" />
	document.aimEditActivityForm.action = "<%= rev %>";
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();		  
}


-->
</script>


<digi:instance property="aimEditActivityForm" />
<digi:form action="/addActivity.do" method="post">

<html:hidden property="step"/>
<input type="hidden" name="selectedDate" value="">
<input type="hidden" name="currUrl">
<input type="hidden" name="currUrl1">

<html:hidden property="reset" />

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left" border=0>
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left vAlign=top class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
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
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop </digi:trn>
									</bean:define>
									<digi:link href="/viewMyDesktop.do" styleClass="comment"  title="<%=translation%>">
										<digi:trn key="aim:portfolio">Portfolio</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;								
								</c:if>
								<c:if test="${aimEditActivityForm.edit == true}">
									<digi:trn key="aim:editActivityStep1">Edit Activity - Step 1</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.edit == false}">
									<digi:trn key="aim:addActivityStep1">Add Activity - Step 1</digi:trn>
								</c:if>								
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height=16 vAlign=center width="100%"><span class=subtitle-blue>
								<c:if test="${aimEditActivityForm.edit == false}">
									<digi:trn key="aim:addNewActivity">Add New Activity</digi:trn>
								</c:if>			
								<c:if test="${aimEditActivityForm.edit == true}">
									<digi:trn key="aim:editActivity">Edit Activity</digi:trn>
								</c:if>										
							</td>
						</tr>	
					</table>
				</td></tr>
				<tr><td>
					&nbsp;
					<digi:trn key="um:allMarkedRequiredField">All fields marked with an <FONT color=red><B><BIG>*</BIG>
					</B></FONT> are required.</digi:trn>					
				</td></tr>
				<tr> <td>
					<digi:errors/>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td width="75%" vAlign="top">	
						<table cellPadding=2 cellSpacing=1 width="100%" border=0 bgcolor="#006699">
							<tr>
								<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:step1IdentificationAndPlanning">Step 1 of 7: Identification | Planning</digi:trn>
								</td>
							</tr>
							<tr><td bgcolor="#f4f4f2" width="100%">
							<table width="100%" cellSpacing="2" cellPadding="2" vAlign="top" align="left" bgcolor="#f4f4f2">
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<!-- contents -->
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:identification">Identification</digi:trn></b>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>
										<table width="100%" bgcolor="#cccccc" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<FONT color=red>*</FONT>
												<a title="<digi:trn key="aim:TitleInDonorsOrMoFEDInternalSystems">Title used in donors or MoFED internal systems</digi:trn>">
												<digi:trn key="aim:projectTitle">Project Title</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<a title="<digi:trn key="aim:TitleInDonorsOrMoFEDInternalSystems"> 
												Title used in donors or MoFED internal systems
												</digi:trn>">
												<html:textarea property="title" cols="60" rows="2" styleClass="inp-text"/>
												</a>
											</td></tr>									
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:ObjectivesAndComponentsofProject">The key objectives and main components of the project</digi:trn>">
												<digi:trn key="aim:objective">Objective</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<%--
												<a title="<digi:trn key="aim:ObjectivesAndComponentsofProject">The key objectives and main components of the project</digi:trn>">
												<html:textarea property="objectives" cols="60" rows="4" styleClass="inp-text" />
											   </a>
												<digi:edit key="aim.act.desc.1247"/>
												--%>
												<bean:define id="objKey">
													<c:out value="${aimEditActivityForm.objectives}"/>
												</bean:define>
												
												<digi:edit key="<%=objKey%>"></digi:edit>
												<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=objKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do">Edit</a>
											</td></tr>																				
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionofProject">Summary information describing the project</digi:trn>">
												<digi:trn key="aim:description">
												Description
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<%--
												<a title="<digi:trn key="aim:DescriptionofProject">Summary information describing the project</digi:trn>">
												<html:textarea property="description" cols="60" rows="4" styleClass="inp-text" />
											   </a>--%>

												<bean:define id="descKey">
													<c:out value="${aimEditActivityForm.description}"/>
												</bean:define>
												
												<digi:edit key="<%=descKey%>"></digi:edit>
												<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=descKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do">Edit</a>
											</td></tr>																				
										</table>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
									
										<table cellPadding=5 cellSpacing=1 border=0 width="100%" bgcolor="#d7eafd">
											<tr>
												<td align="left"><b>
													<a title="<digi:trn key="aim:TrackActivitiesintheDonorsInternalDatabase">Facilitates tracking activities in donors' internal databases </digi:trn>">
													<digi:trn key="aim:orgsAndProjectIds">Organizations and Project IDs</digi:trn>
													</a></b>												
												</td>
											</tr>
											<tr>
												<td bgcolor="#ffffff" width="100%">

										<table cellPadding=1 cellSpacing=1 border=0 bgcolor="#ffffff" width="100%">
											<logic:empty name="aimEditActivityForm" property="selectedOrganizations">
												<td>
													<a title="<digi:trn key="aim:TrackActivitiesintheDonorsInternalDatabase">Facilitates tracking activities in donors' internal databases </digi:trn>">
													<input type="button" value="Add Organisation" class="buton" name="addOrgs" onclick="selectOrganisation()">  					  </a>
												</td>
											</logic:empty>
											<logic:notEmpty name="aimEditActivityForm" property="selectedOrganizations">
											<td>
												<table cellSpacing=1 cellPadding=1 border=0 width="500">
													<c:forEach var="selectedOrganizations" items="${aimEditActivityForm.selectedOrganizations}" >
														<tr>
															<td align="left" width=3> 	
																<html:multibox property="selOrgs">
																	<c:out value="${selectedOrganizations.ampOrgId}"/>
																</html:multibox>
															</td>
															<td align="left" width="367">
																<c:out value="${selectedOrganizations.name}"/>
															</td>
															<td align="left" width="130">
																<html:text name="selectedOrganizations" 
																property="projectId" indexed="true" 
																styleClass="inp-text" size="15"/>
															</td>															
														</td></tr>
													</c:forEach>													
													<tr><td colspan="3">
														<table cellSpacing=2 cellPadding=2>
															<tr>
																<td>
																	<input type="button" value="Add Organisation" class="buton" 
																		onclick="selectOrganisation()">
																</td>
																<td>
																	<input type="button" value="Remove Organisations" class="buton" 
																		onclick="return removeSelOrganisations()">
																</td>
															</tr>
														</table>
													</td></tr>
												</table>
											</td>
											</logic:notEmpty>
											</tr>
										</table>
												
												</td>
											</tr>
										</table>

										
									
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:planning">Planning</digi:trn></b>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>		
									<tr><td>
										<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ExpectedApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project is expected to be approved by the donor agency</digi:trn>">&nbsp;
													<digi:trn key="aim:proposedApprovalDate">Proposed Approval Date</digi:trn>
													</a>
												</td>												
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:ExpectedApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project is expected to be approved by the donor agency </digi:trn>">								
																<html:text name="aimEditActivityForm" property="originalAppDate" size="10" 
																styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
															<a href="javascript:newWindow(1)">
																<img src= "../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" width=24 height=22 border=0>
															</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ActualApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project was approved by the donor agency</digi:trn>">&nbsp;
												<digi:trn key="aim:actualApprovalDate">Actual Approval Date </digi:trn></a>					
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:ActualApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project was approved by the donor agency</digi:trn>">
																<html:text name="aimEditActivityForm" property="revisedAppDate" size="10" 
																styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:newWindow(2)">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" width=24 height=22 border=0>
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
																						
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ProposedDateProjectStart">Date (dd/mm/yy) when the project is expected to commence</digi:trn>">&nbsp;
   												<digi:trn key="aim:proposedStartDate">Proposed Start Date</digi:trn>
													</a>					
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:ProposedDateProjectStart">Date (dd/mm/yy) when the project is expected to commence</digi:trn>">																   	<html:text name="aimEditActivityForm" property="originalStartDate" size="10" 
																styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:newWindow(3)">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" width=24 height=22 border=0>
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ActualDateofProjectStart">Date (dd/mm/yy) when the project commenced (effective start date) </digi:trn>">&nbsp;&nbsp;
													<digi:trn key="aim:actualStartDate">Actual Start Date </digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:ActualDateofProjectStart">Date (dd/mm/yy) when the project commenced (effective start date)</digi:trn>">
																<html:text name="aimEditActivityForm" property="revisedStartDate" size="10" 
																styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:newWindow(4)">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" width=24 height=22 border=0>
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<c:if test="${aimEditActivityForm.edit == false}">
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ExpectedCompletionDateofProject">Date (dd/mm/yy) when the project is expected to be completed </digi:trn>">&nbsp;&nbsp;
													<digi:trn key="aim:proposedCompletionDate">Proposed Completion Date</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:ExpectedCompletionDateofProject">Date (dd/mm/yy) when the project is expected to be completed</digi:trn>">
																<html:text name="aimEditActivityForm" property="proposedCompDate" 
																size="10" styleClass="inp-text" 
																readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:newWindow(9)">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to view the Calendar" width=24 height=22 border=0>
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>											
											<tr>
												<td width=200 bgcolor="#ffffff">	&nbsp;
												<a title="<digi:trn key="aim:CompletionDateofProject">Date (dd/mm/yy) when the project is expected to end or ended. AMP users have the possibility to update this field; AMP will keep track of the history of proposed completion dates </digi:trn>">
												<digi:trn key="aim:currentCompletionDate">Current Completion Date</digi:trn>
												</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:CompletionDateofProject">Date (dd/mm/yy) when the project is expected to end or ended. AMP users have the possibility to update this field; AMP will keep track of the history of proposed completion dates </digi:trn>">
																<html:text name="aimEditActivityForm" property="currentCompDate" 
																size="10" styleClass="inp-text" 
																readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:newWindow(5)">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" width=24 height=22 border=0>
																</a>
															</td>
															<td>&nbsp;
																<input type="button" class="buton" value="Comment" onclick="commentWin()">
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</c:if>
											<c:if test="${aimEditActivityForm.edit == true}">
												<tr>
													<td width=200 bgcolor="#ffffff">&nbsp;
													<digi:trn key="aim:currentCompletionDate">Current Completion Date</digi:trn>
													</td>
													<td bgcolor="#ffffff">
														<table cellPadding=0 cellSpacing=0>
															<tr>
																<td>
																	<html:text name="aimEditActivityForm" property="currentCompDate" 
																	size="10" styleClass="inp-text" 
																	readonly="true"/>
																</td>
																<td align="left" vAlign="center">&nbsp;
																	<input type="button" class="buton" value="Revise"
																	onclick="reviseCloseDate()">
																</td>
																<td>&nbsp;
																	<input type="button" class="buton" value="Comment" onclick="commentWin()">
																</td>
															</tr>
														</table>
													</td>
												</tr>											
												<c:if test="${!empty aimEditActivityForm.activityCloseDates}">
												<tr>
													<td width=200 bgcolor="#ffffff">&nbsp;
													<digi:trn key="aim:proposedCompletionDates">Proposed Completion Dates</digi:trn>
													</td>
													<td bgcolor="#ffffff">
														<table cellPadding=0 cellSpacing=0>
															<c:forEach var="closeDate" items="${aimEditActivityForm.activityCloseDates}">
															<tr>
																<td>
																	<c:out value="${closeDate}"/>
																</td>
															</tr>
															</c:forEach>
														</table>
													</td>
												</tr>
												</c:if>
											</c:if>
											<tr>
												<td bgcolor="#ffffff">													
												<a title="<digi:trn key="aim:StatusofProject">Planned: from the conceptual stage to just prior to official commitment. On-going: the project is committed, is active, but not yet completed. Completed: the project is finished, with all approved assistance provided. Cancelled: the project was committed but was terminated prior to planned completion.  Suspended: the project has been suspended.</digi:trn>">
												<FONT color=red>*</FONT>&nbsp;
												<digi:trn key="aim:status">Status</digi:trn>
												</a>
												</td>
												<td bgcolor="#ffffff">
														<html:select property="status" styleClass="inp-text">
														<html:option value="-1">Please select the status</html:option>
														<html:optionsCollection name="aimEditActivityForm" property="statusCollection" 
														value="ampStatusId" label="name" />
													</html:select>													
													<br><br>
													<a title="<digi:trn key="aim:ReasonforStatusofProject">Use this space to provide explanations as to why that status was selected. Used primarily in the case of cancelled and suspended projects</digi:trn>">
													If there have been some changes in the status, explain below the reasons :
													<br>														
													<html:textarea property="statusReason" cols="50" rows="3" styleClass="inp-text" />
													</a>														
												</td>
											</tr>											
										</table>
									</td></tr>									
								</table>

								<!-- end contents -->
							</td></tr>
							<tr><td bgColor=#f4f4f2>&nbsp;</td></tr>
							<tr><td bgColor=#f4f4f2 align="center">
								<table cellPadding=3>
									<tr>
										<td>
											<html:submit value="Next >>" styleClass="dr-menu" onclick="return validateForm()"/>
										</td>
										<td>
											<html:reset value="Reset" styleClass="dr-menu" onclick="return resetAll()"/>
										</td>
									</tr>
								</table>
							</td></tr>
							</table>
							</td></tr>							
						</table>						
						</td>
						<td width="25%" vAlign="top" align="right">
						<!-- edit activity form menu -->
							<jsp:include page="editActivityMenu.jsp" flush="true" />
						<!-- end of activity form menu -->							
						</td></tr>
					</table>
				</td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>
