<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<digi:instance property="messageForm" />
<html:hidden name="messageForm" property="tabIndex"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

 
	
	<!-- Individual YUI CSS files --> 
 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/autocomplete/assets/skins/sam/autocomplete.css"> 

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/autocomplete/autocomplete-min.js"></script> 
	




<style>
<!--
.ui-autocomplete {
	font-size:12px;
	border: 1px solid silver;
	max-height: 150px;
	overflow-y: scroll;
	background: white;
}

-->
</style>
<!-- for browse button -->
<style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}
input.file {
	width: 300px;
	margin: 0;
}
input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity:0 ;
	filter:alpha(opacity: 0);
	width: 300px;
	opacity: 0;
	z-index: 2;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile input {
	margin-bottom: 5px;
	margin-left: 0;
	width: 217px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: 100px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}

div.charcounter-progress-container {
	width:100%; 
	height:3px;
	max-height:3px;
	border: 1px solid gray; 
	filter:alpha(opacity=20); 
	opacity:0.2;
}

div.charcounter-progress-bar {
	height:3px; 
	max-height:3px;
	font-size:3px;
	background-color:#5E8AD1;
}


-->
</style>

<script langauage="JavaScript">	
	
	var W3CDOM = (document.createElement && document.getElementsByTagName);

	function initFileUploads() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));

		var fakeFileUpload2 = document.createElement('div');
		fakeFileUpload2.className = 'fakefile2';


		var button = document.createElement('input');
		button.type = 'button';

		button.value = '<digi:trn>Browse...</digi:trn>';
		fakeFileUpload2.appendChild(button);

		fakeFileUpload.appendChild(fakeFileUpload2);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];

 			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}	
	}
    function showMessagesHelpTooltip() {
			
           var div=document.getElementById("createMessagesHelpTooltip");
           div.style.display = "block";
    }
    function hideMessagesHelpTooltip(){
      document.getElementById("createMessagesHelpTooltip").style.display = "none";
    }

</script>
<DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<script langauage="JavaScript">	
	
var MyArray=new Array();

function initMyArray(){
  var reslist = document.getElementById('whoIsReceiver');
  var teamIndex = 0;
  for(var i = 0; i < reslist.length; i++) {
    if(reslist.options[i].value.indexOf('t')==0){//it is a team
      MyArray[teamIndex]=new Array();
      MyArray[teamIndex][0]=reslist.options[i];
      teamIndex++;
     }
  }
}
function addUserOrTeam(){
    
  var reslist = document.getElementById('whoIsReceiver');
  var selreceivers=document.getElementById('selreceivers');

  if(reslist == null) {
    return false;
  }
  var Mindex=reslist.selectedIndex;
    
  initMyArray();//creates the empty array
  
  if(selreceivers.length!=0){
    getCurrentSelectedReceivers(); //fills the array with existing selected options
  }
  if (Mindex != -1) {
    for(var i = 0; i < reslist.length; i++) {
      if(reslist.options[i].selected){
        if(reslist.options[i].value=="all"){
          selectAllReceivers();
          break;
        }
        else if(document.getElementById('whoIsReceiver').options[i].value.indexOf('t')==0){//the option is a team
          var Myrow=getTeamRow(reslist.options[i].value);
          //clean all row
          for(var col=1; col<MyArray[Myrow].length; col++){
             MyArray[Myrow][col]=null;
          }
          //fill the row
          var Mycol=1;
          for(var index=0; index<reslist.length; index++){
            if(reslist.options[index].id==MyArray[Myrow][0].value){
              MyArray[Myrow][Mycol]=reslist.options[index];
              Mycol++
            }
          }
        }  
        else{//the option is a member
          if(!isOptionSelected(reslist.options[i])){//it is not at the list yet
            var Myrow=getTeamRow(reslist.options[i].id);
            var Mycol=MyArray[Myrow].length;
            MyArray[Myrow][Mycol]=reslist.options[i];
          } 
        }
      }
    }
    showReceivers();
  }
  return false;
}
function getTeamRow(idTeam){
  for(var row=0; row<MyArray.length; row++){
    if(MyArray[row][0].value==idTeam){
      return row;
    }
  }  
}
function isOptionSelected(option){
  var selreceivers=document.getElementById('selreceivers');
  for(var j=0; j<selreceivers.length;j++){
    if(selreceivers.options[j].value==option.value){
      return true;
    }
  }
  var row=getTeamRow(option.id);
  for(var col=1;col<MyArray[row].length;col++){
    if(MyArray[row][col].value==option.value){
      return true;
    }
  }
  return false
}

