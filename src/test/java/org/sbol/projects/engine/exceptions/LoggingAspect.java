package org.sbol.projects.engine.exceptions;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * General logging aspect.
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
    @Pointcut("within(org.sbol.projects.engine..*)")
    public void loggingPointcut() {
        // Solo necesario para definir el pointcut
    }

}
