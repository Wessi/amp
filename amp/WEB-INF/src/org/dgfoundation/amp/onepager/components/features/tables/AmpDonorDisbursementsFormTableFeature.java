/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionsSumComparatorValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpTransactionTypeDonorFundingDetailModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpDonorDisbursementsFormTableFeature extends
		AmpDonorFormTableFeaturePanel {
	private static final long serialVersionUID = 1L;
	private boolean alertIfExpenditureBiggerDisbursment = false;  
	private boolean alertIfDisbursmentBiggerCommitments = false;
	private final static int SELECTOR_SIZE = 80;
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpDonorDisbursementsFormTableFeature(String id,
			final IModel<AmpFunding> model, String fmName, final int transactionType) throws Exception {
		super(id, model, fmName, Constants.DISBURSEMENT, 8);
	
		final AbstractReadOnlyModel<List<String>> disbOrderIdModel = new AbstractReadOnlyModel<List<String>>() {
			@Override
			public List<String> getObject() {
				List<String> ret=new ArrayList<String>(); 
				for (AmpFundingDetail ampFundingDetail : parentModel.getObject()) 
					if(ampFundingDetail.getTransactionType().equals(Constants.DISBURSEMENT_ORDER)) ret.add(ampFundingDetail.getDisbOrderId());
				return ret;
			}
		};		
		

		if("TRUE".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_EXPENDITURE_BIGGER_DISBURSMENT)))
			alertIfExpenditureBiggerDisbursment = true;
		if("TRUE".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_DISBURSMENT_BIGGER_COMMITMENTS)))
			alertIfDisbursmentBiggerCommitments = true;

		AbstractReadOnlyModel<List<AmpFundingDetail>> setAmountListModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel);
		AbstractReadOnlyModel<List<AmpFundingDetail>> commitmentModel =  OnePagerUtil
				.getReadOnlyListModelFromSetModel(new AmpTransactionTypeDonorFundingDetailModel(parentModel, Constants.COMMITMENT));
		AbstractReadOnlyModel<List<AmpFundingDetail>> expenditureModel =  OnePagerUtil
				.getReadOnlyListModelFromSetModel(new AmpTransactionTypeDonorFundingDetailModel(parentModel, Constants.EXPENDITURE));

		
		WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
		add(wmc);
		AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
		wmc.add(iValidator);
		
		final AmpCollectionsSumComparatorValidatorField amountSumComparator=
				new AmpCollectionsSumComparatorValidatorField("amountSumComparator",setAmountListModel,"checkCommitmentSum", "AmpCommitmentsCollectionsSumComparatorValidator"); 
		amountSumComparator.setIndicatorAppender(iValidator);
		amountSumComparator.setSecondCollectionModel(commitmentModel);
		amountSumComparator.setAlertIfCurrentModelAmountSumBig(true);
        amountSumComparator.setOutputMarkupId(true);
		amountSumComparator.setVisibilityAllowed(alertIfDisbursmentBiggerCommitments);
		add(amountSumComparator);
		
		
		final AmpCollectionsSumComparatorValidatorField amountSumComparator1=
				new AmpCollectionsSumComparatorValidatorField("amountSumComparator1",setAmountListModel,"checkExpenditureSum", "AmpExpendituresCollectionsSumComparatorValidator");
		amountSumComparator1.setIndicatorAppender(iValidator);
		amountSumComparator1.setSecondCollectionModel(expenditureModel);
		amountSumComparator1.setAlertIfCurrentModelAmountSumBig(false);
        amountSumComparator1.setOutputMarkupId(true);
		amountSumComparator1.setVisibilityAllowed(alertIfExpenditureBiggerDisbursment);
		wmc.setVisibilityAllowed(alertIfDisbursmentBiggerCommitments || alertIfExpenditureBiggerDisbursment);
		add(amountSumComparator1);
		
		list = new ListEditor<AmpFundingDetail>("listDisbursements", setModel, new AmpFundingDetail.FundingDetailComparator()) {

			@Override
			protected void onPopulateItem(
					org.dgfoundation.amp.onepager.components.ListItem<AmpFundingDetail> item) {
				item.add(getAdjustmentTypeComponent(item.getModel(), transactionType));
				AmpFundingAmountComponent amountComponent = getFundingAmountComponent(item.getModel());
				
				amountComponent.setAmountValidator(amountSumComparator1);
				amountComponent.setAmountValidator(amountSumComparator); 	
				item.add(amountComponent);

                AmpTextFieldPanel<Float> capitalSpendingPercentage = new AmpTextFieldPanel<Float>(
                                        "capitalSpendingPercentage",
                                        new PropertyModel<Float>(item.getModel(), "capitalSpendingPercentage"), "Capital Spending Percentage", false, true);
                capitalSpendingPercentage.getTextContainer().add(new RangeValidator<Float>(0f, 100f));
                capitalSpendingPercentage.getTextContainer().add(new AttributeModifier("size", new Model<String>("5")));
                item.add(capitalSpendingPercentage);

                AmpSelectFieldPanel<String> disbOrdIdSelector = new AmpSelectFieldPanel<String>("disbOrderId",
						new PropertyModel<String>(item.getModel(),
								"disbOrderId")
								,disbOrderIdModel,
						"Disbursement Order Id", false, true, null, true);
                disbOrdIdSelector.getChoiceContainer().add(new AttributeAppender("style", new Model<String>("width: "+SELECTOR_SIZE+"px")));
				item.add(disbOrdIdSelector);
				
				ArrayList<IPAContract> contractList;
				if (model.getObject().getAmpActivityId() != null && model.getObject().getAmpActivityId().getContracts() != null)
					contractList = new ArrayList<IPAContract>(model.getObject()
						.getAmpActivityId().getContracts());
				else
					contractList = new ArrayList<IPAContract>();
				AmpSelectFieldPanel<IPAContract> contractSelector = new AmpSelectFieldPanel<IPAContract>("contract",
						new PropertyModel<IPAContract>(item.getModel(),
								"contract"),
						new Model<ArrayList<IPAContract>>(contractList),
						"Contract", false, true, null, true);
				
				contractSelector.getChoiceContainer().add(new AttributeAppender("style", new Model<String>("width: "+SELECTOR_SIZE+"px")));
				item.add(contractSelector);
				
				IModel<List<FundingPledges>> pledgesModel = new LoadableDetachableModel<List<FundingPledges>>() {
					protected java.util.List<FundingPledges> load() {
						return PledgesEntityHelper
								.getPledgesByDonorGroup(model.getObject()
								.getAmpDonorOrgId().getOrgGrpId().getAmpOrgGrpId());
					};
				};
				
				AmpSelectFieldPanel<FundingPledges> pledgeSelector = new AmpSelectFieldPanel<FundingPledges>("pledge",
						new PropertyModel<FundingPledges>(item.getModel(),
								"pledgeid"), pledgesModel,
						"Pledges", false, true, new ChoiceRenderer<FundingPledges>() {
							@Override
							public Object getDisplayValue(FundingPledges arg0) {
								return arg0.getTitle();
							}
						}, true);
				pledgeSelector.getChoiceContainer().add(new AttributeAppender("style", new Model<String>("width: "+SELECTOR_SIZE+"px")));
				item.add(pledgeSelector);
				item.add(new ListEditorRemoveButton("delDisbursement", "Delete Disbursement"){
					protected void onClick(final org.apache.wicket.ajax.AjaxRequestTarget target) {
						AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
						super.onClick(target);
						parent.getFundingInfo().checkChoicesRequired(list.getCount());
						target.add(parent.getFundingInfo());
						target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
						target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
						updateModel();
						
						parent.visitChildren(AmpCollectionValidatorField.class, new IVisitor<AmpCollectionValidatorField, Void>(){
							@Override
							public void component(
									AmpCollectionValidatorField component,
									IVisit<Void> visit) {
								component.reloadValidationField(target);
								target.add(component.getParent());
								visit.dontGoDeeper();
							}
						});
					};
				});
			}
		};
		add(list);

	}

}
