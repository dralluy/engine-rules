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
        PrecioRebajadoRule rule = new PrecioRebajadoRule();
        CriterionRuleFactory<ProductCatalog> criterion = new CriterionRuleFactory<>();
        criterion.fromCriterion(rule.getRule(), ProductCatalog.class);
        StreamRule<ProductCatalog> streamRule = criterion.build(ProductCatalog.class);
        assertNotNull(streamRule);
    }

    /**
     * Rules parameters.
     */
    @Test
    public void checkRuleParametersTest() {
        PrecioRebajadoRule rule = new PrecioRebajadoRule();
        Map<String, Object> params = rule.getParameters();
        assertEquals(new Integer(RulesTest.PRICE_100), params.get("precio"));
    }

    /**
     * Update rules parameters.
     */
    @Test
    public void updateRuleParametersTest() {
        PrecioRebajadoRule rule = new PrecioRebajadoRule();
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
        PrecioRebajadoNoParametersRule rule = new PrecioRebajadoNoParametersRule();
        rule.updateParameters(null);
        Map<String, Object> params = rule.getParameters();
        assertNull(params);
    }

    /**
     * Test predicate rule.
     */
    @Test
    public void createRulesPredicateTest() {
        CriterionRuleFactory<ProductCatalog> criterion = new CriterionRuleFactory<>();
        criterion.fromPredicate(p -> p.getPrecio() > 0, ProductCatalog.class);
        StreamRule<ProductCatalog> streamRule = criterion.build(ProductCatalog.class);
        assertNotNull(streamRule);
    }

    /**
     * Test comparator rule.
     */
    @Test
    public void createRulesComparatorTest() {
        CriterionRuleFactory<ProductCatalog> criterion = new CriterionRuleFactory<>();
        criterion.fromComparator((p1, p2) -> p1.getPrecio() - p2.getPrecio(), ProductCatalog.class);
        StreamRule<ProductCatalog> streamRule = criterion.build(ProductCatalog.class);
        assertNotNull(streamRule);
    }

    /**
     * Clear rules.
     */
    @Test
    public void clearRulesTest() {
        CriterionRuleFactory<ProductCatalog> criterion = new CriterionRuleFactory<>();
        criterion.fromComparator((p1, p2) -> p1.getPrecio() - p2.getPrecio(), ProductCatalog.class);
        criterion.clearRules();
        assertTrue(criterion.getReglas().isEmpty());
    }
}
