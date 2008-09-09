package org.digijava.module.message.jobs;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.um.util.AmpUserUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class UserVerifiedDateJob implements StatefulJob {
    public void execute(JobExecutionContext context) throws JobExecutionException{

        Date curDate=new Date();
        Date dateBeforeDays=AmpDateUtils.getDateBeforeDays(curDate,30);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String exDt=sdf.format(dateBeforeDays);

        Collection<User> userList=AmpUserUtil.getAllNotVerifiedUsers();
        if(userList!=null){
            for (User user : userList) {
                if (user.getCreationDate()!= null) {
                    String dt = sdf.format(user.getCreationDate());
                    if (dt.equals(exDt) && !user.isEmailVerified()) {
                        AmpUserUtil.deleteUser(user.getId());
                    }
                }
            }
        }
        try {
            PersistenceManager.closeRequestDBSessionIfNeeded();
        } catch (DgException e) {
            e.printStackTrace();
        }
    }
}
