package com.dgsd.android.sydtrip.fragment;

import android.content.Context;
import android.content.Intent;

import com.dgsd.android.sydtrip.R;
import com.dgsd.android.sydtrip.activity.RoutingResultListActivity;
import com.dgsd.android.sydtrip.mvp.presenter.FavouritesPresenter;
import com.dgsd.android.sydtrip.mvp.view.FavouritesView;
import com.dgsd.android.sydtrip.util.UiUtils;
import com.dgsd.sydtrip.model.StopPair;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

public class FavouritesFragment extends PresentableFragment<FavouritesPresenter> implements FavouritesView {

    @InjectView(R.id.fab)
    AddFloatingActionButton fab;

    FavouritesPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_favourites;
    }

    @Override
    public void showFavourites(List<StopPair> favourites) {

    }

    @Override
    public void showEmptyMessage(String message) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void addNewFavourite() {
//        final Intent intent = new Intent(getContext(), AddFavouriteActivity.class);
        final Intent intent = RoutingResultListActivity.createIntent(getActivity(), 4352, 4008);
        UiUtils.startActivity(fab, intent);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    protected FavouritesPresenter getPresenter() {
        if (presenter == null) {
            presenter = new FavouritesPresenter(this);
        }

        return presenter;
    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        getPresenter().openNewTrip();
    }
}
