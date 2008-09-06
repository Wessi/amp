<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<!-- expenditures -->
<digi:instance property="aimEditActivityForm" />

<tr bgcolor="#FFFFCC">
	<td colspan="4" style="text-transform: uppercase"><a
		title='<digi:trn key="aim:ExpenditureofFund">Amount effectively spent by the implementing agency</digi:trn>'>

	<digi:trn key="aim:plannedexpenditures"> PLANNED EXPENDITURES</digi:trn>
	</a></td>
</tr>

<c:if test="${!empty funding.fundingDetails}">
	<logic:iterate name="funding" property="fundingDetails"
		id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">


		<logic:equal name="fundingDetail" property="transactionType" value="2">
			<logic:equal name="fundingDetail" property="adjustmentType" value="0">

						<tr bgcolor="#ffffff">
							<td align="right" bgcolor="#FFFFFF"><field:display
								name="Adjustment Type Expenditure"
								feature="Funding Information">
								<digi:trn
									key='<%="aim:expenditures:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
									<bean:write name="fundingDetail" property="adjustmentTypeName" />
								</digi:trn>
							</field:display></td>
							<td align="right"><field:display name="Date Expenditure"
								feature="Funding Information">
								<bean:write name="fundingDetail" property="transactionDate" />
							</field:display></td>

							<td align="right"><field:display name="Amount Expenditure"
								feature="Funding Information">
								<!-- <FONT color=blue>*</FONT> -->
								<bean:write name="fundingDetail" property="transactionAmount" />&nbsp;																								</field:display><field:display
								name="Currency Expenditure" feature="Funding Information">
								<bean:write name="fundingDetail" property="currencyCode" />
							</field:display></td>
							<td align="left" bgcolor="#ffffff"></td>
						</tr>
						<tr>
							<td colspan="4"></td>
						</tr>

			</logic:equal>
		</logic:equal>
	</logic:iterate>


	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn
			key="aim:subtotalActualExpenditures">
           	  SUBTOTAL PLANNED EXPENDITURES             </digi:trn></td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">
            ${funding.subtotalPlannedExpenditures} ${aimEditActivityForm.currCode}
           </td>
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="4" height="7px"></td>
	</tr>
	<tr bgcolor="#FFFFCC">
		<td colspan="4" style="text-transform: uppercase"><a
			title='<digi:trn key="aim:ExpenditureofFund">Amount effectively spent by the implementing agency</digi:trn>'>
		<digi:trn key="aim:actualexpenditures">ACTUAL EXPENDITURES</digi:trn>
		</a></td>
	</tr>
	<logic:iterate name="funding" property="fundingDetails"
		id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">


		<!--Actual-->
		<logic:equal name="fundingDetail" property="transactionType" value="2">
			<logic:equal name="fundingDetail" property="adjustmentType" value="1">

						<tr bgcolor="#ffffff">
							<td align="right" bgcolor="#FFFFFF"><field:display
								name="Adjustment Type Expenditure"
								feature="Funding Information">
								<digi:trn
									key='<%="aim:expenditures:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
									<bean:write name="fundingDetail" property="adjustmentTypeName" />
								</digi:trn>
							</field:display></td>
							<td align="right"><field:display name="Date Expenditure"
								feature="Funding Information">
								<bean:write name="fundingDetail" property="transactionDate" />
							</field:display></td>
							<td align="right"><field:display name="Amount Expenditure"
								feature="Funding Information">
								<!-- <FONT color="blue">*</FONT> -->
								<bean:write name="fundingDetail" property="transactionAmount" />&nbsp;																								</field:display><field:display
								name="Currency Expenditure" feature="Funding Information">
								<bean:write name="fundingDetail" property="currencyCode" />
							</field:display></td>
							<td align="left" bgcolor="#ffffff"></td>

						</tr>
						<field:display name="Classification Expenditure"
							feature="Funding Information">
							<tr>
								<td colspan="4"><bean:write name="fundingDetail"
									property="classification" /></td>
							</tr>
						</field:display>

			</logic:equal>
		</logic:equal>
	</logic:iterate>
	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn
			key="aim:subtotalplannedExpenditures">
       	 	 	SUBTOTAL ACTUAL EXPENDITURES       	 	 </digi:trn></td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">
 			${funding.subtotalExpenditures} ${aimEditActivityForm.currCode}
           </td>
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="4" height="7px"></td>
	</tr>
</c:if>
<!-- expenditures -->

