function initializePage(){
	YAHOO.util.Event.onAvailable("region_dropdown_id", callbackChildren);
	YAHOO.util.Event.onAvailable("org_group_dropdown_id", callbackChildren);
	YAHOO.util.Event.onAvailable("sector_dropdown_id", callbackChildren);
	YAHOO.util.Event.onAvailable("sector_config_dropdown_id", callbackChildren);
	myTabs = new YAHOO.widget.TabView("demo");
	myTabs.selectTab(0);
	yuiLoadingPanel = function(conf){
	    conf = conf == undefined ? new Array() : conf;
	    conf.id = conf.id == undefined ? 'yuiLoadingPanel':confi.id;
	    conf.header = conf.header == undefined ? trnLoading:conf.header;
	    conf.width = conf.width == undefined ? '240px':conf.width;
	    this.conf = conf;
	    this.cancelEvent = new YAHOO.util.CustomEvent("cancelEvent", this);
	    this.init();
		
	};
	yuiLoadingPanel.prototype = {
		    init:function(){
		        var loadingPanel = new YAHOO.widget.Panel(this.conf.id,{
		            width:this.conf.width,
			    fixedcenter:true,
		            close:false,
		            draggable:false,
		            modal:true,
		            visible:false
		        });
		    
		       loadingPanel.setBody(this.conf.header + '<img src="/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif" />');
		       loadingPanel.render(document.body);
		       $D.addClass(loadingPanel.id, 'tcc_lightboxLoader');
		       var cancelLink = document.createElement('a');
		       $D.setStyle(cancelLink, 'cursor', 'pointer');
		       cancelLink.appendChild(document.createTextNode(trnCancel));
		       $E.on(cancelLink, 'click', function(e, o){
		           o.self.loadingPanel.hide();
		           o.self.cancelEvent.fire();
		           window.stop();
		       }, {self:this});
		       loadingPanel.appendToBody(document.createElement('br'));
		       loadingPanel.appendToBody(cancelLink);
		       $D.setStyle(loadingPanel.body, 'text-align', 'center');
//		               $D.addClass(document.body, 'yui-skin-sam');
		        this.loadingPanel = loadingPanel;
		    },
		    show:function(text){
		        if(text != undefined){
		            this.loadingPanel.setHeader(text);
		        }else{
			    this.loadingPanel.setHeader(this.conf.header);
			}
			this.loadingPanel.show();
		    },
		    hide:function(){
		        this.loadingPanel.hide();
		    }
		};
	loadingPanel = new yuiLoadingPanel();
}


function showFullList(objectType){
	var sUrl="/visualization/dataDispatcher.do?action=getFullList&objectType=" + objectType;
	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, updateFullList);
	var divFullName = "";
	var divTopName = "";
	switch(objectType){
		case "sectors":
			divFullName = "divFullSectors";
			divTopName = "divTopSectors";
			break;
		case "donors":
			divFullName = "divFullDonors";
			divTopName = "divTopDonors";
			break;
		case "regions":
			divFullName = "divFullRegions";
			divTopName = "divTopRegions";
			break;
		case "projects":
			divFullName = "divFullProjects";
			divTopName = "divTopProjects";
			break;
	}
	var divFull = document.getElementById(divFullName);
	divFull.innerHTML = trnLoading + "<br/> <img src=\"/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif\" />"; 
	var divTop = document.getElementById(divTopName);
	divFull.style.display = "";
	divTop.style.display = "none";
}

var updateFullList = {
		  success: function(o) {
			  	var dashboardType = document.getElementById("dashboardType").value;
				var results = YAHOO.lang.JSON.parse(o.responseText);
				var child = results.children[0];
				switch(child.type){
					case "ProjectsList":
						inner = "<a href='javascript:hideFullProjects()' style='float:right;'>"+trnShowTop+"</a> <br />";
						var isPublicView = document.getElementById("fromPublicView").value;
						for(var i = 0; i < child.list.length; i++){
							inner = inner + (i+1) + ". " + "";
							if (isPublicView == "false"){
								inner = inner + "<a target='_blank' href='/aim/viewActivityPreview.do~pageId=2~activityId=" + child.list[i].id + "~isPreview=1'>"  + child.list[i].name + "</a>" + "  <b>(" + child.list[i].value + ")</b> <hr />";
							} else {
								inner = inner + child.list[i].name + "  <b>(" + child.list[i].value + ")</b> <hr />";
							}
						}
						inner = inner + "<a href='javascript:hideFullProjects()' style='float:right;'>"+trnShowTop+"</a>";
						var div = document.getElementById("divFullProjects");
						div.innerHTML = inner;
						break;
					case "DonorsList":
						if (dashboardType!=1) {
							inner = "<a href='javascript:hideFullDonors()' style='float:right;'>"+trnShowTop+"</a> <br />";
							for(var i = 0; i < child.list.length; i++){
								inner = inner + (i+1) + ". " + child.list[i].name + "  <b>(" + child.list[i].value + ")</b> <hr />";
							}
							inner = inner + "<a href='javascript:hideFullDonors()' style='float:right;'>"+trnShowTop+"</a>";
							var div = document.getElementById("divFullDonors");
							div.innerHTML = inner;
						}
						break;
					case "SectorsList":
						if (dashboardType!=3) {
							inner = "<a href='javascript:hideFullSectors()' style='float:right;'>"+trnShowTop+"</a> <br />";
							for(var i = 0; i < child.list.length; i++){
								inner = inner + (i+1) + ". " + child.list[i].name + "  <b>(" + child.list[i].value + ")</b> <hr />";
							}
							inner = inner + "<a href='javascript:hideFullSectors()' style='float:right;'>"+trnShowTop+"</a>";
							var div = document.getElementById("divFullSectors");
							div.innerHTML = inner;
						}
						break;
					case "RegionsList":
						if (dashboardType!=2) {
							inner = "<a href='javascript:hideFullRegions()' style='float:right;'>"+trnShowTop+"</a> <br />";
							for(var i = 0; i < child.list.length; i++){
								inner = inner + (i+1) + ". " + child.list[i].name + "  <b>(" + child.list[i].value + ")</b> <hr />";
							}
							inner = inner + "<a href='javascript:hideFullRegions()' style='float:right;'>"+trnShowTop+"</a>";
							var div = document.getElementById("divFullRegions");
							div.innerHTML = inner;
							inner = "";
						}
						break;
					default:
						break;
				}
		  },
		  failure: function(o) {
//			  alert("problema");
		  }
		};

function hideFullProjects(){
	var divFull = document.getElementById("divFullProjects");
	var divTop = document.getElementById("divTopProjects");
	divFull.style.display = "none";
	divFull.innerHTML = "";
	divTop.style.display = "";
}

function hideFullSectors(){
	var divFull = document.getElementById("divFullSectors");
	var divTop = document.getElementById("divTopSectors");
	divFull.style.display = "none";
	divFull.innerHTML = "";
	divTop.style.display = "";
}

function hideFullRegions(){
	var divFull = document.getElementById("divFullRegions");
	var divTop = document.getElementById("divTopRegions");
	divFull.style.display = "none";
	divFull.innerHTML = "";
	divTop.style.display = "";
}

function hideFullDonors(){
	var divFull = document.getElementById("divFullDonors");
	var divTop = document.getElementById("divTopDonors");
	divFull.style.display = "none";
	divTop.style.display = "";
}



$D = YAHOO.util.Dom;
$E = YAHOO.util.Event;
var yuiLoadingPanel;

function checkUncheckRelatedEntities(option,name,id){
	uncheckAllRelatedEntities(name);
	checkRelatedEntities(option,name,id);
}
function allOptionChecked(option,name,subname){
	if(option.checked){
		var options=$("input[name='"+name+"']").removeAttr('checked');
		var options=$("input[name='"+subname+"']").removeAttr('checked');
		option.checked=true;
	}
}

