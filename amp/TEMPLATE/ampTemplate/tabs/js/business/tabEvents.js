/*https://gist.github.com/jonnyreeves/2474026*/
/*https://github.com/icereval/backbone-documentmodel*/
define([ 'marionette', 'collections/contents', 'models/content', 'views/dynamicContentView', 'text!views/html/filtersWrapperTemplate.html',
		'text!views/html/filtersItemTemplate.html', 'models/filter', 'collections/filters', 'models/tab',
		'text!views/html/invisibleTabLinkTemplate.html' ], function(Marionette, Contents, Content, DynamicContentView, filtersTemplate,
		filtersItemTemplate, Filter, Filters, Tab, invisibleTabLinkTemplate) {

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

	function extractFilters(content) {
		var filters = new Filters();
		var filtersJson = content.get('metadata').get('filter');
		$(filtersJson.keys()).each(function(i, item) {
			if (filtersJson.get(item) instanceof Backbone.Collection) {
				var content = [];
				$(filtersJson.get(item)).each(function(j, item2) {
					$(item2.models).each(function(j, item3) {
						content.push(item3.get('name'));
					});
				});
				var auxFilter = new Filter({
					name : item,
					values : content
				});
				filters.push(auxFilter);
			}
		});
		return filters;
	}

	function retrieveTabContent(selectedTabIndex) {
		var id = app.TabsApp.tabItemsView.collection.models[selectedTabIndex].get('id');

		// Create a region where the dynamic content will be rendered inside
		// the tab.
		var regionsName = '#main-dynamic-content-region_' + id;
		app.TabsApp.addRegions({
			'filtersRegion' : regionsName
		});

		if (id >= 0) {
			// Get collection with data we will use to render the tab content.
			var tabContents = new Contents();
			var firstContent = tabContents.first();

			// Create collection of Filters.
			var filters = extractFilters(firstContent);

			// Define the views.
			var FilterItemView = Marionette.ItemView.extend({
				tagName : 'div',
				className : 'round-filter',
				template : $(filtersItemTemplate, '#template-filters').html()
			});
			var CompositeItemView = Marionette.CompositeView.extend({
				template : $(filtersTemplate, '#template-table-filters').html(),
				childView : FilterItemView
			});
			var compositeView = new CompositeItemView({
				collection : filters
			});

			// Render views.
			var dynamicLayoutView = new DynamicContentView();
			app.TabsApp.filtersRegion.show(dynamicLayoutView);
			dynamicLayoutView.filters.show(compositeView);

			// Create accordion for filters area.
			$("#main-dynamic-content-region_" + id + " #filters-collapsible-area").accordion({
				collapsible : true,
				active : false
			});
			// Create jQuery buttons.
			$("#main-dynamic-content-region_" + id + " .buttonify").button();
		} else if (id == -1) {
			// "More Tabs..." tab.
			var ItemView = Marionette.ItemView.extend({
				model : Tab,
				template : $(invisibleTabLinkTemplate, '#invisibleTabLink').html()
			});

			var InvisibleTabsCollectionView = Marionette.CollectionView.extend({
				childView : ItemView,
				tagName: 'ul'
			});

			app.TabsApp.filtersRegion.show(new InvisibleTabsCollectionView({
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
		},
		onActivateTab : function(event, ui) {
			console.log('activate tab');

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
			// Put loading animation.
			// $(panel).html(putAnimation());
			// Simulate time consuming content.
			setTimeout(function() {
				retrieveTabContent(selectedTabIndex);
			}, 200);
		}
	};

	return TabEvents;
});