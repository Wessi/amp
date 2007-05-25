/*
 * UpdateMeasures.java
 * Created : 06-Sep-2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Measures;

public class UpdateMeasures extends Action {
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		if (eaForm.getMeasure() != null && 
				eaForm.getMeasure().trim().length() > 0) {
			if (eaForm.getIssueId() != null) {
				Issues issue = new Issues();
				issue.setId(eaForm.getIssueId());
				Measures measure = new Measures();
				if (eaForm.getMeasureId() == null || 
						eaForm.getMeasureId().longValue() < 0) {
					measure.setId(new Long(System.currentTimeMillis()));
				} else {
					measure.setId(eaForm.getMeasureId());
				}
				int index = eaForm.getIssues().indexOf(issue);
				issue = (Issues) eaForm.getIssues().get(index);
				if (issue.getMeasures() == null) {
					issue.setMeasures(new ArrayList());
					measure.setName(eaForm.getMeasure());
					issue.getMeasures().add(measure);
				} else {
					int mIndex = issue.getMeasures().indexOf(measure);
					if (mIndex < 0) {
						measure.setName(eaForm.getMeasure());
						issue.getMeasures().add(measure);
					} else {
						measure = (Measures) issue.getMeasures().get(mIndex);
						measure.setName(eaForm.getMeasure());
						issue.getMeasures().set(mIndex,measure);	
					}
				}
				eaForm.getIssues().set(index,issue);				
				eaForm.setMeasure(null);
				eaForm.setIssueId(null);
			}
		}
		return mapping.findForward("forward");
	}
}