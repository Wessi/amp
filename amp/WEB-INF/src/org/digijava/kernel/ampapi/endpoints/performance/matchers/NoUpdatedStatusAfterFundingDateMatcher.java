package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.performance.PerfomanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 *  The matcher checks if the activity is in a certain status after certain period has passed 
 *  from the selected funding date
 * 
 * @author Viorel Chihai
 *
 */
public class NoUpdatedStatusAfterFundingDateMatcher extends PerformanceRuleMatcher {
    
    public NoUpdatedStatusAfterFundingDateMatcher() {
        super("noUpdatedStatusAfterFundingDate", "No updated status after selected funding date");

        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT, 
                "Time Unit", PerformanceRuleAttributeType.TIME_UNIT));
        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT, 
                "Time Amount", PerformanceRuleAttributeType.AMOUNT));
        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE, 
                "Funding Date", PerformanceRuleAttributeType.FUNDING_DATE));
        attributes.add(new PerformanceRuleMatcherAttribute(PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_STATUS, 
                "Activity Status", PerformanceRuleAttributeType.ACTIVITY_STATUS));
    }

    @Override
    public boolean match(AmpPerformanceRule rule, AmpActivityVersion a) {
        PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();
        
        int timeUnit = performanceRuleManager.getCalendarTimeUnit(
                performanceRuleManager.getAttributeValue(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT));
        int timeAmount = Integer.parseInt(performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT));
        String selectedDate = performanceRuleManager.getAttributeValue(rule,
                PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE);
        String selectedStatus = performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_STATUS);
        
        AmpCategoryValue activityStatus = CategoryManagerUtil
                .getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, a.getCategories());
        
        if (activityStatus == null) {
            return true;
        }
        
        Date currentDate = new Date();

        boolean statusIsNotUpdatedToOngoing = false;
        
        for(AmpFunding f : a.getFunding()) {
            Date fundingSelectedDate = getFundingDate(f, selectedDate);
            if (fundingSelectedDate != null && StringUtils.isNotBlank(selectedStatus)) {
                Date deadline = getDeadline(fundingSelectedDate, timeUnit, timeAmount);
                
                if (currentDate.after(deadline)) {
                    statusIsNotUpdatedToOngoing |= !selectedStatus.equals(activityStatus.getLabel());
                }
            }
        }

        return statusIsNotUpdatedToOngoing;
    }
}
