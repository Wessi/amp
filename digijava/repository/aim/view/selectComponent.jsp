<%@ page pageEncoding="UTF-8" %>

<%@ page import="org.digijava.module.aim.form.EditActivityForm, java.util.*, org.digijava.module.aim.dbentity.*" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>

<% EditActivityForm eaForm = (EditActivityForm) session.getAttribute("siteampdefaultaimEditActivityForm"); 
  	String defPers = (String) request.getAttribute("defPerspective"); 
  	String defCurr = (String) request.getAttribute("defCurrency"); 
 int indexC = 0;
 int indexD = 0; 
 int indexE = 0; %>




<digi:instance property="aimEditActivityForm" />

<%--
<digi:form action="/componentSelected.do" method="post">

--%>
<digi:form action="/showAddComponent.do" method="post">
<html:hidden property="componentReset" value="false"/>
<html:hidden property="componentId"/>


<input type="hidden" name="selectedDate">

<table width="100%" cellSpacing=0 cellPadding=0 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
							<a title="<digi:trn key="aim:ComponentofProject">A smaller sub project of a donor approved project</digi:trn>">								
							<digi:trn key="aim:addComponent">Add Component</digi:trn></a>
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td>
											<FONT color=red>*</FONT>
											<a title="<digi:trn key="aim:TitleforComponent">Title of the project component specified</digi:trn>">					
											<digi:trn key="aim:title">Title</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:TitleforComponent">Title of the project component specified</digi:trn>">
											<html:text property="componentTitle" styleClass="inp-text" tabindex="1"/>
											</a>
										</td>
									</tr>								
									<tr>
										<td>
										<a title="<digi:trn key="aim:ComponentDescribe">Descriptive text as to the component objectives and tasks</digi:trn>">							
										<digi:trn key="aim:description">Description</digi:trn>
										</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:ComponentDescribe">Descriptive text as to the component objectives and tasks</digi:trn>">
											<html:textarea property="componentDesc" cols="50" rows="4" styleClass="inp-text" tabindex="2"/>
											</a>
										</td>
									</tr>			
									<tr><td colspan=2>
										<FONT color=blue><BIG>*</BIG>
											<digi:trn key="aim:pleaseEnterTheAmountInThousands">
												Please enter amount in thousands (000)
		  									</digi:trn>
										</FONT>
									</td></tr>
								<tr bgcolor="#f4f4f2">
								<td colspan="2" class="box-border-alt1">
										<span class="f-names">Commitments - (Total Actual Allocation <%=eaForm.getTotalCommitments()%>
															 <%=eaForm.getCurrCode()%> )
										</span>

										<a href="javascript:addCommitments()">Add</a><br><br>
												Planned/Actual&nbsp;&nbsp;&nbsp;
												Amount&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												Currency&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												Date&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												Perspective<br>
										<div id="comm">

										<c:if test="${aimEditActivityForm.componentId != -1}">
											<c:forEach var="fundComp" items="${aimEditActivityForm.selectedComponents}">

											<c:if test="${aimEditActivityForm.componentId == fundComp.componentId}">

											<c:forEach var="comm" items="${fundComp.commitments}">

											
											<% String tNameBase = "comm_" + indexC + "_"; 
												String divName = "comm_" + indexC;
												indexC++;
												%>
												<% String field1 = tNameBase + "1"; 
												 String field2 = tNameBase + "2"; 
												 String field3 = tNameBase + "3"; 
												 String field4 = tNameBase + "4";
												 String field5 = tNameBase + "5"; %>
												 <div id="<%=divName%>">
												<select name="<%=field1%>" class="inp-text">
													<c:if test="${comm.adjustmentType == 1}">
														<option value="1" selected="true">Actual</option>
														<option value="0">Planned</option>
													</c:if>
													<c:if test="${comm.adjustmentType == 0}">
														<option value="1">Actual</option>
														<option value="0" selected="true">Planned</option>
													</c:if>					
												</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=field2%>" 
												value="<c:out value="${comm.transactionAmount}"/>" size="15"  class='amt'>&nbsp;<select name="<%=field3%>" class="inp-text">
													<c:forEach var="currency" items="${aimEditActivityForm.currencies}">
														<c:if test="${comm.currencyCode == currency.currencyCode}">
															<option selected="true" value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
														<c:if test="${comm.currencyCode != currency.currencyCode}">
															<option value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
															<c:out value="${currency.currencyName}"/>
														</option>
													</c:forEach>
												</select>&nbsp;<input type="text" readonly="true" name="<%=field4%>" id="<%=field4%>"
												value="<c:out value="${comm.transactionDate}"/>" size="10"  class="inp-text">&nbsp;<a href=javascript:calendar('<%=field4%>')><img src='../ampTemplate/images/show-calendar.gif' border='0'></a>&nbsp;<select name="<%=field5%>" value="<c:out value="${comm.perspectiveCode}"/>" class="inp-text">
													<c:forEach var="pers" items="${aimEditActivityForm.perspectives}">
														<c:if test="${comm.perspectiveCode == pers.code}">
															<option selected="true" value="<c:out value="${pers.code}"/>">
														</c:if>
														<c:if test="${comm.perspectiveCode != pers.code}">
															<option value="<c:out value="${pers.code}"/>">
														</c:if>
														<c:out value="${pers.name}"/>
														</option>	
													</c:forEach>
												</select>&nbsp;<input type='button' value='Delete' class='inp-text' 
												onclick="removeCommitment('<%=divName%>')"></div>
											</c:forEach>	
											</c:if>
											</c:forEach>
										</c:if>
										</div>
								</td>
							</tr>
							<tr bgcolor="#ffffff">
								<td colspan="2" class="box-border">
										<span class="f-names">Disbursement - (Total actual to date <%=eaForm.getTotalDisbursements()%>
															 <%=eaForm.getCurrCode()%>)
										</span>
										<a href="javascript:addDisbursement()">Add</a><br><br>
												Planned/Actual&nbsp;&nbsp;&nbsp;
												Amount&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												Currency&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												Date&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												Perspective<br>
									<div id="disb">
										<c:if test="${aimEditActivityForm.componentId != -1}">
											<c:forEach var="fundComp" items="${aimEditActivityForm.selectedComponents}">
											<c:if test="${aimEditActivityForm.componentId == fundComp.componentId}">
											<c:forEach var="comm" items="${fundComp.disbursements}">
											<% String tNameBase = "disb_" + indexD + "_"; 
												String divName = "disb_" + indexD;
												indexD++;
												%>
												<% String field1 = tNameBase + "1"; 
												 String field2 = tNameBase + "2"; 
												 String field3 = tNameBase + "3"; 
												 String field4 = tNameBase + "4";
												 String field5 = tNameBase + "5"; %>
												 <div id="<%=divName%>">
												<select name="<%=field1%>" class="inp-text">
													<c:if test="${comm.adjustmentType == 1}">
														<option value="1" selected="true">Actual</option>
														<option value="0">Planned</option>
													</c:if>
													<c:if test="${comm.adjustmentType == 0}">
														<option value="1">Actual</option>
														<option value="0" selected="true">Planned</option>
													</c:if>					
												</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=field2%>" 
												value="<c:out value="${comm.transactionAmount}"/>" size="15"  class='amt'>&nbsp;<select name="<%=field3%>" class="inp-text">
													<c:forEach var="currency" items="${aimEditActivityForm.currencies}">
														<c:if test="${comm.currencyCode == currency.currencyCode}">
															<option selected="true" value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
														<c:if test="${comm.currencyCode != currency.currencyCode}">
															<option value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
															<c:out value="${currency.currencyName}"/>
														</option>
													</c:forEach>
												</select>&nbsp;<input type="text" name="<%=field4%>" id="<%=field4%>" readonly="true" 
												value="<c:out value="${comm.transactionDate}"/>" size="10"  class="inp-text">&nbsp;<a href=javascript:calendar("<%=field4%>")><img src='../ampTemplate/images/show-calendar.gif' border='0'></a>&nbsp;<select name="<%=field5%>" 
												class="inp-text">
													<c:forEach var="pers" items="${aimEditActivityForm.perspectives}">
														<c:if test="${comm.perspectiveCode == pers.code}">
															<option selected="true" value="<c:out value="${pers.code}"/>">
														</c:if>
														<c:if test="${comm.perspectiveCode != pers.code}">
															<option value="<c:out value="${pers.code}"/>">
														</c:if>
														<c:out value="${pers.name}"/>
														</option>	
													</c:forEach>				
												</select>&nbsp;<input type='button' value='Delete' class='inp-text' 
												onclick="removeDisbursement('<%=divName%>')"></div>
											</c:forEach>	
											</c:if>
											</c:forEach>
										</c:if>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="2" class="box-border-alt1">
											<span class="f-names">Expenditure - (Total actual to date <%=eaForm.getTotalExpenditures()%>
																 <%=eaForm.getCurrCode()%>)</span>
											<a href="javascript:addExpenditure()">Add</a>&nbsp;&nbsp;
											<br><br>
												Planned/Actual&nbsp;&nbsp;&nbsp;
												Amount&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												Currency&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												Date&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												Perspective<br>
									<div id="expn">
										<c:if test="${aimEditActivityForm.componentId != -1}">
											<c:forEach var="fundComp" items="${aimEditActivityForm.selectedComponents}">

											<c:if test="${aimEditActivityForm.componentId == fundComp.componentId}">

											<c:forEach var="comm" items="${fundComp.expenditures}">

											
											<% String tNameBase = "expn_" + indexE + "_"; 
												String divName = "expn_" + indexE;
												indexE++;
												%>
												<% String field1 = tNameBase + "1"; 
												 String field2 = tNameBase + "2"; 
												 String field3 = tNameBase + "3"; 
												 String field4 = tNameBase + "4";
												 String field5 = tNameBase + "5"; %>
												 <div id="<%=divName%>">
												<select name="<%=field1%>" class="inp-text">
													<c:if test="${comm.adjustmentType == 1}">
														<option value="1" selected="true">Actual</option>
														<option value="0">Planned</option>
													</c:if>
													<c:if test="${comm.adjustmentType == 0}">
														<option value="1">Actual</option>
														<option value="0" selected="true">Planned</option>
													</c:if>					
												</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=field2%>" 
												value="<c:out value="${comm.transactionAmount}"/>" size="15"  class='amt'>&nbsp;<select name="<%=field3%>" class="inp-text">
													<c:forEach var="currency" items="${aimEditActivityForm.currencies}">
														<c:if test="${comm.currencyCode == currency.currencyCode}">
															<option selected="true" value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
														<c:if test="${comm.currencyCode != currency.currencyCode}">
															<option value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
															<c:out value="${currency.currencyName}"/>
														</option>
													</c:forEach>
												</select>&nbsp;<input type="text" name="<%=field4%>" id="<%=field4%>" readonly="true" 
												value="<c:out value="${comm.transactionDate}"/>" size="10"  class="inp-text">&nbsp;<a href=javascript:calendar("<%=field4%>")><img src='../ampTemplate/images/show-calendar.gif' border='0'></a>&nbsp;<select name="<%=field5%>" value="<c:out value="${comm.perspectiveCode}"/>" class="inp-text">
													<c:forEach var="pers" items="${aimEditActivityForm.perspectives}">
														<c:if test="${comm.perspectiveCode == pers.code}">
															<option selected="true" value="<c:out value="${pers.code}"/>">
														</c:if>
														<c:if test="${comm.perspectiveCode != pers.code}">
															<option value="<c:out value="${pers.code}"/>">
														</c:if>
														<c:out value="${pers.name}"/>
														</option>	
													</c:forEach>
												</select>&nbsp;<input type='button' value='Delete' class='inp-text' 
												onclick="removeExpenditure('<%=divName%>')"></div>
											</c:forEach>	
											</c:if>
											</c:forEach>
										</c:if>									
									</div>
								</td>									
							</tr>
							<tr>
								<td colspan="2" align="center">
									<table cellPadding=3>
										<tr>
											<td>
												<input type="button" value="Save" class="inp-text" onclick="return addComponents()">
											</td>
											<td>
												<input type="reset" value="Reset" class="inp-text">
											</td>
											<td>
												<input type="button" value="Close" class="inp-text" onclick="closeWindow()">
											</td>
										</tr>
									</table>
								</td>									
							</tr>
							</td>
						</tr>					
					</table>
				</td>
			</tr>
		</table>
	</td></tr>
