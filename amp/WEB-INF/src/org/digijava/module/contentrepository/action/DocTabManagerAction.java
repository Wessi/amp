package org.digijava.module.contentrepository.action;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.contentrepository.dbentity.filter.DocumentFilter;
import org.digijava.module.contentrepository.form.DocTabManagerForm;
import org.digijava.module.contentrepository.helper.filter.DocumentFilterJson;
import org.digijava.module.contentrepository.util.DocumentFilterDAO;

public class DocTabManagerAction extends MultiAction {

	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return modeSelect(mapping, form, request, response);
	}

	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DocTabManagerForm myForm	= (DocTabManagerForm) form;
		String action				= request.getParameter("action");
		if ( "show".equals( action ) )
			return modeShow(mapping, myForm, request, response);
		else if ( "save".equals(action) ) {
			return modeSave(mapping, myForm, request, response);
		}
		else if ( "jsonfilter".equals(action) ) {
			return modeGetJSONFilters(mapping, myForm, request, response);
		}
		else if ("savePositions".equals(action)) {
			return modeSavePositions(mapping, myForm, request, response);
		}
		else if ("publicShow".equals(action) ) {
			return modePublicShow(mapping, myForm, request, response);
		}
		else if ("delete".equals(action) ) {
			return modeDelete(mapping, myForm, request, response);
		}
		
		
		return null;
	}
	
	public ActionForward modeShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DocTabManagerForm myForm	= (DocTabManagerForm) form;
		
		DocumentFilterDAO dfDAO	= new DocumentFilterDAO();
		List<DocumentFilter> availableDocumentFilters	= dfDAO.getAll();
		
		myForm.setAvailableDocumentFilters(availableDocumentFilters);
		
		return mapping.findForward("forward");
	}
	
	public ActionForward modeSave(ActionMapping mapping, DocTabManagerForm myForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		HttpSession session		= request.getSession();
		DocumentFilter df		= (DocumentFilter) session.getAttribute(DocumentFilter.SESSION_LAST_APPLIED_PUBLIC_FILTER );
		if ( myForm.getSavingFilterName() != null) {
			df.setName(myForm.getSavingFilterName() );
			DocumentFilterDAO dfDAO	= new DocumentFilterDAO();
			dfDAO.saveObject(df);
			myForm.setSavingFilterName(null);
		}
		
		return modeShow(mapping, myForm, request, response);
	}
	public ActionForward modeSavePositions(ActionMapping mapping, DocTabManagerForm myForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (myForm.getPublicViewPosition() != null) {
			DocumentFilterDAO dfDAO		= new DocumentFilterDAO();
			List<DocumentFilter> dfList	= dfDAO.getAll();
			if (dfList != null) {
				for (DocumentFilter df: dfList) {
					if (df.getPublicViewPosition() != null) {
						df.setPublicViewPosition(null);
						dfDAO.saveObject(df);
					}
				}
			}
				
			for (int i=0; i<myForm.getPublicViewPosition().length; i++) {
				Long filterId			= myForm.getPublicViewPosition()[i];
				if (filterId != -1) {
					DocumentFilter df		= dfDAO.getDocumentFilter(filterId);
					df.setPublicViewPosition((long)i);
					dfDAO.saveObject(df);
				}
			}
		}
		return modeShow(mapping, myForm, request, response);
	}
	
	public ActionForward modeGetJSONFilters(ActionMapping mapping, DocTabManagerForm myForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String filterIdStr		= request.getParameter("filterId");
		if ( filterIdStr != null ) {
			Long filterId				= Long.parseLong(filterIdStr);
			DocumentFilter df			= new DocumentFilterDAO().getDocumentFilter(filterId);
			DocumentFilterJson dfJSON	= new DocumentFilterJson(df, request);	
			
			JsonConfig jsonConfig	= new JsonConfig();
			jsonConfig.setExcludes(new String[] {"children", "node" });
			JSONObject jsonObj		= JSONObject.fromObject(dfJSON, jsonConfig);
			
			System.out.println(jsonObj.toString());
			response.setContentType("text/json");
			response.setCharacterEncoding("UTF-16");
			PrintStream ps						= new PrintStream( response.getOutputStream(), false, "UTF-16" );
			ps.print( jsonObj.toString() );
		}
		
		return null;
	}
	public ActionForward modePublicShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DocTabManagerForm myForm	= (DocTabManagerForm) form;
		
		DocumentFilterDAO dfDAO		= new DocumentFilterDAO();
		List<DocumentFilter> availableDocumentFilters	= dfDAO.getAll();
		
		TreeSet<DocumentFilter> positioned			= new TreeSet<DocumentFilter>(
					new Comparator<DocumentFilter>() {
						@Override
						public int compare(DocumentFilter o1, DocumentFilter o2) {
							return o1.getPublicViewPosition().compareTo(o2.getPublicViewPosition());
						}
						
		});
		List<DocumentFilter> unPositioned		= new ArrayList<DocumentFilter>();
		
		if (availableDocumentFilters != null) {
			for (DocumentFilter df: availableDocumentFilters) {
				if (df.getPublicViewPosition() != null) {
					positioned.add(df);
				}
				else
					unPositioned.add(df);
			}
			myForm.setPublicFiltersPositioned( new ArrayList<DocumentFilter>() );
			myForm.getPublicFiltersPositioned().addAll(positioned);
			myForm.setPublicFiltersUnpositioned(unPositioned);
		}
		
		return mapping.findForward("publicResources");
	}
	
	public ActionForward modeDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DocTabManagerForm myForm	= (DocTabManagerForm) form;
		String filterIdStr		= request.getParameter("filterId");
		if ( filterIdStr != null ) {
			Long filterId				= Long.parseLong(filterIdStr);
			DocumentFilterDAO dfDAO		= new DocumentFilterDAO();
			dfDAO.deleteDocumentFilter(filterId);
			
		}
		return modeShow(mapping, myForm, request, response);
		
	}
}
