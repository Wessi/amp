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
dojo.require("esri.dijit.BasemapGallery");
dojo.require("esri.arcgis.utils");

dojo.require("esri.toolbars.draw");
dojo.require("esri.toolbars.edit");
dojo.require("dijit.Menu");

var map, navToolbar,geometryService,findTask,findParams, tb;
var totallocations = 0;
var features = new Array();
var timer_on = 0;
var activitiesarray = new Array();
var loading;
var cL;
var basemapGallery;

function init() {
	//This have to be replaced with Global Settings values
	loading = dojo.byId("loadingImg");
	loading.hidden = true;
	
	var basemapUrl;
	var mapurl;
	
	var xhrArgs = {
			url : "/esrigis/datadispatcher.do?getconfig=true",
			handleAs : "json",
			sync:true,
			load: function(jsonData) {
				   dojo.forEach(jsonData,function(map) {
			        	switch (map.maptype) {
						case 1:
							basemapUrl = map.mapurl;
							break;
						case 2:
							mapurl = map.mapurl;
						default:
							break;
						}
			        });
				},
			error : function(error) {
				console.log(error);
			}
		}
		// Call the asynchronous xhrGet
		var deferred = dojo.xhrGet(xhrArgs);
	
	
	var basemap = new esri.layers.ArcGISTiledMapServiceLayer(basemapUrl, {opacity : 0.90}); // Levels at which this layer will be visible);
	liberiamap = new esri.layers.ArcGISDynamicMapServiceLayer(mapurl, {opacity : 0.90});

	
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
	esri.hide(loading);

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
        dojo.byId('map_zoom_slider').style.top = '80px';
        createToolbarAndContextMenu();
        initToolbar(map);
        try {
        	selectCurrentLocation();
        }
        catch(e){
        	
        	console.log("Couldn't position current structure:" + e);
        }
    });
	map.addLayer(myService1);
	map.addLayer(myService2);
	navToolbar = new esri.toolbars.Navigation(map);
	dojo.connect(navToolbar, "onExtentHistoryChange",extentHistoryChangeHandler);
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
    console.log(mp.x + ", " + mp.y);
     
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

var editToolbar;
var ctxMenuForGraphics, ctxMenuForMap;
var selected, currentLocation;


function createToolbarAndContextMenu() {
  // Create and setup editing tools
  editToolbar = new esri.toolbars.Edit(map);

  dojo.connect(map,"onClick", function(evt){
    editToolbar.deactivate();
  });

  createMapMenu();
  createGraphicsMenu();
}



function createMapMenu() {
  // Creates right-click context menu for map
  ctxMenuForMap = new dijit.Menu({
    onOpen: function(box) {
      // Lets calculate the map coordinates where user right clicked.
      // We'll use this to create the graphic when the user clicks
      // on the menu item to "Add Point"
      currentLocation = getMapPointFromMenuPosition(box);          
      editToolbar.deactivate();
    }
  });

  ctxMenuForMap.addChild(new dijit.MenuItem({ 
    label: "Select this point",
    onClick: function(evt) {
    	selectLocationCaller(currentLocation);
    }
  }));


  ctxMenuForMap.startup();
  ctxMenuForMap.bindDomNode(map.container);
}


function createGraphicsMenu() {
  // Creates right-click context menu for GRAPHICS

  ctxMenuForGraphics = new dijit.Menu({});
  ctxMenuForGraphics.addChild(new dijit.MenuItem({ 
	    label: "Select this structure",
	    onClick: function(evt) {
	    	selectLocationCallerShape(selected);
	    }
	  }));
  ctxMenuForGraphics.addChild(new dijit.MenuItem({ 
    label: "Edit",
    onClick: function() {
      if(selected.geometry.type !== "point"){
        editToolbar.activate(esri.toolbars.Edit.EDIT_VERTICES, selected);
      }
      else{
        alert("Not implemented");
      }
    } 
  }));


  ctxMenuForGraphics.addChild(new dijit.MenuItem({ 
    label: "Move",
    onClick: function() {
      editToolbar.activate(esri.toolbars.Edit.MOVE, selected);
    } 
  }));

  ctxMenuForGraphics.addChild(new dijit.MenuItem({ 
    label: "Rotate/Scale",
    onClick: function() {
    if(selected.geometry.type !== "point"){
        editToolbar.activate(esri.toolbars.Edit.ROTATE | esri.toolbars.Edit.SCALE, selected);
      }
      else{
        alert("Not implemented");
      }
    }
  }));

  ctxMenuForGraphics.addChild(new dijit.MenuSeparator());
  ctxMenuForGraphics.addChild(new dijit.MenuItem({ 
    label: "Delete",
    onClick: function() {
      map.graphics.remove(selected);
    }
  }));

  ctxMenuForGraphics.startup();

  dojo.connect(map.graphics, "onMouseOver", function(evt) {
    // We'll use this "selected" graphic to enable editing tools
    // on this graphic when the user click on one of the tools
    // listed in the menu.
    selected = evt.graphic;

    // Let's bind to the graphic underneath the mouse cursor           
    ctxMenuForGraphics.bindDomNode(evt.graphic.getDojoShape().getNode());
  });


  dojo.connect(map.graphics, "onMouseOut", function(evt) {
    ctxMenuForGraphics.unBindDomNode(evt.graphic.getDojoShape().getNode());
  });
}

