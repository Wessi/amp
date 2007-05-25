/**
 * ActivitySector.java
 * 
 * @author Priyajith
 */

package org.digijava.module.aim.helper;

import java.io.Serializable;

public class ActivitySector implements Serializable{

	private Long id;

	private Long sectorId;

	private String sectorName;

	private Long subsectorLevel1Id;

	private String subsectorLevel1Name;

	private Long subsectorLevel2Id;

	private String subsectorLevel2Name;
	
	private Integer sectorPercentage;

	public ActivitySector() {
		id = new Long(-1);
		sectorId = new Long(-1);
		sectorName = null;
		subsectorLevel1Id = new Long(-1);
		subsectorLevel2Id = new Long(-1);
		subsectorLevel1Name = null;
		subsectorLevel2Name = null;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ActivitySector))
			throw new ClassCastException();

		if (obj == null)
			return false;

		ActivitySector actSect = (ActivitySector) obj;
		boolean equal = false;
		if (((actSect.getSectorId() == null && sectorId == null) || actSect
				.getSectorId().equals(sectorId))
				&& ((actSect.getSubsectorLevel1Id() == null && subsectorLevel1Id == null) || actSect
						.getSubsectorLevel1Id().equals(subsectorLevel1Id))
				&& ((actSect.getSubsectorLevel2Id() == null && subsectorLevel2Id == null) || actSect
						.getSubsectorLevel2Id().equals(subsectorLevel2Id))) {

			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return Returns the d.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the sectorId.
	 */
	public Long getSectorId() {
		return sectorId;
	}

	/**
	 * @param sectorId
	 *            The sectorId to set.
	 */
	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

	/**
	 * @return Returns the sectorName.
	 */
	public String getSectorName() {
		return sectorName;
	}

	/**
	 * @param sectorName
	 *            The sectorName to set.
	 */
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	/**
	 * @return Returns the subsectorLevel1Id.
	 */
	public Long getSubsectorLevel1Id() {
		return subsectorLevel1Id;
	}

	/**
	 * @param subsectorLevel1Id
	 *            The subsectorLevel1Id to set.
	 */
	public void setSubsectorLevel1Id(Long subsectorLevel1Id) {
		this.subsectorLevel1Id = subsectorLevel1Id;
	}

	/**
	 * @return Returns the subsectorLevel1Name.
	 */
	public String getSubsectorLevel1Name() {
		return subsectorLevel1Name;
	}

	/**
	 * @param subsectorLevel1Name
	 *            The subsectorLevel1Name to set.
	 */
	public void setSubsectorLevel1Name(String subsectorLevel1Name) {
		this.subsectorLevel1Name = subsectorLevel1Name;
	}

	/**
	 * @return Returns the subsectorLevel2Id.
	 */
	public Long getSubsectorLevel2Id() {
		return subsectorLevel2Id;
	}

	/**
	 * @param subsectorLevel2Id
	 *            The subsectorLevel2Id to set.
	 */
	public void setSubsectorLevel2Id(Long subsectorLevel2Id) {
		this.subsectorLevel2Id = subsectorLevel2Id;
	}

	/**
	 * @return Returns the subsectorLevel2Name.
	 */
	public String getSubsectorLevel2Name() {
		return subsectorLevel2Name;
	}

	/**
	 * @param subsectorLevel2Name
	 *            The subsectorLevel2Name to set.
	 */
	public void setSubsectorLevel2Name(String subsectorLevel2Name) {
		this.subsectorLevel2Name = subsectorLevel2Name;
	}

	public Integer getSectorPercentage() {
		return sectorPercentage;
	}

	public void setSectorPercentage(Integer sectorPercentage) {
		this.sectorPercentage = sectorPercentage;
	}
}