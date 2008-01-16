package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.helper.Indicator;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class AddNewIndicatorTL extends Action {
	private static Logger logger = Logger.getLogger(AddNewIndicatorTL.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IndicatorForm indForm = (IndicatorForm) form;
		
		
		 AmpPrgIndicator newInd = new AmpPrgIndicator();
		Long selInds[] =  indForm.getIndid();
		
		if(selInds!=null){
			for (int j = 0; j < selInds.length; j++) {
				AllPrgIndicators ind = IndicatorUtil.getAmpIndicator(selInds[j]);
				
				
				newInd.setIndicatorId(selInds[j]);
				newInd.setName(ind.getName());
				newInd.setCode(ind.getCode());{
				if (newInd.getDescription() != null &&
						newInd.getDescription().length() > 0){
					newInd.setDescription(ind.getDescription());
				}else{
					newInd.setDescription(" ");
				}
				  newInd.setSelectedActivityId(indForm.getActivityId());
				 
				//if (indForm.getAscendingInd() == 'A')
					//ampMEIndnew.setAscendingInd(true);
				//else
					//ampMEIndnew.setAscendingInd(false);
				
				newInd.setDefaultInd(false);

				//MEIndicatorsUtil.saveMEIndicator(ampMEIndnew, indForm.getActivityId(), false);
				IndicatorUtil.saveIndicators(newInd);
			}
				
				
			}
		}
		
		
		
		
//		if (indForm.getIndicatorName().trim() != null 
//				&& indForm.getIndicatorCode().trim() != null) {
//			
//			ampMEIndnew.setName(indForm.getIndicatorName());
//			ampMEIndnew.setCode(indForm.getIndicatorCode());
//			if (ampMEIndnew.getDescription() != null &&
//					ampMEIndnew.getDescription().length() > 0)
//				ampMEIndnew.setDescription(indForm.getIndicatorDesc());
//			else
//				ampMEIndnew.setDescription(" ");
//			
//			if (indForm.getAscendingInd() == 'A')
//				ampMEIndnew.setAscendingInd(true);
//			else
//				ampMEIndnew.setAscendingInd(false);
//			
//			ampMEIndnew.setDefaultInd(false);
//
//			MEIndicatorsUtil.saveMEIndicator(ampMEIndnew, indForm
//					.getActivityId(), false);
//		}
		return mapping.findForward("forward");
	}
}