var Backbone = require('backbone');
var _ = require('underscore');

module.exports = Backbone.Model.extend({
  idAttribute: 'Activity Id',
  defaults: {
    name: 'Untitled Activity',
    ampUrl: '/aim/viewActivityPreview.do~public=true',
    matchesFilters: {
      //'Donor Id': ''
    }
  },

  initialize: function() {
    this._joinComplete = this._joinFilters();
  },


  getJoinedVersion: function() {
    return this._joinComplete;
  },

  //created this because the joins were being overriden after the initialize, not sure how/why
  // ideally track down why and this won't be needed
  tempDirtyForceJoin: function() {
    return this._joinFilters();
  },

  _joinFilters: function() {
    var self = this;

    return this.collection.appData.filter.getAllFilters().then(function(allFilters) {
      var matchesFilters = self.attributes.matchesFilters;
      if (allFilters && allFilters.columnFilters && matchesFilters) {
        _.each(matchesFilters, function(v, k) {
          //make sure it's a valid filter
          if (allFilters.columnFilters[k]) {
            //iterate over ids.
            _.each(matchesFilters[k], function(id, index) {
              var matched = _.find(allFilters.columnFilters[k], function(filter) {return filter.id == id; });
              if (matched) {
                matchesFilters[k][index] = matched;
              }
            });
          }
        });
        self.set('matchesFilters', matchesFilters);
      } else {
        console.warn('drs I need make a deferred...');
      }
    });
  },

  parse: function(data) {
    // split matchesFilters
    if (data.matchesFilters) {
      _.each(data.matchesFilters, function(v, k) {
        data.matchesFilters[k] = data.matchesFilters[k].split(',');
      });
    }
    return data;
  },

  // Use to hook in before template calls.
  toJSON: function() {
    var json = _.clone(this.attributes);

    json.donorNames = this._getDonorNames();
    json.sectorNames = this._getSectorNames();

    return json;
  },

  _getDonorNames: function() {
    var matchesFilters = this.attributes.matchesFilters;
    if (matchesFilters && matchesFilters['Donor Id']) {
      if (matchesFilters['Donor Id'].length > 1) {
        return 'Multiple';
      } else if (matchesFilters['Donor Id'][0] && matchesFilters['Donor Id'][0].attributes) {
        return matchesFilters['Donor Id'][0].get('name');
      }
    }
    return '';
  },


  _getSectorNames: function() {
    var matchesFilters = this.attributes.matchesFilters;
    if (matchesFilters && matchesFilters['Primary Sector Id']) {
      if (matchesFilters['Primary Sector Id'].length > 1) {
        return 'Multiple';
      } else if (matchesFilters['Primary Sector Id'][0] && matchesFilters['Primary Sector Id'][0].attributes) {
        return matchesFilters['Primary Sector Id'][0].get('name');
      }
    }
    return '';
  }
});
