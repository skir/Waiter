package com.kodisoft.waiter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kodisoft.waiter.R;
import com.kodisoft.waiter.data.DataLoader;
import com.kodisoft.waiter.data.model.Order;
import com.kodisoft.waiter.data.model.OrderItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Order mOrder;
    Context mContext;
    ClickListener clickListener;

    private final int TYPE_ITEM = 0;
    private final int TYPE_FOOTER = 1;
    private final int TYPE_ADD_ITEM = 2;

    public OrderAdapter(Context context) {
        mContext = context;
        clickListener = new ClickListener() {
            @Override
            public void addItem() {
            }

            @Override
            public void pay(double sum) {
            }

            @Override
            public void addOrder() {
            }

            @Override
            public void printRecipe() {
            }
        };
    }

    public void setOrder(Order order) {
        mOrder = order;
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_ITEM:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_item, parent, false));
            case TYPE_FOOTER:
                return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false));
            case  TYPE_ADD_ITEM:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_item, parent, false));
        }
        throw new RuntimeException("unsupported type");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_ITEM: {
                OrderItem row = mOrder.getItems().get(position);
                ViewHolder holder = (ViewHolder) viewHolder;
                holder.description.setText(row.getName());
                holder.price.setText(String.format("%.2f", row.getPrice()));
                holder.view.setClickable(false);
                holder.icon.setImageResource(DataLoader.getDataLoader(mContext).getMenuItem(row.getMenuItem()).getItemIcon());
                break;
            }
            case TYPE_ADD_ITEM: {
                ViewHolder holder = (ViewHolder) viewHolder;
                holder.description.setText(mContext.getString(R.string.add_item));
                holder.view.setOnClickListener(view -> clickListener.addItem());
                holder.icon.setImageResource(R.drawable.ic_add);
                break;
            }
            case TYPE_FOOTER: {
                FooterViewHolder holder = (FooterViewHolder) viewHolder;
                double sum = 0.0;
                List<OrderItem> items = mOrder.getItems();
                for (OrderItem item : items) {
                    sum += item.getPrice();
                }
                holder.pay.setText(String.format(mContext.getString(R.string.price),sum));
                final double finalSum = sum;                                        //wtf?
                holder.pay.setOnClickListener(view -> clickListener.pay(finalSum));
                holder.addOrder.setOnClickListener(view -> clickListener.addOrder());
                holder.printRecipe.setOnClickListener(view -> clickListener.printRecipe());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mOrder != null && mOrder.getItems() != null) {
            return mOrder.getItems().size() + 2;    //plus 'add item' and footer
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemCount() - 2) {
            return TYPE_ITEM;
        } else if (position == getItemCount() - 2) {
            return TYPE_ADD_ITEM;
        }
        return TYPE_FOOTER;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.descriptionMain)
        TextView description;
        @Bind(R.id.descriptionSecondary)
        TextView descriptionSecondary;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.icon)
        ImageView icon;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
            descriptionSecondary.setVisibility(View.GONE);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.pay)
        TextView pay;
        @Bind(R.id.add_order)
        TextView addOrder;
        @Bind(R.id.print_recipe)
        TextView printRecipe;
        View view;

        public FooterViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }

    public interface ClickListener {
        void addItem();
        void pay(double sum);
        void addOrder();
        void printRecipe();
    }
}


