package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.NpdForm;
import org.digijava.module.aim.helper.FilteredAmpTheme;
import org.digijava.module.aim.util.ChartUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ProgramUtil;

/**
 * NPD main page.
 * Most things on this page are asynchronous,
 * so this action just sets lists and settingslike data.
 * @author Irakli Kobiashvili
 *
 */
public class ViewNPD extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		NpdForm npdForm=(NpdForm)form;
		//dimensions should come from NPD settings not from constants
		npdForm.setGraphWidth(ChartUtil.CHART_WIDTH);
		npdForm.setGraphHeight(ChartUtil.CHART_HEIGHT);
		npdForm.setYears(new ArrayList(ProgramUtil.getYearsBeanList()));
//		if (npdForm.getSelYears() == null)
		npdForm.setSelYears(selectNYears(ProgramUtil.getYearsBeanList(),3));
		npdForm.setDummyYear("-1");
		npdForm.setDonors(getDonorsList(30));
		npdForm.setStatuses(getStatuses());
		
		List themes = ProgramUtil.getAllThemes(true);
		npdForm.setAllThemes( FilteredAmpTheme.transformAmpThemeList(themes) );
		
		return mapping.findForward("forward");
	}
	
	private String[] selectNYears(Collection years,int num){
		String[] result= {};
		if (years != null && years.size() > num){
			List temp=new ArrayList(years);
			result= new String[num];
			int c=0;
			for (int i=temp.size()-num;i<temp.size();i++){
				LabelValueBean year = (LabelValueBean) temp.get(i);
				result[c++]=year.getValue();
			}
		}
		return result;
	}

	private List getDonorsList(int nameLimit) {
		List result = null;
		Collection dbDonors = DbUtil.getAllDonorOrgs();
		if (dbDonors != null) {
			result = new ArrayList();
			Iterator dbIter = dbDonors.iterator();
			while (dbIter.hasNext()) {
				AmpOrganisation donor = (AmpOrganisation) dbIter.next();
				String id = donor.getAmpOrgId().toString();
				String name = (donor.getName().length() > nameLimit) ? donor
						.getName().substring(0, nameLimit)
						+ "..." : donor.getName();
				LabelValueBean lvBean = new LabelValueBean(name, id);
				result.add(lvBean);
			}
		}
		return result;
	}

	private List getStatuses() throws AimException {
		List result = DbUtil.getAllAmpStatusesLVB();
		return result;
	}

}
