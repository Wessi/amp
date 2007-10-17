/*
 * ViewChannelOverview.java
 */

package org.digijava.module.aim.action;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.ChannelOverviewForm;
import org.digijava.module.aim.form.ReferenceForm;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class ViewReferences extends TilesAction {

	private static Logger logger = Logger.getLogger(ViewReferences.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		Collection<AmpActivityReferenceDoc> activityRefDocs=null;
		Map<Long, AmpActivityReferenceDoc> categoryRefDocMap=null;
		Collection<AmpCategoryValue> catValues=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.REFERENCE_DOCS_KEY,false);
		List<ReferenceDoc> refDocs=new ArrayList<ReferenceDoc>();
		ReferenceForm formBean = (ReferenceForm) form;

		if (teamMember == null) {
			formBean.setValidLogin(false);
		} else {
			formBean.setValidLogin(true);
			Long id = null;
			if (request.getParameter("ampActivityId") != null) {
				id = new Long(request.getParameter("ampActivityId"));
			}
			else {
				id = formBean.getId();
			}
			
			try {
				activityRefDocs=ActivityUtil.getReferenceDocumentsFor(id);
			} catch (DgException e) {
				// TODO Auto-generated catch block
				throw new ServletException("can't get reference doc", e); 
				//e.printStackTrace();
			}
			
			
			
			categoryRefDocMap = AmpCollectionUtils.createMap(activityRefDocs, new ActivityUtil.CategoryIdRefDocMapBuilder());
			
			
			//create arrays, number of elements as much as category values
        	Long[] refdocIds=new Long[catValues.size()];
        	String[] refdocComments=new String[catValues.size()];
        	
        	int c=0;
        	int selectedIds=0;
        	for(AmpCategoryValue catVal: catValues){
        		AmpActivityReferenceDoc refDoc=(categoryRefDocMap==null)?null:categoryRefDocMap.get(catVal.getId());
        		ReferenceDoc doc=new ReferenceDoc();
        		doc.setCategoryValueId(catVal.getId());
        		doc.setCategoryValue(catVal.getValue());
        		if (refDoc==null){
        			refdocComments[c]="";
        			doc.setComment("");
        			doc.setChecked(false);
        		}else{
        			refdocIds[selectedIds++]=refDoc.getCategoryValue().getId();
        			refdocComments[c]=refDoc.getComment();
        			doc.setComment(refDoc.getComment());
        			doc.setRefDocId(refDoc.getId());
        			doc.setChecked(true);
        		}
        		refDocs.add(doc);
        		c++;
        	}
        	
        	
        	//set selected ids
        	formBean.setAllReferenceDocNameIds(refdocIds);
        	//set all comments, some are empty
//        	eaForm.setRefDocComments(refdocComments);
        	
        	formBean.setReferenceDocs(refDocs);
        	
    		
			
			
		}
		return null;
	}
}
