/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;

/**
 * @author aartimon@dginternational.org
 * since Oct 19, 2010
 */
public class AmpCommentTabsFieldWrapper extends AmpFieldPanel {

	public AmpCommentTabsFieldWrapper(String id, String fmName, List<ITab> tabs) {
		super(id, fmName);
		AjaxTabbedPanel atp = new AjaxTabbedPanel("tabs", tabs);
		atp.setOutputMarkupId(true);
		add(atp);
	}

}
