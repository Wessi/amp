/*
 * Measures.java
 * Created : 07-Sep-2005
 */
package org.digijava.module.aim.helper;

import java.util.ArrayList;

public class Measures {
	private Long id;
	private String name;
	private ArrayList actors;
	/**
	 * @return Returns the actors.
	 */
	public ArrayList getActors() {
		return actors;
	}
	/**
	 * @param actors The actors to set.
	 */
	public void setActors(ArrayList actors) {
		this.actors = actors;
	}
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
		if (arg instanceof Measures) {
			Measures measure = (Measures) arg;
			return measure.getId().equals(id);
		}
		throw new ClassCastException();
	}
}