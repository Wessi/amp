/*
 *   SaveText.java
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.form.SdmForm;
import org.digijava.module.sdm.util.DbUtil;
import org.digijava.module.sdm.util.SdmCommon;
import org.digijava.module.sdm.util.SdmParagraph;

public class SaveText
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        SdmForm formBean = (SdmForm) form;

        if (formBean.getSdmDocument() != null) {
            SdmItem sdmItem = null;

            if (formBean.getSdmItem() != null) {
                sdmItem = formBean.getSdmItem();
            }
            else {
                sdmItem = new SdmItem();
            }

            SdmParagraph paragraph = SdmCommon.createParagraph(formBean);

            sdmItem.setContentType(SdmItem.TYPE_TEXT);
            sdmItem.setRealType(SdmItem.TYPE_TEXT);
            SdmCommon.fillParagraph(paragraph, sdmItem);

            sdmItem.setContentText(paragraph.getContent());
	    sdmItem.setContent(sdmItem.getContentText().getBytes());

//            sdmItem.setContentTitle(SdmCommon.createParagraph(paragraph));

            DbUtil.addUpdateItem(formBean.getSdmDocument(),sdmItem);

            formBean.setSdmDocument(DbUtil.getDocument(formBean.getSdmDocument().getId()));
            formBean.setDocumentItemsList(SdmCommon.loadDocumentItemsList(
                formBean.getSdmDocument()));
        }

        return mapping.findForward("forward");
    }
}