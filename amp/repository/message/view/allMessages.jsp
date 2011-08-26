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
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>


<script langauage="JavaScript">

    var  selectedMessagePanel;
    var noMsgs="<digi:trn>No Messages Present</digi:trn>";
    var noAlerts="<digi:trn>No Alerts Present</digi:trn>";
    var noApprovals="<digi:trn>No Pending Approvals</digi:trn>";
    var noEvents="<digi:trn>No Upcoming Events</digi:trn>";
   	var from='<digi:trn>From</digi:trn>';
    var to='<digi:trn>To</digi:trn>';
	var date='<digi:trn>Date</digi:trn>';
	var prLevel='<digi:trn>Priority</digi:trn>';
	var desc='<digi:trn>Message Details</digi:trn>';
	var editBtn='<digi:trn>Edit</digi:trn>';
	var fwdBtn='<digi:trn>Forward</digi:trn>';
	var deleteBtn='<digi:trn>Delete</digi:trn>';
	var pagesTrn='<digi:trn>Pages</digi:trn>';
	var ofTrn='<digi:trn>of</digi:trn>';
	var firstPage='<digi:trn>click here to go to first page</digi:trn>';
	var prevPage='<digi:trn>click here to go to previous page</digi:trn>';
	var nextPage='<digi:trn>Click here to go to next page</digi:trn>';
	var lastPg='<digi:trn>click here to go to last page</digi:trn>';
	var referenceURL='<digi:trn>Reference URL</digi:trn>';
	var attachmedFiles='<digi:trn>Attached Files</digi:trn>';
    var forwardClick='<digi:trn> Click on this icon to forward message&nbsp;</digi:trn>';
    var editClick='<digi:trn> Click on this icon to edit message&nbsp;</digi:trn>';
    var replyClick='<digi:trn> Click on this icon to reply message&nbsp;</digi:trn>';
    var deleteClick='<digi:trn> Click on this icon to delete message&nbsp;</digi:trn>';
    var viewMessage='<digi:trn> Click here to view the message</digi:trn>';
    var viewDetails='<digi:trn>Click here to view details</digi:trn>';
    
    msgHeaders = {
    		msgTitle : '<digi:trn>Message Title</digi:trn>',
    		msgActions : '<digi:trn>Actions</digi:trn>'
    }
    
    viewOrHideAllLabel = {
    		vAll : '<digi:trn jsFriendly="true">View All</digi:trn>' ,
    		hAll : '<digi:trn jsFriendly="true">Hide All</digi:trn>'
    }
    
    alertForEmptySelection = {
    		selectAlert :'<digi:trn>Please Select Messages</digi:trn>'
    }
    
	//used to define whether we just entered page from desktop
	//var firstEntry=0;
	var firstEntry=1;
	var currentPage=1;
    var messages;
	var slMsgId;
	var lastTimeStamp;
	//used to hold already rendered messages
	var myArray=new Array();
	
	//window.onload=getMessages;
	</script>

