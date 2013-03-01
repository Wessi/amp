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

package org.digijava.module.editor.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.text.regex.RegexBatch;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DbUtil {

    private static Logger logger = Logger.getLogger(DbUtil.class);
    
	/**
	 * Flags used for regex matching to strip out all unneeded html.
	 * Can combine multiple patterns like this = Pattern.DOTALL | Pattern.MULTILINE; 
	 */
	private static final int REGEX_FLAGS = Pattern.DOTALL;
	
	/**
	 * Regexes split and ordered so that it will leave only 
	 * text required for Lucene indexing.  
	 */
	private static final String[] HTML_STRIP_REGEXES = 
	{
			"<!--.*?-->"						//commented texts
			, "<!DOCTYPE.*?>"					//doc type tags
			, "<head.*?>.*?</head>"				//head tag with content
			, "<script.*?>.*?</script>"			//script tag with content
			, "<style.*?>.*?</style>"			//style tag with content
			, "<(link|input|a|br|hr|meta).*?>"	//some tags
			, "<\\s*?[a-z]+(:[a-z0-9]+)?.*?>"	//Beginnings of tags 
			, "</\\s*?[a-z]+(:[a-z0-9]+)?.*?>"	//Endings of tags
			, "&[a-z]*?;"						//&nbsp; and things like that
			,"\\s{2,}"							//multiple spaces
	};
	
	/**
	 * Stripps html tags from text.
	 */
	private static final RegexBatch htmlStripper = new RegexBatch(HTML_STRIP_REGEXES,REGEX_FLAGS);

    /**
     * Get editor item by key from database
     *
     * @param key
     * @return
     * @throws EditorException
     */
    @SuppressWarnings("unchecked")
	public static List<Editor> getEditorList(String siteId, String editorKey, String language) throws EditorException {

        Session session = null;
        List<Editor> items = new ArrayList<Editor>();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          Editor.class.getName() +
                                          " e where (e.siteId=:siteId) and (e.editorKey=:editorKey) and (e.language!=:language)");

            q.setString("siteId", siteId);
            q.setString("editorKey", editorKey);
            q.setString("language", language);

            items = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            throw new EditorException("Unable to get editor item from database",
                                      ex);
        }

        return items;
    }

    /**
     * Returns editors of all language with specified key and siteId
     * Useful when one wants to remove or update some other bean, and its editors in all language.
     * @param editorKey
     * @param siteId
     * @return
     * @throws EditorException
     */
	@SuppressWarnings("unchecked")
	public static List<Editor> getEditorList(String editorKey,String siteId) throws EditorException {

		Session session = null;
		List<Editor> items = new ArrayList<Editor>();
		try {
			session = PersistenceManager.getRequestDBSession();
			Query q = session.createQuery("from "
							+ Editor.class.getName()
							+ " e where (e.siteId=:siteId) and (e.editorKey=:editorKey)");

			q.setString("siteId", siteId);
			q.setString("editorKey", editorKey);

			items = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get editor item from database ", ex);
			throw new EditorException(
					"Unable to get editor item from database", ex);
		}

		return items;
	}    
	
	public static List<Editor> getEditorList(String editorKey,String siteId,Session session) throws EditorException {
		List<Editor> items = new ArrayList<Editor>();
		try {
			Query q = session.createQuery("from "+ Editor.class.getName()+ " e where (e.siteId=:siteId) and (e.editorKey=:editorKey)");
			q.setString("siteId", siteId);
			q.setString("editorKey", editorKey);

			items = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get editor item from database ", ex);
			throw new EditorException(
					"Unable to get editor item from database", ex);
		}

		return items;
	}    
    
    public static void deleteEditor(Editor ed) throws
        EditorException {
        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();

//beginTransaction();
            session.delete(ed);
            //tx.commit();
        }
        catch (Exception ex) {

            logger.debug("Unable to delete editor", ex);
            throw new EditorException(
                "Unable to delete editor", ex);
        }
    }

    /**
     * Get editor list for specified site
     *
     * @param key
     * @return
     * @throws EditorException
     */
    
    public static List<Editor> getSiteEditorList(String siteId) throws
        EditorException {

        Session session = null;
        List<Editor> items = new ArrayList<Editor>();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " + Editor.class.getName() +
                " e where (e.siteId=:siteId) order by e.orderIndex");

            q.setString("siteId", siteId);

            @SuppressWarnings("unchecked")
			List<Editor> result = q.list();
            if (result != null && (result.size() != 0)) {
                items = result;
            }
            else {
                return null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor list from database ", ex);
            throw new EditorException("Unable to get editor item from database",
                                      ex);
        }

        return items;
    }

    public static List<Editor> getSiteEditorList(String siteId,String lang,String groupName) throws EditorException {

        Session session = null;
        List<Editor> items = new ArrayList<Editor>();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          Editor.class.getName() +
                " e where e.siteId=:siteId and e.groupName=:groupName " +
                "and e.language=:language " +
                "order by e.orderIndex");

            q.setString("siteId", siteId);
            q.setString("groupName", groupName);
            q.setString("language", lang);

            @SuppressWarnings("unchecked")
			List<Editor> result = q.list();
            if (result != null && (result.size() != 0)) {
                items = result;
            }
            else {
                return null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor list from database ", ex);
            throw new EditorException("Unable to get editor item from database", ex);
        }

        return items;
    }

    /**
     * Get editor item by siteId key and language code from database
     *
     * @param key
     * @return
     * @throws EditorException
     */
    public static Editor getEditor(String siteId, String editorKey,String language) throws EditorException {

        Session session = null;
        Editor item = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            try {
            	Query q = session.createQuery("from " + Editor.class.getName() +" e where e.siteId=:siteId and e.editorKey=:editorKey");
            		q.setString("siteId", siteId);
            		q.setString("editorKey", editorKey);
            		//q.setString("language", language);
            		@SuppressWarnings("unchecked")
					Collection<Editor> edits=q.list();
            		for (Iterator iterator = edits.iterator(); iterator.hasNext();) {
        				Editor editor = (Editor) iterator.next();
        				if (editor.getLanguage().equalsIgnoreCase(language) && !"".equalsIgnoreCase(editor.getBody())){
        					item = editor;
        					break;
        				}else if (!"".equalsIgnoreCase(editor.getBody())){
        					item= editor;
        				}
        			}
            }
            catch (ObjectNotFoundException ex1) {
                logger.error("DbUtil:getEditor:Unable to get Editor item", ex1);
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            item = null;
        }
        return item;
    }


    public static Editor getEditor(String siteId, int orderIndex) throws EditorException {

        Session session = null;
        Editor item = new Editor();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          Editor.class.getName() +
                                          " e where (e.siteId=:siteId) and " +
                                          "(e.orderIndex=:orderIndex)");

            q.setString("siteId", siteId);
            q.setInteger("orderIndex", new Integer(orderIndex));

            @SuppressWarnings("unchecked")
			List<Editor> result = q.list();
            if (result != null && (result.size() != 0)) {
            	for (Editor editor : result) {
                    item = editor;
                    break;
				}
            }
            else {
                item = null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            throw new EditorException("Unable to get editor item from database", ex);
        }

        return item;
    }

    /**
     * Get editor item by key and language code from database
     *
     * @param key
     * @return
     * @throws EditorException
     */
    public static Editor getEditor(String editorKey, String language) throws EditorException {

        Session session = null;
        Editor item = new Editor();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " + Editor.class.getName() + 
                " e where (e.editorKey=:editorKey) and (e.language=:language)");

            q.setString("editorKey", editorKey);
            q.setString("language", language);

            @SuppressWarnings("unchecked")
			List<Editor> result = q.list();
            if (result != null && (result.size() != 0)) {
            	item = result.get(0);
            }
            else {
                item = null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            throw new EditorException("Unable to get editor item from database", ex);
        }
        return item;
    }

   
    
    
    /**
     * Update editor
     *
     * @param editor
     * @throws EditorException
     */
    public static void updateEditor(Editor editor) throws EditorException {

        Session session = null;
        Transaction tx = null;
        try {

            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.update(editor);
            //tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update editor information into database", ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new EditorException(
                "Unable to update editor information into database", ex);
        }
    }

    public static void updateEditorList(Collection<Editor> editors) throws
        EditorException {

        Session session = null;
        Transaction tx = null;
        try {

            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            for (Editor editor : editors) {
                session.update(editor);
			}
            //tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update editor information into database", ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new EditorException(
                "Unable to update editor information into database", ex);
        }
    }

    public static void saveEditor(Editor editor) throws EditorException {
        saveEditor(editor, false);
    }

    /**
     * Save editor
     *
     * @param editor
     * @throws EditorException
     */
    public static void saveEditor(Editor editor, boolean newSess) throws EditorException {

        Session session = null;
        Transaction tx = null;
        try {
            if (!newSess) {
                session = PersistenceManager.getRequestDBSession();
            } else {
                session = PersistenceManager.openNewSession();
                tx = session.beginTransaction();
            }
//beginTransaction();
            session.save(editor);
            if (newSess) {
                tx.commit();
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to save editor information into database",
                         ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new EditorException(
                "Unable to save editor information into database", ex);
        } finally {
            if (newSess && session != null) {
                session.close();
            }
        }
    }

    /**
     * Creates editor in db.
     * @param user
     * @param languageCode
     * @param url
     * @param editorKey
     * @param title
     * @param body
     * @param notice
     * @param request
     * @return
     */
    public static Editor createEditor(User user, String languageCode,
                                      String url, String editorKey,
                                      String title,
                                      String body, String notice,
                                      HttpServletRequest request) {

        Editor editor = new Editor();

        // get module instance
        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);

        editor.setSiteId(moduleInstance.getSite().getSiteId());
        editor.setEditorKey(editorKey);
        editor.setUrl(url);
        editor.setLanguage(languageCode);

        editor.setTitle(title);
        editor.setBody(body);
        editor.setNotice(notice);

        editor.setLastModDate(null);
        editor.setUser(user);
        editor.setCreationIp(RequestUtils.getRemoteAddress(request));

        return editor;
    }

    /**
     * Retrieves editor body text.
     * @param siteId
     * @param editorKey
     * @param language
     * @return
     * @throws EditorException
     */
    public static String getEditorBody(String siteId, String editorKey, String language) throws EditorException {

        Session session = null;
        String body = null;

        try {
            session = PersistenceManager.getRequestDBSession();

            Query q = session.createQuery(
                "select e from " + Editor.class.getName() + " e " +
                " where (e.siteId=:siteId) and (e.editorKey=:editorKey)");

          //  q.setCacheable(true);
            q.setString("siteId", siteId);
            q.setString("editorKey", editorKey);
            //q.setString("language", language);

            @SuppressWarnings("unchecked")
			List<Editor> result = q.list();
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
				Editor editor = (Editor) iterator.next();
				if (editor.getLanguage().equalsIgnoreCase(language) && !"".equalsIgnoreCase(editor.getBody())){
					body = editor.getBody();
					break;
				}else if (!"".equalsIgnoreCase(editor.getBody())){
					body = editor.getBody();
				}
			}

        }
        catch (Exception ex) {
            logger.debug("Unable to get editor from database", ex);
            throw new EditorException("Unable to get editor from database", ex);
        }

        return body;
    }
    /**
     * Retrieves editor body text.
     * @param siteId
     * @param editorKey
     * @param language
     * @return
     * @throws EditorException
     */
    public static String getEditorBodyEmptyInclude(String siteId, String editorKey, String language) throws EditorException {

        Session session = null;
        String body = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery(
                "select e.body from " + Editor.class.getName() + " e " +
                " where (e.siteId=:siteId) and (e.editorKey=:editorKey) and e.language=:language");
          //  q.setCacheable(true);
            q.setString("siteId", siteId);
            q.setString("editorKey", editorKey);
            q.setString("language", language);
            body=(String)q.uniqueResult();
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor from database", ex);
            throw new EditorException("Unable to get editor from database", ex);
        }

        return body;
    }
    

    /**
     * Returns editor body text but strips out all HTML tags.
     * Uses {@link #getEditorBody(String, String, String)} method to 
     * retrieve initial body text and then uses regexps to strip tags.
     * Note that to strip all possible HTML tags batch processing is used to 
     * avoid stack overflow problems which happens when processing too huge regexp.
     * Solution copied from AMP v2 Help+Lucene integration solution.
     * Check AMP-9328 for more details. 
     * @param siteId
     * @param editorKey
     * @param language
     * @return
     * @throws EditorException
     */
    public static String getEditorBodyFiltered(String siteId, String editorKey, String language) throws EditorException {
    	String body = getEditorBody(siteId, editorKey, language);
    	if (body != null){
    		RegexBatch batch = new RegexBatch(HTML_STRIP_REGEXES,REGEX_FLAGS);
    		body = batch.replaceAll(body, " "); 
    		
    	}
    	return body;
    }

    /**
     * Retrieves editor title text.
     * @param siteId
     * @param editorKey
     * @param language
     * @return title text or null.
     * @throws EditorException
     */
    public static String getEditorTitle(String siteId, String editorKey,String language) throws EditorException {

        Session session = null;
        String title = "";

        try {
            session = PersistenceManager.getRequestDBSession();

            Query q = session.createQuery(
                "select e.title from " +
                Editor.class.getName() + " e, " +
                " where (e.siteId=:siteId) and (e.editorKey=:editorKey) and (e.language=:language)");

            q.setCacheable(true);
            q.setString("siteId", siteId);
            q.setString("editorKey", editorKey);
            q.setString("language", language);

            @SuppressWarnings("unchecked")
			List<String> result = q.list();
            if (result != null && (result.size() != 0)) {
            	for (String editorTitle : result) {
            		if (editorTitle != null){
    					title = editorTitle;
    					break;
            		}
				}
//                Iterator iter = result.iterator();
//                while (iter.hasNext()) {
//                    Object item = iter.next();
//                    if( item != null ) {
//                        title = new String( (String) item);
//                    }
//                    break;
//                }
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor title from database", ex);
            throw new EditorException(
                "Unable to get editor title from database", ex);
        }

        return title;
    }

}