function selectAllReceivers(){
  var reslist = document.getElementById('whoIsReceiver');
  var selreceivers=document.getElementById('selreceivers');
  initMyArray();
  for(var h; h<selreceivers.length; h++){
    selreceivers.options[h]=null
  }  
  selreceivers.options.length=0;
  for(var i=1; i<reslist.length; i++){
    if(reslist.options[i].value.indexOf('m')==0){//it is not at the list yet
      var Myrow=getTeamRow(reslist.options[i].id);
      var Mycol=MyArray[Myrow].length;
      MyArray[Myrow][Mycol]=reslist.options[i];
    } 
  }      
}
function showReceivers(){
  var selreceivers=document.getElementById('selreceivers');
  for(var h; h<selreceivers.length; h++){
    selreceivers.options[h]=null
  }  
  selreceivers.options.length=0;
  for(var i=0; i<MyArray.length; i++){
    if(MyArray[i][1]!=null){
      for(var j=0; j<MyArray[i].length; j++){
        if(MyArray[i][j]!=null){
          addOnption(selreceivers,MyArray[i][j].text,MyArray[i][j].value, MyArray[i][j].id);
        }
      }
    }
  }
}
function getCurrentSelectedReceivers(){
  var selreceivers=document.getElementById('selreceivers');
  for(var i = 0; i < selreceivers.length; i++) {
    if(selreceivers.options[i].id.indexOf('t')==0){//filters team members, they have id=team's id
      var row = getTeamRow(document.getElementById('selreceivers')[i].id);
      var col = MyArray[row].length;
      MyArray[row][col]=selreceivers.options[i];
    }
  }
} 
function registerOrphanMember(orphans){
   for(var i=0; i<orphans.length; i++){
      var itsTeam = getTeam(orphans[i].value);
      orphans[i].id=itsTeam;
   }    	
}
function getTeam(memberValue){
  var reslist = document.getElementById('whoIsReceiver');
  var found = false;
  for(var i=reslist.length-1; i>0; i--){
  	if(reslist.options[i].value==memberValue){
  	   return reslist.options[i].id;
  	}
  }
  return "";
}
function addOnption(list, text, value, id){
    if (list == null) {
        return;
    }
    var option = document.createElement("OPTION");
    option.value = value;
    option.text = text;
    option.id = id;
    list.options.add(option);
    return false;
}
  
function removeUserOrTeam() {
  var tobeRemoved=document.getElementById('selreceivers');
  if(tobeRemoved==null){
    return;
  }   
  var teamId=-1;
  if($("#selreceivers > option[value='guest']:selected").length>0){
       $("#selreceivers > option[value^='c:']").remove();
  }
  for(var i=document.getElementById('selreceivers').length-1; i>=0; i--){
    if(document.getElementById('selreceivers')[i].selected){
      if(document.getElementById('selreceivers')[i].value.indexOf('t')==0){//it's a team
        var elem = document.getElementById('selreceivers').length
        for(var j=elem-1; j>=0; j--){
          if(document.getElementById('selreceivers')[j].id == document.getElementById('selreceivers')[i].value){
            document.getElementById('selreceivers')[j]=null;
          }
        }
      }
      else{// it is a team member
        //get team's id from the team member
        teamId = document.getElementById('selreceivers')[i].id;
      }
      document.getElementById('selreceivers')[i]=null;
      if(teamId!=-1){//if a member has been removed
        var noMember=true;
        //check if another member belonging to the same team exists
        for(var j=document.getElementById('selreceivers').length-1; j>=0; j--){
          if(document.getElementById('selreceivers')[j].id==teamId){
            noMember=false;//there is member belonging to the same team
          }
        }
        if(noMember){
          //there are not members so, remove the team from the receivers
          for(var h=document.getElementById('selreceivers').length-1; h>=0; h--){
            if(document.getElementById('selreceivers')[h].value==teamId){
              document.getElementById('selreceivers')[h]=null;
              i--;
            }
          }     
        }
      }     
    }     
  }
  if($("#selreceivers > option[value^='c:']").length==0){
        $("#selreceivers > option[value='guest']").remove();
   }
}

