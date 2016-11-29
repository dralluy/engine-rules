package org.sbol.projects.engine.rules;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.sbol.projects.engine.constants.Channel;

/**
 * Builder for lambda based business rules. We can build the following types:
 * - Filters: Using Predicate. Very useful for logical conditions applied to lists.
 * - Comparators: Using Comparator we can order the result.
 * - Rule. Any function processing a stream and returning another. For general business logic,
 *
 * The final result is got from an all rules reduction composition.
 *
 * @author david.ralluy
 * @param <T>
 *            Item type for rules application.
 *
 */
public final class CriterionRuleFactory<T> {

    private Map<Class<?>, RulesMap<T>> reglas = new HashMap<>();

    /**
     * Default constructor.
     */
    public CriterionRuleFactory() {
        //
    }

    private void checkChannelCreated(final Channel channel, final Class<?> clazz) {
        if (this.reglas.get(clazz) == null) {
            this.reglas.put(clazz, new RulesMap<T>());
        }
        if (this.reglas.get(clazz).get(channel) == null) {
            this.reglas.get(clazz).put(channel, new ArrayList<StreamRule<T>>());
        }
    }

    /**
     * Apply a filter to a list.
     *
     * @param predicate
     *            Predicate.
     * @param clazz
     *            Class.
     * @return this
     */
    public CriterionRuleFactory<T> fromPredicate(final Predicate<T> predicate, final Class<?> clazz) {
        return this.fromPredicate(predicate, Channel.ALL, clazz);
    }

    /**
     * Apply a filter to a list in a specific channel.
     *
     * @param predicate
     *            Predicate.
     * @param channel
     *            Channel.
     * @param clazz
     *            Class.
     * @return this
     */
    public CriterionRuleFactory<T> fromPredicate(final Predicate<T> predicate, final Channel channel,
            final Class<?> clazz) {
        this.checkChannelCreated(channel, clazz);
        this.reglas.get(clazz).get(channel).add(p -> p.filter(predicate));
        return this;
    }

    /**
     * Apply an order to a list.
     *
     * @param comparator
     *            Comparator.
     * @param clazz
     *            Class.
     * @return this
     */
    public CriterionRuleFactory<T> fromComparator(final Comparator<T> comparator, final Class<?> clazz) {
        return this.fromComparator(comparator, Channel.ALL, clazz);
    }

    /**
     * Apply an order to a list in a specific channel.
     *
     * @param comparator
     *            Comparator.
     * @param channel
     *            Channel.
     * @param clazz
     *            Clase.
     * @return this
     */
    public CriterionRuleFactory<T> fromComparator(final Comparator<T> comparator, final Channel channel,
            final Class<?> clazz) {
        this.checkChannelCreated(channel, clazz);
        this.reglas.get(clazz).get(channel).add(p -> p.sorted(comparator));
        return this;
    }

    /**
     * Apply a general business rule to a stream.
     *
     * @param rule
     *            Rule.
     * @param clazz
     *            Class.
     * @return this
     */
    public CriterionRuleFactory<T> fromCriterion(final StreamRule<T> rule, final Class<?> clazz) {
        return this.fromCriterion(rule, Channel.ALL, clazz);
    }

    /**
     * Apply a general business rule to a stream in a specific channel.
     *
     * @param rule
     *            Rule.
     * @param channel
     *            Channel.
     * @param clazz
     *            Class.
     * @return this
     */
    public CriterionRuleFactory<T> fromCriterion(final StreamRule<T> rule, final Channel channel,
            final Class<?> clazz) {
        this.checkChannelCreated(channel, clazz);
        this.reglas.get(clazz).get(channel).add(rule);
        return this;
    }

    /**
     * Business rule builder.
     *
     * @param clazz
     *            Class.
     * @return Stream of rules
     */
    public StreamRule<T> build(final Class<?> clazz) {
        return this.build(Channel.ALL, clazz);
    }

    /**
     * Recorrido sobre todas las reglas de negocio, y aplicación de unas sobre otras según el orden de inserción.
     * Función reduce. Tiene dos parámetros: identidad y acumulador
     * Parámetro identidad: Valor inicial de la reducción y resultado por defecto si no hay elementos (reglas) en el
     * stream. Es decir,
     * es la lista original de productos devueltos en la consulta, a la que se deben aplicar las reglas.
     * Parámetro acumulador: Es una función con dos parámetros: Un resultado parcial de la acumulación y el siguiente
     * elemento del stream.
     * Este acumulador es un lambda cuya aplicación es un Stream<ProductTO> resultado de aplicar una regla sobre otra
     * regla.
     *
     * @param channel
     *            Canal.
     * @param clazz
     *            Clase.
     *
     * @return Regla compuesta
     */
    public StreamRule<T> build(final Channel channel, final Class<?> clazz) {
        //@formatter:off
        return
            this
                .reglas
                .get(clazz)
                .get(channel)
                .stream()
                .reduce(
                    (final Stream<T> r) -> r,
                    (final StreamRule<T> r1, final StreamRule<T> r2) -> (final Stream<T> s) -> r2.apply(r1.apply(s))
                );
        //@formatter:on
    }

    /**
     * Clearing business rules.
     */
    public void clearRules() {
        this.reglas.clear();
    }

    /**
     * Get rules.
     *
     * @return Rules Map.
     */
    public Map<Class<?>, RulesMap<T>> getReglas() {
        return this.reglas;
    }

}
