package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.Output;

public class AmpAnnualProjectBudget implements Serializable, Versionable, Cloneable, Comparable {

	private static final Logger logger = Logger.getLogger(AmpAnnualProjectBudget.class);
	private Long ampAnnualProjectBudgetId;
	private Double amount;
	private Date year;
	private AmpActivityVersion activity;

	public Long getAmpAnnualProjectBudgetId() {
		return ampAnnualProjectBudgetId;
	}

	public void setAmpAnnualProjectBudgetId(Long ampAnnualProjectBudgetId) {
		this.ampAnnualProjectBudgetId = ampAnnualProjectBudgetId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getYear() {
		return year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	public AmpActivityVersion getActivity() {
		return activity;
	}

	public void setActivity(AmpActivityVersion activity) {
		this.activity = activity;
	}

	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpAnnualProjectBudget aux = (AmpAnnualProjectBudget) obj;
		String original = "" + this.getAmount() + this.getYear().getYear();
		String copy = "" + +aux.getAmount() + aux.getYear().getYear();
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(new Output(null, new String[] { "AnnualProjectBudget" }, new Object[] { this }));
		return out;
	}

	@Override
	public Object getValue() {
		return "" + this.getAmount() + " " + this.getYear().getYear();
	}

	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpAnnualProjectBudget aux = (AmpAnnualProjectBudget) clone();
		aux.activity = newActivity;
		aux.ampAnnualProjectBudgetId = null;
		return aux;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public int compareTo(Object other) {
		AmpAnnualProjectBudget oth = (AmpAnnualProjectBudget) other;
		int cmpClass = this.getClass().getName().compareTo(oth.getClass().getName());
		if (cmpClass != 0)
			return cmpClass; // normally we shouldn't be getting entries of
								// different classes

		Long id1 = this.getAmpAnnualProjectBudgetId();
		Long id2 = oth.getAmpAnnualProjectBudgetId();

		if (id1 == null) {
			if (id2 == null)
				return 0;
			return 1; // nulls go to the end
		}
		if (id2 == null)
			return -1; // nulls go to the end

		return id1.compareTo(id2);
	}

	@Override
	public boolean equals(Object other) {
		return this.compareTo(other) == 0;
	}

}