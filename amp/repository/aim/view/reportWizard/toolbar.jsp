<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

	<c:set var="className" value="toolbar"/>
	<c:set var="imgName" value="prev.png"/>
	<c:set var="disabledString" value=" "/>
	<c:if test="${stepNum==0}">
		<c:set var="className" value="toolbar-dis"/>
		<c:set var="imgName" value="prev_dis.png"/>
		<c:set var="disabledString" value="disabled='disabled'"/>
	</c:if>
		
	<div class="subtabs">
		<button id="step${stepNum}_prev_button" type="button" class="${className}" ${disabledString}
			onclick="repManager.previousStep();">
			<img src="/TEMPLATE/ampTemplate/imagesSource/reports/${imgName}" class="toolbar" />
			<digi:trn key="btn:previous">Previous</digi:trn>
		</button>
		<button id="step${stepNum}_next_button" type="button" class="toolbar-dis" 
			onclick="repManager.nextStep()" disabled="disabled">
			<img height="16" src="/TEMPLATE/ampTemplate/imagesSource/reports/next_dis.png" class="toolbar" /> 
			<digi:trn key="btn:next">Next</digi:trn>
		</button>
			<feature:display  name="Filter Button" module="Report and Tab Options">
				<button id="step${stepNum}_add_filters_button" type="button" class="toolbar" onclick="repFilters.showFilters()">
					<img src="/TEMPLATE/ampTemplate/imagesSource/reports/add_filters.png" class="toolbar" style="height: 15px;" /> 
					<digi:trn key="btn:repFilters">Filters</digi:trn>
				</button>
			</feature:display>
			<%if ((Boolean) session.getAttribute("runreport")==false){ %>
				<button type="button" class="toolbar-dis" disabled="disabled" name="save" onclick="saveReportEngine.decideToShowTitlePanel()" >
					<img height="16" src="/TEMPLATE/ampTemplate/imagesSource/reports/save_dis.png" class="toolbar"/>
					<digi:trn key="rep:wizard:Save">Save</digi:trn>
				</button>
				<button type="button" class="toolbar-dis" onclick="saveReportEngine.showTitlePanel()" disabled="disabled" name="save">
					<img src="/TEMPLATE/ampTemplate/imagesSource/reports/save_as_dis.png" class="toolbar"/>
					<digi:trn key="rep:wizard:SaveAs">Save As..</digi:trn>	
				</button>
			<%}else{ %>
				<button type="button" class="toolbar-dis" onclick="saveReportEngine.runReport()" disabled="disabled" name="save">
					<img src="/TEMPLATE/ampTemplate/imagesSource/reports/save_as_dis.png" class="toolbar"/>
					<digi:trn key="rep:wizard:Run">Run</digi:trn>	
				</button>	
			<%} %>
		<button id="step${stepNum}_cancel" type="button" class="toolbar" onclick="repManager.cancelWizard();" >
				<img src="/TEMPLATE/ampTemplate/imagesSource/reports/cancel.png" class="toolbar" />
				<digi:trn key="btn:wizard:Cancel">Cancel</digi:trn>
		</button>
 	 </div>
