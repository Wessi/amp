<%@ page pageEncoding="UTF-8"%>
<%@ page import="org.digijava.module.aim.helper.*"%>
<%@ page import="org.digijava.module.aim.helper.ChartGenerator"%>
<%@ page import="java.io.PrintWriter,java.util.*"%>
<%@ page import="org.digijava.module.aim.util.TeamMemberUtil"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/aim" prefix="aim"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>

<jsp:include page="activityHistoryUtil.jsp" flush="true" />
<%@page import="java.math.BigDecimal"%>
<style type="text/css">
	.legend_label a.trnClass { color:yellow;}
}
</style>

<script language="JavaScript1.2" type="text/javascript"src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/panel/yahoo-min.js"/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" ></script>
<script type="text/javascript" src="<digi:file src="script/ajaxconnection/connection-min.js"/>"></script>


	<DIV id="TipLayer"
		style="visibility: hidden; position: absolute; z-index: 1000; top: -100;">
	</DIV>

<digi:instance property="aimEditActivityForm" />

<%
	//Quick fix AMP-6573 please check it
	if (request.getParameter("currentlyEditing") != null) {
%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<%
	}
%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:out value="${aimEditActivityForm.esriapiurl}"/>/jsapi/arcgis/2.2/js/dojo/dijit/themes/claro/claro.css">
<script type="text/javascript" src="<c:out value="${aimEditActivityForm.esriapiurl}"/>/jsapi/arcgis/?v=2.2"></script>




<script language="JavaScript">

function exportToPdf (actId) {
	var href="/aim/exportActToPDF.do?activityid="+actId;
	if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
		var popupName = "popup"+new Date().getTime();	
		var popupWindow =  window.open("", popupName, "height=500,width=780,menubar=no,scrollbars=yes,resizable");
		var referLink = document.createElement('a');
		referLink.href = href;
		referLink.target = popupName;
		document.body.appendChild(referLink);
		referLink.click();
		
	}
	else{
		openURLinResizableWindow(href, 780, 500);
	}
	
}

function exportToWord (actId) {
	var href="/aim/exportActToWord.do?activityid="+actId;
	if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
		var popupName = "popup"+new Date().getTime();	
		var popupWindow =  window.open("", popupName, "height=500,width=780,menubar=no,scrollbars=yes,resizable");
		var referLink = document.createElement('a');
		referLink.href = href;
		referLink.target = popupName;
		document.body.appendChild(referLink);
		referLink.click();
		
	}
	else{
		openURLinResizableWindow(href, 780, 500);
	}
	
}

