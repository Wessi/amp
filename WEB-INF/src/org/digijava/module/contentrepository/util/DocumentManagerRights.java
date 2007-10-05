package org.digijava.module.contentrepository.util;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Workspace;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;

public class DocumentManagerRights {
	
	public static Boolean hasDeleteRights(String uuid, HttpServletRequest request) {
		Node node	= DocumentManagerUtil.getWriteNode(uuid, request);
		return hasDeleteRights(node, request);
	}
	public static Boolean hasDeleteRights(Node node, HttpServletRequest request) {
		boolean result						= true;
		Boolean manuallySetNoDeleteFlag	= isManuallySetNoDeleteFlag(request);
		if (manuallySetNoDeleteFlag != null) {
			result = result && manuallySetNoDeleteFlag;
		}
		return result && isOwnerOrTeamLeader(node, request);
	}
	
	public static Boolean hasViewRights (Node node, HttpServletRequest request) {
		Boolean isOwnerOrTeamLeader	= isOwnerOrTeamLeader(node, request);
		if ( isOwnerOrTeamLeader != null && isOwnerOrTeamLeader.booleanValue() )
			return new Boolean(true);
		
		Boolean isTeamDocument		= isTeamDocument(node, request);
		if ( isTeamDocument != null && isTeamDocument.booleanValue() ) 
			return new Boolean(true);
		
		return new Boolean(false);
	}
	
	public static Boolean hasVersioningRights (Node node, HttpServletRequest request) {
		boolean result						= true;
		Boolean manuallySetNoVersioningFlag	= isManuallySetNoVersioningFlag(request);
		if (manuallySetNoVersioningFlag != null) {
			result = result && manuallySetNoVersioningFlag;
		}
		return result && isOwnerOrTeamLeader(node, request);
	}
	
	public static Boolean hasMakePublicRights (Node node, HttpServletRequest request) {
		return true && isOwnerOrTeamLeader(node, request);
	}
	public static Boolean hasDeleteRightsOnPublicVersion(Node node, HttpServletRequest request) {
		return true && isOwnerOrTeamLeader(node, request);
	}
	
	private static Boolean isOwnerOrTeamLeader(Node node, HttpServletRequest request) {
		HttpSession httpSession		= request.getSession(); 
		TeamMember teamMember		= (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
		String username				= teamMember.getEmail();
		String teamId				= teamMember.getTeamId() + "";
		
		String userPath				= "private"+"/"+teamId+"/"+username;
		
		try {
			Workspace workspace			= node.getSession().getWorkspace();
			String path					= node.getCorrespondingNodePath( workspace.getName() );
			/**
			 * If owner of node
			 */
			if ( path.contains(userPath) ) {
				return new Boolean(true);
			}
			
			/**
			 * If team leader of the team
			 */
			if ( teamMember.getTeamHead() && path.contains("/" + teamId + "/") ) {
				return new Boolean(true);
			}
			return new Boolean(false);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private static Boolean isTeamDocument(Node node, HttpServletRequest request) {
		HttpSession httpSession		= request.getSession(); 
		TeamMember teamMember		= (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
		if ( teamMember == null )
			return new Boolean(false);
			
		String teamId				= teamMember.getTeamId() + "";
		
		String myTeamPath			= "team/"+teamId;
		
		try {
			Workspace workspace			= node.getSession().getWorkspace();
			String path					= node.getCorrespondingNodePath( workspace.getName() );
			if ( path.contains(myTeamPath) ) {
				return new Boolean(true);
			}
			else 
				return new Boolean(false);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	private static Boolean isManuallySetNoVersioningFlag(HttpServletRequest request) {
		if ( request.getParameter("versioningRights") != null ) {
			Boolean result	= Boolean.parseBoolean( request.getParameter("versioningRights") );
			return result;
		}
		return null;
	}
	
	private static Boolean isManuallySetNoDeleteFlag(HttpServletRequest request) {
		if ( request.getParameter("deleteRights") != null ) {
			Boolean result	= Boolean.parseBoolean( request.getParameter("deleteRights") );
			return result;
		}
		return null;
	}

}
