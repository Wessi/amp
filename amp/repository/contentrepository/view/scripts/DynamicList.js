function RetrieveFilters( dynamicListObj) {
		this.dynamicListObj		= dynamicListObj;
	}
	RetrieveFilters.prototype.success	= function (o) {
		if ( o.responseText.length > 0) {
			var filterWrapper					= eval("(" + o.responseText + ")" );
			var fw								= new FilterWrapper();
			for (var field in filterWrapper) {
				fw[field]	= filterWrapper[field];
			}
			this.dynamicListObj.filterWrapper	= fw;
			this.dynamicListObj.sendRequest(false);
		}
		else
			alert("The returned filter info is empty !");
	}
	RetrieveFilters.prototype.failure	= function (o) {
		alert("We are sorry but your request cannot be processed at this time");
	}

function KeyValue (key, value) {
	this.key	= key;
	this.value	= value;
}
function FilterWrapper( trnObj ) {
	this.filterLabels	= new Array();
	this.filterDocTypeIds	= new Array(); //key-value arrays
	this.filterFileTypes	= new Array();
	this.filterOwners		= new Array();
	this.filterTeamIds		= new Array();
	this.filterKeywords		= new Array();	
	
	this.trnObj				= {
			labels: "Lables",
			filters: "Filters",
			keywords: "Keywords"
	};
	if ( trnObj != null )
		this.trnObj			= trnObj;
	
	
}
FilterWrapper.prototype.labelsToHTML	= function(text) {
	var ret	= "";
	ret += "<span style='font-family:Arial,sans-serif;font-size:11px;'><b>" + this.trnObj.labels + ":</b><span> "; 
	if ( this.filterLabels.length > 0) {
		for (var i=0; i<this.filterLabels.length; i++) {
			var l	= this.filterLabels[i];
			var name=l.name.replace(/</g, "&lt;").replace(/>/g, "&gt;");
			ret += "<span style='color:" + l.color + "; background-color:" + l.backgroundColor + "; padding: 1px;'>";
			ret += name + "</span> <b>+</b> &nbsp;|&nbsp;";
		}
	}
	else
		ret += "<span style='font-family:Arial,sans-serif;font-size:11px;'>"+this.trnObj.none +"<span>";
	
	//ret += "<a style='cursor:pointer; text-decoration:none; color: blue'> Add Label </a>";
	return ret;	
}

FilterWrapper.prototype.fToHTML	= function() {
	var ret	= "<span style='font-family:Arial,sans-serif;font-size:11px;'><b>" + this.trnObj.filters + ":</b><span> ";
	if ( this.filterDocTypeIds.length > 0 ) {
		var docType		= this.filterDocTypeIds[0];
		if ( docType.key != "0" ) {
			ret += "<span>Document Type - ";
			ret += docType.value;
			ret += "</span>, ";
		}
	}
	if ( this.filterFileTypes.length > 0 ) {
		var docType		= this.filterFileTypes[0];
		if ( docType.key != "-1" ) {
			ret += "<span>File Type - ";
			ret += docType.value;
			ret += "</span>, ";
		}
	}
	if ( this.filterOwners.length > 0 ) {
		var docType		= this.filterOwners[0];
		if ( docType.key != "-1" ) {
			ret += "<span>Creator - ";
			ret += docType.value;
			ret += "</span>, ";
		}
	}
	if ( this.filterTeamIds.length > 0 ) {
		var docType		= this.filterTeamIds[0];
		if ( docType.key != "-1" ) {
			ret += "<span>Creator team - ";
			ret += docType.value;
			ret += "</span>, ";
		}
	}
	
	
	
	if ( ret.charAt(ret.length-2)==',' ) {
		ret		= ret.substring(0, ret.length-2);
	}
	return ret;
}

FilterWrapper.prototype.kToHTML	= function() {
	var ret	= "<span style='font-family:Arial,sans-serif;font-size:11px;'><b>" + this.trnObj.keywords + ":</b><span> ";
	if ( this.filterKeywords.length > 0 ) {		
		ret += "<span>";
		for (var i=0;i < this.filterKeywords.length ; i ++){
			ret += this.filterKeywords[i]+" ";
		}
		ret += "</span>, ";		
	}
	if ( ret.charAt(ret.length-2)==',' ) {
		ret		= ret.substring(0, ret.length-2);
	}
	return ret;
}

