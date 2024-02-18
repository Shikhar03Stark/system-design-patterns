package com.shikhar03stark.behavioural.observer.stock;

/*
 * This interface is used to notify the client of the stock exchange about the price change of a stock.
 * The client can then notify all other services that are interested in the price change.
 */
public interface ExchangeClientListener {
    void onPriceChange(String stockSymbol, double price);
}
