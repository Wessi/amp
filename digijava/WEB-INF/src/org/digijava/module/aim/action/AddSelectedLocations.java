package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Location;

public class AddSelectedLocations extends Action {

	private static Logger logger = Logger.getLogger(AddSelectedLocations.class);

	EditActivityForm eaForm; 
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {
		
		eaForm = (EditActivityForm) form;
		
		if (eaForm.getSelectedLocs() == null) {
			eaForm.setSelectedLocs(new ArrayList());
		}
		
		Long selsearchedLoc[] = eaForm.getSearchedLocs();

		Iterator itr = eaForm.getSearchLocs().iterator();
		int count = 0;
		Location loc = null;
		
		while (itr.hasNext()) {
			loc = (Location) itr.next();
			for (int i = 0;i < selsearchedLoc.length;i ++) {
				if (loc.getLocId().equals(selsearchedLoc[i])) {
					if(!checkDuplicate(loc)) {
							logger.info("adding now...");
							eaForm.getSelectedLocs().add(loc);
							count ++;
							break;
						}
				}
			}
			if (count == selsearchedLoc.length) 
				break;
		}

		//checking duplicates
		Location dup = null;
		
		
		return mapping.findForward("forward");
	}	
	
	public boolean checkDuplicate(Location loc)
	{
		Iterator itr=eaForm.getSelectedLocs().iterator();
		Location location;
		boolean flag=false;
		while(itr.hasNext()){
			location = (Location) itr.next();
			if(location.equals(loc)){
				flag = true;
				logger.info("duplicate found....");
				break;
			}
			else
				flag = false;
		}
		return flag;
	}
}