</table>

<script language="JavaScript">
<!--

var numComm = <%=indexC%>;
var numExpn = <%=indexE%>;
var numDisb = <%=indexD%>;

var tempComm = numComm;
var tempDisb = numDisb;
var tempExpn = numExpn;

function addCommitments() 
{
	var ni = document.getElementById('comm');
	var divname = "comm_" + numComm;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<select name='comm_" + numComm + "_1' class='inp-text'>";
	s += "<option value='1'>Actual</option>";
	s += "<option value='0'>Planned</option>";
	s += "</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	s += "<input type='text' name='comm_" + numComm + "_2' size='15' class='amt'>&nbsp;";
	s += "<select name='comm_" + numComm + "_3' class='inp-text'>&nbsp;";

	<% Collection col = eaForm.getCurrencies();
		Iterator itr = col.iterator();
		while (itr.hasNext()) 
		{
			AmpCurrency curr = (AmpCurrency) itr.next();	
			if (curr.getCurrencyCode().equalsIgnoreCase(defCurr)) { %>
				s += "<option value='<%=curr.getCurrencyCode()%>' selected='true'><%=curr.getCurrencyName()%></option>";
			<% } else { %>
				s += "<option value='<%=curr.getCurrencyCode()%>'><%=curr.getCurrencyName()%></option>";				  			
			<% }
		 }%>
	
	s += "</select>&nbsp;";
	s += "<input type='text' name='comm_" + numComm + "_4' id='comm_" + numComm + "_4' size='10' class='inp-text'>&nbsp;";
	s += "<a href=javascript:calendar('comm_" + numComm + "_4')><img src='../ampTemplate/images/show-calendar.gif' border='0'></a>&nbsp;";
	s += "<select name='comm_" + numComm + "_5' class='inp-text'>";
	<% itr = eaForm.getPerspectives().iterator();
		while (itr.hasNext()) { 
			AmpPerspective pers = (AmpPerspective) itr.next();
			if (pers.getCode().equalsIgnoreCase(defPers)) { %>
				s += "<option value='<%=pers.getCode()%>' selected='true'><%=pers.getName()%></option>";
			<% } else { %>
				s += "<option value='<%=pers.getCode()%>'><%=pers.getName()%></option>";
			<% } 
		} %>	
	s += "</select>&nbsp;";
	s += "<input type='button' value='Delete' class='inp-text' onclick=removeCommitment('" + divname + "')><br>";
	
	newdiv.innerHTML = s;
	ni.appendChild(newdiv);
	numComm++;
	tempComm++;
}

