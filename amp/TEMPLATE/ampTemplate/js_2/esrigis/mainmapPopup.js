var map; // the leaflet map
var basemapLayer;
var basemapLabel;
var searchLocationsLayer; // leaflet GEOJson object containing the locations returned from the search
var isDrawActive = false;
var isMenuOpen = false;
var selectedPointEvent;
var circlePoint;
var latitude;
var longitude;

function MapPopup (lat,long) {
	latitude = lat;
	longitude = long;
	initMap();
	startContextMenu();	
}

function initMap() {
	map = L.map('map').setView([ latitude, longitude ], 7);
	basemapLayer = L.esri.basemapLayer("Streets").addTo(map);
	//basemapLayer = L.esri.tiledMapLayer(basemapurl, {}).addTo(map);

	// attach listener to basemap select to change the map's basemaps
	var basemaps = $('#basemaps');
	basemaps.on('change', function() {
		setBasemap(this.value);
	});
	map.on('click', onMapClick);
}

function onMapClick (e) {
	hideMenu();
	if (!isDrawActive) {
		return;
	}
	if (circlePoint) {
		map.removeLayer (circlePoint);
	}
	circlePoint = L.circle([e.latlng.lat, e.latlng.lng], 8500, {
	    color: 'red',
	    fillColor: '#f03',
	    fillOpacity: 0.5
	}).addTo(map);
	
	circlePoint.on('contextmenu', function(e) {
		selectedPointEvent = e;
		isMenuOpen = true;
		showMenu (e.originalEvent.clientY,e.originalEvent.clientX);
	});
	
}

function selectLocationCallerShape(selectedGraphic){
	var callerButton = window.opener.callerGisObject;
	//Lat
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1].value = "";
	//Long
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2].value = "";
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1].value = selectedGraphic.latlng.lat;
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2].value =  selectedGraphic.latlng.lng;
	element = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1];
	window.opener.postvaluesy(element);
    element = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2];
    window.opener.postvaluesx(element);
	window.close();
}


function locate() {
	$("#loadingImg").show();
	var location = $("#address").val();
	$.getJSON("/rest/postgis/location/" + location + "/false", function(data) {
		showNewResults(data);
	});
}

function showNewResults(candidates) {
	var geojsonMarkerOptions = {
		radius : 8,
		fillColor : "#ff7800",
		color : "#000",
		weight : 1,
		opacity : 1,
		fillOpacity : 0.8
	};
	// remove all options from the select that filters the type of locations
	$('#fclList').empty();
	if (searchLocationsLayer) {
		searchLocationsLayer.clearLayers();
	}
	var typeValue = $("#fclList option:selected").val();
	searchLocationsLayer = L.geoJson(
			candidates.features,
			{
				pointToLayer : function(feature, latlng) {
					return L.circleMarker(latlng, geojsonMarkerOptions);
			},
				filter : function(feature, layer) {
				var validType = true;
				if (typeValue && feature.geometry.type != typeValue) {
					validType = false;
				}
				return validType && feature.properties.score > 90;
				},
				onEachFeature : function(feature, layer) {
					layer.bindPopup("Location: " + feature.properties.name
							+ "<br>Score: " + feature.properties.score
							+ "<br>FCL:" + feature.geometry.type);
					layer.on('contextmenu', function(e) {
					   
					    showMenu (e.originalEvent.y,e.originalEvent.x);
						isMenuOpen = true;
						selectedPointEvent = e;
					});
					if (!isInList(feature.geometry.type)) {
						$("#fclList").append($('<option>', {
							value : feature.geometry.type
						}).text(feature.geometry.type));
					}
				}
			}).addTo(map);
	$("#loadingImg").hide();

}

/**
 * Changes the basemap for the map, based on the selection
 * @param basemap, the basemap to set into the map
 */
function setBasemap(basemap) {
	if (basemapLayer) {
		map.removeLayer(basemapLayer);
	}
	basemapLayer = L.esri.basemapLayer(basemap);
	map.addLayer(basemapLayer);
	if (basemapLabel) {
		map.removeLayer(basemapLabel);
	}
	if (basemap === 'ShadedRelief' || basemap === 'Oceans'
			|| basemap === 'Gray' || basemap === 'DarkGray'
			|| basemap === 'Imagery' || basemap === 'Terrain') {
		basemapLabel = L.esri.basemapLayer(basemap + 'Labels');
		map.addLayer(basemapLabel);
	}
}

/**
 * Checks if the geometry's type combobox contains the option value received as a parameter
 * @param searchtext, the select options's value to search for
 * @returns {Boolean} true if it contains the option with that value, false otherwise
 */
function isInList(searchtext) {
	var inList = false;
	$("#fclList option").each(function(i) {
		if ($(this).text() == searchtext) {
			inList = true;
			return;
		}
	});
	return inList;
}

/**
 * Removes from the map the locations whose type is different from the one received as a paremeter
 * @param value the fcl for the location, for example: Point
 */
function filterLocation (value) {
	searchLocationsLayer.eachLayer (function (layer) {
		if (layer.feature.geometry.type != value) {
			searchLocationsLayer.removeLayer (layer);
		} 
	});

}

function createPoint () {
	isDrawActive = true;
	$('#map').css("cursor","pointer");
	$('#pointBtn').addClass("button-pressed");
	$('#deactivateBtn').removeClass("button-pressed");
}

function deactivate () {
	isDrawActive = false;
	$('#map').css("cursor","-webkit-grab");
	$('#deactivateBtn').addClass("button-pressed");
	$('#pointBtn').removeClass("button-pressed");

}

function startContextMenu () {
	$(document).bind("mousedown", function (e) {
	    // If the clicked element is not the menu
	    if (!$(e.target).parents(".custom-menu").length > 0) {
	        
	        // Hide it
	        $(".custom-menu").hide(100);
	    }
	});
	// If the menu element is clicked
	$(".custom-menu li").click(function(){
	    // This is the triggered action name
	    switch($(this).attr("data-action")) {
	        // A case for each action
	        case "select": 	selectLocationCallerShape (selectedPointEvent); break;
	        case "remove": map.removeLayer(selectedPointEvent.target); circlePoint = null; break;
	    }
	  
	    // Hide it AFTER the action was triggered
	    $(".custom-menu").hide(100);
	    isMenuOpen = false;
	  });
}

function hideMenu () {
	if ($(".custom-menu").css("display") !="none") {
		$(".custom-menu").hide(100);
		isMenuOpen = false;
	}
}
function showMenu (top,left) {
// Show contextmenu
$(".custom-menu").toggle(100).css({
    top: top + "px",
    left: left + "px"
});
}