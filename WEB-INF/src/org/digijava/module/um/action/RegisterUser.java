/*
 * RegisterUser.java
 */

package org.digijava.module.um.action;


import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpUserExtension;
import org.digijava.module.aim.dbentity.AmpUserExtensionPK;
import org.digijava.module.um.form.AddUserForm;
import org.digijava.module.um.util.AmpUserUtil;
import org.digijava.module.um.util.DbUtil;

import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.persistence.WorkerException;

public class RegisterUser extends Action {

	private static Logger logger = Logger.getLogger(RegisterUser.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {

		
		/**
		 * Test if user is administrator
		 */
		HttpSession session		= request.getSession();
		String ampAdmin			= (String)session.getAttribute("ampAdmin"); 
		if ( ampAdmin == null || ampAdmin.equals("no") ) {
			return mapping.findForward("login");
		}
		
		AddUserForm userRegisterForm = (AddUserForm) form;

		logger.debug("In UserRegisterAction");

		if(!userRegisterForm.getErrors().isEmpty())
			return (mapping.getInputForward());
		try {

			User user = new User(userRegisterForm.getEmail().toLowerCase(),
					userRegisterForm.getFirstNames(), userRegisterForm
							.getLastName());

			// set client IP address
			user.setModifyingIP(RequestUtils.getRemoteAddress(request));

			// set password
			user.setPassword(userRegisterForm.getPassword().trim());
			user.setSalt(userRegisterForm.getPassword().trim());

			// set Website
			user.setUrl(userRegisterForm.getWebSite());

			// register through
			user.setRegisteredThrough(RequestUtils.getSite(request));

			// set mailing address
			user.setAddress(userRegisterForm.getMailingAddress());

			// set organization name
			user.setOrganizationName(userRegisterForm.getOrganizationName());

			user.setOrganizationTypeOther(new String(" "));
			

			// set country
            ;
            user.setCountry(org.digijava.module.aim.util.DbUtil.getDgCountry(userRegisterForm.getSelectedCountryResidence()));
            ////System.out.println(" this is the default country.... "+countryIso);
            //user.setCountry(new Country(countryIso));
			//user.setCountry(new Country(org.digijava.module.aim.helper.Constants.COUNTRY_ISO));

			// set default language
			user.setRegisterLanguage(RequestUtils
					.getNavigationLanguage(request));
                        user.setEmailVerified(false);
                        user.setActive(false);
                        user.setBanned(false);

			SiteDomain siteDomain = (SiteDomain) request
					.getAttribute(Constants.CURRENT_SITE);

			// ------------- SET USER LANGUAGES
			UserLangPreferences userLangPreferences = new UserLangPreferences(
					user, DgUtil.getRootSite(siteDomain.getSite()));

			Locale language = new Locale();
			language.setCode(userRegisterForm.getSelectedLanguage());

			// set alert language
			userLangPreferences.setAlertsLanguage(language);

			// set navigation language
			userLangPreferences.setNavigationLanguage(RequestUtils
					.getNavigationLanguage(request));
			user.setUserLangPreferences(userLangPreferences);

			// ===== start user extension setup =====
			AmpUserExtension userExt=new AmpUserExtension();
			// org type
			AmpOrgType orgType=org.digijava.module.aim.util.DbUtil.getAmpOrgType(userRegisterForm.getSelectedOrgType());
			userExt.setOrgType(orgType);
			AmpOrgGroup orgGroup=org.digijava.module.aim.util.DbUtil.getAmpOrgGroup(userRegisterForm.getSelectedOrgGroup());
			userExt.setOrgGroup(orgGroup);
			AmpOrganisation organ = org.digijava.module.aim.util.DbUtil.getOrganisation(userRegisterForm.getSelectedOrganizationId());
			userExt.setOrganization(organ);
			// ===== end user extension setup =====

			// if email register get error message

			if (DbUtil.isRegisteredEmail(user.getEmail())) {
				userRegisterForm.addError("error.registration.emailexits", "Email already exits");
				//return (new ActionForward(mapping.getInput()));
				return (mapping.getInputForward());
			} else {
				
 				
 				String des1 = "Welcome to AMP!";
 				String des2 = "AMP Administrator has created your user profile.";
 				String des3 = "Your login information:";
 				String des4 = "Username: ";
 				String cri1 = "password: ";
 				String pti1 = "Please change your password when you first login to AMP in order to keep it private.";
				
				

				TranslatorWorker transwob = new TranslatorWorker();
				Long siteId = RequestUtils.getSite(request).getId();
				String langCode= RequestUtils.getNavigationLanguage(request).getCode();
				try { 
				des1 = transwob.translateText(des1, null, langCode, siteId);
				des2 = transwob.translateText(des2, null, langCode, siteId);
				des3 = transwob.translateText(des3, null, langCode, siteId);
				des4 = transwob.translateText(des4, null, langCode, siteId);
				cri1 = transwob.translateText(cri1, null, langCode, siteId); 
				pti1 = transwob.translateText(pti1, null, langCode, siteId); 
				
				} 
				catch (WorkerException e1){
				e1.printStackTrace(); 
				}
				
				String des = des1+ '\n'+'\n'+des2 +'\n'+ des3 +'\n'+'\n'+'\t'+'\t'+ des4;
				String cri = '\n'+'\t'+'\t'+cri1;
				String pti = ""+'\n'+'\n'+ pti1;
				
				DbUtil.registerUser(user);
                if(userRegisterForm.isSendEmail()){
		    
                    String description = des + user.getEmail() + cri + userRegisterForm.getPassword()+pti;

                    DgEmailManager.sendMail(user.getEmail(), "Registration Confirmation", description);
                }	
				Site site = RequestUtils.getSite(request);
				Group memberGroup = org.digijava.module.aim.util.DbUtil.getGroup(Group.MEMBERS,site.getId());
				Long uid[] = new Long[1];
				uid[0] = user.getId();
				org.digijava.module.admin.util.DbUtil.addUsersToGroup(memberGroup.getId(),uid);

				//save amp user extensions;
				AmpUserExtensionPK extPK=new AmpUserExtensionPK(user);
				userExt.setAmpUserExtId(extPK);
				AmpUserUtil.saveAmpUserExtension(userExt);
			}
		} catch (Exception e) {
			logger.error("Exception from RegisterUser :" + e);
		}
		if(userRegisterForm.isAddWorkspace()){
			userRegisterForm.setAssignedWorskpaces(null);
			return mapping.findForward("forward");
		}
		else{
			Site site = RequestUtils.getSite(request);
			Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
			Long siteId = site.getId();
			String locale = navigationLanguage.getCode();
			userRegisterForm.addError("error.aim.addUser.success", TranslatorWorker.translateText("User registered successfully",locale,siteId));
			userRegisterForm.reset(mapping, request);
			return (mapping.findForward("index"));
		}
	}
}
