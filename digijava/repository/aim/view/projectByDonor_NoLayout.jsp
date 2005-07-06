<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<digi:errors/>
<digi:instance property="aimMulitlateralbyDonorForm" />
<digi:form action="/viewbyProjectbyDonor.do" >
<table width="772" border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF">
<tr>
	<td width="14" class="r-dotted-lg">&nbsp;</td>
	<td width="750" align="left" valign="top" >
	<table width="100%"  border="0" cellpadding="5" cellspacing="0">
           <tr>
              <td width="2">&nbsp;</td>
            </tr>
			<tr>
				<td colspan=3 class=subtitle-blue align=center>
					<digi:trn key="aim:ProjectbyDonorTitle">
					<bean:write name="aimMulitlateralbyDonorForm" property="reportName" />
					</digi:trn>
				</td>
			</tr>
			<tr>
				<td colspan=3 class=box-title align=center>
					<bean:write name="aimMulitlateralbyDonorForm" property="workspaceType" />&nbsp; <bean:write name="aimMulitlateralbyDonorForm" property="workspaceName" />&nbsp; 
				<!--	<digi:trn key="aim:team">Team</digi:trn> -->
				</td>
			</tr>

			<tr>
			<td width="9"></td>

<bean:define id="fcount" name="aimMulitlateralbyDonorForm" property="filterCnt" type="java.lang.Integer" /> 
<%
	int fcnt = fcount.intValue();
	System.out.println("FC = " +fcnt);
	
