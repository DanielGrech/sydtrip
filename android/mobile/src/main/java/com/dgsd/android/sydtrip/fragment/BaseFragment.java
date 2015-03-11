package com.dgsd.android.sydtrip.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgsd.android.sydtrip.STApp;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment extends Fragment {

    /**
     * @return id of the layout to use in this fragment
     */
    protected abstract @LayoutRes int getLayoutId();

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutId(), container, false);

        ButterKnife.inject(this, view);
        onCreateView(view, savedInstanceState);

        return view;
    }

    @Override
    public void onPause() {
        subscriptions.unsubscribe();
        super.onPause();
    }

    protected void onCreateView(View rootView, Bundle savedInstanceState) {
        // Hook for subclasses to override
    }

    protected STApp getApp() {
        return (STApp) getActivity().getApplication();
    }
}
