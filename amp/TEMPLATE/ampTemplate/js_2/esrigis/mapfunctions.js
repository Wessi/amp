dojo.require("dijit.dijit"); // Optimize: load dijit layer
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("esri.map");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("esri.toolbars.navigation");
dojo.require("dijit.form.Button");
dojo.require("dijit.Toolbar");
dojo.require("esri.tasks.find");
dojo.require("esri.tasks.geometry");
dojo.require("esri.dijit.BasemapGallery");
dojo.require("esri.arcgis.utils");
dojo.require("dijit.TitlePane");
dojo.require("dijit.Menu");

var map, navToolbar,geometryService,findTask,findParams;
var totallocations = 0;
var features = new Array();
var structures = new Array();
var timer_on = 0;
var activitiesarray = new Array();
var loading;
var cL;
var basemapGallery;
var activitieson;
var structureson;
var maxExtent;
var basemap;
//---- Search variable ---- 
var searchactive=new Boolean();
var searchdistance;
var foundstr = new Array();
var searchpoint;

function init() {
	//This have to be replaced with Global Settings values
	loading = dojo.byId("loadingImg");
	var basemapUrl = "http://4.79.228.117:8399/arcgis/rest/services/World_Street_Map/MapServer";
	var mapurl = "http://4.79.228.117:8399/arcgis/rest/services/Liberia_Map_Test/MapServer";
	var indicatorurl = "http://4.79.228.117:8399/arcgis/rest/services/Liberia_Pop_Density_and_Poverty/MapServer";
	basemap = new esri.layers.ArcGISTiledMapServiceLayer(basemapUrl, {id:'base'}); // Levels at which this layer will be visible);
	liberiamap = new esri.layers.ArcGISDynamicMapServiceLayer(mapurl, {opacity : 0.90,id:'liberia'});
	povertymap = new esri.layers.ArcGISDynamicMapServiceLayer(indicatorurl, {id:'indicator',visible:false});
	
	geometryService = new esri.tasks.GeometryService("http://4.79.228.117:8399/arcgis/rest/services/Geometry/GeometryServer");
	//esri.config.defaults.io.alwaysUseProxy = true;
	esriConfig.defaults.io.proxyUrl = "/esrigis/esriproxy.do";
	
	var layerLoadCount = 0;
	if (basemap.loaded) {
		layerLoadCount += 1;
		if (layerLoadCount === 2) {
			createMapAddLayers(basemap, liberiamap);
		}
	} else {
		dojo.connect(basemap, "onLoad", function(service) {
			layerLoadCount += 1;
			if (layerLoadCount === 2) {
				createMapAddLayers(basemap, liberiamap);
			}
		});
	}
	if (liberiamap.loaded) {
		layerLoadCount += 1;
		if (layerLoadCount === 2) {
			createMapAddLayers(basemap, liberiamap);
		}
	} else {
		dojo.connect(liberiamap, "onLoad", function(service) {
			layerLoadCount += 1;
			if (layerLoadCount === 2) {
				createMapAddLayers(basemap, liberiamap);
			}
		});
	}

}

// Create a map, set the extent, and add the services to the map.
function createMapAddLayers(myService1, myService2) {
	// create map
	lods = 
	map = new esri.Map("map", {extent : esri.geometry.geographicToWebMercator(myService2.fullExtent)});

	dojo.connect(map, 'onLoad', function(map) {
		dojo.connect(dijit.byId('map'), 'resize', resizeMap);
        dojo.byId('map_zoom_slider').style.top = '95px';
        getActivities(false);
        getStructures(false);
    });
	map.addLayer(myService1);
	map.addLayer(myService2);
	map.addLayer(povertymap);
	navToolbar = new esri.toolbars.Navigation(map);
	dojo.connect(navToolbar, "onExtentHistoryChange",extentHistoryChangeHandler);
	
	dojo.connect(map, "onClick", doBuffer);
	//dojo.connect(map, "onExtentChange", showExtent);
	
	createBasemapGalleryEsri();
	createBasemapGallery();
	maxExtent = map.extent;
	searchactive = false;
}


