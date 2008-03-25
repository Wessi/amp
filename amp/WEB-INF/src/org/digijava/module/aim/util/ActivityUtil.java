/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005
 */

package org.digijava.module.aim.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpClosingDateHistory;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpNotes;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalComponentReport;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpReportCache;
import org.digijava.module.aim.dbentity.AmpReportLocation;
import org.digijava.module.aim.dbentity.AmpReportPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpReportSector;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AmpProjectDonor;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.FormatHelper;
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
  public static Long saveActivity(AmpActivity activity, ArrayList commentsCol,
                                  boolean serializeFlag, Long field,
                                  Collection relatedLinks, Long memberId,
                                  Set<Components<AmpComponentFunding>> ampTempComp,List<IPAContract> contracts) {
    /*
     * calls saveActivity(AmpActivity activity,Long oldActivityId,boolean edit)
     * by passing null and false to the parameters oldActivityId and edit respectively
     * since this is creating a new activity
     */
    return saveActivity(activity, null, false, commentsCol, serializeFlag,
                        field, relatedLinks, memberId, null, ampTempComp, contracts);
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
public static Long saveActivity(AmpActivity activity, Long oldActivityId,
                                  boolean edit,
                                  ArrayList commentsCol, boolean serializeFlag,
                                  Long field,
                                  Collection relatedLinks, Long memberId,
                                  Collection indicators, Set<Components<AmpComponentFunding>> componentsFunding, List<IPAContract> contracts) {
    logger.debug("In save activity " + activity.getName());
    Session session = null;
    Transaction tx = null;
    AmpActivity oldActivity = null;

    Long activityId = null;
    Set fundSet		= null;

    try {
      session = PersistenceManager.getSession();
      tx = session.beginTransaction();

      AmpTeamMember member = (AmpTeamMember) session.load(AmpTeamMember.class,
          memberId);

      if (edit) { /* edit an existing activity */
        oldActivity = (AmpActivity) session.load(AmpActivity.class,
                                                 oldActivityId);

        activityId = oldActivityId;

        activity.setAmpActivityId(oldActivityId);

        if (oldActivity == null) {
          logger.debug("Previous Activity is null");
        }

        // delete previos fundings and funding details
        /*Set fundSet = oldActivity.getFunding();
        /*if (fundSet != null) {
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
        }*/

        // delete previous regional fundings
        oldActivity.getRegionalFundings().clear();
        /*fundSet = oldActivity.getRegionalFundings();
        if (fundSet != null) {
          Iterator fundSetItr = fundSet.iterator();
          while (fundSetItr.hasNext()) {
            AmpRegionalFunding regFund = (AmpRegionalFunding) fundSetItr.next();
            session.delete(regFund);
          }
        }*/

        // delete all previous components
        /*Set comp = oldActivity.getComponents();
             if (comp != null) {
         Iterator compItr = comp.iterator();
         while (compItr.hasNext()) {
          AmpComponent ampComp = (AmpComponent) compItr.next();
          session.delete(ampComp);
         }
             }*/

        // delete all previous org roles
        oldActivity.getOrgrole().clear();
       /* Set orgrole = oldActivity.getOrgrole();
        if (orgrole != null) {
          Iterator orgroleItr = orgrole.iterator();
          while (orgroleItr.hasNext()) {
            AmpOrgRole ampOrgrole = (AmpOrgRole) orgroleItr.next();
            session.delete(ampOrgrole);
          }
        }*/

        // delete all previous closing dates
        oldActivity.getClosingDates().clear();
        /*Set closeDates = oldActivity.getClosingDates();
        if (closeDates != null) {
          Iterator dtItr = closeDates.iterator();
          while (dtItr.hasNext()) {
            AmpActivityClosingDates date = (AmpActivityClosingDates) dtItr.next();
            session.delete(date);
          }
        }*/

        // delete all previous issues
        oldActivity.getIssues().clear();
/*        Set issues = oldActivity.getIssues();
        if (issues != null) {
          Iterator iItr = issues.iterator();
          while (iItr.hasNext()) {
            AmpIssues issue = (AmpIssues) iItr.next();
            session.delete(issue);
          }
        }*/

        // delete all previous Reference Docs
        oldActivity.getReferenceDocs().clear();
        /*if (oldActivity.getReferenceDocs() != null) {
          Iterator refItr = oldActivity.getReferenceDocs().iterator();
          while (refItr.hasNext()) {
            AmpActivityReferenceDoc refDoc = (AmpActivityReferenceDoc) refItr.next();
            session.delete(refDoc);
          }
        }*/

        oldActivity.getComponentes().clear();
        
        // delete all previous sectors
        oldActivity.getSectors().clear();
       /* if (oldActivity.getSectors() != null) {
          Iterator iItr = oldActivity.getSectors().iterator();
          while (iItr.hasNext()) {
            AmpActivitySector sec = (AmpActivitySector) iItr.next();
            session.delete(sec);
          }
        }*/
        
//				 delete all previous costs
        oldActivity.getCosts().clear();
        /*if (oldActivity.getCosts() != null) {
          Iterator iItr = oldActivity.getCosts().iterator();
          while (iItr.hasNext()) {
            EUActivity eu = (EUActivity) iItr.next();
            session.delete(eu);
          }
        }
*/
        // delete all previous comments
        
          ArrayList col = org.digijava.module.aim.util.DbUtil.
              getAllCommentsByActivityId( oldActivity.getAmpActivityId() );
          if (col != null) {
            Iterator itr = col.iterator();
            while (itr.hasNext()) {
              AmpComments comObj = (AmpComments) itr.next();
              session.delete(comObj);
            }
          }
        
        

        if ( oldActivity.getCategories() != null ) {
        	oldActivity.getCategories().clear();
        }
        else
        	oldActivity.setCategories( new HashSet() );

        if ( oldActivity.getFunding() != null ) {
        	oldActivity.getFunding().clear();
        }
        else
        	oldActivity.setFunding( new HashSet() );

        oldActivity.getClosingDates().clear();
        oldActivity.getComponents().clear();
        oldActivity.getDocuments().clear();
        oldActivity.getInternalIds().clear();
        oldActivity.getLocations().clear();
       // oldActivity.getOrgrole().clear();
       // oldActivity.getReferenceDocs().clear();
       // oldActivity.getSectors().clear();
       // oldActivity.getCosts().clear();
        oldActivity.getActivityDocuments().clear();
        oldActivity.getActivityPrograms().clear();

        oldActivity.setLineMinRank(activity.getLineMinRank());
        oldActivity.setPlanMinRank(activity.getPlanMinRank());
        oldActivity.setActivityCreator(activity.getActivityCreator());
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
        oldActivity.setDocumentSpace(activity.getDocumentSpace());
        oldActivity.setEmail(activity.getEmail());
        oldActivity.setLanguage(activity.getLanguage());

        oldActivity.setDnrCntTitle(activity.getDnrCntTitle());
        oldActivity.setDnrCntOrganization(activity.getDnrCntOrganization());
        oldActivity.setDnrCntPhoneNumber(activity.getDnrCntPhoneNumber());
        oldActivity.setDnrCntFaxNumber(activity.getDnrCntFaxNumber());

        oldActivity.setMfdCntTitle(activity.getMfdCntTitle());
        oldActivity.setMfdCntOrganization(activity.getMfdCntOrganization());
        oldActivity.setMfdCntPhoneNumber(activity.getMfdCntPhoneNumber());
        oldActivity.setMfdCntFaxNumber(activity.getMfdCntFaxNumber());

//				oldActivity.setLevel(activity.getLevel()); //TO BE DELETED
        oldActivity.setModality(activity.getModality());
        oldActivity.setMofedCntEmail(activity.getMofedCntEmail());
        oldActivity.setMofedCntFirstName(activity.getMofedCntFirstName());
        oldActivity.setMofedCntLastName(activity.getMofedCntLastName());
        oldActivity.setName(activity.getName());
        oldActivity.setBudget(activity.getBudget());
        oldActivity.setObjective(activity.getObjective());
        oldActivity.setResults(activity.getResults());
        oldActivity.setPurpose(activity.getPurpose());
        oldActivity.setProgramDescription(activity.getProgramDescription());
        oldActivity.setProposedApprovalDate(activity.getProposedApprovalDate());
        oldActivity.setProposedStartDate(activity.getProposedStartDate());
        oldActivity.setProposedCompletionDate(activity.
                                              getProposedCompletionDate());
        oldActivity.setContractingDate(activity.getContractingDate());
        oldActivity.setDisbursmentsDate(activity.getDisbursmentsDate());

        oldActivity.setStatusReason(activity.getStatusReason());
        oldActivity.setThemeId(activity.getThemeId());
        oldActivity.setUpdatedDate(activity.getUpdatedDate());
        if (activity.getClosingDates() != null)
        	oldActivity.getClosingDates().addAll(activity.getClosingDates());
        if (activity.getComponents() != null)
        	oldActivity.getComponents().addAll(activity.getComponents());
        //oldActivity.setDocuments(activity.getDocuments());
        if (activity.getFunding() != null)
        	oldActivity.getFunding().addAll(activity.getFunding());
        if (activity.getRegionalFundings() != null)
        	oldActivity.getRegionalFundings().addAll(activity.getRegionalFundings());
        if (activity.getInternalIds() != null)
        	oldActivity.getInternalIds().addAll(activity.getInternalIds());
        if (activity.getLocations() != null)
        	oldActivity.getLocations().addAll(activity.getLocations());
       
        if (activity.getOrgrole() != null)
        	oldActivity.getOrgrole().addAll(activity.getOrgrole());
        if (activity.getReferenceDocs() != null)
        	oldActivity.getReferenceDocs().addAll(activity.getReferenceDocs());
        if (activity.getSectors() != null)
        	oldActivity.getSectors().addAll(activity.getSectors()); 
        if (activity.getComponentes() != null)
        	oldActivity.getComponentes().addAll(activity.getComponentes());
        if (activity.getIssues() != null)
        	oldActivity.getIssues().addAll(activity.getIssues());
        if (activity.getCosts() != null)
        	oldActivity.getCosts().addAll(activity.getCosts());
        if (activity.getActivityPrograms() != null)
        	oldActivity.getActivityPrograms().addAll(activity.getActivityPrograms());

        if (activity.getCategories() != null)
        	oldActivity.getCategories().addAll( activity.getCategories() );

        if(activity.getActivityDocuments() !=null)
        	oldActivity.getActivityDocuments().addAll( activity.getActivityDocuments() );

        oldActivity.setFunAmount(activity.getFunAmount());
        oldActivity.setCurrencyCode(activity.getCurrencyCode());
        oldActivity.setFunDate(activity.getFunDate());

        oldActivity.setApprovalStatus(activity.getApprovalStatus());

        Set programs = activity.getActPrograms();
        Set oldPrograms = oldActivity.getActPrograms();
        Set deletedPrograms = new HashSet();
        Set newPrograms = new HashSet();
        if (oldPrograms != null && oldPrograms.size() > 0) {
                Iterator iterOldProgram = oldPrograms.iterator();
                while (iterOldProgram.hasNext()) {
                        AmpActivityProgram oldProgram = (AmpActivityProgram)
                            iterOldProgram.next();
                        boolean delete = true;
                        if (programs != null) {
                                Iterator iterProgram = programs.iterator();
                                while (iterProgram.hasNext()) {
                                        AmpActivityProgram program = (
                                            AmpActivityProgram)
                                            iterProgram.next();
                                        if (program.getAmpActivityProgramId() == null) {
                                                program.setActivity(oldActivity);
                                                newPrograms.add(program);
                                        }
                                        else {
                                                if (oldProgram.
                                                    getAmpActivityProgramId().
                                                    equals(
                                                    program.
                                                    getAmpActivityProgramId())) {
                                                        oldProgram.
                                                            setProgramPercentage(
                                                            program.
                                                            getProgramPercentage());
                                                        delete = false;
                                                        break;

                                                }

                                        }

                                }
                                if (delete) {
                                        deletedPrograms.add(oldProgram);
                                }

                        }
                }
                oldActivity.getActPrograms().removeAll(deletedPrograms);
                oldActivity.getActPrograms().addAll(newPrograms);
                if (deletedPrograms.size() > 0) {
                        Iterator delProgramIter = deletedPrograms.
                            iterator();
                        while (delProgramIter.hasNext()) {
                                AmpActivityProgram delProgram = (
                                    AmpActivityProgram) delProgramIter.
                                    next();
                                //  delProgram.setActivity(null);
                                session.delete(delProgram);
                        }
                }
                oldActivity.setActPrograms(oldPrograms);

        }
        else {
                if (programs != null) {
                        Iterator iterProgram = programs.iterator();
                        while (iterProgram.hasNext()) {
                                AmpActivityProgram program = (
                                    AmpActivityProgram) iterProgram.next();
                                program.setActivity(oldActivity);
                        }

                }
                oldActivity.setActPrograms(programs);
        }



        /*
         * tanzania ADDS
         */
        oldActivity.setFY(activity.getFY());
        oldActivity.setGovernmentApprovalProcedures(activity.
            isGovernmentApprovalProcedures());
        oldActivity.setJointCriteria(activity.isJointCriteria());
        oldActivity.setGbsSbs(activity.getGbsSbs());
        oldActivity.setProjectCode(activity.getProjectCode());
        oldActivity.setSubProgram(activity.getSubProgram());
        oldActivity.setSubVote(activity.getSubVote());
        oldActivity.setVote(activity.getVote());
        oldActivity.setActivityCreator(activity.getActivityCreator());
        oldActivity.setDraft(activity.getDraft());
        oldActivity.setGovAgreementNumber(activity.getGovAgreementNumber());

      }

      Iterator itr = null;
      if (relatedLinks != null && relatedLinks.size() > 0) {
        itr = relatedLinks.iterator();
        member = (AmpTeamMember) session.load(AmpTeamMember.class, memberId);
        while (itr.hasNext()) {
          RelatedLinks rl = (RelatedLinks) itr.next();
          CMSContentItem temp = (CMSContentItem) session.get(CMSContentItem.class,
              new Long(rl.getRelLink().getId()));
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
          }
          else {
            if (activity.getDocuments() == null) {
              activity.setDocuments(new HashSet());
            }
            activity.getDocuments().add(temp);
          }
        }
        session.saveOrUpdate(member);
      }


      /* Persists the activity */
      if (edit) {
        // update the activity
        logger.debug("updating ....");
        oldActivity.setUpdatedDate(new Date(System.currentTimeMillis()));
        oldActivity.setUpdatedBy(member);
        session.saveOrUpdate(oldActivity);
        activityId = oldActivity.getAmpActivityId();
        String ampId=generateAmpId(member.getUser(),activityId );
        oldActivity.setAmpId(ampId);
        session.update(oldActivity);
        activity = oldActivity;
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

      }
      else {
        // create the activity
        logger.debug("creating ....");
        if (activity.getMember() == null) {
                activity.setMember(new HashSet());
                Set programs = activity.getActPrograms();
                if (programs != null) {
                        Iterator iterProgram = programs.iterator();
                        while (iterProgram.hasNext()) {
                                AmpActivityProgram program = (
                                    AmpActivityProgram) iterProgram.next();
                                program.setActivity(activity);
                        }

                }

        }

        activity.getMember().add(activity.getActivityCreator());
        /*
             member = (AmpTeamMember) session.load(AmpTeamMember.class,
                activity.getActivityCreator().getAmpTeamMemId());
             if (member.getActivities() == null) {
            member.setActivities(new HashSet());
             }
             member.getActivities().add(activity);
         */
        session.save(activity);
        activityId = activity.getAmpActivityId();
        String ampId=generateAmpId(member.getUser(),activityId );
        activity.setAmpId(ampId);
        session.update(activity);
        //session.saveOrUpdate(member);
      }

      Collection<AmpComponentFunding>  componentFundingCol = getFundingComponentActivity(activityId);
      Iterator<Components<AmpComponentFunding>> componentsFundingIt = componentsFunding.iterator();
      while(componentsFundingIt.hasNext()){
    	  Components<AmpComponentFunding> ampTempComp = componentsFundingIt.next();

	      //to save the Component fundings
	      if (ampTempComp.getCommitments() != null) {
	        Iterator compItr = ampTempComp.getCommitments().iterator();
	        while (compItr.hasNext()) {
	          AmpComponentFunding ampComp = (AmpComponentFunding) compItr.next();
	          session.saveOrUpdate(ampComp);
	          if (componentFundingCol != null) {
	        	  componentFundingCol.remove(ampComp);
	          }
	        }
	      }

	      if (ampTempComp.getDisbursements() != null) {
	        Iterator compItr = ampTempComp.getDisbursements().iterator();
	        while (compItr.hasNext()) {
	          AmpComponentFunding ampComp = (AmpComponentFunding) compItr.next();
	          session.saveOrUpdate(ampComp);
	          if (componentFundingCol != null) {
	        	  componentFundingCol.remove(ampComp);
	          }
	        }
	      }

	      if (ampTempComp.getExpenditures() != null) {
	        Iterator compItr = ampTempComp.getExpenditures().iterator();
	        while (compItr.hasNext()) {
	          AmpComponentFunding ampComp = (AmpComponentFunding) compItr.next();
	          session.saveOrUpdate(ampComp);
	          if (componentFundingCol != null) {
	        	  componentFundingCol.remove(ampComp);
			  }
	        }
	      }


	      Collection<AmpPhysicalPerformance> phyProgress = DbUtil.getAmpPhysicalProgress(activityId,ampTempComp.getComponentId());

	      if (ampTempComp.getPhyProgress() != null) {
				Iterator compItr = ampTempComp.getPhyProgress().iterator();
				while (compItr.hasNext()) {
					AmpPhysicalPerformance ampPhyPerf = (AmpPhysicalPerformance) compItr.next();
					session.saveOrUpdate(ampPhyPerf);
					phyProgress.remove(ampPhyPerf);
				}
	      }

	      if (phyProgress != null&&ampTempComp.getComponentId()!=null) {
				Iterator<AmpPhysicalPerformance> phyProgressColIt = phyProgress.iterator();
				while (phyProgressColIt.hasNext()) {
					session.delete(phyProgressColIt.next());
				}
		  }

      }
      if (componentFundingCol != null) {
			Iterator<AmpComponentFunding> componentFundingColIt = componentFundingCol.iterator();
			while (componentFundingColIt.hasNext()) {
				session.delete(componentFundingColIt.next());
			}
	  }

      /* Persists comments, of type AmpComments, related to the activity */
      if (commentsCol != null && !commentsCol.isEmpty()) {
        logger.debug("commentsCol.size() [Inside Persisting]: " +
                     commentsCol.size());
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
          logger.debug("Comment Saved [AmpCommentId] : " +
                       comObj.getAmpCommentId());
        }
      }
      else
        logger.debug("commentsCol is empty");

      if (indicators != null && indicators.size() > 0) {
        itr = indicators.iterator();
        while (itr.hasNext()) {
          ActivityIndicator actInd = (ActivityIndicator) itr.next();
          
          
          
          AmpIndicator ind=(AmpIndicator)session.get(AmpIndicator.class,actInd.getIndicatorId());
          AmpIndicatorRiskRatings risk=(AmpIndicatorRiskRatings)session.load(AmpIndicatorRiskRatings.class, actInd.getRisk());

          //try to find connection of current activity with current indicator
          IndicatorActivity indConn=IndicatorUtil.findActivityIndicatorConnection(activity, ind);
          //if no connection found then create new one. Else clear old values for the connection.
          if (indConn==null){
        	  indConn=new IndicatorActivity();
              indConn.setActivity(activity);
              indConn.setIndicator(ind);
              indConn.setValues(new HashSet<AmpIndicatorValue>());
          }else{
        	  if (indConn.getValues()!=null && indConn.getValues().size()>0){
        		  for (AmpIndicatorValue value : indConn.getValues()) {
					session.delete(value);
				}
        		  indConn.getValues().clear();
        	  }
          }

          //create each type of value and assign to connection
          if (actInd.getActualVal()!=null){
        	  AmpIndicatorValue indValActual=new AmpIndicatorValue();
        	  indValActual.setValueType(AmpIndicatorValue.ACTUAL);
        	  indValActual.setValue(new Double(actInd.getActualVal()));
        	  indValActual.setComment(actInd.getCurrentValComments());
        	  indValActual.setValueDate(DateConversion.getDate(actInd.getCurrentValDate()));
        	  indValActual.setRisk(risk);
        	  indValActual.setIndicatorConnection(indConn);
        	  indConn.getValues().add(indValActual);
          }
          if (actInd.getTargetVal()!=null){
        	  AmpIndicatorValue indValTarget=new AmpIndicatorValue();
        	  indValTarget.setValueType(AmpIndicatorValue.TARGET);
        	  indValTarget.setValue(new Double(actInd.getTargetVal()));
        	  indValTarget.setComment(actInd.getTargetValComments());
        	  indValTarget.setValueDate(DateConversion.getDate(actInd.getTargetValDate()));
        	  indValTarget.setRisk(risk);
        	  indValTarget.setIndicatorConnection(indConn);
        	  indConn.getValues().add(indValTarget);
          }
          if (actInd.getBaseVal()!=null){
        	  AmpIndicatorValue indValBase=new AmpIndicatorValue();
        	  indValBase.setValueType(AmpIndicatorValue.BASE);
        	  indValBase.setValue(new Double(actInd.getBaseVal()));
        	  indValBase.setComment(actInd.getBaseValComments());
        	  indValBase.setValueDate(DateConversion.getDate(actInd.getBaseValDate()));
        	  indValBase.setRisk(risk);
        	  indValBase.setIndicatorConnection(indConn);
        	  indConn.getValues().add(indValBase);
          }
          if (actInd.getRevisedTargetVal()!=null){
        	  AmpIndicatorValue indValRevised=new AmpIndicatorValue();
        	  indValRevised.setValueType(AmpIndicatorValue.REVISED);
        	  indValRevised.setValue(new Double(actInd.getRevisedTargetVal()));
        	  indValRevised.setComment(actInd.getRevisedTargetValComments());
        	  indValRevised.setValueDate(DateConversion.getDate(actInd.getRevisedTargetValDate()));
        	  indValRevised.setRisk(risk);
        	  indValRevised.setIndicatorConnection(indConn);
        	  indConn.getValues().add(indValRevised);
          }
          // save connection with its new values.
          IndicatorUtil.saveConnectionToActivity(indConn);
        }
      }
        String queryString = "select con from " + IPAContract.class.getName() + " con where con.activity=" + activityId;
        String ids = "";
        if (contracts != null) {

            Iterator<IPAContract> ipaConIter = contracts.iterator();
            while (ipaConIter.hasNext()) {
                IPAContract contract = ipaConIter.next();
                contract.setActivity(activity);
                if (contract.getId() != null) {
                    IPAContract oldContract = (IPAContract) session.get(IPAContract.class, contract.getId());
                    oldContract.setContractName(contract.getContractName());
                    oldContract.setDescription(contract.getDescription());
                    oldContract.setContractingOrganizationText(contract.getContractingOrganizationText());
                    oldContract.setActivityCategory(contract.getActivityCategory());
                    oldContract.setStartOfTendering(contract.getStartOfTendering());
                    oldContract.setSignatureOfContract(contract.getSignatureOfContract());
                    oldContract.setContractValidity(contract.getContractValidity());
                    oldContract.setContractCompletion(contract.getContractCompletion());
                    oldContract.setTotalECContribIBAmount(contract.getTotalECContribIBAmount());
                    oldContract.setTotalECContribIBCurrency(contract.getTotalECContribIBCurrency());
                    oldContract.setTotalAmount(contract.getTotalAmount());
                    oldContract.setTotalAmountCurrency(contract.getTotalAmountCurrency());
                    oldContract.setDibusrsementsGlobalCurrency(contract.getDibusrsementsGlobalCurrency());
                    oldContract.setExecutionRate(contract.getExecutionRate());
                    oldContract.setTotalECContribINVAmount(contract.getTotalECContribINVAmount());
                    oldContract.setTotalECContribINVCurrency(contract.getTotalECContribINVCurrency());
                    oldContract.setTotalNationalContribCentralAmount(contract.getTotalNationalContribCentralAmount());
                    oldContract.setTotalNationalContribCentralCurrency(contract.getTotalNationalContribCentralCurrency());
                    oldContract.setTotalNationalContribRegionalAmount(contract.getTotalNationalContribRegionalAmount());
                    oldContract.setTotalNationalContribRegionalCurrency(contract.getTotalNationalContribRegionalCurrency());
                    oldContract.setTotalNationalContribIFIAmount(contract.getTotalNationalContribIFIAmount());
                    oldContract.setTotalNationalContribIFICurrency(contract.getTotalNationalContribIFICurrency());
                    oldContract.setTotalPrivateContribAmount(contract.getTotalPrivateContribAmount());
                    oldContract.setTotalPrivateContribCurrency(contract.getTotalPrivateContribCurrency());
                    oldContract.setOrganization(contract.getOrganization());
                    oldContract.setStatus(contract.getStatus());
                    oldContract.setType(contract.getType());
                    //oldContract.getDisbursements().clear();
                    Set toRetain=new HashSet();
                   
                    Set newDisbs = contract.getDisbursements();
                    if (newDisbs != null && newDisbs.size() > 0) {
                        Iterator<IPAContractDisbursement> iterNewDisb = newDisbs.iterator();
                        while (iterNewDisb.hasNext()) {
                            IPAContractDisbursement newDisb = iterNewDisb.next();
                            if (newDisb.getId() != null) {
                                IPAContractDisbursement oldDisb = (IPAContractDisbursement) session.load(IPAContractDisbursement.class,
                                        newDisb.getId());
                                oldDisb.setAdjustmentType(newDisb.getAdjustmentType());
                                oldDisb.setAmount(newDisb.getAmount());
                                oldDisb.setCurrency(newDisb.getCurrency());
                                oldDisb.setDate(newDisb.getDate());
                                toRetain.add(oldDisb);
                            } else {
                                if (oldContract.getDisbursements() == null) {
                                    oldContract.setDisbursements(new HashSet());
                                }
                                newDisb.setContract(oldContract);
                                oldContract.getDisbursements().add(newDisb);
                                toRetain.add(newDisb);
                                
                            }
                        }
                        oldContract.getDisbursements().retainAll(toRetain);
                    }
                    else{
                        if(oldContract.getDisbursements()!=null){
                        oldContract.getDisbursements().clear();
                        }
                    }

                    contract=oldContract;

                }
                session.saveOrUpdate(contract);
                ids += contract.getId() + ", ";
            }
            if(ids.length()>2)
            ids = ids.substring(0, ids.length() - 2);



        }
        if (ids.length() != 0) {
            queryString += " and con.id not in (" + ids + ")";
        }
       session.delete(queryString);
       session.flush();
			tx.commit(); // commit the transcation
			logger.debug("Activity saved");
    }
    catch (Exception ex) {
      logger.error("Exception from saveActivity().", ex);
      if (tx != null) {
        try {
          tx.rollback();
          logger.debug("Transaction Rollbacked");
        }
        catch (HibernateException e) {
          logger.error("Rollback failed", e);
        }
      }
    }
    finally {
		if (session != null) {
			try {
				PersistenceManager.releaseSession(session);
			}
			catch (Exception e) {
				logger.error("Release session faliled.", e);
			}
		}
    }

    return activityId;
  }

  /**
	 * Return all reference documents for Activity
	 * 
	 * @param activityId
	 * @return
	 */
  @SuppressWarnings("unchecked")
  public static Collection<AmpActivityReferenceDoc> getReferenceDocumentsFor(Long activityId) throws DgException{
	  String oql="select refdoc from "+AmpActivityReferenceDoc.class.getName()+" refdoc "+
	  " where refdoc.activity.ampActivityId=:actId";
	  try {
		Session session=PersistenceManager.getRequestDBSession();
		Query query=session.createQuery(oql);
		query.setLong("actId", activityId);
		return query.list();
	} catch (Exception e) {
		logger.error(e);
		throw new DgException("Cannot load reference documents for activity id="+activityId,e);
	}
  }

  public static void updateActivityCreator(AmpTeamMember creator,
                                           Long activityId) {
    Session session = null;
    Transaction tx = null;
    AmpActivity oldActivity = null;

    try {
      session = PersistenceManager.getRequestDBSession();
      tx = session.beginTransaction();

      oldActivity = (AmpActivity) session.load(AmpActivity.class, activityId);

      if (oldActivity == null) {
        logger.debug("Previous Activity is null");
        return;
      }

      oldActivity.setActivityCreator(creator);

      session.update(oldActivity);
      tx.commit();
      logger.debug("Activity saved");
    }
    catch (Exception ex) {
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
    }
  }

  public static void updateActivityDocuments(Long activityId, Set documents) {
		Session session = null;
		Transaction tx = null;
		AmpActivity oldActivity = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();

			oldActivity = (AmpActivity) session.load(AmpActivity.class,
					activityId);

			if (oldActivity == null) {
				logger.debug("Previous Activity is null");
				return;
			}

			oldActivity.setDocuments(documents);

			session.update(oldActivity);
			tx.commit();
			logger.debug("Activity saved");
		} catch (Exception ex) {
			logger.error("Exception from saveActivity()  " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
					logger.debug("Transaction Rollbacked");
				} catch (HibernateException e) {
					logger.error("Rollback failed :" + e);
				}
			}
		}
	}

  public static Collection getComponents(Long actId) {
    Session session = null;
    Collection col = new ArrayList();
    logger.info(" this is the other components getting called....");
    try {
      session = PersistenceManager.getSession();
      String queryString = "select comp from " + AmpComponent.class.getName() +
          " comp where (comp.activity=:actId)";
      Query qry = session.createQuery(queryString);
      qry.setParameter("actId", actId, Hibernate.LONG);
      col = qry.list();
    }
    catch (Exception e) {
      logger.error("Unable to get all components");
      logger.error(e.getMessage());
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex) {
        logger.error("Release Session failed :" + ex);
      }
    }
    return col;
  }

  /**
   * Searches activities.
   * Please note that this method is too slow if there are too many activities, because hibernate should load them all. Please use pagination.
   * @param ampThemeId filter by program
   * @param statusCode filter by status if not null
   * @param donorOrgId filter by donor org if not null
   * @param fromDate filter by date if not null
   * @param toDate filter by date if not null
   * @param locationId filter by location if not null
   * @param teamMember filter by team if not null
   * @param pageStart if null then 0 is assumed.
   * @param rowCount number of activities to return
   * @return list of activities.
   * @throws DgException
   */
  public static Collection<AmpActivity> searchActivities(Long ampThemeId,
      String statusCode,
      Long donorOrgId,
      Date fromDate,
      Date toDate,
      Long locationId,
      TeamMember teamMember,Integer pageStart,Integer rowCount) throws DgException{
    Collection<AmpActivity> result = null;
    try {
      Session session = PersistenceManager.getRequestDBSession();

      String oql = "select act from " + AmpActivityProgram.class.getName() + " prog ";
      oql+= getSearchActivitiesWhereClause(ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
      oql += " order by act.name";

      Query query = session.createQuery(oql);
      
      setSearchActivitiesQueryParams(query, ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
      
      if (pageStart!=null && rowCount!=null){
          query.setFirstResult(pageStart);
          query.setMaxResults(rowCount);
      }
      
      result = query.list();
    }
    catch (Exception ex) {
      throw new DgException("Cannot search activities for NPD",ex);
    }

    return result;
  }


  /**
   * Count how man activities will find search without pagination.
   * This method is used together with {@link #searchActivities(Long, String, Long, Date, Date, Long, TeamMember, Integer, Integer)}
   * @param ampThemeId
   * @param statusCode
   * @param donorOrgId
   * @param fromDate
   * @param toDate
   * @param locationId
   * @param teamMember
   * @return
   * @throws DgException
   */
  public static Integer searchActivitiesCount(Long ampThemeId,
	      String statusCode,
	      Long donorOrgId,
	      Date fromDate,
	      Date toDate,
	      Long locationId,
	      TeamMember teamMember) throws DgException{
	    Integer result = null;
	    try {
	      Session session = PersistenceManager.getRequestDBSession();
	      String oql = "select count(act) from " + AmpActivityProgram.class.getName() + " prog ";
	      oql += getSearchActivitiesWhereClause(ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
	      oql += " order by act.name";

	      Query query = session.createQuery(oql);

	      setSearchActivitiesQueryParams(query, ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
	      
	      result = (Integer)query.uniqueResult();
	    }
	    catch (Exception ex) {
	      throw new DgException("Cannot count activities for NPD",ex);
	    }

	    return result;
	  }

  /**
   * Setups query string where clause for search and count methods.
   * @param ampThemeId
   * @param statusCode
   * @param donorOrgId
   * @param fromDate
   * @param toDate
   * @param locationId
   * @param teamMember
   * @return
   * @see #searchActivities(Long, String, Long, Date, Date, Long, TeamMember, Integer, Integer)
   * @see #searchActivitiesCount(Long, String, Long, Date, Date, Long, TeamMember)
   */
  public static String getSearchActivitiesWhereClause(Long ampThemeId,
	      String statusCode,
	      Long donorOrgId,
	      Date fromDate,
	      Date toDate,
	      Long locationId,
	      TeamMember teamMember) {
	  
	  String oql="";
	  
      if (ampThemeId!=null){
    	  oql += " inner join prog.program as theme ";
      }
      oql+=" inner join prog.activity as  act ";
      if (statusCode!=null && !"".equals(statusCode.trim())){
    	  oql+=" join  act.categories as categories ";
      }
      oql+=" where 1=1 ";
//      if (ampThemeId != null) {
//        oql += " and ( :ampThemeId in elements(act.activityPrograms )) ";
//      }
      if (ampThemeId != null) {
          oql += " and ( theme.ampThemeId = :ampThemeId) ";
        }
      if (donorOrgId != null) {
        String s = " and act in (select rol.activity from " +
            AmpOrgRole.class.getName() + " rol, " +
            "where rol.organisation.ampOrgId=:DonorId  )";
        oql += s;
      }
      if (statusCode != null) {
        oql += " and categories.id=:statusCode ";
      }
      if (fromDate != null) {
        oql += " and act.createdDate >= :FromDate";
      }
      if (toDate != null) {
        oql += " and act.createdDate <= :ToDate";
      }
      if (locationId != null) {
        oql += " and act.locations in (from " + AmpLocation.class.getName() +" loc where loc.id=:LocationID)";
      }
      if (teamMember != null) {
        oql += " and ( act.team.ampTeamId =:teamId) "; //oql += " and " +getTeamMemberWhereClause(teamMember);
      }
	  return oql;
  }

  public static void setSearchActivitiesQueryParams(Query query, Long ampThemeId,
	      String statusCode,
	      Long donorOrgId,
	      Date fromDate,
	      Date toDate,
	      Long locationId,
	      TeamMember teamMember) {
	  
      if (ampThemeId != null) {
          query.setLong("ampThemeId", ampThemeId.longValue());
        }
        if (donorOrgId != null) {
          query.setLong("DonorId", donorOrgId.longValue());
        }
        if (statusCode != null) {
          query.setString("statusCode", statusCode);
        }
        if (fromDate != null) {
          query.setDate("FromDate", fromDate);
        }
        if (toDate != null) {
          query.setDate("ToDate", toDate);
        }
        if (locationId != null) {
          query.setLong("LocationID", locationId.longValue());
        }
        if (teamMember!=null && teamMember.getTeamId()!=null){
      	  query.setLong("teamId", teamMember.getTeamId());
        }
        
  }
	  
  
  
  private static String getTeamMemberWhereClause(TeamMember teamMember) {
    Long teamId = teamMember.getTeamId();
    //boolean teamHead = teamMember.getTeamHead();
    String result = " ( act.team.ampTeamId = ";
    result += teamId.toString();
    result += " ) ";
    return result;
  }

  public static Collection getActivityCloseDates(Long activityId) {
    Session session = null;
    Collection col = new ArrayList();

    try {
      session = PersistenceManager.getSession();
      String queryString = "select date from " +
          AmpActivityClosingDates.class.getName() +
          " date where (date.ampActivityId=:actId) and type in (0,1) order by date.ampActivityClosingDateId";
      Query qry = session.createQuery(queryString);
      qry.setParameter("actId", activityId, Hibernate.LONG);
      col = qry.list();
    }
    catch (Exception e) {
      logger.error("Unable to get activity close dates");
      logger.error(e.getMessage());
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex) {
        logger.error("Release Session failed :" + ex);
      }
    }
    return col;
  }

  public static Collection getOrganizationWithRole(Long actId, String roleCode) {
    Session session = null;
    Collection col = new ArrayList();
    try {
      session = PersistenceManager.getSession();
      String qryStr = "select aor from " + AmpOrgRole.class.getName() + " aor " +
          "where (aor.activity=actId)";
      Query qry = session.createQuery(qryStr);
      qry.setParameter("actId", actId, Hibernate.LONG);
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

      AmpActivity act = (AmpActivity) session.load(AmpActivity.class, actId);

      if (act.getOrgrole() != null) {

      }
    }
    catch (Exception e) {
      logger.error("Unable to get Organization with role " + roleCode);
      logger.error(e.getMessage());
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex) {
        logger.error("Release Session failed :" + ex);
      }
    }
    return col;
  }

  /**
   * Load activity from db.
   * Use this one instead of method below this if you realy want to load all data.
   * @author irakli
   * @param id
   * @return
   * @throws DgException
   */
  public static AmpActivity loadActivity(Long id) throws DgException {
		AmpActivity result = null;
		Session session = PersistenceManager.getRequestDBSession();
		try {
			result = (AmpActivity) session.load(AmpActivity.class, id);
		} catch (ObjectNotFoundException e) {
			logger.debug("AmpActivity with id=" + id + " not found");
		} catch (Exception e) {
			throw new DgException("Canno load AmpActivity with id" + id, e);
		}
		return result;
	}
  
  
  //WTF!!!!
  public static AmpActivity getAmpActivity(Long id) {
    Session session = null;
    AmpActivity activity = null;

    try {
      session = PersistenceManager.getRequestDBSession();

      AmpActivity ampActivity = (AmpActivity) session.load(AmpActivity.class,
          id);

      if (ampActivity != null) {
        activity = new AmpActivity();
        activity.setLineMinRank(ampActivity.getLineMinRank());
        activity.setPlanMinRank(ampActivity.getPlanMinRank());
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
        activity.setDocumentSpace(ampActivity.getDocumentSpace());
        activity.setProposedCompletionDate(ampActivity.
                                           getProposedCompletionDate());

        activity.setBudget(ampActivity.getBudget());
        activity.setUpdatedBy(ampActivity.getUpdatedBy());
        activity.setGovAgreementNumber(ampActivity.getGovAgreementNumber());

        activity.setDnrCntTitle(ampActivity.getDnrCntTitle());
        activity.setDnrCntOrganization(ampActivity.getDnrCntOrganization());
        activity.setDnrCntPhoneNumber(ampActivity.getDnrCntPhoneNumber());
        activity.setDnrCntFaxNumber(ampActivity.getDnrCntFaxNumber());

        activity.setMfdCntTitle(ampActivity.getMfdCntTitle());
        activity.setMfdCntOrganization(ampActivity.getMfdCntOrganization());
        activity.setMfdCntPhoneNumber(ampActivity.getMfdCntPhoneNumber());
        activity.setMfdCntFaxNumber(ampActivity.getMfdCntFaxNumber());

        activity.setEmail(ampActivity.getEmail());
        activity.setLanguage(ampActivity.getLanguage());
//			    activity.setLevel(ampActivity.getLevel()); // TO BE DELETED
        activity.setModality(ampActivity.getModality());
        activity.setMofedCntEmail(ampActivity.getMofedCntEmail());
        activity.setMofedCntFirstName(ampActivity.getMofedCntFirstName());
        activity.setMofedCntLastName(ampActivity.getMofedCntLastName());
        activity.setName(ampActivity.getName());
        activity.setObjective(ampActivity.getObjective());
        activity.setPurpose(ampActivity.getPurpose());
        activity.setResults(ampActivity.getResults());
        activity.setOriginalCompDate(ampActivity.getOriginalCompDate());
        activity.setContractingDate(ampActivity.getContractingDate());
        activity.setDisbursmentsDate(ampActivity.getDisbursmentsDate());
        activity.setProgramDescription(ampActivity.getProgramDescription());
        activity.setProposedApprovalDate(ampActivity.getProposedApprovalDate());
        activity.setProposedStartDate(ampActivity.getProposedStartDate());
//			    activity.setStatus(ampActivity.getStatus()); // TO BE DELETED
        activity.setStatusReason(ampActivity.getStatusReason());
        activity.setTeam(ampActivity.getTeam());
        activity.setThemeId(ampActivity.getThemeId());
        activity.setUpdatedDate(ampActivity.getUpdatedDate());
        activity.setVersion(ampActivity.getVersion());

        activity.setActivityPrograms(ampActivity.getActivityPrograms());
        activity.setFunAmount(ampActivity.getFunAmount());
        activity.setFunDate(ampActivity.getFunDate());
        activity.setCurrencyCode(ampActivity.getCurrencyCode());

        activity.setClosingDates(new HashSet(ampActivity.getClosingDates()));
        activity.setComponents(new HashSet(ampActivity.getComponents()));
        activity.setDocuments(new HashSet(ampActivity.getDocuments()));
        activity.setFunding(new HashSet(ampActivity.getFunding()));
        activity.setRegionalFundings(new HashSet(ampActivity.
                                                 getRegionalFundings()));
        activity.setInternalIds(new HashSet(ampActivity.getInternalIds()));
        activity.setLocations(new HashSet(ampActivity.getLocations()));
        activity.setSectors(new HashSet(ampActivity.getSectors()));
        activity.setComponentes(new HashSet(ampActivity.getComponentes())); // yes but why??? 
        activity.setOrgrole(new HashSet(ampActivity.getOrgrole()));
        activity.setIssues(new HashSet(ampActivity.getIssues()));
        activity.setCosts(new HashSet(ampActivity.getCosts()));

        /* Categories */
        activity.setCategories(ampActivity.getCategories());

        /* Content Repository */
	    activity.setActivityDocuments( ampActivity.getActivityDocuments() );

        /*
         * tanzania adds
         */
        activity.setGbsSbs(ampActivity.getGbsSbs());
        activity.setGovernmentApprovalProcedures(ampActivity.
                                                 isGovernmentApprovalProcedures());
        activity.setJointCriteria(ampActivity.isJointCriteria());
        activity.setVote(ampActivity.getVote());
        activity.setSubProgram(ampActivity.getSubProgram());
        activity.setSubVote(ampActivity.getSubVote());
        activity.setFY(ampActivity.getFY());
        activity.setProjectCode(ampActivity.getProjectCode());
        activity.setEqualOpportunity(ampActivity.getEqualOpportunity());
        activity.setEnvironment(ampActivity.getEnvironment());
        activity.setMinorities(ampActivity.getMinorities());
        if (ampActivity.getActivityCreator() != null) {
          activity.setCreatedBy(ampActivity.getActivityCreator());
        }
        // get lessons learned
		if (ampActivity.getLessonsLearned() != null) {
			activity.setLessonsLearned(ampActivity.getLessonsLearned());
		}
		
		activity.setReferenceDocs(ampActivity.getReferenceDocs());
	      
		
		activity.setProjectImpact(ampActivity.getProjectImpact());
        activity.setActivitySummary(ampActivity.getActivitySummary());
        activity.setContractingArrangements(ampActivity.getContractingArrangements());
        activity.setCondSeq(ampActivity.getCondSeq());
        activity.setLinkedActivities(ampActivity.getLinkedActivities());
        activity.setConditionality(ampActivity.getConditionality());
        activity.setProjectManagement(ampActivity.getProjectManagement());
        activity.setContractDetails(ampActivity.getContractDetails());
        
        
        if (ampActivity.getCreditType() != null) {
        	activity.setCreditType(ampActivity.getCreditType());
        }

      }
    }
    catch (Exception e) {
      logger.error("Unable to getAmpActivity");
      e.printStackTrace(System.out);
    }
//		finally {
//			try {
//				session.close();
//			} catch (HibernateException e) {
//				logger.error("Unable to close session");
//				e.printStackTrace();
//			}
//		}
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
      qry.setParameter("actId", actId, Hibernate.LONG);
      Collection act = qry.list();
      Iterator actItr = act.iterator();
      if (actItr.hasNext()) {
        activity = new Activity();
        AmpActivity ampAct = (AmpActivity) actItr.next();
        activity.setActivityId(ampAct.getAmpActivityId());
        activity.setAmpId(ampAct.getAmpId());


//				activity.setStatus(ampAct.getStatus().getName()); // TO BE DELETED
        if (ampAct.getStatusReason() != null)
        	activity.setStatusReason(ampAct.getStatusReason().trim());
        activity.setBudget(ampAct.getBudget());

        activity.setObjective(ampAct.getObjective());
        activity.setPurpose(ampAct.getPurpose());
        activity.setResults(ampAct.getResults());
        activity.setDescription(ampAct.getDescription());

        activity.setLessonsLearned(ampAct.getLessonsLearned());
        activity.setProjectImpact(ampAct.getProjectImpact());
        activity.setActivitySummary(ampAct.getActivitySummary());
        activity.setContractingArrangements(ampAct.getContractingArrangements());
        activity.setCondSeq(ampAct.getCondSeq());
        activity.setLinkedActivities(ampAct.getLinkedActivities());
        activity.setConditionality(ampAct.getConditionality());
        activity.setProjectManagement(ampAct.getProjectManagement());
      
        
        activity.setCurrCompDate(DateConversion.
                                 ConvertDateToString(ampAct.
            getActualCompletionDate()));
        activity.setOrigAppDate(DateConversion.
                                ConvertDateToString(ampAct.
            getProposedApprovalDate()));
        activity.setOrigStartDate(DateConversion.
                                  ConvertDateToString(ampAct.
            getProposedStartDate()));
        activity.setPropCompDate(DateConversion.ConvertDateToString(ampAct.getProposedCompletionDate()));
        activity.setRevAppDate(DateConversion.
                               ConvertDateToString(ampAct.getActualApprovalDate()));
        activity.setRevStartDate(DateConversion.
                                 ConvertDateToString(ampAct.getActualStartDate()));
        activity.setContractingDate(DateConversion.
                                    ConvertDateToString(ampAct.
            getContractingDate()));
        activity.setDisbursmentsDate(DateConversion.
                                     ConvertDateToString(ampAct.
            getDisbursmentsDate()));
        activity.setUpdatedBy(ampAct.getUpdatedBy());
        activity.setUpdatedDate(ampAct.getUpdatedDate());

        /* Set Categories */
        activity.setAccessionInstrument(
            CategoryManagerUtil.getStringValueOfAmpCategoryValue(
                CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.ACCESSION_INSTRUMENT_NAME, ampAct.getCategories())
            )
            );
        activity.setAcChapter(
            CategoryManagerUtil.getStringValueOfAmpCategoryValue(
                CategoryManagerUtil.getAmpCategoryValueFromList(
            CategoryConstants.ACCHAPTER_NAME, ampAct.getCategories())
            )
            );
        activity.setStatus(
            CategoryManagerUtil.getStringValueOfAmpCategoryValue(
                CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            CategoryConstants.ACTIVITY_STATUS_KEY, ampAct.getCategories())
            )
            );
        activity.setImpLevel(
            CategoryManagerUtil.getStringValueOfAmpCategoryValue(
                CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            CategoryConstants.IMPLEMENTATION_LEVEL_KEY, ampAct.getCategories())
            )
            );
        /* END - Set Categories */
        
        activity.setDraft( ampAct.getDraft() );
        activity.setApprovalStatus( ampAct.getApprovalStatus() );

        activity.setFinancialInstrument(CategoryManagerUtil.getStringValueOfAmpCategoryValue(
                CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            CategoryConstants.FINANCIAL_INSTRUMENT_KEY, ampAct.getCategories())
            ));
        /*
         * Tanzania adds
         */

        activity.setFY(ampAct.getFY());

        activity.setVote(ampAct.getVote());
        activity.setSubProgram(ampAct.getSubProgram());
        activity.setSubVote(ampAct.getSubVote());
        activity.setJointCriteria(ampAct.isJointCriteria());
        activity.setGovernmentApprovalProcedures(ampAct.
                                                 isGovernmentApprovalProcedures());
        activity.setProjectCode(ampAct.getProjectCode());


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
        Collections.sort(dates, DateConversion.dtComp);
        activity.setRevCompDates(dates);

        if (ampAct.getThemeId() != null) {
          activity.setProgram(ampAct.getThemeId().getName());
          activity.setProgramDescription(ampAct.getProgramDescription());
        }

        activity.setContractors(ampAct.getContractors());

        activity.setContFirstName(ampAct.getContFirstName());
        activity.setContLastName(ampAct.getContLastName());
        activity.setEmail(ampAct.getEmail());
        activity.setDnrCntTitle(ampAct.getDnrCntTitle());
        activity.setDnrCntOrganization(ampAct.getDnrCntOrganization());
        activity.setDnrCntPhoneNumber(ampAct.getDnrCntPhoneNumber());
        activity.setDnrCntFaxNumber(ampAct.getDnrCntFaxNumber());

        activity.setMfdContFirstName(ampAct.getMofedCntFirstName());
        activity.setMfdContLastName(ampAct.getMofedCntLastName());
        activity.setMfdContEmail(ampAct.getMofedCntEmail());
        activity.setMfdCntTitle(ampAct.getMfdCntTitle());
        activity.setMfdCntOrganization(ampAct.getMfdCntOrganization());
        activity.setMfdCntPhoneNumber(ampAct.getMfdCntPhoneNumber());
        activity.setMfdCntFaxNumber(ampAct.getMfdCntFaxNumber());

        if (ampAct.getCreatedDate() != null) {
          activity.setCreatedDate(
              DateConversion.ConvertDateToString(ampAct.getCreatedDate()));
        }

        if (ampAct.getActivityCreator() != null) {
          activity.setCreatedBy(ampAct.getActivityCreator());
          User usr = ampAct.getActivityCreator().getUser();
          if (usr != null) {
            activity.setActAthFirstName(usr.getFirstNames());
            activity.setActAthLastName(usr.getLastName());
            activity.setActAthEmail(usr.getEmail());
            activity.setActAthAgencySource(usr.getOrganizationName());
          }
        }

        if (ampAct.getModality() != null) {
          activity.setModality(ampAct.getModality().getValue());
          activity.setModalityCode(ampAct.getModality().getIndex() + "");
        }

        queryString = "select distinct f.typeOfAssistance.value from " +
            AmpFunding.class.getName() + " f where f.ampActivityId=:actId";

        qry = session.createQuery(queryString);
        qry.setParameter("actId", actId, Hibernate.LONG);

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
            AmpOrganisation auxOrgRel = orgRole.getOrganisation();
            if(auxOrgRel!=null)
            {
            	RelOrganization relOrg = new RelOrganization();
                relOrg.setOrgName(auxOrgRel.getName());
                relOrg.setRole(orgRole.getRole().getRoleCode());
                relOrg.setAcronym(auxOrgRel.getAcronym());
                relOrg.setOrgCode(auxOrgRel.getOrgCode());
                relOrg.setOrgGrpId(auxOrgRel.getOrgGrpId());
                relOrg.setOrgTypeId(auxOrgRel.getOrgTypeId());
                relOrg.setOrgId(auxOrgRel.getAmpOrgId());
                if (!relOrgs.contains(relOrg)) {
                	relOrgs.add(relOrg);
                }
            }
          }
        }
        activity.setRelOrgs(relOrgs);

        Collection sectors = new ArrayList();
        if (ampAct.getSectors() != null) {
          Iterator sectItr = ampAct.getSectors().iterator();
          while (sectItr.hasNext()) {
            //AmpSector sec = (AmpSector) sectItr.next();
            AmpActivitySector ampActSect = (AmpActivitySector) sectItr.next();
            AmpSector sec = ampActSect.getSectorId();
            ActivitySector actSect = new ActivitySector();
            actSect.setSectorPercentage(ampActSect.getSectorPercentage());
            actSect.setSectorScheme(sec.getAmpSecSchemeId().getSecSchemeName());
            if (sec.getParentSectorId() == null) {
              actSect.setSectorName(sec.getName());
            }
            else if (sec.getParentSectorId().getParentSectorId() == null) {
              actSect.setSectorName(sec.getParentSectorId().getName());
              actSect.setSubsectorLevel1Name(sec.getName());
            }
            else {
              actSect.setSectorName(sec.getParentSectorId().getParentSectorId().
                                    getName());
              actSect.setSubsectorLevel1Name(sec.getParentSectorId().getName());
              actSect.setSubsectorLevel2Name(sec.getName());
            }
            sectors.add(actSect);
          }
        }
        activity.setSectors(sectors);

        if (ampAct.getActivityPrograms() != null) {
          Collection programs = new ArrayList();
          programs.addAll(ampAct.getActivityPrograms());
          activity.setActPrograms(programs);
        }

        AmpCategoryValue ampCategoryValueForStatus =
            CategoryManagerUtil.getAmpCategoryValueFromListByKey(
            CategoryConstants.IMPLEMENTATION_LEVEL_KEY, ampAct.getCategories());
        if (ampCategoryValueForStatus != null) {
          activity.setImpLevel(ampCategoryValueForStatus.getValue());
        }

        Collection locColl = new ArrayList();
        if (ampAct.getLocations() != null) {
          Iterator locItr = ampAct.getLocations().iterator();
          while (locItr.hasNext()) {
            AmpActivityLocation actLoc = (AmpActivityLocation) locItr.next();
            if(actLoc!=null){
            	AmpLocation ampLoc = actLoc.getLocation();
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
                if(actLoc.getLocationPercentage()!=null)
                	loc.setPercent(DecimalToText.ConvertDecimalToText(actLoc.getLocationPercentage()));
                locColl.add(loc);
            }
            
          }
        }
        activity.setLocations(locColl);
        //set lessons learned
        //activity.setLessonsLearned(ampAct.getLessonsLearned());

        activity.setProjectIds(ampAct.getInternalIds());

        Collection modalities = new ArrayList();
        queryString = "select fund from " + AmpFunding.class.getName() +
            " fund " +
            "where (fund.ampActivityId=:actId)";
        qry = session.createQuery(queryString);
        qry.setParameter("actId", actId, Hibernate.LONG);
        Iterator itr = qry.list().iterator();
        while (itr.hasNext()) {
          AmpFunding fund = (AmpFunding) itr.next();
          if (fund.getFinancingInstrument() != null)
        	  modalities.add( fund.getFinancingInstrument() );
        }
        activity.setModalities(modalities);
        activity.setUniqueModalities(new TreeSet(modalities));


      }
    }
    catch (Exception e) {
      logger.error("Unable to get channnel overview");
      e.printStackTrace(System.out);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex) {
        logger.error("Release Session failed :" + ex);
      }
    }
    return activity;
  }

  public static Collection getActivitySectors(Long actId) {
    Session session = null;
    Collection sectors = new ArrayList();

    try {
      session = PersistenceManager.getSession();
      String queryString = "select a from " + AmpActivity.class.getName() +
          " a " + "where (a.ampActivityId=:actId)";
      Query qry = session.createQuery(queryString);
      qry.setParameter("actId", actId, Hibernate.LONG);
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
    }
    catch (Exception ex) {
      logger.error("Unable to get activity sectors :" + ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
        logger.error("releaseSession() failed ");
      }
    }
    return sectors;
  }

  public static Collection getOrgRole(Long id) {
    Session session = null;
    Collection orgroles = new ArrayList();
    try {
      session = PersistenceManager.getSession();
      String queryString = "select aor from " + AmpOrgRole.class.getName() +
          " aor " + "where (aor.activity=:actId)";
      Query qry = session.createQuery(queryString);
      qry.setParameter("actId", id, Hibernate.LONG);
      orgroles = qry.list();
    }
    catch (Exception ex) {
      logger.error("Unable to get activity sectors :" + ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
        logger.error("releaseSession() failed ");
      }
    }
    return orgroles;
  }

  public static Collection getFundingByOrg(Long id) {
    Session session = null;
    Collection orgroles = new ArrayList();
    try {
      session = PersistenceManager.getSession();
      String queryString = "select f from " + AmpFunding.class.getName() +
          " f " + "where (f.ampDonorOrgId=:orgId)";
      Query qry = session.createQuery(queryString);
      qry.setParameter("orgId", id, Hibernate.LONG);
      orgroles = qry.list();
    }
    catch (Exception ex) {
      logger.error("Unable to get fundings for organization :" + ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
        logger.error("releaseSession() failed ");
      }
    }
    return orgroles;
  }

  public static Collection<Components> getAllComponents(Long id) {
    Collection<Components> componentsCollection = new ArrayList<Components>();

    Session session = null;

    try {
      session = PersistenceManager.getSession();
      AmpActivity activity = (AmpActivity) session.load(AmpActivity.class, id);
      Set comp = activity.getComponents();
      if (comp != null && comp.size() > 0) {
        Iterator itr1 = comp.iterator();
        while (itr1.hasNext()) {
          AmpComponent ampComp = (AmpComponent) itr1.next();
          Components<FundingDetail> components = new Components<FundingDetail>();
          components.setComponentId(ampComp.getAmpComponentId());
          components.setDescription(ampComp.getDescription());
          components.setTitle(ampComp.getTitle());
          components.setCommitments(new ArrayList());
          components.setDisbursements(new ArrayList());
          components.setExpenditures(new ArrayList());
          components.setPhyProgress(new ArrayList());

          Collection<AmpComponentFunding> componentsFunding = ActivityUtil.getFundingComponentActivity(ampComp.
              getAmpComponentId(), activity.getAmpActivityId());
          Iterator compFundIterator = componentsFunding.iterator();
          while (compFundIterator.hasNext()) {
            AmpComponentFunding cf = (AmpComponentFunding) compFundIterator.next();
            FundingDetail fd = new FundingDetail();
            fd.setAdjustmentType(cf.getAdjustmentType().intValue());
            if (fd.getAdjustmentType() == Constants.PLANNED) {
              fd.setAdjustmentTypeName("Planned");
            }
            else {
              fd.setAdjustmentTypeName("Actual");
            }
            fd.setCurrencyCode(cf.getCurrency().getCurrencyCode());
            fd.setCurrencyName(cf.getCurrency().getCurrencyName());
            fd.setPerspectiveCode(cf.getPerspective().getCode());
            fd.setPerspectiveName(cf.getPerspective().getName());
            fd.setTransactionAmount(FormatHelper.formatNumber(cf.getTransactionAmount().doubleValue()));
            fd.setTransactionDate(
                DateConversion.ConvertDateToString(
                    cf.getTransactionDate()));
            fd.setTransactionType(cf.getTransactionType().intValue());
            if (fd.getTransactionType() == Constants.COMMITMENT) {
              components.getCommitments().add(fd);
            }
            else if (fd.getTransactionType() == Constants.DISBURSEMENT) {
              components.getDisbursements().add(fd);
            }
            else if (fd.getTransactionType() == Constants.EXPENDITURE) {
              components.getExpenditures().add(fd);
            }
          }
          Collection<AmpPhysicalPerformance> physicalPerf = ActivityUtil.getPhysicalProgressComponentActivity(
        		  											ampComp.getAmpComponentId(), activity.getAmpActivityId());
          Iterator<AmpPhysicalPerformance> physicalPerfIterator = physicalPerf.iterator();
          while (physicalPerfIterator.hasNext()) {
            AmpPhysicalPerformance ampPhyPerf = (AmpPhysicalPerformance) physicalPerfIterator.
                next();
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
            Collections.sort(list, FundingValidator.dateComp);
          }
          components.setCommitments(list);
          list = null;
          if (components.getDisbursements() != null) {
            list = new ArrayList(components.getDisbursements());
            Collections.sort(list, FundingValidator.dateComp);
          }
          components.setDisbursements(list);
          list = null;
          if (components.getExpenditures() != null) {
            list = new ArrayList(components.getExpenditures());
            Collections.sort(list, FundingValidator.dateComp);
          }
          components.setExpenditures(list);
          componentsCollection.add(components);
        }
      }

    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    return componentsCollection;
  }

  /*
   * edited by Govind Dalwani
   */
  // this function is to get the fundings for the components along with the activity Id

  public static Collection<AmpComponentFunding> getFundingComponentActivity(Long componentId, Long activityId) {
    Collection col = null;
    logger.info(" inside getting the funding.....");
    Session session = null;

    try {
      session = PersistenceManager.getSession();
      String qryStr = "select a from " + AmpComponentFunding.class.getName() +
          " a " +
          "where amp_component_id = '" + componentId + "' and activity_id = '" + activityId +
          "'";
      Query qry = session.createQuery(qryStr);
      col = qry.list();
    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    //getComponents();
    return col;
  }

  /*
   * This function gets AmpComponentFunding of an Activity.
   *
   * @param activityId Activity id
   */
  public static Collection<AmpComponentFunding> getFundingComponentActivity(Long activityId) {
    Collection col = null;
    logger.info(" inside getting the funding.....");
    Session session = null;

    try {
      session = PersistenceManager.getSession();
      String qryStr = "select a from " + AmpComponentFunding.class.getName() +
          " a where activity_id = '" + activityId + "'";
      Query qry = session.createQuery(qryStr);
      col = qry.list();
    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    //getComponents();
    return col;
  }

  // function for getting fundings for components and ids ends here

  //function for physical progress

  public static Collection<AmpPhysicalPerformance> getPhysicalProgressComponentActivity(Long id,
      Long actId) {
    Collection col = null;
    logger.info(" inside getting the Physical Progress.....");
    Session session = null;

    try {
      session = PersistenceManager.getSession();
      String qryStr = "select a from " + AmpPhysicalPerformance.class.getName() +
          " a " +
          "where amp_component_id = '" + id + "' and amp_activity_id = '" +
          actId + "'";
      Query qry = session.createQuery(qryStr);
      //Iterator itr = qry.list().iterator();
      col = qry.list();

    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    return col;
  }

  //function end physical progress
//function to get all the components in the database
  public static Collection getAllComponentNames() {
    Collection col = null;
    logger.info(" inside getting the components.....");
    Session session = null;

    try {
      session = PersistenceManager.getSession();
      String qryStr = "select a from " + AmpComponent.class.getName() + " a ";
      Query qry = session.createQuery(qryStr);
      col = qry.list();

    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    // EditActivityForm f = new EditActivityForm();
    //f.setAllComps(col);

    return col;
  }

//end functino to get components
  public static ArrayList getIssues(Long id) {
    ArrayList list = new ArrayList();

    Session session = null;
    try {
      session = PersistenceManager.getSession();
      AmpActivity activity = (AmpActivity) session.load(AmpActivity.class, id);
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
            Iterator mItr = ampIssue.getMeasures().iterator();
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
    }
    catch (Exception e) {
      logger.debug("Exception in getIssues() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    return list;
  }

  public static Collection getRegionalFundings(Long id) {
    Collection col = new ArrayList();

    Session session = null;
    try {
      session = PersistenceManager.getSession();
      AmpActivity activity = (AmpActivity) session.load(AmpActivity.class, id);
      col = activity.getRegionalFundings();
    }
    catch (Exception e) {
      logger.debug("Exception in getRegionalFundings() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    return col;
  }

  public static Collection getRegionalFundings(Long id, Long regId) {
    Collection col = new ArrayList();

    Session session = null;
    try {
      session = PersistenceManager.getSession();
      AmpActivity activity = (AmpActivity) session.load(AmpActivity.class, id);
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
    }
    catch (Exception e) {
      logger.debug("Exception in getRegionalFundings() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    return col;
  }

  public static AmpActivity getActivityByName(String name) {
    AmpActivity activity = null;
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      String qryStr = "select a from " + AmpActivity.class.getName() + " a " +
          "where lower(a.name) = :lowerName";
      Query qry = session.createQuery(qryStr);
      qry.setString("lowerName", name.toLowerCase());
      Iterator itr = qry.list().iterator();
      if (itr.hasNext()) {
        activity = (AmpActivity) itr.next();
      }
    }
    catch (Exception e) {
      logger.debug("Exception in isActivityExisting() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex) {
          logger.debug("Exception while releasing session " + ex.getMessage());
        }
      }
    }
    return activity;
  }

  public static void saveDonorFundingInfo(Long actId, Set fundings) {
    Session session = null;
    Transaction tx = null;

    try {
      session = PersistenceManager.getSession();
      tx = session.beginTransaction();

      //logger.info("Before iterating");
      Iterator itr = fundings.iterator();
      while (itr.hasNext()) {
        AmpFunding temp = (AmpFunding) itr.next();
        AmpFunding fund = (AmpFunding) session.load(AmpFunding.class,
            temp.getAmpFundingId());
        Iterator fItr = fund.getFundingDetails().iterator();
        while (fItr.hasNext()) {
          AmpFundingDetail fd = (AmpFundingDetail) fItr.next();
          session.delete(fd);
        }
        fund.getFundingDetails().clear();
        fund.setFundingDetails(temp.getFundingDetails());

        fund.getMtefProjections().clear();
        fund.getMtefProjections().addAll( temp.getMtefProjections() );
        //logger.info("Updating " + fund.getAmpFundingId());
        session.update(fund);
        //logger.info("Updated...");
      }
      tx.commit();
      //logger.info("Donor info. saved");
    }
    catch (Exception e) {
      logger.error("Exception from saveDonorFundingInfo()");
      e.printStackTrace(System.out);
      if (tx != null) {
        try {
          tx.rollback();
        }
        catch (Exception rbf) {
          logger.error("Rollback failed");
        }
      }
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception rsf) {
          logger.error("Release session failed");
        }
      }
    }

  }

  public static boolean canViewActivity(Long actId, TeamMember tm) {
    boolean canView = false;
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      if (tm.getTeamHead()) {
        if (tm.getTeamType().equalsIgnoreCase("DONOR")) {
          // DONOR team leader
          AmpTeam team = (AmpTeam) session.load(AmpTeam.class, tm.getTeamId());
          AmpActivity act = new AmpActivity();
          act.setAmpActivityId(actId);
          if (team.getActivityList().contains(act))
            canView = true;
        }
        else {
          // MOFED team leader
          //logger.info("Mofed team leader");
          //logger.info("loading activity " + actId);
          AmpActivity act = (AmpActivity) session.load(AmpActivity.class, actId);
          if (act.getTeam().getAmpTeamId().equals(tm.getTeamId())) {
            logger.debug("Can view " + actId + " , team " + tm.getTeamId());
            canView = true;
          }
          else {

          }
        }
      }
      else {
        AmpTeamMember ampTeamMem = (AmpTeamMember) session.load(AmpTeamMember.class,
            tm.getMemberId());
        AmpActivity act = new AmpActivity();
        act.setAmpActivityId(actId);
        if (ampTeamMem.getActivities().contains(act))
          canView = true;
      }
    }
    catch (Exception e) {
      logger.error("Exception from canViewActivity()");
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception rsf) {
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
      AmpActivity act = (AmpActivity) session.load(AmpActivity.class, actId);
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

    }
    catch (Exception e) {
      logger.error("Exception from getDonors()");
      e.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception rsf) {
          logger.error("Release session failed");
        }
      }
    }
    return col;
  }

  public static long getActivityMaxId() {
    Session session = null;
    long maxId = 0;

    try {
      session = PersistenceManager.getSession();

      String queryString = "select max(act.ampActivityId) from "
          + AmpActivity.class.getName() + " act";
      Query qry = session.createQuery(queryString);
      Iterator itr = qry.list().iterator();
      if (itr.hasNext()) {
        Long temp = (Long) itr.next();
        if (temp != null) {
          maxId = temp.longValue();
        }
      }

    }
    catch (Exception e) {
      logger.error("Uanble to max id :" + e);
    }
    finally {

      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex) {
        logger.error("releaseSession() failed " + ex);
      }
    }
    return maxId;
  }

  public static AmpActivity getProjectChannelOverview(Long id) {
    Session session = null;
    AmpActivity activity = null;

    try {
      logger.debug("Id is " + id);
      session = PersistenceManager.getSession();

      // modified by Priyajith
      // Desc: removed the usage of session.load and used the select query
      // start
      String queryString = "select a from " + AmpActivity.class.getName()
          + " a " + "where (a.ampActivityId=:id)";
      Query qry = session.createQuery(queryString);
      qry.setParameter("id", id, Hibernate.LONG);
      Iterator itr = qry.list().iterator();
      while (itr.hasNext())
        activity = (AmpActivity) itr.next();
      // end
    }
    catch (Exception ex) {
      logger
          .error("Unable to get Amp Activity getProjectChannelOverview() :"
                 + ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
        logger.error("releaseSession() failed ");
      }
    }
    return activity;
  }
   /*
   * get the  the Contracts for Activity
   * 
   */
  
  public static List getIPAContracts(Long activityId) {
    Session session = null;
    List<IPAContract> contrcats = null;

    try {
      session = PersistenceManager.getRequestDBSession();

     
      String queryString = "select con from " + IPAContract.class.getName()
          + " con " + "where (con.activity=:activityId)";
      Query qry = session.createQuery(queryString);
      qry.setLong("activityId",activityId );
      contrcats = qry.list();
      ArrayList<IPAContract> fullContracts=new ArrayList<IPAContract>();
      for(Iterator i=contrcats.iterator();i.hasNext();)
      {
    	  IPAContract c=(IPAContract) i.next();
    	  double td=0;
    	  for(Iterator j=c.getDisbursements().iterator();j.hasNext();)
    	  {
    		  IPAContractDisbursement cd=(IPAContractDisbursement) j.next();
    		  if(cd.getAmount()!=null)
    			  td+=cd.getAmount().doubleValue();
    	  }
    	  c.setTotalDisbursements(new Double(td));
      }
    }
     
    catch (Exception ex) {
      logger
          .error("Unable to get IPAContracts :"
                 + ex);
    }
    
    return  contrcats ;
  } 
/**
 * @author dan
 * @return
 */
  
  public static List getIPAContracts(Long activityId, String currCode) {
	    Session session = null;
	    List<IPAContract> contrcats = null;

	    try {
	      session = PersistenceManager.getRequestDBSession();

	     
	      String queryString = "select con from " + IPAContract.class.getName()
	          + " con " + "where (con.activity=:activityId)";
	      Query qry = session.createQuery(queryString);
	      qry.setLong("activityId",activityId );
	      contrcats = qry.list();
	      String cc=currCode;
          
          double usdAmount;  
  		   double finalAmount; 

	      for(Iterator i=contrcats.iterator();i.hasNext();)
	      {
	    	  IPAContract c=(IPAContract) i.next();
	    	  if(c.getDibusrsementsGlobalCurrency()!=null)
	          	   cc=c.getDibusrsementsGlobalCurrency().getCurrencyCode();
	    	  double td=0;
	    	  for(Iterator j=c.getDisbursements().iterator();j.hasNext();)
	    	  {
	    		  IPAContractDisbursement cd=(IPAContractDisbursement) j.next();
	    		  if(cd.getAmount()!=null)
     			  {
     			  	usdAmount = CurrencyWorker.convertToUSD(cd.getAmount().doubleValue(),cd.getCurrCode());
     			  	finalAmount = CurrencyWorker.convertFromUSD(usdAmount,cc);
     			  	td+=finalAmount;
     			  }
	    	  }
	    	  c.setTotalDisbursements(new Double(td));
	    	  if(c.getTotalAmount()!=null)
	    	  {
	    		  double usdAmount1=0;  
	      		   double finalAmount1=0; 
	             	try {
	     				usdAmount1 = CurrencyWorker.convertToUSD(c.getTotalAmount().doubleValue(),c.getTotalAmountCurrency().getCurrencyCode());
	     			} catch (AimException e) {
	     				// TODO Auto-generated catch block
	     				e.printStackTrace();
	     			}
	     			  	try {
	     				finalAmount1 = CurrencyWorker.convertFromUSD(usdAmount1,cc);
	     			} catch (AimException e) {
	     				// TODO Auto-generated catch block
	     				e.printStackTrace();
	     			}	
	    		  
	    		  double execRate=c.getTotalDisbursements()/finalAmount1;
	    		  System.out.println("1 execution rate: "+execRate);
	    		  c.setExecutionRate(execRate);
	    	  }
	    	  else c.setExecutionRate(new Double(0));
	    	  
	      }
	    }
	     
	    catch (Exception ex) {
	      logger
	          .error("Unable to get IPAContracts :"
	                 + ex);
	    }
	    
	    return  contrcats ;
	  } 

  /*
   * get the list of all the activities
   * to display in the activity manager of Admin
   */
  public static List<AmpActivity> getAllActivitiesList() {
    List col = null;
    Session session = null;
    Query qry = null;

    try {
      session = PersistenceManager.getSession();
      String queryString = "select ampAct from " + AmpActivity.class.getName() +
          " ampAct";
      qry = session.createQuery(queryString);
      col = qry.list();
      logger.debug("the size of the ampActivity : " + col.size());
    }
    catch (Exception e1) {
      logger.error("Could not retrieve the activities list from getallactivitieslist");
      e1.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception e2) {
          logger.error("Release session failed");
        }
      }
    }
    return col;
  }

  /*
   * get the list of all the activities
   * to display in the activity manager of Admin
   */
  public static List<AmpActivity> getAllActivitiesByName(String name) {
    List col = null;
    Session session = null;
    Query qry = null;

    try {
      session = PersistenceManager.getSession();
      String queryString = "select ampAct from " + AmpActivity.class.getName() +
          " ampAct where ampAct.name like (:name)";
      qry = session.createQuery(queryString);
      qry.setParameter("name", "%" + name + "%", Hibernate.STRING);
      col = qry.list();
      logger.debug("the size of the ampActivity : " + col.size());
    }
    catch (Exception e1) {
      logger.error("Could not retrieve the activities list from getallactivitiesbyname", e1);
      e1.printStackTrace();
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception e2) {
          logger.error("Release session failed");
        }
      }
    }
    return col;
  }

  /* functions to DELETE an activity by Admin start here.... */
  public static void deleteActivity(Long ampActId) {
    Session session = null;
    Transaction tx = null;

    try {
      session = PersistenceManager.getSession();
      tx = session.beginTransaction();

      AmpActivity ampAct = (AmpActivity) session.load(
          AmpActivity.class, ampActId);

      if (ampAct == null)
        logger.debug("Activity is null. Hence no activity with id : " +
                     ampActId);
      else {
        /* delete fundings and funding details */
        Set fundSet = ampAct.getFunding();
        if (fundSet != null) {
          Iterator fundSetItr = fundSet.iterator();
          while (fundSetItr.hasNext()) {
            AmpFunding fund = (AmpFunding) fundSetItr.next();
            Set fundDetSet = fund.getFundingDetails();
            if (fundDetSet != null) {
              Iterator fundDetItr = fundDetSet.iterator();
              while (fundDetItr.hasNext()) {
                AmpFundingDetail ampFundingDetail = (AmpFundingDetail)
                    fundDetItr.next();
                session.delete(ampFundingDetail);
              }
            }
            Set closingDate = fund.getClosingDateHistory();
            if (closingDate != null) {
              Iterator closingDateItr = closingDate.iterator();
              while (closingDateItr.hasNext()) {
                AmpClosingDateHistory closeHistory = (AmpClosingDateHistory)
                    closingDateItr.next();
                session.delete(closeHistory);
              }
            }
            session.delete(fund);
          }
        }

        /* delete regional fundings */
        fundSet = ampAct.getRegionalFundings();
        if (fundSet != null) {
          Iterator fundSetItr = fundSet.iterator();
          while (fundSetItr.hasNext()) {
            AmpRegionalFunding regFund = (AmpRegionalFunding) fundSetItr.next();
            session.delete(regFund);
          }
        }

        /* delete components */
        Set comp = ampAct.getComponents();
        if (comp != null) {
          Iterator compItr = comp.iterator();
          while (compItr.hasNext()) {
            AmpComponent ampComp = (AmpComponent) compItr.next();
            session.delete(ampComp);
          }
        }


        /* delete Component Fundings */
        Collection<AmpComponentFunding>  componentFundingCol = getFundingComponentActivity(ampActId);
        if (componentFundingCol != null) {
  			Iterator<AmpComponentFunding> componentFundingColIt = componentFundingCol.iterator();
  			while (componentFundingColIt.hasNext()) {
  				session.delete(componentFundingColIt.next());
  			}
	  	}

        /* delete org roles */
        Set orgrole = ampAct.getOrgrole();
        if (orgrole != null) {
          Iterator orgroleItr = orgrole.iterator();
          while (orgroleItr.hasNext()) {
            AmpOrgRole ampOrgrole = (AmpOrgRole) orgroleItr.next();
            session.delete(ampOrgrole);
          }
        }

  
        /* delete issues,measures,actors */
        Set issues = ampAct.getIssues();
        if (issues != null) {
          Iterator iItr = issues.iterator();
          while (iItr.hasNext()) {
            AmpIssues issue = (AmpIssues) iItr.next();
            Set measure = issue.getMeasures();
            if (measure != null) {
              Iterator measureItr = measure.iterator();
              while (measureItr.hasNext()) {
                AmpMeasure ampMeasure = (AmpMeasure) measureItr.next();
                Set actor = ampMeasure.getActors();
                if (actor != null) {
                  Iterator actorItr = actor.iterator();
                  while (actorItr.hasNext()) {
                    AmpActor ampActor = (AmpActor) actorItr.next();
                    session.delete(ampActor);
                  }
                }
                session.delete(ampMeasure);
              }
            }
            session.delete(issue);
          }
        }

        // delete all previous sectors
        Set sectors = ampAct.getSectors();
        if (sectors != null) {
          Iterator iItr = sectors.iterator();
          while (iItr.hasNext()) {
            AmpActivitySector sec = (AmpActivitySector) iItr.next();
            session.delete(sec);
          }
        }

        /* delete activity internal id
             Set internalIds = ampAct.getInternalIds();
             if(internalIds != null)
             {
         Iterator interIdItr = internalIds.iterator();
         while(interIdItr.hasNext())
         {
         AmpActivityInternalId ampInternalId = (AmpActivityInternalId) interIdItr.next();
          logger.info("internal id : "+ampInternalId.getInternalId());
          session.delete(ampInternalId);
         }
             }
         */

        /* delete AMP activity Survey */
        Set ampSurvey = ampAct.getSurvey();
        if (ampSurvey != null) {
          Iterator surveyItr = ampSurvey.iterator();
          while (surveyItr.hasNext()) {
            AmpAhsurvey ahSurvey = (AmpAhsurvey) surveyItr.next();
            Set ahAmpSurvey = ahSurvey.getResponses();
            if (ahSurvey != null) {
              Iterator ahSurveyItr = ahAmpSurvey.iterator();
              while (ahSurveyItr.hasNext()) {
                AmpAhsurveyResponse surveyResp = (AmpAhsurveyResponse)
                    ahSurveyItr.next();
                session.delete(surveyResp);
              }
            }
            session.delete(ahSurvey);
          }
        }

        /* delete the activity relevant notes */
        Set notesSet = ampAct.getNotes();
        if (notesSet != null) {
          Iterator notesItr = notesSet.iterator();
          while (notesItr.hasNext()) {
            AmpNotes notesAmp = (AmpNotes) notesItr.next();
            session.delete(notesAmp);
          }
        }


        /* delete the activity closing dates */
        Set closingDates = ampAct.getClosingDates();
        if (closingDates != null) {
          Iterator closingDatesItr = closingDates.iterator();
          while (closingDatesItr.hasNext()) {
            AmpActivityClosingDates closingDatesItem = (AmpActivityClosingDates) closingDatesItr.next();
            session.delete(closingDatesItem);
            
          }
        }
        
       
        

        
        //	 delete all previous comments
        ArrayList col = org.digijava.module.aim.util.DbUtil.
            getAllCommentsByActivityId(ampAct.getAmpActivityId());
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
      session.delete(ampAct);
      tx.commit();
      session.flush();
    }
    catch (Exception e1) {
      logger.error("Could not delete the activity with id : " + ampActId);
      e1.printStackTrace(System.out);
    }
    finally {
      if (session != null) {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception e2) {
          logger.error("Release session failed");
        }
      }
    }
  }

  public static void deleteActivityAmpComments(Collection commentId) {
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      if (commentId != null) {
        Iterator commentItr = commentId.iterator();
        while (commentItr.hasNext()) {
          AmpComments ampComment = (AmpComments) commentItr.next();
          AmpComments ampComm = (AmpComments) session.load
              (AmpComments.class, ampComment.getAmpCommentId());
          session.delete(ampComm);
        }
      }
    }
    catch (Exception e1) {
      logger.error(
          "Could not delete/find the comments revelant to the activity");
      e1.printStackTrace(System.out);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception e2) {
        logger.error("Release session failed");
      }
    }
  }

  public static void deleteActivityPhysicalComponentReport(Collection
      phyCompReport) {
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      if (phyCompReport != null) {
        Iterator phyReportItr = phyCompReport.iterator();
        while (phyReportItr.hasNext()) {
          AmpPhysicalComponentReport phyReport = (AmpPhysicalComponentReport)
              phyReportItr.next();
          AmpPhysicalComponentReport physicalReport = (
              AmpPhysicalComponentReport) session.load
              (AmpPhysicalComponentReport.class, phyReport.getAmpReportId());
          session.delete(physicalReport);
        }
      }
    }
    catch (Exception e1) {
      logger.error(
          "could not delete/find the physical component report activities");
      e1.printStackTrace(System.out);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception e2) {
        logger.error("Release session failed");
      }
    }
  }

  public static void deleteActivityAmpReportCache(Collection repCache) {
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      if (repCache != null) {
        Iterator repCacheItr = repCache.iterator();
        while (repCacheItr.hasNext()) {
          AmpReportCache reportCache = (AmpReportCache) repCacheItr.next();
          AmpReportCache ampReportCache = (AmpReportCache) session.load
              (AmpReportCache.class, reportCache.getAmpReportId());
          session.delete(ampReportCache);
        }
      }
    }
    catch (Exception e1) {
      logger.error(
          "could not delete/find the physical component report activities");
      e1.printStackTrace(System.out);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception e2) {
        logger.error("Release session failed");
      }
    }
  }

  public static void deleteActivityReportLocation(Collection repLoc) {
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      if (repLoc != null) {
        Iterator repLocItr = repLoc.iterator();
        while (repLocItr.hasNext()) {
          AmpReportLocation repLocTemp = (AmpReportLocation) repLocItr.next();
          AmpReportLocation amprepLoc = (AmpReportLocation) session.load
              (AmpReportLocation.class, repLocTemp.getAmpReportId());
          session.delete(amprepLoc);
        }
      }
    }
    catch (Exception e1) {
      logger.error(
          "could not delete/find the physical component report activities");
      e1.printStackTrace(System.out);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception e2) {
        logger.error("Release session failed");
      }
    }
  }

  public static void deleteActivityReportPhyPerformance(Collection phyPerform) {
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      if (phyPerform != null) {
        Iterator phyPerformItr = phyPerform.iterator();
        while (phyPerformItr.hasNext()) {
          AmpReportPhysicalPerformance repPhyTemp = (
              AmpReportPhysicalPerformance) phyPerformItr.next();
          AmpReportPhysicalPerformance repPhyPerform = (
              AmpReportPhysicalPerformance) session.load
              (AmpReportPhysicalPerformance.class, repPhyTemp.getAmpPpId());
          session.delete(repPhyPerform);
        }
      }
    }
    catch (Exception e1) {
      logger.error(
          "could not delete/find the physical component report activities");
      e1.printStackTrace(System.out);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception e2) {
        logger.error("Release session failed");
      }
    }
  }

  public static void deleteActivityReportSector(Collection repSector) {
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      if (repSector != null) {
        Iterator repSectorItr = repSector.iterator();
        while (repSectorItr.hasNext()) {
          AmpReportSector repSecTemp = (AmpReportSector) repSectorItr.next();
          AmpReportSector ampRepSector = (AmpReportSector) session.load
              (AmpReportSector.class, repSecTemp.getAmpReportId());
          session.delete(ampRepSector);
        }
      }
    }
    catch (Exception e1) {
      logger.error(
          "could not delete/find the physical component report activities");
      e1.printStackTrace(System.out);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception e2) {
        logger.error("Release session failed");
      }
    }
  }

  public static void deleteActivityIndicatorVal(Collection indVal) {
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      Transaction tx = session.beginTransaction();
      if (indVal != null) {
        Iterator indValItr = indVal.iterator();
        while (indValItr.hasNext()) {
          AmpMEIndicatorValue indValue = (AmpMEIndicatorValue) indValItr.next();
          AmpMEIndicatorValue indicatorVal = (AmpMEIndicatorValue) session.load
              (AmpMEIndicatorValue.class, indValue.getAmpMeIndValId());
          session.delete(indicatorVal);
        }
      }
      tx.commit();
      session.flush();
    }
    catch (Exception e1) {
      logger.error(
          "could not delete/find the physical component report activities");
      e1.printStackTrace(System.out);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception e2) {
        logger.error("Release session failed");
      }
    }
  }

  /* functions to DELETE an activity by Admin end here.... */


  public static class ActivityAmounts {
    private Double proposedAmout;
    private Double plannedAmount;
    private Double actualAmount;
    private DecimalFormat df = NpdUtil.getNumberFormatter();

    public void AddPalenned(double amount) {
      if (plannedAmount != null) {
        plannedAmount = new Double(plannedAmount.doubleValue() + amount);
      }
      else {
        plannedAmount = new Double(amount);
      }
    }

    public void AddActual(double amount) {
      if (actualAmount != null) {
        actualAmount = new Double(actualAmount.doubleValue() + amount);
      }
      else {
        actualAmount = new Double(amount);
      }
    }

    public String actualAmount() {
      if (actualAmount == null) {
        return "N/A";
      }
      return df.format(actualAmount);
    }

    public String plannedAmount() {
      if (plannedAmount == null) {
        return "N/A";
      }
      return df.format(plannedAmount);
    }

    public String proposedAmout() {
      if (proposedAmout == null) {
        return "N/A";
      }
      return df.format(proposedAmout);
    }

    public void setProposedAmout(double proposedAmout) {
      this.proposedAmout = new Double(proposedAmout);
    }

    public double getActualAmount() {
      if (actualAmount == null) {
        return 0;
      }
      return actualAmount.doubleValue();
    }

    public double getPlannedAmount() {
      if (plannedAmount == null) {
        return 0;
      }
      return plannedAmount.doubleValue();
    }

    public double getProposedAmout() {
      if (proposedAmout == null) {
        return 0;
      }
      return proposedAmout.doubleValue();
    }

  }

  public static ActivityAmounts getActivityAmmountIn(AmpActivity act,
      String tocode) throws Exception {
    double tempProposed = 0;
    double tempActual = 0;
    double tempPlanned = 0;
    ActivityAmounts result = new ActivityAmounts();

    AmpCategoryValue statusValue = CategoryManagerUtil.
        getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY,act.getCategories());

    if (act != null && statusValue != null) {
      if (statusValue.getValue().equals(Constants.ACTIVITY_STATUS_PROPOSED) &&
          act.getFunAmount() != null) {
        String currencyCode = act.getCurrencyCode();
        //AMP-1403 assume USD if no code is specified
        if (currencyCode == null || currencyCode.trim().equals("")) {
          currencyCode = "USD";
        } //end of AMP-1403
        tempProposed = CurrencyWorker.convert(act.getFunAmount().doubleValue(),currencyCode);
        result.setProposedAmout(tempProposed);
      }
      else {
        Set fundings = act.getFunding();
        if (fundings != null) {
          for (Iterator iter = fundings.iterator(); iter.hasNext(); ) {
            AmpFunding funding = (AmpFunding) iter.next();
            Set details = funding.getFundingDetails();
            if (details != null) {
              for (Iterator detailIterator = details.iterator();detailIterator.hasNext(); ) {
                AmpFundingDetail detail = (AmpFundingDetail) detailIterator.next();
                Integer transType = detail.getTransactionType();

                Double amount = new Double(detail.getTransactionAmount().doubleValue());

                Integer adjastType = detail.getAdjustmentType();

                //AMP-1403 workaround
                String currencyCode = "USD";
                if (detail.getAmpCurrencyId() != null
                    && detail.getAmpCurrencyId().getCurrencyCode() != null
                    &&
                    detail.getAmpCurrencyId().getCurrencyCode().trim().equals("")) {
                  currencyCode = detail.getAmpCurrencyId().getCurrencyCode();
                } //end of AMP-1403

                if (transType != null
                    && transType.intValue() == Constants.COMMITMENT
                    && adjastType != null && amount != null
                    && detail.getAmpCurrencyId() != null) {

                	if (detail.getFixedExchangeRate()!=null && detail.getFixedExchangeRate().doubleValue()!=1d
                			&& tocode!=null && tocode.trim().equals("USD")){
                		//in this case we use fixed exchange rates to convert to USD, see AMP-1821,

                		//convert to USD with fixed rate secified in the FundingDetail
            			double tempAmount = amount.doubleValue()/detail.getFixedExchangeRate().doubleValue();
            			//sett to correct place
                		if (adjastType.intValue() == Constants.ACTUAL) {
                			result.AddActual(tempAmount);
                		}else if (adjastType.intValue() == Constants.PLANNED) {
                			result.AddPalenned(tempAmount);
                		}

                	}else{
                		//calculate in old way

                		double tempAmount = CurrencyWorker.convert(amount.doubleValue(),currencyCode);

            			//sett to correct place
                		if (adjastType.intValue() == Constants.ACTUAL) {
                			result.AddActual(tempAmount);
                		}
                		if (adjastType.intValue() == Constants.PLANNED) {
                			result.AddPalenned(tempAmount);
                		}

                	}

                }
              }
            }
          }
        }
      }

    }
    return result;
  }

  public static List getActivityProgramsByProgramType(Long actId, String settingName) {
                Session session = null;
                List col = new ArrayList();
                try {
                       session = PersistenceManager.getRequestDBSession();
                       String queryString = "select ap from " +AmpActivityProgram.class.getName() +
                       " ap join ap.programSetting s where (ap.activity=:actId) and (s.name=:settingName)";
                       Query qry = session.createQuery(queryString);
                       qry.setLong("actId",actId);
                       qry.setString("settingName",settingName);
                       col = qry.list();
                } catch (Exception e) {
                       logger.error("Unable to get all components");
                       logger.error(e.getMessage());
                }
                return col;
       }
  public static boolean isImplLocationCountry(Long actId) {
                Session session = null;
                boolean flag = false;
                try {
                       session = PersistenceManager.getRequestDBSession();
                       String queryString = "select apl from " +AmpActivityLocation.class.getName() +
                       " apl join apl.location l where (apl.activity=:actId) and (l.ampRegion is not NULL or l.ampZone is not  NULL or l.ampWoreda is not NULL)";
                       Query qry = session.createQuery(queryString);
                       qry.setLong("actId",actId);
                       if(qry.list()!=null&&qry.list().size()>0){
                           flag=true;
                       }
                } catch (Exception e) {
                       logger.error("Unable to get locations");
                       logger.error(e.getMessage());
                }
                return flag;
       }

  public static class HelperAmpActivityNameComparator
        implements Comparator {
        public int compare(Object obj1, Object obj2) {
            AmpActivity act1 = (AmpActivity) obj1;
            AmpActivity act2 = (AmpActivity) obj2;
            return (act1.getName()!=null && act2.getName()!=null)?act1.getName().compareTo(act2.getName()):0; 
        }
    }

  /**
   * Comparator for AmpActivity class.
   * Compears activities by its ID's.
   * AmpActivity is comparable itself, but it is comparable by names,
   * so this class was created to compeare them with ID's
   * @see AmpActivity#compareTo(AmpActivity)
   */
  public static class ActivityIdComparator
      implements Comparator<AmpActivity> {

    public int compare(AmpActivity act1, AmpActivity act2) {
      return act1.getAmpActivityId().compareTo(act2.getAmpActivityId());
    }
  }

  /**
   * Creates map from {@link AmpActivityReferenceDoc} collection
   * where each elements key is the id of {@link AmpCategoryValue} object which is asigned to the element itself
   *
   */
  public static class CategoryIdRefDocMapBuilder implements AmpCollectionUtils.KeyResolver<Long, AmpActivityReferenceDoc>{

	public Long resolveKey(AmpActivityReferenceDoc element) {
		return element.getCategoryValue().getId();
	}

  }
  
  /**
   * generates ampId
   * @param user,actId
   * @return ampId
   * @author dare
   */
  public static String generateAmpId(User user,Long actId) {
		String retValue=null;		
		String globSetting="numeric";// TODO This should come from global settings
		if(globSetting.equals("numeric")){
			retValue=numericAmpId(user,actId);
		}else if(globSetting.equals("text")){
			retValue=combinedAmpId(actId);
		}
		return retValue;
	}
