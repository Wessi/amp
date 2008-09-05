<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@page import="java.util.*"%>

<digi:instance property="calendarViewForm"/>
<table border="0" border-color="red" bgcolor="f5f8e5" width="100%" cellspacing="" cellpadding="1" >
  <tr>
    <td >
      <table border="0" width="100%" height="40px" bgcolor="f5f8e5">
        <tr >
          <c:if test="${calendarViewForm.view != 'custom'}">

            <td align="right" width="40%" vAlign="center">
              <digi:img src="module/calendar/images/calenderLeftArrow1.jpg"/>
              <a href="#"  onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.leftTimestamp}');return(false);"><digi:trn key="aim:last">Last</digi:trn></a>
            </td>
          </c:if>
          <td align="center" >
            <span id="calendarFont">
              <c:if test="${calendarViewForm.view == 'yearly'}">
              ${calendarViewForm.baseDateBreakDown.year}
              </c:if>
              <c:if test="${calendarViewForm.view == 'monthly'}">

                <digi:trn key="aim:calendar:basemonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>,&nbsp;
                ${calendarViewForm.baseDateBreakDown.year}

              </c:if>
              <c:if test="${calendarViewForm.view == 'weekly'}">

                <digi:trn key="aim:calendar:startmonthNameShort:${calendarViewForm.startDateBreakDown.monthNameShort}">${calendarViewForm.startDateBreakDown.monthNameShort}</digi:trn>
                ${calendarViewForm.startDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.startDateBreakDown.year}&nbsp;-&nbsp;
                <digi:trn key="aim:calendar:endmonthNameShort:${calendarViewForm.endDateBreakDown.monthNameShort}">${calendarViewForm.endDateBreakDown.monthNameShort}</digi:trn>
                ${calendarViewForm.endDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.endDateBreakDown.year}
              </c:if>
              <c:if test="${calendarViewForm.view == 'daily'}">
                <digi:trn key="aim:calendar:dailymonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>
                ${calendarViewForm.baseDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.baseDateBreakDown.year}
              </c:if>
              <c:if test="${calendarViewForm.view == 'custom'}">
                <digi:trn key="aim:calendar:startmonthNameLong:${calendarViewForm.startDateBreakDown.monthNameLong}">${calendarViewForm.startDateBreakDown.monthNameLong}</digi:trn>
                ${calendarViewForm.startDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.startDateBreakDown.year}&nbsp;-&nbsp;
                <digi:trn key="aim:calendar:endmonthNameLong:${calendarViewForm.endDateBreakDown.monthNameLong}">${calendarViewForm.endDateBreakDown.monthNameLong}</digi:trn>
                ${calendarViewForm.endDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.endDateBreakDown.year}
              </c:if>
            </span>
          </td>
          <c:if test="${calendarViewForm.view != 'custom'}">
            <td align="left" width="40%">
              <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.rightTimestamp}');return(false);"><digi:trn key="aim:next">Next</digi:trn></a>	<digi:img src="module/calendar/images/calenderRightArrow1.jpg"/>
            </td>
          </c:if>
        </tr>
      </table>
    </td>
  </tr>
  <tr width="100%" height="3px" bgcolor=#ffffff>

    <td  >
    </td>
  </tr>
  <tr width="98%">
    <td align="center" vAlign="center">


      <table border="0"   width="100%" cellpadding="1" cellspacing="1" bgcolor=#f5f8e5 ailgn="center">

        <%--   f5f8e5     <td align="center" width="140" <c:if test="${calendarViewForm.view == 'monthly'}">rowspan="2"</c:if>><digi:trn key="calendar:EventName">Event Name</digi:trn></td>
        <c:if test="${calendarViewForm.view != 'custom'}">
          <c:if test="${calendarViewForm.view == 'monthly'}">
            <c:forEach var="row" name="calendarViewForm" property="dateNavigator.items">
              <c:set var="firstItem" value="true"/>
              <c:forEach var="item" name="row">
                <c:if test="${firstItem}">
                  <td colspan="7" style="border-left:1px solid;">&nbsp;<digi:trn key="aim:calendar:month:${item.month}">${item.month}</digi:trn>&nbsp;${item.dayOfMonth} :(</td>
                </c:if>
                <c:set var="firstItem" value="false"/>
              </c:forEach>
            </c:forEach>
  </tr><tr>
          </c:if>--%>





          <c:if test="${calendarViewForm.view != 'custom'}">
            <c:if test="${calendarViewForm.view == 'monthly'}">
              <tr width="99%" align="center" vAlign="center" bgcolor="#f5f8e5">
                <td>
                  <table width="99%" border="0" bordercolor="blue" align="center" >
                    <tr width="100%">
                      <td valign="left"><digi:trn key="aim:m">M</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:t">T</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:w">W</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:t">T</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:f">F</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:s">S</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:s">S</digi:trn></td>
                    </tr>
                    <c:forEach var="row" items="${calendarViewForm.dateNavigator.items}">

                      <tr vAlign="center" bgcolor="#ffffff">

                        <c:forEach var="item" items="${row}" >
                          <td align="top" vAlign="top">
                            <c:if test="${!item.enabled}"><span style="color:#cbcbcb"></c:if>
                            ${item.dayOfMonth}
                            <c:if test="${!item.enabled}"></span></c:if>
                          </td>
                        </c:forEach>

                      </tr>
                      <c:forEach var="ampCalendarGraph" items="${calendarViewForm.ampCalendarGraphs}">



                        <c:set var="startDay">
                         ${ampCalendarGraph.ampCalendar.calendarPK.startDay}
                        </c:set>
                        <c:set var="endDay">
                         ${ampCalendarGraph.ampCalendar.calendarPK.endDay}
                        </c:set>
                        <c:set var="endMonth">
                        ${ampCalendarGraph.ampCalendar.calendarPK.endMonth+1}
                        </c:set>
                        <c:set var="startMonth">
                        ${ampCalendarGraph.ampCalendar.calendarPK.startMonth+1}
                        </c:set>
                         <c:set var="currentMonth">
                         ${calendarViewForm.baseDateBreakDown.month}
                        </c:set>
                        <tr vAlign="center" bgcolor="#ffffff">
                          <c:forEach var="item" items="${row}" >
                            <td align="top" vAlign="top" width="14%" >

                              <c:if test="${startMonth==currentMonth}">
                                <c:if test="${item.dayOfMonth==startDay&&item.enabled}">
                                  <digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
                                    <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
                                    ${ampCalendarEventItem.title}
                                    </c:forEach>
                                  </digi:link>
                                </c:if>
                              </c:if>

                              <c:if test="${startMonth!=currentMonth&&currentMonth==endMonth}">
                                <c:if test="${item.dayOfMonth==startDay&&!item.enabled}">
                                  <digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
                                    <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
                                    ${ampCalendarEventItem.title}
                                    </c:forEach>
                                  </digi:link>
                                </c:if>
                              </c:if>



                              <c:if test="${startMonth!=currentMonth&&endMonth!=currentMonth}">

                                <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                  <digi:img src="module/calendar/images/spacer.gif"  style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                </c:forEach>


                              </c:if>

                              <c:if test="${startMonth==currentMonth&&endMonth==currentMonth}">
                                <c:if test="${item.dayOfMonth>startDay && item.dayOfMonth<=endDay && item.enabled}">

                                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                    <digi:img src="module/calendar/images/spacer.gif"  style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                  </c:forEach>

                                </c:if>
                              </c:if>

                              <c:if test="${startMonth==currentMonth&&endMonth!=currentMonth}">
                                <c:if test="${item.dayOfMonth>startDay&&item.enabled}">

                                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                    <digi:img src="module/calendar/images/spacer.gif"  style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                  </c:forEach>

                                </c:if>
                                <c:if test="${item.dayOfMonth<endDay && !item.enabled}">

                                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                    <digi:img src="module/calendar/images/spacer.gif"  style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                  </c:forEach>

                                </c:if>
                              </c:if>

                              <c:if test="${startMonth!=currentMonth && endMonth==currentMonth}">
                                <c:if test="${item.dayOfMonth>startDay&&!item.enabled}">
                                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                    <digi:img src="module/calendar/images/spacer.gif"  style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                  </c:forEach>

                                </c:if>
                                <c:if test="${item.dayOfMonth<=endDay&&item.enabled}">
                                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                    <digi:img src="module/calendar/images/spacer.gif"  style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                  </c:forEach>

                                </c:if>
                              </c:if>



                            </td>

                          </c:forEach>

                        </tr>


                      </c:forEach>





                      <tr height="1px" bgcolor="f5f8e5"><td colspan="7" ></td></tr>
                    </c:forEach>
                  </table>
    </td>
              </tr>
                            </c:if>
                            <c:if test="${calendarViewForm.view == 'daily'}">
                              <tr width="100%">
                                <td width="100%">
                                  <table width="100%" bgcolor="ffffff" >
                                    <tr width="100%" bgcolor="f5f8e5">
                                      <td colspan="3">
                                      &nbsp;
                                      </td>
                                    </tr>
                                    <tr width="100%">
                                      <td width="75%">
                                        <table width="100%">
                                          <tr>
                                            <td align="center" vAlign="center">
                                              <digi:trn key="aim:regular">Regular Tasks or events</digi:trn>
                                            </td>
                                          </tr>
                                          <c:forEach var="hour" begin="6" end="18">
                                            <tr height="40px">
                                              <td align="left" width="20%" valign="top">
                                                <c:if test="${hour < 12}">
                                                  <c:if test="${hour < 10}">
                                                    <c:set var="hoursToDisplay" value="0${hour}"/>
                                                    ${hoursToDisplay} <digi:trn key="aim:am">AM</digi:trn>
                                                  </c:if>
                                                  <c:if test="${hour > 9}">
                                                    ${hour} <digi:trn key="aim:am">AM</digi:trn>
                                                  </c:if>
                                                </c:if>
                                                <c:if test="${hour > 11}">
                                                  <c:if test="${hour < 13}">
                                                    ${hour} <digi:trn key="aim:mp">PM</digi:trn>
                                                  </c:if>
                                                  <c:if test="${hour > 12}">
                                                    <c:set var="hoursToDisplay" value="0${hour - 12}"/>
                                                    ${hoursToDisplay} <digi:trn key="aim:mp">PM</digi:trn>
                                                  </c:if>
                                                </c:if>
                                              </td>
                                              <td>
                                                <c:forEach var="ampCalendarGraph" items="${calendarViewForm.ampCalendarGraphs}">

                                                  <c:set var="startHours">
                                                  ${ampCalendarGraph.ampCalendar.calendarPK.startHour}
                                                  </c:set>



                                                  <c:if test="${hour==startHours}">

                                                    <digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~~method=preview~resetForm=true">
                                                      <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
                                                      ${ampCalendarEventItem.title}
                                                      </c:forEach>
                                                    </digi:link>
                                                    <br/>

                                                  </c:if>
                                                </c:forEach>


                                              </td>
                                            </tr>
                                            <tr height="1px" bgcolor="f5f8e5"><td colspan="2"></td></tr>
                                          </c:forEach>
                                        </table>
                                </td>
                                <td width="1px" bgcolor="f5f8e5">

                                </td>
                                <td>
                                &nbsp;
                                </td>
                                    </tr>

                                  </table>
</td>
                              </tr>
                            </c:if>
  </tr>
            </c:if>
            <tr width="100%" >





              <td align="center" width="140" <c:if test="${calendarViewForm.view == 'monthly'}">rowspan="2"</c:if>>
              <c:if test="${calendarViewForm.view != 'daily' && calendarViewForm.view != 'monthly'}">
                <digi:trn key="calendar:EventName">Event Name</digi:trn>
              </c:if>
</td>
<c:forEach var="row" items="${calendarViewForm.dateNavigator.items}">

  <c:forEach var="item" items="${row}">



    <c:choose>
      <c:when test="${calendarViewForm.view == 'yearly'}">
        <td align="center" style="width:7%;border-left:1px solid" <c:if test="${item.nolink}">bgcolor="#ffbebe"</c:if>>
        <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}');return(false);"><digi:trn key="aim:calendar${item.month}">${item.month}</digi:trn></a>
