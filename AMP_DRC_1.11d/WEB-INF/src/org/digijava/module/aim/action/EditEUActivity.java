/**
 *
 */
package org.digijava.module.aim.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Session;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.EUActivityContribution;
import org.digijava.module.aim.form.EUActivityForm;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author mihai
 *
 */
public class EditEUActivity extends MultiAction {
	/**
	 *
	 */
	public EditEUActivity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean hasUnselectedItems(List items) {
		Iterator i=items.iterator();
		while (i.hasNext()) {
			String element = (String) i.next();
			if("-1".equals(element)) return true;
		}
		return false;
	}


	public boolean hasInvalidAmounts(List items) {
		Iterator i=items.iterator();
		while (i.hasNext()) {
				String element = (String) i.next();
				try {
					Double.parseDouble(element);
			} catch (NumberFormatException e) {
			 return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EUActivityForm eaf = (EUActivityForm) form;
		eaf.setCurrencies(CurrencyUtil.getAmpCurrency());
		//eaf.setDonors(DbUtil.getAllOrganisation());
		eaf.setFinTypes(DbUtil.getAllAssistanceTypesFromCM());
		eaf.setFinInstrs(DbUtil.getAllFinancingInstruments());

		eaf.getContrAmountList().clear();
		eaf.getContrCurrIdList().clear();
		eaf.getContrDonorIdList().clear();
		eaf.getContrDonorNameList().clear();
		eaf.getContrFinInstrIdList().clear();
		eaf.getContrFinTypeIdList().clear();

		if(eaf.getContrAmount()!=null) {
			eaf.getContrAmountList().addAll(Arrays.asList(eaf.getContrAmount()));
			eaf.getContrCurrIdList().addAll(Arrays.asList(eaf.getContrCurrId()));
			eaf.getContrDonorIdList().addAll(Arrays.asList(eaf.getContrDonorId()));
			eaf.getContrDonorNameList().addAll(Arrays.asList(eaf.getContrDonorName()));
			
			eaf.getContrFinInstrIdList().addAll(Arrays.asList(eaf.getContrFinInstrId()));
			eaf.getContrFinTypeIdList().addAll(Arrays.asList(eaf.getContrFinTypeId()));
		}


		return modeSelect(mapping, form, request, response);
	}


	public ActionForward modeDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session=request.getSession();
		EditActivityForm eaf=(EditActivityForm) session.getAttribute("eaf");
		Integer IndexId=new Integer(request.getParameter("indexId"));
		eaf.getCosts().remove(IndexId.intValue());

		request.setAttribute("close", "close");
		return modeFinalize(mapping, form, request, response);
	}


	public ActionForward modeEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session=request.getSession();

		EditActivityForm eaf=(EditActivityForm) session.getAttribute("eaf");
		EUActivityForm euaf = (EUActivityForm) form;

		Integer IndexId=new Integer(request.getParameter("indexId"));
		euaf.setEditingIndexId(IndexId);

		EUActivity element=(EUActivity) eaf.getCosts().get(IndexId.intValue());
				euaf.setId(element.getId());
				euaf.setAssumptions(element.getAssumptions());
				euaf.setInputs(element.getInputs());
				euaf.setName(element.getName());
				euaf.setProgress(element.getProgress());
				euaf.setTextId(element.getTextId());
				euaf.setTotalCost(element.getTotalCost().toString());
				euaf.setTotalCostCurrencyId(element.getTotalCostCurrency().getAmpCurrencyId());
				euaf.setDueDate(DateTimeUtil.parseDateForPicker2(element.getDueDate()));

				euaf.getContrAmountList().clear();
				euaf.getContrCurrIdList().clear();
				euaf.getContrDonorIdList().clear();
				euaf.getContrDonorNameList().clear();

				euaf.getContrFinInstrIdList().clear();
				euaf.getContrFinTypeIdList().clear();
				
				Iterator ii=element.getContributions().iterator();
				while (ii.hasNext()) {
					EUActivityContribution element2 = (EUActivityContribution) ii.next();
					euaf.getContrAmountList().add(element2.getAmount().toString());
					euaf.getContrCurrIdList().add(element2.getAmountCurrency().getAmpCurrencyId().toString());
					euaf.getContrDonorIdList().add(element2.getDonor().getAmpOrgId().toString());
					euaf.getContrDonorNameList().add(element2.getDonor().getName());
					euaf.getContrFinInstrIdList().add(element2.getFinancingInstr().getId().toString());
					euaf.getContrFinTypeIdList().add(element2.getFinancingTypeCategVal().getId().toString());
				}
		return modeFinalize(mapping,form,request,response);
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
		if (request.getParameter("editEU") != null)
			return modeEdit(mapping, form, request, response);
		if (request.getParameter("new") != null)
			return modeNew(mapping, form, request, response);
		if (request.getParameter("save") != null)
			return modeValidateSave(mapping, form, request, response);
		if (request.getParameter("addFields") != null)
			return modeAddFields(mapping, form, request, response);
		if (request.getParameter("removeFields") != null)
			return modeRemoveFields(mapping, form, request, response);
		if (request.getParameter("deleteEU") != null)
			return modeDelete(mapping, form, request, response);
		return modeFinalize(mapping, form, request, response);
	}

