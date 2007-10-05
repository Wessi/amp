package org.digijava.module.contentrepository.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.contentrepository.form.SelectDocumentForm;
import org.digijava.module.contentrepository.helper.TeamInformationBeanDM;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class SelectDocumentDM extends Action {
	public static String CONTENT_REPOSITORY_HASH_MAP	= "Content Repository Hash Map";
	
	public static HashMap<String, Object> getContentRepositoryHashMap(HttpServletRequest request) {
		HashMap<String, Object> contentRepositoryHashMap	= (HashMap<String, Object>)request.getSession().getAttribute(CONTENT_REPOSITORY_HASH_MAP);
		
		if (contentRepositoryHashMap == null) {
			contentRepositoryHashMap	= new HashMap<String, Object>();
			request.getSession().setAttribute(CONTENT_REPOSITORY_HASH_MAP, contentRepositoryHashMap);
		}
		
		return contentRepositoryHashMap;
	}
	
	public static HashSet<String> getSelectedDocsSet (HttpServletRequest request, String documentsType, boolean createIfNull) {
		HashMap<String,Object> map			= getContentRepositoryHashMap(request);
		HashSet<String> selectedDocsSet		= (HashSet<String>)map.get( documentsType );
		if ( createIfNull && selectedDocsSet == null) {
			selectedDocsSet	= new HashSet<String>();
			map.put(documentsType, selectedDocsSet);
		}
		
		return selectedDocsSet;
	}
	
	public static void clearContentRepositoryHashMap(HttpServletRequest request) {
		HashMap<String,Object> map	= getContentRepositoryHashMap(request);
		map.clear();
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception 
	{

		SelectDocumentForm selectDocumentForm	= (SelectDocumentForm) form;
		
		if (request.getParameter("selectedDocs") != null) {
			HashMap<String, Object>	crSessionMap	= getContentRepositoryHashMap(request);
			HashSet<String> selectedDocsSet			= getSelectedDocsSet(request, selectDocumentForm.getDocumentsType(), true);
			
			if ( selectDocumentForm.getAction().equals("set") ) {
				for (int i=0; i<selectDocumentForm.getSelectedDocs().length; i++) {
					selectedDocsSet.add( selectDocumentForm.getSelectedDocs()[i] );
				}
			}
			if ( selectDocumentForm.getAction().equals("remove") ){
				for (int i=0; i<selectDocumentForm.getSelectedDocs().length; i++) {
					Set<String> typeSets			= crSessionMap.keySet();
					Iterator<String> typeSetsIter	= typeSets.iterator();
					while ( typeSetsIter.hasNext() ) {
						Object value		= crSessionMap.get( typeSetsIter.next() );
						if (value instanceof HashSet) {
							HashSet<String> docsSet	= (HashSet<String>) value;
							docsSet.remove( selectDocumentForm.getSelectedDocs()[i] );
						}
					} 
				}
			}
			
			//crSessionMap.put( SELECTED_DOCUMENTS, selectedDocsSet );
			return null; 
		}
		
		this.resetForm(selectDocumentForm);
		
		TeamInformationBeanDM teamInfo			= DocumentManagerUtil.getTeamInformationBeanDM( request.getSession() );
		
		selectDocumentForm.setTeamInformationBeanDM(teamInfo);
		
		return mapping.findForward("forwardDM");
	}
	
	private void resetForm(SelectDocumentForm myForm) {
		myForm.setSelectedDocs(null);
		myForm.setTeamInformationBeanDM(null);
		myForm.setHasAddRights(false);
	}
}