function gotoStep(value) {
	document.aimEditActivityForm.step.value = value;
	<digi:context name="step" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%=step%>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function backClicked() {
	document.aimEditActivityForm.step.value = "8";
	<digi:context name="backStep" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%=backStep%>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function disable() {
	document.aimEditActivityForm.submitButton.disabled = true;
	document.aimEditActivityForm.backButton.disabled = true;
	var appstatus = document.getElementById('approvalStatus').value;
	var wTLFlag   = document.getElementById('workingTeamLeadFlag').value;
	var msg='';
	if (appstatus == "started") {
		msg+='<digi:trn key="aim:saveActivity:started">Do you want to submit this activity for approval ?</digi:trn>';
		if (wTLFlag == "yes") {
			//if (confirm("Do you want to approve this activity ?"))
				document.getElementById('approvalStatus').value = "approved";
		}
		else if (confirm(msg))
				document.getElementById('approvalStatus').value = "created";
	}
	if (appstatus == "approved") {
		msg+='<digi:trn key="aim:saveActivity:approved">Do you want to approve this activity ?</digi:trn>';
		if (wTLFlag != "yes")
			document.getElementById('approvalStatus').value = "edited";
	}
	else if (wTLFlag == "yes") {
		if (appstatus == "created" || appstatus == "edited") {
			if (confirm(msg))
				document.getElementById('approvalStatus').value = "approved";
		}
	}
	document.aimEditActivityForm.submit();
	return true;
}

function viewChanges(){
	openNewWindow(650,200);
	<digi:context name="showLog" property="context/module/moduleinstance/showActivityLog.do" />
	popupPointer.document.location.href = "<%=showLog%>?activityId=${aimEditActivityForm.activityId}";
}

function editActivity() {
	<digi:context name="editActivity" property="/wicket/onepager/activity" />
	document.location.href="<%=editActivity%>/<%=request.getAttribute("actId")%>";
}

function expandAll(){
   	$("img[id$='_minus']").show();
	$("img[id$='_plus']").hide();	
	$("div[id$='_dots']").hide();
	$("div[id^='act_']").show('fast');
}

function collapseAll() {
	$("img[id$='_minus']").hide();
	$("img[id$='_plus']").show();	
	$("div[id$='_dots']").show();
	$("div[id^='act_']").hide();
}

	showZoomedMap = function(show) {
		var containerObj = $("#zoomMapContainer");
		if (show) {
			document.body.appendChild(containerObj[0]);
			containerObj.show();
			var containerWidth = containerObj.outerWidth();
			var containerHeight = containerObj.outerHeight();
			
			var wndWidth = window.innerWidth;
			var wndHeight = window.innerHeight;
			var wndScrollX = window.pageXOffset;
			var wndScrollY = window.pageYOffset;
			
			var moveToX = (wndWidth - wndScrollX)/2 - containerWidth/2;
			var moveToY = wndHeight/2 + wndScrollY - containerHeight/2;
			
			containerObj.css("left", moveToX + "px");
			containerObj.css("top", moveToY + "px");
		} else {
			containerObj.hide();
		}	
	}
	var coordinates = new Array();
	
	
	
	YAHOO.namespace("YAHOO.amp");

		var myPanel = new YAHOO.widget.Panel("indicatorChartPopin", {
			width:"600px",
			fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true
		    });
	
	
</script>

<%
	Long actId = (Long) request.getAttribute("actId");
	String url = "/aim/viewIndicatorValues.do?ampActivityId=" + actId+ "&tabIndex=6";
	String actPerfChartFileName = null;
	String actPerfChartUrl = null;
	String actRiskChartUrl = null;
	String actRiskChartFileName = null;
	if(actId != null ){
		
	try {
		actPerfChartFileName = ChartGenerator
				.getActivityPerformanceChartFileName(actId, session,
						new PrintWriter(out), 370, 450, url, true,
						request);
	} catch (Exception e) {
		e.printStackTrace();
	}
		
	if (actPerfChartFileName != null) {
			actPerfChartUrl = request.getContextPath()+ "/aim/DisplayChart.img?filename="+ actPerfChartFileName;
	}

		
	try {

			actRiskChartFileName = ChartGenerator.getActivityRiskChartFileName(actId, session,new PrintWriter(out), 370, 350, url);
	} catch (Exception e) {
		e.printStackTrace();
	}


	if (actRiskChartFileName != null) {
		actRiskChartUrl = request.getContextPath()
				+ "/aim/DisplayChart.img?filename="
				+ actRiskChartFileName;
	}
	}
	
%>

<digi:context name="digiContext" property="context" />
<digi:form action="/saveActivity.do" method="post">
	<html:hidden property="step" />
	<html:hidden property="editAct" />
	<html:hidden property="identification.approvalStatus"styleId="approvalStatus"/>
	<html:hidden property="workingTeamLeadFlag"styleId="workingTeamLeadFlag"/>
	

<!-- MAIN CONTENT PART START -->

<logic:present scope="request" parameter="editError">
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center style="margin-top:15px;">
	     <tr>
		     <td align="center">
		        <font color="red" size="3">
		                <digi:trn key="aim:activityIsBeeingEdited">Current activity is being edited by:</digi:trn> <%= TeamMemberUtil.getTeamMember(Long.valueOf(request.getParameter("editError"))).getMemberName() %>
		        </font>
		     </td>
	     </tr>           
	     <tr>
	         <td>&nbsp;
	             
	         </td>
	     </tr>
	</table>
</logic:present>
<c:if test="${aimEditActivityForm.activityExists=='no'}">
	<div class="activity_preview_header" style="font-size: 12px;text-align: center;color:red">
		<ul style="padding-top: 5px;font-size: 12px">
			<li><digi:trn>Couldn't find activity! It may be no longer exists in the system. </digi:trn></li>
		</ul>	
	</div>
</c:if>
<c:if test="${aimEditActivityForm.activityExists!='no'}">

	<div class="activity_preview_header">
	  <table width="990" border="0" cellpadding="0" cellspacing="0">
	  <tr>
	    <td width="60%">
	    	<div class="activity_preview_name"><c:out value="${aimEditActivityForm.identification.title}"/></div>
            <div style="clear:both;"></div>
			<div class="l_sm">
			 	<font color="red">
			 		<jsp:include page="utils/amountUnitsUnformatted.jsp">
						<jsp:param value="" name="amount_prefix"/>
					</jsp:include>
				</font>
	    	</div>
            <div style="clear:both;"></div>

	    </td>
		<logic:present name="currentMember" scope="session">
	    
	    <td align=right width=40%>
	    	<c:set var="trn">
				<digi:trn>Export To PDF</digi:trn>
			</c:set> 
			<a onclick="javascript:exportToPdf(${actId})" class="l_sm" style="cursor: pointer; color:#376091;" title="${trn}">
				<img src="img_2/ico_pdf.gif" />${trn}
			</a>
			<a onclick="javascript:exportToWord(${actId})" class="l_sm" style="cursor: pointer; color:#376091;">
				<img src="img_2/ico_word.gif" />
				<digi:trn>Export to Word</digi:trn>
			</a>
	    	<a onclick="window.open('/showPrinterFriendlyPage.do?edit=true', '_blank', '');" class="l_sm" style="cursor: pointer; color:#376091;" title="<digi:trn key="aim:print">Print</digi:trn>">
	    		<img src="img_2/ico_print.gif" width="15" height="18" />
	    		<digi:trn key="aim:print">Print</digi:trn>
	    	</a>
	    </td>
	    </logic:present>
	  </tr>
	</table>
	</div>
	
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center style="margin-top:15px;">
  <tr>
    <td width=215 bgcolor="#F4F4F4" valign=top>
	<div class="dash_left">
	<fieldset>
		<legend>
			<span class=legend_label>
				<digi:trn>Funding Information</digi:trn>&nbsp; 
				(${aimEditActivityForm.currCode})			
			</span>		
		</legend>
		<div class="field_text_big">
			<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Commitments" 
														parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
			<c:if test="${aimEditActivityForm.funding.showActual}">
			<digi:trn>Total Actual Commitment</digi:trn>:<br/> 
				<c:if test="${not empty aimEditActivityForm.funding.totalCommitments}">
					<b>
		                 <bean:write name="aimEditActivityForm" property="funding.totalCommitments" /> 
		                 <bean:write name="aimEditActivityForm" property="currCode" />
	            	</b>		         	
		         </c:if>    
		         <c:if test="${empty aimEditActivityForm.funding.totalCommitments}">
			         <b>
			         0
			         <bean:write name="aimEditActivityForm" property="currCode" />
			         </b>
		         </c:if>
			<hr/>
			</c:if>
			<c:if test="${aimEditActivityForm.funding.showPlanned}">
			<digi:trn>Total Planned Commitment</digi:trn>:<br/>
				<c:if test="${not empty aimEditActivityForm.funding.totalPlannedCommitments}">
					<b>
					 <bean:write name="aimEditActivityForm" property="funding.totalPlannedCommitments" /> 
		             <bean:write name="aimEditActivityForm" property="currCode" />
                	</b>
                </c:if>
                <c:if test="${empty aimEditActivityForm.funding.totalPlannedCommitments}">
                	<b>
			         0
			         <bean:write name="aimEditActivityForm" property="currCode" />
			         </b>
		         </c:if>
		         <hr/>
		         </c:if>
		       </module:display>
			
			<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Disbursements" 
														parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
			<c:if test="${aimEditActivityForm.funding.showActual}">
			<digi:trn>Total Actual Disbursements</digi:trn>:<br/>
				<c:if test="${not empty aimEditActivityForm.funding.totalDisbursements}">
	                <b>
	                 <bean:write name="aimEditActivityForm" property="funding.totalDisbursements" /> 
	                 <bean:write name="aimEditActivityForm" property="currCode" />
	                </b>		          
		         </c:if>
		         <c:if test="${empty aimEditActivityForm.funding.totalDisbursements}">
			         <b>
			         0
			         <bean:write name="aimEditActivityForm" property="currCode" />
			         </b>
		         </c:if>
			<hr/>
			</c:if>
			<c:if test="${aimEditActivityForm.funding.showPlanned}">
			<digi:trn>Total Planned Disbursements</digi:trn>:<br/>
				<c:if test="${not empty aimEditActivityForm.funding.totalPlannedDisbursements}">
	                <b>
	                 <bean:write name="aimEditActivityForm" property="funding.totalPlannedDisbursements" /> 
	                 <bean:write name="aimEditActivityForm" property="currCode" />
	                </b>		          
		         </c:if>
		         <c:if test="${empty aimEditActivityForm.funding.totalPlannedDisbursements}">
			         <b>
			         0
			         <bean:write name="aimEditActivityForm" property="currCode" />
			         </b>
		         </c:if>
			<hr/>
			</c:if>
			</module:display>
			<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Expenditures" 
														parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
			<c:if test="${aimEditActivityForm.funding.showActual}">
			<digi:trn>Total Expenditures</digi:trn>:<br/>
				<c:if test="${not empty aimEditActivityForm.funding.totalExpenditures}">
	                <b>
	                 <bean:write name="aimEditActivityForm" property="funding.totalExpenditures" /> 
	                 <bean:write name="aimEditActivityForm" property="currCode" />
	                </b>		          
		         </c:if>
		          <c:if test="${empty aimEditActivityForm.funding.totalExpenditures}">
			         <b>
			         0
			         <bean:write name="aimEditActivityForm" property="currCode" />
			         </b>
		         </c:if>
			<hr/>
			</c:if>
			<c:if test="${aimEditActivityForm.funding.showPlanned}">
			<digi:trn>Total Planned Expenditures</digi:trn>:<br/>
				<c:if test="${not empty aimEditActivityForm.funding.totalPlannedExpenditures}">
	                <b>
	                 <bean:write name="aimEditActivityForm" property="funding.totalPlannedExpenditures" /> 
	                 <bean:write name="aimEditActivityForm" property="currCode" />
	                </b>		          
		         </c:if>
		         <c:if test="${empty aimEditActivityForm.funding.totalPlannedExpenditures}">
			         <b>
			         0
			         <bean:write name="aimEditActivityForm" property="currCode" />
			         </b>
		         </c:if>
			<hr/>
			</c:if>
			</module:display>
			<field:display name="Duration of Project" feature="Planning">
				<img src="../ampTemplate/images/help.gif" title="<digi:trn>Actual Completion Date - Actual Start Date</digi:trn>" width="10" height=10 border="0">
				<digi:trn>Duration of project</digi:trn>: <br/>
				<b>${aimEditActivityForm.planning.projectPeriod }&nbsp; </b><digi:trn>Months</digi:trn>
				<hr/>
			</field:display>
			<field:display name="Delivery rate" feature="Funding Information">
				<img src="../ampTemplate/images/help.gif" title="<digi:trn>Actual Disbursements / Actual Commitments * 100</digi:trn>" width="10" height=10 border="0">
				<digi:trn>Delivery rate</digi:trn>:<br/>
				<b> ${aimEditActivityForm.funding.deliveryRate}</b>
				<hr/>
			</field:display> 
			<field:display name="Consumption rate" feature="Funding Information">
				<img src="../ampTemplate/images/help.gif" title="<digi:trn>Actual Expenditures / Actual Disbursements * 100</digi:trn>" width="10" height=10 border="0">
				<digi:trn>Consumption rate</digi:trn>:<br/>
				<b>${aimEditActivityForm.funding.consumptionRate}</b>		</div>
			</field:display>
	</fieldset>	
	<fieldset>
	<legend>
		<span class=legend_label><digi:trn>Additional info</digi:trn></span>	</legend>
	<div class="field_text_big">
	<digi:trn>Activity created by</digi:trn>: <br/>
		<b> 
			<c:out value="${aimEditActivityForm.identification.actAthFirstName}"/> 
			<c:out value="${aimEditActivityForm.identification.actAthLastName}"/> 
		</b>
		<hr/>
	<digi:trn>Created in workspace</digi:trn>: <br />
	<b>
		<c:out value="${aimEditActivityForm.identification.team.name}"/> - 
		<c:out value="${aimEditActivityForm.identification.team.accessType}"/>
	</b>
	<hr />
 	<digi:trn>Computation</digi:trn>: <br/>
	<b>
		<c:if test="${aimEditActivityForm.identification.team.computation == 'true'}">
			<digi:trn key="aim:yes">Yes</digi:trn>
		</c:if> 
		<c:if test="${aimEditActivityForm.identification.team.computation == 'false'}">
			<digi:trn key="aim:no">No</digi:trn>
		</c:if>
	</b>
	<hr/>
	<digi:trn>Activity created on</digi:trn>:<br/>
	<b><c:out value="${aimEditActivityForm.identification.createdDate}"/></b>
	<hr />
	<field:display name="Data Team Leader" feature="Identification">
		<digi:trn>Data Team Leader</digi:trn>: <br />
		<b>
			<c:out value="${aimEditActivityForm.identification.team.teamLead.user.firstNames}"/> 
			<c:out value="${aimEditActivityForm.identification.team.teamLead.user.lastName}"/>
			<c:out value="${aimEditActivityForm.identification.team.teamLead.user.email}"/>
		</b>	
	</field:display>
	</div>
</fieldset>	
	</div>	</td>
	<td width=15>&nbsp;</td>
    <td width=689 bgcolor="#F4F4F4" valign=top style="border:1px solid #DBDBDB">
	<div style="padding:10px; font-size:12px;">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
    	<input type="button" value="<digi:trn>Collapse All</digi:trn>" class="buttonx_sm" id="collapseall">    
    </td>
    <td align=right>
		<logic:present name="currentMember" scope="session">
	    <c:set var="trn"><digi:trn>Version History</digi:trn></c:set>		
	    <input type="button" class="buttonx_sm" onclick="javascript:previewHistory(<%=request.getAttribute("actId")%>); return false;" value="${trn}"/>
	    <module:display name="Previews" parentModule="PROJECT MANAGEMENT">
			<feature:display name="Edit Activity" module="Previews">
				<field:display feature="Edit Activity" name="Edit Activity Button">
					<logic:equal name="aimEditActivityForm" property="buttonText" value="edit">
						<c:set var="trn"><digi:trn>Edit</digi:trn></c:set>
						<input type="button" class="buttonx_sm" onclick="javascript:editActivity()" value="${trn}"/>
					</logic:equal>
					<logic:equal name="aimEditActivityForm" property="buttonText" value="validate">
						<c:set var="trn"><digi:trn>Validate</digi:trn></c:set>							
						<input type="button" class="buttonx_sm" onclick="javascript:editActivity()" value="${trn}"/>
					</logic:equal>
				</field:display>
			</feature:display>
		</module:display>
		</logic:present>    
		</td>
  </tr>
  <tr>
      <td colspan="2" style="color:red;text-align: center">
          <c:forEach var="element" items="${aimEditActivityForm.warningMessges}">
              <digi:trn>${element}</digi:trn>
          </c:forEach>
      </td>
  </tr>
</table>
		
<!-- IDENTIFICATION SECTION -->
<module:display name="/Activity Form/Identification" parentModule="/Activity Form">
	<fieldset>
		<legend>
			<span class=legend_label id="identificationlink" style="cursor: pointer;">
				<digi:trn>Identification</digi:trn>
			</span>		
		</legend>
		<div id="identificationdiv">	
			<digi:trn key="aim:ampId">AMP ID</digi:trn>:&nbsp;<br /><b><c:out value="${aimEditActivityForm.identification.ampId}"/></b> <br>
			<hr />	
			<module:display name="/Activity Form/Identification/Project Title" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:projectTitle">Project title</digi:trn>:&nbsp;<br />
				<b><c:out value="${aimEditActivityForm.identification.title}"/></b>
				<hr />
			</module:display>
			
			<module:display name="/Activity Form/Identification/Status Reason" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:status">Status</digi:trn>:&nbsp;<br />
				<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.statusId}"/></b>
				<hr />
				<c:if test="${not empty aimEditActivityForm.identification.statusReason}">
				<b>${aimEditActivityForm.identification.statusReason}</b>
				<hr />
				</c:if>
			</module:display>
			
			<module:display name="/Activity Form/Identification/Objective" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:objectives">Objectives</digi:trn>:&nbsp;<br />
				<c:if test="${aimEditActivityForm.identification.objectives!=null}">
					<b><c:set var="objKey" value="${aimEditActivityForm.identification.objectives}"/>
					<digi:edit key="${objKey}"></digi:edit></b>
				</c:if>
					<hr />
			</module:display>
			
			<module:display name="/Activity Form/Identification/Objective Comments" parentModule="/Activity Form/Identification">
				<logic:present name="currentMember" scope="session">
					<digi:trn key="aim:objectiveComments">Objective Comments</digi:trn>:&nbsp;
						<ul>
						<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
							<module:display name="/Activity Form/Identification/Objective Comments/Objective Assumption" parentModule="/Activity Form/Identification/Objective Comments">
								<logic:equal name="comments" property="key" value="Objective Assumption">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
											<digi:trn>Objective Assumption</digi:trn>:&nbsp;<br />								
											<b><bean:write name="comment" property="comment" /></b>
											<br />
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>	
							<module:display name="/Activity Form/Identification/Objective Comments/Objective Verification" parentModule="/Activity Form/Identification/Objective Comments">
								<logic:equal name="comments" property="key" value="Objective Verification">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
											<digi:trn>Objective Verification</digi:trn>:&nbsp;	<br />								
											<b><bean:write name="comment" property="comment" /></b>
										<br/>
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>							
							<module:display name="/Activity Form/Identification/Objective Comments/Objective Objectively Verifiable Indicators" 
								parentModule="/Activity Form/Identification/Objective Comments">
								<logic:equal name="comments" property="key" value="Objective Objectively Verifiable Indicators">
									<logic:iterate name="comments" id="comment" property="value" 
										type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
											<digi:trn>Objective Objectively Verifiable Indicators</digi:trn>:&nbsp;	<br />									
											<b><bean:write name="comment" property="comment"/></b>
											<br/>
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>
						</logic:iterate>
						</ul>
						<hr/>
					</logic:present>
			</module:display>
			
			<module:display name="/Activity Form/Identification/Description" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:description">Description</digi:trn>:&nbsp;<br />
				<c:if test="${aimEditActivityForm.identification.description!=null}">
					<b><c:set var="descKey" value="${aimEditActivityForm.identification.description}"/>
					<digi:edit key="${descKey}"></digi:edit></b>
				</c:if>
					<hr />
			</module:display>
			
			<module:display name="/Activity Form/Identification/Project Comments" parentModule="/Activity Form/Identification">
				<digi:trn>Project Comments</digi:trn>:&nbsp;<br />
				<c:if test="${aimEditActivityForm.identification.projectComments!=null}">
					<b><c:set var="projcomKey" value="${aimEditActivityForm.identification.projectComments}"/>
					<digi:edit key="${projcomKey}"></digi:edit></b>
				</c:if>
					<hr />
			</module:display>
					
			<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
<!--			<field:display name="NPD Clasification" feature="Identification">-->
<!--				<digi:trn>NPD Clasification</digi:trn>:&nbsp;<br />-->
<!--				<b><c:out value="${aimEditActivityForm.identification.clasiNPD}"/></b>-->
<!--				<hr />-->
<!--			</field:display>-->
			
			<module:display name="/Activity Form/Identification/Lessons Learned" parentModule="/Activity Form/Identification">
				<digi:trn>Lessons Learned</digi:trn>:&nbsp;<br />
				<c:if test="${not empty aimEditActivityForm.identification.lessonsLearned}">
					<bean:define id="lessonsLearnedKey">
						<c:out value="${aimEditActivityForm.identification.lessonsLearned}"/>
					</bean:define>
					<b><digi:edit key="${lessonsLearnedKey}"></digi:edit></b>
				</c:if>
					<hr />
			</module:display>
					
			<bean:define id="largeTextFeature" value="Identification" toScope="request"/>
			<logic:present name="aimEditActivityForm" property="identification.projectImpact">
				<bean:define id="moduleName" value="/Activity Form/Identification/Project Impact" toScope="request"/>
				<bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
				<bean:define id="largeTextLabel" value="Project Impact" toScope="request"/>
				<bean:define id="largeTextKey" toScope="request">
					<c:out value="${aimEditActivityForm.identification.projectImpact}"/>
				</bean:define>
				<jsp:include page="largeTextPropertyView.jsp" />
			</logic:present>
			
		 	<logic:present name="aimEditActivityForm" property="identification.activitySummary"> 
				<bean:define id="moduleName" value="/Activity Form/Identification/Activity Summary" toScope="request"/>
				<bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
				<bean:define id="largeTextLabel" value="Activity Summary" toScope="request"/>
				<bean:define id="largeTextKey" toScope="request">
					<c:out value="${aimEditActivityForm.identification.activitySummary}"/>
				</bean:define>
				<jsp:include page="largeTextPropertyView.jsp" />
		 	</logic:present> 
			<logic:present name="aimEditActivityForm" property="identification.contractingArrangements">
				<bean:define id="moduleName" value="/Activity Form/Identification/Contracting Arrangements" toScope="request"/>
				<bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
				<bean:define id="largeTextLabel" value="Contracting Arrangements" toScope="request"/>
				<bean:define id="largeTextKey" toScope="request">
					<c:out value="${aimEditActivityForm.identification.contractingArrangements}"/>
				</bean:define>
				<jsp:include page="largeTextPropertyView.jsp" />
			</logic:present>
			<logic:present name="aimEditActivityForm" property="identification.condSeq">
				<bean:define id="moduleName" value="/Activity Form/Identification/Conditionality and Sequencing" toScope="request"/>
				<bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
				<bean:define id="largeTextLabel" value="Conditionality and Sequencing" toScope="request"/>
				<bean:define id="largeTextKey" toScope="request">
					<c:out value="${aimEditActivityForm.identification.condSeq}" />
				</bean:define>
				<jsp:include page="largeTextPropertyView.jsp" />
			</logic:present>
			<logic:present name="aimEditActivityForm" property="identification.linkedActivities">
				<bean:define id="moduleName" value="/Activity Form/Identification/Linked Activities" toScope="request"/>
				<bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
				<bean:define id="largeTextLabel" value="Linked Activities" toScope="request"/>
				<bean:define id="largeTextKey" toScope="request">
					<c:out value="${aimEditActivityForm.identification.linkedActivities}"/>
				</bean:define>
				<jsp:include page="largeTextPropertyView.jsp" />
			</logic:present>
			<logic:present name="aimEditActivityForm" property="identification.conditionality">
				<bean:define id="moduleName" value="/Activity Form/Identification/Conditionalities" toScope="request"/>
				<bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
				<bean:define id="largeTextLabel" value="Conditionalities" toScope="request"/>
				<bean:define id="largeTextKey" toScope="request">
					<c:out value="${aimEditActivityForm.identification.conditionality}"/>
				</bean:define>
				<jsp:include page="largeTextPropertyView.jsp" />
			</logic:present>
			<logic:present name="aimEditActivityForm" property="identification.projectManagement">
				<bean:define id="moduleName" value="/Activity Form/Identification/Project Management" toScope="request"/>
				<bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
				<bean:define id="largeTextLabel" value="Project Management" toScope="request"/>
				<bean:define id="largeTextKey" toScope="request">
					<c:out value="${aimEditActivityForm.identification.projectManagement}"/>
				</bean:define>
				<jsp:include page="largeTextPropertyView.jsp" />
			</logic:present>
			<module:display name="/Activity Form/Identification/Purpose" parentModule="/Activity Form/Identification">
				<digi:trn >Purpose</digi:trn>:<br />
				<c:if test="${aimEditActivityForm.identification.purpose!=null}">
					<b><c:set var="objKey" value="${aimEditActivityForm.identification.purpose}"/>
					<digi:edit key="${objKey}"></digi:edit></b>
				</c:if>
				<hr/>
			</module:display>
			<module:display name="/Activity Form/Identification/Purpose Comments" parentModule="/Activity Form/Identification">
				<logic:present name="aimEditActivityForm" property="comments.allComments">
					<digi:trn>Purpose Comments</digi:trn>&nbsp;
					<ul>
					<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
						<module:display name="/Activity Form/Identification/Purpose Comments/Purpose Assumption" parentModule="/Activity Form/Identification/Purpose Comments">
						<logic:equal name="comments" property="key" value="Purpose Assumption">
							<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
								 <li>
								 <digi:trn key="aim:purposeAssumption">Purpose Assumption</digi:trn>:<br />
								<b><bean:write name="comment" property="comment" /></b>
								<br />
								</li>
							</logic:iterate>
						</logic:equal>
						</module:display>
						<module:display name="/Activity Form/Identification/Purpose Comments/Purpose Verification" parentModule="/Activity Form/Identification/Purpose Comments">
						<logic:equal name="comments" property="key" value="Purpose Verification">
							<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
								 <li>
								 <digi:trn key="aim:purposeVerification">Purpose Verification</digi:trn>:<br />
								<b><bean:write name="comment" property="comment" /></b>
								<br/>
								</li>
							</logic:iterate>
						</logic:equal>
						</module:display>
						<module:display name="/Activity Form/Identification/Purpose Comments/Purpose Objectively Verifiable Indicators" parentModule="/Activity Form/Identification/Purpose Comments">
						<logic:equal name="comments" property="key" value="Purpose Objectively Verifiable Indicators">
							<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
								<li>
								<digi:trn key="aim:purposeObjectivelyVerifiableIndicators">Purpose Objectively Verifiable Indicators</digi:trn>:<br />
								<b><bean:write name="comment" property="comment" /></b>
								<br/>
								</li>
							</logic:iterate>
						</logic:equal>
						</module:display>
					</logic:iterate>
					</ul> 
					<hr/>
				</logic:present>
			</module:display>
					
			<module:display name="/Activity Form/Identification/Results" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:results">Results</digi:trn>:&nbsp;<br />
					<c:if test="${aimEditActivityForm.identification.results!=null}">
						<b><c:set var="objKey" value="${aimEditActivityForm.identification.results}"/></b>
						<b><digi:edit key="${objKey}"></digi:edit></b>
						<hr>
					</c:if> 
			</module:display>					
				<logic:present name="aimEditActivityForm" property="comments.allComments">
					<module:display name="/Activity Form/Identification/Results Comments" parentModule="/Activity Form/Identification">
						<digi:trn>Results Comments</digi:trn>
						<ul>
						<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
							<module:display name="/Activity Form/Identification/Results Comments/Results Assumption" parentModule="/Activity Form/Identification/Results Comments">
								<logic:equal name="comments" property="key" value="Results Assumption">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
										<digi:trn key="aim:resultsAssumption">Results Assumption</digi:trn>:<br />
										<b><bean:write name="comment" property="comment" /></b>
										<br/>
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>
							<module:display name="/Activity Form/Identification/Results Comments/Results Verification" parentModule="/Activity Form/Identification/Results Comments">							
								<logic:equal name="comments" property="key" value="Results Verification">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
										<digi:trn key="aim:resultsVerification">Results Verification</digi:trn>:<br />
										<b><bean:write name="comment" property="comment" /></b>
										<br/>
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>
							<module:display name="/Activity Form/Identification/Results Comments/Results Objectively Verifiable Indicators" parentModule="/Activity Form/Identification/Results Comments">							
								<logic:equal name="comments" property="key" value="Results Objectively Verifiable Indicators">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										 <li>
											<digi:trn key="aim:resultsObjectivelyVerifiableIndicators">Results Objectively Verifiable Indicators</digi:trn>:<br />
										<b><bean:write name="comment" property="comment" /></b>
										<br/>
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>								
						</logic:iterate>
						</ul>
					</module:display>
					<hr/>
				</logic:present>
			
			
			
			<module:display name="/Activity Form/Identification/Accession Instrument" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:AccessionInstrument">Accession Instrument</digi:trn>:&nbsp;<br />
					<c:if test="${aimEditActivityForm.identification.accessionInstrument > 0}">
							<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.accessionInstrument}"/></b>
					</c:if> 
						<hr />
			</module:display>
			
			<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
			<module:display name="/Activity Form/Identification/Project Implementing Unit" parentModule="/Activity Form/Identification">
			
				<digi:trn>Project Implementing Unit</digi:trn><br />
				<c:if test="${aimEditActivityForm.identification.projectImplUnitId > 0}">
					<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectImplUnitId}"/></b>
				</c:if> 
				<hr/>
			</module:display>
					 
			<module:display name="/Activity Form/Identification/A.C. Chapter" parentModule="/Activity Form/Identification">
				<digi:trn>A.C. Chapter</digi:trn><br />
				<c:if test="${aimEditActivityForm.identification.acChapter > 0}">
					<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.acChapter}"/></b>
				</c:if>
					<hr />
			</module:display>
					 
			<module:display name="/Activity Form/Identification/Cris Number" parentModule="/Activity Form/Identification">
				<digi:trn>Cris Number</digi:trn>:&nbsp;<br />
					<b><c:out value="${aimEditActivityForm.identification.crisNumber}"/></b><hr />
			</module:display>
					
			<module:display name="/Activity Form/Identification/Procurement System" parentModule="/Activity Form/Identification">
				<digi:trn>Procurement System</digi:trn><br />
				<c:if test="${aimEditActivityForm.identification.procurementSystem > 0}">
					<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.procurementSystem}"/></b>
				</c:if>
					<hr />
			</module:display>
								
			<module:display name="/Activity Form/Identification/Reporting System" parentModule="/Activity Form/Identification">
				<digi:trn>Reporting System</digi:trn>:&nbsp;<br />
				<c:if test="${aimEditActivityForm.identification.reportingSystem > 0}">
					<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.reportingSystem}"/></b>
				</c:if> 
					<hr />
			</module:display>
								
			<module:display name="/Activity Form/Identification/Audit System" parentModule="/Activity Form/Identification">
				<digi:trn>Audit System</digi:trn>:&nbsp;<br />
				<c:if test="${aimEditActivityForm.identification.auditSystem > 0}">
					<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.auditSystem}"/></b>
				</c:if> 
					<hr />	
			</module:display>
				
			<module:display name="/Activity Form/Identification/Institutions" parentModule="/Activity Form/Identification">
				<digi:trn>Institutions</digi:trn>:&nbsp;<br />
				<c:if test="${aimEditActivityForm.identification.institutions > 0}">
					<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.institutions}"/></b>
				</c:if>
					<hr />
			</module:display>
							
			<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
			<module:display name="/Activity Form/Identification/Project Category" parentModule="/Activity Form/Identification">
				<digi:trn>Project Category</digi:trn><br />
				<c:if test="${aimEditActivityForm.identification.projectCategory > 0}">
					<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectCategory}"/></b>
				</c:if>
					<hr />	
			</module:display>
						 
			<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
			<module:display name="/Activity Form/Identification/Government Agreement Number" parentModule="/Activity Form/Identification">
				<digi:trn>Government Agreement Number</digi:trn>:&nbsp;<br />
				<b><c:out value="${aimEditActivityForm.identification.govAgreementNumber}"/></b><hr /></module:display>
	</module:display>
	<!-- END IDENTIFICATION SECTION -->
	<!-- BUDGET SECTION -->
	<!-- MISSING FIELD IN THE NEW ACTIVITY FORM -->
	
	
		<module:display name="/Activity Form/Identification/Activity Budget" parentModule="/Activity Form/Identification">
		<c:choose>
			<c:when test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOn}">
				<digi:trn>Activity is On Budget</digi:trn><br />
			</c:when>
			<c:when test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOff}">
				<digi:trn>Activity is Off Budget</digi:trn><br />
			</c:when>
			<c:when test="${aimEditActivityForm.identification.budgetCV==0}">
				<digi:trn>Budget Unallocated</digi:trn><br />
			</c:when>
			<c:otherwise>
				<digi:trn>Activity is On</digi:trn>
				<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.budgetCV}"/></b>
			</c:otherwise>
		</c:choose>
		<hr/>
		
		<c:if test="${!empty aimEditActivityForm.identification.chapterForPreview}" >
			<digi:trn>Code Chapitre</digi:trn>:<br />
			<b><bean:write name="aimEditActivityForm" property="identification.chapterForPreview.code" /> - 
			<bean:write name="aimEditActivityForm" property="identification.chapterForPreview.description"/></b>
			<digi:trn>Imputations</digi:trn>:<br />
			<logic:iterate id="imputation" name="aimEditActivityForm" property="identification.chapterForPreview.imputations">
				<b><bean:write name="aimEditActivityForm" property="identification.chapterForPreview.year"/> - 
				<bean:write name="imputation" property="code" /> - 
				<bean:write name="imputation" property="description" /></b>
				<hr>
			</logic:iterate>
		</c:if>
		</module:display>
		
		<module:display name="/Activity Form/Identification/Budget Extras" parentModule="/Activity Form/Identification">
			<c:if test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOn}">
				<module:display name="/Activity Form/Identification/Budget Extras/FY" parentModule="/Activity Form/Identification">
					<digi:trn>FY</digi:trn>:&nbsp;
					<b><bean:write name="aimEditActivityForm" property="identification.FY"/></b>
					<br />
				</module:display>
				<module:display name="/Activity Form/Identification/Budget Extras/Ministry Code"  parentModule="/Activity Form/Identification/Budget Extras">
					<digi:trn>Ministry Code</digi:trn>:&nbsp;
					<b><bean:write name="aimEditActivityForm" property="identification.ministryCode"/></b>
					<br />
				</module:display>
				
				<module:display name="/Activity Form/Identification/Budget Extras/Project Code" parentModule="/Activity Form/Identification/Budget Extras">
					<digi:trn>Project Code</digi:trn>:&nbsp;
					<b><bean:write name="aimEditActivityForm" property="identification.projectCode"/></b>
					<br />
				</module:display>
				
				<module:display name="/Activity Form/Identification/Budget Extras/Vote"  parentModule="/Activity Form/Identification/Budget Extras">
					<digi:trn>Vote</digi:trn>:&nbsp;
					<b><bean:write name="aimEditActivityForm" property="identification.vote"/></b>
					<br />
				</module:display>
					<module:display name="/Activity Form/Identification/Budget Extras/Sub-Vote"  parentModule="/Activity Form/Identification/Budget Extras">
					<digi:trn>Sub-Vote </digi:trn>:&nbsp;
					<b><bean:write name="aimEditActivityForm" property="identification.subVote"/></b>
					<br />
				</module:display>
				<module:display name="/Activity Form/Identification/Budget Extras/Sub-Program" parentModule="/Activity Form/Identification/Budget Extras">
					<digi:trn>Sub-Program</digi:trn>:&nbsp;
					<b><bean:write name="aimEditActivityForm" property="identification.subProgram"/></b>
					<br />
				</module:display>
				
			</c:if>
		</module:display>
		<hr>	
		<module:display name="/Activity Form/Identification/Budget Classification" parentModule="/Activity Form/Identification">
			
			<digi:trn>Budget Classification</digi:trn>:<br />
			<c:if test="${!empty aimEditActivityForm.identification.selectedbudgedsector}">
				<c:forEach var="selectedsector" items="${aimEditActivityForm.identification.budgetsectors}">
					<c:if test="${aimEditActivityForm.identification.selectedbudgedsector==selectedsector.idsector}">
						<li style="margin-left: 10px">
							<b><c:out value="${selectedsector.code}" /> - <c:out value="${selectedsector.sectorname}" /></b>
						</li>
					</c:if>
				</c:forEach>
				<br>
			</c:if>
			
			<c:if test="${!empty aimEditActivityForm.identification.selectedorg}">
				<c:forEach var="selectedorgs" items="${aimEditActivityForm.identification.budgetorgs}">
					<c:if test="${aimEditActivityForm.identification.selectedorg==selectedorgs.ampOrgId}">
						<li style="margin-left: 10px"><c:out value="${selectedorgs.budgetOrgCode}"/> - 
						<b><c:out value="${selectedorgs.name}" /></b></li>
					</c:if>
				</c:forEach>
				<br>
			</c:if>
			
			<c:if test="${!empty aimEditActivityForm.identification.selecteddepartment}">
				<c:forEach var="selecteddep" items="${aimEditActivityForm.identification.budgetdepartments}">
					<c:if test="${aimEditActivityForm.identification.selecteddepartment==selecteddep.id}">
						<li style="margin-left: 10px">
							<b><c:out value="${selecteddep.code}"/> - 
							<c:out value="${selecteddep.name}"/></b>
						</li>
					</c:if>
				</c:forEach>
				<br>
			</c:if>
			
			<c:if test="${!empty aimEditActivityForm.identification.selectedprogram}" >
				<c:forEach var="selectedprog" items="${aimEditActivityForm.identification.budgetprograms}">
					<c:if test="${aimEditActivityForm.identification.selectedprogram==selectedprog.ampThemeId}">
						<li style="margin-left: 10px">
							<b><c:out value="${selectedprog.themeCode}"/> - <c:out value="${selectedprog.name}" /></b>
						</li>
					</c:if>
				</c:forEach>
			</c:if>
			<hr/>
		</module:display>
	
	<!-- END BUDGET SECTION -->
	
	<!-- INDETIFICATION SECTION 2 -->
	<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
	<field:display feature="Identification" name="Organizations and Project ID">
		<digi:trn>Organizations and Project IDs</digi:trn>:&nbsp;<br />
		<c:if test="${!empty aimEditActivityForm.identification.selectedOrganizations}">
			<table cellSpacing=2 cellPadding=2 border="0">
				<c:forEach var="selectedOrganizations" items="${aimEditActivityForm.identification.selectedOrganizations}">
					<c:if test="${not empty selectedOrganizations}">
						<tr>
							<td>
								<c:if test="${!empty selectedOrganizations.organisation.ampOrgId}">
									<b><bean:define id="selectedOrgForPopup" 
										name="selectedOrganizations" 
										type="org.digijava.module.aim.helper.OrgProjectId"
										toScope="request"/></b>
										<jsp:include page="previewOrganizationPopup.jsp" />
								</c:if>							</td>
						</tr>
					</c:if>
				</c:forEach>
			</table><hr />
		</c:if> 
	</field:display>
	
	<module:display name="/Activity Form/Identification/Humanitarian Aid" parentModule="/Activity Form/Identification">
		<digi:trn>Humanitarian Aid</digi:trn>:&nbsp;<br />
		<c:if test="${!aimEditActivityForm.identification.humanitarianAid==true}">
			<b><digi:trn key="aim:no">No</digi:trn></b>
		</c:if> 
		<c:if test="${aimEditActivityForm.identification.humanitarianAid==true}">
			<b><digi:trn key="aim:yes">Yes</digi:trn></b>
		</c:if>
		<hr/>
	</module:display>
	<!-- END INDETIFICATION SECTION 2 -->
	</div>
