<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<jsp:include page="../../aim/view/scripts/newCalendar.jsp" flush="true" />

<digi:instance property="calendarViewForm"/>
<script type="text/javascript" src="<digi:file src="module/calendar/js/main.js"/>"></script>
<script type="text/javascript">
function selectCalendarType(view, type) {
    var form = document.getElementById('filterForm');
    if (form != null) {
        if (view == 'custom') {
            form.customViewStartDate.value = null;
            form.customViewEndDate.value = null;
        }
        form.view.value = view;
        form.submit();
    }
}
</script>

<table width="100%" style="border:1px solid; border-color: #CCECFF;width: 200px">
    <tr>
        <td nowrap="nowrap">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td nowrap="nowrap"><digi:trn key="calendar:CalendarType">&nbsp;Calendar Type&nbsp;&nbsp;</digi:trn></td>
                    <td>
                        <html:select name="calendarViewForm" property="selectedCalendarType" onchange="selectCalendarType('${calendarViewForm.view}', '${calendarViewForm.selectedCalendarType}')">
                            <bean:define id="types" name="calendarViewForm" property="calendarTypes" type="java.util.List"/>
                            <html:options collection="types" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<c:if test="${calendarViewForm.view == 'custom'}">
    <table width="100%" style="border:1px solid; border-color: #484846;width: 200px">
        <tr>
            <td nowrap="nowrap">
                <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td nowrap="nowrap"><digi:trn key="calendar:fromDate">&nbsp;From&nbsp;&nbsp;</digi:trn></td>
                        <c:if test="${calendarViewForm.selectedCalendarType == 0}">
              	           <td nowrap="nowrap">
                                <table cellpadding="0" cellspacing="0">
                                	<tr>
                                    	<td nowrap="nowrap">
                                    		<html:text styleId="customViewStartDate" name="calendarViewForm" property="customViewStartDate" style="width:80px"/>
                                    	</td>
                                    	<td nowrap="nowrap">&nbsp;
                                    		<a id="clear1" href="javascript:clearDate(document.calendarViewForm.customViewStartDate, 'clear1')" style="text-decoration:none">
									 			<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="CLEAR"/>
											</a>
                                    		<a id="date1" href='javascript:pickDateWithClear("date1",document.calendarViewForm.customViewStartDate,"clear1")' style="text-decoration:none">
												<img src="../ampTemplate/images/show-calendar.gif" alt="START DATE" border=0>
											</a>
                                   		</td>
                                   	</tr>
                                </table>
                            </td>
                        </c:if>


          				<c:if test="${calendarViewForm.selectedCalendarType != 0}">
                            <html:hidden styleId="customViewStartDate" name="calendarViewForm" property="customViewStartDate"/>
                            <td nowrap="nowrap">
                                <select id="customViewStartYear" onchange="updateDate(document.getElementById('customViewStartDate'), 'year', this.value)"></select>
                                <script type="text/javascript">
                                createYearCombo(document.getElementById('customViewStartYear'), document.getElementById('customViewStartDate').value);
                                </script>
                                <select id="customViewStartMonth" onchange="updateDate(document.getElementById('customViewStartDate'), 'month', this.value)">
                                    <c:forEach var="i" begin="1" end="13">
                                        <bean:define id="index" value="${i - 1}"/>
                                        <bean:define id="type" value="${calendarViewForm.selectedCalendarType}"/>
                                        <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                        <option value="${i}"/>
                                            <%=org.digijava.module.calendar.entity.DateBreakDown.getMonthName(Integer.parseInt(index), Integer.parseInt(type), false)%>
                                        </option>
                                    </c:forEach>
                                </select>
                                <script type="text/javascript">
                                selectOptionByValue(document.getElementById('customViewStartMonth'), get('month', document.getElementById('customViewStartDate').value));
                                </script>
                                <select id="customViewStartDay" onchange="updateDate(document.getElementById('customViewStartDate'), 'day', this.value)">
                                    <c:forEach var="i" begin="1" end="30">
                                        <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                        <option value="${i}"/>${i}</option>
                                    </c:forEach>
                                </select>
                                <script type="text/javascript">
                                selectOptionByValue(document.getElementById('customViewStartDay'), get('day', document.getElementById('customViewStartDate').value));
                                </script>
                            </td>
                        </c:if>
                    </tr>
                    <tr>
                        <td nowrap="nowrap">&nbsp;<digi:trn key="aim:calendar:to">To</digi:trn>&nbsp;&nbsp;</td>
                        <c:if test="${calendarViewForm.selectedCalendarType == 0}">
                            <td nowrap="nowrap">
                                <table cellpadding="0" cellspacing="0">
                                	<tr>
                                    	<td nowrap="nowrap">
                                    		<html:text styleId="customViewEndDate" name="calendarViewForm" property="customViewEndDate" style="width:80px"/>
                                    	</td>
                                    	<td nowrap="nowrap">&nbsp;
                                    		<a id="clear2" href="javascript:clearDate(document.calendarViewForm.customViewEndDate, 'clear2')" style="text-decoration:none">
									 			<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="CLEAR"/>
											</a>
                                    		<a id="date2" href='javascript:pickDateWithClear("date2",document.calendarViewForm.customViewEndDate,"clear2")' style="text-decoration:none">
												<img src="../ampTemplate/images/show-calendar.gif" alt="END DATE" border=0>
											</a>
										</td>
									</tr>
                           		</table>
                            </td>
                        </c:if>
                        <c:if test="${calendarViewForm.selectedCalendarType != 0}">
                            <html:hidden styleId="customViewEndDate" name="calendarViewForm" property="customViewEndDate"/>
                            <td nowrap="nowrap">
                                <select id="customViewEndYear" onchange="updateDate(document.getElementById('customViewEndDate'), 'year', this.value)"></select>
                                <script type="text/javascript">
                                createYearCombo(document.getElementById('customViewEndYear'), document.getElementById('customViewEndDate').value);
                                </script>
                                <select id="customViewEndMonth" onchange="updateDate(document.getElementById('customViewEndDate'), 'month', this.value)">
                                    <c:forEach var="i" begin="1" end="13">
                                        <bean:define id="index" value="${i - 1}"/>
                                        <bean:define id="type" value="${calendarViewForm.selectedCalendarType}"/>
                                        <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                        <option value="${i}"/>
                                            <%=org.digijava.module.calendar.entity.DateBreakDown.getMonthName(Integer.parseInt(index), Integer.parseInt(type), false)%>
                                        </option>
                                    </c:forEach>
                                </select>
                                <script type="text/javascript">
                                selectOptionByValue(document.getElementById('customViewEndMonth'), get('month', document.getElementById('customViewEndDate').value));
                                </script>
                                <select id="customViewEndDay" onchange="updateDate(document.getElementById('customViewEndDate'), 'day', this.value)">
                                    <c:forEach var="i" begin="1" end="30">
                                        <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                        <option value="${i}"/>${i}</option>
                                    </c:forEach>
                                </select>
                                <script type="text/javascript">
                                selectOptionByValue(document.getElementById('customViewEndDay'), get('day', document.getElementById('customViewEndDate').value));
                                </script>
                            </td>
                        </c:if>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</c:if>
