/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.kernel.exception.*;


/**
 * @author dan
 *
 */
public class FeatureVisibilityTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1296936554150626082L;
	private static Logger logger = Logger.getLogger(FeatureVisibilityTag.class);
	private String name;
	private String module;
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
	public FeatureVisibilityTag() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		ServletContext ampContext=pageContext.getServletContext();
 	   AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
 try{
	 String cache=(String) ampContext.getAttribute("FMcache");
//	   if(cache==null || cache=="" || "read".compareTo(cache)==0) ;
//	   else
//	   if("readwrite".compareTo(cache)==0)
//	   {
		   //logger.info("	Feature visibility: cache is in writing mode...");
		   if(ampTreeVisibility!=null){
				 if(!existModule(ampTreeVisibility)) return SKIP_BODY;

	 		   if(!existFeatureinDB(ampTreeVisibility)){
	 			  synchronized (this) {
	 				  if(FeaturesUtil.getFeatureVisibility(name)==null)
	 				  {
	                    AmpModulesVisibility moduleByNameFromRoot = ampTreeVisibility.getModuleByNameFromRoot(this.getModule());
	                    Long id=null;
	                    if(moduleByNameFromRoot!=null){
	                       id = moduleByNameFromRoot.getId();
	                       try {	
	                    	   if(FeaturesUtil.getFeatureVisibility(this.getName())!=null)
		   			        	{
		   			        		FeaturesUtil.updateFeatureWithModuleVisibility(ampTreeVisibility.getModuleByNameFromRoot(this.getModule()).getId(),this.getName());
		   			        	}
		   			        	else    
	                    	   		FeaturesUtil.insertFeatureWithModuleVisibility(ampTreeVisibility.getRoot().getId(),id, this.getName(), this.getHasLevel());
		                            AmpTemplatesVisibility currentTemplate = (AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
		                            ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
	                                ampContext.setAttribute("ampTreeVisibility",ampTreeVisibility);
	                           	}
	                           	catch (DgException ex) {throw new JspException(ex);}
	                     }
	                    else {
	                    	logger.info("Feature: "+this.getName() + " has the parent: "+this.getModule()+ " which doesn't exist in DB");
	                    	return SKIP_BODY;
	                    }
	 				  }
	 			  }
	 		   }
		   }
		   ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
		   if(ampTreeVisibility!=null)
	   		   if(!isModuleTheParent(ampTreeVisibility)){
	   			   //update(featureId, fieldname);
				   ////System.out.println("error!!!! module "+this.getModule()+" is not the parent");
				   
				   FeaturesUtil.updateFeatureWithModuleVisibility(ampTreeVisibility.getModuleByNameFromRoot(this.getModule()).getId(),this.getName());
				   AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
	   			   ////System.out.println("-------------------------------update the parent of the feature");
	   			   ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
	   			   ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);
			   }
//	   }
	   
 	   
 	}catch (Exception e) {e.printStackTrace();}
	   
		return EVAL_BODY_BUFFERED;//super.doStartTag();
	}
	public int doEndTag() throws JspException 
    {
	   if (bodyContent==null) return  SKIP_BODY;
	   if(bodyContent.getString()==null) return SKIP_BODY;
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
   		   
   		   ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
   		   if(ampTreeVisibility!=null)
   			   if(isFeatureActive(ampTreeVisibility)){
   				   pageContext.getOut().print(bodyText);
   			   }
    	   
       }
       catch (Exception e) {
    	   e.printStackTrace();
       	throw new JspTagException(e.getMessage());
       }
       return EVAL_PAGE;//SKIP_BODY 
    }
	
	public boolean isFeatureActive(AmpTreeVisibility atv)
	{
		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) atv.getRoot();
		if(currentTemplate!=null)
			if(currentTemplate.getFeatures()!=null)
				for(Iterator it=currentTemplate.getFeatures().iterator();it.hasNext();)
				{
					AmpFeaturesVisibility feature=(AmpFeaturesVisibility) it.next();
					if(feature.getName().compareTo(this.getName())==0) 
					{
						return true;
					}	
					
				}
		return false;
	}
	
	public boolean existModule(AmpTreeVisibility atv)
	{

		AmpModulesVisibility moduleByNameFromRoot = atv.getModuleByNameFromRoot(this.getModule());
		if(moduleByNameFromRoot==null) return false;
		return true;
	}
	
	public boolean existFeatureinDB(AmpTreeVisibility atv)
	{
		AmpFeaturesVisibility featureByNameFromRoot = atv.getFeatureByNameFromRoot(this.getName());
		if(featureByNameFromRoot==null) return false;
		return true;
	}
	
	public boolean isModuleTheParent(AmpTreeVisibility atv)
	{
		AmpTreeVisibility moduleByNameFromRoot = atv.getModuleTreeByNameFromRoot(this.getModule());
		//AmpFeaturesVisibility f=(AmpFeaturesVisibility) featureByNameFromRoot.getRoot();
		if(moduleByNameFromRoot!=null)
			if(moduleByNameFromRoot.getItems()!=null)
				{
				if(moduleByNameFromRoot.getItems().containsKey(this.getName())) return true;
				}
			//else //System.out.println("errror in FM - feature: "+this.getModule());
		//else //System.out.println("errror in FM - feature: "+this.getModule());		
		return false;
	}
	
	
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
}
