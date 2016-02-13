<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %> 
<%@ taglib uri="/taglib/category" prefix="category" %>
            


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
<!--
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
  return autosum();
}

function fnChk1(frmContrl){
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

function autosum(){
	var v1 = document.aimIPAContractForm.totalECContribIBAmount.value;
	var v2 = document.aimIPAContractForm.totalECContribINVAmount.value;
	var v3 = document.aimIPAContractForm.totalNationalContribCentralAmount.value;
	var v4 = document.aimIPAContractForm.totalNationalContribIFIAmount.value;
	var v5 = document.aimIPAContractForm.totalNationalContribRegionalAmount.value;
	var v6 = document.aimIPAContractForm.totalPrivateContribAmount.value;
	
	
	document.aimIPAContractForm.totalAmount.value = (v1*1)+(v2*1)+(v3*1)+(v4*1)+(v5*1)+(v6*1);
	return true; 
}

function validate(){
    if (trim(document.aimIPAContractForm.contractName.value) == "") {
        <c:set var="translation">
        <digi:trn key="aim:pleaseEnterContractName">Please enter Contract Name</digi:trn>
        </c:set>
        alert("${translation}");
            document.aimIPAContractForm.contractName.focus();
            return false;
        }
    <c:set var="errMsgSelectCurrency">
    <digi:trn key="aim:PleaseSelectCurrency">
    Please Select Currency
    </digi:trn>
    </c:set>
    if(document.aimIPAContractForm.contractTotalValue!=null)
      {
      	if(document.aimIPAContractForm.contractTotalValue.value!='' && (document.aimIPAContractForm.totalAmountCurrency.value==-1))
	     {
	     	alert("${errMsgSelectCurrency}");
		    return false;
	      }
	 
      }
      else{
	    if((document.aimIPAContractForm.totalECContribIBAmount.value!='' ||
	   		document.aimIPAContractForm.totalECContribINVAmount.value!='' ||
	    	document.aimIPAContractForm.totalNationalContribIFIAmount.value!='' ||
	    	document.aimIPAContractForm.totalNationalContribCentralAmount.value!='' ||
	    	document.aimIPAContractForm.totalNationalContribRegionalAmount.value!='' ||
	    	document.aimIPAContractForm.totalPrivateContribAmount.value!='') &&
	        (document.aimIPAContractForm.totalAmountCurrency.value==-1))
	    {
	        alert("${errMsgSelectCurrency}");
	        return false;
	    }
	    }
			    
        mySaveReportEngine.saveContract();
        return true;
    }

function selectOrganisation1() {
		openNewWindow(650, 420);
		<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true&edit=true&step=3" />
		document.aimEditActivityForm.action = "<%= selectOrganization %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
}



var responseSuccess = function(o){ 
/* Please see the Success Case section for more
 * details on the response object's properties.
 * o.tId
 * o.status
 * o.statusText
 * o.getResponseHeader[ ]
 * o.getAllResponseHeaders
 * o.responseText
 * o.responseXML
 * o.argument
 */
	var response = o.responseText; 
	var content = document.getElementById("myContractContent");
    //response = response.split("<!")[0];
	content.innerHTML = response;
	showAddContract();
}
	 
var responseFailure = function(o){ 
// Access the response object's properties in the 
// same manner as listed in responseSuccess( ). 
// Please see the Failure Case section and 
// Communication Error sub-section for more details on the 
// response object's properties.
	alert("Connection Failure!"); 
}  
var callback = 
{ 
	success:responseSuccess, 
	failure:responseFailure 
};


function getContractDisbursments(){
	var divId = document.getElementById("ContractDisbursmentsList");
	var ret = "";
	if (divId != null){
		var elems = divId.getElementsByTagName("input");
		
		for (var i=0; i<elems.length; i++) {
			if (elems[i].type != "checkbox"){
				ret += elems[i].name + "=" + elems[i].value + "&";
			}
		}
		elems = divId.getElementsByTagName("select");
		for (var i=0; i<elems.length; i++) {
			ret += elems[i].name + "=" + elems[i].value + "&";
		}	
	}
	return ret;
}

function generateFields(){
	var ret = "";
	ret =			    
						
							"contractName="+document.getElementsByName("contractName")[0].value+"&"
						<field:display name="Contract Description" feature="Contracting">
							+ "description="+document.getElementsByName("description")[0].value+"&"
						</field:display>
						<field:display name="Contracting IPA Activity Category" feature="Contracting">
							+ "activityCategoryId="+document.getElementsByName("activityCategoryId")[0].value+"&"
						</field:display>
						<field:display name="Contracting IPA Contract Type" feature="Contracting">
							+ "typeId="+document.getElementsByName("typeId")[0].value+"&"
						</field:display>
						<field:display name="Contracting Start of Tendering" feature="Contracting"> 
							+ "startOfTendering="+document.getElementsByName("startOfTendering")[0].value+"&"
						</field:display>
						<field:display name="Contract Validity Date" feature="Contracting">
							+ "contractValidity="+document.getElementsByName("contractValidity")[0].value+"&"
						</field:display>
						<field:display name="Contracting Tab Status" feature="Contracting">
							+ "statusId="+document.getElementsByName("statusId")[0].value+"&"
						</field:display>
						<field:display name="Contracting IPA Contract Type" feature="Contracting">
							+ "contractTypeId="+document.getElementsByName("contractTypeId")[0].value+"&"
						</field:display>
						<field:display name="Signature of Contract" feature="Contracting">
							+ "signatureOfContract="+document.getElementsByName("signatureOfContract")[0].value+"&"
						</field:display>
						<field:display name="Contract Completion" feature="Contracting">
							+ "contractCompletion="+document.getElementsByName("contractCompletion")[0].value+"&"
						</field:display>
						<field:display name="Contracting Contractor Name" feature="Contracting">
							+ "contractingOrganizationText="+document.getElementsByName("contractingOrganizationText")[0].value+"&"
						</field:display>
						<field:display name="Contracting Total Amount" feature="Contracting">
							+ "totalAmount="+document.getElementsByName("totalAmount")[0].value+"&"
						</field:display>
						<field:display name="Contract Total Value" feature="Contracting">
							+ "contractTotalValue="+document.getElementsByName("contractTotalValue")[0].value+"&"
						</field:display>
						
						+ "totalAmountCurrency="+document.getElementsByName("totalAmountCurrency")[0].value+"&"
						<field:display name="Contracting IB" feature="Contracting">
							+ "totalECContribIBAmount="+document.getElementsByName("totalECContribIBAmount")[0].value+"&"
							+ "totalECContribIBAmountDate="+document.getElementsByName("totalECContribIBAmountDate")[0].value+"&"
						</field:display>
						//+ "totalECContribIBCurrency="+document.getElementsByName("totalECContribIBCurrency")[0].value+"&"
						<field:display name="Contracting INV" feature="Contracting">
							+ "totalECContribINVAmount="+document.getElementsByName("totalECContribINVAmount")[0].value+"&"
							+ "totalECContribINVAmountDate="+document.getElementsByName("totalECContribINVAmountDate")[0].value+"&"
						</field:display>
						//+ "totalECContribINVCurrency="+document.getElementsByName("totalECContribINVCurrency")[0].value+"&"
						<field:display name="Contracting Central Amount" feature="Contracting">
							+ "totalNationalContribCentralAmount="+document.getElementsByName("totalNationalContribCentralAmount")[0].value+"&"
							+ "totalNationalContribCentralAmountDate="+document.getElementsByName("totalNationalContribCentralAmountDate")[0].value+"&"
						</field:display>
						//+ "totalNationalContribCentralCurrency="+document.getElementsByName("totalNationalContribCentralCurrency")[0].value+"&"
						<field:display name="Contracting IFIs" feature="Contracting">
							+ "totalNationalContribIFIAmount="+document.getElementsByName("totalNationalContribIFIAmount")[0].value+"&"
							+ "totalNationalContribIFIAmountDate="+document.getElementsByName("totalNationalContribIFIAmountDate")[0].value+"&"
						</field:display>
						//+ "totalNationalContribIFICurrency="+document.getElementsByName("totalNationalContribIFICurrency")[0].value+"&"
						<field:display name="Contracting Regional Amount" feature="Contracting">
							+ "totalNationalContribRegionalAmount="+document.getElementsByName("totalNationalContribRegionalAmount")[0].value+"&"
							+ "totalNationalContribRegionalAmountDate="+document.getElementsByName("totalNationalContribRegionalAmountDate")[0].value+"&"
						</field:display>
						<field:display name="Total Private Contribution" feature="Contracting">
						//+ "totalNationalContribRegionalCurrency="+document.getElementsByName("totalNationalContribRegionalCurrency")[0].value+"&"
						+ "totalPrivateContribAmount="+document.getElementsByName("totalPrivateContribAmount")[0].value+"&"
						+ "totalPrivateContribAmountDate="+document.getElementsByName("totalPrivateContribAmountDate")[0].value+"&"
						</field:display>
						<field:display name="Contracting Disbursements Global Currency" feature="Contracting">
						//+ "totalPrivateContribCurrency="+document.getElementsByName("totalPrivateContribCurrency")[0].value+"&"
						+ "dibusrsementsGlobalCurrency="+document.getElementsByName("dibusrsementsGlobalCurrency")[0].value
						+ "&"
						</field:display>
						 + getContractDisbursments();
	
	return ret;
}

function addDisb() {
	var postString		= "addFields=true&"+generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
}

function orgsAdded() {
	var postString		= generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
}

function delOrgs() {
	var postString		= "removeOrgs=true&" + getCheckedFields("selOrgs")+"&"+generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);	
}