</fieldset>

<!-- PLANNING SECTION -->
<module:display name="/Activity Form/Planning" parentModule="/Activity Form">	
<fieldset>
	<legend>
		<span class=legend_label id="planninglink" style="cursor: pointer;">
			<digi:trn>Planning</digi:trn>
		</span>
		</legend>
	<div id="planningdiv">
		<module:display name="/Activity Form/Planning/Line Ministry Rank" parentModule="/Activity Form/Planning">
			<br>
			<digi:trn>Line Ministry Rank</digi:trn>:&nbsp;
				<c:if test="${aimEditActivityForm.planning.lineMinRank == -1}"></c:if> 
				<c:if test="${aimEditActivityForm.planning.lineMinRank != -1}">
					<b>${aimEditActivityForm.planning.lineMinRank}</b>				</c:if>
		</module:display>
		
		<module:display name="/Activity Form/Planning/Ministry of Planning Rank" parentModule="/Activity Form/Planning">
			<hr>
			<digi:trn>Ministry of Planning Rank</digi:trn>:&nbsp;
			<c:if test="${aimEditActivityForm.planning.planMinRank == -1}"></c:if> 
			<c:if test="${aimEditActivityForm.planning.planMinRank != -1}">
				<b>${aimEditActivityForm.planning.planMinRank}</b>			</c:if>
		</module:display>
		
		<module:display name="/Activity Form/Planning/Proposed Approval Date" parentModule="/Activity Form/Planning">
			<hr>
			<digi:trn>Proposed Approval Date</digi:trn>:&nbsp;
			<b>${aimEditActivityForm.planning.originalAppDate}</b>		</module:display>
			
		<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
		<module:display name="/Activity Form/Planning/Actual Approval Date" parentModule="/Activity Form/Planning">
			<hr>
				<digi:trn>Actual Approval Date</digi:trn>:&nbsp;
				<b>${aimEditActivityForm.planning.revisedAppDate}</b>		</module:display>
				
		
		<module:display name="/Activity Form/Planning/Proposed Start Date" parentModule="/Activity Form/Planning">
			<hr>
			<digi:trn>Proposed Start Date</digi:trn>:&nbsp;
			<b>${aimEditActivityForm.planning.originalStartDate}</b>		</module:display>
							
		<module:display name="/Activity Form/Planning/Actual Start Date" parentModule="/Activity Form/Planning">
			<hr>
			<digi:trn>Actual Start Date </digi:trn>:&nbsp;
			<b>${aimEditActivityForm.planning.revisedStartDate}</b>		</module:display>
		
		<module:display name="/Activity Form/Planning/Original Completion Date" parentModule="/Activity Form/Planning">
			<hr>
			<digi:trn>Original Completion Date</digi:trn>:&nbsp;
			<b>${aimEditActivityForm.planning.originalCompDate}</b>		</module:display>	
		
		<module:display name="/Activity Form/Planning/Proposed Completion Date" parentModule="/Activity Form/Planning">
			<hr>
			<digi:trn>Proposed Completion Date</digi:trn>:&nbsp;
			<b>${aimEditActivityForm.planning.proposedCompDate}</b>		</module:display>
		
		<module:display name="/Activity Form/Planning/Actual Completion Date" parentModule="/Activity Form/Planning">
			<hr>
			<digi:trn>Actual Completion Date</digi:trn>:&nbsp;
			<b><c:out value="${aimEditActivityForm.planning.currentCompDate}"/></b>		
		</module:display> 
							
		<module:display name="/Activity Form/Planning/Final Date for Contracting" parentModule="/Activity Form/Planning">
			<hr>
			<digi:trn>Final Date for Contracting</digi:trn>:&nbsp;
			<b><c:out value="${aimEditActivityForm.planning.contractingDate}"/></b>		</module:display>
					
		<module:display name="/Activity Form/Planning/Final Date for Disbursements" parentModule="/Activity Form/Planning">
			<hr>
			<digi:trn>Final Date for Disbursements</digi:trn>:&nbsp;
			<b><c:out value="${aimEditActivityForm.planning.disbursementsDate}"/></b>		</module:display>
		
		
		<hr>
		<field:display name="Duration of Project" feature="Planning"> 
			<digi:trn>Duration of project</digi:trn>:&nbsp;
			<b>${aimEditActivityForm.planning.projectPeriod }</b>
		</field:display>
		</div>
