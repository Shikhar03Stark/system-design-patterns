package com.shikhar03stark.behavioural.observer.stock.impl;

import com.shikhar03stark.behavioural.observer.stock.ExchangeClientListener;
import com.shikhar03stark.behavioural.observer.stock.StockDataObserver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NseExchangeClientListenerImpl implements ExchangeClientListener {

    private final StockDataObserver stockDataObserver;

    @Override
    public void onPriceChange(String stockSymbol, double price) {
        stockDataObserver.notifyListeners(stockSymbol, price);
    }

}
