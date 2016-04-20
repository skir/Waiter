package com.kodisoft.waiter.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.kodisoft.waiter.R;
import com.kodisoft.waiter.activities.DetailsActivity;
import com.kodisoft.waiter.adapters.NotificationsAdapter;
import com.kodisoft.waiter.data.DataLoader;
import com.kodisoft.waiter.tools.DividerItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.notifications)
    RecyclerView mNotifications;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    NotificationsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setAllowEnterTransitionOverlap(true);
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            Slide slide = new Slide();
            slide.setInterpolator(new AccelerateDecelerateInterpolator());
            slide.setSlideEdge(Gravity.LEFT);
            slide.excludeTarget(android.R.id.statusBarBackground, true);
            slide.excludeTarget(android.R.id.navigationBarBackground, true);
            slide.excludeTarget(R.id.toolbar, true);
            getWindow().setExitTransition(slide);
            getWindow().setEnterTransition(slide);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.notifications);

        mNotifications.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new NotificationsAdapter(this);
        mAdapter.setOnClickListener(id -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("notification", id);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } else {
                startActivity(intent);
            }
        });
        mNotifications.setAdapter(mAdapter);
        mNotifications.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setNotifications(DataLoader.getDataLoader(this).getNotifications());

    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setNotifications(DataLoader.getDataLoader(this).getNotifications());
    }
}
