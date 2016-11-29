package org.sbol.projects.engine.pipeline;

import java.util.Map;

import org.sbol.projects.engine.constants.Channel;

/**
 * Execution context for the engine rules.
 *
 * @author david.ralluy
 *
 * @param <T>
 */
public interface Context<T> {

    /**
     * Obtención de los datos almacenado en el contexto.
     *
     * @return Dato
     */
    T getData();

    /**
     * Canal de ejecución.
     *
     * @return Canal
     */
    Channel getChannel();

    /**
     * Parámetros del contexto.
     *
     * @return Parámetros
     */
    Map<String, Object> getParameters();

}
