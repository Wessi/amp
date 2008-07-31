<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="messageForm" />
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/script/messages.js"/>"></script>
<script langauage="JavaScript">

	function validate(){
		if(document.messageForm.messageName.value.length==0){
			alert('Please Enter Name');
			return false;
		}
		return true;
	}

	function save (){
		if(!validate()){
			return false;
		}
		if(selectUsers()){
			messageForm.action="${contextPath}/message/templatesManager.do?actionType=saveTemplate";
  			messageForm.target = "_self";
  			messageForm.submit();		
		}	 		
	}
	
	
	function cancel() {
		messageForm.action="${contextPath}/message/templatesManager.do?actionType=cancel";
  		messageForm.target = "_self";
  		messageForm.submit();	
	}

	function selectUsers() {
    	var list = document.getElementById('selreceivers');
    	if (list == null || list.length==0) {
        	alert('Please add receivers');
        	return false ;
    	}    	    	
    	if(list!=null){
    		for(var i = 0; i < list.length; i++) {
        		list.options[i].selected = true;
    		}
    	}
    	
    	return true;
	}

</script>
<style  type="text/css">
<!--

.contentbox_border{
        border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}

#statesautocomplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#statesautocomplete div {
	padding: 0px;
	margin: 0px; 
}



#statesautocomplete,
#statesautocomplete2 {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#statesautocomplete {
    z-index:9000; /* z-index needed on top instance for ie & sf absolute inside relative issue */
}
#statesinput,
#statesinput2 {
    _position:absolute; /* abs pos needed for ie quirks */
}
.charcounter {
    display: block;
}

-->
</style>
<html:hidden name="messageForm" property="templateId"/>
<digi:form action="/templatesManager.do">
			<table width="100%" cellspacing="0" cellpadding="0" border="0" bgcolor="#ffffff" align="center" valign="top">
				<tr>
