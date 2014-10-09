var _ = require('underscore');
var Backbone = require('backbone');
var ProjectSiteModel = require('../models/structure-model');
//var ActivityCollection = require('../collections/activity-collection');

/* ProjectSites a.k.a Structures collection */
module.exports = Backbone.Collection.extend({

  url: '/rest/gis/structures',

  model: ProjectSiteModel,

  fetch: function(options) {
    options = _.defaults((options || {}), {
      type: 'POST',
      data:'{}'
    });

    /*TODO implement manual caching */

    /* Be sure to be matching the Backbone.Collection or Backbone.Model prototype below */
    return Backbone.Collection.prototype.fetch.call(this, options);
  },

  parse: function(response) {
    //fetch ALL activities
    //window.app.data.activities.fetch();

    //get the list of unique activities for the structures
    var activeActivityList = _.chain(response.features)
      .pluck('properties')
      .pluck('activity')
      .flatten()
      .unique()
      .value();

    /* TODO(thadk): find a more encapsulated path to communicate this promise to model's map function */
    window.app.data.relevantActivitesFetch = window.app.data.activities.getActivities(activeActivityList);

    return response.features;
  }

});
