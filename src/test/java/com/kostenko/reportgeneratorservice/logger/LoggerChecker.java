package com.kostenko.reportgeneratorservice.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import lombok.experimental.UtilityClass;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UtilityClass
public class LoggerChecker {
    public void checkLog(Iterator<ILoggingEvent> eventIterator, Level expectedLevel, String expectedMessage) {
        assertTrue(eventIterator.hasNext());
        ILoggingEvent iLoggingEvent = eventIterator.next();

        assertEquals(expectedLevel, iLoggingEvent.getLevel());
        assertEquals(expectedMessage, iLoggingEvent.getMessage());
    }

    public void checkErrorLog(Iterator<ILoggingEvent> eventIterator, String expectedMessage, Exception expectedException) {
        assertTrue(eventIterator.hasNext());
        ILoggingEvent iLoggingEvent = eventIterator.next();

        assertEquals(Level.ERROR, iLoggingEvent.getLevel());
        assertEquals(expectedMessage, iLoggingEvent.getMessage());

        IThrowableProxy iThrowableProxy = iLoggingEvent.getThrowableProxy();
        assertTrue(iThrowableProxy instanceof ThrowableProxy);
        assertEquals(expectedException, ((ThrowableProxy) iThrowableProxy).getThrowable());
    }
}
