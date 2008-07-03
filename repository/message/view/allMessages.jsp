<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>

<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/amptabs.css"/>"/>

<!-- Yahoo Panel --> 
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"/>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>"></script>

<style>
<!--

#selectedMessagePanel .bd { 
    height: 500px; 
    /* Apply scrollbars for all browsers. */ 
    overflow: auto; 
} 
.yui-panel-container.hide-scrollbars #selectedMessagePanel .bd { 
	    /* Hide scrollbars by default for Gecko on OS X */ 
	    overflow: hidden; 
	} 
	
	.yui-panel-container.show-scrollbars #selectedMessagePanel .bd { 
	    /* Show scrollbars for Gecko on OS X when the Panel is visible  */ 
	    overflow: auto; 
} 
	


.my-border-style {
      border-top: 1px solid  #f4f4f2;
}
.trOdd {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.trEven{
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
.contentbox_border{
        border: 1px solid black;
	border-width: 0px 1px 1px 1px; 
	background-color: #f4f4f2;
}

.Hovered {
	background-color:#a5bcf2;
}

.userMsg{
background-color:yellow;
}

-->
</style>

<digi:instance property="messageForm"/>
<digi:form action="/messageActions.do">
<html:hidden name="messageForm" property="msgRefreshTimeCurr"/>
<html:hidden name="messageForm" property="tabIndex"/>
<html:hidden name="messageForm" property="childTab"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/script/messages.js"/>"></script>
<script langauage="JavaScript">
    
        var  selectedMessagePanel;
	var noMsgs='<digi:trn key="message:noMessages">No Messages Present</digi:trn>';
        var noAlerts='<digi:trn key="message:noAlerts">No Alerts Present</digi:trn>';
        var noApprovals='<digi:trn key="message:noPendingApprovals">No Pending Approvals</digi:trn>';
        var noEvents='<digi:trn key="message:noUpcomingEvents">No Upcoming Events</digi:trn>';
	var from='<digi:trn key="message:from">From</digi:trn>';
        var to='<digi:trn key="message:to">To</digi:trn>';
	var date='<digi:trn key="message:date">Date</digi:trn>';
	var prLevel='<digi:trn key="message:priority">Priority</digi:trn>';
	var desc='<digi:trn key="message:msgDetails">Message Details</digi:trn>';
	var editBtn='<digi:trn key="message:Edit">Edit</digi:trn>';
	var fwdBtn='<digi:trn key="message:Forward">Forward</digi:trn>';
	var deleteBtn='<digi:trn key="message:delete">Delete</digi:trn>';
	var pagesTrn='<digi:trn key="message:pages">Pages</digi:trn>';
	var firstPage='<digi:trn key="message:firstPage">click here to go to first page</digi:trn>';
	var prevPage='<digi:trn key="message:previousPage">click here to go to previous page</digi:trn>';
	var nextPage='<digi:trn key="aim:clickToGoToNext">Click here to go to next page</digi:trn>';
	var lastPg='<digi:trn key="message:firstPage">click here to go to last page</digi:trn>';
	var referenceURL='<digi:trn key="message:referenceURL">Reference URL</digi:trn>';
        var forwardClick="<digi:trn key="message:ClickForwardMessage"> Click on this icon to forward message&nbsp;</digi:trn>";
        var editClick="<digi:trn key="message:ClickEditMessage"> Click on this icon to edit message&nbsp;</digi:trn>";
        var deleteClick="<digi:trn key="message:ClickDeleteMessage"> Click on this icon to delete message&nbsp;</digi:trn>";
        var viewMessage="<digi:trn key="message:ClickViewMessage"> Click here to view the message</digi:trn>";
	//used to define whether we just entered page from desktop
	var firstEntry=0;
	var currentPage=1;
        var messages;
	var slMsgId;
	//used to hold already rendered messages
	var myArray=new Array();
	
	window.onload=getMessages;
		
	//setting timer to check for new messages after specified time	
	if(document.getElementsByName('msgRefreshTimeCurr')[0].value>0){
		id=window.setTimeout("checkForNewMessages()",60000*document.getElementsByName('msgRefreshTimeCurr')[0].value,"JavaScript");
	}

    function hoverTr(id, obj){
    	
    	if(slMsgId!=id){
         obj.className='Hovered';
        }   
    }
    
    function closeWindow() {	
       selectedMessagePanel.destroy();
    }
    
    /*code below doesn't look good... but still
     *  its attachs events to rows: mouse over(makes row color darker) 
     *  and mouse out(returns to row it basic color)
     */

    function paintTr(msgTR,i){
    
        var className='';
        if(i!=1&&i%2==0){
            msgTR.className = 'trEven';
            className="this.className='trEven'";                                                            
        }
        else{
            msgTR.className = 'trOdd';
            className="this.className='trOdd'";
        }        
        
        var setBGColor = new Function(className);
       
        /*msgTR.onmouseover=hoverTr;*/
        msgTR.onmouseout=setBGColor;
      
       
        return msgTR;
       
    }
    
    


	 function checkForNewMessages(){
		var url=addActionToURL('messageActions.do?actionType=viewAllMessages&page='+currentPage);			
		var async=new Asynchronous();
		async.complete=buildMessagesList;
		async.call(url);
		id=window.setTimeout("checkForNewMessages()",60000*document.getElementsByName('msgRefreshTimeCurr')[0].value,"JavaScript");
	}
        
    function loadSelectedMessage(id){
        /* 
         * some messages need long time to load, 
         * that is why we create blank panel here, so user will see blank panel
         * before function call is completed
         */
        
        //create div to hold selected message
        var div=document.createElement('DIV');
        div.id="selectedMessagePanel";
        document.body.appendChild(div);
        
        // create body div to hold selected message
        var divBody=document.createElement('DIV');
        divBody.className="bd";
        divBody.id="msg_bd";
        divBody.innerHTML='';
        div.appendChild(divBody);
        selectedMessagePanel=new YAHOO.widget.Panel("selectedMessagePanel",{
            width:"600px", 
            height:"510px",
            fixedcenter: true, 
            constraintoviewport: true, 
            underlay:"shadow", 
            modal: true,
            close:true, 
            visible:true, 
            draggable:true} );
        selectedMessagePanel.render();
        var url;
        var ind=id.indexOf('_fId');
        if(ind!=-1){
            var msgId=id.substring(0,ind);
            url=addActionToURL('messageActions.do?actionType=viewSelectedMessage&msgId='+msgId); 
        }
        else{
            url=addActionToURL('messageActions.do?actionType=viewSelectedMessage&msgStateId='+id);  
        }			
        var async=new Asynchronous();
        async.complete=viewMsg;
        async.call(url);
        
    }
	
	function openObjectURL(url){
            window.open(url,'','channelmode=no,directories=no,menubar=yes,resizable=yes,status=yes,toolbar=yes,scrollbars=yes,location=yes');
            //openURLinWindow(url,600,550);
	
	}
	
    function viewMsg(status, statusText, responseText, responseXML) {
        var divBody=document.getElementById("msg_bd");
        divBody.setAttribute("visibility","visible");
        divBody.innerHTML=responseText;
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
				tbl.tBodies[0].removeChild(msgTR);
                                
                                /* 
                                 * after we delete row we need to repaint remain rows
                                 *  its also reattachs events to rows: mouse over(makes row color darker) 
                                 *  and mouse out(returns to row it basic (new) color)
                                 */
                                var trs=tbl.tBodies[0].rows;
                                
                                for(var i=1;i<trs.length;i++){
                                
                                var className='';
                                if(i!=1&&i%2==0){
                                    trs[i].className = 'trOdd';
                                    className="this.className='trOdd'";
                                                                                               
                                }
                                else{
                                       trs[i].className='trEven';
                                       className="this.className='trEven'";
                                    }
                                       var setBGColor = new Function(className);
                                       trs[i].onmouseout=setBGColor;
                                }
                                
                              
				//removing record from db
				var url=addActionToURL('messageActions.do');	
				url+='~actionType=removeSelectedMessage';
				url+='~editingMessage=false';
				url+='~msgStateId='+msgId;
				url+='~page='+currentPage;			
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
		if(myArray!=null && myArray.length>0){
			myArray.splice(0,myArray.length);
		}
		//creating table
		var tbl=document.getElementById('msgsList');
		var tblBody=tbl.getElementsByTagName('tbody')[0];	
		if(tblBody.childNodes!=null && tblBody.childNodes.length>0){
			while (tblBody.childNodes.length>0){
				tblBody.removeChild(tblBody.childNodes[0]);
			}
		}				
		currentPage=page;
		getMessages();		
	}
        
        function toggleGr(id){
            $('#'+id+'_minus'+':visible').slideUp('fast');
            $('#'+id+'_plus'+':hidden').slideDown('fast');
            $('#msg_'+id+':visible').slideUp('fast');
        }
        
   
        
    function toggleGroup(group_id){
    
    
    if(group_id==slMsgId){
    	slMsgId='';
    	
    }else{
    		slMsgId=group_id;
    		 
    		 var ind=group_id.indexOf('_fId');
        	 var stateId;
    	
    			if(ind!=-1){
    				stateId = group_id.slice(ind+4);
    				slMsgId = stateId;
    			
    	}
    }
    
                var strId='#'+group_id;
                $(strId+'_minus').toggle();
                $(strId+'_plus').toggle();
        var ind=group_id.indexOf('_fId');
        var stateId;
       
        if(ind!=-1){
            ind=group_id.indexOf('_fId');
            stateId=group_id.slice(ind+4);   
        }
        else{
            stateId=group_id;
            markMsgeAsRead( stateId);
          
        }
       
        for(var j=0;j<messages.length;j++){ 
            var stId= messages[j].getAttribute('id');
            if(stateId!=stId){
                toggleGr(stId);
            }  
            for(var i=0;i<messages[j].childNodes.length;i++){
                var forwardedMsg=messages[j].childNodes[i];
                var parStId=forwardedMsg.getAttribute('parentStateId');
                if(stateId!=parStId){
                    var lastMsgId=forwardedMsg.getAttribute('msgId');
                    var fId=lastMsgId+'_fId'+parStId;
                    toggleGr(fId);
                }
                
            }
            
        }
                $('#msg_'+group_id).toggle('fast');
                
	}
                  
	function markMsgeAsRead(group_id){
        var partialUrl=addActionToURL('messageActions.do');
		var url=getUrl(partialUrl,group_id);
		var async=new Asynchronous();
		async.complete=makeRead;
		async.call(url);                
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
		
	}
	
	function getMessages(){
		var url=addActionToURL('messageActions.do?actionType=viewAllMessages&page='+currentPage);			
		var async=new Asynchronous();
		async.complete=buildMessagesList;
		async.call(url);
	}
	
	
	function buildMessagesList (status, statusText, responseText, responseXML){		
		msgsXml=responseXML;		
		var tbl=document.getElementById('msgsList');
		tbl.border='0';
		tbl.cellPadding="1";
		tbl.cellSpacing="1";
		tbl.width="100%";
				
		var mainTag=responseXML.getElementsByTagName('Messaging')[0];
		if(mainTag!=null){
			var paginationTag=mainTag.getElementsByTagName('Pagination')[0];
			//messages start	
			var root=mainTag.getElementsByTagName('MessagesList')[0];
			if(root!=null){
                           if(!root.hasChildNodes()&& firstEntry==0){
                               var newTR=document.createElement('TR');
                                var newTD=document.createElement('TD');
                                switch (document.messageForm.tabIndex.value){
                                case "2":
                                    newTD.innerHTML=noAlerts;
                                    break;
                                case "3":
                                    newTD.innerHTML=noApprovals;
                                    break;
                                case "4":
                                    newTD.innerHTML=noEvents;
                                    break;
                        
                                default :newTD.innerHTML=noMsgs;
                                }
                                
                                newTD.colSpan=4;
                                newTR.appendChild(newTD);
                                newTR.id="noMsg";
                                var tableBody= tbl.getElementsByTagName("tbody");
                                tableBody[0].appendChild(newTR);
                                firstEntry++;
                                return;
                               
                            }
				else{
                                     messages=root.childNodes;
                                    
                                     
					//var tblBody=tbl.getElementsByTagName('tbody')[0];
					//while (tblBody.childNodes.length>0){
					//	tblBody.removeChild(tblBody.childNodes[0]);
					//}
					if(myArray!=null && myArray.length>0){
						var whereToInsertRow=1;						
							for(var i=0;i<messages.length;i++){
							var msgId=messages[i].getAttribute('id');
                                                       
							for(var j=0;j<myArray.length;j++){
								if(msgId==myArray[j]){
									break;
								}else{
									if(j==myArray.length-1){
										if(paginationTag!=null){
											var pagParams=paginationTag.childNodes[0];
											var wasDelteActionCalled=pagParams.getAttribute('deleteWasCalled');
											if(wasDelteActionCalled=='true'){
												var msgTR=document.createElement('TR');
												var browser=navigator.appName;
												
												if(browser=="Microsoft Internet Explorer"){
							
										   				var msgTR=document.createElement('<tr onmouseover="hoverTr('+msgId+',this);" onmouseout="paintTr(this,'+i+');"></tr>');
										   				msgTr=paintTr(msgTr,i);
										   				
													}else{
										            
										                msgTR=paintTr(msgTR,i);
                                                        msgTR.setAttribute('onmouseover','hoverTr('+msgId+',this)');
                                               		}
                                               		                                              
												tbl.tBodies[0].appendChild(createTableRow(tbl,msgTR,messages[i],true));
												myArray[myArray.length]=msgId;										
											}else{
												tbl.tBodies[0].insertRow(whereToInsertRow);
												var msgTR=tbl.tBodies[0].rows[whereToInsertRow];
												var browser=navigator.appName;
												
												if(browser=="Microsoft Internet Explorer"){
							
										   				var msgTR=document.createElement('<tr onmouseover="hoverTr('+msgId+',this);" onmouseout="paintTr(this,'+i+');"></tr>');
														msgTr=paintTr(msgTr,i);	
							
												}else{
                                                        msgTR=paintTr(msgTR,i);
                                                        msgTR.setAttribute('onmouseover','hoverTr('+msgId+',this)');
												}
								
												createTableRow(tbl,msgTR,messages[i],true);
												myArray[myArray.length]=msgId;
												whereToInsertRow++;										
												tbl.tBodies[0].removeChild(tbl.tBodies[0].lastChild);
											}
										}		
										
									}							
								}
							}
						}		
					}else {
						for(var i=0;i<messages.length;i++){				
							var msgId=messages[i].getAttribute('id');
							myArray[i]=msgId;
							
							//creating tr
							var msgTr=document.createElement('TR');	
							var isMsgRead=messages[i].getAttribute('read');					
                            var browser=navigator.appName;
												
							if(browser=="Microsoft Internet Explorer"){
								   		var msgTr=document.createElement('<tr onmouseover="hoverTr('+msgId+',this);" onmouseout="paintTr(this,'+i+');"></tr>');
										msgTr=paintTr(msgTr,i);
										
							
								}else{
                                                       
                                        msgTr=paintTr(msgTr,i);
                      					msgTr.setAttribute('onmouseover','hoverTr('+msgId+',this)');
                        	}
								
							var myTR=createTableRow(tbl,msgTr,messages[i],true);													
                                                var tablBody= tbl.getElementsByTagName("tbody");
                                                tablBody[0].appendChild(myTR);
                                                
                      			 
																				
						}//end of for loop
					}			
				}//messages end
				
				//pagination start
			
				if(paginationTag!=null){
					var paginationParams=paginationTag.childNodes[0];
					var doMsgsExist=paginationParams.getAttribute('messagesExist');	
					if(doMsgsExist=='true'){
						var page=paginationParams.getAttribute('page');
						var allPages=paginationParams.getAttribute('allPages');
						var lastPage=paginationParams.getAttribute('lastPage');
						setupPagionation(paginationTag,parseInt(page),parseInt(allPages));
					}				
				}
				//pagination end
			}
	}	
	
	function setupPagionation (paginationTag,page,allPages){
		currentPage=page;
		var paginationTR=document.getElementById('paginationPlace');
		while(paginationTR.firstChild != null){
			paginationTR.removeChild(paginationTR.firstChild);
		}
		var paginationTag=mainTag.getElementsByTagName('Pagination')[0];
			var paginationParams=paginationTag.childNodes[0];
			if(paginationParams!=null){
				var paginationTD=document.createElement('TD');
				var paginationTDContent=pagesTrn+':';
					if(currentPage>1){
						var prPage=currentPage-1;
						paginationTDContent+=':<a href="javascript:goToPage(1)" title="'+firstPage+'">&lt;&lt; </a> ';
						paginationTDContent+='<a href="javascript:goToPage('+prPage+')" title="'+prevPage+'" > &lt; </a>';								
					}
					paginationTDContent+='&nbsp';
					if(allPages!=null){
						var fromIndex=1;
						if((currentPage-2)<1){
							fromIndex=1;
						}else{
							fromIndex=currentPage-2;
						}
						var toIndex;
						if(currentPage+2>allPages){
							toIndex=lastPage;
						}else{
							toIndex=currentPage+2;
						}
						for(var i=fromIndex;i<=toIndex;i++){
							if(i<=allPages && i==page) {paginationTDContent+='<font color="red">'+i+'</font>|&nbsp;';}
							if(i<=allPages && i!=page) {paginationTDContent+='<a href="javascript:goToPage('+i+')" title="'+nextPage+'">'+i+'</a>|&nbsp;'; }
						}
					}
					if(page<lastPage){
						var nextPg=page+1;									
						paginationTDContent+='<a href="javascript:goToPage('+nextPg+')" title="'+nextPage+'">&gt;</a>';
						paginationTDContent+='<a href="javascript:goToPage('+lastPage+')" title="'+lastPg+'">&gt;&gt;</a>|';
					}	
					paginationTDContent+='&nbsp;'+page+'of'+lastPage;
				paginationTD.innerHTML=	paginationTDContent;						
				paginationTR.appendChild(paginationTD);						
			}
		}				
	}
	
	//creates table rows with message information
	function createTableRow(tbl,msgTr,message,fwdOrEditDel){
	    
		var msgId=message.getAttribute('id');	//message state id
                var messageId=message.getAttribute('msgId');
                var sateId;
                if(msgId==null){
                    msgId=messageId;
                }
                else{
                    sateId=msgId;
                }
                var newMsgId=message.getAttribute('parentStateId');// id of the hierarchy end for forwarding messages
		//create image's td
		var imgTD=document.createElement('TD');
               
                if(newMsgId!=null){
                    msgId+='_fId'+newMsgId; // create id for forwarded message
    
                }
               
		imgTD.vAlign='top';	
               
                    imgTD.innerHTML='<img id="'+msgId+'_plus"  onclick="toggleGroup(\''+msgId+'\')" src="/repository/message/view/images/unread.gif" title="<digi:trn key="message:ClickExpandMessage">Click on this icon to expand message&nbsp;</digi:trn>"/>'+
				'<img id="'+msgId+'_minus"  onclick="toggleGroup(\''+msgId+'\')" src="/repository/message/view/images/read.gif" style="display : none" <digi:trn key="message:ClickCollapseMessage"> Click on this icon to collapse message&nbsp;</digi:trn>/>';
                    msgTr.appendChild(imgTD);
                
               
					
		//message name and description
		var nameTD=document.createElement('TD');				
		var msgName; 
                 if(message.childNodes!=null&&message.childNodes.length>0){
                     msgName="FW: "+message.getAttribute('name');
                 }
                 else{
                      msgName=message.getAttribute('name');
                 }
                if(fwdOrEditDel){
                    nameTD.width='80%';}
                else{
                    nameTD.width='95%';
                }
                   
		//creating visible div for message name
		var nameDiv=document.createElement('DIV');
                var visId;
                 if(!fwdOrEditDel){
                    visId=newMsgId+'_'+msgId+'_dots'
                }
                else{
                   visId=+msgId+'_dots' 
                }
                
		nameDiv.setAttribute("id",visId);	
		var sp=document.createElement('SPAN');
		var isMsgRead=message.getAttribute('read');
		if(isMsgRead=='false'){
			sp.innerHTML='<A id="'+msgId+'_unreadLink" href="javascript:loadSelectedMessage(\''+msgId+'\')"; style="color:red;" title="'+viewMessage+'">'+msgName+'</A>';   
		}else {
			sp.innerHTML='<A id="'+msgId+'_unreadLink" href="javascript:loadSelectedMessage(\''+msgId+'\')"; title="'+viewMessage+'">'+msgName+'</A>';
		}
		nameDiv.appendChild(sp);
		nameTD.appendChild(nameDiv);
					
		//creating hidden div for message description.It'll become visible after user clicks on twistie
		var descDiv=document.createElement('DIV');
                var invId='msg_'+msgId;
		descDiv.setAttribute("id",invId);	
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
                                        if(fromTD2.textContent==undefined){
                                            fromTD2.innerText=msgSender;
                                        }
                                        else{
                                            fromTD2.textContent=msgSender;
                                        }
				fromTR.appendChild(fromTD2);
			divTblBody.appendChild(fromTR);
                        
                                var toTR=document.createElement('TR');
                                var toTD1=document.createElement('TD');
                                toTD1.innerHTML='<strong>'+to+'</strong>';
                                toTR.appendChild(toTD1);
                                //getting receives
                                var toTD2=document.createElement('TD');
                                var msgReceiver=message.getAttribute('to');
                                var receiver_array=msgReceiver.split(",");
                                if(receiver_array.length>5){
                                 msgReceiver="";
                                 for(var j=0;j<5;j++){
                                    msgReceiver+=receiver_array[j];
                                    if(j!=4){
                                        msgReceiver+=",";
                                    }
                                    else{
                                         msgReceiver+="......";
                                    }
                                 }
                                 
                                }
                                if(toTD2.textContent==undefined){
                                     toTD2.innerText=msgReceiver;
                                }
                                else{
                                    toTD2.textContent=msgReceiver;
                                }
                                  
                               
                                toTR.appendChild(toTD2);
                                divTblBody.appendChild(toTR);
                        
				var receivedTR=document.createElement('TR');
					var receivedTD1=document.createElement('TD');
					receivedTD1.innerHTML='<strong>'+date+'</strong>';							
				receivedTR.appendChild(receivedTD1);
							
					//getting received date
					var receivedTD2=document.createElement('TD');
					var received=message.getAttribute('received');
					receivedTD2.innerHTML=received;
				receivedTR.appendChild(receivedTD2);
			divTblBody.appendChild(receivedTR);						
				var priorityTR=document.createElement('TR');
					var priorityTD1=document.createElement('TD');
					priorityTD1.innerHTML='<strong>'+prLevel+'</strong>';
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
				
					//getting URL
					var objectURL=message.getAttribute('objURL');
                                        if(objectURL!='null'){
				var objURLTR=document.createElement('TR');
					var objURLTD1=document.createElement('TD');
                                        objURLTD1.innerHTML='<strong>'+referenceURL+'</strong>';
				objURLTR.appendChild(objURLTD1);
					var objURLTD2=document.createElement('TD');
                        	objURLTD2.innerHTML='<A href="javascript:openObjectURL(\''+objectURL+'\')";> '+'click here to view details</A>';

				objURLTR.appendChild(objURLTD2);
			divTblBody.appendChild(objURLTR);
                                    }	
				
				var detailsTR=document.createElement('TR');
					var detailsTD1=document.createElement('TD');
                                        detailsTD1.width='30%';
					detailsTD1.innerHTML='<strong>'+desc+'</strong>';
				detailsTR.appendChild(detailsTD1);
					
					var detailsTD2=document.createElement('TD');
					//getting description
					var description=message.getAttribute('msgDetails');
                                        if(description!='null'){
                                            description=description.substring(0,34);
                                            detailsTD2.innerHTML=description+" .........";
                                        }
                                        else{
                                            detailsTD2.innerHTML="&nbsp";
                                        }					
				detailsTR.appendChild(detailsTD2);
			divTblBody.appendChild(detailsTR);
                        
        // create forwarded messages
        if(message.childNodes!=null&&message.childNodes.length>0){
            var forwardedTb=document.createElement('TABLE');
            forwardedTb.width="100%";
            forwardedTb.className="my-border-style";
            var forwardedTbody=document.createElement('TBODY');
            for(var i=0;i<message.childNodes.length;i++){
                var forwardedTR=document.createElement('TR');
                createTableRow(tbl,forwardedTR,message.childNodes[i],false);
                forwardedTbody.appendChild(forwardedTR);
            }
            forwardedTb.appendChild(forwardedTbody);
            var forwardTR=document.createElement('TR');
            var forwardTD=document.createElement('TD');
            forwardTD.setAttribute("colSpan","3");
            
            forwardTD.appendChild(forwardedTb);
            forwardTR.appendChild(forwardTD);
            divTblBody.appendChild(forwardTR);
        }
        descDiv.appendChild(divTable);	
        nameTD.appendChild(descDiv);
        msgTr.appendChild(nameTD);
        
        if(fwdOrEditDel){
		// forward or edit link
		fwdOrEditTD=document.createElement('TD');
		fwdOrEditTD.width='10%';
		fwdOrEditTD.align='center';
                fwdOrEditTD.vAlign="top";
		var isDraft=message.getAttribute('isDraft');
		if(isDraft=='true'){
                    fwdOrEditTD.innerHTML='<digi:link href="/messageActions.do?actionType=fillTypesAndLevels&editingMessage=true&msgStateId='+sateId+'" style="cursor:pointer; text-decoration:underline; color: blue" title="'+editClick+'"><img  src="/repository/message/view/images/edit.gif" border=0 hspace="2" /></digi:link>';									
		}else{
			fwdOrEditTD.innerHTML='<digi:link href="/messageActions.do?actionType=forwardMessage&fwd=fillForm&msgStateId='+sateId+'" style="cursor:pointer; text-decoration:underline; color: blue" title="'+forwardClick+'" ><img  src="/repository/message/view/images/finalForward.gif" border=0  hspace="2" /></digi:link>';
		}
		msgTr.appendChild(fwdOrEditTD);	
					
		//delete link
		var deleteTD=document.createElement('TD');
		deleteTD.width='10%';
		deleteTD.align='center';
                deleteTD.vAlign="top";
		//deleteTD.innerHTML='<digi:link href="/messageActions.do?editingMessage=false&actionType=removeSelectedMessage&msgStateId='+msgId+'">'+deleteBtn+'</digi:link>';
		deleteTD.innerHTML='<a href="javascript:deleteMessage(\''+msgId+'\')" style="cursor:pointer; text-decoration:underline; color: blue" title="'+deleteClick+'" ><img  src="/repository/message/view/images/trash_12.gif" border=0 hspace="2"/></a>';
		msgTr.appendChild(deleteTD);
                }
					
		return msgTr;			
	
	}
    

$(document).ready(function(){
	   $("#displaySettingsButton").toggle(function(){
	     	$("#currentDisplaySettings").show('fast');
	     	$("#show").hide('fast');
	     	$("#hidde").css("background", "#FFFFCC" );
	     	$("#hidde").show('fast');
	   },function(){
	     	$("#currentDisplaySettings").hide('fast');
	     	$("#hidde").hide('fast');
	     	$("#show").css("background", "#CCDBFF" );
	     	$("#show").show('fast');
	   	});
});
        
</script>
<table cellSpacing=0 cellPadding=0 vAlign="top" align="left" width="100%">
<tr>
<td width="100%">
<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />
</td>
</tr>
<tr>
<td>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780 border="0">
    <tr>
   <td width=20>&nbsp;</td>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border="0">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/../aim/showDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:MessageModule">
						Message Module 
						</digi:trn>
                                                &nbsp;&gt;&nbsp;
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
                                                <c:if test="${messageForm.tabIndex!=3 && messageForm.tabIndex!=4}">
                                                &nbsp;&gt;&nbsp;
                                                <c:choose>
                                                 <c:when test="${messageForm.childTab=='inbox'}">
                                                    <digi:trn key="message:inbox">Inbox</digi:trn>
                                                </c:when>
                                                <c:when test="${messageForm.childTab=='sent'}">
                                                  <digi:trn key="message:sent">Sent</digi:trn>
                                                </c:when>
                                                <c:otherwise>
                                                     <digi:trn key="message:draft">Draft</digi:trn>	
                                                </c:otherwise>
                                                </c:choose>
                                                    
                                                </c:if>

                                                </span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
							<digi:trn key="aim:MessageModule ">
								Message Module 
							</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
                            <td noWrap vAlign="top">
				<TABLE cellPadding=0 cellSpacing=0 width="100%"	valign="top" align="left" border="0" >
	        		<TR>
                                    <TD STYLE="width:750">
                                        <DIV id="tabs">
                                            <UL>
                                                
								<c:if test="${messageForm.tabIndex==1}">
                                                    <LI>
                                                        <a name="node">
                                                        <div>
									<digi:trn key="message:Messages">Messages</digi:trn>							
                                                        </div>
                                                        </a>
                                                    </LI>
								</c:if> 
								<c:if test="${messageForm.tabIndex!=1}">
                                                    <LI>
                                                        <span>
                                                           
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1">
                                                             <div title='<digi:trn key="message:messagesAssosiatedWithTeam">List of Messages associated with Team</digi:trn>'>
	                 					<digi:trn key="message:Messages">Messages</digi:trn>
                                                            </div>
	                 				</a>
                                                        </span>
                                                    </LI>
								</c:if>							
                                                    
                                                    
                                                    
								<c:if test="${messageForm.tabIndex==2}">
                                                    <LI>
                                                        <a name="node">
                                                            <div>
									<digi:trn key="message:Alerts">Alerts</digi:trn>							
                                                            </div>
                                                        </a>
                                                    </LI>
								</c:if>
								<c:if test="${messageForm.tabIndex!=2}">
                                                    <LI>
                                                        <span>
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2">
                                                            <div title='<digi:trn key="message:alertsAssosiatedWithTeam">List of Alerts associated with Team</digi:trn>'>
										<digi:trn key="message:Alerts">Alerts</digi:trn>
                                                                </div>
									</a>							
                                                        </span>
                                                    </LI>
								</c:if>
                                                    
                                                    
                                                    
								<c:if test="${messageForm.tabIndex==3}">
                                                    <LI>
                                                        <a name="node"	>show
                                                            <div>
									<digi:trn key="message:approvals">Approvals</digi:trn>
                                                        </div>
                                                        </a>
                                                    </LI>
								</c:if>
								<c:if test="${messageForm.tabIndex!=3}">
                                                    <LI>
                                                        <span>
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=3">
                                                            <div title='<digi:trn key="message:approvalsAssosiatedWithTeam">List of Approvals associated with Team</digi:trn>'>
										<digi:trn key="message:approvals">Approvals</digi:trn>
                                                            </div>
									</a>
                                                        </span>
                                                    </LI>
								</c:if>
                                                    
                                                    
                                                    
								<c:if test="${messageForm.tabIndex==4}">
                                                    <LI>
                                                        <a name="node">
                                                            <div>
									<digi:trn key="message:ebents">Calendar Events</digi:trn>
                                                        </div>
                                                        </a>
                                                    </LI>
								</c:if>
								<c:if test="${messageForm.tabIndex!=4}">
                                                    <LI>
                                                        <span>
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=4">
                                                             <div title='<digi:trn key="message:eventsAssosiatedWithTeam">List of Events associated with Team</digi:trn>'>
										<digi:trn key="message:ebents">Calendar Events</digi:trn>
                                                            </div>
									</a>
                                                        </span>
                                                    </LI>
								</c:if>							
                                            </UL>						
                                        </DIV>
                                        
					<div id="main">
					<c:if test="${messageForm.tabIndex!=3 && messageForm.tabIndex!=4}">
					
						
                                                                <DIV id="subtabs">
                                                                 <div style="pa">
                                                                    <UL>
								
											<c:if test="${messageForm.childTab=='inbox'}">
                                                                                            <LI>
                                                                                                <span>
                                                                                                    <digi:trn key="message:inbox">Inbox</digi:trn>&nbsp;&nbsp;|					
                                                                                                </span>
                                                                                            </LI>
												
                                                                                        </c:if>
											<c:if test="${empty messageForm.childTab || messageForm.childTab!='inbox'}">
                                                                                            
                                                                                                <LI>
                                                                                                    <div>
                                                                                                        <span>
                                                                                                            
                                                                                                            <a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=inbox&tabIndex=${messageForm.tabIndex}">
                                                                                                                <digi:trn key="message:inbox">Inbox</digi:trn>
                                                                                                            </a>&nbsp;&nbsp;|							
                                                                                                        </span>
                                                                                                    </div>	
                                                                                                </LI>
                                                                                                    
											</c:if>
										
										
											<c:if test="${messageForm.childTab=='sent'}">
                                                                                             <LI>
                                                                                                <span>
                                                                                                    <digi:trn key="message:sent">Sent</digi:trn>&nbsp;&nbsp;|					
                                                                                                </span>
                                                                                            </LI>
												
											</c:if>
											<c:if test="${empty messageForm.childTab || messageForm.childTab!='sent'}">
                                                                                             <LI>
                                                                                                    <div>
                                                                                                        <span>
                                                                                                            <a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=${messageForm.tabIndex}">
                                                                                                                <digi:trn key="message:sent">Sent</digi:trn>
                                                                                                            </a>&nbsp;&nbsp;|							
                                                                                                        </span>
                                                                                                    </div>	
                                                                                                </LI>
												
											</c:if>
										 
										
											<c:if test="${messageForm.childTab=='draft'}">
                                                                                                <LI>
                                                                                                <span>
                                                                                                    <digi:trn key="message:draft">Draft</digi:trn>					
                                                                                                </span>
                                                                                            </LI>
												
											</c:if>
											<c:if test="${empty messageForm.childTab || messageForm.childTab!='draft'}">
                                                                                              <LI>
                                                                                                    <div>
                                                                                                        <span>
                                                                                                            <a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=${messageForm.tabIndex}">
                                                                                                                <digi:trn key="message:draft">Draft</digi:trn>
                                                                                                            </a>							
                                                                                                        </span>
                                                                                                    </div>	
                                                                                                </LI>
												
											</c:if>
												 <LI style="float: right;">
									               <div>
													<span id="displaySettingsButton"  style="cursor: pointer;float: right; font-style: italic;">
														<div id="show"  style="display:block; float: right; margin:0 3px 0 0;">Show more information &gt;&gt;</div>
														<div id="hidde" style="display:none;float: right; margin:0 3px 0 0;">Hidde more information<< </div>
													</span>
												   </div>	
									           </LI>
											</UL>
											</div>
									            <div id="currentDisplaySettings" style="clear:both; padding: 2px; display:none; background-color: rgb(255, 255, 204);">
			                                        <table  cellpadding="4" cellspacing="4" style="clear:both; padding:4px; border:silver dotted 1px; " >
														<tr>
															<td colspan="4">
																<b>Total of Massages</b>:		
															</td>
															<td colspan="4">
																<c:if test="${messageForm.allmsg != 0}">
																	${messageForm.allmsg}
																</c:if>
																<c:if test="${messageForm.allmsg == 0}">
																	(No of messages)
																</c:if>		
															</td>
														</tr>
														<tr>
															<td colspan="4">
																<b>Admin Settings</b>:
														
															</td>
															<td colspan="4">
																	<b>Message Refresh Time(minutes)</b>: ${messageForm.msgRefreshTimeCurr}|
																	<b>Message Storage Per Message Type</b>: ${messageForm.msgStoragePerMsgTypeCurr}|
																	<b>Days of Advance Alert Warnings</b>: ${messageForm.daysForAdvanceAlertsWarningsCurr}|
																	<b>Maximum validate</b>: ${messageForm.maxValidityOfMsgCurr}|
																	<b>Email Alerts</b>:
																	<c:if test="${messageForm.emailMsgsCurrent==-1}">
																		&nbsp;
																	</c:if>
																	<c:if test="${messageForm.emailMsgsCurrent==0}">
																		<digi:trn key="message:No">No</digi:trn>
																	</c:if>
																	<c:if test="${messageForm.emailMsgsCurrent==1}">
																		<digi:trn key="message:yes">Yes</digi:trn>
																	</c:if>
																	<br>
																	
															</td>
															
														</tr>
																		
			                                			
													</table>
											   </div>   
                                                &nbsp;
                                             </DIV>
											</c:if>
                                        </div>
                                        	</TD>					
						</TR>
                                              <TR>
                                              
                        
							<TD bgColor="#ffffff" class="contentbox_border" align="left">
								<TABLE id="msgsList" border="1">
									<TR class="usersg">
										<TD colspan="4">
										
										</TD>
									</TR>			
								</TABLE>
							</TD>
						</TR>
                                                <TR>
                                                      
                                               
							<TD bgColor="#ffffff"  align="left">
								<TABLE >
									<TR id="paginationPlace"><TD colspan="4"></TD></TR>			
								</TABLE>
							</TD>
                                                        </TR>
                                                        <TR >
                                                            <TD>&nbsp;</TD>
                                                        </TR>
                                                        <TR>
                                                        <TD>
                                                            <TABLE width="750px">
                                                            <TR>
                                                                <TD COLSPAN="2">
                                                                <strong><digi:trn key="message:IconReference">Icons Reference</digi:trn></strong>
                                                            </TD>
                                                            </TR>
                                                            <TR>
                                                                <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/unread.gif" vspace="2" border="0" align="absmiddle" />
                                                                    <digi:trn key="message:ClickExpandMessage"> Click on this icon to expand message&nbsp;</digi:trn>
                                                                    <br />
                                                            </TD>
                                                            </TR>
                                                             <TR>
                                                                <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/read.gif" vspace="2" border="0" align="absmiddle" />
                                                                    <digi:trn key="message:ClickCollapseMessage">Click on this icon to collapse message&nbsp;</digi:trn>
                                                                    <br />
                                                            </TD>
                                                            </TR>
                                                            <TR>
                                                                <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/finalForward.gif" vspace="2" border="0" align="absmiddle" />
                                                                    <digi:trn key="message:ClickForwardMessage">Click on this icon to forward message&nbsp;</digi:trn>
                                                                    <br />
                                                            </TD>
                                                            </TR>
                                                            <TR>
                                                                <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/edit.gif" vspace="2" border="0" align="absmiddle" />
                                                                    <digi:trn key="message:ClickEditMessage">Click on this icon to edit message&nbsp;</digi:trn>
                                                                    <br />
                                                            </TD>
                                                            </TR>
                                                             <TR>
                                                                <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/trash_12.gif" vspace="2" border="0" align="absmiddle" />
                                                                    <digi:trn key="message:ClickDeleteMessage">Click on this icon to delete message&nbsp;</digi:trn>
                                                                    <br />
                                                            </TD>
                                                            </TR>
                                                        </TABLE>
                                                        </TD>
                                                    </TR>
						
                                              
                                             </TABLE>				
                                                 
                                         </td>
                                     </tr>
                                 </table>
                             </td>
                         </tr>
                     </table>
                 </td>
             </tr>
         </table>

</digi:form>
