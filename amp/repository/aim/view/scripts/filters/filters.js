var dateFilterErrorMsg = null; 
function resetFilter(){
		resetRootCheckboxes();
		if (aimReportsFilterPickerForm.text)
			aimReportsFilterPickerForm.text.value="";

		if (aimReportsFilterPickerForm.indexString)
			aimReportsFilterPickerForm.indexString.value="";
			
		if (aimReportsFilterPickerForm.searchMode)
			aimReportsFilterPickerForm.searchMode.value="0";

		if (aimReportsFilterPickerForm.fromDate)
			aimReportsFilterPickerForm.fromDate.value="";
		
		if (aimReportsFilterPickerForm.toDate)
			aimReportsFilterPickerForm.toDate.value="";
			
		if (aimReportsFilterPickerForm.currency)
			aimReportsFilterPickerForm.currency.value=aimReportsFilterPickerForm.defaultCurrency.value;
		
		if (aimReportsFilterPickerForm.fromYear)		
			aimReportsFilterPickerForm.fromYear.selectedIndex=0;
		
		if (aimReportsFilterPickerForm.toYear)
			aimReportsFilterPickerForm.toYear.selectedIndex=0;
			
		if (aimReportsFilterPickerForm.fromMonth)
			aimReportsFilterPickerForm.fromMonth.selectedIndex=0;
			
		if (aimReportsFilterPickerForm.toMonth)
			aimReportsFilterPickerForm.toMonth.selectedIndex=0;
		
		resetElement(aimReportsFilterPickerForm.selectedSectors);
		resetElement(aimReportsFilterPickerForm.selectedSecondarySectors);
        resetElement(aimReportsFilterPickerForm.selectedTertiarySectors);
		resetElement(aimReportsFilterPickerForm.selectedNatPlanObj);
		resetElement(aimReportsFilterPickerForm.selectedPrimaryPrograms);
        resetElement(aimReportsFilterPickerForm.selectedSecondaryPrograms);
        resetElement(aimReportsFilterPickerForm.selectedBudgets);
        resetElement(aimReportsFilterPickerForm.selectedWorkspaces);
        resetElement(aimReportsFilterPickerForm.selectedRisks);
        resetElement(aimReportsFilterPickerForm.regionSelected);
        resetElement(aimReportsFilterPickerForm.approvalStatusSelected);
        resetElement(aimReportsFilterPickerForm.lineMinRanks);
        resetElement(aimReportsFilterPickerForm.planMinRanks);
        resetElement(aimReportsFilterPickerForm.selectedStatuses);
        resetElement(aimReportsFilterPickerForm.selectedFinancingInstruments);
        resetElement (aimReportsFilterPickerForm.selectedContractingAgencyGroups);
        resetElement (aimReportsFilterPickerForm.selectedContractingAgency);
        aimReportsFilterPickerForm.justSearch.checked = false;
       
        
        resetElement(aimReportsFilterPickerForm.selectedTypeOfAssistance);
        resetElement(aimReportsFilterPickerForm.selectedModeOfPayment);
        resetElement(aimReportsFilterPickerForm.selectedProjectImplUnit);
 		resetElement(aimReportsFilterPickerForm.selectedDonorGroups);
 		resetElement(aimReportsFilterPickerForm.selectedDonorTypes);
 		resetElement(aimReportsFilterPickerForm.selectedProjectCategory);
 		resetElement(aimReportsFilterPickerForm.selectedBeneficiaryAgency);
 		resetElement(aimReportsFilterPickerForm.selectedExecutingAgency);
 		resetElement(aimReportsFilterPickerForm.selectedImplementingAgency);
 		resetElement(aimReportsFilterPickerForm.selectedDonnorAgency);
 		resetElement(aimReportsFilterPickerForm.selectedresponsibleorg);
 		
 		resetElement(aimReportsFilterPickerForm.disbursementOrders);
 		resetElement(aimReportsFilterPickerForm.selectedBudgets);
 		resetElement(aimReportsFilterPickerForm.lineMinRanks);
 		resetElement(aimReportsFilterPickerForm.planMinRanks);
 		resetElement(aimReportsFilterPickerForm.selectedArchivedStatus);
 		if (document.getElementById("workspace_only")!=null){
 			document.getElementById("workspace_only").checked = false;
 		}
 		try {
	 		for (var i=0; i<aimReportsFilterPickerForm.selectedArchivedStatus.length; i++) {
	 			var inputEl	= aimReportsFilterPickerForm.selectedArchivedStatus[i];
	 			if (inputEl.value==1)
	 				inputEl.checked = true;
	 		}
 		}
 		catch(e){
 			;
 		}

			
				
		if (aimReportsFilterPickerForm.computedYear){
			aimReportsFilterPickerForm.computedYear.selectedIndex=0;
		}
		
		if (aimReportsFilterPickerForm.actualAppYear)
			aimReportsFilterPickerForm.actualAppYear.selectedIndex = 0;
		
		resetDateOptions();
	}

