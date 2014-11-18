/*https://gist.github.com/jonnyreeves/2474026*/
/*https://github.com/icereval/backbone-documentmodel*/

define([ 'marionette', 'models/content', 'models/legend', 'views/dynamicContentView', 'text!views/html/filtersWrapperTemplate.html',
		'text!views/html/filtersItemTemplate.html', 'models/tab', 'text!views/html/invisibleTabLinkTemplate.html',
		'text!views/html/legendsTemplate.html', 'business/grid/gridManager', 'business/translations/translationManager',
		'business/filter/filterUtils', 'jquery', 'jqueryui' ], function(Marionette, Content, Legend, DynamicContentView, filtersTemplate,
		filtersItemTemplate, Tab, invisibleTabLinkTemplate, legendsTemplate, gridManager, TranslationManager, FilterUtils, jQuery) {

	"use strict";

	function TabEvents() {
		// Constructor
		function TabEvents() {
		}
	}

	// Some private method.
	function putAnimation() {
		return '<span><img src="/TEMPLATE/ampTemplate/tabs/css/images/ajax-loader.gif"/></span>';
	}

	function retrieveTabContent(selectedTabIndex) {
		app.TabsApp.currentTab = app.TabsApp.tabItemsView.collection.models[selectedTabIndex];
		app.TabsApp.currentId = app.TabsApp.currentTab.get('id');

		// Create a region where the dynamic content will be rendered
		// inside
		// the tab.
		var regionsName = '#main-dynamic-content-region_' + app.TabsApp.currentTab.get('id');
		app.TabsApp.addRegions({
			'dynamicContentRegion' : regionsName
		});

		if (app.TabsApp.currentTab.get('id') >= 0) {
			// Get collection with data we will use to render the tab
			// content.
			var firstContent = new Content({
				id : app.TabsApp.currentTab.get('id')
			});

			// --------------------------------------------------------------------------------------//
			// TODO: Move filters section elsewhere.
			// Create collection of Filters used for legends.
			app.TabsApp.filters = FilterUtils.extractFilters(firstContent.get('reportMetadata').get('reportSpec').get('filters'));
			// Variable to save the current serialized filters from widget.
			app.TabsApp.serializedFilters = null;

			// Define the views.
			var FilterItemView = Marionette.ItemView.extend({
				tagName : 'div',
				/* className : 'round-filter', */
				template : jQuery(filtersItemTemplate, '#template-filters').html(),
				events : {
					'click' : "testclick"
				},
				testclick : function() {
					console.log('testclick');
				}
			});
			var CompositeItemView = Marionette.CompositeView.extend({
				template : jQuery(filtersTemplate, '#template-table-filters').html(),
				childView : FilterItemView
			});
			var compositeView = new CompositeItemView({
				collection : app.TabsApp.filters
			});

			// TODO: I know, not the best member names but thats defined in the
			// endpoint.
			app.TabsApp.appliedSettings = {
				"2" : firstContent.get('reportMetadata').get('reportSpec').get('settings').get('ampFiscalCalId'),
				"1" : firstContent.get('reportMetadata').get('reportSpec').get('settings').get('currencyCode')
			};
			app.TabsApp.numericFormatOptions = firstContent.get('reportMetadata').get('settings').models;

			// Render views.
			var dynamicLayoutView = new DynamicContentView({
				id : app.TabsApp.currentTab.get('id'),
				filters : app.TabsApp.filters
			});
			app.TabsApp.dynamicContentRegion.show(dynamicLayoutView);
			dynamicLayoutView.filters.show(compositeView);

			// Create accordion for filters area.
			jQuery("#main-dynamic-content-region_" + app.TabsApp.currentTab.get('id') + " #filters-collapsible-area").accordion({
				collapsible : true,
				active : false
			});

			// --------------------------------------------------------------------------------------//
			// TODO: make complex view for adding more info in this
			// section.
			var LegendView = Marionette.ItemView.extend({
				template : _.template(legendsTemplate),
				className : 'legends-container',
				onShow : function() {
					jQuery(document).tooltip({
						items : ('#show-legends-link-' + app.TabsApp.currentTab.get('id')),
						content : function() {
							return jQuery('#show_legend_pop_box').html();
						}
					});
				}
			});
			var legend = new Legend({
				currencyCode : firstContent.get('reportMetadata').get('reportSpec').get('settings').get('currencyCode'),
				id : app.TabsApp.currentTab.get('id')
			});
			var legendView = new LegendView({
				model : legend
			});
			dynamicLayoutView.legends.show(legendView);

			// --------------------------------------------------------------------------------------//
			gridManager.populateGrid(app.TabsApp.currentTab.get('id'), dynamicLayoutView, firstContent);

		} else if (app.TabsApp.currentTab.get('id') == -1) {
			// "More Tabs..." tab.
			var ItemView = Marionette.ItemView.extend({
				model : Tab,
				template : jQuery(invisibleTabLinkTemplate, '#invisibleTabLink').html()
			});

			var InvisibleTabsCollectionView = Marionette.CollectionView.extend({
				childView : ItemView,
				tagName : 'ul'
			});

			app.TabsApp.dynamicContentRegion.show(new InvisibleTabsCollectionView({
				collection : app.TabsApp.tabsCollection
			}));
		}
	}

	// "Class" methods definition here.
	TabEvents.prototype = {
		constructor : TabEvents,
		onCreateTab : function(event, ui) {
			console.log('create tab');
			this.onActivateTab(event, ui);

			TranslationManager.searchAndTranslate();
		},
		onActivateTab : function(event, ui) {
			console.log('activate tab');

			// Restart app variables defined for the active tab.
			app.TabsApp.serializedFilters = null;
			app.TabsApp.appliedSettings = null;
			app.TabsApp.currentGrid = null;
			app.TabsApp.currentTab = null;
			app.TabsApp.numericFormatOptions = null;

			// TODO: move this logic elsewhere.
			var panel = null;
			var selectedTabIndex = 0;
			if (ui.panel != undefined) {
				panel = ui.panel;
				selectedTabIndex = ui.tab.index();
			} else {
				panel = ui.newPanel;
				selectedTabIndex = ui.newTab.index();
			}
			retrieveTabContent(selectedTabIndex);
			TranslationManager.searchAndTranslate();
		}
	};

	return TabEvents;
});