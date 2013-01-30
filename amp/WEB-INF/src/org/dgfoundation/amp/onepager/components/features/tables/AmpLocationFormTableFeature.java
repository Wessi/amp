/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.items.AmpLocationItemPanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpMinSizeCollectionValidationField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.models.AmpLocationSearchModel;
import org.dgfoundation.amp.onepager.util.AmpDividePercentageField;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author mpostelnicu@dgateway.org since Oct 20, 2010
 */
public class AmpLocationFormTableFeature extends
		AmpFormTableFeaturePanel<AmpActivityVersion, AmpActivityLocation> {

	private static final long serialVersionUID = 1L;
	private IModel<Set<AmpActivityLocation>> setModel;

	public IModel<Set<AmpActivityLocation>> getSetModel() {
		return setModel;
	}

	/**
	 * @param id
	 * @param fmName
	 * @param regionalFundingFeature
	 * @param implementationLevel 
	 * @throws Exception
	 */
	public AmpLocationFormTableFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am,
			final AmpRegionalFundingFormSectionFeature regionalFundingFeature,
			final AmpCategorySelectFieldPanel implementationLocation, final AmpCategorySelectFieldPanel implementationLevel,
			final IModel<Boolean> disablePercentagesForInternational)
			throws Exception {
		super(id, am, fmName, false, true);
		setTitleHeaderColSpan(4);
		setModel = new PropertyModel<Set<AmpActivityLocation>>(
				am, "locations");
		if (setModel.getObject() == null)
			setModel.setObject(new HashSet<AmpActivityLocation>());

		AbstractReadOnlyModel<List<AmpActivityLocation>> listModel = new AbstractReadOnlyModel<List<AmpActivityLocation>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<AmpActivityLocation> getObject() {
				// remove sectors with other classification
				ArrayList<AmpActivityLocation> ret = new ArrayList<AmpActivityLocation>();

				if(setModel.getObject()!=null){
					for (AmpActivityLocation ampActivityLocation : setModel.getObject()) {
						ret.add(ampActivityLocation);
					}
				}

				Comparator<AmpActivityLocation> comparator = new Comparator<AmpActivityLocation>(){
					@Override
					public int compare(AmpActivityLocation o1, AmpActivityLocation o2) {
						return o1.getLocation().getLocation().getAutoCompleteLabel().compareTo(o2.getLocation().getLocation().getAutoCompleteLabel());
					}		
				};

				Collections.sort(ret, comparator);
				return ret;
			}
		};
		WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
		add(wmc);
		AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
		wmc.add(iValidator);
		
		final AmpPercentageCollectionValidatorField<AmpActivityLocation> percentageValidationField=
			new AmpPercentageCollectionValidatorField<AmpActivityLocation>("locationPercentageTotal",listModel,"locationPercentageTotal") {
				private static final long serialVersionUID = 1L;

				@Override
				public Number getPercentage(AmpActivityLocation item) {
					return item.getLocationPercentage();
				}
		};
		percentageValidationField.setIndicatorAppender(iValidator);
		add(percentageValidationField);
		
		final AmpUniqueCollectionValidatorField<AmpActivityLocation> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpActivityLocation>(
				"uniqueLocationsValidator", listModel, "uniqueLocationsValidator") {
			private static final long serialVersionUID = 1L;

			@Override
		 	public Object getIdentifier(AmpActivityLocation t) {
				return t.getLocation().getLocation().getAutoCompleteLabel();
		 	}	
		};
		uniqueCollectionValidationField.setIndicatorAppender(iValidator);
		add(uniqueCollectionValidationField);
		
		final AmpMinSizeCollectionValidationField<AmpActivityLocation> minSizeCollectionValidationField = new AmpMinSizeCollectionValidationField<AmpActivityLocation>(
				"minSizeValidator", listModel, "Location required validator");
		minSizeCollectionValidationField.setIndicatorAppender(iValidator);
		add(minSizeCollectionValidationField);
		
		list = new ListView<AmpActivityLocation>("listLocations", listModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<AmpActivityLocation> item) {
				AmpLocationItemPanel li = new AmpLocationItemPanel("locationItem", item.getModel(), "Location Item", 
						disablePercentagesForInternational, am, regionalFundingFeature, percentageValidationField, 
						uniqueCollectionValidationField, setModel, list);
				item.add(li);
			}
		};
		list.setReuseItems(true);
		add(list);

		add(new AmpDividePercentageField<AmpActivityLocation>("dividePercentage", "Divide Percentage", "Divide Percentage", setModel, new Model<ListView<AmpActivityLocation>>(list)){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onClick(AjaxRequestTarget target) {
				String pattern = FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.DECIMAL_LOCATION_PERCENTAGES_DIVIDE);
				int factor = 1;
				if(pattern != null && Integer.parseInt(pattern) > 0){
					Integer numDecimals = Integer.parseInt(pattern);
					factor = (int)(Math.pow(10, numDecimals));
				}

				Set<AmpActivityLocation> set = setModel.getObject();
				if (set.size() == 0)
					return;
				
				int size = 0;
				Iterator<AmpActivityLocation> it = set.iterator();
				while (it.hasNext()) {
					AmpActivityLocation t = it.next();
					if (itemInCollection(t))
						size++;
				}
				
				if (size == 0)
					return;
				
				int alloc = (100*factor)/size;
				it = set.iterator();
				while (it.hasNext()) {
					AmpActivityLocation loc = it.next();
					if (!itemInCollection(loc))
						continue;
					setPercentageLocation(loc, alloc/(double)factor);
				}
				
				int dif = (100*factor) - alloc*size;
				int delta = 1;
				if (dif < 0)
					delta = -1;
				it = set.iterator();
				while (dif != 0 && it.hasNext()){
					AmpActivityLocation loc = it.next();
					if (!itemInCollection(loc))
						continue;
					setPercentageLocation(loc, (getPercentageLocation(loc) + (delta)/(double)factor));
					dif = dif - delta;
				}
				list.removeAll();
				target.add(list.getParent());
			}

			
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				if (this.isEnabled()){
					this.setEnabled(!disablePercentagesForInternational.getObject());
				}
			}
			public void setPercentageLocation(AmpActivityLocation loc, double val) {
				loc.setLocationPercentage((float) val);
			}
			public double getPercentageLocation(AmpActivityLocation loc) {
				return (double)(loc.getLocationPercentage());
			}
			@Override
			public void setPercentage(AmpActivityLocation loc, int val) {
				loc.setLocationPercentage((float) val);
			}
			@Override
			public int getPercentage(AmpActivityLocation loc) {
				return (int)((float)(loc.getLocationPercentage()));
			}
			@Override
			public boolean itemInCollection(AmpActivityLocation item) {
				return true; //all items displayed in the same list
			}
			
		});
		
		final AmpAutocompleteFieldPanel<AmpCategoryValueLocations> searchLocations=
                new AmpAutocompleteFieldPanel<AmpCategoryValueLocations>("searchLocations","Search Locations",
                        AmpLocationSearchModel.class,false) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSelect(AjaxRequestTarget target,
					AmpCategoryValueLocations choice) {
				locationSelected(choice, am, disablePercentagesForInternational);

                // toggleHeading(target, setModel.getObject());
                target.add(list.getParent());
                regionalFundingFeature.getList().removeAll();
                target.add(regionalFundingFeature);
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(regionalFundingFeature));
                percentageValidationField.reloadValidationField(target);
                uniqueCollectionValidationField.reloadValidationField(target);
                minSizeCollectionValidationField.reloadValidationField(target);
                list.removeAll();
			}

			@Override
			public Integer getChoiceLevel(AmpCategoryValueLocations choice) {
				return null;
			}

			@Override
			protected String getChoiceValue(AmpCategoryValueLocations choice) {
				return choice.getAutoCompleteLabel();
			}
		};

		searchLocations.getModelParams().put(AmpLocationSearchModel.PARAM.LAYER,
				implementationLocation.getChoiceModel());
	
		searchLocations.getModelParams().put(AmpLocationSearchModel.PARAM.LEVEL,
				implementationLevel.getChoiceModel());
		
		
		add(searchLocations);
	}

    public void locationSelected(AmpCategoryValueLocations choice, IModel<AmpActivityVersion> am, IModel<Boolean> disablePercentagesForInternational){
        AmpActivityLocation activityLocation = new AmpActivityLocation();

        AmpLocation ampLoc = LocationUtil
                .getAmpLocationByCVLocation(choice.getId());
        if (ampLoc == null) {
            ampLoc = new AmpLocation();

            ampLoc.setDescription(new String(" "));
            ampLoc.setLocation(choice);
            if (choice != null) {
                AmpCategoryValueLocations regionLocation = DynLocationManagerUtil
                        .getAncestorByLayer(
                                choice,
                                CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
                if (regionLocation != null) {
                    ampLoc.setRegionLocation(regionLocation);
                    ampLoc.setRegion(regionLocation.getName());
                }
            }
            DbUtil.add(ampLoc);
        }
        activityLocation.setLocation(ampLoc);
        if (disablePercentagesForInternational.getObject()){
            String cIso = null;
            try {
                cIso = FeaturesUtil.getDefaultCountryIso();
            } catch (Exception e) {
                logger.error(e);
            }
            AmpCategoryValueLocations defCountry = DynLocationManagerUtil.getLocationByIso(cIso, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
            if (choice.getId().longValue() == defCountry.getId().longValue())
                activityLocation.setLocationPercentage(100f);
            else
                activityLocation.setLocationPercentage(0f);
        }
        else{
            if(list.size()>0)
                activityLocation.setLocationPercentage(0f);
            else
                activityLocation.setLocationPercentage(100f);
        }

        activityLocation.setActivity(am.getObject());

        if (setModel.getObject() == null)
            setModel.setObject(new HashSet<AmpActivityLocation>());

        Set<AmpActivityLocation> set = setModel.getObject();
        set.add(activityLocation);
    }
}
