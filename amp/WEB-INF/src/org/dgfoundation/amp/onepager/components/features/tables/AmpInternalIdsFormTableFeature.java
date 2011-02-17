/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author mpostelnicu@dgateway.org
 * since Oct 20, 2010
 */
public class AmpInternalIdsFormTableFeature extends AmpFormTableFeaturePanel {

	protected ListView<AmpActivityInternalId> idsList;
	
	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpInternalIdsFormTableFeature(String id, String fmName,
			final IModel<AmpActivity> am) throws Exception {
		super(id, am, fmName);
		final IModel<Set<AmpActivityInternalId>> setModel=new PropertyModel<Set<AmpActivityInternalId>>(am,"internalIds");
		

		AbstractReadOnlyModel<List<AmpActivityInternalId>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(setModel);


		
		idsList = new ListView<AmpActivityInternalId>("listOrgs", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpActivityInternalId> item) {
				final MarkupContainer listParent=this.getParent();
				item.add(new TextField<String>(
						"internalId",
						new PropertyModel<String>(item.getModel(), "internalId")));
				
				item.add(new Label("orgNameLabel", item.getModelObject()
						.getOrganisation().getAcronymAndName()));			
				
				AmpDeleteLinkField delOrgId = new AmpDeleteLinkField(
						"delOrgId", "Delete Internal Id") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(listParent);
						idsList.removeAll();
					}
				};
				item.add(delOrgId);
				
			}
		};
		idsList.setReuseItems(true);
		add(idsList);

		final AbstractAmpAutoCompleteTextField<AmpOrganisation> autoComplete = new AbstractAmpAutoCompleteTextField<AmpOrganisation>(AmpOrganisationSearchModel.class) {
			private static final long serialVersionUID = 1227775244079125152L;

			@Override
			protected String getChoiceValue(AmpOrganisation choice)
					throws Throwable {
				return choice.getAcronymAndName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target,
					AmpOrganisation choice) {
				AmpActivityInternalId activityInternalId = new AmpActivityInternalId();
				activityInternalId.setOrganisation(choice);
				activityInternalId.setAmpActivity(am.getObject());
				Set<AmpActivityInternalId> set = setModel.getObject();
				set.add(activityInternalId);
				idsList.removeAll();
				target.addComponent(idsList.getParent());
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		final AmpComboboxFieldPanel<AmpOrganisation> searchOrgs=new AmpComboboxFieldPanel<AmpOrganisation>("searchOrgs", "Search Organizations", autoComplete);
		add(searchOrgs);
		
	}

}