%>              
            <table border="0" cellspacing="0" cellpadding="2">
			<TR>
			<logic:greaterThan name="aimMulitlateralbyDonorForm" property="filterCnt" value="0" >
			<TD bgColor="black" colspan=<%= fcnt+1%> ></TD>
			</logic:greaterThan>
			</TR>

			<tr bgcolor="#c0c0c0" height=30>

						<logic:equal name="aimMulitlateralbyDonorForm" property="filterFlag" value="true">
						<td>
							<html:select styleClass="dr-menu" property="perspectiveFilter">
								<html:option value="DN">Donor View</html:option>
								<html:option value="MA">MOFED</html:option>
							</html:select>
						</td>
						</logic:equal>

				<!--		<logic:equal name="aimMulitlateralbyDonorForm" property="adjustmentFlag" value="true">
						<td>
							<html:select styleClass="dr-menu" property="ampAdjustmentId">
								<html:option value="1">Actual</html:option>
								<html:option value="0">Planned</html:option>
							</html:select>
						</td>
						</logic:equal>-->

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="currencyColl">
						<td>
							<html:select property="ampCurrencyCode" styleClass="dr-menu" >
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="currencyColl" value="currencyCode" label="currencyName"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="fiscalYears">
						<td>
							<html:select property="fiscalCalId" styleClass="dr-menu">
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="fiscalYears" value="ampFiscalCalId" label="name"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="ampFromYears">
						<td>
							<html:select property="ampFromYear" styleClass="dr-menu">

								<html:options name="aimMulitlateralbyDonorForm" property="ampFromYears" /> 
							</html:select>
						</td>							
	 					</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="ampToYears">
						<td>
							<html:select property="ampToYear" styleClass="dr-menu">

								<html:options name="aimMulitlateralbyDonorForm" property="ampToYears" /> 
							</html:select>
						</td>							
	 					</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="regionColl">
						<td>
							<html:select property="ampLocationId" styleClass="dr-menu" >
								<option value="All">All Regions</option>
									<html:optionsCollection name="aimMulitlateralbyDonorForm" property="regionColl" value="name" 
									label="name"  /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="modalityColl">
						<td>						
							<html:select property="ampModalityId" styleClass="dr-menu" >
								<option value="0">All Financing Instruments</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="modalityColl" value="ampModalityId" label="name" /> 
							</html:select>
						</td>
		 				</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="donorColl">
						<td>
							<html:select property="ampOrgId" styleClass="dr-menu" >
								<option value="0">All Donors</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="donorColl" value="ampOrgId" label="acronym"/> 
							</html:select>
						<td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="statusColl">
						<td>
							<html:select property="ampStatusId" styleClass="dr-menu" >
								<option value="0">All Status</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="statusColl" value="ampStatusId" label="name"  /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="sectorColl">
						<td>
							<html:select property="ampSectorId" styleClass="dr-menu" >
								<option value="0">All Sectors</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="sectorColl" value="ampSectorId" label="name" /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="ampStartDays">
						<td>
						
	 						<html:select property="startDay" styleClass="dr-menu">
								<option value="0">DD</option>
								<html:options name="aimMulitlateralbyDonorForm" 	property="ampStartDays" /> 
							</html:select>
							
		 				</td>
						<td>
	 						<html:select property="startMonth" styleClass="dr-menu">
								<option value="0">MON</option>
	 							<html:option value="1">Jan</html:option>
								<html:option value="2">Feb</html:option>
								<html:option value="3">March</html:option>
								<html:option value="4">April</html:option>
								<html:option value="5">May</html:option>
								<html:option value="6">June</html:option>
								<html:option value="7">July</html:option>
								<html:option value="8">Aug</html:option>
								<html:option value="9">Sept</html:option>
								<html:option value="10">Oct</html:option>
								<html:option value="11">Nov</html:option>
								<html:option value="12">Dec</html:option>
							</html:select>
	 					</td>
						<td>
	 						<html:select property="startYear" styleClass="dr-menu">
							<option value="0">YYYY</option>
							<html:options name="aimMulitlateralbyDonorForm" property="ampStartYears" /> 
							</html:select> 
		 				</td>
						</logic:notEmpty>
						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="ampCloseDays">
						<td>
	 						<html:select property="closeDay" styleClass="dr-menu">
							<option value="0">DD</option>
							<html:options name="aimMulitlateralbyDonorForm" property="ampCloseDays" /> 
							</html:select> 
	 					</td>
						<td>
	 						<html:select property="closeMonth" styleClass="dr-menu">
	 						<option value="0">MON</option>
	 						<html:option value="1">Jan</html:option>
							<html:option value="2">Feb</html:option>
							<html:option value="3">March</html:option>
							<html:option value="4">April</html:option>
							<html:option value="5">May</html:option>
							<html:option value="6">June</html:option>
							<html:option value="7">July</html:option>
							<html:option value="8">Aug</html:option>
							<html:option value="9">Sept</html:option>
							<html:option value="10">Oct</html:option>
							<html:option value="11">Nov</html:option>
							<html:option value="12">Dec</html:option>
							</html:select>
	 					</td>
						<td>
							<html:select property="closeYear" styleClass="dr-menu">
							<option value="0">YYYY</option>
							<html:options name="aimMulitlateralbyDonorForm" property="ampCloseYears" /> 
							</html:select> 
						</td>
						</logic:notEmpty>
						<logic:equal name="aimMulitlateralbyDonorForm" property="goFlag" value="true">
						<td>
						<input type="button" name="GOButton" value=" GO " class="dr-menu" onclick="clearFilter()">
						</td>
						</logic:equal>
                </tr>
              </table></td>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>
       <!--     <tr>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
			<td width="2" valign="middle">&nbsp;</td>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>-->
		
			<tr>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
              <td >
			  <table width="100%"  border="0" cellpadding="0" cellspacing="1" bgcolor="8B8B83">
            <tr bgcolor="#FFFFFF">
              <td colspan=<bean:write name="aimMulitlateralbyDonorForm" property="totalColumns"/>><table border="0" cellspacing="0" cellpadding="0">
                  <tr bgcolor="#C9C9C7">
                  <td bgcolor="#C9C9C7" class="box-title">&nbsp;
					<digi:trn key="aim:ProjectbyDonor">Commitment Disbursement by Project by Donor</digi:trn>
                  </td>
                 <td bgcolor="#FFFFFF"><img src="../ampTemplate/images/corner-r.gif" width="17" height="17"></td>
                 </tr>
      				</table>

</tr>
<tr bgcolor="#F4F4F2">
	<td rowspan="3" align="center" height="21" width="4%" > 
	<a title="<digi:trn key="aim:SerialNumber">Serial Number</digi:trn>">
	<digi:trn key="aim:SerialNo">S.No  </digi:trn>
	</a>
	</td>
	<td rowspan="3" align="center" height="21" width="12%"> 
	<a title="<digi:trn key="aim:DonorName">The country or agency that financed the project</digi:trn>">
	<digi:trn key="aim:donor">Donor</digi:trn>
	</a>
	</td>
	
		<td colspan=<bean:write name="aimMulitlateralbyDonorForm" property="yearCount" /> align="center" height="21"  width="63%"> <strong>
		<digi:trn key="aim:year">Year</digi:trn></strong>
		</td>
		<td rowspan="2" colspan="3" align="center" height="21" width="20%">
		<digi:trn key="aim:total">Total</digi:trn>
		</td>
		
