package com.kodisoft.waiter.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kodisoft.waiter.activities.DetailsActivity;
import com.kodisoft.waiter.R;
import com.kodisoft.waiter.views.CustomRelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NumberFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.num0)
    TextView mNum0;
    @Bind(R.id.num1)
    TextView mNum1;
    @Bind(R.id.num2)
    TextView mNum2;
    @Bind(R.id.num3)
    TextView mNum3;
    @Bind(R.id.num4)
    TextView mNum4;
    @Bind(R.id.num5)
    TextView mNum5;
    @Bind(R.id.num6)
    TextView mNum6;
    @Bind(R.id.num7)
    TextView mNum7;
    @Bind(R.id.num8)
    TextView mNum8;
    @Bind(R.id.num9)
    TextView mNum9;
    @Bind(R.id.dot)
    TextView mDot;

    @Bind(R.id.input)
    EditText mInput;

    @Bind(R.id.textInputLayout)
    TextInputLayout mInputLayput;

    @Bind(R.id.back)
    TextView mBack;
    @Bind(R.id.done)
    TextView mDone;

    @Bind(R.id.relativeLayout)
    CustomRelativeLayout mRelativeLayout;

    public static final int CASH = 0;
    public static final int CARD = 1;

    private int mType = -1;
    private double mSum = 0.0;

    public NumberFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
            mSum = getArguments().getDouble("sum");
        }
    }

    public static NumberFragment newInstance(int type, double sum) {
        NumberFragment fragment = new NumberFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putDouble("sum", sum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_number, container, false);
        ButterKnife.bind(this, rootView);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mRelativeLayout.setWidth(metrics.widthPixels);

        mInput.setOnTouchListener((view, motion) -> true);

        if (mType == CARD) {
            mDot.setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.box).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
            mInputLayput.setHint(getActivity().getString(R.string.card));
            mInput.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
        } else {
            mDot.setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.box).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
            mInputLayput.setHint(getActivity().getString(R.string.cash));
        }

        mNum0.setOnClickListener(this);
        mNum1.setOnClickListener(this);
        mNum2.setOnClickListener(this);
        mNum3.setOnClickListener(this);
        mNum4.setOnClickListener(this);
        mNum5.setOnClickListener(this);
        mNum6.setOnClickListener(this);
        mNum7.setOnClickListener(this);
        mNum8.setOnClickListener(this);
        mNum9.setOnClickListener(this);
        mDot.setOnClickListener(this);

        mBack.setOnClickListener(view -> {
            if (mInput.getText().length() > 0) {
                mInput.setText(mInput.getText().toString().substring(0, mInput.getText().length() - 1));
            }
        });

        mDone.setOnClickListener(view -> {
            Log.e("input", Double.valueOf(mInput.getText().toString()).toString());
            double change = 0.0;
            if (mType == CASH) {
                change = Double.valueOf(mInput.getText().toString()) - mSum;
            }
            if (change >= 0) {
                ((DetailsActivity) getActivity()).payed(change);
            } else {
                Toast.makeText(getActivity(), R.string.not_enough, Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View view) {
        mInput.setText(mInput.getText().toString() + ((TextView) view).getText().toString());
    }
}
