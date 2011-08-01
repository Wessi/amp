/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Implements the aspect of a form section. A section is actually a {@link AmpFMTypes#FEATURE}
 * but it has a special way of displaying itself. Its title is with blue background and the whole section
 * is a white table on the gray background of the form
 * @author mpostelnicu@dgateway.org
 * since Oct 20, 2010
 */
public class AmpFormSectionFeaturePanel extends AmpFeaturePanel {
	
	
	protected final IModel<AmpActivityVersion> am;

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpFormSectionFeaturePanel(String id, String fmName,final IModel<AmpActivityVersion> am)
			throws Exception {
		super(id, fmName);	
		this.am = am;
		this.labelContainer.add(new SimpleAttributeModifier("id", id));
		this.labelContainer.add(new SimpleAttributeModifier("name", id));
	}

}
