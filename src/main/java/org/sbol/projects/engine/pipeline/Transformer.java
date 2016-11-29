package org.sbol.projects.engine.pipeline;

import java.util.function.Function;

/**
 * Type transformation using a generic function.
 *
 * @author david.ralluy
 *
 * @param <I>
 * @param <O>
 */
@FunctionalInterface
public interface Transformer<I, O> {

    /**
     * Definición de la función de transformación de la entrada en salida.
     *
     * @return Función de transofrmación
     */
    Function<I, O> defineTransformation();

    /**
     * Transformación de la entrada a la salida.
     *
     * @param input
     *            Objeto a transformar.
     *
     * @return output Objeto resultado de la transformacion.
     */
    default O transform(final I input) {
        return this.defineTransformation().apply(input);
    }

}
