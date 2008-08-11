<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>


<digi:instance property="messageForm"/>
<digi:form action="/msgSettings.do" >
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script langauage="JavaScript">

	var help='<digi:trn key="message:msgHelp">Message Settings Help</digi:trn>'
	var helpForRefreshTime='<digi:trn key="message:helpForMsgRefreshTime">This indicates the time in minutes the messages count on the user\'s desktop is refreshed.</digi:trn>'
	var helpForStorage='<digi:trn key="message:helpForStorage">This indicates the total number of messages that are permissible per type of message <br>i.e. Messages , Alerts , Approvals and Events. Within Messages and Alerts this setting <br>indicates the total number of messages permissible in the Inbox , Sent and Draft messages.</digi:trn>'
	var helpForAdvanceAlerts='<digi:trn key="message:helpForAdvAlerts">This indicates the number of days in advance an Alert will be recieved by a user<br> for all alerts that are time sensitive and for upcoming events.</digi:trn>'
	var helpForEmailable='<digi:trn key="message:helpForEmailables">Set this to true to allow all alerts to be forwarded to the user\'s email address</digi:trn>'
	
	function validate(record){
		if(record==null || record=="" || record=="-1"){
			alert('<digi:trn key="message:pleaseenterdata">Please enter data to save</digi:trn>');
			return false;
		}
		return true;
	}

	function saveRecord(settingType) {
		var record;
		if(settingType=='refreshTime'){
			 record=document.getElementsByName('msgRefreshTimeNew')[0];
		}else if(settingType=='storage'){
			record=document.getElementsByName('msgStoragePerMsgTypeNew')[0];
		}else if(settingType=='warning'){
			record=document.getElementsByName('daysForAdvanceAlertsWarningsNew')[0];
		}else if(settingType=='emailAlerts'){
			record=document.getElementsByName('emailMsgsNew')[0];
		}

		if(validate(record.value)){
			 <digi:context name="saveRecord" property="context/module/moduleinstance/msgSettings.do"/>
			 url = "<%= saveRecord %>?actionType=saveSettings&settingType="+settingType;
			 messageForm.action =url;
			 messageForm.submit();
			 return true;
		}
	}

	function saveAll(){
		<digi:context name="saveAll" property="context/module/moduleinstance/msgSettings.do"/>
		url = "<%= saveAll %>?actionType=saveSettings&settingType=saveAll";
		messageForm.action =url;
		messageForm.submit();
		return true;
	}