function showExtent(extent, delta, levelChange, lod) {
	//In javascript, object passes byref. so it's not correct to difine new extent using
	//"var adjustedEx = extent;"
	var adjustedEx = new esri.geometry.Extent(extent.xmin, extent.ymin, extent.xmax, extent.ymax);
	var flag = false;	
	//set a buffer to make the max extent a slightly bigger to void minor differences
	//the map unit for this case is meter. 
	var buffer = 100;
	console.log(extent.xmin + "," + maxExtent.xmin);
	var onLoadHandle = dojo.connect(basemap, "onUpdate", function() {
		//cancel the connection because it should'n only be triggered when extent changes. Other events causing tiles loading shoudn't trigger this.
        dojo.disconnect(onLoadHandle);
        if(extent.xmin < maxExtent.xmin-buffer) {
			adjustedEx.xmin = maxExtent.xmin;
			adjustedEx.xmax = Math.abs(extent.xmin - maxExtent.xmin) + extent.xmax;
            flag = true;
        }
		if(extent.ymin < maxExtent.ymin-buffer) {
		    adjustedEx.ymin = maxExtent.ymin;
		    adjustedEx.ymax = Math.abs(extent.ymin - maxExtent.ymin) + extent.ymax;
            flag = true;
        }
		if(extent.xmax-buffer > maxExtent.xmax) {
		    adjustedEx.xmax = maxExtent.xmax;
		    adjustedEx.xmin =extent.xmin - Math.abs(extent.xmax - maxExtent.xmax);
            flag = true;
        }
		if(extent.ymax-buffer > maxExtent.ymax) {
		    adjustedEx.ymax = maxExtent.ymax;
		    adjustedEx.ymin =extent.ymin - Math.abs(extent.ymax - maxExtent.ymax);
            flag = true;
        }
		if (flag === true) {
			map.setExtent(adjustedEx);				
		}
		flag = false;
	});		
  }

function toggleindicatormap(id) {
	  var layer = map.getLayer(id);
	  var functionalayer = map.getLayer('liberia');
	  if (layer.visible) {
	    layer.hide();
	    functionalayer.show();
	  } else {
	    layer.show();
	    functionalayer.hide();
	  }
	}

function showLoading() {
    esri.show(loading);
    map.disableMapNavigation();
    map.hideZoomSlider();
  }

  function hideLoading(error) {
    esri.hide(loading);
    map.enableMapNavigation();
    map.showZoomSlider();
    
  
  }

function showCoordinates(evt) {
    //get mapPoint from event
    //The map is in web mercator - modify the map point to display the results in geographic
    var mp = esri.geometry.webMercatorToGeographic(evt.mapPoint);
    //display mouse coordinates
    //console.log(mp.x + ", " + mp.y);
     
  }

function extentHistoryChangeHandler() {
	dijit.byId("zoomprev").disabled = navToolbar.isFirstExtent();
	dijit.byId("zoomnext").disabled = navToolbar.isLastExtent();
}

function resizeMap() {
	// resize the map when the browser resizes - view the 'Resizing and
	// repositioning the map' section in
	// the following help topic for more details
	// http://help.esri.com/EN/webapi/javascript/arcgis/help/jshelp_start.htm#jshelp/inside_faq.htm
	var resizeTimer;
	clearTimeout(resizeTimer);
	resizeTimer = setTimeout(function() {
		map.resize();
		map.reposition();
	}, 500);
}

// show map on load
dojo.addOnLoad(init);


function doBuffer(evt) {
	if (searchactive && searchdistance){
		showLoading();
	    map.graphics.clear();
	    var params = new esri.tasks.BufferParameters();
	    params.geometries = [ evt.mapPoint ];
	    
	    //buffer in linear units such as meters, km, miles etc.
	    params.distances = [ 1, searchdistance ];
	    params.unit = esri.tasks.GeometryService.UNIT_KILOMETER;
	    params.outSpatialReference = map.spatialReference;
		geometryService.buffer(params, showBuffer);
		findbydistance(evt);
	}
}

