package com.dgsd.android.sydtrip.activity;

import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.dgsd.android.sydtrip.STApp;

abstract class BaseActivity extends ActionBarActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected STApp getApp() {
        return (STApp) getApplication();
    }
}
