<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>

<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/yahoo-dom-event.js"/>"></script>
<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/animation-min.js"/>"></script>
<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/autocomplete-min.js"/>"></script>


<digi:instance property="messageForm" />
<html:hidden name="messageForm" property="tabIndex"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/script/messages.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<style>
<!--

.yui-skin-sam .yui-ac{position:relative;font-family:arial;font-size: 100%}
.yui-skin-sam .yui-ac-input{position:absolute;width:100%;font-size: 100%}
.yui-skin-sam .yui-ac-container{position:absolute;top:1.6em;width:100%;}
.yui-skin-sam .yui-ac-content{position:absolute;width:100%;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;}
.yui-skin-sam .yui-ac-shadow{position:absolute;margin:.3em;width:100%;background:#000;-moz-opacity:0.10;opacity:.10;filter:alpha(opacity=10);z-index:9049;}
.yui-skin-sam .yui-ac-content ul{margin:0;padding:0;width:100%;}
.yui-skin-sam .yui-ac-content li{margin:0;padding:2px 5px;cursor:default;white-space:nowrap;FONT-SIZE: 100%;}
.yui-skin-sam .yui-ac-content li.yui-ac-prehighlight{background:#B3D4FF;}
.yui-skin-sam .yui-ac-content li.yui-ac-highlight{background:#426FD9;color:#FFF;}

-->
</style>

<script langauage="JavaScript">
	
	function validate(){
		if(document.messageForm.messageName.value.length==0){
			alert('Please Enter Name');
			return false;
		}
		return true;
	}

	function save (event){
		if(!validate()){
			return false;
		}
		if(selectUsers(event)){
			messageForm.action="${contextPath}/message/messageActions.do?actionType=addMessage&toDo="+event;
  			messageForm.target = "_self";
  			messageForm.submit();		
		}	 		
	}
	
	
	function cancel() {
		messageForm.action="${contextPath}/message/messageActions.do?actionType=cancelMessage";
  		messageForm.target = "_self";
  		messageForm.submit();	
	}
	
	function selectUsers(event) {
    	var list = document.getElementById('selreceivers');
    	if (event=='send') {
    		if (list == null || list.length==0) {
        		alert('Please add receivers');
        		return false ;
    		}
    	}    	
    	if(list!=null){
    		for(var i = 0; i < list.length; i++) {
        		list.options[i].selected = true;
    		}
    	}
    	
    	return true;
	}
	// don't remove or change this line!!!
	document.getElementsByTagName('body')[0].className='yui-skin-sam';

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

-->
</style>

<c:set var="messageType">
    <c:choose>
        <c:when test="${messageForm.tabIndex==1}">
            <digi:trn key="message:Messages">Messages</digi:trn>
        </c:when>
        <c:when test="${messageForm.tabIndex==2}">
            <digi:trn key="message:Alerts">Alerts</digi:trn>
        </c:when>
        <c:when test="${messageForm.tabIndex==3}">
            <digi:trn key="message:approvals">Approvals</digi:trn>
        </c:when>
        <c:otherwise>
            <digi:trn key="message:ebents">Calendar Events</digi:trn>
        </c:otherwise>
    </c:choose>
</c:set>
<c:set var="title">
    <c:choose>
        <c:when test="${not empty messageForm.forwardedMsg}">
            
            <c:choose>
                <c:when test="${messageForm.tabIndex==1}">
            <digi:trn key="message:ForwardMessage">Forward Message</digi:trn>
        </c:when>
                <c:when test="${messageForm.tabIndex==2}">
                    <digi:trn key="message:ForwardAlert">Forward Alert</digi:trn>
                </c:when>
                <c:when test="${messageForm.tabIndex==3}">
                    <digi:trn key="message:forwardApprovals">Forward Approvals</digi:trn>
                </c:when>
                <c:otherwise>
                    <digi:trn key="message:ForwardEvents">Forward Calendar Events</digi:trn>
                </c:otherwise>
            </c:choose>
            
            
        </c:when>
        <c:when test="${messageForm.messageId==null}">
            <digi:trn key="message:AddMessage">Add Message</digi:trn>
        </c:when>
        <c:otherwise>
            <digi:trn key="message:EditMessage">Edit Message</digi:trn>
        </c:otherwise>
    </c:choose>
</c:set>

<digi:form action="/messageActions.do">
    <table cellSpacing=0 cellPadding=0 vAlign="top" align="left" width="100%">
		<tr>
			<td width="100%">
				<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />
			</td>
		</tr>
		<tr>
		<td>
			<table  cellPadding=0 cellSpacing=0 width=780 border="0">
			    <tr>
				   <td width=14>&nbsp;</td>
					<td align=left vAlign=top width=750>
						<table cellPadding=5 cellSpacing=0 width="100%">
							<tr>
								<td height=33>
									<span class=crumb>
										<c:set var="translation">
											<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
										</c:set>
										<digi:link href="/../aim/showDesktop.do" styleClass="comment" title="${translation}" >
											<digi:trn key="aim:portfolio">Portfolio</digi:trn>
										</digi:link>&nbsp;&gt;&nbsp
				                        <digi:link href="/messageActions.do?actionType=gotoMessagesPage&tabIndex=${messageForm.tabIndex}" styleClass="comment"  >
				                             ${messageType}
				                        </digi:link>
				                       &nbsp;&gt;&nbsp; ${title}
				                    </span>
								</td>
							</tr>
							<tr>
								<td height=16 vAlign=center width=571>
									<span class=subtitle-blue>							
											${title}						
									</span>
								</td>
							</tr>
							<tr>
								<td noWrap vAlign="top">
                                                                    <table class="contentbox_border" width="100%" cellpadding="0" cellspacing="0">
                                                                        <tr>				
                                                                            <td align="center" style="padding: 0px 3px 0px 3px;">
                                                                                <table width="100%">
                                                                                    <tr>
                                                                                        <td  style="height: 5px;"/>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td style="background-color: #CCDBFF;height: 18px;"/>
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                       
                                                                                  <tr>				
                                                                                      <td>
													<table width="100%" cellspacing="1" cellpadding="0"   valign="top">
                                                                                                            <tr>
															<td valign="top" bgcolor="#f4f4f2" align="center">
																<table width="100%" cellspacing="0" cellpadding="3" >																				
																	
																	<tr>
																		<td align="right" width="25%"><digi:trn key="messages:title">Title</digi:trn><font color="red">*</font> </td>
																		<td align="left"><html:text property="messageName" style="width:485px;" styleClass="inp-text"/></td>
																	</tr>																																					
																	<tr>
																		<td align="right"><digi:trn key="message:description">Description</digi:trn></td>
																		<td align="left"> <html:textarea name="messageForm" property="description"  rows="3" cols="73" styleClass="inp-text"/></td>
																	</tr>																				
																	<tr>
																		<td align="right" nowrap="nowrap"><digi:trn key="message:priorityLevel">Priority Level</digi:trn></td>
																		<td align="left"> 
																			<html:select property="priorityLevel" styleClass="inp-text">
																				<html:option value="-1"><digi:trn key="message:selectPriorityLevel">Select Priority level </digi:trn></html:option>
																				<html:option value="1"><digi:trn key="message:priorityLeel:low">low</digi:trn> </html:option>
																				<html:option value="2"><digi:trn key="message:priorityLevel:medium">Medium</digi:trn> </html:option>
																				<html:option value="3"><digi:trn key="message:priorityLevel:critical">Critical</digi:trn> </html:option>																							
																			</html:select>																												                                                																																												
																		</td>
																	</tr> 
																	<tr>
																		<td align="right" nowrap="nowrap" valign="top"><digi:trn key="message:relatedActivity">Related Activity</digi:trn></td>
																		<td align="left">
																			<div id="statesautocomplete"> 
																				<html:text property="selectedAct" name="messageForm" styleId="statesinput" style="width:485px;font-size:100%"></html:text>																			    
																				<div id="statescontainer" style="width:485px"></div> 
																			</div>																		
																		</td>																					
																	</tr>																	 																							
																	<tr>
																		<td align="right" valign="top"><digi:trn key="message:setAsAlert">Set as alert</digi:trn></td>
																		<td align="left"> 
																			<html:select property="setAsAlert" styleClass="inp-text">																							
																							<html:option value="0"><digi:trn key="message:no">No</digi:trn> </html:option>
																							<html:option value="1"><digi:trn key="message:yes">Yes</digi:trn> </html:option>																																														
																						</html:select>																												                                                																																												
																					</td>
																				<tr>
																				</tr>															
																				<tr>
																					<td nowrap="nowrap" valign="top" align="right"><digi:trn key="message:Receevers">Receivers</digi:trn></td>
																                    <td>
																                        <table border="0" >
																                            <tr>
																                                <td valign="top">
																                                   <select multiple="multiple" size="5" id="whoIsReceiver"  class="inp-text" style="width:200px">
																										<logic:empty name="messageForm" property="teamMapValues">
																											<option value="-1">No receivers</option>
																										</logic:empty>
																										<logic:notEmpty name="messageForm"  property="teamMapValues" >																								
                                                                                                    	    <option value="all"><digi:trn key="message:AllTeams">ALL</digi:trn></option>
                                                                                                               	<c:forEach var="team" items="${messageForm.teamMapValues}">
                                                                                                                   	<logic:notEmpty name="team" property="members">
                                                                                                                       	<option value="t:${team.id}" style="font-weight: bold;background:#CCDBFF;font-size:12px;">${team.name}</option>
                                                                                                                        <c:forEach var="tm" items="${team.members}">
                                                                                                                       		<option value="m:${tm.memberId}" style="font:italic;font-size:11px;" >${tm.memberName}</option>
                                                                                                                         </c:forEach>
                                                                                                                     </logic:notEmpty>											                                                		
                                                                                                                </c:forEach>
																                                        </logic:notEmpty>
																                                	</select>																                                         
																                                </td>
																                                <td>
																                                  <input type="button" onclick="addUserOrTeam();" style="width:80px;font-family:tahoma;font-size:11px;" value="<digi:trn key="message:addUsBtn">Add >></digi:trn>">
																                                  <br><br>
																                       			  <input type="button" style="width:80px;font-family:tahoma;font-size:11px;" onclick="removeUserOrTeam()" value="<<<digi:trn key="message:rmbtn">Remove</digi:trn>" >	
																                                </td>
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
																				<tr>
																					<td colspan="2">
																						<table width="100%" >
																							<tr>
																									<td align="right" width="30%">
																										<c:set var="trnSavetBtn">
																											<digi:trn key="messages:btn:save">Save</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSavetBtn }" onclick="save('draft');" class="dr-menu"/>
																									</td>
                                                                                                     <c:if test="${empty messageForm.forwardedMsg}">
																									<td align="center" width="6%">
																										<c:set var="trnSendtBtn">
																											<digi:trn key="messages:btn:send">Send</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSendtBtn }" onclick="save('send');" class="dr-menu"/>
																									</td>
                                                                                                    </c:if>
                                                                                                  <c:if test="${not empty messageForm.forwardedMsg}">
																									<td align="center" width="6%">
																										<c:set var="trnFwdtBtn">
																											<digi:trn key="messages:btn:fwd">Forward</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnFwdtBtn }" onclick="save('send');" class="dr-menu" />
																									</td>
                                                                                                    </c:if>
																									<td align="left" width="47%">
																										<c:set var="trnCancelBtn">
																											<digi:trn key="message:btn:cancel">Cancel</digi:trn>
																										</c:set>
																										<input type="button" value="${trnCancelBtn}" onclick="cancel();" class="dr-menu">																																							
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
																						

<script type="text/javascript">
	var myArray = [
		<c:forEach var="relAct" items="${messageForm.relatedActivities}">
			 "<bean:write name="relAct" filter="true"/>",
		</c:forEach>     
	];

	YAHOO.example.ACJSArray = new function() {
		// Instantiate JS Array DataSource
	    this.oACDS2 = new YAHOO.widget.DS_JSArray(myArray);
	    // Instantiate AutoComplete
	    this.oAutoComp2 = new YAHOO.widget.AutoComplete('statesinput','statescontainer', this.oACDS2);
	    this.oAutoComp2.prehighlightClassName = "yui-ac-prehighlight";    
	    this.oAutoComp2.useShadow = true;
	    this.oAutoComp2.forceSelection = true;
	    this.oAutoComp2.formatResult = function(oResultItem, sQuery) {
	        var sMarkup = oResultItem[0];
	        return (sMarkup);
	    };
	}; 

</script>
</digi:form>
