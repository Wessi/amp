<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/advanceReportManager.js"/>"></script>
<script language="JavaScript">
	function saveReport()
	{
		alert("Your report is being saved");
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SaveReport" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}

function gotoStep() {

	<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=5" />
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
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 vAlign="top" align="left" border=0>
	<tr><td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
	<td>
	<table><tr><td>

	<table cellPadding=5 cellSpacing=0 width="100%">
	<tr><td height=33><span class=crumb>
	<bean:define id="translation">
	<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
	</bean:define>
	<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
	<digi:trn key="aim:portfolio">Portfolio</digi:trn></digi:link>&nbsp;&gt;&nbsp;						<bean:define id="translation"><digi:trn key="aim:clickToGotoStep1">Click here to goto Step 1</digi:trn></bean:define>
	<digi:link href="/advancedReportManager.do?check=forward" styleClass="comment" title="<%=translation%>" ><digi:trn key="aim:reportBuilder:selectcolumn">Report Builder : Select Column</digi:trn>&gt;&gt;</digi:link>&nbsp;&nbsp;<digi:link href="/advancedReportManager.do?check=5" styleClass="comment" title="<%=translation%>" ><digi:trn key="aim:reportBuilder:selectrows">		Report Builder : Select Rows</digi:trn>&gt;&gt;</digi:link>&nbsp;&nbsp;							<digi:link href="/advancedReportManager.do?check=SelectMeasuress" styleClass="comment" title="<%=translation%>" ><digi:trn key="aim:reportBuilder:selectmeasures">Report Builder : Select Measures</digi:trn>&gt;&gt;</digi:link>&nbsp;&nbsp;<digi:link href="/advancedReportManager.do?check=4" styleClass="comment" title="<%=translation%>" ><digi:trn key="aim:reportBuilder:reportDetails">Report Builder : Report Details</digi:trn>&gt;&gt;</digi:link>&nbsp;&nbsp;<digi:trn key="aim:reportBuilder:htmlReport">Report Builder : HTML Report</digi:trn>					
	</td></tr>
	</table>	
	</td></tr>

	<tr>
	<td height=16 vAlign=right align=center><span class=subtitle-blue>Report Builder : Report Creation	</span></td>
	</tr>
	
	<tr colspan="2">
	<td class=box-title align="center" valign="top">&nbsp;<td>
	</tr>

	<TR>
	<TD vAlign="top" align="center">
	<TABLE width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4" class="box-border-nopadding">
	<TR><TD bgcolor="#f4f4f4">
	<TABLE width="100%" cellSpacing=1 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4">
	<tr width="100%" valign="top"><td height="20">
	<table bgcolor="#f4f4f4" align="left" valign="bottom" cellPadding=0 cellspacing=1 height="20">
	<tr>
	<td noWrap align=left> 
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectReportType">Click here to Select Report Type</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do~check=forward" styleClass="sub-nav" title="<%=translation%>"  >
														1 :   Select Report Type
													</digi:link>
												</td>
											<!--ends here-->
												<td noWrap align=left> 
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectColumns">Click here to Select Columns</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=SelectCols" styleClass="sub-nav" title="<%=translation%>" >
														2 :   Select Columns
													</digi:link>
												</td>											
												<td noWrap align=left>
													<bean:define id="translation">
														<digi:trn key="aim:clickToselectrows/hierarchies" >Click here to select rows/hierarchies</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=SelectRows"  styleClass="sub-nav" title="<%=translation%>" >
														3 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
													</digi:link>
												</td>										
												<td noWrap align=left>
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectMeasures">Click here to Select Measures</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=SelectMeasures"  styleClass="sub-nav" title="<%=translation%>" > 
													4 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
													</digi:link>
												</td>											
												
											</tr>
										</table>	
									</td>
								</tr>
								<TR>

									<td noWrap valign=top align=left>
									 <table cellpadding=0 cellspacing=1 valign=top align=left border="0">	<tr>	
									 <td noWrap align=left> 
													<bean:define id="translation">
														<digi:trn key="aim:clickToViewReportDetails">Click here to view Report Details</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=4"  styleClass="sub-nav" title="<%=translation%>" >
														5 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
													</digi:link>
									</td>
									 <td valign=top>
									 
									<!-- <font color="#f4f4f2"  >-->
										<bean:define id="translation">
											<digi:trn key="aim:clickToGenerateReport">Click here to Generate Reports</digi:trn>
										</bean:define>
										<digi:link href="/advancedReportManager.do?check=5"  styleClass="sub-nav3" title="<%=translation%>">
										6 : <digi:trn key="aim:GenerateReport">Generate Report</digi:trn>
										<!--</digi:link>-->
										<!--</font>-->
										</td>
										</tr>
										</table>
										</td>
										</tr>
										<!--<td noWrap valign=top align=left>
										<bean:define id="translation">
											<digi:trn key="aim:clickToGenerateReport">Click here to Generate Chart</digi:trn>
										</bean:define>
										<digi:link href="/advancedReportManager.do?check=forward"  styleClass="sub-nav" title="<%=translation%>" onclick="javascript:alert('Charts Coming Soon...');">
										6 : <digi:trn key="aim:GenerateChart">Generate Chart</digi:trn>
										</digi:link>
										</td>-->

	<TR bgColor=#f4f4f2>
	<TD vAlign="top" align="left" width="100%"></TD>
	</TR>				
	<TR bgColor=#f4f4f2>
	<TD vAlign="top" align="center" width="100%" bgColor=#f4f4f2 colspan="2">
	<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 >
	<TR>
	<c:if test="${!empty aimAdvancedReportForm.finalData}">
	<TD width="100%" align="center"  valign=top>
	<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2 class="box-border-nopadding" border=0>

	<!-- begin no hierarchy -->
	<logic:equal name="aimAdvancedReportForm"  property="hierarchyFlag" value="false">			
	<jsp:include page="AdvancedReportManagerStep2Hierarchy0.jsp"/>
	</logic:equal>
	<!-- end of no hierarchy -->

	<!-- begin of hierarchy -->
	<logic:equal name="aimAdvancedReportForm"  property="hierarchyFlag" value="true">		
	<jsp:include page="AdvancedReportManagerStep2Hierarchyn.jsp"/>
	</logic:equal>
	<!-- end of hierarchy -->


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
															//logger.info(curr + " Comparison : " + cnt);
														%>
														<% if( curr != cnt ) { %>
														<bean:define id="translation">
															<digi:trn key="aim:clickToViewSpecificPages">Click here to view Specified Page</digi:trn>
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
													<input type="button" name="Cancel" value=" Cancel " class="dr-menu" onclick="return quitAdvRptMngr()" >
													<input type=button name=next value="  Chart Creation  " class="dr-menu" onclick="javascript:gotoStep()" >
													<input type=button name=back value=" Save Report "   class="dr-menu" onclick="saveReport()">						
													
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