/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.web.pages;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.IPageMap;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;
import org.dgfoundation.amp.onepager.AmpWebSession;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.translation.AmpAjaxBehavior;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.wicketstuff.jquery.JQueryBehavior;

/**
 * @author dan
 *
 */
public class AmpPMHeaderFooter extends WebPage {

	/**
	 * 
	 */
	public AmpPMHeaderFooter() {
		// TODO Auto-generated constructor stub
		
		add(new HeaderContributor(new com.google.excanvas.ExCanvasHeaderContributor()));
		add(new JQueryBehavior());
		AmpAjaxBehavior ampajax = new AmpAjaxBehavior();
		add(ampajax);
		final CharSequence callBackUrl = ampajax.getCallbackUrl();

		IModel variablesModel = new AbstractReadOnlyModel() {
			public Map getObject() {
				Map<String, CharSequence> variables = new HashMap<String, CharSequence>(2);
				variables.put("callBackUrl", callBackUrl);
				return variables;
			}
		};

		add(TextTemplateHeaderContributor.forJavaScript(AmpAjaxBehavior.class,"translations.js", variablesModel));
		//add(JavascriptPackageResource.getHeaderContribution(AmpSubsectionFeaturePanel.class, "subsectionSlideTogglePM.js"));
//		String changeTrnMode = "Enable TrnMode";
////		add(new AjaxLink("changeTrnMode", new Model(changeTrnMode)) {
//
//			@Override
//			public void onClick(AjaxRequestTarget arg0) {
//				AmpWebSession session = (AmpWebSession) getSession();
//				if (session.isTranslatorMode())
//					session.setTranslatorMode(false);
//				else
//					session.setTranslatorMode(true);
//				setResponsePage(OnePager.class);
//			}
//		});

//		add(new AjaxLink("changeFmMode", new Model("FM Mode")) {
//			@Override
//			public void onClick(AjaxRequestTarget arg0) {
//				AmpWebSession session = (AmpWebSession) getSession();
//				if (session.isFmMode())
//					session.setFmMode(false);
//				else
//					session.setFmMode(true);
//				setResponsePage(OnePager.class);
//			}
//		});
//		
	}

	/**
	 * @param model
	 */
	public AmpPMHeaderFooter(IModel<?> model) {
		super(model);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pageMap
	 */
	public AmpPMHeaderFooter(IPageMap pageMap) {
		super(pageMap);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parameters
	 */
	public AmpPMHeaderFooter(PageParameters parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pageMap
	 * @param model
	 */
	public AmpPMHeaderFooter(IPageMap pageMap, IModel<?> model) {
		super(pageMap, model);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pageMap
	 * @param parameters
	 */
	public AmpPMHeaderFooter(IPageMap pageMap, PageParameters parameters) {
		super(pageMap, parameters);
		// TODO Auto-generated constructor stub
	}

}
