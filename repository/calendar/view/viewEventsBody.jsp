<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<%@page import="java.util.*"%>
<%@page import="org.digijava.module.calendar.form.CalendarViewForm" %>
<%@page import="org.digijava.module.calendar.util.CalendarUtil"%>

<script type="text/javascript" charset="utf-8">
    var trn_today_button ='<digi:trn>Today</digi:trn>';
	var trn_day_tab ='<digi:trn>Day</digi:trn>';
	var trn_week_tab='<digi:trn>Week</digi:trn>';
	var trn_month_tab='<digi:trn>Month</digi:trn>';
	
//Translations for calendar locale_sv.js file 
//month full names
	
	var trn_month_january ='<digi:trn>January</digi:trn>';
	var trn_month_february ='<digi:trn>February</digi:trn>';
	var trn_month_march ='<digi:trn>March</digi:trn>';
	var trn_month_april ='<digi:trn>April</digi:trn>';
	var trn_month_may ='<digi:trn>May</digi:trn>';
	var trn_month_june ='<digi:trn>June</digi:trn>';
	var trn_month_july ='<digi:trn>July</digi:trn>';
	var trn_month_august ='<digi:trn>August</digi:trn>';
	var trn_month_september ='<digi:trn>September</digi:trn>';
	var trn_month_october ='<digi:trn>October</digi:trn>';
	var trn_month_november ='<digi:trn>November</digi:trn>';
	var trn_month_december ='<digi:trn>December</digi:trn>';
	
//month short names
	var trn_month_jan='<digi:trn>Jan</digi:trn>';
	var trn_month_feb='<digi:trn>Feb</digi:trn>';
	var trn_month_mar='<digi:trn>Mar</digi:trn>';
	var trn_month_apr='<digi:trn>Apr</digi:trn>';
	var trn_month_jun='<digi:trn>Jun</digi:trn>';
	var trn_month_jul='<digi:trn>Jul</digi:trn>';
	var trn_month_aug='<digi:trn>Aug</digi:trn>';
	var trn_month_sep='<digi:trn>Sep</digi:trn>';
	var trn_month_oct='<digi:trn>Oct</digi:trn>';
	var trn_month_nov='<digi:trn>Nov</digi:trn>';
	var trn_month_dec='<digi:trn>Dec</digi:trn>';

//day full names 
	
	var trn_day_sunday='<digi:trn>Sunday</digi:trn>';
	var trn_day_monday='<digi:trn>Monday</digi:trn>';
	var trn_day_tuesday='<digi:trn>Tuesday</digi:trn>';
	var trn_day_wednesday='<digi:trn>Wednesday</digi:trn>';
	var trn_day_thursday='<digi:trn>Thursday</digi:trn>';
	var trn_day_friday='<digi:trn>Friday</digi:trn>';
	var trn_day_saturday='<digi:trn>Saturday</digi:trn>';

//day shotr names 
	

	var trn_day_sun='<digi:trn>Sun</digi:trn>';
	var trn_day_mon='<digi:trn>Mon</digi:trn>';
	var trn_day_tue='<digi:trn>Tue</digi:trn>';
	var trn_day_wed='<digi:trn>Wed</digi:trn>';
	var trn_day_thu='<digi:trn>Thu</digi:trn>';
	var trn_day_fri='<digi:trn>Fri</digi:trn>';
	var trn_day_sat='<digi:trn>Sat</digi:trn>';

</script>

 <script src="<digi:file src="module/calendar/dhtmlxScheduler/dhtmlxcommon.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/scheduler.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/config.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/base.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/locale_sv.js"/>" language="JavaScript" type="text/javascript" ></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/event.js"/>"language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/load.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/lightbox.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/dhtmlxdataprocessor.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/property.js"/>" language="JavaScript" type="text/javascript"></script>
  <script src="<digi:file src="module/calendar/dhtmlxScheduler/recurring.js"/>" language="JavaScript" type="text/javascript"></script>

 <link rel="stylesheet" href="<digi:file src="module/calendar/css/layout.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/note.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/recurring.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/lightbox.css"/>"> 
 
