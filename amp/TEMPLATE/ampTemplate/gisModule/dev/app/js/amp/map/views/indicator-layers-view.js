var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');


module.exports = Backbone.View.extend({

  initialize: function(options) {
    this.app = options.app;
    this.map = options.map;
    this.leafletLayerMap = {};
    this.listenTo(this.app.data.indicators, 'show', this.showLayer);
    this.listenTo(this.app.data.indicators, 'hide', this.hideLayer);
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
      console.log('lalala');
      var layerType = layer.get('type');
      if (layerType === 'joinBoundaries') {  // geojson
        loadedLayer = self.getNewGeoJSONLayer(layer);
      } else if (layerType === 'wms') {
        loadedLayer = self.getNewWMSLayer(layer);
      } else if (layerType === 'arcgis') {
        loadedLayer = self.getNewArcGISLayer(layer);
      } else {
        throw new Error('Map view for layer type not implemented. layer:', layer);
      }
      self.leafletLayerMap[layer.cid] = loadedLayer;
      self.map.addLayer(loadedLayer);
      self.trigger('addedToMap'); //TODO: Phil should i do this better?...
      // ...the main map view needs to know when layer is actually added to map.
    });

    layer.load();
  },

  hideLayer: function(layer) {
    var leafletLayer = this.leafletLayerMap[layer.cid];
    if (! leafletLayer) {
      throw new Error('cannot remove a layer that is not loaded????', layer);
    }
    this.map.removeLayer(leafletLayer);
  },

  getNewGeoJSONLayer: function(layer) {
    var featureValue,
        colour;

    return new L.geoJson(layer.get('geoJSON'), {
      style: function(feature) {
        featureValue = feature.properties.value;
        colour = layer.palette.colours.find(function(colour) {
          return colour.get('test')(featureValue);
        });
        if (! colour) {
          throw new Error('No colour matched for the value ' + featureValue);
        }
        return {
          color: colour.hex(),
          weight: 4,
          opacity: 0.9,
          fillOpacity: 0.6
        };
      }
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
