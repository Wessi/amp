/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.admin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.GroupPermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.admin.exception.AdminException;

public class DbUtil {

    private static Logger logger = Logger.getLogger(DbUtil.class);

    public static List getLanguages() throws AdminException {

	Session session = null;
	List languages = null;
	try {
	    session = PersistenceManager.getSession();
	    languages = session.find("from " +
				     Locale.class.getName());
	}
	catch (Exception ex) {
	    logger.debug("Unable to get language list from database", ex);
	    throw new AdminException(
		  "Unable to get language list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return languages;
    }

    public static List getAvailableLanguages() throws AdminException {

	Session session = null;
	List languages = null;
	try {
	    session = PersistenceManager.getSession();
	    Query q = session.createQuery("from " +
					  Locale.class.getName() +
					  " rs where rs.available=true");
	    languages = q.list();
	}
	catch (Exception ex) {
	    logger.debug("Unable to get language list from database", ex);
	    throw new AdminException(
		  "Unable to get language list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return languages;
    }

    public static void createSite(Site site) throws AdminException {
	Session sess = null;
	Transaction tx = null;
	try {
	    sess = PersistenceManager.getSession();
	    tx = sess.beginTransaction();

	    sess.save(site);
	    tx.commit();
	}
	catch (Exception ex) {
	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (HibernateException ex1) {
		    logger.warn("rollback() failed", ex1);
		}
	    }
	    logger.debug("Unable to create site", ex);
	    throw new AdminException(
		  "Unable to create site", ex);
	}
	finally {
	    if (sess != null) {
		try {
		    PersistenceManager.releaseSession(sess);
		}
		catch (Exception ex1) {
		    logger.warn("releaseSession() failed", ex1);
		}
	    }

	}

    }

    public static void editSite(Site site) throws AdminException {
	Session sess = null;
	Transaction tx = null;
	try {
	    sess = PersistenceManager.
		  getSession();
	    tx = sess.beginTransaction();

	    Iterator iter = site.getModuleInstances().iterator();
	    while (iter.hasNext()) {
		ModuleInstance item = (ModuleInstance) iter.next();
		if (item.getSite() == null) {
iter.remove();
		    sess.delete(item);
		}
	    }
	    iter = site.getSiteDomains().iterator();
	    while (iter.hasNext()) {
		SiteDomain item = (SiteDomain) iter.next();
		if (item.getSite() == null) {
iter.remove();
		    sess.delete(item);
		}
	    }

	    iter = site.getGroups().iterator();
	    while (iter.hasNext()) {
		Group item = (Group) iter.next();
		if (item.getSite() == null) {
iter.remove();
		    sess.delete(item);
		}
	    }

	    sess.saveOrUpdate(site);
	    tx.commit();
	}
	catch (Exception ex) {

	    logger.debug("Unable to modify site", ex);

	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (HibernateException ex1) {
		    logger.warn("rollback() failed", ex1);
		}
	    }
	    throw new AdminException(
		  "Unable to modify site", ex);
	}
	finally {
	    if (sess != null) {
		try {
		    PersistenceManager.releaseSession(sess);
		}
		catch (Exception ex1) {
		    logger.warn("releaseSession() failed", ex1);
		}
	    }

	}
    }

    public static Site getSite(Long id) throws AdminException {
	Site site = null;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();
	    site = (Site) session.load(Site.class, id);
	}
	catch (Exception ex) {
	    logger.debug("Unable to get Site ", ex);
	    throw new AdminException("Unable to get Site ", ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed", ex1);
	    }
	}
	return site;
    }

    public static List getMasterInstances(String moduleName, Long siteId) throws
	  AdminException {
	Session session = null;
	List instances = null;

	try {
	    session = PersistenceManager.getSession();
	    Object[] params = {
		  siteId, moduleName};
	    Type[] types = {
		  Hibernate.LONG, Hibernate.STRING};
	    instances = session.find(" from " +
				     ModuleInstance.class.getName() +
		  " i where i.site is not null and i.site.id !=? " +
		  " and i.moduleName = ? " +
		  " and i.realInstance is null",
		  params, types);
	}
	catch (Exception ex) {
	    logger.warn("Unable to get Master Instances from database ", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}

	return instances;
    }

    /**
     * Returns group, read from database
     * @param id group identity
     * @return group, read from database
     * @throws AdminException if any error occurs
     */
    public static Group getGroup(Long id) throws AdminException {
	Group group = null;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();
	    group = (Group) session.load(Group.class, id);

	}
	catch (Exception ex) {
	    logger.debug("Unable to get Group ", ex);
	    throw new AdminException("Unable to get Group ", ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("relseaseSession() failed ", ex1);
	    }
	}
	return group;
    }

    public static void editGroup(Group group) throws AdminException {
	Session sess = null;
	Transaction tx = null;
	try {
	    sess = PersistenceManager.getSession();
	    tx = sess.beginTransaction();

	    Iterator iter = group.getPermissions().iterator();
	    while (iter.hasNext()) {
		GroupPermission item = (GroupPermission) iter.next();
		if (item.getGroup() == null) {
iter.remove();
		    sess.delete(item);
		}
	    }

	    sess.saveOrUpdate(group);
	    tx.commit();
	}
	catch (Exception ex) {
	    logger.debug("Unable to modify Group ", ex);

	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (HibernateException ex1) {}
	    }
	    throw new AdminException(
		  "Unable to modify Group", ex);
	}
	finally {
	    if (sess != null) {
		try {
		    PersistenceManager.releaseSession(sess);
		}
		catch (Exception ex1) {
		    logger.warn("releaseSession() failed ", ex1);
		}
	    }

	}
    }

