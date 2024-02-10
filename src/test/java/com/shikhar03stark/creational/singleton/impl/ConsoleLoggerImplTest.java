package com.shikhar03stark.creational.singleton.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.shikhar03stark.creational.singleton.Logger;
import com.shikhar03stark.creational.singleton.LoggerFactory;


public class ConsoleLoggerImplTest {

    @Test
    public void CheckSingletonPatternTest(){
        final Logger logger = LoggerFactory.getInstance();
        final Logger logger2 = LoggerFactory.getInstance();

        // Same instance
        Assertions.assertEquals(logger, logger2);
    }

    @Test
    public void CheckThreadSafeSingletonPatternTest() throws InterruptedException, ExecutionException, TimeoutException{
        final int numOfThreads = 8;
        final ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        List<Callable<Logger>> callables = new ArrayList<>();
        for(int thread = 0; thread<numOfThreads; thread++){
            callables.add(() -> {
                return LoggerFactory.getInstance();
            });
        }

        try{
            List<Future<Logger>> futures = executorService.invokeAll(callables);
            for(int t = 0; t<numOfThreads-1; t++){
                // Check if t and t+1 are same instance
                Assertions.assertEquals(futures.get(t).get(2, TimeUnit.SECONDS), futures.get(t+1).get(2, TimeUnit.SECONDS));
            }
        } catch (ExecutionException | TimeoutException e){
            e.printStackTrace();
        } finally {
            executorService.shutdownNow();
        }


    }


}
