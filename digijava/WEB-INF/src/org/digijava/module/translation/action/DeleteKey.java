/*
 *   DeleteKey.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sept 28, 2004
 * 	 CVS-ID: $Id: DeleteKey.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/

package org.digijava.module.translation.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.translation.form.AdvancedTranslationForm;
import org.digijava.module.translation.security.TranslateObject;
import org.digijava.module.translation.security.TranslatePermission;
import org.digijava.module.translation.security.TranslateSecurityManager;
import org.digijava.module.translation.util.DbUtil;

public class DeleteKey
      extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	AdvancedTranslationForm formBean = (AdvancedTranslationForm) form;

	boolean permitted = true;

	if (!DgUtil.isLocalTranslatorForSite(request)) {
	    permitted = TranslateSecurityManager.getInstance().
		  checkPermission(
		  DgSecurityManager.getSubject(request), TranslateObject.class,
		  new TranslateObject(formBean.getSiteId(),
				      formBean.getSelectedLangSource()),
		  TranslatePermission.INT_TRANSLATE);
	}

	if (permitted) {
	    if (formBean.getKey() != null) {
		String decoded = DgUtil.decodeString(formBean.getKey());
		DbUtil.deleteMessage(decoded, formBean.getSelectedLangSource(),
				     formBean.getSiteId());
	    }
	}

	return mapping.findForward("forward");
    }
}