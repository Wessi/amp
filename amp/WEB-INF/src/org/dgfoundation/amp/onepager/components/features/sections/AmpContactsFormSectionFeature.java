/**
 * 
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContactsSubsectionFeaturePanel;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;

/**
 * @author dan
 *
 */
public class AmpContactsFormSectionFeature extends AmpFormSectionFeaturePanel {
	
	private static final long serialVersionUID = 30916024623677185L;
	
	public AmpContactsFormSectionFeature(String id, String fmName, IModel<AmpActivity> am) throws Exception {
		super(id, fmName, am);
		
		//AmpContactsSubsectionFeaturePanel donors =new AmpContactsSubsectionFeaturePanel("donorContactInformation", "Donor Contact Information", am, org.digijava.module.aim.helper.Constants.DONOR_CONTACT);
		//donors.setOutputMarkupId(true);
		//add(donors);
		add(new AmpContactsSubsectionFeaturePanel("donorContactInformation", "Donor Contact Information", am, org.digijava.module.aim.helper.Constants.DONOR_CONTACT));
		add(new AmpContactsSubsectionFeaturePanel("mofedContactInformation", "Mofed Contact Information", am, org.digijava.module.aim.helper.Constants.MOFED_CONTACT));
		add(new AmpContactsSubsectionFeaturePanel("projectCoordinatorContactInformation", "Project Coordinator Contact Information", am, org.digijava.module.aim.helper.Constants.SECTOR_MINISTRY_CONTACT));
		add(new AmpContactsSubsectionFeaturePanel("sectorMinistryContactInformation", "Sector Ministry Contact Information", am, org.digijava.module.aim.helper.Constants.PROJECT_COORDINATOR_CONTACT));
		add(new AmpContactsSubsectionFeaturePanel("implExecAgencyContactInformation", "Implementing/Executing Agency Contact Information", am, org.digijava.module.aim.helper.Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT));
 
				
	}
	

}
