package org.digijava.module.message.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.Approval;
import org.digijava.module.message.dbentity.CalendarEvent;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.triggers.ActivityActualStartDateTrigger;
import org.digijava.module.message.triggers.ActivityCurrentCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityDisbursementDateTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForContractingTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForDisbursementsTrigger;
import org.digijava.module.message.triggers.ActivityProposedApprovalDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedStartDateTrigger;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.AlertForDonorsTrigger;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.CalendarEventSaveTrigger;
import org.digijava.module.message.triggers.CalendarEventTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;
import org.digijava.module.message.triggers.RemoveCalendarEventTrigger;
import org.digijava.module.message.util.AmpMessageUtil;

public class AmpMessageWorker {

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
                }
                else if (e.getTrigger().equals(AlertForDonorsTrigger.class)) {
                    newMsg = processDonorAlertEvent(e, newAlert, template);
                }

                AmpMessageUtil.saveOrUpdateMessage(newMsg);
                /**
                 * getting states according to tempalteId
                 * New Requirement for ApprovedActiviti and Activity waiting approval.only teamLeader(in case approval is needed) and
                 *  activity creator/updater should get an alert regardless of receivers list in template
                 */
                if(e.getTrigger().equals(ApprovedActivityTrigger.class) || e.getTrigger().equals(NotApprovedActivityTrigger.class)) {
                    defineReceiversForApprovedAndNotApprovedActivities(e.getTrigger(), newMsg,(Long)e.getParameters().get(NotApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM));
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
                }else if (e.getTrigger().equals(AlertForDonorsTrigger.class)) {
                    defineReceiversForDonorAlert(template, newMsg);
                }else{ //<-- currently for else is left user registration or activity disbursement date triggers
                    List<AmpMessageState> statesRelatedToTemplate = null;
                    statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
                    HashMap<Long, AmpMessageState> msgStateMap = new HashMap<Long, AmpMessageState> ();
                    if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
                        for (AmpMessageState state : statesRelatedToTemplate) {
                            createMsgState(state, newMsg,false);
                            if (!msgStateMap.containsKey(state.getReceiver().getAmpTeamMemId())) {
                                msgStateMap.put(state.getReceiver().getAmpTeamMemId(), state);
                            }
                        }
                        sendMailes(msgStateMap.values());
                    }
                }

                //sending mail to external reciviers
                String externalReceivers = template.getExternalReceivers();
                if (externalReceivers != null) {
                    String[] exReceivers = externalReceivers.split(",");
                    for (String email : exReceivers) {
                        if (email.indexOf("<") != -1) { // for handling contacts in the future...
                            email = email.substring(email.indexOf("<") + 1, email.indexOf(">"));
                        }
                        sendMail("system@digijava.org",email,template.getName(),"UTF8",template.getDescription());
                    }
                  
                }
              
            }
        }
    }

    /**
     *	Calendar Event processing
     */
    private static CalendarEvent proccessCalendarEvent(Event e, CalendarEvent event, TemplateAlert template,boolean saveActionWasCalled) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        //get event creator
        AmpTeamMember tm=(AmpTeamMember)e.getParameters().get(CalendarEventSaveTrigger.SENDER);

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(CalendarEventTrigger.PARAM_NAME));
        //if event was removed, then we can't create it's url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(CalendarEventTrigger.PARAM_URL) + "\">View Event</a>");
            event.setObjectURL(partialURL + e.getParameters().get(CalendarEventTrigger.PARAM_URL));
        }
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
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(NotApprovedActivityTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY)).getUser().getName());
        myHashMap.put(MessageConstants.OBJECT_TEAM,  ((Long)e.getParameters().get(NotApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM)).toString());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL) + "\">activity URL</a>");
            approval.setObjectURL(partialURL + e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL));
        }
        approval.setSenderId( ( (AmpTeamMember) e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY)).getAmpTeamMemId());
        approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createApprovalFromTemplate(template, myHashMap, approval, true);
    }

    /**
     *	Approved Activity Event processing
     */
    private static Approval processApprovedActivityEvent(Event e, Approval approval, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent();
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ApprovedActivityTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY)).getUser().getName());
        myHashMap.put(MessageConstants.OBJECT_TEAM,  ((Long)e.getParameters().get(ApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM)).toString());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ApprovedActivityTrigger.PARAM_URL) + "\">activity URL</a>");
            approval.setObjectURL(partialURL + e.getParameters().get(ApprovedActivityTrigger.PARAM_URL));
        }
        approval.setSenderId( ( (AmpTeamMember) e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY)).getAmpTeamMemId());
        approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createApprovalFromTemplate(template, myHashMap, approval, false);
    }

    private static AmpAlert processActivitySaveEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent();
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
            partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
                     }*/
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivitySaveTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivitySaveTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivitySaveTrigger.PARAM_URL));
        }
        alert.setSenderId( ( (AmpTeamMember) e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY)).getAmpTeamMemId());
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityActualStartDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent();
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityActualStartDateTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityActualStartDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityActualStartDateTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityActualStartDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityCurrentCompletionDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_NAME));
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityFinalDateForContractingEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
            partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_NAME));
        //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityFinalDateForDisbursementsEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_NAME));
        //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityProposedApprovalDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_NAME));
        //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityProposedCompletionDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        /*if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) != null) {
        partialURL = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SITE_DOMAIN) + "/";
        }*/

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_NAME));
        //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    private static AmpAlert processActivityProposedStartDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;
        
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_NAME));
        //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);

    }

    /**
     * Activity's disbursement date Event processing
     */
    private static AmpAlert processActivityDisbursementDateComingEvent(Event e, AmpAlert alert, TemplateAlert template) {
        DigiConfig config = DigiConfigManager.getConfig();
        String partialURL = config.getSiteDomain().getContent() ;

        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_NAME));
      //creator
        myHashMap.put(MessageConstants.OBJECT_AUTHOR, ( (AmpTeamMember) e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_CREATED_BY)).getUser().getName());
        //url
        if (partialURL != null) {
            myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + partialURL + e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL) + "\">activity URL</a>");
            alert.setObjectURL(partialURL + e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL));
        }
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);
    }

    /**
     *
     */
    private static AmpAlert processDonorAlertEvent(Event e, AmpAlert alert, TemplateAlert template) {
        HashMap<String, String> myHashMap = new HashMap<String, String> ();
        alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
        return createAlertFromTemplate(template, myHashMap, alert);
    }

    private static Approval createApprovalFromTemplate(TemplateAlert template, HashMap<String, String> myMap, Approval newApproval, boolean needsApproval) {
        newApproval.setName(DgUtil.fillPattern(template.getName(), myMap));
        newApproval.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
        newApproval.setDraft(false);
        newApproval.setCreationDate(new Date());
        //receivers
        String receivers;
        AmpTeamMember tm = TeamMemberUtil.getAmpTeamMember(newApproval.getSenderId());
        receivers = tm.getUser().getFirstNames() + " " + tm.getUser().getLastName() + "<" + tm.getUser().getEmail() + ">;" + tm.getAmpTeam().getName() + ";";
        if (needsApproval) {
            //team lead can't be null,because if team has no leader,then no trigger will be invoked
            String teamId=myMap.get(MessageConstants.OBJECT_TEAM);
            AmpTeamMember teamHead=TeamMemberUtil.getTeamHead(Long.parseLong(teamId));
            if(teamHead!=null){
            	receivers += ", " + teamHead.getUser().getFirstNames() + " " + teamHead.getUser().getLastName() + "<" + teamHead.getUser().getEmail() + ">;" + teamHead.getAmpTeam().getName() + ";";
            }            
        }
        newApproval.setReceivers(receivers);
        newApproval.setEmailable(template.getEmailable());
        newApproval.setExternalReceivers(template.getExternalReceivers());
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
        newAlert.setEmailable(template.getEmailable());
        newAlert.setExternalReceivers(template.getExternalReceivers());
        return newAlert;
    }

    private static CalendarEvent createEventFromTemplate(TemplateAlert template, HashMap<String, String> myMap, CalendarEvent newEvent) {
        newEvent.setName(DgUtil.fillPattern(template.getName(), myMap));
        newEvent.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
        newEvent.setReceivers(template.getReceivers());
        newEvent.setDraft(false);        
        Calendar cal = Calendar.getInstance();
        newEvent.setCreationDate(cal.getTime());
        newEvent.setEmailable(template.getEmailable());
        newEvent.setExternalReceivers(template.getExternalReceivers());
        return newEvent;
    }

    /**
     * this method defines approval receivers and creates corresponding AmpMessageStates.
     */
    private static void defineReceiversForApprovedAndNotApprovedActivities(Class triggerClass, AmpMessage approval,Long teamId) throws Exception {
        AmpMessageState state = new AmpMessageState();
        //state.setMemberId(approval.getSenderId());
        state.setReceiver(TeamMemberUtil.getAmpTeamMember(approval.getSenderId()));
        createMsgState(state, approval,false);
        if (triggerClass.equals(NotApprovedActivityTrigger.class)) {
            AmpTeamMember teamHead=TeamMemberUtil.getTeamHead(teamId);
            if (teamHead != null) {
                // Long teamHeadId = teamHead.getAmpTeamMemId();
                state = new AmpMessageState();
                // state.setMemberId(teamHeadId);
                state.setReceiver(teamHead);
                createMsgState(state, approval,false);
            }
        }
    }

    /**
     * this method defines calendar event receivers and creates corresponding AmpMessageStates.
     * saveActionWasCalled field is used to define whether user created new calendar event or not
     */
    private static void defineReceiversForCalendarEvents(Event e, TemplateAlert template, AmpMessage calEvent,boolean  saveActionWasCalled,boolean eventRemoved) throws Exception {
        HashMap<Long, AmpMessageState> temMsgStateMap = new HashMap<Long, AmpMessageState> ();
        List<AmpMessageState> lstMsgStates = AmpMessageUtil.loadMessageStates(template.getId());
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
                	String email=ampAtt.getGuest();
                	sendMail(((CalendarEvent)calEvent).getSenderEmail(),email, calEvent.getName(), "UTF-8", calEvent.getDescription());
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
        }
    }

    /**
     * this method defines alert receivers and creates corresponding AmpMessageStates.
     */

    private static void defineReceievrsByActivityTeam(TemplateAlert template, AmpMessage alert, Long teamId) throws Exception {
        List<AmpMessageState> statesRelatedToTemplate = null;
        //get the member who created an activity. it's the current member    

        statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
        if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
            //create receivers list for activity
            String receivers;
            Collection<TeamMember> teamMembers =TeamMemberUtil.getAllTeamMembers(teamId);
            receivers = fillTOfieldForReceivers(teamMembers, statesRelatedToTemplate);
            alert.setReceivers(receivers);

            for (AmpMessageState state : statesRelatedToTemplate) {
                //get receiver Team Member.
                //AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(state.getMemberId());
            	AmpTeamMember teamMember=state.getReceiver();
                /**
                 * Alert about new activity creation should get only members of the same team in which activity was created,if this team is listed as receivers in template.
                 */
                if (teamMember.getAmpTeam().getAmpTeamId().equals(teamId)) {
                    createMsgState(state, alert,false);
            }
    }
        }
    }

    private static void defineActivityCreationReceievrs(TemplateAlert template, AmpMessage alert) throws Exception {
        List<AmpMessageState> statesRelatedToTemplate = null;
        //get the member who created an activity. it's the current member
        AmpTeamMember activityCreator = TeamMemberUtil.getAmpTeamMember(alert.getSenderId());

        statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
        if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
            //create receivers list for activity
            String receivers;
            Collection<TeamMember> teamMembers = TeamMemberUtil.getAllTeamMembers(activityCreator.getAmpTeam().getAmpTeamId());
            receivers = fillTOfieldForReceivers(teamMembers, statesRelatedToTemplate);
            alert.setReceivers(receivers);

            for (AmpMessageState state : statesRelatedToTemplate) {
                //get receiver Team Member.
            	//AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(state.getMemberId());
            	AmpTeamMember teamMember=state.getReceiver();
                /**
                 * Alert about new activity creation should get only members of the same team in which activity was created,if this team is listed as receivers in template.
                 */
                if (teamMember.getAmpTeam().getAmpTeamId().equals(activityCreator.getAmpTeam().getAmpTeamId())) {
                    createMsgState(state, alert,false);
                }
            }
        }
    }
     private static void defineReceiversForDonorAlert(TemplateAlert template, AmpMessage alert) throws Exception {
        List<AmpMessageState> statesRelatedToTemplate = null;
        statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
        if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
            String receivers= fillTOfieldForReceivers(statesRelatedToTemplate);
            alert.setReceivers(receivers);
            alert.setExternalReceivers(template.getExternalReceivers());
            for (AmpMessageState state : statesRelatedToTemplate) {
                    createMsgState(state, alert,false);
                   
            }          
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
            sendMail(newState,calendarSaveActionWasCalled);
          
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
     /*
     * This function is used to create receivers String, which will be shown on view message page in TO: section
     */
    private static String fillTOfieldForReceivers(List<AmpMessageState> states) {
        String receivers = "";
        for (AmpMessageState state : states) {
            AmpTeamMember receiver=state.getReceiver();
            receivers+=receiver.getUser().getName() + " " + "<" + receiver.getUser().getEmail() + ">;" + receiver.getAmpTeam().getName()+ ";";

        }
        return receivers;
    }

    private static void sendMailes(Collection<AmpMessageState> statesRelatedToTemplate) throws Exception {
        for(AmpMessageState state:statesRelatedToTemplate){
            sendMail(state,false);
        }
    }

    private static void sendMail(AmpMessageState state,boolean calendarSaveActionWasCalled) throws Exception {
        //AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMember(state.getMemberId());
    	AmpTeamMember teamMember=state.getReceiver();
        if (teamMember != null) {
            Boolean emailable = state.getMessage().getEmailable();
            if(emailable!=null&&emailable){
            	User user = teamMember.getUser();
            	if(calendarSaveActionWasCalled){ // <---means that user created new calendar event. if so, a bit different e-mail should be sent
            		sendMail(state.getSender(),user.getEmail(),state.getMessage().getName(),"UTF8",state.getMessage().getDescription());
            	}else{
            		sendMail("system@digijava.org",user.getEmail(),"New alert","UTF8",state.getMessage().getDescription());
            	}                
            }
        }
    }

    private static void sendMail(String from, String to, String subject, String charset, String text) {
        try {
            InternetAddress[] ito = new InternetAddress[] {new InternetAddress(to)};
            Address[] cc = null;
            Address[] bcc = null;
            boolean asHtml = true;
            boolean log = true;
            boolean rtl = false;

            DgEmailManager.sendMail(ito, from, cc, bcc, subject, text, charset, asHtml, log, rtl);
        } catch (Exception ex) {

        }
    }
}