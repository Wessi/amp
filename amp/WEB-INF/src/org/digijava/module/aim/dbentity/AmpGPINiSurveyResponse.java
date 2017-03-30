package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class AmpGPINiSurveyResponse implements Serializable, Cloneable {

	private static final long serialVersionUID = -6656563271238273140L;
	
	private Long ampGPINiSurveyResponseId;
	private AmpGPINiSurvey ampGPINiSurvey;
	private AmpGPINiQuestion ampGPINiQuestion;
	private Long integerResponse;
	private String textResponse;
	private AmpGPINiQuestionOption questionOption;
	private Set<AmpGPINiSurveyResponseDocument> supportingDocuments;

	public Long getAmpGPINiSurveyResponseId() {
		return ampGPINiSurveyResponseId;
	}

	public void setAmpGPINiSurveyResponseId(Long ampGPINiSurveyResponseId) {
		this.ampGPINiSurveyResponseId = ampGPINiSurveyResponseId;
	}

	public AmpGPINiSurvey getAmpGPINiSurvey() {
		return ampGPINiSurvey;
	}

	public void setAmpGPINiSurvey(AmpGPINiSurvey ampGPINiSurvey) {
		this.ampGPINiSurvey = ampGPINiSurvey;
	}

	public AmpGPINiQuestion getAmpGPINiQuestion() {
		return ampGPINiQuestion;
	}

	public void setAmpGPINiQuestion(AmpGPINiQuestion ampGPINiQuestion) {
		this.ampGPINiQuestion = ampGPINiQuestion;
	}

	public Long getIntegerResponse() {
		return integerResponse;
	}

	public void setIntegerResponse(Long integerResponse) {
		this.integerResponse = integerResponse;
	}

	public String getTextResponse() {
		return textResponse;
	}

	public void setTextResponse(String textResponse) {
		this.textResponse = textResponse;
	}

	public AmpGPINiQuestionOption getQuestionOption() {
		return questionOption;
	}

	public void setQuestionOption(AmpGPINiQuestionOption questionOption) {
		this.questionOption = questionOption;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	};
	
	public Set<AmpGPINiSurveyResponseDocument> getSupportingDocuments() {
		return supportingDocuments;
	}

	public void setSupportingDocuments(Set<AmpGPINiSurveyResponseDocument> supportingDocuments) {
		this.supportingDocuments = supportingDocuments;
	}

	public boolean isEmpty() {
		if (ampGPINiQuestion != null) {
			switch(ampGPINiQuestion.getType()) {
				case INTEGER:
					return integerResponse != null;
				case DECIMAL:
				case FREE_TEXT:
					return StringUtils.isNotBlank(textResponse);
				case LINK:
				case DOCUMENT:
					return supportingDocuments != null && !supportingDocuments.isEmpty();
				case MULTIPLE_CHOICE:
					return questionOption != null;
				
				default:
					return StringUtils.isNotBlank(textResponse);
			}
		}
		
		return false;
	}
}
