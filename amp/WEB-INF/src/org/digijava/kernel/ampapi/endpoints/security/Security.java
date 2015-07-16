package org.digijava.kernel.ampapi.endpoints.security;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.util.AmpApiToken;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.SecurityUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;

import com.sun.jersey.spi.container.ContainerRequest;

/**
 * This class should have all security / permissions related methods
 * 
 * @author jdeanquin@developmentgateway.org
 * 
 */
@Path("security")
public class Security {
	@Context
	private HttpServletRequest httpRequest;
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/user/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public JsonBean user() {
		JsonBean authenticationResult = new JsonBean();
		boolean isAdmin;

		AmpApiToken apiToken = SecurityUtil.getTokenFromSession();

		isAdmin ="yes".equals(httpRequest.getSession().getAttribute("ampAdmin"));
		
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
		String username=null;
		String team=null;
		
		//if the user is admin the he doesn't have a workspace assigned
		if (tm != null && !isAdmin ) {
			User u;
			AmpTeamMember ampTeamMember;
			try {
				u = UserUtils.getUserByEmail(tm.getEmail());
				username=u.getName();
				ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
				team=ampTeamMember.getAmpTeam().getName();
				//if the user is logged in without a token, we generate one
				if (apiToken == null) {
					//if no token is present in session we generate one 
					apiToken = SecurityUtil.generateToken();
				}

			} catch (DgException e) {
				// TODO return error 500 with description
				e.printStackTrace();
			}

		}
	    String port="";
	    //if we are in secure mode and the port is not 443 or if we are not secure and the port is not 80 we have to add the port to the url
	    if( (TLSUtils.getRequest().isSecure() && TLSUtils.getRequest().getServerPort()!=443 ) ||( !TLSUtils.getRequest().isSecure() && TLSUtils.getRequest().getServerPort()!=80 )){
	    	port=":"+TLSUtils.getRequest().getServerPort();
	    }
		authenticationResult.set("token", apiToken!=null && apiToken.getToken()!=null?apiToken.getToken():null);
		authenticationResult.set("url", "http"+ (TLSUtils.getRequest().isSecure()?"S":"") +"://"+ TLSUtils.getRequest().getServerName() + port +"/showLayout.do?layout=login");
		authenticationResult.set("team", team);
		authenticationResult.set("user-name", username);
		authenticationResult.set("add-activity", false); //to check if the user can add activity in the selected ws
		authenticationResult.set("view-activity", true); //to check if the user can edit activity in the selected ws

		return authenticationResult;
	}
	
	/**
	 * Authorizes Container Request
	 * @param containerReq
	 */
	public static void authorize(ContainerRequest containerReq) {
		ApiAuthorization.authorize(containerReq);
	}
	
	/**
	 * THIS IS FOR DEBUG ONLY. Must be disabled on production.
	 * @param token
	 * @return
	 */
	/*
	@GET
	@Path("/token")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String echo(@QueryParam("amp_api_token") String token) {
		token = SecurityUtil.generateToken();
		return "Token: " + token;
	}
	*/
}
