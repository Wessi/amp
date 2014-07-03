define(
  [
    'underscore',
    'backbone',
    'jquery',
    'amp/sidebar/base-control/base-control-view',
    'amp/sidebar/filters/views/generic-filter-view',
    'amp/sidebar/filters/views/org-filter-view',
    'amp/sidebar/filters/views/years-filter-view',
    'text!amp/sidebar/filters/templates/filters-template.html'
  ],
  function (_, Backbone, $, BaseToolView,  GenericFilterView, OrgFilterView, YearsFilterView, Template) {
    'use strict';

    var filterViews = [OrgFilterView, YearsFilterView];

    var View = BaseToolView.extend({
      id: 'tool-filters',
      title: 'Filters',
      iconClass: 'ampicon-filters',
      description: 'Apply filters to the map.',
      apiURL: 'js/mock-api/filters.json',

      // collection of child views..
      filterViewsInstances:[],

      template: _.template(Template),

      initialize: function() {
        var self = this;
        BaseToolView.prototype.initialize.apply(this);

        this._getFilterList().done(function(filterList){
          self.filterViewsInstances = filterList;
          self.render();
        });

        //TODO: register listener for FILTER_CHANGED event, then iterate over
        //      filterViews and call createFilterJSON on each model
        //      create master filter object and pass it to the map. to call api and re-render.
      },


      render: function() {
        BaseToolView.prototype.render.apply(this);

        // add content
        this.$('.content').html(this.template({title: this.title}));

        var filtersContainer = this;
        _.each(this.filterViewsInstances, function(filterView) {
          filtersContainer.$('.filter-list').append(filterView.renderTitle().el);
        });

        return this;
      },

      _getFilterList: function(){
        var self = this;
        var filterList = [];
        var deferred =  $.Deferred();

        $.ajax({
            url: this.apiURL
          })
          .done(function(data){
            if( _.isEmpty(data) ){
              console.warn('Filters API returned empty', data);
            }

            _.each(data, function(APIFilter){
              var view = self._createFilterView(APIFilter);
              filterList.push(view);
            });

            deferred.resolve(filterList);
          })
          .fail(function(jqXHR, textStatus, errorThrown){
            var errorMessage = 'Getting filters failed';
            console.error('Getting filters failed', jqXHR, textStatus, errorThrown);
            deferred.reject(errorMessage);
          });

        return deferred;
      },

      _createFilterView: function(APIFilter){
        // Assume all filters are genericView, but if we want, we can
        // use specific granular views for some filters: OrgFilterView
        // TODO: magic strings are dangerous, config somewhere...
        var view = null;
        switch (APIFilter.name){
          case 'Organizations':
            view = new OrgFilterView({url:APIFilter.endpoint});
            break;
          case 'Years':
            view = new YearsFilterView({url:APIFilter.endpoint});
            break;
          default:
            view = new GenericFilterView();
            view.model.set({url:APIFilter.endpoint, title:APIFilter.name});
        }
        return view;
      }
    });

    return View;
  }
);