</fieldset>
</module:display>

<!-- REFERENCES -->
<module:display name="/References">	
<fieldset>
	<legend>
		<span class=legend_label id="referenceslink" style="cursor: pointer;">
			<digi:trn>References</digi:trn>
		</span>
	</legend>
	<div id="referencesdiv">
	<ul>
		<logic:notEmpty name="aimEditActivityForm" property="documents.referenceDocs">
			<logic:iterate name="aimEditActivityForm" property="documents.referenceDocs" id="doc">
				<li>
					<bean:write name="doc" property="categoryValue"/>
				</li>
			</logic:iterate>
		</logic:notEmpty>
	</ul>
	</div>
</fieldset>
</module:display>



<!--LOCATIONS SECTION-->
<module:display name="/Activity Form/Location" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="locationlink" style="cursor: pointer;">
			<digi:trn>Location</digi:trn></span>	
		</legend>
		<div id="locationdiv">
		<module:display name="/Activity Form/Location/Implementation Location" parentModule="/Activity Form/Location">
			<c:if test="${!empty aimEditActivityForm.location.selectedLocs}">
					<c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
						<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
						<tr>
							<td width="85%">
								<c:forEach var="ancestorLoc" items="${selectedLocs.ancestorLocationNames}">
	                           		<b>[${ancestorLoc}]</b>	                                           
	                            </c:forEach>
	                          </td>
							<td width="15%" align="right" valign=top>
								<field:display name="Regional Percentage" feature="Location">
									<c:if test="${selectedLocs.showPercent}">
										<b><c:out value="${selectedLocs.percent}"/>%</b>									
									</c:if>
								</field:display>							
							</td>
						</tr>
						</table>
						<hr/>
					</c:forEach>
					<module:display name="GIS DASHBOARD">
					<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
					<tr>
						<td colspan="2">
						<br>
						<logic:notEmpty name="aimEditActivityForm" property="location.selectedLocs">
							<bean:define id="selLocIds">
							<c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
								<bean:write name="selectedLocs" property="locId" />|							
							</c:forEach>
							</bean:define>
						</logic:notEmpty>
						
						<logic:notEmpty name="aimEditActivityForm" property="location.selectedLocs">
							<div id="mapPreviewThumbnail" style="cursor:pointer;">
								<c:if test="${aimEditActivityForm.location.levelIdx==1}">
									<img id="mapThumbnail" border="0" src="/gis/getActivityMap.do?action=paintMap&noCapt=true&width=200&height=200&mapLevel=2&mapCode=TZA&selRegIDs=<bean:write name="selLocIds"/>">
								</c:if>
								<c:if test="${aimEditActivityForm.location.levelIdx>1}">
									<img id="mapThumbnail" border="0" src="/gis/getActivityMap.do?action=paintMap&noCapt=true&width=200&height=200&mapLevel=3&mapCode=TZA&selRegIDs=<bean:write name="selLocIds"/>">
								</c:if>
							</div>
							<div id="zoomMapContainer" style="display: none; border: 1px solid black; position: absolute; left: 0px; top: 0px;" z-index="9999">
								<a href="javascript:showZoomedMap(false)">
									<c:if test="${aimEditActivityForm.location.levelIdx==1}">
										<img border="0" src="/gis/getActivityMap.do?action=paintMap&width=500&height=500&mapLevel=2&mapCode=TZA&selRegIDs=<bean:write name="selLocIds"/>">
									</c:if>
									<c:if test="${aimEditActivityForm.location.levelIdx>1}">
										<img border="0" src="/gis/getActivityMap.do?action=paintMap&width=500&height=500&mapLevel=3&mapCode=TZA&selRegIDs=<bean:write name="selLocIds"/>">
									</c:if>
								</a>
							</div>
						</logic:notEmpty>						
						</td>
					</tr>
					</table>
					<hr/>
					</module:display>
					<field:display name="Show Map In Activity Preview" feature="Map Options">
					<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
					<tr>
						<td colspan="2">
							<script type="text/javascript">
							<c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
								coordinates.push('<c:out value="${selectedLocs.lat}"/>;<c:out value="${selectedLocs.lon}"/>;<c:out value="${selectedLocs.locationName}"/>');
							</c:forEach>
							</script>
							<jsp:include page="previewmap.jsp"/>						
						</td>
					</tr>
					</table>
					<hr/>
					</field:display>
			</c:if>
		</module:display>
		<module:display name="/Activity Form/Location/Implementation Level" parentModule="/Activity Form/Location">
			<table>
				<tr>
					<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
						<digi:trn key="aim:level">Implementation Level</digi:trn>:					
					</td>
					<td bgcolor="#ffffff">
						<c:if test="${aimEditActivityForm.location.levelId>0}">
							<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.location.levelId}"/></b>
						</c:if>					
					</td>
				</tr>
			</table>
			<hr/>
		</module:display>
		<module:display name="/Activity Form/Location/Implementation Location" parentModule="/Activity Form/Location">
			<table style="font-size:11px;">
				<tr>
					<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
						<digi:trn key="aim:implementationLocation">Implementation Location</digi:trn>:					</td>
					<td bgcolor="#ffffff">
						<c:if test="${aimEditActivityForm.location.implemLocationLevel>0}">
							<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.location.implemLocationLevel}"/></b>
						</c:if>					
					</td>
				</tr>
			</table>
			<hr/>
		</module:display>
	</div>
</fieldset>
</module:display>
<!-- LOCATIONS SECTION -->
<!-- NATIONAL PLAN SECTION -->
<module:display name="/Activity Form/Program/National Plan Objective" parentModule="/Activity Form/Program">
<fieldset>
	<legend>
		<span class=legend_label id="nationalplanlink" style="cursor: pointer;">
			<digi:trn>National Plan</digi:trn></span>	
		</legend>
	<div id="nationalplandiv">
		<c:if test="${!empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
			<c:forEach var="nationalPlanObjectivePrograms" items="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
				<c:set var="program" value="${nationalPlanObjectivePrograms.program}"/>
				<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
					  <tr>
 						   <td width=85%><b>${nationalPlanObjectivePrograms.hierarchyNames}</b></td>
  					   	  	<td width=15% align=right valign=top><b>${nationalPlanObjectivePrograms.programPercentage}%</b></td>
					  </tr>
				</table>
				<hr />
			</c:forEach>
		</c:if>
	</div>
</fieldset>
</module:display>
<!-- END NATIONAL PLAN SECTION -->

<!-- PROGRAM SECTION -->
<module:display name="/Activity Form/Program" parentModule="/Activity Form">
	<fieldset>
		<legend>
			<span class=legend_label id="programlink" style="cursor: pointer;">
				<digi:trn>Program</digi:trn>
			</span>		
		</legend>
		<div id="programdiv">
			<module:display name="/Activity Form/Program/Primary Programs" parentModule="/Activity Form/Program">
				<digi:trn>Primary Programs</digi:trn>
				 <hr/>
				<c:forEach var="program" items="${aimEditActivityForm.programs.primaryPrograms}">
					<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
				 		<tr>
					  		<td width=85%>
								<b><c:out value="${program.hierarchyNames}"/></b>					
					 		</td>
							<td width=15% align=right>
							<b>
								<c:out value="${program.programPercentage}"/>%
							</b>					
							</td>
						</tr>
					</table>
					<hr>
				</c:forEach>	
			</module:display>
			<module:display name="/Activity Form/Program/Secondary Programs" parentModule="/Activity Form/Program">
				<digi:trn>Secondary Programs</digi:trn><br/>
				<c:forEach var="program" items="${aimEditActivityForm.programs.secondaryPrograms}">
					<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
				  		<tr>
					  	<td width=85%>
					  		<b><c:out value="${program.hierarchyNames}"/></b>
					  	</td>
						<td width="15%" align=right valign=top>
							<b><c:out value="${program.programPercentage}"/>%</b>
						</td>
					</tr>
					</table>
				</c:forEach>
			</module:display>
		</div>
	</fieldset>
</module:display>
<!-- END PROGRAM SECTION -->

<!-- SECTORS SECTION -->
<module:display name="/Activity Form/Sectors" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="sectorslink" style="cursor: pointer;">
			<digi:trn>Sectors</digi:trn>
		</span>	
	</legend>
	<div id="sectorsdiv">
		<c:forEach var="config" items="${aimEditActivityForm.sectors.classificationConfigs}" varStatus="ind">
			<bean:define id="emptySector" value="Sector"/>
			<module:display name="/Activity Form/Sectors/${config.name} Sectors" parentModule="/Activity Form/Sectors">
				<c:set var="hasSectors">false </c:set>
				<c:forEach var="actSect" items="${aimEditActivityForm.sectors.activitySectors}">
					<c:if test="${actSect.configId==config.id}">
						<c:set var="hasSectors">true</c:set>
					</c:if>
				</c:forEach>
				<c:if test="${hasSectors}">
					<digi:trn key="aim:addactivitysectors:${config.name} Sector">
						<c:out value="${config.name} Sector" />
					</digi:trn> 
					<br/>
				</c:if>
				<c:if test="${!empty aimEditActivityForm.sectors.activitySectors}">
				<c:forEach var="sectors" items="${aimEditActivityForm.sectors.activitySectors}">
					<c:if test="${sectors.configId==config.id}">
						<module:display name="/Activity Form/Sectors" parentModule="/Activity Form">
							<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:10px;">
							  <tr>
								 <td width=85%>
								  	<b><c:out value="${sectors.sectorScheme}" /></b>
									<c:if test="${!empty sectors.sectorName}">
										-
										<b><c:out value="${sectors.sectorName}"/></b>
									</c:if>
									<c:if test="${!empty sectors.subsectorLevel1Name}">
										<!-- Sub sector field not found -->
										-
										<b><c:out value="${sectors.subsectorLevel1Name}"/></b>
									</c:if>
									<c:if test="${!empty sectors.subsectorLevel2Name}">
										-
										<b><c:out value="${sectors.subsectorLevel2Name}"/></b>
									</c:if>
								</td>
							<td width=15% align=right valign=top>
                                <c:if test="${sector.sectorPercentage!='' && sector.sectorPercentage!='0'}">
                               		<b>(<c:out value="${sectors.sectorPercentage}"/>)%</b>                                            
                                </c:if>
                            </td>
							</tr>
							</table>
							<hr>
					</module:display>
					</c:if>
			</c:forEach>
			</c:if>
		</module:display>
	</c:forEach>		
	<c:if test="${not empty aimEditActivityForm.components.activityComponentes}">
		<digi:trn>Components</digi:trn>:&nbsp;
		<table>
			<c:forEach var="compo" items="${aimEditActivityForm.components.activityComponentes}">
			<tr>
				<td width="100%">${compo.sectorName}</td>
				<td align="right">${compo.sectorPercentage}%</td>
			</tr>
			</c:forEach>
		</table>
		<hr />
	</c:if>
</div>
</fieldset>
</module:display>
<!-- END SECTORS SECTION -->

<!-- FUNDING SECTION -->
<logic:present name="currentMember" scope="session">
	<module:display name="/Activity Form/Donor Funding" parentModule="/Activity Form">
		<fieldset>
			<legend>
				<span class=legend_label id="fundinglink" style="cursor: pointer;">
					<digi:trn>Donor Funding</digi:trn>
				</span>			
			</legend>
			<div id="fundingdiv">
				<bean:define id="aimEditActivityForm" name="aimEditActivityForm"scope="page" toScope="request"/>
				<jsp:include page="previewActivityFunding.jsp"/>
			</div>
		</fieldset>
	</module:display>
</logic:present>

<!-- END FUNDING SECTION -->

