var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');

var Template = fs.readFileSync(__dirname + '/../tree/node-template.html', 'utf8');


var TreeNodeView = Backbone.View.extend({

  tagName: 'li',
  className: 'parent_li',

  //TODO: debug after usability testing, currently setting in addUIListeners
  // won't work on second use of widget.
  // events: {
  //   'click .selectable': 'select',
  //   'click  .toggle-nav': 'clickName'
  // },

  template: _.template(Template),

  initialize:function() {
  },


  render:function(model) {
    this.model = model;
    this.$el.html(this.template(model.toJSON()));
    this.renderChildren();

    return this;
  },

  renderChildren:function() {
    var ul = $('<ul>');
    this.$el.append(ul);

    var children = this.model.get('children');
    if (!children.isEmpty()) {
      children.each(function(child) {
        var tmpView = new TreeNodeView();
        ul.append(tmpView.render(child).$el);
      });
    } else {
      this.$('.expanded').remove();
      this.$('> .node > .toggle-nav > .count').text('');
    }

    this._addModelListeners();
    this._addUIListeners();

    this._updateSelection();
    this._updateExpanded(ul);
  },

  _addModelListeners:function() {
    var self = this;

    //Add model listeneres
    this.model.on('change:selected', function() {
      self._updateSelection();
    });

    this.model.on('change:expanded', function() {
      self._updateExpanded();
    });

    this.model.on('change:numSelected', function() {
      self._updateCountUI();
    });

  },

  _addUIListeners:function() {
    var self = this;
    this.$('> .node > .selectable').on('click', function() {
      self.clickBox();

    });
    this.$('> .node > .toggle-nav').on('click', function() {
      self.clickName();
    });
  },

  _updateSelection:function() {
    this._updateCheckboxFill();
  },

  _updateCountUI:function() {
    if (!this.model.get('children').isEmpty()) {
      this.$('> .node > .toggle-nav > .count').text(
        '(' + this.model.get('numSelected') + ' / ' + this.model.get('numPossible') + ')');
      this._updateCheckboxFill();
    }
  },

  // For updating non-leaf nodes
  _updateCheckboxFill:function() {
    if (!this.model.get('children').isEmpty()) {
      if (this.model.get('numSelected') > 0) {
        if (this.model.get('numSelected') < this.model.get('numPossible')) {
          this.$('> .node > .selectable').addClass('half-fill');
          this.$('> .node > .selectable').removeClass('selected');
        } else {
          this.$('> .node > .selectable').removeClass('half-fill');
          this.$('> .node > .selectable').addClass('selected');
        }
      } else if (this.model.get('numSelected') === 0) {
        this.$('> .node > .selectable').removeClass('half-fill');
        this.$('> .node > .selectable').removeClass('selected');
      }
    } else { // else leaf node
      if (this.model.get('selected')) {
        this.$('> .node > .selectable').addClass('selected');
      } else {
        this.$('> .node > .selectable').removeClass('selected');
      }
    }
  },

  _updateExpanded:function(ul) {
    var iElement = this.$('> .node > .toggle-nav > .expanded');
    if (this.model.get('expanded')) {
      this.expand();
      iElement.text('-');
      iElement.addClass('open').removeClass('closed');
    } else {
      this.collapse();

      // to run on first time...need to use ul, since el is not on DOM yet
      if (ul) {
        ul.find('> li').hide();
      }

      iElement.text('+');
      iElement.addClass('closed').removeClass('open');
    }
  },


  clickBox:function() {
    this.model.set('selected', !this.model.get('selected'), {propagation: true});
  },


  clickName:function() {
    // if we have children expand
    if (!this.model.get('children').isEmpty()) {
      this.model.set('expanded', !this.model.get('expanded'));
    } else {
      // leaf node, so pretend the clicked on the box
      this.clickBox();
    }
  },


  collapse:function() {
    var children = this.$el.find(' > ul > li');
    children.hide('fast');

  },

  expand:function() {
    var children = this.$el.find(' > ul > li');
    children.show('fast');
  }

});

module.exports = TreeNodeView;