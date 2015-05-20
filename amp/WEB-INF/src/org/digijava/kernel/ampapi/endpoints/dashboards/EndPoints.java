package org.digijava.kernel.ampapi.endpoints.dashboards;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.DashboardsService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

import net.sf.json.JSONObject;


/**
 * 
 * @author Diego Dimunzio
 * - All dash boards end points 
 */

@Path("dashboard")
public class EndPoints {
	/**
	 * Show a list of available top ___ things, with their names
	 * @return
	 */
	
	@GET
	@Path("/tops")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "topsList")
	public List<JsonBean> getAdminLevelsTotalslist() {
		return DashboardsService.getTopsList();
	}

	/**
	 * Get top donors values for dash boards chart
	 * @param type (Chart Type)
	 * @param adjtype (Adjustment Type)
	 * @param limit (Result Limit)
	 * @return
	 */
	
	@POST 
	@Path("/tops/{type}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "tops")
	//TODO: Implement Filters
	public JsonBean getAdminLevelsTotals(JsonBean config,
			@PathParam("type") String type,
			@DefaultValue("5") @QueryParam("limit") Integer limit) {
		return DashboardsService.getTops(type, limit, config);
	}
	
	/**
	 * Get aid predictability values for dash boards chart
	 * @param years (number of years to include)
	 * @return
	 */
	
	@POST 
	@Path("/aid-predictability")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "aidPredictability")
	public JSONObject getAidPredictability(JsonBean filter) throws Exception {
		return DashboardsService.getAidPredictability(filter);
	}
	
	/**
	 * Get funding types by year
	 * @param adjtype
	 * @param limit
	 * @return
	 */
	
	@POST 
	@Path("/ftype")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "ftype")
	//TODO: Implement Filters
	public JsonBean getfundingtype(JsonBean config,
			@DefaultValue("ac") @QueryParam("adjtype") String adjtype) {
		return DashboardsService.fundingtype(adjtype,config);
	}
	
	@POST 
	@Path("/ndd")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "ndd")
	public JsonBean getNDD(JsonBean config) throws Exception {
		return DashboardsService.getNDD(config);
	}

	@POST
	@Path("/saved-charts")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "SaveChart")
	public JsonBean savedMaps(final JsonBean pChrat) {
		return EndpointUtils.saveApiState(pChrat,"C");
	}

	@GET
	@Path("/saved-charts/{chartId}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "ChartById")
	public JsonBean savedCharts(@PathParam("chartId") Long chartId) {
		return EndpointUtils.getApiState(chartId);

	}

	@GET
	@Path("/saved-charts")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "ChartList")
	public List<JsonBean> savedCharts() {
		String type="C";
		return EndpointUtils.getApiStateList(type);
	}
	
	@POST
	@Path("/ndd/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "ndd_projects")
	public JsonBean getAdminLevelsTotals(JsonBean config, @PathParam("id") Integer id) {
		return DashboardsService.getPeaceMarkerProjectsByCategory(config, id);
	}
}
