/**
 * Copyright (c) 2012 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpComponentAnnualBudgetSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpOverallFundingTotalsTable;
import org.dgfoundation.amp.onepager.events.OverallFundingTotalsEvents;
import org.dgfoundation.amp.onepager.events.ProposedProjectCostUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.models.ProposedProjectCostModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Encaspulates an ajax link of type {@link AjaxLink}
 * 
 * @author aartimon@dginternational.org
 * @since Jan 11, 2012
 */
public class AmpProposedProjectCost extends AmpComponentPanel<Void> implements AmpRequiredComponentContainer {

	private static final long serialVersionUID = 3042844165981373432L;
	protected IndicatingAjaxLink button;
	private List<FormComponent<?>> requiredFormComponents = new ArrayList<FormComponent<?>>();

	public AmpProposedProjectCost(String id, String fmName, IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName);

		final AmpTextFieldPanel<Double> amount;
		if (!FeaturesUtil.isVisibleModule("/Activity Form/Funding/Proposed Project Cost/Annual Proposed Project Cost")) {
			amount = new AmpTextFieldPanel<Double>("proposedAmount", new PropertyModel<Double>(am,
					CategoryConstants.PROPOSE_PRJC_AMOUNT_KEY), CategoryConstants.PROPOSE_PRJC_AMOUNT_NAME, false) {
				public IConverter getInternalConverter(java.lang.Class<?> type) {
					DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
					NumberFormat formatter = FormatHelper.getDecimalFormat(true);
					converter.setNumberFormat(getLocale(), formatter);
					return converter;
				}

				@Override
				protected void onAjaxOnUpdate(AjaxRequestTarget target) {
					super.onAjaxOnUpdate(target);
					if (!this.getTextContainer().isRequired()) {
						((AmpDatePickerFieldPanel) this.getParent().get("proposedDate")).getDate().setRequired(false);
						String js = String.format("$('#%s').change();", ((AmpDatePickerFieldPanel) this.getParent()
								.get("proposedDate")).getDate().getMarkupId());
						target.appendJavaScript(js);
					} else if (this.getTextContainer().isRequired()
							&& this.getTextContainer().getModel().getObject() != null) {
						((AmpDatePickerFieldPanel) this.getParent().get("proposedDate")).getDate().setRequired(true);
						String js = String.format("$('#%s').change();", ((AmpDatePickerFieldPanel) this.getParent()
								.get("proposedDate")).getDate().getMarkupId());
						target.appendJavaScript(js);
					}
				}

			};
			amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("12")));
			add(amount);
		} else {
			amount = new AmpTextFieldPanel<Double>("proposedAmount", new ProposedProjectCostModel(
					new PropertyModel<Double>(am, "funAmount"), new PropertyModel<Set<AmpAnnualProjectBudget>>(am,
							"annualProjectBudgets")), "Amount", false) {
				public IConverter getInternalConverter(java.lang.Class<?> type) {
					DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
					NumberFormat formatter = FormatHelper.getDecimalFormat(true);
					converter.setNumberFormat(getLocale(), formatter);
					return converter;
				}

				@Override
				protected void onAjaxOnUpdate(AjaxRequestTarget target) {
					super.onAjaxOnUpdate(target);
					if (!this.getTextContainer().isRequired()) {
						((AmpDatePickerFieldPanel) this.getParent().get("proposedDate")).getDate().setRequired(false);
						String js = String.format("$('#%s').change();", ((AmpDatePickerFieldPanel) this.getParent()
								.get("proposedDate")).getDate().getMarkupId());
						target.appendJavaScript(js);
					} else if (this.getTextContainer().isRequired()
							&& this.getTextContainer().getModel().getObject() != null) {
						((AmpDatePickerFieldPanel) this.getParent().get("proposedDate")).getDate().setRequired(true);
						String js = String.format("$('#%s').change();", ((AmpDatePickerFieldPanel) this.getParent()
								.get("proposedDate")).getDate().getMarkupId());
						target.appendJavaScript(js);
					}

				}

			};
			amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("12")));
			amount.add(UpdateEventBehavior.of(ProposedProjectCostUpdateEvent.class));
			amount.getTextContainer().add(new AttributeModifier("readonly", new Model<String>("readonly")));
			add(amount);
		}

		AbstractReadOnlyModel<List<String>> currencyList = new AbstractReadOnlyModel<List<String>>() {
			@Override
			public List<String> getObject() {
				List<AmpCurrency> tmp = (List<AmpCurrency>) CurrencyUtil.getActiveAmpCurrencyByCode();
				ArrayList<String> ret = new ArrayList<String>();

				Iterator<AmpCurrency> it = tmp.iterator();
				while (it.hasNext()) {
					AmpCurrency c = (AmpCurrency) it.next();
					ret.add(c.getCurrencyCode());
				}
				return ret;
			}
		};

		PropertyModel<String> currencyModel = new PropertyModel<String>(am, "currencyCode");

		if (currencyModel.getObject() == null) {
			currencyModel.setObject(CurrencyUtil.getWicketWorkspaceCurrency().getCurrencyCode());
		}

		AmpSelectFieldPanel<String> currency = new AmpSelectFieldPanel<String>("proposedCurrency", currencyModel,
				currencyList, "Currency", false, false);
		add(currency);
		AmpComponentAnnualBudgetSubsectionFeature annualBudgets;
		try {
			annualBudgets = new AmpComponentAnnualBudgetSubsectionFeature("annualProposedCost", am,
					"Annual Proposed Project Cost");
			add(annualBudgets);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final PropertyModel<Date> funDateModel = new PropertyModel<Date>(am, CategoryConstants.PROPOSE_PRJC_DATE_KEY);
		final AmpDatePickerFieldPanel date = new AmpDatePickerFieldPanel("proposedDate", funDateModel, null,
				CategoryConstants.PROPOSE_PRJC_DATE_NAME);

		// validator for poposed project cost amount AMP-17254
		add(new AmpComponentPanel("proposedAmountRequired", "Required Validator for "
				+ CategoryConstants.PROPOSE_PRJC_AMOUNT_NAME) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				if (this.isVisible()) {
					amount.getTextContainer().setRequired(true);
					date.getDate().setRequired(true);
					requiredFormComponents.add(date.getDate());
					// date.getDate().setRequired(true);
					requiredFormComponents.add(amount.getTextContainer());
				}
			}
		});
		// the amount should be added to the form AFTER the validator so it gets
		// the star
		// add(amount);
		add(date);
		
		RangeValidator<Integer> rangeValidator = new RangeValidator<Integer>(1,
				10);
		AttributeModifier attributeModifier = new AttributeModifier("size",
				new Model(1));
		AmpTextFieldPanel<Integer> fundingSourcesNumberPanel = new AmpTextFieldPanel<Integer>(
				"fundingSourcesNumber", new PropertyModel<Integer>(am,
						"fundingSourcesNumber"),
				CategoryConstants.FUNDING_SOURCES_NUMBER_NAME,
				AmpFMTypes.MODULE);
		fundingSourcesNumberPanel.getTextContainer().add(rangeValidator);
		fundingSourcesNumberPanel.getTextContainer().add(attributeModifier);
		add(fundingSourcesNumberPanel);

		AmpCategorySelectFieldPanel typeOfCooperation = new AmpCategorySelectFieldPanel(
				"typeOfCooperation", CategoryConstants.TYPE_OF_COOPERATION_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"),
						CategoryConstants.TYPE_OF_COOPERATION_KEY),
				CategoryConstants.TYPE_OF_COOPERATION_NAME, true, false, null,
				AmpFMTypes.MODULE);
		add(typeOfCooperation);

		AmpCategorySelectFieldPanel typeOfImplementation = new AmpCategorySelectFieldPanel(
				"typeOfImplementation",
				CategoryConstants.TYPE_OF_IMPLEMENTATION_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"),
						CategoryConstants.TYPE_OF_IMPLEMENTATION_KEY),
				CategoryConstants.TYPE_OF_IMPLEMENTATION_NAME, true, false,
				null, AmpFMTypes.MODULE);
		add(typeOfImplementation);

		AmpCategorySelectFieldPanel modalities = new AmpCategorySelectFieldPanel(
				"modalities",
				CategoryConstants.MODALITIES_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"), CategoryConstants.MODALITIES_KEY),
				CategoryConstants.MODALITIES_NAME, true, false, null,
				AmpFMTypes.MODULE);
		add(modalities);		
		
		AmpOverallFundingTotalsTable overallFunding = new AmpOverallFundingTotalsTable(
				"overallFunding", "Overall Funding Totals", new PropertyModel<Set<AmpFunding>>(am, "funding"));
		overallFunding.add(UpdateEventBehavior.of(OverallFundingTotalsEvents.class));

		add(overallFunding);
	}
	 
	/**
	 * Return a list of FormComponent that are marked as required for this panel
	 * 
	 * @return List<FormComponent<?>> with the FormComponent
	 */
	public List<FormComponent<?>> getRequiredFormComponents() {
		return requiredFormComponents;
	}
}
