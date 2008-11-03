/*
 * JTip
 * By Cody Lindley (http://www.codylindley.com)
 * Under an Attribution, Share Alike License
 * JTip is built on top of the very light weight jquery library.
 
 * Modifications by Rey Bango and Karl Swedberg
 */

// counter for assigning dynamic ids to the tooltip links
var tooltip_id_counter;

//on page load (as soon as it is ready) call JT_init
jQuery(document).ready(JT_init);

function JT_init(){
	// initialize id counter
	tooltip_id_counter = 0;
	
	// 9/21/06 - Rey Bango added hide() method to correct an issue with FF
	jQuery("a.jTip").each(function() {
	  jQuery(this).wrap('<span style="position:relative;"></span>')
		.attr('id', 'tooltip_' + tooltip_id_counter)
	  .hover(function() {
	    JT_show(this.href,this.id,this.name)
	  },function() {
	    jQuery('#JT, #JT_arrow_left, #JT_arrow_right').hide().remove();
	  })
    .click(function(){return false});	 

  	tooltip_id_counter++;
	});
}

function JT_show(url,linkId,title){
	if(title == false)title="&nbsp;";
	var de = document.documentElement;
	var w = self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth;
	var hasArea = w - getAbsoluteLeft(linkId);
	var clickElementy = getAbsoluteTop(linkId) - 3; //set y position
	
	var queryString = url.replace(/^[^\?]+\??/,'');
	var params = parseQuery( queryString );
	if(params['width'] === undefined){params['width'] = 250};
	if(params['link'] !== undefined){
  	jQuery('#' + linkId).bind('click',function(){window.location = params['link']});
  	jQuery('#' + linkId).css('cursor','pointer');
	}
	
	if(hasArea>((params['width']*1)+75)){
		jQuery("body").append("<div id='JT' style='width:"+params['width']*1+"px'><div id='JT_close_left'>"+title+"</div><div id='JT_copy'><div class='JT_loader'><div></div></div>");//right side
		jQuery('body').append('<div id="JT_arrow_left"></div>'); 
		var arrowOffset = getElementWidth(linkId) + 11;
		var clickElementx = getAbsoluteLeft(linkId) + arrowOffset; //set x position
    jQuery('#JT_arrow_left').css({left: (clickElementx - 10) + "px", top: clickElementy +"px"});		
	}else{
		jQuery("body").append("<div id='JT' style='width:"+params['width']*1+"px'><div id='JT_close_right'>"+title+"</div><div id='JT_copy'><div class='JT_loader'><div></div></div>");//left side
		jQuery('body').append('<div id="JT_arrow_right"></div>');
		var clickElementx = getAbsoluteLeft(linkId) - ((params['width']*1) + 20); //set x position
	  jQuery('#JT_arrow_right').css({left: (getAbsoluteLeft(linkId) - 20) + "px", top: clickElementy + "px"});		
	}
	if (jQuery.browser.msie) { 
		jQuery('#JT').prepend('<iframe id="jTipiFrame"></iframe>'); // iframe for IE select box z-index issue
	  jQuery('#jTipiFrame').width((params['width']*1) + "px");	
	}	
	jQuery('#JT').css({left: clickElementx+"px", top: clickElementy +"px"});
   
	jQuery('#JT_copy').load(url, function() {
	  //if jtip goes to left side and is partially cut off at left of doc...	  
	  if (jQuery('#JT_arrow_right') && clickElementx < 0) {
	    var JT_width = (getAbsoluteLeft(linkId) - 22);
	    jQuery('#JT').css({left: 2, width: JT_width}); //adjust width to fit
	  }
	  //get the height of the jtip after loading it
	  var jtip_height = jQuery('#JT').height();
	  //adjust the top of jTip
	  move_jtip();
	  if ( (scroll_position + window_height) - clickElementy < jtip_height ) {
	    var adjusted_top = (window_height - jtip_height) - 6 + scroll_position;
      if ( adjusted_top - scroll_position < 0 ) {
        jQuery('#JT').css({top: scroll_position + 1});
      } else {
        jQuery('#JT').css({top: adjusted_top});
      }
    }      
	}); // end .load()
	jQuery('#JT').show();
} // end JT_show()

function getElementWidth(objectId) {
	x = document.getElementById(objectId);
	return x.offsetWidth;
}

function getAbsoluteLeft(objectId) {
	// Get an object left position from the upper left viewport corner
	o = document.getElementById(objectId);
	oLeft = o.offsetLeft;            // Get left position from the parent object
	while(o.offsetParent!=null) {   // Parse the parent hierarchy up to the document element
		oParent = o.offsetParent;    // Get parent object reference
		oLeft += oParent.offsetLeft; // Add parent left position
		o = oParent;
	}
	return oLeft;
}

function getAbsoluteTop(objectId) {
	// Get an object top position from the upper left viewport corner
	o = document.getElementById(objectId);
	oTop = 0;
	if(o.offsetParent) {
	  o = o.offsetParent;
	}
	while(o) { // Parse the parent hierarchy up to the document element
		oTop += o.offsetTop; // Add parent top position
		o = o.offsetParent;
	}
	return oTop;
}

function parseQuery ( query ) {
   var Params = new Object ();
   if ( ! query ) return Params; // return empty object
   var Pairs = query.split(/[;&]/);
   for ( var i = 0; i < Pairs.length; i++ ) {
      var KeyVal = Pairs[i].split('=');
      if ( ! KeyVal || KeyVal.length != 2 ) continue;
      var key = unescape( KeyVal[0] );
      var val = unescape( KeyVal[1] );
      val = val.replace(/\+/g, ' ');
      Params[key] = val;
   }
   return Params;
}
function move_jtip() {
  if (window.innerHeight) {
	  scroll_position = window.pageYOffset;
	  window_height = window.innerHeight;
	}
	else if (document.documentElement && document.documentElement.scrollTop) {
		scroll_position = document.documentElement.scrollTop;
    window_height = document.documentElement.clientHeight;
	}
	else if (document.body) {
	  scroll_position = document.body.scrollTop;
	  window_height = document.body.clientHeight;
	}
}

function blockEvents(evt) {
  if(evt.target){
    evt.preventDefault();
  }else{
    evt.returnValue = false;
  }
}