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

package org.digijava.kernel.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.entity.UserPreferencesPK;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.security.principal.UserPrincipal;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;

/**
 * This class containts user-related utillity functions. User must be
 * <b>always</b> identified by User object
 */
public class UserUtils {
    private static Logger logger = Logger.getLogger(UserUtils.class);

    /**
     * Compares users by identifiers
     */
    public static final Comparator USER_COMPARATOR;


    static {
        USER_COMPARATOR = new Comparator() {
            public int compare(Object o1, Object o2) {
                User user1 = (User)o1;
                User user2 = (User)o2;

                return user1.getId().compareTo(user2.getId());
            }
        };
    }
    /**
     * Returns user preferences for particular site. null, if there are no
     * preferences defined for this user and site
     * @param user User, for which preferences are defined
     * @param site Site, which is the owner of user preferences
     * @throws DgException if error occurs.
     * @return UserPreferences
     */
    public static UserPreferences getUserPreferences(User user, Site site) throws
	  DgException {

	logger.debug("Searching user preferences for user#" + user.getId() +
		     " site#" + site.getId());

	UserPreferences result = null;
	net.sf.hibernate.Session session = null;

	UserPreferencesPK key = new UserPreferencesPK(user, site);

	try {
	    session = PersistenceManager.getSession();
	    result = (UserPreferences) session.load(UserPreferences.class, key);

	}
	catch (ObjectNotFoundException ex) {
	    logger.debug("User preferences does not exist");
	}
	catch (Exception ex) {
	    logger.warn("Unable to get user preferences", ex);
	    throw new DgException(ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return result;
    }

    /**
     * Returns user language preferences for particular site. null, if there
     * are no preferences defined for this user and site
     * @param user User, for which preferences are defined
     * @param site Site, which is the owner of user preferences
     * @throws DgException if error occurs.
     * @return UserLangPreferences
     */
    public static UserLangPreferences getUserLangPreferences(User user,
	  Site site) throws
	  DgException {

	logger.debug("Searching user language preferences for user#" +
user.getId() +
		     " site#" + site.getId());

	UserLangPreferences result = null;
	net.sf.hibernate.Session session = null;

	UserPreferencesPK key = new UserPreferencesPK(user, site);

	try {
	    session = PersistenceManager.getSession();
	    result = (UserLangPreferences) session.load(UserLangPreferences.class,
		  key);

	}
	catch (ObjectNotFoundException ex) {
	    logger.debug("User language preferences do not exist");
	}
	catch (Exception ex) {
	    logger.warn("Unable to get user language preferences", ex);
	    throw new DgException(ex);
	}
	finally {
	    try {
		PersistenceManager.releaseSession(session);
	    }
	    catch (Exception ex2) {
		logger.warn("releaseSession() failed", ex2);
	    }
	}

	return result;
    }

    /**
     * Store user preferences
     * @param preferences UserPreferences user preferences object. Must have id
     * property initialized
     * @throws DgException
     */
    public static void saveUserPreferences(UserPreferences preferences) throws
	  DgException {
	logger.debug("Saving user preferences for user#" +
		     preferences.getId().getUser().getId() +
		     " site#" + preferences.getId().getSite().getId());
	Session sess = null;
	Transaction tx = null;
	try {
	    sess = PersistenceManager.
		  getSession();
	    UserPreferences existing = null;
	    try {
		existing = (UserPreferences) sess.load(UserPreferences.class,
		      preferences.getId());
		logger.debug("Updating");
	    }
	    catch (ObjectNotFoundException ex2) {
		logger.debug("Creating new record");
	    }
	    tx = sess.beginTransaction();

	    if (existing == null) {
		sess.save(preferences);
	    }
	    else {
		// Let's forget about old one
		sess.evict(existing);
		// Save new one
		sess.update(preferences);
	    }
	    tx.commit();
	}
	catch (Exception ex) {

	    logger.error("Unable to update user preferences ", ex);
	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (HibernateException ex1) {
		    logger.warn("rollback() failed ", ex);
		}
	    }
	    throw new DgException(
		  "Unable to update user preferencese", ex);
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

    /**
     * Returns user's biography for particular site
     * @param user User
     * @param site Site
     * @throws DgException If error occurs
     * @return user's biography for particular site
     */
    public static String getUserBiography(User user, Site site) throws
	  DgException {
	UserPreferences up = getUserPreferences(user, site);
	if (up == null) {
	    return null;
	}
	else {
	    return up.getBiography();
	}
    }

    /**
     * Returns URL of user's photo to be displayed on the current site. The
     * current site (and its domain) is determined from the request. Returns
     * null if the current user does not have image
     * @param user User
     * @param request HttpServletRequest
     * @return String
     */
    public static String getUserImageUrl(User user, HttpServletRequest request) {
	/**
	 * @todo Refactor these metod when RequestUtils class will be ready
	 */
	SiteDomain siteDomain = RequestUtils.getSiteDomain(request);
	String siteUrl = DgUtil.getSiteUrl(siteDomain, request);

	if (user.getPhoto() == null) {
	    logger.debug("user#" + user.getId() + " does not have image");
	    return null;
	}
	else {
	    String imageUrl = siteUrl + "/showImage.do?id=" +
		  user.getPhoto().getId();

	    logger.debug("Image URL for user#" + user.getId() +
			 " for the current site is: " + imageUrl);

	    return imageUrl;
	}
    }

    /**
     * Store user's language preferences
	   * @param preferences UserLangPreferences user preferences object. Must have id
     * property initialized
     * @throws DgException
     */
    public static void saveUserLangPreferences(UserLangPreferences preferences) throws
	  DgException {
	logger.debug("Saving user's language preferences for user#" +
		     preferences.getId().getUser().getId() +
		     " site#" + preferences.getId().getSite().getId());
	Session sess = null;
	Transaction tx = null;
	try {
	    sess = PersistenceManager.
		  getSession();
	    UserLangPreferences existing = null;
	    try {
		existing = (UserLangPreferences) sess.load(UserLangPreferences.class,
		      preferences.getId());
		logger.debug("Updating");
	    }
	    catch (ObjectNotFoundException ex2) {
		logger.debug("Creating new record");
	    }
	    tx = sess.beginTransaction();

	    if (existing == null) {
		sess.save(preferences);
	    }
	    else {
		// Let's forget about old one
		sess.evict(existing);
		// Save new one
		sess.update(preferences);
	    }
	    tx.commit();
	}
	catch (Exception ex) {

	    logger.error("Unable to update user's language preferences ", ex);
	    if (tx != null) {
		try {
tx.rollback();
		}
		catch (HibernateException ex1) {
		    logger.warn("rollback() failed ", ex);
		}
	    }
	    throw new DgException(
		  "Unable to update user's language preferencese", ex);
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

    /**
     * Gets User by scpecified id
     * @param id  id of user to be retrieved
     * @return User by specified id, null of no user by that id was found
     */
    public static User getUser(Long id) {
	User result = null;
	Session session = null;

	try {
	    session = PersistenceManager.getRequestDBSession();
	    result = (User) session.load(User.class, id);
	    ProxyHelper.initializeObject(result);
	}

	catch (ObjectNotFoundException ex) {
	    logger.debug("Unable to get User");
	    return null;
	}
	catch (Exception ex) {
	    logger.warn("Unable to get User", ex);
        throw new RuntimeException(ex);
	}
	return result;

    }

    /**
     * Searchs users with given criteria
     * @param criteria criteria by which users are searched
     * @return list of users matching given criteria
     * @throws DgException
     */
    public static List searchUsers(String criteria) throws DgException {
	return searchUsers(criteria, null);
    }

    /**
     * Searchs users with given criteria and sorts users list according to orderBy parameter
     * @param criteria criteria by which users are searched
     * @param orderBy list of User object properties by which users list should be ordered
     * @return list of users matching given criteria
     * @throws DgException
     */
    public static List searchUsers(String criteria, String orderBy[]) throws
	  DgException {

	Session session = null;
	List userList;

	try {

	    session = PersistenceManager.getRequestDBSession();

	    String criteriaLow = criteria.trim().toLowerCase();
	    String criteriaLowConcat = new String("");

	    StringTokenizer st = new StringTokenizer(criteriaLow);
	    while (st.hasMoreTokens()) {
		criteriaLowConcat += st.nextToken();
	    }
	    criteriaLow = "%" + criteriaLow + "%";
	    criteriaLowConcat = "%" + criteriaLowConcat + "%";

	    String queryString = "from usr in class " + User.class.getName() +
		  " where usr.email like ? or lower(usr.firstNames) like ? or lower(usr.lastName) like ? or lower(usr.firstNames || usr.lastName) like ?";

	    if (orderBy != null && orderBy.length != 0) {
		String orderStr = " order by " + "usr." + orderBy[0];
		for (int i = 1; i < orderBy.length; i++) {
		    orderStr += ", " + "usr." + orderBy[i];
		}

		queryString += orderStr;
	    }

	    userList = session.find(queryString,
				    new Object[] {
				    criteriaLow, criteriaLow, criteriaLow,
				    criteriaLowConcat

	    }

, new Type[] {
		  Hibernate.STRING, Hibernate.STRING, Hibernate.STRING,
		  Hibernate.STRING});

	    Iterator userIter = userList.iterator();
	    while (userIter.hasNext()) {
		User user = (User) userIter.next();
		ProxyHelper.initializeObject(user);
	    }

	}

	catch (Exception ex) {
	    logger.debug("Unable to get users list from database ", ex);
	    throw new DgException(
		  "Unable to get users list from database", ex);
	}

	return userList;
    }

    /**
     * Creates and returns Subject for the given user
     * @param user User
     * @return Subject for the given user
     */
    public static Subject getUserSubject(User user) {
        return fillUserSubject(null, user);
    }

    /**
     * Fills given subject with user's information and returns it. If subject
     * is null, new instance is created
     * @param subject Subject user's subject
     * @param user User
     * @return Subject, filled with user's information
     */
    public static Subject fillUserSubject(Subject subject, User user) {
        if (subject == null) {
            subject = new Subject();
        }
        subject.getPrincipals().clear();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        subject.getPrincipals().add(userPrincipal);

        Iterator iter = user.getGroups().iterator();
        while (iter.hasNext()) {
            Group group = (Group) iter.next();
            GroupPrincipal groupPrincipal = new GroupPrincipal(group);
            subject.getPrincipals().add(groupPrincipal);
        }
/*
        if (user.isGlobalAdmin()) {
            GlobalAdminGroup adminGroup = new GlobalAdminGroup();
            adminGroup.addMember(userPrincipal);
            subject.getPrincipals().add(adminGroup);
        }
*/
        subject.getPublicCredentials().clear();
        subject.getPrivateCredentials().clear();

        return subject;

    }

    /**
     * Searches user object by email and returns it. If such user does not
     * exists, returns null
     * @param email String User email
     * @return User object
     * @throws DgException if error occurs
     */
    public static User getUserByEmail(String email) throws DgException {
        User user = null;
        Session sess = null;
        try {
            sess = PersistenceManager.getRequestDBSession();

            Iterator iter = sess.find("from rs in class " +
                                         User.class.getName() +
                                         " where rs.email =? ", email,
                                         Hibernate.STRING).iterator();
            while (iter.hasNext()) {
                user = (User) iter.next();
                ProxyHelper.initializeObject(user);
                break;
            }

        }
        catch (Exception ex0) {
            logger.debug("Unable to get user from database", ex0);
            throw new DgException(
                "Unable to get user information from database", ex0);
        }

        return user;
    }

    /**
     * Put new password in user object. This method does not perform any
     * database modifications. It simply puts new password and salt in object's
     * fields
     * @param user User
     * @param password String new password
     */
    public static void setPassword(User user, String password) {
        user.setPassword(ShaCrypt.crypt(password.trim()).trim());
        user.setSalt(new Long(password.trim().hashCode()).toString());
    }
}