/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.Set;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMViewGlobalPermsTableFeaturePanel;
import org.digijava.module.gateperm.core.Permission;

/**
 * 
 * @author dan 
 */

public class AmpPMManageGlobalPanel extends AmpComponentPanel {
	
	
	
	public AmpPMManageGlobalPanel(String id,  IModel<Set<Permission>> globalPermissionsModel, String fmName, boolean hideLabel) throws Exception {
		super(id, fmName);
		super.setOutputMarkupId(true);
		
		AmpPMAssignGlobalPermissionComponentPanel assignGlobalPerm = new AmpPMAssignGlobalPermissionComponentPanel("assignGlobalPermission", globalPermissionsModel,"Assign Global Permission");
		assignGlobalPerm.setOutputMarkupId(true);
		add(assignGlobalPerm);
		
		AmpPMViewGlobalPermsTableFeaturePanel globalPermsTable = new AmpPMViewGlobalPermsTableFeaturePanel("viewGlobalPerms", globalPermissionsModel, "View Existing Global Permissions", true);
		globalPermsTable.setOutputMarkupId(true);
		add(globalPermsTable);
		add(new PagingNavigator("globalPermsNavigator", (PageableListView)globalPermsTable.getList()));
		
	}

}