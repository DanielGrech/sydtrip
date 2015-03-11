package com.dgsd.android.sydtrip.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dgsd.sydtrip.model.Stop;

public class StopListItemView extends TextView {
    public StopListItemView(Context context) {
        super(context);
    }

    public StopListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StopListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void populate(Stop stop) {
        setText(stop.getName());
    }
}
