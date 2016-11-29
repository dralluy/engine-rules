package org.sbol.projects.engine.rules.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rule annotation.
 *
 * @author david.ralluy
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Rule {

    /**
     * Class type for the rule.
     *
     * @return Class
     */
    @SuppressWarnings({ "rawtypes" })
    Class type();

}