</td>
      </c:when>
      <%-- <c:when test="${calendarViewForm.view == 'monthly'}">
        <td align="center" style="border-left:1px solid" <c:if test="${item.nolink}">bgcolor="#ffbebe"</c:if>>
        <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}');return(false);"><digi:trn key="aim:calendar:day:${item.dayOfWeek}">${item.dayOfWeek}</digi:trn></a>
</td>
</c:when>--%>

<c:when test="${calendarViewForm.view == 'weekly' && item.selected}">
  <td>
    <table  width="98%" cellspacing="1" cellpadding="2" border="0" bgcolor="#ffffff">
      <tr>
        <td align="left" vAlign="top">
          <span id="calenderSubFont">

            <digi:trn key="aim:dayOfWeek${item.dayOfWeek}">${item.dayOfWeek}</digi:trn>

          </span>
        </td>
        <td align="center" <c:if test="${item.nolink}">bgcolor="#ffbebe"</c:if>>
        <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}');return(false);">${item.dayOfMonth}/<digi:trn key="calendar:${item.month}">${item.month}</digi:trn>/${calendarViewForm.baseDateBreakDown.year}</a>
  </td>
      </tr>
    </table>
</td>
</c:when>
    </c:choose>
  </c:forEach>
</c:forEach>

<c:if test="${calendarViewForm.view == 'custom'}">
  <td align="center" width="80"><digi:trn key="calendar:BodyEventTypes">Event Types</digi:trn></td>
  <td align="center" width="80"><digi:trn key="calendar:BodyDonors">Donors</digi:trn></td>
  <td align="center" width="80"><digi:trn key="calendar:BodyAttendees">Attendees</digi:trn></td>
  <td>
    <table width="100%" cellspacing="0" cellpadding="0">
      <td width="25%" align="center"><digi:trn key="aim:calendar:Q1">Q1</digi:trn></td>
      <td width="25%" align="center"><digi:trn key="aim:calendar:Q2">Q2</digi:trn></td>
      <td width="25%" align="center"><digi:trn key="aim:calendar:Q3">Q3</digi:trn></td>
      <td width="25%" align="center"><digi:trn key="aim:calendar:Q4">Q4</digi:trn></td>
    </table>
  </td>
