package com.shikhar03stark.behavioural.observer.stock.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.shikhar03stark.behavioural.observer.stock.ExchangeClientListener;
import com.shikhar03stark.behavioural.observer.stock.StockDataObserver;
import com.shikhar03stark.creational.singleton.Logger;
import com.shikhar03stark.creational.singleton.LoggerFactory;

public class WatchlistTrackerTest {

    private final Logger logger = LoggerFactory.getInstance();

    public enum STOCKS {
        ITC("ITC"), TATACONSUM("TATACONSUM"), NESTLEIND("NESTLEIND"), HINDUNILVR("HINDUNILVR"), BRITANNIA("BRITANNIA"),
        ICICIBANK("ICICIBANK"), HDFC("HDFC"), AXISBANK("AXISBANK"), 
        NTPC("NTPC"), ONGC("ONGC"), IRCTC("IRCTC");
        private String name;
        STOCKS(String name){
            this.name = name;
        }

        public String value(){
            return this.name;
        }
    }

    private double generatePrice(Double previousPrice){
        // Initial price set between 100-1000
        if(Objects.isNull(previousPrice)){
            return 100 + (1000-100)*Math.random();
        }

        // Randomly change the price by 5
        return previousPrice + (10*Math.random() - 5);
    }

    @Test
    public void observerPlaygroundTest(){
        final StockDataObserver stockDataObserver = new ZerodhaStockDataObserverImpl();
        final ExchangeClientListener nseStockListener = new NseExchangeClientListenerImpl(stockDataObserver);
        final WatchlistTracker fmcgTracker = new WatchlistTracker(stockDataObserver, "FMCG Tracker", STOCKS.ITC.name(), STOCKS.HINDUNILVR.name(), STOCKS.BRITANNIA.name(), STOCKS.IRCTC.name());
        final WatchlistTracker bankTracker = new WatchlistTracker(stockDataObserver, "BANK Tracker", STOCKS.AXISBANK.name(), STOCKS.HDFC.name(), STOCKS.IRCTC.name());

        final List<String> allStocks = Arrays.stream(STOCKS.values()).map(STOCKS::name).toList();
        final Map<String, Double> price = new HashMap<>();

        allStocks.forEach(stock -> price.put(stock, generatePrice(null)));

        for(int i = 0; i < 10; i++){
            allStocks.forEach(stock -> {
                price.put(stock, generatePrice(price.get(stock)));
                nseStockListener.onPriceChange(stock, price.get(stock));
                logger.info("Price of " + stock + " updated to " + price.get(stock));
            });

            // Assert that the price is updated in the tracker
            fmcgTracker.getStockSymbols().forEach(stock -> {
                final List<WatchlistTracker.WatchlistTrackerPrice> prices = fmcgTracker.getStockPriceMap().get(stock);
                final WatchlistTracker.WatchlistTrackerPrice lastPrice = prices.get(prices.size()-1);
                Assertions.assertTrue(Math.abs(price.get(stock) - lastPrice.getPrice()) < 0.01);
            });

            bankTracker.getStockSymbols().forEach(stock -> {
                final List<WatchlistTracker.WatchlistTrackerPrice> prices = bankTracker.getStockPriceMap().get(stock);
                final WatchlistTracker.WatchlistTrackerPrice lastPrice = prices.get(prices.size()-1);
                Assertions.assertTrue(Math.abs(price.get(stock) - lastPrice.getPrice()) < 0.01);
            });
        }
    }

    @Test
    public void verifyPriceUpdateFromClientListernerTest(){
        final StockDataObserver stockDataObserver = Mockito.spy(new ZerodhaStockDataObserverImpl());
        final ExchangeClientListener nseStockListener = new NseExchangeClientListenerImpl(stockDataObserver);
        
        final List<String> allStocks = Arrays.stream(STOCKS.values()).map(STOCKS::name).toList();
        final Map<String, Double> price = new HashMap<>();

        allStocks.forEach(stock -> price.put(stock, generatePrice(null)));

        // Track for ITC price
        for(int i = 0; i<10; i++){
            price.put(STOCKS.ITC.name(), generatePrice(price.get(STOCKS.ITC.name())));
            logger.info("Price of " + STOCKS.ITC.name() + " updated to " + price.get(STOCKS.ITC.name()));
            nseStockListener.onPriceChange(STOCKS.ITC.name(), price.get(STOCKS.ITC.name()));

            Mockito.verify(stockDataObserver, Mockito.times(1)).notifyListeners(STOCKS.ITC.name(), price.get(STOCKS.ITC.name()));
        }
    }