<!-- REGIONAL FUNDING -->
<module:display name="/Activity Form/Regional Funding" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="regionalfundinglink" style="cursor: pointer;">
			<digi:trn>Regional Funding</digi:trn>
		</span>	
	</legend>
	<div id="regionalfundingdiv">
	<c:if test="${!empty aimEditActivityForm.funding.regionalFundings}">
	<table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#aaaaaa">
		<c:forEach var="regFunds" items="${aimEditActivityForm.funding.regionalFundings}">
			<tr>
				<td class="prv_right">
					<table width="100%" cellSpacing="1" cellPadding="1">
						<tr>
							<td class="prv_right">
								<b><c:out value="${regFunds.regionName}"/></b>							
							</td>
						</tr>
						<module:display name="/Activity Form/Regional Funding/Region Item/Commitments" parentModule="/Activity Form/Regional Funding/Region Item">
						<c:if test="${!empty regFunds.commitments}">
						<tr>
							<td class="prv_right">
								<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding" border="1">
									<tr>
											<td valign="top" width="100" bgcolor="#f0f0f0"> 
												<digi:trn>Commitments</digi:trn>											</td>
											<td class="prv_right">
											<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
													<c:forEach var="fd" items="${regFunds.commitments}">
														<tr>
															<td width="50" bgcolor="#f0f0f0">
																<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																	<c:out value="${fd.adjustmentTypeName}" />
																</digi:trn></td>
															<td align="right" width="100" bgcolor="#f0f0f0">
																<c:out value="${fd.transactionAmount}"/>															</td>
															<td class="prv_right">
																<c:out value="${fd.currencyCode}"/>															</td>
															<td bgcolor="#f0f0f0" width="70">
																<c:out value="${fd.transactionDate}"/>															</td>
															<td class="prv_right"></td>
														</tr>
													</c:forEach>
												</table>												</td>
											</tr>
										</table>										</td>
									</tr>
								</c:if>
							</module:display>
							<module:display name="/Activity Form/Regional Funding/Region Item/Disbursements" 
								parentModule="/Activity Form/Regional Funding/Region Item">
							<c:if test="${!empty regFunds.disbursements}">
								<tr>
									<td class="prv_right">
									<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
										<tr>
											<td valign="top" width="100" bgcolor="#f0f0f0">
												<digi:trn key="aim:disbursements">Disbursements</digi:trn>											</td>
											<td class="prv_right">
											<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
												<c:forEach var="fd" items="${regFunds.disbursements}">
													<tr>
														<td width="50" bgcolor="#f0f0f0">
															<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																<c:out value="${fd.adjustmentTypeName}" />
															</digi:trn>														</td>
														<td align="right" width="100" bgcolor="#f0f0f0">
															<c:out value="${fd.transactionAmount}"/>														</td>
														<td class="prv_right">
															<c:out value="${fd.currencyCode}"/>														</td>
														<td bgcolor="#f0f0f0" width="70">
															<c:out value="${fd.transactionDate}"/>														</td>
														<td class="prv_right"></td>
													</tr>
												</c:forEach>
											</table>											</td>
										</tr>
									</table>									</td>
								</tr>
							</c:if>
							</module:display>
							
							<module:display name="/Activity Form/Regional Funding/Region Item/Expenditures" 
										parentModule="/Activity Form/Regional Funding/Region Item">
							<c:if test="${!empty regFunds.expenditures}">
								<tr>
									<td class="prv_right">
									<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
										<tr>
											<td valign="top" width="100" bgcolor="#f0f0f0">
												<digi:trn key="aim:expenditures">Expenditures</digi:trn>											</td>
											<td class="prv_right">
											<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
												<c:forEach var="fd" items="${regFunds.expenditures}">
													<tr>
														<td width="50" bgcolor="#f0f0f0">
															<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																<c:out value="${fd.adjustmentTypeName}" />
															</digi:trn>														</td>
														<td align="right" width="100" bgcolor="#f0f0f0">
															<c:out value="${fd.transactionAmount}"/>														</td>
														<td class="prv_right">
															<c:out value="${fd.currencyCode}"/></td>
														<td bgcolor="#f0f0f0" width="70">
															<c:out value="${fd.transactionDate}"/>														</td>
														<td class="prv_right"></td>
													</tr>
												</c:forEach>
											</table>											</td>
										</tr>
									</table>									</td>
								</tr>
							</c:if>
							</module:display>
						</table>						</td>
					</tr>
				</c:forEach>
				<tr>
					<td class="prv_right">
						<FONT color='blue'>
							<jsp:include page="utils/amountUnitsUnformatted.jsp">
								<jsp:param value="* " name="amount_prefix"/>
							</jsp:include>	
						</FONT>
					</td>
				</tr>
			</table>
		</c:if> 
	</div>
</fieldset>
</module:display>
<!-- END REGIONAL FUNDING -->

<!-- COMPONENTS -->
<module:display name="/Activity Form/Components" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="componentlink" style="cursor: pointer;">
			<digi:trn>Components</digi:trn>
		</span>	
	</legend>
	<div id="componentdiv">
<logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="false">
		<c:if test="${!empty aimEditActivityForm.components.selectedComponents}">
			<c:forEach var="comp" items="${aimEditActivityForm.components.selectedComponents}">
				<table width="100%" cellSpacing="1" cellPadding="1">
					<tr>
						<td>
							<table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">
								<tr>
									<td>
										<b> <c:out value="${comp.title}" /> </b>									</td>
								</tr>
								<tr>
									<td>
										<i> <digi:trn key="aim:description">Description</digi:trn>
										:</i> <c:out value="${comp.description}" />									</td>
								</tr>
								<tr>
									<td class="prv_right">
										<digi:trn>Component Funding</digi:trn>									
									</td>
								</tr>
								<module:display name="/Activity Form/Components/Component/Components Commitments" 
															parentModule="/Activity Form/Components/Component">
								<c:if test="${!empty comp.commitments}">
								<tr>
									<td class="prv_right">
										<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
											<tr>
												<td width="100" style="padding-left:5px;" bgcolor="#f0f0f0">
													<digi:trn key="aim:commitments">Commitments</digi:trn>												
												</td>
												<td class="prv_right">
												<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
													<c:forEach var="fd" items="${comp.commitments}">
														<tr>
															<module:display name="/Activity Form/Components/Component/Components Commitments" 
																parentModule="/Activity Form/Components/Component">
															<td width="50" bgcolor="#f0f0f0">
																<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																	<b><c:out value="${fd.adjustmentTypeName}" /></b>
																</digi:trn>															
															</td>
															</module:display>
															<module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Amount" 
																parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">	
																<td align="right" width="100" bgcolor="#f0f0f0">
																	<b><c:out value="${fd.transactionAmount}"/></b>																
																</td>
															</module:display>
															<module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Currency"
																parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																<td class="prv_right">
																	<b><c:out value="${fd.currencyCode}"/></b>																
																</td>
															</module:display>
															<module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Transaction Date"
																parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																<td bgcolor="#f0f0f0" width="70">
																	<b><c:out value="${fd.transactionDate}"/></b>																
															   </td>
															</module:display>
														</tr>
													</c:forEach>
												</table>												</td>
											</tr>
										</table>									</td>
								</tr>
							</c:if>
							</module:display>
  							<module:display name="/Activity Form/Components/Component/Components Disbursements"
													parentModule="/Activity Form/Components/Component">
							<c:if test="${!empty comp.disbursements}">
								<tr>
									<td class="prv_right">
									<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
										<tr>
											<td width="100" style="padding-left:5px;" bgcolor="#f0f0f0">
												<digi:trn key="aim:disbursements">Disbursements</digi:trn>											
											</td>
											<td class="prv_right">
											<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
												<c:forEach var="fd" items="${comp.disbursements}">
													<tr>
														<module:display name="/Activity Form/Components/Component/Components Disbursements"
																parentModule="/Activity Form/Components/Component">
															<td width="50" bgcolor="#f0f0f0"> 
																<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																	<b><c:out value="${fd.adjustmentTypeName}" /></b>
																</digi:trn>															
															</td>
														</module:display>
														<module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Amount"
																	parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
															<td align="right" width="100" bgcolor="#f0f0f0">
																<b><c:out value="${fd.transactionAmount}"/></b>															
															</td>
														</module:display>
														
														<module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Currency"
																parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
															<td class="prv_right">
																<b><c:out value="${fd.currencyCode}"/></b>
															</td>
														</module:display>
														<module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Transaction Date"
															parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
															<td bgcolor="#f0f0f0" width="70">
																<b><c:out value="${fd.transactionDate}"/></b>
															</td>
														</module:display>
													</tr>
												</c:forEach>
											</table>											</td>
										</tr>
								</table>							</td>
						</tr>
					</c:if>
					</module:display>
					<module:display name="/Activity Form/Components/Component/Components Expeditures" 
							parentModule="/Activity Form/Components/Component/">
					
					<c:if test="${!empty comp.expenditures}">
						<tr>
							<td class="prv_right">
							<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
								<tr>
									<td width="100" bgcolor="#f0f0f0" style="padding-left:5px;">
										<digi:trn key="aim:expenditures">Expenditures</digi:trn>									
									</td>
									<td class="prv_right">
									<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
										<c:forEach var="fd" items="${comp.expenditures}">
											<tr bgcolor="#f0f0f0">
												<module:display name="/Activity Form/Components/Component/Components Expeditures" 
													parentModule="/Activity Form/Components/Component/">
													<td width="50" bgcolor="#f0f0f0">
														<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
															<b><c:out value="${fd.adjustmentTypeName.value}" /></b>
														</digi:trn>													
													</td>
												</module:display>
												<module:display name="/Activity Form/Components/Component/Components Expeditures/Expenditure Table/Amount"
													parentModule="/Activity Form/Components/Component/Components Expeditures/Expenditure Table">
													<td align="right" width="100" bgcolor="#f0f0f0">
														<b><c:out value="${fd.transactionAmount}"/></b>				
													</td>
												</module:display>
												<module:display name="/Activity Form/Components/Component/Components Expeditures/Expenditure Table/Currency"
													parentModule="/Activity Form/Components/Component/Components Expeditures/Expenditure Table">
													<td class="prv_right">
														<b><c:out value="${fd.currencyCode}"/></b>
													</td>
												</module:display>
												<module:display name="/Activity Form/Components/Component/Components Expeditures/Expenditure Table/Transaction Date"
													parentModule="/Activity Form/Components/Component/Components Expeditures/Expenditure Table">
													<td bgcolor="#f0f0f0" width="70">
														<b><c:out value="${fd.transactionDate}"/></b>
													</td>
												</module:display>
											</tr>
										</c:forEach>
									</table>									</td>
								</tr>
							</table>							</td>
						</tr>
					</c:if>
					</module:display>
					<tr>
						<td class="prv_right">
							<FONT color='blue'>
								<jsp:include page="utils/amountUnitsUnformatted.jsp">
									<jsp:param value="* " name="amount_prefix"/>
								</jsp:include>	
							</FONT>					
						</td>
				  </tr>
				<!-- Field not found -->
				<field:display name="Components Physical Progress" feature="Activity - Component Step">
					<tr>
						<td class="prv_right">
							<digi:trn key="aim:physicalProgressOfTheComponent">Physical progress of the component</digi:trn>
						</td>
					</tr>
					<c:if test="${!empty comp.phyProgress}">
						<c:forEach var="phyProg" items="${comp.phyProgress}">
							<tr>
								<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								<b> 
									<c:out value="${phyProg.title}"/> - 
									<c:out value="${phyProg.reportingDate}" />
								</b>								
								</td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i> 
									<digi:trn key="aim:description">Description</digi:trn> :</i> 
									<c:out value="${phyProg.description}"/>								</td>
							</tr>
						</c:forEach>
					</c:if>
					</field:display>
				</table>				</td>
			</tr>
		</table>
	</c:forEach>
	</c:if>
	</logic:equal>
	<logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="true">
		<c:if test="${!empty aimEditActivityForm.components.selectedComponents}">
			<c:forEach var="comp" items="${aimEditActivityForm.components.selectedComponents}">
				<table width="100%" cellSpacing="1" cellPadding="1">
					<tr>
						<td>
						<table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">
							<tr>
								<td>
									<b><c:out value="${comp.title}" /></b>								</td>
							</tr>
							<tr>
								<td>
									<i><digi:trn key="aim:component_code">Component code</digi:trn>
								:</i><c:out value="${comp.code}" /></td>
							</tr>
							<tr>
								<td>
									<a href="<c:out value="${comp.url}"/>" target="_blank">
										<digi:trn key="aim:preview_link_to_component">Link to component</digi:trn>&nbsp;
										<c:out value="${comp.code}"/>
									</a>								</td>
							</tr>
							<tr>
								<td class="prv_right"><b>
									<digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>								</td>
							</tr>
							<tr>
								<td class="prv_right">
								<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
									<c:forEach var="financeByYearInfo" items="${comp.financeByYearInfo}">
										<tr>
											<td valign="top" width="100" bgcolor="#f0f0f0">
												<c:out value="${financeByYearInfo.key}"/></td>
												<c:set var="financeByYearInfoMap" value="${financeByYearInfo.value}"/>
											<td class="prv_right">
											<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
												<fmt:timeZone value="US/Eastern">
													<tr>
														<td width="50" bgcolor="#f0f0f0">
															<digi:trn key="aim:preview_plannedcommitments_sum">Planned Commitments Sum</digi:trn>														</td>
														<td align="right" width="100" bgcolor="#f0f0f0">
															<aim:formatNumber value="${financeByYearInfoMap['MontoProgramado']}"/>USD														</td>
													</tr>
													<tr>
														<td width="50" bgcolor="#f0f0f0">
															<digi:trn key="aim:preview_actualcommitments_sum">Actual Commitments Sum</digi:trn>														</td>
														<td align="right" width="100" bgcolor="#f0f0f0">
															<aim:formatNumber value="${financeByYearInfoMap['MontoReprogramado']}"/> USD														</td>
													</tr>
													<tr>
														<td width="50" bgcolor="#f0f0f0">
															<digi:trn key="aim:preview_plannedexpenditures_sum">Actual Expenditures Sum</digi:trn>														</td>
														<td align="right" width="100" bgcolor="#f0f0f0">
															<aim:formatNumber value="${financeByYearInfoMap['MontoEjecutado']}"/>USD														</td>
													</tr>
												</fmt:timeZone>
											</table>											</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
									</c:forEach>
								</table>								</td>
							</tr>
						</table>						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
				</table>
			</c:forEach>
			</c:if>	
	</logic:equal>
	</div>
</fieldset>
</module:display>
<!-- END COMPONENTS -->
<!-- ISSUES SECTION -->
<module:display name="/Activity Form/Issues Section" parentModule="/Activity Form">
<module:display name="/Activity Form/Issues Section/Issue Field" parentModule="/Activity Form/Issues Section">
<fieldset>
	<legend>
		<span class=legend_label id="issueslink" style="cursor: pointer;">
			<digi:trn>Issues</digi:trn> 
		</span>	
	</legend>
	<div id="issuesdiv">
		<c:if test="${!empty aimEditActivityForm.issues.issues}">
		<table width="100%" cellSpacing="2" cellPadding="2" border="0">
			<c:forEach var="issue" items="${aimEditActivityForm.issues.issues}">
				<tr>
					<td valign="top">
						<li class="level1"><b> 
							<digi:trn key="aim:issuename:${issue.id}">
							<c:out value="${issue.name}" />
							</digi:trn> 
								<c:out value="${issue.issueDate}" />
							</b>						</li>					</td>
				</tr>
					<c:if test="${!empty issue.measures}">
						<c:forEach var="measure" items="${issue.measures}">
							<tr>
								<td>
									<li class="level2"><i> <digi:trn key="aim:${measure.nameTrimmed}">
										<c:out value="${measure.name}" />
										</digi:trn> </i>									</li>								</td>
							</tr>
								<c:if test="${!empty measure.actors}">
									<c:forEach var="actor" items="${measure.actors}">
										<tr>
											<td>
											<li class="level3">
												<digi:trn key="aim:${actor.nameTrimmed}">
													<c:out value="${actor.name}" />
												</digi:trn>
											</li>											</td>
										</tr>
									</c:forEach>
								</c:if>
						</c:forEach>
					</c:if>
			</c:forEach>
		</table>
	</c:if>
	</div>
