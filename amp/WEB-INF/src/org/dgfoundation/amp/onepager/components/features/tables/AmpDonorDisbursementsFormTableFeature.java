/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpDonorDisbursementsFormTableFeature extends
		AmpDonorFormTableFeaturePanel {

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpDonorDisbursementsFormTableFeature(String id,
			final IModel<AmpFunding> model, String fmName) throws Exception {
		super(id, model, fmName, Constants.DISBURSEMENT, 8);

		AbstractReadOnlyModel<List<AmpFundingDetail>> listModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel);

		
		final AbstractReadOnlyModel<List<String>> disbOrderIdModel = new AbstractReadOnlyModel<List<String>>() {
			@Override
			public List<String> getObject() {
				List<String> ret=new ArrayList<String>(); 
				for (AmpFundingDetail ampFundingDetail : parentModel.getObject()) 
					if(ampFundingDetail.getTransactionType().equals(Constants.DISBURSEMENT_ORDER)) ret.add(ampFundingDetail.getDisbOrderId());
				return ret;
			}
		};
		
		
		list = new ListView<AmpFundingDetail>("listDisbursements", listModel) {

			@Override
			protected void populateItem(final ListItem<AmpFundingDetail> item) {
				item.add(getAdjustmentTypeComponent(item.getModel()));
				item.add(getFundingAmountComponent(item.getModel()));

				item.add(new AmpSelectFieldPanel<String>("disbOrderId",
						new PropertyModel<String>(item.getModel(),
								"disbOrderId")
								,disbOrderIdModel,
						"Pledges", true, true));
				
				item.add(new AmpSelectFieldPanel<IPAContract>("contract",
						new PropertyModel<IPAContract>(item.getModel(),
								"contract"),
						new ArrayList<IPAContract>(model.getObject()
								.getAmpActivityId().getContracts()),
						"Contract", true, true));

				item.add(new AmpSelectFieldPanel<FundingPledges>("pledge",
						new PropertyModel<FundingPledges>(item.getModel(),
								"pledgeid"), PledgesEntityHelper
								.getPledgesByDonor(model.getObject()
										.getAmpDonorOrgId().getAmpOrgId()),
						"Pledges", true, true));

				item.add(getDeleteLinkField("delDisbursement",
						"Delete Disbursement Order", item));
			}
		};
		list.setReuseItems(true);
		add(list);

	}

}
