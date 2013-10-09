package com.messaggi.junit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class MockAppender extends AppenderSkeleton
{
    private final List<LoggingEvent> logEvents = Collections.synchronizedList(new ArrayList<LoggingEvent>());

    @Override
    protected void append(LoggingEvent logEvent)
    {
        logEvents.add(logEvent);
    }

    @Override
    public void close()
    {
    }

    @Override
    public boolean requiresLayout()
    {
        return false;
    }

    public void clearLogEvents()
    {
        logEvents.clear();
    }

    public List<LoggingEvent> getLogEvents()
    {
        synchronized (logEvents) {
            return new ArrayList<>(logEvents);
        }
    }

    public int getLogEventCount()
    {
        synchronized (logEvents) {
            return logEvents.size();
        }
    }

    public String getLogAsString()
    {
        StringBuilder log = new StringBuilder(">>>LOG START. ");
        synchronized (logEvents) {
            log.append(logEvents.size()).append(" messages total.\n");
            int messageCounter = 0;
            for (LoggingEvent message : logEvents) {
                messageCounter++;
                // COUNTER - CATEGORY - LEVEL: MESSAGE
                log.append(messageCounter).append(" - ").append(message.getLoggerName()).append(" - ");
                log.append(message.getLevel()).append(": ").append(message.getRenderedMessage()).append("\n");
            }
        }
        log.append("<<<LOG END");
        return log.toString();
    }
}

