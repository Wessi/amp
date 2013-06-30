/**
 * Copyright (c) 2012 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.Set;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.models.AbstractMixedSetModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;


/**
 * @author aartimon@dginternational.org since Jul 4, 2012
 */
public class AmpFundingGroupFeaturePanel extends AmpFeaturePanel<AmpOrganisation> {
	private static final long serialVersionUID = 1L;
	private ListEditor<AmpFunding> list;
	private IModel<AmpOrganisation> fundingOrgModel; 
	
	public ListEditor<AmpFunding> getList() {
		return list;
	}

	@Override
	protected void onConfigure() {
		AmpAuthWebSession session = (AmpAuthWebSession) getSession();
		if (fundingOrgModel != null && fundingOrgModel.getObject() != null){
			FundingOrganization fo = new FundingOrganization();
			fo.setAmpOrgId(fundingOrgModel.getObject().getAmpOrgId());                                                     
			PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_ORG, fo);
			PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_ORG_ROLE, Constants.FUNDING_AGENCY);
		}
		super.onConfigure();
		if (fundingOrgModel != null && fundingOrgModel.getObject() != null){
			PermissionUtil.removeFromScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_ORG);
			PermissionUtil.removeFromScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_ORG_ROLE);
		}
	}

	public AmpFundingGroupFeaturePanel(String id, String fmName,
			IModel<Set<AmpFunding>> fundsModel, final IModel<AmpOrganisation> model,final IModel<AmpActivityVersion> am, final AmpDonorFundingFormSectionFeature parent) {
		super(id, model, fmName, true);
		fundingOrgModel = model;
		add(new Label("donorOrg", model.getObject().getName()));
        AbstractMixedSetModel<AmpFunding> setModel = new AbstractMixedSetModel<AmpFunding>(fundsModel) {
            @Override
            public boolean condition(AmpFunding item) {
                return item.getAmpDonorOrgId().getAmpOrgId().compareTo(model.getObject().getAmpOrgId()) == 0;
            }
        };
		
		list = new ListEditor<AmpFunding>("listFunding", setModel) {
			@Override
			protected void onPopulateItem(
					org.dgfoundation.amp.onepager.components.ListItem<AmpFunding> item) {
				AmpFundingItemFeaturePanel fundingItemFeature;
				try {
					fundingItemFeature = new AmpFundingItemFeaturePanel(
							"fundingItem", "Funding Item",
							item.getModel(), am, parent);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				item.add(fundingItemFeature);
			}
		};
		add(list);
		
	}
}
