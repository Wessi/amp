/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionsSumComparatorValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpTransactionTypeDonorFundingDetailModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpDonorExpendituresFormTableFeature extends
		AmpDonorFormTableFeaturePanel {

	private boolean alertIfExpenditureBiggerDisbursment = false;  
	
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param transactionType 
	 * @throws Exception
	 */
	public AmpDonorExpendituresFormTableFeature(String id,
			final IModel<AmpFunding> model, String fmName, final int transactionType) throws Exception {
		super(id, model, fmName, Constants.EXPENDITURE, 6);

		
		if("TRUE".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_EXPENDITURE_BIGGER_DISBURSMENT)))
			alertIfExpenditureBiggerDisbursment = true;

		AbstractReadOnlyModel<List<AmpFundingDetail>> setAmountListModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel);
		AbstractReadOnlyModel<List<AmpFundingDetail>> disbursementModel =  OnePagerUtil
				.getReadOnlyListModelFromSetModel(new AmpTransactionTypeDonorFundingDetailModel(parentModel, Constants.DISBURSEMENT));
		
		WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
		add(wmc);
		
		final AmpCollectionsSumComparatorValidatorField amountSumComparator=
				new AmpCollectionsSumComparatorValidatorField("amountSumComparator",setAmountListModel,"checkCommitmentSum", "AmpExpendituresCollectionsSumComparatorValidator");
		wmc.add(amountSumComparator.getIndicatorAppender());
		amountSumComparator.setSecondCollectionModel(disbursementModel);
		amountSumComparator.setAlertIfCurrentModelAmountSumBig(true);
        amountSumComparator.setOutputMarkupId(true);
		amountSumComparator.setVisibilityAllowed(alertIfExpenditureBiggerDisbursment);
		wmc.setVisibilityAllowed(alertIfExpenditureBiggerDisbursment);
		add(amountSumComparator);			
		
		
		list = new ListEditor<AmpFundingDetail>("listExp", setModel, new AmpFundingDetail.FundingDetailComparator()) {
			@Override
			protected void onPopulateItem(
					org.dgfoundation.amp.onepager.components.ListItem<AmpFundingDetail> item) {

				item.add(getAdjustmentTypeComponent(item.getModel(), transactionType));
				final AmpFundingAmountComponent amountComponent = getFundingAmountComponent(item.getModel());
                amountComponent.setAmountValidator(amountSumComparator);
				item.add(amountComponent);
				
				AmpTextFieldPanel<String> classification = new AmpTextFieldPanel<String>(
						"classification", new PropertyModel<String>(
								item.getModel(), "expCategory"),
						"Expenditure Classification", false, true);
				classification.getTextContainer().add(new AttributeModifier("size", new Model<String>("12")));
				classification.setTextContainerDefaultMaxSize();
				item.add(classification);
				item.add(new ListEditorRemoveButton("delExp", "Delete Expenditure"){
					protected void onClick(final org.apache.wicket.ajax.AjaxRequestTarget target) {
						AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
						super.onClick(target);
						if(alertIfExpenditureBiggerDisbursment)
						    amountSumComparator.reloadValidationField(target);
						parent.getFundingInfo().checkChoicesRequired(list.getCount());
						target.add(parent.getFundingInfo());
						target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
						target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
						
					//	if(alertIfExpenditureBiggerDisbursment) {
						   parent.visitChildren(AmpCollectionValidatorField.class,
								new IVisitor<AmpCollectionValidatorField, Void>() {
									@Override
									public void component(AmpCollectionValidatorField component,
											IVisit<Void> visit) {
										component.reloadValidationField(target);
										target.add(component.getParent());
										visit.dontGoDeeper();
									}
								});
						//   }
					};
				});
			}
		};
		add(list);

	}

}
