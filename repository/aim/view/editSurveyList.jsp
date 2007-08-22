<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>



<script language="JavaScript">

<!--

	function move(cntr) {

		var sid = "survey[" + cntr + "].surveyId";

		var val = (document.aimEditActivityForm.elements)[sid].value;

		if (val != "-1") {

			document.aimEditActivityForm.surveyId.value = val;

			document.aimEditActivityForm.target = "_self" ;

			document.aimEditActivityForm.submit();

		}

		else

			return false;

	}

-->

</script>



<digi:instance property="aimEditActivityForm" />

<digi:form action="/editSurvey.do" method="post">



<%-- <input type="hidden" name="surveyId" value=""> --%>



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

								<c:set var="translation">

									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop </digi:trn>

								</c:set>

								<c:set var="message">
										<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
									</c:set>

								<digi:link href="/viewMyDesktop.do" styleClass="comment"  onclick="return quitRnot1('${message}')"  title="${translation}">

									<digi:trn key="aim:portfolio">Portfolio</digi:trn>

								</digi:link>&nbsp;&gt;&nbsp;

								<digi:trn key="aim:aidEffectivenessSurvey">Aid Effectiveness Survey</digi:trn>

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

									<tr><td>&nbsp;</td></tr>

									<tr><td>



								<!-- Indicator Table starts here -->



									<TABLE width="656"  align="center" cellpadding="4" cellspacing="1" class="box-border-nopadding">

                 					<TR bgcolor="#DDDDDB" >

                 					<%--

	                        			<TD width="212"><digi:trn key="aim:orgFundingId">Org Funding ID</digi:trn></TD>

				                    	<TD width="204"><digi:trn key="aim:organization">Organization</digi:trn></TD>

									    <TD width="114"><digi:trn key="aim:termAssist">Term Assist</digi:trn></TD>

	                         	   		<TD width="143"><digi:trn key="aim:surveyYear">Survey Year</digi:trn></TD>

	                         	   	--%>

	                         	   		<TD width="172"><digi:trn key="aim:aeSurvey">Aid Effectiveness Survey</digi:trn></TD>

				                    	<TD width="410"><digi:trn key="aim:donorOrganization">Donor Organization</digi:trn></TD>

									</TR>

									<nested:empty name="aimEditActivityForm" property="survey">

			                    		<TR valign="top">

											<TD align="center" colspan="7" width="742"><span class="note"> No records found </span></TD>

			                     		</TR>

			                    	</nested:empty>

			                    	<nested:notEmpty name="aimEditActivityForm" property="survey">

										<nested:iterate name="aimEditActivityForm" property="survey" id="surveyFund" indexId="cntr"

			  	                   					    type="org.digijava.module.aim.helper.SurveyFunding">

											<TR valign="top" bgcolor="#f4f4f2">

												<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap" />

												<c:set target="${urlParams}" property="surveyId" value="${surveyFund.surveyId}" />

												<c:set target="${urlParams}" property="edit" value="true" />

												<c:set var="translation">

													<digi:trn key="aim:clickToViewAESurvey">Click here to view Aid Effectiveness Survey</digi:trn>

												</c:set>

												<TD width="172">

													<digi:link href="/editSurvey.do" name="urlParams" styleClass="comment" title="${translation}" >

														AES-<%=cntr.intValue()+1%></digi:link>

												</TD>

						               			<TD width="410"><nested:write name="surveyFund" property="fundingOrgName" /></TD>

											<%--

					    	           			<TD width="212">

					    	           				<nested:write name="surveyFund" property="fundingId" />

						               			</TD>

					                  		    <TD width="204"><nested:write name="surveyFund" property="fundingOrgName" /></TD>

							                    <TD align="left" width="114"><nested:write name="surveyFund" property="termAssist"/></TD>

							                    <TD align="left" width="143">

							                    	<nested:select property="surveyId" onchange='<%="move(" + cntr + ")" %>'>

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

									</td></tr>

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




