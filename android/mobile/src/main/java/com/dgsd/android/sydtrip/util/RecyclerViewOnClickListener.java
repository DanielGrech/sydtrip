package com.dgsd.android.sydtrip.util;

import android.view.View;

public interface RecyclerViewOnClickListener<V extends View> {

    public void onClick(V view, int position);
}
