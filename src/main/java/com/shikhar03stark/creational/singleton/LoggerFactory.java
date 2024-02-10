package com.shikhar03stark.creational.singleton;

import java.util.Objects;
import java.util.concurrent.Semaphore;

import com.shikhar03stark.creational.singleton.impl.ConsoleLoggerImpl;

public class LoggerFactory {
    private static Logger loggerImpl;

    private static Semaphore createInstanceSemphore = new Semaphore(1);

    private LoggerFactory(){

    }

    public static Logger getConsoleInstance(){
        try {
            createInstanceSemphore.acquire();
            if(Objects.isNull(loggerImpl)){
                loggerImpl = new ConsoleLoggerImpl();
            }
            createInstanceSemphore.release();
            return loggerImpl;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not instanstiate logger");
        }
    }

    public static Logger getInstance(){
        return getConsoleInstance();
    }
}
