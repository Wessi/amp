package org.digijava.module.aim.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class FundingAdded extends Action {

	private static Logger logger = Logger.getLogger(FundingAdded.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);

		Iterator fundOrgsItr = eaForm.getFunding().getFundingOrganizations().iterator();
		FundingOrganization fundOrg = null;
		boolean found = false;
		int fundOrgOffset = 0;
		while (fundOrgsItr.hasNext()) {
			fundOrg = (FundingOrganization) fundOrgsItr.next();
			if (fundOrg.getAmpOrgId().equals(eaForm.getFunding().getOrgId())) {
				found = true;
				break;
			}
			fundOrgOffset++;
		}
		//
		int offset = -1;
		Collection oldFundDetails = null;
		if (found) {
			if (eaForm.getFunding().isEditFunding()) {
				offset = eaForm.getFunding().getOffset();
				ArrayList fundList = new ArrayList(fundOrg.getFundings());
				Funding fs = (Funding) fundList.get(offset);
				oldFundDetails = fs.getFundingDetails();
				fundList.set(offset, null);
				fundOrg.setFundings(fundList);
			}
		}
		//		
		BigDecimal totalComms	= new BigDecimal(0);
		BigDecimal totalDisbs	= new BigDecimal(0);
		BigDecimal totalExps	= new BigDecimal(0);
		boolean isBigger = false;
		//
		if (eaForm.getFunding().getFundingDetails() != null) {
			Iterator itr = eaForm.getFunding().getFundingDetails().iterator();
			while (itr.hasNext()) {
				FundingDetail fundDet = (FundingDetail) itr.next();
				//
				BigDecimal amount = this.getAmountInDefaultCurrency(fundDet, tm.getAppSettings());					
				if (( fundDet.getTransactionType() == Constants.COMMITMENT )&&(fundDet.getAdjustmentType()==Constants.ACTUAL))
					totalComms=totalComms.add(amount);
				else 
					if (( fundDet.getTransactionType() == Constants.DISBURSEMENT )&&(fundDet.getAdjustmentType()==Constants.ACTUAL))
						totalDisbs=totalDisbs.add( amount);
					else 
						if (( fundDet.getTransactionType() == Constants.EXPENDITURE )&&(fundDet.getAdjustmentType()==Constants.ACTUAL))
							totalExps =totalExps.add( amount);
			}
			// Don't know why when using constant it is not working ???
			String alert = FeaturesUtil.getGlobalSettingValue("Alert if sum of disbursments is bigger than sum of commitments");
			//
			if (Boolean.parseBoolean(alert)) {
				if (totalDisbs.compareTo(totalComms) > 0 ) {
					eaForm.setTotDisbIsBiggerThanTotCom(true);
					isBigger = true;
				} else {
					eaForm.setTotDisbIsBiggerThanTotCom(false);
				}
			}
		}
		EditActivityForm.Funding currentFunding = null;
		if ((!isBigger) || (eaForm.isIgnoreDistBiggerThanComm())) {
			eaForm.setIgnoreDistBiggerThanComm(false);
			currentFunding = eaForm.getFunding();
			//
			Funding newFund = new Funding();
			//
			if (currentFunding.getFundingId() != null && currentFunding.getFundingId().longValue() > 0) {
				newFund.setFundingId(currentFunding.getFundingId().longValue());
			} else {
				newFund.setFundingId(System.currentTimeMillis());
			}
			//newFund.setAmpTermsAssist(DbUtil.getAssistanceType(eaForm.getAssistanceType()));
			newFund.setTypeOfAssistance( CategoryManagerUtil.getAmpCategoryValueFromDb(currentFunding.getAssistanceType()) );
			newFund.setOrgFundingId(currentFunding.getOrgFundingId());
			newFund.setFinancingInstrument(CategoryManagerUtil.getAmpCategoryValueFromDb(currentFunding.getModality()));
			newFund.setConditions(currentFunding.getFundingConditions());
			newFund.setDonorObjective(currentFunding.getDonorObjective());
			newFund.setTypeOfAssistance( CategoryManagerUtil.getAmpCategoryValueFromDb(currentFunding.getAssistanceType()) );
			newFund.setOrgFundingId(currentFunding.getOrgFundingId());
			newFund.setFinancingInstrument(CategoryManagerUtil.getAmpCategoryValueFromDb(currentFunding.getModality()));
			newFund.setConditions(currentFunding.getFundingConditions());
			//
			Collection mtefProjections=new ArrayList();
			if (currentFunding.getFundingMTEFProjections() != null) {
				Iterator itr = currentFunding.getFundingMTEFProjections().iterator();
				while (itr.hasNext()) {
					MTEFProjection mtef = (MTEFProjection) itr.next();

					if ( mtef.getAmount() == null ) //This MTEFProjection has been created in AddFunding action 
						continue;				// but if projections are disabled then the amount will be empty so this shouldn't be taken into consideration
					String formattedAmt = CurrencyWorker.formatAmount(
							mtef.getAmount());
					mtef.setAmount(formattedAmt);
					if (mtef.getCurrencyCode() != null
							&& mtef.getCurrencyCode().trim().length() != 0) {
						AmpCurrency currency = CurrencyUtil.getCurrencyByCode(mtef.getCurrencyCode());
						mtef.setCurrencyName(currency.getCountryName());
					}
					if (mtef.getReportingOrganizationId() != null
							&& mtef.getReportingOrganizationId().intValue() != 0) {
						AmpOrganisation org = DbUtil.getOrganisation(mtef
								.getReportingOrganizationId());
						mtef.setReportingOrganizationName(org.getName());

					}
					mtefProjections.add(mtef);
				}
			}		
			Collection fundDetails = new ArrayList();
			if (currentFunding.getFundingDetails() != null) {
				Iterator itr = currentFunding.getFundingDetails().iterator();				
				itr = currentFunding.getFundingDetails().iterator();
				while (itr.hasNext()) {
					FundingDetail fundDet = (FundingDetail) itr.next();
					String formattedAmt = CurrencyWorker.formatAmount(
							fundDet.getTransactionAmount());
					fundDet.setTransactionAmount(formattedAmt);
					//
					if (fundDet.getCurrencyCode() != null
							&& fundDet.getCurrencyCode().trim().length() != 0) {
						AmpCurrency currency = CurrencyUtil.getCurrencyByCode(fundDet
								.getCurrencyCode());
						fundDet.setCurrencyName(currency.getCountryName());
					}
					if (fundDet.getReportingOrganizationId() != null
							&& fundDet.getReportingOrganizationId().intValue() != 0) {
						AmpOrganisation org = DbUtil.getOrganisation(fundDet
								.getReportingOrganizationId());
						fundDet.setReportingOrganizationName(org.getName());
					}
					if (fundDet.getAdjustmentType() == Constants.PLANNED)
						fundDet.setAdjustmentTypeName("Planned");
					else if (fundDet.getAdjustmentType() == Constants.ACTUAL) {
						fundDet.setAdjustmentTypeName("Actual");
					}				
					//
					fundDetails.add(fundDet);
				}
			}
			//
			List sortedList = new ArrayList(fundDetails);
			Collections.sort(sortedList,FundingValidator.dateComp);

			ArrayList fundList = new ArrayList();
			if (fundOrg.getFundings() != null) {
				fundList = new ArrayList(fundOrg.getFundings());
			}

			currentFunding.setDupFunding(false);
			currentFunding.setFirstSubmit(false);

			if (currentFunding.getFundingDetails() != null) 
			{
				int i=0;
				Iterator fundItr1 = currentFunding.getFundingDetails().iterator();
				while(fundItr1.hasNext())
				{
					i++;
					FundingDetail fundDetItr1 = (FundingDetail) fundItr1.next();
					Iterator fundItr2 = currentFunding.getFundingDetails().iterator();
					int j=0;
					while(fundItr2.hasNext())
					{
						j++;
						FundingDetail fundDetItr2 = (FundingDetail) fundItr2.next();
						if(j>i)
						{
							if((fundDetItr2.getAdjustmentTypeName().equalsIgnoreCase(fundDetItr1.getAdjustmentTypeName()))&&
									(fundDetItr2.getCurrencyCode().equalsIgnoreCase(fundDetItr1.getCurrencyCode()))&&
									(fundDetItr2.getTransactionAmount().equalsIgnoreCase(fundDetItr1.getTransactionAmount()))&&
									(fundDetItr2.getTransactionDate().equalsIgnoreCase(fundDetItr1.getTransactionDate()))&&
									(fundDetItr2.getTransactionType()==fundDetItr1.getTransactionType()))
							{
								currentFunding.setDupFunding(true);
								currentFunding.setFirstSubmit(true);
							}
						}
					}
				}
			}
			//
			newFund.setFundingDetails(sortedList);
			newFund.setMtefProjections(mtefProjections);
			if (offset != -1)
				fundList.set(offset, newFund);
			else
				fundList.add(newFund);
			//
			fundOrg.setFundings(fundList);
			ArrayList fundingOrgs = new ArrayList();
			if (currentFunding.getFundingOrganizations() != null) {
				fundingOrgs = new ArrayList(currentFunding.getFundingOrganizations());
				fundingOrgs.set(fundOrgOffset, fundOrg);
			}
			//
			this.updateTotals(eaForm, tm);
		}
		//
		String currCode = CurrencyUtil.getAmpcurrency( tm.getAppSettings().getCurrencyId() ).getCurrencyCode();
		eaForm.setCurrCode( currCode );
		eaForm.setStep("3");

		return mapping.findForward("forward");
	}
	
	private BigDecimal[] getFundingAmounts(EditActivityForm form, TeamMember tm) {
		BigDecimal totalComms	= new BigDecimal(0);
		BigDecimal totalDisbs	= new BigDecimal(0);
		BigDecimal totalExps	= new BigDecimal(0);		
		//
		Collection <FundingOrganization> orgs	= form.getFunding().getFundingOrganizations();
		if ( orgs != null ) {
			Iterator<FundingOrganization> iterOrg	= orgs.iterator();
			while ( iterOrg.hasNext() ) {
				Collection<Funding> funds		= iterOrg.next().getFundings();
				if ( funds != null ) {
					Iterator<Funding> iterFund	= funds.iterator();	
					while ( iterFund.hasNext() ) {
						Collection<FundingDetail> details	= iterFund.next().getFundingDetails();
						if ( details != null ) {
							Iterator<FundingDetail> iterDet	= details.iterator();
							while ( iterDet.hasNext() ) {
								FundingDetail detail		= iterDet.next();
								BigDecimal amount				= this.getAmountInDefaultCurrency(detail, tm.getAppSettings());					
								if (( detail.getTransactionType() == Constants.COMMITMENT )&&(detail.getAdjustmentType()==Constants.ACTUAL))
											totalComms	=totalComms.add(amount) ;
								else 
									if (( detail.getTransactionType() == Constants.DISBURSEMENT )&&(detail.getAdjustmentType()==Constants.ACTUAL))
											totalDisbs=totalDisbs.add( amount);
									else 
										if (( detail.getTransactionType() == Constants.EXPENDITURE )&&(detail.getAdjustmentType()==Constants.ACTUAL))
											totalExps=totalExps.add(amount);
							}
						}
					}
				}
				
			}
		}
		BigDecimal[] amounts = {totalComms, totalDisbs, totalExps};
		//
		return amounts;
	}
	
	private void updateTotals ( EditActivityForm form, TeamMember tm ) {
		BigDecimal [] amounts = getFundingAmounts(form, tm);
		//
		form.getFunding().setTotalCommitments(FormatHelper.formatNumber(amounts[0]));
		form.getFunding().setTotalDisbursements(FormatHelper.formatNumber(amounts[1]));
		form.getFunding().setTotalExpenditures(FormatHelper.formatNumber(amounts[2])	);
	}
	
	private BigDecimal getAmountInDefaultCurrency(FundingDetail fundDet, ApplicationSettings appSet) {		
		java.sql.Date dt = new java.sql.Date(DateConversion.getDate(fundDet.getTransactionDate()).getTime());
		double frmExRt = Util.getExchange(fundDet.getCurrencyCode(),dt);
		String toCurrCode = CurrencyUtil.getAmpcurrency( appSet.getCurrencyId() ).getCurrencyCode();
		double toExRt = Util.getExchange(toCurrCode,dt);
		BigDecimal amt = CurrencyWorker.convert1(FormatHelper.parseBigDecimal(fundDet.getTransactionAmount()),frmExRt,toExRt);
		//
		return amt;		
	}
}