</fieldset>
</module:display>
</module:display>
<!-- END ISSUES SECTION -->
	
<!-- DOCUMENT SECTION -->
<module:display name="/Activity Form/Related Documents" parentModule="/Activity Form">
	<fieldset>
	<legend>
		<span class=legend_label id="documentslink" style="cursor: pointer;">
			<digi:trn>Related Documents</digi:trn>
		</span>	
	</legend>
	<div id="documnetsdiv">
	<c:if test="${ (!empty aimEditActivityForm.documents.documentList) || (!empty aimEditActivityForm.documents.crDocuments)}">
		<table width="100%" cellSpacing="0" cellPadding="0">
			<logic:iterate name="aimEditActivityForm" property="documents.documents" id="docs" type="org.digijava.module.aim.helper.Documents">
				<c:if test="${docs.isFile == true}">
					<tr>
						<td>
						<table width="100%" class="box-border-nopadding">
							<tr bgcolor="#f0f0f0">
								<td vAlign="center" align="left">&nbsp;
									<b><c:out value="${docs.title}"/></b> - &nbsp;&nbsp;&nbsp;<i>
									<c:out value="${docs.fileName}"/></i> 
									<logic:notEqual name="docs" property="docDescription" value=" ">
										<br/>&nbsp;
										<b><digi:trn>Description</digi:trn>:</b>
										&nbsp;<bean:write name="docs" property="docDescription" />
									</logic:notEqual> 
									<logic:notEmpty name="docs" property="date">
										<br />&nbsp;
										<b><digi:trn>Date</digi:trn>:</b>
										&nbsp;<c:out value="${docs.date}" />
									</logic:notEmpty> 
									<logic:notEmpty name="docs" property="docType">
										<br />&nbsp;
										<b><digi:trn>Document Type</digi:trn>:</b>&nbsp;
										<bean:write name="docs" property="docType" />
									</logic:notEmpty>								</td>
							</tr>
						</table>						</td>
					</tr>
				</c:if>
			</logic:iterate>
			<logic:notEmpty name="aimEditActivityForm" property="documents.crDocuments" >
				<tr>
					<td>
						<logic:iterate name="aimEditActivityForm" property="documents.crDocuments" id="crDoc">
						<table width="100%" class="box-border-nopadding">
								<tr bgcolor="#f0f0f0">
									<td vAlign="center" align="left">
										&nbsp;<b><c:out value="${crDoc.title}"/></b> - &nbsp;&nbsp;&nbsp;
										<i><c:out value="${crDoc.name}"/></i>
										<c:set var="translation">
											<digi:trn>Click here to download document</digi:trn>
										</c:set> 
										<%-- <a style="cursor: pointer; text-decoration: underline; color: blue;" id="<c:out value="${crDoc.uuid}"/>"
											onclick="window.location='/contentrepository/downloadFile.do?uuid=<c:out value="${crDoc.uuid}"/>'"
											title="${translation}">
											<img src="/repository/contentrepository/view/images/check_out.gif" border="0" >
										</a> --%>
										<a id="<c:out value="${crDoc.uuid}"/>" target="_blank" href="${crDoc.generalLink}" title="${translation}">
											<img src="/repository/contentrepository/view/images/check_out.gif" border="0">
										</a>
										<logic:notEmpty name="crDoc" property="description">
											<br/>&nbsp;
											<b><digi:trn>Description</digi:trn>:</b>&nbsp;
											<bean:write name="crDoc" property="description" />
										</logic:notEmpty> 
										<logic:notEmpty name="crDoc" property="calendar">
											<br/>&nbsp;
											<b><digi:trn>Date</digi:trn>:</b>
											&nbsp;<c:out value="${crDoc.calendar}" />
										</logic:notEmpty>									</td>
								</tr>
							</table>
						</logic:iterate>					</td>
				</tr>
			</logic:notEmpty>
		</table>
	</c:if> 
	<c:if test="${!empty aimEditActivityForm.documents.linksList}">
		<table width="100%" cellSpacing="0" cellPadding="0">
			<c:forEach var="docList" items="${aimEditActivityForm.documents.linksList}" >
				<bean:define id="links" name="docList" property="relLink" />
				<tr>
					<td>
					<table width="100%" class="box-border-nopadding">
						<tr>
							<td width="2">
								<digi:img src="module/aim/images/web-page.gif"/>							</td>
							<td align="left" vAlign="center">&nbsp; <b>
								<c:out value="${links.title}"/></b> - &nbsp;&nbsp;&nbsp;
								<i> <a href="<c:out value="${links.url}"/>"> 
									<c:out value="${links.url}" />
								</a></i> 
								<br>
								&nbsp; <b><digi:trn>Description</digi:trn>:</b>
								&nbsp;<c:out value="${links.description}" />							</td>
						</tr>
					</table>					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	</div>
</fieldset>
</module:display>

<jsp:include page="previewActivityLineMinistryObservations.jsp"></jsp:include>

<!-- RELATED ORGANIZATIONS SECTION -->
<module:display name="/Activity Form/Related Organizations" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="relatedorglink" style="cursor: pointer;">
			<digi:trn>Related Organizations</digi:trn>
		</span>	
	</legend>
	<div id="relateorgdiv">
		<module:display name="/Activity Form/Related Organizations/Responsible Organization" parentModule="/Activity Form/Related Organizations">
			<digi:trn key="aim:responsibleorganisation">Responsible Organization</digi:trn>
			<br/>
			<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrganisations" >
				<div id="act_responsible_organisation" style="display: block;">
				<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding" >
					<tr>
						<td>
							<logic:iterate name="aimEditActivityForm" property="agencies.respOrganisations" id="respOrganisations"
								type="org.digijava.module.aim.dbentity.AmpOrganisation">
								<ul>
									<li>
										<b><bean:write name="respOrganisations" property="name"/></b> 
										<c:set var="tempOrgId" scope="page">${respOrganisations.ampOrgId}</c:set>
										<!-- Additional Info field not found in the new activity form-->
										<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrgToInfo(${tempOrgId})">
											<b>(<c:out value="${aimEditActivityForm.agencies.respOrgToInfo[tempOrgId]}"/>)</b>										
										</logic:notEmpty>
										 <field:display name="Responsible Organization Percentage"  feature="Responsible Organization">
 	 	 	 		                         <logic:notEmpty name="aimEditActivityForm" property="agencies.respOrgPercentage(${tempOrgId})" >
												<c:out value="${aimEditActivityForm.agencies.respOrgPercentage[tempOrgId]}" /> %
 	 	 	 								</logic:notEmpty> 
 	 	 	 							</field:display>
									</li>
								</ul>
							</logic:iterate>						</td>
					</tr>
				</table>
				</div>
			</logic:notEmpty>
		</module:display>
		<!-- Executing Agency not found in the new activity form-->
		<hr />	
		
		<module:display name="/Activity Form/Related Organizations/Executing Agency" parentModule="/Activity Form/Related Organizations">		
		<digi:trn key="aim:executingAgency">Executing Agency</digi:trn>
		<br/>
		<logic:notEmpty name="aimEditActivityForm" property="agencies.executingAgencies">
			<div id="act_executing_agency" style="display: block;">
			<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
				<tr>
					<td>
						<logic:iterate name="aimEditActivityForm" property="agencies.executingAgencies" id="execAgencies"
							type="org.digijava.module.aim.dbentity.AmpOrganisation">
							<ul>
								<li>
									<b><bean:write name="execAgencies" property="name" /></b>
									<c:set var="tempOrgId">${execAgencies.ampOrgId}</c:set> 
									<!-- Additional Info field not found in the new activity form-->
									<logic:notEmpty name="aimEditActivityForm" property="agencies.executingOrgToInfo(${tempOrgId})">
										<b>(<c:out value="${aimEditActivityForm.agencies.executingOrgToInfo[tempOrgId]}"/>)</b>									
									</logic:notEmpty>
									<field:display name="Executing Agency Percentage"  feature="Executing Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.executingOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.executingOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																	</field:display>
								</li>
							</ul>
						</logic:iterate>					
					</td>
				</tr>
			</table>
			</div>
		</logic:notEmpty>
		<hr/>
		</module:display> 			
		<module:display name="/Activity Form/Related Organizations/Implementing Agency" parentModule="/Activity Form/Related Organizations">
			<digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn>
			<br/>
			<logic:notEmpty name="aimEditActivityForm" property="agencies.impAgencies" >
			<div id="act_implementing_agency" style="display: block;">
				<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
					<tr>
						<td>
							<logic:iterate name="aimEditActivityForm" property="agencies.impAgencies" id="impAgencies" 
								type="org.digijava.module.aim.dbentity.AmpOrganisation">
								<ul>
									<li>
										<b><bean:write name="impAgencies" property="name" /></b>
										<c:set var="tempOrgId">${impAgencies.ampOrgId}</c:set> 
										<!-- Additional Info field not found in the new activity form-->
										<logic:notEmpty name="aimEditActivityForm" property="agencies.impOrgToInfo(${tempOrgId})">
											<b>(<c:out value="${aimEditActivityForm.agencies.impOrgToInfo[tempOrgId]}"/>)</b>										
										</logic:notEmpty>
										<field:display name="Implementing Agency Percentage"  feature="Implementing Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.impOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.impOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																	</field:display>
									</li>
								</ul>
							 </logic:iterate>						
						</td>
					</tr>
				</table>
			</div>
			</logic:notEmpty>
		</module:display> 
		<hr />			
		<module:display name="/Activity Form/Related Organizations/Beneficiary Agency" parentModule="/Activity Form/Related Organizations">
			<digi:trn key="aim:beneficiary2Agency">Beneficiary Agency</digi:trn>
			<br />
			<logic:notEmpty name="aimEditActivityForm" property="agencies.benAgencies">
				<div id="act_benAgencies_agency" style="display: block;">
				<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
					<tr>
						<td>
							<logic:iterate name="aimEditActivityForm" property="agencies.benAgencies" id="benAgency"
								type="org.digijava.module.aim.dbentity.AmpOrganisation">	
								<ul>
									<li>
										<b><bean:write name="benAgency" property="name" /></b> 
										<c:set var="tempOrgId">${benAgency.ampOrgId}</c:set> 
										<!-- Additional Info field not found in the new activity form-->
										<logic:notEmpty name="aimEditActivityForm" property="agencies.benOrgToInfo(${tempOrgId})">
											<b>(<c:out value="${aimEditActivityForm.agencies.benOrgToInfo[tempOrgId]}"/>)</b>
										</logic:notEmpty>
										<field:display name="Beneficiary Agency  Percentage"  feature="Beneficiary Agency">
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.benOrgPercentage(${tempOrgId})" >
																			  <c:out value="${aimEditActivityForm.agencies.benOrgPercentage[tempOrgId]}" /> %
																			</logic:notEmpty> 
																		</field:display>
									</li>
								</ul>
							</logic:iterate>						
						</td>
					</tr>
				</table>
				</div>
			</logic:notEmpty>
		</module:display>
		<hr />			 
		<module:display name="/Activity Form/Related Organizations/Contracting Agency" parentModule="/Activity Form/Related Organizations">
			<digi:trn key="aim:contracting2Agency">Contracting Agency</digi:trn>
			<br/>
			<logic:notEmpty name="aimEditActivityForm" property="agencies.conAgencies">
				<div id="act_contracting_agency" style="display: block;">
				<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
					<tr>
						<td>
							<logic:iterate name="aimEditActivityForm" property="agencies.conAgencies" id="conAgencies"
								type="org.digijava.module.aim.dbentity.AmpOrganisation">
								<ul>
									<li>
										<b><bean:write name="conAgencies" property="name" /></b>
										<c:set var="tempOrgId">${conAgencies.ampOrgId}</c:set> 
										<!-- Additional Info field not found in the new activity form-->
										<logic:notEmpty name="aimEditActivityForm" property="agencies.conOrgToInfo(${tempOrgId})">
											<b>(<c:out value="${aimEditActivityForm.agencies.conOrgToInfo[tempOrgId]}"/> )</b>										
										</logic:notEmpty>
										<field:display name="Contracting Agency Percentage"  feature="Contracting Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.conOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.conOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																	</field:display>
									</li>
								</ul>
							</logic:iterate>						
						</td>
					</tr>
				</table>
				</div>
			</logic:notEmpty>
		</module:display>
		<hr />			
		<!--SECTOR GROUP SECTION -->
		<module:display name="/Activity Form/Related Organizations/Sector Group" parentModule="/Activity Form/Related Organizations">
			<digi:trn key="aim:sectorGroup">Sector Group</digi:trn>
			<br/>
			<logic:notEmpty name="aimEditActivityForm" property="agencies.sectGroups">
			<div id="act_sectGroups_agency" style="display: block;">
				<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
					<tr>
						<td>
						<logic:iterate name="aimEditActivityForm" property="agencies.sectGroups" id="sectGroup"
							type="org.digijava.module.aim.dbentity.AmpOrganisation">
							<ul>
								<li>
									<b><bean:write name="sectGroup" property="name" /></b> 
									<c:set var="tempOrgId">${sectGroup.ampOrgId}</c:set> 
									
									<logic:notEmpty name="aimEditActivityForm" property="agencies.sectOrgToInfo(${tempOrgId})">
										<b>(<c:out value="${aimEditActivityForm.agencies.sectOrgToInfo[tempOrgId]}"/> )</b>									
									</logic:notEmpty>
									<field:display name="Sector Group Percentage"  feature="Sector Group">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.sectOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.sectOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																	</field:display>
								</li>
							</ul>
						</logic:iterate></td>
					</tr>
				</table>
				</div>
			</logic:notEmpty>
		</module:display>
		<hr />			
		<module:display name="/Activity Form/Related Organizations/Regional Group" parentModule="/Activity Form/Related Organizations">
			<digi:trn key="aim:regionalGroup">Regional Group</digi:trn>
			<br/>
			<logic:notEmpty name="aimEditActivityForm" property="agencies.regGroups">
					<div id="act_regGroups_agency" style="display: block;">
						<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
							<tr>
								<td>
									<logic:iterate name="aimEditActivityForm" property="agencies.regGroups" id="regGroup"
										type="org.digijava.module.aim.dbentity.AmpOrganisation">
										<ul>
											<li>
												<b><bean:write name="regGroup" property="name" /></b> 
												<c:set var="tempOrgId" >${regGroup.ampOrgId}</c:set> 
												<logic:notEmpty property="agencies.regOrgToInfo(${tempOrgId})" name="aimEditActivityForm">
													<b>(<c:out value="${aimEditActivityForm.agencies.regOrgToInfo[tempOrgId]}"/>)</b>												
												</logic:notEmpty>
												<field:display name="Regional Group Percentage"  feature="Regional Group">
																	<logic:notEmpty name="aimEditActivityForm" property="agencies.regOrgPercentage(${tempOrgId})" >
																	  <c:out value="${aimEditActivityForm.agencies.regOrgPercentage[tempOrgId]}" /> %
																	</logic:notEmpty> 
																</field:display>
											</li>
										</ul>
									</logic:iterate>								</td>
							</tr>
						</table>
					</div>
			</logic:notEmpty>
		</module:display>
	</div>
