package org.sbol.projects.engine.exceptions;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Logging general para todas las llamadas a las capas de api, dominio y servicio.
 *
 * @author david.ralluy
 *
 */
@Aspect
public class LoggingAspect extends GeneralLoggingAspect {

    /**
     * Definición de los métodos a interceptar para logging. En este caso todos los métodos de los packages
     * especificados.
     */
    @Override
    @Pointcut("within(com.mango.fwk..*)")
    public void loggingPointcut() {
        // Solo necesario para definir el pointcut
    }

}
