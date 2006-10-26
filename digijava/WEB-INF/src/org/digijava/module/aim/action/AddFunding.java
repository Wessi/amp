package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTermsAssist;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.util.DbUtil;

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
		formBean.setEditFunding(false);
		formBean.setNumComm(0);
		formBean.setNumDisb(0);
		formBean.setNumExp(0);
		
		
		Collection fundingOrganizations = formBean.getFundingOrganizations();
		Iterator iter = fundingOrganizations.iterator();
		while ( iter.hasNext() )	{
			FundingOrganization fundingOrganization = (FundingOrganization)iter.next();
			if ( orgId.equals( fundingOrganization.getAmpOrgId() ) )	{
				formBean.setOrgName(fundingOrganization.getOrgName());
				break;
			}
		}
		Collection c = DbUtil.getAllAssistanceTypes();
		formBean.setAssistanceTypes(c);
		if (c != null) {
			Iterator tempItr = c.iterator();
			while (tempItr.hasNext()) {
				AmpTermsAssist assist = (AmpTermsAssist) tempItr.next();
				if (assist.getTermsAssistName().equalsIgnoreCase("Grant")) {
					formBean.setAssistanceType(assist.getAmpTermsAssistId());
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
}
