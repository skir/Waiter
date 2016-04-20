package com.kodisoft.waiter.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kodisoft.waiter.activities.DetailsActivity;
import com.kodisoft.waiter.R;
import com.kodisoft.waiter.adapters.ChooserAdapter;
import com.kodisoft.waiter.data.DataLoader;
import com.kodisoft.waiter.data.model.MenuItem;
import com.kodisoft.waiter.data.model.Order;
import com.kodisoft.waiter.data.model.Row;
import com.kodisoft.waiter.tools.DividerItemDecoration;
import com.kodisoft.waiter.views.CustomRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooserFragment extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.root)
    CustomRelativeLayout mRelativeLayout;

    ChooserAdapter mAdapter;
    List<Row> mData = new ArrayList<>();

    public static final int TYPE_ORDERS = 0;
    public static final int TYPE_MENU_ITEMS = 1;

    private int currentType = -1;
    private int orderId = -1;

    public ChooserFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentType = getArguments().getInt("type");
            orderId = getArguments().getInt("source");
            switch (currentType) {
                case TYPE_ORDERS: {
                    mData.clear();
                    Order order = null;
                    if (orderId != -1) {
                        order = DataLoader.getDataLoader(getActivity()).getOrder(orderId);
                        Log.e("order",order.getPayfor().toString());
                    }
                    List<Order> orderList = DataLoader.getDataLoader(getActivity()).getOrders();
                    for (Order item : orderList) {
                        item.setAlreadyTaken(order != null && order.getPayfor().contains(item.getId()));
                        mData.add(item);
                    }
                    break;
                }
                case TYPE_MENU_ITEMS: {
                    mData.clear();
                    List<MenuItem> items = DataLoader.getDataLoader(getActivity()).getMenuItems();
                    for (MenuItem item : items) {
                        item.setAlreadyTaken(false);
                        mData.add(item);
                    }
                }
            }
        }
    }

    public static ChooserFragment newInstance(int type, int sourceId) {
        ChooserFragment fragment = new ChooserFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("source", sourceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mRelativeLayout.setWidth(metrics.widthPixels);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ChooserAdapter(getActivity());
        mAdapter.setOnClickListener(id -> {
            Log.e("id",String.valueOf(id));
            switch (currentType) {
                case TYPE_MENU_ITEMS: {
                    DataLoader.getDataLoader(getActivity()).addItemToOrder(id, orderId);
                    break;
                }
                case TYPE_ORDERS: {
                    DataLoader.getDataLoader(getActivity()).addOrderToOrder(id, orderId);
                }
            }
            ((DetailsActivity) getActivity()).backToOrder();
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setRows(mData);

        return rootView;
    }
}
