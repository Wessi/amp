<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>

<digi:instance property="calendarViewForm"/>

<script type="text/javascript">
function submitFilterForm(view, timestamp) {
	changeState();	
  	var form = document.getElementById('filterForm');
  	if (form != null) {
    	form.view.value = view;
    	form.timestamp.value = timestamp;
    	form.submit();
  	}
}

function changeState() {
	var publicFilter = document.getElementById('publicFilter').value;
	if (publicFilter!=null) {
	  document.calendarViewForm.showPublicEvents.value = publicFilter;
	}
	document.getElementById('filterForm').submit();
} 

function changeDonorsAndEventTypesState(){
	changeDonorsState();
	changeEventTypesState();
}

function openPrinter(){
var view = document.getElementById("printView").value;
var date = document.getElementById("printDate").value;
var myDate = new Date(date)

if(view!= null)
     {
 		window.open('/calendar/showCalendarView.do~filterInUse=false~view='+view+'~date='+myDate.valueOf()+'~print=true','mywindow','toolbar=no,location=no, width="10"10,height=600", directories=no,status=no,menubar=yes,scrollbars=yes,copyhistory=yes,resizable=yes');
	}
}

function changeDonorsState(){	
	var donors = new Array();
	var inputs = document.getElementsByTagName("input");
	var j = 0;
	for (var i = 0; i < inputs.length; i++) {
		if(inputs[i].id.indexOf("donors_") == 0){
			donors[j++]=inputs[i];
		}
	}
	var resetDonors=true;
	for(var i=0;i<donors.length;i++){
		if(donors[i].checked){
			resetDonors=false;
			break;
		}
	}
	if(resetDonors==true){
		document.calendarViewForm.resetDonors.value=true;
	}else{
		document.calendarViewForm.resetDonors.value=false;
	}
}

function changeEventTypesState(){
	var evntTypes = new Array();
	var inputs = document.getElementsByTagName("input");
	var j = 0;
	for (var i = 0; i < inputs.length; i++) {
		if(inputs[i].id.indexOf("evType_") == 0){
			evntTypes[j++]=inputs[i];
		}
	}
	var resetEventTypes=true;
	for(var i=0;i<evntTypes.length;i++){
		if(evntTypes[i].checked){
			resetEventTypes=false;
			break;
		}
	}
	if(resetEventTypes==true){
		document.calendarViewForm.resetEventTypes.value=true;
	}else{
		document.calendarViewForm.resetEventTypes.value=false;
	}	
}

</script>


<html:hidden name="calendarViewForm" property="view" value="${calendarViewForm.view}"/>
<html:hidden name="calendarViewForm" property="timestamp" value="${calendarViewForm.timestamp}"/>
<html:hidden name="calendarViewForm" property="filterInUse" value="true"/>
<html:hidden name="calendarViewForm" property="showPublicEvents" value="${calendarViewForm.showPublicEvents}"/>
<html:hidden name="calendarViewForm" property="resetDonors" value="${calendarViewForm.resetDonors}"/>
<html:hidden name="calendarViewForm" property="resetEventTypes" value="${calendarViewForm.resetEventTypes}"/>
<html:hidden name="calendarViewForm" property="filter.showPublicEvents" value="${filter.showPublicEvents}"/>

