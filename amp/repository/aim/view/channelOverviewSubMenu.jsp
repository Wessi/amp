<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
<!--

	function viewProjectDetails(type,key) {
		openNewWindow(600, 400);
		<digi:context name="viewProjDetails" property="context/module/moduleinstance/viewProjectDetails.do"/>
		document.aimMainProjectDetailsForm.action = "<%= viewProjDetails %>";
		document.aimMainProjectDetailsForm.type.value = type;
		document.aimMainProjectDetailsForm.description.value = key;
		document.aimMainProjectDetailsForm.objectives.value = key;
		document.aimMainProjectDetailsForm.target = popupPointer.name;
		document.aimMainProjectDetailsForm.submit();					  
	}

-->
</script>


<digi:instance property="aimMainProjectDetailsForm" />

<logic:equal name="aimMainProjectDetailsForm" property="sessionExpired" value="true">
	
</logic:equal>

<logic:equal name="aimMainProjectDetailsForm" property="sessionExpired" value="false">
<jsp:useBean id="urlTabs" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlTabs}" property="ampActivityId">
	<bean:write name="aimMainProjectDetailsForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlTabs}" property="tabIndex" value="0"/>
<jsp:useBean id="urlDescription" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlDescription}" property="ampActivityId">
	<bean:write name="aimMainProjectDetailsForm" property="ampActivityId"/>
</c:set>

<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" vAlign="top" align="left">
  
	<TR>
	<digi:form action="/viewProjectDetails.do">
			<html:hidden property="type" />
			<html:hidden property="description" />
			<html:hidden property="objectives" />		
		<TD width="100%" nowrap align="center" vAlign="bottom" height="20"  bgcolor="#f7f9e3">
			<TABLE width="98%" cellspacing="0" cellpadding=0 border=0  bgcolor="#f7f9e3">
			  	<TR><TD bgcolor="#f7f9e3" >
				<DIV id="leftNav">
				
						
						<div id="gen" title='<digi:trn key="aim:clickToViewChannelOverview">Click here to view Channel Overview</digi:trn>' >
						
						<digi:link href="/viewChannelOverview.do" name="urlTabs" styleClass="leftNavItem" >
							 <digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
						</digi:link></div>
						
						
						
					</td></tr>		
					<TR><TD bgcolor="#f7f9e3" >
					<DIV id="leftNav">
					
					
					  	<span id="leftNavSelected">
						 <digi:trn key="aim:financialProgress">Financial Progress</digi:trn>								
						</span>
					
					
								
					</td></tr>
	<%if (session.getAttribute("hell").equals("1")){ %>
					  kill me..
					<tr><td><jsp:include page="submenu.jsp" flush="true" /></td></tr><%}%>
					<TR><TD bgcolor="#f7f9e3">
					<DIV id="leftNav">

						<logic:notEmpty name="SA" scope="application">
					
                  <c:set target="${urlTabs}" property="tabIndex" value="2"/>
					
						<div id="gen" title='<digi:trn key="aim:clickToViewPhysicalProgress">Click here to view Physical Progress</digi:trn>'>
                 	<digi:link href="/viewPhysicalProgress.do" name="urlTabs" styleClass="leftNavItem">
                 		 <digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>
                 	</digi:link></div>
					
					
						</logic:notEmpty>
					</td></tr>		
					<TR><TD bgcolor="#f7f9e3">
					<DIV id="leftNav">

					
					
					
     					<c:set target="${urlTabs}" property="tabIndex" value="3"/>
					
						<div id="gen" title='<digi:trn key="aim:clickToViewDocuments">Click here to view Documents</digi:trn>'>
           			<digi:link href="/viewKnowledge.do" name="urlTabs" styleClass="leftNavItem">
							 <digi:trn key="aim:documents">Documents</digi:trn>	
						</digi:link></div>
					
					
					</td></tr>		
					<TR><TD bgcolor="#f7f9e3">
					<DIV id="leftNav">
						<logic:notEmpty name="SA" scope="application">												
					
					
						<c:set target="${urlTabs}" property="tabIndex" value="4"/>
						<c:set target="${urlTabs}" property="regionId" value="-1"/>
					
						<div id="gen" title='<digi:trn key="aim:clickToViewRegionalFundings">Click here to view regional funding</digi:trn>'>
						<digi:link href="/viewRegionalFundingBreakdown.do" name="urlTabs" styleClass="leftNavItem">
							 <digi:trn key="aim:regionalFunding">Regional Funding</digi:trn> 	
						</digi:link></div>
					
					
						</logic:notEmpty>
	<logic:notEmpty name="SA" scope="application">												
				</DIV>
				</TD></TR>
			  	<TR><TD bgcolor="#f7f9e3">
				<DIV id="leftNav">
				
	</logic:notEmpty>					
					
						<logic:notEmpty name="PI" scope="application">
					
					
           			<jsp:useBean id="survey" type="java.util.Map" class="java.util.HashMap" />
						<c:set target="${survey}" property="ampActivityId">
							<bean:write name="aimMainProjectDetailsForm" property="ampActivityId"/>
						</c:set>
						<c:set target="${survey}" property="tabIndex" value="5"/>
				
						<div id="gen" 
						title='<digi:trn key="aim:clickToViewAidEffectIndicators">Click here to view Aid Effectiveness Indicators</digi:trn>'>
						<digi:link href="/viewSurveyList.do" name="survey" styleClass="leftNavItem">
							 <digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn> 	
						</digi:link></div>
				
						
						</logic:notEmpty>
						</DIV>
					</TD></TR>
			  		<TR><TD bgcolor="#f7f9e3">
					<DIV id="leftNav">
				
						<logic:notEmpty name="ME" scope="application">
						
						
						<c:set target="${urlTabs}" property="tabIndex" value="6"/>
					
						<div id="gen" title='<digi:trn key="aim:clickToViewActivityDashboard">Click here to view activity dashboard</digi:trn>'>
						<digi:link href="/viewActivityDashboard.do" name="urlTabs" styleClass="leftNavItem">
							 <digi:trn key="aim:activityDashboard">Dashboard</digi:trn>
						</digi:link></div>
					
					
						</logic:notEmpty>
					</DIV>
				</TD></TR>
			  	<TR><TD bgcolor="#f7f9e3">
				<DIV id="leftNav">
					
						
					
						
     					<c:set target="${urlTabs}" property="tabIndex" value="7"/>
					
						<div id="gen" title='<digi:trn key="aim:clickToViewCosting">Click here to view Costing</digi:trn>'>
              		<digi:link href="/viewProjectCostsBreakdown.do" name="urlTabs" styleClass="leftNavItem">
							 <digi:trn key="aim:projectCosting">Costing</digi:trn>
						</digi:link></div>
					
					
						
					
				</DIV>	
				</TD></TR>
			</TABLE>
		</TD>
	</TR></digi:form>
