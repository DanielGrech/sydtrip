package com.dgsd.android.sydtrip.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dgsd.android.sydtrip.R;
import com.dgsd.android.sydtrip.fragment.RoutingResultListFragment;

public class RoutingResultListActivity extends BaseActivity {

    public static final String EXTRA_ORIGIN_STOP_ID = "_origin";
    public static final String EXTRA_DEST_STOP_ID = "_dest";

    private int orig;
    private int dest;

    public static Intent createIntent(Context context, int orig, int dest) {
        return new Intent(context, RoutingResultListActivity.class)
                .putExtra(EXTRA_ORIGIN_STOP_ID, orig)
                .putExtra(EXTRA_DEST_STOP_ID, dest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_trip_result_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.orig = getIntent().getIntExtra(EXTRA_ORIGIN_STOP_ID, -1);
        this.dest = getIntent().getIntExtra(EXTRA_DEST_STOP_ID, -1);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, RoutingResultListFragment.newInstance(orig, dest))
                .commit();
    }
}
