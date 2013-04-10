/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.digijava.module.gateperm.gates;

import java.util.Map;
import java.util.Queue;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * This gate allows access on activities belonging to a workspace in which the current user belongs
 * {@link Gate#getParameters()}
 * 
 * @author mpostelnicu@dgateway.org
 * @since 10 apr 2013
 */
public class RegularTeamActivityGate extends Gate {

	public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER };

	public static final MetaInfo[] PARAM_INFO = new MetaInfo[] {};
	
	private static final String DESCRIPTION = "This gate returns true if the CURRENT_MEMBER belongs to the workspace with the Id given as parameter";

	/**
	 * @param scope
	 * @param parameters
	 */
	public RegularTeamActivityGate(Map scope, Queue<String> parameters) {
		super(scope, parameters);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public RegularTeamActivityGate() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digijava.module.gateperm.core.Gate#logic()
	 */
	@Override
	public boolean logic() throws Exception {
		
		Object o = scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
		AmpActivityVersion ampa = null;
		if (o instanceof AmpActivityVersion)
		    ampa = (AmpActivityVersion) o;
		
		TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
		AmpTeam currentTeam = TeamUtil.getAmpTeam(tm.getTeamId());
		AmpTeam activityTeam=ampa.getTeam();
		
		//current team same as activity team=>always access
		if (currentTeam.getAmpTeamId().equals(activityTeam.getAmpTeamId()))
				return true;
			
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digijava.module.gateperm.core.Gate#parameterInfo()
	 */
	@Override
	public MetaInfo[] parameterInfo() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digijava.module.gateperm.core.Gate#mandatoryScopeKeys()
	 */
	@Override
	public MetaInfo[] mandatoryScopeKeys() {
		return SCOPE_KEYS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digijava.module.gateperm.core.Gate#description()
	 */
	@Override
	public String description() {
		return DESCRIPTION;
	}

}
