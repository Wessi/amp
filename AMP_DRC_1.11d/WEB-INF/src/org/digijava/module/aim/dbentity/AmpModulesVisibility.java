/*
 * AMP FEATURE TEMPLATES
 */
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.module.aim.util.FeaturesUtil;

public class AmpModulesVisibility extends AmpObjectVisibility implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 292612393819900427L;

	/**
	 * @author dan
	 */
	private Set submodules;

	/**
	 * @see org.dgfoundation.amp.visibility.AmpObjectVisibility#getParent()
	 */
	public AmpObjectVisibility getParent() {
		return super.parent;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg0) {
		AmpObjectVisibility x = (AmpObjectVisibility) arg0;
		return this.getId().compareTo(x.getId());

	}

	/**
	 * @see org.dgfoundation.amp.visibility.AmpObjectVisibility#getVisible()
	 */
	public String getVisible() {
		return getTemplate().getItems().contains(this) ? "true" : "false";
	}

	/**
	 * @param id
	 * @return
	 */
	public boolean isVisibleId(Long id) {
		for (Iterator it = this.getTemplates().iterator(); it.hasNext();) {
			AmpTemplatesVisibility x = (AmpTemplatesVisibility) it.next();
			if (x.getId().compareTo(id) == 0)
				return true;

		}
		return false;
	}

	/**
	 * @param aObjVis
	 * @return
	 */
	public boolean isVisibleTemplateObj(AmpObjectVisibility aObjVis) {
		for (Iterator it = aObjVis.getItems().iterator(); it.hasNext();) {
			AmpModulesVisibility x = (AmpModulesVisibility) it.next();
			if (x.getId().compareTo(id) == 0)
				return true;

		}
		return false;
	}

	/**
	 * @see org.dgfoundation.amp.visibility.AmpObjectVisibility#getTemplate()
	 */
	public AmpTemplatesVisibility getTemplate() {
		return (AmpTemplatesVisibility) parent;
	}

	/**
	 * @return
	 */
	public TreeSet getSortedAlphaSubModules() {
		TreeSet mySet = new TreeSet(FeaturesUtil.ALPHA_ORDER);
		mySet.addAll(submodules);
		return mySet;
	}

	/**
	 * @return
	 */
	public Set getSubmodules() {
		return submodules;
	}

	/**
	 * @param submodules
	 */
	public void setSubmodules(Set submodules) {
		this.submodules = submodules;
	}

	/**
	 * @see org.digijava.module.gateperm.core.Permissible#getImplementedActions()
	 */
	@Override
	public String[] getImplementedActions() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.digijava.module.gateperm.core.Permissible#getPermissibleCategory()
	 */
	@Override
	public Class getPermissibleCategory() {
		return AmpModulesVisibility.class;
	}

}