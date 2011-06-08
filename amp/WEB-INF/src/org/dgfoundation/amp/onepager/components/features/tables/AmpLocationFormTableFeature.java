/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageValidationHiddenField;
import org.dgfoundation.amp.onepager.models.AmpLocationSearchModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * @author mpostelnicu@dgateway.org since Oct 20, 2010
 */
public class AmpLocationFormTableFeature extends
		AmpFormTableFeaturePanel<AmpActivityVersion, AmpActivityLocation> {

	protected String getFormattedLocationName(AmpCategoryValueLocations l) {
		return getFormattedLocationName(new StringBuffer(), l).toString();
	}

	protected StringBuffer getFormattedLocationName(StringBuffer output,
			AmpCategoryValueLocations l) {
		if (l.getParentLocation() != null)
			getFormattedLocationName(output, l.getParentLocation());
		return output.append("[").append(l.getName()).append("] ");
	}

	/**
	 * @param id
	 * @param fmName
	 * @param regionalFundingFeature
	 * @throws Exception
	 */
	public AmpLocationFormTableFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am,
			final AmpRegionalFundingFormSectionFeature regionalFundingFeature,
			final AmpCategorySelectFieldPanel implementationLocation)
			throws Exception {
		super(id, am, fmName);
		final IModel<Set<AmpActivityLocation>> setModel = new PropertyModel<Set<AmpActivityLocation>>(
				am, "locations");

	
		final AmpPercentageValidationHiddenField<AmpActivityLocation> percentageValidationField=
			new AmpPercentageValidationHiddenField<AmpActivityLocation>("locationPercentageTotal",setModel,"locationPercentageTotal") {
				@Override
				public Number getPercentage(AmpActivityLocation item) {
					return item.getLocationPercentage();
				}
		};
		
		add(percentageValidationField);
		
		AbstractReadOnlyModel<List<AmpActivityLocation>> listModel = OnePagerUtil
				.getReadOnlyListModelFromSetModel(setModel);

		list = new ListView<AmpActivityLocation>("listLocations", listModel) {

			@Override
			protected void populateItem(final ListItem<AmpActivityLocation> item) {
				final MarkupContainer listParent = this.getParent();
				PropertyModel<Double> percModel = new PropertyModel<Double>(item.getModel(),"locationPercentage");
				AmpPercentageTextField percentageField=new AmpPercentageTextField("percentage",
						percModel,"locationPercentage",percentageValidationField);				
				item.add(percentageField);
				item.add(new Label("locationLabel",
						getFormattedLocationName(item.getModelObject()
								.getLocation().getLocation())));

				AmpDeleteLinkField delLocation = new AmpDeleteLinkField(
						"delLocation",
						"Delete Location",
						new Model<String>(
								"Delete this location and any related funding elements, if any?")) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						
						// toggleHeading(target, setModel.getObject());

						// remove any regional funding with this region
						if (CategoryManagerUtil
								.equalsCategoryValue(
										item.getModelObject().getLocation()
												.getLocation()
												.getParentCategoryValue(),
										CategoryConstants.IMPLEMENTATION_LOCATION_REGION)) {
							final IModel<Set<AmpRegionalFunding>> regionalFundings = new PropertyModel<Set<AmpRegionalFunding>>(
									am, "regionalFundings");
							Iterator<AmpRegionalFunding> iterator = regionalFundings.getObject().iterator();
							while (iterator.hasNext()) {
								AmpRegionalFunding ampRegionalFunding = (AmpRegionalFunding) iterator
										.next();
								if (ampRegionalFunding.getRegionLocation().equals(
										item.getModelObject().getLocation()
												.getLocation()))
									iterator.remove();
							}
							regionalFundingFeature.getList().removeAll();
							target.addComponent(regionalFundingFeature);
							target.appendJavascript(OnePagerConst.getToggleChildrenJS(regionalFundingFeature));
							
							percentageValidationField.reloadValidationField(target);							
						}
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(listParent);
						list.removeAll();
					}

				};
				item.add(delLocation);

			}
		};
		list.setReuseItems(true);
		add(list);

		final AbstractAmpAutoCompleteTextField<AmpCategoryValueLocations> autoComplete = new AbstractAmpAutoCompleteTextField<AmpCategoryValueLocations>(
				AmpLocationSearchModel.class) {

			@Override
			protected String getChoiceValue(AmpCategoryValueLocations choice)
					throws Throwable {
				return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target,
					AmpCategoryValueLocations choice) {
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
				activityLocation.setActivity(am.getObject());
				Set<AmpActivityLocation> set = setModel.getObject();
				set.add(activityLocation);
				// toggleHeading(target, setModel.getObject());
				target.addComponent(list.getParent());
				regionalFundingFeature.getList().removeAll();
				target.addComponent(regionalFundingFeature);
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(regionalFundingFeature));
				list.removeAll();
			}

			@Override
			public Integer getChoiceLevel(AmpCategoryValueLocations choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		autoComplete.getModelParams().put(AmpLocationSearchModel.PARAM.LAYER,
				implementationLocation.getChoiceModel());
		final AmpComboboxFieldPanel<AmpCategoryValueLocations> searchLocations = new AmpComboboxFieldPanel<AmpCategoryValueLocations>(
				"searchLocations", "Search Locations", autoComplete);

		add(searchLocations);
	}

}
