package org.sbol.projects.engine.constants;

/**
 * Application constants for general use.
 *
 * @author david.ralluy
 *
 */
public final class ApplicationConstants {

    /**
     * Development profile.
     */
    public static final String PROFILE_DEVELOPMENT = "default";

    /**
     * Integration profile.
     */
    public static final String PROFILE_INTEGRATION = "integration";

    /**
     * Production profile.
     */
    public static final String PROFILE_PRODUCTION = "production";

    /**
     * OK response.
     */
    public static final int HTTP_OK = 200; // HttpStatus.OK

    /**
     * CREATED repsonse.
     */
    public static final int HTTP_CREATED = 201; // HttpStatus.CREATED

    /**
     * NOT FOUND response.
     */
    public static final int HTTP_NOT_FOUND = 404; // HttpStatus.NOT_FOUND

    /**
     * Internal error repsonse.
     */
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500; // HttpStatus.INTERNAL_SERVER_ERROR

    private ApplicationConstants() {
    }
}
