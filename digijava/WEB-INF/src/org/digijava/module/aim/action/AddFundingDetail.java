package org.digijava.module.aim.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * @author jose
 */
public class AddFundingDetail extends Action {

	private static Logger logger = Logger.getLogger(AddFundingDetail.class);

	private ArrayList fundingDetails = null;

	private String event;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		EditActivityForm formBean = (EditActivityForm) form;
		formBean.setReset(false);
		event = formBean.getEvent();
		logger.info("EVENT : "+event);
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		
		String perspCode = null;
		if (formBean.isDonorFlag()) perspCode = Constants.DONOR;
		else perspCode = Constants.MOFED;
			
		String currCode = Constants.DEFAULT_CURRENCY;
		if (teamMember.getAppSettings() != null) {
			ApplicationSettings appSettings = teamMember.getAppSettings();
			if (appSettings.getCurrencyId() != null) {
				currCode = CurrencyUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
			}
		}		
				
		long index = formBean.getTransIndexId();
		logger.info("Index = " + index);
		String subEvent = event.substring(0,3);
		FundingDetail fd = null;
		if (subEvent.equalsIgnoreCase("del") || subEvent.equalsIgnoreCase("add")) {
			if (formBean.getFundingDetails() == null) {
				fundingDetails = new ArrayList();
				fd = getFundingDetail(perspCode,currCode);
				fundingDetails.add(fd);		
			} else {
				fundingDetails = new ArrayList(formBean.getFundingDetails());
				if (subEvent.equals("del")) {
					FundingDetail temp = new FundingDetail();
					temp.setIndexId(index);
					fundingDetails.remove(temp);					
				} else {
					fd = getFundingDetail(perspCode,currCode);
					fundingDetails.add(fd);							
				}
			}
			if (fd != null && fd.getTransactionType() == 0) {
				formBean.setNumComm(formBean.getNumComm() + 1);
			} else if (fd != null && fd.getTransactionType() == 1) {
				formBean.setNumDisb(formBean.getNumDisb() + 1);
			} else if (fd != null && fd.getTransactionType() == 2) {
				formBean.setNumExp(formBean.getNumExp() + 1);
			}
			formBean.setFundingDetails(fundingDetails);			
		}
		formBean.setEvent(null);
		formBean.setDupFunding(true);
		formBean.setTransAmtZeroOrEmpty(false);
		formBean.setTransAmtLarge(false);
		formBean.setTransAmtInvalid(false);
		formBean.setTransDateEmpty(false);
		formBean.setFirstSubmit(false);
		return mapping.findForward("forward");
	}

	private FundingDetail getFundingDetail(String perspCode,String currCode) {
		FundingDetail fundingDetail = new FundingDetail();
		if (event.equalsIgnoreCase("addCommitments")) {
			fundingDetail.setTransactionType(Constants.COMMITMENT);
		} else if (event.equalsIgnoreCase("addDisbursements")) {
			fundingDetail.setTransactionType(Constants.DISBURSEMENT);
		} else if (event.equalsIgnoreCase("addExpenditures")) {
			fundingDetail.setTransactionType(Constants.EXPENDITURE);
			fundingDetail.setClassification("");
		}
		fundingDetail.setCurrencyCode(currCode);
		fundingDetail.setAdjustmentType(Constants.ACTUAL);
		fundingDetail.setIndex(fundingDetails.size());
		fundingDetail.setIndexId(System.currentTimeMillis());
		fundingDetail.setPerspectiveCode(perspCode);	
		return fundingDetail;
	}
}