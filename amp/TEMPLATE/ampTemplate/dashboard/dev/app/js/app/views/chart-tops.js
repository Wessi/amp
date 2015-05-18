var d3 = require('d3-browserify');
var ChartViewBase = require('./chart-view-base');


module.exports = ChartViewBase.extend({

  uiDefaults: {
    adjtype: 'FAKE',
    view: 'bar'
  },

  downloadChartOptions: {
    trimLabels: false
  },

  getTTContent: function(context) {
	var ofTotal = app.translator.translateSync("amp.dashboard:of-total","of total");
    return {tt: {
      heading: context.x.raw,
      bodyText: '<b>' + context.y.fmt + '</b> ' + this.model.get('currency'),
      footerText: '<b>' + d3.format('%')(context.y.raw / this.model.get('total')) + '</b>&nbsp<span>' + ofTotal + '</span>'
    }};
  },

  chartClickHandler: function(context) {
    // clicking on the "others" bar loads five more.
    if (context.data[context.series.index]
               .values[context.x.index].special === 'others') {
      this.model.set('limit', this.model.get('limit') + 5);
      if (this.model.get('limit') > 10) {  // also make the chart bigger if lots of bars are shown
        this.model.set('big', true);
      }
    }
  }


});
