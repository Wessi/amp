<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#show_login_pop').click(function () { 
    	$("#show_login_pop_box").toggle();
  	});
	
});
</script>
<digi:secure authenticated="false">
<logic:notPresent name="currentMember" scope="session">
	<form action="/j_acegi_security_check" method="post" style="margin:0;">
		<table border="0" cellspacing="0" cellpadding="0">
  			<tr>
  				<td>
  					<div class="login_here">
  						<div class="login_here_cont">
  							<a  id="show_login_pop" style="color:#000000; font-weight:bold;text-decoration: underline;cursor: pointer;">
  								<digi:trn>Login Here</digi:trn>
  							</a>
  						</div>
  					</div>
  				</td>
				<td style="font-size: 11px;color: #ffffff">
					<c:set var="trn3">
						<digi:trn key="aim:clickforNewUserRegistration">Click here for new user registration</digi:trn>
					</c:set>
					<digi:link href="/showRegisterUser.do" module="aim" title="${trn3}">
			    		<digi:trn key="aim:newUserRegistration"> New user registration</digi:trn>
			  		</digi:link>	
				 	&nbsp;|&nbsp; 
					<c:set var="title">
						<digi:trn>Click here to change your password</digi:trn>
					</c:set>
	  				<digi:link href="/showChangePassword.do" title="${title}" module="aim">
	    				<digi:trn> Change Password</digi:trn>
	  				</digi:link>	 
	  				&nbsp;|&nbsp; 
					<c:set var="trn1">
                		<digi:trn key="aim:clickifyouForgotYourPassword">Click here if you have problem with login in</digi:trn>
                	</c:set>
                	<digi:link href="/showEmailForm.do" module="aim" title="${trn1}">
                   		<digi:trn key="aim:forgotPassword">Trouble signing in?</digi:trn>
					</digi:link>
				</td>
  			</tr>
  			<tr>
    			<td>
					<div id="show_login_pop_box">
						<table cellpadding="2" cellspacing="2">
							<tr>
				    			<td>
				    				<div class="login_label" style="font-size: 11px;">
				    					<digi:trn>Username</digi:trn>:
				    				</div>
				    			</td>
				    			<td>
				    				<input name="j_username" type="text" class="inputx">
				    			</td>
							</tr>
							<tr>
    							<td>
    								<div class="login_label" style="font-size: 11px;">
    									<digi:trn>Password</digi:trn>:</div>
    							</td>
	    						<td>
	    							<input name="j_password" type="password" class="inputx">
	    						</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
	    						<td>
	    							<html:submit  styleClass="buttonx_sm_lgn" property="submitButton">
  										<digi:trn key="btn:signIn">Login</digi:trn>
  									</html:submit>
	    						</td>
							</tr>
						</table>
					</div>
				</td>
    			<td>&nbsp;</td>
  			</tr>
		</table>
	</form>
	<c:if test="${param['loginError'] != null}">
		<div class="error_login">
			<c:if test="${param['loginError'] == 'invalidLogin'}">
				<c:set var="errorDisplayed">true</c:set>
				<bean:message key="error.aim.invalidLogin" />
			</c:if>
			<c:if test="${param['loginError'] == 'userBanned'}">
				<c:set var="errorDisplayed">true</c:set>
				<bean:message key="error.aim.userBanned" />
			</c:if>
			<c:if test="${param['loginError'] == 'noTeamMember'}">
				<c:set var="errorDisplayed">true</c:set>
				<digi:trn key="error.aim.userinvalidteammembernoworkplace">You can not login into AMP because you are not assigned to a workspace</digi:trn>
			</c:if>
			<c:if test="${param['loginError'] == 'invalidUser'}">
				<c:set var="errorDisplayed">true</c:set>
				<bean:message key="error.aim.userInvalid" />
			</c:if>
			<c:if test="${errorDisplayed == null}">
				<bean:message key="error.aim.loginFailed" />
			</c:if>
		</div>
	</c:if>
					
</logic:notPresent>
</digi:secure>

  
