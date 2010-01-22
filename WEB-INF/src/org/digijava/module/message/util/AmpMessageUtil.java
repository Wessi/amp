package org.digijava.module.message.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.helper.MessageConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AmpMessageUtil {
	private static Logger logger = Logger.getLogger(AmpMessageUtil.class);
	
	public static <E extends AmpMessage> List getAllMessages(Class<E> clazz) throws AimException {
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpMessage> returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();			
			queryString= "select a from " + clazz.getName()+ " a where a.name ";
			query=session.createQuery(queryString);
			returnValue=query.list();
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());
		}
		return returnValue;
	}
	
	public static void saveOrUpdateMessage(AmpMessage message) throws AimException {
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(message);
			tx.commit();
		}catch(Exception ex) {
			if(tx!=null) {
				try {
					tx.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("update failed",ex);
		}
	}
	
	public static AmpMessage getMessage(Long messageId) throws AimException{
		Session session=null;
		String queryString =null;
		Query query=null;
		AmpMessage returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select a from " + AmpMessage.class.getName()+ " a where a.id=:id";
			query=session.createQuery(queryString);
			query.setParameter("id", messageId);
			returnValue=(AmpMessage)query.uniqueResult();
		}catch(Exception ex) {
			logger.error("couldn't load Message" + ex.getMessage());	
			ex.printStackTrace();
		}
		return returnValue;
	}
	/**
	 * removes AmpMessage from db
	 * @param id
	 * @throws Exception
	 */
	public static void removeMessage(Long id) throws Exception{
		Session session=null;
		Transaction trans=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			trans=session.beginTransaction();
			AmpMessage message=getMessage(id);
			session.delete(message);
			trans.commit();
		} catch (Exception ex) {
			if(trans!=null) {
				try {
					trans.rollback();
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}
			}
			throw new AimException("delete failed",ex);
		}
	}	
	
	public static List<TemplateAlert> getTemplateAlerts(String relatedTriggerName) throws Exception{
		Session session=null;
		String queryString =null;
		Query query=null;
		List<TemplateAlert>  returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select t from " + TemplateAlert.class.getName()+ " t where t.relatedTriggerName=:relTriggerName";
			query=session.createQuery(queryString);			
			query.setParameter("relTriggerName", relatedTriggerName);
			returnValue=(List<TemplateAlert> )query.list();
		}catch(Exception ex) {
			logger.error("couldn't load TemplateAlert" + ex.getMessage());	
			ex.printStackTrace();
		}
		return returnValue;
	}
	
	//***********************************************Message State functions************************************************
	
	/**
	 * loads AmpMessageState with the given state id
	 * @param stateId
	 * @return
	 * @throws Exception
	 */
	public static AmpMessageState getMessageState(Long stateId) throws Exception{
		Session session=null;
		String queryString =null;
		Query query=null;
		AmpMessageState returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select st from " + AmpMessageState.class.getName()+ " st where st.id=:id";
			query=session.createQuery(queryString);
			query.setParameter("id", stateId);
			returnValue=(AmpMessageState)query.uniqueResult();
		}catch(Exception ex) {
			logger.error("couldn't load Message State" + ex.getMessage());	
			ex.printStackTrace();
		}
		return returnValue;
	}
	
	public static void removeMessageState(Long stateId) throws AimException{
		Session session=null;
		Transaction trans=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			trans=session.beginTransaction();
			AmpMessageState state=getMessageState(stateId);
			session.delete(state);
			trans.commit();
		} catch (Exception ex) {
			if(trans!=null) {
				try {
					trans.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("delete failed",ex);
		}
	}
	
	public static void removeMessageState(AmpMessageState state) throws AimException{
		Session session=null;
		Transaction trans=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			trans=session.beginTransaction();			
			session.delete(state);
			trans.commit();
		} catch (Exception ex) {
			if(trans!=null) {
				try {
					trans.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("delete failed",ex);
		}
	}
	

	public static void removeMessageStates(List<AmpMessageState> states) throws AimException{
		Session session=null;
		Transaction trans=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			trans=session.beginTransaction();
			for (AmpMessageState state : states) {
				session.delete(state);
			}			
			trans.commit();
		} catch (Exception ex) {
			if(trans!=null) {
				try {
					trans.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("delete failed",ex);
		}
	}
	

	public static void saveOrUpdateMessageState(AmpMessageState messageState) throws AimException {
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.clear();
			session.saveOrUpdate(messageState);
			tx.commit();
		}catch(Exception ex) {
			if(tx!=null) {
				try {
					tx.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("update failed",ex);
		}
	}
	
	public static List<AmpMessageState> loadMessageStates(Long messageId) throws AimException{
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpMessageState> returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select a from " + AmpMessageState.class.getName()+ " a where a.message.id="+messageId;
			query=session.createQuery(queryString);
			returnValue=(List<AmpMessageState>)query.list();
		}catch(Exception ex) {
			logger.error("couldn't load States" + ex.getMessage());	
			ex.printStackTrace();
		}
		return returnValue;
	}
	
	/**
	 * return inbox messages amount for each type of message. if onlyUnred is true, then returns only unread messages amount
	 */
	public static <E extends AmpMessage> int getInboxMessagesCount(Class<E> clazz,Long tmId,boolean onlyUnread,boolean hidden,int msgStoragePerMsgType) throws Exception {
		int retValue=0;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();	
			queryString="select count(*) from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.receiver.ampTeamMemId=:tmId and msg.draft=false and state.messageHidden=:hidden";
			if(onlyUnread){
				queryString+=" and state.read=false";
			}
			query=session.createQuery(queryString);			 				
			query.setParameter("tmId", tmId);
                        query.setParameter("hidden", hidden);
                        Integer retInt=((Integer)query.uniqueResult());
            if(retInt==null) return 0;
			retValue=retInt.intValue();
                        
                       /* Someone may change msgStoragePerMsgType (make it more then it was previously). 
                        In this case we need to unhide some states which are marked as hidden in db*/
                        
                        if(retValue<msgStoragePerMsgType&&!onlyUnread){ //temporary fix @todo change code, but not today
                            int limit=msgStoragePerMsgType-retValue;
                            int numUnhidden=unhideMessageStates(clazz,tmId,limit);
                            retValue+=numUnhidden;
                        }
		}catch(Exception ex) {			
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);
			
		}
		return retValue;
	}
        
        /**
	 * unhide hidden message
         * 
	 */
	public static <E extends AmpMessage> int unhideMessageStates(Class<E> clazz,Long memberId,int limit) throws DgException {
		Session session=null;
                int retValue=0;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();	
			queryString="select state from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.receiver.ampTeamMemId=:tmId and msg.draft=false and (state.messageHidden=:hidden or state.messageHidden is null)";			
		
			queryString+=" order by msg.creationDate desc";
			query=session.createQuery(queryString);			 				
			query.setParameter("tmId", memberId);
            query.setParameter("hidden", true );
            query.setMaxResults(limit);
            if(!query.list().isEmpty()){
            	Iterator<AmpMessageState> iterState=query.list().iterator();
                while(iterState.hasNext()){
                	unhideMessageState(iterState.next().getId());
                }
            }						
		}catch(Exception ex) {			
			ex.printStackTrace();
			throw new DgException("Unable to unhide Messages", ex);
			
		}
                return retValue;
	}
        
         /**
	 * unhide hidden message
	 */
	public static  void unhideMessageState(Long stateId) throws DgException {
		Session session=null;
		try {
			session=PersistenceManager.getRequestDBSession();	
			AmpMessageState state=(AmpMessageState)session.load(AmpMessageState.class, stateId);
                        state.setMessageHidden(false);
                        session.update(state);			
		}catch(Exception ex) {			
			ex.printStackTrace();
			throw new DgException("Unable to unhide Messages", ex);
			
		}
         
	}
	
	public static <E extends AmpMessage> int getSentOrDraftMessagesCount(Class<E> clazz,Long tmId,Boolean draft,boolean hidden) throws Exception {
		int retValue=0;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();	
			queryString="select count(*) from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.senderId=:tmId and msg.draft="+draft+" and state.messageHidden=:hidden order by msg.creationDate desc";
			query=session.createQuery(queryString);			 				
			query.setParameter("tmId", tmId);
                        query.setParameter("hidden", hidden);
			retValue=((Integer)query.uniqueResult()).intValue();			
		}catch(Exception ex) {			
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);			
		}
		return retValue;
	}
	
	public static <E extends AmpMessage> List<AmpMessageState> loadAllInboxMessagesStates(Class<E> clazz,Long teamMemberId,int maxStorage,Integer[] page,int msgStoragePerMsgType) throws Exception{
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpMessageState> returnValue=null;	
		int messagesAmount=0;
		try {
			messagesAmount=getInboxMessagesCount(clazz,teamMemberId,false,false,msgStoragePerMsgType);
			session=PersistenceManager.getRequestDBSession();	
			queryString="select state from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.receiver.ampTeamMemId=:tmId and msg.draft=false and state.messageHidden=false order by msg.creationDate";	
			query=session.createQuery(queryString);
			
			//if max.storage is less then amount ,then we should not show extra messages. We must show the oldest(not newest) max.storage amount messages.
//			AmpMessageSettings settings=getMessageSettings();
//			if(settings!=null && settings.getMsgStoragePerMsgType()!=null){
//				int storage=settings.getMsgStoragePerMsgType().intValue();
//				if(storage<messagesAmount){
//					messagesAmount=messagesAmount-storage;
//				}
//			}
			int fromIndex=messagesAmount-page[0]*MessageConstants.MESSAGES_PER_PAGE;			
			if(fromIndex<0){
				fromIndex=0;
			}
			int toIndex;
			if(messagesAmount-(page[0]-1)*MessageConstants.MESSAGES_PER_PAGE< MessageConstants.MESSAGES_PER_PAGE){
				toIndex=messagesAmount-(page[0]-1)*MessageConstants.MESSAGES_PER_PAGE;
			}else{
				toIndex=MessageConstants.MESSAGES_PER_PAGE;
			}

			query.setFirstResult(fromIndex);
			query.setMaxResults(toIndex);
			query.setParameter("tmId", teamMemberId);			
			returnValue=query.list();
                        // after we delete the all rows we need to move to previous page
                        if((returnValue==null||returnValue.size()==0)&&page[0]!=1){
                            page[0]--;
                            returnValue=loadAllInboxMessagesStates(clazz,teamMemberId,maxStorage,page,msgStoragePerMsgType);
                        }
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);
			
		}
		return returnValue;
	}
	
	public static <E extends AmpMessage> List<AmpMessageState> loadAllSentOrDraftMessagesStates(Class<E> clazz,Long teamMemberId,int maxStorage,Boolean draft,Integer[] page) throws Exception{
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpMessageState> returnValue=null;	
		int messagesAmount=0;
		try {
			messagesAmount=getSentOrDraftMessagesCount(clazz,teamMemberId,draft,false);
			session=PersistenceManager.getRequestDBSession();	
			queryString="select state from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where "+
			"msg.id=state.message.id and state.senderId=:tmId and msg.draft="+draft+" and state.messageHidden="+false+" order by msg.creationDate ";
			query=session.createQuery(queryString);
			int fromIndex=messagesAmount-page[0]*MessageConstants.MESSAGES_PER_PAGE;			
			if(fromIndex<0){
				fromIndex=0;				
			}	
			int toIndex;
			if(messagesAmount-(page[0]-1)*MessageConstants.MESSAGES_PER_PAGE< MessageConstants.MESSAGES_PER_PAGE){
				toIndex=messagesAmount-(page[0]-1)*MessageConstants.MESSAGES_PER_PAGE;
			}else{
				toIndex=MessageConstants.MESSAGES_PER_PAGE;
			}
			query.setFirstResult(fromIndex);
			query.setMaxResults(toIndex);
			query.setParameter("tmId", teamMemberId);			
			returnValue=query.list();
                         // after we delete the all rows we need to move to previous page
                        if((returnValue==null||returnValue.size()==0)&&page[0]!=1){
                            page[0]--;
                            returnValue=loadAllSentOrDraftMessagesStates(clazz,teamMemberId,maxStorage,draft,page);
                        }
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);
			
		}
		return returnValue;
	}

	
	//***************************************************Message Settings***********************************************
	
	public static AmpMessageSettings getMessageSettings() throws Exception{ //for this moment(according to requirements)for whole AMP there is one setting,So it's one record in table;
		Session session=null;
		String queryString =null;
		Query query=null;
		AmpMessageSettings returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();			
			queryString= "select a from " + AmpMessageSettings.class.getName()+ " a";
			query=session.createQuery(queryString);
			returnValue=(AmpMessageSettings)query.uniqueResult();
		}catch(Exception ex) {
			logger.error("couldn't load Settings" + ex.getMessage());			
		}
		return returnValue;
	}
	
	public static boolean saveOrUpdateSettings(AmpMessageSettings setting) throws Exception{
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(setting);
			tx.commit();
		}catch(Exception ex) {
			if(tx!=null) {
				try {
					tx.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("update failed",ex);
		}
		return true;
	}

	/**
	 * returns domain 
	 */
	public static String getCurrentURL(HttpServletRequest request) throws DgException {
		String partialURL=DgUtil.getCurrRootUrl(request);
		if(!partialURL.endsWith("/")){
			partialURL+="/";
		}
		return partialURL;
	}
	
	/**
	 * checks if any of the inbox is full
	 */
	public static <E extends AmpMessage> boolean isInboxFull(Class<E> clazz, Long tmId) throws Exception{
		boolean full=false;
		int hiddenMsgs=0;
		Session session=null;
		String queryString =null;
		Query query=null;	
		
		try {			
			session=PersistenceManager.getRequestDBSession();	
			queryString="select count(state.id) from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.receiver.ampTeamMemId=:tmId and msg.draft=false and state.messageHidden=true";	
			query=session.createQuery(queryString);			
			query.setParameter("tmId", tmId);			
			hiddenMsgs=((Integer)query.uniqueResult()).intValue();
			if(hiddenMsgs>0){
				full=true;
			}
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);			
		}
		return full;
	}	
	
	
	public static <E extends AmpMessage> boolean isSentOrDraftFull(Class<E> clazz, Long tmId,boolean draft) throws Exception{
		boolean full=false;
		int hiddenMsgs=0;	
		Session session=null;
		String queryString =null;
		Query query=null;			
		try {			
			session=PersistenceManager.getRequestDBSession();	
			queryString="select count(state.id) from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.senderId = :tmId and msg.draft="+draft+" and state.messageHidden=true";	
			query=session.createQuery(queryString);			
			query.setLong("tmId", tmId);	
			if(query.list().size()>0){
				hiddenMsgs=((Integer)query.uniqueResult()).intValue();
				if(hiddenMsgs>0){
					full=true;
				}
			}
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);			
		}
		return full;
	}
	
	
	
	/**
	 * Gets Members Ids whos Inbox messages are more then allowed max.Storage and have to be filtered	 
	 * @return members Ids
	 */
	public static <E extends AmpMessage> List<Long> getOverflowedMembersIdsForInbox(int limit,Class<E> clazz){
		Session session=null;
		String queryString =null;
		Query query=null;
		List<Long> memIds=null;
		try {
			session=PersistenceManager.getRequestDBSession();			
			queryString= "select s.receiver.ampTeamMemId from " + AmpMessageState.class.getName()+ " s, "+clazz.getName()+
			" msg  where msg.id=s.message.id and s.receiver.ampTeamMemId is not null group by s.receiver.ampTeamMemId having count(s.id)>"+limit;
			query=session.createQuery(queryString);
			memIds=query.list();
		}catch(Exception ex) {
			logger.error("couldn't load member ids" + ex.getMessage());			
		}
		return memIds;
	}
	
	/**
	 * Gets Senders Ids whos Sent or draft messages are more then allowed max.Storage and have to be filtered
	 * @return senders ids
	 */
	public static <E extends AmpMessage> List<Long> getOverflowedMembersIdsForSentOrDraft(int limit,Class<E> clazz,Boolean draft){
		Session session=null;
		String queryString =null;
		Query query=null;
		List<Long> senderIds=null;
		try {
			session=PersistenceManager.getRequestDBSession();			
			queryString= "select s.senderId from " + AmpMessageState.class.getName()+ " s, "+clazz.getName()+
			" msg  where msg.id=s.message.id and s.senderId is not null and msg.draft="+draft+" group by s.senderId having count(s.id)>"+limit;
			query=session.createQuery(queryString);
			senderIds=query.list();
		}catch(Exception ex) {
			logger.error("couldn't load sender ids" + ex.getMessage());			
		}
		return senderIds;
	}
	
	/**
	 * Returns first message ,which is hidden. Used to then change it's state to visible if someone deleted visible message in inbox  
	 */
	public static <E extends AmpMessage> AmpMessageState getFirstHiddenInboxMessage(Class<E> clazz,Long tmId) throws Exception{
		AmpMessageState state=null;
		Session session=null;
		String queryString =null;
		Query query=null;		
		try {			
			session=PersistenceManager.getRequestDBSession();	
			queryString="select state from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.receiver.ampTeamMemId=:tmId and msg.draft=false and state.messageHidden=true order by msg.creationDate";	
			query=session.createQuery(queryString);
			query.setMaxResults(1);			
			query.setParameter("tmId", tmId);			
			state=(AmpMessageState)query.uniqueResult();			
		}catch(Exception ex) {
			logger.error("couldn't load Message" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Message", ex);			
		}
		return state;
	}
	
	/**
	 * Returns first message ,which is hidden. Used to then change it's state to visible if someone deleted visible message in inbox  
	 */
	public static <E extends AmpMessage> AmpMessageState getFirstHiddenSentOrDraftMessage(Class<E> clazz,Long tmId,boolean draft) throws Exception{
		AmpMessageState state=null;
		Session session=null;
		String queryString =null;
		Query query=null;		
		try {			
			session=PersistenceManager.getRequestDBSession();	
			queryString="select state from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.senderId=:tmId and msg.draft="+draft+" and state.messageHidden=true order by msg.creationDate";	
			query=session.createQuery(queryString);
			query.setMaxResults(1);			
			query.setParameter("tmId", tmId);			
			state=(AmpMessageState)query.uniqueResult();			
		}catch(Exception ex) {
			logger.error("couldn't load Message" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Message", ex);			
		}
		return state;
	}
	
	/**
	 * updates first @limit amount of hidden messages and makes them visible
	 */
	public static <E extends AmpMessage> void updateHiddenInboxMsgsToVisible(Class<E> clazz,Long tmId,int limit) throws Exception{
		Session session=null;
		Query query=null;
		String queryString =null;
		List<Long> hiddenMsgsIds=null;				
		try {			
			session=PersistenceManager.getRequestDBSession();
				
			queryString="select state.id from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.receiver.ampTeamMemId=:tmId and msg.draft=false";	
			query=session.createQuery(queryString);
			query.setMaxResults(limit);			
			query.setParameter("tmId", tmId);
			hiddenMsgsIds=query.list();
			
			updateMsgHiddenVisibleState(session, hiddenMsgsIds,false);			
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);			
		}
	}
	
	/**
	 * updates first @limit amount of hidden messages and makes them visible
	 */
	public static <E extends AmpMessage> void updateHiddenSentOrDraftMsgsToVisible(Class<E> clazz,Long tmId,Boolean draft,int limit) throws Exception{
		List<Long> hiddenMsgsIds=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {			
			session=PersistenceManager.getRequestDBSession();
			queryString="select state.id from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.senderId=:tmId and msg.draft="+draft;	
			query=session.createQuery(queryString);
			query.setMaxResults(limit);
			query.setParameter("tmId", tmId);
			hiddenMsgsIds=query.list();
			
			updateMsgHiddenVisibleState(session, hiddenMsgsIds,false);
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);			
		}
	}
	
	/**
	 * hides last @limit amount of visible inbox  messages
	 */
	public static <E extends AmpMessage> void updateVisibleInboxMsgsToHidden(Class<E> clazz,Long tmId,int limit) throws Exception{
		List<Long> hiddenMsgsIds=null;	
		Session session=null;
		String queryString =null;
		Query query=null;		
		try {			
			session=PersistenceManager.getRequestDBSession();	
			queryString="select state.id from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.receiver.ampTeamMemId=:tmId and msg.draft="+false;	
			query=session.createQuery(queryString);
			query.setFirstResult(limit);
			query.setParameter("tmId", tmId);
			hiddenMsgsIds=query.list();
			
			updateMsgHiddenVisibleState(session, hiddenMsgsIds,true);
			
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);			
		}
	}
	
	/**
	 * hides last @limit amount of visible sent/draft messages
	 */
	public static <E extends AmpMessage> void updateVisibleSentOrDraftMsgsToHidden(Class<E> clazz,Long tmId,Boolean draft,int limit) throws Exception{
		List<Long> hiddenMsgsIds=null;	
		Session session=null;
		String queryString =null;
		Query query=null;		
		try {			
			session=PersistenceManager.getRequestDBSession();
			queryString="select state.id from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.senderId=:tmId and msg.draft="+draft;	
			query=session.createQuery(queryString);
			query.setFirstResult(limit);			
			query.setParameter("tmId", tmId);			
			hiddenMsgsIds=query.list();
			
			updateMsgHiddenVisibleState(session, hiddenMsgsIds,true);
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);			
		}
	}
	
	/**
	 * show all hidden inbox messages
	 * @param <E> User message,alert,approval or calendar event
	 * @param clazz
	 * @throws Exception
	 */
	public static <E extends AmpMessage> void updateAllHiddenInboxMessagesToVisible(Class<E> clazz) throws Exception{
		List<Long> statesIds=null;
		Session session=null;
		String queryString =null;
		Query query=null;		
		try {			
			session=PersistenceManager.getRequestDBSession();	
			queryString="select state.id from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.senderId is null and state.messageHidden=true order by msg.creationDate";	
			query=session.createQuery(queryString);
			statesIds=query.list();
			
			updateMsgHiddenVisibleState(session, statesIds,false);
		}catch(Exception ex) {
			logger.error("couldn't load Message" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Message", ex);			
		}
	}
	
	/**
	 * show all hidden sent/draft messages
	 * @param <E> User message,alert
	 * @param clazz
	 * @param draft
	 * @throws Exception
	 */
	public static <E extends AmpMessage> void updateAllSentOrDrartHiddenMsgsToVisible(Class<E> clazz,boolean draft) throws Exception{
		List<Long> statesIds=null;
		Session session=null;
		String queryString =null;
		Query query=null;		
		try {			
			session=PersistenceManager.getRequestDBSession();	
			queryString="select state.id from "+AmpMessageState.class.getName()+" state, "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.senderId is not null and msg.draft="+draft+" and state.messageHidden=true order by msg.creationDate";	
			query=session.createQuery(queryString);
			statesIds=query.list();
			
			updateMsgHiddenVisibleState(session, statesIds,false);
		}catch(Exception ex) {
			logger.error("couldn't load Message" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Message", ex);			
		}
	}
	
	 //change message visibility states to @hidden state
	public static void updateMsgHiddenVisibleState(Session session, List<Long> statesIds,boolean hidden) {
		Query query;
		if(statesIds!=null && statesIds.size()>0){
			while(statesIds.size()>0){
				int toIndex=statesIds.size()>1000 ? 1000 : statesIds.size();
			    List<Long> ids=statesIds.subList(0, toIndex);
			    statesIds=statesIds.subList(toIndex, statesIds.size());
			    String qhl="update " + AmpMessageState.class.getName()+" state set state.messageHidden="+hidden+" where state.id in (:hiddenMsgsIds)";
			    query=session.createQuery(qhl); 
			    query.setParameterList("hiddenMsgsIds", ids);
			    query.executeUpdate();
			 }               
		}
	}
	
	public static String buildDateFromEvent(Date date){
        String pattern = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
        if (pattern == null) {
            pattern = "dd/MM/yyyy";
        }
        pattern+=" HH:mm a";
        
        SimpleDateFormat formater=new SimpleDateFormat(pattern);
		String result = formater.format(date);
		return result;	
	}
	
	public static String[] buildExternalReceiversFromContacts() throws Exception{ //used in add message form
		String[] retVal=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		List contacts=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString="select concat(prop.contact.name,"+"' ',"+"prop.contact.lastname), prop.value from " + AmpContactProperty.class.getName() + " prop where prop.name=:contEmail" +
					" and prop.value is not null and trim(prop.value) like '%@%.%'  and prop.contact.name is not null and trim(prop.contact.name)!='' and prop.contact.lastname is not null and trim(prop.contact.lastname)!=''";
			query=session.createQuery(queryString);
			query.setString("contEmail", Constants.CONTACT_PROPERTY_NAME_EMAIL);
			contacts=query.list();
		} catch (Exception ex) {
			logger.error("couldn't load Contacts " + ex.getMessage());	
			ex.printStackTrace(); 
		}
		
		if(contacts!=null){
			retVal=new String[contacts.size()];
			int i=0;
			for (Object contRow : contacts) {
				Object[] row = (Object[])contRow;
				String contact=(String)row[0];
				String contEmail=(String)row[1];
				
				if(contact!=null){
					retVal[i]=contact+ " <" + contEmail + ">";
				}
				i++;
			}
		}
		
		return retVal;
	}
}