function uncheckAllRelatedEntities(name){
	$("input[name='"+name+"']").removeAttr('checked');
}
function checkRelatedEntities(option,name,id){
	var options=$("input[class='"+name+"_"+id+"']");
	if(option.checked){
		options.attr('checked','checked');
	}
	else{
		options.removeAttr('checked');
	}
	
}
function uncheckAllOption(name){
	$("#"+name+"_all").removeAttr('checked');
}
function manageSectorEntities(option,configId,sectorId){
	$("li[id^='config_']").each(function() {
		if(this.id!='config_'+configId){
			$(this).find("input[name='sub_sector_check']").removeAttr('checked');
			$(this).find("input[name='sector_check']").removeAttr('checked');
			$(this).find("#config_"+configId+"_radio").removeAttr('checked');;
		}
		else{
			$(this).find("#config_"+configId+"_radio").attr('checked','checked');
		}
	  });
	if(sectorId!=null){
		var options=$("input[class='sub_sector_check_"+sectorId+"']");
		if(option.checked){
			options.attr('checked','checked');
		}
		else{
			options.removeAttr('checked');
		}
	}
	
}
var currentIndex=-1;
var searchterm;
var searchResult;
function findNext(divId){
	if(currentIndex==-1){
		searchterm=$("#"+divId+"_search").val();
		searchResult=$("#"+divId+" span:containsi('"+searchterm+"')");
		searchResult.css("font-weight","bold");
	}
	if(searchResult.length-1>currentIndex){
		searchResult.css("color","black");
		currentIndex++;
		var currentSpan=searchResult.eq(currentIndex);
		currentSpan.css("color","red");
		currentSpan.prev().focus(); 
	}
}
function findPrev(divId){
	if(currentIndex==-1){
		searchterm=$("#"+divId+"_search").val();
		searchResult=$("#"+divId+" span:containsi('"+searchterm+"')");
		searchResult.css("font-weight","bold");
	}
	if(currentIndex>0){	
		searchResult.css("color","black");
		currentIndex--;
		var currentSpan=searchResult.eq(currentIndex);
		currentSpan.css("color","red");
		currentSpan.prev().focus(); 
	}
}
function clearSearch(divId){
	currentIndex=-1;
	$("#"+divId+" span").css("color","black").css("font-weight","normal");
}


function toggleHeader(button, containerId){
	var container = document.getElementById(containerId);
	var imgShow = "<img src=\"/TEMPLATE/ampTemplate/img_2/ico_perm_open.gif\" vspace=\"5\" align=\"absMiddle\"/>"; 
	var imgHide = "<img src=\"/TEMPLATE/ampTemplate/img_2/ico_perm_close.gif\" vspace=\"5\" align=\"absMiddle\"/>"; 
	if(container.style.display == "none"){
		container.style.display = "block";
		button.innerHTML = imgHide + " " + trnHideSettings;
	}
	else if(container.style.display == "block"){
		container.style.display = "none";	
		button.innerHTML = imgShow + " " + trnShowSettings;
	}
	
}


YAHOO.namespace("YAHOO.amp");
popinPanels = new Array();

var myPanel = new YAHOO.widget.Panel("newPanel", {
	width:"750px",
	maxHeight:"500px",
	fixedcenter: true,
    constraintoviewport: false,
    underlay:"none",
    close:false,
    visible:false,
    modal:true,
    draggable:true,
    context: ["showbtn", "tl", "bl"]
    });
var panelStart=0;

function initPanel() {
	
	myPanel.setBody("");
	myPanel.beforeHideEvent.subscribe(function() {
		panelStart=1;
	}); 
	
	myPanel.render(document.body);
}

function clearAllLocalSearchResults(){
	$("#orgGrpDivList span").css("color","black").css("font-weight","normal");
	$("#orgGrpDivList_search").val('');
	$("#regionDivList span").css("color","black").css("font-weight","normal");
	$("#regionDivList_search").val('');
	$("#sectorDivList span").css("color","black").css("font-weight","normal");
	$("#sectorDivList_search").val('');
	currentIndex=-1;
}

function showPopin() {
	if ( popinPanels['Panel1'] == null ) {
		popinPanels['Panel1'] = new YAHOO.widget.Panel('Panel1', {
		width:"750px",
		maxHeight:"500px",
		fixedcenter: true,
	    constraintoviewport: false,
	    underlay:"none",
	    close:true,
	    visible:false,
	    modal:true,
	    draggable:true,
	    context: ["showbtn", "tl", "bl"]
	    });
		popinPanels['Panel1'].render(document.body);
		var msg=trnAdvancedFilter;
		popinPanels['Panel1'].setHeader(msg);
		var element = document.getElementById("dialog2");
		element.style.display 	= "inline";
		popinPanels['Panel1'].setBody(element);
	}
	popinPanels['Panel1'].show();
	changeTab(0);
}

function hidePopin() {
	popinPanels['Panel1'].hide();
}

function showExport() {
	if ( popinPanels['Panel2'] == null ) {
		popinPanels['Panel2'] = new YAHOO.widget.Panel('Panel2', {
		width:"750px",
		maxHeight:"500px",
		fixedcenter: true,
	    constraintoviewport: false,
	    underlay:"none",
	    close:true,
	    visible:false,
	    modal:true,
	    draggable:true,
	    context: ["showbtn", "tl", "bl"]
	    });
		popinPanels['Panel2'].render(document.body);
		var msg=trnExportOptions;
		popinPanels['Panel2'].setHeader(msg);
		var element = document.getElementById("exportPopin");
		element.style.display 	= "inline";
		popinPanels['Panel2'].setBody(element);
	}
	popinPanels['Panel2'].show();
}
function hideExport() {
	popinPanels['Panel2'].hide();
}

function doExport(){
	var options = "?";
	options += "typeOpt=" + getOptionChecked("export_type_");
	options += "&summaryOpt=" + getOptionChecked("export_summary_");
	options += "&ODAGrowthOpt=" + getOptionChecked("export_ODAGrowth_");
	options += "&fundingOpt=" + getOptionChecked("export_funding_");
	options += "&aidPredicOpt=" + getOptionChecked("export_aid_pred_");
	options += "&aidTypeOpt=" + getOptionChecked("export_aid_type_");
	options += "&financingInstOpt=" + getOptionChecked("export_fin_inst_");
	options += "&donorOpt=" + getOptionChecked("export_donor_");
	options += "&sectorOpt=" + getOptionChecked("export_sector_");
	options += "&regionOpt=" + getOptionChecked("export_region_");
	var type = "" + getOptionChecked("export_type_");
	if (type=="0") {
		document.visualizationform.action= urlPdfExport + options ;
		document.visualizationform.target="_blank";
		document.visualizationform.submit();
	} 
	if (type=="1") {
		document.visualizationform.action= urlWordExport + options ;
		document.visualizationform.target="_blank";
		document.visualizationform.submit();
	}
	if (type=="2") {
		document.visualizationform.action= urlExcelExport + options ;
		document.visualizationform.target="_blank";
		document.visualizationform.submit();
	}
	hideExport();
}

function getOptionChecked (elements){
	var cnt = 0;
	while (document.getElementById("" + elements + cnt) != null) {
		if (document.getElementById("" + elements + cnt).checked == true) {
			return document.getElementById("" + elements + cnt).value;
		}
		cnt++;
	}
	return 0;
}

