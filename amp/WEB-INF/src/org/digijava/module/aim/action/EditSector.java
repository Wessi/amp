package org.digijava.module.aim.action ;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;

public class EditSector extends Action {

		  private static Logger logger = Logger.getLogger(GetSectors.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {

		HttpSession session = request.getSession();
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		
		String locale = navigationLanguage.getCode();
		
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}
					 AddSectorForm editSectorForm = (AddSectorForm) form;
					 logger.debug("In edit sector action");
					 logger.debug("In edit sector action");
					 if (request.getParameter("id") != null) {
						 	String id = (String)request.getParameter("id");
							Long secId = new Long(Long.parseLong(request.getParameter("id")));
							String event = request.getParameter("event");
							String flag = request.getParameter("flag");
							if(request.getParameter("flag")==null)
								flag = "false";
							////System.out.println(flag);
							////System.out.println("FLAG========================"+editSectorForm.getJspFlag());
						 if(flag.equalsIgnoreCase("false"))
						 	{

							 
								if(event!=null || !event.equals("") || event.length()<=0)
								{
									if(event.equalsIgnoreCase("update2LevelSector"))
								  {

										
										
										
										AmpSector ampSector = SectorUtil.getAmpSector(secId);
										editSectorForm.setSectorId(secId);
										ampSector.setName(editSectorForm.getSectorName());
										ampSector.setDescription(editSectorForm.getDescription());
										ampSector.setSectorCode(AddSector.DEFAULT_VALUE_SECTOR);										
										ampSector.setSectorCodeOfficial(editSectorForm.getSectorCodeOfficial());
										String secSchemeCode = ampSector.getAmpSecSchemeId().getSecSchemeCode();
										String secSchemename = ampSector.getAmpSecSchemeId().getSecSchemeName();
										Collection sectors = SectorUtil.getSectorLevel1(ampSector.getAmpSecSchemeId().getAmpSecSchemeId().intValue());
										if(checkSectorNameCodeIsNull(editSectorForm)){
											request.setAttribute("event", "view");
											ActionErrors errors = new ActionErrors();
											errors.add("title", new ActionError("error.aim.addScheme.emptyTitleOrCode", TranslatorWorker.translateText("The name or code of the sector is empty. Please enter a title and a code for the sector.",locale,site.getId())));
											if (errors.size() > 0)
											{
												saveErrors(request, errors);
											}
											refreshFirstLevelSectors(editSectorForm, sectors, ampSector);
											if (editSectorForm.getTreeView() != null
													&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
												//request.setAttribute("ampSecSchemeIdFromTree", rootId);
												return mapping.findForward("editedSecondLevelSectorTree");
											} else {
												return mapping.findForward("editedSecondLevelSector");
											}
										}
										if(existSectorForUpdate(editSectorForm,secId, sectors) == 1){
											request.setAttribute("event", "view");
											ActionErrors errors = new ActionErrors();
							        		errors.add("title", new ActionError("error.aim.addScheme.wrongTitle", TranslatorWorker.translateText("The name of the sector already exist in database. Please enter another title",locale,site.getId())));
							        		if (errors.size() > 0)
						        			{
						        				saveErrors(request, errors);
						        			}
							        		refreshFirstLevelSectors(editSectorForm, sectors, ampSector);
							        		if (editSectorForm.getTreeView() != null
													&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
												//request.setAttribute("ampSecSchemeIdFromTree", rootId);
												return mapping.findForward("editedSecondLevelSectorTree");
											} else {
												return mapping.findForward("editedSecondLevelSector");
											}
										}
										
										if(existSectorForUpdate(editSectorForm, secId, sectors) == 2){
											request.setAttribute("event", "view");
											ActionErrors errors = new ActionErrors();
							        		errors.add("title", new ActionError("error.aim.addScheme.wrongCode", TranslatorWorker.translateText("The code of the sector already exist in database. Please enter another code",locale,site.getId())));
							        		if (errors.size() > 0)
						        			{
						        				saveErrors(request, errors);
						        			}
							        		refreshFirstLevelSectors(editSectorForm, sectors, ampSector);
							        		if (editSectorForm.getTreeView() != null
													&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
												//request.setAttribute("ampSecSchemeIdFromTree", rootId);
												return mapping.findForward("editedSecondLevelSectorTree");
											} else {
												return mapping.findForward("editedSecondLevelSector");
											}
										}
										
										logger.debug("Updating.............................................");
										DbUtil.update(ampSector);
										Long schemeId =ampSector.getAmpSecSchemeId().getAmpSecSchemeId();
										Integer schemeID = new Integer(schemeId.intValue());
										editSectorForm.setFormFirstLevelSectors(SectorUtil.getSectorLevel1(schemeID));
										editSectorForm.setSecSchemeCode(secSchemeCode);
										editSectorForm.setSecSchemeName(secSchemename);
										logger.debug(" update sector1 Complete");
										if (editSectorForm.getTreeView() != null
												&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
											//request.setAttribute("ampSecSchemeIdFromTree", rootId);
											return mapping.findForward("editedSecondLevelSectorTree");
										} else {
											return mapping.findForward("editedSecondLevelSector");
										}
								}
								else if(event.equalsIgnoreCase("update3LevelSector"))
								 {
									AmpSector ampSector = SectorUtil.getAmpSector(secId);
									editSectorForm.setSectorId(secId);
									ampSector.setName(editSectorForm.getSectorName());
									ampSector.setDescription(editSectorForm.getDescription());
									ampSector.setSectorCode(AddSector.DEFAULT_VALUE_SUB_SECTOR);
									ampSector.setSectorCodeOfficial(editSectorForm.getSectorCodeOfficial());
									Collection sectors = SectorUtil.getAllChildSectors(ampSector.getParentSectorId().getAmpSectorId());
									if(checkSectorNameCodeIsNull(editSectorForm)){
										request.setAttribute("event", "view");
										ActionErrors errors = new ActionErrors();
										errors.add("title", new ActionError("error.aim.addScheme.emptyTitleOrCode", TranslatorWorker.translateText("The name or code of the sector is empty. Please enter a title and a code for the sector.",locale,site.getId())));
										if (errors.size() > 0)
										{
											saveErrors(request, errors);
											//session.setAttribute("managingSchemes",errors);
										}
										refreshSubSectors(ampSector, sectors, editSectorForm);
										if (editSectorForm.getTreeView() != null
												&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
											//request.setAttribute("ampSecSchemeIdFromTree", rootId);
											return mapping.findForward("editedThirdLevelSectorTree");
										} else {
											return mapping.findForward("editedThirdLevelSector");
										}
									}
									if(existSectorForUpdate(editSectorForm,secId, sectors) == 1){
										request.setAttribute("event", "view");
										ActionErrors errors = new ActionErrors();
						        		errors.add("title", new ActionError("error.aim.addScheme.wrongTitle", TranslatorWorker.translateText("The name of the sector already exist in database. Please enter another title",locale,site.getId())));
						        		if (errors.size() > 0)
					        			{
					        				saveErrors(request, errors);
					        			}
						        		refreshSubSectors(ampSector, sectors, editSectorForm);
						        		if (editSectorForm.getTreeView() != null
												&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
											//request.setAttribute("ampSecSchemeIdFromTree", rootId);
											return mapping.findForward("editedThirdLevelSectorTree");
										} else {
											return mapping.findForward("editedThirdLevelSector");
										}
									}
									
									if(existSectorForUpdate(editSectorForm, secId, sectors) == 2){
										request.setAttribute("event", "view");
										ActionErrors errors = new ActionErrors();
						        		errors.add("title", new ActionError("error.aim.addScheme.wrongCode", TranslatorWorker.translateText("The code of the sector already exist in database. Please enter another code",locale,site.getId())));
						        		if (errors.size() > 0)
					        			{
					        				saveErrors(request, errors);
					        			}
						        		refreshSubSectors(ampSector, sectors,editSectorForm);
						        		if (editSectorForm.getTreeView() != null
												&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
											//request.setAttribute("ampSecSchemeIdFromTree", rootId);
											return mapping.findForward("editedThirdLevelSectorTree");
										} else {
											return mapping.findForward("editedThirdLevelSector");
										}
									}
									
									logger.debug("Updating.............................................");
									DbUtil.update(ampSector);
									ampSector = SectorUtil.getAmpSector(secId);
									Long sectorId = ampSector.getParentSectorId().getAmpSectorId();
									Integer schemeID = new Integer(sectorId.intValue());
									editSectorForm.setSubSectors(SectorUtil.getAllChildSectors(sectorId));
									editSectorForm.setSectorCode(ampSector.getParentSectorId().getSectorCode());
									editSectorForm.setSectorCodeOfficial(ampSector.getParentSectorId().getSectorCodeOfficial());
									editSectorForm.setSectorName(ampSector.getParentSectorId().getName());
									editSectorForm.setSecSchemeId(ampSector.getAmpSecSchemeId().getAmpSecSchemeId());
									editSectorForm.setSectorId(ampSector.getParentSectorId().getAmpSectorId());
									editSectorForm.setDescription(ampSector.getParentSectorId().getDescription());
									logger.debug(" update sector2 Complete");
									if (editSectorForm.getTreeView() != null
											&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
										//request.setAttribute("ampSecSchemeIdFromTree", rootId);
										return mapping.findForward("editedThirdLevelSectorTree");
									} else {
										return mapping.findForward("editedThirdLevelSector");
									}
								}
					 
							  }
		  		  
						 	}
						 else if(flag.equalsIgnoreCase("true"))
						 	{
							 if(event.equalsIgnoreCase("update3LevelSector"))
							 	{
									AmpSector ampSector = SectorUtil.getAmpSector(secId);
									editSectorForm.setSectorId(secId);
									ampSector.setName(editSectorForm.getSectorName());
									ampSector.setDescription(editSectorForm.getDescription());
									ampSector.setSectorCode(editSectorForm.getSectorCode());
									ampSector.setSectorCodeOfficial(editSectorForm.getSectorCodeOfficial());
									
									Collection sectors = SectorUtil.getAllChildSectors(ampSector.getParentSectorId().getAmpSectorId());
									if(checkSectorNameCodeIsNull(editSectorForm)){
										request.setAttribute("event", "view");
										ActionErrors errors = new ActionErrors();
										errors.add("title", new ActionError("error.aim.addScheme.emptyTitleOrCode", TranslatorWorker.translateText("The name or code of the sector is empty. Please enter a title and a code for the sector.",locale,site.getId())));
										if (errors.size() > 0)
										{
											saveErrors(request, errors);
											//session.setAttribute("managingSchemes",errors);
										}
										refreshSubSectors1(ampSector, sectors,editSectorForm);
										if (editSectorForm.getTreeView() != null
												&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
											//request.setAttribute("ampSecSchemeIdFromTree", rootId);
											return mapping.findForward("editedThirdLevelSectorPlusOneTree");
										} else {
											return mapping.findForward("editedThirdLevelSectorPlusOne");
										}
									}
									if(existSectorForUpdate(editSectorForm,secId, sectors) == 1){
										request.setAttribute("event", "view");
										ActionErrors errors = new ActionErrors();
						        		errors.add("title", new ActionError("error.aim.addScheme.wrongTitle", TranslatorWorker.translateText("The name of the sector already exist in database. Please enter another title",locale,site.getId())));
						        		if (errors.size() > 0)
					        			{
					        				saveErrors(request, errors);
					        			}
						        		refreshSubSectors1(ampSector, sectors,editSectorForm);
						        		if (editSectorForm.getTreeView() != null
												&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
											//request.setAttribute("ampSecSchemeIdFromTree", rootId);
											return mapping.findForward("editedThirdLevelSectorPlusOneTree");
										} else {
											return mapping.findForward("editedThirdLevelSectorPlusOne");
										}
									}
									
									if(existSectorForUpdate(editSectorForm, secId, sectors) == 2){
										request.setAttribute("event", "view");
										ActionErrors errors = new ActionErrors();
						        		errors.add("title", new ActionError("error.aim.addScheme.wrongCode", TranslatorWorker.translateText("The code of the sector already exist in database. Please enter another code",locale,site.getId())));
						        		if (errors.size() > 0)
					        			{
					        				saveErrors(request, errors);
					        			}
						        		refreshSubSectors1(ampSector, sectors,editSectorForm);
						        		if (editSectorForm.getTreeView() != null
												&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
											//request.setAttribute("ampSecSchemeIdFromTree", rootId);
											return mapping.findForward("editedThirdLevelSectorPlusOneTree");
										} else {
											return mapping.findForward("editedThirdLevelSectorPlusOne");
										}
									}
									
									logger.debug("Updating.............................................");
									DbUtil.update(ampSector);
									ampSector = SectorUtil.getAmpSector(secId);
									Long sectorId = ampSector.getParentSectorId().getAmpSectorId();
									Integer schemeID = new Integer(sectorId.intValue());
									editSectorForm.setSubSectors(SectorUtil.getAllChildSectors(sectorId));
									editSectorForm.setSectorCode(ampSector.getParentSectorId().getSectorCode());
									editSectorForm.setSectorCodeOfficial(ampSector.getParentSectorId().getSectorCodeOfficial());
									editSectorForm.setSectorName(ampSector.getParentSectorId().getName());
									editSectorForm.setSecSchemeId(ampSector.getAmpSecSchemeId().getAmpSecSchemeId());
									editSectorForm.setSectorId(ampSector.getParentSectorId().getAmpSectorId());
									editSectorForm.setDescription(ampSector.getParentSectorId().getDescription());
									logger.debug(" update sector3 Complete");
									editSectorForm.setJspFlag(false);
									if (editSectorForm.getTreeView() != null
											&& editSectorForm.getTreeView().equalsIgnoreCase("true")) {
										//request.setAttribute("ampSecSchemeIdFromTree", rootId);
										return mapping.findForward("editedThirdLevelSectorPlusOneTree");
									} else {
										return mapping.findForward("editedThirdLevelSectorPlusOne");
									}
						 }
						 }
					}
					 return null;
		  }
		  
		  
		  private void refreshSubSectors1(AmpSector ampSector,Collection sectors, AddSectorForm editSectorForm) {
			// TODO Auto-generated method stub
			  editSectorForm.setSubSectors(sectors);
				editSectorForm.setSectorCode(ampSector.getParentSectorId().getSectorCode());
				editSectorForm.setSectorCodeOfficial(ampSector.getParentSectorId().getSectorCodeOfficial());
				editSectorForm.setSectorName(ampSector.getParentSectorId().getName());
				editSectorForm.setSecSchemeId(ampSector.getAmpSecSchemeId().getAmpSecSchemeId());
				editSectorForm.setSectorId(ampSector.getParentSectorId().getAmpSectorId());
				editSectorForm.setDescription(ampSector.getParentSectorId().getDescription());
				logger.debug(" update sector3 Complete");
				editSectorForm.setJspFlag(false);
		}


