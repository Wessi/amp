package org.digijava.kernel.ampapi.endpoints.performance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.digijava.kernel.ampapi.endpoints.performance.matchers.PerformanceRuleMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matchers.PerformanceRuleMatcherAttribute;
import org.digijava.kernel.ampapi.endpoints.performance.matchers.PerformanceRuleMatchers;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PerfomanceRuleManager {

    private static PerfomanceRuleManager performanceRuleManager;

    /**
     * 
     * @return PerfomanceRuleManager instance
     */
    public static PerfomanceRuleManager getInstance() {
        if (performanceRuleManager == null) {
            performanceRuleManager = new PerfomanceRuleManager();
        }

        return performanceRuleManager;
    }

    public AmpPerformanceRule getPerformanceRuleById(Long id) {
        AmpPerformanceRule performanceRule = (AmpPerformanceRule) PersistenceManager.getSession()
                .get(AmpPerformanceRule.class, id);

        if (performanceRule == null) {
            throw new PerformanceRuleException(PerformanceRulesErrors.RULE_INVALID, String.valueOf(id));
        }

        return performanceRule;
    }

    public AmpCategoryValue requireCategoryValueExists(Long id) {
        AmpCategoryValue acv = (AmpCategoryValue) PersistenceManager.getSession().get(AmpCategoryValue.class, id);

        if (acv == null) {
            throw new PerformanceRuleException(PerformanceRulesErrors.CATEGORY_VALUE_INVALID, String.valueOf(id));
        }

        return acv;
    }

    public void updatePerformanceRule(AmpPerformanceRule performanceRule) {
        getPerformanceRuleById(performanceRule.getId());

        if (performanceRule.getLevel() == null) {
            throw new PerformanceRuleException(PerformanceRulesErrors.REQUIRED_ATTRIBUTE, "level");
        }

        requireCategoryValueExists(performanceRule.getLevel().getId());

        Session session = PersistenceManager.getSession();
        session.merge(performanceRule);
    }

    public void savePerformanceRule(AmpPerformanceRule performanceRule) {

        if (performanceRule.getLevel() == null) {
            throw new PerformanceRuleException(PerformanceRulesErrors.REQUIRED_ATTRIBUTE, "level");
        }

        requireCategoryValueExists(performanceRule.getLevel().getId());

        Session session = PersistenceManager.getSession();
        session.saveOrUpdate(performanceRule);
    }

    public void deletePerformanceRule(Long id) {
        AmpPerformanceRule performanceRule = getPerformanceRuleById(id);

        Session session = PersistenceManager.getSession();
        session.delete(performanceRule);
    }

    public List<AmpPerformanceRule> getPerformanceRules() {

        Session session = PersistenceManager.getSession();

        return session.createCriteria(AmpPerformanceRule.class).addOrder(Order.asc("id")).list();
    }

    public ResultPage<AmpPerformanceRule> getPerformanceRules(int page, int size) {

        int recordsPerPage = size > 0 ? size : PerformanceRuleConstants.DEFAULT_RECORDS_PER_PAGE;
        int start = page > 0 ? (page - 1) * recordsPerPage : 0;

        Session session = PersistenceManager.getSession();

        List<AmpPerformanceRule> pagePerformanceRules = session.createCriteria(AmpPerformanceRule.class)
                .addOrder(Order.asc("id")).setFirstResult(start).setMaxResults(recordsPerPage).list();

        int totalRecords = (int) session.createCriteria(AmpPerformanceRule.class).setProjection(Projections.rowCount())
                .uniqueResult();

        ResultPage<AmpPerformanceRule> resultPage = new ResultPage<>();
        resultPage.setItems(pagePerformanceRules);
        resultPage.setTotalRecords(totalRecords);

        return resultPage;
    }

    public List<PerformanceRuleMatcherAttribute> getAttributes(String type) {
        PerformanceRuleMatcher matcher = PerformanceRuleMatchers.RULE_TYPES.stream()
                .filter(m -> m.getName().equals(type)).findAny()
                .orElseThrow(() -> new PerformanceRuleException(PerformanceRulesErrors.RULE_TYPE_INVALID, type));

        return matcher.getAttributes();
    }

    public List<PerformanceRuleMatcher> getTypes() {
        return PerformanceRuleMatchers.RULE_TYPES;
    }

    public Map<Long, AmpCategoryValue> matchActivities(List<Long> actIds) {

        List<AmpPerformanceRule> rules = getPerformanceRules().stream()
                .filter(AmpPerformanceRule::getEnabled)
                .collect(Collectors.toList());

        Map<Long, AmpCategoryValue> performanceAlertMap = new HashMap<>();
        actIds.stream().forEach(actId -> performanceAlertMap.put(actId, matchActivity(rules, actId)));

        return performanceAlertMap;
    }

    public AmpCategoryValue matchActivity(AmpActivityVersion a) {
        List<AmpPerformanceRule> rules = getPerformanceRules().stream()
                .filter(AmpPerformanceRule::getEnabled)
                .collect(Collectors.toList());

        return matchActivity(rules, a);
    }
    
    public AmpCategoryValue matchActivity(List<AmpPerformanceRule> rules, Long actId) {
        AmpActivityVersion a = (AmpActivityVersion) PersistenceManager.getSession()
                .get(AmpActivityVersion.class, actId);
        
        return matchActivity(rules, a);
    }
    

    public AmpCategoryValue matchActivity(List<AmpPerformanceRule> rules, AmpActivityVersion a) {

        AmpCategoryValue level = null;
        
        Map<String, PerformanceRuleMatcher> ruleMatcherMap = PerformanceRuleMatchers.RULE_TYPES.stream()
                .collect(Collectors.toMap(r -> r.getName(), r -> r));
        
        for (AmpPerformanceRule rule : rules) {
            PerformanceRuleMatcher matcher = ruleMatcherMap.get(rule.getTypeClassName());
            if (matcher != null) {
                level = getHigherLevel(level, matcher.match(rule, a));
            }
        }
        
        return level;
    }
    
    public AmpPerformanceRuleAttribute getAttributeFromRule(AmpPerformanceRule rule, String attributeName) {
        AmpPerformanceRuleAttribute monthAttribute = rule.getAttributes().stream().
                filter(attr -> attr.getName().equals(attributeName))
                .findAny().orElse(null);
        
        return monthAttribute;
    }
    
    public AmpCategoryValue getHigherLevel(AmpCategoryValue level1, AmpCategoryValue level2) {
        if (level1 == null) {
            return level2;
        } else if (level2 == null) {
            return level1;
        } else if (level1.getIndex() > level2.getIndex()) {
            return level2;
        }
        
        return level1;
    }
}
