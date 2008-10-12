<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="calendarViewForm"/>

<script type="text/javascript">
function submitFilterForm(view, timestamp) {
  var form = document.getElementById('filterForm');
  if (form != null) {
    form.view.value = view;
    form.timestamp.value = timestamp;
    form.submit();
  }
}
function changeState() {
  var showPublicEvents = document.getElementById('showPublicEvents');
  if ( showPublicEvents.checked==true) {
    document.calendarViewForm.resetFilter.value=true;
  }
  else{
    document.calendarViewForm.resetFilter.value= false;

  }
}



</script>


<html:hidden name="calendarViewForm" property="view" value="${calendarViewForm.view}"/>
<html:hidden name="calendarViewForm" property="timestamp" value="${calendarViewForm.timestamp}"/>
<html:hidden name="calendarViewForm" property="filterInUse" value="true"/>
<html:hidden name="calendarViewForm" property="resetFilter" value="${calendarViewForm.resetFilter}"/>
<html:hidden name="calendarViewForm" property="filter.showPublicEvents" value="${filter.showPublicEvents}"/>

<div style="width:200px;height:120px;border:1px solid #CCECFF;font-family:Tahoma;">
  <div style="padding:5px;font-size:13px;font-weight:bold;color:White;background-color: #7B9EBD">
  <digi:trn key="calendar:eventTypes:page_header">Event Types</digi:trn>
  </div>
  <div style="overflow:auto;width:200px;height:92px;font-size:13px;font-weight:bold;">
    <c:if test="${!empty calendarViewForm.filter.eventTypes}">
      <table cellpadding="0" cellspacing="0">
        <c:forEach var="eventType" items="${calendarViewForm.filter.eventTypes}">
          <tr>
            <td style="background-color: #CCECFF;width:29px;padding:4px;text-align:center;">
              <div style="height: 15px; width: 24px; background-color: ${eventType.color}; border: solid 1px Black;">
              </div>
            </td>
            <td style="padding:5px;width:115px;text-align:center;font-weight:bold;">
            ${eventType.name}
            </td>
            <td>
              <html:multibox name="calendarViewForm" property="filter.selectedEventTypes" value="${eventType.id}"/>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </div>
</div>
<div style="width:200px;height:5px;font-family:Tahoma;">
&nbsp;
</div>
<div style="width:200px;height:120px;border:1px solid #CCECFF;font-family:Tahoma;">
  <div style="padding:5px;font-size:13px;font-weight:bold;color:White;background-color: #7B9EBD">
  <digi:trn key="calendar:bodydonors">Donors</digi:trn>
  </div>
  <div style="overflow:auto;width:200px;height:92px;font-size:13px;font-weight:bold;">
    <c:if test="${!empty calendarViewForm.filter.donors}">
      <table cellpadding="0" cellspacing="0">
        <tr>
          <td style="background-color: #CCECFF;width:29px;padding:4px;text-align:center;">
            <html:multibox name="calendarViewForm" property="filter.selectedDonors" value="None" />
          </td>
          <td style="padding:5px;width:115px;text-align:center;font-weight:bold;">
            <digi:trn key="calendar:donorsNone">None</digi:trn>
          </td>
        </tr>
        <c:forEach var="donor" items="${calendarViewForm.filter.donors}">
          <tr>
            <td style="background-color: #CCECFF;width:29px;padding:4px;text-align:center;font-weight:bold;">
              <html:multibox name="calendarViewForm" property="filter.selectedDonors" value="${donor.value}"/>
            </td>
            <td style="padding:5px;width:115px;text-align:center;font-weight:bold;">
              ${donor.label}
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </div>
</div>
<div style="padding:5px;width:190px;height:28px;">
  <html:checkbox styleId="showPublicEvents" name="calendarViewForm" property="filter.showPublicEvents" onchange="changeState()"/>
  <digi:trn key="calendar:showPublicEvents">
  &nbsp;Public events
  </digi:trn>
</div>
<div style="padding:5px;width:190px;height:28px;">
    <input type="submit" value="<digi:trn key="calendar:runFilter">Run Filter</digi:trn>" style="width:88px;" />
    &nbsp;
    <input type="reset" value="<digi:trn key="aim:btnreset">Reset</digi:trn>" style="width:88px;" />
</div>
