/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.ErrorLevelsFeedbackMessageFilter;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIdentificationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpSemanticValidatorField;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.dgfoundation.amp.onepager.validators.AmpSemanticValidator;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;

/**
 * Main component hub for all activity form subcomponents.
 * This is {@link AmpFeaturePanel}, as such, it supports
 * {@link OnePagerUtil#cascadeFmEnabled(AjaxRequestTarget, boolean, org.apache.wicket.Component)}
 * and
 * {@link OnePagerUtil#cascadeFmVisible(AjaxRequestTarget, boolean, org.apache.wicket.Component)}
 * @author mpostelnicu@dgateway.org
 * @since Jun 7, 2011
 */
public class AmpActivityFormFeature extends AmpFeaturePanel<AmpActivityVersion> {
	

	protected Form<AmpActivityVersion> activityForm;
	public Form<AmpActivityVersion> getActivityForm() {
		return activityForm;
	}

	public void setActivityForm(Form<AmpActivityVersion> activityForm) {
		this.activityForm = activityForm;
	}

	public ListView<AmpComponentPanel> getFeatureList() {
		return featureList;
	}
	
	
	/**
	 * Toggles the validation of semantic validators. 
	 * @param enabled whether these validators are enabled
	 * @param form the form to set the validators
	 * @param target 
	 * @see AmpSemanticValidatorField
	 * @see AmpSemanticValidator
	 */
	public void toggleSemanticValidation(final boolean enabled, Form<?> form,
			final AjaxRequestTarget target) {

		// visit all the semantic validator fields and enable/disable them
		form.visitChildren(AmpSemanticValidatorField.class,
				new Component.IVisitor<AmpSemanticValidatorField<?>>() {
					@Override
					public Object component(AmpSemanticValidatorField<?> ifs) {
						ifs.getSemanticValidator().setEnabled(enabled);
						target.addComponent(ifs);
						return Component.IVisitor.CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
					}

				});

		// put status to not required
		form.visitChildren(AmpIdentificationFormSectionFeature.class,
				new Component.IVisitor<AmpIdentificationFormSectionFeature>() {

					@Override
					public Object component(
							AmpIdentificationFormSectionFeature ifs) {
						AbstractChoice<?, AmpCategoryValue> statusField = ifs
								.getStatus().getChoiceContainer();
						String js = String.format("$('#%s').change();",
								statusField.getMarkupId());
						statusField.setRequired(enabled);
						target.appendJavascript(js);
						target.addComponent(ifs.getStatus());
						return Component.IVisitor.STOP_TRAVERSAL;
					}

				});
	}

	private ListView<AmpComponentPanel> featureList;

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param newActivity 
	 * @param listModel 
	 * @param hideLabel
	 * @throws Exception 
	 */
	public AmpActivityFormFeature(String id, final IModel<AmpActivityVersion> am,
			String fmName, final boolean newActivity, AbstractReadOnlyModel<List<AmpComponentPanel>> listModel) throws Exception {
		super(id, am, fmName, true);
		
		activityForm=new Form<AmpActivityVersion>("activityForm") { 
			@Override
			protected void onError() {
				// TODO Auto-generated method stub
				super.onError();
			}
		};
		activityForm.setOutputMarkupId(true);
		
		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
		feedbackPanel.setOutputMarkupId(true);
		
		
		//do not show errors in this feedbacklabel (they will be shown for each component)
        int[] filteredErrorLevels = new int[]{FeedbackMessage.ERROR};
        feedbackPanel.setFilter(new ErrorLevelsFeedbackMessageFilter(filteredErrorLevels));

		activityForm.add(feedbackPanel);
		add(activityForm);
		
		//add ajax submit button
		AmpButtonField saveAndSubmit = new AmpButtonField("saveAndSubmit","Save and Submit", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				am.setObject(am.getObject());
				toggleSemanticValidation(true, form,target);
				
				// process the form for this request
				form.process(this.getButton());
				
				if(!form.hasError()) saveMethod(target, am, feedbackPanel, false);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(feedbackPanel);
			}
			
		};
		AttributePrepender updateEditors = new AttributePrepender("onclick", new Model("window.onbeforeunload = null; for (instance in CKEDITOR.instances) CKEDITOR.instances[instance].updateElement(); "), "");
		
