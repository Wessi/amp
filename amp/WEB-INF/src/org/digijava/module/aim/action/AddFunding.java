package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

public class AddFunding extends Action {

	private static Logger logger = Logger.getLogger(AddFunding.class);

	public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse
								response) throws java.lang.Exception {

		EditActivityForm formBean = (EditActivityForm) form;
		formBean.setReset(false);
		Long orgId = formBean.getOrgId();
		 if ( logger.isDebugEnabled() )
		        logger.debug("< orgId=" + orgId);

		formBean.setAssistanceType(null);
		formBean.setOrgFundingId("");
		formBean.setSignatureDate("");
		formBean.setReportingDate("");
		formBean.setPlannedCompletionDate("");
		formBean.setPlannedStartDate("");
		formBean.setActualCompletionDate("");
		formBean.setActualStartDate("");
		formBean.setFundingConditions("");
		formBean.setFundingDetails(null);
		//formBean.setFundingMTEFProjections(null);
		formBean.setEditFunding(false);
		formBean.setNumComm(0);
		formBean.setNumDisb(0);
		formBean.setNumExp(0);
                formBean.setNumDisb(0);

		formBean.setFundingMTEFProjections( new ArrayList<MTEFProjection>() );
		boolean afterFiscalYearStart	= isAfterFiscalYearStart();
		for (int i=0; i<3; i++) {
			MTEFProjection me	= getMTEFProjection(request.getSession(), i, afterFiscalYearStart );
			formBean.getFundingMTEFProjections().add( me );
		}
		formBean.setNumProjections(formBean.getFundingMTEFProjections().size());

		Collection fundingOrganizations = formBean.getFundingOrganizations();
		Iterator iter = fundingOrganizations.iterator();
		while ( iter.hasNext() )	{
			FundingOrganization fundingOrganization = (FundingOrganization)iter.next();
			if ( orgId.equals( fundingOrganization.getAmpOrgId() ) )	{
				formBean.setOrgName(fundingOrganization.getOrgName());
				break;
			}
		}
		Collection c 	= CategoryManagerUtil.getAmpCategoryValueCollectionByKey(
								CategoryConstants.TYPE_OF_ASSISTENCE_KEY, null);
		if (c != null) {
			Iterator tempItr = c.iterator();
			while (tempItr.hasNext()) {
				AmpCategoryValue assistCategoryValue = (AmpCategoryValue) tempItr.next();
				if (assistCategoryValue.getValue().equalsIgnoreCase("Grant")) {
					formBean.setAssistanceType(assistCategoryValue.getId());
					break;
				}
			}
		}
		formBean.setOrganizations(DbUtil.getAllOrganisation());
		formBean.setEvent(null);
		formBean.setDupFunding(true);
		formBean.setFirstSubmit(false);
		return mapping.findForward("forward");
	}
	public static String getFYDate(int numOfAddedYears) {
		try {
			int year;
			String yearGS	= FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.CURRENT_SYSTEM_YEAR );
			if ( yearGS.equals(GlobalSettingsConstants.SYSTEM_YEAR) )
				year		= Util.getSystemYear();
			else
				year		= Integer.parseInt( yearGS );
			year			+= numOfAddedYears;

			String date		= FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.FISCAL_YEAR_END_DATE ) + "/" + year;

			return date;
		}
		catch(Exception E) {
			logger.error("Error getting Fiscal Year global settings");
			E.printStackTrace();
			return null;
		}
	}
	public static boolean isAfterFiscalYearStart() {
		String date				= getFYDate(0);
		String [] dateElements	= date.split("/");
		GregorianCalendar gcFY	= new GregorianCalendar(
										Integer.parseInt(dateElements[2]),
										Integer.parseInt(dateElements[1]) - 1,
										Integer.parseInt(dateElements[0])
										);
		GregorianCalendar gcCur	= new GregorianCalendar();
		gcCur.set(Calendar.YEAR, gcFY.get(Calendar.YEAR));

		return gcCur.after(gcFY);
	}
	public static MTEFProjection getMTEFProjection(HttpSession session, int index, boolean afterFiscalYearStart) {
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		String currCode = Constants.DEFAULT_CURRENCY;
		if (teamMember.getAppSettings() != null) {
			ApplicationSettings appSettings = teamMember.getAppSettings();
			if (appSettings.getCurrencyId() != null) {
				currCode = CurrencyUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
			}
		}

		int offset;
		if (afterFiscalYearStart)
			offset	= 1;
		else
			offset 	= 0;

		MTEFProjection mp 		= new MTEFProjection();
		mp.setCurrencyCode(currCode);
		mp.setProjectionDate( getFYDate(index+offset) );
		mp.setIndex( index );
		mp.setIndexId( System.currentTimeMillis() );
		return mp;
	}
}
