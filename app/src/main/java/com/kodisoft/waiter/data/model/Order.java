package com.kodisoft.waiter.data.model;

import java.util.List;

public class Order extends Row {
    int id;
    Place destination;
    List<OrderItem> items;
    List<Integer> payfor;

    @Override
    public int getId() {
        return id;
    }

    public Place getDestination() {
        return destination;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public List<Integer> getPayfor() {
        return payfor;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setPayFor(List<Integer> payfor) {
        this.payfor = payfor;
    }

    @Override
    public String toString() {
        String result = String.valueOf(id) + ", [";
        for (OrderItem item : items) {
            result += "{" + item.name + String.valueOf(item.price) + String.valueOf(item.menu_item) +"}";
        }
        result += "]";
        result += payfor.toString();
        return result;
    }
}
