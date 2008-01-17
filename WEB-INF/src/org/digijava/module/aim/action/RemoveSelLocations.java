package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Location;

public class RemoveSelLocations extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

				EditActivityForm eaForm = (EditActivityForm) form;

				Long selLocs[] = eaForm.getSelLocs();
				Collection prevSelLocs = eaForm.getSelectedLocs(); 
				Collection locs = new ArrayList<Location>();

				Iterator itr = prevSelLocs.iterator();

				while (itr.hasNext()) {
						  boolean flag = false;
						  Location loc = (Location) itr.next();
						  for (int i = 0;i < selLocs.length;i ++) {
									 if (loc.getLocId().equals(selLocs[i])) {
												flag = true;
												break;
									 }									 
						  }
						  if (!flag) {
									 locs.add(loc);
						  }
						  
				}
				
				eaForm.setSelectedLocs(locs);
				eaForm.setSelLocs(null);
				eaForm.setCols(null);
				eaForm.setNumResults(0);
				eaForm.setStep("2");
				return mapping.findForward("forward");
    }
}

