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

package org.digijava.module.editor.action.admin;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.form.EditorAdminForm;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.kernel.entity.ModuleInstance;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;



public class DeleteEditor extends Action {
    private static Logger logger = Logger.getLogger(DbUtil.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        EditorAdminForm editorAdminForm = (EditorAdminForm) form;
        String forward = "showAdmin";
        ActionErrors errors = new ActionErrors();


        String key = editorAdminForm.getKey().trim();
        String lang = editorAdminForm.getLang();
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
            request);
        String siteId = moduleInstance.getSite().getSiteId();
        if (key != null &&
            key.length()>0) {
            try {
                Editor ed = DbUtil.getEditor(siteId, key, lang);
                if (ed != null) {
                    DbUtil.deleteEditor(ed);
                }

            } catch (EditorException ex) {
                logger.debug("Unable to delete editor", ex);
                errors.add("editor" ,
                           new ActionError("error.editor.errorDeletingEditor"));
            }
        }
        return mapping.findForward(forward);
    }

}