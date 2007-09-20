package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.util.Identifiable;
/**
 * Represents one of the possible values for a certain category
 * @author Alex Gartner
 *
 */
public class AmpCategoryValue implements Serializable, Identifiable, Comparable {
	private Long id;
	private AmpCategoryClass ampCategoryClass;
	private String value;
	private int index;
	
	private Set activities;
	
	public Set getActivities() {
		return activities;
	}
	public void setActivities(Set activities) {
		this.activities = activities;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public AmpCategoryClass getAmpCategoryClass() {
		return ampCategoryClass;
	}
	public void setAmpCategoryClass(AmpCategoryClass ampCategoryClass) {
		this.ampCategoryClass = ampCategoryClass;
	}
	
	public int getIndex()
	{
		return index;
	 }

	public void setIndex(int index)
	{
		this.index	= index;
	    // not used, calculated value, see getIndex() method
	}

	public String toString() {
		return value;
	}
	public Object getIdentifier() {
		return this.getId();
	}
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		AmpCategoryValue a = (AmpCategoryValue) o;
		return this.getId().compareTo(a.getId());
	}
		
}
