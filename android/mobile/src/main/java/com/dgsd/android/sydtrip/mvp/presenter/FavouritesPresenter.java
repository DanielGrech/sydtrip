package com.dgsd.android.sydtrip.mvp.presenter;

import android.support.annotation.NonNull;

import com.dgsd.android.sydtrip.mvp.view.FavouritesView;
import com.dgsd.sydtrip.model.StopPair;

public class FavouritesPresenter extends Presenter<FavouritesView> {

    public FavouritesPresenter(@NonNull FavouritesView view) {
        super(view);
    }

    public void openFavourite(StopPair stops) {

    }

    public void openNewTrip() {
        getView().addNewFavourite();
    }

    public void openSettings() {

    }
}
