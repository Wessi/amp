define([ 'underscore', 'backbone', 'models/document' ], function(_, Backbone, Document) {
	var Documents = Backbone.Collection.extend({
		model : Document,
		url : '/rest/documents/getTopDocuments',
		fetchData : function() {
			console.log('Initialized Documents Collection');
			this.fetch({
				async : false,
				error : function(collection, response) {
					console.error('error loading documents url');
				},
				success : function(collection, response) {
					// console.log("list of documents is: " + JSON.toString(response));
				}
			});
		}
	});

	return Documents;
});