/*
 * EditActivity.java 
 * Created: Feb 10, 2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpModality;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.cms.dbentity.CMSContentItem;

/**
 * Loads the activity details of the activity specified in the form bean
 * variable 'activityId' to the EditActivityForm bean instance
 * 
 * @author Priyajith
 */
public class EditActivity extends Action {

	private ServletContext ampContext = null;

	private static Logger logger = Logger.getLogger(EditActivity.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");

		ampContext = getServlet().getServletContext();

		// if user is not logged in, forward him to the home page
		if (session.getAttribute("currentMember") == null)
			return mapping.findForward("index");

		EditActivityForm eaForm = (EditActivityForm) form; // form bean
		// instance

		try {

			// Checking whether the activity is already opened for editing
			// by some other user

			Long activityId = eaForm.getActivityId();

			eaForm.setReset(true);
			eaForm.setOrgSelReset(true);
			eaForm.setSectorReset(true);
			eaForm.setLocationReset(true);
			eaForm.setPhyProgReset(true);
			eaForm.setDocReset(true);
			eaForm.setComponentReset(true);
			eaForm.reset(mapping, request);

			eaForm.setActivityId(activityId);
			HashMap activityMap = (HashMap) ampContext
					.getAttribute(Constants.EDIT_ACT_LIST);
			
			boolean canEdit = true;

			eaForm.setActivityId(activityId);

			if (tm.getTeamType()
					.equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) {
				eaForm.setDonorFlag(true);
			} else {
				eaForm.setDonorFlag(false);
			}

			if (activityMap != null && activityMap.containsValue(activityId)) {
			    //logger.info("activity is in activityMap " + activityId);
				// The activity is already opened for editing
				synchronized (ampContext) {
					HashMap tsaMap = (HashMap) ampContext
							.getAttribute(Constants.TS_ACT_LIST);
					if (tsaMap != null) {
						Long timeStamp = (Long) tsaMap.get(activityId);
						if (timeStamp != null) {
							
							if ((System.currentTimeMillis() - timeStamp
									.longValue()) > Constants.MAX_TIME_LIMIT) {
								// time limit has execeeded. invalidate the activity references
								tsaMap.remove(activityId);
								HashMap userActList = (HashMap) ampContext
									.getAttribute(Constants.USER_ACT_LIST);								
								Iterator itr = userActList.keySet().iterator();
								while (itr.hasNext()) {
									Long userId = (Long) itr.next();
									Long actId = (Long) userActList.get(userId);
									if (actId.longValue() == activityId
											.longValue()) {
										userActList.remove(userId);
										break;
									}
								}
								itr = activityMap.keySet().iterator();
								String sessId = null;
								while (itr.hasNext()) {
									sessId = (String) itr.next();
									Long actId = (Long) activityMap.get(sessId);
									if (actId.longValue() == activityId.longValue()) {
										activityMap.remove(sessId);
										break;
									}									
								}
								ArrayList sessList = (ArrayList) ampContext
										.getAttribute(Constants.SESSION_LIST);
								sessList.remove(sessId);
								Collections.sort(sessList);
								
								ampContext.setAttribute(Constants.EDIT_ACT_LIST,activityMap);
								ampContext.setAttribute(Constants.USER_ACT_LIST,userActList);
								ampContext.setAttribute(Constants.SESSION_LIST,sessList);
								ampContext.setAttribute(Constants.TS_ACT_LIST,tsaMap);
								
							} else
								canEdit = false;
						} else
							canEdit = false;
					} else
						canEdit = false;
				}
			}


			//logger.info("CanEdit = " + canEdit);
			if (!canEdit) {
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.aim.activityAlreadyOpenedForEdit"));
				saveErrors(request, errors);

				String url = "/aim/viewChannelOverview.do?ampActivityId="
						+ activityId + "&tabIndex=0";
				RequestDispatcher rd = getServlet().getServletContext()
						.getRequestDispatcher(url);
				rd.forward(request, response);
			} else {
			   // logger.info("Path = " + mapping.getPath());
			    if (!mapping.getPath().trim().endsWith("viewActivityPreview")) {
					// Edit the activity
			        //logger.info("mapping does not end with viewActivityPreview.do");
					String sessId = session.getId();
					synchronized (ampContext) {				
						ArrayList sessList = (ArrayList) ampContext.getAttribute(Constants.SESSION_LIST);
						HashMap userActList = (HashMap) ampContext.getAttribute(Constants.USER_ACT_LIST);

						HashMap tsaList = (HashMap) ampContext.getAttribute(Constants.TS_ACT_LIST);
						
						
						if (sessList == null) {
							sessList = new ArrayList();
						}
						if (userActList == null) {
							userActList = new HashMap();
						}
						if (activityMap == null) {
							activityMap = new HashMap();
						}
						if (tsaList == null) {
							tsaList = new HashMap();
						}

						sessList.add(sessId);
						Collections.sort(sessList);
						activityMap.put(sessId, activityId);
						userActList.put(tm.getMemberId(), activityId);
						tsaList.put(activityId,new Long(System.currentTimeMillis()));
						
						ampContext.setAttribute(Constants.SESSION_LIST, sessList);
						ampContext.setAttribute(Constants.EDIT_ACT_LIST,
								activityMap);
						ampContext.setAttribute(Constants.USER_ACT_LIST,
								userActList);
						ampContext.setAttribute(Constants.TS_ACT_LIST,tsaList);			        
					}
					eaForm.setEditAct(true);    
				} else {
				    //logger.info("mapping does end with viewActivityPreview.do");				    
				}
			}


			// Clearing comment properties
			String action = request.getParameter("action");
			// logger.debug("action [inside EditActivity] : " + action);
			if (action != null && action.trim().length() != 0) {
				if ("edit".equals(action)) {
					eaForm.getCommentsCol().clear();
					eaForm.setCommentFlag(false);
				}
			}

			if (eaForm.isDonorFlag()) {
				eaForm.setStep("3");
				eaForm.setFundDonor(TeamMemberUtil.getFundOrgOfUser(tm.getMemberId()));
			} else {
				eaForm.setStep("1");
			}
			eaForm.setReset(false);
			eaForm.setPerspectives(DbUtil.getAmpPerspective());
			
			// load the activity details
			if (activityId != null) {
				AmpActivity activity = ActivityUtil.getAmpActivity(activityId);
				
				String actApprovalStatus = DbUtil.getActivityApprovalStatus(activityId);
				eaForm.setApprovalStatus(actApprovalStatus);
				
				if (activity != null) {
					// set title,description and objective
					eaForm.setTitle(activity.getName().trim());
					eaForm.setDescription(activity.getDescription().trim());
					eaForm.setObjectives(activity.getObjective().trim());
					eaForm.setAmpId(activity.getAmpId());
					eaForm.setStatusReason(activity.getStatusReason());

					eaForm.setCreatedDate(DateConversion
							.ConvertDateToString(activity.getCreatedDate()));

					eaForm.setOriginalAppDate(DateConversion
							.ConvertDateToString(activity
									.getProposedApprovalDate()));
					eaForm.setRevisedAppDate(DateConversion
							.ConvertDateToString(activity
									.getActualApprovalDate()));
					eaForm.setOriginalStartDate(DateConversion
							.ConvertDateToString(activity
									.getProposedStartDate()));
					eaForm
							.setRevisedStartDate(DateConversion
									.ConvertDateToString(activity
											.getActualStartDate()));
					eaForm.setCurrentCompDate(DateConversion
							.ConvertDateToString(activity
									.getActualCompletionDate()));

					eaForm.setContractors(activity.getContractors().trim());

					Collection col = activity.getClosingDates();
					List dates = new ArrayList();
					if (col != null && col.size() > 0) {
						Iterator itr = col.iterator();
						while (itr.hasNext()) {
							AmpActivityClosingDates cDate = (AmpActivityClosingDates) itr
									.next();
							if (cDate.getType().intValue() == Constants.REVISED
									.intValue()) {
								dates.add(DateConversion
										.ConvertDateToString(cDate
												.getClosingDate()));
							}
						}
					}

					Collections.sort(dates, DateConversion.dtComp);
					eaForm.setActivityCloseDates(dates);

					// loading organizations and thier project ids.
					Set orgProjIdsSet = activity.getInternalIds();
					if (orgProjIdsSet != null) {
						Iterator projIdItr = orgProjIdsSet.iterator();
						Collection temp = new ArrayList();
						while (projIdItr.hasNext()) {
							AmpActivityInternalId actIntId = (AmpActivityInternalId) projIdItr
									.next();
							OrgProjectId projId = new OrgProjectId();
							projId.setAmpOrgId(actIntId.getOrganisation()
									.getAmpOrgId());
							projId
									.setName(actIntId.getOrganisation()
											.getName());
							projId.setProjectId(actIntId.getInternalId());
							temp.add(projId);
						}
						if (temp != null && temp.size() > 0) {
							OrgProjectId orgProjectIds[] = new OrgProjectId[temp
									.size()];
							Object arr[] = temp.toArray();
							for (int i = 0; i < arr.length; i++) {
								orgProjectIds[i] = (OrgProjectId) arr[i];
							}
							eaForm.setSelectedOrganizations(orgProjectIds);
						}
					}

					// setting status and modality
					if (activity.getStatus() != null)
						eaForm.setStatus(activity.getStatus().getAmpStatusId());
					if (activity.getLevel() != null)
						eaForm.setLevel(activity.getLevel().getAmpLevelId());

					// loading the locations
					int impLevel = 0;

					Collection ampLocs = activity.getLocations();

					if (ampLocs != null && ampLocs.size() > 0) {
						Collection locs = new ArrayList();

						Iterator locIter = ampLocs.iterator();
						boolean maxLevel = false;
						while (locIter.hasNext()) {
							AmpLocation loc = (AmpLocation) locIter.next();
							if (!maxLevel) {
								if (loc.getAmpWoreda() != null) {
									impLevel = 3;
									maxLevel = true;
								} else if (loc.getAmpZone() != null
										&& impLevel < 2) {
									impLevel = 2;
								} else if (loc.getAmpRegion() != null
										&& impLevel < 1) {
									impLevel = 1;
								}
							}

							if (loc != null) {
								Location location = new Location();
								location.setLocId(loc.getAmpLocationId());
								Country cntry = DbUtil
										.getDgCountry(Constants.COUNTRY_ISO);
								location.setCountryId(cntry.getCountryId());
								location.setCountry(cntry.getCountryName());
								if (loc.getAmpRegion() != null) {
									location.setRegion(loc.getAmpRegion()
											.getName());
									location.setRegionId(loc.getAmpRegion()
											.getAmpRegionId());
									if (eaForm.getFundingRegions() == null) {
										eaForm
												.setFundingRegions(new ArrayList());
									}
									if (eaForm.getFundingRegions().contains(
											loc.getAmpRegion()) == false) {
										eaForm.getFundingRegions().add(
												loc.getAmpRegion());
									}
								}
								if (loc.getAmpZone() != null) {
									location
											.setZone(loc.getAmpZone().getName());
									location.setZoneId(loc.getAmpZone()
											.getAmpZoneId());
								}
								if (loc.getAmpWoreda() != null) {
									location.setWoreda(loc.getAmpWoreda()
											.getName());
									location.setWoredaId(loc.getAmpWoreda()
											.getAmpWoredaId());
								}
								locs.add(location);
							}
						}
						eaForm.setSelectedLocs(locs);
					}

					switch (impLevel) {
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

					if (sectors != null && sectors.size() > 0) {
						Collection activitySectors = new ArrayList();

						Iterator sectItr = sectors.iterator();
						while (sectItr.hasNext()) {
							AmpSector sec = (AmpSector) sectItr.next();
							if (sec != null) {
								AmpSector parent = null;
								AmpSector subsectorLevel1 = null;
								AmpSector subsectorLevel2 = null;
								if (sec.getParentSectorId() != null) {
									if (sec.getParentSectorId()
											.getParentSectorId() != null) {
										subsectorLevel2 = sec;
										subsectorLevel1 = sec
												.getParentSectorId();
										parent = sec.getParentSectorId()
												.getParentSectorId();
									} else {
										subsectorLevel1 = sec;
										parent = sec.getParentSectorId();
									}
								} else {
									parent = sec;
								}
								ActivitySector actSect = new ActivitySector();
								if (parent != null) {
									actSect.setId(parent.getAmpSectorId());
									actSect
											.setSectorId(parent
													.getAmpSectorId());
									actSect.setSectorName(parent.getName());
									if (subsectorLevel1 != null) {
										actSect
												.setSubsectorLevel1Id(subsectorLevel1
														.getAmpSectorId());
										actSect
												.setSubsectorLevel1Name(subsectorLevel1
														.getName());
										if (subsectorLevel2 != null) {
											actSect
													.setSubsectorLevel2Id(subsectorLevel2
															.getAmpSectorId());
											actSect
													.setSubsectorLevel2Name(subsectorLevel2
															.getName());
										}
									}
								}
								activitySectors.add(actSect);
							}
						}
						eaForm.setActivitySectors(activitySectors);
					}

					if (activity.getThemeId() != null) {
						eaForm
								.setProgram(activity.getThemeId()
										.getAmpThemeId());
					}
					eaForm.setProgramDescription(activity
							.getProgramDescription().trim());

					double totComm = 0;
					double totDisb = 0;
					double totExp = 0;

					ArrayList fundingOrgs = new ArrayList();
					Iterator fundItr = activity.getFunding().iterator();
					while (fundItr.hasNext()) {
						AmpFunding ampFunding = (AmpFunding) fundItr.next();
						AmpOrganisation org = ampFunding.getAmpDonorOrgId();
						FundingOrganization fundOrg = new FundingOrganization();
						fundOrg.setAmpOrgId(org.getAmpOrgId());
						fundOrg.setOrgName(org.getName());
						int index = fundingOrgs.indexOf(fundOrg);
						//logger.info("Getting the index as " + index
							//	+ " for fundorg " + fundOrg.getOrgName());
						if (index > -1) {
							fundOrg = (FundingOrganization) fundingOrgs
									.get(index);
						}

						Funding fund = new Funding();
						fund.setAmpTermsAssist(ampFunding.getAmpTermsAssistId());
						fund.setFundingId(ampFunding.getAmpFundingId().longValue());
						fund.setOrgFundingId(ampFunding.getFinancingId());
						fund.setModality(ampFunding.getModalityId());
						fund.setConditions(ampFunding.getConditions());
						Collection fundDetails = ampFunding.getFundingDetails();
						if (fundDetails != null && fundDetails.size() > 0) {
							Iterator fundDetItr = fundDetails.iterator();
							List fundDetail = new ArrayList();

							while (fundDetItr.hasNext()) {
								AmpFundingDetail fundDet = (AmpFundingDetail) fundDetItr
										.next();
								FundingDetail fundingDetail = new FundingDetail();
								fundingDetail.setIndexId(System.currentTimeMillis());
								int adjType = fundDet.getAdjustmentType()
										.intValue();
								fundingDetail.setAdjustmentType(adjType);
								if (adjType == Constants.PLANNED) {
									fundingDetail
											.setAdjustmentTypeName("Planned");
								} else if (adjType == Constants.ACTUAL) {
									fundingDetail
											.setAdjustmentTypeName("Actual");
									Date dt = fundDet.getTransactionDate();
									double frmExRt = DbUtil.getExchangeRate(
											fundDet.getAmpCurrencyId()
													.getCurrencyCode(), 1, dt);
									double toExRt = DbUtil.getExchangeRate(
											DbUtil.getAmpcurrency(
													tm.getAppSettings()
															.getCurrencyId())
													.getCurrencyCode(), 1, dt);
									double amt = CurrencyWorker.convert1(
											fundDet.getTransactionAmount()
													.doubleValue(), frmExRt,
											toExRt);
									if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
										totComm += amt;
									} else if (fundDet.getTransactionType()
											.intValue() == Constants.DISBURSEMENT) {
										totDisb += amt;
									} else if (fundDet.getTransactionType()
											.intValue() == Constants.EXPENDITURE) {
										totExp += amt;
									}
								}
								if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
									fundingDetail.setClassification(fundDet
											.getExpCategory());
								}
								fundingDetail.setCurrencyCode(fundDet
										.getAmpCurrencyId().getCurrencyCode());
								fundingDetail.setCurrencyName(fundDet
										.getAmpCurrencyId().getCountryName());

								fundingDetail
										.setTransactionAmount(CurrencyWorker
												.convert(fundDet
														.getTransactionAmount()
														.doubleValue(), 1, 1));
								fundingDetail.setTransactionDate(DateConversion
										.ConvertDateToString(fundDet
												.getTransactionDate()));
								
								fundingDetail.setPerspectiveCode(fundDet.getPerspectiveId().getCode());
								fundingDetail.setPerspectiveName(fundDet.getPerspectiveId().getName());
								
								/*
								fundingDetail.setPerspectiveCode(fundDet
										.getOrgRoleCode());

								Iterator itr1 = eaForm.getPerspectives()
										.iterator();
								while (itr1.hasNext()) {
									AmpPerspective pers = (AmpPerspective) itr1
											.next();
									if (pers.getCode().equals(
											fundDet.getOrgRoleCode())) {
										fundingDetail.setPerspectiveName(pers
												.getName());
									}
								}
								*/
								
								

								fundingDetail.setTransactionType(fundDet
										.getTransactionType().intValue());
								fundDetail.add(fundingDetail);
							}
							if (fundDetail != null)
								Collections.sort(fundDetail,
										FundingValidator.dateComp);
							fund.setFundingDetails(fundDetail);
							// funding.add(fund);
						}
						if (fundOrg.getFundings() == null)
							fundOrg.setFundings(new ArrayList());
						fundOrg.getFundings().add(fund);

						if (index > -1) {
							fundingOrgs.set(index, fundOrg);
						//	logger
							//		.info("Setting the fund org obj to the index :"
								//			+ index);
						} else {
							fundingOrgs.add(fundOrg);
						//	logger.info("Adding new fund org object");
						}
					}
					//logger.info("size = " + fundingOrgs);
					eaForm.setFundingOrganizations(fundingOrgs);
					eaForm.setTotalCommitments(totComm);
					eaForm.setTotalDisbursements(totDisb);
					eaForm.setTotalExpenditures(totExp);

					ArrayList regFunds = new ArrayList();
					Iterator rItr = activity.getRegionalFundings().iterator();

					while (rItr.hasNext()) {
						AmpRegionalFunding ampRegFund = (AmpRegionalFunding) rItr
								.next();
						FundingDetail fd = new FundingDetail();
						fd.setAdjustmentType(ampRegFund.getAdjustmentType()
								.intValue());
						if (fd.getAdjustmentType() == 1) {
							fd.setAdjustmentTypeName("Actual");
						} else if (fd.getAdjustmentType() == 0) {
							fd.setAdjustmentTypeName("Planned");
						}
						fd.setCurrencyCode(ampRegFund.getCurrency()
								.getCurrencyCode());
						fd.setCurrencyName(ampRegFund.getCurrency()
								.getCurrencyName());
						fd.setPerspectiveCode(ampRegFund.getPerspective()
								.getCode());
						fd.setPerspectiveName(ampRegFund.getPerspective()
								.getName());
						fd.setTransactionAmount(DecimalToText
								.ConvertDecimalToText(ampRegFund
										.getTransactionAmount().doubleValue()));
						fd.setTransactionDate(DateConversion
								.ConvertDateToString(ampRegFund
										.getTransactionDate()));
						fd.setTransactionType(ampRegFund.getTransactionType()
								.intValue());

						RegionalFunding regFund = new RegionalFunding();
						regFund.setRegionId(ampRegFund.getRegion()
								.getAmpRegionId());
						regFund.setRegionName(ampRegFund.getRegion().getName());

						if (regFunds.contains(regFund) == false) {
							regFunds.add(regFund);
						}

						int index = regFunds.indexOf(regFund);
						regFund = (RegionalFunding) regFunds.get(index);
						if (fd.getTransactionType() == 0) { // commitments
							if (regFund.getCommitments() == null) {
								regFund.setCommitments(new ArrayList());
							}
							regFund.getCommitments().add(fd);
						} else if (fd.getTransactionType() == 1) { // disbursements
							if (regFund.getDisbursements() == null) {
								regFund.setDisbursements(new ArrayList());
							}
							regFund.getDisbursements().add(fd);
						} else if (fd.getTransactionType() == 2) { // expenditures
							if (regFund.getExpenditures() == null) {
								regFund.setExpenditures(new ArrayList());
							}
							regFund.getExpenditures().add(fd);
						}
						regFunds.set(index, regFund);
					}

					// Sort the funding details based on Transaction date.
					Iterator itr1 = regFunds.iterator();
					int index = 0;
					while (itr1.hasNext()) {
						RegionalFunding regFund = (RegionalFunding) itr1.next();
						List list = null;
						if (regFund.getCommitments() != null) {
							list = new ArrayList(regFund.getCommitments());
							Collections.sort(list, FundingValidator.dateComp);
						}
						regFund.setCommitments(list);
						list = null;
						if (regFund.getDisbursements() != null) {
							list = new ArrayList(regFund.getDisbursements());
							Collections.sort(list, FundingValidator.dateComp);
						}
						regFund.setDisbursements(list);
						list = null;
						if (regFund.getExpenditures() != null) {
							list = new ArrayList(regFund.getExpenditures());
							Collections.sort(list, FundingValidator.dateComp);
						}
						regFund.setExpenditures(list);
						regFunds.set(index++, regFund);
					}

					eaForm.setRegionalFundings(regFunds);

					eaForm.setSelectedComponents(null);

					Collection componets = activity.getComponents();
					if (componets != null && componets.size() > 0) {
						ArrayList comp = new ArrayList();
						Iterator compItr = componets.iterator();
						while (compItr.hasNext()) {
							AmpComponent temp = (AmpComponent) compItr.next();
							Components tempComp = new Components();
							tempComp.setTitle(temp.getTitle());
							tempComp.setComponentId(temp.getAmpComponentId());
							tempComp.setDescription(temp.getDescription()
									.trim());
							tempComp.setCommitments(new ArrayList());
							tempComp.setDisbursements(new ArrayList());
							tempComp.setExpenditures(new ArrayList());

							Iterator cItr = temp.getComponentFundings()
									.iterator();
							while (cItr.hasNext()) {
								AmpComponentFunding ampCompFund = (AmpComponentFunding) cItr
										.next();
								FundingDetail fd = new FundingDetail();
								fd.setAdjustmentType(ampCompFund
										.getAdjustmentType().intValue());
								if (fd.getAdjustmentType() == 1) {
									fd.setAdjustmentTypeName("Actual");
								} else if (fd.getAdjustmentType() == 0) {
									fd.setAdjustmentTypeName("Planned");
								}
								fd.setCurrencyCode(ampCompFund.getCurrency()
										.getCurrencyCode());
								fd.setCurrencyName(ampCompFund.getCurrency()
										.getCurrencyName());
								fd.setPerspectiveCode(ampCompFund
										.getPerspective().getCode());
								fd.setPerspectiveName(ampCompFund
										.getPerspective().getName());
								fd.setTransactionAmount(DecimalToText
										.ConvertDecimalToText(ampCompFund
												.getTransactionAmount()
												.doubleValue()));
								fd.setTransactionDate(DateConversion
										.ConvertDateToString(ampCompFund
												.getTransactionDate()));
								fd.setTransactionType(ampCompFund
										.getTransactionType().intValue());

								if (fd.getTransactionType() == 0) {
									tempComp.getCommitments().add(fd);
								} else if (fd.getTransactionType() == 1) {
									tempComp.getDisbursements().add(fd);
								} else if (fd.getTransactionType() == 2) {
									tempComp.getExpenditures().add(fd);
								}
							}

							Collection phyProgess = temp.getPhysicalProgress();
							if (phyProgess != null && phyProgess.size() > 0) {
								Collection physicalProgress = new ArrayList();
								Iterator phyProgItr = phyProgess.iterator();
								while (phyProgItr.hasNext()) {
									AmpPhysicalPerformance phyPerf = (AmpPhysicalPerformance) phyProgItr
											.next();
									PhysicalProgress phyProg = new PhysicalProgress();
									phyProg.setPid(phyPerf.getAmpPpId());
									phyProg.setDescription(phyPerf
											.getDescription());
									phyProg.setReportingDate(DateConversion
											.ConvertDateToString(phyPerf
													.getReportingDate()));
									phyProg.setTitle(phyPerf.getTitle());
									physicalProgress.add(phyProg);
								}
								tempComp.setPhyProgress(physicalProgress);
							}
							comp.add(tempComp);
						}

						// Sort the funding details based on Transaction date.
						itr1 = comp.iterator();
						index = 0;
						while (itr1.hasNext()) {
							Components components = (Components) itr1.next();
							List list = null;
							if (components.getCommitments() != null) {
								list = new ArrayList(components
										.getCommitments());
								Collections.sort(list,
										FundingValidator.dateComp);
							}
							components.setCommitments(list);
							list = null;
							if (components.getDisbursements() != null) {
								list = new ArrayList(components
										.getDisbursements());
								Collections.sort(list,
										FundingValidator.dateComp);
							}
							components.setDisbursements(list);
							list = null;
							if (components.getExpenditures() != null) {
								list = new ArrayList(components
										.getExpenditures());
								Collections.sort(list,
										FundingValidator.dateComp);
							}
							components.setExpenditures(list);
							comp.set(index++, components);
						}

						eaForm.setSelectedComponents(comp);
					}

					Collection memLinks = TeamUtil.getMemberLinks(tm
							.getMemberId());
					Collection actDocs = activity.getDocuments();
					if (actDocs != null && actDocs.size() > 0) {
						Collection docsList = new ArrayList();
						Collection linksList = new ArrayList();

						Iterator docItr = actDocs.iterator();
						while (docItr.hasNext()) {
							RelatedLinks rl = new RelatedLinks();

							CMSContentItem cmsItem = (CMSContentItem) docItr
									.next();
							rl.setRelLink(cmsItem);
							rl.setMember(DbUtil.getAmpTeamMember(tm
									.getMemberId()));
							Iterator tmpItr = memLinks.iterator();
							while (tmpItr.hasNext()) {
								Documents doc = (Documents) tmpItr.next();
								if (doc.getDocId().longValue() == cmsItem
										.getId()) {
									rl.setShowInHomePage(true);
									break;
								}
							}

							if (cmsItem.getIsFile()) {
								docsList.add(rl);
							} else {
								linksList.add(rl);
							}
						}
						eaForm.setDocumentList(docsList);
						eaForm.setLinksList(linksList);
					}

					// loading the related organizations
					eaForm.setExecutingAgencies(new ArrayList());
					eaForm.setImpAgencies(new ArrayList());
					eaForm.setReportingOrgs(new ArrayList());
					Set relOrgs = activity.getOrgrole();
					if (relOrgs != null) {
						Iterator relOrgsItr = relOrgs.iterator();
						while (relOrgsItr.hasNext()) {
							AmpOrgRole orgRole = (AmpOrgRole) relOrgsItr.next();
							if (orgRole.getRole().getRoleCode().equals(
									Constants.EXECUTING_AGENCY)
									&& (!eaForm
											.getExecutingAgencies()
											.contains(orgRole.getOrganisation()))) {
								eaForm.getExecutingAgencies().add(
										orgRole.getOrganisation());
							} else if (orgRole.getRole().getRoleCode().equals(
									Constants.IMPLEMENTING_AGENCY)
									&& (!eaForm.getImpAgencies().contains(
											orgRole.getOrganisation()))) {
								eaForm.getImpAgencies().add(
										orgRole.getOrganisation());
							} else if (orgRole.getRole().getRoleCode().equals(
									Constants.REPORTING_AGENCY)
									&& (!eaForm.getReportingOrgs().contains(
											orgRole.getOrganisation()))) {
								eaForm.getReportingOrgs().add(
										orgRole.getOrganisation());
							}
						}
					}

					if (activity.getIssues() != null
							&& activity.getIssues().size() > 0) {
						ArrayList issueList = new ArrayList();
						Iterator iItr = activity.getIssues().iterator();
						while (iItr.hasNext()) {
							AmpIssues ampIssue = (AmpIssues) iItr.next();
							Issues issue = new Issues();
							issue.setId(ampIssue.getAmpIssueId());
							issue.setName(ampIssue.getName());
							ArrayList measureList = new ArrayList();
							if (ampIssue.getMeasures() != null
									&& ampIssue.getMeasures().size() > 0) {
								Iterator mItr = ampIssue.getMeasures()
										.iterator();
								while (mItr.hasNext()) {
									AmpMeasure ampMeasure = (AmpMeasure) mItr
											.next();
									Measures measure = new Measures();
									measure.setId(ampMeasure.getAmpMeasureId());
									measure.setName(ampMeasure.getName());
									ArrayList actorList = new ArrayList();
									if (ampMeasure.getActors() != null
											&& ampMeasure.getActors().size() > 0) {
										Iterator aItr = ampMeasure.getActors()
												.iterator();
										while (aItr.hasNext()) {
											AmpActor actor = (AmpActor) aItr
													.next();
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
						eaForm.setIssues(issueList);
					} else {
						eaForm.setIssues(null);
					}

					// loading the contact person details and condition
					eaForm.setDnrCntFirstName(activity.getContFirstName());
					eaForm.setDnrCntLastName(activity.getContLastName());
					eaForm.setDnrCntEmail(activity.getEmail());

					eaForm.setMfdCntFirstName(activity.getMofedCntFirstName());
					eaForm.setMfdCntLastName(activity.getMofedCntLastName());
					eaForm.setMfdCntEmail(activity.getMofedCntEmail());

					eaForm.setConditions(activity.getCondition().trim());

					if (activity.getActivityCreator() != null) {
						User usr = activity.getActivityCreator().getUser();
						if (usr != null) {
							eaForm.setActAthFirstName(usr.getFirstNames());
							eaForm.setActAthLastName(usr.getLastName());
							eaForm.setActAthEmail(usr.getEmail());
						}
					}
				}
			}
			Collection statusCol = null;
			// load the status from the database
			if(eaForm.getStatusCollection() == null) {
				statusCol= DbUtil.getAmpStatus();
				eaForm.setStatusCollection(statusCol);
			}
			else {
				statusCol = eaForm.getStatusCollection();
			}
			// Initailly setting the implementation level as "country"
			if (eaForm.getImplementationLevel() == null)
				eaForm.setImplementationLevel("country");

			Collection modalColl = null;
			// load the modalities from the database
			if (eaForm.getModalityCollection() == null) {
				modalColl = DbUtil.getAmpModality();
				eaForm.setModalityCollection(modalColl);
			} else {
				modalColl = eaForm.getModalityCollection();
			}

			// Initally set the modality as "Project Support"
			if (modalColl != null && eaForm.getModality() == null) {
				Iterator itr = modalColl.iterator();
				while (itr.hasNext()) {
					AmpModality mod = (AmpModality) itr.next();
					if (mod.getName().equalsIgnoreCase("Project Support")) {
						eaForm.setModality(mod.getAmpModalityId());
						break;
					}
				}
			}
			Collection levelCol = null;
			// Loading the levels from the database
			if (eaForm.getLevelCollection() == null) {
				levelCol = DbUtil.getAmpLevels();
				eaForm.setLevelCollection(levelCol);
			} else {
				levelCol = eaForm.getLevelCollection();
			}
			
			// load all themes 
			eaForm.setProgramCollection(ProgramUtil.getAllThemes());
			
			// load all the active currencies
			eaForm.setCurrencies(DbUtil.getAmpCurrency());
			
			// load all the perspectives
			eaForm.setPerspectives(DbUtil.getAmpPerspective());
			
			// load all the Activity specific M&E Indicators
//			eaForm.setIndicators(MEIndicatorsUtil.getActivityIndicators(eaForm.getActivityId()));
			logger.info("yeah in the edit activity......eaForm.getActivityId()... ::::::::::::::"+eaForm.getActivityId());
//			eaForm.setRiskCollection(MEIndicatorsUtil.getAllIndicatorRisks());

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return mapping.findForward("forward");
	}
}
