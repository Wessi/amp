
function getReportType() {
	var radioEls		= aimReportWizardForm.reportType;
	for (var i=0; i<radioEls.length; i++) {
		if ( radioEls[i].checked ) {
			return radioEls[i].value;
		}
	}
}

function getReportTitle() {
	return aimReportWizardForm.reportTitle.value;
}

function getReportDescription() {
	return aimReportWizardForm.reportDescription.value;
}

function getHideActivities() {
	if ( aimReportWizardForm.hideActivities.checked )
			return true;
	else
			return false;
}

function getReportPeriod() {
	var radioEls		= aimReportWizardForm.reportPeriod;
	for (var i=0; i<radioEls.length; i++) {
		if ( radioEls[i].checked ) {
			return radioEls[i].value;
		}
	}
}

function getDesktopTab() {
	return aimReportWizardForm.desktopTab.value;
}

function getSelectedFields( ulId, name ) {
	var ret			= "";
	var ulEl		= document.getElementById( ulId );
	
	var fields		= ulEl.getElementsByTagName( "input" );
	for ( var i=0; i<fields.length; i++ ) {
		ret			+= name+"="+fields[i].value;
		if ( i < fields.length-1 )
			ret		+= "&";
	}
	return ret;	
}

function SaveReportEngine ( savingMessage, failureMessage ) {
	this.failureMessage	= failureMessage;
	this.savingMessage	= savingMessage;
	this.divEl			=  document.getElementById("savingReportDiv");
	this.titlePanel		= null;
}

SaveReportEngine.prototype.success		= function (o) {
	if ( o.responseText.length > 2 ) {
		this.divEl.innerHTML	= o.responseText;
		if ( o.responseText.indexOf("duplicateName") >= 0 ) {
			aimReportWizardForm.reportTitle.value	= "";
		}
	}
	else
		window.location.replace("/aim/showDesktop.do");
		
}
SaveReportEngine.prototype.failure			= function(o) {
	this.divEl.innerHTML			= this.failureMessage;
}

SaveReportEngine.prototype.decideToShowTitlePanel	= function () {
	if ( aimReportWizardForm.reportTitle.value == "" )
			this.showTitlePanel();
	else
			this.saveReport( aimReportWizardForm );
}

SaveReportEngine.prototype.showTitlePanel	= function () {
	if ( this.titlePanel == null ) {
		document.getElementById("titlePanel").style.display	= "";
		this.titlePanel	= new YAHOO.widget.Panel("titlePanel", 
				{ 	visible:true,
					width: "400px", 
					constraintoviewport:true, 
					fixedcenter: true, 
					underlay:"shadow", 
					modal: true,
					close:true, 
					visible:false, 
					draggable:true } );
		this.titlePanel.render();
	}
	this.titlePanel.show();
	aimReportWizardForm.reportTitle.focus();
}

SaveReportEngine.prototype.saveReport	= function () {
	this.titlePanel.hide();

	this.divEl.style.visibility		= "";
	this.divEl.innerHTML			= this.savingMessage + 
			"... <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/>";
	
	var postString		= "reportTitle="+getReportTitle() + "&reportDescription="+getReportDescription() + "&reportPeriod="+getReportPeriod() + 
						"&reportType="+getReportType() + "&" + getSelectedFields("dest_col_ul", "selectedColumns") + 
						"&desktopTab="+getDesktopTab() +
						"&" + getSelectedFields ("dest_measures_ul","selectedMeasures")+ "&" + getSelectedFields("dest_hierarchies_ul","selectedHierarchies");
	//alert (postString);
	
	YAHOO.util.Connect.asyncRequest("POST", "/aim/reportWizard.do", this, postString);
	
}