<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>


<%@ page import="java.util.Date" %>


<% Date now = new Date(); %>





<table width="100%">
	<tr>
		<td>
			<div id="mapHolder" style="border:1px solid black; width:500px; height:500px"></div>
			<div id="imageMapContainer"></div>
		</td>
	</tr>
	<tr>
		<td><div id="regionComboContainer" style="border:1px solid black; width:500px; height:20px"></div></td>
	</tr>
	<tr>
		<td><input type="Button" value="Load map" onClick="updateMap()"></td>
	</tr>
</table>


<script language="JavaScript">
	var sessionIdStr = '<%= request.getRequestedSessionId() %>';

	var mapImg = null;
	
	var xmlhttp =  new XMLHttpRequest();
	var mapImageContainer = document.getElementById("mapHolder");
	var imageMapLoaded = false;
	var imageMapLoaded1 = false;
	
	
	function initMap() {
		mapImg = document.createElement("IMG");
		mapImg.onLoad = imageLoaded(); 
		mapImg.useMap="#areaMap";
	}
	
	function updateMap() {
		if (mapImg == null) {
			initMap();
		}
		mapImg.src = "../../gis/gisService.do?action=paintMap&canvasWidth=500&canvasHeight=500&autoRect=true&mapCode=TZA";
	}
	
	function getImageMap() {
		imageMapLoaded = false;
		var uniqueStr = (new Date()).getTime();
		xmlhttp.open("GET", "../../gis/gisService.do?action=getImageMap&canvasWidth=500&canvasHeight=500&autoRect=true&mapCode=TZA&uniqueStr=" + uniqueStr, true);
		xmlhttp.onreadystatechange = addImageMap;
		xmlhttp.send(null);
		mapImageContainer.appendChild(mapImg);
	}
	
	function addImageMap() {
		if (xmlhttp.readyState == 4) {
			imageMapLoaded1 = true;
			document.getElementById("imageMapContainer").innerHTML = null;
			document.getElementById("imageMapContainer").innerHTML = generateImageMap (xmlhttp.responseXML);
			window.setTimeout(getRegionList,10);
		}
	}
	
	
	
	function getRegionList() {
		var uniqueStr = (new Date()).getTime();
		xmlhttp.open("GET", "../../gis/gisService.do?action=getSegmentInfo&mapCode=TZA&uniqueStr=" + uniqueStr, true);
		xmlhttp.onreadystatechange = addRegionList;
		xmlhttp.send(null);
		
	}
	
	function addRegionList() {
		if (xmlhttp.readyState == 4) {
			var segments = xmlhttp.responseXML.getElementsByTagName('segment');
			var segmentIndex = 0;
			var comboSrc = "<select onChange=\"hilightRegion(this.value)\">";
			
			for (segmentIndex = 0; segmentIndex < segments.length; segmentIndex ++) {
				var segment = segments[segmentIndex];
				
				comboSrc += "<option value=\"" + segment.attributes.getNamedItem("code").value + "\">";
				comboSrc += segment.attributes.getNamedItem("name").value;
				comboSrc += "</option>";
			}
	
			comboSrc += "</select>";
			document.getElementById("regionComboContainer").innerHTML = comboSrc;
		}
		
	}
	
	function imageLoaded() {
		if (!imageMapLoaded) {
			getImageMap();
		}
	}
	
	function generateImageMap (XmlObj) {
		var retVal = "<map name=\"areaMap\">";
		
		var segments = XmlObj.getElementsByTagName('segment');
				
		var segmentIndex = 0;
		
		
		
		for (segmentIndex = 0; segmentIndex < segments.length; segmentIndex ++) {
			var segment = segments[segmentIndex];
			var shapeIndex = 0;

			for (shapeIndex = 0; shapeIndex < segment.childNodes.length; shapeIndex ++) {
				var shape = segment.childNodes[shapeIndex];
				
				retVal += "<area shape=\"polygon\" coords=\""
				var pointIndex = 0;
				for (pointIndex = 0; pointIndex < shape.childNodes.length; pointIndex ++) {
					var point = shape.childNodes[pointIndex];
					retVal += point.attributes.getNamedItem("x").value;
					retVal += ",";
					retVal += point.attributes.getNamedItem("y").value;
					if (pointIndex < shape.childNodes.length - 1) {
						retVal += ",";
					}
				}
				retVal += "\"";
				retVal += " title=\"" + segment.attributes.getNamedItem("name").value + "\"";
				retVal += ">";
				//retVal += " onClick=\"alert('" + segment.attributes.getNamedItem("name").value + "')\">";
			}
		}
		retVal += "</map>";
		return retVal;
	}
	
	function hilightRegion (regionCode) {
		mapImg.src = "../../gis/gisService.do?action=paintMap&canvasWidth=500&canvasHeight=500&autoRect=true&mapCode=TZA&hilight="+regionCode;
	}
	

	
</script>
