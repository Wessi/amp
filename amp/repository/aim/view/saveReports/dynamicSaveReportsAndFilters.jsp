<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>

<logic:notEmpty name="reportMeta" scope="session">

<bean:define id="reportObject" name="reportMeta" scope="session" toScope="page" />

<c:set var="failureMessage">
	<digi:trn key="aim:reportwizard:connectionProblems">Apparently there are some connection problems. Please try again in a few moments.</digi:trn>
</c:set>
<c:set var="errorMessage">
	<digi:trn key="rep:dynamicsave:error">Apparently there are some problems in the saving process. Please try again at a later moment.</digi:trn>
</c:set>
<c:set var="savingDataMessage">
	<digi:trn key="aim:reportwizard:savingData">Saving data. Please wait.</digi:trn>
</c:set>

<c:choose>
	<c:when test="${reportObject.drilldownTab}">
		<c:set var="plsEnterTitle">
			<digi:trn key="rep:wizard:enterTitleForTab">Please enter a title for this tab: </digi:trn>
		</c:set>
		<c:set var="saveBtn">
			<digi:trn key="btn:saveTab">Save Tab</digi:trn>
		</c:set>
		<c:set var="overwrite">
			<digi:trn key="rep:dynamicsave:overwriteTab">Overwrite original tab</digi:trn>
		</c:set>
		<c:set var="saveFilters" scope="request">
			<digi:trn key="rep:pop:saveTabAndFilters">Save Tab&Filters</digi:trn>
		</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="plsEnterTitle">
			<digi:trn key="rep:wizard:enterTitleForReport">Please enter a title for this report: </digi:trn>
		</c:set>
		<c:set var="saveBtn">
			<digi:trn key="btn:saveReport">Save Report</digi:trn>
		</c:set>
		<c:set var="overwrite">
			<digi:trn key="rep:dynamicsave:overwriteReport">Overwrite original report</digi:trn>
		</c:set>
		<c:set var="saveFilters" scope="request" >
			<digi:trn key="rep:pop:saveReportAndFilters">Save Report&Filters</digi:trn>
		</c:set>
	</c:otherwise>
</c:choose> 
<div id="saveTitlePanel" style="display: none">
	<div class="hd" style="font-size: 8pt">
		${plsEnterTitle}
	</div>
	<div class="bd" id="titlePanelBody">
		<input type="text" id="saveReportName" onkeyup="saveReportEngine.checkTyping(event)" onkeypress="saveReportEngine.checkEnter(event)"  
			class="inp-text" style="border: 1px solid gray; width: 100%; font-size: 8pt; font-weight: bolder;" value="${reportObject.name}" />
		<input type="hidden" id="saveReportId" value="${reportObject.ampReportId}" />
		<input type="hidden" id="saveOriginalReportName" value="${reportObject.name}" />
		<br /> <br />
		<div align="right">
			<button id="dynamic_save_button" type="button" class="buton" 
				style="font-weight: bold;" onclick="saveReportEngine.saveReport();">
					${saveBtn}
			</button>
			&nbsp;&nbsp;&nbsp;
		</div>
	</div>
	<div class="ft" align="center">
	</div>
</div>

</logic:notEmpty>