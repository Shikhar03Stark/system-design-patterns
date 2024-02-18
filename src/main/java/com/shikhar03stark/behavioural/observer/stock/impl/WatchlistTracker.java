package com.shikhar03stark.behavioural.observer.stock.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.shikhar03stark.behavioural.observer.stock.StockDataListener;
import com.shikhar03stark.behavioural.observer.stock.StockDataObserver;

import lombok.Getter;
import lombok.ToString;

@Getter
public class WatchlistTracker implements StockDataListener {

    @Getter(value = lombok.AccessLevel.PRIVATE)
    private final StockDataObserver stockDataObserver;

    private final String trackerName;
    private final List<String> stockSymbols;
    private final Map<String, List<WatchlistTrackerPrice>> stockPriceMap = new HashMap<>();

    public WatchlistTracker(StockDataObserver observer, String trackerName, String... stockSymbols) {
        this.stockDataObserver = observer;
        this.stockDataObserver.subscribe(this, List.of(stockSymbols));

        this.trackerName = trackerName;
        this.stockSymbols = List.of(stockSymbols);
        this.stockSymbols.forEach(stock -> stockPriceMap.put(stock, new LinkedList<>()));
    }

    @Override
    public void acceptPriceChange(String stockSymbol, double price) {
        final WatchlistTrackerPrice watchlistTrackerPrice = new WatchlistTrackerPrice(stockSymbol, price, Instant.now());
        stockPriceMap.get(stockSymbol).add(watchlistTrackerPrice);
    }

    @ToString
    public class WatchlistTrackerPrice {
        private final String stockSymbol;
        private final double price;
        private final Instant timestamp;

        public WatchlistTrackerPrice(String stockSymbol, double price, Instant timestamp) {
            this.stockSymbol = stockSymbol;
            this.price = price;
            this.timestamp = timestamp;
        }

        public String getStockSymbol() {
            return stockSymbol;
        }

        public double getPrice() {
            return price;
        }

        public Instant getTimestamp() {
            return timestamp;
        }
    }

}
