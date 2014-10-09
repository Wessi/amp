var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var BaseControlView = require('../../base-control/base-control-view');
var Template = fs.readFileSync(__dirname + '/../templates/settings-template.html', 'utf8');
var OptionTemplate = fs.readFileSync(__dirname + '/../templates/settings-option-template.html', 'utf8');



module.exports = BaseControlView.extend({
  id: 'tool-settings',
  title:  'Settings',
  iconClass:  'ampicon-settings',
  description:  '',

  template:  _.template(Template),
  templateOption: _.template(OptionTemplate),
  events:{
    'change select': 'optionChanged'
  },

  initialize: function() {
    BaseControlView.prototype.initialize.apply(this, arguments);
  },

  render: function() {
    var self = this;
    BaseControlView.prototype.render.apply(this);

    // TODO: make all/most api requests send the serialized settings...
    // TODO: register for state serialization

    self.$('.content').html(this.template({title: this.title}));

    // add content
    this.app.data.settings.load().then(function() {
      self.$('.settings').html('');
      self.app.data.settings.each(function(setting) {
        self.$('.settings').append(self.templateOption(setting.toJSON()));
      });
    });

    return this;
  },

  // update the settings collection with the latest selected one.
  optionChanged: function(evt) {
    var settingID = $(evt.currentTarget).attr('id');
    var selectedID = $(evt.currentTarget).val();
    var setting = this.app.data.settings.get(settingID);
    if (setting) {
      setting.set('selected', selectedID);
    } else {
      console.error('setting not found...');
    }
  }
});
