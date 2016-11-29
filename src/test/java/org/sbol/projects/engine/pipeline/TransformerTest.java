package org.sbol.projects.engine.pipeline;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Test;

/**
 * Transformer tests.
 *
 * @author david.ralluy
 *
 */
public class TransformerTest {
    private static final int INPUT_LENGTH = 4;

    private Transformer<String, Integer> createTransformer() {
        return new Transformer<String, Integer>() {

            @SuppressWarnings("boxing")
            @Override
            public Function<String, Integer> defineTransformation() {
                return s -> s.length();
            }
        };
    }

    /**
     * Test input length.
     */
    @Test
    public void countInputLengthTest() {
        Transformer<String, Integer> processor = this.createTransformer();
        String input = "hola";
        Integer result = processor.transform(input);
        assertEquals(INPUT_LENGTH, result.intValue());

    }
}
