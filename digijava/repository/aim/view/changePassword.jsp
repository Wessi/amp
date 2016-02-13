<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="aimChangePasswordForm" />

<html:javascript formName="aimChangePasswordForm"/>

<digi:form action="/changePassword.do" method="post" onsubmit="return validateAimChangePasswordForm(this);">

<table width="100%" valign="top" align="left" cellpadding=0 cellSpacing=0 border=0>
<tr><td width="100%" valign="top" align="left">
<jsp:include page="header.jsp" flush="true" />			
</td>
</tr>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border=0 cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;
		</td>
		<td align=left class=r-dotted-lg vAlign=top width=520><br>
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
						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
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
						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
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
						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
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
						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
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
						<html:submit value="Submit" styleClass="dr-menu" />
					</td>
				</tr>

				<tr>
					<td colspan=3>&nbsp;</td>				
				</tr>
				
			</table>
		</td>
		<td bgColor=#f7f7f4 class=r-dotted-lg vAlign=top>
	      <table align=center border=0 cellPadding=3 cellSpacing=0 width="90%">
      		 <tr>
		          <td class=r-dotted-lg-buttom vAlign=top><br>
						<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
						<bean:define id="translation">
							<digi:trn key="aim:clickToUseAmp">Click here to Use AMP now</digi:trn>
						</bean:define>
						<digi:link href="/index.do" title="<%=translation%>" >	
						<digi:trn key="aim:useAMPSiteNow">
						Use AMP Ethiopia now
						</digi:trn>
						</digi:link>
						<BR><BR><BR>
      	     	</td>
        		</tr>
        		<tr>
		          <td vAlign=top>&nbsp;</td>
        		</tr>
        		<tr>
	          	<td class=r-dotted-lg-buttom vAlign=top>
						<digi:img src="module/aim/images/i-C2160E.gif" width="13" height="9"/>
						<digi:trn key="aim:loginWarning">
						 You are signing-in to one or more secure applications for        
        			    official business. You have been granted the right to access these        
          		 	 applications and the information contained in them to facilitate        
           			 your official business. Your accounts and passwords are your        
						 responsibility. Do not share them with anyone.        
						 </digi:trn>
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
</td></tr>
</table>
</digi:form>