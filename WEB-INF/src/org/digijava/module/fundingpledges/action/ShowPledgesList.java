package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.ViewPledgesForm;

public class ShowPledgesList extends Action {

	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        	
		ViewPledgesForm plForm = (ViewPledgesForm) form;
		
		ArrayList<FundingPledges> pledges = PledgesEntityHelper.getPledges();
		Collections.sort(pledges, new Comparator<FundingPledges>(){
			public int compare(FundingPledges a1, FundingPledges a2) 
			{				
				return a1.compareTo(a2);
			}
		});
		TreeMap<FundingPledges, Boolean> map = new TreeMap<FundingPledges, Boolean>();
		
		for (Iterator iterator = pledges.iterator(); iterator.hasNext();) {
			FundingPledges pledge = (FundingPledges) iterator.next();
			pledge.setTotalAmount((double) 0);
			pledge.setYearsList(new TreeSet<Long>());
			Collection<FundingPledgesDetails> fpdl = PledgesEntityHelper.getPledgesDetails(pledge.getId());
			if (fpdl!=null) {
				for (Iterator iterator2 = fpdl.iterator(); iterator2.hasNext();) {
					FundingPledgesDetails fpd = (FundingPledgesDetails) iterator2.next();
					pledge.setTotalAmount(pledge.getTotalAmount() + fpd.getAmount());
					if (fpd.getFundingYear()!=null) {
						pledge.getYearsList().add(fpd.getFundingYear());
					} else if (fpd.getFunding_date()!=null) {
						Long year = new Long(fpd.getFunding_date().getYear()+1900);
						fpd.setFundingYear(year);
						PledgesEntityHelper.updateFundingPledgeDetail(fpd);
						pledge.getYearsList().add(year);
					}
				}
			}
			ArrayList<AmpFundingDetail> fundsRelated = PledgesEntityHelper.getFundingRelatedToPledges(pledge);
			if (fundsRelated == null || fundsRelated.size()==0) {
				map.put(pledge, false);
			} else {
				map.put(pledge, true);
			}
		}
		plForm.setAllFundingPledges(map);
		return mapping.findForward("forward");
    		
	}
}
