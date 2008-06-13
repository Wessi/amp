package org.digijava.module.message.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Team;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.util.AmpMessageUtil;

public class TemplateAlertActions extends DispatchAction {
	
	public ActionForward viewTemplates(ActionMapping mapping,ActionForm form, HttpServletRequest request,	HttpServletResponse response) throws Exception {
		 AmpMessageForm messageForm=(AmpMessageForm)form;
		 messageForm.setTemplates((List<TemplateAlert>)AmpMessageUtil.getAllMessages(TemplateAlert.class));
		 return mapping.findForward("templatesManager"); 
	 }
	
	 public ActionForward addOrEditTemplate(ActionMapping mapping,ActionForm form, HttpServletRequest request,	HttpServletResponse response) throws Exception {
		 AmpMessageForm messagesForm=(AmpMessageForm)form;
		 if(request.getParameter("event")!=null && request.getParameter("event").equalsIgnoreCase("add")){
			 setDefaultValues(messagesForm);
		 }
		 
		 if(messagesForm.getTeamsMap()!=null){
			 messagesForm.setTeamsMap(null); 
		 }
	     messagesForm.setTeamsMap(loadRecepients());
	     
	     List<LabelValueBean> availableTriggers= new ArrayList<LabelValueBean>();	   
	     for (int i=0;i<MessageConstants.availableTriggers.length;i++) {
			LabelValueBean lvb=new LabelValueBean(MessageConstants.triggerName[i],MessageConstants.availableTriggers[i].getName());
			availableTriggers.add(lvb);
		}
	     messagesForm.setAvailableTriggersList(availableTriggers);
	     
	     if(request.getParameter("event")!=null && request.getParameter("event").equalsIgnoreCase("edit")){
	    	 TemplateAlert tempAlert=(TemplateAlert)AmpMessageUtil.getMessage(messagesForm.getTemplateId());
	    	 messagesForm.setTemplateId(tempAlert.getId());
	    	 messagesForm.setMessageName(tempAlert.getName());
	    	 messagesForm.setDescription(tempAlert.getDescription());
	    	 messagesForm.setSelectedTrigger(tempAlert.getRelatedTriggerName());
	    	 //receivers	    	 
	    	 messagesForm.setReceivers(getMessageRecipients(tempAlert.getId()));
	     }
		 
	     return mapping.findForward("addOrEditPage"); 
	 }
	 
	 public ActionForward saveTemplate(ActionMapping mapping,ActionForm form, HttpServletRequest request,	HttpServletResponse response) throws Exception {
		
	    	AmpMessageForm msgForm=(AmpMessageForm)form;    	
	    	TemplateAlert newTemplate=null; 
	    	String[] messageReceivers=msgForm.getReceiversIds();
	    	
	    	if(msgForm.getTemplateId()==null) {
	    		newTemplate=new TemplateAlert();   		    		
	    	}else {
	    		newTemplate=new TemplateAlert();
	    		//remove all States that were associated to this message
				List<AmpMessageState> statesAssociatedWithMsg=AmpMessageUtil.loadMessageStates(msgForm.getTemplateId());
				for (AmpMessageState state : statesAssociatedWithMsg) {
					AmpMessageUtil.removeMessageState(state.getId());
				}
				//remove message
				AmpMessageUtil.removeMessage(msgForm.getTemplateId());
	    	}    	
	    	newTemplate.setName(msgForm.getMessageName());
	    	newTemplate.setDescription(msgForm.getDescription());
	    	Calendar cal=Calendar.getInstance();
	    	newTemplate.setCreationDate(cal.getTime());   	
	    	newTemplate.setRelatedTriggerName(msgForm.getSelectedTrigger());
	    	        	 	
	    	//saving template
	    	AmpMessageUtil.saveOrUpdateMessage(newTemplate);    	
	    	
	    	
	    	List<AmpMessageState> statesList=AmpMessageUtil.loadMessageStates(msgForm.getTemplateId());  
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
				for (String receiver : messageReceivers) {				
					if(receiver.startsWith("t")){//<--this means that receiver is team
						List<TeamMember> teamMembers=(List<TeamMember>)TeamMemberUtil.getAllTeamMembers(new Long(receiver.substring(2)));
						if(teamMembers!=null && teamMembers.size()>0){
							for (TeamMember tm : teamMembers) {
								if(! statesMemberIds.contains(tm.getMemberId())){
									createMessageState(newTemplate,tm.getMemberId());									
								}
							}
						}						
					}else {//<--receiver is team member
						if(! statesMemberIds.contains(new Long(receiver.substring(2)))){
							Long memId=new Long(receiver.substring(2));
							createMessageState(newTemplate,memId);							
						}
					}				
				}		
			}
			
	    	//cleaning form values
	    	setDefaultValues(msgForm);

			return viewTemplates(mapping,msgForm,request,response);	
		
	 }
	 
	 /**
	    * user clicked cancel on add Template page   
	    */ 
	    public ActionForward cancel(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {    	
	    	AmpMessageForm msgForm=(AmpMessageForm)form;
	    	setDefaultValues(msgForm);
	    	return viewTemplates(mapping,msgForm,request,response);
	    }
	 
	 public ActionForward deleteTemplate(ActionMapping mapping,ActionForm form, HttpServletRequest request,	HttpServletResponse response) throws Exception {
		 AmpMessageForm msgForm=(AmpMessageForm)form;
		 AmpMessage templateAlert=null;
		 List<AmpMessageState> states=new ArrayList<AmpMessageState>();
		 if(msgForm.getTemplateId()!=null){
			 templateAlert=AmpMessageUtil.getMessage(msgForm.getTemplateId());
			 states=AmpMessageUtil.loadMessageStates(templateAlert.getId());
			 for (AmpMessageState state : states) {
				 AmpMessageUtil.removeMessageState(state.getId());
			}
			 AmpMessageUtil.removeMessage(templateAlert.getId());
		 }
		 return viewTemplates(mapping,msgForm,request,response);
	 }
	 
	 private Map<String, Team> loadRecepients(){
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
	    return teamMap;
	 }
	 
	 /**
	  * used to get message recipients, which will be shown on edit Message Page 
	  */
	 private static List<LabelValueBean> getMessageRecipients(Long tempId) {
		 	List<AmpMessageState> msgStates=null;
			try {
				msgStates = AmpMessageUtil.loadMessageStates(tempId);
			} catch (AimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	 
	 private void createMessageState(TemplateAlert tempAlert,Long memberId){
	    	AmpMessageState newMessageState=new AmpMessageState();
			newMessageState.setMessage(tempAlert);			
			newMessageState.setMemberId(memberId);
			//receivers list as string
			String receivers = tempAlert.getReceivers();
            if (receivers == null) {
                receivers = "";
            } else {
                if (receivers.length() > 0) {
                    receivers += ", ";
                }
            }
            User user=TeamMemberUtil.getAmpTeamMember(memberId).getUser();
            receivers+=user.getFirstNames()+" "+user.getLastName()+"<"+user.getEmail()+">";
            tempAlert.setReceivers(receivers);
            
			//saving current state in db
			try {
				AmpMessageUtil.saveOrUpdateMessageState(newMessageState);
			} catch (AimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	 
	 private void setDefaultValues(AmpMessageForm form){
		 form.setEditingMessage(false);
		 form.setTemplateId(null);
		 form.setMessageName(null);
		 form.setDescription(null);		 
		 form.setTeamsMap(null);
		 form.setReceiversIds(null);	 
		 form.setClassName(null);
		 form.setMsgStateId(null);
		 form.setReceivers(null);
		 form.setSelectedTrigger(null);
	 }
}
