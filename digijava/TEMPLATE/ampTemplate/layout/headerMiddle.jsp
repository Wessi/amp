<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<logic:notPresent name="currentMember">
<DIV id="menu">
<UL>
		<LI class="noLink">
			<digi:link styleClass="head-menu-link" href="/reportsPublicView.do" module="aim" onclick="return quitRnot()">			
					::: <digi:trn key="aim:aidInformationModule">AID INFORMATION MODULE</digi:trn>
			</digi:link>
		</LI>				
		<LI class="noLink">
		<digi:link styleClass="head-menu-link" href="/viewTeamReports.do" module="aim" onclick="return quitRnot()">
				::: <digi:trn key="aim:reports">REPORTS</digi:trn></digi:link>	
		</LI>				
</UL>		
</DIV>
</logic:notPresent>
	
<logic:present name="ampAdmin" scope="session">
<DIV id="menu">
	<UL>
		<LI class="noLink">&nbsp;</LI>
		
		
		
		<logic:equal name="ampAdmin" value="yes">
		<LI>
			<div id="gen" title='<digi:trn key="aim:clickToAccessAdminTools">Click here to access admin tools</digi:trn>'>
				<digi:link styleClass="head-menu-link" href="/admin.do" module="aim" onclick="return quitRnot()">
					::: <digi:trn key="aim:aminTools">ADMIN TOOLS</digi:trn>
				</digi:link>				
			</div></LI> 		
		</logic:equal>
		<logic:equal name="ampAdmin" value="no">
		<LI>
			<div id="gen" title='<digi:trn key="aim:enterIntoAIM">Enter in to Aid Information Module</digi:trn>'>
			<logic:notEmpty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/showDesktop.do" module="aim" onclick="return quitRnot()">
					::: <digi:trn key="aim:aidInformationModule">AID INFORMATION MODULE</digi:trn>
				</digi:link>				
			</logic:notEmpty>
			<logic:empty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/" module="aim" onclick="return quitRnot()">			
					::: <digi:trn key="aim:aidInformationModule">AID INFORMATION MODULE</digi:trn>
				</digi:link>				
			</logic:empty>			
			</div></LI> 
		<LI>
			<div id="gen"  title='<digi:trn key="aim:viewPublicReports">View public team reports</digi:trn>'>
			<logic:notEmpty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/viewTeamReports.do" module="aim" onclick="return quitRnot()">
				::: <digi:trn key="aim:reports">REPORTS</digi:trn></digi:link>				
			</logic:notEmpty>
			<logic:empty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/" module="aim" onclick="return quitRnot()">
				::: <digi:trn key="aim:reports">REPORTS</digi:trn></digi:link>
			</logic:empty>			
			</div></LI> 
		<logic:notEmpty name="DC" scope="application">
	    	<LI><a class="head-menu-link">::: <digi:trn key="aim:documentsHeader">DOCUMENTS</digi:trn></a></LI>
		</logic:notEmpty>	
		<logic:notEmpty name="SC" scope="application">
	    	<LI><a class="head-menu-link">::: <digi:trn key="aim:scenarios">SCENARIOS</digi:trn></a></LI>
		</logic:notEmpty>		
		<LI>
			<div id="gen" title='<digi:trn key="aim:viewPlanningCalendar">View Planning Calendar</digi:trn>'>
			<logic:notEmpty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/showCalendarView.do" module="calendar" onclick="return quitRnot()">
				::: <digi:trn key="aim:calendar">CALENDAR</digi:trn></digi:link>
			</logic:notEmpty>
			<logic:empty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/" module="aim" onclick="return quitRnot()">
				::: <digi:trn key="aim:calendar">CALENDAR</digi:trn></digi:link>
			</logic:empty>			
			</div></LI> 
		<logic:notEmpty name="ME" scope="application">
    	<LI>
			<div id="gen"  title='<digi:trn key="aim:viewMEDashboard">View M&E Dashboard</digi:trn>'>
			<logic:notEmpty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/viewPortfolioDashboard.do~actId=-1~indId=-1" module="aim" onclick="return quitRnot()">
				::: <digi:trn key="aim:medashboard">M & E DASHBOARD</digi:trn></digi:link>
			</logic:notEmpty>
			<logic:empty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/" module="aim" onclick="return quitRnot()">			
				::: <digi:trn key="aim:medashboard">M & E DASHBOARD</digi:trn></digi:link>
			</logic:empty>
			</div></LI>
		</logic:notEmpty>
		</logic:equal>
		
		
	</UL>
</DIV>	
</logic:present>
