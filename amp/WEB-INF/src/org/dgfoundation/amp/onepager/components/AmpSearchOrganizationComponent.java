package org.dgfoundation.amp.onepager.components;


import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IOnChangeListener;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatedChoiceRenderer;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.DbUtil;



public class AmpSearchOrganizationComponent<T> extends AmpComponentPanel<T>  implements IOnChangeListener{

	private AmpSelectFieldPanel<AmpOrgGroup> orgGroupPanel;
	private AmpAutocompleteFieldPanel<AmpOrganisation> autocompletePanel;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AmpSearchOrganizationComponent(String id, IModel<T> model,
			String fmName,  final AmpAutocompleteFieldPanel<AmpOrganisation> autocompletePanel ) {
		super(id, model, fmName);
		//TrnLabel selectOrgTypeLabel = new TrnLabel("selectOrgTypeLabel", "Select Organization Type");
		//add(selectOrgTypeLabel);
		
		
		ChoiceRenderer cr = new ChoiceRenderer(){
			@Override
			public Object getDisplayValue(Object object) {
				if(object == null)
					return TranslatorUtil.getTranslatedText("All Groups");
				AmpOrgGroup orgGroup = (AmpOrgGroup)object;
			    return orgGroup.getOrgGrpName();
			}
		};
		IModel<List<? extends AmpOrgGroup>> orgGroupsModel = Model.ofList((List<AmpOrgGroup>) DbUtil.getAllOrgGroups());
		orgGroupPanel = new AmpSelectFieldPanel<AmpOrgGroup>("selectOrgType", new Model<AmpOrgGroup>(),  orgGroupsModel, "Select Organization Type", true, true, cr);

		orgGroupPanel.getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
				@Override
				protected  void onUpdate(AjaxRequestTarget target)
				{
					
					AmpOrgGroup org_group = (AmpOrgGroup) orgGroupPanel.getChoiceContainer().getModelObject();
					if(org_group != null)						
					   autocompletePanel.getModelParams().put(AmpOrganisationSearchModel.PARAM.GROUP_FILTER, org_group);
					else
						autocompletePanel.getModelParams().remove(AmpOrganisationSearchModel.PARAM.GROUP_FILTER);
					target.addComponent(autocompletePanel);
				}
				
		});
		add(orgGroupPanel);
	    
	    
		orgGroupPanel.setOutputMarkupId(true);
		this.autocompletePanel = autocompletePanel;
		add(autocompletePanel);
	}
	
	
	
	@Override
	public void onSelectionChanged() {
		Long id =  ((AmpOrgGroup)orgGroupPanel.getChoiceContainer().getModelObject()).getAmpOrgGrpId();
		autocompletePanel.getModelParams().put(AmpOrganisationSearchModel.PARAM.GROUP_FILTER,id);
	}

	
	

}
