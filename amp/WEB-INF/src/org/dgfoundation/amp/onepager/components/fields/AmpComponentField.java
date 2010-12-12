/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpComponentsFundingSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpComponentIdentificationFormTableFeature;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.helper.Constants;

/**
 * @author aartimon@dginternational.org
 * since Oct 27, 2010
 */
public class AmpComponentField extends AmpFieldPanel<Boolean>{

	private static final long serialVersionUID = 0L;
	private RadioChoice<Boolean> choiceContainer;

	public AmpComponentField(String id,	IModel<AmpActivity> activityModel, 
			IModel<AmpComponent> componentModel, String fmName){
		super(id,fmName, true);
		
		final PropertyModel<Set<AmpComponentFunding>> componentsSetModel=new 
		PropertyModel<Set<AmpComponentFunding>>(activityModel, "componentFundings");

		try {
			WebMarkupContainer componentFunding = new WebMarkupContainer("componentFunding");
			componentFunding.setVisible(false);
			componentFunding.setOutputMarkupId(true);
			componentFunding.setOutputMarkupPlaceholderTag(true);

			AmpComponentIdentificationFormTableFeature firstSection = 
				new AmpComponentIdentificationFormTableFeature("typeAndTitle", activityModel, 
						componentModel, componentFunding, "Component");
			add(firstSection);
			
			AmpComponentsFundingSubsectionFeature commitments = new AmpComponentsFundingSubsectionFeature("commitments", activityModel, 
					componentModel, componentsSetModel, "Components Commitments", 
					Constants.COMMITMENT);
			commitments.setOutputMarkupId(true);
			commitments.setOutputMarkupPlaceholderTag(true);
			componentFunding.add(commitments);
			
			AmpComponentsFundingSubsectionFeature disbursements = new AmpComponentsFundingSubsectionFeature("disbursements", activityModel, 
					componentModel, componentsSetModel, "Components Disbursements", 
					Constants.DISBURSEMENT);
			disbursements.setOutputMarkupId(true);
			disbursements.setOutputMarkupPlaceholderTag(true);
			componentFunding.add(disbursements);

			AmpComponentsFundingSubsectionFeature expeditures = new AmpComponentsFundingSubsectionFeature("expeditures", activityModel, 
					componentModel, componentsSetModel, "Components Expeditures", 
					Constants.EXPENDITURE);
			expeditures.setOutputMarkupId(true);
			expeditures.setOutputMarkupPlaceholderTag(true);
			componentFunding.add(expeditures);


			add(componentFunding);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
