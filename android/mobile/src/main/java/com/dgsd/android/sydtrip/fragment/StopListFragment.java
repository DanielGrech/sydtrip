package com.dgsd.android.sydtrip.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dgsd.android.sydtrip.R;
import com.dgsd.android.sydtrip.adapter.StopListAdapter;
import com.dgsd.android.sydtrip.mvp.presenter.StopListPresenter;
import com.dgsd.android.sydtrip.mvp.view.StopListMVPView;
import com.dgsd.sydtrip.model.Stop;

import java.util.List;

import butterknife.InjectView;

public class StopListFragment extends PresentableFragment<StopListPresenter> implements StopListMVPView {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    StopListAdapter adapter;

    private StopListPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_stop_list;
    }

    @Override
    protected void onCreateView(View rootView, Bundle savedInstanceState) {
        super.onCreateView(rootView, savedInstanceState);

        adapter = new StopListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    protected StopListPresenter getPresenter() {
        if (presenter == null) {
            presenter = new StopListPresenter(this, getApp().getDataSource());
        }
        return presenter;
    }

    @Override
    public void showStops(List<Stop> stops) {
        adapter.populate(stops);
    }
}
