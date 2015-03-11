package com.dgsd.android.sydtrip.mvp.presenter;

import android.support.annotation.NonNull;

import com.dgsd.android.data.DataSource;
import com.dgsd.android.sydtrip.DataSourceRoutingDataProvider;
import com.dgsd.android.sydtrip.mvp.view.RoutingResultListMVPView;
import com.dgsd.sydtrip.model.Stop;
import com.dgsd.sydtrip.model.StopPair;
import com.dgsd.sydtrip.routing.RoutingContraints;
import com.dgsd.sydtrip.routing.RoutingEngine;
import com.dgsd.sydtrip.routing.model.RoutingResult;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.functions.Func2;

public class RoutingResultListPresenter extends Presenter<RoutingResultListMVPView> {

    private final DataSource dataSource;
    private final RoutingEngine routingEngine;

    private int origId;
    private int destId;

    public RoutingResultListPresenter(@NonNull RoutingResultListMVPView view,
                                      @NonNull DataSource dataSource) {
        super(view);
        this.dataSource = dataSource;
        this.routingEngine = new RoutingEngine(
                new DataSourceRoutingDataProvider(dataSource));
    }

    public void setRoutingOptions(int origId, int destId) {
        this.origId = origId;
        this.destId = destId;
    }

    @Override
    public void onResume() {
        super.onResume();
        bind(getRoutingObservable(), new Observer<RoutingResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(RoutingResult rr) {
                getView().setTitle(String.format("%s - %s",
                        rr.getOrigin().getName(), rr.getDestination().getName()));
                getView().showResults(rr.getItineries());
            }
        });
    }

    private Observable<RoutingResult> getRoutingObservable() {
        return Observable.combineLatest(
                dataSource.getStop(origId).defaultIfEmpty(null),
                dataSource.getStop(destId).defaultIfEmpty(null),
                new Func2<Stop, Stop, StopPair>() {
                    @Override
                    public StopPair call(Stop orig, Stop dest) {
                        return new StopPair(orig, dest);
                    }
                }
        ).map(new Func1<StopPair, StopPair>() {
            @Override
            public StopPair call(StopPair stopPair) {
                if (stopPair.getFrom() == null) {
                    throw new RuntimeException("Could not find origin stop!");
                }

                if (stopPair.getTo() == null) {
                    throw new RuntimeException("Could not find destination stop!");
                }
                return stopPair;
            }
        }).flatMap(new Func1<StopPair, Observable<RoutingResult>>() {
            @Override
            public Observable<RoutingResult> call(StopPair stopPair) {
                return routingEngine.route(stopPair, new RoutingContraints.Builder().today().build());
            }
        });
    }
}
