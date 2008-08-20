<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:instance property="gisWidgetTeaserForm" />

<c:if test="${gisWidgetTeaserForm.rendertype==4}">
	<img src="/widget/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}">		
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==3}">

	<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
	
	<div id="tableWidgetContainer_${gisWidgetTeaserForm.id}">
		
	</div>


	<script language="JavaScript">
	
		function requestTable_${gisWidgetTeaserForm.id}(columnId,itemId){
			<digi:context name="tableRendererUrl" property="/widget/getTableWidget.do" />
			var url = '${tableRendererUrl}~tableId=${gisWidgetTeaserForm.id}';
			if (columnId!=null && itemId!=null){
				url+='~columnId='+columnId+'~itemId='+itemId;
			}
			var async=new Asynchronous();
			async.complete=tableCallBack_${gisWidgetTeaserForm.id};
			async.call(url);
		}
	
		function tableCallBack_${gisWidgetTeaserForm.id}(status, statusText, responseText, responseXML){
			processTableResponce_${gisWidgetTeaserForm.id}(responseText);
		}
	
		function processTableResponce_${gisWidgetTeaserForm.id}(htmlResponce){
			var myDiv = document.getElementById('tableWidgetContainer_${gisWidgetTeaserForm.id}');
			myDiv.innerHTML = htmlResponce;		
		}
	
		function tableWidgetFilterChanged_${gisWidgetTeaserForm.id}(columnId){
			var myDiv = document.getElementById('tableWidgetContainer_${gisWidgetTeaserForm.id}');
			var selItem = document.getElementsByName('selectedFilterItemId_${gisWidgetTeaserForm.id}')[0];
			var itemId = selItem.value;
			myDiv.innerHTML = 'loading...';
			requestTable_${gisWidgetTeaserForm.id}(columnId,itemId);
		}
		
		requestTable_${gisWidgetTeaserForm.id}();
	
	</script>

</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==1}">
	<digi:trn key="gis:widgetTeaser:emptyPlace">empty teaser: </digi:trn>&nbsp;${gisWidgetTeaserForm.placeName}
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==0}">
	<digi:trn key="gis:widgetTeaser:noParamSpecified">ERROR : no place param specified in layout definition for this teaser.</digi:trn>
</c:if>
