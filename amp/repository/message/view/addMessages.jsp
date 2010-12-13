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

<!-- Individual YUI CSS files --> 

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/autocomplete/assets/skins/sam/autocomplete.css"> 

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/autocomplete/autocomplete-min.js"></script> 


<digi:instance property="messageForm" />
<html:hidden name="messageForm" property="tabIndex"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<%--
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/script/messages.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
--%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.charcounter.js"/>"></script>


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

#statescontainer .yui-ac-content { 
    max-height:16em;overflow:auto;overflow-x:hidden; /* set scrolling */ 
    _height:16em; /* ie6 */ 
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
    _position:absolute; /* abs pos needed for ie quirks */
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
	
	<%--
    <table cellSpacing=0 cellPadding=0 vAlign="top" align="left" width="100%">
		<tr>
			<td width="100%">
				<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />
			</td>
		</tr>
		<tr>
		<td>
			<table  cellPadding="0" cellSpacing="0" width="780" border="0">
			    <tr>
				   <td width="14">&nbsp;</td>
					<td align="left" vAlign="top" width="750">
						<table cellPadding="5" cellSpacing="0" width="100%">
							<tr>
								<td height="33">
									<span class="crumb">
										<c:set var="translation">
											<digi:trn>Click here to view MyDesktop</digi:trn>
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
								<td><digi:errors/></td>
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
																	   <field:display name="Title Text Box" feature="Create Message Form">
																		 <td align="right" width="25%"><digi:trn key="messages:title">Title</digi:trn><font color="red">*</font> </td>
                                                                         <td align="left"><html:text property="messageName" style="width:320px;" styleClass="inp-text" styleId="titleMax"/></td>
																		</field:display>
																	</tr>																																					
																	<tr>
																	   <field:display name="Description Text Box" feature="Create Message Form">
																		<td align="right"><digi:trn key="message:description">Description</digi:trn></td>
                                                                        <td align="left">
                                                                         	<html:textarea name="messageForm" property="description"  rows="20"  styleClass="inp-text" style="width:320px;" styleId="descMax"/>
                                                                         </td>                                                                         
																	 </field:display>
																	</tr>
																	<tr>
																	  <field:display name="Related Activity Dropdown" feature="Create Message Form">
																		<td align="right" nowrap="nowrap"><digi:trn key="message:relatedActivity">Related Activity</digi:trn></td>
																		<td align="left"  nowrap="nowrap">
                                                                            <div>
                                                                                <div id="statesautocomplete" >
                                                                                    <html:text property="selectedAct" name="messageForm" style="width:320px;font-size:100%"  styleId="statesinput" ></html:text>
                                                                                    <img alt="" src="../ampTemplate/images/help.gif" onmouseover="showMessagesHelpTooltip()" onmouseout="hideMessagesHelpTooltip()" align="top" id="myImage"/>
                                                                                    <div id="statescontainer" style="width:320px;z-index: 100"></div>
                                                                                </div>     
                                                                                <div id="createMessagesHelpTooltip" style="display:none; z-index:10; position:absolute; left:400px;  border: 1px solid silver;">
                                                                                    <TABLE WIDTH='200px' BORDER='0' CELLPADDING='0' CELLSPACING='0'>
                                                                                        <TR style="background-color:#376091"><TD style="color:#FFFFFF" nowrap><digi:trn>Message Help</digi:trn></TD></TR>
                                                                                        <TR style="background-color:#FFFFFF"><TD><digi:trn>Type first letter of activity to view suggestions</digi:trn></TD></TR>
                                                                                    </TABLE>
                                                                                </div>
                                                                            </div>
																		</td>
																	  </field:display>
																	</tr>
																	<c:if test="${not empty messageForm.sdmDocument}">
																		<c:forEach var="attachedDoc" items="${messageForm.sdmDocument.items}">
																			<tr>
																				<td/>
																				<td >
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
																		<td/>
																		<td>
																			<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
																				<input id="fileUploaded" name="fileUploaded" type="file" class="file"/>
																			</div>
																			<input type="submit" value="upload" class="dr-menu" align="right" onclick="return validateFile()"/>
																		</td>
																	</tr>
                                                                    <tr>
                                                                    	<td align="right" nowrap="nowrap"><digi:trn key="message:priorityLevel">Priority Level</digi:trn></td>
                                                                        <td align="left"> 
                                                                        	<html:select property="priorityLevel" styleClass="inp-text" style="width:140px">                                         
                                                                        		<html:option value="0"><digi:trn>none</digi:trn> </html:option>                                       
                                                                                <html:option value="1"><digi:trn>low</digi:trn> </html:option>
                                                                                <html:option value="2"><digi:trn>Medium</digi:trn> </html:option>
                                                                                <html:option value="3"><digi:trn>Critical</digi:trn> </html:option>																							
                                                                            </html:select>																												                                                																																												
                                                                         </td>
                                                                    </tr> 
																	<tr>
																		 <field:display name="Set Alert Drop down" feature="Create Message Form">
																			<td align="right" valign="top"><digi:trn key="message:setAsAlert">Set as alert</digi:trn></td>
																			<td align="left">
	                                                                        	<html:select property="setAsAlert" styleClass="inp-text" style="width:140px">																							
																					<html:option value="0"><digi:trn>No</digi:trn> </html:option>
																					<html:option value="1"><digi:trn>Yes</digi:trn> </html:option>																																														
																			  	</html:select>																												                                                																																												
																			</td>
																		</field:display>
																      </tr>	
                                                                       <tr>
                                                                       		<field:display name="Recievers" feature="Create Message Form">
																					<td nowrap="nowrap" align="right">
																						<digi:trn>Receivers</digi:trn>
																						<img src="../ampTemplate/images/help.gif" onmouseover="stm([messageHelp,tmHelp],Style[15])" onmouseout="htm()"/>
																					</td>
																                    <td>
																                        <table border="0" >
																                            <tr>
																                                <td valign="top">
																                                   <select multiple="multiple" size="12" id="whoIsReceiver"  class="inp-text" style="width:200px" >
																										<logic:empty name="messageForm" property="teamMapValues">
																											<option value="-1">No receivers</option>
																										</logic:empty>
																										<logic:notEmpty name="messageForm"  property="teamMapValues" >																								
                                                                                                    	    <option value="all" ><digi:trn key="message:AllTeams">All</digi:trn></option>
                                                                                                               	<c:forEach var="team" items="${messageForm.teamMapValues}">
																														<logic:notEmpty name="team" property="members">
																															<option value="t:${team.id}" style="font-weight: bold;background:#CCDBFF;font-size:11px;">---${team.name}---</option>
																                                                			<c:forEach var="tm" items="${team.members}">
																                                                				<option value="m:${tm.memberId}" style="font:italic;font-size:11px;" id="t:${team.id}">${tm.memberName}</option>
																                                                			</c:forEach>
																														</logic:notEmpty>											                                                		
                                                                                                                </c:forEach>
																                                        </logic:notEmpty>
																                                	</select>																                                         
																                                </td>
																                                <td>
																                                  <input type="button" onclick="MyaddUserOrTeam();" style="width:80px;font-family:tahoma;font-size:11px;" value="<digi:trn key="message:addUsBtn">Add >></digi:trn>">
																                                  <br><br>
																                       			  <input type="button" style="width:80px;font-family:tahoma;font-size:11px;" onclick="MyremoveUserOrTeam()" value="<<<digi:trn key="message:rmbtn">Remove</digi:trn>" >	
																                                </td>
																                                <td valign="top">
															                                		<table>
																                                		<tr height="25px">
																                                			<td>
																                                				<div style="width:220px;">
																			                                		<div id="contactsAutocomplete"">
																			                                			<input type="text" id="contactInput" style="width:220px;font-size:100%">																                                			     
																														<div id="contactsContainer" style="width:220px;"></div>																				 
																													</div>																													
																			                                	</div>
																                                			</td>
																                                			<td nowrap="nowrap">
																                                				<html:button property="" onclick="addContact(document.getElementById('contactInput'))">Add</html:button>
																                                				<img src="../ampTemplate/images/help.gif" onmouseover="stm([messageHelp,extraReceivers],Style[15])" onmouseout="htm()"/>
																                                			</td>
																                                		</tr>
																                                		<tr height="75px">
																                                			<td colspan="2">
																                                				<div>
																			                                		<html:select multiple="multiple" styleId="selreceivers" name="messageForm" property="receiversIds"  size="10" styleClass="inp-text" style="width:220px">
																				                                    	<c:if test="${!empty messageForm.receivers}">
																					                                    	<html:optionsCollection name="messageForm" property="receivers" value="value" label="label" />
																					                                    </c:if>                
																				                                    </html:select>
																			                                	</div>
																                                			</td>
																                                		</tr>
																                                	</table>  
																                                </td>
																                            </tr>
																                        </table>
																                    </td>
																                  </field:display>
																				</tr>
																				
																																																																	
																				<tr>
																					<td colspan="2">
																						<table width="100%" >
																							<tr>
																							 <field:display name="Save button" feature="Create Message Form">
																									<td align="right" width="30%">
																										<c:set var="trnSavetBtn">
																											<digi:trn>Save</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSavetBtn }" onclick="save('draft');" class="dr-menu"/>
																									</td>
																							  </field:display>	
																							   <field:display name="Send button" feature="Create Message Form">
                                                                                                     <c:if test="${empty messageForm.forwardedMsg}">
																									<td align="center" width="6%">
																										<c:set var="trnSendtBtn">
																											<digi:trn>Send</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnSendtBtn }" onclick="save('send');" class="dr-menu"/>
																									</td>
                                                                                                    </c:if>
                                                                                               </field:display>
                                                                                                  <c:if test="${not empty messageForm.forwardedMsg}">
																									<td align="center" width="6%">
																										<c:set var="trnFwdtBtn">
																											<digi:trn>Forward</digi:trn>
																										</c:set> 
																										<input type="button" value="${trnFwdtBtn }" onclick="save('send');" class="dr-menu" />
																									</td>
                                                                                                    </c:if>
                                                                                                   <field:display name="Cancel button" feature="Create Message Form">
																										<td align="left" width="47%">
																											<c:set var="trnCancelBtn">
																												<digi:trn>Cancel</digi:trn>
																											</c:set>
																											<input type="button" value="${trnCancelBtn}" onclick="cancel();" class="dr-menu">																																							
																										</td>
																									</field:display>
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
	--%>																					




