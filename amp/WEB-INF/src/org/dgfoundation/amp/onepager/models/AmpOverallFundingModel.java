package org.dgfoundation.amp.onepager.models;

import java.util.Set;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpOverallFundingModel implements IModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4602506401806485678L;
	private IModel<Set<AmpFunding>> fundingModel;
	private int transactionType;
	String adjustmentType;
	private IModel<AmpFunding> singleFundingModel;

	public AmpOverallFundingModel(IModel<Set<AmpFunding>> fundingModel, IModel<AmpFunding> singleFundingModel,
			int transactionType, String adjustmentType) {
		this.fundingModel = fundingModel;
		this.transactionType = transactionType;
		this.adjustmentType = adjustmentType;
		this.singleFundingModel = singleFundingModel;
	}

	@Override
	public void detach() {
		if (fundingModel != null) {
			fundingModel.detach();
		} else {
			singleFundingModel.detach();
		}
	}

	@Override
	public String getObject() {
		String toCurrCode = Constants.DEFAULT_CURRENCY;
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute("currentMember");

		if (tm != null) {
			toCurrCode = CurrencyUtil.getAmpcurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
		}
		return doCalculations(toCurrCode).toString() + " " + toCurrCode;
	}

	@Override
	public void setObject(Object object) {
		throw new AssertionError("Shouldn't be used!");
	}

	private DecimalWraper doCalculations(String toCurrCode) {
		DecimalWraper amount = new DecimalWraper();
		if (singleFundingModel != null) {
			processFunding(toCurrCode, amount, singleFundingModel.getObject());
		} else {
			for (AmpFunding funding : fundingModel.getObject()) {
				processFunding(toCurrCode, amount, funding);
			}
		}
		return amount;
	}

	private void processFunding(String toCurrCode, DecimalWraper amount, AmpFunding funding) {
		for (AmpFundingDetail fundingDetail : funding.getFundingDetails()) {
			if (fundingDetail.getAdjustmentType() != null && fundingDetail.getAdjustmentType().getValue() != null
					&& fundingDetail.getAdjustmentType().getValue().equals(adjustmentType)
					&& fundingDetail.getTransactionType().intValue() == transactionType) {

				if (fundingDetail.getAmpCurrencyId() != null && fundingDetail.getTransactionAmount() != null
						&& fundingDetail.getTransactionDate() != null) {
					amount.add(getDecimalWraper(fundingDetail, toCurrCode));
				}
			}
		}
	}

	private DecimalWraper getDecimalWraper(AmpFundingDetail fundDet, String toCurrCode) {
		java.sql.Date dt = new java.sql.Date(fundDet.getTransactionDate().getTime());

		Double fixedExchangeRate = fundDet.getFixedExchangeRate();
		if (fixedExchangeRate != null && (Math.abs(fixedExchangeRate.doubleValue()) < 1.0E-15))
			fixedExchangeRate = null;
		double frmExRt;
		if (fixedExchangeRate == null) {
			frmExRt = Util.getExchange(fundDet.getAmpCurrencyId().getCurrencyCode(), dt);
		} else {
			frmExRt = fixedExchangeRate;
		}
		double toExRt;

		if (fundDet.getAmpCurrencyId().getCurrencyCode().equalsIgnoreCase(toCurrCode)) {
			toExRt = frmExRt;
		} else {
			toExRt = Util.getExchange(toCurrCode, dt);
		}
		return CurrencyWorker.convertWrapper(fundDet.getTransactionAmount().doubleValue(), frmExRt, toExRt, dt);

	}

}
