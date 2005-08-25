<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
function gotoStep() {
	var temp = document.aimAdvancedReportForm.reportTitle.value;
	var desc = document.aimAdvancedReportForm.reportDescription.value;
	if(trim(temp) == "")
	{
		alert(" Please enter report title");
		document.aimAdvancedReportForm.reportTitle.value = ""
	}
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=5" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.reportTitle.value = temp;
		document.aimAdvancedReportForm.reportDescription.value = desc;
		document.aimAdvancedReportForm.submit();
	}
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
								
								
								<digi:link href="/advancedReportManager.do?check=SelectMeasures" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:reportBuilder:selectmeasures">
									Report Builder : Select Measure
								</digi:trn>					
								&gt;&gt;		
								</digi:link>&nbsp;&nbsp;

								<digi:trn key="aim:reportBuilder:reportDetails">
									Report Builder: Report Details
								</digi:trn>					
							</td>
						</tr>
					</table>	
				</td>
			</tr>
		 	<tr>

				<td height=16 vAlign=right align=center>
					<span class=subtitle-blue>
					Report Builder : Report Details
					</span>
				</td>
			</tr>
			<tr colspan="2">
				<td class=box-title align="center" valign="top">

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
													<digi:link href="/advancedReportManager.do?check=4"  styleClass="sub-nav3" title="<%=translation%>" >
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
										<digi:link href="/advancedReportManager.do?check=4"  styleClass="sub-nav" title="<%=translation%>" >
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
									<TD vAlign="top" align="center" width="100%" bgColor=#f4f4f2>
										<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 >
											<tr>	
												<td>		
													<digi:errors/>
												</td>
											</tr>
											<TR>
												<TD width="100%" bgcolor="pink" align="center"  valign=top>
													<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2 class="box-border-nopadding" border=0>
													<tr>
														<td bgColor=#f4f4f2 height=50>
															<TABLE width="100%" cellPadding=2 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 border=1
															style="border-collapse: collapse" >
															<tr>
																<td align="left" height=20  bgColor=#f4f4f2 width=150 class=box-title>
																	<FONT color=red><BIG>*</BIG> </font>	Report Title
																</td>
															</tr>
															<tr>
																<td align="left" height=20 width=200 >
																	<html:textarea property="reportTitle" cols="60" rows="2" styleClass="inp-text" />
																</td>
															</tr>
															<tr>
					
																<td align="left"  bgColor=#f4f4f2 height=20 width=150 class=box-title >
																	Report Description
																</td>
															</tr>
															<tr>
																<td align="left" height=60 width=200 >
																	<html:textarea property="reportDescription" cols="60" rows="10" styleClass="inp-text" />
																</td>
															</tr>
															</TABLE>
														</td>
													</tr>
													</TABLE>
												</TD>
											</TR>
											<tr bgcolor="#f4f4f2">
												<td align="center" colspan="2" bgcolor="#f4f4f2">
													<input type=button name=back value="<< Previous"   class="dr-menu" onclick="javascript:history.back()">
													<input type=button name=cancel value=" Cancel "   class="dr-menu">
													<input type=button name=generateReport value="  Generate Report  " class="dr-menu"  onclick="javascript:gotoStep()">															
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