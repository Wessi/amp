<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>

<style>
<!--
tr.my-border-style td {
      border-bottom: 1px solid silver;
}
-->
</style>

<digi:instance property="messageForm"/>
<digi:form action="/messageActions.do">
<html:hidden name="messageForm" property="msgRefreshTimeCurr"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script langauage="JavaScript">

	var noMsgs='<digi:trn key="message:noMessages">No Messages Present</digi:trn>';
	var from='<digi:trn key="message:from">From</digi:trn>';
	var received='<digi:trn key="message:received">Received</digi:trn>';
	var prLevel='<digi:trn key="message:priority">priority</digi:trn>';
	var description='<digi:trn key="message:msgDetails">Message Details</digi:trn>';
	var editBtn='<digi:trn key="message:Edit">Edit</digi:trn>';
	var fwdBtn='<digi:trn key="message:Forward">Forward</digi:trn>';
	var deleteBtn='<digi:trn key="message:delete">Delete</digi:trn>';
	//used to define whether we just entered page from desktop
	var firstEntry=0;
	
	//used to hold already rendered messages
	var myArray=new Array();
	
	window.onload=getMessages;
		
	//setting timer to check for new messages after specified time	
	if(document.getElementsByName('msgRefreshTimeCurr')[0].value>0){
		id=window.setTimeout("checkForNewMessages()",60000*document.getElementsByName('msgRefreshTimeCurr')[0].value,"JavaScript");
	}


	 function checkForNewMessages(){
		var url=addActionToURL('messageActions.do?actionType=viewAllMessages');			
		var async=new Asynchronous();
		async.complete=buildMessagesList;
		async.call(url);
		id=window.setTimeout("checkForNewMessages()",60000*document.getElementsByName('msgRefreshTimeCurr')[0].value,"JavaScript");
	}
	
	function viewMessage(id) {
		openURLinWindow('${contextPath}/message/messageActions.do?actionType=viewSelectedMessage&msgStateId='+id,550,400);
	}
	
	function deleteMessage(msgId) {
		if(deleteMsg()){
			//remove current element from array
			var index=getIndexOfElement(msgId);
			if(index!=-1){
				myArray.splice(index,1);	
				//removing TR from rendered messages list
				var tbl=document.getElementById('msgsList');			
				var img=document.getElementById(msgId+'_plus');
				var imgTD=img.parentNode;
				var msgTR=imgTD.parentNode;
				tbl.removeChild(msgTR);
				//removing record from db
				var url=addActionToURL('messageActions.do');	
				url+='~actionType=removeSelectedMessage';
				url+='~editingMessage=false';
				url+='~msgStateId='+msgId;		
				var async=new Asynchronous();
				async.complete=buildMessagesList;
				async.call(url);		
			}	
		}
	}
	
	function deleteMsg(){
		return confirm("Are You Sure You Want To Remove This Message ?");
	}
	
	function getIndexOfElement(elemId){
		var index=-1;
		for(var i=0;i<myArray.length;i++){
			if(myArray[i]==elemId){
				index=i;
				return index;
			}
		}
	}
	
	
	function goToPage(page){	
		 <digi:context name="searchMsg" property="context/module/moduleinstance/messageActions.do"/>
		 url = "<%= searchMsg %>?actionType=viewAllMessages&page="+page;
		 document.forms[0].action =url;
		 document.forms[0].submit();		 
		 return true;
	}
	
	function toggleGroup(group_id){
		var strId='#'+group_id;
		$(strId+'_minus').toggle();
		$(strId+'_plus').toggle();		
		$('#msg_'+group_id).toggle('fast');		
		
		var partialUrl=addActionToURL('messageActions.do');
		var url=getUrl(partialUrl,group_id);
		var async=new Asynchronous();
		async.complete=makeRead;
		async.call(url);
	}
	
	
	function addActionToURL(actionName){
		var fullURL=document.URL;
		var lastSlash=fullURL.lastIndexOf("/");
		var partialURL=fullURL.substring(0,lastSlash);
		return partialURL+"/"+actionName;
	}
	
	function getUrl(url,group_id){				
		var result=url;
		result+='~actionType=makeMsgRead';
		result+='~msgStateId='+group_id;
		return result;
	}
	
	function makeRead (status, statusText, responseText, responseXML){		
		var root=responseXML.getElementsByTagName('Messaging')[0].childNodes[0];
		var stateId=root.getAttribute('id');
		var isRead=root.getAttribute('read');
		if(isRead){
			var myid='#'+stateId+'_unreadLink';		
			$(myid).css("color","");		
		}
		
		//changing tr's backgroung color from white. white background have only unread messages
		var tbl=document.getElementById('msgsList');			
		var img=document.getElementById(stateId+'_plus');
		var imgTD=img.parentNode;
		var msgTR=imgTD.parentNode;
		msgTR.style.backgroundColor='#eeeeee';
		
	}
	
	function getMessages(){
		var url=addActionToURL('messageActions.do?actionType=viewAllMessages');			
		var async=new Asynchronous();
		async.complete=buildMessagesList;
		async.call(url);
	}
	
	
	function buildMessagesList (status, statusText, responseText, responseXML){
		//alert('aaa');
		msgsXml=responseXML;		
		var tbl=document.getElementById('msgsList');
		tbl.border='0';
		tbl.cellPadding="1";
		tbl.cellSpacing="1";
		tbl.width="100%";
		
		
		var root=responseXML.getElementsByTagName('Messaging')[0];
		if(root!=null){
			messages=root.childNodes;
			if((messages==null || messages.length==0) && firstEntry==0){
				var newTR=document.createElement('TR');
				newTR.innerHTML='<td colspan="4">'+noMsgs+'</td>';
                                var tableBody= tbl.getElementsByTagName("tbody");
                                tableBody[0].appendChild(myTR);
				firstEntry++;
				return;
			}else{
				if(myArray!=null && myArray.length>0){
					var whereToInsertRow=1;
					//if(messages.length>myArray.length){
						for(var i=0;i<messages.length;i++){
						var msgId=messages[i].getAttribute('id');
						for(var j=0;j<myArray.length;j++){
							if(msgId==myArray[j]){
								break;
							}else{
								if(j==myArray.length-1){									
									tbl.tBodies[0].insertRow(whereToInsertRow);
									var msgTR=tbl.rows[whereToInsertRow];
									msgTR.className = 'my-border-style';									
									msgTR.style.backgroundColor='#ffffff';
									createTableRow(tbl,msgTR,messages[i]);
									myArray[myArray.length]=msgId;
									whereToInsertRow++;
								}							
							}
						}
					//}	
					}		
				}else {
					for(var i=0;i<messages.length;i++){				
						var msgId=messages[i].getAttribute('id');
						myArray[i]=msgId;
						
						//creating tr
						var msgTr=document.createElement('TR');	
						var isMsgRead=messages[i].getAttribute('read');					
						if(isMsgRead=='false'){
							msgTr.style.backgroundColor='#ffffff';							
						}else{
							msgTr.style.backgroundColor='#eeeeee';							
						}
						msgTr.height='20px';
							
						var myTR=createTableRow(tbl,msgTr,messages[i]);
                                                var tablBody= tbl.getElementsByTagName("tbody");
                                                tablBody[0].appendChild(myTR);
                        
						myTR.className = 'my-border-style';													
					}//end of for loop
				}			
			}
		}			
			
	}	
	
	//creates table rows with message information
	function createTableRow(tbl,msgTr,message){
	
		var msgId=message.getAttribute('id');	
		//create image's td
		var imgTD=document.createElement('TD');
		imgTD.vAlign='top';				
		imgTD.innerHTML='<img id="'+msgId+'_plus"  onclick="toggleGroup('+msgId+')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>'+
				'<img id="'+msgId+'_minus"  onclick="toggleGroup('+msgId+')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display : none"/>';
		msgTr.appendChild(imgTD);
					
				
		//message name and description
		var nameTD=document.createElement('TD');				
		var msgName=message.getAttribute('name');				
		nameTD.width='60%';
		
		//creating visible div for message name
		var nameDiv=document.createElement('DIV');
		nameDiv.setAttribute("id",msgId+'_dots');		
		var sp=document.createElement('SPAN');
		var isMsgRead=message.getAttribute('read');
		if(isMsgRead=='false'){
			sp.innerHTML='<A id="'+msgId+'_unreadLink" href="javascript:viewMessage('+msgId+')"; style="color:red" >'+msgName+'</A>';
		}else {
			sp.innerHTML='<A id="'+msgId+'_unreadLink" href="javascript:viewMessage('+msgId+')";>'+msgName+'</A>';
		}
		nameDiv.appendChild(sp);
		nameTD.appendChild(nameDiv);
					
		//creating hidden div for message description.It'll become visible after user clicks on twistie
		var descDiv=document.createElement('DIV');
		descDiv.setAttribute("id",'msg_'+msgId);	
		descDiv.style.display='none';
		//creating table inside hidden div
			var divTable=document.createElement('TABLE');
                        var divTblBody=document.createElement('TBODY');
                        divTable.appendChild(divTblBody);
			divTable.width='100%';
				var fromTR=document.createElement('TR');
					var fromTD1=document.createElement('TD');
					fromTD1.innerHTML='<strong>'+from+'</strong>';
				fromTR.appendChild(fromTD1);
					//getting sender
					var fromTD2=document.createElement('TD');
					var msgSender=message.getAttribute('from');
					fromTD2.innerHTML=msgSender;
				fromTR.appendChild(fromTD2);
			divTblBody.appendChild(fromTR);
				var receivedTR=document.createElement('TR');
					var receivedTD1=document.createElement('TD');
					receivedTD1.innerHTML='<strong>'+received+'</strong>';							
				receivedTR.appendChild(receivedTD1);
							
					//getting received date
					var receivedTD2=document.createElement('TD');
					var received=message.getAttribute('received');
					receivedTD2.innerHTML=received;
				receivedTR.appendChild(receivedTD2);
			divTblBody.appendChild(receivedTR);						
				var priorityTR=document.createElement('TR');
					var priorityTD1=document.createElement('TD');
					priorityTD1.innerHTML='&nbsp;'+prLevel;
				priorityTR.appendChild(priorityTD1);
								
					var priorityTD2=document.createElement('TD');
					//getting priority level
					var priority=message.getAttribute('priority');
					if(priority==1){priorityTD2.innerHTML='low';}
					else if(priority==2){priorityTD2.innerHTML='Medium';}
					else if(priority==3){priorityTD2.innerHTML='Critical';}
					else if(priority==-1){priorityTD2.innerHTML='None';}
				priorityTR.appendChild(priorityTD2);
			divTblBody.appendChild(priorityTR);	
				var detailsTR=document.createElement('TR');
					var detailsTD1=document.createElement('TD');
					detailsTD1.innerHTML='&nbsp;'+description;
				detailsTR.appendChild(detailsTD1);
					
					var detailsTD2=document.createElement('TD');
					//getting description
					var description=message.getAttribute('msgDetails');
					detailsTD2.innerHTML=description;
				detailsTR.appendChild(detailsTD2);
			divTblBody.appendChild(detailsTR);
		descDiv.appendChild(divTable);	
		nameTD.appendChild(descDiv);
		msgTr.appendChild(nameTD);				
						
		// forward or edit link
		fwdOrEditTD=document.createElement('TD');
		fwdOrEditTD.width='20%';
		fwdOrEditTD.align='right';
		var isDraft=message.getAttribute('isDraft');
		if(isDraft=='true'){
			fwdOrEditTD.innerHTML='<digi:link href="/messageActions.do?actionType=fillTypesAndLevels&editingMessage=true&msgStateId='+msgId+'">'+editBtn+'</digi:link>';									
		}else{
			fwdOrEditTD.innerHTML='<digi:link href="/messageActions.do?actionType=forwardMessage&fwd=fillForm&msgStateId='+msgId+'">'+fwdBtn+'</digi:link>';
		}
		msgTr.appendChild(fwdOrEditTD);	
					
		//delete link
		var deleteTD=document.createElement('TD');
		deleteTD.width='20%';
		deleteTD.align='center';
		//deleteTD.innerHTML='<digi:link href="/messageActions.do?editingMessage=false&actionType=removeSelectedMessage&msgStateId='+msgId+'">'+deleteBtn+'</digi:link>';
		deleteTD.innerHTML='<a href="javascript:deleteMessage('+msgId+')">'+deleteBtn+'</a>';
		msgTr.appendChild(deleteTD);	
					
		return msgTr;			
	
	}
	
