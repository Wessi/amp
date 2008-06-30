package org.digijava.module.message.action;

import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.digijava.module.message.form.QuartzJobClassManagerForm;
import org.digijava.module.message.util.QuartzJobClassUtils;
import org.digijava.module.message.dbentity.AmpQuartzJobClass;

public class QuartzJobClassManager extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception {

        QuartzJobClassManagerForm jcForm=(QuartzJobClassManagerForm)form;

        if("addJc".equals(jcForm.getAction())){
            jcForm.reset(mapping, request);
            return mapping.findForward("addJobClass");
        }else if("saveJc".equals(jcForm.getAction())){
            AmpQuartzJobClass jc=null;
            jc=QuartzJobClassUtils.getJobClassesByName(jcForm.getName());
            if(jc==null){
                jc=QuartzJobClassUtils.getJobClassesByClassfullName(jcForm.getClassFullname());
                if(jc==null){
                    jc=new AmpQuartzJobClass();
                    jc.setName(jcForm.getName());
                    jc.setClassFullname(jcForm.getClassFullname());
                    QuartzJobClassUtils.addJobClasses(jc);
                }
            }

        }else if("updateJc".equals(jcForm.getAction())){
            AmpQuartzJobClass jc=QuartzJobClassUtils.getJobClassesById(jcForm.getId());
            jc.setName(jcForm.getName());
            jc.setClassFullname(jcForm.getClassFullname());
            QuartzJobClassUtils.updateJobClasses(jc);

        }else if("editJc".equals(jcForm.getAction())){
            AmpQuartzJobClass jc=QuartzJobClassUtils.getJobClassesById(jcForm.getId());
            if(jc!=null){
                jcForm.setName(jc.getName());
                jcForm.setClassFullname(jc.getClassFullname());
                return mapping.findForward("editJobClass");
            }
        }else if("deleteJc".equals(jcForm.getAction())){
            QuartzJobClassUtils.deleteJobClasses(jcForm.getId());
        }

        jcForm.reset(mapping, request);
        jcForm.setClsCol(QuartzJobClassUtils.getAllJobClasses());

        return mapping.findForward("forward");
    }

    public QuartzJobClassManager() {
    }
}
