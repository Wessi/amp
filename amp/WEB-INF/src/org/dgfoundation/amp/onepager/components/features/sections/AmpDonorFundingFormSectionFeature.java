/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpOrgRoleSelectorComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingGroupFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpProposedProjectCost;
import org.dgfoundation.amp.onepager.events.DonorFundingRolesEvent;
import org.dgfoundation.amp.onepager.models.AmpFundingGroupModel;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;

/**
 * The donor funding section of the activity form. Includes selecting an org,
 * adding funding item, showing already added items
 * 
 * 
 * @author mpostelnicu@dgateway.org since Nov 3, 2010
 */
public class AmpDonorFundingFormSectionFeature extends
		AmpFormSectionFeaturePanel {
	private static final long serialVersionUID = 1L;
	private TreeMap<AmpOrganisation, AmpFundingGroupFeaturePanel> listItems = new TreeMap<AmpOrganisation, AmpFundingGroupFeaturePanel>();
	protected ListEditor<AmpOrganisation> list;
	private IModel<Set<AmpOrganisation>> setModel;
    private IModel<Set<AmpOrgRole>> roleModel;
	private AbstractReadOnlyModel<List<AmpFunding>> listModel;
	private PropertyModel<Set<AmpFunding>> fundingModel;
	private AmpAjaxLinkField addNewFunding;
	private AmpOrgRoleSelectorComponent orgRoleSelector;

	public ListEditor<AmpOrganisation> getList() {
		return list;
	}

	public IModel<Set<AmpOrganisation>> getSetModel() {
		return setModel;
	}
	
	public void switchOrg(ListItem item, AmpFunding funding, AmpOrganisation newOrg, AmpRole role, AjaxRequestTarget target) {
		
		AmpFundingGroupFeaturePanel existingFundGrp = listItems.get(funding.getAmpDonorOrgId());
			
		existingFundGrp.getList().remove(item);
		existingFundGrp.getList().updateModel();
		
		funding.setAmpDonorOrgId(newOrg);
		funding.setSourceRole(role);
		
		if (listItems.containsKey(newOrg)){
			AmpFundingGroupFeaturePanel fg = listItems.get(newOrg);
			fg.getList().addItem(funding);
		}
		else{
			fundingModel.getObject().add(funding);
			list.origAddItem(newOrg);
		}
		
		target.add(list.getParent());
		target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(list.getParent()));
	}

	public void updateFundingGroups(AmpOrganisation missing, AjaxRequestTarget target){
		Iterator<AmpFunding> it = fundingModel.getObject().iterator();
		boolean found = false;
		while (it.hasNext()) {
			AmpFunding funding = (AmpFunding) it.next();
			AmpOrganisation org = funding.getAmpDonorOrgId();
			if (missing.getAmpOrgId().equals(org.getAmpOrgId())){
				found = true;
				break;
			}
		}
		
		if (!found){
            //remove the org group
			int idx = -1;
			ListItem<AmpOrganisation> delItem = null;
			for (int i = 0; i < list.size(); i++){
				ListItem<AmpOrganisation> item = (ListItem<AmpOrganisation>) list.get(i);
				AmpOrganisation org = item.getModelObject();
				if (missing.getAmpOrgId().equals(org.getAmpOrgId())){
					idx = item.getIndex();
					delItem = item;
				}
			}
			if (idx > -1){
				for (int i = idx + 1; i < list.size(); i++){
					ListItem< ? > item = (ListItem< ? >)list.get(i);
					item.setIndex(item.getIndex() - 1);
				}
				
				list.items.remove(idx);
				list.updateModel();
				target.add(list.getParent());
				list.remove(delItem);
				listItems.remove(missing);
			}
            //also remove the org role
            Set<AmpOrgRole> roles = roleModel.getObject();
            for (Iterator<AmpOrgRole> it2 = roles.iterator(); it2.hasNext();){
                AmpOrgRole role = it2.next();
                if (role.getRole().getRoleCode().equals(Constants.FUNDING_AGENCY) && role.getOrganisation().getAmpOrgId().equals(missing.getAmpOrgId())){
                    it2.remove();
                    send(getPage(), Broadcast.BREADTH, new DonorFundingRolesEvent(target));
                    break;
                }
            }

		}
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
		
		
		//group fields in FM under "Proposed Project Cost"
		AmpProposedProjectCost propProjectCost = new AmpProposedProjectCost("propProjCost", "Proposed Project Cost", am);
		add(propProjectCost);

		fundingModel = new PropertyModel<Set<AmpFunding>>(am, "funding");
		if (fundingModel.getObject() == null)
			fundingModel.setObject(new LinkedHashSet<AmpFunding>());
		
		setModel = new AmpFundingGroupModel(fundingModel);
        roleModel = new PropertyModel<Set<AmpOrgRole>>(am, "orgrole");

        final WebMarkupContainer wmc = new WebMarkupContainer("container");
        wmc.setOutputMarkupId(true);
        add(wmc);

		list = new ListEditor<AmpOrganisation>("listFunding", setModel) {
			@Override
			protected void onPopulateItem(ListItem<AmpOrganisation> item) {
				AmpFundingGroupFeaturePanel fg = new AmpFundingGroupFeaturePanel("fundingItem", "Funding Group", fundingModel, item.getModel(), am, AmpDonorFundingFormSectionFeature.this);
				listItems.put(item.getModelObject(), fg);
				item.add(fg);
			}
			
			@Override
			public void addItem(AmpOrganisation org) {
				AmpFunding funding = new AmpFunding();
				if(orgRoleSelector.getRoleSelect().getChoiceContainer().getModelObject()!=null)
					funding.setSourceRole((AmpRole)orgRoleSelector.getRoleSelect().getChoiceContainer().getModelObject());
				funding.setAmpDonorOrgId(org);
				funding.setAmpActivityId(am.getObject());
				funding.setMtefProjections(new HashSet<AmpFundingMTEFProjection>());
				funding.setFundingDetails(new HashSet<AmpFundingDetail>());
				funding.setGroupVersionedFunding(System.currentTimeMillis());
				list.updateModel();

				if (listItems.containsKey(org)){
					AmpFundingGroupFeaturePanel fg = listItems.get(org);
					fg.getList().addItem(funding);
				}
				else{
					if (fundingModel.getObject() == null)
						fundingModel.setObject(new LinkedHashSet<AmpFunding>());
					
					fundingModel.getObject().add(funding);
					super.addItem(org);
				}

                //check if donor role for this org has been added, if not then add it
                boolean found = false;
                Set<AmpOrgRole> orgRoles = roleModel.getObject();
                for (AmpOrgRole role: orgRoles)
                    if (role.getRole().getRoleCode().equals(Constants.FUNDING_AGENCY) && role.getOrganisation().getAmpOrgId().equals(org.getAmpOrgId())){
                        found = true;
                        break;
                    }
                if (!found){
                    AmpOrgRole role = new AmpOrgRole();
                    role.setOrganisation(org);
                    role.setActivity(am.getObject());
                    role.setRole(DbUtil.getAmpRole(Constants.FUNDING_AGENCY));
                    orgRoles.add(role);
                }
			}
		};
		wmc.add(list);

		
//		final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchAutocomplete","Search Organizations", true, AmpOrganisationSearchModel.class) {
//			@Override
//			protected String getChoiceValue(AmpOrganisation choice) {
//				return DbUtil.filter(choice.getName());
//			}
//			
//			@Override
//			protected boolean showAcronyms() {
//				return true;
//			}
//			
//			@Override
//			protected String getAcronym(AmpOrganisation choice) {
//				return choice.getAcronym();
//			}
//
//			@Override
//			public void onSelect(AjaxRequestTarget target,
//					AmpOrganisation choice) {
//				list.addItem(choice);
//
//				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
//				target.add(wmc);
//			}
//
//			@Override
//			public Integer getChoiceLevel(AmpOrganisation choice) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		};

//		AmpSearchOrganizationComponent<String> searchOrganization = new AmpSearchOrganizationComponent<String>("searchFundingOrgs", new Model<String> (),
//				"Search Funding Organizations",   searchOrgs );
//		wmc.add(searchOrganization);

		
		orgRoleSelector = new AmpOrgRoleSelectorComponent("orgRoleSelector", am, new String[]{Constants.FUNDING_AGENCY});
		add(orgRoleSelector);
		
		// when the org select changes, update the status of the addNewFunding
		// button, enable it if there is a selection made, disable it otherwise
		orgRoleSelector.getOrgSelect().getChoiceContainer().add(
				new AjaxFormComponentUpdatingBehavior("onchange") {

					private static final long serialVersionUID = 2964092433905217073L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						if (orgRoleSelector.getOrgSelect().getChoiceContainer().getModelObject() == null)
							addNewFunding.getButton().setEnabled(false);
						else
							addNewFunding.getButton().setEnabled(true);
						target.add(addNewFunding);
					}
				});

		

		// button used to add funding based on the selected organization and
		// role
		addNewFunding = new AmpAjaxLinkField("addNewFuding",
				"New Funding Group", "New Funding") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target) {
				list.addItem((AmpOrganisation) orgRoleSelector.getOrgSelect().getChoiceContainer()
						.getModelObject());
				target.appendJavaScript(OnePagerUtil
						.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
				target.add(wmc);
			}
		};

		// by default this button is disabled, when the form first loads
		addNewFunding.getButton().setEnabled(false);
		add(addNewFunding);
	}

}
