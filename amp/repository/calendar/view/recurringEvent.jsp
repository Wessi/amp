<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:instance property="calendarEventForm"/>


<script type="text/javascript">

function validateDuration(rec,duration){
/*
     if(rec < duration){
	
     	alert("The duration of appointment should be shorter than how often it recurs. Modify the duration of appointment or change the recurrence");

		return false;
     }else{

         return true;
	}
 	*/
 	return true;
}

function fnChk(elem){
	  <c:set var="errMsg">
	  <digi:trn>
	  Please enter numeric value only
	  </digi:trn>
	  </c:set>
	  
	  if (isNaN(elem.value) || elem.value<0 ) {
	    alert("${errMsg}");
	    elem.value = "";
	    elem.focus();
	    return false;
	  }
	  return true;
}

function eventType(){
        var Daily = document.getElementById("Daily").checked;
		var Weekly = document.getElementById("Weekly").checked;
		var Monthly = document.getElementById("Monthly").checked;
		var Yearly = document.getElementById("Yearly").checked;

		var recStartTime = document.getElementById("selectedEndHour").value;
		var recEndTime = document.getElementById("recSelectedEndHour").value;
		var recStartMinute = document.getElementById("selectedStartMinute").value;
		var recEndMinute = document.getElementById("recSelectedEndMinute").value;
		
		//document.getElementById("recurrSelectedStartTime").value = recStartTime+":"+recStartMinute;
		document.getElementById("recurrSelectedEndTime").value = recEndTime+":"+recEndMinute;
		
		
	    //var recStartDate = document.getElementById("recurrSelectedStartDate").value;
        var recEndDate = document.getElementById("recurrSelectedEndDate").value;
        //document.getElementById("recurrStrDate").value = recStartDate;
        document.getElementById("recurrEndDate").value = recEndDate;

        var occStartDate = document.getElementById("selectedStartDate").value;
        var occEndDate = document.getElementById("selectedEndDate").value;
    
        var start = parseInt(occStartDate.slice(0,occStartDate.indexOf("/")));
        var end = parseInt(occEndDate.slice(0,occEndDate.indexOf("/")));

        var daily_occurance_duration = end-start;

        var startMonth = parseInt(occStartDate.slice(3,5),10);
        var endMonth = parseInt(occEndDate.slice(3,5),10);

        var month_occurance_duration = endMonth - startMonth;

        var startYear = parseInt(occStartDate.slice(6));
        var endYear = parseInt(occEndDate.slice(6));
        var year_occurance_duration = endYear - startYear;


	    if(!Daily && !Weekly && !Monthly && !Yearly){
	   		alert('<digi:trn jsFriendly="true">please choose type of recurring event</digi:trn>');
			return false;
		}

		var periodValid = true;
    
	    if(Yearly){
        
    	//var yearlyMonth = document.getElementById("selectedStartYearlyMonth").value;
        
    		if(document.getElementById("recurrYearly").value=='' || document.getElementById("recurrYearly").value=='0'){
            	alert ('<digi:trn jsFriendly="true">Recurring period should be higher than 0</digi:trn>');
            	periodValid = false;
   			}else{
   	    		var rec = document.getElementById("recurrYearly").value;
        		document.getElementById("type").value = 'year';
        		//document.getElementById("hiddenYearMonth").value = yearlyMonth;
        		document.getElementById("hiddenMonth").value = '';
        		document.getElementById("weekDays").value = '';
				document.getElementById("hidden").value = rec;
	 		}
   		}
	
		if(Monthly){
			var month = document.getElementById("selectedStartMonth").value;
		 	if(document.getElementById("selectedStartMonth").value=='' || document.getElementById("selectedStartMonth").value=='0'){
	        	alert ('<digi:trn jsFriendly="true">Recurring period should be higher than 0</digi:trn>');
	            periodValid = false;
	   		}else{
		   	    document.getElementById("hidden").value = month;
	        	document.getElementById("type").value = 'month';
	        	document.getElementById("hiddenMonth").value = month;
	        	document.getElementById("weekDays").value = '';
			}
		}

		if(Daily){
	        var rec = document.getElementById("recurrDaily").value; 
        	if(document.getElementById("recurrDaily").value=='' || document.getElementById("recurrDaily").value=='0'){
            	alert ('<digi:trn jsFriendly="true">Recurring period should be higher than 0</digi:trn>');
            	periodValid = false;
   			}else{
   	    		document.getElementById("hidden").value = rec;
        		document.getElementById("type").value = 'day';
        		document.getElementById("hiddenMonth").value = '';
        		document.getElementById("weekDays").value = '';
        	}
    	}

		if(Weekly){
			var rec = document.getElementById("recurrWeekly").value;

			if(document.getElementById("recurrWeekly").value=='' || document.getElementById("recurrWeekly").value=='0'){
	            alert ('<digi:trn jsFriendly="true">Recurring period should be higher than 0</digi:trn>');
	            periodValid = false;
	   		}else{
				var result = "";
			    document.getElementById("type").value = 'week';
		        document.getElementById("hidden").value = rec;
		        document.getElementById("hiddenMonth").value = '';
		        for(i=0; i<document.getElementsByName("occurrWeekDays").length; i++){
					var elemId = ""+document.getElementsByName("occurrWeekDays")[i].id;
					if(document.getElementsByName("occurrWeekDays")[i].checked == true && elemId.indexOf('checkDay') !=-1){
						var day = document.getElementsByName("occurrWeekDays")[i].value;
						result += day;
					}
				}
				document.getElementById("weekDays").value = result;
	    	}
		}
		if (periodValid){
			submit();
		}
}

