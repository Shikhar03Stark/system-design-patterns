package com.shikhar03stark.creational.singleton.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.shikhar03stark.creational.singleton.Logger;

public class ConsoleLoggerImpl implements Logger {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");

    private String getCurrentFormattedTime() {
        final Instant currentInstant = Instant.now();
        final String time = LocalDateTime.ofInstant(currentInstant, ZoneId.systemDefault()).format(dateTimeFormatter);
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
