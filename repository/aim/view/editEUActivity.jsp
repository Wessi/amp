<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>

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

function selectOrganisation() {
		openNewWindow(650, 420);
		<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true&edit=true&step=3" />
		document.aimEditActivityForm.action = "<%= selectOrganization %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
}
</script>

<!-- code for rendering that nice calendar -->
<bean:define id="langBean" name="org.digijava.kernel.navigation_language" scope="request" type="org.digijava.kernel.entity.Locale" toScope="page" />
<bean:define id="lang" name="langBean" property="code" scope="page" toScope="page" />

<script type="text/javascript">
	var myCalendarModel = new DHTMLSuite.calendarModel();
	
	myCalendarModel.setLanguageCode('<bean:write name="lang" />'); 
	calendarObjForForm = new DHTMLSuite.calendar({callbackFunctionOnDayClick:'getDateFromCalendar',isDragable:false,displayTimeBar:false,calendarModelReference:myCalendarModel}); 
		
	function getDateFromCalendar(inputArray)
	{
		var references = calendarObjForForm.getHtmlElementReferences(); // Get back reference to form field.
		references.dueDate.value = inputArray.year + '-' + inputArray.month + '-' + inputArray.day;
		calendarObjForForm.hide();			
	}	

	function pickDate(buttonObj,inputObject)
	{
		calendarObjForForm.setCalendarPositionByHTMLElement(inputObject,0,inputObject.offsetHeight-80);	// Position the calendar right below the form input
		calendarObjForForm.setInitialDateFromInput(inputObject,'yyyy-mm-dd');	// Specify that the calendar should set it's initial date from the value of the input field.
		calendarObjForForm.addHtmlElementReference('dueDate',inputObject);	// Adding a reference to this element so that I can pick it up in the getDateFromCalendar below(myInput is a unique key)
		if(calendarObjForForm.isVisible()){
			calendarObjForForm.hide();
		}else{
			calendarObjForForm.resetViewDisplayedMonth();	// This line resets the view back to the inital display, i.e. it displays the inital month and not the month it displayed the last time it was open.
			calendarObjForForm.display();
		}		
	}
</script>		

<body onload="load()">
<digi:instance property="aimEUActivityForm" />
<digi:form action="/editEUActivity.do" method="post">

<input type="hidden" name="edit" value="true">
<html:hidden property="id"/>