function resetToDefaults(){
	//loadingPanel.show();
	
	unCheckOptions("org_grp_check");
	unCheckOptions("region_check");
	unCheckOptions("sector_config_check");
	unCheckOptions("sector_check");
	unCheckOptions("organization_check");
	unCheckOptions("zone_check");
	unCheckOptions("sub_sector_check");
	
	document.getElementById("decimalsToShow_dropdown").selectedIndex = 2;
	document.getElementById("topLists_dropdown").selectedIndex = 0;
	
	document.getElementById("commitments_visible").checked = true;
	document.getElementById("disbursements_visible").checked = true;
	if (document.getElementById("expenditures_visible")!=null){
		document.getElementById("expenditures_visible").checked = true;
	}
	if (document.getElementById("pledge_visible")!=null){
		document.getElementById("pledge_visible").checked = true;
	}
	if (document.getElementById("workspace_only")!=null){
		document.getElementById("workspace_only").checked = false;
	}
	
	
	document.getElementById("transaction_type_0").checked = false;
	document.getElementById("transaction_type_1").checked = true;
	if (document.getElementById("transaction_type_2")!=null){
		document.getElementById("transaction_type_2").checked = false;
	}
	document.getElementById("org_group_dropdown_id").selectedIndex = 0;
	document.getElementById("region_dropdown_id").selectedIndex = 0;
	document.getElementById("sector_dropdown_id").selectedIndex = 0;
	document.getElementById("sector_config_dropdown_id").selectedIndex = 0;
	removeOptionsDropdown("org_dropdown_id");
	removeOptionsDropdown("zone_dropdown_id");
	removeOptionsDropdown("sector_dropdown_id");
	removeOptionsDropdown("sub_sector_dropdown_id");
	document.getElementById("filterOrganizations").innerHTML = trnAll;
	document.getElementById("filterOrgGroups").innerHTML = trnAll;
	document.getElementById("filterSectors").innerHTML = trnAll;
	document.getElementById("filterSectorConfiguration").innerHTML = trnPrimary;
	document.getElementById("filterRegions").innerHTML = trnAll;

	document.getElementById("show_amounts_in_thousands").checked = false;
	applyFilterPopin();
}

function removeOptionsDropdown(object){
	obj = document.getElementById(object);
	for(var i = 1; i < obj.options.length; i++){
		obj.options[i].remove;
	}
	obj.options.length = 0;
	obj.options[0] = new Option("All", -1);
}
function removeOptions (obj){
	var div = document.getElementById(obj);
	div.innerHTML = "";
}

function unCheckOptions (obj){
	var elems = document.getElementsByName(obj);
	for(i=0;i<elems.length;i++){
		elems[i].checked=false;
	}
}

function changeTab (selected){
	for(var i=0;i<4;i++){
		if(i!=selected){
			$("#general_selector_"+i).removeClass("side_opt_sel");	
		}
		else{
			$("#general_selector_"+i).addClass("side_opt_sel");	
		}
	}
	$("#generalInfoId").css("display","none");
	$("#orgGrpContent").css("display","none");
	$("#regionDivContent").css("display","none");
	$("#sectorDivContent").css("display","none");
	
	if(selected!=0){
		clearAllLocalSearchResults();
	}
	
	
	switch (selected) {
	case 0:
		$("#generalInfoId").css("display","block");
		break;
	case 1:
		$("#orgGrpContent").css("display","block");
		break;
	case 2:
		$("#regionDivContent").css("display","block");
		break;
	case 3:
		$("#sectorDivContent").css("display","block");
		break;
	default:
		break;
	}
}


		
function getChecked (checkName){
	var count = 0;
	var id = 0;
	if (checkName!=null){
		var checks = document.getElementsByName(checkName);
		for (i=0; i<checks.length; i++) {
		  if (checks[i].checked) {
		    count++;
		    id = checks[i].value;
		  }
		}
	}
	if (count > 1) {
		return -1;
	} else {
		return id;
	}
}


function toggleSettings(){
	
	var currentDisplaySettings = document.getElementById('currentDisplaySettings');
	var displaySettingsButton = document.getElementById('displaySettingsButton');
	if(currentDisplaySettings.style.display == "block"){
		currentDisplaySettings.style.display = "none";
		displaySettingsButton.innerHTML = trnShowFilterSetttings;
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = trnHideFilterSetttings;
	}
}

