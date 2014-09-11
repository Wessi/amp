var Deferred = require('jquery').Deferred;
var _ = require('underscore');


module.exports = {

  load: function() {
    if (!_.has(this, '_loaded')) {
      var self = this;
      this._loaded = new Deferred();
      this.fetch()
        .done(function() {
          self._loaded.resolveWith(self, [self]);
        })
        .fail(function(xhr, textStatus) {
          console.warn('failed load:', textStatus, self, xhr);
          self._loaded.rejectWith(self, [self, textStatus]);
          delete self._loaded;  // so that retries can happen
        });
    }

    return this._loaded.promise();
  },

  loadAll: function() {
    // classes using this mixin can override this method to add more loading
    // and processing before resolving. For example a class which loads a
    // boundary could implement this method as:
    //
    // return jQuery.when(this.load(), this.loadBoundary()).promise();
    //
    return this.load();
  }

};
