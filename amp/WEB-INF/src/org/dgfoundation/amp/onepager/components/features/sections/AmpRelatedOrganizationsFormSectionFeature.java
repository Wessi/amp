/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpRelatedOrganizationsFormTableFeature;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.helper.Constants;

/**
 * @author aartimon@dginternational.org since Oct 26, 2010
 */
public class AmpRelatedOrganizationsFormSectionFeature extends
		AmpFormSectionFeaturePanel {

	private static final long serialVersionUID = -6654390083784446344L;

	public AmpRelatedOrganizationsFormSectionFeature(String id, String fmName,
			final IModel<AmpActivity> am) throws Exception {
		super(id, fmName, am);
		add(new AmpRelatedOrganizationsFormTableFeature(
				"responsibleOrganization", "Responsible Organization", am, Constants.RESPONSIBLE_ORGANISATION));
		
		add(new AmpRelatedOrganizationsFormTableFeature(
				"implementingAgency", "Implementing Agency", am, Constants.IMPLEMENTING_AGENCY));

		add(new AmpRelatedOrganizationsFormTableFeature(
				"beneficiaryAgency", "Beneficiary Agency", am, Constants.BENEFICIARY_AGENCY));
		
		add(new AmpRelatedOrganizationsFormTableFeature(
				"contractingAgency", "Contracting Agency", am, Constants.CONTRACTING_AGENCY));
		
		add(new AmpRelatedOrganizationsFormTableFeature(
				"regionalGroup", "Regional Group", am, Constants.REGIONAL_GROUP));
		
		add(new AmpRelatedOrganizationsFormTableFeature(
				"sectorGroup", "Sector Group", am, Constants.SECTOR_GROUP));
		
	}

}