<div style="display: none"><jsp:include page="viewEventsFilter.jsp" flush="true"/></div>

<c:set var="printButon"><%=request.getSession().getAttribute("print")%></c:set>
 <c:if test="${printButon}">
 	<style>
		.dhx_cal_container {
		font-size: 10pt;
		}
		.dhx_cal_event_line {
		 height: auto;
		}
		.dhx_cal_event_clear{
		 height: auto;
	
		}
		
	</style>
	<script type="text/javascript" charset="utf-8">
		  scheduler._click.dhx_cal_tab=function(){
		   var mode = this.getAttribute("name").split("_")[0];
		   scheduler.setCurrentView(scheduler._date,mode);
		    if(mode=="week"||mode=="day")
		      scheduler._els["dhx_cal_data"][0].style.height="1100px";
		 }
	
	</script>  
	  <table width="200px" height="40px">
	  	<tr>
	  		<td>	
	 	
		 	<a target="_blank" title="Printing" onclick="window.print();" style="cursor: pointer">
		 		<img width="20" vspace="2" hspace="2" height="30" border="0" alt="Printer Friendly" src="/TEMPLATE/ampTemplate/module/aim/images/printer.gif"/>
		 	</a>
	 		<input type="button" value="close" onClick="window.close()" width="20" height="30"/>
	 		</td>
	 	</tr>
	  </table>
 </c:if>

<style>
	<%=CalendarUtil.getEventTypesCss()%>
</style>
<script type="text/javascript" charset="utf-8">
<!--

	function init() {
		 
		var sections=[
		  			{key:1, label:"Section A"},
		  			{key:2, label:"Section B"},
		  			{key:3, label:"Section C"},
		  			{key:4, label:"Section D"},
		  			{key:5, label:"Section E"},
		  			{key:6, label:"Section F"},
		  			{key:7, label:"Section G"},
		  			{key:8, label:"Section H"},
		  			{key:9, label:"Section I"},
		  			{key:10, label:"Section G"},
		  			{key:11, label:"Section K"},
		  			{key:12, label:"Section L"}
		  			
		  		];		
		
		scheduler.config.xml_date="%Y-%m-%d %H:%i";
		scheduler.config.multi_day = true;
	
		var eth = getEthiopianCalendarDate(<%=request.getSession().getAttribute("year")%>,<%=request.getSession().getAttribute("month")%>,<%=request.getSession().getAttribute("day")%>);
		var myDate = new Date(eth);
		var ehtMonth = <%=request.getSession().getAttribute("month")%>;
		var type = calType(<%=request.getSession().getAttribute("type")%>);
		var printView = <%=request.getSession().getAttribute("view")%>;
		var printDate = <%=request.getSession().getAttribute("date")%>;


		if(printDate !=null){
		 var date = <%=request.getSession().getAttribute("date")%>;
		}else{
			date = myDate;
			}
		
		var defoultView = "month";
		if(printView!=null){
			if(printView == 1){
				defoultView = "day"

				}else if(printView == 2){
					defoultView = "week"

				}

			}
		
		
		scheduler.init('scheduler_here',date,defoultView,type, ehtMonth);
		scheduler.templates.event_text=function(start_date,end_date,ev){
			return "Text:<b> "+ev.text+"</b><br>"+"Descr."+ev.details;
		}
		scheduler.load("/calendar/showEvents.do");
		scheduler.templates.event_class=function(start_date,end_date,ev){
			if(ev.type != 0){
						return "event_"+ev.type;
				}
		  }
		
		 
		scheduler.attachEvent("onViewChange", function (mode , date){
			   document.getElementById("printView").value = mode;
			   document.getElementById("printDate").value = date;
			   //show corresponding view breadcrumb
			   var whichView=document.getElementById("viewSpan");
			   var monthly='<digi:trn>Monthly View</digi:trn>';
			   var weekly='<digi:trn>Weekly View</digi:trn>';
			   var daily='<digi:trn>Daily View</digi:trn>';
			   if(mode=='month'){
				   whichView.innerHTML=monthly;
			   }else if(mode=='week'){
				   whichView.innerHTML=weekly;
			   }else if(mode=='day'){
				   whichView.innerHTML=daily;
			   }			   
			});
		scheduler.attachEvent("onClick",function(id){
		    var ev = scheduler.getEvent(id);
			var eventId = ev.id;
			if(eventId.indexOf("#") != "-1"){
			var	eventId = eventId.slice(0,eventId.indexOf("#"));
			}
			<digi:context name="previewEvent" property="context/module/moduleinstance/showCalendarEvent.do" />
		        document.forms[0].action = "<%= previewEvent%>~ampCalendarId="+eventId+"~method=preview~resetForm=true";
		       document.forms[0].submit();
		    return true;
		});
		scheduler.createUnitsView("unit","section_id",sections);
		var printView = <%=request.getSession().getAttribute("print")%>
		
	if(!printView){
		 scheduler.templates.event_bar_text=function(start_date,end_date,ev){
		    var dotted= "..."; 
			var isDotted = ev.text.length >20 ? dotted : "";
		    var text = ev.text.substr(0,20)+isDotted;
	        var img = '<digi:img src="module/calendar/images/magnifier.png" height="12" width="12" align="left"/>';
	        return "<span title='"+"Title:"+text+" "+"StartDate:"+start_date+"EndDate:"+end_date+"'>"+img+""+text+"</span>";
		 }
	 }else{

		 scheduler.templates.event_bar_text=function(start_date,end_date,ev){
		        var text = ev.text.substr(0,20);
		        return "<span style='font-size: 10pt;'>"+text+"</span>";
			 }
		 

		 }
		scheduler.config.dblclick_create = false;
		scheduler.config.multi_days = true;
	/*
		scheduler.attachEvent("onViewChange",function(mode,date){

			if(mode == "month"){
						submitFilterForm('monthly',date.getTime()/1000); 
						
						return true;
					}
			});
		
	*/


	
	} 

