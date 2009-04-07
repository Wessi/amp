/*
 * ShowActivityPrintPreview.java
 * Created : 24-May-2005
 */

package org.digijava.module.aim.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityComponente;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ShowActivityPrintPreview
    extends Action {

    @SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        EditActivityForm eaForm = (EditActivityForm) form;
        Long actId = eaForm.getActivityId();  
        if(actId==null){
        	actId=new Long(request.getParameter("activityid"));        	
        }
        
             
        if(actId != null) {
            HttpSession session = request.getSession();
            TeamMember tm = (TeamMember) session.getAttribute("currentMember");
            AmpActivity activity = null;
			try {
				activity = ActivityUtil.loadActivity(actId);
			} catch (DgException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if(activity != null){
            	
            	
                //costing
                Collection euActs = EUActivityUtil.getEUActivities(activity.getAmpActivityId());
      	      	request.setAttribute("costs", euActs);
      	        
      	      	request.setAttribute("actId", actId);     	      	
		        int risk = IndicatorUtil.getOverallRisk(actId);
		        String riskName = MEIndicatorsUtil.getRiskRatingName(risk);
		        String rskColor = MEIndicatorsUtil.getRiskColor(risk);
		        request.setAttribute("overallRisk", riskName);
		        request.setAttribute("riskColor", rskColor);
		        
                // set title,description and objective

            	ProposedProjCost pg = new ProposedProjCost();
                if (activity.getFunAmount() != null){
                	pg.setFunAmountAsDouble(activity.getFunAmount().doubleValue());
                }                  
                pg.setCurrencyCode(activity.getCurrencyCode());
                pg.setFunDate(FormatHelper.formatDate(activity.getFunDate()));
                eaForm.getFunding().setProProjCost(pg);


                try {
                    List actPrgs = new ArrayList();                    
                    Collection prgSet = ActivityUtil.getActivityPrograms(activity.getAmpActivityId());
                    if(prgSet != null) {
                        Iterator prgItr = prgSet.iterator();
                        while(prgItr.hasNext()) {
                            actPrgs.add((AmpTheme) prgItr.next());
                        }
                    }
                    eaForm.getPrograms().setActPrograms(actPrgs);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                if(activity.getName()!=null){
                	eaForm.getIdentification().setTitle(activity.getName().trim());
                }
                if(activity.getDescription()!=null){
                	eaForm.getIdentification().setDescription(activity.getDescription().trim());
                }
                if(activity.getLessonsLearned()!=null){
                	eaForm.getIdentification().setLessonsLearned(activity.getLessonsLearned().trim());
                }
                if(activity.getObjective()!=null){
               	 eaForm.getIdentification().setObjectives(activity.getObjective().trim());
               }
                if(activity.getProjectComments()!=null){
               	 eaForm.getIdentification().setProjectComments(activity.getProjectComments().trim());
               }
                // fferreyra: Added null checking for field project_impact
                if(activity.getProjectImpact()!=null){
                	eaForm.getIdentification().setProjectImpact(activity.getProjectImpact().trim());
                }
                
                // fferreyra: Added null checking for field activity_summary
                if(activity.getActivitySummary()!=null){
                	eaForm.getIdentification().setActivitySummary(activity.getActivitySummary().trim());
                }

                // fferreyra: Added null checking for field contracting_arrangements
                if(activity.getContractingArrangements()!=null){
                	eaForm.getIdentification().setContractingArrangements(activity.getContractingArrangements().trim());
                }

                // fferreyra: Added null checking for field cond_seq
                if(activity.getCondSeq()!=null){
                	eaForm.getIdentification().setCondSeq(activity.getCondSeq().trim());
                }
                
                // fferreyra: Added null checking for field linked_activities
                if(activity.getLinkedActivities()!=null){
                	eaForm.getIdentification().setLinkedActivities(activity.getLinkedActivities().trim());
                }
                
                // fferreyra: Added null checking for field conditionality
                if(activity.getConditionality()!=null){
                	eaForm.getIdentification().setConditionality(activity.getConditionality().trim());
                }
                
                // fferreyra: Added null checking for field project_management
                if(activity.getProjectManagement()!=null){
                	eaForm.getIdentification().setProjectManagement(activity.getProjectManagement().trim());
                }
                
                // fferreyra: Added null checking for field contract_details
                if(activity.getContractDetails()!=null){
                	eaForm.getContracts().setContractDetails(activity.getContractDetails().trim());
                }
                
            	
                
                
               
                if(activity.getDocumentSpace() == null ||
                   activity.getDocumentSpace().trim().length() == 0) {
                    if(DocumentUtil.isDMEnabled()) {
                        eaForm.getDocuments().setDocumentSpace("aim-document-space-" +
                                                tm.getMemberId() +
                                                "-" + System.currentTimeMillis());
                        Site currentSite = RequestUtils.getSite(request);
                        DocumentUtil.createDocumentSpace(currentSite,
                            eaForm.getDocuments().getDocumentSpace());
                    }
                } else {
                    eaForm.getDocuments().setDocumentSpace(activity.getDocumentSpace().
                                            trim());
                }
                eaForm.getIdentification().setAmpId(activity.getAmpId());

                eaForm.getIdentification().setStatus(DbUtil.getActivityApprovalStatus(new Long(actId)));
                
                eaForm.getPlanning().setStatusReason(activity.getStatusReason());

                if(null != activity.getLineMinRank())
                    eaForm.getPlanning().setLineMinRank(activity.getLineMinRank().
                                          toString());
                else
                    eaForm.getPlanning().setLineMinRank("-1");
                if(null != activity.getPlanMinRank())
                    eaForm.getPlanning().setPlanMinRank(activity.getPlanMinRank().
                                          toString());
                else
                    eaForm.getPlanning().setPlanMinRank("-1");
                
                eaForm.getPlanning().setActRankCollection(new ArrayList());
                for(int i = 1; i < 6; i++) {
                	eaForm.getPlanning().getActRankCollection().add(new Integer(i));
                }

                eaForm.getIdentification().setCreatedDate(DateConversion
                                      .ConvertDateToString(activity.
                    getCreatedDate()));
                eaForm.getIdentification().setUpdatedDate(DateConversion
                        .ConvertDateToString(activity.
                        		getUpdatedDate()));

                eaForm.getPlanning().setOriginalAppDate(DateConversion
                                          .ConvertDateToString(activity
                    .getProposedApprovalDate()));
                eaForm.getPlanning().setRevisedAppDate(DateConversion
                                         .ConvertDateToString(activity
                    .getActualApprovalDate()));
                eaForm.getPlanning().setOriginalStartDate(DateConversion
                                            .ConvertDateToString(activity
                    .getProposedStartDate()));
                eaForm
                .getPlanning().setRevisedStartDate(DateConversion
                                         .ConvertDateToString(activity
                    .getActualStartDate()));
                eaForm.getPlanning().setCurrentCompDate(DateConversion
                                          .ConvertDateToString(activity
                    .getActualCompletionDate()));
                eaForm.getPlanning().setContractingDate(DateConversion
                        .ConvertDateToString(activity.
                        		getContractingDate()));
                eaForm.getPlanning().setDisbursementsDate(DateConversion
                        .ConvertDateToString(activity.
                        		getDisbursmentsDate()));

                eaForm.getPlanning().setProposedCompDate(DateConversion.ConvertDateToString(activity.getProposedCompletionDate()));
            
                

                Collection col = ActivityUtil.getActivityCloseDates(activity.getAmpActivityId());
                List dates = new ArrayList();
                if(col != null && col.size() > 0) {
                    Iterator itr = col.iterator();
                    while(itr.hasNext()) {
                        AmpActivityClosingDates cDate = (
                            AmpActivityClosingDates) itr
                            .next();
                        if(cDate.getType().intValue() == Constants.REVISED
                           .intValue()) {
                            dates.add(DateConversion
                                      .ConvertDateToString(cDate
                                .getClosingDate()));
                        }
                    }
                }
                if (tm != null && tm.getAppSettings() != null
						&& tm.getAppSettings().getCurrencyId() != null) {
					String currCode = "";
					AmpCurrency curr = CurrencyUtil.getAmpcurrency(tm
							.getAppSettings().getCurrencyId());
					if (curr != null) {
						currCode = curr.getCurrencyCode();
					}
					eaForm.setCurrCode(currCode);
				}

                Collections.sort(dates, DateConversion.dtComp);
                eaForm.getPlanning().setActivityCloseDates(dates);

                // loading organizations and thier project ids.                
                Collection orgProjIdsSet = DbUtil.getActivityInternalId(activity.getAmpActivityId());
                if(orgProjIdsSet != null) {
                    Iterator projIdItr = orgProjIdsSet.iterator();
                    Collection temp = new ArrayList();
                    while(projIdItr.hasNext()) {
                        AmpActivityInternalId actIntId = (
                            AmpActivityInternalId) projIdItr
                            .next();
                        OrgProjectId projId = new OrgProjectId();
                        projId.setId(actIntId.getId());
                         projId.setProjectId(actIntId.getInternalId());
                        temp.add(projId);
                    }
                    if(temp != null && temp.size() > 0) {
                        OrgProjectId orgProjectIds[] = new OrgProjectId[
                            temp
                            .size()];
                        Object arr[] = temp.toArray();
                        for(int i = 0; i < arr.length; i++) {
                            orgProjectIds[i] = (OrgProjectId) arr[i];
                        }
                        eaForm.getIdentification().setSelectedOrganizations(orgProjectIds);
                    }
                }

                // setting status and modality
                AmpCategoryValue ampCategoryValueForStatus	= 
                	CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, activity.getCategories());
                if (ampCategoryValueForStatus != null)
            		eaForm.getPlanning().setStatusId( ampCategoryValueForStatus.getId() );
                
                AmpCategoryValue ampCategoryValueForLevel	= 
                	CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.IMPLEMENTATION_LEVEL_KEY, activity.getCategories());
                if(ampCategoryValueForLevel != null)
                    eaForm.getLocation().setLevelId(ampCategoryValueForLevel.getId());

                // loading the locations
                int impLevel = 0;
                
                Collection<AmpActivityLocation> ampLocs = ActivityUtil.getActivityLocations(activity.getAmpActivityId());

                if (ampLocs != null && ampLocs.size() > 0) {
                    Collection locs = new ArrayList();

                    Iterator locIter = ampLocs.iterator();
                    boolean maxLevel = false;
                    while (locIter.hasNext()) {
                    	AmpActivityLocation actLoc = (AmpActivityLocation) locIter.next();	//AMP-2250
                    	if (actLoc == null)
                    		continue;
                    	AmpLocation loc=actLoc.getLocation();								//AMP-2250
                      if (!maxLevel) {
                        if (loc.getAmpWoreda() != null) {
                          impLevel = 3;
                          maxLevel = true;
                        }
                        else if (loc.getAmpZone() != null
                                 && impLevel < 2) {
                          impLevel = 2;
                        }
                        else if (loc.getAmpRegion() != null
                                 && impLevel < 1) {
                          impLevel = 1;
                        }
                      }

                      if (loc != null) {
                        Location location = new Location();
                        location.setLocId(loc.getAmpLocationId());
                        Collection col1 = FeaturesUtil.getDefaultCountryISO();
                        String ISO = null;
                        Iterator itr1 = col1.iterator();
                        while (itr1.hasNext()) {
                          AmpGlobalSettings ampG = (AmpGlobalSettings) itr1.next();
                          ISO = ampG.getGlobalSettingsValue();
                        }
                        
                        //Country cntry = DbUtil.getDgCountry(Constants.COUNTRY_ISO);
                        Country cntry = DbUtil.getDgCountry(ISO);
                        location.setCountryId(cntry.getCountryId());
                        location.setCountry(cntry.getCountryName());
                        location.setNewCountryId(cntry.getIso());
                        
                        location.setAmpCVLocation( loc.getLocation() );
                        AmpCategoryValueLocations ampCVRegion	= 
                			DynLocationManagerUtil.getAncestorByLayer(loc.getLocation(), CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
						if ( ampCVRegion != null ) {
						    if (eaForm.getFunding().getFundingRegions() == null) {
						      eaForm.getFunding()
						          .setFundingRegions(new ArrayList());
						    }
						    if (!eaForm.getFunding().getFundingRegions().contains(ampCVRegion) ) {
						      eaForm.getFunding().getFundingRegions().add( ampCVRegion );
						}
}
//                        if (loc.getAmpRegion() != null) {
//                          location.setRegion(loc.getAmpRegion()
//                                             .getName());
//                          location.setRegionId(loc.getAmpRegion()
//                                               .getAmpRegionId());
//                          if (eaForm.getFunding().getFundingRegions() == null) {
//                            eaForm.getFunding().setFundingRegions(new ArrayList());
//                          }
//                          if (eaForm.getFunding().getFundingRegions().contains(
//                              loc.getAmpRegion()) == false) {
//                            eaForm.getFunding().getFundingRegions().add(
//                                loc.getAmpRegion());
//                          }
//                        }
//                        if (loc.getAmpZone() != null) {
//                          location
//                              .setZone(loc.getAmpZone().getName());
//                          location.setZoneId(loc.getAmpZone()
//                                             .getAmpZoneId());
//                        }
//                        if (loc.getAmpWoreda() != null) {
//                          location.setWoreda(loc.getAmpWoreda()
//                                             .getName());
//                          location.setWoredaId(loc.getAmpWoreda()
//                                               .getAmpWoredaId());
//                        }

                        if(actLoc.getLocationPercentage()!=null)
                        location.setPercent(FormatHelper.formatNumber( actLoc.getLocationPercentage().doubleValue()));

                        locs.add(location);
                      }
                    }
                    eaForm.getLocation().setSelectedLocs(locs);
                  }

                /*switch(impLevel) {
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
                
                

        		Collection sectors = ActivityUtil.getAmpActivitySectors(activity.getAmpActivityId());

        		if (sectors != null && sectors.size() > 0) {
        			List<ActivitySector> activitySectors = new ArrayList<ActivitySector>();
        			Iterator sectItr = sectors.iterator();
        			while (sectItr.hasNext()) {
        				AmpActivitySector ampActSect = (AmpActivitySector) sectItr.next();
        				if (ampActSect != null) {
        					AmpSector sec = ampActSect.getSectorId();
        					if (sec != null) {
        						AmpSector parent = null;
        						AmpSector subsectorLevel1 = null;
        						AmpSector subsectorLevel2 = null;
        						if (sec.getParentSectorId() != null) {
        							if (sec.getParentSectorId().getParentSectorId() != null) {
        								subsectorLevel2 = sec;
        								subsectorLevel1 = sec.getParentSectorId();
        								parent = sec.getParentSectorId().getParentSectorId();
        							} else {
        								subsectorLevel1 = sec;
        								parent = sec.getParentSectorId();
        							}
        						} else {
        							parent = sec;
        						}
        						ActivitySector actSect = new ActivitySector();
                                                        actSect.setConfigId(ampActSect.getClassificationConfig().getId());
        						if (parent != null) {
        							actSect.setId(parent.getAmpSectorId());
        							String view = FeaturesUtil.getGlobalSettingValue("Allow Multiple Sectors");
        							if (view != null)
        								if (view.equalsIgnoreCase("On")) {
        									actSect.setCount(1);
        								} else {
        									actSect.setCount(2);
        								}

        							actSect.setSectorId(parent.getAmpSectorId());
        							actSect.setSectorName(parent.getName());
        							if (subsectorLevel1 != null) {
        								actSect.setSubsectorLevel1Id(subsectorLevel1.getAmpSectorId());
        								actSect.setSubsectorLevel1Name(subsectorLevel1.getName());
        								if (subsectorLevel2 != null) {
        									actSect.setSubsectorLevel2Id(subsectorLevel2.getAmpSectorId());
        									actSect.setSubsectorLevel2Name(subsectorLevel2.getName());
        								}
        							}
        							actSect.setSectorPercentage(ampActSect.getSectorPercentage());
                                                                actSect.setSectorScheme(parent.getAmpSecSchemeId().getSecSchemeName());
                                                                
        						}
                                                       
        						activitySectors.add(actSect);
        					}
        				}
        			}
        			Collections.sort(activitySectors);
        			eaForm.getSectors().setActivitySectors(activitySectors);
        		}
        		
                if(activity.getThemeId() != null) {
                    eaForm.getPrograms()
                        .setProgram(activity.getThemeId()
                                    .getAmpThemeId());
                }
                if(activity.getProgramDescription()!=null){
                	 eaForm.getPrograms().setProgramDescription(activity.getProgramDescription().trim()); 	
                }
                
                FundingCalculationsHelper calculations=new FundingCalculationsHelper();  
                String toCurrCode=null;
                if (tm != null)
                    toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();

          
                ArrayList fundingOrgs = new ArrayList();
                Iterator fundItr=null;
                List listf = DbUtil.getAmpFunding(activity.getAmpActivityId());
                if(listf!=null) {                	
                	fundItr = listf.iterator();
                    while(fundItr.hasNext()) {
                        AmpFunding ampFunding = (AmpFunding) fundItr.next();
                        AmpOrganisation org = ampFunding.getAmpDonorOrgId();
                        FundingOrganization fundOrg = new FundingOrganization();
                        fundOrg.setAmpOrgId(org.getAmpOrgId());
                        fundOrg.setOrgName(org.getName());
                        int index = fundingOrgs.indexOf(fundOrg);
                        //logger.info("Getting the index as " + index
                        //	+ " for fundorg " + fundOrg.getOrgName());
                        if(index > -1) {
                            fundOrg = (FundingOrganization) fundingOrgs
                                .get(index);
                        }
                        
                        Funding fund = new Funding();
                        //fund.setAmpTermsAssist(ampFunding.getAmpTermsAssistId());
                        fund.setTypeOfAssistance( ampFunding.getTypeOfAssistance() );
                        fund.setFundingId(ampFunding.getAmpFundingId().
                                          longValue());
                        fund.setOrgFundingId(ampFunding.getFinancingId());
                        //fund.setModality(ampFunding.getModalityId());
                        fund.setFinancingInstrument(ampFunding.getFinancingInstrument());
                        fund.setConditions(ampFunding.getConditions());
                        Collection<AmpFundingDetail> fundDetails = ampFunding.getFundingDetails();
                       
                        if(fundDetails != null && fundDetails.size() > 0) {
                          

                            calculations.doCalculations(fundDetails, toCurrCode);
    			            
    			            List<FundingDetail> fundDetail = calculations.getFundDetailList();
	                        Iterator fundingIterator = fundDetail.iterator();
	                         while(fundingIterator.hasNext())
	                         {
	                         	FundingDetail currentFundingDetail = (FundingDetail)fundingIterator.next();
	                         	
	                         	if(currentFundingDetail.getFixedExchangeRate() == null)
	                         	{
	                            	BigDecimal currencyAppliedAmount = getAmountInDefaultCurrency(currentFundingDetail, tm.getAppSettings());

	                            	String currentAmount = FormatHelper.formatNumber(currencyAppliedAmount);
	                            	currentFundingDetail.setTransactionAmount(currentAmount);
	                            	currentFundingDetail.setCurrencyCode(CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId() ).getCurrencyCode());
	                         	}
	                         	else
	                         	{
	                         		Double fixedExchangeRate = currentFundingDetail.getFixedExchangeRate();
	                         		BigDecimal currencyAppliedAmount = CurrencyWorker.convert1(FormatHelper.parseBigDecimal(currentFundingDetail.getTransactionAmount()),fixedExchangeRate,1);
	                            	String currentAmount = FormatHelper.formatNumber(currencyAppliedAmount);
	                            	currentFundingDetail.setTransactionAmount(currentAmount);
	                            	currentFundingDetail.setCurrencyCode(CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId() ).getCurrencyCode());
	                         	}
	                         }
    			            
                            if(fundDetail != null)
                                Collections.sort(fundDetail,
                                                 FundingValidator.dateComp);
                            fund.setFundingDetails(fundDetail);
                            fund.setAmpFundingDetails(fundDetails);
                            eaForm.getFunding().setFundingDetails(fundDetail);
                            // funding.add(fund);
                        }
                        if(fundOrg.getFundings() == null)
                            fundOrg.setFundings(new ArrayList());
                        fundOrg.getFundings().add(fund);

                        if(index > -1) {
                            fundingOrgs.set(index, fundOrg);
                            //	logger
                            //		.info("Setting the fund org obj to the index :"
                            //			+ index);
                        } else {
                            fundingOrgs.add(fundOrg);
                            //	logger.info("Adding new fund org object");
                        }
                    }
                    //Added for the calculation of the subtotal per Organization          
                    Iterator<FundingOrganization> iterFundOrg = fundingOrgs.iterator();
                    while (iterFundOrg.hasNext())
                    {
                  	  FundingOrganization currFundingOrganization = iterFundOrg.next();
                  	  Iterator<Funding> iterFunding = currFundingOrganization.getFundings().iterator();
                  	  while(iterFunding.hasNext()){
                  		  Funding currFunding = iterFunding.next();
                            FundingCalculationsHelper calculationsSubtotal=new FundingCalculationsHelper(); 
                                if(currFunding.getAmpFundingDetails()!=null){
                                calculationsSubtotal.doCalculations(currFunding.getAmpFundingDetails(), toCurrCode);
                                 currFunding.setSubtotalPlannedCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotPlannedComm().doubleValue()));
                  		  currFunding.setSubtotalActualCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotActualComm().doubleValue()));
                  		  currFunding.setSubtotalPlannedDisbursements(FormatHelper.formatNumber(calculationsSubtotal.getTotPlanDisb().doubleValue()));
                  		  currFunding.setSubtotalDisbursements(FormatHelper.formatNumber(calculationsSubtotal.getTotActualDisb().doubleValue()));
                  		  currFunding.setSubtotalPlannedExpenditures(FormatHelper.formatNumber(calculationsSubtotal.getTotPlannedExp().doubleValue()));
                  		  currFunding.setSubtotalExpenditures(FormatHelper.formatNumber(calculationsSubtotal.getTotActualExp().doubleValue()));
                  		  currFunding.setSubtotalActualDisbursementsOrders(FormatHelper.formatNumber(calculationsSubtotal.getTotActualDisbOrder().doubleValue()));
                  		  currFunding.setUnDisbursementBalance(FormatHelper.formatNumber(calculationsSubtotal.getUnDisbursementsBalance().doubleValue()));
                                }
                  		 
                  		  currFunding.setAmpFundingDetails(null);
                  		  //TODO:aca se setearia el resto
                  		  
                  	  }
                    }

                  eaForm.getFunding().setFundingOrganizations(fundingOrgs);
                  eaForm.getFunding().setTotalCommitments(calculations.getTotalCommitments().toString());
              	  eaForm.getFunding().setTotalDisbursements(calculations.getTotActualDisb().toString());
              	  eaForm.getFunding().setTotalExpenditures(calculations.getTotActualExp().toString());
              	  eaForm.getFunding().setTotalActualDisbursementsOrders(calculations.getTotActualDisbOrder().toString());
              	  eaForm.getFunding().setTotalPlannedDisbursements(calculations.getTotPlanDisb().toString());
              	  eaForm.getFunding().setTotalPlannedCommitments(calculations.getTotPlannedComm().toString());
              	  eaForm.getFunding().setTotalPlannedExpenditures(calculations.getTotPlannedExp().toString());
              	  eaForm.getFunding().setTotalPlannedDisbursementsOrders(calculations.getTotPlannedDisbOrder().toString());
              	  eaForm.getFunding().setUnDisbursementsBalance(calculations.getUnDisbursementsBalance().toString());
                }
                

        

                ArrayList regFunds = new ArrayList();
                Iterator rItr=null;
                if(activity.getRegionalFundings()!=null) {
                	rItr = activity.getRegionalFundings().iterator();
                    eaForm.getFunding().setRegionTotalDisb(0);
                    while(rItr.hasNext()) {
                        AmpRegionalFunding ampRegFund = (AmpRegionalFunding)
                            rItr
                            .next();

                        double disb = 0;
                        if(ampRegFund.getAdjustmentType().intValue() == 1 &&
                           ampRegFund.getTransactionType().intValue() == 1)
                            disb = ampRegFund.getTransactionAmount().
                                doubleValue();
               
                        eaForm.getFunding().setRegionTotalDisb(eaForm.getFunding().getRegionTotalDisb() +
                                                  disb);

                        FundingDetail fd = new FundingDetail();
                        fd.setAdjustmentType(ampRegFund.getAdjustmentType()
                                             .intValue());
                        if(fd.getAdjustmentType() == 1) {
                            fd.setAdjustmentTypeName("Actual");
                        } else if(fd.getAdjustmentType() == 0) {
                            fd.setAdjustmentTypeName("Planned");
                        }
                        fd.setCurrencyCode(ampRegFund.getCurrency()
                                           .getCurrencyCode());
                        fd.setCurrencyName(ampRegFund.getCurrency()
                                           .getCurrencyName());
                        fd.setTransactionAmount(DecimalToText
                                                .ConvertDecimalToText(
                                                    ampRegFund
                                                    .getTransactionAmount().doubleValue()));
                        fd.setTransactionDate(DateConversion
                                              .ConvertDateToString(ampRegFund
                            .getTransactionDate()));
                        fd.setTransactionType(ampRegFund.getTransactionType()
                                              .intValue());

                        RegionalFunding regFund = new RegionalFunding();
                        
                        regFund.setRegionId(ampRegFund.getRegionLocation()
                                            .getId());
                        regFund.setRegionName(ampRegFund.getRegionLocation().getName());

                        if(regFunds.contains(regFund) == false) {
                            regFunds.add(regFund);
                        }

                        int index = regFunds.indexOf(regFund);
                        regFund = (RegionalFunding) regFunds.get(index);
                        if(fd.getTransactionType() == 0) { // commitments
                            if(regFund.getCommitments() == null) {
                                regFund.setCommitments(new ArrayList());
                            }
                            regFund.getCommitments().add(fd);
                        } else if(fd.getTransactionType() == 1) { // disbursements
                            if(regFund.getDisbursements() == null) {
                                regFund.setDisbursements(new ArrayList());
                            }
                            regFund.getDisbursements().add(fd);
                        } else if(fd.getTransactionType() == 2) { // expenditures
                            if(regFund.getExpenditures() == null) {
                                regFund.setExpenditures(new ArrayList());
                            }
                            regFund.getExpenditures().add(fd);
                        }
                        regFunds.set(index, regFund);
                    }
                }
                

       

                // Sort the funding details based on Transaction date.
                Iterator itr1 = regFunds.iterator();
                int index = 0;
                while(itr1.hasNext()) {
                    RegionalFunding regFund = (RegionalFunding) itr1.next();
                    List list = null;
                    if(regFund.getCommitments() != null) {
                        list = new ArrayList(regFund.getCommitments());
                        Collections.sort(list, FundingValidator.dateComp);
                    }
                    regFund.setCommitments(list);
                    list = null;
                    if(regFund.getDisbursements() != null) {
                        list = new ArrayList(regFund.getDisbursements());
                        Collections.sort(list, FundingValidator.dateComp);
                    }
                    regFund.setDisbursements(list);
                    list = null;
                    if(regFund.getExpenditures() != null) {
                        list = new ArrayList(regFund.getExpenditures());
                        Collections.sort(list, FundingValidator.dateComp);
                    }
                    regFund.setExpenditures(list);
                    regFunds.set(index++, regFund);
                }

                eaForm.getFunding().setRegionalFundings(regFunds);

                eaForm.getComponents().setSelectedComponents(null);
                eaForm.getComponents().setCompTotalDisb(0);
                
                Collection comp = ActivityUtil.getComponents(activity.getAmpActivityId());
                if (comp != null && comp.size() > 0) {
                	getComponents(comp, activity.getAmpActivityId(), eaForm);
                }
                

                Collection memLinks = TeamMemberUtil.getMemberLinks(tm.getMemberId());
                Collection actDocs = DocumentUtil.getDocumentsForActivity(RequestUtils.getSite(request), activity);
                if(actDocs != null && actDocs.size() > 0) {
                    Collection docsList = new ArrayList();
                    Collection linksList = new ArrayList();

                    Iterator docItr = actDocs.iterator();
                    while(docItr.hasNext()) {
                        RelatedLinks rl = new RelatedLinks();

                        CMSContentItem cmsItem = (CMSContentItem) docItr
                            .next();
                        rl.setRelLink(cmsItem);
                        rl.setMember(TeamMemberUtil.getAmpTeamMember(tm
                            .getMemberId()));
                        Iterator tmpItr = memLinks.iterator();
                        while(tmpItr.hasNext()) {
                            Documents doc = (Documents) tmpItr.next();
                            if(doc.getDocId().longValue() == cmsItem
                               .getId()) {
                                rl.setShowInHomePage(true);
                                break;
                            }
                        }

                        if(cmsItem.getIsFile()) {
                            docsList.add(rl);
                        } else {
                            linksList.add(rl);
                        }
                    }
                    eaForm.getDocuments().setDocumentList(docsList);
                    eaForm.getDocuments().setLinksList(linksList);
                }                
                eaForm.getDocuments().setManagedDocumentList(actDocs);
                // loading the related organizations
                List executingAgencies = new ArrayList();
                List impAgencies = new ArrayList();
                List benAgencies = new ArrayList();
                List conAgencies = new ArrayList();
                List reportingOrgs = new ArrayList();
                List sectGroups = new ArrayList();
                List regGroups = new ArrayList();

                Collection relOrgs = ActivityUtil.getOrgRole(activity.getAmpActivityId());
                if (relOrgs != null) {
                  Iterator relOrgsItr = relOrgs.iterator();
                  AmpOrgRole orgRole = null;
                  AmpRole role = null;
                  AmpOrganisation organisation = null;
                  while (relOrgsItr.hasNext()) {
                	orgRole = (AmpOrgRole) relOrgsItr.next();
                	role = ActivityUtil.getAmpRole(activity.getAmpActivityId(), orgRole.getAmpOrgRoleId());     
                	organisation = ActivityUtil.getAmpOrganisation(activity.getAmpActivityId(), orgRole.getAmpOrgRoleId());
                    if (role.getRoleCode().equals(
                        Constants.EXECUTING_AGENCY)
                        && (!executingAgencies.contains(organisation))) {
                    	executingAgencies.add(organisation);
                    }
                    else if (role.getRoleCode().equals(
                        Constants.IMPLEMENTING_AGENCY)
                             && (!impAgencies.contains(organisation))) {
                    	impAgencies.add(organisation);
                    }
                    else if (role.getRoleCode().equals(
                        Constants.BENEFICIARY_AGENCY)
                             && (!benAgencies.contains(organisation))) {
                      benAgencies.add(organisation);
                    }
                    else if (role.getRoleCode().equals(
                        Constants.CONTRACTING_AGENCY)
                             && (!conAgencies.contains(organisation))) {
                    	conAgencies.add(organisation);
                    }
                    else if (role.getRoleCode().equals(
                        Constants.REPORTING_AGENCY)
                             && (!reportingOrgs.contains(organisation))) {
                    	reportingOrgs.add(organisation);
                    } else if (role.getRoleCode().equals(
                            Constants.SECTOR_GROUP)
                            && (!sectGroups.contains(organisation))) {
                    	sectGroups.add(DbUtil.getOrganisation(orgRole.getOrganisation().getAmpOrgId()));
                    	//sectGroups.add(orgRole.getOrganisation());
                   } else if (role.getRoleCode().equals(
                           Constants.REGIONAL_GROUP)
                           && (!regGroups.contains(organisation))) {
                	   regGroups.add(organisation);
                  }

                  }
                }
                
                eaForm.getAgencies().setExecutingAgencies(executingAgencies);
                eaForm.getAgencies().setImpAgencies(impAgencies);
                eaForm.getAgencies().setBenAgencies(benAgencies);
                eaForm.getAgencies().setConAgencies(conAgencies);
                eaForm.getAgencies().setReportingOrgs(reportingOrgs);
                eaForm.getAgencies().setSectGroups(sectGroups);
                eaForm.getAgencies().setRegGroups(regGroups);
                
                Collection colIssues = ActivityUtil.getAmpIssues(activity.getAmpActivityId());
                if(colIssues != null
                   && colIssues.size() > 0) {
                    ArrayList issueList = new ArrayList();
                    Iterator iItr = colIssues.iterator();
                    while(iItr.hasNext()) {
                        AmpIssues ampIssue = (AmpIssues) iItr.next();
                        Issues issue = new Issues();
                        issue.setId(ampIssue.getAmpIssueId());
                        issue.setName(ampIssue.getName());
                        issue.setIssueDate(FormatHelper.formatDate(ampIssue.getIssueDate()));
                        ArrayList measureList = new ArrayList();
                        if(ampIssue.getMeasures() != null
                           && ampIssue.getMeasures().size() > 0) {
                            Iterator mItr = ampIssue.getMeasures().iterator();
                            while(mItr.hasNext()) {
                                AmpMeasure ampMeasure = (AmpMeasure) mItr.next();
                                Measures measure = new Measures();
                                measure.setId(ampMeasure.getAmpMeasureId());
                                measure.setName(ampMeasure.getName());
                                ArrayList actorList = new ArrayList();
                                if(ampMeasure.getActors() != null
                                   && ampMeasure.getActors().size() > 0) {
                                    Iterator aItr = ampMeasure.getActors().iterator();
                                    while(aItr.hasNext()) {
                                        AmpActor actor = (AmpActor) aItr.next();
                                        actorList.add(actor);
                                    }
                                }
                                measure.setActors(actorList);
                                measureList.add(measure);
                            }
                        }
                        issue.setMeasures(measureList);
                        issueList.add(issue);
                    }
                    eaForm.getIssues().setIssues(issueList);
                } else {
                    eaForm.getIssues().setIssues(null);
                }

                // loading the contact person details and condition
                eaForm.getContactInfo().setDnrCntFirstName(activity.getContFirstName());
                eaForm.getContactInfo().setDnrCntLastName(activity.getContLastName());
                eaForm.getContactInfo().setDnrCntEmail(activity.getEmail());
                eaForm.getContactInfo().setDnrCntTitle(activity.getDnrCntTitle());
                eaForm.getContactInfo().setDnrCntOrganization(activity.getDnrCntOrganization());
                eaForm.getContactInfo().setDnrCntPhoneNumber(activity.getDnrCntPhoneNumber());
                eaForm.getContactInfo().setDnrCntFaxNumber(activity.getDnrCntFaxNumber());
    
                eaForm.getContactInfo().setMfdCntFirstName(activity.getMofedCntFirstName());
                eaForm.getContactInfo().setMfdCntLastName(activity.getMofedCntLastName());
                eaForm.getContactInfo().setMfdCntEmail(activity.getMofedCntEmail());
                eaForm.getContactInfo().setMfdCntTitle(activity.getMfdCntTitle());
                eaForm.getContactInfo().setMfdCntOrganization(activity.getMfdCntOrganization());
                eaForm.getContactInfo().setMfdCntPhoneNumber(activity.getMfdCntPhoneNumber());
                eaForm.getContactInfo().setMfdCntFaxNumber(activity.getMfdCntFaxNumber());
                
                eaForm.getContactInfo().setPrjCoFirstName(activity.getPrjCoFirstName());
                eaForm.getContactInfo().setPrjCoLastName(activity.getPrjCoLastName());
                eaForm.getContactInfo().setPrjCoEmail(activity.getPrjCoEmail());
                eaForm.getContactInfo().setPrjCoTitle(activity.getPrjCoTitle());
                eaForm.getContactInfo().setPrjCoOrganization(activity.getPrjCoOrganization());
                eaForm.getContactInfo().setPrjCoPhoneNumber(activity.getPrjCoPhoneNumber());
                eaForm.getContactInfo().setPrjCoFaxNumber(activity.getPrjCoFaxNumber());
                
                eaForm.getContactInfo().setSecMiCntFirstName(activity.getSecMiCntFirstName());
                eaForm.getContactInfo().setSecMiCntLastName(activity.getSecMiCntLastName());
                eaForm.getContactInfo().setSecMiCntEmail(activity.getSecMiCntEmail());
                eaForm.getContactInfo().setSecMiCntTitle(activity.getSecMiCntTitle());
                eaForm.getContactInfo().setSecMiCntOrganization(activity.getSecMiCntOrganization());
                eaForm.getContactInfo().setSecMiCntPhoneNumber(activity.getSecMiCntPhoneNumber());
                eaForm.getContactInfo().setSecMiCntFaxNumber(activity.getSecMiCntFaxNumber());
                
                if(activity.getCondition()!=null){
                	 eaForm.getIdentification().setConditions(activity.getCondition().trim());
                }
               
                
                AmpCategoryValue ampCategoryValue = CategoryManagerUtil.
                getAmpCategoryValueFromList(CategoryConstants.ACCHAPTER_NAME,
                                            activity.getCategories());
                if (ampCategoryValue != null) {
                	 eaForm.getIdentification().setAcChapter(ampCategoryValue.getId());
                }
                
                ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
                        CategoryConstants.ACCESSION_INSTRUMENT_NAME, activity.getCategories());
                    if (ampCategoryValue != null) {
                    	eaForm.getIdentification().setAccessionInstrument(ampCategoryValue.getId());
                    }
                ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromList(
                        CategoryConstants.PROJECT_CATEGORY_NAME, activity.getCategories());
                    if (ampCategoryValue != null) {
                      	eaForm.getIdentification().setProjectCategory(ampCategoryValue.getId());
                    }
                    
                    
                  //load programs by type
                    if(ProgramUtil.getAmpActivityProgramSettingsList()!=null){
                                 List activityNPO=ActivityUtil.getActivityProgramsByProgramType(activity.getAmpActivityId(),ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
                                 List activityPP=ActivityUtil.getActivityProgramsByProgramType(activity.getAmpActivityId(),ProgramUtil.PRIMARY_PROGRAM);
                                 List activitySP=ActivityUtil.getActivityProgramsByProgramType(activity.getAmpActivityId(),ProgramUtil.SECONDARY_PROGRAM);
                                 eaForm.getPrograms().setNationalPlanObjectivePrograms(activityNPO);
                                 eaForm.getPrograms().setPrimaryPrograms(activityPP);
                                 eaForm.getPrograms().setSecondaryPrograms(activitySP);
                                 eaForm.getPrograms().setNationalSetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE));
                                 eaForm.getPrograms().setPrimarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM));
                                 eaForm.getPrograms().setSecondarySetting(ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM));
                      }
                    
                    Collection<AmpActivityComponente> componentes = ActivityUtil.getAmpActivityComponente(activity.getAmpActivityId());
            		if (componentes != null && componentes.size() > 0) {
            			Collection activitySectors = new ArrayList();
            			Iterator<AmpActivityComponente> sectItr = componentes.iterator();
            			while (sectItr.hasNext()) {
            				AmpActivityComponente ampActSect =  sectItr.next();
            				if (ampActSect != null) {
            					AmpSector sec = ampActSect.getSector();
            					if (sec != null) {
            						AmpSector parent = null;
            						AmpSector subsectorLevel1 = null;
            						AmpSector subsectorLevel2 = null;
            						if (sec.getParentSectorId() != null) {
            							if (sec.getParentSectorId().getParentSectorId() != null) {
            								subsectorLevel2 = sec;
            								subsectorLevel1 = sec.getParentSectorId();
            								parent = sec.getParentSectorId().getParentSectorId();
            							} else {
            								subsectorLevel1 = sec;
            								parent = sec.getParentSectorId();
            							}
              
            						} else {
            							parent = sec;
            						}
            						ActivitySector actCompo = new ActivitySector();
            						if (parent != null) {
            							actCompo.setId(parent.getAmpSectorId());
            							String view = FeaturesUtil.getGlobalSettingValue("Allow Multiple Sectors");
            							if (view != null)
            								if (view.equalsIgnoreCase("On")) {
            									actCompo.setCount(1);
            								} else {
            									actCompo.setCount(2);
            								}

            							actCompo.setSectorId(parent.getAmpSectorId());
            							actCompo.setSectorName(parent.getName());
            							if (subsectorLevel1 != null) {
            								actCompo.setSubsectorLevel1Id(subsectorLevel1.getAmpSectorId());
            								actCompo.setSubsectorLevel1Name(subsectorLevel1.getName());
            								if (subsectorLevel2 != null) {
            									actCompo.setSubsectorLevel2Id(subsectorLevel2.getAmpSectorId());
            									actCompo.setSubsectorLevel2Name(subsectorLevel2.getName());
            								}
            							}
            							actCompo.setSectorPercentage(ampActSect.getPercentage().floatValue());
            						}
            						activitySectors.add(actCompo);
            					}
            				}
            			}

            			eaForm.getComponents().setActivityComponentes(activitySectors);
            		}
            		
            		
            		//get all possible refdoc names from categories
                  	Collection<AmpCategoryValue> catValues=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.REFERENCE_DOCS_KEY,false, request);

                	if (catValues!=null && eaForm.getDocuments().getReferenceDocs()==null){
                    	List<ReferenceDoc> refDocs=new ArrayList<ReferenceDoc>();
                		Collection<AmpActivityReferenceDoc> activityRefDocs=null;
                		Map<Long, AmpActivityReferenceDoc> categoryRefDocMap=null;

                		if (activity.getAmpActivityId()!=null){
                    		//get list of ref docs for activity
                			activityRefDocs=ActivityUtil.getReferenceDocumentsFor(activity.getAmpActivityId());
                        	//create map where keys are category value ids.
                			categoryRefDocMap = AmpCollectionUtils.createMap(
                					activityRefDocs,
                					new ActivityUtil.CategoryIdRefDocMapBuilder());
                		}
                		eaForm.getDocuments().setReferenceDocs(refDocs);
                	}
                		
                		
                //allComments
                		ArrayList<AmpComments> colAux	= null;
                        Collection ampFields 			= DbUtil.getAmpFields();
                        HashMap allComments 			= new HashMap();
                        
                        if (ampFields!=null) {
                        	for (Iterator itAux = ampFields.iterator(); itAux.hasNext(); ) {
                                AmpField field = (AmpField) itAux.next();
                                	colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(),
                                                                      actId);
                                allComments.put(field.getFieldName(), colAux);
                              }
                        }
                        
                        eaForm.getComments().setAllComments(allComments);
                        
                        
                 //purpose and results
                if (activity.getPurpose() != null)
                    eaForm.getIdentification().setPurpose(activity.getPurpose().trim());
                  if (activity.getResults() != null)
                    eaForm.getIdentification().setResults(activity.getResults());
                  

                
                if(activity.getFY()!=null)
                	eaForm.getIdentification().setFY(activity.getFY().trim());
                if(activity.getVote()!=null)
                	eaForm.getIdentification().setVote(activity.getVote().trim());
                if(activity.getSubVote()!=null)
                	eaForm.getIdentification().setSubVote(activity.getSubVote().trim());
                if(activity.getSubProgram()!=null)
                	eaForm.getIdentification().setSubProgram(activity.getSubProgram().trim());
                if(activity.getProjectCode()!=null)
                	eaForm.getIdentification().setProjectCode(activity.getProjectCode().trim());
                
                eaForm.getIdentification().setGbsSbs(activity.getGbsSbs());
                eaForm.getIdentification().setGovernmentApprovalProcedures(activity.isGovernmentApprovalProcedures());
                eaForm.getIdentification().setJointCriteria(activity.isJointCriteria());
                eaForm.getIdentification().setHumanitarianAid(activity.isHumanitarianAid());
                
                if(activity.getCrisNumber()!=null)
                	eaForm.getIdentification().setCrisNumber(activity.getCrisNumber().trim());
                
                if(activity.getUpdatedBy()!=null){
                	eaForm.getIdentification().setUpdatedBy(activity.getUpdatedBy());
                }
                if(activity.getActivityCreator() != null) {
                    User usr = activity.getActivityCreator().getUser();
                    if(usr != null) {
                        eaForm.getIdentification().setActAthFirstName(usr.getFirstNames());
                        eaForm.getIdentification().setActAthLastName(usr.getLastName());
                        eaForm.getIdentification().setActAthEmail(usr.getEmail());
                        eaForm.getIdentification().setActAthAgencySource(usr.getOrganizationName());
                    }
                }
            }else{
                eaForm.getIdentification().setAmpId(null);
                eaForm.getIdentification().setTitle(null);
                eaForm.getIdentification().setObjectives(null);
                eaForm.getIdentification().setProjectComments(null);
                eaForm.getIdentification().setDescription(null);
                eaForm.getSectors().setActivitySectors(null);
//                eaForm.setSectorSchemes(null);
                eaForm.getPrograms().setActPrograms(null);
                eaForm.getPrograms().setProgramCollection(null);
                eaForm.getPlanning().setOriginalAppDate(null);
                eaForm.getPlanning().setRevisedAppDate(null);
                eaForm.getPlanning().setOriginalStartDate(null);
                eaForm.getPlanning().setRevisedStartDate(null);
                eaForm.getPlanning().setCurrentCompDate(null);
                eaForm.getDocuments().setDocumentSpace(null);
                eaForm.getPlanning().setStatusReason(null);
                eaForm.getPlanning().setLineMinRank(null);
                eaForm.getPlanning().setLineMinRank(null);
                eaForm.getPlanning().setPlanMinRank(null);
                eaForm.getPlanning().setPlanMinRank(null);
                eaForm.getIdentification().setCreatedDate(null);
                eaForm.getIdentification().setUpdatedDate(null);
                eaForm.getPlanning().setOriginalAppDate(null);
                eaForm.getPlanning().setRevisedAppDate(null);
                eaForm.getPlanning().setOriginalStartDate(null);
                eaForm.getPlanning().setRevisedStartDate(null);
                eaForm.getPlanning().setCurrentCompDate(null);
                eaForm.getPlanning().setProposedCompDate(null);
                eaForm.getPrograms().setActPrograms(null);
                eaForm.getFunding().setProProjCost(null);
                eaForm.getFunding().setRegionalFundings(null);
                eaForm.getComponents().setSelectedComponents(null);
                eaForm.getComponents().setCompTotalDisb(0);
                eaForm.getContactInfo().setDnrCntFirstName(null);
                eaForm.getContactInfo().setDnrCntLastName(null);
                eaForm.getContactInfo().setDnrCntEmail(null);
                eaForm.getContactInfo().setDnrCntTitle(null);
				eaForm.getContactInfo().setDnrCntOrganization(null);
				eaForm.getContactInfo().setDnrCntPhoneNumber(null);
				eaForm.getContactInfo().setDnrCntFaxNumber(null);
				
                eaForm.getContactInfo().setMfdCntFirstName(null);
                eaForm.getContactInfo().setMfdCntLastName(null);
                eaForm.getContactInfo().setMfdCntEmail(null);
                eaForm.getContactInfo().setMfdCntTitle(null);
				eaForm.getContactInfo().setMfdCntOrganization(null);
				eaForm.getContactInfo().setMfdCntPhoneNumber(null);
				eaForm.getContactInfo().setMfdCntFaxNumber(null);
				
				eaForm.getContactInfo().setPrjCoFirstName(null);
				eaForm.getContactInfo().setPrjCoLastName(null);
				eaForm.getContactInfo().setPrjCoEmail(null);
				eaForm.getContactInfo().setPrjCoTitle(null);
				eaForm.getContactInfo().setPrjCoOrganization(null);
				eaForm.getContactInfo().setPrjCoPhoneNumber(null);
				eaForm.getContactInfo().setPrjCoFaxNumber(null);
				
				eaForm.getContactInfo().setSecMiCntFirstName(null);
				eaForm.getContactInfo().setSecMiCntLastName(null);
				eaForm.getContactInfo().setSecMiCntEmail(null);
				eaForm.getContactInfo().setSecMiCntTitle(null);
				eaForm.getContactInfo().setSecMiCntOrganization(null);
				eaForm.getContactInfo().setSecMiCntPhoneNumber(null);
				eaForm.getContactInfo().setSecMiCntFaxNumber(null);
				
                eaForm.getIdentification().setConditions(null);
                eaForm.getIdentification().setActAthFirstName(null);
                eaForm.getIdentification().setActAthLastName(null);
                eaForm.getIdentification().setActAthEmail(null);
                eaForm.getIdentification().setActAthAgencySource(null);
                eaForm.getIdentification().setUpdatedBy(null);
                
                eaForm.getIdentification().setAccessionInstrument(null);
                eaForm.getIdentification().setAcChapter(null);
                
                /*
				 * tanzania adds
				 */
				eaForm.getIdentification().setFY(null);
				eaForm.getIdentification().setVote(null);
				eaForm.getIdentification().setSubVote(null);
				eaForm.getIdentification().setSubProgram(null);
				eaForm.getIdentification().setProjectCode(null);
				eaForm.getIdentification().setGbsSbs(null);
				
				eaForm.getIdentification().setGovernmentApprovalProcedures(false);
				eaForm.getIdentification().setJointCriteria(false);               
                eaForm.getIdentification().setCrisNumber(null);
            
     }
    }
        return mapping.findForward("forward");
    }

	private void getComponents(Collection componets, Long actId, EditActivityForm eaForm) {
		List<Components<FundingDetail>> selectedComponents = new ArrayList<Components<FundingDetail>>();
		Iterator compItr = componets.iterator();
		while (compItr.hasNext()) {
			AmpComponent temp = (AmpComponent) compItr.next();
			Components<FundingDetail> tempComp = new Components<FundingDetail>();
			tempComp.setTitle(temp.getTitle());
			tempComp.setComponentId(temp.getAmpComponentId());
			tempComp.setType_Id((temp.getType()!=null)?temp.getType().getType_id():null);
			
			if (temp.getDescription() == null) {
				tempComp.setDescription(" ");
			} else {
				tempComp.setDescription(temp.getDescription().trim());
			}
			tempComp.setCode(temp.getCode());
			tempComp.setUrl(temp.getUrl());

			tempComp.setCommitments(new ArrayList<FundingDetail>());
			tempComp.setDisbursements(new ArrayList<FundingDetail>());
			tempComp.setExpenditures(new ArrayList<FundingDetail>());


			Collection<AmpComponentFunding> fundingComponentActivity = ActivityUtil.getFundingComponentActivity(
					tempComp.getComponentId(), actId);
			Iterator cItr = fundingComponentActivity.iterator();
			while (cItr.hasNext()) {
				AmpComponentFunding ampCompFund = (AmpComponentFunding) cItr
						.next();

				double disb = 0;
				if (ampCompFund.getAdjustmentType().intValue() == 1
					&& ampCompFund.getTransactionType().intValue() == 1)
					disb = ampCompFund.getTransactionAmount().doubleValue();

				eaForm.getComponents().setCompTotalDisb(eaForm.getComponents().getCompTotalDisb() + disb);
				FundingDetail fd = new FundingDetail();
				fd.setAdjustmentType(ampCompFund.getAdjustmentType().intValue());
				if (fd.getAdjustmentType() == 1) {
					fd.setAdjustmentTypeName("Actual");
				} else if (fd.getAdjustmentType() == 0) {
					fd.setAdjustmentTypeName("Planned");
				}
				fd.setAmpComponentFundingId(ampCompFund.getAmpComponentFundingId());
				fd.setCurrencyCode(ampCompFund.getCurrency().getCurrencyCode());
				fd.setCurrencyName(ampCompFund.getCurrency().getCurrencyName());
				fd.setTransactionAmount(FormatHelper.formatNumber(ampCompFund.getTransactionAmount().doubleValue()));
				fd.setTransactionDate(DateConversion.ConvertDateToString(ampCompFund.getTransactionDate()));
				fd.setTransactionType(ampCompFund.getTransactionType().intValue());
				if (fd.getTransactionType() == 0) {
					tempComp.getCommitments().add(fd);
				} else if (fd.getTransactionType() == 1) {
					tempComp.getDisbursements().add(fd);
				} else if (fd.getTransactionType() == 2) {
					tempComp.getExpenditures().add(fd);
				}
			}

			ComponentsUtil.calculateFinanceByYearInfo(tempComp,fundingComponentActivity);

			Collection<AmpPhysicalPerformance> phyProgress = ActivityUtil
						.getPhysicalProgressComponentActivity(tempComp.getComponentId(), actId);

			if (phyProgress != null && phyProgress.size() > 0) {
				Collection physicalProgress = new ArrayList();
				Iterator phyProgItr = phyProgress.iterator();
				while (phyProgItr.hasNext()) {
					AmpPhysicalPerformance phyPerf = (AmpPhysicalPerformance) phyProgItr
							.next();
					PhysicalProgress phyProg = new PhysicalProgress();
					phyProg.setPid(phyPerf.getAmpPpId());
					phyProg.setDescription(phyPerf.getDescription());
					phyProg.setReportingDate(DateConversion
							.ConvertDateToString(phyPerf.getReportingDate()));
					phyProg.setTitle(phyPerf.getTitle());
					physicalProgress.add(phyProg);
				}
				tempComp.setPhyProgress(physicalProgress);
			}

			selectedComponents.add(tempComp);
		}


		// Sort the funding details based on Transaction date.
		Iterator compIterator = selectedComponents.iterator();
		int index = 0;
		while (compIterator.hasNext()) {
			Components components = (Components) compIterator.next();
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
			selectedComponents.set(index++, components);
		}

		eaForm.getComponents().setSelectedComponents(selectedComponents);
	}

	private BigDecimal getAmountInDefaultCurrency(FundingDetail fundDet, ApplicationSettings appSet) {
		
		java.sql.Date dt = new java.sql.Date(DateConversion.getDate(fundDet.getTransactionDate()).getTime());
		double frmExRt = Util.getExchange(fundDet.getCurrencyCode(),dt);
		String toCurrCode = CurrencyUtil.getAmpcurrency( appSet.getCurrencyId() ).getCurrencyCode();
		double toExRt = Util.getExchange(toCurrCode,dt);
	
		BigDecimal amt = CurrencyWorker.convert1(FormatHelper.parseBigDecimal(fundDet.getTransactionAmount()),frmExRt,toExRt);
		
		return amt;
		
	}

}
  


