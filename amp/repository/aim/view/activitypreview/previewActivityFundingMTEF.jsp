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

<digi:instance property="aimEditActivityForm" />
<c:if test="${!empty funding.mtefDetails}">
    <tr bgcolor="#FFFFCC">
        <td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase;">
            <a><digi:trn key="aim:plannedexpenditures">MTEF Projections</digi:trn></a>
        </td>
		<td height="20" bgcolor="#FFFFCC" align="center">
			<c:if test="${aimEditActivityForm.funding.fixerate == true}">
				<b> <digi:trn key="aim:exchange">Exchange Rate</digi:trn> </b>
			</c:if>
		</td>        
    </tr>

	<logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="3">
			<%@include file="previewActivityFundingDetail.jspf" %>
		</logic:equal>
   	</logic:iterate>

	<tr>
		<td colspan="2" bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase">
            <digi:trn key='aim:subtotalActualdisbursement'>Subtotal MTEF Projections</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" align="right" bgcolor="#eeeeee"	style="border-top: 1px solid #000000">
			 <c:if test="${not empty funding.subtotalMTEFs}">
                <b>${funding.subtotalMTEFs} ${aimEditActivityForm.currName}</b>
             </c:if>&nbsp;
        </td>
                   
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
</c:if>

<tr>
    <td colspan="4" height="7px"></td>
</tr>



