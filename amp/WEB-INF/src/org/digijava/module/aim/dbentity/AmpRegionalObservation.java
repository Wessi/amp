package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.digijava.module.aim.util.Output;

public class AmpRegionalObservation implements Serializable, Versionable, Cloneable {

	private Long ampRegionalObservationId;
	private String name;
	private AmpActivityVersion activity;
	private Set<AmpRegionalObservationMeasure> regionalObservationMeasures;
	private Date observationDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AmpActivityVersion getActivity() {
		return activity;
	}

	public void setActivity(AmpActivityVersion activity) {
		this.activity = activity;
	}

	public boolean equals(Object arg) {
		if (arg instanceof AmpRegionalObservation) {
			AmpRegionalObservation issue = (AmpRegionalObservation) arg;
			return issue.getAmpRegionalObservationId().equals(ampRegionalObservationId);
		}
		throw new ClassCastException();
	}

	public Long getAmpRegionalObservationId() {
		return ampRegionalObservationId;
	}

	public void setAmpRegionalObservationId(Long ampRegionalObservationId) {
		this.ampRegionalObservationId = ampRegionalObservationId;
	}

	public Set getRegionalObservationMeasures() {
		return regionalObservationMeasures;
	}

	public void setRegionalObservationMeasures(Set regionalObservationMeasures) {
		this.regionalObservationMeasures = regionalObservationMeasures;
	}

	public Date getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(Date observationDate) {
		this.observationDate = observationDate;
	}

	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpRegionalObservation aux = (AmpRegionalObservation) obj;
		String original = this.name != null ? this.name : "";
		String copy = aux.name != null ? aux.name : "";
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(
				new Output(null, new String[] { "&nbsp;Name:&nbsp;" }, new Object[] { this.name != null ? this.name
						: "Empty Name" }));
		if (this.observationDate != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "&nbsp;Date:&nbsp;" }, new Object[] { this.observationDate }));
		}

		return out;
	}

	@Override
	public Object getValue() {
		String value = " " + this.name + this.observationDate;
		//Iterator<AmpRegionalObservationMeasure> i = this.regionalObservationMeasures.iterator() 
		return value;
	}

	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpRegionalObservation aux = (AmpRegionalObservation) clone();
		aux.activity = newActivity;
		aux.ampRegionalObservationId = null;
		return aux;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}