/**
 * 
 */
package org.digijava.module.gateperm.gates;

import java.util.Map;
import java.util.Queue;

import net.sf.hibernate.Session;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * @author mihai
 *
 */
public class UserLevelGate extends Gate {

	public static final String PARAM_EVERYONE="everyone";
	public static final String PARAM_GUEST="guest";
	public static final String PARAM_OWNER="owner";
	
	public static final MetaInfo[] SCOPE_KEYS  = new MetaInfo[] { GatePermConst.ScopeKeys.CURRENT_MEMBER, GatePermConst.ScopeKeys.ACTIVITY  };
	
	public static final MetaInfo[] PARAM_INFO  = new MetaInfo[] { new MetaInfo("Level",
    "The name of the user level. Eg: everyone(public user), guest(user logged in but no rights)") };

	 private static final String  DESCRIPTION = "Returns access based on the rights level of the current user";
	/**
	 * @param scope
	 * @param parameters
	 */
	public UserLevelGate(Map scope, Queue<String> parameters) {
		super(scope, parameters);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public UserLevelGate() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.digijava.module.gateperm.core.Gate#description()
	 */
	@Override
	public String description() {
		// TODO Auto-generated method stub
		return DESCRIPTION;
	}

	/* (non-Javadoc)
	 * @see org.digijava.module.gateperm.core.Gate#logic()
	 */
	@Override
	public boolean logic() throws Exception {
		Session session = PersistenceManager.getSession();
		String param = parameters.poll().trim();
		
		TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);
		if(tm.getTeamHead()) 
			return true;
		AmpActivity act = (AmpActivity) scope.get(GatePermConst.ScopeKeys.ACTIVITY);
		boolean owner=false;
		if( tm!=null && act.getCreatedBy().getAmpTeamMemId().equals(tm.getMemberId()) ) owner=true;
		
		//if im the owner and this gate checks for ownership....
		if(owner && PARAM_OWNER.equals(param)) return true;
		
		//if im not even a team member 
		if(tm==null) 
			if(PARAM_EVERYONE.equals(param)) return true; else return false;
		
		//if i am a guest and not the owner of the current object i will have guest access
		if(!owner && PARAM_GUEST.equals(param)) return true;
		
		return false;
		
	}

	/* (non-Javadoc)
	 * @see org.digijava.module.gateperm.core.Gate#mandatoryScopeKeys()
	 */
	@Override
	public MetaInfo[] mandatoryScopeKeys() {
		// TODO Auto-generated method stub
		return SCOPE_KEYS;
	}

	/* (non-Javadoc)
	 * @see org.digijava.module.gateperm.core.Gate#parameterInfo()
	 */
	@Override
	public MetaInfo[] parameterInfo() {
		// TODO Auto-generated method stub
		return PARAM_INFO;
	}
	
	
}
