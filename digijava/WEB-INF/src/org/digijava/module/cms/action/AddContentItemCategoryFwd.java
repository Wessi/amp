/*
 *   AddContentItemCategoryFwd.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: May 7, 2004
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

package org.digijava.module.cms.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.cms.form.CMSContentItemForm;


public class AddContentItemCategoryFwd extends Action {
  public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
      String forward = "addContentItemCategory";
      CMSContentItemForm contentItemForm = (CMSContentItemForm) form;

      contentItemForm.setNoReset(true);

      ActionForward fwd = new ActionForward();
      switch (contentItemForm.getProcessingMode()) {
        case CMSContentItemForm.MODE_NEW:
          fwd.setPath(mapping.findForward(forward).getPath() +
                      "&processingMode=" +
                      String.valueOf(CMSContentItemForm.MODE_NEW));
          break;
        case CMSContentItemForm.MODE_EDIT:
          fwd.setPath(mapping.findForward(forward).getPath() +
                      "&processingMode=" +
                      String.valueOf(CMSContentItemForm.MODE_EDIT));

          break;
          default:
            fwd.setPath(mapping.findForward(forward).getPath() +
                        "&processingMode=" +
                        String.valueOf(CMSContentItemForm.MODE_EDIT));

              break;
      }
      return fwd;
    }

}