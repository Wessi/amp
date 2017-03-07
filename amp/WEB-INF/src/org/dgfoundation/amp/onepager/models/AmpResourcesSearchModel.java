/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;

import org.apache.wicket.Session;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.util.SessionUtil;
import org.digijava.kernel.request.Site;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author aartimon@dginternational.org
 * @since Aug 18, 2011
 */
public class AmpResourcesSearchModel extends
		AbstractAmpAutoCompleteModel<NodeWrapper> {

	public AmpResourcesSearchModel(String input,String language,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, language, params);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 8211300754918658832L;
	
	@Override
	protected Collection<NodeWrapper> load() {
		ArrayList<NodeWrapper> ret = new ArrayList<NodeWrapper>();
		try {
			AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();
			javax.jcr.Session jcrWriteSession = DocumentManagerUtil.getWriteSession(SessionUtil.getCurrentServletRequest());

			Node otherHomeNode = DocumentManagerUtil.getUserPrivateNode(jcrWriteSession, s.getCurrentMember());
			Iterator<Node> nit = otherHomeNode.getNodes();
			while (nit.hasNext()) {
				Node n = (Node) nit.next();
				NodeWrapper nw = new NodeWrapper(n);
				if (input != null && input.length() > 0) {
					String title = nw.getTitle();
					if (title == null) {
						AmpAuthWebSession session = (AmpAuthWebSession) Session.get();
						Site site = session.getSite();
						title = nw.getTranslatedTitleByLang(site.getDefaultLanguage().getCode());
					}
					if (title != null && title.toLowerCase().startsWith(input.trim().toLowerCase())) {
						ret.add(nw);
					}
				} else {
					ret.add(nw);
				}
			}
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
