/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager;

import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpSession;

import org.apache.wicket.MetaDataKey;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.digijava.kernel.startup.AmpSessionListener;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.helper.Constants;
import org.hibernate.Session;

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

	public static final MetaDataKey<HashMap<String, String>> EDITOR_ITEMS = new MetaDataKey<HashMap<String, String>>(){};

	public static final MetaDataKey<HashSet<AmpComments>> COMMENTS_ITEMS = new MetaDataKey<HashSet<AmpComments>>(){};
	public static final MetaDataKey<HashSet<AmpComments>> COMMENTS_DELETED_ITEMS = new MetaDataKey<HashSet<AmpComments>>(){};
	
	
	/**
	 * {@linkplain http://community.jboss.org/wiki/OpenSessionInView}
	 * manual session-per-conversation model
	 * We do not use Wicket MetaDatKeyS because we want to store this in the big {@link HttpSession} 
	 * Thus we can use a session listener to catch any {@link HttpSession#invalidate()} and close the Hibernate session too
	 * @see AmpSessionListener
	 */
	public static final String ONE_PAGER_HIBERNATE_SESSION_KEY =  "onePagerHibernateSessionKey";

	


	/**
	 * NOTICE: please load this as a JS resource, DO NOT PUT JS scripts in java unless they only invoke a function
	 */
	/**
	 * @deprecated
	 * Don't use this unless necessary, it updates all
	 * sliders on page and some might get the click set 
	 * more than once!
	 */
	@Deprecated
	private final static String slideToggle = "$('a.slider').click(function(){$(this).siblings('div:first').slideToggle();return false;});";

	final static String toggleJS= "$('#%s').click(function(){$(this).siblings('div:first').slideToggle();return false;})";
	final static String toggleChildrenJS = "$('#%s').find('a.slider').click(function(){$(this).siblings('div:first').slideToggle();return false;})";
	final static String clickToggleJS= "$('#%s').siblings('div:first').slideToggle();";
	final static String clickToggle2JS= "$('#%s').find('div:first').find('div:first').slideToggle();";
	final static String toggleJSPM ="$(document).ready(function(){$('#%s').click(function(){$(this).siblings('div:first').slideToggle();return false;});})";
}
