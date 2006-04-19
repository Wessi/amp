package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.lang.Long;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMEIndicators;

import javax.servlet.http.*;

public class IndicatorManager extends Action 
{
	private static Logger logger = Logger.getLogger(IndicatorManager.class);

	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response) throws java.lang.Exception 
	{
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}
		
		String action = request.getParameter("action");
		Collection indicators = new ArrayList();
		Collection colMeIndValIds = null;
		IndicatorForm indForm = (IndicatorForm) form;

		if (action != null && action.equals("delete")) 
		{
			Long indId = new Long(Long.parseLong(request.getParameter("id")));
			if( indId != null )
			{
				AmpMEIndicators ampMEInd = new AmpMEIndicators();
				ampMEInd.setAmpMEIndId(indId);
				
				colMeIndValIds = MEIndicatorsUtil.getMeIndValIds(indId);
				AmpMEIndicatorValue ampMEIndVal = null;
				
				Iterator itr = colMeIndValIds.iterator();
				while(itr.hasNext())
				{
					ampMEIndVal = (AmpMEIndicatorValue) itr.next();
					DbUtil.delete(ampMEIndVal);
				}
				DbUtil.delete(ampMEInd);
			}
		}
		indicators = MEIndicatorsUtil.getAllIndicators();
		indForm.setIndicators(indicators);
		
		return mapping.findForward("forward");
	}
}