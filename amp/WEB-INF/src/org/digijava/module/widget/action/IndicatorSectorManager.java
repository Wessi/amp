package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.gis.helper.IndicatorSectorWithSubgroup;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.widget.form.IndicatorSectorRegionForm;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;

/**
 *
 * @author medea
 */
public class IndicatorSectorManager extends DispatchAction {
    
    public final static int RECORDS_PER_PAGE=15;
    public final static long ALL_REGIONS_SELECTED=-2l;
    public final static long NATIONAL_SELECTED=-3l;

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return cancel(mapping, form, request, response);
    }

    /*
     * forwards to the page where user can view all sectorIndicators 
     */
    public ActionForward viewAll(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		
		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}
    	
    	IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
    	if(request.getParameter("reset")!=null && request.getParameter("reset").equals("true")){
    		indSecForm.setResultsPerPage(10);
    		indSecForm.setKeyWord(null);
    	}
    	
    	//get all sectors
    	Collection allSectors = SectorUtil.getAllParentSectors();
    	Collections.sort(new ArrayList(allSectors), new SectorUtil.HelperSectorNameComparator());
    	indSecForm.setSectors(allSectors);
    	//get all regions
    	//List ampRegions=LocationUtil.getAllLocations("");
    	List ampRegions= DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry();
    	Collections.sort(ampRegions, new LocationUtil.HelperAmpCategoryValueLocationsNameComparator());
    	indSecForm.setRegions(ampRegions);
    	
    	List<IndicatorSector> allIndSects=null;
    	List<IndicatorSector> alphaIndSects=null;
    	String alpha=indSecForm.getAlpha();
    	if(alpha==null || alpha.equals("viewAll")){
    		Long sectorId=indSecForm.getSectorId()==null || indSecForm.getSectorId().equals(new Long(-1)) ? null : indSecForm.getSectorId();
    		Long regionId=indSecForm.getRegionId()==null || indSecForm.getRegionId().equals(new Long(-1)) ? null : indSecForm.getRegionId() ;
    		allIndSects=DbUtil.searchIndicatorSectors(indSecForm.getSortBy(),indSecForm.getKeyWord(),sectorId,regionId);
    		indSecForm.setAllIndSectList(allIndSects);
    	}else{
    		allIndSects=indSecForm.getAllIndSectList();
    		List<IndicatorSector> myList=new ArrayList<IndicatorSector>();
    		if(!indSecForm.getSectorId().equals(new Long(-1))){
    			Long sectorId=indSecForm.getSectorId();
    			for (IndicatorSector indicatorSector : allIndSects) {
					if(indicatorSector.getSector().getAmpSectorId().longValue()==sectorId.longValue()){
						myList.add(indicatorSector);
					}
				}
    			allIndSects=myList;
    		}
    		
    		if(!indSecForm.getRegionId().equals(new Long(-1))){
    			myList=new ArrayList<IndicatorSector>();
    			Long regionId=indSecForm.getRegionId();
    			for (IndicatorSector indicatorSector : allIndSects) {
    				if(indicatorSector.getLocation().getLocation().getId().equals(regionId)){
    					myList.add(indicatorSector);
    				}
    			}
    			allIndSects=myList;
    		}
    		String sortBy=indSecForm.getSortBy();
    		 if(sortBy==null || sortBy.equals("nameAscending")){
 				Collections.sort(allIndSects, new DbUtil.HelperIndicatorNameAscComparator());
 			}else if(sortBy.equals("nameDescending")){
 				Collections.sort(allIndSects, new DbUtil.HelperIndicatorNameDescComparator());
 			}else if(sortBy.equals("sectNameAscending")){
 				Collections.sort(allIndSects, new DbUtil.HelperSectorNameAscComparator());
 			}else if(sortBy.equals("sectNameDescending")){
 				Collections.sort(allIndSects, new DbUtil.HelperSectorNameDescComparator());
 			}else if(sortBy.equals("regionNameAscending")){
 				Collections.sort(allIndSects, new DbUtil.HelperRegionNameAscComparator());
 			}else if(sortBy.equals("regionNameDescending")){
 				Collections.sort(allIndSects, new DbUtil.HelperRegionNameDescComparator());
 			}
    		
    	}
    	
    	
    	if(allIndSects!=null && allIndSects.size()>0){
    		 if(alpha == null || alpha.trim().length() == 0){
    			 if (indSecForm.getCurrentAlpha() != null) {
    				 indSecForm.setCurrentAlpha(null);
    			 } 
             }else {
            	 indSecForm.setCurrentAlpha(alpha);
             }
    		 
    		 String[] alphaArray = new String[26];
             int i = 0;
             for (char c = 'A'; c <= 'Z'; c++) {
            	 Iterator<IndicatorSector> itr = allIndSects.iterator();
            	 while (itr.hasNext()) {
            		 IndicatorSector indSec = itr.next();
            		 if (indSec.getIndicator().getName().toUpperCase().indexOf(c) == 0) {
            			 alphaArray[i++] = String.valueOf(c);
            			 break;
            		 }
            	 }
             }
             indSecForm.setAlphaPages(alphaArray);
    	}else{
    		indSecForm.setAlphaPages(null);
    	}
    	
    	//any letter was selected
    	if(alpha!=null && !alpha.equals("viewAll")){
    		alphaIndSects=new ArrayList();
    		Iterator<IndicatorSector> iter=allIndSects.iterator();
    		while(iter.hasNext()){
    			IndicatorSector indSec=iter.next();
    			if(indSec.getIndicator().getName().toUpperCase().startsWith(alpha)){
    				alphaIndSects.add(indSec);
    			}
    		}    		
    	}
    	
    	//pagnation
    	int stIndex = 0;
        int endIndex = indSecForm.getResultsPerPage();
       
        List<IndicatorSector> myIndSects=alphaIndSects==null?allIndSects:alphaIndSects;
        //If ALL was selected in pagination dropdown
        if (endIndex < 0) {        	
        	endIndex = myIndSects.size();
        }
        
        Collection pages = null;
        int pagesNum=0;
        if(indSecForm.getResultsPerPage().intValue()!=-1){        	
        	if(myIndSects.size() % indSecForm.getResultsPerPage()==0){
            	pagesNum=myIndSects.size()/indSecForm.getResultsPerPage();
            }else{
            	pagesNum=myIndSects.size()/indSecForm.getResultsPerPage()+1;
            }
        }        
        
    	if (pagesNum > 1) {
	        pages = new ArrayList();
	        for (int i = 0; i < pagesNum; i++) {
	          Integer pageNum = new Integer(i + 1);
	          pages.add(pageNum);
	        }
    	}
	
    	int page = 0; //selected page
		if (indSecForm.getSelectedPage() == 0) {
			page = 1;
		} else {
			page = indSecForm.getSelectedPage();
		}
		
		if(page>1){
			stIndex=(page - 1) * indSecForm.getResultsPerPage();			
		}
		if(indSecForm.getResultsPerPage().intValue()!=-1){
			endIndex=(stIndex+indSecForm.getResultsPerPage())<=myIndSects.size()? stIndex+indSecForm.getResultsPerPage() : myIndSects.size();
		}
		
		if(stIndex>endIndex){
			stIndex=0;
		}
		List<IndicatorSector> tempCol = new ArrayList();
	      for (int i = stIndex; i < endIndex; i++) {
	        tempCol.add(myIndSects.get(i));
	      }

	      
    	//build indicatorsectors with subgroups
	      List<IndicatorSectorWithSubgroup> indSectsWithSubGroups=new ArrayList<IndicatorSectorWithSubgroup>();
	      for (IndicatorSector indicatorSector : tempCol) {
			List subgroups=DbUtil.getAvailSubgroupsForSectorIndicator(indicatorSector.getSector().getAmpSectorId(),indicatorSector.getIndicator().getIndicatorId());
			IndicatorSectorWithSubgroup indSecWithSubGrp=new IndicatorSectorWithSubgroup(indicatorSector,subgroups);
			if(indicatorSector.getIndicator().getName().length()>40){
				indSecWithSubGrp.setShortIndName(indicatorSector.getIndicator().getName().substring(0, 40));
			}else{
				indSecWithSubGrp.setShortIndName(indicatorSector.getIndicator().getName());
			}
			if(indicatorSector.getSector().getName().length()>40){
				indSecWithSubGrp.setShortSectName(indicatorSector.getSector().getName().substring(0,40));
			}else{
				indSecWithSubGrp.setShortSectName(indicatorSector.getSector().getName());
			}
			if(indicatorSector.getLocation().getLocation().getName().length()>40){
				indSecWithSubGrp.setShortRegionName(indicatorSector.getLocation().getLocation().getName().substring(0,40));
			}else{
				indSecWithSubGrp.setShortRegionName(indicatorSector.getLocation().getLocation().getName());
			}
			indSectsWithSubGroups.add(indSecWithSubGrp);			
		}
	    indSecForm.setIndSectsWithSubGroups(indSectsWithSubGroups);
    	indSecForm.setIndSectList(tempCol);		
    	indSecForm.setSelectedPage(page);    	
    	indSecForm.setPages(pages);
    	if(pages!=null){
    		indSecForm.setPagesSize(pages.size());
    	}    	
    	if(indSecForm.getSortBy()==null){
    		indSecForm.setSortBy("nameAscending");
    	}
        return mapping.findForward("forward");
    }
    
    /*
     * saves modified SectorIndicator;
     */
    public ActionForward save(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {

        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        ActionErrors errors = new ActionErrors();
        IndicatorSector indSec;
        List<AmpCategoryValueLocations> regions = new ArrayList<AmpCategoryValueLocations>();
        AmpSector selectedSector = indSecForm.getSector();
        Long indId = indSecForm.getSelIndicator();
        AmpIndicator selectedIndicator = IndicatorUtil.getIndicator(indId);
        AmpLocation locNational = null;


        // true if "all" is selected in region dropdown
        boolean allRegionsSelected = indSecForm.getSelRegionId().equals(ALL_REGIONS_SELECTED);
        //true if "national" is selected in region dropdown
        boolean nationalSelected = (allRegionsSelected ? false : indSecForm.getSelRegionId().equals(NATIONAL_SELECTED));

        if (allRegionsSelected) {
            regions.addAll(indSecForm.getRegions());
        }else {
            if (!nationalSelected) {
                regions.add(DynLocationManagerUtil.getLocation(indSecForm.getSelRegionId(),false));
            } else {
                //National means no region, zone, district are selected
                String iso= FeaturesUtil.getDefaultCountryIso();
                AmpCategoryValueLocations defCountry = DynLocationManagerUtil.getLocationByIso(iso, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
                locNational = LocationUtil.getAmpLocationByCVLocation(defCountry.getId());
                if (locNational == null) {
                    locNational = new AmpLocation();
                    locNational.setCountry(FeaturesUtil.getDefaultCountryIso());
                    locNational.setLocation(defCountry);
                    LocationUtil.saveLocation(locNational);
                }
                
            }
        }


        if (indSecForm.getIndSectId() != null && indSecForm.getIndSectId() > 0) {
            // we are editing IndicatorSector

            indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
            indSec.setSector(selectedSector);
            indSec.setIndicator(selectedIndicator);

            if (allRegionsSelected) {

                /* if all is selected in the region dropdown 
                 * then we should create new sector indicator objects 
                 * for all regions except region which initially is assigned to IndicatorSector
                 * that is why we are removing it from list of regions
                 */

            	regions.remove(indSec.getLocation().getLocation());
                IndicatorUtil.saveIndicatorConnection(indSec);
            } else if (nationalSelected) {
                indSec.setLocation(locNational);
                IndicatorUtil.saveIndicatorConnection(indSec);
                return viewAll(mapping, form, request, response);
            } else {
                AmpLocation ampLoc = getAmpLocation(regions.get(0));
                indSec.setLocation(ampLoc);

                if (!WidgetUtil.indicarorSectorExist(indSecForm.getSector().getAmpSectorId(), ampLoc.getAmpLocationId(), indId, indSecForm.getIndSectId())) {
                    IndicatorUtil.saveIndicatorConnection(indSec);
                    return viewAll(mapping, form, request, response);
                } else {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.widget.indicatorSector.indicatorSectorExists"));
                    saveErrors(request, errors);
                    return mapping.findForward("create");
                }

            }

        }
        if (locNational != null) {
            IndicatorSector ind = new IndicatorSector();
            ind.setLocation(locNational);
            ind.setSector(selectedSector);
            ind.setIndicator(selectedIndicator);
            IndicatorUtil.saveIndicatorConnection(ind);
        } else {
            for (AmpCategoryValueLocations   region : regions) {

                IndicatorSector ind = new IndicatorSector();
                AmpLocation ampLoc = getAmpLocation(region);
                ind.setLocation(ampLoc);
                ind.setSector(selectedSector);
                ind.setIndicator(selectedIndicator);

                if (!WidgetUtil.indicarorSectorExist(indSecForm.getSector().getAmpSectorId(), ampLoc.getAmpLocationId(), indId, null)) {
                    IndicatorUtil.saveIndicatorConnection(ind);
                } else {

                    if (allRegionsSelected) {
                        if (errors.size() == 0) {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.widget.indicatorSector.indicatorSectorSkipped"));
                            saveErrors(request, errors);
                        }
                    } else {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.widget.indicatorSector.indicatorSectorExists"));
                        saveErrors(request, errors);
                        return mapping.findForward("create");
                    }
                }

            }
        }
        return viewAll(mapping, form, request, response);

    }

    /*
     * selects sector
     */
    public ActionForward selectSector(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        HttpSession session = request.getSession();
        Object sectorObject = session.getAttribute(
                "sectorSelected");
        /* Don't be surprised , yes, you see ActivitySector class here :)
        Currently sector selector component is written in 
        Such way that it uses ActivitySector class*/

        ActivitySector selectedSector;
        Long sectorId;
        if (sectorObject instanceof ActivitySector) {
            selectedSector = (ActivitySector) sectorObject;
        } else {
            selectedSector = (ActivitySector) ((ArrayList) sectorObject).get(0);// If they select more then one sector we will use first one
        }
        if (selectedSector.getSubsectorLevel2Id() != -1) {
            sectorId = selectedSector.getSubsectorLevel2Id();
        } else {
            if (selectedSector.getSubsectorLevel1Id() != -1) {
                sectorId = selectedSector.getSubsectorLevel1Id();
            } else {
                sectorId = selectedSector.getSectorId();
            }
        }
        indSecForm.setSector(SectorUtil.getAmpSector(sectorId));
        session.removeAttribute("sectorSelected");
        return mapping.findForward("create");

    }
    /*
     * goes to page where you can create IndicatorSector object
     */

    public ActionForward create(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        //sort indicators by Name
        List<AmpIndicator> allIndicators=IndicatorUtil.getAllIndicators();
        if(allIndicators!=null && allIndicators.size()>0){
        	Collections.sort(allIndicators, new IndicatorUtil.IndicatorNameComparator());
        }
        indSecForm.setIndicators(allIndicators);
        List<AmpCategoryValueLocations>  regions=
                			DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry() ;
        indSecForm.setRegions(new ArrayList(regions));
        indSecForm.setSelIndicator(-1l);
        indSecForm.setSelRegionId(-1l);
        indSecForm.setSector(null);
        indSecForm.setIndSectId(null);
        return mapping.findForward("create");

    }
    /*
     * goes to page where you can edit IndicatorSector object
     */

    public ActionForward edit(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        indSecForm.setIndicators(IndicatorUtil.getAllIndicators());

        List<AmpCategoryValueLocations>  regions=
                			DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry() ;
        indSecForm.setRegions(new ArrayList(regions));
        IndicatorSector indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        if (indSec.getIndicator() != null) {
            indSecForm.setSelIndicator(indSec.getIndicator().getIndicatorId());
        } else {
            indSecForm.setSelIndicator(null);
        }
        if (indSec.getLocation() == null) {
            indSecForm.setSelRegionId(null);
        } else {
          AmpCategoryValueLocations loc = indSec.getLocation().getLocation();
          if(loc!=null){
        	  String parentCategoryName=loc.getParentCategoryValue().getValue();
              if (CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey().equalsIgnoreCase(parentCategoryName) ){
    				//National is selected
                        indSecForm.setSelRegionId(NATIONAL_SELECTED);
    			}
              else{
                   indSecForm.setSelRegionId(indSec.getLocation().getLocation().getId());
              }  
          }          
        }

        indSecForm.setSector(indSec.getSector());
        return mapping.findForward("create");

    }
    /*
     * deletes IndicatorSector objectt
     */

    public ActionForward delete(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        IndicatorSector indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        boolean widgetsExists = ChartWidgetUtil.isWidgetForIndicator(indSec);
        if (widgetsExists){
        	ActionErrors aes = new ActionErrors();
        	ActionError ae = new ActionError("error.aim.deleteIndicatorSector.widgetReferencesIt");
        	aes.add("Cannot delete indicator - widget is referenceing it",ae);
        	saveErrors(request, aes);
        }else{
            IndicatorUtil.removeConnection(indSec);
        }
        return viewAll(mapping, form, request, response);

    }

    /* loads page where you can add/edit values of the selected IndicatorSector*/
    public ActionForward addEditValue(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        IndicatorSector indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        List<AmpIndicatorValue> values = new ArrayList(indSec.getValues());
        indSecForm.setValues(values);
        if (indSec.getIndicator() != null) {
            indSecForm.setSelIndicator(indSec.getIndicator().getIndicatorId());
            indSecForm.setIndicatorName(indSec.getIndicator().getName());
        } else {
            indSecForm.setSelIndicator(null);
            indSecForm.setIndicatorName("");
        }
        if (indSec.getLocation() != null && indSec.getLocation().getLocation() != null) {
            indSecForm.setSelRegionId(indSec.getLocation().getLocation().getId());
            indSecForm.setRegionName(indSec.getLocation().getLocation().getName());
        } else {
            indSecForm.setSelRegionId(null);
            indSecForm.setRegionName("");
        }
        indSecForm.setSector(indSec.getSector());
        if (indSec.getSector()!=null){
            indSecForm.setSectorName(indSec.getSector().getName());
        }else{
            indSecForm.setSectorName("");
        }

        return mapping.findForward("addValue");

    }
    /* addes value to collection, 
     * note: changes are not saved until saveValue is not called 
     */

    public ActionForward addValue(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        if (indSecForm.getValues() == null) {
            indSecForm.setValues(new ArrayList());
        }
        AmpIndicatorValue newValue = new AmpIndicatorValue();
        IndicatorSector indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        newValue.setIndicatorConnection(indSec);
        indSecForm.getValues().add(newValue);
        return mapping.findForward("addValue");

    }
    /* removes selected value from collection, 
     *note: changes are not saved until saveValue is not called
     */

    public ActionForward removeValue(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        indSecForm.getValues().remove(indSecForm.getDeleteValIndex());
        indSecForm.setDeleteValIndex(-1);
        return mapping.findForward("addValue");

    }

    /*
     * saves modified  values of the selected SectorIndicator;
     */
    public ActionForward saveValue(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        IndicatorSector indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        List<AmpIndicatorValue> newlyAddedValues = new ArrayList();
        List<AmpIndicatorValue> remainsValues = new ArrayList();
        for (AmpIndicatorValue val : indSecForm.getValues()) {
            if (val.getIndValId() != null) {
                AmpIndicatorValue oldValue = IndicatorUtil.getAmpIndicatorValue(val.getIndValId());
                oldValue.setValue(val.getValue());
                oldValue.setValueDate(val.getValueDate());
                oldValue.setValueType(val.getValueType());
                remainsValues.add(oldValue);
            } else {
                newlyAddedValues.add(val);
            }

        }

        indSec.getValues().retainAll(remainsValues);
        indSec.getValues().addAll(newlyAddedValues);
        IndicatorUtil.saveIndicatorConnection(indSec);
        return viewAll(mapping, form, request, response);

    }
    /*
     * cancel action, reset values;
     */

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        indSecForm.setSelIndicator(-1l);
        indSecForm.setSelRegionId(-1l);
        indSecForm.setSector(null);
        indSecForm.setIndSectId(null);
        indSecForm.setValues(null);
        indSecForm.setDeleteValIndex(-1);
        indSecForm.setSectorName(null);
        indSecForm.setRegionName(null);
        indSecForm.setIndicatorName(null);
        indSecForm.setIndSectId(null);
        indSecForm.setSelectedPage(0);
        indSecForm.setKeyWord(null);
        indSecForm.setResultsPerPage(new Integer(10));
        indSecForm.setSortBy(null);
        indSecForm.setAlpha(null);
        indSecForm.setAlphaPages(null);
        indSecForm.setRegionId(new Long (-1));
        indSecForm.setSectorId(new Long (-1));
        return viewAll(mapping, form, request, response);

    }
    
    public ActionForward resetSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
    	indSecForm.setKeyWord(null);
        indSecForm.setResultsPerPage(new Integer(10));
        indSecForm.setSectorId(new Long(-1));
        indSecForm.setRegionId(new Long(-1));
        indSecForm.setAlpha(null);
    	return viewAll(mapping, form, request, response);
    }

    private AmpLocation getAmpLocation(AmpCategoryValueLocations region) throws Exception {
        AmpLocation ampLoc = LocationUtil.getAmpLocationByCVLocation(region.getId());
        if (ampLoc == null) {
            ampLoc = new AmpLocation();
            ampLoc.setCountry(FeaturesUtil.getDefaultCountryIso());
            ampLoc.setRegionLocation(region);
            ampLoc.setLocation(region);
            LocationUtil.saveLocation(ampLoc);
        }
        return ampLoc;
    }
}
