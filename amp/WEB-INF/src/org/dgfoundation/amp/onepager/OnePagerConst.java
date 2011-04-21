/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager;

import java.util.HashSet;
import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.helper.Constants;

/**
 * One Pager Constants
 * @author mpostelnicu@dgateway.org
 * since Nov 11, 2010
 */
public final class OnePagerConst {
	public final static MetaInfo<Integer>[] adjustmentTypes=new MetaInfo[] { new MetaInfo<Integer>("Actual",Constants.ACTUAL), 
		new MetaInfo<Integer>("Planned" ,Constants.PLANNED),  new MetaInfo<Integer>("Pipeline",Constants.ADJUSTMENT_TYPE_PIPELINE )};
	public final static MetaInfo<Integer>[] adjustmentTypesShort=new MetaInfo[] { new MetaInfo<Integer>("Actual",Constants.ACTUAL), 
		new MetaInfo<Integer>("Planned" ,Constants.PLANNED)};
	
	
	public static final MetaDataKey<HashSet<TemporaryDocument>> RESOURCES_NEW_ITEMS = new MetaDataKey<HashSet<TemporaryDocument>>(){};
	public static final MetaDataKey<HashSet<AmpActivityDocument>> RESOURCES_DELETED_ITEMS = new MetaDataKey<HashSet<AmpActivityDocument>>(){};
	
	//TODO: please load this as a JS resource, DO NOT PUT JS scripts in java unless they only invoke a function
	public final static String slideToggle = "$('a.slider').click(function(){$(this).siblings('div:first').slideToggle();return false;});";
	public final static String toggleJS= "$('#%s').click(function(){$(this).siblings('div:first').slideToggle();return false;})";
	public final static String toggleChildrenJS = "$('#%s').find('a.slider').click(function(){$(this).siblings('div:first').slideToggle();return false;})";
	public final static String clickToggleJS= "$('#%s').siblings('div:first').slideToggle();";
	public final static String clickToggle2JS= "$('#%s').find('div:first').find('div:first').slideToggle();";

	public final static String toggleJSPM ="$(document).ready(function(){$('#%s').click(function(){$(this).siblings('div:first').slideToggle();return false;});})";
	
	public static String getToggleJS(Component c)
	{
		return String.format(toggleJS, c.getMarkupId());
	}

	public static String getToggleChildrenJS(Component c)
	{
		return String.format(toggleChildrenJS, c.getMarkupId());
	}

	public static String getToggleJSPM(Component c)
	{
		return String.format(toggleJSPM, c.getMarkupId());
	}
	
	/**
	 * Use this when c is a sibbling with the slider or the slider itself
	 * @param c
	 * @return
	 */
	public static String getClickToggleJS(Component c){
		return String.format(clickToggleJS, c.getMarkupId());
	}

	/**
	 * Use this when c is the parent to the slider
	 * @param c
	 * @return
	 */
	public static String getClickToggle2JS(Component c){
		return String.format(clickToggle2JS, c.getMarkupId());
	}
}