function getCheckedFields(name) {
	var ret			= "";
	//var ulEl		= document.getElementById( ulId );
	//var fields		= ulEl.getElementsByTagName( "input" );
	
	var elems = document.getElementsByName(name);
	
	for ( var i=0; i<elems.length; i++ ) {
		if (elems[i].checked){
			ret += name+"=";
			ret +=elems[i].value;//"true";
			if ( i < elems.length-1 )
				ret += "&";
		}
		//else
		//    ret +="false";
	}
	return ret;	
}

function delDisb() {
	var postString		= "removeFields=true&" + getCheckedFields("selContractDisbursements")+"&"+generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
}




function getContractActivityCategoryId() {
	return aimIPAContractForm.activityCategoryId.value;
}

function getContractActivityCategoryId() {
	return aimIPAContractForm.activityCategoryId.value;
}


function SaveReportEngine ( savingMessage, failureMessage ) {
;
}

function initScripts(){
	autosum();
}

SaveReportEngine.prototype.success		= function (o) {
	window.location.replace(window.location.href);
}
SaveReportEngine.prototype.failure			= function(o) {
	alert("Could not connect!");
}

SaveReportEngine.prototype.saveContract	= function () {
	var postString		= "save=true&"+generateFields();
	//alert (postString);
	
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", this, postString);
}

