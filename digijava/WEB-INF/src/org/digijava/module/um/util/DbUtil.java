/*
 *   DbUtil.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created:
 * 	 CVS-ID: $Id: DbUtil.java,v 1.3 2007-01-23 17:33:50 steve Exp $
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
package org.digijava.module.um.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.ContentAlert;
import org.digijava.kernel.entity.HowDidYouHear;
import org.digijava.kernel.entity.Image;
import org.digijava.kernel.entity.Interests;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.OrganizationType;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.ProxyHelper;
import org.digijava.kernel.util.ShaCrypt;
import org.digijava.kernel.util.UnixCrypt;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.um.dbentity.ResetPassword;
import org.digijava.module.um.exception.UMException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;
import net.sf.hibernate.ObjectNotFoundException;
import java.util.Date;
import org.digijava.kernel.util.RequestUtils;
import net.sf.hibernate.Query;
import java.util.*;


public class DbUtil {
    private static Logger logger = Logger.getLogger(DbUtil.class);

    public static boolean isResetPermitted(String email, String code,
                                           String resetPassword) throws
        UMException {
        boolean permitted = false;
        Session sess = null;
        User userobj = new User();

        Transaction tx = null;

        try {
            sess = PersistenceManager.getSession();

            Iterator iter = sess.iterate(
                "select ur from ResetPassword rs,User ur where ur.email='" +
                email + "' and ur.id=rs.userId and rs.code='" + code + "'");

            //////////
            User iterUser = null;
            if (iter.hasNext()) {
                permitted = true;
                iterUser = (User) iter.next();
                tx = sess.beginTransaction();
                iterUser.setPassword(resetPassword);
                sess.save(iterUser);
                tx.commit();

            }

        }
        catch (Exception ex) {

            logger.debug("Unable to update user information into database",ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed",ex1);
                }
            }
            throw new UMException(
                "Unable to update user information into database", ex);

        }
        finally {
            if (sess != null) {
                try {
                    PersistenceManager.releaseSession(sess);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed",ex1);
                }
            }

        }

        return permitted;
    }

    public static long getuserId(String email) throws UMException {
        long userId = 0;
        Session sess = null;
        boolean iscorrect = false;
        try {
            sess = org.digijava.kernel.persistence.PersistenceManager.
                getSession();

            Iterator iter = sess.iterate("from rs in class " +
                                         User.class.getName() +
                                         " where rs.email =? ", email,
                                         Hibernate.STRING);
            while (iter.hasNext()) {
                User iterUser = (User) iter.next();
                userId = iterUser.getId().longValue();
                break;
            }

        }
        catch (Exception ex0) {
            logger.debug("Unable to get user information from database",ex0);
            throw new UMException("Unable to get user information from database",ex0);
        }
        finally {
            if (sess != null) {
                try {
                    PersistenceManager.releaseSession(sess);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed",ex1);
                }
            }

        }

        return userId;
    }

    /**
     * check if password is Correct in database
     * @param user
     * @param password
     * @return
     * @throws ServletException
     */
    public static boolean isCorrectPassword(String user, String pass) throws
        UMException {
        Session sess = null;
        String compare = null;
        String encryptPassword = null;
        boolean iscorrect = false;
        try {
            sess = org.digijava.kernel.persistence.PersistenceManager.
                getSession();

            Iterator iter = sess.iterate("from rs in class " +
                                         User.class.getName() +
                                         " where rs.email =? ", user,
                                         Hibernate.STRING);
//////////////////////
            while (iter.hasNext()) {
                User iterUser = (User) iter.next();


                for (int i = 0; i < 3; i++) {

                        switch (i) {
                            case 0:

                                compare = pass.trim() + iterUser.getSalt();

                                // first try new user ( using SHA1 )
                                encryptPassword = ShaCrypt.crypt(compare.trim()).trim().toUpperCase();
                                break;

                            case 1:

                                compare = pass.trim();

                                // first try new user ( using SHA1 )
                                encryptPassword = ShaCrypt.crypt(compare.trim()).trim();
                                break;

                            case 2:
                                // second try old user ( using unix crypt )
                                if (!pass.startsWith("8x"))
                                    encryptPassword = UnixCrypt.crypt("8x", pass.trim()).
                                        trim();
                                else
                                    encryptPassword = pass.trim();
                                break;
                        }

                    // check user in database
                    if ( encryptPassword.equalsIgnoreCase(iterUser.getPassword().trim())) {
                        iscorrect = true;
                        break;
                    }
                }

            }

/////////////////////

        }
        catch (Exception ex0) {
            logger.debug("isCorrectPassword() failed",ex0);
            throw new UMException(ex0.getMessage(),ex0);
        }
        finally {
            if (sess != null) {
                try {
                    PersistenceManager.releaseSession(sess);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed ",ex1);
                }
            }

        }

        return iscorrect;
    }

