/*
 *   RevokeHighlight.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 11, 2003
 * 	 CVS-ID: $Id: RevokeHighlight.java,v 1.1 2005-07-06 10:34:04 rahul Exp $
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
package org.digijava.module.highlights.action;

import java.util.GregorianCalendar;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.form.HighlightItemForm;
import org.digijava.module.highlights.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;

/**
 * Action revokes archived Highlight identified by activeHighlightId identity, if the last is not null
 */

public class RevokeHighlight
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        HighlightItemForm formBean = (HighlightItemForm) form;

        User user = RequestUtils.getUser(request);

        if (user != null) {

            Highlight highlight = null;

            if (formBean.getActiveHighlightId() != null) {

                ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
                    request);
                Highlight activeHighlight = DbUtil.getHighlight(moduleInstance.
                    getSite().
                    getSiteId(), moduleInstance.getInstanceName());

                if (activeHighlight != null) {
                    activeHighlight.setActive(false);
                    activeHighlight.setUpdaterUserId(user.getId());

                    GregorianCalendar date = new GregorianCalendar();
                    activeHighlight.setUpdationDate(date.getTime());

                    DbUtil.updateHighlight(activeHighlight);
                }

                highlight = DbUtil.getHighlight(formBean.getActiveHighlightId());
                highlight.setActive(true);

                highlight.setUpdaterUserId(user.getId());
                GregorianCalendar date = new GregorianCalendar();
                highlight.setUpdationDate(date.getTime());

                DbUtil.updateHighlight(highlight);
            }

        }
        else {
            ActionErrors errors = new ActionErrors();
            errors.add(null,
                       new ActionError("error.highlights.userEmpty"));
            saveErrors(request, errors);
        }

        return mapping.findForward("forward");
    }
}