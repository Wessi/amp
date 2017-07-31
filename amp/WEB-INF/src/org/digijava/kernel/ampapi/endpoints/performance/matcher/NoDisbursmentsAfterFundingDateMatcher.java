package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import java.util.Date;

import org.digijava.kernel.ampapi.endpoints.performance.PerfomanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.helper.Constants;

/**
 * The matcher checks if no disbursments were entered after certain period has passed from the selected funding date
 * 
 * @author Viorel Chihai
 *
 */
public class NoDisbursmentsAfterFundingDateMatcher extends PerformanceRuleMatcher {
    
    public NoDisbursmentsAfterFundingDateMatcher(PerformanceRuleMatcherDefinition definition, AmpPerformanceRule rule) {
        super(definition, rule);
    }

    @Override
    public boolean match(AmpActivityVersion a) {
        PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();

        int timeUnit = performanceRuleManager.getCalendarTimeUnit(performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT));
        int timeAmount = Integer.parseInt(performanceRuleManager.getAttributeValue(rule, 
                PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT));
        String selectedFundingDate = performanceRuleManager.getAttributeValue(rule,
                PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE);
        
        for (AmpFunding f : a.getFunding()) {
            Date fundingSelectedDate = getFundingDate(f, selectedFundingDate);
            if (fundingSelectedDate != null) {
                Date deadline = getDeadline(fundingSelectedDate, timeUnit, timeAmount);
                
                boolean hasDisbursmentsAfterDeadline = f.getFundingDetails().stream()
                        .filter(t -> t.getTransactionType() == Constants.DISBURSEMENT)
                        .anyMatch(t -> t.getTransactionDate().after(deadline));
                
                if (hasDisbursmentsAfterDeadline) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public boolean validate() {
        PerfomanceRuleManager performanceRuleManager = PerfomanceRuleManager.getInstance();
        
        if (rule.getAttributes() == null) {
            return false;
        }
        
        AmpPerformanceRuleAttribute attr1 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT);
        AmpPerformanceRuleAttribute attr2 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT);
        AmpPerformanceRuleAttribute attr3 = performanceRuleManager
                .getAttributeFromRule(rule, PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE);

        if (attr1 == null || attr2 == null || attr3 == null) {
            throw new IllegalArgumentException();
        }
        
        return true;
    }
    
}
