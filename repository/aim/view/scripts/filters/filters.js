function resetFilter(){
		if (aimReportsFilterPickerForm.text)
			aimReportsFilterPickerForm.text.value="";

		if (aimReportsFilterPickerForm.indexString)
			aimReportsFilterPickerForm.indexString.value="";
			
		if (aimReportsFilterPickerForm.fromDate)
			aimReportsFilterPickerForm.fromDate.value="";
		
		if (aimReportsFilterPickerForm.toDate)
			aimReportsFilterPickerForm.toDate.value="";
			
		if (aimReportsFilterPickerForm.currency)
			aimReportsFilterPickerForm.currency.value=aimReportsFilterPickerForm.defaultCurrency.value;
		
		if (aimReportsFilterPickerForm.fromYear)		
			aimReportsFilterPickerForm.fromYear.selectedIndex=0;
		
		if (aimReportsFilterPickerForm.toYear)
			aimReportsFilterPickerForm.toYear.selectedIndex=0
			
		if (aimReportsFilterPickerForm.fromMonth)
			aimReportsFilterPickerForm.fromMonth.selectedIndex=0;
			
		if (aimReportsFilterPickerForm.toMonth)
			aimReportsFilterPickerForm.toMonth.selectedIndex=0;
		
		resetElement(aimReportsFilterPickerForm.selectedSectors);
 	 	resetElement(aimReportsFilterPickerForm.selectedSecondarySectors);
 	 	resetElement(aimReportsFilterPickerForm.selectedNatPlanObj);
 	 	resetElement(aimReportsFilterPickerForm.selectedPrimaryPrograms);
 	 	resetElement(aimReportsFilterPickerForm.selectedSecondaryPrograms);
 	 	resetElement(aimReportsFilterPickerForm.selectedBudget);
 	 	resetElement(aimReportsFilterPickerForm.selectedRisks);
 	 	resetElement(aimReportsFilterPickerForm.regionSelected);
 	 	resetElement(aimReportsFilterPickerForm.approvalStatusSelected);
 	 	resetElement(aimReportsFilterPickerForm.lineMinRank);
 	 	resetElement(aimReportsFilterPickerForm.planMinRank);
 	 	resetElement(aimReportsFilterPickerForm.selectedStatuses);
 	 	resetElement(aimReportsFilterPickerForm.selectedFinancingInstruments);
        
 	 	resetElement(aimReportsFilterPickerForm.selectedTypeOfAssistance);
 	 	resetElement(aimReportsFilterPickerForm.selectedDonorGroups);
 	 	resetElement(aimReportsFilterPickerForm.selectedDonorTypes);
 	 	resetElement(aimReportsFilterPickerForm.selectedProjectCategory);
 	 	resetElement(aimReportsFilterPickerForm.selectedBeneficiaryAgency);
 	 	resetElement(aimReportsFilterPickerForm.selectedExecutingAgency);
 	 	resetElement(aimReportsFilterPickerForm.selectedImplementingAgency);
 	 	resetElement(aimReportsFilterPickerForm.selectedDonnorAgency);
 	 	
 	 	if (aimReportsFilterPickerForm.jointCriteria){
 	 	aimReportsFilterPickerForm.jointCriteria.checked=false;
 	 	}
 	 	
		if (aimReportsFilterPickerForm.jointCriteria){
			aimReportsFilterPickerForm.jointCriteria.checked=false;
		}

		if (aimReportsFilterPickerForm.governmentApprovalProcedures){
			aimReportsFilterPickerForm.governmentApprovalProcedures.checked=false;
		}
		
		if (aimReportsFilterPickerForm.computedYear){
			aimReportsFilterPickerForm.computedYear.selectedIndex=0;
		}
		
	}

function resetElement( elem ) {
	 	if (elem != null && elem.length > 1)
	 	for (var i=0; i<elem.length; i++)
	 	elem[i].checked = false;
	 	}

YAHOOAmp.namespace("YAHOO.amptab");

