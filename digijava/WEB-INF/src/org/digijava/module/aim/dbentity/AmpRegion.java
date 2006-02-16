
/**
 * AmpRegion.java
 * @author Priyajith
 * Created: 17-Dec-2004
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.kernel.dbentity.Country;

public class AmpRegion implements Serializable{
	private Long ampRegionId;
	private String name;
	private String description;
/*	private Double gsLat;    */
/*	private Double gsLong;   */
	private String gsLat;
	private String gsLong;
	private String geoCode;
	private String regionCode;
	private Country country;
	
	public String toString() {
		return name!=null?name:"";
	}
	
	
	/**
	 * @return Returns the ampRegionId.
	 */
	public Long getAmpRegionId() {
		return ampRegionId;
	}
	/**
	 * @param ampRegionId The ampRegionId to set.
	 */
	public void setAmpRegionId(Long ampRegionId) {
		this.ampRegionId = ampRegionId;
	}
	/**
	 * @return Returns the country.
	 */
	public Country getCountry() {
		return country;
	}
	/**
	 * @param country The country to set.
	 */
	public void setCountry(Country country) {
		this.country = country;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the geoCode.
	 */
	public String getGeoCode() {
		return geoCode;
	}
	/**
	 * @param geoCode The geoCode to set.
	 */
	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}
	/**
	 * @return Returns the gsLat.
	 */
	public String getGsLat() {
		return gsLat;
	}
	/**
	 * @param gsLat The gsLat to set.
	 */
	public void setGsLat(String gsLat) {
		this.gsLat = gsLat;
	}
	/**
	 * @return Returns the gsLong.
	 */
	public String getGsLong() {
		return gsLong;
	}
	/**
	 * @param gsLong The gsLong to set.
	 */
	public void setGsLong(String gsLong) {
		this.gsLong = gsLong;
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
	 * @return
	 */
	public String getRegionCode() {
		return regionCode;
	}

	/**
	 * @param string
	 */
	public void setRegionCode(String string) {
		regionCode = string;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg) {
		// TODO Auto-generated method stub
		if (arg instanceof AmpRegion) {
			AmpRegion region = (AmpRegion) arg;
			return region.getAmpRegionId().equals(ampRegionId);
		}
		throw new ClassCastException();
	}

}
 