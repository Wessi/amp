/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

/**
 * Encaspulates a html button of type {@link AjaxButton}
 * 
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public abstract class AmpButtonField extends AmpFieldPanel<Void> {

	private static final long serialVersionUID = 3042844165981373890L;
	protected IndicatingAjaxButton button;

	public IndicatingAjaxButton getButton() {
		return button;
	}

	/**
	 * Escalated method invoker for wrapped {@link AjaxButton#onSubmit()}
	 * 
	 * @param target
	 * @param form
	 */
	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);

	/**
	 * 
	 * @param id
	 * @param fmName
	 */
	public AmpButtonField(String id, String fmName) {
		this(id, fmName, false, false);
	}

	protected void onError(AjaxRequestTarget target, Form<?> form) {
		// TODO Auto-generated method stub

	}

	public AmpButtonField(String id, String fmName, boolean hideLabel) {
		this(id, fmName, hideLabel, false);
	}

	public AmpButtonField(String id, String fmName, boolean hideLabel,
			boolean hideNewLine) {
		super(id, fmName, hideLabel, hideNewLine);
		button = new IndicatingAjaxButton("fieldButton", new Model<String>(
				fmName)) {
			private static final long serialVersionUID = -5699378405978605979L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AmpButtonField.this.onSubmit(target, form);
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				AmpButtonField.this.onError(target, form);

				// visit form children and add to the ajax request the invalid
				// ones
				form.visitChildren(FormComponent.class,
						new Component.IVisitor<FormComponent>() {

							@Override
							public Object component(FormComponent component) {
								if (!component.isValid()) {
									if (component instanceof HiddenField) {									
										 target.appendJavascript("$('#"+ component.getMarkupId() +"').parents().show();");
										 target.appendJavascript("$(window).scrollTop($('#"+component.getParent().getMarkupId()+"').position().top)");
										 target.addComponent(component);
										//target.addComponent(arg0);

									} else {
										target.focusComponent(component);
										String js = String.format(
												"$('#%s').change();",
												component.getMarkupId());
										target.appendJavascript(js);
										target.addComponent(component);
									}
								}
								return Component.IVisitor.CONTINUE_TRAVERSAL;
							}
						});

			}
		};
		addFormComponent(button);
	}

	public AmpButtonField(String id, String fmName, AmpFMTypes fmType,
			boolean hideNewLine) {
		this(id, fmName, true, hideNewLine);
	}

	public AmpButtonField(String id, String fmName, AmpFMTypes fmType) {
		this(id, fmName);
		this.fmType = fmType;
	}
}
