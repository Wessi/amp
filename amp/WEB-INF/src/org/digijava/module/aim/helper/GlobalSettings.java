package org.digijava.module.aim.helper;

import org.dgfoundation.amp.ar.AmpARFilter;

/**
 * 
 * 
 * There is one object of this class which is initialized at context start-up
 * (AMPStartupListener). This bean is accesible from the view using the
 * following code: 
 * <p>
 * 
 * &lt;logic:equal name="globalSettings" scope="application" ...  	&gt;
 * 
 * <p>
 * Also, this bean should be updated each time GlobalSettings are being updated:
 * <p>
 * <code>
 * globalSettings = (GlobalSettings)getServlet().getServletContext().getAttribute(Constants.GLOBAL_SETTINGS);
 * globalSettings.setPerspectiveEnabled(FeaturesUtil.isPerspectiveEnabled());
 * </code> 
 * <p>
 * The name of the bean is Constants.GLOBAL_SETTINGS.
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class GlobalSettings {
	private Boolean perspectiveEnabled;

	private static final GlobalSettings INSTANCE = new GlobalSettings();
	
	public static GlobalSettings getInstance(){
		return INSTANCE;
	}
	
	private GlobalSettings(){}

	public void setPerspectiveEnabled(Boolean isPerspectiveEnabled) {
		this.perspectiveEnabled = isPerspectiveEnabled;
		AmpARFilter.showPerspectiveProperty(isPerspectiveEnabled.booleanValue());
	}

	public Boolean getPerspectiveEnabled() {
		return perspectiveEnabled;
	}
}
