package org.sbol.projects.engine.exceptions;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.sbol.projects.engine.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

/**
 * Logging general aspect. A pointcut must be defined for every case.
 *
 * @author david.ralluy
 *
 */
@Slf4j
public abstract class GeneralLoggingAspect {

    /**
     * Spring environment.
     */
    private Environment env;

    /**
     * Method definitions logging interception. In this case all methods of specified packages must be redefined in each module.
     */
    public abstract void loggingPointcut();

    /**
     * Captura de excepciones y envío a los logs.
     *
     * @param joinPoint
     *            Llamada
     * @param e
     *            Excepción
     */
    @AfterThrowing(pointcut = "loggingPointcut()", throwing = "e")
    public void logAfterThrowing(final JoinPoint joinPoint, final Throwable e) {
        if (this.env.acceptsProfiles(ApplicationConstants.PROFILE_DEVELOPMENT)) {
            GeneralLoggingAspect.log.error("Excepción en {}.{}() con causa = {}",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), e.getCause(),
                    e);
        } else {
            GeneralLoggingAspect.log.error("Excepción no controlada. Activar modo de desarrollo para visualizarla.");
        }
    }

    /**
     * En modo debug, mostramos las llamadas a cualquier método junto con los argumentos utilizados.
     *
     * @param joinPoint
     *            Llamada
     * @return Valor de la llamada
     * @throws Throwable
     *             Excepción general
     */
    @SuppressWarnings("static-method")
    @Around("loggingPointcut()")
    public Object logAround(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (GeneralLoggingAspect.log.isDebugEnabled()) {
            GeneralLoggingAspect.log.debug("Entrada: {}.{}() con argumentos = {}",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (GeneralLoggingAspect.log.isDebugEnabled()) {
                GeneralLoggingAspect.log.debug("Salida: {}.{}() con resultado = {}",
                        joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            GeneralLoggingAspect.log.error("Argumento ilegal: {} en {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }

    /**
     * Asignación el entorno de Spring.
     *
     * @param environment
     *            Entorno
     */
    @Autowired
    public void setEnvironment(final Environment environment) {
        this.env = environment;
    }

}