	public ActionForward modeNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EUActivityForm eaf = (EUActivityForm) form;
		HttpSession session=request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		Long currencyId = tm.getAppSettings().getCurrencyId();
		eaf.clear();
		eaf.setTotalCostCurrencyId(currencyId);
		eaf.getContrCurrIdList().set(0,currencyId);
		eaf.getContrDonorIdList().set(0,new Long(-1));
		eaf.getContrDonorNameList().set(0,"");
         
		
		//System.out.println("DueDate:"+eaf.getDueDate());
		return modeFinalize(mapping, form, request, response);
	}

	public ActionForward modeValidateSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
                      HttpSession session=request.getSession();
		ActionErrors errors = new ActionErrors();
                TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		Long currencyId = tm.getAppSettings().getCurrencyId();
                String defaultCurCode="";
                String defaultCurName="";
                if(currencyId!=null){
                  AmpCurrency defcurr=CurrencyUtil.getAmpcurrency(currencyId);
                  defaultCurCode=defcurr.getCurrencyCode();
                  defaultCurName=defcurr.getCurrencyName();
                }
		EUActivityForm eaf = (EUActivityForm) form;
                Long totalCostCurrId=eaf.getTotalCostCurrencyId();
                AmpCurrency  totalCostCurr=CurrencyUtil.getAmpcurrency(totalCostCurrId);
                String totalCurCode = totalCostCurr.getCurrencyCode();
                double totalCostExRate = CurrencyUtil.getExchangeRate(
                          totalCurCode);
                if (totalCostExRate == 1.0 && !totalCurCode.equals("USD")) {
                  errors.add("title", new ActionError(
                      "error.aim.addActivity.noExchangeRateIsDefined",
                      totalCostCurr.getCurrencyName(), defaultCurName));
                }
                   else{
                     Object[] currencies = eaf.getContrCurrId();
                     if (currencies != null) {
                       for (int i = 0; i < currencies.length; i++) {
                         Long id = Long.parseLong( (String) currencies[i]);
                         AmpCurrency curr = CurrencyUtil.getAmpcurrency(id);
                         String currCode = curr.getCurrencyCode();
                         double exchangeRate = CurrencyUtil.getExchangeRate(
                             currCode);
                         if (exchangeRate == 1.0 &&!currCode.equals("USD")) {
                           errors.add("title", new ActionError(
                               "error.aim.addActivity.noExchangeRateIsDefined",
                               curr.getCurrencyName(), defaultCurName));
                           break;
                         }

                       }
                     }
                   }
		try {
			Double.parseDouble(eaf.getTotalCost());
		} catch (NumberFormatException e) {
			errors.add("title", new ActionError(
					"error.aim.euactivity.invalidAmountFormat"));
		}

		try {
			//System.out.println("DueDate:"+eaf.getDueDate());
			DateTimeUtil.parseDateForPicker(eaf.getDueDate());
			
		} catch (ParseException e) {
			//System.out.println("Exception:"+e);
			errors.add("title", new ActionError(
					"error.aim.euactivity.dueDate"));
		}

		if(hasInvalidAmounts(eaf.getContrAmountList())) errors.add("title", new ActionError(
		"error.aim.euactivity.invalidAmountFormat"));
		if(hasUnselectedItems(eaf.getContrDonorIdList())) errors.add("title", new ActionError(
		"error.aim.euactivity.selectDonor"));
		if(hasUnselectedItems(eaf.getContrCurrIdList())) errors.add("title", new ActionError(
		"error.aim.euactivity.selectCurrency"));
		if(hasUnselectedItems(eaf.getContrFinInstrIdList())) errors.add("title", new ActionError(
		"error.aim.euactivity.contrFinInstr"));
		if(hasUnselectedItems(eaf.getContrFinTypeIdList())) errors.add("title", new ActionError(
		"error.aim.euactivity.contrFinType"));



		saveErrors(request, errors);

		if(!errors.isEmpty()) return modeFinalize(mapping, form, request, response);

		return modeSave(mapping, form, request, response);

	}


	public ActionForward modeFinalize(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EUActivityForm eaf = (EUActivityForm) form;
		eaf.setContrAmount(eaf.getContrAmountList().toArray());
		eaf.setContrCurrId(eaf.getContrCurrIdList().toArray());
		eaf.setContrDonorId(eaf.getContrDonorIdList().toArray());
		eaf.setContrDonorName(eaf.getContrDonorNameList().toArray());
		eaf.setContrFinInstrId(eaf.getContrFinInstrIdList().toArray());
		eaf.setContrFinTypeId(eaf.getContrFinTypeIdList().toArray());
		return mapping.findForward("forward");
	}

	public ActionForward modeAddFields(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EUActivityForm eaf = (EUActivityForm) form;
		HttpSession session=request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		Long currencyId = tm.getAppSettings().getCurrencyId();
		eaf.getContrAmountList().add(new String(""));
		eaf.getContrCurrIdList().add(currencyId);
		eaf.getContrDonorNameList().add("");
		eaf.getContrDonorIdList().add(new Long(-1));
		eaf.getContrFinInstrIdList().add(new String("-1"));
		eaf.getContrFinTypeIdList().add(new String("-1"));

		return modeFinalize(mapping, form, 			request, response);
	}

	public ActionForward modeRemoveFields(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EUActivityForm eaf = (EUActivityForm) form;
		for(int i=0;i<eaf.getDeleteContrib().length;i++) {
			eaf.getContrAmountList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);
			eaf.getContrCurrIdList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);
			eaf.getContrDonorIdList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);
			eaf.getContrDonorNameList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);
			eaf.getContrFinInstrIdList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);
			eaf.getContrFinTypeIdList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);
			
			//eaf.getContrDonorNameList().set(Long.parseLong(eaf.getDeleteContrib().), element)
		}

		for(int i=0;i<eaf.getContrAmountList().size();i++) {
			if(eaf.getContrAmountList().get(i)==null) {
				eaf.getContrAmountList().remove(i);
				eaf.getContrCurrIdList().remove(i);
				eaf.getContrDonorIdList().remove(i);
				eaf.getContrFinInstrIdList().remove(i);
				eaf.getContrFinTypeIdList().remove(i);
				eaf.getContrDonorNameList().remove(i);
			}
		}


		return modeFinalize(mapping, form, request, response);
	}


	public ActionForward modeSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// generate business objects:
		HttpSession httpSess=request.getSession();
		Session sess = PersistenceManager.getSession();
		EUActivityForm euaf = (EUActivityForm) form;
		EditActivityForm eaf=(EditActivityForm) httpSess.getAttribute("eaf");
		EUActivity eua =null ;

		eua=new EUActivity();
		if(euaf.getEditingIndexId()!=null)
			eaf.getCosts().set(euaf.getEditingIndexId().intValue(),eua);



		eua.setAssumptions(euaf.getAssumptions());
		eua.setDueDate(DateTimeUtil.parseDateForPicker(euaf.getDueDate()));
		eua.setInputs(euaf.getInputs());
		eua.setName(euaf.getName());
		eua.setProgress(euaf.getProgress());
		eua.setTextId(euaf.getTextId());
		eua.setTotalCost(new Double(euaf.getTotalCost()));
		eua.setTotalCostCurrency((AmpCurrency) sess.load(AmpCurrency.class, euaf
				.getTotalCostCurrencyId()));
		eua.setTransactionDate(new Date(System.currentTimeMillis()));

		// create the contribution objects:
		eua.getContributions().clear();
		for (int i = 0; i < euaf.getContrAmountList().size(); i++) {
			Long financingInstrumentId	= new Long ( (String)euaf.getContrFinInstrIdList().get(i) );
			Long typeOfAssistanceId		= new Long( (String) euaf.getContrFinTypeIdList().get(i) );

			EUActivityContribution eac=new EUActivityContribution();
			eac.setEuActivity(eua);
			eac.setAmount(new Double((String) euaf.getContrAmountList().get(i)));
			eac.setAmountCurrency((AmpCurrency) sess.load(AmpCurrency.class,new Long((String) euaf.getContrCurrIdList().get(i))));
			eac.setDonor((AmpOrganisation) sess.load(AmpOrganisation.class,new Long((String) euaf.getContrDonorIdList().get(i))));
			//eac.setFinancingInstrument((AmpModality) sess.load(AmpModality.class,new Long((String) euaf.getContrFinInstrIdList().get(i))));
			eac.setFinancingInstr( CategoryManagerUtil.getAmpCategoryValueFromDb(financingInstrumentId) );
			eac.setFinancingTypeCategVal( CategoryManagerUtil.getAmpCategoryValueFromDb(typeOfAssistanceId) );
			eac.setTransactionDate(new Date(System.currentTimeMillis()));

			eua.getContributions().add(eac);
		}


		if(eaf.getCosts()==null) eaf.setCosts(new ArrayList());
		if(euaf.getEditingIndexId()==null) eaf.getCosts().add(eua);

		PersistenceManager.releaseSession(sess);
		request.setAttribute("close", "close");
		return modeFinalize(mapping, form, request, response);
	}

}
