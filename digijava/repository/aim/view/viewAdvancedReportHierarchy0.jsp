<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimAdvancedReportForm" />
           
			    <table width="100%"  border="0" cellpadding="1" cellspacing="1" bgcolor="#C6C7C4">
                    <logic:equal name="aimAdvancedReportForm" property="option" value="A">	
                    <tr bgcolor="#F4F4F2">
					<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
					<td align="center" height="21" width="42" ><div align="center"><strong>
					<bean:write name="titles" property="columnName" /></strong></div>
					</td></logic:iterate>
					<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
						<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center" >
						<div align="center"><strong><%=fiscalYearRange%></strong></div>
						</td>
					</logic:iterate>
					
					
				<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" >
					<b>Total 	</b>
				</td>
			
				</tr>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="option" value="Q">	
                    <tr bgcolor="#F4F4F2">
					<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
					<td align="center" height="21" width="42" rowspan="2" ><div align="center"><strong>
					<bean:write name="titles" property="columnName" /></strong></div>
					</td></logic:iterate>
					<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
						<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="quarterColumns" /> align="center"><strong><%=fiscalYearRange%></strong>
						</td>
					</logic:iterate>
					
					
				<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" rowspan="2">
					<b>Total 	</b>
				</td>
			
				</tr>
				<tr bgcolor="#F4F4F2">
				<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q1</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q2</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q3</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q4</strong></td>
				</logic:iterate>
				</tr>
				</logic:equal>
				<tr bgcolor="#FFFFFF">
				<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
			<td>&nbsp;</td></logic:iterate>
			<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
			<logic:iterate name="aimAdvancedReportForm"  property="options" id="options">
				<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
					<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
					</a>
					</td>
				</logic:equal>

				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
					<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
					</a>
					</td>
				</logic:equal>
			</logic:iterate>
			</logic:iterate>
			
					<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
					<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
					</a>
					</td>
				</logic:equal>


				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
					<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
					</a>
					</td>
				</logic:equal>

				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
					<td height="21" width="23" align="center" >
					<digi:trn key="aim:undisbursed">Undisbursed</digi:trn>
					</td>
				</logic:equal>

			
				<logic:empty name="aimAdvancedReportForm" property="report"> 
		<tr bgcolor="#F4F4F2">
				<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
				<td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/> align="center" height="21" >
				<b>
					<digi:trn key="aim:noRecords">No Records</digi:trn>
				</b>
				</td>
			</tr>
		</logic:empty>
	
		<bean:define id="measureCnt" >
			<bean:write property="measureCount" name="aimAdvancedReportForm"/>
		</bean:define>
	
		<logic:notEmpty name="aimAdvancedReportForm" property="report"> 
		<logic:iterate name="aimAdvancedReportForm"  property="report" id="report" type="org.digijava.module.aim.helper.Report">
		<tr bgcolor="#F4F4F2">
		<logic:iterate name="report"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
		<logic:notEmpty name="records" property="title">
				<td align="center" height="21" >
				<bean:write name="records" property="title" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="actualStartDate">
						<td align="center" height="21" >
				<bean:write name="records" property="actualStartDate" />
				</td></logic:notEmpty>
				<logic:notEmpty name="records" property="actualCompletionDate">
						<td align="center" height="21" >
				<bean:write name="records" property="actualCompletionDate" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="status">
						<td align="center" height="21" >
				<bean:write name="records" property="status" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="level">
						<td align="center" height="21" >
				<bean:write name="records" property="level" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="objective">
						<td align="center" height="21" >
				<bean:write name="records" property="objective" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="description">
				<td align="center" height="21" width="800">
					<bean:define id="descriptionKey">
						<bean:write name="records" property="description" />
					</bean:define>						
				<digi:edit key="<%=descriptionKey%>" />
				</td></logic:notEmpty>

				<logic:notEmpty name="records" property="assistance">
						<td align="center" height="21" >
				<logic:iterate name="records" id="assistance" property="assistance"> <%=assistance%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="donors">
						<td align="center" height="21" >
				<logic:iterate name="records" id="donors" property="donors"> <%=donors%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
					<logic:notEmpty name="records" property="sectors">
							<td align="center" height="21" >
				<logic:iterate name="records" id="sectors" property="sectors"> <%=sectors%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="regions">
						<td align="center" height="21" >
				<logic:iterate name="records" id="regions" property="regions"> <%=regions%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="contacts">
						<td align="center" height="21" >
				<logic:iterate name="records" id="contacts" property="contacts"> <%=contacts%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="modality">
						<td align="center" height="21" >
				<logic:iterate name="records" id="modality" property="modality"> <%=modality%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="ampId">
						<td align="center" height="21" >
				<bean:write name="records" property="ampId" /></td></logic:notEmpty>

				<logic:notEmpty name="records" property="totalCommitment">
					<td align="center" height="21" >
					<logic:notEqual name="records" property="totalCommitment" value="0">
					<bean:write name="records" property="totalCommitment" /></logic:notEqual></td></logic:notEmpty>

				<logic:notEmpty name="records" property="totalDisbursement">
						<td align="center" height="21" >
						<logic:notEqual name="records" property="totalDisbursement" value="0">
				<bean:write name="records" property="totalDisbursement" /></logic:notEqual></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="ampFund">
				<logic:iterate name="records"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
				<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="commAmount" value="0">
							<bean:write name="ampFund" property="commAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="disbAmount" value="0">
							<bean:write name="ampFund" property="disbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="expAmount" value="0">
							<bean:write name="ampFund" property="expAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="plCommAmount" value="0">
							<bean:write name="ampFund" property="plCommAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
							<bean:write name="ampFund" property="plDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">

						<td align="right" height="21" width="69" >
							<logic:notEqual name="ampFund" property="plExpAmount" value="0">
							<bean:write name="ampFund" property="plExpAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>

				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				<logic:notEmpty name="ampFund" property="unDisbAmount">
						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
							<bean:write name="ampFund" property="unDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:notEmpty>
				</logic:equal>
					</logic:iterate></logic:notEmpty>
				</logic:iterate>
			</tr>
			</logic:iterate>
		
              
              
			  <tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> Total</b></td>
			  <logic:iterate name="aimAdvancedReportForm"  property="totFund" id="totFund" type="org.digijava.module.aim.helper.AmpFund">
			  <logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			  
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="commAmount" value="0">
							<bean:write name="totFund" property="commAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
				
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="disbAmount" value="0">
							<bean:write name="totFund" property="disbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
				
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="expAmount" value="0">
							<bean:write name="totFund" property="expAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
				
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="plCommAmount" value="0">
							<bean:write name="totFund" property="plCommAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
				
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="plDisbAmount" value="0">
							<bean:write name="totFund" property="plDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
				
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="plExpAmount" value="0">
							<bean:write name="totFund" property="plExpAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				
				<logic:notEmpty name="totFund" property="unDisbAmount">
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="unDisbAmount" value="0">
							<bean:write name="totFund" property="unDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:notEmpty>		
						
				</logic:equal>
					</logic:iterate>
				</tr>
		</logic:notEmpty>
	
                     </table>
