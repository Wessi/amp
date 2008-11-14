package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class AddMTEFProjection extends Action{

	private static Logger logger = Logger.getLogger(AddMTEFProjection.class);
	
	private List<MTEFProjection> mtefProjections=null;
	
	private String event;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		EditActivityForm formBean = (EditActivityForm) form;
		formBean.setReset(false);
		event = formBean.getFunding().getEvent();
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		
		formBean.getFunding().setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false));
		long index = formBean.getTransIndexId();
		String subEvent = event.substring(0,3);
		
		
		MTEFProjection mp = null;
		if (subEvent.equalsIgnoreCase("del") || subEvent.equalsIgnoreCase("add")) {
			if (formBean.getFunding().getFundingMTEFProjections() == null || 
					formBean.getFunding().getFundingMTEFProjections().size() == 0) {
				boolean afterFiscalYearStart	= AddFunding.isAfterFiscalYearStart( null );
				mtefProjections = new ArrayList<MTEFProjection>();
				mp = AddFunding.getMTEFProjection(request.getSession(), 0, afterFiscalYearStart, null);
				mtefProjections.add(mp);		
			} else {
				MTEFProjection firstMtef		= formBean.getFunding().getFundingMTEFProjections().get(0);
				boolean afterFiscalYearStart	= AddFunding.isAfterFiscalYearStart( firstMtef.getProjectionDate() );
				String [] dateSplit				= firstMtef.getProjectionDate().split("/");
				Integer year;
				try {
					year	= Integer.parseInt( dateSplit[2] );
				}
				catch (Exception E) {
					year	= null;
					logger.error(E.getMessage());
				}
				
				mtefProjections 			= formBean.getFunding().getFundingMTEFProjections();
				if (subEvent.equals("del")) {
					Iterator<MTEFProjection> iter	= mtefProjections.iterator();
					int offset;
					if (afterFiscalYearStart)
							offset	= 1;
					else
							offset	= 0;
					while (iter.hasNext()) {
						MTEFProjection proj	= iter.next();
						if (proj.getIndexId() == index) {
							iter.remove();
						}
						else {
							proj.setProjectionDate( AddFunding.getFYDate(offset++, year) );
						}
					}
					MTEFProjection temp = new MTEFProjection();
					temp.setIndexId(index);
					mtefProjections.remove(temp);					
				} else {
					mp = AddFunding.getMTEFProjection(request.getSession(), mtefProjections.size(), afterFiscalYearStart, year );
					mtefProjections.add(mp);							
				}
			}
			formBean.getFunding().setFundingMTEFProjections(mtefProjections);			
		}
		formBean.getFunding().setEvent(null);
		formBean.getFunding().setDupFunding(true);
		formBean.getFunding().setFirstSubmit(false);
		return mapping.findForward("forward");
	}
}
