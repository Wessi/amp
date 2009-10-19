<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>

<digi:instance property="calendarEventForm"/>

<style  type="text/css">
<!--

.contentbox_border {
    border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}
-->
</style>

<script language="javascript">

function deleteEvent(){
	var err = '<digi:trn>Are You sure?</digi:trn>';
	if(confirm(err))
	{
		document.getElementById('hdnMethod').value = "delete";
		return true;
	}
	return false;
}


function openPrinter(){
	var id = document.getElementById('id').value;

	//<digi:context name="rev" property="/calendar/showCalendarEvent.do~method=print~resetForm=true" />
		//openURLinWindow("<%=rev%>",1024,768);
	window.open('/calendar/showCalendarEvent.do~method=print~resetForm=true~calendarId='+id+'','mywindow','toolbar=no,location=no, width=540,height=500, directories=no,status=no,menubar=yes,scrollbars=yes,copyhistory=yes,resizable=yes');
		
	}
</script>

<digi:form action="/showCalendarEvent.do">

  <html:hidden styleId="hdnMethod" name="calendarEventForm" property="method"/>
  <html:hidden styleId="id" name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>

  <table width="520">
  	 <tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
		<td height=33>
			<span class=crumb>&nbsp;
				<c:set var="translation">
					<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
				</c:set>
				<digi:link href="/../aim/showDesktop.do" styleClass="comment" title="${translation}" >
					<digi:trn key="aim:portfolio">Portfolio</digi:trn>
				</digi:link>&nbsp;&gt;&nbsp;
				<digi:link href="/../calendar/showCalendarView.do" styleClass="comment" title="${translation}">
					<digi:trn key="calendar:Calendar">Calendar</digi:trn>
				</digi:link>&nbsp;&gt;&nbsp;
				<digi:trn key="calendar:previewEvent">Preview Event</digi:trn>
			</span>
		</td>
	</tr>	
	<tr>
		<td height="16" vAlign="middle" width="520">
			<span class=subtitle-blue>	<digi:trn key="calendar:previewEvent">Preview  Event</digi:trn> </span>
		</td>
	</tr>
	<tr>				
        <td align="center" nowrap="nowrap" valign="top">
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
                <!-- calendar event always has a title -->
                <logic:empty name="calendarEventForm" property="eventTitle">
                	<tr style="height: 50px;">
                		<td style="text-align: center;font-family: Tahoma;font-size: 15px;font-weight:bold;color: grey;background-color: #F5F5F5;">
                			<digi:trn>The Event Doesn't Exist Any More </digi:trn>
                		</td>
                	</tr>
                </logic:empty>
				<logic:notEmpty name="calendarEventForm" property="eventTitle">
					<tr>
				      <td style="font-family: Tahoma; font-size: 12px;">        
				        <div style="padding: 20px; background-color: #F5F5F5;">
				          <table>
				            <tr>
				              <td style="text-align: right;font-family: Tahoma;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:evntTitle">Event title</digi:trn>
				              </td>
				              <td style="font-family: Tahoma;font-size: 12px;">
				                <html:hidden name="calendarEventForm" property="eventTitle" value="${calendarEventForm.eventTitle}"/>
				                ${calendarEventForm.eventTitle}
				              </td>
				            </tr>
				           <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="text-align: right;font-family: Tahoma;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:Description">Description</digi:trn>
				              </td>
				              <td>
				              	<html:textarea name="calendarEventForm" property="description" style="width: 220px;" readonly="true"/>                
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="text-align: right;font-family: Tahoma;font-size: 12px;font-weight:bold;"nowrap="nowrap">
				                <digi:trn key="calendar:cType">Calendar type</digi:trn>
				              </td>
				              <td style="font-family: Tahoma;font-size: 12px;">
				                <html:hidden name="calendarEventForm" property="selectedCalendarTypeId" value="${calendarEventForm.selectedCalendarTypeId}"/>
				                  <c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
				                        Gregorian                     
				                  </c:if>
				                  <c:if test="${calendarEventForm.selectedCalendarTypeId == 1}">
				                        Ethiopian                     
				                  </c:if>
				                  <c:if test="${calendarEventForm.selectedCalendarTypeId == 2}">
				                        Ethiopian_FY                     
				                  </c:if>
				              </td>
				             </tr>
				             <tr height="3px"><td colspan="2"></td></tr>
				            <feature:display name="Event Type" module="Calendar">
				            	<tr>
					              <td style="text-align: right;font-family: Tahoma;font-size: 12px;font-weight:bold;"nowrap="nowrap">
					                <digi:trn key="calendar:eventsType">Event type</digi:trn>
					              </td>
					              <td style="font-family: Tahoma;font-size: 12px;">
					                <html:hidden name="calendarEventForm" property="selectedEventTypeId" value="${calendarEventForm.selectedEventTypeId}"/>
					                ${calendarEventForm.selectedEventTypeName}
					              </td>
					            </tr>
					            <tr height="3px"><td colspan="2"></td></tr>
				            </feature:display>			            
				            <feature:display name="Donors" module="Calendar">
				            	<tr>
					              <td style="text-align: right;font-family: Tahoma;font-size: 12px; font-weight:bold;"nowrap="nowrap">
					                <digi:trn key="cal:organizations">Organizations</digi:trn>
					              </td>
					              <td>
					                <html:select name="calendarEventForm" property="selOrganizations" multiple="multiple" size="5" styleId="organizationList" style="width: 220px; height: 70px;">
					                    <html:optionsCollection name="calendarEventForm" property="organizations" value="ampOrgId" label="acronymAndName" style="font-family: Tahoma;font-size:11px;"/>
					                </html:select>
					              </td>
					            </tr>
					            <tr height="3px"><td colspan="2"></td></tr>
				            </feature:display>			            
				            <tr>
				              <td style="text-align: right;font-family: Tahoma;font-size: 12px;font-weight:bold;"nowrap="nowrap">
				                <digi:trn key="calendar:StDate">Start date</digi:trn>
				              </td>
				              <td style="font-family: Tahoma;font-size: 12px;">
				                <html:hidden name="calendarEventForm" property="selectedStartDate" value="${calendarEventForm.selectedStartDate}"/>
				                <html:hidden name="calendarEventForm" property="selectedStartTime" value="${calendarEventForm.selectedStartTime}"/>
				                ${calendarEventForm.selectedStartDate}&nbsp;${calendarEventForm.selectedStartTime}
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="text-align: right;font-family: Tahoma;font-size: 12px;font-weight:bold;"nowrap="nowrap">
				                <digi:trn key="calendar:EndDate">End Date</digi:trn>
				              </td>
				              <td style="font-family: Tahoma;font-size: 12px;">
				                <html:hidden name="calendarEventForm" property="selectedEndDate" value="${calendarEventForm.selectedEndDate}"/>
				                <html:hidden name="calendarEventForm" property="selectedEndTime" value="${calendarEventForm.selectedEndTime}"/>
				                ${calendarEventForm.selectedEndDate}&nbsp;${calendarEventForm.selectedEndTime}
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="text-align: right;font-family: Tahoma;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:Attendee">Attendee</digi:trn>
				              </td>
				              <td>
				                <html:select multiple="multiple" styleId="selreceivers" name="calendarEventForm" property="selectedAtts" size="11" styleClass="inp-text" style="width: 220px; height: 70px;">
				                  <c:if test="${!empty calendarEventForm.selectedAttsCol}">
				                    <html:optionsCollection name="calendarEventForm" property="selectedAttsCol" value="value" label="label" style="font-family: Tahoma;font-size:11px;"/>
				                  </c:if>
				                </html:select>
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="text-align: right;font-family: Tahoma;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:PublicEvent">Public Event</digi:trn>
				              </td>
				              <td style="font-family: Tahoma;font-size: 12px;">
				                <html:hidden name="calendarEventForm" property="privateEvent" value="${calendarEventForm.privateEvent}"/>
				                <c:if test="${calendarEventForm.privateEvent}"><digi:trn key="calendar:no">No</digi:trn></c:if>
				                <c:if test="${!calendarEventForm.privateEvent}"><digi:trn key="calendar:yes">Yes</digi:trn></c:if>
				              </td>
				            </tr>
				            <c:if test="${calendarEventForm.typeofOccurrence != null}">
					            <tr height="5px"><td colspan="2"></td></tr>
					            <tr>
					              <td style="text-align: right;font-family: Tahoma;font-size: 12px;font-weight:bold;" nowrap="nowrap">
					                <digi:trn>Recurring Event</digi:trn>
					              </td>
					              <td style="font-family: Tahoma;font-size: 12px;">
					                ${calendarEventForm.typeofOccurrence}
					                 ${calendarEventForm.weekDays}
					                 <c:if test="${calendarEventForm.selectedStartMonth != 0}"> ${calendarEventForm.selectedStartMonth}</c:if>
					                  <c:if test="${calendarEventForm.selectedStartYear != 0}"> ${calendarEventForm.selectedStartYear}</c:if>
					                 <c:if test="${calendarEventForm.recurrPeriod != 0}">${calendarEventForm.recurrPeriod}</c:if>
					                 <c:if test="${calendarEventForm.recurrStartDate != 0}">${calendarEventForm.recurrStartDate}</c:if>
					                 <c:if test="${calendarEventForm.recurrEndDate != 0}">${calendarEventForm.recurrStartDate}</c:if>
					                  
					              </td>
					            </tr>
				            </c:if>
				            <tr height="5px"><td colspan="2">&nbsp;</td></tr>
				            <tr>
				              <td>
				              </td>
				              <td>
				                <input type="submit"  value="<digi:trn>Save</digi:trn>" onclick="document.getElementById('hdnMethod').value = 'save'">
				                &nbsp;				               
								<c:if test="${calendarEventForm.actionButtonsVisible!=false}">
				                	<input type="submit"  value="<digi:trn>Edit</digi:trn>" onclick="document.getElementById('hdnMethod').value = ''">
				                	&nbsp;
				                	<input type="submit" value="<digi:trn>Delete</digi:trn>"  onclick="deleteEvent();" />
				               </c:if>
				                	&nbsp;
				                <input type="button" value="<digi:trn>Print</digi:trn>" onclick="openPrinter();" />
				              </td>
				            </tr>
				          </table>
				        </div>
				      </td>
				    </tr>
				</logic:notEmpty>
             </table>
       		</td>
    	</tr>
    </table>
	</td>
</tr>
</table>
</digi:form>