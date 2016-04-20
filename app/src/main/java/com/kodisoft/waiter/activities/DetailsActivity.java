package com.kodisoft.waiter.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kodisoft.waiter.R;
import com.kodisoft.waiter.data.DataLoader;
import com.kodisoft.waiter.data.model.Notification;
import com.kodisoft.waiter.fragments.ChooserFragment;
import com.kodisoft.waiter.fragments.NumberFragment;
import com.kodisoft.waiter.fragments.OrderFragment;
import com.kodisoft.waiter.fragments.SimpleFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    Notification mNotification;

    OrderFragment mOrderFragment;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            Slide slide = new Slide();
            slide.setInterpolator(new AccelerateDecelerateInterpolator());
            slide.setSlideEdge(Gravity.RIGHT);
            slide.excludeTarget(android.R.id.statusBarBackground, true);
            slide.excludeTarget(android.R.id.navigationBarBackground, true);
            slide.excludeTarget(R.id.toolbar, true);
            getWindow().setEnterTransition(slide);
            getWindow().setExitTransition(slide);
            getWindow().setAllowEnterTransitionOverlap(true);
        }

        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        int notificationId = -1;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("notification")) {
            notificationId = intent.getIntExtra("notification", -1);
        }

        if (notificationId != -1) {
            mNotification = DataLoader.getDataLoader(this).getNotification(notificationId);
            FragmentManager manager = getFragmentManager();
            int count = manager.getBackStackEntryCount();
            if (count > 0) {
                manager.executePendingTransactions();
            } else {
                if (mNotification.getType() == Notification.TYPE_ORDER) {
                    getSupportActionBar().setTitle(getString(R.string.order) + " " + mNotification.getDestination().toString().toUpperCase());
                    mOrderFragment = OrderFragment.newInstance(mNotification.getOrderId());
                    manager.beginTransaction()
//                    .setCustomAnimations(R.animator.slide_in, R.animator.slide_in_left, R.animator.slide_out_left, R.animator.slide_out)
                            .replace(R.id.fragment, mOrderFragment)
                            .addToBackStack("order" + String.valueOf(mNotification.getOrderId()))
                            .commit();
                } else {
                    manager.beginTransaction()
                            .replace(R.id.fragment,
                                    SimpleFragment.newInstance(mNotification.getDescription() + '\n' + mNotification.getDestination().toString().toUpperCase())
                                            .setCallBack(() -> done()))
                            .addToBackStack("simple")
                            .commit();
                }
            }
        }
    }

    public void addItem() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in, R.animator.slide_in_left, R.animator.slide_out_left, R.animator.slide_out)
                .replace(R.id.fragment, ChooserFragment.newInstance(ChooserFragment.TYPE_MENU_ITEMS, mNotification.getOrderId()))
                .addToBackStack("chooser")
                .commit();
    }

    public void pay(double sum) {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .title(R.string.payment)
                .customView(R.layout.payment_chooser, false)
                .build();
        View dialogRoot = mDialog.getView();
        dialogRoot.findViewById(R.id.card).setOnClickListener(v -> {
            mDialog.dismiss();
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_in, R.animator.slide_in_left, R.animator.slide_out_left, R.animator.slide_out)
                    .replace(R.id.fragment, NumberFragment.newInstance(NumberFragment.CARD, sum))
                    .addToBackStack("number")
                    .commit();
        });
        dialogRoot.findViewById(R.id.cash).setOnClickListener(v -> {
            mDialog.dismiss();
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_in, R.animator.slide_in_left, R.animator.slide_out_left, R.animator.slide_out)
                    .replace(R.id.fragment, NumberFragment.newInstance(NumberFragment.CASH, sum))
                    .addToBackStack("number")
                    .commit();
        });

        mDialog.show();
    }

    public void payed(double change) {
        if (change > 0.0) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment,
                            SimpleFragment.newInstance(getString(R.string.give) + " " + String.valueOf(change))
                                    .setCallBack(() -> printReceipt(true)))
                    .addToBackStack("simple")
                    .commit();
        } else {
            printReceipt(true);
        }
    }

    public void addOrder() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in, R.animator.slide_in_left, R.animator.slide_out_left, R.animator.slide_out)
                .replace(R.id.fragment, ChooserFragment.newInstance(ChooserFragment.TYPE_ORDERS, mNotification.getOrderId()))
                .addToBackStack("chooser" + String.valueOf(mNotification.getOrderId()))
                .commit();
    }

    public void backToOrder() {
        if (mOrderFragment != null) {
            mOrderFragment.update();
            onBackPressed();
        }
    }

    public void done() {
        DataLoader.getDataLoader(this).deleteNotification(mNotification);
        finish();
    }

    public void printReceipt(boolean payed) {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment,
                        SimpleFragment.newInstance(getString(R.string.give_receipt))
                                .setCallBack(() -> {
                                    if (payed) {
                                        done();
                                    }
                                }))
                .addToBackStack("simple")
                .commit();
    }

    @Override
    public void onBackPressed() {
        Log.e("backStack", String.valueOf(getFragmentManager().getBackStackEntryCount()));
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
