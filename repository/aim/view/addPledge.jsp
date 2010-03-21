<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<jsp:include page="addSectors.jsp" flush="true" />

<script language="JavaScript" type="text/javascript"><!--
function fnChk(frmContrl, f){
	  <c:set var="errMsgAddSectorNumericValue">
	  <digi:trn key="aim:addSecorNumericValueErrorMessage">
	  Please enter numeric value only
	  </digi:trn>
	  </c:set>
	  
	  if (isNaN(frmContrl.value)) {
	    alert("${errMsgAddSectorNumericValue}");
	    frmContrl.value = "";
	    //frmContrl.focus();
	    return false;
	  }  
	  if (frmContrl.value > 100) {    
	      if (f == "sector") {
		     <c:set var="errMsgAddSumExceed">
			  <digi:trn key="aim:addSecorSumExceedErrorMessage">
			  Sector percentage can not exceed 100
			  </digi:trn>
			  </c:set>
			  alert("${errMsgAddSumExceed}");
		  } else if (f == "program") {
		     <c:set var="errMsgAddSumExceed">
			  <digi:trn key="aim:addProgramSumExceedErrorMessage">
			  Program percentage can not exceed 100
			  </digi:trn>
			  </c:set>  
			  alert("${errMsgAddSumExceed}");
		  } else if (f == "region") {
		      <c:set var="errMsgAddSumExceed">
			  <digi:trn key="aim:addRegionSumExceedErrorMessage">
			  Region percentage can not exceed 100
			  </digi:trn>
			  </c:set> 
			  alert("${errMsgAddSumExceed}");
		  }
	    frmContrl.value = "";
	    return false;
	  }
	  return true;
	}

function addLocation() {
	  openNewWindow(600, 500);
	  <digi:context name="selectLoc" property="context/module/moduleinstance/selectPledgeLocation.do?edit=false" />
	  document.pledgeForm.action = "<%= selectLoc %>";
	  document.pledgeForm.target = popupPointer.name;
	  document.pledgeForm.submit();
	}

function removeLocation() {
	//document.getElementsByName("fundingEvent")[0].value = "delFunding";
	var i = 1
	var delStr = "deleteLocs="
	while (document.getElementById("checkLoc"+i)!=null){
		if(document.getElementById("checkLoc"+i).checked==true){
			delStr = delStr + "_" + i;
		}
		i++;
	}
	if (delStr.length < 13){
		alert ("Please, select a location first.");
	} else {
		document.pledgeForm.target = "_self";
		document.pledgeForm.action="/removePledgeLocation.do?"+delStr;
		document.pledgeForm.submit();
	}		
}

function addSectors(editAct,configId) {
/*  openNewWindow(600, 450);
  document.aimEditActivityForm.action = "/selectSectors.do?edit=true&configId="+configId;
  document.aimEditActivityForm.target = popupPointer.name;
  document.aimEditActivityForm.submit();
*/ 	
	initSectorScript();
	 myAddSectors("edit=true&configId=1");	  
}

function addSector(param)
{
    
    <digi:context name="addSec" property="context/addPledge.do?addSector=true&edit=param" />
    document.pledgeForm.action = "<%= addSec %>";
    document.pledgeForm.target = "_self";
    document.pledgeForm.submit();
}

function removeSector() {
	//document.getElementsByName("fundingEvent")[0].value = "delFunding";
	var i = 1
	var delStr = "deleteSect="
	while (document.getElementById("checkSect"+i)!=null){
		if(document.getElementById("checkSect"+i).checked==true){
			delStr = delStr + "_" + i;
		}
		i++;
	}
	if (delStr.length < 13){
		alert ("Please, select a sector first.");
	} else {
		document.pledgeForm.target = "_self";
		document.pledgeForm.action="/removePledgeSector.do?"+delStr;
		document.pledgeForm.submit();
	}		
}

function addFunding() {
	document.getElementsByName("fundingEvent")[0].value = "addFunding";
	
	document.pledgeForm.target = "_self";
	document.pledgeForm.action="/addFundingPledgeDetail.do";
	document.pledgeForm.submit();
	
}


function removeFunding() {
	document.getElementsByName("fundingEvent")[0].value = "delFunding";
	var i = 1
	var delStr = "deleteFunds="
	while (document.getElementById("checkFund"+i)!=null){
		if(document.getElementById("checkFund"+i).checked==true){
			delStr = delStr + "_" + i;
		}
		i++;
	}
	if (delStr.length < 13){
		alert ("Please, select a funding first.");
	} else {
		document.pledgeForm.target = "_self";
		document.pledgeForm.action="/addFundingPledgeDetail.do?"+delStr;
		document.pledgeForm.submit();
	}		
}

