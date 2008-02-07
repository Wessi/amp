<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<!-- invoked to close myself and reload my parent (after save was performed) -->
<logic:present name="close">
<script type="text/javascript">
	window.opener.location.href = window.opener.location.href;
	window.close();
</script>	
</logic:present>

<script type="text/javascript">
function clearDefault(editBox)
{
		if(editBox.value=='Amount') editBox.value='';
}
function fnChk(frmContrl){
  <c:set var="errMsgAddNumericValue">
  <digi:trn key="aim:addNumericValueErrorMessage">
  Please enter numeric value only
  </digi:trn>
  </c:set>
  if (isNaN(frmContrl.value)) {
    alert("${errMsgAddNumericValue}");
    frmContrl.value = "";
    return false;
  }
  return true;
}

function selectOrganisation1() {
		openNewWindow(650, 420);
		<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true&edit=true&step=3" />
		document.aimEditActivityForm.action = "<%= selectOrganization %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
}
</script>

<!-- code for rendering that nice calendar -->


<body onload="load()">
<digi:instance property="aimIPAContractForm" />
<digi:form action="/editIPAContract.do" method="post">

<input type="hidden" name="edit" value="true">
<html:hidden property="id"/>
<html:hidden property="indexId"/>

<digi:errors/>

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
<tr><td colspan="4" bgcolor="#006699" class="textalb" align="center">
<digi:trn key="aim:IPA:popup:Title">Add/Edit IPA Contract</digi:trn>
</td></tr>
	<tr>
		<td align="right">
                   
		<b><digi:trn key="aim:IPA:popup:name">Contract name:</digi:trn></b>
		</td>
		<td>
		         	<html:text property="contractName" size="30"/> 
		</td>
	
	</tr>

	<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:description">Description:</digi:trn></b>
		</td>
		<td>
			<html:textarea property="description" rows="3" cols="90" styleClass="inp-text"/>
		</td>
	</tr>
	
	<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:actCat">Activity Category:</digi:trn></b>
		</td>
		<td>
			<html:select property="activityCategoryId" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:select">Select</digi:trn></option>
			<logic:iterate id="actCat" name="aimIPAContractForm" property="activitiyCategories">
				<c:set var="trn">
					<digi:trn key="aim:ipa:popup:${actCat.id}">${actCat.value}</digi:trn>
				</c:set>
				<html:option value="${actCat.id}">${trn}</html:option>			
			</logic:iterate>			
			</html:select>
		</td>
	</tr>
	
		<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup">Start of Tendering:</digi:trn></b>
		</td>
		<td>
			<html:text readonly="true" property="startOfTendering" styleClass="inp-text" styleId="startOfTendering"/>
                          <a id ="startOfTenderingDate" href='javascript:pickDateById("startOfTenderingDate","startOfTendering")'>
				<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
                          </a>
                         
			</td>
		
	</tr>	

		<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:signatureOfContract">Signature of Contract:</digi:trn></b>
		</td>
		<td>
			<html:text readonly="true" property="signatureOfContract" styleClass="inp-text" styleId="signatureOfContract"/>
                           <a id="signatureOfContractDate" href='javascript:pickDateById("signatureOfContractDate","signatureOfContract")'>
								<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
                          </a>
			</td>
		
	</tr>	
          <tr>
	<td align="right">
		<b><digi:trn key="aim:IPA:popup:contractingOrg">Contracting Organisation:</digi:trn></b>
	</td>
         <td>
         <html:select property="contrOrg" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:selectOrganisation">Select Organisation</digi:trn></option>
			<logic:iterate id="actOrg" name="aimIPAContractForm" property="organisations">
				<c:set var="trn">
					<digi:trn key="aim:ipa:popup:${actOrg.ampOrgId}">${actOrg.name}</digi:trn>
				</c:set>
				<html:option value="${actOrg.ampOrgId}">${trn}</html:option>			
			</logic:iterate>
			</html:select>
        </td>
	</tr>
	
		<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:contractCompletion">Contract Completion:</digi:trn></b>
		</td>
		<td>
			<html:text readonly="true" property="contractCompletion" styleClass="inp-text" styleId="contractCompletion"/>
                          <a id="contractCompletionDate" href='javascript:pickDateById("contractCompletionDate","contractCompletion")'>
								<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
                          </a>
		</td>
		
	</tr>	
         <tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:status">Status:</digi:trn></b>
		</td>
		<td>
			<html:select property="statusId" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:select">Select</digi:trn></option>
			<logic:iterate id="actstat" name="aimIPAContractForm" property="statuses">
				<c:set var="trn">
					<digi:trn key="aim:ipa:popup:${actstat.id}">${actstat.value}</digi:trn>
				</c:set>
				<html:option value="${actstat.id}">${trn}</html:option>			
			</logic:iterate>			
			</html:select>
        </td>
	</tr>	
         
        
	

		<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:totalECContribution">Total EC Contribution:</digi:trn></b>
		</td>
		</tr>
	
		<tr>
		<td align="right">
		<b><digi:trn key="aim:ipa:popup:ib">IB</digi:trn>:</b>
		</td>
		<td>
			<html:text property="totalECContribIBAmount" style="text-align:right" onkeyup="fnChk(this)"/>
			<html:select property="totalECContribIBCurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
	</tr>

		<tr>
		<td align="right">
		<b><digi:trn key="aim:ipa:popup:inv">INV</digi:trn>:</b>
		</td>
		<td>
			<html:text property="totalECContribINVAmount" style="text-align:right" onkeyup="fnChk(this)"/>
			<html:select property="totalECContribINVCurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
	</tr>





		<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:totalNationalContribution">Total National Contribution:</digi:trn></b>
		</td>
		</tr>
	
		<tr>
		<td align="right">
		<b><digi:trn key="aim:ipa:popup:central">Central</digi:trn>:</b>
		</td>
		<td>
			<html:text property="totalNationalContribCentralAmount" style="text-align:right" onkeyup="fnChk(this)"/>
			<html:select property="totalNationalContribCentralCurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
	</tr>

		<tr>
		<td align="right">
		<b><digi:trn key="aim:ipa:popup:regional">Regional</digi:trn>:</b>
		</td>
		<td>
			<html:text property="totalNationalContribRegionalAmount" style="text-align:right" onkeyup="fnChk(this)"/>
			<html:select property="totalNationalContribRegionalCurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
		</tr>

	<tr>
		<td align="right">
		<b><digi:trn key="aim:ipa:popup:ifis">IFIs</digi:trn>:</b>
		</td>
		<td>
			<html:text property="totalNationalContribIFIAmount" style="text-align:right" onkeyup="fnChk(this)"/>
			<html:select property="totalNationalContribIFICurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
		</tr>



	<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:totalPrivateContribution">Total Private Contribution:</digi:trn></b>
		</td>
		</tr>
	
		<tr>
		<td align="right">
		<b><digi:trn key="aim:ipa:popup:ib">IB</digi:trn>:</b>
		</td>
		<td>
			<html:text property="totalPrivateContribAmount" style="text-align:right" onkeyup="fnChk(this)"/>
			<html:select property="totalPrivateContribCurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
	</tr>




	<tr>
		<td>&nbsp;</td>
		<td bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:popup:disbursements">Disbursements:</digi:trn></b>
		</td>
	</tr>
	<tr>
		<td>&nbsp;
		</td>
		<td>
		<table>
	
	<logic:notEmpty name="aimIPAContractForm" property="contractDisbursements">
						<c:forEach  items="${aimIPAContractForm.contractDisbursements}" var="contractDisbursement"  varStatus="idx" >
							<tr>
                                                         <td>
                                                            <html:hidden property="${contractDisbursement}" value="${id}"/>
                                                              <html:multibox property="selContractDisbursements" value="${idx.count}"/>
                                                                
                                                         </td>
							<td align="right" valign="top">
							<html:select indexed="true" name="contractDisbursement" property="adjustmentType">
							<html:option value="0"><digi:trn key="aim:ipa:popup:actual"> Actual</digi:trn></html:option>
							<html:option value="1"><digi:trn key="aim:ipa:popup:planned">Planned</digi:trn></html:option>							
							</html:select>
							</td>
							<td align="right" valign="top">
							<html:text indexed="true" name="contractDisbursement" property="amount" onkeyup="fnChk(this)"><digi:trn key="aim:ipa:popup:amount">Amount</digi:trn></html:text>
							</td>
							<td align="right" valign="top">
                                                         <html:select name="contractDisbursement" indexed="true" property="currCode" styleClass="inp-text">
							<html:optionsCollection name="aimIPAContractForm" property="currencies" value="currencyCode" label="currencyName"/>
                                                        </html:select>
							</td>
							<td align="right" valign="top"  nowrap>
							<html:text readonly="true" indexed="true" name="contractDisbursement" property="disbDate" styleClass="inp-text" styleId="date${idx.count}"/>
                                                         <a id="image${idx.count}" href='javascript:pickDateById("image${idx.count}","date${idx.count}")'>
								<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
                                                         </a>
														
							</td>
							</tr>
						</c:forEach>
	</logic:notEmpty>						
		</table>		
		</td>		
		</tr>		
	
	<tr>
		<td>&nbsp;
		</td>
		<td>
		<tr><td colspan="2" align="center">
				<html:submit styleClass="buton" property="addFields"><digi:trn key="aim:IPA:popup:addDisbursement">Add Disbursement</digi:trn></html:submit>&nbsp;&nbsp;
				<html:submit styleClass="buton" property="removeFields"><digi:trn key="aim:IPA:popup:deleteSelected">Delete Selected</digi:trn></html:submit>				
		</td></tr>
	
	
	


	<tr>
		<td colspan="2" align="center">
		<html:submit styleClass="buton" property="save"><digi:trn key="aim:addEditActivityOK">OK</digi:trn></html:submit>
		
		&nbsp;&nbsp;
		<html:button styleClass="buton" property="cancel" onclick="window.close();">
		<digi:trn key="aim:addEditActivityCancel">Cancel</digi:trn>
		</html:button>
		</td>
	</tr>	

	
</table>

</digi:form>