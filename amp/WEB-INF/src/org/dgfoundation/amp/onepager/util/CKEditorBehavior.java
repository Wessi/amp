package org.dgfoundation.amp.onepager.util;

import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.protocol.http.WebRequest;

public class CKEditorBehavior extends Behavior{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Component component;

	@Override
	public void renderHead(Component component, IHeaderResponse response) {

		String renderOnDomReady = getRenderOnDomReadyJavaScript(response);
        if (renderOnDomReady != null)
            response.renderOnDomReadyJavaScript(renderOnDomReady);
        /*
         * 
        String renderJavaScript = getRenderJavaScript(response);
        if (renderJavaScript != null)
            response.renderJavaScript(renderJavaScript, null);
         */
	}
	
	protected String getRenderOnDomReadyJavaScript(IHeaderResponse response) {
        if (component == null)
            throw new IllegalStateException("CKEditorBehavior is not bound to a component");
        //if (! mayRenderJavaScriptDirect())
            return getAddCKEditorScript(component);
        //return null;
    }

    private boolean mayRenderJavaScriptDirect() {
    	return RequestCycle.get().getRequest() instanceof WebRequest && !((WebRequest)RequestCycle.get().getRequest()).isAjax();
	}

	protected String getRenderJavaScript(IHeaderResponse response) {
        if (component == null)
            throw new IllegalStateException("CKEditorBehavior is not bound to a component");
        if (mayRenderJavaScriptDirect())
            return getAddCKEditorScript(component);
        return null;
    }

    protected String getAddCKEditorScript(Component c) {
        return "" //
                + " CKEDITOR.replace('" + c.getMarkupId() + "');"; //
    }

    public void bind(Component component) {
        if (this.component != null)
            throw new IllegalStateException("CKEditorBehavior can not bind to more than one component");
        super.bind(component);
        if (isMarkupIdRequired())
            component.setOutputMarkupId(true);
        this.component = component;
    }

	private boolean isMarkupIdRequired() {
		return true;
	}
}
