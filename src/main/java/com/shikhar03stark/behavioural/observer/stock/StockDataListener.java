package com.shikhar03stark.behavioural.observer.stock;

public interface StockDataListener {

    void acceptPriceChange(String stockSymbol, double price);

}