    @Test
    public void verifyPartialSubscriberStockWatchlistTest(){
        final StockDataObserver stockDataObserver = new ZerodhaStockDataObserverImpl();
        final WatchlistTracker fmcgTracker = new WatchlistTracker(stockDataObserver, "FMCG Tracker", STOCKS.ITC.name(), STOCKS.HINDUNILVR.name(), STOCKS.BRITANNIA.name());

        stockDataObserver.notifyListeners(STOCKS.ITC.name(), 100);
        logger.info("Price of " + STOCKS.ITC.name() + " updated to 100");
        logger.info("FMCG Tracker price for " + STOCKS.ITC.name() + " is " + fmcgTracker.getStockPriceMap().get(STOCKS.ITC.name()));
        Assertions.assertEquals(100, fmcgTracker.getStockPriceMap().get(STOCKS.ITC.name()).get(0).getPrice());

        stockDataObserver.unsubscribe(fmcgTracker, Collections.singletonList(STOCKS.BRITANNIA.name()));
        logger.info("FMCG unsubscribed BRITANNIA stock");
        
        stockDataObserver.notifyListeners(STOCKS.BRITANNIA.name(), 120);
        logger.info("Price of " + STOCKS.BRITANNIA.name() + " updated to 120");
        logger.info("FMCG Tracker price for " + STOCKS.BRITANNIA.name() + " is " + fmcgTracker.getStockPriceMap().get(STOCKS.BRITANNIA.name()));
        Assertions.assertTrue(fmcgTracker.getStockPriceMap().get(STOCKS.BRITANNIA.name()).isEmpty());

        stockDataObserver.notifyListeners(STOCKS.ITC.name(), 107);
        logger.info("Price of " + STOCKS.ITC.name() + " updated to 107");
        logger.info("FMCG Tracker price for " + STOCKS.ITC.name() + " is " + fmcgTracker.getStockPriceMap().get(STOCKS.ITC.name()));
        Assertions.assertEquals(107, fmcgTracker.getStockPriceMap().get(STOCKS.ITC.name()).get(1).getPrice());
    }

    @Test
    public void verifyOneToManyPriceUpdateTest(){
        final StockDataObserver stockDataObserver = new ZerodhaStockDataObserverImpl();
        final WatchlistTracker harshitPortfolio = new WatchlistTracker(stockDataObserver, "Harshit's Portfolio", STOCKS.ITC.name(), STOCKS.HINDUNILVR.name(), STOCKS.BRITANNIA.name());
        final WatchlistTracker shrutyPortfolio = new WatchlistTracker(stockDataObserver, "Shruty's Portfolio", STOCKS.ITC.name(), STOCKS.NESTLEIND.name(), STOCKS.ONGC.name());
        final WatchlistTracker carlPortfolio = new WatchlistTracker(stockDataObserver, "Carl's Portfolio", STOCKS.BRITANNIA.name(), STOCKS.ITC.name(), STOCKS.AXISBANK.name(), STOCKS.HDFC.name());

        stockDataObserver.notifyListeners(STOCKS.ITC.name(), 100);
        logger.info("Price of " + STOCKS.ITC.name() + " updated to 100");
        logger.info("Harshit's Portfolio price for " + STOCKS.ITC.name() + " is " + harshitPortfolio.getStockPriceMap().get(STOCKS.ITC.name()));
        logger.info("Shruty's Portfolio price for " + STOCKS.ITC.name() + " is " + shrutyPortfolio.getStockPriceMap().get(STOCKS.ITC.name()));
        logger.info("Carl's Portfolio price for " + STOCKS.ITC.name() + " is " + carlPortfolio.getStockPriceMap().get(STOCKS.ITC.name()));
        Assertions.assertEquals(100, harshitPortfolio.getStockPriceMap().get(STOCKS.ITC.name()).get(0).getPrice());
        Assertions.assertEquals(100, shrutyPortfolio.getStockPriceMap().get(STOCKS.ITC.name()).get(0).getPrice());
        Assertions.assertEquals(100, carlPortfolio.getStockPriceMap().get(STOCKS.ITC.name()).get(0).getPrice());

        stockDataObserver.notifyListeners(STOCKS.ITC.name(), 107);
        logger.info("Price of " + STOCKS.ITC.name() + " updated to 107");
        logger.info("Harshit's Portfolio price for " + STOCKS.ITC.name() + " is " + harshitPortfolio.getStockPriceMap().get(STOCKS.ITC.name()));
        logger.info("Shruty's Portfolio price for " + STOCKS.ITC.name() + " is " + shrutyPortfolio.getStockPriceMap().get(STOCKS.ITC.name()));
        logger.info("Carl's Portfolio price for " + STOCKS.ITC.name() + " is " + carlPortfolio.getStockPriceMap().get(STOCKS.ITC.name()));
        Assertions.assertEquals(107, harshitPortfolio.getStockPriceMap().get(STOCKS.ITC.name()).get(1).getPrice());
        Assertions.assertEquals(107, shrutyPortfolio.getStockPriceMap().get(STOCKS.ITC.name()).get(1).getPrice());
        Assertions.assertEquals(107, carlPortfolio.getStockPriceMap().get(STOCKS.ITC.name()).get(1).getPrice());

    }
}
