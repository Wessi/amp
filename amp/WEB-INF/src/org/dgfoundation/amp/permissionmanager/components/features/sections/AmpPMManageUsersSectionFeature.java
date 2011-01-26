/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMUserSearchModel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMManageUsersTableFeaturePanel;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author dan
 *
 */
public class AmpPMManageUsersSectionFeature extends AmpPMSectionFeaturePanel {

	protected ListView<User> idsList;
	protected boolean visible = true ;
	
	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMManageUsersSectionFeature(String id, String fmName, final IModel<Set<User>> usersModel)
			throws Exception {
		super(id, fmName);
		
		Set<AmpOrganisation> s = new TreeSet<AmpOrganisation>();
		final IModel<Set<AmpOrganisation>> allOrgsModel = new Model((Serializable)s);
		Set<User> t = new TreeSet<User>();
		final IModel<Set<User>> allUsersModel = new Model((Serializable)t);

		final AmpPMAssignVerifiedOrgs usersOrgs = new AmpPMAssignVerifiedOrgs("assignMultiUsersMultiOrgs",allOrgsModel, allUsersModel, "Assign Verified Organizations to Users", false);
		usersOrgs.getLabelContainer().add(new AttributeModifier("class",new Model("perm_h3")));
		add(usersOrgs);
		usersOrgs.setVisible(!visible);
		
		final AmpPMManageUsersTableFeaturePanel usersTable = new AmpPMManageUsersTableFeaturePanel("users", usersModel, "Users");
		add(usersTable);
		final PagingNavigator paginator = new PagingNavigator("navigator", (PageableListView)usersTable.getList());
		add(paginator);
		idsList = usersTable.getList();
		
		final AbstractAmpAutoCompleteTextField<User> autoComplete = new AbstractAmpAutoCompleteTextField<User>(AmpPMUserSearchModel.class) {

			@Override
			protected String getChoiceValue(User choice) throws Throwable {
				return choice.getName() +" - "+ choice.getEmail();
			}
			
			@Override
			public void onSelect(AjaxRequestTarget target, User choice) {
				Set<User> set = usersModel.getObject();
				set.clear();
				set.add(choice);
				idsList.removeAll();
				target.addComponent(AmpPMManageUsersSectionFeature.this);
				target.appendJavascript(OnePagerConst.getToggleJS(AmpPMManageUsersSectionFeature.this.getSliderPM()));
//				add(JavascriptPackageResource.getHeaderContribution(AmpSubsectionFeaturePanel.class, "subsectionSlideTogglePM.js"));
			//	target.appendJavascript(OnePagerConst.getToggleJS(AmpContactsSubsectionFeaturePanel.this.getSlider()));
			}

			@Override
			public Integer getChoiceLevel(User choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		AttributeModifier sizeModifier = new AttributeModifier("size",new Model(25));
		autoComplete.add(sizeModifier);
		final AmpComboboxFieldPanel<User> searchContacts=new AmpComboboxFieldPanel<User>("searchUsers", "Search Users", autoComplete,true);
		add(searchContacts);
		
		
		add(new Link("addOrgsToUsers"){
			@Override
			public void onClick() {
				visible = !visible;
				usersTable.setVisible(visible);
				paginator.setVisible(visible);
				searchContacts.setVisible(visible);
				usersOrgs.setVisible(!visible);
			}
			
		});
		
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLabel
	 * @throws Exception
	 */
	public AmpPMManageUsersSectionFeature(String id, IModel model,
			String fmName, boolean hideLabel) throws Exception {
		super(id, model, fmName, hideLabel);
		// TODO Auto-generated constructor stub
	}

}
