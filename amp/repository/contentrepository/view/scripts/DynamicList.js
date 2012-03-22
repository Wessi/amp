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
function FilterWrapper() {
	this.filterLabels	= new Array();
	this.filterDocTypeIds	= new Array(); //key-value arrays
	this.filterFileTypes	= new Array();
	this.filterOwners		= new Array();
	this.filterTeamIds		= new Array();
}
FilterWrapper.prototype.labelsToHTML	= function(text) {
	var ret	= "";
	ret += "<strong>"+text+" :</strong> ";
	if ( this.filterLabels.length > 0) {
		for (var i=0; i<this.filterLabels.length; i++) {
			var l	= this.filterLabels[i];
			ret += "<span style='color:" + l.color + "; background-color:" + l.backgroundColor + "; padding: 1px;'>";
			ret += l.name + "</span> <strong>+</strong> ";
		}
	}
	else
		ret += nonetext;
	
	//ret += "<a style='cursor:pointer; text-decoration:none; color: blue'> Add Label </a>";
	return ret;
}

FilterWrapper.prototype.fToHTML	= function(text) {
	var ret	= '<strong>'+text+' :</strong> ';
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

function AbstractDynamicList (containerEl, thisObjName, fDivId) {
	this.containerEl	= containerEl;
	this.thisObjName	= thisObjName;
	this.fDivId			= fDivId;
	
	this.filterWrapper	= new FilterWrapper();
	
	this.reqString		= "";
	
	this.fPanel				= null;
	
	this.filterInfoDivId	= null;
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
//	alert(this.reqString);
	YAHOO.util.Connect.asyncRequest('POST', '/contentrepository/documentManager.do?ajaxDocumentList=true&dynamicList='+this.thisObjName+
			this.reqString, callbackObj );
	if ( this.fPanel != null)
		this.fPanel.hide();
	
	if ( this.filterInfoDivId != null ) {
		var divEl	= document.getElementById( this.filterInfoDivId );
		divEl.innerHTML = this.filterWrapper.labelsToHTML(getlabelsext()) + "&nbsp;&nbsp;&nbsp;&nbsp;" + this.filterWrapper.fToHTML(getfiltertext());
	}
}

AbstractDynamicList.prototype.retrieveFilterData	= function (divId) {
	var divEl	= document.getElementById(divId);
	var form	= divEl.getElementsByTagName("form")[0];
	for (var field in this.filterWrapper) {
		if ( field.indexOf("filter") == 0 && field!="filterLabels" ) {
			var selectEl	= form.elements[field];
			var optionEl	= selectEl.options[selectEl.selectedIndex];
			
			this.filterWrapper[field]	= new Array();
			this.filterWrapper[field].push( new KeyValue(optionEl.value, optionEl.text) );
		}
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

AbstractDynamicList.prototype.getFilterPanel	= function (buttonId,divId) {
	if ( this.fPanel == null) {
		var divEl		= document.getElementById(divId);
		
		var panel 		= 
			new YAHOO.widget.Panel("FilterPanel"+divId, { width:"400px", visible:true, draggable:true, close:true, modal:false, 
				context:[buttonId,"tl","bl"]} );
		panel.setHeader('Filters');
		panel.setBody(divEl);
		panel.render(document.body);
		
		this.fPanel		= panel;
		
		divEl.style.display	= "";
		var buttonEls	= divEl.getElementsByTagName("button");
		YAHOO.util.Event.on(buttonEls[0],"click", this.sendRequest, this, true);
		YAHOO.util.Event.on(buttonEls[1],"click", this.fPanel.hide, this.fPanel, true);
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

function DynamicList(containerEl, thisObjName,fDivId, teamId, username) {
	
	this.parent.call(this, containerEl, thisObjName, fDivId);
	
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
function SharedDynamicList(containerEl, thisObjName, fDivId) {
	this.parent.call(this, containerEl, thisObjName, fDivId);
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
function PublicDynamicList(containerEl, thisObjName, fDivId) {
	this.parent.call(this, containerEl, thisObjName, fDivId);
}