/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005 
 */

package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AmpProjectDonor;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.RelOrganization;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.cms.dbentity.CMSContentItem;

/**
 * ActivityUtil is the persister class for all activity related 
 * entities 
 * 
 * @author Priyajith
 */
public class ActivityUtil {
	
	private static Logger logger = Logger.getLogger(ActivityUtil.class);
	
	/**
	 * Persists an AmpActivity object to the database
	 * This function is used to create a new activity
	 * @param activity The activity to be persisted
	 */
	public static void saveActivity(AmpActivity activity, ArrayList commentsCol,
			boolean serializeFlag, Long field,Collection relatedLinks,Long memberId) {
		/*
		 * calls saveActivity(AmpActivity activity,Long oldActivityId,boolean edit)
		 * by passing null and false to the parameters oldActivityId and edit respectively
		 * since this is creating a new activity
		 */
		saveActivity(activity,null,false,commentsCol,serializeFlag,field,relatedLinks,memberId);
	}
	
	/**
	 * Persist an AmpActivity object to the database
	 * This function is used to either update an existing activity
	 * or creating a new activity. If the parameter 'edit' is set to
	 * true the function will update an existing activity with id
	 * given by the parameter 'oldActivityId'. If the 'edit' parameter
	 * is false, the function will create a new activity  
	 * 
	 * @param activity The AmpActivity object to be persisted
	 * @param oldActivityId The id of the AmpActivity object which is to be updated
	 * @param edit This boolean variable represents whether to create a new
	 * activity object or to update the existing activity object
	 */
	public static void saveActivity(AmpActivity activity,Long oldActivityId,boolean edit,
			ArrayList commentsCol,boolean serializeFlag, Long field,
			Collection relatedLinks,Long memberId) {
		logger.debug("In save activity " + activity.getName());
		Session session = null;
		Transaction tx = null;
		AmpActivity oldActivity = null;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			
			if (edit) { /* edit an existing activity */
			    oldActivity = (AmpActivity) session.load(AmpActivity.class,oldActivityId);
			    
			    activity.setAmpActivityId(oldActivityId);
				
			    
				if (oldActivity == null) {
					logger.debug("Previous Activity is null");
				}
				
				/* delete previos fundings and funding details */
				Set fundSet = oldActivity.getFunding();
				if (fundSet != null) {
					Iterator fundSetItr = fundSet.iterator();
					while (fundSetItr.hasNext()) {
						AmpFunding fund = (AmpFunding) fundSetItr.next();
						Set fundDetSet = fund.getFundingDetails();
						if (fundDetSet != null) {
							Iterator fundDetItr = fundDetSet.iterator();
							while (fundDetItr.hasNext()) {
								AmpFundingDetail ampFundingDetail = (AmpFundingDetail) fundDetItr.next();
								session.delete(ampFundingDetail);
							}
						}
						session.delete(fund);
					}
				}
				
				/* delete previous regional fundings */
				fundSet = oldActivity.getRegionalFundings();
				if (fundSet != null) {
					Iterator fundSetItr = fundSet.iterator();
					while (fundSetItr.hasNext()) {
						AmpRegionalFunding regFund = (AmpRegionalFunding) fundSetItr.next();
						session.delete(regFund);
					}
				}
				
				/* delete all previous components */
				Set comp = oldActivity.getComponents();
				if (comp != null) {
					Iterator compItr = comp.iterator();
					while (compItr.hasNext()) {
						AmpComponent ampComp = (AmpComponent) compItr.next();
						session.delete(ampComp);
					}
				}
				
				/* delete all previous org roles */
				Set orgrole = oldActivity.getOrgrole();
				if (orgrole != null) {
					Iterator orgroleItr = orgrole.iterator();
					while (orgroleItr.hasNext()) {
						AmpOrgRole ampOrgrole = (AmpOrgRole) orgroleItr.next();
						session.delete(ampOrgrole);
					}
				}				
				
				/* delete all previous closing dates */
				Set closeDates = oldActivity.getClosingDates();
				if (closeDates != null) {
					Iterator dtItr = closeDates.iterator();
					while (dtItr.hasNext()) {
						AmpActivityClosingDates date = (AmpActivityClosingDates) dtItr.next();
						session.delete(date);
					}
				}				

				/* delete all previous issues */
				Set issues = oldActivity.getIssues();
				if (issues != null) {
					Iterator iItr = issues.iterator();
					while (iItr.hasNext()) {
						AmpIssues issue = (AmpIssues) iItr.next();
						session.delete(issue);
					}
				}	

				/* delete all previous comments */
				if (!commentsCol.isEmpty()) {
					ArrayList col = org.digijava.module.aim.util.DbUtil.getAllCommentsByField(field,oldActivity.getAmpActivityId());
					logger.debug("col.size() [Inside deleting]: " + col.size());
						if (col != null) {
							Iterator itr = col.iterator();
							while (itr.hasNext()) {
								AmpComments comObj = (AmpComments) itr.next();
								session.delete(comObj);
							}
						}
					logger.debug("comments deleted");
				}
				else
					logger.debug("commentsCol is empty");

				oldActivity.getClosingDates().clear();
				oldActivity.getComponents().clear();
				oldActivity.getDocuments().clear();
				oldActivity.getFunding().clear();
				oldActivity.getInternalIds().clear();
				oldActivity.getLocations().clear();
				oldActivity.getOrgrole().clear();
				oldActivity.getSectors().clear();				
				
				oldActivity.setActualApprovalDate(activity.getActualApprovalDate());
				oldActivity.setActualCompletionDate(activity.getActualCompletionDate());
				oldActivity.setActualStartDate(activity.getActualStartDate());
				oldActivity.setAmpId(activity.getAmpId());
				oldActivity.setCalType(activity.getCalType());				
				oldActivity.setCondition(activity.getCondition());
				oldActivity.setContFirstName(activity.getContFirstName());
				oldActivity.setContLastName(activity.getContLastName());
				oldActivity.setContractors(activity.getContractors());
				oldActivity.setCountry(activity.getCountry());
				oldActivity.setDescription(activity.getDescription());
				oldActivity.setEmail(activity.getEmail());
				oldActivity.setLanguage(activity.getLanguage());
				oldActivity.setLevel(activity.getLevel());
				oldActivity.setModality(activity.getModality());
				oldActivity.setMofedCntEmail(activity.getMofedCntEmail());
				oldActivity.setMofedCntFirstName(activity.getMofedCntFirstName());
				oldActivity.setMofedCntLastName(activity.getMofedCntLastName());
				oldActivity.setName(activity.getName());
				oldActivity.setObjective(activity.getObjective());
				oldActivity.setProgramDescription(activity.getProgramDescription());
				oldActivity.setProposedApprovalDate(activity.getProposedApprovalDate());
				oldActivity.setProposedStartDate(activity.getProposedStartDate());
				oldActivity.setStatus(activity.getStatus());
				oldActivity.setStatusReason(activity.getStatusReason());
				oldActivity.setThemeId(activity.getThemeId());
				oldActivity.setUpdatedDate(activity.getUpdatedDate());
				oldActivity.setClosingDates(activity.getClosingDates());
				oldActivity.setComponents(activity.getComponents());
				//oldActivity.setDocuments(activity.getDocuments());
				oldActivity.setFunding(activity.getFunding());
				oldActivity.setRegionalFundings(activity.getRegionalFundings());
				
				oldActivity.setInternalIds(activity.getInternalIds());
				oldActivity.setLocations(activity.getLocations());
				oldActivity.setOrgrole(activity.getOrgrole());
				oldActivity.setSectors(activity.getSectors());
				oldActivity.setIssues(activity.getIssues());
				
				oldActivity.setApprovalStatus(activity.getApprovalStatus());
			}

			Iterator itr = relatedLinks.iterator();
			AmpTeamMember member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			while (itr.hasNext()) {
				RelatedLinks rl = (RelatedLinks) itr.next();
				CMSContentItem temp = (CMSContentItem) session.get(CMSContentItem.class, new Long(rl.getRelLink().getId()));
				if (temp == null) {
					logger.debug("Item doesn't exist. Creating the CMS item");
					temp = rl.getRelLink();					 
					session.save(temp);
				}
				logger.debug("CMS item = " + temp.getId());
				if (rl.isShowInHomePage()) {
					if (member.getLinks() == null) 
						member.setLinks(new HashSet());
					member.getLinks().add(temp);					
				}
				
				if (edit) {
					if (oldActivity.getDocuments() == null) {
						oldActivity.setDocuments(new HashSet());
					}
					oldActivity.getDocuments().add(temp);
				} else {
					if (activity.getDocuments() == null) {
						activity.setDocuments(new HashSet());
					}
					activity.getDocuments().add(temp);					
				}
			}
			session.saveOrUpdate(member);			
			
			
			/* Persists the activity */
			if (edit) {
				// update the activity
			    logger.debug("updating ....");
			    
			    session.saveOrUpdate(oldActivity);
			    /*
			    // added by Akash
			    // desc: Saving team members in amp_member_activity table in case activity is Approved
			    // start
				if ("approved".equals(oldActivity.getApprovalStatus())) {
					Long teamId = oldActivity.getTeam().getAmpTeamId();
					Query qry = null;
					String queryString = "select tm from " + AmpTeamMember.class.getName()
					 					 + " tm where (tm.ampTeam=:teamId)";
					qry = session.createQuery(queryString);
					qry.setParameter("teamId", teamId, Hibernate.LONG);
					Iterator tmItr = qry.list().iterator();
					member = new AmpTeamMember();
					while (tmItr.hasNext()) {
						member = (AmpTeamMember) tmItr.next();
						if (!member.getAmpMemberRole().getTeamHead().booleanValue()) {
							if (member.getActivities() == null)
								member.setActivities(new HashSet());
						}
						member.getActivities().add(oldActivity);
						session.saveOrUpdate(member);
					}
				}
				// end	*/
				
			} else {
				// create the activity
			    logger.debug("creating ....");
			    if (activity.getMember() == null) {
					activity.setMember(new HashSet());			        
			    }

				activity.getMember().add(activity.getActivityCreator());
				member = (AmpTeamMember) session.load(AmpTeamMember.class,
				        activity.getActivityCreator().getAmpTeamMemId());
				if (member.getActivities() == null) {
				    member.setActivities(new HashSet());
				}
				member.getActivities().add(activity);
				session.save(activity);
				session.saveOrUpdate(member);
			}
			

			
			/* Persists comments, of type AmpComments, related to the activity */
			if (!commentsCol.isEmpty()) {
				logger.debug("commentsCol.size() [Inside Persisting]: " + commentsCol.size());
				boolean flag = true;
				/*
				if (edit && serializeFlag)
					flag = false; */
				logger.debug("flag [Inside Persisting comments]: " + flag);
				itr = commentsCol.iterator();
				while (itr.hasNext()) {
					AmpComments comObj = (AmpComments) itr.next();
					comObj.setAmpActivityId(activity);
					session.save(comObj);
					logger.debug("Comment Saved [AmpCommentId] : " + comObj.getAmpCommentId());
				}
			} else
				logger.debug("commentsCol is empty");
			
			tx.commit(); // commit the transcation
			logger.debug("Activity saved");
		} catch (Exception ex) {
			logger.error("Exception from saveActivity()  " + ex.getMessage()); 
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
					logger.debug("Transaction Rollbacked");
				}
				catch (HibernateException e) {
					logger.error("Rollback failed :" + e);
				}
			}			
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception e) {
					logger.error("Release session faliled :" + e);
				}
			}
		}
	}
	
	public static Collection getComponents(Long actId) {
		 Session session = null;
		 Collection col = new ArrayList();

		 try {
			session = PersistenceManager.getSession();
			String queryString = "select comp from " + AmpComponent.class.getName() + 
			" comp where (comp.activity=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",actId,Hibernate.LONG);
			col = qry.list();
		 } catch (Exception e) {
		 	logger.error("Unable to get all components");
		 	logger.error(e.getMessage());
		 } finally {
		 	try {
		 		PersistenceManager.releaseSession(session);
		 	} catch (Exception  ex) {
		 		logger.error("Release Session failed :" + ex);
		 	}
		 }
		 return col;
	}
	
	public static Collection getActivityCloseDates(Long activityId) {
		 Session session = null;
		 Collection col = new ArrayList();

		 try {
			session = PersistenceManager.getSession();
			String queryString = "select date from " + AmpActivityClosingDates.class.getName() + 
			" date where (date.ampActivityId=:actId) and type in (0,1) order by date.ampActivityClosingDateId";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",activityId,Hibernate.LONG);
			col = qry.list();
		 } catch (Exception e) {
		 	logger.error("Unable to get activity close dates");
		 	logger.error(e.getMessage());
		 } finally {
		 	try {
		 		PersistenceManager.releaseSession(session);
		 	} catch (Exception  ex) {
		 		logger.error("Release Session failed :" + ex);
		 	}
		 }
		 return col;
	}
	
	public static Collection getOrganizationWithRole(Long actId,String roleCode) {
		Session session = null;
		Collection col = new ArrayList();
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select aor from " + AmpOrgRole.class.getName() + " aor " +
					"where (aor.activity=actId)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Collection orgRoles = qry.list();
			Collection temp = new ArrayList();
			
			Iterator orgItr = orgRoles.iterator();
			while (orgItr.hasNext()) {
				AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
				if (orgRole.getRole().getRoleCode().equalsIgnoreCase(roleCode)) {
					if (!temp.contains(orgRole.getOrganisation())) {
						temp.add(orgRole.getOrganisation());							
					}
				}											
			}
			
			orgItr = temp.iterator();
			while (orgItr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) orgItr.next();
				col.add(org.getName());
			}			
			
			AmpActivity act = (AmpActivity) session.load(AmpActivity.class,actId);
				
			if (act.getOrgrole() != null) {

			}
		} catch (Exception e) {
			logger.error("Unable to get Organization with role " + roleCode);
			logger.error(e.getMessage());
		} finally {
		 	try {
		 		PersistenceManager.releaseSession(session);
		 	} catch (Exception  ex) {
		 		logger.error("Release Session failed :" + ex);
		 	}			
		}
		return col;
	}
	
	public static AmpActivity getAmpActivity(Long id) {
	    Session session = null;
	    AmpActivity activity = null;
	    
	    try {
			session = PersistenceManager.getSession();
			
			AmpActivity ampActivity = (AmpActivity) session.load(AmpActivity.class,id);
			
			if (ampActivity != null) {
			    activity = new AmpActivity();
			    activity.setActivityApprovalDate(ampActivity.getActivityApprovalDate());
			    activity.setActivityCloseDate(ampActivity.getActivityCloseDate());
			    activity.setActivityCreator(ampActivity.getActivityCreator());
			    activity.setActivityStartDate(ampActivity.getActivityStartDate());
			    activity.setActualApprovalDate(ampActivity.getActualApprovalDate());
			    activity.setActualCompletionDate(ampActivity.getActualCompletionDate());
			    activity.setActualStartDate(ampActivity.getActualStartDate());
			    activity.setAmpActivityId(ampActivity.getAmpActivityId());
			    activity.setAmpId(ampActivity.getAmpId());
			    activity.setCalType(ampActivity.getCalType());
			    activity.setComments(ampActivity.getComments());
			    activity.setCondition(ampActivity.getCondition());
			    activity.setContactName(ampActivity.getContactName());
			    activity.setContFirstName(ampActivity.getContFirstName());
			    activity.setContLastName(ampActivity.getContLastName());
			    activity.setContractors(ampActivity.getContractors());
			    activity.setCountry(ampActivity.getCountry());
			    activity.setCreatedDate(ampActivity.getCreatedDate());
			    activity.setDescription(ampActivity.getDescription());
			    activity.setEmail(ampActivity.getEmail());
			    activity.setLanguage(ampActivity.getLanguage());
			    activity.setLevel(ampActivity.getLevel());
			    activity.setModality(ampActivity.getModality());
			    activity.setMofedCntEmail(ampActivity.getMofedCntEmail());
			    activity.setMofedCntFirstName(ampActivity.getMofedCntFirstName());
			    activity.setMofedCntLastName(ampActivity.getMofedCntLastName());
			    activity.setName(ampActivity.getName());
			    activity.setObjective(ampActivity.getObjective());
			    activity.setOriginalCompDate(ampActivity.getOriginalCompDate());
			    activity.setProgramDescription(ampActivity.getProgramDescription());
			    activity.setProposedApprovalDate(ampActivity.getProposedApprovalDate());
			    activity.setProposedStartDate(ampActivity.getProposedStartDate());
			    activity.setStatus(ampActivity.getStatus());
			    activity.setStatusReason(ampActivity.getStatusReason());
			    activity.setTeam(ampActivity.getTeam());
			    activity.setThemeId(ampActivity.getThemeId());
			    activity.setUpdatedDate(ampActivity.getUpdatedDate());
			    activity.setVersion(ampActivity.getVersion());
			    
			    activity.setClosingDates(new HashSet(ampActivity.getClosingDates()));
			    activity.setComponents(new HashSet(ampActivity.getComponents()));
			    activity.setDocuments(new HashSet(ampActivity.getDocuments()));
			    activity.setFunding(new HashSet(ampActivity.getFunding()));
			    activity.setRegionalFundings(new HashSet(ampActivity.getRegionalFundings()));
			    activity.setInternalIds(new HashSet(ampActivity.getInternalIds()));
			    activity.setLocations(new HashSet(ampActivity.getLocations()));
			    activity.setSectors(new HashSet(ampActivity.getSectors()));
			    activity.setOrgrole(new HashSet(ampActivity.getOrgrole()));
			    logger.debug("** Issue size = " + ampActivity.getIssues().size() + " **");
			    activity.setIssues(new HashSet(ampActivity.getIssues()));
			}
		} catch (Exception e) {
		 	logger.error("Unable to getAmpActivity");
		 	e.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception  ex) {
		 		logger.error("Release Session failed :" + ex);
		 	}
		}	    
	    return activity;
	}
	
	public static Activity getChannelOverview(Long actId) {
		Session session = null;
		Activity activity = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select act from " + AmpActivity.class.getName() + 
			" act where (act.ampActivityId=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Collection act = qry.list();
			Iterator actItr = act.iterator();
			if (actItr.hasNext()) {
				activity = new Activity();
				AmpActivity ampAct = (AmpActivity) actItr.next();
				activity.setActivityId(ampAct.getAmpActivityId());
				
				activity.setStatus(ampAct.getStatus().getName());
				activity.setStatusReason(ampAct.getStatusReason().trim());
				
				activity.setObjective(ampAct.getObjective());
				
				activity.setCurrCompDate(DateConversion.
						ConvertDateToString(ampAct.getActualCompletionDate()));
				activity.setOrigAppDate(DateConversion.
						ConvertDateToString(ampAct.getProposedApprovalDate()));
				activity.setOrigStartDate(DateConversion.
						ConvertDateToString(ampAct.getProposedStartDate()));
				activity.setRevAppDate(DateConversion.
						ConvertDateToString(ampAct.getActualApprovalDate()));
				activity.setRevStartDate(DateConversion.
						ConvertDateToString(ampAct.getActualStartDate()));

				Collection col = ampAct.getClosingDates();				
				List dates = new ArrayList();
				if (col != null && col.size() > 0) {
					Iterator itr = col.iterator();
					while (itr.hasNext()) {
						AmpActivityClosingDates cDate = (AmpActivityClosingDates) itr
								.next();
						if (cDate.getType().intValue() == Constants.REVISED.intValue()) {
							dates.add(DateConversion.ConvertDateToString(cDate
									.getClosingDate()));
						}
					}
				}
				Collections.sort(dates,DateConversion.dtComp);
				activity.setRevCompDates(dates);
				

				if (ampAct.getLevel() != null) {
					activity.setImpLevel(ampAct.getLevel().getName());	
				}
				
				if (ampAct.getThemeId() != null) {
					activity.setProgram(ampAct.getThemeId().getName());
					activity.setProgramDescription(ampAct.getProgramDescription());
				}
				
				activity.setContractors(ampAct.getContractors());
				
				activity.setContFirstName(ampAct.getContFirstName());
				activity.setContLastName(ampAct.getContLastName());
				activity.setEmail(ampAct.getEmail());
				
				activity.setMfdContFirstName(ampAct.getMofedCntFirstName());
				activity.setMfdContLastName(ampAct.getMofedCntLastName());
				activity.setMfdContEmail(ampAct.getMofedCntEmail());
				
				if (ampAct.getCreatedDate() != null) {
					activity.setCreatedDate(
							DateConversion.ConvertDateToString(ampAct.getCreatedDate()));
				}
				
				
				if (ampAct.getActivityCreator() != null) {
					User usr = ampAct.getActivityCreator().getUser();
					if (usr != null) {
						activity.setActAthFirstName(usr.getFirstNames());
						activity.setActAthLastName(usr.getLastName());
						activity.setActAthEmail(usr.getEmail());	
					}
				}
				
				if (ampAct.getModality() != null) {
					activity.setModality(ampAct.getModality().getName());
					activity.setModalityCode(ampAct.getModality().getModalityCode());				
				}
				
				queryString = "select distinct f.ampTermsAssistId.termsAssistName from " + 
				AmpFunding.class.getName() + " f where f.ampActivityId=:actId"; 
				
				qry = session.createQuery(queryString);
				qry.setParameter("actId",actId,Hibernate.LONG);
				
				Collection temp = new ArrayList();
				Iterator typesItr = qry.list().iterator();
				while (typesItr.hasNext()) {
					String code = (String) typesItr.next();
					temp.add(code);
				}
				activity.setAssistanceType(temp);
				
				Collection relOrgs = new ArrayList();
				if (ampAct.getOrgrole() != null) {
					Iterator orgItr = ampAct.getOrgrole().iterator();
					while (orgItr.hasNext()) {
						AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
						RelOrganization relOrg = new RelOrganization();
						relOrg.setOrgName(orgRole.getOrganisation().getName());
						relOrg.setRole(orgRole.getRole().getRoleCode());
						if (!relOrgs.contains(relOrg)) {
							relOrgs.add(relOrg);							
						}
					}
				}
				activity.setRelOrgs(relOrgs);

				Collection sectors = new ArrayList();		
				if (ampAct.getSectors() != null) {
					Iterator sectItr = ampAct.getSectors().iterator();
					while (sectItr.hasNext()) {
						AmpSector sec = (AmpSector) sectItr.next();
						ActivitySector actSect = new ActivitySector();
						if (sec.getParentSectorId() == null) {
							actSect.setSectorName(sec.getName());
						} else if (sec.getParentSectorId().getParentSectorId() == null) {
							actSect.setSectorName(sec.getParentSectorId().getName());
							actSect.setSubsectorLevel1Name(sec.getName());
						} else {
							actSect.setSectorName(sec.getParentSectorId().getParentSectorId().getName());
							actSect.setSubsectorLevel1Name(sec.getParentSectorId().getName());
							actSect.setSubsectorLevel2Name(sec.getName());
						}
						sectors.add(actSect);
					}
				}
				activity.setSectors(sectors);	
			
				if (ampAct.getLevel() != null) {
					activity.setImpLevel(ampAct.getLevel().getName());	
				}
				
				Collection locColl = new ArrayList();
				if (ampAct.getLocations() != null) {
					Iterator locItr = ampAct.getLocations().iterator();
					while (locItr.hasNext()) {
						AmpLocation ampLoc = (AmpLocation) locItr.next();
						Location loc = new Location();
						if (ampLoc.getAmpRegion() != null) {
							loc.setRegion(ampLoc.getAmpRegion().getName());
						} 
						if (ampLoc.getAmpZone() != null) {
							loc.setZone(ampLoc.getAmpZone().getName());
						} 
						if (ampLoc.getAmpWoreda() != null) {
							loc.setWoreda(ampLoc.getAmpWoreda().getName());
						}
						locColl.add(loc);
					}
				}
				activity.setLocations(locColl);				
				
				activity.setProjectIds(ampAct.getInternalIds());
				
				Collection modalities = new ArrayList();
				queryString = "select fund from " + AmpFunding.class.getName() + " fund " +
						"where (fund.ampActivityId=:actId)";
				qry = session.createQuery(queryString);
				qry.setParameter("actId",actId,Hibernate.LONG);	
				Iterator itr = qry.list().iterator();
				while (itr.hasNext()) {
					AmpFunding fund = (AmpFunding) itr.next();
					modalities.add(fund.getModalityId());
				}
				activity.setModalities(modalities);
			}
		} catch (Exception e) {
		 	logger.error("Unable to get channnel overview");
		 	e.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception  ex) {
		 		logger.error("Release Session failed :" + ex);
		 	}
		}
		return activity;
	}
	
	public static Collection getActivitySectors(Long actId) {
		Session session = null;
		Collection sectors = new ArrayList();
		
		try {
			session = PersistenceManager.getSession() ;
			String queryString = "select a from " + AmpActivity.class.getName() +
 			 " a " +  "where (a.ampActivityId=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				AmpActivity act = (AmpActivity) itr.next();
				Set set = act.getSectors();
				if (set != null) {
					Iterator sectItr = set.iterator();
					while (sectItr.hasNext()) {
						AmpSector sec = (AmpSector) sectItr.next();
						sectors.add(sec);
					}
				}
			}
		} catch(Exception ex) {
			logger.error("Unable to get activity sectors :" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return sectors;
	}
	
	public static Collection getOrgRole(Long id) {
	    Session session = null;
	    Collection orgroles = new ArrayList();
		try {
			session = PersistenceManager.getSession() ;
			String queryString = "select aor from " + AmpOrgRole.class.getName() +
 			 " aor " +  "where (aor.activity=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",id,Hibernate.LONG);
			orgroles = qry.list();
		} catch(Exception ex) {
			logger.error("Unable to get activity sectors :" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return orgroles;
	}
	
	public static Collection getAllComponents(Long id) {
	    Collection col = new ArrayList();
	    
	    Session session = null;
	    
	    try {
	        session = PersistenceManager.getSession();
	        AmpActivity activity = (AmpActivity) session.load(AmpActivity.class,id);
	        Set comp = activity.getComponents();
	        if (comp != null && comp.size() > 0) {
	            Iterator itr1 = comp.iterator();
	            while (itr1.hasNext()) {
	                AmpComponent ampComp = (AmpComponent) itr1.next();
	                Components components = new Components();
	                components.setComponentId(ampComp.getAmpComponentId());
	                components.setDescription(ampComp.getDescription());
	                components.setTitle(ampComp.getTitle());
	                components.setCommitments(new ArrayList());
	                components.setDisbursements(new ArrayList());
	                components.setExpenditures(new ArrayList());
	                components.setPhyProgress(new ArrayList());
	                Iterator itr2 = ampComp.getComponentFundings().iterator();
	                while (itr2.hasNext()) {
	                    AmpComponentFunding cf = (AmpComponentFunding) itr2.next();
	                    FundingDetail fd = new FundingDetail();
	                    fd.setAdjustmentType(cf.getAdjustmentType().intValue());
	                    if (fd.getAdjustmentType() == Constants.PLANNED) {
	                        fd.setAdjustmentTypeName("Planned");
	                    } else {
	                        fd.setAdjustmentTypeName("Actual");
	                    }
	                    fd.setCurrencyCode(cf.getCurrency().getCurrencyCode());
	                    fd.setCurrencyName(cf.getCurrency().getCurrencyName());
	                    fd.setPerspectiveCode(cf.getPerspective().getCode());
	                    fd.setPerspectiveName(cf.getPerspective().getName());
	                    fd.setTransactionAmount(
	                            DecimalToText.ConvertDecimalToText(
	                                    cf.getTransactionAmount().doubleValue()));
	                    fd.setTransactionDate(
	                            DateConversion.ConvertDateToString(
	                                    cf.getTransactionDate()));
	                    fd.setTransactionType(cf.getTransactionType().intValue());
	                    if (fd.getTransactionType() == Constants.COMMITMENT) {
	                        components.getCommitments().add(fd);
	                    } else if (fd.getTransactionType() == Constants.DISBURSEMENT) {
	                        components.getDisbursements().add(fd);
	                    } else if (fd.getTransactionType() == Constants.EXPENDITURE) {
	                        components.getExpenditures().add(fd);
	                    }
	                }
	                itr2 = ampComp.getPhysicalProgress().iterator();
	                while (itr2.hasNext()) {
	                    AmpPhysicalPerformance ampPhyPerf = (AmpPhysicalPerformance) itr2.next();
	                    PhysicalProgress pp = new PhysicalProgress();
	                    pp.setDescription(ampPhyPerf.getDescription());
	                    pp.setPid(ampPhyPerf.getAmpPpId());
	                    pp.setReportingDate(
	                            DateConversion.ConvertDateToString(
	                                    ampPhyPerf.getReportingDate()));
	                    pp.setTitle(ampPhyPerf.getTitle());
	                    components.getPhyProgress().add(pp);
	                }
	    			List list = null;
	    			if (components.getCommitments() != null) {
	    				list = new ArrayList(components.getCommitments());
	    				Collections.sort(list,FundingValidator.dateComp);
	    			}
	    			components.setCommitments(list);
	    			list = null;
	    			if (components.getDisbursements() != null) {
	    				list = new ArrayList(components.getDisbursements());
	    				Collections.sort(list,FundingValidator.dateComp);
	    			}
	    			components.setDisbursements(list);
	    			list = null;
	    			if (components.getExpenditures() != null) {
	    				list = new ArrayList(components.getExpenditures());
	    				Collections.sort(list,FundingValidator.dateComp);
	    			}
	    			components.setExpenditures(list);	                
	                col.add(components);
	            }
	        }
	        
	    } catch (Exception e) {
			logger.debug("Exception in getAmpComponents() " + e.getMessage());
			e.printStackTrace(System.out);	        
	    } finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}	        
	    }
	    return col;
	}
	 
	public static ArrayList getIssues(Long id) {
		ArrayList list = new ArrayList();
		
		Session  session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivity activity = (AmpActivity) session.load(AmpActivity.class,id);
			Set issues = activity.getIssues();
			if (issues != null && issues.size() > 0) {
				Iterator iItr = issues.iterator();
				while (iItr.hasNext()) {
					AmpIssues ampIssue = (AmpIssues) iItr.next();
					Issues issue = new Issues();
					issue.setId(ampIssue.getAmpIssueId());
					issue.setName(ampIssue.getName());
					ArrayList mList = new ArrayList();
					if (ampIssue.getMeasures() != null &&
							ampIssue.getMeasures().size() > 0) {
						Iterator mItr = ampIssue.getMeasures().iterator() ;
						while (mItr.hasNext()) {
							AmpMeasure ampMeasure = (AmpMeasure) mItr.next();
							Measures measure = new Measures();
							measure.setId(ampMeasure.getAmpMeasureId());
							measure.setName(ampMeasure.getName());
							ArrayList aList = new ArrayList();
							if (ampMeasure.getActors() != null &&
									ampMeasure.getActors().size() > 0) {
								Iterator aItr = ampMeasure.getActors().iterator();
								while (aItr.hasNext()) {
									AmpActor actor = (AmpActor) aItr.next();	
									aList.add(actor);
								}
							}
							measure.setActors(aList);
							mList.add(measure);
						}
					}
					issue.setMeasures(mList);
					list.add(issue);
				}
			}
		} catch (Exception e) {
			logger.debug("Exception in getIssues() " + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
		}
		return list;
	}
	
	public static Collection getRegionalFundings(Long id) {
		Collection col = new ArrayList();

		Session  session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivity activity = (AmpActivity) session.load(AmpActivity.class,id);
			col = activity.getRegionalFundings();
		} catch (Exception e) {
			logger.debug("Exception in getRegionalFundings() " + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
		}		
		return col;
	}
	
	public static Collection getRegionalFundings(Long id,Long regId) {
		Collection col = new ArrayList();

		Session  session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivity activity = (AmpActivity) session.load(AmpActivity.class,id);
			col = activity.getRegionalFundings();
			ArrayList temp = new ArrayList(col);
			Iterator itr = temp.iterator();
			AmpRegionalFunding regionFunding = new AmpRegionalFunding();
			regionFunding.setAmpRegionalFundingId(regId);
			while (itr.hasNext()) {
				AmpRegionalFunding regFund = (AmpRegionalFunding) itr.next();
				if (regionFunding.equals(regFund)) {
					col.remove(regFund);
				}
 			}
		} catch (Exception e) {
			logger.debug("Exception in getRegionalFundings() " + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
		}		
		return col;
	}	
	
	public static AmpActivity getActivityByName(String name) {
		AmpActivity activity = null;
		Session  session = null;
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select a from " + AmpActivity.class.getName() + " a " +
					"where lower(a.name) = '" + name.toLowerCase() + "'";
			Query qry = session.createQuery(qryStr);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				activity = (AmpActivity) itr.next();
			}
		} catch (Exception e) {
			logger.debug("Exception in isActivityExisting() " + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
		}				
		return activity;
	}
	
	public static void saveDonorFundingInfo(Long actId,Set fundings) {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			
			//logger.info("Before iterating");
			Iterator itr = fundings.iterator();
			while (itr.hasNext()) {
				AmpFunding temp = (AmpFunding) itr.next();
				AmpFunding fund = (AmpFunding) session.load(AmpFunding.class,temp.getAmpFundingId());
				Iterator fItr = fund.getFundingDetails().iterator();
				while (fItr.hasNext()) {
					AmpFundingDetail fd = (AmpFundingDetail) fItr.next();
					session.delete(fd);
				}
				fund.getFundingDetails().clear();
				fund.setFundingDetails(temp.getFundingDetails());
				//logger.info("Updating " + fund.getAmpFundingId());
				session.update(fund);
				//logger.info("Updated...");
			}
			tx.commit();
			//logger.info("Donor info. saved");
		} catch (Exception e) {
			logger.error("Exception from saveDonorFundingInfo()");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();					
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try{
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		
	}
	
	public static boolean canViewActivity(Long actId,TeamMember tm) {
		boolean canView = false;
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			if (tm.getTeamHead()) {
				if (tm.getTeamType().equalsIgnoreCase("DONOR")) {
					// DONOR team leader
					AmpTeam team = (AmpTeam) session.load(AmpTeam.class,tm.getTeamId());
					AmpActivity act = new AmpActivity();
					act.setAmpActivityId(actId);
					if (team.getActivityList().contains(act)) canView = true;
				} else {
					// MOFED team leader
					//logger.info("Mofed team leader");
					//logger.info("loading activity " + actId); 
					AmpActivity act = (AmpActivity) session.load(AmpActivity.class,actId);
					if (act.getTeam().getAmpTeamId().equals(tm.getTeamId())) {
						logger.debug("Can view " + actId + " , team " + tm.getTeamId());
						canView = true;
					} else {
						
					}
				}
			} else {
				AmpTeamMember ampTeamMem = (AmpTeamMember) session.load(AmpTeamMember.class,
						tm.getMemberId());
				AmpActivity act = new AmpActivity();
				act.setAmpActivityId(actId);
				if (ampTeamMem.getActivities().contains(act)) canView = true;
			}
		} catch (Exception e) {
			logger.error("Exception from canViewActivity()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try{
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		//logger.info("Canview =" + canView);
		return canView;
	}

	public static Collection getDonors(Long actId) {
		Collection col = new ArrayList();
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivity act = (AmpActivity) session.load(AmpActivity.class,actId);
			if (act.getFunding() != null) {
				Iterator itr = act.getFunding().iterator();
				while (itr.hasNext()) {
					AmpFunding fund = (AmpFunding) itr.next();
					AmpProjectDonor ampProjectDonor = new AmpProjectDonor();
					ampProjectDonor.setDonorName(fund.getAmpDonorOrgId().getName());
					ampProjectDonor.setAmpDonorId(fund.getAmpDonorOrgId().getAmpOrgId());
					col.add(ampProjectDonor); 
				}
			}
				
		} catch (Exception e) {
			logger.error("Exception from getDonors()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try{
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		return col;		
	}

} // End