</script>


	<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">
		<TR>
			<TD class=r-dotted-lg-buttom vAlign=top>
				<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
						<TR><TD bgColor="#ffffff" class="box-border" align="left">
							<TABLE border="0" cellPadding="1" cellSpacing="1" width="100%">
								<TR>
									<TD>
										<TABLE border="1" cellPadding="3" cellSpacing="3" width="100%" bordercolor="#ccecff" rules="all">
											<tr>
												<td colspan="4" align="center" bgcolor="#ccecff"><digi:trn key="message:msgSettings">Message Settings</digi:trn> </td>
											</tr>
											<tr>
												<td align="center"><digi:trn key="message:setting">Setting</digi:trn> </td>
												<td align="center"><digi:trn key="message:currentValue">Current Value</digi:trn></td>
												<td align="center"><digi:trn key="message:newValue">New Value</digi:trn></td>
												<td>&nbsp;</td>
											</tr>
											<tr>
												<td align="center" >
													<digi:trn key="message:refreshTime">Message Refresh Time(minutes)</digi:trn> &nbsp;
													<img src="../ampTemplate/images/help.gif" onmouseover="stm([help,helpForRefreshTime],Style[13])" onmouseout="htm()"/>
												</td>
												<td align="center">${messageForm.msgRefreshTimeCurr}</td>
												<td align="center"><html:text name="messageForm" property="msgRefreshTimeNew" /></td>
												<td align="center">
													<c:set var="saveBtn"><digi:trn key="message:btn:save">Save</digi:trn></c:set>
													<input type="button" value="${saveBtn}" onclick="saveRecord('refreshTime')"/>
												</td>
											</tr>
											<tr>
												<td align="center">
													<digi:trn key="message:msgStorage">Message Storage Per Message Type</digi:trn>
													<img src="../ampTemplate/images/help.gif" onmouseover="stm([help,helpForStorage],Style[13])" onmouseout="htm()"/>
												</td>
												<td align="center">${messageForm.msgStoragePerMsgTypeCurr} </td>
												<td align="center"><html:text name="messageForm" property="msgStoragePerMsgTypeNew" /> </td>
												<td align="center"><input type="button" value="${saveBtn}" onclick="saveRecord('storage')" /></td>
											</tr>
											<tr>
												<td align="center">
													<digi:trn key="message:advanceAlertWarnings">Days of Advance Alert Warnings</digi:trn>
													<img src="../ampTemplate/images/help.gif" onmouseover="stm([help,helpForAdvanceAlerts],Style[13])" onmouseout="htm()"/>
												</td>
												<td align="center">${messageForm.daysForAdvanceAlertsWarningsCurr}</td>
												<td align="center"><html:text name="messageForm" property="daysForAdvanceAlertsWarningsNew"/> </td>
												<td align="center"><input type="button" value="${saveBtn}" onclick="saveRecord('warning')" /></td>
											</tr>
											<tr>
												<td align="center">
													<digi:trn key="message:emailAlerts">Email Alerts</digi:trn>
													<img src="../ampTemplate/images/help.gif" onmouseover="stm([help,helpForEmailable],Style[13])" onmouseout="htm()"/>
												</td>
												<td align="center">
													<c:if test="${messageForm.emailMsgsCurrent==-1}">
														&nbsp;
													</c:if>
													<c:if test="${messageForm.emailMsgsCurrent==0}">
														<digi:trn key="message:No">No</digi:trn>
													</c:if>
													<c:if test="${messageForm.emailMsgsCurrent==1}">
														<digi:trn key="message:yes">Yes</digi:trn>
													</c:if>
												</td>
												<td align="center">
													<html:select property="emailMsgsNew" name="messageForm">
														<html:option value="-1"><digi:trn key="aim:pleaseselectemail">please select</digi:trn></html:option>
														<html:option value="0"><digi:trn key="message:No">No</digi:trn></html:option>
														<html:option value="1"><digi:trn key="message:yes">Yes</digi:trn></html:option>
													</html:select>
												</td>
												<td align="center"><input type="button" value="${saveBtn}" onclick="saveRecord('emailAlerts')" /></td>
											</tr>
											<tr>
												<td colspan="4" align="right">
													<c:set var="saveAllBtn"><digi:trn key="message:btn:saveAll">Save All</digi:trn></c:set>
													<input type="button" value="${saveAllBtn}" onclick="saveAll()" />
												</td>
											</tr>
										</TABLE>
									</TD>
									<TD>&nbsp;</TD>
									<td  vAlign="top">
										<table align=center cellPadding=0 cellSpacing=0 width="70%" border=0>
											<tr>
												<td>
													<!-- Other Links -->
													<table cellPadding=0 cellSpacing=0 width=100>
														<tr>
															<td bgColor=#c9c9c7 class=box-title>
																<digi:trn key="aim:otherLinks">
																Other links
																</digi:trn>
															</td>
															<td background="module/aim/images/corner-r.gif" height="17" width=17>
																&nbsp;
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table cellPadding=3 cellSpacing=1 width="100%">
														<tr>
															<td>
																<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
																<c:set var="translation">
																	<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
																</c:set>
																<digi:link href="/admin.do" title="${translation}" module="aim">
																	<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
																</digi:link>
															</td>
														</tr>
														<tr>
															<td>
																<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
																<c:set var="trn">
								                                  <digi:trn key="message:viewSettings">Click here to view TemplateAlerts Manager</digi:trn>
								                                </c:set>
								                                <digi:link module="message" href="/templatesManager.do?actionType=viewTemplates" title="${trn}">
								                                  <digi:trn key="message:templatesManager">TemplateAlerts Manager</digi:trn>
								                                </digi:link>
															</td>
														</tr>
														<tr>
															<td>
																<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
																<c:set var="trn">
								                                  <digi:trn key="aim:viewSettings">Click here to view Job Manager</digi:trn>
								                                </c:set>
								                                <digi:link module="message" href="/quartzJobManager.do" title="${trn}">
								                                  <digi:trn key="aim:jobManager">Job Manager</digi:trn>
								                                </digi:link>
															</td>
														</tr>
														<tr>
															<td>
																<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
																<c:set var="trn">
								                                  <digi:trn key="aim:viewSettings">Click here to view Job Class Manager</digi:trn>
								                                </c:set>
								                                <digi:link module="message" href="/quartzJobClassManager.do" title="${trn}">
								                                  <digi:trn key="aim:jobClassManager">Job Class Manager</digi:trn>
								                                </digi:link>
															</td>
														</tr>
														<!-- end of other links -->
													</table>
												</td>
											</tr>
										</table>
									</td>
								</TR>
							</TABLE>
						</TD></TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</digi:form>
