package com.shikhar03stark.behavioural.observer.stock.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shikhar03stark.behavioural.observer.stock.StockDataListener;
import com.shikhar03stark.behavioural.observer.stock.StockDataObserver;

public class ZerodhaStockDataObserverImpl implements StockDataObserver{

    // Map of listeners interested in a particular stock
    final Map<String, Set<StockDataListener>> stockListenerMap = new HashMap<>();

    // Map of listeners to its subscription
    final Map<StockDataListener, Set<String>> listenerStockMap = new HashMap<>();

    @Override
    public void subscribe(StockDataListener listener, List<String> stockSymbols) {
        for (String stockSymbol : stockSymbols) {
            stockListenerMap.computeIfAbsent(stockSymbol, k -> new HashSet<>()).add(listener);
        }

        listenerStockMap.computeIfAbsent(listener, k -> new HashSet<>()).addAll(stockSymbols);
    }

    @Override
    public void unsubscribe(StockDataListener listener, List<String> stockSymbols) {
        if(listenerStockMap.containsKey(listener) == false){
            return;
        }
        for (String stockSymbol : stockSymbols) {
            stockListenerMap.get(stockSymbol).remove(listener);
        }

        listenerStockMap.get(listener).removeAll(stockSymbols);
        if(listenerStockMap.get(listener).isEmpty()){
            listenerStockMap.remove(listener);
        }
    }

    @Override
    public void unsubscribe(StockDataListener listener) {
        if(listenerStockMap.containsKey(listener) == false){
            return;
        }
        for (String stockSymbol : listenerStockMap.get(listener)) {
            stockListenerMap.get(stockSymbol).remove(listener);
        }
        listenerStockMap.remove(listener);
    
    }

    @Override
    public void notifyListeners(String stockSymbol, double price) {
        if(stockListenerMap.containsKey(stockSymbol) == false){
            return;
        }
        for (StockDataListener listener : stockListenerMap.get(stockSymbol)) {
            listener.acceptPriceChange(stockSymbol, price);
        }
    }

}
