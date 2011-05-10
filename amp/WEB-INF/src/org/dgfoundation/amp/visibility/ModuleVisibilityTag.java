/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;


/**
 * @author dan
 *
 */
public class ModuleVisibilityTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3079619271981032373L;
	private String name;
	private String enabled;
	private String parentModule;
	private String hasLevel;
	private static Logger logger = Logger.getLogger(ModuleVisibilityTag.class);
	
	public String getHasLevel() {
		return hasLevel;
	}
	public void setHasLevel(String hasLevel) {
		this.hasLevel = hasLevel;
	}
	public String getParentModule() {
		return parentModule;
	}
	public void setParentModule(String parentModule) {
		this.parentModule = parentModule;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
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
	public ModuleVisibilityTag() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		
		ServletContext ampContext=pageContext.getServletContext();
		AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
		try
		{
			String cache=(String) ampContext.getAttribute("FMcache");
//			   if(cache==null || cache=="" || "read".compareTo(cache)==0) ;
//			   else
//			   if("readwrite".compareTo(cache)==0)
//			   {
				  //logger.info("Module visibility: cache is in writing mode...");
				   if(ampTreeVisibility!=null)
					{
//						if(!existModuleinDB(ampTreeVisibility) && FeaturesUtil.getModuleVisibility(name)==null) //for concurent users...
					   
						if(!existModuleinDB(ampTreeVisibility))
						{//insert without parent??
							synchronized (this) {
								if(FeaturesUtil.getModuleVisibility(name)==null){
									FeaturesUtil.insertModuleVisibility(ampTreeVisibility.getRoot().getId(),this.getName(),this.getHasLevel());
									logger.debug("Inserting module: " + this.getName());
									AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
									ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
									ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);
								}
							}
		   		   		}
//						else 
//							if(!checkTypeAndParentOfModule(ampTreeVisibility) || !checkTypeAndParentOfModule2(FeaturesUtil.getModuleVisibility(name))) //parent or type is not ok
						if(!checkTypeAndParentOfModule(ampTreeVisibility)) 
							{
								try{
									//logger.info("Updating module: "+this.getName() +" with  id:"+ ampTreeVisibility.getModuleByNameFromRoot(this.getName()).getId() +"and his parent "+parentModule);
									synchronized (this) {
										if(!checkTypeAndParentOfModule2(FeaturesUtil.getModuleVisibility(name))){
											logger.debug("Trying to update module: "+this.getName() +" with  id:" +"and his parent "+parentModule);
											AmpModulesVisibility moduleAux= ampTreeVisibility.getModuleByNameFromRoot(this.getName());
											if(moduleAux!=null)
												if(moduleAux.getId()!=null)
												{
													FeaturesUtil.updateModuleVisibility(moduleAux.getId(), parentModule);
													logger.debug(".........updating module: "+this.getName() +" with  id:" +"and his parent "+parentModule);
												}
										}
									}
								}
								catch(Exception e)
									{
										e.printStackTrace();
									}
									AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
									ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
									ampContext.setAttribute("ampTreeVisibility", ampTreeVisibility);
							}
						
					}
//					else return SKIP_BODY;
//				}
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return EVAL_BODY_BUFFERED;
	}
	public int doEndTag() throws JspException 
    {
		if (bodyContent==null) return  SKIP_BODY;
		if(bodyContent.getString()==null) return SKIP_BODY;
       String bodyText = bodyContent.getString();
       try {
    	   
    	   ServletContext ampContext=pageContext.getServletContext();
    	   HttpSession session=pageContext.getSession();
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
   		   if(isModuleActive(ampTreeVisibility)){
                               Map scope=PermissionUtil.getScope(pageContext.getSession());
                               AmpModulesVisibility ampModulesFromTree=ampTreeVisibility.getModuleByNameFromRoot(getName());
                               TeamMember teamMember 	= (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);

   				if (teamMember!=null && !teamMember.getTeamHead()){
   	   			    PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
   	   			    ServletRequest request = pageContext.getRequest();
   	   			    String actionMode = (String) request.getAttribute(GatePermConst.ACTION_MODE);
   	   			    if(ampModulesFromTree.getPermission(false)!=null &&
   	   			    	PermissionUtil.getFromScope(session, GatePermConst.ScopeKeys.ACTIVITY)!=null &&
   	   			    	!ampModulesFromTree.canDo(GatePermConst.Actions.EDIT.equals(actionMode)?
   	   			    			actionMode:GatePermConst.Actions.VIEW,scope)){
   	   			    	return SKIP_BODY;
   	   			    }
   				}

   			pageContext.getOut().print(bodyText);
   		   }
   		   else{;
   			////System.out.println("Field MANAGER!!!! module "+this.getName()+" is not ACTIVE");
   			   //the field is not active!!!
   		   }
       }
       catch (Exception e) {
    	   e.printStackTrace();
       	throw new JspTagException(e.getMessage());
       }
       
       return EVAL_PAGE;//SKIP_BODY 
    }
	
	public boolean isModuleActive(AmpTreeVisibility atv)
	{
		AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) atv.getRoot();
		if(currentTemplate!=null)
			if(currentTemplate.getAllItems()!=null)
				for(Iterator it=currentTemplate.getItems().iterator();it.hasNext();)
				{
					AmpModulesVisibility module=(AmpModulesVisibility) it.next();
					if(module.getName().compareTo(this.getName())==0) 
					{
						return true;
					}
			
				}
		return false;
	}
	
	
	public boolean existModuleinDB(AmpTreeVisibility atv)
	{
		AmpModulesVisibility moduleByNameFromRoot=null;
		if(atv!=null)
			moduleByNameFromRoot = atv.getModuleByNameFromRoot(this.getName());
		if(moduleByNameFromRoot==null) return false;
		return true;
	}
	
	public boolean checkTypeAndParentOfModule(AmpTreeVisibility atv)
	{
		AmpModulesVisibility moduleByNameFromRoot=null;
		//boolean typeOK=false;
		boolean typeOK=true;
		boolean parentOK=false;
		if(atv!=null)
			moduleByNameFromRoot = atv.getModuleByNameFromRoot(this.getName());
		else {
			return typeOK && parentOK;
		}

		if(moduleByNameFromRoot==null) {
			return false;
		}
		if(this.getParentModule()!=null && moduleByNameFromRoot.getParent()!=null)
			if(moduleByNameFromRoot.getParent().getName().compareTo(this.getParentModule())==0)
				parentOK=true;
		if(this.getParentModule()==null && moduleByNameFromRoot.getParent()==null)
			parentOK=true;
		return typeOK && parentOK;
	}

	public boolean checkTypeAndParentOfModule2(AmpModulesVisibility atv)
	{
		AmpModulesVisibility moduleByNameFromRoot=null;
		//boolean typeOK=false;
		boolean typeOK=true;
		boolean parentOK=false;
		if(atv!=null)
			moduleByNameFromRoot = atv;
		else return typeOK && parentOK;
		if(moduleByNameFromRoot==null) return false;
		
		if(this.getParentModule()!=null && moduleByNameFromRoot.getParent()!=null)
			if(moduleByNameFromRoot.getParent().getName().compareTo(this.getParentModule())==0)
				parentOK=true;
		
		//if(moduleByNameFromRoot.getParent()==null) return false;
		if(this.getParentModule()==null && moduleByNameFromRoot.getParent()==null)
			parentOK=true;
		return typeOK && parentOK;
	}
	
}
