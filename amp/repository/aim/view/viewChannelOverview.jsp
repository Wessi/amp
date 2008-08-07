<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ page import="org.digijava.module.aim.helper.CategoryConstants" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<jsp:include page="previewLogframeUtil.jsp" flush="true" />

<script type="text/javascript">

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.aimChannelOverviewForm.action = "<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~activityId=" + id + "~actId=" + id;
	document.aimChannelOverviewForm.target = "_self";
    document.aimChannelOverviewForm.submit();
}

function fnEditProject1(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.aimChannelOverviewForm.action = "<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~debugFM=true~activityId=" + id + "~actId=" + id;
	document.aimChannelOverviewForm.target = "_self";
    document.aimChannelOverviewForm.submit();
}

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	var url ="<%=ficheUrl%>~ampActivityId=" + id;
	openURLinWindow(url,650,500);
}


function preview(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
   document.aimChannelOverviewForm.action = "<%=addUrl%>~pageId=2~activityId=" + id+"~isPreview=" +1;
	document.aimChannelOverviewForm.target = "_self";
   document.aimChannelOverviewForm.submit();
}

function login()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/login.do" />
    document.aimChannelOverviewForm.action = "<%=addUrl%>";
    document.aimChannelOverviewForm.submit();
}

function fnDeleteProject()
{
<c:set var="translation">
	<digi:trn key="aim:deleteRecord">Are you sure you want to delete the record</digi:trn>
</c:set>
	var name=confirm("${translation}")
	if (name==true)
	{ 
		<digi:context name="addUrl" property="context/module/moduleinstance/deleteAmpActivity.do" />
	    document.aimChannelOverviewForm.action = "<%=addUrl%>";
		document.aimChannelOverviewForm.submit();
	}
	else
	{
		<digi:context name="addUrl" property="context/module/moduleinstance/viewChannelOverview.do" />
	    document.aimChannelOverviewForm.action = "<%=addUrl%>";
		document.aimChannelOverviewForm.submit();

	}
}

function commentWin(val) {
	openNewWindow(600, 400);
	<digi:context name="commurl" property="context/module/moduleinstance/viewComment.do" />
	url = "<%=commurl %>~comment=viewccd~previus=vco~actId=" + val;
	document.aimChannelOverviewForm.action = url;
	document.aimChannelOverviewForm.currUrl1.value = "<%=commurl%>";
	document.aimChannelOverviewForm.target = popupPointer.name;
	document.aimChannelOverviewForm.submit();
}

</script>


<digi:instance property="aimChannelOverviewForm" />
<digi:form action="/viewChannelOverview.do"
	name="aimChannelOverviewForm"
	type="org.digijava.module.aim.form.ChannelOverviewForm" method="post">


	<html:hidden property="id" styleId="actId" />
	<input type="hidden" name="currUrl1">

	<logic:equal name="aimChannelOverviewForm" property="validLogin"
		value="false">
		<h3 align="center">Invalid Login. Please Login Again.</h3>
		<p align="center"><html:submit styleClass="dr-menu" value="Log In"
			onclick="login()" /></p>
	</logic:equal>

	<logic:equal name="aimChannelOverviewForm" property="validLogin"
		value="true">
		<TABLE cellSpacing=0 cellPadding=0 align="left" vAlign="top" border=0
			width=100%>
			<TR>
				<TD vAlign="top" align="center"><!-- contents -->

				<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top"
					align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
					<TR>
						<TD bgcolor="#f4f4f4"><c:set var="activity"
							value="${aimChannelOverviewForm.activity}" />

						<TABLE width="100%" cellSpacing=1 cellPadding=3 vAlign="top"
							align="center" bgcolor="#f4f4f4">
							<TR bgColor=#f4f4f2>
								<TD align=left>
								<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left"
									vAlign="top">
									<TR>
										<TD align="left"><SPAN class=crumb> <jsp:useBean
											id="urlChannelOverview" type="java.util.Map"
											class="java.util.HashMap" /> <c:set
											target="${urlChannelOverview}" property="ampActivityId">
                                            ${aimChannelOverviewForm.id}

										</c:set> <c:set target="${urlChannelOverview}" property="tabIndex"
											value="0" /> <c:set var="translation">
											<digi:trn key="aim:clickToViewChannelOverview">Click here to view Channel Overview</digi:trn>
										</c:set> <digi:link href="/viewChannelOverview.do"
											name="urlChannelOverview" styleClass="comment"
											title="${translation}">
											<digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
										</digi:link>&nbsp;&gt;&nbsp;<digi:trn key="aim:actOverview">Overview</digi:trn>										
										</SPAN></TD>
										<TD align="right">
										<table>
											<tr>
												<module:display name="Previews"
													parentModule="PROJECT MANAGEMENT">
													<feature:display name="Preview Activity" module="Previews">
														<field:display feature="Preview Activity" name="Preview Button">
															<td><input type="button"
																value="<digi:trn key='btn:preview'>Preview</digi:trn>"
																class="dr-menu"
																onclick="preview(${aimChannelOverviewForm.id})"></td>

														</field:display>
													</feature:display>
												</module:display>
												<module:display name="Previews"
													parentModule="PROJECT MANAGEMENT">
													<feature:display name="Edit Activity" module="Previews">
														<field:display feature="Edit Activity" name="Edit Activity Button">
															 <td>              
																				<!-- 
                                                                               <c:if test="${aimChannelOverviewForm.buttonText != 'validate'}">              
                                                                               <c:if test="${sessionScope.currentMember.teamAccessType != 'Management'}"> 
                                                                                 -->  
                                                                                       <input type="button" value="<digi:trn key='btn:edit'>Edit</digi:trn>" 
                                                                                       class="dr-menu" onclick="fnEditProject(${activity.activityId})">
                                                                               <!--       
                                                                               </c:if>                                                                 
                                                                                     
                                                                               </c:if>
                                                                                --> &nbsp;                                                                
                                                                       </td>      
														</field:display>
													</feature:display>
												</module:display>
												
												<module:display name="Previews"
													parentModule="PROJECT MANAGEMENT">
													<feature:display name="Edit Activity" module="Previews">
														<field:display feature="Edit Activity" name="Validate Activity Button">
    														<c:if
																test="${aimChannelOverviewForm.buttonText == 'validate'}">
																 <c:if test="${sessionScope.currentMember.teamAccessType != 'Management'}"> 
																
																<td><html:button styleClass="dr-menu"
																	onclick="fnEditProject(${aimChannelOverviewForm.id})"
																	property="validateBtn">
																	<digi:trn key="aim:validate">Validate</digi:trn>
																</html:button></td>
 															</c:if>	

															</c:if>  
															<%--<c:if test="${aimChannelOverviewForm.buttonText == 'approvalAwaited'}">
							                                        	<td >
							                                        		<html:button  styleClass="FONT-SIZE: 11px; COLOR: #313131" disabled="true" property="validateBtn">
																				<digi:trn key="aim:approvalAwaited">Approval Awaited</digi:trn>
																			</html:button>
							                                        	</td>

																	</c:if> --%>
														</field:display>
													</feature:display>
												</module:display>

												<module:display name="Previews"
													parentModule="PROJECT MANAGEMENT">
													<feature:display name="Logframe" module="Previews">
														<field:display name="Logframe Preview Button"
															feature="Logframe">
															<td><input type="button"
																value="<digi:trn key="logframeBtn:previewLogframe">Preview Logframe</digi:trn>"
																class="dr-menu"
																onclick="previewLogframe(${aimChannelOverviewForm.id})">
															</td>

														</field:display>
													</feature:display>
												</module:display>

												<module:display name="Previews"
													parentModule="PROJECT MANAGEMENT">
													<feature:display name="Project Fiche" module="Previews">
														<field:display name="Project Fiche Button"
															feature="Project Fiche">

															<td><input type='button'
																value='<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>'
																class='dr-menu'
																onclick='projectFiche(${aimChannelOverviewForm.id})'>
															</td>

														</field:display>
													</feature:display>
												</module:display>

											</tr>
										</table>
										</TD>
									</TR>
								</TABLE>
								</TD>
							</TR>

							<TR bgColor=#f4f4f2 >
								<TD vAlign="top" align="center" width="100%">
								<font color="#FF0000">
								<!--  FFerreyra: This was added to handle errors between different forms while keeping translation tags possible -->
								<logic:present name="<%=org.apache.struts.action.Action.ERROR_KEY%>">
									<bean:define id="errors" name="<%=org.apache.struts.action.Action.ERROR_KEY%>" type="org.apache.struts.action.ActionErrors"/>
									<ul>
										<logic:iterate collection="<%=errors.get()%>" id="error" type="org.apache.struts.action.ActionError">
										<li>
											<digi:trn key="${error.key}"><bean:message key="<%=error.getKey()%>"/></digi:trn>
										</li>
										</logic:iterate>
									</ul>
								</logic:present>
								
                            		<logic:iterate id="element" name="aimChannelOverviewForm" property="errors">
                               			<digi:trn key="${element.key}">
                                   			<bean:write name="element" property="value"/>                                   			
                               			</digi:trn>
                           			</logic:iterate>
                       				<logic:iterate id="element" name="aimChannelOverviewForm" property="messages">
                               			<digi:trn key="${element.key}">
                                   			<bean:write name="element" property="value"/>
                               		</digi:trn>
                           			</logic:iterate>
                   				</font>
								</TD>
							</TR>
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="left" width="100%">
								<TABLE width="100%" cellPadding=2 cellSpacing=2 vAlign="top"
									align="left" bgColor=#f4f4f2>
									<TR>
										<TD width="100%"><IMG height=10
											src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<c:if test="${activity.status == 'Planned'}">
											<b><digi:trn key="aim:plannedCommitment">
										Planned Commitment</digi:trn> : </b>
										</c:if> <c:if test="${activity.status != 'Planned'}">
											<b><digi:trn key="aim:totalCommitmentsActual">
										Total Commitments(Actual)</digi:trn> : </b>
										</c:if> ${aimChannelOverviewForm.grandTotal}
										${aimChannelOverviewForm.currCode} 

