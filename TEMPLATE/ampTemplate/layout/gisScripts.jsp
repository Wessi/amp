<script language="javascript">
window.onload = function(){
	resizeDivs();
}

  function exportPDF() {
  
  	var divs = document.getElementsByTagName("select");
	var matchingDivs = new Array();
	var currentDiv;
	
	var tableId = new Array();
	var columnId = new Array();
	var itemId = new Array();
	var columnquerystring="";

	// Iterate through all selects in the page
	for(var idx=0;idx < divs.length; idx++){
		if(divs[idx].id.search("tableWidgetDropDown") > -1){
			var tableId;
			var columnId;
			var itemId;
			tableId.push(divs[idx].getAttribute("idtable"));
			columnId.push(divs[idx].getAttribute("idcolumn"));
			itemId.push(divs[idx].value);
		}
	}
	var tableIdStr = "-1";
	var columnIdStr = "-1";
	var itemIdStr = "-1";
	
	for(var idx=0;idx < tableId.length; idx++)
	{
		if(tableIdStr == "-1"){
			tableIdStr = tableId[idx];
			columnIdStr = columnId[idx];
			itemIdStr = itemId[idx];
		}
		else
		{
			tableIdStr += "," + tableId[idx];
			columnIdStr += "," + columnId[idx];
			itemIdStr += "," + itemId[idx];
		}
	}
	columnquerystring += "&tableId="+tableIdStr+"&columnId="+columnIdStr+"&itemId="+itemIdStr;

	var selectedDonor = document.getElementsByName("selectedDonor")[0].value;
	var selectedYear = document.getElementsByName("selectedYear")[0].value;
	var showLabels = document.getElementsByName("showLabels")[0].checked;
	var showLegends = document.getElementsByName("showLegends")[0].checked;

	//Get donor name also
	var selectDonors = document.getElementsByName("selectedDonor")[0];
	var selectDonorsStr = " ";
	for(var idx = 0; idx < selectDonors.length;idx++)
	{
		if(selectDonors.options[idx].value == selectDonors.value)
			selectDonorsStr = (selectDonors.options[idx].text);
	}

	var sectorId =  document.getElementById("sectorsMapCombo").value;
	var indicatorId = document.getElementById("indicatorsCombo").value;	
  
	openURLinWindow("/gis/pdfExport.do?selectedDonor=" + selectedDonor + "&selectedYear=" + selectedYear + "&showLabels=" + showLabels + "&showLegends=" + showLegends + "&sectorId=" + sectorId + "&indicatorId=" + indicatorId + ""+ columnquerystring + "&selectedDonorName=" +selectDonorsStr, 780, 500);
  }
function resizeDivs(){
	var tables = document.getElementsByTagName("table");
	var matchingTables = new Array();
	var currentTable;
	for(var idx=0;idx < tables.length; idx++){
		if(tables[idx].id.search("tableWidget") > -1){
			//Resize the container
			table = tables[idx];
			tempTable = table;
			var counter = 0;
		
			while(tempTable.id != "content" && counter < 10)
			{
				if(tempTable.id != "content")
					tempTable = tempTable.parentNode;
				counter++;
			}
			if(tempTable.id == "content"){
				if(tempTable.style.width == "1000px" || tempTable.alreadyResized )
				{
					if(tempTable.alreadyResized == true )
					{
						if((table.offsetWidth + 20) > tempTable.style.width)
							tempTable.style.width = (table.offsetWidth + 20);
					}
					else
					{
						tempTable.style.width = (table.offsetWidth + 20);
						tempTable.alreadyResized = true;
					}
				}
				
			}
		}
	}
}    
function applyStyle(table){
	table.className += " tableElement";
	setStripsTable(table.id, "tableEven", "tableOdd");
	setHoveredTable(table.id, true);

}
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	if(tableElement)
	{
		tableElement.setAttribute("border","0");
		tableElement.setAttribute("cellPadding","2");
		tableElement.setAttribute("cellSpacing","2");
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}
}
function setHoveredTable(tableId, hasHeaders) {
	var tableElement = document.getElementById(tableId);
	if(tableElement){
	    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		var i = 0;
		if(hasHeaders){
			rows[0].className += " tableHeader";
			i = 1;
			
		}
	
		for(i, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}
</script>