package org.digijava.module.message.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Team;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.Approval;
import org.digijava.module.message.dbentity.CalendarEvent;
import org.digijava.module.message.dbentity.UserMessage;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.helper.MessageHelper;
import org.digijava.module.message.util.AmpMessageUtil;

public class AmpMessageActions extends DispatchAction {
	
	public static final String ROOT_TAG = "Messaging";
	public static final String MESSAGES_TAG = "MessagesList";
	public static final String PAGINATION_TAG = "Pagination";
	
    
    public ActionForward fillTypesAndLevels (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {        		
    		AmpMessageForm messageForm=(AmpMessageForm)form;    		
    		if(request.getParameter("editingMessage").equals("false")){
    			//load activities
    	    	messageForm.setRelatedActivities(ActivityUtil.loadActivitiesNamesAndIds());
    			setDefaultValues(messageForm);
    		}else {
    			Long id=new Long(request.getParameter("msgStateId"));    			
    	    	AmpMessageState state=AmpMessageUtil.getMessageState(id);
    	    	fillFormFields(state.getMessage(),messageForm,id,true);    	
    		}
		 return loadReceiversList(mapping,form,request,response);	
	}
    
   /**
    * user clicked cancel on view Messages page   
    */ 
    public ActionForward cancelMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {    	
    	//AmpMessageForm alertsForm=(AmpMessageForm)form;
    	//setDefaultValues(alertsForm);
        return mapping.findForward("showAllMessages");
    	
    }
    
    public ActionForward gotoMessagesPage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession();
    	
    	TeamMember teamMember = new TeamMember();        	  
    	 // Get the current team member 
    	teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);   	
    	
    	AmpMessageForm messageForm=(AmpMessageForm)form;
    	AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
    	if(settings!=null && settings.getMsgRefreshTime()!=null && settings.getMsgRefreshTime().longValue()>0){
    	
    		messageForm.setMsgRefreshTimeCurr(settings.getMsgRefreshTime());
    		messageForm.setMsgStoragePerMsgTypeCurr(settings.getMsgStoragePerMsgType());
			messageForm.setDaysForAdvanceAlertsWarningsCurr(settings.getDaysForAdvanceAlertsWarnings());
			messageForm.setMaxValidityOfMsgCurr(settings.getMaxValidityOfMsg());
			messageForm.setEmailMsgsCurrent(settings.getEmailMsgs());
			messageForm.setAllmsg(AmpMessageUtil.getInboxMessagesCount(UserMessage.class,teamMember.getMemberId()));
			
			
    	}  else{
    		messageForm.setMsgRefreshTimeCurr(new Long(-1));
    	}
    	
    	int tabIndex=0;
    	if(request.getParameter("tabIndex")!=null){
    		tabIndex=Integer.parseInt(request.getParameter("tabIndex"));
    	}else {
    		tabIndex=messageForm.getTabIndex();
    	}  
    	messageForm.setTabIndex(tabIndex);
    	
    	String childTab=null;
    	if(request.getParameter("childTab")!=null){
    		childTab=request.getParameter("childTab");
    	}else {
    		childTab=messageForm.getChildTab();
    	}
    	if(childTab==null){
    		childTab="inbox";
    	}
    	messageForm.setChildTab(childTab);
   