/**
 * combines countryId, current member id and last activityId+1 and makes ampId
 * @param user,actId
 * @return 
 * @author dare
 */
	private static String numericAmpId(User user,Long actId){
		String retVal=null;
		String countryCode=FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY);
		String userId=user.getId().toString();
                Country country=DbUtil.getDgCountry(countryCode);
                String countryId="0";
                if(country!=null){
                    countryId=country.getCountryId().toString();
                }
		
		String lastId=null;
		if(actId!=null){
			 lastId = actId.toString();	
		}		
		retVal=countryId+userId+lastId;
		return retVal;
	}
	
	/**
	 * combines countryIso and last activityId+1 and makes ampId
	 * @param actId
	 * @return 
	 * @author dare
	 */
	private static String combinedAmpId(Long actId){
		String retVal=null;
		String countryCode=FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY);
		String lastId=null;
		if(actId!=null){
			 lastId = actId.toString();	
		}	
		retVal=countryCode.toUpperCase()+"/"+lastId;		
		return retVal;
	}
	
	public static Collection getActivitiesRelatedToAmpTeamMember(Session session, Long ampTeamMemberId) {
		  try {
	            String queryStr	= "SELECT a FROM " + AmpActivity.class.getName() + " a WHERE " +
	            			"(a.activityCreator=:atmId)  OR  (a.updatedBy=:atmId) OR (:atmId IN ELEMENTS(a.member))";
	            Query qry 		= session.createQuery(queryStr);
	            qry.setLong("atmId", ampTeamMemberId);
	            
	            return qry.list();
	            
		  }
		  catch (Exception ex) {
	        ex.printStackTrace();
	        logger.error("There was an error getting all activities related to AmpTeamMember " + ampTeamMemberId);
	        return null;
		  }
	}
	
	public static String collectionToCSV(Collection<AmpActivity> activities) {
		if ( activities == null )
			return null;
		String ret	= "";
		Iterator<AmpActivity> iter	= activities.iterator();
		while ( iter.hasNext() ) {
			AmpActivity activity	= iter.next();
			if ( activity.getName() != null ) {
				ret	+= "'" + activity.getName() + "'" + ", ";
			}
			else
				ret +=  "' '" + ", ";
		}
		return ret.substring(0, ret.length()-2);
		
	}
	
} // End
