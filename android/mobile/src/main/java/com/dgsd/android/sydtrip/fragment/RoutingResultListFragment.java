package com.dgsd.android.sydtrip.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dgsd.android.sydtrip.R;
import com.dgsd.android.sydtrip.adapter.RoutingResultAdapter;
import com.dgsd.android.sydtrip.mvp.presenter.RoutingResultListPresenter;
import com.dgsd.android.sydtrip.mvp.view.RoutingResultListMVPView;
import com.dgsd.android.sydtrip.util.RecyclerViewOnClickListener;
import com.dgsd.android.sydtrip.view.RoutingResultListItemView;
import com.dgsd.sydtrip.routing.model.RoutingItinery;

import java.util.List;

import butterknife.InjectView;
import timber.log.Timber;

public class RoutingResultListFragment extends PresentableFragment<RoutingResultListPresenter>
        implements RoutingResultListMVPView, RecyclerViewOnClickListener<RoutingResultListItemView> {

    private static final String KEY_ORIG = "_orig";
    private static final String KEY_DEST = "_dest";

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RoutingResultAdapter adapter;

    private RoutingResultListPresenter presenter;

    public static RoutingResultListFragment newInstance(int orig, int dest) {
        final RoutingResultListFragment frag = new RoutingResultListFragment();

        final Bundle args = new Bundle();
        args.putInt(KEY_ORIG, orig);
        args.putInt(KEY_DEST, dest);
        frag.setArguments(args);

        return frag;
    }

    @Override
    protected RoutingResultListPresenter getPresenter() {
        if (presenter == null) {
            presenter = new RoutingResultListPresenter(this, getApp().getDataSource());
        }

        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orig = -1;
        int dest = -1;
        if (getArguments() != null) {
            orig = getArguments().getInt(KEY_ORIG);
            dest = getArguments().getInt(KEY_DEST);
        }

        if (orig < 0 || dest < 0) {
            throw new IllegalStateException("Must pass origin & destination ids!");
        }

        getPresenter().setRoutingOptions(orig, dest);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_routing_result_list;
    }

    @Override
    protected void onCreateView(View rootView, Bundle savedInstanceState) {
        super.onCreateView(rootView, savedInstanceState);

        adapter = new RoutingResultAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void showResults(List<RoutingItinery> results) {
        adapter.populate(results);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onClick(RoutingResultListItemView view, int position) {
        final RoutingItinery itinery = adapter.get(position);
        if (itinery != null) {
            Timber.d("Clicked on %s", itinery);
        }
    }
}
