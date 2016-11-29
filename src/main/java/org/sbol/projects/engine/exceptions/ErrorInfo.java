package org.sbol.projects.engine.exceptions;

/**
 * Error general information.
 *
 * @author david.ralluy
 *
 */
public class ErrorInfo {

    private String url;
    private String message;
    private Exception exception;

    /**
     * Constructor.
     *
     * @param url
     *            url error
     * @param message
     *            message
     */
    public ErrorInfo(final String url, final String message) {
        this(url, message, null);
    }

    /**
     * Constructor.
     *
     * @param url
     *            URL que ha provocado el error.
     * @param message
     *            Mensaje de error.
     * @param ex
     *            Excepcion asociada al error.
     */
    public ErrorInfo(final String url, final String message, final Exception ex) {
        this.setUrl(url);
        this.setMessage(message);
        this.setException(ex);
    }

    /**
     * Recupera la URL que ha provocado el error.
     *
     * @return url URL.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Establece el valor de la URL que ha provocado el error.
     *
     * @param url
     *            URL.
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * Recupera el mensaje del error.
     *
     * @return mensaje Mensaje de error.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Establece el mensaje de error.
     *
     * @param message
     *            Mensaje de error.
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Recupera la excepcion que ha provocado el error.
     *
     * @return Exception
     */
    public Exception getException() {
        return this.exception;
    }

    /**
     * Establece la excepcion que ha provocado el error.
     *
     * @param exception
     *            Excepcion.
     */
    public void setException(final Exception exception) {
        this.exception = exception;
    }

}