function addActionToURL(actionName){
  var fullURL=document.URL;
  var lastSlash=fullURL.lastIndexOf("/");
  var partialURL=fullURL.substring(0,lastSlash);
  return partialURL+"/"+actionName;
}
	
	
	
	
	
	
	
	
	
	
    var guestText='---<digi:trn jsFriendly="true">Guest</digi:trn>---';
	var messageHelp='<digi:trn>Message Help</digi:trn>';
	var relatedActs='<digi:trn>Type first letter of activity to view suggestions</digi:trn>';
	var extraReceivers='<digi:trn>Type first letter of contact to view suggestions \n or enter email to send message to</digi:trn>';
	var tmHelp='<digi:trn>A user may appear in more than one workspace.\n Be sure to choose the correct workspace and user within the workspace.</digi:trn>';

	function getElementOffset (domObject) {
		var pos = {top: 0, left: 0}

		var retX = 0;
		var retY = 0;
		
		while (domObject.offsetParent != null) {
			retX += domObject.offsetLeft;
			retY += domObject.offsetTop;
			
			domObject = domObject.offsetParent;
		}
		logged = true;
    pos.left = retX;
    pos.top = retY;
		return pos;
	}


    function showMessagesHelpTooltip(obj) {
    	var callerPos = getElementOffset(obj);
      var div=document.getElementById("createMessagesHelpTooltip");
      div.style.left = callerPos.left;
      div.style.top = callerPos.top + 15;
    	div.style.display = "block";
    }
	function hideMessagesHelpTooltip(){
  		document.getElementById("createMessagesHelpTooltip").style.display = "none";
	}
    
    
  function MyremoveUserOrTeam(){
  	var orphands=new Array();
    var list = document.getElementById('selreceivers');
	var orpIndex = 0;
    for(var i=0; i<list.length;i++){
      if(list.options[i].value.indexOf('m')==0 && list.options[i].id.indexOf('t')!=0){
         orphands[orpIndex]=list.options[i];
         orpIndex++;
      }
    }
    if(orpIndex!=0){
       registerOrphanMember(orphands);
    }
    removeUserOrTeam();
  }  
  
  function MyaddUserOrTeam(){
    var list = document.getElementById('selreceivers');
    var MyContacts=new Array();
	var orphands=new Array();
	var orpIndex = 0; //teams and team members
	var index = 0; //contact
    for(var i=0; i<list.length;i++){
		if(list.options[i].value.indexOf('m')==0 && list.options[i].id.indexOf('t')!=0){
			orphands[orpIndex]=list.options[i];
			orpIndex++;
		}

		if(list.options[i].value.indexOf('c')==0){
        	MyContacts[index]=list.options[i];
        	index++;
        }
    }
    if(orpIndex!=0){
		registerOrphanMember(orphands);
    }

	//add teams and members
  	addUserOrTeam();//fills out the list with teams and members

  	if(index != 0){
        addOption(list,guestText,"guest");
		for(var j=0; j<index; j++){
			list.options.add(MyContacts[j]);
		}
	}
  }
	
  function validate(){
	        var titleSize=document.messageForm.messageName.value.length;
            var descSize=document.messageForm.description.value.length;
            <c:set var="message">
        		<digi:trn>Please enter name </digi:trn>						
            </c:set>
            <c:set var="msg">
        		${fn:replace(message,'\\n',' ')}
            </c:set>
            <c:set var="quote">'</c:set>
            <c:set var="escapedQuote">\'</c:set>
            <c:set var="msgsq">
        		${fn:replace(msg,quote,escapedQuote)}
            </c:set>
		if(titleSize==0){
			alert('${msgsq}');
			return false;
		}else{
        	if(titleSize>50){
         		alert(' You have entered '+titleSize+' symblos but maximum allowed are 50');
				return false;
         	}
         	if(descSize>500){
         		alert(' You have entered '+descSize+' symblos but maximum allowed are 500');
				return false;
         	}
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

	function removeAttachment(attachmentOrder){
		//check submit selected receivers
		var list = document.getElementById('selreceivers');
		if(list!=null){
    		for(var i = 0; i < list.length; i++) {
        		list.options[i].selected = true;
    		}
    	}
		messageForm.action="${contextPath}/message/messageActions.do?actionType=removeAttachment&attachmentOrder="+attachmentOrder;
  		messageForm.target = "_self";
  		messageForm.submit();
	}
	
	function cancel() {
		messageForm.action="${contextPath}/message/messageActions.do?actionType=cancelMessage";
  		messageForm.target = "_self";
  		messageForm.submit();	
	}
	
	function selectUsers(event) {
    	//var list = document.getElementsByName('selreceivers');
    	
    	var mainList = $("input[name='selreceivers'][type='checkbox']:checked");
    	var guestList = $("input[name='selreceivers'][type='hidden']");
			
			var list = mainList.push(guestList);
    	
    	
    	
    	
    	if (event=='send') {
    		if ((mainList == null || mainList.length==0) && (guestList == null || guestList.length==0)) {
    			<c:set var="message">
            	<digi:trn>Please add receivers </digi:trn>						
                </c:set>
                <c:set var="msg">
            	${fn:replace(message,'\\n',' ')}
                </c:set>
                <c:set var="quote">'</c:set>
                <c:set var="escapedQuote">\'</c:set>
                <c:set var="msgsq">
            	${fn:replace(msg,quote,escapedQuote)}
                </c:set>
        		alert('${msgsq}');
        		return false ;
    		}
    	}    	
    	
        //check guest emails
				for(var i = 0; i < guestList.length; i++) {
						var receiver=guestList[i].value.substring(2);
						var email=receiver;
						if(receiver.indexOf("<")!=-1){
							email=receiver.substr(receiver.indexOf("<")+1, receiver.indexOf(">")-receiver.indexOf("<")-1); //cut email from "some text <email>"
						}
						
						var pattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
						var expression = new RegExp(pattern)
					    if(expression.test(email)!=true){
						    var trn='<digi:trn>Please provide correct email</digi:trn>';
								alert(trn);
						    return false; 
					    }
				}

       /* 	
    		for(var i = 0; i < list.length; i++) {
        		list.options[i].selected = true;
    		}*/
    	
    	return true;
	}
	
	var addedGuests = new Array();
	
	function addContact(contact){
		var guestVal=contact.value;
		if(guestVal.length>0 && $.inArray("c:" + guestVal, addedGuests) < 0){
			addedGuests.push("c:" + guestVal);
			var filteredGusetId = guestVal.replace("<", "&lt;").replace(">", "&gt;");

			var guestListItemMarkup = new Array();
			guestListItemMarkup.push('<div class="msg_added_cont">');
			guestListItemMarkup.push('<div style="float:right;"><span style="cursor:pointer;" onClick="removeGuest(this)">[x] remove</span></div>');
			guestListItemMarkup.push(filteredGusetId);
			guestListItemMarkup.push('<input name="receiversIds" class="guest_contact_hidden" type="hidden" value="c:');
			guestListItemMarkup.push(filteredGusetId);
			guestListItemMarkup.push('">');
			guestListItemMarkup.push('</div></div>');
			$('#guest_user_container').append(guestListItemMarkup.join(''));
		}	

		contact.value = "";
	}
	
	function removeGuest(obj) {
		var delControl = $(obj);
		delControl.parent().parent().remove();
		
		var addedGuestIdx;
		for (addedGuestIdx = 0; addedGuestIdx < addedGuests.length; addedGuestIdx ++) {
			if (addedGuests[addedGuestIdx] == delControl.parent().parent().find("input").attr("value")) {
				addedGuests.splice(addedGuestIdx, 1);
				break;
			}
		}
		return null;
	}

	function validateFile(){
		var fileToBeAttached=document.getElementById('fileUploaded');
		if(fileToBeAttached.value==null || fileToBeAttached.value==''){
			var msg='<digi:trn>Please select file to attach</digi:trn>';
			alert(msg);
			return false;
		}
		//check submit selected receivers
		
		//$("input[type=checkbox][name=receiversIds]:checked");
		
		var list = document.getElementById('selreceivers');
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

#statesAutoComplete ul,
{
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#contactsAutocomplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#statesAutoComplete div{
	padding: 0px;
	margin: 0px; 
}

#contactsAutocomplete div {
	padding: 0px;
	margin: 0px; 
}

#statesAutoComplete,
#contactsAutocomplete {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#statesAutoComplete,contactsAutocomplete {
    z-index:3; /* z-index needed on top instance for ie & sf absolute inside relative issue */
    font-size: 12px;
}

#statesInput,
#contactInput {
    font-size: 12px;
}
.charcounter {
    display: block;
    font-size: 11px;
}

