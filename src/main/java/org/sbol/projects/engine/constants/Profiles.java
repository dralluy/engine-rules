package org.sbol.projects.engine.constants;

/**
 * Profile definitions.
 *
 * @author david.ralluy
 *
 */
public final class Profiles {
    /**
     * Entorno de desarrollo local apuntando a recursos de test.
     */
    public static final String DEVELOPMENT = "dev";

    /**
     * Entorno de desarrollo local apuntando a recursos de docker en local.
     */
    public static final String DOCKERDEVELOPMENT = "docker";

    /**
     * Entorno para pruebas unitarias y de integracion, los recursos se crean en memoria.
     */
    public static final String CI = "ci";

    /**
     * Entorno de test.
     */
    public static final String TEST = "test";

    /**
     * Entorno de preproduccion.
     */
    public static final String PREPRODUCTION = "pre";

    /**
     * Entorno de produccion.
     */
    public static final String PRODUCTION = "pro";

    private Profiles() {
        //
    }

}
