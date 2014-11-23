var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');


module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({

  initialize: function() {
    // TODO: factor this behavior into an indicator base class
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });
  },

  fetch: function() {
    if (!_(this).has('_esriLoaded')) {
      this._esriLoaded = new Deferred();
      this.esriLayer = new L.esri.DynamicMapLayer(this.get('link'), {
        opacity: 0.5
      });
      this._esriLoaded.resolveWith(this, [this, this.esriLayer]);
    }
    return this._esriLoaded.promise();
  },

  getLegend: function() {
    if (!this._loadedLegend) {
      this._loadedLegend = $.ajax({
        url: this.get('link') + '/legend',
        data:{f: 'pjson'},
        dataType: 'jsonp'
      });
    }

    return this._loadedLegend;
  }

});