		private void refreshSubSectors(AmpSector ampSector, Collection sectors, AddSectorForm editSectorForm) {
			// TODO Auto-generated method stub
			  editSectorForm.setSubSectors(sectors);
				editSectorForm.setSectorCode(ampSector.getParentSectorId().getSectorCode());
				editSectorForm.setSectorCodeOfficial(ampSector.getParentSectorId().getSectorCodeOfficial());
				editSectorForm.setSectorName(ampSector.getParentSectorId().getName());
				editSectorForm.setSecSchemeId(ampSector.getAmpSecSchemeId().getAmpSecSchemeId());
				editSectorForm.setSectorId(ampSector.getParentSectorId().getAmpSectorId());
				editSectorForm.setDescription(ampSector.getParentSectorId().getDescription());
		}


		private void refreshFirstLevelSectors(AddSectorForm editSectorForm,	Collection sectors, AmpSector ampSector) {
			// TODO Auto-generated method stub
			  String secSchemeCode = ampSector.getAmpSecSchemeId().getSecSchemeCode();
			  String secSchemename = ampSector.getAmpSecSchemeId().getSecSchemeName();
			  editSectorForm.setFormFirstLevelSectors(sectors);
			  editSectorForm.setSecSchemeCode(secSchemeCode);
			  editSectorForm.setSecSchemeName(secSchemename);
		}