function resetElement( elem ) {
	if (elem != null && elem.length > 1)
		for (var i=0; i<elem.length; i++)
			elem[i].checked = false;
	else 
		if (elem!=null){
			elem.checked	= false;
		}
}

function resetRootCheckboxes() {
	var allCheckboxes	= document.getElementsByTagName("input");
	for (var i=0; i<allCheckboxes.length; i++) {
		var inputYuiEl	= new YAHOO.util.Element(allCheckboxes[i]);
		if ( inputYuiEl.hasClass("root_checkbox") ) {
			allCheckboxes[i].checked = false;
		}
	}
}

YAHOO.namespace("YAHOO.amptab");

YAHOO.amptab.afterFiltersLoad   = function (){
	//alert('ss');
	var selColor    = "#BFD2DF";
	 	donorsPropertyObj               = new RowManagerProperty(null, "filter_rows_donors", selColor);
	 	relAgenciesPropertyObj  = new RowManagerProperty(null, "filter_rows_rel_agencies", selColor);
	 	sectorsPropertyObj              = new RowManagerProperty(null, "filter_rows_sectors", selColor);
	 	programsPropertyObj             = new RowManagerProperty(null, "filter_rows_programs", selColor);
	 	financingLocPropertyObj         = new RowManagerProperty(null, "filter_rows_finanacing_loc", selColor);
	 	otherCriteriaPropertyObj        = new RowManagerProperty(null, "filter_rows_other_criteria", selColor);
	 	
	 	YAHOO.amptab.initDisplayOfMemberSelectors("donorsTab");
	 	YAHOO.amptab.initDisplayOfMemberSelectors("relAgenciesTab");
	 	YAHOO.amptab.initDisplayOfMemberSelectors("sectorsTab");
	 	YAHOO.amptab.initDisplayOfMemberSelectors("programsTab");
	 	YAHOO.amptab.initDisplayOfMemberSelectors("financingLocTab");
	 	YAHOO.amptab.initDisplayOfMemberSelectors("otherCriteriaTab");
	 	
	 	enableDisableDateOptions();
	 	
	 };
	 	
YAHOO.amptab.initDisplayOfMemberSelectors       = function(bigDivId) {
	var bigDivEl            = document.getElementById(bigDivId);
	 	//alert("aici" + bigDivEl);
	 	if (bigDivEl != null) {
		 	var listOfDivs  = bigDivEl.getElementsByTagName("div");
		 	if (listOfDivs != null && listOfDivs.length > 0) {
		 		for (var i=0; i<listOfDivs.length; i++) {
		 			var divYuiEl    = new YAHOO.util.Element(listOfDivs[i]);
		 			if ( divYuiEl.hasClass("grouping_selector_wrapper") ) {
		 				var buttonEl    = listOfDivs[i].getElementsByTagName("button")[0];
		 					if(typeof(buttonEl) != "undefined") buttonEl.click();
		 					break;
		 				}
		 		}
		 	}
		}
	};

