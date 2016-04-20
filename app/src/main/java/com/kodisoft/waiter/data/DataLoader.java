package com.kodisoft.waiter.data;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kodisoft.waiter.data.model.MenuItem;
import com.kodisoft.waiter.data.model.Notification;
import com.kodisoft.waiter.data.model.Order;
import com.kodisoft.waiter.data.model.OrderItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DataLoader {

    private static DataLoader loader;
    private Context mContext;

    private List<Order> mOrders;
    private List<MenuItem> mMenuItems;
    private List<Notification> mNotifications;

    public static DataLoader getDataLoader(Context context) {
        if (loader == null) {
            loader = new DataLoader(context);
        }
        return loader;
    }

    private DataLoader(Context context) {
        mContext = context;
    }

    public List<Order> getOrders() {
        if (mOrders == null) {
            mOrders = new Gson().fromJson(readJSON("orders.json"), new TypeToken<List<Order>>(){}.getType());
        }
        return mOrders;
    }

    public Order getOrder(int id) {
        if (mOrders == null) {
            getOrders();
        }
        for (Order order : mOrders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    public Notification getNotification(int id) {
        if (mNotifications == null) {
            getNotifications();
        }
        for (Notification notification : mNotifications) {
            if (notification.getId() == id) {
                return notification;
            }
        }
        return null;
    }

    public List<Notification> getNotifications() {
        if (mNotifications == null) {
            mNotifications = new Gson().fromJson(readJSON("notifications.json"),
                    new TypeToken<List<Notification>>(){}.getType());
        }
        return mNotifications;
    }

    public void deleteNotification(Notification notification) {
        getNotifications();
        if (mNotifications.contains(notification)) {
            mNotifications.remove(notification);
        }
    }

    public List<MenuItem> getMenuItems() {
        if (mMenuItems == null) {
            mMenuItems = new Gson().fromJson(readJSON("items.json"), new TypeToken<List<MenuItem>>(){}.getType());
        }
        return mMenuItems;
    }

    public MenuItem getMenuItem(int id) {
        if (mMenuItems == null) {
            getMenuItems();
        }
        for (MenuItem menuItem : mMenuItems) {
            if (menuItem.getId() == id) {
                return menuItem;
            }
        }
        return null;
    }

    private String readJSON(String filePath) {
        Log.e("read",filePath);
        String json;
        try {
            InputStream is = mContext.getAssets().open(filePath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void addItemToOrder(int itemId, int orderId) {
        getOrders();
        getMenuItems();
        for (MenuItem menuItem : mMenuItems) {
            if (menuItem.getId() == itemId) {
                for (Order order : mOrders) {
                    if (order.getId() == orderId) {
                        List<OrderItem> items = order.getItems();
                        OrderItem newItem = new OrderItem();
                        newItem.setName(menuItem.getName());
                        newItem.setPrice(menuItem.getPrice());
                        newItem.setMenuItem(menuItem.getId());
                        items.add(newItem);
                        order.setItems(items);
                        Log.e("order","inserted"+items.size());
                        return;
                    }
                }
                return;
            }
        }
    }

    public void addOrderToOrder(int newOrderId, int id) {
        getOrders();
        for (Order order : mOrders) {
            if (order.getId() == id) {
                Log.e("order", "before "+order.getPayfor().toString());
                List<Integer> payFor = order.getPayfor();
                if (payFor.contains(newOrderId)) {
                    payFor.remove(Integer.valueOf(newOrderId));
                } else {
                    payFor.add(newOrderId);
                }
                order.setPayFor(payFor);
                Log.e("order", "after "+order.getPayfor().toString());
                return;
            }
        }
    }
}
