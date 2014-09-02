var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/legend-item-indicator-wms.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  render: function() {
    var base = this.model.get('link');
    var qs = '?request=GetLegendGraphic&version=1.1.1&format=image/png%26layer=';
    var wmsLayer = this.model.get('layer');
    this.$el.html(this.template(_.extend({}, this.model.toJSON(), {
      status: 'loaded',
      legendSrc: base + qs + wmsLayer
    })));

    return this;
  }

});
