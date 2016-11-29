package org.sbol.projects.engine.pipeline;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test processor pipeline.
 *
 * @author david.ralluy
 *
 */
@RunWith(JUnit4.class)
public class ProcessorPipelineTest {

    private Processor<Context<String>, Context<String>> createFirstProcessor() {
        return new AbstractProcessor<Context<String>, Context<String>>() {

            @Override
            public void preProcess(final Context<String> inputContext) {
                // TODO Auto-generated method stub

            }

            @Override
            public void postProcess(final Context<String> inputContext) {
                // TODO Auto-generated method stub

            }

            @Override
            protected Context<String> internalProcess(final Context<String> context) {
                return new DefaultContext<>(context.getData() + context.getData());
            }

        };
    }

    private Processor<Context<String>, Context<String>> createSecondProcessor() {
        return new AbstractProcessor<Context<String>, Context<String>>() {

            @Override
            public void preProcess(final Context<String> inputContext) {
                // TODO Auto-generated method stub

            }

            @Override
            public void postProcess(final Context<String> inputContext) {
                // TODO Auto-generated method stub

            }

            @Override
            protected Context<String> internalProcess(final Context<String> context) {
                return new DefaultContext<>(context.getData() + context.getData());
            }

        };
    }

    /**
     * Test processor composition.
     */
    @Test
    public void generateOutputAsConcatOfInputTest() {
        List<Processor<Context<String>, Context<String>>> processors = Arrays.asList(this.createFirstProcessor(),
                this.createSecondProcessor());
        ProcessorPipeline<Context<String>, Context<String>> pipeline = new ProcessorPipeline<>();
        pipeline.setProcessors(processors);
        Context<String> context = new DefaultContext<>("1");
        context = pipeline.process(context);

        assertEquals("1111", context.getData());
        assertEquals(Boolean.TRUE, Boolean.valueOf(pipeline.isEnabled()));
        assertEquals(0, pipeline.getOrder());
        assertEquals(0, pipeline.getPriority());
    }

}
