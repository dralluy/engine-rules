package org.sbol.projects.engine.pipeline;

import java.util.Arrays;
import java.util.List;

import org.sbol.projects.engine.constants.Channel;

/**
 * Processing unit with an input context and an output context.
 * The processor business logic makes this transformation.
 *
 * @author david.ralluy
 *
 * @param <I>
 *            Input context
 * @param <O>
 *            Output context
 */
public interface Processor<I, O> {

    /**
     * Ejecución del servicio.
     *
     * @param inputContext
     *            Valor a procesar
     * @return outputContext Valor procesado
     */
    O process(final I inputContext);

    /**
     * Ejecucion anterior al procesado.
     *
     * @param inputContext
     *            context
     */
    void preProcess(final I inputContext);

    /**
     * Ejecucion posterior al procesado.
     *
     * @param inputContext
     *            context
     */
    void postProcess(final I inputContext);

    /**
     * Indicador de procesador activo.
     *
     * @return Activo S/N
     */
    boolean isEnabled();

    /**
     * Prioridad de ejecución del procesador (0 -> Máxima prioridad. Por defecto).
     *
     * @return Prioridad
     */
    int getPriority();

    /**
     * Orden de ejecución dentro de la misma prioridad (0 > 1 > 2 .... > MAX).
     *
     * @return Orden
     */
    int getOrder();

    /**
     * Nombre del procesador.
     *
     * @return Nombre
     *
     */
    String getName();

    /**
     * Lista de canales de aplicación para el procesador. Por defecto es de aplicación global.
     *
     * @return Lista de canales.
     */
    default List<Channel> getApplicationChannels() {
        return Arrays.asList(Channel.ALL);
    }

}