function savePledge() {

	<digi:context name="save" property="context/module/moduleinstance/savePledge.do" />
    document.pledgeForm.action = "<%= save %>?edit=true";
    document.pledgeForm.target = "_self";

    document.pledgeForm.submit();
}
 
--></script>

<digi:instance property="pledgeForm" />

<digi:form action="/addPledge.do" method="post">
<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<body>
<html:hidden name="pledgeForm" styleId="event" property="fundingEvent"/>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="1024" vAlign="top" align="center" border=0>
	
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left vAlign=top class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" >

										<digi:trn key="aim:desktop">Desktop</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:trn key="aim:addPledge">Add Pledge</digi:trn>
								
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height=16 vAlign=center width="100%"><span class=subtitle-blue>
								<digi:trn key="aim:addNewPledge">Add New Pledge</digi:trn>
								
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border=0>
						<tr><td width="75%" vAlign="top">
						<table cellPadding=0 cellSpacing=0 width="100%" border=0>
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn key="pledgeInformation">Pledge Information</digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgcolor="#f4f4f2" width="100%">
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<!-- contents -->
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG  height="10" src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:pledgeIdentification">Pledge Identification</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="30%">
													<FONT color=red>*</FONT>
													<a>
													<digi:trn key="pledgeTitle">Pledge Title</digi:trn>
													</a>
												
												</td>
												<td valign="middle" align="left" width="70%">
													<a>
														<html:text property="pledgeTitle" size="30"/>
                            						</a>
												</td>											
											</tr>
										</table>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG  height="10" src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:donorInformation">Donor Information</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="30%">
													<FONT color=red>*</FONT>
													<a>
													<digi:trn key="donorCountryInstitution">Donor (Country/Institution)</digi:trn>
													</a>
												
												</td>
												<td valign="middle" align="left" width="70%">
													<a>
