package org.digijava.module.aim.action ;

import org.apache.log4j.Logger ;
import org.apache.struts.action.Action ;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm ;
import org.apache.struts.action.ActionMapping ;
import org.apache.struts.action.ActionForward ;
import java.util.Collection;
import java.util.Iterator;

import org.digijava.module.aim.form.StatusItemForm ;
import org.digijava.module.aim.dbentity.AmpStatus;

import org.digijava.module.aim.util.DbUtil;



import javax.servlet.http.* ;



public class AddStatus extends Action
{
	private static Logger logger = Logger.getLogger(UpdateStatus.class) ;
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
			boolean Flag = true;
			StatusItemForm formBean = (StatusItemForm) form ; 	
			AmpStatus statusItem = new AmpStatus() ;
			String code = formBean.getStatusCode();
			if (code == null || request.getParameter("saveStatus") == null)
				return mapping.findForward("forward");
			Collection col = DbUtil.getStatusCodes();
			Iterator itr = col.iterator();
			while(itr.hasNext())
			{
				AmpStatus amp=  (AmpStatus) itr.next();
				String a = amp.getStatusCode();
				if(a.compareTo(code) == 0)
				{
					Flag = false;
				}
			}
			
		if(Flag){
			if (formBean.getStatusCode() != null)
			{
				statusItem.setName(formBean.getName()) ;
				statusItem.setStatusCode(formBean.getStatusCode()) ;
/*				if (formBean.getDescription() == null)
					statusItem.setDescription("blank") ;
				else
					statusItem.setDescription(formBean.getDescription()) ;
					*/
				if (formBean.getDescription() == null 
					|| formBean.getDescription().trim().equals("")) 
			  	{ statusItem.setDescription(new String(" "));
			  	}
				else {
					statusItem.setDescription(formBean.getDescription());
				}
				statusItem.setType(formBean.getType()) ;
				statusItem.setLanguage(null) ;
				statusItem.setVersion(null) ;
				
				DbUtil.add(statusItem);
				return mapping.findForward("addforward") ;
			}
		else 			
			return mapping.findForward("forward") ;
		}
		else{ 
			ActionErrors errors = new ActionErrors();
			errors.add("title", new ActionError(
					"error:aim:addStatus:statusAdded"));
			saveErrors(request, errors);
			return mapping.findForward("forward");
		}
	 }
		
}