function toggleCheckChildren(checkboxEl) {
	var parentTdEl				= checkboxEl.parentNode;
	for (var i=0; i<=5; i++) {
		if (parentTdEl.nodeName.toLowerCase()=="li") break;
		parentTdEl		= parentTdEl.parentNode;
	}
	var descendantCheckboxes	= parentTdEl.getElementsByTagName('input');
	for (var i=0; i<descendantCheckboxes.length; i++ ) {
		descendantCheckboxes[i].checked	= checkboxEl.checked ;
		toggleByParent (getRelatedTab(descendantCheckboxes[i].name),descendantCheckboxes[i].value,checkboxEl.checked);
		//necessary for related donor groups and agencies
		//root is selected we need to show all groups/agencies. Checked or unchecked
		if ( $(checkboxEl).hasClass("root_checkbox")) {
			toggleRelatedGroups (getRelatedTab(descendantCheckboxes[i].name),true);
		}
		else {
			//we need to hide the non related and show related
			toggleRelatedGroups (getRelatedTab(checkboxEl.name));
		}
	}
	if (isTopHierarchy(checkboxEl.name)) {
	var childrenTab = getRelatedTab(checkboxEl.name);
	refreshRelatedGroups(childrenTab,getRelatedTab(childrenTab));
	refreshRelatedGroups(checkboxEl.name,childrenTab);
	
	}
	else {
		refreshRelatedGroups(checkboxEl.name,getRelatedTab(checkboxEl.name));
	}
	
	if ( ! checkboxEl.checked ) {
		var tempParent				= parentTdEl.parentNode;
		var nodeName				= tempParent.nodeName.toLowerCase();
		while ( tempParent != null && 
				(nodeName=="li" || nodeName=="ul" || 
						nodeName=="td" || nodeName=="tr" ||	
							nodeName=="table") ) {
			
			if ( nodeName=="li" ) {
				for ( var i=0; i<tempParent.childNodes.length; i++) {
					var tempNode	= tempParent.childNodes[i];
					if ( tempNode.nodeName.toLowerCase()=="table" )
						tempNode.getElementsByTagName("input")[0].checked	= false;
					if ( tempNode.nodeName.toLowerCase()=="input" )
						tempNode.checked	= false;
				}
			}
			tempParent				= tempParent.parentNode;
			nodeName				= tempParent.nodeName.toLowerCase();
		}
	}
}

function isTopHierarchy (name) {
	return (name=='selectedDonorTypes');
		
}
function refreshRelatedGroups(parent,children) {
	if (children == null) 
		return;
	var isOneSelected = $('input:checkbox[name="'+parent+'"]:checked').size() > 0;
    if (isOneSelected == false) 
      {
    	$('input:checkbox[name="'+children+'"]').closest("li").show();
      }
    else {
    	$('input:checkbox[name="'+children+'"]:not(:checked)').each(function() {
    	$(this).closest("li").hide();
    	});
    }
    
}


function toggleRelatedGroups (name,forceShow) {
	if (name == null) {
		return
	}
$('input:checkbox[name="'+name+'"][parentid]').each(function() {
		var liElement =  $(this).closest("li");
		if (liElement!=null) {
			if ($(this).is(':checked') == true || true == forceShow)
				liElement.show();
			else 
				liElement.hide();
		}
	});
}