    public static List getSites() throws AdminException {
	List sites = null;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();
	    sites = session.find("from " + Site.class.getName() +
				 " s order by s.name");
	}
	catch (Exception ex) {
	    logger.debug("Unable to get sites list from database ", ex);
	    throw new AdminException("Unable to get sites list from database ",
				     ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
	return sites;
    }

    /**
     * Return list of child sites
     * @param parentId
     * @return
     * @throws AdminException
     * @deprecated use SiteUtils.getChildSites() instead
     */
    public static List getChildSites(Long parentId) throws AdminException {
	List sites = null;
	try {
	    sites = SiteUtils.getChildSites(parentId.longValue());
	}
	catch (DgException ex) {
	    throw new AdminException(ex);
	}
	return sites;
    }

    public static List getTopLevelSites() throws AdminException {
	List sites = null;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();
	    sites = session.find(" from " + Site.class.getName() +
				 " s where s.parentId is null order by s.name");
	}
	catch (Exception ex) {
	    logger.debug("Unable to get top level sites list from database ",
			 ex);
	    throw new AdminException(
		  "Unable to get top level sites list from database ", ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
	return sites;
    }

    public static List getGroupUsers(Long id) throws AdminException {
	ArrayList users = new ArrayList(); ;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();
	    Group group = (Group) session.load(Group.class, id);
	    Iterator iter = group.getUsers().iterator();
	    while (iter.hasNext()) {
		User user = (User) iter.next();
		users.add(user);
	    }

	}
	catch (Exception ex) {
	    logger.debug("Unable to get Users group ", ex);
	    throw new AdminException("Unable to get Users group ", ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
	return users;
    }

    public static void removeUserFromGroup(Long groupId, Long userId) throws
	  AdminException {
	Session session = null;
	Transaction tx = null;
	try {
	    session = PersistenceManager.getSession();
	    tx = session.beginTransaction();
	    Group group = (Group) session.load(Group.class, groupId);
	    User user = (User) session.load(User.class, userId);
	    user.getGroups().remove(group);
	    tx.commit();
	}
	catch (Exception ex) {
	    logger.debug("Unable to remove User from group ", ex);

	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (HibernateException ex2) {
		    logger.error("rollback() failed ", ex2);
		}
	    }
	    throw new AdminException("Unable to remove User from group ", ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
    }

    public static void addUsersToGroup(Long groupId, Long[] userIds) throws
	  AdminException {
	Session session = null;
	Transaction tx = null;
	try {
	    session = PersistenceManager.getSession();
	    tx = session.beginTransaction();
	    Group group = (Group) session.load(Group.class, groupId);
	    for (int i = 0; i < userIds.length; i++) {
		User user = (User) session.load(User.class, userIds[i]);
		user.getGroups().add(group);
	    }
	    tx.commit();
	}
	catch (Exception ex) {
	    logger.debug("Unable to add Users to group ", ex);
	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (HibernateException ex2) {
		    logger.error("rollback() failed ", ex2);
		}
	    }
	    throw new AdminException("Unable to add Users to group ", ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
    }

    public static List searchSite(String siteKey) throws AdminException {

	Session session = null;
	List siteList = new ArrayList();

	try {
	    session = PersistenceManager.getSession();

	    siteKey = siteKey.toLowerCase();

	    StringTokenizer st = new StringTokenizer(siteKey);
	    String domainUrl = new String("");
	    while (st.hasMoreTokens()) {
		domainUrl += st.nextToken();
	    }

	    siteKey = "%" + siteKey + "%";
	    domainUrl = "%" + domainUrl + "%";

	    Iterator iterator = session.iterate(
		  "select distinct s from " +
		  SiteDomain.class.getName() +
		  " d, " + Site.class.getName() +
		  " s where (d.site.id=s.id) and (lower(s.name) like ? or lower(s.siteId) like ? or lower(d.siteDbDomain) like ? or lower(d.sitePath) like ? or lower(d.siteDbDomain || d.sitePath) like ?)",
new Object[] {
		siteKey, siteKey, siteKey, siteKey, domainUrl

	    }

	    ,
		  new Type[] {
		  Hibernate.STRING, Hibernate.STRING, Hibernate.STRING,
		  Hibernate.STRING, Hibernate.STRING});

	    if (iterator != null && iterator.hasNext()) {
		while (iterator.hasNext()) {
		    siteList.add(iterator.next());
		}
	    }
	}
	catch (Exception ex) {
	    logger.debug("Unable to get site list from database ", ex);
	    throw new AdminException(
		  "Unable to get site list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}
	return siteList;
    }

    public static void deleteSite(Long id) throws AdminException {

	Session session = null;
	Transaction tx = null;
	Site site = null;

	try {
	    session = PersistenceManager.getSession();
	    site = (Site) session.load(Site.class, id);
	    tx = session.beginTransaction();

	    Iterator iterator;

	    if (site.getGroups() != null) {
		iterator = site.getGroups().iterator();
		while (iterator.hasNext()) {
		    Group item = (Group) iterator.next();
		    if (item.getPermissions() != null) {
			Iterator iter = item.getPermissions().iterator();
			while (iter.hasNext()) {
			    GroupPermission gp = (GroupPermission) iter.next();
			    session.delete(gp);
			}
		    }
		    session.delete(item);
		}

	    }

	    iterator = site.getSiteDomains().iterator();
	    while (iterator.hasNext()) {
		SiteDomain item = (SiteDomain) iterator.next();
		session.delete(item);
	    }

	    if (site.getUserLanguages() != null) {
		iterator = site.getUserLanguages().iterator();
		while (iterator.hasNext()) {
		    Locale item = (Locale) iterator.next();
		    session.delete(item);
		}

	    }

	    if (site.getTranslationLanguages() != null) {
		iterator = site.getTranslationLanguages().iterator();
		while (iterator.hasNext()) {
		    Locale item = (Locale) iterator.next();
		    session.delete(item);
		}
	    }

	    if (site.getModuleInstances() != null) {
		iterator = site.getModuleInstances().iterator();
		while (iterator.hasNext()) {
		    ModuleInstance item = (ModuleInstance) iterator.next();
		    session.delete(item);
		}
	    }

	    session.delete(site);

	    tx.commit();

	}
	catch (Exception ex) {
	    logger.debug("Unable to get site list from database ", ex);
	    throw new AdminException(
		  "Unable to get site list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}

    }

    /**
     * Returns site by string id
     * @param siteId site id
     * @return Site object
     * @throws AdminException
     * @deprecated Use SiteUtils.getSite() instead
     */
    public static Site getSite(String siteId) throws AdminException {
	Site site = null;
	try {
	    site = SiteUtils.getSite(siteId);
	}
	catch (DgException ex) {
	    throw new AdminException(ex);
	}
	return site;
    }

    public static Site getSite(String domain, String path) throws
	  AdminException {
	Site site = null;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();
	    Iterator iter = null;
	    if (path == null) {
		Object[] params = {
		      domain};
		Type[] types = {
		      Hibernate.STRING};
		iter = session.iterate("select sd.site from " +
SiteDomain.class.getName() +
" sd where sd.siteDbDomain=?" +
				       " and sd.sitePath is null", params,
				       types);
	    }
	    else {
		Object[] params = {
domain, path};
		Type[] types = {
		      Hibernate.STRING, Hibernate.STRING};
		iter = session.iterate("select sd.site from " +
SiteDomain.class.getName() +
" sd where sd.siteDbDomain=?" +
				       " and sd.sitePath=?", params,
				       types);
	    }
	    while (iter.hasNext()) {
		site = (Site) iter.next();
		break;
	    }
	}
	catch (Exception ex) {
	    logger.debug("Unable to get site from database ", ex);
	    throw new AdminException("Unable to get site from database ", ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
	return site;
    }

    public static ModuleInstance getModuleInstance(Long id) throws
	  AdminException {
	ModuleInstance moduleInstance = null;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();
	    moduleInstance = (ModuleInstance) session.load(ModuleInstance.class,
		  id);

	}
	catch (Exception ex) {
	    logger.debug("Unable to get Module Instance from database ", ex);
	    throw new AdminException(
		  "Unable to get Module Instance from database ", ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
	return moduleInstance;
    }

    public static List getReferencedInstances(Long siteId) throws
	  AdminException {
	List sites = null;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();
	    Object[] params = {
siteId, siteId
	    };
	    Type[] types = {
		  Hibernate.LONG, Hibernate.LONG};
	    sites = session.find(" from " +
				 ModuleInstance.class.getName() +
		  " m where m.site.id != ? and m.realInstance is not null" +
		  " and m.realInstance.site.id = ? " +
		  " order by m.site.name, m.moduleName, m.instanceName",
		  params, types);
	}
	catch (Exception ex) {
	    logger.debug("Unable to get Referenced Instances from database ",
			 ex);
	    throw new AdminException(
		  "Unable to get Referenced Instances from database ", ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
	return sites;
    }

    public static List getSitesToReference(Long siteId, String module) throws
	  AdminException {
	List sites = null;
	Session session = null;
	try {
	    session = PersistenceManager.getSession();
	    Object[] params = {
siteId, module
	    };
	    Type[] types = {
		  Hibernate.LONG, Hibernate.STRING};

	    sites = session.find("select distinct mi.site from " +
				 ModuleInstance.class.getName() +
		  " mi where mi.site.id != ? and mi.moduleName = ? " +
		  " and mi.realInstance is null", params, types);

	}
	catch (Exception ex) {
	    logger.debug("Unable to get sites list from database ", ex);
	    throw new AdminException("Unable to get sites list from database ",
				     ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
	return sites;
    }

    public static void updateSiteInstance(ModuleInstance instance) throws
	  AdminException {
	Session sess = null;
	Transaction tx = null;
	try {
	    sess = PersistenceManager.
		  getSession();
	    tx = sess.beginTransaction();

	    sess.update(instance);
	    tx.commit();
	}
	catch (Exception ex) {

	    logger.debug("Unable to update ModuleInstance ", ex);
	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (HibernateException ex1) {
		    logger.warn("rollback() failed ", ex);
		}
	    }
	    throw new AdminException(
		  "Unable to update ModuleInstance", ex);
	}
	finally {
	    if (sess != null) {
		try {
		    PersistenceManager.releaseSession(sess);
		}
		catch (Exception ex1) {
		    logger.warn("releaseSession() failed ", ex1);
		}
	    }

	}

    }

    public static void editSiteInstances(Site site, List otherInstances) throws
	  AdminException {
	Session sess = null;
	Transaction tx = null;
	try {
	    sess = org.digijava.kernel.persistence.PersistenceManager.
		  getSession();
	    tx = sess.beginTransaction();

	    Iterator iter = otherInstances.iterator();
	    while (iter.hasNext()) {
		ModuleInstance item = (ModuleInstance) iter.next();
		sess.update(item);
	    }

	    iter = site.getModuleInstances().iterator();
	    while (iter.hasNext()) {
		ModuleInstance item = (ModuleInstance) iter.next();
		if (item.getSite() == null) {
		    // Remove permissions, assigned on this instance
            /**
             * @todo implement permission removing here
             */
            iter.remove();
		    sess.delete(item);
		}
	    }

	    sess.saveOrUpdate(site);
	    tx.commit();
	}
	catch (Exception ex) {
	    logger.debug("Unable to modify site ", ex);
	    if (tx != null) {
		try {
		    tx.rollback();
		}
		catch (HibernateException ex1) {
		    logger.warn("rollback() failed ", ex1);
		}
	    }
	    throw new AdminException(
		  "Unable to modify site", ex);
	}
	finally {
	    if (sess != null) {
		try {
		    PersistenceManager.releaseSession(sess);
		}
		catch (Exception ex1) {
		    logger.warn("releaseSession() failed ", ex1);
		}
	    }

	}
    }

    public static List getReferencedGroups(Long siteId) throws AdminException {

	Session session = null;
	List groupList = new ArrayList();

	try {
	    session = PersistenceManager.getSession();
	    StringBuffer buff = new StringBuffer();
	    boolean passed = false;
	    Site site = (Site) session.load(Site.class, siteId);

	    Iterator iter = site.getModuleInstances().iterator();
	    while (iter.hasNext()) {
		ModuleInstance item = (ModuleInstance) iter.next();
		if (item.getRealInstance() == null) {
		    if (passed) {
			buff.append(",");
		    }
		    else {
			passed = true;
		    }
		    buff.append("'").append(item.getModuleInstanceId()).append(
			  "'");
		}
	    }

	    if (passed) {
		GroupPermission p;

		groupList = session.find(
		      "select distinct p.group from " +
		      GroupPermission.class.getName() +
		      " p where p.group.site.id != ? and p.permissionType = ? and p.targetName in (" +
		      buff.toString() + ")",
new Object[] {
		    siteId,
			  new Integer(GroupPermission.
				      MODULE_INSTANCE_PERMISSION)

		}

		,
		      new Type[] {
		      Hibernate.LONG, Hibernate.INTEGER});
	    }
	}
	catch (Exception ex) {
	    logger.debug("Unable to get group list from database ", ex);
	    throw new AdminException(
		  "Unable to get group list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}
	return groupList;
    }

    public static void updateUser(User user) throws
        AdminException {

        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();

            tx = session.beginTransaction();

            session.update(user);

            tx.commit();

        }
        catch (Exception ex) {
            logger.debug("Unable to update user information into database", ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (Throwable cause) {
                    logger.warn("rollback() failed ", cause);
                }
            }
            throw new AdminException(
                "Unable to update user information into database", ex);
        }
    }

    /**
     * Return user
     * @param activeUserId user id
     * @return User object
     * @throws AdminException
     * @deprecated use DgUtil.getUser() instead
     */
    public static User getUser(Long activeUserId) throws
	  AdminException {
	return DgUtil.getUser(activeUserId);
    }

    public static List getInheritedPermissions(Long groupId) throws
	  AdminException {
	Session session = null;
	List result = null;
	try {
	    session = PersistenceManager.getSession();
	    result = getInheritedPermissions(groupId, session);

	}
	catch (Exception ex) {
	    logger.debug("Unable to get permissions from database", ex);
	    throw new AdminException("Unable to get permissions from database",
				     ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.debug("Unable to get permissions from database ", ex2);
		throw new AdminException(
		      "Unable to get permissions from database ", ex2);
	    }
	}
	return result;
    }

    private static List getInheritedPermissions(Long groupId, Session session) throws
	  Exception {
	List permissions = null;

	Object[] params = {
	      Boolean.TRUE, groupId
	};
	Type[] types = {
	      Hibernate.BOOLEAN, Hibernate.LONG};

	permissions = new ArrayList();
	Iterator iter = session.iterate("from " +
					Group.class.getName() +
	      " gr where gr.site.inheritSecurity = ? and gr.parentId = ? ",
	      params, types);
	while (iter.hasNext()) {
	    Group group = (Group) iter.next();
	    permissions.addAll(group.getPermissions());
	    permissions.addAll(getInheritedPermissions(group.getId(), session));
	}

	return permissions;
    }

    public static List getLocales() throws AdminException {

	Session session = null;
	List locales = null;

	try {
	    session = PersistenceManager.getSession();
	    locales = session.find("from " +
				   Locale.class.getName());
	}
	catch (Exception ex) {
	    logger.debug("Unable to get locales list from database", ex);
	    throw new AdminException(
		  "Unable to get locales list from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}

	return locales;
    }

    public static Locale getLocale(String code) throws AdminException {

	Session session = null;
	Locale locale = null;

	try {
	    session = PersistenceManager.getSession();
	    Iterator iterator = session.iterate("from " +
						Locale.class.getName() +
						" loc where (loc.code=?)",
						code, Hibernate.STRING);
	    while (iterator.hasNext()) {
		locale = (Locale) iterator.next();
		break;
	    }

	}
	catch (Exception ex) {
	    logger.debug("Unable to get locale from database", ex);
	    throw new AdminException(
		  "Unable to get locale from database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}

	return locale;
    }

    public static void updateLocale(Locale locale) throws
	  AdminException {

	Transaction tx = null;
	Session session = null;

	try {
	    session = PersistenceManager.getSession();

	    tx = session.beginTransaction();
        if (locale.getMessageLangKey() == null) {
            locale.setMessageLangKey("ln:" + locale.getCode());
        }

	    //Locale oldLocale = (Locale) session.load(Locale.class,  locale.getCode());
	    //oldLocale.setAvailable(locale.isAvailable());

	    session.update(locale);

	    tx.commit();

	}
	catch (Exception ex) {

	    logger.debug("Unable to update locale information into database",
			 ex);

	    if (tx != null) {
		try {
		    tx.rollback();
		}
		catch (Throwable cause) {
		    logger.warn("rollback() failed ", cause);
		}
	    }
	    throw new AdminException(
		  "Unable to update locale information into database", ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed ", ex2);
	    }
	}

    }
    public static List getCommonInstances() throws AdminException {
	List commonInstances = new ArrayList();
	Session session = null;
	Query q =null;
	try {
	    session = PersistenceManager.getSession();
	    q = session.createQuery("from " + ModuleInstance.class.getName() +
				 " m where m.site is null" +
				 " order by m.moduleName");

	    commonInstances = q.list();
	}
	catch (Exception ex) {
	    logger.debug("Unable to get sites list from database ", ex);
	    throw new AdminException("Unable to get sites list from database ",
				     ex);
	}
	finally {
	    try {
		if (session != null) {
		    PersistenceManager.releaseSession(session);
		}
	    }
	    catch (Exception ex1) {
		logger.warn("releaseSession() failed ", ex1);
	    }
	}
	return commonInstances;
    }

    public static void editCommonInstances(List newInstances) throws
	  AdminException {
	Session session = null;
	Transaction tx = null;
	try {
	    session = org.digijava.kernel.persistence.PersistenceManager.
		  getSession();
	    tx = session.beginTransaction();

	    List commonInstances = getCommonInstances();

	    HashMap oldInstancesMap = new HashMap();
	    Iterator iter = commonInstances.iterator();
	    while (iter.hasNext()) {
		ModuleInstance instance = (ModuleInstance) iter.next();
		oldInstancesMap.put(instance.getModuleInstanceId(), instance);
	    }

	    HashMap newInstancesMap = new HashMap();
	    iter = newInstances.iterator();
	    while (iter.hasNext()) {
		ModuleInstance instance = (ModuleInstance) iter.next();
		newInstancesMap.put(instance.getModuleInstanceId(), instance);
	    }

	    if (newInstancesMap.size() <= oldInstancesMap.size()) {
		iter = commonInstances.iterator();
		while (iter.hasNext()) {
		    ModuleInstance oldInstance = (ModuleInstance) iter.next();
		    ModuleInstance newInstance = (ModuleInstance) newInstancesMap.
			  get(
			  oldInstance.getModuleInstanceId());

		    if (newInstance != null) {
			session.update(newInstance);
		    }
		    else {
			session.delete(oldInstance);
		    }
		}
	    } else {
		iter = newInstances.iterator();
		while (iter.hasNext()) {
		    ModuleInstance newInstance = (ModuleInstance) iter.next();
		    ModuleInstance oldInstance = (ModuleInstance) oldInstancesMap.
			  get(
			  newInstance.getModuleInstanceId());

		    if (oldInstance != null) {
			session.update(newInstance);
		    }
		    else {
			session.save(newInstance);
		    }
		}

	    }

	    tx.commit();
	}
	catch (Exception ex) {
	    logger.debug("Unable to modify site ", ex);
	    if (tx != null) {
		try {
		    tx.rollback();
		}
		catch (HibernateException ex1) {
		    logger.warn("rollback() failed ", ex1);
		}
	    }
	    throw new AdminException(
		  "Unable to modify site", ex);
	}
	finally {
	    if (session != null) {
		try {
		    PersistenceManager.releaseSession(session);
		}
		catch (Exception ex1) {
		    logger.warn("releaseSession() failed ", ex1);
		}
	    }

	}
    }

}