<br><br>


<div class="ins_box_left" style="border: 1px solid silver;">
	<div class="create_message">
		<table width="100%" border="0" cellspacing="3" cellpadding="3">
		  <tr>
  		  <td valign="top"><b style="font-size:12px;">Receivers</b> <img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif"/></td>
    		<td style="font-size:11px;"><input type="checkbox" name="checkbox" value="checkbox"/> Send to All<br/>
						
						<%--
						<select multiple="multiple" size="12" id="whoIsReceiver"  class="inp-text" style="width:200px" >
							<logic:empty name="messageForm" property="teamMapValues">
								<option value="-1">No receivers</option>
							</logic:empty>
							<logic:notEmpty name="messageForm"  property="teamMapValues" >																								
								<option value="all" ><digi:trn key="message:AllTeams">All</digi:trn></option>
									<c:forEach var="team" items="${messageForm.teamMapValues}">
										<logic:notEmpty name="team" property="members">
											<option value="t:${team.id}" style="font-weight: bold;background:#CCDBFF;font-size:11px;">---${team.name}---</option>
												<c:forEach var="tm" items="${team.members}">
													<option value="m:${tm.memberId}" style="font:italic;font-size:11px;" id="t:${team.id}">${tm.memberName}</option>
												</c:forEach>
										</logic:notEmpty>											                                                		
									</c:forEach>
							</logic:notEmpty>
						</select>	
						--%>
					


					<div class="msg_receivers">
						
						
						
							<logic:empty name="messageForm" property="teamMapValues">
								<div class="msg_lbl">No receivers</div>
							</logic:empty>
							<logic:notEmpty name="messageForm"  property="teamMapValues" >																								
									<c:forEach var="team" items="${messageForm.teamMapValues}">
										<logic:notEmpty name="team" property="members">
											<div class="msg_grp_name">
												<input type="checkbox" value="t:${team.id}" style="float:left;">
												<div class="msg_lbl">---${team.name}---</div>
											</div>
											<div class="msg_grp_mem_name">
												<c:forEach var="tm" items="${team.members}">
													<input type="checkbox" name="receiversIds" id="t:${team.id} type="checkbox" value="m:${tm.memberId}"/>${tm.memberName}<br/>
												</c:forEach>
											</div>
										</logic:notEmpty>											                                                		
									</c:forEach>
							</logic:notEmpty>						
				</div>
			<br/>
	<b>Additional Receivers: </b>Type first letter of contact to view suggestions or enter e-mail to send message to<br />
			<div class="msg_add">
				
				<input type="text" id="contactInput" class="inputx" style="width:470px; Font-size: 10pt; height:22px;">
				<input type="button" value="Add" class="buttonx_sm" style="position:absolute; left:480px;" onClick="addContact(document.getElementById('contactInput'))">
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
	    <td width=200 valign="top"><b style="font-size:12px;">Title</b> <b class="mand">*</b></td>
	    <td valign="top"><html:text property="messageName" style="width:100%;" styleClass="inputx" styleId="titleMax"/>
	  </tr>
	</field:display>
  <tr>
    <td valign="top"><b style="font-size:12px;">Description</b> <b class="mand">*</b></td>
    <td valign="top">
    	<html:textarea name="messageForm" property="description"  rows="5"  styleClass="inputx" style="width:100%; height:85px;" styleId="descMax"/>
    </td>
  </tr>
  <tr>
    <td valign="top">
    	<b style="font-size:12px;">Related Activity</b><img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" onmouseover="showMessagesHelpTooltip(this)" onmouseout="hideMessagesHelpTooltip()" id="myImage"/>
  		<div id="createMessagesHelpTooltip" style="display:none; z-index:10; position:absolute; border: 1px solid silver;">
          <TABLE WIDTH='200px' BORDER='0' CELLPADDING='0' CELLSPACING='0'>
              <TR style="background-color:#376091"><TD style="color:#FFFFFF" nowrap><digi:trn>Message Help</digi:trn></TD></TR>
              <TR style="background-color:#FFFFFF"><TD><digi:trn>Type first letter of activity to view suggestions</digi:trn></TD></TR>
          </TABLE>
      </div>
    </td>
    <td>
      <div>
        <div id="statesautocomplete" style="width:100%;">
            <html:text property="selectedAct" name="messageForm" style="width:100%;font-size:100%"  styleId="statesinput" ></html:text>
            <div id="statescontainer" style="width:100%;z-index: 100"></div>
        </div>     
      </div>
        
    </td>
  </tr>
  <tr>
    <td><b style="font-size:12px;">Priority Level</b></td>
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
    <td><b style="font-size:12px;">Set as alert</b></td>
    <td>
    	<html:select property="setAsAlert" styleClass="dropdwn_sm" style="width:140px">																							
				<html:option value="0"><digi:trn>No</digi:trn> </html:option>
				<html:option value="1"><digi:trn>Yes</digi:trn> </html:option>																																														
	  	</html:select>
    </td>
  </tr>
  
  <tr>
  	<td valign="top"><b style="font-size:12px;">Attachment</b></td>
		<td>
			<table width="100%" border="0">
			<c:if test="${not empty messageForm.sdmDocument}">
				<c:forEach var="attachedDoc" items="${messageForm.sdmDocument.items}">
					<tr>
						<td colspan="2">
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
					<input type="submit" value="upload" class="buttonx" align="right" onclick="return validateFile()"/>
				</td>
			</tr>
		</table>
		
		
		</td>
	</tr>
  
  
  <tr>
    <td colspan="2" align=center>
			<hr class="hr_3">
			
			<input type="button" value="Save" onclick="save('draft');" class="buttonx">
			
			<c:if test="${empty messageForm.forwardedMsg}">
				<c:set var="trnSendtBtn">
					<digi:trn>Send</digi:trn>
				</c:set> 
				<input type="button" value="${trnSendtBtn }" onclick="save('send');" class="buttonx">
			</c:if>
			<c:if test="${not empty messageForm.forwardedMsg}">
				<c:set var="trnFwdtBtn">
					<digi:trn>Forward</digi:trn>
				</c:set> 
				<input type="button" value="${trnFwdtBtn }" onclick="save('send');" class="buttonx">
			</c:if>
			
			<input type="button" value="Cancel" onclick="cancel();" class="buttonx">
		</td>
	</tr>