function checkSelectedDays(){
	var daysSelected = "" + document.getElementById("weekDays").value;
	if (daysSelected.length>0) {
		for(j=0; j<document.getElementsByName("occurrWeekDays").length; j++){
			var value = ""+document.getElementsByName("occurrWeekDays")[j].value;
			if (daysSelected.indexOf(value)!=-1){
				document.getElementsByName("occurrWeekDays")[j].checked = true;
			} else {
				document.getElementsByName("occurrWeekDays")[j].checked = false;
			}
		}
	}
}

function disableInputs(){

	var Daily = document.getElementById("Daily").checked;
	var Weekly = document.getElementById("Weekly").checked;
	var Monthly = document.getElementById("Monthly").checked;
	var Yearly = document.getElementById("Yearly").checked;

	for(var i=1; i<8; i++){
		var checkId = "checkDay" + i;
		document.getElementById(checkId).disabled = "disabled";
		//document.getElementById(checkId).checked = false;
	}
	document.getElementById("recurrDaily").disabled = "disabled";
	document.getElementById("recurrWeekly").disabled = "disabled";
	document.getElementById("recurrYearly").disabled = "disabled";
	document.getElementById("selectedStartMonth").disabled = "disabled";

	
	if (Daily){
		for(var i=1; i<8; i++){
			var checkId = "checkDay" + i;
			document.getElementById(checkId).checked = false;
		}
		document.getElementById("recurrDaily").disabled = "";
		document.getElementById("recurrWeekly").value = "";
		document.getElementById("recurrYearly").value = "";
	}

	if (Weekly){
		for(var i=1; i<8; i++){
			var checkId = "checkDay" + i;
			document.getElementById(checkId).disabled = "";
		}
		document.getElementById("recurrWeekly").disabled = "";
		document.getElementById("recurrDaily").value = "";
		document.getElementById("recurrYearly").value = "";
	}

	if (Monthly){
		for(var i=1; i<8; i++){
			var checkId = "checkDay" + i;
			document.getElementById(checkId).checked = false;
		}
		document.getElementById("selectedStartMonth").disabled = "";
		document.getElementById("recurrDaily").value = "";
		document.getElementById("recurrWeekly").value = "";
		document.getElementById("recurrYearly").value = "";
	}

	if (Yearly){
		for(var i=1; i<8; i++){
			var checkId = "checkDay" + i;
			document.getElementById(checkId).checked = false;
		}
		document.getElementById("recurrYearly").disabled = "";
		document.getElementById("recurrDaily").value = "";
		document.getElementById("recurrWeekly").value = "";
	}
	
}

</script>


<table border="0" cellPadding=2 cellSpacing=0 width="100%" >


<tr>
	<td style="font-family: Tahoma;">
	                <div style="padding: 1px;">
	                    <div style="padding:7px;text-align:center;background-color: #336699; font-size: 18px;color:white; font-weight: bold;">
	                        <digi:trn>Recurring Event Setup</digi:trn>
	                    </div>
	                </div>
	</td>
