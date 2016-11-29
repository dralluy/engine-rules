package org.sbol.projects.engine.rules;

import java.util.stream.Stream;

/**
 * Business rule definition with Java Streams.
 * It's a single operation that applies business logic to an items list stream, and returns another stream with
 * the modified items.
 *
 * @author david.ralluy
 * @param <T>
 *            Rule type
 *
 */
@FunctionalInterface
public interface StreamRule<T> {

    /**
     * Apply business logic to stream. We get the updated stream.
     *
     * @param stream Input stream
     * @return stream ouput stream
     */
    Stream<T> apply(final Stream<T> stream);

}
