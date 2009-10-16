

package org.digijava.module.widget.action;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.widget.form.WidgetTeaserForm;

public class GetWidgetMap extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		WidgetTeaserForm wForm = (WidgetTeaserForm) form;
		
		response.setContentType("text/xml");
		
		HttpSession session = request.getSession();

		String map= (String) session.getAttribute("chartMap"+wForm.getType());
		
	
				try {
					response.getWriter().print(map);
				} catch (IOException e) {
					// TODO handle this exception
					e.printStackTrace();
				}
		
		
		return null;
	}

}