function removeCommitment(divname)
{
	var d = document.getElementById('comm');
	var olddiv = document.getElementById(divname);
	d.removeChild(olddiv);
	tempComm--;
}

function addDisbursement() 
{
	var ni = document.getElementById('disb');
	var divname = "disb_" + numDisb;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<select name='disb_" + numDisb + "_1' class='inp-text'>";
	s += "<option value='1'>Actual</option>";
	s += "<option value='0'>Planned</option>";
	s += "</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	s += "<input type='text' name='disb_" + numDisb + "_2' size='15'  class='amt'>&nbsp;";
	s += "<select name='disb_" + numDisb + "_3' class='inp-text'>&nbsp;";
	
	<% col = eaForm.getCurrencies();
		itr = col.iterator();
		while (itr.hasNext()) {
			AmpCurrency curr = (AmpCurrency) itr.next();	
			if (curr.getCurrencyCode().equalsIgnoreCase(defCurr)) { %>
				s += "<option value='<%=curr.getCurrencyCode()%>' selected='true'><%=curr.getCurrencyName()%></option>";
			<% } else { %>
				s += "<option value='<%=curr.getCurrencyCode()%>'><%=curr.getCurrencyName()%></option>";				  			
			<% }
		 }%>
	
	s += "</select>&nbsp;";
	s += "<input type='text' name='disb_" + numDisb + "_4' id='disb_" + numDisb + "_4' size='10' class='inp-text'>&nbsp;";
	s += "<a href=javascript:calendar('disb_" + numDisb + "_4')><img src='../ampTemplate/images/show-calendar.gif' border='0'></a>&nbsp;";
	s += "<select name='disb_" + numDisb + "_5' class='inp-text'>";
	<% itr = eaForm.getPerspectives().iterator();
		while (itr.hasNext()) { 
			AmpPerspective pers = (AmpPerspective) itr.next();
			if (pers.getCode().equalsIgnoreCase(defPers)) { %>
				s += "<option value='<%=pers.getCode()%>' selected='true'><%=pers.getName()%></option>";
			<% } else { %>
				s += "<option value='<%=pers.getCode()%>'><%=pers.getName()%></option>";
			<% } 
		} %>
	s += "</select>&nbsp;";
	s += "<input type='button' value='Delete' class='inp-text' onclick=removeDisbursement('" + divname + "')><br>";
	
	newdiv.innerHTML = s;
	ni.appendChild(newdiv);
	numDisb++;
	tempDisb++;
}

