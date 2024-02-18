package com.shikhar03stark.behavioural.observer.stock;

import java.util.List;

public interface StockDataObserver {
    
    void subscribe(StockDataListener listener, List<String> stockSymbols);
    void unsubscribe(StockDataListener listener, List<String> stockSymbols);
    void unsubscribe(StockDataListener listener);
    void notifyListeners(String stockSymbol, double price);
}