<digi:errors/>

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
<tr><td colspan="4" bgcolor="#006699" class="textalb" align="center">
<digi:trn key="aim:addEditActivity">Add/Edit Activity</digi:trn>
</td></tr>
	<tr>
		<td align="right">
		<b><digi:trn key="aim:addEditActivityName">Activity name:</digi:trn></b>
		</td>
		<td>
		         	<html:text property="name" size="30"/> 
		&nbsp;&nbsp;<b><digi:trn key="aim:addEditActivityID">Activity ID:</digi:trn></b> <html:text property="textId" size="10"/> 
		</td>
	</tr>

	<tr>
		<td align="right">
		<b><digi:trn key="aim:addEditActivityInputs">Inputs:</digi:trn></b>
		</td>
		<td>
			<html:textarea property="inputs" rows="3" cols="90" styleClass="inp-text"/>
		</td>
	</tr>
	
		<tr>
		<td align="right">
		<b><digi:trn key="aim:addEditActivityTotalCost">Total Cost:</digi:trn></b>
		</td>
		<td>
			<html:text property="totalCost" style="text-align:right"/>
			<html:select property="totalCostCurrencyId" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimEUActivityForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
	</tr>


	<tr>
		<td>&nbsp;</td>
		<td bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:addEditActivityContributions">Contributions</digi:trn></b>
		</td>
	</tr>
	<tr>
		<td>&nbsp;
		</td>
		<td>
		<table>
	
						<logic:iterate name="aimEUActivityForm" property="contrCurrId" indexId="idx"
							id="currContr" scope="page">
							<tr>
							<td align="right" valign="top">
							<input type="checkbox" name="deleteContrib" value='<bean:write name="idx"/>'>
							<input style="text-align:right" type='text' name='contrAmount' onclick="clearDefault(this)"
									value='${aimEUActivityForm.contrAmount[idx]}'>
							<select name="contrCurrId" style="width: 100px" class="inp-text">
								<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
							<logic:iterate name="aimEUActivityForm" property="currencies" id="currency" indexId="cIdx" type="org.digijava.module.aim.dbentity.AmpCurrency">
								<option value='<bean:write name="currency" property="ampCurrencyId"/>' 
								<c:if test="${ aimEUActivityForm.contrCurrId[idx] == currency.ampCurrencyId }">selected</c:if>>
								<bean:write name="currency" property="currencyName"/>
								</option>
							</logic:iterate>
							</select>
						
							<select name="contrFinInstrId" class="inp-text" style="width: 100px">
								<option value="-1"><digi:trn key="aim:addEditActivitySelectTypeOfAssistance">Select Type of Assistance</digi:trn></option>
							<logic:iterate name="aimEUActivityForm" property="finInstrs" id="modality" indexId="cIdx" type="org.digijava.module.aim.dbentity.AmpCategoryValue">
								<option value='<bean:write name="modality" property="id"/>'
								<c:if test="${ aimEUActivityForm.contrFinInstrId[idx] == modality.id }">selected</c:if>>
								<bean:write name="modality" property="value"/>
								</option>
							</logic:iterate>
							</select>

							<select name="contrDonorId" style="width: 100px" class="inp-text"> 
								<option value="-1"><digi:trn key="aim:addEditActivityDonor">Donor</digi:trn></option>
							<logic:iterate name="aimEUActivityForm" property="donors" id="donor" indexId="cIdx" type="org.digijava.module.aim.dbentity.AmpOrganisation">
								<option value='<bean:write name="donor" property="ampOrgId"/>'
								<c:if test="${ aimEUActivityForm.contrDonorId[idx] == donor.ampOrgId }">selected</c:if>>
								<bean:write name="donor" property="name"/>
								</option>
							</logic:iterate>
							</select>
							
							<input style="text-align:left" type='text' name='contrAmount' disabled="disabled"
									value='${aimEUActivityForm.contrDonorId[idx]}'>
								<!-- <digi:trn key="btn:selOrganizations">Sel Org</digi:trn> -->
 						    <html:button  styleClass="buton" property="submitButton" onclick="selectOrganisation()">
								Sel Org
							</html:button>
							
							<logic:present name="aimEUActivityForm" property="finTypes">
							<select name="contrFinTypeId" style="width: 100px" class="inp-text">
								<option value="-1"><digi:trn key="aim:addEditActivitySelectFinancingInstrument">Select Financing Instrument</digi:trn></option>
							<logic:iterate name="aimEUActivityForm" property="finTypes" id="finType" indexId="cIdx" type="org.digijava.module.aim.dbentity.AmpTermsAssist">
								<option value='<bean:write name="finType" property="ampTermsAssistId"/>'
								<c:if test="${ aimEUActivityForm.contrFinTypeId[idx] == finType.ampTermsAssistId }">selected</c:if>>
								<bean:write name="finType" property="termsAssistName"/>
								</option>
							</logic:iterate>
							</select>
							</logic:present>
						</td>

							</tr>
						</logic:iterate>			
		</table>
		<tr><td colspan="2" align="center">
				<html:submit styleClass="buton" property="addFields"><digi:trn key="aim:addEditActivityAddContribution">Add Contribution</digi:trn></html:submit>&nbsp;&nbsp;
				<html:submit styleClass="buton" property="removeFields"><digi:trn key="aim:addEditActivityDeleteSelected">Delete Selected</digi:trn></html:submit>				
		</td></tr>
	
	
	
	<tr>
		<td align="right">
		<b><digi:trn key="aim:addEditActivityAssumptions">Assumptions:</digi:trn></b>
		</td>
		<td>
			<html:textarea property="assumptions" rows="3" cols="90" styleClass="inp-text"/>
		</td>
	</tr>

	<tr>
		<td align="right">
		<b><digi:trn key="aim:addEditActivityProgress">Progress:</digi:trn></b>
		</td>
		<td>
			<html:textarea property="progress" rows="3" cols="90" styleClass="inp-text"/>
		</td>
	</tr>	

	<tr>
		<td align="right">
		<b><digi:trn key="aim:addEditActivityDueDate">Due Date:</digi:trn></b>
		</td>
		<td>
			<html:text readonly="true" property="dueDate" styleClass="inp-text"/>
			<input type="button" class="buton" value='<digi:trn key="aim:addEditActivityPickDate">Pick date</digi:trn>' onclick="pickDate(this,document.aimEUActivityForm.dueDate);"></td>
		</td>
	</tr>	


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