function calType(type){
	switch (type){
	case 0:
		return "GRE";
		break;
	case 1:
		return "ETH";
		break;	

		}
}	
	
function getEthiopianCalendarDate(yyyy,mm,dd)
{
   var months = new Array(13);
   months[1]  = "jan";
   months[2]  = "feb";
   months[3]  = "mar";
   months[4]  = "apr";
   months[5]  = "may";
   months[6]  = "jun";
   months[7]  = "jul";
   months[8]  = "aug";
   months[9]  = "sep";
   months[10] = "oct";
   months[11] = "nov";
   months[12] = "dec";
   months[13] = "dec";
   var now = new Date(yyyy,mm,dd);

   if(now.getMonth()==0 || mm == 13){
   		var monthnumber = now.getMonth()+12;
   }else{
	   var monthnumber = now.getMonth();
	   }
   var monthname   = months[monthnumber];
   var monthday    = now.getDate();
 
   var dateString = monthname +
                    ' ' + monthday +
                	' ' + yyyy;
  return dateString;
}	
-->
</script>	
<style> 

</style>
<body>
<script type="text/javascript">
window.onload = function(){
	init();
	};

</script>
<input type="hidden" value=""  id="printView"/>
<input type="hidden" value=""  id="printDate"/>
	<div id="scheduler_here" class="dhx_cal_container"  style='padding:1% 0% 1% 0%; width:100%; height:100%; position:relative'>
		<div class="dhx_cal_navline">
			<div class="dhx_cal_prev_button">&nbsp;</div>
			<div class="dhx_cal_next_button">&nbsp;</div>
			<div class="dhx_cal_today_button"></div>
			<div class="dhx_cal_date"></div>
	<!-- <div  class="dhx_cal_tab" name="unit_tab" style="right:270px;"></div>  -->	 
			<div class="dhx_cal_tab" name="month_tab" style="right:205px;"></div>
			<div class="dhx_cal_tab" name="week_tab" style="right:140px;"></div>
			<div class="dhx_cal_tab" name="day_tab" style="right:76px;"></div>
			
		</div>
		<div class="dhx_cal_header">
		</div>
		<div class="dhx_cal_data">
		</div>		
	</div>
</body>