var callbackChildrenCall = {
	  success: function(o) {
		  try {
			    var results = YAHOO.lang.JSON.parse(o.responseText);
			    switch(results.objectType)
			    {
				    case "Organization":
			    		var orgDropdown = document.getElementById("org_dropdown_id");
			    		orgDropdown.options.length = 0;
			    		orgDropdown.options[0] = new Option(trnAll, -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			orgDropdown.options[orgDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
				    case "Sector":
			    		var subSectorDropdown = document.getElementById("sub_sector_dropdown_id");
			    		subSectorDropdown.options.length = 0;
			    		subSectorDropdown.options[0] = new Option(trnAll, -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			subSectorDropdown.options[subSectorDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	case "Region":
			    		var zonesDropdown = document.getElementById("zone_dropdown_id");
			    		zonesDropdown.options.length = 0;
			    		zonesDropdown.options[0] = new Option(trnAll, -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			zonesDropdown.options[zonesDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	  case "Config":
				    		var sectorDropdown = document.getElementById("sector_dropdown_id");
				    		var subSectorDropdown = document.getElementById("sub_sector_dropdown_id");
				    		subSectorDropdown.options.length = 0;
				    		sectorDropdown.options.length = 0;
				    		subSectorDropdown.options[0] = new Option(trnAll, -1);
				    		sectorDropdown.options[0] = new Option(trnAll, -1);
				    		for(var i = 0; i < results.children.length; i++){
				    			sectorDropdown.options[sectorDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
				    		}
				    		break;
			    	
			    }
			}
			catch (e) {
			    alert("Invalid respose.");
			}
	  },
	  failure: function(o) {//Fail silently
		  }
	};

function callbackChildren(e) {
	var parentId, targetId, targetObj;
	//if (e == undefined){
		parentId = this.value;
		targetId = this.id;
		targetObj = this;
	//}
	//else
	//{
	//	parentId = e.target.value;
	//	targetId = e.target.id;
	//	targetObj = e.target;
	//}
	
	var objectType = "";

	switch(targetId){
		case "sector_config_dropdown_id":
		objectType = "Config";
		break;
		case "sector_dropdown_id":
			objectType = "Sector";
			//try to set the SectorProfileItemId from select:
			try {
				document.getElementById("SectorProfileItemId").value = parentId;
			}
			catch(e){
					
			}
			break;
		case "region_dropdown_id":
			objectType = "Region";
			//try to set the SectorProfileItemId from select:
			try {
				document.getElementById("RegionProfileItemId").value = parentId;
			}
			catch(e){
					
			}
			break;
		case "org_group_dropdown_id":
			objectType = "Organization";
			break;
	}

	if (parentId != "" && objectType != ""){
		var transaction = YAHOO.util.Connect.asyncRequest('GET', "/visualization/dataDispatcher.do?action=getJSONObject&objectType=" + objectType + "&parentId=" + parentId, callbackChildrenCall, null);
	}
}

function countSelected (selector){
	var count = 0;
	if (selector!=null){
		for (i=0; i<selector.options.length; i++) {
		  if (selector.options[i].selected) {
		    count++;
		  }
		}
	}
	return count;
}

var callbackApplyFilterCall = {
		  success: function(o) {
			  panelLoaded = true;
			  refreshBoxes(o);
			  refreshGraphs();
		  },
		  failure: function(o) {
			  loadingPanel.hide();
		  }
		};

function callbackApplyFilter(e){
	panelLoaded = false;
	if (document.getElementById("workspaceOnlyQuickFilter")!=null){
		document.getElementById("workspaceOnly").value = document.getElementById("workspaceOnlyQuickFilter").checked;
		document.getElementById("workspace_only").checked = document.getElementById("workspaceOnlyQuickFilter").checked;
	}
	document.getElementById("currencyId").value = document.getElementById("currencyQuickFilter_dropdown").value;
	document.getElementById("currencies_dropdown_ids").value = document.getElementById("currencyQuickFilter_dropdown").value;
	document.getElementById("startYear").value = document.getElementById("startYearQuickFilter_dropdown").value;
	document.getElementById("endYear").value = document.getElementById("endYearQuickFilter_dropdown").value;
	document.getElementById("startYear_dropdown").value = document.getElementById("startYearQuickFilter_dropdown").value;
	document.getElementById("endYear_dropdown").value = document.getElementById("endYearQuickFilter_dropdown").value;
	document.getElementById("transactionType").value = document.getElementById("transactionType_dropdown").value;
	document.getElementById("showAmountsInThousands").value = document.getElementById("show_amounts_in_thousands").checked;
	
	loadingPanel.show();

	YAHOO.util.Connect.setForm('visualizationform');

	var sUrl="/visualization/dataDispatcher.do?action=applyFilter";

	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackApplyFilterCall);

	loadingPanel.loadingPanel.setBody("");
	refreshLoadingPanel();

	document.getElementById("filterOrgGroups").innerHTML = document.getElementById("org_group_dropdown_id").options[document.getElementById("org_group_dropdown_id").selectedIndex].text;
	document.getElementById("filterOrganizations").innerHTML = document.getElementById("org_dropdown_id").options[document.getElementById("org_dropdown_id").selectedIndex].text;
	document.getElementById("filterSectorConfiguration").innerHTML = document.getElementById("sector_config_dropdown_id").options[document.getElementById("sector_config_dropdown_id").selectedIndex].text;
	document.getElementById("filterSectors").innerHTML = document.getElementById("sector_dropdown_id").options[document.getElementById("sector_dropdown_id").selectedIndex].text;
	document.getElementById("filterRegions").innerHTML = document.getElementById("region_dropdown_id").options[document.getElementById("region_dropdown_id").selectedIndex].text;
	document.getElementById("filterStartYear").innerHTML = document.getElementById("startYearQuickFilter_dropdown").options[document.getElementById("startYearQuickFilter_dropdown").selectedIndex].text;
	document.getElementById("filterEndYear").innerHTML = document.getElementById("endYearQuickFilter_dropdown").options[document.getElementById("endYearQuickFilter_dropdown").selectedIndex].text;

}
var panelLoaded = false;
function refreshLoadingPanel(){
	if(!panelLoaded){
		var sUrl="/visualization/dataDispatcher.do?action=getProgress&rnd=" + Math.floor(Math.random()*50000);
		var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackUpdateLoadingPanel);
	}
}
var callbackUpdateLoadingPanel = {
		  success: function(o) {
			   loadingPanel.loadingPanel.setBody(o.responseText + '<img src="/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif" />');
			   loadingPanel.loadingPanel.render(document.body);
		       var cancelLink = document.createElement('a');
		       $D.setStyle(cancelLink, 'cursor', 'pointer');
		       cancelLink.appendChild(document.createTextNode(trnCancel));
		       $E.on(cancelLink, 'click', function(e, o){
		           loadingPanel.loadingPanel.hide();
		           loadingPanel.cancelEvent.fire();
		           window.stop();
		       }, {self:this});
		       loadingPanel.loadingPanel.appendToBody(document.createElement('br'));
		       loadingPanel.loadingPanel.appendToBody(cancelLink);
		       $D.setStyle(loadingPanel.body, 'text-align', 'center');

//			  loadingPanel.loadingPanel.setBody( + '<img src="/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif" />');
			  if(!panelLoaded)
			  	setTimeout(refreshLoadingPanel, 1000);
		  },
		  failure: function(o) {
//			  alert("error");
		  }
		};

function applyFilterPopin(e){
	
//var allGraphs = document.getElementsByName("flashContent");
	document.getElementById("topLists").value = document.getElementById("topLists_dropdown").options[document.getElementById("topLists_dropdown").selectedIndex].value;
	document.getElementById("decimalsToShow").value = document.getElementById("decimalsToShow_dropdown").options[document.getElementById("decimalsToShow_dropdown").selectedIndex].value;
	document.getElementById("startYear").value = document.getElementById("startYear_dropdown").options[document.getElementById("startYear_dropdown").selectedIndex].value;
	document.getElementById("endYear").value = document.getElementById("endYear_dropdown").options[document.getElementById("endYear_dropdown").selectedIndex].value;
	//Copy the values of the start/end year from the Advanced to the quick
	document.getElementById("startYearQuickFilter_dropdown").value = document.getElementById("startYear_dropdown").options[document.getElementById("startYear_dropdown").selectedIndex].value;
	document.getElementById("endYearQuickFilter_dropdown").value = document.getElementById("endYear_dropdown").options[document.getElementById("endYear_dropdown").selectedIndex].value;

	//document.getElementById("yearToCompare").value = document.getElementById("yearToCompare_dropdown").options[document.getElementById("yearToCompare_dropdown").selectedIndex].value;
	document.getElementById("currencyId").value = document.getElementById("currencies_dropdown_ids").options[document.getElementById("currencies_dropdown_ids").selectedIndex].value;
	document.getElementById("currencyQuickFilter_dropdown").value = document.getElementById("currencies_dropdown_ids").options[document.getElementById("currencies_dropdown_ids").selectedIndex].value;
	document.getElementById("fiscalCalendarId").value = document.getElementById("fiscalCalendar_dropdown_Id").options[document.getElementById("fiscalCalendar_dropdown_Id").selectedIndex].value;
	document.getElementById("commitmentsVisible").value = document.getElementById("commitments_visible").checked;
	document.getElementById("disbursementsVisible").value = document.getElementById("disbursements_visible").checked;
	if (document.getElementById("expenditures_visible")!=null){
		document.getElementById("expendituresVisible").value = document.getElementById("expenditures_visible").checked;
	}
	if (document.getElementById("pledge_visible")!=null){
		document.getElementById("pledgeVisible").value = document.getElementById("pledge_visible").checked;
	}
	if (document.getElementById("workspace_only")!=null){
		document.getElementById("workspaceOnly").value = document.getElementById("workspace_only").checked;
		document.getElementById("workspaceOnlyQuickFilter").checked = document.getElementById("workspace_only").checked;
	}
	document.getElementById("showAmountsInThousands").value = document.getElementById("show_amounts_in_thousands").checked;
	document.getElementById("showMonochrome").value = document.getElementById("show_monochrome").checked;
	
	if (document.getElementById("transaction_type_0").checked == true) {
		document.getElementById("transactionType").value = document.getElementById("transaction_type_0").value;
	}
	if (document.getElementById("transaction_type_1").checked == true) {
		document.getElementById("transactionType").value = document.getElementById("transaction_type_1").value;
	}
	if (document.getElementById("transaction_type_2")!=null){
		if (document.getElementById("transaction_type_2").checked == true) {
			document.getElementById("transactionType").value = document.getElementById("transaction_type_2").value;
		}
	}
	document.getElementById("transactionType_dropdown").value = document.getElementById("transactionType").value;
	
	var params = "";
	params = params + "&orgGroupIds=" + getSelectionsFromElement("org_grp_check",false);
	params = params + "&orgIds=" + getSelectionsFromElement("organization_check",false);
	params = params + "&regionIds=" + getSelectionsFromElement("region_check",false);
	params = params + "&zoneIds=" + getSelectionsFromElement("zone_check",false);
	params = params + "&selSectorConfigId=" + getSelectionsFromElement("sector_config_check",false);
	params = params + "&sectorIds=" + getSelectionsFromElement("sector_check",false);
	params = params + "&subSectorIds=" + getSelectionsFromElement("sub_sector_check",false);

	loadingPanel.show();
	YAHOO.util.Connect.setForm('visualizationform');
	var sUrl="/visualization/dataDispatcher.do?action=applyFilter" + params;

	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackApplyFilterCall);
	hidePopin();
}

function getSelectionsFromElement(elementId, text){
	var sels = "";
	var cnt = 0;
	var elems=document.getElementsByName(elementId);
	for(i=0;i<elems.length;i++){
		if (elems[i].checked==true){
			if(sels != ""){
				sels = sels + ",";
			}
			if(text){
				sels = sels + elems[i].title;
			} else {
				sels = sels + elems[i].value;
			}
			cnt++;
		}
	}
	return sels;
}
var nonRefreshedMovies = [];

function refreshGraphs(){
	//Get array of graphs
	var allGraphs = getElementsByName_iefix("div", "flashContent");
	//Push it into an array that will be emptied as they become available
	for(var idx = 0; idx < allGraphs.length; idx++){
		nonRefreshedMovies.push(allGraphs[idx].children[0]);
	}
	refreshAsync();
}

function getElementsByName_iefix(tag, name) {
  var elem = document.getElementsByTagName(tag);
  var arr = new Array();
  for(i = 0,iarr = 0; i < elem.length; i++) {
       att = elem[i].getAttribute("name");
       if(att == name) {
            arr[iarr] = elem[i];
            iarr++;
       }
  }
  return arr;
}
function refreshAsync(){
//	console.log("Refreshing graphs. Number of graphs to refresh: " + nonRefreshedMovies.length);
	if(nonRefreshedMovies.length > 0){
		//Get one flash movie, try to refresh it, if it doesn't work, push it back again
		var currentMovie = nonRefreshedMovies.shift();
//		console.log("popping one: " + currentMovie.id);
		try
		{
			if (YAHOO.env.ua.gecko <= 1.92 && YAHOO.env.ua.gecko != 0) {
				currentMovie.scrollIntoView(true);
			}
			currentMovie.refreshGraph();
//			console.log("success: " + currentMovie.id);
		}
		catch(e)
		{
//			console.log("back inside: " + currentMovie.id);
			nonRefreshedMovies.push(currentMovie);
		}
		setTimeout(refreshAsync, 500);
	}
	else
	{
		scroll(0,0);
		loadingPanel.hide();	
	}	
}
function refreshBoxes(o){

	var dashboardType = document.getElementById("dashboardType").value;
	var results = YAHOO.lang.JSON.parse(o.responseText);
	var inner = "";
	var inner2 = "";
	var valTotalDisbs="";
	var valNumOfProjs="";
	var valNumOfSecs="";
	var valNumOfRegs="";
	var valAvgProjSize="";
	
	for(var j = 0; j < results.children.length; j++){
		var child = results.children[j];
		switch(child.type){
			case "ProjectsList":
				inner = "";
				var isPublicView = document.getElementById("fromPublicView").value;
				for(var i = 0; i < child.top.length; i++){
					inner = inner + (i+1) + ". ";
					if (isPublicView == "false"){
						inner = inner + "<a target='_blank' href='/aim/viewActivityPreview.do~pageId=2~activityId=" + child.top[i].id + "~isPreview=1'>" + child.top[i].name + "</a>" + "  <b>(" + child.top[i].value + ")</b> <hr />";
					} else {
						inner = inner + child.top[i].name + "  <b>(" + child.top[i].value + ")</b> <hr />";
					}
				}
				inner = inner + "<a href='javascript:showFullList(\"projects\")' style='float:right;'>"+trnShowFullList+"</a>";
				var div = document.getElementById("divTopProjects");
				div.innerHTML = inner;
				break;
			case "DonorsList":
				if (dashboardType!=1) {
					inner = "";
					for(var i = 0; i < child.top.length; i++){
						inner = inner + (i+1) + ". " + child.top[i].name + "  <b>(" + child.top[i].value + ")</b> <hr />";
					}
					inner = inner + "<a href='javascript:showFullList(\"donors\")' style='float:right;'>"+trnShowFullList+"</a>";
					var div = document.getElementById("divTopDonors");
					div.innerHTML = inner;
				}
				break;
			case "SectorsList":
				if (dashboardType!=3) {
					inner = "";
					for(var i = 0; i < child.top.length; i++){
						inner = inner + (i+1) + ". " + child.top[i].name + "  <b>(" + child.top[i].value + ")</b> <hr />";
					}
					inner = inner + "<a href='javascript:showFullList(\"sectors\")' style='float:right;'>"+trnShowFullList+"</a>";
					var div = document.getElementById("divTopSectors");
					div.innerHTML = inner;
				}
				break;
			case "RegionsList":
				if (dashboardType!=2) {
					inner = "";
					for(var i = 0; i < child.top.length; i++){
						inner = inner + (i+1) + ". " + child.top[i].name + "  <b>(" + child.top[i].value + ")</b> <hr />";
					}
					inner = inner + "<a href='javascript:showFullList(\"regions\")' style='float:right;'>"+trnShowFullList+"</a>";
					var div = document.getElementById("divTopRegions");
					div.innerHTML = inner;
				}
				break;
			case "SelOrgGroupsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
						inner2 = inner2 + child.list[i].name + " - "
					}
					var div = document.getElementById("org_group_list_id");
					div.innerHTML = inner;
					document.getElementById("filterOrgGroups").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("org_group_dropdown_id").style.display = "none";
					} else {
						document.getElementById("org_group_list_id").style.display = "none";
						document.getElementById("org_group_dropdown_id").style.display = "";
					}
				//}
				break;
			case "SelOrgsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
						inner2 = inner2 + child.list[i].name + " - "
					}
					var div = document.getElementById("org_list_id");
					div.innerHTML = inner;
					document.getElementById("filterOrganizations").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("org_dropdown_id").style.display = "none";
					} else {
						document.getElementById("org_list_id").style.display = "none";
						document.getElementById("org_dropdown_id").style.display = "";
					}
				//}
				break;
			case "SelRegionsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
						inner2 = inner2 + child.list[i].name + " - "
					}
					var div = document.getElementById("region_list_id");
					div.innerHTML = inner;
					document.getElementById("filterRegions").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("region_dropdown_id").style.display = "none";
					} else {
						document.getElementById("region_list_id").style.display = "none";
						document.getElementById("region_dropdown_id").style.display = "";
					}
				//}
				break;
			case "SelZonesList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
					}
					var div = document.getElementById("zone_list_id");
					div.innerHTML = inner;
					div.style.display = "";
					document.getElementById("zone_dropdown_id").style.display = "none";
					} else {
						document.getElementById("zone_list_id").style.display = "none";
						document.getElementById("zone_dropdown_id").style.display = "";
					}
				//}
				break;
			case "SelSectorConfig":
					if (child.list.length > 0) {
					inner =child.list[0].name;
					var div = document.getElementById("sector_config_list_id");
					div.innerHTML = inner;
					document.getElementById("filterSectorConfiguration").innerHTML = inner;
					div.style.display = "";
					document.getElementById("sector_config_dropdown_id").style.display = "none";
					} else {
						document.getElementById("sector_config_list_id").style.display = "none";
						document.getElementById("sector_config_dropdown_id").style.display = "";
					}
				break;
			case "SelSectorsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "";
					inner2 = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
						inner2 = inner2 + child.list[i].name + " - "
					}
					var div = document.getElementById("sector_list_id");
					div.innerHTML = inner;
					document.getElementById("filterSectors").innerHTML = inner2;
					div.style.display = "";
					document.getElementById("sector_dropdown_id").style.display = "none";
					} else {
						document.getElementById("sector_list_id").style.display = "none";
						document.getElementById("sector_dropdown_id").style.display = "";
					}
				//}
				break;
			case "SelSubSectorsList":
				//if (dashboardType!=3) {
					if (child.list.length > 0) {
					inner = "";
					for(var i = 0; i < child.list.length; i++){
						inner = inner + "<li>" + child.list[i].name + "</li>"
					}
					var div = document.getElementById("sub_sector_list_id");
					div.innerHTML = inner;
					div.style.display = "";
					document.getElementById("sub_sector_dropdown_id").style.display = "none";
					} else {
						document.getElementById("sub_sector_list_id").style.display = "none";
						document.getElementById("sub_sector_dropdown_id").style.display = "";
					}
				//}
				break;
			case "TotalComms":
				inner = "<b class='dashboard_total_num'>" + child.value + "</b><br />" + trnTotalCommitments;
				var div = document.getElementById("divTotalComms");
				div.innerHTML = inner;
				var textAmounts = document.getElementById("show_amounts_in_thousands").checked ? trnAllAmountsInThousands:trnAllAmountsInMillions;
				
				inner = "<i><font size='2' color='red'>" + textAmounts + " - " + child.curr + "</font></i>";
				document.getElementById("currencyCode").value = child.curr;
				var div = document.getElementById("currencyInfo");
				div.innerHTML = inner;
				
				var div = document.getElementById("filterCurrency");
				div.innerHTML = "" + child.curr;
				
				break;
			case "TotalDisbs":
				valTotalDisbs = child.value;
				break;
			case "NumberOfProjs":
				valNumOfProjs = child.value;
				break;
			case "NumberOfDons":
				valNumOfDons = child.value;
				break;
			case "NumberOfSecs":
				valNumOfSecs = child.value;
				break;
			case "NumberOfRegs":
				valNumOfRegs = child.value;
				break;
			case "AvgProjSize":
				valAvgProjSize = child.value;
				break;
			case "SelOrgContact":
				if (child.list.length ==1) {
					var contact=child.list[0];
					var contactMarkup = new Array();
					contactMarkup.push("<table class=\"inside\">");
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\">" + trnTitle + ":</td>");
					contactMarkup.push("<td class=\"inside\">");
					contactMarkup.push(contact.title);
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\">" + trnName + ":</td>");
					contactMarkup.push("<td class=\"inside\">");
					contactMarkup.push(contact.name);
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\">" + trnEmails + ":</td>");
					contactMarkup.push("<td class=\"inside\">");
					var conactEmails=contact.email;
				
					for(var i=0;i<conactEmails.length;i++){
						contactMarkup.push(conactEmails[i].value);
						contactMarkup.push("<br/>");
						
					}
		
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\">" + trnPhones + ":</td>");
					contactMarkup.push("<td class=\"inside\">");
					var conactPhones=contact.phones;
					for(var i=0;i<conactPhones.length;i++){
						contactMarkup.push(conactPhones[i].value);
						contactMarkup.push("<br/>");
						
					}
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					
					contactMarkup.push("<tr>");
					contactMarkup.push("<td class=\"inside\">" + trnFaxes + ":</td>");
					contactMarkup.push("<td class=\"inside\">");
					var conactFaxes=contact.faxes;
					for(var i=0;i<conactFaxes.length;i++){
						contactMarkup.push(conactFaxes[i].value);
						contactMarkup.push("<br/>");
						
					}
					contactMarkup.push("</td>");
					contactMarkup.push("</tr>");
					contactMarkup.push("</table>");
					$("#tab2").html(contactMarkup.join(""));
					
				}
				else{
					$("#tab2").html(trnNoContactInfo);
				}
				break;
			case "SelAdditionalInfo":
				if (typeof child.additionalInfo.info != 'undefined') {
					var info=child.additionalInfo.info;
					var infoMarkup = new Array();
					infoMarkup.push("<div id=\"saveResultMsg\"></div><table class=\"inside\"><tbody>");
					infoMarkup.push("<tr>");
					infoMarkup.push("<td class=\"inside\">" + trnBackgroundDonor +":</td>");
					infoMarkup.push("<td class=\"inside\">");
					infoMarkup.push("<textarea cols=\"40\" rows=\"3\" id=\"orgBackground\">");
					infoMarkup.push(info.orgBackground);
					infoMarkup.push("</textarea>");
					infoMarkup.push("</td>");
					infoMarkup.push("</tr>");

					infoMarkup.push("<tr>");
					infoMarkup.push("<td class=\"inside\">" + trnDescription + ":</td>");
					infoMarkup.push("<td class=\"inside\">");
					infoMarkup.push("<textarea cols=\"40\" rows=\"3\" id=\"orgDescription\">");
					infoMarkup.push(info.orgDescription);
					infoMarkup.push("</textarea>");
					infoMarkup.push("</td>");
					infoMarkup.push("</tr>");
					
					infoMarkup.push("<tr>");
					infoMarkup.push("<td class=\"inside\" colspan=\"2\">");
					infoMarkup.push("<input type=\"button\" value=\"" + trnSave + "\" onclick=\"saveAdditionalInfo("+info.orgId+")\"/>");
					infoMarkup.push("</td>");
					infoMarkup.push("</tr>");
					infoMarkup.push("</tbody></table>");
					var markup=infoMarkup.join("");
					$("#tab3").html(markup);
				}
				else{
					$("#tab3").html(trnNoAdditionalInfo);
				}
				break; 
				
		}
	}
	inner = "<a title='" + trnTotalDisbsDescription + "' style='color: black;'>" + trnTotalDisbs + "</a> <b>" + valTotalDisbs + "</b><span class='breadcrump_sep'>|</span>";
	inner = inner + "<a title='" + trnNumOfProjsDescription + "' style='color: black;'>" + trnNumOfProjs + "</a> <b>" + valNumOfProjs + "</b><span class='breadcrump_sep'>|</span>";
	if (dashboardType!=1) {
		inner = inner + "<a title='" + trnNumOfDonsDescription + "' style='color: black;'>" + trnNumOfDons + "</a> <b>" + valNumOfDons + "</b><span class='breadcrump_sep'>|</span>";
	}
	if (dashboardType!=3) {
		inner = inner + "<a title='" + trnNumOfSecsDescription + "' style='color: black;'>" + trnNumOfSecs + "</a> <b>" + valNumOfSecs + "</b><span class='breadcrump_sep'>|</span>";
	}
	if (dashboardType!=2) {
		inner = inner + "<a title='" + trnNumOfRegsDescription + "' style='color: black;'>" + trnNumOfRegs + "</a> <b>" + valNumOfRegs + "</b><span class='breadcrump_sep'>|</span>";
	}
	inner = inner + "<a title='" + trnAvgProjSizeDescription + "' style='color: black;'>" + trnAvgProjSize + "</a> <b>" + valAvgProjSize;
	var div = document.getElementById("divSummaryInfo");
	div.innerHTML = inner;

	var namePlaceholder = document.getElementById("dashboard_name");
	if (dashboardType==1) {
		var name1 = "";
		var name2 = "";
		if (getSelectionsFromElement("org_grp_check",true)==""){
			if (document.getElementById("org_group_dropdown_id").selectedIndex == 0) {
				name1 = trnAllOrgGroups;
			} else {
				name1 = document.getElementById("org_group_dropdown_id").options[document.getElementById("org_group_dropdown_id").selectedIndex].text;
			}
		} else {
			name1 = getSelectionsFromElement("org_grp_check",true);
		}
		if (getSelectionsFromElement("organization_check",true)==""){
			if (document.getElementById("org_dropdown_id").selectedIndex == 0) {
				//Do nothing
			} else {
				name2 = document.getElementById("org_dropdown_id").options[document.getElementById("org_dropdown_id").selectedIndex].text;
			}
		} else {
			if (getSelectionsFromElement("organization_check",false).indexOf(',') !=-1) {
				name2 = trnMultipleOrgs;
			} else {
				name2 = getSelectionsFromElement("organization_check",true);
			}
		}
		name1 = name1.replace(/</g, "< ");
		namePlaceholder.innerHTML =  "<span style=\"font-size:18px\">" + name1 + "</span><br/><span style=\"font-size:13px\">" + name2 + "</span>";
	}
	if (dashboardType==3) {
		var name1 = "";
		var name2 = "";
		if (getSelectionsFromElement("sector_check",true)==""){
			if (document.getElementById("sector_dropdown_id").selectedIndex == 0) {
				name1 = trnAllSectors;
			} else {
				name1 = document.getElementById("sector_dropdown_id").options[document.getElementById("sector_dropdown_id").selectedIndex].text;
			}
		} else {
			name1 = getSelectionsFromElement("sector_check",true);
		}
		if (getSelectionsFromElement("sub_sector_check",true)==""){
			if (document.getElementById("sub_sector_dropdown_id").selectedIndex == 0) {
				name2 = trnAllSubSector;
			} else {
				name2 = document.getElementById("sub_sector_dropdown_id").options[document.getElementById("sub_sector_dropdown_id").selectedIndex].text;
			}
		} else {
			if (getSelectionsFromElement("sub_sector_check",false).indexOf(',') !=-1) {
				name2 = trnMultipleSubSector;
			} else {
				name2 = getSelectionsFromElement("sub_sector_check",true);
			}
		}
		name1 = name1.replace(/</g, "< ");
		namePlaceholder.innerHTML =  "<span style=\"font-size:18px\">" + name1 + "</span><br/><span style=\"font-size:13px\">" + name2 + "</span>";
	}
	if (dashboardType==2) {
		var name1 = "";
		var name2 = "";
		if (getSelectionsFromElement("region_check",true)==""){
			if (document.getElementById("region_dropdown_id").selectedIndex == 0) {
				name1 = trnAllRegions;
			} else {
				name1 = document.getElementById("region_dropdown_id").options[document.getElementById("region_dropdown_id").selectedIndex].text;
			}
		} else {
			name1 = getSelectionsFromElement("region_check",true);
		}
		if (getSelectionsFromElement("zone_check",true)==""){
			if (document.getElementById("zone_dropdown_id").selectedIndex == 0) {
				name2 = trnAllZones;
			} else {
				name2 = document.getElementById("zone_dropdown_id").options[document.getElementById("zone_dropdown_id").selectedIndex].text;
			}
		} else {
			if (getSelectionsFromElement("zone_check",false).indexOf(',') !=-1) {
				name2 = trnMultipleZones;
			} else {
				name2 = getSelectionsFromElement("zone_check",true);
			}
		}
		name1 = name1.replace(/</g, "< ");
		namePlaceholder.innerHTML =  "<span style=\"font-size:18px\">" + name1 + "</span><br/><span style=\"font-size:13px\">" + name2 + "</span>";
	}
	
	var type = document.getElementById("transactionType").value;
	var fundType = "";
	if (type==0) {
		fundType = trnCommitments;
	}
	if (type==1) {
		fundType = trnDisbursements;
	}
	if (type==2) {
		fundType = trnExpenditures;
	}
	try
	{
		if (dashboardType==1) {
			var currentYear = document.getElementById("endYear").value;
			var yearToCompare = document.getElementById("yearToCompare").value;
			if (yearToCompare == "0" || yearToCompare == "" || yearToCompare == null || yearToCompare >= currentYear){
				yearToCompare =  "" + (currentYear - 1);
			}
			var endYear =  "" + currentYear;
			div = document.getElementById("ODAGrowthTitleLegend");
			input = document.getElementById("ODAGrowthTitle");
			if (document.getElementById("org_dropdown_id").selectedIndex == 0) {
				value = trnODAGrowth + " " + " - " + fundType;
			} else {
				value = trnODAGrowth + " " + " - " + document.getElementById("org_dropdown_id").options[document.getElementById("org_dropdown_id").selectedIndex].text;
				if (getSelectionsFromElement("organization_check",true)!="" && getSelectionsFromElement("organization_check",false).indexOf(',') ==-1) {
					value = trnODAGrowth + " " + " - " + getSelectionsFromElement("organization_check",true);
				}
			}
			if (div != null)
				div.innerHTML = value;
			if (input != null)
				input.value = value;
		}
		div = document.getElementById("AidPredictabilityTitleLegend");
		input = document.getElementById("AidPredictabilityTitle");
		value = trnAidPredictability + " - " + fundType;
		if (div != null)
			div.innerHTML = value;
		if (input != null)
			input.value = value;
	
		div = document.getElementById("AidTypeTitleLegend");
		input = document.getElementById("AidTypeTitle");
		value = trnAidType + " - " + fundType;
		if (div != null)
			div.innerHTML = value;
		if (input != null)
			input.value = value;
	
		div = document.getElementById("FinancingInstrumentTitleLegend");
		input = document.getElementById("FinancingInstrumentTitle");
		value = trnFinancingInstrument + " - " + fundType;
		if (div != null)
			div.innerHTML = value;
		if (input != null)
			input.value = value;
	
		if (dashboardType!=1) {
			div = document.getElementById("DonorProfileTitleLegend");
			input = document.getElementById("DonorProfileTitle");
			value = trnDonorProfile + " - " + fundType;
			if (div != null)
				div.innerHTML = value;
			if (input != null)
				input.value = value;
		}
		if (dashboardType !=3 || dashboardType == 3) {
			div = document.getElementById("SectorProfileTitleLegend");
			input = document.getElementById("SectorProfileTitle");
			isSubsector = (document.getElementById("SectorProfileItemId") && document.getElementById("SectorProfileItemId").value != "-1") ? true : false;
			if(isSubsector){
				value = trnSubSectorProfile + " - " + fundType;
			}
			else
			{
				value = trnSectorProfile + " - " + fundType;
			}
			if (div != null)
				div.innerHTML = value;
			if (input != null)
				input.value = value;
		}
		if (dashboardType!=2 ||  dashboardType == 2) {
			div = document.getElementById("RegionProfileTitleLegend");
			input = document.getElementById("RegionProfileTitle");
			isSubregion = (document.getElementById("RegionProfileItemId") && document.getElementById("RegionProfileItemId").value != "-1") ? true : false;
			if(isSubregion){
				value = trnSubRegionProfile + " - " + fundType;
			}
			else
			{
				value = trnRegionProfile + " - " + fundType;
			}
			if (div != null)
				div.innerHTML = value;
			if (input != null)
				input.value = value;
		}
	}
	catch(e){
		
	}

	var startYear = document.getElementById("startYear").value;
	var endYear = document.getElementById("endYear").value;

	div = document.getElementById("topProjectsTitle");
	if (startYear == endYear) {
		inner = trnTopProjects + " (" + startYear + ")";
	} else {
		inner = trnTopProjects + " (" + startYear + "-" + endYear + ")";
	}
	div.innerHTML = inner;
	if (dashboardType!=3) {
		div = document.getElementById("topSectorsTitle");
		if (startYear == endYear) {
			inner = trnTopSectors + " (" + startYear + ")";
		} else {
			inner = trnTopSectors + " (" + startYear + "-" + endYear + ")";
		}
		div.innerHTML = inner;
	}
	if (dashboardType!=1) {
		div = document.getElementById("topDonorsTitle");
		if (startYear == endYear) {
			inner = trnTopDonors + " (" + startYear + ")";
		} else {
			inner = trnTopDonors + " (" + startYear + "-" + endYear + ")";
		}
		div.innerHTML = inner;
	}
	if (dashboardType!=2) {
		div = document.getElementById("topRegionsTitle");
		if (startYear == endYear) {
			inner = trnTopRegions + " (" + startYear + ")";
		} else {
			inner = trnTopRegions + " (" + startYear + "-" + endYear + ")";
		}
		div.innerHTML = inner;
	}
}