var rangegraphicLayer;
function showBuffer(geometries) {
	  var symbol = new esri.symbol.SimpleFillSymbol( esri.symbol.SimpleFillSymbol.STYLE_SOLID,new esri.symbol.SimpleLineSymbol(
    		esri.symbol.SimpleLineSymbol.STYLE_DASH,new dojo.Color([112,0,0,0.60]), 2),new dojo.Color([112,0,0,0.15])
   );
   try{
	   if (rangegraphicLayer){
		   map.removeLayer(map.getLayer("rangelayer"));
	   }
	}
	catch(e){
		console.log(e);
	}
	rangegraphicLayer = esri.layers.GraphicsLayer({displayOnPan: false, id: "rangelayer", visible: true}); 
    dojo.forEach(geometries, function(geometry) {
	      var graphic = new esri.Graphic(geometry,symbol);
	      rangegraphicLayer.add(graphic);
    });
   
    map.addLayer(rangegraphicLayer);
    map.reorderLayer(map.getLayer("rangelayer"),0);
}

function clearbuffer(){
	try{
		if (rangegraphicLayer){
			map.removeLayer(map.getLayer("rangelayer"));
		}
		map.infoWindow.hide;
	}catch(e){
		console.log(e);
	}
}

function findbydistance(evt){
	searchpoint = evt;
	foundstr = [];
	if (structures.length>0){
		var count=0;
		var tasktime = structures.length * 5 * 1000 ;
		var t=setTimeout("showStInfoWindow();",tasktime);
	   
		var distParams = new esri.tasks.DistanceParameters();
		//Iterate structures and query the geometry server to calculate the distance. 
		for ( var int = 0; int < structures.length; int++) {
			distParams.distanceUnit = esri.tasks.GeometryService.UNIT_KILOMETER;    
			distParams.geometry1 = evt.mapPoint;
			distParams.geometry2 = structures[int].geometry;
			distParams.geodesic = true;
			
			geometryService.distance(distParams,function(distance){
				if (distance <=searchdistance){
					//console.log("Results: " + distance + " " + structures[count].attributes.Activity );
					foundstr.push(structures[count]);
				}
				count++;
				if (count+1 >structures.length ){
					showStInfoWindow();
					}
				});
			
		}
	}
}

function showStInfoWindow () {
	 searchactive = false;
	 var content = "<table border='1' width='100%'>" 
	 			+ "<tr>" 
	 			+ "<td align='center' width='200px'>Name</td>" 
	 			+ "<td align='center' width='100px'>Type</td>" 
	 			+ "<td align='center' width='300px'>Activity</td>"
	 			+ "</tr>";
		if (map.infoWindow.isShowing) {
	        map.infoWindow.hide();
		}
    map.infoWindow.setTitle("Structures");
    for ( var int = 0; int < foundstr.length; int++) {
    	    content = content + "<tr><td>" + foundstr[int].attributes["Structure Name"] + "</a></td>" ;
    	    content = content + "<td align='left'>" + foundstr[int].attributes["Structure Type"] + "</td>" ;
    	    content = content + "<td>" + foundstr[int].attributes["Activity"] + "</td></tr>" ;
     }
    content = content + "</table>";
    if (foundstr.length>0){
    	map.infoWindow.setContent(content);
    	map.infoWindow.resize(600, 200);
    	map.infoWindow.show(searchpoint.screenPoint,map.getInfoWindowAnchor(searchpoint.screenPoint));
    }
    dojo.connect(map.infoWindow, "onHide", clearbuffer);
    hideLoading();
}

function getActivities(clear) {
	if (clear && cL){
		cL.clear();
	}
	
	var xhrArgs = {
		url : "/esrigis/datadispatcher.do?showactivities=true",
		handleAs : "json",
		   load: function(jsonData) {
		        // For every item we received...
			   totallocations=0;
		       features = []; 
			   dojo.forEach(jsonData,function(activity) {
		        	activitiesarray.push(activity);
		        	MapFind(activity);
		        });
			   if (totallocations==0){
					timer_on = 1;
					drawpoints();
				}
		   },
		error : function(error) {
			console.log(error);
		}
	}
	// Call the asynchronous xhrGet
	var deferred = dojo.xhrGet(xhrArgs);
	showLoading();
}