</tr>
	<tr>
		<td>
		 <table border="0" cellpadding="10" width="100%" style="border-style:solid;border-color:#1C5180;border-width: 1px" >
		 	<tr>
		 		<td>
			 		<table  border="0" width="100%">
					 		<tr bgcolor="white">
					 			<td>
					 				<input type="radio" name="typeofOccurrence" value="day" id="Daily" onclick="disableInputs();"/><digi:trn>Daily</digi:trn>
					 			</td>
					 		</tr>
					 		<tr>
					 			<td>
					 			 <table bgcolor="#F5F5F5" align="center" height="40px" width="300px" cellpadding="7"  style="border-style:solid;border-color:#1C5180;border-width: 1px">
					 			 	<tr>
							 		 	<td><digi:trn>Every</digi:trn></td>
							 	 		<td>
											<html:hidden  property="recurrPeriod" name="calendarEventForm"  styleId="testRecPer"/>
							 	 			<html:text name="calendarEventForm" property="recurrPeriod" size="7px" styleId="recurrDaily" onkeyup="fnChk(this)"/>
							 	 		</td>
							 	 		<td align="left"><digi:trn>Day(s)</digi:trn></td>
					 			 	</tr>
					 			 </table>
					 			</td>
					 		</tr>
					 		<tr><td height="20px">&nbsp;</td></tr>
					 		<tr>
							 	<td>
									<input type="radio"  name="typeofOccurrence" value="month" id="Monthly" onclick="disableInputs();"/><digi:trn>Monthly</digi:trn>
								 </td>
							   </tr>
					 		<tr>
				 			<td>
				 				<table bgcolor="#F5F5F5" align="center" height="40px" width="300px" cellpadding="7" style="border-style:solid;border-color:#1C5180;border-width: 1px">
				 				<!-- 	
				 					<tr>
							 	 		<td width="95px"><digi:trn>Recover Every Day</digi:trn></td>
							 	 		<td><input type="text" size="9px" name="recurrPeriod" id="recurrMonthly" value=""/></td>
							 	 	</tr>
							 	-->
							 	 	<tr>	
							 	 		<td ><digi:trn>Every</digi:trn></td>
							 	 		<td>
							 	 		<html:select property="selectedStartMonth" name="calendarEventForm" styleId="selectedStartMonth">
							 	 		 			<c:forEach var="month" begin="1" end="12">
                                                		<c:if test="${month < 10}"><c:set var="month" value="0${month}"/></c:if>
                                                         	<html:option value="${month}">${month}</html:option>
                               				 		</c:forEach>
							 	 		
							 	 		</html:select>
								 	 		
										</td>
										<td align="left"><digi:trn>Month(s)</digi:trn></td>
					 	 			</tr>
					 	 			
					 	 		</table>
					 	 	</td>	
				 		</tr>
				 		<tr><td height="40px">&nbsp;</td></tr>
			 		</table>
		 		</td>
			 		<td>
				 		<table  bgcolor="#F5F5F5">
						 		<tr bgcolor="white">
						 			<td><input type="radio" name="typeofOccurrence" value="week" id="Weekly" onclick="disableInputs();"/><digi:trn>Weekly</digi:trn></td>
						 		</tr>
								<tr>
									<td>
									<html:hidden  property="occurrWeekDays" name="calendarEventForm"  styleId="daysOfWeek"/>
									  <table bgcolor="#F5F5F5" align="center" height="100px" width="300px" cellpadding="5" style="border-style:solid;border-color:#1C5180;border-width: 1px">		
									 		<tr>
									 	 		<td><digi:trn>Every</digi:trn></td>
									 	 		<td>
													<html:text name="calendarEventForm" property="recurrPeriod" size="7px" styleId="recurrWeekly" onkeyup="fnChk(this)"/> 
													<!--<input type="text"  size="7px" name="recurrPeriod" id="recurrWeekly"/>-->
												</td>
									 	 		<td><digi:trn>Week (s)</digi:trn></td>
									 		</tr>
									 		<tr> 
									 			<td><input id="checkDay7" type="checkbox" name="occurrWeekDays" value="7" /><digi:trn>Sun</digi:trn></td>
									 			<td><input id="checkDay3" type="checkbox" name="occurrWeekDays" value="3" /><digi:trn>Wed</digi:trn></td>
									 			<td><input id="checkDay6" type="checkbox" name="occurrWeekDays" value="6" /><digi:trn>Saturday</digi:trn></td>
									 		</tr>
									 		<tr>
									 			<td><input id="checkDay1" type="checkbox" name="occurrWeekDays" value="1" /><digi:trn>Monday</digi:trn></td>
									 			<td><input id="checkDay4" type="checkbox" name="occurrWeekDays" value="4" /><digi:trn>Thur</digi:trn></td>
									 			<td>
									 		</tr>
									 		<tr>
									 			<td><input id="checkDay2" type="checkbox" name="occurrWeekDays" value="2" /><digi:trn>Tuesday</digi:trn></td>
									 			<td><input id="checkDay5" type="checkbox" name="occurrWeekDays" value="5" /><digi:trn>Friday</digi:trn></td>
									 		</tr>
								 		</table>
						 			</td>
						 		</tr>
				 		 		<tr bgcolor="white">
					 				<td colspan="4">
					 					<input type="radio" name="typeofOccurrence" value="year" id="Yearly" onclick="disableInputs();"/><digi:trn>Yearly</digi:trn>
					 				</td>
					 			</tr>
					 			<tr>
					 			  <td>
					 				<table bgcolor="#F5F5F5" align="center" height="40px" width="300px" cellpadding="7" style="border-style:solid;border-color:#1C5180;border-width: 1px">		
									  	<tr>
					 						<td><digi:trn>Every</digi:trn></td>
							 	 			<!--<td>
								 	 			<html:select property="selectedStartYear" name="calendarEventForm"  styleId="selectedStartYearlyMonth">
                                           	 		<c:forEach var="month" begin="1" end="12">
                                                		<c:if test="${month < 10}"><c:set var="month" value="0${month}"/></c:if>
                                                         	<html:option value="${month}">${month}</html:option>
                               				 		</c:forEach>
												</html:select>
						 	 		    	</td>
							 	 			--><td>
												<html:text name="calendarEventForm" property="recurrPeriod" size="7px" styleId="recurrYearly" onkeyup="fnChk(this)"/> 
												<!--<input type="text"  size="7px" name="recurrPeriod" id="recurrYearly" value=""/>-->
											</td>
							 	 			<td><digi:trn>Year(s)</digi:trn></td>
							 	 			 
							 	 	    </tr>
							  	   </table>
							  </td>
				 		  </tr>
			 		</table>
			 	</td>
				</tr>
		 		<c:if test="${calendarEventForm.typeofOccurrence == 'week'}">
					<script language="JavaScript" type="text/javascript">
						document.getElementById("Weekly").checked = true;
						document.getElementById("recurrDaily").value = "";
						document.getElementById("recurrYearly").value = "";
						disableInputs();
						
						var allselectedDays = document.getElementById("daysOfWeek").value;
						var days = allselectedDays.split("",allselectedDays.length);
						
						for(i=0; i<days.length; i++){
							var checkId = "checkDay" + days[i];
							document.getElementById(checkId).checked = true;
						}					
					</script>
				</c:if>
				<c:if test="${calendarEventForm.typeofOccurrence == 'day'}">
					<script language="JavaScript" type="text/javascript">
						document.getElementById("Daily").checked = true;
						document.getElementById("recurrWeekly").value = "";
						document.getElementById("weekDays").value = "";
						document.getElementById("recurrYearly").value = "";
						disableInputs();
						
					</script>
				</c:if>
		 	    <c:if test="${calendarEventForm.typeofOccurrence == 'year'}">
					<script language="JavaScript" type="text/javascript">
						document.getElementById("Yearly").checked = true;
						document.getElementById("recurrWeekly").value = "";
						document.getElementById("weekDays").value = "";
						document.getElementById("recurrDaily").value = "";
						disableInputs();
						
					</script>
				</c:if>
		 	   <c:if test="${calendarEventForm.typeofOccurrence == 'month'}">
					<script language="JavaScript" type="text/javascript">
						document.getElementById("Monthly").checked = true;
						document.getElementById("recurrWeekly").value = "";
						document.getElementById("weekDays").value = "";
						document.getElementById("recurrDaily").value = "";
						document.getElementById("recurrYearly").value = "";
						disableInputs();
						
					</script>
				</c:if>
		 </table>
		</td>
	</tr>


