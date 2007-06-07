<%@ page pageEncoding="UTF-8" %>
<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>
<%@ page import = "java.io.PrintWriter, java.util.*" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>	
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>


<digi:instance property="aimEditActivityForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

<!--

function gotoStep(value) {
	document.aimEditActivityForm.step.value = value;
	<digi:context name="step" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= step %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function backClicked() {
	document.aimEditActivityForm.step.value = "8";
	<digi:context name="backStep" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= backStep %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function disable() {
	document.aimEditActivityForm.submitButton.disabled = true;
	document.aimEditActivityForm.backButton.disabled = true;
	var appstatus = document.aimEditActivityForm.approvalStatus.value;
	var wTLFlag   = document.aimEditActivityForm.workingTeamLeadFlag.value;
	if (appstatus == "started") {
		if (wTLFlag == "yes") {
			//if (confirm("Do you want to approve this activity ?"))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}
		else if (confirm("Do you want to submit this activity for approval ?"))
				document.aimEditActivityForm.approvalStatus.value = "created";
	}
	if (appstatus == "approved") {
		if (wTLFlag != "yes")
			document.aimEditActivityForm.approvalStatus.value = "edited";
	}
	else if (wTLFlag == "yes") {
		if (appstatus == "created" || appstatus == "edited") {
			if (confirm("Do you want to approve this activity ?"))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}
	}
	document.aimEditActivityForm.submit();
	return true;
}
-->

<%
	Long actId = (Long) request.getAttribute("actId");
	
	String url = "/aim/viewIndicatorValues.do?ampActivityId="+actId+"&tabIndex=6";
	
	String actPerfChartFileName = ChartGenerator.getActivityPerformanceChartFileName(
						 actId,session,new PrintWriter(out),370,450,url,true);

	String actPerfChartUrl = null;
	if (actPerfChartFileName != null) {
		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;
	}

	
	String actRiskChartFileName = ChartGenerator.getActivityRiskChartFileName(
						 actId,session,new PrintWriter(out),370,350,url);

	String actRiskChartUrl = null;

	if (actRiskChartFileName != null)  {
		actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;
	}
%>

</script>
<digi:context name="digiContext" property="context" />
<digi:form action="/saveActivity.do" method="post">
<html:hidden property="step" />
<html:hidden property="editAct" />
<html:hidden property="approvalStatus" />
<html:hidden property="workingTeamLeadFlag" />

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<logic:present name="currentMember" scope="session">
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
</logic:present>
<tr><td width="100%" vAlign="top" align="left">

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="780" vAlign="top" align="left" border=0>
	<tr>
		<td class=r-dotted-lg width="10" align="left" vAlign="top">&nbsp;</td>
		<td class=r-dotted-lg align=left vAlign=top>
			<table width="100%" cellSpacing="3" cellPadding="1" vAlign="top" align="left" border=0>
					<tr><td>
					<logic:present name="currentMember" scope="session">
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
								<c:if test="${aimEditActivityForm.pageId == 2}">
									<c:set var="trnViewMyDesktop">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
									</c:set>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${trnViewMyDesktop}" >
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;

									<jsp:useBean id="urlParam" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParam}" property="ampActivityId">
										<c:out value="${aimEditActivityForm.activityId}"/>
									</c:set>
									<c:set target="${urlParam}" property="tabIndex" value="0"/>

									<c:set var="trnViewChannelOverview">
										<digi:trn key="aim:clickToViewProjectDetails">Click here to view Project Details</digi:trn>
									</c:set>

									<digi:link href="/viewChannelOverview.do" styleClass="comment" name="urlParam" title="${trnViewChannelOverview}" >
										<digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:trn key="aim:previewActivity">
									Preview Activity
									</digi:trn>
								</c:if>

								<c:if test="${aimEditActivityForm.pageId == 1}">

								<c:set var="trnAddActivity1">
									<digi:trn key="aim:clickToViewAddActivityStep1">Click here to goto Add Activity Step 1</digi:trn>
								</c:set>

								<c:if test="${aimEditActivityForm.donorFlag == true}">
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
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<digi:link href="/addActivity.do?step=1&edit=true" styleClass="comment" title="${trnAddActivity1}" >
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
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep2">
										Step 2
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity2">
										<digi:trn key="aim:clickToViewAddActivityStep2">Click here to goto Add Activity Step 2</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment" title="${trnAddActivity2}" >
										<digi:trn key="aim:addActivityStep2">
											Step 2
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:set var="trnAddActivity3">
									<digi:trn key="aim:clickToViewAddActivityStep3">Click here to goto Add Activity Step 3</digi:trn>
								</c:set>
								<digi:link href="/addActivity.do?step=3&edit=true" styleClass="comment" title="${trnAddActivity3}" >
									<digi:trn key="aim:addActivityStep3">
										Step 3
									</digi:trn>
								</digi:link>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep4">
										Step 4
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity4">
										<digi:trn key="aim:clickToViewAddActivityStep4">Click here to goto Add Activity Step 4</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=4&edit=true" styleClass="comment" title="${trnAddActivity4}" >
										<digi:trn key="aim:addActivityStep4">
											Step 4
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep5">
										Step 5
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity">
										<digi:trn key="aim:clickToViewAddActivityStep5">Click here to goto Add Activity Step 5</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=5&edit=true" styleClass="comment" title="${trnAddActivity}" >
										<digi:trn key="aim:addActivityStep5">
											Step 5
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep6">
										Step 6
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity6">
										<digi:trn key="aim:clickToViewAddActivityStep6">Click here to goto Add Activity Step 6</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=6&edit=true" styleClass="comment" title="${trnAddActivity6}" >
										<digi:trn key="aim:addActivityStep6">
											Step 6
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep7">
										Step 7
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity7">
										<digi:trn key="aim:clickToViewAddActivityStep7">Click here to goto Add Activity Step 7</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=7&edit=true" styleClass="comment" title="${trnAddActivity7}" >
										<digi:trn key="aim:addActivityStep7">
											Step 7
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep8">
										Step 8
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity8">
										<digi:trn key="aim:clickToViewAddActivityStep8">Click here to goto Add Activity Step 8</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=8&edit=true" styleClass="comment" title="${trnAddActivity8}" >
										<digi:trn key="aim:addActivityStep8">
											Step 8
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep9">
										Step 9
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity9">
										<digi:trn key="aim:clickToViewAddActivityStep9">Click here to goto Add Activity Step 9</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=10&edit=true" styleClass="comment" title="${trnAddActivity9}" >
										<digi:trn key="aim:addActivityStep9">
											Step 9
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<digi:trn key="aim:previewActivity">
									Preview Activity
								</digi:trn>
								</c:if>
								</span>
							</td>
						</tr>
					</table>
					</logic:present>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="0" cellPadding="0" vAlign="bottom">
						<tr>
							<td width="50%" align="left">
								<c:if test="${aimEditActivityForm.pageId == 1}">
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
							<logic:present name="currentMember" scope="session">
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
							</logic:present>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td width="100%" vAlign="top">
						<table width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left">
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn key="aim:previewActivity">
													Preview Activity
												</digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td width="100%" bgcolor="#f4f4f2">
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							<tr><td align="center" vAlign="top" bgcolor="#ffffff">
								<table width="100%" cellSpacing=1 cellpadding=3 bgcolor="#dddddd">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:ampId">
											AMP ID</digi:trn>
										</td>
										<td class="v-name" bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.ampId}"/>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:projectTitle">
											Project title</digi:trn>
										</td>
										<td class="v-name"  bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.title}"/>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:objectives">
											Objectives</digi:trn>
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.objectives!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.objectives}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:objectiveComments">
											Objective Comments</digi:trn>
										</td>
										<td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Objective Assumption">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:objectiveAssumption">Objective Assumption</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Objective Verification">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:objectiveVerification">Objective Verification</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										
										</td>
									</tr>
									<logic:notEmpty name="SA" scope="application">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:description">
											Description</digi:trn>
										</td>
										<td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.description!=null}">
											<c:set var="descKey" value="${aimEditActivityForm.description}" />
											<digi:edit key="${descKey}"></digi:edit>
                                            </c:if>
										</td>
									</tr>
									
									</logic:notEmpty>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:purpose">
											Purpose</digi:trn>
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.purpose!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.purpose}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:purposeComments">
											Purpose Comments</digi:trn>
										</td>
										<td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Purpose Assumption">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeAssumption">Purpose Assumption</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Purpose Verification">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeVerification">Purpose Verification</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										
										</td>
									</tr>
									
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:results">
											Results</digi:trn>
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.results!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.results}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:resultsComments">
											Results Comments</digi:trn>
										</td>
										<td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Results Assumption">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsAssumption">Results Assumption</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Results Verification">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsVerification">Results Verification</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										
										</td>
									</tr>
									

									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:actBudget">Budget</digi:trn>
										</td>
										<td bgcolor="#ffffff">

										<logic:equal name="aimEditActivityForm" property="budget" value="true">
										<digi:trn key="aim:actBudgeton">
												Activity is On Budget
										</digi:trn>
										</logic:equal>

										<logic:equal name="aimEditActivityForm" property="budget" value="false">
										<digi:trn key="aim:actBudgetoff">
												Activity is Off Budget
										</digi:trn>
										</logic:equal>

										<logic:equal name="aimEditActivityForm" property="budget" value="">
										<digi:trn key="aim:actBudgetoff">
												Activity is Off Budget
										</digi:trn>
										</logic:equal>

										</td>
									</tr>

									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
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
														<bean:define id="selectedOrgForPopup" name="selectedOrganizations"
															type="org.digijava.module.aim.helper.OrgProjectId"
															toScope="request" />
														<jsp:include page="previewOrganizationPopup.jsp"/>
														</td></tr>
														
													</c:forEach>
													
												</table>
											</c:if>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:planning">
											Planning</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<table width="100%" cellSpacing=2 cellPadding=1>
												<tr>
													<td width="32%"><digi:trn key="aim:lineMinRank">
													Line Ministry Rank</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
													<c:if test="${aimEditActivityForm.lineMinRank == -1}">
													
													</c:if>
													<c:if test="${aimEditActivityForm.lineMinRank != -1}">
													${aimEditActivityForm.lineMinRank}
													</c:if>
													</td>
												</tr>
												<tr>
													<td width="32%"><digi:trn key="aim:planMinRank">
													Ministry of Planning Rank</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
													<c:if test="${aimEditActivityForm.planMinRank == -1}">
													
													</c:if>
													<c:if test="${aimEditActivityForm.planMinRank != -1}">
													${aimEditActivityForm.planMinRank}
													</c:if>
													</td>
												</tr>
												<logic:notEmpty name="SA" scope="application">
												<tr>
													<td width="32%"><digi:trn key="aim:originalApprovalDate">
													Original Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.originalAppDate}
													</td>
												</tr>
												<tr>
													<td width="32%"><digi:trn key="aim:revisedApprovalDate">Revised Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.revisedAppDate}
													</td>
												</tr>
												<tr>
													<td width="32%"><digi:trn key="aim:originalStartDate">Original Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.originalStartDate}
													</td>
												</tr>
												</logic:notEmpty>
												<tr>
													<td width="32%"><digi:trn key="aim:ContractingDateofProject1">Final Date for Contracting</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.contractingDate}"/>
													</td>
												</tr>
												<tr>
													<td width="32%"><digi:trn key="aim:DisbursementsDateofProject1">Final Date for Disbursements</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.disbursementsDate}"/>
													</td>
												</tr>
												<tr>
													<td width="32%"><digi:trn key="aim:revisedStartDate">Revised Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.revisedStartDate}
													</td>
												</tr>
												<c:if test="${!aimEditActivityForm.editAct}">
												<tr>
													<td width="32%"><digi:trn key="aim:proposedCompletionDate">
													Proposed Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.proposedCompDate}
													</td>
												</tr>
												</c:if>
												<tr>
													<td width="32%"><digi:trn key="aim:currentCompletionDate">
													Current Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.currentCompDate}"/>
													</td>
												</tr>
												<c:if test="${aimEditActivityForm.editAct}">
												<c:if test="${!empty aimEditActivityForm.activityCloseDates}">
												<tr>
													<td width="32%" valign=top><digi:trn key="aim:proposedCompletionDates">
													Proposed Completion Dates</digi:trn></td>
													<td width="1" valign=top>:</td>
													<td align="left" valign=top>
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
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												</c:if>
												<tr>
													<td colspan="3"><digi:trn key="aim:status">Status</digi:trn> :
														<category:getoptionvalue categoryValueId="${aimEditActivityForm.statusId}"/>
													</td>
												</tr>
												<tr>
													<td colspan="3"><c:out value="${aimEditActivityForm.statusReason}"/></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:level">
											Level</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<category:getoptionvalue categoryValueId="${aimEditActivityForm.levelId}"/>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:location">
											Location</digi:trn>
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
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:sector">
											Sector</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.activitySectors}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="sectors" items="${aimEditActivityForm.activitySectors}">
													<tr><td>
													<c:if test="${!empty sectors.sectorName}">
																				<c:out value="${sectors.sectorName}" />
																			</c:if>&nbsp;&nbsp; <c:if
																				test="${sector.sectorPercentage!=''}">
																				<c:if test="${sector.sectorPercentage!='0'}">
																			(<c:out value="${sectors.sectorPercentage}" />)%
																			</c:if>
																			</c:if> <c:if test="${!empty sectors.subsectorLevel1Name}">
														[<c:out value="${sectors.subsectorLevel1Name}"/>]
													</c:if>
													<c:if test="${!empty sectors.subsectorLevel2Name}">
														[<c:out value="${sectors.subsectorLevel2Name}"/>]
													</c:if>
													</td>
														
													</tr>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:program">Program</digi:trn>
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.actPrograms!=null}">
                                            <c:if test="${!empty aimEditActivityForm.actPrograms}">
                                              <c:forEach var="tempPgm" items="${aimEditActivityForm.actPrograms}">
                                                <c:if test="${tempPgm!=null}">
                                                  <c:out value="${tempPgm.name}"/>
                                                </c:if>
                                              </c:forEach>
                                            </c:if>
                                          </c:if>
										</td>
                                    </tr>

									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:proposedPrjectCost">Proposed Project Cost</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.proProjCost!=null}">
                                                  <table cellSpacing=1 cellPadding="3" bgcolor="#aaaaaa" width="100%">
                                                      <tr bgcolor="#ffffff">
																		  <td>
																		  			Cost
																		  </td>
                                                        <td bgcolor="#FFFFFF" align="left" >
                                                          <c:if test="${aimEditActivityForm.proProjCost.funAmount!=null}">
																			 	<FONT color=blue>*</FONT>
                                                            ${aimEditActivityForm.proProjCost.funAmount}
                                                          </c:if>&nbsp;
																			 <c:if test="${aimEditActivityForm.proProjCost.currencyCode!=null}">
                                                            ${aimEditActivityForm.proProjCost.currencyCode}
                                                          </c:if>
                                                        </td>
																		  </tr>
																		  <tr bgcolor="#ffffff">
																		  <td>
																		  	Proposed	Completion Date
																		  </td>
                                                        <td bgcolor="#FFFFFF" align="left" width="150">
                                                          <c:if test="${aimEditActivityForm.proProjCost.funDate!=null}">
                                                             ${aimEditActivityForm.proProjCost.funDate}
                                                          </c:if>
                                                        </td>
                                                      </tr>
                                                    </table>
                                             </c:if>
										</td>	
										
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:meActivityPerformance">
											Activity - Performance</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<% if (actPerfChartUrl != null) { %>
												<img src="<%= actPerfChartUrl %>" width=370 height=450 border=0 usemap="#<%= actPerfChartFileName %>"><br><br>
											<% } else { %>
												<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
											    <digi:trn key="aim:activityPerformanceChart">Activity-Performance chart</digi:trn>
											    </span><br><br>
											<% } %>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:meActivityRisk">
											Activity - Risk</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<% if (actRiskChartUrl != null) { %>
												<bean:define id="riskColor" name="riskColor" scope="request" toScope="page" type="java.lang.String"/>
												<bean:define id="riskName" name="overallRisk" scope="request" toScope="page" type="java.lang.String"/>
												<digi:trn key="aim:overallActivityRisk">Overall Risk</digi:trn>: 
												<font color="<bean:write name="riskColor" />">
												
												<b><digi:trn key="<%=riskName%>"><%=riskName%></digi:trn></b>
										
												<img src="<%= actRiskChartUrl %>" width=370 height=350 border=0 usemap="#<%= actRiskChartFileName %>">
												<br><br>
											<% } else { %>
												<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
										  	    <digi:trn key="aim:activityRiskChart">Activity-Risk chart</digi:trn>
											    </span><br><br>
											<% } %>
										</td>
									</tr>
									
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:funding">
											Funding</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.fundingOrganizations}">
												<table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#aaaaaa">
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


																						<c:if test="${aimEditActivityForm.donorFlag == true}">
																						<c:if test="${fundDet.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																						</c:if>
																						<c:if test="${fundDet.perspectiveCode == 'DN'}">
																						<tr bgcolor="#ffff00">
																						</c:if>
																							<td width="50">
																								<c:out value="${fundDet.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fundDet.transactionAmount}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.currencyCode}"/>
																							</td>
																							<td width="70">
																								<c:out value="${fundDet.transactionDate}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.perspectiveName}"/>
																							</td>
																						</tr>
																						</c:if>
																						<c:if test="${aimEditActivityForm.donorFlag == false}">
																						<c:if test="${fundDet.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<c:out value="${fundDet.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fundDet.transactionAmount}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.currencyCode}"/>
																							</td>
																							<td width="70">
																								<c:out value="${fundDet.transactionDate}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.perspectiveName}"/>
																							</td>
																						</tr>
																						</c:if>
																						</c:if>


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
																						<c:if test="${aimEditActivityForm.donorFlag == true}">
																						<c:if test="${fundDet.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																						</c:if>
																						<c:if test="${fundDet.perspectiveCode == 'DN'}">
																						<tr bgcolor="#ffff00">
																						</c:if>
																							<td width="50">
																								<c:out value="${fundDet.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fundDet.transactionAmount}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.currencyCode}"/>
																							</td>
																							<td width="70">
																								<c:out value="${fundDet.transactionDate}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.perspectiveName}"/>
																							</td>
																						</tr>
																						</c:if>
																						<c:if test="${aimEditActivityForm.donorFlag == false}">
																						<c:if test="${fundDet.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<c:out value="${fundDet.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fundDet.transactionAmount}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.currencyCode}"/>
																							</td>
																							<td width="70">
																								<c:out value="${fundDet.transactionDate}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.perspectiveName}"/>
																							</td>
																						</tr>
																						</c:if>
																						</c:if>
																						</c:if>
																					</table>
																				   </c:forEach>
																				</td></tr>
																			</table>
																			</td></tr>
																			<tr><td colspan="3">
																			<table width="100%" cellSpacing=1 cellPadding=1 bgcolor="#dddddd">
																				<tr><td valign="top" width="100" bgcolor="#ffffff">
																					<digi:trn key="aim:expenditures">
																					Expenditures</digi:trn>
																				</td>
																				<td bgcolor="#ffffff">
																					<c:forEach var="fundDet" items="${fund.fundingDetails}">
																					<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																						<c:if test="${fundDet.transactionType == 2}">
																						<c:if test="${aimEditActivityForm.donorFlag == true}">
																						<c:if test="${fundDet.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																						</c:if>
																						<c:if test="${fundDet.perspectiveCode == 'DN'}">
																						<tr bgcolor="#ffff00">
																						</c:if>
																							<td width="50">
																								<c:out value="${fundDet.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fundDet.transactionAmount}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.currencyCode}"/>
																							</td>
																							<td width="70">
																								<c:out value="${fundDet.transactionDate}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.perspectiveName}"/>
																							</td>
																						</tr>
																						</c:if>
																						<c:if test="${aimEditActivityForm.donorFlag == false}">
																						<c:if test="${fundDet.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<c:out value="${fundDet.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fundDet.transactionAmount}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.currencyCode}"/>
																							</td>
																							<td width="70">
																								<c:out value="${fundDet.transactionDate}"/>
																							</td>
																							<td>
																								<c:out value="${fundDet.perspectiveName}"/>
																							</td>
																						</tr>
																						</c:if>
																						</c:if>
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
									<!-- Costing -->
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:costing">
											Costing</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											&nbsp;&nbsp;&nbsp;
											<table width="100%">
												<tr>
													<td>
														<bean:define id="mode" value="preview" type="java.lang.String" toScope="request" />
														<jsp:include page="viewCostsSummary.jsp" flush="" />
													</td>
												</tr>
											</table>
											
										</td>
									</tr>
									<!-- End Costing -->
									
									<logic:notEmpty name="SA" scope="application">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:regionalFunding">
											Regional Funding</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.regionalFundings}">
												<table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#aaaaaa">
												<c:forEach var="regFunds" items="${aimEditActivityForm.regionalFundings}">
													<tr><td bgcolor="#ffffff">
														<table width="100%" cellSpacing="1" cellPadding="1">
															<tr><td bgcolor="#ffffff"><b>
																<c:out value="${regFunds.regionName}"/></b>
															</td></tr>
															<c:if test="${!empty regFunds.commitments}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:commitments">
																				Commitments</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.commitments}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty regFunds.disbursements}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:disbursements">
																				Disbursements</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.disbursements}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty regFunds.expenditures}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:expenditures">
																				Expenditures</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.expenditures}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
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
									</logic:notEmpty>
									
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:relatedOrganizations">
											Related Organizations</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<b><digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="impAgencies">
												<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="impAgencies"
													id="impAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<ul><li> <bean:write name="impAgencies" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/>
										
											<b><digi:trn key="aim:beneficiary2Agency">Beneficiary Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="benAgencies">
												<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="benAgencies"
													id="benAgency" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<ul><li> <bean:write name="benAgency" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/>
											<b><digi:trn key="aim:contracting2Agency">Contracting Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="conAgencies">
												<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="conAgencies"
													id="conAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
														<ul><li> <bean:write name="conAgencies" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/>
										</td>
									</tr>									
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:components">
											Components</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.selectedComponents}">
												<c:forEach var="comp" items="${aimEditActivityForm.selectedComponents}">
													<table width="100%" cellSpacing="1" cellPadding="1">
													<tr><td>
														<table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">

															<tr><td><b>
																<c:out value="${comp.title}"/></b>
															</td></tr>
															<tr><td>
																<i>
																<digi:trn key="aim:description">Description</digi:trn> :</i>
																<c:out value="${comp.description}"/>
															</td></tr>
															<tr><td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>
															</td></tr>
															<c:if test="${!empty comp.commitments}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:commitments">
																				Commitments</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.commitments}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																								<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty comp.disbursements}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:disbursements">
																				Disbursements</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.disbursements}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																								<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty comp.expenditures}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:expenditures">
																				Expenditures</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.expenditures}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right">
																								<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td>
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td>
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<tr><td bgcolor="#ffffff">
																<FONT color=blue>*
																	<digi:trn key="aim:theAmountEnteredAreInThousands">
																		The amount entered are in thousands (000)
		  															</digi:trn>
																</FONT>
															</td></tr>
															<tr><td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:physicalProgressOfTheComponent">
																Physical progress of the component</digi:trn></b>
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
														</table>
													</td></tr>
													</table>
												</c:forEach>
											</c:if>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:issues">
											Issues</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.issues}">
												<table width="100%" cellSpacing="2" cellPadding="2" border=0>
												<c:forEach var="issue" items="${aimEditActivityForm.issues}">
													<tr><td valign="top">
														<li class="level1"><b><c:out value="${issue.name}"/></b></li>
													</td></tr>
													<c:if test="${!empty issue.measures}">
														<c:forEach var="measure" items="${issue.measures}">
															<tr><td>
																<li class="level2"><i><c:out value="${measure.name}"/></i></li>
															</td></tr>
															<c:if test="${!empty measure.actors}">
																<c:forEach var="actor" items="${measure.actors}">
																	<tr><td>
																		<li class="level3"><c:out value="${actor.name}"/></li>
																	</td></tr>
																</c:forEach>
															</c:if>
														</c:forEach>
													</c:if>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:relatedDocuments">
											Related Documents</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.documentList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												 <logic:iterate name="aimEditActivityForm"  property="documents" 
													id="docs" type="org.digijava.module.aim.helper.Documents">
													<c:if test="${docs.isFile == true}">
													<tr><td>
													 <table width="100%" class="box-border-nopadding">
													 	<tr bgcolor="#ffffff">
															<td vAlign="center" align="left">
																&nbsp;<b><c:out value="${docs.title}"/></b> -
																&nbsp;&nbsp;&nbsp;<i><c:out value="${docs.fileName}"/></i>
																<logic:notEqual name="docs" property="docDescription" value=" ">
																	<br />&nbsp;
																	<b>Description:</b>&nbsp;<bean:write name="docs" property="docDescription" />
																</logic:notEqual>
																<logic:notEmpty name="docs" property="date">
																	<br />&nbsp;
																	<b>Date:</b>&nbsp;<c:out value="${docs.date}"/>
																</logic:notEmpty>
																<logic:notEmpty name="docs" property="docType">
																	<br />&nbsp;
																	<b>Document Type:</b>&nbsp;
																	<bean:write name="docs" property="docType"/>
																</logic:notEmpty>
															</td>
														</tr>
													 </table>
													</td></tr>
													</c:if>
													</logic:iterate>
												</table>
											</c:if>
											<c:if test="${!empty aimEditActivityForm.linksList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												<c:forEach var="docList" items="${aimEditActivityForm.linksList}">
					   							<bean:define id="links" name="docList" property="relLink" />
													<tr><td>
														<table width="100%" class="box-border-nopadding">
															<tr>
																<td width="2">
																	<digi:img src="module/aim/images/web-page.gif"/>
																</td>
																<td align="left" vAlign="center">&nbsp;
																	<b><c:out value="${links.title}"/></b> -
																	&nbsp;&nbsp;&nbsp;<i><a href="<c:out value="${links.url}"/>">
																	<c:out value="${links.url}"/></a></i>
																	<br>&nbsp;
																	<b>Desc:</b>&nbsp;<c:out value="${links.description}"/>
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
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
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
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
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
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:contractors">
											Contractors</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.contractors}"/>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
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
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
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
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:AccessionInstrument">Accession Instrument</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.accessionInstrument}"/>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:acChapter"> A.C. Chapter</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.acChapter}"/>
										</td>
									</tr>
									
									
									
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:activityCreatedBy">
											Activity created by</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.actAthFirstName}"/>
											<c:out value="${aimEditActivityForm.actAthLastName}"/> -
											<c:out value="${aimEditActivityForm.actAthEmail}"/>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:dataSource">
											Data Source</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.actAthAgencySource}"/>
										</td>
									</tr>									
									<logic:notEmpty name="aimEditActivityForm" property="updatedDate">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:activityUpdatedOn">
											Activity updated on</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.updatedDate}"/>
										</td>
									</tr>	
									</logic:notEmpty>
									<logic:notEmpty name="aimEditActivityForm" property="updatedBy">									
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:activityUpdatedBy">
											Activity updated by</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.updatedBy.user.firstNames}"/>										
											<c:out value="${aimEditActivityForm.updatedBy.user.lastName}"/>	-
											<c:out value="${aimEditActivityForm.updatedBy.user.email}"/>
										</td>
									</tr>																	
									</logic:notEmpty>
									
									<logic:notEmpty name="aimEditActivityForm" property="createdDate">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:activityCreatedOn">
											Activity created on</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.createdDate}"/>
										</td>
									</tr>
									
									</logic:notEmpty>
									<logic:notEmpty name="aimEditActivityForm" property="team">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:activityTeamLeader">
											Data Team Leader</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.team.teamLead.user.firstNames}"/>										
											<c:out value="${aimEditActivityForm.team.teamLead.user.lastName}"/>	-
											<c:out value="${aimEditActivityForm.team.teamLead.user.email}"/>

										</td>
									</tr>
									</logic:notEmpty>
									<c:if test="${aimEditActivityForm.pageId == 1}">
									<tr><td bgColor=#ffffff align="center" colspan=2>
										<table cellPadding=3>
											<tr>
												<c:if test="${aimEditActivityForm.donorFlag == true}">
												<td>
													<input type="button" class="dr-menu" onclick="javascript:history.go(-1)" value='<< <digi:trn key="btn:back">Back</digi:trn>'name="backButton"/>


												</td>
												</c:if>
												<c:if test="${aimEditActivityForm.donorFlag == false}">
												<td>
													<input type="button" class="dr-menu" onclick="javascript:history.go(-1)" value='<< <digi:trn key="btn:back">Back</digi:trn>'name="backButton"/>
												</td>
												</c:if>
												<td>
													<input type="button" class="dr-menu" onclick="disable()" value='<digi:trn key="btn:saveActivity">Save Activity</digi:trn>' name="submitButton"/>

												</td>
											</tr>
										</table>
									</td></tr>
									</c:if>
									<c:if test="${aimEditActivityForm.pageId > 2}">
									<tr><td bgColor=#ffffff align="center" colspan=2>
										<input type="button" class="dr-menu" onclick="javascript:history.go(-1)" value='<digi:trn key="btn:back">Back</digi:trn>' />
									</td></tr>
									</c:if>
								</table>
							</td></tr>
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
