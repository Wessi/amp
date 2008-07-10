package org.digijava.module.message.helper;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.forum.action.AlertNewPm;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.Approval;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.util.AmpMessageUtil;

public class AmpMessageWorker {

    public static void processEvent(Event e) throws Exception{
    	String triggerClassName=e.getTrigger().getName();
    	List<TemplateAlert> tempAlerts=AmpMessageUtil.getTemplateAlerts(triggerClassName);
    	if(tempAlerts!=null && !tempAlerts.isEmpty()){
    		for (TemplateAlert template : tempAlerts) {
    			//AmpAlert newAlert=createAlertFromTemplate(template);
                AmpMessage newMsg=null;

    			AmpAlert newAlert=new AmpAlert();
                Approval newApproval=new Approval();

    			if(e.getTrigger().equals(ActivitySaveTrigger.class)){//<------ Someone created new Activity
    				newMsg=processActivitySaveEvent(e,newAlert,template);
    			}else if(e.getTrigger().equals(UserRegistrationTrigger.class)){//<----- Registered New User
    				newMsg=proccessUserRegistrationEvent(e,newAlert,template);
    			}else if(e.getTrigger().equals(ActivityDisbursementDateTrigger.class)){
    				newMsg=processActivityDisbursementDateComingEvent(e,newAlert,template);
                }else if(e.getTrigger().equals(CalendarEventTrigger.class)){
    				newMsg=proccessCalendarEvent(e,newAlert,template);
    			}else if(e.getTrigger().equals(ApprovedActivityTrigger.class)){
                    newMsg=processApprovedActivityEvent(e,newApproval,template);
                }else if(e.getTrigger().equals(NotApprovedActivityTrigger.class)){
                    newMsg=processNotApprovedActivityEvent(e,newApproval,template);
                }

    			AmpMessageUtil.saveOrUpdateMessage(newMsg);
    			/**
    			 * getting states according to tempalteId
    			 * New Requirement for ApprovedActiviti and Activity waiting approval.only teamLeader(in case approval is needed) and
    			 *  activity creator/updater should get an alert regardless of receivers list in template  
    			 */    			    		
				if(e.getTrigger().equals(ApprovedActivityTrigger.class)||e.getTrigger().equals(NotApprovedActivityTrigger.class)){
					defineReceiversForApprovedAndNotApprovedActivities(e.getTrigger(),newMsg);				
                } else if (e.getTrigger().equals(CalendarEventTrigger.class)) {
                	defineReceiversForCalendarEvents(e,template,newMsg);
    			}else if(e.getTrigger().equals(ActivitySaveTrigger.class)){
    				defineActivityCreationReceievrs(template,newMsg);
    			}else{ //<-- currently for else is left user registration or activity disbursement date triggers
    				List<AmpMessageState> statesRelatedToTemplate=null;    			
        			statesRelatedToTemplate=AmpMessageUtil.loadMessageStates(template.getId());    			
        			if(statesRelatedToTemplate!=null && statesRelatedToTemplate.size()>0){
            			for (AmpMessageState state : statesRelatedToTemplate) {           		
            				createMsgState(state,newMsg);            		            				
            			}    				
        			}
    			}
			}
    	}
    }

    /**
     *	Calendar Event Event processing
     */
    private static AmpAlert proccessCalendarEvent(Event e, AmpAlert alert,TemplateAlert template){
        String partialURL=null;
        if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
            partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
        }

