/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpTransactionTypeDonorFundingDetailModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.exception.NoCategoryClassException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * @author mpostelnicu@dgateway.org since Nov 12, 2010
 */
public abstract class AmpDonorFormTableFeaturePanel extends
	AmpFundingFormTableFeaturePanel<AmpFunding, AmpFundingDetail> {

	 private static Logger logger = Logger.getLogger(AmpDonorFormTableFeaturePanel.class);
	
	protected IModel<Set<AmpFundingDetail>> parentModel;
	protected IModel<Set<AmpFundingDetail>> setModel;
	
	public AmpFundingItemFeaturePanel getParentFundingItem() {
		AmpFundingItemFeaturePanel parent=(AmpFundingItemFeaturePanel) this.getParent().getParent();
		return parent;
	}

	public AmpDonorFormTableFeaturePanel(String id,
			final IModel<AmpFunding> model, String fmName, int transactionType,
			int titleHeaderColSpan) throws Exception {
		super(id, model, fmName);

		getTableId().add(new AttributeModifier("width", "620"));
		
		setTitleHeaderColSpan(titleHeaderColSpan);
		parentModel = new PropertyModel<Set<AmpFundingDetail>>(model,
				"fundingDetails");

		setModel = new AmpTransactionTypeDonorFundingDetailModel(parentModel, transactionType);
	}


	protected AmpCategoryGroupFieldPanel getAdjustmentTypeComponent(
			IModel<AmpFundingDetail> model, int transactionType) {
		
		String transactionTypeString = "";
		switch (transactionType) {
		case Constants.COMMITMENT:
			transactionTypeString = "Commitments";
			break;
		case Constants.DISBURSEMENT:
			transactionTypeString = "Disbursements";
			break;
		case Constants.DISBURSEMENT_ORDER:
			transactionTypeString = "Disbursement Orders";
			break;
		case Constants.EXPENDITURE:
			transactionTypeString = "Expenditures";
			break;
		default:
			throw new RuntimeException("unsupported transaction type");
		}

		IModel<Set<AmpCategoryValue>> dependantModel = null;
		AmpCategoryClass categClass = null;
		try {
			categClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.TRANSACTION_TYPE_KEY, false);
		} catch (NoCategoryClassException e1) {
			logger.error(e1);
		}
		List<AmpCategoryValue> values = categClass.getPossibleValues();
		Iterator<AmpCategoryValue> it = values.iterator();
		while (it.hasNext()) {
			AmpCategoryValue val = (AmpCategoryValue) it
					.next();
			if (val.getValue().compareTo(transactionTypeString) == 0){
				if (val.getUsedByValues() != null && val.getUsedByValues().size() > 0){
					HashSet<AmpCategoryValue> tmp = new HashSet<AmpCategoryValue>();
					tmp.add(val);
					dependantModel = new Model(tmp);
				}
				break;
			}
		}
		
		try{
			AmpCategoryGroupFieldPanel adjustmentTypes = new AmpCategoryGroupFieldPanel(
				"adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY,
						new PropertyModel<AmpCategoryValue>(model,"adjustmentType"),
						CategoryConstants.ADJUSTMENT_TYPE_NAME, //fmname
						 false, false, true, dependantModel);
			adjustmentTypes.getChoiceContainer().setRequired(true);
			return adjustmentTypes;
		}catch(Exception e)
		{
			logger.error("AmpCategoryGroupFieldPanel initialization failed");
		}
		return null;

		
	}

	protected AmpFundingAmountComponent getFundingAmountComponent(
			IModel<AmpFundingDetail> model) {
		return new AmpFundingAmountComponent<AmpFundingDetail>("fundingAmount",
				model, "Amount", "transactionAmount", "Currency",
				"ampCurrencyId", "Transaction Date", "transactionDate", false);
	}

	
	/**
	 * Deprecated
	 * 
	protected AmpDeleteLinkField getDeleteLinkField(String id, String fmName,
			final ListItem<AmpFundingDetail> item) {
		return new AmpDeleteLinkField(id, fmName) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				parentModel.getObject().remove(item.getModelObject());
				target.add(AmpDonorFormTableFeaturePanel.this);
				list.removeAll();
				target.add(getParentFundingItem().getFundingInfo());
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(getParentFundingItem().getFundingInfo()));
			}
		};
	}
	 *	
	 */
}
