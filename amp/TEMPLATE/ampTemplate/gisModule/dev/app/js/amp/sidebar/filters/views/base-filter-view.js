var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');

var Backbone = require('backbone');
var TitleTemplate = fs.readFileSync(__dirname + '/../templates/subfilter-title-template.html', 'utf8');
var ContentTemplate = fs.readFileSync(__dirname + '/../templates/filters-content-template.html', 'utf8');

require('jquery-ui/draggable'); // ?not sure if working...

// Parent base view for fitlers.
module.exports = Backbone.View.extend({

  className: 'filter-type',

  titleTemplate: _.template(TitleTemplate),
  contentTemplate: _.template(ContentTemplate),

  initialize:function(options) {
    this.app = options.app;
  },

  renderFilters: function() {

  },

  renderTitle: function() {
    var self = this;

    this.titleEl = this.titleTemplate(this.model.toJSON());
    this.$titleEl = $(this.titleEl).on('click', function() {
      $(this).siblings().removeClass('active');
      $(this).addClass('active');

      self.$el.html('');
      self.renderFilters();
    });
    return this;
  }

});
