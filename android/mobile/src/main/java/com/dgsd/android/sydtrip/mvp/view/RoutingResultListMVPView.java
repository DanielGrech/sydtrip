package com.dgsd.android.sydtrip.mvp.view;

import com.dgsd.sydtrip.routing.model.RoutingItinery;

import java.util.List;

public interface RoutingResultListMVPView extends MVPView {

    public void setTitle(String title);

    public void showResults(List<RoutingItinery> results);
}
