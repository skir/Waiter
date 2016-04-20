package com.kodisoft.waiter.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout {

    private int width = 0;

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setXFraction(final float fraction) {
        if (getWidth() != 0) {
            width = getWidth();
        }
        float translationX = width * fraction;
        setTranslationX(translationX);
    }

    public float getXFraction() {
        if (getWidth() != 0) {
            width = getWidth();
        }
        if (width == 0) {
            return 0;
        }
        return getTranslationX() / width;
    }
}
