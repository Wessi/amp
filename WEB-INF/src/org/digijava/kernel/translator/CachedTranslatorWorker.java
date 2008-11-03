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

package org.digijava.kernel.translator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.util.DigiCacheManager;

public class CachedTranslatorWorker
    extends TranslatorWorker {
    private static Logger logger =
        Logger.getLogger(CachedTranslatorWorker.class);

    private AbstractCache messageCache;

    CachedTranslatorWorker() {
        super();
        messageCache = DigiCacheManager.getInstance().getCache(
            "org.digijava.kernel.entity.Message.id_cache");
    }

    /**
     * Read translation from database and put to translation cache
     * @param key translation key
     * @param locale locale
     * @param siteId owner site
     * @throws WorkerException if process was not completed successfully
     */
    public void refresh(String key, String locale, String siteId) throws
        WorkerException {
        Session session = null;

        try {

            Message mesageKey = new Message();
            mesageKey.setKey(processKeyCase(key));
            mesageKey.setLocale(locale);
            mesageKey.setSiteId(siteId);

            session = PersistenceManager.getSession();
            Message message = (Message) session.load(Message.class, mesageKey);
            processBodyChars(message);//if we run script on db which will do same action, do we need this here?
            
            messageCache.put(message, message);
            logger.debug("Refreshed translation for siteId="
                         + siteId + ", key = " + key + ",locale=" + locale);
        }
        catch (ObjectNotFoundException onfe) {
        }

        catch (Exception ex) {
            throw new WorkerException("Unable to refresh translation[key=" +
                                      key + ", locale=" + locale + ", siteId=" +
                                      siteId + "]", ex);
        }
        finally {
            try {
                if (session != null) {

                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception e) {}
        }

    }

    public Message get(String key, String locale, String siteId) throws
        WorkerException {

        Message message = new Message();

        message.setKey(processKeyCase(key));
        message.setLocale(locale);
        message.setSiteId(siteId);

        Object obj = messageCache.get(message);
        if (obj == null) {
            logger.debug("No translation exists for siteId="
                         + siteId + ", key = " + key + ",locale=" + locale);
            return null;
        }
        else {
            return (Message) obj;
        }
    }

    public void save(Message message) throws WorkerException {
        saveDb(message); //message will be processed there 
        
        messageCache.put(message, message);
        fireRefreshAlert(message);
    }

    /**
     * Updates a particular message
     * @param message
     * in the cache
     * @throws WorkerException
     */
    public void update(Message message) throws WorkerException {
        updateDb(message);//message will be processed there

        messageCache.put(message, message);
        fireRefreshAlert(message);
    }

    /**
     * Deletes a particular message
     * @param message
     * @throws WorkerException
     */
    public void delete(Message message) throws WorkerException {
        deleteDb(message);//message will be processed there
        messageCache.evict(message);
        fireRefreshAlert(message);
    }

    protected void setTimestamps(String key, Timestamp timestamp) throws
        WorkerException {
        if (key == null)
            return;

        Session ses = null;
        Transaction tx = null;
        List messages;
        String queryString = "from " + Message.class.getName() +
            " msg where msg.key=:msgKey";

        try {

            ses = PersistenceManager.getSession();
            tx = ses.beginTransaction();
            Query q = ses.createQuery(queryString);
            q.setString("msgKey", processKeyCase(key.trim()));
            
            messages = q.list();
            Iterator it = messages.iterator();

            while (it.hasNext()) {
                Message msg = (Message) it.next();
                msg.setCreated(timestamp);
                ses.update(msg);
                messageCache.put(msg, msg);
            }

            tx.commit();

        }
        catch (SQLException se) {
            logger.error("Error updating translations. key=" + key, se);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new WorkerException("Error updating translations. key=" + key, se);
        }
        catch (HibernateException e) {
            logger.error("Error updating translations. key=" + key, e);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new WorkerException("Error updating translations. key=" + key, e);
        }
        finally {
            try {
                if (ses != null) {
                    PersistenceManager.releaseSession(ses);
                }
            }
            catch (Exception e) {
                logger.error("releaseSession() failed. key=" + key, e);
            }
        }

    }
}
