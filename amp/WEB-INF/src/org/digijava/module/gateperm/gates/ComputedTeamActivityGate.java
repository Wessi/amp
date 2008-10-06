/**
 * 
 */
package org.digijava.module.gateperm.gates;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import net.sf.hibernate.Session;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.RelOrganization;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * Implements the logic that allows an user of a computed workspace to access an
 * activity based on the involved organisations present in that activity and the
 * linked organisations of the computed workspace
 * 
 * @author mihai
 */
public class ComputedTeamActivityGate extends Gate {

	public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[] {
			GatePermConst.ScopeKeys.CURRENT_MEMBER};

	public static final MetaInfo[] PARAM_INFO = new MetaInfo[] {};

	private static final String DESCRIPTION = "Implements the logic that allows an user of a computed workspace to access an activity based on the involved organisations"
			+ " present in that activity and the linked organisations of the computed workspace";

	/**
	 * @param scope
	 * @param parameters
	 */
	public ComputedTeamActivityGate(Map scope, Queue<String> parameters) {
		super(scope, parameters);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public ComputedTeamActivityGate() {
		// TODO Auto-generated constructor stub
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digijava.module.gateperm.core.Gate#logic()
	 */
	@Override
	public boolean logic() throws Exception {
		Session session = PersistenceManager.getSession();
		AmpActivity ampa = null;
//		Activity a = null;
		
		Object o = scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
		if (o instanceof AmpActivity)
		    ampa = (AmpActivity) o;
		
		Object oo = scope.get(GatePermConst.ScopeKeys.ACTIVITY);
		if (oo instanceof AmpActivity)
			ampa = (AmpActivity) oo;
//		if (oo instanceof Activity)
//			a = (Activity) oo;

		TeamMember tm = (TeamMember) scope
				.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);

		AmpTeamMember atm = (AmpTeamMember) session.get(AmpTeamMember.class, tm
				.getMemberId());
		PersistenceManager.releaseSession(session);
		User user = atm.getUser();

		Set relatedTeamsForMember = TeamUtil.getRelatedTeamsForMember(tm);
		Set computedOrgs = TeamUtil.getComputedOrgs(relatedTeamsForMember);

		// iterate the assigned orgs:
		if (ampa != null) {
			if (ampa.getOrgrole() == null)
				return false;
			Iterator i = ampa.getOrgrole().iterator();
			while (i.hasNext()) {
				AmpOrgRole element = (AmpOrgRole) i.next();
				Long orgId = element.getOrganisation().getAmpOrgId();
				Iterator ii = computedOrgs.iterator();
				while (ii.hasNext()) {
					AmpOrganisation computedOrg = (AmpOrganisation) ii.next();
					if (computedOrg.getAmpOrgId().equals(orgId))
						return true;
				}
			}

		}
//		if (a != null) {
//			if (a.getRelOrgs() == null)
//				return false;
//			Iterator i = a.getRelOrgs().iterator();
//			while (i.hasNext()) {
//				RelOrganization element = (RelOrganization) i.next();
//				Long orgId = element.getOrgId();
//				Iterator ii = computedOrgs.iterator();
//				while (ii.hasNext()) {
//					AmpOrganisation computedOrg = (AmpOrganisation) ii.next();
//					if (computedOrg.getAmpOrgId().equals(orgId))
//						return true;
//				}
//
//			}
//		}

		return false;
	}

	@Override
	public MetaInfo[] mandatoryScopeKeys() {
		return SCOPE_KEYS;
	}

	@Override
	public MetaInfo[] parameterInfo() {
		return null;
	}

}
