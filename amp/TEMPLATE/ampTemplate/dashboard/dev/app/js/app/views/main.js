var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');

var StateLoadError = require('amp-state/index').StateLoadError;

var Controls = require('./controls');
var ChartsView = require('./charts');
var Charts = require('../models/charts-collection');
var boilerplate = require('amp-boilerplate');
var TopsChart = require('../models/chart-tops');
var PredictabilityChart = require('../models/chart-aid-predictability');
var FundingTypeChart = require('../models/chart-funding-type');

var template = _.template(fs.readFileSync(
  __dirname + '/../templates/main.html', 'UTF-8'));
var modalTemplate = _.template(fs.readFileSync(
  __dirname + '/../templates/modal.html', 'UTF-8'));

var EnabledChartsCollection = require('../models/enabled-charts-collection');

module.exports = BackboneDash.View.extend({

  initialize: function(options) {
    this.app = options.app;

    // try to load an initial state from the url
    try {
      this.app.state.urlMaybeLoad();
    } catch (e) {
      if (e instanceof StateLoadError) {
        this.app.report('Could not load saved dashboard',
          ['If you are trying to load a shared link, please make sure the entire URL was copied']);
        this.app.url.hash('');  // clear the bad saved-state hash
      } else {
        throw e;
      }
    }
    
    this.app.settings.load();  // maybe should go in render or something
                               // but we already do other fetches on init so...
    this.controls = new Controls({ app: this.app });

    // AMP-19545: We instantiate the collection of enabled charts (from FM) and use it to enable or not each chart.
    var enabledChartsFM = new EnabledChartsCollection();
    enabledChartsFM.fetchData();
    var col = [];
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Top Donors'})) {
    	col.push(new TopsChart(
  	          { name: 'Top Donor Agencies' },
  	          { app: this.app, url: '/rest/dashboard/tops/do' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Top Donor Group'})) {
    	col.push(new TopsChart(
  	          { name: 'Top Donor Groups' },
  	          { app: this.app, url: '/rest/dashboard/tops/dg' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Top Regions'})) {
    	col.push(new TopsChart(
  	          { name: 'Top Regions' },
	          { app: this.app, url: '/rest/dashboard/tops/re' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Top Sectors'})) {
    	col.push(new TopsChart(
  	          { name: 'Top Sectors' },
	          { app: this.app, url: '/rest/dashboard/tops/ps' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Aid Predictability'})) {
    	col.push(new PredictabilityChart(
  	          { name: 'Aid Predictability' },
	          { app: this.app, url: '/rest/dashboard/aid-predictability' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Funding Type'})) {
    	col.push(new FundingTypeChart(
  	          { name: 'Funding Type' },
	          { app: this.app, url: '/rest/dashboard/ftype' }));
    }
       
    this.charts = new ChartsView({
      app: this.app,
      collection: new Charts(col, { app: this.app })
    });
    //auto-renders the layout
    this.headerWidget = new boilerplate.layout(
      {
        caller: 'DASHBOARD'
	  });
    var self = this;
    this.listenTo(this.headerWidget.menu, 'switchLanguage', function(lng) {
    	self.app.translator.setLanguage(lng.language).then(function() {
            self.app.translator.translateDOM(document); 
         });
    	
    });
  },

  render: function() {
    this.$el.html(template());
    this.$('.container').html([
      this.controls.render().el,
      this.charts.render().el,
    ]);
    return this;
  },

  modal: function(title, options) {
    options = _({
      title: title,
      id: _.uniqueId('modal')
    }).extend(options);
    this.$el.parent().append(modalTemplate({m: options}));
    var thisModal = this.$el.parent().find('#' + options.id);
    if (options.bodyEl) { thisModal.find('.modal-body').html(options.bodyEl); }
    thisModal.modal();
    return thisModal[0];  // the actual DOM element
  }

});
