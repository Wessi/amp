/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.text.NumberFormat;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converters.DoubleConverter;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorCommitmentsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpDonorCommitmentsFormTableFeature extends
		AmpDonorFormTableFeaturePanel {

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param ampDonorCommitmentsSubsectionFeature 
	 * @throws Exception
	 */
	public AmpDonorCommitmentsFormTableFeature(String id,
			final IModel<AmpFunding> model, String fmName) throws Exception {
		super(id, model, fmName, Constants.COMMITMENT, 7);

		AbstractReadOnlyModel<List<AmpFundingDetail>> listModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel,new AmpFundingDetail.FundingDetailComparator());

		list = new ListView<AmpFundingDetail>("listCommitments", listModel) {

			@Override
			protected void populateItem(final ListItem<AmpFundingDetail> item) {

				item.add(getAdjustmentTypeComponent(item.getModel()));
				item.add(getFundingAmountComponent(item.getModel()));

				IModel<List<FundingPledges>> pledgesModel = new LoadableDetachableModel<List<FundingPledges>>() {
					protected java.util.List<FundingPledges> load() {
						return PledgesEntityHelper
								.getPledgesByDonor(model.getObject()
								.getAmpDonorOrgId().getAmpOrgId()); 
					};
				};
				
				
				AmpTextFieldPanel<Double> exchangeRate = new AmpTextFieldPanel<Double>("exchangeRate",
						new PropertyModel<Double>(item.getModel(), "fixedExchangeRate"), "exchangeRate",true);
	
				exchangeRate.getTextContainer().add(new MinimumValidator<Double>(0.001d));
				exchangeRate.getTextContainer().add(new AttributeModifier("size", true, new Model<String>("6")));
				item.add(exchangeRate);
				
				item.add(new AmpSelectFieldPanel<FundingPledges>("pledge",
						new PropertyModel<FundingPledges>(item.getModel(),
								"pledgeid"), pledgesModel,
						"Pledges", true, true, new ChoiceRenderer<FundingPledges>() {
							@Override
							public Object getDisplayValue(FundingPledges arg0) {
								return arg0.getTitle();
							}
						}));

				item.add(getDeleteLinkField("delCommitment",
						"Delete Commitment", item));

			}
		};
		list.setReuseItems(true);
		add(list);

	}

}