#statesAutoComplete {
    width:320px; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}
#myImage {
    position:absolute; left:320px; margin-left:1em; /* place the button next to the input */
}

span.extContactDropdownEmail {
	color:grey;
}

-->
</style>

<c:set var="messageType">
    <c:choose>
        <c:when test="${messageForm.tabIndex==1}">
            <digi:trn>Messages</digi:trn>
        </c:when>
        <c:when test="${messageForm.tabIndex==2}">
            <digi:trn>Alerts</digi:trn>
        </c:when>
        <c:when test="${messageForm.tabIndex==3}">
            <digi:trn>Approvals</digi:trn>
        </c:when>
        <c:otherwise>
            <digi:trn>Calendar Events</digi:trn>
        </c:otherwise>
    </c:choose>
</c:set>
<c:set var="title">
    <c:choose>
        <c:when test="${not empty messageForm.forwardedMsg}">
            
            <c:choose>
                <c:when test="${messageForm.tabIndex==1}">
            <digi:trn>Forward Message</digi:trn>
        </c:when>
                <c:when test="${messageForm.tabIndex==2}">
                    <digi:trn>Forward Alert</digi:trn>
                </c:when>
                <c:when test="${messageForm.tabIndex==3}">
                    <digi:trn>Forward Approvals</digi:trn>
                </c:when>
                <c:otherwise>
                    <digi:trn>Forward Calendar Events</digi:trn>
                </c:otherwise>
            </c:choose>
            
            
        </c:when>
        <c:when test="${messageForm.messageId==null}">
            <digi:trn>Add Message</digi:trn>
        </c:when>
        <c:otherwise>
            <digi:trn>Edit Message</digi:trn>
        </c:otherwise>
    </c:choose>
