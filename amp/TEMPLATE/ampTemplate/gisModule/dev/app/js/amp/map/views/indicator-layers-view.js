var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');
// var Colors = require('../collections/layer-colors-collection');

module.exports = Backbone.View.extend({

  initialize: function(options) {
    this.app = options.app;
    this.map = options.map;
    this.leafletLayerMap = {};
    this.listenTo(this.app.display.indicators, 'show', this.showLayer);
    this.listenTo(this.app.display.indicators, 'hide', this.hideLayer);
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

    layer.indicator.load().then(function ShowLoadedLayer() {
      if (layer.indicator.has('geoJSON')) {
        loadedLayer = self.showNewGeoJSONLayer(layer);
      } else {
        throw new Error('Map view for layer type not implemented. layer:', layer);
      }
      self.leafletLayerMap[layer.cid] = loadedLayer;
      self.map.addLayer(loadedLayer);
    });
  },

  hideLayer: function(layer) {
    var leafletLayer = this.leafletLayerMap[layer.cid];
    if (! leafletLayer) {
      throw new Error('cannot remove a layer that is not loaded????', layer);
    }
    this.map.removeLayer(leafletLayer);
  },

  showNewGeoJSONLayer: function(layer) {
    return new L.geoJson(layer.indicator.get('geoJSON'), {
      onEachFeature: function() { console.log('feature', arguments); }
    });
  }

});
