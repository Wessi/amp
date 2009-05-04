package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.ActivityContactInfo;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/*
 * ResetAll.java
 */

/**
 * ResetAll class resets and loads the activity details of the activity when an add or
 * edit is performed on the details of the activity
 */

public class ResetAll extends Action
{

	private static Logger logger = Logger.getLogger(ResetAll.class);
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
    {
		HttpSession session = request.getSession();
		// if user is not logged in, forward him to the home page
		if (session.getAttribute("currentMember") == null)
			return mapping.findForward("index");

		EditActivityForm eaForm = (EditActivityForm) form; // form bean instance
		logger.info("In reset all method");

    	if(!eaForm.isEditAct())
	    {
	    	if(eaForm.getStep().equals("1"))
	    	{
				logger.info("In reset all method");
			    eaForm.getIdentification().setTitle(null);
			    eaForm.getIdentification().setTeam(null);
				eaForm.getIdentification().setDescription(null);
				eaForm.getIdentification().setLessonsLearned(null);
				eaForm.getIdentification().setObjectives(null);
                
				eaForm.getDocuments().setDocumentSpace(null);
				eaForm.getIdentification().setSelectedOrganizations(null);
				eaForm.getPlanning().setOriginalAppDate(null);
				eaForm.getPlanning().setRevisedAppDate(null);
				eaForm.getPlanning().setOriginalStartDate(null);
				eaForm.getPlanning().setRevisedStartDate(null);
				eaForm.getPlanning().setCurrentCompDate(null);
				eaForm.getPlanning().setProposedCompDate(null);
				eaForm.getPlanning().setRevisedCompDate(null);
				eaForm.getIdentification().setCreatedDate(null);
//				eaForm.setActivityCloseDates(null);
				eaForm.getPlanning().setStatusId(new Long(0));
				eaForm.getPlanning().setStatusReason(null);
				eaForm.getIdentification().setBudgetCheckbox("");
				
				/*
				 * tanzania adds
				 */
				eaForm.getIdentification().setFY(null);
				eaForm.getIdentification().setVote(null);
				eaForm.getIdentification().setSubVote(null);
				eaForm.getIdentification().setSubProgram(null);
				eaForm.getIdentification().setProjectCode(null);
				eaForm.getIdentification().setGbsSbs(null);
				eaForm.getIdentification().setGovernmentApprovalProcedures(new Boolean(false));
				eaForm.getIdentification().setJointCriteria(new Boolean(false));
				
				eaForm.getIdentification().setCrisNumber(null);
	    	}
	    	if(eaForm.getStep().equals("2"))
	    	{
	    		eaForm.getCrossIssues().setEqualOpportunity(null);
	    		eaForm.getCrossIssues().setMinorities(null); 
	    		eaForm.getCrossIssues().setEnvironment(null);
	    		eaForm.getLocation().setLevelId(new Long(0));
	    		eaForm.getIdentification().setActivityLevel(new Long(0));
//	    		eaForm.setImplementationLevel(null);
	    		eaForm.getLocation().setImplemLocationLevel( 
	    				CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.IMPLEMENTATION_LOCATION_KEY, new Long(0)).getId() 
	    		);
	    		eaForm.getLocation().setSelectedLocs(null);
	    		eaForm.getSectors().setActivitySectors(null);
	    		eaForm.getPrograms().setProgram(null);
	    		eaForm.getPrograms().setProgramDescription(null);
	    	}
	    	if(eaForm.getStep().equals("3"))
	    	{
	    		eaForm.getFunding().setFundingOrganizations(null);
	    	}
	    	if(eaForm.getStep().equals("4"))
	    	{
	    		eaForm.getComponents().setSelectedComponents(null);
				eaForm.getIssues().setIssues(null);
				eaForm.getIssues().setMeasure(null);
				eaForm.getIssues().setActor(null);
	    	}
	    	if(eaForm.getStep().equals("5"))
	    	{
	    		eaForm.getDocuments().setDocumentList(null);
	    		eaForm.getDocuments().setLinksList(null);
                eaForm.getDocuments().setManagedDocumentList(null);
	    	}
	    	if(eaForm.getStep().equals("6"))
	    	{
	    		eaForm.getAgencies().setExecutingAgencies(null);
	    		eaForm.getAgencies().setImpAgencies(null);
	    		eaForm.getAgencies().setConAgencies(null);
	    		eaForm.getAgencies().setBenAgencies(null);
	    		
	    	}
	    	if(eaForm.getStep().equals("7"))
	    	{	    		
	    		eaForm.getContactInformation().setContacts(null);
	    		eaForm.getContactInformation().setContactType("-1");
	    		eaForm.getContactInformation().setPrimaryContact(null);
	    		eaForm.getContactInformation().setActivityContacts(null);
	    		eaForm.getContactInformation().setMofedContacts(null);
	    		eaForm.getContactInformation().setDonorContacts(null);
	    		eaForm.getContactInformation().setSectorMinistryContacts(null);
	    		eaForm.getContactInformation().setProjCoordinatorContacts(null);
				
	    	}
	    }
		else
	    {
	    	AmpActivity activity = ActivityUtil.getAmpActivity(eaForm.getActivityId());
			if(eaForm.getStep().equals("1"))
		    {
				eaForm.getIdentification().setTitle(activity.getName().trim());
				eaForm.getIdentification().setDescription(activity.getDescription().trim());
				eaForm.getIdentification().setObjectives(activity.getObjective().trim());
				eaForm.getIdentification().setPurpose(activity.getPurpose().trim());
				eaForm.getIdentification().setResults(activity.getResults().trim());
                eaForm.getDocuments().setDocumentSpace(activity.getDocumentSpace().trim());
				eaForm.getIdentification().setCreatedDate(DateConversion.ConvertDateToString(activity.getCreatedDate()));
				eaForm.getPlanning().setOriginalAppDate(DateConversion.ConvertDateToString(activity.getProposedApprovalDate()));
				eaForm.getPlanning().setRevisedAppDate(DateConversion.ConvertDateToString(activity.getActualApprovalDate()));
				eaForm.getPlanning().setOriginalStartDate(DateConversion.ConvertDateToString(activity.getProposedStartDate()));
				eaForm.getPlanning().setRevisedStartDate(DateConversion.ConvertDateToString(activity.getActualStartDate()));
				eaForm.getPlanning().setCurrentCompDate(DateConversion.ConvertDateToString(activity.getActualCompletionDate()));
				eaForm.getPlanning().setDisbursementsDate(DateConversion.ConvertDateToString(activity.getDisbursmentsDate()));
				eaForm.getPlanning().setContractingDate(DateConversion.ConvertDateToString(activity.getContractingDate()));
				eaForm.getPlanning().setStatusReason(activity.getStatusReason());
				AmpCategoryValue ampCategoryValue	= CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, activity.getCategories());
                if (ampCategoryValue != null)
            		eaForm.getPlanning().setStatusId( ampCategoryValue.getId() );
				
				Set orgProjIdsSet = activity.getInternalIds();
				if (orgProjIdsSet != null)
				{
					Iterator projIdItr = orgProjIdsSet.iterator();
					Collection temp = new ArrayList();
					while (projIdItr.hasNext())
					{
						AmpActivityInternalId actIntId = (AmpActivityInternalId) projIdItr.next();
						OrgProjectId projId = new OrgProjectId();
						projId.setId(actIntId.getId());
						projId.setProjectId(actIntId.getInternalId());
						projId.setOrganisation(actIntId.getOrganisation());
						temp.add(projId);
					}
					if (temp != null && temp.size() > 0)
					{
						OrgProjectId orgProjectIds[] = new OrgProjectId[temp.size()];
						Object arr[] = temp.toArray();
						for (int i = 0; i < arr.length; i++)
						{
							orgProjectIds[i] = (OrgProjectId) arr[i];
						}
						eaForm.getIdentification().setSelectedOrganizations(orgProjectIds);
					}
				}
			 }
		    if(eaForm.getStep().equals("2"))
		    {
		    	AmpCategoryValue ampCategoryValue	= 
		    					CategoryManagerUtil.getAmpCategoryValueFromListByKey
		    						(CategoryConstants.IMPLEMENTATION_LEVEL_KEY, activity.getCategories() );
				if (ampCategoryValue != null)
					eaForm.getLocation().setLevelId(ampCategoryValue.getId());
				int impLevel = 0;
				Collection ampLocs = activity.getLocations();
				if (ampLocs != null && ampLocs.size() > 0)
				{
					Collection locs = new TreeSet();
					Iterator locIter = ampLocs.iterator();
					boolean maxLevel = false;
					while (locIter.hasNext())
					{
						AmpLocation loc = (AmpLocation) locIter.next();
						if (!maxLevel)
						{
							if (loc.getAmpWoreda() != null)
							{
								impLevel = 3;
								maxLevel = true;
							}
							else if (loc.getAmpZone() != null && impLevel < 2)
							{
								impLevel = 2;
							}
							else if (loc.getAmpRegion() != null && impLevel < 1)
							{
								impLevel = 1;
							}
						}
						if (loc != null)
						{
							Location location = new Location();
							location.setLocId(loc.getAmpLocationId());
							//changed By Govind
							Collection col1 =FeaturesUtil.getDefaultCountryISO();
				            String ISO= null;
				            Iterator itr1 = col1.iterator();
				            while(itr1.hasNext())
				            {
				            	AmpGlobalSettings ampG = (AmpGlobalSettings)itr1.next();
				            	ISO = ampG.getGlobalSettingsValue();
				            }
				            Country cntry = DbUtil.getDgCountry(ISO);
							//Country cntry = DbUtil.getDgCountry(Constants.COUNTRY_ISO);
							location.setCountryId(cntry.getCountryId());
							location.setCountry(cntry.getCountryName());
							if (loc.getAmpRegion() != null)
							{
								location.setRegion(loc.getAmpRegion().getName());
								location.setRegionId(loc.getAmpRegion().getAmpRegionId());
							}
							if (loc.getAmpZone() != null)
							{
								location.setZone(loc.getAmpZone().getName());
								location.setZoneId(loc.getAmpZone().getAmpZoneId());
							}
							if (loc.getAmpWoreda() != null)
							{
								location.setWoreda(loc.getAmpWoreda().getName());
								location.setWoredaId(loc.getAmpWoreda().getAmpWoredaId());
							}
							locs.add(location);
						}
					}
					eaForm.getLocation().setSelectedLocs(locs);
				}
				/*switch (impLevel)
				{
					case 0:
						eaForm.setImplementationLevel("country");
						break;
					case 1:
						eaForm.setImplementationLevel("region");
						break;
					case 2:
						eaForm.setImplementationLevel("zone");
						break;
					case 3:
						eaForm.setImplementationLevel("woreda");
						break;
					default:
						eaForm.setImplementationLevel("country");
				}*/
				if (impLevel >= 0) {
                	eaForm.getLocation().setImplemLocationLevel( 
                			CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.IMPLEMENTATION_LEVEL_KEY, 
                													new Long(impLevel) ).getId()
                	);
                }
                else
                	eaForm.getLocation().setImplemLocationLevel( 
                			CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.IMPLEMENTATION_LEVEL_KEY, 
									new Long(0) ).getId()
                	);

				Collection sectors = activity.getSectors();

				if (sectors != null && sectors.size() > 0)
				{
					Collection activitySectors = new ArrayList();

					Iterator sectItr = sectors.iterator();
					while (sectItr.hasNext())
					{
                                                 AmpActivitySector ampActSect = (AmpActivitySector) sectItr.next();
						//AmpSector sec = (AmpSector) sectItr.next();
						AmpSector sec = (ampActSect).getSectorId();
						if (sec != null)
						{
							AmpSector parent = null;
							AmpSector subsectorLevel1 = null;
							AmpSector subsectorLevel2 = null;
							if (sec.getParentSectorId() != null)
							{
								if (sec.getParentSectorId().getParentSectorId() != null)
								{
									subsectorLevel2 = sec;
									subsectorLevel1 = sec.getParentSectorId();
									parent = sec.getParentSectorId()
											.getParentSectorId();
								}
								else
								{
									subsectorLevel1 = sec;
									parent = sec.getParentSectorId();
								}
							}
							else
							{
								parent = sec;
							}
							ActivitySector actSect = new ActivitySector();
                                                        actSect.setConfigId(ampActSect.getClassificationConfig().getId());
							if (parent != null)
							{
								actSect.setId(parent.getAmpSectorId());
								actSect.setSectorId(parent.getAmpSectorId());
								actSect.setSectorName(parent.getName());
								if (subsectorLevel1 != null)
								{
									actSect.setSubsectorLevel1Id(subsectorLevel1.getAmpSectorId());
									actSect.setSubsectorLevel1Name(subsectorLevel1.getName());
									if (subsectorLevel2 != null)
									{
										actSect.setSubsectorLevel2Id(subsectorLevel2.getAmpSectorId());
										actSect.setSubsectorLevel2Name(subsectorLevel2.getName());
									}
								}
							}
							activitySectors.add(actSect);
						}
					}
					eaForm.getSectors().setActivitySectors(activitySectors);
				}
				if (activity.getThemeId() != null)
				{
					eaForm.getPrograms().setProgram(activity.getThemeId().getAmpThemeId());
				}
				eaForm.getPrograms().setProgramDescription(activity.getProgramDescription().trim());
		    }
		    if(eaForm.getStep().equals("3"))
		    {
				Collection fundingOrgs = new ArrayList();
				Iterator fundItr = activity.getFunding().iterator();
				while (fundItr.hasNext())
				{
				   AmpFunding ampFunding = (AmpFunding) fundItr.next();
					AmpOrganisation org = ampFunding.getAmpDonorOrgId();
					FundingOrganization fundOrg = new FundingOrganization();
					fundOrg.setAmpOrgId(org.getAmpOrgId());
					fundOrg.setOrgName(org.getName());
					Funding fund = new Funding();
					fund.setFundingId(System.currentTimeMillis());
					//fund.setAmpTermsAssist(ampFunding.getAmpTermsAssistId());
					fund.setTypeOfAssistance( ampFunding.getTypeOfAssistance() );
					fund.setFundingId(ampFunding.getAmpFundingId().intValue());
					fund.setOrgFundingId(ampFunding.getFinancingId());
					//fund.setModality(ampFunding.getModalityId());
					fund.setFinancingInstrument(ampFunding.getFinancingInstrument());
					fund.setConditions(ampFunding.getConditions());
					Collection fundDetails = ampFunding.getFundingDetails();
					Collection mtefProjections=ampFunding.getMtefProjections();
					Collection funding = new ArrayList();
					if (fundDetails != null && fundDetails.size() > 0)
					{
						Iterator fundDetItr = fundDetails.iterator();
						Collection fundDetail = new ArrayList();
						
						while (fundDetItr.hasNext())
						{
							AmpFundingDetail fundDet = (AmpFundingDetail) fundDetItr.next();
							FundingDetail fundingDetail = new FundingDetail();
							int adjType = fundDet.getAdjustmentType().intValue();
							fundingDetail.setAdjustmentType(adjType);
							if (adjType == Constants.PLANNED)
							{
								fundingDetail.setAdjustmentTypeName("Planned");
							}
							else if (adjType == Constants.ACTUAL)
							{
								fundingDetail.setAdjustmentTypeName("Actual");
							}
							if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE)
							{
								fundingDetail.setClassification(fundDet.getExpCategory());
							}
							fundingDetail.setCurrencyCode(fundDet.getAmpCurrencyId().getCurrencyCode());
							fundingDetail.setCurrencyName(fundDet.getAmpCurrencyId().getCountryName());
							fundingDetail.setTransactionAmount(CurrencyWorker.convert(fundDet.getTransactionAmount(),1, 1));
							fundingDetail.setTransactionDate(DateConversion.ConvertDateToString(fundDet.getTransactionDate()));
							fundingDetail.setTransactionType(fundDet.getTransactionType().intValue());
							fundDetail.add(fundingDetail);
						}
						Collection mtefPrj=new ArrayList();
						if(mtefProjections!=null && mtefProjections.size()>0)
						{
							Iterator prjIterator=mtefProjections.iterator();
							
							while (prjIterator.hasNext())
							{
								AmpFundingMTEFProjection projection=(AmpFundingMTEFProjection)prjIterator.next();
								MTEFProjection mtef=new MTEFProjection();
								mtef.setAmount(projection.getAmount().toString());
								mtef.setCurrencyCode(projection.getAmpCurrency().getCurrencyCode());
								mtef.setCurrencyName(projection.getAmpCurrency().getCurrencyName());
								mtef.setProjected( projection.getProjected().getId() );
								mtef.setProjectionDate( DateConversion.ConvertDateToString(projection.getProjectionDate()) );
								mtefPrj.add(mtef);
								
							}
						}
						fund.setMtefProjections(mtefPrj);
						fund.setFundingDetails(fundDetail);
						funding.add(fund);
					}
					fundOrg.setFundings(funding);
					fundingOrgs.add(fundOrg);
				}
				eaForm.getFunding().setFundingOrganizations(fundingOrgs);
		    }
		    if(eaForm.getStep().equals("4"))
		    {
				Collection componets = activity.getComponents();
				logger.debug("Components Size = " + componets.size());
				if (componets != null && componets.size() > 0)
				{
					Collection comp = new ArrayList();
					Iterator compItr = componets.iterator();
					while (compItr.hasNext())
					{
						AmpComponent temp = (AmpComponent) compItr.next();
						Components tempComp = new Components();
						tempComp.setTitle(temp.getTitle());
						//tempComp.setAmount(DecimalToText.ConvertDecimalToText(temp.getAmount().doubleValue()));
						tempComp.setComponentId(temp.getAmpComponentId());
						/*
						if (temp.getCurrency() != null)
							tempComp.setCurrencyCode(temp.getCurrency().getCurrencyCode());
						*/
						tempComp.setDescription(temp.getDescription().trim());
						/*
						tempComp.setReportingDate(DateConversion.ConvertDateToString(temp.getReportingDate()));
						Collection phyProgess = temp.getPhysicalProgress();
						if (phyProgess != null && phyProgess.size() > 0)
						{
							Collection physicalProgress = new ArrayList();
							Iterator phyProgItr = phyProgess.iterator();
							while (phyProgItr.hasNext())
							{
								AmpPhysicalPerformance phyPerf = (AmpPhysicalPerformance) phyProgItr.next();
								PhysicalProgress phyProg = new PhysicalProgress();
								phyProg.setPid(phyPerf.getAmpPpId());
								phyProg.setDescription(phyPerf.getDescription());
								phyProg.setReportingDate(DateConversion.ConvertDateToString(phyPerf.getReportingDate()));
								phyProg.setTitle(phyPerf.getTitle());
								physicalProgress.add(phyProg);
							}
							tempComp.setPhyProgress(physicalProgress);
						}*/
						comp.add(tempComp);
					}
					eaForm.getComponents().setSelectedComponents(comp);
				}
				ArrayList list = new ArrayList();
				Set issues = activity.getIssues();
				if (issues != null && issues.size() > 0)
				{
					Iterator iItr = issues.iterator();
					while (iItr.hasNext())
					{
						AmpIssues ampIssue = (AmpIssues) iItr.next();
						Issues issue = new Issues();
						issue.setId(ampIssue.getAmpIssueId());
						issue.setName(ampIssue.getName());
						ArrayList mList = new ArrayList();
						if (ampIssue.getMeasures() != null && ampIssue.getMeasures().size() > 0)
						{
							Iterator mItr = ampIssue.getMeasures().iterator() ;
							while (mItr.hasNext())
							{
								AmpMeasure ampMeasure = (AmpMeasure) mItr.next();
								Measures measure = new Measures();
								measure.setId(ampMeasure.getAmpMeasureId());
								measure.setName(ampMeasure.getName());
								ArrayList aList = new ArrayList();
								if (ampMeasure.getActors() != null && ampMeasure.getActors().size() > 0)
								{
									Iterator aItr = ampMeasure.getActors().iterator();
									while (aItr.hasNext())
									{
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
						eaForm.getIssues().setIssues(list);
					}
			    }
			 }
		    if(eaForm.getStep().equals("5"))
		    {
				Collection actDocs = activity.getDocuments();
//				if (actDocs != null && actDocs.size() > 0)
//				{
					Collection docsList = new ArrayList();
					Collection linksList = new ArrayList();
					Iterator docItr = actDocs.iterator();
					while (docItr.hasNext())
					{
						CMSContentItem cmsItem = (CMSContentItem) docItr.next();
						if (cmsItem.getIsFile())
						{
							docsList.add(cmsItem);
						}
						else
						{
							linksList.add(cmsItem);
						}
					}
					eaForm.getDocuments().setDocumentList(docsList);
					eaForm.getDocuments().setLinksList(linksList);
                    Site currentSite = RequestUtils.getSite(request);
                    eaForm.getDocuments().setManagedDocumentList(DocumentUtil.getDocumentsForActivity(currentSite, activity));
//				}
		    }
		    if(eaForm.getStep().equals("6"))
		    {
				eaForm.getAgencies().setExecutingAgencies(new ArrayList());
				eaForm.getAgencies().setImpAgencies(new ArrayList());
				eaForm.getAgencies().setReportingOrgs(new ArrayList());
				Set relOrgs = activity.getOrgrole();
				if (relOrgs != null)
				{
					Iterator relOrgsItr = relOrgs.iterator();
					while (relOrgsItr.hasNext())
					{
						AmpOrgRole orgRole = (AmpOrgRole) relOrgsItr.next();
						if (orgRole.getRole().getRoleCode().equals(
								Constants.EXECUTING_AGENCY) && (!eaForm.getAgencies().getExecutingAgencies().contains(orgRole.getOrganisation()))) 							{
							eaForm.getAgencies().getExecutingAgencies().add(orgRole.getOrganisation());
						}
						else if (orgRole.getRole().getRoleCode().equals(
								Constants.IMPLEMENTING_AGENCY) && (!eaForm.getAgencies().getImpAgencies().contains(orgRole.getOrganisation())))
						{
							eaForm.getAgencies().getImpAgencies().add(orgRole.getOrganisation());
						}
						else if (orgRole.getRole().getRoleCode().equals(
								Constants.BENEFICIARY_AGENCY) && (!eaForm.getAgencies().getBenAgencies().contains(orgRole.getOrganisation())))
						{
							eaForm.getAgencies().getBenAgencies().add(orgRole.getOrganisation());
						}
						else if (orgRole.getRole().getRoleCode().equals(
								Constants.CONTRACTING_AGENCY) && (!eaForm.getAgencies().getConAgencies().contains(orgRole.getOrganisation())))
						{
							eaForm.getAgencies().getConAgencies().add(orgRole.getOrganisation());
						}
						else if (orgRole.getRole().getRoleCode().equals(
								Constants.REPORTING_AGENCY) && (!eaForm.getAgencies().getReportingOrgs().contains(orgRole.getOrganisation())))
						{
							eaForm.getAgencies().getReportingOrgs().add(orgRole.getOrganisation());
						}
						else if (orgRole.getRole().getRoleCode().equals(
							Constants.SECTOR_GROUP) && (!eaForm.getAgencies().getSectGroups().contains(orgRole.getOrganisation())))
						{
						    eaForm.getAgencies().getSectGroups().add(orgRole.getOrganisation());
						}					
					else if (orgRole.getRole().getRoleCode().equals(
						Constants.REGIONAL_GROUP) && (!eaForm.getAgencies().getRegGroups().contains(orgRole.getOrganisation())))
				{
					eaForm.getAgencies().getRegGroups().add(orgRole.getOrganisation());
				}
				}
				
				}
		    }
		    if(eaForm.getStep().equals("7"))
		    {
		    	ActivityContactInfo contactInfo=eaForm.getContactInformation();
		    	Set<AmpActivityContact> activityContacts=activity.getActivityContacts();
		    	contactInfo.setActivityContacts(new ArrayList<AmpActivityContact>(activityContacts));
		    	 if(activityContacts!=null && activityContacts.size()>0){
			    	  for (AmpActivityContact ampActContact : activityContacts) {
			    		//donor contact
						if(ampActContact.getContactType().equals(Constants.DONOR_CONTACT)){
							if(contactInfo.getDonorContacts()==null){
								contactInfo.setDonorContacts(new ArrayList<AmpActivityContact>());
							}
							contactInfo.getDonorContacts().add(ampActContact);
						}
						//mofed contact
						else if(ampActContact.getContactType().equals(Constants.MOFED_CONTACT)){
							if(contactInfo.getMofedContacts()==null){
								contactInfo.setMofedContacts(new ArrayList<AmpActivityContact>());
							}
							contactInfo.getMofedContacts().add(ampActContact);
						}
						//project coordinator contact
						else if(ampActContact.getContactType().equals(Constants.PROJECT_COORDINATOR_CONTACT)){
							if(contactInfo.getProjCoordinatorContacts()==null){
								contactInfo.setProjCoordinatorContacts(new ArrayList<AmpActivityContact>());
							}
							contactInfo.getProjCoordinatorContacts().add(ampActContact);
						}
						//sector ministry contact
						else if(ampActContact.getContactType().equals(Constants.SECTOR_MINISTRY_CONTACT)){
							if(contactInfo.getSectorMinistryContacts()==null){
								contactInfo.setSectorMinistryContacts(new ArrayList<AmpActivityContact>());
							}
							contactInfo.getSectorMinistryContacts().add(ampActContact);
						}
					}
		    	 }
			}
		}
		return mapping.findForward("forward");
    }
}
