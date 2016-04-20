package com.kodisoft.waiter.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kodisoft.waiter.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SimpleFragment extends Fragment {

    @Bind(R.id.description)
    TextView mDescription;

    @Bind(R.id.done)
    Button mDone;

    String mMessage;

    CallBack mCallBack;

    public SimpleFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMessage = getArguments().getString("message");
        }
    }

    public static SimpleFragment newInstance(String message) {
        SimpleFragment fragment = new SimpleFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        fragment.setArguments(args);
        return fragment;
    }

    public SimpleFragment setCallBack(CallBack callBack) {
        this.mCallBack = callBack;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_simple, container, false);
        ButterKnife.bind(this, rootView);

        mDescription.setText(mMessage);
        mDone.setOnClickListener(view ->
            mCallBack.onDone());

        return rootView;
    }

    public interface CallBack {
        void onDone();
    }
}
