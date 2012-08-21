package org.dgfoundation.amp.onepager.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

public abstract class ListEditor<T> extends RepeatingView implements IFormModelUpdateListener{
	private static final long serialVersionUID = 1L;
	public transient List<T> items = new ArrayList<T>();
	IModel<Set<T>> model;
	private transient Comparator<T> comparator;

	public ListEditor(String id, IModel<Set<T>> model){
		this(id, model, null);
	}
	public ListEditor(String id, IModel<Set<T>> model, Comparator<T> comparator){
		super(id, model);
		setOutputMarkupId(true);
		this.model = model;
		this.comparator = comparator;
	}

	protected abstract void onPopulateItem(ListItem<T> item);

	public int getCount(){
		int ret = 0;
		if (items != null)
			ret = items.size();
		return ret;
	}
	
	public void addItem(T value){
		origAddItem(value);
	}
	
	public final void origAddItem(T value){
		items.add(value);
		ListItem<T> item = new ListItem<T>(newChildId(), 
				items.size() - 1);
		add(item);
		onPopulateItem(item);
		updateModel();
	}

	protected void onBeforeRender(){
		if (!hasBeenRendered())	{
			items = new ArrayList<T>((Set<T>)model.getObject());
			if (comparator != null)
				Collections.sort(items, comparator);
			for (int i = 0; i < items.size(); i++){
				ListItem<T> li = new ListItem<T>(newChildId(), i);
				add(li);
				onPopulateItem(li);
			}
		}
		super.onBeforeRender();
	}

	@Override
	public void updateModel(){
		Set<T> set = model.getObject();
		if (set == null)
			set = new HashSet<T>();
		set.clear();
		set.addAll(items);
		//setObject last, in order to work with custom set models
		model.setObject(set);
	}
}