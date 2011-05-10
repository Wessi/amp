/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorDisbOrdersFormTableFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorDisbursementsFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpDonorDisbOrdersSubsectionFeature extends
		AmpSubsectionFeaturePanel<AmpFunding> {

	protected AmpDonorDisbOrdersFormTableFeature disbOrdersTableFeature;
	protected Random randomGenerator = new Random();
	protected AmpDonorDisbursementsSubsectionFeature disbursements;

	public AmpDonorDisbursementsSubsectionFeature getDisbursements() {
		return disbursements;
	}

	public void setDisbursements(
			AmpDonorDisbursementsSubsectionFeature disbursements) {
		this.disbursements = disbursements;
	}

	/**
	 * Refreshes the {@link AmpSelectFieldPanel}S from the
	 * {@link AmpDonorDisbursementsFormTableFeature} that has the name
	 * "disbOrderId" when the list of disbursement orders is changed
	 * (added/deleted).
	 * 
	 * @param target
	 */
	public void updateDisbOrderPickers(AjaxRequestTarget target) {
		Iterator<? extends ListItem<AmpFundingDetail>> iterator = disbursements
				.getDisbursementsTableFeature().getList().iterator();
		while (iterator.hasNext()) {
			ListItem<AmpFundingDetail> listItem = (ListItem<AmpFundingDetail>) iterator
					.next();
			Component component = listItem.get("disbOrderId");
			target.addComponent(component);
		}
	}

	/**
	 * Generates a unique random int between 0 and 100, to populate
	 * {@link AmpFundingDetail#getDisbOrderId()}
	 * 
	 * @param fundingDetails
	 * @return
	 */
	protected String generateNewDisbOrderId(
			Collection<AmpFundingDetail> fundingDetails) {
		String ret = null;
		boolean found = false;
		do {
			ret = Integer.toString(randomGenerator.nextInt(100));
			for (AmpFundingDetail ampFundingDetail : fundingDetails) {
				if (ret.equals(ampFundingDetail.getDisbOrderId())) {
					found = true;
					break;
				}
			}
		} while (found);
		return ret;
	}

	/**
	 * @param id
	 * @param fmName
	 * @param model
	 * @throws Exception
	 */
	public AmpDonorDisbOrdersSubsectionFeature(String id,
			final IModel<AmpFunding> model, String fmName, int transactionType)
			throws Exception {
		super(id, fmName, model);
		disbOrdersTableFeature = new AmpDonorDisbOrdersFormTableFeature(
				"disbOrdersTableFeature", model, "Disbursement Orders Table");
		add(disbOrdersTableFeature);

		AmpAjaxLinkField addCommit = new AmpAjaxLinkField("addDisbOrder",
				"Add Disbursement Order", "Add Disbursement Order") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				AmpFundingDetail fd = new AmpFundingDetail();
				fd.setAmpFundingId(model.getObject());
				fd.setTransactionAmount(0d);
				fd.setAdjustmentType(Constants.ACTUAL);
				fd.setTransactionDate(new Date(System.currentTimeMillis()));
				fd.setTransactionType(Constants.DISBURSEMENT_ORDER);
				Set fundingDetails = model.getObject().getFundingDetails();
				fd.setDisbOrderId(generateNewDisbOrderId(fundingDetails));
				fundingDetails.add(fd);
				disbOrdersTableFeature.getList().removeAll();
				target.addComponent(disbOrdersTableFeature);
				updateDisbOrderPickers(target);
			}
		};
		add(addCommit);
	}

}
