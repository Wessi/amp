/*
 * ShowAddComponent.java
 */

package org.digijava.module.aim.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.AmpComponent;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ShowAddComponent extends Action {

	private static Logger logger = Logger.getLogger(ShowAddComponent.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  throws Exception{
		try {
			
			HttpSession session = request.getSession();
			TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);

			String defCurr = DbUtil.getMemberAppSettings(tm.getMemberId()).getCurrency().getCurrencyCode();
			request.setAttribute("defCurrency", defCurr);
			
			String event = (request.getParameter("compFundAct") != null) ? request.getParameter("compFundAct") : "";

			if ("switchType".equalsIgnoreCase(event)) {
				return switchType(mapping, form, request, response);
			} else if ("save".equalsIgnoreCase(event)) {
				return save(mapping, form, request, response);
			} else if ("addCompoenents".equalsIgnoreCase(event)) {
				return addNewComponent(mapping, form, request, response);
			} else if ("addNewComponent".equalsIgnoreCase(event)) {
				return addNewComponent(mapping, form, request, response);
			} else if ("showEdit".equalsIgnoreCase(event)) {
				return showEdit(mapping, form, request, response);
			}
         
			// default action
			return showCompoenents(mapping, form, request, response);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}

	}

	public ActionForward switchType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		Collection<AmpCategoryValue> componentstype = null;
		try {
			componentstype = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.COMPONET_TYPE_KEY, null, request);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		eaForm.getComponents().setAllCompsType(componentstype);

		ArrayList<org.digijava.module.aim.dbentity.AmpComponent> ampComponents = null;
		ampComponents = (ArrayList<org.digijava.module.aim.dbentity.AmpComponent>) ComponentsUtil.getAmpComponentsByType(eaForm.getComponents().getSelectedType());

		List<AmpComponent> componentsList = new ArrayList<AmpComponent>();

		if (ampComponents != null) {
			Iterator<org.digijava.module.aim.dbentity.AmpComponent> iter = ampComponents.iterator();
			while (iter.hasNext()) {
				org.digijava.module.aim.dbentity.AmpComponent comp = iter.next();
				AmpComponent helperComponent = new AmpComponent();
				helperComponent.setAmpComponentId(comp.getAmpComponentId());
				helperComponent.setName(comp.getTitle());
				if(comp.getTitle() != null)
					helperComponent.setShortName(comp.getTitle().length() > 60 ? comp.getTitle().substring(0, 60) : comp.getTitle());
				componentsList.add(helperComponent);
			}
		}

		eaForm.getComponents().setAllComps(componentsList);		
		
		return mapping.findForward("forward");

	}

	public ActionForward showCompoenents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);

		EditActivityForm eaForm = (EditActivityForm) form;
		eaForm.setStep("5");
		
		Collection<AmpCategoryValue> componentstype = null;
		try {
			componentstype = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.COMPONET_TYPE_KEY, null, request);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		eaForm.getComponents().setAllCompsType(componentstype);
		/*if(eaForm.getComponents().getComponentId()==null) {
			eaForm.getComponents().setComponentId(new Long(-1));
		}*/
		eaForm.getComponents().setComponentId(null);
		eaForm.getComponents().setComponentTitle(null);
		eaForm.getComponents().setComponentDesc(null);
		eaForm.getComponents().setNewCompoenentName(null);
		String defCurr = DbUtil.getMemberAppSettings(tm.getMemberId()).getCurrency().getCurrencyCode();
		request.setAttribute("defCurrency", defCurr);
        if(eaForm.getComponents().getFundingCurrCode()==null){
            eaForm.getComponents().setFundingCurrCode(defCurr);
        }
        setFundingTotals(eaForm,session);

		if(!isComponentTypeEnabled()){
			AmpCategoryValue defaultComponentType = FeaturesUtil.getDefaultComponentType();
			eaForm.getComponents().setSelectedType(defaultComponentType.getId());
			return switchType(mapping, form, request, response);
		}

		return mapping.findForward("forward");

	}
	
	public boolean isComponentTypeEnabled(){
 	   ServletContext ampContext = getServlet().getServletContext();
 	   
	   AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
 	   
		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) ampTreeVisibility.getRoot();
		if(currentTemplate!=null)
			if(currentTemplate.getFeatures()!=null)
				for(Iterator it=currentTemplate.getFeatures().iterator();it.hasNext();)
				{
					AmpFeaturesVisibility feature=(AmpFeaturesVisibility) it.next();
					if(feature.getName().compareTo("Admin - Component Type")==0) 
					{
						return true;
					}	
					
				}
		return false;

	}

	public ActionForward showEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);


		EditActivityForm eaForm = (EditActivityForm) form;
		List<org.digijava.module.aim.dbentity.AmpComponent> ampComponents = new ArrayList<org.digijava.module.aim.dbentity.AmpComponent>();
		eaForm.setStep("5");
		//ArrayList<AmpComponentType> ampComponentTypes = null;
		//ampComponentTypes = new ArrayList<AmpComponentType>(ComponentsUtil.getAmpComponentTypes());
		
		Collection<AmpCategoryValue> componentstype = null;
		try {
			componentstype = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.COMPONET_TYPE_KEY, null, request);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		eaForm.getComponents().setAllCompsType(componentstype);
	
		List<AmpComponent> componentsList = new ArrayList<AmpComponent>();
		ampComponents = (ArrayList<org.digijava.module.aim.dbentity.AmpComponent>) ComponentsUtil.getAmpComponents();
		if (ampComponents != null) {
			Iterator<org.digijava.module.aim.dbentity.AmpComponent> iter = ampComponents.iterator();
			while (iter.hasNext()) {
				org.digijava.module.aim.dbentity.AmpComponent comp = iter.next();
				AmpComponent helperComponent = new AmpComponent();
				helperComponent.setAmpComponentId(comp.getAmpComponentId());
				helperComponent.setName(comp.getTitle());
				if(comp.getTitle() != null)
					helperComponent.setShortName(comp.getTitle().length() > 60 ? comp.getTitle().substring(0, 60) : comp.getTitle());
				componentsList.add(helperComponent);
			}
		}

		eaForm.getComponents().setAllComps(componentsList);
		Iterator itr = eaForm.getComponents().getSelectedComponents().iterator();
		String id = request.getParameter("fundId");
		long cId = Long.parseLong(id);
		
		while (itr.hasNext()) {
			Components comp = (Components) itr.next();
			if (comp.getComponentId().longValue() == cId) {
				eaForm.getComponents().setComponentId(comp.getComponentId());
				eaForm.getComponents().setComponentTitle(comp.getTitle());
				eaForm.getComponents().setComponentDesc(comp.getDescription());
				eaForm.getComponents().setSelectedType(comp.getType_Id());
				break;
			}
		}
     
		String defCurr = CurrencyUtil.getCurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
		request.setAttribute("defCurrency", defCurr);
       if(eaForm.getComponents().getFundingCurrCode()==null){
            eaForm.getComponents().setFundingCurrCode(defCurr);
        }
        setFundingTotals(eaForm, session);
		return mapping.findForward("forward");
	}

	public ActionForward addNewComponent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  throws Exception {
		EditActivityForm eaForm = (EditActivityForm) form;
		String name = eaForm.getComponents().getNewCompoenentName();
		AmpCategoryValue type = ComponentsUtil.getComponentTypeById(eaForm.getComponents().getSelectedType());
		org.digijava.module.aim.dbentity.AmpComponent newCompo = new org.digijava.module.aim.dbentity.AmpComponent();

		newCompo.setType(type);
		newCompo.setTitle(name);
		newCompo.setAmpComponentId(new Long(System.currentTimeMillis() * -1));

		//ComponentsUtil.addNewComponent(newCompo);
		eaForm.getComponents().setComponentId(newCompo.getAmpComponentId());
		eaForm.getComponents().setComponentTitle(newCompo.getTitle());
		eaForm.getComponents().setNewCompoenentName(null);
		return switchType(mapping, form, request, response);
	}

	@SuppressWarnings("unchecked")
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		try {

			List<org.digijava.module.aim.dbentity.AmpComponent> ampComponents = new ArrayList<org.digijava.module.aim.dbentity.AmpComponent>();
			eaForm.setStep("5");
			
			String name = eaForm.getComponents().getNewCompoenentName();
			AmpCategoryValue type = ComponentsUtil.getComponentTypeById(eaForm.getComponents().getSelectedType());
			org.digijava.module.aim.dbentity.AmpComponent ampComp = new org.digijava.module.aim.dbentity.AmpComponent();
			ampComp.setTitle( name );
			ampComp.setType( type );
			
			ampComp.setAmpComponentId(eaForm.getComponents().getComponentId());
			/*String id = request.getParameter("fundId");
			long cId = Long.parseLong(id);
			if (cId > 0) {
				ampComp.setActivity(ActivityUtil.getAmpActivity(eaForm.getActivityId()));
			}*/
			//ComponentsUtil.updateComponents(ampComp);
			
			Components<FundingDetail> compFund = new Components<FundingDetail>();
			compFund.setType_Id(eaForm.getComponents().getSelectedType());
			compFund.setComponentId(eaForm.getComponents().getComponentId());
				
			compFund.setTitle(eaForm.getComponents().getComponentTitle());
			compFund.setAmount(eaForm.getComponents().getComponentAmount());
			compFund.setDescription(eaForm.getComponents().getComponentDesc());
			compFund.setType_Id(eaForm.getComponents().getSelectedType());
			
			
			Enumeration<Object> paramNames = request.getParameterNames();
			String param = "";
			String val = "";
			Map<Integer, FundingDetail> comm = new HashMap<Integer, FundingDetail>();
			Map<Integer, FundingDetail> disb = new HashMap<Integer, FundingDetail>();
			Map<Integer, FundingDetail> exp = new HashMap<Integer, FundingDetail>();

			while (paramNames.hasMoreElements()) {
				param = (String) paramNames.nextElement();
				if(param.length()>=7 && !param.substring(5, 7).equals("@@")){
					if (param.startsWith("comm_")) {					
						comm=fillFundingDetailsMap(request, param, comm);
					} else if (param.startsWith("disb_")) {
						disb=fillFundingDetailsMap(request, param, disb);						
					} else if (param.startsWith("expn_")) {
						exp=fillFundingDetailsMap(request, param, exp);
					}
				}
			}

			Iterator itrS = comm.keySet().iterator();
			while (itrS.hasNext()) {
				Integer index = (Integer) itrS.next();
				FundingDetail fd = (FundingDetail) comm.get(index);
				if (compFund.getCommitments() == null) {
					compFund.setCommitments(new ArrayList());
				}
				compFund.getCommitments().add(fd);
			}

			itrS = disb.keySet().iterator();
			while (itrS.hasNext()) {
				Integer index = (Integer) itrS.next();
				FundingDetail fd = (FundingDetail) disb.get(index);
				if (compFund.getDisbursements() == null) {
					compFund.setDisbursements(new ArrayList());
				}
				compFund.getDisbursements().add(fd);
			}

			itrS = exp.keySet().iterator();
			while (itrS.hasNext()) {
				Integer index = (Integer) itrS.next();
				FundingDetail fd = (FundingDetail) exp.get(index);
				if (compFund.getExpenditures() == null) {
					compFund.setExpenditures(new ArrayList());
				}
				compFund.getExpenditures().add(fd);
			}

			if (eaForm.getComponents().getSelectedComponents() == null) {
				eaForm.getComponents().setSelectedComponents(new ArrayList());
			}
			if (eaForm.getComponents().getSelectedComponents().contains(compFund)) {
				Iterator itr = eaForm.getComponents().getSelectedComponents().iterator();
				while (itr.hasNext()) {
					Components comp = (Components) itr.next();
					if (compFund.equals(comp)) {
						compFund.setPhyProgress(comp.getPhyProgress());
					}
				}
				eaForm.getComponents().getSelectedComponents().remove(compFund);
			}
			Set fundingset = new HashSet();
			List list = null;
			if (compFund.getCommitments() != null) {
				list = new ArrayList(compFund.getCommitments());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					FundingDetail funding = (FundingDetail) iterator.next();
					fundingset.add(createComponentFunding(ampComp,funding,Constants.COMMITMENT));
				}
				Collections.sort(list, FundingValidator.dateComp);
			}
			compFund.setCommitments(list);
			list = null;
			if (compFund.getDisbursements() != null) {
				list = new ArrayList(compFund.getDisbursements());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					FundingDetail funding = (FundingDetail) iterator.next();
					fundingset.add(createComponentFunding(ampComp,funding,Constants.DISBURSEMENT));
				}
				Collections.sort(list, FundingValidator.dateComp);
			}
			compFund.setDisbursements(list);
			list = null;
			if (compFund.getExpenditures() != null) {
				list = new ArrayList(compFund.getExpenditures());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					FundingDetail funding = (FundingDetail) iterator.next();
					fundingset.add(createComponentFunding(ampComp,funding,Constants.EXPENDITURE));
				}
				Collections.sort(list, FundingValidator.dateComp);
			}
			compFund.setExpenditures(list);

			eaForm.getComponents().getSelectedComponents().add(compFund);
			Double totdisbur = 0d;
			for (Iterator iterator = eaForm.getComponents().getSelectedComponents().iterator(); iterator.hasNext();) {
				Components object = (Components) iterator.next();
				if (object.getDisbursements() != null) {
					for (Iterator iterator2 = object.getDisbursements().iterator(); iterator2.hasNext();) {
						FundingDetail disdeatils = (FundingDetail) iterator2.next();
						totdisbur = totdisbur + FormatHelper.parseDouble(disdeatils.getTransactionAmount());
					}
				}
			}
			
			ampComp.setFunding(fundingset);
			eaForm.getComponents().setCompTotalDisb(totdisbur);
			
			if (eaForm.getComponents().getCompotosave()==null){
				eaForm.getComponents().setCompotosave(new ArrayList<org.digijava.module.aim.dbentity.AmpComponent>());
			}
			// Add the AmpComponent or replace it in collection compotosave.
			boolean add = true;
			Iterator<org.digijava.module.aim.dbentity.AmpComponent> iCompotosave = eaForm.getComponents().getCompotosave().iterator();
			while(iCompotosave.hasNext()) {
				org.digijava.module.aim.dbentity.AmpComponent auxComponent = iCompotosave.next();
				// Case 1: I'm editing an existing (previously saved) component. 
				if(ampComp.getAmpComponentId().equals(auxComponent.getAmpComponentId())) {
					add = false;
					eaForm.getComponents().getCompotosave().remove(auxComponent);
					eaForm.getComponents().getCompotosave().add(ampComp);
					break;
					
				} else if(auxComponent.getTitle() != null && auxComponent.getTitle().equals(ampComp.getTitle())) {
					// Case 2: I'm editing a new component not yet saved.
					//TODO: if title changes then is considered a different component, this should be changed.
					add = false;
				}
			}
			if(add) {
				eaForm.getComponents().getCompotosave().add(ampComp);
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return mapping.findForward("updated");
	}
	
	/**
	 * fills commitments,disbursements or expenditures map with correct data
	 * @param request
	 * @param param
	 * @param mapToFill
	 * @return
	 * @author dare
	 */
	private Map<Integer, FundingDetail> fillFundingDetailsMap(HttpServletRequest request,String param, Map<Integer, FundingDetail> mapToFill) {
		String val;
		val = request.getParameter(param);
		StringTokenizer st = new StringTokenizer(param, "_");
		st.nextToken();
		int index = Integer.parseInt(st.nextToken());
		int num = Integer.parseInt(st.nextToken());

		if (mapToFill.containsKey(new Integer(index)) == false) {
			mapToFill.put(new Integer(index), new FundingDetail());
		}
		FundingDetail fd = (FundingDetail) mapToFill.get(new Integer(index));

		if (fd != null) {
			switch (num) {
			case 1:
				fd.setAdjustmentType(Integer.parseInt(val));
				if (fd.getAdjustmentType() == 1) {
					fd.setAdjustmentTypeName("Actual");
				} else if (fd.getAdjustmentType() == 0) {
					fd.setAdjustmentTypeName("Planned");
				} else if (fd.getAdjustmentType() == 2) {
                    fd.setAdjustmentTypeName("Pipeline");
                }
				break;
			case 2:
				fd.setTransactionAmount(CurrencyWorker.formatAmount(val));
				break;
			case 3:
				fd.setCurrencyCode(val);
				break;
			case 4:
				fd.setTransactionDate(val);
				break;
			case 6:
				if (!"".equals(val)) {
					fd.setAmpComponentFundingId(Long.valueOf(val));
				}
			}
			mapToFill.put(new Integer(index), fd);
		}
		return mapToFill;
	}

    public void setFundingTotals(EditActivityForm eaForm, HttpSession session){
        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        Collection fundDets = eaForm.getFunding().getFundingDetails();
        if (fundDets != null) {
            Collection<AmpFundingDetail> ampFundDets = ActivityUtil.createAmpFundingDetails(fundDets);
            cal.doCalculations(ampFundDets, eaForm.getComponents().getFundingCurrCode());
            session.setAttribute("totalComm", cal.getTotActualComm());
            session.setAttribute("totalDisb", cal.getTotActualDisb());
            session.setAttribute("totalExpn", cal.getTotActualExp());
        }
         else {
            session.setAttribute("totalComm", 0);
            session.setAttribute("totalDisb", 0);
            session.setAttribute("totalExpn", 0);

        }
    }
    
    /**
     * @param ampComp
     * @param funding
     * @param transactionType
     * @return
     * @author dare
     */
    private AmpComponentFunding createComponentFunding(org.digijava.module.aim.dbentity.AmpComponent ampComp, FundingDetail funding, int transactionType){    	
    	AmpComponentFunding compofunding = new AmpComponentFunding();
		compofunding.setTransactionAmount(FormatHelper.parseBigDecimal(funding.getTransactionAmount()));
		compofunding.setTransactionType(transactionType);
		compofunding.setAdjustmentType(funding.getAdjustmentType());
		//compofunding.setTransactionDate(new Date(funding.getTransactionDate()));
		compofunding.setTransactionDate(DateConversion.getDate(funding.getTransactionDate()));
		compofunding.setComponent(ampComp);
		if (funding.getFormattedRate()!=null){
			compofunding.setExchangeRate(new Float(funding.getFormattedRate()));
		}
		AmpCurrency currency = CurrencyUtil.getCurrencyByCode(funding.getCurrencyCode());
		compofunding.setCurrency(currency);
    	return compofunding;
    }
   
}