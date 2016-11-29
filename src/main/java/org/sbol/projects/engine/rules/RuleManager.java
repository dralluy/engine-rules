package org.sbol.projects.engine.rules;

import java.util.List;
import java.util.Map;

import org.sbol.projects.engine.constants.Channel;

/**
 * Definición de un motor de ejecución de reglas sobre una lista de productos.
 * Habrá diversas implementaciones: Streams, Motor de búsquedas y BBDD.
 *
 * @param <P> Object type
 *
 * @author dralluy
 *
 */
public interface RuleManager<P> {

    /**
     * Rules execution.
     * @param items Items list to process
     * @param channel Channel
     * @return Items list updated
     */
    List<P> executeRules(final List<P> items, final Channel channel);

    /**
     * Load rules from persistence system.
     * @param rules Rules
     */
    void loadRules(final String rules);

    /**
     * Remove rule from execution.
     * @param ruleName Rule name
     */
    void removeRule(final String ruleName);

    /**
     * Add rule dynamically to exection.
     * @param ruleCollection Rule collection
     * @param ruleName Rule name
     */
    void addRule(final String ruleCollection, final String ruleName);

    /**
     * Rule activation.
     * @param ruleCollection Rule collection
     * @param ruleName Rule name
     * @param active Rule active
     */
    void activateRule(final String ruleCollection, final String ruleName, final boolean active);

    /**
     * Update rule priorit.
     * @param ruleCollection Rule collection
     * @param ruleName Rule name
     * @param priority Rule priority
     */
    void updateRulePriority(final String ruleCollection, final String ruleName, final int priority);

    /**
     * Update rule parameters.
     * @param ruleCollection Rule collection
     * @param ruleName Rule name
     * @param parameters Rule parameters
     */
    void updateRule(final String ruleCollection, final String ruleName, final Map<String, Object> parameters);

    /**
     * Clear all rules.
     */
    void clearRules();

    /**
     * Get rules in execution.
     * @return Rules
     */
    Map<String, BusinessRule<P>> getRules();

}
