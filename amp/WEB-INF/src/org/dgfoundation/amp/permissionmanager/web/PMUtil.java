/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMGateReadEditWrapper;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMReadEditWrapper;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.GatePermission;
import org.digijava.module.gateperm.core.Permissible;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;
import org.digijava.module.gateperm.gates.OrgRoleGate;
import org.digijava.module.gateperm.gates.UserLevelGate;
import org.digijava.module.gateperm.gates.WorkspaceGate;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author dan
 *
 */
public final class PMUtil {

	 private static Logger logger = Logger.getLogger(PMUtil.class);
	
	public static final String CUMMULATIVE = "Cummulative";
	public static final String WORKSPACE_PERMISSION = "Workspace based permission";
	public static final String ROLE_PERMISSION = "Role based permission";
	public static final String WORKSPACE_MEMBER_IMG_SRC = "/TEMPLATE/ampTemplate/img_2/ico_user.gif";
	public static final String WORKSPACE_MANAGER_IMG_SRC = "/TEMPLATE/ampTemplate/img_2/ico_user_admin.gif";
	
	
	
	public static void setGlobalPermission(Class globalPermissionMapForPermissibleClass, Permission permission,String simpleName) {
		// TODO Auto-generated method stub
		Session hs=null;
		try {
			hs = PermissionUtil.saveGlobalPermission(globalPermissionMapForPermissibleClass, permission.getId(), simpleName);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(hs!=null){
			//pf.setPermissionId(new Long(0));
			try {
				PersistenceManager.releaseSession(hs);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	
	
	public static void savePermission(IModel<CompositePermission> cpModel, Set<AmpPMGateReadEditWrapper> gatesSet) throws DgException {
		// TODO Auto-generated method stub
		Session session=null;
		try {
			session = PersistenceManager.getSession();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PermissionMap permissionMap=new PermissionMap(); 
		permissionMap.setPermissibleCategory(null);
		permissionMap.setObjectIdentifier(null);
		
//		CompositePermission cp=new CompositePermission(false);
//		cp.setDescription("This permission was created using the PM UI by admin user");
//		cp.setDedicated(false);
//		cp.setName(cpModel.getObject().getName());
		
		cpModel.getObject().setDescription("This permission was created using the PM UI by admin user");
		cpModel.getObject().setDedicated(false);
		
		
		for (AmpPMGateReadEditWrapper ampPMGateWrapper : gatesSet) {
			initializeAndSaveGatePermission(session,cpModel.getObject(),ampPMGateWrapper);
		}
		
		session.save(cpModel.getObject());
		
		permissionMap.setPermission(cpModel.getObject());
		
		session.save(permissionMap);
		
		session.flush();
		
	}
	public static void initializeAndSaveGatePermission(Session session, CompositePermission cp, AmpPMReadEditWrapper ampPMGateWrapper) throws HibernateException {
		AmpPMGateReadEditWrapper ampPMGateWrapperLocal = (AmpPMGateReadEditWrapper) ampPMGateWrapper;
		initializeAndSaveGatePermission(session, cp, cp.getName()+" - "+ampPMGateWrapperLocal.getName(), 
				ampPMGateWrapperLocal.getParameter(), ampPMGateWrapperLocal.getGate(),ampPMGateWrapperLocal.getReadFlag()?"on":"off",
						ampPMGateWrapperLocal.getEditFlag()?"on":"off", "This permission has been generated by the Permission Manager UI");
	}
	
	public static void initializeAndSaveGatePermission(Session session,CompositePermission cp,String permissionName,String parameter, Class gate,String readFlag,String editFlag, String description) throws HibernateException {
		GatePermission baGate=new GatePermission(true);
		baGate.setName(permissionName);
		baGate.setDescription(description);
		baGate.getGateParameters().add(parameter);
		baGate.setGateTypeName(gate.getName());
		HashSet baActions=new HashSet();
		if("on".equals(editFlag)) baActions.add(GatePermConst.Actions.EDIT);
		if("on".equals(readFlag)) baActions.add(GatePermConst.Actions.VIEW);
		baGate.setActions(baActions);
		if(baGate.getActions().size()>0) { 
			session.save(baGate);
			cp.getPermissions().add(baGate);
		}
	}
	
	public static void deletePermissionMap(PermissionMap permissionMap, Session session){
	    Permission p=permissionMap.getPermission();
	    //we delete the old permissions, if they are dedicated
	    if (p!=null && p.isDedicated()) {
		CompositePermission cp = (CompositePermission)p;
		Iterator<Permission> i = cp.getPermissions().iterator();
		while (i.hasNext()) {
		    Permission element = (Permission) i.next();
		    Object object = session.load(Permission.class, element.getId());
		    session.delete(object);
		}
		Object object = session.load(Permission.class, cp.getId());
		session.delete(object);
	    }
	    session.flush();
	}




	public static void deleteCompositePermission(CompositePermission cp, Session session) {
		// TODO Auto-generated method stub
		session.flush();	
		Iterator<Permission> i = cp.getPermissions().iterator();
		while (i.hasNext()) {
		    Permission element = (Permission) i.next();
		    Object object = session.load(Permission.class, element.getId());
		    session.delete(object);
		}
		Object object = session.load(Permission.class, cp.getId());
		session.delete(object);
	    session.flush();		
	}




	public static void assignGlobalPermission(PermissionMap pm, Set<AmpPMReadEditWrapper> gatesSet) {
		// TODO Auto-generated method stub
		Session session = null;
			try {
				session = PersistenceManager.getRequestDBSession();
			} catch (DgException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		if(pm!=null && session!=null) {
		    Permission p=pm.getPermission();
		    //we delete the old permissions
		    if (p!=null) {
		    String name = p.getName();
			CompositePermission cp = (CompositePermission)p;
			if(cp.getId()!=null) 
				PMUtil.deleteCompositePermission(cp, session);
			
			cp=new CompositePermission(false);
			cp.setDescription("This permission was created using the PM UI by admin user");
			cp.setName(name);
			
			for (AmpPMReadEditWrapper ampPMGateWrapper : gatesSet) {
				initializeAndSaveGatePermission(session,cp,ampPMGateWrapper);
			}
			session.save(cp);
			pm.setPermission(cp);
			session.save(pm);
			session.flush();
		    }
		}
	}
	
	
	public static CompositePermission createCompositePermissionForFM(String name, Set<AmpPMReadEditWrapper> gatesSet, Set<AmpPMReadEditWrapper> workspacesSet){
		Session session = null;
		try {
			session	=	PersistenceManager.getRequestDBSession();
		} catch (DgException e) {
			e.printStackTrace();
		}
		CompositePermission cp = new CompositePermission(true);
		cp.setDescription("This permission was created using the PM UI by admin user");
		cp.setName(name);
		if(gatesSet!=null && gatesSet.size()>0)
			for (AmpPMReadEditWrapper ampPMGateWrapper : gatesSet)
				initializeAndSaveGatePermission(session,cp,ampPMGateWrapper);
		if(workspacesSet!=null && workspacesSet.size()>0)
			for (AmpPMReadEditWrapper ampPMGateWrapper : workspacesSet)
				initializeAndSaveGatePermission(session,cp,ampPMGateWrapper);

		session.flush(); //
		return cp;
	}
	
	
	public static AmpTreeVisibilityModelBean getAmpTreeFMPermissions() {
		return generateAmpTreeFMPermissions(getDefaultAmpTemplateVisibility());
	}
	
	public static AmpTreeVisibilityModelBean generateAmpTreeFMPermissions(AmpTemplatesVisibility currentTemplate) {
		AmpTreeVisibilityModelBean tree = new AmpTreeVisibilityModelBean(currentTemplate.getName(), new ArrayList<Object>(), currentTemplate);
		if (currentTemplate.getAllItems() != null && currentTemplate.getAllItems().iterator() != null)
				for (Iterator it = currentTemplate.getSortedAlphaAllItems().iterator(); it.hasNext();) {
					AmpModulesVisibility module = (AmpModulesVisibility) it.next();
					if(module.getParent()==null) 
						{
							tree.getItems().add(new AmpTreeVisibilityModelBean(module.getName(),buildAmpSubTreeFMPermission(module), module));
						}
				}
		return tree;
	}
	
	public static AmpTemplatesVisibility getDefaultAmpTemplateVisibility() {
		AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
		// get the default amp template
		Session session = null;
		try {
			session	=	PersistenceManager.getRequestDBSession();
		} catch (DgException e) {
			e.printStackTrace();
		}
		if(session == null) return null;
		AmpTemplatesVisibility currentTemplate = null;
		currentTemplate = FeaturesUtil.getTemplateVisibility(FeaturesUtil.getGlobalSettingValueLong("Visibility Template"),session);
		return currentTemplate;
	}
	
	
	public static AmpTreeVisibilityModelBean buildTreeObjectFMPermissions(AmpObjectVisibility currentAOV) {
		AmpTreeVisibilityModelBean tree = new AmpTreeVisibilityModelBean(currentAOV.getName(), new ArrayList<Object>(), currentAOV);
		Set itemsSet=null;
		if(currentAOV instanceof AmpModulesVisibility && ((AmpModulesVisibility) currentAOV).getSortedAlphaSubModules().size()>0)
			itemsSet = ((AmpModulesVisibility) currentAOV).getSortedAlphaSubModules();
		else itemsSet = currentAOV.getSortedAlphaItems();
		if (itemsSet != null && itemsSet.iterator() != null)
				for (Iterator it = itemsSet.iterator(); it.hasNext();) {
					AmpObjectVisibility item = (AmpObjectVisibility) it.next();
					AmpTreeVisibilityModelBean iitem = new AmpTreeVisibilityModelBean(item.getName(),buildAmpSubTreeFMPermission(item),item);
					tree.getItems().add(iitem);
				}
		return tree;
	}
	
	public static List<Object> buildAmpSubTreeFMPermission(AmpObjectVisibility aov){
		List<Object> list = new ArrayList<Object>();
		Set itemsSet=null;
		if(aov instanceof AmpModulesVisibility && ((AmpModulesVisibility) aov).getSortedAlphaSubModules().size()>0)
			itemsSet = ((AmpModulesVisibility) aov).getSortedAlphaSubModules();
		else itemsSet = aov.getSortedAlphaItems();
		if(itemsSet!=null)
			for (Iterator it = itemsSet.iterator(); it.hasNext();) {
				AmpObjectVisibility item = (AmpObjectVisibility) it.next();
				AmpTreeVisibilityModelBean iitem = new AmpTreeVisibilityModelBean(item.getName(),buildAmpSubTreeFMPermission(item), item);
				list.add(iitem);
			}
		return list;
	}

	
    public static TreeModel createTreeModel(IModel<AmpTreeVisibilityModelBean> treeModel)
    {
    	AmpTreeVisibilityModelBean tree = treeModel.getObject();
        return convertToTreeModel(tree,tree.getItems());
    }

    public static TreeModel convertToTreeModel(AmpTreeVisibilityModelBean tree, List<Object> list)
    {
        TreeModel model = null;
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new AmpTreeVisibilityModelBean(tree.getName(),list, tree.getAmpObjectVisibility()));
        add(rootNode, list);
        model = new DefaultTreeModel(rootNode);
        return model;
    }

    public static void add(DefaultMutableTreeNode parent, List<Object> sub)
    {
        for (Iterator<Object> i = sub.iterator(); i.hasNext();)
        {
        	AmpTreeVisibilityModelBean o = (AmpTreeVisibilityModelBean)i.next();
        	if(o.getItems().size()>0){
              DefaultMutableTreeNode child = new DefaultMutableTreeNode(new AmpTreeVisibilityModelBean(o.getName(),o.getItems(), o.getAmpObjectVisibility()));
              parent.add(child);
              add(child, (List<Object>)o.getItems());
        	}
        	else{
              DefaultMutableTreeNode child = new DefaultMutableTreeNode(new AmpTreeVisibilityModelBean(o.toString(), o.getAmpObjectVisibility()));
              parent.add(child);
        	}
        }
    }
    
	public static void generateGatesList(Object o, Set<AmpPMReadEditWrapper> gatesSet){
		if(!(o instanceof CompositePermission)) return;
		CompositePermission cp = (CompositePermission)o;
		if(cp==null || cp.getId() == null) 
			{
				generateDefaultGatesList(gatesSet);
				return;
			}
    	gatesSet.add(new AmpPMGateReadEditWrapper(new Long(4),"Beneficiary Agency","BA",OrgRoleGate.class, hasView(cp.getPermissions(),"BA"),hasEdit(cp.getPermissions(),"BA")));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(5),"Contracting Agency", "CA", OrgRoleGate.class, hasView(cp.getPermissions(),"CA"),hasEdit(cp.getPermissions(),"CA")));
    	gatesSet.add(new AmpPMGateReadEditWrapper(new Long(6),"Executing Agency", "EA", OrgRoleGate.class, hasView(cp.getPermissions(),"EA"),hasEdit(cp.getPermissions(),"EA")));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(8),"Implementing Agency", "IA", OrgRoleGate.class, hasView(cp.getPermissions(),"IA"),hasEdit(cp.getPermissions(),"IA")));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(7),"Funding Agency", "DN", OrgRoleGate.class, hasView(cp.getPermissions(),"DN"),hasEdit(cp.getPermissions(),"DN")));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(11),"Sector Group", "SG", OrgRoleGate.class, hasView(cp.getPermissions(),"SG"),hasEdit(cp.getPermissions(),"SG")));		
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(10),"Regional Group", "RG", OrgRoleGate.class, hasView(cp.getPermissions(),"RG"),hasEdit(cp.getPermissions(),"RG")));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(9),"Responsible Agency", "RO", OrgRoleGate.class, hasView(cp.getPermissions(),"RO"),hasEdit(cp.getPermissions(),"RO")));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(1),"Everyone", UserLevelGate.PARAM_EVERYONE, UserLevelGate.class, hasView(cp.getPermissions(),UserLevelGate.PARAM_EVERYONE),hasEdit(cp.getPermissions(),UserLevelGate.PARAM_EVERYONE)));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(2),"Guest", UserLevelGate.PARAM_GUEST, UserLevelGate.class, hasView(cp.getPermissions(),UserLevelGate.PARAM_GUEST),hasEdit(cp.getPermissions(),UserLevelGate.PARAM_GUEST)));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(3),"Owner", UserLevelGate.PARAM_OWNER, UserLevelGate.class, hasView(cp.getPermissions(),UserLevelGate.PARAM_OWNER),hasEdit(cp.getPermissions(),UserLevelGate.PARAM_OWNER)));
	}
	
	public static Boolean hasEdit(Set<Permission> permissions, String param) {
		for (Permission p : permissions) {
			{
				GatePermission ap = (GatePermission)p;
				if(ap.hasParameter(param)) return hasEdit(ap);
			}
		}
		return false;
	}

	public static Boolean hasView(Set<Permission> permissions, String param) {
		for (Permission p : permissions) {
			{
				GatePermission ap = (GatePermission)p;
				if(ap.hasParameter(param)) return hasView(ap);
			}
		}
		return false;
	}

	public static boolean hasEdit(GatePermission agencyPerm) {
		return agencyPerm.hasAction(GatePermConst.Actions.EDIT);
	}

	public static boolean hasView(GatePermission agencyPerm) {
		return agencyPerm.hasAction(GatePermConst.Actions.VIEW);
	}
    
	public static void generateDefaultGatesList(Set<AmpPMReadEditWrapper> gatesSet){
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(1),"Everyone", UserLevelGate.PARAM_EVERYONE, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(2),"Guest", UserLevelGate.PARAM_GUEST, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(3),"Owner", UserLevelGate.PARAM_OWNER, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(4),"Beneficiary Agency","BA",OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(5),"Contracting Agency", "CA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(6),"Executing Agency", "EA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(7),"Funding Agency", "DN", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(8),"Implementing Agency", "IA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(9),"Responsible Agency", "RO", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(10),"Regional Group", "RG", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateReadEditWrapper(new Long(11),"Sector Group", "SG", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
	}
	
	public static PermissionMap createPermissionMap(Class globalPermissionMapForPermissibleClass) {
		PermissionMap pmAux;
		pmAux = new PermissionMap();
		pmAux.setPermissibleCategory(globalPermissionMapForPermissibleClass.getSimpleName());
		pmAux.setObjectIdentifier(null);
		pmAux.setPermission(createCompositePermission(globalPermissionMapForPermissibleClass.getSimpleName() + " - Composite Permission",
				"This permission was created using the PM UI by admin user",false));
		return pmAux;
	}
	
	public static CompositePermission createCompositePermission(String name, String description, boolean dedicated){
		CompositePermission cp=new CompositePermission(dedicated);
		cp.setDescription(description);
		cp.setName(name);
		return cp;
	}

	public static void generateWorkspacesList(IModel<Set<AmpTeam>> teamsModel, TreeSet<AmpPMReadEditWrapper> workspacesSet) {
		Set<AmpTeam> ampTeamSet = teamsModel.getObject();
		if(ampTeamSet!=null){
			int i=1;
			for (AmpTeam ampTeam : ampTeamSet) {
				workspacesSet.add(new AmpPMGateReadEditWrapper(new Long(i),ampTeam.getName(),ampTeam.getAmpTeamId().toString(),WorkspaceGate.class, Boolean.FALSE,Boolean.FALSE));
				i++;
			}
		}
	}




	public static List<String> getPermissionPriority() {
			List<String> permissionPriority = new ArrayList<String>();
			permissionPriority.add(PMUtil.ROLE_PERMISSION);
			permissionPriority.add(PMUtil.WORKSPACE_PERMISSION);
			permissionPriority.add(PMUtil.CUMMULATIVE);
			return permissionPriority;
	}




	public static void savePermissionMap( Session session, AmpTreeVisibilityModelBean ampTreeRootObject, CompositePermission cp) {
			PermissionMap permissionMap = new PermissionMap(); 
			permissionMap.setPermissibleCategory(ampTreeRootObject.getAmpObjectVisibility().getPermissibleCategory().getSimpleName());
			permissionMap.setObjectIdentifier(ampTreeRootObject.getAmpObjectVisibility().getId());
//			Calendar cal = Calendar.getInstance();
//			CompositePermission cp1 = PMUtil.createCompositePermissionForFM(ampTreeRootObject.getAmpObjectVisibility().getName()+" Composite Permission " + cal.getTimeInMillis(), gatesSet, workspacesSet);
			session.save(cp);
			permissionMap.setPermission(cp);
			session.save(permissionMap);
		
	}
	
	
    public static List<PermissionMap> getOwnPermissionMapListForPermissible(Permissible obj) {
    	Session session = null;
    	try {
    	    session = PersistenceManager.getRequestDBSession();

    	    Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
    		    + " p WHERE p.permissibleCategory=:categoryName AND p.objectIdentifier=:objectId ORDER BY p.objectIdentifier");
    	    query.setParameter("objectId", obj.getIdentifier());
    	    query.setParameter("categoryName", obj.getPermissibleCategory().getSimpleName());
    	    List<PermissionMap> col = query.list();
    	    
    	    if (col.size() == 0) return null;

    	    return col;
    	
    	} catch (HibernateException e) {
    	    logger.error(e);
    	    throw new RuntimeException("HibernateException Exception encountered", e);
    	} catch (DgException e) {
    	    logger.error(e);
    	    throw new RuntimeException("DgException Exception encountered", e);
    	} finally {
    	    try {
    		//PersistenceManager.releaseSession(session);
    	    } catch (HibernateException e) {
    		// TODO Auto-generated catch block
    		throw new RuntimeException( "HibernateException Exception encountered", e);

    	    }
    	}

    }

    
    
	public static void assignFieldsPermission(IModel<TreeModel> iTreeModel, IModel<Set<AmpPMReadEditWrapper>> gatesSetModel, IModel<Set<AmpPMReadEditWrapper>> workspacesSetModel) {
		
			Session session = null;
			try {
				session	=	PersistenceManager.getRequestDBSession();
			} catch (DgException e) {
				e.printStackTrace();
			}
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)iTreeModel.getObject().getRoot();
			AmpTreeVisibilityModelBean ampTreeRootObject = (AmpTreeVisibilityModelBean)root.getUserObject();
