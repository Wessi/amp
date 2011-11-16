/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.lang.Double;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converters.DoubleConverter;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpMinSizeCollectionValidationField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * The donor funding section of the activity form. Includes selecting an org,
 * adding funding item, showing already added items
 * 
 * @author mpostelnicu@dgateway.org since Nov 3, 2010
 */
public class AmpDonorFundingFormSectionFeature extends
		AmpFormSectionFeaturePanel {

	protected ListView<AmpFunding> list;
	private IModel<Set<AmpFunding>> setModel;
	private AbstractReadOnlyModel<List<AmpFunding>> listModel;

	public ListView<AmpFunding> getList() {
		return list;
	}

	public IModel<Set<AmpFunding>> getSetModel() {
		return setModel;
	}

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpDonorFundingFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		setModel = new PropertyModel<Set<AmpFunding>>(
				am, "funding");
		if (setModel.getObject() == null)
			setModel.setObject(new LinkedHashSet<AmpFunding>());

		AmpFundingAmountComponent<AmpActivityVersion> funding = new AmpFundingAmountComponent<AmpActivityVersion>("proposedAmount",
				am, "Amount", "funAmount", "Currency",
				"currencyCode", "Date", "funDate");
		add(funding);
		listModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel);

		list = new ListView<AmpFunding>("listFunding", listModel) {
			@Override
			protected void populateItem(final ListItem<AmpFunding> item) {
				AmpFundingItemFeaturePanel fundingItemFeature;
				try {
					fundingItemFeature = new AmpFundingItemFeaturePanel(
							"fundingItem", "Funding Item",
							item.getModel(),am,AmpDonorFundingFormSectionFeature.this);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				item.add(fundingItemFeature);

				String translatedMessage = TranslatorUtil.getTranslation("Do you really want to delete this funding item?");
				AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
						"delFunding", "Delete Funding Item",new Model<String>(translatedMessage)) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(AmpDonorFundingFormSectionFeature.this);
						target.appendJavascript(OnePagerUtil.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
						list.removeAll();
					}
				};
				item.add(deleteLinkField);

			}
		};

		list.setReuseItems(true);
		add(list);

		
		final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchFundingOrgs","Search Funding Organizations",AmpOrganisationSearchModel.class) {			
			@Override
			protected String getChoiceValue(AmpOrganisation choice) {
				return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target,
					AmpOrganisation choice) {
				AmpFunding funding = new AmpFunding();
				funding.setAmpDonorOrgId(choice);
				funding.setAmpActivityId(am.getObject());

				funding.setMtefProjections(new HashSet<AmpFundingMTEFProjection>());
				funding.setFundingDetails(new HashSet<AmpFundingDetail>());
				funding.setGroupVersionedFunding(System.currentTimeMillis());

				setModel.getObject().add(funding);
				list.removeAll();
				target.addComponent(AmpDonorFundingFormSectionFeature.this);
				target.appendJavascript(OnePagerUtil.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		add(searchOrgs);

	}

}
