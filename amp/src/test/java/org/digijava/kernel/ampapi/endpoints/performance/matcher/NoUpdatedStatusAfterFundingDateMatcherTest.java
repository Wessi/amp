package org.digijava.kernel.ampapi.endpoints.performance.matcher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.dgfoundation.amp.activity.builder.CategoryClassBuilder;
import org.dgfoundation.amp.activity.builder.CategoryValueBuilder;
import org.dgfoundation.amp.activity.builder.FundingBuilder;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleConstants;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoUpdatedStatusAfterFundingDateMatcherDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

/**
 * 3 months went by after the contract signature date and the project status was not modified from planned to ongoing
 * 
 * @author Viorel Chihai
 */
public class NoUpdatedStatusAfterFundingDateMatcherTest extends PerformanceRuleMatcherTest {
    
    @Before
    public void setUp() {
        super.setUp();
        definition = new NoUpdatedStatusAfterFundingDateMatcherDefinition();
    }
    
    @Test
    public void testValidation() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "3", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, Constants.ACTIVITY_STATUS_ONGOING, 
                getCriticalLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);

        assertTrue(matcher.validate());
    }

    @Test
    public void testPlannedStatusAfterClasificationDate() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "3", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, Constants.ACTIVITY_STATUS_ONGOING, 
                getCriticalLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2016, 12, 12).toDate())
                                .getFunding())
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2017, 5, 12).toDate())
                                .getFunding())
                .addCategoryValue(
                        new CategoryValueBuilder()
                                .withLabel(Constants.ACTIVITY_STATUS_PLANNED)
                                .withCategoryClass(
                                        new CategoryClassBuilder()
                                        .withKey(CategoryConstants.ACTIVITY_STATUS_KEY)
                                        .getCategoryClass())
                                .getCategoryValue())
                .getActivity();
        
        assertFalse(matcher.match(a));
    }
    
    @Test
    public void testUpdatedActivityStatus() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "3", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, Constants.ACTIVITY_STATUS_ONGOING, 
                getCriticalLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2015, 10, 12).toDate())
                                .getFunding())
                .addCategoryValue(
                        new CategoryValueBuilder()
                                .withLabel(Constants.ACTIVITY_STATUS_ONGOING)
                                .withCategoryClass(
                                        new CategoryClassBuilder()
                                        .withKey(CategoryConstants.ACTIVITY_STATUS_KEY)
                                        .getCategoryClass())
                                .getCategoryValue())
                .getActivity();
        
        assertFalse(matcher.match(a));
    }
    
    @Test
    public void testNotUpdatedActivitySatus() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "3", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, Constants.ACTIVITY_STATUS_ONGOING, 
                getCriticalLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2017, 3, 13).toDate())
                                .getFunding())
                .addCategoryValue(
                        new CategoryValueBuilder()
                                .withLabel(Constants.ACTIVITY_STATUS_PLANNED)
                                .withCategoryClass(
                                        new CategoryClassBuilder()
                                        .withKey(CategoryConstants.ACTIVITY_STATUS_KEY)
                                        .getCategoryClass())
                                .getCategoryValue())
                .getActivity();
        
        assertTrue(matcher.match(a));
    }
    
    @Test
    public void testActivityStatusUpdatedWihtoutTimePeriod() {
        AmpPerformanceRule rule = createRule(PerformanceRuleConstants.TIME_UNIT_MONTH, "3", 
                PerformanceRuleConstants.FUNDING_CLASSIFICATION_DATE, Constants.ACTIVITY_STATUS_ONGOING, 
                getCriticalLevel());
        
        PerformanceRuleMatcher matcher = definition.createMatcher(rule);
        
        AmpActivityVersion a = new ActivityBuilder()
                .addFunding(
                        new FundingBuilder()
                                .withClassificationDate(new LocalDate(2017, 7, 13).toDate())
                                .getFunding())
                .addCategoryValue(
                        new CategoryValueBuilder()
                                .withLabel(Constants.ACTIVITY_STATUS_ONGOING)
                                .withCategoryClass(
                                        new CategoryClassBuilder()
                                        .withKey(CategoryConstants.ACTIVITY_STATUS_KEY)
                                        .getCategoryClass())
                                .getCategoryValue())
                .getActivity();
        
        assertFalse(matcher.match(a));
    }

    /**
     * @return
     */
    public AmpPerformanceRule createRule(String timeUnit, String timeAmount, String fundingDate, String activityStatus, 
            AmpCategoryValue level) {
        
        AmpPerformanceRule rule = new AmpPerformanceRule();

        AmpPerformanceRuleAttribute attr1 = new AmpPerformanceRuleAttribute();
        attr1.setName(PerformanceRuleConstants.ATTRIBUTE_TIME_UNIT);
        attr1.setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.TIME_UNIT);
        attr1.setValue(timeUnit);
        AmpPerformanceRuleAttribute attr2 = new AmpPerformanceRuleAttribute();
        attr2.setName(PerformanceRuleConstants.ATTRIBUTE_TIME_AMOUNT);
        attr2.setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.AMOUNT);
        attr2.setValue(timeAmount);
        AmpPerformanceRuleAttribute attr3 = new AmpPerformanceRuleAttribute();
        attr3.setName(PerformanceRuleConstants.ATTRIBUTE_FUNDING_DATE);
        attr3.setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.FUNDING_DATE);
        attr3.setValue(fundingDate);
        AmpPerformanceRuleAttribute attr4 = new AmpPerformanceRuleAttribute();
        attr4.setName(PerformanceRuleConstants.ATTRIBUTE_ACTIVITY_STATUS);
        attr4.setType(AmpPerformanceRuleAttribute.PerformanceRuleAttributeType.ACTIVITY_STATUS);
        attr4.setValue(activityStatus);

        rule.setAttributes(Stream.of(attr1, attr2, attr3, attr4).collect(Collectors.toSet()));
        rule.setLevel(level);

        return rule;
    }
    
}
