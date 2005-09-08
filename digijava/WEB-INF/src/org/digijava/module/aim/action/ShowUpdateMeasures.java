/*
 * ShowUpdateMeasures.java
 * Created : 06-Sep-2005
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Measures;

public class ShowUpdateMeasures extends Action {
	
	private static Logger logger = Logger.getLogger(ShowUpdateMeasures.class);
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
	
		EditActivityForm eaForm = (EditActivityForm) form;
		logger.debug("In showUpdateMeasures");
		if (eaForm.getMeasureId() != null && 
				eaForm.getIssueId() != null && 
				eaForm.getIssueId().longValue() > 0 && 
				eaForm.getMeasureId().longValue() > 0) {
			
			logger.debug("In here");
			Issues issue = new Issues();
			issue.setId(eaForm.getIssueId());
			int index = eaForm.getIssues().indexOf(issue);
			issue = (Issues) eaForm.getIssues().get(index);
			
			Measures measures = new Measures();
			measures.setId(eaForm.getMeasureId());
			index = issue.getMeasures().indexOf(measures);
			measures = (Measures) issue.getMeasures().get(index);
			eaForm.setMeasure(measures.getName());
		} else {
			eaForm.setMeasure(null);
		}
		return mapping.findForward("forward");
	}
}