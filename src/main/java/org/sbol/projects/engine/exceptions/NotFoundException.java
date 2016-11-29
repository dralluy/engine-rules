package org.sbol.projects.engine.exceptions;

/**
 * Resource not found exception.
 *
 * @author david.ralluy
 *
 */
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 2058435682131291270L;

    /**
     * Construye una excepcion que indica que una entidad no se ha encontrado junto con un mensaje descriptivo del
     * error.
     *
     * @param message
     *            Detalle del error. Se puede recuperar mediante el metodo {@link #getMessage()}.
     */
    public NotFoundException(final String message) {
        super(message);
    }

}
