package com.kodisoft.waiter.data.model;

import com.kodisoft.waiter.R;

import java.util.List;

public class MenuItem extends Row {
    int id;
    String name;
    double price;
    List<MenuItem> sub_items;

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public List<MenuItem> getSubItems() {
        return sub_items;
    }

    public int getItemIcon() {
        if (name.equals("soup")) {          //silly
            return R.drawable.ic_soup;
        } else if (name.equals("tea")) {
            return R.drawable.ic_cup;
        }
        return R.drawable.ic_wine;
    }
}
