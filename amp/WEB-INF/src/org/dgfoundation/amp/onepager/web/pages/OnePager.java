/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.web.pages;

import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.AmpActivityFormFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpFormSectionFeaturePanel;
import org.dgfoundation.amp.onepager.helper.OnepagerSection;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

import com.rc.retroweaver.runtime.Arrays;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author mpostelnicu@dgateway.org since Sep 22, 2010
 */
public class OnePager extends AmpHeaderFooter {
	
	private static Logger logger = Logger.getLogger(OnePager.class);
	//for test purposes, it will be removed !!
	 
	protected IModel<AmpActivityVersion> am;
//	protected AmpActivityModel activityModelForSave;

	static OnepagerSection[] test = {
		new OnepagerSection("Identification", "org.dgfoundation.amp.onepager.components.features.sections.AmpIdentificationFormSectionFeature", 1, false),
		new OnepagerSection("Activity Internal IDs", "org.dgfoundation.amp.onepager.components.features.sections.AmpInternalIdsFormSectionFeature", 2, false),
		new OnepagerSection("Planning", "org.dgfoundation.amp.onepager.components.features.sections.AmpPlanningFormSectionFeature", 3, false),
		new OnepagerSection("Location", "org.dgfoundation.amp.onepager.components.features.sections.AmpLocationFormSectionFeature", 4, false, true, "org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature"),
		new OnepagerSection("Program", "org.dgfoundation.amp.onepager.components.features.sections.AmpProgramFormSectionFeature", 5, false),
		new OnepagerSection("Cross Cutting Issues", "org.dgfoundation.amp.onepager.components.features.sections.AmpCrossCuttingIssuesFormSectionFeature", 6, false),
		new OnepagerSection("Sectors", "org.dgfoundation.amp.onepager.components.features.sections.AmpSectorsFormSectionFeature", 7, false),
		new OnepagerSection("Donor Funding", "org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature", 8, false),
		new OnepagerSection("Regional Funding", "org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature", 9, false),
		new OnepagerSection("Related Organizations", "org.dgfoundation.amp.onepager.components.features.sections.AmpRelatedOrganizationsFormSectionFeature", 10, false),
		new OnepagerSection("Components", "org.dgfoundation.amp.onepager.components.features.sections.AmpComponentsFormSectionFeature", 11, false),
		new OnepagerSection("Structures", "org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature", 12, false),
		new OnepagerSection("Issues Section", "org.dgfoundation.amp.onepager.components.features.sections.AmpIssuesFormSectionFeature", 13, false),
		new OnepagerSection("Regional Observations", "org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalObservationsFormSectionFeature", 14, false),
		new OnepagerSection("Contacts", "org.dgfoundation.amp.onepager.components.features.sections.AmpContactsFormSectionFeature", 15, false),
	    new OnepagerSection("Contracts", "org.dgfoundation.amp.onepager.components.features.sections.AmpContractingFormSectionFeature", 16, false),
		new OnepagerSection("M&E", "org.dgfoundation.amp.onepager.components.features.sections.AmpMEFormSectionFeature", 17, false),
		new OnepagerSection("Paris Indicators", "org.dgfoundation.amp.onepager.components.features.sections.AmpPIFormSectionFeature", 18, false),
		new OnepagerSection("Related Documents", "org.dgfoundation.amp.onepager.components.features.sections.AmpResourcesFormSectionFeature", 19, false)
		};
	public static final LinkedList<OnepagerSection> flist = new LinkedList<OnepagerSection>(Arrays.asList(test));
	protected AbstractReadOnlyModel<List<AmpComponentPanel>> listModel;

	public static OnepagerSection findByName(String name){
		Iterator<OnepagerSection> it = flist.iterator();
		while (it.hasNext()) {
			OnepagerSection os = (OnepagerSection) it.next();
			if (os.getClassName().compareTo(name) == 0)
				return os;
		}
		return null;
	}
	
	public static OnepagerSection findByPosition(int pos){
		Iterator<OnepagerSection> it = flist.iterator();
		while (it.hasNext()) {
			OnepagerSection os = (OnepagerSection) it.next();
			if (os.getPosition() == pos)
				return os;
		}
		return null;
	}