mySaveReportEngine = new SaveReportEngine();
window.onload=autosum;
-->
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
<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
<b><digi:trn key="aim:IPA:newPopup:title">Add/Edit Contracting</digi:trn></b>
</td></tr>
<field:display name="Contract Name" feature="Contracting">
	<tr>
		<td align="left" nowrap>
                     <FONT color=red>*</FONT>
		<digi:trn key="aim:IPA:newPopup:name">Contract Name</digi:trn>
		</td>
		<td>
		         	<html:text property="contractName" size="90"/> 
		</td>
	</tr>
</field:display>
<field:display name="Contract Description" feature="Contracting">
	<tr>
		<td align="left" valign="top" nowrap>
			<digi:trn key="aim:IPA:newPopup:description">Contract Description</digi:trn>
		</td>
		<td>
			<html:textarea property="description" rows="5" cols="87" styleClass="inp-text"/>
		</td>
	</tr>
</field:display>
<field:display name="Contracting Activity Category" feature="Contracting">
	<tr>
		
		<td colspan="2">
			<field:display name="Contracting IPA Activity Category" feature="Contracting">
				<digi:trn key="aim:IPA:newPopup:actCat">Activity Category</digi:trn>&nbsp;&nbsp;
				<category:showoptions name="aimIPAContractForm" property="activityCategoryId"  
									  keyName="<%= org.digijava.module.aim.helper.CategoryConstants.IPA_ACTIVITY_CATEGORY_KEY %>" 
									  styleClass="inp-text" />
				
				&nbsp;&nbsp;&nbsp;
			</field:display>
			<field:display name="Contracting IPA Contract Type" feature="Contracting">
				<digi:trn key="aim:IPA:newPopup:contractType">Contract Type</digi:trn>
				&nbsp;&nbsp;
	
				<category:showoptions name="aimIPAContractForm" property="contractTypeId"  
									  keyName="<%= org.digijava.module.aim.helper.CategoryConstants.IPA_TYPE_KEY %>" 
									  styleClass="inp-text" />
	
				&nbsp;&nbsp;&nbsp;
			</field:display>
			<field:display name="Contracting IPA Contract Type" feature="Contracting">
				<digi:trn key="aim:IPA:newPopup:actType">Activity Type</digi:trn>
				&nbsp;&nbsp;
				
				<category:showoptions name="aimIPAContractForm" property="typeId"  
									  keyName="<%= org.digijava.module.aim.helper.CategoryConstants.IPA_ACTIVITY_TYPE_KEY %>" 
									  styleClass="inp-text" />
			</field:display>
			
		</td>
	</tr>
