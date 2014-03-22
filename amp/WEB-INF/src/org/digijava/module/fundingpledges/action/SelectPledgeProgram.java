package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.Location;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class SelectPledgeProgram extends Action {

	private static Logger logger = Logger.getLogger(SelectPledgeProgram.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception 
	{
		// entry point for sectors and programs AJAX
		PledgeForm pledgeForm = (PledgeForm) form;
		
		String extraAction = request.getParameter("extraAction");
		if (extraAction != null)
		{
			// {id: 'program_id_select', action: 'rootThemeChanged', attr: 'rootThemeId'},
			if (extraAction.equals("pledge_program_rootThemeChanged"))
			{
				pledgeForm.setSelectedRootProgram(Long.parseLong(request.getParameter("rootThemeId")));
				return null;
			}
						
			if (extraAction.equals("pledge_program_submit"))
			{
				String[] ids = request.getParameter("selected_program").split(",");
				for(String id: ids){
					Long lid = Long.parseLong(id);
					pledgeForm.addSelectedProgram(lid);
				}
				ARUtil.writeResponse(response, "ok");
				return null;
			}
			
			if (extraAction.equals("pledge_program_delete")){
				pledgeForm.deleteUniquelyIdentifiable(pledgeForm.getSelectedProgs(), Long.parseLong(request.getParameter("id")));
				return null;
			}
		
			if (extraAction.equals("pledge_sector_rootSectorChanged"))
			{
				pledgeForm.setSelectedRootSector(Long.parseLong(request.getParameter("rootSectorId")));
				return null;
			}
			
			if (extraAction.equals("pledge_sector_submit"))
			{
				String[] ids = request.getParameter("selected_sector").split(",");
				for(String id: ids) {
					Long sid = Long.parseLong(id);
					pledgeForm.addSelectedSector(sid);
				}
				ARUtil.writeResponse(response, "ok");
				return null;
			}
			
			if (extraAction.equals("pledge_sector_delete")){
				pledgeForm.deleteUniquelyIdentifiable(pledgeForm.getSelectedSectors(), Long.parseLong(request.getParameter("id")));
				return null;
			}

			if (extraAction.equals("pledge_funding_submit")){
				pledgeForm.addNewPledgeFundingEntry();
				ARUtil.writeResponse(response, "ok");
				return null;
			}
			if (extraAction.equals("pledge_funding_delete")){
				pledgeForm.deleteUniquelyIdentifiable(pledgeForm.getSelectedFunding(), Long.parseLong(request.getParameter("id")));
				return null;
			}
			
			// {id: 'program_item_select', action: 'themeSelected', attr: 'themeId'} - ignored
			// {id: 'sector_item_select', action: 'sectorSelected', attr: 'sectorId'} - ignored
		}
	    return mapping.findForward("forward");
	}
}
