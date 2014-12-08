package org.dgfoundation.amp.onepager.components.features.tables;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.helper.FormatHelper;

public class AmpComponentFormTableAnnualBudget
		extends
		AmpFundingFormTableFeaturePanel<AmpActivityVersion, AmpAnnualProjectBudget> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 732018195505777380L;

	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */

	public AmpComponentFormTableAnnualBudget(String id, String fmName,
			final IModel<AmpActivityVersion> model) throws Exception {
		super(id, model, fmName);

		getTableId().add(new AttributeModifier("width", "620"));

		final IModel<Set<AmpAnnualProjectBudget>> setModel = new PropertyModel<Set<AmpAnnualProjectBudget>>(
				model, "annualProjectBudgets");
		if (setModel.getObject() == null)
			setModel.setObject(new TreeSet<AmpAnnualProjectBudget>());
		setTitleHeaderColSpan(5);
		list = new ListEditor<AmpAnnualProjectBudget>("listAnnualBudget", setModel, new AmpAnnualProjectBudget.AmpAnnualProjectBudgerComparator()) {
			@Override
			protected void onPopulateItem(
					final org.dgfoundation.amp.onepager.components.ListItem<AmpAnnualProjectBudget> item) {
				final MarkupContainer listParent = this.getParent();

				final AmpTextFieldPanel<Double> amount = new AmpTextFieldPanel<Double>(
						"amountAnnualBudget", new PropertyModel<Double>(item.getModel(),
								"amount"), "Amount", false, false) {

					@Override
					protected void onAjaxOnUpdate(final AjaxRequestTarget target) {

						onFundingDetailChanged(target);
					}

					public IConverter getInternalConverter(
							java.lang.Class<?> type) {
						DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
						NumberFormat formatter = FormatHelper
								.getDecimalFormat(true);

						converter.setNumberFormat(getLocale(), formatter);
						return converter;
					}
				};
				amount.getTextContainer().setRequired(true);
				amount.setOutputMarkupId(true);
				amount.getTextContainer().add(
						new AttributeModifier("size", new Model<String>("9")));

				item.add(amount);

				AmpDatePickerFieldPanel datetmp = new AmpDatePickerFieldPanel(
						"dateAnnualBudget",
						new PropertyModel<Date>(item.getModel(), "year"),
						"Projection Date", null, false, false);
				datetmp.getDate().setRequired(true);
				datetmp.getDate().add(
						new AttributeModifier("class", "inputx_date"));
				Component date = datetmp;

				item.add(date);

				AmpDeleteLinkField delAnnualBudget = new AmpDeleteLinkField("delAnnualBudget",
						"Delete Internal Id") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.add(listParent);
						//boolean result = model.getObject().getAnnualProjectBudgets().removeAll(model.getObject().getAnnualProjectBudgets());
						//amount.getModel().setObject(- amount.getModel().getObject());
						
						int idx = item.getIndex();

						for (int i = idx + 1; i < item.getParent().size(); i++) {
							ListItem<?> listItem = (ListItem<?>) item.getParent().get(i);
							listItem.setIndex(listItem.getIndex() - 1);
						}
						 
						list.items.remove(item.getIndex());
						list.remove(item);
						onFundingDetailChanged(target);
						
						//list.updateModel();
					}
				};
				item.add(delAnnualBudget);

			}
		};
		add(list);
	}

	/**
	 * Method called when the amount field value has been changed
	 * 
	 * @param target
	 */
	protected void onFundingDetailChanged(AjaxRequestTarget target) {
		System.out.println("test");
	}

}