function MapFind(activity){
	dojo.forEach(activity.locations,function(location) {
    	//If the location has lat and lon not needs to find the point in the map
		if (location.islocated==false){
    		findTask = new esri.tasks.FindTask("http://4.79.228.117:8399/arcgis/rest/services/Liberia/MapServer");
    	    //create find parameters and define known values
    	    findParams = new esri.tasks.FindParameters();
    	    findParams.returnGeometry = true;
    	    findParams.layerIds = [0,1];
    	    findParams.contains = false;
    	    //TODO:Configure the search field in global settings
    	    findParams.searchFields = ["GEO_ID"];
    		execute(location.geoId);
    		totallocations ++;
    	}else{
    		//Create a graphic point based on the x y coordinates wkid(Well-known ID) 4326 for GCS_WGS_1984 projection
    		var pt = new esri.geometry.Point(location.lat,location.lon,new esri.SpatialReference({"wkid":4326}));
    		var sms = new esri.symbol.SimpleMarkerSymbol().setStyle(esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE).setColor(new dojo.Color([255,0,0,0.5]));
    		var attr = {"Temp":"Temporal Attribute"};
    		var infoTemplate = new esri.InfoTemplate("");   
    		var pgraphic = new esri.Graphic(pt,sms,attr,infoTemplate);
    		var exit = false;
    		var primarysector;
    		var primarysectorschema;
    		var primarypercentage;
    		
    		dojo.forEach(activity.sectors,function(sector) {
    			primarysector = sector.sectorName;
    			primarysectorschema = sector.sectorScheme;
    			primarypercentage = sector.sectorPercentage;
    		});
    		
    		var donorname;
    		dojo.forEach(activity.donors,function(donor) {
    			if (donorname==null){
    				donorname = donor.donorname;
    			}else{
    				donorname = donorname +","+ donor.donorname;
    			}
    		});
    		
    		pgraphic.setAttributes( {
    			  "Activity":'<b>'+activity.activityname+'</b>',
    			  "Activity Preview":'<a href="/aim/selectActivityTabs.do~ampActivityId='+activity.id+'" target="_blank">Click here</a>',	
    			  "AMP Activity ID":activity.ampactivityid,	
    			  "Activity commitments":'<b>'+activity.commitments+'</b>',
  				  "Activity disbursements":'<b>'+activity.disbursements+'</b>',
  				  "Activity expenditures":'<b>'+activity.expenditures+'</b>',
  				  "Donors":'<b>'+donorname+'</b>',
  				  "Location":'<b>'+location.name+'</b>',
  				  "Location commitments":'<b>'+location.commitments+'</b>',
  				  "Location disbursements":'<b>'+location.disbursements+'</b>',
  				  "Location expenditures":'<b>'+location.expenditures+'</b>',
  				  "Location percentage":location.percentage,
  				  "Primary Sector":'<b>'+primarysector+'</b>',
  				  "Primary Schema":primarysectorschema,
  				  "Primary Percentage":primarypercentage
  				  });
  			location.isdisplayed = true;
  			features.push(pgraphic);
    	}
    });
}

function execute(searchText) {
    //set the search text to find parameters
    findParams.searchText = searchText;
    findTask.execute(findParams, showResults);
    var t=setTimeout("drawpoints()",10000);
    timer_on = 1;
  }


