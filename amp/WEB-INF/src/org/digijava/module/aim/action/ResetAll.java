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
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.cms.dbentity.CMSContentItem;

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
			   eaForm.setTitle(null);
			   eaForm.setTeam(null);
				eaForm.setDescription(null);
				eaForm.setObjectives(null);
                eaForm.setDocumentSpace(null);
				eaForm.setSelectedOrganizations(null);
				eaForm.setOriginalAppDate(null);
				eaForm.setRevisedAppDate(null);
				eaForm.setOriginalStartDate(null);
				eaForm.setRevisedStartDate(null);
				eaForm.setCurrentCompDate(null);
				eaForm.setProposedCompDate(null);
				eaForm.setRevisedCompDate(null);
				eaForm.setCreatedDate(null);
//				eaForm.setActivityCloseDates(null);
				eaForm.setStatusId(new Long(0));
				eaForm.setStatusReason(null);
				eaForm.setBudget(null);
	    	}
	    	if(eaForm.getStep().equals("2"))
	    	{
	    		eaForm.setLevelId(new Long(0));
	    		eaForm.setImplementationLevel(null);
	    		eaForm.setSelectedLocs(null);
	    		eaForm.setActivitySectors(null);
	    		eaForm.setProgram(null);
	    		eaForm.setProgramDescription(null);
	    	}
	    	if(eaForm.getStep().equals("3"))
	    	{
	    		eaForm.setFundingOrganizations(null);
	    	}
	    	if(eaForm.getStep().equals("4"))
	    	{
	    		eaForm.setSelectedComponents(null);
				eaForm.setIssues(null);
				eaForm.setMeasure(null);
				eaForm.setActor(null);
	    	}
	    	if(eaForm.getStep().equals("5"))
	    	{
	    		eaForm.setDocumentList(null);
	    		eaForm.setLinksList(null);
                eaForm.setManagedDocumentList(null);
	    	}
	    	if(eaForm.getStep().equals("6"))
	    	{
	    		eaForm.setExecutingAgencies(null);
	    		eaForm.setImpAgencies(null);
	    		eaForm.setConAgencies(null);
	    		eaForm.setBenAgencies(null);
	    		eaForm.setContractors(null);
	    	}
	    	if(eaForm.getStep().equals("7"))
	    	{
				eaForm.setDnrCntFirstName(null);
				eaForm.setDnrCntLastName(null);
				eaForm.setDnrCntEmail(null);
				eaForm.setDnrCntTitle(null);
				eaForm.setDnrCntOrganization(null);
				eaForm.setDnrCntPhoneNumber(null);
				eaForm.setDnrCntFaxNumber(null);
				eaForm.setMfdCntFirstName(null);
				eaForm.setMfdCntLastName(null);
				eaForm.setMfdCntEmail(null);
				eaForm.setMfdCntTitle(null);
				eaForm.setMfdCntOrganization(null);
				eaForm.setMfdCntPhoneNumber(null);
				eaForm.setMfdCntFaxNumber(null);
				
	    	}
	    }
		else
	    {
	    	AmpActivity activity = ActivityUtil.getAmpActivity(eaForm.getActivityId());
			if(eaForm.getStep().equals("1"))
		    {
				eaForm.setTitle(activity.getName().trim());
				eaForm.setDescription(activity.getDescription().trim());
				eaForm.setObjectives(activity.getObjective().trim());
				eaForm.setPurpose(activity.getPurpose().trim());
				eaForm.setResults(activity.getResults().trim());
                eaForm.setDocumentSpace(activity.getDocumentSpace().trim());
				eaForm.setCreatedDate(DateConversion.ConvertDateToString(activity.getCreatedDate()));
				eaForm.setOriginalAppDate(DateConversion.ConvertDateToString(activity.getProposedApprovalDate()));
				eaForm.setRevisedAppDate(DateConversion.ConvertDateToString(activity.getActualApprovalDate()));
				eaForm.setOriginalStartDate(DateConversion.ConvertDateToString(activity.getProposedStartDate()));
				eaForm.setRevisedStartDate(DateConversion.ConvertDateToString(activity.getActualStartDate()));
				eaForm.setCurrentCompDate(DateConversion.ConvertDateToString(activity.getActualCompletionDate()));
				eaForm.setDisbursementsDate(DateConversion.ConvertDateToString(activity.getDisbursmentsDate()));
				eaForm.setContractingDate(DateConversion.ConvertDateToString(activity.getContractingDate()));
				eaForm.setStatusReason(activity.getStatusReason());
				AmpCategoryValue ampCategoryValue	= CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, activity.getCategories());
                if (ampCategoryValue != null)
            		eaForm.setStatusId( ampCategoryValue.getId() );
				
				Set orgProjIdsSet = activity.getInternalIds();
				if (orgProjIdsSet != null)
				{
					Iterator projIdItr = orgProjIdsSet.iterator();
					Collection temp = new ArrayList();
					while (projIdItr.hasNext())
					{
						AmpActivityInternalId actIntId = (AmpActivityInternalId) projIdItr.next();
						OrgProjectId projId = new OrgProjectId();
						projId.setAmpOrgId(actIntId.getOrganisation().getAmpOrgId());
						projId.setName(actIntId.getOrganisation().getName());
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
						eaForm.setSelectedOrganizations(orgProjectIds);
					}
				}
			 }
		    if(eaForm.getStep().equals("2"))
		    {
		    	AmpCategoryValue ampCategoryValue	= 
		    					CategoryManagerUtil.getAmpCategoryValueFromListByKey
		    						(CategoryConstants.IMPLEMENTATION_LEVEL_KEY, activity.getCategories() );
				if (ampCategoryValue != null)
					eaForm.setLevelId(ampCategoryValue.getId());
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
					eaForm.setSelectedLocs(locs);
				}
				switch (impLevel)
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
				}

				Collection sectors = activity.getSectors();

				if (sectors != null && sectors.size() > 0)
				{
					Collection activitySectors = new ArrayList();

					Iterator sectItr = sectors.iterator();
					while (sectItr.hasNext())
					{
						//AmpSector sec = (AmpSector) sectItr.next();
						AmpSector sec = ((AmpActivitySector) sectItr.next()).getSectorId();
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
					eaForm.setActivitySectors(activitySectors);
				}
				if (activity.getThemeId() != null)
				{
					eaForm.setProgram(activity.getThemeId().getAmpThemeId());
				}
				eaForm.setProgramDescription(activity.getProgramDescription().trim());
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
					fund.setAmpTermsAssist(ampFunding.getAmpTermsAssistId());
					fund.setFundingId(ampFunding.getAmpFundingId().intValue());
					fund.setOrgFundingId(ampFunding.getFinancingId());
					fund.setModality(ampFunding.getModalityId());
					fund.setConditions(ampFunding.getConditions());
					Collection fundDetails = ampFunding.getFundingDetails();
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
							fundingDetail.setTransactionAmount(CurrencyWorker.convert(fundDet.getTransactionAmount().doubleValue(),1, 1));
							fundingDetail.setTransactionDate(DateConversion.ConvertDateToString(fundDet.getTransactionDate()));
							fundingDetail.setPerspectiveCode(fundDet.getOrgRoleCode());
							if (fundDet.getOrgRoleCode().equals(Constants.DONOR))
								fundingDetail.setPerspectiveName("Donor");
							else if (fundDet.getOrgRoleCode().equals(Constants.MOFED))
								fundingDetail.setPerspectiveName("MOFED");
							else if (fundDet.getOrgRoleCode().equals(Constants.IMPLEMENTING_AGENCY))
								fundingDetail.setPerspectiveName("Implementing Agency");
							fundingDetail.setPerspectiveCode(fundDet.getOrgRoleCode());
							fundingDetail.setTransactionType(fundDet.getTransactionType().intValue());
							fundDetail.add(fundingDetail);
						}
						fund.setFundingDetails(fundDetail);
						funding.add(fund);
					}
					fundOrg.setFundings(funding);
					fundingOrgs.add(fundOrg);
				}
				eaForm.setFundingOrganizations(fundingOrgs);
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
					eaForm.setSelectedComponents(comp);
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
						eaForm.setIssues(list);
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
					eaForm.setDocumentList(docsList);
					eaForm.setLinksList(linksList);
                    Site currentSite = RequestUtils.getSite(request);
                    eaForm.setManagedDocumentList(DocumentUtil.getDocumentsForActivity(currentSite, activity));
