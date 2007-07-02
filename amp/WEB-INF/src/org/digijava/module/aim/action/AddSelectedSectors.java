package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;

public class AddSelectedSectors extends Action {

	private static Logger logger = Logger.getLogger(AddSelectedLocations.class);

	EditActivityForm eaForm;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {

		eaForm = (EditActivityForm) form;

		if (eaForm.getActivitySectors() == null) {
			eaForm.setActivitySectors(new ArrayList());
		}
		/*
		Long selsearchedSector[] = eaForm.getSelSectors();
		logger.info("size off selected searched sectors: "+selsearchedSector.length);
		Iterator itr = eaForm.getSearchedSectors().iterator();
		int count = 0;
		ActivitySector sctr = null;

		while (itr.hasNext()) {
			sctr = (ActivitySector) itr.next();
			for (int i = 0;i < selsearchedSector.length;i ++) {
				logger.info("getsectorid: "+sctr.getSectorId()+" selsearchedSector: "+selsearchedSector[i]);
				if (sctr.getSectorId().equals(selsearchedSector[i])) {
					if(!checkDuplicate(sctr)) {
							logger.info("adding now...");
                            if(eaForm.getActivitySectors()==null){
                                if(selsearchedSector.length==1){
                                    sctr.setSectorPercentage(new Integer(100));
                                }
                            }else if(eaForm.getActivitySectors().size()==0){
                                if(selsearchedSector.length==1){
                                    sctr.setSectorPercentage(new Integer(100));
                                }
                            }else if(eaForm.getActivitySectors().size()==1){
                                Iterator sectItr=eaForm.getActivitySectors().iterator();
                                while(sectItr.hasNext()){
                                    ActivitySector oldSect=(ActivitySector)sectItr.next();
//                                    if(oldSect.getSectorPercentage().equals("100")){
//                                        oldSect.setSectorPercentage(null);
//                                    }
                                    break;
                                }
                            }
							eaForm.getActivitySectors().add(sctr);
							count ++;
							break;
						}
				}
			}
			if (count == selsearchedSector.length)
				break;
		}

		//checking duplicates
	//	Sector dup = null;

*/
		return mapping.findForward("forward");
	}

	public boolean checkDuplicate(ActivitySector dup)
	{
		Iterator itr=eaForm.getActivitySectors().iterator();
		ActivitySector sector;
		boolean flag=false;
		while(itr.hasNext()){
			sector = (ActivitySector) itr.next();
			if(sector.equals(dup)){
				flag = true;
				logger.info("duplicate sector found....");
				break;
			}
			else
				flag = false;
		}
		return flag;
	}
}