function showResults(results) {
    //find results return an array of findResult.
    dojo.forEach(results, function(result) {
      var graphic = result.feature;
      console.log("Found : " + result.layerName + "," + result.foundFieldName + "," + result.value);
      var point =  graphic.geometry.getExtent().getCenter();
      var sms = new esri.symbol.SimpleMarkerSymbol().setStyle(esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE).setColor(new dojo.Color([255,0,0,0.5]));
      var attr = {"Location":result.value};
      var infoTemplate = new esri.InfoTemplate("");   
      var pgraphic = new esri.Graphic(point,sms,attr,infoTemplate);
      
      //Iterate over the activities array and assign the attributes to each point
      var exit = false;
      for ( var int = 0; int < activitiesarray.length; int++) {
    	var activity = activitiesarray[int];
    	for ( var int2 = 0; int2 < activitiesarray[int].locations.length; int2++) {
			var loc = activitiesarray[int].locations[int2];
			if (loc.name==result.value && loc.isdisplayed!=true){
  			  pgraphic.setAttributes( {
  				  "Activity":activity.activityname,
  				  "Location":loc.name});
  			  loc.isdisplayed = true;
  			  exit = true;
  			}
		}
    	if (exit==true){
			break;
		}
      }
      features.push(pgraphic);
  	  totallocations--;

  	  if (totallocations == 0){
  		  drawpoints();
  	  }
  	});
}

function drawpoints(){
	if (timer_on){
	 if(cL!=null){ 
		 cL._features=[];
		 cL.levelPointTileSpace = [];
	 }
	  	cL = new esri.ux.layers.ClusterLayer({
			displayOnPan: false,
			map: map,
			id: "activitiesMap",
			features: features,
			infoWindow: {
				template: new esri.InfoTemplate("Activity Details", "${*}"),
			width: 300,
			height: 300
		},
		flareLimit: 15,
		flareDistanceFromCenter: 20
	});
	map.addLayer(cL);
	timer_on=0;
	}
}



//--------------------------------------FFerreyra Functions
var locations = new Array();
var implementationLevel = [{"name": "Region", "mapId": "0", "mapField": "COUNTY"},
                           {"name": "Zone", "mapId": "1", "mapField": "DISTRICT"}
                          ];
function getHighlights(level) {
	closeHide();
	var xhrArgs = {
			url : "/esrigis/datadispatcher.do?showhighlights=true&level=" + implementationLevel[level].name,
			handleAs : "json",
			   load: function(jsonData) {
			        // For every item we received...
			        dojo.forEach(jsonData,function(location) {
			        	locations.push(location);
			        });
		        	MapFindLocation(implementationLevel[level]);
			    },
			error : function(error) {
				// Error handler
			}
		};
	// Call the asynchronous xhrGet
	var deferred = dojo.xhrGet(xhrArgs);
}

function closeHide(){
	$('#legenddiv').hide('slow');
	try{
		map.removeLayer(map.getLayer("highlightMap"));
	}
	catch(e){}
//	cL.clear();
}
var currentLevel;
function MapFindLocation(level){
	showLoading();
	var queryTask = new esri.tasks.QueryTask("http://4.79.228.117:8399/arcgis/rest/services/Liberia/MapServer/" + level.mapId);
    var query = new esri.tasks.Query();
    query.where = level.mapField + " <> ''";
    query.outSpatialReference = {wkid:map.spatialReference.wkid};
    query.returnGeometry = true;
    query.outFields = ["COUNT", level.mapField, "GEO_ID"];
    currentLevel = level;
    queryTask.execute(query, addResultsToMap);
}

