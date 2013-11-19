/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.validation.validator.StringValidator;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpActivityBudgetExtrasPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpBooleanChoiceField;
import org.dgfoundation.amp.onepager.components.fields.AmpBudgetClassificationField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentTab;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentTabsFieldWrapper;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpWarningComponentPanel;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.validators.AmpUniqueActivityTitleValidator;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * Identification section in activity form. This is also an AMP feature
 * @author mpostelnicu@dgateway.org since Oct 3, 2010
 * @see OnePager
 */
public class AmpIdentificationFormSectionFeature extends AmpFormSectionFeaturePanel {

	private static final long serialVersionUID = 8568986144567957699L;
	private AmpTextAreaFieldPanel title;
	private AmpCategorySelectFieldPanel status;
	private AmpWarningComponentPanel<String> titleSimilarityWarning;

	public AmpCategorySelectFieldPanel getStatus() {
		return status;
	}

	public AmpTextAreaFieldPanel<String> getTitle() {
		return title;
	}

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception 
	 *
	 */
	public AmpIdentificationFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
			super(id, fmName, am);
			this.fmType = AmpFMTypes.MODULE;
			
			IModel<String> m = new PropertyModel<String>(am, "name");
			title = new AmpTextAreaFieldPanel("title", m, "Project Title", false, false, false);
			//title = new AmpTextFieldPanel<String>(
			//		"title", m, "Project Title", AmpFMTypes.MODULE);
			//title.getTextContainer().add(new AmpRequiredFieldValidator<String>(title));
			title.getTextAreaContainer().setRequired(true);
			title.getTextAreaContainer().add(new AmpUniqueActivityTitleValidator(new PropertyModel<AmpActivityGroup>(am,"ampActivityGroup")));			
			title.getTextAreaContainer().add(StringValidator.maximumLength(255));
			title.getTextAreaContainer().add(new AttributeModifier("style", "width: 710px; margin: 0px;"));
			title.getTextAreaContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {

				@Override
				protected void onUpdate(AjaxRequestTarget target) {		
					if(!titleSimilarityWarning.isVisible()) return;
					titleSimilarityWarning.getWarning().modelChanged();
					target.add(titleSimilarityWarning);					
				}
				
			});
			
			add(title);
			
		final AbstractReadOnlyModel<String> warningModel = new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public String getObject() {
				if (title.getTextAreaContainer().getModelObject() == null)
					return null;
				String stitle=(String) title.getTextAreaContainer().getModelObject();
				ServletContext context = ((WebApplication) Application.get())
						.getServletContext();
				List<String> list = LuceneUtil.findActivitiesMoreLikeThis(
						context.getRealPath("/")
								+ LuceneUtil.ACTVITY_INDEX_DIRECTORY,stitle,
						2);
				if (!list.isEmpty()) {
					String ret=TranslatorUtil
							.getTranslation("Warning! Potential duplicates! The database already contains project(s) with similar title(s):")+"\n";
					for (String string : list) 
						if(string!=null && string.trim().compareTo(stitle.trim())!=0)
							ret+=" - "+string+ "\n";
					return ret;
					
					
				} else
					return null;

			}
		};
			
			titleSimilarityWarning=new AmpWarningComponentPanel<String>("titleSimilarityWarning", "Project Title Similarity Warning", warningModel);
			titleSimilarityWarning.setOutputMarkupId(true);
			add(titleSimilarityWarning);
			
			status = new AmpCategorySelectFieldPanel(
					"status", CategoryConstants.ACTIVITY_STATUS_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.ACTIVITY_STATUS_KEY),
							CategoryConstants.ACTIVITY_STATUS_NAME, true, false, null, AmpFMTypes.MODULE);
			status.getChoiceContainer().setRequired(true);
			add(status);
			
			add(new AmpTextAreaFieldPanel<String>("statusReason",
					new PropertyModel<String>(am, "statusReason"), "Status Reason", true, AmpFMTypes.MODULE));
			
			AmpTextFieldPanel<String> budgetCodeProjectId = new AmpTextFieldPanel<String>(
					"budgetCodeProjectID", new PropertyModel<String>(am,
							"budgetCodeProjectID"), "Budget Code Project ID", AmpFMTypes.MODULE);
			budgetCodeProjectId.setTextContainerDefaultMaxSize();
			add(budgetCodeProjectId);
	
			AmpTextFieldPanel<String> donorProjectCode = new AmpTextFieldPanel<String>(
					"donorProjectCode", new PropertyModel<String>(am,
							"projectCode"), "Donor Project Code", AmpFMTypes.MODULE);
			donorProjectCode.setTextContainerDefaultMaxSize();
			add(donorProjectCode);
			
			AmpTextFieldPanel<String> govAgreementNum = new AmpTextFieldPanel<String>(
					"govAgreementNum", new PropertyModel<String>(am,
							"govAgreementNumber"), "Government Agreement Number", AmpFMTypes.MODULE);
			govAgreementNum.setTextContainerDefaultMaxSize();
			add(govAgreementNum);
	
			AmpTextFieldPanel<String> crisNumber = new AmpTextFieldPanel<String>(
					"crisNumber", new PropertyModel<String>(am,
							"crisNumber"), "Cris Number", AmpFMTypes.MODULE);
			govAgreementNum.setTextContainerDefaultMaxSize();
			add(crisNumber);

			AmpCategorySelectFieldPanel acChapter = new AmpCategorySelectFieldPanel(
					"acChapter", CategoryConstants.ACCHAPTER_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.ACCHAPTER_KEY), 
							CategoryConstants.ACCHAPTER_NAME, true, true, null, AmpFMTypes.MODULE);
			add(acChapter);
			
			final AmpActivityBudgetExtrasPanel budgetExtras = new AmpActivityBudgetExtrasPanel("budgetExtras", am, "Budget Extras");
			budgetExtras.setOutputMarkupId(true);
			budgetExtras.setIgnoreFmVisibility(true);
            budgetExtras.setIgnorePermissions(true);

			WebMarkupContainer budgetExtrasContainter = new WebMarkupContainer("budgetExtrasContainer");
			budgetExtrasContainter.add(budgetExtras);
			budgetExtrasContainter.setOutputMarkupId(true);
			add(budgetExtrasContainter);
			
			WebMarkupContainer budgetClassificationContainer = new WebMarkupContainer("budgetClassificationContainer");
			budgetClassificationContainer.setOutputMarkupId(true);
			final AmpBudgetClassificationField budgetClassification = new AmpBudgetClassificationField("budgetClassification", am, "Budget Classification");
			budgetClassification.setOutputMarkupId(true);
			budgetClassificationContainer.add(budgetClassification);
			add(budgetClassificationContainer);

			//AmpActivityBudgetField activityBudget = new AmpActivityBudgetField("activityBudget", new PropertyModel(am, "budget"), "Activity Budget", budgetExtras, budgetClassification);
			//add(activityBudget);

			final AmpCategorySelectFieldPanel activityBudget = new AmpCategorySelectFieldPanel(
					"activityBudget",
					CategoryConstants.ACTIVITY_BUDGET_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.ACTIVITY_BUDGET_KEY),
							CategoryConstants.ACTIVITY_BUDGET_NAME, true, true, null, AmpFMTypes.MODULE);
			activityBudget.getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				{
					updateBudget();
				}
				
				private void toggleExtraFields(boolean b){
					budgetExtras.setVisible(b);
					budgetClassification.toggleActivityBudgetVisibility(b);
				}
				
				private void updateExtraFields(AjaxRequestTarget target){
					target.add(budgetExtras);
					target.add(budgetExtras.getParent());
					budgetClassification.addToTargetActivityBudget(target);
				}
				
				private void updateBudget(){
					AmpCategoryValue obj = (AmpCategoryValue) activityBudget.getChoiceContainer().getModelObject();
					AmpCategoryValue budgetOn = null;
					try {
						budgetOn = CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ACTIVITY_BUDGET_ON);
					} catch (Exception e) {
						logger.error(e);
					}	
				    long budgetOnId = (budgetOn==null)?1:budgetOn.getId();
					if (obj != null && obj.getId() == budgetOnId) // "On" was selected
						toggleExtraFields(true);
					else
						toggleExtraFields(false);
				}
				
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					updateBudget();
					updateExtraFields(target);
				}
			});
            add(new AmpComponentPanel("activityBudgetRequired", "Required Validator for " + CategoryConstants.ACTIVITY_BUDGET_NAME) {
                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    if (this.isVisible()){
                        activityBudget.getChoiceContainer().setRequired(true);
                    }
                }
            });
            add(activityBudget);


			AmpCategoryGroupFieldPanel financialInstrument = new AmpCategoryGroupFieldPanel(
					"financialInstrument",
					CategoryConstants.FINANCIAL_INSTRUMENT_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.FINANCIAL_INSTRUMENT_KEY),
							CategoryConstants.FINANCIAL_INSTRUMENT_NAME, true, true, null, AmpFMTypes.MODULE);
			add(financialInstrument);

			AmpCategorySelectFieldPanel procurementSystem = new AmpCategorySelectFieldPanel(
					"procurementSystem",
					CategoryConstants.PROCUREMENT_SYSTEM_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.PROCUREMENT_SYSTEM_KEY),
							CategoryConstants.PROCUREMENT_SYSTEM_NAME, true, true, null, AmpFMTypes.MODULE);
			add(procurementSystem);
			
			AmpCategorySelectFieldPanel reportingSystem = new AmpCategorySelectFieldPanel(
					"reportingSystem",
					CategoryConstants.REPORTING_SYSTEM_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.REPORTING_SYSTEM_KEY),
							CategoryConstants.REPORTING_SYSTEM_NAME, true, true, null, AmpFMTypes.MODULE);
			add(reportingSystem);
			
			AmpCategorySelectFieldPanel auditSystem = new AmpCategorySelectFieldPanel(
					"auditSystem",
					CategoryConstants.AUDIT_SYSTEM_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.AUDIT_SYSTEM_KEY),
							CategoryConstants.AUDIT_SYSTEM_NAME, true, true, null, AmpFMTypes.MODULE);
			add(auditSystem);
			
			AmpCategorySelectFieldPanel institutions = new AmpCategorySelectFieldPanel(
					"institutions",
					CategoryConstants.INSTITUTIONS_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.INSTITUTIONS_KEY),
							CategoryConstants.INSTITUTIONS_NAME, true, true, null, AmpFMTypes.MODULE);
			add(institutions);
			
			AmpCategorySelectFieldPanel accessionInstrument = new AmpCategorySelectFieldPanel(
					"accessionInstrument",
					CategoryConstants.ACCESSION_INSTRUMENT_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.ACCESSION_INSTRUMENT_KEY),
							CategoryConstants.ACCESSION_INSTRUMENT_NAME, true, true, null, AmpFMTypes.MODULE);
			add(accessionInstrument);

			AmpCategorySelectFieldPanel projectCategory = new AmpCategorySelectFieldPanel(
					"projectCategory",
					CategoryConstants.PROJECT_CATEGORY_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.PROJECT_CATEGORY_KEY),
							CategoryConstants.PROJECT_CATEGORY_NAME, true, true, null, AmpFMTypes.MODULE);
			add(projectCategory);
			AmpCategorySelectFieldPanel projectImplementingUnit = new AmpCategorySelectFieldPanel(
					"projectImplementingUnit",
					CategoryConstants.PROJECT_IMPLEMENTING_UNIT_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.PROJECT_IMPLEMENTING_UNIT_KEY),
							CategoryConstants.PROJECT_IMPLEMENTING_UNIT_NAME, true, true, null, AmpFMTypes.MODULE);
			add(projectImplementingUnit);

			add(new AmpBooleanChoiceField("governmentApprovalProcedures", 
					new PropertyModel<Boolean>(am, "governmentApprovalProcedures"), "Government Approval Procedures"));

			add(new AmpBooleanChoiceField("jointCriteria", 
					new PropertyModel<Boolean>(am, "jointCriteria"), "Joint Criteria"));

			add(new AmpBooleanChoiceField("humanitarianAid", 
					new PropertyModel<Boolean>(am, "humanitarianAid"), "Humanitarian Aid"));
			
		
			add(new AmpTextAreaFieldPanel<String>("projectComments",
					new PropertyModel<String>(am, "projectComments"),
					"Project Comments", true, AmpFMTypes.MODULE));
			add(new AmpTextAreaFieldPanel<String>("description",
					new PropertyModel<String>(am, "description"), "Description",
					true, AmpFMTypes.MODULE));
			add(new AmpTextAreaFieldPanel<String>("objective",
					new PropertyModel<String>(am, "objective"), "Objective", true, AmpFMTypes.MODULE));
			
			AmpAuthWebSession session = (AmpAuthWebSession) getSession();
			Site site = session.getSite();

			String txtValue = "OV Indicators";
			String genKey = TranslatorWorker.generateTrnKey(txtValue);
			String cOvIndicators = TranslatorWorker.getInstance(genKey).translateFromTree(genKey, site, session.getLocale().getLanguage(), txtValue, TranslatorWorker.TRNTYPE_LOCAL, null);
			txtValue = "Assumption";
			genKey = TranslatorWorker.generateTrnKey(txtValue);
			String cAssumption = TranslatorWorker.getInstance(genKey).translateFromTree(genKey, site, session.getLocale().getLanguage(), txtValue, TranslatorWorker.TRNTYPE_LOCAL, null);
			txtValue = "Verification";
			genKey = TranslatorWorker.generateTrnKey(txtValue);
			String cVerification = TranslatorWorker.getInstance(genKey).translateFromTree(genKey, site, session.getLocale().getLanguage(), txtValue, TranslatorWorker.TRNTYPE_LOCAL, null);
			
			List<ITab> objectiveTabs = new ArrayList<ITab>();
			objectiveTabs.add(new AmpCommentTab(cOvIndicators , "Objective Objectively Verifiable Indicators", am, AmpCommentPanel.class));
			objectiveTabs.add(new AmpCommentTab(cAssumption , "Objective Assumption", am, AmpCommentPanel.class));
			objectiveTabs.add(new AmpCommentTab(cVerification , "Objective Verification", am, AmpCommentPanel.class));
			
			AmpCommentTabsFieldWrapper objTabs = new AmpCommentTabsFieldWrapper("objectiveTabs", "Objective Comments", objectiveTabs);
			add(objTabs);
			
			add(new AmpTextAreaFieldPanel<String>("purpose",
					new PropertyModel<String>(am, "purpose"), "Purpose", true, AmpFMTypes.MODULE));
			
			List<ITab> tabs = new ArrayList<ITab>();
			tabs.add(new AmpCommentTab(cOvIndicators , "Purpose Objectively Verifiable Indicators", am, AmpCommentPanel.class));
			tabs.add(new AmpCommentTab(cAssumption , "Purpose Assumption", am, AmpCommentPanel.class));
			tabs.add(new AmpCommentTab(cVerification , "Purpose Verification", am, AmpCommentPanel.class));
			
			AmpCommentTabsFieldWrapper purposeTabs = new AmpCommentTabsFieldWrapper("purposeTabs", "Purpose Comments", tabs);
			add(purposeTabs);
			
			add(new AmpTextAreaFieldPanel<String>("results",
					new PropertyModel<String>(am, "results"), "Results", true, AmpFMTypes.MODULE));
	
			tabs = new ArrayList<ITab>();
			tabs.add(new AmpCommentTab(cOvIndicators , "Results Objectively Verifiable Indicators", am, AmpCommentPanel.class));
			tabs.add(new AmpCommentTab(cAssumption , "Results Assumption", am, AmpCommentPanel.class));
			tabs.add(new AmpCommentTab(cVerification , "Results Verification", am, AmpCommentPanel.class));
			
			AmpCommentTabsFieldWrapper resultsTabs = new AmpCommentTabsFieldWrapper("resultsTabs", "Results Comments", tabs);
			add(resultsTabs);
			add(new AmpTextAreaFieldPanel<String>("lessonsLearned",
					new PropertyModel<String>(am, "lessonsLearned"), "Lessons Learned", true, AmpFMTypes.MODULE));
			add(new AmpTextAreaFieldPanel<String>("projectImpact",
					new PropertyModel<String>(am, "projectImpact"), "Project Impact", true, AmpFMTypes.MODULE));
			add(new AmpTextAreaFieldPanel<String>("activitySummary",
					new PropertyModel<String>(am, "activitySummary"), "Activity Summary", true, AmpFMTypes.MODULE));
			add(new AmpTextAreaFieldPanel<String>("contractingArrangements",
					new PropertyModel<String>(am, "contractingArrangements"), "Contracting Arrangements", true, AmpFMTypes.MODULE));
			add(new AmpTextAreaFieldPanel<String>("linkedActivities",
					new PropertyModel<String>(am, "linkedActivities"), "Linked Activities", true, AmpFMTypes.MODULE));
			add(new AmpTextAreaFieldPanel<String>("conditionalitySequencing",
					new PropertyModel<String>(am, "condSeq"), "Conditionality and Sequencing", true, AmpFMTypes.MODULE));
			add(new AmpTextAreaFieldPanel<String>("conditionalities",
					new PropertyModel<String>(am, "conditionality"), "Conditionalities", true, AmpFMTypes.MODULE));
			add(new AmpTextAreaFieldPanel<String>("projectManagement",
					new PropertyModel<String>(am, "projectManagement"), "Project Management", true, AmpFMTypes.MODULE));
	}

}
