package com.dgsd.android.sydtrip.activity;

import android.os.Bundle;

import com.dgsd.android.sydtrip.R;

public class AddFavouriteActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_favourite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
