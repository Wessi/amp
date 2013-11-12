enableComputateVisibleSections = false;
onepagerMode = ${onepagerMode};

function isScrolledIntoView(docViewTop, docViewBottom, elem){
    var elemTop = $(elem).offset().top;
    var elemBottom = elemTop + $(elem).height();

    return (((elemBottom >= docViewTop) && (elemBottom <= docViewBottom)) 
      || ((elemTop <= docViewBottom) && (elemTop >= docViewTop)) 
      || ((elemTop <= docViewTop) && (elemBottom >= docViewBottom)));
}


function computateVisibleSections(){
	var docViewTop = $(window).scrollTop();
    var docViewBottom = docViewTop + $(window).height();

	if (enableComputateVisibleSections){
		$('#qListItems').find('li').removeClass('quickListHighlight');
		$('span[name=section]').each(function (){
			if (isScrolledIntoView(docViewTop, docViewBottom, this))
				$('#qItem'+$(this).find('a:first').attr('id')).parent().parent().addClass('quickListHighlight');
		});
	}
}

function adjustQuickLinks(){
	var mainContentTop = $('#mainContent').offset().top - 23;
	var mainContentLeft = $('#mainContent').offset().left;
	var currentScrollLeft = $(window).scrollLeft();
	mainContentLeft = mainContentLeft + 800 - currentScrollLeft;
	var currentScrollTop = mainContentTop - $(window).scrollTop();
	if (currentScrollTop < 0)
		currentScrollTop = 2;
	if ( $('#footer').offset() !=null &&  $('#footer').offset().top < $('#rightMenu').offset().top + $('#rightMenu').height())
		currentScrollTop = $('#footer').offset().top - $('#rightMenu').height() - $(window).scrollTop();
	
	$('#rightMenu').css('top', currentScrollTop + "px");
	$('#rightMenu').css('left', mainContentLeft + "px");
	if (onepagerMode)
		computateVisibleSections();
}

function highlightQItem(currentItem){
	$('#qListItems').find('li').removeClass('quickListHighlight');
	$(currentItem).parent().parent().addClass('quickListHighlight');
}

function showSection(itemId){
	if (onepagerMode){
		$('#' + itemId).parent().parent().siblings('div:first').show();
		$('html, body').animate({scrollTop: $('#' + itemId).offset().top}, 1200);
	}
	else{
		window.scrollTo(0,0);
		$('span[name=section]').hide();
		$('#'+itemId).parents('span[name=section]').show();
	}
	highlightQItem($("#qItem"+itemId));
}

function switchOnepagerMode(){
	if (onepagerMode){
		onepagerMode = false;
		$('span[name=section]').hide();
		$('span[name=section]').eq(0).show();
		$('#imgGroupMode').hide();
		$('#imgOnepagerMode').show();
	}
	else{
		onepagerMode = true;
		$('span[name=section]').show();
		$('#imgOnepagerMode').hide();
		$('#imgGroupMode').show();
	}
	highlightQItem($("#qListItems").find('a:first'));
}

function translationsEnable(){
	$("#switchTranslationMode").attr('href', 'javascript:wicketSwitchTranslationMode()');
	$("#switchFMMode").css("display", "block");
}
function subSectionsSliderEnable(){
	$("a.slider").click(function(){
		$(this).siblings("div:first").slideToggle();
		return false;
	});
}
function rightMenuEnable(){
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

	var mainContentTop = $('#mainContent').offset().top - 23;
	var mainContentLeft = $('#mainContent').offset().left + 800;
	$('#rightMenu').css('top', mainContentTop + "px");
	$('#rightMenu').css('left', mainContentLeft + "px");

	//adjustQuickLinks();
	$('#rightMenu').css('display', 'block');
	enableComputateVisibleSections = true;
	if (onepagerMode)
		computateVisibleSections();
	else
		highlightQItem($("#qListItems").find('a:first'));
}
function pageLeaveConfirmationEnabler(){
	window.onbeforeunload = function (e) {
		  e = e || window.event;
		  
		  // For IE and Firefox prior to version 4
		  if (e) {
			  if (navigator.appName == 'Microsoft Internet Explorer') {
				  return 'Are you sure you want to navigate away from this page?';
			  }else{
				  e.returnValue = ' ';
			  }
		  }
	}
	oldAjaxCallProcessAjaxResponse = Wicket.Ajax.Call.prototype.processAjaxResponse;
	Wicket.Ajax.Call.prototype.processAjaxResponse = function (data, textStatus, jqXHR, context){
		if (jqXHR != null && jqXHR.readyState == 4){
			var tmp = jqXHR.getResponseHeader('Ajax-Location');
			if (typeof(tmp) != "undefined" && tmp != null){
				window.onbeforeunload=null;
			}
		}
		return oldAjaxCallProcessAjaxResponse.call(this, data, textStatus, jqXHR, context);
	}
}

$(document).ready(function(){
	translationsEnable();
	subSectionsSliderEnable();
	rightMenuEnable();
	pageLeaveConfirmationEnabler();
});

$(window).resize(function() {
	adjustQuickLinks();
});

$(window).scroll(function() {
	adjustQuickLinks();
});