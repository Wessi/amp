/**
 * SelectSector.java
 * 
 * @author Priyajith
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.SectorUtil;

public class SelectSector extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		SelectSectorForm ssForm = (SelectSectorForm) form;
		
		if (request.getParameter("sectorReset") != null
				&& request.getParameter("sectorReset").equals("false")) {
			ssForm.setSectorReset(false);
		} else {
			ssForm.setSectorReset(true);
			ssForm.reset(mapping, request);
		}
		if(request.getParameter("sectorScheme") != null)
		{
			//Added for AMP-3943, to be able to select sectors from a specified scheme
			ssForm.setSectorScheme(new Long(request.getParameter("sectorScheme")));
			//If the sectorSchemes is specified, set it with the current Sector Scheme
			AmpSectorScheme defClassification=SectorUtil.getAmpSectorScheme(ssForm.getSectorScheme());
			Collection secSchemes = new ArrayList() ;
                        secSchemes.add(defClassification);
			ssForm.setSectorSchemes(secSchemes);
			ssForm.setSectorScheme(defClassification.getAmpSecSchemeId());
			Collection classConfigs = SectorUtil.getAllClassificationConfigs();
			
			
			
			// Comented for AMP-3971 to work
//			for (Iterator<AmpClassificationConfiguration> it=classConfigs.iterator(); it.hasNext(); ) {
//		        AmpClassificationConfiguration classConfig = it.next();
//		        if(classConfig.getClassification().getAmpSecSchemeId().equals(defClassification.getAmpSecSchemeId())){
//		        	ssForm.setConfigId(classConfig.getId());
//		        }
//		        
//		    }
			
			
		}
		if (ssForm.getSectorScheme() == null
				|| ssForm.getSectorScheme().equals(new Long(-1))) {
			// if sector schemes not loaded or reset, load all sector schemes
			// and reset the
			// parent sectors and child sectors.
			AmpClassificationConfiguration config= null;
			if(ssForm.getConfigId()==null){
				//if no ID specified then load primary config
				config = SectorUtil.getPrimaryConfigClassification();
				ssForm.setConfigId(config.getId());
			}else{
				//else load with specified ID 
				config = SectorUtil.getClassificationConfigById(ssForm.getConfigId());
			}
			ssForm.setConfigName(config.getName());
			AmpSectorScheme defClassification=config.getClassification();
			Collection secSchemes = new ArrayList() ;
                        secSchemes.add(defClassification);
			ssForm.setSectorSchemes(secSchemes);
			ssForm.setSectorScheme(defClassification.getAmpSecSchemeId());
			Collection parentSectors = SectorUtil
			.getAllParentSectors(ssForm.getSectorScheme());
			ssForm.setParentSectors(parentSectors);
			ssForm.setChildSectorsLevel1(null);
			ssForm.setChildSectorsLevel2(null);
			ssForm.setSector(new Long(-1));
			ssForm.setSubsectorLevel1(new Long(-1));
			ssForm.setSubsectorLevel2(new Long(-1));			
		} else if (ssForm.getSector() == null
				|| ssForm.getSector().equals(new Long(-1))) {
			// if a sector scheme is selected and the parent sectors of that
			// scheme are not
			// loaded, load all the parent sectors and reset the child sectors.
			Long sectorSchemeId = ssForm.getSectorScheme();
			Collection parentSectors = SectorUtil
					.getAllParentSectors(sectorSchemeId);
			ssForm.setParentSectors(parentSectors);
			ssForm.setChildSectorsLevel1(null);
			ssForm.setChildSectorsLevel2(null);
			ssForm.setSector(new Long(-1));
			ssForm.setSubsectorLevel1(new Long(-1));
			ssForm.setSubsectorLevel2(new Long(-1));			
									
		} else if (ssForm.getSubsectorLevel1() == null
				|| ssForm.getSubsectorLevel1().equals(new Long(-1))) {
			// if the sector scheme and corresponding some parent sector is
			// loaded and the subsectors
			// list is not loaded, load them
			Long parSector = ssForm.getSector();
			Collection childSectors = SectorUtil.getAllChildSectors(parSector);
			Collection parentSectors = SectorUtil
			.getAllParentSectors(ssForm.getSectorScheme());
			ssForm.setParentSectors(parentSectors);
			ssForm.setChildSectorsLevel1(childSectors);
			ssForm.setChildSectorsLevel2(null);
			ssForm.setSubsectorLevel1(new Long(-1));
			ssForm.setSubsectorLevel2(new Long(-1));			
		} else if (ssForm.getSubsectorLevel2() == null
				|| ssForm.getSubsectorLevel2().equals(new Long(-1))) {
			// if the sector scheme and corresponding some parent sector is
			// loaded and the subsectors
			// list is not loaded, load them
			Long parSector = ssForm.getSubsectorLevel1();
			Collection childSectors = SectorUtil.getAllChildSectors(parSector);
			ssForm.setChildSectorsLevel2(childSectors);
			ssForm.setSubsectorLevel2(new Long(-1));			
		}
		
		if (request.getParameter("addButton") != null){
			if (ssForm.getSector().equals(new Long(-1)))
				request.setAttribute("errSector", "true");
			else{
				request.setAttribute("addButton", "true");
				HttpSession session = request.getSession();
				
				
				Long sector = ssForm.getSector();
				Long subsectorLevel1 = ssForm.getSubsectorLevel1();
				Long subsectorLevel2 = ssForm.getSubsectorLevel2();

				Collection newSectors = new ArrayList();


				ActivitySector actSect = new ActivitySector();
				actSect.setSectorId(sector);
                                actSect.setConfigId(ssForm.getConfigId());
				AmpSector sec = SectorUtil.getAmpSector(actSect.getSectorId());
				actSect.setSectorName(sec.getName());
				actSect.setSubsectorLevel1Id(subsectorLevel1);
				actSect.setSubsectorLevel2Id(subsectorLevel2);
                                actSect.setSectorScheme(sec.getAmpSecSchemeId().getSecSchemeName());
				/*
				if (actSect.getSectorName().equalsIgnoreCase("MULTISECTOR/CROSS-CUTTING")) {
					actSect.setSubsectorLevel1Id(null);
					actSect.setSubsectorLevel2Id(null);
				} else {
					actSect.setSubsectorLevel1Id(subsectorLevel1);
					actSect.setSubsectorLevel2Id(subsectorLevel2);
				}*/

				if (subsectorLevel2 != null && (!subsectorLevel2.equals(new Long(-1)))) {
					actSect.setId(subsectorLevel2);
				} else if (subsectorLevel1 != null
						&& (!subsectorLevel1.equals(new Long(-1)))) {
					actSect.setId(subsectorLevel1);
				} else {
					actSect.setId(sector);
				}

				boolean flag = false;

				if (!flag && actSect.getSectorId() != null
						&& (!(actSect.getSectorId().equals(new Long(-1))))) {
					if (actSect.getSubsectorLevel1Id() != null
							&& (!(actSect.getSubsectorLevel1Id().equals(new Long(-1))))) {
						sec = SectorUtil.getAmpSector(actSect.getSubsectorLevel1Id());
						actSect.setSubsectorLevel1Name(sec.getName());
					}
					if (actSect.getSubsectorLevel2Id() != null
							&& (!(actSect.getSubsectorLevel2Id().equals(new Long(-1))))) {
						sec = SectorUtil.getAmpSector(actSect.getSubsectorLevel2Id());
						actSect.setSubsectorLevel2Name(sec.getName());
					}

					newSectors.add(actSect);
				}

				
				session.setAttribute("sectorSelected", actSect);
			}
			
		}
		
		return mapping.findForward("forward");
	}
}