</c:if>
            </tr>
            <c:if test="${calendarViewForm.view != 'daily'&&calendarViewForm.view != 'monthly'}">
              <c:forEach var="ampCalendarGraph" items="${calendarViewForm.ampCalendarGraphs}">
                <tr>
                  <td align="center" style="border-top:1px solid;" width="80">
                    <div style="width:140;overflow:hidden">
                      <digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
                        <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
                        ${ampCalendarEventItem.title}
                        </c:forEach>
                      </digi:link>
                    </div>
                  </td>
                  <c:if test="${calendarViewForm.view == 'custom'}">
                    <td align="center" style="border-top:1px solid" width="80">
                    ${ampCalendarGraph.ampCalendar.eventType.name}
                    </td>
                    <td align="center" style="border-top:1px solid">
                      <select style="width:80">
                        <c:if test="${calendarViewForm.view == 'custom'}">
                          <c:forEach var="organization" items="${ampCalendarGraph.ampCalendar.organisations}">
                            <option>${organization.name}</option>
                          </c:forEach>
                        </c:if>
                      </select>
                    </td>
                    <td align="center" style="border-top:1px solid" width="80">
                      <select style="width:80">
                        <c:forEach var="attendee" items="${ampCalendarGraph.ampCalendar.attendees}">
                          <c:if test="${!empty attendee.member}">
                            <option>${attendee.member.user.firstNames} ${attendee.member.user.lastName}</option>
                          </c:if>
                          <c:if test="${!empty attendee.team}">
                            <option>${attendee.team.name}</option>
                          </c:if>
                          <c:if test="${!empty attendee.guest}">
                            <option>${attendee.guest}</option>
                          </c:if>
                        </c:forEach>
                      </select>
                    </td>
                    <td align="center" style="border-top:1px solid">
                      <table width="100%" cellspacing="0" cellpadding="0" border="0">
                        <tr>
                  </c:if>

                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                    <td align="center" valign="middle" <c:if test="${calendarViewForm.view == 'custom'}">width="25%"</c:if><c:if test="${calendarViewForm.view != 'custom'}">style="border-top:1px solid; border-left:1px solid"</c:if>>
                    <table cellpadding="0" cellspacing="0" width="100%" border="0">
                      <tr>
                        <c:if test="${ampCalendarGraphItem.left > 0}">
                          <td width="${ampCalendarGraphItem.left}%"><digi:img src="module/calendar/images/spacer.gif"/></td>
                        </c:if>
                        <c:if test="${ampCalendarGraphItem.center > 0}">
                          <td width="${ampCalendarGraphItem.center}%"><digi:img src="module/calendar/images/spacer.gif" style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/></td>
                        </c:if>
                        <c:if test="${ampCalendarGraphItem.right > 0}">
                          <td width="${ampCalendarGraphItem.right}%"><digi:img src="module/calendar/images/spacer.gif"/></td>
                        </c:if>
                      </tr>
                    </table>
</td>
                  </c:forEach>

                  <c:if test="${calendarViewForm.view == 'custom'}">
                        </tr></table></td>
                  </c:if>
                </tr>
              </c:forEach>
</c:if>
                      </table>
</td>
  </tr>
      </table>
