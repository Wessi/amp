/**
 * 
 */
package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.form.PledgeForm;


public class PledgeLocationSelected extends Action {
	private static Logger logger = Logger.getLogger(PledgeLocationSelected.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {
		
		PledgeForm plForm 	= (PledgeForm) form;
		Long [] userSelectedLocs	= plForm.getUserSelectedLocs(); 
		if ( userSelectedLocs != null ) {
			for (int i=0; i<userSelectedLocs.length; i++) {
				AmpCategoryValueLocations ampCVLocation		= DynLocationManagerUtil.getLocation( userSelectedLocs[i], false);
				AmpLocation location							= new AmpLocation();
				location.setLocation(ampCVLocation);
				//location.setAncestorLocationNames( DynLocationManagerUtil.getParents(ampCVLocation) );
				location.setName(ampCVLocation.getName());
				//location.setId( ampCVLocation.getId() );
				//location.setPercent("");
				FundingPledgesLocation fpl = new FundingPledgesLocation();
				fpl.setLocation(location);
				if ( plForm.getSelectedLocs() == null )
					plForm.setSelectedLocs( new ArrayList<FundingPledgesLocation>() );
				if ( !plForm.getSelectedLocs().contains(fpl) )
					plForm.getSelectedLocs().add(fpl);
				
				/*AmpCategoryValueLocations ampRegion			= DynLocationManagerUtil.getAncestorByLayer(ampCVLocation, 
																CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
				if ( ampRegion != null ){
					if ( eaForm.getFunding().getFundingRegions() == null )
						eaForm.getFunding().setFundingRegions( new ArrayList<AmpCategoryValueLocations>() );
					eaForm.getFunding().getFundingRegions().add(ampRegion);
				}*/
			}
		}
		
		return mapping.findForward("forward");
	}
}