</c:set>

<digi:form  action="/messageActions.do?actionType=attachFilesToMessage" method="post" enctype="multipart/form-data">
																				




<div class="ins_box_left" style="border: 1px solid silver; background-color:#F5F5F5;">
	<div class="create_message">
		<table width="100%" border="0" cellspacing="3" cellpadding="3">
		  <tr>
  		  <td valign="top"><b style="font-size:12px;"><digi:trn>Receivers</digi:trn></b>
  		  	<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" onmouseover="stm([messageHelp,tmHelp],Style[15])" onmouseout="htm()"/>
  		  </td>
    		<td style="font-size:11px;">

					<div class="msg_receivers">
						
						
						
							<logic:empty name="messageForm" property="teamMapValues">
								<div class="msg_lbl">No receivers</div>
							</logic:empty>
							<logic:notEmpty name="messageForm"  property="teamMapValues" >
								<c:forEach var="team" items="${messageForm.teamMapValues}">
									<logic:notEmpty name="team" property="members">
										<div class="rec_group_container">
											<div class="msg_grp_name">
												<input class="group_checkbox" type="checkbox" value="t:${team.id}" style="float:left;">
												<div class="msg_lbl">---${team.name}---</div>
											</div>
											<div class="msg_grp_mem_name">
												<c:forEach var="tm" items="${team.members}">
													<input type="checkbox" name="receiversIds" id="t:${team.id} type="checkbox" value="m:${tm.memberId}"/>${tm.memberName}<br/>
												</c:forEach>
											</div>
										</div>
									</logic:notEmpty>											                                                		
								</c:forEach>
		
							</logic:notEmpty>						
				</div>
			<br/>
			<input type="checkbox" name="sendToAll" value="checkbox"/><digi:trn>Send to All</digi:trn><br/><br/>
	<b>Additional Receivers: </b>Type first letter of contact to view suggestions or enter e-mail to send message to<br />
			<div class="msg_add">
				
				<input type="text" id="contactInput" class="inputx" style="width:470px; Font-size: 10pt; height:22px;">
				<div id="extContactAutocom"></div>
				<input type="button" value="Add" class="buttonx_sm" onClick="addContact(document.getElementById('contactInput'))">
				<br><br>
				
				<div id="contactsContainer" style="width:470px;"></div>
			
				<div id="guest_user_container">
				</div>
	</td>
  </tr>
