package org.dgfoundation.amp.onepager.components.features.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.GPIYesNoAnswerModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.digijava.module.aim.dbentity.AmpGPISurvey;
import org.digijava.module.aim.dbentity.AmpGPISurveyIndicator;
import org.digijava.module.aim.dbentity.AmpGPISurveyQuestion;
import org.digijava.module.aim.dbentity.AmpGPISurveyResponse;

import java.util.Collections;

public class AmpGPIQuestionItemFeaturePanel extends AmpFeaturePanel<AmpGPISurveyIndicator> {

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpGPIQuestionItemFeaturePanel(String id, String fmName, final IModel<AmpGPISurveyIndicator> surveyIndicator, final IModel<AmpGPISurvey> survey) {
		super(id, surveyIndicator, fmName, true);

		if (surveyIndicator.getObject() != null) {
			Iterator<AmpGPISurveyQuestion> it = surveyIndicator.getObject().getQuestions().iterator();
			while (it.hasNext()) {
				AmpGPISurveyQuestion qq = (AmpGPISurveyQuestion) it.next();
				qq.getAmpTypeId().getName();
			}
		}

		final AbstractReadOnlyModel<List<AmpGPISurveyQuestion>> listModel = new AbstractReadOnlyModel<List<AmpGPISurveyQuestion>>() {
			private static final long serialVersionUID = 3706184421451839215L;

			@Override
			public List<AmpGPISurveyQuestion> getObject() {
				Set<AmpGPISurveyQuestion> set = (Set<AmpGPISurveyQuestion>) surveyIndicator.getObject().getQuestions();
				ArrayList<AmpGPISurveyQuestion> list = new ArrayList<AmpGPISurveyQuestion>(set);
				Collections.sort(list, new AmpGPISurveyQuestion.GPISurveyQuestionComparator());

				ArrayList<AmpGPISurveyQuestion> listOrderedByParent = new ArrayList<AmpGPISurveyQuestion>();

				// now sort the question after their parent, iterate each
				// question, find siblings and move them below
				for (AmpGPISurveyQuestion parent : list) {
					if (parent.getParentQuestion() != null)
						continue;
					listOrderedByParent.add(parent);

					for (AmpGPISurveyQuestion sibling : list)
						if (parent.equals(sibling.getParentQuestion()))
							listOrderedByParent.add(sibling);

				}

				return listOrderedByParent;
			}
		};

		ListView<AmpGPISurveyQuestion> list = new ListView<AmpGPISurveyQuestion>("list", listModel) {
			@Override
			protected void populateItem(final ListItem<AmpGPISurveyQuestion> item) {

				Set<AmpGPISurveyResponse> responses = survey.getObject().getResponses();

				AmpGPISurveyResponse response = null;
				Iterator<AmpGPISurveyResponse> it = responses.iterator();
				while (it.hasNext()) {
					AmpGPISurveyResponse rs = (AmpGPISurveyResponse) it.next();
					if (rs.getAmpQuestionId().getAmpQuestionId().compareTo(item.getModelObject().getAmpQuestionId()) == 0) {
						response = rs;
						break;
					}
				}
				if (response == null) {
					response = new AmpGPISurveyResponse();
					response.setAmpGPISurveyId(survey.getObject());
					response.setAmpQuestionId(item.getModelObject());
					responses.add(response);
				}

				IModel<AmpGPISurveyResponse> responseModel = PersistentObjectModel.getModel(response);

				Label indName = new TrnLabel("qtext", new Model<String>() {
					PropertyModel<String> pm = new PropertyModel<String>(item.getModelObject(), "questionText");

					@Override
					public String getObject() {
						return (item.getModelObject().getParentQuestion() != null ? "- " : "") + pm.getObject();
					}
				});

				if (item.getModelObject().getParentQuestion() != null) {
					indName.add(new AttributeModifier("style", "padding-left:4em;font-style:italic"));
				}

				item.add(indName);

				AmpTextFieldPanel<String> references = new AmpTextFieldPanel<String>("references", new PropertyModel<String>(responseModel, "references"), "References:", false, true);
				item.add(references);

				String qtype = item.getModelObject().getAmpTypeId().getName();
				if (qtype.compareTo("yes-no") == 0) {
					final String[] elements = new String[] { "Yes", "No" };
					RadioChoice<String> answer = new RadioChoice<String>("answer", new GPIYesNoAnswerModel(new PropertyModel<String>(responseModel, "response")), Arrays.asList(elements));
					answer.setSuffix(" ");
					item.add(answer);

					TextField hidden = new TextField("answerInput");
					hidden.setVisible(false);
					item.add(hidden);

				} else if (qtype.compareTo("calculated") == 0) {
					Label l = new Label("answer", "");
					item.add(l);
					TextField hidden = new TextField("answerInput");
					hidden.setVisible(false);
					item.add(hidden);
				} else if (qtype.compareTo("input") == 0) {
					TextField<String> input = new TextField<String>("answerInput", new PropertyModel<String>(responseModel, "response"));
					item.add(input);
					WebMarkupContainer hidden = new WebMarkupContainer("answer");
					hidden.setVisible(false);
					item.add(hidden);
				}
			}
		};
		list.setReuseItems(true);
		add(list);
	}
}