</fieldset>
</module:display>
<!-- CONTACT INFORMATION -->
<module:display name="/Activity Form/Contacts" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="contactlink" style="cursor: pointer;">
			<digi:trn>Contact Information</digi:trn>
		</span>	
	</legend>
	<div id="contactdiv">
		<module:display name="/Activity Form/Contacts/Donor Contact Information" parentModule="/Activity Form/Contacts">
			<digi:trn>Donor funding contact information</digi:trn>:&nbsp;
			<c:if test="${not empty aimEditActivityForm.contactInformation.donorContacts}">
				<c:forEach var="donorContact" items="${aimEditActivityForm.contactInformation.donorContacts}">
					<div>		
						<b><c:out value="${donorContact.contact.name}" /></b> 
						<b><c:out value="${donorContact.contact.lastname}"/></b> - 
						<c:forEach var="property" items="${donorContact.contact.properties}">
							<c:if test="${property.name=='contact email'}">
								<b><c:out value="${property.value}" /> </b>;							
							</c:if>
						</c:forEach>
					</div>
				</c:forEach>
			</c:if> 
		</module:display>	
		<hr>
		<module:display name="/Activity Form/Contacts/Mofed Contact Information" parentModule="/Activity Form/Contacts">
			<digi:trn>MOFED contact information</digi:trn>:&nbsp;
			<c:if test="${not empty aimEditActivityForm.contactInformation.mofedContacts}">
				<c:forEach var="mofedContact" items="${aimEditActivityForm.contactInformation.mofedContacts}">
					<div>
						<b><c:out value="${mofedContact.contact.name}" /></b> 
						<b><c:out value="${mofedContact.contact.lastname}"/> </b>- 
						<c:forEach var="property" items="${mofedContact.contact.properties}">
							<c:if test="${property.name=='contact email'}">
								<b><c:out value="${property.value}" /></b> ;							
							</c:if>
						</c:forEach>
					</div>
				</c:forEach>
			</c:if> 
		</module:display>
		<hr>
		<module:display name="/Activity Form/Contacts/Project Coordinator Contact Information" parentModule="/Activity Form/Contacts">
			<digi:trn>Project Coordinator Contact Information</digi:trn>:&nbsp;
				<c:if test="${not empty aimEditActivityForm.contactInformation.projCoordinatorContacts}">
					<c:forEach var="projCoordinatorContact" items="${aimEditActivityForm.contactInformation.projCoordinatorContacts}">
						<div>
							<b><c:out value="${projCoordinatorContact.contact.name}"/></b> 
							<b><c:out value="${projCoordinatorContact.contact.lastname}" /></b>- 
							<c:forEach var="property"items="${projCoordinatorContact.contact.properties}">
								<c:if test="${property.name=='contact email'}">
									<b><c:out value="${property.value}" /></b> ;								</c:if>
							</c:forEach>
						</div>
					</c:forEach>
				</c:if>
		</module:display>
		<hr>		
		<module:display name="/Activity Form/Contacts/Sector Ministry Contact Information" parentModule="/Activity Form/Contacts">
			<digi:trn>Sector Ministry Contact Information</digi:trn>:&nbsp;
			<c:if test="${not empty aimEditActivityForm.contactInformation.sectorMinistryContacts}">
				<c:forEach var="sectorMinistryContact" items="${aimEditActivityForm.contactInformation.sectorMinistryContacts}">
					<div>
						<b><c:out value="${sectorMinistryContact.contact.name}" /></b>
						<b><c:out value="${sectorMinistryContact.contact.lastname}" /></b> -
						<c:forEach var="property" items="${sectorMinistryContact.contact.properties}">
							<c:if test="${property.name=='contact email'}">
								<b><c:out value="${property.value}" /></b>;							</c:if>
						</c:forEach>
					</div>
				</c:forEach>
			</c:if> 
		</module:display>
		<hr>			
		<module:display name="/Activity Form/Contacts/Implementing Executing Agency Contact Information" 
			parentModule="/Activity Form/Contacts">
			<digi:trn>Implementing/Executing Agency Contact Information</digi:trn>:&nbsp;
				<c:if test="${not empty aimEditActivityForm.contactInformation.implExecutingAgencyContacts}">
					<c:forEach var="implExecAgencyContact" items="${aimEditActivityForm.contactInformation.implExecutingAgencyContacts}">
						<div>
							<b><c:out value="${implExecAgencyContact.contact.name}" /></b>
							<b><c:out value="${implExecAgencyContact.contact.lastname}" /></b> -
							<c:forEach var="property" items="${implExecAgencyContact.contact.properties}">
								<c:if test="${property.name=='contact email'}">
									<b><c:out value="${property.value}" /></b> ;								</c:if>
							</c:forEach>
						</div>
					</c:forEach>
				</c:if>
		</module:display>
	</div>
</fieldset>
</module:display>
<!-- END CONTACT INFORMATION -->

<!-- PROPOSED PROJECT COST -->
<module:display name="/Activity Form/Donor Funding/Proposed Project Cost" parentModule="/Activity Form/Donor Funding">
<fieldset>
	<legend>
		<span class=legend_label id="proposedcostlink" style="cursor: pointer;">
			<digi:trn>Proposed Project Cost</digi:trn>
		</span>	</legend>
	<div id="proposedcostdiv">
		<c:if test="${aimEditActivityForm.funding.proProjCost!=null}">
			<table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa" width="100%" >
				<tr bgcolor="#f0f0f0">
					<td>
						<digi:trn key="aim:cost">Cost</digi:trn>					
					</td>
					<td bgcolor="#f0f0f0" align="left">
						<c:if test="${aimEditActivityForm.funding.proProjCost.funAmount!=null}">
							<b>${aimEditActivityForm.funding.proProjCost.funAmount}</b>						
						</c:if>&nbsp;
						<c:if test="${aimEditActivityForm.funding.proProjCost.currencyCode!=null}"> 
							<b>${aimEditActivityForm.funding.proProjCost.currencyCode}</b>						
						</c:if>					
					</td>
				</tr>
				<tr bgcolor="#f0f0f0">
					<td>
						<digi:trn>Date</digi:trn>					
					</td>
					<td bgcolor="#f0f0f0" align="left" width="150">
						<c:if test="${aimEditActivityForm.funding.proProjCost.funDate!=null}">
							<b>${aimEditActivityForm.funding.proProjCost.funDate}</b>						
						</c:if>					
					</td>
				</tr>
			</table>
		</c:if>
	</div>
</fieldset>
</module:display>
<!-- END PROPOSED PROJECT COST -->

<!-- COSTING -->
<feature:display name="Costing" module="Activity Costing">
<fieldset>
	<legend>
		<span class=legend_label id="costinglink" style="cursor: pointer;">
			<digi:trn>Costing</digi:trn>
		</span>	
	</legend>
	<div id=costingdiv>
		<table width="100%" style="font-size:11px;">
			<tr>
				<td>
					<bean:define id="mode" value="preview" type="java.lang.String" toScope="request"/> 
					<jsp:include page="viewCostsSummary.jsp" flush="" />				</td>
			</tr>
		</table>
	</div>
</fieldset>
</feature:display>
<!-- END COSTING -->
<!-- IPA Contracting -->
<feature:display name="Contracting" module="Contracting">
<fieldset>
	<legend>
		<span class=legend_label id="ipalink" style="cursor: pointer;">
			<digi:trn>IPA Contracting</digi:trn>
		</span>	
	</legend>
	<div id="ipadiv">
		<table width="100%">
			<tr>
				<td><!-- contents --> 
					<logic:notEmpty name="aimEditActivityForm" property="contracts">
					<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#DBDBDB">
						<c:forEach items="${aimEditActivityForm.contracts.contracts}" var="contract" varStatus="idx">
							<tr>
								<td bgColor=#f4f4f2 align="center" vAlign="top">
								<table width="100%" border="0" cellspacing="2" cellpadding="2" align="left" class="box-border-nopadding">
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Info/Contract Name" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:name" >Contract name:</digi:trn>											
											</td>
											<td><b>${contract.contractName}</b></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Info/Contract Description" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:description">Description:</digi:trn>											
											</td>
											<td><b>${contract.description}</b></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Info/Activity Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">										<tr>
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:actCat">Activity Category:</digi:trn>											
											</td>
											<td>
												<c:if test="${not empty contract.activityCategory}">
													<b>${contract.activityCategory.value}</b>
												</c:if>											
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Info/Contract Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">										
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:type">Type</digi:trn>:
											</td>
											<td>
												<c:if test="${not empty contract.type}">
													<b>${contract.type.value}</b>
												</c:if>
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Details/Start of Tendering" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:startOfTendering">Start of Tendering:</digi:trn>											
											</td>
											<td><b>${contract.formattedStartOfTendering}</b></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Details/Signature" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:signatureOfContract">Signature of Contract:</digi:trn>											
											</td>
											<td><b>${contract.formattedSignatureOfContract}</b></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Organizations" parentModule="/Activity Form/Contracts/Contract Item">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:contractOrg">Contract Organization:</digi:trn>											
											</td>
											<td>
												<c:if test="${not empty contract.organization}">
	                                            	<b>${contract.organization.name}</b>	                                            
	                                            </c:if>
	                                          </td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Details/Contractor Name" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:contractOrg">Contract Organization</digi:trn>:											
											</td>
											<td><b>${contract.contractingOrganizationText}</b></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Details/Completion" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:contractCompletion">Contract Completion:</digi:trn>											
											</td>
											<td><b>${contract.formattedContractCompletion}</b></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Details/Status" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:status">Status:</digi:trn>											
											</td>
											<td>
												<c:if test="${not empty contract.status}">
	                                            	<b>${contract.status.value}</b>	                                            
	                                           </c:if>											
	                                         </td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/Contract Total Value" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:totalAmount">Total Amount</digi:trn>:											
											</td>
											<td>
												<b>
												${contract.totalAmount}
												${contract.totalAmountCurrency}
												</b>											
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Contract Total Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left" colspan="2">
												<digi:trn key="aim:IPA:popup:totalECContribution">Total EC Contribution:</digi:trn>											
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:IB">IB</digi:trn>:											
											</td>
											<td>
												<b>
												${contract.totalECContribIBAmount}
												${contract.totalAmountCurrency}
												</b>											
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/INV Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:INV">INV:</digi:trn>											
											</td>
											<td>
												<b>
												${contract.totalECContribINVAmount}
												${contract.totalAmountCurrency}
												</b>											
											</td>
										</tr>
									</module:display>
									<tr>
										<td align="left" colspan="2">
											<digi:trn key="aim:IPA:popup:totalNationalContribution">Total National Contribution:</digi:trn>											
										</td>
									</tr>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Central Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:Central">Central</digi:trn>:
											</td>
											<td>
												<b>
												${contract.totalNationalContribCentralAmount}
												${contract.totalAmountCurrency}
												</b>
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Regional Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:Regional">Regional</digi:trn>:											
											</td>
											<td>
												<b>
												${contract.totalNationalContribRegionalAmount}
												${contract.totalAmountCurrency}
												</b>
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IFI Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:IFIs">IFIs</digi:trn>:											
											</td>
											<td>
												<b>
												${contract.totalNationalContribIFIAmount}
												${contract.totalAmountCurrency}
												</b>											
											</td>
										</tr>
									</module:display>
									<tr>
										<td align="left" colspan="2">
											<digi:trn key="aim:IPA:popup:totalPrivateContribution">Total Private Contribution:</digi:trn>											
										</td>
									</tr>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:IB">IB:</digi:trn></b>											
											</td>
											<td>
												<b>
												${contract.totalPrivateContribAmount}
												${contract.totalAmountCurrency}
												</b>											
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements" parentModule="/Activity Form/Contracts/Contract Item">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:totalDisbursements">Total Disbursements</digi:trn>:											
											</td>
											<td>
												<b>${contract.totalDisbursements}</b> &nbsp; 
											<logic:empty name="contract" property="dibusrsementsGlobalCurrency">
													&nbsp; <b>${aimEditActivityForm.currCode}</b>		                                        
											</logic:empty> 
		                                    <logic:notEmpty name="contract" property="dibusrsementsGlobalCurrency">
											&nbsp; <b>${contract.dibusrsementsGlobalCurrency}</b>		                                         
											</logic:notEmpty>	                                        
											</td>
										</tr>
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:totalFundingDisbursements">Total Funding Disbursements</digi:trn>:											
											</td>
											<td>
												<b>${contract.fundingTotalDisbursements}</b> &nbsp; 
												<logic:empty name="contract" property="dibusrsementsGlobalCurrency">
													&nbsp;<b>${contract.totalAmountCurrency}</b>							              		
												</logic:empty> 
							              		<logic:notEmpty name="contract" property="dibusrsementsGlobalCurrency">
						              				&nbsp;<b>${contract.dibusrsementsGlobalCurrency}</b>
						              			</logic:notEmpty>
						              		</td>
										</tr>
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:contractExecutionRate">Contract Execution Rate</digi:trn>:											
											</td>
											<td>
												&nbsp; <b>${contract.executionRate}</b>											
											</td>
										</tr>
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:contractFundingExecutionRate">Contract Funding Execution Rate</digi:trn>:											
											</td>
											<td>&nbsp; <b>${contract.fundingExecutionRate}</b></td>
										</tr>
										<tr>
											<td colspan="2">
												<digi:trn key="aim:IPA:popup:disbursements">Disbursements:</digi:trn>											
											</td>
										</tr>
									<tr>
										<td>&nbsp;</td>
										<td>
											<logic:notEmpty name="contract" property="disbursements">
											<table>
												<c:forEach items="${contract.disbursements}" var="disbursement">
													<tr>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
														<td align="left" valign="top">
			                                                   <digi:trn>${contract.adjustmentTypeName.value}</digi:trn>
														
