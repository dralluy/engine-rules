package org.sbol.projects.engine.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test rules creation.
 *
 * @author david.ralluy
 *
 */
@RunWith(JUnit4.class)
public class RulesTest {

    private static final int PRICE_200 = 200;
    private static final int PRICE_100 = 100;

    /**
     * Rules creation.
     */
    @Test
    public void createRulesTest() {
        DiscountedPriceRule rule = new DiscountedPriceRule();
        CriterionRuleFactory<ItemCollection> criterion = new CriterionRuleFactory<>();
        criterion.fromCriterion(rule.getRule(), ItemCollection.class);
        StreamRule<ItemCollection> streamRule = criterion.build(ItemCollection.class);
        assertNotNull(streamRule);
    }

    /**
     * Rules parameters.
     */
    @Test
    public void checkRuleParametersTest() {
        DiscountedPriceRule rule = new DiscountedPriceRule();
        Map<String, Object> params = rule.getParameters();
        assertEquals(new Integer(RulesTest.PRICE_100), params.get("precio"));
    }

    /**
     * Update rules parameters.
     */
    @Test
    public void updateRuleParametersTest() {
        DiscountedPriceRule rule = new DiscountedPriceRule();
        Map<String, Object> params = rule.getParameters();
        assertEquals(new Integer(RulesTest.PRICE_100), params.get("precio"));
        params.put("precio", new Integer(RulesTest.PRICE_200));
        rule.updateParameters(params);
        params = rule.getParameters();
        assertEquals(new Integer(RulesTest.PRICE_200), params.get("precio"));
    }

    /**
     * Update rules parameters with test.
     */
    @Test
    public void updateNullParametersTest() {
        DiscountedPriceNoParametersRule rule = new DiscountedPriceNoParametersRule();
        rule.updateParameters(null);
        Map<String, Object> params = rule.getParameters();
        assertNull(params);
    }

    /**
     * Test predicate rule.
     */
    @Test
    public void createRulesPredicateTest() {
        CriterionRuleFactory<ItemCollection> criterion = new CriterionRuleFactory<>();
        criterion.fromPredicate(p -> p.getPrecio() > 0, ItemCollection.class);
        StreamRule<ItemCollection> streamRule = criterion.build(ItemCollection.class);
        assertNotNull(streamRule);
    }

    /**
     * Test comparator rule.
     */
    @Test
    public void createRulesComparatorTest() {
        CriterionRuleFactory<ItemCollection> criterion = new CriterionRuleFactory<>();
        criterion.fromComparator((p1, p2) -> p1.getPrecio() - p2.getPrecio(), ItemCollection.class);
        StreamRule<ItemCollection> streamRule = criterion.build(ItemCollection.class);
        assertNotNull(streamRule);
    }

    /**
     * Clear rules.
     */
    @Test
    public void clearRulesTest() {
        CriterionRuleFactory<ItemCollection> criterion = new CriterionRuleFactory<>();
        criterion.fromComparator((p1, p2) -> p1.getPrecio() - p2.getPrecio(), ItemCollection.class);
        criterion.clearRules();
        assertTrue(criterion.getReglas().isEmpty());
    }
}
