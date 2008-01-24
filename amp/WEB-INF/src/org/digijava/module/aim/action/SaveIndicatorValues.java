package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.CategoryManagerUtil;

public class SaveIndicatorValues extends Action
{
	private static Logger logger = Logger.getLogger(SaveIndicatorValues.class);

	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception
	{

		EditActivityForm eaForm = (EditActivityForm) form;

		if (eaForm.getIndicatorsME() != null &&
				eaForm.getIndicatorsME().size() > 0) {
			ActivityIndicator actInd = new ActivityIndicator();
			actInd.setIndicatorId(eaForm.getIndicatorId());
			Iterator itr = eaForm.getIndicatorsME().iterator();
			while (itr.hasNext()) {
				ActivityIndicator temp = (ActivityIndicator) itr.next();
				if (temp.equals(actInd)) {
					if(eaForm.getWorkingTeamLeadFlag().equalsIgnoreCase("no"))
					{
						if (eaForm.getCurrentValDate() != null) {
							if( eaForm.getCurrentVal() != null) {
								temp.setCurrentVal(Float.parseFloat(eaForm.getCurrentVal()));
							}
							temp.setCurrentValDate(eaForm.getCurrValDate());
							temp.setCurrentValComments(eaForm.getCurrentValComments());
						}
						temp.setRisk(eaForm.getIndicatorRisk());
						break;
					}
					if(eaForm.getWorkingTeamLeadFlag().equalsIgnoreCase("yes"))
					{
						temp.setBaseVal(eaForm.getBaseVal());
						temp.setBaseValDate(eaForm.getBaseValDate());
						temp.setBaseValComments(eaForm.getBaseValComments());
						temp.setTargetVal(eaForm.getTargetVal());
						temp.setRevisedTargetVal(eaForm.getTargetVal());
						temp.setTargetValDate(eaForm.getTargetValDate());
						temp.setTargetValComments(eaForm.getTargetValComments());
						if (eaForm.getRevTargetValDate() != null) {
							temp.setTargetVal(eaForm.getRevTargetVal());
                            temp.setTargetValDate(eaForm.getRevTargetValDate());
							temp.setRevisedTargetVal(eaForm.getRevTargetVal());
							temp.setRevisedTargetValDate(eaForm.getRevTargetValDate());
							temp.setRevisedTargetValComments(eaForm.getRevTargetValComments());
						} else {
                                eaForm.setRevTargetVal(eaForm.getTargetVal());
							temp.setRevisedTargetVal(eaForm.getTargetVal());
							temp.setRevisedTargetValDate(eaForm.getTargetValDate());
							temp.setRevisedTargetValComments(eaForm.getTargetValComments());
						}

						if (eaForm.getCurrentValDate() != null) {
							logger.info("Setting currValComments :" + eaForm.getCurrentValComments());
							if( eaForm.getCurrentVal() != null) {
								temp.setCurrentVal(Float.parseFloat(eaForm.getCurrentVal()));
							}
							//temp.setCurrentVal(eaForm.getCurrentVal());

							temp.setCurrentValDate(eaForm.getCurrValDate());
							temp.setActualValDate(eaForm.getCurrValDate());
							temp.setCurrentValComments(eaForm.getCurrentValComments());
						}
						if(eaForm.getCurrentVal() != null){
						temp.setActualVal(Float.parseFloat(eaForm.getCurrentVal()));
						
						}else{
							temp.setActualVal(null);
						}
						temp.setRisk(eaForm.getIndicatorRisk());
						////System.out.println(eaForm.getLogframeCategory());
						//temp.setLogframeValueId(eaForm.getLogframeCategory());
						AmpCategoryValue acv=CategoryManagerUtil.getAmpCategoryValueFromDb(eaForm.getLogframeCategory());
						temp.setIndicatorsCategory(acv);
						break;
					}
				}
			}
		}
		return mapping.findForward("forward");
	}
}
