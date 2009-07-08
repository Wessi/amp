<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"%>

<link rel="stylesheet"
	href="<digi:file src="module/aim/css/newamp.css"/>" />

<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/scrollableTable.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/util.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/relatedLinks.js"/>"></script>

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"
	style="visibility: hidden; position: absolute; z-index: 1000; top: -100;"></DIV>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<!-- script for tree-like view (drilldown reports) -->
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>

<!-- dynamic drive ajax tabs -->
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.js"/>"></script>

<script type="text/javascript"
	src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>
<script type="text/javascript"
	src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>

<!-- dynamic tooltip -->
<script type="text/javascript"
	src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
<script type="text/javascript"
	src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicTooltip.js"/>"></script>

<link rel="stylesheet"
	href="/repository/aim/view/css/css_dhtmlsuite/modal-message.css" />
<link rel="stylesheet" type="text/css"
	href="<digi:file src='module/aim/css/filters.css'/>">

<script type="text/javascript">
	messageObj = new DHTMLSuite.modalMessage(); // We only create one object of this class
	messageObj.setWaitMessage('Loading message - please wait....');
	messageObj.setShadowOffset(5); // Large shadow

	DHTMLSuite.commonObj.setCssCacheStatus(false);

	function displayMessage(url) {
		messageObj.setSource(url);
		messageObj.setCssClassMessageBox(false);
		messageObj.setSize(400, 200);
		messageObj.setShadowDivVisible(true); // Enable shadow for these boxes
		messageObj.display();
	}

	function displayStaticMessage(messageContent, cssClass, width, height) {
		messageObj.setHtmlContent(messageContent);
		messageObj.setSize(width, height);
		messageObj.setCssClassMessageBox(cssClass);
		messageObj.setSource(false); // no html source since we want to use a static message here.
		messageObj.setShadowDivVisible(true); // Disable shadow for these boxes
		messageObj.display();
	}

	function closeMessage() {
		alert('closeMessage');
		messageObj.close();
	}
</script>


<!-- virtual pagination -->
<script type="text/javascript"
	src="<digi:file src="module/aim/scripts/virtualpaginate.js"/>">
	/***********************************************
	 * Virtual Pagination script- © Dynamic Drive DHTML code library (www.dynamicdrive.com)
	 * This notice MUST stay intact for legal use
	 * Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
	 ***********************************************/
</script>

<style type="text/css">
/*Sample CSS used for the Virtual Pagination Demos. Modify/ remove as desired*/
.virtualpage,.virtualpage2,.virtualpage3 {
	/*hide the broken up pieces of contents until script is called. Remove if desired*/
	display: none;
}

.paginationstyle { /*Style for demo pagination divs*/
	width: 250px;
	text-align: center;
	padding: 2px 0;
	margin: 10px 0;
}

.paginationstyle select {
	/*Style for demo pagination divs' select menu*/
	border: 1px solid navy;
	margin: 0 15px;
}

.paginationstyle a { /*Pagination links style*/
	padding: 0 5px;
	text-decoration: none;
	border: 1px solid black;
	color: navy;
	background-color: white;
}

.paginationstyle a:hover,.paginationstyle a.selected {
	color: #000;
	background-color: #FEE496;
}

.paginationstyle a.imglinks {
	/*Pagination Image links style (class="imglinks") */
	border: 0;
	padding: 0;
}

.paginationstyle a.imglinks img {
	vertical-align: bottom;
	border: 0;
}

.paginationstyle a.imglinks a:hover {
	background: none;
}

.paginationstyle .flatview a:hover,.paginationstyle .flatview a.selected
	{ /*Pagination div "flatview" links style*/
	color: #000;
	background-color: yellow;
}
</style>

<script type="text/javascript"
	src="<digi:file src="script/yui/tabview-min.js"/>"></script>
<digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />

<script language="JavaScript" type="text/javascript"
	src="<digi:file src='script/tooltip/wz_tooltip.js'/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src='module/aim/scripts/filters/filters.js'/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src='module/aim/scripts/saveReports.js'/>"></script>

