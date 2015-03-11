package com.dgsd.android.sydtrip.mvp.view;

import com.dgsd.sydtrip.model.StopPair;

import java.util.List;

public interface FavouritesView extends MVPView {

    public void showFavourites(List<StopPair> favourites);

    public void showEmptyMessage(String message);

    public void showLoading();

    public void hideLoading();

    public void addNewFavourite();

}
