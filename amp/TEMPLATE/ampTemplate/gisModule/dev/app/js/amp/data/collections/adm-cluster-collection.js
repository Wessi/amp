var Backbone = require('backbone');
var _ = require('underscore');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var ADMClusterModel = require('../models/adm-cluster-model');

/* Backbone Collection ClusterPointsByAdmin aka cluster */
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({

  model:  ADMClusterModel,
  url: '/rest/gis/clusters',
  defaultSelected: 'adm-1',   // if set, then the cluster will auto select this adm after sync.

  initialize: function(models, options) {
    // TODO: probably pass app or data here instead?
    this.boundaries = options.boundaries;
    this.filter = options.filter;

    this.listenTo(this, 'sync', this._setDefault);
  },

  parse: function(data) {
    data = _.sortBy(data, function(regionLevel) {
      return regionLevel.id ;
    });
    _.each(data, function(regionLevel) {
      regionLevel.id = regionLevel.adminLevel;
      regionLevel.value = regionLevel.adminLevel;
      regionLevel.title = regionLevel.title;
    });
    return data;
  },

  getSelected: function() {
    return this.chain()
      .filter(function(model) { return model.get('selected'); });
  },

  _setDefault: function() {
    if (this.defaultSelected) {
      // default adm-1 Clusters to on.
      var defaultCluster = this.findWhere({adminLevel: this.defaultSelected});
      if (defaultCluster) {
        defaultCluster.set('selected', true);
      }
    }
  }

});
