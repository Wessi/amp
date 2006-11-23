package org.digijava.module.aim.helper ;

public class AllPrgIndicators
{
	private Long indicatorId;
	private String name;
	private String code;
	private String type;
	private String creationDate;
	private int valueType;
	private int category;
	private boolean npIndicator;
	/**
	 * @return Returns the category.
	 */
	public int getCategory() {
		return category;
	}
	/**
	 * @param category The category to set.
	 */
	public void setCategory(int category) {
		this.category = category;
	}
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}
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
	 * @return Returns the indicatorId.
	 */
	public Long getIndicatorId() {
		return indicatorId;
	}
	/**
	 * @param indicatorId The indicatorId to set.
	 */
	public void setIndicatorId(Long indicatorId) {
		this.indicatorId = indicatorId;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the npIndicator.
	 */
	public boolean isNpIndicator() {
		return npIndicator;
	}
	/**
	 * @param npIndicator The npIndicator to set.
	 */
	public void setNpIndicator(boolean npIndicator) {
		this.npIndicator = npIndicator;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
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
}