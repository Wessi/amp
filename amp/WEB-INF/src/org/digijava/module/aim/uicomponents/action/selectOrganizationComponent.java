package org.digijava.module.aim.uicomponents.action;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.uicomponents.AddOrganizationButton;
import org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm;
import org.digijava.module.aim.util.DbUtil;

public class selectOrganizationComponent extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String subAction = (request.getParameter("subAction") != null) ? request.getParameter("subAction") : "";
		selectOrganizationComponentForm oForm = (selectOrganizationComponentForm) form;
		// call the requested action
		if ("search".equalsIgnoreCase(subAction)) {
			return search(mapping, form, request, response);
		} else if ("selectPage".equalsIgnoreCase(subAction)) {
			return selectPage(mapping, form, request, response);
		} else if ("organizationSelected".equalsIgnoreCase(subAction)) {
			// if have to add to a collection so this is a multiselector
			if (oForm.getMultiSelect()) {
				return addOrganizationToForm(mapping, form, request, response);
			} else {
				return setOrganizationToForm(mapping, form, request, response);
			}
		}

		return prepare(mapping, form, request, response);

	}

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		selectOrganizationComponentForm oForm = (selectOrganizationComponentForm) form;
		HttpSession session = request.getSession();
		ActionForm targetForm = (ActionForm) session.getAttribute(AddOrganizationButton.PARAM_PARAM_FORM_NAME);

		if ("true".equalsIgnoreCase(request.getParameter("reset"))) {
			oForm.clearSelected();
		}

		oForm.setTargetForm(targetForm);

		String collection = request.getParameter(AddOrganizationButton.PARAM_COLLECTION_NAME);
		oForm.setTargetCollection(collection);

		String propertyName = request.getParameter(AddOrganizationButton.PARAM_PROPERY_NAME);
		oForm.setTargetProperty(propertyName);

		String callbackFunction = request.getParameter(AddOrganizationButton.PARAM_CALLBACKFUNCTION_NAME);
		oForm.setCallbackFunction(callbackFunction);

		if ("true".equalsIgnoreCase(request.getParameter(AddOrganizationButton.PARAM_REFRESH_PARENT))) {
			oForm.setRefreshParent(true);
		} else {
			oForm.setRefreshParent(false);
		}

		if ("true".equalsIgnoreCase(request.getParameter(AddOrganizationButton.PARAM_USE_CLIENT))) {
			oForm.setUseClient(true);
			oForm.setValueHoder(request.getParameter(AddOrganizationButton.PARAM_VALUE_HOLDER));
			oForm.setNameHolder(request.getParameter(AddOrganizationButton.PARAM_NAME_HOLDER));
		}else{
			
			oForm.setUseClient(false);
		}

		if (!"".equalsIgnoreCase(collection) && collection != null) {
			oForm.setMultiSelect(true);
		} else {
			oForm.setMultiSelect(false);
		}

		if (request.getParameter(AddOrganizationButton.PARAM_RESET_FORM) != null && request.getParameter(AddOrganizationButton.PARAM_RESET_FORM).equals("false")) {
			oForm.setOrgSelReset(false);
		} else {
			oForm.setOrgSelReset(true);
			oForm.reset(mapping, request);
		}

		Collection<AmpOrgType> types;
		types = DbUtil.getAllOrgTypes();
		oForm.setOrgTypes(types);
		oForm.setTempNumResults(10);
		return mapping.findForward("forward");

	}

	@SuppressWarnings("unchecked")
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;
		// EditActivityForm eaForm = (EditActivityForm) form;
		String alpha = request.getParameter("alpha");
		Collection<AmpOrganisation> organizationResult = null;
		Collection<AmpOrganisation> alphaResult = null;
		if (eaForm.getTempNumResults() != 0) {
			eaForm.setNumResults(eaForm.getTempNumResults());
		} else {
			eaForm.setNumResults(10);
		}

		eaForm.setPagesToShow(10);

		if (alpha == null || alpha.trim().length() == 0) {
			organizationResult = new ArrayList();

			if (!eaForm.getAmpOrgTypeId().equals(new Long(-1))) {
				if (eaForm.getKeyword().trim().length() != 0) {
					// serach for organisations based on the keyword and the
					// organisation type
					organizationResult = DbUtil.searchForOrganisation(eaForm.getKeyword().trim(), eaForm.getAmpOrgTypeId());
				} else {
					// search for organisations based on organisation type only
					organizationResult = DbUtil.searchForOrganisationByType(eaForm.getAmpOrgTypeId());
				}
			} else if (eaForm.getKeyword().trim().length() != 0) {
				// search based on the given keyword only.
				organizationResult = DbUtil.searchForOrganisation(eaForm.getKeyword().trim());
			} else {
				// get all organisations since keyword field is blank and org
				// type field has 'ALL'.
				organizationResult = DbUtil.getAmpOrganisations();
			}

			if (organizationResult != null && organizationResult.size() > 0) {
				List<AmpOrganisation> temp = (List<AmpOrganisation>) organizationResult;
				Collections.sort(temp);
				organizationResult = (Collection<AmpOrganisation>) temp;

				if (eaForm.getCurrentAlpha() != null) {
					eaForm.setCurrentAlpha(null);

				}
				eaForm.setStartAlphaFlag(true);

				String[] alphaArray = new String[26];
				int i = 0;

				for (char c = 'A'; c <= 'Z'; c++) {

					for (AmpOrganisation org : organizationResult) {
						if (org.getName().toUpperCase().indexOf(c) == 0) {
							alphaArray[i++] = String.valueOf(c);
							break;
						}
					}

				}
				eaForm.setAlphaPages(alphaArray);
			} else {
				eaForm.setAlphaPages(null);
			}

		} else {
			organizationResult = eaForm.getOrganizations();
			eaForm.setCurrentAlpha(alpha);
			if (!alpha.equals("viewAll")) {
				eaForm.setStartAlphaFlag(false);
				alphaResult = new ArrayList();
				Iterator itr = organizationResult.iterator();
				while (itr.hasNext()) {
					AmpOrganisation org = (AmpOrganisation) itr.next();
					if (org.getName().toUpperCase().startsWith(alpha)) {
						alphaResult.add(org);
					}
				}
				eaForm.setColsAlpha(alphaResult);
			} else
				eaForm.setStartAlphaFlag(true);
		}

		eaForm.setAllOrganization(organizationResult);

		int stIndex = 1;
		int edIndex = eaForm.getNumResults();
		Vector vect = new Vector();
		int numPages;

		if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
			if (edIndex > organizationResult.size()) {
				edIndex = organizationResult.size();
			}
			vect.addAll(organizationResult);
			numPages = organizationResult.size() / eaForm.getNumResults();
			numPages += (organizationResult.size() % eaForm.getNumResults() != 0) ? 1 : 0;
		} else {
			if (edIndex > alphaResult.size()) {
				edIndex = alphaResult.size();
			}
			vect.addAll(alphaResult);
			numPages = alphaResult.size() / eaForm.getNumResults();
			numPages += (alphaResult.size() % eaForm.getNumResults() != 0) ? 1 : 0;
		}

		Collection filteredResult = new ArrayList();
		for (int i = (stIndex - 1); i < edIndex; i++) {
			filteredResult.add(vect.get(i));
		}

		Collection pages = null;

		if (numPages > 1) {
			pages = new ArrayList();
			for (int i = 0; i < numPages; i++) {
				Integer pageNum = new Integer(i + 1);
				pages.add(pageNum);
			}
		}
		eaForm.setOrganizations(filteredResult);
		eaForm.setPages(pages);
		eaForm.setCurrentPage(new Integer(1));

		return mapping.findForward("forward");

	}

	public ActionForward selectPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;

		int page = 0;
		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));
		}

		if (eaForm.getNumResults() == 0) {
			eaForm.setTempNumResults(10);
			eaForm.setOrgTypes(DbUtil.getAllOrgTypes());
			if (eaForm.getAlphaPages() != null)
				eaForm.setAlphaPages(null);
		} else {
			int stIndex = ((page - 1) * eaForm.getNumResults()) + 1;
			int edIndex = page * eaForm.getNumResults();

			if (eaForm.isStartAlphaFlag()) {
				if (edIndex > eaForm.getAllOrganization().size()) {
					edIndex = eaForm.getAllOrganization().size();
				}
			} else {
				if (edIndex > eaForm.getColsAlpha().size()) {
					edIndex = eaForm.getColsAlpha().size();
				}
			}

			Vector vect = new Vector();

			if (eaForm.isStartAlphaFlag())
				vect.addAll(eaForm.getAllOrganization());
			else
				vect.addAll(eaForm.getColsAlpha());

			Collection tempCol = new ArrayList();

			for (int i = (stIndex - 1); i < edIndex; i++) {
				tempCol.add(vect.get(i));
			}

			eaForm.setOrganizations(tempCol);
			eaForm.setCurrentPage(new Integer(page));
		}
		return mapping.findForward("forward");
	}

	public ActionForward setOrganizationToForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;

		AmpOrganisation org = DbUtil.getOrganisation(eaForm.getSelOrganisations()[0]);

		if (!eaForm.isUseClient()) {
			Field target = eaForm.getTargetForm().getClass().getDeclaredField(eaForm.getTargetProperty());
			target.setAccessible(true);

			target.set(eaForm.getTargetForm(), org);

		}

		return mapping.findForward("forward");

	}

	public ActionForward addOrganizationToForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;

		if (!eaForm.isUseClient()) {
			Field target = eaForm.getTargetForm().getClass().getDeclaredField(eaForm.getTargetCollection());
			target.setAccessible(true);
			Collection<AmpOrganisation> targetCollecion = (Collection<AmpOrganisation>) target.get(eaForm.getTargetForm());

			if (targetCollecion == null) {
				targetCollecion = new ArrayList<AmpOrganisation>();
			}

			Long[] orgIds = eaForm.getSelOrganisations();

			for (int i = 0; i < orgIds.length; i++) {

				AmpOrganisation org = DbUtil.getOrganisation(orgIds[i]);
				if (!targetCollecion.contains(org)) {
					targetCollecion.add(org);
				}
			}

			target.set(eaForm.getTargetForm(), targetCollecion);
			eaForm.setAfterSelect(true);
		}
		return mapping.findForward("forward");
	}

};
