<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="JavaScript">
function gotoStep() {

	<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=charts" />
	document.aimAdvancedReportForm.action = "<%= step %>";
	document.aimAdvancedReportForm.target = "_self";
	document.aimAdvancedReportForm.submit();
}
</script>


<digi:instance property="aimAdvancedReportForm" />
<digi:form action="/advancedReportManager.do" method="post">
<input type="hidden" name="isAdd" >

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
<tr>
	<td>
		<jsp:include page="teamPagesHeader.jsp" flush="true" />
	</td>
</tr>

<tr>

<td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="770" vAlign="top" align="left" border=0>
	<tr>

	<td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
	<td>
		<table>
			<tr>
				<td>
					<table cellPadding=5 cellSpacing=0 width="100%">
						<tr>
							<td height=33><span class=crumb>
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</bean:define>
								<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:portfolio">
									Portfolio
								</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;

								<bean:define id="translation">
									<digi:trn key="aim:clickToGotoStep1">Click here to goto Step 1</digi:trn>
								</bean:define>

								<digi:link href="/advancedReportManager.do?check=forward" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:reportBuilder:selectcolumn">
									Report Builder : Select Column
								</digi:trn>					
								&gt;&gt;		
								</digi:link>&nbsp;&nbsp;

								<digi:link href="/advancedReportManager.do?check=SelectRows" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:reportBuilder:selectrows">
									Report Builder : Select Rows
								</digi:trn>					
								&gt;&gt;		
								</digi:link>&nbsp;&nbsp;

								<digi:link href="/advancedReportManager.do?check=SelectMeasuress" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:reportBuilder:selectmeasures">
									Report Builder : Select Measures
								</digi:trn>					
								&gt;&gt;		
								</digi:link>&nbsp;&nbsp;

								<digi:link href="/advancedReportManager.do?check=4" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:reportBuilder:reportDetails">
									Report Builder : Report Details
								</digi:trn>					
								&gt;&gt;		
								</digi:link>&nbsp;&nbsp;

								<digi:trn key="aim:reportBuilder:htmlReport">
									Report Builder : HTML Report
								</digi:trn>					
								
							</td>
						</tr>
					</table>	
				</td>
			</tr>
		 	<tr>

				<td height=16 vAlign=right align=center>
					<span class=subtitle-blue>
					Report Builder : Report Creation
					</span>
				</td>
			</tr>
			<tr colspan="2">
				<td class=box-title align="center" valign="top">
				&nbsp;
				<td>
			</tr>
			<TR>
			<TD vAlign="top" align="center">
	
				<TABLE width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4" class="box-border-nopadding">
					<TR>
						<TD bgcolor="#f4f4f4">
							<TABLE width="100%" cellSpacing=1 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4">
								<tr width="100%" valign="top">
									<td height="20">
										<table bgcolor="#f4f4f4" align="left" valign="bottom" cellPadding=0 cellspacing=1 height="20">
											<tr>
												<td noWrap align=left> 
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectColumns">Click here to Select Columns</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=forward"   styleClass="sub-nav" title="<%=translation%>" >
														1 :   Select Columns
													</digi:link>
												</td>											
												<td noWrap align=left>
													<bean:define id="translation">
														<digi:trn key="aim:clickToselectrows/hierarchies">Click here to select rows/hierarchies</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=SelectRows"  styleClass="sub-nav" title="<%=translation%>" >
														2 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
													</digi:link>
												</td>										
												<td noWrap align=left>
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectMeasures">Click here to Select Measures</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=SelectMeasures"  styleClass="sub-nav" title="<%=translation%>" > 
													3 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
													</digi:link>
												</td>											
												<td noWrap align=left> 
													<bean:define id="translation">
														<digi:trn key="aim:clickToViewReportDetails">Click here to view Report Details</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=4"  styleClass="sub-nav" title="<%=translation%>" >
														4 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
													</digi:link>
												</td>
											</tr>
										</table>	
									</td>
								</tr>
								<TR>
									<td noWrap valign=top align=left>
									 <table cellpadding=0 cellspacing=1 valign=top align=left>	<tr>	<td valign=top>
										<bean:define id="translation">
											<digi:trn key="aim:clickToGenerateReport">Click here to Generate Reports</digi:trn>
										</bean:define>
										<digi:link href="/advancedReportManager.do?check=5"  styleClass="sub-nav3" title="<%=translation%>" >
										5 : <digi:trn key="aim:GenerateReport">Generate Report</digi:trn>
										</digi:link>
										</td>	
										<td noWrap valign=top align=left>
										<bean:define id="translation">
											<digi:trn key="aim:clickToGenerateReport">Click here to Generate Chart</digi:trn>
										</bean:define>
										<digi:link href="/advancedReportManager.do?check=charts"  styleClass="sub-nav" title="<%=translation%>" >
										6 : <digi:trn key="aim:GenerateChart">Generate Chart</digi:trn>
										</digi:link>
										</td>	
										</tr>	</table>
									</td>	
								</tr>

								<TR bgColor=#f4f4f2>
									<TD vAlign="top" align="left" width="100%">
									</TD>
								</TR>				
								<TR bgColor=#f4f4f2>
									<TD vAlign="top" align="center" width="100%" bgColor=#f4f4f2>
										<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 >
											<TR>
												<c:if test="${!empty aimAdvancedReportForm.finalData}">
												<TD width="100%" align="center"  valign=top>
													<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2 class="box-border-nopadding" border=0>
													<tr>
														<td bgColor=#f4f4f2>
															<TABLE width="700" cellPadding=2 cellSpacing=0 vAlign="top" align="top" bgColor=#f4f4f2 border=1
															style="border-collapse: collapse">
															    <tr bgcolor="#cccccc">
																    <c:if test="${!empty aimAdvancedReportForm.addedColumns}">
																	<logic:iterate name="aimAdvancedReportForm" id="addedColumns"	property="addedColumns" >
																		<td align="center"  class=box-title>
																			<c:out value="${addedColumns.columnName}"/>
																		</td>
																	</logic:iterate>
																	</c:if>
																	<td></td>
																	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
																		<td height="21" width="69" colspan="3" align="center" >
																			<strong><%=fiscalYearRange%></strong>
																		</td>
																	</logic:iterate>
															    </tr>
															   	<tr>
																	<td></td>
																	<logic:iterate name="aimAdvancedReportForm"  property="addedColumns" id="addedColumns">
																		<td>
																		</td>
																	</logic:iterate>
																	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
																			<td height="21" width="23" align="center" > <strong>
																			<a title="<digi:trn key="aim:actualCommitment">Commitment</digi:trn>">
																			<digi:trn key="aim:commitment">Commitment</digi:trn>
																			</a></strong>
																			</td>
																			<td height="21" width="23" align="center" ><strong>
																			<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
																			<digi:trn key="aim:disbursements">Disbursement</digi:trn>
																			</a>
																			</td></strong>
																			<td height="21" width="23" align="center" ><strong>
																			<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
																			<digi:trn key="aim:expenditure">Expenditure</digi:trn>
																			</a></strong>
																			</td>
																   	
																	</logic:iterate>
															   	</tr>
																<logic:notEmpty name="aimAdvancedReportForm" property="report"> 
																<logic:iterate name="aimAdvancedReportForm"  property="report" id="report" type="org.digijava.module.aim.helper.Report">
																	<tr bgcolor="#F4F4F2">
																		<logic:iterate name="report"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
																			<td align="center" height="21">
																				<logic:notEmpty name="records" property="title">
																					<bean:write name="records" property="title" /></logic:notEmpty>

																				<logic:notEmpty name="records" property="actualStartDate">
																					<bean:write name="records" property="actualStartDate" /></logic:notEmpty>
		
																				<logic:notEmpty name="records" property="actualCompletionDate">
																					<bean:write name="records" property="actualCompletionDate" /></logic:notEmpty>
		
																				<logic:notEmpty name="records" property="status">
																					<bean:write name="records" property="status" /></logic:notEmpty>
		
																				<logic:notEmpty name="records" property="level">
																					<bean:write name="records" property="level" /></logic:notEmpty>
		
																				<logic:notEmpty name="records" property="actualCommitment">
																					<bean:write name="records" property="actualCommitment" /></logic:notEmpty>
																				
																				<logic:notEmpty name="records" property="assistance">
																					<logic:iterate name="records" id="assistance" property="assistance"> 
																						<%=assistance%>	
																					</logic:iterate>
																				</logic:notEmpty>

																				<logic:notEmpty name="records" property="donors">
																					<logic:iterate name="records" id="donors" property="donors"> 
																						<%=donors%>	
																					</logic:iterate>
																				</logic:notEmpty>

																				<logic:notEmpty name="records" property="ampFund">
																				<logic:iterate name="records"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
																					<td align="right" height="21" width="69">
																						<logic:notEqual name="ampFund" property="commAmount" value="0">
																						<bean:write name="ampFund" property="commAmount" />
																						</logic:notEqual>
																					</td>
																					<td align="right" height="21" width="69">
																						<logic:notEqual name="ampFund" property="disbAmount" value="0">
																						<bean:write name="ampFund" property="disbAmount" />
																						</logic:notEqual>
																					</td>
																					<td align="right" height="21" width="69">
																						<logic:notEqual name="ampFund" property="expAmount" value="0">
																						<bean:write name="ampFund" property="expAmount" />
																						</logic:notEqual>
																					</td>
																				</logic:iterate>
																				</logic:notEmpty>
																			</td>
																		</logic:iterate>
																	</tr>
																</logic:iterate>
																</logic:notEmpty>
															</TABLE>
														</td>
													</tr>
													</TABLE>
												</TD>
												</c:if>
												<c:if test="${empty aimAdvancedReportForm.finalData}">
												<td align=center>
													<TABLE width="100%" height="100" cellPadding=2 cellSpacing=0 vAlign="top" align="center" bgcolor="#f4f4f2">
														<tr bgcolor="#eeeeee">	<td class=box-title align=center>
																<b>No Data present	</b>
														</td></tr>
													</table>
												</td>
												</c:if>
											</TR>
							                <logic:notEmpty name="aimAdvancedReportForm" property="pages">
												<tr>
												<td>
					
					<!-- -------------------------------  Prevoius Links     ---------------------------       -->
													<bean:define id="currPage" name="aimAdvancedReportForm" property="page" type="java.lang.Integer" /> 
													<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams2}" property="page">
														<%=(currPage.intValue()-1)%>
													</c:set>
													<logic:notEqual name="aimAdvancedReportForm" property="page"
													value="1">
													  <bean:define id="translation">
															<digi:trn key="aim:clickToViewPreviousPage">Click here to view Previous page</digi:trn>
														</bean:define>
														<digi:link href="/advancedReportManager.do?check=5" name="urlParams2" title="<%=translation%>" >
															Previous
														</digi:link>
														&nbsp
													</logic:notEqual>
													
													<logic:equal name="aimAdvancedReportForm" property="page"
													value="1">
														<digi:trn key="aim:prev">Previous</digi:trn> &nbsp
													</logic:equal>	

													<logic:notEmpty name="aimAdvancedReportForm" property="finalData">
													  <bean:define id="translation">
															<digi:trn key="aim:clickToViewAllRecords">Click here to view All Records</digi:trn>
														</bean:define>
														<digi:link href="/advancedReportManager.do?check=5&page=all" title="<%=translation%>" >
															All
														</digi:link>
														&nbsp
													</logic:notEmpty>

					<!----------------------------------END   -----------------------------------------------     -->									
					
													
													<logic:iterate name="aimAdvancedReportForm" property="pages" id="pages" type="java.lang.Integer">
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page">
															<%=pages%>
														</c:set>
													
														<%  int curr = currPage.intValue();
															int cnt = pages.intValue();
															System.out.println(curr + " Comparison : " + cnt);
														%>
														<% if( curr != cnt ) { %>
														<bean:define id="translation">
															<digi:trn key="aim:clickToViewAllPages">Click here to view All pages</digi:trn>
														</bean:define>
														<digi:link href="/advancedReportManager.do?check=5" name="urlParams1" title="<%=translation%>" >
															<%=pages%>
														</digi:link>
														<% } else { %>
														<%=pages%>
														<% } %>
															|&nbsp; 
													</logic:iterate>
													
													
					<!-- -------------------------------  Next Links -------------------------------       -->									
													<bean:define id="currPage" name="aimAdvancedReportForm" property="page" type="java.lang.Integer" /> 
													<jsp:useBean id="urlParams3" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams3}" property="page">
														<%=(currPage.intValue()+1)%>
													</c:set>
														
													<bean:define name="aimAdvancedReportForm" id="allPages" property="pages" 
													type="java.util.Collection" />
													<% if(allPages.size() == currPage.intValue()) { %>	
														&nbsp; <digi:trn key="aim:next">Next</digi:trn>  
													<% } else { %>
													  <bean:define id="translation">
															<digi:trn key="aim:clickToViewNextPage">Click here to go to Next page</digi:trn>
														</bean:define>
														<digi:link href="/advancedReportManager.do?check=5" name="urlParams3" title="<%=translation%>" >
															Next
														</digi:link>
														&nbsp;	
													<% } %>
					<!-- ------------------------------------------------------------------------------  -->									
													
												</td>
												</tr>
											</logic:notEmpty>					


											<tr bgcolor="#f4f4f2">
												<td align="center" colspan="2" bgcolor="#f4f4f2">
													<input type=button name=back value="<< Previous"   class="dr-menu" onclick="javascript:history.back()">
													<input type=button name=next value="  Chart Creation  " class="dr-menu" onclick="javascript:gotoStep()" >															
												</td>
											</tr>
										</TABLE>
									</TD>
								</TR>	
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
			</TR>
		</table>
	</td>	
	<td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
</tr>
</table>
</td>	
</TR>
</TABLE>
</digi:form>