		saveAndSubmit.getButton().add(new AttributeModifier("class", true, new Model("sideMenuButtons")));
		saveAndSubmit.getButton().add(updateEditors);
		saveAndSubmit.getButton().setDefaultFormProcessing(false);
		activityForm.add(saveAndSubmit);

		AmpButtonField saveAsDraft = new AmpButtonField("saveAsDraft", "Save as Draft", AmpFMTypes.MODULE, true) {
			TextField<String> titleField=null;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				am.setObject(am.getObject());
				

				toggleSemanticValidation(false, form,target);


				// process the form for this request
				form.process(this.getButton());
				//only in the eventuality that the title field is valid (is not empty) we proceed with the real save!
				if(!form.hasError())  saveMethod(target, am, feedbackPanel, true);
				
				
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(feedbackPanel);
			}
		};
		saveAsDraft.getButton().setDefaultFormProcessing(false); //disable global validation of the form
		saveAsDraft.getButton().add(new AttributeModifier("class", true, new Model("sideMenuButtons")));
		saveAsDraft.getButton().add(updateEditors);
		activityForm.add(saveAsDraft);

		AmpButtonField logframe = new AmpButtonField("logframe", "Logframe", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}
		};
		if (am.getObject().getAmpActivityId() == null)
			logframe.setVisible(false);
		else{
			logframe.add(new SimpleAttributeModifier("onclick", "previewLogframe(" + am.getObject().getAmpActivityId() + ");"));
			logframe.setVisible(true);
		}
		logframe.getButton().add(new AttributeModifier("class", true, new Model("sideMenuButtons")));
		activityForm.add(logframe);
		
		AmpButtonField preview = new AmpButtonField("preview", "Preview", AmpFMTypes.MODULE, true) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.appendJavascript("window.location.replace(\"/aim/viewActivityPreview.do~pageId=2~activityId=" + am.getObject().getAmpActivityId() + "~isPreview=1\");");
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(feedbackPanel);
			}
		};
		preview.getButton().add(new AttributeModifier("class", true, new Model("sideMenuButtons")));
		if (am.getObject().getAmpActivityId() == null)
			preview.setVisible(false);
		
		activityForm.add(preview);
		
		featureList = new ListView<AmpComponentPanel>("featureList", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpComponentPanel> item) {
				if (item.getModelObject() != null)
					item.add(item.getModelObject());
				else{
					Label tmp = new Label("featureItem", "ERROR: Section failed to load!");
					tmp.add(new SimpleAttributeModifier("style", "font-size: medium; font-style: bold; color: red; margin: 15px;"));
					item.add(tmp);
				}
					
			}
		};
		featureList.setReuseItems(true);
		activityForm.add(featureList);
		
		quickMenu(am, listModel);
	}
	
	protected void saveMethod(AjaxRequestTarget target,
			IModel<AmpActivityVersion> am, FeedbackPanel feedbackPanel,
			boolean draft) {
		
		OnePager op = this.findParent(OnePager.class);
		//disable lock refresher
		op.getEditLockRefresher().setEnabled(false);
		
		AmpActivityModel a = (AmpActivityModel) am;
		AmpActivityVersion activity=am.getObject();
		Long oldId = activity.getAmpActivityId();
		Boolean wasDraft=activity.getDraft();
		AmpTeamMember modifiedBy = activity.getModifiedBy();
		AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
		AmpTeamMember ampCurrentMember = wicketSession.getAmpCurrentMember();


		//Before starting to save check lock
		if (oldId != null && !ActivityGatekeeper.verifyLock(String.valueOf(a.getId()), a.getEditingKey())){
			//Someone else has grabbed the lock ... maybe connection slow and lock refresh timed out
			getRequestCycle().setRequestTarget(new RedirectRequestTarget(ActivityGatekeeper.buildRedirectLink(String.valueOf(a.getId()))));
			return;
		}
		
		ActivityUtil.saveActivity((AmpActivityModel) am, draft);

		info("Activity saved successfully");

		/*
		 * if activity created or created as draft 
		 * and then saved the message should be sent to the list
		 */
		AmpActivityVersion newActivity=am.getObject();
		if ((oldId == null || (wasDraft != null && wasDraft))
				&& newActivity.getDraft() != null && !newActivity.getDraft()) {
			new ActivitySaveTrigger(newActivity);
		}
    	String additionalDetails="approved";
		//if validation is off in team setup no messages should be generated
			String validation = DbUtil.getTeamAppSettingsMemberNotNull(ampCurrentMember.getAmpTeam().getAmpTeamId()).getValidation();
			if (activity.getDraft() != null&& !activity.getDraft()&&!("validationOff".equals(validation))) {
        	String approvalStatus = newActivity.getApprovalStatus();
			if(approvalStatus.equals(Constants.APPROVED_STATUS)||approvalStatus.equals(Constants.STARTED_APPROVED_STATUS)){
        		if(modifiedBy!=null){
        			AmpTeamMemberRoles role=modifiedBy.getAmpMemberRole();
            		boolean isTeamHead=false;
            		if(role.getTeamHead()!=null&&role.getTeamHead()){
            			isTeamHead=true;
            		}
            		if(!isTeamHead&&!role.isApprover()){
            			if(oldId==null||("allEdits".equals(validation))){
            				new ApprovedActivityTrigger(newActivity,modifiedBy); //if TL or approver created activity, then no Trigger is needed
            			}
            		}
        		}
        		
        	}else{
        		if("allEdits".equals(validation)||oldId==null){
        			new NotApprovedActivityTrigger(newActivity);
            		additionalDetails="pending approval";
        		}
        	}
        }
		else{
			if (newActivity.getDraft() != null&& newActivity.getDraft()){
				additionalDetails="draft";
			}
		}
			WebRequestCycle cycle    = (WebRequestCycle)RequestCycle.get();
		    HttpServletRequest hsRequest   = cycle.getWebRequest().getHttpServletRequest();

			if (oldId != null) {
				List<String> details=new ArrayList<String>();
				details.add(additionalDetails);
				AuditLoggerUtil.logActivityUpdate(hsRequest, newActivity,details);
			} else {
				try {
					AuditLoggerUtil.logObject(hsRequest, newActivity, "add",additionalDetails);
				} catch (DgException e) {
					e.printStackTrace();
				}
			}

		//if (newActivity){
			Long actId = am.getObject().getAmpActivityId();//getAmpActivityGroup().getAmpActivityGroupId();
			String replaceStr;
			if (oldId == null)
				replaceStr = "new";
			else
				replaceStr = String.valueOf(oldId);
			target.appendJavascript("window.location.replace('/');");
		//}
		target.addComponent(feedbackPanel);
	}

	private void quickMenu(IModel<AmpActivityVersion> am, AbstractReadOnlyModel<List<AmpComponentPanel>> listModel) {
		ListView<AmpComponentPanel> list = new ListView<AmpComponentPanel>("quickList", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpComponentPanel> item) {
				if (item.getModelObject() != null){
					Label label = new TrnLabel("quickName", item.getModelObject().getFMName());
					String itemId = Hex.encodeHexString(item.getModelObject().getFMName().getBytes());
					label.add(new SimpleAttributeModifier("onclick", "$('#" + itemId + "').parent().parent().siblings('div:first').show();$('html, body').animate({scrollTop: $('#" + itemId + "').offset().top}, 1200); return false;"));
					if (!item.getModelObject().isVisible())
						item.setVisible(false);
					item.add(label);
				}
				else{
					WebMarkupContainer tmp = new WebMarkupContainer("quickName");
					tmp.setVisible(false);
					item.add(tmp);
				}
			}
		};
		list.setReuseItems(true);
		activityForm.add(list);
	}

}
