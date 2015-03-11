package com.dgsd.android.sydtrip.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgsd.android.sydtrip.util.RecyclerViewOnClickListener;

class IntelligentRecyclerViewHolder<T extends View> extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final RecyclerViewOnClickListener<T> clickListener;

    public IntelligentRecyclerViewHolder(ViewGroup parent, @LayoutRes int layoutId,
                                         RecyclerViewOnClickListener<T> clickListener) {
        this((T) LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false),
                clickListener);
    }

    public IntelligentRecyclerViewHolder(T itemView, RecyclerViewOnClickListener<T> clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        if (clickListener != null) {
            itemView.setOnClickListener(this);
        }
    }

    public T getView() {
        return (T) itemView;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick((T) v, getPosition());
    }
}
