package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.FundingOrganization;

public class RemoveSelFundingOrgs 
extends Action {
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		Long selFund[] = eaForm.getSelFundingOrgs();
		Collection prevSelFund = eaForm.getFundingOrganizations();
		Collection newFund = new ArrayList();

		Iterator itr = prevSelFund.iterator();

		while (itr.hasNext()) {
			boolean flag = false;
			FundingOrganization fo = (FundingOrganization) itr.next();
			for (int i = 0; i < selFund.length; i++) {
				if (fo.getAmpOrgId().equals(selFund[i])) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				newFund.add(fo);
			}

		}

		eaForm.setFundingOrganizations(newFund);
		eaForm.setStep("3");
		eaForm.setSelFundingOrgs(null);
		return mapping.findForward("forward");
	
	}
}

