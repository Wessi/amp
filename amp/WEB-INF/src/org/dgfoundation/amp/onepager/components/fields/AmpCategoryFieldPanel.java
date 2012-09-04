/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * This class is used by all Category manager controls on the page, by
 * subclassing and implementing constructors that populate the
 * {@link AmpCategoryFieldPanel#choiceContainer} based on what type of control
 * we have (group or select)
 * 
 * @author mpostelnicu@dgateway.org since Sep 27, 2010
 */
public abstract class AmpCategoryFieldPanel extends
		AmpFieldPanel<AmpCategoryValue> {

	private static final long serialVersionUID = 8917920670614629713L;
	protected IModel<List<? extends AmpCategoryValue>> choices;
	protected Boolean selectedMultiselect;
	protected String categoryKey;
	protected boolean ordered;
	protected AbstractChoice<?, AmpCategoryValue> choiceContainer;
	protected final IModel<Set<AmpCategoryValue>> relatedChoicesModel;

	public AbstractChoice<?, AmpCategoryValue> getChoiceContainer() {
		return choiceContainer;
	}

	/**
	 * 
	 * @see AmpCategoryFieldPanel#AmpCategoryFieldPanel(String, String, String,
	 *      boolean, Boolean, IModel)
	 */
	public AmpCategoryFieldPanel(String id, String categoryKey, String fmName,
			boolean ordered, Boolean isMultiselect) throws Exception {
		this(id, categoryKey, fmName, ordered, isMultiselect, null);
	}

	/**
	 * Constructs a category field panel used by all types of category manager
	 * controls.
	 * 
	 * @param id
	 *            the id of the control on the page
	 * @param categoryKey
	 *            the category key of the category displayed
	 * @param fmName
	 *            the feature manager name from FM
	 * @param ordered
	 *            if this category list is ordered
	 * @param isMultiselect
	 *            if this category is multiselect. Use null here to read the
	 *            database state of this category, or true/false to override
	 *            that behavior
	 * @param relatedChoicesModel
	 *            if this parameter is not null, it will be used to iterate all
	 *            {@link AmpCategoryValue}S and retrieve the
	 *            {@link AmpCategoryValue#getUsedByValues()} for each, making
	 *            sure this control only displays {@link AmpCategoryValue}S that
	 *            are used by the related {@link AmpCategoryValue}S This is used
	 *            to filter linked categories
	 * @see CategoryConstants#IMPLEMENTATION_LEVEL_NAME
	 * @see CategoryConstants#IMPLEMENTATION_LOCATION_NAME
	 * @throws Exception
	 */
	public AmpCategoryFieldPanel(String id, final String categoryKey, String fmName,
			boolean ordered, Boolean isMultiselect,
			final IModel<Set<AmpCategoryValue>> relatedChoicesModel,boolean hideLabel) throws Exception {
		super(id, fmName,hideLabel);
		selectedMultiselect = isMultiselect;
		this.categoryKey = categoryKey;
		this.relatedChoicesModel = relatedChoicesModel;
		if (selectedMultiselect == null) {
			AmpCategoryClass categoryClass = CategoryManagerUtil
					.loadAmpCategoryClassByKey(categoryKey);
			selectedMultiselect = categoryClass.isMultiselect();
		}
		
		choices = new AbstractReadOnlyModel<List<? extends AmpCategoryValue>>() {
			@Override
			public List<AmpCategoryValue> getObject() {
				List<AmpCategoryValue> collectionByKey = new ArrayList<AmpCategoryValue>();
				try {
					collectionByKey.addAll(CategoryManagerUtil
							.getAmpCategoryValueCollectionByKey(categoryKey));
					if (relatedChoicesModel != null) {
						Set<AmpCategoryValue> relatedReunion = new TreeSet<AmpCategoryValue>();
						Collection<AmpCategoryValue> collection = relatedChoicesModel
								.getObject();
						for (AmpCategoryValue ampCategoryValue : collection) {
							relatedReunion.addAll(CategoryManagerUtil
									.getAmpCategoryValueFromDb(ampCategoryValue.getId(),
											true).getUsedByValues());
						}
						collectionByKey.retainAll(relatedReunion);
					}
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				return collectionByKey;
			}
		};
		
	}
	
	public AmpCategoryFieldPanel(String id, String categoryKey, String fmName,
			boolean ordered, Boolean isMultiselect,
			IModel<Set<AmpCategoryValue>> relatedChoicesModel) throws Exception  {
		this(id,categoryKey,fmName,ordered,isMultiselect,relatedChoicesModel,false);
	}
}