<feature:display name="Filter" module="Calendar">
	<field:display name="Event Type Filter" feature="Filter">
		<div class="right_menu">
			<div class="right_menu_header">
				<div class="right_menu_header_cont">
					<digi:trn>Event Types</digi:trn>
				</div>
			</div>
			<div class="right_menu_box" style="width:220px;">
	    		<c:if test="${!empty calendarViewForm.filter.eventTypes}">
	      			<table cellpadding="0" cellspacing="0">
	        			<c:forEach var="eventType" items="${calendarViewForm.filter.eventTypes}" varStatus="stat">
	         				 <tr>
	            				<td style="width:29px;padding-left:10px;text-align:center;">
	              					<div style="height: 15px; width: 24px; background-color: ${eventType.color}; border: solid 1px Black;">
	              					</div>
	            				</td>
	            				<td style="padding:5px;width:140px;text-align:left;font-size:11px;" nowrap="nowrap">
	             					<div style="white-space: nowrap;">${eventType.name}</div> 
	            				</td>
	            				<td>
	              					<html:multibox name="calendarViewForm" property="filter.selectedEventTypes" value="${eventType.id}" styleId="evType_${stat.index}"/>
	            				</td>
	          				</tr>
	        			</c:forEach>
	      			</table>
	    		</c:if>
			</div>
		</div>
	</field:display>	
	<div style="width:220px;height:5px;font-family:Tahoma;">
	&nbsp;
	</div>
	<field:display name="Donor Filter" feature="Filter">
		<div class="right_menu">
			<div class="right_menu_header">
				<div class="right_menu_header_cont"><digi:trn>Donors</digi:trn></div>
		  	</div>
		  	<div class="right_menu_box" style="width:220px; overflow: auto;">
			    <c:if test="${!empty calendarViewForm.filter.donors}">
			    	<ul style="width: 350px; list-style-type: none outside none; padding: 0px; font-size: 11px;white-space: nowrap;">
			        	<li style="white-space: nowrap; margin: 5px;">
			            	<html:multibox name="calendarViewForm" property="filter.selectedDonors" value="None" styleId="donors_none"/>
			            	<digi:trn>None</digi:trn>
			        	</li>
			        	<c:forEach var="donor" items="${calendarViewForm.filter.donors}" varStatus="stat">
			          		<li style="white-space: nowrap; margin: 5px;">
		              			<html:multibox name="calendarViewForm" property="filter.selectedDonors" value="${donor.value}" styleId="donors_${stat.index}"/>
		              			${donor.label} 
			          		</li>
			        	</c:forEach>
			      	</ul>
				</c:if>
			</div>
		</div>
	</field:display>
	<c:if test="${not empty sessionScope.currentMember}">
		<div style="padding:5px;width:210px;height:28px;">
	 	<select id="publicFilter" class="inp-text" style="width: 200px; max-width: 200px;" onchange="changeState()" name="filter.showPublicEvents" >
			<c:if test="${calendarViewForm.showPublicEvents == 0}">
				<option selected="true" value="0">	
			</c:if>
			<c:if test="${calendarViewForm.showPublicEvents != 0}">
				<option value="0">
			</c:if>
				<digi:trn>Public and private events</digi:trn>
			</option>

	  		<c:if test="${calendarViewForm.showPublicEvents == 1}">
				<option selected="true" value="1">	
			</c:if>
			<c:if test="${calendarViewForm.showPublicEvents != 1}">
				<option value="1">
			</c:if>
				<digi:trn>Only private events</digi:trn>
			</option>
	  		
			<c:if test="${calendarViewForm.showPublicEvents == 2}">
				<option selected="true" value="2">	
			</c:if>
			<c:if test="${calendarViewForm.showPublicEvents != 2}">
				<option value="2">
			</c:if>
				<digi:trn>Only public events</digi:trn>
			</option>
		</select>
	</div>
	</c:if>
	
	<div style="padding:5px;width:200px;height:28px;">
		<field:display name="Run Filter Button" feature="Filter">
			<input class="buttonx" type="submit" value="<digi:trn>Run Filter</digi:trn>" onclick="changeDonorsAndEventTypesState();"/>
		</field:display>
	    &nbsp;
	    <field:display name="Reset Filter Button" feature="Filter">
	    	<input class="buttonx" type="reset" value="<digi:trn>Reset</digi:trn>" />
	    </field:display>
	    &nbsp;
	      <input type="button" class="buttonx" value="<digi:trn key="calendar:print">Print</digi:trn>"  onclick="openPrinter();" />
	</div>
</feature:display>


