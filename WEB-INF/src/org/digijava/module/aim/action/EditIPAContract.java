    package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.IPAContractForm;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author mihai
 * 
 */
public class EditIPAContract extends MultiAction {

    /**
     *
     */
    public EditIPAContract() {
        super();
    // TODO Auto-generated constructor stub
    }

    public boolean hasUnselectedItems(List items) {
        Iterator i = items.iterator();
        while (i.hasNext()) {
            String element = (String) i.next();
            if ("-1".equals(element)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasInvalidAmounts(List items) {
        Iterator i = items.iterator();
        while (i.hasNext()) {
            String element = (String) i.next();
            try {
            	if(element==null) return false;
               if(!"".equals(element)){
               Double.parseDouble(element);
               }
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }

    /*
     * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPAContractForm eaf = (IPAContractForm) form;
        eaf.setCurrencies(CurrencyUtil.getAmpCurrency());

        return modeSelect(mapping, form, request, response);
    }

    public ActionForward modeDelete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        EditActivityForm eaf = (EditActivityForm) session.getAttribute("eaf");
        Integer indexId = new Integer(request.getParameter("indexId"));
        eaf.getContracts().remove(indexId - 1);
        //request.setAttribute("close", "close");
        return modeFinalize(mapping, form, request, response);
       
    }

    public ActionForward modeEdit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();

        EditActivityForm eaf = (EditActivityForm) session.getAttribute("eaf");
        IPAContractForm euaf = (IPAContractForm) form;
        euaf.reset(mapping, request);
        Integer indexId = new Integer(request.getParameter("indexId")) - 1;
        IPAContract contract = (IPAContract) eaf.getContracts().get(indexId);
        
        for (Iterator it = eaf.getFundingDetails().iterator(); it.hasNext();) {
			FundingDetail afd = (FundingDetail) it.next();
			if(afd.getContract()!=null)
				if(afd.getContract().getContractName().equals(contract.getContractName()))
					euaf.getFundingDetailsLinked().add(afd);
		}
        
        euaf.setIndexId(indexId);
        euaf.setContractName(contract.getContractName());
        euaf.setDescription(contract.getDescription());
        euaf.setContractingOrganizationText(contract.getContractingOrganizationText());
        
        euaf.setId(contract.getId());
        
        if (contract.getActivityCategory() != null) {
            euaf.setActivityCategoryId(contract.getActivityCategory().getId());
        }
        else euaf.setActivityCategoryId((long)0);
        
        if (contract.getStatus() != null) {
            euaf.setStatusId(contract.getStatus().getId());
        }
        else euaf.setStatusId((long)0);
        
        if (contract.getType()!= null) {
            euaf.setTypeId(contract.getType().getId());
        }
        else euaf.setTypeId((long)0);
        
        if (contract.getContractType()!= null) {
            euaf.setContractTypeId(contract.getContractType().getId());
        }
        else euaf.setContractTypeId((long)0);
        
        if (contract.getOrganization() != null) {
            euaf.setContrOrg(contract.getOrganization().getAmpOrgId());
        }
        else euaf.setContrOrg((long)0);
        
        if (contract.getStartOfTendering() != null) {
            euaf.setStartOfTendering(DateTimeUtil.parseDateForPicker2(contract.getStartOfTendering()));
        }
        else euaf.setStartOfTendering("");
        
        if (contract.getSignatureOfContract() != null) {
            euaf.setSignatureOfContract(DateTimeUtil.parseDateForPicker2(contract.getSignatureOfContract()));
        }
        else euaf.setSignatureOfContract("");
        
        if (contract.getContractValidity() != null) {
            euaf.setContractValidity(DateTimeUtil.parseDateForPicker2(contract.getContractValidity()));
        }
        else euaf.setContractValidity("");

        
        
        
        if (contract.getTotalPrivateContribAmountDate() != null) {
            euaf.setTotalPrivateContribAmountDate(DateTimeUtil.parseDateForPicker2(contract.getTotalPrivateContribAmountDate()));
        }
        else euaf.setTotalPrivateContribAmountDate("");
        
        if (contract.getTotalNationalContribRegionalAmountDate() != null) {
            euaf.setTotalNationalContribRegionalAmountDate(DateTimeUtil.parseDateForPicker2(contract.getTotalNationalContribRegionalAmountDate()));
        }
        else euaf.setTotalNationalContribRegionalAmountDate("");

        if (contract.getTotalNationalContribIFIAmountDate() != null) {
            euaf.setTotalNationalContribIFIAmountDate(DateTimeUtil.parseDateForPicker2(contract.getTotalNationalContribIFIAmountDate()));
        }
        else euaf.setTotalNationalContribIFIAmountDate("");

        if (contract.getTotalNationalContribCentralAmountDate() != null) {
            euaf.setTotalNationalContribCentralAmountDate(DateTimeUtil.parseDateForPicker2(contract.getTotalNationalContribCentralAmountDate()));
        }
        else euaf.setTotalNationalContribCentralAmountDate("");

        if (contract.getTotalECContribINVAmountDate() != null) {
            euaf.setTotalECContribINVAmountDate(DateTimeUtil.parseDateForPicker2(contract.getTotalECContribINVAmountDate()));
        }
        else euaf.setTotalECContribINVAmountDate("");
        
        if (contract.getTotalECContribIBAmountDate() != null) {
            euaf.setTotalECContribIBAmountDate(DateTimeUtil.parseDateForPicker2(contract.getTotalECContribIBAmountDate()));
        }
        else euaf.setTotalECContribIBAmountDate("");
        
        

        //contractValidity
        if (contract.getContractCompletion() != null) {
            euaf.setContractCompletion(DateTimeUtil.parseDateForPicker2(contract.getContractCompletion()));
        }
        else euaf.setContractCompletion("");
        
        if (contract.getTotalECContribIBAmount() != null) {
            euaf.setTotalECContribIBAmount(String.valueOf(contract.getTotalECContribIBAmount()));
        }
        
        if (contract.getTotalAmount() != null) {
            euaf.setTotalAmount(String.valueOf(contract.getTotalAmount()));
        }
        else euaf.setTotalAmount("");
        
        if (contract.getContractTotalValue() != null) {
            euaf.setContractTotalValue(String.valueOf(contract.getContractTotalValue()));
        }
        else euaf.setContractTotalValue("");
        
        
        if (contract.getTotalAmountCurrency() != null) {
            euaf.setTotalAmountCurrency(contract.getTotalAmountCurrency().getAmpCurrencyId());
        }
        else  euaf.setTotalAmountCurrency((long)0);
        
        //dibusrsementsGlobalCurrency
        if (contract.getDibusrsementsGlobalCurrency()!= null) {
            euaf.setDibusrsementsGlobalCurrency(contract.getDibusrsementsGlobalCurrency().getAmpCurrencyId());
        }
        else euaf.setDibusrsementsGlobalCurrency((long)0);
                
        if (contract.getTotalECContribINVAmount() != null) {
            euaf.setTotalECContribINVAmount(String.valueOf(contract.getTotalECContribINVAmount()));
        }
        else euaf.setTotalECContribINVAmount("");
        
        if (contract.getTotalNationalContribCentralAmount() != null) {
            euaf.setTotalNationalContribCentralAmount(String.valueOf(contract.getTotalNationalContribCentralAmount()));
        }
        else  euaf.setTotalNationalContribCentralAmount("");
        
        if (contract.getTotalNationalContribRegionalAmount() != null) {
            euaf.setTotalNationalContribRegionalAmount(String.valueOf(contract.getTotalNationalContribRegionalAmount()));
        }
        else euaf.setTotalNationalContribRegionalAmount("");
        
        
        if (contract.getTotalNationalContribIFIAmount() != null) {
            euaf.setTotalNationalContribIFIAmount(String.valueOf(contract.getTotalNationalContribIFIAmount()));
        }
        else euaf.setTotalNationalContribIFIAmount("");
        
        if (contract.getTotalPrivateContribAmount() != null) {

            euaf.setTotalPrivateContribAmount(String.valueOf(contract.getTotalPrivateContribAmount()));
        }
        else euaf.setTotalPrivateContribAmount("");
        
        if (contract.getOrganizations() != null){
        	ArrayList<AmpOrganisation> corgs = new ArrayList<AmpOrganisation>(contract.getOrganizations());
        	euaf.setOrganisations(corgs);
        }
        
        String cc=eaf.getCurrCode();
        if (contract.getDisbursements() != null) {
            ArrayList<IPAContractDisbursement> disbs = new ArrayList(contract.getDisbursements());
            
            //if there is no disbursement global currency saved in db we'll use the default from edit activity form
            
           if(contract.getDibusrsementsGlobalCurrency()!=null)
        	   cc=contract.getDibusrsementsGlobalCurrency().getCurrencyCode();
           double td=0;
           double usdAmount=0;  
   		   double finalAmount=0; 
   		   


   		   for(Iterator j=disbs.iterator();j.hasNext();)
      	  	{
      		  IPAContractDisbursement cd=(IPAContractDisbursement) j.next();
      		  // converting the amount to the currency from the top and adding to the final sum.
      		  if(cd.getAmount()!=null)
      			  {
      			  	usdAmount = CurrencyWorker.convertToUSD(cd.getAmount().doubleValue(),cd.getCurrCode());
      			  	finalAmount = CurrencyWorker.convertFromUSD(usdAmount,cc);
      			  	td+=finalAmount;
      			  }
      	  	}
      	  	
      	  	//eaf.getCurrCode();
      	  	euaf.setTotalDisbursements(td);
            euaf.setContractDisbursements(disbs);
        }

        if(contract.getTotalAmount()!=null)
   	  	{
//        	double usdAmount1=0;  
// 		   double finalAmount1=0; 
//        	try {
//				usdAmount1 = CurrencyWorker.convertToUSD(contract.getTotalAmount().doubleValue(),contract.getTotalAmountCurrency().getCurrencyCode());
//			} catch (AimException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			  	try {
//				finalAmount1 = CurrencyWorker.convertFromUSD(usdAmount1,cc);
//			} catch (AimException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}	
//			double amountRate=0;
//			if(finalAmount1!=0 )
//				amountRate=contract.getTotalDisbursements().doubleValue()/finalAmount1;
//        	
   	  		contract.setExecutionRate(ActivityUtil.computeExecutionRateFromTotalAmount(contract, cc));
   	  		euaf.setExecutionRate(ActivityUtil.computeExecutionRateFromTotalAmount(contract, cc));
   	  	}
        else if(contract.getContractTotalValue()!=null){
//	           double usdAmount1=0;  
//	  		   double finalAmount1=0; 
//	         	try {
//	 				usdAmount1 = CurrencyWorker.convertToUSD(contract.getContractTotalValue().doubleValue(),contract.getTotalAmountCurrency().getCurrencyCode());
//	 			} catch (AimException e) {
//	 				// TODO Auto-generated catch block
//	 				e.printStackTrace();
//	 			}
//	 			  	try {
//	 				finalAmount1 = CurrencyWorker.convertFromUSD(usdAmount1,cc);
//	 			} catch (AimException e) {
//	 				// TODO Auto-generated catch block
//	 				e.printStackTrace();
//	 			}	
//	 			double amountRate=0;
//	 			if(finalAmount1!=0 )
//	 				amountRate=contract.getTotalDisbursements().doubleValue()/finalAmount1;
//	         	
	    	  		contract.setExecutionRate(ActivityUtil.computeExecutionRateFromContractTotalValue(contract, cc));
	    	  		euaf.setExecutionRate(ActivityUtil.computeExecutionRateFromContractTotalValue(contract, cc));
	        }
	   	  	else {
	   	  		contract.setExecutionRate(new Double(0));
	   	  		euaf.setExecutionRate(new Double(0));
	   	  	}

        return modeFinalize(mapping, form, request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (request.getParameter("editEU") != null) {
            return modeEdit(mapping, form, request, response);
        }
        if (request.getParameter("new") != null) {
            return modeNew(mapping, form, request, response);
        }
        if (request.getParameter("save") != null) {
            return modeValidateSave(mapping, form, request, response);
        }
        if (request.getParameter("addFields") != null) {
            return modeAddFields(mapping, form, request, response);
        }
        if (request.getParameter("removeFields") != null) {
            return modeRemoveFields(mapping, form, request, response);
        }
        if (request.getParameter("removeOrgs") != null) {
            return modeRemoveOrgs(mapping, form, request, response);
        }
        if (request.getParameter("deleteEU") != null) {
            return modeDelete(mapping, form, request, response);
        }
        return modeFinalize(mapping, form, request, response);
    }

    public ActionForward modeNew(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPAContractForm eaf = (IPAContractForm) form;
        //eaf.reset(mapping, request);
        eaf.setContractDisbursements(new ArrayList<IPAContractDisbursement>());
        eaf.setIndexId(-1);
        Long currId=new Long(-1);
        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
          if (tm != null && tm.getAppSettings() != null && tm.getAppSettings()
          .getCurrencyId() != null) {
            
              AmpCurrency curr=CurrencyUtil.
                  getAmpcurrency(
                      tm.getAppSettings()
                      .getCurrencyId());
              if(curr!=null){
                      currId = curr.getAmpCurrencyId();
              }
          }
        eaf.setDibusrsementsGlobalCurrency(currId);
        eaf.setTotalAmountCurrency(currId);
        eaf.setOrganisations(new ArrayList());
        
        request.setAttribute("editingNew", "editingNew");
        
        return modeFinalize(mapping, form, request, response);
    }

    public ActionForward modeValidateSave(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionErrors errors = new ActionErrors();
        IPAContractForm eaf = (IPAContractForm) form;
        List<String> amountsList = new ArrayList<String>();
        amountsList.add(eaf.getTotalECContribIBAmount());
        amountsList.add(eaf.getTotalECContribINVAmount());

        amountsList.add(eaf.getTotalNationalContribCentralAmount());
        amountsList.add(eaf.getTotalNationalContribIFIAmount());
        amountsList.add(eaf.getTotalNationalContribRegionalAmount());

        amountsList.add(eaf.getTotalPrivateContribAmount());


        if (hasInvalidAmounts(amountsList)) {
            errors.add("title", new ActionError(
                    "error.aim.ipacontract.invalidAmountFormat"));
        }
        //if(hasUnselectedItems(eaf.())) errors.add("title", new ActionError(
        //"error.aim.euactivity.selectDonor"));

        saveErrors(request, errors);

        if (!errors.isEmpty()) {
            return modeFinalize(mapping, form, request, response);
        }

        return modeSave(mapping, form, request, response);

    }

    public ActionForward modeFinalize(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPAContractForm eaf = (IPAContractForm) form;

        return mapping.findForward("forward");



    }

    public ActionForward modeAddFields(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPAContractForm eaf = (IPAContractForm) form;
        IPAContractDisbursement icd = new IPAContractDisbursement();
        eaf.getContractDisbursements().add(icd);
        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
          if (tm != null && tm.getAppSettings() != null && tm.getAppSettings()
          .getCurrencyId() != null) {
              AmpCurrency curr=CurrencyUtil.
                  getAmpcurrency(
                      tm.getAppSettings()
                      .getCurrencyId());
              icd.setCurrency(curr);
 
          }

        return modeFinalize(mapping, form, request, response);
    }

    public ActionForward modeRemoveFields(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPAContractForm eaf = (IPAContractForm) form;
        Long[] idx=eaf.getSelContractDisbursements();
        List toRemove=new ArrayList();
        if(idx!=null&&idx.length>0){
            for(int i=0; i<idx.length;i++){
               IPAContractDisbursement contrDisb=eaf.getContractDisbursement(idx[i].intValue()-1);
               toRemove.add(contrDisb);
            }
            eaf.getContractDisbursements().removeAll(toRemove);
            eaf.setSelContractDisbursements(null);
        }

        return modeFinalize(mapping, form, request, response);
    }

    public ActionForward modeRemoveOrgs(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPAContractForm eaf = (IPAContractForm) form;
        Long[] idx=eaf.getSelOrgs();
        List toRemove=new ArrayList();
        if(idx!=null&&idx.length>0){
            for(int i=0; i<idx.length;i++){
               AmpOrganisation org = eaf.getOrganisation(idx[i].intValue() - 1);
               toRemove.add(org);
            }
            eaf.getOrganisations().removeAll(toRemove);
            eaf.setSelOrgs(null);
        }

        return modeFinalize(mapping, form, request, response);
    }

    public ActionForward modeSave(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // generate business objects:
        HttpSession httpSess = request.getSession();
        IPAContractForm euaf = (IPAContractForm) form;
        EditActivityForm eaf = (EditActivityForm) httpSess.getAttribute("eaf");
        IPAContract eua = null;

        eua = new IPAContract();
        if (euaf.getId() != null && euaf.getId() != 0) {
            eua.setId(euaf.getId());
        }
        eua.setContractName(euaf.getContractName());
        eua.setDescription(euaf.getDescription());
        eua.setContractingOrganizationText(euaf.getContractingOrganizationText());
        if (euaf.getActivityCategoryId() != null) {
            eua.setActivityCategory(CategoryManagerUtil.getAmpCategoryValueFromDb(euaf.getActivityCategoryId()));
        }
        if (euaf.getStatusId() != null) {
            eua.setStatus(CategoryManagerUtil.getAmpCategoryValueFromDb(euaf.getStatusId()));
        }
        if (euaf.getTypeId() != null) {
            eua.setType(CategoryManagerUtil.getAmpCategoryValueFromDb(euaf.getTypeId()));
        }
        if (euaf.getContractTypeId() != null) {
            eua.setContractType(CategoryManagerUtil.getAmpCategoryValueFromDb(euaf.getContractTypeId()));
        }
        if (euaf.getStartOfTendering() != null&&!euaf.getStartOfTendering().equals("")) {
            eua.setStartOfTendering(DateTimeUtil.parseDateForPicker(euaf.getStartOfTendering()));
        }
        if (euaf.getSignatureOfContract() != null&&!euaf.getSignatureOfContract().equals("")) {
            eua.setSignatureOfContract(DateTimeUtil.parseDateForPicker(euaf.getSignatureOfContract()));
        }
        if (euaf.getContractValidity() != null&&!euaf.getContractValidity().equals("")) {
            eua.setContractValidity(DateTimeUtil.parseDateForPicker(euaf.getContractValidity()));
        }
        if (euaf.getContractCompletion() != null&&!euaf.getContractCompletion().equals("")) {
            eua.setContractCompletion(DateTimeUtil.parseDateForPicker(euaf.getContractCompletion()));
        }
        
        if (euaf.getTotalPrivateContribAmountDate() != null && !euaf.getTotalPrivateContribAmountDate().equals("")){
        	eua.setTotalPrivateContribAmountDate(DateTimeUtil.parseDateForPicker(euaf.getTotalPrivateContribAmountDate()));
        }

        if (euaf.getTotalNationalContribRegionalAmountDate() != null && !euaf.getTotalNationalContribRegionalAmountDate().equals("")){
        	eua.setTotalNationalContribRegionalAmountDate(DateTimeUtil.parseDateForPicker(euaf.getTotalNationalContribRegionalAmountDate()));
        }

        if (euaf.getTotalNationalContribIFIAmountDate() != null && !euaf.getTotalNationalContribIFIAmountDate().equals("")){
        	eua.setTotalNationalContribIFIAmountDate(DateTimeUtil.parseDateForPicker(euaf.getTotalNationalContribIFIAmountDate()));
        }
        
        if (euaf.getTotalNationalContribCentralAmountDate() != null && !euaf.getTotalNationalContribCentralAmountDate().equals("")){
        	eua.setTotalNationalContribCentralAmountDate(DateTimeUtil.parseDateForPicker(euaf.getTotalNationalContribCentralAmountDate()));
        }
        
        if (euaf.getTotalECContribIBAmountDate() != null && !euaf.getTotalECContribIBAmountDate().equals("")){
        	eua.setTotalECContribIBAmountDate(DateTimeUtil.parseDateForPicker(euaf.getTotalECContribIBAmountDate()));
        }

        if (euaf.getTotalECContribINVAmountDate() != null && !euaf.getTotalECContribINVAmountDate().equals("")){
        	eua.setTotalECContribINVAmountDate(DateTimeUtil.parseDateForPicker(euaf.getTotalECContribINVAmountDate()));
        }
        
        if (euaf.getTotalECContribIBAmount() != null&&!euaf.getTotalECContribIBAmount().equals("")) {
            eua.setTotalECContribIBAmount(Double.parseDouble(euaf.getTotalECContribIBAmount()));
        }
        if (euaf.getTotalAmount() != null && !euaf.getTotalAmount().equals("")) {
            eua.setTotalAmount(Double.parseDouble(euaf.getTotalAmount()));
        }
        
        if (euaf.getContractTotalValue() != null && !euaf.getContractTotalValue().equals("")) {
            eua.setContractTotalValue(Double.parseDouble(euaf.getContractTotalValue()));
        }
        
        if (euaf.getTotalAmountCurrency() != null && !euaf.getTotalAmountCurrency().equals("") && !euaf.getTotalAmountCurrency().equals(new Long(-1))) {
        	eua.setTotalAmountCurrency(CurrencyUtil.getAmpcurrency(euaf.getTotalAmountCurrency()));
        }
        else eua.setTotalAmountCurrency(CurrencyUtil.getAmpcurrency(eaf.getCurrCode()));
        
        if (euaf.getDibusrsementsGlobalCurrency() != null&&!euaf.getDibusrsementsGlobalCurrency().equals("") && !euaf.getDibusrsementsGlobalCurrency().equals(new Long(-1))) {
        	eua.setDibusrsementsGlobalCurrency(CurrencyUtil.getAmpcurrency(euaf.getDibusrsementsGlobalCurrency()));
        }
        else
        	eua.setDibusrsementsGlobalCurrency(CurrencyUtil.getAmpcurrency(eaf.getCurrCode()));
        
        if (euaf.getTotalECContribINVAmount() != null&&!euaf.getTotalECContribINVAmount().equals("")) {

            eua.setTotalECContribINVAmount(Double.parseDouble(euaf.getTotalECContribINVAmount()));
        }
      
        if (euaf.getTotalNationalContribCentralAmount() != null&&!euaf.getTotalNationalContribCentralAmount().equals("")) {
            eua.setTotalNationalContribCentralAmount(Double.parseDouble(euaf.getTotalNationalContribCentralAmount()));
        }
        
        if (euaf.getTotalNationalContribRegionalAmount() != null&&!euaf.getTotalNationalContribRegionalAmount().equals("")) {

            eua.setTotalNationalContribRegionalAmount(Double.parseDouble(euaf.getTotalNationalContribRegionalAmount()));
        }
       
        if (euaf.getTotalNationalContribIFIAmount() != null&&!euaf.getTotalNationalContribIFIAmount().equals("")) {
            eua.setTotalNationalContribIFIAmount(Double.parseDouble(euaf.getTotalNationalContribIFIAmount()));
        }
        
        if (euaf.getTotalPrivateContribAmount() != null&&!euaf.getTotalPrivateContribAmount().equals("")) {
            eua.setTotalPrivateContribAmount(Double.parseDouble(euaf.getTotalPrivateContribAmount()));
        }
       
         if (eaf.getContracts() == null) {
                eaf.setContracts(new ArrayList());
        }
         
		
		if (euaf.getOrganisations() != null){
			Set<AmpOrganisation> orgs = new HashSet<AmpOrganisation>(euaf.getOrganisations());
			eua.setOrganizations(orgs);
		}
         
         
        if (euaf.getContractDisbursements() != null) {
            HashSet disbs = new HashSet(euaf.getContractDisbursements());
            eua.setDisbursements(disbs);
           
            Iterator<IPAContractDisbursement> disbIter = disbs.iterator();
            while (disbIter.hasNext()) {
                IPAContractDisbursement disb = disbIter.next();
                disb.setContract(eua);
            }
           
        }
         
         
        String cc=eaf.getCurrCode();
         if (eua.getDisbursements() != null) {
             ArrayList<IPAContractDisbursement> disbs = new ArrayList(eua.getDisbursements());
             
             //if there is no disbursement global currency saved in db we'll use the default from edit activity form
             
            if(eua.getDibusrsementsGlobalCurrency()!=null)
         	   cc=eua.getDibusrsementsGlobalCurrency().getCurrencyCode();
            double td=0;
            double usdAmount;  
    		   double finalAmount; 

    		   for(Iterator j=disbs.iterator();j.hasNext();)
       	  	{
       		  IPAContractDisbursement cd=(IPAContractDisbursement) j.next();
       		  // converting the amount to the currency from the top and adding to the final sum.
       		  if(cd.getAmount()!=null)
       			  {
       			  	usdAmount = CurrencyWorker.convertToUSD(cd.getAmount().doubleValue(),cd.getCurrCode());
       			  	finalAmount = CurrencyWorker.convertFromUSD(usdAmount,cc);
       			  	td+=finalAmount;
       			  }
       	  	}
       	  	
       	  	//eaf.getCurrCode();
    		   
       	  	eua.setTotalDisbursements(td);
       	  	
       	  		
         }
         
         if(eua.getTotalAmount()!=null)
    	  	{
//	           double usdAmount1=0;  
//	   		   double finalAmount1=0; 
//	          	try {
//	  				usdAmount1 = CurrencyWorker.convertToUSD(eua.getTotalAmount().doubleValue(),eua.getTotalAmountCurrency().getCurrencyCode());
//	  			} catch (AimException e) {
//	  				// TODO Auto-generated catch block
//	  				e.printStackTrace();
//	  			}
//	  			  	try {
//	  				finalAmount1 = CurrencyWorker.convertFromUSD(usdAmount1,cc);
//	  			} catch (AimException e) {
//	  				// TODO Auto-generated catch block
//	  				e.printStackTrace();
//	  			}	
//	  		 
//		  		 double amountRate=0;
//		  		 if(finalAmount1!=0){
//			  		 if (eua.getTotalDisbursements() != null)
//			  			 amountRate=eua.getTotalDisbursements().doubleValue()/finalAmount1;
//			  		 else
//			  			 amountRate=0;
//		  		 }
//		    	 eua.setExecutionRate(amountRate);
        	 eua.setExecutionRate(ActivityUtil.computeExecutionRateFromTotalAmount(eua, cc));
    	  	}
         else if(eua.getContractTotalValue()!=null){
//	        	   double usdAmount1=0;  
//		   		   double finalAmount1=0; 
//		          	try {
//		  				usdAmount1 = CurrencyWorker.convertToUSD(eua.getContractTotalValue().doubleValue(),eua.getTotalAmountCurrency().getCurrencyCode());
//		  			} catch (AimException e) {
//		  				// TODO Auto-generated catch block
//		  				e.printStackTrace();
//		  			}
//		  			  	try {
//		  				finalAmount1 = CurrencyWorker.convertFromUSD(usdAmount1,cc);
//		  			} catch (AimException e) {
//		  				// TODO Auto-generated catch block
//		  				e.printStackTrace();
//		  			}	
//		  		 
//			  		 double amountRate=0;
//			  		 if(finalAmount1!=0){
//				  		 if (eua.getTotalDisbursements() != null)
//				  			 amountRate=eua.getTotalDisbursements().doubleValue()/finalAmount1;
//				  		 else
//				  			 amountRate=0;
//			  		 }
			    	 //eua.setExecutionRate(amountRate);
        	 	eua.setExecutionRate(ActivityUtil.computeExecutionRateFromContractTotalValue(eua, cc));
         		}
    	  		else eua.setExecutionRate(new Double(0));
         
         if (eaf.getContracts() != null && euaf.getIndexId() != null && euaf.getIndexId() != -1) {
             eaf.getContracts().set(euaf.getIndexId(), eua);
         }
         else {
             eaf.getContracts().add(eua);
         	}
         
        request.setAttribute("close", "close");
        return null;//mapping.findForward("newforward");
    }
    }