<tr>
<td colspan=2><hr class="hr_3"></td>
</tr>
	<field:display name="Title Text Box" feature="Create Message Form">
	  <tr>
	    <td width=200 valign="top"><b style="font-size:12px;"><digi:trn>Title</digi:trn></b> <b class="mand">*</b>
	    	<br/>
	    	<span style="font-size:11px;" id="titleCharCounter"></span>	
	    	<div class="charcounter-progress-container">
	    		<div id="titleProgressBar" class="charcounter-progress-bar" style="width:0%;"></div>
	    	</div>
	    </td>
	    <td valign="top"><html:text property="messageName" style="width:100%;" styleClass="inputx" styleId="titleMax"/>
	  </tr>
	</field:display>
  <tr>
    <td valign="top"><b style="font-size:12px;"><digi:trn>Description</digi:trn></b> <b class="mand">*</b>
    	<br/>
	    <span style="font-size:11px;" id="descCharCounter"></span>
	    <div class="charcounter-progress-container">
	    	<div id="descProgressBar" class="charcounter-progress-bar" style="width:0%;"></div>
	    </div>
    </td>
    <td valign="top">
    	<html:textarea name="messageForm" property="description"  rows="5"  styleClass="inputx" style="width:100%; height:85px;" styleId="descMax"/>
    </td>
  </tr>
  <tr>
    <td valign="top">
    	<b style="font-size:12px;"><digi:trn>Related Activity</digi:trn></b>
    	&nbsp;<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" onmouseover="showMessagesHelpTooltip(this)" onmouseout="hideMessagesHelpTooltip()"/>
    	
    	
  		<div id="createMessagesHelpTooltip" style="display:none; z-index:10; position:absolute; border: 1px solid silver;">
          <TABLE WIDTH='200px' BORDER='0' CELLPADDING='0' CELLSPACING='0'>
              <TR style="background-color:#376091"><TD style="color:#FFFFFF" nowrap><digi:trn>Message Help</digi:trn></TD></TR>
              <TR style="background-color:#FFFFFF"><TD><digi:trn>Type first letter of activity to view suggestions</digi:trn></TD></TR>
          </TABLE>
      </div>
      
      
    </td>
    <td>
      <div>
        <div style="width:100%;">
            <html:text property="selectedAct" name="messageForm" style="width:100%;"  styleId="statesinput"></html:text>
            <div id="statesautocomplete"></div>
        </div>     
      </div>
        
    </td>
  </tr>
  <tr>
    <td><b style="font-size:12px;"><digi:trn>Priority Level</digi:trn></b></td>
    <td>
    	<html:select property="priorityLevel" styleClass="dropdwn_sm" style="width:140px">
    		<html:option value="0"><digi:trn>none</digi:trn> </html:option>
        <html:option value="1"><digi:trn>low</digi:trn> </html:option>
        <html:option value="2"><digi:trn>Medium</digi:trn> </html:option>
        <html:option value="3"><digi:trn>Critical</digi:trn> </html:option>
      </html:select>
		</td>
  </tr>
  <tr>
    <td><b style="font-size:12px;"><digi:trn>Set as alert</digi:trn></b></td>
    <td>
    	<html:select property="setAsAlert" styleClass="dropdwn_sm" style="width:140px">																							
				<html:option value="0"><digi:trn>No</digi:trn> </html:option>
				<html:option value="1"><digi:trn>Yes</digi:trn> </html:option>																																														
	  	</html:select>
    </td>
  </tr>
  
  <tr>
  	<td valign="top"><b style="font-size:12px;"><digi:trn>Attachment</digi:trn></b></td>
		<td>
			<table width="100%" border="0">
			<c:if test="${not empty messageForm.sdmDocument}">
				<c:forEach var="attachedDoc" items="${messageForm.sdmDocument.items}">
					<tr>
						<td colspan="2" style="font-size:12px;>
							<jsp:useBean id="urlParamsSort" type="java.util.Map" class="java.util.HashMap"/>
							<c:if test="${not empty messageForm.sdmDocument.id}">
								<c:set target="${urlParamsSort}" property="documentId" value="${messageForm.sdmDocument.id}"/>
							</c:if>																					
							<digi:link module="sdm" href="/showFile.do~activeParagraphOrder=${attachedDoc.paragraphOrder}" name="urlParamsSort">
								<img src="/repository/message/view/images/attachment.png" border="0" />
								${attachedDoc.contentTitle}
							</digi:link>
							<a href="javascript:removeAttachment(${attachedDoc.paragraphOrder})" title="Click Here To Remove Attachment" ><img  src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border=0"/></a>
						</td>
					</tr>
				</c:forEach>
			</c:if>
			<tr>
				<td>
					<%--
					<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
					--%>
						<input id="fileUploaded" name="fileUploaded" type="file" class="file"/>
					<%--
					</div>
					--%>
				</td><td align="right">
					<input type="submit" value="<digi:trn>Upload</digi:trn>" class="buttonx" align="right" onclick="return validateFile()"/>
				</td>
			</tr>
		</table>
		
		
		</td>
	</tr>
  
  
  <tr>
    <td colspan="2" align=center>
			<hr class="hr_3">
			<field:display name="Save button" feature="Create Message Form">
				<input type="button" value="<digi:trn>Save</digi:trn>" onclick="save('draft');" class="buttonx">
			</field:display>
			
			<field:display name="Send button" feature="Create Message Form">
				<c:if test="${empty messageForm.forwardedMsg}">
					<c:set var="trnSendtBtn">
						<digi:trn>Send</digi:trn>
					</c:set> 
					<input type="button" value="${trnSendtBtn }" onclick="save('send');" class="buttonx">
				</c:if>
			</field:display>
			
			<c:if test="${not empty messageForm.forwardedMsg}">
				<c:set var="trnFwdtBtn">
					<digi:trn>Forward</digi:trn>
				</c:set> 
				<input type="button" value="${trnFwdtBtn }" onclick="save('send');" class="buttonx">
			</c:if>
			
			<field:display name="Cancel button" feature="Create Message Form">
				<input type="button" value="<digi:trn>Cancel</digi:trn>" onclick="cancel();" class="buttonx">
			</field:display>
		</td>
	</tr>
