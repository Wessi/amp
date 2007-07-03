/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Iterator;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;

public class AmpFieldsVisibility extends AmpObjectVisibility implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1255296454545642749L;
	/**
	 * 
	 */


	public int compareTo(Object arg0) {
		AmpFieldsVisibility x=(AmpFieldsVisibility) arg0;
		return this.getId().compareTo(x.getId());	
	}
	
	public String getVisible() {
		return templates.contains(parent.getParent().getParent())?"true":"false";
	}

	public AmpTemplatesVisibility getTemplate() {
		return parent.getTemplate();
	} 
	
	public boolean isVisibleTemplateObj(AmpTemplatesVisibility aObjVis){
		for(Iterator it=aObjVis.getFields().iterator();it.hasNext();)
		{
			AmpFieldsVisibility x=(AmpFieldsVisibility) it.next();
			if(x.getId().compareTo(id)==0) return true;
			
		}
		return false;
	}
		
}