<!-- END - For DHTML Tab View of Filters -->


<script type="text/javascript">
	SaveReportEngine.connectionErrorMessage = '<digi:trn key="aim:reportwizard:connectionProblems">Apparently there are some connection problems. Please try again in a few moments.</digi:trn>';
	SaveReportEngine.savingMessage = '<digi:trn key="aim:reportwizard:savingData">Saving data. Please wait.</digi:trn>';
	saveReportEngine = null;
</script>

<script type="text/javascript">
	YAHOOAmp.namespace("YAHOOAmp.amptab");
	YAHOOAmp.amptab.init = function() {
		var tabView = new YAHOOAmp.widget.TabView('tabview_container');
	};

	/*YAHOOAmp.amptab.handleCloseAbout = function() {
		if (navigator.appName == 'Microsoft Internet Explorer') {
			//window.location.reload();
			//history.go(-1);
		}
	}*/

	YAHOOAmp.amptab.handleClose = function() {
		//alert('handleClose()');
		//var wrapper = document.getElementById('myFilterWrapper');
		var wrapper = document;
		var filter = document.getElementById('myFilter');
		if (filter.parent != null)
			filter.parent.removeChild(filter);
		wrapper.appendChild(filter);
	};

	var myPanel1 = new YAHOOAmp.widget.Panel("new", {
		width :"700px",
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"none",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	var myPanel2 = new YAHOOAmp.widget.Panel("new2", {
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"shadow",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	var myPanel3 = new YAHOOAmp.widget.Panel("new3", {
		width :"300px",
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"none",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	var myPanel4 = new YAHOOAmp.widget.Panel("new3", {
		width :"450px",
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"none",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	var myPanel5 = new YAHOOAmp.widget.Panel("new5", {
		width :"480px",
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"none",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	myPanel1.beforeHideEvent.subscribe(YAHOOAmp.amptab.handleClose);
	//myPanel5.beforeHideEvent.subscribe(YAHOOAmp.amptab.handleCloseAbout);

	function initScripts() {
		//alert('initScripts');

		var msg = '\n<digi:trn key="rep:filter:filters">Filters</digi:trn>';
		myPanel1.setHeader(msg);
		myPanel1.setBody("<p>initScripts</p>");
		myPanel1.render(document.body);
	}

	function submitFilters() {
		//alert('submitfilters');
        var filterForm = document.getElementsByName("parisIndicatorForm")[0];
        filterForm.selectedStartYear.value = document.getElementsByName("selectedStartYear")[0].value;
        filterForm.selectedEndYear.value = document.getElementsByName("selectedEndYear")[0].value;
        filterForm.selectedCalendar.value = document.getElementsByName("selectedCalendar")[0].value;
        filterForm.selectedCurrency.value = document.getElementsByName("selectedCurrency")[0].value;
        
        //Donors
        var txtSelectedValuesObj = filterForm.selectedDonors;
        var selectedArray = new Array();
        var selObj = document.getElementsByName("selectedDonors")[0];
        var i;
        var count = 0;
        for (i=0; i<selObj.options.length; i++) {
            if (selObj.options[i].selected) {
            	  selectedArray[count] = selObj.options[i].value;
            	  count++;
            }
        }
        txtSelectedValuesObj.value = selectedArray;

        //groups
        var txtSelectedValuesObj = filterForm.selectedDonorGroups;
        var selectedArray = new Array();
        var selObj = document.getElementsByName("selectedDonorGroups")[0];
        var i;
        var count = 0;
        for (i=0; i<selObj.options.length; i++) {
            if (selObj.options[i].selected) {
                  selectedArray[count] = selObj.options[i].value;
                  count++;
            }
        }
        txtSelectedValuesObj.value = selectedArray;

        //status
        var txtSelectedValuesObj = filterForm.selectedStatuses;
        var selectedArray = new Array();
        var selObj = document.getElementsByName("selectedStatuses")[0];
        var i;
        var count = 0;
        for (i=0; i<selObj.options.length; i++) {
            if (selObj.options[i].selected) {
                  selectedArray[count] = selObj.options[i].value;
                  count++;
            }
        }
        txtSelectedValuesObj.value = selectedArray;

        //sectors
        var txtSelectedValuesObj = filterForm.selectedSectors;
        var selectedArray = new Array();
        var selObj = document.getElementsByName("selectedSectors")[0];
        var i;
        var count = 0;
        for (i=0; i<selObj.options.length; i++) {
            if (selObj.options[i].selected) {
                  selectedArray[count] = selObj.options[i].value;
                  count++;
            }
        }
        txtSelectedValuesObj.value = selectedArray;

        //filterForm.selectedFinancingIstruments.value = document.getElementsByName("selectedFinancingIstruments")[0].value;
        filterForm.submit();
	}

	function showFilter() {
		//alert('showFilter');
		YAHOOAmp.amptab.init();
		var element = document.getElementById("myFilter");
		element.style.display = "inline";
		//alert(element.innerHTML);
		myPanel1.setBody(element);
		myPanel1.center();
		myPanel1.show();
	}

	function checkRangeValues() {
		var actualFrom = document.aimReportsFilterPickerForm2.renderStartYear.value;
		var actualTo = document.aimReportsFilterPickerForm2.renderEndYear.value;
		var msg = '\n<digi:trn key="rep:filter:wrongSelecteRange">Default Start Year must be lesser than Default End Year</digi:trn>';
		if (actualFrom > actualTo) {
			alert(msg);
			return false;
		}
		return true;
	}

	function showFormat() {
		initFormatPopup();
		YAHOOAmp.amptab.init();
		var element = document.getElementById("customFormat");
		element.style.display = "inline";
		myPanel4.setBody(element);
		myPanel4.center();
		myPanel4.show();
	}

	function changeRange() {
		var cant = document.aimReportsFilterPickerForm2.countYear.value;
		var actualFrom = document.aimReportsFilterPickerForm2.fromYear.value;
		var actualTo = document.aimReportsFilterPickerForm2.toYear.value;
		var initialYear = document.aimReportsFilterPickerForm2.countYearFrom.value;
		document.aimReportsFilterPickerForm2.fromYear.length = 0;
		document.aimReportsFilterPickerForm2.toYear.length = 0;
		var masterFrom = document.aimReportsFilterPickerForm2.fromYear;
		var masterTo = document.aimReportsFilterPickerForm2.toYear;
		masterFrom.options[0] = new Option("All", "-1", false, true);
		for (i = 1; i <= cant; i++) {
			var year = parseInt(initialYear) + i;
			if (year == actualFrom) {
				masterFrom.options[i] = new Option(year, year, false, true);
			} else {
				masterFrom.options[i] = new Option(year, year, false, false);
			}
		}
		masterTo.options[0] = new Option("All", "-1", false, true);
		for (i = 1; i <= cant; i++) {
			var year = parseInt(initialYear) + i;
			if (year == actualTo) {
				masterTo.options[i] = new Option(year, year, false, true);
			} else {
				masterTo.options[i] = new Option(year, year, false, false);
			}
		}
	}

	function hideFilter() {
		myPanel1.hide();
	}

	function showSorter() {
		if (myPanel2EmptyBody) {
			var element2 = document.getElementById("mySorter");
			element2.style.display = "inline";
			myPanel2.setBody(element2);
			myPanel2EmptyBody = false;
		}
		myPanel2.show();
	}

	function hideSorter() {
		myPanel2.hide();
	}

	function showRange() {
		YAHOOAmp.amptab.init();
		var element = document.getElementById("myRange");
		element.style.display = "inline";

		myPanel3.setBody(element);
		myPanel3.center();
		myPanel3.show();
	}

	function hideRange() {
		myPanel3.hide();
	}

	function checkProjectId(x) {
		var s_len = x.value.length;
		var s_charcode = 0;
		for ( var s_i = 0; s_i < s_len; s_i++) {
			s_charcode = x.value.charCodeAt(s_i);
			if (!((s_charcode >= 48 && s_charcode <= 57))) {
				alert("Only Numeric Values Allowed");
				x.value = '';
				x.focus();
				return false;
			}
		}
		return true;
	}

	function resetFormat() {
		document.aimReportsFilterPickerForm3.action = document.aimReportsFilterPickerForm3.action + '&resetFormat=true';
		alert(document.aimReportsFilterPickerForm3.action);
		document.aimReportsFilterPickerForm3.submit();
	}

	function initFormatPopup() {
		var decimalSymbol = document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
		if (decimalSymbol == "CUSTOM") {
			document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled = false;
		} else {
			document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value = "";
			document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled = true;
		}

		var customDecimalPlaces = document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
		if (customDecimalPlaces == "CUSTOM") {
			document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled = false;
		} else {
			document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value = "";
			document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled = true;
		}

		var customUseGrouping = document.aimReportsFilterPickerForm3.customUseGrouping.checked;
		document.aimReportsFilterPickerForm3.customGroupCharacter.disabled = !customUseGrouping;
		var customGroupCharacter = document.aimReportsFilterPickerForm3.customGroupCharacter.value;
		document.aimReportsFilterPickerForm3.customGroupSize.disabled = !customUseGrouping;
		document.aimReportsFilterPickerForm3.customGroupCharacterTxt.disabled = ((!customUseGrouping) || ("CUSTOM" != customGroupCharacter));

		changeFormat();
	}

	function changeFormat() {
		var decimalSymbol = document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
		decimalSymbol = ("CUSTOM" == decimalSymbol) ? document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value
				: decimalSymbol;

		var customDecimalPlaces = document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
		customDecimalPlaces = ("CUSTOM" == customDecimalPlaces) ? document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value
				: customDecimalPlaces;

		var customUseGrouping = document.aimReportsFilterPickerForm3.customUseGrouping.checked;

		var customGroupCharacter = document.aimReportsFilterPickerForm3.customGroupCharacter.value;
		customGroupCharacter = ("CUSTOM" == customGroupCharacter) ? document.aimReportsFilterPickerForm3.customGroupCharacterTxt.value
				: customGroupCharacter;

		var customGroupSize = document.aimReportsFilterPickerForm3.customGroupSize.value;

		var num = Number(123456789.928);

		var format = new Format(decimalSymbol, customDecimalPlaces,
				customUseGrouping, customGroupCharacter, customGroupSize);
		document.getElementById("number").innerHTML = "<B>"
				+ num.format(format) + "</B>";
		//alert(num.format(format));
		return true;
	}

	function validateFormat() {

		var decimalSymbol = document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
		decimalSymbol = ("CUSTOM" == decimalSymbol) ? document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value
				: decimalSymbol;

		var customDecimalPlaces = document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
		customDecimalPlaces = ("CUSTOM" == customDecimalPlaces) ? document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value
				: customDecimalPlaces;

		var customUseGrouping = document.aimReportsFilterPickerForm3.customUseGrouping.checked;

		var customGroupCharacter = document.aimReportsFilterPickerForm3.customGroupCharacter.value;
		customGroupCharacter = ("CUSTOM" == customGroupCharacter) ? document.aimReportsFilterPickerForm3.customGroupCharacterTxt.value
				: customGroupCharacter;

		var customGroupSize = document.aimReportsFilterPickerForm3.customGroupSize.value;

		if ((decimalSymbol == customGroupCharacter) && (customUseGrouping)) {
			var msg = '<digi:trn key="rep:format:equalsSymbol">Decimal Symbol and group symbol must be diferents</digi:trn>';
			alert(msg);
			return false;
		}
		var validNumbers = "0123456789";

		if (decimalSymbol == "" || customGroupCharacter == "") {
			var msg = '<digi:trn key="rep:format:badSymbolEmpty">Symbols can not be a empty, you can use the space character</digi:trn>';
			alert(msg)
			return false;
		}

		if ((validNumbers.indexOf(decimalSymbol) != -1)
				|| (validNumbers.indexOf(customGroupCharacter) != -1)) {
			var msg = '<digi:trn key="rep:format:badSymbolNumber">Symbols can not be a number</digi:trn>';
			alert(msg);
			return false;
		}

		if (customGroupSize < 1) {
			var msg = '<digi:trn key="rep:format:badGorupSize">The value should be greater than zero</digi:trn>';
			alert(msg);
			return false;
		}

		return true;
	}

	window.onload = initScripts;
	var msg0 = '<digi:trn key="rep:pop:pleasewait..."> Please wait...</digi:trn>';
	var msg1 = '<digi:trn key="rep:pop:freezeReportHeading">Freeze Report Heading</digi:trn>';
	var msg2 = '<digi:trn key="rep:pop:unFreezeReportHeading">Unfreeze Report Heading</digi:trn>';
	var msg3 = '<digi:trn key="rep:pop:freezingReportHeading"> Freezing Report Heading </digi:trn>';

	function addOnloadEvent(fnc) {
		if (typeof window.addEventListener != "undefined")
			window.addEventListener("load", fnc, false);
		else if (typeof window.attachEvent != "undefined") {
			window.attachEvent("onload", fnc);
		} else {
			if (window.onload != null) {
				var oldOnload = window.onload;
				window.onload = function(e) {
					oldOnload(e);
					window[fnc]();
				};
			} else
				window.onload = fnc;
		}
	}

	var scrolling = readCookie('report_scrolling');
	scrolling = (scrolling == null) ? false : (scrolling == "true") ? true
			: false;

	function makeScroll() {
		/*alert('makescroll');
		createCookie('report_scrolling', true, 1);
		showScroll();
		document.getElementById("frezzlink").setAttribute("onClick",
				"hiddeScroll()");
		document.getElementById("frezzlink").setAttribute("class",
				"settingsLink");
		document.getElementById("frezzlink").innerHTML = msg2;*/
	}

	function hiddeScroll() {
		eraseCookie('report_scrolling', true, 1);
		document.location = document.location;
	}

	var enableLink = function() {
		/*alert('nada2');
		if (scrolling) {
			document.getElementById("frezzlink").setAttribute("onClick",
					"hiddeScroll()");
			document.getElementById("frezzlink").setAttribute("class",
					"settingsLink");
			document.getElementById("frezzlink").innerHTML = msg2;
			showScroll();
		} else {
			document.getElementById("frezzlink").setAttribute("onClick",
					"makeScroll()");
			document.getElementById("frezzlink").setAttribute("class",
					"settingsLink");
			document.getElementById("frezzlink").innerHTML = msg1;
		}*/
	}
	addOnloadEvent(enableLink);

	//-----------------------
	function showScroll() {
		var wait = new YAHOOAmp.widget.Panel("wait", {
			width :"240px",
			fixedcenter :true,
			close :false,
			draggable :false,
			zindex :99,
			modal :true,
			visible :false,
			underlay :"shadow"
		});

		wait.setHeader(msg0);
		wait.setBody("<div align='center'>" + msg3 + "</div>");
		wait.render(document.body);
		wait.show();
		var winH;

		if (navigator.appName.indexOf("Microsoft") != -1) {
			winH = document.body.offsetHeight;
		} else {
			winH = window.innerHeight;
		}
		var call = function() {
			var reporTable = new scrollableTable("reportTable", winH - 320);
			reporTable.debug = false;
			reporTable.maxRowDepth = 1;
			reporTable.scroll();
			wait.hide();
		}
		window.setTimeout(call, 200);
	}
</script>


<style type="text/css">
.mask {
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity =   80);
	background-color: #2f2f2f;
}
</style>