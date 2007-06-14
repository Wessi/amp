/**
 * 
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Session;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author mihai
 *
 */
public class ReportsFilterPicker extends MultiAction {
	 
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ReportsFilterPickerForm filterForm=(ReportsFilterPickerForm) form;
		
		String ampReportId=request.getParameter("ampReportId");
		
		if(ampReportId!=null) filterForm.setAmpReportId(new Long(ampReportId));
		
		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession
				.getAttribute(Constants.CURRENT_MEMBER);

		Long ampTeamId = null;
		
		if(teamMember!=null) ampTeamId=teamMember.getTeamId();
		
				
		//create filter dropdowns		
		Collection currency = CurrencyUtil.getAmpCurrency();
		Collection allFisCalenders = DbUtil.getAllFisCalenders();
		List ampSectors = SectorUtil.getAmpSectorsAndSubSectors();

		ArrayList donors;
		if(ampTeamId!=null) donors=DbUtil.getAmpDonors(ampTeamId); else donors=new ArrayList();
		Collection allIndicatorRisks = MEIndicatorsUtil.getAllIndicatorRisks();
		
		filterForm.setCurrencies(currency);
		filterForm.setCalendars(allFisCalenders);
		filterForm.setDonors(donors);
		filterForm.setRisks(allIndicatorRisks);
		filterForm.setSectors(ampSectors);
		filterForm.setFromYears(new ArrayList());
		filterForm.setToYears(new ArrayList());
		
		// loading Activity Rank collection
		if (null == filterForm.getActRankCollection()) {
			filterForm.setActRankCollection(new ArrayList());
			for (int i = 1; i < 6; i++)
				filterForm.getActRankCollection().add(new BeanWrapperImpl(new Integer(i)));
		}
		
		
		for (int i = (2010 - Constants.FROM_YEAR_RANGE); i <= (2010 + Constants.TO_YEAR_RANGE); i++) {
			filterForm.getFromYears().add(new BeanWrapperImpl(new Long(i)));
			filterForm.getToYears().add(new BeanWrapperImpl(new Long(i)));		
		}
		
		return modeSelect(mapping,form,request,response);
	}

	public ActionForward modeReset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReportsFilterPickerForm filterForm=(ReportsFilterPickerForm) form;
		request.setAttribute("reset","reset");
		filterForm.setSelectedDonors(null);
		filterForm.setSelectedRisks(null);
		filterForm.setSelectedSectors(null);
		filterForm.setSelectedStatuses(null);
		filterForm.setCalendar(null);
		filterForm.setCurrency(null);
		filterForm.setFromYear(null);
		filterForm.setToYear(null);
		filterForm.setLineMinRank(null);
		filterForm.setPlanMinRank(null);
	
		return modeApply(mapping,form,request,response);
	}
	
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getParameter("apply")!=null && request.getAttribute("apply")==null) return modeApply(mapping,form,request,response);
		if(request.getParameter("reset")!=null && request.getAttribute("reset")==null) return modeReset(mapping,form,request,response);		
		return mapping.findForward("forward");
	}
	
	/**
	 * generate a session based AmpARFilter object based on the form selections
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modeApply(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReportsFilterPickerForm filterForm=(ReportsFilterPickerForm) form;
		HttpSession httpSession = request.getSession();
		
		Session session = PersistenceManager.getSession();
		
		request.setAttribute("apply","apply");
		AmpARFilter arf = (AmpARFilter) httpSession.getAttribute(ArConstants.REPORTS_FILTER);
		if(arf==null) arf=new AmpARFilter();
		arf.readRequestData(request);
		
		arf.setSectors(Util.getSelectedObjects(AmpSector.class,filterForm.getSelectedSectors()));
		AmpFiscalCalendar selcal=(AmpFiscalCalendar) Util.getSelectedObject(AmpFiscalCalendar.class,filterForm.getCalendar());
		arf.setCalendarType(selcal);
		arf.setFromYear(filterForm.getFromYear()==null || filterForm.getFromYear().longValue()==-1?null:new Integer(filterForm.getFromYear().intValue()));
		arf.setToYear(filterForm.getToYear()==null || filterForm.getToYear().longValue()==-1?null:new Integer(filterForm.getToYear().intValue()));
		arf.setDonors(Util.getSelectedObjects(AmpOrganisation.class,filterForm.getSelectedDonors()));
		AmpCurrency currency = (AmpCurrency) Util.getSelectedObject(AmpCurrency.class,filterForm.getCurrency());
		arf.setCurrency(currency);
		Integer all=new Integer(-1);
		if(!all.equals(filterForm.getLineMinRank())) arf.setLineMinRank(filterForm.getLineMinRank());
		if(!all.equals(filterForm.getPlanMinRank())) arf.setPlanMinRank(filterForm.getPlanMinRank());
		
		if(filterForm.getSelectedStatuses()!=null && filterForm.getSelectedStatuses().length>0)
		arf.setStatuses(new HashSet()); else arf.setStatuses(null);
	
		for (int i = 0;filterForm.getSelectedStatuses()!=null && i < filterForm.getSelectedStatuses().length; i++) {
			AmpCategoryValue value = (AmpCategoryValue) session.load(AmpCategoryValue.class,new Long((String) filterForm.getSelectedStatuses()[i]));
			arf.getStatuses().add(value);
		}
		
		arf.setRisks(Util.getSelectedObjects(AmpIndicatorRiskRatings.class,filterForm.getSelectedRisks()));
		
		httpSession.setAttribute(ArConstants.REPORTS_FILTER,arf);

		return mapping.findForward(arf.isWidget()?"mydesktop":"reportView");
	}
	 
	
}

