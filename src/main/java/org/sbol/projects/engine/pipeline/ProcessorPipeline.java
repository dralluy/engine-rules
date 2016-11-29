package org.sbol.projects.engine.pipeline;

import java.util.List;

/**
 * Processor pipeline. A pipeline by definition runs a processing sequence. Every process gets applied an input
 * function,
 * and the resulting process goes to the output. This sequence gets repeated through the pipeline.
 *
 * @author dralluy
 *
 * @param <I>
 * @param <O>
 */
public class ProcessorPipeline<I, O> implements Processor<I, O> {

    private List<? extends Processor<?, ?>> processors;

    @SuppressWarnings("unchecked")
    @Override
    public O process(final I inputContext) {
        Object tmpResult = inputContext;
        for (Processor<?, ?> processor : this.processors) {
            tmpResult = this.internalProcessHelper(processor, tmpResult);
        }
        return (O) tmpResult;
    }

    /**
     * Workaround for compilation wildcard capture.
     *
     * See: http://docs.oracle.com/javase/tutorial/java/generics/capture.html
     *
     * @param processor
     * @param input
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> Object internalProcessHelper(final Processor<T, ?> processor, final Object input) {
        return processor.process((T) input);
    }

    @Override
    public void preProcess(final I inputContext) {
        //
    }

    @Override
    public void postProcess(final I inputContext) {
        //
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Asignar la lista de procesadores al pipeline.
     *
     * @param processors
     *            Lista de procesadores.
     */
    public void setProcessors(final List<? extends Processor<?, ?>> processors) {
        this.processors = processors;
    }

}
