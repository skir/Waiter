package com.kodisoft.waiter.data.model;

public class Row {
    boolean alreadyTaken;

    public void setAlreadyTaken(boolean alreadyTaken) {
        this.alreadyTaken = alreadyTaken;
    }

    public boolean isAlreadyTaken() {
        return alreadyTaken;
    }

    public int getId() {
        return -1;
    }
}