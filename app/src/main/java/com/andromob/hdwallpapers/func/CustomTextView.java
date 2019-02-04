package com.andromob.hdwallpapers.func;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by andromob on 26/08/2018.
 */

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
    public CustomTextView(Context context) {
        super(context);
        setFont();
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
