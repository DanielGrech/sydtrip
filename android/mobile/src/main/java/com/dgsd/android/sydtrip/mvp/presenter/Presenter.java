package com.dgsd.android.sydtrip.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.dgsd.android.sydtrip.mvp.view.MVPView;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class Presenter<T extends MVPView> {

    private final T view;

    private CompositeSubscription subscriptions;

    public Presenter(@NonNull T view) {
        this.view = view;
    }

    protected T getView() {
        return view;
    }

    protected Context getContext() {
        return view.getContext();
    }

    public void onCreate(Bundle savedInstanceState) {
        subscriptions = new CompositeSubscription();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    public void onDestroy() {

    }

    public void onResume() {

    }

    public void onPause() {
        subscriptions.unsubscribe();
    }

    protected <R> void bind(Observable<R> observable, Observer<? super R> observer) {
        final Observable<R> boundObservable;

        if (view instanceof Fragment) {
            boundObservable = AppObservable.bindFragment(view, observable);
        } else if (getContext() instanceof Activity) {
            boundObservable = AppObservable.bindActivity((Activity) getContext(), observable);
        } else {
            boundObservable = observable;
        }

        final Subscription subscription
                = boundObservable.subscribeOn(Schedulers.io()).subscribe(observer);

        if (subscriptions.isUnsubscribed()) {
            subscriptions = new CompositeSubscription();
        }

        subscriptions.add(subscription);
    }

}
