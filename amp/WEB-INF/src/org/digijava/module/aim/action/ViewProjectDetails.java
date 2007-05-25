package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.MainProjectDetailsForm;

public class ViewProjectDetails extends Action {
    private static Logger logger = Logger.getLogger(ViewProjectDetails.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        MainProjectDetailsForm mpForm = (MainProjectDetailsForm) form;
        logger.debug("Description = " + mpForm.getDescription());
        logger.debug("Objectives = " + mpForm.getObjectives());
        
        return mapping.findForward("forward");
    }
}