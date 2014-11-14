/*/A
 * The GIS Data Engine
 * attaches to app.data
 */

var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');

var Filter = require('amp-filter/src/main');
var translator = require('../services/translator');

var Title = require('./title');
var SavedMaps = require('./collections/saved-maps-collection');
var Activities = require('./collections/activity-collection');
var Boundaries = require('./collections/boundary-collection');
var Indicators = require('./collections/indicator-collection');
var ProjectSitesMenu = require('./models/structures-menu-model'); /*a.k.a. structures */
var ADMClusters = require('./collections/adm-cluster-collection');
var HilightFundingCollection = require('./collections/hilight-funding-collection');
var Settings = require('./collections/settings-collection');

var ProjectSites = require('./collections/structures-collection'); /*a.k.a. structures */

var GISData = function() {
  this.initialize.apply(this, arguments);
};


_.extend(GISData.prototype, Backbone.Events, {

  layerEvents: ['show', 'hide', 'loaded', 'processed', 'sync'],

  initialize: function() {
    this.translator = translator;

    this.savedMaps = new SavedMaps([], {appData: this});


    /* stub filled in by Filters service */
    this.filter = new Filter({
      draggable: true,
      translator: this.translator
    });
    // forces filter to start loading list immediately. TODO: move to an option for filter init.
    this.filter.view._getFilterList();


    this.boundaries = new Boundaries();
    this.settings = new Settings();
    this.activities = new Activities([], {
      settings: this.settings,
      filter: this.filter,
      pageSize: 15,
      appData: this
    });

    this.projectSites = new ProjectSites([], {
      settings: this.settings,
      filter: this.filter,
      appData: this
    });

    this.projectSitesMenu = new ProjectSitesMenu([
      {}  // just the one model, all defaults
    ], {
      filter: this.filter,
      appData: this
    });


    this.indicators = new Indicators([], { boundaries: this.boundaries });

    this.admClusters = new ADMClusters([], {
      boundaries: this.boundaries,
      filter: this.filter,
      settings: this.settings
    });

    // TODO get these from the api
    this.hilightFundingCollection = new HilightFundingCollection([],
      { boundaries: this.boundaries, filter: this.filter, settings: this.settings });

    this.title = new Title({ data: this });

    // bubble indicator events on the data object
    this.listenTo(this.indicators, 'all', this.bubbleLayerEvents('indicator'));
    this.listenTo(this.hilightFundingCollection, 'all', this.bubbleLayerEvents('highlightFunding'));
    this.listenTo(this.projectSitesMenu, 'all', this.bubbleLayerEvents('structure'));
    this.listenTo(this.projectSites, 'all', this.bubbleLayerEvents('structure'));
    this.listenTo(this.admClusters, 'all', this.bubbleLayerEvents('adm-cluster'));
  },

  load: function(options) {
    var self = this;
    this.state = options.state;

    this._stateWait = new $.Deferred();
    if (this.savedMaps.length) {
      // a bit sketch....
      this.state.loadPromise.always(this._stateWait.resolve);
    } else {
      this._stateWait.resolve();
    }

    $.when(this.filter.loaded, this._stateWait).then(function() {
      self.boundaries.load();
      self.indicators.loadAll();

      self.admClusters.load();  // also special for now
      self.admClusters.attachListeners();
      self.hilightFundingCollection.load();  // also special for now
    });
  },

  bubbleLayerEvents: function(namespace) {
    /*
     * Bubble some events, including namespaced versions of the event.
     *
     * If an indicator triggers 'show', data will bubble it as both 'show' and 'show:indicator'.
     * All arguments are forwarded.
     */
    var namespacer = _.template('<%= ev %> <%= ev %>:' + namespace);

    return function(eventName) {

      if (_.contains(this.layerEvents, eventName)) {
        var args = _.tail(arguments);  // everything after eventName
        args.unshift(namespacer({ ev: eventName }));  // prepend the events to triger

        this.trigger.apply(this, args);
      }
    };
  },

  getAllVisibleLayers: function() {
    var layers = _.union(
      this.indicators.getSelected().value(),
      this.hilightFundingCollection.getSelected().value(),
      this.projectSitesMenu.getSelected().value(),
      this.admClusters.getSelected().value()
    );

    return _.chain(layers);
  }

});


module.exports = GISData;

