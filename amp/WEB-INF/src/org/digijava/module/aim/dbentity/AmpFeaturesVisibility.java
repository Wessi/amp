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

public class AmpFeaturesVisibility extends AmpObjectVisibility implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7004856623866175824L;
	/**
	 * 
	 */
	

	public AmpObjectVisibility getParent() {
		return (AmpModulesVisibility)parent;
	}
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		AmpFeaturesVisibility x=(AmpFeaturesVisibility) arg0;
		return this.getId().compareTo(x.getId());
		
	}
	public String getVisible() {
		return templates.contains(parent.getParent())?"true":"false";
	}
	
	public AmpTemplatesVisibility getTemplate() {
		return parent.getTemplate();
	} 
	
	public boolean isVisibleTemplateObj(AmpTemplatesVisibility aObjVis){
		for(Iterator it=aObjVis.getFeatures().iterator();it.hasNext();)
		{
			AmpFeaturesVisibility x=(AmpFeaturesVisibility) it.next();
			if(x.getId().compareTo(id)==0) return true;
			
		}
		return false;
	}
	
	
	
}