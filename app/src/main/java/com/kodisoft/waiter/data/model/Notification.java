package com.kodisoft.waiter.data.model;

public class Notification {

    public static final int TYPE_ORDER = 0;
    public static final int TYPE_CUTLERY = 1;
    public static final int TYPE_TAXI = 2;
    public static final int TYPE_KITCHEN = 3;

    int id;
    int type;
    Place destination;
    String description;
    int orderId;

    public int getId() {
        return id;
    }

    public Place getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public int getOrderId() {
        return orderId;
    }
}