AbstractDynamicList.prototype.setKeywordTextboxInformation = function (inputId,buttonId){
	this.searchBtn = document.getElementById(buttonId);
	this.searchBox	= document.getElementById(inputId);
	YAHOO.util.Event.on(this.searchBtn,"click", this.sendRequest, this, true);
	YAHOO.util.Event.on(this.searchBox,"keydown", handleKeyPress, this, true);
	
};



function handleKeyPress(arg) {
	
	if(!arg || arg.keyCode != 13)
		return;
	this.sendRequest(true);
}


function AbstractDynamicList (containerEl, thisObjName, fDivId, trnObj) {
	this.containerEl	= containerEl;
	this.thisObjName	= thisObjName;
	this.fDivId			= fDivId;
	
	this.filterWrapper	= new FilterWrapper(trnObj);
	
	this.reqString		= "";
	
	this.fPanel				= null;
	
	this.filterInfoDivId	= null;
	
	this.trnObj				= trnObj;	
}

AbstractDynamicList.prototype.clearBody		= function () {
	this.containerEl.innerHTML	= "";
}

AbstractDynamicList.prototype.sendRequest		= function (shouldRetrieveFilters) {
	this.reqString		= "";
	if ( shouldRetrieveFilters != null && !shouldRetrieveFilters) 
		this.createFilterString(false);
	else 
		this.createFilterString(true);
	this.createReqString();
	
	var callbackObj		= getCallbackForOtherDocuments(this.containerEl, null, this.thisObjName +"DivId");
	//alert(this.reqString);
	YAHOO.util.Connect.asyncRequest('POST', '/contentrepository/documentManager.do?ajaxDocumentList=true&dynamicList='+this.thisObjName+
			this.reqString, callbackObj );
	if ( this.fPanel != null)
		this.fPanel.hide();
	
	if ( this.filterInfoDivId != null ) {
		var divEl	= document.getElementById( this.filterInfoDivId );
		divEl.innerHTML = this.filterWrapper.labelsToHTML(getlabelsext()) + "&nbsp;&nbsp;&nbsp;&nbsp;" + this.filterWrapper.fToHTML(getfiltertext())
		+ "&nbsp;&nbsp;&nbsp;&nbsp;" + this.filterWrapper.kToHTML(getkeywordsext());
	}
};

AbstractDynamicList.prototype.sendResetRequest		= function (shouldRetrieveFilters) {
	this.resetFilterData(shouldRetrieveFilters);
	this.sendRequest(shouldRetrieveFilters);
};

AbstractDynamicList.prototype.resetFilterData		= function (shouldRetrieveFilters) {
	var panel = this.fPanel;
	
	var elems = panel.element.getElementsByTagName('select');
		
		//panel.getBody().getElementByTagName('select');
	
	for(var i=0; i < elems.length; i++)
		{
		var optList = elems[i];
		
		for(var optInd=0; optInd < optList.length; optInd++)
		{
		if(optInd==0)
			{
			optList[optInd].setAttribute("selected","true");
			optList[optInd].selected=true;
			}
			
		else
			{
			optList[optInd].removeAttribute("selected");
			optList[optInd].selected=false;
			}
			
		}
	    
		
		}
	    
	
};

AbstractDynamicList.prototype.retrieveFilterData	= function (divId) {
	var divEl	= document.getElementById(divId);
	var form	= divEl.getElementsByTagName("form")[0];
	for (var field in this.filterWrapper) {
		if ( field.indexOf("filter") == 0 && field!="filterLabels" && field!="filterKeywords") {
			var selectEl	= form.elements[field];
			var optionEl	= selectEl.options[selectEl.selectedIndex];
			
			this.filterWrapper[field]	= new Array();
			this.filterWrapper[field].push( new KeyValue(optionEl.value, optionEl.text) );
		}
	}
	if (this.searchBox != null && this.searchBox.value.length > 0) {
		var typedText = this.searchBox.value.split(" ");
		this.filterWrapper["filterKeywords"] = new Array();
		for (var i =0;i< typedText.length ; i++) {
			this.filterWrapper["filterKeywords"].push( new KeyValue(typedText[i], 'keyword'+i) );
		}
	}else{
		this.filterWrapper["filterKeywords"] = new Array();
		this.filterWrapper["filterKeywords"].push( new KeyValue('', 'keyword'+i) );
	}
}