YAHOO.util.Event.onDOMReady(initializeTranslations);
YAHOO.util.Event.onDOMReady(initializeGlobalVariables);
YAHOO.util.Event.onDOMReady(initializePage);
YAHOO.util.Event.onDOMReady(initDashboard);
YAHOO.util.Event.onDOMReady(initPanel);
YAHOO.util.Event.addListener("region_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.addListener("org_group_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.addListener("sector_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.addListener("sector_config_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.addListener("applyButton", "click", callbackApplyFilter);
YAHOO.util.Event.addListener("applyButtonPopin", "click", applyFilterPopin);
YAHOO.util.Event.addListener("visualizationDiv", "click", refreshGraphs);

var initialized = false;
function initDashboard(){
	var dashboardType = document.getElementById("dashboardType").value;
	changeChart(null, 'bar', 'FundingChart', true);
	changeChart(null, 'bar', 'ODAGrowth');
	changeChart(null, 'bar', 'AidPredictability', true);
	changeChart(null, 'bar', 'AidType', true);
	changeChart(null, 'bar', 'FinancingInstrument', true);
	if (dashboardType!=1) {
		changeChart(null, 'bar_profile', 'DonorProfile', true);
	}
	changeChart(null, 'bar_profile', 'SectorProfile', true);
	changeChart(null, 'bar_profile', 'RegionProfile', true);
	callbackApplyFilter();
}

function  saveAdditionalInfo(orgId){
    var postString		="orgBackground=" + document.getElementById("orgBackground").value+
        "&orgDescription="+document.getElementById("orgDescription").value+"&orgId="+orgId ;
        $("#saveResultMsg").html(trnSavingInformation);
        YAHOO.util.Connect.asyncRequest("POST", urlSaveAdditional, additionalInfoCallback, postString);
    }

    var additionalInfoResponseSuccess = function(o){
    	$("#saveResultMsg").html(trnSavedInformation);
    }

    var additionalInfoResponseFailure = function(o){
    	$("#saveResultMsg").html(trnFailedSave);
    }
    var additionalInfoCallback =
        {
        success:additionalInfoResponseSuccess,
        failure:additionalInfoResponseFailure
    };

function getValueToFlash(idContainer, field){
	
	if (field == 'Currency'){
		return document.getElementById("currencyCode").value;
	}
	if (field == 'DecimalsToShow'){
		return document.getElementById("decimalsToShow").value;
	}
	var inputObject = document.getElementById(idContainer + field);
	var returnValue;
	
	if(inputObject != undefined && inputObject.type == "checkbox"){
		returnValue = (inputObject == undefined) ? "" : inputObject.checked;
	}
	else
	{
		returnValue = (inputObject == undefined) ? "" : inputObject.value;
	}
	return returnValue;
}


function updateGraph(e, chartName){

	//Get array of graphs
	var allGraphs = getElementsByName_iefix("div", "flashContent");
	//Iterate and refresh the graph
	for(var idx = 0; idx < allGraphs.length; idx++){
		// Get flash object and refresh it by calling internal
		if(allGraphs[idx].children[0].id.toLowerCase() == chartName.toLowerCase()){
			document.getElementById(chartName + "TitleLegend").innerHTML = document.getElementById(chartName + "Title").value; 
			allGraphs[idx].children[0].refreshGraph();
			break;
		}
	}
}

function changeChart(e, chartType, container, useGeneric){
	var startMovie = false;
	//Get the calling object to select it and remove style.
	if(e != null){
		if (e == "start"){
			startMovie = true; 
		} else {
			var caller = e.target || e.srcElement;
			var linkBar = caller.parentNode.getElementsByTagName("A");
		    for(var i in linkBar)
		    {
		    	linkBar[i].className = "";
		    }
		    caller.className = "sel_sm_b";
		    startMovie = true; // This is set to true if it comes from clicking in the top right of the charts
		}
	}

	var palette = "0xFF6600,0x7EAE58,0x88BFF5,0xBE0035,0x8B007E,0x99431C,0xFF6666,0x94FF29,0x2929FF,0xFF29FF";
	if (document.getElementById("show_monochrome").checked){
		palette = "0x000000,0x969696,0x191919,0xAFAFAF,0x323232,0xC8C8C8,0x4B4B4B,0xE1E1E1,0x646464,0xFAFAFA,0x7D7D7D";
	}
	 
	var decimalSeparator = document.getElementById("decimalSeparator").value;
	var groupSeparator = document.getElementById("groupSeparator").value;
	var decimalsToShow = document.getElementById("decimalsToShow").value;
	var currCode = document.getElementById("currencyCode").value;
	
	var flashvars = { 
			decimalSeparator: decimalSeparator, 
			groupSeparator: groupSeparator,
			decimalsToShow: decimalsToShow,
			currCode: currCode,
			palette: palette, 
			id: container,
			start: (startMovie ? "true" : "false")
		};
	var params = {
			wmode: "transparent"
		};
	var attributes = {};
	attributes.id = container;
	//Setting for cache in development mode
//	var cache = "?rnd=" + Math.floor(Math.random()*50000);
	var cache = "";
	
	switch(chartType){
		case "bar":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries.swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			else
			{
				if(container == "ODAGrowth"){
					swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
				}
				else
					swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			}
			break;
		case "bar_profile":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_Profile.swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			break;
		case "donut":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/PieChartSeries.swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/PieChart_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			break;
		case "line":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/LineAreaSeries.swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/LineAreaSeries_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			break;
		case "dataview":
			if(useGeneric)
				swfobject.embedSWF("/repository/visualization/view/charts/DataViewSeries.swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			else
				swfobject.embedSWF("/repository/visualization/view/charts/DataViewSeries_" + container + ".swf" + cache, container, "634", "460", "10.0.0", false, flashvars, params, attributes);
			break;
	}
	updateChartSettings(container, chartType);
	return false;
}

function updateChartSettings(container, chartType){
	var title = document.getElementById(container + "Title") == undefined ? "" : document.getElementById(container + "Title");
	var fontSize = document.getElementById(container + "FontSize") == undefined ? "" : document.getElementById(container + "FontSize");
	var boldTitle = document.getElementById(container + "Bold") == undefined ? "" : document.getElementById(container + "Bold");
	var showLegend = document.getElementById(container + "ShowLegend") == undefined ? "" : document.getElementById(container + "ShowLegend");
	var showDataLabel = document.getElementById(container + "ShowDataLabel") == undefined ? "" : document.getElementById(container + "ShowDataLabel");
	var rotateDataLabel = document.getElementById(container + "RotateDataLabel") == undefined ? "" : document.getElementById(container + "RotateDataLabel");
	var divide = document.getElementById(container + "Divide") == undefined ? "" : document.getElementById(container + "Divide");

	switch(chartType){
	case "bar":
		title.disabled = false;
		fontSize.disabled = false;
		boldTitle.disabled = false;
		showLegend.disabled = false;
		showDataLabel.disabled = false;
		rotateDataLabel.disabled = false;
		divide.disabled = false;
		break;
	case "donut":
		title.disabled = false;
		fontSize.disabled = false;
		boldTitle.disabled = false;
		showLegend.disabled = false;
		showDataLabel.disabled = true;
		rotateDataLabel.disabled = true;
		divide.disabled = true;
		break;
	case "line":
		title.disabled = false;
		fontSize.disabled = false;
		boldTitle.disabled = false;
		showLegend.disabled = false;
		showDataLabel.disabled = true;
		rotateDataLabel.disabled = true;
		divide.disabled = false;
		break;
	case "dataview":
		title.disabled = true;
		fontSize.disabled = true;
		boldTitle.disabled = true;
		showLegend.disabled = true;
		showDataLabel.disabled = true;
		rotateDataLabel.disabled = true;
		divide.disabled = true;
		break;
}
	
	
}
//var mySelec = new YAHOO.widget.TabView("selectDiv");
//mySelec.selectTab(0);


function reloadGraphs(){
	var dashboardType = document.getElementById("dashboardType").value;
	changeChart('start', 'bar', 'ODAGrowth');
	changeChart('start', 'bar', 'FundingChart', true);
	changeChart('start', 'bar', 'AidPredictability', true);
	changeChart('start', 'bar', 'AidType', true);
	changeChart('start', 'bar', 'FinancingInstrument', true);
	if (dashboardType!=1) {
		changeChart('start', 'bar_profile', 'DonorProfile', true);
	}
	changeChart('start', 'bar_profile', 'SectorProfile', true);
	changeChart('start', 'bar_profile', 'RegionProfile', true);
}

function itemClick(id, type, startYear, endYear){
	  openNewWindow(600, 400);
	  var urlItemClick= urlShowList + "&id=" + id + "&type=" + type + "&startYear=" + startYear + "&endYear=" + endYear;
	  document.visualizationform.action = urlItemClick;
	  document.visualizationform.target = popupPointer.name;
	  document.visualizationform.submit();

		//var transaction = YAHOO.util.Connect.asyncRequest('GET', "/visualization/dataDispatcher.do?action=getActivitiesList&id=" + id + "&type=" + type + "&year=" + year, showListPopinCall, null);
}
