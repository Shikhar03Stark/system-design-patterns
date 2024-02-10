package com.shikhar03stark.creational.singleton.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

    @Test
    public void CheckLoggingPrefixTest(){
        final PrintStream originalOutput = System.out;
        final PrintStream originalError = System.err;

        final ByteArrayOutputStream spyOutput = new ByteArrayOutputStream();
        final ByteArrayOutputStream spyError = new ByteArrayOutputStream();

        final String testMessage = "Hello fr0m t3st";

        try{

            System.setOut(new PrintStream(spyOutput));
            System.setErr(new PrintStream(spyError));

            final Logger logger = LoggerFactory.getInstance();

            spyOutput.flush();
            logger.info(testMessage);
            Assertions.assertTrue(spyOutput.toString().contains("[INFO]"));
            Assertions.assertTrue(spyOutput.toString().contains(testMessage));

            spyOutput.flush();
            logger.debug(testMessage);
            Assertions.assertTrue(spyOutput.toString().contains("[DEBUG]"));
            Assertions.assertTrue(spyOutput.toString().contains(testMessage));

            spyError.flush();
            logger.error(testMessage);
            Assertions.assertTrue(spyError.toString().contains("[ERROR]"));
            Assertions.assertTrue(spyError.toString().contains(testMessage));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.setOut(originalOutput);
            System.setErr(originalError);;
        }

    }

    @Test
    public void playgroundTest(){
        final Logger log = LoggerFactory.getInstance();

        log.info("Hello World");

        for(int i = 0; i<5; i++){
            log.info(String.format("Hello from iteration %s", i));
        }

        for(int i = 0; i<5; i++){
            final int itr = i;
            Thread t = new Thread(() -> {
                final Logger logger = LoggerFactory.getInstance();
                logger.debug(String.format("Hey from parallel thread t=%s", itr));
            });
            t.start();
        }
    }

}
