package org.sbol.projects.engine.pipeline;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.sbol.projects.engine.constants.Channel;

/**
 * Processor test.
 *
 * @author david.ralluy
 *
 */
@RunWith(JUnit4.class)
public class ProcessorTest {

    private Processor<Context<String>, Context<String>> createProcessor() {
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
        Processor<Context<String>, Context<String>> processor = this.createProcessor();

        Context<String> context = new DefaultContext<>("hola");
        Context<String> contextResult = processor.process(context);
        assertEquals("holahola", contextResult.getData());
        assertEquals(Boolean.TRUE, Boolean.valueOf(processor.isEnabled()));
        assertEquals(0, processor.getOrder());
        assertEquals(0, processor.getPriority());
    }

    /**
     * La aplicación por defecto debe ser para todos los canales.
     */
    @Test
    public void processorChannelShouldBeAllTest() {
        Processor<Context<String>, Context<String>> processor = this.createProcessor();
        assertEquals(Arrays.asList(Channel.ALL), processor.getApplicationChannels());
    }
}
