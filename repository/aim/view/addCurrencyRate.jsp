<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>
<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<%@page import="org.digijava.module.aim.util.CurrencyUtil"%>


<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%><script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>


<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>
<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<div id="myContent" style="display: none">
	<div id="myContentContent" class="content">
	</div>
</div>

<c:set var="baseCurrencyGS" value="<%= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY) %>" target="request" />

<script type="text/javascript">
<!--

		YAHOOAmp.namespace("YAHOOAmp.amp");

		var myPanel = new YAHOOAmp.widget.Panel("newmyCurrencyRate", {
			width:"600px",
			fixedcenter: true,
		    constraintoviewport: false,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true,
		    context: ["showbtn", "tl", "bl"]
		    });
	var panelStart=0;
	var checkAndClose=false;
	function initCurrencyScripts() {
		var msg='\n<digi:trn key="aim:addCurrencyRate">Add Currency Rate</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
			if(calendarObjForForm.isVisible()){
				calendarObjForForm.hide();
			}
		}); 
		
		myPanel.render(document.body);
	}
	//this is called from editActivityMenu.jsp
	addLoadEvent(initCurrencyScripts);
-->	
</script>
<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#myStep2 .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 

	
</style>



<script language="JavaScript">
    <!--
   
    //DO NOT REMOVE THIS FUNCTION --- AGAIN!!!!
    function mapCallBack(status, statusText, responseText, responseXML){
       window.location.reload();
    }
    
    
    var responseSuccess = function(o){
	/* Please see the Success Case section for more
	 * details on the response object's properties.
	 * o.tId
	 * o.status
	 * o.statusText
	 * o.getResponseHeader[ ]
	 * o.getAllResponseHeaders
	 * o.responseText
	 * o.responseXML
	 * o.argument
	 */
		var response = o.responseText; 
		var content = document.getElementById("myContentContent");
	    //response = response.split("<!")[0];
		content.innerHTML = response;
	    //content.style.visibility = "visible";
		
		showContent();
	}
 
	var responseFailure = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
		//alert("Connection Failure!"); 
	}  
	var callback = 
	{ 
		success:responseSuccess, 
		failure:responseFailure 
	};

	function showContent(){
		var element = document.getElementById("myContent");
		element.style.display = "inline";
		if (panelStart < 1){
			myPanel.setBody(element);
		}
		if (panelStart < 2){
			document.getElementById("myContent").scrollTop=0;
			myPanel.show();
			panelStart = 2;
		}
		checkErrorAndClose();
	}
    function checkErrorAndClose(){
    	if(checkAndClose==true){
    		if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
    			//close the pop in
    			myclose();
    			//refresh parent page
    			refreshPage();
    		}
    		checkAndClose=false;			
    	}
        }

	function generateFields(type){
		var ret="";
		if(type==1){
			ret=
			"updateCRateId="   	+(document.getElementsByName("updateCRateId")[0].value==""?"\"\"":document.getElementsByName("updateCRateId")[0].value)+"&"+
			"doAction="		    +(document.getElementsByName("doAction")[0].value==""?"\"\"":document.getElementsByName("doAction")[0].value)+"&"+
			"ratesFile="		+(document.getElementsByName("ratesFile")[0].value==""?"\"\"":document.getElementsByName("ratesFile")[0].value)+"&"+
			"currUrl="          +(document.getElementsByName("currUrl")[0].value==""?"\"\"":document.getElementsByName("currUrl")[0].value);
		}
		else if (type==2){
			ret=
			"doAction="		    +document.getElementsByName("doAction")[0].value+"&"+
			"updateCRateCode="  +document.getElementsByName("updateCRateCode")[0].value+"&"+
			"updateCRateAmount="+document.getElementsByName("updateCRateAmount")[0].value+"&"+
			"updateCRateDate="  +document.getElementsByName("updateCRateDate")[0].value;
		}
		return ret;
	}
	function myclose(){
		var content = document.getElementById("myContentContent");
		content.innerHTML="";
		myPanel.hide();	
		panelStart=1;
	}

	function myAddExchangeRate(){
		showPanelLoading();
		var postString		= "reset=true&"+generateFields(1);
		<digi:context name="addExchangeRate" property="context/module/moduleinstance/showAddExchangeRates.do" />
		var url = "<%=addExchangeRate %>?"+postString;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}
	function myEditExchangeRate(date,code){
		showPanelLoading();
		var postString="doAction=showRates&updateCRateCode="+code+"&updateCRateDate="+date+"&reset=false";
		<digi:context name="addExchangeRate" property="context/module/moduleinstance/showAddExchangeRates.do" />
		var url = "<%=addExchangeRate %>?"+postString;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}
	function showPanelLoading(){
		  var content = document.getElementById("myContentContent");
			content.innerHTML = '<div style="text-align: center">' + 
			'<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif" border="0" height="17px"/>&nbsp;&nbsp;' + 
			'<digi:trn>Loading, please wait ...</digi:trn><br/><br/></div>';
		  showContent();
	}

	-->

</script>
<script language="JavaScript">

function validate() {
	if (isEmpty(document.aimCurrencyRateFormPop.updateCRateCode.value) == true) {
		alert('<digi:trn key="aim:currencyCodenotEntered">Currency code not entered</digi:trn>');
		document.aimCurrencyRateFormPop.updateCRateCode.focus();
		return false;
	}
	if (document.aimCurrencyRateFormPop.updateCRateCode.value == '${baseCurrencyGS}') {
		alert('<digi:trn>All exchange rates are saved in terms of the base currency. Please select a different currency.</digi:trn>');
		document.aimCurrencyRateFormPop.updateCRateCode.focus();
		return false;
	}

	if (isEmpty(document.aimCurrencyRateFormPop.updateCRateDate.value) == true) {
		alert('<digi:trn key="aim:exchangeRateDateNotEntered">Exchange rate date not entered</digi:trn>');
		document.aimCurrencyRateFormPop.updateCRateDate.focus();
		return false;
	}
	if (isEmpty(document.aimCurrencyRateFormPop.updateCRateAmount.value) == true) {
		alert('<digi:trn key="aim:exchangeRateNotEntered">Exchange rate not entered</digi:trn>');
		document.aimCurrencyRateFormPop.updateCRateAmount.focus();
		return false;
	}
	
	if (checkAmountUsingSymbols(document.aimCurrencyRateFormPop.updateCRateAmount.value,'<%=FormatHelper.getGroupSymbol()%>','<%=FormatHelper.getDecimalSymbol()%>') == false) 
		{
			alert('<digi:trn key="aim:invalidExchangeRateEntered">Invalid exchange rate entered</digi:trn>');
			document.aimCurrencyRateFormPop.updateCRateAmount.focus();
			return false;
		}
	
	return true;
}

function saveRate() {
	var valid = validate();

	var callbackImpl	= {
		success: function () {
			myclose();
			reload();
		},
		failure: function() {
			alert ("<digi:trn>There was a connection error. Please try again.</digi:trn>");
		}
	};
	
	if (valid == true) {
		var postString		= generateFields(2);
		<digi:context name="addExchangeRate" property="context/module/moduleinstance/saveCurrencyRate.do" />
		var url = "<%=addExchangeRate %>";
		checkAndClose=true;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callbackImpl, postString);
	}
	return valid;
}
function refreshPage() {
	document.aimCurrencyRateForm.submit();
}

function load() {
	if(document.getElementsByName('updateCRateCode')[0]!=null)
		document.aimCurrencyRateFormPop.updateCRateCode.focus();
}

function closePopup() {
	myclose();
}

</script>
