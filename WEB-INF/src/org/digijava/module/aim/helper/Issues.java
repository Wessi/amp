/*
 * Issues.java
 * Created : 07-Sep-2005
 */

package org.digijava.module.aim.helper;

import java.util.ArrayList;

public class Issues {
	private Long id;
	private String name;
	private ArrayList measures;
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the measures.
	 */
	public ArrayList getMeasures() {
		return measures;
	}
	/**
	 * @param measures The measures to set.
	 */
	public void setMeasures(ArrayList measures) {
		this.measures = measures;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg) {
		if (arg instanceof Issues) {
			Issues issue = (Issues) arg;
			return issue.getId().equals(id);
		}
		throw new ClassCastException();
	}
	
}