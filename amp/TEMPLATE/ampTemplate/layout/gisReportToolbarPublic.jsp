<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>	

<digi:instance property="gisDashboardForm"/>
<style>
<!--
.toolbar{
	width: 350px;
	background: #addadd; 
	background-color: #addadd; 
	padding: 3px 3px 3px 3px; 
	position: relative; 
	top: 10px; 
	left: 10px;
	bottom: 100px;
		
}
.toolbartable{
	border-color: #FFFFFF;
	border-width: 2px;
	border-bottom-width: 2px; 
	border-right-width: 2px;"
	border-left-width: 2px;
	border-style: solid;
	height: 25px;
}
-->
</style>
<div class="gis_wht" style="height:75px; padding-top:7px; margin-right:4px;" ><div class="gis_cont">
	<div style="float:right;">
		<table border="0" cellspacing="0" cellpadding="0">
				<tr>
				<td>
					<digi:img src="img_2/ico_pdf.gif" align="left" style="margin-right:5px;"/>
				</td>
				<td>
					<a class="l_sm" href="#" target="_blank" onclick="exportPDF(); return false;">Export to PDF</a>
				</td>
				<td width=10></td>
				<td>
					<digi:img src="img_2/ico_print.gif" style="margin-right:5px;"/>
				</td>
				<td>
					<digi:link styleClass="l_sm" href="#" onclick="window.print(); return false;"><digi:trn>Print</digi:trn></digi:link>
				</td>
			</tr>
		</table>
	
		<div class="dash_ico">
			<div class="dash_ico_link">
			</div>
		</div>
		<div class="dash_ico">
			<div class="dash_ico_link">
			</div>
		</div>
	</div>
	
	<div style="float:left;">
		<img
						style="width: 16px; height: 16px; margin-right:5px;"
						src="/TEMPLATE/ampTemplate/images/info.png" /> <digi:trn></>Select the date
						range for the funding information on the map</digi:trn>
						<br/>
		<digi:trn>From Date:</digi:trn> 
		<html:select styleClass="dropdwn_sm" style="width:145px;" name="gisDashboardForm" property="selectedFromYear" onchange="mapYearChanged()">
			<html:optionsCollection name="gisDashboardForm" property="yearsFrom" label="label" value="value"/>
		</html:select>
		&nbsp;&nbsp;
		<digi:trn>To Date:</digi:trn> 
		<html:select styleClass="dropdwn_sm" style="width:145px;" name="gisDashboardForm" property="selectedToYear" onchange="mapYearChanged()">
			<html:optionsCollection name="gisDashboardForm" property="yearsTo" label="label" value="value"/>
		</html:select>
	</div>
</div>
</div>

<br>

<script language="javascript">
	function exportPDF() {
  
	var selectedFromYear = document.getElementsByName("selectedFromYear")[0].value;
	var selectedToYear = document.getElementsByName("selectedToYear")[0].value;


	if (showDevinfo) {
			var sectorId =  document.getElementById("sectorsMapCombo").value;
			var indicatorId = document.getElementById("indicatorsCombo").value;	
			var subgroupId = document.getElementById("indicatorSubgroupCombo").value;
			var timeInterval = document.getElementById("indicatorYearCombo").value;
		  
			openURLinWindow("/gis/pdfExport.do?mapMode=DevInfo&publicMode=true&selectedFromYear=" + selectedFromYear+ "&selectedToYear=" + selectedToYear + "&sectorId=" + sectorId + "&indicatorId=" + indicatorId + "&subgroupId=" + subgroupId + "&indYear=" + timeInterval, 780, 500);
		} else {
			var sectorId =  document.getElementById("sectorsMapComboFin").value;
			var fundingType = document.getElementById("fundingType").value;	
			var donorId = document.getElementById("donorsCombo").value;
		  
			openURLinWindow("/gis/pdfExport.do?mapMode=FinInfo&publicMode=true&selectedFromYear=" + selectedFromYear+ "&selectedToYear=" + selectedToYear + "&sectorId=" + sectorId + "&fundingType=" + fundingType + "&donorId=" + donorId , 780, 500);

		}
  }
</script>