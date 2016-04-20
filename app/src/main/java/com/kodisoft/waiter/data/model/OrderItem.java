package com.kodisoft.waiter.data.model;

public class OrderItem {
    String name;
    double price;
    int menu_item;

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getMenuItem() {
        return menu_item;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMenuItem(int menu_item) {
        this.menu_item = menu_item;
    }
}
