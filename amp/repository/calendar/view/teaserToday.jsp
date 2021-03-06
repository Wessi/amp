
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="calendarForm"/>
	<logic:present name="calendarForm" property="eventsListToday">
	<table width="180" border="0" align="center" cellpadding="5" cellspacing="1" bordercolor="#FFFFFF" bgcolor="#006699">
		<tr>
			<td bgcolor="#FFFFFF">
				<logic:iterate id="eventsListToday" name="calendarForm" property="eventsListToday">
				<p><font color="#1A8CFF">&raquo;</font>
				<logic:present name="eventsListToday" property="title">
				<logic:present name="eventsListToday" property="description">
				<digi:link href="/showCalendarItemDetails.do" paramName="eventsListToday" paramId="activeCalendarItem" paramProperty="id">
				<bean:define id="title" name="eventsListToday" property="title" type="java.lang.String"/>
				<%=title%></digi:link></logic:present>
				<logic:notPresent name="eventsListToday" property="description"><a href='<bean:write name="eventsListToday" property="sourceUrl"/>' class="text">
				<bean:define id="title" name="eventsListToday" property="title" type="java.lang.String"/>
				<%=title%></a></logic:notPresent></logic:present><br>
				<span class="dta"><i>
				<bean:write name="eventsListToday" property="startDate"/>-
				<bean:write name="eventsListToday" property="endDate"/></i></span></p></logic:iterate>
			</td>
		</tr>
		<tr>
			<td bgcolor="#FFFFFF">
				<div align="right">
					<digi:link href="/viewEvents.do"><font color="#1A8CFF" size="-2">
					<digi:trn key="calendar:viewAll">View All</digi:trn></font></digi:link><br>
					<digi:secure authenticated="true">
					<digi:link href="/showCreateCalendarItem.do"><font color="#1A8CFF" size="-2">
					<digi:trn key="calendar:addEvent">Add event</digi:trn></font></digi:link></digi:secure>
				</div>
			</td>
		</tr>
	</table>
    </logic:present>	