<c:if test="${calendarViewForm.view != 'custom'}">
    <table width="100%" style="border-left: solid 1px #CCECFF;border-right: solid 1px #CCECFF;padding:2px;background-color:#376091;color:White;width: 200px">
        <tr>
            <td style="text-align:right;">
                <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.leftTimestamp}');return(false);">
                  <digi:img src="module/calendar/images/Previous.png" border="0" alt=""/>
                </a>
            </td>
            <td align="center" style="font-size:12px;font-family: Tahoma;">
                <c:if test="${calendarViewForm.view == 'yearly'}">
                  <%-- ${calendarViewForm.baseDateBreakDown.year - 1}--%>
                  &nbsp;
                  <b>${calendarViewForm.baseDateBreakDown.year}</b>
                  &nbsp;
                  <%-- ${calendarViewForm.baseDateBreakDown.year + 1}--%>
                </c:if>
                <c:if test="${calendarViewForm.view == 'monthly'}">	
                	<digi:trn key="aim:calendar:basemonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>
                        ${calendarViewForm.baseDateBreakDown.year}                
                </c:if>
                <c:if test="${calendarViewForm.view == 'weekly'}">
                    <digi:trn key="aim:calendar:weeklyMonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>
                        ${calendarViewForm.baseDateBreakDown.year}
                </c:if>
                <c:if test="${calendarViewForm.view == 'daily'}">                   
                      <digi:trn key="aim:calendar:dayMonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>
                        ${calendarViewForm.baseDateBreakDown.dayOfMonth},&nbsp;
                        ${calendarViewForm.baseDateBreakDown.year}                   
                </c:if>
            </td>
            <td>
                <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.rightTimestamp}');return(false);">
                  <digi:img src="module/calendar/images/Next.png" border="0" alt=""/>                 
                </a>
            </td>
        </tr>
    </table>
    <table width="100%" style="border: solid 1px #CCECFF;width: 200px">
        <c:forEach var="row" items="${calendarViewForm.dateNavigator.items}">
            <tr>
                <c:forEach var="item" items="${row}">
                  <td align="center" style='font-size:12px;padding:2px;font-family: Tahoma;<c:if test="${item.nolink}">color:Black;</c:if><c:if test="${item.selected}">color:Red;</c:if>'>
                    <c:if test="${!item.nolink}">
                      <a href="#" style="text-decoration:none;color:black;font-size:12px;font-family: Tahoma;" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}'); return(false);">
                    </c:if>
                    <c:if test="${!item.enabled}">
                      <span style="color:LightGrey;">
                    </c:if>
                    <c:choose>
                      <c:when test="${calendarViewForm.view == 'yearly'}">
                        <digi:trn key="aim:cal${item.month}">
                        ${item.month}
                        </digi:trn>
                      </c:when>
                      <c:otherwise>${item.dayOfMonth}</c:otherwise>
                    </c:choose>
                    <c:if test="${!item.enabled}"></span></c:if>
                    <c:if test="${!item.nolink}"></a></c:if>
                  </td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
</c:if>
