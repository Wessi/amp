dojo.require("dijit.dijit"); // Optimize: load dijit layer
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("esri.map");
dojo.require("esri.layers.FeatureLayer");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("esri.toolbars.navigation");
dojo.require("dijit.form.Button");
dojo.require("dijit.Toolbar");
dojo.require("esri.tasks.find");
dojo.require("esri.tasks.geometry");

var map, navToolbar,geometryService,findTask,findParams;
var totallocations = 0;
var features = new Array();
var timer_on = 0;
function init() {
	//This have to be replaced with Global Settings values
	var basemapUrl = "http://4.79.228.117:8399/arcgis/rest/services/World_Physical_Map/MapServer";
	var mapurl = "http://4.79.228.117:8399/arcgis/rest/services/Liberia/MapServer";
	var basemap = new esri.layers.ArcGISTiledMapServiceLayer(basemapUrl, {opacity : 0.90}); // Levels at which this layer will be visible);
	liberiamap = new esri.layers.ArcGISDynamicMapServiceLayer(mapurl, {opacity : 0.50});

	
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
	// convert the extent to Web Mercator
	map = new esri.Map("map", {
		extent : esri.geometry.geographicToWebMercator(myService2.fullExtent)
	});

	dojo.connect(map, 'onLoad', function(map) {
		dojo.connect(dijit.byId('map'), 'resize', resizeMap);
		dojo.connect(map, "onMouseMove", showCoordinates);
	    dojo.connect(map, "onMouseDrag", showCoordinates);
	});
	
	map.addLayer(myService1);
	map.addLayer(myService2);
	navToolbar = new esri.toolbars.Navigation(map);
	dojo.connect(navToolbar, "onExtentHistoryChange",extentHistoryChangeHandler);
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

function getContent() {
	var xhrArgs = {
		url : "/esrigis/maphelper.do?showactivities=true",
		handleAs : "json",
		   load: function(jsonData) {
		        // For every item we received...
			   totallocations=0;
		        dojo.forEach(jsonData,function(activity) {
		        	MapFind(activity);
		        });
		    },
		error : function(error) {
			// Error hanlder
		}
	}
	// Call the asynchronous xhrGet
	var deferred = dojo.xhrGet(xhrArgs);
}

function MapFind(activity){
	 map.graphics.clear();
    findTask = new esri.tasks.FindTask("http://4.79.228.117:8399/arcgis/rest/services/Liberia/MapServer");
    //create find parameters and define known values
    findParams = new esri.tasks.FindParameters();
    findParams.returnGeometry = true;
    findParams.layerIds = [0,1];
    findParams.contains = false;
    //Let's configure the search field in global settings
    
    findParams.searchFields = ["FIRST_FIRS","DNAME"];
    dojo.forEach(activity.locations,function(location) {
    	execute(location.name);
    	totallocations ++;
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
      
      features.push(point);
  	  totallocations--;
  	});
}

function drawpoints(){
	if (timer_on){
	  var cL = new esri.ux.layers.ClusterLayer({
			displayOnPan: false,
			map: map,
			features: features,
			infoWindow: {
				template: new esri.InfoTemplate('${api_number}', '<b>Operator:</b> ${operator_n}'),
			width: 225,
			height: 85
		},
		flareLimit: 15,
		flareDistanceFromCenter: 20
	});
	map.addLayer(cL);
	timer_on=0;
	}

}

//FFerreyra Functions
var locations = new Array();

function getHighlights() {
	var xhrArgs = {
			url : "/esrigis/maphelper.do?showhighlights=true",
			handleAs : "json",
			   load: function(jsonData) {
			        // For every item we received...
			        dojo.forEach(jsonData,function(location) {
			        	locations.push(location);
			        });
		        	MapFindLocation();
			    },
			error : function(error) {
				// Error handler
			}
		};
	// Call the asynchronous xhrGet
	var deferred = dojo.xhrGet(xhrArgs);
}

function MapFindLocation(){
	map.graphics.clear();
    var queryTask = new esri.tasks.QueryTask("http://4.79.228.117:8399/arcgis/rest/services/Liberia/MapServer/0");
    var query = new esri.tasks.Query();
    query.where = "COUNTY <> ''";
    query.outSpatialReference = {wkid:map.spatialReference.wkid};
    query.returnGeometry = true;
    query.outFields = ["COUNT", "COUNTY"];
    queryTask.execute(query, addResultsToMap);
}

function addResultsToMap(featureSet) {
    var symbol = new esri.symbol.SimpleFillSymbol();
    symbol.setColor(new dojo.Color([255, 255, 255, 0.5]));
    var numRanges = 40;
    //Generate array of colors
    var colors = new Array();
    for(var i=0; i < numRanges; i++){
    	colors.unshift(new dojo.Color([0, 0, 150 + Math.round((i*(150/numRanges))), 0.3]));
    }
    var max = getMaxValue(locations, "commitments");
    var min = getMinValue(locations, "commitments");
    var breaks = (max - min) / numRanges;

    var renderer = new esri.renderer.ClassBreaksRenderer(symbol, "COUNT");
    for (var i=0; i<numRanges; i++) {
        renderer.addBreak(parseFloat(min + (i*breaks)),
                parseFloat(min + ((i+1)*breaks)),
                new esri.symbol.SimpleFillSymbol().setColor(colors[i]));
      }

    resultTemplate = new esri.InfoTemplate("County", "<tr><td>${FIRST_FIRS}</td></tr><br/><tr><td>${COUNT}</td></tr>");

    dojo.forEach(featureSet.features,function(feature){
      map.graphics.add(feature);
    });
    updateLocationAttributes();
    map.graphics.setRenderer(renderer);
    map.setExtent(map.extent.expand(1.01));
  }

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

function updateLocationAttributes(){
    var count = map.graphics.graphics.length;
    for(var i=0;i<count;i++) {
      var g = map.graphics.graphics[i];
      for(var j=0;j<locations.length;j++){
    	  var currentLocation = locations[j];
          if(g.attributes["COUNTY"] == currentLocation.name){
        	  g.attributes["COUNT"] = currentLocation.commitments;
        	  break;
          }
      }
    }
}
