var $ = require('jquery');
var BaseFilterModel = require('../models/base-filter-model');


module.exports = BaseFilterModel.extend({

  //TODO: serialize-deserialize functions

  initialize:function(options) {
    BaseFilterModel.prototype.initialize.apply(this, [options]);
    this.set({
      title: 'Years',
      selectedStart: null,
      selectedEnd: null,
      // range is provided by api, but will fallback to this if ot provided, or set to -1
      startYear: '01/01/1961',
      endYear: '31/12/2015'
    });
    this.set('_loaded', $.Deferred());
  },

  parse:function(data) {
    if (!data.startYear || data.startYear === -1) {
      data.startYear = this.attributes.startYear;
    }

    if (!data.endYear || data.endYear === -1) {
      data.endYear = this.attributes.endYear;
    }


    this.get('_loaded').resolve();
    return data;
  },

  serialize: function() {
    return {
      startYear: this.get('selectedStart'),
      endYear: this.get('selectedEnd')
    };
  },

  deserialize: function(obj) {
    this.set('selectedStart', obj.startYear);
    this.set('selectedEnd', obj.endYear);
  }
});