<%-- 															<c:if test="${disbursement.adjustmentType==0}"> --%>
<%-- 																<b><digi:trn key="aim:actual">Actual</digi:trn></b> --%>
<%-- 															</c:if>  --%>
<%-- 															<c:if test="${disbursement.adjustmentType==1}"> --%>
<%-- 																<b><digi:trn key="aim:planned">Planned</digi:trn></b> --%>
<%-- 															</c:if>														 --%>
														</td>
														</module:display>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
														<td align="left" valign="top">
															<b>${disbursement.amount}</b>														
														</td>
														</module:display>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
														<td align="left" valign="top">
															<b>${disbursement.currency.currencyName}</b>														
														</td>
														</module:display>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
														<td align="left" valign="top">
															<b>${disbursement.disbDate}</b>														
														</td>
														</module:display>
													</tr>
												</c:forEach>
											</table>
										</logic:notEmpty></td>
									</tr>
									<tr>
										<td colspan="2">
											<digi:trn key="aim:IPA:popup:fundingDisbursements">Funding Disbursements:</digi:trn>											
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td>
											<logic:notEmpty name="aimEditActivityForm"
											property="funding.fundingDetails">
											<table width="100%">
												<tr>
													<td>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
															<digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn>
														</module:display>
													</td>
													<td>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
															<digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn>
														</module:display>
													</td>
													<td>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
															<digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn>
														</module:display>													
													</td>
													<td>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
															<digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn>
														</module:display>
												</tr>
												<c:forEach items="${aimEditActivityForm.funding.fundingDetails}" var="fundingDetail">
													<logic:equal name="contract" property="contractName" value="${fundingDetail.contract.contractName}">
														<c:if test="${fundingDetail.transactionType == 1}">
															<tr>
																<td align="center" valign="top">
<%-- 			                                                   <digi:trn>${fundingDetail.adjustmentTypeName.value}</digi:trn> --%>
																
<%-- 																	<c:if test="${fundingDetail.adjustmentType==0}"> --%>
<%-- 																		<digi:trn key="aim:actual">Actual</digi:trn> --%>
<%-- 																	</c:if>  --%>
<%-- 																	<c:if test="${fundingDetail.adjustmentType==1}"> --%>
<%-- 																		<digi:trn key="aim:planned">Planned</digi:trn> --%>
<%-- 																	</c:if>																 --%>
																</td>
																<td align="center" valign="top">
																<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
																	<b>${fundingDetail.transactionAmount}</b>																
																</module:display>	
																</td>
																
																<td align="center" valign="top">
																<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
																	<b>${fundingDetail.currencyCode}</b>																
																</module:display>
																</td>
																<td align="center" valign="top">
																<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
																	<b>${fundingDetail.transactionDate}</b>
																</module:display>																
																</td>
															</tr>
														</c:if>
													</logic:equal>
												</c:forEach>
											</table>
										</logic:notEmpty></td>
									</tr>
								</module:display>									
									<field:display name="Contracting Amendments" feature="Contracting">
										<bean:define id="ct" name="contract" type="org.digijava.module.aim.dbentity.IPAContract"/>
										<tr>
											<td align="left">
												<digi:trn>Contracts financed by the lessor</digi:trn>:
											</td>
											<td>&nbsp;
												<%if(ct.getDonorContractFundinAmount()!=null){ %> 
													<b><%=BigDecimal.valueOf(ct.getDonorContractFundinAmount()).toPlainString()%></b>
												<%}%>
												&nbsp;&nbsp;&nbsp;&nbsp;
											<b>${contract.donorContractFundingCurrency.currencyName}</b>											
										</td>
										</tr>
										<tr>
											<td align="left"><digi:trn>Total amount of the contract by the lessor</digi:trn>:											
										</td>
											<td>&nbsp; 
												<%if(ct.getDonorContractFundinAmount()!=null){ %> 
													<b><%=BigDecimal.valueOf(ct.getTotAmountDonorContractFunding()).toPlainString()%></b>
												<%}%>
												&nbsp;&nbsp;&nbsp;&nbsp;
												<b>${contract.totalAmountCurrencyDonor.currencyName}</b>											
										</td>
										</tr>
										<tr>
											<td align="left"><digi:trn>Total contract amount from state</digi:trn>:											
											</td>
											<td>&nbsp; 
											<%if(ct.getDonorContractFundinAmount()!=null){ %> 
												<b><%=BigDecimal .valueOf(ct.getTotAmountCountryContractFunding()).toPlainString()%></b>
											<%}%>
												&nbsp;&nbsp;&nbsp;&nbsp;
												<b>${contract.totalAmountCurrencyCountry.currencyName}</b>											
											</td>
										</tr>
										<tr>
											<td colspan="2"><b><digi:trn>Amendments :</digi:trn></b>											</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td><logic:notEmpty name="contract"
												property="amendments"
											>
												<table width="100%">
													<tr>
														<th><digi:trn>Amount</digi:trn></th>
														<th><digi:trn>Currency</digi:trn></th>
														<th><digi:trn>Date</digi:trn></th>
														<th><digi:trn>Reference</digi:trn></th>
													</tr>
													<c:forEach items="${contract.amendments}"
														var="amendment"
													>
														<bean:define id="am" name="amendment"
															type="org.digijava.module.aim.dbentity.IPAContractAmendment"
														/>
														<tr>
															<td align="center" valign="top">
																<b>${amendment.amoutStr}</b>
															</td>
															<td align="center" valign="top">
																<b>${amendment.currency.currencyName}</b>
															</td>
															<td align="center" valign="top">
																<b>${amendment.amendDate}</b>
															</td>
															<td align="center" valign="top">
																<b>${amendment.reference}</b>
															</td>
														</tr>
													</c:forEach>
												</table>
											</logic:notEmpty></td>
										</tr>
									</field:display>
								</table>								</td>
							</tr>
						</c:forEach>
					</table>
				</logic:notEmpty></td>
			</tr>
		</table>
	</div>
</fieldset>
<!-- end IPA Contracting -->
</feature:display>
<!-- M&I -->
<field:display name="Activity Performance" feature="Activity Dashboard" >
<fieldset>
	<legend>
		<span class=legend_label id="milink" style="cursor: pointer;">
			<digi:trn>M&E</digi:trn>
		</span>	
	</legend>
	<div id="midiv">
		<%
			if (actPerfChartUrl != null) {
		%> 
		<img src="<%=actPerfChartUrl%>" width="370" height="450" border="0" usemap="#<%= actPerfChartFileName %>">
		<br>
		<br>
		<%
			} else {
		%> 
		<br>
			<span class="red-log">
				<digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
				<digi:trn key="aim:activityPerformanceChart">Activity-Performance chart</digi:trn>
			</span>
		<br>
		<br>
		<%
			}
		%>&nbsp;	</div>
</fieldset>
</field:display>
<!-- END M&I -->

<!-- PROJECT RISK -->
<field:display name="Project Risk" feature="Activity Dashboard">
<fieldset>
	<legend>
		<span class=legend_label id="projectrisklink" style="cursor: pointer;">
			<digi:trn>Project Risk</digi:trn>
		</span>	
	</legend>
	<div id="projectriskdiv">
		<digi:trn key="aim:meActivityRisk" > Activity - Risk</digi:trn> <br />
		<digi:trn key="aim:overallActivityRisk">Overall Risk</digi:trn>
			<%
				if (actRiskChartUrl != null) {
			%> 
			<img src="<%=actRiskChartUrl%>" align="bottom" width="370" height="350" border="0" usemap="#<%= actRiskChartFileName %>"> 
			<br>
			<br>
			<%
				} else {
			%> 
			<br>
			<span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
			<digi:trn key="aim:activityRiskChart">Activity-Risk chart</digi:trn>
			</span><br>
			<br>
			<%
				}
			%>
	</div>
</fieldset>
</field:display>
<!-- END PROJECT RISK -->
<%@include file="previewActivityStructures.jsp" %>
<br/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
    	<input type="button" value="<digi:trn>Collapse All</digi:trn>" class="buttonx_sm" id="collapseall_1">    
    </td>
    <td align=right>
		<logic:present name="currentMember" scope="session">
    	<c:set var="trn"><digi:trn>Version History</digi:trn></c:set>		
    	<input type="button" class="buttonx_sm" onclick="javascript:previewHistory(<%=request.getAttribute("actId")%>); return false;" value="${trn}"/> 
   		<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
			<feature:display name="Edit Activity" module="Previews">
				<field:display feature="Edit Activity" name="Edit Activity Button">
					<logic:equal name="aimEditActivityForm" property="buttonText" value="edit">
						<c:set var="trn"><digi:trn>Edit</digi:trn></c:set>
						<input type="button" class="buttonx_sm" onclick="javascript:editActivity()" value="${trn}"/>
					</logic:equal>
					<logic:equal name="aimEditActivityForm" property="buttonText" value="validate">
						<c:set var="trn"><digi:trn>Validate</digi:trn></c:set>							
						<input type="button" class="buttonx_sm" onclick="javascript:editActivity()" value="${trn}"/>
					</logic:equal>
				</field:display>
			</feature:display>
		</module:display>   	
		</logic:present>
		</td>
  </tr>
</table>
</div></td>
  </tr>
</table>
</c:if>



<!-- MAIN CONTENT PART END -->
</digi:form>
<script language="JavaScript">
	$(document).ready(function(){
		expandAll();
	});
	$("#identificationlink").click(function() {
		  $("div#identificationdiv").toggle('slow', function() {
		   //$("#toggleidentification").removeClass("toogle_open").addClass("toogle_close");
		  });
	});
	$("#planninglink").click(function() {
		  $("div#planningdiv").toggle('slow', function() {
		  });
	});
	$("#locationlink").click(function() {
		  $("div#locationdiv").toggle('slow', function() {
		  });
	});
	$("#programlink").click(function() {
		  $("div#programdiv").toggle('slow', function() {
		  });
	});
	$("#sectorslink").click(function() {
		  $("div#sectorsdiv").toggle('slow', function() {
		  });
	});
	$("#fundinglink").click(function() {
		  $("div#fundingdiv").toggle('slow', function() {
		  });
	});
	$("#nationalplanlink").click(function() {
		  $("div#nationalplandiv").toggle('slow', function() {
		  });
	});
	$("#regionalfundinglink").click(function() {
		  $("div#regionalfundingdiv").toggle('slow', function() {
		  });
	});
	$("#componentlink").click(function() {
		  $("div#componentdiv").toggle('slow', function() {
		  });
	});
	$("#issueslink").click(function() {
		  $("div#issuesdiv").toggle('slow', function() {
		  });
	});
	
	$("#documentslink").click(function() {
		  $("div#documnetsdiv").toggle('slow', function() {
		  });
	});
	$("#relatedorglink").click(function() {
		  $("div#relateorgdiv").toggle('slow', function() {
		  });
	});
	$("#contactlink").click(function() {
		  $("div#contactdiv").toggle('slow', function() {
		  });
	});
	$("#milink").click(function() {
		  $("div#midiv").toggle('slow', function() {
		  });
	});
	$("#projectrisklink").click(function() {
		  $("div#projectriskdiv").toggle('slow', function() {
		  });
	});
	$("#proposedcostlink").click(function() {
		  $("div#proposedcostdiv").toggle('slow', function() {
		  });
	});
	$("#costinglink").click(function() {
		  $("div#costingdiv").toggle('slow', function() {
		  });
	});
	$("#ipalink").click(function() {
		  $("div#ipadiv").toggle('slow', function() {
		  });
	});
	$("#collapseall").click(function() {
		var showOrHide;  
			  if($("#collapseall").attr('value')== '<digi:trn>Collapse All</digi:trn>'){ 
			  		$("#collapseall").attr('value','<digi:trn>Expand All</digi:trn>');
			  		$("#collapseall_1").attr('value','<digi:trn>Expand All</digi:trn>');
			  		showOrHide=false;
		  	  }else{
			  		$("#collapseall").attr('value','<digi:trn>Collapse All</digi:trn>');
			  		$("#collapseall_1").attr('value','<digi:trn>Collapse All</digi:trn>');
			  		showOrHide=true;
		  	  }
		  $("div#identificationdiv,div#planningdiv,div#locationdiv,div#programdiv,div#sectorsdiv,div#fundingdiv,div#componentdiv,div#issuesdiv,div#documnetsdiv,div#relateorgdiv,div#contactdiv,div#midiv,div#projectriskdiv,div#costingdiv,div#ipadiv,div#proposedcostdiv,div#regionalfundingdiv,div#nationalplandiv").toggle(showOrHide);
	});
	
	$("#collapseall_1").click(function() {
		var showOrHide;  
			  if($("#collapseall_1").attr('value')== '<digi:trn>Collapse All</digi:trn>'){ 
			  		$("#collapseall_1").attr('value','<digi:trn>Expand All</digi:trn>');
			  		$("#collapseall").attr('value','<digi:trn>Expand All</digi:trn>');
			  		showOrHide=false;
		  	  }else{
			  		$("#collapseall_1").attr('value','<digi:trn>Collapse All</digi:trn>');
			  		$("#collapseall").attr('value','<digi:trn>Collapse All</digi:trn>');
			  		showOrHide=true;
		  	  }
			  $("div#identificationdiv,div#planningdiv,div#locationdiv,div#programdiv,div#sectorsdiv,div#fundingdiv,div#componentdiv,div#issuesdiv,div#documnetsdiv,div#relateorgdiv,div#contactdiv,div#midiv,div#projectriskdiv,div#costingdiv,div#ipadiv,div#proposedcostdiv,div#regionalfundingdiv,div#nationalplandiv").toggle(showOrHide);
	});
	
	//Change map URLs to popin
	var chartAreas = $("map[name='<%= actPerfChartFileName %>']").find("area");
	if (chartAreas != null) {
		$.each(chartAreas, function (idx, val) {
				//console.log (val.href);
				val.href = "javascript:showIndicatorValsPopin(\"" + val.href + "\")";
			});
	}
	
	var chartAreasRisk = $("map[name='<%= actRiskChartFileName %>']").find("area");
	if (chartAreasRisk != null) {
		$.each(chartAreasRisk, function (idx, val) {
				//console.log (val.href);
				val.href = "javascript:showIndicatorValsPopin(\"" + val.href + "\")";
			});
	}
	
	
	//Init indicator popin
	var msg="\n<digi:trn>Indicator values</digi:trn>";
	myPanel.setHeader(msg);
	myPanel.setBody("");
	myPanel.render(document.body);	
	
	
	function showIndicatorValsPopin (url) {
		YAHOOAmp.util.Connect.asyncRequest("GET", url, callback);
	}
	
	var responseSuccess = function(o){
		var response = o.responseText; 
		myPanel.setBody(response);
		myPanel.show();
	}

	var responseFailure = function(o){ 
		alert("Connection Failure!"); 
	}
	
	var callback = 
	{ 
		success:responseSuccess, 
		failure:responseFailure 
	}; 
	
	
	$("#mapPreviewThumbnail").click(function (obj) {
		$("#zoomMapContainer").css("left", obj.pageX - 250 + "px").css("top", obj.pageY - 250 + "px").css("display", "block");
	});
	
	
	
</script>