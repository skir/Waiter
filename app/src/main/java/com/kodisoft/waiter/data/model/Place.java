package com.kodisoft.waiter.data.model;

public class Place {
    int table;
    String seat;

    @Override
    public String toString() {
        return String.valueOf(table) + seat;
    }
}
