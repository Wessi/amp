package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.util.SiteUtils;
import org.apache.log4j.Logger;

public class ShowUserRegister extends Action {

	private static Logger logger = Logger.getLogger(ShowUserRegister.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {

		try {
			UserRegisterForm registerForm = (UserRegisterForm) form;
			String actionFlag = request.getParameter("actionFlag");
			logger.debug("actionFlag: " + actionFlag);
			
			if ("".equals(actionFlag) || actionFlag == null) {
				if (registerForm.getCountryResidence() == null) {
					// set country resident data
					List countries = DbUtil.getCountries();
					HashMap countriesMap = new HashMap();
					Iterator iterator = TrnUtil.getCountries(
							RequestUtils.getNavigationLanguage(request).getCode())
							.iterator();
					while (iterator.hasNext()) {
						TrnCountry item = (TrnCountry) iterator.next();
						countriesMap.put(item.getIso(), item);
					}
					// sort countries
					List sortedCountries = new ArrayList();
					iterator = countries.iterator();
					while (iterator.hasNext()) {
						Country item = (Country) iterator.next();
						sortedCountries.add(countriesMap.get(item.getIso()));
					}
					Collections.sort(sortedCountries, TrnUtil.countryNameComparator);

					if (sortedCountries != null) {
						//            sortedCountries.add(0, new Country( new Long( 0
						// ),null,"Select a country",null,null,null,null ) );
						sortedCountries
								.add(0, new TrnCountry("-1", "-- Select a country --"));
					}
					registerForm.setCountryResidence(sortedCountries);
					logger.debug("sortedCountries.size : " + sortedCountries.size());

					// set default web site
					registerForm.setWebSite("http://");

					// set Navigation languages
					Set languages = SiteUtils.getUserLanguages(RequestUtils
							.getSite(request));

					HashMap translations = new HashMap();
					iterator = TrnUtil.getLanguages(
							RequestUtils.getNavigationLanguage(request).getCode())
							.iterator();
					while (iterator.hasNext()) {
						TrnLocale item = (TrnLocale) iterator.next();
						translations.put(item.getCode(), item);
					}
					//sort languages
					List sortedLanguages = new ArrayList();
					iterator = languages.iterator();
					while (iterator.hasNext()) {
						Locale item = (Locale) iterator.next();
						sortedLanguages.add(translations.get(item.getCode()));
					}
					Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);

					registerForm.setNavigationLanguages(sortedLanguages);
					
					// set organisation types
					registerForm.setOrgTypeColl(DbUtil.getAllOrgTypes());
				}
			}
			else if ("typeSelected".equals(actionFlag)) {
				//	load organisation groups related to selected organisation-type
				registerForm.setOrgGroupColl(DbUtil.getOrgGroupByType(registerForm.getSelectedOrgType()));
				if (null != registerForm.getOrgColl() && registerForm.getOrgColl().size() != 0)
					registerForm.getOrgColl().clear();				
			}
			else if ("groupSelected".equals(actionFlag))
				//	load organisations related to selected organisation-group
				registerForm.setOrgColl(DbUtil.getOrgByGroup(registerForm.getSelectedOrgGroup()));
			
		} catch (Exception e) {
			logger.error("Exception from ShowUserRegister :" + e);
			return mapping.findForward(null);
		}
		
		return mapping.findForward("forward");
	}
}
