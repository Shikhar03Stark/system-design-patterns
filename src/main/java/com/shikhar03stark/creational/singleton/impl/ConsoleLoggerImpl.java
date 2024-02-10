package com.shikhar03stark.creational.singleton.impl;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import com.shikhar03stark.creational.singleton.Logger;

public class ConsoleLoggerImpl implements Logger {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss.SSS");

    private String getCurrentFormattedTime() {
        final Instant currentInstant = Instant.now();
        final String time = dateTimeFormatter.format(currentInstant);
        return time;
    }
    
    @Override
    public void info(String message) {
        final String prefix = "[INFO]";
        final String time = getCurrentFormattedTime();
        System.out.println(String.format("%s %s %s", time, prefix, message));
    }

    @Override
    public void error(String message) {
        final String prefix = "[ERROR]";
        final String time = getCurrentFormattedTime();
        System.err.println(String.format("%s %s %s", time, prefix, message));
    }

    @Override
    public void debug(String message) {
        final String prefix = "[DEBUG]";
        final String time = getCurrentFormattedTime();
        System.out.println(String.format("%s %s %s", time, prefix, message));
    }
}
