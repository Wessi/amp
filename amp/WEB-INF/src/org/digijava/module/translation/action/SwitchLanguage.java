/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.translation.action;

import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.util.DashboardUtil;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SwitchLanguage
    extends Action {
	private static final Logger logger = Logger.getLogger(SwitchLanguage.class);
	private static final String ACT_FORM_PATH = "wicket/onepager/activity/";
	private static final String ACT_SSC_FORM_PATH = "wicket/onepager/ssc/";

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        //TranslationForm formBean = (TranslationForm) form;
        String localeKey = null;
        String referrerUrl = request.getParameter("rfr");
        localeKey = request.getParameter("code");
        Enumeration en = request.getParameterNames();
        while (en.hasMoreElements()) {
            String paramName = (String) en.nextElement();
            if (!paramName.equals("rfr")&&!paramName.equals("code")){
            	referrerUrl = referrerUrl + "&" + paramName + "=" + request.getParameter(paramName);
            }
        }
        
        if( referrerUrl != null )
            referrerUrl = URLDecoder.decode(referrerUrl, "UTF-8");
        else
            referrerUrl = "";

        //are we going back to the activity form?
       
        if (referrerUrl.contains(ACT_FORM_PATH)){ 
        	//get activity id
        	String actId = referrerUrl.substring(referrerUrl.indexOf(ACT_FORM_PATH) + ACT_FORM_PATH.length());
        	//free lock
        	ActivityGatekeeper.pageModeChange(actId);
        }
        else if (referrerUrl.contains(ACT_SSC_FORM_PATH)) {
        	String actId = referrerUrl.substring(referrerUrl.indexOf(ACT_SSC_FORM_PATH) + ACT_SSC_FORM_PATH.length());
        	ActivityGatekeeper.pageModeChange(actId);	
        }
        
        //String localeKey=(String)request.getParameter("lang");
        Locale locale = new Locale();
        locale.setCode(localeKey);
        DgUtil.switchLanguage(locale, request, response);

        if(referrerUrl.equals("/translation/default/showAdvancedTranslation.do")) {
        	referrerUrl = "/translation/showAdvancedTranslation.do";
        }
        
        //Switch language of dashboars that is a session scope variable to solve AMP-16364
        Collection<AmpDashboard> dashboards = org.digijava.module.visualization.util.DbUtil.getDashboardsToShowInMenu();
        request.getSession().setAttribute(Constants.MENU_DASHBOARDS, DashboardUtil.generateIdToNameForDashboards(dashboards));

        return new ActionForward(referrerUrl, true);
    }
}
