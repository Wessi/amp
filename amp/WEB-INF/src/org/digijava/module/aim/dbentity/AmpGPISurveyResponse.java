package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Output;

public class AmpGPISurveyResponse implements Versionable, Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	@Interchangeable(fieldTitle="Response ID")
	private Long ampReponseId;
	@Interchangeable(fieldTitle="GPI Survey", recursive=true)
	private AmpGPISurvey ampGPISurveyId;
	@Interchangeable(fieldTitle="GPI Survey Question")
	private AmpGPISurveyQuestion ampQuestionId;
	@Interchangeable(fieldTitle="Response")
	private String response;
	@Interchangeable(fieldTitle="References")
	private String references;

	public String getReferences() {
		return references;
	}

	public void setReferences(String references) {
		this.references = references;
	}

	public AmpGPISurveyQuestion getAmpQuestionId() {
		return ampQuestionId;
	}

	public void setAmpQuestionId(AmpGPISurveyQuestion ampQuestionId) {
		this.ampQuestionId = ampQuestionId;
	}

	public Long getAmpReponseId() {
		return ampReponseId;
	}

	public void setAmpReponseId(Long ampReponseId) {
		this.ampReponseId = ampReponseId;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public AmpGPISurvey getAmpGPISurveyId() {
		return ampGPISurveyId;
	}

	public void setAmpGPISurveyId(AmpGPISurvey ampGPISurveyId) {
		this.ampGPISurveyId = ampGPISurveyId;
	}

	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpGPISurveyResponse auxResponse = (AmpGPISurveyResponse) obj;
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
		return response;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setTitle(new String[] { " Q", this.ampQuestionId.getQuestionNumber().toString(), ": " });
		out.setOutputs(new ArrayList<Output>());
		/*
		 * if (this.response != null && !this.response.equals("nil")) {
		 * out.getOutputs().add(new Output(null, new String[] {
		 * "Response:&nbsp;" }, new Object[] { this.getResponse() })); }
		 */
		out.setValue(new Object[] { this.getResponse() != null ? this.getResponse() : "" });
		return out;
	}

	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}