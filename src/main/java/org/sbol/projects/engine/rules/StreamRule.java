package org.sbol.projects.engine.rules;

import java.util.stream.Stream;

/**
 * Definición de una regla de negocio con Java Streams. Define una única operación mediante la cual se aplica cierta
 * lógica de negocio a un Stream de productos y se devuelve otro Stream de productos modificados por la regla.
 * Este interface funcional equivale al uso de UnaryOperator.
 *
 * @author david.ralluy
 * @param <T>
 *            Tipo del Stream
 *
 */
@FunctionalInterface
public interface StreamRule<T> {

    /**
     * Lógica de negocio aplicada sobre un Stream. Se devuelve el Stream modificado.
     *
     * @param stream Stream sobre el que aplicar las reglas.
     * @return stream
     */
    Stream<T> apply(final Stream<T> stream);

}
