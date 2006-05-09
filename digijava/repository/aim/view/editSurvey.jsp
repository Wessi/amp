<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>

<script language="Javascript">
<!--

	function move(pg) {
		document.aimEditActivityForm.page.value = pg;
		document.aimEditActivityForm.submit();
	}
	
	function fnGetSurveyList() {
		<digi:context name="survey" property="context/module/moduleinstance/editSurveyList.do?edit=true" />
		document.aimEditActivityForm.action = "<%= survey %>";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
	}

-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/editSurvey.do" method="post">

<input type="hidden" name="page" value="">
<html:hidden property="step" />

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
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop </digi:trn>
								</bean:define>
								<digi:link href="/viewMyDesktop.do" styleClass="comment"  onclick="return quitRnot()"  title="<%=translation%>">
									<digi:trn key="aim:portfolio">Portfolio</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;
								<bean:define id="tip">
									<digi:trn key="aim:clickToViewSurveyList">Click here to view survey list</digi:trn>
								</bean:define>
								<a href="javascript:fnGetSurveyList()" class="comment"  title="<%=tip%>">
									<digi:trn key="aim:aidHarmonizationSurvey">Aid Harmonization Survey</digi:trn>
								</a>&nbsp;&gt;&nbsp;
									<digi:trn key="aim:ahIndicators">Indicators</digi:trn>
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height=16 vAlign=center width="100%"><span class=subtitle-blue>
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn key="aim:addNewActivity">Add New Activity</digi:trn>
								</c:if>			
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivity">Edit Activity</digi:trn>
								</c:if>										
							</td>
						</tr>	
					</table>
				</td></tr>
				<tr><td>
					<digi:errors/>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border=0>
						<tr><td width="75%" vAlign="top">
						<table cellPadding=0 cellSpacing=0 width="100%" border=0>
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
                                            &nbsp;
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn key="aim:aidEffectiveIndicators">
													Aid Effectiveness Indicators</digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
                                            &nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgcolor="#f4f4f2" width="100%">
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<!-- contents -->

								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><c:out value="${aimEditActivityForm.fundingOrg}" /></b>
										</td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>
								<!-- Indicator Table starts here -->
										
										<logic:notEmpty name="aimEditActivityForm" property="indicators">
											<table width="100%" cellPadding=3>
												<bean:define id="start" name="aimEditActivityForm" property="startIndex" />
													<nested:iterate name="aimEditActivityForm" property="indicators" 
																    offset="start" length="5">
														<tr>
															<td bgcolor=#ECF3FD width="5%">
																<b><nested:write property="indicatorCode" /></b>
															</td>
															<td bgcolor=#ECF3FD width="95%"><b>
																<nested:write property="name" />
															</td>
														</tr>
													<nested:iterate property="question">
														<TR bgcolor="#f4f4f2">
															<TD colspan="2" width="90%">
															<TABLE width="90%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgcolor="#ffffff">
																<TR>
																	<TD width="80%">
																		<nested:write property="questionText" />
																	</TD>
																<nested:equal property="questionType" value="yes-no"> 
																	<TD width="3%">
																		<nested:radio property="response" value="yes" />
																	</TD>
																	<TD width="7%">yes</TD>
																	<TD width="3%">
																		<nested:radio property="response" value="no" />
																	</TD>
																	<TD width="7%">no</TD>																		
																</nested:equal>
																<nested:equal property="questionType" value="calculated">
																	<TD width="20%">
																		<nested:equal property="response" value="nil">
																			<digi:trn key="aim:noPlannedDisbursement">No planned disbursement</digi:trn>
																		</nested:equal>
																		<nested:notEqual property="response" value="nil">
																			<nested:text property="response" size="5" readonly="true" />
																		</nested:notEqual>
																	</TD>
																</nested:equal>
																</TR>
															</TABLE>
															</TD>
														</TR>
														</nested:iterate>
													</nested:iterate>
												</table>
											</logic:notEmpty>
										</td></tr>
										<tr><td>&nbsp;</td></tr>
										
										<!-- pagination starts here-->
										
											<logic:notEmpty name="aimEditActivityForm" property="pageColl">
												<tr>
													<td colspan="2">
														<digi:trn key="aim:surveyPages">Pages :</digi:trn>
														<logic:iterate name="aimEditActivityForm" property="pageColl" id="pages" type="java.lang.Integer">
														<%--
															<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams}" property="page"><%=pages%></c:set>
														--%>
															<c:if test="${aimEditActivityForm.currPage == pages}">
																<font color="#FF0000"><%=pages%></font>
															</c:if>
															<c:if test="${aimEditActivityForm.currPage != pages}">
																<bean:define id="translation">
																	<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
																</bean:define>
																<a href="javascript:move(<%=pages%>)"><%=pages%></a>
																<%--<digi:link href="/editSurvey.do" name="urlParams" title="<%=translation%>" >
																	<%=pages%>
																</digi:link> --%>
															</c:if>
															|&nbsp; </logic:iterate>
													</td>
												</tr>
											</logic:notEmpty>
										
										<!-- pagination ends here-->
										
												<tr><td>&nbsp;</td></tr>
											</table>
										
								<!-- Indicator Table ends here -->
									
									</td></tr>									
								</table>

								<!-- end contents -->
							</td></tr>
							</table>
							</td></tr>							
						</table>						
						</td>
						<td width="25%" vAlign="top" align="right">
						<!-- edit activity form menu -->
							<jsp:include page="donorEditActivityMenu.jsp" flush="true" />
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
<%--
</td></tr>
</table> --%>
</digi:form>
