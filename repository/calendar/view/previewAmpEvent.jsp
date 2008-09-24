<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="calendarEventForm"/>

<digi:form action="/showCalendarEvent.do">

  <html:hidden styleId="hdnMethod" name="calendarEventForm" property="method"/>
  <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>

  <table>
    <tr>
      <td style="font-family: Tahoma; font-size: 12px;">
        <div style="padding: 10px;">
          <div style="padding: 7px; text-align: center; background-color: #CCECFF; font-size: 18px; font-weight: bold;">
            <digi:trn key="calendar:PreviewEvent">Preview Event</digi:trn>
          </div>
        </div>
        <div style="padding: 20px; background-color: #F5F5F5;">
          <table>
            <tr>
              <td style="font-size: 14px; font-weight: bold; padding: 10px;vertical-align:top;">
                <digi:trn key="calendar:eventTitle">Event Title:</digi:trn>
              </td>
              <td>
                <html:hidden name="calendarEventForm" property="eventTitle" value="${calendarEventForm.eventTitle}"/>
                ${calendarEventForm.eventTitle}
              </td>
            </tr>
            <tr>
              <td style="font-size: 14px; font-weight: bold; padding: 10px;vertical-align:top;">
                <digi:trn key="calendar:Description">Description:</digi:trn>
              </td>
              <td>
                <textarea rows="4" cols="25"></textarea>
              </td>
            </tr>
            <tr>
              <td style="font-size: 14px; font-weight: bold; padding: 10px;vertical-align:top;">
                <digi:trn key="calendar:CalendarType">Calendar type:</digi:trn>
              </td>
              <td>
                <html:hidden name="calendarEventForm" property="selectedCalendarTypeId" value="${calendarEventForm.selectedCalendarTypeId}"/>
                ${calendarEventForm.selectedCalendarTypeName}
              </td>
            </tr>
            <tr>
              <td style="font-size: 14px; font-weight: bold; padding: 10px;vertical-align:top;">
                <digi:trn key="calendar:EventType">Event type:</digi:trn>
              </td>
              <td>
                <html:hidden name="calendarEventForm" property="selectedEventTypeId" value="${calendarEventForm.selectedEventTypeId}"/>
                ${calendarEventForm.selectedEventTypeName}
              </td>
            </tr>
            <tr>
              <td style="font-size: 14px; font-weight: bold; padding: 10px;vertical-align:top;">
                <digi:trn key="calendar:Organisations">Organisations:</digi:trn>
              </td>
              <td>
                <html:select name="calendarEventForm" property="selectedEventOrganisations" multiple="multiple" size="5" styleId="organizationList" style="width: 220px; height: 70px;">
                  <c:if test="${!empty calendarEventForm.selectedEventOrganisationsCol}">
                    <html:optionsCollection name="calendarEventForm" property="selectedEventOrganisationsCol" value="value" label="label" />
                  </c:if>
                </html:select>
              </td>
            </tr>
            <tr>
              <td style="font-size: 14px; font-weight: bold; padding: 10px;vertical-align:top;">
                <digi:trn key="calendar:StartDate">Start date:</digi:trn>
              </td>
              <td>
                <html:hidden name="calendarEventForm" property="selectedStartDate" value="${calendarEventForm.selectedStartDate}"/>
                <html:hidden name="calendarEventForm" property="selectedStartTime" value="${calendarEventForm.selectedStartTime}"/>
                ${calendarEventForm.selectedStartDate}&nbsp;${calendarEventForm.selectedStartTime}
              </td>
            </tr>
            <tr>
              <td style="font-size: 14px; font-weight: bold; padding: 10px;vertical-align:top;">
                <digi:trn key="calendar:EndDate">End date:</digi:trn>
              </td>
              <td>
                <html:hidden name="calendarEventForm" property="selectedEndDate" value="${calendarEventForm.selectedEndDate}"/>
                <html:hidden name="calendarEventForm" property="selectedEndTime" value="${calendarEventForm.selectedEndTime}"/>
                ${calendarEventForm.selectedEndDate}&nbsp;${calendarEventForm.selectedEndTime}
              </td>
            </tr>
            <tr>
              <td style="font-size: 14px; font-weight: bold; padding: 10px;vertical-align:top;">
                <digi:trn key="calendar:Attendees">Attendees:</digi:trn>
              </td>
              <td>
                <html:select multiple="multiple" styleId="selreceivers" name="calendarEventForm" property="selectedAtts" size="11" styleClass="inp-text" style="width: 220px; height: 70px;">
                  <c:if test="${!empty calendarEventForm.selectedAttsCol}">
                    <html:optionsCollection name="calendarEventForm" property="selectedAttsCol" value="value" label="label" />
                  </c:if>
                </html:select>
              </td>
            </tr>
            <tr>
              <td style="font-size: 14px; font-weight: bold; padding: 10px;vertical-align:top;">
                <digi:trn key="calendar:PublicEvent">Public Event:</digi:trn>
              </td>
              <td>
                <html:hidden name="calendarEventForm" property="privateEvent" value="${calendarEventForm.privateEvent}"/>
                <c:if test="${calendarEventForm.privateEvent}"><digi:trn key="calendar:yes">Yes</digi:trn></c:if>
                <c:if test="${!calendarEventForm.privateEvent}"><digi:trn key="calendar:no">No</digi:trn></c:if>
              </td>
            </tr>
            <tr>
              <td style="font-size: 14px; font-weight: bold; padding: 10px;">
              </td>
              <td>
                <input type="submit" style="width: 100px;" value="<digi:trn key="calendar:savebutton">Save</digi:trn>" onclick="document.getElementById('hdnMethod').value = 'save'">
                &nbsp;
                <input type="submit" style="width: 100px;" value="<digi:trn key="calendar:editbutton">Edit</digi:trn>" onclick="document.getElementById('hdnMethod').value = ''">
                &nbsp;
                <input type="button" value="Delete" style="width: 100px;" />
              </td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
  </table>
</digi:form>

