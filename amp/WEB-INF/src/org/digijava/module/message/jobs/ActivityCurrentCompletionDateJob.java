package org.digijava.module.message.jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.message.triggers.ActivityDisbursementDateTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.digijava.module.message.util.AmpMessageUtil;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import java.text.SimpleDateFormat;

public class ActivityCurrentCompletionDateJob implements StatefulJob {
    public void execute(JobExecutionContext context) throws JobExecutionException{

        Date curDate=new Date();
        Date dateAfterDays=null;
        try{
            AmpMessageSettings as=AmpMessageUtil.getMessageSettings();
            if(as!=null &&
               as.getDaysForAdvanceAlertsWarnings()!=null &&
               as.getDaysForAdvanceAlertsWarnings().intValue()>0){
                dateAfterDays=AmpDateUtils.getDateAfterDays(curDate,as.getDaysForAdvanceAlertsWarnings().intValue());
            }else{
                dateAfterDays=AmpDateUtils.getDateAfterDays(curDate,3);
            }
        }catch(Exception ex){
            dateAfterDays=AmpDateUtils.getDateAfterDays(curDate,3);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String exDt=sdf.format(dateAfterDays);
        List<AmpActivity> actList=ActivityUtil.getAllActivitiesList();
        for (AmpActivity act: actList){
            String dt=sdf.format(act.getActualStartDate());
            if(dt.equals(exDt)){
                new ActivityDisbursementDateTrigger(act);
            }
        }
        try {
            PersistenceManager.closeRequestDBSessionIfNeeded();
        } catch (DgException e) {
            e.printStackTrace();
        }
    }
}
