<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<html:javascript formName="userRegisterForm" />

      <TABLE width="500px" align=center>
      <digi:form action="/userRegisterGateway.do" method="post" onsubmit="return validateUserRegisterForm(form);" >
        <TR>
          <TD class=PageTitle colSpan=2><digi:trn key="um:registerBlank">Register BLANK</digi:trn></TD></TR>
        <TR>
          <TD class=text align=left colSpan=2><digi:trn key="um:completeForm">To become a member of the
            Development Gateway, please complete the form below.</digi:trn></TD></TR>
        <TR>
          <TD>&nbsp;</TD></TR>
        <TR>
          <TD class=title align=left colSpan=2><digi:trn key="um:accountInfoAboutYou">Account information / about you</digi:trn></TD></TR>
        <TR>
          <TD class=text align=left colSpan=2><digi:trn key="um:allMarkedRequiredField">All fields marked with an <FONT
            color=red><B><BIG>*</BIG></B></FONT> are required.</digi:trn> <digi:trn key="um:userValidEmail"> Please use a
            valid e-mail address.</digi:trn></TD></TR>
        <TR>
          <TD colSpan=2 td>
            <TABLE cellSpacing=1 cellPadding=2 width="95%" border=0>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="um:firstName">First Name</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="firstNames" size="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="um:lastName">Last Name</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="lastName" size="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="um:emailAddress">E-mail Address</digi:trn></TD>
            <TD class=text noWrap align=left><INPUT type=hidden
                  value=642226 name=user_id> <html:text  property="email" size="50" />
            </TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="um:repEmailAddress">Repeat Email Address</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="emailConfirmation" size="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="um:password">Password</digi:trn></TD>
            <TD class=text noWrap align=left><html:password  property="password" size="30" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="um:repPassword">Repeat Password</digi:trn></TD>
            <TD class=text noWrap align=left><html:password  property="passwordConfirmation" size="30" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left><FONT color=red>*</FONT><digi:trn key="um:countryOfResidence">Country of Residence</digi:trn></TD>
            <TD class=text noWrap align=left>
              <html:select  property="selectedCountryResidence" >
                <bean:define id="countries" name="userRegisterForm" property="countryResidence" type="java.util.Collection" />
                <html:options  collection="countries" property="iso" labelProperty="countryName" />
              </html:select>
            </TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="um:mailingAddress">Mailing Address</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="mailingAddress" size="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="um:organizationName">Organization Name</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="organizationName" size="50" /></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="um:organizationType">Organization Type</digi:trn></TD>
            <TD class=text noWrap align=left>
              <html:select property="selectedOrganizationType">
                <bean:define id="types" name="userRegisterForm" property="organizationType" type="java.util.Collection" />
                <html:options collection="types" property="id" labelProperty="type" />
              </html:select>
            </TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="um:website">Website</digi:trn></TD>
            <TD class=text noWrap align=left><html:text  property="webSite" size="60" /></TD>
          </TR>
          <tr><td><img src="/images/trans.gif" height=15 width=1 border=0></td></tr>
          <TR >
            <TD class=text noWrap align=left colspan=2>
          	<table cellpadding=0 cellspacing=0 border=0>
             <tr><td nowrap class="text"><digi:trn key="um:howDidYouHear">How did you hear about the Development Gateway?</digi:trn></td></tr>
             <tr><td>
              <html:select property="howDidyouSelect">
                <bean:define id="howDidYou" name="userRegisterForm" property="howDidyouhear" type="java.util.Collection" />
                <html:options collection="howDidYou" property="id" labelProperty="referral" />
              </html:select>
              </td>
             </tr>
             </table>
            </TD>
          </TR>
          <tr><td><img src="/images/trans.gif" height=15 width=1 border=0></td></tr>
          <TR >
            <TD class=text noWrap align=left colspan=2>
             <table border="0" cellspacing="1" cellpadding="0" width="100%">
                <TR bgcolor="#D5D5D5">
                   <TD align=left noWrap class="title" ><digi:trn key="um:focus">Focus</digi:trn></TD>
                   <TD align=center noWrap class="title"><digi:trn key="um:join">Join</digi:trn></TD>
                </TR>
               <logic:iterate name="userRegisterForm" type="org.digijava.kernel.entity.ItemBeanInfo"  id="item" property="items">
                <tr bgColor="#f0f0f0">
                   <td><c:out value="${item.info}"/></td>
                   <td align="center"><html:multibox  property="selectedItems" ><c:out value="${item.value}"/> </html:multibox></td>
                </tr>
               </logic:iterate>
          <tr><td><img src="/images/trans.gif" height=15 width=1 border=0></td></tr>
          <TR bgcolor="#D5D5D5">
            <TD align=left noWrap class="title"><digi:trn key="um:topics">Topics</digi:trn></TD>
            <TD align=center noWrap class="title"><digi:trn key="um:join">Join</digi:trn></TD>
          </TR>
           <c:forEach var="item" items="${userRegisterForm.topicitems}">
            <tr bgColor="#f0f0f0">
               <td><c:out value="${item.info}"/></td>
              <td align="center"><html:multibox  property="topicselectedItems" ><c:out value="${item.value}"/> </html:multibox></td>
            </tr>
           </c:forEach>
              </table>
            </TD>
          </TR>
          <TR >
            <TD align=left noWrap class="text"><digi:trn key="um:sendMeMonthlyNewsletter">Send me the monthly e-mail newsletter?</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor="#FFFFFF">
            <TD align=left noWrap class=text><html:radio  property ="newsLetterRadio" value="true"/><digi:trn key="um:yes">Yes</digi:trn>
              <html:radio  property ="newsLetterRadio" value="false"/><digi:trn key="um:no">No</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <tr><td><img src="/images/trans.gif" height=15 width=1 border=0></td></tr>
          <TR >
            <TD align=left noWrap class=title><digi:trn key="um:yourMembersProfile">YOUR MEMBERS PROFILE</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR >
            <TD align=left class="text" colspan=2>
            <digi:trn key="um:contributionsRegister">            
            <p>In addition to having your name
                associated with your content contributions, you can create a
                member profile and appear in the topic directory so that other
                members can see your organization, country of residence and
                favorite topics. <br>
                Please note that your e-mail address will not be displayed.</digi:trn> </p></TD>
          </TR>
          <TR bgColor="#FFFFFF">
            <TD align=left noWrap class=text><digi:trn key="um:displMyMemberProfile">Display my member profile</digi:trn> <html:radio  property ="membersProfile" value="true"/><digi:trn key="um:yes">Yes</digi:trn>
              <html:radio  property ="membersProfile" value="false"/><digi:trn key="um:no">No</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <tr><td><img src="/images/trans.gif" height=15 width=1 border=0></td></tr>
          <TR>
            <TD align=left noWrap class="title"><digi:trn key="um:yourLangSettings">Your language settings</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor="#FFFFFF">
            <TD align=left noWrap class=text><digi:trn key="um:alertLanguage">Alert language</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor="#FFFFFF">
            <TD align=left noWrap class=text>
              <html:select  property="selectedLanguage">
                <bean:define id="languages" name="userRegisterForm" property="navigationLanguages" type="java.util.Collection" />
                <html:options  collection="languages" property="code" labelProperty="name" />
              </html:select>
             </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class="text"><digi:trn key="um:iViewContentInLanguages">I want to view content in following languages</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class=text>
          	<table cellpadding=0 cellspacing=0 border=0>
		  	 <c:forEach var="item" items="${userRegisterForm.contentLanguages}">
             <tr><td>
             <html:multibox property="contentSelectedLanguages" ><c:out value="${item.code}"/></html:multibox>
             <c:out value="${item.name}"/>
              </td></tr>
             </c:forEach>
            </table>
            </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
        </TABLE >
            </td>

<TR>
<TD colspan=2 align=center>
<html:submit property="submit" value="SUBMIT"/>
</TD>
</TR>
</digi:form>
</TABLE>