</field:display>
         
         
	<tr><td id="calendarPosition" colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:details">Details</digi:trn></b>
	</td></tr>
         
	<tr>
	<td colspan="2">
	<table cellpadding="2" cellspacing="2" width="100%">
	<tr>
		<field:display name="Contracting Start of Tendering" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup">Start of Tendering</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" size="9" property="startOfTendering" styleClass="inp-text" styleId="startOfTendering"/>
				<a id ="startOfTenderingDate" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","startOfTendering",-250,-230)'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
				</a>
			</td>
		</field:display>

		<field:display name="Contract Validity Date" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:contractValidityDate">Contract Validity Date</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" size="9" property="contractValidity" styleClass="inp-text" styleId="contractValidity"/>
				<a id="contractValidityDate" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","contractValidity",-250,-230)'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
				</a>
			</td>
		</field:display>
		
		<field:display name="Contracting Tab Status" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:status">Status</digi:trn>
			</td>
			<td align="left">
				<category:showoptions name="aimIPAContractForm" property="statusId"  
					  keyName="<%= org.digijava.module.aim.helper.CategoryConstants.IPA_STATUS_KEY %>" 
					  styleClass="inp-text" />
			</td>
		</field:display>
	</tr>
	<tr>
		<field:display name="Signature of Contract" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:signatureOfContract">Signature of Contract</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" size="9" property="signatureOfContract" styleClass="inp-text" styleId="signatureOfContract"/>
				<a id="signatureOfContractDate" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","signatureOfContract",-250,-230)'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
				</a>
			</td>
		</field:display>
		<field:display name="Contract Completion" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:contractCompletion">Contract Completion</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" size="9" property="contractCompletion" styleClass="inp-text" styleId="contractCompletion"/>
				<a id="contractCompletionDate" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","contractCompletion",-250,-230)'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
				</a>
			</td>
		</field:display>
	</tr>

	
	<field:display name="Contracting Contractor Name" feature="Contracting">
		<tr>
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:contractingContractorName">Contractor Name</digi:trn>
			</td>
			<td colspan="5" align="left">
				<html:text  property="contractingOrganizationText" size="75" styleClass="inp-text"/>
			</td>
		</tr>				
	</field:display>
	</table>
	</td>
	</tr>	


	<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:organizations">Organization(s)</digi:trn></b>
	</td></tr>

	<tr>
		<td colspan="6" align="left">
			<field:display name="Contract Organization" feature="Contracting">
				<aim:addOrganizationButton form="${aimIPAContractForm}" collection="organisations" refreshParentDocument="false" callBackFunction="orgsAdded()" useClient="false"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
				&nbsp;
				<html:button styleClass="dr-menu" property="deleteOrgs" onclick="delOrgs();">
					<digi:trn key="aim:IPA:newPopup:removeOrganizations">Remove Organizations</digi:trn>
				</html:button>	
			</field:display>
		</td>
	</tr>
	<tr>
		<td colspan="6" align="left">
			<table>
				<c:forEach items="${aimIPAContractForm.organisations}" var="selectedOrganizations" varStatus="selIdx">
					<c:if test="${!empty selectedOrganizations.ampOrgId}">
						<tr>
							<td align="left" width=3>
								<html:multibox property="selOrgs" >
									<c:out value="${selIdx.count}"/>
								</html:multibox>
							</td>
							<td align="left" width="367">
								<c:out value="${selectedOrganizations.name}"/>
							</td>
						</tr>
					</c:if>	
				</c:forEach>
			</table>
		</td>
	</tr>
	

	<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:fundingAllocation">Funding Allocation</digi:trn></b>
	</td></tr>

	<tr>
	<td colspan="2">
	<table cellpadding="2" cellspacing="2" width="100%">
	
		<field:display name="Contract Total Value" feature="Contracting">
			<tr>
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:contractTotalValue">Contract Total Value</digi:trn>
				</td>
				<td align="left">
					<c:set var="trnSum">
						<digi:trn key="aim:addNumericValueErrorMessage">
							Please enter the amount
						</digi:trn>
					</c:set>
					<html:text title="${trnSum}" property="contractTotalValue" style="text-align:right" onkeyup="fnChk1(this)" />
				</td>
				<td align="left" colspan="4">
					<digi:trn key="aim:ipa:newPopup:currencyType">Currency Type</digi:trn>
					&nbsp;&nbsp;
					<html:select property="totalAmountCurrency" styleClass="inp-text">
						<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
					</html:select>
				</td>
			</tr>
		</field:display>
	
		<field:display name="Contracting Total Amount" feature="Contracting">
			<tr>
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:totalAmount">Total Amount</digi:trn>
				</td>
				<td align="left">
					<c:set var="trnAutosum">
						<digi:trn key="aim:addNumericValueErrorMessage">
							This amount will auto-calculate!
						</digi:trn>
					</c:set>
					<html:text readonly="true" title="${trnAutosum}" property="totalAmount" style="text-align:right" onkeyup="fnChk(this)" />
				</td>
				<td align="left" colspan="4">
					<digi:trn key="aim:ipa:newPopup:currencyType">Currency Type</digi:trn>
					&nbsp;&nbsp;
					<html:select property="totalAmountCurrency" styleClass="inp-text">
						<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
					</html:select>
				</td>
			</tr>
		</field:display>

		<field:display name="Total EC Contribution" feature="Contracting">
			<tr>
				<td colspan="6" align="center">
					<b><digi:trn key="aim:ipa:newPopup:totalECContribution">Total EC Contribution</digi:trn></b>
				</td>
			</tr>
			<tr>
			<field:display name="Contracting IB" feature="Contracting">
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:ib">IB</digi:trn>
				</td>
				<td align="left">
					<html:text property="totalECContribIBAmount" style="text-align:right" onkeyup="fnChk(this)"/>
				</td>
				<td align="left">
					<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate1" property="totalECContribIBAmountDate"/>
					<a id="fimage1" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate1",-250,-230)'>
						<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
					</a>
				</td>
		    </field:display>
		     
			<field:display name="Contracting INV" feature="Contracting">
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:inv">INV</digi:trn>
				</td>
				<td align="left">
					<html:text property="totalECContribINVAmount" style="text-align:right" onkeyup="fnChk(this)"/>
				</td>
				<td align="left">
					<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate2" property="totalECContribINVAmountDate"/>
					<a href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate2",-250,-230)'>
						<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
					</a>
				</td>
			</field:display>
			</tr>
		</field:display>
		<field:display name="Contracting Total National Contribution" feature="Contracting">
			<tr>
				<td align="center" colspan="6">
					<b><digi:trn key="aim:IPA:newPopup:totalNationalContribution">Total National Contribution</digi:trn></b>
				</td>
			</tr>
			<tr>
				<field:display name="Contracting Central Amount" feature="Contracting">
					<td align="left">
						<digi:trn key="aim:ipa:newPopup:central">Central</digi:trn>
					</td>
					<td align="left">
						<html:text property="totalNationalContribCentralAmount" style="text-align:right" onkeyup="fnChk(this)"/>
					</td>
					<td align="left">
						<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate3" property="totalNationalContribCentralAmountDate"/>
						<a href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate3",-250,-230)'>
							<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
						</a>
					</td>
 				</field:display>

				<field:display name="Contracting IFIs" feature="Contracting">
					<td align="left">
						<digi:trn key="aim:ipa:newPopup:ifis">IFIs</digi:trn>
					</td>
					<td align="left">
						<html:text property="totalNationalContribIFIAmount" style="text-align:right" onkeyup="fnChk(this)"/>
					</td>
					<td align="left">
						<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate4" property="totalNationalContribIFIAmountDate"/>
						<a href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate4",-250,-230)'>
							<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
						</a>
					</td>
				</field:display>
			</tr>
			
			<tr>
				<field:display name="Contracting Regional Amount" feature="Contracting">
					<td align="left">
						<digi:trn key="aim:ipa:newPopup:regional">Regional</digi:trn>
					</td>
					<td align="left">
						<html:text property="totalNationalContribRegionalAmount" style="text-align:right" onkeyup="fnChk(this)"/>
					</td>
					<td align="left">
						<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate5" property="totalNationalContribRegionalAmountDate"/>
						<a href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate5",-250,-230)'>
							<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
						</a>
					</td>
				</field:display>
			</tr>
		</field:display>
		
		<field:display name="Total Private Contribution" feature="Contracting">
			<tr>
				<td align="center" colspan="6">
					<b><digi:trn key="aim:IPA:newPopup:totalPrivateContribution">Total Private Contribution</digi:trn></b>
				</td>
			</tr>
			<tr>
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:ib">IB</digi:trn>
				</td>
				<td align="left">
					<html:text property="totalPrivateContribAmount" style="text-align:right" onkeyup="fnChk(this)"/>
				</td>
				<td align="left">
					<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate6" property="totalPrivateContribAmountDate"/>
					<a href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate6",-250,-230)'>
						<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
					</a>
				</td>
			</tr>
		</field:display>
		
	</table>
	</td>
	</tr>

	
	<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:disbursements">Disbursements</digi:trn></b>
	</td></tr>
	<field:display name="Contract Donor Disbursements" feature="Contracting">
	<logic:notEmpty name="aimIPAContractForm" property="fundingDetailsLinked">
	<tr>
		<td colspan="2">
			<table width="100%" >
			<tr>
				<th><field:display name="Adjustment Type Disbursement" feature="Disbursement"><digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn></field:display></th>
				<th><field:display name="Amount Disbursement" feature="Disbursement"><digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn></field:display></th>
				<th><field:display name="Currency Disbursement" feature="Disbursement"><digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn></field:display></th>
				<th><field:display name="Date Disbursement" feature="Disbursement"><digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn></field:display></th>
				
			</tr>
				<logic:iterate name="aimIPAContractForm" property="fundingDetailsLinked" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
					<logic:equal name="fundingDetail" property="transactionType" value="1">		
																									<tr bgcolor="#ffffff">
																										<td align="center">
																											<field:display name="Adjustment Type Disbursement" feature="Disbursement">
																												<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																													<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																												</digi:trn>
																											</field:display>
																										</td>
																										<td align="center">
																											<field:display name="Amount Disbursement" feature="Disbursement">
																												<FONT color=blue>*</FONT>
																												<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																											</field:display>
																										</td>
																										<td align="center">
																											<field:display name="Currency Disbursement" feature="Disbursement">
																												<bean:write name="fundingDetail" property="currencyCode"/>
																											</field:display>
																										</td>
																										<td align="center">
																											<field:display name="Date Disbursement" feature="Disbursement">
																												<bean:write name="fundingDetail" property="transactionDate"/>
																											</field:display>
																										</td>
																																																	
																									</tr>
																						</logic:equal>
				</logic:iterate>
			</table>
		</td>
	</tr>
	</logic:notEmpty>
	</field:display>
	
	<field:display name="Contracting Disbursements" feature="Contracting">
	<tr>
		<td colspan="2" align="left">
			<field:display name="Contracting Add Disbursement" feature="Contracting">
				<html:button styleClass="dr-menu" property="adddisb" onclick="addDisb()">
					<digi:trn key="aim:IPA:newPopup:addDisbursement">Add Disbursement</digi:trn>
				</html:button>
			</field:display>
			&nbsp;	
			<field:display name="Contracting Remove Disbursements" feature="Contracting">
				<html:button styleClass="dr-menu" property="deldisbursement" onclick="delDisb();">
					<digi:trn key="aim:IPA:newPopup:removeDisbursements">Remove Disbursements</digi:trn>
				</html:button>			
			</field:display>				
		</td>
	</tr>
	
	<field:display name="Contracting Disbursements Global Currency" feature="Contracting">
	<tr>
		<td colspan="2">
		<table width="100%">
			<tr>
				<td align="left" width="30%">
					<digi:trn key="aim:ipa:newPopup:dibusrsementsGlobalCurrency">Disbursements Global Currency</digi:trn>
				</td>
				<td align="left">
					<html:select property="dibusrsementsGlobalCurrency" styleClass="inp-text">
						<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
					</html:select>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	</field:display>
	
	
	<logic:notEmpty name="aimIPAContractForm" property="contractDisbursements">
		<tr>
			<td colspan="2">
				<table id="ContractDisbursmentsList">
					<c:forEach  items="${aimIPAContractForm.contractDisbursements}" var="contractDisbursement"  varStatus="idx" >
						<tr>
						<td align="left" colspan="2">
							<html:hidden property="${contractDisbursement}" value="${id}"/>
							&nbsp;
							<html:multibox property="selContractDisbursements" value="${idx.count}"/>
							<html:select indexed="true" name="contractDisbursement" property="adjustmentType">
								<html:option value="0"><digi:trn key="aim:ipa:popup:actual"> Actual</digi:trn></html:option>
								<html:option value="1"><digi:trn key="aim:ipa:popup:planned">Planned</digi:trn></html:option>							
							</html:select>
							&nbsp;
							<html:text indexed="true" name="contractDisbursement" property="amount" onkeyup="fnChk(this)"><digi:trn key="aim:ipa:popup:amount">Amount</digi:trn></html:text>
							&nbsp;
							<html:select name="contractDisbursement" indexed="true" property="currCode" styleClass="inp-text">
								<html:optionsCollection name="aimIPAContractForm" property="currencies" value="currencyCode" label="currencyName"/>
							</html:select>
							&nbsp;
							<html:text readonly="true" size="9" indexed="true" name="contractDisbursement" property="disbDate" styleClass="inp-text" styleId="date${idx.count}"/>
							<a id="image${idx.count}" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","date${idx.count}",-250,-230)'>
								<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
							</a>
						</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</logic:notEmpty>						
	</field:display>
	
	<tr>
		<td colspan="2" align="center">
			<field:display name="Contracting Save Button" feature="Contracting">
				<html:button property="submit" styleClass="dr-menu" onclick="validate()"><digi:trn key="aim:save">Save</digi:trn></html:button>
			</field:display>
			&nbsp;&nbsp;
			<field:display name="Contracting Cancel Saving" feature="Contracting">
				<html:button styleClass="dr-menu" property="cancel" onclick="hideAddContract()">
					<digi:trn key="aim:addEditActivityCancel">Cancel</digi:trn>
				</html:button>
			</field:display>
		</td>
	</tr>	
</table>
</digi:form>