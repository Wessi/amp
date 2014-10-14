require.config({
	paths : {
		backbone : 'lib/backbone.marionette/backbone',
		underscore : 'lib/backbone.marionette/underscore',
		jquery : 'lib/jquery_1.10.2',
		jqueryui : 'lib/jquery-ui.min_1.11.0',
		marionette : 'lib/backbone.marionette/backbone.marionette',
		text : 'lib/text_2.0.12',
		localStorage : 'lib/Backbone.localStorage-master/backbone.localStorage-min',
		documentModel : 'lib/backbone-documentmodel-master/backbone-documentmodel',
		documentCollection : 'lib/backbone-documentmodel-master/backbone-documentmodel',
		jqgrid_lang : [ 'lib/jqgrid-4.6.0/js2/i18n/grid.locale-es', 'lib/jqgrid-4.6.0/js2/i18n/grid.locale-en' ],
		jqgrid : 'lib/jqgrid-4.6.0/js2/jquery.jqGrid.src',
		filtersWidget : '/TEMPLATE/ampTemplate/node_modules/amp-filter/dist/amp-filter',
		bootstrap : 'lib/bootstrap-3.2.0-dist/bootstrap.min'
	},
	shim : {
		jquery : {
			exports : 'jQuery'
		},
		underscore : {
			exports : '_'
		},
		backbone : {
			deps : [ 'jquery', 'underscore' ],
			exports : 'Backbone'
		},
		marionette : {
			deps : [ 'jquery', 'underscore', 'backbone' ],
			exports : 'Marionette'
		},
		jqueryui : {
			deps : [ 'jquery' ],
			exports : 'jQueryUI'
		},
		documentModel : {
			deps : [ 'backbone' ],
			exports : 'documentModel'
		},
		jqgrid_lang : {
			deps : [ 'jquery', 'jqueryui' ],
			exports : 'jqgrid_lang'
		},
		jqgrid : {
			deps : [ 'jquery', 'jqueryui', 'jqgrid_lang' ],
			exports : 'jqgrid'
		},
		filtersWidget : {
			exports : 'filtersWidget'
		},
		bootstrap : {
			deps : [ 'jquery' ],
			exports : 'bootstrap'
		}
	}
});

require([ 'app' ]);