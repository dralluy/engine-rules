package org.sbol.projects.engine.pipeline;

/**
 * Default processor implementation.
 *
 * @author david.ralluy
 *
 * @param <I> Input type
 * @param <O> Output type
 */
public abstract class AbstractProcessor<I, O> implements Processor<I, O> {

    /**
     * Processor name.
     */
    private String name = this.getClass().getSimpleName();

    /**
     * Processor activation.
     */
    private boolean enabled = true;

    /**
     * Execution priority.
     */
    private int priority;

    /**
     * Execution order with same priority.
     */
    private int order;

    @Override
    public O process(final I context) {
        this.preProcess(context);
        O output = this.internalProcess(context);
        this.postProcess(context);
        return output;
    }

    /**
     * Business logic implementation.
     *
     * @param context
     * @return context
     */
    protected abstract O internalProcess(final I context);

    @Override
    public void preProcess(final I inputContext) {
        // Sin proceso previo
    }

    @Override
    public void postProcess(final I inputContext) {
        // Sin proceso posterior
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Dar nombre a la regla.
     *
     * @param name
     *            Nombre
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Fijar el orden de ejecición.
     *
     * @param order
     *            Orden
     */
    public void setOrder(final int order) {
        this.order = order;
    }

    /**
     * Fijar la prioridad de la regla.
     *
     * @param priority
     *            Prioridad
     */
    public void setPriority(final int priority) {
        this.priority = priority;
    }

    /**
     * Activar o desactivar la regla.
     *
     * @param enabled
     *            Activación
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
