package org.digijava.module.help.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.help.dbentity.HelpTopic;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Utility method for glossary
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class GlossaryUtil {

	private static Logger logger = Logger.getLogger(GlossaryUtil.class);

	public static final Integer TYPE_GLOSSARY = new Integer(2);

	/**
	 * Loads all glossary topics from DB.
	 * Both parameters are used to filter results and can be null, 
	 * but be careful with Admin and User help instances.
	 * Glossary topics are with topicType=TYPE_GLOSSARY
	 * @param moduleInstance can be null, but use correct value to not mess with admin and user helps.
	 * @param siteId can be null.
	 * @return list of help topics with topicType=TYPE_GLOSSARY
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<HelpTopic> getAllGlosaryTopics(String moduleInstance, String siteId) throws DgException{
		List<HelpTopic> result = null;
		String oql = " from " + HelpTopic.class.getName()+ " as ht where ht.topicType=:GLOSS_TYPE";
		if (moduleInstance != null){
			oql += " and ht.moduleInstance=:MOD_INST";
		}
		if (siteId != null){
			oql += " and ht.siteId = :SITE_ID";
		}
		Session session = PersistenceManager.getRequestDBSession();
		Query query = session.createQuery(oql);
		query.setInteger("GLOSS_TYPE",TYPE_GLOSSARY);
		if (moduleInstance != null){
			query.setString("MOD_INST", moduleInstance);
		}
		if (siteId != null){
			query.setString("SITE_ID", siteId);
		}
		result = query.list();
		return result;
	}

	/**
	 * Search glossary.
	 * Tries to match keywords in title and in body.
	 * @param keyWords list of strings that will be used in LIKE operation. can be null
	 * @param moduleInstance help module instance name, can be null
	 * @param siteId digi site ID- value of siteId field, can be null.
	 * @return list of found glossary items.
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<HelpTopic> searchGlossary(List<String> keyWords, String moduleInstance, String siteId) throws DgException{
		
		List<HelpTopic> result = null;
		String oql = "select ht from "; 
		oql += HelpTopic.class.getName()+ " as ht, ";
		oql += Editor.class.getName()+" as e ";
		oql += " where ht.topicType=:GLOSS_TYPE";
		oql += " and ht.bodyEditKey = e.editorKey";
		if (keyWords!=null && keyWords.size()>0){
			oql += " and ( ";
			int c = 0;
			for (int i=0;i<keyWords.size();i++){
				if (c>0) oql += " or ";
				oql += " ht.topicKey like :KEY_WORD_"+c;
				oql += " or   e.body like :KEY_WORD_"+c;
				c++;
			}
			oql+=" )";
		}
		if (moduleInstance != null){
			oql += " and ht.moduleInstance=:MOD_INST";
		}
		if (siteId != null){
			oql += " and ht.siteId = :SITE_ID";
		}
		Session session = PersistenceManager.getRequestDBSession();
		Query query = session.createQuery(oql);
		query.setInteger("GLOSS_TYPE",TYPE_GLOSSARY);
		if (keyWords!=null && keyWords.size()>0){
			int c = 0;
			for (String kw : keyWords) {
				query.setString("KEY_WORD_"+c, "%"+kw+"%");
				c++;
			}
		}
		if (moduleInstance != null){
			query.setString("MOD_INST", moduleInstance);
		}
		if (siteId != null){
			query.setString("SITE_ID", siteId);
		}
		result = query.list();
		return result;
	}
	
	/**
	 * Loads glossary topic object by ID.
	 * @param id pk in db.
	 * @return glossary topic bean
	 * @throws DgException
	 */
	public static HelpTopic getGlosaryTopic(Long id) throws DgException{
		String oql = " from " + HelpTopic.class.getName()+ " as ht where ht.topicType=:GLOSS_TYPE and ht.helpTopicId=:TOPIC_ID";
		Session session = PersistenceManager.getRequestDBSession();
		Query query = session.createQuery(oql);
		query.setInteger("GLOSS_TYPE",TYPE_GLOSSARY);
		query.setLong("TOPIC_ID",id);
		HelpTopic result = (HelpTopic) query.uniqueResult();
		return result;
	}
	
	/**
	 * Creates ne glossary topic.
	 * @param topic
	 * @throws DgException
	 */
	public static void createOrUpdateGlossaryTopic(HelpTopic topic) throws DgException{
		topic.setTopicType(TYPE_GLOSSARY);
		HelpUtil.saveOrUpdateHelpTopic(topic);
	}
	
	/**
	 * Deletes glossary topic.
	 * Also does all necessary cleanup of editors and children.
	 * @param glossaryTopic
	 * @throws DgException
	 */
	public static void deleteGlossaryTopic(HelpTopic glossaryTopic) throws DgException{
		Session dbSession = null;
		Transaction tx = null;
		try {
			dbSession = PersistenceManager.getRequestDBSession();
			tx = dbSession.beginTransaction();						//Start transaction
			deleteTopic(glossaryTopic, dbSession);					//delete topic, its editor and children
			tx.commit();											//commit changes
		} catch (Exception e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception ex) {
					logger.error(ex);
					throw new DgException("Cnnot roll back glossary deletion!!!", ex);
				}
			}
			logger.error(e);
			throw new DgException("Can't remove Glossary items", e);
		}
	}

	/**
	 * Delete glossary, its editor, and its children recursively.
	 * All deletes are done in one session for which client should start
	 * transaction and later commit it or rollback.
	 * @param glossaryTopic
	 * @param dbSession
	 * @throws DgException
	 */
	private static void deleteTopic(HelpTopic glossaryTopic, Session dbSession) throws DgException{
		List<HelpTopic> children = getChildren(glossaryTopic, dbSession);
		if (children!=null && children.size()>0){
			for (HelpTopic child : children) {
				deleteTopic(child, dbSession);
			}
		}
		deleteEditor(glossaryTopic, dbSession);
		dbSession.delete(glossaryTopic);
	}
	
	/**
	 * Removes all editor records for key stored in help topic.
	 * @param topic
	 * @param dbSession
	 * @throws DgException
	 */
	private static void deleteEditor(HelpTopic topic, Session dbSession) throws DgException{
		if (topic.getBodyEditKey()!=null && topic.getSiteId()!=null){
			List<Editor> editors = DbUtil.getEditorList(topic.getBodyEditKey(), topic.getSiteId());
			if (editors!=null && editors.size()>0){
				for (Editor editor : editors) {
					dbSession.delete(editor);
				}
			}
		}
	}
	
	/**
	 * Retrieves direct children topics of specified parent topic.
	 * @param topic paremt topic, should NOT be NULL
	 * @param dbSession db session, should NOT be NULL
	 * @return list of one level children topics or NULL
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	private static List<HelpTopic> getChildren(HelpTopic topic,Session dbSession) throws DgException{
		List<HelpTopic> helpTopics = null;
		try {
			String queryString = "from "+ HelpTopic.class.getName() + " topic where topic.parent.helpTopicId=:id";
			Query query = dbSession.createQuery(queryString);
        	query.setParameter("id", topic.getHelpTopicId());
			helpTopics = query.list();
		} catch (Exception e) {
			logger.error(e);
  			throw new AimException("Unable to load glossary children", e);
		}
		return helpTopics;
	}
}
