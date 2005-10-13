/**
 * GetActivityDetails.java
 * 
 * @author Priyajith C 22-Nov-2004
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpModality;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.ChannelOverviewForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Assistance;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancingBreakdownWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

public class GetActivityDetails extends Action {

	private static Logger logger = Logger.getLogger(GetActivityDetails.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		boolean permitted = false;
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") != null) {
			String key = (String) session.getAttribute("ampAdmin");
			if (key.equalsIgnoreCase("yes")) {
				permitted = true;
			} else {
				if (session.getAttribute("teamLeadFlag") != null) {
					key = (String) session.getAttribute("teamLeadFlag");
					if (key.equalsIgnoreCase("true")) {
						permitted = true;	
					}
				}
			}
		}
		if (!permitted) {
			return mapping.findForward("index");
		}
		
		Long id = null;
		if (request.getParameter("id") == null) {
			mapping.findForward("viewMyDesktop");
		} else {
			try {
				id = new Long(Long.parseLong(request.getParameter("id")));
			} catch (Exception e) {
				return mapping.findForward("viewMyDesktop");
			}
		}

		ChannelOverviewForm coForm = (ChannelOverviewForm) form;

		boolean addFlag = Boolean.getBoolean(request.getParameter("add"));
		if (addFlag == true) {
			coForm.setAdd(true);
		}

		AmpActivity activity = DbUtil.getProjectChannelOverview(id);


		Collection dbReturnSet = null;
		ArrayList subSectors = new ArrayList();
		ArrayList sectors = new ArrayList();
		Iterator itera=null;
		AmpSector ampSector = null;
		String modalityCode;
		dbReturnSet = DbUtil.getAmpSectors(activity.getAmpActivityId());
		
		if(activity != null)
			logger.info(" Activity ID  = " + activity.getAmpId());
		String modName ="";
		AmpModality modality = activity.getModality();
		if (modality != null) {
			modName = modality.getName();
			//logger.info("Project Modality : " + modName);
			coForm.setModality(modName);
			modName = modality.getModalityCode();
			modalityCode = modName;
			//logger.info("Project Modality Code: " + modName);
			coForm.setModalityCode(modName);					
		}
		Long actId = activity.getAmpActivityId();
		Collection funding = DbUtil.getFundingByActivity(actId);
		coForm.setModal(new ArrayList());
		if (funding != null) {
			Iterator itrf = funding.iterator(); 
			boolean flag = true; 
			while (itrf.hasNext()) {
				AmpFunding ampf = (AmpFunding) itrf.next();
				AmpModality modal = ampf.getModalityId();
				if (modal != null) {
					if (!coForm.getModal().isEmpty()) {
						for (int i = 0; flag && i < coForm.getModal().size(); i++) {
							Iterator itr = coForm.getModal().iterator();
							while (itr.hasNext()) {
								AmpModality amod = (AmpModality) itr.next();
								if (modal.getName().equals(amod.getName())) {
									flag = false;
									break;
								}
							}
						}
					}
					if (flag)
						coForm.getModal().add(modal);
				}
			}
		}
		

		// start $3
		if (modality != null) {
			if (modality.getModalityCode().equals(Constants.PROGRAM_SUPPORT) ||
					modality.getModalityCode().equals(Constants.OTHER_AID)) {
				if (activity.getThemeId() != null) {
					modName = activity.getThemeId().getName() ;
					//logger.info("Program/Theme Name : " + modName) ;
					coForm.setTheme(modName) ;						
				}
			}
		}
		// end $3
	
		if (modality != null) {
			if (!(modality.getModalityCode().equals(Constants.DIRECT_BUDGET_SUPPORT))) {
				modName = "";
				if (activity.getLevel() != null) {
					modName = activity.getLevel().getName();
					//logger.info("Implementation Level : " + modName);
				}
				coForm.setLevel(modName);
			}
			//logger.info("after level");
			if (modality.getModalityCode().equals(Constants.PROJECT_SUPPORT)
					|| modality.getModalityCode().equals(Constants.DIRECT_BUDGET_SUPPORT)) {
				dbReturnSet = DbUtil.getAmpSectors(activity
						.getAmpActivityId());
				if (dbReturnSet.size() == 0)
					logger.info("No sectors returned");
				itera = dbReturnSet.iterator();
				while (itera.hasNext()) {
					ampSector = (AmpSector) itera.next();
					if (ampSector.getParentSectorId() != null) {
						logger.info("Sub sector : " + ampSector.getName()
								+ " " + ampSector.getAmpSectorId());
						logger.info("Parent sector : "
								+ ampSector.getParentSectorId()
										.getAmpSectorId());
						subSectors.add(ampSector);
						ampSector = ampSector.getParentSectorId();
						if (sectors.indexOf(ampSector) == -1)
							sectors.add(ampSector);
					} else {
						logger.info("Main sector : " + ampSector.getName()
								+ " " + ampSector.getAmpSectorId());
						if (sectors.indexOf(ampSector) == -1)
							sectors.add(ampSector);
					}
				}
				coForm.setSectors(sectors);
				coForm.setSubSectors(subSectors);
			}					
		}
		
		dbReturnSet = activity.getInternalIds();
		itera = dbReturnSet.iterator();
		coForm.setInternalIds(new ArrayList());
		while (itera.hasNext()) {
			AmpActivityInternalId ampActivityInternalId = (AmpActivityInternalId) itera.next();
			logger.debug("Amp Activity Internal Id "
					+ ampActivityInternalId.getInternalId());
			coForm.getInternalIds().add(ampActivityInternalId);
		}
		
		coForm.setId(activity.getAmpActivityId());
		coForm.setActivityStartDate(DateConversion.
				ConvertDateToString(activity.getActivityStartDate()));
		coForm.setActivityCloseDate(DateConversion
				.ConvertDateToString(activity.getActivityCloseDate()));
		coForm.setCondition(activity.getCondition());

		Collection ampFundings = null;
		String currCode = null;
		String perspective = null;
		Long fiscalCalId = null;
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		if (teamMember == null) 
		{
			coForm.setValidLogin(false);
		} 
		else 
		{
			coForm.setValidLogin(true);
		}
		ampFundings = DbUtil.getAmpFunding(activity.getAmpActivityId());
		ApplicationSettings appSettings = teamMember.getAppSettings();
		if (appSettings.getCurrencyId() != null) {
			currCode = DbUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
		} else {
			currCode = Constants.DEFAULT_CURRENCY;
		}
		if (appSettings.getFisCalId() != null) {
			fiscalCalId = appSettings.getFisCalId();
		} else {
			fiscalCalId = Constants.GREGORIAN;
		}
		if (appSettings.getPerspective() != null) {
			perspective = appSettings.getPerspective();
		} else {
			perspective = "Donor";
		}
		if (ampFundings != null) {
			FilterParams fp = new FilterParams();
			fp.setCurrencyCode(currCode);
			fp.setFiscalCalId(fiscalCalId);
			if (perspective.equalsIgnoreCase("Donor"))
				fp.setPerspective(Constants.DONOR);
			if (perspective.equalsIgnoreCase("MOFED"))
				fp.setPerspective(Constants.MOFED);
			fp.setFromYear(1934);
			fp.setToYear(2014);
			Collection fb = FinancingBreakdownWorker.getFinancingBreakdownList(
					activity.getAmpActivityId(), ampFundings, fp);
			coForm.setGrandTotal(
					FinancingBreakdownWorker.getOverallTotal(fb, 0));
		}

		logger.info("Results Check ");
		logger.info(" Codition :" + coForm.getCondition());
		logger.info(" Imp Level : " + coForm.getLevel());
		logger.info(" Eff Date : " + coForm.getActivityStartDate());
		logger.info(" Close Date :   " + coForm.getActivityCloseDate());
		logger.info(" COst : " + coForm.getGrandTotal());

//------------------------------------------------------------------------------

		if (activity != null) {
			coForm.setName(activity.getName());
			coForm.setDescription(activity.getDescription());
			coForm.setObjective(activity.getObjective());

			if (activity.getModality() != null) 
				coForm.setModality(activity.getModality().getName());
			if (activity.getStatus() != null)
				coForm.setStatus(activity.getStatus().getName());
			if (activity.getThemeId() != null)
				coForm.setTheme(activity.getThemeId().getName());
			//Modified by Swapnil as now the method returns country,region,zone and woreda.
			coForm.setLocations(new ArrayList());
			coForm.getLocations().addAll(DbUtil.getAmpLocations(activity.getAmpActivityId()));

			ArrayList list = new ArrayList();
			list = DbUtil.getOrgRole(activity.getAmpActivityId());
			if (list.size() >= 2) {
				coForm.setReportingagency((String) list.get(0));
				coForm.setFundingagency((String) list.get(1));
				if (activity.getModality() != null && 
						(!activity.getModality().
								getModalityCode().equals(Constants.DIRECT_BUDGET_SUPPORT))) {
					if (list.size() >= 4) {
						coForm.setImplagency((String) list.get(2));
						coForm.setRelatedins((String) list.get(3));
					}
				}

			}

			Iterator iter = DbUtil.getAmpAssistanceType(
					activity.getAmpActivityId()).iterator();
			Collection tempCol = new ArrayList();
			while (iter.hasNext()) {
				Assistance assistance = (Assistance) iter.next();
				tempCol.add(assistance);
			}
			coForm.setAssistance(tempCol);

			if (session.getAttribute("pageno") != null) {
				Integer page = (Integer) session.getAttribute("pageno");
				coForm.setPageNo(page);
			}
		}

		return mapping.findForward("forward");

	}
}
