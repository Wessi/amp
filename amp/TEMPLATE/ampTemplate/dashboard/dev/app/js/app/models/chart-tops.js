var param = require('jquery').param;
var _ = require('underscore');
var ChartModel = require('./chart-model-base');


module.exports = ChartModel.extend({

  defaults: {
    limit: 5,
    title: ''
  },

  _prepareTranslations: function() {
    var topBaseLanguage = {};

    this.chartName = 'amp.dashboard:chart-' + this.get('name').replace(/ /g, '');

    /*
     * TODO: load all the localizations in this chart's namespace to this array
     * from initial-translation-request.json -- For now just hardcode the two sorts.
     */
    if (this.get('name') === 'Top Regions') {
      topBaseLanguage[this.chartName + '-DistrictUndefined'] = 'Districts: Undefined';
    }
    topBaseLanguage[this.chartName + '-others'] = 'Others';

    this.localizedTopChart = this.app.translator.translateList(topBaseLanguage)
      .done(_(function(localizedTopChartKeyVal) {
        this.localizedLookup = localizedTopChartKeyVal;
      }).bind(this));
  },

  parse: function(data) {
	this.set('title', data.title);
	  
    if (!this.localizedLookup) {
      // we can't procede if we don't have translations yet :(
      // this code should now be unreachable, but y'never know...
      this.app.report('Loading error', [
      'Translations for the application were not loaded before rendering']);
    }

    this.localizedOthers = this.localizedLookup[this.chartName + '-others'];

    var values = _(data.values.slice()).map(function(v) {
      var cleanName = v.name.replace(/[ :.]/g, '');
      var localizedName = this.localizedLookup[this.chartName + '-' + cleanName] || v.name;

      return {
        x: localizedName,
        y: v.amount
      };
    }, this);

    if (data.maxLimit > values.length) {
      values.push({
        x: this.localizedOthers,
        y: data.total -  // total minus the sum of what we have
          _.chain(values).pluck('y').reduce(function(l, r) { return l + r; }, 0).value(),
        color: '#777',
        special: 'others'
      });
    }

    data.processed = [{values: values}];
    return data;
  },

  fetch: function(options) {
    options = _.defaults(
      options || {},
      { url: this.url + '?' + param(this.pick('limit')) });
    return ChartModel.prototype.fetch.call(this, options);
  }

});
