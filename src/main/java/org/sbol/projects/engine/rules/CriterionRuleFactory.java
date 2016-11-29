package org.sbol.projects.engine.rules;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.sbol.projects.engine.constants.Channel;

/**
 * Builder para la construcción de reglas de negocio basadas en lambdas. Se ofrece la posibilidad de construir los
 * siguientes tipos:
 * - Filtros. Mediante el uso de Predicate. Útil para condiciones lógicas a aplicar a la lista.
 * - Comparadores. Medieante Comparator podemos ordenar el resultado.
 * - Rule. Cualquier función que procesa un Stream y devuelve otro. Para lógica de uso general.
 *
 * El resultado final se obtiene de la reducción de todas las reglas, aplicándose unas sobre otras, según el orden en
 * que se han añadido a la lista general.
 *
 * @author david.ralluy
 * @param <T>
 *            Tipo al que aplicar las reglas.
 *
 */
public final class CriterionRuleFactory<T> {

    private Map<Class<?>, RulesMap<T>> reglas = new HashMap<>();

    /**
     * Constructor por defecto.
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
     * Aplicar un filtro sobre la lista.
     *
     * @param predicate
     *            Predicado.
     * @param clazz
     *            Clase.
     * @return this
     */
    public CriterionRuleFactory<T> fromPredicate(final Predicate<T> predicate, final Class<?> clazz) {
        return this.fromPredicate(predicate, Channel.ALL, clazz);
    }

    /**
     * Aplicar un filtro sobre la lista para un canal concreto.
     *
     * @param predicate
     *            Predicado.
     * @param channel
     *            Canal.
     * @param clazz
     *            Clase.
     * @return this
     */
    public CriterionRuleFactory<T> fromPredicate(final Predicate<T> predicate, final Channel channel,
            final Class<?> clazz) {
        this.checkChannelCreated(channel, clazz);
        this.reglas.get(clazz).get(channel).add(p -> p.filter(predicate));
        return this;
    }

    /**
     * Aplicar una ordenacion sobre la lista.
     *
     * @param comparator
     *            Comparador.
     * @param clazz
     *            Clase.
     * @return this
     */
    public CriterionRuleFactory<T> fromComparator(final Comparator<T> comparator, final Class<?> clazz) {
        return this.fromComparator(comparator, Channel.ALL, clazz);
    }

    /**
     * Aplicar una ordenacion sobre la lista para un canal concreto.
     *
     * @param comparator
     *            Comparador.
     * @param channel
     *            Canal.
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
     * Aplicar una regla general de negocio sobre la lista.
     *
     * @param rule
     *            Regla.
     * @param clazz
     *            Clase.
     * @return this
     */
    public CriterionRuleFactory<T> fromCriterion(final StreamRule<T> rule, final Class<?> clazz) {
        return this.fromCriterion(rule, Channel.ALL, clazz);
    }

    /**
     * Aplicar una regla general de negocio sobre la lista para un canal concreto.
     *
     * @param rule
     *            Regla.
     * @param channel
     *            Canal.
     * @param clazz
     *            Clase.
     * @return this
     */
    public CriterionRuleFactory<T> fromCriterion(final StreamRule<T> rule, final Channel channel,
            final Class<?> clazz) {
        this.checkChannelCreated(channel, clazz);
        this.reglas.get(clazz).get(channel).add(rule);
        return this;
    }

    /**
     * Builder de la regla de negocio.
     *
     * @param clazz
     *            Clase.
     * @return Regla
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
     * Limpiar las reglas de negocio.
     */
    public void clearRules() {
        this.reglas.clear();
    }

    /**
     * Recuperar reglas.
     *
     * @return Map de reglas.
     */
    public Map<Class<?>, RulesMap<T>> getReglas() {
        return this.reglas;
    }

}
