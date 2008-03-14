package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.util.MEIndicatorsUtil;


public class AddIndicatorForStepNine extends Action{
	
private static Logger logger = Logger.getLogger(AddIndicatorForStepNine.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
	
		EditActivityForm eaForm = (EditActivityForm) form;
		HttpSession session = request.getSession();
		
		
		Collection colampMEIndValbox = (Collection)session.getAttribute("ampMEIndValbox");
		String name=null,code=null;
		Collection  tmpActivityIndicator = 	eaForm.getIndicatorsME();
		if (tmpActivityIndicator==null){
			tmpActivityIndicator=new HashSet();
		}
		
	 if(colampMEIndValbox!=null && !colampMEIndValbox.isEmpty()){
		Iterator itr = colampMEIndValbox.iterator();
		while(itr.hasNext()){
			AmpMEIndicatorValue tmpObj = (AmpMEIndicatorValue)itr.next();
			ActivityIndicator actInd = new ActivityIndicator();
			actInd.setActivityId(tmpObj.getActivityId().getAmpActivityId());
			//actInd.setIndicatorValId(new Long(-1));
			name = tmpObj.getIndicator().getName();
			code = tmpObj.getIndicator().getCode();
			actInd.setIndicatorName(name);
			actInd.setIndicatorCode(code);
			actInd.setBaseVal(null);
			actInd.setBaseValDate(null);
			actInd.setBaseValComments("");
			actInd.setTargetVal(null);
			actInd.setTargetValDate(null);
			actInd.setTargetValComments("");
			actInd.setRevisedTargetVal(null);
			actInd.setRevisedTargetValDate(null);
			actInd.setRevisedTargetValComments("");
			actInd.setCurrentVal(null);
			actInd.setCurrentValDate("");
			actInd.setCurrentValComments("");
			actInd.setRisk(new Long(-1));
			AmpIndicator ind=tmpObj.getIndicator();
			if(ind!=null){
			actInd.setIndicatorId(ind.getIndicatorId());
			}
			tmpActivityIndicator.add(actInd);
		}
		eaForm.setIndicatorsME(tmpActivityIndicator);
		}
	
		if(!eaForm.getIndicatorsME().isEmpty())
			eaForm.setRiskCollection(MEIndicatorsUtil.getAllIndicatorRisks());
		eaForm.setIndicatorRisk(null);
		return mapping.findForward("successfull");
	}
}
