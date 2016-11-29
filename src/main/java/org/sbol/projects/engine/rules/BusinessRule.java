package org.sbol.projects.engine.rules;

import java.util.Map;
import java.util.Map.Entry;

import org.sbol.projects.engine.constants.Channel;
import org.sbol.projects.engine.pipeline.AbstractProcessor;
import org.sbol.projects.engine.pipeline.Transformer;

import lombok.Getter;
import lombok.Setter;

/**
 * Business rule abstraction. It defines the lambda rule for business modeling logic and aplication channel.
 * The engine rules applies all the business rules using composing streams.
 *
 * @author dralluy
 *
 * @param <T>
 *
 */
public abstract class BusinessRule<T> extends AbstractProcessor<T, T> implements Transformer<T, T> {

    private StreamRule<T> rule;

    private Map<String, Object> parameters;

    @Getter
    @Setter
    private Channel channel = Channel.ALL;

    /**
     * Constructor de una regla con solo un nombre y activa por defecto.
     *
     * @param name
     *            Nombre
     */
    public BusinessRule(final String name) {
        this(name, true);
    }

    /**
     * Constructor de una regla con un nombre, activa y prioridad 1.
     *
     * @param name
     *            Nombre
     * @param enabled
     *            Activación
     */
    public BusinessRule(final String name, final boolean enabled) {
        this(name, enabled, 1);
    }

    /**
     * Constructor de una regla con un nombre, activa, prioridad 1 y de afectación a todos los canales.
     *
     * @param name
     *            Nombre
     * @param enabled
     *            Activación
     * @param priority
     *            Prioridad
     */
    public BusinessRule(final String name, final boolean enabled, final int priority) {
        this.setName(name);
        this.setEnabled(enabled);
        this.setPriority(priority);
        this.parameters = this.defineParameters();
        this.setRule(this.defineRule());
    }

    /**
     * Definición de la regla de negocio. Se trata de una función que se aplica sobre un Stream, y cuyo resultado es el
     * mismo Stream modificado.
     *
     * @return Regla de negocio
     */
    public abstract StreamRule<T> defineRule();

    /**
     * Definición de los parámetros asociados a la regla.
     *
     * @return Mapeo de propiedades
     */
    public abstract Map<String, Object> defineParameters();

    /**
     * Obtención de los parámetros.
     *
     * @return Parámetros
     */
    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    /**
     * Actualización de los parámetros de la regla.
     *
     * @param lParameters
     *            {@link Map} de parametros.
     */
    public void updateParameters(final Map<String, Object> lParameters) {
        if (this.parameters != null) {
            for (Entry<String, Object> parameter : this.parameters.entrySet()) {
                if (lParameters.containsKey(parameter.getKey())) {
                    this.parameters.put(parameter.getKey(), lParameters.get(parameter.getKey()));
                }
            }
        }
    }

    /**
     * Getter de la regla.
     *
     * @return Regla
     */
    public StreamRule<T> getRule() {
        return this.rule;
    }

    /**
     * Setter de la regla.
     *
     * @param rule
     *            Regla a setear.
     */
    public void setRule(final StreamRule<T> rule) {
        this.rule = rule;
    }

}