        HashMap<String, String> myHashMap=new HashMap<String, String>();
        myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(UserRegistrationTrigger.PARAM_NAME));
        if(partialURL!=null){
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\""+partialURL+e.getParameters().get(CalendarEventTrigger.PARAM_URL)+"\">View Event</a>");
            alert.setObjectURL(partialURL+e.getParameters().get(CalendarEventTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);
        AmpAlert newAlert=createAlertFromTemplate(template, myHashMap,alert);

        String receivers=new String();

        Long calId = new Long(e.getParameters().get(CalendarEventTrigger.PARAM_ID).toString());
        AmpCalendar ampCal = AmpDbUtil.getAmpCalendar(calId);
        Set<AmpCalendarAttendee> att = ampCal.getAttendees();
        if (att != null && !att.isEmpty()) {
            for (AmpCalendarAttendee ampAtt : att) {
                User user=ampAtt.getUser();
                if (user != null && newAlert.getReceivers().indexOf(user.getEmail())>-1){
                    receivers+=", "+user.getFirstNames()+" "+user.getLastName()+"<"+user.getEmail()+">";
                }
            }
        }
        newAlert.setReceivers(receivers.substring(", ".length()));

        return newAlert;
    }
    /**
     *	Not Approved Activity Event processing
     */
    private static Approval processNotApprovedActivityEvent(Event e,Approval approval,TemplateAlert template){
        String partialURL=null;
        if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
            partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
        }
        HashMap<String, String> myHashMap=new HashMap<String, String>();
        myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(NotApprovedActivityTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ((AmpTeamMember)e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY)).getUser().getName());
        //url
        if(partialURL!=null){
            myHashMap.put(MessageConstants.OBJECT_URL,"<a href=\""+partialURL+e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL)+"\">activity URL</a>");
            approval.setObjectURL(partialURL+e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL));
        }
        approval.setSenderId(((AmpTeamMember)e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY)).getAmpTeamMemId());
        approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createApprovalFromTemplate(template, myHashMap,approval,true);
    }

    /**
     *	Approved Activity Event processing
     */
    private static Approval processApprovedActivityEvent(Event e,Approval approval,TemplateAlert template){
        String partialURL=null;
        if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
            partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
        }
        HashMap<String, String> myHashMap=new HashMap<String, String>();
        myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(ApprovedActivityTrigger.PARAM_NAME));      
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ((AmpTeamMember)e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY)).getUser().getName());
        //url
        if(partialURL!=null){
            myHashMap.put(MessageConstants.OBJECT_URL,"<a href=\""+partialURL+e.getParameters().get(ApprovedActivityTrigger.PARAM_URL)+"\">activity URL</a>");
            approval.setObjectURL(partialURL+e.getParameters().get(ApprovedActivityTrigger.PARAM_URL));
        }
        approval.setSenderId(((AmpTeamMember)e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY)).getAmpTeamMemId());
        approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createApprovalFromTemplate(template, myHashMap,approval,false);
    }    
   
    /**
	 *	Save Activity Event processing
	 */
    private static AmpAlert processActivitySaveEvent(Event e,AmpAlert alert,TemplateAlert template){
    	String partialURL=null;
    	if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
			partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
		}
    	HashMap<String, String> myHashMap=new HashMap<String, String>();
    	myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(ActivitySaveTrigger.PARAM_NAME));
    	myHashMap.put(MessageConstants.OBJECT_AUTHOR, ((AmpTeamMember)e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY)).getUser().getName());
    	//url
    	if(partialURL!=null){
    		myHashMap.put(MessageConstants.OBJECT_URL,"<a href=\""+partialURL+e.getParameters().get(ActivitySaveTrigger.PARAM_URL)+"\">activity URL</a>");
			alert.setObjectURL(partialURL+e.getParameters().get(ActivitySaveTrigger.PARAM_URL));
		}
    	alert.setSenderId(((AmpTeamMember)e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY)).getAmpTeamMemId());
    	alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
    	return createAlertFromTemplate(template, myHashMap,alert);

    }

	/**
	 *	User Registration Event processing
	 */
    private static AmpAlert proccessUserRegistrationEvent(Event e, AmpAlert alert,TemplateAlert template){
    	//url
    	String partialURL=null;
    	if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
			partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
		}

    	HashMap<String, String> myHashMap=new HashMap<String, String>();
    	myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(UserRegistrationTrigger.PARAM_NAME));
    	if(partialURL!=null){
    		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\""+partialURL+e.getParameters().get(UserRegistrationTrigger.PARAM_URL)+"\">User Profile URL</a>");
    		alert.setObjectURL(partialURL+e.getParameters().get(UserRegistrationTrigger.PARAM_URL));
    	}
    	alert.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);

    	return createAlertFromTemplate(template, myHashMap,alert);
    }

	/**
	 * Activity's disbursement date Event processing
	 */
    private static AmpAlert processActivityDisbursementDateComingEvent(Event e, AmpAlert alert,TemplateAlert template){
    	String partialURL=null;
    	if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)!=null){
			partialURL=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN)+"/";
		}
    	HashMap<String, String> myHashMap=new HashMap<String, String>();
    	myHashMap.put(MessageConstants.OBJECT_NAME,(String)e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_NAME));
    	//url
    	if(partialURL!=null){
    		myHashMap.put(MessageConstants.OBJECT_URL,"<a href=\""+partialURL+e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL)+"\">activity URL</a>");
			alert.setObjectURL(partialURL+e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL));
		}
    	alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
    	return createAlertFromTemplate(template, myHashMap,alert);
    }

    private static Approval createApprovalFromTemplate(TemplateAlert template,HashMap<String, String> myMap,Approval newApproval,boolean needsApproval){
        newApproval.setName(DgUtil.fillPattern(template.getName(), myMap));
        newApproval.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
        newApproval.setDraft(false);
        newApproval.setCreationDate(new Date());
        //receivers
        String receivers;
        AmpTeamMember tm= TeamMemberUtil.getAmpTeamMember(newApproval.getSenderId());
        receivers=tm.getUser().getFirstNames()+" "+tm.getUser().getLastName()+"<"+tm.getUser().getEmail()+">;"+tm.getAmpTeam().getName()+";";
        if(needsApproval){
        	//team lead can't be null,because if team has no leader,then no trigger will be invoked
        	AmpTeamMember teamLead=tm.getAmpTeam().getTeamLead();
        	receivers+=", "+teamLead.getUser().getFirstNames()+" "+teamLead.getUser().getLastName()+"<"+teamLead.getUser().getEmail()+">;"+teamLead.getAmpTeam().getName()+";";
        }
        newApproval.setReceivers(receivers);
        return newApproval;
	}
    /**
     * created different kinds of alerts(not approvals or calendar events )
     */
    private static AmpAlert createAlertFromTemplate(TemplateAlert template,HashMap<String, String> myMap,AmpAlert newAlert){
		newAlert.setName(DgUtil.fillPattern(template.getName(), myMap));
		newAlert.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
		newAlert.setReceivers(template.getReceivers());
		newAlert.setDraft(false);
		Calendar cal=Calendar.getInstance();
		newAlert.setCreationDate(cal.getTime());
		return newAlert;
	}

    /**
     * this method defines approval receivers and creates corresponding AmpMessageStates.
     */
    private static void defineReceiversForApprovedAndNotApprovedActivities(Class triggerClass,AmpMessage approval) throws Exception{
    	AmpMessageState state=new AmpMessageState();
		state.setMemberId(approval.getSenderId());    				
		createMsgState(state,approval);
		if(triggerClass.equals(NotApprovedActivityTrigger.class)){
			AmpTeamMember tm= TeamMemberUtil.getAmpTeamMember(approval.getSenderId());
			if(tm.getAmpTeam().getTeamLead()!=null){
				Long teamLeaderId=tm.getAmpTeam().getTeamLead().getAmpTeamMemId();
				state=new AmpMessageState();      
				state.setMemberId(teamLeaderId);            				
				createMsgState(state,approval);            				
			}
		}    				
    }
    
    /**
     * this method defines calendar event receivers and creates corresponding AmpMessageStates.
     */
    private static void defineReceiversForCalendarEvents(Event e,TemplateAlert template, AmpMessage calEvent) throws Exception{
    	List<AmpMessageState> statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
        if (statesRelatedToTemplate != null) {
            for (AmpMessageState state : statesRelatedToTemplate) {
                createMsgState(state, calEvent);
            }
        }
        Long calId = new Long(e.getParameters().get(CalendarEventTrigger.PARAM_ID).toString());
        AmpCalendar ampCal = AmpDbUtil.getAmpCalendar(calId);
        Set<AmpCalendarAttendee> att = ampCal.getAttendees();
        if (att != null && !att.isEmpty()) {
            for (AmpCalendarAttendee ampAtt : att) {
                User user=ampAtt.getUser();
                if (user != null){
                    Collection<AmpTeamMember> members=TeamMemberUtil.getTeamMembers(user.getEmail());
                    if(members!=null){
                        for (AmpTeamMember mem : members) {
                            AmpMessageState state = new AmpMessageState();
                            state.setMemberId(mem.getAmpTeamMemId());
                            state.setSenderId(calEvent.getSenderId());
                            createMsgState(state, calEvent);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * this method defines alert receivers and creates corresponding AmpMessageStates.
     */
    private static void defineActivityCreationReceievrs(TemplateAlert template,AmpMessage alert) throws Exception{
    	List<AmpMessageState> statesRelatedToTemplate=null;    			
		statesRelatedToTemplate=AmpMessageUtil.loadMessageStates(template.getId());    			
		if(statesRelatedToTemplate!=null && statesRelatedToTemplate.size()>0){
			for (AmpMessageState state : statesRelatedToTemplate) {
				//get the member who created an activity. it's the current member
				AmpTeamMember activityCreator=TeamMemberUtil.getAmpTeamMember(alert.getSenderId());
				//get receiver Team Member.				
				AmpTeamMember teamMember=TeamMemberUtil.getAmpTeamMember(state.getMemberId());
				/**
				 * Alert about new activity creation should get only members of the same team in which activity was created,if this team is listed as receivers in template.
				 */
				if(teamMember.getAmpTeam().getAmpTeamId().equals(activityCreator.getAmpTeam().getAmpTeamId())){
					createMsgState(state,alert);
				}            				
			}    				
		}
    }
    
	private static void createMsgState(AmpMessageState state,AmpMessage newMsg) throws Exception{
		AmpMessageState newState=new AmpMessageState();
		newState.setMessage(newMsg);
		newState.setMemberId(state.getMemberId());
		newState.setRead(false);
		//will this message be visible in user's mailbox
		if(AmpMessageUtil.isInboxFull(Approval.class, state.getMemberId())){
			newState.setMessageHidden(true);
		}else{
			newState.setMessageHidden(false);
		}
		try {
			AmpMessageUtil.saveOrUpdateMessageState(newState);
		} catch (AimException e) {
			e.printStackTrace();
		}
	}	

}