<tr>
		<td>
			<table bgcolor="#F5F5F5" border="0" cellPadding=2 cellSpacing=2 width="350px" style="border-style:solid;border-color:#1C5180;border-width: 1px">
				 		
				 		<tr><td><digi:trn>Time</digi:trn></td></tr>
				 		<tr>
				 		<!-- 
				 						 	 		<td><digi:trn>Start Time</digi:trn></td>
				 	 		<td>
				 	 				<select id="selectedStartHour">
                                       <c:forEach var="hour" begin="0" end="23">
                                         <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
                                         <option value="${hour}">${hour}</option>
                                       </c:forEach>
                                    </select>:
                                    <select id="selectedStartMinute">
                                         <c:forEach var="minute" begin="0" end="59">
                                           <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
                                           <option value="${minute}" class="inp-text">${minute}</option>
                                         </c:forEach>
			                      </select>
                                   
							</td>
				 	 		-->
				 	 		<td><digi:trn>End Time</digi:trn></td>
				 	 		<td>
				 	 			<select id="recSelectedEndHour">
                                                    <c:forEach var="hour" begin="0" end="23">
	                                                      <c:if test="${hour < 10}">
	                                                      	<c:set var="hour" value="0${hour}"/>
	                                                      </c:if>
	                                                      	<option value="${hour}">${hour}</option>
                                                    </c:forEach>
                                </select>:
                                <select id="recSelectedEndMinute">
                                      <c:forEach var="minute" begin="0" end="59">
                                        <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
                                        <option value="${minute}" >${minute}</option>
                                      </c:forEach>
			                      </select>
                      	    </td>
				 	 	 </tr>
				 	 	<!-- 
				 	 	<tr>
				 	 		<td>
				 	 			<digi:trn>Start date:</digi:trn>
				 	 		</td>
				 	 		 <td>
				 		 	 	<c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
		                                 
		                                     <table cellpadding="0" cellspacing="0">
		                                         <tr>
		                                                <td nowrap="nowrap">
		                                                  <html:text styleId="recurrSelectedStartDate" readonly="true" name="calendarEventForm" property="recurrStartDate" style="width:80px"/>
		                                                </td>
		                                                <td>&nbsp;</td>
		                                                <td>
		                                                  <a id="clear1" href="javascript:clearDate(document.getElementById('recurrSelectedStartDate'), 'clear1')">
		                                                    <digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
		                                                  </a>
		                                                  <a id="date1" href='javascript:pickDateWithClear("date1",document.getElementById("recurrSelectedStartDate"),"clear1")'>
		                                                    <img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
		                                                  </a>
		                                               </td>
		                                          </tr>
		                                      </table>
		                        	</c:if>
		 	                 	</td>
						 	 </tr>
				 	 	-->
				 	 	<tr>
				 	 		<td><digi:trn>End Date</digi:trn></td>
				 	 		<td>
			 	 				<c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
                                     <table cellpadding="0" cellspacing="0">
                                           <tr>
                                             <td nowrap="nowrap">
        	                                       <html:text styleId="recurrSelectedEndDate" readonly="true" name="calendarEventForm" property="recurrEndDate" style="width:80px"/>
                                             </td>
                                             <td>
            	                                 &nbsp;
                                             </td>
                                             <!--<td>
                                               <a id="clear2" href="javascript:clearDate(document.getElementById('recurrSelectedEndDate'),'clear2')">
                                                 <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" alt="Delete this transaction"/>
                                               </a>
                                             </td>
                                             --><td>
                	                             &nbsp;
                                             </td>
                                             <td>
                                               <a id="date2" href='javascript:pickDateWithClear("date2",document.getElementById("recurrSelectedEndDate"),"clear2")'>
                                                 <img src="/TEMPLATE/ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
                                               </a>
                                             </td>
                                           </tr>
                                      </table>
                                 </c:if>
				 	 		</td>
				 	 		
				 	 	</tr>
			 	</table>
			 	
		</td>
		
	</tr>
	<tr>
		<td align="center">
			<input type="button" onclick="eventType();" value="<digi:trn>Save And Close</digi:trn>"/>
		</td>
	</tr>
</table>

<script type="text/javascript">

checkSelectedDays();

</script>
