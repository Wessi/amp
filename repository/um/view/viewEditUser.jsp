<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="umViewEditUserForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<!-- jsp:include page="teamPagesHeader.jsp" flush="true" /-->
<!-- End of Logo -->
<script language="javascript" type="text/javascript">

function goAction(value){
	var submitForm=true;
  	if(value!=null){
	  	if(value=='save'){
	  		submitForm=validateUserInfo();		  
	  	}
	  	if(submitForm==true){
	  		document.getElementById("event").value=value;
	    	document.umViewEditUserForm.submit();
	  	}    	
  	}
}
function resetPasswordFields(){
  document.umViewEditUserForm.newPassword.value='';
  document.umViewEditUserForm.confirmNewPassword.value=''
}
function validate(str,value){
  var newPassword=document.umViewEditUserForm.newPassword.value;
  var confirmNewPassword=document.umViewEditUserForm.confirmNewPassword.value;
  if(newPassword.length==0||confirmNewPassword.length==0||newPassword!=confirmNewPassword){
    alert(str);
  }
  else{
    goAction(value);
  }
}

function validateUserInfo(){
	var userMail=document.getElementById("userMail").value;
	var firstName=document.getElementById("firstName").value;
	var lastName=document.getElementById("lastName").value;
	var country=document.getElementById("country").value;
	var errorMsg='';
	
	if(isInvalid(userMail)){
		errorMsg='<digi:trn jsFriendly="true">Email Is Blank</digi:trn>';
		alert(errorMsg);
		return false;
	}else if(userMail.indexOf('@')==-1 || userMail.indexOf('.')==-1){
		errorMsg='<digi:trn jsFriendly="true" >Please enter valid email</digi:trn>';
		alert(errorMsg);
		return false;
	}
	if(isInvalid(firstName)){
		errorMsg='<digi:trn jsFriendly="true" >FirstName Is Blank</digi:trn>';
		alert(errorMsg);
		return false;
	}
	if(isInvalid(lastName)){
		errorMsg='<digi:trn jsFriendly="true">LastName Is Blank</digi:trn>';
		alert(errorMsg);
		return false;
	}
	if(country=='-1'){
		errorMsg='<digi:trn jsFriendly="true" >Please Select Country</digi:trn>';
		alert(errorMsg);
		return false;
	}
	return true;
}