    	return mapping.findForward("showAllMessages");
    }
    
    /**    
     * @return All Messages that belong to this Team Member. 
     * @throws Exception
     */
    public ActionForward viewAllMessages(ActionMapping mapping,	ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	
    	AmpMessageForm messageForm=(AmpMessageForm)form;
    	
    	HttpSession session = request.getSession();
    	TeamMember teamMember = new TeamMember();        	  
    	 // Get the current team member 
    	teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);   	
    	
    	
    	
    	
    	
    	int tabIndex=0;
    	if(request.getParameter("tabIndex")!=null){
    		tabIndex=Integer.parseInt(request.getParameter("tabIndex"));
    	}else {
    		tabIndex=messageForm.getTabIndex();
    	}    	    	
    	
    	String childTab=null;
    	if(request.getParameter("childTab")!=null){
    		childTab=request.getParameter("childTab");
    	}else {
    		childTab=messageForm.getChildTab();
    	}
    	
    	List<AmpMessageState> allMessages=null; //all messages
    	AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
    	
    	int page=1;
    	if(request.getParameter("page")!=null){
    		page=Integer.parseInt(request.getParameter("page"));
    	}
    	
    	int howManyPages=0;
    	int count=0;
    	
    	if(tabIndex==1){ //<-----messages    
    		if(childTab==null || childTab.equalsIgnoreCase("inbox")){
    			//how many messages are in db. used for pagination
    			count=AmpMessageUtil.getInboxMessagesCount(UserMessage.class,teamMember.getMemberId());
    			allMessages =AmpMessageUtil.loadAllInboxMessagesStates(UserMessage.class,teamMember.getMemberId(),-1,page);        		
    		}else if(childTab.equalsIgnoreCase("sent")){
    			//how many messages are in db. used for pagination
    			count=AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class,teamMember.getMemberId(),false);
    			allMessages=AmpMessageUtil.loadAllSentOrDraftMessagesStates(UserMessage.class,teamMember.getMemberId(),-1, false,page);
    		}else if(childTab.equalsIgnoreCase("draft")){
    			//how many messages are in db. used for pagination
    			count=AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class,teamMember.getMemberId(),true);
    			allMessages=AmpMessageUtil.loadAllSentOrDraftMessagesStates(UserMessage.class,teamMember.getMemberId(),-1, true,page);
    		}    		
    	}else if(tabIndex==2){// <--alerts
    		if(childTab==null || childTab.equalsIgnoreCase("inbox")){
    			//how many messages are in db. used for pagination
    			count=AmpMessageUtil.getInboxMessagesCount(AmpAlert.class,teamMember.getMemberId());
    			allMessages =AmpMessageUtil.loadAllInboxMessagesStates(AmpAlert.class,teamMember.getMemberId(),-1,page);        		
    		}else if(childTab.equalsIgnoreCase("sent")){
    			//how many messages are in db. used for pagination
    			count=AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class,teamMember.getMemberId(),false);
    			allMessages=AmpMessageUtil.loadAllSentOrDraftMessagesStates(AmpAlert.class,teamMember.getMemberId(),-1, false,page);
    		}else if(childTab.equalsIgnoreCase("draft")){
    			//how many messages are in db. used for pagination
    			count=AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class,teamMember.getMemberId(),true);
    			allMessages=AmpMessageUtil.loadAllSentOrDraftMessagesStates(AmpAlert.class,teamMember.getMemberId(),-1, true,page);
    		}    		
    	}else if(tabIndex==3){// <---approvals
    		//how many messages are in db. used for pagination
			count=AmpMessageUtil.getInboxMessagesCount(Approval.class,teamMember.getMemberId());
    		allMessages =AmpMessageUtil.loadAllInboxMessagesStates(Approval.class,teamMember.getMemberId(),-1,page);    		
    	}else if(tabIndex==4){// <--calendar events
    		//how many messages are in db. used for pagination
			count=AmpMessageUtil.getInboxMessagesCount(CalendarEvent.class,teamMember.getMemberId());
    		allMessages =AmpMessageUtil.loadAllInboxMessagesStates(CalendarEvent.class,teamMember.getMemberId(),-1,page);    		
    	}
    	Collections.reverse(allMessages);
    	messageForm.setMessagesForTm(allMessages);
    	
    	
    	messageForm.setTabIndex(tabIndex);
    	
    	if(childTab==null){
    		childTab="inbox";
    	}
    	messageForm.setChildTab(childTab);    	
    	
    	//pagination
    	List<AmpMessageState> messages=messageForm.getMessagesForTm();  
    	List<AmpMessageState> msgs=messageForm.getMessagesForTm();
    	/**
    	 * if max.Storage per message type is less than messages in db,then we should show only max.Storage amount messages.
    	 *and pages quantity will depend on max.Storage. In other case,we will show all messages and pages amount will depend on messages amount 
    	 */    	
    	if(settings!=null && settings.getMsgStoragePerMsgType()!=null && settings.getMsgStoragePerMsgType().intValue()<=count){
    		count=settings.getMsgStoragePerMsgType().intValue();
    		//if(count<messages.size()){
    			if(page==1){
    				if(count<messages.size()){
    					messageForm.setMessagesForTm(messages.subList(0,count));
    				}    				
    			}else if(page>1){
    				int toIndex=count-(page-1)*MessageConstants.MESSAGES_PER_PAGE;
    				if(toIndex<messages.size()){    
    					messageForm.setMessagesForTm(null);
    					msgs=new ArrayList<AmpMessageState>();    					
    					for (int i=0;i<toIndex;i++) {
    						msgs.add(messages.get(i));							
						}
    					messageForm.setMessagesForTm(msgs);
    				}    				
    			}    			
    		//}    		   
    	}
    	if(count%MessageConstants.MESSAGES_PER_PAGE==0){ 
			howManyPages=count/MessageConstants.MESSAGES_PER_PAGE;
		}else {
			howManyPages=(count/MessageConstants.MESSAGES_PER_PAGE)+1;
		} 
    	
    		// we always have at least 1 page
    		howManyPages=howManyPages==0?1:howManyPages;
    		
    		String[] allPages=new String[howManyPages==0?1:howManyPages];
    		
    		for (int i=1;i<=howManyPages;i++) {
				allPages[i-1]=Integer.toString(i);
			}
    		
    		messageForm.setAllPages(allPages);
    		
    		if(messageForm.getPage()==null){
    			if(request.getParameter("page")!=null){
    				messageForm.setPage(request.getParameter("page"));
    			}else{
    				messageForm.setPage("1");
    			}    			
    		}

    		messageForm.setLastPage(Integer.toString(howManyPages));
    		messageForm.setPagesToShow(MessageConstants.PAGES_TO_SHOW);

    	messageForm.setPagedMessagesForTm(msgs);
    	
    	response.setContentType("text/xml");
		OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
		PrintWriter out = new PrintWriter(outputStream, true);
		String xml = messages2XML(messageForm.getPagedMessagesForTm(),messageForm);
		out.println(xml);
		out.close();
		// return xml			
		outputStream.close();
		if(messageForm.isDeleteActionWasCalled()){
			messageForm.setDeleteActionWasCalled(false);
		}
    	return null;
    }
        
    /**
     * fills Form to view selected Message    
     */
    public ActionForward viewSelectedMessage(ActionMapping mapping,	ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	
    	   AmpMessageForm messagesForm = (AmpMessageForm) form;
        AmpMessage message = null;
        boolean isMessageStateId=true;
        Long id=null;
        if (request.getParameter("msgStateId") != null) {
            id = new Long(request.getParameter("msgStateId"));
            AmpMessageState msgState = AmpMessageUtil.getMessageState(id);
            msgState.setRead(true);
            AmpMessageUtil.saveOrUpdateMessageState(msgState);
            message = msgState.getMessage();            
        } else {
            // view forwarded message
            id = new Long(request.getParameter("msgId"));
            message = AmpMessageUtil.getMessage(id);
            isMessageStateId=false;
        }
        fillFormFields(message, messagesForm, id,isMessageStateId);
        return mapping.findForward("viewMessage");
    }
    
    public ActionForward makeMsgRead(ActionMapping mapping,	ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	
    	AmpMessageState state=null;
    	Long msgStateId=null;
    	if(request.getParameter("msgStateId")!=null){
    		msgStateId=new Long(request.getParameter("msgStateId"));
    		state=AmpMessageUtil.getMessageState(msgStateId);
    		if(state.getSenderId()==null){
    			state.setRead(true);
    		}    		
    		AmpMessageUtil.saveOrUpdateMessageState(state);
    		
    		//creating xml that will be returned
    		response.setContentType("text/xml");
    		OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
    		PrintWriter out = new PrintWriter(outputStream, true);
    		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    		xml += "<" + ROOT_TAG +">";
    		xml+="<"+"message id=\""+msgStateId+"\" ";
    		xml+="read=\""+state.getRead()+"\" ";
                xml+="msgId=\""+state.getMessage().getId()+"\" ";
    		xml+="/>";
    		xml+="</"+ROOT_TAG+">";
    		out.println(xml);
			out.close();
			// return xml			
			outputStream.close();
    	}    	
    	return null;
    }
    
    /*
     * used to check for new messages from desktop 
     */
    public ActionForward checkForNewMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	AmpMessageForm messagesForm=(AmpMessageForm)form;
    	
    	HttpSession session = request.getSession();
    	TeamMember teamMember = new TeamMember();        	  
    	 // Get the current member who has logged in from the session
    	teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
    	
    	//separate message types: used on myMessages page on desktop
		int alertType=0;
		int msgType=0;
		int approvalType=0;
		int calEventType=0;
		msgType=AmpMessageUtil.getUnreadMessagesAmountPerMsgType(UserMessage.class, teamMember.getMemberId());
		alertType=AmpMessageUtil.getUnreadMessagesAmountPerMsgType(AmpAlert.class, teamMember.getMemberId());
		approvalType=AmpMessageUtil.getUnreadMessagesAmountPerMsgType(Approval.class, teamMember.getMemberId());
		calEventType=AmpMessageUtil.getUnreadMessagesAmountPerMsgType(CalendarEvent.class, teamMember.getMemberId());
		
		//checking if inbox is full
		AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
		if(settings!=null && settings.getMsgStoragePerMsgType()!=null && settings.getMsgStoragePerMsgType().intValue()>0){
			int maxStorage=settings.getMsgStoragePerMsgType().intValue();
			if(msgType<=maxStorage || alertType<=maxStorage|| approvalType<=maxStorage ||calEventType<=maxStorage){
				messagesForm.setInboxFull(true);
			}
		}		
		
		//creating xml that will be returned   		
		
		response.setContentType("text/xml");
		OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
		PrintWriter out = new PrintWriter(outputStream, true);
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xml += "<" + ROOT_TAG +">";
		xml+="<"+"amount messages=\""+msgType+"\" ";
		xml+="alerts=\""+alertType+"\" ";
		xml+="approvals=\""+approvalType+"\" ";
		xml+="calEvents=\""+calEventType+"\" ";
		xml+="inboxFull=\""+messagesForm.isInboxFull()+"\" ";
		xml+="/>";
		xml+="</"+ROOT_TAG+">";
		out.println(xml);
		out.close();
		// return xml			
		outputStream.close();
    	return null;
    }
    
   /**
     * used when user clicks on forward link 
     */
    public ActionForward forwardMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	AmpMessageForm messagesForm=(AmpMessageForm)form;
        setDefaultValues(messagesForm);
    	if(request.getParameter("fwd")!=null && request.getParameter("fwd").equals("fillForm")){
    		Long stateId=new Long(request.getParameter("msgStateId"));
        	AmpMessageState oldMsgsState=AmpMessageUtil.getMessageState(stateId);
        	AmpMessage msg=oldMsgsState.getMessage();
                
                // for Bread-crumb Generation. we need to which tab we must return..
                if (msg instanceof AmpAlert) {
                    messagesForm.setTabIndex(2);
                } else {
                    if (msg instanceof Approval) {
                        messagesForm.setTabIndex(3);
                    } else {
                        if (msg instanceof CalendarEvent) {
                            messagesForm.setTabIndex(4);
                        } else {
                            messagesForm.setTabIndex(1);
                        }
                    }
                }                
                
            messagesForm.setMessageName("FWD: "+ msg.getName());
        	MessageHelper msgHelper=createHelperMsgFromAmpMessage(msg,stateId);
        	messagesForm.setForwardedMsg(msgHelper);
        	messagesForm.setRelatedActivities(ActivityUtil.loadActivitiesNamesAndIds());
    	}
    	return loadReceiversList(mapping,messagesForm,request,response);	
    }
    
    /**
     * Removes selected Message
    */
    public ActionForward removeSelectedMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {    	
    	AmpMessageForm messagesForm=(AmpMessageForm)form;
    	messagesForm.setDeleteActionWasCalled(true);
    	//remove from db
    	AmpMessageUtil.removeMessageState(messagesForm.getMsgStateId());	
    	return viewAllMessages(mapping, messagesForm, request, response);
    }    
 
    
    /**
     * loads List of Receivers according to the receiverType(all,Users,Team,TM)
     * @author Dare Roinishvili
    */
    public ActionForward loadReceiversList(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	
    	AmpMessageForm messagesForm=(AmpMessageForm)form;    	
    	messagesForm.setTeamsMap(null);    	
    	
    	Map<String, Team> teamMap=new HashMap<String, Team>();
    	List<AmpTeam> teams=(List<AmpTeam>)TeamUtil.getAllTeams();
    	if(teams!=null && teams.size()>0){
    		for (AmpTeam ampTeam : teams) {
				if(!teamMap.containsKey("t"+ampTeam.getAmpTeamId())){
					Team team=new Team();
					team.setId(ampTeam.getAmpTeamId());
					team.setName(ampTeam.getName());
					//getting members of that team
					List<TeamMember> teamMembers=(List<TeamMember>)TeamMemberUtil.getAllTeamMembers(team.getId());
					team.setMembers(teamMembers);
					//putting team to Map
					teamMap.put("t"+team.getId(), team); //if teamId=2 then the key will be t2. t=team
				}
			}
    	}    	
    	messagesForm.setTeamsMap(teamMap);
    	
    	return mapping.findForward("addOrEditAmpMessage");	
    }
    
 
    /**     
     * add Message
     */
    public ActionForward addMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,	HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
    	TeamMember teamMember = new TeamMember();        	  
    	 // Get the current member who has logged in from the session
    	teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
    	//getting settings for message
    	AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
    	
    	AmpMessageForm messageForm=(AmpMessageForm)form;    	
    	AmpMessage message=null; 
    	String[] messageReceivers=messageForm.getReceiversIds();
    	
    	if(messageForm.getMessageId()==null) {
    		if(messageForm.getSetAsAlert()==0){
    			message=new UserMessage();
    		}else {
    			message=new AmpAlert(); 
    		}    		    		
    	}else {
    		if(messageForm.getSetAsAlert()==0){
    			message=new UserMessage();
    		}else {    			
    			message=new AmpAlert();  
    		}
    		//remove all States that were associated to this message
			List<AmpMessageState> statesAssociatedWithMsg=AmpMessageUtil.loadMessageStates(messageForm.getMessageId());
			for (AmpMessageState state : statesAssociatedWithMsg) {
				AmpMessageUtil.removeMessageState(state.getId());
			}
			//remove message
			AmpMessageUtil.removeMessage(messageForm.getMessageId());
    	}
        if(message instanceof AmpAlert){
             messageForm.setTabIndex(2); // to navigate to the Alert Tab
        }
        else{
             messageForm.setTabIndex(1);// to navigate to the Message Tab
        }
    	message.setName(messageForm.getMessageName());
    	message.setDescription(messageForm.getDescription());
    	message.setMessageType(messageForm.getMessageType());  
    	message.setPriorityLevel(messageForm.getPriorityLevel());
    	//This is User. (because this action happens only when user adds a message from the homepage.)
    	message.setSenderType(MessageConstants.SENDER_TYPE_USER); 
    	message.setSenderId(teamMember.getMemberId());
        User user=TeamMemberUtil.getAmpTeamMember(teamMember.getMemberId()).getUser();
        String senderName=user.getFirstNames()+" "+user.getLastName()+"<"+user.getEmail()+">";
        message.setSenderName(senderName);
    	/**
    	 * this will be filled only when we are forwarding a message
    	 */
    	if(messageForm.getForwardedMsg()!=null){
    		message.setForwardedMessage(AmpMessageUtil.getMessage(messageForm.getForwardedMsg().getMsgId()));
    	} 
    	//link message to activity if necessary
    	if(messageForm.getSelectedAct()!=null && messageForm.getSelectedAct().length()>0){ 
    		String act=messageForm.getSelectedAct();
    		String activityId=act.substring(act.lastIndexOf("(")+1,act.lastIndexOf("")-1);
    		message.setRelatedActivityId(new Long(activityId));
    		//now we must create activity URL
    		String fullModuleURL=RequestUtils.getFullModuleUrl(request);
    		String objUrl=fullModuleURL.substring(0,fullModuleURL.indexOf("message"))+"aim/viewChannelOverview.do~tabIndex=0~ampActivityId="+activityId;
    		message.setObjectURL(objUrl);
    	}
    	//should we send a message or not
    	if(request.getParameter("toDo")!=null){
    		if(request.getParameter("toDo").equals("draft")){
    			message.setDraft(true);
    		}else {
    			message.setDraft(false);
    		}
    	}
    	
    	 	 
    		Calendar cal=Calendar.getInstance();
    		message.setCreationDate(cal.getTime());   	
    	      	 	
    	//saving message
    	AmpMessageUtil.saveOrUpdateMessage(message);   
    	
    	//now create one state, with senderId=teamMemberId. This AmpMessageState will be used further to see sent messages
   		AmpMessageState state=new AmpMessageState();
       	state.setMessage(message);
       	state.setSender(teamMember.getMemberName());
       	state.setSenderId(teamMember.getMemberId());
       	AmpMessageUtil.saveOrUpdateMessageState(state);        	
    	    	
    	
    	
    	List<AmpMessageState> statesList=AmpMessageUtil.loadMessageStates(messageForm.getMessageId());  
    	List<Long> statesMemberIds=new ArrayList<Long>();
    	if(statesList==null){
    		statesList=new ArrayList<AmpMessageState>();
    	}
    	
    	if(statesList!=null && statesList.size()>0){
			//getting members Ids from states list			
			for (AmpMessageState mId : statesList) {
				statesMemberIds.add(mId.getMemberId());
			}    			    			
    	}	
		if(messageReceivers!=null && messageReceivers.length>0){
			Address [] addresses=new Address [messageReceivers.length];
			int addressIndex=0;
			for (String receiver : messageReceivers) {				
				if(!receiver.startsWith("m")){//<--this means that receiver is team
					List<TeamMember> teamMembers=null;
                                        if(receiver.startsWith("t")){
                                               teamMembers=(List<TeamMember>)TeamMemberUtil.getAllTeamMembers(new Long(receiver.substring(2)));
                                        }
                                        else{
                                             teamMembers=(List<TeamMember>)TeamMemberUtil.getAllTeamMembers(null);
                                        }
					if(teamMembers!=null && teamMembers.size()>0){
						for (TeamMember tm : teamMembers) {
							if(! statesMemberIds.contains(tm.getMemberId())){
								createMessageState(message,tm.getMemberId(),teamMember.getMemberName());
								if(settings!=null && settings.getEmailMsgs()!=null && settings.getEmailMsgs().equals(new Long(1))){
									//creating internet address where the mail will be sent
									addresses[addressIndex]=new InternetAddress(tm.getEmail());
									addressIndex++;									
								}
							}
						}
					}
					
				}else {//<--receiver is team member
					if(! statesMemberIds.contains(new Long(receiver.substring(2)))){
						Long memId=new Long(receiver.substring(2));
						createMessageState(message,memId,teamMember.getMemberName());
						if(settings!=null && settings.getEmailMsgs()!=null && settings.getEmailMsgs().equals(new Long(1))){
							//creating internet address where the mail will be sent
							addresses[addressIndex]=new InternetAddress(TeamMemberUtil.getAmpTeamMember(memId).getUser().getEmail());
							addressIndex++;							
						}
					}
				}				
			}
			
			if(settings!=null && settings.getEmailMsgs()!=null && settings.getEmailMsgs().equals(new Long(1))){
				if(request.getParameter("toDo")!=null && !request.getParameter("toDo").equals("draft")){
		    		DgEmailManager.sendMail(addresses, teamMember.getEmail(), message.getName(), message.getDescription());
		    	}
			}
           AmpMessageUtil.saveOrUpdateMessage(message);			
		}
		
    	//cleaning form values
    	setDefaultValues(messageForm);
   	    if (request.getParameter("toDo")!=null&&request.getParameter("toDo").equals("draft")) {
              //  messageForm.setChildTab("draft");
            return mapping.findForward("showAllMessages");
        }
            else{
    	
		return mapping.findForward("viewMyDesktop");
            }
	}   
    
    
    private void createMessageState(AmpMessage message,Long memberId,String senderName) throws Exception{
    	AmpMessageState newMessageState=new AmpMessageState();
		newMessageState.setMessage(message);
		newMessageState.setSender(senderName);
		newMessageState.setMemberId(memberId);	
                String receivers = message.getReceivers();
                if (receivers == null) {
                    receivers = "";
                } else {
                    if (receivers.length() > 0) {
                        receivers += ", ";
                    }
                }
                User user=TeamMemberUtil.getAmpTeamMember(memberId).getUser();
                receivers+=user.getFirstNames()+" "+user.getLastName()+"<"+user.getEmail()+">";
                message.setReceivers(receivers);
		newMessageState.setRead(false);
		//saving current state in db
		AmpMessageUtil.saveOrUpdateMessageState(newMessageState);
    }
    
    /**
     * clear form    
     */
	 private void setDefaultValues(AmpMessageForm form) {
		 form.setEditingMessage(false);
		 form.setMessageId(null);
		 form.setMessageName(null);
		 form.setDescription(null);
		 form.setPriorityLevel(new Long(0));
		 form.setMessageType(new Long(0));
		 form.setSenderType(null);
		 form.setSenderId(null);		
		 form.setTeamsMap(null);
		 form.setReceiversIds(null);	 
		 form.setClassName(null);
		 form.setMsgStateId(null);
		 form.setReceivers(null);
		 form.setSender(null);
		 //form.setTabIndex(1);
		 form.setMsgType(0);
		 form.setAlertType(0);
		 form.setCalendarEventType(0);
		 form.setApprovalType(0);
		 //form.setChildTab("inbox");
		 form.setSetAsAlert(0);
		 form.setForwardedMsg(null);
		 form.setPage(null);
		 form.setAllPages(null);
		 form.setPagedMessagesForTm(null);
		 form.setLastPage(null);
		 form.setDeleteActionWasCalled(false);
         form.setReceiver(null);
         form.setSelectedAct(null);  
         form.setInboxFull(false);
	 }
	 
	 private void fillFormFields (AmpMessage message,AmpMessageForm form,Long id,boolean isStateId) throws Exception{	 
		 if(message!=null){
			 form.setMessageId(message.getId());
			 form.setMessageName(message.getName());
			 form.setDescription(message.getDescription());
			 form.setMessageType(message.getMessageType());
			 form.setPriorityLevel(message.getPriorityLevel());
			 form.setSenderType(MessageConstants.SENDER_TYPE_USER); //this message is created by User
			 form.setSenderId(message.getSenderId()); 	 
			 form.setCreationDate(DateConversion.ConvertDateToString(message.getCreationDate()));		 
			 form.setClassName(message.getClassName());
			 form.setObjectURL(message.getObjectURL());
             form.setReceiver(message.getReceivers());
	         if(message.getSenderType().equalsIgnoreCase("User")){
	        	 form.setSender(message.getSenderName());
	         } else{
	        	 form.setSender(message.getSenderType());
	         }
             if(isStateId){
            	 form.setMsgStateId(id);
	             AmpMessage msg = message.getForwardedMessage();
                 if (msg != null) {
                	 form.setForwardedMsg(createHelperMsgFromAmpMessage(msg, id));
                 } else {
                	 form.setForwardedMsg(null);
                 }
             }
                             
			 
			 form.setReceivers(getMessageRecipients(message.getId()));
			 form.setSelectedAct(null);
			 //getting related activity if exists
			 if(message.getRelatedActivityId()!=null){
				 form.setSelectedAct(getRelatedActivity(message.getRelatedActivityId()));
			 }
			 
			 //is alert or not
			 if(message.getClassName().equals("a")){
				 form.setSetAsAlert(1);
			 }else {
				 form.setSetAsAlert(0);
			 }	
		 }
	 }
			 
	 
	 private String getRelatedActivity(Long actId){
		 String retValue;
		 String actName=null;
		 try {
			//it can't be null
			actName=ActivityUtil.getActivityName(actId);
		} catch (DgException e) {			
			e.printStackTrace();
		}
		retValue=actName+"("+actId+")";
		return retValue;
	 }
	 
	 /**
	 * Constructs XML from Messages      
	 */     
    private String messages2XML(List<AmpMessageState> states,AmpMessageForm form) throws AimException {
               
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        result += "<" + ROOT_TAG + ">";
        result+="<" + MESSAGES_TAG +">";
        if (states != null && states.size() > 0) {
            for (AmpMessageState state : states) {
                result += "<" + "message name=\"" + state.getMessage().getName() + "\" ";
                result += " id=\"" + state.getId() + "\"";
                result += " msgId=\"" + state.getMessage().getId() + "\"";
                if(state.getMessage().getSenderType()!=null && state.getMessage().getSenderType().equalsIgnoreCase(MessageConstants.SENDER_TYPE_USER)){
                	result += " from=\"" +org.digijava.module.aim.util.DbUtil.filter(state.getMessage().getSenderName()) + "\"";
                }else{
                	result += " from=\"" +MessageConstants.SENDER_TYPE_SYSTEM + "\"";
                }                
                result += " to=\"" + org.digijava.module.aim.util.DbUtil.filter(state.getMessage().getReceivers()) + "\"";
                result += " received=\"" + DateConversion.ConvertDateToString(state.getMessage().getCreationDate()) + "\"";
                result += " priority=\"" + state.getMessage().getPriorityLevel() + "\"";
                String desc=org.digijava.module.aim.util.DbUtil.filter(state.getMessage().getDescription());
                result += " msgDetails=\"" +desc + "\"";
                result += " read=\"" + state.getRead() + "\"";
                result += " isDraft=\"" + state.getMessage().getDraft() + "\"";
                String objUrl=org.digijava.module.aim.util.DbUtil.filter(state.getMessage().getObjectURL());
                result += " objURL=\"" + objUrl + "\"";
                result += ">";
                AmpMessage forwarded = state.getMessage().getForwardedMessage();
                if (forwarded != null) {
                    result += messages2XML(forwarded,state.getId());
                    
                }
                result += "</message>";
            }
        }
        result+="</" + MESSAGES_TAG +">";
        //pagination
		boolean messagesExist=(states==null||states.size()==0)?false:true;
		result+="<" + PAGINATION_TAG +">";
		result+="<"+"pagination messagesExist=\""+messagesExist+"\"";
		result+=" page=\""+form.getPage()+"\"";
		result+=" allPages=\""+form.getAllPages().length+"\"";
		result+=" lastPage=\""+form.getLastPage()+"\"";
		result+=" deleteWasCalled=\""+form.isDeleteActionWasCalled()+"\"";
		result+="/>";
		result+="</" + PAGINATION_TAG +">";
        result += "</" + ROOT_TAG + ">";
        return result;
    }

    /**
     * for forward thread generation
     * @param forwardedMessage
     * @param parentStateId state id of the newest message
     * @return forwarded messages 
     * @throws org.digijava.module.aim.exception.AimException
     */
    private String messages2XML(AmpMessage forwardedMessage, Long parentStateId) throws AimException {

        String result = "";
        result += "<" + "forwarded name=\"" + forwardedMessage.getName() + "\" ";
        result += " msgId=\"" + forwardedMessage.getId() + "\"";
        if(forwardedMessage.getSenderType()!=null && forwardedMessage.getSenderType().equalsIgnoreCase(MessageConstants.SENDER_TYPE_USER)){
        result += " from=\"" +org.digijava.module.aim.util.DbUtil.filter(forwardedMessage.getSenderName())+ "\"";
        } else {
            result += " from=\"" + MessageConstants.SENDER_TYPE_SYSTEM + "\"";
        }
        result += " received=\"" + DateConversion.ConvertDateToString(forwardedMessage.getCreationDate()) + "\"";
        result += " to=\"" + org.digijava.module.aim.util.DbUtil.filter(forwardedMessage.getReceivers()) + "\"";
        result += " priority=\"" + forwardedMessage.getPriorityLevel() + "\"";
        result += " objURL=\"" + org.digijava.module.aim.util.DbUtil.filter(forwardedMessage.getObjectURL()) + "\"";
        String desc=org.digijava.module.aim.util.DbUtil.filter(forwardedMessage.getDescription());
        result += " msgDetails=\"" + desc + "\"";
        result+=" read=\""+true+"\"";
        result += " parentStateId=\"" + parentStateId + "\""; 
        result += "/>";
        if (forwardedMessage.getForwardedMessage() != null) {
            AmpMessage forwarded = forwardedMessage.getForwardedMessage();
            result += messages2XML(forwarded,parentStateId);
        }
        return result;
    }
	 
	 /**
	  *create helper Message class from AmpMessage entity	  
	  */
	 private MessageHelper createHelperMsgFromAmpMessage(AmpMessage msg,Long stateId) throws Exception{
		 MessageHelper msgHelper=new MessageHelper(msg.getId(),msg.getName(),msg.getDescription());
     	 msgHelper.setFrom(AmpMessageUtil.getMessageState(stateId).getSender());
     	 msgHelper.setObjectURL(msg.getObjectURL());
     	 if(msgHelper.getReceivers()==null){
     		msgHelper.setReceivers(new ArrayList<String>());
     		List<LabelValueBean> receivers=getMessageRecipients(msg.getId());
     		for (LabelValueBean lvb : receivers) {
 				msgHelper.getReceivers().add(lvb.getLabel());
 			}
     		msgHelper.setCreationDate(DateConversion.ConvertDateToString(msg.getCreationDate()));
     	 }
     	 return msgHelper;
	 }
	 
	 
	 /**
	  * used to get message recipients, which will be shown on edit Message Page 
	  */
	 private static List<LabelValueBean> getMessageRecipients(Long messageId) throws Exception{
	 	List<AmpMessageState> msgStates=AmpMessageUtil.loadMessageStates(messageId);
		List<LabelValueBean> members=null;
		if(msgStates!=null && msgStates.size()>0){
			members=new ArrayList<LabelValueBean>();
			for (AmpMessageState state :msgStates) {
				if(state.getMemberId()!=null){
					AmpTeamMember teamMember=TeamMemberUtil.getAmpTeamMember(state.getMemberId());
					LabelValueBean tm=new LabelValueBean(teamMember.getUser().getName(),"m:"+state.getMemberId());				
					members.add(tm);
				}					
			}
		}
		return members;
	 }         
}