function addResultsToMap(featureSet) {
    var border = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([150,150,150]), 1);
    var symbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID, border, new dojo.Color([150, 150, 150, 0.5]));
    var colors = colorsOrange;
    var numRanges = colors.length;
    var localGraphicLayer = esri.layers.GraphicsLayer({displayOnPan: true, id: "highlightMap", visible: true});

    //Using logarithmic scale
    var maxLog = Math.log(getMaxValue(locations, "commitments"));
    var minLog = Math.log(getMinValue(locations, "commitments"));

    var max = getMaxValue(locations, "commitments");
    var min = getMinValue(locations, "commitments");

    var breaksLog = (maxLog - minLog) / numRanges;
    var breaks = (max - min) / numRanges;

    var rangeColors = new Array();
    var renderer = new esri.renderer.ClassBreaksRenderer(symbol, "COUNT");
    for (var i=0; i<numRanges; i++) {
    	rangeColors.push([parseFloat(min + (i*breaks)), parseFloat(min + ((i+1)*breaks))]);
        renderer.addBreak(parseFloat(minLog + (i*breaksLog)),
                parseFloat(minLog + ((i+1)*breaksLog)),
                new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID, border, colors[i]));
      }

    dojo.forEach(featureSet.features,function(feature){
    	//Read current attributes and assign a new set
    	var count = feature.attributes["COUNT"];
    	var county = feature.attributes["COUNTY"];
    	var geoId = feature.attributes["GEO_ID"];
    	feature.setAttributes( {
			  "COUNT": count,
			  "AMOUNT": count,
			  "COUNTY": county,
			  "GEO_ID": geoId
			  });
    	//TODO: Should support all types of funding
    	feature.setInfoTemplate(new esri.InfoTemplate("Funding", "County: ${COUNTY} <br/>Commitments: ${AMOUNT}<br/> "));
    	localGraphicLayer.add(feature);
    });
    
    localGraphicLayer = updateLocationAttributes(localGraphicLayer);
    localGraphicLayer.setRenderer(renderer);
    map.addLayer(localGraphicLayer);
    map.reorderLayer(map.getLayer("highlightMap"),0);
    map.setExtent(map.extent.expand(1.01));
    hideLoading();
    showLegend(rangeColors, colors);
  }

function showLegend(rangeColors, colors){
	//TODO: Should support currencies
	var currencyString = "USD";
	//TODO: Should support all types of funding
	var typeOfFunding = "Commitments";
	var htmlDiv = "";
	htmlDiv += "<div onclick='closeHide()' style='color:white;float:right;cursor:pointer;'>X</div>";
	htmlDiv += "<div class='legendHeader'>Showing " + typeOfFunding + " for " + currentLevel.name + "<br/><hr/></div>";
	for(var i=0; i< rangeColors.length; i++){
		htmlDiv += "<div class='legendContentContainer'>"
				+ "<div class='legendContentValue' style='background-color:rgba(" + colors[i].toRgba() + ");' ></div>"
				+"</div>"
				+ "<div class='legendContentLabel'>" + Math.ceil(rangeColors[i][0]) + " " + currencyString + " - " + Math.floor(rangeColors[i][1]) + " " + currencyString + " </div><br/>";
	}
	$('#legenddiv').html(htmlDiv);
	$('#legenddiv').show('slow');
}

var colorsBlue = [
		new dojo.Color([ 222, 235, 247, 0.7]),
		new dojo.Color([ 198, 219, 239, 0.7]),
		new dojo.Color([ 158, 202, 225, 0.7]),
		new dojo.Color([ 107, 174, 214, 0.7]),
		new dojo.Color([ 66, 146, 198, 0.7]),
		new dojo.Color([ 33, 113, 181, 0.7]),
		new dojo.Color([ 8, 81, 156, 0.7]),
		new dojo.Color([ 8, 48, 107, 0.7])];

var colorsOrange = [
		new dojo.Color([255, 255, 229, 0.8]),
		new dojo.Color([255, 247, 188, 0.8]),
		new dojo.Color([254, 227, 145, 0.8]), 
		new dojo.Color([254, 196, 79, 0.8]), 
		new dojo.Color([254, 153, 41, 0.8]), 
		new dojo.Color([236, 112, 20, 0.8]), 
		new dojo.Color([204, 76, 2, 0.8]), 
		new dojo.Color([153, 52, 4, 0.8]), 
		new dojo.Color([102, 37, 6 , 0.8])];


function getMaxValue(array, measure){
	var maxValue = 0;
	for(var i=0; i < array.length; i++){
		var currentMeasure = parseFloat(array[i][measure]);
		if(currentMeasure > maxValue)
			maxValue = currentMeasure; 
	}
	return maxValue+10;
}

