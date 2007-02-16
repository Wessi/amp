package org.digijava.module.aim.action ;

import org.apache.log4j.Logger ;
import org.apache.struts.action.Action ;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm ;
import org.apache.struts.action.ActionMapping ;
import org.apache.struts.action.ActionForward ;

import org.digijava.module.aim.form.StatusItemForm ;

import org.digijava.module.aim.dbentity.AmpStatus;

import org.digijava.module.aim.util.DbUtil;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import javax.servlet.http.HttpSession;



public class GetStatusItem extends Action
{
	private static Logger logger = Logger.getLogger(GetStatusItem.class) ;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
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

		StatusItemForm formBean = (StatusItemForm) form ;
		Long id = null;
		String action = null;
		if(request.getParameter("ampStatusId")!=null)
		{
			 id = new Long(request.getParameter("ampStatusId"));
			 formBean.setOriginalAmpStatusId(id);
		}
		else
		{
			id = formBean.getOriginalAmpStatusId();
			ActionErrors errors = new ActionErrors();
			errors.add("title", new ActionError(
					"error.aim.addStatus.statusAdded"));
			saveErrors(request, errors);
		}
		if(request.getParameter("action")!=null)
		{
			 action = new String(request.getParameter("action"));
		}
		else
		{
			action = "update";
		}
		AmpStatus statusItem = DbUtil.getAmpStatus(id);

		if (statusItem != null)
		{
			logger.debug("Get Status Action: status name = " + statusItem.getName());
			formBean.setName(statusItem.getName()) ;
			formBean.setStatusCode(statusItem.getStatusCode()) ;
			formBean.setType(statusItem.getType()) ;
			formBean.setDescription(statusItem.getDescription()) ;
			formBean.setAmpStatusId(statusItem.getAmpStatusId()) ;
		}
		else 
		{
			logger.debug("Get Status Action: status not found for id = " + id.toString());
		}
		
		if (action.equals("delete"))
			return mapping.findForward("deleteforward");
		else
			return mapping.findForward("editforward");
		
		
						
	}
}

