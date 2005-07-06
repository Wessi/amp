/*
 *   GroupPermissionsForm.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 20, 2003
 * 	 CVS-ID: $Id: GroupPermissionsForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.module.admin.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.Collection;
import org.digijava.kernel.entity.Locale;
import java.util.ArrayList;
import java.util.List;
import org.digijava.kernel.util.SiteConfigUtils;
import java.util.HashMap;
import java.util.Collections;
import java.util.HashSet;
import org.digijava.kernel.request.SiteDomain;
import java.util.Iterator;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.ResourcePermission;

public class GroupPermissionsForm
    extends ActionForm {

    private Long groupId;
    private String groupName;
    private String siteName;
    private Collection resources;
    private ArrayList permissions;
    private static final Collection actions;
    private Collection foreignPerms;
    private Collection inheritedPermissions;

    static {
        actions = new ArrayList();
        actions.add(ResourcePermission.READ);
        actions.add(ResourcePermission.WRITE);
        actions.add(ResourcePermission.ADMIN);
        actions.add(ResourcePermission.TRANSLATE);
    }

    public static class PermissionInfo {
        private long id;
        private String resourceId;
        private String action;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public String getResourceId() {
            return resourceId;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    public static class ForeignPermissionInfo {
        private Site site;
        private String module;
        private String instance;
        private String actions;

        public Site getSite() {
            return site;
        }

        public void setSite(Site site) {
            this.site = site;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public String getInstance() {
            return instance;
        }

        public void setInstance(String instance) {
            this.instance = instance;
        }

        public String getActions() {
            return actions;
        }

        public void setActions(String actions) {
            this.actions = actions;
        }
    }

    public static class Resource {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        groupId = null;
        siteName = null;
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {
        return null;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Collection getResources() {
        return resources;
    }

    public void setResources(Collection resources) {
        this.resources = resources;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public ArrayList getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList permissions) {
        this.permissions = permissions;
    }

    /**
     * Returns one permission. This methid is created to support indexed
     * property
     * @param index permission index
     * @return one permission
     */
    public PermissionInfo getPermission(int index) {
        PermissionInfo info = null;
        int actualSize = permissions.size();
        if (index >= actualSize) {
            // Expand the list
            for (int i = 0; i <= index - actualSize; i++) {
                permissions.add(new PermissionInfo());
            }
        }

        return (PermissionInfo) permissions.get(index);
    }

    public Collection getActions() {
        return actions;
    }

    public Collection getForeignPerms() {
        return foreignPerms;
    }

    public void setForeignPerms(Collection foreignPerms) {
        this.foreignPerms = foreignPerms;
    }

    public Collection getInheritedPermissions() {
        return inheritedPermissions;
    }

    public void setInheritedPermissions(Collection inheritedPermissions) {
        this.inheritedPermissions = inheritedPermissions;
    }

}