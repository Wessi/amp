var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var ProjectListTemplate = fs.readFileSync(__dirname + '/../templates/structure-list-template.html', 'utf8');

module.exports = Backbone.View.extend({
  // These will eventually move to a config.
  // They control when to resize points based on zoom
  ZOOM_BREAKPOINT: 7,
  SMALL_ICON_RADIUS: 2,
  BIG_ICON_RADIUS: 7,
  currentRadius: 2,
  markerCluster: null,

  popup: null,
  projectListTemplate: _.template(ProjectListTemplate),

  customClusterMap: {},
  maxClusterCount: 0,
  CLUSTER_PRECISION: 8, //decimal places of lat, lng precision for clustering. (doesn't effect plugin.)
  MAX_CLUSTER_SIZE: 20,

  initialize: function(options) {
    this.app = options.app;
    this.map = options.map;

    this.initCluster();

    this.listenTo(this.app.data.projectSites, 'show', this.showLayer);
    this.listenTo(this.app.data.projectSites, 'hide', this.hideLayer);

    this.listenTo(this.markerCluster, 'clusterclick', this.clusterClick);

    _.bindAll(this, '_onEachFeature');
  },

  render: function() {
    return this;
  },

  // ==================
  // Point / Feature Code
  // ==================
  getNewProjectSitesLayer: function(projectSitesModel) {
    this.features = projectSitesModel.get('features');
    this.rawData = projectSitesModel.attributes;
    this._renderFeatures();
    return this.featureGroup;
  },

  _renderFeatures: function() {
    var self = this;

    self.markerCluster.clearLayers();

    var model = self.app.data.projectSites.at(0);

    // add new featureGroup
    //console.log(JSON.stringify(self.rawData));
    self.featureGroup = L.geoJson(self.rawData, {
      pointToLayer: function (feature, latlng) {
        var colors = model.palette.colours.filter(function(colour) {
          return colour.get('test').call(colour, feature.id);
        });
        if (colors.length > 2) {  // 2, because "other" is always true...
          colors = [model.palette.colours.find(function(colour) {
            return colour.get('multiple') === true;
          })];
        }

        // temp hack for if pallette part didn't work.
        if (colors.length === 0) {
          colors[0] = {hex: function(){ return '#fa5';}};
        }

        var point = new L.CircleMarker(latlng, {
          radius: self.currentRadius,
          fillColor: colors[0].hex(),
          color: '#333',
          weight: 1,
          opacity: 1,
          fillOpacity: 1
        });

        self.markerCluster.addLayer(point);


        // DRS in progress custom own clustering. big efficiency gains.
        var latLngString = Math.round(latlng.lat* self.CLUSTER_PRECISION * 10) + 
          ',' + Math.round(latlng.lng*self.CLUSTER_PRECISION * 10);
        if(self.customClusterMap[latLngString]){
          self.customClusterMap[latLngString].push(point); //TODO: should push point or feature?
          self.maxClusterCount = Math.max(self.maxClusterCount,self.customClusterMap[latLngString].length);
        } else{
          self.customClusterMap[latLngString] =[point];
        }

        return point;
      },
      onEachFeature: self._onEachFeature
    });
  },

  // circles  shrink if we're zoomed out, get big if zoomed in
  _updateZoom: function() {
    var self = this;

    if (this.featureGroup) {
      var zoom = this.map.getZoom();
      // make small points
      if (zoom < this.ZOOM_BREAKPOINT && self.currentRadius !== self.SMALL_ICON_RADIUS) {
        self.currentRadius = self.SMALL_ICON_RADIUS;
        this.featureGroup.eachLayer(function(layer) {
          layer.setRadius(self.currentRadius);
        });
      } else if (zoom >= this.ZOOM_BREAKPOINT && self.currentRadius !== self.BIG_ICON_RADIUS) {
        self.currentRadius = self.BIG_ICON_RADIUS;
        this.featureGroup.eachLayer(function(layer) {
          layer.setRadius(self.currentRadius);
        });
      }
    }
  },

  _hilightProject: function(projectId) {
    this.featureGroup.eachLayer(function(layer) {
      var properties = layer.feature.properties;
      if (properties.projectId === projectId) {
        layer.setStyle({fillColor: '#008'});
      }
    });
  },

  _dehilightProject: function(projectId) {
    this.featureGroup.eachLayer(function(layer) {
      var properties = layer.feature.properties;
      if (properties.projectId === projectId) {
        layer.setStyle({fillColor: '#f70'});
      }
    });
  },


  // ==================
  // Cluster Code
  // ==================
  initCluster: function() {
    var self = this;

    this.layerLoadState = 'pending';  // 'loading', 'loaded'.

    var model = self.app.data.projectSites.at(0);

    //TODO: checkout prune cluster, supposedly way faster...
    // may also be worth doing manually since we don't want updates on zoom
    // TODO: make sizing dynamic based on highest cluster... and put into own function...
    this.markerCluster = new L.markerClusterGroup({
      maxClusterRadius: 0.5,
      iconCreateFunction: function(cluster){return self._createCluster(cluster, model);},
      zoomToBoundsOnClick: false,
      showCoverageOnHover: false,
      spiderfyOnMaxZoom: false,
      removeOutsideVisibleBounds: true
    });
  },

  _createCluster: function(cluster, model){
    var self = this;
    var markers = cluster.getAllChildMarkers();
    var size = (markers.length / self.maxClusterCount) * self.MAX_CLUSTER_SIZE;
    size = Math.min(self.MAX_CLUSTER_SIZE, size);
    size = Math.max(self.BIG_ICON_RADIUS, size);

    var zoomedIn = (self.map.getZoom() >= self.ZOOM_BREAKPOINT);

    if (zoomedIn) {
      size += 2 + self.BIG_ICON_RADIUS;
    }

    var colours = _(markers)
      .chain()
      .map(function(m) {
        var colour = model.palette.colours.find(function(c) {
          return c.get('test').call(c, m.feature.id);
        });
        return colour;
      })
      .uniq()
      .value();

    if (colours.length > 1) {
      colours = [model.palette.colours.find(function(c) {
        return c.get('multiple') === true;
      })];
    }

    var marker = new L.circleDivIcon(size / 2, {
        className: 'marker-cluster' + (zoomedIn ? '' : ' marker-cluster-small'),
        html: (zoomedIn ? '<div class="text">' + markers.length + '</div>' : ''),
        color: '#444',
        fillColor: (colours[0] && colours[0].hex()),
        weight: 1
      });

    return marker;
  },

  clusterClick: function(a) {
    var self = this;
    //TODO: seems silly to bind on every click...
    // Once we have nailed down desired fuhnctionality it will be worth writing our own clusterer since it only
    // needs to run once on project site load and that is it. (not each zoom etc.)
    a.layer.bindPopup(self.projectListTemplate({ projects: a.layer.getAllChildMarkers() }));
    a.layer.openPopup(self.map);
  },


  // Create pop-ups
  _onEachFeature: function(feature, layer) {
    var self = this;

    if (feature.properties) {
      //TODO: template:
      layer.bindPopup('Project #: '+ (feature.properties.activity ? feature.properties.activity[0] :'') +
        '<br />Site: ' + feature.properties.structureTitle );
    }

    layer.on('click', function(evt) {
      var feature = evt.target.feature;
      if (feature) {
        var projectId = feature.properties.projectId;
        self._hilightProject(projectId);
      }
    });

    layer.on('popupclose',function(evt) {
      var feature = evt.target.feature;
      if (feature) {
        var projectId = feature.properties.projectId;
        self._dehilightProject(projectId);
      }
    });
  },

  // ==================
  // Layer management
  // ==================

  showLayer: function(projectSitesModel) {
    var self = this;

    if (this.layerLoadState === 'loading') {
      console.warn('tried to show project sites while they are still loading');
      return;
    } else if (this.layerLoadState === 'pending') {
      this.layerLoadState = 'loading';
    }

    projectSitesModel.loadAll().done(function() {
      self.layerLoadState = 'loaded';
      self.getNewProjectSitesLayer(projectSitesModel);
      self.map.addLayer(self.markerCluster);
    });

    this.map.on('zoomend', this._updateZoom, this);

    projectSitesModel.load();
  },

  bringToFront: function(){
    if(this.markerCluster && this.markerCluster._featureGroup){
      this.markerCluster._featureGroup.bringToFront();
    }
  },

  hideLayer: function() {
    if (this.layerLoadState === 'pending') {
      throw new Error('Tried to remove project sites but they have not been added');
    } else if (this.layerLoadState === 'loading') {
      console.warn('removing layers while they are loading is not yet supported');
    }

    this.map.off('zoomend', this._updateZoom);

    this.map.removeLayer(this.markerCluster);
  }

});