/**
     * Reset Password in Database
     * @param email
     * @param newPassword
     * @param confirmPassword
     * @throws ServletException
     * @deprecated Please, do not use this silly method
     */
    public static void ResetPassword(String email, String newPassword) throws
        UMException {
        User userobj = new User();

        Transaction tx = null;
        Session session = null;
        try {
            session = org.digijava.kernel.persistence.PersistenceManager.
                getSession();
            Iterator iter = session.iterate("from rs in class " +
                                            User.class.getName() +
                                            " where rs.email =? ", email,
                                            Hibernate.STRING);
            User iterUser = null;
            while (iter.hasNext()) {
                iterUser = (User) iter.next();

            }
            tx = session.beginTransaction();
            iterUser.setPassword(ShaCrypt.crypt(newPassword.trim()).trim());
            iterUser.setSalt(new Long(newPassword.trim().hashCode()).toString());
            session.save(iterUser);
            tx.commit();

        }
        catch (Exception ex) {
            logger.debug("Unable to update user information into database",ex);

           if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed ",ex1);
                }
            }
            throw new UMException(
                "Unable to update user information into database", ex);
        }
        finally {
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed ",ex1);
                }
            }
        }

    }
    /**
     * Reset Password in Database
     * @param email
     * @param newPassword
     * @param confirmPassword
     * @throws ServletException
     */
    public static boolean ResetPassword(String email, String code, String newPassword) throws
        UMException {
        User userobj = new User();

        Transaction tx = null;
        Session session = null;
        try {
            session = org.digijava.kernel.persistence.PersistenceManager.
                getSession();
            tx = session.beginTransaction();
            Iterator iter = session.iterate("from rs in class " +
                                            User.class.getName() +
                                            " where rs.email =? ", email,
                                            Hibernate.STRING);
            User iterUser = null;
            while (iter.hasNext()) {
                iterUser = (User) iter.next();
                break;
            }
            if (iterUser == null) {
                logger.warn("Attempt to reset password for unknown user: " + email);
                return false;
            }

            ResetPassword resetPassword = null;
            try {
                resetPassword = (ResetPassword) session.load(ResetPassword.class,
                    iterUser.getId());
            }
            catch (ObjectNotFoundException ex2) {
                logger.warn("User " + email + " have not requested password reset change");
                return false;
            }

            if (!resetPassword.getCode().equals(code)) {
                logger.error("Invalid password request code");
                return false;
            }

            iterUser.setPassword(ShaCrypt.crypt(newPassword.trim()).trim());
            iterUser.setSalt(new Long(newPassword.trim().hashCode()).toString());
            session.update(iterUser);
            session.delete(resetPassword);
            tx.commit();

        }
        catch (Exception ex) {
            logger.debug("Unable to update user information into database",ex);

           if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed ",ex1);
                }
            }
            throw new UMException(
                "Unable to update user information into database", ex);
        }
        finally {
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed ",ex1);
                }
            }
        }
        return true;

    }

    /**
     * Update password in database see table
     *
     * @param user
     * @throws ServletException
     */
    public static void updatePassword(String user, String oldPassword,
                                      String newPassword) throws
        UMException {

        User userobj = new User();

        Transaction tx = null;
        Session session = null;
        try {
            session = org.digijava.kernel.persistence.PersistenceManager.
                getSession();
            Iterator iter = session.iterate("from rs in class " +
                                            User.class.getName() +
                                            " where rs.email =? ", user,
                                            Hibernate.STRING);
            User iterUser = null;
            while (iter.hasNext()) {
                iterUser = (User) iter.next();
            }
            tx = session.beginTransaction();
            iterUser.setPassword(ShaCrypt.crypt(newPassword.trim()).trim());
            iterUser.setSalt(new Long(newPassword.trim().hashCode()).toString());
            session.save(iterUser);

            tx.commit();

        }
        catch (Exception ex) {
            logger.debug("Unable to update user information into database", ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed ",ex1);
                }
            }
            throw new UMException(
                "Unable to update user information into database", ex);
        }
        finally {
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed ",ex1);
                }
            }
        }
    }

    /**
     * Updates user biography in Database
     * @param id
     * @param image
     * @param bio
     * @throws ServletException
     */
    public static Image getImage(long id) throws UMException {

        Session sess = null;
        Image iterImage = null;
        try {
            sess = org.digijava.kernel.persistence.PersistenceManager.
                getSession();

            iterImage = (Image) sess.load(Image.class, new Long(id));
            ProxyHelper.initializeObject(iterImage);
        }
        catch (Exception ex0) {
            logger.debug("Unable to get Image from database ",ex0);
            throw new UMException("Unable to get Image from database ",ex0);
        }
        finally {
            if (sess != null) {
                try {
                    PersistenceManager.releaseSession(sess);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed ",ex1);
                }
            }

        }

        return iterImage;
    }


    /**
     * Update user bio in database
     *
     * @param user object
     * @throws UMException
     */
    public static void updateUserBio(User user) throws
        UMException {

        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update user information into database",ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (Throwable cause) {
                    logger.warn("rollback() failed ",cause);
                }
            }
            throw new UMException(
                "Unable to update user information into database", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("eleaseSession() failed ",ex2);
            }
        }
        try {
            UserUtils.saveUserPreferences(user.getUserPreference());
            UserUtils.saveUserLangPreferences(user.getUserLangPreferences());
        }
        catch (DgException ex1) {
            throw new UMException(ex1);
        }
    }


    /**
     * Update user bio in database
     *
     * @param user object
     * @throws UMException
     */
    public static void updateUserMarket(User user) throws
        UMException {

        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update user information into database",ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (Throwable cause) {
                    logger.warn("rollback() failed ",cause);
                }
            }
            throw new UMException(
                "Unable to update user information into database", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("eleaseSession() failed ",ex2);
            }
        }
        try {
            UserUtils.saveUserPreferences(user.getUserPreference());
            UserUtils.saveUserLangPreferences(user.getUserLangPreferences());
        }
        catch (DgException ex1) {
            throw new UMException(ex1);
        }
    }

    /**
     * Update user in database
     *
     * @param user object
     * @throws UMException
     */
    public static void updateUser(User user) throws
        UMException {

        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            tx = session.beginTransaction();
            ArrayList removeArray = new ArrayList();

            if( user.getInterests() != null ) {
                Iterator iter = user.getInterests().iterator();
                while ( iter.hasNext() ) {
                    Interests item = ( Interests ) iter.next();

                    List list = getGeoupsBySiteId( item.getSite().getId() );
                    Iterator iterGroups = list.iterator();
                    while ( iterGroups.hasNext() ) {
                        Group group = ( Group ) iterGroups.next();
                        if ( group.isMemberGroup() ) {
                            if ( item.getUser() != null ) {
                                org.digijava.module.admin.util.DbUtil.
                                    addUsersToGroup(
                                    group.getId(), new Long[] {user.getId()} );
                            }
                            else {
                                org.digijava.module.admin.util.DbUtil.
                                    removeUserFromGroup(
                                    group.getId(), user.getId() );
                            }
                        }
                    }

                    if ( item.getUser() == null ) {
                        session.delete( item );
                        removeArray.add( item );
                    }
                }
            }

            if( removeArray.size() > 0 )
                user.getInterests().removeAll(removeArray);

            session.saveOrUpdate(user);

            tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update user information into database",ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (Throwable cause) {
                    logger.warn("rollback() failed ",cause);
                }
            }
            throw new UMException(
                "Unable to update user information into database", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("eleaseSession() failed ",ex2);
            }
        }
        try {
            UserUtils.saveUserPreferences(user.getUserPreference());
            UserUtils.saveUserLangPreferences(user.getUserLangPreferences());
        }
        catch (DgException ex1) {
            throw new UMException(ex1);
        }
    }

    public static void registerUser(User user) throws UMException {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateClassLoader.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // set encrypted password
            user.setPassword(ShaCrypt.crypt(user.getPassword().trim()).trim());

            // set hashed password
            user.setSalt(new Long(user.getPassword().trim().hashCode()).
                         toString());

            // update user
            session.save(user);

            // update user preference
            if( user.getUserPreference() != null ) {
                session.save(user.getUserPreference());
            }

            // update user language preferences
            if( user.getUserLangPreferences() != null ) {
                session.save(user.getUserLangPreferences());
            }

            tx.commit();

            // Is becoming a member of Member group of corresponding site
            if( user.getInterests() != null ) {
                Iterator iter = user.getInterests().iterator();
                while (iter.hasNext()) {
                    Interests item = (Interests) iter.next();

                    List list = getGeoupsBySiteId(item.getSite().getId());
                    Iterator iterGroups = list.iterator();
                    while (iterGroups.hasNext()) {
                        Group group = (Group) iterGroups.next();
                        if (group.isMemberGroup()) {
                            if (item.getUser() != null) {
                                org.digijava.module.admin.util.DbUtil.
                                    addUsersToGroup(
                                    group.getId(), new Long[] {user.getId()});
                            }
                        }
                    }
                }
            }
            // ------------------------------------------------------------

        }
        catch (Exception ex) {
            logger.debug("Unable to update user information into database", ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed ",ex1);
                }
            }
            throw new UMException(
                "Unable to update user information into database", ex);
        }
        finally {
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed ",ex1);
                }
            }

        }

    }

    public static boolean isRegisteredEmail(String email) throws
        UMException {
        Session sess = null;
        boolean iscorrect = false;
        try {
            sess = PersistenceManager.getSession();

            Iterator iter = sess.iterate("from rs in class " +
                                         User.class.getName() +
                                         " where lower(rs.email)=? ", email,
                                         Hibernate.STRING);
            if (iter.hasNext()) {
                iscorrect = true;
            }
        }
        catch (Exception ex0) {
            logger.debug("isRegisteredEmail() failed",ex0);
            throw new UMException(ex0.getMessage(),ex0);
        }
        finally {
            if (sess != null) {
                try {
                    PersistenceManager.releaseSession(sess);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed ",ex1);
                }
            }

        }

        return iscorrect;
    }


    public static void saveResetPassword(long userId, String code) throws
        UMException {
        Transaction tx = null;
        Session session = null;
        try {
            session = org.digijava.kernel.persistence.PersistenceManager.getSession();
            tx = session.beginTransaction();

            ResetPassword resetPassword;
            boolean create = true;
            try {
                resetPassword = (ResetPassword) session.load(ResetPassword.class, new Long(userId));
                create = false;
            }
            catch (ObjectNotFoundException ex2) {
                resetPassword = new ResetPassword();
                resetPassword.setUserId(userId);
            }
            resetPassword.setCode(code);
            resetPassword.setResetDate(new Date());

            if (create) {
                session.save(resetPassword);
            } else {
                session.update(resetPassword);
            }

            tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to put reset password record into database",ex);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed ",ex1);
                }
            }
            throw new UMException(
                "Unable to put reset password record into database", ex);
        }
        finally {
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed ",ex1);
                }
            }

        }

    }


    /**
     * Get user preference object by given user and site
     *
     * @param user
     * @param site
     * @return
     * @throws UMException
     */
    public static UserPreferences getUserPreferences(User user, Site site) throws
        UMException {
        long userId = 0;
        Session session = null;
        UserPreferences userPreferences = null;
        try {
            session = org.digijava.kernel.persistence.PersistenceManager.
                getSession();

            Object[] params = {
                user.getId(), site.getId()};
            Type[] paramTypes = {
                Hibernate.LONG, Hibernate.LONG};
            Iterator iter = session.iterate("from p in class " +
                                            UserPreferences.class.getName() +
                " where p.user.id =? and p.site.id=?", params,
                paramTypes);
            while (iter.hasNext()) {
                userPreferences = (UserPreferences) iter.next();
                break;
            }

        }
        catch (Exception ex0) {
            logger.debug("Can not load user preferences",ex0);
            throw new UMException("Can not load user preferences", ex0);
        }
        finally {
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed ",ex1);
                }
            }

        }

        return userPreferences;
    }

    /**
     * Get Countrie list from DB
     *
     * @return
     * @throws UMException
     */
    public static List getCountries() throws UMException {
        return getList(Country.class.getName(), "countryName");
    }

    /**
     * Get Organization types from DB
     *
     * @return
     * @throws UMException
     */
    public static List getOrganizationTypes() throws UMException {
        return getList(OrganizationType.class.getName(), "type");
    }

    /**
     * Get Content Alerts from DB
     *
     * @return
     * @throws UMException
     */
    public static List getContentAlerts() throws UMException {
        return getList(ContentAlert.class.getName(), null);
    }

    /**
     * Get How Did you hear list from DB
     *
     * @return
     * @throws UMException
     */
    public static List getHowDidYouHear() throws UMException {
        return getList(HowDidYouHear.class.getName(), null);
    }

    /**
     * Get Languages from DB
     *
     * @return
     * @throws UMException
     */
    public static List getLanguages() throws UMException {
        return getList(Locale.class.getName(), null);
    }

    /**
     *
     *
     * @param className
     * @param order
     * @return
     * @throws UMException
     */
    public static List getList(String className, String order) throws
        UMException {
        Session session = null;
        List list = null;
        String find = null;
        try {
            session = PersistenceManager.getSession();

            if (order != null && order.trim().length() > 0) {
                find = new String("from rs in class " + className +
                                  " order by rs." + order);
            }
            else {
                find = new String("from rs in class " + className);
            }

            list = session.find(find);
        }
        catch (Exception ex) {
            logger.debug("Unable to get data from " + className,ex);
            throw new UMException("Unable to get data from " + className, ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ",ex2);
            }
        }

        return list;
    }

    /**
     * Get user object by  id
     *
     * @param activeUserId
     * @return
     * @throws UMException
     */
    public static User getSelectedUser(Long activeUserId,
                                       HttpServletRequest request) throws
        UMException {
        User result;
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            result = (User) session.load(User.class, activeUserId);
            ProxyHelper.initializeObject(result);

            Site site = RequestUtils.getSite(request);
            UserPreferences userPreferences = UserUtils.getUserPreferences(result,
                site);
            if (userPreferences == null) {
                userPreferences = new UserPreferences(result, site);
            }
            result.setUserPreference(userPreferences);

        }
        catch (Exception ex) {
            logger.debug("Unable to get User from database ",ex);
            throw new UMException("Unable to get User from database", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ",ex2);
            }
        }

        return result;

    }

    public static List searchUsers(String criteria) throws UMException {

        Session session = null;
        List userList;

        try {

            session = PersistenceManager.
                getSession();

            String userLow = criteria.trim().toLowerCase();
            String userLowConcat;

            StringTokenizer st = new StringTokenizer(userLow);
            userLowConcat = "";
            while (st.hasMoreTokens()) {
                userLowConcat += st.nextToken();
            }
            userLow = "%" + userLow + "%";
            userLowConcat = "%" + userLowConcat + "%";

            /*            if (st.hasMoreTokens()) {
                            userLowConcat = st.nextToken();
                            while (st.hasMoreTokens()) {
                                userLowConcat = userLowConcat + st.nextToken();
                            }
                        }
                        else {
                            userLowConcat = userLow;
                        }
             */

            userList = session.find("from rs in class " + User.class.getName() +
                                    " where rs.email like ? or lower(rs.firstNames) like ? or lower(rs.lastName) like ? or lower(rs.firstNames || rs.lastName) like ?",
                                    new Object[] {
                userLow, userLow, userLow, userLowConcat

            }

            , new Type[] {
                Hibernate.STRING, Hibernate.STRING, Hibernate.STRING,
                Hibernate.STRING});
            Iterator userIter = userList.iterator();
            while (userIter.hasNext()) {
                User user = (User)userIter.next();
                ProxyHelper.initializeObject(user);
            }

        }
        catch (Exception ex) {
            logger.debug("Unable to get username list from database ",ex);
            throw new UMException(
                "Unable to get username list from database", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ",ex2);
            }
        }


        return userList;
    }


    /**
     *
     * @param site Site
     * @param user User
     * @return Interests
     * @throws UMException
     */
    public static Interests getInterestBySite(Site site, User user) throws UMException {
        List interests = null;
        Session session = null;
        try {
            if( site != null ) {
                session = PersistenceManager.getSession();
                Query query = session.createQuery("select s from " + Interests.class.getName() +
                                     " s where (s.site.id = ? and s.user.id = ?)");
                query.setParameter(0, site.getId());
                query.setParameter(1, user.getId());

                interests = query.list();
                if( interests != null ) {
                    Iterator iter = interests.iterator();
                    while (iter.hasNext()) {
                        Interests item = (Interests) iter.next();
                        return item;
                    }
                }
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get Topics Sites",ex);
            throw new UMException("Unable to get Topics Sites",ex);
        }
        finally {
            try {
                if (session != null) {
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed ",ex1);
            }
        }
        return null;
    }


    /**
     *
     * @param site
     * @return
     * @throws UMException
     */
    public static List getTopicsSites(Site site) throws UMException {
        List sites = null;
        Session session = null;
        try {
            if( site != null ) {
                session = PersistenceManager.getSession();
                Query query = session.createQuery("select s from " + Site.class.getName() +
                                     " s where (s.parentId = ?) order by s.priority");
                query.setParameter(0, site.getId());
                query.setCacheable(true);

                sites = query.list();
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get Topics Sites",ex);
            throw new UMException("Unable to get Topics Sites",ex);
        }
        finally {
            try {
                if (session != null) {
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed ",ex1);
            }
        }
        return sites;
    }


    /**
     *
     * @param siteId
     * @return
     * @throws UMException
     */
    public static List getGeoupsBySiteId(Long siteId) throws UMException {
        List groups = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            groups = session.find(" from s in class " + Group.class.getName() +
                                 " where s.site.id = ?", new Object[] {siteId}, new Type[] {Hibernate.LONG});
        }
        catch (Exception ex) {
            logger.debug("Unable to get Group list from database ",ex);
            throw new UMException("Unable to get Group list from database ",ex);
        }
        finally {
            try {
                if (session != null) {
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception ex1) {
              logger.warn("releaseSession() failed ",ex1);
            }
        }
        return groups;
    }
    
    /**
     * @author akashs
     * retrieves all organisation groups
     * @return col, collection of retrieved organisation groups
     */
    public static Collection getAllOrgGroup() {
		Session session = null;
		Collection col = new ArrayList();
		try {
			session = PersistenceManager.getSession();
			String q = "select grp from " + AmpOrgGroup.class.getName() + " grp";
			col = session.createQuery(q).list();
		} catch (Exception ex) {
			logger.error("Unable to get Org Group" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}
    
    /**
     * @author akashs
     * @param Id id of organisation group
     * retrieves all organisations under organisation group with this id
     * @return col collection of retrieved organisations
     */
	public static Collection getOrgByGroup(Long Id) {

		Session sess = null;
		Collection col = new ArrayList();
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select o from " + AmpOrganisation.class.getName()
								 + " o where (o.orgGrpId=:orgGrpId) order by o.name asc";
			qry = sess.createQuery(queryString);
			qry.setParameter("orgGrpId", Id, Hibernate.LONG);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getOrgByGroup()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	/**
     * @author akashs
     * retrieves all organisation types
     * @return col, collection of retrieved organisation types
     */
    public static Collection getAllOrgTypes() {
		Session session = null;
		Collection col = new ArrayList();
		try {
			session = PersistenceManager.getSession();
			String q = "select type from " + AmpOrgType.class.getName() + " type order by type asc";
			col = session.createQuery(q).list();
		} catch (Exception ex) {
			logger.error("Unable to get Org Types" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}

    /**
     * @author akashs
     * @param Id id of organisation type
     * retrieves all organisation groups of this type with id as their PK
     * @return col collection of retrieved organisation groups
     */
	public static Collection getOrgGroupByType(Long Id) {

		Session sess = null;
		Collection col = new ArrayList();
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select o from " + AmpOrgGroup.class.getName()
								 + " o where (o.orgType=:orgTypeId) order by o.orgGrpName asc";
			qry = sess.createQuery(queryString);
			qry.setParameter("orgTypeId", Id, Hibernate.LONG);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getOrgGroupByType()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

    /**
     * @author akashs
     * @param Id id of organisation type
     * retrieves all organisations of this type with id as their PK
     * @return col collection of retrieved organisations
     */
	public static Collection getOrgByType(Long Id) {

		Session sess = null;
		Collection col = new ArrayList();
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select o from " + AmpOrganisation.class.getName()
								 + " o where (o.orgType=:orgTypeId)";
			qry = sess.createQuery(queryString);
			qry.setParameter("orgTypeId", Id, Hibernate.LONG);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getOrgByType()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}
   
}
