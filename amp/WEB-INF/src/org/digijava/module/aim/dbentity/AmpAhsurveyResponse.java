/*
 * Created on 1/03/2006
 * 
 * @author akashs
 * 
 */
package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Output;

public class AmpAhsurveyResponse implements Versionable {

	private Long ampReponseId;
	private AmpAhsurvey ampAHSurveyId;
	private AmpAhsurveyQuestion ampQuestionId;
	private String response;
	
	/**
	 * @return Returns the questionId.
	 */
	public AmpAhsurveyQuestion getAmpQuestionId() {
		return ampQuestionId;
	}
	/**
	 * @param questionId The questionId to set.
	 */
	public void setAmpQuestionId(AmpAhsurveyQuestion ampQuestionId) {
		this.ampQuestionId = ampQuestionId;
	}
	/**
	 * @return Returns the reponseId.
	 */
	public Long getAmpReponseId() {
		return ampReponseId;
	}
	/**
	 * @param reponseId The reponseId to set.
	 */
	public void setAmpReponseId(Long ampReponseId) {
		this.ampReponseId = ampReponseId;
	}
	/**
	 * @return Returns the response.
	 */
	public String getResponse() {
		return response;
	}
	/**
	 * @param response The response to set.
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	/**
	 * @return Returns the surveyId.
	 */
	public AmpAhsurvey getAmpAHSurveyId() {
		return ampAHSurveyId;
	}
	/**
	 * @param surveyId The surveyId to set.
	 */
	public void setAmpAHSurveyId(AmpAhsurvey ampAHSurveyId) {
		this.ampAHSurveyId = ampAHSurveyId;
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpAhsurveyResponse auxResponse = (AmpAhsurveyResponse) obj;
		String val1 = this.response != null ? this.response : "";
		String val2 = auxResponse.getResponse() != null ? auxResponse.getResponse() : "";
		if (this.ampQuestionId.equals(auxResponse.getAmpQuestionId()) && val1.equals(val2)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setTitle(new String[] { " Q", this.ampQuestionId.getQuestionNumber().toString(), ": " });
		if (this.response != null && !this.response.equals("nil")) {
			out.setValue(new Object[] { this.response });
		}
		return out;
	}
}