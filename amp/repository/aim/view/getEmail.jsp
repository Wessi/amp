<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="globalsettings" %>

<digi:form name="aimUserEmailForm" type="org.digijava.module.um.form.UserEmailForm" action="/resetUserPassword.do" >
<div class="reg_form_container">
<div class="reg_form_title"><h2><digi:trn key="aim:changePassword">
						Reset your e-mail address
						</digi:trn></h2></div>
<table width="100%" valign="top" align="left" cellpadding="0" cellspacing="0" border="0">

<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="5%">&nbsp;
		</td>
		<td align=left valign="top" width="60%"><br>
			<table border="0" cellPadding=5 cellspacing="0" width="60%">
				<tr>
					<td width="3">&nbsp;</td>
					<td colspan="2">
						<digi:errors/>
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td align=right class=f-names noWrap>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<digi:trn key="aim:email">Email</digi:trn>
					</td>
					<td align="left">
						<html:text property="email" size="20" />
						<font color="red">
						<digi:trn key="aim:userIdExample1">
						e.g. yourname@emailaddress.com
						</digi:trn>
						</font>
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td colspan="2" align="center">
						<html:submit property="submit" styleClass="dr-menu" ><digi:trn key="btn:submit">Submit</digi:trn> </html:submit>
					</td>
				</tr>
			</table>
		</td>
		<td width="5%">&nbsp;
		</td>
		<td bgcolor="#dbe5f1" valign="top" width="30%">
	      <table align="center" border="0" cellPadding=3 cellspacing="0" width="90%">
        		<tr>
		          <td valign="top">&nbsp;</td>
        		</tr>
        		<tr>
	          	<td valign="top">
<span class="formnote">
						 <digi:trn key="aim:loginWarning">
						 You are signing-in to one or more secure applications for
	        		     official business. You have been granted the right to access these
    	      		 	 applications and the information contained in them to facilitate
        	   			 your official business. Your accounts and passwords are your
						 responsibility. Do not share them with anyone.
						 </digi:trn>
</span>						<BR><BR>
          		</td>
  				</tr>
        		<tr>
          		<td valign="top">&nbsp;</td>
  				</tr>
	      </table>
		  <TR>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>



</div>