</table>
</div></div>



<script type="text/javascript">
	
	//Char counters	
	var titleLength = 50;
	var titleCounter = $("#titleCharCounter");
	var titleProgressBar = $("#titleProgressBar");
	initTitleCharCounter();
	
	function initTitleCharCounter() {
		var titleCounterTxt = ["(", titleLength - $("#titleMax").val().length, " <digi:trn>characters remaining</digi:trn>", ")"];
		titleCounter.html(titleCounterTxt.join(""));
		titleProgressBar.css("width", $("#titleMax").val().length/titleLength*100 + "%");
	}
	$("#titleMax").bind("keyup", function (event) {
		if (this.value.length > titleLength) {
			this.value = this.value.substring(0, titleLength);
		}
		var titleCounterTxt = ["(", titleLength - this.value.length, " <digi:trn>characters remaining</digi:trn>", ")"];
		titleCounter.html(titleCounterTxt.join(""));
		titleProgressBar.css("width", this.value.length/titleLength*100 + "%");
	});
	
	var descLength = 500;
	var descCounter = $("#descCharCounter");
	var descProgressBar = $("#descProgressBar");
	
	initDescCharCounter();
	
	function initDescCharCounter() {
		var descCounterTxt = ["(", descLength - $("#descMax").val().length, " <digi:trn>characters remaining</digi:trn>", ")"];
		descCounter.html(descCounterTxt.join(""));
		descProgressBar.css("width", $("#descMax").val().length/descLength*100 + "%");
	}
	$("#descMax").bind("keyup", function (event) {
		if (this.value.length > descLength) {
			this.value = this.value.substring(0, descLength);
		}
		var descCounterTxt = ["(", descLength - this.value.length, " <digi:trn>characters remaining</digi:trn>", ")"];
		descCounter.html(descCounterTxt.join(""));
		descProgressBar.css("width", this.value.length/descLength*100 + "%");
	});
	//End of char counters	
	

	$(".group_checkbox").bind("change", function (event) { 
		var selGrpCtrl = $(this);
		var childUsers = selGrpCtrl.parent().parent().find("input[name='receiversIds']");
		
		childUsers.each(
			function (idx){
				this.checked = selGrpCtrl.attr("checked");
			})
		});



			
			//Related activity autocomplite
			var relatedActDataSource = new YAHOO.widget.DS_XHR("/message/messageActions.do", ["\n", ";"]);
			relatedActDataSource.scriptQueryAppend = "actionType=searchRelatedAcrivities";
			relatedActDataSource.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
			relatedActDataSource.queryMatchContains = true;
		  relatedActDataSource.scriptQueryParam  = "srchStr";
			var relatedActAutoComp = new YAHOO.widget.AutoComplete("statesinput","statesautocomplete", relatedActDataSource);
			relatedActAutoComp.queryDelay = 0.5;
			$("#statesinput").css("position", "static");
			
			
			//External contact autocomplite
			var extContactDataSource = new YAHOO.widget.DS_XHR("/message/messageActions.do", ["\n", ";"]);
			extContactDataSource.scriptQueryAppend = "actionType=searchExternalContacts";
			//extContactDataSource.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
			extContactDataSource.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
			extContactDataSource.queryMatchContains = true;
		  extContactDataSource.scriptQueryParam  = "srchStr";
			var extContactAutoComp = new YAHOO.widget.AutoComplete("contactInput","extContactAutocom", extContactDataSource);
			
			extContactAutoComp.formatResult = function( oResultData , sQuery , sResultMatch ) {
				var retVal;
				//Hilight email separately
				
				if (oResultData[0].indexOf('<') > -1 && oResultData[0].indexOf('>') > -1) {
					var contactEmail = oResultData[0].substring (oResultData[0].indexOf('<') + 1, oResultData[0].indexOf('>'));
					var contactName = oResultData[0].substring (0, oResultData[0].indexOf('<'));
					var markup = [contactName, '<span class="extContactDropdownEmail">(', contactEmail, ')<span>'];
					retVal = markup.join("");
				} else {
					retVal = oResultData;
				}
				
				return retVal;
			}
			
			extContactAutoComp.queryDelay = 0.5;
			$("#contactInput").css("position", "static");
	

	

	$("input[name='sendToAll']").bind("change", function (event) { 
		var selAllCtrl = $(this);
		var allUsers = selAllCtrl.parent().find(".msg_receivers").find("input");
		allUsers.each(
			function (idx){
				this.checked = selAllCtrl.attr("checked");
			})
		});
		
		
		//Preselect recipients
		function preselectRecipients() {
			var preselectedIds = Array();
			<logic:notEmpty name="messageForm"  property="receiversIds" >
				<c:forEach var="recId" items="${messageForm.receiversIds}">
					preselectedIds.push('${recId}');
				</c:forEach>
			</logic:notEmpty>						
			
			var preselIdx;
			for (preselIdx = 0; preselIdx < preselectedIds.length; preselIdx ++) {
				$('input[type="checkbox"][value="' + preselectedIds[preselIdx] + '"]').attr("checked",true);
			} 
		
		}
		
		preselectRecipients();

  $("#descMax").bind("paste", function (event) { 
  	var browser=navigator.appName;
  	if(browser=="Microsoft Internet Explorer"){
  		var textThatNeedsToBePasted = window.clipboardData.getData("Text");
  		var desc = document.getElementById('descMax');
  		if(textThatNeedsToBePasted.length + desc.value.length >500){
  			var msg="<digi:trn jsFriendly='true'>You can not exceed 500 symbols</digi:trn>";
  			alert(msg);
  			window.clipboardData.setData("Text",'');
  		}
      }				
	});

    $("#titleMax").bind("paste", function (event) { 
    	var browser=navigator.appName;
    	if(browser=="Microsoft Internet Explorer"){
    		var textThatNeedsToBePasted = window.clipboardData.getData("Text");
    		var title = document.getElementById('titleMax');
    		if(textThatNeedsToBePasted.length + title.value.length >500){
    			var msg="<digi:trn jsFriendly='true'>You can not exceed 50 symbols</digi:trn>";
    			alert(msg);
    			window.clipboardData.setData("Text",'');
    		}
        }
	});

	
</script>



<script type="text/javascript">
	initFileUploads();
</script>


</digi:form>