<!--														<html:hidden property="selectedOrgId" />-->
<!--														<html:text property="selectedOrgName" readonly="true"/>-->
<!--														<aim:addOrganizationButton refreshParentDocument="true"  form="${pledgeForm}" htmlvalueHolder="selectedOrgId" htmlNameHolder="selectedOrgName" useClient="true" styleClass="dr-menu">...</aim:addOrganizationButton>-->
															<c:set var="valueId"> contrDonorId </c:set>
							                              <c:set var="nameId"> nameContrDonorId </c:set>
							                              <input  name='selectedOrgId' type="hidden" id="${valueId}" style="text-align:right" value='${pledgeForm.selectedOrgId}' size="4"/>
							                              <input name="selectedOrgName" type='text' id="${nameId}" style="text-align:right" value='${pledgeForm.selectedOrgName}' size="20" style="background-color:#CCCCCC" onKeyDown="return false" />
							                              <aim:addOrganizationButton useClient="true" htmlvalueHolder="${valueId}" htmlNameHolder="${nameId}" >...</aim:addOrganizationButton>
                            						</a>
													
												</td>											
											</tr>
										</table>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG height="10" src="../ampTemplate/images/arrow-014E86.gif" width="15">
										<b><digi:trn key="aim:pointContactDonorsConferenceMarch31st">Point of Contact at Donors Conference on March 31st</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactName">Name</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact1Name" size="20"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactTitle">Title</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact1Title" size="20"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactOrganization">Organization</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<c:set var="valueId1"> contact1OrgId </c:set>
							                              <c:set var="nameId1"> contact1OrgName </c:set>
							                              <input name='contact1OrgId' type="hidden" id="${valueId1}" style="text-align:right" value='${pledgeForm.contact1OrgId}' size="4"/>
							                              <input name="contrDonorName" type='text' id="${nameId1}" style="text-align:right" value='${pledgeForm.contact1OrgName}' size="20" style="background-color:#CCCCCC" onKeyDown="return false" />
							                              <aim:addOrganizationButton useClient="true" htmlvalueHolder="${valueId1}" htmlNameHolder="${nameId1}" >...</aim:addOrganizationButton>
                            						
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactMinistry">Ministry</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact1Ministry" size="20"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactAddress">Address</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact1Address" size="20"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactTelephone">Telephone</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact1Telephone" size="20"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactEmail">Email</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact1Email" size="20"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactFax">Fax</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact1Fax" size="20"/>
                            						</a>
												</td>										
											</tr>
										</table>
										<tr><td>&nbsp;</td></tr>
									<tr><td><b><digi:trn key="alternateContactPerson">Alternate Contact Person</digi:trn></b></td></tr>
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactName">Name</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contactAlternate1Name" size="20"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactTelephone">Telephone</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contactAlternate1Telephone" size="20"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactEmail">Email</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contactAlternate1Email" size="20"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
													</a>
												</td>										
											</tr>
										</table>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG height="10" src="../ampTemplate/images/arrow-014E86.gif" width="15">
										<b><digi:trn key="aim:pointContactFollowUp">Point of Contact for Follow Up</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr>
												<td valign="middle" align="left" width="20%" colspan="2">
													<a>
														<digi:trn key="sameAsOriginalPointOfContact">Same As Original Point Of Contact</digi:trn>
													</a>
													<input type="checkbox" id="sameContact" >
												</td>
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactName">Name</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact2Name" size="20"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactTitle">Title</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact2Title" size="20"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactOrganization">Organization</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<c:set var="valueId1"> contact2OrgId </c:set>
							                              <c:set var="nameId1"> contact2OrgName </c:set>
							                              <input name='contact2OrgId' type="hidden" id="${valueId1}" style="text-align:right" value='${pledgeForm.contact1OrgId}' size="4"/>
							                              <input name="contrDonorName" type='text' id="${nameId1}" style="text-align:right" value='${pledgeForm.contact1OrgName}' size="20" style="background-color:#CCCCCC" onKeyDown="return false" />
							                              <aim:addOrganizationButton useClient="true" htmlvalueHolder="${valueId1}" htmlNameHolder="${nameId1}" >...</aim:addOrganizationButton>
                            						
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactMinistry">Ministry</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact2Ministry" size="20"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactAddress">Address</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact2Address" size="20"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactTelephone">Telephone</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact2Telephone" size="20"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactEmail">Email</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact2Email" size="20"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactFax">Fax</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contact2Fax" size="20"/>
                            						</a>
												</td>										
											</tr>
										</table>
										<tr><td>&nbsp;</td></tr>
									<tr><td><b><digi:trn key="alternateContactPerson">Alternate Contact Person</digi:trn></b></td></tr>
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactName">Name</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contactAlternate2Name" size="20"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactTelephone">Telephone</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contactAlternate2Telephone" size="20"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="20%">
													<a>
														<digi:trn key="pointContactEmail">Email</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
														<html:text property="contactAlternate2Email" size="20"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="20%">
													<a>
													</a>
												</td>
												<td valign="middle" align="left" width="30%">
													<a>
													</a>
												</td>										
											</tr>
										</table>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr>
									    <td>
									        <!-- contents -->
									        <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
									        <b><digi:trn key="aim:sectorAndLocation">Sector and Location</digi:trn></b>
									         
									    </td>
							        </tr>
						            <tr><td>&nbsp;</td></tr>
									<tr>
						                <td>
						                    <table cellPadding=5 cellSpacing=1 border=0 width="100%"	bgcolor="#d7eafd">
						                    	<tr>
						                            <td align="left">
						                                <b>
						                                    <digi:trn key="aim:sector">
						                                        Sector
						                                    </digi:trn>
						                                </b>
						                            </td>
						                        </tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
									       <table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
                                             	<tr><td>
													<c:forEach var="pledgeSectors" items="${pledgeForm.pledgeSectors}" varStatus="index">
                                                            <tr> 
                                                                   <c:set var="indexSect" value="${indexSect+1}"/>
										                            <td align="center" width="3%">
																		<input type="checkbox" id="checkSect${indexSect}"  >
																	</td>
                                                                    <td  width="77%" valign="middle" align="left">
                                                                        
                                                                        [${pledgeSectors.sectorScheme}]
                                                                        <c:if test="${!empty pledgeSectors.sectorName}">
                                                                            [${pledgeSectors.sectorName}]
                                                                        </c:if>
									                               		<c:if test="${!empty pledgeSectors.subsectorLevel1Name}">
                                                                            [${pledgeSectors.subsectorLevel1Name}]
                                                                        </c:if>
																		<c:if test="${!empty pledgeSectors.subsectorLevel2Name}">
                                                                            [${pledgeSectors.subsectorLevel2Name}]
                                                                        </c:if>
																		
                                                                    </td>
                                                                    <td width="10%" valign="middle" align="right">
                                                                       
                                                                    <FONT color="red">*</FONT><digi:trn key="aim:percentage">Percentage</digi:trn>:&nbsp;</td>
                                                                    <td width="10%" valign="middle" align="left">
                                                                        <html:text name="pledgeSectors" indexed="true" property="sectorPercentage"size="2" onkeyup="fnChk(this, 'sector')" />
                                                                    </td>
                                                                </tr>
                                                                <c:set var="sectorAdded" value="true"/>
                                                           </c:forEach>
												</td></tr>
												<tr>
													<td colspan="2"> &nbsp;
                                                    
                                                           <html:button styleClass="dr-menu"  
                                                                         property="submitButton" onclick="addSectors();" >
                                                                <digi:trn key="btn:addSectors">Add Sectors</digi:trn>
                                                            </html:button>
															 &nbsp;
	                                                 		<logic:notEmpty name="pledgeForm" property="pledgeSectors">
																<html:button styleClass="dr-menu" property="submitButton" onclick="return removeSector()">
	                                                          	  <digi:trn key="btn:removeSector">Remove Sector</digi:trn>
	                                                        	</html:button>
															</logic:notEmpty>
	                                                </td>
	                                            </tr>
	                                        </table>
									     
									    </td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr>
						                <td>
						                    <table cellPadding=5 cellSpacing=1 border=0 width="100%"	bgcolor="#d7eafd">
						                    	<tr>
						                            <td align="left">
						                                <b>
						                                    <digi:trn key="aim:Location">
						                                        Location
						                                    </digi:trn>
						                                </b>
						                            </td>
						                        </tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
									       <table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
                                             	<tr><td>
												<c:forEach var="selectedLocs" items="${pledgeForm.selectedLocs}" varStatus="index">
                                                  <tr>
                                                      <c:set var="indexLoc" value="${indexLoc+1}"/>
								                            <td align="center" width="3%">
																<input type="checkbox" id="checkLoc${indexLoc}"  >
															</td>
                                                            <td align="left" width="77%">
	                                                            [${selectedLocs.location.name}] 
                                                            </td>
                                                            <td align="right" width="10%" nowrap="nowrap">
                                                            <FONT color="red">*</FONT>
                                                            		<digi:trn key="aim:percentage">Percentage</digi:trn>:&nbsp;
															</td>
															<td align="left" width="10%" nowrap="nowrap">
                                                            		<html:text name="selectedLocs" indexed="true" property="locationpercentage" size="2"  maxlength="3" onkeyup="fnChk(this, 'region')"/>
                                                            </td>
                                                          
                                                    </tr>
                                                  </c:forEach>
												</td></tr>
												<tr>
													<td colspan="2"> &nbsp;
                                                           <html:button styleClass="dr-menu"  
                                                                         property="submitButton" onclick="addLocation();">
                                                                <digi:trn key="btn:addLocation">Add Location</digi:trn>
                                                            </html:button>
															 &nbsp;
	                                                 		<logic:notEmpty name="pledgeForm" property="selectedLocs">
																<html:button styleClass="dr-menu" property="submitButton" onclick="return removeLocation()">
	                                                            <digi:trn key="btn:removeLocation">Remove Location</digi:trn>
	                                                        	</html:button>
															</logic:notEmpty>
	                                                </td>
	                                            </tr>
	                                        </table>
									     
									    </td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr>
									    <td>
									        <!-- contents -->
									        <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
									        <b><digi:trn key="aim:pledgeInformation">Pledge Information</digi:trn></b>
									         
									    </td>
							        </tr>
						            <tr><td>&nbsp;</td></tr>
									<logic:notEmpty name="pledgeForm" property="fundingPledgesDetails">
									<tr>
						                <td>
						                    <table cellPadding=5 cellSpacing=1 border=0 width="100%"	bgcolor="#d7eafd">
						                    	<tr>
						                            <td align="center" width="20%">
						                                <b><digi:trn key="aim:typeOfPledge">Type Of Pledge</digi:trn></b>
						                            </td>
													<td align="center" width="20%">
						                                <b><digi:trn key="aim:typeOfAssistance">Type Of Assistance</digi:trn></b>
						                            </td>
													<td align="center" width="15%">
						                                <b><digi:trn key="aim:amount">Amount</digi:trn></b>
						                            </td>
													<td align="center" width="10%">
						                                <b><digi:trn key="aim:typeOfCurrency">Currency</digi:trn></b>
						                            </td>
													<td align="center" width="15%">
						                                <b><digi:trn key="aim:date">Date</digi:trn></b>
						                            </td>
													<td align="center" width="20%">
						                                <b><digi:trn key="aim:aidModality">Aid Modality</digi:trn></b>
						                            </td>
						                        </tr>
											</table>
										</td>
									</tr>
									</logic:notEmpty>
									<tr>
										<td>
											<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
                                             	<c:forEach var="fundingPledgesDetails" items="${pledgeForm.fundingPledgesDetails}" varStatus="status">
									       		<tr>
													<c:set var="indexFund" value="${indexFund+1}"/>
						                            <c:set var="translation">
														<digi:trn key="aim:selectFromBelow">Please select from below</digi:trn>
													</c:set>	
													<td align="center" valign="bottom" >
														<input type="checkbox" id="checkFund${indexFund}"  >
													</td>
													<td align="center" valign="bottom" width="20%">
														 <category:showoptions firstLine="${translation}" name="fundingPledgesDetails" property="pledgetypeid"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.PLEDGES_TYPES_KEY%>" styleClass="inp-text"/>
							                        </td>
													<td align="center" valign="bottom" width="20%">
						                                <category:showoptions firstLine="${translation}" name="fundingPledgesDetails" property="typeOfAssistanceid"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.TYPE_OF_ASSISTENCE_KEY %>" styleClass="inp-text"/>
						                            </td>
													<td align="center" valign="bottom" width="15%">
						                                <html:text name="fundingPledgesDetails" indexed="true" property="amount" size="17" styleClass="inp-text"/>
						                            </td>
													<td align="center" valign="bottom" width="10%">
						                                <html:select name="fundingPledgesDetails" indexed="true" property="currencycode" styleClass="inp-text">
															<html:optionsCollection name="pledgeForm" property="validcurrencies" value="currencyCode"
															label="currencyName"/>
														</html:select>
						                            </td>
													<td align="center" valign="bottom" width="15%">
						                                <table cellPadding="0" cellSpacing="0">
															<tr>
																<td align="left" vAlign="bottom">
																	<html:text name="fundingPledgesDetails" property="funding_date" styleClass="inp-text"
																	styleId="${indexFund}" readonly="true" size="10" onchange="addForValidation(this)"/>
																</td>
																<td align="left" vAlign="bottom">&nbsp;
																	<a id="transDate${indexFund}" href='javascript:pickDateById("transDate${indexFund}",${indexFund})'>
																			<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0" align="top">
																		</a>
																</td>														
															</tr>
														</table>
						                            </td>
													<td align="center" valign="bottom" width="20%">
						                                <category:showoptions firstLine="${translation}" name="fundingPledgesDetails" property="aidmodalityid" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" styleClass="inp-text" />
						                            </td>
						                        </tr>
												</c:forEach>
									    		<tr>
													<td colspan="4"> &nbsp;
                                                           <html:button styleClass="dr-menu"  
                                                                         property="submitButton" onclick="addFunding();">
                                                                <digi:trn key="btn:addFunding">Add Funding</digi:trn>
                                                            </html:button>
															 &nbsp;
	                                                 		<logic:notEmpty name="pledgeForm" property="fundingPledgesDetails">
																<html:button styleClass="dr-menu" property="submitButton" onclick="return removeFunding()">
	                                                            <digi:trn key="btn:removeFunding">Remove Funding</digi:trn>
	                                                        	</html:button>
															</logic:notEmpty>
	                                                </td>
	                                            </tr>
	                                        </table>
									     </td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG  height="10" src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:additionalInformation">Additional Information</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="90%">
													<a>
														<html:textarea property="additionalInformation" rows="6" cols="80"/>
                            						</a>
												</td>											
											</tr>
										</table>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td align="center">
										<html:button styleClass="dr-menu" property="submitButton" onclick="return savePledge()">
	                                         <digi:trn key="btn:savePlegde">Save Pledge</digi:trn>
										</html:button>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<!-- end contents -->
							</td></tr>
							</table>
							</td></tr>
						</table>
						</td>
					</tr>	
					</table>
				</td></tr>
			</table>
		</td>
	</tr>
</table>

</body>
</digi:form>
