package com.kodisoft.waiter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kodisoft.waiter.R;
import com.kodisoft.waiter.data.model.MenuItem;
import com.kodisoft.waiter.data.model.Order;
import com.kodisoft.waiter.data.model.Row;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooserAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Row> mRows = new ArrayList<>();
    Context mContext;
    OnClickListener mListener;

    public ChooserAdapter(Context context) {
        mContext = context;
    }

    public void setRows(List<Row> rows) {
        mRows = rows;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Row row = mRows.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;
        if (row instanceof Order) {
            if (row.isAlreadyTaken()) {
                holder.icon.setImageResource(R.drawable.ic_done);
            } else {
                holder.icon.setImageResource(R.drawable.ic_circle);
            }
            holder.description.setText(((Order) row).getDestination().toString());
            holder.description.setAllCaps(true);
        } else if (row instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) row;
            holder.description.setText(menuItem.getName());
            holder.price.setText(String.format("%.2f", menuItem.getPrice()));
            holder.icon.setImageResource(menuItem.getItemIcon());
        }
        holder.view.setOnClickListener((view) -> mListener.onClick(row.getId()));
    }

    @Override
    public int getItemCount() {
        if (mRows != null) {
            return mRows.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
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


    public interface OnClickListener {
        void onClick(int id);
    }
}


