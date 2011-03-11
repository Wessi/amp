/*
 * Created on 1/03/2006
 *
 * @author akashs
 *
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.form.EditActivityForm.Survey;
import org.digijava.module.aim.util.Output;

import com.rc.retroweaver.runtime.Collections;

public class AmpAhsurvey implements Versionable, Serializable, Cloneable {

	private Long ampAHSurveyId;

	//private AmpFunding ampFundingId;
	//private Integer surveyYear;
	//point of delivery donor
	private AmpActivity ampActivityId;
	private AmpOrganisation ampDonorOrgId;
    private AmpOrganisation pointOfDeliveryDonor;
	private Set<AmpAhsurveyResponse> responses;

	/**
	 * @return Returns the ampAHSurveyId.
	 */
	public Long getAmpAHSurveyId() {
		return ampAHSurveyId;
	}
	/**
	 * @param ampAHSurveyId The ampAHSurveyId to set.
	 */
	public void setAmpAHSurveyId(Long ampAHSurveyId) {
		this.ampAHSurveyId = ampAHSurveyId;
	}
	/**
	 * @return Returns the responses.
	 */
	public Set<AmpAhsurveyResponse> getResponses() {
		return responses;
	}
	/**
	 * @param responses The responses to set.
	 */
	public void setResponses(Set<AmpAhsurveyResponse> responses) {
		this.responses = responses;
	}
	/**
	 * @return Returns the ampActivityId.
	 */
	public AmpActivity getAmpActivityId() {
		return ampActivityId;
	}
	/**
	 * @param ampActivityId The ampActivityId to set.
	 */
	public void setAmpActivityId(AmpActivity ampActivityId) {
		this.ampActivityId = ampActivityId;
	}
	/**
	 * @return Returns the ampDonorOrgId.
	 */
	public AmpOrganisation getAmpDonorOrgId() {
		return ampDonorOrgId;
	}

    public AmpOrganisation getPointOfDeliveryDonor() {
        return pointOfDeliveryDonor;
    }

    /**
	 * @param ampDonorOrgId The ampDonorOrgId to set.
	 */
	public void setAmpDonorOrgId(AmpOrganisation ampDonorOrgId) {
		this.ampDonorOrgId = ampDonorOrgId;
	}

    public void setPointOfDeliveryDonor(AmpOrganisation pointOfDeliveryDonor) {
        this.pointOfDeliveryDonor = pointOfDeliveryDonor;
    }
    
    @Override
	public boolean equals(Object obj) {
		if(obj instanceof AmpAhsurvey) {
			AmpAhsurvey aux = (AmpAhsurvey) obj;
			if (aux.getAmpAHSurveyId() != null && aux.getAmpAHSurveyId() == this.getAmpAHSurveyId()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		if(this.ampAHSurveyId != null) {
			return this.ampAHSurveyId.intValue();
		} else {
			return 0;
		}
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpAhsurvey aux = (AmpAhsurvey) obj;
		if (this.ampDonorOrgId.equals(aux.getAmpDonorOrgId())) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Object getValue() {

		Comparator surveyComparator = new Comparator() {
			public int compare(Object o1, Object o2) {
				AmpAhsurveyResponse aux1 = (AmpAhsurveyResponse) o1;
				AmpAhsurveyResponse aux2 = (AmpAhsurveyResponse) o2;
				return aux1.getAmpQuestionId().getAmpQuestionId().compareTo(aux2.getAmpQuestionId().getAmpQuestionId());
			}
		};

		String ret = "";
		ret = this.pointOfDeliveryDonor.getAcronym();
		List<AmpAhsurveyResponse> auxList = new ArrayList<AmpAhsurveyResponse>(this.responses);
		Collections.sort(auxList, surveyComparator);
		Iterator<AmpAhsurveyResponse> iter = auxList.iterator();
		while (iter.hasNext()) {
			AmpAhsurveyResponse auxResponse = iter.next();
			ret = ret
					+ ((auxResponse.getResponse() != null && !auxResponse.getResponse().equals("nil")) ? auxResponse
							.getResponse() : "-");
		}
		return ret;
	}
	
	@Override
	public Output getOutput() {
		Comparator surveyComparator = new Comparator() {
			public int compare(Object o1, Object o2) {
				AmpAhsurveyResponse aux1 = (AmpAhsurveyResponse) o1;
				AmpAhsurveyResponse aux2 = (AmpAhsurveyResponse) o2;
				return aux1.getAmpQuestionId().getAmpQuestionId().compareTo(aux2.getAmpQuestionId().getAmpQuestionId());
			}
		};

		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(new Output(null, new String[] {"Donor:&nbsp;" }, new Object[] { this.ampDonorOrgId.getName() }));
		out.getOutputs().add(
				new Output(null, new String[] {"<br/>", "PoDD:&nbsp;" }, new Object[] { this.pointOfDeliveryDonor.getName() }));
		out.getOutputs().add(
				new Output(new ArrayList(), new String[] { "<br/>", "Questions:" }, new Object[] { "" }));
		Output questions = out.getOutputs().get(out.getOutputs().size() - 1);

		if (this.responses != null) {
			List<AmpAhsurveyResponse> auxList = new ArrayList<AmpAhsurveyResponse>(this.responses);
			Collections.sort(auxList, surveyComparator);
			Iterator<AmpAhsurveyResponse> iter = auxList.iterator();
			while (iter.hasNext()) {
				AmpAhsurveyResponse auxResponse = iter.next();
				Output auxOutResp = new Output();
				auxOutResp.setTitle(auxResponse.getOutput().getTitle());
				auxOutResp.setValue(auxResponse.getOutput().getValue());
				if (auxOutResp.getValue() != null) {
					questions.getOutputs().add(auxOutResp);
				}
			}
		}
		return out;
	}
	
	@Override
	public Object prepareMerge(AmpActivity newActivity) throws CloneNotSupportedException {
		this.ampActivityId = newActivity;
		this.ampAHSurveyId = null;
		if (this.responses != null && this.responses.size() > 0) {
			Set<AmpAhsurveyResponse> responses = new HashSet<AmpAhsurveyResponse>();
			Iterator<AmpAhsurveyResponse> i = this.responses.iterator();
			while (i.hasNext()) {
				AmpAhsurveyResponse newResp = (AmpAhsurveyResponse) i.next().clone();
				newResp.setAmpAHSurveyId(this);
				newResp.setAmpReponseId(null);
				responses.add(newResp);
			}
			this.responses = responses;
		} else {
			this.responses = null;
		}
		return this;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}