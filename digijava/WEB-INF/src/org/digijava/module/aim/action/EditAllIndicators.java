package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AllMEIndicators;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;

public class EditAllIndicators extends Action {

	private static Logger logger = Logger.getLogger(EditAllIndicators.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception 
	{
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) 
		{
			return mapping.findForward("index");
		} 
		else 
		{
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) 
			{
				return mapping.findForward("index");
			}
		}
		
		ThemeForm indThemeForm = (ThemeForm) form;
		String type = request.getParameter("indicator");
		Long id = new Long(Long.parseLong(request.getParameter("indicatorId")));
		AllPrgIndicators allPrgInd = new AllPrgIndicators();
		AllMEIndicators allMEInd = new AllMEIndicators();
		
		if(type.equalsIgnoreCase("program"))
		{
			allPrgInd = ProgramUtil.getThemeIndicator(id);
			indThemeForm.setIndicatorId(allPrgInd.getIndicatorId());
			indThemeForm.setName(allPrgInd.getName());
			indThemeForm.setCode(allPrgInd.getCode());
			indThemeForm.setType(allPrgInd.getType());
			indThemeForm.setCreationDate(allPrgInd.getCreationDate());
			indThemeForm.setValueType(allPrgInd.getValueType());
			indThemeForm.setCategory(allPrgInd.getCategory());
			indThemeForm.setNpIndicator(allPrgInd.isNpIndicator());
			indThemeForm.setSaveType("program");
		}
		if(type.equalsIgnoreCase("project"))
		{
			allMEInd = MEIndicatorsUtil.getMEIndicator(id);
			indThemeForm.setIndicatorId(allMEInd.getAmpMEIndId());
			indThemeForm.setName(allMEInd.getName());
			indThemeForm.setCode(allMEInd.getCode());
			indThemeForm.setSaveType("project");
		}
		if(type.equalsIgnoreCase("savePrg"))
		{
			allPrgInd.setIndicatorId(id);
			allPrgInd.setName(indThemeForm.getName());
			allPrgInd.setCode(indThemeForm.getCode());
			allPrgInd.setType(indThemeForm.getType());
			allPrgInd.setCreationDate(indThemeForm.getCreationDate());
			allPrgInd.setValueType(indThemeForm.getValueType());
			allPrgInd.setCategory(indThemeForm.getCategory());
			allPrgInd.setNpIndicator(indThemeForm.isNpIndicator());
			ProgramUtil.saveIndicator(allPrgInd);
			return mapping.findForward("saving");
		}
		if(type.equalsIgnoreCase("saveProj"))
		{
			allMEInd.setAmpMEIndId(id);
			allMEInd.setName(indThemeForm.getName());
			allMEInd.setCode(indThemeForm.getCode());
			MEIndicatorsUtil.saveIndicator(allMEInd);
			return mapping.findForward("saving");
		}
		if(type.equalsIgnoreCase("deletePrg"))
		{
			ProgramUtil.deletePrgIndicator(id);
			return mapping.findForward("saving");
		}
		if(type.equalsIgnoreCase("deleteProj"))
		{
			MEIndicatorsUtil.deleteProjIndicator(id);
			return mapping.findForward("saving");
		}
		return mapping.findForward("forward");
	}
}