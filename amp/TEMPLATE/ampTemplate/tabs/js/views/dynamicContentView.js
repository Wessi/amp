define([ 'marionette', 'text!views/html/dynamicContentTemplate.html', 'text!views/html/settingsDialogTemplate.html', 'models/settings',
		'business/settings/settingsManager', 'filtersWidget', 'business/filter/filterUtils', 'business/translations/translationManager',
		'jquery', 'jqueryui' ], function(Marionette, dynamicContentTemplate, settingsDialogTemplate, Settings, SettingsManager,
		FiltersWidget, FilterUtils, TranslationManager, jQuery) {

	var reportId;
	var reportFilters;
	var currency;
	var calendar;

	var DynamicContentView = Marionette.LayoutView.extend({
		template : _.template(dynamicContentTemplate),
		regions : {
			filters : '#dynamic-filters-region',
			legends : '#dynamic-legends-region',
			results : '#dynamic-results-region'
		},
		events : {
			'click #filters-button' : "clickFiltersButton",
			'click #settings-button' : "clickSettingsButton"
		},
		initialize : function(data) {
			reportId = data.id;
			reportFilters = data.filters;
			calendar = data.calendar;
			currency = data.currency;
		},
		clickFiltersButton : function() {
			console.log('clickFiltersButton');

			// We need to reset the widget because is shared between all
			// tabs.
			app.TabsApp.filtersWidget.reset({
				silent : true
			});

			var containerName = '#filters-container';
			var FilterDialogContainerView = Marionette.ItemView.extend({
				template : "<p></p>",
				render : function(model) {
					// Close floating accordion if needed.
					jQuery("#main-dynamic-content-region_" + reportId + " #filters-collapsible-area").accordion('option', 'active', false);

					console.log('filter widget loaded');
					// Convert report filters to filterwidget filters.
					var blob = FilterUtils.convertJavaFiltersToJS(reportFilters);
					app.TabsApp.filtersWidget.reset({
						silent : true
					});
					app.TabsApp.filtersWidget.deserialize(blob, {
						silent : true
					});

					// Show the dialog and fix the position.
					jQuery(containerName).show();
					jQuery(containerName).css('position', 'absolute');
					jQuery(containerName).css('top', 10);
				}
			});
			var filterDialog = new FilterDialogContainerView();
			filterDialog.render();
		},
		clickSettingsButton : function() {
			console.log('clickSettingsButton');
			var SettingDialogContainerView = Marionette.ItemView.extend({
				template : _.template(settingsDialogTemplate)
			});
			// TODO: Replace some of the default values with the ones
			// for this tab.
			var settings = new Settings();
			settings.set('selectedCurrency', currency);
			settings.set('selectedCalendar', calendar);
			// SettingsManager.setAvailableCategories(settings, reportId);
			var settingsDialog = new SettingDialogContainerView({
				model : settings
			});
			settingsDialog.render();
			jQuery(settingsDialog.el).dialog({
				modal : true,
				title : "Settings",
				width : 500
			});
			jQuery(".buttonify").button();
			TranslationManager.searchAndTranslate();
		},
		onShow : function(data) {
			// Create jQuery buttons.
			jQuery("#main-dynamic-content-region_" + reportId + " #filters-button").button({
				icons : {
					primary : "ui-icon-search"
				}
			});
			jQuery("#main-dynamic-content-region_" + reportId + " #save-button").button({
				icons : {
					primary : 'ui-icon-disk'
				}
			});
			jQuery("#main-dynamic-content-region_" + reportId + " #settings-button").button({
				icons : {
					primary : 'ui-icon-gear'
				}
			});
		}
	});

	return DynamicContentView;

});