YAHOOAmp.amptab.afterFiltersLoad   = function (){
		var selColor    = "#BFD2DF";
	 	donorsPropertyObj               = new RowManagerProperty(null, "filter_rows_donors", selColor);
	 	relAgenciesPropertyObj  = new RowManagerProperty(null, "filter_rows_rel_agencies", selColor);
	 	sectorsPropertyObj              = new RowManagerProperty(null, "filter_rows_sectors", selColor);
	 	programsPropertyObj             = new RowManagerProperty(null, "filter_rows_programs", selColor);
	 	financingLocPropertyObj         = new RowManagerProperty(null, "filter_rows_finanacing_loc", selColor);
	 	otherCriteriaPropertyObj        = new RowManagerProperty(null, "filter_rows_other_criteria", selColor);
	 	
	 	YAHOOAmp.amptab.initDisplayOfMemberSelectors("donorsTab");
	 	YAHOOAmp.amptab.initDisplayOfMemberSelectors("relAgenciesTab");
	 	YAHOOAmp.amptab.initDisplayOfMemberSelectors("sectorsTab");
	 	YAHOOAmp.amptab.initDisplayOfMemberSelectors("programsTab");
	 	YAHOOAmp.amptab.initDisplayOfMemberSelectors("financingLocTab");
	 	YAHOOAmp.amptab.initDisplayOfMemberSelectors("otherCriteriaTab");
	 }
	 	
YAHOOAmp.amptab.initDisplayOfMemberSelectors       = function(bigDivId) {
	 	var bigDivEl            = document.getElementById(bigDivId);
	 	//alert("aici" + bigDivEl);
	 	if (bigDivEl != null) {
		 	var listOfDivs  = bigDivEl.getElementsByTagName("div");
		 	if (listOfDivs != null && listOfDivs.length > 0) {
		 		for (var i=0; i<listOfDivs.length; i++) {
		 			var divYuiEl    = new YAHOOAmp.util.Element(listOfDivs[i]);
		 			if ( divYuiEl.hasClass("grouping_selector_wrapper") ) {
		 				var buttonEl    = listOfDivs[i].getElementsByTagName("button")[0];
		 					buttonEl.click();
		 					break;
		 				}
		 		}
		 	}
		}
	}

function toggleCheckChildren(checkboxEl) {
		var parentTdEl = checkboxEl.parentNode;
	 	var descendantCheckboxes = parentTdEl.getElementsByTagName('input');
	 	for (var i=0; i<descendantCheckboxes.length; i++ ) {
	 		descendantCheckboxes[i].checked = checkboxEl.checked ;
	 	}
	 	if ( ! checkboxEl.checked ) {
	 		var tempParent = parentTdEl.parentNode;
 	 		while ( tempParent != null && (tempParent.nodeName.toLowerCase()=="li" || tempParent.nodeName.toLowerCase()=="ul") ) {
 	 			if ( tempParent.nodeName.toLowerCase()=="li" ) {
 	 				for ( var i=0; i<tempParent.childNodes.length; i++) {
 	 					var tempNode    = tempParent.childNodes[i];
 	 					if ( tempNode.nodeName.toLowerCase()=="input" )
 	 						tempNode.checked        = false;
 	 				}
 	 			}
 	 			tempParent = tempParent.parentNode;
 	 		}
	 	}
	}
	 
function DivManager(divId, propertyObj) {
	 	this.divEl                      = document.getElementById(divId);
	 	this.propertyObj        = propertyObj;
	 }
	
DivManager.prototype.onSelect           = function () {
	 	if ( this.propertyObj.lastShownDiv      != null )
	 		this.propertyObj.lastShownDiv.style.display     = "none";
	 	this.divEl.style.display                = "block";
	 	this.propertyObj.lastShownDiv   = this.divEl;
	 	var parentDiv                                   = this.divEl
	 	var found                                               = false;
	 	for (var i=0; i<4; i++) { //should be more than 5 steps aways
		 	parentDiv       = parentDiv.parentNode;
		 	if ( parentDiv.nodeName.toLowerCase() == "div" && parentDiv.id.indexOf("Tab") > 0  ){
		 		found   = true;
		 		break;
		 	}
	 	}
	 	if (found)
	 		getSearchManagerInstanceById( parentDiv.id+"_search" ).setDiv(this.divEl);
	 	else
	 		alert("Not found");
	 }