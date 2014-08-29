package org.digijava.kernel.ampapi.endpoints.gis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

import org.apache.log4j.Logger;
import org.codehaus.jackson.node.POJONode;
import org.codehaus.jackson.node.TextNode;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.ampapi.endpoints.dto.gis.IndicatorLayers;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.GisUtil;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureCollectionGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.PointGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.objects.ClusteredPoints;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.form.helpers.ActivityFundingDigest;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.esrigis.dbentity.AmpMapState;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapConstants;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

/**
 * Class that holds entrypoing for GIS api methods
 * 
 * @author ddimunzio@developmentgateway.org jdeanquin@developmentgateway.org
 * 
 */
@Path("gis")
public class GisEndPoints {
	private static final Logger logger = Logger.getLogger(GisEndPoints.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<AvailableMethod> getAvailableFilters() {
		return GisUtil.getAvailableMethods(GisEndPoints.class.getName());
	}	

	/**
	 * Returns Aggregate ADM info by ADM Level
	 * 
	 * @param filter
	 *            adminLevel to filter, the json should look like {
	 *            "FiltersParams":
	 *            {"params":[{"filterName":"adminLevel","filterValue"
	 *            :["Region"]}] } }
	 * 
	 *            Available regions
	 * @return
	 */
	@POST
	@Path("/cluster")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="ClusterPointsByAdmin")
	public final FeatureCollectionGeoJSON getClusteredPointsByAdm(
			final JsonBean filter) {

		List<ClusteredPoints> c = QueryUtil.getClusteredPoints(filter.get(
				"adminLevel").toString());
		FeatureCollectionGeoJSON result = new FeatureCollectionGeoJSON();
		for (ClusteredPoints clusteredPoints : c) {
			result.features.add(getPoint(new Double(clusteredPoints.getLon()),
					new Double(clusteredPoints.getLat()),
					clusteredPoints.getActivityids(),
					clusteredPoints.getAdmin()));
		}

		return result;
	}

