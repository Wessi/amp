var BaseFilterModel = require('../models/base-filter-model');


module.exports = BaseFilterModel.extend({

  initialize: function() {
    BaseFilterModel.prototype.initialize.apply(this);
    this.set({
        title: 'Years',
        selectedStart: 1990,
        selectedEnd: 2014,
        startYear:1960,
        endYear:2015
      });
  }

});
