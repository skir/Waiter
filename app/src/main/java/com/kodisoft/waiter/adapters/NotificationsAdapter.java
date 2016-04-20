package com.kodisoft.waiter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kodisoft.waiter.R;
import com.kodisoft.waiter.data.model.Notification;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotificationsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Notification> mNotifications = new ArrayList<>();
    Context mContext;
    OnClickListener mListener;

    public NotificationsAdapter(Context context) {
        mContext = context;
    }

    public void setNotifications(List<Notification> notifications) {
        mNotifications = notifications;
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
        Notification row = mNotifications.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.description.setText(row.getDestination().toString());
        holder.description.setAllCaps(true);
        holder.descriptionSecondary.setText(row.getDescription());
        holder.view.setOnClickListener((view) -> mListener.onClick(row.getId()));
        switch (row.getType()) {
            case Notification.TYPE_ORDER:
                holder.icon.setImageResource(R.drawable.ic_table);
                break;
            case Notification.TYPE_CUTLERY:
                holder.icon.setImageResource(R.drawable.ic_cutlery);
                break;
            case Notification.TYPE_TAXI:
                holder.icon.setImageResource(R.drawable.ic_taxi);
                break;
            case Notification.TYPE_KITCHEN:
                holder.icon.setImageResource(R.drawable.ic_kitchen);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mNotifications != null) {
            return mNotifications.size();
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
            price.setVisibility(View.GONE);
        }
    }


    public interface OnClickListener {
        void onClick(int id);
    }
}