	/**
	 * Returns Aggregate ADM info by ADM Level
	 * 
	 * @param filter
	 *            adminLevel to filter, the json should look like {
	 *            "FiltersParams":
	 *            {"params":[{"filterName":"adminLevel","filterValue"
	 *            :["Region"]}] } }
	 * 
	 *            Available regions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Path("/structures")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="Structures")
	public final FeatureCollectionGeoJSON getProjectSites(final JsonBean filter) {
		FeatureCollectionGeoJSON f = new FeatureCollectionGeoJSON();

		List<AmpStructure> al = QueryUtil.getStructures();
		for (AmpStructure structure : al) {
			FeatureGeoJSON fgj = new FeatureGeoJSON();
			PointGeoJSON pg = new PointGeoJSON();
			pg.coordinates.add(Double.parseDouble(structure.getLongitude()));
			pg.coordinates.add(Double.parseDouble(structure.getLatitude()));
			fgj.id = structure.getAmpStructureId().toString();
			fgj.properties.put("title",
					new TextNode(structure.getTitle()));
			if (structure.getDescription() != null
					&& !structure.getDescription().trim().equals("")) {
				fgj.properties.put("description", new TextNode(
						structure.getDescription()));
			}
			Set<AmpActivityVersion> av = structure.getActivities();
			List<Long> actIds = new ArrayList<Long>();

			for (AmpActivityVersion ampActivity : av) {
				actIds.add(ampActivity.getAmpActivityId());
			}

			fgj.properties.put("activity", new POJONode(actIds));
			// fgj.properties.put("admId", new LongNode(23));
			fgj.geometry = pg;

			f.features.add(fgj);
		}
		return f;
	}

	@POST
	@Path("/saved-maps")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="SaveMap")
	public JsonBean savedMaps(final JsonBean pMap) {
		Date creationDate = new Date();
		JsonBean mapId = new JsonBean();

		AmpMapState map = new AmpMapState();
		map.setTitle(pMap.getString("title"));
		map.setDescription(pMap.getString("description"));
		map.setStateBlob(pMap.getString("stateBlob"));
		map.setCreatedDate(creationDate);
		map.setUpdatedDate(creationDate);
		map.setLastAccesedDate(creationDate);

		try {
			Session s = PersistenceManager.getRequestDBSession();
			s.save(map);
			s.flush();
			mapId.set("mapId", map.getId());
		} catch (DgException e) {
			logger.error("Cannot Save map", e);
			throw new WebApplicationException(e);
		}
		return mapId;
	}

	@GET
	@Path("/saved-maps/{mapId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="MapById")
	public JsonBean savedMaps(@PathParam("mapId") Long mapId) {
		JsonBean jMap = null;
		try {
			Session s = PersistenceManager.getRequestDBSession();
			AmpMapState map = (AmpMapState) s.load(AmpMapState.class, mapId);
			jMap = getJsonBeanFromMapState(map, Boolean.TRUE);
			map.setLastAccesedDate(new Date());
			s.merge(map);

		} catch (ObjectNotFoundException e) {
			jMap = new JsonBean();
		} catch (DgException e) {
			logger.error("cannot get map by id " + mapId, e);
			throw new WebApplicationException(e);
		}
		return jMap;

	}


	@GET
	@Path("/saved-maps")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="MapList")
	public List<JsonBean> savedMaps() {
		List<JsonBean> maps = new ArrayList<JsonBean>();

		try {
			List<AmpMapState> l = QueryUtil.getMapList();
			for (AmpMapState map : l) {
				maps.add(getJsonBeanFromMapState(map, Boolean.FALSE));
			}
			return maps;
		} catch (DgException e) {
			logger.error("Cannot get maps list", e);
			throw new WebApplicationException(e);
		}
	}

	@GET
	@Path("/indicator-layers")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="IndiactorLayers")
	public List<IndicatorLayers> getIndicatorLayers() {
		List<IndicatorLayers> indicatorLayers = new ArrayList<IndicatorLayers>();
		List<AmpMapConfig> mapsConfigs = DbHelper.getMaps();
		for (AmpMapConfig ampMapConfig : mapsConfigs) {
			IndicatorLayers i = new IndicatorLayers();
			i.setId(ampMapConfig.getId());
			i.setTitle(ampMapConfig.getConfigName());
			i.setLink(ampMapConfig.getMapUrl());
			String type = MapConstants.mapTypeNames.get(ampMapConfig
					.getMapType());
			i.setType(type);
			indicatorLayers.add(i);
		}
		return indicatorLayers;
	}

	@POST
	@Path("/activities/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="ActivitiesLists")
	public List<Activity> getActivities(JsonBean filter) {
		List<Activity> activities = new ArrayList<Activity>();
		List<AmpActivity> ampActivities = QueryUtil.getActivities();

		if (ampActivities != null) {
			for (AmpActivity ampActivity : ampActivities) {
				activities.add(buildActivityDto(ampActivity));
			}

		}

		return activities;
	}

	/**
	 * build an activity to be serialized base upon AmpActivity class
	 * 
	 * @param ampActivity
	 * @return
	 */
	private Activity buildActivityDto(AmpActivity ampActivity) {
		Activity a = new Activity();
		a.setId(ampActivity.getAmpActivityId());
		a.setName(ampActivity.getName());
		String description = null;
		//do not return description yet since they are stored 
		//in dg_message table an would need to do a query for each
		//row
//		if (ampActivity.getDescription() != null) {
//			if (ampActivity.getDescription().length() > 50) {
//				description = StringUtils
//						.left(ampActivity.getDescription(), 50) + "...";
//			} else {
//				description = ampActivity.getDescription();
//			}
//
//		}
		a.setDescription(description);
		a.setAmpUrl(ActivityGatekeeper.buildPreviewUrl(String.valueOf(ampActivity
				.getAmpActivityId())));
		JsonBean j=new JsonBean();
		Map<Long,List<Long>> sectors=new HashMap<Long,List<Long>>();
		Map<Long,List<Long>> roles=new HashMap<Long,List<Long>>();
		Map<Long,List<Long>> programs=new HashMap<Long,List<Long>>();
		for (Object o : ampActivity.getSectors()) {
			AmpActivitySector as=(AmpActivitySector)o;
			if(
			sectors.get(as.getClassificationConfig().getId())  ==null){
				sectors.put(as.getClassificationConfig().getId(), new ArrayList<Long>());
			}
			sectors.get(as.getClassificationConfig().getId()).add(as.getSectorId().getAmpSectorId());
		}
		for(AmpOrgRole orgRole:ampActivity.getOrgrole()){
			if(roles.get(orgRole.getAmpOrgRoleId())==null){
				roles.put(orgRole.getAmpOrgRoleId(),new ArrayList<Long>());
			}
			roles.get(orgRole.getAmpOrgRoleId()).add(orgRole.getOrganisation().getAmpOrgId());
		}
		
		
		for(Object o :ampActivity.getActPrograms()){
			AmpActivityProgram aap=(AmpActivityProgram)o;
			if(programs.get(aap.getProgramSetting().getAmpProgramSettingsId()) ==null){
				programs.put(aap.getProgramSetting().getAmpProgramSettingsId(),new ArrayList<Long>());
			}
			programs.get(aap.getProgramSetting().getAmpProgramSettingsId()).add(aap.getProgram().getAmpThemeId());
			
		}
		
		j.set("sectors", sectors);
		j.set("organizations", roles);
		j.set("programs", programs);
		
		a.setMatchesFilters(j);
		return a;
	}

	
	@GET
	@Path("/activities/{activityId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="ActivitiesById")
	public List<Activity> getActivities(@PathParam("activityId") PathSegment activityIds) {
		List<Activity> l=new ArrayList<Activity>();
		
		List<AmpActivity>activities=QueryUtil.getActivities(activityIds.getPath());
		for (AmpActivity activity : activities) {
			
			
			if(activity!=null){
				Activity a = new Activity();
				a.setId(activity.getAmpActivityId());
				a.setName(activity.getName());
//				String 
//				description =null;
//				try {
//					description = DbUtil.getEditorBody(TLSUtils.getSite(),
//					        activity.getDescription(),
//					        RequestUtils.
//					        getNavigationLanguage(TLSUtils.getRequest()).
//					        getCode());
//					a.setDescription(description);
//				} catch (EditorException e) {
//					logger.error("cannot get description");
//				}
				a.setAmpUrl(ActivityGatekeeper.buildPreviewUrl(String.valueOf(activity
						.getAmpActivityId())));
				ActivityFundingDigest fundingDigest=new ActivityFundingDigest();
				fundingDigest.populateFromFundings(activity.getFunding(), "US", null, false);
				for(FundingDetail fd:fundingDigest.getCommitmentsDetails()){
					a.addCommitments(fd.getTransactionAmount(), fd.getTransactionDate());	
				}
				for(FundingDetail fd:fundingDigest.getDisbursementsDetails()){
					a.addDisbursment(fd.getTransactionAmount(), fd.getTransactionDate());	
				}			
				
				l.add(a);
			}
		}
		return l;
	}

	private FeatureGeoJSON getPoint(Double lat, Double lon,
			List<Long> activityid, String adm) {
		FeatureGeoJSON fgj = new FeatureGeoJSON();
		PointGeoJSON pg = new PointGeoJSON();
		pg.coordinates.add(lat);
		pg.coordinates.add(lon);

		fgj.properties.put("activityid", new POJONode(activityid));
		fgj.properties.put("admName", new TextNode(adm));

		fgj.geometry = pg;
		return fgj;
	}

	private JsonBean getJsonBeanFromMapState(AmpMapState map, Boolean getBlob) {
		JsonBean jMap = new JsonBean();

		jMap.set("id", map.getId());
		jMap.set("title", map.getTitle());
		jMap.set("description", map.getDescription());
		if (getBlob) {
			jMap.set("stateBlob", map.getStateBlob());
		}
		jMap.set("created", GisUtil.formatDate(map.getCreatedDate()));
		jMap.set("lastAccess", GisUtil.formatDate(map.getLastAccesedDate()));
		return jMap;
	}

}