function getMinValue(array, measure){
	var minValue = 0;
	for(var i=0; i < array.length; i++){
		var currentMeasure = parseFloat(array[i][measure]);
		if(minValue == 0) minValue = currentMeasure; 
		if(currentMeasure < minValue)
			minValue = currentMeasure; 
	}
	return minValue-10;
}

function updateLocationAttributes(graphicLayer){
    var count = graphicLayer.graphics.length;
    for(var i=0;i<count;i++) {
      var g = graphicLayer.graphics[i];
      for(var j=0;j<locations.length;j++){
    	  var currentLocation = locations[j];
          if(g.attributes["GEO_ID"] == currentLocation.geoId){
        	  g.attributes["COUNT"] = Math.log(currentLocation.commitments);
        	  g.attributes["AMOUNT"] = currentLocation.commitments;
        	  break;
          }
      }
    }
    return graphicLayer;
}

var structureGraphicLayer;
function getStructures(clear) {
	if (clear){
		try {
			map.removeLayer(map.getLayer("structuresMap"));
			map.removeLayer(map.getLayer("activitiesMap"));
		}catch(e){
			console.log(e);
		}
	}
	if (structureGraphicLayer){
		if (structureGraphicLayer.visible){
			structureGraphicLayer.hide();
		}else{
			structureGraphicLayer.show();
		}
	}else{
	    structureGraphicLayer = esri.layers.GraphicsLayer({displayOnPan: false, id: "structuresMap", visible: false});
	    structureson =true;
	    var xhrArgs = {
			url : "/esrigis/datadispatcher.do?showstructures=true",
			handleAs : "json",
			   load: function(jsonData) {
				   dojo.forEach(jsonData,function(activity) {
			        	MapFindStructure(activity, structureGraphicLayer);
			        });
				    map.addLayer(structureGraphicLayer);
				    map.setExtent(map.extent.expand(1.01));
					hideLoading();
			   },
			error : function(error) {
				console.log(error);
			}
		}
		// Call the asynchronous xhrGet
		var deferred = dojo.xhrGet(xhrArgs);
		showLoading();
	}
}

function MapFindStructure(activity, structureGraphicLayer){
	dojo.forEach(activity.structures,function(structure) {
		var sms = new esri.symbol.PictureMarkerSymbol('/esrigis/structureTypeManager.do~action=displayIcon~id=' + structure.typeId, 32, 37);
		var pgraphic;
		if(structure.shape == ""){
			var pt = new esri.geometry.Point(structure.lat,structure.lon,map.spatialReference);
			var transpt = esri.geometry.geographicToWebMercator(pt);
			var infoTemplate = new esri.InfoTemplate("");   
			var attr = {"Temp":"Temporal Attribute"};
			pgraphic = new esri.Graphic(transpt,sms,attr,infoTemplate);
			
			pgraphic.setAttributes( {
				  "Structure Name":structure.name,
				  "Activity": activity.activityname,
				  "Structure Type":structure.type
				  });
			structures.push(pgraphic);
		}
		else
		{
			var jsonObject = eval('(' + structure.shape + ')');
			if(jsonObject.geometry != null){ //If it's a complete Graphic object
				pgraphic = new esri.Graphic(jsonObject);
				pgraphic.setAttributes( {
					  "Structure Name":structure.name,
					  "Activity": activity.activityname,
					  "Structure Type":structure.type
					  });
				pgraphic.setInfoTemplate(new esri.InfoTemplate(""));
				if(jsonObject.symbol.style == "esriSMSCircle") //If it's a point, put the appropriate icon
				{
					pgraphic.setSymbol(sms);
				}
				
			}
			else
			{
				pt = new esri.geometry.Point(jsonObject);
				var infoTemplate = new esri.InfoTemplate("");   
				var attr = {"Temp":"Temporal Attribute"};
				pgraphic = new esri.Graphic(pt,sms,attr,infoTemplate);
				pgraphic.setAttributes( {
					  "Structure Name":structure.name,
					  "Activity": activity.activityname,
					  "Structure Type":structure.type
					  });
				
			}
		}
		structureGraphicLayer.add(pgraphic);
		structures.push(pgraphic);
	});
}