//				}
		    }
		    if(eaForm.getStep().equals("6"))
		    {
				eaForm.setExecutingAgencies(new ArrayList());
				eaForm.setImpAgencies(new ArrayList());
				eaForm.setReportingOrgs(new ArrayList());
				Set relOrgs = activity.getOrgrole();
				if (relOrgs != null)
				{
					Iterator relOrgsItr = relOrgs.iterator();
					while (relOrgsItr.hasNext())
					{
						AmpOrgRole orgRole = (AmpOrgRole) relOrgsItr.next();
						if (orgRole.getRole().getRoleCode().equals(
								Constants.EXECUTING_AGENCY) && (!eaForm.getExecutingAgencies().contains(orgRole.getOrganisation()))) 							{
							eaForm.getExecutingAgencies().add(orgRole.getOrganisation());
						}
						else if (orgRole.getRole().getRoleCode().equals(
								Constants.IMPLEMENTING_AGENCY) && (!eaForm.getImpAgencies().contains(orgRole.getOrganisation())))
						{
							eaForm.getImpAgencies().add(orgRole.getOrganisation());
						}
						else if (orgRole.getRole().getRoleCode().equals(
								Constants.BENEFICIARY_AGENCY) && (!eaForm.getBenAgencies().contains(orgRole.getOrganisation())))
						{
							eaForm.getBenAgencies().add(orgRole.getOrganisation());
						}
						else if (orgRole.getRole().getRoleCode().equals(
								Constants.CONTRACTING_AGENCY) && (!eaForm.getConAgencies().contains(orgRole.getOrganisation())))
						{
							eaForm.getConAgencies().add(orgRole.getOrganisation());
						}
						else if (orgRole.getRole().getRoleCode().equals(
								Constants.REPORTING_AGENCY) && (!eaForm.getReportingOrgs().contains(orgRole.getOrganisation())))
						{
							eaForm.getReportingOrgs().add(orgRole.getOrganisation());
						}
					}
					eaForm.setContractors(activity.getContractors().trim());
				}
		    }
		    if(eaForm.getStep().equals("7"))
		    {
				eaForm.setDnrCntFirstName(activity.getContFirstName());
				eaForm.setDnrCntLastName(activity.getContLastName());
				eaForm.setDnrCntEmail(activity.getEmail());
				eaForm.setDnrCntTitle(activity.getDnrCntTitle());
                eaForm.setDnrCntOrganization(activity.getDnrCntOrganization());
                eaForm.setDnrCntPhoneNumber(activity.getDnrCntPhoneNumber());
                eaForm.setDnrCntFaxNumber(activity.getDnrCntFaxNumber());

                eaForm.setMfdCntFirstName(activity.getMofedCntFirstName());
				eaForm.setMfdCntLastName(activity.getMofedCntLastName());
				eaForm.setMfdCntEmail(activity.getMofedCntEmail());
                eaForm.setMfdCntTitle(activity.getMfdCntTitle());
                eaForm.setMfdCntOrganization(activity.getMfdCntOrganization());
                eaForm.setMfdCntPhoneNumber(activity.getMfdCntPhoneNumber());
                eaForm.setMfdCntFaxNumber(activity.getMfdCntFaxNumber());
			}
		}
		return mapping.findForward("forward");
    }
}