</tr>
	<tr bgcolor="#F4F4F2">
		<logic:iterate name="aimMulitlateralbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
			<td colspan=3 align="center" height="21" width="21%">
					<%=fiscalYearRange%>
			</td>
			
		</logic:iterate>
	</tr>
	<tr bgcolor="#F4F4F2">
	<logic:iterate name="aimMulitlateralbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
		<td align="center" height="21" width="7%"> 
		<a title="<digi:trn key="aim:PlannedDisbursementofFund">Expected disbursements</digi:trn>">
		<digi:trn key="aim:plannedDisbursements">Planned Disbursements</digi:trn>
		</a>
		</td>
		<td align="center" height="21" width="7%"> 
		<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
		<digi:trn key="aim:disbursements">Disbursements</digi:trn>
		</a>
		</td>
		<td align="center" height="21" width="7%"> 
		<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
		<digi:trn key="aim:expenditures">Expenditures</digi:trn>
		</a>
		</td>
	</logic:iterate>
		<td align="center" height="21" width="7%"> 
		<a title="<digi:trn key="aim:PlannedDisbursementofFund">Expected disbursements</digi:trn>">
		<digi:trn key="aim:plannedDisbursements">Planned Disbursements</digi:trn>
		</a>
		</td>
		<td align="center" height="21" width="7%"> 
		<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>"><digi:trn key="aim:disbursements">Disbursements</digi:trn>
		</a>
		</td>
		<td align="center" height="21" width="7%"> 
		<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
		<digi:trn key="aim:expenditures">Expenditures</digi:trn>
		</a>
		</td>
	</tr>
	<logic:empty name="aimMulitlateralbyDonorForm" property="multiReport"> 
		<tr><td bgcolor="#ffffff" align="center" colspan=<bean:write name="aimMulitlateralbyDonorForm" property="totalColumns"/>>
		<b>
		<digi:trn key="aim:noRecords">No Records</digi:trn>
		</b>
		</td></tr>
		</logic:empty>
		
	<logic:notEmpty name="aimMulitlateralbyDonorForm"  property="multiReport">
	<logic:iterate name="aimMulitlateralbyDonorForm"  property="multiReport" id="multiReport" type="org.digijava.module.aim.helper.multiReport">
	<tr bgcolor="white">
		<td align="center"></td>
			<td align="left" colspan=<bean:write name="aimMulitlateralbyDonorForm" property="totalColumns"/> height="21"> 
				<strong><bean:write name="multiReport" property="teamName"/></strong>
			</td>
		</tr>
	<logic:iterate name="multiReport"  property="donors" id="donors" type="org.digijava.module.aim.helper.AmpTeamDonors">
		<tr bgcolor="white">
		<td align="center"><bean:write name="donors" property="donorCount"/></td>
			<td align="left" colspan=<bean:write name="aimMulitlateralbyDonorForm" property="totalColumns"/> height="21"> 
				<strong><bean:write name="donors" property="donorAgency"/></strong>
			</td>
		</tr>
		
		<logic:iterate name="donors"  property="project" id="project" type="org.digijava.module.aim.helper.Project">
			<tr bgcolor="white">
			<td align="center" rowspan=<bean:write name="project" property="rowspan"/>><bean:write name="donors" property="donorCount"/>.<bean:write name="project" property="count"/></td>
				<td align="left" height="21" > 
					<bean:write name="project" property="name"/>
				</td>
				<logic:iterate name="project"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
					<td align="right" height="21" >
						<logic:notEqual name="ampFund" property="commAmount"  value="0" >
						<bean:write name="ampFund" property="commAmount" />
						</logic:notEqual>
					</td>
					<td align="right" height="21"> 
						<logic:notEqual name="ampFund" property="disbAmount"  value="0" >
						<bean:write name="ampFund" property="disbAmount" />
						</logic:notEqual>
					</td>
					<td align="right" height="21" > 
						<logic:notEqual name="ampFund" property="expAmount"  value="0" >
						<bean:write name="ampFund" property="expAmount" />
						</logic:notEqual>
					</td>
				</logic:iterate>
				<td align="right" height="21"> 
					<logic:notEqual name="project" property="projCommAmount"  value="0" >
					<bean:write name="project" property="projCommAmount"/>
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="project" property="projDisbAmount"  value="0" >
					<bean:write name="project" property="projDisbAmount"/>
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="project" property="projExpAmount"  value="0" >
					<bean:write name="project" property="projExpAmount"/>
					</logic:notEqual>
				</td>
			</tr>
			<logic:notEmpty name="project"  property="termAssist">
			<logic:iterate name="project"  property="termAssist" id="termAssist" type="org.digijava.module.aim.helper.ProjectTermAssist">
		<tr bgcolor="white">
			
				<td align="left" height="21" > 
					<bean:write name="termAssist" property="termAssistName"/>
				</td>
				<logic:iterate name="termAssist"  property="termAssistFund" id="termAssistFund" type="org.digijava.module.aim.helper.AmpFund">
					<td align="right" height="21" >
						<logic:notEqual name="termAssistFund" property="commAmount"  value="0" >
						<bean:write name="termAssistFund" property="commAmount" />
						</logic:notEqual>
					</td>
					<td align="right" height="21"> 
						<logic:notEqual name="termAssistFund" property="disbAmount"  value="0" >
						<bean:write name="termAssistFund" property="disbAmount" />
						</logic:notEqual>
					</td>
					<td align="right" height="21" > 
						<logic:notEqual name="termAssistFund" property="expAmount"  value="0" >
						<bean:write name="termAssistFund" property="expAmount" />
						</logic:notEqual>
					</td>
				</logic:iterate>
				<td align="right" height="21"> 
					<logic:notEqual name="termAssist" property="termCommAmount"  value="0" >
					<bean:write name="termAssist" property="termCommAmount"/>
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termAssist" property="termDisbAmount"  value="0" >
					<bean:write name="termAssist" property="termDisbAmount"/>
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termAssist" property="termExpAmount"  value="0" >
					<bean:write name="termAssist" property="termExpAmount"/>
					</logic:notEqual>
				</td>
			</tr>
		</logic:iterate></logic:notEmpty>
		</logic:iterate>
		<logic:notEmpty name="donors"  property="totalDonorTermAssistFund">
		<logic:iterate name="donors"  property="totalDonorTermAssistFund" id="totalDonorTermAssistFund" type="org.digijava.module.aim.helper.TermFund">
		<tr bgcolor="#C9C9C7"><td>&nbsp;</td>
			<td align="left" height="21">Total <bean:write name="totalDonorTermAssistFund" property="termAssistName" /></td>
			<logic:iterate name="totalDonorTermAssistFund"  property="termFundTotal" id="termFundTotal" type="org.digijava.module.aim.helper.TermFundTotal">
				<td align="right" height="21"> 
					<logic:notEqual name="termFundTotal" property="totCommAmount"  value="0" >
					<br><bean:write name="termFundTotal" property="totCommAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21">
					<logic:notEqual name="termFundTotal" property="totDisbAmount"  value="0" >
					<br><bean:write name="termFundTotal" property="totDisbAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termFundTotal" property="totExpAmount"  value="0" >
					<br><bean:write name="termFundTotal" property="totExpAmount" />
					</logic:notEqual>
				</td>
			</logic:iterate>
			<td align="right" height="21"><br>
				<logic:notEqual name="totalDonorTermAssistFund" property="totDonorCommAmount"  value="0" >
				<bean:write name="totalDonorTermAssistFund" property="totDonorCommAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="totalDonorTermAssistFund" property="totDonorDisbAmount"  value="0" >
				<bean:write name="totalDonorTermAssistFund" property="totDonorDisbAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="totalDonorTermAssistFund" property="totDonorExpAmount"  value="0" >	
				<bean:write name="totalDonorTermAssistFund" property="totDonorExpAmount"/>
				</logic:notEqual>
			</td>
		</tr>
	</logic:iterate></logic:notEmpty>
		<tr bgcolor="#C9C9C7"><td>&nbsp;</td>
			<td align="left" height="21">
			<digi:trn key="aim:totalFor">Total for </digi:trn>
			<bean:write name="donors" property="donorAgency" /></td>
			<logic:iterate name="donors"  property="totalDonorFund" id="totalDonorFund" type="org.digijava.module.aim.helper.FundTotal">
				<td align="right" height="21"> 
					<logic:notEqual name="totalDonorFund" property="totCommAmount"  value="0" >	
					<br><bean:write name="totalDonorFund" property="totCommAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21">
					<logic:notEqual name="totalDonorFund" property="totDisbAmount"  value="0" >	
					<br><bean:write name="totalDonorFund" property="totDisbAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="totalDonorFund" property="totExpAmount"  value="0" >	
					<br><bean:write name="totalDonorFund" property="totExpAmount" />
					</logic:notEqual>
				</td>
			</logic:iterate>
			<td align="right" height="21"> <br>
				<logic:notEqual name="donors" property="donorCommAmount"  value="0" >	
				<bean:write name="donors" property="donorCommAmount"/></td>
				</logic:notEqual>
			<td align="right" height="21"><br>
			 	<logic:notEqual name="donors" property="donorDisbAmount"  value="0" >	
				<bean:write name="donors" property="donorDisbAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="donors" property="donorExpAmount"  value="0" >	
				<bean:write name="donors" property="donorExpAmount"/>
				</logic:notEqual>
			</td>
		</tr>
		
				</logic:iterate>
	<logic:iterate name="multiReport"  property="totalTeamTermAssistFund" id="totalTeamTermAssistFund" type="org.digijava.module.aim.helper.TermFund">
		<tr bgcolor="#C9C9C7"><td>&nbsp;</td>
			<td align="left" height="21">Total <bean:write name="totalTeamTermAssistFund" property="termAssistName" /></td>
			<logic:iterate name="totalTeamTermAssistFund"  property="termFundTotal" id="termFundTotal" type="org.digijava.module.aim.helper.TermFundTotal">
				<td align="right" height="21"> 
					<logic:notEqual name="termFundTotal" property="totCommAmount"  value="0" >	
					<br><bean:write name="termFundTotal" property="totCommAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21">
					<logic:notEqual name="termFundTotal" property="totDisbAmount"  value="0" >
					<br><bean:write name="termFundTotal" property="totDisbAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termFundTotal" property="totExpAmount"  value="0" >				
					<br><bean:write name="termFundTotal" property="totExpAmount" />
					</logic:notEqual>
				</td>
			</logic:iterate>
			<td align="right" height="21"> <br>
				<logic:notEqual name="totalTeamTermAssistFund" property="totDonorCommAmount"  value="0" >
				<bean:write name="totalTeamTermAssistFund" property="totDonorCommAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="totalTeamTermAssistFund" property="totDonorDisbAmount"  value="0" >
				<bean:write name="totalTeamTermAssistFund" property="totDonorDisbAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="totalTeamTermAssistFund" property="totDonorExpAmount"  value="0" >
				<bean:write name="totalTeamTermAssistFund" property="totDonorExpAmount"/>
				</logic:notEqual>
			</td>
		</tr>
	</logic:iterate>		
	<tr bgcolor="#C9C9C7"><td>&nbsp;</td>
			<td align="left" height="21">
			<digi:trn key="aim:totalFor">Total for </digi:trn>
			<bean:write name="multiReport" property="teamName" /></td>
			<logic:iterate name="multiReport"  property="totalTeamFund" id="totalTeamFund" type="org.digijava.module.aim.helper.FundTotal">
				<td align="right" height="21"> 
					<logic:notEqual name="totalTeamFund" property="totCommAmount"  value="0" >
					<br><bean:write name="totalTeamFund" property="totCommAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21">
					<logic:notEqual name="totalTeamFund" property="totDisbAmount"  value="0" >
					<br><bean:write name="totalTeamFund" property="totDisbAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="totalTeamFund" property="totExpAmount"  value="0" >
					<br><bean:write name="totalTeamFund" property="totExpAmount" />
					</logic:notEqual>
				</td>
			</logic:iterate>
			<td align="right" height="21"> <br>
				<logic:notEqual name="multiReport" property="teamCommAmount"  value="0" >
				<bean:write name="multiReport" property="teamCommAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="multiReport" property="teamDisbAmount"  value="0" >	
				<bean:write name="multiReport" property="teamDisbAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="multiReport" property="teamExpAmount"  value="0" >
				<bean:write name="multiReport" property="teamExpAmount"/>
				</logic:notEqual>
			</td>
		</tr>
		
	</logic:iterate>
	</logic:notEmpty>			
</table>
            </td>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>

		  </td>
		</tr>

</table>

</digi:form>