		private boolean checkSectorNameCodeIsNull(AddSectorForm sectorsForm){
				if(sectorsForm.getSectorCodeOfficial() == null || sectorsForm.getSectorName() == null ||
						"".equals(sectorsForm.getSectorCodeOfficial()) || "".equals(sectorsForm.getSectorName()) )
					return true;
				return false;
			}
			
			private int existSector (AddSectorForm sectorsForm, Collection sectors){
				for (Iterator it = sectors.iterator(); it.hasNext();) {
					AmpSector sector = (AmpSector) it.next();
					if(sector.getName() != null && sectorsForm.getSectorName().equals(sector.getName())) return 1;
					if(sector.getSectorCode() != null && sectorsForm.getSectorCodeOfficial().equals(sector.getSectorCode())) return 2;
				}
				return 0;
			}
			
			private int existSectorForUpdate (AddSectorForm sectorsForm, Long Id, Collection sectors){
				for (Iterator it = sectors.iterator(); it.hasNext();) {
					AmpSector sector = (AmpSector) it.next();
					if(!Id.equals(sector.getAmpSectorId())){
						if( sector.getName() != null && sectorsForm.getSectorName().equals(sector.getName()) ) 
							return 1;
						if( sector.getSectorCode() != null && sectorsForm.getSectorCodeOfficial().equals(sector.getSectorCode()) ) 
							return 2;
					}
				}
				return 0;
			}
		  
}

