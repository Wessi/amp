package org.digijava.module.message.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpEmail;
import org.digijava.module.message.dbentity.AmpEmailReceiver;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.Approval;
import org.digijava.module.message.dbentity.CalendarEvent;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.triggers.AbstractCalendarEventTrigger;
import org.digijava.module.message.triggers.ActivityActualStartDateTrigger;
import org.digijava.module.message.triggers.ActivityCurrentCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityDisbursementDateTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForContractingTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForDisbursementsTrigger;
import org.digijava.module.message.triggers.ActivityProposedApprovalDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedStartDateTrigger;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.ApprovedCalendarEventTrigger;
import org.digijava.module.message.triggers.ApprovedResourceShareTrigger;
import org.digijava.module.message.triggers.CalendarEventSaveTrigger;
import org.digijava.module.message.triggers.CalendarEventTrigger;
import org.digijava.module.message.triggers.AwaitingApprovalCalendarTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;
import org.digijava.module.message.triggers.NotApprovedCalendarEventTrigger;
import org.digijava.module.message.triggers.PendingResourceShareTrigger;
import org.digijava.module.message.triggers.RejectResourceSharetrigger;
import org.digijava.module.message.triggers.RemoveCalendarEventTrigger;
import org.digijava.module.message.triggers.UserRegistrationTrigger;
import org.digijava.module.message.util.AmpMessageUtil;

public class AmpMessageWorker {
	
	private static Logger logger = Logger.getLogger(AmpMessageWorker.class);

