/*
 * RemoveComponent.java
 */

package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;

public class RemoveComponent extends Action {
	
	private static Logger logger = Logger.getLogger(ComponentSelected.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		String comp[] = eaForm.getComponents().getSelComp();
		for (int i = 0;i < comp.length; i++) {
		    for (Iterator iterator = eaForm.getComponents().getCompotosave().iterator(); iterator.hasNext();) {
				AmpComponent component = (AmpComponent) iterator.next();
				if (component.getTitle().trim().equalsIgnoreCase(comp[i].trim())){
					iterator.remove();
					for (Iterator iterator2 = eaForm.getComponents().getSelectedComponents().iterator(); iterator2.hasNext();) {
						org.digijava.module.aim.helper.Components<FundingDetail> selectedcompo = (org.digijava.module.aim.helper.Components<FundingDetail>) iterator2.next();
						if (selectedcompo.getTitle().trim().equalsIgnoreCase(comp[i].trim())){
							iterator2.remove();
						}
					}
				}
			}
		}
		eaForm.getComponents().setSelComp(null);
		Double totdisbur=0d;
		for (Iterator iterator = eaForm.getComponents().getSelectedComponents().iterator(); iterator.hasNext();) {
			Components object = (Components) iterator.next();
			if ( object.getDisbursements()!=null){
			for (Iterator iterator2 = object.getDisbursements().iterator(); iterator2
					.hasNext();) {
				FundingDetail disdeatils = (FundingDetail) iterator2.next();
				totdisbur = totdisbur + FormatHelper.parseDouble(disdeatils.getTransactionAmount());
				}
			}
		}
		eaForm.getComponents().setCompTotalDisb(totdisbur);
		return mapping.findForward("forward");
	}
}