<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
										<FONT color="blue">
										( <digi:trn key="aim:enteredInThousands">Entered in thousands 000</digi:trn>)
										</FONT>
</gs:test>
										</TD>
									</TR>
								</TABLE>
								</TD>
							</TR>
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="100%">
								<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top"
									align="center" bgColor=#f4f4f2>

									<TR>
										<TD width="100%" bgcolor="#F4F4F2" align="center">
										<TABLE width="100%" cellPadding="2" cellSpacing="2"
											vAlign="top" align="center" bgColor=#f4f4f2
											class="box-border-nopadding">
											<TR>
												<TD width="50%" vAlign="top" align="left">
												<TABLE width="100%" cellPadding="3" cellSpacing="1"
													vAlign="top" align="left">
													<module:display name="Project ID and Planning"
														parentModule="PROJECT MANAGEMENT">
														<feature:display name="Planning"
															module="Project ID and Planning">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding=2 cellSpacing=1
																	vAlign="top" align="center" bgcolor="#FFFFFF">
																	<field:display name="AMP ID" feature="Identification">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18" colspan="2">&nbsp; <IMG
																			height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																			<b>
																	  		<digi:trn key="aim:ampId">AMP ID</digi:trn></b>
																	  	</td>
																	  </TR>
																	<TR>
																		<TD bgcolor="#ffffff" colspan="2">&nbsp;&nbsp;&nbsp;
																	  		<c:out value="${activity.ampId}" />
																		</td>
																	</TR>
																	</field:display> 
																	<field:display name="Organizations and Project ID" feature="Identification">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18" colspan="2">&nbsp; <IMG
																			height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																		<b>
																		<digi:trn key="aim:orgsAndProjectIds">Organizations and Project IDs</digi:trn></b>
																	  </td></TR>
																		<c:forEach items="${aimChannelOverviewForm.selectedOrganizations}" var="selectedOrganizations">
																		<tr>
																			<c:if test="${!empty selectedOrganizations.id}">
																				<td width="75%">
																					<li><i><c:out value="${selectedOrganizations.organisation.name}"/></i></li>
																			  </td>
																	
																				<td width="25%" align="center">
																					<i>
																				  <c:out value="${selectedOrganizations.projectId}"/>
																					</i></td>
																			</c:if>
																		</tr>
																	</c:forEach>
																	</field:display>
																</TABLE>
																</TD>
															</TR>

															<field:display name="Status" feature="Planning">
																<TR>
																	<TD>
																	<TABLE width="100%" cellPadding=2 cellSpacing=1
																		vAlign="top" align="top" bgcolor="#FFFFFF">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn key="aim:status">Status</digi:trn></b></TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff"><c:out
																				value="${activity.status}" /></TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff"><i><digi:trn
																				key="aim:reason">Reason</digi:trn></i>: <c:out
																				value="${activity.statusReason}" /></TD>
																		</TR>
																	</TABLE>
																	</TD>
																</TR>
															</field:display>
														</feature:display>
														<feature:display name="Budget" module="Project ID and Planning">
															<TR>
																<TD>
																	<TABLE width="100%" cellPadding=2 cellSpacing=1
																		vAlign="top" align="top" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn key="aim:actBudget">Budget</digi:trn></b>
																			</TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff"><c:if
																				test="${activity.budget}">
																				<table>
																					<tr>
																						<td><digi:trn key="aim:actBudgeton">
																							Activity is On Budget
																						</digi:trn></td>
																					</tr>
																					<field:display name="FY" feature="Budget">
																						<tr>
																							<td><digi:trn key="aim:actFY">
																					FY
																					</digi:trn>: ${activity.FY}</td>
																						</tr>
																					</field:display>
																					<field:display name="Vote" feature="Budget">
																						<tr>
																							<td><digi:trn key="aim:actVote">
																					Vote
																					</digi:trn>: ${activity.vote}</td>
																						</tr>
																					</field:display>
																					<field:display name="Sub-Vote" feature="Budget">
																						<tr>
																							<td><digi:trn key="aim:actSub-Vote">
																					Sub-Vote
																					</digi:trn>: ${activity.subVote}</td>
																						</tr>
																					</field:display>
																					<field:display name="Sub-Program" feature="Budget">
																						<tr>
																							<td><digi:trn key="aim:actSub-Program">
																					Sub-Program
																					</digi:trn>: ${activity.subProgram}</td>
																						</tr>
																					</field:display>
																					<field:display name="Project Code" feature="Budget">
																						<tr>
																							<td><digi:trn key="aim:actProjectCode">
																					Project Code
																					</digi:trn>: ${activity.projectCode}</td>
																						</tr>
																					</field:display>
																					<field:display name="Financial Instrument"
																						feature="Budget">
																						<tr>
																							<td><digi:trn key="aim:actGBS">
																					Financial Instrument
																					</digi:trn>: <!-- this part is to be replaced when the category manager is ready -->
																							${activity.financialInstrument}</td>
																						</tr>
																					</field:display>
																					<field:display
																						name="Government Approval Procedures"
																						feature="Budget">
																						<tr>
																							<td><digi:trn
																								key="aim:actGovernmentApprovalProcedures">
																					Government Approval Procedures
																					</digi:trn>: <c:if
																								test="${activity.governmentApprovalProcedures==true}">
																						Yes
																					</c:if> <c:if
																								test="${activity.governmentApprovalProcedures==false || activity.governmentApprovalProcedures==''}">
																						No
																					</c:if></td>
																						</tr>
																					</field:display>
																					<field:display name="Joint Criteria"
																						feature="Budget">
																						<tr>
																							<td><digi:trn key="aim:actJointCriteria">
																					Joint Criteria
																					</digi:trn>: <c:if test="${activity.jointCriteria==true}">
																								<c:out value="Yes" />
																							</c:if> <c:if
																								test="${activity.jointCriteria==false || activity.jointCriteria==''}">
																								<c:out value="No" />
																							</c:if></td>
																						</tr>
																					</field:display>
																				</table>
																			</c:if> <c:if test="${!activity.budget}">
																				<digi:trn key="aim:actBudgetoff">
																						Activity is Off Budget
																				</digi:trn>
																			</c:if> <c:if test="${empty activity.budget}">
																				<digi:trn key="aim:actBudgetoff">
																						Activity is Off Budget
																				</digi:trn>
																			</c:if></TD>
																		</TR>

																	</TABLE>
																	</TD>
																</TR>
														</feature:display>
														<feature:display module="Project ID and Planning" name="Sectors">
															<field:display feature="Sectors" name="Level 1 Sectors List">
																<TR>
																	<TD>
																	<TABLE width="100%" cellPadding=2 cellSpacing=1
																		vAlign="top" align="top" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn key="aim:sector">Sector</digi:trn></b></TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff">
												                            <c:forEach var="config" items="${aimChannelOverviewForm.classificationConfigs}" varStatus="ind">
																				<bean:define id="emptySector" value="Sector"></bean:define>
																				<c:if test="${config.name== 'Secondary' }">
																					<bean:define id="auxSectorType" value="Secondary Sector"></bean:define>
																				</c:if>
																				<c:if test="${config.name== 'Primary' }">
																					<bean:define id="auxSectorType" value="Primary Sector"></bean:define>
																				</c:if>
																				 <field:display name="${auxSectorType}" feature="Sectors">
																				<c:set var="hasSectors">
																					false
																				</c:set>

																				<c:forEach var="actSect" items="${activity.sectors}">
																					<c:if test="${actSect.configId==config.id}">
																						<c:set var="hasSectors">
																							true
																						</c:set>
																					</c:if>
																				</c:forEach>
																				<c:if test="${hasSectors}">
												                                <strong>
													                               	<digi:trn key="aim:addactivitysectors:${auxSectorType }">
													                                <c:out value="${auxSectorType }"/>
													                                </digi:trn>
													                                </strong>
												                                </c:if>
										                                        <c:if test="${!empty activity.sectors}">
																					<ul>
																						<c:forEach var="actSect" items="${activity.sectors}">
						                                                            		<c:if test="${actSect.configId==config.id}">
																								<li>
																									<field:display name="Sector Scheme Name" feature="Sectors">
																										<c:out value="${actSect.sectorScheme}" />
																										<br/>&nbsp;
																										<IMG src="../ampTemplate/images/link_out_bot.gif"/>
																									</field:display>
																									<field:display name="${auxSectorType}" feature="Sectors">
																										<c:out value="${actSect.sectorName}" />
																									</field:display>
																									<c:if test="${!empty actSect.subsectorLevel1Name}">
																										<field:display name="${auxSectorType} Sub-Sector" feature="Sectors">
																										<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<IMG
																											src="../ampTemplate/images/link_out_bot.gif"/>
																										<c:out value="${actSect.subsectorLevel1Name}" />
																										</field:display>
																										<c:if test="${!empty actSect.subsectorLevel2Name}">
																										<field:display name="${auxSectorType} Sub-Sub-Sector" feature="Sectors">
																											 <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<IMG
																												src="../ampTemplate/images/link_out_bot.gif"/>
																											<c:out value="${actSect.subsectorLevel2Name}" />
																											&nbsp;
																										</field:display>
																										</c:if>
																									</c:if>
																									<field:display name="Percentage" feature="Sectors">
				                                                                                    <logic:present name="actSect" property="sectorPercentage">
																										<c:if test="${actSect.sectorPercentage!=0}">
																											(<c:out value="${actSect.sectorPercentage}" />%)
																										</c:if>
																									</logic:present>
																									</field:display>

																								</li>
																								</c:if>
																						</c:forEach>
																					</ul>
										                                        </c:if>
											                                    </field:display>
																			</c:forEach>
																			
																			</TD>
																		</TR>
																	</TABLE>
																	</TD>
																</TR>

															</field:display>
														</feature:display>

														<feature:display module="Project ID and Planning"
															name="Location">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding=2 cellSpacing=1
																	vAlign="top" align="left" bgcolor="#aaaaaa">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																			height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																		<b><digi:trn key="aim:location">Location</digi:trn></b>
																		</TD>
																	</TR>
																	<TR>
																		<TD bgcolor="#ffffff">
																		<TABLE width="100%" cellSpacing="0" cellPadding="0"
																			vAlign="top" align="left" bgcolor="#ffffff">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<field:display name="Implementation Level"
																						feature="Location">
																						<TR>
																							<TD width="100%" colspan="4" align="left" bgcolor="#ffffff">
																								<i><digi:trn key="aim:impLevel">Implementation Level</digi:trn></i>
																								 :&nbsp;<c:out value="${activity.impLevel}"/>
																							</TD>
																						</TR>
																					</field:display>

																					<TD width="100%" colspan="4" align="left" bgcolor="#ffffff">
																						<i>
																							<digi:trn key="aim:impLocations">Implementation Location</digi:trn>
																							:&nbsp;
																						</i>
																						<c:out value="${aimChannelOverviewForm.implemLocationLevel}" />

																					</TD>

																					<c:if test="${!empty activity.locations}">
                                                                                                                                                                             <c:if test="${aimChannelOverviewForm.implLocationCountry}" >
                                                                                                                                                                 
																						<TR>
																							<TD width="30%" align="center" bgcolor="#ffffff">
																								<c:if test="${aimChannelOverviewForm.numImplLocationLevels > 1}" >
																									<i>
																									<category:getoptionvalue categoryIndex="1" categoryKey="<%=CategoryConstants.IMPLEMENTATION_LOCATION_KEY %>"  />
																									</i>
																								</c:if>
																								&nbsp;
																							</TD>
																							<TD width="30%" align="center" bgcolor="#ffffff">
																								<c:if test="${aimChannelOverviewForm.numImplLocationLevels > 2}" >
																									<i>
																									<category:getoptionvalue categoryIndex="2" categoryKey="<%=CategoryConstants.IMPLEMENTATION_LOCATION_KEY %>"  />
																									</i>
																								</c:if>
																								&nbsp;
																							</TD>
																							<TD width="30%" align="center" bgcolor="#ffffff">
																								<c:if test="${aimChannelOverviewForm.numImplLocationLevels > 3}" >
																									<i>
																									<category:getoptionvalue categoryIndex="3" categoryKey="<%=CategoryConstants.IMPLEMENTATION_LOCATION_KEY %>"  />
																									</i>
																								</c:if>
																								&nbsp;
																							</TD>
                                                                                              <TD  align="center" bgcolor="#ffffff">
                                                                                              <i> <digi:trn key="aim:percent">Percent</digi:trn></i>
																							</TD>
																							</TR>
																						
																						<c:forEach varStatus="varSt" var="loc" items="${activity.locations}">

																							
                                                                                            
																							
																							<c:if test="${not empty loc.region || not empty loc.zone ||not empty loc.woreda}">
																							
																							<c:set var="mapParam">${mapParam}${loc.region}/${loc.zone}/${loc.percent}</c:set>
																							<c:if test="${not varSt.last}">
																								<c:set var="mapParam">${mapParam}|</c:set>
																							</c:if>
																							
																							<TR>
																								<TD width="30%" align="center" bgcolor="#ffffff">
																									<c:out value="${loc.region}" />
																								</TD>

																								<TD width="30%" align="center" bgcolor="#ffffff">
																									<c:out value="${loc.zone}" />
																								</TD>
																								<TD width="30%" align="center" bgcolor="#ffffff">
																									<c:out value="${loc.woreda}" />
																								</TD>
                                                                                                  <TD  align="center" bgcolor="#ffffff">
                                                                                                     <c:if test='${loc.percent > 0}'>
																									<c:out value="${loc.percent}%" />
                                                                                                     </c:if>
																								</TD>
																							</TR>
                                                                                            </c:if>
																						</c:forEach>
																					 </c:if>
																					</c:if>
																					<!--commented by Sebastian Dimunzio when working on UI issues: This code Is not showing the image and can't hidde it by FM 
																					<tr>
																						<td colspan="4">
																							<img width="500" height="500" src="../../gis/getActivityMap.do?action=paintMap&mapCode=TZA&segmentData=${mapParam}">
																						</td>
																					</tr>
																					 -->
																				</TABLE>
																				</TD>
																			</TR>
																		</TABLE>
																		</TD>
																	</TR>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
													</module:display>

													<TR>
														<TD>
														<TABLE width="100%" cellPadding=3 cellSpacing=1
															vAlign="top" align="left" bgcolor="#aaaaaa">
															<module:display name="National Planning Dashboard"
																parentModule="NATIONAL PLAN DASHBOARD">
																<feature:display name="NPD Programs"
																	module="National Planning Dashboard">
																	<field:display name="National Plan Objective"
																		feature="NPD Programs">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn
																				key="aim:national Plan Objective">National Plan Objective</digi:trn></b></TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff">
																			<c:forEach var="program"
																				items="${aimChannelOverviewForm.nationalPlanObjectivePrograms}">
																				<c:out value="${program.hierarchyNames}" />&nbsp;
																				<c:out value="${program.programPercentage}"/>%<br/>
																			</c:forEach></TD>
																		</TR>
																	</field:display>
																	<field:display name="Primary Program"
																		feature="NPD Programs">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn
																				key="aim:primary Programs">Primary Programs</digi:trn></b></TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff"><c:forEach var="program"
																				items="${aimChannelOverviewForm.primaryPrograms}">
																				<c:out value="${program.hierarchyNames}" />&nbsp; <c:out
																					value="${program.programPercentage}" />%<br />
																			</c:forEach></TD>
																		</TR>
																	</field:display>

																	<field:display name="Secondary Program"
																		feature="NPD Programs">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn
																				key="aim:secondary Programs">Secondary Programs</digi:trn></b></TD>
																		</TR>
																	</field:display>
																	<TR>
																		<TD bgcolor="#ffffff"><c:forEach var="program"
																			items="${aimChannelOverviewForm.secondaryPrograms}">
																			<c:out value="${program.hierarchyNames}" />&nbsp; <c:out
																				value="${program.programPercentage}" />%<br />
																		</c:forEach></TD>
																	</TR>
																</feature:display>
															</module:display>
															<module:display name="Project ID and Planning"
																parentModule="PROJECT MANAGEMENT">
																<feature:display name="Identification"
																	module="Project ID and Planning">
																	<TR>
																		<TD bgcolor="#ffffff">
																		<field:display name="Description" feature="Identification">
																			<i><b><digi:trn key="aim:programDescription">Description</digi:trn></b></i>:
																			<c:if test='${!empty activity.description}'>
																				<digi:edit key="${activity.description}" />
																			</c:if>
																			<br />
																		</field:display>
																		<field:display feature="Identification" name="Objectives">
																			<field:display feature="Identification" name="Objective">
																			<i><b><digi:trn key="aim:programObjective">
																				Objective
																			</digi:trn></b></i>:
																			<c:if test='${!empty activity.objective}'>
																				<digi:edit key="${activity.objective}" />
																			</c:if>
																			</field:display>
																		
																			<ul>
																				<c:forEach var="comments"
																					items="${aimChannelOverviewForm.allComments}">
																					<field:display feature="Identification" name="Objective Assumption">
																						<c:if test='${comments.key=="Objective Assumption"}'>
																							<c:forEach var="comment" items="${comments.value}">
																								<li><i><digi:trn
																									key="aim:objectiveAssumption">Objective Assumption</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																					</field:display>
																					<field:display feature="Identification" name="Objective Verification">
																						<c:if test='${comments.key=="Objective Verification"}'>
																							<c:forEach var="comment" items="${comments.value}">
																								<li><i><digi:trn
																								key="aim:objectiveVerification">Objective Verification</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																					</field:display>
																					<field:display feature="Identification" name="Objective Objectively Verifiable Indicators">
																						<c:if test='${comments.key=="Objective Objectively Verifiable Indicators"}'>
																							<c:forEach var="comment" items="${comments.value}">
																								<li><i><digi:trn key="aim:objectivelyindicatorspreview">
																								Objectively Verifiable Indicators
																								</digi:trn>:
																								</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																					</field:display>
																				</c:forEach>
																			</ul>
																			</field:display>

																		<field:display name="Lessons Learned" feature="Identification">
																			<TR>
																				<TD bgcolor="#ffffff">
																					<i><b><digi:trn key="aim:Lessons Learned">Lessons Learned</digi:trn></b></i>:
																					<c:if test="${not empty activity.lessonsLearned}">
																						<bean:define id="lessonsLearnedKey">
																						<c:out value="${activity.lessonsLearned}"/>
																					</bean:define>
																						<digi:edit key="<%=lessonsLearnedKey%>"/>
																					 </c:if>
																				</TD>
																			</TR>

																		</field:display>
																		
											<bean:define id="largeTextFeature" value="Identification" toScope="request"/>																		
											<logic:notEmpty name="activity" property="projectImpact">			
											<bean:define id="largeTextLabel" value="Project Impact" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${activity.projectImpact}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyPopup.jsp"/>
											</logic:notEmpty>

											<logic:notEmpty name="activity" property="activitySummary">			
											<bean:define id="largeTextLabel" value="Activity Summary" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${activity.activitySummary}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyPopup.jsp"/>
											</logic:notEmpty>

											<logic:notEmpty name="activity" property="contractingArrangements">						
											<bean:define id="largeTextLabel" value="Contracting Arrangements" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${activity.contractingArrangements}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyPopup.jsp"/>
											</logic:notEmpty>

											<logic:notEmpty name="activity" property="condSeq">									
											<bean:define id="largeTextLabel" value="Conditionality and Sequencing" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${activity.condSeq}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyPopup.jsp"/>
											</logic:notEmpty>
			
											<logic:notEmpty name="activity" property="linkedActivities">												
											<bean:define id="largeTextLabel" value="Linked Activities" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${activity.linkedActivities}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyPopup.jsp"/>
											</logic:notEmpty>
			
											<logic:notEmpty name="activity" property="conditionality">									
											<bean:define id="largeTextLabel" value="Conditionalities" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${activity.conditionality}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyPopup.jsp"/>
											</logic:notEmpty>
			
											<logic:notEmpty name="activity" property="projectManagement">												
											<bean:define id="largeTextLabel" value="Project Management" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${activity.projectManagement}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyPopup.jsp"/>
											</logic:notEmpty>

																		<field:display feature="Identification"
																			name="Purpose">

																			<c:if test="${!empty activity.purpose}">
																				<i><b><digi:trn key="aim:programPurpose">Purpose</digi:trn></b></i>:
                                                                          <digi:edit
																					key="${activity.purpose}" />
																				<ul>
																					<c:forEach var="comments"
																						items="${aimChannelOverviewForm.allComments}">
																						<c:if test='${comments.key=="Purpose Assumption"}'>
																							<c:forEach var="comment"
																								items="${comments.value}">
																								<li><i><digi:trn
																									key="aim:purposeAssumption">Purpose Assumption</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																						<c:if
																							test='${comments.key=="Purpose Verification"}'>
																							<c:forEach var="comment"
																								items="${comments.value}">
																								<li><i><digi:trn
																									key="aim:purposeVerification">Purpose Verification</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																					</c:forEach>
																				</ul>
																			</c:if>
																		</field:display> <field:display feature="Identification"
																			name="Results">

																			<c:if test="${!empty activity.results}">
																				<i><b><digi:trn key="aim:programResults">Results</digi:trn></b></i>:
                                                                            <digi:edit
																					key="${activity.results}" />
																				<ul>
																					<c:forEach var="comments"
																						items="${aimChannelOverviewForm.allComments}">
																						<c:if test='${comments.key=="Results Assumption"}'>
																							<c:forEach var="comment"
																								items="${comments.value}">
																								<li><i><digi:trn
																									key="aim:resultsAssumption">Results Assumption</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																						<c:if
																							test='${comments.key=="Purpose Verification"}'>
																							<c:forEach var="comment"
																								items="${comments.value}">
																								<li><i><digi:trn
																									key="aim:resultsVerification">Results Verification</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																					</c:forEach>
																				</ul>
																			</c:if>
																		</field:display></TD>
																	</TR>
																</feature:display>
															</module:display>
														</TABLE>
														</TD>
													</TR>




													<module:display name="Contact Information"
														parentModule="PROJECT MANAGEMENT">
														<feature:display name="Donor Contact Information"
															module="Contact Information">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding=3 cellSpacing=1
																	vAlign="top" align="top" bgcolor="#aaaaaa">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																			height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																		<b><digi:trn
																			key="aim:donorFundingContactInformation">
																				Donor funding Contact Information
																			</digi:trn></b></TD>
																	</TR>
																	<field:display feature="Donor Contact Information"
																		name="Donor First Name">
																		<TR>
																			<TD bgcolor="#ffffff"><i><digi:trn
																				key="aim:contactPersonName">Name</digi:trn></i>: <c:out
																				value="${activity.contFirstName}" />&nbsp; <c:out
																				value="${activity.contLastName}" /></TD>
																		</TR>
																	</field:display>
																	<field:display feature="Donor Contact Information"
																		name="Donor Email">
																		<TR>
																			<TD bgcolor="#ffffff"><i><digi:trn
																				key="aim:contactPersonEmail">Email</digi:trn></i>: <c:set
																				var="mailTo" value="mailto:${activity.email}" /> <a
																				href="mailto:${activity.email}">${activity.email}</a></TD>
																		</TR>
																	</field:display>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
														<feature:display module="Contact Information"
															name="Government Contact Information">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding=3 cellSpacing=1
																	vAlign="top" align="top" bgcolor="#aaaaaa">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																			height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																		<b><digi:trn key="aim:mofedContactInformation">
																			MOFED Contact Information</digi:trn></b></TD>
																	</TR>
																	<field:display feature="Government Contact Information"
																		name="Government First Name">
																		<TR>
																			<TD bgcolor="#ffffff"><i><digi:trn
																				key="aim:contactPersonName">Name</digi:trn></i>: <c:out
																				value="${activity.mfdContFirstName}" />&nbsp; <c:out
																				value="${activity.mfdContLastName}" /></TD>
																		</TR>
																	</field:display>
																	<field:display feature="Government Contact Information"
																		name="Government Email">
																		<TR>
																			<TD bgcolor="#ffffff"><i><digi:trn
																				key="aim:contactPersonEmail">Email</digi:trn></i>: <a
																				href="mailto:${activity.mfdContEmail}">${activity.mfdContEmail}</a></TD>
																		</TR>
																	</field:display>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
														<feature:display name="Project Coordinator Contact Information"
															module="Contact Information">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding=3 cellSpacing=1
																	vAlign="top" align="top" bgcolor="#aaaaaa">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																			height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																		<b><digi:trn
																			key="aim:projectCoordinator">
																				Project Coordinator Contact Information
																			</digi:trn></b></TD>
																	</TR>
																	<field:display feature="Project Coordinator Contact Information"
																		name="Project Coordinator First Name">
																		<TR>
																			<TD bgcolor="#ffffff"><i><digi:trn
																				key="aim:contactPersonName">Name</digi:trn></i>: <c:out
																				value="${activity.prjCoFirstName}" />&nbsp; <c:out
																				value="${activity.prjCoLastName}" /></TD>
																		</TR>
																	</field:display>
																	<field:display feature="Project Coordinator Contact Information"
																		name="Project Coordinator Email">
																		<TR>
																			<TD bgcolor="#ffffff"><i><digi:trn
																				key="aim:contactPersonEmail">Email</digi:trn></i>: <c:set
																				var="mailTo" value="mailto:${activity.prjCoEmail}" /> <a
																				href="mailto:${activity.prjCoEmail}">${activity.prjCoEmail}</a></TD>
																		</TR>
																	</field:display>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
														<feature:display name="Sector Ministry Contact Information"
															module="Contact Information">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding=3 cellSpacing=1
																	vAlign="top" align="top" bgcolor="#aaaaaa">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																			height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																		<b><digi:trn
																			key="aim:sectorMinistryCnt">
																				Sector Ministry Contact Information
																			</digi:trn></b></TD>
																	</TR>
																	<field:display feature="Sector Ministry Contact Information"
																		name="Sector Ministry Contact First Name">
																		<TR>
																			<TD bgcolor="#ffffff"><i><digi:trn
																				key="aim:contactPersonName">Name</digi:trn></i>: <c:out
																				value="${activity.secMiCntFirstName}" />&nbsp; <c:out
																				value="${activity.secMiCntLastName}" /></TD>
																		</TR>
																	</field:display>
																	<field:display feature="Sector Ministry Contact Information"
																		name="Sector Ministry Contact Email">
																		<TR>
																			<TD bgcolor="#ffffff"><i><digi:trn
																				key="aim:contactPersonEmail">Email</digi:trn></i>: <c:set
																				var="mailTo" value="mailto:${activity.secMiCntEmail}" /> <a
																				href="mailto:${activity.secMiCntEmail}">${activity.secMiCntEmail}</a></TD>
																		</TR>
																	</field:display>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
													</module:display>
													<field:display name="Accession Instrument" feature="Identification">
													<c:if test="${!empty activity.accessionInstrument}">
														<TR>
															<TD>
															<TABLE width="100%" cellPadding=3 cellSpacing=1
																vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR>
																	<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																		height=10 src="../ampTemplate/images/arrow-014E86.gif"
																		width=15> <b><digi:trn
																		key="aim:AccessionInstrument">
																	Accession Instrument</digi:trn></b></TD>
																</TR>
																<TR>
																	<TD bgcolor="#ffffff">
																	${activity.accessionInstrument}</TD>
																</TR>
															</TABLE>
															</TD>
														</TR>
													</c:if>
													</field:display>
													<field:display name="A.C. Chapter" feature="Identification">
													<c:if test="${!empty activity.acChapter}">
														<TR>
															<TD>
															<TABLE width="100%" cellPadding=3 cellSpacing=1
																vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR>
																	<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																		height=10 src="../ampTemplate/images/arrow-014E86.gif"
																		width=15> <b><digi:trn key="aim:acChapter">
                                                              A.C. Chapter</digi:trn></b></TD>
																</TR>
																<TR>
																	<TD bgcolor="#ffffff">${activity.acChapter}</TD>
																</TR>
															</TABLE>
															</TD>
														</TR>
													</c:if>
													</field:display>
												</TABLE>
												</TD>
												<TD width="50%" vAlign="top" align="left">
												<TABLE width="100%" cellPadding="2" cellSpacing="2"
													vAlign="top" align="left">
													<TR>
														<TD>
														<TABLE width="100%" cellPadding=0 cellSpacing=1
															vAlign="top" align="left" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																	height=10 src="../ampTemplate/images/arrow-014E86.gif"
																	width=15> <b><digi:trn
																	key="aim:relatedOrganizations">Related Organizations</digi:trn></b>
																</TD>
															</TR>




															<TR>
																<TD bgcolor="#fffff">
																<TABLE width="100%" cellSpacing="2" cellPadding="2"
																	vAlign="top" align="center" bgcolor="#ffffff">
																	<module:display name="Funding"
																		parentModule="PROJECT MANAGEMENT">
																		<feature:display module="Funding"
																			name="Funding Information">
																			<TR>
																				<TD>
																				<TABLE cellSpacing="1" cellPadding="2" vAlign="top"
																					align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff" colspan="2"><b><digi:trn
																							key="aim:fundingCountryAgency">
																								Funding Country/Agency</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty activity.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${activity.relOrgs}">
																								<c:if test="${relOrg.role == 'DN'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%-- 	<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																						<td bgcolor="#ffffff">&nbsp;</td>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
																	</module:display>
																	<module:display name="Organizations" parentModule="PROJECT MANAGEMENT">
																	
																		<feature:display module="Organizations" name="Responsible Organisation">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:responsibleOrganisation">
																							Responsible Organisation</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty activity.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${activity.relOrgs}">
																								<c:if test="${relOrg.role == 'RO'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%-- <li><c:out value="${relOrg.orgName}" /></li><br>
																									<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
																		<feature:display module="Organizations"
																			name="Executing Agency">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:executingAgencies">
																							Executing Agencies</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty activity.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${activity.relOrgs}">
																								<c:if test="${relOrg.role == 'EA'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%-- <li><c:out value="${relOrg.orgName}" /></li><br>
																									<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
																		<feature:display module="Organizations"
																			name="Implementing Agency">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:implementingAgency">
																								Implementing Agency</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty activity.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${activity.relOrgs}">
																								<c:if test="${relOrg.role == 'IA'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%-- <bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
																		<feature:display module="Organizations"
																			name="Beneficiary Agency">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:beneficiary2Agency">
																								Beneficiary Agency</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty activity.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${activity.relOrgs}">
																								<c:if test="${relOrg.role == 'BA'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%--<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
																		<feature:display module="Organizations"
																			name="Contracting Agency">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:contracting2Agency">
																								Contracting Agency</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty activity.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${activity.relOrgs}">
																								<c:if test="${relOrg.role == 'CA'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%--<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>

																		<feature:display module="Organizations"
																			name="Regional Group">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:regionalGroup">Regional Group</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty activity.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${activity.relOrgs}">
																								<c:if test="${relOrg.role == 'RG'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%--<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>


																		<feature:display module="Organizations" name="Sector Group">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:sectorGroup">Sector Group</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty activity.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${activity.relOrgs}">
																								<c:if test="${relOrg.role == 'SG'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%--<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
													</module:display>
																</TABLE>
																</TD>
															</TR>
														</TABLE>
														</TD>
													</TR>
													<module:display name="Project ID and Planning"
														parentModule="PROJECT MANAGEMENT">
														<feature:display module="Project ID and Planning"
															name="Planning">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding=2 cellSpacing=1
																	vAlign="top" align="top" bgcolor="#aaaaaa">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																			height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																		<b><digi:trn key="aim:keyActivityDates">Key Activity Dates</digi:trn></b>
																		</TD>
																	</TR>
																	<field:display name="Proposed Approval Date" feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff">
																			<digi:trn key="aim:proposedApprovalDate">Proposed Approval Date</digi:trn> : 
																				<c:out value="${activity.origAppDate}" />
																			</TD>
																		</TR>
																	</field:display>
																	<field:display name="Actual Approval Date" feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff"><digi:trn
																				key="aim:actualapprovaldate">Actual Approval Date</digi:trn> : <c:out
																				value="${activity.revAppDate}" /></TD>
																		</TR>
																	</field:display>
																	<field:display name="Proposed Start Date"
																		feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff"><digi:trn
																				key="aim:originalStartDate">
																			Original Start Date</digi:trn> : <c:out
																				value="${activity.origStartDate}" /></TD>
																		</TR>
																	</field:display>
																	<field:display name="Actual Start Date" feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff">
																				<digi:trn key="aim:actualStartDate">Actual Start Date</digi:trn> : 
																				<c:out value="${activity.revStartDate}" />
																			</TD>
																		</TR>
																	</field:display>
																	<field:display name="Final Date for Contracting"
																		feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff"><digi:trn
																				key="aim:FinalDateForContracting">
																			Final Date for Contracting</digi:trn> : <c:out
																				value="${activity.contractingDate}" /></TD>
																		</TR>
																	</field:display>
																	<field:display name="Final Date for Disbursements"
																		feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff"><digi:trn
																				key="aim:FinalDateForDisbursments">
																			Final Date for Disbursments</digi:trn> : <c:out
																				value="${activity.disbursmentsDate}" /></TD>
																		</TR>
																	</field:display>
																	<field:display name="Current Completion Date"
																		feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff"><digi:trn
																				key="aim:currentCompletionDate">
																			Current Completion Date</digi:trn> : <c:out
																				value="${activity.currCompDate}" /> &nbsp; <a
																				href="javascript:commentWin('<c:out value="${activity.activityId}" />')">
																			<digi:trn key="aim:comment">Comment</digi:trn></a></TD>
																		</TR>
																	</field:display>
																	<field:display name="Proposed Completion Date"
																		feature="Planning">
																		<tr>
																			<TD bgcolor="#ffffff"><digi:trn
																				key="aim:proposedCompletionDate">
																			Proposed Completion Date</digi:trn> : <c:out
																				value="${activity.propCompDate}" /></TD>
																		</tr>
																		<TR>
																			<TD bgcolor="#ffffff">
																			<TABLE width="100%" cellspacing=0 cellpadding=0
																				valign=top align=left>
																				<TR>
																					<TD width="170" valign=top><digi:trn
																						key="aim:proposedClosingDates">Closing Dates
																					</digi:trn> :</TD>
																					<TD>
																					<TABLE cellPadding=0 cellSpacing=0>
																						<c:forEach var="closeDate"
																							items="${activity.revCompDates}">
																							<TR>
																								<TD><c:out value="${closeDate}" /></TD>
																							</TR>
																						</c:forEach>
																					</TABLE>
																					</TD>
																				</TR>
																			</TABLE>
																			</TD>
																		</TR>
																	</field:display>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
													</module:display>

													<module:display name="Funding"
														parentModule="PROJECT MANAGEMENT">
														<feature:display name="Funding Information"
															module="Funding">
															<field:display feature="Funding Information"
																name="Type Of Assistance">
																<TR>
																	<TD>
																	<TABLE width="100%" cellPadding=2 cellSpacing=1
																		vAlign="top" align="left" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn
																				key="aim:typeOfAssistance">
																				Type Of Assistance</digi:trn></b></TD>
																		</TR>
																		<c:if test="${!empty activity.assistanceType}">
																			<c:forEach var="asstType"
																				items="${activity.assistanceType}">
																				<TR>
																					<TD bgcolor="#ffffff"><c:out
																						value="${asstType}" /></TD>
																				</TR>
																			</c:forEach>
																		</c:if>
																	</TABLE>
																	</TD>
																</TR>
															</field:display>
														</feature:display>
													</module:display>
													<module:display name="Funding"
														parentModule="PROJECT MANAGEMENT">
														<feature:display name="Funding Information"
															module="Funding">
															<field:display name="Financing Instrument" feature="Funding Information">
																<TR>
																	<TD>
																	<TABLE width="100%" cellPadding=2 cellSpacing=1
																		vAlign="top" align="left" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn
																				key="aim:financingInstruments">
																				Financing Instruments</digi:trn></b></TD>
																		</TR>
																		<c:if test="${!empty activity.uniqueModalities}">

																			<c:forEach var="modal"
																				items="${activity.uniqueModalities}">
																				<TR>
																					<TD bgcolor="#ffffff"><c:out
																						value="${modal.value}" /></TD>
																				</TR>
																			</c:forEach>
																		</c:if>
																	</TABLE>
																	</TD>
																</TR>
															</field:display>
														</feature:display>
													</module:display>


													<TR>
														<TD>
														<TABLE width="100%" cellPadding=3 cellSpacing=1
															vAlign="top" align="top" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																	height=10 src="../ampTemplate/images/arrow-014E86.gif"
																	width=15> <b><digi:trn
																	key="aim:activityCreationDetails">
																	Activity creation details</digi:trn></b></TD>
															</TR>
															<field:display name="Activity Created By" feature="Identification">
																<TR>
																	<TD bgcolor="#ffffff"><i><digi:trn
																		key="aim:createdBy">Created By</digi:trn></i>: <c:out
																		value="${activity.createdBy.user.firstNames}" /> <c:out
																		value="${activity.createdBy.user.lastName}" /> - <c:out
																		value="${activity.createdBy.user.email}" />
																	</TD>
																</TR>
															</field:display>
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn
																	key="aim:email">Email</digi:trn></i>: <bean:define
																	id="mailTo">
																		mailto:<c:out value="${activity.createdBy.user.email}" />
																</bean:define> <a href="<%=mailTo%>"> <c:out
																	value="${activity.createdBy.user.email}" /></a></TD>
															</TR>
															<field:display name="Activity Created On" feature="Identification">
																<TR>
																	<TD bgcolor="#ffffff"><i><digi:trn
																		key="aim:createdDate">Created date</digi:trn></i>: <c:out
																		value="${activity.createdDate}" />&nbsp;
																	</TD>
																</TR>
															</field:display>
															<field:display name="Activity Approved By" feature="Identification">
																<c:if test="${!empty activity.approvedBy}">
																	<TR>
																		<TD bgcolor="#ffffff"><i> 
																		<digi:trn key="aim:activityApprovedBy">Activity approved by</digi:trn></i>: 
																		<c:out value="${activity.approvedBy.user.firstNames}"/> 
																		<c:out value="${activity.approvedBy.user.lastName}" /> - 
																		<c:out value="${activity.approvedBy.user.email}" />
																	</TR>
																</c:if>
															</field:display>
															<field:display name="Activity Approved On" feature="Identification">
																<c:if test="${!empty activity.approvalDate}">
																	<TR>
																		<TD bgcolor="#ffffff">
																			<i>
																				<digi:trn key="aim:activityApprovedOn">Activity approved on</digi:trn>
																			</i>: 
																			<c:out value="${activity.approvalDate}" /> &nbsp;
																		</TD>
																	</TR>
																</c:if>
															</field:display>
															
															<field:display name="Activity Updated By" feature="Identification">
																<c:if test="${!empty activity.updatedBy}">
																	<TR>
																		<TD bgcolor="#ffffff"><i> <digi:trn
																			key="aim:activityUpdatedBy">
																				Activity updated by</digi:trn></i>: <c:out
																			value="${activity.updatedBy.user.firstNames}" /> <c:out
																			value="${activity.updatedBy.user.lastName}" /> - <c:out
																			value="${activity.updatedBy.user.email}" />
																	</TR>
																</c:if>
															</field:display>
															<field:display name="Workspace of Creator" feature="Identification">
															<TR>
																		<TD bgcolor="#ffffff">
																			<i>
																				<digi:trn key="aim:workspaceOfCreator">Worskpace of creator</digi:trn>
																			</i>:
																			<c:out value="${activity.createdBy.ampTeam.name}" /> - <c:out value="${activity.createdBy.ampTeam.accessType}" />
																			<br/>
																			<i>
																				<digi:trn key="aim:computation">Computation</digi:trn>
																			</i>:
																				<c:if test="${activity.createdBy.ampTeam.computation == 'true'}">
																				  <digi:trn key="aim:yes">Yes</digi:trn>
																				</c:if>
																				<c:if test="${activity.createdBy.ampTeam.computation == 'false'}">
																				  <digi:trn key="aim:no">No</digi:trn>
																				</c:if>
																			<br/>
																		</TD>
															</TR>
															</field:display>
															<field:display name="Activity Updated On" feature="Identification">
																<c:if test="${!empty activity.updatedDate}">
																	<TR>
																		<TD bgcolor="#ffffff"><i><digi:trn
																			key="aim:activityUpdatedOn">
																			Activity updated on</digi:trn></i>: <c:out
																			value="${activity.updatedDate}" /> &nbsp;
																		</TD>
																	</TR>
																</c:if>
															</field:display>
															<field:display name="Data Source"
																feature="Identification">
																<c:if test="${!empty activity.actAthAgencySource}">
																	<TR>
																		<TD bgcolor="#ffffff"><i><digi:trn
																			key="aim:dataSource">
																Data Source</digi:trn></i>: <c:out
																			value="${activity.actAthAgencySource}" /> &nbsp;</TD>
																	</TR>
																</c:if>
															</field:display>
														</TABLE>
														</TD>
													</TR>

												</TABLE>
												</TD>
											</TR>
										</TABLE>
										</TD>
									</TR>
								</TABLE>
								</TD>
							</TR>
							<TR>
								<TD>&nbsp;</TD>
							</TR>

						</TABLE>

						</TD>
					</TR>

				</TABLE>
				<!-- end --></TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
		</TABLE>

	</logic:equal>
</digi:form>
