package com.dgsd.android.sydtrip.mvp.view;

import com.dgsd.sydtrip.model.Stop;

import java.util.List;

public interface StopListMVPView extends MVPView {

    public void showStops(List<Stop> stops);

}
