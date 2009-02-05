<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
	
	<c:set var="className" value="toolbar"/>
	<c:set var="imgName" value="prev.png"/>
	<c:set var="disabledString" value=" "/>
	<c:if test="${stepNum==0}">
		<c:set var="disabledString" value="disabled='disabled'"/>
	</c:if>
		
	<div class="subtabs">
		<button id="step${stepNum}_prev_button" type="button" class="toolbar-dis"
			<c:if test="${stepNum==0}">disabled="disabled"</c:if>
			onclick="navigateTab(-1);">
			<img src="/TEMPLATE/ampTemplate/images/prev_dis.png" class="toolbar" />
			<digi:trn key="btn:previous">Previous</digi:trn>
		</button>
		<button id="step${stepNum}_next_button" type="button" class="toolbar-dis" 
			<c:if test="${stepNum==2}">disabled="disabled"</c:if>
			onclick="navigateTab(1)">
			<img height="16" src="/TEMPLATE/ampTemplate/images/next_dis.png" class="toolbar" /> 
			<digi:trn key="btn:next">Next</digi:trn>
		</button>
		<button id="step${stepNum}_cancel" type="button" class="toolbar" onclick="repManager.cancelWizard();" name="cancel">
			<img src="/TEMPLATE/ampTemplate/images/cancel.png" class="toolbar" />
			<digi:trn key="btn:wizard:Cancel">Cancel</digi:trn>
		</button>
		<button type="button" class="toolbar-dis" onclick="saveReportEngine.showTitlePanel()" name="export">
			<img src="/TEMPLATE/ampTemplate/images/file-export-16x16.png" class="toolbar"/>
			<digi:trn key="btn:wizard:Export">Export</digi:trn>
		</button>
 	 </div>
