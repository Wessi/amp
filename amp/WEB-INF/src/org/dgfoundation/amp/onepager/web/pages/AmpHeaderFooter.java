/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.web.pages;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.AmpWebSession;
import org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.translation.AmpAjaxBehavior;
import org.wicketstuff.jquery.JQueryBehavior;

import wicket.contrib.tinymce.settings.TinyMCESettings;

/**
 * @author mpostelnicu@dgateway.org
 * @since Sep 22, 2010
 */
public class AmpHeaderFooter extends WebPage {
	public AmpHeaderFooter() {
		
		Cookie[] cookies = ((WebRequest)getRequestCycle().getRequest()).getCookies();
		if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("digi_language")) {
                	String languageCode = cookies[i].getValue();
                	Session.get().setLocale(new Locale(languageCode));
                    if (languageCode != null) {
                        break;
                    }
                }
            }
        }
		
		add(new HeaderContributor(
				new com.google.excanvas.ExCanvasHeaderContributor()));
		add(new JQueryBehavior());
		AmpAjaxBehavior ampajax = new AmpAjaxBehavior();
		add(ampajax);
		final CharSequence callBackUrl = ampajax.getCallbackUrl();

		IModel variablesModel = new AbstractReadOnlyModel() {
			public Map getObject() {
				Map<String, CharSequence> variables = new HashMap<String, CharSequence>(
						2);
				variables.put("callBackUrl", callBackUrl);
				return variables;
			}
		};
		
		add(CSSPackageResource.getHeaderContribution(AmpHeaderFooter.class, "amp-wicket.css"));

		add(TextTemplateHeaderContributor.forJavaScript(AmpAjaxBehavior.class,
				"translations.js", variablesModel));
		add(JavascriptPackageResource.getHeaderContribution("/ckeditor/ckeditor.js"));
		
		add(JavascriptPackageResource.getHeaderContribution(
				AmpSubsectionFeaturePanel.class, "subsectionSlideToggle.js"));
		add(JavascriptPackageResource.getHeaderContribution(
				AmpStructuresFormSectionFeature.class, "gisPopup.js"));
	}
	
	public HttpServletRequest getServletRequest(){
		ServletWebRequest servletWebRequest = (ServletWebRequest) getRequest();
		HttpServletRequest request = servletWebRequest.getHttpServletRequest();
		return request;
	}
	
	public HttpSession getHttpSession(){
		HttpSession session = getServletRequest().getSession();
		return session;
	}
}
