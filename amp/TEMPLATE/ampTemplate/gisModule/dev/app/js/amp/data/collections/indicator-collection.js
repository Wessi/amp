var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var JoinIndicator = require('../models/indicator-join-model');
var ArcGISDynamicLayerIndicator = require('../models/indicator-arcgis-dynamicLayer-model');
var WMSIndicator = require('../models/indicator-wms-model');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var IndicatorLayerLocalStorage = require('../indicator-layer-localstorage');
var StringUtil = require('../../../libs/local/string-util');

/* Backbone Collection IndicatorLayers (RENAME FILE) */
module.exports = Backbone.Collection.extend({

  url: '/rest/gis/indicator-layers',
  JOIN_BOUNDARIES_PREFIX:'J',
  model: function(attrs) {
    var typeName = attrs.type;

    switch (typeName) {
      case 'joinBoundaries':
        return new JoinIndicator(attrs);
      case 'wms':
        return new WMSIndicator(attrs);
      case 'arcgis':
      case 'Indicator Layers':
        return new ArcGISDynamicLayerIndicator(attrs);
      default:
        console.error('Unrecognized indicator type. Check parse function.: ' + attrs.type);
        return new Backbone.Model();
    }
  },

  initialize: function(models, options) {
    this.boundaries = options.boundaries;
    this.settings = options.settings;
  },

  loadAll: function() {
    var self = this;
    var deferred = $.Deferred();
    this.load().then(function() {
      self.url = '/rest/gis/indicators';
      self.fetch({remove: false}).then(function() {
        deferred.resolve();
      });
    });

    return deferred;
  },
  load: function(){
	  this.url = '/rest/gis/indicator-layers';
	  return this.fetch()
  },
  loadFromLocalStorage: function(data){	 
	  if(this.url === '/rest/gis/indicators'){
		  IndicatorLayerLocalStorage.cleanUp();
		  var layers = IndicatorLayerLocalStorage.findAll();	  
		  layers.forEach(function(localLayer){
			  // Here we check if this local indicator can do gap analysis or not.
			  $.ajax({
				  url: '/rest/gis/can-do-gap-analysis', 
				  async: false, 
				  data: {indicatorTypeId: localLayer.indicatorTypeId, admLevelId: localLayer.admLevelId} })
			  .done(function(data) {
				  localLayer.canDoGapAnalysis = data.canDoGapAnalysis;
			  });			  
			  data.push(localLayer);
		  });
	  }	  
  },
  parse: function(data) {	
    var self = this;
    self.loadFromLocalStorage(data);
    var parsedData = data;
    parsedData = _.filter(data, function(layer) {
      switch (layer.type) {
        case 'joinBoundaries':
        case 'wms':
        case 'arcgis':
        case 'Indicator Layers':
          return true;
        default:
      }

      // this is a custom one. API is a bit messy so we do fair bit of manual work.
      if (layer.colorRamp) {
    	 layer.id = self.JOIN_BOUNDARIES_PREFIX + layer.id;
    	 self.settings.load().then(function() {
    	    
    	   layer.title = StringUtil.getMultilangString(layer,'name', self.settings);
    	   layer.description = StringUtil.getMultilangString(layer,'description', self.settings);  
    	   layer.unit = StringUtil.getMultilangString(layer,'unit', self.settings);
    	 });   	 
        layer.type = 'joinBoundaries';
        //debugger
        layer.adminLevel = self._magicConversion(layer.admLevelName);
        layer.tooltip = self._createTooltip(layer); 
        layer.classes = layer.numberOfClasses;        
        return true;
      }

      return false;
    });

    return parsedData;
  },
  getSelected: function() {
    return this.chain()
      .filter(function(model) { return model.get('selected'); });
  },

  _magicConversion: function(textAdm) {
    var magicWords = {
      Country: 'adm-0',
      Region: 'adm-1',
      Zone: 'adm-2',
      District: 'adm-3'
    };

    return magicWords[textAdm];
  },
  _createTooltip: function(obj){
	     var tooltip  = '';
	     if(obj.description){
	       tooltip += obj.description + '. ' ;
	     }
	     
	     if(obj.createdOn){
	      tooltip += '&#013; Created on ' + obj.createdOn + '. ';
	     }
	     if(obj.createdBy){
		      tooltip += '&#013; Created by ' + obj.createdBy + '. ';
		 }
	     return tooltip;
}

});
