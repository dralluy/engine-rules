package org.sbol.projects.engine.exceptions;

/**
 * Generic exception for engine rules.
 *
 * @author david.ralluy
 *
 */
public class EngineRuleException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * General onstructor.
     *
     * @param cause Cause exception
     */
    public EngineRuleException(final Throwable cause) {
        super(cause);
    }
}