function isInvalid(field){
	if (field == null || field == '' || !isNaN(field) || field.charAt(0) == ' '){
    	return true;
    }		
	return false;
}
</script>
<digi:form action="/viewEditUser.do" method="post">
  <html:hidden name="umViewEditUserForm" property="event" styleId="event"/>
  <table bgColor="#ffffff" cellPadding="5" cellSpacing="1" width="705">
	<tr>
		<td class="r-dotted-lg" width="14">&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=752>
			<table cellPadding="5" cellSpacing="0" width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height="33"><span class=crumb>

						<c:set var="translation">
				            <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
				          </c:set>
				          <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
				            <digi:trn key="aim:AmpAdminHome">
				            Admin Home
				            </digi:trn>
				          </digi:link>&nbsp;&gt;&nbsp;
				
				          <c:set var="translation">
				            <digi:trn key="aim:clickToViewAllUsers">Click here to goto users manager</digi:trn>
				          </c:set>
				          <digi:link href="/viewAllUsers.do" styleClass="comment" title="${translation}" >
				            <digi:trn key="aim:viewAllUsers">
				            List of users
				            </digi:trn>
				          </digi:link>&nbsp;&gt;&nbsp;
				
				          <digi:trn key="aim:viewEditUser:EditUser">
				          Edit user
				          </digi:trn>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
			    <tr>
			      <td colspan="2">
			        <span class=subtitle-blue>
			          <digi:trn key="aim:viewEditUser:EditUserHeader">
			          Edit user
			          </digi:trn>
			        </span>
			      </td>
			    </tr>
    			<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:errors/>
					        &nbsp;
					        <br/>
					        <logic:equal name="umViewEditUserForm" property="displaySuccessMessage" value="true" >
					          <b>
					            <digi:trn key="aim:viewEditUser:changePasswordSuccessMessage">
					            The password has been changed successfully
					            </digi:trn>
					          </b>
					        </logic:equal>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
			    </tr>
   				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="740" cellspacing="1" cellSpacing="1">
					<tr>
						<td noWrap width="616" vAlign="top">
							<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" class="box-border-nopadding" width="100%">
								
								<tr bgColor="#f4f4f2">
									<td valign="top">
										<table align="center" bgColor="#f4f4f2" cellPadding="0" cellSpacing="0" width="562" border="0">
											<tr>
												<td bgColor="#ffffff" class="box-border" width="560">
													<table border="0" cellPadding="1" cellSpacing="1" class="box-border" width="100%">
														<tr bgColor="#dddddb">
															<td bgColor="#dddddb" height="20" align="center" colspan="5">
																 <b><digi:trn key="aim:viewEditUser:edit">Edit</digi:trn> ${umViewEditUserForm.name}</b>
															</td>
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">
																<table width="563" border="0" bgColor="#f4f4f2" height="363">
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:viewEditUser:firstName">First name</digi:trn>
																			<font color="red">*</font>
																		</td>
																	    <td width="380" height="30" colspan="2" >
																	          <html:text name="umViewEditUserForm" property="firstNames" style="font-family:verdana;font-size:11px;width:180px;" styleId="firstName"/>																	          
																	    </td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:viewEditUser:lastName">Last name</digi:trn>
																			<font color="red">*</font>
																		</td>
																	    <td width="380" height="30" colspan="2" >
																	          <html:text name="umViewEditUserForm" property="lastName" style="font-family:verdana;font-size:11px;width:180px;" styleId="lastName"/>
																	    </td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																	        <digi:trn key="aim:viewEditUser:email">Email </digi:trn>
																	        <font color="red">*</font>
																		</td>
																		<td width="190" height="30">
																           <html:text name="umViewEditUserForm" property="email" style="font-family:verdana;font-size:11px;width:180px;" styleId="userMail"/>
																		</td>
																	</tr>
																	<tr>
																		<td width="169" height="30" align="right">
																			<digi:trn key="aim:viewEditUser:country">Country</digi:trn>
																			<font color="red">*</font>
																	    </td>
																	    <td width="190" height="30">
																           <html:select name="umViewEditUserForm" property="selectedCountryIso" style="font-family:verdana;font-size:11px;width:180px;" styleId="country">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectCountry">
																                  --Select country--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.countries}">
																                  <c:forEach var="cn" items="${umViewEditUserForm.countries}">
																                    <c:set var="cnName">
																                    ${cn.name}
																                      <%--<digi:trn key="aim:cn:${cn.name}">${cn.name}</digi:trn>--%>
																                    </c:set>
																                    <html:option value="${cn.iso}">${cnName}</html:option>
																                  </c:forEach>
																                </c:if>
																             </html:select>
																		</td>
																	</tr>
                                                          			<tr>
																		<td width="169" align="right" height="30">
                                                                    		<digi:trn key="aim:viewEditUser:mailingAddress">Mailing address</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    		<html:text name="umViewEditUserForm" property="mailingAddress" style="font-family:verdana;font-size:11px;width:180px;"/>
																		</td>
                                                          			</tr>
                                                          			<tr>
																		<td width="169" align="right" height="2">
                                                                            &nbsp;
																		</td>																		
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																	       <digi:trn key="aim:viewEditUser:organizationType">Organization type</digi:trn>
																		</td>
																		<td width="380" height="30" colspan="2">
																			<html:select name="umViewEditUserForm" property="selectedOrgTypeId" onchange="goAction('typeSelected');" style="font-family:verdana;font-size:11px;width:180px;">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectOrganisationType">
																                  --Select organisation type--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.orgTypes}">
																                  <html:optionsCollection name="umViewEditUserForm" property="orgTypes" value="ampOrgTypeId" label="orgType"/>
																                </c:if>
																            </html:select>
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																	        <digi:trn key="aim:viewEditUser:organizationGroup">Organization group</digi:trn>
																		</td>
																		<td width="380" height="30" colspan="2">
																	         <html:select name="umViewEditUserForm" property="selectedOrgGroupId" onchange="goAction('groupSelected');" style="font-family:verdana;font-size:11px;width:180px;">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectOrganisationGroup">
																                  --Select organisation group--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.orgGroups}">
																                  <html:optionsCollection name="umViewEditUserForm" property="orgGroups" value="ampOrgGrpId" label="orgGrpName"/>
																                </c:if>
																              </html:select>
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
                                                                   			<digi:trn key="aim:viewEditUser:organisationName">Organisation name</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    		<html:select name="umViewEditUserForm" property="selectedOrgId" style="font-family:verdana;font-size:11px;width:180px;">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectOrganisation">
																                  --Select organisation--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.orgs}">
																                  <html:optionsCollection name="umViewEditUserForm" property="orgs" value="ampOrgId" label="name"/>
																                </c:if>
																             </html:select>
                                                               			</td>
																	</tr>

																	<tr>
																		<td width="169" align="right" height="30">
                                                                 			<digi:trn key="aim:viewEditUser:verifiedAssignedOrg">Verified Assigned Organisation</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    		<html:select name="umViewEditUserForm" property="assignedOrgId" style="font-family:verdana;font-size:11px;width:180px;">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectOrganisation">
																                  --Select organisation--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.orgs}">
																                  <html:optionsCollection name="umViewEditUserForm" property="orgs" value="ampOrgId" label="name"/>
																                </c:if>
																            </html:select>
																		</td>
																	</tr>
																	<tr>
																		<td>&nbsp;</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
                                                                     		 <digi:trn key="aim:viewEditUser:languageSettings">Language settings</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
          																	<html:select name="umViewEditUserForm" property="selectedLanguageCode" style="font-family:verdana;font-size:11px;width:180px;">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectLanguage">
																                  --Select language--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.languages}">
																                  <html:optionsCollection name="umViewEditUserForm" property="languages" value="code" label="name"/>
																                </c:if>
																             </html:select>
																		</td>
																	</tr>
																	<tr>
															            <td>&nbsp;</td>
															          </tr>
																	<tr>
																	    <td width="380" height="30" colspan="2" align="center">
                                                              				<c:set var="translation">
																                <digi:trn key="aim:viewEditUser:saveButton" jsFriendly="true">Save</digi:trn>
																              </c:set>
																              <input type="button" value="${translation}" onclick="goAction('save');" style="font-family:verdana; font-size:11px; min-width:60px; "/>
																
																              <c:set var="translation">
																                <digi:trn key="aim:viewEditUser:cancelButton" jsFriendly="true">
																                Cancel
																                </digi:trn>
																              </c:set>
																              <input type="button" value="${translation}" onclick="history.back();" style="font-family:verdana; font-size:11px; min-width:60px; "/>
																		</td>
																	</tr>																	
																	<tr>
															            <td>&nbsp;</td>
															          </tr>
															          <tr>
															            <td>&nbsp;</td>
															          </tr>
																	<tr>
																		<c:set var="translation">
															              <digi:trn key="aim:viewEditUser:changePasswordButton">Change password</digi:trn>
															            </c:set>
																		<td width="169" align="right" height="30">
                                                                   			<digi:trn key="aim:viewEditUser:password">Password:</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    		<html:password name="umViewEditUserForm" property="newPassword" />
                                                               			</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
                                                                   			<digi:trn key="aim:viewEditUser:confirmPassword">Confirm:</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    		<html:password name="umViewEditUserForm" property="confirmNewPassword" />
                                                               			</td>
																	</tr>
																	<tr>
																		<c:set var="errMsg">
																           <digi:trn jsFriendly="true">
																              Either fields are blank or their values do not match
																            </digi:trn>
																        </c:set>
																	    <td width="270" height="30" coslpan="2" align="right">
                                                              				<input type="button" value="${translation}" onclick="validate('${errMsg}','changePassword');" style="font-family:verdana;font-size:11px;"/></td>
																		</td>
																		<td width="169" height="30" colspan="2" align="left">
																			<c:set var="translation">
																                <digi:trn key="aim:viewEditUser:resetPasswordButton" jsFriendly="true">Reset </digi:trn>
																              </c:set>
                                                              				<input type="button" value="${translation}" onclick="resetPasswordFields()" style="font-family:verdana;font-size:11px;"/></td>
																		</td>
																	</tr>
																	<tr height="5px"><td colspan="2">&nbsp;</td></tr>
																	<tr>
																		<td colspan="2" align="center">
																			<c:set var="trn">
																                <digi:trn jsFriendly="true">Assign User To Workspace</digi:trn>
																              </c:set>
                                                              				<input type="button" value="${trn}" onclick="goAction('gotoAssignWorkspacePage')" style="font-family:verdana;font-size:11px;"/></td>
																		</td>
																	</tr>
																</table>
															</td>
														</tr>
													<!-- end page logic -->
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
        </table>
        <br />
</digi:form>