/*****************
 * Helper Methods
 *****************/      

function getMapPointFromMenuPosition(box) {
  var x = box.x, y = box.y;

  switch(box.corner) {
    case "TR":
      x += box.w;
      break;
    case "BL":
      y += box.h;
      break;
    case "BR":
      x += box.w;
      y += box.h;
      break;
  }

  var screenPoint = new esri.geometry.Point(x - map.position.x, y - map.position.y);
  return map.toMap(screenPoint);
}

function selectCurrentLocation(){
	//Hack to get the Ids for the fields lan/lon
	var callerButton = window.opener.callerGisObject;
	var pgraphic;
	
	var shapeValue = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[4].value;
	var typeSelect = callerButton.parentNode.parentNode.getElementsByTagName("SELECT")[0];
	var typeText = typeSelect.options[typeSelect.selectedIndex].text; 
	var typeValue = typeSelect.options[typeSelect.selectedIndex].value; 
	var sms = new esri.symbol.PictureMarkerSymbol('/esrigis/structureTypeManager.do~action=displayIcon~id=' + typeValue, 32, 37);

	if(shapeValue == ""){
		//This takes an identifier for the element shape (the sibling of the Map button) and replaces shape with latitude and longitude or any other attribute we need
		var latitude = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1].value;
		var longitude = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2].value;

		var pt = new esri.geometry.Point(longitude,latitude,map.spatialReference);
		var transpt = esri.geometry.geographicToWebMercator(pt);
		var infoTemplate = new esri.InfoTemplate("");   
		var attr = {"Temp":"Temporal Attribute"};
		pgraphic = new esri.Graphic(transpt,sms,attr,infoTemplate);
		pgraphic.setAttributes( {
			  "Structure Type":typeText
			  });
		var exit = false;
	}
	else
	{
		var jsonObject = eval('(' + shapeValue + ')');
		if(jsonObject.geometry != null){ //If it's a complete Graphic object
			pgraphic = new esri.Graphic(jsonObject);
			if(jsonObject.symbol.style == "esriSMSCircle") //If it's a point, put the appropriate icon
			{
				pgraphic.setAttributes( {
					  "Structure Type":typeText
					  });
				pgraphic.setInfoTemplate(new esri.InfoTemplate(""));
				pgraphic.setSymbol(sms);
			}
			
		}
		else
		{
			pt = new esri.geometry.Point(jsonObject);
//			var transpt = esri.geometry.geographicToWebMercator(pt);
			var infoTemplate = new esri.InfoTemplate("");   
			var attr = {"Temp":"Temporal Attribute"};
			pgraphic = new esri.Graphic(pt,sms,attr,infoTemplate);
			pgraphic.setAttributes( {
				  "Structure Type":typeText
				  });
			
		}
	}
	map.graphics.add(pgraphic);
	
}


function selectLocationCaller(currentLocation){
	//Hack to get the Ids for the fields lan/lon from parent window
	var callerButton = window.opener.callerGisObject;
	//Lat
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1].value = esri.geometry.webMercatorToGeographic(currentLocation).y;
	//Long
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2].value = esri.geometry.webMercatorToGeographic(currentLocation).x;
	//Very experimental, storing the point as Json Object
	var shapeField = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[4]; //this is the "shape" field
	shapeField.value = JSON.stringify(currentLocation.toJson());
	window.close();
}

function initToolbar(map) {
    tb = new esri.toolbars.Draw(map);
    dojo.connect(tb, "onDrawEnd", addGraphic);
}

function addGraphic(geometry) {
  var type = geometry.type;
  if (type === "point" || type === "multipoint") {
    symbol = tb.markerSymbol;
  }
  else if (type === "line" || type === "polyline") {
    symbol = tb.lineSymbol;
  }
  else {
    symbol = tb.fillSymbol;
  }
    map.graphics.clear();
    map.graphics.add(new esri.Graphic(geometry, symbol));
  }

function selectLocationCallerShape(selectedGraphic){
	//Hack to get the Ids for the fields lan/lon from parent window
	var callerButton = window.opener.callerGisObject;
	//This takes an identifier for the element shape (the sibling of the Map button) and replaces shape with latitude and longitude
	//Very experimental, storing the point as Stringified Json Object
	var shapeField = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[4]; //this is the "shape" field
	shapeField.value = JSON.stringify(selectedGraphic.toJson());
	//Since this is a shape that we have stored, we'll wipe the lat/lon values to avoid confusion
	//The lat/lon fields can be used if they are known before hand and entered manually
	//For quicker data entry.
	//The function that renders this in the esrimap/mainmap.do draws it either coming from shape or lat/lon

	//Lat
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1].value = "";
	//Long
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2].value = "";
	//Except in the case of a point which has coordinates
	if(selectedGraphic.geometry.type === "point"){
		callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1].value = esri.geometry.webMercatorToGeographic(selectedGraphic.geometry).y;
		callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2].value = esri.geometry.webMercatorToGeographic(selectedGraphic.geometry).x;
	}
	
	window.close();
}

