/**
 * 
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.validators.AmpTreeCollectionValidator;
import org.digijava.module.aim.util.AmpComboboxDisplayable;

/**
 * @author mihai
 *
 */
public abstract class AmpTreeCollectionValidatorField<T> extends
		AmpCollectionValidatorField<T, String> {

	/**
	 * @param id
	 * @param collectionModel
	 * @param fmName
	 */
	public AmpTreeCollectionValidatorField(String id,
			IModel<? extends Collection<T>> collectionModel, String fmName) {
		super(id, collectionModel, fmName);
		hiddenContainer.setType(String.class);
		hiddenContainer.add(new AmpTreeCollectionValidator());
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField#getHiddenContainerModel(org.apache.wicket.model.IModel)
	 */
	@Override
	public AbstractReadOnlyModel getHiddenContainerModel(
			final IModel<? extends Collection<T>> collectionModel) {
			AbstractReadOnlyModel<String> model=new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				Set<AmpComboboxDisplayable> quickItems=new TreeSet<AmpComboboxDisplayable>();
				for (T t : collectionModel.getObject()) quickItems.add(getItem(t));
				
				Set<String> ret=new TreeSet<String>();
				for (T t : collectionModel.getObject()) {
					AmpComboboxDisplayable node=getItem(t).getParent();
					while(node!=null) {
						if(quickItems.contains(node)) ret.add(getItem(t)+"("+node+")");
						node=node.getParent();
					}
				}
						
				if(ret.size()>0) 
						return ret.toString();
				return "";
			}
		};
		return model;
	}

	public abstract AmpComboboxDisplayable getItem(T t);
	
}
