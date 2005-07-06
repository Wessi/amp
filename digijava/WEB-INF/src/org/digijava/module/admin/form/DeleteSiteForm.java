/*
 *   DeleteSiteForm.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 1, 2003
 * 	 CVS-ID: $Id: DeleteSiteForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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
package org.digijava.module.admin.form;

import org.apache.struts.action.ActionForm;


/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DeleteSiteForm
    extends ActionForm {

    private Long    selectedSiteId;
    private String  siteName;


    public Long getSelectedSiteId() {
        return selectedSiteId;
    }
    public void setSelectedSiteId(Long selectedSiteId) {
        this.selectedSiteId = selectedSiteId;
    }
    public String getSiteName() {
        return siteName;
    }
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}