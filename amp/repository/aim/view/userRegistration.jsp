<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">



	function optionChanged(flag) {



		if (flag == 'otype') {

			var index1  = document.aimUserRegisterForm.selectedOrgType.selectedIndex;

			var val1    = document.aimUserRegisterForm.selectedOrgType.options[index1].value;

			var orgType = document.aimUserRegisterForm.orgType.value;

			if ( val1 != "-1") {

				if (val1 != orgType) {

					document.aimUserRegisterForm.orgType.value = val1;

					document.aimUserRegisterForm.actionFlag.value = "typeSelected";

					<digi:context name="selectType" property="context/module/moduleinstance/showRegisterUser.do" />

		   			document.aimUserRegisterForm.action = "<%= selectType %>";

					document.aimUserRegisterForm.target = "_self";

					document.aimUserRegisterForm.submit();

				}

				return false;

			}

			else

				return false;

		}

		if (flag == 'ogroup') {

			var index2  = document.aimUserRegisterForm.selectedOrgGroup.selectedIndex;

			var val2    = document.aimUserRegisterForm.selectedOrgGroup.options[index2].value;

			var orgGrp = document.aimUserRegisterForm.orgGrp.value;

			if ( val2 != "-1") {

				if (val2 != orgGrp) {

					document.aimUserRegisterForm.orgGrp.value = val2;

					document.aimUserRegisterForm.actionFlag.value = "groupSelected";

					<digi:context name="selectGrp" property="context/module/moduleinstance/showRegisterUser.do" />

		   			document.aimUserRegisterForm.action = "<%= selectGrp %>";

					document.aimUserRegisterForm.target = "_self";

					document.aimUserRegisterForm.submit();

				}

				return false;

			}

			else

				return false;

		}

	}

	function isVoid(name){
        if (name == "" || name == null || !isNaN(name) || name.charAt(0) == ' '){
        	return true;
        }		
		return false;		
	}
	function validate(){
        name = document.aimUserRegisterForm.firstNames.value;
        lastname = document.aimUserRegisterForm.lastName.value;
        password = document.aimUserRegisterForm.password.value;
        passwordConfirmation = document.aimUserRegisterForm.passwordConfirmation.value;
        selectedOrgType = document.aimUserRegisterForm.selectedOrgType.value;
        selectedOrgGroup = document.aimUserRegisterForm.selectedOrgGroup.value;
        selectedOrganizationId = document.aimUserRegisterForm.selectedOrganizationId.value;
        
        if (isVoid(name))
        {
			<c:set var="translation">
			<digi:trn key="erroruregistration.FirstNameBlank">First Name is Blank</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if (isVoid(lastname))
        {
			<c:set var="translation">
			<digi:trn key="error.registration.LastNameBlank">LastName is Blank</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(validateEmail()==false)
            return false
        if (isVoid(password)||isVoid(passwordConfirmation))
        {
			<c:set var="translation">
			<digi:trn key="error.registration.passwordOneChar">Please use at least one letter in the password field.</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(password != passwordConfirmation){
			<c:set var="translation">
			<digi:trn key="error.registration.NoPasswordMatch">Passwords in both fields must be the same</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(selectedOrgType=="-1"){
			<c:set var="translation">
			<digi:trn key="error.registration.enterorganizationother">Please enter Organization Type</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(selectedOrgGroup=="-1"){
			<c:set var="translation">
			<digi:trn key="error.registration.NoOrgGroup">Please Select Organization Group</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(selectedOrganizationId=="-1"){
			<c:set var="translation">
			<digi:trn key="error.registration.NoOrganization">Please Select Organization</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }

        return true;
	}
	function validateEmail() {
	    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
		var address = document.aimUserRegisterForm.email.value;
		var address2 = document.aimUserRegisterForm.emailConfirmation.value;
		if(reg.test(address) == false||reg.test(address2) == false) {
			<c:set var="translation">
			<digi:trn key="error.registration.noemail">you must enter Valid email please check in</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
		}
		if(address != address2){
			<c:set var="translation">
			<digi:trn key="error.registration.noemailmatch">Emails in both fields must be the same</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
		}
		return true;
	}


</script>





<digi:instance property="aimUserRegisterForm" />

<digi:form action="/registerUser.do" method="post" onsubmit="return validateAimUserRegisterForm(this);">



<html:hidden property="orgType" />

<html:hidden property="orgGrp" />

<input type="hidden" name="actionFlag" value="">



<table width="100%" valign="top" align="left" cellpadding=0 cellSpacing=0 border=0>


<tr><td width="100%" valign="top" align="left">

<table bgColor=#ffffff border=0 cellPadding=0 cellSpacing=0 width=772>

	<tr>

		<td width=14>&nbsp;

		</td>

		<td align=left vAlign=top width=520><br>

			<table border=0 cellPadding=5 cellSpacing=0 width="100%">

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=left class=title noWrap colspan="2">

						<!-- digi:errors /-->
						<logic:notEmpty name="aimUserRegisterForm" property="errors" >
                        <font color="red">
							<ul>
							
                            <logic:iterate id="element"	name="aimUserRegisterForm" property="errors">
                               <li><digi:trn key="${element.key}">
                                   <bean:write name="element" property="value"/>
                               </digi:trn></li>
                           </logic:iterate>
                           </ul>
                        </font>
                        </logic:notEmpty>
					</td>

				</tr>

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=left class=title noWrap colspan="2">

						<digi:trn key="um:accountInfoAboutYou">Account information / about you

						</digi:trn>

					</td>

				</tr>

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=left class=title noWrap colspan="2">

						<digi:trn key="um:allMarkedRequiredField">All fields marked with an <FONT color=red><B><BIG>*</BIG>

						</B></FONT> are required.

						</digi:trn>

						<digi:trn key="um:userValidEmail"> Please use a valid e-mail address.</digi:trn>

					</td>

				</tr>

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap width="40%">

						<FONT color=red>*</FONT>						

						<digi:trn key="um:firstName">First Name

						</digi:trn>

					</td>

					<td align="left">

						<html:text property="firstNames" size="20" styleClass="inp-text"/>

					</td>

				</tr>

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap width="40%">

						<FONT color=red>*</FONT>

						<digi:trn key="um:lastName">Last Name

						</digi:trn>

					</td>

					<td align="left">

						<html:text property="lastName" size="20" styleClass="inp-text"/>

					</td>

				</tr>

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						<FONT color=red>*</FONT>

						<digi:trn key="um:emailAddress">E-mail Address

						</digi:trn>

					</td>

					<td align="left">

						<html:text property="email" size="20" styleClass="inp-text"/>

					</td>

				</tr>

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						<FONT color=red>*</FONT>

						<digi:trn key="um:repEmailAddress">Repeat Email Address

						</digi:trn>

					</td>

					<td align="left">

						<html:text property="emailConfirmation" size="20" styleClass="inp-text"/>

					</td>

				</tr>

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						<FONT color=red>*</FONT>

						<digi:trn key="um:password">Password

						</digi:trn>

					</td>

					<td align="left">

						<html:password property="password" size="20" />

					</td>

				</tr>

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						<FONT color=red>*</FONT>

						<digi:trn key="um:repPassword">Repeat Password

						</digi:trn>

					</td>

					<td align="left">

						<html:password property="passwordConfirmation" size="20" />

					</td>

				</tr>



				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						<FONT color=red>*</FONT>

						<digi:trn key="um:countryOfResidence">Country of Residence

						</digi:trn>

					</td>

					<td align="left">

              <html:select  property="selectedCountryResidence" styleClass="inp-text">
                  <c:forEach var="cn" items="${aimUserRegisterForm.countryResidence}">
                    <html:option value="${cn.iso}">${cn.name}</html:option>
                  </c:forEach>
              </html:select>

					</td>

				</tr>



				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						<digi:trn key="um:mailingAddress">Mailing Address

						</digi:trn>

					</td>

					<td align="left">

						<html:text property="mailingAddress" size="20" styleClass="inp-text"/>

					</td>

				</tr>

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						 <FONT color=red>*</FONT> 

						<digi:trn key="um:organizationType">Organization Type</digi:trn>

					</td>

					<td align="left">

						<html:select property="selectedOrgType" styleClass="inp-text" onchange="optionChanged('otype')">

							<html:option value="-1">-- <digi:trn key="um:selectType">Select a type</digi:trn> --</html:option>

							<html:optionsCollection name="aimUserRegisterForm" property="orgTypeColl"

													value="ampOrgTypeId" label="orgType" />

						</html:select>

					</td>

				</tr>

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						<FONT color=red>*</FONT> 

						<digi:trn key="um:organizationGroup">Organization Group</digi:trn>

					</td>

					<td align="left">

						<html:select property="selectedOrgGroup" styleClass="inp-text" onchange="optionChanged('ogroup')">

							<html:option value="-1">-- <digi:trn key="um:selectGroup">Select a group</digi:trn> --</html:option>

							<logic:notEmpty name="aimUserRegisterForm" property="orgGroupColl" >

								<html:optionsCollection name="aimUserRegisterForm" property="orgGroupColl"

									         			value="ampOrgGrpId" label="orgGrpName" />

							</logic:notEmpty>



						</html:select>

					</td>

				</tr>

				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						<FONT color=red>*</FONT> 	

						<digi:trn key="um:organizationName">Organization Name

						</digi:trn>

					</td>

					<td align="left">
						<html:hidden property="organizationName" value="-1"/>

						<html:select property="selectedOrganizationId" styleClass="inp-text" >

							<html:option value="-1">-- <digi:trn key="um:selectOrganization">Select an organization</digi:trn> --</html:option>

							<logic:notEmpty name="aimUserRegisterForm" property="orgColl" >

								<html:optionsCollection name="aimUserRegisterForm" property="orgColl" value="ampOrgId" label="name" />

							</logic:notEmpty>

						</html:select>

					</td>

				</tr>



				<!-- <tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						<digi:trn key="um:website">Website

						</digi:trn>

					</td>

					<td align="left">

						<html:text property="webSite" size="20" styleClass="inp-text"/>

					</td>

				</tr> -->



				<tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						<digi:trn key="um:yourLangSettings">Your language settings

						</digi:trn>

					</td>

					<td align="left">

              <html:select  property="selectedLanguage" styleClass="inp-text">

                <bean:define id="languages" name="aimUserRegisterForm" property="navigationLanguages"

					 type="java.util.Collection" />

                <html:options  collection="languages" property="code" labelProperty="name" />

              </html:select>

					</td>

				</tr>

				<tr>

					<td>&nbsp;</td>

					<td>&nbsp;</td>

					<td align="left">
                                        <c:set var="btnSubmit">
                                        <digi:trn key="btn:submit">Submit</digi:trn>
                                        </c:set>


						<html:submit value="${btnSubmit}" styleClass="dr-menu" onclick="return validate();"/>

					</td>

				</tr>

				<tr>

					<td colspan=3>&nbsp;</td>

				</tr>

			</table>

		</td>

		<td bgcolor="#dbe5f1"   vAlign=top>

	      <table align=center border=0 cellPadding=3 cellSpacing=0 width="90%">

      		 <tr>

		          <td  vAlign=top><br>


<!--						<c:set var="translation">-->
<!---->
<!--							<digi:trn key="aim:clickToUseAmp">Click here to Use AMP now</digi:trn>-->
<!---->
<!--						</c:set>-->
<!---->
<!--						<digi:link href="/index.do" title="${translation}" >-->
<!---->
<!--						<digi:trn key="aim:useAMPSiteNow">-->
<!---->
<!--						Use AMP now-->
<!---->
<!--						</digi:trn>-->
<!---->
<!--						</digi:link>-->
<!--						<BR><BR><BR>-->

      	     	</td>

        		</tr>

        		<tr>

		          <td vAlign=top>&nbsp;</td>

        		</tr>

        		<tr>

	          	<td  vAlign=top>

<strong>
						<digi:trn key="aim:loginWarning">

						 You are signing-in to one or more secure applications for

        			    official business. You have been granted the right to access these

          		 	 applications and the information contained in them to facilitate

           			 your official business. Your accounts and passwords are your

						 responsibility. Do not share them with anyone.

						 </digi:trn		>
</strong>
						<BR><BR>

          		</td>

  				</tr>

        		<tr>

          		<td vAlign=top>&nbsp;</td>

  				</tr>

	      </table>

		  <TR>

		</td>

	</tr>

</table>
<br /><br />
</td></tr>

</table>

</digi:form>






