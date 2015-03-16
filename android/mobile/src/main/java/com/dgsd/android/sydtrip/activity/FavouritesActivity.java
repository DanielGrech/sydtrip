package com.dgsd.android.sydtrip.activity;

import android.os.Bundle;
import android.view.Menu;

import com.dgsd.android.sydtrip.R;

public class FavouritesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_favourites);

//        getApp().getDataSource()
//                .getNetwork()
//                .subscribe(new Action1<List<GraphEdge>>() {
//                    @Override
//                    public void call(List<GraphEdge> graphEdges) {
//                        final Stop from = getApp().getDataSource().getStop(4352).toBlocking().single();
//                        final Stop to = getApp().getDataSource().getStop(2026).toBlocking().single();
//                        final Stop to = getApp().getDataSource().getStop(4008).toBlocking().single();
//
//                        new TransportNetworkGraph(graphEdges).findBestPath(from, to);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        throwable.printStackTrace();
//                    }
//                });

//        final Observable<List<String>> observable = getApp().getUpdateManager()
//                .update()
//                .flatMap(new Func1<String, Observable<List<String>>>() {
//                    @Override
//                    public Observable<List<String>> call(String rootDir) {
//                        return getApp().getImportManager().asObservable(rootDir);
//                    }
//                });
//
//        observable.observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer<List<String>>() {
//                    @Override
//                    public void onCompleted() {
//                        Timber.w("onCompleted()");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Timber.e(e, "onError()");
//                    }
//
//                    @Override
//                    public void onNext(List<String> strings) {
//                        Timber.w("onNext() -> %s", strings);
//                    }
//                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_favourites, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
