package org.digijava.module.esrigis.helpers;

/**
 * Simplified structure object to manage locations in JSON, see if can be replaced by AmpStructure 
 *
 */

public class Structure {
	private String name;
	private String description;
	private String lat;
	private String lon;
	private String shape;
	private String type;
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLat() {
		return lat;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLon() {
		return lon;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	public String getShape() {
		return shape;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
}