function removeDisbursement(divname)
{
	var d = document.getElementById('disb');
	var olddiv = document.getElementById(divname);
	d.removeChild(olddiv);
	tempDisb--;
}

function addExpenditure() 
{
	var ni = document.getElementById('expn');
	var divname = "expn_" + numExpn;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<select name='expn_" + numExpn + "_1' class='inp-text'>";
	s += "<option value='1'>Actual</option>";
	s += "<option value='0'>Planned</option>";
	s += "</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	s += "<input type='text' name='expn_" + numExpn + "_2' size='15' class='amt'>&nbsp;";
	s += "<select name='expn_" + numExpn + "_3' class='inp-text'>&nbsp;";
	
	<% col = eaForm.getCurrencies();
		itr = col.iterator();
		while (itr.hasNext()) {
			AmpCurrency curr = (AmpCurrency) itr.next();	
			if (curr.getCurrencyCode().equalsIgnoreCase(defCurr)) { %>
				s += "<option value='<%=curr.getCurrencyCode()%>' selected='true'><%=curr.getCurrencyName()%></option>";
			<% } else { %>
				s += "<option value='<%=curr.getCurrencyCode()%>'><%=curr.getCurrencyName()%></option>";				  			
			<% }
		 }%>
	
	s += "</select>&nbsp;";
	s += "<input type='text' name='expn_" + numExpn + "_4' id='expn_" + numExpn + "_4' size='10' class='inp-text'>&nbsp;";
	s += "<a href=javascript:calendar('expn_" + numExpn + "_4')><img src='../ampTemplate/images/show-calendar.gif' border='0'></a>&nbsp;";
	s += "<select name='expn_" + numExpn + "_5' class='inp-text'>";
	<% itr = eaForm.getPerspectives().iterator();
		while (itr.hasNext()) { 
			AmpPerspective pers = (AmpPerspective) itr.next();
			if (pers.getCode().equalsIgnoreCase(defPers)) { %>
				s += "<option value='<%=pers.getCode()%>' selected='true'><%=pers.getName()%></option>";
			<% } else { %>
				s += "<option value='<%=pers.getCode()%>'><%=pers.getName()%></option>";
			<% } 
		} %>
	s += "</select>&nbsp;";
	s += "<input type='button' value='Delete' class='inp-text' onclick=removeExpenditure('" + divname + "')><br>";
	
	newdiv.innerHTML = s;
	ni.appendChild(newdiv);
	numExpn++;
	tempExpn++;
}

