/**
 * 
 */
package org.digijava.module.contentrepository.helper;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * @author Alex Gartner
 * This class should be extended by all other classes that are referencing documents from
 * Content Repository through their UUID  
 */
public abstract class ObjectReferringDocument {
	private String uuid;
	public ObjectReferringDocument() {
		uuid	= null;
	}
	public void remove(Session session) throws HibernateException {
		this.detach();
		session.delete(this);
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * This method needs to be implemented by those classes that have relations to other entities.
	 * In this case this function should delete all relations between 'this' and the other entities.
	 *
	 */
	protected void detach() {
		;
	}
	
	
	
}