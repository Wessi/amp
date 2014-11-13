var _ = require('underscore');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');

// Note: doesn't have an explicit model.
// We can add one for clarity, but not needed.
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({
  url: '/rest/gis/settings',

  serialize: function() {
    var tmpJSON = {};
    this.each(function(setting) {
      if (setting.get('options')) {

        // if nothing selected yet, just take defaultId
        if (!setting.get('selected')) {
          setting.set('selected', setting.get('defaultId'));
        }

        // find the match.
        var match = _.findWhere(setting.get('options'), {id: setting.get('selected')});
        if (match) {
          tmpJSON[setting.id] = match.id;
        } else {
          console.warn('no match', setting.get('options'), {id: setting.get('selected')});
        }
      }
    });
    return tmpJSON;
  },

/*TODO: verify functional */
/*  serializeDeferred: function() {
    return this.load().then(function() {
      var tmpJSON = {};
      this.each(function(setting) {
        tmpJSON[setting.id] = _.findWhere(setting.get('options'), {id: setting.get('selected')}).id;
      });
      return tmpJSON;
    });
  },*/


  deserialize: function(jsonBlob) {
    var self = this;
    if (jsonBlob) {
      _.each(jsonBlob, function(v, k) {
        self.get(k).set('selected', v);
      });
    } else {
      this.each(function(setting) {
        setting.set('selected', setting.get('defaultId'));
      });
    }
  }

});
