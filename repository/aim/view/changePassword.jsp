<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimChangePasswordForm" />

<html:javascript formName="aimChangePasswordForm"/>

<digi:form action="/changePassword.do" method="post" onsubmit="return validateAimChangePasswordForm(this);">

<table width="100%" valign="top" align="left" cellpadding=0 cellSpacing=0 border=0>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border=0 cellPadding=0 cellSpacing=0 width=100%>
	<tr>
		<td width="5%">&nbsp;
		</td>
		<td align=left vAlign=top width="60%"><br>
			<table border=0 cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td width="3">&nbsp;</td>
					<td vAlign=left>
						<digi:errors />
					</td>
					<td>&nbsp;
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td class=subtitle-blue vAlign=top>
						<digi:trn key="aim:changePassword">
						Change Password
						</digi:trn>
					</td>
					<td>&nbsp;
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td align=right class=f-names noWrap width="40%">
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<digi:trn key="aim:email">
							UserId:
						</digi:trn>
					</td>
					</td>
					<td align="left">
						<html:text property="userId" size="20" /><br>
						<font color="red">
						<digi:trn key="aim:userIdExample1">
						e.g. yourname@emailaddress.com
						</digi:trn>
						</font>
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td align=right class=f-names noWrap>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<digi:trn key="aim:oldPassword">
						Old Password:
						</digi:trn>
					</td>
					<td align="left">
						<html:password property="oldPassword" size="20" />
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td align=right class=f-names noWrap>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<digi:trn key="aim:newPassword">
						New Password:
						</digi:trn>
					</td>
					<td align="left">
						<html:password property="newPassword" size="20" />
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td align=right class=f-names noWrap>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<digi:trn key="aim:confirmNewPassword">
						Confirm new Password:
						</digi:trn>
					</td>
					<td align="left">
						<html:password property="confirmNewPassword" size="20" />
					</td>
				</tr>

				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td align="left">

						<html:submit styleClass="dr-menu"><digi:trn key="btn:submit">Submit</digi:trn></html:submit>
					</td>
				</tr>

				<tr>
					<td colspan=3>&nbsp;</td>
				</tr>

			</table>
		</td>
		<td width="5%">&nbsp;
		</td>
		<td bgcolor="#dbe5f1" vAlign=top width="30%">
	      <table align=center border=0 cellPadding=3 cellSpacing=0 width="90%">
        		<tr>
		          <td vAlign=top>&nbsp;</td>
        		</tr>
        		<tr>
	          	<td vAlign=top>
                <strong>
						<digi:trn key="aim:loginWarning">
						 You are signing-in to one or more secure applications for
        			    official business. You have been granted the right to access these
          		 	 applications and the information contained in them to facilitate
           			 your official business. Your accounts and passwords are your
						 responsibility. Do not share them with anyone.
						 </digi:trn>
						<BR><BR>
				</strong>
          		</td>
  				</tr>
        		<tr>
          		<td vAlign=top>&nbsp;</td>
  				</tr>
	      </table>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>



