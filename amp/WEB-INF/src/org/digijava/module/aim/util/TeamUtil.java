/*
 * TeamUtil.java Created: 07-Apr-2005
 */

package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DonorTeam;
import org.digijava.module.aim.helper.ReportsCollection;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Workspace;
import net.sf.hibernate.ObjectNotFoundException;
import java.util.Comparator;
import java.util.List;

/**
 * Persister class for all Team/Workspaces related Objects
 *
 * @author priyajith
 */
public class TeamUtil {

    private static Logger logger = Logger.getLogger(TeamUtil.class);

    /**
     * The function will get all the teams/workspaces which is unassociated with
     * a parent team. It will also filter the teams based on the 'workspace
     * type' and 'team category'
     *
     * @param workspaceType
     * @param teamCategory
     * @return The collection of all teams
     */

    public static Collection getUnassignedWorkspaces(String workspaceType,
        Long teamCategoryId, String team) {

        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getSession();

            // get all teams whose 'parent id' is set to null
            String qryStr = "select t from "
                + AmpTeam.class.getName()
                + " t"
                + " where t.parentTeamId is null and (t.teamCategory=:team) ";

            boolean wTypeFlag = false;
            boolean tCatFlag = false;

            // if user has specified a workspaceType filter
            if(workspaceType != null && workspaceType.trim().length() != 0) {
                qryStr += " and (t.accessType=:wType)";
                wTypeFlag = true;
            }
            // if user has specified team category filter
            if(teamCategoryId != null && teamCategoryId.longValue() != 0) {
                qryStr += " and (t.type=:typeId)";
                tCatFlag = true;
            }

            Query qry = session.createQuery(qryStr);
            qry.setParameter("team", team, Hibernate.STRING);
            if(wTypeFlag) {
                qry.setParameter("wType", workspaceType, Hibernate.STRING);
            }
            if(tCatFlag) {
                qry.setParameter("typeId", teamCategoryId, Hibernate.LONG);
            }

            col = qry.list();

        } catch(Exception e) {
            logger.error("Exception from getUnassignedWorkspcaes : "
                         + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release Session failed");
                }
            }
        }
        return col;
    }

    public static Collection getAllTeams(Long teamId[]) {
        Session session = null;
        Collection col = new ArrayList();

        try {
            if(teamId != null && teamId.length > 0) {
                session = PersistenceManager.getSession();
                StringBuffer qryBuf = new StringBuffer();
                qryBuf.append("select t from ");
                qryBuf.append(AmpTeam.class.getName());
                qryBuf.append(" t where t.ampTeamId in (");
                for(int i = 0; i < teamId.length; i++) {
                    if(teamId[i] != null) {
                        qryBuf.append(teamId[i]);
                        if((i + 1) != teamId.length) {
                            qryBuf.append(",");
                        }
                    }
                }
                qryBuf.append(")");
                Query qry = session.createQuery(qryBuf.toString());
                col = qry.list();
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release session failed");
                }
            }
        }
        return col;
    }

    public static Collection getAllRelatedTeams() {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getSession();
            String query = "select team from " + AmpTeam.class.getName()
                + " team where (team.accessType=:accessType)";
            Query qry = session.createQuery(query);
            qry.setParameter("accessType", "Team");
            col = qry.list();

        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release session failed");
                }
            }
        }
        return col;
    }

    public static Collection getAllRelatedTeamsByType(String type) {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getSession();
            String query = "select team from "
                + AmpTeam.class.getName()
                + " team where (team.accessType=:accessType) and (team.type=:type)";
            Query qry = session.createQuery(query);
            qry.setParameter("accessType", "Team");
            qry.setParameter("type", type);
            col = qry.list();

        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release session failed");
                }
            }
        }
        return col;
    }

    /**
     * Creates a new team
     *
     * @param team
     *            The team object which is to be persisted
     * @param childTeams
     *            The collection of child teams which is to be associated with
     *            the parent team
     * @return true if the team is successfully created else returns false
     */
    public static boolean createTeam(AmpTeam team, Collection childTeams) {
        boolean teamExist = false;
        Session session = null;
        Transaction tx = null;

        try {
            session = PersistenceManager.getSession();
            tx = session.beginTransaction();

            // check whether a team with the same name already exist
            String qryStr = "select t from " + AmpTeam.class.getName() + " t "
                + "where (t.name=:name)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("name", team.getName(), Hibernate.STRING);
            Collection col = qry.list();
            if(col.size() > 0) {
                // throw new AimException("Cannot create team: The team name " +
                // team.getName() + " already exist");
                teamExist = true;
                return teamExist;
            } else {
                // save the new team
                session.save(team);

                qryStr = "select fiscal from "
                    + AmpFiscalCalendar.class.getName() + " fiscal "
                    + "where (fiscal.name=:cal) or (fiscal.yearOffset=:yearOffset)";
                qry = session.createQuery(qryStr);
                qry.setParameter("cal", "Gregorian Calendar", Hibernate.STRING);
                qry.setParameter("yearOffset", new Integer(0), Hibernate.INTEGER);
                col = qry.list();
                AmpFiscalCalendar fiscal = null;
                if(col.size() > 0) {
                    for(Iterator itr = col.iterator(); itr.hasNext(); ) {
                        fiscal = (AmpFiscalCalendar) itr.next();
                        logger.debug("[createTeam(-)] fiscal calendar - " + fiscal.getName());
                        if(fiscal != null)
                            break;
                    }
                } else
                    logger.error("[createTeam(-)] fiscal calendar collection is empty");
                qryStr = "select curr from " + AmpCurrency.class.getName()
                    + " curr " + "where (curr.currencyCode=:code)";
                qry = session.createQuery(qryStr);
                qry.setParameter("code", "USD", Hibernate.STRING);
                col = qry.list();
                AmpCurrency curr = null;
                if(col.size() > 0) {
                    Iterator itr = col.iterator();
                    if(itr.hasNext()) {
                        curr = (AmpCurrency) itr.next();
                    }
                }

                // create application settings for the team and save
                AmpApplicationSettings ampAppSettings = new AmpApplicationSettings();
                ampAppSettings.setTeam(team);
                ampAppSettings.setMember(null);
                ampAppSettings.setDefaultRecordsPerPage(new Integer(10));
                ampAppSettings.setCurrency(curr);
                ampAppSettings.setFiscalCalendar(fiscal);
                ampAppSettings.setLanguage("English");
                if("MOFED".equalsIgnoreCase(team.getTeamCategory()))
                    ampAppSettings.setDefaultPerspective("MOFED");
                else if("DONOR".equalsIgnoreCase(team.getTeamCategory()))
                    ampAppSettings.setDefaultPerspective("DONOR");
                session.save(ampAppSettings);

                // update all child workspaces parent team
                if(childTeams != null && childTeams.size() > 0) {
                    Iterator itr = childTeams.iterator();
                    while(itr.hasNext()) {
                        AmpTeam childTeam = (AmpTeam) itr.next();
                        childTeam.setParentTeamId(team);
                        session.update(childTeam);
                    }
                }

                // commit the changes
                tx.commit();
            }
        } catch(Exception e) {
            /*
             * teamExist = true; logger.error(ae.getMessage());
             */
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Rollback failed");
                }
            }
            throw new RuntimeException(e);

        }
        /*
        * catch (Exception e) { logger.error("Execption from
        * createTeam()"); logger.error(e.getMessage()); if (tx != null) {
        * try { tx.rollback(); } catch (Exception rbf) {
        * logger.error("Rollback failed"); } } }
        finally
        */ {
           if(session != null) {
               try {
                   PersistenceManager.releaseSession(session);
               } catch(Exception rsf) {
                   logger.error("Release session failed");
               }
           }
       }
        return teamExist;
    }

    /**
     * Retreives the team/workspace information
     *
     * @param id
     *            The teamId
     * @return The Workspace object
     */
    public static Workspace getWorkspace(Long id) {
        Workspace workspace = null;
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            String qryStr = "select t from " + AmpTeam.class.getName() + " t "
                + "where (t.ampTeamId=:id)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if(itr.hasNext()) {
                AmpTeam team = (AmpTeam) itr.next();
                workspace = new Workspace();
                workspace.setDescription(team.getDescription().trim());
                workspace.setId(team.getAmpTeamId().toString());
                workspace.setName(team.getName());
                workspace.setTeamCategory(team.getTeamCategory());
                workspace.setType(team.getType());
                workspace.setWorkspaceType(team.getAccessType());
                if(null == team.getRelatedTeamId())
                    workspace.setRelatedTeam(null);
                else
                    workspace.setRelatedTeam(team.getRelatedTeamId()
                                             .getAmpTeamId());
                qryStr = "select count(*) from "
                    + AmpTeamMember.class.getName() + " t "
                    + "where (t.ampTeam=:teamId)";
                qry = session.createQuery(qryStr);
                qry.setParameter("teamId", team.getAmpTeamId(), Hibernate.LONG);
                Iterator itr1 = qry.list().iterator();
                int numMem = 0;
                if(itr1.hasNext()) {
                    Integer num = (Integer) itr1.next();
                    numMem = num.intValue();
                }
                if(numMem == 0) {
                    workspace.setHasMembers(false);
                } else {
                    workspace.setHasMembers(true);
                }
                qryStr = "select count(*) from " + AmpActivity.class.getName()
                    + " act " + "where (act.team=:teamId)";
                qry = session.createQuery(qryStr);
                qry.setParameter("teamId", team.getAmpTeamId(), Hibernate.LONG);
                itr1 = qry.list().iterator();
                int numAct = 0;
                if(itr1.hasNext()) {
                    Integer num = (Integer) itr1.next();
                    numAct = num.intValue();
                }
                if(numAct == 0) {
                    workspace.setHasActivities(false);
                } else {
                    workspace.setHasActivities(true);
                }
                qryStr = "select t from " + AmpTeam.class.getName() + " t "
                    + "where (t.parentTeamId=:teamId)";
                qry = session.createQuery(qryStr);
                qry.setParameter("teamId", team.getAmpTeamId(), Hibernate.LONG);
                itr1 = qry.list().iterator();
                Collection childWorkspaces = new ArrayList();
                while(itr1.hasNext()) {
                    AmpTeam childTeam = (AmpTeam) itr1.next();
                    childWorkspaces.add(childTeam);
                }
                workspace.setChildWorkspaces(childWorkspaces);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release session failed");
                }
            }
        }

        return workspace;
    }

    /**
     * Updates a team/workspace
     *
     * @param team
     * @param childTeams
     * @return
     */
    public static boolean updateTeam(AmpTeam team, Collection childTeams) {
        boolean teamExist = false;
        Session session = null;
        Transaction tx = null;

        try {

            session = PersistenceManager.getSession();
            tx = session.beginTransaction();

            // check whether a team with the same name already exist
            String qryStr = "select t from " + AmpTeam.class.getName() + " t "
                + "where (t.name=:name)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("name", team.getName(), Hibernate.STRING);
            Iterator tempItr = qry.list().iterator();
            if(tempItr.hasNext()) {
                AmpTeam tempTeam = (AmpTeam) tempItr.next();
                if(!(tempTeam.getAmpTeamId().equals(team.getAmpTeamId()))) {
                    // throw new AimException("Cannot create team: The team name
                    // " + team.getName() + " already exist");
                    teamExist = true;
                    return teamExist;
                }
            }

            logger.debug("Updating team....");

            qryStr = "select t from " + AmpTeam.class.getName() + " t "
                + "where (t.ampTeamId=:id)";
            qry = session.createQuery(qryStr);
            qry.setParameter("id", team.getAmpTeamId(), Hibernate.LONG);
            logger.debug("Getting the team with id " + team.getAmpTeamId());
            tempItr = qry.list().iterator();
            if(tempItr.hasNext()) {
                logger.debug("Before update....");
                AmpTeam updTeam = (AmpTeam) tempItr.next();
                updTeam.setName(team.getName());
                updTeam.setDescription(team.getDescription());
                updTeam.setTeamCategory(team.getTeamCategory());
                updTeam.setAccessType(team.getAccessType());
                updTeam.setType(team.getType());
                updTeam.setRelatedTeamId(team.getRelatedTeamId());
                session.saveOrUpdate(updTeam);

                qryStr = "select t from " + AmpTeam.class.getName() + " t "
                    + "where (t.parentTeamId=:parId)";
                qry = session.createQuery(qryStr);
                qry.setParameter("parId", updTeam.getAmpTeamId(),
                                 Hibernate.LONG);

                Iterator itr = qry.list().iterator();
                while(itr.hasNext()) {
                    AmpTeam child = (AmpTeam) itr.next();
                    child.setParentTeamId(null);
                    session.saveOrUpdate(child);
                }

                logger.debug("Team updated");

                if(childTeams != null && childTeams.size() > 0) {
                    itr = childTeams.iterator();
                    logger.info("Size " + childTeams.size());
                    while(itr.hasNext()) {
                        AmpTeam childTeam = (AmpTeam) itr.next();
                        AmpTeam upChildTeam = (AmpTeam) session.load(
                            AmpTeam.class, childTeam.getAmpTeamId());
                        upChildTeam.setParentTeamId(updTeam);
                        session.saveOrUpdate(upChildTeam);
                    }
                }
                // commit the changes
                tx.commit();
            }

        } catch(Exception e) {

            /*
             * teamExist = true; logger.error("Execption from updateTeam() :" +
             * ae.getMessage()); ae.printStackTrace(System.out);
             */
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Rollback failed");
                }
            }
            throw new RuntimeException(e);

        }
        /*
        * catch (Exception e) { logger.error("Execption from updateTeam() :" +
        * e.getMessage()); e.printStackTrace(System.out); if (tx != null) {
        * try { tx.rollback(); } catch (Exception rbf) {
        * logger.error("Rollback failed"); } } }
        finally
        */ {
           if(session != null) {
               try {
                   PersistenceManager.releaseSession(session);
               } catch(Exception rsf) {
                   logger.error("Release session failed");
               }
           }
       }
        return teamExist;
    }

    public static boolean membersExist(Long teamId) {
        boolean memExist = false;
        Session session = null;
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select count(*) from " + AmpTeamMember.class.getName()
                + " tm" + " where (tm.ampTeam=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setParameter("teamId", teamId, Hibernate.LONG);

            Iterator itr = qry.list().iterator();
            if(itr.hasNext()) {
                Integer cnt = (Integer) itr.next();
                logger.info("cnt.intValue = " + cnt.intValue());
                if(cnt.intValue() > 0)
                    memExist = true;
            }

        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return memExist;
    }

    /**
     * Removes a team
     *
     * @param teamId
     *            The team id of the team to be removed
     */
    public static void removeTeam(Long teamId) {
        Session session = null;
        Transaction tx = null;
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            tx = session.beginTransaction();

            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);

            // Remove reference from activity
            qryStr = "select act from " + AmpActivity.class.getName() + " act"
                + " where (act.team=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setLong("teamId", teamId);
            Iterator itr = qry.list().iterator();
            while(itr.hasNext()) {
                AmpActivity act = (AmpActivity) itr.next();
                act.setTeam(null);
                session.update(act);
            }

            // Remove reference from AmpTeamPageFilters
            qryStr = "select tpf from " + AmpTeamPageFilters.class.getName()
                + " tpf" + " where (tpf.team=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setLong("teamId", teamId);
            itr = qry.list().iterator();
            while(itr.hasNext()) {
                AmpTeamPageFilters tpf = (AmpTeamPageFilters) itr.next();
                session.delete(tpf);
            }

            // Remove reference from AmpTeamReports
            qryStr = "select tr from " + AmpTeamReports.class.getName() + " tr"
                + " where (tr.team=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setLong("teamId", teamId);
            itr = qry.list().iterator();
            while(itr.hasNext()) {
                AmpTeamReports tr = (AmpTeamReports) itr.next();
                session.delete(tr);
            }

            // Remove reference from AmpTeam
            qryStr = "select t from " + AmpTeam.class.getName() + " t"
                + " where (t.parentTeamId=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setLong("teamId", teamId);
            itr = qry.list().iterator();
            while(itr.hasNext()) {
                AmpTeam t = (AmpTeam) itr.next();
                t.setParentTeamId(null);
                session.update(t);
            }

            // Remove reference from AmpApplicationSettings
            qryStr = "select a from " + AmpApplicationSettings.class.getName()
                + " a " + "where (a.team=:teamId)";
            qry = session.createQuery(qryStr);
            qry.setLong("teamId", teamId);
            itr = qry.list().iterator();
            if(itr.hasNext()) {
                AmpApplicationSettings as = (AmpApplicationSettings) itr.next();
                session.delete(as);
            }
            session.delete(team);

            tx.commit();
        } catch(ObjectNotFoundException objectNotFoundEx) {
            logger.error("Execption from removeTeam() :" + objectNotFoundEx.getMessage());
            return;
        } catch(Exception ex) {
            logger.error("Execption from removeTeam() :" + ex.getMessage());
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Rollback failed");
                }
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(ex);
        }
    }

    /**
     * Return an AmpTeam object corresponding to the id
     *
     * @param id
     *            The id of the team whose object is to be returned.
     * @return The AmpTeam object
     */
    public static AmpTeam getAmpTeam(Long id) {

        long t2, t1;
        Session session = null;
        AmpTeam team = null;

        try {
            session = PersistenceManager.getSession();
            team = (AmpTeam) session.load(AmpTeam.class, id);
        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return team;
    }

    /**
     * Return an AmpTeamMember object corresponding to the id
     *
     * @param id
     *            The id of the team member whose object is to be returned.
     * @return The AmpTeamMember object
     */
    public static AmpTeamMember getAmpTeamMember(Long id) {
        Session session = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();
            member = (AmpTeamMember) session.load(AmpTeamMember.class, id);
        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return member;
    }

    public static void addTeamMember(AmpTeamMember member,
                                     AmpApplicationSettings appSettings, Site site) {
        Session session = null;
        Transaction tx = null;

        try {
            session = PersistenceManager.getSession();
            tx = session.beginTransaction();
            session.save(member);
            session.save(appSettings);
            if(member.getAmpMemberRole().getTeamHead().booleanValue() == true) {
                AmpTeam team = member.getAmpTeam();
                team.setTeamLead(member);
                session.update(team);
            }
            User user = (User) session.load(User.class, member.getUser()
                                            .getId());
            String qryStr = "select grp from " + Group.class.getName()
                + " grp " + "where (grp.key=:key) and (grp.site=:sid)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("key", Group.EDITORS, Hibernate.STRING);
            qry.setParameter("sid", site.getId(), Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            Group group = null;
            if(itr.hasNext())
                group = (Group) itr.next();
            user.getGroups().add(group);
            tx.commit();
            logger.debug("User added to group " + group.getName());
        } catch(Exception e) {
            logger.error("Exception from addTeamMember :" + e);
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Rollback failed :" + rbf);
                }
            }
            throw new RuntimeException(e);

        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release session failed :" + rsf);
                }
            }
        }
    }

    public static boolean isMemberExisting(Long teamId, String email) {
        Session session = null;
        Query qry = null;
        String qryStr = null;
        Iterator itr = null;
        boolean memberExist = false;

        try {
            session = PersistenceManager.getSession();
            qryStr = "select u.id from " + User.class.getName() + " u "
                + "where (u.email=:email)";
            qry = session.createQuery(qryStr);
            qry.setParameter("email", email, Hibernate.STRING);
            itr = qry.list().iterator();
            logger.debug("Here #1");
            if(itr.hasNext()) {
                Long id = (Long) itr.next();
                logger.debug("Id is " + id);
                qryStr = "select tm.ampTeam from "
                    + AmpTeamMember.class.getName() + ""
                    + " tm where (tm.user=:id)";
                qry = session.createQuery(qryStr);
                qry.setParameter("id", id, Hibernate.LONG);
                Iterator tempItr = qry.list().iterator();
                logger.debug("Here #2");
                if(tempItr.hasNext()) {
                    AmpTeam team = (AmpTeam) tempItr.next();
                    logger.debug("Got team " + team.getAmpTeamId());
                    logger.debug("Checking " + team.getAmpTeamId() + " and "
                                 + teamId);
                    if(team.getAmpTeamId().equals(teamId)) {
                        logger.debug("member already exist for the team");
                        memberExist = true;
                    }
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            if(session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                } catch(Exception rsf) {
                    logger.error("Release session failed");
                }
            }
        }

        return memberExist;
    }

    public static void removeActivitiesFromDonorTeam(Long activities[],
        Long teamId) {
        Session session = null;
        Transaction tx = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();
            tx = session.beginTransaction();

            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);

            for(int i = 0; i < activities.length; i++) {
                AmpActivity activity = (AmpActivity) session.load(
                    AmpActivity.class, activities[i]);

                team.getActivityList().remove(activity);
                Iterator membersItr = activity.getMember().iterator();
                while(membersItr.hasNext()) {
                    member = (AmpTeamMember) membersItr.next();
                    if(member.getAmpTeam().getAmpTeamId().equals(teamId)) {
                        member.getActivities().remove(activity);
                        session.update(member);
                    }
                }
                session.update(team);
                session.flush();
            }

            tx.commit();
        } catch(Exception e) {

            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Roll back failed");
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
    }

    public static void removeActivitiesFromTeam(Long activities[]) {
        Session session = null;
        Transaction tx = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();
            tx = session.beginTransaction();

            for(int i = 0; i < activities.length; i++) {
                AmpActivity activity = (AmpActivity) session.load(
                    AmpActivity.class, activities[i]);
                activity.setTeam(null);
                Iterator membersItr = activity.getMember().iterator();
                while(membersItr.hasNext()) {
                    member = (AmpTeamMember) membersItr.next();
                    member.getActivities().remove(activity);
                    session.update(member);
                }
                activity.setMember(null);
                session.update(activity);
                session.flush();
                // UpdateDB.updateReportCache(activities[i]);
            }

            tx.commit();
        } catch(Exception e) {
            logger.error("Unable to remove activities" + e.getMessage());
            e.printStackTrace(System.out);
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Roll back failed");
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
    }

    public static Collection getAllDonorsToDesktop(Long teamId) {
        Session session = null;
        Query qry = null;
        Collection donors = new ArrayList();

        try {
            session = PersistenceManager.getSession();
            Vector teams = new Vector();
            Vector temp = new Vector();

            teams.add(teamId);
            temp.add(teamId);
            String qryStr = "";
            Long tId = new Long(0);
            String inclause = "";

            ArrayList dbReturnSet = (ArrayList) getAmpLevel0TeamIds(teamId);
            if(dbReturnSet.size() == 0)
                inclause = "'" + teamId + "'";
            else {
                Iterator iter = dbReturnSet.iterator();
                while(iter.hasNext()) {
                    tId = (Long) iter.next();
                    if(inclause == null || inclause.trim().length() == 0) {
                        inclause = "'" + tId + "'";
                    } else {
                        inclause = inclause + ",'" + tId + "'";
                    }
                }
            }

            qryStr = "select act.ampActivityId from "
                + AmpActivity.class.getName() + ""
                + " act where act.team in (" + inclause.toString() + ")";

            qry = session.createQuery(qryStr);
            Iterator itr = qry.list().iterator();
            inclause = "";
            while(itr.hasNext()) {
                if(inclause.length() != 0)
                    inclause += ",";
                inclause += (Long) itr.next();
            }

            if(!"".equals(inclause)) {
                qryStr = "select distinct aor.organisation from "
                    + AmpOrgRole.class.getName() + " aor, "
                    + AmpOrganisation.class.getName() + " " + "org, "
                    + AmpRole.class.getName() + " role where "
                    + "aor.activity in (" + inclause + ") and "
                    + "aor.role = role.ampRoleId and "
                    + "aor.organisation = org.ampOrgId and "
                    + "role.roleCode = '" + Constants.FUNDING_AGENCY + "'"
                    + " order by org.acronym asc";

                qry = session.createQuery(qryStr);
                itr = qry.list().iterator();
                while(itr.hasNext()) {
                    AmpOrganisation ampOrg = (AmpOrganisation) itr.next();
                    if(ampOrg.getAcronym().length() > 20)
                        ampOrg.setAcronym(ampOrg.getAcronym().substring(0, 20)
                                          + "...");
                    donors.add(ampOrg);
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }

        return donors;
    }

    public static void updateTeamPageConfiguration(Long teamId, Long pageId,
        Long filters[]) {
        Session session = null;
        Transaction tx = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            tx = session.beginTransaction();
            AmpTeam ampTeam = (AmpTeam) session.load(AmpTeam.class, teamId);
            AmpPages ampPage = (AmpPages) session.load(AmpPages.class, pageId);
            String qryStr = "select tpf from "
                + AmpTeamPageFilters.class.getName() + " tpf "
                + "where (tpf.team=:tId) and (tpf.page=:pId)";
            qry = session.createQuery(qryStr);
            qry.setParameter("tId", teamId, Hibernate.LONG);
            qry.setParameter("pId", pageId, Hibernate.LONG);
            Iterator tpfItr = qry.list().iterator();
            while(tpfItr.hasNext()) {
                AmpTeamPageFilters ampTpf = (AmpTeamPageFilters) tpfItr.next();
                session.delete(ampTpf);
            }

            for(int i = 0; i < filters.length; i++) {
                AmpFilters ampFilter = (AmpFilters) session.load(
                    AmpFilters.class, filters[i]);
                AmpTeamPageFilters teamPageFilters = new AmpTeamPageFilters();
                teamPageFilters.setFilter(ampFilter);
                teamPageFilters.setPage(ampPage);
                teamPageFilters.setTeam(ampTeam);
                session.save(teamPageFilters);
            }
            tx.commit();
        } catch(Exception e) {
            logger.error("Unable to update team page filters" + e.getMessage());
            e.printStackTrace(System.out);
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Roll back failed");
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
    }

    public static Collection getDonorTeams(Long teamId) {
        // Check whether the team whose donor teams need to be found is a
        // MOFED team

        Collection col = new ArrayList();
        Session session = null;

        try {
            session = PersistenceManager.getSession();

            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);
            if(team.getTeamCategory().equalsIgnoreCase("MOFED")) {
                String qryStr = "select t from " + AmpTeam.class.getName()
                    + " t " + "where (t.relatedTeamId=:tId)";
                Query qry = session.createQuery(qryStr);
                qry.setParameter("tId", teamId, Hibernate.LONG);
                Iterator itr = qry.list().iterator();
                while(itr.hasNext()) {
                    AmpTeam ampTeam = (AmpTeam) itr.next();
                    DonorTeam dt = new DonorTeam();
                    dt.setTeamId(ampTeam.getAmpTeamId());
                    if(ampTeam.getTeamLead() != null) {
                        dt.setTeamMeberId(ampTeam.getTeamLead()
                                          .getAmpTeamMemId());
                        dt.setTeamMemberName(ampTeam.getTeamLead().getUser()
                                             .getEmail());
                    }
                    dt.setTeamName(ampTeam.getName());
                    col.add(dt);
                }
            }

        } catch(Exception e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return col;
    }

    public static Collection getDonorTeamActivities(Long teamId) {

        Collection col = new ArrayList();
        Session session = null;

        try {
            session = PersistenceManager.getSession();

            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);
            Iterator itr = team.getActivityList().iterator();
            while(itr.hasNext()) {
                AmpActivity activity = (AmpActivity) itr.next();
                Collection temp1 = activity.getOrgrole();
                Collection temp2 = new ArrayList();
                Iterator temp1Itr = temp1.iterator();
                while(temp1Itr.hasNext()) {
                    AmpOrgRole orgRole = (AmpOrgRole) temp1Itr.next();
                    if(!temp2.contains(orgRole))
                        temp2.add(orgRole);
                }

                Iterator orgItr = temp2.iterator();

                Activity act = new Activity();
                act.setActivityId(activity.getAmpActivityId());
                act.setName(activity.getName());
                act.setAmpId(activity.getAmpId());
                act.setBudget(activity.getBudget());
                act.setUpdatedBy(activity.getUpdatedBy());
                act.setUpdatedDate(activity.getUpdatedDate());
                String donors = "";

                while(orgItr.hasNext()) {
                    AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
                    AmpRole role = orgRole.getRole();
                    if(role != null){
                      String roleCode = role.getRoleCode();
                      if (roleCode != null && roleCode.equals(Constants.DONOR)) {
                        if (donors.trim().length() > 0) {
                          donors += ", ";
                        }
                        donors += orgRole.getOrganisation().getName();
                      }
                    }
                }

                act.setDonors(donors);
                col.add(act);
            }

        } catch(Exception e) {
            logger.error("Unable to getDonorTeamActivities" + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return col;
    }

    public static Collection getDonorUnassignedActivities(Long dnrTeamId,
        Long teamId) {

        Collection col = new ArrayList();
        Session session = null;

        try {

            Collection temp = getDonorTeamActivities(dnrTeamId);

            session = PersistenceManager.getSession();
            String qryStr = "select act from " + AmpActivity.class.getName()
                + " act where "
                + "(act.team=:teamId) and (act.approvalStatus=:status)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            qry.setParameter("status", Constants.APPROVED_STATUS,
                             Hibernate.STRING);
            Iterator itr = qry.list().iterator();
            while(itr.hasNext()) {
                Activity act = new Activity();
                AmpActivity activity = (AmpActivity) itr.next();
                act.setActivityId(activity.getAmpActivityId());

                if(temp.contains(act) == false) {
                    Collection temp1 = activity.getOrgrole();
                    Collection temp2 = new ArrayList();
                    Iterator temp1Itr = temp1.iterator();
                    while(temp1Itr.hasNext()) {
                        AmpOrgRole orgRole = (AmpOrgRole) temp1Itr.next();
                        if(!temp2.contains(orgRole))
                            temp2.add(orgRole);
                    }

                    Iterator orgItr = temp2.iterator();

                    act.setName(activity.getName());
                    act.setBudget(activity.getBudget());
                    act.setAmpId(activity.getAmpId());
                    act.setUpdatedBy(activity.getUpdatedBy());
                    act.setUpdatedDate(activity.getUpdatedDate());

                    String donors = "";

                    while(orgItr.hasNext()) {
                        AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
                        AmpRole role = orgRole.getRole();
                        if(role != null){
                          String roleCode = role.getRoleCode();
                          if (roleCode!=null&&roleCode.equals(
                              Constants.DONOR)) {
                            if (donors.trim().length() > 0) {
                              donors += ", ";
                            }
                            donors += orgRole.getOrganisation().getName();
                          }
                        }
                    }

                    act.setDonors(donors);
                    col.add(act);
                }
            }

        } catch(Exception e) {
            logger.error("Unable to getDonorTeamActivities" + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return col;
    }

    public static void assignActivitiesToDonor(Long dnrTeamId,
                                               Long activityId[]) {
        Session session = null;
        Transaction tx = null;

        try {
            session = PersistenceManager.getSession();
            tx = session.beginTransaction();

            AmpTeam ampTeam = (AmpTeam) session.get(AmpTeam.class, dnrTeamId);
            if(ampTeam.getActivityList() == null) {
                ampTeam.setActivityList(new HashSet());
            }
            logger.info("ActivityId length = " + activityId.length);
            for(int i = 0; i < activityId.length; i++) {
                logger.info("Id = " + activityId[i]);
                if(activityId[i] != null) {
                    AmpActivity ampActivity = (AmpActivity) session.get(
                        AmpActivity.class, activityId[i]);
                    ampTeam.getActivityList().add(ampActivity);
                }
            }

            session.update(ampTeam);
            tx.commit();

        } catch(Exception e) {
            logger.error("Unable to assignActivitiesToDonor" + e.getMessage());
            e.printStackTrace(System.out);
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Rollback failed");
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
    }

    public static void removeActivitiesFromDonor(Long dnrTeamId,
                                                 Long activityId[]) {
        Session session = null;
        Transaction tx = null;

        try {
            session = PersistenceManager.getSession();
            tx = session.beginTransaction();

            AmpTeam ampTeam = (AmpTeam) session.get(AmpTeam.class, dnrTeamId);
            Set newList = new HashSet();

            Iterator itr = ampTeam.getActivityList().iterator();
            while(itr.hasNext()) {
                AmpActivity act = (AmpActivity) itr.next();
                boolean present = false;
                for(int i = 0; i < activityId.length; i++) {
                    if(act.getAmpActivityId().longValue() == activityId[i]
                       .longValue()) {
                        present = true;
                        break;
                    }
                }
                if(!present) {
                    newList.add(act);
                }
            }

            ampTeam.setActivityList(newList);

            session.update(ampTeam);
            tx.commit();

        } catch(Exception e) {
            logger.error("Unable to assignActivitiesToDonor" + e.getMessage());
            e.printStackTrace(System.out);
            if(tx != null) {
                try {
                    tx.rollback();
                } catch(Exception rbf) {
                    logger.error("Rollback failed");
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
    }

    public static boolean checkForParentTeam(Long ampTeamId) {
        Session session = null;
        Query q = null;
        boolean ans = false;
        try {
            session = PersistenceManager.getSession();
            String qry = "select tm from " + AmpTeam.class.getName()
                + " tm where tm.parentTeamId=:ampTeamId";
            q = session.createQuery(qry);
            q.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
            if(q != null && q.list().size() > 0)
                ans = false;
            else
                ans = true;
        } catch(Exception ex) {
            logger.error("Unable to get AmpTeam [checkForParentTeam()]", ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
            }
        }
        logger.debug("Getting checkForParentTeam Executed successfully ");
        return ans;
    }

    public static AmpTeam getTeamByName(String teamName) {
        Session session = null;
        Query qry = null;
        AmpTeam team = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select t from " + AmpTeam.class.getName()
                + " t where (t.name=:teamName)";
            qry = session.createQuery(queryString);
            qry.setParameter("teamName", teamName, Hibernate.STRING);
            Iterator itr = qry.list().iterator();
            if(itr.hasNext()) {
                team = (AmpTeam) itr.next();
            }
        } catch(Exception e) {
            logger.error("Unable to get team");
            logger.debug("Exceptiion " + e);
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return team;
    }

    public static Collection getAllTeamAmpActivities(Long teamId) {
        Session session = null;
        Collection col = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select act from "
                + AmpActivity.class.getName()
                + " act where (act.team=:teamId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            col = qry.list();
        } catch(Exception e) {
            logger.debug("Exception from getAllTeamAmpActivities()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return col;
    }

    public static boolean hasActivities(Long teamId) {
        Session session = null;
        boolean flag = false;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select count(*) from "
                + AmpActivity.class.getName()
                + " act where (act.team=:teamId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while(itr.hasNext()) {
                Integer numActivities = (Integer) itr.next();
                if(numActivities.intValue() != 0) {
                    flag = true;
                }
            }
        } catch(Exception e) {
            logger.debug("Exception from hasActivities()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return flag;
    }

    public static Collection getManagementTeamActivities(Long teamId) {
        Session session = null;
        Collection col = new ArrayList();
        String queryString = "";
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            Collection childIds = DesktopUtil.getAllChildrenIds(teamId);
            childIds.add(teamId);
            if(childIds != null && childIds.size() > 0) {
                Iterator itr = childIds.iterator();
                String params = "";
                while(itr.hasNext()) {
                    Long id = (Long) itr.next();
                    if(params.length() > 0) {
                        params += ",";
                    }
                    params += id;
                }
                queryString = "select act from " + AmpActivity.class.getName()
                    + " " + "act where act.team in (" + params + ")";
                qry = session.createQuery(queryString);

                itr = qry.list().iterator();
                while(itr.hasNext()) {

                    AmpActivity activity = (AmpActivity) itr.next();
                    Collection temp1 = activity.getOrgrole();
                    Collection temp2 = new ArrayList();
                    Iterator temp1Itr = temp1.iterator();
                    while(temp1Itr.hasNext()) {
                        AmpOrgRole orgRole = (AmpOrgRole) temp1Itr.next();
                        if(!temp2.contains(orgRole))
                            temp2.add(orgRole);
                    }

                    Iterator orgItr = temp2.iterator();

                    Activity act = new Activity();
                    act.setActivityId(activity.getAmpActivityId());
                    act.setName(activity.getName());
                    act.setAmpId(activity.getAmpId());
                    act.setBudget(activity.getBudget());
                    act.setUpdatedBy(activity.getUpdatedBy());
                    act.setUpdatedDate(activity.getUpdatedDate());
                    String donors = "";

                    while(orgItr.hasNext()) {
                        AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
                        if(orgRole.getRole().getRoleCode().equals(
                            Constants.DONOR)) {
                            if(donors.trim().length() > 0) {
                                donors += ", ";
                            }
                            donors += orgRole.getOrganisation().getName();
                        }
                    }

                    act.setDonors(donors);
                    col.add(act);

                }

            }
        } catch(Exception e) {
            logger.debug("Exception from getAllTeamActivities()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return col;
    }

    public static Collection getAllTeamActivities(Long teamId) {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getSession();

            String queryString = "";
            Query qry = null;

            if(teamId != null) {
                queryString = "select act from " + AmpActivity.class.getName()
                    + " act where (act.team=:teamId)";
                qry = session.createQuery(queryString);
                qry.setParameter("teamId", teamId, Hibernate.LONG);

            } else {
                queryString = "select act from " + AmpActivity.class.getName()
                    + " act where act.team is null";
                qry = session.createQuery(queryString);
            }

            Iterator itr = qry.list().iterator();

            while(itr.hasNext()) {

                AmpActivity activity = (AmpActivity) itr.next();
                Collection temp1 = activity.getOrgrole();
                Collection temp2 = new ArrayList();
                Iterator temp1Itr = temp1.iterator();
                while(temp1Itr.hasNext()) {
                    AmpOrgRole orgRole = (AmpOrgRole) temp1Itr.next();
                    if(!temp2.contains(orgRole))
                        temp2.add(orgRole);
                }

                Iterator orgItr = temp2.iterator();

                Activity act = new Activity();
                act.setActivityId(activity.getAmpActivityId());
                act.setName(activity.getName());
                act.setBudget(activity.getBudget());
                act.setAmpId(activity.getAmpId());
                act.setUpdatedBy(activity.getUpdatedBy());
                act.setUpdatedDate(activity.getUpdatedDate());
                String donors = "";

                while(orgItr.hasNext()) {
                  AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
                  AmpRole ampRole = orgRole.getRole();
                  if (ampRole != null) {
                    String roleCode = ampRole.getRoleCode();
                    if (roleCode != null && roleCode.equals(Constants.DONOR)) {
                      if (donors.trim().length() > 0) {
                        donors += ", ";
                      }
                      donors += orgRole.getOrganisation().getName();
                    }
                  }
                }
                act.setDonors(donors);
                col.add(act);

            }

        } catch(Exception e) {
            logger.debug("Exception from getAllTeamActivities()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return col;
    }

    /*
     * return ReportsCollection Object
     */
    public static Collection getTeamReportsCollection(Long teamId) {
        Session session = null;
        ArrayList col = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "select tr from "
                + AmpTeamReports.class.getName()
                + " tr where (tr.team=:teamId) order by tr.report";
            Query qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            col = new ArrayList();
            while(itr.hasNext()) {
                AmpTeamReports ampTeamRep = (AmpTeamReports) itr.next();
                // modified by Priyajith
                // desc:used select query instead of session.load
                // start
                queryString = "select r from " + AmpReports.class.getName()
                    + " r " + "where (r.ampReportId=:id) order by r.name";
                qry = session.createQuery(queryString);
                qry.setParameter("id", ampTeamRep.getReport().getAmpReportId(),
                                 Hibernate.LONG);
                Iterator itrTemp = qry.list().iterator();
                AmpReports ampReport = null;
                while(itrTemp.hasNext()) {
                    ampReport = (AmpReports) itrTemp.next();
                }
                // end
                ReportsCollection rc = new ReportsCollection();
                rc.setReport(ampReport);
                if(ampTeamRep.getTeamView() == false) {
                    rc.setTeamView(false);
                } else {
                    rc.setTeamView(true);
                }

                col.add(rc);

            }
            Collections.sort(col);
        } catch(Exception e) {
            logger.debug("Exception from getTeamReportsCollection");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return col;
    }

    public static Collection getAllTeamReports(Long teamId) {
        Session session = null;
        Collection col = new ArrayList();
        try {
            session = PersistenceManager.getSession();
            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);

            String queryString = null;
            Query qry = null;

            if(team.getAccessType().equalsIgnoreCase(
                Constants.ACCESS_TYPE_MNGMT)) {
                queryString = "select r from " + AmpReports.class.getName()
                    + " r " + " order by r.name";
                qry = session.createQuery(queryString);
                col = qry.list();
            } else {
                queryString = "select tr from "
                    + AmpTeamReports.class.getName()
                    + " tr where (tr.team=:teamId)";
                qry = session.createQuery(queryString);
                qry.setParameter("teamId", teamId, Hibernate.LONG);
                Iterator itr = qry.list().iterator();
                col = new ArrayList();
                StringBuffer qryBuffer = new StringBuffer();
                AmpTeamReports ampTeamRep = null;
                while(itr.hasNext()) {
                    ampTeamRep = (AmpTeamReports) itr.next();
                    if(qryBuffer.length() != 0)
                        qryBuffer.append(",");
                    qryBuffer.append(ampTeamRep.getReport().getAmpReportId());
                }

                if(qryBuffer != null && qryBuffer.length() > 0) {
                    queryString = "select r from " + AmpReports.class.getName()
                        + " r " + "where r.ampReportId in (" + qryBuffer
                        + ") order by r.name";
                    qry = session.createQuery(queryString);
                    col = qry.list();
                }
                Collections.sort((ArrayList) col);
            }
        } catch(Exception e) {
            logger.debug("Exception from getAllTeamReports()");
            logger.error(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return col;
    }

    public static List getAllTeamReports(Long teamId, int currentPage, int reportPerPage) {
       Session session = null;
       List col = new ArrayList();
       try {
           session = PersistenceManager.getRequestDBSession();
           AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);

           String queryString = null;
           Query qry = null;

           if(team.getAccessType().equalsIgnoreCase(
               Constants.ACCESS_TYPE_MNGMT)) {
               queryString = "select r from " + AmpReports.class.getName()
                   + " r " + " order by r.name limit "+currentPage+ ", "+reportPerPage;
               qry = session.createQuery(queryString);
               col = qry.list();
           } else {
               queryString = "select r from "
                   + AmpTeamReports.class.getName()+" tr inner join  tr.report r "
                   + "  where (tr.team=:teamId) order by r.name limit "+currentPage+ ", "+reportPerPage;
               qry = session.createQuery(queryString);
               qry.setParameter("teamId", teamId, Hibernate.LONG);
               col = qry.list();
           }
       } catch(Exception e) {
           logger.debug("Exception from getAllTeamReports()");
           logger.error(e.toString());
           throw new RuntimeException(e);
       }
       return col;
   }

   public static int getAllTeamReportsCount(Long teamId) {
       Session session = null;
       int count=0;
       try {
           session = PersistenceManager.getRequestDBSession();
           AmpTeam team = (AmpTeam) session.load(AmpTeam.class, teamId);

           String queryString = null;
           Query qry = null;

           if(team.getAccessType().equalsIgnoreCase(
               Constants.ACCESS_TYPE_MNGMT)) {
               queryString = "select r from " + AmpReports.class.getName()
                   + " r " + " order by r.name ";
               qry = session.createQuery(queryString);
               count=(Integer)qry.uniqueResult();
           } else {
               queryString = "select r from "
                   + AmpTeamReports.class.getName()+" tr inner join tr.report r "
                   + "  where (tr.team=:teamId) ";
               qry = session.createQuery(queryString);
               qry.setParameter("teamId", teamId, Hibernate.LONG);
              count=qry.list().size();
           }
       } catch(Exception e) {
           logger.debug("Exception from getAllTeamReports()");
           logger.error(e.toString());
           throw new RuntimeException(e);
       }
       return count;
   }



    public static AmpTeamReports getAmpTeamReport(Long teamId, Long reportId) {
        Session session = null;
        AmpTeamReports ampTeamRep = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "select tr from "
                + AmpTeamReports.class.getName()
                + " tr where (tr.team=:teamId) and (tr.report=:reportId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            qry.setParameter("reportId", reportId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if(itr.hasNext()) {
                ampTeamRep = (AmpTeamReports) itr.next();
            }
        } catch(Exception e) {
            logger.debug("Exception from getAmpTeamReport()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return ampTeamRep;
    }

    public static Collection getAllUnassignedActivities() {
        return getAllTeamActivities(null);
    }

    public static Collection getAllUnassignedTeamReports(Long id) {
        Session session = null;
        Collection col = null;
        Collection col1 = null;

        try {
            col = DbUtil.getAllReports();
            session = PersistenceManager.getSession();

            String queryString = "select tr from "
                + AmpTeamReports.class.getName()
                + " tr where (tr.team=:teamId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("teamId", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            col1 = new ArrayList();
            while(itr.hasNext()) {
                AmpTeamReports ampTeamRep = (AmpTeamReports) itr.next();
                // modified by Priyajith
                // desc:used select query instead of session.load
                // start
                queryString = "select r from " + AmpReports.class.getName()
                    + " r " + "where (r.ampReportId=:id)";
                qry = session.createQuery(queryString);
                qry.setParameter("id", ampTeamRep.getReport().getAmpReportId(),
                                 Hibernate.LONG);
                Iterator itrTemp = qry.list().iterator();
                AmpReports ampReport = null;
                while(itrTemp.hasNext()) {
                    ampReport = (AmpReports) itrTemp.next();
                }
                // end
                col1.add(ampReport);
            }

            Iterator itr2 = col1.iterator();

            while(itr2.hasNext()) {
                AmpReports rep = (AmpReports) itr2.next();
                Iterator itr1 = col.iterator();
                while(itr1.hasNext()) {
                    AmpReports tempRep = (AmpReports) itr1.next();
                    if(tempRep.getAmpReportId().equals(rep.getAmpReportId())) {
                        col.remove(tempRep);
                        break;
                    }
                }
            }

        } catch(Exception e) {
            logger.debug("Exceptiion from getAllUnassignedTeamReports()");
            logger.debug("Exceptiion " + e);
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
            }
        }
        return col;
    }

    public static Collection getAllTeams() {
        Session session = null;
        Query qry = null;
        Collection teams = new ArrayList();

        try {
            session = PersistenceManager.getSession();
            String queryString = "select t from " + AmpTeam.class.getName()
                + " t";
            qry = session.createQuery(queryString);
            teams = qry.list();
        } catch(Exception e) {
            logger.debug("cannot get All teams");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.error("releaseSession() failed");
                throw new RuntimeException(ex);
            }
        }
        return teams;
    }

    public static Set getAmpLevel0Teams(Long ampTeamId) {
        Session session = null;
        Set teams = new TreeSet();

        try {
            logger.debug("Team Id:" + ampTeamId);
            session = PersistenceManager.getSession();
            String queryString = "select t from " + AmpTeam.class.getName()
                + " t " + "where (t.parentTeamId.ampTeamId=:ampTeamId)";
            logger.debug("Query String:" + queryString);
            Query qry = session.createQuery(queryString);
            qry.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
            Iterator itrTemp = qry.list().iterator();
            AmpTeam ampTeam = null;
            LinkedList list = new LinkedList();
            list.addAll(qry.list());
            while(list.size() > 0) {
                ampTeam = (AmpTeam) list.removeFirst();
                if(ampTeam.getAccessType().equals("Team"))
                    teams.add(ampTeam);
                else {
                    queryString = "select t from " + AmpTeam.class.getName()
                        + " t " + "where (t.parentTeamId.ampTeamId="
                        + ampTeam.getAmpTeamId() + ")";
                    qry = session.createQuery(queryString);
                    list.addAll(qry.list());
                }

                // ampTeam = (AmpTeam) itrTemp.next();
                // teams.add(ampTeam.getAmpTeamId());
            }
            logger.debug("Size: " + teams.size());

        } catch(Exception e) {
            logger.debug("Exception from getAmpLevel0Team()" + e.getMessage());
            logger.debug(e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if(session != null) {
                    PersistenceManager.releaseSession(session);
                }
            } catch(Exception ex) {
                logger.debug("releaseSession() failed");
                logger.debug(ex.toString());
            }
        }
        return teams;
    }

    public static Collection getAmpLevel0TeamIds(Long ampTeamId) {
        Set teams = getAmpLevel0Teams(ampTeamId);
        Collection ret = new ArrayList();
        Iterator i = teams.iterator();
        while(i.hasNext()) {
            AmpTeam element = (AmpTeam) i.next();
            ret.add(element.getAmpTeamId());
        }
        return ret;
    }

    /**
     * Retrieves current Team from request
     * @param request
     * @return currentAmpTeam
     */
    public static AmpTeam getCurrentTeam(HttpServletRequest request) {
        AmpTeam currentAmpTeam = TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeam();
        return currentAmpTeam;
    }

    public static class HelperAmpTeamNameComparator
        implements Comparator {
        public int compare(Object obj1, Object obj2) {
            AmpTeam team1 = (AmpTeam) obj1;
            AmpTeam team2 = (AmpTeam) obj2;
            return team1.getName().compareTo(team2.getName());
        }

    }

}
