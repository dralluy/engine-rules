package org.sbol.projects.engine.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sbol.projects.engine.pipeline.AbstractProcessor;
import org.sbol.projects.engine.pipeline.Processor;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.core.env.Environment;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

/**
 * Exceptions tests.
 *
 * @author david.ralluy
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionsTest {

    @Mock
    private Environment env;

    /**
     * Error definitions.
     */
    @Test
    public void checkErrorInfo() {
        ErrorInfo errorInfo = new ErrorInfo("url", "error");
        assertNull(errorInfo.getException());
        assertEquals("url", errorInfo.getUrl());
        assertEquals("error", errorInfo.getMessage());
    }

    /**
     * Logging aspect.
     */
    @Test
    public void checkLoggingAspect() {
        Processor<String, String> processor = new AbstractProcessor<String, String>() {

            @Override
            protected String internalProcess(final String context) {
                // TODO Auto-generated method stub
                return null;
            }
        };
        AspectJProxyFactory factory = new AspectJProxyFactory(processor);
        LoggingAspect aspect = new LoggingAspect();
        this.updateLoggerLevel();
        factory.addAspect(aspect);
        Processor<String, String> proxy = factory.getProxy();
        proxy.process("hola");
        assertNotNull(proxy);
    }

    /**
     * Loggin exception test.
     */
    @Test(expected = NumberFormatException.class)
    public void checkLoggingExceptionAspect() {
        Processor<String, String> processor = new AbstractProcessor<String, String>() {

            @Override
            protected String internalProcess(final String context) {
                throw new NumberFormatException();
            }
        };

        this.activateProfile(Boolean.TRUE);

        AspectJProxyFactory factory = new AspectJProxyFactory(processor);
        LoggingAspect aspect = new LoggingAspect();
        aspect.setEnvironment(this.env);
        this.updateLoggerLevel();
        factory.addAspect(aspect);
        Processor<String, String> proxy = factory.getProxy();
        proxy.process("hola");
    }

    /**
     * Test logging aspect.
     */
    @Test(expected = NumberFormatException.class)
    public void checkNoLoggingExceptionAspect() {
        Processor<String, String> processor = new AbstractProcessor<String, String>() {

            @Override
            protected String internalProcess(final String context) {
                throw new NumberFormatException();
            }
        };

        this.activateProfile(Boolean.FALSE);

        AspectJProxyFactory factory = new AspectJProxyFactory(processor);
        LoggingAspect aspect = new LoggingAspect();
        aspect.setEnvironment(this.env);
        this.updateLoggerLevel();
        factory.addAspect(aspect);
        Processor<String, String> proxy = factory.getProxy();
        proxy.process("hola");
    }

    private void activateProfile(final Boolean active) {
        when(this.env.acceptsProfiles(anyString())).thenReturn(active);
    }

    private void updateLoggerLevel() {
        LoggerContext ctx = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = ctx.getLogger("com.mango.fwk.exceptions");
        logger.setLevel(Level.DEBUG);
    }
}
