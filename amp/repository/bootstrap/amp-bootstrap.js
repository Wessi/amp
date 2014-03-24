/**
 * AMP-bootstrap scripts
 * @author Dolghier Constantin
 */

$.fn.disable = function() { // disable a div in its entirety
	$(this).addClass("disabled-div");
	$(this).find("input,textarea,button,a").addClass("disabled-by-me").attr("disabled", "disabled");
    return true;
};

$.fn.enable = function() { // redo the actions of "disable" from above
	$(this).removeClass("disabled-div");
	$(this).find("input.disabled-by-me, textarea.disabled-by-me, button.disabled-by-me, a.disabled-by-me").removeAttr("disabled").removeClass("disabled-by-me");
	return true;
};

if(!Array.prototype.last) {
    Array.prototype.last = function() {
        return this[this.length - 1];
    };
}

if (typeof String.prototype.trim != 'function') {
	  String.prototype.trim = function (){
	    return this.replace(/^\s+|\s+$/gm,'');
	  };
	}

function fix_aim_button(elem)
{
	elem.addClass("input-sm").css("float", "right");
}

//$(document).on('change', '.aim-button-to-fix input', function() // change the "Implementation Level" select
//{
//	fix_aim_button($(this));
//});

$(document).ready(function()
{
	$('.aim-button-to-fix input').each(function(){fix_aim_button($(this));});
	$('.auto-placeholder input[id]').each(function() // automatically set the "placeholder" attribute of inputs contained in this
			{
				var id = $(this).attr('id');
				var label = $('label[for="' + id + '"]');
				$(this).attr('placeholder', label.html()); //copy the insides of the label to the "placeholder" attribute of the input
			});
	$('.fields_group_title').click(function()
	{
		$(this).siblings('.fields_group_contents').slideToggle();
		return false;
	});
});


function looksLikeEmail(email) {
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (!filter.test(email)) {
		return false;
	}
	return true;
}

function looksLikePhoneNumber(input)
{
	var nr = input.replace('+', '').replace('-', '').replace(' ', '');
	var isnum = /^\d*$/.test(nr);
	return isnum && nr.length < 20;
}

function looksLikeNumber(nr){
	if (nr.length == 0)
		return true;
	
	if (isNaN(nr))
		return false;
	
	var number = parseInt(nr);
	return number > 0;
}

function isYearValidator(input){
	var nr = input;
	return looksLikeNumber(nr) && (parseInt(nr) > 1900) && (parseInt(nr) < 2100);
}


/**
 * should be called on every new ajax addition, or all functionality dependent on $(document).ready() will not work
 * TODO: maybe change everything to $(document).on(...);
 */
function init_custom_looks(divId)
{
	//alert('called');
	$(divId + ' select').addClass('text-left');
	$(divId + ' select').selectpicker({
		style: 'btn-primary btn-xs',
		'data-style': 'btn-primary btn-xs',
		//size: 5
	});
	$(divId + ' select.live-search').attr('data-live-search', 'true'); // Struts is stupid and does not allow to inject custom attributes 
}

function init_amp_magic(divIdInput){
	var divId = divIdInput;
	$(document).ready(function(){
		if (divId.charAt(0) != '#')
			divId = '#' + divId;
		init_custom_looks(divId);
		init_validation(divId);
	});
}