<!-- MAIN CONTENT PART START -->


	<div id="tabs-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
	<digi:instance property="messageForm"/>
		<digi:form action="/messageActions.do">
		<html:hidden name="messageForm" property="msgRefreshTimeCurr"/>
		<html:hidden name="messageForm" property="tabIndex"/>
		<html:hidden name="messageForm" property="childTab"/>
		<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>
		
		
		<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
		
		<script language="javascript" type="text/javascript">
			//setting timer to check for new messages after specified time	
			if(document.getElementsByName('msgRefreshTimeCurr')[0].value>0){
				id=window.setTimeout("checkForNewMessages()",60000*document.getElementsByName('msgRefreshTimeCurr')[0].value,"JavaScript");
			}
		</script>	
		
		<div class="tab_opt_box">
				<div class="show_hide_setting"><img src="/TEMPLATE/ampTemplate/img_2/ico_write.png" align=left style="margin-right:5px;">
					<digi:link href="/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels"><b><digi:trn>Create new message</digi:trn></b></digi:link>
				</div>
			
				<c:if test="${messageForm.tabIndex  == 1 || messageForm.tabIndex  == 2}">
					<div id="subtabContainer" class="tab_opt">
				</c:if>
				<c:if test="${messageForm.tabIndex  != 1 && messageForm.tabIndex  != 2}">
					<div id="subtabContainer" class="tab_opt" style="visibility:hidden">
				</c:if>
				
						<div class="tab_opt_cont">
							<span id="tab_inbox">
								<c:if test="${messageForm.childTab=='inbox'}">
									<b class="sm_sel"><digi:trn>Inbox</digi:trn></b> 
								</c:if>
								<c:if test="${messageForm.childTab!='inbox'}">
									<a href="#tab_inbox" class="l_sm"><digi:trn>Inbox</digi:trn></a> 
								</c:if>
							</span>
							&nbsp;|&nbsp;
								
							<span id="tab_sent">
								<c:if test="${messageForm.childTab=='sent'}">
									<b class="sm_sel"><digi:trn>Sent</digi:trn></b> 
								</c:if>
								<c:if test="${messageForm.childTab!='sent'}">
									<a href="#tab_sent" class="l_sm"><digi:trn>Sent</digi:trn></a>
								</c:if>
							</span>
							&nbsp;|&nbsp; 
							
							<span id="tab_draft">
								<c:if test="${messageForm.childTab=='draft'}">
									<b class="sm_sel"><digi:trn>Draft</digi:trn></b>
								</c:if>
								<c:if test="${messageForm.childTab!='draft'}">
									<a href="#tab_draft" class="l_sm"><digi:trn>Draft</digi:trn></a>
								</c:if>
							</span>
							
						</div>
					</div>
					</div>
				
			

		
		

		<div class="paging">
	
		<table><tr id="paginationPlace"><td>&nbsp;</td></tr></table>
		</div>
		<table class="inside" width=740 cellpadding="0" cellspacing="0" id="msgsList">
			<tr><td></td></tr>
		</table>
<br />
<input type="button" onclick="deleteMessage()" value="<digi:trn>Delete selected messages</digi:trn>" class="buttonx_sm" />
<input type="button" onclick="markMessageAsRead()" value="<digi:trn>Mark as read</digi:trn>" class="buttonx_sm" />





<!-- MAIN CONTENT PART END -->
<script language="javascript">
$(document).ready(function(){
	
//	$("#tabs").tabs();
//	$("#filter_tabs").tabs();
//		console.log($("#tabs"));

	$("#tabs>ul>li>a").unbind("click");
	$("#tabs>ul>li>a").bind("click", tabsClick);
	

	

	function tabsClick (event){
		var selTab = $(this).attr("href");
		$("#tabs ul li").removeClass("ui-tabs-selected").removeClass("ui-state-active");
		$(this).parent().addClass("ui-tabs-selected").addClass("ui-state-active");
		tabIndex = parseInt(selTab.substring(6));
		
		if (tabIndex == 1 || tabIndex == 2) {
			$("div #subtabContainer").css("visibility", "visible");
		} else if (tabIndex == 3 || tabIndex == 4) {
			$("div #subtabContainer").css("visibility", "hidden");
		}
		
		currentPage = 1;
		switchBoxTab('#tab_inbox');
		
		
		
		/*
		if (selTab == "#tabs-1") {
				tabIndex = 1;
		} else if (selTab == "#tabs-2") {
				tabIndex = 2;
		} else if (selTab == "#tabs-3") {
				tabIndex = 3;
		} else if (selTab == "#tabs-4") {
				tabIndex = 5;
		}*/
		getMessages();
		return false;
	};

	function switchBoxTab(switchToTabID) {
		childTab = switchToTabID.substring(5);
		var selectedTab = $(".tab_opt>.tab_opt_cont b");
		selectedTab.parent().html("<a class='l_sm' href='#" + selectedTab.parent().attr("id") + "'>" + selectedTab.text() + "</a>");
		$(switchToTabID).html('<b class="sm_sel">' + $(switchToTabID + '>a').text() + '</b>');
		$(".tab_opt_cont a").unbind("click");
		$(".tab_opt_cont a").bind("click", boxTabClick);
	}

	$(".tab_opt_cont a").bind("click", boxTabClick);
		
	function boxTabClick (obj){
		var selector = $(this).attr("href");
		switchBoxTab(selector);
		currentPage = 1;
		getMessages();
		return false;
		}		
	
})

</script>
</digi:form>
</div>
