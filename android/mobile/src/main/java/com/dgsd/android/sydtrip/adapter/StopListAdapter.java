package com.dgsd.android.sydtrip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dgsd.android.sydtrip.R;
import com.dgsd.android.sydtrip.view.StopListItemView;
import com.dgsd.sydtrip.model.Stop;

import java.util.List;

public class StopListAdapter extends RecyclerView.Adapter<IntelligentRecyclerViewHolder<StopListItemView>> {

    private List<Stop> stops;

    @Override
    public IntelligentRecyclerViewHolder<StopListItemView> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IntelligentRecyclerViewHolder<>(parent, R.layout.li_stop_list, null);
    }

    @Override
    public int getItemCount() {
        return stops == null ? 0 : stops.size();
    }

    @Override
    public void onBindViewHolder(IntelligentRecyclerViewHolder<StopListItemView> holder, int position) {
        holder.getView().populate(stops.get(position));
    }

    public void populate(List<Stop> stops) {
        this.stops = stops;
        notifyDataSetChanged();
    }
}
