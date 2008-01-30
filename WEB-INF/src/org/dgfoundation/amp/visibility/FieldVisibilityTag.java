/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;


/**
 * @author dan
 *
 */
public class FieldVisibilityTag extends BodyTagSupport {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7009665621191882475L;
	private String name;
	private String feature;
	private String enabled;
	private String hasLevel;
	
	public String getHasLevel() {
		return hasLevel;
	}
	public void setHasLevel(String hasLevel) {
		this.hasLevel = hasLevel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 */
	public FieldVisibilityTag() {
		super();
		// TODO Auto-generated constructor stub
	}
		public int doStartTag() throws JspException {
	
 	   ServletContext ampContext=pageContext.getServletContext();
 try{
	   AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
	   if(ampTreeVisibility!=null)
		   if(!existFieldinDB(ampTreeVisibility)){
			   //if(FeaturesUtil.getFieldVisibility(name)==null)
			   //{
                    AmpFeaturesVisibility featureByNameFromRoot = ampTreeVisibility.getFeatureByNameFromRoot(this.getFeature());
                    Long id=null;
                    if(featureByNameFromRoot!=null)
                    {
                        id = featureByNameFromRoot.getId();
	   			        try {
                             FeaturesUtil.insertFieldWithFeatureVisibility(ampTreeVisibility.getRoot().getId(),id, this.getName(),this.getHasLevel());
                             AmpTemplatesVisibility  currentTemplate = (AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
                             ampTreeVisibility. buildAmpTreeVisibility(currentTemplate);
                             ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);
                           	}
                         catch (DgException ex) {throw new JspException(ex);	}
                     }
                     else return EVAL_BODY_BUFFERED;
			  //}
	   		}
	   		ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
	   		if(ampTreeVisibility!=null)
	   		   if(!isFeatureTheParent(ampTreeVisibility)){
	   			   //update(featureId, fieldname);
				   //System.out.println("error!!!! feature "+this.getFeature()+" is not the parent");
				   FeaturesUtil.updateFieldWithFeatureVisibility(ampTreeVisibility.getFeatureByNameFromRoot(this.getFeature()).getId(),this.getName());
	   			   AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
	   			   //System.out.println("-------------------------------"+currentTemplate.getId());
	   			   ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
	   			   ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);

			   }
 }catch (Exception e) {e.printStackTrace();}
	   	
 	return EVAL_BODY_BUFFERED;//super.doStartTag();
	
	}
	
	public int doEndTag() throws JspException 
    {
		if (bodyContent==null) return  EVAL_PAGE;//SKIP_BODY;
		if(bodyContent.getString()==null) return EVAL_PAGE;
		String bodyText = bodyContent.getString();
       
       try {
    	   ServletContext ampContext=pageContext.getServletContext();
    	   AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
    	   
    	   /* name, feature, enable
    	    * 
    	    * if feature is not in the db, error! it has to be already added this feature
    	    * 
    	    *if field is not in db insert it with feature as parent
    	    *
    	    * is this feature the correct parent? if not -> error!
    	    * 
    	    * if field is active then display the content
    	    */
    	   
   		   if(ampTreeVisibility!=null)
   		   {
   			if(! existFeature(ampTreeVisibility)) 
 			   return SKIP_BODY;
   			
 		    AmpFieldsVisibility ampFieldFromTree=ampTreeVisibility.getFieldByNameFromRoot(getName());
 		    
   			HashMap<String, HttpSession> sessionMap=new HashMap<String, HttpSession>();
   			sessionMap.put("session", pageContext.getSession());
   			
   			Map scope=PermissionUtil.getScope(pageContext.getSession());   				    
   			if(isFieldActive (ampTreeVisibility) ) {
   				HttpSession session		= pageContext.getSession();
   				TeamMember teamMember 	= (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
   			    
   				//TODO AMP-2579 this IF was added to fix null pointer temporary.
   				if (teamMember!=null){
   	   			    PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
   	   			    if(ampFieldFromTree.getPermission(false)!=null && !ampFieldFromTree.canDo(GatePermConst.Actions.VIEW,scope))
   	   			    return SKIP_BODY;
   				}

   				pageContext.getOut().print(bodyText);   			    
   			} else return SKIP_BODY;//the field is not active!!!
   		   }
    	   
       }
       catch (Exception e) {
    	   e.printStackTrace();
       	throw new JspTagException(e.getMessage());
       }
       return EVAL_PAGE;//SKIP_BODY 
    }
	
	public boolean isFieldActive(AmpTreeVisibility atv)
	{
		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) atv.getRoot();
		if(currentTemplate!=null)
			if(currentTemplate.getFeatures()!=null)
				for(Iterator it=currentTemplate.getFields().iterator();it.hasNext();)
				{
					AmpFieldsVisibility field=(AmpFieldsVisibility) it.next();
					if(field.getName().compareTo(this.getName())==0) 
					{	
						return true;
					}
			
				}
		return false;
	}
	
	public boolean existFeature(AmpTreeVisibility atv)
	{

		AmpFeaturesVisibility featureByNameFromRoot = atv.getFeatureByNameFromRoot(this.getFeature());
		if(featureByNameFromRoot==null) return false;
		return true;
	}
	
	public boolean existFieldinDB(AmpTreeVisibility atv)
	{

		AmpFieldsVisibility fieldByNameFromRoot = atv.getFieldByNameFromRoot(this.getName());
		if(fieldByNameFromRoot==null) return false;
		return true;
	}
	
	public boolean isFeatureTheParent(AmpTreeVisibility atv)
	{
		AmpTreeVisibility featureByNameFromRoot = atv.getFeatureTreeByNameFromRoot(this.getFeature());
		//AmpFeaturesVisibility f=(AmpFeaturesVisibility) featureByNameFromRoot.getRoot();
		if(featureByNameFromRoot!=null)
			if(featureByNameFromRoot.getItems()!=null)
				{
					if(featureByNameFromRoot.getItems().containsKey(this.getName())) return true;
				}
			else System.out.println("errror in FM - field: "+this.getFeature());
		else System.out.println("errror in FM - field: "+this.getFeature());
		return false;
	}
	
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
}
