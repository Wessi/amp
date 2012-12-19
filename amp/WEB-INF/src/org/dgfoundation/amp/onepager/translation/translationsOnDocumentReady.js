/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */

/**
 * @author aartimon@dginternational.org
 * since Oct 4, 2010
 */

$(document).ready(function(){
	$("#switchTranslationMode").attr('href', 'javascript:wicketSwitchTranslationMode()');
	$("#switchFMMode").css("display", "block");
	$('#wicketRightMenu').replaceWith($('#rightMenu'));
	
	$('#imgGroupMode').attr("title", $("#imgGroupModeTitle").html());
	$('#imgOnepagerMode').attr("title", $("#imgOnepagerModeTitle").html());
	if (typeof(onepagerMode) !== 'undefined') {
		if(onepagerMode){
			$('#imgGroupMode').show();
		}
		else{
			$('#imgOnepagerMode').show();
		}
	}

	myOldEventOnBeforeUnload = window.onbeforeunload; 
	window.onbeforeunload = function (e) {
	  e = e || window.event;
	  e.returnValue = ' ';
	};

	
	//Listener to remove the leave page confirmation on errors
	oldAjaxRequestCallback = Wicket.Ajax.Request.prototype.stateChangeCallback;
	Wicket.Ajax.Request.prototype.stateChangeCallback = function(){
	    var t = this.transport;
	    try{
	    	if (t != null && t.readyState != 1) {
	       		var tmp = t.getResponseHeader("Ajax-Location"); 
	       		if (typeof(tmp) != "undefined" && tmp != null && tmp != "" ){
	          		window.onbeforeunload=null;
	       		}
	    	}
	    }catch(ignore){};
	    
	    return oldAjaxRequestCallback.call(this);    
	};

});




//////////////////////////////////////////////////////////////
//
// functions for button label
//
//////////////////////////////////////////////////////////////


