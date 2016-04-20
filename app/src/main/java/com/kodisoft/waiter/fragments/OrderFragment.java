package com.kodisoft.waiter.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kodisoft.waiter.activities.DetailsActivity;
import com.kodisoft.waiter.R;
import com.kodisoft.waiter.adapters.OrderAdapter;
import com.kodisoft.waiter.data.DataLoader;
import com.kodisoft.waiter.data.model.Order;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderFragment extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    OrderAdapter mAdapter;
    Order mOrder;

    private int orderId = -1;

    public OrderFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().getInt("order");
            mOrder = DataLoader.getDataLoader(getActivity()).getOrder(orderId);
        }
    }

    public void update() {
        if (mOrder != null) {
            mOrder = DataLoader.getDataLoader(getActivity()).getOrder(mOrder.getId());
            if (mAdapter != null) {
                mAdapter.setOrder(mOrder);
                Log.e("order", "updated" + mOrder.getItems().size());
            }
        }
    }

    public static OrderFragment newInstance(int orderId) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt("order", orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new OrderAdapter(getActivity());
        mAdapter.setClickListener(new OrderAdapter.ClickListener() {
            @Override
            public void addItem() {
                ((DetailsActivity) getActivity()).addItem();
            }

            @Override
            public void pay(double sum) {
                ((DetailsActivity) getActivity()).pay(sum);
            }

            @Override
            public void addOrder() {
                ((DetailsActivity) getActivity()).addOrder();
            }

            @Override
            public void printRecipe() {
                ((DetailsActivity) getActivity()).printReceipt(false);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOrder(mOrder);

        return rootView;
    }
}
