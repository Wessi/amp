var param = require('jquery').param;
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');


module.exports = BackboneDash.Model.extend({

  defaults: {
    adjtype: 'ac',
    limit: 5
  },

  initialize: function(attrs, options) {
    this.app = options.app;
    this.url = options.url;

    this._prepareTranslations();
  },

  _prepareTranslations: function() {
    var self = this;
    var topBaseLanguage = {};

    /* Prepare the translations for the chart */
    var chartName = ['amp.dashboard:chart-', this.get('name').replace(/ /g, ''), '-'].join('');

    /*
     * TODO: load all the localizations in this chart's namespace to this array
     * from initial-translation-request.json -- For now just hardcode the two sorts.
     */
    if (this.get('name') === 'Top Regions') {
      topBaseLanguage[chartName + 'DistrictUndefined'] = 'Districts: Undefined';
    }
    topBaseLanguage[chartName + 'others'] = 'Others';

    this.localizedTopChart = this.app.translator.translateList(topBaseLanguage).then(
      function(localizedTopChartKeyVal) {
        self.localizedLookup = localizedTopChartKeyVal;
      });
  },

  parse: function(data) {
    var self = this;

    var chartName = ['amp.dashboard:chart-', this.get('name').replace(/ /g, ''), '-'].join('');
    var localizedOthers = self.localizedLookup[chartName + 'others'];

    var values = _(data.values.slice()).map(function(v) {

      var cleanName = v.name.replace(/[ :.]/g, '');
      var localizedName = v.name;
      if (self.localizedLookup[chartName + cleanName]) {
        localizedName = self.localizedLookup[chartName + cleanName];
      }

      return {
        x: localizedName,
        y: v.amount
      };
    });

    // make sure we don't have any duplicate keys... nvd3 pukes on those
    if (_(_(values).pluck('x')).uniq().length < values.length) {
      this.app.report('Data Error',
        ['The data for ' + this.get('name') + ' was inconsistent due to duplicate keys',
        'The chart will be shown, but it may have errors or other issues as a result.']);
    }

    if (data.maxLimit > values.length) {
      values.push({
        x: localizedOthers,
        y: data.total -  // total minus the sum of what we have
          _.chain(values).pluck('y').reduce(function(l, r) { return l + r; }, 0).value(),
        color: '#777'
      });
    }

    data.processed = [{values: values}];
    return data;
  },

  fetch: function(options) {
    options = _.defaults(
      options || {},
      { url: this.url + '?' + param(this.pick('adjtype', 'limit')) });
    return BackboneDash.Model.prototype.fetch.call(this, options);
  }

});
