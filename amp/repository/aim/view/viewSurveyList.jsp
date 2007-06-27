<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="Javascript">
<!--
	function move(cntr) {
		var sid = "survey[" + cntr + "].surveyId";
		var val = (document.aimSurveyForm.elements)[sid].value;
		
		if (val != "-1") {
			<digi:context name="survey" property="context/module/moduleinstance/viewSurvey.do" />
			document.aimSurveyForm.action = "<%=survey %>";
			document.aimSurveyForm.surveyId.value = val;
			document.aimSurveyForm.target = "_self" ;
			document.aimSurveyForm.submit();
		}
		else
			return false;
	}

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.aimSurveyForm.action = "<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~activityId=" + id;
	document.aimSurveyForm.target = "_self";    
   document.aimSurveyForm.submit();
}

function preview(id)
{

	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
   document.aimSurveyForm.action = "<%=addUrl%>~pageId=2~activityId=" + id;
	document.aimSurveyForm.target = "_self";
   document.aimSurveyForm.submit();
}

-->
</script>

<digi:instance property="aimSurveyForm" />
<digi:form action="/viewSurveyList.do" method="post">

<%--
<html:hidden property="ampActivityId" />
<html:hidden property="tabIndex" />
<input type="hidden" name="surveyId" value=""> --%>

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="760">
	<TR>
		<TD vAlign="top" align="center">
			<!-- contents -->
			
			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
				<TR>
														<td align="top" vAlign="top">
<jsp:include page="channelOverviewMenu.jsp" flush="true" />
	</td>
					<TD bgcolor="#f4f4f4" align="top" vAlign="top">
						<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
							<TR bgColor=#f4f4f2>
      	      			<TD align=left>
									<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
										<TR>
											<TD align="left">
												<SPAN class=crumb>
													<bean:define id="translation">
														<digi:trn key="aim:clickToViewAESurveys">Click here to view Aid Effectiveness Surveys</digi:trn>
													</bean:define>
													<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap" />
													<c:set target="${urlParams}" property="ampActivityId" value="${aimSurveyForm.ampActivityId}" />
													<c:set target="${urlParams}" property="tabIndex" value="${aimSurveyForm.tabIndex}" />
													<digi:link href="/viewSurveyList.do" name="urlParams" styleClass="comment" title="<%=translation%>" >
													<digi:trn key="aim:viewAESurveys">Aid Effectiveness Surveys</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
													<digi:trn key="aim:actOverview">Overview</digi:trn>
												</SPAN>
											</TD>
											<TD align="right">
												&nbsp;
											</TD>
											<TD align=right>
												<input type="button" value="Preview" class="dr-menu"
												onclick="preview(<c:out value="${aimSurveyForm.ampActivityId}"/>)">
												<input type="button" value="Edit" class="dr-menu"
												onclick="fnEditProject(<c:out value="${aimSurveyForm.ampActivityId}"/>)">
											<logic:empty name="SA" scope="application">
												<input type="button" value="Preview Logframe" class="dr-menu"	onclick="previewLogframe(<c:out value="${aimFinancingBreakdownForm.ampActivityId}"/>)">
											</logic:empty>
											<logic:empty name="SA" scope="application">
											<input type='button' value='<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>' class='dr-menu'
												onclick='projectFiche(<c:out value="${aimFinancingBreakdownForm.ampActivityId}"/>)'>
											</logic:empty>
												
												
											</TD>
										</TR>
									</TABLE>										
								</TD>
							</TR>
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="750">
									<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" height="17">
												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                        							<TR bgcolor="#F4F4F2" height="17"> 
                          	  							<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
															<digi:trn key="aim:viewAESurveysList">Aid Effectiveness Survey List</digi:trn>
							  							</TD>
	                          							<TD background="module/aim/images/corner-r.gif" height=17 width=17>
                                						 &nbsp;
							  							</TD>
													</TR>
												</TABLE>									
											</TD>
										</TR>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">
												<TABLE width="756"  border="0" cellpadding="4" cellspacing="1">
                 									<TR bgcolor="#DDDDDB" > 
                 									<%--
	                        							<TD width="212"><digi:trn key="aim:orgFundingId">Org Funding ID</digi:trn></TD>
				                    					<TD width="204"><digi:trn key="aim:organization">Organization</digi:trn></TD>
									    				<TD width="114"><digi:trn key="aim:termAssist">Term Assist</digi:trn></TD>
	                         	   						<TD width="173"><digi:trn key="aim:surveyYear">Survey Year</digi:trn></TD>
	                         	   					--%>
	                         	   						<TD width="172"><digi:trn key="aim:aeSurvey">Aid Effectiveness Survey</digi:trn></TD>
				                    					<TD width="410"><digi:trn key="aim:donorOrganization">Donor Organization</digi:trn></TD>
													</TR>
												<nested:empty name="aimSurveyForm" property="survey"> 
			                    					<TR valign="top"> 
													<TD align="center" colspan="7" width="742"><span class="note">No funding for this activity !</span></TD>
			                    					</TR>
			                    				</nested:empty>
			                    				<nested:notEmpty name="aimSurveyForm" property="survey">
													<nested:iterate name="aimSurveyForm" property="survey" id="surveyFund" indexId="cntr"
														     		type="org.digijava.module.aim.helper.SurveyFunding">
													<TR valign="top" bgcolor="#f4f4f2">
														<c:set target="${urlParams}" property="surveyId" value="${surveyFund.surveyId}" />
														<c:set target="${urlParams}" property="reset" value="true" />
														<bean:define id="translation">
															<digi:trn key="aim:clickToViewAESurvey">Click here to view Aid Effectiveness Survey</digi:trn>
														</bean:define>
														<TD width="162">
															<digi:link href="/viewSurvey.do" name="urlParams" styleClass="comment" title="<%=translation%>" >
																AES-<%=cntr.intValue()+1%></digi:link>
														</TD>
						               					<TD width="400"><nested:write name="surveyFund" property="fundingOrgName" /></TD>
													<%--
					    	           					<TD width="212">
					    	           						<nested:write name="surveyFund" property="fundingId" />
						               					</TD>
					                  					<TD width="204"><nested:write name="surveyFund" property="fundingOrgName" /></TD>
							                			<TD align="left" width="114"><nested:write name="surveyFund" property="termAssist"/></TD>
							                			<TD align="left" width="173">
							                   				<nested:select property="surveyId"  onchange='<%="move(" + cntr + ")" %>'>
																<html:option value="-1">-- Select Year --</html:option>
															<nested:notEmpty name="surveyFund" property="survey">
																<nested:optionsCollection name="surveyFund" property="survey" 
																						  value="surveyId" label="year" />
															</nested:notEmpty>
															</nested:select>
							                			</TD>
							                		--%>
												    </TR>
													</nested:iterate>
												</nested:notEmpty>
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
</TABLE>
</digi:form>
