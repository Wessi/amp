<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

<digi:instance id="wform" property="gisTableWidgetRenderForm"/>

<div id="tableWidgetContainer_${wform.tableId}">
	
</div>

<script language="JavaScript">

	function requestTable_${wform.tableId}(){
		<digi:context name="tableRendererUrl" property="/widget/getTableWidget.do" />
		var url = '${tableRendererUrl}~tableId=${wform.tableId}';
		var async=new Asynchronous();
		async.complete=tableCallBack_${wform.tableId};
		async.call(url);
	}

	function tableCallBack_${wform.tableId}(status, statusText, responseText, responseXML){
		processTableResponce_${wform.tableId}(responseText);
	}

	function processTableResponce_${wform.tableId}(htmlResponce){
		var myDiv = document.getElementById('tableWidgetContainer_${wform.tableId}');
		myDiv.innerHTML = htmlResponce;;		
	}

	requestTable_${wform.tableId}();
</script>
