<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>


<!-- code for rendering that nice calendar -->
<bean:define id="langBean" name="org.digijava.kernel.navigation_language" scope="request" type="org.digijava.kernel.entity.Locale" toScope="page" />
<bean:define id="lang" name="langBean" property="code" scope="page" toScope="page" />

<script type="text/javascript">

	var myCalendarModel = new DHTMLSuite.calendarModel();
	myCalendarModel.setLanguageCode('<bean:write name="lang" />'); 
	var calendarObjForForm = new DHTMLSuite.calendar({callbackFunctionOnDayClick:'getDateFromCalendar',isDragable:false,displayTimeBar:false}); 
	calendarObjForForm.setCalendarModelReference(myCalendarModel);
	
	var dateFormat = '<%=org.digijava.module.aim.util.FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBALSETTINGS_DATEFORMAT) %>';
	if (dateFormat == 'null')
		dateFormat = 'dd/mm/yyyy';
	
	function getDateFromCalendar(inputArray)
	{
		var references = calendarObjForForm.getHtmlElementReferences(); // Get back reference to form field.
		references.myDate.value = inputArray.day + '/' + inputArray.month + '/' + inputArray.year;
		calendarObjForForm.hide();			
	}	

	function pickDate(buttonObj,inputObject)
	{
		var butt = document.getElementById(buttonObj);
		var intY = (document.all?document.body.scrollTop:window.pageYOffset);
		calendarObjForForm.setCalendarPositionByHTMLElement(butt,0,intY+butt.offsetHeight-80);	// Position the calendar right below the form input
		calendarObjForForm.setInitialDateFromInput(inputObject,dateFormat);	// Specify that the calendar should set it's initial date from the value of the input field.
		calendarObjForForm.addHtmlElementReference('myDate',inputObject);	// Adding a reference to this element so that I can pick it up in the getDateFromCalendar below(myInput is a unique key)
		
		if(calendarObjForForm.isVisible()){
			calendarObjForForm.hide();
		}else{
			calendarObjForForm.resetViewDisplayedMonth();	// This line resets the view back to the inital display, i.e. it displays the inital month and not the month it displayed the last time it was open.
			calendarObjForForm.display();
		}		
	}
	
	function pickDateWithClear(buttonObj,inputObject,clearObj) //also sets visibility for clear button after the selection has been done
	{
		var butt = document.getElementById(buttonObj);
		var clr = document.getElementById(clearObj);
		var intY = (document.all?document.body.scrollTop:window.pageYOffset);
		calendarObjForForm.setCalendarPositionByHTMLElement(butt,0,intY+butt.offsetHeight-80);	// Position the calendar right below the form input
		calendarObjForForm.setInitialDateFromInput(inputObject,dateFormat);	// Specify that the calendar should set it's initial date from the value of the input field.
		calendarObjForForm.addHtmlElementReference('myDate',inputObject);	// Adding a reference to this element so that I can pick it up in the getDateFromCalendar below(myInput is a unique key)
		
		if(calendarObjForForm.isVisible()){
			calendarObjForForm.hide();
		}else{
			calendarObjForForm.resetViewDisplayedMonth();	// This line resets the view back to the inital display, i.e. it displays the inital month and not the month it displayed the last time it was open.
			calendarObjForForm.display();
		}		
		
		//set Clear link visible
		clr.style.display="inline";
	}
	
	function clearDisplay(editBox, clearLink){ //Display the clear link or not ?
		var clr = document.getElementById(clearLink);
		if (editBox != undefined){
			if (editBox.value != "")
				clr.style.display="inline";
			else
				clr.style.display="none";
		}
	}
	function clearDate(editBox, clearLink){	//The clear link has been pressed
		var clr = document.getElementById(clearLink);
		editBox.value = "";
		clr.style.display="none";
	}
	
	
	function pickDateObjName(buttonObj,objectName)
	{
		var butt = document.getElementById(buttonObj);
		var inputObjects = document.getElementsByName(objectName);
		var inputObject = inputObjects[0];
		var intY = (document.all?document.body.scrollTop:window.pageYOffset);
		calendarObjForForm.setCalendarPositionByHTMLElement(butt,-160,intY+butt.offsetHeight-80);	// Position the calendar right below the form input
		calendarObjForForm.setInitialDateFromInput(inputObject,dateFormat);	// Specify that the calendar should set it's initial date from the value of the input field.
		calendarObjForForm.addHtmlElementReference('myDate',inputObject);	// Adding a reference to this element so that I can pick it up in the getDateFromCalendar below(myInput is a unique key)
		
		if(calendarObjForForm.isVisible()){
			calendarObjForForm.hide();
		}else{
			calendarObjForForm.resetViewDisplayedMonth();	// This line resets the view back to the inital display, i.e. it displays the inital month and not the month it displayed the last time it was open.
			calendarObjForForm.display();
		}		
	}
	
	function pickDateById(buttonObj,objectId,format)
	{
		if(format==null){
			format=	dateFormat;
		}
		var butt = document.getElementById(buttonObj);
		var inputObject = document.getElementById(objectId);
		var intY = (document.all?document.body.scrollTop:window.pageYOffset);
		calendarObjForForm.setCalendarPositionByHTMLElement(butt,-160,intY+butt.offsetHeight-80);	// Position the calendar right below the form input
		calendarObjForForm.setInitialDateFromInput(inputObject,format);	// Specify that the calendar should set it's initial date from the value of the input field.
		calendarObjForForm.addHtmlElementReference('myDate',inputObject);	// Adding a reference to this element so that I can pick it up in the getDateFromCalendar below(myInput is a unique key)
		
		if(calendarObjForForm.isVisible()){
			calendarObjForForm.hide();
		}else{
			calendarObjForForm.resetViewDisplayedMonth();	// This line resets the view back to the inital display, i.e. it displays the inital month and not the month it displayed the last time it was open.
			calendarObjForForm.display();
		}		
	}
	
	function pickDateCurrency(buttonObj,inputObject)
	{
		var butt = document.getElementById(buttonObj);
		var intY = (document.all?document.body.scrollTop:window.pageYOffset);
		calendarObjForForm.setCalendarPositionByHTMLElement(butt,-160,intY+butt.offsetHeight-40);	// Position the calendar right below the form input
		calendarObjForForm.setInitialDateFromInput(inputObject,dateFormat);	// Specify that the calendar should set it's initial date from the value of the input field.
		calendarObjForForm.addHtmlElementReference('myDate',inputObject);	// Adding a reference to this element so that I can pick it up in the getDateFromCalendar below(myInput is a unique key)
		
		if(calendarObjForForm.isVisible()){
			calendarObjForForm.hide();
		}else{
			calendarObjForForm.resetViewDisplayedMonth();	// This line resets the view back to the inital display, i.e. it displays the inital month and not the month it displayed the last time it was open.
			calendarObjForForm.display();
		}		var dateFormat = "mm/dd/yyyy";
	
	}
	function pickDateByIdDxDy(buttonObj,objectId,dx,dy)
	{
		var butt = document.getElementById(buttonObj);
		var inputObject = document.getElementById(objectId);
		var intY = (document.all?document.body.scrollTop:window.pageYOffset);
		calendarObjForForm.setCalendarPositionByHTMLElement(butt,-dx,intY+butt.offsetHeight-dy);	// Position the calendar right below the form input
		calendarObjForForm.setInitialDateFromInput(inputObject,dateFormat);	// Specify that the calendar should set it's initial date from the value of the input field.
		calendarObjForForm.addHtmlElementReference('myDate',inputObject);	// Adding a reference to this element so that I can pick it up in the getDateFromCalendar below(myInput is a unique key)
		
		if(calendarObjForForm.isVisible()){
			calendarObjForForm.hide();
		}else{
			calendarObjForForm.resetViewDisplayedMonth();	// This line resets the view back to the inital display, i.e. it displays the inital month and not the month it displayed the last time it was open.
			calendarObjForForm.display();
		}		
	}
</script>		