AbstractDynamicList.prototype.createFilterString	= function (shouldRetrieveFilters) {
	
		if ( shouldRetrieveFilters == null || shouldRetrieveFilters )
			if ( this.fDivId != null )
				this.retrieveFilterData(this.fDivId);
		for (var field in this.filterWrapper) {
			if ( field.indexOf("filter") == 0 && field!="filterLabels" && this.filterWrapper[field] != null && this.filterWrapper[field].length > 0 ) {
				for (var i=0; i<this.filterWrapper[field].length; i++)
					this.reqString	+= "&"+field+"="+this.filterWrapper[field][i].key;
			}
		}
	
	for (var i=0; i<this.filterWrapper.filterLabels.length; i++) {
		this.reqString	+= "&filterLabelsUUID=" + this.filterWrapper.filterLabels[i].uuid ;
	}
}

AbstractDynamicList.prototype.createReqString	= function () {
	return this.reqString;
}

AbstractDynamicList.prototype.getFilterPanel	= function (buttonId,divId,hide) {
	
	if ( this.fPanel == null && hide == false) {
		var divEl		= document.getElementById(divId);
		
		var panel 		= 
			new YAHOO.widget.Panel("FilterPanel"+divId, { width:"400px", 
				visible:true, draggable:true, close:true, 
				modal:false,
				effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
				context:[buttonId,"tl","bl"]} );
		panel.setHeader(this.trnObj.filters);
		panel.setBody(divEl);
		panel.render(document.body);
		
		this.fPanel		= panel;
		
		divEl.style.display	= "";
		var buttonEls	= divEl.getElementsByTagName("button");
		YAHOO.util.Event.on(buttonEls[0],"click", this.sendRequest, this, true);
		YAHOO.util.Event.on(buttonEls[1],"click", this.sendResetRequest, this, true);
		YAHOO.util.Event.on(buttonEls[2],"click", this.fPanel.hide, this.fPanel, true);
	} else if (hide == true) {
//		var isVisible = this.fPanel.get("visible");
//		alert (isVisible);
//		if ( isVisible )
//			this.fPanel.hide();
//		else
			if (this.fPanel != null) this.fPanel.hide();
//		this.fPanel = null;
	} else if (hide == false) {
//		var isVisible = this.fPanel.get("visible");
//		alert (isVisible);
//		if ( isVisible )
//			this.fPanel.hide();
//		else
		if (this.fPanel != null) this.fPanel.show();
//		this.fPanel = null;
	}
	return this.fPanel;
}

AbstractDynamicList.prototype.emptyLabels			= function () {
	this.filterWrapper.filterLabels	= new Array();
}
AbstractDynamicList.prototype.addRemoveLabel			= function ( label ) {
	for (var i=0; i<this.filterWrapper.filterLabels.length; i++) {
		if (this.filterWrapper.filterLabels[i].uuid == label.uuid ) {
			this.filterWrapper.filterLabels.splice(i, 1);
			return;
		}
	}
	this.filterWrapper.filterLabels.push (label);
}

/**
 * DynamicList class
 */
DynamicList.prototype				= new AbstractDynamicList();
DynamicList.prototype.parent		= AbstractDynamicList;
DynamicList.prototype.constructor	= DynamicList;

function DynamicList(containerEl, thisObjName,fDivId, teamId, username, trnObj) {
	
	this.parent.call(this, containerEl, thisObjName, fDivId, trnObj);
	
	this.teamId			= teamId;
	this.username		= username;
	
}

DynamicList.prototype.createReqString	= function () {
	this.reqString 	+= "&otherTeamId="+this.teamId;
	if ( this.username != null ) 
		this.reqString	+= "&otherUsername="+this.username;
	
	return this.reqString;
}

/**
 * SharedDynamicList class
 */
SharedDynamicList.prototype				= new AbstractDynamicList();
SharedDynamicList.prototype.parent		= AbstractDynamicList;
SharedDynamicList.prototype.constructor	= SharedDynamicList;

/**
 * 
 * @param containerEl
 * @returns {SharedDynamicList}
 */
function SharedDynamicList(containerEl, thisObjName, fDivId, trnObj) {
	this.parent.call(this, containerEl, thisObjName, fDivId, trnObj);
}
SharedDynamicList.prototype.createReqString	= function () {
	this.reqString	+= "&showSharedDocs=true";
}

/**
 * PublicDynamicList class
 */
PublicDynamicList.prototype				= new AbstractDynamicList();
PublicDynamicList.prototype.parent		= AbstractDynamicList;
PublicDynamicList.prototype.constructor	= PublicDynamicList;

/**
 * 
 * @param containerEl
 * @returns {PublicDynamicList}
 */
function PublicDynamicList(containerEl, thisObjName, fDivId, trnObj) {
	this.parent.call(this, containerEl, thisObjName, fDivId, trnObj);
}
