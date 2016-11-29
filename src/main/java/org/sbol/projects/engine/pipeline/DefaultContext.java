package org.sbol.projects.engine.pipeline;

import java.util.Map;

import org.sbol.projects.engine.constants.Channel;

/**
 * Default context implementation.
 *
 * @author david.ralluy
 *
 * @param <T>
 */
public class DefaultContext<T> implements Context<T> {

    /**
     * Generic data.
     */
    private final T data;

    private Channel channel;

    private Map<String, Object> parameters;

    /**
     * Constructor.
     *
     * @param data
     *            Datos para el contexto en todos los canales.
     */
    public DefaultContext(final T data) {
        this(data, Channel.ALL);
    }

    /**
     * Constructor.
     *
     * @param data
     *            Datos para el contexto.
     * @param channel
     *            Canal en el cual introducir los datos.
     */
    public DefaultContext(final T data, final Channel channel) {
        this(data, channel, null);
    }

    /**
     * Constructor.
     *
     * @param data
     *            Datos para el contexto.
     * @param channel
     *            Canal en el cual introducir los datos.
     * @param parameters
     *            {@link Map} de parametros.
     */
    public DefaultContext(final T data, final Channel channel, final Map<String, Object> parameters) {
        this.data = data;
        this.channel = channel;
        this.parameters = parameters;
    }

    @Override
    public T getData() {
        return this.data;
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public Map<String, Object> getParameters() {
        return this.parameters;
    }

}
