package com.dgsd.android.sydtrip.mvp.presenter;

import android.support.annotation.NonNull;

import com.dgsd.android.data.DataSource;
import com.dgsd.android.sydtrip.mvp.view.StopListMVPView;
import com.dgsd.sydtrip.model.Stop;

import java.util.List;

import rx.Observer;

public class StopListPresenter extends Presenter<StopListMVPView> {

    private final DataSource dataSource;

    public StopListPresenter(@NonNull StopListMVPView view, @NonNull DataSource dataSource) {
        super(view);
        this.dataSource = dataSource;
    }

    @Override
    public void onResume() {
        super.onResume();
        bind(dataSource.getStops(), new Observer<List<Stop>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Stop> stops) {
                getView().showStops(stops);
            }
        });
    }
}
