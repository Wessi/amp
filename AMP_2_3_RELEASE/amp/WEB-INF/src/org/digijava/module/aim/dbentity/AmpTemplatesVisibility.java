/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.*;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

public class AmpTemplatesVisibility extends AmpObjectVisibility implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4765301740400470276L;
	/**
	 * 
	 */
	private Set<AmpFeaturesVisibility> features;
	private Set<AmpFieldsVisibility> fields;
	private String visible;
	
	public Set<AmpFeaturesVisibility> getFeatures() {
		return features;
	}

	public void setFeatures(Set<AmpFeaturesVisibility> features) {
		this.features = features;
	}

	public Set<AmpFieldsVisibility> getFields() {
		return fields;
	}

	public void setFields(Set<AmpFieldsVisibility> fields) 
	{
		namesCache = null;
		this.fields = fields;
	}

	public AmpObjectVisibility getParent() {
		// TODO Auto-generated method stub
		//if(getVisible()) return this;
 		return this;
	}

	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		AmpTemplatesVisibility obj=(AmpTemplatesVisibility) arg0;
		
		return this.getId().compareTo(obj.getId());
		//return 0;
	}

	public String getVisible() {
			return visible;
	}

	public AmpTemplatesVisibility getTemplate() {
		return this;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}
	
	public boolean isDefault(){
		return this.getId().equals(FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.VISIBILITY_TEMPLATE));
	}

	@Override
	public String[] getImplementedActions() {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public Class getPermissibleCategory() {
	    return AmpTemplatesVisibility.class;

	}
	
	/**
	 * fields indexed by name, for easy lookup: we don't want to iterate through 450 items for each and every of the 220 columns
	 */
	private transient Map<String, AmpFieldsVisibility> namesCache = null;
	private synchronized void buildNamesCache()
	{
		if (fields == null)
			return;
		Map<String, AmpFieldsVisibility> tempNamesCache = new HashMap<String, AmpFieldsVisibility>();
		for(AmpFieldsVisibility vis:getFields())
			tempNamesCache.put(vis.getName(), vis);
		namesCache = tempNamesCache;
	}
	
	/**
	 * returns true iff a field with a given name exists
	 * @param name
	 * @return
	 */
	public boolean fieldExists(String name)
	{
		if (fields == null)
			return false;
		
		if (namesCache == null)
			buildNamesCache();
		return namesCache.containsKey(name);
	}
	
	/**
	 * removed a field from the internal fields list and invalidates cache
	 * @param field
	 */
	public void removeField(AmpFieldsVisibility field)
	{
		if (fields != null)
			fields.remove(field);
		invalidateCache();
	}
	
	public void clearFields()
	{
		if (fields != null)
			fields.clear();
		invalidateCache();
	}
	
	/**
	 * SLOW, but seldomly called
	 * @param field
	 */
	public void addField(AmpFieldsVisibility field)
	{
		fields.add(field);
		invalidateCache(); // don't change cache here, as this is not thread safe
	}
	
	public void invalidateCache()
	{
		namesCache = null;
	}
}