</table>
</div></div>



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
            this.oAutoComp2.maxResultsDisplayed = myArray.length;
	    this.oAutoComp2.formatResult = function(oResultItem, sQuery) {
	        var sMarkup = oResultItem[0];
	        return (sMarkup);
	    };
	    };

	    var contactsArray= [
	    	<c:forEach var="cont" items="${messageForm.contacts}">
	        	"<bean:write name="cont" filter="true"/>",
	        </c:forEach>
	   ];

	   YAHOO.example.ACJSArray = new function() {
		   	for(var i=0;i<contactsArray.length;i++){
		    	if(contactsArray[i]!= undefined ){
		        	contactsArray[i]=contactsArray[i].replace("&lt;","<");
		            contactsArray[i]=contactsArray[i].replace("&gt;",">");	
		        }
		    }
	        // Instantiate JS Array DataSource
	        this.oACDS2 = new YAHOO.widget.DS_JSArray(contactsArray);
	        // Instantiate AutoComplete
	        this.oAutoComp2 = new YAHOO.widget.AutoComplete('contactInput','contactsContainer', this.oACDS2);
	        this.oAutoComp2.prehighlightClassName = "yui-ac-prehighlight";
	        this.oAutoComp2.useShadow = true;
	        //this.oAutoComp2.forceSelection = true;
	        this.oAutoComp2.maxResultsDisplayed = contactsArray.length;
	        this.oAutoComp2.formatResult = function(oResultItem, sQuery) {
		        var sMarkup = oResultItem[0];
		        sMarkup=sMarkup.replace("<","&lt;");
		        sMarkup=sMarkup.replace(">","&gt;");
		        return (sMarkup);
		    };
	    };
     
        // attach character counters
        $("#titleMax").charCounter(50,{
	format: " (%1"+ " <digi:trn>characters remaining</digi:trn>)",
	pulse: false});

    $("#descMax").charCounter(500,{
		format: " (%1"+ " <digi:trn>characters remaining</digi:trn>)",
		pulse: false
	});

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
