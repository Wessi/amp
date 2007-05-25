/*
 *   SaveFile.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Dec 03, 2003
 * 	 CVS-ID: $Id$
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

package org.digijava.module.sdm.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.form.SdmForm;
import org.digijava.module.sdm.util.DbUtil;
import org.digijava.module.sdm.util.SdmCommon;
import org.digijava.module.sdm.util.SdmParagraph;

public class SaveFile
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        SdmForm formBean = (SdmForm) form;

        if (formBean.getContentTitle() == null || formBean.getContentTitle().trim().length() == 0) {
            return mapping.findForward("error");
        }

        if (formBean.getSdmDocument() != null) {
            SdmItem sdmItem = null;

            if (formBean.getSdmItem() != null) {
                sdmItem = formBean.getSdmItem();
            }
            else {
                sdmItem = new SdmItem();
            }

            SdmParagraph paragraph = SdmCommon.createParagraph(formBean);

            if (formBean.getFormFile() != null) {
                if (formBean.getFormFile().getFileSize() != 0) {
                    sdmItem.setContentType(formBean.getFormFile().
                                           getContentType());
                    sdmItem.setRealType(SdmItem.TYPE_FILE);
                    sdmItem.setContent(formBean.getFormFile().getFileData());
                }
            }

            if( sdmItem.getContent() == null ) {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.sdm.fileRequired"));
                saveErrors(request, errors );

                return mapping.getInputForward();
            }

            SdmCommon.fillParagraph(paragraph, sdmItem);
            sdmItem.setContentTitle(paragraph.getContent());
//            sdmItem.setContentTitle(SdmCommon.createParagraph(paragraph));

            // set file name
            if (formBean.getFormFile() != null &&
                formBean.getFormFile().getFileSize() != 0) {
                sdmItem.setContentText(formBean.getFormFile().getFileName());
            }

            DbUtil.addUpdateItem(formBean.getSdmDocument(), sdmItem);

            formBean.setSdmDocument(DbUtil.getDocument(formBean.getSdmDocument().
                getId()));
            formBean.setDocumentItemsList(SdmCommon.loadDocumentItemsList(
                formBean.getSdmDocument()));
        }

        return mapping.findForward("forward");
    }
}