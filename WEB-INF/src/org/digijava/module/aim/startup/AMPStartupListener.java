/*
 * @Author Priyajith C
 */
package org.digijava.module.aim.startup;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumnsOrder;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettings;
import org.digijava.module.aim.util.FeaturesUtil;



public class AMPStartupListener extends HttpServlet
	implements ServletContextListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5724776790911414323L;

	private static final String PATCH_METHOD_KEY = "patchAMP";

    private static Logger logger = Logger.getLogger(
            AMPStartupListener.class);
    
    protected Session session;

    public Session createSession() throws HibernateException, SQLException {
    	if(session!=null) return session;
    	session = PersistenceManager.getSession();
    	return session;
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
    public void contextInitialized(ServletContextEvent sce) {
    	ServletContext ampContext = null;

    	try {
        	ampContext = sce.getServletContext();

        	
        		ampContext.setAttribute(Constants.ME_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.AA_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.PI_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.CL_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.DC_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.SC_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.MS_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.AC_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.LB_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.SA_FEATURE,new Boolean(true));
        	
    		if (FeaturesUtil.getDefaultFlag() != null)
    			ampContext.setAttribute(Constants.DEF_FLAG_EXIST,new Boolean(true));
        
        	AmpTreeVisibility ampTreeVisibility=new AmpTreeVisibility();
        	//get the default amp template!!!
        	AmpTreeVisibility ampTreeVisibilityAux=new AmpTreeVisibility();
        	AmpTreeVisibility ampTreeVisibilityAux2=new AmpTreeVisibility();
        	Session session=this.createSession();
        	AmpTemplatesVisibility currentTemplate=FeaturesUtil.getTemplateVisibility(FeaturesUtil.getGlobalSettingValueLong("Visibility Template"),session);
        	ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
        	ampContext.setAttribute("ampTreeVisibility",ampTreeVisibility);
        	
        	currentTemplate=FeaturesUtil.getTemplateVisibility(FeaturesUtil.getGlobalSettingValueLong("Visibility Template"),session);
        	ampTreeVisibilityAux.buildAmpTreeVisibilityMultiLevel(currentTemplate);
        	//ampTreeVisibilityAux2.displayVisibilityTreeForDebug(ampTreeVisibilityAux);
        	ampContext.setAttribute("ampTreeVisibility",ampTreeVisibility);
        	Collection ampColumns=FeaturesUtil.getAMPColumnsOrder();
        	ampContext.setAttribute("ampColumnsOrder",ampColumns);
        	
        	GlobalSettings globalSettings = GlobalSettings.getInstance();
        	globalSettings.setPerspectiveEnabled(FeaturesUtil.isPerspectiveEnabled());
        	globalSettings.setShowComponentFundingByYear(FeaturesUtil.isShowComponentFundingByYear());
        	FeaturesUtil.switchLogicInstance();
        	
        	ampContext.setAttribute(Constants.GLOBAL_SETTINGS, globalSettings);
        	
    	} catch (Exception e) {
    		logger.error("Exception while initialising AMP :" + e.getMessage());
    		e.printStackTrace(System.out);
    	}
    }
}