    public static void processEvent(Event e) throws Exception {
        String triggerClassName = e.getTrigger().getName();
        List<TemplateAlert> tempAlerts = AmpMessageUtil.getTemplateAlerts(triggerClassName);
        if (tempAlerts != null && !tempAlerts.isEmpty()) {
            for (TemplateAlert template : tempAlerts) {
                //AmpAlert newAlert=createAlertFromTemplate(template);
                AmpMessage newMsg = null;

                AmpAlert newAlert = new AmpAlert();
                Approval newApproval = new Approval();
                CalendarEvent newEvent = new CalendarEvent();

                if (e.getTrigger().equals(ActivitySaveTrigger.class)) { //<------ Someone created new Activity
                    newMsg = processActivitySaveEvent(e, newAlert, template);
                } else if (e.getTrigger().equals(UserRegistrationTrigger.class)) { //<----- Registered New User
                    newMsg = proccessUserRegistrationEvent(e, newAlert, template);
                } else if (e.getTrigger().equals(ActivityDisbursementDateTrigger.class)) {
                    newMsg = processActivityDisbursementDateComingEvent(e, newAlert, template);
                } else if (e.getTrigger().equals(CalendarEventTrigger.class)) {
                    newMsg = proccessCalendarEvent(e, newEvent, template,false);
                }else if(e.getTrigger().equals(CalendarEventSaveTrigger.class)){
                	 newMsg = proccessCalendarEvent(e, newEvent, template,true);
                }else if(e.getTrigger().equals(RemoveCalendarEventTrigger.class)){
                	newMsg = proccessCalendarEventRemoval(e, newEvent, template);
                }else if (e.getTrigger().equals(ApprovedActivityTrigger.class)) {
                    newMsg = processApprovedActivityEvent(e, newApproval, template);
                }else if (e.getTrigger().equals(NotApprovedCalendarEventTrigger.class) || 
                		e.getTrigger().equals(ApprovedCalendarEventTrigger.class)|| 
                		e.getTrigger().equals(AwaitingApprovalCalendarTrigger.class)) {
                    newMsg = processApprovedCalendarEvent(e, newApproval, template);
                } else if (e.getTrigger().equals(NotApprovedActivityTrigger.class)) {
                    newMsg = processNotApprovedActivityEvent(e, newApproval, template);
                }else if (e.getTrigger().equals(ActivityActualStartDateTrigger.class)) {
                    newMsg = processActivityActualStartDateEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityCurrentCompletionDateTrigger.class)) {
                    newMsg = processActivityCurrentCompletionDateEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityFinalDateForContractingTrigger.class)) {
                    newMsg = processActivityFinalDateForContractingEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityFinalDateForDisbursementsTrigger.class)) {
                    newMsg = processActivityFinalDateForDisbursementsEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityProposedApprovalDateTrigger.class)) {
                    newMsg = processActivityProposedApprovalDateEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityProposedCompletionDateTrigger.class)) {
                    newMsg = processActivityProposedCompletionDateEvent(e, newAlert, template);
                }else if (e.getTrigger().equals(ActivityProposedStartDateTrigger.class)) {
                    newMsg = processActivityProposedStartDateEvent(e, newAlert, template);
                }else if(e.getTrigger().equals(PendingResourceShareTrigger.class)){
                	newMsg =processResourceShareEvent(e, newApproval, template, true);
                }else if(e.getTrigger().equals(ApprovedResourceShareTrigger.class) || e.getTrigger().equals(RejectResourceSharetrigger.class)){
                	newMsg =processResourceShareEvent(e, newApproval, template, false);
                }

                AmpMessageUtil.saveOrUpdateMessage(newMsg);
                /**
                 * getting states according to tempalteId
                 * New Requirement for ApprovedActiviti and Activity waiting approval.only teamLeader(in case approval is needed) and
                 *  activity creator/updater should get an alert regardless of receivers list in template
                 */
                if(e.getTrigger().equals(ApprovedActivityTrigger.class) || e.getTrigger().equals(NotApprovedActivityTrigger.class)) {
                    defineReceiversForApprovedAndNotApprovedActivities(e.getTrigger(), newMsg,(Long)e.getParameters().get(NotApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM));
                }else if(e.getTrigger().equals(NotApprovedCalendarEventTrigger.class) || 
                		e.getTrigger().equals(ApprovedCalendarEventTrigger.class)|| 
                		e.getTrigger().equals(AwaitingApprovalCalendarTrigger.class)) {
                	AmpTeamMember creator  = (AmpTeamMember)e.getParameters().get(AbstractCalendarEventTrigger.PARAM_AUTHOR);
                    defineReceiversForApprovedCalendarEvent(creator, newMsg);
                }else if(e.getTrigger().equals(CalendarEventTrigger.class)) {
                    defineReceiversForCalendarEvents(e, template, newMsg,false,false);
                }else if(e.getTrigger().equals(CalendarEventSaveTrigger.class)){
                	defineReceiversForCalendarEvents(e, template, newMsg,true,false);
                }else if(e.getTrigger().equals(RemoveCalendarEventTrigger.class)){
                	defineReceiversForCalendarEvents(e, template, newMsg,false,true);
                }
                else if(e.getTrigger().equals(ActivitySaveTrigger.class)) {
                    defineActivityCreationReceievrs(template, newMsg);
                }else if(e.getTrigger().equals(ActivityActualStartDateTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityActualStartDateTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityCurrentCompletionDateTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityFinalDateForContractingTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityFinalDateForDisbursementsTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityProposedApprovalDateTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityProposedCompletionDateTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(ActivityProposedStartDateTrigger.class)) {
                    defineReceievrsByActivityTeam(template, newMsg,new Long(e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_TEAM_ID).toString()));
                }else if(e.getTrigger().equals(PendingResourceShareTrigger.class)){
                	defineReceiversForResourceShare(template,newMsg,true);
                }else if(e.getTrigger().equals(ApprovedResourceShareTrigger.class) || e.getTrigger().equals(RejectResourceSharetrigger.class)){
                	defineReceiversForResourceShare(template,newMsg,false);
                } else{ //<-- currently for else is left user registration or activity disbursement date triggers
                	List<String> emailReceivers=new ArrayList<String>();
                    List<AmpMessageState> statesRelatedToTemplate = null;
                    statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
                    HashMap<Long, AmpMessageState> msgStateMap = new HashMap<Long, AmpMessageState> ();
                    if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
                        for (AmpMessageState state : statesRelatedToTemplate) {
                            createMsgState(state, newMsg,false);
                            if (!msgStateMap.containsKey(state.getReceiver().getAmpTeamMemId())) {
                                msgStateMap.put(state.getReceiver().getAmpTeamMemId(), state);
                                emailReceivers.add(state.getReceiver().getUser().getEmail());
                            }
                        }
                        createEmailsAndReceivers(newMsg,emailReceivers,false);
                        //sendMailes(msgStateMap.values());
                    }
                }
            }
        }
    }
    
   	public static Approval processResourceShareEvent(Event e, Approval approval,TemplateAlert template,boolean needsApproval){
    	 HashMap<String, String> myHashMap = new HashMap<String, String> ();
    	 Long teamId=(Long)e.getParameters().get(PendingResourceShareTrigger.PARAM_CREATOR_TEAM);
    	 String userMail=(String)e.getParameters().get(PendingResourceShareTrigger.PARAM_SHARED_BY);
         myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(PendingResourceShareTrigger.PARAM_NAME));
         myHashMap.put(MessageConstants.OBJECT_AUTHOR, userMail); //holds email of creator
         myHashMap.put(MessageConstants.OBJECT_TEAM,  teamId.toString());
        
         AmpTeamMember tm=TeamMemberUtil.getAmpTeamMemberByEmailAndTeam(userMail, teamId);         
         approval.setSenderId(tm.getAmpTeamMemId());
         
         approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
         return createApprovalFromTemplate(template, myHashMap, approval, true,false);
    }

    /**
     *	Calendar Event processing
     */
    private static CalendarEvent proccessCalendarEvent(Event e, CalendarEvent event, TemplateAlert template,boolean saveActionWasCalled) {     
        //get event creator
        AmpTeamMember tm=(AmpTeamMember)e.getParameters().get(CalendarEventSaveTrigger.SENDER);
        
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(CalendarEventTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" +"/" + e.getParameters().get(CalendarEventTrigger.PARAM_URL) + "\">View Event</a>");
        event.setObjectURL("/"+e.getParameters().get(CalendarEventTrigger.PARAM_URL));
        if(saveActionWasCalled){
        	event.setSenderType(MessageConstants.SENDER_TYPE_USER);        	
        	event.setSenderId(tm.getAmpTeamMemId());
        	event.setSenderName(tm.getUser().getFirstNames()+" "+tm.getUser().getLastName()+"<"+tm.getUser().getEmail()+">;"+tm.getAmpTeam().getName());
        	event.setSenderEmail(tm.getUser().getEmail());
        	//put event's start/end dates in map.
        	myHashMap.put(MessageConstants.START_DATE, (String) e.getParameters().get(CalendarEventSaveTrigger.EVENT_START_DATE));
        	myHashMap.put(MessageConstants.END_DATE, (String) e.getParameters().get(CalendarEventSaveTrigger.EVENT_END_DATE));
        }else{
        	myHashMap.put(MessageConstants.START_DATE, (String) e.getParameters().get(CalendarEventTrigger.EVENT_START_DATE));
        	myHashMap.put(MessageConstants.END_DATE, (String) e.getParameters().get(CalendarEventTrigger.EVENT_END_DATE));
        	event.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);
        }
        
        CalendarEvent newEvent = createEventFromTemplate(template, myHashMap, event);

        String receivers = new String();

        Long calId = new Long(e.getParameters().get(CalendarEventTrigger.PARAM_ID).toString());        
        AmpCalendar ampCal = AmpDbUtil.getAmpCalendar(calId);
        Set<AmpCalendarAttendee> att=ampCal.getAttendees();
        
        if (att != null) {
            receivers = createReceiversFieldForEvent(receivers, att);            
        }
        //In case this event is created when new calendar event was added, message should go to it's creator too
        //so in the receivers list we should also add it's creator (AMP-3775)
        if(saveActionWasCalled){        	
            User user = tm.getUser();
            receivers += ", " + user.getFirstNames() + " " + user.getLastName() + "<" + user.getEmail() + ">;" + tm.getAmpTeam().getName() + ";";
        }        
        
        newEvent.setReceivers(receivers.substring(", ".length()));
        return newEvent;
    }
    
    private static CalendarEvent proccessCalendarEventRemoval(Event e, CalendarEvent event, TemplateAlert template) {
    	HashMap<String, String> myHashMap = new HashMap<String, String> ();
    	
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(RemoveCalendarEventTrigger.PARAM_NAME));
        //get event creator
        AmpTeamMember tm=(AmpTeamMember)e.getParameters().get(RemoveCalendarEventTrigger.SENDER);
        event.setSenderType(MessageConstants.SENDER_TYPE_USER);        	
    	event.setSenderId(tm.getAmpTeamMemId());
    	event.setSenderName(tm.getUser().getFirstNames()+" "+tm.getUser().getLastName()+"<"+tm.getUser().getEmail()+">;"+tm.getAmpTeam().getName());
    	event.setSenderEmail(tm.getUser().getEmail());
    	//put event's start/end dates in map.
    	myHashMap.put(MessageConstants.START_DATE, (String) e.getParameters().get(RemoveCalendarEventTrigger.EVENT_START_DATE));
    	myHashMap.put(MessageConstants.END_DATE, (String) e.getParameters().get(RemoveCalendarEventTrigger.EVENT_END_DATE));
    	
    	CalendarEvent newEvent = createEventFromTemplate(template, myHashMap, event);

        String receivers = new String();
        Set<AmpCalendarAttendee> att=(Set<AmpCalendarAttendee>) e.getParameters().get(RemoveCalendarEventTrigger.ATTENDEES);
        if(att!=null){
        	receivers = createReceiversFieldForEvent(receivers, att);
        }
    	return newEvent;
    }
    
    private static String createReceiversFieldForEvent(String receivers,Set<AmpCalendarAttendee> att) {
		for (AmpCalendarAttendee ampAtt : att) {
		    if (ampAtt.getMember() != null) {
		        AmpTeamMember member = ampAtt.getMember();
		        User user = member.getUser();
		        receivers += ", " + user.getFirstNames() + " " + user.getLastName() + "<" + user.getEmail() + ">;" + member.getAmpTeam().getName() + ";";                    
		    }
		    if(ampAtt.getGuest()!=null){ //guests e-mails should also be included in receivers list
		    	receivers+=", <"+ampAtt.getGuest()+">;";
		    }
		}
		return receivers;
	}

    /**
     *	Not Approved Activity Event processing
     */
    private static Approval processNotApprovedActivityEvent(Event e, Approval approval, TemplateAlert template) {
     
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(NotApprovedActivityTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY)).getUser().getName());
        myHashMap.put(MessageConstants.OBJECT_TEAM,  ((Long)e.getParameters().get(NotApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM)).toString());
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" +"/"+ e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL) + "\">activity URL</a>");
        approval.setObjectURL("/" + e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL));
        approval.setSenderId( ( (AmpTeamMember) e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY)).getAmpTeamMemId());
        approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createApprovalFromTemplate(template, myHashMap, approval, true,false);
    }

    /**
     *	Approved Activity Event processing
     */
    private static Approval processApprovedActivityEvent(Event e, Approval approval, TemplateAlert template) {
        
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ApprovedActivityTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY)).getUser().getName());
        myHashMap.put(MessageConstants.OBJECT_TEAM,  ((Long)e.getParameters().get(ApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM)).toString());
        //url
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(ApprovedActivityTrigger.PARAM_URL) + "\">activity URL</a>");
        approval.setObjectURL("/" + e.getParameters().get(ApprovedActivityTrigger.PARAM_URL));
        approval.setSenderId( ( (AmpTeamMember) e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY)).getAmpTeamMemId());
        approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createApprovalFromTemplate(template, myHashMap, approval, false,false);
    }
    
    private static Approval processApprovedCalendarEvent(Event e, Approval approval, TemplateAlert template) {

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(AbstractCalendarEventTrigger.PARAM_TITLE));
        //url
        if (e.getParameters().get(AbstractCalendarEventTrigger.PARAM_URL) != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(AbstractCalendarEventTrigger.PARAM_URL) + "\">activity URL</a>");
            approval.setObjectURL("/" + e.getParameters().get(AbstractCalendarEventTrigger.PARAM_URL));
        }
        AmpTeamMember creator =  (AmpTeamMember) e.getParameters().get(AbstractCalendarEventTrigger.PARAM_AUTHOR);
        approval.setSenderId( creator.getAmpTeamMemId());
        approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        approval.setCreationDate(new Date());
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, (String)e.getParameters().get(AbstractCalendarEventTrigger.PARAM_TEAM_MANAGER));
        User user = creator.getUser();
        String receivers = user.getFirstNames() + " " + user.getLastName() + "<" + user.getEmail() + ">;" + user.getName() + ";";

        approval.setReceivers(receivers);
        return createApprovalFromTemplate(template, myHashMap, approval, false,false);
    }    

    private static AmpAlert processActivitySaveEvent(Event e, AmpAlert alert, TemplateAlert template) {

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivitySaveTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY)).getUser().getName());
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(ActivitySaveTrigger.PARAM_URL) + "\">activity URL</a>");
        alert.setObjectURL("/" + e.getParameters().get(ActivitySaveTrigger.PARAM_URL));
        alert.setSenderId( ( (AmpTeamMember) e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY)).getAmpTeamMemId());
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityActualStartDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityActualStartDateTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityActualStartDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(ActivityActualStartDateTrigger.PARAM_URL) + "\">activity URL</a>");
        alert.setObjectURL("/" + e.getParameters().get(ActivityActualStartDateTrigger.PARAM_URL));
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityCurrentCompletionDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_URL) + "\">activity URL</a>");
        alert.setObjectURL("/" + e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_URL));
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityFinalDateForContractingEvent(Event e, AmpAlert alert, TemplateAlert template) {

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_NAME));
        //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_URL) + "\">activity URL</a>");
        alert.setObjectURL("/" + e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_URL));
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityFinalDateForDisbursementsEvent(Event e, AmpAlert alert, TemplateAlert template) {
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_NAME));
        //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_URL) + "\">activity URL</a>");
        alert.setObjectURL("/" + e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_URL));
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityProposedApprovalDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_NAME));
        //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_URL) + "\">activity URL</a>");
        alert.setObjectURL("/" + e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_URL));
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityProposedCompletionDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_NAME));
        //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_URL) + "\">activity URL</a>");
        alert.setObjectURL("/" + e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_URL));
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityProposedStartDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_NAME));
        //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_URL) + "\">activity URL</a>");
        alert.setObjectURL("/" + e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_URL));
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    /**
     *	User Registration Event processing
     */
    private static AmpAlert proccessUserRegistrationEvent(Event e, AmpAlert alert, TemplateAlert template) {
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(UserRegistrationTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(UserRegistrationTrigger.PARAM_URL) + "\">User Profile URL</a>");
        alert.setObjectURL("/" + e.getParameters().get(UserRegistrationTrigger.PARAM_URL));
        alert.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);
        return createAlertFromTemplate(template, myHashMap, alert);
    }

    /**
     * Activity's disbursement date Event processing
     */
    private static AmpAlert processActivityDisbursementDateComingEvent(Event e, AmpAlert alert, TemplateAlert template) {

       
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_NAME));
        //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/" + e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL) + "\">activity URL</a>");
        alert.setObjectURL("/" + e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL));
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);
    }

    private static Approval createApprovalFromTemplate(TemplateAlert template, HashMap<String, String> myMap, Approval newApproval, boolean needsApproval,boolean sourceIsResource) {
        newApproval.setName(DgUtil.fillPattern(template.getName(), myMap));
        newApproval.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
        newApproval.setDraft(false);
        newApproval.setCreationDate(new Date());
        //receivers
        String receivers="";
        
        if((needsApproval&& !sourceIsResource) || !needsApproval){
        	AmpTeamMember tm = TeamMemberUtil.getAmpTeamMember(newApproval.getSenderId());
            receivers += tm.getUser().getFirstNames() + " " + tm.getUser().getLastName() + "<" + tm.getUser().getEmail() + ">;" + tm.getAmpTeam().getName() + ";";
        }else{
        	String teamId=myMap.get(MessageConstants.OBJECT_TEAM);
            AmpTeamMember teamHead=TeamMemberUtil.getTeamHead(Long.parseLong(teamId));
            if(teamHead!=null){
            	receivers += ", " + teamHead.getUser().getFirstNames() + " " + teamHead.getUser().getLastName() + "<" + teamHead.getUser().getEmail() + ">;" + teamHead.getAmpTeam().getName() + ";";
            }
        }
               
        newApproval.setReceivers(receivers);
        return newApproval;
    }   
    

    /**
     * created different kinds of alerts(not approvals or calendar events )
     */
    private static AmpAlert createAlertFromTemplate(TemplateAlert template, HashMap<String, String> myMap, AmpAlert newAlert) {
        newAlert.setName(DgUtil.fillPattern(template.getName(), myMap));
        newAlert.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
        newAlert.setReceivers(template.getReceivers());
        newAlert.setDraft(false);
        Calendar cal = Calendar.getInstance();
        newAlert.setCreationDate(cal.getTime());
        return newAlert;
    } 

    private static CalendarEvent createEventFromTemplate(TemplateAlert template, HashMap<String, String> myMap, CalendarEvent newEvent) {
        newEvent.setName(DgUtil.fillPattern(template.getName(), myMap));
        newEvent.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
        newEvent.setReceivers(template.getReceivers());
        newEvent.setDraft(false);        
        Calendar cal = Calendar.getInstance();
        newEvent.setCreationDate(cal.getTime());
        return newEvent;
    }
    
    private static void defineReceiversForResourceShare(TemplateAlert template,AmpMessage approval, boolean needsApproval) throws Exception{
    	List<String> emailReceivers=new ArrayList<String>();
    	List<TeamMember> receiverTeamMembers=new ArrayList<TeamMember>();
    	List<AmpMessageState> statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());    	
    	  if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
              //create receivers list for resources
    		  TeamMember sharedBy=TeamMemberUtil.getTeamMember(approval.getSenderId());
    		  Long teamId=sharedBy.getTeamId();
              String receivers;
              if(needsApproval){            	  
            	  TeamMember teamLead=TeamMemberUtil.getTMTeamHead(teamId);
            	  receiverTeamMembers.add(teamLead);
              }else{
            	  receiverTeamMembers.add(sharedBy);
              }
              receivers=fillTOfieldForReceivers(receiverTeamMembers, statesRelatedToTemplate);
              approval.setReceivers(receivers);

              for (AmpMessageState state : statesRelatedToTemplate) {
              	AmpTeamMember teamMember=state.getReceiver();                  
              	/**
              	 * Approval should get TL or creator of resource depending whether it's approved/not approved message
              	 */              	
                  if (teamMember.getAmpTeam().getAmpTeamId().equals(teamId)) {
                	  boolean createNewMsgState=false;
                	  if(needsApproval){
                		  AmpTeamMemberRoles headRole = TeamMemberUtil.getAmpTeamHeadRole();
                		  AmpTeamMemberRoles ampRole = teamMember.getAmpMemberRole();
                		  if(headRole!=null && ampRole.getAmpTeamMemRoleId().equals(headRole.getAmpTeamMemRoleId())){ //TL
                			  createNewMsgState=true;
                		  }
                	  }else{
                		  if(teamMember.getAmpTeamMemId().equals(sharedBy.getMemberId())){ // member who requested to shared resource
                			  createNewMsgState=true;                			  
                		  }
                	  }
                	  if(createNewMsgState){
                		  createMsgState(state, approval,false);
                          emailReceivers.add(state.getReceiver().getUser().getEmail());
                          break;
                	  }
                      
                  }
              }
              createEmailsAndReceivers(approval,emailReceivers,false);
          }
		
	}
    

    /**
     * this method defines approval receivers and creates corresponding AmpMessageStates.
     */
    private static void defineReceiversForApprovedAndNotApprovedActivities(Class triggerClass, AmpMessage approval,Long teamId) throws Exception {
    	List<String> emailReceivers=new ArrayList<String>();
    	
    	AmpMessageState state = new AmpMessageState();    	
    	AmpTeamMember msgSender=TeamMemberUtil.getAmpTeamMember(approval.getSenderId());
    	emailReceivers.add(msgSender.getUser().getEmail());
        state.setReceiver(msgSender);
        createMsgState(state, approval,false);
        if (triggerClass.equals(NotApprovedActivityTrigger.class)) {
            AmpTeamMember teamHead=TeamMemberUtil.getTeamHead(teamId);
            emailReceivers.add(teamHead.getUser().getEmail());
            if (teamHead != null) {
                state = new AmpMessageState();
                state.setReceiver(teamHead);
                createMsgState(state, approval,false);
            }
        }        
        //define emails and receivers
        createEmailsAndReceivers(approval,emailReceivers,false);
    }

    
    private static void defineReceiversForApprovedCalendarEvent(AmpTeamMember msgSender, AmpMessage approval) throws Exception {
    	List<String> emailReceivers=new ArrayList<String>();
    	
    	AmpMessageState state = new AmpMessageState();    	
    	emailReceivers.add(msgSender.getUser().getEmail());
        state.setReceiver(msgSender);
        createMsgState(state, approval,false);
       
        //define emails and receivers
        createEmailsAndReceivers(approval,emailReceivers,false);
    }
    
    /**
     * this method defines calendar event receivers and creates corresponding AmpMessageStates.
     * saveActionWasCalled field is used to define whether user created new calendar event or not
     */
    private static void defineReceiversForCalendarEvents(Event e, TemplateAlert template, AmpMessage calEvent,boolean  saveActionWasCalled,boolean eventRemoved) throws Exception {
        HashMap<Long, AmpMessageState> temMsgStateMap = new HashMap<Long, AmpMessageState> ();
        List<AmpMessageState> lstMsgStates = AmpMessageUtil.loadMessageStates(template.getId());
        List<String> emailReceivers=new ArrayList<String>();
        if (lstMsgStates != null) {
            for (AmpMessageState state : lstMsgStates) {
                if (!temMsgStateMap.containsKey(state.getReceiver().getAmpTeamMemId())) {
                    temMsgStateMap.put(state.getReceiver().getAmpTeamMemId(), state);
                }
            }
        }

        HashMap<Long, AmpMessageState> eventMsgStateMap = new HashMap<Long, AmpMessageState> ();
        Long calId = new Long(e.getParameters().get(CalendarEventTrigger.PARAM_ID).toString());
        AmpCalendar ampCal=null;
        Set<AmpCalendarAttendee> att= null;
        if(eventRemoved){
        	att=(Set<AmpCalendarAttendee>)e.getParameters().get(RemoveCalendarEventTrigger.ATTENDEES);
        }else{
        	ampCal = AmpDbUtil.getAmpCalendar(calId);
            att = ampCal.getAttendees();
        }   
        if (att != null) {
            for (AmpCalendarAttendee ampAtt : att) {
                if (ampAtt.getMember() != null) {
                    AmpTeamMember member = ampAtt.getMember();
                    if (!eventMsgStateMap.containsKey(member.getAmpTeamMemId())) {
                        AmpMessageState state = new AmpMessageState();
                        //state.setMemberId(member.getAmpTeamMemId());
                        state.setReceiver(member);
                        state.setSenderId(calEvent.getSenderId());
                        eventMsgStateMap.put(state.getReceiver().getAmpTeamMemId(), state);
                    }
                }else if(ampAtt.getGuest()!=null){ // <---guests should always get e-mails about event
                	String emailAddress=ampAtt.getGuest();
                	emailReceivers.add(emailAddress);                	
                	//sendMail(((CalendarEvent)calEvent).getSenderEmail(),emailAddress, calEvent.getName(), "UTF-8", calEvent.getDescription());
                }
            }
        }
        HashMap<Long, AmpMessageState> msgStateMap = new HashMap<Long, AmpMessageState> ();
        
        //calendar event creator should also get a message (AMP-3775)
        if(saveActionWasCalled){
        	AmpTeamMember calEventcreator=ampCal.getMember();
            AmpMessageState msgState = new AmpMessageState();
           // msgState.setMemberId(calEventcreator.getAmpTeamMemId());
            msgState.setReceiver(calEventcreator);
            msgState.setSenderId(calEvent.getSenderId());
            msgStateMap.put(msgState.getReceiver().getAmpTeamMemId(), msgState);
        }        
        
        for (AmpMessageState state : temMsgStateMap.values()) {
            if (eventMsgStateMap.containsKey(state.getReceiver().getAmpTeamMemId())) {
                msgStateMap.put(state.getReceiver().getAmpTeamMemId(), state);
            }
        }       
        
        for (AmpMessageState state : msgStateMap.values()) {
            createMsgState(state, calEvent,saveActionWasCalled);
            emailReceivers.add(state.getReceiver().getUser().getEmail());
        }
        
        createEmailsAndReceivers(calEvent,emailReceivers,saveActionWasCalled);
    }

    /**
     * this method defines alert receivers and creates corresponding AmpMessageStates.
     */

    private static void defineReceievrsByActivityTeam(TemplateAlert template, AmpMessage alert, Long teamId) throws Exception {
        List<AmpMessageState> statesRelatedToTemplate = null;
        //get the member who created an activity. it's the current member    

        statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
        if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
        	List<String> emailReceivers=new ArrayList<String>();
            //create receivers list for activity
            String receivers;
            Collection<TeamMember> teamMembers =TeamMemberUtil.getAllTeamMembers(teamId);
            receivers = fillTOfieldForReceivers(teamMembers, statesRelatedToTemplate);
            alert.setReceivers(receivers);

            for (AmpMessageState state : statesRelatedToTemplate) {            	
                //get receiver Team Member.
            	AmpTeamMember teamMember=state.getReceiver();
                /**
                 * Alert about new activity creation should get only members of the same team in which activity was created,if this team is listed as receivers in template.
                 */
                if (teamMember.getAmpTeam().getAmpTeamId().equals(teamId)) {
                    createMsgState(state, alert,false);
                    emailReceivers.add(state.getReceiver().getUser().getEmail());
                }
            }
            createEmailsAndReceivers(alert,emailReceivers,false);
        }
    }

    private static void defineActivityCreationReceievrs(TemplateAlert template, AmpMessage alert) throws Exception {
        List<AmpMessageState> statesRelatedToTemplate = null;
        //get the member who created an activity. it's the current member
        AmpTeamMember activityCreator = TeamMemberUtil.getAmpTeamMember(alert.getSenderId());

        statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
        if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
        	List<String> receiversAddresses = new ArrayList<String>(); //receivers that should get emails
            //create receivers list for activity
            String receivers;
            Collection<TeamMember> teamMembers = TeamMemberUtil.getAllTeamMembers(activityCreator.getAmpTeam().getAmpTeamId());
            receivers = fillTOfieldForReceivers(teamMembers, statesRelatedToTemplate);
            alert.setReceivers(receivers);

            for (AmpMessageState state : statesRelatedToTemplate) {
                //get receiver Team Member.
            	AmpTeamMember teamMember=state.getReceiver();
                /**
                 * Alert about new activity creation should get only members of the same team in which activity was created,if this team is listed as receivers in template.
                 */
                if (teamMember.getAmpTeam().getAmpTeamId().equals(activityCreator.getAmpTeam().getAmpTeamId())) {
                    createMsgState(state, alert,false);
                    receiversAddresses.add(teamMember.getUser().getEmail());
                }
            }
            
            //Emails and Receivers
            createEmailsAndReceivers(alert,receiversAddresses,false);
        }
    }

    private static void createMsgState(AmpMessageState state, AmpMessage newMsg,boolean calendarSaveActionWasCalled) throws Exception {
        AmpMessageState newState = new AmpMessageState();
        Class clazz=newMsg.getClass();
        newState.setMessage(newMsg);
        //newState.setMemberId(state.getMemberId());
        newState.setReceiver(state.getReceiver());
        newState.setRead(false);
        if(newMsg.getClassName().equals("c")){
        	newState.setSender(((CalendarEvent)newMsg).getSenderEmail());
        }        
        //will this message be visible in user's mailbox
        int maxStorage=-1;
		AmpMessageSettings setting=AmpMessageUtil.getMessageSettings();
		if(setting!=null && setting.getMsgStoragePerMsgType()!=null){
			maxStorage=setting.getMsgStoragePerMsgType().intValue();
		}
        if (AmpMessageUtil.isInboxFull(newMsg.getClass(), state.getReceiver().getAmpTeamMemId()) || (maxStorage>=0 && AmpMessageUtil.getInboxMessagesCount(clazz, state.getReceiver().getAmpTeamMemId(), false, false, maxStorage) >= maxStorage)) {
            newState.setMessageHidden(true);
        } else {
            newState.setMessageHidden(false);
        }
        try {
            AmpMessageUtil.saveOrUpdateMessageState(newState);
        } catch (AimException e) {
            e.printStackTrace();
        }
    }

    /*
     * This function is used to create receivers String, which will be shown on view message page in TO: section
     */
    private static String fillTOfieldForReceivers(Collection<TeamMember> teamMembers, List<AmpMessageState> states) {
        String receivers = "";
        for (AmpMessageState state : states) {
            for (TeamMember tm : teamMembers) {
                if (state.getReceiver().getAmpTeamMemId().equals(tm.getMemberId())) {
                    receivers += tm.getMemberName() + " " + "<" + tm.getEmail() + ">;" + tm.getTeamName() + ";" + ", ";
                }
            }
        }
        return receivers;
    }

    
    /**
     * Create AmpEmails with receivers that Quartz Job will use to send emails when called
     * @param message
     * @param receiversAddresses
     * @param calendarSaveActionWasCalled
     * @throws Exception
     */
    private static void createEmailsAndReceivers(AmpMessage message,List<String> receiversAddresses,boolean calendarSaveActionWasCalled) throws Exception{
    	AmpMessageSettings messageSettings = AmpMessageUtil.getMessageSettings();
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent();
        String objUrl=message.getObjectURL();
        String description=message.getDescription();
        if(objUrl!=null){
            if(partialURL.endsWith("/")){
                partialURL=partialURL.substring(0,partialURL.length()-1);
            }
            String url=partialURL+objUrl;
            description=description.replaceAll(objUrl,url);
        }
    	if(messageSettings!=null){
    		Long sendMail = messageSettings.getEmailMsgs();
            if(sendMail!=null&&sendMail.intValue() == 1){
            	AmpEmail ampEmail=null;
            	AmpTeamMember msgSender=TeamMemberUtil.getAmpTeamMember(message.getSenderId());            	
           	 	//create AmpEmail
            	if(calendarSaveActionWasCalled){ // <---means that user created new calendar event. if so, a bit different e-mail should be sent
            		ampEmail=new AmpEmail(msgSender.getUser().getEmail(),message.getName(),description);
            	}else{
            		ampEmail=new AmpEmail("system@digijava.org",message.getName(),description);
            	}
            	
            	DbUtil.saveOrUpdateObject(ampEmail);
            	//email receivers
            	if(receiversAddresses.size()>0){
            		for (String emailAddr : receiversAddresses) {
            			AmpEmailReceiver emailReceiver=new AmpEmailReceiver(emailAddr, ampEmail, MessageConstants.UNSENT_STATUS);
            			DbUtil.saveOrUpdateObject(emailReceiver);
            		}
            	}
            }
    	}
    }
}