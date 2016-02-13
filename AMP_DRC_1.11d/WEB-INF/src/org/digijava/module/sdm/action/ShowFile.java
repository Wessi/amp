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

package org.digijava.module.sdm.action;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.form.SdmForm;
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.util.DbUtil;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowFile
    extends Action {

  // log4J class initialize String
  private static Logger logger = I18NHelper.getKernelLogger(ShowFile.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws
      java.lang.Exception {

    SdmForm formBean = (SdmForm) form;
    Sdm sdmDoc = null;
    SdmItem sdmItem = null;

    Long paramDocId = new Long(Long.parseLong(request.getParameter("documentId")));

    if (paramDocId != null) {
      sdmDoc = DbUtil.getDocument(paramDocId);
    }
    else {
      sdmDoc = formBean.getSdmDocument();
    }
    if (sdmDoc != null) {
      Long paramParagId = new Long(Long.parseLong(request.getParameter("activeParagraphOrder")));

      if (paramParagId != null) {

        sdmItem = sdmDoc.getItemByIndex(paramParagId);

        if (sdmItem != null) {
          byte[] file = sdmItem.getContent();

          if (file != null) {
            ResponseUtil.writeFile(response, sdmItem.getContentType(),
                                   sdmItem.getContentText(), file);

          }
        }
      }
    }
    return null;
  }

}