package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.NpdSettings;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.NpdForm;
import org.digijava.module.aim.helper.FilteredAmpTheme;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ChartUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.NpdUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * NPD main page.
 * Most things on this page are asynchronous,
 * so this action just sets lists and settings like data.
 * @author Irakli Kobiashvili
 *
 */
public class ViewNPD extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		NpdForm npdForm=(NpdForm)form;
		//dimensions should come from NPD settings not from constants
		TeamMember teamMember = (TeamMember) request.getSession().getAttribute("currentMember");
		Long teamId=teamMember.getTeamId();
		NpdSettings npdSettings=NpdUtil.getCurrentSettings(teamId);
		npdForm.setGraphWidth(npdSettings.getWidth().intValue());
		npdForm.setGraphHeight(npdSettings.getHeight().intValue());
		npdForm.setYears(new ArrayList(ProgramUtil.getYearsBeanList()));
//		if (npdForm.getSelYears() == null)
		//  If default years for this team is null, than default years will be last three years from Yearsbean list                 
		if(npdSettings.getSelectedYearsForTeam()!=null){
			npdForm.setSelYears(defaulYearsGeneratorForNpdGraph(npdSettings.getSelectedYearsForTeam()));
		}else {
			npdForm.setSelYears(selectNYears(ProgramUtil.getYearsBeanList(),3));
		}
		
		npdForm.setDummyYear("-1");
		npdForm.setDonors(getDonorsList(30));
        npdForm.setDefaultProgram(FeaturesUtil.getGlobalSettingValue("NPD Default Program"));
		//npdForm.setStatuses(getStatuses());

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
	
	private String[] defaulYearsGeneratorForNpdGraph(String selectedYearForTeam){
		StringTokenizer toka=new StringTokenizer(selectedYearForTeam,",");
		String[] result=new String[toka.countTokens()];
		int c=0;
		while (toka.hasMoreTokens()) {
			result[c++]=toka.nextToken();
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
