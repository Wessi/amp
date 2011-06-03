/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMUtil;

/**
 * Basic class for AMP components. This component wraps a feature manager connectivity, receiving
 * the feature name and the fmType (field,feature,module). It then queries the FM utils and 
 * determines if this component is visible or not and if this component is enabled or disabled.
 * This class can be used directly to create panels or extended to make amp field /feature panels
 * @author mpostelnicu@dgateway.org since Sep 22, 2010
 */
public abstract class AmpComponentPanel<T> extends Panel implements
		AmpFMConfigurable {

	protected String fmName;
	protected AmpFMTypes fmType;
	protected WebMarkupContainer fmBorder;
	protected IndicatingAjaxLink visibleFmButton;
	protected IndicatingAjaxLink enabledFmButton;
	protected AjaxCheckBox cascadeFmToChildren;
	protected Label cascadeFmToChildrenLabel;
	
	public AjaxCheckBox getCascadeFmToChildren() {
		return cascadeFmToChildren;
	}


	public void setCascadeFmToChildren(AjaxCheckBox cascadeFmToChildren) {
		this.cascadeFmToChildren = cascadeFmToChildren;
	}

	protected static Logger logger = Logger.getLogger(AmpComponentPanel.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5847159396251223479L;

	/**
	 * Constructs a new object using the component id, fmName and fmType
	 * @see AmpFMTypes
	 * @param id the component id
	 * @param fmName the feature manager name
	 * @param fmType the feature type
	 */
	public AmpComponentPanel (String id,String fmName, AmpFMTypes fmType) {
		this(id,null,fmName,fmType);
	}

	
	/**
	 * Constructs a new object using only the component id and the FM Name.
	 * Assumes the FM Type is {@link AmpFMTypes#FEATURE}
	 * @param id
	 * @param fmName
	 */
	public AmpComponentPanel(String id,String fmName) {
		this(id,null,fmName,AmpFMTypes.FEATURE);
	}
	
	/**
	 * Switch visibility for this fm Control. Change Hide with Show for the FM Button
	 * @param target the ajax target
	 */
	public void switchFmVisible(AjaxRequestTarget target) {
		FMUtil.switchFmVisible(AmpComponentPanel.this);
		visibleFmButton.add(new AttributeModifier("value", new Model(FMUtil.isFmVisible(AmpComponentPanel.this)?"Hide":"Show")));
		target.addComponent(this);
	}

	/**
	 * Switch enabling/disabling for this fm Control. Change Hide with Show for the FM Button
	 * @param target the ajax target
	 */
	public void switchFmEnabled(AjaxRequestTarget target) {
		FMUtil.switchFmEnabled(AmpComponentPanel.this);
		enabledFmButton.add(new AttributeModifier("value", new Model(FMUtil.isFmEnabled(AmpComponentPanel.this)?"Disable":"Enable")));
		target.addComponent(this);
	}
	
	/**
	 * Cascade the {@link #switchFmVisible(AjaxRequestTarget)} to all children of this {@link AmpComponentPanel}
	 * @param target
	 */
	public void cascadeFmVisible(AjaxRequestTarget target, final boolean visible, Component c) {
		if (c instanceof MarkupContainer){
			MarkupContainer m = (MarkupContainer) c;
			for (int i = 0; i < m.size(); i++) {
				Component component = m.get(i);
				if(component instanceof AmpComponentPanel) {
					FMUtil.changeFmVisible(component, visible);
				}
				cascadeFmVisible(target,visible, component);
			}
		}
	}

	/**
	 * Cascade the {@link #switchFmEnabled(AjaxRequestTarget)} to all children of this {@link AmpComponentPanel}
	 * @param target
	 */
	public void cascadeFmEnabled(AjaxRequestTarget target, boolean enabled, Component c) {
		if (c instanceof MarkupContainer){
			MarkupContainer m = (MarkupContainer) c;
			for (int i = 0; i < m.size(); i++) {
				Component component = m.get(i);
				if(component instanceof AmpComponentPanel) {
					FMUtil.changeFmEnabled(component, enabled);
				}
				cascadeFmEnabled(target, enabled, component);
			}
		}
	}
	
	
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param fmBehavior
	 */
	public AmpComponentPanel(String id, IModel<T> model,String fmName, AmpFMTypes fmBehavior) {
		super(id, model);
		setOutputMarkupId(true);
		this.fmName=fmName;
		this.fmType=fmBehavior;
		fmBorder = new TransparentWebMarkupContainer("fmBorder");
		add(fmBorder);
		visibleFmButton=new IndicatingAjaxLink("visibleFmButton") {	
			@Override
			public void onClick(AjaxRequestTarget target) {
				switchFmVisible(target);
				if(cascadeFmToChildren.getModelObject()) 
					cascadeFmVisible(target,FMUtil.isFmVisible(AmpComponentPanel.this), AmpComponentPanel.this);				
			}
		};
		visibleFmButton.setOutputMarkupId(true);
		visibleFmButton.setVisible(false);
		add(visibleFmButton);
		
		enabledFmButton=new IndicatingAjaxLink("enabledFmButton") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				switchFmEnabled(target);
				if(cascadeFmToChildren.getModelObject()) 
					cascadeFmEnabled(target,FMUtil.isFmEnabled(AmpComponentPanel.this), AmpComponentPanel.this);  
			}
		};
		
		cascadeFmToChildren=new IndicatingAjaxCheckBox("cascadeFmToChildren",new Model<Boolean>()) {			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				
			}
		};
		
		cascadeFmToChildren.setOutputMarkupId(true);
		cascadeFmToChildren.setVisible(false);
		add(cascadeFmToChildren);
		
		cascadeFmToChildrenLabel= new Label("cascadeFmToChildrenLabel","Cascade to children");
		cascadeFmToChildrenLabel.setVisible(false);
		add(cascadeFmToChildrenLabel);
		
		
		enabledFmButton.setOutputMarkupId(true);
		enabledFmButton.setVisible(false);
		add(enabledFmButton);
	}
	
	public AmpComponentPanel(String id, IModel<T> model,String fmName) {
		this(id,model,fmName,AmpFMTypes.FEATURE);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public AmpFMTypes getFMType() {
		return fmType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFMName() {
		return fmName;
	}
	
	@Override
	protected void onBeforeRender() {
		boolean fmMode = ((AmpAuthWebSession)getSession()).isFmMode();
		/**
		 * Do not reverse the order of fmEnabled and fmVisible
		 */
		boolean fmEnabled = FMUtil.isFmEnabled(this);
		boolean fmVisible = FMUtil.isFmVisible(this);
		
		setEnabled(fmMode?true:fmEnabled);
		setVisible(fmMode?true:fmVisible);
		
		enabledFmButton.add(new AttributeModifier("value", new Model(fmEnabled?"Disable":"Enable")));
		visibleFmButton.add(new AttributeModifier("value", new Model(fmVisible?"Hide":"Show")));
		
		if(fmMode) {
			visibleFmButton.setVisible(true);
			enabledFmButton.setVisible(true);
			cascadeFmToChildren.setVisible(true);
			cascadeFmToChildrenLabel.setVisible(true);
		}
		String style=fmMode?"border: 2px blue solid; padding: 4px;":"";
		fmBorder.add(new AttributeModifier("style", true, new Model(style)));
		super.onBeforeRender();
	}
}
