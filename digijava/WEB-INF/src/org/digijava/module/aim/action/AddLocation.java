/** 
 * AddLocation.java
 * @author Akash Sharma
 * Created on 1/02/2005
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpZone;
import org.digijava.module.aim.dbentity.AmpWoreda;
import org.digijava.module.aim.form.AddLocationForm;
import javax.servlet.http.*;

public class AddLocation extends Action 
{
  private static Logger logger = Logger.getLogger(AddLocation.class);

  public ActionForward execute(ActionMapping mapping,
							   ActionForm form,
							   HttpServletRequest request,
							   HttpServletResponse response) throws java.lang.Exception
  {
	 AddLocationForm addForm = (AddLocationForm) form;
	 
	 String level = addForm.getEdLevel();
	 String action = addForm.getEdAction();
	 boolean edFlag = false;
	 if (addForm.getEdFlag()!= null && addForm.getEdFlag().equals("on"))
	 	edFlag = true;

	if ("create".equals(action)) {
	 if (addForm.getName() == null || edFlag) {
	 	if (edFlag) {
	 		addForm.setName("");
			addForm.setCode("");
			addForm.setGsLat("");
			addForm.setGsLong("");
			addForm.setGeoCode("");
			addForm.setDescription("");
			addForm.setIso("");
			addForm.setIso3("");
	 	}
	 	return mapping.findForward("forward");
	 }
	 else {
	 	Country ctry  = new Country();
	 	AmpRegion reg = null;
 		AmpZone zon   = null;
	 	if (addForm.getCountryId() != null) {
	 		ctry = DbUtil.getDgCountry(addForm.getCountryId());
 		}
	 	if ("country".equalsIgnoreCase(level)){
	 		if (LocationUtil.chkDuplicateIso(addForm.getName(), addForm.getIso(), addForm.getIso3())) {
	 			ctry.setCountryName(addForm.getName());
		 		ctry.setIso(addForm.getIso());
		 		ctry.setIso3(addForm.getIso3());
		 		ctry.setAvailable(true);
		 		ctry.setStat("t");
		 		ctry.setShowCtry("t");
		 		ctry.setDecCtryFlag("t");
		 		ctry.setCountryId(new Long(LocationUtil.getDgCountryWithMaxCountryId().intValue() + 1));
		 		ctry.setMessageLangKey("cn:" + ctry.getIso());
		 		DbUtil.add(ctry);
	 		}
	 		else {
	 			ActionErrors errors = new  ActionErrors();
	 			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.addLocation.duplicateCountryInfo"));
	 			saveErrors(request, errors);
	 			return mapping.getInputForward();
	 		}
	 	}
		if (level.equals("region")){
	 		reg = new AmpRegion();
	 		if(ctry != null) {
	 			reg.setCountry(ctry);
	 		}
	 		if (addForm.getDescription() == null || 
					addForm.getDescription().trim().length() == 0) {
				reg.setDescription(" 	");}
			else
				reg.setDescription(addForm.getDescription());
			reg.setGeoCode(addForm.getGeoCode());
	 		reg.setGsLat(addForm.getGsLat());
	 		reg.setGsLong(addForm.getGsLong());
	 		reg.setName(addForm.getName());
	 		reg.setRegionCode(addForm.getCode());
	 		DbUtil.add(reg);
	 	}
	 	if (level.equals("zone")){
	 		reg = new AmpRegion();
	 		zon = new AmpZone();
	 		if(ctry != null) {
	 			zon.setCountry(ctry);
	 		}
	 		if (addForm.getRegionId() != null) {
		 		reg = LocationUtil.getAmpRegion(addForm.getRegionId());
	 		}
	 		if(reg != null) {
	 			zon.setRegion(reg);
	 		}
	 		if (addForm.getDescription() == null ||
					addForm.getDescription().trim().length() == 0)
				zon.setDescription(" ");
			else
				zon.setDescription(addForm.getDescription());
			zon.setGeoCode(addForm.getGeoCode());
	 		zon.setGsLat(addForm.getGsLat());
	 		zon.setGsLong(addForm.getGsLong());
	 		zon.setName(addForm.getName());
	 		zon.setZoneCode(addForm.getCode());
	 		DbUtil.add(zon);
	 	}		 
	 	if (level.equals("woreda")){
	 		AmpWoreda w = new AmpWoreda();
	 		zon = new AmpZone();
	 		if (addForm.getZoneId() != null) {
	 			zon = LocationUtil.getAmpZone(addForm.getZoneId());
	 		}		 	
	 		if(ctry != null) {
	 			w.setCountry(ctry);
	 		}
	 		if(zon != null) {
	 			w.setZone(zon);
	 		}
	 		if (addForm.getDescription() == null ||
					addForm.getDescription().trim().length() == 0)
				w.setDescription(" ");
			else
				w.setDescription(addForm.getDescription());
			w.setGeoCode(addForm.getGeoCode());
	 		w.setGsLat(addForm.getGsLat());
	 		w.setGsLong(addForm.getGsLong());
	 		w.setName(addForm.getName());
	 		w.setWoredaCode(addForm.getCode());
	 		DbUtil.add(w);
	 	}
		addForm.setEdFlag("yes");
		return mapping.findForward("added");
	 }
   }
   else if ("edit".equals(action)) {
   	if (addForm.getName() == null || edFlag) {
			if (level.equals("region")) {
				AmpRegion obj = LocationUtil.getAmpRegion(addForm.getRegionId());
				if (obj.getName() != null)
					addForm.setName(obj.getName());
				else
					addForm.setName("");
				if (obj.getRegionCode() != null)
					addForm.setCode(obj.getRegionCode());
				else
					addForm.setCode("");
				if (obj.getGsLat() != null)
					addForm.setGsLat(obj.getGsLat());
				else
					addForm.setGsLat("");
				if (obj.getGsLong() != null)
					addForm.setGsLong(obj.getGsLong());
				else
					addForm.setGsLong("");
				if (obj.getGeoCode() != null)
					addForm.setGeoCode(obj.getGeoCode());
				else
					addForm.setGeoCode("");
				if (obj.getDescription() != null)
					addForm.setDescription(obj.getDescription());
				else
					addForm.setDescription("");
			}
			if (level.equals("zone")) {
				AmpZone obj = LocationUtil.getAmpZone(addForm.getZoneId());
				if (obj.getName() != null)
					addForm.setName(obj.getName());
				else
					addForm.setName("");
				if (obj.getZoneCode() != null)
					addForm.setCode(obj.getZoneCode());
				else
					addForm.setCode("");
				if (obj.getGsLat() != null)
					addForm.setGsLat(obj.getGsLat());
				else
					addForm.setGsLat("");
				if (obj.getGsLong() != null)
					addForm.setGsLong(obj.getGsLong());
				else
					addForm.setGsLong("");
				if (obj.getGeoCode() != null)
					addForm.setGeoCode(obj.getGeoCode());
				else
					addForm.setGeoCode("");
				if (obj.getDescription() != null)
					addForm.setDescription(obj.getDescription());
				else
					addForm.setDescription("");
			}
			if (level.equals("woreda")) {
				AmpWoreda obj = LocationUtil.getAmpWoreda(addForm.getWoredaId());
				if (obj.getName() != null)
					addForm.setName(obj.getName());
				else
					addForm.setName("");
				if (obj.getWoredaCode() != null)
					addForm.setCode(obj.getWoredaCode());
				else
					addForm.setCode("");
				if (obj.getGsLat() != null)
					addForm.setGsLat(obj.getGsLat());
				else
					addForm.setGsLat("");
				if (obj.getGsLong() != null)
					addForm.setGsLong(obj.getGsLong());
				else
					addForm.setGsLong("");
				if (obj.getGeoCode() != null)
					addForm.setGeoCode(obj.getGeoCode());
				else
					addForm.setGeoCode("");
				if (obj.getDescription() != null)
					addForm.setDescription(obj.getDescription());
				else
					addForm.setDescription("");
			}
			addForm.setEdFlag("on");
	 	return mapping.findForward("forward");
	 }
   	else {
		 	if (level.equals("region")){
		 		AmpRegion obj = null;
		 		if (addForm.getRegionId() != null) {
		 			obj = LocationUtil.getAmpRegion(addForm.getRegionId());
		 		}
			 	
		 		obj.setGeoCode(addForm.getGeoCode());
		 		obj.setGsLat(addForm.getGsLat());
		 		obj.setGsLong(addForm.getGsLong());
		 		obj.setName(addForm.getName());
		 		obj.setRegionCode(addForm.getCode());
		 		if (addForm.getDescription() == null ||
						addForm.getDescription().trim().length() == 0)
					obj.setDescription(" ");
				else
					obj.setDescription(addForm.getDescription());
		 
		 		DbUtil.update(obj);
		 	}
		 	if (level.equals("zone")){
		 		AmpZone obj = null;
		 		if (addForm.getZoneId() != null) {
		 			obj = LocationUtil.getAmpZone(addForm.getZoneId());
		 		}
			 	
		 		obj.setGeoCode(addForm.getGeoCode());
		 		obj.setGsLat(addForm.getGsLat());
		 		obj.setGsLong(addForm.getGsLong());
		 		obj.setName(addForm.getName());
		 		obj.setZoneCode(addForm.getCode());
		 		if (addForm.getDescription() == null ||
						addForm.getDescription().trim().length() == 0)
					obj.setDescription(" ");
				else
					obj.setDescription(addForm.getDescription());
		 
		 		DbUtil.update(obj);
		 	}
		 	if (level.equals("woreda")){
		 		AmpWoreda obj = null;
		 		if (addForm.getWoredaId() != null) {
		 			obj = LocationUtil.getAmpWoreda(addForm.getWoredaId());
		 		}
			 	
		 		obj.setGeoCode(addForm.getGeoCode());
		 		obj.setGsLat(addForm.getGsLat());
		 		obj.setGsLong(addForm.getGsLong());
		 		obj.setName(addForm.getName());
		 		obj.setWoredaCode(addForm.getCode());
		 		if (addForm.getDescription() == null ||
						addForm.getDescription().trim().length() == 0)
					obj.setDescription(" ");
				else
					obj.setDescription(addForm.getDescription());
		 
		 		DbUtil.update(obj);
		 	}
		 	
		 	addForm.setEdFlag("yes");
		 	return mapping.findForward("added");
   	}
   }else if ("delete".equals(action)) {
   		if (level.equals("region")){
   			AmpRegion obj = null;
   			if (addForm.getRegionId() != null) {
   				obj = LocationUtil.getAmpRegion(addForm.getRegionId());
   				DbUtil.delete(obj);
   			}
   		}
   		if (level.equals("zone")){
   			AmpZone obj = null;
   			if (addForm.getZoneId() != null) {
   				obj = LocationUtil.getAmpZone(addForm.getZoneId());
   				DbUtil.delete(obj);
   			}
   		}
   		if (level.equals("woreda")){
   			AmpWoreda obj = null;
   			if (addForm.getWoredaId() != null) {
   				obj = LocationUtil.getAmpWoreda(addForm.getWoredaId());
   				DbUtil.delete(obj);
   			}
   		}

	 	addForm.setEdFlag("yes");
   		return mapping.findForward("added");
   }
   		
	return null;
  }
 }
