package org.digijava.kernel.ampapi.endpoints.gpi;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

@Path("gpi")
public class GPIEndPoints {

	@GET
	@Path("/aid-on-budget")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "getAidOnBudgetList", ui = false, authTypes = { AuthRule.IN_WORKSPACE})
	public JsonBean getAidOnBudgetList(@QueryParam("offset") Integer offset, @QueryParam("count") Integer count,
			@QueryParam("orderby") String orderBy, @QueryParam("sort") String sort) {
		return GPIDataService.getAidOnBudgetList();
	}

	@GET
	@Path("/aid-on-budget/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "getAidOnBudgetById", ui = false, authTypes = { AuthRule.IN_WORKSPACE})
	public JsonBean getAidOnBudgetById(@PathParam("id") long id) {
		return GPIDataService.getAidOnBudgetById(id);
	}

	@POST
	@Path("/aid-on-budget")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "saveAidOnBudget", ui = false)
	public JsonBean saveAidOnBudget(JsonBean aidOnBudget) {
		return GPIDataService.saveAidOnBudget(aidOnBudget);
	}
	
	@DELETE
    @Path("/aid-on-budget/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "deleteAidOnBudgetById", ui = false, authTypes = { AuthRule.IN_WORKSPACE})
    public JsonBean deleteIndicatorById(@PathParam("id") long id) {
        return GPIDataService.delete(id);
    }
}
