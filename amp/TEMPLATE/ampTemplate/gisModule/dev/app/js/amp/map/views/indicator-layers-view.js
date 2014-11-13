var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');


module.exports = Backbone.View.extend({

  initialize: function(options) {
    this.app = options.app;
    this.map = options.map;
    this.admClustersLayersView = options.admClustersLayersView;
    this.leafletLayerMap = {};
    this.listenTo(this.app.data.indicators, 'show', this.showLayer);
    this.listenTo(this.app.data.indicators, 'hide', this.hideLayer);

    this.listenTo(this.app.data.hilightFundingCollection, 'hide', this.hideLayer);
    this.listenTo(this.app.data.hilightFundingCollection, 'sync', this.refreshLayer);
  },

  // removes layer, then shows it. Important for layers whos content changes.
  refreshLayer: function(layer) {
    var loadedLayer = this.leafletLayerMap[layer.cid];

    if (loadedLayer) {
      this.map.removeLayer(loadedLayer);
    }
    this.showLayer(layer);
  },

  showLayer: function(layer) {
    var self = this,
        loadedLayer = this.leafletLayerMap[layer.cid];
    if (loadedLayer === 'loading') {
      console.warn('tried to show a layer that is currently loading (be patient :) )');
      return;
    } else if (typeof loadedLayer === 'undefined') {
      this.leafletLayerMap[layer.cid] = 'loading';  // will be replaced in time...
    }

    layer.loadAll().done(function() {
      var layerType = layer.get('type');
      if (layerType === 'joinBoundaries') {  // geojson
        loadedLayer = self.getNewGeoJSONLayer(layer);
      } else if (layerType === 'wms') {
        loadedLayer = self.getNewWMSLayer(layer);
      } else if (layerType === 'arcgis' || layerType === 'Indicator Layers') {
        loadedLayer = self.getNewArcGISLayer(layer);
      } else {
        console.warn('Map view for layer type not implemented. layer:', layer);
      }
      self.leafletLayerMap[layer.cid] = loadedLayer;

      // only add it to the map if is still selected.
      if (layer.get('selected')) {
        self.map.addLayer(loadedLayer);
        if (loadedLayer.bringToBack) {
          loadedLayer.bringToBack();
          //TODO: drs, very dirty way of hiding boundaries so they don't hijack click events
          // I need to pull out boundaries into own view.
          self.admClustersLayersView.moveBoundaryBack();
        }
        self.trigger('addedToMap'); //TODO: better way. needed to let map bring projectSites to front.
      }
    });

    layer.load();
  },

  hideLayer: function(layer) {
    var leafletLayer = this.leafletLayerMap[layer.cid];
    if (!leafletLayer) {
      throw new Error('cannot remove a layer that is not loaded????', layer);
    }

    this.map.removeLayer(leafletLayer);
  },

  getNewGeoJSONLayer: function(layer) {
    var featureValue,
        colour;
console.log('make it new! getNewGeoJSONLayer');
    return new L.geoJson(layer.get('geoJSON'), {
      style: function(feature) {
        featureValue = feature.properties.value;
        // sets colour for each polygon
        colour = layer.palette.colours.find(function(colour) {
          return colour.get('test').call(colour, featureValue);
        });
        if (!colour) {

          colour = {hex: function() {return '#354';}};
          console.warn('No colour matched for the value ' + featureValue);
          //throw new Error('No colour matched for the value ' + featureValue);
        }
        return {
          color: colour.hex(),
          weight: 2,
          opacity: 0.9,
          fillOpacity: 0.6
        };
      },
      onEachFeature: this.tmpFundingOnEachFeature
    });
  },

  // used to hilight the geojson layer on click, show popup, and unhilight after.
  tmpFundingOnEachFeature: function(feature, layer) {
    // Add popup
    if (feature && feature.properties) {
      // TODO: drs append currency name and format value.
      layer.bindPopup('<strong>' + feature.properties.name + '</strong>' +
                      '<br/>$' + feature.properties.value);
    }

    // hilight and unhilight the area when a user clicks on them..
    layer.on('popupopen', function() {
      layer.setStyle({
        fillOpacity: 1.0
      });
    });
    layer.on('popupclose', function() {
      layer.setStyle({
        fillOpacity: 0.6
      });
    });
  },

  getNewWMSLayer: function(layer) {
    return L.tileLayer.wms(layer.get('link'), {
      layers: layer.get('layer'),
      // TODO: should these details be obtained from the API?
      format: 'image/png',
      transparent: true,
      opacity: 0.75
    });
  },

  getNewArcGISLayer: function(layer) {
    return layer.esriLayer;
  }

});
