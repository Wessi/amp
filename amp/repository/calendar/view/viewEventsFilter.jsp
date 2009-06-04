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
  var showPublicEvents = document.getElementById('showPublicEvents');
  if (showPublicEvents!=null && showPublicEvents.checked==true) {
    document.calendarViewForm.resetFilter.value=true;
  }
  else{
    document.calendarViewForm.resetFilter.value= false;

  }
} 
function changeDonorsAndEventTypesState(){
	changeDonorsState();
	changeEventTypesState();
}

function changeDonorsState(){	
	var donors= $("input[@id^='donors_']");
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
	var evntTypes= $("input[@id^='evType_']");
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
<html:hidden name="calendarViewForm" property="resetFilter" value="${calendarViewForm.resetFilter}"/>
<html:hidden name="calendarViewForm" property="resetDonors" value="${calendarViewForm.resetDonors}"/>
<html:hidden name="calendarViewForm" property="resetEventTypes" value="${calendarViewForm.resetEventTypes}"/>
<html:hidden name="calendarViewForm" property="filter.showPublicEvents" value="${filter.showPublicEvents}"/>

<feature:display name="Filter" module="Calendar">
	<field:display name="Event Type Filter" feature="Filter">
		<div style="width:220px;height:auto;max-height:120px;border:1px solid #CCECFF;font-family:Tahoma;vertical-align: top;">
		  <div style="padding:5px;font-size:12px;color:White;background-color: #376091;font-family:Tahoma;">
		  	<digi:trn key="calendar:eventTypes:page_header">Event Types</digi:trn>
		  </div>
		  <div style="overflow:auto;width:220px;height:auto;max-height:92px;font-size:12px;font-family:Tahoma;">
		    <c:if test="${!empty calendarViewForm.filter.eventTypes}">
		      <table cellpadding="0" cellspacing="0">
		        <c:forEach var="eventType" items="${calendarViewForm.filter.eventTypes}" varStatus="stat">
		          <tr>
		            <td style="background-color: #CCECFF;width:29px;padding:4px;text-align:center;">
		              <div style="height: 15px; width: 24px; background-color: ${eventType.color}; border: solid 1px Black;">
		              </div>
		            </td>
		            <td style="padding:5px;width:115px;text-align:left;font-weight:bold;" nowrap="nowrap">
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
		<div style="width:220px;height:auto;max-height:220px;border:1px solid #CCECFF;font-family:Tahoma;white-space: nowrap;">
		  <div style="padding:5px;font-size:12px;color:White;background-color: #376091;font-family: Tahoma;">
		  <digi:trn key="calendar:bodydonors">Donors</digi:trn>
		  </div>
		  <div style="overflow:auto;width:220px;height:auto;max-height:200px;font-size:12px;font-weight:bold;font-family:Tahoma;white-space: nowrap">
		    <c:if test="${!empty calendarViewForm.filter.donors}">
		      <table cellpadding="0" cellspacing="0">
		        <tr>
		          <td style="background-color: #CCECFF;width:29px;padding:4px;text-align:center;">
		            <html:multibox name="calendarViewForm" property="filter.selectedDonors" value="None" styleId="donors_none"/>
		          </td>
		          <td style="padding:5px;width:115px;text-align:left;font-weight:bold;white-space: nowrap;">
		            <digi:trn key="calendar:donorsNone">None</digi:trn>
		          </td>
		        </tr>
		        <c:forEach var="donor" items="${calendarViewForm.filter.donors}" varStatus="stat">
		          <tr>
		            <td style="background-color: #CCECFF;width:29px;padding:2px;text-align:center;font-weight:bold;">
		              <html:multibox name="calendarViewForm" property="filter.selectedDonors" value="${donor.value}" styleId="donors_${stat.index}"/>
		            </td>
		            <td style="padding:3px;width:115px;text-align:left;font-weight:bold;white-space: nowrap" title="${donor.label}" nowrap="nowrap">
		              <div style="white-space: nowrap;">${donor.label}</div> 
		            </td>
		          </tr>
		        </c:forEach>
		      </table>
		    </c:if>
		  </div>
		</div>
	</field:display>
	<div style="padding:5px;width:190px;height:28px;">
	  <html:checkbox styleId="showPublicEvents" name="calendarViewForm" property="filter.showPublicEvents" onchange="changeState()"/>
	  <digi:trn key="calendar:showPublicEvents">
	  &nbsp;Public events
	  </digi:trn>
	</div>
	<div style="padding:5px;width:200px;height:28px;">
		<field:display name="Run Filter Button" feature="Filter">
			<input type="submit" value="<digi:trn key="calendar:runFilter">Run Filter</digi:trn>" style="min-width:88px;" onclick="changeDonorsAndEventTypesState();"/>
		</field:display>
	    &nbsp;
	    <field:display name="Reset Filter Button" feature="Filter">
	    	<input type="reset" value="<digi:trn key="aim:btnreset">Reset</digi:trn>" style="width:88px;" />
	    </field:display>
	</div>
</feature:display>