function removeExpenditure(divname)
{
	var d = document.getElementById('expn');
	var olddiv = document.getElementById(divname);
	d.removeChild(olddiv);
	tempExpn--;
}



function addComponents() 
{
	var flag = validate();
	if (flag == true) 
	{
			  
/*		document.aimEditActivityForm.target = window.opener.name;
		document.aimEditActivityForm.compFundAct.value = "update";
	   document.aimEditActivityForm.submit();
		window.close();			  
*/	

	<digi:context name="addComp" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=update"/>
	document.aimEditActivityForm.action = "<%= addComp %>";
	document.aimEditActivityForm.target = window.opener.name;
	document.aimEditActivityForm.submit();
	window.close();			  
	}
	return flag;
}

function validate() 
{

	var titleFlag = isEmpty(document.aimEditActivityForm.componentTitle.value);
	if(titleFlag == true) {
		alert("Please enter title");			
		document.aimEditActivityForm.componentTitle.focus();
		return false;
	}

	var x = document.aimEditActivityForm;

	/*
	if (tempComm == 0) {
		alert ("Commitment not entered.");
		return false;			  
	}
	*/

	if (tempExpn > 0 && tempDisb == 0) {
		alert ("Expenditure entered without entering disbursements.");
		return false;			  			  
	}
	
	for (index = 0;index < x.elements.length;index++) {
		var str = x.elements[index].name;
		if (str.match("comm_[0-9]*_2")) {
			// validate amount
			if (trim(x.elements[index].value) == "") {
				alert ("Amount not entered.");
				x.elements[index].focus();
				return false;
			}
			if (checkAmount(x.elements[index].value) == false) {
				alert ("Invalid amount entered.");
				x.elements[index].focus();
				return false;
			}
			
		} else if (str.match("comm_[0-9]*_4")) { 
			// validate date	  
			if (trim(x.elements[index].value) == "") {
				alert ("Date not entered.");
				x.elements[index].focus();
				return false;
			}
		} else if (str.match("disb_[0-9]*_2")) { 
			// validate amount
			if (trim(x.elements[index].value) == "") {
				alert ("Amount not entered.");
				x.elements[index].focus();
				return false;				  
			}
			if (checkAmount(x.elements[index].value) == false) {
				alert ("Invalid amount entered.");
				x.elements[index].focus();
				return false;
			}			
		} else if (str.match("disb_[0-9]*_4")) { 
			if (trim(x.elements[index].value) == "") {
				alert ("Date not entered.");
				x.elements[index].focus();
				return false;
			}				  
		} else if (str.match("expn_[0-9]*_2")) { 
			// validate amount
			if (trim(x.elements[index].value) == "") {
				alert ("Amount not entered.");
				x.elements[index].focus();
				return false;
			}
			if (checkAmount(x.elements[index].value) == false) {
				alert ("Invalid amount entered.");
				x.elements[index].focus();
				return false;
			}			
		} else if (str.match("expn_[0-9]*_4")) { 
			if (trim(x.elements[index].value) == "") {
				alert ("Date not entered.");
				x.elements[index].focus();
				return false;
			}				  
		}
				  
	}
	return true;	
}