</script>


	<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">
		<TR>
			<TD class=r-dotted-lg-buttom vAlign=top>
				<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
	        		<TR><TD>
	              	<TABLE border=0 cellPadding=0 cellSpacing=0 >
	              		<TR bgColor=#f4f4f2>
	                 		<TD bgColor=#c9c9c7 class=box-title	title='<digi:trn key="message:messagesAssosiatedWithTeam">List of Messages associated with Team</digi:trn>'>
								<c:if test="${messageForm.tabIndex==1}">
									<digi:trn key="message:Messages">Messages</digi:trn>							
								</c:if> 
								<c:if test="${messageForm.tabIndex!=1}">
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&page=1">
	                 					<digi:trn key="message:Messages">Messages</digi:trn>
	                 				</a>
								</c:if>							
							</TD>
	                    	<TD background="module/aim/images/corner-r.gif"	height=17 width=17></TD>
	                    	<TD bgColor=#c9c9c7 class=box-title	title='<digi:trn key="message:alertsAssosiatedWithTeam">List of Alerts associated with Team</digi:trn>'>
								<c:if test="${messageForm.tabIndex==2}">
									<digi:trn key="message:Alerts">Alerts</digi:trn>							
								</c:if>
								<c:if test="${messageForm.tabIndex!=2}">
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2&page=1">
										<digi:trn key="message:Alerts">Alerts</digi:trn>
									</a>							
								</c:if>
							</TD>
	                    	<TD background="module/aim/images/corner-r.gif"	height=17 width=17></TD>
	                    	<TD bgColor=#c9c9c7 class=box-title	title='<digi:trn key="message:approvalsAssosiatedWithTeam">List of Approvals associated with Team</digi:trn>'>
								<c:if test="${messageForm.tabIndex==3}">
									<digi:trn key="message:approvals">Approvals</digi:trn>
								</c:if>
								<c:if test="${messageForm.tabIndex!=3}">
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=3&page=1">
										<digi:trn key="message:approvals">Approvals</digi:trn>
									</a>
								</c:if>
							</TD>
	                    	<TD background="module/aim/images/corner-r.gif"	height=17 width=17></TD>
	                    	<TD bgColor=#c9c9c7 class=box-title	title='<digi:trn key="message:eventsAssosiatedWithTeam">List of Events associated with Team</digi:trn>'>
								<c:if test="${messageForm.tabIndex==4}">
									<digi:trn key="message:ebents">Calendar Events</digi:trn>
								</c:if>
								<c:if test="${messageForm.tabIndex!=4}">
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=4&page=1">
										<digi:trn key="message:ebents">Calendar Events</digi:trn>
									</a>
								</c:if>							
							</TD>
	                    	<TD background="module/aim/images/corner-r.gif"	height=17 width=17></TD>
							</TR>
						</TABLE>
					</TD></TR>
					<c:if test="${messageForm.tabIndex!=3 && messageForm.tabIndex!=4}">
						<TR >
							<TD width="100%">
								<table align="center" width="30%" cellpadding="5" cellspacing="5">
									<tr >
										<td width="6%"></td>
										<td width="38%" align="right">
											<c:if test="${messageForm.childTab=='inbox'}">
												<digi:trn key="message:inbox">Inbox</digi:trn>
											</c:if>
											<c:if test="${empty messageForm.childTab || messageForm.childTab!='inbox'}">
												<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=inbox&tabIndex=${messageForm.tabIndex}&page=1">
													<digi:trn key="message:inbox">Inbox</digi:trn>
												</a>
											</c:if>
										</td>
										<td width="2%"> |</td>
										<td width="14%" align="center">
											<c:if test="${messageForm.childTab=='sent'}">
												<digi:trn key="message:sent">Sent</digi:trn>
											</c:if>
											<c:if test="${empty messageForm.childTab || messageForm.childTab!='sent'}">
												<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=${messageForm.tabIndex}&page=1">
													<digi:trn key="message:sent">Sent</digi:trn>
												</a>
											</c:if>
										</td>
										<td width="2%"> |</td>
										<td width="38%" align="left">
											<c:if test="${messageForm.childTab=='draft'}">
												<digi:trn key="message:draft">Draft</digi:trn>
											</c:if>
											<c:if test="${empty messageForm.childTab || messageForm.childTab!='draft'}">
												<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=${messageForm.tabIndex}&page=1">
													<digi:trn key="message:draft">Draft</digi:trn>
												</a>
											</c:if>
										</td>
									</tr>
								</table>
							</TD>					
						</TR>
					</c:if>
												
						<TR>
							<TD bgColor="#ffffff" class="box-border" align="left" >
								<TABLE id="msgsList">
									<TR><TD colspan="4"></TD></TR>			
								</TABLE>
							</TD>
						</TR><!-- 
								<logic:notEmpty name="messageForm" property="pagedMessagesForTm">
							<TR><TD> 
								<digi:trn key="message:pages">Pages</digi:trn>:
								<c:if test="${messageForm.page>1}">
									<c:set var="trn">
										<digi:trn key="message:firstPage">click here to go to first page</digi:trn>
									</c:set>
									<a href="javascript:goToPage('1')" title="${trn}" >&lt;&lt;</a>
									
									<c:set var="trn">
										<digi:trn key="message:previousPage">click here to go to previous page</digi:trn>
									</c:set>
									<a href="javascript:goToPage(${messageForm.page-1})" title="${trn}" > &lt; </a>
									
								</c:if>
								&nbsp;
								<c:if test="${not empty messageForm.allPages}">
									
									<c:set var="length" value="${messageForm.pagesToShow}"></c:set>
									<c:set var="start" value="${messageForm.offset}"/>								
									
									<logic:iterate id="pg" name="messageForm" property="allPages" offset="${start}" length="${length}">
										<c:if test="${pg==messageForm.page}"><font color="red">${pg}</font></c:if>
										<c:if test="${pg!=messageForm.page}">
											<c:set var="translation">
												<digi:trn key="aim:clickToGoToNext">Click here to go to next page</digi:trn>
											</c:set>
											<a href="javascript:goToPage(${pg})" title="${translation}" >${pg}</a> 
										</c:if> |&nbsp;
									</logic:iterate>
								</c:if>
								<c:if test="${messageForm.page<messageForm.lastPage}">
									<c:set var="trn">
										<digi:trn key="message:previousPage">click here to go to next page</digi:trn>
									</c:set>
									<a href="javascript:goToPage(${messageForm.page+1})" title="${trn}" > &gt; </a>
									
									<c:set var="trn">
										<digi:trn key="message:firstPage">click here to go to last page</digi:trn>
									</c:set>
									<a href="javascript:goToPage(${messageForm.lastPage})" title="${trn}" >&gt;&gt;</a>							
								</c:if>
								&nbsp; ${messageForm.page}of ${messageForm.lastPage}
							</TD></TR>					
						</logic:notEmpty>					
						 -->						
				</TABLE>				
			</TD>
		</TR>
	</TABLE>
</digi:form>
