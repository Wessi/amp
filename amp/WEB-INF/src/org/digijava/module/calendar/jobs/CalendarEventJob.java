package org.digijava.module.calendar.jobs;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.message.helper.CalendarEventTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.util.AmpMessageUtil;

public class CalendarEventJob implements StatefulJob{
    private static Logger logger = Logger.getLogger(CalendarEventJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException{

        Date curDate=new Date();
        Date dateBeforeDays;
        try{
            AmpMessageSettings as=AmpMessageUtil.getMessageSettings();
            if(as!=null &&
               as.getDaysForAdvanceAlertsWarnings()!=null &&
               as.getDaysForAdvanceAlertsWarnings().intValue()>0){
                dateBeforeDays=AmpDateUtils.getDateBeforeDays(curDate,as.getDaysForAdvanceAlertsWarnings().intValue());
            }else{
                dateBeforeDays=AmpDateUtils.getDateBeforeDays(curDate,3);
            }
        }catch(Exception ex){
            dateBeforeDays=AmpDateUtils.getDateBeforeDays(curDate,3);
        }

        java.util.Calendar cl=java.util.Calendar.getInstance();
        cl.setTime(curDate);

        List<Calendar> eventList=AmpDbUtil.getAmpCalendarsByStartDate(dateBeforeDays);

        for (Calendar cal: eventList){
            new CalendarEventTrigger(cal);
        }
        try {
            PersistenceManager.closeRequestDBSessionIfNeeded();
        } catch (DgException e) {
            e.printStackTrace();
        }
    }

    public CalendarEventJob() {
    }
}