/*
function validate() {
		var titleFlag = isEmpty(document.aimEditActivityForm.componentTitle.value);
		if(titleFlag == true) {
			alert("Please enter title");			
			document.aimEditActivityForm.componentTitle.focus();
			return false;
		}
		var amtFlag = isEmpty(document.aimEditActivityForm.componentAmount.value);
		if (document.aimEditActivityForm.componentAmount.value != "0.00") {
			if(amtFlag == false) {
				amtFlag = checkAmount(document.aimEditActivityForm.componentAmount.value);
				if(amtFlag != true) {
					alert("Please enter valid amount");	
					document.aimEditActivityForm.componentAmount.focus();
					return false;
				} else {
					if (document.aimEditActivityForm.currencyCode.value == "-1") {
						alert("Please select a currency");	
						document.aimEditActivityForm.currencyCode.focus();
						return false;
					}
				}
			}				  
		}

		return true;
	}

	function addComponents() {
		var flag = validate();
		if (flag == true) {
			document.aimEditActivityForm.target = window.opener.name;
			document.aimEditActivityForm.submit();
			window.close();
			return flag;
		} else {
			return false;
		}
	}	
*/


	function load() {
		document.aimEditActivityForm.componentTitle.focus();
	}

	function unload() {
	}
	function closeWindow() {
		window.close();
	}
	function trim(s) 
	{
		return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
   }

-->
</script>

</digi:form>
