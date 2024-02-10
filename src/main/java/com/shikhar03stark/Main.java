package com.shikhar03stark;

import com.shikhar03stark.creational.singleton.Logger;
import com.shikhar03stark.creational.singleton.LoggerFactory;

public class Main {
    public static void main(String[] args) {
        Logger log = LoggerFactory.getConsoleInstance();
    }
}