</TABLE>
</logic:equal>		
<%--

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<html:errors/>
<digi:instance property="aimFinancialOverviewForm" />

<digi:context name="digiContext" property="context"/>

<logic:equal name="aimFinancialOverviewForm" property="sessionExpired" value="true">
	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp" flush="true" />
</logic:equal>

<logic:equal name="aimFinancialOverviewForm" property="sessionExpired" value="false">
<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlSubTabs}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlSubTabs}" property="ampFundingId">
	<bean:write name="aimFinancialOverviewForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlSubTabs}" property="tabIndex" value="1"/>
<c:set target="${urlSubTabs}" property="transactionType" value="0"/>

<jsp:useBean id="urlAll" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlAll}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlAll}" property="ampFundingId">
	<bean:write name="aimFinancialOverviewForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlAll}" property="tabIndex" value="1"/>

<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlDiscrepancy}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlDiscrepancy}" property="tabIndex" value="1"/>
<c:set target="${urlDiscrepancy}" property="transactionType" value="0"/>

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
<TR>
	<TD vAlign="top" align="center">
		<!-- contents -->

			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
			
			<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
				<TR bgColor=#222e5d height="20"><TD style="COLOR: #c9c9c7" height="20"> 	
				&nbsp;&nbsp;&nbsp;
				<SPAN class="sub-nav2-selected">OVERVIEW</SPAN> | 
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>
					</bean:define>
					<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="<%=translation%>" >
					<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>
					</digi:link>|
					<c:set target="${urlSubTabs}" property="transactionType" value="1"/>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>
					</bean:define>
					<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="<%=translation%>" >
					<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
					</digi:link>| 
					<c:set target="${urlSubTabs}" property="transactionType" value="2"/>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>
					</bean:define>
					<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="<%=translation%>" >
					<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
					</digi:link>| 
					<digi:link href="/viewYearlyDiscrepancy.do" name="urlDiscrepancy" styleClass="sub-nav2" title="<%=translation%>" >
					<digi:trn key="aim:discrepancy">DISCREPANCY</digi:trn>
					</digi:link> |
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewAll">Click here to view All</digi:trn>
					</bean:define>
					<digi:link href="/viewYearlyComparisons.do" name="urlAll" styleClass="sub-nav2" title="<%=translation%>" >
					<digi:trn key="aim:all">ALL</digi:trn>
					</digi:link>	
				</TD></TR>	
</table>	
</logic:equal>--%>
