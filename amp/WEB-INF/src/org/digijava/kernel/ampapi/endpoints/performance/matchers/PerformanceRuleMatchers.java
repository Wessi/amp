package org.digijava.kernel.ampapi.endpoints.performance.matchers;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Viorel Chihai
 *
 */
public final class PerformanceRuleMatchers {

    public static final List<PerformanceRuleMatcher> RULE_TYPES = new ArrayList<PerformanceRuleMatcher>() {
        {
            add(new NoDisbursmentsAfterSignatureDateMatcher());
        }
    };

    private PerformanceRuleMatchers() {

    }

}
