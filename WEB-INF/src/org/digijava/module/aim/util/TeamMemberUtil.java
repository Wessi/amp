/*
 * TeamMemberUtil.java
 * Created : 17-Feb-2006
 */
package org.digijava.module.aim.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.cms.dbentity.CMSContentItem;
import java.util.List;

public class TeamMemberUtil {

	private static Logger logger = Logger.getLogger(TeamMemberUtil.class);

	public static Long getFundOrgOfUser(Long id) {
		Long orgId = null;
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			AmpTeamMember tm = (AmpTeamMember) session.load(AmpTeamMember.class,
					id);
			User user = tm.getUser();

			String qryStr = "select o.ampOrgId from " + AmpOrganisation.class.getName() + " o " +
					"where (o.name=:name)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("name",user.getOrganizationName(),Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				orgId = (Long) itr.next();
			}
		} catch (Exception e) {
			logger.error("Exception from getFundOrgOfUser()");
			e.printStackTrace(System.out);
		}
//		finally {
//			if (session != null) {
//				try {
//					PersistenceManager.releaseSession(session);
//				} catch (Exception rsf) {
//					logger.error("Release session failed");
//				}
//			}
//		}

		logger.info("OrgId is " + orgId);
		return orgId;
	}

	public static Collection getAllTMExceptTL(Long teamId) {
		Session session = null;
		Query qry = null;
		Collection members = new ArrayList();

			try {
				session = PersistenceManager.getSession();
				String queryString = "select tm from " + AmpTeamMember.class.getName()
									 + " tm where (tm.ampTeam=:teamId)";

				qry = session.createQuery(queryString);
				qry.setParameter("teamId", teamId, Hibernate.LONG);
				Iterator itr = qry.list().iterator();

				while (itr.hasNext()) {
					AmpTeamMember ampMem = (AmpTeamMember) itr.next();
					Long id = ampMem.getAmpTeamMemId();
					User user = UserUtils.getUser(ampMem.getUser().getId());
					String name = user.getName();
					String role = ampMem.getAmpMemberRole().getRole();
					AmpTeamMemberRoles ampRole = ampMem.getAmpMemberRole();
					AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
					TeamMember tm = new TeamMember();
					tm.setMemberId(id);
					tm.setMemberName(name);
					tm.setRoleName(role);
					tm.setEmail(user.getEmail());
					if (ampRole.getAmpTeamMemRoleId().equals(
							headRole.getAmpTeamMemRoleId())) {
						tm.setTeamHead(true);
					} else {
						tm.setTeamHead(false);
						if (ampMem.getActivities() == null) {
							tm.setActivities(new HashSet());
						}
						else
							tm.setActivities(ampMem.getActivities());
					}
					if (!tm.getTeamHead())
						members.add(tm);
				}
			} catch (Exception e) {
				logger.error("Unable to get all team members [getAllTMExceptTL()]");
				logger.debug("Exceptiion " + e);
			} finally {
				try {
					if (session != null) {
						PersistenceManager.releaseSession(session);
					}
				} catch (Exception ex) {
					logger.error("releaseSession() failed");
				}
			}
			logger.debug("returning members");
			return members;
	}

	public static AmpTeamMember getAmpTeamMember(Long id) {
		AmpTeamMember ampMember = null;
		Session session = null;

		try {
			session = PersistenceManager.getSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.ampTeamMemId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				ampMember = (AmpTeamMember) itr.next();
			}
			// end
		} catch (Exception ex) {
			logger.error("Unable to get team member " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return ampMember;
	}

	public static AmpTeamMember getMember(String email) {

		User user = DbUtil.getUser(email);
		if (user == null)
			return null;

		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.user=:user)";
			qry = session.createQuery(queryString);
			qry.setParameter("user", user.getId(), Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team member");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return member;

	}

	public static TeamMember getTMTeamHead(Long teamId) {
		AmpTeamMember ampMem	= getTeamHead(teamId);
		Long id 	= ampMem.getAmpTeamMemId();
		User usr 	= UserUtils.getUser(ampMem.getUser().getId());
		String name = usr.getName();
		String role = ampMem.getAmpMemberRole().getRole();
		AmpTeamMemberRoles ampRole = ampMem.getAmpMemberRole();
		AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
		TeamMember tm = new TeamMember();
		tm.setMemberId(id);
		tm.setTeamId(teamId);
		tm.setMemberName(name);
		tm.setRoleName(role);
		tm.setEmail(usr.getEmail());
		if (headRole!=null && ampRole.getAmpTeamMemRoleId().equals(
				headRole.getAmpTeamMemRoleId())) {
			tm.setTeamHead(true);
		} else {
			tm.setTeamHead(false);
		}

		return tm;

	}

	public static AmpTeamMember getTeamHead(Long teamId) {

		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {

			AmpTeamMemberRoles ampRole = getAmpTeamHeadRole();
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampTeam=:teamId) and (tm.ampMemberRole=:role)";
			qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			qry.setParameter("role", ampRole.getAmpTeamMemRoleId(),
					Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team member");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return member;
	}

	public static Collection getMembersUsingRole(Long roleId) {
		Session session = null;
		Query qry = null;
		Collection members = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampMemberRole=:roleId)";

			qry = session.createQuery(queryString);
			qry.setParameter("roleId", roleId, Hibernate.LONG);
			members = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all team members");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("returning members");
		return members;
	}

	public static Collection getAllTeamMembers(Long teamId) {
		Session session = null;
		Query qry = null;
		Collection members = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampTeam=:teamId)";

			qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();

			while (itr.hasNext()) {
				AmpTeamMember ampMem = (AmpTeamMember) itr.next();
				Long id = ampMem.getAmpTeamMemId();
				User user = UserUtils.getUser(ampMem.getUser().getId());
				String name = user.getName();
				String role = ampMem.getAmpMemberRole().getRole();
				AmpTeamMemberRoles ampRole = ampMem.getAmpMemberRole();
				AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
				TeamMember tm = new TeamMember();
				tm.setMemberId(id);
				tm.setMemberName(name);
				tm.setRoleName(role);
				tm.setEmail(user.getEmail());
				tm.setTeamId(teamId);
				if (headRole!=null && ampRole.getAmpTeamMemRoleId().equals(
						headRole.getAmpTeamMemRoleId())) {
					tm.setTeamHead(true);
				} else {
					tm.setTeamHead(false);
				}
				members.add(tm);
			}
		} catch (Exception e) {
			logger.error("Unable to get all team members");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("returning members");
		return members;
	}	
	

    public static Collection<User> getAllTeamMemberUsers() {
        Session session = null;
        Query qry = null;
        Collection<User> users = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select tm from "
                    + AmpTeamMember.class.getName()
                    + " tm";

            qry = session.createQuery(queryString);

            Collection teamMembers=qry.list();
            if(teamMembers!=null){
                users=new ArrayList();
                Iterator itr = teamMembers.iterator();
                while(itr.hasNext()) {
                    AmpTeamMember ampMem = (AmpTeamMember) itr.next();
                    users.add(ampMem.getUser());
                }
            }
        } catch (Exception e) {
            logger.error("Unable to get all team members");
            logger.debug("Exceptiion " + e);
        }
        logger.debug("returning members");
        return users;
	}

	public static Collection getAllMembersUsingActivity(Long activityId) {
		Session session = null;
		Collection col = null;

		try {

			session = PersistenceManager.getSession();
			// modified by Priyajith
			// desc:used select query instead of
			// session.load
			// start
			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a " + "where (a.ampActivityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", activityId, Hibernate.LONG);
			Iterator itrTemp = qry.list().iterator();
			AmpActivity ampActivity = null;
			while (itrTemp.hasNext()) {
				ampActivity = (AmpActivity) itrTemp.next();
			}
			// end

			Iterator itr = ampActivity.getMember().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}
		} catch (Exception e) {
			logger.debug("Exception from getAllMembersUsingActivity()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllMemberActivities(Long memberId) {
		Session session = null;
		Collection col = null;

		try {

			session = PersistenceManager.getRequestDBSession();
			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.ampTeamMemId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", memberId, Hibernate.LONG);

			Iterator itrTemp = qry.list().iterator();
			AmpTeamMember ampMember = null;
			while (itrTemp.hasNext()) {
				ampMember = (AmpTeamMember) itrTemp.next();
			}
			// end

			Iterator itr = ampMember.getActivities().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				AmpActivity activity = (AmpActivity) itr.next();
				Iterator orgItr = activity.getOrgrole().iterator();
				Activity act = new Activity();
				act.setActivityId(activity.getAmpActivityId());
				act.setAmpId(activity.getAmpId());
				act.setName(activity.getName());
				act.setBudget(activity.getBudget());
				act.setUpdatedBy(activity.getUpdatedBy());
				act.setUpdatedDate(activity.getUpdatedDate());
				String donors = "";

				while (orgItr.hasNext()) {
					AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
					if(orgRole.getRole()!=null){
						if (orgRole.getRole().getRoleCode().equals(Constants.DONOR)) {
							if (donors.trim().length() > 0)
								donors += ", ";
							donors += orgRole.getOrganisation().getName();
						}
					}

				}
				act.setDonors(donors);
				col.add(act);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return col;
	}


	public static Collection getAllMemberAmpActivities(Long memberId) {
		Session session = null;
		Collection col = new ArrayList();

		try {

			session = PersistenceManager.getSession();
			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.ampTeamMemId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", memberId, Hibernate.LONG);

			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				AmpTeamMember member = (AmpTeamMember) itr.next();
				col.addAll(member.getActivities());
			}

		} catch (Exception e) {
			logger.debug("Exception from getAllMemberActivities()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static ArrayList getAllMemberReports(Long id) {
		Session session = null;
		ArrayList col = new ArrayList();

		try {

			session = PersistenceManager.getSession();
			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.ampTeamMemId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);

			Iterator itrTemp = qry.list().iterator();
			AmpTeamMember ampTeamMember = null;
			while (itrTemp.hasNext()) {
				ampTeamMember = (AmpTeamMember) itrTemp.next();
			}
			// end

			Iterator itr = ampTeamMember.getReports().iterator();
			while (itr.hasNext()) {
				AmpReports ampReports = (AmpReports) itr.next();
				if (!(ampReports.getAmpReportId().equals(new Long(7)))) {
					logger.debug("inside iter");
					col.add(ampReports);
				}
			}
			Collections.sort(col);
		} catch (Exception e) {
			logger.error("Exception from getAllMemberReports()");
			logger.error(e.toString());
			e.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

        public static List getAllMemberReports(Long id, int currentPage, int reportPerPage) {
                Session session = null;
                List col = new ArrayList();

                try {

                        session = PersistenceManager.getRequestDBSession();
                        // modified by Priyajith
                        // Desc: removed the usage of session.load and used the select query
                        // start

                        String queryString = "select r from "
					+ AmpTeamMember.class.getName() + " t  inner join  t.reports r "
                            + "  where (t.ampTeamMemId=:teamId) order by r.name limit " + currentPage + ", " +
                            reportPerPage;
                        Query qry = session.createQuery(queryString);
                        qry.setLong("teamId", id);
                        col = qry.list();
                } catch (Exception e) {
                        logger.error("Exception from getAllMemberReports()");
                        logger.error(e.toString());
                        e.printStackTrace(System.out);
                }
                return col;
        }
        
        public static List<AmpReports> getAllTeamMembersReports(Long teamId,Integer currentPage,Integer reportPerPage){
    		Session session=null;
    		Query qry=null;
    		List<AmpReports> result=null;    		
    		try {
    			session = PersistenceManager.getRequestDBSession();
    			result=new ArrayList<AmpReports>();
    			String oql= "select r from "
                    + AmpTeamMember.class.getName() + " t  inner join  t.reports r "
                    + "  where (t.ampTeam=:id)";    			
				qry=session.createQuery(oql);
				qry.setParameter("id", teamId,Hibernate.LONG);
				if (currentPage !=null){
	            	   qry.setFirstResult(currentPage);
	               }
	               if(reportPerPage!=null){
	            	   qry.setMaxResults(reportPerPage);
	               }
				result=qry.list();
    			
    		} catch (Exception e) {
    			logger.error("Exception from getAllMemberReports()");
                logger.error(e.toString());
                e.printStackTrace(System.out);
    		}
    		return result;
    	}


        public static Integer getAllMemberReportsCount(Long id) {
             Session session = null;
             Integer count = 0;

             try {

                     session = PersistenceManager.getRequestDBSession();
                     String queryString = "select r from "
                                     + AmpTeamMember.class.getName() + " t  inner join  t.reports r "
                         + "  where (t.ampTeamMemId=:id)";
                     Query qry = session.createQuery(queryString);
                     qry.setLong("id", id);
                     count=qry.list().size();
             } catch (Exception e) {
                     logger.error("Exception from getAllMemberReports()");
                     logger.error(e.toString());
                     e.printStackTrace(System.out);
             }
             return  count;
     }



	public static AmpTeamMember getAmpTeamMember(User user) {
		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.user=:user)";
			qry = session.createQuery(queryString);
			qry.setParameter("user", user.getId(), Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team Member");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return member;
	}

	public static Collection getTeamMembers(Long teamId) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			AmpTeamMember mem = getTeamHead(teamId);
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampTeam=:teamId) and (tm.ampTeamMemId!=:memId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			qry.setParameter("memId", mem.getAmpTeamMemId(), Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpTeamMember ampMem = (AmpTeamMember) itr.next();
				Long id = ampMem.getAmpTeamMemId();
				User user = UserUtils.getUser(ampMem.getUser().getId());
				String name = user.getName();
				String role = ampMem.getAmpMemberRole().getRole();
				TeamMember tm = new TeamMember();
				tm.setMemberId(id);
				tm.setMemberName(name);
				tm.setRoleName(role);
				col.add(tm);
			}
		} catch (Exception e) {
			logger.debug("Exception from getTeamMembers()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static String[] getMemberInformation(Long userId) {
		logger.debug("In getMemberInformation() : " + userId);
		Session session = null;
		String query = " ";
		Iterator iter = null;
		AmpTeamMember tm = null;
		Vector vect = new Vector();
		String[] info = null;

		Collection memCollInfo = new ArrayList();
		try {
			Query q = null;
			session = PersistenceManager.getSession();
			query = "select member from " + AmpTeamMember.class.getName() + " "
					+ "member where (member.user=:memberId)";
			q = session.createQuery(query);
			q.setParameter("memberId", userId, Hibernate.LONG);
			if (q != null) {
				iter = q.list().iterator();
				while (iter.hasNext()) {
					tm = (AmpTeamMember) iter.next();
					vect.add(tm.getAmpTeam().getName());
					vect.add(tm.getAmpMemberRole().getRole());
				}
			}
			info = new String[vect.size()];
			vect.toArray(info);
		} catch (Exception e) {
			logger.debug("Exception in getTeamMemberInformation() : " + e);
			e.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger
						.info("releaseSession() failed for getMemberInformation()");
			}
		}

		return info;
	}

	public static AmpTeamMemberRoles getAmpTeamHeadRole() {
		Session session = null;
		Query qry = null;
		AmpTeamMemberRoles ampRole = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName()
					+ " r where r.teamHead = 1";
			qry = session.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampRole = (AmpTeamMemberRoles) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get role");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampRole;
	}

	public static Collection getAllTeamMemberRoles() {
		Session session = null;
		Query qry = null;
		Collection roles = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName() + " r";
			qry = session.createQuery(queryString);
			roles = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all roles");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return roles;
	}

	public static AmpTeamMemberRoles getAmpTeamMemberRole(Long id) {
		Session session = null;
		Query qry = null;
		AmpTeamMemberRoles role = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName()
					+ " r where (r.ampTeamMemRoleId=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				role = (AmpTeamMemberRoles) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get role");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return role;
	}

	public static AmpTeamMemberRoles getAmpRoleByName(String name) {
		Session session = null;
		Query qry = null;
		AmpTeamMemberRoles role = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName()
					+ " r where (r.role=:name)";
			qry = session.createQuery(queryString);
			qry.setParameter("name", name, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				role = (AmpTeamMemberRoles) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get role");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return role;
	}

	public static Collection getTeamMembers(String email) {
		 User user = org.digijava.module.aim.util.DbUtil.getUser(email);
		 if (user == null) return null;

		Session session = null;
		Query qry = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from " + AmpTeamMember.class.getName() +
			  " tm where (tm.user=:user)";
			qry = session.createQuery(queryString);
			qry.setParameter("user",user.getId(),Hibernate.LONG);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get TeamMembers" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}
	public static Collection getTMTeamMembers(String email) {
		 User user = org.digijava.module.aim.util.DbUtil.getUser(email);
		 if (user == null) return null;

		Session session = null;
		Query qry = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from " + AmpTeamMember.class.getName() +
			  " tm where (tm.user=:user)";
			qry = session.createQuery(queryString);
			qry.setParameter("user",user.getId(),Hibernate.LONG);
			Collection results	= qry.list();
			Iterator itr		= results.iterator();

			while ( itr.hasNext() ) {
				AmpTeamMember ampMem = (AmpTeamMember) itr.next();
				Long id 	= ampMem.getAmpTeamMemId();
				Long teamId	= ampMem.getAmpTeam().getAmpTeamId();
				User usr 	= UserUtils.getUser(ampMem.getUser().getId());
				String name = usr.getName();
				String role = ampMem.getAmpMemberRole().getRole();
				AmpTeamMemberRoles ampRole = ampMem.getAmpMemberRole();
				AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
				TeamMember tm = new TeamMember();
				tm.setMemberId(id);
				tm.setTeamId(teamId);
				tm.setMemberName(name);
				tm.setRoleName(role);
				tm.setEmail(usr.getEmail());
				if (headRole!=null && ampRole.getAmpTeamMemRoleId().equals(
						headRole.getAmpTeamMemRoleId())) {
					tm.setTeamHead(true);
				} else {
					tm.setTeamHead(false);
				}
				col.add( tm );
			}
		} catch (Exception e) {
			logger.error("Unable to get TeamMembers" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}

	public static void assignActivitiesToMember(Long memberId,Long activities[]) {
		Session session = null;
		Transaction tx = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				for (int i = 0;i < activities.length;i ++) {
					if (activities[i] != null) {
						AmpActivity activity = (AmpActivity)session.load(
								AmpActivity.class,activities[i]);
						member.getActivities().add(activity);
					}
				}
				session.update(member);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to assign activities" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
	}

	public static void removeActivitiesFromMember(Long memberId,Long activities[]) {
		Session session = null;
		Transaction tx = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				for (int i = 0;i < activities.length;i ++) {
					if (activities[i] != null) {
						AmpActivity activity = (AmpActivity)session.load(
								AmpActivity.class,activities[i]);
						member.getActivities().remove(activity);
					}
				}
				session.update(member);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to remove activities" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
	}

    public static void removeActivitiesFromMember(Long memberId) {
        Session session = null;
        Transaction tx = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
            if (member != null) {
                tx = session.beginTransaction();
                member.getActivities().clear();
                session.update(member);
                tx.commit();
            }
        } catch (Exception e) {
            logger.error("Unable to remove activities" + e.getMessage());
            e.printStackTrace(System.out);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception rbf) {
                    logger.error("Roll back failed");
                }
            }
        }
	}

	public static Collection getUnassignedDonorMemberActivities(Long teamId,Long memberId) {
		Collection col = new ArrayList();
		Collection col1 = new ArrayList();
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			AmpTeam team = (AmpTeam) session.load(AmpTeam.class,teamId);
			AmpTeamMember member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);

			col1.addAll(team.getActivityList());
			col1.removeAll(member.getActivities());

			Iterator itr1 = col1.iterator();
			while (itr1.hasNext()) {
				AmpActivity act = (AmpActivity) itr1.next();
				Iterator orgItr = act.getOrgrole().iterator();
				Activity activity = new Activity();
				activity.setActivityId(act.getAmpActivityId());
				activity.setName(act.getName());
				activity.setBudget(act.getBudget());
				activity.setAmpId(act.getAmpId());
				act.setUpdatedBy(activity.getUpdatedBy());
				act.setUpdatedDate(activity.getUpdatedDate());
				String donors = "";

				while (orgItr.hasNext()) {
					AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
					if (orgRole.getRole().getRoleCode().equals(Constants.DONOR)) {
						if (donors.trim().length() > 0)
							donors += ", ";
						donors += orgRole.getOrganisation().getName();
					}
				}
				activity.setDonors(donors);
				col.add(activity);
			}

		} catch (Exception e) {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release seesion failed " + rsf.getMessage());
				}
			}
		}
		return col;
	}

	public static Collection getUnassignedMemberActivities(Long teamId,Long memberId) {
		Collection col = null;
		Collection col1 = new ArrayList();
		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();

			String queryString = "select act from " + AmpActivity.class.getName() +
			  " act where (act.team=:id) and (act.approvalStatus!=:status)";
			qry = session.createQuery(queryString);
			qry.setParameter("id",teamId,Hibernate.LONG);
			qry.setParameter("status","started",Hibernate.STRING);
			col = qry.list();

			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);

			col.removeAll(member.getActivities());
			logger.debug("Collection size after remove all:" + col.size());
			col1 = new ArrayList();
			Iterator itr = col.iterator();
			while (itr.hasNext()) {
				AmpActivity activity = (AmpActivity) itr.next();
				Iterator orgItr = activity.getOrgrole().iterator();
				Activity act = new Activity();
				act.setActivityId(activity.getAmpActivityId());
				act.setName(activity.getName());
				act.setBudget(activity.getBudget());
				act.setAmpId(activity.getAmpId());
				act.setUpdatedBy(activity.getUpdatedBy());
				act.setUpdatedDate(activity.getUpdatedDate());
				String donors = "";

				while (orgItr.hasNext()) {
					AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
					if (orgRole.getRole().getRoleCode().equals(Constants.DONOR)) {
						if (donors.trim().length() > 0)
							donors += ", ";
						donors += orgRole.getOrganisation().getName();
					}
				}
				act.setDonors(donors);
				col1.add(act);
			}

		} catch (Exception e) {
			logger.error("Unable to remove activities" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col1;
	}

	public static Collection getMemberLinks(Long memberId) {
		Collection col = new ArrayList();

		Session session = null;

		try {
			session = PersistenceManager.getSession();
			AmpTeamMember tm = (AmpTeamMember) session.load(AmpTeamMember.class,
					memberId);
			Iterator itr = tm.getLinks().iterator();
			while (itr.hasNext()) {
				CMSContentItem cmsItem = (CMSContentItem) itr.next();
				Documents document = new Documents();
				document.setDocId(new Long(cmsItem.getId()));
				document.setTitle(cmsItem.getTitle());
				document.setIsFile(cmsItem.getIsFile());
				document.setFileName(cmsItem.getFileName());
				document.setUrl(cmsItem.getUrl());
				document.setDocDescription(cmsItem.getDescription());
				document.setDate(cmsItem.getDate());
				col.add(document);
			}
		} catch (Exception e) {
			logger.error("Unable to get Member links" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return col;
	}

    public static CMSContentItem getMemberLink(Long memberLinkId) {
        CMSContentItem cmsItem=null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            cmsItem = (CMSContentItem) session.load(CMSContentItem.class,memberLinkId);
        } catch (Exception e) {
            logger.error("Unable to get Member links" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return cmsItem;
	}

	public static Collection getUnassignedMemberReports(Long teamId,Long memberId) {
		Collection col = new ArrayList();

		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();

			String queryString = "select rep.report from " + AmpTeamReports.class.getName() +
			  " rep where (rep.team=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id",teamId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpReports rep = (AmpReports) itr.next();
				col.add(rep);
			}

			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			col.removeAll(member.getReports());
		} catch (Exception e) {
			logger.error("Unable to remove activities" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}


	public static void removeMemberLinks(Long memberId,Long links[]) {
		Session session = null;
		Transaction tx = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
			    Collection col = new ArrayList();
				tx = session.beginTransaction();

				Iterator itr = member.getLinks().iterator();
				while (itr.hasNext()) {
				    CMSContentItem cmsItem = (CMSContentItem) itr.next();
				    boolean flag = false;
				    for (int i = 0;i < links.length;i ++) {
				        if (cmsItem.getId() == links[i].longValue()) {
				            flag = true;
				            session.delete(cmsItem);
				            break;
				        }
				    }
				    if (!flag) {
				        col.add(cmsItem);
				    }
				}
				member.setLinks(new HashSet(col));
				session.update(member);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to remove members link" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
	}

	public static void addLinkToMember(Long memberId,CMSContentItem cmsItem) {
		Session session = null;
		Transaction tx = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				if (member.getLinks() == null)
				    member.setLinks(new HashSet());
				member.getLinks().add(cmsItem);
				session.update(member);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to add Links to members" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
	}

	public static void assignReportsToMember(Long memberId,Long reports[]) {
		Session session = null;
		Transaction tx = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				for (int i = 0;i < reports.length;i ++) {
					if (reports[i] != null) {
						AmpReports report = (AmpReports)session.load(
								AmpReports.class,reports[i]);
						member.getReports().add(report);
					}
				}
				session.update(member);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to assign reports" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
	}

	public static void removeReportsFromMember(Long memberId,Long reports[]) {
		Session session = null;
		Transaction tx = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				for (int i = 0;i < reports.length;i ++) {
					if (reports[i] != null) {
						AmpReports report = (AmpReports)session.load(
								AmpReports.class,reports[i]);
						member.getReports().remove(report);
					}
				}
				session.update(member);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to remove reports" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
	}

	public static Collection getAllTeamMembersToDesktop(Long teamId) {
		Session session = null;
		Query qry = null;
		Collection members = new ArrayList();

		try {
			session = PersistenceManager.getSession();

			// get the team leader of the team
			String queryString = "select t.teamLead.ampTeamMemId from " + AmpTeam.class.getName() +
				" t where (t.ampTeamId=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", teamId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			Long id = new Long(-1);
			if (itr.hasNext()) {
			    Object idObj = itr.next();
			    id = (Long) idObj;
			}
			logger.debug("Got team leader " + id);

			// get all members of the team and also set the team leader
			// flag of the member who is the team leader
			queryString = "select tm.ampTeamMemId,usr.firstNames," +
					"usr.lastName from " + AmpTeamMember.class.getName()
					+ " tm, " + User.class.getName() + " usr " +
							"where tm.user=usr.id and (tm.ampTeam=:teamId)";
			qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			itr = qry.list().iterator();

			Object temp[] = null;
			while (itr.hasNext()) {
			    temp = (Object[]) itr.next();
				TeamMember tm = new TeamMember();
				Long memId = (Long)temp[0];
				tm.setMemberId(memId);
				tm.setMemberName((String)temp[1] + " " + (String)temp[2]);
				if (memId.equals(id)) {
				    tm.setTeamHead(true);
				}
				members.add(tm);
			}
		} catch (Exception e) {
			logger.error("Unable to get all team members");
			logger.debug("Exceptiion " + e);
			e.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("returning members");
		return members;
	}
	
	
	public static void removeTeamMembers(Long id[],Long groupId) {
		Session session = null;
		Transaction tx = null;
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			for (int i = 0;i < id.length;i++) {
				if (id[i] != null) {
					AmpTeamMember ampMember = (AmpTeamMember) session.load(AmpTeamMember.class,id[i]);
					if (isTeamLead(ampMember)) {
						AmpTeam team = ampMember.getAmpTeam();
						team.setTeamLead(null);
						session.update(team);
					}

					qryStr = "select a from " + AmpApplicationSettings.class.getName() +
							" a where (a.member=:memberId)";
					qry = session.createQuery(qryStr);
					qry.setParameter("memberId", id[i], Hibernate.LONG);
					Iterator itr = qry.list().iterator();
					if (itr.hasNext()) {
						logger.info("Got the app settings..");
						AmpApplicationSettings ampAppSettings = (AmpApplicationSettings) itr.next();
						session.delete(ampAppSettings);
						logger.info("deleted the app settings..");
					}

					User user = (User) session.load(User.class,ampMember.getUser().getId());
					Group group = (Group) session.load(Group.class,groupId);
					user.getGroups().remove(group);
					session.update(user);
					// Verify for reports that are owned by this user and delete them
					//DbUtil.deleteReportsForOwner(ampMember.getAmpTeamMemId());
					String queryString = "select rep from " + AmpReports.class.getName() + " rep " + "where rep.ownerId=:oId ";
					qry = session.createQuery(queryString);
					qry.setParameter("oId", ampMember.getAmpTeamMemId(), Hibernate.LONG);
					Iterator it = qry.list().iterator();
					while (it.hasNext()) {
						AmpReports rep = (AmpReports) it.next();
						//verify Default Report in App Settings
						queryString = "select app from " + AmpApplicationSettings.class.getName() + " app " + "where app.defaultTeamReport=:rId ";
						qry = session.createQuery(queryString);
						qry.setParameter("rId", rep.getAmpReportId(), Hibernate.LONG);
						Iterator iter = qry.list().iterator();
						while (iter.hasNext()) {
							AmpApplicationSettings set = (AmpApplicationSettings) iter.next();
							set.setDefaultTeamReport(null);
							session.update(set);
						}
						// delete related information before we delete the report
                                                String deleteTeamReports=" select tr from "+AmpTeamReports.class.getName()+ " tr where tr.report="+rep.getAmpReportId();
                                                session.delete(deleteTeamReports);
						session.delete(rep);
					}
					session.delete(ampMember);
				}
			}
			tx.commit();
		} catch (Exception e) {
			logger.error("Unable to removeTeamMembers " + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
	}

	private static boolean isTeamLead(AmpTeamMember member) {
		Session session = null;

		try {
			session = PersistenceManager.getSession();
			AmpTeam ampTeam = (AmpTeam) session.load(AmpTeam.class,
					member.getAmpTeam().getAmpTeamId());
			if(ampTeam!=null)
				if(ampTeam.getTeamLead()!=null)
					if(ampTeam.getTeamLead().getAmpTeamMemId()!=null)
						if (ampTeam.getTeamLead().getAmpTeamMemId().
									equals(member.getAmpTeamMemId())) {
								return true;
			}
		} catch (Exception e) {
			logger.error("Unable to update team page filters" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return false;
	}
	/**
	 * Retrieves current TeamMember from request
	 * @param HttpServletRequest request
	 * @return AmpTeamMember currentAmpTeamMember
	 */
	public static AmpTeamMember getCurrentAmpTeamMember(HttpServletRequest request){
		TeamMember currentTeamMember = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
		AmpTeamMember currentAmpTeamMember=getAmpTeamMember(currentTeamMember.getMemberId());
		return currentAmpTeamMember;
	}
        public static List getTeamMemberbyUserId(Long userId) throws Exception{


            Session session = null;
            Query qry = null;
            List teamMembers = null;

            try {
                    session = PersistenceManager.getRequestDBSession();
                    String queryString = "select tm.ampTeamMemId from "
                                    + AmpTeamMember.class.getName()
                                    + " tm where (tm.user.id=:user)";
                    qry = session.createQuery(queryString);
                    qry.setParameter("user",userId, Hibernate.LONG);
                    teamMembers= qry.list();
            }
            catch (HibernateException ex) {
                    logger.error("Unable to get team member");
                    logger.debug("Exceptiion " + ex);
                    throw ex;
            }

            return teamMembers;

    }

}