<!--					<td class="r-dotted-lg" width="10"/>-->
<!--					<td class="r-dotted-lg" valign="top" align="left">-->
						<table width="98%" cellspacing="3" cellpadding="1" align="left" valign="top">
						<td/>
						<td>
							<table width="100%" cellspacing="5" cellpadding="3" border="0" valign="top">
								<tr>
									<td width="75%" valign="top">
										<table width="100%" cellspacing="0" cellpadding="5">
											<tr>
												<td height=33 width="100%">
												&nbsp;
												</td>
											</tr>
											<tr>
								               <td height=16 vAlign=center width=571>
									              <span class=subtitle-blue>							
											        <digi:trn key="aim:pagetitle:add/edittemplateWizard">Add/Edit Template Wizard</digi:trn>						
									              </span>
								               </td>
							                </tr>
											
											<tr>
												<td width="100%">
													<table width="100%" cellspacing="1" cellpadding="4" align="left" valign="top">
														<tr>
															<td valign="top">
																<table class="contentbox_border" width="75%" border="0" bgcolor="#f4f4f2">
							                                        <tr>			
							                                           <td align="center">
							                                         	  <table width="100%">
							                                                  <tr>
							                                                	<td style="background-color: #CCDBFF;height: 18px;"/>
							                                                  </tr>
							                                              </table>
							                                           </td>
							                                        </tr>
																	<tr>
																		<td valign="top" bgcolor="#f4f4f2" align="center">
																			<table width="100%" cellspacing="3" cellpadding="5">																				
																				<tr>
																					<td align="right" width="25%"><digi:trn key="messages:templateName">Template Name</digi:trn><font color="red">*</font> </td>
																					<td align="left" width="90%"><html:text property="messageName" styleClass="inp-text" style="width:600px;"/></td>
																				</tr>																																				
																				<tr>
																					<td align="right"><digi:trn key="messages:text">Text</digi:trn></td>
																					<td align="left"> <html:textarea name="messageForm" property="description" rows="3" cols="75" styleClass="inp-text"/></td>
																				</tr>																				
																				<tr>
																					<td align="right"><digi:trn key="messages:relatedTriggers">related trigger</digi:trn></td>
																					<td align="left"> 
																						<html:select property="selectedTrigger" name="messageForm" styleClass="inp-text">
																							<html:option value="-1"><digi:trn key="message:selectRelatedTrigger">Select from below</digi:trn></html:option>
																							<html:optionsCollection name="messageForm" property="availableTriggersList" label="label" value="value"/>
																								<!-- 
																								<logic:iterate id="trigger" name="messageForm" property="availableTriggersList">																																															
																									<html:option value="${trigger}">${trigger}</html:option>																		
																								</logic:iterate>
																								 --> 
																						</html:select> 
																					</td>
																				</tr>													
																				<tr>
																					<td nowrap="nowrap" valign="top" align="right"><digi:trn key="message:Receevers">Receivers</digi:trn></td>
																                    <td>
																                        <table border="0" >
																                            <tr>
																                                <td valign="top">
																                                    <table border="0" width="100%">																                                        
																                                        <tr>
																                                            <td>
																                                              <select multiple="multiple" size="5" id="whoIsReceiver"  class="inp-text" style="width:200px">
																												<logic:empty name="messageForm" property="teamMapValues">
																													<option value="-1">No receivers</option>
																												</logic:empty>
																												<logic:notEmpty name="messageForm"  property="teamMapValues" >
																												    <option value="all" ><digi:trn key="message:AllTeams">All</digi:trn></option>																								
																													<c:forEach var="team" items="${messageForm.teamMapValues}">
																														<logic:notEmpty name="team" property="members">
																															<option value="t:${team.id}" style="font-weight: bold;background:#CCDBFF;font-size:11px;">---${team.name}---</option>
																                                                			<c:forEach var="tm" items="${team.members}">
																                                                				<option value="m:${tm.memberId}" id="t:${team.id}" style="font:italic;font-size:11px;">${tm.memberName}</option>
																                                                			</c:forEach>
																														</logic:notEmpty>											                                                		
																                                                	</c:forEach>
																                                                </logic:notEmpty>
																                                            </select>
																                                            </td>
																                                        </tr>
																                                    </table>
																                                </td>
																                                <td>
																                                  <input type="button" onclick="addUserOrTeam();" style="width:80px;font-family:tahoma;font-size:11px;" value="<digi:trn key="message:addUsBtn">Add >></digi:trn>">
																                                  <br><br>
																                       			  <input type="button" style="width:80px;font-family:tahoma;font-size:11px;" onclick="removeUserOrTeam()" value="<<<digi:trn key="message:rmbtn">Remove</digi:trn>" >	
																                                </td>
																                                <td valign="top">
																                                    <table border="0" width="100%" cellpadding="0">																                                       
																                                        <tr>
																                                            <td nowrap="nowrap" valign="top">
																                                                <table border="0" width="100%">																                                                   
																                                                    <tr>
																                                                        <td valign="top">																
																                                                            <html:select multiple="multiple" styleId="selreceivers" name="messageForm" property="receiversIds"  size="5" styleClass="inp-text" style="width:200px">
																                                              					<c:if test="${!empty messageForm.receivers}">
																	                                                              	<html:optionsCollection name="messageForm" property="receivers" value="value" label="label" />
																	                                                              </c:if>                
																                                                            </html:select>
																                                                        </td>																                                                        
																                                                    </tr>
																                                                </table>
																                                            </td>
																                                        </tr>
																                                    </table>
																                                </td>
																                            </tr>
																                        </table>
																                    </td>
																				</tr>																											
																				<tr>
																					<td colspan="2">
																						<table width="100%" >
																							<tr>
																								<td align="right" width="47%">
																									<c:set var="trnSavetBtn">
																										<digi:trn key="message:btn:save">save</digi:trn>
																									</c:set> 
																									<input type="button" value="${trnSavetBtn }" onclick="save();" />
																								</td>																									
																								<td align="left" width="47%">
																									<c:set var="trnCancelBtn">
																										<digi:trn key="message:btn:cancel">Cancel</digi:trn>
																									</c:set>
																									<input type="button" value="${trnCancelBtn}" onclick="cancel();">																																							
																								</td>
																							</tr>																							
																						</table>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>																							
																	<tr>
																		<td/>
																	</tr>
																</table>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>	
									</td>
								</tr>
							</table>
						</td>
						<td/>
						</table>
					</td>
					<td width="10"/>
				</tr>
			</table>
</digi:form>