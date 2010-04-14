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
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.gateperm.core.GatePermConst;

public class AmpFieldsVisibility extends AmpObjectVisibility implements Serializable{
    
    	private final static String [] IMPLEMENTED_ACTIONS=new String[] { GatePermConst.Actions.EDIT, GatePermConst.Actions.VIEW } ;
    	


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
		
	public boolean isFieldActive(AmpTreeVisibility atv)
	{
		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) atv.getRoot();
		
		for(Iterator it=currentTemplate.getFields().iterator();it.hasNext();)
		{
			AmpFieldsVisibility field=(AmpFieldsVisibility) it.next();
			if(field.getName().compareTo(this.getName())==0) 
			{
				return true;
			}
			
		}
		return false;
	}

	@Override
	public String[] getImplementedActions() {
	   return IMPLEMENTED_ACTIONS;
	}


	@Override
	public Class getPermissibleCategory() {
	    return AmpFieldsVisibility.class;
	}
	@Override
	public Object getIdentifier(){
		return this.id;
	}
	
	public String getClusterIdentifier() {
		return name;
	}

			
}