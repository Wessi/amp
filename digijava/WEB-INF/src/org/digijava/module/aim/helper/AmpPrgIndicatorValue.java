package org.digijava.module.aim.helper;

import java.io.Serializable;

public class AmpPrgIndicatorValue implements Serializable
{
	private String creationDate;
	private int valueType;
	private Double valAmount;

	/**
	 * @return Returns the creationDate.
	 */
	public String getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate The creationDate to set.
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return Returns the valueType.
	 */
	public int getValueType() {
		return valueType;
	}
	/**
	 * @param valueType The valueType to set.
	 */
	public void setValueType(int valueType) {
		this.valueType = valueType;
	}
	/**
	 * @return Returns the valAmount.
	 */
	public Double getValAmount() {
		return valAmount;
	}
	/**
	 * @param valAmount The valAmount to set.
	 */
	public void setValAmount(Double valAmount) {
		this.valAmount = valAmount;
	}
}