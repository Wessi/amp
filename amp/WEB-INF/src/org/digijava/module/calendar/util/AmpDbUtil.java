package org.digijava.module.calendar.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.dbentity.AmpCalendarPK;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.entity.AmpEventType;
import org.digijava.module.calendar.exception.CalendarException;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AmpDbUtil {
  private static Logger logger = Logger.getLogger(AmpDbUtil.class);

  public static List getUsers() throws CalendarException {
    try {
      Session session = PersistenceManager.getRequestDBSession();
      String queryString = "from " + User.class.getName();
      Query query = session.createQuery(queryString);
      return query.list();
    }
    catch (Exception e) {
      logger.debug("Unable to get users from database", e);
      throw new CalendarException("Unable to get users from database", e);
    }
  }

  public static AmpCalendar getAmpCalendar(Long ampCalendarId) {
	  AmpCalendar ampCalendar=null;
	  Calendar calendar=null;
	  if(ampCalendarId!=null){
		  try {
			Session session = PersistenceManager.getRequestDBSession();
	        //Calendar calendar = (Calendar) session.load(Calendar.class,ampCalendarId);
			Query query=session.createQuery("from "+ Calendar.class.getName() + " c where c.id= :calendarId");
			query.setParameter("calendarId", ampCalendarId);
			calendar=(Calendar)query.uniqueResult();
			if(calendar!=null){
				AmpCalendarPK ampCalendarPK = new AmpCalendarPK(calendar);
				query=session.createQuery("from "+ AmpCalendar.class.getName() + " c where c.calendarPK= :calendarPK");
				query.setParameter("calendarPK", ampCalendarPK);
				ampCalendar=(AmpCalendar)query.uniqueResult();
				//ampCalendar = (AmpCalendar) session.load(AmpCalendar.class,ampCalendarPK);
			}
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  return ampCalendar;
  }

  public static List<AmpCalendar> getAmpCalendarsByStartDate(Date startDate) {
      if (startDate == null) {
          return null;
      }
      try {
          Session session = PersistenceManager.getRequestDBSession();

          List<AmpCalendar> ampCalLst = new ArrayList<AmpCalendar> ();

          List<Calendar> clst = getCalendarsByStartDate(startDate);
          for (Calendar cal : clst) {
              AmpCalendarPK ampCalendarPK = new AmpCalendarPK(cal);
              AmpCalendar ampCalendar = (AmpCalendar) session.load(AmpCalendar.class, ampCalendarPK);
              ampCalLst.add(ampCalendar);
          }
          return ampCalLst;
      } catch (Exception e) {
          logger.debug("Unable to get AmpCalendars by Start Date", e);
          return null;
      }
  }

  public static List<AmpCalendar> getAmpCalendarsByEndDate(Date endDate) {
      if (endDate == null) {
          return null;
      }
      try {
          Session session = PersistenceManager.getRequestDBSession();

          List<AmpCalendar> ampCalLst = new ArrayList<AmpCalendar> ();

          List<Calendar> clst = getCalendarsByEndDate(endDate);
          for (Calendar cal : clst) {
              AmpCalendarPK ampCalendarPK = new AmpCalendarPK(cal);
              AmpCalendar ampCalendar = (AmpCalendar) session.load(AmpCalendar.class, ampCalendarPK);
              ampCalLst.add(ampCalendar);
          }
          return ampCalLst;
      } catch (Exception e) {
          logger.debug("Unable to get Calendars by Start Date", e);
          return null;
      }
  }


  public static List<Calendar> getCalendarsByStartDate(Date startDate) {
    if(startDate == null) {
      return null;
    }
    try{
      Session session = PersistenceManager.getRequestDBSession();
      String queryString = "select c from "+Calendar.class.getName()+" c";
      Query query = session.createQuery(queryString);

      List<Calendar> exList=query.list();
      List<Calendar> rList=new ArrayList<Calendar>();

      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      String strStartDate=sdf.format(startDate);
      for(Calendar exCal:exList){
          String exStrStartDate=sdf.format(exCal.getStartDate());
          if(strStartDate.equals(exStrStartDate)){
              rList.add(exCal);
          }
      }
      return rList;
    }catch (Exception e) {
      logger.debug("Unable to get AmpCalendars by Start Date", e);
      return null;
    }
  }

  public static List<Calendar> getCalendarsByEndDate(Date endDate) {
    if(endDate == null) {
      return null;
    }
    try{
        Session session = PersistenceManager.getRequestDBSession();
        String queryString = "select c from "+Calendar.class.getName()+" c";
        Query query = session.createQuery(queryString);

        List<Calendar> exList=query.list();
        List<Calendar> rList=new ArrayList<Calendar>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strEndDate=sdf.format(endDate);
        for(Calendar exCal:exList){
            String exStrEndDate=sdf.format(exCal.getEndDate());
            if(strEndDate.equals(exStrEndDate)){
                rList.add(exCal);
            }
        }
      return rList;
    }catch (Exception e) {
      logger.debug("Unable to get AmpCalendars by Start Date", e);
      return null;
    }
  }
  public static AmpCalendar getAmpCalendar(Long ampCalendarId,String instanceId, String siteId) {
    AmpCalendar ampCalendar = getAmpCalendar(ampCalendarId);  
    Calendar calendar = ampCalendar != null ?
        ampCalendar.getCalendarPK().getCalendar() : null;
    if (calendar == null || calendar.getInstanceId() == null ||
        calendar.getSiteId() == null) {
      return null;
    }
    if (calendar.getInstanceId().equals(instanceId) &&
        calendar.getSiteId().equals(siteId)) {
      return ampCalendar;
    }
    else {
      return null;
    }
  }

  public static List<AmpCalendarAttendee> getAmpCalendarAttendees(AmpCalendar cal) {
      try {
          Session session = PersistenceManager.getRequestDBSession();
          String queryString = "from " + AmpCalendarAttendee.class.getName() +
              " att where att.CALENDAR_ID = :calendarId";
          Query query = session.createQuery(queryString);
          query.setLong("calendarId", cal.getCalendarPK().getCalendar().getId());

          return query.list();
      }
      catch (Exception e) {
        logger.debug("Unable to get AmpCalendarAttendees by AmpCalendar", e);
        return null;
    }
  }
  public static void deleteAmpCalendarAttendee(AmpCalendarAttendee attendee) throws  CalendarException {
		Transaction tx = null;
		try {
		  Session session = PersistenceManager.getRequestDBSession();
		  tx = session.beginTransaction();  
		  session.delete(attendee);
		  tx.commit();
		}
		catch (Exception ex) {
		  if (tx != null) {
		    try {
		      tx.rollback();
		    }
		    catch (Exception ex1) {
		      throw new CalendarException(
		          "Cannot rollback delete AMPCalendar", ex1);
		    }
		  }
		  throw new CalendarException("Canot delete AMPCalendar", ex);
		}
  }

  public static void deleteAmpCalendar(Long ampCalendarId) throws
      CalendarException {
    Transaction tx = null;
    try {
      Session session = PersistenceManager.getRequestDBSession();
      tx = session.beginTransaction();
      AmpCalendar ampCal = getAmpCalendar(ampCalendarId);
      Calendar  cal=ampCal.getCalendarPK().getCalendar();
      session.delete(ampCal);
      session.delete(cal);
      tx.commit();
    }
    catch (Exception ex) {
      if (tx != null) {
        try {
          tx.rollback();
        }
        catch (Exception ex1) {
          throw new CalendarException(
              "Cannot rollback delete AMPCalendar", ex1);
        }
      }
      throw new CalendarException("Canot delete AMPCalendar", ex);
    }
  }
  
 
  public static void updateAmpCalendarAttendee(AmpCalendarAttendee attendee) throws CalendarException{
	  Transaction tx = null;
	  try {
		  Session session = PersistenceManager.getRequestDBSession();
	      tx = session.beginTransaction();
	      session.update(attendee);
	      tx.commit();
	} catch (Exception e) {
		throw new CalendarException("Unable to update AmpCalendarAttendee object", e);
	}
  }

  public static void updateAmpCalendar(AmpCalendar ampCalendar) throws
      CalendarException {
    Transaction tx = null;
    try {
        Session session = PersistenceManager.getRequestDBSession();
        tx = session.beginTransaction();
        Calendar calendar = ampCalendar.getCalendarPK().getCalendar();
        // calendar
        if (calendar.getId() == null) {
            session.save(calendar);
            session.save(ampCalendar);
        } else {
            if (calendar.getCalendarItem() != null) {
                for (Iterator itemIter = calendar.getCalendarItem().iterator(); itemIter.hasNext(); ) {
                    CalendarItem item = (CalendarItem) itemIter.next();
                    if(item.getId()<0){
                        item.setId(-item.getId());
                        session.delete(item);
                        itemIter.remove();
                    }
                }
            }

            if (ampCalendar.getAttendees() != null) {
                for (Iterator attIter = ampCalendar.getAttendees().iterator(); attIter.hasNext(); ) {
                    AmpCalendarAttendee att = (AmpCalendarAttendee) attIter.next();
                    if(att.getId()<0){
                        att.setId(-att.getId());
                        session.delete(att);
                        attIter.remove();
                    }
                }
            }

            session.update(calendar);
            session.update(ampCalendar);
        }
        tx.commit();
    }
    catch (Exception e) {
      if (tx != null) {
        try {
          tx.rollback();
        }
        catch (Exception ex) {
          logger.warn("Unable to rollback AmpCalendar object", ex);
        }
      }
      logger.debug("Unable to update AmpCalendar object", e);
      throw new CalendarException("Unable to update AmpCalendar object",
                                  e);
    }
  }

  public static Collection<AmpCalendar> getAmpCalendarEventsPublic(
		  Integer showPublicEvents, 
		  String[] selectedDonorIds,String[] selectedeventTypeIds,
		  String instanceId, 
		  String siteId)
		  throws CalendarException {

	  try {
		  Hashtable<Long, AmpCalendar> retEvents=new Hashtable<Long,AmpCalendar>();
		
			List events = getAmpCalendarEvents(selectedDonorIds,selectedeventTypeIds, null, showPublicEvents, instanceId, siteId);
			if (events != null) {
				for (Iterator eventItr = events.iterator(); eventItr.hasNext(); ) {
					AmpCalendar ampCal = (AmpCalendar) eventItr.next();
					if (ampCal.getMember() != null) {
						retEvents.put(ampCal.getCalendarPK().getCalendar().getId(), ampCal);
					}
				}
			}
			return retEvents.values();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
  }
  
  public static Collection<AmpCalendar> getAmpCalendarEventsByMember(AmpTeamMember curMember,
		  Integer showPublicEvents, 
		  String[] selectedDonorIds,String[] selectedeventTypeIds,
		  String instanceId, 
		  String siteId)
		  throws CalendarException {

	  try{


		  Hashtable<Long, AmpCalendar> retEvents=new Hashtable<Long,AmpCalendar>();
		
	List events = getAmpCalendarEvents(selectedDonorIds,selectedeventTypeIds, curMember.getUser().getId(), showPublicEvents, instanceId, siteId);
	if (events != null) {
		for (Iterator eventItr = events.iterator(); eventItr.hasNext(); ) {
			AmpCalendar ampCal = (AmpCalendar) eventItr.next();
			if (ampCal.getMember() != null) {

				if(ampCal.getMember().getAmpTeamMemId().equals(curMember.getAmpTeamMemId())){
					retEvents.put(ampCal.getCalendarPK().getCalendar().getId(), ampCal);
				}


				boolean isCurrentMemberEventAttendee=false;
					if(ampCal.getAttendees()!=null && ampCal.getAttendees().size()>0){
						for (Object attendee : ampCal.getAttendees()) {
							AmpCalendarAttendee att=(AmpCalendarAttendee)attendee;
							if(att.getMember()!=null && att.getMember().getAmpTeamMemId().equals(curMember.getAmpTeamMemId())){
								isCurrentMemberEventAttendee=true;
								break;
							}
						}
					}

				if(!ampCal.isPrivateEvent() && (showPublicEvents==0 || showPublicEvents==2)){
					retEvents.put(ampCal.getCalendarPK().getCalendar().getId(), ampCal);
					continue;
				}
				if(ampCal.isPrivateEvent() && (showPublicEvents==0 || showPublicEvents==1)){
					if(isCurrentMemberEventAttendee){
						retEvents.put(ampCal.getCalendarPK().getCalendar().getId(), ampCal);
						continue;
					}
				}
			}
		}
	}
		return retEvents.values();
	  	} catch (Exception ex) {
	  		throw new RuntimeException(ex);
	  	}
  }
  
  public static List getAmpCalendarEvents(String[] selectedDonorIds,String[] selectedEventTypeIds,
		  Long userId,Integer showPublicEvents,String instanceId, String siteId) throws CalendarException {
	  try {
		  Session session = PersistenceManager.getRequestDBSession();

		  StringBuffer queryString = new StringBuffer();
		  	queryString.append("select ac from ").append(AmpCalendar.class.getName());
		  	queryString.append(" ac left join ac.organisations org, ").append(Calendar.class.getName()).append(" c ");

			queryString.append(" where c.id=ac.calendarPK.calendar.id ");


			if(showPublicEvents == 2){
	        	  queryString.append(" and ac.privateEvent=false");
	        }
			if(showPublicEvents == 1){
	        	  queryString.append(" and ac.privateEvent=true");
	        }
			List <Long> selectedDonors = new ArrayList<Long>();
			if(selectedDonorIds != null && selectedDonorIds.length != 0) {
			boolean includeNullOrganizations = false;
			for(int index = 0; index < selectedDonorIds.length; index++){
				if(selectedDonorIds[index].equals("None")){
				includeNullOrganizations = true;
			} else {
				selectedDonors.add(Long.parseLong(selectedDonorIds[index]));
			}
			}

				queryString.append(" and (");
			if (selectedDonors.size() > 0) {
				queryString.append("org.id in (:selectedDonorIds)");
			}
			if (includeNullOrganizations) {
			if (selectedDonors.size() > 0) {
				queryString.append(" or ");
			}
				queryString.append("org is null");
			}
				queryString.append(")");
			}
			
			if ( (selectedEventTypeIds != null) && (selectedEventTypeIds.length > 0) ) {
	        	  queryString.append(" and ac.eventsType.id in (:selectedEventTypes)");
	          }else{
	        	  queryString.append(" and 0 = 1");
	          }
			
			if (instanceId != null) {
				queryString.append(" and c.instanceId = :instanceId");
			}
			if (siteId != null) {
				queryString.append(" and c.siteId = :siteId");
			}
			Query query = session.createQuery(queryString.toString());
			
			if (selectedDonorIds != null && selectedDonorIds.length != 0 && selectedDonors.size()>0) {
				query.setParameterList("selectedDonorIds", selectedDonors);
			}
			
			if ( (selectedEventTypeIds != null) && (selectedEventTypeIds.length > 0) ) {
	        	  List <Long> selectedEventType = new ArrayList<Long>();
	        	  for(int index = 0; index < selectedEventTypeIds.length; index++) {
	        		  selectedEventType.add(Long.parseLong(selectedEventTypeIds[index]));
	        	  }
	              query.setParameterList("selectedEventTypes", selectedEventType);
	          }
			
			if (instanceId != null) {
				query.setString("instanceId", instanceId);
			}
			if (siteId != null) {
				query.setString("siteId", siteId);
			}
			
				return query.list();
			} catch (Exception ex) {
				logger.debug("Unable to get amp calendar events", ex);
			throw new CalendarException("Unable to get amp calendar events", ex);
	}
}

  
  public static List getAmpCalendarEventsType() throws CalendarException {
		List events = null;
		Session session = null;
	try {
		session = PersistenceManager.getRequestDBSession();

		 String queryString = "from " + AmpEventType.class.getName();
	     Query q = session.createQuery(queryString);
		 events = q.list();
		 
	}
	catch (Exception ex) {
		logger.debug("Unable to get event list from database", ex);
		throw new CalendarException(
				"Unable to get event list from database", ex);
	}

	return events;
}

	  public static List getAmpEventTypeIdByCalendarID(Long calendarId) throws CalendarException {
			List id = null;
			Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();

			Query q = session.createQuery("select c from " +
		     AmpCalendar.class.getName() +
			 " c where (c.id=:calendarId)");
	
			q.setParameter("calendarId", calendarId, Hibernate.LONG);
			//q.setParameter("userId", userId, Hibernate.LONG);
			id = q.list();

		}
		catch (Exception ex) {
			logger.debug("Unable to get event list from database", ex);
			throw new CalendarException(
					"Unable to get event list from database", ex);
		}

		return id;
	}
  }
  