function getRelatedTab (name) {
	if (name =='selectedDonorTypes') {
		return  'selectedDonorGroups';
	}
	else if (name == 'selectedDonorGroups') {
	return 'selectedDonnorAgency';	
	}
	else {
		return null;
	}
	
}
function toggleByParent (name,id,check) {
	if (name==null) {
		return;
	}
	$("input[name="+name+"][type='checkbox'][parentid="+id+"]").trigger( "click" );
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
	 	var parentDiv                                   = this.divEl;
	 	var found                                               = false;
	 	for (var i=0; i<4; i++) { //should be more than 5 steps aways
		 	parentDiv       = parentDiv.parentNode;
		 	if ( parentDiv.nodeName.toLowerCase() == "div" && parentDiv.id.indexOf("Tab") > 0  ){
		 		found   = true;
		 		break;
		 	}
	 	}
	 	if (found){
	 		getSearchManagerInstanceById( parentDiv.id+"_search" ).setDiv(this.divEl);
	 	}
	 };
	 

	 function changeDateFilteringGroup(group){
	 	var selectFields = $("#" + group.name + "_1").find("select[id^='filter_input']");
	 	selectFields = selectFields.add(  $("#" + group.name + "_1").find("input[id^='filter_input']"));
	 	
	 	var dateFields = $("#" + group.name + "_0").find(".dateInputMarker");
	 	dateFields = dateFields.add( $("#" + group.name + "_0").find("a"));
	 	
	 	if(group.value == "1"){//dynamic filter selected
	 		enableDisableInputs(selectFields, dateFields);
	 	}else{//date fields filter selected
	 		enableDisableInputs(dateFields, selectFields);
	 	}
	 }
	 
	 function disabler(event) {
		    event.preventDefault();
		    return false;
	}
	 
	 function enableDisableInputs(toEnable, toDisable){
	 	toDisable.each(
	 		function(){
	 			$(this).attr("disabled", "disabled");
	 			
	 			if( $(this).attr("href") ){
	 				$(this).bind("click",disabler);	
	 			}
	 			
	 		}		
	 	);	
	 	toEnable.each(
	 			function(){
	 				$(this).removeAttr("disabled");
	 				if( $(this).attr("href") ){
	 					$(this).unbind("click",disabler);
	 				}
	 			}		
	 		);	
	 }
	 
	 function enableDisableDateOptions(){

		 var groups = $("input[name^='optGroupDateFilter_']:radio");
		 groups.each(
				 function(){
					 if ($(this).attr("checked")){
						 changeDateFilteringGroup(this);
					 }
				 }
		 );
	 }
	 
	 function toggleWorkspaceLinkedVisibility() {
		 // var testFunction       = function (e) {
	 	 //                       if ( e.name == "publicReport")
	 	 //                               return true;
	 	 //                       return false;
	 	 //               };              
	 	 //               var publicRepEl        = YAHOO.util.Dom.getElementBy(testFunction, "input", "type_step_div");
		 var publicRepEl                = document.getElementById("publicReportChkBox");
		 var workSpaceLinkedEl  = document.getElementById("workspaceLinkedHolder");

		 if ( workSpaceLinkedEl != null ) {
			 if ( publicRepEl.checked ) 
				 workSpaceLinkedEl.style.display = "block";
			 else
	 	 	 	workSpaceLinkedEl.style.display = "none";
		 	}
	 }


	 function resetDateOptions(){
		 var groups = $("input[name^='optGroupDateFilter_']:radio");
		 groups.each(
				 function(){

					 var selectFields = $("#" + this.name + "_" + this.value).find("select[id^='filter_input']");
					 selectFields.each(function(){
						 this.selectedIndex = 0;
					 });
					 
					 var inputFields = $("#" + this.name + "_" + this.value).find(".dateInputMarker");
					 inputFields = inputFields.add(  $("#" + this.name + "_" + this.value).find("input[id^='filter_input']")); 
					 inputFields.each(function(){
						 this.value = "";
					 });

					 if (this.value == 0){
						 $(this).attr("checked","checked");
						 changeDateFilteringGroup(this);
					 }else{
						 $(this).removeAttr("checked");
					 }
				 }
		 );
	 }
	 
		function validateDynamicDateFilters(){
			var wrongMsg = null;
			$("input[id^='filter_input_amount_']:text").each(function () { 
				var re =new RegExp(/[^0-9]/g);
				if(!$(this).attr("disabled") && this.value && re.test(this.value)){
					$(this).focus();
					wrongMsg = $("#" + this.id + "_error").val();
				}
			});
			if (wrongMsg){
				alert(wrongMsg);
				return false;
			}
			return true;
		}
	 
	 
	 function validateDateFilters(){
		 var wrongMsg = null;
		 var inputFields = $(document).find(".dateInputMarker");
		 //assuming that the date fields come in pairs (from-to)
		 for(var i=0; i < inputFields.length - 1; i+=2){
			 var fromStr = inputFields[i].value;
			 var toStr = inputFields[i+1].value;
			 
			 if(!validateFromToDates(fromStr, toStr)){
					wrongMsg = dateFilterErrorMsg; 
					
					var label = $(inputFields[i]).next("input:hidden").val();
					
					if(label){
						wrongMsg = wrongMsg + " ( " + label + " )";
					}
					
					break;
			 }
		 }
			if (wrongMsg){
				alert(wrongMsg);
				return false;
			}
			return true;
		 
	 }
	 
	 //this function depends on newCalendar.jsp
	 function validateFromToDates(fromStr, toStr){
		 if (!fromStr || !toStr) return true;
		 var fromArray = dateStringToObject (fromStr, dateFormat );
		 var toArray = dateStringToObject (toStr, dateFormat );
		 
		 var from = new Date(fromArray.year, fromArray.month - 1, fromArray.day);
		 var to = new Date(toArray.year, toArray.month - 1, toArray.day);
		 
		 return (from <= to);
		 
	 }