//			List<PermissionMap> pmList = PMUtil.getOwnPermissionMapListForPermissible(ampTreeRootObject.getAmpObjectVisibility());
			
			Calendar cal = Calendar.getInstance();
			CompositePermission cp = PMUtil.createCompositePermissionForFM(ampTreeRootObject.getAmpObjectVisibility().getName()+" Composite Permission Multiple Assigned " + cal.getTimeInMillis(), gatesSetModel.getObject(), workspacesSetModel.getObject());
			session.save(cp);
			PMUtil.saveFieldsPermission(session, root, cp);
			
	}


	private static void saveFieldsPermission(Session session, DefaultMutableTreeNode root, CompositePermission cp) {
		AmpTreeVisibilityModelBean ampTreeRootObject = (AmpTreeVisibilityModelBean)root.getUserObject();
		if(ampTreeRootObject.getChecked())
			{
				PMUtil.deletePermissionMap(ampTreeRootObject.getAmpObjectVisibility());
				PMUtil.savePermissionMap(session, ampTreeRootObject,cp);
			}
		Enumeration children = root.children();
		while ( children.hasMoreElements() ) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)children.nextElement();
			AmpTreeVisibilityModelBean userObject = (AmpTreeVisibilityModelBean)child.getUserObject();
			saveFieldsPermission(session, child, cp);
		}
		return ;
	}




	private static void deletePermissionMap(AmpObjectVisibility ampObjectVisibility) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session	=	PersistenceManager.getRequestDBSession();
		} catch (DgException e) {
			e.printStackTrace();
		}
		List<PermissionMap> pmList = PMUtil.getOwnPermissionMapListForPermissible(ampObjectVisibility);
		for (PermissionMap permissionMap : pmList) {
			if(permissionMap!=null) {
			    Permission p=permissionMap.getPermission();
			    //we delete the old permissions, if they are dedicated
			    List<PermissionMap> pMapList=null;
				try {
					pMapList = PermissionUtil.getAllPermissionMapsForPermission(p.getId());
				} catch (HibernateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DgException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    if (p!=null && p.isDedicated() && pMapList!=null && pMapList.size() == 1) {
				CompositePermission cp = (CompositePermission)p;
				PMUtil.deleteCompositePermission(cp, session);
			    }
			}
		}
		
	}
	
	
	
	

	
}
