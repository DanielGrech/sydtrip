package com.dgsd.android.sydtrip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dgsd.android.sydtrip.R;
import com.dgsd.android.sydtrip.util.RecyclerViewOnClickListener;
import com.dgsd.android.sydtrip.view.RoutingResultListItemView;
import com.dgsd.sydtrip.routing.model.RoutingItinery;

import java.util.List;

public class RoutingResultAdapter extends RecyclerView.Adapter<IntelligentRecyclerViewHolder<RoutingResultListItemView>> {

    private List<RoutingItinery> results;

    private final RecyclerViewOnClickListener<RoutingResultListItemView> onClickListener;

    public RoutingResultAdapter(RecyclerViewOnClickListener<RoutingResultListItemView> onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public IntelligentRecyclerViewHolder<RoutingResultListItemView> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IntelligentRecyclerViewHolder<>(parent, R.layout.li_routing_result, onClickListener);
    }

    @Override
    public int getItemCount() {
        return results == null ? 0 : results.size();
    }

    @Override
    public void onBindViewHolder(IntelligentRecyclerViewHolder<RoutingResultListItemView> holder, int position) {
        holder.getView().populate(results.get(position));
    }

    public RoutingItinery get(int position) {
        return results == null || position >= results.size() ? null : results.get(position);
    }

    public void populate(List<RoutingItinery> results) {
        this.results = results;
        notifyDataSetChanged();
    }
}
