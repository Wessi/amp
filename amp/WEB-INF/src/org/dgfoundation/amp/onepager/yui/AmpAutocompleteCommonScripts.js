YAHOO.widget.WicketDataSource = function(callbackUrl) {
    this.callbackUrl = callbackUrl;
    this.responseArray = [];
    this.transactionId = 0;
    this.queryMatchContains = true;

};

YAHOO.widget.WicketDataSource.prototype = new YAHOO.util.LocalDataSource();

YAHOO.widget.WicketDataSource.prototype.makeConnection = function(oRequest, oCallback, oCaller) {
    var tId = this.transactionId++;
    this.fireEvent("requestEvent", {tId: tId, request: oRequest, callback: oCallback, caller: oCaller});
    var _this = this;
    var onWicketSuccessFn = function() {
        _this.handleResponse(oRequest, _this.responseArray, oCallback, oCaller, tId);
    };    
    wicketAjaxGet(this.callbackUrl + '&q=' + oRequest, onWicketSuccessFn);
};

function ac_preg_quote( str ) {
    //http://kevin.vanzonneveld.net - replace all regex chars with escaped versions
    return (str+'').replace(/([\\\.\+\*\?\[\^\]\$\(\)\{\}\=\!\<\>\|\:])/g, "\\$1");
};

//left padding a string + colorize siblings
function ac_left_padding(str,level) {
	var color=222+(222*level);
   return (level>0?"<span style='color:#"+color+"'>":"")+Array(level*3).join("&nbsp;")+str+(level>0?"</span>":"");
};

//show loading icon while list is loading
function ac_show_loading(indicatorId,inputId,toggleButtonId) {
	var mySpan = YAHOO.util.Dom.get(indicatorId);
	mySpan.style.display = 'block';
	YAHOO.util.Dom.get(inputId).disabled=true;
	YAHOO.util.Dom.get(toggleButtonId).disabled=true;
	mySpan.innerHTML = '<span id="'+indicatorId+'"><img src="/TEMPLATE/ampTemplate/js_2/yui/carousel/assets/skins/sam/ajax-loader.gif" align="left" /></span>';	
};