	public OnePager(PageParameters parameters) {
		super();
		
		String activityId = (String) parameters.get("activity");
		boolean newActivity = false;
		if ((activityId == null) || (activityId.compareTo("new") == 0)){
			am = new AmpActivityModel();
			newActivity = true;
			
			
			if(parameters.get("lat") != null && parameters.get("lat") != null && parameters.get("geoId") != null && parameters.get("name") != null){
				String activityName = (String)parameters.get("name");
				String latitude = (String)parameters.get("lat");
				String longitude = (String)parameters.get("lon");
				String geoId = (String)parameters.get("geoId");
				
				initializeActivity(am.getObject(), activityName, latitude, longitude, geoId);
			}
		}
		else{
			am = new AmpActivityModel(Long.valueOf(activityId));
		}
		
		AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
		session.reset();
		PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_MEMBER, session.getCurrentMember());
		PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.ACTIVITY, am.getObject());

		try {
			initializeFormComponents(am);
			AmpActivityFormFeature formFeature= new AmpActivityFormFeature("activityFormFeature", am, "Activity Form", newActivity, listModel);
			add(formFeature);
			quickMenu(am);
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		
		
		
	}

	/**
	 * Method used to initialize an Activity/ActivityVersion with
	 * preset location information for the GIS module.
	 * This allows to add activities by clicking on a map
	 *
	 * @param activity the activity
	 * @param activityName the new activity name
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param geoId the geoId/geoCode for the location
	 */

	private void initializeActivity(AmpActivityVersion activity,
			String activityName, String latitude, String longitude, String geoId) {

		AmpActivityLocation actLoc = new AmpActivityLocation();
		AmpLocation ampLoc = LocationUtil.getAmpLocationByGeoCode(geoId);
		// This check is necessary to avoid an exception if the location doesn't
		// have geoCode
		if (ampLoc != null) {
			Set<AmpActivityLocation> locations = new HashSet<AmpActivityLocation>();
			activity.setName(activityName);
			actLoc.setLatitude(latitude);
			actLoc.setLongitude(longitude);
			actLoc.setLocationPercentage(100f);
			actLoc.setLocation(ampLoc);
			locations.add(actLoc);
			activity.setLocations(locations);
		}
	}
	private void quickMenu(IModel<AmpActivityVersion> am) {
		ListView<AmpComponentPanel> list = new ListView<AmpComponentPanel>("quickList", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpComponentPanel> item) {
				if (item.getModelObject() != null){
					Label label = new Label("quickName", item.getModelObject().getFMName());
					label.add(new SimpleAttributeModifier("href", "#" + item.getModelObject().getFMName().replaceAll(" ", "")));
					if (!item.getModelObject().isVisible())
						item.setVisible(false);
					item.add(label);
				}
			}
		};
		list.setReuseItems(true);
		add(list);
	}

	public void initializeFormComponents(final IModel<AmpActivityVersion> am) throws Exception {

		Collections.sort(flist, new Comparator<OnepagerSection>() {
			@Override
			public int compare(OnepagerSection o1, OnepagerSection o2) {
				return o1.getPosition() - o2.getPosition();
			}
		});
		listModel = new AbstractReadOnlyModel<List<AmpComponentPanel>>() {
			private List<AmpComponentPanel> list = null;
			private AmpComponentPanel initObject(OnepagerSection os, LinkedList<OnepagerSection> features, HashMap<String, AmpComponentPanel> temp){
				AmpComponentPanel dep = null;
				if (os.isDependent()){
					Iterator<OnepagerSection> it = features.iterator();
					OnepagerSection depOs = null;
					while (it.hasNext()) {
						OnepagerSection tmpos = (OnepagerSection) it
								.next();
						if (tmpos.getClassName().compareTo(os.getDependentClassName()) == 0){
							depOs = tmpos;
							break;
						}
					}
					dep = initObject(depOs, features, temp);
				}
				
				Class clazz = null;
				try {
					clazz = Class.forName(os.getClassName());
				} catch (ClassNotFoundException e) {
					logger.error("Can't find class for section:" + os.getName(), e);
				}
				Constructor constructor = null;
				try {
					if (os.isDependent())
						constructor = clazz.getConstructor(String.class, String.class, IModel.class, AmpComponentPanel.class);
					else
						constructor = clazz.getConstructor(String.class, String.class, IModel.class);
					
					AmpComponentPanel feature = null;
					if (os.isDependent())
						feature = (AmpComponentPanel) constructor.newInstance("featureItem", os.getName(), am, dep);
					else
						feature = (AmpComponentPanel) constructor.newInstance("featureItem", os.getName(), am);
					
					if (AmpFormSectionFeaturePanel.class.isAssignableFrom(feature.getClass())){
						AmpFormSectionFeaturePanel fsfp = (AmpFormSectionFeaturePanel) feature;
						fsfp.setFolded(os.isFolded());
					}
					
					temp.put(os.getClassName(), feature);
					feature.setOutputMarkupId(true);
					return feature;
				} catch (Exception e) {
					logger.error("Can't init constructor for section:" + os.getName(), e);
					return null;
				}
				
			}
			
			public List<AmpComponentPanel> initObjects(){
				Iterator<OnepagerSection> it = flist.iterator();
				LinkedList<AmpComponentPanel> ret = new LinkedList<AmpComponentPanel>();
				HashMap<String, AmpComponentPanel> temp = new HashMap<String, AmpComponentPanel>();
				while (it.hasNext()) {
					OnepagerSection os = (OnepagerSection) it
							.next();
					AmpComponentPanel fet = initObject(os, flist, temp);
					ret.add(fet);
				}
				return ret;
			}
			
			@Override
			public List<AmpComponentPanel> getObject() {
				if (list == null)
					list = initObjects();
				
				return list;
			}
		};
	}
}
