/*
 *   RenderTeaser.java
 *   @Author Lasha Dolidze maka@digijava.org
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

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class RenderTeaser
    extends TilesAction {

  public ActionForward execute(ComponentContext context,
                               ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {

/*      SdmForm.ELJInstance instance = new SdmForm.ELJInstance();

      // configure Edit Java Live
      instance.setDownloadDir("../../redistributables/editlivejava/");
      instance.setName("ELJApplet1");
      instance.setConfigurationFile("../../redistributables/editlivejava/sample_eljconfig.xml");
      // ------------------------

      sdmForm.setInstance(instance);*/


    return null;
  }
}
