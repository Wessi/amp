/*
 * ShowAddComponent.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.AmpComponent;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

public class ShowAddComponent extends Action {

	private static Logger logger = Logger.getLogger(ShowAddComponent.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
			return null;
		}

	}

	public ActionForward switchType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		EditActivityForm eaForm = (EditActivityForm) form;
		ArrayList<AmpComponentType> ampComponentTypes = ampComponentTypes = new ArrayList<AmpComponentType>(ComponentsUtil.getAmpComponentTypes());
		eaForm.setAllCompsType(ampComponentTypes);

		ArrayList<org.digijava.module.aim.dbentity.AmpComponent> ampComponents = null;
		ampComponents = (ArrayList<org.digijava.module.aim.dbentity.AmpComponent>) ComponentsUtil.getAmpComponentsByType(eaForm.getSelectedType());

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

		eaForm.setAllComps(componentsList);		
		
		return mapping.findForward("forward");

	}

	public ActionForward showCompoenents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);

		EditActivityForm eaForm = (EditActivityForm) form;
		eaForm.setStep("5");
		
		ArrayList<org.digijava.module.aim.dbentity.AmpComponent> ampComponents = null;
		ArrayList<AmpComponentType> ampComponentTypes = null;
		ampComponentTypes = new ArrayList<AmpComponentType>(ComponentsUtil.getAmpComponentTypes());
		eaForm.setAllCompsType(ampComponentTypes);
		eaForm.setComponentId(new Long(-1));
		eaForm.setComponentTitle(null);
		eaForm.setComponentDesc(null);
		eaForm.setNewCompoenentName(null);
		//String defCurr = CurrencyUtil.getCurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
		String defCurr = DbUtil.getMemberAppSettings(tm.getMemberId()).getCurrency().getCurrencyCode();
		request.setAttribute("defCurrency", defCurr);
		
		if(!isComponentTypeEnabled()){
			AmpComponentType defaultComponentType = FeaturesUtil.getDefaultComponentType();
			eaForm.setSelectedType(defaultComponentType.getType_id());
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
		ArrayList<AmpComponentType> ampComponentTypes = null;
		ampComponentTypes = new ArrayList<AmpComponentType>(ComponentsUtil.getAmpComponentTypes());
		eaForm.setAllCompsType(ampComponentTypes);
	
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

		eaForm.setAllComps(componentsList);
		Iterator itr = eaForm.getSelectedComponents().iterator();
		String id = request.getParameter("fundId");
		long cId = Long.parseLong(id);
		
		while (itr.hasNext()) {
			Components comp = (Components) itr.next();
			if (comp.getComponentId().longValue() == cId) {
				eaForm.setComponentId(comp.getComponentId());
				eaForm.setComponentTitle(comp.getTitle());
				eaForm.setComponentDesc(comp.getDescription());
				eaForm.setSelectedType(comp.getType_Id());
				break;
			}
		}

		String defCurr = CurrencyUtil.getCurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode();
		request.setAttribute("defCurrency", defCurr);
		return mapping.findForward("forward");
	}

	public ActionForward addNewComponent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		EditActivityForm eaForm = (EditActivityForm) form;
		String name = eaForm.getNewCompoenentName();
		AmpComponentType type = ComponentsUtil.getComponentTypeById(eaForm.getSelectedType());
		org.digijava.module.aim.dbentity.AmpComponent newCompo = new org.digijava.module.aim.dbentity.AmpComponent();

		newCompo.setType(type);
		newCompo.setTitle(name);

		ComponentsUtil.addNewComponent(newCompo);
		eaForm.setComponentTitle(newCompo.getTitle());
		eaForm.setNewCompoenentName(null);
		return switchType(mapping, form, request, response);
	}

	@SuppressWarnings("unchecked")
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		try {

			List<org.digijava.module.aim.dbentity.AmpComponent> ampComponents = new ArrayList<org.digijava.module.aim.dbentity.AmpComponent>();
			eaForm.setStep("5");

			Components<FundingDetail> compFund = new Components();
			
			compFund.setType_Id(eaForm.getSelectedType());
			
			if (eaForm.getComponentId() == null || eaForm.getComponentId().longValue() == -1) {
				compFund.setComponentId(new Long(System.currentTimeMillis()));
			} else {
				compFund.setComponentId(eaForm.getComponentId());
			}
			compFund.setTitle(eaForm.getComponentTitle());
			compFund.setAmount(eaForm.getComponentAmount());
			compFund.setDescription(eaForm.getComponentDesc());
			compFund.setType_Id(eaForm.getSelectedType());
			
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
						val = request.getParameter(param);
						StringTokenizer st = new StringTokenizer(param, "_");
						st.nextToken();
						int index = Integer.parseInt(st.nextToken());
						int num = Integer.parseInt(st.nextToken());
	
						if (comm.containsKey(new Integer(index)) == false) {
							comm.put(new Integer(index), new FundingDetail());
						}
						FundingDetail fd = (FundingDetail) comm.get(new Integer(index));
	
						if (fd != null) {
							switch (num) {
							case 1:
								fd.setAdjustmentType(Integer.parseInt(val));
								if (fd.getAdjustmentType() == 1) {
									fd.setAdjustmentTypeName("Actual");
								} else if (fd.getAdjustmentType() == 0) {
									fd.setAdjustmentTypeName("Planned");
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
							comm.put(new Integer(index), fd);
						}
					} else if (param.startsWith("disb_")) {
						val = request.getParameter(param);
						StringTokenizer st = new StringTokenizer(param, "_");
						st.nextToken();
						int index = Integer.parseInt(st.nextToken());
						int num = Integer.parseInt(st.nextToken());
	
						if (disb.containsKey(new Integer(index)) == false) {
							disb.put(new Integer(index), new FundingDetail());
						}
	
						FundingDetail fd = (FundingDetail) disb.get(new Integer(index));
	
						if (fd != null) {
							switch (num) {
							case 1:
								fd.setAdjustmentType(Integer.parseInt(val));
								logger.debug("Adjustment type = " + fd.getAdjustmentType());
								if (fd.getAdjustmentType() == 1) {
									fd.setAdjustmentTypeName("Actual");
								} else if (fd.getAdjustmentType() == 0) {
									fd.setAdjustmentTypeName("Planned");
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
							disb.put(new Integer(index), fd);
						}
					} else if (param.startsWith("expn_")) {
						val = request.getParameter(param);
						StringTokenizer st = new StringTokenizer(param, "_");
						st.nextToken();
						int index = Integer.parseInt(st.nextToken());
						int num = Integer.parseInt(st.nextToken());
	
						if (exp.containsKey(new Integer(index)) == false) {
							exp.put(new Integer(index), new FundingDetail());
						}
	
						FundingDetail fd = (FundingDetail) exp.get(new Integer(index));
	
						if (fd != null) {
							switch (num) {
							case 1:
								fd.setAdjustmentType(Integer.parseInt(val));
								logger.debug("Adjustment type = " + fd.getAdjustmentType());
								if (fd.getAdjustmentType() == 1) {
									fd.setAdjustmentTypeName("Actual");
								} else if (fd.getAdjustmentType() == 0) {
									fd.setAdjustmentTypeName("Planned");
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
							exp.put(new Integer(index), fd);
						}
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

			if (eaForm.getSelectedComponents() == null) {
				eaForm.setSelectedComponents(new ArrayList());
			}
			if (eaForm.getSelectedComponents().contains(compFund)) {
				Iterator itr = eaForm.getSelectedComponents().iterator();
				while (itr.hasNext()) {
					Components comp = (Components) itr.next();
					if (compFund.equals(comp)) {
						compFund.setPhyProgress(comp.getPhyProgress());
					}
				}
				eaForm.getSelectedComponents().remove(compFund);
			}

			List list = null;
			if (compFund.getCommitments() != null) {
				list = new ArrayList(compFund.getCommitments());
				Collections.sort(list, FundingValidator.dateComp);
			}
			compFund.setCommitments(list);
			list = null;
			if (compFund.getDisbursements() != null) {
				list = new ArrayList(compFund.getDisbursements());
				Collections.sort(list, FundingValidator.dateComp);
			}
			compFund.setDisbursements(list);
			list = null;
			if (compFund.getExpenditures() != null) {
				list = new ArrayList(compFund.getExpenditures());
				Collections.sort(list, FundingValidator.dateComp);
			}
			compFund.setExpenditures(list);

			eaForm.getSelectedComponents().add(compFund);
			Double totdisbur = 0d;
			for (Iterator iterator = eaForm.getSelectedComponents().iterator(); iterator.hasNext();) {
				Components object = (Components) iterator.next();
				if (object.getDisbursements() != null) {
					for (Iterator iterator2 = object.getDisbursements().iterator(); iterator2.hasNext();) {
						FundingDetail disdeatils = (FundingDetail) iterator2.next();
						totdisbur = totdisbur + FormatHelper.parseDouble(disdeatils.getTransactionAmount());
					}
				}
			}
			eaForm.setCompTotalDisb(totdisbur);

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return mapping.findForward("updated");
	}

}